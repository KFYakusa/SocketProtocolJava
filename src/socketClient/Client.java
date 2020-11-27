package socketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utilConnection.Mensagem;
import utilConnection.ReplyStatus;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 5555);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			Mensagem m = new Mensagem("HELLO");
			String operacao = m.getOperacao();
			BufferedReader reader;
			String sysInput = null;
			Boolean logado = false;
			Boolean admin = false;
//			System.out.println(m);
			System.out.println("Cliente, antes de enviar a primeira mensagem");
			output.writeObject(m);
			output.flush(); // enviando pro servidor a mensagem
			System.out.println("Cliente, antes de receber o reply");
			m = (Mensagem) input.readObject();// recebendo as mensagens REPLY
//			System.out.println("resposta do servidor: ");// mostrando as REPLY
			System.out.println(m.getParam("response"));
			operacao = m.getOperacao();// Pegando a operacao da REPLY
			
			do  {
				
				// mostrar menu a cada passagem
				// receber entrada
				// escolher qual das opções,
				// com base nas opções escrever uma mensagem condizente
				// enviar a mensagem.
				// ouvir o reply,
				// iterar novamente

				System.out.println("MENU:");
				reader = new BufferedReader(new InputStreamReader(System.in));
				if(!logado) {
					System.out.print("|--------------------|\n"
							       + "|	    	         |\n"
							       + "|       Login        |\n"
							       + "|                    |\n"
							       + "|--------------------|\n"
							       + " First Name: ");	
					

					// Reading data using readLine
					m = new Mensagem("LOGIN");
					sysInput = reader.readLine();
					
					if (sysInput.length() > 0) {
						m.setParam("nome", sysInput);
						sysInput = null;
					}
					
					System.out.println("Password:");
					sysInput = reader.readLine();
					
					if (sysInput.length() > 0) {
						System.out.println(sysInput);
						m.setParam("senha", sysInput);
						sysInput = null;
					}
				}else if(!admin) {
					System.out.println("|--------------------|\n"
									 + "|1-LIST CANDIDATOS   |\n"
									 + "|2-VOTE     3-LOGOUT |\n"
									 + "|4-CONSULT RESULT    |\n"
									 + "|--------------------|\n"
									 + "Option: ");
					sysInput = reader.readLine();
					switch (sysInput) {
					case "1":{
						m = new Mensagem("LISTCANDIDATOS");
						
						
						break;
					}case "2":{
						m = new Mensagem("VOTE");
						
						
						break;
					}case "3":{
						m = new Mensagem("LOGOUT");
						
						
						break;
					}case "4":{
						m = new Mensagem("CONSULTRESULT");
						
						
						break;
					}default:{
						
						System.out.println("THERE IS NO HIDDEN OPTION");
						
						break;
					}
					
					}// end of Switch(sysInput)
				}else { // start of admin menu
					System.out.println("|--------------------|\n"
							 + "|1-LIST CANDIDATOS   |\n"
							 + "|2-VOTE     3-LOGOUT |\n"
							 + "|4-CONSULT RESULT    |\n"
							 + "|5-START VOTE        |\n"
							 + "|6-STOP VOTE         |\n"
							 + "|7-ADD CANDIDATO     |\n"
							 + "|--------------------|\n"
							 + "Option: ");
					sysInput = reader.readLine();
					switch (sysInput) {
					case "1":{
						m = new Mensagem("LISTCANDIDATOS");
						
						
						break;
					}case "2":{
						m = new Mensagem("VOTE");
						
						
						break;
					}case "3":{
						m = new Mensagem("LOGOUT");
						
						
						break;
					}case "4":{
						m = new Mensagem("CONSULTRESULT");
						
						
						break;
					}case "5":{
						m = new Mensagem("STARTVOTE");
						
						
						break;
					}case "6":{
						m = new Mensagem("STOPVOTE");
						
						
						break;
					}case "7":{
						m = new Mensagem("ADDCANDIDATO");
						
						
						break;
					}
					
					default:{
						
						System.out.println("THERE IS NO HIDDEN OPTION");
						
						break;
					}
				}// end of admin menu
				
				
				if (sysInput.equals("1")) {

					

				} else if (sysInput.equals("2")) {
					m = new Mensagem("LIST");

				} else if (sysInput.equals("3")) {
					m = new Mensagem("DOWNLOAD");
					System.out.println("arquivo:");
					sysInput = reader.readLine();

					if (sysInput.length() > 0) {
						m.setParam("arquivo", sysInput);
						sysInput = null;
					}

				} else if (sysInput.equals("4")) {
					m = new Mensagem("UPLOAD");
					System.out.println("arquivo:");
					sysInput = reader.readLine();

					if (sysInput.length() > 0) {
						m.setParam("arquivo", sysInput);
						sysInput = null;
					}
					System.out.println("caminho?:");
					sysInput = reader.readLine();
					if (sysInput.length() > 0) {
						m.setParam("caminho", sysInput);
						sysInput = null;
					}
				} else if (sysInput.equals("5")) {
					m = new Mensagem("LOGOUT");
					
				}

				System.out.println("Enviando pro servirod");
				output.writeObject(m);

				output.flush(); // enviando pro servidor a mensagem
				System.out.println("abaixo está sendo recebida a mensagem do servidor");
				m = (Mensagem) input.readObject();// recebendo as mensagens REPLY
				System.out.println("era pra ta executando essa ação de receber a mensagem");
				System.out.println(m.getParam("response"));
				System.out.println("resposta do servidor: " + m);// mostrando as REPLY
				operacao = m.getOperacao();// PEgando a operacao da REPLY
				System.out.println(operacao);
				
				if(operacao == "LOGINREPLY" && m.getStatus() == ReplyStatus.OK) {
					logado = true;
					admin = false;
				}else if(operacao == "ADMINLOGINREPLY" && m.getStatus() == ReplyStatus.OK) {
					logado = true;
					admin = true;
				}
			}while (!operacao.equals("LOGOUTREPLY"));//end of while

			/*
			 * if (m.getStatus() == Status.OK) { String resposta = (String)
			 * m.getParam("mensagem"); System.out.println("Reply: \n" + m.toString()); }
			 * else { System.out.println("erro: " + m.getStatus()); }
			 */
			input.close();
			output.close();
			socket.close();
			return;
		} catch (IOException e) {
			System.out.println("error in IO exception:");
			System.out.println(e);
		} catch (ClassNotFoundException e) {

		}

	}
}
