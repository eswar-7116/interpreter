package expr;

import org.jetbrains.annotations.NotNull;
import token.Token;

public record BinaryExpr(
        Expr left,
        Token operator,
        Expr right
) implements Expr {
    @Override
    public @NotNull String toString() {
        return "BinaryExpr(["+left+"]"+operator.lexeme()+"["+right+"]";
    }
}
