package deng.huffman.dukehuff.test;

/**
 * Utility binary-tree (Huffman tree) node for Huffman coding
 * 
 * @author Owen Astrachan
 * @version 1.0 July 2000
 */
public class TreeNode implements Comparable
{
    /**
     * construct leaf node (null children)
     * @param value is the value stored in the node (e.g., character)
     * @param weight is used for comparison (e.g., count of # occurrences)
     */
    
    public TreeNode(int value, int weight)
    {
	myValue = value;
	myWeight = weight;
    }
    /**
     * construct internal node (with children)
     * @param value is stored as value of node
     * @param weight is weight of node
     * @param ltree is left subtree
     * @param rtree is right subtree
     */

    public TreeNode(int value, int weight, TreeNode ltree, TreeNode rtree)
    {
	this(value,weight);
	myLeft = ltree;
	myRight = rtree;
    }

    /**
     * @return -1 if this < o, +1 if this > o, and 0 if this == 0
     */
    
    public int compareTo(Object o)
    {
	TreeNode rhs = (TreeNode) o;
	if (myWeight < rhs.myWeight)      return -1;
	else if (myWeight > rhs.myWeight) return  1;
	else                              return  0;
    }

    int myValue;
    int myWeight;
    TreeNode myLeft;
    TreeNode myRight;
}
