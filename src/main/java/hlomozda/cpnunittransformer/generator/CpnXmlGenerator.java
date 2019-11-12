package hlomozda.cpnunittransformer.generator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import hlomozda.cpnunittransformer.cpn.Arc;
import hlomozda.cpnunittransformer.cpn.ColoredPetriNet;
import hlomozda.cpnunittransformer.cpn.Page;
import hlomozda.cpnunittransformer.cpn.Place;
import hlomozda.cpnunittransformer.cpn.Transition;
import static hlomozda.cpnunittransformer.definitions.ArcXmlDefinitions.*;
import static hlomozda.cpnunittransformer.definitions.GlobalXmlDefinitions.*;
import static hlomozda.cpnunittransformer.definitions.PageXmlDefinitions.*;
import static hlomozda.cpnunittransformer.definitions.PlaceXmlDefinitions.*;
import static hlomozda.cpnunittransformer.definitions.TransitionXmlDefinitions.*;
import static hlomozda.cpnunittransformer.definitions.XmlDefinitions.*;

public class CpnXmlGenerator implements CpnGenerator {
    
    private OutputStream mOut;
    private int idCounter = 1;
    private int indentationCounter = 0;
    
    private Map<String, Integer> placeIds;
    private Map<String, Integer> transitionIds;
    
    @Override
    public void generate(final ColoredPetriNet cpn, final OutputStream out) {
        mOut = out;
        placeIds = new HashMap<>();
        transitionIds = new HashMap<>();
        
        generatePreludeXml();
        
        generateWorkspaceElementsXml(cpn);        
    }
    
    private void generatePreludeXml() {
        generateLineSingleNlXml(XML_VERSION, 0);
        generateLineDoubleNlXml(XML_DOCTYPE, 0);            
    }
    
    private void generateWorkspaceElementsXml(final ColoredPetriNet cpn) {
        generateLineSingleNlXml(TAG_WORKSPACEELEMENTS_OPEN, 0);        
        generateLineSingleNlXml(GENERATOR, 1);        
        generateLineSingleNlXml(TAG_CPNET_OPEN, 0);
        
        generateGlobBoxXml(cpn);        
        
        cpn.getPages().stream().forEach(this::generatePageXml);
        
        generateLineSingleNlXml(TAG_CPNET_CLOSE, -1);
        generateLineSingleNlXml(TAG_WORKSPACEELEMENTS_CLOSE, -1);            
    }
    
    private void generateGlobBoxXml(final ColoredPetriNet cpn) {
        generateLineSingleNlXml(TAG_GLOBBOX_OPEN, 1);
        
        generateLineSingleNlXml(TAG_BLOCK_OPEN, 1, true);
        generateLineSingleNlXml(ID_STANDARD_DECLARATIONS, 1);        
        cpn.getDeclarations().stream().filter(s -> s.contains(KEYWORD_COLSET)).forEach(this::generateColorXml);
        cpn.getDeclarations().stream().filter(s -> s.contains(KEYWORD_VAR)).forEach(this::generateVarXml);        
        generateLineSingleNlXml(TAG_BLOCK_CLOSE, -1);
        
        generateLineSingleNlXml(TAG_GLOBBOX_CLOSE, -1);            
    }
    
    private void generateVarXml(final String varDeclaration) {
        generateLineSingleNlXml(String.format(TAG_VAR_OPEN, String.valueOf(idCounter++)), 0);
        
        String[] splitString = varDeclaration.split("( )|(:)|(;)");
        generateLineSingleNlXml(createOpenTag(KEYWORD_TYPE), 1);
        generateLineSingleNlXml(createOpenTag(KEYWORD_ID) + splitString[2].toUpperCase() + createCloseTag(KEYWORD_ID), 1);
        generateLineSingleNlXml(createCloseTag(KEYWORD_TYPE), -1);
        generateLineSingleNlXml(createOpenTag(KEYWORD_ID) + splitString[1].toLowerCase() + createCloseTag(KEYWORD_ID), 0);
        generateLineSingleNlXml(createOpenTag(KEYWORD_LAYOUT) + varDeclaration + createCloseTag(KEYWORD_LAYOUT), 0);
        
        generateLineSingleNlXml(TAG_VAR_CLOSE, -1);
    }
    
