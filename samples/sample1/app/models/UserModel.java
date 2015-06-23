package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;
//import play.db.ebean.Model;
import com.avaje.ebean.Model;

@Entity
public class UserModel extends Model
{


	public static Finder<Long,UserModel> find = new Finder(Long.class, UserModel.class);  

	public static List<UserModel> all() {
		return find.all();
	}
	public static void delete(Long id) {
		find.ref(id).delete();
	}

	//getters and setters

	@Id 
	private Long id;
	public Long getId() {
		return this.id;
	}
	public void setId(Long val) {
		this.id = val;
	}

	@Required 
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String val) {
		this.name = val;
	}
	
	@Required 
	private String firstName;
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String val) {
		this.firstName = val;
	}

	@Required 
	private String lastName;
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String val) {
		this.lastName = val;
	}

	private Long subjId;
	public Long getSubjId() {
		return this.subjId;
	}
	public void setSubjId(Long val) {
		this.subjId = val;
	}

	private String pwd;
	public String getPwd() {
		return this.pwd;
	}
	public void setPwd(String val) {
		this.pwd = val;
	}

	@Required 
	private String email;
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String val) {
		this.email = val;
	}
	
	@Required 
	private boolean confirmed;
	public boolean getConfirmed() {
		return this.confirmed;
	}
	public void setConfirmed(boolean val) {
		this.confirmed = val;
	}

	@Required 
	private int triflag;
	public int getTriflag() {
		return this.triflag;
	}
	public void setTriflag(int val) {
		this.triflag = val;
	}
	
	@Required 
	private int userType;
	public int getUserType() {
		return this.userType;
	}
	public void setUserType(int val) {
		this.userType = val;
	}

	//authorziation
	@Required 
	private String srtHint;
	public String getSrtHint() {
		return this.srtHint;
	}
	public void setSrtHint(String val) {
		this.srtHint = val;
	}
	
	
	@Version  
	Timestamp lastMod;  
	public Timestamp getTimestamp()
	{
		return lastMod;
	}
}
