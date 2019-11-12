/*CREATED BY OLEG MATSUK*/

package hlomozda.cpnunittransformer.utils;

public class Pair<Left, Right> {

    private final Left left;
    private final Right right;

    public Pair(final Left left, final Right right) {
        this.left = left;
        this.right = right;
    }

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pair))
            return false;
        Pair<?, ?> p = (Pair<?, ?>) o;
        return p.getLeft().equals(getLeft()) && p.getRight().equals(getRight());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }
}
