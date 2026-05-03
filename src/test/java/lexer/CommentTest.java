package lexer;

import org.junit.jupiter.api.Test;
import token.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentTest extends BaseLexerTest {
    @Test
    void testFullLineComment() throws Exception {
        List<Token> tokens = lexLineOf("# this is a comment");
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testInlineComment() throws Exception {
        List<Token> tokens = lexLineOf("1 + 2 # add");
        assertEquals(3, tokens.size());
    }
}
