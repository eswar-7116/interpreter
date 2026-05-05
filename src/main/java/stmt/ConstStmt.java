package stmt;

import expr.Expr;
import token.Token;

public record ConstStmt(
        Token name,
        Expr initializer
) implements Stmt {
}
