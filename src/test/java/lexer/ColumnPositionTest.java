package lexer;

import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ColumnPositionTest extends BaseLexerTest {

    @Test
    void testSingleTokenColumn() throws Exception {
        List<Token> tokens = lexLineOf("+");
        assertEquals(1, tokens.getFirst().column());
    }

    @Test
    void testMultipleTokenColumns() throws Exception {
        // "1 + 2"  → cols: 1, 3, 5
        List<Token> tokens = lexLineOf("1 + 2");
        assertEquals(1, tokens.get(0).column());
        assertEquals(3, tokens.get(1).column());
        assertEquals(5, tokens.get(2).column());
    }

    @Test
    void testIdentifierColumn() throws Exception {
        // "  foo"  → col 3
        List<Token> tokens = lexLineOf("  foo");
        assertEquals(3, tokens.getFirst().column());
    }

    @Test
    void testMultiDigitNumberColumn() throws Exception {
        // "  123 + 4" → NUMBER at col 3, PLUS at col 7, NUMBER at col 9
        List<Token> tokens = lexLineOf("  123 + 4");
        assertEquals(3, tokens.get(0).column());
        assertEquals(7, tokens.get(1).column());
        assertEquals(9, tokens.get(2).column());
    }

    @Test
    void testKeywordColumn() throws Exception {
        // "print x" → PRINT at col 1, IDENTIFIER at col 7
        List<Token> tokens = lexLineOf("print x");
        assertEquals(1, tokens.get(0).column());
        assertEquals(7, tokens.get(1).column());
    }

    @Test
    void testAdjacentTokensColumns() throws Exception {
        // "1+2" → NUMBER at col 1, PLUS at col 2, NUMBER at col 3
        List<Token> tokens = lexLineOf("1+2");
        assertEquals(3, tokens.size());
        assertEquals(1, tokens.get(0).column());
        assertEquals(2, tokens.get(1).column());
        assertEquals(3, tokens.get(2).column());
    }

    @Test
    void testFloatNumberColumn() throws Exception {
        // "3.14 + 1" → NUMBER at col 1, PLUS at col 6, NUMBER at col 8
        List<Token> tokens = lexLineOf("3.14 + 1");
        assertEquals(1, tokens.get(0).column());
        assertEquals(6, tokens.get(1).column());
    }

    @Test
    void testParenColumns() throws Exception {
        // "(1)" → LPAREN at col 1, NUMBER at col 2, RPAREN at col 3
        List<Token> tokens = lexLineOf("(1)");
        assertEquals(1, tokens.get(0).column());
        assertEquals(2, tokens.get(1).column());
        assertEquals(3, tokens.get(2).column());
    }
}
