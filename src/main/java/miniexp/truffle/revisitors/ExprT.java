package miniexp.truffle.revisitors;

public interface ExprT extends StmtT {
    Object eval();

    @Override
    default void execute() {
        this.eval();
    }
}
