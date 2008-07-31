/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javasupport.spring.webmvc.validation;

import org.springframework.validation.Errors;

/**
 *
 * @author zemian
 */
public class SkipableFieldValidator extends FieldValidator {
    public SkipableFieldValidator(Object command, Errors errors){
        super(command, errors);
    }
    
    public SkipableFieldValidator(FieldValidator that){
        super(that.getCommandBean(), that.errors);
    }
}
