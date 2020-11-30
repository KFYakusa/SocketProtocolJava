package socketServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utilConnection.Cliente;
import utilConnection.Estado;
import utilConnection.Mensagem;
import utilConnection.ReplyStatus;

public class HandleConnection implements Runnable {
	private Socket socket;
	private Server server;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Estado estado;
	private Cliente usuario;

	public HandleConnection(Server server, Socket socket, Integer id) {
		this.server = server;
		this.socket = socket;
		this.estado = Estado.CONECTADO;
		this.usuario = new Cliente();
		this.usuario.setId(id);
	}

	private void closeSocket(Socket s) throws IOException {
		s.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.tratarConexao();
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void tratarConexao() throws IOException, ClassNotFoundException, InterruptedException {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			Mensagem mInput;
			String operacao;
			Mensagem mReply = null;

			while (estado != Estado.EXIT) {

				// sequencia:
				// ler mensagem vinda do cliente
				// filtrar estado do cliente
				// filtrar por operacao
				// tratar a requisição
				// escrever reply pro cliente

				mInput = (Mensagem) input.readObject();
				operacao = mInput.getOperacao();

				System.out.println("leu mensagem e operacao");

				switch (estado) {

				case CONECTADO: {
					switch (operacao) {
					case "LOGIN": {
						String nome = (String) mInput.getParam("nome");
						String senha = (String) mInput.getParam("senha");
						mReply = new Mensagem("LOGINREPLY");
						if (nome == null || senha == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", nome != null ? "Senha is NULL" : "Nome is NULL");
						} else {
							usuario.setNome(nome);
							usuario.setSenha(senha);

							if (!server.hasCliente(usuario)) {
								if (!server.hasAdmin()) {
									usuario.setAdmin(true);
									mReply = new Mensagem("ADMINLOGINREPLY");
									mReply.setParam("response", "new Captain!, " + usuario.getNome());
									estado = Estado.ADMIN;
								} else {
									usuario.setAdmin(false);
									mReply.setParam("response", "novo marujo, " + usuario.getNome());
									estado = Estado.AUTH;
								}
								server.addCliente(usuario);

							} else {
								if (usuario.getAdmin()) {
									mReply.setParam("response", "Welcome again Captain! " + usuario.getNome());
									estado = Estado.ADMIN;
								} else {
									mReply.setParam("response", "Bem vindo denovo, " + usuario.getNome());
									estado = Estado.AUTH;
								}

							}

						}
					} // end of LOGIN
					case "EXIT": {
						mReply = new Mensagem("EXITREPLY");
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", "Farewell, Traveller!");
						estado = Estado.EXIT;
					}
					default: {
						mReply = new Mensagem("LOGINREPLY");
						mReply.setStatus(ReplyStatus.ERROR);
						mReply.setParam("response", "OPERACAO INVALIDA");
					}
					}// end of switch(operacao)
					break;
				} // end of CONECTADO
				case AUTH: {

					switch (operacao) {
					case "LOGOUT": {
						mReply = new Mensagem("LOGOUTREPLY");
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", "that's a sad decision, " + this.usuario.getNome() + "?");
						estado = Estado.CONECTADO;
					}
					case "VOTE": {
						String numero = (String) mInput.getParam("numero");
						mReply = new Mensagem("VOTEREPLY");
						if(numero==null) {
							mReply.setStatus(ReplyStatus.PARAMNULL);
							mReply.setParam("response", "");
						}
						if (server.IncrementVoteCandidato(numero)) {
							mReply.setStatus(ReplyStatus.OK);
							mReply.setParam("response", "vote contabilized");
						} else {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response",
									"something went wrong trying to vote, do this candidate exists?");
						}
						break;
					}
					case "LISTCANDIDATOS": {
						mReply = new Mensagem("LISTCANDIDATOSREPLY");
						if (server.getCandidatos() == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "ERROR: there is no candidates yet");
							break;
						}
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", server.getCandidatos());
						break;
					}
					case "CONSULTRESULT": {
						mReply = new Mensagem("CONSULTRESULTREPLY");
						String resposta = server.getVotos();
						if (resposta == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "ERROR: there is no candidates yet");
							break;
						}
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", resposta);
						break;
					}
					default: {
						mReply = new Mensagem("LISTCANDIDATOSREPLY");
						mReply.setStatus(ReplyStatus.ERROR);
						mReply.setParam("response", "OPERACAO INVALIDA");
						break;
					} // end of default
					}// end of switch(operacao)

					break;
				} // end of AUTH
				case ADMIN: {
					switch (operacao) {
					case "ADDCANDIDATO": {
						if(server.getCandidatos().isEmpty()) {
							Integer number = (Integer) mInput.getParam("quantidade");
						}
						 break;
					}
					case "STARTVOTE": {
						mReply = new Mensagem("STARTVOTEREPLY");
						if(server.getElectionisOn()) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "Votação já foi iniciada");
							break;
						}
						server.setElectioisOn(true);
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", "Ay, Captain! Starting votation now!");
						break;
					}
					case "ENDVOTE":{
						mReply = new Mensagem("ENDVOTEREPLY");
						if(!server.getElectionisOn()) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "Votação já foi encerra");
							break;
						}
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", "Ay, Captain! back to the bay");
						break;
					}

					case "LOGOUT": {
						mReply = new Mensagem("LOGOUTREPLY");
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", "that's a sad decision, " + this.usuario.getNome() + "?");
						estado = Estado.CONECTADO;
						break;
					}
					case "VOTE": {
						String numero = (String) mInput.getParam("numero");
						mReply = new Mensagem("VOTEREPLY");

						if (server.IncrementVoteCandidato(numero)) {
							mReply.setStatus(ReplyStatus.OK);
							mReply.setParam("response", "vote contabilized");
						} else {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response",
									"something went wrong trying to vote, do this candidate exists?");
						}
						break;
					}
					case "LISTCANDIDATOS": {
						mReply = new Mensagem("LISTCANDIDATOSREPLY");
						if (server.getCandidatos() == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "ERROR: there is no candidates yet");
							break;
						}
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", server.getCandidatos());
						break;
					}
					case "CONSULTRESULT": {
						mReply = new Mensagem("CONSULTRESULTREPLY");
						String resposta = server.getVotos();
						if (resposta == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "ERROR: there is no candidates yet");
							break;
						}
						mReply.setStatus(ReplyStatus.OK);
						mReply.setParam("response", resposta);
						break;
					}

					default: {
						mReply = new Mensagem("LISTREPLY");
						mReply.setStatus(ReplyStatus.ERROR);
						mReply.setParam("response", "OPERACAO INVALIDA");
						break;
					} // end of default
					}// end of switch(operacao)
					break;
				} // end of ADMIN
				case EXIT: {
					switch (operacao) {

					default: {
						mReply = new Mensagem("LOGINREPLY");
						mReply.setStatus(ReplyStatus.ERROR);
						mReply.setParam("response", "OPERACAO INVALIDA");
					} // end of default
					}// end of switch(operacao)
					break;
				} // end of EXIT

				default: {

					break;
				}
				}
				output.writeObject(mReply);
				output.flush();
			}
		} finally {
			closeSocket(socket);
		}
	}

}
