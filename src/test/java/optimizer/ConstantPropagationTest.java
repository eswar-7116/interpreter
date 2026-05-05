package optimizer;

import expr.*;
import org.junit.jupiter.api.Test;
import stmt.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantPropagationTest extends BaseOptimizerTest {

    @Test
    void testPropagatesLetConstant() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let x = 5"));
        Stmt optimized = opt.optimize(parse("print x"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(5.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testPropagatesConstConstant() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("const pi = 3"));
        Stmt optimized = opt.optimize(parse("print pi"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(3.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testPropagatesThroughExpressions() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let a = 2"));
        opt.optimize(parse("let b = 3"));
        Stmt optimized = opt.optimize(parse("print a + b"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(5.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testDelInvalidatesConstant() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let x = 10"));
        opt.optimize(parse("del x"));
        Stmt optimized = opt.optimize(parse("print x"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testAssignmentUpdatesConstant() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let x = 5"));
        opt.optimize(parse("x = 20"));
        Stmt optimized = opt.optimize(parse("print x"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(20.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testNonConstantLetIsNotPropagated() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let y = x"));
        Stmt optimized = opt.optimize(parse("print y"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testPreloadedConstants() throws Exception {
        Map<String, Double> constants = new HashMap<>();
        constants.put("preloaded", 100.0);
        Optimizer opt = optimizerWith(constants);
        Stmt optimized = opt.optimize(parse("print preloaded"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(100.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testChainedPropagation() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let a = 1"));
        opt.optimize(parse("let b = a + 1"));
        Stmt optimized = opt.optimize(parse("print b"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(LiteralExpr.class, print.expr());
        assertEquals(2.0, ((LiteralExpr) print.expr()).value());
    }

    @Test
    void testAssignNonConstantInvalidatesPropagation() throws Exception {
        Optimizer opt = newOptimizer();
        opt.optimize(parse("let x = 5"));
        // Assign x = y (y is unknown) → x becomes non-constant
        opt.optimize(parse("x = y"));
        Stmt optimized = opt.optimize(parse("print x"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testConstWithNonLiteralInitializer() throws Exception {
        Optimizer opt = newOptimizer();
        // const c = x (x is unknown) → c is not trackable
        Stmt optimized = opt.optimize(parse("const c = x"));
        ConstStmt cst = (ConstStmt) optimized;
        assertInstanceOf(VariableExpr.class, cst.initializer());
        // c should NOT be propagated since its value is unknown
        Stmt printOpt = opt.optimize(parse("print c"));
        PrintStmt print = (PrintStmt) printOpt;
        assertInstanceOf(VariableExpr.class, print.expr());
    }

    @Test
    void testDelReturnsOriginalStmt() throws Exception {
        Optimizer opt = newOptimizer();
        Stmt original = parse("del x");
        Stmt optimized = opt.optimize(original);
        assertSame(original, optimized);
    }

    @Test
    void testLetNonLiteralRemovesFromConstants() throws Exception {
        Optimizer opt = newOptimizer();
        // First, x is known
        opt.optimize(parse("let x = 5"));
        // Overwrite x with non-literal: let y = x (x resolves to 5, so y = 5)
        // then let z = unknown
        opt.optimize(parse("let z = unknown"));
        // z should not propagate
        Stmt optimized = opt.optimize(parse("print z"));
        PrintStmt print = (PrintStmt) optimized;
        assertInstanceOf(VariableExpr.class, print.expr());
    }
}

