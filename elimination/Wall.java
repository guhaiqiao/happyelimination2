package elimination;

public class Wall extends Chess {
    Wall() {
        super(Settings.WALL_SHAPE, Settings.OPERATE_UNABLE, Settings.CLEAN_UNABLE);
    }

    public boolean compare(Chess c) {
        return false;
    }

    public boolean isExist() {
        return true;
    }

    public void eliminate() {
    }
}