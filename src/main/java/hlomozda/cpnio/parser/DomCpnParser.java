/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import hlomozda.cpnio.cpn.Arc;
import hlomozda.cpnio.cpn.ColoredPetriNet;
import hlomozda.cpnio.cpn.Page;
import hlomozda.cpnio.cpn.Place;
import hlomozda.cpnio.cpn.Transition;
import hlomozda.cpnio.cpn.Arc.Orientation;
import hlomozda.cpnio.geom.Ellipse;
import hlomozda.cpnio.geom.Point;
import hlomozda.cpnio.geom.Rectangle;
import hlomozda.cpnio.geom.Shape;
import hlomozda.cpnio.gfx.Line;
import hlomozda.cpnio.gfx.Text;

public class DomCpnParser implements CpnParser {

    private static final Logger logger = Logger.getLogger(DomCpnParser.class);

    private DocumentBuilder builder;

    private Map<String, Place> placeIds;

    private Map<String, Transition> transitionIds;

    public DomCpnParser() {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            builder = factory.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError("Failed to instantiate DocumentBuilder");
        }
        placeIds = new HashMap<>();
        transitionIds = new HashMap<>();
    }

    @Override
    public ColoredPetriNet parse(final InputSource in) {
        try {
            return parseFromDocument(builder.parse(in));
        } catch (SAXException | IOException e) {
            logger.info("Failed to parse document");
            return null;
        }
    }

    @Override
    public ColoredPetriNet parse(final InputStream in) {
        try {
            return parseFromDocument(builder.parse(in));
        } catch (SAXException | IOException e) {
            logger.info("Failed to parse document");
            return null;
        }
    }

    @Override
    public ColoredPetriNet parse(final String uri) {
        try {
            return parseFromDocument(builder.parse(uri));
        } catch (SAXException | IOException e) {
            logger.info("Failed to parse document");
            return null;
        }
    }

    private ColoredPetriNet parseFromDocument(final Document doc) {
        ColoredPetriNet cpn = new ColoredPetriNet();
        for (String decl : parseDeclarations(doc, "color", "var", "ml"))
            cpn.addDeclaration(decl);

        NodeList pages = doc.getElementsByTagName("page");
        for (int i = 0; i < pages.getLength(); ++i) {
            Node page = pages.item(i);
            cpn.addPage(parsePage(page));
        }
        return cpn;
    }

    private List<String> parseDeclarations(final Document doc, final String... tagNames) {
        List<String> declarations = new ArrayList<>();
        for (String tagName : tagNames) {
            NodeList nodes = doc.getElementsByTagName(tagName);
            for (int i = 0; i < nodes.getLength(); ++i) {
                Node node = nodes.item(i);
                String decl = parseDeclaration(node);
                if (decl != null)
                    declarations.add(decl);
            }
        }
        return declarations;
    }

    private String parseDeclaration(final Node node) {
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            if (childNode.getNodeName().equals("layout")) {
                return childNode.getTextContent();
            }
        }
        return null;
    }

    private Page parsePage(final Node node) {
        placeIds.clear();
        transitionIds.clear();

        Page page = new Page();
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            switch (childNode.getNodeName()) {
                case "pageattr":
                    String name = ((Element) childNode).getAttribute("name");
                    page.setName(name);
                    break;
                case "place":
                    page.addPlace(parsePlace(childNode));
                    break;
                case "trans":
                    page.addTransition(parseTransition(childNode));
                    break;
                case "arc":
                    page.addArc(parseArc(childNode));
                    break;
                default:
                    break;
            }
        }
        return page;
    }

    private Place parsePlace(final Node node) {
        Place place = new Place();
        place.setShape(new Ellipse());
        place.setName(new Text());

        String id = ((Element) node).getAttribute("id");
        placeIds.put(id, place);

        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            switch (childNode.getNodeName()) {
                case "posattr":
                    place.getShape().setPosition(parsePoint(childNode));
                    break;
                case "fillattr":
                    place.setFill(parseFill(childNode));
                    break;
                case "lineattr":
                    place.setLine(parseLine(childNode));
                    break;
                case "textattr":
                    place.getName().setColor(parseColor(childNode));
                    break;
                case "text":
                    place.getName().setValue(childNode.getTextContent());
                    break;
                case "ellipse":
                    parseShapeSize(childNode, place.getShape());
                    break;
                case "type":
                    place.setType(parseText(childNode));
                    break;
                case "initmark":
                    place.setInitMark(parseText(childNode));
                    break;
                default:
                    break;
            }
        }
        return place;
    }

    private Transition parseTransition(final Node node) {
        Transition trans = new Transition();
        trans.setShape(new Rectangle());
        trans.setName(new Text());

        String id = ((Element) node).getAttribute("id");
        transitionIds.put(id, trans);

        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            switch (childNode.getNodeName()) {
                case "posattr":
                    trans.getShape().setPosition(parsePoint(childNode));
                    break;
                case "fillattr":
                    trans.setFill(parseFill(childNode));
                    break;
                case "lineattr":
                    trans.setLine(parseLine(childNode));
                    break;
                case "textattr":
                    trans.getName().setColor(parseColor(childNode));
                    break;
                case "text":
                    trans.getName().setValue(childNode.getTextContent());
                    break;
                case "box":
                    parseShapeSize(childNode, trans.getShape());
                    break;
                case "cond":
                    trans.setCondition(parseText(childNode));
                    break;
                case "time":
                    trans.setTime(parseText(childNode));
                    break;
                case "code":
                    trans.setCode(parseText(childNode));
                    break;
                case "priority":
                    trans.setPriority(parseText(childNode));
                    break;
                default:
                    break;
            }
        }
        return trans;
    }

    //modified by Dmytro Hlomozda (added inhibitor orientation) 
    private Arc parseArc(final Node node) {
        Arc arc = new Arc();
        Element elem = (Element) node;

        String orientation = elem.getAttribute("orientation");
        if (orientation.equals("PtoT"))
            arc.setOrientation(Orientation.TO_TRANS);
        else if (orientation.equals("TtoP"))
            arc.setOrientation(Orientation.TO_PLACE);
        else if (orientation.equals("BOTHDIR"))
            arc.setOrientation(Orientation.BOTH_DIR);
        else if (orientation.equals("Inhibitor"))
            arc.setOrientation(Orientation.INHIBITOR);

        int order = 0;
        try {
            order = Integer.parseInt(elem.getAttribute("order"));
        } catch (NumberFormatException e) {
            logger.error(e);
        }

        arc.setOrder(order);

        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            switch (childNode.getNodeName()) {
                case "lineattr":
                    arc.setLine(parseLine(childNode));
                case "placeend":
                    String placeId = ((Element) childNode).getAttribute("idref");
                    arc.setPlace(placeIds.get(placeId));
                    break;
                case "transend":
                    String transId = ((Element) childNode).getAttribute("idref");
                    arc.setTransition(transitionIds.get(transId));
                    break;
                case "annot":
                    arc.setAnnotation(parseText(childNode));
                    break;
                case "bendpoint":
                    for (int j = 0; j < childNode.getChildNodes().getLength(); ++j) {
                        Node grandChildNode = childNode.getChildNodes().item(j);
                        if (grandChildNode.getNodeName().equals("posattr"))
                            arc.addBendPoint(parsePoint(grandChildNode));
                    }
                    break;
                default:
                    break;
            }
        }
        return arc;
    }

    private Text parseText(final Node node) {
        Text text = new Text();
        for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
            Node childNode = node.getChildNodes().item(i);
            switch (childNode.getNodeName()) {
                case "posattr":
                    text.setPosition(parsePoint(childNode));
                    break;
                case "textattr":
                    text.setColor(parseColor(childNode));
                    break;
                case "text":
                    text.setValue(childNode.getTextContent());
                    break;
                default:
                    break;
            }
        }
        return text;
    }

    private Point parsePoint(final Node node) {
        Element elem = (Element) node;
        try {
            double x = Double.parseDouble(elem.getAttribute("x"));
            double y = Double.parseDouble(elem.getAttribute("y"));
            return new Point(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void parseShapeSize(final Node node, final Shape shape) {
        Element elem = (Element) node;
        try {
            double w = Double.parseDouble(elem.getAttribute("w"));
            double h = Double.parseDouble(elem.getAttribute("h"));
            shape.setWidth(w);
            shape.setHeight(h);
        } catch (NumberFormatException e) {
            logger.error(e);
        }
    }

    private Line parseLine(final Node node) {
        Element elem = (Element) node;
        String color = parseColor(node);
        double thickness = Double.parseDouble(elem.getAttribute("thick"));
        return new Line(color, thickness);
    }

    private String parseFill(final Node node) {
        String fill = null;
        Element elem = (Element) node;
        if (!elem.getAttribute("filled").equals("false")) {
            fill = parseColor(node);
        }
        return fill;
    }

    private String parseColor(final Node node) {
        Element elem = (Element) node;
        return elem.getAttribute("colour").toLowerCase();
    }

}
