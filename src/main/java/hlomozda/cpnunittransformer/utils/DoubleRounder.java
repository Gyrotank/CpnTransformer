/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleRounder {

    private DoubleRounder() {}

    public static double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
