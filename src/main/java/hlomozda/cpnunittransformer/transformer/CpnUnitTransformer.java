package hlomozda.cpnunittransformer.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import hlomozda.cpnunittransformer.cpn.Arc;
import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;
import hlomozda.cpnunittransformer.cpn.Page;
import hlomozda.cpnunittransformer.cpn.Place;
import hlomozda.cpnunittransformer.cpn.Transition;
import hlomozda.cpnunittransformer.geom.Point;
import static hlomozda.cpnunittransformer.definitions.CpnMlDefinitions.*;

public class CpnUnitTransformer implements CpnTransformer {

    /**
     * Offset values to avoid cloned places, transitions, and their markings
     * stacking into a single mutually obscuring piles. 
     */    
    private static final double CLONES_Y_STEP = 100.0;
    private static final double MARKINGS_Y_STEP = 33.0;
    
    /**
     * A suffix to mark newly created signal place supplementing a place with output inhibitor arcs.
     */
    private static final String SIGNAL_FLAG = "_signal";
    
    /**
     * A randomly generated ID to mark already unfolded places and transitions to avoid exponential growth
     * of their numbers.
     */
    private static final String UNFOLDED_FLAG = "_" + UUID.randomUUID().toString();
    
    /**
     * A regexp to filter out places marked with natural numbers 
     * in order to mark them with the same quantities of UNIT tokens.
     */
    private static final String REGEXP_NON_ZERO_NUMBERS = "^[1-9][0-9]*$";

    /* (non-Javadoc)
     * @see hlomozda.cpnunittransformer.transformer.CpnTransformer#transform(hlomozda.cpnunittransformer.cpn.ColoredPetriNet)
     */
    @Override
    public ColoredPetriNet transform(final ColoredPetriNet cpn) {
        transformDeclarations(cpn);
        transformPages(cpn);        

        return cpn;
    }
    
    /**
     * Substitutes declarations block of the original parsed Colored Petri Net 
     * with the one containing only colset UNIT and a variable u of type UNIT.
     * 
     * @param cpn - a parsed Colored Petri Net to be transformed
     * 
     */
    private void transformDeclarations(final ColoredPetriNet cpn) {
        List<String> declarations = new ArrayList<>();
        declarations.add(DECLARATION_COLSET_UNIT);
        declarations.add(DECLARATION_VAR_U);

        cpn.setDeclarations(declarations);
    }

    /**
     * Substitutes pages block of the original parsed Colored Petri Net 
     * with the one containing pages with transformed places, transitions, and arcs.
     * 
     * @param cpn - a parsed Colored Petri Net to be transformed
     * 
     */
    private void transformPages(final ColoredPetriNet cpn) {
        List<Page> pages = cpn.getPages();
        
        pages.stream().forEach(this::mergeInputOutputArcPairs);
        pages.stream().forEach(this::transformInhibitorArcs);
        pages.stream().forEach(this::transformListPlaces);
        pages.stream().forEach(this::transformTransitions);
        pages.stream().forEach(this::transformArcs);
        pages.stream().forEach(this::cleanUp);

        cpn.setPages(pages);
    }
    
