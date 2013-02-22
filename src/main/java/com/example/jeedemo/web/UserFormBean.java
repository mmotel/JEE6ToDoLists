package com.example.jeedemo.web;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import com.example.jeedemo.domain.User;
import com.example.jeedemo.service.UserManager;

@SessionScoped
@Named("UserBean")
public class UserFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user = new User();
	private User logedInUser = new User();
	private String repratePassword;
	private String answer;
	private String tmpPassword;

	private ListDataModel<User> users = new ListDataModel<User>();

	@Inject
	UserManager um;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTmpPassword() {
		return tmpPassword;
	}

	public void setTmpPassword(String tmpPassword) {
		this.tmpPassword = tmpPassword;
	}

	@Size(min = 8, max = 36)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getRepratePassword() {
		return repratePassword;
	}

	public void setRepratePassword(String repratePassword) {
		this.repratePassword = repratePassword;
	}

	public ListDataModel<User> getAllUsers() {
		users.setWrappedData(um.getAllUsers());
		return users;
	}

	public User getLogedInUser() {
		String userName = FacesContext.getCurrentInstance()
				.getExternalContext().getUserPrincipal().getName();
		logedInUser = um.findUser(userName);
		return logedInUser;
	}

	// Actions

	public String forgotPasswordStepOne() {
		User u = um.findUser(user.getUser_login());
		if (u != null) {
			user = u;
			return "forgotPasswordTwo";

		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("userPassForm:login", new FacesMessage(
					"User with this LOGIN don't exists."));
			context.renderResponse();
			return null;
		}
	}

	public String forgotPasswordStepTwo() {
		if (answer.compareTo(user.getAnswer()) == 0) {
			answer = "";
			String currentPass = user.getUser_password();
			Random rn = new Random();
			Integer n = currentPass.length() - 8;
			Integer s = rn.nextInt(n);
			String tmpPass = currentPass.substring(s, s+8);
			user.setUser_password(um.passwordSHA256(tmpPass));
			um.editUser(user);
			tmpPassword = tmpPass;
			return "forgotPasswordThree";
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("userPassForm:answer", new FacesMessage(
					"Answer don't match."));
			context.renderResponse();
			return null;
		}
	}

	public String addUser() {
		try {
			um.addUser(user);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return "app/home.jsf?faces-redirect=true";
	}

	public String editUser() {
		um.editUser(user);
		return null;
	}

	public String deleteUser() {
		um.deleteUser(user);
		return null;
	}

	// Buisness logic

	public void uniqueLogin(FacesContext context, UIComponent component,
			Object value) {

		String login = (String) value;

		for (User u : um.getAllUsers()) {
			if (u.getUser_login().compareToIgnoreCase(login) == 0) {
				FacesMessage message = new FacesMessage(
						"User with this LOGIN already exists");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	// Multifield validation - password & repeat password match
	public void validatePasswords(ComponentSystemEvent event) {

		// UIForm form = (UIForm) event.getComponent();
		// UIInput pass = (UIInput) form.findComponent("password");
		// UIInput rePass = (UIInput) form.findComponent("rePassword");

		if (user.getUser_password().compareTo(repratePassword) != 0) {

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("userForm:rePassword", new FacesMessage(
					"Passwords don't match. Pleas type passwords again."));
			context.renderResponse();
		}
	}

}
