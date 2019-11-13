package hlomozda.cpnunittransformer.application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;
import hlomozda.cpnunittransformer.cpn.Page;
import hlomozda.cpnunittransformer.cpn.Place;
import hlomozda.cpnunittransformer.cpn.Transition;
import hlomozda.cpnunittransformer.generator.CpnGenerator;
import hlomozda.cpnunittransformer.generator.CpnXmlGenerator;
import hlomozda.cpnunittransformer.parser.CpnParser;
import hlomozda.cpnunittransformer.parser.DomCpnParser;
import hlomozda.cpnunittransformer.transformer.CpnTransformer;
import hlomozda.cpnunittransformer.transformer.CpnUnitTransformer;
import hlomozda.cpnunittransformer.tss.CpnTssFinderRunner;
import hlomozda.cpnunittransformer.utils.IncidenceMatrixBuilder;

import org.apache.log4j.Logger;

public class CpnToUnit {

    private static final Logger logger = Logger.getLogger(CpnToUnit.class);

    public static void main(final String[] args) throws IOException {
        System.setProperty("javax.xml.accessExternalDTD", "http");

        InputStream in = new FileInputStream(args[0]);
        CpnParser parser = new DomCpnParser();
        ColoredPetriNet inputCpn = parser.parse(in);

        logger.info("Input CPN: ");
        printCpnInfo(inputCpn);

        CpnTransformer transformer = new CpnUnitTransformer();
        CpnGenerator cut = new CpnXmlGenerator();
        ColoredPetriNet outputCpn = transformer.transform(inputCpn);
        OutputStream outCpnNet;
        if (args.length == 1) {
            outCpnNet = new FileOutputStream(args[0].substring(0, args[0].lastIndexOf('.')) + "_CpnToUnit.cpn");
        } else {
            outCpnNet = new FileOutputStream(args[1]);
        }
        cut.generate(outputCpn, outCpnNet);
        outCpnNet.close();

        logger.info("Output CPN: ");
        printCpnInfo(outputCpn);
        logger.info("Output CPN created successfully");

        if (reportToBeGenerated(args)) {
            String tssReportFileName;
            if (args.length == 2) {
                tssReportFileName = args[0].substring(0, args[0].lastIndexOf('.')) + "_CpnToUnit_TSS_Report.txt";
            } else {
                tssReportFileName = args[1].substring(0, args[0].lastIndexOf('.')) + "_TSS_Report.txt";
            }

            try (OutputStream outCpnNetTssReport = new FileOutputStream(tssReportFileName)) {
                logger.info("Generating structural analysis report...");
                outCpnNetTssReport.write("=====================".getBytes());
                outCpnNetTssReport.write(System.lineSeparator().getBytes());
                outCpnNetTssReport.write("TSS Analysis Results:".getBytes());
                outCpnNetTssReport.write(System.lineSeparator().getBytes());
                outCpnNetTssReport.write("=====================".getBytes());
                outCpnNetTssReport.write(System.lineSeparator().getBytes());

                List<Integer[][]> matricesForPage = IncidenceMatrixBuilder.buildMatrix(outputCpn);
                matricesForPage.forEach(matrixForPage -> {
                            try {
                                outCpnNetTssReport.write(("Places:" + System.lineSeparator()).getBytes());
                                outputCpn.getPages().get(matricesForPage.lastIndexOf(matrixForPage)).getPlaces()
                                        .forEach(new Consumer<Place>() {
                                            int i = 1;

                                            public void accept(Place place) {
                                                try {
                                                    outCpnNetTssReport.write(((i++) + " # "
                                                            + place.getNameValue().replace("\n", " ")
                                                            + System.lineSeparator()).getBytes());
                                                } catch (Exception e) {
                                                    logger.error(e);
                                                }
                                            }
                                        });
                                outCpnNetTssReport.write(System.lineSeparator().getBytes());

                                outCpnNetTssReport.write(("Transitions:" + System.lineSeparator()).getBytes());
                                outputCpn.getPages().get(matricesForPage.lastIndexOf(matrixForPage)).getTransitions()
                                        .forEach(new Consumer<Transition>() {
                                            int i = 1;

                                            public void accept(Transition transition) {
                                                try {
                                                    outCpnNetTssReport.write(((i++) + " # "
                                                            + transition.getNameValue().replace("\n", " ")
                                                            + System.lineSeparator()).getBytes());
                                                } catch (Exception e) {
                                                    logger.error(e);
                                                }
                                            }
                                        });
                                outCpnNetTssReport.write(System.lineSeparator().getBytes());

                                outCpnNetTssReport.write("Incidence matrix:".getBytes());
                                outCpnNetTssReport.write(System.lineSeparator().getBytes());
                                for (Integer[] row : matrixForPage) {
                                    outCpnNetTssReport.write("[".getBytes());
                                    for (int i = 0; i < row.length; i++) {
                                        if (row[i] >= 0) {
                                            outCpnNetTssReport.write(" ".getBytes());
                                        }
                                        if (i > 0) {
                                            outCpnNetTssReport.write(" ".getBytes());
                                        }
                                        outCpnNetTssReport.write(row[i].toString().getBytes());
                                        if (i < row.length - 1) {
                                            outCpnNetTssReport.write(",".getBytes());
                                        }
                                    }
                                    outCpnNetTssReport.write("]".getBytes());
                                    outCpnNetTssReport.write(System.lineSeparator().getBytes());
                                }
                                List<String> solution = CpnTssFinderRunner.findAndOutputSolution(matrixForPage);
                                solution.forEach(line -> {
                                    try {
                                        outCpnNetTssReport.write(line.getBytes());
                                        outCpnNetTssReport.write(System.lineSeparator().getBytes());
                                    } catch (Exception e) {
                                        logger.error(e);
                                    }
                                });
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        });

                logger.info("Structural analysis report generated and saved to " + tssReportFileName);
            }
        }
    }

    private static boolean reportToBeGenerated(final String[] args) {
        for (String argument : args) {
            if ("-r".equals(argument)) {
                return true;
            }
        }
        return false;
    }

    private static void printCpnInfo(final ColoredPetriNet cpn) {
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
