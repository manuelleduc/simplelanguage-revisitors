package fr.mleduc.simplelanguage.revisitor.revisitors.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class SLBreakException extends ControlFlowException {
    public static final SLBreakException SINGLETON = new SLBreakException();


    /* Prevent instantiation from outside. */
    private SLBreakException() {
    }
}
