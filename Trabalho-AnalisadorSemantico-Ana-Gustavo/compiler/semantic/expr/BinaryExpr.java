package semantic.expr;

import semantic.SemanticException;
import semantic.value.BoolValue;
import semantic.value.CharValue;
import semantic.value.DoubleValue;
import semantic.value.IntegerValue;
import semantic.value.Value;

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
                res = andorOp(v1, v2);
                break;
            case Or:
                res = andorOp(v1, v2);
                break;
            case Equal:
                res = commumCompareOp(v1, v2);
                break;
            case NotEqual:
                res = commumCompareOp(v1, v2);
                break;
            case LowerThan:
                res = commumCompareOp(v1, v2);
                break;
            case LowerEqual:
                res = commumCompareOp(v1, v2);
                break;
            case GreaterThan:
                res = commumCompareOp(v1, v2);
                break;
            case GreaterEqual:
                res = commumCompareOp(v1, v2);
                break;
            case Add:
                res = commumAritimecOp(v1, v2);
                break;
            case Sub:
                res = commumAritimecOp(v1, v2);
                break;
            case Mul:
                res = commumAritimecOp(v1, v2);
                break;
            case Div:
            default:
                res = divOp(v1, v2);
                break;
        }

        return res;
    }

 
    // && e ||
    private Value<?> andorOp(Value<?> v1, Value<?> v2) {
        // Só pode se ambos os tipos são booleanos
        // Bool com Bool -> Bool
        if((v1 instanceof BoolValue) &&  (v2 instanceof BoolValue)){
            return new BoolValue(null);
        } 
        // Se não é erro
        else{
            throw new SemanticException(super.getLine(),"Tipo não permitido para comparação de && ou ||");
        }
    }

    // == != > >= < <= 
    private Value<?> commumCompareOp(Value<?> v1, Value<?> v2) {
        // Pode ser comparação:
        // Float com Float -> Bool
        // Float com Int -> Bool
        // Int com Int -> Bool
        // Char com Int -> Bool
        // Char com Char -> Bool
        // Bool com Bool -> Bool

        // Float com Float -> Float
        if((v1 instanceof DoubleValue) &&  (v2 instanceof DoubleValue)){
            return new BoolValue(null);
        } 
        // Float com Inteiro -> Float
        else if(((v1 instanceof DoubleValue) &&  (v2 instanceof IntegerValue))){
            return new BoolValue(null);
        } else if(((v1 instanceof IntegerValue) &&  (v2 instanceof DoubleValue))){
            return new BoolValue(null);
        } 
        // Inteiro com Inteiro -> Inteiro
        else if((v1 instanceof IntegerValue && v2 instanceof IntegerValue)){
            return new BoolValue(null);
        } 
        // Char com Inteiro -> Inteiro
        else if(v1 instanceof IntegerValue && v2 instanceof CharValue){
            return new BoolValue(null);
        } else if(v1 instanceof CharValue && v2 instanceof IntegerValue){
            return new BoolValue(null);
        } 
        //Char com Char
        else if(v1 instanceof CharValue && v2 instanceof CharValue){
            return new BoolValue(null);
        }
        //Bool com Bool
        else if(v1 instanceof BoolValue && v2 instanceof BoolValue){
            return new BoolValue(null);
        }
        // Se não é erro
        else{
            throw new SemanticException(super.getLine(),"Operandos não permitido para comparação de ==, !=, >, >=, < ou <=" + v1.getClass());
        }
    }

    // + - *
    private Value<?> commumAritimecOp(Value<?> v1, Value<?> v2) {
        // Permitido para os tipos:
        // Float com Float -> Float
        // Float com Int -> Float
        // Int com Int -> Int
        // Char com Int -> Int
        // Char com Char -> Int

        // Float com Float -> Float
        if((v1 instanceof DoubleValue) &&  (v2 instanceof DoubleValue)){
            return new DoubleValue(null);
        } 
        // Float com Inteiro -> Float
        else if(((v1 instanceof DoubleValue) &&  (v2 instanceof IntegerValue))){
            return new DoubleValue(null);
        } else if(((v1 instanceof IntegerValue) &&  (v2 instanceof DoubleValue))){
            return new DoubleValue(null);
        } 
        // Inteiro com Inteiro -> Inteiro
        else if((v1 instanceof IntegerValue && v2 instanceof IntegerValue)){
            return new IntegerValue(null);
        } 
        // Char com Inteiro -> Inteiro
        else if(v1 instanceof IntegerValue && v2 instanceof CharValue){
            return new IntegerValue(null);
        } else if(v1 instanceof CharValue && v2 instanceof IntegerValue){
            return new IntegerValue(null);
        }
        // Char com Char -> Inteiro
        else if((v1 instanceof CharValue && v2 instanceof CharValue)){
            return new IntegerValue(null);
        } 
        // Se não é erro
        else{
            throw new SemanticException(super.getLine(),"Operandos não permitido para comparação de + - ou *");
        }
    }

    private Value<?> divOp(Value<?> v1, Value<?> v2) {
        // Permitido para os tipos:
        // Float com Float -> Float
        // Float com Int -> Float
        // Int com Int -> Float
        // Char com Int -> Float
        // Char com Char -> Float
        // Float com Float -> Float

        if((v1 instanceof DoubleValue) &&  (v2 instanceof DoubleValue)){
            return new DoubleValue(null);
        } 
        // Float com Inteiro -> Float
        else if(((v1 instanceof DoubleValue) &&  (v2 instanceof IntegerValue))){
            return new DoubleValue(null);
        } else if(((v1 instanceof IntegerValue) &&  (v2 instanceof DoubleValue))){
            return new DoubleValue(null);
        } 
        // Inteiro com Inteiro -> Float
        else if((v1 instanceof IntegerValue && v2 instanceof IntegerValue)){
            return new DoubleValue(null);
        } 
        // Char com Inteiro -> Float
        else if(v1 instanceof IntegerValue && v2 instanceof CharValue){
            return new DoubleValue(null);
        } else if(v1 instanceof CharValue && v2 instanceof IntegerValue){
            return new DoubleValue(null);
        } 
        // Char com Char -> Float
        else if((v1 instanceof CharValue && v2 instanceof CharValue)){
            return new DoubleValue(null);
        } 
        // Se não é erro
        else{
            throw new SemanticException(super.getLine(),"Operandos não permitido para comparação de /");
        }
    }
}
