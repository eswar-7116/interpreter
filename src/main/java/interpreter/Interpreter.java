package interpreter;

import expr.BinaryExpr;
import expr.Expr;
import expr.LiteralExpr;
import parser.ParserException;
import token.Token;

public class Interpreter {
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
            } default -> throw new ParserException("Invalid expr: "+expr);
        };
    }
}
