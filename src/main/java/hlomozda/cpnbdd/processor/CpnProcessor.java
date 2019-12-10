package hlomozda.cpnbdd.processor;

import java.util.Collection;

import hlomozda.cpnio.cpn.ColoredPetriNet;

public interface CpnProcessor<T> {

    /**
     * Main processing method of the input parsed Colored Petri Net
     *
     * @param cpn - parsed Colored Petri Net to be processed
     * @return processing results as Collection
     */
    Collection<T> process(final ColoredPetriNet cpn);

}
