package semantic;

public class Semantic {

    public final static Environment globals;

    //Cria um escopo global, que no caso é o escopo inicial.
    static {
        globals = new Environment();
    }
}