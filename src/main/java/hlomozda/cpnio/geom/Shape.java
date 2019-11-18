/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.geom;

public class Shape {
    
    private double width;
    private double height;
    
    private Point position;
    
    public Shape() {
        position = new Point();
    }
    
    public Shape(final double x, final double y, final double width, final double height) {
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(final double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(final double height) {
        this.height = height;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(final Point pos) {
        this.position = pos;
    }
    
    public double getPosX() {
        return position.x();
    }
    
    public double getPosY() {
        return position.y();
    }
    
    public double getTop() {
        return position.y() + height / 2;
    }
    
    public double getRight() {
        return position.x() + width / 2;
    }
    
    public double getBottom() {
        return position.y() - height / 2;
    }
    
    public double getLeft() {
        return position.x() - width / 2;
    }

}
