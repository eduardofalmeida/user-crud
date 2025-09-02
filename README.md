User CRUD API

API em Java com Spring Boot para gerenciar usuários, permitindo cadastro, atualização, listagem e exclusão. A aplicação possui validação de dados, paginação e ordenação e utiliza um banco de dados H2 em memória para desenvolvimento e testes.

Tecnologias utilizadas:
Java 17
Spring Boot
H2 Database

Funcionalidades:
Criar novo usuário
Atualizar usuário existente
Listar usuários com paginação e ordenação
Buscar usuário por ID
Deletar usuário
Validação de campos obrigatórios e formatos (ex: email)

Documentação da API
A documentação completa está disponível na interface Swagger da aplicação:

<img width="1523" height="689" alt="image" src="https://github.com/user-attachments/assets/3a7c3eec-7be1-4684-9c6c-3a31686dd022" />


Como executar
Clone o repositório:

git clone https://github.com/eduardofalmeida/user-crud.git
cd user-crud

Execute a aplicação:

mvn spring-boot:run

Acesse a documentação da API em:

http://localhost:8080/swagger-ui.html
