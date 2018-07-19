package miniexp.truffle.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.expr.Expr;

@NodeInfo(description = "while")
public class While extends Stmt {

    @Child
    private Expr cond;

    @Child
    private Block body;

    public While(Expr cond, Block body) {
        this.cond = cond;
        this.body = body;
    }

    public Expr getCond() {
        return cond;
    }

    public Block getBody() {
        return body;
    }
}
