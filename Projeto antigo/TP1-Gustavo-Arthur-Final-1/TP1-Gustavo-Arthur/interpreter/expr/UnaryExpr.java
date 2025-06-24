package interpreter.expr;

import interpreter.InterpreterException;
import interpreter.value.BoolValue;
import interpreter.value.NumberValue;
import interpreter.value.Value;

public class UnaryExpr extends Expr {

    public static enum Op {
        Not,
        Pos,
        Neg,
        PreInc,
        PosInc,
        PreDec,
        PosDec
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
            case Pos:
                return posOp(v);
            case Neg:
                return negOp(v);
            case PreInc:
                return preIncOp(v);
            case PosInc:
                return posIncOp(v);
            case PreDec:
                return preDecOp(v);
            case PosDec:
            default:
                return posDecOp(v);
        }
    }

    private Value<?> notOp(Value<?> v) {
        boolean b = BoolValue.convert(v);
        return new BoolValue(!b);
    }

    private Value<?> posOp(Value<?> v) {
        double n = NumberValue.convert(v);
        return new NumberValue(n);
    }

    private Value<?> negOp(Value<?> v) {
        double n = NumberValue.convert(v);
        return new NumberValue(-n);
    }

    private Value<?> preIncOp(Value<?> v) {
        v = expr.expr();
        double d = NumberValue.convert(v);
        double d1 = d + 1;
        Value<?> v2 = new NumberValue(d1);

        if (expr instanceof SetExpr) {
            SetExpr aux = (SetExpr) expr;
            aux.setValue(v2);
            return v2;
        }
        throw new InterpreterException(super.getLine());
    }

    private Value<?> posIncOp(Value<?> v) {
        v = expr.expr();
        double d = NumberValue.convert(v);
        double d1 = d + 1;
        //crio dois, retorno o antigo mas seto o valor do novo
        Value<?> v1 = new NumberValue(d);
        Value<?> v2 = new NumberValue(d1);

        if (expr instanceof SetExpr) {
            SetExpr aux = (SetExpr) expr;
            aux.setValue(v2);
            return v1;
        }
        throw new InterpreterException(super.getLine());
    }

    private Value<?> preDecOp(Value<?> v) {
        v = expr.expr();
        double d = NumberValue.convert(v);
        double d1 = d - 1;
        Value<?> v2 = new NumberValue(d1);

        if (expr instanceof SetExpr) {
            SetExpr aux = (SetExpr) expr;
            aux.setValue(v2);
            return v2;
        }
        throw new InterpreterException(super.getLine());
    }

    private Value<?> posDecOp(Value<?> v) {
        v = expr.expr();
        double d = NumberValue.convert(v);
        double d1 = d - 1;
        //crio dois, retorno o antigo mas seto o valor do novo
        Value<?> v1 = new NumberValue(d);
        Value<?> v2 = new NumberValue(d1);

        if (expr instanceof SetExpr) {
            SetExpr aux = (SetExpr) expr;
            aux.setValue(v2);
            return v1;
        }
        throw new InterpreterException(super.getLine());
    }
}
