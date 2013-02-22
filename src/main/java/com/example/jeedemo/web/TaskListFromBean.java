package com.example.jeedemo.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.jeedemo.domain.TaskList;
import com.example.jeedemo.service.TaskListManager;
import com.example.jeedemo.service.UserManager;

@SessionScoped
@Named("TasklistBean")
public class TaskListFromBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TaskList taskList = new TaskList();
	
	private ListDataModel<TaskList> taskLists = new ListDataModel<TaskList>();
	
	@Inject
	TaskListManager tlm;
	
	@Inject
	UserManager um;

	public TaskList getTaskList() {
		return taskList;
	}

	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	}
	
	public ListDataModel<TaskList> getAllTaskLists(){
		taskLists.setWrappedData(tlm.getAllTaskLists());
		return taskLists;
	}
	
	
	//Actions

	public String addTaskList(){
		//tlm.addTaskList(taskList);
		return null;
	}
	
	public String editTaskList(){
		tlm.editTaskList(taskList);
		return null;
	}
	
	public String deleteTaskList(){
		//tlm.deleteTaskList(taskList);
		return null;
	}
	
	public String addTask(){
		taskList = taskLists.getRowData();
		return "addTask?faces-redirect=true";
	}

}
