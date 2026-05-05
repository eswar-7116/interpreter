package optimizer;

import expr.*;
import org.junit.jupiter.api.Test;
import stmt.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlgebraicSimplificationTest extends BaseOptimizerTest {

    @Test
    void testXPlusZeroBecomesX() throws Exception {
        Optimizer opt = newOptimizer();
        // x + 0 → x (x is unknown, so no constant folding, but algebraic simplification)
        Stmt stmt = parse("print x + 0");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
        assertEquals("x", ((VariableExpr) print.expr()).name().lexeme());
    }

    @Test
    void testZeroPlusXBecomesX() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 0 + x");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testXTimesOneBecomesX() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print x * 1");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testOneTimesXBecomesX() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 1 * x");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testXTimesZeroBecomesZero() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print x * 0");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(0.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testZeroTimesXBecomesZero() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt stmt = parse("print 0 * x");
        Stmt optimized = opt.optimize(stmt);
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(0.0, ((LiteralExpr) print.expr()).value());
    }
}
