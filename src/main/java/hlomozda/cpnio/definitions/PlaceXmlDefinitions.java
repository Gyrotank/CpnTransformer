package hlomozda.cpnio.definitions;

public final class PlaceXmlDefinitions implements XmlDefinitions {
    
    public static final String TAG_ELLIPSE_CUSTOM = "<ellipse w=\"%s\" h=\"%s\"/>";
    public static final String TAG_ELLIPSE_DEFAULT = "<ellipse w=\"60.000000\" h=\"40.00000\"/>";
    
    public static final String TAG_TOKEN_CUSTOM = "<token x=\"%s\" y=\"%s\"/>";
    public static final String TAG_TOKEN_DEFAULT = "<token x=\"-10.000000\" y=\"0.000000\"/>";

    public static final String TAG_MARKING_OPEN_CUSTOM = "<marking x=\"%s\" y=\"%s\" hidden=\"%s\">";
    public static final String TAG_MARKING_OPEN_DEFAULT = "<marking x=\"0.000000\" y=\"0.000000\" hidden=\"false\">";
    public static final String TAG_MARKING_CLOSE = "</marking>";
    
    public static final String TAG_INITMARK_OPEN = "<initmark id=\"ID%s\">";
    public static final String TAG_INITMARK_CLOSE = "</initmark>";

    public static final String TAG_PLACE_OPEN = "<place id=\"ID%s\">";
    public static final String TAG_PLACE_CLOSE = "</place>";
    
    public static final String TAG_TYPE_OPEN = "<type id=\"ID%s\">";
    public static final String TAG_TYPE_CLOSE = "</type>";

    private PlaceXmlDefinitions() {       
    }
}
