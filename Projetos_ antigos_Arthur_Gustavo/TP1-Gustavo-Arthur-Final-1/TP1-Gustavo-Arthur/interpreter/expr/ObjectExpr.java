package interpreter.expr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import interpreter.value.ObjectValue;
import interpreter.value.TextValue;
import interpreter.value.Value;

public class ObjectExpr extends Expr {

    private List<ObjectItem> items;

    public ObjectExpr(int line, List<ObjectItem> items) {
        super(line);
        this.items = items;
    }

    @Override
    public Value<?> expr() {
        // cria um map e coloca ele no objectValue
        Map<TextValue, Value<?>> temp = new HashMap<TextValue, Value<?>>();

        for (ObjectItem e : items) {
            TextValue text = new TextValue(e.key);
            temp.put(text, e.value.expr());
        }

        ObjectValue objList = new ObjectValue(temp);
        return objList;
    }
}