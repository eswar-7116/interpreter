package interpreter;

import org.junit.jupiter.api.Test;
import stmt.Stmt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PrintTest extends BaseInterpreterTest {

    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return baos.toString().trim();
    }

    @Test
    void testPrintLiteral() throws Exception {
        Interpreter interp = newInterpreter();
        Stmt stmt = parse("print 42");
        String output = captureOutput(() -> interp.execute(stmt));
        assertEquals("42.0", output);
    }

    @Test
    void testPrintExpression() throws Exception {
        Interpreter interp = newInterpreter();
        Stmt stmt = parse("print 1 + 2");
        String output = captureOutput(() -> interp.execute(stmt));
        assertEquals("3.0", output);
    }

    @Test
    void testPrintVariable() throws Exception {
        Interpreter interp = newInterpreter();
        interp.execute(parse("let x = 99"));
        Stmt printStmt = parse("print x");
        String output = captureOutput(() -> interp.execute(printStmt));
        assertEquals("99.0", output);
    }

    @Test
    void testPrintFloat() throws Exception {
        Interpreter interp = newInterpreter();
        Stmt stmt = parse("print 3.14");
        String output = captureOutput(() -> interp.execute(stmt));
        assertEquals("3.14", output);
    }

    @Test
    void testPrintNegative() throws Exception {
        Interpreter interp = newInterpreter();
        Stmt stmt = parse("print -5");
        String output = captureOutput(() -> interp.execute(stmt));
        assertEquals("-5.0", output);
    }

    @Test
    void testPrintComplex() throws Exception {
        Interpreter interp = newInterpreter();
        interp.execute(parse("let a = 10"));
        interp.execute(parse("let b = 3"));
        Stmt stmt = parse("print a % b");
        String output = captureOutput(() -> interp.execute(stmt));
        assertEquals("1.0", output);
    }
}
