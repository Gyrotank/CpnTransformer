/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.geom;

public class Segment {

    private Point a, b;

    public Segment(final Point a, final Point b) {
        this.setA(a);
        this.setB(b);
    }

    public Segment(final double x1, final double y1, final double x2, final double y2) {
        a = new Point(x1, y1);
        b = new Point(x2, y2);
    }
    
    public Segment(final Segment s) {
        this.setA(new Point(s.a));
        this.setB(new Point(s.b));
    }

    public Point a() {
        return a;
    }

    public void setA(final Point a) {
        this.a = new Point(a);
    }

    public Point b() {
        return b;
    }

    public void setB(final Point b) {
        this.b = new Point(b);
    }

    public double length() {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }
    
    public Point vector() {
        Point vector = new Point(b.x() - a.x(), b.y() - a.y());
        //normalize the vector
        double len = (new Segment(new Point(), vector)).length();
        vector.multiply(1/len , 1/len);
        return vector;
    }
    
    public Point normal() {
        Point vector = vector();
        return new Point(-vector.y(), vector.x());
    }
    
    public Segment parallel(final double shiftLength) {
        Point normal = normal();
        normal.multiply(shiftLength, shiftLength);
        Segment s = new Segment(this);
        s.a().add(normal);
        s.b().add(normal);
        return s;
    }    
    
    @Override
    public String toString() {
        return "[" + a + ";" + b + "]";
    }

}
