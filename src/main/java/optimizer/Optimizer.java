package optimizer;

import expr.*;
import token.Token;
import stmt.*;

import java.util.Map;

public class Optimizer {

    private final Map<String, Double> constants;

    public Optimizer(Map<String, Double> constants) {
        this.constants = constants;
    }

    public Stmt optimize(Stmt stmt) {
        return switch (stmt) {
            case LetStmt(Token name, Expr init) -> {
                Expr opt = optimize(init);

                if (opt instanceof LiteralExpr(double v)) {
                    constants.put(name.lexeme(), v);
                } else {
                    constants.remove(name.lexeme());
                }

                yield new LetStmt(name, opt);
            }
            case ConstStmt(Token name, Expr init) -> {
                Expr opt = optimize(init);

                if (opt instanceof LiteralExpr(double v)) {
                    constants.put(name.lexeme(), v);
                }

                yield new ConstStmt(name, opt);
            }
            case DelStmt(Token name) -> {
                constants.remove(name.lexeme());
                yield stmt;
            }
            case PrintStmt(Expr expr) -> new PrintStmt(optimize(expr));
            case ExprStmt(Expr expr) -> new ExprStmt(optimize(expr));
            case ExitStmt(Expr code) -> new ExitStmt(code == null ? null : optimize(code));
            default -> stmt;
        };
    }

    public Expr optimize(Expr expr) {
        return switch (expr) {
            case VariableExpr(Token name) -> {
                if (constants.containsKey(name.lexeme())) {
                    yield new LiteralExpr(constants.get(name.lexeme()));
                }
                yield expr;
            }
            case AssignExpr(Token name, Expr value) -> {
                Expr optimizedValue = optimize(value);

                // constant propagation
                if (optimizedValue instanceof LiteralExpr(double v)) {
                    constants.put(name.lexeme(), v);
                } else {
                    constants.remove(name.lexeme());
                }

                yield new AssignExpr(name, optimizedValue);
            }

            case BinaryExpr(Expr left, Token op, Expr right) -> {
                Expr l = optimize(left);
                Expr r = optimize(right);

                // constant folding
                if (l instanceof LiteralExpr(double lv) && r instanceof LiteralExpr(double rv)) {
                    double result = switch (op.type()) {
                        case PLUS -> lv + rv;
                        case MINUS -> lv - rv;
                        case ASTERISK -> lv * rv;
                        case FSLASH -> lv / rv;
                        case PERCENT -> {
                            if (rv == 0)
                                throw new RuntimeException("Modulo by zero");
                            yield lv % rv;
                        }
                        default -> throw new RuntimeException("Unknown operator");
                    };

                    yield new LiteralExpr(result);
                }

                // Algebraic simplifications

                // x + 0 → x
                if (op.type().name().equals("PLUS")) {
                    if (l instanceof LiteralExpr(double lv) && lv == 0) yield r;
                    if (r instanceof LiteralExpr(double rv) && rv == 0) yield l;
                }

                // x * 1 → x
                if (op.type().name().equals("ASTERISK")) {
                    if (l instanceof LiteralExpr(double lv) && lv == 1) yield r;
                    if (r instanceof LiteralExpr(double rv) && rv == 1) yield l;

                    // x * 0 → 0
                    if ((l instanceof LiteralExpr(double lv) && lv == 0) ||
                            (r instanceof LiteralExpr(double rv) && rv == 0)) {
                        yield new LiteralExpr(0);
                    }
                }

                yield new BinaryExpr(l, op, r);
            }
            default -> expr;
        };
    }
}