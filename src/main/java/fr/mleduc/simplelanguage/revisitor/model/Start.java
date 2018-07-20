package fr.mleduc.simplelanguage.revisitor.model;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "start")
public class Start extends Node {

    @Child
    private Program program;

    public Start(Program program) {
        this.program = program;
    }

    public Program getProgram() {
        return program;
    }
}