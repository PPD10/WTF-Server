package model;

import java.util.ArrayList;

import model.bean.Device;
import model.bean.Player;
import model.dao.DeviceDAO;
import model.dao.PlayerDAO;

public class PlayerModel extends Model<PlayerDAO> {

	public PlayerModel() {
		setDAO(new PlayerDAO());
	}

	public boolean isLoggedIn(int idDevice, int idPlayer) {
		boolean isLoggedIn = false;
		
		ArrayList<Player> players = getDAO().findAll(true, 
				"device.id = " + idDevice);
		
		for (Player player : players) {
			if (player.getId().equals(idPlayer))
				isLoggedIn = true;
		}
		
		return isLoggedIn;
	}

	public boolean addDevice(int id, String macAddress) {
		Player player = new Player();
		player.setId(id);
		getDAO().setBean(player);
		player = getDAO().find(false);
		
		if (player != null) {
			//player.addMacAddress(macAddress);
			return true;
		}
		
		return false;
	}

	public boolean createPlayer(String username, String password,
			String macAddress) {
		Player player = new Player(username, password);

		getDAO().setBean(player);

		if (getDAO().save()) {
			DeviceModel deviceModel = new DeviceModel();
			deviceModel.createDevice(macAddress, player.getId());
			if (deviceModel.getDAO().save())
				return true;
		}

		return false;
	}

	public boolean changePassword(int id, String password) {
		Player player = new Player();
		player.setPassword(password);
		player.setId(id);
		getDAO().setBean(player);

		return getDAO().save();
	}

	public boolean deletePlayer(int id) {
		Player player = new Player();
		player.setId(id);
		getDAO().setBean(player);

		return getDAO().delete();
	}

}