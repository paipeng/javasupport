package deng.javademo;

import deng.javademo.util.OptionsParserSupport;

/**
 * Sample of OptionParserSupport Usage.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class App extends OptionsParserSupport {
    /////////////////////////////////////////////////////////////////////////
    // Setters for OptionsParserSupport
    /////////////////////////////////////////////////////////////////////////
    private boolean debug= false;
    public void debug(String flag) { debug= Boolean.valueOf(flag); }
    
    /////////////////////////////////////////////////////////////////////////
    // Main Class
    /////////////////////////////////////////////////////////////////////////
    public void run(String[] args) {
        if(debug)
            System.out.println("show debug");        
        System.out.println("running");
    }
        
    /////////////////////////////////////////////////////////////////////////
    // Main Program Entry
    /////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
    	App main = new App();
        String[] newArgs= parseAndSetOptionsObject(main, args);
        main.run(newArgs);
    }
}

