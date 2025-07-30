package semantic.expr;

import lexical.Token;
import semantic.value.Value;

public class Variable extends Expr {

    private String name;
    private Value<?> value;

    public Variable(Token name) {
        super(name.line);
        this.name = name.lexeme;
    }

    public String getName() {
        return this.name;
    }

    public void initialize(Value<?> value) {
        this.value = value;
    }

    public Value<?> expr() {
        return this.value;
    }

    public void setValue(Value<?> value) {
        this.value = value;
    }

}
