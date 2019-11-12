package hlomozda.cpnunittransformer.definitions;

public final class TransitionXmlDefinitions implements XmlDefinitions {

    public static final String TAG_BINDING_CUSTOM = "<binding x=\"%s\" y=\"%s\"/>";
    public static final String TAG_BINDING_DEFAULT = "<binding x=\"7.200000\" y=\"-3.000000\"/>";
    
    public static final String TAG_BOX_CUSTOM = "<box w=\"%s\" h=\"%s\"/>";
    public static final String TAG_BOX_DEFAULT = "<box w=\"60.000000\" h=\"40.00000\"/>";
    
    public static final String TAG_CODE_OPEN = "<code id=\"ID%s\">";
    public static final String TAG_CODE_CLOSE = "</code>";
    
    public static final String TAG_COND_OPEN = "<cond id=\"ID%s\">";
    public static final String TAG_COND_CLOSE = "</cond>";
    
    public static final String TAG_PRIORITY_OPEN = "<priority id=\"ID%s\">";
    public static final String TAG_PRIORITY_CLOSE = "</priority>";
    
    public static final String TAG_TIME_OPEN = "<time id=\"ID%s\">";
    public static final String TAG_TIME_CLOSE = "</time>";
    
    public static final String TAG_TRANS_OPEN_CUSTOM = "<trans id=\"ID%s\" explicit=\"%s\">";
    public static final String TAG_TRANS_OPEN_DEFAULT = "<trans id=\"ID%s\" explicit=\"false\">";
    public static final String TAG_TRANS_CLOSE = "</trans>";
    
    private TransitionXmlDefinitions() {       
    }

}
