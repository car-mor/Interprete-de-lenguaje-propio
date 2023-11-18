import java.util.List;
public class ASDR implements parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


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

        if(preanalisis.tipo==TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
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
            match();
        }
    }
    public void TERM(){

    }
    public void TERM_2(){

    }
    public void FACTOR(){

    }
    public void FACTOR_2(){

    }
    public void UNARY(){

    }
    public void CALL(){

    }
    public void CALL_2(){

    }
    public void PRIMARY(){

    }
    ///*******************Vanessa sentencias


    ///*******************Rodrigo Expresiones


    ///*******************Carlitos otras


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
