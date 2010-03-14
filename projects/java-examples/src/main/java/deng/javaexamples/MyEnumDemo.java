package deng.javaexamples;

public class MyEnumDemo {

	public static void main(String[] args) {
		for(MyEnum2 e2 : MyEnum2.values()) {
			System.out.println(e2.name() + ": " + e2.ordinal() + ": " + e2.getIndex() + ": " + e2.getCode());
		}
		
		MyEnum2 e2 = MyEnum2.valueOf("FOO1");
		System.out.println(e2.name() + ": " + e2.ordinal() + ": " + e2.getIndex() + ": " + e2.getCode());
	}
	
	public static void main2(String[] args) {
		MyEnum e = MyEnum.FOO12;
		
		System.out.println(e);
		System.out.println(e.name());
		System.out.println(e.ordinal());
		
		//foo1 = MyEnum.valueOf("FOO1");
		e = MyEnum.valueOf("FOO13");
		System.out.println(e);
		
		System.out.println();
		for(MyEnum e2 : MyEnum.values()) {
			System.out.println(e2.name() + ": " + e2.ordinal());
		}
	}
}
