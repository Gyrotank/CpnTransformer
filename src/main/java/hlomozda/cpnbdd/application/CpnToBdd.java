package hlomozda.cpnbdd.application;

import java.io.*;
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

        CpnProcessor<Map<String, List<String>>> processor = new CpnBddProcessor();

        List<Map<String, List<String>>> processedCpn = new ArrayList<>(processor.process(cpn));

        OutputStream outCpnNet;
        if (args.length == 1) {
            outCpnNet = new FileOutputStream(args[0].substring(0, args[0].lastIndexOf('.')) + "_CpnToBdd.story");
        } else {
            outCpnNet = new FileOutputStream(args[1]);
        }

        processedCpn.forEach(scenario -> {
            String line = "Scenario: " + scenario.get("Name").get(0);
            logger.info(line);
            try {
                outCpnNet.write(line.getBytes());
                outCpnNet.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                logger.error(e);
            }

            List<String> preconditions = scenario.get("Given");
            for (int i = 0; i < preconditions.size(); i++) {
                line = (i == 0 ? "Given " : "And ") + preconditions.get(i).replace("\n", "");
                logger.info(line);
                try {
                    outCpnNet.write(line.getBytes());
                    outCpnNet.write(System.lineSeparator().getBytes());
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            List<String> actions = scenario.get("When");
            for (int i = 0; i < actions.size(); i++) {
                line = (i == 0 ? "When " : "And ") + actions.get(i).replace("\n", "");
                logger.info(line);
                try {
                    outCpnNet.write(line.getBytes());
                    outCpnNet.write(System.lineSeparator().getBytes());
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            List<String> postconditions = scenario.get("Then");
            for (int i = 0; i < postconditions.size(); i++) {
                line = (i == 0 ? "Then " : "And ") + postconditions.get(i).replace("\n", "");
                logger.info(line);
                try {
                    outCpnNet.write(line.getBytes());
                    outCpnNet.write(System.lineSeparator().getBytes());
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            try {
                outCpnNet.write(System.lineSeparator().getBytes());
                outCpnNet.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                logger.error(e);
            }
            logger.info("\n\n");
        });
        outCpnNet.close();
    }
}
