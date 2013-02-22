package com.example.jeedemo.web;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;

import com.example.jeedemo.domain.Task;
import com.example.jeedemo.domain.TaskList;
import com.example.jeedemo.domain.User;
import com.example.jeedemo.service.TaskListManager;
import com.example.jeedemo.service.TaskManager;
import com.example.jeedemo.service.UserManager;

@SessionScoped
@Named("AppBean")
public class AppFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user = new User();
	private User logedInUser = new User();

	private TaskList taskList = new TaskList();
	private TaskList taskListToAdd = new TaskList();
	private ListDataModel<TaskList> taskLists = new ListDataModel<TaskList>();

	private Task task = new Task();
	private ListDataModel<Task> tasks = new ListDataModel<Task>();

	private String password;
	private String newPassword;
	private String reNewPassword;

	@Inject
	UserManager um;

	@Inject
	TaskListManager tlm;

	@Inject
	TaskManager tm;

	@Size(min = 3)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Size(min = 6)
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Size(min = 6)
	public String getReNewPassword() {
		return reNewPassword;
	}

	public void setReNewPassword(String reNewPassword) {
		this.reNewPassword = reNewPassword;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public TaskList getTaskList() {
		return taskList;
	}

	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	}

	public TaskList getTaskListToAdd() {
		return taskListToAdd;
	}

	public void setTaskListToAdd(TaskList taskListToAdd) {
		this.taskListToAdd = taskListToAdd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// ---
	public User getLogedInUser() {
		String userName = FacesContext.getCurrentInstance()
				.getExternalContext().getUserPrincipal().getName();
		logedInUser = um.findUser(userName);
		return logedInUser;
	}

	public ListDataModel<TaskList> getUserTaskLists() {
		taskLists.setWrappedData(um.getOwnedTaskLists(getLogedInUser()
				.getUser_login()));
		return taskLists;
	}

	public ListDataModel<Task> getListTasks() {
		tasks.setWrappedData(tlm.getContainingTasks(taskList));
		return tasks;
	}

	public ListDataModel<Task> getListIncomingTasks() {
		tasks.setWrappedData(um.getIncomingTasks(getLogedInUser()
				.getUser_login()));
		return tasks;
	}

	// Actions

	// TaskLists
	public String addTaskList() {
		taskListToAdd.setId(null);
		tlm.addTaskList(taskListToAdd, getLogedInUser());
		return "userTaskLists";
	}

	public String showTasks() {
		taskList = taskLists.getRowData();
		return "userTasks";
	}

	public String showIncomingTaskOnTaskList() {
		taskList = tlm.getTaskListByTask(getLogedInUser(), tasks.getRowData());
		return "userTasks";
	}

	public String editTaskList() {
		taskList = taskLists.getRowData();
		return "editTaskList";
	}

	public String editedTaskList() {
		tlm.editTaskList(taskList);
		return "userTaskLists";
	}

	public String deleteTaskList() {
		TaskList tlToDelete = taskLists.getRowData();
		tlm.deleteTaskList(tlToDelete, getLogedInUser());
		return null;
	}

	// Tasks
	public String makeTaskDone(){
		task = tasks.getRowData();
		task.setDone(true);
		tm.editTask(task);
		return null;
	}
	
	public String addTask() {
		taskList = taskLists.getRowData();
		return "addTask";
	}

	public String addingTask() {
		task.setId(null);
		tm.addTask(task, taskList, getLogedInUser());
		return "userTasks";
	}

	public String editTask() {
		task = tasks.getRowData();
		return "editTask";
	}

	public String editIncomingTask() {
		task = tasks.getRowData();
		taskList = tlm.getTaskListByTask(getLogedInUser(), task);
		return "editTask";
	}

	public String editedTask() {
		tm.editTask(task);
		return "userTasks";
	}

	public String deleteTask() {
		Task t = tasks.getRowData();
		tm.deleteTask(t, taskList, getLogedInUser());
		return null;
	}

	public String deleteIncomingTask() {
		Task t = tasks.getRowData();
		TaskList tl = tlm.getTaskListByTask(getLogedInUser(), t);
		tm.deleteIncomingTask(tl, t);
		return null;
	}

	// User

	public String editUser() {
		user = getLogedInUser();
		return "editUser";
	}

	public String editedUser() {
		um.editUser(user);
		return null;
	}

	public String deleteUser() {
		um.deleteUser(user);
		return "../logout.jsp?faces-redirect=true";
	}

	public String editUserPassword() {
		return "editUserPassword";
	}

	public String editedUserPassword() {
		String currentPassword = getLogedInUser().getUser_password();
		String passwordToCompare = um.passwordSHA256(password);
		if(currentPassword.compareTo(passwordToCompare) == 0){
			if(newPassword.compareTo(reNewPassword) == 0){
				user.setUser_password(um.passwordSHA256(newPassword));
				um.editUser(user);
			}
			else{
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage("userPasswordForm:newPassword", new FacesMessage(
						"Passwords don't match. Pleas type passwords again."));
				context.renderResponse();
				return null;
			}
			
		}
		else{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("userPasswordForm:password", new FacesMessage(
					"Password is not valid."));
			context.renderResponse();
			return null;
		}
		return "../logout.jsp?faces-redirect=true";
	}

	// Buisness logic

	public void uniqueTaskListName(FacesContext context, UIComponent component,
			Object value) {

		String name = (String) value;
		User u = getLogedInUser();
		for (TaskList tl : um.getOwnedTaskLists(u.getUser_login())) {
			if (tl.getName().compareTo(name) == 0) {
				FacesMessage message = new FacesMessage(
						"Task list with this NAME already exists");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	public void uniqueTaskListNameEdit(FacesContext context,
			UIComponent component, Object value) {
		TaskList tlFromDb = tlm.getTaskListById(taskList);
		String name = (String) value;
		User u = getLogedInUser();
		for (TaskList tl : um.getOwnedTaskLists(u.getUser_login())) {
			if (tl.getName().compareTo(name) == 0
					&& tlFromDb.getName().compareTo(name) != 0) {
				FacesMessage message = new FacesMessage(
						"Task list with this NAME already exists");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	public void uniqueTaskName(FacesContext context, UIComponent component,
			Object value) {

		String name = (String) value;
		TaskList tl = tlm.getTaskListById(taskList);
		for (Task t : tl.getTasks()) {
			if (t.getName().compareTo(name) == 0) {
				FacesMessage message = new FacesMessage(
						"Task with this NAME already exists on this task list.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	public void uniqueTaskNameEdit(FacesContext context, UIComponent component,
			Object value) {

		String name = (String) value;
		Task tFromDb = tm.getTaskById(task);
		TaskList tl = tlm.getTaskListById(taskList);
		for (Task t : tl.getTasks()) {
			if (t.getName().compareTo(name) == 0
					&& tFromDb.getName().compareTo(name) != 0) {
				FacesMessage message = new FacesMessage(
						"Task with this NAME already exists on this task list.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

}