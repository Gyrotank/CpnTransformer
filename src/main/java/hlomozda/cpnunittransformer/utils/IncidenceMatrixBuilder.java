package hlomozda.cpnunittransformer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import hlomozda.cpnunittransformer.cpn.Arc;
import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;
import hlomozda.cpnunittransformer.cpn.Page;

/**
 * Builder of incidence matrix for a given Colored Petri net.  
 *
 */
public class IncidenceMatrixBuilder {

    private IncidenceMatrixBuilder() {}

    public static List<Integer[][]> buildMatrix(final ColoredPetriNet cpn) {
        ArrayList<Integer[][]> res = new ArrayList<>();
        cpn.getPages().forEach(page -> {
            if (!page.getArcs().isEmpty()) {
                res.add(buildMatrixForPage(page));
            }
        });
        
        return res;
    }
    
    private static Integer[][] buildMatrixForPage(final Page cpnPage) {
        Integer[][] res = new Integer[cpnPage.getPlaces().size()][cpnPage.getTransitions().size()];
        for (Integer[] row : res) {
            Arrays.fill(row, 0);
        }
        
        for (int i = 0; i < cpnPage.getPlaces().size(); i++) {
            for (int j = 0; j < cpnPage.getTransitions().size(); j++) {
                List<Arc> arcsBetweenCurrentPlaceAndTransition =
                        cpnPage.getArcs(cpnPage.getPlaces().get(i), cpnPage.getTransitions().get(j));
                if (Objects.nonNull(arcsBetweenCurrentPlaceAndTransition)) {
                    for (Arc arc : arcsBetweenCurrentPlaceAndTransition) {
                        if (arc.getOrientation().equals(Arc.Orientation.TO_PLACE)) {
                            res[i][j]++;
                        }
                        if (arc.getOrientation().equals(Arc.Orientation.TO_TRANS)) {
                            res[i][j]--;
                        }
                    }
                }
            }
        }
        return res;
    }
}
