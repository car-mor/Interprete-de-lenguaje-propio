package parser;

public class StmtPrint extends Statement {
    public final Expression expression;

    public StmtPrint(Expression expression) {
        this.expression = expression;
    }
}
