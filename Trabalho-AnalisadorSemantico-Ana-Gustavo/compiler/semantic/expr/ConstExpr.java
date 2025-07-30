package semantic.expr;

import semantic.expr.Expr;
import semantic.value.Value;

public class ConstExpr extends Expr {

    private Value<?> value;

    public ConstExpr(int line, Value<?> value) {
        super(line);
        this.value = value;
    }

    @Override
    public Value<?> expr() {
        return value;
    }
    
}
