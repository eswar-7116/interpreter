package parser;

import lexer.Lexer;
import lexer.LexerException;
import stmt.Stmt;
import token.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public abstract class BaseParserTest {

    protected Stmt parse(String input) throws IOException, LexerException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            List<Token> tokens = lexer.lexLine();
            Parser parser = new Parser(tokens);
            return parser.parse();
        }
    }

    protected List<Token> lex(String input) throws IOException, LexerException {
        try (Lexer lexer = new Lexer(new StringReader(input))) {
            return lexer.lexLine();
        }
    }
}
