package parser;

import exception.ParserException;
import interpreter.TipoToken;
import interpreter.Token;
import parser.Expression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import mx.ipn.escom.k.exception.ParserException;

public class Ejemplo {
    private final List<Token> tokens;
    private boolean hayErrores = false;
    private int i = 0;
    private Token preanalisis;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    public boolean parse() {

        //PROGRAM()
        if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
            System.out.println("Consulta correcta");
            return true;
        } else {
            System.out.println("Se encontraron errores");
        }

        return false;
    }

    private Statement classDeclaration() {
        if (preanalisis.tipo == TipoToken.CLASS) {
            match(TipoToken.CLASS);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            ExprVariable superClass = classInher();
            match(TipoToken.LEFT_BRACE);
            List<StmtFunction> methods = new ArrayList<>();
            functions(methods);
            match(TipoToken.RIGHT_BRACE);
        }
    }

    private parser.Expression expression() {

    }

    private parser.Expression assignment(){
        Expression expr = logicOr();
        return assignmentOptional(expr);
    }

    private Expression assignmentOptional(Expression expr){
        if (preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Expression value = expression();
            if (expr instanceof ExprVariable){
                Token name = ((ExprVariable) expr).name;
                return new ExprAssign(name, value);
            } else if (expr instanceof ExprGet) {
                ExprGet get = (ExprGet) expr;
                return new ExprSet(get.object, get.name, value);
            }
            throw new ParserException("Asignación inválida del objetivo");
        }
        return expr;
    }

    private void term(){ //hacer lo mismo que con primary, unary y call
        factor(); //cacha lo que venga de factor, y pueda ser una resta o suma
        term2();
    }

    private void term2(){
        switch (preanalisis.tipo){
            case MINUS:
                match(TipoToken.MINUS);
                factor();
                term2();
            case PLUS:
                match(TipoToken.PLUS);
                factor();
                term2();
        }
    }


    private parser.Expression factor() { //lo mismo para term
        Expression expr = unary();
        if (preanalisis.tipo == TipoToken.STAR || preanalisis.tipo == TipoToken.SLASH){
            expr = factor2(expr);
        }
        return expr;
    }

    //expr = operando izquierdo
    //expr2= operando derecho
    //expresión binaria de dos operadores
    private parser.Expression factor2(parser.Expression expr) { //se encarga de determinar si es una división o una multiplicación
        switch (preanalisis.tipo) {
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = unary();
                parser.ExprBinary expb = new parser.ExprBinary(expr, operador, expr2);
                return factor2(expb);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = unary();
                expb = new parser.ExprBinary(expr, operador, expr2);
                return factor2(expb);
        }
        return expr; //si entra aquí no tenemos ninguna división ni multiplicación, regresa lo que recibimos como parametro
    }

    private Expression unary() { //a unary lo llama factor2
        switch (preanalisis.tipo) {
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = unary();
                return new parser.ExprUnary(operador, expr);
            case MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr = unary();
                return new parser.ExprUnary(operador, expr);
            default:
                return call();
        }
    }

    private Expression call() { //llamada de funciones
        Expression expr = primary(); //Aquel que llamó primary tiene que cachar los resultados de primary
        if (preanalisis.tipo == TipoToken.LEFT_PAREN){
            expr = call2(expr);
        }
        return expr;
    }

    private Expression call2(parser.Expression expr) { //cuando se tenga una producción  o cadena vacía, esa función recibirá como parámetro una expresión
        switch (preanalisis.tipo) {
            case LEFT_PAREN: // en caso de que sea llamada a funcion
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = argumentsOptional();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new parser.ExprCallFunction(expr, lstArguments);
                call2(ecf);
                break;
        }
        return expr; //si no retorna mismo valor que cachó
    }

    private parser.Expression primary() {
        switch (preanalisis.tipo) {
            case TRUE:
                match(TipoToken.TRUE);
                return new parser.ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new parser.ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new parser.ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new parser.ExprLiteral(numero.getLiteral());
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous(); //previous regresa token anterior sin mover apuntador
                return new parser.ExprLiteral(cadena.getLiteral());
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new parser.ExprVariable(id);
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                Expression expr = expression();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new parser.ExprGrouping(expr);
        }
        return null;
    }

    //Funciones auxiliares
    private StmtFunction function() {
        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            match(TipoToken.LEFT_PAREN);
            List<Token>parameters;
            parametersOptional();
            match(TipoToken.RIGHT_PAREN);
            Statement body = block();
            return new StmtFunction(name,)
        }else {
            /*String message = "Error "+
                   preanalisis.getP...+
            "cerca de" +
            preanalisis.getLexema() +
            ". Se esperaba un identificador";
            throw new ParseException(new )*/
        }
    }

    private void functions (List<StmtFunction>functions){
        if (preanalisis.tipo==TipoToken.FUN){
            StmtFunction fun = (StmtFunction) fun...;
            functions.add(fun);
            functions(functions);
        }
    }
    private List<Token>parametersOptional(){
        if (preanalisis.tipo==TipoToken.IDENTIFIER){
            List<Token>params = new ArrayList<>();
            parameters(params);
            return params;
        }
        return null;
    }
    private void parameters(List<Token>params){
        if (preanalisis.tipo==){
            match(TipoToken.IDENTIFIER);
            Token name =previous();
            params.add(name);
            parameters2(params);
        }else {
            /*String message = "Error "+
                   preanalisis.getP...+
            "cerca de" +
            preanalisis.getLexema() +
            ". Se esperaba un identificador";
            throw new ParseException(new )*/
        }
    }
    private parser.Expression parameters2(List<Token>params){
        if (preanalisis.tipo==TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            params.add(name);
            parameters2(params);
        }
        return null;
    }

    private List<parser.Expression> argumentsOptional(){
        List<Expression> lstArguments = new ArrayList<>()
        switch (preanalisis.tipo) {
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
        }
        return lstArguments;
    }
    private void arguments(List<parser.Expression>lstArguments){
        if (preanalisis.tipo==TipoToken.COMMA){
            match(TipoToken.COMMA);
            parser.Expression expr = expression();
            lstArguments.add(expr);
            arguments(lstArguments);
        }
    }
    private void match(TipoToken tt) throws ParserException {
        if(preanalisis.tipo ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.linea +
                    ". Se esperaba " + preanalisis.tipo +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
