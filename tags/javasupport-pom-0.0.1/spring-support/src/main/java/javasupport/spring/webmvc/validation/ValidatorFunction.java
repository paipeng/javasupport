/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javasupport.spring.webmvc.validation;

import org.springframework.validation.Errors;

/**
 *
 * @author thebugslayer
 */
public interface ValidatorFunction {
    public void apply(String fieldName, Object fieldValue, Errors errors);
}
