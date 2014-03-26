package controller;

import view.ThreadPool;
import model.PlayerModel;

public class AppController extends Controller {

	public static void main(String[] args) {
//		LevelDAO levelDAO = new LevelDAO();
//		Level level = new Level();
//		level.setId(1);
//
//		levelDAO.setBean(level);
//
//		ArrayList<Level> levels = new ArrayList<Level>();
//
//		levels = levelDAO.findAll();
		
//		PlayerDAO playerDAO = new PlayerDAO();
//		ArrayList<Player> players = new ArrayList<Player>();
//		
//		players = playerDAO.findAll(true);
				
//		PlayerModel playerModel = new PlayerModel();
//		System.out.println(playerModel.isLoggedIn(4, 2));
//		System.out.println(playerModel.isLoggedIn(4, 1));
//		
//		playerModel.createPlayer("PhilémonBG", "swagg", "I am a MAC address");
//		
		//System.out.println(playerModel.isLoggedIn(, 1));
		
//		for (Player player : players) {
//			System.out.println("Player n°" + player.getId());
//			System.out.println("    Games:");
//			for (Game game : player.getGameAssociations()) {
//				System.out.println("    " + game.getId());
//				System.out.println("    " + game.getScore());
//				System.out.println();
//			}
//			
//			System.out.println("    Devices:");
//			for (Device device : player.getDeviceAssociations()) {
//				System.out.println("    " + device.getId());
//				System.out.println("    " + device.getMacAddress());
//				System.out.println();
//			}
//			System.out.println();
//		}
//
//		System.out.println("Mother-fucking test!");

		ThreadPool server = new ThreadPool();
		new Thread(server).start();
		
		try {
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Arrêt du server.");
		server.stop();
	}

}
