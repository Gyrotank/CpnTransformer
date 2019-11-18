/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.cpn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hlomozda.cpnio.geom.Point;
import hlomozda.cpnio.gfx.Line;
import hlomozda.cpnio.gfx.Text;

public class Arc {
    
    //modified by Dmytro Hlomozda (added inhibitor orientation)
    public enum Orientation { TO_TRANS, TO_PLACE, BOTH_DIR, INHIBITOR }
    
    private Place place;
    
    private Transition transition;
    
    private Line line;
    
    private Text annotation;
    
    private Orientation orientation;
    
    private List<Point> bendPoints;
    
    private int order;
    
    //ADDED BY DMYTRO HLOMOZDA
    public Arc() {       
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public Arc(final Arc other) {
        this.place = other.getPlace();
        this.transition = other.getTransition();
        this.line = new Line(other.line.getColor(),
                other.line.getThickness());
        this.annotation = new Text(other.annotation.getValue(),
                other.annotation.getColor(),
                new Point(other.annotation.getPosition().x(),
                        other.annotation.getPosition().y()));
        this.orientation = other.orientation;        
        this.bendPoints = other.getBendPoints();
        this.order = other.order;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(final Place place) {
        this.place = place;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(final Transition transition) {
        this.transition = transition;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final Orientation orientation) {
        this.orientation = orientation;
    }
    
    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Text getAnnotation() {
        return annotation;
    }

    public void setAnnotation(final Text annotation) {
        this.annotation = annotation;
    }

    public List<Point> getBendPoints() {
        Optional<List<Point>> optionalBendPoints = Optional.ofNullable(bendPoints);
        return new ArrayList<>(optionalBendPoints.orElse(new ArrayList<>()));
    }

    public void addBendPoint(final Point bendPoint) {
        if (bendPoints == null)
                bendPoints = new ArrayList<>();
        bendPoints.add(bendPoint);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public String toString() {
        return "| Arc: " + place.toString() + ", " + transition.toString() + ", " + orientation.name() + " |";
    }    
}
