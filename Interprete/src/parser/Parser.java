package parser;

import interpreter.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


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

    ///*******************Carlitos declaraciones
    //PROGRAM->DECLARATION
    private void PROGRAM() {
        DECLARATION();
    }

    //DECLARATION->FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | e
    private void DECLARATION() {
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        } else if (preanalisis.tipo == TipoToken.VAR ) {
            VAR_DECL();
            DECLARATION();
        } else if (
                preanalisis.tipo==TipoToken.BANG||
                        preanalisis.tipo==TipoToken.FOR||
                        preanalisis.tipo==TipoToken.IF||
                        preanalisis.tipo==TipoToken.PRINT||
                        preanalisis.tipo==TipoToken.RETURN||
                        preanalisis.tipo==TipoToken.WHILE||
                        preanalisis.tipo==TipoToken.LEFT_BRACE||
                        preanalisis.tipo==TipoToken.MINUS||
                        preanalisis.tipo==TipoToken.TRUE||
                        preanalisis.tipo==TipoToken.FALSE||
                        preanalisis.tipo==TipoToken.NULL||
                        preanalisis.tipo==TipoToken.NUMBER||
                        preanalisis.tipo==TipoToken.STRING||
                        preanalisis.tipo==TipoToken.IDENTIFIER||
                        preanalisis.tipo==TipoToken.LEFT_PAREN) {
            STATEMENT();
            DECLARATION();
        }


    }
    //FUN_DECL-> fun FUNCTION
    private void FUN_DECL(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.FUN) {
            match(TipoToken.FUN);
            FUNCTION();
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ", columna: "+ preanalisis.columnaE+". Se esperaba 'fun'.");
        }
    }
    //VAR_DECL-> var id VAR_INIT ;
    private void VAR_DECL(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            if(preanalisis.tipo == TipoToken.IDENTIFIER) {
                match(TipoToken.IDENTIFIER);
                VAR_INIT();
                if (preanalisis.tipo == TipoToken.SEMICOLON) {
                    match(TipoToken.SEMICOLON);
                }
                else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea + ", columna: "+ preanalisis.columnaE+". Se esperaba ';' o '='.");
                }
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'identifier'.");
            }
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'var'.");
        }
    }

    //VAR_INIT-> = EXPRESSION | e
    private void VAR_INIT(){
        if (hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }


    ///*******************Vanessa sentencias

    private void STATEMENT(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }else if (preanalisis.tipo == TipoToken.FOR){
            FOR_STMT();
        }else if (preanalisis.tipo == TipoToken.IF){
            IF_STMT();
        }else if (preanalisis.tipo == TipoToken.PRINT){
            PRINT_STMT();
        }else if (preanalisis.tipo == TipoToken.RETURN){
            RETURN_STMT();
        }else if (preanalisis.tipo == TipoToken.WHILE){
            WHILE_STMT();
        }else if (preanalisis.tipo == TipoToken.LEFT_BRACE){
            BLOCK();
        }
    }

    private void EXPR_STMT(){
        if(hayErrores)
            return;
        EXPRESSION();
        if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
        }
    }

    void FOR_STMT(){
        if(hayErrores) return;

        if(preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                FOR_STMT_1();
                FOR_STMT_2();
                FOR_STMT_3();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN){
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
                else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ')'.");
                }
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'for'.");
        }
    }

    void FOR_STMT_1(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        } else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        } else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'var', una 'expresion' ó ';'.");
        }

    }

    void FOR_STMT_2(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.SEMICOLON){
                match(TipoToken.SEMICOLON);
            }
        } else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
        }
    }

    void FOR_STMT_3(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
        //EPSILON
    }

    void IF_STMT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN){
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                    ELSE_STATEMENT();
                }
                else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ')'.");
                }
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'if'.");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            STATEMENT();
        }

    }

    void PRINT_STMT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            EXPRESSION();
            if(preanalisis.tipo == TipoToken.SEMICOLON){
                match(TipoToken.SEMICOLON);
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'print'.");
        }
    }

    void RETURN_STMT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            RETURN_EXP_OPC();
            if(preanalisis.tipo == TipoToken.SEMICOLON){
                match(TipoToken.SEMICOLON);
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'return'.");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
        //EPSILON

    }
    void WHILE_STMT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN){
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
                else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ')'.");
                }
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'while'");
        }
    }


    void BLOCK(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION();
            if(preanalisis.tipo == TipoToken.RIGHT_BRACE){
                match(TipoToken.RIGHT_BRACE);
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '}'.");
            }
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '{'.");
        }
    }



    ///*******************Rodrigo Expresiones
    public Expression EXPRESSION(){

        if(hayErrores)
            return null;
        return new ExprAssign(preanalisis, ASSIGNMENT());

    }
    public ExprAssign ASSIGNMENT(){
        if(hayErrores)
            return null;
        LOGIC_OR();
        ASSIGNMENT_OPC();
        return null;
    }
    public void ASSIGNMENT_OPC(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }

    }
    public void LOGIC_OR(){
        if(hayErrores)
            return;
        LOGIC_AND();
        LOGIC_OR_2();
    }
    public void LOGIC_OR_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.OR){
            match(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }
    public void LOGIC_AND(){
        if(hayErrores)
            return;
        EQUALITY();
        LOGIC_AND_2();
    }
    public void LOGIC_AND_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.AND){
            match(TipoToken.AND);
            EQUALITY();
            LOGIC_AND_2();
        }
    }
    public void EQUALITY(){
        if(hayErrores)
            return;
        COMPARISON();
        EQUALITY_2();
    }
    public void EQUALITY_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.tipo==TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }
    public void COMPARISON(){
        if(hayErrores)
            return;
        TERM();
        COMPARISON_2();
    }
    public void COMPARISON_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.LESS
                || preanalisis.tipo==TipoToken.LESS_EQUAL
                || preanalisis.tipo==TipoToken.GREATER
                || preanalisis.tipo==TipoToken.GREATER_EQUAL){
            match(TipoToken.LESS);
            match(TipoToken.LESS_EQUAL);
            match(TipoToken.GREATER);
            match(TipoToken.GREATER_EQUAL);

            TERM();
            COMPARISON_2();
        }
    }
    public void TERM(){
        if(hayErrores)
            return;
        FACTOR();
        TERM_2();
    }
    public void TERM_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.PLUS || preanalisis.tipo==TipoToken.MINUS){
            match(TipoToken.MINUS);
            match(TipoToken.PLUS);
            FACTOR();
            TERM_2();
        }
    }
    public void FACTOR(){
        if (hayErrores)
            return;
        Expression exp = UNARY();
        exp=FACTOR_2(exp);
        return exp;
    }
    public Expression FACTOR_2(Expression exp){
        if (hayErrores)
            return null;
        if (preanalisis.tipo==TipoToken.SLASH ){
            match(TipoToken.SLASH);
            Token previous = tokens.get(i - 1);
            Expression expR=UNARY();
            ExprBinary expb =new ExprBinary(exp,previous,expR);
            return FACTOR_2(expb);
        }
        else if ( preanalisis.tipo==TipoToken.STAR){
            match(TipoToken.STAR);
            Token previous = tokens.get(i - 1);
            Expression expR=UNARY();
            ExprBinary expb =new ExprBinary(exp,previous,expR);
            return FACTOR_2(expb);
        }
        return null;
    }
    public Expression UNARY(){
        if(hayErrores)
            return null;
        if(preanalisis.tipo==TipoToken.BANG){
            match(TipoToken.BANG);
            Token previous = tokens.get(i - 1);
            Expression exp=UNARY();
            return new ExprUnary(previous,exp);
        }
        else if(preanalisis.tipo==TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token previus = tokens.get(i - 1);
            Expression exp=UNARY();
            return new ExprUnary(previus,exp);
        }
        else if(
                preanalisis.tipo==TipoToken.TRUE||
                preanalisis.tipo==TipoToken.FALSE||
                preanalisis.tipo==TipoToken.NULL||
                preanalisis.tipo==TipoToken.NUMBER||
                preanalisis.tipo==TipoToken.STRING||
                preanalisis.tipo==TipoToken.IDENTIFIER||
                preanalisis.tipo==TipoToken.LEFT_PAREN){
            return CALL();
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '!', '-', 'true', 'false', 'null', 'number', 'string' o 'identifier'.");
            return null;
        }
    }
    public Expression CALL(){
        if (hayErrores)
            return null;
        Expression exp = PRIMARY();
        exp = CALL_2(exp);
        return exp;
    }
    public Expression CALL_2(Expression exp){
        if (hayErrores)
            return null;
        if (preanalisis.tipo==TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            List<Expression> arguments=ARGUMENTS_OPC();
            if(preanalisis.tipo==TipoToken.RIGHT_PAREN){
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(exp, arguments);
                return CALL_2(ecf);
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        }
        return null;
        //epsilon
    }
    public Expression PRIMARY(){
        if (hayErrores)
            return null;
        if( preanalisis.tipo==TipoToken.TRUE){
            match(TipoToken.TRUE);
            return new ExprLiteral(true);
        }
        else if(preanalisis.tipo==TipoToken.FALSE){
            match(TipoToken.FALSE);
            return new ExprLiteral(false);
        }
        else if(preanalisis.tipo==TipoToken.NUMBER){
            match(TipoToken.NUMBER);
            return new ExprLiteral(preanalisis.literal);
        }
        else if(preanalisis.tipo==TipoToken.STRING){
            match(TipoToken.STRING);
            return new ExprLiteral(preanalisis.literal);
        }
        else if(preanalisis.tipo==TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            return new ExprVariable(preanalisis.lexema);
        }
        else if(preanalisis.tipo==TipoToken.NULL){
            match(TipoToken.NULL);
            return new ExprLiteral(null);
        }
        else if (preanalisis.tipo==TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);

            Expression expr = EXPRESSION();
            if(preanalisis.tipo==TipoToken.RIGHT_PAREN){
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ')'.");
            }
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'true', 'false', 'null', 'number', 'string' o 'identifier'.");
        }
        return null;
    }

    ///*******************Carlitos otras
    //FUNCTION-> id (PARAMETERS_OPC) BLOCK
    private void FUNCTION(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                PARAMETERS_OPC();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    BLOCK();
                }
                else {
                    hayErrores = true;
                    System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ')'.");
                }
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'identifier'.");
        }
    }

    //FUNCTIONS-> FUN_DECL FUNCTIONS | e
    private void FUNCTIONS(){
        if(hayErrores)
            return;
        FUN_DECL();
        FUNCTIONS();
    }

    //PARAMETERS_OPC->PARAMETERS | e
    private void PARAMETERS_OPC(){
        if(hayErrores)
            return;
        PARAMETERS();
    }
    //PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'identifier'.");
        }
    }

    //PARAMETERS_2-> ,id PARAMETERS_2 | e
    private void PARAMETERS_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            if(preanalisis.tipo==TipoToken.IDENTIFIER){
                match(TipoToken.IDENTIFIER);
                PARAMETERS_2();
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'identifier'.");
            }
        }
        //epsilon
    }

    //ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | e
    private void ARGUMENTS_OPC(){
        if(hayErrores)
            return;
        EXPRESSION();
        ARGUMENTS();
    }

    //ARGUMENTS -> , EXPRESSION ARGUMENTS | e
    private void ARGUMENTS(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            EXPRESSION();
            ARGUMENTS();
        }
        //epsilon
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
    }
}
