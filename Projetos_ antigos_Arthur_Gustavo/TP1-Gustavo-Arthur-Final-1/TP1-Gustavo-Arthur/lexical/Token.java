package lexical;

import interpreter.value.Value;

public class Token {

    public static enum Type {
        // Specials.
        INVALID_TOKEN,
        UNEXPECTED_EOF,
        END_OF_FILE,

        // Symbols.
        DOT,               // .
        COMMA,             // ,
        COLON,             // :
        SEMICOLON,         // ;
        OPEN_PAR,          // (
        CLOSE_PAR,         // )
        OPEN_BRA,          // [
        CLOSE_BRA,         // ]
        OPEN_CUR,          // {
        CLOSE_CUR,         // }

        // Operators.
        ASSIGN,            // =
        TERNARY,           // ?
        AND,               // &&
        OR,                // ||
        LOWER_THAN,        // <
        LOWER_EQUAL,       // <=
        GREATER_THAN,      // >
        GREATER_EQUAL,     // >=
        EQUALS,            // ==
        NOT_EQUALS,        // !=
        ADD,               // +
        SUB,               // -
        MUL,               // *
        DIV,               // /
        NOT,               // !
        INC,               // ++
        DEC,               // --

        // Keywords.
        CONST,             // const
        LET,               // let
        DEBUG,             // debug
        IF,                // if
        ELSE,              // else
        WHILE,             // while
        FOR,               // for
        IN,                // in
        UNDEFINED,         // undefined
        FALSE,             // false
        TRUE,              // true
        FUNCTION,          // function
        RETURN,            // return

        // Others.
        NAME,              // identificador
        NUMBER,            // number
        TEXT               // string
    };

    public String lexeme;
    public Type type;
    public int line;
    public Value<?> literal;

    public Token(String lexeme, Type type, Value<?> literal) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = 0;
        this.literal = literal;
    }

    public String toString() {
        return new StringBuffer()
            .append("(\"")
            .append(this.lexeme)
            .append("\", ")
            .append(this.type)
            .append(", ")
            .append(this.line)
            .append(", ")
            .append(this.literal)
            .append(")")
            .toString();
    }

}
