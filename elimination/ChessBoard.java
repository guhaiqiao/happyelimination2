package elimination;

import java.util.Random;

public class ChessBoard {
    private int row, col;
    private Chess[][] Board;
    private Chess plate;
    private int score = 0;
    private int totalTime;
    private int[] goal;
    private Random r = new Random();
    private int[][] checkRowState;
    private int[][] checkColState;
    private int[][] iceCheckState;
    private int[] flagState;
    protected int x, y;
    private int step;

    ChessBoard(int[][] map) {
        if (map != null) {
            this.row = map.length - 1;
            this.col = map[0].length;
            goal = new int[6];
            setGoal(map[row]);
            Board = new Chess[row][col];
            checkRowState = new int[row][col];
            checkColState = new int[row][col];
            iceCheckState = new int[row][col];
            flagState = new int[6];
            plate = new Food(r.nextInt(6), 0);
            initBoard(map);
            while (!check()) {
                initBoard(map);
            }
            // printBoard();

        }
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setGoal(int[] set) {
        step = set[0];
        for (int i = 0; i < 6; i++) {
            goal[i] = set[i + 1];
        }
        totalTime = set[7];
        System.out.println("step: " + step);
        System.out.println("goal: " + goal[0] + " " + goal[1] + " " + goal[2] + " " + goal[3] + " " + goal[4] + " "
                + goal[5] + " ");
        System.out.println("totalTime: " + totalTime);
    }

    public int getStep() {
        return step;
    }

    public int[] getGoal() {
        return goal;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int s) {
        score += s;
    }

    public Chess getChess(int i, int j) {
        if (checkPosi(i, j))
            return Board[i][j];
        else
            return null;
    }

    public Chess getPlate() {
        return plate;
    }

    public void clearCheckState() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                checkRowState[i][j] = 0;
                checkColState[i][j] = 0;
                iceCheckState[i][j] = 0;
            }
        }
    }

    public void clearFlagState() {
        for (int i = 0; i < 6; i++) {
            flagState[i] = 0;
        }
    }

    public void printBoard() {
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(Board[i][j].getShape() + " ");
            }
            System.out.println();
        }
        Functions.print("plate: " + plate.getShape(), 1);
    }

    public void printSameRow() {
        System.out.println("HorizontalSame");
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(Board[i][j].getHorizontalSame() + " ");
            }
            System.out.println();
        }
        Functions.print("plate: " + plate.getShape(), 1);
    }

    public void printSameCol() {
        System.out.println("Vertical");
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(Board[i][j].getVerticalSame() + " ");
            }
            System.out.println();
        }
        Functions.print("plate: " + plate.getShape(), 1);
    }

    public void printState() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (Board[i][j].isCleanable())
                    System.out.print(Board[i][j].isExist() + "\t");
                else {
                    System.out.print("None\t");
                }
            }
            System.out.println();
        }
        Functions.print("plate: " + plate.getShape(), 1);
    }

    public boolean swapWithPlate(int i, int j) {
        if (checkPosi(i, j)) {
            if (Board[i][j].isExist() && Board[i][j].isOperable()) {
                Chess c = plate;
                plate = Board[i][j];
                Board[i][j] = c;
                checkColState[i][j] = 0;
                checkRowState[i][j] = 0;
                x = i;
                y = j;
                return true;
            }
        }
        return false;
    }

    public boolean swap(int i1, int j1, int i2, int j2) {
        if (Board[i1][j1].isOperable() && Board[i2][j2].isOperable()) {
            Chess temp = Board[i1][j1];
            Board[i1][j1] = Board[i2][j2];
            Board[i2][j2] = temp;
            return true;
        }
        return false;
    }

    public boolean checkPosi(int i, int j) {
        if (i >= 0 && i < row && j >= 0 && j < col)
            return true;
        else
            return false;
    }

    public boolean checkSucceed() {
        for (int i = 0; i < 6; i++) {
            if (goal[i] != 0)
                return false;
        }
        return true;
    }

    public void reduceGoal(int kind) {
        if (goal[kind] > 0)
            goal[kind]--;
    }

    public boolean check() {
        clearFlagState();
        boolean flag = true;
        while (flag) {
            int flagCount = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (swapWithPlate(i, j)) {
                        findSameFood(i, j);
                        if (Board[i][j].getHorizontalSame() >= 3 || Board[i][j].getVerticalSame() >= 3) {
                            swapWithPlate(i, j);
                            // System.out.println(i + ", " + j);
                            flag = false;
                            break;
                        }
                        swapWithPlate(i, j);
                        clearCheckState();
                    }
                }
            }
            for (int k = 0; k < 6; k++)
                flagCount += flagState[k];
            if (flagCount == 6 && flag)
                return false;
            if (flag) {
                int random = r.nextInt(6);
                while (flagState[random] != 0) {
                    random = r.nextInt(6);
                }
                // System.out.println(random);
                this.plate = new Food(random, 0);
                flagState[random] = 1;
            }
        }
        return true;
    }

    public void findSameFood() {
        clearCheckState();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                findSameFoodRow(i, j);
                findSameFoodCol(i, j);
            }
        }
        // clearCheckState();
    }

    public void findSameFood(int i, int j) {
        clearCheckState();
        findSameFoodCol(i - 2, j);
        findSameFoodCol(i - 1, j);
        findSameFoodRow(i, j - 1);
        findSameFoodRow(i, j - 2);
        findSameFoodCol(i, j);
        findSameFoodRow(i, j);
    }

    public void findSameFoodRow(int i, int j) {
        if (!checkPosi(i, j) || !Board[i][j].isExist() || !Board[i][j].isCleanable() || checkRowState[i][j] == 1)
            return;
        // System.out.println("check row " + i + ", " + j);
        int v = 1;
        int now_x = i, now_y = j;
        int next_x = i, next_y = j + 1;
        checkRowState[now_x][now_y] = 1;
        while (checkPosi(next_x, next_y) && Board[next_x][next_y].isExist()
                && Board[next_x][next_y].compare(Board[now_x][now_y])) {
            checkRowState[next_x][next_y] = 1;
            v++;
            now_y++;
            next_y++;
        }
        for (int k = 0; k < v; k++) {
            Board[i][j + k].setHorizontalSame(v);
            for (int l = 0; l < v; l++) {
                Board[i][j + k].horizontalSameList[l] = j + l;
            }
        }
    }

    public void findSameFoodCol(int i, int j) {
        if (!checkPosi(i, j) || !Board[i][j].isExist() || !Board[i][j].isCleanable() || checkColState[i][j] == 1)
            return;
        // System.out.println("check col " + i + ", " + j);
        int h = 1;
        int now_x = i, now_y = j;
        int next_x = i + 1, next_y = j;
        checkColState[now_x][now_y] = 1;
        while (checkPosi(next_x, next_y) && Board[next_x][next_y].isExist()
                && Board[next_x][next_y].compare(Board[now_x][now_y])) {
            checkColState[next_x][next_y] = 1;
            h++;
            now_x++;
            next_x++;
        }
        for (int k = 0; k < h; k++) {
            Board[i + k][j].setVerticalSame(h);
            for (int l = 0; l < h; l++) {
                Board[i + k][j].verticalSameList[l] = i + l;
            }
        }
    }

    public void initBoard(int[][] map) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] != 1) {
                    int ice = 0;
                    if (map[i][j] == 2)
                        ice = 4;
                    Board[i][j] = new Food(r.nextInt(6), ice);
                    while ((i >= 2 && Board[i - 1][j].compare(Board[i][j]) && Board[i - 2][j].compare(Board[i][j]))
                            || (j >= 2 && Board[i][j - 1].compare(Board[i][j]) && Board[i][j - 2].compare(Board[i][j])))
                        Board[i][j] = new Food(r.nextInt(6), ice);
                    // Board[i][j] = new Food(r.nextInt(6), r.nextInt(4));
                } else {
                    Board[i][j] = new Wall();
                }
                checkRowState[i][j] = 0;
                checkColState[i][j] = 0;
                iceCheckState[i][j] = 0;
            }
        }
        findSameFood();
    }

    public boolean checkIce() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (Board[i][j].getIce() != 0)
                    return false;
            }
        }
        return true;
    }

    public String eliminateChess(int i, int j) {
        Chess c = Board[i][j];
        if (!c.isCleanable())
            return "";

        int h = c.getHorizontalSame();
        int v = c.getVerticalSame();
        // System.out.println("h: " + h + " v: " + v);
        if (c.horizontalE == true && c.verticalE == true)
            return "";
        String s = "";

        if (c.isExist()) {
            c.eliminate(); // eliminate itself
            score += 10;
            if (goal[c.getKind()] > 0)
                goal[c.getKind()]--;
            // System.out.println("eliminate " + i + ", " + j);
        }
        // Functions.print(i + ", " + j, 1);
        if (h >= 3 && v >= 3) {
            Functions.print("boom", 1);
            s += "b ";
            score += 50;
            for (int m = 0; m < 3; m++) {
                for (int n = 0; n < 2 * m + 1; n++) {
                    if (checkPosi(i - 2 + m, j - m + n) && Board[i - 2 + m][j - m + n].isExist()
                            && Board[i - 2 + m][j - m + n].isCleanable()) {
                        Board[i - 2 + m][j - m + n].eliminate();
                        iceCheckState[i - 2 + m][j - m + n] = 1;
                        if (goal[Board[i - 2 + m][j - m + n].getKind()] > 0)
                            goal[Board[i - 2 + m][j - m + n].getKind()]--;
                        score += 10;
                    }
                }
            }
            for (int m = 1; m < 3; m++) {
                for (int n = 0; n < 5 - 2 * m; n++) {
                    if (checkPosi(i + m, j - m + n) && Board[i + m][j - m + n].isExist()
                            && Board[i + m][j - m + n].isCleanable()) {
                        Board[i + m][j - m + n].eliminate();
                        if (goal[Board[i + m][j - m + n].getKind()] > 0)
                            goal[Board[i + m][j - m + n].getKind()]--;
                        score += 10;
                    }
                }
            }
        }
        if (checkPosi(i, j - 1) && Board[i][j - 1].isCleanable() && !Board[i][j - 1].compare(Board[i][j])
                && iceCheckState[i][j - 1] == 0) {
            ((Food) Board[i][j - 1]).reduceIce();
            iceCheckState[i][j - 1] = 1;
        }
        if (checkPosi(i, j + 1) && Board[i][j + 1].isCleanable() && !Board[i][j + 1].compare(Board[i][j])
                && iceCheckState[i][j + 1] == 0) {
            ((Food) Board[i][j + 1]).reduceIce();
            iceCheckState[i][j + 1] = 1;
        }
        if (checkPosi(i - 1, j) && Board[i - 1][j].isCleanable() && !Board[i - 1][j].compare(Board[i][j])
                && iceCheckState[i - 1][j] == 0) {
            ((Food) Board[i - 1][j]).reduceIce();
            iceCheckState[i - 1][j] = 1;
        }
        if (checkPosi(i + 1, j) && Board[i + 1][j].isCleanable() && !Board[i + 1][j].compare(Board[i][j])
                && iceCheckState[i + 1][j] == 0) {
            ((Food) Board[i + 1][j]).reduceIce();
            iceCheckState[i + 1][j] = 1;
        }
        if (c.horizontalE == false) {
            int[] hList = c.horizontalSameList;
            if (h > 1) {
                for (int k = 0; k < h; k++) {
                    Board[i][hList[k]].horizontalE = true;
                    if (hList[k] != j)
                        eliminateChess(i, hList[k]);
                }
            }
            if (h == 5) {
                s += "h5 ";
                Functions.print("horizontal 5!", 1);
                score += 50;
                // eliminate same chess beside it
                for (int m = 0; m < row; m++) {
                    for (int n = 0; n < col; n++) {
                        if (Board[m][n].compare(c) && Board[m][n].isExist()) {
                            Board[m][n].eliminate();
                            score += 10;
                            if (goal[Board[m][n].getKind()] > 0)
                                goal[Board[m][n].getKind()]--;
                        }
                    }
                }
            } else if (h == 4) {
                s += "h4";
                Functions.print("horizontal 4!", 1);
                score += 40;
                int Col = hList[r.nextInt(4)];
                if (i == x && j == y)
                    Col = y;
                for (int m = 0; m < row; m++) {
                    if (Board[m][Col].isCleanable()) {
                        Board[m][Col].eliminate();
                        score += 10;
                        if (goal[Board[m][Col].getKind()] > 0)
                            goal[Board[m][Col].getKind()]--;

                    }
                }
            }
        }
        if (c.verticalE == false) {
            int[] vList = c.verticalSameList;
            if (v > 1) {
                for (int k = 0; k < v; k++) {
                    Board[vList[k]][j].verticalE = true;
                    if (vList[k] != i)
                        eliminateChess(vList[k], j);
                }
            }
            if (v == 5) {
                Functions.print("vertical 5!", 1);
                s += "v5 ";
                score += 50;
                // eliminate same chess beside it
                for (int m = 0; m < row; m++) {
                    for (int n = 0; n < col; n++) {
                        if (Board[m][n].compare(c) && Board[m][n].isExist()) {
                            Board[m][n].eliminate();
                            score += 10;
                            if (goal[Board[m][n].getKind()] > 0)
                                goal[Board[m][n].getKind()]--;
                        }
                    }
                }
            } else if (v == 4) {
                Functions.print("vertical 4!", 1);
                s += "v4 ";
                score += 40;
                int Row = vList[r.nextInt(4)];
                if (i == x && j == y)
                    Row = x;
                for (int n = 0; n < col; n++) {
                    if (Board[Row][n].isCleanable()) {
                        Board[Row][n].eliminate();
                        score += 10;
                        if (goal[Board[Row][n].getKind()] > 0)
                            goal[Board[Row][n].getKind()]--;
                    }
                }
            }
        }
        return s;
    }

    public boolean fallOneRow(int i) {
        boolean flag = false;
        for (int j = 0; j < col; j++) {
            if (!Board[i][j].isExist()) {
                if (i != 0) {
                    if (Board[i - 1][j].isExist() && Board[i - 1][j].isOperable()) {
                        swap(i, j, i - 1, j);
                        flag = true;
                    }
                } else {
                    Board[i][j] = new Food(r.nextInt(6), 0);
                }
            }
        }
        return flag;
    }

    public boolean fallSlide(int i, int j) {
        if (i == 0) {
            Board[i][j] = new Food(r.nextInt(6), 0);
            return true;
        }
        int choose;
        boolean left = checkPosi(i - 1, j - 1) && Board[i - 1][j - 1].isExist() && Board[i - 1][j - 1].isOperable();
        boolean right = checkPosi(i - 1, j + 1) && Board[i - 1][j + 1].isExist() && Board[i - 1][j + 1].isOperable();
        if (!left && !right)
            return false;
        else if (left && !right)
            choose = j - 1;
        else if (right && !left)
            choose = j + 1;
        else
            choose = r.nextInt(2) * 2 - 1 + j;
        swap(i, j, i - 1, choose);
        return true;
    }

    public void fall() {
        for (int loop = 0; loop < 9; loop++) {
            for (int m = 0; m < row; m++) {
                fallOneRow(m);
            }
        }
        boolean fallFlag = true;
        while (fallFlag) {
            fallFlag = false;
            for (int m = 0; m < row; m++)
                for (int n = 0; n < col; n++)
                    if (!getChess(m, n).isExist()) {
                        if (fallSlide(m, n)) {
                            fallFlag = true;
                            for (int loop = 0; loop < 9; loop++) {
                                for (int r = 0; r < row; r++) {
                                    fallOneRow(r);
                                }
                            }
                        } else {
                            continue;
                        }
                    }
        }
    }

    public void eliminate() {
        int flag = 1;
        while (flag == 1) {
            flag = 0;
            findSameFood();
            printSameCol();
            printSameRow();
            for (int m = row - 1; m >= 0; m--) {
                for (int n = 0; n < col; n++) {
                    if (getChess(m, n).canEliminate()) {
                        eliminateChess(m, n);
                        printBoard();
                        flag = 1;
                        fall();
                        printBoard();
                    }
                }
            }
        }
    }
}