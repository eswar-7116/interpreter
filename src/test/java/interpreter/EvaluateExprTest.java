package interpreter;

import expr.BinaryExpr;
import expr.LiteralExpr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import token.Token;
import token.TokenType;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluateExprTest extends BaseInterpreterTest {

    @Test
    void testLiteral() {
        Interpreter interp = newInterpreter();
        assertEquals(42.0, interp.evaluate(new LiteralExpr(42)));
    }

    @Test
    void testNullExpr() {
        Interpreter interp = newInterpreter();
        assertEquals(-1, interp.evaluate(null));
    }

    @ParameterizedTest
    @CsvSource({
            "PLUS,  3.0, 2.0, 5.0",
            "MINUS, 3.0, 2.0, 1.0",
            "ASTERISK, 3.0, 2.0, 6.0",
            "FSLASH, 6.0, 2.0, 3.0",
            "PERCENT, 7.0, 3.0, 1.0"
    })
    void testBinaryOperations(TokenType op, double left, double right, double expected) {
        Interpreter interp = newInterpreter();
        Token opToken = new Token(op, op.name(), 1, 1);
        BinaryExpr expr = new BinaryExpr(new LiteralExpr(left), opToken, new LiteralExpr(right));
        assertEquals(expected, interp.evaluate(expr));
    }

    @Test
    void testAddition() throws Exception {
        Interpreter interp = executeAndReturn("let x = 1 + 2");
        assertEquals(3.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testSubtraction() throws Exception {
        Interpreter interp = executeAndReturn("let x = 10 - 3");
        assertEquals(7.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testMultiplication() throws Exception {
        Interpreter interp = executeAndReturn("let x = 4 * 5");
        assertEquals(20.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testDivision() throws Exception {
        Interpreter interp = executeAndReturn("let x = 10 / 4");
        assertEquals(2.5, interp.symbolTable.get("x").value);
    }

    @Test
    void testModulo() throws Exception {
        Interpreter interp = executeAndReturn("let x = 10 % 3");
        assertEquals(1.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testDivisionByZeroProducesInfinity() throws Exception {
        Interpreter interp = executeAndReturn("let x = 1 / 0");
        assertTrue(Double.isInfinite(interp.symbolTable.get("x").value));
    }

    @Test
    void testPrecedence() throws Exception {
        Interpreter interp = executeAndReturn("let x = 1 + 2 * 3");
        assertEquals(7.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testParentheses() throws Exception {
        Interpreter interp = executeAndReturn("let x = (1 + 2) * 3");
        assertEquals(9.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testNestedParentheses() throws Exception {
        Interpreter interp = executeAndReturn("let x = ((2 + 3) * (4 - 1))");
        assertEquals(15.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testUnaryMinus() throws Exception {
        Interpreter interp = executeAndReturn("let x = -5");
        assertEquals(-5.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testComplexExpression() throws Exception {
        Interpreter interp = executeAndReturn("let x = 2 + 3 * 4 - 6 / 2");
        // 2 + 12 - 3 = 11
        assertEquals(11.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testExprStmtEvaluates() throws Exception {
        // An expression statement should evaluate without errors
        // (e.g. a bare expression like "1 + 2")
        Interpreter interp = newInterpreter();
        interp.execute(parse("1 + 2")); // should not throw
    }

    @Test
    void testModuloByZeroProducesNaN() throws Exception {
        Interpreter interp = executeAndReturn("let x = 0 % 0");
        assertTrue(Double.isNaN(interp.symbolTable.get("x").value));
    }

    @Test
    void testModuloWithNegativeNumbers() throws Exception {
        Interpreter interp = executeAndReturn("let x = -7 % 3");
        assertEquals(-1.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testDoubleUnaryMinus() throws Exception {
        Interpreter interp = executeAndReturn("let x = --5");
        assertEquals(5.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testUnaryPlus() throws Exception {
        Interpreter interp = executeAndReturn("let x = +5");
        assertEquals(5.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testZeroDividedByZero() throws Exception {
        Interpreter interp = executeAndReturn("let x = 0 / 0");
        assertTrue(Double.isNaN(interp.symbolTable.get("x").value));
    }

    @Test
    void testVariableAfterReassignment() throws Exception {
        Interpreter interp = executeLines("let a = 5", "a = 10", "let b = a * 2");
        assertEquals(20.0, interp.symbolTable.get("b").value);
    }

    @Test
    void testNegativeFloat() throws Exception {
        Interpreter interp = executeAndReturn("let x = -3.14");
        assertEquals(-3.14, interp.symbolTable.get("x").value, 0.001);
    }
}

