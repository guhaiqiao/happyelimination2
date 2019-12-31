package elimination;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener, MouseInputListener {
    public ChessBoard board;
    public int row = 9;
    public int col = 9;
    public Audio bgm = new Audio("resource/music/bgm.wav");
    public Audio countTime = new Audio("resource/music/countTime.wav");
    public GamePanel nowPanel;
    public MainWindow m;
    public int step;
    public int mode;
    // game button
    public JButton btnReturnBack;
    public JButton btnPause;
    // info label
    public JLabel[] goalLabel;
    public JLabel stepLabel;
    public JLabel scoreLabel;
    public JLabel[] toolNumLabel;
    // tool
    public JButton[] toolBtn;
    public ImageIcon[] toolIcon;
    // cursor
    public ImageIcon[] cursor;
    public Cursor NormalCursor, RocketCursor, RainbowCursor;
    // flag
    public int toolFlag = 0; // 0 is None, 1 is rocket, 2 is rainbow
    public boolean stopFlag = false;
    public boolean pauseFlag = false;
    public boolean clickFlag = false;
    public int nowScore = 0;

    public int[] toolNumber = new int[3];

    public MainWindow() {
        toolIcon = new ImageIcon[3];
        toolBtn = new JButton[3];
        cursor = new ImageIcon[3];
        for (int i = 0; i < 3; i++) {
            toolIcon[i] = new ImageIcon("resource/" + Settings.TOOL_ICON[i]);
            cursor[i] = new ImageIcon("resource/cursor/" + Settings.CURSOR_ICON[i]);
        }
        NormalCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor[0].getImage(), new Point(10, 20), "stick");
        RocketCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor[1].getImage(), new Point(10, 20), "stick");
        RainbowCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor[2].getImage(), new Point(10, 20),
                "stick");
        setCursor(NormalCursor);
        setSize(1000, 562);
        setBackground(Color.GRAY);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setContentPane(getStartPanel());
        setVisible(true);
        bgm.loop();
        m = this;
    }

    public static void main(String[] args) {
        MainWindow m = new MainWindow();
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getStartPanel() {
        JPanel startPanel = new BgPanel();
        JButton btnStart = getButton("开始", 670, 450, 90, 45, 25, Color.YELLOW);
        JButton btnScore = getButton("排行榜", 740, 450, 110, 45, 25, Color.YELLOW);
        JButton btnExit = getButton("退出", 830, 450, 90, 45, 25, Color.YELLOW);
        startPanel.add(btnStart);
        startPanel.add(btnScore);
        startPanel.add(btnExit);
        btnStart.addActionListener(this);
        btnScore.addActionListener(this);
        btnExit.addActionListener(this);
        return startPanel;
    }

    public JPanel getScorePanel() {
        JPanel scorePanel = new ScorePanel();
        JButton btnReturn = getButton("返回", 750, 450, 90, 45, 25, Color.YELLOW);
        scorePanel.add(btnReturn);
        btnReturn.addActionListener(this);
        JLabel[] scoreTextLabel = new JLabel[3];
        String[] scoreText = { "简单", "中等", "困难" };
        JLabel[][] scoreLabel = new JLabel[3][3];
        int[][] score = Functions.readScore("resource/score.txt");
        for (int i = 0; i < 3; i++) {
            scoreTextLabel[i] = getLabel(scoreText[i], 370 + i * 110, 80, 60, 30, 25, Color.YELLOW);
            for (int j = 0; j < 3; j++) {
                scoreLabel[i][j] = getLabel(score[i][j] + "", 370 + i * 110, 150 + j * 50, 60, 30, 20, Color.YELLOW);
                scorePanel.add(scoreLabel[i][j]);
            }
            scorePanel.add(scoreTextLabel[i]);
        }
        return scorePanel;
    }

    public JPanel getChoosePanel() {
        JPanel ChoosePanel = new BgPanel();
        JButton btnEasy = getButton("简单", 600, 450, 90, 45, 25, Color.YELLOW);
        JButton btnNormal = getButton("中等", 690, 450, 90, 45, 25, Color.YELLOW);
        JButton btnHard = getButton("困难", 780, 450, 90, 45, 25, Color.YELLOW);
        JButton btnReturn = getButton("返回", 870, 450, 90, 45, 25, Color.YELLOW);
        ChoosePanel.add(btnEasy);
        ChoosePanel.add(btnNormal);
        ChoosePanel.add(btnHard);
        ChoosePanel.add(btnReturn);
        btnEasy.addActionListener(this);
        btnNormal.addActionListener(this);
        btnHard.addActionListener(this);
        btnReturn.addActionListener(this);
        return ChoosePanel;
    }

    public JButton getButton(String content, int x, int y, int width, int height, int fontSize, Color fontColor) {
        JButton btn = new JButton(content);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        btn.setForeground(fontColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBounds(x, y, width, height);
        return btn;
    }

    public JButton getButton(ImageIcon img, String text, int x, int y, int width, int height, int fontSize,
            Color fontColor) {
        JButton btn = new JButton(text, img);

        btn.setContentAreaFilled(false);
        btn.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        btn.setForeground(fontColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBounds(x, y, width, height);
        return btn;
    }

    public JLabel getLabel(String content, int x, int y, int width, int height, int fontSize, Color fontColor) {
        JLabel label = new JLabel(content);
        label.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        label.setForeground(fontColor);
        label.setOpaque(false);
        label.setBounds(x, y, width, height);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public GamePanel getGamePanel(String map) {
        stopFlag = false;
        board = new ChessBoard(Functions.readMap("resource/map/" + map));
        step = board.getStep();
        nowScore = 0;
        toolNumLabel = new JLabel[3];
        goalLabel = new JLabel[6];
        int[] goal = board.getGoal();
        GamePanel gamePanel = new GamePanel(board);
        for (int i = 0; i < 3; i++) {
            toolBtn[i] = getButton(toolIcon[i], Settings.TOOL_NAME[i], 700 + i * 80, 440, 50, 50, 0, Color.YELLOW);
            toolNumber[i] = Settings.TOOL_NUM[i];
            toolNumLabel[i] = getLabel(toolNumber[i] + "", 678 + i * 80, 420, 50, 50, 15, Color.BLACK);
            gamePanel.add(toolBtn[i]);
            gamePanel.add(toolNumLabel[i]);
        }
        for (int i = 0; i < 6; i++) {
            goalLabel[i] = getLabel(goal[i] + "", 750 + 115 * (i % 2), 136 + 80 * (i / 2), 60, 60, 25, Color.BLACK);
            gamePanel.add(goalLabel[i]);
        }
        btnPause = getButton("暂停", 725, 495, 90, 45, 25, Color.YELLOW);
        btnReturnBack = getButton("返回", 825, 495, 90, 45, 25, Color.YELLOW);
        JLabel plateText = getLabel("手中挂件：", 700, 370, 150, 50, 25, Color.BLACK);
        JLabel stepText = getLabel("步数", 30, 200, 60, 40, 25, Color.YELLOW);
        stepLabel = getLabel(step + "", 30, 241, 60, 40, 25, Color.YELLOW);
        JLabel scoreText = getLabel("分数", 30, 70, 60, 40, 25, Color.YELLOW);
        scoreLabel = getLabel(nowScore + "", 20, 110, 80, 40, 25, Color.YELLOW);
        gamePanel.add(stepLabel);
        gamePanel.add(stepText);
        gamePanel.add(scoreLabel);
        gamePanel.add(scoreText);
        gamePanel.add(plateText);
        gamePanel.add(btnPause);
        gamePanel.add(btnReturnBack);
        nowPanel = gamePanel;
        return gamePanel;
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("退出")) {
            setVisible(false);
            System.exit(0);
        } else if (s.equals("开始")) {
            setContentPane(getChoosePanel());
        } else if (s.equals("排行榜")) {
            setContentPane(getScorePanel());
        } else if (s.equals("简单")) {
            bgm.stop();
            mode = 0;
            GamePanel gamePanel = getGamePanel("map1.txt");
            setContentPane(gamePanel);
            beginAnimation(gamePanel, "规定时间内完成右侧目标");
        } else if (s.equals("中等")) {
            bgm.stop();
            mode = 1;
            GamePanel gamePanel = getGamePanel("map2.txt");
            setContentPane(gamePanel);
            beginAnimation(gamePanel, "规定时间内消除所有冰块并完成右侧目标");
        } else if (s.equals("困难")) {
            bgm.stop();
            mode = 2;
            GamePanel gamePanel = getGamePanel("map3.txt");
            setContentPane(gamePanel);
            beginAnimation(gamePanel, "规定时间内消除所有冰块并完成右侧目标");
        } else if (s.equals("返回")) {
            stopFlag = true;
            setContentPane(getStartPanel());
        } else if (s.equals("暂停")) {
            pauseFlag = true;
            bgm.stop();
            btnPause.setText("继续");
            removeMouseListener(this);
        } else if (s.equals("继续")) {
            pauseFlag = false;
            bgm.loop();
            btnPause.setText("暂停");
            addMouseListener(this);
        } else if (s.equals("rocket")) {
            if (toolFlag == 1) {
                toolFlag = 0;
                this.setCursor(NormalCursor);
            } else if (toolNumber[0] > 0) {
                toolFlag = 1;
                this.setCursor(RocketCursor);
            }
        } else if (s.equals("rainbow")) {
            if (toolFlag == 2) {
                toolFlag = 0;
                this.setCursor(NormalCursor);
            } else if (toolNumber[1] > 0) {
                toolFlag = 2;
                this.setCursor(RainbowCursor);
            }
        } else if (s.equals("timeAdd")) {
            if (toolNumber[2] > 0) {
                toolNumber[2] -= 1;
                nowPanel.addTime();
            }
            toolNumLabel[2].setText(toolNumber[2] + "");
            repaint();
        }
    }

    public void beginAnimation(GamePanel gamePanel, String text) {
        JLabel s = getLabel(text, 220, 181, 700, 200, 35, Color.BLACK);
        gamePanel.add(s);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    countTime.start(4);
                    Thread.sleep(200);
                    s.setFont(new Font("微软雅黑", Font.PLAIN, 150));
                    s.setBounds(320, 181, 200, 200);
                    for (int i = 3; i > 0; i--) {
                        s.setText(i + "");
                        repaint();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                }
                s.setBounds(275, 181, 300, 200);
                s.setFont(new Font("微软雅黑", Font.PLAIN, 100));
                s.setText("开始");
                repaint();
                try {
                    Thread.sleep(1000);
                    gamePanel.remove(s);
                    repaint();
                    addMouseListener(m);
                    btnReturnBack.addActionListener(m);
                    btnPause.addActionListener(m);
                    for (int i = 0; i < 3; i++)
                        toolBtn[i].addActionListener(m);
                    bgm.loop();
                    while (!stopFlag) {
                        if (nowScore < board.getScore()) {
                            nowScore += 10;
                            scoreLabel.setText(nowScore + "");
                        }
                        repaint();
                        if (!pauseFlag) {
                            if (!gamePanel.reduceTime()) {
                                scoreLabel.setText(board.getScore() + "");
                                repaint();
                                stopFlag = true;
                                bgm.stop();
                                new Audio("resource/music/failure.wav").start(3);
                                new GameEndDialog(m, "消消乐", "时间到了，游戏失败", mode, board.getScore(), "返回主界面");
                            }
                        }
                        Thread.sleep(50);
                    }
                } catch (Exception e) {
                }
            }
        }).start();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        synchronized (m) {
            if (!clickFlag) {
                clickFlag = true;
                removeMouseListener(m);
            } else {
                return;
            }
        }
        int i = (e.getY() - 31) / 55; // vertical is i
        int j = (e.getX() - 125) / 55; // horizontal is j
        if (toolFlag == 1) {
            if (board.checkPosi(i, j)) {
                toolFlag = 0;
                toolNumber[0] -= 1;
                toolNumLabel[0].setText(toolNumber[0] + "");
                repaint();
                m.setCursor(NormalCursor);
                board.addScore(50);
                new Audio("resource/music/all.wav").start(3);
                for (int m = 0; m < row; m++) {
                    if (board.getChess(m, j).isCleanable()) {
                        board.getChess(m, j).eliminate();
                        board.addScore(10);
                        board.reduceGoal(board.getChess(m, j).getKind());
                    }
                }
                for (int n = 0; n < col; n++) {
                    if (n != j) {
                        if (board.getChess(i, n).isCleanable()) {
                            board.getChess(i, n).eliminate();
                            board.addScore(10);
                            board.reduceGoal(board.getChess(i, n).getKind());
                        }
                    }
                }
                for (int k = 0; k < 6; k++) {
                    goalLabel[k].setText(board.getGoal()[k] + "");
                }
            }
        } else if (toolFlag == 2) {
            if (board.checkPosi(i, j)) {
                toolFlag = 0;
                toolNumber[1] -= 1;
                toolNumLabel[1].setText(toolNumber[1] + "");
                repaint();
                m.setCursor(NormalCursor);
                new Audio("resource/music/rainbow.wav").start(2);
                board.addScore(50);
                for (int m = 0; m < row; m++) {
                    for (int n = 0; n < col; n++) {
                        if (board.getChess(m, n).isCleanable() && board.getChess(i, j).compare(board.getChess(m, n))) {
                            board.getChess(m, n).eliminate();
                            board.addScore(10);
                            board.reduceGoal(board.getChess(m, n).getKind());
                        }
                    }
                }
                for (int k = 0; k < 6; k++) {
                    goalLabel[k].setText(board.getGoal()[k] + "");
                }
            }

        } else {
            System.out.println(i + ", " + j);
            if (board.checkPosi(i, j)) {
                board.swapWithPlate(i, j);
                // board.printBoard();
                board.findSameFood();
                if (board.getChess(i, j).isCleanable() && board.getChess(i, j).canEliminate()) {
                    step--;
                    stepLabel.setText(step + "");
                    effectPlay(board.eliminateChess(i, j));
                    // board.printBoard();
                    for (int k = 0; k < 6; k++) {
                        goalLabel[k].setText(board.getGoal()[k] + "");
                    }
                } else {
                    new Audio("resource/music/fail.wav").start(1);
                    board.swapWithPlate(i, j);
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                repaint();
                pauseFlag = true;
                fall();
                eliminate();
                scoreLabel.setText(board.getScore() + "");
                repaint();
                if (board.checkSucceed() && board.checkIce()) {
                    stopFlag = true;
                    bgm.stop();
                    new Audio("resource/music/succeed.wav").start(2);
                    new GameEndDialog(m, "消消乐", "目标达成，恭喜过关", mode, board.getScore(), "返回主界面");
                } else if (!board.check()) {
                    stopFlag = true;
                    bgm.stop();
                    new Audio("resource/music/failure.wav").start(3);
                    new GameEndDialog(m, "消消乐", "无法交换，游戏失败", mode, board.getScore(), "返回主界面");
                } else if (step == 0) {
                    stopFlag = true;
                    bgm.stop();
                    new Audio("resource/music/failure.wav").start(3);
                    new GameEndDialog(m, "消消乐", "步数为零，游戏失败", mode, board.getScore(), "返回主界面");
                }
                System.out.println("end");
                pauseFlag = false;
                clickFlag = false;
                addMouseListener(m);
            }

            public void sleep(long time) {
                try {
                    Thread.sleep(time);
                    repaint();
                } catch (Exception ex) {
                }
            }

            public void eliminate() {
                int flag = 1;
                while (flag == 1) {
                    flag = 0;
                    board.findSameFood();
                    for (int m = row - 1; m >= 0; m--) {
                        for (int n = 0; n < col; n++) {
                            if (board.getChess(m, n).canEliminate()) {
                                effectPlay(board.eliminateChess(m, n));
                                for (int i = 0; i < 6; i++) {
                                    goalLabel[i].setText(board.getGoal()[i] + "");
                                }

                                sleep(100);
                                flag = 1;
                                fall();
                            }
                        }
                    }
                    // board.printBoard();
                }
            }

            public void fall() {
                for (int loop = 0; loop < 9; loop++) {
                    for (int m = 0; m < row; m++) {
                        if (board.fallOneRow(m)) {
                            sleep(30);
                        }
                    }
                }
                boolean fallFlag = true;
                while (fallFlag) {
                    fallFlag = false;
                    for (int m = 0; m < row; m++)
                        for (int n = 0; n < col; n++)
                            if (!board.getChess(m, n).isExist()) {
                                if (board.fallSlide(m, n)) {
                                    fallFlag = true;
                                    sleep(30);
                                    for (int loop = 0; loop < 9; loop++) {
                                        for (int r = 0; r < row; r++) {
                                            if (board.fallOneRow(r)) {
                                                sleep(30);
                                            }
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }
                }
            }

        }).start();
    }

    public void effectPlay(String s) {
        if (s.equals(""))
            new Audio("resource/music/elimination.wav").start(1);
        for (String ss : s.split(" ")) {
            switch (ss) {
            case "b":
                new Audio("resource/music/boom.wav").start(3);
                break;
            case "h4":
            case "v4":
                new Audio("resource/music/light.wav").start(1);
                break;
            case "h5":
            case "v5":
                new Audio("resource/music/all.wav").start(1);
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

class BgPanel extends JPanel {
    public ImageIcon img = new ImageIcon("resource/background.jpg");

    BgPanel() {
        setSize(1000, 562);
        setLayout(null);
        JLabel title = new JLabel(new ImageIcon("resource/title.png"));
        add(title);
        title.setBounds(100, 50, 200, 200);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img.getImage(), 0, 0, null);
    }

}

class GamePanel extends BgPanel {
    public ChessBoard board;
    public double time;
    public int row, col;
    public Image[] image_pic = new Image[7];
    public int totalTime;
    public Image clock = new ImageIcon("resource/clock.png").getImage();

    GamePanel(ChessBoard board) {
        removeAll();
        setLayout(null);
        this.board = board;
        totalTime = board.getTotalTime();
        time = totalTime;
        row = board.getRow();
        col = board.getCol();
        for (int i = 0; i < 6; i++) {
            image_pic[i] = new ImageIcon(Settings.ICON_PATH + Settings.FOOD_ICON[i]).getImage();
        }
        image_pic[6] = new ImageIcon(Settings.ICON_PATH + Settings.WALL_ICON).getImage();
    }

    public double getTime() {
        return time;
    }

    public void addTime() {
        time += 5;
        if (time > totalTime) {
            time = totalTime;
        }
    }

    public boolean reduceTime() {
        time = time - 0.05;
        if (time <= 0)
            return false;
        else
            return true;
    }

    public void paintComponent(Graphics g) {
        int w_offset = 130;
        int h_offset = 36;
        int chessSize = 50;
        g.drawImage(img.getImage(), 0, 0, null);
        g.setColor(Color.WHITE);
        g.fillRect(125, 31, 500, 500); // chessBoard
        g.fillRect(685, 125, 255, 300); // goalBoard and plateBoard
        for (int i = 0; i < 3; i++) {
            g.fillRect(700 + i * 80, 440, 50, 50); // toolBoard
        }
        g.fillRoundRect(745, 50, 195, 20, 3, 3); // timeBar
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(750, 53, (int) (185 * time / totalTime), 14, 2, 2);

        g.setColor(new Color(135, 206, 235));
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board.getChess(i, j).getIce() != 0) {
                    g.fillRect(w_offset + (chessSize + 5) * j,
                            h_offset + (chessSize + 5) * i
                                    + (int) ((double) (4 - board.getChess(i, j).getIce()) / 4 * chessSize),
                            chessSize, (int) ((double) board.getChess(i, j).getIce() / 4 * chessSize) + 1);
                }
                if (board.getChess(i, j).isExist()) {
                    g.drawImage(image_pic[board.getChess(i, j).getKind()], w_offset + (chessSize + 5) * j,
                            h_offset + (chessSize + 5) * i, chessSize, chessSize, null);
                }
            }
        }
        g.setColor(Color.RED);
        for (int i = 0; i < 10; i++) {
            g.fillRect(125, 31 + 55 * i, 500, 5);
        }
        for (int i = 0; i < 10; i++) {
            g.fillRect(125 + 55 * i, 31, 5, 500);
        }
        w_offset += 565;
        h_offset += 105;
        for (int i = 0; i < 6; i++) {
            g.drawImage(image_pic[i], w_offset + (chessSize + 65) * (i % 2), h_offset + (chessSize + 30) * (i / 2), 55,
                    55, null);
        }

        g.drawImage(clock, 675, 30, 68, 60, null);
        g.drawImage(image_pic[board.getPlate().getKind()], 840, 360, 60, 60, null);
    }
}

class GameEndDialog extends JDialog implements ActionListener {
    /**
     *
     */
    private JButton btnEnd;
    private MainWindow frame;
    private int mode;
    private int score;

    GameEndDialog(MainWindow frame, String title, String text, int mode, int score, String btn) {
        super(frame, title, true);
        this.frame = frame;
        this.mode = mode;
        this.score = score;
        setIconImage(new ImageIcon("resource/title.png").getImage());
        setLayout(null);
        setLocationRelativeTo(null);
        setSize(300, 200);
        setResizable(false);
        getButton(btn, 45, 100, 200, 50);
        JLabel gameInfo = getLabel(text, 0, 20, 300, 50, 22);
        JLabel scoreInfo = getLabel("你的分数为：" + score, 0, 68, 300, 20, 18);
        add(gameInfo);
        add(btnEnd);
        add(scoreInfo);
        // setUndecorated(true);
        setVisible(true);
    }

    private void recordScore() {
        int[][] totalScore = Functions.readScore("resource/score.txt");
        int temp;
        for (int i = 0; i < 3; i++) {
            if (score > totalScore[mode][i]) {
                temp = totalScore[mode][i];
                totalScore[mode][i] = score;
                score = temp;
            }
        }
        Functions.write("resource/score.txt", totalScore);
        Functions.printDoubleIntArray(totalScore);
    }

    private JLabel getLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel gameInfo = new JLabel();
        gameInfo.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        gameInfo.setText(text);
        gameInfo.setBounds(x, y, width, height);
        gameInfo.setHorizontalAlignment(SwingConstants.CENTER);
        return gameInfo;

    }

    private void getButton(String text, int x, int y, int width, int height) {
        btnEnd = new JButton();
        btnEnd.setFont(new Font("微软雅黑", Font.BOLD, 16));
        btnEnd.setText(text);
        btnEnd.setContentAreaFilled(false);
        btnEnd.setForeground(Color.BLACK);
        btnEnd.setFocusPainted(false);
        btnEnd.setBorderPainted(false);
        btnEnd.setBounds(x, y, width, height);
        btnEnd.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        recordScore();
        this.frame.setContentPane(frame.getStartPanel());
        this.frame.bgm.loop();
    }
}

class ScorePanel extends BgPanel {
    ScorePanel() {
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img.getImage(), 0, 0, null);
        // g.setColor(Color.WHITE);
        // g.fillRect(125, 31, 500, 500); // chessBoard
    }
}