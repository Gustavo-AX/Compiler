package interpreter.command;

import java.util.List;
import java.util.Map;

import interpreter.InterpreterException;
import interpreter.expr.Expr;
import interpreter.value.ListValue;
import interpreter.value.ObjectValue;
import interpreter.value.TextValue;
import interpreter.value.Value;
import interpreter.expr.Variable;

public class ForCommand extends Command {

    private Expr expr;
    private Command cmds;
    private Variable var;

    public ForCommand(int line, Variable var, Expr expr, Command cmds) {
        super(line);
        this.expr = expr;
        this.cmds = cmds;
        this.var = var;
    }

    @Override
    public void execute() {
        Value<?> v = expr.expr();
        // se for uma lista
        if (v instanceof ListValue) {
            ListValue lv = (ListValue) v;
            List<Value<?>> value = lv.value();
            // percorro o list e atribuo o valor a variável
            for (int i = 0; i < value.size(); i++) {
                var.setValue(value.get(i));
                cmds.execute();
            }
            // se for um object
        } else if (v instanceof ObjectValue) {
            ObjectValue ov = (ObjectValue) v;
            Map<TextValue, Value<?>> value = ov.value();
            // percorro o map e atribuo o valor da chave a variável
            for (TextValue key : value.keySet()) {
                var.setValue(key);
                cmds.execute();
            }
            // se não for list ou map, gero o erro
        } else {
            throw new InterpreterException(super.getLine());
        }

    }
}
