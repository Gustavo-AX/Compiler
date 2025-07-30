package semantic;

public class SemanticException extends RuntimeException {

    public SemanticException(int line, String mensagem) {
        super(String.format("%02d: Operação semântica inválida: " + mensagem, line));
    }
}
