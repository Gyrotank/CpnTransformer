/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.cpn;

import java.util.Comparator;
import java.util.Objects;

import hlomozda.cpnio.geom.Ellipse;
import hlomozda.cpnio.geom.Point;
import hlomozda.cpnio.gfx.Line;
import hlomozda.cpnio.gfx.Text;

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
        this.name = new Text(other.name.getValue(),
                other.name.getColor(), 
                null);
        this.type = new Text(other.type.getValue(),
                other.type.getColor(),
                new Point(other.type.getPosition().x(),
                        other.type.getPosition().y()));
        this.initMark = new Text(other.initMark.getValue(),
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

    public String getNameValue() {
        return name.getValue();
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
        return "(" + name.getValue() + ": " + type.getValue() + ", " + initMark.getValue() + ")";
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
        
        return (this.getNameValue().equals(otherPlace.getNameValue()));
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public static final Comparator<Place> PlaceNameComparator = Comparator.comparing(Place::getNameValue);

}