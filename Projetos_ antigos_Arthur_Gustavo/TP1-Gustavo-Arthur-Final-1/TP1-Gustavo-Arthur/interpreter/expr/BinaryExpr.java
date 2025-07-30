package interpreter.expr;

import interpreter.InterpreterException;
import interpreter.value.BoolValue;
import interpreter.value.FunctionValue;
import interpreter.value.ListValue;
import interpreter.value.NumberValue;
import interpreter.value.ObjectValue;
import interpreter.value.TextValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    public static enum Op {
        And,
        Or,
        Equal,
        NotEqual,
        LowerThan,
        LowerEqual,
        GreaterThan,
        GreaterEqual,
        Add,
        Sub,
        Mul,
        Div
    }

    private Expr left;
    private Op op;
    private Expr right;

    public BinaryExpr(int line, Expr left, Op op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr() {
        Value<?> v1 = left.expr();
        Value<?> v2 = right.expr();

        Value<?> res;
        switch (op) {
            case And:
                res = andOp(v1, v2);
                break;
            case Or:
                res = orOp(v1, v2);
                break;
            case Equal:
                res = equalOp(v1, v2);
                break;
            case NotEqual:
                res = notEqualOp(v1, v2);
                break;
            case LowerThan:
                res = lowerThanOp(v1, v2);
                break;
            case LowerEqual:
                res = lowerEqualOp(v1, v2);
                break;
            case GreaterThan:
                res = greaterThanOp(v1, v2);
                break;
            case GreaterEqual:
                res = greaterEqualOp(v1, v2);
                break;
            case Add:
                res = addOp(v1, v2);
                break;
            case Sub:
                res = subOp(v1, v2);
                break;
            case Mul:
                res = mulOp(v1, v2);
                break;
            case Div:
            default:
                res = divOp(v1, v2);
                break;
        }

        return res;
    }

    private Value<?> andOp(Value<?> v1, Value<?> v2) {
        Boolean d1 = BoolValue.convert(v1);
        Boolean d2 = BoolValue.convert(v2);
        return new BoolValue(d1 && d2);
    }

    private Value<?> orOp(Value<?> v1, Value<?> v2) {
        Boolean d1 = BoolValue.convert(v1);
        Boolean d2 = BoolValue.convert(v2);
        return new BoolValue(d1 || d2);
    }

    private Value<?> equalOp(Value<?> v1, Value<?> v2) {
        // se forem de instâncias iguais, eu comparo, se não, é erro

        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            return new BoolValue(v1.equals(v2));
        } else if (v1 instanceof TextValue && v2 instanceof TextValue) {
            return new BoolValue(v1.equals(v2));
        } else if (v1 instanceof BoolValue && v2 instanceof BoolValue) {
            return new BoolValue(v1.equals(v2));
        } else if (v1 instanceof FunctionValue && v2 instanceof FunctionValue) {
            return new BoolValue(v1.equals(v2));
        } else if (v1 instanceof ListValue && v2 instanceof ListValue) {
            return new BoolValue(v1.equals(v2));
        } else if (v1 instanceof ObjectValue && v2 instanceof ObjectValue) {
            return new BoolValue(v1.equals(v2));
        } else {
            throw new InterpreterException(super.getLine());
        }
    }

    private Value<?> notEqualOp(Value<?> v1, Value<?> v2) {
        // mesma coisa que o equals, porém retorna o contrário
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else if (v1 instanceof TextValue && v2 instanceof TextValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else if (v1 instanceof BoolValue && v2 instanceof BoolValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else if (v1 instanceof FunctionValue && v2 instanceof FunctionValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else if (v1 instanceof ListValue && v2 instanceof ListValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else if (v1 instanceof ObjectValue && v2 instanceof ObjectValue) {
            return new BoolValue(!(v1.equals(v2)));
        } else {
            throw new InterpreterException(super.getLine());
        }
    }

    private Value<?> lowerThanOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new BoolValue(d1 < d2);
    }

    private Value<?> lowerEqualOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new BoolValue(d1 <= d2);
    }

    private Value<?> greaterThanOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new BoolValue(d1 > d2);
    }

    private Value<?> greaterEqualOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new BoolValue(d1 >= d2);
    }

    private Value<?> addOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof TextValue || v2 instanceof TextValue) {
            String s1 = TextValue.convert(v1);
            String s2 = TextValue.convert(v2);
            return new TextValue(s1 + s2);
        }
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new NumberValue(d1 + d2);
    }

    private Value<?> subOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new NumberValue(d1 - d2);
    }

    private Value<?> mulOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new NumberValue(d1 * d2);
    }

    private Value<?> divOp(Value<?> v1, Value<?> v2) {
        double d1 = NumberValue.convert(v1);
        double d2 = NumberValue.convert(v2);
        return new NumberValue(d1 / d2);
    }

}
