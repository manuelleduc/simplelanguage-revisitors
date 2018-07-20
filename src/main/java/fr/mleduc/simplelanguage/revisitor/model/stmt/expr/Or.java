package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "Or", shortName = "||")
public class Or extends Binary {
    public Or(Expr left, Expr right) {
        super(left, right);
    }
}
