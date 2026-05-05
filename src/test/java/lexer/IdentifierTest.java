package lexer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import token.Token;
import token.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IdentifierTest extends BaseLexerTest {

    @Test
    void testSimpleIdentifier() throws Exception {
        List<Token> tokens = lexLineOf("x");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("x", tokens.getFirst().lexeme());
    }

    @Test
    void testMultiCharIdentifier() throws Exception {
        List<Token> tokens = lexLineOf("foobar");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("foobar", tokens.getFirst().lexeme());
    }

    @Test
    void testIdentifierWithDigits() throws Exception {
        List<Token> tokens = lexLineOf("x1");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("x1", tokens.getFirst().lexeme());
    }

    @Test
    void testIdentifierWithUnderscore() throws Exception {
        List<Token> tokens = lexLineOf("my_var");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("my_var", tokens.getFirst().lexeme());
    }

    @Test
    void testIdentifierStartingWithUnderscore() throws Exception {
        List<Token> tokens = lexLineOf("_private");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("_private", tokens.getFirst().lexeme());
    }

    @Test
    void testUnderscoreOnly() throws Exception {
        List<Token> tokens = lexLineOf("_");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals("_", tokens.getFirst().lexeme());
    }

    @ParameterizedTest
    @CsvSource({
            "printer, printer",
            "exits, exits",
            "letters, letters",
            "letting, letting",
            "constants, constants",
            "deleting, deleting"
    })
    void testKeywordPrefixIsIdentifier(String input, String expectedLexeme) throws Exception {
        List<Token> tokens = lexLineOf(input);
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.getFirst().type());
        assertEquals(expectedLexeme, tokens.getFirst().lexeme());
    }
}
