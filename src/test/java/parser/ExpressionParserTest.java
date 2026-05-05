package parser;

import expr.*;
import org.junit.jupiter.api.Test;
import stmt.ExprStmt;
import stmt.Stmt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTest extends BaseParserTest {

    @Test
    void testLiteralNumber() throws Exception {
        Stmt stmt = parse("42");
        assertInstanceOf(ExprStmt.class, stmt);
        ExprStmt exprStmt = (ExprStmt) stmt;
        assertInstanceOf(LiteralExpr.class, exprStmt.expr());
        assertEquals(42.0, ((LiteralExpr) exprStmt.expr()).value());
    }

    @Test
    void testFloatLiteral() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("3.14");
        assertEquals(3.14, ((LiteralExpr) exprStmt.expr()).value());
    }

    @Test
    void testSimpleAddition() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("1 + 2");
        BinaryExpr binary = (BinaryExpr) exprStmt.expr();
        assertEquals(1.0, ((LiteralExpr) binary.left()).value());
        assertEquals("+", binary.operator().lexeme());
        assertEquals(2.0, ((LiteralExpr) binary.right()).value());
    }

    @Test
    void testMultiplicationPrecedence() throws Exception {
        // 1 + 2 * 3 → 1 + (2 * 3)
        ExprStmt exprStmt = (ExprStmt) parse("1 + 2 * 3");
        BinaryExpr add = (BinaryExpr) exprStmt.expr();
        assertEquals("+", add.operator().lexeme());
        assertEquals(1.0, ((LiteralExpr) add.left()).value());
        BinaryExpr mul = (BinaryExpr) add.right();
        assertEquals("*", mul.operator().lexeme());
    }

    @Test
    void testParenthesesOverridePrecedence() throws Exception {
        // (1 + 2) * 3
        ExprStmt exprStmt = (ExprStmt) parse("(1 + 2) * 3");
        BinaryExpr mul = (BinaryExpr) exprStmt.expr();
        assertEquals("*", mul.operator().lexeme());
        assertInstanceOf(BinaryExpr.class, mul.left());
        assertEquals(3.0, ((LiteralExpr) mul.right()).value());
    }

    @Test
    void testSubtractionAndDivision() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("10 - 4 / 2");
        BinaryExpr sub = (BinaryExpr) exprStmt.expr();
        assertEquals("-", sub.operator().lexeme());
        assertInstanceOf(BinaryExpr.class, sub.right());
    }

    @Test
    void testModuloOperator() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("10 % 3");
        BinaryExpr mod = (BinaryExpr) exprStmt.expr();
        assertEquals("%", mod.operator().lexeme());
    }

    @Test
    void testUnaryMinus() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("-5");
        BinaryExpr binary = (BinaryExpr) exprStmt.expr();
        assertEquals("-", binary.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) binary.left()).value());
        assertEquals(5.0, ((LiteralExpr) binary.right()).value());
    }

    @Test
    void testUnaryPlus() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("+5");
        BinaryExpr binary = (BinaryExpr) exprStmt.expr();
        assertEquals("+", binary.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) binary.left()).value());
    }

    @Test
    void testDoubleUnaryMinus() throws Exception {
        // --5 → 0 - (0 - 5)
        ExprStmt exprStmt = (ExprStmt) parse("--5");
        BinaryExpr outer = (BinaryExpr) exprStmt.expr();
        assertEquals("-", outer.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) outer.left()).value());
        BinaryExpr inner = (BinaryExpr) outer.right();
        assertEquals("-", inner.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) inner.left()).value());
        assertEquals(5.0, ((LiteralExpr) inner.right()).value());
    }

    @Test
    void testUnaryInBinaryExpression() throws Exception {
        // 1 + -2 → 1 + (0 - 2)
        ExprStmt exprStmt = (ExprStmt) parse("1 + -2");
        BinaryExpr add = (BinaryExpr) exprStmt.expr();
        assertEquals("+", add.operator().lexeme());
        assertEquals(1.0, ((LiteralExpr) add.left()).value());
        BinaryExpr neg = (BinaryExpr) add.right();
        assertEquals("-", neg.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) neg.left()).value());
        assertEquals(2.0, ((LiteralExpr) neg.right()).value());
    }

    @Test
    void testNestedParentheses() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("((1 + 2))");
        BinaryExpr add = (BinaryExpr) exprStmt.expr();
        assertEquals("+", add.operator().lexeme());
    }

    @Test
    void testLeftAssociativeAddition() throws Exception {
        // 1 + 2 + 3 → (1 + 2) + 3
        ExprStmt exprStmt = (ExprStmt) parse("1 + 2 + 3");
        BinaryExpr outer = (BinaryExpr) exprStmt.expr();
        assertEquals("+", outer.operator().lexeme());
        assertEquals(3.0, ((LiteralExpr) outer.right()).value());
        BinaryExpr inner = (BinaryExpr) outer.left();
        assertEquals("+", inner.operator().lexeme());
        assertEquals(1.0, ((LiteralExpr) inner.left()).value());
        assertEquals(2.0, ((LiteralExpr) inner.right()).value());
    }

    @Test
    void testLeftAssociativeMultiplication() throws Exception {
        // 2 * 3 * 4 → (2 * 3) * 4
        ExprStmt exprStmt = (ExprStmt) parse("2 * 3 * 4");
        BinaryExpr outer = (BinaryExpr) exprStmt.expr();
        assertEquals("*", outer.operator().lexeme());
        assertEquals(4.0, ((LiteralExpr) outer.right()).value());
        assertInstanceOf(BinaryExpr.class, outer.left());
    }

    @Test
    void testVariableReference() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("x");
        assertInstanceOf(VariableExpr.class, exprStmt.expr());
        assertEquals("x", ((VariableExpr) exprStmt.expr()).name().lexeme());
    }

    @Test
    void testVariableInExpression() throws Exception {
        ExprStmt exprStmt = (ExprStmt) parse("x + 1");
        BinaryExpr binary = (BinaryExpr) exprStmt.expr();
        assertInstanceOf(VariableExpr.class, binary.left());
    }

    @Test
    void testRightAssociativeAssignment() throws Exception {
        // a = b = 5 → a = (b = 5)
        ExprStmt exprStmt = (ExprStmt) parse("a = b = 5");
        AssignExpr outer = (AssignExpr) exprStmt.expr();
        assertEquals("a", outer.name().lexeme());
        AssignExpr inner = (AssignExpr) outer.value();
        assertEquals("b", inner.name().lexeme());
        assertEquals(5.0, ((LiteralExpr) inner.value()).value());
    }

    @Test
    void testUnaryNegationOfParenExpr() throws Exception {
        // -(1 + 2) → 0 - (1 + 2)
        ExprStmt exprStmt = (ExprStmt) parse("-(1 + 2)");
        BinaryExpr neg = (BinaryExpr) exprStmt.expr();
        assertEquals("-", neg.operator().lexeme());
        assertEquals(0.0, ((LiteralExpr) neg.left()).value());
        BinaryExpr inner = (BinaryExpr) neg.right();
        assertEquals("+", inner.operator().lexeme());
    }

    @Test
    void testNullForBlankInput() throws Exception {
        assertNull(parse("   "));
    }

    @Test
    void testNullTokenList() {
        Parser parser = new Parser(null);
        assertNull(parser.parse());
    }

    @Test
    void testEmptyTokenList() {
        Parser parser = new Parser(List.of());
        assertNull(parser.parse());
    }
}
