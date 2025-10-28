Excelente, Stephany 👏
Agora que o sistema também gerencia **Departamentos**, vamos atualizar o `README.md` do backend mantendo seu estilo claro, organizado e com os mesmos ícones e formatação.

Aqui está a **versão revisada e ampliada**, refletindo todas as novas funcionalidades do projeto:

---

# 🧩 API — Gestão de Funcionários e Departamentos

Aplicação **Spring Boot 3** desenvolvida para gerenciar **funcionários** e **departamentos** de uma empresa fictícia da área de TI.

---

## ⚙️ Tecnologias utilizadas

* **Java 17+**
* **Spring Boot 3 (Web / Data JPA / Validation)**
* **Banco H2 (em memória)**
* **Jackson** para serialização JSON
* **OpenAPI / Swagger UI** para documentação automática

---

## 📚 Funcionalidades principais

### 👨‍💼 Funcionários

* **Cadastro, edição e exclusão** de funcionários
* **Listagem com filtros** por cargo e status (ativo/inativo)
* **Associação a um Departamento**
* **Reativação automática** se o e-mail já existir e estiver inativo
* **Bloqueio de e-mail duplicado**
* **Validação para não reduzir salário**
* **Inativação via PATCH**
* **Datas formatadas** no padrão `dd/MM/yyyy`

### 🏢 Departamentos

* **Cadastro, edição e exclusão** de departamentos
* **Listagem com filtro** por status (ativo/inativo)
* **Relação bidirecional** com funcionários (`@OneToMany`)
* **Inativação de departamentos** sem exclusão física
* **Reativação via API**

---

## 🔗 Endpoints principais

### 👨‍💼 Funcionários

| Método   | Caminho                           | Descrição                                  |
| :------- | :-------------------------------- | :----------------------------------------- |
| `GET`    | `/api/funcionarios`               | Lista funcionários (com filtros opcionais) |
| `GET`    | `/api/funcionarios/{id}`          | Busca funcionário por ID                   |
| `POST`   | `/api/funcionarios`               | Cadastra novo funcionário                  |
| `PUT`    | `/api/funcionarios/{id}`          | Atualiza funcionário existente             |
| `PATCH`  | `/api/funcionarios/{id}/inativar` | Inativa funcionário                        |
| `DELETE` | `/api/funcionarios/{id}`          | Exclui definitivamente                     |

### 🏢 Departamentos

| Método   | Caminho                            | Descrição                       |
| :------- | :--------------------------------- | :------------------------------ |
| `GET`    | `/api/departamentos`               | Lista departamentos             |
| `GET`    | `/api/departamentos/{id}`          | Busca departamento por ID       |
| `POST`   | `/api/departamentos`               | Cadastra novo departamento      |
| `PUT`    | `/api/departamentos/{id}`          | Atualiza departamento existente |
| `PATCH`  | `/api/departamentos/{id}/inativar` | Inativa departamento            |
| `DELETE` | `/api/departamentos/{id}`          | Exclui definitivamente          |

---

## 🧱 Como executar

1. Certifique-se de ter o **Java 17+** instalado.

2. No terminal, dentro da pasta do projeto:

   ```bash
   mvn clean spring-boot:run
   ```

3. Acesse:

   * API Funcionários → [http://localhost:8080/api/funcionarios](http://localhost:8080/api/funcionarios)
   * API Departamentos → [http://localhost:8080/api/departamentos](http://localhost:8080/api/departamentos)
   * Documentação Swagger → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧠 Observações

* O **banco de dados H2** é recriado a cada execução (ideal para testes).
* O projeto possui CORS liberado para o frontend Angular: **[http://localhost:4200](http://localhost:4200)**.
* Datas devem ser enviadas no formato **`dd/MM/yyyy`**.
* Ao inativar um Departamento, os Funcionários permanecem vinculados, mas podem ser atualizados normalmente.

---

> 🧩 Projeto desenvolvido para fins acadêmicos, com integração direta ao frontend Angular “Gestão de Funcionários Front”, contemplando o módulo de **Departamentos** e a evolução do modelo de dados original.

---

Quer que eu gere também a **versão do README do frontend** (Angular), no mesmo estilo, com as seções de componentes, rotas e comandos `ng`?
