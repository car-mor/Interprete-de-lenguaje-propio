import java.util.List;
public class ASDR implements parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private final Token identificador = new Token(TipoToken.IDENTIFIER, "");
    private final Token fun = new Token(TipoToken.FUN, "fun");
    private final Token var = new Token(TipoToken.VAR, "var");
    private final Token neg_logica = new Token(TipoToken.BANG, "!");
    private final Token puntoycoma = new Token(TipoToken.SEMICOLON, ";");
    private final Token igual = new Token(TipoToken.EQUAL, "=");
    private final Token parentesis_abre = new Token(TipoToken.LEFT_PAREN, "(");
    private final Token parentesis_cierra = new Token(TipoToken.RIGHT_PAREN, ")");
    private final Token coma = new Token(TipoToken.COMMA, ",");
    private final Token resta = new Token(TipoToken.MINUS, "-");

    private final Token este = new Token(TipoToken.THIS, "this");
    private final Token verdadero = new Token(TipoToken.TRUE, "true");

    private final Token de_otro_modo = new Token(TipoToken.ELSE, "else");
    private final Token verdadero = new Token(TipoToken.TRUE, "true");
    private final Token falso = new Token(TipoToken.FALSE, "false");
    private final Token nulo = new Token(TipoToken.NULL, "null");
    private final Token numero = new Token(TipoToken.NUMBER, "number");
    private final Token cadena = new Token(TipoToken.STRING, "string");


    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }
    @Override
    public boolean parse() {

       // PROGRAM();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }

        return false;
    }

    ///*******************Carlitos declaraciones
    public void Program(){
        i = 0;
        preanalisis = tokens.get(i);

        Declaracion();

        if(!hayErrores && !preanalisis.equals(TipoToken.EOF)){
            System.out.println("Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(TipoToken.EOF)){
            System.out.println("Consulta válida");
        }

    }

    private void match(Token t){
        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error encontrado en la posicion"+ preanalisis.linea + ". Se esperaba un  " + t.tipo);
        }

    }
    void Declaracion(){
        if(hayErrores) return;
        if(preanalisis.equals(fun)){
            Fun_decl();
            Declaracion();
        } else if(preanalisis.equals(var)){
            Var_decl();
            Declaracion();
        } else if(preanalisis.equals(para) || preanalisis.equals(si) || preanalisis.equals(imprimir) || preanalisis.equals(regresa) || preanalisis.equals(mientras) || preanalisis.equals(llave_abre) ||  preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesis_abre) || preanalisis.equals(zuper) ){
            Statement();
            Declaracion();
        } else { //EPSILON
        }
    }
    void Fun_decl(){
        if(hayErrores) return;
        if(preanalisis.equals(fun)){
            match(fun);
            Function();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba 'fun'.");
        }
    }
    void Var_decl(){
        if(hayErrores) return;
        if(preanalisis.equals(var)){
            match(var);
            if(preanalisis.equals(identificador)){
                match(identificador);
                Var_init();
                if(preanalisis.equals(puntoycoma)){
                    match(puntoycoma);
                }
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba una 'var'.");
        }
    }

    ///*******************Vanessa sentencias

    private void STATEMENT(){
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }else if (preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            FOR_STMT();
        }else if (preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            IF_STMT();
        }else if (preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            PRINT_STMT();
        }else if (preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            RETURN_STMT();
        }else if (preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            WHILE_STMT();
        }else if (preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            BLOCK();
        }
    }

    private void EXPR_STMT(){
        if(hayErrores)
            return;
        EXPRESSION();
        if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else{
            hayErrores = true;
            System.out.println("Se esperaba ';' ");
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
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'for'.");
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
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'var', una 'expresion' ó ';'.");
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
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'for' o ';'.");
        }
    }

    void FOR_STMT_3(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        } else {
            //EPSILON
        }
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
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'if'.");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            STATEMENT();
        } else {
            //EPSILON
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
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'print'.");
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
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba 'return'.");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        } else {
            //EPSILON
        }
    }
    void WHILE_STMT(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            if(preanalisis.tipo == TipoToken.LEFT_PAREN){
                match(TipoToken.LEFT_PAREN);
                EXPRESSION();
                if(preanalisis.tipo == TipoToken.RIGHT_PAREN)){
                    match(TipoToken.RIGHT_PAREN);
                    STATEMENT();
                }
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba WHILE.");
        }
    }


    void BLOCK(){
        if(hayErrores) return;
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION();
            if(preanalisis.tipo == TipoToken.RIGHT_BRACE){
                match(TipoToken.RIGHT_PAREN);
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la línea " + preanalisis.linea + ". Se esperaba una LLAVE ABRIENDO.");
        }
    }

    ///*******************Rodrigo Expresiones


    ///*******************Carlitos otras
    void Function(){
        if(hayErrores) return;
        if(preanalisis.equals(identificador)){
            match(identificador);
            if(preanalisis.equals(parentesis_abre)){
                match(parentesis_abre);
                Parameters_opc();
                if(preanalisis.equals(parentesis_cierra)){
                    match(parentesis_cierra);
                    Block();
                }
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba 'identificiador'.");
        }
    }
    void Functions(){
        if(hayErrores) return;
        if(preanalisis.equals(identificador)){
            Function();
            Functions();
        } else { //EPSILON

        }

    }
    void Parameters_opc(){
        if(hayErrores) return;
        if(preanalisis.equals(identificador)){
            match(identificador);
            Parameters();
        } else { //EPSILON

        }

    }
    void Parameters(){
        if(hayErrores) return;
        if(preanalisis.equals(identificador)){
            match(identificador);
            Parameters_2();
        } else {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba 'identificador'.");
        }
    }

    void Parameters_2(){
        if(hayErrores) return;
        if(preanalisis.equals(coma)){
            match(coma);
            if(preanalisis.equals(identificador)){
               match(identificador);
                Parameters_2();
            }
        } else { //EPSILON

        }

    }

    void Arguments_opc(){
        if(hayErrores) return;
        if(preanalisis.equals(neg_logica) || preanalisis.equals(resta) || preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesis_abre)){
            Arguments();
        } else { //EPSILON

        }

    }

    void Arguments(){
        if(hayErrores) return;
        Expression();
        Arguments();

    }
}
