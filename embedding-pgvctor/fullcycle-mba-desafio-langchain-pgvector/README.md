# Desafio MBA Engenharia de Software com IA - Full Cycle

## Pré-requisitos
- Python 3.10+
- Docker e Docker Compose

## Instalação e Execução

### 1. Configurar ambiente virtual
```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

### 2. Configurar variáveis de ambiente
```bash
cp .env.example .env
# Editar .env com suas chaves da OpenAI
```

### 3. Iniciar banco de dados
```bash
docker-compose up -d
```

### 4. Ingerir documentos
```bash
python src/ingest.py
```

### 5. Executar chat
```bash
python src/chat.py
```

## Estrutura do Projeto
- `src/ingest.py` - Ingestão de documentos PDF para o banco vetorial
- `src/search.py` - Busca semântica e geração de respostas com LLM
- `src/chat.py` - Interface de chat interativo
- `docker-compose.yml` - Configuração do PostgreSQL com pgvector