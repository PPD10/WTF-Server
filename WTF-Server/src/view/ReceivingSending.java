package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controller.AppController;

public class ReceivingSending implements Runnable {

	private Socket clientSocket = null;

	public ReceivingSending(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			AppController appController = new AppController();
			
			InputStream inputStream = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream();

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			PrintWriter printWriter = new PrintWriter(outputStream, true);

			Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			
			long time = System.currentTimeMillis();
			
			String className = bufferedReader.readLine();
			String json = bufferedReader.readLine();

			Gson gson = new GsonBuilder().create();

			Object o = gson.fromJson(json, Class.forName(className));
			
			if (o instanceof RegistrationRequest) {
				RegistrationResponse registrationResponse = 
						appController.register((RegistrationRequest) o);
				
				json = gson.toJson(registrationResponse);
				
				// Envoi
				printWriter.println("main.RegistrationResponse");
				printWriter.println(json);
			}

			System.out.println(json);
			System.out.println(o);

			outputStream.close();
			inputStream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
