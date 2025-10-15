import os
from dotenv import load_dotenv

from langchain_openai import OpenAIEmbeddings, ChatOpenAI
from langchain_postgres import PGVector
from sqlalchemy import create_engine

load_dotenv()
for k in ("OPENAI_API_KEY", "PGVECTOR_URL","PG_VECTOR_COLLECTION_NAME","PDF_PATH"):
    if not os.getenv(k):
        raise RuntimeError(f"Environment variable {k} is not set")
        
PROMPT_TEMPLATE = """
CONTEXTO:
{contexto}

REGRAS:
- Responda somente com base no CONTEXTO.
- O contexto contém nomes, valores de reais de faturamento e ano de fundação de cada empresa.
- Se a informação não estiver explicitamente no CONTEXTO, responda:
  "Não tenho informações necessárias para responder sua pergunta."
- Nunca invente ou use conhecimento externo.
- Nunca produza opiniões ou interpretações além do que está escrito.

EXEMPLOS DE PERGUNTAS FORA DO CONTEXTO:
Pergunta: "Qual é a capital da França?"
Resposta: "Não tenho informações necessárias para responder sua pergunta."

Pergunta: "Quantos clientes temos em 2024?"
Resposta: "Não tenho informações necessárias para responder sua pergunta."

Pergunta: "Você acha isso bom ou ruim?"
Resposta: "Não tenho informações necessárias para responder sua pergunta."

PERGUNTA DO USUÁRIO:
{pergunta}

RESPONDA A "PERGUNTA DO USUÁRIO"
"""

def format_context(documents):
    """Formata os documentos encontrados em contexto para o prompt"""
    if not documents:
        return "Nenhum documento relevante encontrado."
    
    context_parts = []
    for i, (doc, score) in enumerate(documents, 1):
        context_parts.append(f"Documento {i} (relevância: {score:.3f}):\n{doc.page_content}\n")
    
    return "\n".join(context_parts)

def search_prompt(question=None, min_score=0.1, k=10, max_score=1.0):
    embeddings = OpenAIEmbeddings(model=os.getenv("OPENAI_EMBEDDING_MODEL","text-embedding-3-small"))
    
    engine = create_engine(os.getenv("PGVECTOR_URL"))
    
    store = PGVector(
        embeddings=embeddings,
        collection_name=os.getenv("PG_VECTOR_COLLECTION_NAME"),
        connection=engine,
        use_jsonb=True,
    )

    if question:
        # Buscar resultados
        results = store.similarity_search_with_score(question, k)
        
        # Aplicar filtros de relevância
        filtered_results = []
        for doc, score in results:
            # Filtro por score mínimo
            if score >= min_score and score <= max_score:
                filtered_results.append((doc, score))
            
            # Limitar número de resultados
            if len(filtered_results) >= k:
                break
        
        # Ordenar por relevância (score mais alto primeiro)
        filtered_results.sort(key=lambda x: x[1], reverse=True)
        
        if not filtered_results:
            return "Nenhum documento relevante encontrado para responder sua pergunta."
        
        # Formatar contexto
        context = format_context(filtered_results)

        llm = ChatOpenAI(
            model=os.getenv("OPENAI_CHAT_MODEL", "gpt-3.5-turbo"),
            temperature=0.1  # Baixa temperatura para respostas mais precisas
        )
        
        # Criar prompt final
        prompt = PROMPT_TEMPLATE.format(
            contexto=context,
            pergunta=question
        )
        
        # Gerar resposta com LLM
        print("💭 Gerando resposta com LLM...")
        response = llm.invoke(prompt)
        
        return response.content
    else:
        return store