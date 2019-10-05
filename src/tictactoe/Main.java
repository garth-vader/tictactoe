package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Main.menu(scanner);
        }
    }

    public static void menu(Scanner scanner) {
        boolean exit = false;
        String[] command;

        do {
            System.out.print("Input command: ");
            command = scanner.nextLine().split(" ");

            switch (command[0]) {
            case "start":
                if (command.length == 3) {
                    String xPlayer = command[1];
                    String oPlayer = command[2];
                    if (xPlayer.equals("user") || xPlayer.equals("easy") || xPlayer.equals("medium")
                            || xPlayer.equals("hard") && oPlayer.equals("user") || oPlayer.equals("easy")
                            || oPlayer.equals("medium") || oPlayer.equals("hard")) {
                        Main.playGame(xPlayer, oPlayer, scanner);
                    } else {
                        System.out.println("Bad parameters!");
                    }
                } else {
                    System.out.println("Bad parameters!");
                }
                break;
            case "exit":
                exit = true;
                break;
            default:
                System.out.println("Bad parameters!");
            }
        } while (!exit);
    }

    public static void playGame(String xPlayer, String oPlayer, Scanner scanner) {
        Main game = new Main();
        boolean xTurn = true;
        game.printGame();
        while (!game.isFinished) {
            if (xTurn) {
                if (xPlayer.equals("player")) {
                    game.promptPlayerMove('X', scanner);
                } else if (xPlayer.equals("easy")) {
                    System.out.println("Making move level \"easy\"");
                    game.makeEasyCPUMove('X');
                } else if (xPlayer.equals("medium")) {
                    System.out.println("Making move level \"medium\"");
                    game.makeMediumCPUMove('X');
                } else {
                    System.out.println("Making move level \"hard\"");
                    game.makeHardCPUMove('X');
                }
                xTurn = false;
            } else {
                if (oPlayer.equals("player")) {
                    game.promptPlayerMove('O', scanner);
                } else if (oPlayer.equals("easy")) {
                    System.out.println("Making move level \"easy\"");
                    game.makeEasyCPUMove('O');
                } else if (oPlayer.equals("medium")) {
                    System.out.println("Making move level \"medium\"");
                    game.makeMediumCPUMove('O');
                } else {
                    System.out.println("Making move level \"hard\"");
                    game.makeHardCPUMove('O');
                }
                xTurn = true;
            }
            game.printGame();
        }
        System.out.println(game.result);
    }

    private char[][] field;
    private int score;
    public boolean isFinished;
    public String result;
    private int[] lastMove;
    private char winner;

    Main(char[][] field) {
        this.isFinished = false;
        char[][] nfield = new char[field.length][field.length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                nfield[i][j] = field[i][j];
            }
        }
        this.field = nfield;
        this.lastMove = new int[2];
    }

    Main() {

        this.isFinished = false;
        this.field = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.field[i][j] = ' ';
            }
        }
        this.result = "Game not finished";
        this.lastMove = new int[2];
    }

    void move(int x, int y, char player) {
        if (field[3 - y][x - 1] != ' ') {
            throw new IllegalArgumentException();
        }
        field[3 - y][x - 1] = player;
        checkResult();
    }

    void moveCpu(int x, int y, char player) {
        if (field[x][y] != ' ') {
            throw new IllegalArgumentException();
        }
        field[x][y] = player;
        lastMove[0] = x;
        lastMove[1] = y;
        checkResult();
    }

    void makeEasyCPUMove(char player) {
        boolean isInvalid = true;
        Random random = new Random();
        do {
            try {
                move(random.nextInt(3 - 1 + 1) + 1, random.nextInt(3 - 1 + 1) + 1, player);
                isInvalid = false;
            } catch (Exception e) {
                isInvalid = true;
            }
        } while (isInvalid);
    }

    void makeMediumCPUMove(char player) {
        char opPlayer;
        if (player == 'X') {
            opPlayer = 'O';
        } else {
            opPlayer = 'X';
        }
        int[] pWin = findWinning(player);
        int[] oWin = findWinning(opPlayer);
        if (pWin[0] != -1) {
            moveCpu(pWin[0], pWin[1], player);
        } else if (oWin[0] != -1) {
            moveCpu(oWin[0], oWin[1], player);
        } else {
            makeEasyCPUMove(player);
        }
    }

    void makeHardCPUMove(char player) {
        Main m = Main.miniMax(field, player, player);
        moveCpu(m.lastMove[0], m.lastMove[1], player);

    }

    private static Main miniMax(char[][] newBoard, char player, char curPlayer) {
        char opPlayer;
        if (player == 'X') {
            opPlayer = 'O';
        } else {
            opPlayer = 'X';
        }

        Main currentBoard = new Main(newBoard);
        currentBoard.checkResult();
        if (currentBoard.isFinished) {
            if (player == currentBoard.winner) {
                currentBoard.score = 10;
            } else if (opPlayer == currentBoard.winner) {
                currentBoard.score = -10;
            } else {
                currentBoard.score = 0;
            }
            return currentBoard;
        }

        // field filled each empty spots filled
        Main[] emptySpotsTaken = Main.emptySpots(newBoard, curPlayer);
        for (int i = 0; i < emptySpotsTaken.length; i++) {
            if (curPlayer == player) {
                Main m = miniMax(emptySpotsTaken[i].field, player, opPlayer);
                emptySpotsTaken[i].score = m.score;
            } else {
                Main m = miniMax(emptySpotsTaken[i].field, player, player);
                emptySpotsTaken[i].score = m.score;
            }
        }

        int bestMove = 0;
        if (curPlayer == player) {
            int bestScore = -10000;
            for (int i = 0; i < emptySpotsTaken.length; i++) {
                if (emptySpotsTaken[i].score > bestScore) {
                    bestScore = emptySpotsTaken[i].score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < emptySpotsTaken.length; i++) {
                if (emptySpotsTaken[i].score < bestScore) {
                    bestScore = emptySpotsTaken[i].score;
                    bestMove = i;
                }
            }
        }
        return emptySpotsTaken[bestMove];
    }

    private static Main[] emptySpots(char[][] board, char player) {
        int[][] spots = new int[9][2];
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    spots[count][0] = i;
                    spots[count][1] = j;
                    count++;
                }
            }
        }
        Main[] result = new Main[count];
        for (int i = 0; i < count; i++) {
            Main m = new Main(board);
            m.moveCpu(spots[i][0], spots[i][1], player);
            result[i] = m;

        }
        return result;
    }

    private int[] findWinning(char player) {

        // Check for horizontal moves
        int nPlayer = 0;
        int nSpace = 0;
        for (int i = 0; i < 3; i++) {
            nPlayer = 0;
            nSpace = 0;
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == player) {
                    nPlayer++;
                } else if (field[i][j] == ' ') {
                    nSpace++;
                }
            }
            if (nPlayer == 2 && nSpace == 1) {
                int j = 0;
                while (field[i][j] != ' ') {
                    j++;
                }
                return new int[] { i, j };
            }
        }

        // Check for vertical moves
        for (int j = 0; j < 3; j++) {
            nPlayer = 0;
            nSpace = 0;
            for (int i = 0; i < 3; i++) {
                if (field[i][j] == player) {
                    nPlayer++;
                } else if (field[i][j] == ' ') {
                    nSpace++;
                }
            }
            if (nPlayer == 2 && nSpace == 1) {
                int i = 0;
                while (field[i][j] != ' ') {
                    i++;
                }
                return new int[] { i, j };
            }
        }

        // check for cross moves
        nPlayer = 0;
        nSpace = 0;
        for (int i = 0; i < 3; i++) {
            if (field[i][i] == player) {
                nPlayer++;
            } else if (field[i][i] == ' ') {
                nSpace++;
            }
        }
        if (nPlayer == 2 && nSpace == 1) {
            int i = 0;
            while (field[i][i] != ' ') {
                i++;
            }
            return new int[] { i, i };
        }

        nPlayer = 0;
        nSpace = 0;
        for (int i = 0; i < 3; i++) {
            if (field[i][2 - i] == player) {
                nPlayer++;
            } else if (field[i][2 - i] == ' ') {
                nSpace++;
            }
        }
        if (nPlayer == 2 && nSpace == 1) {
            int i = 0;
            while (field[i][2 - i] != ' ') {
                i++;
            }
            return new int[] { i, 2 - i };
        }

        return new int[] { -1, -1 };
    }

    public void promptPlayerMove(char player, Scanner scanner) {
        boolean isInvalid = true;
        do {
            System.out.print("Enter the coordinates: ");
            try {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                if (x < 1 || x > 3 || y < 1 || y > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                }
                move(x, y, player);
                isInvalid = false;
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
            } catch (Exception e) {
                System.out.println("This cell is occupied! Choose another one!");
            }
        } while (isInvalid);
    }

    public void checkResult() {
        char[] game = toCharArray(field);
        int xCount = 0;
        int oCount = 0;
        int spaceCount = 0;
        for (char c : game) {
            if (c == 'X') {
                xCount += 1;
            } else if (c == 'O') {
                oCount += 1;
            } else {
                spaceCount += 1;
            }
        }

        // Check for horizontal winners
        int xWins = 0;
        int oWins = 0;
        for (int i = 0; i < 3; i++) {
            if (field[i][0] != ' ') {
                char player = field[i][0];
                if (player == field[i][1] && player == field[i][2]) {
                    if (player == 'X') {
                        xWins += 1;
                    } else if (player == 'O') {
                        oWins += 1;
                    }
                }
            }
        }
        // check for vertical winners
        for (int j = 0; j < 3; j++) {
            if (field[0][j] != ' ') {
                char player = field[0][j];
                if (player == field[1][j] && player == field[2][j]) {
                    if (player == 'X') {
                        xWins += 1;
                    } else if (player == 'O') {
                        oWins += 1;
                    }
                }
            }
        }
        // check for cross winners
        char player = field[0][0];
        if (player == field[1][1] && player == field[2][2]) {
            if (player == 'X') {
                xWins += 1;
            } else if (player == 'O') {
                oWins += 1;
            }
        }
        player = field[0][2];
        if (player == field[1][1] && player == field[2][0]) {
            if (player == 'X') {
                xWins += 1;
            } else if (player == 'O') {
                oWins += 1;
            }
        }

        if (Math.abs(xCount - oCount) > 1 || xWins + oWins > 1 && spaceCount != 9) {
            result = "Impossible";
            isFinished = true;
        } else if (xWins == 1) {
            result = "X wins";
            isFinished = true;
            winner = 'X';
        } else if (oWins == 1) {
            result = "O wins";
            isFinished = true;
            winner = 'O';
        } else if (spaceCount == 0) {
            result = "Draw";
            isFinished = true;
            winner = 'D';
        } else {
            result = "Game not finished";
            isFinished = false;
        }
    }

    public static char[] toCharArray(char[][] matrix) {
        char[] game = new char[9];
        int x = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game[x] = matrix[i][j];
                x++;
            }
        }
        return game;
    }

    public void printGame() {
        char[] game = toCharArray(field);
        System.out.println("---------");
        for (int i = 0; i < 9;) {
            System.out.print("| ");
            for (int j = i + 3; i < j; i++) {
                System.out.print(game[i] + " ");
            }
            System.out.print("|\n");
        }
        System.out.println("---------");
    }
}
