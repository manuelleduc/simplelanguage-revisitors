package fr.mleduc.simplelanguage.revisitor.revisitors.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class SLReturnException extends ControlFlowException {
    private final Object result;

    public SLReturnException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
