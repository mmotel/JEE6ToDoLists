package com.example.jeedemo.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
	@NamedQuery(name = "task.all", query = "select t from Task t"),
		@NamedQuery(name = "task.findById", query = "select t from Task t where id = :id")
})
public class Task implements Comparable<Task>{
	
	private Long id;
	
	private String name;
	private String info;
	private Date deadline = new Date();
	private Integer priority;
	private String status;
	private Boolean done;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Size(min = 6, max = 32)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Size(max = 256)
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	

	@Size(max = 32)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean getDone() {
		return done;
	}
	public void setDone(Boolean done) {
		this.done = done;
	}
	@Override
	public int compareTo(Task o) {
		if(this.getDeadline().after(o.getDeadline())){ 
			return 1;
		}
		else if(this.getDeadline().before(o.getDeadline())){
			return -1;
		}
		else{ 
			if(this.getPriority()<o.getPriority()){
				return 1;
			}
			else if(this.getPriority()>o.getPriority()){
				return -1;
			}
			else return 0;
		}
	}
	
	
	
	
	

}
