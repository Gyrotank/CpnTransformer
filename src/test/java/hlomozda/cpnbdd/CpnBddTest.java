package hlomozda.cpnbdd;

import hlomozda.cpnbdd.processor.CpnBddProcessor;
import hlomozda.cpnbdd.processor.CpnProcessor;
import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.cpn.Page;
import hlomozda.cpnio.parser.CpnParser;
import hlomozda.cpnio.parser.DomCpnParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CpnBddTest {

    private static ColoredPetriNet cpn;

    @BeforeAll
    static void init() throws IOException {
        System.setProperty("javax.xml.accessExternalDTD", "http");

        try (InputStream in = new FileInputStream("src/test/resources/BDD1.cpn")) {
            CpnParser parser = new DomCpnParser();
            cpn = parser.parse(in);
        }
    }

    @Test
    void cpnBddBasicTest() {
        CpnProcessor processor = new CpnBddProcessor();

        @SuppressWarnings("unchecked")
        List<Map<String, List<String>>> processedCpn = new ArrayList<>(processor.process(cpn));

        assertEquals(1, processedCpn.size());
        assertEquals(2, processedCpn.get(0).get("Given").size());
        assertEquals(1, processedCpn.get(0).get("When").size());
        assertEquals(2, processedCpn.get(0).get("Then").size());
    }
}
