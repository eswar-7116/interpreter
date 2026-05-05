package parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserErrorTest extends BaseParserTest {

    @Test
    void testMissingClosingParen() {
        assertThrows(ParserException.class, () -> parse("(1 + 2"));
    }

    @Test
    void testUnexpectedEndOfExpression() {
        assertThrows(ParserException.class, () -> parse("1 +"));
    }

    @Test
    void testInvalidAssignmentTarget() {
        // 1 = 5 → parser should reject since 1 is not a variable
        assertThrows(ParserException.class, () -> parse("1 = 5"));
    }

    @Test
    void testLetMissingVariableName() {
        assertThrows(ParserException.class, () -> parse("let = 5"));
    }

    @Test
    void testLetMissingEquals() {
        assertThrows(ParserException.class, () -> parse("let x 5"));
    }

    @Test
    void testLetMissingValue() {
        assertThrows(ParserException.class, () -> parse("let x ="));
    }

    @Test
    void testConstMissingVariableName() {
        assertThrows(ParserException.class, () -> parse("const = 5"));
    }

    @Test
    void testConstMissingEquals() {
        assertThrows(ParserException.class, () -> parse("const x 5"));
    }

    @Test
    void testDelMissingName() {
        assertThrows(ParserException.class, () -> parse("del 5"));
    }

    @Test
    void testUnexpectedToken() {
        assertThrows(ParserException.class, () -> parse("print"));
    }

    @Test
    void testWrongClosingToken() {
        // (1 + 2 * 3 → missing closing paren
        assertThrows(ParserException.class, () -> parse("(1 + 2 * 3"));
    }

    @Test
    void testExtraClosingParen() {
        // ")" alone should fail as unexpected token
        assertThrows(ParserException.class, () -> parse(")"));
    }

    @Test
    void testConstMissingValue() {
        assertThrows(ParserException.class, () -> parse("const x ="));
    }

    @Test
    void testExpressionAssignmentTarget() {
        // (1 + 2) = 5 → BinaryExpr is not a valid assignment target
        assertThrows(ParserException.class, () -> parse("(1 + 2) = 5"));
    }

    @Test
    void testWrongTokenInsideParen() {
        // (1 + 2 = 3) → = inside paren where ) is expected
        assertThrows(ParserException.class, () -> parse("(1 + 2 = 3)"));
    }

    @Test
    void testErrorMessageContainsPosition() {
        ParserException ex = assertThrows(ParserException.class, () -> parse("let = 5"));
        assertTrue(ex.getMessage().contains("line"));
        assertTrue(ex.getMessage().contains("column"));
    }

    @Test
    void testMissingParenErrorContainsPosition() {
        ParserException ex = assertThrows(ParserException.class, () -> parse("(1 + 2"));
        assertTrue(ex.getMessage().contains("line"));
    }

    @Test
    void testDelAlone() {
        // del with nothing after it → consume expects IDENTIFIER
        assertThrows(ParserException.class, () -> parse("del"));
    }
}
