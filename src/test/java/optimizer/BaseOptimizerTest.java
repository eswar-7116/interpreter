package optimizer;

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

public abstract class BaseOptimizerTest {

    protected Optimizer newOptimizer() {
        return new Optimizer(new HashMap<>());
    }

    protected Optimizer optimizerWith(Map<String, Double> constants) {
        return new Optimizer(constants);
    }

    protected Stmt parse(String input) throws IOException, LexerException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            List<Token> tokens = lexer.lexLine();
            Parser parser = new Parser(tokens);
            return parser.parse();
        }
    }
}
