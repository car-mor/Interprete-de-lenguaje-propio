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

        PROGRAM();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }

        return false;
    }

    ///*******************Carlitos declaraciones


    ///*******************Vanessa sentencias


    ///*******************Rodrigo Expresiones


    ///*******************Carlitos otras
}
