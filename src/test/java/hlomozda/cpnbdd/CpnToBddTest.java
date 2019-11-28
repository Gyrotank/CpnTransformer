package hlomozda.cpnbdd;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hlomozda.cpnbdd.processor.CpnBddProcessor;
import hlomozda.cpnbdd.processor.CpnProcessor;
import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.parser.CpnParser;
import hlomozda.cpnio.parser.DomCpnParser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CpnToBddTest {

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
        CpnProcessor<Map<String, List<String>>> processor = new CpnBddProcessor();

        List<Map<String, List<String>>> processedCpn = new ArrayList<>(processor.process(cpn));

        assertEquals(2, processedCpn.size());
        assertEquals(1, processedCpn.get(0).get("Name").size());
        assertEquals(2, processedCpn.get(0).get("Given").size());
        assertEquals(1, processedCpn.get(0).get("When").size());
        assertEquals(2, processedCpn.get(0).get("Then").size());
        assertEquals(0, processedCpn.get(0).get("Examples").size());
        assertEquals(1, processedCpn.get(1).get("Name").size());
        assertEquals(2, processedCpn.get(1).get("Given").size());
        assertEquals(1, processedCpn.get(1).get("When").size());
        assertEquals(2, processedCpn.get(1).get("Then").size());
        assertEquals(0, processedCpn.get(1).get("Examples").size());
    }
}
