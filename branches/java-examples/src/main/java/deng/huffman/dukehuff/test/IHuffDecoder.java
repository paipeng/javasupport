package deng.huffman.dukehuff.test;

/**
 * @author Owen Astrachan
 *
 */
import java.io.IOException;

public interface IHuffDecoder extends IHuffConstants{
    
    public void initialize(ITreeMaker treeMaker);
    public void doDecode(BitInputStream input, BitOutputStream output) throws IOException;
}
