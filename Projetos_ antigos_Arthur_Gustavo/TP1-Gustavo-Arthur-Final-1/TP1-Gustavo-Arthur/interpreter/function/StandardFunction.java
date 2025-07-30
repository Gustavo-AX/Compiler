package interpreter.function;

import interpreter.command.Command;
import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.value.Value;

public class StandardFunction extends Function {

    private Command cmds;
    private Expr ret;

    public StandardFunction(Variable params, Command cmds, Expr ret) {
        super(params);
        this.cmds = cmds;
        this.ret = ret;
    }

    @Override
    public Value<?> call() {
        cmds.execute();
        return ret != null ? ret.expr() : null;
    }
}
