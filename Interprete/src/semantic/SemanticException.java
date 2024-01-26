package semantic;

public class SemanticException extends Exception{
    public SemanticException(StringBuilder message){super (String.valueOf(message));}
}
