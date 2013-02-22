package com.example.jeedemo.oldapp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;


@SessionScoped
@Named("personBeana")
public class PersonFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Person person = new Person();
	private ListDataModel<Person> persons = new ListDataModel<Person>();

	private Person personToShow = new Person();
	private ListDataModel<Car> ownedCars = new ListDataModel<Car>();

	@Inject
	private PersonManager pm;

	@Inject
	private SellingManager sm;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ListDataModel<Person> getAllPersons() {
		persons.setWrappedData(pm.getAllPersons());
		return persons;
	}

	public ListDataModel<Car> getOwnedCars() {
		ownedCars.setWrappedData(pm.getOwnedCars(personToShow));
		return ownedCars;
	}

	// Actions
	public String addPerson() {
		pm.addPerson(person);
		return "showPersons?faces-redirect=true";
		// return null;
	}

	public String deletePerson() {
		Person personToDelete = persons.getRowData();
		pm.deletePerson(personToDelete);
		return null;
	}

	public String editPerson() {
		person = persons.getRowData();
		return "edit?faces-redirect=true";
	}

	public String editedPerson() {
		pm.editPerson(person);
		return "showPersons?faces-redirect=true";
	}

	public String showDetails() {
		personToShow = persons.getRowData();
		return "details?faces-redirect=true";
	}

	public String disposeCar() {
		Car carToDispose = ownedCars.getRowData();
		sm.disposeCar(personToShow, carToDispose);
		return null;
	}

	// Business logic validation
	public void uniquePin(FacesContext context, UIComponent component,
			Object value) {

		String pin = (String) value;

		for (Person person : pm.getAllPersons()) {
			if (person.getPin().equalsIgnoreCase(pin)) {
				FacesMessage message = new FacesMessage(
						"Person with this PIN already exists in database");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	// Multi field validation with <f:event>
	// Rule: first six digits of PIN must match last two digits of the year of
	// birth, two digits 
	public void validatePinDob(ComponentSystemEvent event) {

		UIForm form = (UIForm) event.getComponent();
		UIInput pin = (UIInput) form.findComponent("pin");
		UIInput dob = (UIInput) form.findComponent("dob");

		if (pin.getValue() != null && dob.getValue() != null
				&& pin.getValue().toString().length() >= 6) {
			String twoDigitsOfPin = pin.getValue().toString().substring(0, 6);
			Calendar cal = Calendar.getInstance();
			cal.setTime(((Date) dob.getValue()));

			String lastDigitsOfDob = ((Integer) cal.get(Calendar.YEAR))
					.toString().substring(2);
			
			String monthDigitsOfDob;
			int monthOfDob;
			if((monthOfDob = ((Integer) cal.get(Calendar.MONTH)) + 1) <10){
				monthDigitsOfDob = "0" + ((Integer)monthOfDob).toString();
			}
			else{
				monthDigitsOfDob = ((Integer) monthOfDob).toString();
			}
			
			
			String dayDigitsOfDob;
			int dayOfDob;
			if((dayOfDob =(Integer) cal.get(Calendar.DAY_OF_MONTH)) < 10){
				dayDigitsOfDob = "0" + ((Integer) dayOfDob).toString();
			}
			else{
				dayDigitsOfDob = ((Integer) dayOfDob).toString();
			}

			String dateDigits = lastDigitsOfDob + monthDigitsOfDob + dayDigitsOfDob;
			if (twoDigitsOfPin.compareTo(dateDigits)!=0) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage("personForm:pin" , new FacesMessage("PIN dose not match date of bitrh."));
				context.renderResponse();
			}
		}
	}
}
