package elimination;

public class Food extends Chess {
    private int kind; // 0-5
    private int ice; // num of ice layers
    private boolean exist;

    Food(int kind, int ice) {
        super(Settings.FOOD_SHAPE[kind], (ice == 0) ? Settings.OPERATE_ABLE : Settings.OPERATE_UNABLE,
                Settings.CLEAN_ABLE);
        this.kind = kind;
        this.ice = ice;
        this.exist = true;
    }

    public boolean compare(Chess c) {
        if (this.kind == ((Food) c).kind)
            return true;
        else
            return false;
    }

    public int getKind() {
        return kind;
    }

    public int getIce() {
        return ice;
    }

    public void eliminate() {
        if (exist == false)
            return;
        if (ice > 0) {
            ice--;
            if (ice == 0)
                setOperable();
        } else {
            exist = false;
            this.setShape(Settings.AIR_SHAPE);
        }
    }

    public void reduceIce() {
        if (ice > 0) {
            ice--;
            if (ice == 0)
                setOperable();
        }

    }

    public boolean isExist() {
        return exist;
    }

    public boolean canEliminate() {
        return isExist() && (this.getVerticalSame() >= 3 || this.getHorizontalSame() >= 3);
    }
}
