package parser;

import interpreter.Token;

public class ExprLogical extends Expression{
    public final Expression left;
    public final Token operator;
    public final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
