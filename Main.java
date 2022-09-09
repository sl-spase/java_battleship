package battleship;

import java.io.IOException;
import java.util.*;

public class Main {
    static String[][] fieldPlayer1 = new String[11][11];
    static String[][] fogFieldPlayer1 = new String[11][11];
    static String[][] fieldPlayer2 = new String[11][11];
    static String[][] fogFieldPlayer2 = new String[11][11];
    static Map<String, String> mapLetterToNumber = Map.of("A", "1", "B", "2", "C", "3", "D", "4", "E", "5", "F", "6", "G", "7", "H", "8", "I", "9", "J", "10");
    static int hitCountPlayer1 = 0;
    static int hitCountPlayer2 = 0;
    static boolean playerOne = true;
    static boolean isGame = true;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        drawField(fieldPlayer1, true);
        drawField(fogFieldPlayer1, false);
        fillField(fieldPlayer1);
        drawField(fieldPlayer2, true);
        drawField(fogFieldPlayer2, false);
        fillField(fieldPlayer2);
        turn();
    }

    public static void turn() {
        while (isGame) {

            if (playerOne) {
                printField(fogFieldPlayer2);
                System.out.println("---------------------");
                printField(fieldPlayer1);
                System.out.println("Player 1, it's your turn:");
                checkShot(fogFieldPlayer2, fieldPlayer2);

            } else {
                printField(fogFieldPlayer1);
                System.out.println("---------------------");
                printField(fieldPlayer2);
                System.out.println("Player 2, it's your turn:");
                checkShot(fogFieldPlayer1, fieldPlayer1);
            }

            promptEnterKey();
        }

    }

    public static void fillField(String[][] field) {
        if (playerOne) {
            System.out.println("Player 1, place your ships on the game field\n");
        } else {
            System.out.println("Player 2, place your ships on the game field\n");
        }

        int size = 5;
        System.out.printf("Enter the coordinates of the Aircraft Carrier (%d cells):\n\n", size);
        String[] coordinate1 = sc.nextLine().split("\\s+");
        drawShips(coordinate1, size, field);

        System.out.printf("Enter the coordinates of the Battleship (%d cells):\n\n", --size);
        String[] coordinate2 = sc.nextLine().split("\\s+");
        drawShips(coordinate2, size, field);

        System.out.printf("Enter the coordinates of the Submarine (%d cells):\n\n", --size);
        String[] coordinate3 = sc.nextLine().split("\\s+");
        drawShips(coordinate3, size, field);

        System.out.printf("Enter the coordinates of the Cruiser (%d cells):\n\n", size);
        String[] coordinate4 = sc.nextLine().split("\\s+");
        drawShips(coordinate4, size, field);

        System.out.printf("Enter the coordinates of the Destroyer (%d cells):\n\n", --size);
        String[] coordinate5 = sc.nextLine().split("\\s+");
        drawShips(coordinate5, size, field);
        promptEnterKey();

    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
            playerOne = !playerOne;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkShot(String[][] fogField, String[][] field) {
            String[] shotCoordinate = sc.nextLine().split("", 2);
            String letter = mapLetterToNumber.get(shotCoordinate[0]);
            String number = shotCoordinate[1];

            if (!mapLetterToNumber.containsKey(shotCoordinate[0]) || Integer.parseInt(number) > 10) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                checkShot(fogField, field);
                System.out.println();
                return;
            }

            if (field[Integer.parseInt(letter)][Integer.parseInt(number)].equals("O")) {
                if((Integer.parseInt(number) - 1 > 0 && Objects.equals(field[Integer.parseInt(letter)][Integer.parseInt(number) - 1], "O"))
                 || (Integer.parseInt(letter) - 1 > 0 && Objects.equals(field[Integer.parseInt(letter) - 1][Integer.parseInt(number)], "O"))
                 || (Integer.parseInt(number) + 1 < 10 && Objects.equals(field[Integer.parseInt(letter)][Integer.parseInt(number) + 1], "O"))
                 || (Integer.parseInt(letter) + 1 < 10 && Objects.equals(field[Integer.parseInt(letter) + 1][Integer.parseInt(number)], "O"))) {
                    System.out.println("You hit a ship!\n");

            } else {
                    System.out.println("You sank a ship!\n");
                }
                fogField[Integer.parseInt(letter)][Integer.parseInt(number)] = "X";
                field[Integer.parseInt(letter)][Integer.parseInt(number)] = "X";
                if (playerOne) {
                    hitCountPlayer1++;
                } else {
                    hitCountPlayer2++;
                }

                if (hitCountPlayer1 == 17 || hitCountPlayer2 == 17) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    isGame = false;
                }

            } else {
                if (field[Integer.parseInt(letter)][Integer.parseInt(number)].equals("X")) {
                    System.out.println("You hit a ship!\n");
                } else {
                    System.out.println("You missed!\n");
                    fogField[Integer.parseInt(letter)][Integer.parseInt(number)] = "M";
                }
            }

    }

    public static void drawShips(String[] coordinate, int size, String[][] field) {

        String[] cFromA = coordinate[0].split("", 2);
        String aLetter = mapLetterToNumber.get(cFromA[0]);
        String aNumber = cFromA[1];

        String[] cFromB = coordinate[1].split("", 2);
        String bLetter = mapLetterToNumber.get(cFromB[0]);
        String bNumber = cFromB[1];

        if (Integer.parseInt(aLetter) > Integer.parseInt(bLetter)){
            String temp = bLetter;
            bLetter = aLetter;
            aLetter = temp;
        }

        if (Integer.parseInt(aNumber) > Integer.parseInt(bNumber)){
            String temp = bNumber;
            bNumber = aNumber;
            aNumber = temp;
        }

        if (!Objects.equals(aLetter, bLetter) && !Objects.equals(aNumber, bNumber)) {
            System.out.println("\nError! Wrong ship location! Try again:");
            String[] coordinateRe = sc.nextLine().split("\\s+");
            drawShips(coordinateRe, size, field);
            System.out.println();
            return;
        }

        if ((Objects.equals(aLetter, bLetter) && Integer.parseInt(bNumber) - Integer.parseInt(aNumber) != size - 1)
            || (Objects.equals(aNumber, bNumber) && Integer.parseInt(bLetter) - Integer.parseInt(aLetter) != size - 1)) {
            System.out.println("\nError! Wrong length of the Submarine! Try again:");
            String[] coordinateRe = sc.nextLine().split("\\s+");
            drawShips(coordinateRe, size, field);
            System.out.println();
            return;
        }

        if (!checkPosition(aLetter, aNumber, bLetter, bNumber, field)) {
            System.out.println("\nError! You placed it too close to another one. Try again:");
            String[] coordinateRe = sc.nextLine().split("\\s+");
            drawShips(coordinateRe, size, field);
            System.out.println();
            return;
        }

        if (Objects.equals(aLetter, bLetter)) {
            for (int i = Integer.parseInt(aNumber); i <= Integer.parseInt(bNumber); i++) {
                field[Integer.parseInt(bLetter)][i] = "O";
            }
        }

        if (Objects.equals(aNumber, bNumber)) {
            for (int i = Integer.parseInt(aLetter); i <= Integer.parseInt(bLetter); i++) {
                field[i][Integer.parseInt(aNumber)] = "O";
            }
        }

        System.out.println();
        printField(field);
    }

    public static boolean checkPosition(String aLetter, String aNumber, String bLetter, String bNumber, String[][] field) {
        boolean isGoodPosition = true;
        if (Objects.equals(aLetter, bLetter)){
            for (int i = Integer.parseInt(aNumber); i <= Integer.parseInt(bNumber); i++) {
                if (!isFilledAt(Integer.parseInt(bLetter), i, field)) {
                    isGoodPosition = false;
                }

            }
        }

        if (Objects.equals(aNumber, bNumber)) {
            for (int i = Integer.parseInt(aLetter); i <= Integer.parseInt(bLetter); i++) {
                if (!isFilledAt(i, Integer.parseInt(aNumber), field)) {
                    isGoodPosition = false;
                }
            }
        }
        return isGoodPosition;
    }

    public static boolean isFilledAt(int row, int col, String[][] field)  {
        if (row > 0 && row < 10 && col > 0 && col < 10) {
           if (field[row-1][col-1].equals("O")) {
               return false;
           }
           if (field[row-1][col].equals("O")) {
               return false;
           }
           if (field[row-1][col+1].equals("O")) {
               return false;
           }
           if (field[row][col+1].equals("O")) {
               return false;
           }
           if (field[row+1][col+1].equals("O")) {
               return false;
           }
           if (field[row+1][col].equals("O")) {
               return false;
           }
           if (field[row+1][col-1].equals("O")) {
               return false;
           }
           if (field[row][col-1].equals("O")) {
               return false;
           }

        }
        return true;
    }



    public static void drawField(String[][] field, boolean needPrint) {
        String[] charArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int firstRowCount = 1;
        int rosLetterCount = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    field[i][j] = " ";
                    continue;
                }

                if (i == 0) {
                    field[i][j] = String.valueOf(firstRowCount);
                    firstRowCount++;
                    continue;
                }

                if (j == 0) {
                    field[i][j] = charArr[rosLetterCount];
                    rosLetterCount++;
                } else {
                    field[i][j] = "~";
                }
            }
        }
        if (needPrint) {
            printField(field);
        }

    }
    private static void printField(String[][] field) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
//                System.out.printf("%2s", field[i][j]);
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }
}
