# Desafios MBA - Engenharia de Software com IA

Repositório com os códigos dos desafios desenvolvidos no MBA de Engenharia de Software com IA pela **Full Cycle**.

## 📚 Sobre

Este repositório contém implementações práticas de conceitos e tecnologias aprendidas durante o MBA, com foco em:

- Inteligência Artificial e Machine Learning
- Embeddings e Busca Vetorial
- RAG (Retrieval-Augmented Generation)
- Integração com LLMs (Large Language Models)

## 🚀 Desafios Implementados

### 1. LangChain + PgVector (Python)

Implementação de um sistema de chat com RAG usando LangChain e PostgreSQL com extensão pgvector.

**Tecnologias:**
- Python 3.10+
- LangChain
- OpenAI API
- PostgreSQL + pgvector
- Docker

**Localização:** [`embedding-pgvctor/fullcycle-mba-desafio-langchain-pgvector/`](./embedding-pgvctor/fullcycle-mba-desafio-langchain-pgvector/)

**Funcionalidades:**
- Ingestão de documentos PDF
- Geração de embeddings
- Busca semântica em banco vetorial
- Chat interativo com contexto

[📖 Ver documentação completa](./embedding-pgvctor/fullcycle-mba-desafio-langchain-pgvector/README.md)

---

### 2. Spring AI + PgVector (Java)

API REST para chat com RAG usando Spring AI e PostgreSQL com extensão pgvector.

**Tecnologias:**
- Java 17+
- Spring Boot
- Spring AI
- OpenAI API
- PostgreSQL + pgvector
- Docker

**Localização:** [`embedding-pgvctor/fullcycle-mba-desafio-springai-pgvector/`](./embedding-pgvctor/fullcycle-mba-desafio-springai-pgvector/)

**Funcionalidades:**
- API REST para ingestão de documentos
- Endpoints para chat
- Busca semântica em banco vetorial
- Geração de respostas com contexto

[📖 Ver documentação completa](./embedding-pgvctor/fullcycle-mba-desafio-springai-pgvector/README.md)

## 📁 Estrutura do Repositório

```
desafios/
├── embedding-pgvctor/
│   ├── fullcycle-mba-desafio-langchain-pgvector/    # Desafio Python + LangChain
│   │   ├── src/
│   │   ├── docker-compose.yml
│   │   ├── requirements.txt
│   │   └── README.md
│   └── fullcycle-mba-desafio-springai-pgvector/     # Desafio Java + Spring AI
│       ├── src/
│       ├── pom.xml
│       ├── docker-compose.yml
│       └── README.md
└── README.md                                         # Este arquivo
```

## 🛠️ Pré-requisitos Gerais

- Docker e Docker Compose
- Conta na OpenAI com API Key
- Python 3.10+ (para projetos Python)
- Java 17+ e Maven 3.6+ (para projetos Java)

## 🎓 MBA Full Cycle

Este repositório faz parte do **MBA em Engenharia de Software com IA** da [Full Cycle](https://fullcycle.com.br/).

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais como parte do MBA Full Cycle.

---

**Desenvolvido por:** Rafael Scane  
**Instituição:** Full Cycle

