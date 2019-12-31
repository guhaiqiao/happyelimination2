package elimination;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static void print(String s, int flag) {
        if (flag == 1)
            System.out.println(s);
        else
            System.out.print(s);
    }

    public static int[][] readMap(String path) throws NumberFormatException, IndexOutOfBoundsException {
        int[][] map;
        int row = 0, col = 0;
        List<String[]> lines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line;
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                lines.add(line.split(" "));
            }
            br.close();
        } catch (IOException e) {
            print("No such file", 1);
        }
        row = lines.size() - 7;
        col = lines.get(0).length;

        map = new int[row][col];
        for (int i = 0; i < row - 1; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = Integer.parseInt(lines.get(i)[j]);
            }
        }
        for (int i = 0; i < 8; i++)
            map[row - 1][i] = Integer.parseInt(lines.get(row + i - 1)[0]);
        // printDoubleIntArray(map);
        return map;

    }

    public static int[][] readScore(String path) throws NumberFormatException, IndexOutOfBoundsException {
        int[][] score;
        int row = 3, col = 3;
        List<String[]> lines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.split(" "));
            }
            br.close();
        } catch (IOException e) {
            print("No such file", 1);
        }

        score = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                score[i][j] = Integer.parseInt(lines.get(i)[j]);
            }
        }
        return score;

    }

    public static void write(String path, int[][] data) {
        try {
            File file = new File(path);
            Writer out = new FileWriter(file);
            int row = data.length;
            int col = data[0].length;
            for (int i = 0; i < row; i++) {

                for (int j = 0; j < col; j++) {
                    out.write(data[i][j] + " ");
                }
                out.write("\n");
            }
            out.close();
        } catch (Exception e) {
        }
    }

    public static void printDoubleIntArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++)
                print(a[i][j] + " ", 0);
            print("", 1);
        }
    }

    public static void main(String[] args) {
        Functions.readScore("resource/score.txt");
    }
}