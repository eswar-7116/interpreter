package expr;

import org.jetbrains.annotations.NotNull;
import token.Token;

public record AssignExpr(
        Token name,
        Expr value
) implements Expr {
    @Override
    public @NotNull String toString() {
        return "Assign('"+name+"', '"+value+"')";
    }
}
