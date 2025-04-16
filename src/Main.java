import Model.Player;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Utilities util = new Utilities();
        System.out.println(util.startMessage());

        util.askForName();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        Player player = new Player(input, 50, 10, 1);

        while(util.quit()) {

        }


        System.out.println(util.endMessage());
    }
}
