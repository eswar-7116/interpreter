package repl;

import interpreter.Interpreter;
import interpreter.Variable;
import lexer.Lexer;
import optimizer.Optimizer;
import parser.Parser;
import stmt.Stmt;
import token.Token;

import java.io.InputStreamReader;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class REPL {
    static void main() throws Exception {
        Lexer lexer = new Lexer(new InputStreamReader(System.in));
        Map<String, Variable> symbolTable = new HashMap<>();
        Interpreter interpreter = new Interpreter(symbolTable);
        Optimizer optimizer = new Optimizer(new HashMap<>());

        while (true) {
            IO.print(">>> ");

            List<Token> tokens = lexer.lexLine();
            if (tokens == null) break; // EOF

            if (tokens.isEmpty()) continue;

            try {
                Parser parser = new Parser(tokens);
                Stmt stmt = parser.parse();
                if (stmt != null) {
                    optimizer.optimize(stmt);
                    interpreter.execute(stmt);
                }
            } catch (Exception e) {
                IO.println("Error: " + e.getMessage());
            }
        }
    }
}