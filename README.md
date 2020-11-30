# TCPySocket
A TCP Socket that supports multiple users using Java.


### Protocolo

#### Descrição Geral: 

Protocolo de middleware de um sistema de votação. Cliente deve poder votar, consultar resultado, ver lista de candidatos, enquanto um administrador deve ser capaz de cadastrar candidatos, iniciar votação,obter lista de votação, finalizá-la, apurá-la, votar, e. Não necessariamente nessa ordem. Para realizar os processos respectivos, os clientes, admins ou não, devem estar logados no sistema.

#### Lista de mensagens possíveis:

- LOGIN → Cliente solicita autenticação com o servidor;
- LOGINREPLY→ Servidor responde LOGIN, alterando estado para Autenticado;
- ADMINLOGINREPLY→Caso o cliente entre com dados de administrador, o servidor altera seu estado para tal, ao invés de Autenticado;
- LOGOUT→ Cliente/Admin solicita logout do sistema para o servidor;
- LOGOUTREPLY → Servidor responde alterando estado para Conectado;
- ADDCANDIDATO→ admin requisita cadastro de candidato novo;
- ADDCANDIDATOREPLY→Servidor responde ao pedido de criação de candidato
- STARTVOTE→ admin requisita o começo da votação
- STARTVOTEREPLY→ Servidor responde a requisição startvote do admin
- LISTCANDIDATOS→ Cliente pede ao servidor para ver os candidatos existentes
- LISTCANDIDATOSREPLY→ Servidor envia a lista de candidatos;
- ENDVOTE→ Admin requisita o encerramento da votação;
- ENDVOTEREPLY→ Servidor responde à ENDVOTE;
- CONSULTRESULT→ Cliente requisita ao servidor a contagem de votos;
- CONSULTRESULTREPLY→ Servidor responde cliente com contagem de votos;
- VOTE→ Cliente envia numero e nome do candidato a ser votado;
- VOTEREPLY→ Servidor responde à operação VOTE;

#### Lista de códigos:

- OK→ Operação completada com sucesso
- ERROR→ Erro ao processar requisição
- PARAMNULL→ Falta de parâmetros válidos

#### Tipos Possíveis: 

- String
- Integer

####  OPERAÇÕES

| LOGIN             | LOGINREPLY            | ADMINLOGINREPLY       |
| ---               | ---                   | ---                   |
| SEM CODIGO        | OK, ERROR, PARAMNULL  | OK, ERROR, PARAMNULL  |
| Cliente: String   | response:String       | response:String       |
| Senha: String     | -                     | -                     |



| LOGOUT            | LOGOUTREPLY       | 
| ---               | ---               |
| SEM CODIGO        | OK, ERROR         | 
| -                 | response:String   | 


| STARTVOTE         | STARTVOTEREPLY    | 
| ---               | ---               |
| SEM CODIGO        | OK, ERROR         | 
| -                 | response:String   | 


| ADDCANDITATO                      | ADDCANDIDATOREPLY     | 
| ---                               | ---                   |
| SEM CODIGO                        | OK, ERROR, PARAMNULL  | 
| nome:String                       | response:String       | 
| ListaCandidatos:List< Candidato>  | -                     | 


| LISTCANDIDATOS    | LISTCANDIDATOSREPLY   | 
| ---               | ---                   |
| SEM CODIGO        | OK, ERROR             | 
| -                 | response:String       | 


| ENDVOTE           | ENDVOTEREPLY      | 
| ---               | ---               |
| SEM CODIGO        | OK, ERROR         | 
| -                 | response:String   | 


| CONSULTRESULT     | CONSULTRESULTREPLY  | 
| ---               | ---                 |
| SEM CODIGO        | OK, ERROR           | 
| -                 | response:String     | 


| VOTE          | VOTEREPLY             | 
| ---           | ---                   |
| SEM CODIGO    | OK, ERROR, PARAMNULL  | 
| numero:String | response:String       | 


#### Tabela de estados e suas transições 

- CONECTADO → Estado inicial do cliente, sem auth;
- AUTH→ Estado que cliente está autenticado
- ADMIN→ Estado que cliente se autenticou com uma conta de admin
- EXIT→ Estado apenas para sair da aplicação



| Estado atual  | Mensagem                      | transição | 
| ---           | ---                           | ---       | 
| CONECTADO     | LOGIN,                        | CONECTADO |
|               | LISTCANDIDATOS                |           |
|               | EXIT                          |           |
|               | LOGINREPLY:ERROR              |           |                   
|               | ADMINLOGINREPLY:ERROR         |           |               
|               | LOGINREPLY:PARAMNULL          |           |                   
|               | ADMINLOGINREPLY:PARAMNULL     |           |               
|               | EXITREPLY:ERROR               |           |           
|               | LISTCANDIDATOS:OK             |           |
|               | LISTCANDIDATOS:ERROR          |           |           
|               | LOGINREPLY:OK                 | AUTH      |                   
|               | ADMINLOGINREPLY:OK            | ADMIN     |                    
|               | EXIT:OK                       | EXIT      |               
| AUTH          | LOGOUT                        | AUTH      |               
|               | VOTE                          |           |       
|               | LISTCANDIDATOS                |           |           
|               | CONSULTRESULT                 |           |               
|               | VOTEREPLY:OK                  |           |               
|               | VOTEREPLY:ERROR               |           |               
|               | VOTTEREPLY:PARAMNULL          |           |               
|               | LISTCANDIDATOSREPLY:OK        |           |               
|               | LISTCANDIDATOSREPLY:ERROR     |           |               
|               | CONSULTRESULTREPLY:OK         |           |               
|               | CONSULTRESULTREPLY:ERROR      |           |               
|               | LOGOUTREPLY:OK                | CONECTADO |               
| ADMIN         | ADDCANDIDATO                  | ADMIN     |               
|               | STARTVOTE                     |           |        
|               | ENDVOTE                       |           |               
|               | LOGOUT                        |           |               
|               | STARTVOTEREPLY:OK             |           |                      
|               | ENDVOTEREPLY:OK               |           |               
|               | ADDCANDIDATOREPLY:OK          |           |               
|               | STARTVOTEREPLY:ERROR          |           |               
|               | ENDVOTEREPLY:ERROR            |           |
|               | ADDCANDIDATOREPLY:ERROR       |           |
|               | LOGOUT:ERROR                  |           |
|               | ADDCANDIDATOREPLY:PARAMNULL   |           |
|               | LOGOUTREPLY:OK                | CONECTADO |


#### Exemplos de troca de mensagens:
    LOGIN { nome = daniel, senha = pato}
    → LOGINADMINREPLY:OK
    → ADDCANDIDATO [ 
    { nome = amaral, numero = 13, partido = ST, quantidade = 3},
    { nome = Padre Luiz, numero = 666, partido = IVPB},
    { nome = Antonio da padaria, numero = 26, partido= MB} 
    ] 
    → ADDCANDIDATOREPLY:OK
    → STARTVOTE
    → STARTVOTEREPLY:OK

#### Máquina de estados

<img src="./VotacaoFIniteMachineREAL.png" width="100%">
