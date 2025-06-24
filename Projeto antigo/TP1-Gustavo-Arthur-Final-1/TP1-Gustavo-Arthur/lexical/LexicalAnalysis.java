package lexical;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

import interpreter.value.NumberValue;
import interpreter.value.TextValue;


public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private PushbackInputStream input;
    private static Map<String, Token.Type> keywords;

    static {
        keywords = new HashMap<String, Token.Type>();

        // Symbols.
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

        // Operators.
        keywords.put("=", Token.Type.ASSIGN);
        keywords.put("?", Token.Type.TERNARY);
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
        keywords.put("++", Token.Type.INC);
        keywords.put("--", Token.Type.DEC);

        // Keywords.
        keywords.put("const", Token.Type.CONST);
        keywords.put("let", Token.Type.LET);
        keywords.put("debug", Token.Type.DEBUG);
        keywords.put("if", Token.Type.IF);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("while", Token.Type.WHILE);
        keywords.put("for", Token.Type.FOR);
        keywords.put("in", Token.Type.IN);
        keywords.put("undefined", Token.Type.UNDEFINED);
        keywords.put("false", Token.Type.FALSE);
        keywords.put("true", Token.Type.TRUE);
        keywords.put("function", Token.Type.FUNCTION);
        keywords.put("return", Token.Type.RETURN);
    }

    public LexicalAnalysis(InputStream is) {
        input = new PushbackInputStream(is);
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new LexicalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Token nextToken() {
        Token token = new Token("", Token.Type.END_OF_FILE, null);

        int state = 1;
        while (state != 13 && state != 14) {
            int c = getc();
            // System.out.printf(" [%02d, %03d ('%c')]\n",
            // state, c, (char) c);

            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' ||
                            c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        state = 1;
                        line++;
                    } else if (c == '.' || c == ',' || c == ':' ||
                            c == ';' || c == '?' || c == '*' ||
                            c == '(' || c == ')' || c == '{' ||
                            c == '}' || c == '[' || c == ']') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else if (c == '/') {
                        state = 2;
                        token.lexeme += (char) c;
                    } else if (c == '=' || c == '!' || c == '<' || c == '>') {
                        state = 4;
                        token.lexeme += (char) c;
                    } else if (c == '+') {
                        state = 5;
                        token.lexeme += (char) c;
                    } else if (c == '-') {
                        state = 6;
                        token.lexeme += (char) c;
                    } else if (c == '&') {
                        state = 7;
                        token.lexeme += (char) c;
                    } else if (c == '|') {
                        state = 8;
                        token.lexeme += (char) c;
                    } else if (c == '_' || c == '$' ||
                            Character.isLetter(c)) {
                        state = 9;
                        token.lexeme += (char) c;
                    } else if (Character.isDigit(c)) {
                        state = 10;
                        token.lexeme += (char) c;
                    } else if (c == '"') {
                        state = 12;                    
                    } else if (c == -1) {
                        state = 14;
                        token.type = Token.Type.END_OF_FILE;
                    } else {
                        state = 14;
                        token.lexeme += (char) c;
                        token.type = Token.Type.INVALID_TOKEN;
                    }

                    break;
                case 2:
                    if (c == '/') {
                        state = 3;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }
                    break;
                case 3:
                    if (c == '\n') {
                        state = 1;
                        token.lexeme += (char) c;
                        //reseto o token lexeme, já que os comentários devem ser desconsiderados
                        token.lexeme = "";
                    } else {
                        state = 3;
                        token.lexeme += (char) c;
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
                    if (c == '+') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }

                    break;
                case 6:
                    if (c == '-') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }

                    break;
                case 7:
                    if (c == '&') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        token.type = Token.Type.INVALID_TOKEN;
                    }

                    break;
                case 8:
                    if (c == '|') {
                        state = 13;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        token.type = Token.Type.INVALID_TOKEN;
                    }

                    break;
                case 9:
                    if (c == '_' || c == '$' ||
                            Character.isLetter(c) ||
                            Character.isDigit(c)) {
                        state = 9;
                        token.lexeme += (char) c;
                    } else {
                        state = 13;
                        ungetc(c);
                    }

                    break;
                case 10:
                    if (Character.isDigit(c)) {
                        state = 10;
                        token.lexeme += (char) c;
                    } else if (c == '.') {
                        state = 11;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.NUMBER;
                        token.literal = new NumberValue(toNumber(token.lexeme));
                    }

                    break;
                case 11:
                    if (Character.isDigit(c)) {
                        state = 11;
                        token.lexeme += (char) c;
                    } else {
                        state = 14;
                        ungetc(c);
                        token.type = Token.Type.NUMBER;
                        token.literal = new NumberValue(toNumber(token.lexeme));
                    }
                    break;
                case 12:
                    if (c != '"') {
                        state = 12;
                        token.lexeme += (char) c;
                         
                    } else {
                        state = 14;
                        token.type = Token.Type.TEXT;
                        token.literal = new TextValue(token.lexeme);
                    }
                    break;
                default:
                    throw new RuntimeException("Unreachable");
            }
        }

        if (state == 13)
            token.type = keywords.containsKey(token.lexeme) ? keywords.get(token.lexeme) : Token.Type.NAME;

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

    private double toNumber(String lexeme) {
        try {
            return Double.parseDouble(lexeme);
        } catch (Exception e) {
            return 0.0;
        }
    }

}
