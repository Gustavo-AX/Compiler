package lexical;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private PushbackInputStream input;
    private static Map<String, Token.Type> keywords;

    static {
        keywords = new HashMap<String, Token.Type>();
        // SYMBOLS
        keywords.put("!", Token.Type.NOT);
        keywords.put(".", Token.Type.DOT);
        keywords.put("<", Token.Type.LOWER_THAN);
        keywords.put(">", Token.Type.GREATER_THAN);
        keywords.put("(", Token.Type.OPEN_PAR);
        keywords.put(")", Token.Type.CLOSE_PAR);
        // OPERATORS

        // KEYWORDS
        keywords.put("#define", Token.Type.DEFINE);
        keywords.put("defined", Token.Type.DEFINED);
        keywords.put("#undef", Token.Type.UNDEF);
        keywords.put("#error", Token.Type.ERROR);
        keywords.put("#include", Token.Type.INCLUDE);
        keywords.put("#ifdef", Token.Type.IFDEF);
        keywords.put("#ifndef", Token.Type.IFNDEF);
        keywords.put("#if", Token.Type.IF);
        keywords.put("#elif", Token.Type.ELIF);
        keywords.put("#endif", Token.Type.ENDIF);
        keywords.put("#else", Token.Type.ELSE);
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
        Token token = new Token("", Token.Type.END_OF_FILE);

        int state = 1;
        while (state != 9 && state != 8 && state != 7) {
            int c = getc();

            /*System.out.printf(" [%02d, %03d ('%c')]\n",
                    state, c, (char) c);*/

            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' ||
                            c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        state = 1;
                        line++;
                    } else if (c == '!' || c == '(' || c == ')' || c == '<' || c == '>') {
                        state = 9;
                        token.lexeme += (char) c;
                    } else if (c == '#') {
                        state = 2;
                        token.lexeme += (char) c;
                    } else if (c == '.' || Character.isLetter(c)) {
                        state = 4;
                        token.lexeme += (char) c;
                    } else if (Character.isDigit(c)) {
                        state = 5;
                        token.lexeme += (char) c;
                    } else if (c == '"') {
                        state = 6;
                    } else if (c == -1) {
                        state = 7;
                        token.type = Token.Type.END_OF_FILE;
                    } else {
                        throw new LexicalException("Invalid token");
                    }
                    break;

                case 2:
                    if (c == '\r' || c == '\t' || c == ' ') {
                        state = 2;
                    } else if (Character.isLetter(c)) {
                        state = 3;
                        token.lexeme += (char) c;
                    } else {
                        throw new LexicalException("Invalid token");
                    }
                    break;

                case 3:
                    if (Character.isLetter(c)) {
                        state = 3;
                        token.lexeme += (char) c;
                    } else {
                        state = 8;
                        ungetc(c);
                    }
                    break;

                case 4:
                    if (c == '.' || Character.isLetter(c) || Character.isDigit(c)) {
                        state = 4;
                        token.lexeme += (char) c;
                    } else {
                        state = 9;
                        ungetc(c);
                    }
                    break;

                case 5:
                    if (Character.isDigit(c)) {
                        state = 5;
                        token.lexeme += (char) c;
                    } else {
                        state = 7;
                        ungetc(c);
                        token.type = Token.Type.NUMBER;
                    }
                    break;

                case 6:
                    if (c != '"') {
                        state = 6;
                        token.lexeme += (char) c;
                    } else {
                        state = 7;
                        token.type = Token.Type.TEXT;
                    }
                    break;
                default:
                    throw new RuntimeException("Unreachable");
            }

        }

        if (state == 8) {
            token.type = keywords.containsKey(token.lexeme) ? keywords.get(token.lexeme) : Token.Type.INVALID_TOKEN;
        }
        if (state == 9) {
            token.type = keywords.containsKey(token.lexeme) ? keywords.get(token.lexeme) : Token.Type.NAME;     
        }
        if (token.type == Token.Type.INVALID_TOKEN){
            throw new LexicalException("Invalid token");
        }
        token.line = this.line;
        //System.out.println(token.type);
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

}
