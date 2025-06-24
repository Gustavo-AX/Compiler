package interpreter.expr;

import interpreter.InterpreterException;
import interpreter.value.Value;
import lexical.Token;

public class Variable extends SetExpr {

    private String name;
    private boolean constant;
    private Value<?> value;

    public Variable(Token name) {
        this(name, false);
    }

    public Variable(Token name, boolean constant) {
        super(name.line);

        this.name = name.lexeme;
        this.constant = constant;
        this.value = null;
    }

    public String getName() {
        return this.name;
    }

    public boolean isConstant() {
        return this.constant;
    }

    public void initialize(Value<?> value) {
        this.value = value;
    }

    public Value<?> expr() {
        return this.value;
    }

    public void setValue(Value<?> value) {
        if (this.isConstant())
            throw new InterpreterException(super.getLine());

        this.value = value;
    }

}
