import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {



    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }

    private static final ArrayList<Character> caracteresNP =new ArrayList<>() ;

    static {
        caracteresNP.add('#');
        caracteresNP.add('$');
        caracteresNP.add('@');
        caracteresNP.add('¿');
        caracteresNP.add('?');
        for(int i=128;i<256;i++){
            caracteresNP.add((char) i);
        }
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        int columna=0;
        int linea=1;
        char c;

        for(int i=0; i<source.length(); i++, columna++){
            c = source.charAt(i);

            if (c == 10) {
                linea++;
                columna=0;
            }

            //System.out.println(c+" -> "+(int)c);
            switch (estado){
                //caso 0, inicio de diagrama de estados
                case 0:

                    //verifica si inicia en letra para mandar a estado 9
                    if(Character.isLetter(c)){
                        estado = 9;
                        lexema += c;
                    }

                    //verifica si es un numero
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    //error lexico en estado 0
                    else if(caracteresNP.contains(c)){
                        estado=999;
                        lexema += c;
                    }

                    else if (c=='('){

                        Token t=new Token(TipoToken.LEFT_PAREN, ""+c);
                        tokens.add(t);

                    }

                    else if(c==')'){
                        Token t=new Token(TipoToken.RIGHT_PAREN, ""+c);
                        tokens.add(t);
                    }

                    break;

                case 9:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        estado = 9;
                        lexema += c;
                    }
                    else{
                        // Vamos a crear el Token de identificador o palabra reservada
                        TipoToken tt = palabrasReservadas.get(lexema);

                        //identificador
                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        //palabra reservada
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--; columna--;
                    }
                    break;

                //numeros positivos, negativos, Exponencial
                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 16;
                        lexema += c;

                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--; columna--;
                    }
                    break;

                case 16:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    else{
                        Token t=new Token(TipoToken.ERROR_LEXICAL, lexema,columna,linea);
                        tokens.add(t);

                        lexema="";
                        estado=0;
                        i--; columna--;
                    }
                    break;

                case 17:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--; columna--;
                    }
                    break;
                case 999:{
                    if(caracteresNP.contains(c)){
                        lexema += c;
                    }
                    else {
                        Token t=new Token(TipoToken.ERROR_LEXICAL, lexema,columna,linea);
                        tokens.add(t);

                        lexema="";
                        estado=0;
                        i--; columna--;
                    }
                    break;
                }
            }
        }


        return tokens;
    }
}
