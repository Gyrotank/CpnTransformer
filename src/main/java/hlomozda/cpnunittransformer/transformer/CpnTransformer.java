package hlomozda.cpnunittransformer.transformer;

import hlomozda.cpnio.cpn.ColoredPetriNet;

public interface CpnTransformer {

    /**
     * Main transformation method of the input parsed Colored Petri Net
     * 
     * @param cpn - parsed Colored Petri Net to be transformed
     * @return transformed Colored Petri Net
     */
    ColoredPetriNet transform(final ColoredPetriNet cpn);
    
}
