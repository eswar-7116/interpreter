package lexer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import token.TokenType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleTokenTest extends BaseLexerTest {
    @ParameterizedTest
    @CsvSource({
            "+, PLUS",
            "-, MINUS",
            "*, ASTERISK",
            "/, FSLASH",
            "%, PERCENT",
            "(, LPAREN",
            "), RPAREN"
    })
    void testSingleOperator(String input, TokenType expected) throws IOException, LexerException {
        assertEquals(expected, lexLineOf(input).getFirst().type);
    }
}
