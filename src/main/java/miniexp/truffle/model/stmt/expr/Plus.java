package miniexp.truffle.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.expr.Expr;

@NodeInfo(description = "Plus", shortName = "+")
public class Plus extends Expr {

    @Child
    private Expr left;

    @Child
    private Expr right;

    public Plus(Expr left, Expr right) {
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