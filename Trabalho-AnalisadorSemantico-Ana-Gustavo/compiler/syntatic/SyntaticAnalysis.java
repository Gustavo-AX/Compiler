package syntatic;

//Tem alguns tokens que nem precisa, mas também não atrapalha
//a não ser que tente usar uma palavra reservada que é desnecessária
import java.util.ArrayList;
import java.util.List;
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
import semantic.Environment;
import semantic.Semantic;
import semantic.SemanticException;
import semantic.expr.BinaryExpr;
import semantic.expr.ConstExpr;
import semantic.expr.Expr;
import semantic.expr.UnaryExpr;
import semantic.expr.Variable;
import semantic.value.BoolValue;
import semantic.value.CharValue;
import semantic.value.DoubleValue;
import semantic.value.IntegerValue;
import semantic.value.Value;




public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Token current;
    private Token previous;
    private Environment environment;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
        this.previous = null;
        this.environment = Semantic.globals;
    }

    private void advance() {
        //System.out.println("Found " + current);
        previous = current;
        current = lex.nextToken();
    }

    private void eat(Token.Type type) {
        if (type == current.type) {
            advance();
        } else {
            //System.out.println("Expected (..., " + type + ", ..., ...), found " +
            //current);
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

    public void process() {

        //Sem ação semantica, é feita mais abaixo

        procProgram();
        eat(END_OF_FILE);
    }

// program ::= program [decl-list] begin stmt-list end ";"
    private void procProgram() {

        //Sem ação semantica, é feita mais abaixo
        
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
// As declarações não precisam ser retornadas, isso por que não vou fazer nada
// com elas acima. Se tiver erro com elas, será retratado nelas mesmo
    private void procDeclList() {

        //Sem ação semantica, é feita mais abaixo

        procDecl();

        while (check(INT, FLOAT, CHAR)) {
                procDecl();       
        }
    }


// decl ::= type “:” ident-list ";"
    private void procDecl() {
        
        // Cada chamada dessa cria n variaveis com um tipo T.
        // Para cada variável dessa eu devo ver se ela já foi declarada

        Value<?> current_type = procType();

        eat(COLON);

        List<Token> names = procIdentList();

        // Se uma delas já ter sido declarada, vai gerar erro na classe Enviroment
        for(int i = 0; i<names.size(); i++){
            this.environment.declare(names.get(i), current_type);
        }
        
        eat(SEMICOLON);
    }

// ident-list ::= identifier {"," identifier}
    private List<Token> procIdentList() {

        //Sem ação semantica, é feita mais abaixo

        List<Token> identificadores = new ArrayList<>();

        eat(IDENTIFIER);
        identificadores.add(previous);
        
        //Vai pegando identificadores separados por virgula
        while (match(COMMA)) {
            eat(IDENTIFIER);
            identificadores.add(previous);    
        }
        return identificadores;
    }

// type ::= int | float | char
    private Value<?> procType() {

        //Sem ação semantica, é feita mais abaixo


        if (check(INT, FLOAT, CHAR)) {
            if(match(INT)){
                return new IntegerValue(null);
            } else if(match(FLOAT)){
                return new DoubleValue(null);
            } else if(match(CHAR)){
                return new CharValue(null);
            }
        } else {
            System.out.println("No procType");
            reportError();
        }
        return null;
    }

// stmt-list ::= stmt {";" stmt}
    private void procStmtList() {

        //Sem ação semantica, é feita mais abaixo

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

        // Regra semantica: só pode fazer a atribuição se a variável e a express~ao
        // Forem do mesmo tipo

        eat(IDENTIFIER);

        Token name;
        name = previous;

        eat(ASSIGN);

        Expr expr = procSimpleExpr();

        //Se não são do mesmo tipo, não é possível atribuir
        if(!(environment.get(name).expr().getClass() == expr.expr().getClass()))
            throw new SemanticException(current.line, "Não é são do mesmo tipo");

    }


// if-stmt ::= if condition then [decl-list] stmt-list end | if condition then [decl-list] stmt-list else declaration stmt- list end
    private void procIfStmt() {

        // Não há ação semantica, a verificação se é condição (tem valor booleano)
        // é feita no bloco condition 
        // Mas tem a criação de nova tabela de simbolos

        // Crio uma nova TS e passo a antiga como parâmetro
        // Na hora de declarar ou pesquisar eu busco na atual e na antiga
        // Por isso não tem problema eu criar mesmo não tendo declaração nenhuma feita
        // Essa nova é perdida no fim do comando. Podendo assim reutilizar variáveis
        // Em outros blocos

        Environment old = this.environment;
        this.environment = new Environment(old);

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

        this.environment = old;
    }

// repeat-stmt ::= repeat [decl-list] stmt-list stmt-suffix

    private void procRepeatStmt() {

        // Não há ação semantica, a verificação se é condição (tem valor booleano)
        // é feita no bloco condition 
        // Mas tem a criação de nova tabela de simbolos

        Environment old = this.environment;
        this.environment = new Environment(old);

        eat(REPEAT);

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        procStmtList();

        procStmtSuffix();

        this.environment = old;

    }

// stmt-suffix ::= until condition
    private void procStmtSuffix() {

        // Não há ação semantica, a verificação se é condição (tem valor booleano)
        // é feita no bloco condition 

        eat(UNTIL);

        procCondition();
    }

// while-stmt ::= stmt-prefix [decl-list] stmt-list end
    private void procWhileStmt() {

        // Não há ação semantica, a verificação se é condição (tem valor booleano)
        // é feita no bloco condition 
        // Mas tem a criação de nova tabela de simbolos

        Environment old = this.environment;
        this.environment = new Environment(old);

        procStmtPrefix();

        if (check(INT, FLOAT, CHAR)) {
            procDeclList();
        }

        procStmtList();

        eat(END);

        this.environment = old;
    }

// stmt-prefix ::= while condition do
    private void procStmtPrefix() {

        // Não há ação semantica, a verificação se é condição (tem valor booleano)
        // é feita no bloco condition 

        eat(WHILE);
        procCondition();
        eat(DO);
    }

// condition ::= expression
    private BoolValue procCondition() {
        // Preciso saber se a expressão que eu achei é um valor booleano
        // Sendo que valores booleanos só são gerados a partir de comparações
        // == != > >= < <= 

        Expr expr = procExpression();
        Value v = expr.expr();

        if(v instanceof BoolValue){
            return new BoolValue(null);
        }

        throw new SemanticException(current.line, "Não é uma condição");
    }


// read-stmt ::= in "(" identifier ")"
    private void procReadStmt() {

        //Só preciso saber a variável já foi declarada

        eat(IN);

        eat(OPEN_PAR);

        eat(IDENTIFIER);

        // Se não tiver sido declarada, vai acusar erro
        environment.get(previous);

        eat(CLOSE_PAR);
    }

// write-stmt ::= out "(" writable ")"
    private void procWriteStmt() {

        // Esse método não tem muito erro, não sei o que poderia ser
        // printado que daria ruim

        eat(OUT);

        eat(OPEN_PAR);

        procWritable();

        eat(CLOSE_PAR);
    }

// writable ::= simple-expr | literal
    private Expr procWritable() {

        Expr expr = null;
        int line = current.line;

        if (check(IDENTIFIER)) {
            expr = procSimpleExpr();
        } else if (check(LITERAL)) {
            eat(current.type); 
            expr = new ConstExpr(line, previous.literal);
        } else {
            System.out.println("No procWritable");
            reportError();
        }
        return expr;
    }

// expression ::= simple-expr | simple-expr relop simple-expr
    private Expr procExpression() {

        Expr expr = procSimpleExpr();
        int line = current.line;

        if (check(EQUALS, GREATER_THAN, GREATER_EQUAL, LOWER_THAN, LOWER_EQUAL, NOT_EQUALS)) {
            BinaryExpr.Op op = procRelOp();

            Expr simple_expr = procSimpleExpr();

            expr = new BinaryExpr(line, expr, op, simple_expr);
        }

        return expr;
    }

// simple-expr ::= term | simple-expr addop term
    private Expr procSimpleExpr() {

        Expr expr = procTerm();
        int line = current.line;

        while (check(ADD, SUB, OR)) {
            BinaryExpr.Op op = procAddOp();

            Expr term = procTerm();

            expr = new BinaryExpr(line, expr, op, term);
        }

        return expr;
    }

// term ::= factor-a | term mulop factor-a
    private Expr procTerm() {

        Expr expr = procFactorA();
        int line = current.line;
        
        while (check(MUL, DIV, AND)) {
            BinaryExpr.Op op = procMulOp();

            Expr factor_a = procFactorA();

            expr = new BinaryExpr(line, expr, op, factor_a);
        }

        return expr;
    }

// fator-a ::= factor | ! factor | "-" factor
    private Expr procFactorA() {

        Expr expr = null;
        int line = current.line;

        if (match(NOT, SUB)) {
            switch (previous.type) {
                case NOT:
                    expr = new UnaryExpr(line, procFactor(), UnaryExpr.Op.Not);
                    break;
                case SUB:
                    expr = new UnaryExpr(line, procFactor(), UnaryExpr.Op.Neg);
                    break;
                default:
                    break;
            }
        } else {
            expr = procFactor();
        }

        return expr;
    }

// factor ::= identifier | constant | "(" expression ")"
    private Expr procFactor() {

        Expr expr = null;
        int line = current.line;

        if (match(IDENTIFIER)){
            Variable var = environment.get(previous);
            expr = var;
        } else if (check(INTEGER_CONST, FLOAT_CONST, CHAR_CONST)) {
            expr = procConstant();
        } else if (match(OPEN_PAR)) {
            expr = procExpression();
            eat(CLOSE_PAR);
        } else {
            System.out.println("No procFactor");
            reportError();
        }

        return expr;
    }

// relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
    private BinaryExpr.Op procRelOp() {

        BinaryExpr.Op op = null;

        if (match(EQUALS, GREATER_THAN, GREATER_EQUAL, LOWER_THAN, LOWER_EQUAL, NOT_EQUALS)){
            switch (previous.type) {
                case EQUALS:
                    op = BinaryExpr.Op.Equal;
                    break;
                case GREATER_THAN:
                    op = BinaryExpr.Op.GreaterThan;
                    break;
                case GREATER_EQUAL:
                    op = BinaryExpr.Op.GreaterEqual;
                    break;
                case LOWER_THAN:
                    op = BinaryExpr.Op.LowerThan;
                    break;
                case LOWER_EQUAL:
                    op = BinaryExpr.Op.LowerEqual;
                    break;
                case NOT_EQUALS:
                    op = BinaryExpr.Op.NotEqual;
                    break;
                default:
                    System.out.println("Erro no procRelOP");
                    reportError();
            }
            return op;
        } else {
            System.out.println("No procRelOP");
            reportError();
            return null;
        }

    }

// addop ::= "+" | "-" | ||
    private BinaryExpr.Op procAddOp() {

        BinaryExpr.Op op = null;

        if (match(ADD, SUB, OR)){

            switch (previous.type) {
                case ADD:
                    op = BinaryExpr.Op.Add;
                    break;
                case SUB:
                    op = BinaryExpr.Op.Sub;
                    break;
                case OR:
                    op = BinaryExpr.Op.Or;
                    break;
                default:
                    System.out.println("No procAddOP");
                    reportError();
                    return null;
            }
            return op;
        } else {
            System.out.println("No procAddOP");
            reportError();
            return null;
        }
    }

// mulop ::= "*" | "/" | &&
    private BinaryExpr.Op procMulOp() {

        BinaryExpr.Op op = null;

            if (match(MUL, DIV, AND)){

                switch (previous.type) {
                    case MUL:
                        op = BinaryExpr.Op.Mul;
                        break;
                    case DIV:
                        op = BinaryExpr.Op.Div;
                        break;
                    case AND:
                        op = BinaryExpr.Op.And;
                        break;
                    default:
                        System.out.println("No procMulOP");
                        reportError();
                        return null;
                }
                return op;
            } else {
                System.out.println("No procMulOP");
                reportError();
                return null;
            }
    }
    
// constant ::= integer_const | float_const | char_const
    private Expr procConstant() {
        Expr expr = null;
        int line = current.line;

        if(match(INTEGER_CONST, FLOAT_CONST, CHAR_CONST)){
            expr = new ConstExpr(line, previous.literal);
        }

        return expr;
    }
} 