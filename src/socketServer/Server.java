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
	private List<String> votos;
	private List<Cliente> clientes;
	private List<Thread> ativos;
	private Boolean ElectionisOn;
//	private Integer quantidadeCandidatos =0;
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
		this.ativos = new ArrayList<Thread>();
		this.votos = new ArrayList<String>();
		this.ElectionisOn = false;
	}
	
	public synchronized void setElectioisOn(Boolean a) {
		this.ElectionisOn = a;
		if(ElectionisOn == false) {
			notifyAll();
		}
	}
	
	public Boolean getElectionisOn() {
		return this.ElectionisOn;
	}
	
	public Boolean addCandidatos(List<Candidato> listaCandidatos) {
		int contador =0;
		for (Iterator<Candidato> iterator = listaCandidatos.iterator(); iterator.hasNext();) {
			Candidato candidato = (Candidato) iterator.next();
			if(!this.candidatos.contains(candidato)) {
				this.candidatos.add(candidato);
				contador++;
			}	
		}
		
		if(contador == 0) {
			return false;
		}
		return true;
	}
	public synchronized List<Cliente> getClientes(){
		
		return clientes;
	}
	
	public Cliente getCliente(Cliente cliente) {
		for (Cliente c : clientes) {
			if(c.getNome().equalsIgnoreCase(cliente.getNome())) {
				return c;
			}
		}
		return null;
	}
	
	public boolean hasCliente(Cliente cliente) {
		for (Cliente c : clientes) {
			if(c.getNome().equalsIgnoreCase(cliente.getNome())) {
				return true;
			}
		}
		return false;
	}
	public boolean hasAdmin() {
		if(clientes.size() < 1 )
			return false;
		for (Cliente cliente : clientes) {
			if(cliente.getAdmin())
				return true;
		}
		return false;
	}
	public synchronized boolean addCliente(Cliente cliente) {
		if(this.hasCliente(cliente)) {
			System.out.println("already has a user with this name and password");
			
			return false;
		}
		this.clientes.add(cliente);
		return true;
	}
	
	public String getCandidatosString() {
		String response =null;
		
		for (Candidato candidato : candidatos) {
			if(response==null) {
				response = candidato.getNome() + ", " + candidato.getNumero() + ", " + candidato.getPartido() + "\n";	
			}else
				response += candidato.getNome() + ", " + candidato.getNumero() + ", " + candidato.getPartido() + "\n";
		}
		return response;
	}
	public List<Candidato> getCandidatos(){
		return candidatos;
	}
	
	public synchronized String getVotos() throws InterruptedException{
		String response = null;
		while(ElectionisOn) {
			wait();
		}
		for (Candidato candidato : candidatos) {
			if(response == null) {
				response = candidato.getNome() + "( "+ candidato.getNumero()+ " ) : " + candidato.getVotos()+ " votos\n";	
			}else
				response += candidato.getNome() + "( "+ candidato.getNumero()+ " ) : " + candidato.getVotos()+ " votos\n";
		}
		return response;
	}
	
	public synchronized void IncrementVoteCandidato(String numero) {
		this.votos.add(numero);
		
	}
	
	public synchronized void apurarEleicao() throws InterruptedException{
		while(ElectionisOn) {
			wait();
		}
		for (Candidato c : candidatos) {
			for (int i = 0; i < votos.size(); i++) {
				if(c.getNumero().equalsIgnoreCase(votos.get(i))) {
					c.incrementVotos();
					
				}
			}
		}
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
