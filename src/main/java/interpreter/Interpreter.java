package interpreter;

import expr.*;
import parser.ParserException;
import stmt.ExitStmt;
import stmt.ExprStmt;
import stmt.PrintStmt;
import stmt.Stmt;
import token.Token;

import java.util.Map;

public class Interpreter {
    public Map<String, Double> symbolTable;

    public Interpreter(Map<String, Double> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void execute(Stmt stmt) {
        switch (stmt) {
            case PrintStmt(Expr expr) -> {
                double value = evaluate(expr);
                System.out.println(value);
            }

            case ExitStmt(Expr codeExpr) -> {
                int code = 0;
                if (codeExpr != null) {
                    code = (int) evaluate(codeExpr);
                }
                System.exit(code);
            }

            case ExprStmt(Expr expr) -> evaluate(expr);

            default -> throw new RuntimeException("Unknown statement: " + stmt);
        }
    }

    public double evaluate(Expr expr) {
        return switch (expr) {
            case null -> -1;
            case LiteralExpr(double value) -> value;
            case BinaryExpr(Expr left, Token operator, Expr right) -> {
                double lhs = evaluate(left);
                double rhs = evaluate(right);
                yield switch (operator.type()) {
                    case PLUS -> lhs + rhs;
                    case MINUS -> lhs - rhs;
                    case ASTERISK -> lhs * rhs;
                    case FSLASH -> lhs / rhs;
                    case PERCENT -> lhs % rhs;
                    default -> -1;
                };
            }
            case VariableExpr(Token name) -> {
                if (!symbolTable.containsKey(name.lexeme())) {
                    throw new RuntimeException("Undefined variable: " + name.lexeme());
                }
                yield symbolTable.get(name.lexeme());
            }
            case AssignExpr(Token name, Expr value) -> {
                double evaluated = evaluate(value);
                symbolTable.put(name.lexeme(), evaluated);
                yield evaluated;
            }
            default -> throw new ParserException("Invalid expr: "+expr);
        };
    }
}
