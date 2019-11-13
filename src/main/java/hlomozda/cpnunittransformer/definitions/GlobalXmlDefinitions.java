package hlomozda.cpnunittransformer.definitions;

import static hlomozda.cpnunittransformer.definitions.CpnMlDefinitions.*;

public final class GlobalXmlDefinitions implements XmlDefinitions {
    
    public static final String DOUBLE_NEWLINE = System.lineSeparator() + System.lineSeparator();
    public static final String INDENTATION = "    ";
    
    public static final String GENERATOR = "<generator tool=\"CPN Tools\" version=\"" + CPN_TOOLS_VERSION + "\" format=\"6\"/>";
    
    public static final String ID_STANDARD_DECLARATIONS = "<id>Standard declarations</id>";
    
    public static final String KEYWORD_COLSET = "colset";
    public static final String KEYWORD_CONSTRAINTS = "constraints";
    public static final String KEYWORD_FUNCTION = "fun";
    public static final String KEYWORD_ID = "id";
    public static final String KEYWORD_LAYOUT = "layout";
    public static final String KEYWORD_TYPE = "type";
    public static final String KEYWORD_VAL = "val";
    public static final String KEYWORD_VAR = "var"; 
    
    public static final String TAG_BLOCK_OPEN = "<block id=\"ID%s\">";
    public static final String TAG_BLOCK_CLOSE = "</block>";
    
    public static final String TAG_COLOR_OPEN = "<color id=\"ID%s\">";
    public static final String TAG_COLOR_CLOSE = "</color>";
    
    public static final String TAG_CPNET_OPEN = "<cpnet>";
    public static final String TAG_CPNET_CLOSE = "</cpnet>";
    
    public static final String TAG_GLOBBOX_OPEN = "<globbox>";
    public static final String TAG_GLOBBOX_CLOSE = "</globbox>";
    
    public static final String TAG_VAR_OPEN = "<var id=\"ID%s\">";
    public static final String TAG_VAR_CLOSE = "</var>";
    
    public static final String TAG_WORKSPACEELEMENTS_OPEN = "<workspaceElements>";
    public static final String TAG_WORKSPACEELEMENTS_CLOSE = "</workspaceElements>";
    
    public static final String XML_VERSION = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>";
    public static final String XML_DOCTYPE = "<!DOCTYPE workspaceElements PUBLIC \"-//CPN//DTD CPNXML 1.0//EN\" \"http://cpntools.org/DTD/6/cpn.dtd\">";
    
    private GlobalXmlDefinitions() {       
    }

}
