package interpreter.value;

public class BoolValue extends Value<Boolean> {

    private Boolean value;

    public BoolValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return this.value.booleanValue();
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof BoolValue) {
            return this.value.booleanValue() == ((BoolValue) obj).value.booleanValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public static boolean convert(Value<?> v) {
        if (v instanceof BoolValue) {
            return ((BoolValue) v).value();
        } else if (v instanceof NumberValue) {
            return ((NumberValue) v).value() != 0.0;
        } else if (v instanceof TextValue) {
            return !((TextValue) v).value().isEmpty();
        } else if (v instanceof ListValue) {
            return !((ListValue) v).value().isEmpty();
        } else if (v instanceof ObjectValue) {
            return !((ObjectValue) v).value().isEmpty();
        } else if (v instanceof FunctionValue) {
            return true;
        } else {
            return false;
        }
    }
}
