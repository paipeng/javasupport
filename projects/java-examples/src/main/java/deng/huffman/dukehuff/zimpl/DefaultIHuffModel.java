package deng.huffman.dukehuff.zimpl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import deng.huffman.dukehuff.test.HuffViewer;
import deng.huffman.dukehuff.test.IHuffModel;

public class DefaultIHuffModel implements IHuffModel {

	@Override
	public void initialize(InputStream stream) {
		System.out.println("initialize");
	}

	@Override
	public void setViewer(HuffViewer viewer) {
		System.out.println("setViewer");
	}

	@Override
	public void showCodings() {
		System.out.println("showCodings");
	}

	@Override
	public void showCounts() {
		System.out.println("showCounts");
	}

	@Override
	public void uncompress(InputStream in, OutputStream out) {
		System.out.println("uncompress");
	}

	@Override
	public void write(InputStream stream, File file, boolean force) {
		System.out.println("write");
	}

}
