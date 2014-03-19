package model.dao;

import model.bean.Player;

public class PlayerDAO extends DAO<Player> {

	public PlayerDAO() {
		super(new Player());
	}

}
