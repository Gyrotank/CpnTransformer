package hlomozda.cpnio.definitions;

public interface XmlDefinitions {    

    static String createOpenTag(final String tagName) {
        return "<" + tagName + ">";
    }
    
    static String createCloseTag(final String tagName) {
        return "</" + tagName + ">";
    }
    
    static String createFullTag(final String tagName) {
        return "<" + tagName + "/>";
    }
    
}
