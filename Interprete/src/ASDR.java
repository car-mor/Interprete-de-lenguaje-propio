
import java.util.List;
public class ASDR implements parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDR(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
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
            System.out.println("Se esperaba un 'fun'");
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
                else{
                    hayErrores=true;
                    System.out.println("Se esperaba un ';'");
                }
            }
            else{
                hayErrores=true;
                System.out.println("Se esperaba un Identificador");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba una 'var'");
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


    ///*******************Rodrigo Expresiones
    public void EXPRESSION(){
        if(hayErrores)
            return;
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
        else{
            hayErrores=true;
            System.out.println("Se esperaba un '!' o un '-'");
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
            else{
                hayErrores=true;
                System.out.println("Se esperaba un ')'");
            }
        }
    }
    public void PRIMARY(){
        if (hayErrores)
            return;
        if( preanalisis.tipo==TipoToken.TRUE||
            preanalisis.tipo==TipoToken.FALSE||
            preanalisis.tipo==TipoToken.NULL||
            preanalisis.tipo==TipoToken.NUMBER||
            preanalisis.tipo==TipoToken.STRING||
            preanalisis.tipo==TipoToken.IDENTIFIER){

            match(TipoToken.TRUE);
            match(TipoToken.FALSE);
            match(TipoToken.NULL);
            match(TipoToken.NUMBER);
            match(TipoToken.STRING);
            match(TipoToken.IDENTIFIER);
        }
        else if (preanalisis.tipo==TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            if(preanalisis.tipo==TipoToken.RIGHT_PAREN){
                match(TipoToken.RIGHT_PAREN);
            }
            else{
                hayErrores=true;
                System.out.println("Se esperaba un ')'");
            }
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un true, false, null, number, string o identificador");
        }

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
            }
        } else{
            hayErrores = true;
            System.out.println("Se esperaba un 'id'");
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
        } else{
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador'");
        }
    }

    //PARAMETERS_2-> ,id PARAMETERS_2 | e
    private void PARAMETERS_2(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            PARAMETERS_2();
        } else{
            hayErrores = true;
            System.out.println("Se esperaba una 'coma'");
        }
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
        } else {
            hayErrores = true;
            System.out.println("Se esperaba un 'coma'");
        }
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
    }
}
