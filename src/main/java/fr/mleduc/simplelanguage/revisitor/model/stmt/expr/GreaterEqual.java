package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "greater-equal", shortName = ">=")
public class GreaterEqual extends Binary {
    public GreaterEqual(Expr left, Expr right) {
        super(left, right);
    }
}
