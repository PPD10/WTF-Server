package main;

public class RegistrationRequest {

	private String username;
	private String password;
	private String macAddress;
	
	public RegistrationRequest(String username, String password, String macAddress) {
		this.username = username;
		this.password = password;
		this.macAddress = macAddress;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getMacAddress() {
		return this.macAddress;
	}
	
	public String toString() {
		return username + ", " + password + ", " + macAddress;
	}
	
}
