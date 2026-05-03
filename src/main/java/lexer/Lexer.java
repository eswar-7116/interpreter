package lexer;

import token.Token;
import token.TokenType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Lexer implements AutoCloseable {
    private final BufferedReader reader;
    private int lineNumber;

    public Lexer(Reader reader) {
        this.reader = new BufferedReader(reader);
        lineNumber = 1;
    }

    public List<Token> lexLine() throws IOException, LexerException {
        String line = reader.readLine();

        if (line == null) return null;
        if (line.isBlank()) {
            ++lineNumber;
            return List.of();
        }

        List<Token> tokens = new ArrayList<>();
        loop:
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            switch (c) {
                case '+' -> tokens.add(new Token(TokenType.PLUS, "+", lineNumber, i + 1));
                case '-' -> tokens.add(new Token(TokenType.MINUS, "-", lineNumber, i + 1));
                case '*' -> tokens.add(new Token(TokenType.ASTERISK, "*", lineNumber, i + 1));
                case '/' -> tokens.add(new Token(TokenType.FSLASH, "/", lineNumber, i + 1));
                case '%' -> tokens.add(new Token(TokenType.PERCENT, "%", lineNumber, i + 1));
                case '(' -> tokens.add(new Token(TokenType.LPAREN, "(", lineNumber, i + 1));
                case ')' -> tokens.add(new Token(TokenType.RPAREN, ")", lineNumber, i + 1));
                case '#' -> {
                    break loop;
                }
                default -> {
                    if (Character.isDigit(c)) {
                        int start = i;
                        StringBuilder sb = new StringBuilder();
                        while (i < line.length() && Character.isDigit(line.charAt(i))) {
                            sb.append(line.charAt(i));
                            ++i;
                        }
                        if (i < line.length() && line.charAt(i) == '.') {
                            sb.append('.');
                            ++i;
                            int fracStart = i;
                            while (i < line.length() && Character.isDigit(line.charAt(i))) {
                                sb.append(line.charAt(i));
                                ++i;
                            }
                            if (i == fracStart) {
                                throw new LexerException("Unsupported character", lineNumber, fracStart);
                            }
                        }
                        --i;

                        tokens.add(new Token(TokenType.NUMBER, sb.toString(), lineNumber, start + 1));
                    } else if (!Character.isWhitespace(c)) {
                        throw new LexerException(String.format("Unsupported character '%c'", c), lineNumber, i + 1);
                    }
                }
            }
        }

        ++lineNumber;
        return tokens;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
