package parser;

import interpreter.Token;

import java.util.List;

public class StmtClass extends Statement {
    final Token name;
    final ExprVariable superclass;
    final List<mx.ipn.escom.k.parser.StmtFunction> methods;

    StmtClass(Token name, ExprVariable superclass, List<mx.ipn.escom.k.parser.StmtFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }
}
