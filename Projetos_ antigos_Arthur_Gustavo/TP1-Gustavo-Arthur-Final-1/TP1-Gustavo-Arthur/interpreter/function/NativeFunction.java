package interpreter.function;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import interpreter.expr.Variable;
import interpreter.value.ListValue;
import interpreter.value.NumberValue;
import interpreter.value.TextValue;
import interpreter.value.Value;

public class NativeFunction extends Function {

    public static enum Op {
        Log, // printar
        Read, // ler do teclado
        Random // gerar aleatório
    }

    private Op op;

    public NativeFunction(Variable params, Op op) {
        super(params);
        this.op = op;
    }

    @Override
    public Value<?> call() {
        switch (op) {
            case Log:
                return consoleLog();
            case Read:
                return consoleRead();
            case Random:
            default:
                return consoleRandom();
        }
    }

    private Value<?> consoleLog() {
        Value<?> v = super.getParams().expr();
        // converto para um listvalue, e depois printo
        ListValue lv = (ListValue) v;
        for (Value<?> temp : lv.value())
            System.out.println((temp == null ? "undefined" : temp.toString()) + " ");
        return null;
    }

    // Lê uma linha do teclado
    private Value<?> consoleRead() {
        try {
            // leio a linha e retorno o text
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            System.out.print("> ");
            String line = reader.readLine();
            return new TextValue(line);
        } catch (Exception e) {
            System.err.println("Erro ao pegar string " + e.getMessage());
        }
        return new TextValue("ERRO\n");
    }

    // gera o número aleatório entre 0 e 1;
    private Value<?> consoleRandom() {
        double d = Math.random();
        return new NumberValue(d);
    }

}