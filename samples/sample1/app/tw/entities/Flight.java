package tw.entities;

import java.util.Date;
import java.util.List;

import org.thingworld.entity.BaseEntity;

public class Flight extends BaseEntity
{
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
		setlist.add("s");
		this.s = s;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		setlist.add("userId");
		this.userId = userId;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		setlist.add("size");
		this.size = size;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		setlist.add("lang");
		this.lang = lang;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		setlist.add("isAdmin");
		this.isAdmin = isAdmin;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		setlist.add("startDate");
		this.startDate = startDate;
	}
	public Long getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(Long accountTypeId) {
		setlist.add("accountTypeId");
		this.accountTypeId = accountTypeId;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		setlist.add("emails");
		this.emails = emails;
	}
}