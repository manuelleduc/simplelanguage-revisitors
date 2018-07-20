package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "And", shortName = "&&")
public class And extends Binary {
    public And(Expr left, Expr right) {
        super(left, right);
    }
}
