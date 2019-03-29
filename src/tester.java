public class tester {
    public static void main(String[] args) {
        lambdaRunner run = new lambdaRunner();
        Application app = new Application(null,null);
        String[] test = app.splitNum("d12121");
        System.out.println(test[0] + " " + test[1]);
    }
}
