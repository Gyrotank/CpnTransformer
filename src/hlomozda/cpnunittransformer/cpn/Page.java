/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.cpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hlomozda.cpnunittransformer.geom.Point;
import hlomozda.cpnunittransformer.geom.Shape;
import hlomozda.cpnunittransformer.utils.Pair;

public class Page {
    
    private String name;
    
    private List<Place> places;
    
    private List<Transition> transitions;
    
    private Map<Pair<Place, Transition>, List<Arc>> arcs;
    
    private Point bottomLeft;
    
    private Point topRight;

    public Page() {
        places = new ArrayList<>();
        transitions = new ArrayList<>();
        arcs = new HashMap<>();
        bottomLeft = new Point();
        topRight = new Point();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    
    public void addPlace(final Place p) {
        places.add(p);
        Shape s = p.getShape();
        recalcBounds(s.getLeft(), s.getBottom(), s.getRight(), s.getTop());
    }
    
    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public void setPlaces(final List<Place> places) {
        this.places.clear();
        places.stream().forEach(this::addPlace);
    }
    
    public void addTransition(final Transition t) {
        transitions.add(t);
        Shape s = t.getShape();
        recalcBounds(s.getLeft(), s.getBottom(), s.getRight(), s.getTop());
    }
    
    public List<Transition> getTransitions() {
        return new ArrayList<>(transitions);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public void setTransitions(final List<Transition> transitions) {
        this.transitions.clear();;
        transitions.stream().forEach(this::addTransition);
    }
    
    public void addArc(final Arc arc) {
        Pair<Place, Transition> key = new Pair<Place, Transition>(arc.getPlace(), arc.getTransition());
        if(!arcs.containsKey(key)) {
            arcs.put(key, new ArrayList<Arc>());
        }
        arcs.get(key).add(arc);
    }
    
    public List<Arc> getArcs(final Place place, final Transition transition) {
        return arcs.get(new Pair<Place, Transition>(place, transition));
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public void setArcs(final List<Arc> arcs) {
        this.arcs.clear();
        arcs.stream().forEach(this::addArc);
    }
    
    public int getArcsCount() {
        int count = 0;
        for(List<Arc> arcsList: arcs.values())
            count += arcsList.size();
        return count;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }
    
    public Point getTopRight() {
        return topRight;
    }
    
    private void recalcBounds(final double x0, final double y0, final double x1, final double y1) {
        if(x0 < bottomLeft.x())
            bottomLeft.setX(x0);
        if(y0 < bottomLeft.y())
            bottomLeft.setY(y0);
        if(x1 > topRight.x())
            topRight.setX(x1);
        if(y1 > topRight.y())
            topRight.setY(y1);
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    @Override
    public String toString() {
        return "Page " + name + ": Places: " + places.toString() + "; Transitions: " + transitions.toString() + "; Arcs: " + arcs.toString(); 
    }
    
    //ADDED BY DMYTRO HLOMOZDA
    public List<Arc> getArcs() {
        return arcs.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
