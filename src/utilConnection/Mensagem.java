package utilConnection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Mensagem implements Serializable{
	private String operacao;
	private ReplyStatus status;
	private Map<String,Object> params;
	
	public Mensagem (String operacao) {
		this.operacao = operacao;
		params = new HashMap<>();
	}
	
	public String getOperacao() {
		return operacao;
	}
	
	public void setStatus(ReplyStatus s ) {
		this.status = s;
	}
	public ReplyStatus getStatus() {
		return this.status;
	}
	public void setParam(String chave,Object valor) {
		params.put(chave,valor);
	}
	
	public Object getParam(String chave) {
		return params.get(chave);
	}
	
	@Override
	public String toString() {
		String m = "operacao: "+operacao;
		m += "\n Status: "+status;
		m+= "\nParâmetros:";
		for(String s : params.keySet()) {
			m+= "\n"+s+": "+params.get(s);
		}
		return m;
	}
	
}
