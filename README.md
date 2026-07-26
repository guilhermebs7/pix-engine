

# 🔑 Pix Engine



![Java](https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.4.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge&logo=java&logoColor=white)
![Testcontainers](https://img.shields.io/badge/Testcontainers-2496ED?style=for-the-badge&logo=testcontainers&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger%20OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Render](https://img.shields.io/badge/Deploy-Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)




O **Pix Engine** é uma API REST desenvolvida em **Java 21 + Spring Boot 3.4.2** que simula o núcleo de processamento de transações Pix. A aplicação permite:

- Cadastrar **usuários** com saldo em conta;
- Cadastrar **chaves Pix** dos tipos `CPF`, `EMAIL`, `TELEFONE` e `ALEATORIA` (geração automática de UUID);
- Realizar **transferências** entre usuários com validação de saldo e regras de negócio;
- Emitir automaticamente um **comprovante** com código de autenticação a cada transação concluída;
- Consultar comprovantes por ID.

O projeto foi construído com foco em boas práticas de arquitetura em camadas (Controller → Service → Repository → Entity), validação de regras de negócio no service, e uma suíte de testes completa cobrindo desde testes unitários isolados (Mockito) até testes de integração com banco real via Testcontainers, tudo rodando em pipeline de CI/CD com deploy automatizado no Render.

---



## 🏗️ Arquitetura e estrutura de pacotes

```
src/main/java/engine/pix_core/
├── PixCoreApplication.java     → Classe principal (bootstrap do Spring Boot)
├── config/
│   └── SwaggerConfig.java      → Configuração do OpenAPI/Swagger
├── controller/
│   ├── UsuarioController       → POST /usuarios | GET /usuarios/{id}
│   ├── ChavePixController      → POST /chaves-pix
│   ├── TransacaoController     → POST /transferencias
│   └── ComprovanteController   → GET /comprovantes/{id}
├── dto/
│   ├── Request/                → Records de entrada (com Bean Validation)
│   └── Response/                → Records de saída
├── entity/
│   ├── Usuario                 → id, nome, cpf (único), saldo, lista de chaves Pix
│   ├── ChavePix                → id, tipoChave (enum), valorChave (único), usuário dono, dataCriacao
│   ├── Transacao                → pagador, recebedor, valor, status (enum), dataHora, descrição
│   ├── Comprovante              → transação (1:1), código de autenticação (único), dataEmissao
│   ├── TipoChave (enum)         → CPF, EMAIL, TELEFONE, ALEATORIA
│   └── StatusTransacaoEnum      → PROCESSANDO, CONCLUIDA, FALHA, CANCELADA
├── repository/                 → Interfaces Spring Data JPA (JpaRepository)
└── service/
    ├── UsuarioService          → Cadastro (valida CPF duplicado) e busca de usuário
    ├── ChavePixService         → Cadastro de chave (gera UUID se ALEATORIA, valida duplicidade)
    ├── TransacaoService        → Orquestra a transferência (regra de negócio central)
    └── ComprovanteService      → Gera e consulta comprovantes
```

### 🔄 Fluxo de uma transferência Pix

A lógica central do sistema está no `TransacaoService.processarTransferencia()`:

1. Busca o **pagador** pelo ID (lança exceção se não existir).
2. Busca a **chave Pix de destino** e, a partir dela, o **recebedor**.
3. Valida que o pagador não está transferindo **para si mesmo**.
4. Valida se o pagador tem **saldo suficiente**.
5. Debita o valor do pagador e credita no recebedor (persistindo os dois usuários).
6. Cria e salva a `Transacao` com status `CONCLUIDA`.
7. Aciona o `ComprovanteService` para gerar automaticamente o **comprovante** (com código de autenticação único).
8. Retorna um `TransacaoResponse` com os dados da operação.

---

## 📡 Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastra um novo usuário (nome + CPF, saldo inicia zerado) |
| `GET` | `/usuarios/{id}` | Busca um usuário e suas chaves Pix cadastradas |
| `POST` | `/chaves-pix` | Cadastra uma chave Pix para um usuário |
| `POST` | `/transferencias` | Realiza uma transferência Pix entre dois usuários |
| `GET` | `/comprovantes/{id}` | Consulta o comprovante de uma transação pelo ID |

A documentação interativa completa (gerada pelo springdoc-openapi) fica disponível em `/swagger-ui.html` com a aplicação em execução.

---

## ⚙️ Variáveis de ambiente

A aplicação lê as credenciais do banco a partir de variáveis de ambiente, referenciadas no `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pix-engine
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
```

Ou seja, para rodar localmente é necessário exportar (ou definir num `.env`, se você optar por usar uma lib como o `spring-dotenv`) as seguintes variáveis:

```env
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
```


---

## ▶️ Como executar localmente

**Pré-requisitos:** Java 21, Maven (ou use o wrapper `./mvnw`), Docker.

### Opção 1 — Subindo o Postgres manualmente

```bash
git clone https://github.com/guilhermebs7/pix-engine.git
cd pix-engine

docker run --name pix-engine-db \
  -e POSTGRES_USER=seu_usuario \
  -e POSTGRES_PASSWORD=sua_senha \
  -e POSTGRES_DB=pix-engine \
  -p 5432:5432 -d postgres

export POSTGRES_USER=seu_usuario
export POSTGRES_PASSWORD=sua_senha

./mvnw spring-boot:run
```

### Opção 2 — Via Docker (usando o Dockerfile do projeto)

O projeto já possui um `Dockerfile` com build multi-stage (compila com Maven + JDK 21 e roda com uma imagem enxuta do JDK 21):

```bash
docker build -t pix-engine .
docker run -p 8080:8080 \
  -e POSTGRES_USER=seu_usuario \
  -e POSTGRES_PASSWORD=sua_senha \
  pix-engine
```

A aplicação sobe, por padrão, em `http://localhost:8080`.

---

## 🧪 Testes automatizados

O projeto conta com uma suíte de testes em três níveis: **unitários (Mockito)**, **fatiados de camada web (MockMvc)** e **de integração (Testcontainers)**.

### ✅ Testes unitários com Mockito — `service`

Os testes de service (`TransacaoServiceTest`, `ChavePixServiceTest`) isolam totalmente a regra de negócio das dependências externas, usando:

- `@ExtendWith(MockitoExtension.class)` — habilita o Mockito no JUnit 5.
- `@Mock` — cria dublês dos repositórios (`TransacaoRepository`, `UsuarioRepository`, `ChavePixRepository`) e de outros services usados como dependência (`ComprovanteService`, `UsuarioService`).
- `@InjectMocks` — instancia a classe real (`TransacaoService`/`ChavePixService`), injetando os mocks automaticamente.
- `when(...).thenReturn(...)` / `thenAnswer(...)` — simula o retorno dos repositórios sem tocar em banco de dados algum.
- `verify(...)` — confirma quantas vezes um método do mock foi chamado (ex.: garantir que `save()` **não** é chamado quando a regra de negócio barra a operação).
- `assertThrows(...)` — valida que exceções de negócio são lançadas corretamente (saldo insuficiente, chave duplicada).

**Exemplo real do projeto** (`TransacaoServiceTest`):

```java
@Test
@DisplayName("Deve realizar transferencia PIX com sucesso quando houver saldo")
void processarTransferenciaSucesso(){
    TransacaoRequest request = new TransacaoRequest(1L, "maria@email.com", new BigDecimal("40.00"), "Almoço");

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pagador));
    when(chavePixRepository.findByValorChave("maria@email.com")).thenReturn(Optional.of(chaveDestino));
    when(transacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    TransacaoResponse response = transacaoService.processarTransferencia(request);

    assertNotNull(response);
    assertEquals(new BigDecimal("60.00"), pagador.getSaldo());   // debitou do pagador
    assertEquals(new BigDecimal("40.00"), recebedor.getSaldo()); // creditou no recebedor
    assertEquals(StatusTransacaoEnum.CONCLUIDA, response.status());

    verify(usuarioRepository, times(2)).save(any());             // salvou pagador e recebedor
    verify(comprovanteService, times(1)).gerarComprovante(any()); // gerou o comprovante
}
```

E o teste de caminho de erro:

```java
@Test
@DisplayName("Deve lançar exceção quando o pagador não tiver saldo suficiente")
void processarTransferenciaSaldoInsuficiente(){
    // ...
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> transacaoService.processarTransferencia(request));

    assertEquals("Saldo insuficiente para realizar a transação", exception.getMessage());
    verify(transacaoRepository, never()).save(any()); // nada foi persistido
}
```

A lógica segue o padrão **Given–When–Then**: monta-se o cenário e o comportamento simulado dos mocks (*given*), executa-se o método real do service (*when*) e verifica-se tanto o valor retornado quanto as interações com os mocks (*then*).

O `ChavePixServiceTest` segue a mesma lógica, validando duas regras específicas: a geração automática de UUID quando o tipo da chave é `ALEATORIA`, e o bloqueio de cadastro quando a chave já existe no sistema.

### 🌐 Testes de camada web (MockMvc)

- **`ComprovanteControllerTest`** usa `@WebMvcTest(ComprovanteController.class)` — sobe **apenas a camada web** (não o contexto Spring completo), com o `ComprovanteService` substituído por `@MockitoBean`. É o teste mais rápido, focado em validar serialização JSON, status HTTP e roteamento.
- **`UsuarioControllerTest`** usa `@SpringBootTest` + `@AutoConfigureMockMvc` — sobe o contexto completo da aplicação, mas ainda troca o `UsuarioService` real por um mock (`@MockitoBean`) para não gravar dados de verdade. Valida tanto o cadastro com sucesso (`201 Created`) quanto o retorno de `400 Bad Request` quando as anotações de validação (`@NotBlank`) do `UsuarioRequest` são violadas.

### 🐳 Testes de integração com Testcontainers

A classe **`BaseIntegrationTest`** é a base compartilhada dos testes de integração:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("pix_db_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

Ela sobe um **container Docker real do PostgreSQL 16** a cada execução e injeta a URL/credenciais dinamicamente no contexto do Spring, através de `@DynamicPropertySource`. Isso permite que `ComprovanteIntegrationTest` valide o comportamento **real** de JPA/Hibernate contra um banco de verdade — persistindo um `Usuario` pagador, um `Usuario` recebedor, uma `Transacao` e um `Comprovante` — e depois confirme, via `assertThat` (AssertJ), que os dados foram salvos e recuperados corretamente, incluindo o relacionamento `Comprovante → Transacao → Pagador`.

Diferente dos testes unitários (que mockam o repositório), aqui a query, o mapeamento JPA e as constraints do banco (unicidade, chaves estrangeiras) são exercitados de verdade — evitando o clássico "passou no mock, quebrou em produção".

### 📦 Rodando os testes

```bash
./mvnw clean verify
```

> 💡 Como os testes de integração usam Testcontainers, é necessário ter o **Docker em execução** na máquina (ou no runner de CI) para os containers de teste subirem corretamente.

---

## 🔁 CI/CD (GitHub Actions)

O pipeline (`.github/workflows/ci.yml`) roda em todo push/PR para `main` e `develop`, com dois jobs:

1. **`build`** — configura o JDK 21 (Temurin), sobe um serviço de PostgreSQL 15 no próprio runner do GitHub Actions e executa `mvn -B clean verify`, rodando toda a suíte de testes (unitários + integração).
2. **`deploy`** — roda **somente** se o job `build` passar e o push for na branch `main`. Ele dispara um `curl -X POST` para o **Deploy Hook do Render**, guardado no secret `RENDER_DEPLOY_HOOK`, iniciando o redeploy automático da aplicação.

```yaml
deploy:
  needs: build
  if: github.ref == 'refs/heads/main' && github.event_name == 'push'
  steps:
    - run: curl -X POST "${{ secrets.RENDER_DEPLOY_HOOK }}"
```

---

## ☁️ Deploy

A aplicação está publicada na **[Render](https://pix-engine.onrender.com/swagger-ui/index.html#/)**. O fluxo de deploy é automatizado via GitHub Actions:

1. Push/merge na branch `main`.
2. O GitHub Actions builda o projeto e roda toda a suíte de testes.
3. Se tudo passar, o pipeline chama o **Deploy Hook** do Render, que puxa a última versão do repositório e refaz o build/deploy do serviço (usando o `Dockerfile` do projeto).
4. O banco PostgreSQL de produção é provisionado separadamente no Render, com `POSTGRES_USER` e `POSTGRES_PASSWORD` configurados como variáveis de ambiente no painel do serviço.


