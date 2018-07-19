package miniexp.truffle.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "block")
public class Block extends Stmt {
    @Children
    private Stmt[] stmts;

    public Block(Stmt[] stmts) {
        this.stmts = stmts;
    }

    public Stmt[] getStmts() {
        return stmts;
    }
}
