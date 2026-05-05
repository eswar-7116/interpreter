package interpreter;

import expr.*;
import optimizer.Optimizer;
import stmt.*;
import token.Token;

import java.util.Map;

public class Interpreter {
    public Map<String, Variable> symbolTable;
    private final Optimizer optimizer;

    public Interpreter(Map<String, Variable> symbolTable) {
        this.symbolTable = symbolTable;
        optimizer = new Optimizer();
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

            case LetStmt(Token name, Expr init) -> {
                if (symbolTable.containsKey(name.lexeme())) {
                    throw new InterpreterException(String.format("Cannot re-declare variable '%s' at line %d, column %d", name.lexeme(), name.line(), name.column()));
                }

                double value = evaluate(init);
                symbolTable.put(name.lexeme(), new Variable(value, false));
            }

            case ConstStmt(Token name, Expr init) -> {
                if (symbolTable.containsKey(name.lexeme())) {
                    throw new InterpreterException(String.format("Cannot re-declare variable '%s' at line %d, column %d", name.lexeme(), name.line(), name.column()));
                }

                double value = evaluate(init);
                symbolTable.put(name.lexeme(), new Variable(value, true));
            }

            default -> throw new InterpreterException("Unknown statement: " + stmt);
        }
    }

    public double evaluate(Expr expr) {
        expr = optimizer.optimize(expr);
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
                    throw new InterpreterException("Undefined variable: " + name.lexeme());
                }
                yield symbolTable.get(name.lexeme()).value;
            }
            case AssignExpr(Token name, Expr value) -> {
                if (!symbolTable.containsKey(name.lexeme())) {
                    throw new InterpreterException("Undefined variable: " + name.lexeme());
                }

                Variable var = symbolTable.get(name.lexeme());
                if (var.isConst) {
                    throw new InterpreterException("Cannot reassign const variable: " + name.lexeme());
                }

                double evaluated = evaluate(value);
                var.value = evaluated;

                yield evaluated;
            }
            default -> throw new InterpreterException("Invalid expr: " + expr);
        };
    }
}
