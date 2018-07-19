package miniexp.truffle.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.expr.Expr;

@NodeInfo(description = "if")
public class If extends Stmt {

    @Child
    private Expr cond;

    @Child
    private Block thenBranch;

    @Child
    private Block elseBranch;

    public If(Expr cond, Block thenBranch, Block elseBranch) {
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCond() {
        return cond;
    }

    public Block getThenBranch() {
        return thenBranch;
    }

    public Block getElseBranch() {
        return elseBranch;
    }
}
