package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;


import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.lang.SLDispatchNode;
import fr.mleduc.simplelanguage.revisitor.lang.SLDispatchNodeGen;

@NodeInfo(description = "invoke")
public class Invoke extends Expr {

    @Child
    private SLDispatchNode dispatchNode;
    @Child
    private Expr functionNode;
    @Children
    private final Expr[] argumentNodes;

    public Invoke(Expr functionNode, Expr[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.dispatchNode = SLDispatchNodeGen.create();
    }

    public Expr getFunctionNode() {
        return functionNode;
    }

    public Expr[] getArgumentNodes() {
        return argumentNodes;
    }

    public SLDispatchNode getDispatchNode() {
        return dispatchNode;
    }
}
