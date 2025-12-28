package assignments.Ex2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable{
    private static final long serialVersionUID = 1L;

    private int[][] _map;
    public Map(int w, int h, int v) { init(w, h, v); }
    public Map(int size) { this(size, size, 0);}
    public Map(int[][] data) { init(data); }

	@Override
	public void init(int w, int h, int v) {
        if (w<=0 || h<=0) throw new RuntimeException("init(w,h,v): illegal dimensions" + w + "x" + h);
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = v;
            }
        }

	}

	@Override
	public void init(int[][] arr) {
        if (arr == null) throw new RuntimeException("init(arr): arr is null);
                if (arr.length == 0) throw new RuntimeException("init(arr): empty array");
                if (arr[0] ==null || arr[0].length == 0) throw new RuntimeException("init(arr): empty inner array");

                int w = arr.length;
                int h = arr[0].length;
                for (int x = 0; x < w; x++) {
                    if (arr[x] == null || arr[x].length != h) {
                        throw new RuntimeException("init(arr): ragged array at column" + x);
                    }
                }
                _map = new int[w][h];
                for (int x = 0; x < w; x++) {
                    System.arraycopy(arr[x], 0, _map[x], 0, h);
                }

	}

	@Override
	public int[][] getMap() {
        int w = getWidth();
        int h = getHeight();
		int[][] ans = new int[w][h];
        for (int x = 0; x < w; x++) {
            System.arraycopy(_map[x], 0, ans[x], 0, h);
        }
		return ans;
	}

	@Override
	public int getWidth() {
        return (_map == null) ? 0 : _map.length;
    }

	@Override
	public int getHeight() {
        return (_map == null || _map.length == 0) ? 0 : _map[0].length;
    }

	@Override
	public int getPixel(int x, int y) {
        if (!inbounds(x,y)) throw new RuntimeException("getPixel: out of bounds (" + x + "," + y + ")");
      return _map[x][y];
    }

	@Override
	public int getPixel(Pixel2D p) {
        if (p == null) throw new RuntimeException("getPixel: p is null");
        return getPixel(p.getX(), p.getY());
	}

	@Override
	public void setPixel(int x, int y, int v) {
        if (!inBounds(x,y))  throw new RuntimeException("setPixel: out of bounds (" + x + "," + y + ")");
        _map[x][y] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v) {
        if (p == null) throw new RuntimeException("setPixel: p is null");
        setPixel(p.getX(), p.getY(), v);
	}

    @Override
    public boolean isInside(Pixel2D p) {
        if (p == null) return false;
        return inBounds(p.getX(), p.getY());
    }

    @Override
    public boolean sameDimensions(Map2D p) {
        if (p == null) return false;
        return this.getWidth() == p.getWidth() && this.getHeight() == p.getHeight();
    }

    @Override
    public void addMap2D(Map2D p) {
        if (p == null) return;
        if (!sameDimensions(p)) return;

        int w = getWidth();
        int h = getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] += p.getPixel(x, y);
            }
        }

    }

    @Override
    public void mul(double scalar) {
        int w = getWidth();
        int h = getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = (int)(_map[x][y] * scalar);
            }
        }

    }

    @Override
    public void rescale(double sx, double sy) {
        if (sx <= 0 || sy <=0) throw new RuntimeException("rescale: sx, sy must be > 0");
        int oldW = getWidth();
        int oldH = getHeight();
        int newW = (int) Math.round(oldW * sx);
        int newH = (int) Math.round(oldH * sy);
        if (newW <= 0) newW = 1;
        if (newH <= 0) newH = 1;
        int[][] newMap = new int[newW][newH];
        for (int x = 0; x < newW; x++) {
            for (int y = 0; y < newH; y++) {
                int srcX = (int) Math.floor(x / sx);
                int srcY = (int) Math.floor(y / sy);
                if (srcX < 0) srcX = 0;
                if (srcY < 0) srcY = 0;
                if (srcX >= oldW) srcX = oldW - 1;
                if (srcY >= oldH) srcY = oldH - 1;
                newMap[x][y] = _map[srcX][srcY];
            }
        }
        _map = newMap;

    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {
        if (center == null) throw new RuntimeException("drawCircle: center is null");
        if (rad < 0) return ;
        int w = getWidth();
        int h = getHeight();
        double cx = center.getX();
        double cy = center.getY();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                double dx = x - cx;
                double dy = y - cy;
                if (Math.sqrt(dx * dx + dy * dy) < rad) {
                    _map[x][y] = newColor;
                }
            }
        }

    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        if (p1 == null || p2 == null) throw new RuntimeException("drawLine: p1/p2 is null");
        if (!isInside(p1) || !isInside(p2)) throw new RuntimeException("drawLine: p1/p2 must be inside the map");
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        if (x1 == x2 && y1 == y2) {
            _map[x1][y1] = newColor;
            return;
        }
        int dx =  Math.abs(x2 - x1);
        int dy =  Math.abs(y2 - y1);
        if (dx >= dy) {
            if (x1 > x2) { drawLine(p2, p1, newColor); return; }
            double slope = (x2 == x1) ? 0.0 : ((double) (y2 - y1) / (double) (x2 - x1));
            for (int x = x1; x <= x2; x++) {
                double fy = y1 + slope * (x - x1);
                int y = (int) Math.round(fy);
                if (inBounds(x,y)) _map[x][y] = newColor;
            }
        } else {
            if (y1 > y2) { drawLine(p2, p1, newColor); return; }
            double slop = (y2 == y1) ? 0.0 : ((double) (x2 - x1) / (double) (y2 - y1);
            for (int y = y1; y <= y2; y++) {
                double gx = x1 + slop * (y - y1);
                int x = (int) Math.round(gx);
                if (inBounds(x, y)) _map[x][y] = newColor;
            }
        }

    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        if (p1 == null || p2 == null) throw new RuntimeException("drawRect: p1/p2 is null");
        if (!isInside(p1) || !isInside(p2)) throw new RuntimeException("drawRect: p1/p2 must be inside the map");
        int x1 = Math.min(p1.getX(), p2.getX());
        int x2 = Math.max(p1.getX(), p2.getX());
        int y1 = Math.min(p1.getY(), p2.getY());
        int y2 = Math.max(p1.getY(), p2.getY());
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                _map[x][y] = newColor;
            }
        }

    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob) return true;
        if (!(ob instanceof Map 2D)) return false;
        Map2D other = (Map2D) ob;
        if (!sameDimensions(other)) return false;
        int w = getWidth();
        int h = getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (this.getPixel(x,y) !=other.getPixel(x,y)) return false;
            }
        }
        return true;
    }

	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v,  boolean cyclic) {
		if (start == null) throw new RuntimeException("fill: start is null");
        if (!isInside(start)) return 0;
        int old_v = getPixel(start);
        if (old_v == new_v) return 0;
        boolean[][] visited = new boolean[getWidth()][getHeight()];
        Queue<Index2D> q = new ArrayDeque<>();
        Index2D s = new Index2D(start);
        q.add(s);
        visited[s.getX()][s.getY()] = true;
        int filled = 0;
        while (!q.isEmpty()) {
            Index2D cur = q.poll();
            int cx = cur.getX();
            int cy = cur.getY();
            if (_map[cx][cy] != old_v) continue;
            _map[cx][cy] = new_v;
            filled++;
            for (Index2D nb : neighbors(cx,cy, cycluc)) {
                int nx = nb.getX(), ny = nb.getY();
                if (!visited[nx][ny] && _map[nx][ny] == old_v) {
                    visited[nx][ny] = true;
                    q.add(nb);
                }
            }
        }
        return filled;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
		if (p1 == null || p2 == null) throw new RuntimeException("shortestPath: p1/p2 is null");
        if (!isInside(p1) || !isInside(p2)) return null;
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null;
        if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
            return new Pixel2D[]{new Index2D(p1)};
        }
        int w = getWidth();
        int h = getHeight();
        boolean[][] visited = new boolean[w][h];
        Index2D[][] parent = new Index2D[w][h];
        Queue<Index2D> q = new ArrayDeque<>();
        Index2D s = new Index2D(p1);
        Index2D t = new Index2D(p2);
        visited[s.getX()][s.getY()] = true;
        parent[s.getX()][s.getY()] = null;
        q.add(s);
        boolean found =  false;
        while (!q.isEmpty() && !found) {
            Index2D cur = q.poll();
            int cx = cur.getX();
            int cy = cur.getY();
            for (Index2D nb : neighbors(cx, cy, cycluc)) {
                int nx = nb.getX(), ny = nb.getY();
                if (visited[nx][ny]) continue;
                if (_map[nx][ny] == obsColor) continue;
                visited[nx][ny] = true;
                parent[nx][ny] = cur;
                if (nx == t.getX() && ny == t.getY()) { found = true; break; }
                q.add(nb);
            }
        }
        if (!found) return null;
        ArrayList<Pixel2D> rev = new ArrayList<>();
        Index2D cur = t;
        while (cur != null) {
            rev.add(cur);
            cur = parent[cur.getX()][cur.getY()];
        }
        Pixel2D[] path = new Pixel2D[rev.size()];
        for (int i = 0; i < rev.size; i++) {
            path[i] = rev.get(rev.size() - 1 - i);
        }
        return path;
	}
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        if (start == null) throw new RuntimeException("allDistance: start is null");
        if (!isInside(start)) return null;
        int w = getWidth();
        int h = getHeight();
        int[][] dist = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) dist[x][y] = -1;
        }
        if (getPixel(start) == obsColor) {
            return new Map(dist);
        }
        Queue<Index2D> q = new ArrayDeque<>();
        Index2D s = new Index2D(start);
        dist[s.getX()][s.getY()] = 0;
        q.add(s);
        while (!q.isEmpty()) {
            Index2D cur = q.poll();
            int cx = cur.getX();
            int cy = cur.getY();
            for (Index2D nb : neighbors(cx, cy, cyclic)) {
                int nx = nb.getX(), ny = nb.getY();
                if (_map[nx][ny] == obsColor) continue;
                if (dist[nx][ny] != -1) continue;
                dist[nx][ny] = dist[cx][cy] + 1;
                q.add(nb);
            }
        }
        return new Map(dist);
    }
	////////////////////// Private Methods ///////////////////////

    private boolean isInside(int x, int y) {
    return  x >= 0 &&  y >= 0 && x < getWidth() && y < getHeight();
    }
    private Index2D[] neighbors(int x, int y, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();
        int leftX = x - 1;
        int rightX = x + 1;
        int upY = y - 1;
        int downY = y + 1;
        if (cyclic) {
            if (leftX < 0) leftX = w - 1;
            if (rightX >= w) rightX = 0;
            if (downY < 0) downY = h - 1;
            if (upY >= h) upY = 0;
            return new Index2D[] {
                    new Index2D(leftX, y),
                    new Index2D(rightX, y),
                   new Index2D(x, downY),
                   new Index2D(x, upY)
            };
        } else {
            ArrayList<Index2D> nbs = new ArrayList<>(4);
            if (inBounds(leftX, y)) nbs.add(new Index2D(leftX, y));
            if (inBounds(rightX, y)) nbs.add(new Index2D(rightX, y));
            if (inBounds(x, downY)) nbs.add(new Index2D(x, downY));
            if (inBounds(x, upY)) nbs.add(new Index2D(x, upY));
            return nbs.toArray(new Index2D[0]);
        }
    }
}

