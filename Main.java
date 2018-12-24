import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {


    static {
        System.out.println("              ________    __      __     ________      ___________ ");
        System.out.println("             |   __   |  |  |    |  |   |   ____  \\   |   ________|");
        System.out.println("             |  |  |  |  |  |    |  |   |  |    |  |  |  |");
        System.out.println("             |  |__|  |  |  |    |  |   |  |__ /  /   |  |    ____");
        System.out.println("             |   _____|  |  |    |  |   |   __   |    |  |   |__  |");
        System.out.println("             |  |        |  |    |  |   |  |   \\  \\   |  |     |  |");
        System.out.println("             |  |        |  |____|  |   |  |____|  |  |  |_____|  |");
        System.out.println("             |__|        |__________|   |_________/   |___________|");
    }

    public static void main(String[] args) {
        PUBG pubg = new PUBG();
        pubg.game();
        pubg.setGameObject(pubg);
        System.out.println("\n\t\t\t\tPlease wait while game is loading.\n");
        System.out.print("\t\t\t\t");
        for (int i = 0; i < 45; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("|");
        }
        System.out.println("\n\n\t\t\t\tGame is Staring.");
        System.out.println("\n\n Connecting players to server.......\n");

        try {
            System.out.println(pubg.initialDisplay());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (pubg != null) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            do {
                try {
                    executor.execute(pubg);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (pubg.getGameStatus());
            System.out.println("\n\n Total squad and players information after game.......\n");
            System.out.println(pubg.display());
            System.out.println("\n\n\n\n Winner Winner Chicken Dinner.......\n\n");
            System.out.println(pubg.finalWinner());
            executor.shutdown();
            while (!executor.isTerminated()) {
            }

        }
    }
}
