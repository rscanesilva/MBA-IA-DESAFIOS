# Desafio MBA Engenharia de Software com IA - Spring AI + PgVector

## Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker e Docker Compose

## Instalação e Execução

### 1. Configurar variáveis de ambiente
```bash
cp env.example .env
# Editar .env com suas chaves da OpenAI
```

### 2. Iniciar banco de dados
```bash
docker-compose up -d
```

### 3. Executar aplicação
```bash
mvn spring-boot:run
```

### 4. Ingerir documentos
```bash
curl -X POST http://localhost:8080/api/ingest/pdf \
  -H "Content-Type: application/json" \
  -d '{"pdfPath": "document.pdf"}'
```

### 5. Fazer perguntas
```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"question": "Qual é o faturamento da empresa X?"}'
```

## Endpoints da API

- `POST /api/ingest/pdf` - Ingerir documento PDF
- `POST /api/chat` - Fazer pergunta ao sistema
- `GET /api/chat/health` - Verificar saúde do sistema

## Estrutura do Projeto
- `DocumentService` - Ingestão de documentos PDF para o banco vetorial
- `SearchService` - Busca semântica e geração de respostas com LLM
- `ChatController` - API REST para chat
- `IngestController` - API REST para ingestão
- `docker-compose.yml` - Configuração do PostgreSQL com pgvector
