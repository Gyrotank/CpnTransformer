package hlomozda.cpnunittransformer.generator;

import java.io.OutputStream;

import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;

public interface CpnGenerator {

    public void generate(final ColoredPetriNet cpn, final OutputStream out);
    
}
