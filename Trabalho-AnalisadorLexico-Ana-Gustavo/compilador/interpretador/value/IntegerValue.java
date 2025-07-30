package compilador.interpretador.value;

public class IntegerValue extends Value<Integer> {

    private Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return this.value != 0;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof IntegerValue) {
            return this.value.intValue() == ((IntegerValue) obj).value.intValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
