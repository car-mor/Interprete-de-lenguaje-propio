package parser;

class ExprVariable extends Expression {
    final String name;
/*
* Cuando encontremos variable hay que ir a tabla de símbolos, checar que exista, y si sí, devolver el valor
* En exprliteral ya tenemos el valor en el Object
*
*/
    ExprVariable(String name) {
        this.name = name;
    }
}
