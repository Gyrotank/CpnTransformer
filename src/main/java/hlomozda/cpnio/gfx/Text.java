/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.gfx;

import hlomozda.cpnio.geom.Point;

public class Text {
    
    private String value;
    
    private String color;
    
    private Point position;
    
    public Text() {}

    public Text(final String value, final String color) {
        this(value, color, null);
    }

    public Text(final String value, final String color, final Point pos) {
        this.value = value;
        this.color = color;
        this.position = pos;
    }

    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }
    
    public Point getPosition() {
        return position;
    }

    public void setPosition(final Point position) {
        this.position = position;
    }
    
    public double getPosX() {
        return position.x();
    }
    
    public double getPosY() {
        return position.y();
    }

}
