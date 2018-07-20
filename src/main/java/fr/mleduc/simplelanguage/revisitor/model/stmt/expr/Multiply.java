package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "multiply", shortName = "*")
public class Multiply extends Binary {

    public Multiply(Expr left, Expr right) {
        super(left, right);
    }
}
