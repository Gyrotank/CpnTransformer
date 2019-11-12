package hlomozda.cpnunittransformer.tss;

import java.util.ArrayList;

/**
 * Interface for family of solvers of systems of homogeneous linear Diophantine inequations.
 *
 */
public interface CpnShldiSolver {

    ArrayList<Integer[]> getSolution();
    ArrayList<Integer[]> getMinimalSolution();

}
