package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("child")
public class Unbox extends Expr {

    @Child
    private Expr left;

    public Unbox(Expr left) {
        this.left = left;
    }

    public Expr getLeft() {
        return left;
    }
}
