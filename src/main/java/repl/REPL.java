package repl;

import interpreter.Interpreter;
import interpreter.Variable;
import lexer.Lexer;
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

        while (true) {
            System.out.print(">>> ");

            List<Token> tokens = lexer.lexLine();
            if (tokens == null) break; // EOF

            if (tokens.isEmpty()) continue;

            try {
                Parser parser = new Parser(tokens);
                Stmt stmt = parser.parse();

                interpreter.execute(stmt);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}