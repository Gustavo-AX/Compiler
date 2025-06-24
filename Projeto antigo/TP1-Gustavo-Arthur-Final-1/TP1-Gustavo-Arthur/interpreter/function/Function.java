package interpreter.function;

import interpreter.expr.Variable;
import interpreter.value.Value;

public abstract class Function {

    private Variable params;

    public Function(Variable params) {
        this.params = params;
    }

    public Variable getParams() {
        return params;
    }

    public abstract Value<?> call();

}
