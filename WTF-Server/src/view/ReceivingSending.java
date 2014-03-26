package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivingSending implements Runnable {
	
	private Socket clientSocket = null;
	
	public ReceivingSending(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			InputStream inputStream = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream();
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			PrintWriter printWriter = new PrintWriter(outputStream, true);
			
			long time = System.currentTimeMillis();
			
			String s = bufferedReader.readLine();
			
			printWriter.println("This is a message from the Server");
			
			System.out.println(s);
			
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
