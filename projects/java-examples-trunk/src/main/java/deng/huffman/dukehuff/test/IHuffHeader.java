package deng.huffman.dukehuff.test;

/**
 * Separate reading/writing header for Huffman compression
 * from other parts of the program. The general idea is that
 * all code for reading/writing/checking the header will be
 * in a class that implements this interface. Candidate classes
 * include storing/reading int values, e.g., one for each
 * of 256 different characters or explicitly storing the huffman tree
 * which is what's needed for the uncompression process.
 * @author ola
 *
 */
import java.io.IOException;

public interface IHuffHeader {
    /**
     * Return number of bits needed to store header if writeHeader called.
     * @return number of bits in header if written
     */
    public int headerSize();
    
    /**
     * Write the header to the specified stream.
     * @param out is the stream to which header written
     */
    public void writeHeader(BitOutputStream out);
    
    /**
     * Read the header and create a tree from the read header.
     * Return a treemaker capable of making the tree, don't
     * actually construct the tree here, necessarily.
     * @param in is inputstream for header
     * @return a treemaker for the tree read
     * @throws IOException if header malformed or not readable
     */
    public ITreeMaker readHeader(BitInputStream in) throws IOException;
}
