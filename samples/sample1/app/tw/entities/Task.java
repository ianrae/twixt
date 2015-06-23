package tw.entities;

import org.thingworld.entity.BaseEntity;

public class Task extends BaseEntity
{
	private String s;
	private Long userId;

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
}