    private void generateColorXml(final String colsetDeclaration) {
        String[] colsetDeclarationSplit = colsetDeclaration.split("( )|(;)");
        generateLineSingleNlXml(String.format(TAG_COLOR_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(createOpenTag(KEYWORD_ID) + colsetDeclarationSplit[1] + createCloseTag(KEYWORD_ID), 1);
        generateLineSingleNlXml(createFullTag(colsetDeclarationSplit[3]), 0);
        generateLineSingleNlXml(createOpenTag(KEYWORD_LAYOUT) + colsetDeclaration + createCloseTag(KEYWORD_LAYOUT), 0);
        
        generateLineSingleNlXml(TAG_COLOR_CLOSE, -1);
    }
    
    private void generatePageXml(final Page page) {
        generateLineSingleNlXml(String.format(TAG_PAGE_OPEN, String.valueOf(idCounter++)), 0);
        generateLineSingleNlXml(String.format(TAG_PAGEATTR, page.getName()), 1);
        
        page.getPlaces().stream().forEach(this::generatePlaceXml);
        page.getTransitions().stream().forEach(this::generateTransitionXml);
        page.getArcs().stream().forEach(this::generateArcXml);
        
        generateLineSingleNlXml(createFullTag(KEYWORD_CONSTRAINTS), 0);
        generateLineSingleNlXml(TAG_PAGE_CLOSE, -1);
    }
    
    private void generatePlaceXml(final Place place) {
        placeIds.put(place.getNameText(), idCounter);
        
        generateLineSingleNlXml(String.format(TAG_PLACE_OPEN, String.valueOf(idCounter++)), 0);
        
        generatePlacePreambleXml(place);
        generatePlaceMarkingXml(place);
        generatePlaceTypeXml(place);
        generatePlaceInitMarkXml(place);       
        
        generateLineSingleNlXml(TAG_PLACE_CLOSE, -1);
    }
    
    private void generatePlacePreambleXml(final Place place) {        
        generateLineSingleNlXml(String.format(TAG_POSATTR, place.getShape().getPosX(), place.getShape().getPosY()), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_OPEN + place.getNameText() + TAG_TEXT_CLOSE, 0);
        generateLineSingleNlXml(TAG_ELLIPSE_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TOKEN_DEFAULT, 0);
    }
    
    private void generatePlaceMarkingXml(final Place place) {        
        generateLineSingleNlXml(TAG_MARKING_OPEN_DEFAULT, 0);
        
        generateLineSingleNlXml(TAG_SNAP_DEFAULT, 1);
        
        generateLineSingleNlXml(TAG_MARKING_CLOSE, -1);
    }
    
    private void generatePlaceTypeXml(final Place place) {        
        generateLineSingleNlXml(String.format(TAG_TYPE_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(String.format(TAG_POSATTR, place.getType().getPosX(), place.getType().getPosY()), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL_OPEN + place.getType().getText() + TAG_TEXT_CLOSE, 0);
        
        generateLineSingleNlXml(TAG_TYPE_CLOSE, -1);
    }
    
    private void generatePlaceInitMarkXml(final Place place) {        
        generateLineSingleNlXml(String.format(TAG_INITMARK_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(String.format(TAG_POSATTR, place.getInitMark().getPosX(), place.getInitMark().getPosY()), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL_OPEN + 
                (place.getInitMark().getText().isEmpty() ? "" : place.getInitMark().getText()) 
                + TAG_TEXT_CLOSE, 0);
        
        generateLineSingleNlXml(TAG_INITMARK_CLOSE, -1);
    }
    
    private void generateTransitionXml(final Transition transition) {
        transitionIds.put(transition.getNameText(), idCounter);
        
        generateLineSingleNlXml(String.format(TAG_TRANS_OPEN_DEFAULT, String.valueOf(idCounter++)), 0);
        
        generateTransitionPreambleXml(transition);
        generateTransitionCondXml(transition);
        generateTransitionTimeXml(transition);
        generateTransitionCodeXml(transition);
        generateTransitionPriorityXml(transition);
        
        generateLineSingleNlXml(TAG_TRANS_CLOSE, -1);
    }
    
    private void generateTransitionPreambleXml(final Transition transition) {        
        generateLineSingleNlXml(String.format(TAG_POSATTR, transition.getShape().getPosX(), transition.getShape().getPosY()), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_OPEN + transition.getNameText() + TAG_TEXT_CLOSE, 0);
        generateLineSingleNlXml(TAG_BOX_DEFAULT, 0);
        generateLineSingleNlXml(TAG_BINDING_DEFAULT, 0);
    }
    
    private void generateTransitionCondXml(final Transition transition) {        
        generateLineSingleNlXml(String.format(TAG_COND_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 1);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL, 0);
        
        generateLineSingleNlXml(TAG_COND_CLOSE, -1);
    }
    
    private void generateTransitionTimeXml(final Transition transition) {        
        generateLineSingleNlXml(String.format(TAG_TIME_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 1);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL, 0);
        
        generateLineSingleNlXml(TAG_TIME_CLOSE, -1);
    }
    
    private void generateTransitionCodeXml(final Transition transition) {        
        generateLineSingleNlXml(String.format(TAG_CODE_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 1);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL, 0);
        
        generateLineSingleNlXml(TAG_CODE_CLOSE, -1);
    }
    
    private void generateTransitionPriorityXml(final Transition transition) {        
        generateLineSingleNlXml(String.format(TAG_PRIORITY_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 1);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL, 0);
        
        generateLineSingleNlXml(TAG_PRIORITY_CLOSE, -1);
    }
    
    private void generateArcXml(final Arc arc) {
        generateLineSingleNlXml(String.format(TAG_ARC_OPEN, 
                String.valueOf(idCounter++), getArcCpnOrientation(arc), arc.getOrder()), 0);
        
        generateArcPreambleXml(arc);
        generateArcAnnotXml(arc);
        
        generateLineSingleNlXml(TAG_ARC_CLOSE, -1);
    }
    
    private String getArcCpnOrientation(Arc arc) {
        switch (arc.getOrientation()) {
            case TO_TRANS:
                return ORIENTATION_PLACE_TO_TRANSITION;
            case TO_PLACE:
                return ORIENTATION_TRANSITION_TO_PLACE;
            case BOTH_DIR:
                return ORIENTATION_BIDIRECTIONAL;
            case INHIBITOR:
                return ORIENTATION_INHIBITOR;
            default: 
                return "";
        }        
    }
    
    private void generateArcPreambleXml(final Arc arc) {        
        generateLineSingleNlXml(String.format(TAG_POSATTR, "0.000000", "0.000000"), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_ARROWATTR_DEFAULT, 0);
        generateLineSingleNlXml(String.format(TAG_TRANSEND, getTransitionIdForArc(arc)), 0);
        generateLineSingleNlXml(String.format(TAG_PLACEEND, getPlaceIdForArc(arc)), 0);
    }
    
    private void generateArcAnnotXml(final Arc arc) {
        generateLineSingleNlXml(String.format(TAG_ANNOT_OPEN, String.valueOf(idCounter++)), 0);
        
        generateLineSingleNlXml(String.format(TAG_POSATTR, arc.getAnnotation().getPosX(), arc.getAnnotation().getPosY()), 1);
        generateLineSingleNlXml(TAG_FILLATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_LINEATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXTATTR_DEFAULT, 0);
        generateLineSingleNlXml(TAG_TEXT_TOOL_OPEN + arc.getAnnotation().getText() + TAG_TEXT_CLOSE, 0);
        
        generateLineSingleNlXml(TAG_ANNOT_CLOSE, -1);
    }
    
    private String getTransitionIdForArc(final Arc arc) {
        return "ID" + transitionIds.get(arc.getTransition().getNameText()).toString();
    }
    
    private String getPlaceIdForArc(final Arc arc) {
        return "ID" + placeIds.get(arc.getPlace().getNameText()).toString();
    }    
    
    private void generateLineSingleNlXml(final String line, final int indentIncrement, final boolean isIdUsed) {
        generateLineSingleNlXml(String.format(line, String.valueOf(idCounter++)), indentIncrement);
    }
    
    private void generateLineSingleNlXml(final String line, final int indentIncrement) {
        indentationCounter += indentIncrement;
        try {
            for (int i = 0; i < indentationCounter; i++) {
                mOut.write(_INDENTATION.getBytes());
            }
            mOut.write(line.getBytes());
            mOut.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    @SuppressWarnings("unused")
    private void generateLineDoubleNlXml(final String line, final int indentIncrement, final boolean isIdUsed) {
        generateLineDoubleNlXml(String.format(line, String.valueOf(idCounter++)), indentIncrement);
    }
    
    private void generateLineDoubleNlXml(final String line, final int indentIncrement) {
        indentationCounter += indentIncrement;
        try {
            for (int i = 0; i < indentationCounter; i++) {
                mOut.write(_INDENTATION.getBytes());
            }
            mOut.write(line.getBytes());
            mOut.write(_DOUBLE_NEWLINE.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
