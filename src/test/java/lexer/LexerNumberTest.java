package lexer;

import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerNumberTest extends BaseLexerTest {

    @Test
    void testInteger() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("123");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.getFirst().type);
        assertEquals("123", tokens.getFirst().lexeme);
    }

    @Test
    void testFloat() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("3.14");
        assertEquals("3.14", tokens.getFirst().lexeme);
    }

    @Test
    void testTrailingDot() {
        assertThrows(LexerException.class, () -> lexLineOf("25."));
    }

    @Test
    void testLeadingDot() {
        assertThrows(LexerException.class, () -> lexLineOf(".25"));
    }
}
