package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "not-equal", shortName = "!=")
public class NotEqual extends Binary {

    public NotEqual(Expr left, Expr right) {
        super(left, right);
    }
}
