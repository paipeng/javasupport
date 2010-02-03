package deng.huffman.dukehuff.zimpl;

import deng.huffman.dukehuff.test.HuffViewer;
import deng.huffman.dukehuff.test.IHuffModel;

public class Huff {
    public static void main(String[] args){
    	Huff main = new Huff();
    	main.setModelClassName(System.getProperty("modelClassName", "deng.huffman.dukehuff.zimpl.DefaultIHuffModel"));
    	main.run();
    }
    
    private String modelClassName;
    public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}
    
	private void run() {
		try {
			IHuffModel model = (IHuffModel)Class.forName(modelClassName).newInstance();
	    	HuffViewer sv = new HuffViewer("Duke Compsci Huffing - Z Implementation");
	        sv.setModel(model);    
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
