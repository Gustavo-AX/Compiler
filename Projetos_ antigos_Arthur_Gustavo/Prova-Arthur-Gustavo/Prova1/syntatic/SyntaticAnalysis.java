package syntatic;

//import java.util.List;

import lexical.LexicalAnalysis;
import lexical.Token;

//import static lexical.Token.Type.UNEXPECTED_EOF;
//import static lexical.Token.Type.INVALID_TOKEN;
import static lexical.Token.Type.END_OF_FILE;

//import static lexical.Token.Type.DOT;
import static lexical.Token.Type.NOT;
import static lexical.Token.Type.LOWER_THAN;
import static lexical.Token.Type.GREATER_THAN;
import static lexical.Token.Type.OPEN_PAR;
import static lexical.Token.Type.CLOSE_PAR;
import static lexical.Token.Type.DEFINE;
import static lexical.Token.Type.DEFINED;
import static lexical.Token.Type.UNDEF;
import static lexical.Token.Type.ERROR;
import static lexical.Token.Type.INCLUDE;
import static lexical.Token.Type.IFDEF;
import static lexical.Token.Type.IFNDEF;
import static lexical.Token.Type.IF;
import static lexical.Token.Type.ELIF;
import static lexical.Token.Type.ELSE;
import static lexical.Token.Type.ENDIF;
import static lexical.Token.Type.NAME;
import static lexical.Token.Type.TEXT;
import static lexical.Token.Type.NUMBER;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    //private Token previous;
    private Token current;
    private Token next;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        //this.previous = null;
        this.current = lex.nextToken();
        this.next = lex.nextToken();
    }

    public void process() {
        procCode();
        eat(END_OF_FILE);
    }

    private void advance() {
        //System.out.println("Found " + current);
        //previous = current;
        current = next;
        next = lex.nextToken();
    }

    private void eat(Token.Type type) {
        if (type == current.type) {
            advance();
        } else {
            /*System.out.println("Expected (..., " + type + ", ..., ...), found " +
                    current);*/
            reportError();
        }
    }

    private boolean check(Token.Type... types) {
        for (Token.Type type : types) {
            if (current.type == type)
                return true;
        }

        return false;
    }

    /*
     * private boolean checkNext(Token.Type... types) {
     * for (Token.Type type : types) {
     * if (next.type == type)
     * return true;
     * }
     * 
     * return false;
     * }
     */

    private boolean match(Token.Type... types) {
        if (check(types)) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    private void reportError() {
        String reason;
        switch (current.type) {
            case INVALID_TOKEN:
                reason = String.format("Lexema inválido [%s]", current.lexeme);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                reason = "Fim de arquivo inesperado";
                break;
            default:
                reason = String.format("Lexema não esperado [%s]", current.lexeme);
                break;
        }

        throw new SyntaticException(current.line, reason);
    }

    // <code> ::= {<macros>}
    private void procCode() {
        // int line = current.line;
        while (check(DEFINE, UNDEF, ERROR, INCLUDE, IFDEF, IFNDEF, IF)) {
            procMacros();      
        }
    }

    // <macros> ::= #define|#undef|#error|#include|#ifdef|#ifndef|#if
    private void procMacros() {
        // int line = current.line;
        if (check(DEFINE)) {
            procDefine();
        } else if (check(UNDEF)) {
            procUndef();
        } else if (check(ERROR)) {
            procError();
        } else if (check(INCLUDE)) {
            procInclude();
        } else if (check(IFDEF)) {
            procIfdef();
        } else if (check(IFNDEF)) {
            procIfndef();
        } else if (check(IF)) {
            procIf();
        }
    }

    // <#define> ::= #define <name> [<TEXT> | <NUMBER> ]
    private void procDefine() {
        eat(DEFINE);
        eat(NAME);
        match(TEXT, NUMBER);
    }

    // <#undef> ::= #undef <name>
    private void procUndef() {
        eat(UNDEF);
        eat(NAME);
    }

    // <#error> ::= #error <text>
    private void procError() {
        eat(ERROR);
        eat(TEXT);
    }

    // <#include> ::= #include ( <text> | ('<' <name> '>'))
    private void procInclude() {
        eat(INCLUDE);
        if (check(LOWER_THAN)) {
            eat(LOWER_THAN);
            eat(NAME);
            eat(GREATER_THAN);
        } else {
            eat(TEXT);
        }
    }

    // <#ifdef> ::= #ifdef <name> <macros> [#elif(s) | #else] #endif
    private void procIfdef() {
        eat(IFDEF);
        eat(NAME);
        procCode();
        if (check(ELIF)) {
            procElif();
        }
        if (check(ELSE)) {
            procElse();
        }
        eat(ENDIF);
    }

    // <#ifndef> ::= #ifndef <name> <macros> [#elif(s) | #else] #endif
    private void procIfndef() {
        eat(IFNDEF);
        eat(NAME);
        procCode();
        if (check(ELIF)) {
            procElif();
        }
        if (check(ELSE)) {
            procElse();
        }
        eat(ENDIF);
    }

    // <#if> ::= #if [!] defined '('<name>')' <macros> [ #elif(s) | #else] #endif
    private void procIf() {
        eat(IF);
        match(NOT);
        eat(DEFINED);
        eat(OPEN_PAR);
        eat(NAME);
        eat(CLOSE_PAR);
        procCode();
        if (check(ELIF)) {
            procElif();
        }
        if (check(ELSE)) {
            procElse();
        }
        eat(ENDIF);
    }

    // <#elif> ::= #elif [!] defined '('<name>')' <macro> [#elif(s) | #else]
    private void procElif() {
        eat(ELIF);
        match(NOT);
        eat(DEFINED);
        eat(OPEN_PAR);
        eat(NAME);
        eat(CLOSE_PAR);
        procCode();
        if (check(ELIF)) {
            procElif();
        }
        if (check(ELSE)) {
            procElse();
        }
    }

    // <#else> ::= #else <macros>
    private void procElse() {
        eat(ELSE);
        procCode();
    }
}
