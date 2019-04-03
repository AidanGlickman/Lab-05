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

    // TODO
    @Override
    public boolean equals(Expression other) {

    }

    @Override
    public Expression alphaConvert(String from, String to, boolean captured) {
        if (left.toString().equals(from)) {
            return new Function((Variable) (left.alphaConvert(from,to,true)),(Expression) (right.alphaConvert(from,to,true)));
        } else {
            return new Function((Variable) (left.alphaConvert(from,to,false)),(Expression) (right.alphaConvert(from,to,false)));
        }
    }

    // Not sure if correct
    @Override
    public HashSet<String> allVariables() {
        HashSet<String> vars = this.left.allVariables();
        vars.addAll(this.right.allVariables());
        return vars;
    }

    // Not sure if correct
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
        return new Function(left,right.replace(from, to));
    }

    public Expression replace(Expression to){
        return new Function(left,right.replace(left.toString(),to));
    }
}
