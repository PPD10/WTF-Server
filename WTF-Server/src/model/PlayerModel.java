package model;

import model.dao.PlayerDAO;

public class PlayerModel extends Model<PlayerDAO> {

	public PlayerModel() {
		setDAO(new PlayerDAO());
	}
	
	public boolean isLoggedIn() {
		
	}
	
}
