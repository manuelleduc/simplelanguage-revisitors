package fr.mleduc.simplelanguage.revisitor.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;

@NodeInfo(description = "while")
public class While extends Stmt {

    @Child
    private Expr cond;

    @Child
    private Stmt body;

    public While(Expr cond, Stmt body) {
        this.cond = cond;
        this.body = body;
    }

    public Expr getCond() {
        return cond;
    }

    public Stmt getBody() {
        return body;
    }
}
