package model;

import model.bean.Level;
import model.dao.LevelDAO;

public class LevelModel extends Model<LevelDAO> {

	public LevelModel() {
		setDAO(new LevelDAO());
	}

	public boolean createLevel(int maxScore, int idGame, int idPlayer) {
		Level level = new Level(maxScore, idGame, idPlayer);
		getDAO().setBean(level);
		
		return getDAO().save();
	}

	public boolean deleteLevel(int id) {
		Level level = new Level();
		level.setId(id);
		getDAO().setBean(level);
		
		return getDAO().delete();
	}

	public boolean uploadLevel(int id, int maxScore, int idGame, int idPlayer) {
		Level level = new Level(maxScore, idGame, idPlayer);
		level.setId(id);
		getDAO().setBean(level);
		
		return getDAO().save();
	}
	
}
