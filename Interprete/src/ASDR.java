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
        } else if (preanalisis.tipo == TipoToken.BANG||preanalisis.tipo == TipoToken.FOR||preanalisis.tipo == TipoToken.IF||preanalisis.tipo==TipoToken.PRINT||preanalisis.tipo == TipoToken.RETURN||preanalisis.tipo==TipoToken.WHILE||preanalisis.tipo==TipoToken.LEFT_BRACE) {
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
            }
        } else{
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
        else{
            hayErrores = true;
            System.out.println("Error encontrado");
        }

    }
}
