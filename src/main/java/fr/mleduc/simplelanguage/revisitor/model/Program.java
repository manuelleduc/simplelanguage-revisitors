package fr.mleduc.simplelanguage.revisitor.model;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.lang.FunDef;


@NodeInfo(description = "program")
public class Program extends Node {
    @Children
    private FunDef[] funDefs;

    public Program(FunDef[] funDefs) {
        this.funDefs = funDefs;
    }

    public FunDef[] getFunDefs() {
        return funDefs;
    }
}
