package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "lower-equal", shortName = "<=")
public class LowerEqual extends Binary {
    public LowerEqual(Expr left, Expr right) {
        super(left, right);
    }
}
