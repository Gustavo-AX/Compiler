package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.BoolValue;
import interpreter.value.Value;

public class WhileCommand extends Command {

    private Expr expr;
    private Command cmds;

    public WhileCommand(int line, Expr expr, Command cmds) {
        super(line);
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        while (true) {
            Value<?> v = expr.expr();
            boolean b = BoolValue.convert(v);
            if (!b)
                break;

            cmds.execute();
        }
    }

}
