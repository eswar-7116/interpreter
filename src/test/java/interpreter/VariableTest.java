package interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariableTest extends BaseInterpreterTest {

    @Test
    void testLetDeclares() throws Exception {
        Interpreter interp = executeAndReturn("let x = 10");
        assertTrue(interp.symbolTable.containsKey("x"));
        assertEquals(10.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testLetIsNotConst() throws Exception {
        Interpreter interp = executeAndReturn("let x = 10");
        assertFalse(interp.symbolTable.get("x").isConst);
    }

    @Test
    void testConstDeclares() throws Exception {
        Interpreter interp = executeAndReturn("const c = 42");
        assertTrue(interp.symbolTable.containsKey("c"));
        assertEquals(42.0, interp.symbolTable.get("c").value);
    }

    @Test
    void testConstIsConst() throws Exception {
        Interpreter interp = executeAndReturn("const c = 42");
        assertTrue(interp.symbolTable.get("c").isConst);
    }

    @Test
    void testReassignLetVariable() throws Exception {
        Interpreter interp = executeLines("let x = 1", "x = 99");
        assertEquals(99.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testReassignConstThrows() {
        assertThrows(InterpreterException.class, () ->
                executeLines("const c = 1", "c = 99"));
    }

    @Test
    void testReDeclareLet() {
        assertThrows(InterpreterException.class, () ->
                executeLines("let x = 1", "let x = 2"));
    }

    @Test
    void testReDeclarConst() {
        assertThrows(InterpreterException.class, () ->
                executeLines("const x = 1", "const x = 2"));
    }

    @Test
    void testUndefinedVariableRead() {
        Interpreter interp = newInterpreter();
        assertThrows(InterpreterException.class, () ->
                interp.execute(parse("print y")));
    }

    @Test
    void testUndefinedVariableAssign() {
        assertThrows(InterpreterException.class, () ->
                executeLines("z = 5"));
    }

    @Test
    void testDelRemovesVariable() throws Exception {
        Interpreter interp = executeLines("let x = 1", "del x");
        assertFalse(interp.symbolTable.containsKey("x"));
    }

    @Test
    void testDelUndefinedThrows() {
        assertThrows(RuntimeException.class, () ->
                executeLines("del y"));
    }

    @Test
    void testDelThenRedeclare() throws Exception {
        Interpreter interp = executeLines("let x = 1", "del x", "let x = 99");
        assertEquals(99.0, interp.symbolTable.get("x").value);
    }

    @Test
    void testVariableInExpression() throws Exception {
        Interpreter interp = executeLines("let a = 3", "let b = a + 2");
        assertEquals(5.0, interp.symbolTable.get("b").value);
    }

    @Test
    void testVariableChainedAssignment() throws Exception {
        Interpreter interp = executeLines("let x = 1", "let y = 2", "x = y = 10");
        assertEquals(10.0, interp.symbolTable.get("x").value);
        assertEquals(10.0, interp.symbolTable.get("y").value);
    }

    @Test
    void testAssignmentReturnsValue() throws Exception {
        // let a = 5; let b = (a = 10) → b should be 10
        Interpreter interp = executeLines("let a = 5", "let b = a = 10");
        assertEquals(10.0, interp.symbolTable.get("a").value);
        assertEquals(10.0, interp.symbolTable.get("b").value);
    }

    @Test
    void testMultipleVariables() throws Exception {
        Interpreter interp = executeLines(
                "let a = 1",
                "let b = 2",
                "let c = 3",
                "let d = a + b + c"
        );
        assertEquals(6.0, interp.symbolTable.get("d").value);
    }

    @Test
    void testDelConstVariable() throws Exception {
        // del should work on const variables too (it removes, not reassigns)
        Interpreter interp = executeLines("const c = 42", "del c");
        assertFalse(interp.symbolTable.containsKey("c"));
    }
}
