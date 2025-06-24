    package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;
import interpreter.value.BoolValue;

public class IfCommand extends Command {

    private Command thenCmds;
    private Command elseCmds;
    private Expr expr;

    public IfCommand(int line, Expr expr, Command thenCmds, Command elseCmds) {
        super(line);
        this.expr = expr;
        this.thenCmds = thenCmds;
        this.elseCmds = elseCmds;
    }

    @Override
    public void execute() {
        Value<?> v = expr.expr();
        //converto a expressão para um boolean, e rodo o comando then, se expr for true, se n, rodo o elseCmds
        if (BoolValue.convert(v)) {
            thenCmds.execute();
        } else {
            elseCmds.execute();
        }
    }

}
