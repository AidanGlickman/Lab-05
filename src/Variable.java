import java.util.HashSet;

public class Variable implements Expression {

    private String variableName;

    public Variable(String newVar) {
        variableName = newVar;
    }

    @Override
    public String toString() {
        return variableName;
    }

    @Override
    public Expression copy() {
        return new Variable(variableName);
    }

    @Override
    public boolean equals(Expression other) {
        if(other instanceof Variable) {
            return this.toString().equals(other.toString());
        }
        else return false;
    }

    @Override
    public Expression alphaConvert(String from, String to, boolean captured) {
        if (captured && variableName.equals(from)) {
            return new Variable(to);
        }
        return copy();
    }

    @Override
    public HashSet<String> allVariables() {
        HashSet<String> variables = new HashSet<>();
        variables.add(variableName);
        return variables;
    }

    @Override
    public HashSet<String> boundVariables() {
        return new HashSet<String>();
    }

    @Override
    public Expression eval() {
        return copy();
    }

    @Override
    public Expression replace(String from, Expression to) {
        if(variableName.equals(from)) return to.copy();
        else return copy();
    }
}
