package hlomozda.cpnbdd.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hlomozda.cpnio.cpn.*;
import hlomozda.cpnio.definitions.ArcXmlDefinitions;
import hlomozda.cpnio.definitions.PlaceXmlDefinitions;

public class CpnBddProcessor implements CpnProcessor<Map<String, List<String>>> {

    @Override
    public List<Map<String, List<String>>> process(ColoredPetriNet cpn) {
        List<Map<String, List<String>>> result = new ArrayList<>();

        for (Page page : cpn.getPages()) {
            result.add(processPage(page));
        }

        return result;
    }

    private Map<String, List<String>> processPage(final Page page) {
        Map<String, List<String>> scenario = new LinkedHashMap<>();

        List<Place> allPlaces = page.getPlaces().stream().filter(p -> !isNotProcessiblePlace(p)).collect(Collectors.toList());
        List<Place> dataPlaces =
                page.getPlaces().stream().filter(p -> p.getNameValue().contains(PlaceXmlDefinitions.TYPE_DATA)).collect(Collectors.toList());
        allPlaces.removeIf(this::isNotProcessiblePlace);
        List<Place> visitedPlaces = new ArrayList<>();

        List<List<Place>> tieredPlaces = new ArrayList<>();
        tieredPlaces.add(new ArrayList<>());

        //create tier of preconditions (places with no input arcs) and
        allPlaces.forEach(p -> {
            if (arcsToPlace(page, p).isEmpty() && !arcsFromPlace(page, p).isEmpty()) {
                tieredPlaces.get(0).add(p);
            }
        });

        List<Transition> visitedTransitions = new ArrayList<>();
        List<List<Transition>> tieredTransitions = new ArrayList<>();

        int index = 0;
        while (visitedPlaces.size() < allPlaces.size()) {
            tieredTransitions.add(new ArrayList<>());
            List<Transition> processedTransitions = new ArrayList<>();
            for (Place currentPlace : tieredPlaces.get(index)) {
                if (!visitedPlaces.contains(currentPlace)) {
                    visitedPlaces.add(currentPlace);
                    for (Arc currentArc : arcsFromPlace(page, currentPlace)) {
                        Transition targetTransition = currentArc.getTransition();
                        if (!processedTransitions.contains(targetTransition)) {
                            tieredTransitions.get(index).add(targetTransition);
                            processedTransitions.add(targetTransition);
                        }
                    }
                }
            }

            if (visitedPlaces.size() < allPlaces.size()) {
                tieredPlaces.add(new ArrayList<>());
                List<Place> processedPlaces = new ArrayList<>();
                for (Transition currentTransition : tieredTransitions.get(index)) {
                    if (!visitedTransitions.contains(currentTransition)) {
                        visitedTransitions.add(currentTransition);
                        for (Arc currentArc : arcsFromTransition(page, currentTransition)) {
                            Place targetPlace = currentArc.getPlace();
                            if (!isNotProcessiblePlace(targetPlace) && !processedPlaces.contains(targetPlace)) {
                                tieredPlaces.get(index + 1).add(targetPlace);
                                processedPlaces.add(targetPlace);
                            }
                        }
                    }
                }
                index++;
            }
        }

        if (tieredTransitions.get(tieredTransitions.size() - 1).isEmpty()) {
            tieredTransitions.remove(tieredTransitions.size() - 1);
        }

        List<List<String>> conditions = new ArrayList<>();
        List<String> examples = new ArrayList<>();

        for (int i = 0; i < tieredPlaces.size(); i++) {
            conditions.add(new ArrayList<>());
            List<Transition> processedTransitions = new ArrayList<>();
            for (Place currentPlace : tieredPlaces.get(i)) {
                StringBuilder statement = new StringBuilder(currentPlace.getNameValue());
                for (Arc currentArc : arcsFromPlace(page, currentPlace)) {
                    String arcAnnotation = currentArc.getAnnotation().getValue();
                    if (!ArcXmlDefinitions.DEFAULT_ANNOTATION.equals(arcAnnotation)) {
                        statement.append(" with parameters: <").append(arcAnnotation).append(">");
                    }

                    Transition targetTransition = currentArc.getTransition();
                    if (!processedTransitions.contains(targetTransition)) {
                        if (!targetTransition.getCondition().getValue().isEmpty()) {
                            conditions.get(i).add(targetTransition.getCondition().getValue());
                        }
                        processedTransitions.add(targetTransition);
                    }
                }
                conditions.get(i).add(statement.toString());

                if (!currentPlace.getInitMark().getValue().isEmpty()) {
                    examples.addAll(processInitMarking(page, currentPlace));
                }
            }
        }

        List<List<String>> actions = new ArrayList<>();

        for (int i = 0; i < tieredTransitions.size(); i++) {
            actions.add(new ArrayList<>());
            for (Transition currentTransition : tieredTransitions.get(i)) {
                actions.get(i).add(currentTransition.getNameValue());
            }
        }

        scenario.put("Name", Collections.singletonList(page.getName()));

        scenario.put("Given", conditions.get(0));
        for (int i = 0; i < tieredTransitions.size(); i++) {
            scenario.put("When" + i, actions.get(i));
            if (conditions.size() > i + 1) {
                scenario.put("Then" + i, conditions.get(i + 1));
            } else {
                List<String> errorMessage =
                        new ArrayList<>(Collections.singletonList("Too few place tiers! Try to modify your CPN to have dedicated post-conditions!"));
                scenario.put("Error" + i, errorMessage);
            }
        }

        dataPlaces.forEach(p -> {
            if (!p.getInitMark().getValue().isEmpty()) {
                examples.addAll(processInitMarking(page, p));
            }
        });
        scenario.put("Examples", examples);

        return scenario;
    }

