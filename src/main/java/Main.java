import interpreter.Interpreter;
import lexer.Lexer;
import lexer.LexerException;
import optimizer.Optimizer;
import parser.Parser;
import stmt.Stmt;
import token.Token;

void main() {
    Interpreter interpreter = new Interpreter(new HashMap<>());
    Optimizer optimizer = new Optimizer(new HashMap<>());
    try (
            FileReader fr = new FileReader("src/main/resources/testcode/code.mylang")
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