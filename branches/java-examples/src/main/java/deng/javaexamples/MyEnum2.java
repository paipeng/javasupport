package deng.javaexamples;

public enum MyEnum2 {
	FOO1(71, "foo1"),
	FOO2(72, "foo2");
	
	private int index;
	private String code;
	
	public int getIndex(){ return index; }
	public String getCode(){ return code; }
	
	MyEnum2(int index, String code) {
		this.index = index;
		this.code = code;
	}
}
