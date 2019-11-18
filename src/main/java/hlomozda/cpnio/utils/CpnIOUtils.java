package hlomozda.cpnio.utils;

import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.cpn.Page;

import org.apache.log4j.Logger;

public class CpnIOUtils {

    private CpnIOUtils() {}

    public static void logCpnInfo(final Logger logger, final ColoredPetriNet cpn) {
        int cpnPagesCount = cpn.getPages().size();
        int cpnPlacesCount = 0;
        int cpnTransitionsCount = 0;
        int cpnArcsCount = 0;
        for (Page p : cpn.getPages()) {
            cpnPlacesCount += p.getPlaces().size();
            cpnTransitionsCount += p.getTransitions().size();
            cpnArcsCount += p.getArcsCount();
        }
        logger.info("-- " + cpnPagesCount + " pages;");
        logger.info("-- " + cpnPlacesCount + " places;");
        logger.info("-- " + cpnTransitionsCount + " transitions;");
        logger.info("-- " + cpnArcsCount + " arcs.\n\n");
    }
}
