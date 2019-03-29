import java.util.*;

public class lambdaRunner {

    private static HashMap<String, Expression> defined = new HashMap<>();

    public static Expression lambdify(String e){

        e = e.replaceAll("\\\\", "λ");
        e = e.replaceAll("\\.", " . ");
        e = e.replaceAll("\\(", " (");
        e = e.replaceAll("\\)", ") ");
        e = e.trim();

        return buildTree(e);
    }

    public static Expression buildTree(String e){
        ArrayList<String> topLevels = splitTopLevelArgs(e);

        if(topLevels.size() == 1){
            String token = topLevels.get(0);
            if(defined.containsKey(token)){
                return defined.get(token);
            }
            else if(token.contains("(")){
                buildTree(token.substring(1,token.length()-1));
            }
            else{
                return new Variable(token);
            }
        }

        else{
            if(topLevels.get(0).substring(0,1).equals("λ")){
                return null;
            }
        }
        return null;
    }


    private static final HashSet<Character> DELIM = new HashSet<>(Arrays.asList(' ', '.'));

    public static ArrayList<String> splitTopLevelArgs(String e){
        ArrayList<String> topLevel = new ArrayList<>();

        int openParens = 0;
        String currentArg = "";
        for(int i = 0; i < e.length(); i++){
            char current = e.charAt(i);

            if(current == '(') openParens++;
            if(current == ')') openParens--;

            if(openParens == 0 && DELIM.contains(current)){
                if (!currentArg.equals("")) {
                    topLevel.add(currentArg);
                    currentArg = "";
                }
            }
            else {
                currentArg += current;
            }

        }

        if(!currentArg.equals("")){
            topLevel.add(currentArg);
        }

        return topLevel;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("> ");
            String input = scanner.nextLine().replaceAll("\uFEFF", "");
            lambdify(input);

            if(input.equals("exit")){
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }
}
