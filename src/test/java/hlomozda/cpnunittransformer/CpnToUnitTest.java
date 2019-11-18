package hlomozda.cpnunittransformer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.cpn.Page;
import hlomozda.cpnio.parser.CpnParser;
import hlomozda.cpnio.parser.DomCpnParser;
import hlomozda.cpnunittransformer.transformer.CpnTransformer;
import hlomozda.cpnunittransformer.transformer.CpnUnitTransformer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CpnToUnitTest {

    private static ColoredPetriNet cpn;

    @BeforeAll
    static void init() throws IOException {
        System.setProperty("javax.xml.accessExternalDTD", "http");

        try (InputStream in = new FileInputStream("src/test/resources/G1.cpn")) {
            CpnParser parser = new DomCpnParser();
            cpn = parser.parse(in);
        }
    }

    @Test
    void cpnInputTest() {
        int cpnPagesCount = cpn.getPages().size();
        int cpnPlacesCount = 0;
        int cpnTransitionsCount = 0;
        int cpnArcsCount = 0;
        for (Page p : cpn.getPages()) {
            cpnPlacesCount += p.getPlaces().size();
            cpnTransitionsCount += p.getTransitions().size();
            cpnArcsCount += p.getArcsCount();
        }

        assertEquals(2, cpnPagesCount);
        assertEquals(8, cpnPlacesCount);
        assertEquals(8, cpnTransitionsCount);
        assertEquals(34, cpnArcsCount);
    }

    @Test
    void cpnTransformTest() {
        CpnTransformer transformer = new CpnUnitTransformer();
        transformer.transform(cpn);

        int cpnPagesCount = cpn.getPages().size();
        int cpnPlacesCount = 0;
        int cpnTransitionsCount = 0;
        int cpnArcsCount = 0;
        for (Page p : cpn.getPages()) {
            cpnPlacesCount += p.getPlaces().size();
            cpnTransitionsCount += p.getTransitions().size();
            cpnArcsCount += p.getArcsCount();
        }

        assertEquals(2, cpnPagesCount);
        assertEquals(13, cpnPlacesCount);
        assertEquals(22, cpnTransitionsCount);
        assertEquals(146, cpnArcsCount);
    }

}
