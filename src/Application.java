import java.util.HashSet;

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
        return null;
    }

    @Override
    public HashSet<String> allVariables() {
        HashSet<String> vars = left.allVariables();
        HashSet<String> rightVars = right.allVariables();
        vars.addAll(rightVars);

        return vars;
    }

    @Override
    public HashSet<String> boundVariables() {
        HashSet<String> vars = left.boundVariables();
        HashSet<String> rightVars = right.boundVariables();
        vars.addAll(rightVars);

        return vars;
    }

    private Expression stabilize(Expression expression){
        Expression oldExp = expression;
        Expression newExp = expression.eval();

        while(!oldExp.equals(newExp)){
            oldExp = newExp;
            newExp = newExp.eval();
        }

        return newExp;
    }

    @Override
    public Expression eval() {
        Expression newLeft = stabilize(left);
        Expression newRight = stabilize(right);

        if(newLeft instanceof Variable || newLeft instanceof Application)
            return new Application(newLeft, newRight);
        else return null;
    }

    @Override
    public Expression replace(String from, Expression to) {
        return new Application(left.replace(from, to), right.replace(from, to));
    }

//    TODO
    public Expression removeConflicts(Expression e1, Expression e2){
        return null;
    }


//    TODO
    String[] splitNum(String unsplit){
        return null;
    }
}
