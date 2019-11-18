package hlomozda.cpnio.generator;

import java.io.OutputStream;

import hlomozda.cpnio.cpn.ColoredPetriNet;

public interface CpnGenerator {

    void generate(final ColoredPetriNet cpn, final OutputStream out);
    
}
