package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;


//import play.db.ebean.Model;
import com.avaje.ebean.Model;

@Entity
public class Flight extends Model
{
	public static Finder<Long,Flight> find = new Finder(Long.class, Flight.class);  

	public static List<Flight> all() {
		return find.all();
	}
	public static void delete(Long id) {
		find.ref(id).delete();
	}

	@Id 
	private Long id;
	public Long getId() {
		return this.id;
	}
	public void setId(Long val) {
		this.id = val;
	}
	
	private String s;
	private Long userId;
	private int size;
	private String lang;
	private boolean isAdmin;
	private Date startDate;
	private Long accountTypeId;
	private List<String> emails;

	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Long getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
}
