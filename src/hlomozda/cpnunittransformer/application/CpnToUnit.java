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

public class CpnToUnit {    
    
    public static void main(final String[] args) throws IOException {
        InputStream in = new FileInputStream(args[0]);
        CpnParser parser = new DomCpnParser();
        ColoredPetriNet inputCpn = parser.parse(in);        
        
        System.out.println("Input CPN: ");
        printCpnInfo(inputCpn);
        
        CpnTransformer transformer = new CpnUnitTransformer();
        CpnGenerator cut = new CpnXmlGenerator();
        ColoredPetriNet outputCpn = transformer.transform(inputCpn);
        OutputStream outCpnNet;
        if (args.length == 1) {
            outCpnNet = new FileOutputStream(args[0].substring(0, args[0].lastIndexOf(".")) + "_CpnToUnit.cpn");
        } else {
            outCpnNet = new FileOutputStream(args[1]);
        }
        cut.generate(outputCpn, outCpnNet);
        
        System.out.println("Output CPN: ");
        printCpnInfo(outputCpn);
        
        OutputStream outCpnNetTssReport;
        String tssReportFileName;
        if (args.length == 1) {
            tssReportFileName = args[0].substring(0, args[0].lastIndexOf(".")) + "_CpnToUnit_TSS_Report.txt";
            outCpnNetTssReport = new FileOutputStream(tssReportFileName);
        } else {
            tssReportFileName = args[1].substring(0, args[0].lastIndexOf(".")) + "_TSS_Report.txt";
            outCpnNetTssReport = new FileOutputStream(tssReportFileName);
        }        
        
        System.out.println("Generating structural analysis report...");
        outCpnNetTssReport.write("=====================".getBytes());
        outCpnNetTssReport.write(System.lineSeparator().getBytes());
        outCpnNetTssReport.write("TSS Analysis Results:".getBytes());
        outCpnNetTssReport.write(System.lineSeparator().getBytes());
        outCpnNetTssReport.write("=====================".getBytes());
        outCpnNetTssReport.write(System.lineSeparator().getBytes());
        
        List<Integer[][]> matricesForPage = IncidenceMatrixBuilder.buildMatrix(outputCpn);
        matricesForPage.stream()
            .forEach(matrixForPage -> {
                try {
                    outCpnNetTssReport.write(("Places:" + System.lineSeparator()).getBytes());
                    outputCpn.getPages().get(matricesForPage.lastIndexOf(matrixForPage)).getPlaces().stream()
                        .forEach(new Consumer<Place>() {
                            int i = 1;
                            
                            public void accept(Place place) {
                                try {
                                    outCpnNetTssReport.write(((i++) + " # " 
                                        + place.getName().getText().replace("\n", " ") 
                                            + System.lineSeparator()).getBytes());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });                    
                    outCpnNetTssReport.write(System.lineSeparator().getBytes());
                    
                    outCpnNetTssReport.write(("Transitions:" + System.lineSeparator()).getBytes());
                    outputCpn.getPages().get(matricesForPage.lastIndexOf(matrixForPage)).getTransitions().stream()
                        .forEach(new Consumer<Transition>() {
                            int i = 1;
                            
                            public void accept(Transition transition) {
                                try {
                                    outCpnNetTssReport.write(((i++) + " # " 
                                            + transition.getName().getText().replace("\n", " ") 
                                            + System.lineSeparator()).getBytes());
                                } catch (Exception e) {
                                    e.printStackTrace();
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
                    solution.stream().forEach(line -> {
                        try {
                            outCpnNetTssReport.write(line.getBytes());
                            outCpnNetTssReport.write(System.lineSeparator().getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        
        System.out.println("Structural analysis report generated and saved to " + tssReportFileName);
        outCpnNet.close();
        outCpnNetTssReport.close();
    }
    
    private static void printCpnInfo(final ColoredPetriNet cpn) {
        int cpnPagesCount = cpn.getPages().size();
        int cpnPlacesCount = 0;
        int cpnTransitionsCount = 0;
        int cpnArcsCount = 0;
        for (Page p: cpn.getPages()) {
            cpnPlacesCount += p.getPlaces().size();
            cpnTransitionsCount += p.getTransitions().size();
            cpnArcsCount += p.getArcsCount();            
        }
        System.out.println("-- " + cpnPagesCount + " pages;");
        System.out.println("-- " + cpnPlacesCount + " places;");
        System.out.println("-- " + cpnTransitionsCount + " transitions;");
        System.out.println("-- " + cpnArcsCount + " arcs.\n\n");
    }
}
