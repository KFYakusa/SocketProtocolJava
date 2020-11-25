package utilConnection;

public class Cliente {
	private String nome;
	private String senha;
	private Boolean admin;
	private Integer id;
	public Cliente() {
		this.admin = null;
		this.senha = null;
		this.nome = null;
	}
	//GETs
	public Boolean getAdmin() {
		return admin;
	}
	public String getNome() {
		return nome;
	}
	public String getSenha() {
		return senha;
	}
	public Integer getId() {
		return id;
	} 
	
	
	//SETs
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
