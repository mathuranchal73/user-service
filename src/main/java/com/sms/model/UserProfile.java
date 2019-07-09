package com.sms.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.validation.constraints.Size;

import com.sms.model.audit.UserDateAudit;

@Entity
@Table(name = "user_profile")
public class UserProfile  extends UserDateAudit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	@Size(max = 200)
	private String displayPic;
	
	@Size(max = 100)
	private String address;
    private String city;
    @Size(max = 10)
    private String phoneNo;
    
    

	public UserProfile() {
	}


	
	
	public UserProfile(User user, @Size(max = 200) String displayPic, @Size(max = 100) String address, String city,
			@Size(max = 10) String phoneNo) {
		super();
		this.user = user;
		this.displayPic = displayPic;
		this.address = address;
		this.city = city;
		this.phoneNo = phoneNo;
	}




	public String getDisplayPic() {
		return displayPic;
	}
	public void setDisplayPic(String displayPic) {
		this.displayPic = displayPic;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        UserProfile userProfile = (UserProfile) o;
	        return Objects.equals(id, userProfile.id);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(id);
	    }
    
    
}
