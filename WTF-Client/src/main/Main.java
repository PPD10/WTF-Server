package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Main {

	public static void main(String[] args) {
		try {
			// Connexion au serveur
			Socket socket = new Socket("localhost", 2000);

			// Création des Writer/Reader
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),
					true);

			// Création d'un objet de demande d'inscription
			RegistrationRequest registrationRequest = new RegistrationRequest(
					"userTest", "mdp", "qsdfqsdf");

			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(registrationRequest); // Chaine du JSON

			// Envoi
			printWriter.println("view.RegistrationRequest"); // Nom de la classe
			printWriter.println(json); // Objet sérialisé

			// Réception
			String className = bufferedReader.readLine();
			json = bufferedReader.readLine();

			// Objet de réception
			Object o = gson.fromJson(json, Class.forName(className));

			if (o instanceof RegistrationResponse) {
				// On manipule l'objet o
			}
			
			// Ne pas oublier de fermer ce que l'on à ouvert
			printWriter.close();
			bufferedReader.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
