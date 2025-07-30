package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.value.Value;

public class InitializeCommand extends Command {

    private Variable var;
    private Expr expr;

    public InitializeCommand(int line, Variable var, Expr expr) {
        super(line);
        this.var = var;
        this.expr = expr;
    }

    @Override
    public void execute() {
        Value<?> v = expr.expr();
        var.initialize(v);
    }

    
}
