package view;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool implements Runnable {

	public final static int PORT = 2000;
	public final static int THREAD_POOL_SIZE = 100;

	private ServerSocket serverSocket;
	private boolean isStopped;
	private Thread runningThread;
	private ExecutorService threadPool = 
			Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	@Override
	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}

		openServerSocket();

		while (!isStopped()) {
			Socket clientSocket = null;

			try {
				clientSocket = this.serverSocket.accept();				
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Serveur arrêté.");
					return;
				}
				throw new RuntimeException("Erreur à la connexion.", e);
			}
			
			this.threadPool.execute(new ReceivingSending(clientSocket));
		}
		
		this.threadPool.shutdown();
		System.out.println("Serveur arrêté.");
	}
	
	protected void init() {
		
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;

		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Erreur à la fermeture du serveur.", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			throw new RuntimeException("Port " + PORT + " non disponible.", e);
		}
	}

}
