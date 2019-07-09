package com.sms.model.audit;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "updatedBy"},
        allowGetters = true
)
public abstract class UserDateAudit extends DateAudit {
	
	 @CreatedBy
	private Long CreatedBy;
	 @LastModifiedBy
	private Long Updatedby;
	 
	 
	public Long getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(Long createdBy) {
		CreatedBy = createdBy;
	}
	public Long getUpdatedby() {
		return Updatedby;
	}
	public void setUpdatedby(Long updatedby) {
		Updatedby = updatedby;
	}
	 
	 
	 

}
