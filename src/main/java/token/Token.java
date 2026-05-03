package token;

import org.jetbrains.annotations.NotNull;

public record Token(
        TokenType type,
        String lexeme,
        int line,
        int column
) {
    @Override
    @NotNull
    public String toString() {
        return String.format(
                "Token(type=%s, lexeme='%s', line=%d, col=%d)",
                type,
                lexeme,
                line,
                column
        );
    }
}
