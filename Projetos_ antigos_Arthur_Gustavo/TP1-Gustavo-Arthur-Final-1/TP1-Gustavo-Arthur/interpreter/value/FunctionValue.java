package interpreter.value;

import interpreter.function.Function;

public class FunctionValue extends Value<Function> {

    private Function value;

    public FunctionValue(Function value) {
        this.value = value;
    }

    @Override
    public Function value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof FunctionValue) {
            return this.value.equals(((FunctionValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
