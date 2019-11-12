package hlomozda.cpnunittransformer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class VectorSpaceUtils {
    
    //see http://www.mathbootcamps.com/determine-vector-linear-combination-vectors/
    //https://www.math.hmc.edu/calculus/tutorials/linearsystems/
    //http://www.di-mgt.com.au/matrixtransform.html
    public static boolean isLinearCombination(final Integer[] vector, final List<Integer[]> vectorSpace) {
        if (vectorSpace.isEmpty()) {
            return false;
        }
        
        BigDecimal[][] augmentedMatrix = new BigDecimal[vector.length][vectorSpace.size() + 1];
        
        for (int i = 0; i < vector.length; i++)
            for (int j = 0; j < vectorSpace.size(); j++) {
                augmentedMatrix[i][j] = new BigDecimal(vectorSpace.get(j)[i]);
                augmentedMatrix[i][vectorSpace.size()] = new BigDecimal(vector[i]);
            }
        
        int j = 0;
        for (int i = 0; i < augmentedMatrix.length; i++) {
            if (existInconsistentRows(augmentedMatrix)) {
                return false;
            }
            
            while (allZeroesInColumn(augmentedMatrix, j)) {
                j++;
                if (j >= augmentedMatrix[0].length - 1) {
                    return false;
                }
            }
            
            if (augmentedMatrix[i][j].compareTo(BigDecimal.ZERO) == 0) {
                exchangeMatrixRows(augmentedMatrix, i, suitableRowIndex(augmentedMatrix, i, j));
            }            
            
            BigDecimal pivot = new BigDecimal(augmentedMatrix[i][j].toString());
            
            for (int jj = 0; jj < augmentedMatrix[i].length; jj++) {
                if (augmentedMatrix[i][jj].compareTo(BigDecimal.ZERO) != 0 
                        && pivot.compareTo(BigDecimal.ZERO) != 0) {
                    augmentedMatrix[i][jj] = augmentedMatrix[i][jj].divide(pivot, 2, RoundingMode.HALF_EVEN);
                }
            }            
            
            for (int k = 0; k < augmentedMatrix.length; k++) {
                if (k != i) {
                    pivot = new BigDecimal(augmentedMatrix[k][j].toString());
                    for (int l = 0; l < augmentedMatrix[k].length; l++) {
                        augmentedMatrix[k][l] = 
                                augmentedMatrix[k][l].subtract(augmentedMatrix[i][l].multiply(pivot));
                    }
                }
            }
            
            j++;
            if (j >= augmentedMatrix[0].length - 1) {
                return false;
            }
        }
        
        return noInconsistentRows(augmentedMatrix);
    }
    
    private static boolean allZeroesInColumn(final BigDecimal[][] matrix, final int column) {
        for (BigDecimal[] row : matrix) {
            if (row[column].compareTo(BigDecimal.ZERO) != 0) {
                return false;
            }
        }
        
        return true;
    }
    
    private static int suitableRowIndex(final BigDecimal[][] matrix, final int startRow, final int column) {
        int res;
        for (res = startRow + 1; res < matrix.length; res++) {
            if (!matrix[res][column].equals(BigDecimal.ZERO)) {
                break;
            }
        }
        
        return res;
    }
    
    private static void exchangeMatrixRows(final BigDecimal[][] matrix, 
            final int firstRowIndex, final int secondRowIndex) {
        if (secondRowIndex >= matrix.length) {
            return;
        }
        
        BigDecimal[] buffer = Arrays.copyOf(matrix[firstRowIndex], matrix[firstRowIndex].length);
        matrix[firstRowIndex] = Arrays.copyOf(matrix[secondRowIndex], matrix[secondRowIndex].length);
        matrix[secondRowIndex] = Arrays.copyOf(buffer, buffer.length);
    }
    
    private static boolean noInconsistentRows(final BigDecimal[][] matrix) {
        for (BigDecimal[] row : matrix) {
            if (row[row.length - 1].compareTo(BigDecimal.ZERO) != 0) {
                boolean flag = true;
                for (int i = 0; i < row.length - 1; i++) {
                    if (row[i].compareTo(BigDecimal.ZERO) != 0) {
                        flag = false;
                    }                    
                }
                if (flag) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean existInconsistentRows(final BigDecimal[][] matrix) {        
        return !noInconsistentRows(matrix);
    }
}
