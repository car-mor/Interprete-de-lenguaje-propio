package parser;

public class StmtIf extends Statement {
    public final Expression condition;
    public final Statement thenBranch;
    public final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}
