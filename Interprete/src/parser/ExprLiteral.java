package parser;

public class ExprLiteral extends Expression {
    public final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }
}
