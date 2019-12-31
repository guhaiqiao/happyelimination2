package elimination;

import java.util.Scanner;

public class Game {
    private int[][] map;
    private ChessBoard board;
    private int step = 10;

    Game() {
        this.map = Functions.readMap("resource/map1.txt");
        this.board = new ChessBoard(map);
    }

    Game(String mapfile) {
        this.map = Functions.readMap(mapfile);
        this.board = new ChessBoard(map);
    }

    public int getStep() {
        return step;
    }

    public boolean swap(int i, int j) {
        if (board.swapWithPlate(i, j)) {
            System.out.println("swap");
            board.findSameFood(i, j);
            board.printSameCol();
            board.printSameRow();
            if (board.getChess(i, j).canEliminate()) {
                System.out.println("succeed");
                if (step > 0)
                    step--;
                board.eliminateChess(i, j);
                board.printBoard();
                board.fall();
                board.printBoard();
                board.eliminate();
                return true;
            }
        }
        System.out.println("fail to exchange");
        board.swapWithPlate(i, j); // swap back when fail
        return false;
    }

    public void reduceStep() {
        if (step > 0)
            step--;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public boolean checkGame() {
        return board.check() && (step > 0);
    }

    public boolean checkSucceed() {
        for (int i = 0; i < 6; i++) {
            if (board.getGoal()[i] != 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Game game = new Game("resource/map/map2.txt");
        Scanner s = new Scanner(System.in);
        int x, y;
        game.board.printBoard();
        while (true) {
            x = s.nextInt();
            y = s.nextInt();
            game.swap(x, y);
            // if (game.checkGame()) {
            // System.out.println("You Win");
            // break;
            // }
            if (!game.board.check()) {
                System.out.println("Game Over");
                break;
            }
        }
        s.close();
    }
}