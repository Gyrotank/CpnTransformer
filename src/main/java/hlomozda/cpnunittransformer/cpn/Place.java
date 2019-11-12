/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.cpn;

import java.util.Comparator;
import java.util.Objects;

import hlomozda.cpnunittransformer.geom.Ellipse;
import hlomozda.cpnunittransformer.geom.Point;
import hlomozda.cpnunittransformer.gfx.Line;
import hlomozda.cpnunittransformer.gfx.Text;

public class Place {
    
    private Ellipse shape;
    
    private Line line;
    
    private String fill;

    private Text name;
    
    private Text type;
    
    private Text initMark;
    
    //ADDED BY DMYTRO HLOMOZDA
    public Place() {       
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public Place(final Place other) {
        this.shape = new Ellipse(other.shape.getPosX(), 
                other.shape.getPosY(),
                other.shape.getWidth(),
                other.shape.getHeight());
        this.line = new Line(other.line.getColor(),
                other.line.getThickness());
        this.fill = other.fill;
        this.name = new Text(other.name.getText(),
                other.name.getColor(), 
                null);
        this.type = new Text(other.type.getText(),
                other.type.getColor(),
                new Point(other.type.getPosition().x(),
                        other.type.getPosition().y()));
        this.initMark = new Text(other.initMark.getText(),
                other.initMark.getColor(),
                new Point(other.initMark.getPosition().x(),
                        other.initMark.getPosition().y()));
    }

    public Ellipse getShape() {
        return shape;
    }

    public void setShape(final Ellipse shape) {
        this.shape = shape;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(final Line line) {
        this.line = line;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(final String fill) {
        this.fill = fill;
    }

    public Text getName() {
        return name;
    }

    public void setName(final Text name) {
        this.name = name;
    }

    public String getNameText() {
        return name.getText();
    }

    public Text getType() {
        return type;
    }

    public void setType(final Text type) {
        this.type = type;
    }

    public Text getInitMark() {
        return initMark;
    }

    public void setInitMark(final Text initMark) {
        this.initMark = initMark;
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public String toString() {
        return "(" + name.getText() + ": " + type.getText() + ", " + initMark.getText() + ")";
    }

    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Place)) {
            return false;
        }
        
        Place otherPlace = (Place) other;
        
        return (this.getNameText().equals(otherPlace.getNameText()));
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public static Comparator<Place> PlaceNameComparator = Comparator.comparing(Place::getNameText);

}