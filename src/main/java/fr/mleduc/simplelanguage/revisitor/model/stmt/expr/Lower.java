package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "lower", shortName = "<")
public class Lower extends Binary {
    public Lower(Expr left, Expr right) {
        super(left, right);
    }
}
