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

class CpnToBddPurchaseTest {

    private static ColoredPetriNet cpn;

    @BeforeAll
    static void init() throws IOException {
        System.setProperty("javax.xml.accessExternalDTD", "http");

        try (InputStream in = new FileInputStream("src/test/resources/POS_Purchase.cpn")) {
            CpnParser parser = new DomCpnParser();
            cpn = parser.parse(in);
        }
    }

    @Test
    void cpnBddBasicTest() {
        CpnProcessor<Map<String, List<String>>> processor = new CpnBddProcessor();

        List<Map<String, List<String>>> processedCpn = new ArrayList<>(processor.process(cpn));

        assertEquals(1, processedCpn.size());
        assertEquals(1, processedCpn.get(0).get("Name").size());
        assertEquals(5, processedCpn.get(0).get("Given").size());
        assertEquals(1, processedCpn.get(0).get("When0").size());
        assertEquals(3, processedCpn.get(0).get("Then0").size());
        assertEquals(1, processedCpn.get(0).get("When1").size());
        assertEquals(3, processedCpn.get(0).get("Then1").size());
        assertEquals(1, processedCpn.get(0).get("When2").size());
        assertEquals(3, processedCpn.get(0).get("Then2").size());
        assertEquals(1, processedCpn.get(0).get("When3").size());
        assertEquals(2, processedCpn.get(0).get("Then3").size());
        assertEquals(6, processedCpn.get(0).get("Examples").size());
    }
}
