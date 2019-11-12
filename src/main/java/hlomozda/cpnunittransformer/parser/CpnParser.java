/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.parser;

import java.io.InputStream;

import org.xml.sax.InputSource;

import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;

public interface CpnParser {

    ColoredPetriNet parse(final InputSource in);

    ColoredPetriNet parse(final InputStream in);

    ColoredPetriNet parse(final String uri);
}
