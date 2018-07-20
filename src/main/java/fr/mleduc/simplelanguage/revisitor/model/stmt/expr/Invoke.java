package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;


import com.oracle.truffle.api.nodes.NodeInfo;

import javax.xml.ws.Dispatch;

@NodeInfo(description = "invoke")
public class Invoke extends Expr {

    @Child private Expr functionNode;
    @Children private final Expr[] argumentNodes;

    public Invoke(Expr functionNode, Expr[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
    }

    public Expr getFunctionNode() {
        return functionNode;
    }

    public Expr[] getArgumentNodes() {
        return argumentNodes;
    }
}
