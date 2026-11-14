package client;
import java.util.Scanner;
import chess.*;
import model.endpoints.RegisterRequest;
import model.endpoints.RegisterResult;
import server.Server;

import static ui.EscapeSequences.*;

public class ClientMain {
    private Server server;
    private final ServerFacade facade;
    private boolean loggedIn;
    private String authToken;
    public ClientMain() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        loggedIn = false;
    }
    public static void main(String[] args) {

        ClientMain client = new ClientMain();
        client.client();
    }
    private void client() {
        System.out.print(ERASE_SCREEN);
        System.out.println("♕ 240 Chess Client: type Help to get started");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!loggedIn) {
                System.out.print("[LOGGED_OUT] >>> ");
            }
            else {
                System.out.print("[LOGGED_IN] >>> ");
            }
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            String[] tokens = line.trim().split("\\s+");
            String command = tokens[0].toLowerCase();
            if (!loggedIn) {
                prelogin(command, tokens);
            }
            else {
                postlogin(command, tokens);
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
                    authToken = facade.register(tokens[1], tokens[2], tokens[3]).authToken();
                    loggedIn = true;
                } catch (ResponseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else if (command.equals("login")) {
            if (tokens.length < 3) {
                System.out.println("Error: Missing details");
            } else {
                try {
                    authToken = facade.login(tokens[1], tokens[2]).authToken();
                    loggedIn = true;
                } catch (ResponseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else if (command.equals("clear")) {
            try {
                facade.clear();

            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (command.equals("quit")) {
            System.exit(0);
        }
    }
    private void postlogin(String command, String[] tokens) {
        if (command.equals("help")) {
            help();
        }
        else if (command.equals("register")) {
            if (tokens.length < 4) {
                System.out.println("Error: Missing details");
            } else {
                try {
                    authToken = facade.register(tokens[1], tokens[2], tokens[3]).authToken();
                    loggedIn = true;
                } catch (ResponseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else if (command.equals("logout")) {
            try {
                facade.logout(authToken);
                loggedIn = false;
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (command.equals("clear")) {
            try {
                facade.clear();

            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (command.equals("quit")) {
            System.exit(0);
        }
    }
    private void help() {
        if (!loggedIn) {
            System.out.println(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - Creates a new account\n" +
                    SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - Logins into existing account");
        }
        else {
            System.out.println(SET_TEXT_COLOR_BLUE + "create <GAMENAME>" + RESET_TEXT_COLOR + " - Creates a new game\n" +
                    SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " - Lists all existing games\n" +
                    SET_TEXT_COLOR_BLUE + "join <ID> <WHITE|BLACK>" + RESET_TEXT_COLOR + " - Joins an existing game\n" +
                    SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " - Observes an existing game\n" +
                    SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " - Logs out of account");

        }
        System.out.println(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - Stop running program\n" +
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - Displays possible commands\n");
    }
}
