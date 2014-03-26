package model;

import model.bean.Game;
import model.dao.GameDAO;

public class GameModel extends Model<GameDAO> {

	public GameModel() {
		setDAO(new GameDAO());
	}

	public boolean createGame(int score, int livesNumber, int time, int idPlayer) {
		Game game = new Game(score, livesNumber, time, idPlayer);
		getDAO().setBean(game);

		return getDAO().save();
	}

	public boolean deleteGame(int id) {
		Game game = new Game();
		game.setId(id);
		getDAO().setBean(game);

		return getDAO().delete();
	}

	public boolean uploadGame(int id, int score, int livesNumber, int time,
			int idPlayer) {
		Game game = new Game(score, livesNumber, time, idPlayer);
		game.setId(id);
		getDAO().setBean(game);

		return getDAO().save();
	}

}
