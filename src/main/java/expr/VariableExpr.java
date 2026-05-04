package expr;

import org.jetbrains.annotations.NotNull;
import token.Token;

public record VariableExpr(
        Token name
) implements Expr {
    @Override
    @NotNull
    public String toString() {
        return "Variable('"+name+"')";
    }
}