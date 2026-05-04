package stmt;

import expr.Expr;

public record ExprStmt(
        Expr expr
) implements Stmt {
}
