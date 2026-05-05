package interpreter;

public class Variable {
    double value;
    boolean isConst;

    Variable(double value, boolean isConst) {
        this.value = value;
        this.isConst = isConst;
    }
}
