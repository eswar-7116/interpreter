package benchmark;

import expr.*;
import interpreter.Interpreter;
import lexer.Lexer;
import optimizer.Optimizer;
import parser.Parser;
import stmt.*;
import token.Token;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BenchmarkRunner {

    static void main() throws Exception {
        String path = "src/main/resources/benchmark/benchmark.flux";
        
        List<List<Token>> allTokens = new ArrayList<>();
        try (FileReader fr = new FileReader(path); Lexer lexer = new Lexer(fr)) {
            List<Token> lineTokens;
            while ((lineTokens = lexer.lexLine()) != null) {
                allTokens.add(lineTokens);
            }
        }
        
        List<Stmt> statements = new ArrayList<>();
        long parseStart = System.nanoTime();
        for (List<Token> tokens : allTokens) {
            Parser parser = new Parser(tokens);
            Stmt stmt = parser.parse();
            if (stmt != null) {
                statements.add(stmt);
            }
        }
        long parseEnd = System.nanoTime();
        
        long initialNodes = 0;
        for (Stmt stmt : statements) {
            initialNodes += countNodes(stmt);
        }
        
        Optimizer optimizer = new Optimizer(new HashMap<>());
        List<Stmt> optimizedStatements = new ArrayList<>();
        long optStart = System.nanoTime();
        for (Stmt stmt : statements) {
            optimizedStatements.add(optimizer.optimize(stmt));
        }
        long optEnd = System.nanoTime();
        
        long optimizedNodes = 0;
        for (Stmt stmt : optimizedStatements) {
            optimizedNodes += countNodes(stmt);
        }
        
        Interpreter interpreter = new Interpreter(new HashMap<>());
        long execStart = System.nanoTime();
        for (Stmt stmt : optimizedStatements) {
            interpreter.execute(stmt);
        }
        long execEnd = System.nanoTime();
        
        IO.println("Benchmark results:");
        IO.println("Parsing Time: " + (parseEnd - parseStart) / 1_000_000.0 + " ms");
        IO.println("Optimization Time: " + (optEnd - optStart) / 1_000_000.0 + " ms");
        IO.println("Execution Time: " + (execEnd - execStart) / 1_000_000.0 + " ms");
        IO.println("Initial Nodes: " + initialNodes);
        IO.println("Optimized Nodes: " + optimizedNodes);
        double delta = (double)(initialNodes - optimizedNodes) / initialNodes * 100;
        IO.println("Optimization Delta: " + String.format("%.2f", delta) + "%");
    }

    private static long countNodes(Stmt stmt) {
        if (stmt == null) return 0;
        return switch (stmt) {
            case LetStmt let -> 1 + countNodes(let.initializer());
            case ConstStmt constStmt -> 1 + countNodes(constStmt.initializer());
            case PrintStmt print -> 1 + countNodes(print.expr());
            case ExprStmt exprStmt -> 1 + countNodes(exprStmt.expr());
            default -> 1;
        };
    }
    
    private static long countNodes(Expr expr) {
        if (expr == null) return 0;
        return switch (expr) {
            case BinaryExpr binary -> 1 + countNodes(binary.left()) + countNodes(binary.right());
            case AssignExpr assign -> 1 + countNodes(assign.value());
            default -> 1;
        };
    }
}
