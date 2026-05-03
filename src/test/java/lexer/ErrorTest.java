package lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
