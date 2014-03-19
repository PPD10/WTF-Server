package model;

import model.bean.Bean;
import model.dao.DAO;

public class Model<T extends DAO<? extends Bean>> {
	
	private T dao;
	
	protected void setDAO(T dao) {
		this.dao = dao;
	}
	
	public T getDAO() {
		return this.dao;
	}
	
}