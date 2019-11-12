/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.geom;

public class Ellipse extends Shape {
    
    public Ellipse() {
        super();
    }
    
    public Ellipse(final double x, final double y, final double width, final double height) {
        super(x, y, width, height);
    }

    public int intersection(final Segment seg, final Point p1, final Point p2) {
        double u1 = seg.b().x() - seg.a().x();
        double u2 = seg.a().x() - getPosX();
        double v1 = seg.b().y() - seg.a().y();
        double v2 = seg.a().y() - getPosY();
        double aPow2 = Math.pow(getWidth() / 2, 2);
        double bPow2 = Math.pow(getHeight() / 2, 2);
        double q1 = bPow2 * u1 * u1 + aPow2 * v1 * v1;
        double q2 = 2 * (bPow2 * u1 * u2 + aPow2 * v1 * v2);
        double q3 = bPow2 * u2 * u2 + aPow2 * v2 * v2 - aPow2 * bPow2;

        double d = q2 * q2 - (4 * q1 * q3);
        if (d < 0) {
            return 0;
        }

        if (d == 0) {
            double t = -q2 / (2 * q1);
            if (0 <= t && t <= 1) {
                p1.set(seg.a().x() + t * u1, seg.a().y() + t * v1);
                return 1;
            } else
                return 0;
        } else {
            int n = 0;
            double q = Math.sqrt(d);
            double t = (-q2 - q) / (2 * q1);
            if (0 <= t && t <= 1) {
                p1.set(seg.a().x() + t * u1, seg.a().y() + t * v1);
                n++;
            }

            t = (-q2 + q) / (2 * q1);
            if (0 <= t && t <= 1) {
                if (n == 0) {
                    p1.set(seg.a().x() + t * u1, seg.a().y() + t * v1);
                    n++;
                } else {
                    p2.set(seg.a().x() + t * u1, seg.a().y() + t * v1);
                    n++;
                }
            }
            return n;
        }
    }

}
