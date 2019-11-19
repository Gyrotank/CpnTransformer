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

public class CpnBddProcessor implements CpnProcessor {

    @Override
    public List<Map<String, List<String>>> process(ColoredPetriNet cpn) {
        List<Map<String, List<String>>> result = new ArrayList<>();

        for (Page page : cpn.getPages()) {
            Map<String, List<String>> scenario = new HashMap<>();
            List<String> preconditions = new ArrayList<>();
            List<String> actions = new ArrayList<>();
            List<String> postconditions = new ArrayList<>();

            page.getPlaces().forEach(p -> {
                List<Arc> arcsFromPlace = page.getArcs().stream()
                        .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_TRANS))
                        .collect(Collectors.toList());
                List<Arc> arcsToPlace = page.getArcs().stream()
                        .filter(a -> a.getPlace().equals(p) && a.getOrientation().equals(Arc.Orientation.TO_PLACE))
                        .collect(Collectors.toList());
                if (!arcsFromPlace.isEmpty()) {
                    preconditions.add(p.getNameValue());
                }
                if (!arcsToPlace.isEmpty()) {
                    postconditions.add(p.getNameValue());
                }
            });

            page.getTransitions().forEach(t -> actions.add(t.getNameValue()));

            scenario.put("Name", Collections.singletonList(page.getName()));
            scenario.put("Given", preconditions);
            scenario.put("When", actions);
            scenario.put("Then", postconditions);
            result.add(scenario);
        }

        return result;
    }
}
