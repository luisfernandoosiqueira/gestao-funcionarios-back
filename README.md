# 🧩 API — Gestão de Funcionários

Aplicação **Spring Boot 3** desenvolvida para gerenciar funcionários de uma empresa fictícia da área de TI.

## ⚙️ Tecnologias utilizadas

* **Java 17+**
* **Spring Boot 3**
* **Spring Web / Spring Data JPA / Validation**
* **Banco H2 (memória)**
* **Jackson (para JSON e datas)**

## 📚 Funcionalidades principais

* **Cadastro, edição e exclusão** de funcionários
* **Listagem com filtros** por cargo e status (ativo/inativo)
* **Reativação automática** se o e-mail já existir e o funcionário estiver inativo
* **Bloqueio de e-mail duplicado**
* **Validação para não reduzir salário**
* **Inativação via PATCH**
* **Datas formatadas no padrão `dd/MM/yyyy`**

## 🔗 Endpoints principais

| Método   | Caminho                           | Descrição                                  |
| :------- | :-------------------------------- | :----------------------------------------- |
| `GET`    | `/api/funcionarios`               | Lista funcionários (com filtros opcionais) |
| `GET`    | `/api/funcionarios/{id}`          | Busca por ID                               |
| `POST`   | `/api/funcionarios`               | Cadastra novo funcionário                  |
| `PUT`    | `/api/funcionarios/{id}`          | Atualiza funcionário existente             |
| `PATCH`  | `/api/funcionarios/{id}/inativar` | Inativa funcionário                        |
| `DELETE` | `/api/funcionarios/{id}`          | Exclui definitivamente                     |

## 🧱 Como executar

1. Certifique-se de ter o **Java 17+** instalado.
2. No terminal, dentro da pasta do projeto:

   ```bash
   ./mvnw spring-boot:run
   ```
3. Acesse a API em **[http://localhost:8080/api/funcionarios](http://localhost:8080/api/funcionarios)**

## 🧠 Observações

* O **banco de dados H2** é recriado a cada execução (ideal para testes).
* O projeto já possui CORS liberado para **[http://localhost:4200](http://localhost:4200)** (Angular).
* Datas devem ser enviadas no formato **`dd/MM/yyyy`** no corpo das requisições.

> Projeto desenvolvido para fins acadêmicos, com integração direta ao frontend Angular “Gestão de Funcionários Front”.
