package lexer;

import token.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public abstract class BaseLexerTest {

    protected Lexer lexerOf(String input) {
        return new Lexer(new StringReader(input));
    }

    protected List<Token> lexLineOf(String input) throws IOException, LexerException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            return lexer.lexLine();
        }
    }
}
