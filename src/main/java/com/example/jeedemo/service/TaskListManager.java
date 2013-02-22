package com.example.jeedemo.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.jeedemo.domain.Task;
import com.example.jeedemo.domain.TaskList;
import com.example.jeedemo.domain.User;

@Stateless
public class TaskListManager {
	
	@PersistenceContext
	EntityManager tlm;
	
	public void addTaskList(TaskList taskList, User user){
		User workUser = tlm.find(User.class, user.getId());
		TaskList listToAdd = new TaskList();
		listToAdd = taskList;
		listToAdd.setId(null);
		workUser.getLists().add(listToAdd);
	}
	
	public void editTaskList(TaskList taskList){
		tlm.merge(taskList);
	}
	
	public void deleteTaskList(TaskList taskList, User user){
		User workUser = tlm.find(User.class, user.getId());
		TaskList listToRemove = tlm.find(TaskList.class, taskList.getId());
		workUser.getLists().remove(listToRemove);
		tlm.remove(listToRemove);
	}
	
	//---
	
	@SuppressWarnings("unchecked")
	public List<TaskList> getAllTaskLists(){
		return tlm.createNamedQuery("taskList.all").getResultList();
	}
	
	public List<Task> getContainingTasks(TaskList taskList){
		TaskList tl =  tlm.find(TaskList.class, taskList.getId());
		List<Task> containingTasks = new ArrayList<Task>(tl.getTasks());
		return containingTasks;
	}
	
	public TaskList getTaskListByTask(User user, Task task){
		Task t = tlm.find(Task.class, task.getId());
		User u = tlm.find(User.class, user.getId());
		for(TaskList tl : u.getLists()){
			TaskList wtl = tlm.find(TaskList.class, tl.getId());
			if(wtl.getTasks().contains(t)){
				return wtl;
			}
		}
		return null;
	}
	
	public TaskList getTaskListById(TaskList tl){
		return (TaskList) tlm.createNamedQuery("taskList.findById").setParameter("id", tl.getId()).getSingleResult();
	}

}
