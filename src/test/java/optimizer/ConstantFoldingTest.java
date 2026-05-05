package optimizer;

import expr.*;
import org.junit.jupiter.api.Test;
import stmt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantFoldingTest extends BaseOptimizerTest {

    @Test
    void testFoldsAddition() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 1 + 2");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(3.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testFoldsSubtraction() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 10 - 3");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(7.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testFoldsMultiplication() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 3 * 4");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(12.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testFoldsDivision() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 10 / 4");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(2.5, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testFoldsModulo() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 10 % 3");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(1.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testModuloByZeroThrows() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 10 % 0");
        assertThrows(RuntimeException.class, () -> opt.optimize(stmt));
    }

    @Test
    void testFoldsNestedExpression() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 1 + 2 * 3");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(7.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testFoldsWithParentheses() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print (1 + 2) * 3");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(9.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testDoesNotFoldWithVariable() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print x + 1");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        // x is unknown, so it can't fold
        assertInstanceOf(BinaryExpr.class, print.expr());
    }

    @Test
    void testFoldsLetInitializer() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("let x = 2 + 3");
        Stmt optimized = opt.optimize(stmt);
        LetStmt let = (LetStmt) optimized;
        assertInstanceOf(LiteralExpr.class, let.initializer());
        assertEquals(5.0, ((LiteralExpr) let.initializer()).value());
    }

    @Test
    void testFoldsConstInitializer() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("const c = 10 * 2");
        Stmt optimized = opt.optimize(stmt);
        ConstStmt cst = (ConstStmt) optimized;
        assertInstanceOf(LiteralExpr.class, cst.initializer());
        assertEquals(20.0, ((LiteralExpr) cst.initializer()).value());
    }

    @Test
    void testFoldsExprStmt() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("1 + 2");
        Stmt optimized = opt.optimize(stmt);
        ExprStmt exprStmt = (ExprStmt) optimized;
        assertInstanceOf(LiteralExpr.class, exprStmt.expr());
        assertEquals(3.0, ((LiteralExpr) exprStmt.expr()).value());
    }

    @Test
    void testFoldsExitCode() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("exit 1 + 0");
        Stmt optimized = opt.optimize(stmt);
        ExitStmt exit = (ExitStmt) optimized;
        assertInstanceOf(LiteralExpr.class, exit.code());
        assertEquals(1.0, ((LiteralExpr) exit.code()).value());
    }

    @Test
    void testExitNoCodeStaysNull() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("exit");
        Stmt optimized = opt.optimize(stmt);
        ExitStmt exit = (ExitStmt) optimized;
        assertNull(exit.code());
    }
}
