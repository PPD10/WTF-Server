package main;

public class RegistrationResponse {

	private int idPlayer;
	private int idDevice;
	
	public RegistrationResponse(int idPlayer, int idDevice) {
		this.idPlayer = idPlayer;
		this.idDevice = idDevice;
	}
	
	public String toString() {
		return this.idPlayer + ", " + this.idDevice;
	}
	
}
