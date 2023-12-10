package parser;

import interpreter.Token;

import java.util.List;

public class StmtClass extends Statement {
    final Token name;
    final ExprVariable superclass;
    final List<Parser> methods;

    StmtClass(Token name, ExprVariable superclass, List<Parser> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }
}
