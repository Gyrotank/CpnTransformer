/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.gfx;

public class Line {

    private String color;
    
    private double thickness;

    public Line(final String color, final double thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(final double thickness) {
        this.thickness = thickness;
    }

}
