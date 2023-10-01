public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final Integer columnaE;
    final Integer filaE;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.columnaE = null;
        this.filaE = null;
    }
    public Token(TipoToken tipo, String lexema, int columnaE, int filaE) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.columnaE = columnaE;
        this.filaE = filaE;
    }
    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.columnaE = null;
        this.filaE = null;
    }

    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + " linea:"+filaE+" columna:"+columnaE+">";
    }
}
