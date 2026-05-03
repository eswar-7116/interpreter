package lexer;

import org.junit.jupiter.api.Test;
import token.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineNumberTest extends BaseLexerTest {
    @Test
    void testLineNumberAfterBlank() throws Exception {
        try (Lexer lexer = lexerOf("\n+")) {
            lexer.lexLine(); // blank line
            List<Token> tokens = lexer.lexLine();
            assertEquals(2, tokens.getFirst().line());
        }
    }
}
