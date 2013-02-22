package com.example.jeedemo.web;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("regDateValidator")
public class RegDateValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		Date regDate = (Date) value;

		if (regDate.after(new Date())) {
			FacesMessage message = new FacesMessage();
			message.setDetail("Can't be in the future.");
			message.setSummary("Can't be in the future.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}

}
