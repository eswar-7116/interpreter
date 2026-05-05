package lexer;

import org.junit.jupiter.api.Test;
import token.Token;
import token.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiLineTest extends BaseLexerTest {

    @Test
    void testTwoLinesTracksLineNumbers() throws Exception {
        try (Lexer lexer = lexerOf("1 + 2\n3 + 4")) {
            List<Token> line1 = lexer.lexLine();
            List<Token> line2 = lexer.lexLine();
            assertEquals(1, line1.getFirst().line());
            assertEquals(2, line2.getFirst().line());
        }
    }

    @Test
    void testThreeLinesWithBlank() throws Exception {
        try (Lexer lexer = lexerOf("1\n\n2")) {
            List<Token> line1 = lexer.lexLine();
            List<Token> blank = lexer.lexLine();
            List<Token> line3 = lexer.lexLine();
            assertEquals(1, line1.size());
            assertTrue(blank.isEmpty());
            assertEquals(1, line3.size());
            assertEquals(3, line3.getFirst().line());
        }
    }

    @Test
    void testEOFAfterLines() throws Exception {
        try (Lexer lexer = lexerOf("1\n2")) {
            lexer.lexLine();
            lexer.lexLine();
            assertNull(lexer.lexLine());
        }
    }

    @Test
    void testMultipleBlankLines() throws Exception {
        try (Lexer lexer = lexerOf("\n\n\n+")) {
            lexer.lexLine(); // blank line 1
            lexer.lexLine(); // blank line 2
            lexer.lexLine(); // blank line 3
            List<Token> line4 = lexer.lexLine();
            assertEquals(4, line4.getFirst().line());
        }
    }

    @Test
    void testDifferentStatementsPerLine() throws Exception {
        try (Lexer lexer = lexerOf("let x = 5\nprint x")) {
            List<Token> line1 = lexer.lexLine();
            List<Token> line2 = lexer.lexLine();
            assertEquals(4, line1.size()); // let, x, =, 5
            assertEquals(TokenType.LET, line1.get(0).type());
            assertEquals(2, line2.size()); // print, x
            assertEquals(TokenType.PRINT, line2.get(0).type());
        }
    }
}
