package lexical;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

import semantic.value.*;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private PushbackInputStream input;
    private static Map<String, Token.Type> keywords;

    //Declara os nomes reservados na nossa tabela.
    static {
        keywords = new HashMap<String, Token.Type>();

        // Simbolos:
        keywords.put(".", Token.Type.DOT);
        keywords.put(",", Token.Type.COMMA);
        keywords.put(":", Token.Type.COLON);
        keywords.put(";", Token.Type.SEMICOLON);
        keywords.put("(", Token.Type.OPEN_PAR);
        keywords.put(")", Token.Type.CLOSE_PAR);
        keywords.put("[", Token.Type.OPEN_BRA);
        keywords.put("]", Token.Type.CLOSE_BRA);
        keywords.put("{", Token.Type.OPEN_CUR);
        keywords.put("}", Token.Type.CLOSE_CUR);

        // Operadores
        keywords.put("=", Token.Type.ASSIGN);
        keywords.put("&&", Token.Type.AND);
        keywords.put("||", Token.Type.OR);
        keywords.put("<", Token.Type.LOWER_THAN);
        keywords.put("<=", Token.Type.LOWER_EQUAL);
        keywords.put(">", Token.Type.GREATER_THAN);
        keywords.put(">=", Token.Type.GREATER_EQUAL);
        keywords.put("==", Token.Type.EQUALS);
        keywords.put("!=", Token.Type.NOT_EQUALS);
        keywords.put("+", Token.Type.ADD);
        keywords.put("-", Token.Type.SUB);
        keywords.put("*", Token.Type.MUL);
        keywords.put("/", Token.Type.DIV);
        keywords.put("!", Token.Type.NOT);

        // Palavras chave:
        keywords.put("program", Token.Type.PROGRAM);
        keywords.put("begin", Token.Type.BEGIN);
        keywords.put("end", Token.Type.END);
        keywords.put("int", Token.Type.INT);
        keywords.put("float", Token.Type.FLOAT);
        keywords.put("char", Token.Type.CHAR);
        keywords.put("if", Token.Type.IF);
        keywords.put("then", Token.Type.THEN);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("repeat", Token.Type.REPEAT);
        keywords.put("until", Token.Type.UNTIL);
        keywords.put("while", Token.Type.WHILE);
        keywords.put("do", Token.Type.DO);
        keywords.put("in", Token.Type.IN);
        keywords.put("out", Token.Type.OUT);
    }

    public LexicalAnalysis(InputStream is) {
        input = new PushbackInputStream(is);
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            System.out.println("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    // Chamado por fora, para obter o próximo token
    // Se baseia no diagrama de transições da linguagem
    // Posto no zip junto com esses códigos
    public Token nextToken() {
        Token token = new Token("", Token.Type.END_OF_FILE, null);

        int state = 1;
        while (state != 14 && state != 13) {
            int c = getc();
            // System.out.printf(" [%02d, %03d ('%c')]\n",
            // state, c, (char) c);

            switch (state) {
                case 1:
                    // Espaçamento:
                    if (c == ' ' || c == '\t' ||
                            c == '\r') {
                        state = 1;
                    //Quebra de linha:
                    } else if (c == '\n') {
                        state = 1;
                        line++;
                    // Simbolos:
                    } else if (c == '.' || c == ',' || c == ':' ||
                            c == ';' || c == '*' || c == '/' || c == '+' || c == '-' ||
                            c == '(' || c == ')' ||  c == '[' || c == ']') {
                        state = 13;
                        token.lexeme += (char) c;
                    // Primeiro caracter do ||:
                    } else if (c == '|') {
                        state = 2;
                        token.lexeme += (char) c;
                    // Primeiro caracter do &&:
                    } else if (c == '&') {
                        state = 3;
                        token.lexeme += (char) c;
                    // Pode ser o primeiro caracter do <=, >= ... ou não
                    } else if (c == '=' || c == '!' || c == '<' || c == '>') {
                        state = 4;
                        token.lexeme += (char) c;
                    // Início do comentario de linha
                    } else if (c == '%') {
                        state = 5;
                        token.lexeme += (char) c;
                    // Início do comentario multilinha
                    } else if (c == '{') {
                        state = 6;
                        token.lexeme += (char) c;
                    // Inicio de um nome
                    } else if (Character.isLetter(c) || (c == '_')) {
                        state = 7;
                        token.lexeme += (char) c;
                    //Inicio de um numero
                    } else if (Character.isDigit(c)) {
                        state = 8;
                        token.lexeme += (char) c;
                    // Inicio da declaração de um caracter - char
                    } else if (c == '\'') {
                        state = 10;   
                    // Inicio de uma string                 
                    } else if(c == '"') {
                        state = 12;
                    // Fim de arquivo
                    } else if (c == -1) {
                        state = 14;
                        token.type = Token.Type.END_OF_FILE;
                    // Algo que não pertence a linguagem:
                    } else {
                        state = 14;
                        token.lexeme += (char) c;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Caracter não permitido.");
                    }
                    break;

                case 2:
                    if (c == '|') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Erro Lexico: operador de OU deve ser '||', mas foi encontrado somente '|'");
                    }
                    break;
                case 3:
                    if (c == '&') {
                        state = 13;
                        token.lexeme += (char) c;

                    } else {
                        state = 14;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Erro Lexico: operador de E deve ser '&&', mas foi encontrado somente '&'");
                    }
                    break;
                case 4:
                    if (c == '=') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }
                    break;
                case 5:
                    if(c == -1) {
                        state = 14;
                        token.lexeme = "";
                        token.type = Token.Type.END_OF_FILE;
                    } else if (c != '\n') {
                        state = 5;
                        token.lexeme += (char) c;
                    } else {
                        state = 1;
                        ungetc(c);
                        //reseto o token lexeme, já que os comentários devem ser desconsiderados
                        token.lexeme = "";
                    }

                    break;
                    
                case 6:
                    if(c == -1) {
                        state = 14;
                        token.lexeme = "";
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Fim de arquivo inesperado.");
                    }
                    if (c != '}') {
                        state = 6;
                        if(c == '\n')
                            line++;
                    } else {
                        state = 1;
                        token.lexeme = "";
                    }

                    break;
                case 7:
                    if (Character.isLetter(c) ||
                            Character.isDigit(c) || c == '_') {
                        state = 7;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }
                    break;

                case 8:
                    if (Character.isDigit(c)) {
                        state = 8;
                        token.lexeme += (char) c;
                    } else if (c == '.') {
                        state = 9;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.INTEGER_CONST;
                        token.literal = new IntegerValue(toInt(token.lexeme));
                    }
                    break;
                
                case 9:
                    if (Character.isDigit(c)) {
                        state = 15;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Float inválido.");
                    }
                    break;

                //Esse caso não esta previamente no diagrama pois é um correção. Será
                // incluido em próximas etapas.
                case 15:
                    if (Character.isDigit(c)) {
                        state = 15;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.FLOAT_CONST;
                        token.literal = new DoubleValue(toDouble(token.lexeme));
                    }
                    break;

                case 10:
                    if (c == '\'') {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Char vazio não é permitido.");
                    } else if(c == -1) {
                        state = 14;
                        token.lexeme += (char) c;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Erro Lexico: caractere do tipo char invalido na linha.");

                    } else {      
                        state = 11;
                        token.lexeme += (char) c;
                    }
                    break;
                
                case 11:
                    if(c == '\''){
                        state = 14;
                        token.type = Token.Type.CHAR_CONST;
                        token.literal = new CharValue(token.lexeme.charAt(0));
                    } else {
                        state = 14;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Erro Lexico: aspas simples esperadas na linha");
                    }
                    break;
                case 12:
                    if (c == '"') {
                        state = 14;
                        token.type = Token.Type.LITERAL;
                        token.literal = new TextValue(token.lexeme);
                    } else if(c == -1){
                        state = 14;
                        token.lexeme += (char) c;
                        token.type = Token.Type.INVALID_TOKEN;
                        throw new LexicalException("Na linha " + this.line + "->" +"Erro Lexico: aspas duplas esperadas na linha");

                    } else{
                        state = 12;
                        token.lexeme += (char) c;
                    }
                    break;
                default:
                    throw new RuntimeException("Unreachable");
            }
        }

        if (state == 13)
            token.type = keywords.containsKey(token.lexeme) ? keywords.get(token.lexeme) : Token.Type.IDENTIFIER;

        token.line = this.line;

        return token;
    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new LexicalException("Unable to read file");
        }
    }

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }

    private double toDouble(String lexeme) {
        try {
            return Double.parseDouble(lexeme);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int toInt(String lexeme) {
        try {
            return Integer.parseInt(lexeme);
        } catch (Exception e) {
            return 0;
        }
    }
}
