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
					case "LOGIN":{
						String nome = (String) mInput.getParam("nome");
						String senha = (String) mInput.getParam("senha");
						mReply = new Mensagem("LOGINREPLY");
						if(nome == null || senha == null) {
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", nome!=null? "Senha is NULL" : "Nome is NULL" );
						}else {
							usuario = new Cliente();
							usuario.setNome(nome);
							usuario.setSenha(senha);
							
							if (!server.hasCliente(usuario)) {
								server.addCliente(usuario);
								mReply.setParam("response", "novo usuário, " + usuario.getNome());
							} else {
								mReply.setParam("response", "Bem vindo denovo, " + usuario.getNome());
							}
							estado = Estado.AUTH;
						}
					}// end of LOGIN
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
					default: {
						mReply = new Mensagem("");
						mReply.setStatus(ReplyStatus.ERROR);
						mReply.setParam("response", "OPERACAO INVALIDA");
					}//end of default
					}// end of switch(operacao)

					break;
				} // end of AUTH
				
				
				case ADMIN: {
					switch (operacao) {
						default: {
							mReply = new Mensagem("LISTREPLY");
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "OPERACAO INVALIDA");
						}//end of default
					}// end of switch(operacao)
					break;
				} // end of ADMIN
				case EXIT: {
					switch (operacao) {

						default: {
							mReply = new Mensagem("LOGINREPLY");
							mReply.setStatus(ReplyStatus.ERROR);
							mReply.setParam("response", "OPERACAO INVALIDA");
						}//end of default
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
