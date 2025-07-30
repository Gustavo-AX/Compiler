package syntatic;

//Tem alguns tokens que nem precisa, mas também não atrapalha
//a não ser que tente usar uma palavra reservada que é desnecessária
import lexical.LexicalAnalysis;
import lexical.Token;
import static lexical.Token.Type.ADD;
import static lexical.Token.Type.AND;
import static lexical.Token.Type.ASSIGN;
import static lexical.Token.Type.BEGIN;
import static lexical.Token.Type.CHAR;
import static lexical.Token.Type.CHAR_CONST;
import static lexical.Token.Type.CLOSE_PAR;
import static lexical.Token.Type.COLON;
import static lexical.Token.Type.COMMA;
import static lexical.Token.Type.DIV;
import static lexical.Token.Type.DO;
import static lexical.Token.Type.ELSE;
import static lexical.Token.Type.END;
import static lexical.Token.Type.END_OF_FILE;
import static lexical.Token.Type.EQUALS;
import static lexical.Token.Type.FLOAT;
import static lexical.Token.Type.FLOAT_CONST;
import static lexical.Token.Type.GREATER_EQUAL;
import static lexical.Token.Type.GREATER_THAN;
import static lexical.Token.Type.IDENTIFIER;
import static lexical.Token.Type.IF;
import static lexical.Token.Type.IN;
import static lexical.Token.Type.INT;
import static lexical.Token.Type.INTEGER_CONST;
import static lexical.Token.Type.LITERAL;
import static lexical.Token.Type.LOWER_EQUAL;
import static lexical.Token.Type.LOWER_THAN;
import static lexical.Token.Type.MUL;
import static lexical.Token.Type.NOT;
import static lexical.Token.Type.NOT_EQUALS;
import static lexical.Token.Type.OPEN_PAR;
import static lexical.Token.Type.OR;
import static lexical.Token.Type.OUT;
import static lexical.Token.Type.PROGRAM;
import static lexical.Token.Type.REPEAT;
import static lexical.Token.Type.SEMICOLON;
import static lexical.Token.Type.SUB;
import static lexical.Token.Type.THEN;
import static lexical.Token.Type.UNTIL;
import static lexical.Token.Type.WHILE;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Token current;
    private Token previous;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
        this.previous = null;
    }

    public void process() {
        procProgram();
        eat(END_OF_FILE);
    }


    private void advance() {
        System.out.println("Found " + current);
        previous = current;
        current = lex.nextToken();
    }

    private void eat(Token.Type type) {
        if (type == current.type) {
            advance();
        } else {
            System.out.println("Expected (..., " + type + ", ..., ...), found " +
            current);
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

// program ::= program [decl-list] begin stmt-list end ";"
    private void procProgram() {
        eat(PROGRAM);

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        eat(BEGIN);
        
        procStmtList();

        eat(END);

        eat(SEMICOLON);
    }

// decl-list ::= decl {decl}
    private void procDeclList() {
        procDecl();

        while (check(INT, FLOAT, CHAR)) {
                procDecl();        
        }
    }


// decl ::= type “:” ident-list ";"
    private void procDecl() {
        procType();
        eat(COLON);
        procIdentList();
        eat(SEMICOLON);
    }

// ident-list ::= identifier {"," identifier}
    private void procIdentList() {
        eat(IDENTIFIER);

        //Vai pegando identificadores separados por virgula
        while (match(COMMA)) {
            eat(IDENTIFIER);
        }
    }

// type ::= int | float | char
    private void procType() {
        if (match(INT, FLOAT, CHAR)) {
            // Faz nada por enquanto
        } else {
            System.out.println("No procType");
            reportError();
        }
    }

// stmt-list ::= stmt {";" stmt}
    private void procStmtList() {
        procStmt();

        while (check(IDENTIFIER, IF, WHILE , REPEAT, IN, OUT)) {
            procStmt();
        }
    }

// stmt ::= (assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt) ";"
    private void procStmt() {
        if (check(IDENTIFIER)) {
            procAssignStmt();
        } else if (check(IF)) {
            procIfStmt();
        } else if (check(WHILE)) {
            procWhileStmt();
        } else if (check(REPEAT)) {
            procRepeatStmt();
        } else if (check(IN)) {
            procReadStmt();
        } else if (check(OUT)) {
            procWriteStmt();
        } else {
            System.out.println("No ProcSTMT");
            reportError();
        }
        eat(SEMICOLON);
    }


// assign-stmt ::= identifier "=" simple_expr
    private void procAssignStmt() {
        eat(IDENTIFIER);

        eat(ASSIGN);

        procSimpleExpr();
    }


// if-stmt ::= if condition then [decl-list] stmt-list end | if condition then [decl-list] stmt-list else declaration stmt- list end
    private void procIfStmt() {
        eat(IF);

        procCondition();

        eat(THEN);

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        procStmtList();

        if (match(ELSE)) {
            if (check(INT, FLOAT, CHAR)) {
                procDeclList();
            }
            procStmtList();
        }

        eat(END);
    }


// condition ::= expression

    private void procCondition() {

        procExpression();

    }

// repeat-stmt ::= repeat [decl-list] stmt-list stmt-suffix

    private void procRepeatStmt() {
        eat(REPEAT);

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        procStmtList();

        procStmtSuffix();
    }

// stmt-suffix ::= until condition
    private void procStmtSuffix() {
        eat(UNTIL);

        procCondition();
    }

// while-stmt ::= stmt-prefix [decl-list] stmt-list end
    private void procWhileStmt() {

        procStmtPrefix();

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        procStmtList();

        eat(END);
    }

// stmt-prefix ::= while condition do
    private void procStmtPrefix() {
        eat(WHILE);
        procCondition();
        eat(DO);
    }


// read-stmt ::= in "(" identifier ")"
    private void procReadStmt() {
        eat(IN);

        eat(OPEN_PAR);

        eat(IDENTIFIER);

        eat(CLOSE_PAR);
    }

// write-stmt ::= out "(" writable ")"
    private void procWriteStmt() {
        eat(OUT);

        eat(OPEN_PAR);

        procWritable();

        eat(CLOSE_PAR);
    }

// writable ::= simple-expr | literal
    private void procWritable() {

        if (check(IDENTIFIER)) {
            procSimpleExpr();
        } else if (check(LITERAL)) {
            eat(current.type); 
        } else {
            System.out.println("No procWritable");
            reportError();
        }
    }

// expression ::= simple-expr | simple-expr relop simple-expr
    private void procExpression() {

        procSimpleExpr();

        if (check(EQUALS, GREATER_THAN, GREATER_EQUAL, LOWER_THAN, LOWER_EQUAL, NOT_EQUALS)) {
            procRelOp();

            procSimpleExpr();
        }
    }

// simple-expr ::= term | simple-expr addop term
    private void procSimpleExpr() {

        procTerm();

        while (check(ADD, SUB, OR)) {
            procAddOp();

            procTerm();
        }
    }

// term ::= factor-a | term mulop factor-a
    private void procTerm() {

        procFactorA();

        while (check(MUL, DIV, AND)) {
            procMulOp();

            procFactorA();
        }
    }

// fator-a ::= factor | ! factor | "-" factor
    private void procFactorA() {

        if (match(NOT, SUB)) procFactor();

        else procFactor();
    }

// factor ::= identifier | constant | "(" expression ")"
    private void procFactor() {
        if (match(IDENTIFIER)){
            // faz nada por enquanto
        } else if (match(INTEGER_CONST, FLOAT_CONST, CHAR_CONST)) {
            // faz nada por enquanto, nem preciso chamar procConstant, como são terminais
            // já coloco aqui
        } else if (match(OPEN_PAR)) {
            procExpression();
            eat(CLOSE_PAR);
        } else {
            System.out.println("No procFactor");
            reportError();
        }
    }

// relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
    private void procRelOp() {
        if (!match(EQUALS, GREATER_THAN, GREATER_EQUAL, LOWER_THAN, LOWER_EQUAL, NOT_EQUALS)){
            System.out.println("No procRelOP");
            reportError();
        } 
    }

// addop ::= "+" | "-" | ||
    private void procAddOp() {
        if (!match(ADD, SUB, OR)){
            System.out.println("No procAddOP");
            reportError();
        } 
    }

// mulop ::= "*" | "/" | &&
    private void procMulOp() {
        if (!match(MUL, DIV, AND)){
            System.out.println("No procMulOP");
            reportError();
        } 
    }
    
// constant ::= integer_const | float_const | char_const
}