package expr;

import org.jetbrains.annotations.NotNull;

public record LiteralExpr(
        double value
) implements Expr {
    @Override
    @NotNull
    public String toString() {
        return "Literal("+value+")";
    }
}
