/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.cpn;

import java.util.Comparator;
import java.util.Objects;

import hlomozda.cpnio.geom.Rectangle;
import hlomozda.cpnio.gfx.Line;
import hlomozda.cpnio.gfx.Text;

public class Transition {
    
    private Rectangle shape;
    
    private Line line;
    
    private String fill;
    
    private Text name;
    
    private Text condition;
    
    private Text time;
    
    private Text code;
    
    private Text priority;
    
    //ADDED BY DMYTRO HLOMOZDA
    public Transition() {
        
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public Transition(final Transition other) {
        this.shape = new Rectangle(other.shape.getPosX(), 
                other.shape.getPosY(),
                other.shape.getWidth(),
                other.shape.getHeight());
        this.line = new Line(other.line.getColor(),
                other.line.getThickness());
        this.fill = other.fill;
        this.name = new Text(other.name.getValue(),
                other.name.getColor(), 
                null);
        this.condition = new Text(other.condition.getValue(),
                other.condition.getColor(), 
                null);
        this.time = new Text(other.time.getValue(),
                other.time.getColor(), 
                null);
        this.code = new Text(other.code.getValue(),
                other.code.getColor(), 
                null);
        this.priority = new Text(other.priority.getValue(),
                other.priority.getColor(), 
                null);
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

    public Rectangle getShape() {
        return shape;
    }

    public void setShape(final Rectangle shape) {
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
    
    public Text getCondition() {
        return condition;
    }

    public void setCondition(final Text cond) {
        this.condition = cond;
    }

    public Text getTime() {
        return time;
    }

    public void setTime(final Text time) {
        this.time = time;
    }

    public Text getCode() {
        return code;
    }

    public void setCode(final Text code) {
        this.code = code;
    }

    public Text getPriority() {
        return priority;
    }

    public void setPriority(final Text priority) {
        this.priority = priority;
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public String toString() {
        return "[" + name.getValue() + ": " + condition.getValue() + ", " + code.getValue() + "]";
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
        
        if (!(other instanceof Transition)) {
            return false;
        }
        
        Transition otherTransition = (Transition) other;
        
        return (this.getNameValue().equals(otherTransition.getNameValue()));
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public static final Comparator<Transition> TransitionNameComparator = Comparator.comparing(Transition::getNameValue);

}
