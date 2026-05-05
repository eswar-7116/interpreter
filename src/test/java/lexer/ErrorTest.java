package lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorTest extends BaseLexerTest {
    @Test
    void testUnsupportedCharacter() {
        assertThrows(LexerException.class, () -> lexLineOf("@"));
    }

    @Test
    void testExceptionCarriesPosition() {
        LexerException ex = assertThrows(LexerException.class, () -> lexLineOf("1 + @"));
        assertEquals(1, ex.getLine());
        assertEquals(5, ex.getColumn());
    }

    @Test
    void testUnsupportedDollarSign() {
        assertThrows(LexerException.class, () -> lexLineOf("$"));
    }

    @Test
    void testUnsupportedAmpersand() {
        assertThrows(LexerException.class, () -> lexLineOf("&"));
    }

    @Test
    void testUnsupportedExclamation() {
        assertThrows(LexerException.class, () -> lexLineOf("!"));
    }

    @Test
    void testErrorMessageContainsLineAndColumn() {
        LexerException ex = assertThrows(LexerException.class, () -> lexLineOf("@"));
        assertTrue(ex.getMessage().contains("line"));
        assertTrue(ex.getMessage().contains("column"));
    }

    @Test
    void testTrailingDotFollowedByOperator() {
        assertThrows(LexerException.class, () -> lexLineOf("25.+"));
    }

    @Test
    void testErrorOnSecondLine() throws Exception {
        try (Lexer lexer = lexerOf("1 + 2\n@")) {
            lexer.lexLine();
            LexerException ex = assertThrows(LexerException.class, lexer::lexLine);
            assertEquals(2, ex.getLine());
        }
    }

    @Test
    void testUnsupportedCharMidExpression() {
        LexerException ex = assertThrows(LexerException.class, () -> lexLineOf("1 + 2 @ 3"));
        assertEquals(7, ex.getColumn());
    }
}
