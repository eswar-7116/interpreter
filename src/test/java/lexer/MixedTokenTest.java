package lexer;

import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MixedTokenTest extends BaseLexerTest {

    @Test
    void testAdjacentWithoutSpaces() throws Exception {
        List<Token> tokens = lexLineOf("1+2");
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type());
        assertEquals("1", tokens.get(0).lexeme());
        assertEquals(TokenType.PLUS, tokens.get(1).type());
        assertEquals(TokenType.NUMBER, tokens.get(2).type());
        assertEquals("2", tokens.get(2).lexeme());
    }

    @Test
    void testComplexExpressionNoSpaces() throws Exception {
        List<Token> tokens = lexLineOf("(1+2)*3");
        assertEquals(7, tokens.size());
        assertEquals(TokenType.LPAREN, tokens.get(0).type());
        assertEquals(TokenType.NUMBER, tokens.get(1).type());
        assertEquals(TokenType.PLUS, tokens.get(2).type());
        assertEquals(TokenType.NUMBER, tokens.get(3).type());
        assertEquals(TokenType.RPAREN, tokens.get(4).type());
        assertEquals(TokenType.ASTERISK, tokens.get(5).type());
        assertEquals(TokenType.NUMBER, tokens.get(6).type());
    }

    @Test
    void testLetStatementTokens() throws Exception {
        List<Token> tokens = lexLineOf("let x = 10");
        assertEquals(4, tokens.size());
        assertEquals(TokenType.LET, tokens.get(0).type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("x", tokens.get(1).lexeme());
        assertEquals(TokenType.EQUAL, tokens.get(2).type());
        assertEquals(TokenType.NUMBER, tokens.get(3).type());
        assertEquals("10", tokens.get(3).lexeme());
    }

    @Test
    void testConstStatementTokens() throws Exception {
        List<Token> tokens = lexLineOf("const pi = 3.14");
        assertEquals(4, tokens.size());
        assertEquals(TokenType.CONST, tokens.get(0).type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("pi", tokens.get(1).lexeme());
        assertEquals(TokenType.EQUAL, tokens.get(2).type());
        assertEquals(TokenType.NUMBER, tokens.get(3).type());
        assertEquals("3.14", tokens.get(3).lexeme());
    }

    @Test
    void testDelStatementTokens() throws Exception {
        List<Token> tokens = lexLineOf("del myVar");
        assertEquals(2, tokens.size());
        assertEquals(TokenType.DEL, tokens.get(0).type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("myVar", tokens.get(1).lexeme());
    }

    @Test
    void testPrintExpressionTokens() throws Exception {
        List<Token> tokens = lexLineOf("print 1 + 2 * 3");
        assertEquals(6, tokens.size());
        assertEquals(TokenType.PRINT, tokens.get(0).type());
        assertEquals(TokenType.NUMBER, tokens.get(1).type());
        assertEquals(TokenType.PLUS, tokens.get(2).type());
        assertEquals(TokenType.NUMBER, tokens.get(3).type());
        assertEquals(TokenType.ASTERISK, tokens.get(4).type());
        assertEquals(TokenType.NUMBER, tokens.get(5).type());
    }

    @Test
    void testTabWhitespace() throws Exception {
        List<Token> tokens = lexLineOf("1\t+\t2");
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type());
        assertEquals(TokenType.PLUS, tokens.get(1).type());
        assertEquals(TokenType.NUMBER, tokens.get(2).type());
    }

    @Test
    void testMultipleSpaces() throws Exception {
        List<Token> tokens = lexLineOf("1    +    2");
        assertEquals(3, tokens.size());
    }

    @Test
    void testAssignmentTokens() throws Exception {
        List<Token> tokens = lexLineOf("x = 42");
        assertEquals(3, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).type());
        assertEquals(TokenType.EQUAL, tokens.get(1).type());
        assertEquals(TokenType.NUMBER, tokens.get(2).type());
    }

    @Test
    void testExitWithCodeTokens() throws Exception {
        List<Token> tokens = lexLineOf("exit 1");
        assertEquals(2, tokens.size());
        assertEquals(TokenType.EXIT, tokens.get(0).type());
        assertEquals(TokenType.NUMBER, tokens.get(1).type());
    }

    @Test
    void testExitAloneToken() throws Exception {
        List<Token> tokens = lexLineOf("exit");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EXIT, tokens.getFirst().type());
    }
}
