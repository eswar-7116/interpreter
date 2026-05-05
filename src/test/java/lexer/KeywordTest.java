package lexer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import token.Token;
import token.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KeywordTest extends BaseLexerTest {

    @ParameterizedTest
    @CsvSource({
            "print, PRINT",
            "exit, EXIT",
            "let, LET",
            "const, CONST",
            "del, DEL"
    })
    void testKeywordRecognition(String input, TokenType expected) throws Exception {
        List<Token> tokens = lexLineOf(input);
        assertEquals(1, tokens.size());
        assertEquals(expected, tokens.getFirst().type());
        assertEquals(input, tokens.getFirst().lexeme());
    }
}
