package auth.kayodeo1.com;

public class Instance {
	String email;
	int code;
	long time;
	public Instance(String email, int code, long time) {
		super();
		this.email = email;
		this.code = code;
		this.time = time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Instance [email=" + email + ", code=" + code + ", time=" + time + "]";
	}

}