package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

public class ReadArgument extends Expr {

    private final int index;


    public ReadArgument(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
