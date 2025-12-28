package assignments.Ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;

    @BeforeEach
    public void setup() {
        _m3_3 = new Map(_map_3_3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }
    @Test
    void testInDeepCopy() {
        int[][] a = new int[][]{{1, 2}, {3, 4}};
        Map2D m = new Map(a);
        a[0][0] = 999;
        assertEquals(1, m.getPixel(0, 0));

        int[][] copy = m.getMap();
        copy[0][0] = 777;
        assertEquals(1, m.getPixel(0, 0));
    }
    @Test
    void testFillNonCyclicComponentSize(){
        int filled = _m3_3.fill(new Index2D(1,1) , 9 , false);
        assertEquals(1, filled);
        assertEquals(9, _m3_3.getPixel(1,1));
        assertEquals(0, _m3_3.getPixel(0,0));
    }
    @Test
    void testShortestPathCyclicWrap() {
        Map2D m = nwe Map(new int[][]{{0},{1},{0}});
        Pixel2D[] path = m.shortestPath(new Index2D(0,0), new Index2D(2,0), 1, true);
        assertNotNull(path);
        assertEquals(2, path.length);
        assertEquals(new Index2D(0,0), path[0]);
        assertEquals(new Index2D(2,0), path[1]);
    }
    @Test
    void testAllDistanceOpenGrid(){
        Map2D m = new Map(3,3,0)
        Map2D d = m.allDistance(new Index2D(0,0), 1, false);
        assertNorthNull(d);
        assertEquals(0, d.getPixel(0,0));
        assertEquals(1, d.getPixel(1,0));
        assertEquals(1, d.getPixel(0,1));
        assertEquals(4, d.getPixel(2,2));
    }

}
