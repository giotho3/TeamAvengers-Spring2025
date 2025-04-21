import java.util.Scanner;

public class Utilities
{
    public String startMessage() {
        return "   _____        _             _                             __   _    _            \n" +
                "  / ____|      (_)           (_)                           / _| | |  | |           \n" +
                " | |  __  _ __  _  _ __ ___   _   ___   _ __  ___    ___  | |_  | |_ | |__    ___  \n" +
                " | | |_ || '__|| || '_ ` _ \\ | | / _ \\ | '__|/ _ \\  / _ \\ |  _| | __|| '_ \\  / _ \\ \n" +
                " | |__| || |   | || | | | | || || (_) || |  |  __/ | (_) || |   | |_ | | | ||  __/ \n" +
                "  \\_____||_|   |_||_| |_| |_||_| \\___/ |_|   \\___|  \\___/ |_|    \\__||_| |_| \\___| \n" +
                "  _____            _   _____   _                                                   \n" +
                " |  __ \\          | | |  __ \\ | |                                                  \n" +
                " | |__) | ___   __| | | |__) || |  __ _   __ _  _   _   ___                        \n" +
                " |  _  / / _ \\ / _` | |  ___/ | | / _` | / _` || | | | / _ \\                       \n" +
                " | | \\ \\|  __/| (_| | | |     | || (_| || (_| || |_| ||  __/                       \n" +
                " |_|  \\_\\\\___| \\__,_| |_|     |_| \\__,_| \\__, | \\__,_| \\___|                       \n" +
                "                                          __/ |                                    \n" +
                "                                         |___/                                     ";
    }

    public String endMessage() {
        return "Thanks for playing!";
    }

    public boolean quit(Scanner input) {
        String confirmation;
        while (true) {
            System.out.println("Are you sure you want to quit? (y or n)");
            confirmation = input.next();
            if (confirmation.equals("y")) {
                System.out.println("Thanks for playing");
                return true;
            } else if (confirmation.equals("n")) {
                System.out.println("Returning to game");
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'");
            }
        }
    }

}
