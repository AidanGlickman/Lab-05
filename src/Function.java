import java.awt.desktop.AppForegroundListener;
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
        return new Function(this.left.alphaConvert(from, to, captured), ((Variable) this.right))
    }

    @Override
    public HashSet<String> allVariables() {
        HashSet<String> vars = this.left.allVariables();
        vars.addAll(this.right.allVariables());
        return vars;
    }

    @Override
    public HashSet<String> boundVariables() {
        HashSet<String> vars = this.left.boundVariables();
        vars.addAll(this.right.boundVariables());
        return vars;
    }

    @Override
    public Expression eval() {

    }

    @Override
    public Expression replace(String from, Expression to) {
        return null;
    }

    public Expression replace(Expression to){
    return null;
    }
}
