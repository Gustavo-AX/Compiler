package compilador;

import compilador.analisadorlexico.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class exe {

    public static void main(String[] args) {
        File pastaTestes = new File("testes");
        File[] arquivos = pastaTestes.listFiles((dir, name) -> name.endsWith(".txt"));

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum arquivo de teste encontrado na pasta 'testes'.");
            return;
        }

        for (File arquivo : arquivos) {
            System.out.println("Analisando: " + arquivo.getName());

            try (LexicalAnalysis lex = new LexicalAnalysis(new FileInputStream(arquivo))) {
                Token token;
                do {
                    token = lex.nextToken();
                    System.out.printf("Token: %-15s Lexema: %-10s Linha: %d%n",
                            token.type, token.lexeme, token.line);
                } while (token.type != Token.Type.END_OF_FILE);
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + arquivo.getName());
            } catch (Exception e) {
                System.err.println("Erro ao analisar léxico: " + e.getMessage());
            }

            System.out.println(); 
        }
    }
}
