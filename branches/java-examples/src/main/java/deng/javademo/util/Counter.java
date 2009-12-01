//Counter for inner class
package deng.javademo.util;

/**
 * Just a counter bean.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class Counter {
    private int count = 0;
    public void inc() { count ++; }
    public int getCount() { return count; }
    public void reset() { count = 0; }
}
