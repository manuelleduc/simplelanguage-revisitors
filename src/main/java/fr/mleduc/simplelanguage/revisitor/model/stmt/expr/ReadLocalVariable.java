package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.frame.FrameSlot;

public class ReadLocalVariable extends Expr {

    private final FrameSlot slot;

    public ReadLocalVariable(FrameSlot slot) {
        this.slot = slot;
    }

    public FrameSlot getSlot() {
        return slot;
    }
}
