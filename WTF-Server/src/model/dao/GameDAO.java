package model.dao;

import model.bean.Game;

public class GameDAO extends DAO<Game> {

	public GameDAO() {
		super(new Game());
	}

}
