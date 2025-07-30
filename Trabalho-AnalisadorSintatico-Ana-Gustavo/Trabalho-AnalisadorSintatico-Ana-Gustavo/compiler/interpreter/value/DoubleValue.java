package interpreter.value;

public class DoubleValue extends Value<Double> {

    private Double value;

    public DoubleValue(Double value) {
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
        } else if (obj instanceof DoubleValue) {
            return this.value.doubleValue() == ((DoubleValue) obj).value.doubleValue();
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
}
