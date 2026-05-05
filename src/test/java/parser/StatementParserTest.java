package parser;

import expr.*;
import org.junit.jupiter.api.Test;
import stmt.*;

import static org.junit.jupiter.api.Assertions.*;

public class StatementParserTest extends BaseParserTest {

    @Test
    void testLetStatement() throws Exception {
        Stmt stmt = parse("let x = 10");
        assertInstanceOf(LetStmt.class, stmt);
        LetStmt letStmt = (LetStmt) stmt;
        assertEquals("x", letStmt.name().lexeme());
        assertInstanceOf(LiteralExpr.class, letStmt.initializer());
        assertEquals(10.0, ((LiteralExpr) letStmt.initializer()).value());
    }

    @Test
    void testLetWithExpression() throws Exception {
        Stmt stmt = parse("let y = 1 + 2");
        LetStmt letStmt = (LetStmt) stmt;
        assertEquals("y", letStmt.name().lexeme());
        assertInstanceOf(BinaryExpr.class, letStmt.initializer());
    }

    @Test
    void testConstStatement() throws Exception {
        Stmt stmt = parse("const pi = 3");
        assertInstanceOf(ConstStmt.class, stmt);
        ConstStmt constStmt = (ConstStmt) stmt;
        assertEquals("pi", constStmt.name().lexeme());
        assertEquals(3.0, ((LiteralExpr) constStmt.initializer()).value());
    }

    @Test
    void testConstWithExpression() throws Exception {
        Stmt stmt = parse("const c = 2 * 5");
        ConstStmt constStmt = (ConstStmt) stmt;
        assertInstanceOf(BinaryExpr.class, constStmt.initializer());
    }

    @Test
    void testPrintStatement() throws Exception {
        Stmt stmt = parse("print 42");
        assertInstanceOf(PrintStmt.class, stmt);
        PrintStmt printStmt = (PrintStmt) stmt;
        assertEquals(42.0, ((LiteralExpr) printStmt.expr()).value());
    }

    @Test
    void testPrintExpression() throws Exception {
        Stmt stmt = parse("print 1 + 2");
        PrintStmt printStmt = (PrintStmt) stmt;
        assertInstanceOf(BinaryExpr.class, printStmt.expr());
    }

    @Test
    void testPrintVariable() throws Exception {
        Stmt stmt = parse("print x");
        PrintStmt printStmt = (PrintStmt) stmt;
        assertInstanceOf(VariableExpr.class, printStmt.expr());
    }

    @Test
    void testExitNoCode() throws Exception {
        Stmt stmt = parse("exit");
        assertInstanceOf(ExitStmt.class, stmt);
        ExitStmt exitStmt = (ExitStmt) stmt;
        assertNull(exitStmt.code());
    }

    @Test
    void testExitWithCode() throws Exception {
        Stmt stmt = parse("exit 1");
        ExitStmt exitStmt = (ExitStmt) stmt;
        assertNotNull(exitStmt.code());
        assertEquals(1.0, ((LiteralExpr) exitStmt.code()).value());
    }

    @Test
    void testDelStatement() throws Exception {
        Stmt stmt = parse("del x");
        assertInstanceOf(DelStmt.class, stmt);
        DelStmt delStmt = (DelStmt) stmt;
        assertEquals("x", delStmt.name().lexeme());
    }

    @Test
    void testAssignmentExpression() throws Exception {
        Stmt stmt = parse("x = 5");
        assertInstanceOf(ExprStmt.class, stmt);
        ExprStmt exprStmt = (ExprStmt) stmt;
        assertInstanceOf(AssignExpr.class, exprStmt.expr());
        AssignExpr assign = (AssignExpr) exprStmt.expr();
        assertEquals("x", assign.name().lexeme());
        assertEquals(5.0, ((LiteralExpr) assign.value()).value());
    }

    @Test
    void testAssignmentWithExpression() throws Exception {
        Stmt stmt = parse("x = 1 + 2");
        ExprStmt exprStmt = (ExprStmt) stmt;
        AssignExpr assign = (AssignExpr) exprStmt.expr();
        assertEquals("x", assign.name().lexeme());
        assertInstanceOf(BinaryExpr.class, assign.value());
    }

    @Test
    void testExpressionStatement() throws Exception {
        Stmt stmt = parse("1 + 2");
        assertInstanceOf(ExprStmt.class, stmt);
    }
}
