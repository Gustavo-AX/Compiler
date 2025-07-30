package semantic.expr;

import semantic.SemanticException;
import semantic.value.BoolValue;
import semantic.value.DoubleValue;
import semantic.value.IntegerValue;
import semantic.value.Value;

public class UnaryExpr extends Expr {

    public static enum Op {
        Not,
        Neg,
    }

    private Expr expr;
    private Op op;

    public UnaryExpr(int line, Expr expr, Op op) {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    public Value<?> expr() {
        Value<?> v = this.expr.expr();
        switch (this.op) {
            case Not:
                return notOp(v);
            case Neg:
                return negOp(v);
            default:
                throw new SemanticException(super.getLine(),"Operação unária não permitida");
        }
    }

    private Value<?> notOp(Value<?> v) {
        // Int -> Int
        // Float -> Float
        
        if(v instanceof BoolValue){
            return new BoolValue(null);
        }
        throw new SemanticException(super.getLine(),"Operação unária de negação não permitida");
    }

    private Value<?> negOp(Value<?> v) {
        // Bool -> Bool
        if(v instanceof IntegerValue){
            return new IntegerValue(null);
        } else if(v instanceof DoubleValue){
            return new DoubleValue(null);
        }
        throw new SemanticException(super.getLine(),"Operação unária inversão de negação não permitida");
    
    }
}
