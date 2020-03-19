package hlomozda.cpnbdd.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hlomozda.cpnio.cpn.*;
import hlomozda.cpnio.definitions.ArcXmlDefinitions;

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
        Map<String, List<String>> scenario = new HashMap<>();

        List<Place> allPlaces = page.getPlaces();
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
            tieredPlaces.add(new ArrayList<>());
            List<Place> processedPlaces = new ArrayList<>();
            for (Transition currentTransition : tieredTransitions.get(index)) {
                if (!visitedTransitions.contains(currentTransition)) {
                    visitedTransitions.add(currentTransition);
                    for (Arc currentArc : arcsFromTransition(page, currentTransition)) {
                        Place targetPlace = currentArc.getPlace();
                        if (!processedPlaces.contains(targetPlace)) {
                            tieredPlaces.get(index + 1).add(targetPlace);
                            processedPlaces.add(targetPlace);
                        }
                    }
                }
            }
            index++;
        }

        List<List<String>> conditions = new ArrayList<>();
        conditions.add(new ArrayList<>());

        List<String> examples = new ArrayList<>();

        for (int i = 0; i < tieredPlaces.size(); i++) {

        }

        List<List<String>> actions = new ArrayList<>();
        actions.add(new ArrayList<>());

        for (int i = 0; i < tieredTransitions.size(); i++) {

        }

//        page.getTransitions().forEach(t -> actions.add(t.getNameValue()));

        scenario.put("Name", Collections.singletonList(page.getName()));
        scenario.put("Given", conditions.get(0));
        scenario.put("When", actions.get(0));
//        scenario.put("Then", postconditions);
        scenario.put("Examples", examples);

        return scenario;
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
