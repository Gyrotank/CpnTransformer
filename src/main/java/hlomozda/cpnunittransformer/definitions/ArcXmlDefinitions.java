package hlomozda.cpnunittransformer.definitions;

public class ArcXmlDefinitions implements XmlDefinitions {

    public static final String ORIENTATION_BIDIRECTIONAL = "BOTHDIR";
    public static final String ORIENTATION_PLACE_TO_TRANSITION = "PtoT";
    public static final String ORIENTATION_TRANSITION_TO_PLACE = "TtoP";
    public static final String ORIENTATION_INHIBITOR = "Inhibitor";
    
    public static final String TAG_ANNOT_OPEN = "<annot id=\"ID%s\">";
    public static final String TAG_ANNOT_CLOSE = "</annot>";
    
    public static final String TAG_ARC_OPEN = "<arc id=\"ID%s\" orientation=\"%s\" order=\"%s\">";
    public static final String TAG_ARC_CLOSE = "</arc>";
    
    public static final String TAG_ARROWATTR_CUSTOM = "<arrowattr headsize=\"%s\" currentcyckle=\"%s\"/>";
    public static final String TAG_ARROWATTR_DEFAULT = "<arrowattr headsize=\"1.200000\" currentcyckle=\"2\"/>";
    
    public static final String TAG_PLACEEND = "<placeend idref=\"%s\"/>";
    
    public static final String TAG_TRANSEND = "<transend idref=\"%s\"/>";

    private ArcXmlDefinitions() {
    }
}
