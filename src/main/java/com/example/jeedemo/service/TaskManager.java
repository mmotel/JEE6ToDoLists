package com.example.jeedemo.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.jeedemo.domain.Task;
import com.example.jeedemo.domain.TaskList;
import com.example.jeedemo.domain.User;

@Stateless
public class TaskManager {

	@PersistenceContext
	EntityManager tm;
	
	public void addTask(Task task, TaskList taskList, User user){
		tm.find(User.class, user.getId());
		TaskList ts =  tm.find(TaskList.class, taskList.getId());
		Task taskToAdd = new Task();
		taskToAdd = task;
		taskToAdd.setDone(false);
		ts.getTasks().add(taskToAdd);
	}
	
	public void editTask(Task task){
		tm.merge(task);
	}
	
	public void deleteTask(Task task, TaskList taskList, User user){
		tm.find(User.class, user.getId());
		TaskList ts = tm.find(TaskList.class, taskList.getId());
		Task taskToRemove = tm.find(Task.class, task.getId());
		ts.getTasks().remove(taskToRemove);
		tm.remove(taskToRemove);
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getAllTasks(){
		return tm.createNamedQuery("task.all").getResultList();
	}
	
	public void deleteIncomingTask(TaskList taskList, Task task){
		if(taskList != null){
			TaskList tl = tm.find(TaskList.class, taskList.getId());
			Task t = tm.find(Task.class, task.getId());
			tl.getTasks().remove(t);
			tm.remove(t);
		}
	}
	
	public Task getTaskById(Task t){
		return (Task) tm.createNamedQuery("task.findById").setParameter("id", t.getId()).getSingleResult();
	}
	
	
}
