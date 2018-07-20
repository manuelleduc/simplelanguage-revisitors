package fr.mleduc.simplelanguage.revisitor.revisitors.semantics;

import com.oracle.truffle.api.frame.VirtualFrame;

public interface ExprT extends StmtT {
    Object eval(VirtualFrame frame);

    @Override
    default void execute(VirtualFrame frame) {
        this.eval(frame);
    }
}
