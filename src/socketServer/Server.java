package socketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import utilConnection.Candidato;
import utilConnection.Cliente;

public class Server {
	private ServerSocket serverSocket;
	private List<Candidato> candidatos;
	private List<Cliente> clientes;
	private List<Thread> ativos;
	private Boolean ElectionisOn;

	private void criarServerSocket(int porta) throws IOException {
		serverSocket = new ServerSocket(porta);
	}

	private Socket awaitConection() throws IOException {
		Socket socket = serverSocket.accept();
		return socket;
	}


	public Server() {
		this.clientes = new ArrayList<Cliente>();
		this.candidatos = new ArrayList<Candidato>();
		this.ElectionisOn = false;
	}
	
	public void setElectioisOn(Boolean a) {
		this.ElectionisOn = a;
	}
	public Boolean getElectionisOn() {
		return this.ElectionisOn;
	}
	
	public Boolean addCandidatos(ArrayList<Candidato> listaCandidatos) {
		int contador =0;
		for (Iterator<Candidato> iterator = listaCandidatos.iterator(); iterator.hasNext();) {
			Candidato candidato = (Candidato) iterator.next();
			if(!this.candidatos.contains(candidato)) {
				this.candidatos.add(candidato);
				contador++;
			}	
		}
		
		if(contador == listaCandidatos.size()) {
			return false;
		}
		return true;
	}
	public List<Cliente> getClientes(){
		return clientes;
	}
	
	public boolean hasCliente(Cliente cliente) {
		return this.getClientes().contains(cliente);
	}
	public boolean hasAdmin() {
		if(clientes.size()==0)
			return false;
		for (Cliente cliente : clientes) {
			if(cliente.getAdmin())
				return true;
		}
		return false;
	}
	public boolean addCliente(Cliente cliente) {
		if(this.hasCliente(cliente)) {
			System.out.println("already has a user with this name and password");
			return false;
		}
		return true;
	}
	
	public String getCandidatos() {
		String response =null;
		
		for (Candidato candidato : candidatos) {
			response += candidato.getNome() + ", " + candidato.getNumero() + ", " + candidato.getPartido() + "\n";
		}
		return response;
	}
	public String getVotos() {
		String response = null;
		
		for (Candidato candidato : candidatos) {
			response += candidato.getNome() + "( "+ candidato.getNumero()+ " ) : " + candidato.getVotos()+ " votos\n";
		}
		return response;
	}
	
	public Boolean IncrementVoteCandidato(String numero) {
		for (Iterator iterator = candidatos.iterator(); iterator.hasNext();) {
			Candidato candidato = (Candidato) iterator.next();
			if(candidato.getNumero()==numero) {
				candidato.incrementVotos();
				return true;
			}
		}
		return false;
	}
	public void connectionLoop() throws IOException{
		int id =0;
		while(true) {
			System.out.println("awaiting for connection");
			Socket socket = this.awaitConection();
			System.out.println("Client Connected");
			HandleConnection tc = new HandleConnection(this,socket,id++);
			Thread th = new Thread(tc);
			ativos.add(th);
			th.start();
			
			System.out.println("Client done");
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			
			server.criarServerSocket(5555);
			server.connectionLoop();

		} catch (IOException e) {
			System.out.println("SERVER ERROR: " + e.getMessage());}
//		} catch (ClassNotFoundException e) {
//			System.out.println("SERVER ERROR" + e.getMessage());
//		}
	}
}
