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

    ///*******Carlitos declaraciones
    //PROGRAM->DECLARATION
    private Expression PROGRAM() {
        DECLARATION();
        return null;
    }

    //DECLARATION->FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | e
    private Expression DECLARATION() {
        if (hayErrores)
            return null;
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
    private Expression FUN_DECL(){
        if (hayErrores)
            return null;
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
    private Expression VAR_DECL(){
        if (hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            if(preanalisis.tipo == TipoToken.IDENTIFIER) {
                match(TipoToken.IDENTIFIER);
                Expression exp = VAR_INIT();
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
    private Expression VAR_INIT(Expression expr){
        if (hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Expression exp = EXPRESSION();
        }
        return expr;
    }

    ///*******************Vanessa sentencias

    public Statement STATEMENT(){
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            StmtExpression exp = EXPR_STMT();
        }else if (preanalisis.tipo == TipoToken.FOR){
            StmtLoop exp = FOR_STMT();
        }else if (preanalisis.tipo == TipoToken.IF){
            StmtIf exp = IF_STMT();
        }else if (preanalisis.tipo == TipoToken.PRINT){
            StmtPrint exp = PRINT_STMT();
        }else if (preanalisis.tipo == TipoToken.RETURN){
            StmtReturn exp = RETURN_STMT();
        }else if (preanalisis.tipo == TipoToken.WHILE){
            WHILE_STMT();
        }else if (preanalisis.tipo == TipoToken.LEFT_BRACE){
            StmtBlock exp = BLOCK();
        }
        return null;
    }

    public StmtExpression EXPR_STMT(){
        if(hayErrores)
            return null;
        EXPRESSION();
        if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba ';'.");
        }
        return null;
    }

    StmtLoop FOR_STMT(){
        if(hayErrores) return null;

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
        return null;
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

    public StmtIf IF_STMT(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);

                Expression exp = EXPRESSION();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN){
                    match(TipoToken.RIGHT_PAREN);

                    Statement thenBranch = STATEMENT();
                    Statement elseBranch = ELSE_STATEMENT();

                    StmtIf stm= new StmtIf(exp,thenBranch,elseBranch);

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
        return null;
    }

    public Statement ELSE_STATEMENT(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            return STATEMENT();
        }
        return null;
    }

    public StmtPrint PRINT_STMT(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            Expression exp = EXPRESSION();
            StmtPrint stm= new StmtPrint(exp);
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
        return null;
    }

    public StmtReturn RETURN_STMT(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            return RETURN_EXP_OPC();
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
        return null;
    }

    public StmtReturn RETURN_EXP_OPC(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression exp = EXPRESSION();
            StmtReturn stm = new StmtReturn(exp);
        }
        //EPSILON
        return null;
    }

    Statement WHILE_STMT(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                Expression exp = EXPRESSION();
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
        return null;
    }


    public StmtBlock BLOCK(){
        if(hayErrores) return null;
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            return DECLARATION();
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
        return null;
    }

    private Statement BLOCK(StmtBlock exp2) {


    }


    ///*******************Rodrigo Expresiones
    public void EXPRESSION(){

        if(hayErrores)
            return ;
        ASSIGNMENT();
    }
    public void ASSIGNMENT(){
        if(hayErrores)
            return;
        LOGIC_OR();
        ASSIGNMENT_OPC();
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
        UNARY();
        FACTOR_2();
    }
    public void FACTOR_2(){
        if (hayErrores)
            return;
        if (preanalisis.tipo==TipoToken.SLASH || preanalisis.tipo==TipoToken.STAR){
            match(TipoToken.SLASH);
            match(TipoToken.STAR);
            UNARY();
            FACTOR_2();
        }
    }
    public void UNARY(){
        if(hayErrores)
            return;
        if(preanalisis.tipo==TipoToken.BANG || preanalisis.tipo==TipoToken.MINUS){
            match(TipoToken.BANG);
            match(TipoToken.MINUS);
            UNARY();
        }
        else if(
                preanalisis.tipo==TipoToken.TRUE||
                        preanalisis.tipo==TipoToken.FALSE||
                        preanalisis.tipo==TipoToken.NULL||
                        preanalisis.tipo==TipoToken.NUMBER||
                        preanalisis.tipo==TipoToken.STRING||
                        preanalisis.tipo==TipoToken.IDENTIFIER||
                        preanalisis.tipo==TipoToken.LEFT_PAREN){
            CALL();
        }
        else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '!', '-', 'true', 'false', 'null', 'number', 'string' o 'identifier'.");
        }
    }
    public void CALL(){
        if (hayErrores)
            return;
        PRIMARY();
        CALL_2();
    }
    public void CALL_2(){
        if (hayErrores)
            return;
        if (preanalisis.tipo==TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            ARGUMENTS_OPC();
            if(preanalisis.tipo==TipoToken.RIGHT_PAREN){
                match(TipoToken.RIGHT_PAREN);
                CALL_2();
            }
            else {
                hayErrores = true;
                System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba '('.");
            }
        }
        //epsilon
    }
    public void PRIMARY(){
        if (hayErrores)
            return ;
        if( preanalisis.tipo==TipoToken.TRUE){
            match(TipoToken.TRUE);
            //return new ExprLiteral(true);
        }
        else if(preanalisis.tipo==TipoToken.FALSE){
            match(TipoToken.FALSE);
            //return new ExprLiteral(false);
        }
        else if(preanalisis.tipo==TipoToken.NUMBER){
            match(TipoToken.NUMBER);
           // return new ExprLiteral(preanalisis.literal);
        }
        else if(preanalisis.tipo==TipoToken.STRING){
            match(TipoToken.STRING);
           // return new ExprLiteral(preanalisis.literal);
        }
        else if(preanalisis.tipo==TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
           // return new ExprVariable(preanalisis.lexema);
        }
        else if(preanalisis.tipo==TipoToken.NULL){
            match(TipoToken.NULL);
           // return new ExprLiteral(null);
        }
        else if (preanalisis.tipo==TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            if(preanalisis.tipo==TipoToken.RIGHT_PAREN){
                match(TipoToken.RIGHT_PAREN);
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

    }

    ///*******Carlitos otras
    //FUNCTION-> id (PARAMETERS_OPC) BLOCK
    private Statement FUNCTION(){
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                List<Statement> parameters= PARAMETERS_OPC();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN) {
                    match(TipoToken.RIGHT_PAREN);
                    StmtBlock exp2 = new StmtBlock(parameters);
                    return BLOCK(exp2);
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
        return null;
    }

    //FUNCTIONS-> FUN_DECL FUNCTIONS | e
    private Statement FUNCTIONS(Expression expr){
        if(hayErrores)
            return null;
        FUN_DECL();
//        StmtFunction(Token name, List<Token> params, StmtBlock body)
        Token name = null;
        List<Token> parameters = null;
        StmtBlock body = BLOCK();
        StmtFunction expr = new StmtFunction(null, null, body);
        return FUNCTIONS(expr);

    }

    //PARAMETERS_OPC->PARAMETERS | e
    private List<Statement> PARAMETERS_OPC(){
        if(hayErrores)
            return null;
        PARAMETERS();
    }
    //PARAMETERS -> id PARAMETERS_2
    private Statement PARAMETERS(){
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea +", columna: "+ preanalisis.columnaE+ ". Se esperaba 'identifier'.");
        }
    }

    //PARAMETERS_2-> ,id PARAMETERS_2 | e
    private Statement PARAMETERS_2(){
        if(hayErrores)
            return null;
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
    private List<Expression> ARGUMENTS_OPC(){
        if(hayErrores)
            return null;
        List<Expression> argumentos=new ArrayList<>();
        argumentos.add(EXPRESSION());

        return ARGUMENTS(argumentos);
    }
    //ARGUMENTS -> , EXPRESSION ARGUMENTS | e
    private List<Expression> ARGUMENTS(List<Expression>argumentos){
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            Expression exp=EXPRESSION();
            argumentos.add(exp);
            argumentos=ARGUMENTS(argumentos);
            return argumentos;
        }
        return null;
    }
    

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
    }
}
