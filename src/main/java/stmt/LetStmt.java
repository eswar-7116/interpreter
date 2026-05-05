package stmt;

import expr.Expr;
import token.Token;

public record LetStmt(
        Token name,
        Expr initializer
) implements Stmt {
}