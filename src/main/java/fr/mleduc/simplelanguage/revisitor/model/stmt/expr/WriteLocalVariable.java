package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "write-local-variable")
public class WriteLocalVariable extends Expr {

    @Child
    private Expr value;

    private final FrameSlot slot;


    public WriteLocalVariable(Expr value, FrameSlot slot) {
        this.value = value;
        this.slot = slot;
    }

    public Expr getValue() {
        return value;
    }

    public WriteLocalVariable(FrameSlot slot) {
        this.slot = slot;
    }
}
