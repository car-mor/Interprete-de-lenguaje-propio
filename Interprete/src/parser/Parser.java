package parser;

import interpreter.TipoToken;
import interpreter.Token;
import java.beans.Expression;
import java.util.List;

//import mx.ipn.escom.k.exception.ParserException;

public class Parser {
   private final List<Token> tokens;
    private boolean hayErrores = false;
    private int i = 0;
    private Token preanalisis;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }
    public boolean parse() {

        PROGRAM();

        if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
            System.out.println("Consulta correcta");
            return true;
        } else {
            System.out.println("Se encontraron errores");
        }

        return false;
    }

    private void term(){
        factor();
        term2();
    }


    private Expression factor(){
        Expression expr = unary();
        expr = factor2(expr);
        return expr;
    }

    private Expression factor2(Expression expr){
        switch (preanalisis.getTipo()){
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = unary();
                mx.ipn.escom.k.parser.ExprBinary expb = new mx.ipn.escom.k.parser.ExprBinary(expr, operador, expr2);
                return factor2(expb);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = unary();
                expb = new mx.ipn.escom.k.parser.ExprBinary(expr, operador, expr2);
                return factor2(expb);
        }
        return expr;
    }

    private Expression unary(){
        switch (preanalisis.getTipo()){
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = unary();
                return new mx.ipn.escom.k.parser.ExprUnary(operador, expr);
            case MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr = unary();
                return new mx.ipn.escom.k.parser.ExprUnary(operador, expr);
            default:
                return call();
        }
    }

    private Expression call(){
        Expression expr = primary();
        expr = call2(expr);
        return expr;
    }

    private Expression call2(Expression expr){
        switch (preanalisis.getTipo()){
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = argumentsOptional();
                match(TipoToken.RIGHT_PAREN);
                mx.ipn.escom.k.parser.ExprCallFunction ecf = new mx.ipn.escom.k.parser.ExprCallFunction(expr, lstArguments);
                return call2(ecf);
        }
        return expr;
    }

    private Expression primary(){
        switch (preanalisis.getTipo()){
            case TRUE:
                match(TipoToken.TRUE);
                return new mx.ipn.escom.k.parser.ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new mx.ipn.escom.k.parser.ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new mx.ipn.escom.k.parser.ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new mx.ipn.escom.k.parser.ExprLiteral(numero.getLiteral());
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new mx.ipn.escom.k.parser.ExprLiteral(cadena.getLiteral());
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new mx.ipn.escom.k.parser.ExprVariable(id);
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                Expresion expr = expression();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new mx.ipn.escom.k.parser.ExprGrouping(expr);
        }
        return null;
    }


    private void match(TipoToken tt) throws ParserException {
        if(preanalisis.getTipo() ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.getPosition().getLine() +
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
