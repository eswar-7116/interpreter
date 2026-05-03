package lexer;

import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerExpressionTest extends BaseLexerTest {

    @Test
    void testExpression() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("1 + 2 * 3");
        assertEquals(5, tokens.size());
        assertEquals(TokenType.PLUS, tokens.get(1).type());
        assertEquals(TokenType.ASTERISK, tokens.get(3).type());
    }

    @Test
    void testParens() throws IOException, LexerException {
        List<Token> tokens = lexLineOf("(1 + 2)");
        assertEquals(TokenType.LPAREN, tokens.get(0).type());
        assertEquals(TokenType.RPAREN, tokens.get(4).type());
    }
}
