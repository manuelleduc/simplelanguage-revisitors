package fr.mleduc.simplelanguage.revisitor.model.stmt;


import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(description = "stmt")
public abstract class Stmt extends Node {
}
