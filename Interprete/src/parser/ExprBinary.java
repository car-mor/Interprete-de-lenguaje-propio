package parser;

import interpreter.Token;


public class ExprBinary extends Expression {
    public final Expression left;
    public final Token operator;
    public final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

}
