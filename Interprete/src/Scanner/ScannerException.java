package Scanner;

public class ScannerException extends Exception {
    public ScannerException(StringBuilder message) {
        super(String.valueOf(message));
    }
}
