package com.example.jeedemo.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("pinValidator")
public class PinValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		String pesel = (String) value;
		
		FacesMessage message = new FacesMessage();
		message.setDetail("PIN should contain 8 digits");
		message.setSummary("PIN should contain 8 digits");
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		
		try{
			if (pesel.length() == 0){
				throw new ValidatorException(message);
			}
			if (pesel.length() != 8) {
				Integer.parseInt(pesel);
				throw new ValidatorException(message);
			}
			else{
			Integer.parseInt(pesel);
			}
		}
		catch(NumberFormatException e){
			message.setDetail("PIN should contain only digits");
			message.setSummary("PIN should contain only digits");
			throw new ValidatorException(message);
		}
		
		
		
	}
}
