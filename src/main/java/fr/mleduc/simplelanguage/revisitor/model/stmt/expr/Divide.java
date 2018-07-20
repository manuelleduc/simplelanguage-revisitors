package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "divide", shortName = "/")
public class Divide extends Binary {

    public Divide(Expr left, Expr right) {
        super(left, right);
    }
}
