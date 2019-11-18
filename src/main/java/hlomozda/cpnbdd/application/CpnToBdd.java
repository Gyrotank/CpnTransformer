package hlomozda.cpnbdd.application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hlomozda.cpnbdd.processor.CpnBddProcessor;
import hlomozda.cpnbdd.processor.CpnProcessor;
import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.parser.CpnParser;
import hlomozda.cpnio.parser.DomCpnParser;

import org.apache.log4j.Logger;

import static hlomozda.cpnio.utils.CpnIOUtils.logCpnInfo;

public class CpnToBdd {

    private static final Logger logger = Logger.getLogger(CpnToBdd.class);

    public static void main(final String[] args) throws IOException {

        System.setProperty("javax.xml.accessExternalDTD", "http");

        ColoredPetriNet cpn;

        try (InputStream in = new FileInputStream(args[0])) {
            CpnParser parser = new DomCpnParser();
            cpn = parser.parse(in);
        }

        logger.info("Input CPN: ");
        logCpnInfo(logger, cpn);

        CpnProcessor processor = new CpnBddProcessor();

        @SuppressWarnings("unchecked")
        List<Map<String, List<String>>> processedCpn = new ArrayList<>(processor.process(cpn));

        processedCpn.forEach(scenario -> {
            List<String> preconditions = scenario.get("Given");
            for (int i = 0; i < preconditions.size(); i++) {
                logger.info((i == 0 ? "Given " : "And ") + preconditions.get(i).replace("\n", ""));
            }
            List<String> actions = scenario.get("When");
            for (int i = 0; i < actions.size(); i++) {
                logger.info((i == 0 ? "When " : "And ") + actions.get(i).replace("\n", ""));
            }
            List<String> postconditions = scenario.get("Then");
            for (int i = 0; i < postconditions.size(); i++) {
                logger.info((i == 0 ? "Then " : "And ") + postconditions.get(i).replace("\n", ""));
            }
            logger.info("\n\n");
        });

    }
}
