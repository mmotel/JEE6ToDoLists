package com.example.jeedemo.oldapp;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;


@SessionScoped
@Named("saleBeana")
public class SaleFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SellingManager sm;

	@Inject
	private PersonManager pm;

	private Long carId;
	private Long personId;
	
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public List<Car> getAvailableCars() {
		return sm.getAvailableCars();
	}

	public List<Person> getAllPersons() {
		return pm.getAllPersons();
	}

	public String sellCar() {
		sm.sellCar(personId, carId);
		return null;
	}
}
