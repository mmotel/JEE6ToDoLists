package com.example.jeedemo.web;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("deadLineDateValidator")
public class DeadLineDateValidator implements Validator {

	@SuppressWarnings("deprecation")
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		Date date = (Date) value;
		Long selectedDay = date.getTime();
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		Long currentDay = now.getTime() - (now.getTime() % 1000);

		if (currentDay>selectedDay) {
			FacesMessage message = new FacesMessage();
			message.setDetail("Can't be in the past.");
			message.setSummary("Can't be in the past.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}

}
