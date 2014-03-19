package controller;

import java.util.ArrayList;

import model.bean.Level;
import model.dao.LevelDAO;

public class Controller {

	public static void main(String[] args) {
		LevelDAO levelDAO = new LevelDAO();
		Level level = new Level();
		// level.setId(1);

		ArrayList<Level> levels = new ArrayList<Level>();

		levels = levelDAO.findAll();
		
		System.out.println("Mother-fucking test!");
	}

}