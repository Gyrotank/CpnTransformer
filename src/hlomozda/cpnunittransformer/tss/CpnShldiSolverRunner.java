package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;
import java.util.Arrays;

public class CpnShldiSolverRunner {

    public static void main(final String[] args) {
        
//        int[][] inputMatrix = {{1, 0, 1, 1, 0},
//                               {1, 0,-1, 0, 1},
//                               {0, 0, 0, 0,-1}};
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

        ArrayList<Integer[]> result = new ArrayList<>();
        ArrayList<Integer[]> resultMinimal = new ArrayList<>();
        
        CpnShldiSolver css = new CpnShldiSolverBf(inputMatrix);
        result = css.getSolution();
        resultMinimal = css.getMinimalSolution();
        
        if (result.isEmpty()) {
            System.out.println("The SLHDI is noncompatible");
        }
        System.out.println("There are " + result.size() + " solutions:");
        if (result.size() <= 100) {
            for (Integer[] row : result) {
                System.out.println(Arrays.toString(row));
            }
        } else {
            System.out.println("Showing 100 minimal and 10 maximal solutions:");
            for(int i = 0; i < 100; i++) {
                System.out.println(Arrays.toString(result.get(i)));
            }
            System.out.println("=====");
            for(int i = 10; i > 0; i--) {
                System.out.println(Arrays.toString(result.get(result.size() - i)));
            }
        }
        System.out.println("=====");
        System.out.println("There are " + resultMinimal.size() + " minimal solutions:");
        for (Integer[] row : resultMinimal) {
            System.out.println(Arrays.toString(row));
        }
    }
}
