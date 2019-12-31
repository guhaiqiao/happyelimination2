package elimination;

abstract public class Chess {
    private String shape; // shape of chess
    private boolean operable; // whether can be exchanged
    private boolean cleanable; // whether can be eliminate
    private int verticalSame = 0;
    private int horizontalSame = 0;
    protected boolean verticalE = false;
    protected boolean horizontalE = false;
    protected int[] verticalSameList = new int[Settings.MAXSIZE];
    protected int[] horizontalSameList = new int[Settings.MAXSIZE];

    Chess(String shape, boolean operable, boolean cleanable) {
        this.shape = shape;
        this.operable = operable;
        this.cleanable = cleanable;
    }

    public String getShape() {
        return shape;
    }

    public int getKind() {
        return 6;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public void setVerticalSame(int v) {
        verticalSame = v;
    }

    public void setHorizontalSame(int h) {
        horizontalSame = h;
    }

    public int getVerticalSame() {
        return verticalSame;
    }

    public int getHorizontalSame() {
        return horizontalSame;
    }

    public boolean canEliminate() {
        return verticalSame >= 3 || horizontalSame >= 3;
    }

    public boolean isOperable() {
        return operable;
    }

    public boolean isCleanable() {
        return cleanable;
    }

    public void setOperable() {
        operable = true;
    }

    public void setUnoperable() {
        operable = false;
    }

    abstract public void eliminate();

    public void reduceIce() {
    }

    abstract public boolean isExist();

    abstract public boolean compare(Chess c);

    public int getIce() {
        return 0;
    }
}