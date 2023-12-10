package parser;

import interpreter.Token;

class ExprVariable extends Expression {
    final Token name;
/*
* Cuando encontremos variable hay que ir a tabla de símbolos, checar que exista, y si sí, devolver el valor
* En exprliteral ya tenemos el valor en el Object
*
*/
    ExprVariable(Token name) {
        this.name = name;
    }
}
