package interpreter.value;

import java.util.Map;

public class ObjectValue extends Value<Map<TextValue, Value<?>>> {

    private Map<TextValue, Value<?>> value;

    public ObjectValue(Map<TextValue, Value<?>> value) {
        this.value = value;
    }

    @Override
    public Map<TextValue, Value<?>> value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return !value.isEmpty();
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ObjectValue) {
            return this.value.equals(((ObjectValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        for (Map.Entry<TextValue, Value<?>> e : this.value.entrySet()) {
            TextValue k = e.getKey();
            Value<?> v = e.getValue();

            sb.append(k.toString());
            sb.append(":");
            sb.append(v == null ? "undefined" : v.toString());
            sb.append(", ");
        }

        if (sb.length() > 1)
            sb.setLength(sb.length() - 2);

        sb.append("}");
        return sb.toString();
    }

}
