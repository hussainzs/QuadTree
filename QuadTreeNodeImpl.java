// QuadTree

import java.util.Objects;

public class QuadTreeNodeImpl implements QuadTreeNode {

    /*
     * Attributes
     */
    private Integer color;
    private int size; //dimension
    //instead of adding `isLeaf` attribute we can just check if all children of a node are null
    
    private QuadTreeNodeImpl[] children;
    /**
     * Constructor for a Leaf Node
     * @param color - color of the leaf node
     * @param size - the length and width of the quadrant represented by this node
     */
    public QuadTreeNodeImpl(Integer color, int size) {
        this.color = color;
        this.size = size;
        children = null;
    }
    /**
     * Constructor for an Internal Node
     * @param size - the length and width of the quadrant
     * @param topLeft - the top left quadrant
     * @param topRight - the top right quadrant
     * @param bottomLeft - the bottom left quadrant
     * @param bottomRight - the bottom right quadrant
     */
    public QuadTreeNodeImpl(int size,
                            QuadTreeNodeImpl topLeft, QuadTreeNodeImpl topRight,
                            QuadTreeNodeImpl bottomLeft, QuadTreeNodeImpl bottomRight) {
        this.color = null;
        this.size = size;
        this.children = new QuadTreeNodeImpl[] {topLeft, topRight, bottomLeft, bottomRight};
    }


    /**
     * ! Do not delete this method !
     * <p/>
     *
     * @param image image to put into the tree
     * @return the newly built QuadTreeNode instance which stores the compressed image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.Length is not a power of 2
     * @throws IllegalArgumentException if image - the 2d-array, is not a perfect square
     */
    public static QuadTreeNodeImpl buildFromIntArray(int[][] image) {
        if (image == null || image.length == 0 ||
                !isPowerOfTwo(image.length) || !isPerfectSquare(image)) {
            throw new IllegalArgumentException();
        }
        return buildFromArrayHelper(image, image.length, 0, 0);
    }

    /**
     * Recursive helper method to build the QuadTree
     * @param image - the image to be compressed
     * @param size - the length and width of the quadrant
     * @param x - the x coordinate of the quadrant (top left)
     * @param y - the y coordinate of the quadrant (top left)
     * @return = the newly built QuadTreeNode instance which represents the compressed image
     */
    private static QuadTreeNodeImpl buildFromArrayHelper(int[][] image, int size, int x, int y) {
        //get color of the pixel
        int color = image[y][x];

        //base case: if size is one, then it is a leaf node
        if (size == 1) {
            return new QuadTreeNodeImpl(color, size);
        }

        //recursive case: split the image into 4 quadrants
        //Split into 4 quadrants. We will check the colors later to see if they are the same
        int half = size / 2;
        QuadTreeNodeImpl topLeft = buildFromArrayHelper(image, half, x, y);
        QuadTreeNodeImpl topRight = buildFromArrayHelper(image, half, x + half, y);
        QuadTreeNodeImpl bottomLeft = buildFromArrayHelper(image, half, x, y + half);
        QuadTreeNodeImpl bottomRight = buildFromArrayHelper(image, half, x + half, y + half);

//        /**
//         * check if all quadrants actually have the same color
//         * IF they have same color then we can merge them into one leaf node and return it
//         * ELSE return the node with the 4 quadrants
//         * This allows us to have O(1) runtime on every level since
//            we don't check every pixel with every other pixel
//         */
        //test if any of them have color null
        //And test if all quadrants have the same color
        boolean sameColor = (
                topLeft.color != null &&
                        topRight.color != null &&
                        bottomLeft.color != null &&
                        bottomRight.color != null && topRight.color.equals(topLeft.color) &&
                        bottomLeft.color.equals(topLeft.color) &&
                        bottomRight.color.equals(topLeft.color)
        );
        if (sameColor) {
            //leaf node
            return new QuadTreeNodeImpl(color, size);
        } else {
            //internal node with 4 quadrants
            return new QuadTreeNodeImpl(size, topLeft, topRight, bottomLeft, bottomRight);
        }
    }

