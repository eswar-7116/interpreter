package interpreter;

import expr.*;
import lexer.Lexer;
import lexer.LexerException;
import parser.Parser;
import stmt.Stmt;
import token.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseInterpreterTest {

    protected Interpreter newInterpreter() {
        return new Interpreter(new HashMap<>());
    }

    protected Interpreter interpreterWith(Map<String, Variable> symbolTable) {
        return new Interpreter(symbolTable);
    }

    protected Stmt parse(String input) throws IOException, LexerException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            List<Token> tokens = lexer.lexLine();
            Parser parser = new Parser(tokens);
            return parser.parse();
        }
    }

    /**
     * Helper: execute a single line and return the interpreter for inspection.
     */
    protected Interpreter executeAndReturn(String input) throws IOException, LexerException {
        Interpreter interpreter = newInterpreter();
        Stmt stmt = parse(input);
        if (stmt != null) interpreter.execute(stmt);
        return interpreter;
    }

    /**
     * Helper: execute multiple lines on the same interpreter.
     */
    protected Interpreter executeLines(String... lines) throws IOException, LexerException {
        Interpreter interpreter = newInterpreter();
        for (String line : lines) {
            Stmt stmt = parse(line);
            if (stmt != null) interpreter.execute(stmt);
        }
        return interpreter;
    }
}
