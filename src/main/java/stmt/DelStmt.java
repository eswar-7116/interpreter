package stmt;

import token.Token;

public record DelStmt(
        Token name
) implements Stmt {
}