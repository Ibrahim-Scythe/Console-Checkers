public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-cli")) {
            CheckersCLI.playCheckersCLI();
        }
        else {
            try {
                CheckersGUI.playCheckersGUI();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
