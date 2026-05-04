package optimizer;

import expr.AssignExpr;
import expr.BinaryExpr;
import expr.Expr;
import expr.LiteralExpr;
import token.Token;

public class Optimizer {
    public Expr optimize(Expr expr) {
        return switch (expr) {
            case AssignExpr(Token name, Expr value) -> new AssignExpr(name, optimize(value));
            case BinaryExpr(Expr left, Token op, Expr right) -> {
                Expr l = optimize(left);
                Expr r = optimize(right);

                // constant folding
                if (l instanceof LiteralExpr(double lv) && r instanceof LiteralExpr(double rv)) {

                    double result = switch (op.type()) {
                        case PLUS -> lv + rv;
                        case MINUS -> lv - rv;
                        case ASTERISK -> lv * rv;
                        case FSLASH -> {
                            if (rv == 0)
                                throw new RuntimeException("Division by zero");
                            else
                                yield lv / rv;
                        }
                        case PERCENT -> {
                            if (rv == 0)
                                throw new RuntimeException("Modulo by zero");
                            else
                                yield lv % rv;
                        }
                        default -> throw new RuntimeException("Unknown operator");
                    };
                    yield new LiteralExpr(result);
                }
                yield new BinaryExpr(l, op, r);
            }
            default -> expr;
        };
    }
}
