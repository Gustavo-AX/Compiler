package interpreter.expr;

import java.util.ArrayList;
import java.util.List;
import interpreter.InterpreterException;
import interpreter.function.Function;
import interpreter.value.FunctionValue;
import interpreter.value.ListValue;
import interpreter.value.Value;

public class FunctionCallExpr extends Expr {

    private Expr expr;
    private List<Expr> args;

    public FunctionCallExpr(int line, Expr expr, List<Expr> args) {
        super(line);
        this.expr = expr;
        this.args = args;
    }
    
    @Override
    public Value<?> expr() {
        Value<?> v = expr.expr();
        if (v instanceof FunctionValue) {
            FunctionValue fv = (FunctionValue) v;
            Function f = fv.value();

            List<Value<?>> a = new ArrayList<Value<?>>();
            for (Expr e : args)
                a.add(e.expr());

            ListValue lv = new ListValue(a);
            Variable params = f.getParams();
            params.setValue(lv);
            
            return f.call();
        } else {
            throw new InterpreterException(super.getLine());
        }
    }
}
