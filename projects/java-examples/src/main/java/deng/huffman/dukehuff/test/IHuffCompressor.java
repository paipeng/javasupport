package deng.huffman.dukehuff.test;

/*
 * Created on Nov 11, 2004
 *
 */

/**
 * @author ola
 *
 */
import java.io.InputStream;
import java.io.OutputStream;

public interface IHuffCompressor {
    public void compress(InputStream in, OutputStream out);
}
