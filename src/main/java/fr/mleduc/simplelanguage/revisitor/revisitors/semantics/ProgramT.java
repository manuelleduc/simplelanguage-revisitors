package fr.mleduc.simplelanguage.revisitor.revisitors.semantics;

import com.oracle.truffle.api.frame.VirtualFrame;

public interface ProgramT {
    void run(VirtualFrame frame);
}
