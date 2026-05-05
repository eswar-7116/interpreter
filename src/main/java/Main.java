import interpreter.Interpreter;
import lexer.Lexer;
import lexer.LexerException;
import optimizer.Optimizer;
import parser.Parser;
import stmt.Stmt;
import token.Token;

void main(String[] args) {
    String path;
    if (args.length >= 1) {
        path = args[0];
    } else {
        System.err.println("Usage: flux <source-file>");
        System.err.println("Example: flux program.flux");
        System.exit(1);
        return;
    }

    Interpreter interpreter = new Interpreter(new HashMap<>());
    Optimizer optimizer = new Optimizer(new HashMap<>());
    try (
            FileReader fr = new FileReader(path)
    ) {
        Lexer lexer = new Lexer(fr);
        List<Token> tokens;
        while ((tokens = lexer.lexLine()) != null) {
            Parser parser = new Parser(tokens);
            Stmt stmt = parser.parse();
            if (stmt != null) {
                stmt = optimizer.optimize(stmt);
                interpreter.execute(stmt);
            }
        }
    } catch (LexerException e) {
        System.err.println("Lex error: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("IO error: " + e.getMessage());
    }
}