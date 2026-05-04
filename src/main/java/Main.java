import expr.Expr;
import lexer.Lexer;
import lexer.LexerException;
import parser.Parser;
import token.Token;

void main() {
    try (
            FileReader fr = new FileReader("src/main/resources/testcode/code.mylang")
    ) {
        Lexer lexer = new Lexer(fr);
        List<Token> tokens;
        while ((tokens = lexer.lexLine()) != null) {
            Parser parser = new Parser(tokens);
            Expr ast = parser.parse();
            IO.println(ast);
        }
    } catch (LexerException e) {
        System.err.println("Lex error: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("IO error: " + e.getMessage());
    }
}