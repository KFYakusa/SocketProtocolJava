package socketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utilConnection.Candidato;
import utilConnection.Mensagem;
import utilConnection.ReplyStatus;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 5555);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			Mensagem m = new Mensagem("THIS WILL NOT LEAVE HERE");
			Mensagem retornoMensagem = null;
			String operacao;
			BufferedReader reader;
			String sysInput = null;
			Boolean logado = false;
			Boolean admin = false;
			Boolean accepted = false;

			
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
					
					System.out.println("|--------------------|\n"
							       + "|		  Login        |\n"
							       + "|                    |\n"
							       + "|   LISTCANDIDATOS   |\n"
							       + "|--------------------|\n"
							       + " First Name: ");	
					//creating message for reply
					m = new Mensagem("LOGIN");
					// Reading data using readLine
					while(!accepted) {
						accepted = false;
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
							accepted = true;
							sysInput = null;
						}	
					}
					
				}else if(!admin && logado) {
					
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
							System.out.println("number of you candidate: ");
							
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
					
				}else if(admin && logado){ // start of admin menu
					System.out.println("|--------------------|\n"
							 	   + "|1-LIST CANDIDATOS   |\n"
							 	   + "|2-VOTE     3-LOGOUT |\n"
							 	   + "|4-CONSULT RESULT    |\n"
							 	   + "|5-START VOTE        |\n"
							 	   + "|6-END VOTE          |\n"
							 	   + "|7-ADD CANDIDATO     |\n"
							 	   + "|--------------------|\n"
							 	   + "Option: ");
					
					
					while(!accepted) {
						
					
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
						System.out.println("number of you candidate: ");
						
						sysInput = reader.readLine();
						
						if(sysInput.isBlank() || sysInput.isEmpty()) {
							System.out.println("oh, you should write a number!");
							accepted = false;
						}
						m.setParam("numero", sysInput);
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
						m = new Mensagem("ENDVOTE");
						
						accepted = true;
						break;
					}case "7":{
						m = new Mensagem("ADDCANDIDATO");
						System.out.println("Quantidade de Candidatos: ");
						sysInput = reader.readLine();
						Integer quantidadeCandidatos = Integer.parseInt(sysInput);
						String nome;
						String numero;
						String partido;
						m.setParam("quantidade", quantidadeCandidatos);
						
						output.writeObject(m);
						output.flush();
						
						while(quantidadeCandidatos > 0) {
							
							sysInput = null;
							System.out.println("nome candidato: ");
							sysInput = reader.readLine();
							while(sysInput.length()==0) {
								sysInput = null;
								System.out.println("nome must be informed");
								sysInput = reader.readLine();
							}
							nome = sysInput;
							
							sysInput = null;
							System.out.println("numero candidato: ");
							sysInput = reader.readLine();
							while(sysInput.length()==0) {
								sysInput = null;
								System.out.println("numero must be informed: ");
								sysInput = reader.readLine();
							}
							numero = sysInput;
							
							sysInput = null;
							System.out.println("partido: ");
							sysInput = reader.readLine();
							while(sysInput.length()==0) {
								sysInput = null;
								System.out.println("partido must be informed: ");
								sysInput = reader.readLine();
							}
							partido = sysInput;
							
							Candidato c = new Candidato(nome, numero, partido);
							output.writeObject(c);
							output.flush();
							
							quantidadeCandidatos = quantidadeCandidatos -1;
						}
						
						retornoMensagem = (Mensagem) input.readObject();// recebendo as mensagens REPLY
//						System.out.println("Se recebido o REPLY, abaixo deve-se receber os parâmetros");
						System.out.println(retornoMensagem.getParam("response"));
						
						if(retornoMensagem.getStatus().equals(ReplyStatus.OK)) {
							accepted = true;
						}
						m = new Mensagem("LISTCANDIDATOS");
						
						break;
					}
					
					default:{
						accepted = false;
						System.out.println("THERE IS NO HIDDEN OPTION");
						System.out.println("THERE IS ONLY NUMBERS, they are described in the menu");
						break;
					}// end of default
				}//end of Switch(sysInput);
					}
				}// end of admin menu
				
				

//				System.out.println("Enviando pro servidor");
				output.writeObject(m);
				output.flush(); // enviando pro servidor a mensagem
				// ---------------------------------------------------------------------- HERE FINISH WRITING

//				System.out.println("abaixo está sendo recebida a mensagem do servidor");
				
				//------------------------------------------------------- HERE START LISTENING FROM SERVER
				
				retornoMensagem = (Mensagem) input.readObject();// recebendo as mensagens REPLY
//				System.out.println("Se recebido o REPLY, abaixo deve-se receber os parâmetros");
				System.out.println(retornoMensagem.getParam("response"));
				
				System.out.println("resposta do servidor:" + retornoMensagem);// mostrando as REPLY
				operacao = retornoMensagem.getOperacao();// PEgando a operacao da REPLY
				System.out.println(operacao);
				
				
				if(retornoMensagem.getStatus()==ReplyStatus.ERROR) {
					System.out.println("some Wrong happened, talk to IT professional for further instructions");
					
				}else if(retornoMensagem.getStatus()==ReplyStatus.PARAMNULL) {
					System.out.println("ooh, You, somehow, forgot to fill important information, try again :) ");
					
				}else if(operacao.equals("LOGINREPLY")) {
					logado = true;
					admin = false;
					
				}else if(operacao.equals("ADMINLOGINREPLY")) {
					System.out.println("ta entrando no adminLogingREPLY");
					logado = true;
					admin = true;
					
				}else if(operacao.equals("LOGOUTREPLY") ) {
					logado = false;
					admin = false;
				}
				
				
			}while (!operacao.equals("LOGOUTREPLY") || !retornoMensagem.getStatus().equals(ReplyStatus.OK));//end of while

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
