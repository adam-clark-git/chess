package client;
import java.util.Scanner;
import chess.*;

import static ui.EscapeSequences.*;

public class ClientMain {
    private final ServerFacade facade = new ServerFacade(0);
    private boolean loggedIn = false;
    private String authToken = null;
    public static void main(String[] args) {

        System.out.println("♕ 240 Chess Client: type Help to get started");


        ClientMain client = new ClientMain();
        client.client();
    }
    private void client() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[LOGGED_OUT] >>> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            String[] tokens = line.trim().split("\\s+");
            String command = tokens[0].toLowerCase();
            if (!loggedIn) {
                prelogin(command, tokens);
            }

        }
    }
    private void prelogin(String command, String[] tokens) {
        if (command.equals("help")) {
            help();
        }
        else if (command.equals("register")) {
            if (tokens.length < 4) {
                System.out.println("Error: Missing details");
            } else {
                try {
                    facade.register(tokens[1], tokens[2], tokens[3]);
                } catch (ResponseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private static void help() {
        System.out.println(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - Creates a new account\n" +
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - Logins into existing account\n" +
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - Stop running program\n" +
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - Displays possible commands\n");
    }
}
