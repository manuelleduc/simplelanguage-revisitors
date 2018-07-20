package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "Plus", shortName = "+")
public class Plus extends Binary {
    public Plus(Expr left, Expr right) {
        super(left, right);
    }
}
