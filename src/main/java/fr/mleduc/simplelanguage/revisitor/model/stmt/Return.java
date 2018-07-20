package fr.mleduc.simplelanguage.revisitor.model.stmt;


import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;

@NodeInfo(description = "return")
public class Return extends Stmt {

    @Child
    private Expr expr;

    public Return(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }
}
