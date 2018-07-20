package fr.mleduc.simplelanguage.revisitor.revisitors.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class SLContinueException extends ControlFlowException {
    public static final SLContinueException SINGLETON = new SLContinueException();


    /* Prevent instantiation from outside. */
    private SLContinueException() {
    }
}
