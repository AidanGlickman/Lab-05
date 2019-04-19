import java.util.HashSet;
// Daniel Kim, Aidan Glickman

public class Function implements Expression {
    private Variable left;
    private Expression right;

    public Function(Variable left, Expression right){
        this.left = left;
        this.right = right;
    }

    public Variable getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public Expression copy() {
        return new Function(left, right);
    }

    @Override
    public boolean equals(Expression other) {
        if (other instanceof Function) {
            Variable otherVar = ((Function)other).getLeft();
            Expression otherExp = ((Function)other).getRight();
            return right.equals(otherExp.alphaConvert(otherVar.toString(), left.toString(), true));
        }
        return false;
    }

    @Override
    public Expression alphaConvert(String from, String to, boolean captured) {
        Variable var = (Variable) left.alphaConvert(from, to, captured);
        Expression exp = right.alphaConvert(from, to, captured);

        if (this.left.toString().equals(from)) {
            var = (Variable) left.alphaConvert(from, to, true);
            exp = right.alphaConvert(from, to, true);
        }

        return new Function(var, exp);
    }

    @Override
    public HashSet<String> allVariables() {
        HashSet<String> vars = this.left.allVariables();
        vars.addAll(this.right.allVariables());
        return vars;
    }

    public String toString() {
        return ("(λ"+left.toString()+"."+right.toString()+")");
    }

    @Override
    public HashSet<String> boundVariables() {
        HashSet<String> vars = this.left.allVariables();
        vars.addAll(this.right.boundVariables());
        return vars;
    }

    @Override
    public Expression eval() {
        return new Function(left,right.eval());
    }

    @Override
    public Expression replace(String from, Expression to) {
        if (from.equals(left.toString()))
            return copy();
        return new Function(left, right.replace(from, to));
    }

}
