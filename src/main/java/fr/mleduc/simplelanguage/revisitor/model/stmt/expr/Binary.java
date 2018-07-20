package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

//@NodeChildren({@NodeChild("left"), @NodeChild("right")})
public abstract class Binary extends Expr {

    @Child
    private Expr left;
    @Child
    private Expr right;

    public Binary(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }
}
