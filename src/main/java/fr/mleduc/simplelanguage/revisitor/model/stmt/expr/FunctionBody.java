package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import fr.mleduc.simplelanguage.revisitor.model.stmt.Stmt;

public class FunctionBody extends Expr {

    @Child
    private Stmt bodyNode;

    public FunctionBody(Stmt bodyNode) {
        this.bodyNode = bodyNode;
    }


    public Stmt getBodyNode() {
        return bodyNode;
    }
}
