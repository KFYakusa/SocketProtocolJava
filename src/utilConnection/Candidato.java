package utilConnection;

import java.io.Serializable;

public class Candidato implements Serializable{
	private String nome;
	private String numero;
	private String partido;
	private Integer votos;
	public Candidato(String nome, String numero, String partido) {
		this.nome = nome;
		this.numero = numero;
		this.partido = partido;
		this.votos = 0;
	}
	public String getNumero() {
		return numero;
	}
	
	public String getPartido() {
		return partido;
	}
	public String getNome() {
		return nome;
	}
	public Integer getVotos() {
		return votos;
	}
	
	public void incrementVotos() {
		this.votos++; 
	}
	
}
