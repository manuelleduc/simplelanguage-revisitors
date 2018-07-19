package miniexp.truffle.model;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;


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
