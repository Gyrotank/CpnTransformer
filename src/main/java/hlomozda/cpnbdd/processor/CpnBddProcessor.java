package hlomozda.cpnbdd.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hlomozda.cpnio.cpn.Arc;
import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.cpn.Page;
import hlomozda.cpnio.cpn.Place;
import hlomozda.cpnio.definitions.ArcXmlDefinitions;

public class CpnBddProcessor implements CpnProcessor<Map<String, List<String>>> {

    @Override
    public List<Map<String, List<String>>> process(ColoredPetriNet cpn) {
        List<Map<String, List<String>>> result = new ArrayList<>();

        for (Page page : cpn.getPages()) {
            Map<String, List<String>> scenario = new HashMap<>();
            List<String> preconditions = new ArrayList<>();
            List<String> actions = new ArrayList<>();
            List<String> postconditions = new ArrayList<>();
            List<String> examples = new ArrayList<>();

            page.getPlaces().forEach(p -> {
                List<Arc> arcsFromPlace = page.getArcs().stream()
                        .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_TRANS))
                        .collect(Collectors.toList());
                List<Arc> arcsToPlace = page.getArcs().stream()
                        .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_PLACE))
                        .collect(Collectors.toList());
                if (!arcsFromPlace.isEmpty()) {
                    StringBuilder statement = new StringBuilder(p.getNameValue());
                    String arcAnnotation = arcsFromPlace.get(0).getAnnotation().getValue();
                    if (!ArcXmlDefinitions.DEFAULT_ANNOTATION.equals(arcAnnotation)) {
                        statement.append(" with parameters: <").append(arcAnnotation).append(">");
                    }
                    preconditions.add(statement.toString());
                }
                if (!arcsToPlace.isEmpty()) {
                    postconditions.add(p.getNameValue());
                }
                if (!p.getInitMark().getValue().isEmpty()) {
                    examples.addAll(processInitMarking(page, p));
                }
            });

            page.getTransitions().forEach(t -> actions.add(t.getNameValue()));

            scenario.put("Name", Collections.singletonList(page.getName()));
            scenario.put("Given", preconditions);
            scenario.put("When", actions);
            scenario.put("Then", postconditions);
            scenario.put("Examples", examples);
            result.add(scenario);
        }

        return result;
    }

    private List<String> processInitMarking(final Page page, final Place place) {
        List<String> result = new ArrayList<>();

        if (place.getType().getValue().equals("UNIT")) {
            return result;
        }

        String variableName = page.getArcs().stream()
                .filter(a -> a.getPlace().equals(place) && a.getOrientation().equals(Arc.Orientation.TO_TRANS))
                .collect(Collectors.toList()).get(0).getAnnotation().getValue();
        String variableValue = place.getInitMark().getValue();

        result.add(variableName);
        result.add(variableValue);
        return result;
    }
}
