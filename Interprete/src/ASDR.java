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

    private final Token verdadero = new Token(TipoToken.TRUE, "true");

    private final Token verdadero = new Token(TipoToken.TRUE, "true");
    private final Token falso = new Token(TipoToken.FALSE, "false");
    private final Token nulo = new Token(TipoToken.NULL, "null");
    private final Token numero = new Token(TipoToken.NUMBER, "number");
    private final Token cadena = new Token(TipoToken.STRING, "string");
    private final Token para  = new Token(TipoToken.FOR, "for");
    private final Token si = new Token(TipoToken.IF, "if");
    private final Token imprimir = new Token(TipoToken.PRINT, "print");

    private final Token mientras = new Token(TipoToken.WHILE, "while");
    private final Token regresa = new Token(TipoToken.RETURN, "return");


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

    void Statement(){
        if(hayErrores) return;
        if(preanalisis.equals(neg_logica) || preanalisis.equals(resta) || preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesis_abre) || preanalisis.equals(zuper) ){
            Expr_stmt();
        } else if(preanalisis.equals(para)){
            For_stmt();
        } else if(preanalisis.equals(si)){
            If_stmt();
        }else if(preanalisis.equals(imprimir)){
            Print_stmt();
        } else if(preanalisis.equals(regresa)){
            Return_stmt();
        } else if(preanalisis.equals(mientras)){
            While_stmt();
        } else if(preanalisis.equals(llave_abre)){
            Block();
        }
    }

    void Var_init(){
        if(hayErrores) return;
        if(preanalisis.equals(igual)){
            match(igual);
            Expression();
        } else { //EPSILON

        }

    }

    ///*******************Vanessa sentencias
    void Block(){
        if(hayErrores) return;
        if(preanalisis.equals(llave_abre)){
            match(llave_abre);
            Block_decl();
            if(preanalisis.equals(llave_cierra)){
                match(llave_cierra);
            }
        } else {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba una LLAVE ABRIENDO.");
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
