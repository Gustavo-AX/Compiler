package interpreter;

import java.util.HashMap;
import java.util.Map;

import interpreter.expr.Variable;
import lexical.Token;

public class Environment {

    private final Environment enclosing;
    private final Map<String, Variable> memory = new HashMap<>();

    public Environment() {
        this(null);
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Variable declare(Token name, boolean constant) {
        if (memory.containsKey(name.lexeme))
            throw new InterpreterException(name.line);

        Variable var = new Variable(name, constant);
        memory.put(name.lexeme, var);

        return var;
    }

    public Variable get(Token name) {
        if (memory.containsKey(name.lexeme))
            return memory.get(name.lexeme);

        if (enclosing != null)
            return enclosing.get(name);

        throw new InterpreterException(name.line);
    }

}
