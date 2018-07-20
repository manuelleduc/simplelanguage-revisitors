package fr.mleduc.simplelanguage.revisitor.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;

@NodeInfo(description = "if")
public class If extends Stmt {

    @Child
    private Expr cond;

    @Child
    private Stmt thenBranch;

    @Child
    private Stmt elseBranch;

    public If(Expr cond, Stmt thenBranch, Stmt elseBranch) {
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCond() {
        return cond;
    }

    public Stmt getThenBranch() {
        return thenBranch;
    }

    public Stmt getElseBranch() {
        return elseBranch;
    }
}
