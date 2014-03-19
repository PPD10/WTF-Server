package model;

import model.bean.Bean;
import model.dao.DAO;

public class AppModel extends Model<DAO<? extends Bean>> {

	public AppModel(DAO<? extends Bean> dao) {
		setDAO(dao);
	}
	
}
