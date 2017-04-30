/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.gfx;

import hlomozda.cpnunittransformer.geom.Point;

public class Text {
    
    private String text;
    
    private String color;
    
    private Point position;

    public String getText() {
        return text;
    }
    
    public Text() {}

    public Text(final String text, final String color, final Point pos) {
        this.text = text;
        this.color = color;
        this.position = pos;
    }

    public Text(final String text, final String color){
        this(text, color, null);
    }
    
    public void setText(final String text) {
        this.text = text;
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
