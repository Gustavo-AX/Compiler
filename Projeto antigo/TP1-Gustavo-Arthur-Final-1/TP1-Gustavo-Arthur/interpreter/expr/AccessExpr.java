package interpreter.expr;

import java.util.List;
import java.util.Map;
import interpreter.InterpreterException;
import interpreter.value.*;

public class AccessExpr extends SetExpr {

    private SetExpr base;
    private Expr index;

    public AccessExpr(int line, SetExpr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr() {
        // se for um list, converto a expressão para um numero, que vai ser o index,
        // se o valor existir, ou seja, está na lista, retorno ele, se não, retorno null
        // que posteriormente sera convertido em undefined
        if (base.expr() instanceof ListValue) {
            int i = (int) NumberValue.convert(index.expr());
            ListValue lv = (ListValue) base.expr();
            List<Value<?>> value = lv.value();

            if (i < value.size())
                return value.get(i);
            else {
                return null;
            }
            // se for um Object, pego a chave, que no caso é um text value,
            // se existir a chave, retorno ela, se não, retorno null
            // que posteriormente sera convertido em undefined
        } else if (base.expr() instanceof ObjectValue) {
            TextValue tv = new TextValue(TextValue.convert(index.expr()));
            ObjectValue ov = (ObjectValue) base.expr();
            Map<TextValue, Value<?>> value = ov.value();
            if (value.containsKey(tv))
                return value.get(tv);
            else
                return null;
        } else {
            throw new InterpreterException(super.getLine());
        }
    }

    @Override
    public void setValue(Value<?> value) {
        // se for um list, converto o index, se o index estiver no limite da lista, eu
        // mudo seu valor, se o index estiver fora do limite da lista,
        // adciono null até index, em index, adiciono o valor
        if (base.expr() instanceof ListValue) {
            int i = (int) NumberValue.convert(index.expr());
            ListValue lv = (ListValue) base.expr();
            List<Value<?>> var = lv.value();
            if (i < var.size()) {
                var.set(i, value);
                ListValue temp = new ListValue(var);
                base.setValue(temp);
            } else {
                for (int w = var.size(); w < i; w++) {
                    var.add(w, null);
                }
                var.add(i, value);
            }

            // se for um object, pego a chave a ser colocada, se var conter a chave eu
            // retorno o objeto, se não conter, adiciono a chave e o valor
        } else if (base.expr() instanceof ObjectValue) {
            TextValue tv = new TextValue(TextValue.convert(index.expr()));
            ObjectValue ov = (ObjectValue) base.expr();
            Map<TextValue, Value<?>> var = ov.value();
            if (var.containsValue(tv))
                var.replace(tv, value);
            else
                var.put(tv, value);
        } else {
            throw new InterpreterException(super.getLine());
        }
    }

}