/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.cpn;
import java.util.ArrayList;
import java.util.List;

public class ColoredPetriNet {
    
    private List<String> declarations;

    private List<Page> pages;
    
    public ColoredPetriNet() {
        declarations = new ArrayList<>();
        pages = new ArrayList<>();
    }
    
    public List<String> getDeclarations() {
        return new ArrayList<>(declarations);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public void setDeclarations(final List<String> declarations) {
        this.declarations.clear();
        declarations.stream().forEach(this::addDeclaration);
    }

    public void addDeclaration(final String declaration) {
        declarations.add(declaration);
    }
    
    public List<Page> getPages() {
        return new ArrayList<>(pages);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public void setPages(final List<Page> pages) {
        this.pages.clear();
        pages.stream().forEach(this::addPage);
    }
    
    public void addPage(final Page page) {
        pages.add(page);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public String toString() {
        return declarations.toString() + ";\n" + pages.toString();
    }
}
