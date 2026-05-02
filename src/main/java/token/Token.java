package token;

public class Token {
    public TokenType type;
    public String lexeme;
    public int line;
    public int col;

    public Token(TokenType type, String lexeme, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.col = col;
    }

    @Override
    public String toString() {
        return String.format("Token(type=%s, lexeme='%s', line=%d, col=%d)", type, lexeme, line, col);
    }
}
