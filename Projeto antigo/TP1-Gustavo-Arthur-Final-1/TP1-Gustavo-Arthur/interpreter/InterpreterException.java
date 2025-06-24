package interpreter;

public class InterpreterException extends RuntimeException {

    public InterpreterException(int line) {
        super(String.format("%02d: Operação inválida", line));
    }
}
