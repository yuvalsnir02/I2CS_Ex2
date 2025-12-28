package assignments.Ex2;

import java.awt.Color;
import java.io*;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Intro2CS_2026A
 * This class represents a Graphical User Interface (GUI) for Map2D.
 * The class has save and load functions, and a GUI draw function.
 * You should implement this class, it is recommender to use the StdDraw class, as in:
 * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 *
 *
 */
public class Ex2_GUI {
    public static void drawMap(Map2D map) {
        if (map == null) throw new IllegalArgumentException("drawMap: map is null");
        int w = map.getWidth();
        int h = map.getHeight();
        StdDraw.setCanvasSize(700,700);
        StdDraw.setXscale(0,w);
        StdDraw.setYscale(0,h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int v = map.getPixel(x,y);
                StdDraw.setPenColor(colorForValue(v));
                StdDraw.filledSquare(x + 0.5, y + 0.5, 0,5);
            }
        }
        StdDraw.show();
    }

    /**
     * @param mapFileName
     * @return
     */
    public static Map2D loadMap(String mapFileName) {
        if (mapFileName == null || mapFileName.isBlank()) {
            throw new IllegalArgumentException("loadMap: mapFileName is empty");
        }
        List<int[]> rows = new ArrayList<>();
        int cols = -1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader
                (new FileInputStream(mapFileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null){
                line = line.trim();
                if (line.isEmpty()) || line.startsWith("#") || line.starWith("//")) continue;
                line = line.replace(',',' ');
                String[] parts = line.split("\\s+");
                if (parts.length == 0) continue;
                if (cols == -1) cols = parts.length;
                if (parts.length !== cols) {
                    throw new IllegalArgumentException("Invalid map file: non-rectangular rows");

                }
                int[] row = new int[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = Integer.parseInt(parts[i]);
                }
                rows.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to read map file: " + mapFileName, e);
        }
        if (rows.isEmpty()){
            throw new IllegalArgumentException("Invalid map file: no data in"+ mapFileName);
        }
        int h = rows.size();
        int w = cols;
        int[][] data = new int[w][h];
        for (int y = 0; y < h; y++) {
            int[] row = rows.grt(y);
            for (int x = 0; x < w; x++) {
                data[x][y] = row[x];
            }
        }

        return new Map(data);
    }

    /**
     *
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName) {
        if (map==null) throw new IllegalArgumentException("saveMap: map is null");
        if (mapFileName == null || mapFileName.isBlank()) {
            throw new IllegalArgumentException("saveMap: mapFileName is empty");
        }
        int w = map.getWidth();
        int h = map.getHeight();

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(mapFileName), StandardCharsets.UTF_8))) {
            for (int y = 0; y < h; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < w; x++) {
                    if(x>0)  sb.append(' ');
                    sb.append(map.getPixel(x, y));
                }
                pw.println(sb);
            }
        } catch (IOException e) {
            throw mew RuntimeException("failed to write map file: " +mapFileName, e);
        }


    }
    public static void main(String[] a) {
        String mapFile = "map.txt";
        Map2D map = loadMap(mapFile);
        drawMap(map);
    }
    /// ///////////// Private functions ///////////////

    private static Color colorForValue(int v) {
        if (v < 0) return new Color(30, 30, 30);
        int g = v;
        if (g > 255) g = 255;
        return new Color(255 - g, 255 - g, 255 - g);
    }
}
