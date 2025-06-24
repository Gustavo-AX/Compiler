package interpreter;

import java.util.HashMap;
import java.util.Map;

import interpreter.command.Command;
import interpreter.expr.Expr;
import interpreter.value.FunctionValue;
import interpreter.value.ObjectValue;
import interpreter.value.TextValue;
import interpreter.value.Value;
import lexical.Token;
import interpreter.expr.Variable;
import interpreter.function.NativeFunction;
import lexical.Token.Type;

public class Interpreter {

    public final static Environment globals;

    static {
        globals = new Environment();

        // Tokens do params
        Variable params = globals.declare(new Token("params", Type.NAME, null), false);
        // Tokens do Console
        Variable console = globals.declare(new Token("console", Type.NAME, null), false);

        // Map para guardar o nomes chamadas na função console
        Map<TextValue, Value<?>> map = new HashMap<TextValue, Value<?>>();
        map.put(new TextValue("log"), new FunctionValue(new NativeFunction(params, NativeFunction.Op.Log)));
        map.put(new TextValue("read"), new FunctionValue(new NativeFunction(params, NativeFunction.Op.Read)));
        map.put(new TextValue("random"), new FunctionValue(new NativeFunction(params, NativeFunction.Op.Random)));

        // Guardo o map em um Object
        ObjectValue obj = new ObjectValue(map);
        // e coloco o Object em uma variável chamada console
        console.setValue(obj);
    }

    private Interpreter() {
    }

    public static void interpret(Command cmd) {
        cmd.execute();
    }

    public static void interpret(Expr expr) {
        Value<?> v = expr.expr();
        if (v == null)
            System.out.println("undefined");
        else
            System.out.println(v);
    }

}