    // Find power of two by bit manipulation
    //Runtime: O(1)
    private static boolean isPowerOfTwo(int n) {
        //powers of 2 will have only one bit = 1 and the rest = 0
        //applying AND on n and n-1 will result in 0 if n is a power of 2
        //i.e. n = 8 = 1000, n-1 = 7 = 0111, 1000 & 0111 = 0000
        //i.e. n = 7 = 0111, n-1 = 6 = 0110, 0111 & 0110 = 0110
        return (n & (n - 1)) == 0;
    }

    //Check if the image is a perfect square
    //Runtime: O(n)
    private static boolean isPerfectSquare(int[][] image) {
        for (int[] row : image) {
            if (row.length != image.length) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper for getColor and finds the color of leaf node representing the pixel at (x,y)
     * @param x - the x coordinate of the pixel
     * @param y - the y coordinate of the pixel
     * @return - color of the pixel
     */
    private int getColorHelper(int x, int y) {
        //base case: if leaf node, then return the color of the node
        if (isLeaf()) {
            return this.color;
        }
        int half = size / 2;
        //keep in mind during recursive calls we are
        //passing the x and y coordinates RELATIVE to the node we are calling getLeaf on
        //like representing same x,y but in different coordinate systems (change of basis)
        if (x < half && y < half) {
            return getQuadrant(QuadName.TOP_LEFT).getColorHelper(x, y);
        } else if (x >= half && y < half) {
            return getQuadrant(QuadName.TOP_RIGHT).getColorHelper(x - half, y);
        } else if (x < half && y >= half) {
            return getQuadrant(QuadName.BOTTOM_LEFT).getColorHelper(x, y - half);
        } else {
            return getQuadrant(QuadName.BOTTOM_RIGHT).getColorHelper(x - half, y - half);
        }
    }

    //Runtime: O(log n) because getLeaf is O(log n)
    @Override
    public int getColor(int x, int y) {
        //check if x and y are out of bounds
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException();
        }
        return getColorHelper(x, y);
    }

    //Runtime: O(1)
    @Override
    public QuadTreeNodeImpl getQuadrant(QuadName quadrant) {
        //note in our constructor we set the children array to be in the order of
        //children[0] = top left, children[1] = top right and so on
        if (isLeaf()) {
            return null;
        } else {
            switch (quadrant) {
                case TOP_LEFT:
                    return children[0];
                case TOP_RIGHT:
                    return children[1];
                case BOTTOM_LEFT:
                    return children[2];
                case BOTTOM_RIGHT:
                    return children[3];
                default:
                    return null;
            }
        }
    }

    //Runtime: O(1)
    @Override
    public int getDimension() {
        return size;
    }

    //Runtime: O(n)
    @Override
    public int getSize() {
        //base case: if leaf node, then return size is 1
        if (isLeaf()) {
            return 1;
        }
        //run a for loop to recursively traverse over each child and add their subtrees
        int numNodes = 1;
        for (QuadTreeNodeImpl child : children) {
            numNodes += child.getSize();
        }
        return numNodes;
    }

    //Runtime: O(1)
    @Override
    public boolean isLeaf() {
        return (children == null) || children[0] == null &&
                children[1] == null &&
                children[2] == null &&
                children[3] == null;
    }


    //Runtime O(n)
    @Override
    public int[][] decompress() {
        //create a new array to store the decompressed image
        int [][] decompressedImage = new int [size][size];
        decompressRecursive(decompressedImage, 0, 0, size);
        return decompressedImage;
    }

    private void decompressRecursive(int[][] array, int startX, int startY, int dimension) {
        //Base case: if leaf node, then fill the relevant quadrant with the color of the node
        if (isLeaf()) {
            for (int x = startX; x < startX + dimension; x++) {
                for (int y = startY; y < startY + dimension; y++) {
                    array[y][x] = this.color;
                }
            }
        } else { //Recursive case: fill the quadrants with the colors of the children
            int half = dimension / 2;
            getQuadrant(QuadName.TOP_LEFT).decompressRecursive(array, startX, startY, half);
            getQuadrant(QuadName.TOP_RIGHT).decompressRecursive(array, startX + half,
                    startY, half);
            getQuadrant(QuadName.BOTTOM_LEFT).decompressRecursive(array, startX,
                    startY + half, half);
            getQuadrant(QuadName.BOTTOM_RIGHT).decompressRecursive(array, startX + half,
                    startY + half, half);
        }
    }

    @Override
    public double getCompressionRatio() {
        return (double) getSize() / (getDimension() * getDimension());
    }

    /**
     * Sets the color of the pixel at the given coordinates.
     * @param x - the x coordinate of the pixel
     * @param y - the y coordinate of the pixel
     * @param c - the new color of the pixel
     * @throws IllegalArgumentException if x or y are out of bounds
     */
    @Override
    public void setColor(int x, int y, int c) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException();
        }
        setColorHelper(x, y, c, size);
    }

    /**
     * Recursive helper method to set the color of a pixel and update the quadtree.
     * @param x - the x coordinate of the pixel
     * @param y - the y coordinate of the pixel
     * @param c - the new color of the pixel
     * @param dimension - the length and width of the quadrant
     */
    private void setColorHelper(int x, int y, int c, int dimension) {
        //base case: if size is one, then it is a leaf node
        if (dimension == 1) {
            this.color = c;
            return;
        }

        //recursive case: set the color of the pixel and update the quadtree
        int half = dimension / 2;

        if (isLeaf()) {
            QuadTreeNodeImpl topLeft = new QuadTreeNodeImpl(color, half);
            QuadTreeNodeImpl topRight = new QuadTreeNodeImpl(color, half);
            QuadTreeNodeImpl bottomLeft = new QuadTreeNodeImpl(color, half);
            QuadTreeNodeImpl bottomRight = new QuadTreeNodeImpl(color, half);
            children = new QuadTreeNodeImpl[4];
            children[0] = topLeft;
            children[1] = topRight;
            children[2] = bottomLeft;
            children[3] = bottomRight;
            this.color = null;
        }

        if (x < half && y < half) {
            getQuadrant(QuadName.TOP_LEFT).setColorHelper(x, y, c, half);
        } else if (x >= half && y < half) {
            getQuadrant(QuadName.TOP_RIGHT).setColorHelper(x - half, y, c, half);
        } else if (x < half && y >= half) {
            getQuadrant(QuadName.BOTTOM_LEFT).setColorHelper(x, y - half, c, half);
        } else {
            getQuadrant(QuadName.BOTTOM_RIGHT).setColorHelper(x - half, y - half, c, half);
        }

        //check if all quadrants actually have the same color
        boolean sameColor = (
                getQuadrant(QuadName.TOP_LEFT).isLeaf() &&
                        getQuadrant(QuadName.TOP_RIGHT).isLeaf() &&
                        getQuadrant(QuadName.BOTTOM_LEFT).isLeaf() &&
                        getQuadrant(QuadName.BOTTOM_RIGHT).isLeaf() &&

                        Objects.equals(getQuadrant(QuadName.TOP_RIGHT).color,
                                getQuadrant(QuadName.TOP_LEFT).color) &&

                        Objects.equals(getQuadrant(QuadName.BOTTOM_LEFT).color,
                                getQuadrant(QuadName.TOP_LEFT).color) &&

                        Objects.equals(getQuadrant(QuadName.BOTTOM_RIGHT).color,
                                getQuadrant(QuadName.TOP_LEFT).color)
        );

        if (sameColor) {
            //merge the children into one leaf node
            color = getQuadrant(QuadName.TOP_LEFT).color;
            children[0] = null;
            children[1] = null;
            children[2] = null;
            children[3] = null;
        }
    }

}
