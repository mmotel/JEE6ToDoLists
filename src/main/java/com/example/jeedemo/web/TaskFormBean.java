package com.example.jeedemo.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.jeedemo.domain.Task;
import com.example.jeedemo.service.TaskManager;

@SessionScoped
@Named("TaskBean")
public class TaskFormBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Task task = new Task();
	
	private ListDataModel<Task> tasks = new ListDataModel<Task>();

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	@Inject
	TaskManager tm;
	
	public ListDataModel<Task> getAllTasks(){
		tasks.setWrappedData(tm.getAllTasks());
		return tasks;
	}
	
	//Actions
	
	public String addTask(){
		//tm.addTask(task);
		return null;
	}
	
	public String editTask(){
		tm.editTask(task);
		return null;
	}
	
	public String deleteTask(){
		//tm.deleteTask(task);
		return null;
	}

}
