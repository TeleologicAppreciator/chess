package client;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("\u265A Welcome to 240 Chess, please login to start \u2654");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        System.exit(0);
    }

    private void printPrompt() {
        System.out.print("\n[" + client.getLoginState() + "] >>> ");
    }
    
}
