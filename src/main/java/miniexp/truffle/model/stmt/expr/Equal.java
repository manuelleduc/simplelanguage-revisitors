package miniexp.truffle.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.expr.Expr;

@NodeInfo(description = "equal", shortName = "==")
public class Equal extends Expr {

    @Child
    private Expr left;

    @Child
    private Expr right;

    public Equal(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }
}
