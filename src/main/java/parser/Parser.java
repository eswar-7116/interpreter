package parser;

import expr.BinaryExpr;
import expr.Expr;
import expr.LiteralExpr;
import token.Token;
import token.TokenType;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int idx;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        idx = 0;
    }

    public Expr parse() {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }

        return expression();
    }

    private boolean isEnd() {
        return idx >= tokens.size();
    }

    public Expr expression() {
        Expr left = term();

        while (idx < tokens.size() && (
                tokens.get(idx).type() == TokenType.PLUS || tokens.get(idx).type() == TokenType.MINUS
        )) {
            Token operator = tokens.get(idx++);
            Expr right = term();

            left = new BinaryExpr(left, operator, right);
        }

        return left;
    }

    public Expr term() {
        Expr left = primary();

        while (idx < tokens.size() && (
                tokens.get(idx).type() == TokenType.ASTERISK || tokens.get(idx).type() == TokenType.FSLASH || tokens.get(idx).type() == TokenType.PERCENT
        )) {
            Token operator = tokens.get(idx++);
            Expr right = primary();

            left = new BinaryExpr(left, operator, right);
        }

        return left;
    }

    public Expr primary() {
        if (isEnd()) {
            throw new ParserException("Unexpected end of expression");
        }

        Token token = tokens.get(idx++);
        return switch (token.type()) {
            case NUMBER -> new LiteralExpr(Double.parseDouble(token.lexeme()));
            case LPAREN -> {
                Expr expr = expression();
                if (isEnd()) {
                    Token lastToken = tokens.get(idx - 1);
                    throw new ParserException(String.format("Unexpected end of line: missing ')' at line %d, column %d", lastToken.line(), lastToken.column()));
                }

                token = tokens.get(idx++);
                if (token.type() != TokenType.RPAREN) {
                    throw new RuntimeException(String.format("Expected ')' at line %d, column %d", token.line(), token.column()));
                }
                yield expr;
            }
            default -> throw new ParserException(String.format("Expected number at line %d, column %d", token.line(), token.column()));
        };
    }
}
