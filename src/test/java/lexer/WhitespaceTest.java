package lexer;

import org.junit.jupiter.api.Test;
import token.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WhitespaceTest extends BaseLexerTest {
    @Test
    void testBlankLine() throws Exception {
        List<Token> tokens = lexLineOf("   ");
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testEOF() throws Exception {
        assertNull(lexLineOf(""));
    }
}
