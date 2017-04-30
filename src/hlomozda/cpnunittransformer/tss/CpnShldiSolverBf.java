package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A brute-force solver of systems of homogeneous linear Diophantine inequations.
 *
 */
public class CpnShldiSolverBf implements CpnShldiSolver {
    
    private final Integer[][] systemMatrix;
    
    public CpnShldiSolverBf(final Integer[][] inputSystemMatrix) {
        int equationsNumber = inputSystemMatrix.length;
        int equationsSize = inputSystemMatrix[0].length;
        systemMatrix = 
                new Integer[equationsNumber][equationsSize];
        
        for (int i = 0; i < equationsNumber; i++)
            for (int j = 0; j < equationsSize; j++) {
                systemMatrix[i][j] = inputSystemMatrix[i][j];
            }
    }

    @Override
    public ArrayList<Integer[]> getSolution() {
        int equationsSize = systemMatrix[0].length;
        
        ArrayList<Integer[]> solutionsSet = new ArrayList<>();
        
        Integer[] variantToPlug = new Integer[equationsSize];
        Arrays.fill(variantToPlug, 0);
        variantToPlug = incrementVector(variantToPlug);
        
        while(true) {
            boolean isSolution = true;
            
            for (Integer[] equation : systemMatrix) {
                int plugResult = 0;
                for (int i = 0; i < equationsSize; i++) {
                    plugResult += equation[i] * variantToPlug[i];
                }
                if (plugResult < 0) {
                    isSolution = false;
                    break;
                }
            }
            
            if (isSolution) {
                if (solutionsSet.isEmpty()) {
                    solutionsSet.add(Arrays.copyOf(variantToPlug, variantToPlug.length));
                } else {
                    long count = count1s(variantToPlug);
                    if (count1s(solutionsSet.get(solutionsSet.size() - 1)) <= count) {
                        solutionsSet.add(Arrays.copyOf(variantToPlug, variantToPlug.length));
                    } else {
                        int index = 0;
                        while (count1s(solutionsSet.get(index)) < count) {
                            index++;
                        }
                        solutionsSet.add(index, Arrays.copyOf(variantToPlug, variantToPlug.length));
                    }
                }
            }
            
            if (isFilledWith1s(variantToPlug)) {
                break;
            }
            
            variantToPlug = incrementVector(variantToPlug);
        }
        
        return solutionsSet;
    }
    
    @Override
    public ArrayList<Integer[]> getMinimalSolution() {
        int equationsSize = systemMatrix[0].length;
        
        ArrayList<Integer[]> solutionsSet = new ArrayList<>();
        
        Integer[] variantToPlug = new Integer[equationsSize];
        Arrays.fill(variantToPlug, 0);
        variantToPlug = incrementVector(variantToPlug);
                
        while(true) {
            boolean isSolution = true;
            
            for (Integer[] equation : systemMatrix) {
                int plugResult = 0;
                for (int i = 0; i < equationsSize; i++) {
                    plugResult += equation[i] * variantToPlug[i];
                }
                if (plugResult < 0) {
                    isSolution = false;
                    break;
                }
            }
            
            if (isSolution) {
                if (solutionsSet.isEmpty()) {
                    solutionsSet.add(Arrays.copyOf(variantToPlug, variantToPlug.length));
                } else {
                    if (isMinimalVector(variantToPlug, solutionsSet)) {
                        long count = count1s(variantToPlug);
                        if (count1s(solutionsSet.get(solutionsSet.size() - 1)) <= count) {
                            solutionsSet.add(Arrays.copyOf(variantToPlug, variantToPlug.length));
                        } else {                        
                            int index = 0;
                            while (count1s(solutionsSet.get(index)) < count) {
                                index++;
                            }
                            solutionsSet.add(index, Arrays.copyOf(variantToPlug, variantToPlug.length));
                        }
                    }
                }
            }
            
            if (isFilledWith1s(variantToPlug)) {
                break;
            }
            
            variantToPlug = incrementVector(variantToPlug);
        }
        
        return solutionsSet;
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(systemMatrix);
    }
    
    private boolean isMinimalVector(final Integer[] vectorToCompare, final ArrayList<Integer[]> setToCompare) {
        for (Integer[] vector : setToCompare) {
            int matchCount = 0;
            for (int i = 0; i < vectorToCompare.length; i++) {
                if (vectorToCompare[i] == 1 && vector[i] == 1) {
                    matchCount++;
                }
            }
            if (matchCount == count1s(vector)) {
                return false;
            }
        }
        
        return true;
    }
    
    private long count1s(final Integer[] vector) {
        return Arrays.asList(vector).stream().filter(element -> element == 1).count();
    }
    
    private boolean isFilledWith1s(final Integer[] vector) {
        return Arrays.asList(vector).stream().allMatch(element -> element == 1);
    }
    
    private Integer[] incrementVector(final Integer[] vector) {
        Integer[] result = new Integer[vector.length];
        Arrays.fill(result, 0);
        for (int i = 0; i < result.length; i++) {
            result[i] = vector[i];
        }
        if (result[0] == 0) {
            result[0] = 1;
            return result;
        } else {
            int i = 1;
            result[0] = 0;
            while (i < result.length && result[i] == 1) {
                result[i++] = 0;
            }
            if (i < result.length) {
                result[i] = 1;
            }
            return result;
        }
    }        
}