    /**
     * Replaces symmetrical pairs of input/output arcs between the place and the transition with
     * single bi-directional arcs.
     * 
     * @param page - current CPN page
     */
    private void mergeInputOutputArcPairs(final Page page) {
        page.getPlaces().stream()
            .forEach(p -> {
                List<Arc> arcsFromPlace = page.getArcs().stream()
                    .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_TRANS))
                    .collect(Collectors.toList());
                List<Arc> arcsToPlace = page.getArcs().stream()
                        .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_PLACE))
                        .collect(Collectors.toList());
                if (arcsFromPlace.size() > 0 && arcsToPlace.size() > 0) {
                    for (Arc afp : arcsFromPlace) {
                        arcsToPlace.stream()
                            .filter(a -> a.getTransition().equals(afp.getTransition()))
                            .findFirst().ifPresent(a -> {
                                Arc newArc = new Arc(a);
                                newArc.setOrientation(Arc.Orientation.BOTH_DIR);
                                arcsToPlace.remove(a);
                                
                                List<Arc> allArcs = page.getArcs(); 
                                allArcs.remove(afp);
                                allArcs.remove(a);
                                
                                page.setArcs(allArcs);
                                page.addArc(newArc);
                            });
                    }
                }
            });       
    }
    
    /**
     * Replaces inhibitor arcs with output arcs from newly created signal places, which
     * are then connected to original places' output transitions to mimic inhibiting behavior.
     * 
     * @param page - current CPN page
     */
    private void transformInhibitorArcs(final Page page) {
        List<Arc> inhibitorArcs = page.getArcs().stream()
                .filter(arc -> arc.getOrientation().equals(Arc.Orientation.INHIBITOR))
                .collect(Collectors.toList());
        
        if (inhibitorArcs.size() > 0) {
            List<Place> createdPlaces = new ArrayList<>();
            List<Arc> createdArcs = new ArrayList<>();
            Map<Place, List<Transition>> placeToInhibitedTransitionsMap = new HashMap<>();

            inhibitorArcs.stream().forEach(inhibitorArc -> {
                if (placeToInhibitedTransitionsMap.containsKey(inhibitorArc.getPlace())) {
                    placeToInhibitedTransitionsMap.get(inhibitorArc.getPlace()).add(inhibitorArc.getTransition());
                } else {
                    placeToInhibitedTransitionsMap.put(inhibitorArc.getPlace(), new ArrayList<>());
                    placeToInhibitedTransitionsMap.get(inhibitorArc.getPlace()).add(inhibitorArc.getTransition());
                }
            });

            placeToInhibitedTransitionsMap.keySet().stream().forEach(place -> {
                Place signalPlace = new Place(place);
                signalPlace.getName().setText(signalPlace.getNameText() + SIGNAL_FLAG);
                signalPlace.getShape()
                    .setPosition(new Point(signalPlace.getShape().getPosX(), 
                            signalPlace.getShape().getPosY() - CLONES_Y_STEP));
                if (place.getInitMark().getText().isEmpty() || place.getInitMark().getText().contentEquals("0")) {
                    signalPlace.getInitMark().setText("1");
                } else {
                    signalPlace.getInitMark().setText("");
                }
                signalPlace.getInitMark()
                    .setPosition(new Point(signalPlace.getShape().getPosX(), 
                            signalPlace.getShape().getPosY() + MARKINGS_Y_STEP));
                createdPlaces.add(signalPlace);
                
                placeToInhibitedTransitionsMap.get(place).stream().forEach(trans -> {
                    Arc newArc = new Arc(inhibitorArcs.get(0));
                    newArc.setPlace(signalPlace);
                    newArc.setTransition(trans);
                    newArc.setOrientation(Arc.Orientation.TO_TRANS);
                    createdArcs.add(newArc);
                });
                
                page.getArcs().stream()
                    .filter(arc -> arc.getPlace().equals(place) 
                            && arc.getOrientation().equals(Arc.Orientation.TO_TRANS))
                    .forEach(arc -> {
                        Arc newArc = new Arc(arc);
                        newArc.setPlace(signalPlace);
                        newArc.setOrientation(Arc.Orientation.TO_PLACE);
                        createdArcs.add(newArc);
                    });                
            });
            
            createdPlaces.stream().forEach(createdPlace -> page.addPlace(createdPlace));
            createdArcs.stream().forEach(createdArc -> page.addArc(createdArc));
            
            List<Arc> allArcs = page.getArcs();
            inhibitorArcs.stream().forEach(inhibitorArc -> allArcs.remove(inhibitorArc));
            page.setArcs(allArcs);
        }
        
        return;
    }

    /**
     * Transforms places of the given page. Places containing collection tokens (lists)
     * are unfolded into N places, where N is the number of elements in the list token.
     * <p>
     * All places are marked with type UNIT. 
     * <p>
     * If the place is marked with integer X, 
     * it retains its marking which turns it into a place holding X unit tokens.
     * <p>
     * Other marked places get a single unit token.
     * <p>
     * @param page - a page to get its places transformed
     * <p> 
     */
    private void transformListPlaces(final Page page) {
        List<Place> places = page.getPlaces();

        List<Place> listPlaces = 
                places.stream().filter(p -> p.getInitMark().getText()
                        .startsWith(LIST_SET_BRACKETS.substring(0, LIST_SET_BRACKETS.length() / 2)))
                        .collect(Collectors.toList());

        listPlaces.stream().forEach(lp -> places.remove(lp));
        
        places.addAll(unfoldListPlaces(page, listPlaces));

        places.stream().forEach(p -> p.getType().setText(TYPE_UNIT));
        places.stream().filter(p -> !p.getInitMark().getText().isEmpty() 
                && !p.getInitMark().getText().matches(REGEXP_NON_ZERO_NUMBERS))
                .forEach(p -> p.getInitMark()
                        .setText(String.valueOf(splitMarkingIntoTokens(p.getInitMark().getText()).size())));
        
        connectInputNonListPlacesWithUnfoldedTransitions(page)
            .stream().forEach(arc -> page.addArc(arc));

        page.setPlaces(places);
    }

    /**
     * Unfolds places containing collection tokens (lists)
     * into N places, where N is the number of elements in the list token they are marked with.
     * 
     * @param page - current CPN page
     * @param listPlaces - list of places marked with collection tokens 
     * @return list of places containing original places and their clones
     * corresponding to the elements of the collections they were originally marked with 
     */
    private List<Place> unfoldListPlaces(final Page page, final List<Place> listPlaces) {         
        List<Place> unfoldedListPlaces = new ArrayList<>();

        for (Place listPlace : listPlaces) {
            List<String> tokens = splitMarkingIntoTokens(listPlace.getInitMark().getText());
            List<Place> createdPlaces = new ArrayList<>();

            for (int i = 1; i < tokens.size(); i++) {
                Place createdPlace = new Place(listPlace);                
                createdPlace.getName().setText(createdPlace.getNameText() + "_" + (i + 1) + UNFOLDED_FLAG);
                createdPlace.getShape()
                    .setPosition(new Point(createdPlace.getShape().getPosX(), 
                        createdPlace.getShape().getPosY() - CLONES_Y_STEP * i));
                createdPlace.getInitMark().setText(tokens.get(i));
                createdPlace.getInitMark()
                    .setPosition(new Point(createdPlace.getShape().getPosX(), 
                        createdPlace.getShape().getPosY() + MARKINGS_Y_STEP));
                createdPlaces.add(createdPlace);
                unfoldedListPlaces.add(createdPlace);
            }

            listPlace.getName().setText(listPlace.getNameText() + "_1" + UNFOLDED_FLAG);
            listPlace.getInitMark().setText(tokens.get(0));
            listPlace.getInitMark()
                .setPosition(new Point(listPlace.getShape().getPosX(), 
                        listPlace.getShape().getPosY() + MARKINGS_Y_STEP));            
            unfoldedListPlaces.add(listPlace);

            unfoldListPlaceInputOutputTransitions(page, listPlace, createdPlaces);
            
            connectUnfoldedPlacesWithNonUnfoldedOutputTransitions(page, listPlace, createdPlaces)
                .stream().forEach(arc -> page.addArc(arc));
        }

        return unfoldedListPlaces;
    }
    
    /**
     * Splits original collection marking into separate elements. 
     * 
     * @param marking - original marking of the place
     * @return list of strings containing elements of the list the place was originally marked with.   
     */
    private List<String> splitMarkingIntoTokens(final String marking) {
        int unclosedBrackets = 0, tokenStartIndex = 0;
        String markingCopy = marking;
        List<String> res = new ArrayList<>();

        if (markingCopy.startsWith(LIST_SET_BRACKETS.substring(0, LIST_SET_BRACKETS.length() / 2))) {
            markingCopy = markingCopy.substring(LIST_SET_BRACKETS.length() / 2, 
                    markingCopy.length() - LIST_SET_BRACKETS.length() / 2);
        }

        for (int i = 0; i < markingCopy.length(); i++) {
            if (markingCopy.charAt(i) == PRODUCT_BRACKETS.charAt(0)) {
                unclosedBrackets++;
            }
            if (i < markingCopy.length() - 1 && markingCopy.charAt(i) == PRODUCT_BRACKETS.charAt(1)) {
                unclosedBrackets--;
            }
            if (markingCopy.charAt(i) == TOKEN_SET_DELIMITER.charAt(0) && unclosedBrackets == 0) {
                res.add(markingCopy.substring(tokenStartIndex, i).trim());
                tokenStartIndex = i + 1;
            }
            if (i == markingCopy.length() - 1) {
                res.add(markingCopy.substring(tokenStartIndex, i + 1).trim());
            }
        }

        return res;
    }

    /**
     * Unfolds input and output transitions of the unfolded list place creating as many elements as 
     * the number of new places. 
     * 
     * @param page - current CPN page
     * @param listPlace - original place previously marked with collection token
     * @param createdPlaces - list of new places created to hold an element of the original collection marking
     */
    private void unfoldListPlaceInputOutputTransitions (final Page page, final Place listPlace, 
            final List<Place> createdPlaces) {                
        List<Transition> inputOutputTransitions = new ArrayList<>();
        List<Arc> arcsFromAndToPlace = new ArrayList<>();
        List<Transition> currentUnfoldedTransitionSet = new ArrayList<>();
        List<Arc> originalArcs = new ArrayList<>();

        arcsFromAndToPlace.addAll(page.getTransitions().stream()
                .map(tr -> page.getArcs(listPlace, tr))
                .filter(Objects::nonNull)
                .flatMap(listArcs -> listArcs.stream()).distinct()
                .collect(Collectors.toList()));

        inputOutputTransitions.addAll(arcsFromAndToPlace.stream()                
                .map(arc -> arc.getTransition()).distinct().collect(Collectors.toList()));
                
        for (Transition inputOutputTransition : inputOutputTransitions) {
            if (!inputOutputTransition.getNameText().contains(UNFOLDED_FLAG)) {
                currentUnfoldedTransitionSet = new ArrayList<>();
                
                for (int i = 1; i <= createdPlaces.size(); i++) {
                    Transition createdTransition = new Transition(inputOutputTransition);                
                    createdTransition.getName()
                        .setText(createdTransition.getNameText() + "_" + (i + 1) + UNFOLDED_FLAG);
                    createdTransition.getShape()
                        .setPosition(new Point(createdTransition.getShape().getPosX(), 
                            createdTransition.getShape().getPosY() - CLONES_Y_STEP * i));
                    page.addTransition(createdTransition);
                    currentUnfoldedTransitionSet.add(createdTransition);
                }

                inputOutputTransition.getName()
                    .setText(inputOutputTransition.getNameText() + "_1" + UNFOLDED_FLAG);
                
                originalArcs = 
                        arcsFromAndToPlace.stream()
                        .filter(arc -> arc.getTransition().equals(inputOutputTransition))
                        .collect(Collectors.toList());
                
                connectUnfoldedTransitionsWithUnfoldedPlaces(listPlace, createdPlaces, 
                        inputOutputTransition, currentUnfoldedTransitionSet, originalArcs,
                        arcsFromAndToPlace.stream()
                        .anyMatch(arc -> arc.getOrientation().equals(Arc.Orientation.TO_PLACE)
                                || arc.getOrientation().equals(Arc.Orientation.BOTH_DIR)))
                        .stream().forEach(arc -> page.addArc(arc));                
            } else {
                currentUnfoldedTransitionSet = new ArrayList<>();
                currentUnfoldedTransitionSet.addAll(page.getTransitions().stream()
                    .filter(transition 
                        -> transition.getNameText()
                            .contains(inputOutputTransition.getNameText().replace("_1" + UNFOLDED_FLAG, "")))
                    .collect(Collectors.toList()));
                
                originalArcs = 
                        arcsFromAndToPlace.stream()
                        .filter(arc -> arc.getTransition().equals(inputOutputTransition))
                        .collect(Collectors.toList());
                
                connectAllUnfoldedTransitionsWithAllUnfoldedPlaces(listPlace, createdPlaces, 
                        currentUnfoldedTransitionSet, originalArcs)
                            .stream().forEach(arc -> page.addArc(arc));
            }
        }
    }
    
    /**
     * Creates list of arcs connecting newly created transitions with newly created places (on 1-to-1 basis) 
     * for the case when transition was unfolded at the same time as the place.
     * <p>
     * Also creates place-transition arcs connecting unfolded transitions to all 
     * places from the unfolded set except their original input/output place
     * to model mutual exclusion of list options (only if there were input transition-place arcs
     * coming into original place).
     * 
     * @param originalPlace - original place previously marked with collection token
     * @param createdPlaces - newly created places
     * @param originalTransition - original transition
     * @param createdTransitions - newly created transitions
     * @param arcTemplates - arcs between original place marked with collection and original transition 
     * it was connected with
     * @param hasToPlaceArcs - whether there are input transition-place arcs coming into original place 
     * @return list of arcs connecting newly created transitions with newly created places (on 1-to-1 basis)
     */
    private List<Arc> connectUnfoldedTransitionsWithUnfoldedPlaces(final Place originalPlace, 
            final List<Place> createdPlaces, 
            final Transition originalTransition, final List<Transition> createdTransitions, 
            final List<Arc> arcTemplates, final boolean hasToPlaceArcs) {
        List<Arc> additionalArcs = new ArrayList<>();
        
        for (Arc arcTemplate : arcTemplates) {
            for (int i = 0; i < createdPlaces.size(); i++) {
                Arc newArc = new Arc(arcTemplate);
                newArc.setPlace(createdPlaces.get(i));
                newArc.setTransition(createdTransitions.get(i));
                additionalArcs.add(newArc);
            }
        }
        
        if (hasToPlaceArcs) {
            List<Place> involvedPlaces = new ArrayList<>();
            involvedPlaces.add(originalPlace);
            involvedPlaces.addAll(createdPlaces);

            List<Transition> involvedTransitions = new ArrayList<>();
            involvedTransitions.add(originalTransition);
            involvedTransitions.addAll(createdTransitions);

            for (Transition involvedTransition : involvedTransitions) {
                for (Place involvedPlace : involvedPlaces) {
                    String transitionIndex = involvedTransition.getNameText()
                            .substring(0, involvedTransition.getNameText().indexOf(UNFOLDED_FLAG));
                    transitionIndex = transitionIndex.substring(transitionIndex.lastIndexOf("_") + 1);
                    String placeIndex = involvedPlace.getNameText()
                            .substring(0, involvedPlace.getNameText().indexOf(UNFOLDED_FLAG));
                    placeIndex = placeIndex.substring(placeIndex.lastIndexOf("_") + 1);

                    if (!transitionIndex.equals(placeIndex)) {
                        Arc newArc = new Arc(arcTemplates.get(0));
                        newArc.setPlace(involvedPlace);
                        newArc.setTransition(involvedTransition);
                        newArc.setOrientation(Arc.Orientation.BOTH_DIR);
                        additionalArcs.add(newArc);
                    }
                }
            }
        }
        
        return additionalArcs;
    }
    
    /**
     * Creates list of arcs connecting newly created transitions with newly created places (on m-to-n basis)
     * for the case when transition was unfolded as a part of unfolding some other place.
     * 
     * @param originalPlace - original place previously marked with collection token
     * @param createdPlaces - newly created places
     * @param unfoldedTransitions - unfolded transitions (original plus newly created)
     * @param arcTemplates - arcs between original place marked with collection and original transition 
     * it was connected with
     * @return list of arcs connecting newly created transitions with newly created places (on m-to-n basis)
     */
    private List<Arc> connectAllUnfoldedTransitionsWithAllUnfoldedPlaces(final Place originalPlace,
            final List<Place> createdPlaces, final List<Transition> unfoldedTransitions, 
            final List<Arc> arcTemplates) {
        List<Arc> additionalArcs = new ArrayList<>();
        
        for (Arc arcTemplate : arcTemplates) {
            for (int i = 1; i < unfoldedTransitions.size(); i++) {
                Arc newArc = new Arc(arcTemplate);
                newArc.setPlace(originalPlace);
                newArc.setTransition(unfoldedTransitions.get(i));
                additionalArcs.add(newArc);
            }
            
            for (Transition unfoldedTransition : unfoldedTransitions) {
                for (Place createdPlace : createdPlaces) {
                    Arc newArc = new Arc(arcTemplate);
                    newArc.setPlace(createdPlace);
                    newArc.setTransition(unfoldedTransition);
                    additionalArcs.add(newArc);
                }
            }            
        }
        
        return additionalArcs;
    }
    
    /**
     * Creates list of arcs connecting original non-list and thus non-unfolded places with
     * newly created transitions.
     * 
     * @param page - current CPN page
     * @return list of arcs connecting original non-list and thus non-unfolded places with
     * newly created transitions
     */
    private List<Arc> connectInputNonListPlacesWithUnfoldedTransitions (final Page page) {
        List<Transition> originalUnfoldedTransitions = 
                page.getTransitions().stream()
                .filter(tr -> tr.getNameText().contains("_1" + UNFOLDED_FLAG))
                .collect(Collectors.toList());
        
        List<Arc> additionalArcs = new ArrayList<>();
        
        for (Transition oetr : originalUnfoldedTransitions) {
            String groupName = 
                    oetr.getNameText().substring(0, oetr.getNameText().indexOf("_1" + UNFOLDED_FLAG));
            
            List<Transition> createdUnfoldedTransitions = 
                    page.getTransitions().stream()
                    .filter(tr -> tr.getNameText().contains(groupName)
                            && !tr.getNameText().contains("_1" + UNFOLDED_FLAG))
                    .collect(Collectors.toList());
            
            List<Arc> originalArcsFromNonListPlaces = new ArrayList<>();
            
            originalArcsFromNonListPlaces.addAll(page.getPlaces().stream()
                    .filter(place -> !place.getNameText().contains(UNFOLDED_FLAG))
                    .map(place -> page.getArcs(place, oetr))
                    .filter(Objects::nonNull)
                    .flatMap(listArcs -> listArcs.stream()).distinct()
                    .collect(Collectors.toList()));
            
            List<Place> inputNonListPlaces = new ArrayList<>();
            
            inputNonListPlaces.addAll(originalArcsFromNonListPlaces.stream()                    
                    .map(arc -> arc.getPlace())
                    .distinct()
                    .collect(Collectors.toList()));
            
            for (Place inlp : inputNonListPlaces) {
                List<Arc> arcTemplates = new ArrayList<>();
                arcTemplates.addAll(originalArcsFromNonListPlaces.stream()
                        .filter(arc -> arc.getPlace().equals(inlp)).distinct()
                        .collect(Collectors.toList()));
                                
                for (Arc arcTemplate : arcTemplates) {
                    for (Transition cetr : createdUnfoldedTransitions) {
                        Arc newArc = new Arc(arcTemplate);
                        newArc.setPlace(inlp);
                        newArc.setTransition(cetr);
                        additionalArcs.add(newArc);
                    }
                }
            }                    
        }
        
        return additionalArcs;
    }
    
    /**
     * Creates list of arcs connecting newly created places with
     * output and non-unfolded transitions (on 1-to-1 basis).
     * 
     * @param page - current CPN page
     * @param originalPlace - original place previously marked with collection token
     * @param createdPlaces - newly created places
     * @return list of arcs connecting newly created places with
     * output and non-unfolded transitions (on 1-to-1 basis)
     */
    private List<Arc> connectUnfoldedPlacesWithNonUnfoldedOutputTransitions(final Page page, 
            final Place originalPlace, final List<Place> createdPlaces) {
        List<Transition> outputTransitions = new ArrayList<>();
        List<Arc> arcsFromAndToPlace = new ArrayList<>();        
        List<Arc> originalArcs = new ArrayList<>();
        List<Arc> additionalArcs = new ArrayList<>();

        arcsFromAndToPlace.addAll(page.getTransitions().stream()
                .map(tr -> page.getArcs(originalPlace, tr))
                .filter(Objects::nonNull)
                .flatMap(listArcs -> listArcs.stream()).distinct()
                .collect(Collectors.toList()));

        outputTransitions.addAll(arcsFromAndToPlace.stream()
                .filter(arc -> 
                    arc.getOrientation().equals(Arc.Orientation.TO_TRANS)
                    || arc.getOrientation().equals(Arc.Orientation.BOTH_DIR))
                .map(arc -> arc.getTransition())
                .filter(tr -> !tr.getNameText().contains(UNFOLDED_FLAG))
                .distinct().collect(Collectors.toList()));
        
        for (Transition outputTransition : outputTransitions) {
            originalArcs = 
                arcsFromAndToPlace.stream()
                    .filter(arc -> arc.getTransition().equals(outputTransition))
                    .collect(Collectors.toList());
            
            for (Place createdPlace : createdPlaces) {
                for (Arc originalArc : originalArcs) {
                    Arc newArc = new Arc(originalArc);
                    newArc.setPlace(createdPlace);
                    newArc.setTransition(outputTransition);
                    additionalArcs.add(newArc);
                }
            }
        }        
        
        return additionalArcs;
    }

    /**
     * Transforms transitions on the current page by removing their conditions, codes, times and priorities. 
     * 
     * @param page - current CPN page
     */
    private void transformTransitions(final Page page) {
        List<Transition> transitions = page.getTransitions();        
        
        transitions.stream().filter(t -> !t.getCondition().getText().isEmpty())
            .forEach(t -> t.getCondition().setText(""));
        transitions.stream().filter(t -> !t.getCode().getText().isEmpty())
            .forEach(t -> t.getCode().setText(""));
        transitions.stream().filter(t -> !t.getTime().getText().isEmpty())
            .forEach(t -> t.getTime().setText(""));
        transitions.stream().filter(t -> !t.getPriority().getText().isEmpty())
            .forEach(t -> t.getPriority().setText(""));

        page.setTransitions(transitions);
    }   

    /**
     * Transforms arcs on the original page by replacing their annotations with empty
     * strings (equal to using unit variable u). Then, if a place is marked with natural number
     * N > 1, its input and output arcs are annotated with this number (equal to creating N arcs
     * to model multiplicity). Annotation is placed in the middle of the arc.
     * 
     * @param page - current CPN page
     */
    private void transformArcs(final Page page) {
        page.getArcs().stream()
            .filter(arc -> !arc.getAnnotation().getText().isEmpty())
            .forEach(arc -> arc.getAnnotation().setText(""));
        
        page.getArcs().stream()
            .filter(arc -> arc.getPlace().getInitMark().getText().matches(REGEXP_NON_ZERO_NUMBERS)
                    && !arc.getPlace().getInitMark().getText().contentEquals("1"))
            .forEach(arc -> {
                arc.getAnnotation().setText(arc.getPlace().getInitMark().getText());
                arc.getAnnotation().setPosition(
                    new Point(arc.getPlace().getShape().getPosX() 
                            - (arc.getPlace().getShape().getPosX() - arc.getTransition().getShape().getPosX()) / 2, 
                        arc.getPlace().getShape().getPosY() 
                            - (arc.getPlace().getShape().getPosY() - arc.getTransition().getShape().getPosY()) / 2));
            });
    }
    
    /**
     * Final stage of CPN transformation. Removal of the generated UUID's from 
     * additional transitions and places. Sorting of places and transitions by their names
     * in ascending order.
     * 
     * @param page - current CPN page
     */
    private void cleanUp(final Page page) {        
        page.getPlaces().stream()
            .forEach(p -> p.getName().setText(p.getNameText().replace(UNFOLDED_FLAG, "")));
        page.setPlaces(
                page.getPlaces().stream().sorted(Place.PlaceNameComparator)
                    .collect(Collectors.toList()));
        
        page.getTransitions().stream()
            .forEach(t -> t.getName().setText(t.getNameText().replace(UNFOLDED_FLAG, "")));
        page.setTransitions(
                page.getTransitions().stream().sorted(Transition.TransitionNameComparator)
                    .collect(Collectors.toList()));
    }    
}
