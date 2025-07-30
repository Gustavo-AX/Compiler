package semantic.value;

public class CharValue extends Value<Character> {

    private Character value;

    public CharValue(Character value) {
        this.value = value;
    }

    @Override
    public Character value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return this.value != null && this.value != '\0';
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof CharValue) {
            return this.value.equals(((CharValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public static String convert(Value<?> v) {
        if (v == null) {
            return "undefined";
        } else {
            return v.toString();
        }
    }
}
