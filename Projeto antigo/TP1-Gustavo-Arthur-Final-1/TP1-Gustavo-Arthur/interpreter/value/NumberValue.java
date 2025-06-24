package interpreter.value;

public class NumberValue extends Value<Double> {

    private Double value;

    public NumberValue(Double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return !this.value.isNaN() && this.value != 0.0;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof NumberValue) {
            return this.value.doubleValue() == ((NumberValue) obj).value.doubleValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        long tmp = this.value.longValue();
        return this.value.doubleValue() == ((double) tmp) ?
            Long.toString(tmp) : this.value.toString();
    }

    public static double convert(Value<?> v) {
        if (v instanceof BoolValue) {
            return ((BoolValue) v).value() ? 1.0 : 0.0;
        } else if (v instanceof NumberValue) {
            return ((NumberValue) v).value();
        } else if (v instanceof TextValue) {
            try {
                return Double.parseDouble(((TextValue) v).value());
            } catch (Exception e) {
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

}
