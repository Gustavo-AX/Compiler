package compilador.analisadorlexico;

import compilador.interpretador.value.*;

public class Token {
    public static enum Type {
        // Tokens para controle:
        INVALID_TOKEN,
        UNEXPECTED_EOF,
        END_OF_FILE,

        //Operadores:
        ASSIGN,            // =
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

        // Simbolos:
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


        // Palavras reservadas:
        PROGRAM,           // program
        BEGIN,             // begin
        END,               // end
        INTEGER_CONST,     // int
        FLOAT_CONST,       // float
        CHAR_CONST,        // char 
        IF,                // if
        THEN,              // then
        ELSE,              // else
        REPEAT,            // repeat
        UNTIL,             // until
        WHILE,             // while
        DO,                // do
        IN,                // in
        OUT,               // out
        READ,              // read
        WRITE,             // write

        // Outros:
        IDENTIFIER,        // identificador
        LITERAL            // text
    };

    public String lexeme;       //Lexema é uma palavra
    public Type type;           //É o tipo dos tokens
    public int line;            //Linha
    public Value<?> literal;    //Valor 

    //Construtor, todo token é composto por um lexema (seu "nome", seu tipo, a linha 
    //onde se encontra para gerar o erro, e seu valor)
    public Token(String lexeme, Type type, Value<?> literal) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = 0;
        this.literal = literal;
    }

    @Override
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
