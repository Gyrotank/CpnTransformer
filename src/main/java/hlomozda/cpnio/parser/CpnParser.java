/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.parser;

import java.io.InputStream;

import org.xml.sax.InputSource;

import hlomozda.cpnio.cpn.ColoredPetriNet;

public interface CpnParser {

    ColoredPetriNet parse(final InputSource in);

    ColoredPetriNet parse(final InputStream in);

    ColoredPetriNet parse(final String uri);
}
