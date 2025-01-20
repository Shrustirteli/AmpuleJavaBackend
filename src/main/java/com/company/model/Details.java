package com.company.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "details")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String machine;
    private String operator;
    private String shift;
    private String ampuleType;
    private String ampuleSize;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
 		return id;
 	}

 	public void setId(Long id) {
 		this.id = id;
 	}

 	public String getMachine() {
 		return machine;
 	}

 	public void setMachine(String machine) {
 		this.machine = machine;
 	}

 	public String getOperator() {
 		return operator;
 	}

 	public void setOperator(String operator) {
 		this.operator = operator;
 	}

 	public String getShift() {
 		return shift;
 	}

 	public void setShift(String shift) {
 		this.shift = shift;
 	}

 	public String getAmpuleType() {
 		return ampuleType;
 	}

 	public void setAmpuleType(String ampuleType) {
 		this.ampuleType = ampuleType;
 	}

 	public String getAmpuleSize() {
 		return ampuleSize;
 	}

 	public void setAmpuleSize(String ampuleSize) {
 		this.ampuleSize = ampuleSize;
 	}

 	public User getUser() {
 		return user;
 	}

 	public void setUser(User user) {
 		this.user = user;
 	}

}
