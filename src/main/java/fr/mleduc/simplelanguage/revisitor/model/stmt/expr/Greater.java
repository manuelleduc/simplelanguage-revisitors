package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "greater", shortName = ">")
public class Greater extends Binary {
    public Greater(Expr left, Expr right) {
        super(left, right);
    }
}
