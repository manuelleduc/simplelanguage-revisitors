package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "equal", shortName = "==")
public class Equal extends Binary {
    public Equal(Expr left, Expr right) {
        super(left, right);
    }
}
