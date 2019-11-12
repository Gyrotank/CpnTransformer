package hlomozda.cpnunittransformer.definitions;

import static hlomozda.cpnunittransformer.definitions.CpnMlDefinitions.*;

public final class PageXmlDefinitions implements XmlDefinitions {
    
    public static final String TAG_FILLATTR_CUSTOM = "<fillattr colour=\"%s\" pattern=\"%s\" filled=\"%s\"/>";
    public static final String TAG_FILLATTR_DEFAULT = "<fillattr colour=\"White\" pattern=\"\" filled=\"false\"/>";
    
    public static final String TAG_LINEATTR_CUSTOM = "<lineattr colour=\"%s\" thick=\"%s\" type=\"%s\"/>";
    public static final String TAG_LINEATTR_DEFAULT = "<lineattr colour=\"Black\" thick=\"1\" type=\"Solid\"/>";
    
    public static final String TAG_PAGEATTR = "<pageattr name=\"%s\"/>";
    
    public static final String TAG_POSATTR = "<posattr x=\"%s\" y=\"%s\"/>";
    
    public static final String TAG_SNAP_CUSTOM = "<snap snap_id=\"%s\" anchor.horizontal=\"%s\" anchor.vertical=\"%s\"/>";
    public static final String TAG_SNAP_DEFAULT = "<snap snap_id=\"0\" anchor.horizontal=\"0\" anchor.vertical=\"0\"/>";
    
    public static final String TAG_TEXTATTR_CUSTOM = "<textattr colour=\"%s\" bold=\"%s\"/>";
    public static final String TAG_TEXTATTR_DEFAULT = "<textattr colour=\"Black\" bold=\"false\"/>";
    
    public static final String TAG_PAGE_OPEN = "<page id=\"ID%s\">";
    public static final String TAG_PAGE_CLOSE = "</page>";
    
    public static final String TAG_TEXT_TOOL = "<text tool=\"CPN Tools\" version=\"" + CPN_TOOLS_VERSION + "\"/>";
    public static final String TAG_TEXT_TOOL_OPEN = "<text tool=\"CPN Tools\" version=\"" + CPN_TOOLS_VERSION + "\">";
    public static final String TAG_TEXT_OPEN = "<text>";    
    public static final String TAG_TEXT_CLOSE = "</text>";
        
    private PageXmlDefinitions() {       
    }

}
