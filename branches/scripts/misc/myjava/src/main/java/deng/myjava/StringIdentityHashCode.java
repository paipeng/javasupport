package deng.myjava;

public class StringIdentityHashCode {
  public static void main(String[] args) {
    String s = null;
    
    s = "jtest";
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
    s = "jtest";
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
    s = "jtest";
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
    
    s = new String("jtest");
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
    s = new String("jtest");
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
    s = new String("jtest");
    System.out.println("hashCode=" + s.hashCode() + ", identityHashCode=" + System.identityHashCode(s) + ", identityHashCode(intern)=" + System.identityHashCode(s.intern()));
  }
}
