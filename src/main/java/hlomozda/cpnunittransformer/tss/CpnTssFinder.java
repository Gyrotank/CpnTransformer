package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hlomozda.cpnunittransformer.utils.VectorSpaceUtils;

import org.apache.log4j.Logger;

/**
 * A finder of truncated set of solutions (TSS) for 
 * system of homogeneous linear Diophantine equations representing
 * state equations of Petri net Ax = 0 or A(transposed)y = 0, 
 * where A is the incidence matrix of the Petri net.
 * 
 * The result is a set of T- or S-invariants of the Petri net
 * (depending on whether a system Ax = 0 or A(transposed)y = 0 is solved)
 *
 */
public class CpnTssFinder {

    private static final Logger logger = Logger.getLogger(CpnTssFinder.class);
    
    private final Integer[][] incidenceMatrix;
    
    private final Integer[][] incidenceMatrixTransposed;
    
    CpnTssFinder(final Integer[][] inputIncidenceMatrix) {        
        int equationsNumber = inputIncidenceMatrix.length;
        int equationsSize = inputIncidenceMatrix[0].length;
        incidenceMatrix = 
                new Integer[equationsNumber][equationsSize];
        
        for (int i = 0; i < equationsNumber; i++)
            incidenceMatrix[i] = Arrays.copyOf(inputIncidenceMatrix[i], inputIncidenceMatrix[i].length);

        int equationsNumberTransposed = inputIncidenceMatrix[0].length;
        int equationsSizeTransposed = inputIncidenceMatrix.length;
        incidenceMatrixTransposed = 
                new Integer[equationsNumberTransposed][equationsSizeTransposed];
        
        for (int i = 0; i < equationsNumberTransposed; i++)
            for (int j = 0; j < equationsSizeTransposed; j++) {
                incidenceMatrixTransposed[i][j] = inputIncidenceMatrix[j][i];
            }
    }
    
    public List<Integer[]> tssForIncidenceMatrix() {
        return tssForMatrix(incidenceMatrix);
    }
    
    public List<Integer[]> tssForTransposedIncidenceMatrix() {
        return tssForMatrix(incidenceMatrixTransposed);
    }
    
    private List<Integer[]> tssForMatrix(final Integer[][] inputMatrix) {
        int equationsNumber = inputMatrix.length;
        int equationsSize = inputMatrix[0].length;
        
        ArrayList<Integer[]> basisVectors = new ArrayList<>();
        
        for (int p = 0; p < equationsNumber; p++) {
            if (p == 0) {
                Integer[] basisVector;
                for (int i = 0; i < equationsSize; i++) {
                    basisVector = new Integer[equationsSize];
                    Arrays.fill(basisVector, 0);
                    basisVector[i] = 1;
                    basisVectors.add(basisVector);                    
                }
            }
            
            logger.info("Building TSS for vector " + (p + 1)
                    + "; there are " + basisVectors.size() + " basis vectors");
            
            ArrayList<Integer[]> mZero = new ArrayList<>();
            ArrayList<Integer[]> mMinus = new ArrayList<>();
            ArrayList<Integer[]> mPlus = new ArrayList<>();
            ArrayList<Integer[]> mQuote = new ArrayList<>();
            
            for (Integer[] e : basisVectors) {                
                int pluggingResult = 0;
                for (int i = 0; i < equationsSize; i++) {
                    pluggingResult += e[i] * inputMatrix[p][i];
                }
                
                if (pluggingResult < 0) {
                    mMinus.add(e);
                } else if (pluggingResult == 0) {
                    mZero.add(e);
                } else {
                    mPlus.add(e);
                }
            }
            
            if ((mZero.isEmpty() && mMinus.isEmpty()) || 
                    (mZero.isEmpty() && mPlus.isEmpty())) {
                return new ArrayList<>();
            }
            
            for (Integer[] y : mZero) {
                if (!VectorSpaceUtils.isLinearCombination(y, mQuote)) {
                    mQuote.add(y);
                }
            }
            
            for (Integer[] ei : mMinus)
                for (Integer[] ej : mPlus) {
                    Integer[] yij = new Integer[equationsSize];
                    int lei = 0;
                    int lej = 0;
                    for (int i = 0; i < equationsSize; i++) {
                        lei += ei[i] * inputMatrix[p][i];
                        lej += ej[i] * inputMatrix[p][i];
                    }
                    
                    lei *= -1;
                    for (int i = 0; i < equationsSize; i++) {
                        yij[i] = lei * ej[i] + lej * ei[i];
                    }
                    
                    int gcdOfYij = gcdOfArray(yij);
                    if (gcdOfYij > 1) {
                        for (int i = 0; i < equationsSize; i++) {
                            yij[i] /= gcdOfYij;
                        }
                    }
                    
                    if (!VectorSpaceUtils.isLinearCombination(yij, mQuote)) {
                        mQuote.add(yij);
                    }                    
                }
            
            basisVectors.clear();
            basisVectors.addAll(mQuote);
        }    
        
        return basisVectors;
    }
    
    private int gcdOfArray(final Integer[] arr) {
        int res = 0;
        for (int i : arr) {
            res = gcd(i, res);
        }
        return res;
    }
    
    private int gcd(final int a, final int b) {
        if (b == 0) { 
            return a;
        }
        return gcd(b, a % b);
    }
    
    public String toString() {
        return Arrays.deepToString(incidenceMatrix);
    }
    
}
