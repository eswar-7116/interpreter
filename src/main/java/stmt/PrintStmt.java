package stmt;

import expr.Expr;

public record PrintStmt(
        Expr expr
) implements Stmt {
}
