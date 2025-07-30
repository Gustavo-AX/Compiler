package semantic;

import java.util.HashMap;
import java.util.Map;
import lexical.Token;
import semantic.expr.Variable;
import semantic.value.Value;

public class Environment {

    private final Environment enclosing;
    private final Map<String, Variable> memory = new HashMap<>();

    public Environment() {
        this(null);
    }

    // Quando eu crio um escopo aninhado a esse, o outro vai receber esse como
    // parâmetro, para quando for olha a TS dele, ele tenha acesso a essa
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    // Se tentar declarar uma variável já declarada, gera erro.
    // Se não tiver sido declarada ainda, coloca no map
    public Variable declare(Token name, Value<?> value) {
        if (memory.containsKey(name.lexeme))
            throw new SemanticException(name.line, "Variável já declarada: " + name.lexeme);

        Variable var = new Variable(name);
        var.setValue(value);
        memory.put(name.lexeme, var);

        return var;
    }

    // Se tentar acessar uma variável não declarada, gera erro
    // Se já tiver sido declarada retorna ela.
    public Variable get(Token name) {
        if (memory.containsKey(name.lexeme))
            return memory.get(name.lexeme);

        if (enclosing != null)
            return enclosing.get(name);

        throw new SemanticException(name.line, "Variável não declarada: " + name.lexeme);
    }

}
