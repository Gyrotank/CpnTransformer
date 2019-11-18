/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnio.geom;

public class Rectangle extends Shape {
    
    public Rectangle() {
        super();
    }
    
    public Rectangle(final double x, final double y, final double width, final double height) {
        super(x, y, width, height);
    }

    public int intersection(final Segment seg, final Point p1, final Point p2) {
        double left = getLeft();
        double right = getRight();
        double bottom = getBottom();
        double top = getTop();
        
        double minX = Math.min(seg.a().x(), seg.b().x());
        double maxX = Math.max(seg.a().x(), seg.b().x());
        double minY = Math.min(seg.a().y(), seg.b().y());
        double maxY = Math.max(seg.a().y(), seg.b().y());
        
        int n = 0;
        if(maxX < left || minX > right 
                || maxY < bottom || minY > top)
                return 0;
        
        //segment is horizontal
        if(seg.a().y() == seg.b().y()) {
            if(minX <= left) {
                p1.set(left, seg.a().y());
                ++n;
            }
            if(maxX >= right) {
                ((n == 0) ? p1 : p2).set(right, seg.a().y());
                ++n;
            }
        }
        //segment is vertical
        else if(seg.a().x() == seg.b().x()) {
            if(minY <= bottom) {
                p1.set(seg.a().x(), bottom);
                ++n;
            }
            if(maxY >= top) {
                ((n == 0) ? p1 : p2).set(seg.a().x(), top);
                ++n;
            }
        }
        //segment is sloped
        else {
            double dy = seg.b().y() - seg.a().y();
            double dx = seg.b().x() - seg.a().x();
            double k = dy / dx;
            double b = (seg.b().x() * seg.a().y() - seg.a().x() * seg.b().y()) / dx;
            
            //intersection with top segment
            if(maxY >= top) {
                double xi =  (top - b) / k;
                if(xi >= left && xi <= right) {
                    p1.set(xi, top);
                    ++n;
                }
            }
            //intersection with bottom segment
            if(minY <= bottom) {
                double xi = (bottom - b) / k;
                if(xi >= left && xi <= right) {
                    ((n == 0) ? p1 : p2).set(xi, bottom);
                    ++n;
                }
            }
            //intersection with left segment
            if(minX <= left && n < 2) {
                double yi = k * left + b;
                if(yi >= bottom && yi <= top) {
                    ((n == 0) ? p1 : p2).set(left, yi);
                    ++n;
                }
            }
            //intersection with right segment
            if(maxX >= right && n < 2) {
                double yi = k * right + b;
                if(yi >= bottom && yi <= top) {
                    ((n == 0) ? p1 : p2).set(right, yi);
                    ++n;
                }
            }
        }
        
        return n;
    }

}
