import java.util.Arrays;
import java.util.Random;

final class PUBG implements Runnable {
    private int counter = -1;
    private Squad[] mySquad = new Squad[1];
    private Random random = new Random();
    private int totalMatches, winnerSquad_Id;
    private PUBG gameObject;
    private boolean gameplay = true;
    private int counter_ = -1;
    private Player[] forMvpPlayers = new Player[1];

    PUBG() {
    }

    void game() {
        squadCreator();
    }

    private void winDisplay(Player winner, Player losser) {
        synchronized (PUBG.class) {
            System.out.print("\t\t\t\t");
            for (int i = 0; i < 45; i++) {
                System.out.print("-");
            }
            System.out.println("\n\t\t\t\t|\t" + winner.name + " VS " + losser.name + "\t|");
            System.out.println("\t\t\t\t|\t\t" + winner.getLife() + "\t\t\t |\t\t\t" + losser.playerLifeStatus() + "\t\t\t|");
            System.out.print("\t\t\t\t");
            for (int i = 0; i < 45; i++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    String finalWinner() {

        StringBuilder winner = new StringBuilder();
        winner.append("Squad Id : ")
                .append(mySquad[winnerSquad_Id].squadId)
                .append("\n")
                .append("Player name\t\t\t\t\tDamage\t\tKills\t\tHeal\t\tLife")
                .append("\n-------------------------------------------------------------------------------------\n");
        for (int i = 0; i < mySquad[winnerSquad_Id].squadMembers.length; i++) {
            winner.append(mySquad[winnerSquad_Id].squadMembers[i].name)
                    .append("    \t  \t")
                    .append(mySquad[winnerSquad_Id].squadMembers[i].damage)
                    .append("   \t  \t")
                    .append(mySquad[winnerSquad_Id].squadMembers[i].kills)
                    .append("   \t  \t")
                    .append(mySquad[winnerSquad_Id].squadMembers[i].heal)
                    .append("   \t  \t")
                    .append(mySquad[winnerSquad_Id].squadMembers[i].life)
                    .append("   \t  \t")
                    .append(mySquad[winnerSquad_Id].squadMembers[i].mvp)
                    .append("\n");
        }
        winner.append("-------------------------------------------------------------------------------------\n");
        return winner.toString();
    }

    String initialDisplay() {
        StringBuilder winner = new StringBuilder();
        for (Squad aMySquad : mySquad) {
            winner.append("\n-------------------------------------------------------------------------------------\n")
                    .append("Squad Id : ")
                    .append(aMySquad.squadId)
                    .append("\n-------------------------------------------------------------------------------------\n");
            for (int i = 0; i < aMySquad.squadMembers.length; i++) {
                winner.append(aMySquad.squadMembers[i].name)
                        .append("   \t  \t")
                        .append("Connected..........\n");
            }
            winner.append("-------------------------------------------------------------------------------------\n\n");
        }
        return winner.toString();
    }

    String display() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateMvp();
        StringBuilder winner = new StringBuilder();
        for (Squad aMySquad : mySquad) {
            winner.append("\n-------------------------------------------------------------------------------------\n")
                    .append("Squad Id : ")
                    .append(aMySquad.squadId)
                    .append("\n")
                    .append("Player name\t\t\t\t\tDamage\t\tKills\t\tHeal\t\tLife")
                    .append("\n-------------------------------------------------------------------------------------\n");
            for (int i = 0; i < aMySquad.squadMembers.length; i++) {
                winner.append(aMySquad.squadMembers[i].name)
                        .append("    \t  \t")
                        .append(aMySquad.squadMembers[i].damage)
                        .append("   \t  \t")
                        .append(aMySquad.squadMembers[i].kills)
                        .append("   \t  \t")
                        .append(aMySquad.squadMembers[i].heal)
                        .append("   \t  \t")
                        .append(aMySquad.squadMembers[i].life)
                        .append("   \t  \t")
                        .append(aMySquad.squadMembers[i].mvp)
                        .append("\n");
            }
            winner.append("-------------------------------------------------------------------------------------\n\n");
        }
        winner.append("Total number of matches played : ")
                .append(totalMatches);

        return winner.toString();
    }

    private int randomGenerator() {
        int randomData = 0;
        try {
            randomData = random.nextInt(15);
            Thread.sleep(10);
        } catch (Exception e) {
            System.out.println("Random ERR : " + e.getMessage());
        }
        return randomData;
    }

    private boolean win() {
        int winCounter = 0;
        for (int i = 0; i < mySquad.length; i++) {
            if (mySquad[i].aliveSquad) {
                winCounter++;
                winnerSquad_Id = i;
            }
        }
        if (winCounter == 1) {
            return true;
        }
        return false;
    }

    private int healing(Player p) {
        int heal_life = 0;
        if (p.life < 20) {
            heal_life = random.nextInt(60);
        } else if (p.life < 40) {
            heal_life = random.nextInt(40);
        } else if (p.life < 60) {
            heal_life = random.nextInt(20);
        } else if (p.life < 75) {
            heal_life = random.nextInt(5);
        }
        return heal_life;
    }

    private void fight(Pair p) {
        try {
            do {
                int attack1 = randomGenerator(), attack2 = randomGenerator();
                if (attack1 < attack2) {
                    p.player1.life = p.player1.life - (attack2 - attack1);
                    p.player2.damage += (attack2 - attack1);
                } else if (attack1 > attack2) {
                    p.player2.life = p.player2.life - (attack1 - attack2);
                    p.player1.damage += (attack1 - attack2);
                }
                if (p.player1.life <= 0 && p.player2.life > 0) {
                    try {
                        winDisplay(p.player2, p.player1);
                        ++p.player2.kills;
                        p.player2.heal = healing(p.player2);
                        p.player2.life += p.player2.heal;

                        p.player1.alive = false;
                        p.player1.fightMode = false;
                        p.player1.life = 0;
                        p.player2.fightMode = false;


                        squadAliveStatus(p.player1.squadId);

                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalMatches++;
                    break;
                } else if (p.player2.life <= 0 && p.player1.life > 0) {
                    try {
                        winDisplay(p.player1, p.player2);
                        ++p.player1.kills;
                        p.player1.heal = healing(p.player1);
                        p.player1.life += p.player1.heal;


                        p.player1.fightMode = false;
                        p.player2.alive = false;
                        p.player2.fightMode = false;
                        p.player2.life = 0;

                        squadAliveStatus(p.player2.squadId);

                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalMatches++;
                    break;
                }
            } while ((p.player1.life > 0) || (p.player2.life > 0));
        } catch (Exception e) {
            System.out.println("Fight Error : " + e.getMessage());
        }
    }

    private Pair checkingPlayers() {
        Player p1 = null, p2 = null;
        int sqid = 0;
        outer:
        for (Squad aMySquad : mySquad) {
            if (aMySquad.aliveSquad) {
                for (int j = 0; j < aMySquad.squadMembers.length; j++) {
                    if (aMySquad.squadMembers[j].alive && !aMySquad.squadMembers[j].fightMode) {
                        p1 = aMySquad.squadMembers[j];
                        sqid = aMySquad.squadMembers[j].squadId - 1;
                        break outer;
                    }
                }
            }
        }
        outer1:
        for (int i = 0; i < mySquad.length; i++) {
            if (mySquad[i].aliveSquad) {
                if (i == sqid && p1 != null) {
                    continue;
                }
                for (int j = 0; j < mySquad[i].squadMembers.length; j++) {
                    if (mySquad[i].squadMembers[j].alive && !mySquad[i].squadMembers[j].fightMode) {
                        p2 = mySquad[i].squadMembers[j];
                        break outer1;
                    }
                }
            }
        }
        return new Pair(p1, p2);
    }

    private void squadCreator() {
        int newRandom, countPlayer = 0;
        int MAX_PLAYER_LIMIT = 100;
        do {
            if (counter >= 0) {
                this.mySquad = Arrays.copyOf(mySquad, (mySquad.length + 1));
            }
            do {
                newRandom = random.nextInt(4) + 1;
            } while (countPlayer + newRandom > MAX_PLAYER_LIMIT);
            countPlayer += newRandom;
            mySquad[++counter] = new Squad((counter + 1), "squad-" + (counter + 1), newRandom);

        } while (countPlayer < MAX_PLAYER_LIMIT);
    }

    private void squadAliveStatus(int squadId) {
        int counterDead = 0;
        for (Player p : mySquad[squadId - 1].squadMembers) {
            if (!p.alive) {
                counterDead++;
            }
        }
        if (counterDead == mySquad[squadId - 1].squadMembers.length) {
            mySquad[squadId - 1].squadAliveToogle();
        }
    }

    @Override
    public void run() {
        Pair p;
        try {
            if (!win()) {
                p = checkingPlayers();
                if (p.player1 != null && p.player2 != null && p.player1.alive && p.player2.alive && !p.player1.fightMode && !p.player2.fightMode) {

                    p.player1.fightMode = true;
                    p.player2.fightMode = true;

                    fight(p);
                    Thread.sleep(1000);
                }
            } else {
                gameObject.gameplay = false;
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setGameObject(PUBG gameObject) {
        this.gameObject = gameObject;
    }

    boolean getGameStatus() {
        return gameObject.gameplay;
    }

    private void updateMvp() {
        for (Squad value : mySquad) {
            int maxKills = 0;
            for (int j = 0; j < value.squadMembers.length; j++) {
                if (value.squadMembers[j].kills >= maxKills) {
                    maxKills = value.squadMembers[j].kills;
                }
            }
            for (int j = 0; j < value.squadMembers.length; j++) {
                try {
                    if (value.squadMembers[j].kills == maxKills) {
                        if (counter_ >= 0) {
                            this.forMvpPlayers = Arrays.copyOf(forMvpPlayers, forMvpPlayers.length + 1);
                        }
                        forMvpPlayers[++counter_] = value.squadMembers[j];
                    }
                } catch (Exception e) {
                    System.out.println(Arrays.toString(forMvpPlayers));
                    System.out.println("Error1 :" + e.getMessage());
                }
            }

            try {
                if (counter_ > 0) {
                    damageBasedMvp();
                } else {
                    forMvpPlayers[0].mvp = "M.V.P";
                }
            } catch (Exception e) {
                System.out.println("Error2 : " + e.getMessage());
            }
            counter_ = -1;
            forMvpPlayersReset();
        }
    }

    private void damageBasedMvp() {
        int maxDamage = 0;
        for (Player aP1 : forMvpPlayers) {
            if (aP1.damage >= maxDamage) {
                maxDamage = aP1.damage;
            }
        }
        for (Player aP1 : forMvpPlayers) {
            if (aP1.damage == maxDamage) {
                aP1.mvp = "M.V.P";
                break;
            }
        }
    }

    private void forMvpPlayersReset() {
        if (forMvpPlayers[0] != null) {
            forMvpPlayers = null;
            forMvpPlayers = new Player[1];
        }
    }

    private static class Pair {
        private Player player1, player2;

        Pair(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }
    }

    static class Squad {
        private int squadId;
        private String squadName;
        private Player[] squadMembers;
        private boolean aliveSquad = true;

        Squad(int squadId, String squadName, int squadMembers) {
            this.squadId = squadId;
            this.squadName = squadName;
            this.playerCreator(squadMembers);
        }

        private void playerCreator(int squadMembersLength) {
            this.squadMembers = new Player[squadMembersLength];
            for (int i = 0; i < squadMembersLength; i++) {
                this.squadMembers[i] = new Player(i + 1, this.squadName + " player-" + (i + 1), this.squadId);
            }
        }

        private void squadAliveToogle() {
            aliveSquad = !aliveSquad;
        }

        @Override
        public String toString() {
            return "Squad[" +
                    "squadId=" + squadId +
                    ", squadName='" + squadName + '\'' +
                    ", squadLength " + squadMembers.length +
                    ",\naliveSquad=" + aliveSquad +
                    ",\nsquadMembers=\n" + Arrays.toString(squadMembers) +
                    ']' + "\n";
        }
    }

    static class Player {
        private static int counterId = 1;
        private final int uId;
        private boolean alive = true;
        private int id;
        private String name;
        private int life = 100;
        private boolean fightMode = false;
        private int squadId;
        private int kills;
        private int heal;
        private int damage = 0;
        private String mvp = "";

        Player(int id, String name, int squadId) {
            this.id = id;
            this.name = name;
            this.squadId = squadId;
            this.uId = counterId++;
        }

        int getLife() {
            return life;
        }

        int playerLifeStatus() {
            return (life <= 0) ? 0 : this.life;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "alive=" + alive +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", life=" + playerLifeStatus() +
                    ", fightMode=" + fightMode +
                    ", squadId=" + squadId +
                    ",unique id=" + this.uId +
                    '}' + "\n";
        }
    }
}
