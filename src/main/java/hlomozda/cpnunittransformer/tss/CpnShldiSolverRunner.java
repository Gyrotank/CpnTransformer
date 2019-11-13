package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class CpnShldiSolverRunner {

    private static final Logger logger = Logger.getLogger(CpnShldiSolverRunner.class);

    public static void main(final String[] args) {
        
        Integer[][] inputMatrix = {{-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0,-1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0,-1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0,-1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                               { 1, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0,-1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                               { 0, 1, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0,-1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0, 0, 0,-1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0, 0, 0,-1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                               { 0, 0, 0, 0,-1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0, 0,-1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 1, 0, 1, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 1, 1, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0, 0, 1, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 1, 0,-1, 0, 0, 0, 0, 1, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,-1, 0, 1, 0, 0, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,-1, 0, 0, 0, 0, 1, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,-1, 1, 0, 0, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,-1, 0, 0, 0, 0, 1},
                               { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0,-1, 0, 0, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0,-1, 0, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0,-1, 0, 0},
                               { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0,-1, 0},
                               { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0,-1, 0},
                               { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,-1},
                               { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0,-1}};

        ArrayList<Integer[]> result;
        ArrayList<Integer[]> resultMinimal;
        
        CpnShldiSolver css = new CpnShldiSolverBf(inputMatrix);
        result = css.getSolution();
        resultMinimal = css.getMinimalSolution();
        
        if (result.isEmpty()) {
            logger.info("The SLHDI is noncompatible");
        }
        logger.info("There are " + result.size() + " solutions:");
        if (result.size() <= 100) {
            for (Integer[] row : result) {
                logger.info(Arrays.toString(row));
            }
        } else {
            logger.info("Showing 100 minimal and 10 maximal solutions:");
            for(int i = 0; i < 100; i++) {
                logger.info(Arrays.toString(result.get(i)));
            }
            logger.info("=====");
            for(int i = 10; i > 0; i--) {
                logger.info(Arrays.toString(result.get(result.size() - i)));
            }
        }
        logger.info("=====");
        logger.info("There are " + resultMinimal.size() + " minimal solutions:");
        for (Integer[] row : resultMinimal) {
            logger.info(Arrays.toString(row));
        }
    }
}
