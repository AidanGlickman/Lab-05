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

    public static String stripParen(String e){
        e = e.substring(1);
        e = new StringBuilder(e).reverse().toString();
        e = e.replaceFirst("\\)", "");
        return new StringBuilder(e).reverse().toString();
    }

    public static int anyLambdas(ArrayList<String> args){
        for(int i = 0; i < args.size(); i++) {
            if (args.get(i).substring(0, 1).equals("λ"))
                return i;
        }
        return -1;
    }

    public static ArrayList<String> listRange(ArrayList<String> args, int bot, int top){

    }

    public static Expression buildTree(String e){
        ArrayList<String> topLevels = splitTopLevelArgs(e);
        System.out.println(topLevels);

        if(topLevels.size() == 1){
            String token = topLevels.get(0);
            if(defined.containsKey(token)){
                return defined.get(token);
            }
            else if(token.substring(0,1).equals("(")){
                return buildTree(stripParen(token));
            }
            else{
                return new Variable(token);
            }
        }

        else {
            ArrayList<String> elements = new ArrayList<>();
            for (int i = 1; i < topLevels.size(); i++) {
                elements.add(topLevels.get(i));
            }
            int nextLambdapos = anyLambdas(topLevels);

            String firstEle = topLevels.get(0);

            if (firstEle.substring(0, 1).equals("λ")) {
                Variable topVar = new Variable(firstEle.substring(1));

                return new Function(topVar, buildTree(rebuildString(elements)));
            }

            else {
                ArrayList<String> allButLast = new ArrayList<>();
                for (int i = 0; i < topLevels.size()-1; i++) {
                    allButLast.add(topLevels.get(i));
                }
                return new Application(buildTree(rebuildString(allButLast)), buildTree(topLevels.get(topLevels.size()-1)));
            }
        }
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

    public static String rebuildString(ArrayList<String> elements){
        return String.join(" ", elements);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("> ");
            String input = scanner.nextLine().replaceAll("\uFEFF", "");

            if(input.contains("=")){
                String[] parts = input.split("=");
                String name = parts[0].trim();
                if(defined.containsKey(name))
                    System.out.printf("%s is already defined.\n", name);
                else {
                    Expression definition = lambdify(parts[1]);
                    System.out.printf("Added %s as %s\n", definition, name);

                    defined.put(name, definition);
                }
            }

            else if(input.length() > 3 && input.substring(0,3).equalsIgnoreCase("run")){
                System.out.println(lambdify(input.substring(3)).eval());
            }

            else if(input.equals("exit")){
                System.out.println("Goodbye!");
                System.exit(0);
            }

            else{
                System.out.println(lambdify(input));
            }
        }
    }
}
