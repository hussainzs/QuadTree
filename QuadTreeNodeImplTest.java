import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
public class QuadTreeNodeImplTest {
    //setup
    int [][] array4Quadrants;
    int [][] arrayMultiQuadrants;
    int [][] arrayUniformColor;
    int [][] arrayUniqueAllPixels;
    int [][] emptyArray;
    int [][] raggedArray;
    int [][] notPowerOfTwoArray;
    int [][] nullArray;
    int [][] nullRowArray;
    int [][] oneElementArray;

    @Before
    public void setUp() {
        array4Quadrants = new int[][]{
                {1, 1, 2, 2},
                {1, 1, 2, 2},
                {3, 3, 4, 4},
                {3, 3, 4, 4}};
        arrayMultiQuadrants = new int[][]{
                {1, 1, 2, 3},
                {1, 1, 1, 7},
                {2, 2, 0, 0},
                {2, 2, 0, 0}};
        arrayUniformColor = new int[][]{
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}};
        arrayUniqueAllPixels = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}};
        oneElementArray = new int[][]{
                {1}};
        emptyArray = new int[][]{};
        raggedArray = new int[][]{
                {1, 2, 3},
                {4, 5, 2},
                {6, 7, 8, 9, 10, 11}};
        notPowerOfTwoArray = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8}};
        nullArray = null;
        nullRowArray = new int[][]{ null, {1, 2, 3, 4}, {5, 6, 7, 8}}; }

    /**
     * Test building the quadtree given arrays. Test dimension and size of tree.
     */
    @Test
    public void testBuildTree4Quadrants() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(array4Quadrants);
        assertEquals(4, tree.getDimension());
        assertEquals(5, tree.getSize());
        //compression ratio test
        assertEquals(5.0 / 16.0, tree.getCompressionRatio(), 0.0001);
        //testing colors
        assertEquals(1, tree.getColor(0, 0));
        assertEquals(1, tree.getColor(0, 1));
        assertEquals(2, tree.getColor(2, 0));
        assertEquals(4, tree.getColor(3, 3));
        assertEquals(4, tree.getColor(2, 2));
        assertEquals(4, tree.getColor(3, 2));

    }

    @Test
    public void testBuildTreeMultiQuadrants() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayMultiQuadrants);
        assertEquals(4, tree.getDimension());
        assertEquals(9, tree.getSize());
        //compression ratio test
        assertEquals(9.0 / 16.0, tree.getCompressionRatio(), 0.0001);
        //testing colors
        assertEquals(1, tree.getColor(0, 0));
        assertEquals(1, tree.getColor(0, 1));
        assertEquals(2, tree.getColor(2, 0));
        assertEquals(7, tree.getColor(3, 1));
        assertEquals(0, tree.getColor(2, 2));
    }

    @Test
    public void testBuildTreeUniformColor() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniformColor);
        assertEquals(4, tree.getDimension());
        assertEquals(1, tree.getSize());
        //compression ratio test
        assertEquals(1.0 / 16.0, tree.getCompressionRatio(), 0.0001);
        //testing colors
        assertEquals(1, tree.getColor(0, 0));
        assertEquals(1, tree.getColor(0, 1));
        assertEquals(1, tree.getColor(3, 3));
        assertEquals(1, tree.getColor(2, 2));
    }

    @Test
    public void testBuildTreeUniqueAllPixels() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniqueAllPixels);
        assertEquals(4, tree.getDimension());
        assertEquals(21, tree.getSize());
        //compression ratio test
        assertEquals(21.0 / 16.0, tree.getCompressionRatio(), 0.0001);
        //testing colors
        assertEquals(8, tree.getColor(3, 1));
        assertEquals(14, tree.getColor(1, 3));
        assertEquals(4, tree.getColor(3, 0));
    }

    @Test
    public void testBuildTreeOneElementArray() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(oneElementArray);
        assertEquals(1, tree.getDimension());
        assertEquals(1, tree.getSize());
        //compression ratio test
        assertEquals(1.0, tree.getCompressionRatio(), 0.0001);
        //testing colors
        assertEquals(1, tree.getColor(0, 0));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildTreeEmptyArray() {
        QuadTreeNodeImpl.buildFromIntArray(emptyArray); //throws illegal argument exception
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildTreeRaggedArray() {
        QuadTreeNodeImpl.buildFromIntArray(raggedArray); //throws illegal argument exception
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildTreeNotPowerOfTwoArray() {
        QuadTreeNodeImpl.buildFromIntArray(notPowerOfTwoArray); //throws illegal argument exception
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildTreeNullArray() {
        QuadTreeNodeImpl.buildFromIntArray(nullArray); //throws illegal argument exception
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildTreeNullRowArray() {
        QuadTreeNodeImpl.buildFromIntArray(nullRowArray); //throws illegal argument exception
    }

    /**
     * Test illegal argument exception for getColor
     */
    @Test (expected = IllegalArgumentException.class)
    public void testGetColorIllegalColorOverBounds() {
        QuadTreeNodeImpl.buildFromIntArray(array4Quadrants).getColor(4, 4);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testGetColorIllegalColorUnderBounds() {
        QuadTreeNodeImpl.buildFromIntArray(arrayMultiQuadrants).getColor(-1, 0);
    }

    /**
     * Test decompress
     */
    @Test
    public void testDecompress4Quadrants() {
        int [][] decompressed = QuadTreeNodeImpl.buildFromIntArray(array4Quadrants).decompress();
        assertEquals(4, decompressed.length);
        assertEquals(4, decompressed[0].length);
        assertArrayEquals(array4Quadrants, decompressed);
    }

    @Test
    public void testDecompressMultiQuadrants() {
        int [][] decompressed = QuadTreeNodeImpl.buildFromIntArray(arrayMultiQuadrants).
                decompress();
        assertEquals(4, decompressed.length);
        assertEquals(4, decompressed[0].length);
        assertArrayEquals(arrayMultiQuadrants, decompressed);
    }

    @Test
    public void testDecompressUniformColor() {
        int [][] decompressed = QuadTreeNodeImpl.buildFromIntArray(arrayUniformColor).decompress();
        assertEquals(4, decompressed.length);
        assertEquals(4, decompressed[0].length);
        assertArrayEquals(arrayUniformColor, decompressed);
    }

    @Test
    public void testDecompressUniqueAllPixels() {
        int [][] decompressed = QuadTreeNodeImpl.buildFromIntArray(arrayUniqueAllPixels).
                decompress();
        assertEquals(4, decompressed.length);
        assertEquals(4, decompressed[0].length);
        assertArrayEquals(arrayUniqueAllPixels, decompressed);
    }

    @Test
    public void testDecompressOneElementArray() {
        int [][] decompressed = QuadTreeNodeImpl.buildFromIntArray(oneElementArray).decompress();
        assertEquals(1, decompressed.length);
        assertEquals(1, decompressed[0].length);
        assertArrayEquals(oneElementArray, decompressed);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDecompressEmptyArray() {
        QuadTreeNodeImpl.buildFromIntArray(emptyArray).decompress();
    }

    /*
     * Test getQuadrant
     */
    @Test
    public void testGetQuadrant() {
        QuadTreeNodeImpl root = QuadTreeNodeImpl.buildFromIntArray(array4Quadrants);
        QuadTreeNodeImpl topLeft = root.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT);
        QuadTreeNodeImpl topRight = root.getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT);
        QuadTreeNodeImpl bottomLeft = root.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT);
        QuadTreeNodeImpl bottomRight = root.getQuadrant(QuadTreeNode.QuadName.BOTTOM_RIGHT);
        int[][] expectedTopLeft = {{1, 1}, {1, 1}};
        int[][] expectedTopRight = {{2, 2}, {2, 2}};
        int[][] expectedBottomLeft = {{3, 3}, {3, 3}};
        int[][] expectedBottomRight = {{4, 4}, {4, 4}};
        assertArrayEquals(expectedTopLeft, topLeft.decompress());
        assertArrayEquals(expectedTopRight, topRight.decompress());
        assertArrayEquals(expectedBottomLeft, bottomLeft.decompress());
        assertArrayEquals(expectedBottomRight, bottomRight.decompress());
    }

    /**
     * Test setColor
     */
    @Test
    public void testSetColorMergeBack() {
        int [][] arrayUniformColor = new int[][]{
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 2, 1, 1},
                {1, 1, 1, 1}};
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniformColor);
        tree.setColor(1, 2, 1);
        int [][] expectedArray = new int[][]{
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    @Test
    public void testSetColorNoChangeInNodes() {
        int [][] arrayUniformColor = new int[][]{
                {1, 1, 3, 3},
                {1, 1, 3, 3},
                {2, 2, 4, 3},
                {2, 2, 4, 4}};
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniformColor);
        tree.setColor(3, 2, 5);
        int [][] expectedArray = new int[][]{
                {1, 1, 3, 3},
                {1, 1, 3, 3},
                {2, 2, 4, 5},
                {2, 2, 4, 4}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    @Test
    public void testSetColorSplitFurther() {
        int [][] arrayUniformColor = new int[][]{
                {1, 1, 3, 3},
                {1, 1, 3, 3},
                {2, 2, 4, 4},
                {2, 2, 4, 4}};
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniformColor);
        tree.setColor(3, 0, 1);
        int [][] expectedArray = new int[][]{
                {1, 1, 3, 1},
                {1, 1, 3, 3},
                {2, 2, 4, 4},
                {2, 2, 4, 4}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    @Test
    public void testSetColorUniqueAllPixels() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniqueAllPixels);
        tree.setColor(0, 0, 5);
        int [][] expectedArray = new int[][]{
                {5, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    @Test
    public void testSetColorReplaceSameColor() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(arrayUniqueAllPixels);
        tree.setColor(0, 1, 5);
        int [][] expectedArray = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    @Test
    public void testSetColorOneElementArray() {
        QuadTreeNodeImpl tree = QuadTreeNodeImpl.buildFromIntArray(oneElementArray);
        tree.setColor(0, 0, 5);
        int [][] expectedArray = new int[][]{
                {5}};
        assertArrayEquals(expectedArray, tree.decompress());
    }

    /**
     * Test illegal argument exception for setColor
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetColorIllegalColorOverBounds() {
        QuadTreeNodeImpl.buildFromIntArray(array4Quadrants).setColor(4, 4, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetColorIllegalColorUnderBounds() {
        QuadTreeNodeImpl.buildFromIntArray(arrayMultiQuadrants).setColor(-1, 0, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetColorNullArray() {
        QuadTreeNodeImpl.buildFromIntArray(nullArray).setColor(0, 0, 1);
    }
}





