package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CpnTssFinderRunner {
    
    private static final int OUTPUT_QTY = 100; 

    public static List<String> findAndOutputSolution(final Integer[][] inputIncidenceMatrix) {
        ArrayList<String> output = new ArrayList<>();
        
        ArrayList<Integer[]> result = new ArrayList<>();
        ArrayList<Integer[]> resultTransposed = new ArrayList<>();
        
        CpnTssFinder ctf = new CpnTssFinder(inputIncidenceMatrix);
        
        result = ctf.tssForIncidenceMatrix();
        
        resultTransposed = ctf.tssForTransposedIncidenceMatrix();
        
        output.add("================================");
                
        if (result.isEmpty()) {
            output.add("The SLHDE is noncompatible");
        }
        
        output.add("There are " + result.size() + " T-invariants");
        if (result.size() <= OUTPUT_QTY) {
            for (Integer[] row : result) {
                output.add(Arrays.toString(row));
            }
        } else {
            output.add("Showing the first " + OUTPUT_QTY + " T-invariants:");
            for(int i = 0; i < OUTPUT_QTY; i++) {
                output.add(Arrays.toString(result.get(i)));
            }
        }
        output.add("All-zero columns: ");
        output.add(ZeroColumns(result).isEmpty() 
                ? "none; PN is repeatable" 
                        : ZeroColumns(result).toString() + "; PN is not repeatable");        
        output.add("=======================");
        output.add("There are " + resultTransposed.size() + " S-invariants");
        if (resultTransposed.size() <= OUTPUT_QTY) {
            for (Integer[] row : resultTransposed) {
                output.add(Arrays.toString(row));
            }
        } else {
            for (int i = 0; i < OUTPUT_QTY; i++) {
                output.add(Arrays.toString(resultTransposed.get(i)));
            }
        }
        output.add("=======================");
        output.add("All-zero columns: ");
        output.add(ZeroColumns(resultTransposed).isEmpty() 
                ? "none; PN is bounded" 
                        : ZeroColumns(result).toString() + "; PN is unbounded");
        return output;
    }
    
    private static List<Integer> ZeroColumns(List<Integer[]> vectors) { 
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < vectors.get(0).length; i++) {
            boolean flag = false;            
            if (vectors.get(0)[i] == 0) {
                flag = true;
                for (Integer[] row : vectors) {
                    if (row[i] != 0) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    result.add(i);
                }
            }            
        }
        
        return result;
    }
}
