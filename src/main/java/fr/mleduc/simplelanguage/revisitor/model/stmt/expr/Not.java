package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

public class Not extends Expr {

    @Child
    private Expr child;

    public Not(Expr child) {
        this.child = child;
    }

    public Expr getChild() {
        return child;
    }
}
