package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class DebugCommand extends Command {

    private Expr expr;

    public DebugCommand(int line, Expr expr) {
        super(line);
        this.expr = expr;
    }

    @Override
    public void execute() {
        Value<?> v = expr.expr();
        if (v == null)
            System.out.println("undefined");
        else
            System.out.println(v);
    }

}
