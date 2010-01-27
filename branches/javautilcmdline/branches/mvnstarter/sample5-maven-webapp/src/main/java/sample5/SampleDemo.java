/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sample5;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author thebugslayer
 */
public class SampleDemo {
    public static String[] split(String input){
        return StringUtils.splitByWholeSeparator(input, "longsep");
    }
}
