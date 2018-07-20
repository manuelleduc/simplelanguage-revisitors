package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "minus", shortName = "-")
public class Minus extends Binary {

    public Minus(Expr left, Expr right) {
        super(left, right);
    }
}
