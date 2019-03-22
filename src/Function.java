import java.util.HashSet;

public class Function implements Expression {
    private Variable left;
    private Expression right;

    public Function(Variable left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression copy() {
        return new Function(left, right);
    }

    @Override
    public boolean equals(Expression other) {
        return false;
    }

    @Override
    public Expression alphaConvert(String from, String to, boolean captured) {
        return null;
    }

    @Override
    public HashSet<String> allVariables() {
        return null;
    }

    @Override
    public HashSet<String> boundVariables() {
        return null;
    }

    @Override
    public Expression eval() {
        return null;
    }

    @Override
    public Expression replace(String from, Expression to) {
        return null;
    }

    public Expression replace(Expression to){
    return null;
    }
}
