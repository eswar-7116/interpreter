package parser;

import expr.*;
import stmt.ExitStmt;
import stmt.ExprStmt;
import stmt.PrintStmt;
import stmt.Stmt;
import token.Token;
import token.TokenType;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int idx;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.idx = 0;
    }

    public Stmt parse() {
        if (tokens == null || tokens.isEmpty()) return null;
        return statement();
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStmt(expression());
        }

        if (match(TokenType.EXIT)) {
            Expr code = isEnd() ? null : expression();
            return new ExitStmt(code);
        }

        return new ExprStmt(expression());
    }

    public Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = addition();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof VariableExpr(Token name)) {
                return new AssignExpr(name, value);
            }

            throw new ParserException("Invalid assignment target at line "
                    + equals.line() + ", column " + equals.column());
        }

        return expr;
    }

    private Expr addition() {
        Expr left = term();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = term();

            left = new BinaryExpr(left, operator, right);
        }

        return left;
    }

    public Expr term() {
        Expr left = unary();

        while (match(TokenType.ASTERISK, TokenType.FSLASH, TokenType.PERCENT)) {
            Token operator = previous();
            Expr right = unary();

            left = new BinaryExpr(left, operator, right);
        }

        return left;
    }

    public Expr unary() {
        if (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = unary();

            return new BinaryExpr(new LiteralExpr(0), operator, right);
        }

        return primary();
    }

    public Expr primary() {
        if (isEnd()) {
            throw new ParserException("Unexpected end of expression");
        }

        Token token = tokens.get(idx++);

        return switch (token.type()) {
            case NUMBER -> new LiteralExpr(Double.parseDouble(token.lexeme()));
            case IDENTIFIER -> new VariableExpr(token);
            case LPAREN -> {
                Expr expr = expression();

                if (isEnd()) {
                    throw new ParserException("Missing ')' at line " + token.line() + ", column " + token.column());
                }

                Token closing = tokens.get(idx++);
                if (closing.type() != TokenType.RPAREN) {
                    throw new ParserException("Expected ')' at line " + closing.line() + ", column " + closing.column());
                }
                yield expr;
            }
            default -> throw new ParserException("Unexpected token '" + token.lexeme() + "' at line " + token.line() + ", column " + token.column());
        };
    }

    private boolean isEnd() {
        return idx >= tokens.size();
    }

    private Token peek() {
        return tokens.get(idx);
    }

    private Token previous() {
        return tokens.get(idx - 1);
    }

    private boolean check(TokenType type) {
        if (isEnd()) return false;
        return peek().type() == type;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                idx++;
                return true;
            }
        }
        return false;
    }
}
