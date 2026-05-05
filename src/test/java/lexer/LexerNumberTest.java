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
        assertEquals(TokenType.NUMBER, tokens.getFirst().type());
        assertEquals("123", tokens.getFirst().lexeme());
    }

    @Test
    void testFloat() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("3.14");
        assertEquals("3.14", tokens.getFirst().lexeme());
    }

    @Test
    void testTrailingDot() {
        assertThrows(LexerException.class, () -> lexLineOf("25."));
    }

    @Test
    void testLeadingDot() {
        assertThrows(LexerException.class, () -> lexLineOf(".25"));
    }

    @Test
    void testZero() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("0");
        assertEquals(1, tokens.size());
        assertEquals("0", tokens.getFirst().lexeme());
    }

    @Test
    void testSingleDigit() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("7");
        assertEquals(TokenType.NUMBER, tokens.getFirst().type());
        assertEquals("7", tokens.getFirst().lexeme());
    }

    @Test
    void testLargeNumber() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("123456789");
        assertEquals("123456789", tokens.getFirst().lexeme());
    }

    @Test
    void testFloatWithMultipleDecimalDigits() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("1.23456");
        assertEquals("1.23456", tokens.getFirst().lexeme());
    }

    @Test
    void testFloatStartingWithZero() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("0.5");
        assertEquals("0.5", tokens.getFirst().lexeme());
    }

    @Test
    void testNumberAdjacentToOperator() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("42+");
        assertEquals(2, tokens.size());
        assertEquals("42", tokens.get(0).lexeme());
        assertEquals(TokenType.PLUS, tokens.get(1).type());
    }
}

