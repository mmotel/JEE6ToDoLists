package com.example.jeedemo.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.jeedemo.domain.Task;
import com.example.jeedemo.domain.TaskList;
import com.example.jeedemo.domain.User;
import com.example.jeedemo.domain.UsrGroup;

@Stateless
public class UserManager {

	@PersistenceContext
	EntityManager um;

	//Calculating SHA256 hex code
	public String passwordSHA256(String password) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");

			try {
				digest.update(password.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return "";
			}
			byte[] pass = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pass.length; i++) {
				sb.append(Integer.toString((pass[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e1) {
			return "";
		}
	}

	public void addUser(User originalUser) throws NoSuchAlgorithmException {
		User user = new User();
		user.setEmail(originalUser.getEmail());
		user.setLists(originalUser.getLists());
		user.setUser_login(originalUser.getUser_login());
		user.setUser_password(originalUser.getUser_password());
		user.setEmail(originalUser.getEmail());
		user.setDateOfBirth(originalUser.getDateOfBirth());
		user.setAnswer(originalUser.getAnswer());
		user.setQuestion(originalUser.getQuestion());

		user.setUser_password(passwordSHA256(originalUser.getUser_password()));

		// adding user to User entity
		user.setId(null);
		um.persist(user);

		// adding user to UsrGroup
		UsrGroup grupa = new UsrGroup();
		grupa.setGroupName("USERS");
		grupa.setUser_login(user.getUser_login());
		grupa.setId(null);
		um.persist(grupa);
	}

	public void editUser(User user) {
		um.merge(user);
	}

	public void deleteUser(User user) {
		User userToRemove = um.find(User.class, user.getId());
		UsrGroup usrGruopToRemove = (UsrGroup) um
				.createNamedQuery("UsrGroup.findByUser")
				.setParameter("login", user.getUser_login()).getSingleResult();
		um.remove(userToRemove);
		um.remove(usrGruopToRemove);
	}
	
	//---

	public User findUser(String login) {
		try{
		User u =(User) um.createNamedQuery("user.findByLogin")
				.setParameter("login", login).getSingleResult();
		return u;
		}
		catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return um.createNamedQuery("user.all").getResultList();
	}

	public List<TaskList> getOwnedTaskLists(String userLogin) {
		User user = findUser(userLogin);
		um.find(User.class, user.getId());
		List<TaskList> TaskLists = new ArrayList<TaskList>(user.getLists());
		return TaskLists;
	}
	
	
	public List<Task> getIncomingTasks(String userLogin) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 7);
		Date nextWeek = cal.getTime();
		
		User user = findUser(userLogin);
		um.find(User.class, user.getId());
		List<TaskList> TaskLists = new ArrayList<TaskList>(user.getLists());
		List<Task> incomingTasks = new ArrayList<Task>();
		for(TaskList tl : TaskLists){
			TaskList wtl = um.find(TaskList.class, tl.getId());
			List<Task> tasks = wtl.getTasks();
			for(Task t : tasks){
				if(t.getDeadline().before(nextWeek) && !t.getDone()){
					incomingTasks.add(t);
				}
			}
		}
		Collections.sort(incomingTasks);
		return incomingTasks;
		
	}


}
