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
			
			Mensagem m = null;
			Mensagem retornoMensagem = null;
			String operacao = m.getOperacao();
			BufferedReader reader;
			String sysInput = null;
			Boolean logado = false;
			Boolean admin = false;
			Boolean accepted = false;
//			System.out.println(m);
//			System.out.println("Cliente, antes de enviar a primeira mensagem");
//			output.writeObject(m);
//			output.flush(); // enviando pro servidor a mensagem
//			System.out.println("Cliente, antes de receber o reply");
//			m = (Mensagem) input.readObject();// recebendo as mensagens REPLY
////			System.out.println("resposta do servidor: ");// mostrando as REPLY
//			System.out.println(m.getParam("response"));
//			operacao = m.getOperacao();// Pegando a operacao da REPLY
			
			do  {
				
				// mostrar menu a cada passagem
				// receber entrada
				// escolher qual das opções,
				// com base nas opções escrever uma mensagem condizente
				// enviar a mensagem.
				// ouvir o reply,
				// iterar novamente
				//
				// Fast Index:
				// tests of level of authority and menus
				//   !logado
				//      ReadLine, create message LOGIN 
				//   !admin
				//      ReadLine, switch between ( LIST, VOTE, LOGOUT, CONSULTRESULT)
				//   admin
				//      ReadLine, switch between ( LIST, VOTE, LOGOUT, CONSULTRESULT, STARTVOTE, STOPVOTE, ADDCANDIDATOS)
				// Send message created above
				// Receive Reply message and show it
				// attribute "cookies" for authority level when login is succesful
				
				accepted = false;
				System.out.println("MENU:");
				reader = new BufferedReader(new InputStreamReader(System.in));
				if(!logado) {
					
					System.out.print("|--------------------|\n"
							       + "|	    	           |\n"
							       + "|       Login        |\n"
							       + "|                    |\n"
							       + "|--------------------|\n"
							       + " First Name: ");	
					//creating message for reply
					m = new Mensagem("LOGIN");
					// Reading data using readLine
					while(!accepted) {
						accepted = true;
						sysInput = reader.readLine();

						if (sysInput.length() > 0) {
							m.setParam("nome", sysInput);
							accepted = false;
							sysInput = null;
						}
						
						System.out.println("Password:");
						sysInput = reader.readLine();
						
						if (sysInput.length() > 0) {
							System.out.println(sysInput);
							m.setParam("senha", sysInput);
							accepted = false;
							sysInput = null;
						}	
						
					}
					
				}else if(!admin) {
					
					System.out.println("|--------------------|\n"
									 + "|1-LIST CANDIDATOS   |\n"
									 + "|2-VOTE     3-LOGOUT |\n"
									 + "|4-CONSULT RESULT    |\n"
									 + "|--------------------|\n"
									 + "Option: ");
					
					while(!accepted) {
						sysInput = reader.readLine();
						switch (sysInput) {
						case "1":{
							accepted = true;
							m = new Mensagem("LISTCANDIDATOS");
							break;
						}case "2":{
							m = new Mensagem("VOTE");
							sysInput = null;
							accepted = true;
							System.out.print("number of you candidate: ");
							
							sysInput = reader.readLine();
							
							if(sysInput.isBlank() || sysInput.isEmpty()) {
								System.out.println("oh, you should write a number!");
								accepted = false;
							}
							
							break;
						}case "3":{
							accepted = true;
							m = new Mensagem("LOGOUT");							
							break;
						}case "4":{
							accepted = true;
							m = new Mensagem("CONSULTRESULT");
							
							break;
						}default:{
							accepted = false;
							System.out.println("THERE IS NO HIDDEN OPTION");
							
							break;
						}
						
						}// end of Switch(sysInput)
					}
					
				}else { // start of admin menu
					System.out.print("|--------------------|\n"
							 	   + "|1-LIST CANDIDATOS   |\n"
							 	   + "|2-VOTE     3-LOGOUT |\n"
							 	   + "|4-CONSULT RESULT    |\n"
							 	   + "|5-START VOTE        |\n"
							 	   + "|6-STOP VOTE         |\n"
							 	   + "|7-ADD CANDIDATO     |\n"
							 	   + "|--------------------|\n"
							 	   + "Option: ");
					
					
					while(!accepted)
					sysInput = reader.readLine();
					
					switch (sysInput) {
					case "1":{
						m = new Mensagem("LISTCANDIDATOS");
						
						accepted = true;
						break;
					}case "2":{
						m = new Mensagem("VOTE");
						sysInput = null;
						accepted = true;
						System.out.print("number of you candidate: ");
						
						sysInput = reader.readLine();
						
						if(sysInput.isBlank() || sysInput.isEmpty()) {
							System.out.println("oh, you should write a number!");
							accepted = false;
						}
						break;
					}case "3":{
						m = new Mensagem("LOGOUT");
						
						accepted = true;
						break;
					}case "4":{
						m = new Mensagem("CONSULTRESULT");
						
						accepted = true;
						break;
					}case "5":{
						m = new Mensagem("STARTVOTE");
						accepted = true;
						
						break;
					}case "6":{
						m = new Mensagem("STOPVOTE");
						accepted = true;
						break;
					}case "7":{
						m = new Mensagem("ADDCANDIDATO");
						
						
						break;
					}
					
					default:{
						accepted = false;
						System.out.println("THERE IS NO HIDDEN OPTION");
						System.out.println("THERE IS ONLY NUMBERS, they are described in the menu");
						break;
					}// end of default
				}//end of Switch(sysInput);
				}// end of admin menu
				
				

				System.out.println("Enviando pro servidor");
				output.writeObject(m);
				output.flush(); // enviando pro servidor a mensagem
				// ---------------------------------------------------------------------- HERE FINISH WRITING

				System.out.println("abaixo está sendo recebida a mensagem do servidor");
				
				//------------------------------------------------------- HERE START LISTENING FROM SERVER
				
				retornoMensagem = (Mensagem) input.readObject();// recebendo as mensagens REPLY
				System.out.println("Se recebido o REPLY, abaixo deve-se receber os parâmetros");
				System.out.println(m.getParam("response"));
				
				System.out.println("resposta do servidor: " + retornoMensagem);// mostrando as REPLY
				operacao = retornoMensagem.getOperacao();// PEgando a operacao da REPLY
				System.out.println(operacao);
				
				if(retornoMensagem.getStatus()==ReplyStatus.ERROR) {
					System.out.println("some Wrong happened, talk to IT professional for further instructions");
					
				}else if(retornoMensagem.getStatus()==ReplyStatus.PARAMNULL) {
					System.out.println("ooh, You, somehow, forgot to fill important information, try again :) ");
					
				}else if(operacao == "LOGINREPLY") {
					logado = true;
					admin = false;
					
				}else if(operacao == "ADMINLOGINREPLY") {
					logado = true;
					admin = true;
					
				}else if(operacao == "LOGOUTREPLY") {
					logado = false;
					admin = false;
				}
				
				
			}while (!operacao.equals("LOGOUTREPLY") || retornoMensagem.getStatus() != ReplyStatus.OK);//end of while

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
			System.out.println("error in ClassNotFoundException:");
			System.out.println(e);
		}

	}
}
