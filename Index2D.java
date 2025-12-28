package assignments.Ex2;

import java.util.Objects;

public class Index2D implements Pixel2D {
    private final int _x;
    private final int _y;

    public Index2D(int w, int h) {

        this._x = w;
        this._y = h;

    }

    public Index2D(Pixel2D other) {
        if (other == null) {
            throw new RuntimeException("Index2D copy-ctor: other is null");
        }
        this._x = other.getX();
        this._y = other.getY();
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        if (p2 == null) throw new RuntimeException("distance2D: p2 is null");
        int dx = this.x - p2.getX();
        int dy = this.y - p2.getY();
        return Math.sqrt((double) dx * dx + (double) dy * dy );
    }

    @Override
    public String toString() {
        return _x + "," + _y;
    }

    @Override
    public boolean equals(Object p) {
        if (this == p) return true;
        if (p == null) return false;
        if (p instanceof Pixel2D) {
            Pixel2D other = (pixel2D) p;
            return this._x == other.getX() && this._y == other.getY();
        }
        return false;
    }
}

