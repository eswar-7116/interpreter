package stmt;

import expr.Expr;

public record ExitStmt(
        Expr code
) implements Stmt {
}
