package parser;
import interpreter.Token;

public class StmtVar extends Statement {
    public final Token name;
    public final Expression initializer;

    public StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
}
