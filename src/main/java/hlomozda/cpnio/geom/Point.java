/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.geom;

public class Point {
    
    private double x;
    private double y;
    
    public Point() {}

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(final Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double x() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double y() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }
    
    public void set(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public void add(final double dx, final double dy) {
        setX(x + dx);
        setY(y + dy);
    }
    
    public void add(final Point p) {
        setX(x + p.x);
        setY(y + p.y);
    }
    
    public void multiply(double kx, double ky) {
        setX(x * kx);
        setY(y * ky);
    }
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