    private boolean isNotProcessiblePlace(final Place place) {
        return place.getNameValue().contains(PlaceXmlDefinitions.TYPE_AUXILLARY)
                || place.getNameValue().contains(PlaceXmlDefinitions.TYPE_DATA);
    }

    private List<Arc> arcsFromPlace(final Page page, final Place place) {
        return page.getArcs().stream()
                .filter(a -> a.getPlace().equals(place)
                        && (a.getOrientation().equals(Arc.Orientation.TO_TRANS) || a.getOrientation().equals(Arc.Orientation.BOTH_DIR)))
                .collect(Collectors.toList());
    }

    private List<Arc> arcsToPlace(final Page page, final Place place) {
        return page.getArcs().stream()
                .filter(a -> a.getPlace().equals(place)
                        && (a.getOrientation().equals(Arc.Orientation.TO_PLACE) || a.getOrientation().equals(Arc.Orientation.BOTH_DIR)))
                .collect(Collectors.toList());
    }

    private List<Arc> arcsFromTransition(final Page page, final Transition transition) {
        return page.getArcs().stream()
                .filter(a -> a.getTransition().equals(transition)
                        && (a.getOrientation().equals(Arc.Orientation.TO_PLACE) || a.getOrientation().equals(Arc.Orientation.BOTH_DIR)))
                .collect(Collectors.toList());
    }

    private List<String> processInitMarking(final Page page, final Place place) {
        List<String> result = new ArrayList<>();

        if (place.getType().getValue().equals("UNIT")) {
            return result;
        }

        String variableName = page.getArcs().stream()
                .filter(a -> a.getPlace().equals(place)
                        && (a.getOrientation().equals(Arc.Orientation.TO_TRANS)
                        || a.getOrientation().equals(Arc.Orientation.BOTH_DIR)))
                .collect(Collectors.toList()).get(0).getAnnotation().getValue();
        String variableValue = place.getInitMark().getValue().replaceAll("\\R", " ");

        result.add(variableName);
        result.add(variableValue);
        return result;
    }
}
