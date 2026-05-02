package token;

public class Token {
    public TokenType type;
    public String lexeme;
    public int line;
    public int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("Token(type=%s, lexeme='%s', line=%d, col=%d)", type, lexeme, line, column);
    }
}
