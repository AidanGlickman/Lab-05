import java.util.HashSet;
// Daniel Kim, Aidan Glickman

public class Application implements Expression {

    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" +
                left +
                " " + right +
                ')';
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public Expression copy() {
        return new Application(left, right);
    }

    @Override
    public boolean equals(Expression other) {
        if(other instanceof Application){
            return left.equals(((Application) other).getLeft())
                    && right.equals(((Application) other).getRight());
        }
        return false;
    }

    @Override
    public Expression alphaConvert(String from, String to, boolean captured) {
        return new Application(left.alphaConvert(from, to, captured), right.alphaConvert(from, to, captured));
    }

    @Override
    public HashSet<String> allVariables() {
        HashSet<String> vars = left.allVariables();
        vars.addAll(right.allVariables());

        return vars;
    }

    @Override
    public HashSet<String> boundVariables() {
        HashSet<String> vars = left.boundVariables();
        HashSet<String> rightVars = right.boundVariables();
        vars.addAll(rightVars);

        return vars;
    }

    public Expression stabilize(Expression expression){
        if (expression instanceof Application) {
            Expression oldExp = expression.copy();
            expression = expression.eval();
            while (!oldExp.equals(expression) && expression instanceof Application) {
                oldExp = expression.copy();
                expression = expression.eval();
            }
        }
        return expression.copy();
    }

    @Override
    public Expression eval() {
        Expression newLeft = stabilize(left);
        Expression newRight = stabilize(right);

        if (newLeft instanceof Variable)
            return new Application(newLeft, newRight);
        else if (newLeft instanceof Application)
            return new Application(newLeft, newRight);
        else {
            if (newRight instanceof Variable)
                if (newLeft.boundVariables().contains(newRight.toString()))
                    newLeft = newLeft.alphaConvert(newRight.toString(), newRight + "'", false);

            return ((Function) newLeft).getRight().replace(((Function) newLeft).getLeft().toString(), newRight);
        }
    }

    @Override
    public Expression replace(String from, Expression to) {
        return new Application(left.replace(from, to), right.replace(from, to));
    }
}
