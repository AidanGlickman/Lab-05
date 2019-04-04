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
        return new Application(left.alphaConvert(from, to, false), right);
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

    public Expression stabilize(Expression expression){
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



        if(newLeft instanceof Variable || newLeft instanceof Application) {
            return new Application(newLeft, newRight);
        }
        else return ((Function)newLeft).replace(newRight);
    }

    @Override
    public Expression replace(String from, Expression to) {
        return new Application(left.replace(from, to), right.replace(from, to));
    }

    public String genVar(String oldVar, HashSet<String> usedVars){
        String[] splitvar = splitNum(oldVar);

        String newVar = splitvar[0] + Integer.toString(Integer.parseInt(splitvar[1])+1);
        while(true){
            if(usedVars.contains(newVar)){
                splitvar = splitNum(newVar);
                newVar = splitvar[0] + Integer.toString(Integer.parseInt(splitvar[1])+1);
            }
            else return newVar;
        }
    }

    public Expression removeConflicts(Expression e1, Expression e2){
        HashSet<String> e1vars = e1.boundVariables();
        HashSet<String> e2vars = e2.boundVariables();

        HashSet<String> usedVars = e1.allVariables();
        usedVars.addAll(e2.allVariables());

        HashSet<String> problemVars = e1vars;
        problemVars.retainAll(e2vars);

        for(String s : problemVars){
            if(splitNum(s)[1] == null) s += "0";
            e1 = e1.alphaConvert(s, genVar(s, usedVars), false);
        }
        return e1;
    }


    public String[] splitNum(String unsplit){
        String[] split = new String[2];
        for(int i = 0; i < split.length; i++){
            try{
                split[0] = unsplit.substring(0, i);
                split[1] = Integer.toString(Integer.parseInt(unsplit.substring(i)));

                return split;
            } catch(Exception e) {
                continue;
            }
        }
        return split;
    }
}
