package model.bean;

public class Device extends Bean {

	private String macAddress;
	private Integer idPlayer;
	
	static {
		belongsTo(Player.class);
	}
	
	public Device() {
	}
	
	public Device(String macAddress, Integer idPlayer) {
		this.macAddress = macAddress;
		this.idPlayer = idPlayer;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Integer getIdPlayer() {
		return idPlayer;
	}

	public void setIdPlayer(Integer idPlayer) {
		this.idPlayer = idPlayer;
	}
	
}
