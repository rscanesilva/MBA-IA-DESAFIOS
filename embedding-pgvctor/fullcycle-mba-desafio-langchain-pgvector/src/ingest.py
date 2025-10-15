import os
from pathlib import Path
from dotenv import load_dotenv

from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_openai import OpenAIEmbeddings
from langchain_core.documents import Document
from langchain_postgres import PGVector
from sqlalchemy import create_engine

load_dotenv()
for k in ("OPENAI_API_KEY", "PGVECTOR_URL","PG_VECTOR_COLLECTION_NAME","PDF_PATH"):
    if not os.getenv(k):
        raise RuntimeError(f"Environment variable {k} is not set")

PDF_PATH = os.getenv("PDF_PATH")
PG_VECTOR_COLLECTION_NAME = os.getenv("PG_VECTOR_COLLECTION_NAME")
PGVECTOR_URL = os.getenv("PGVECTOR_URL")

def ingest_pdf():
    docs = PyPDFLoader(PDF_PATH).load()
    text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=150, add_start_index=False)
    chunks = text_splitter.split_documents(docs)

    enriched = [
        Document(
            page_content=doc.page_content,
            metadata={k: v for k, v in doc.metadata.items() if v not in ("", None)}
        )
        for doc in chunks
    ]

    embeddings = OpenAIEmbeddings(model=os.getenv("OPENAI_EMBEDDING_MODEL", "text-embedding-3-small"))
    
    engine = create_engine(os.getenv("PGVECTOR_URL"))
    
    PGVector.from_documents(
        collection_name=PG_VECTOR_COLLECTION_NAME,
        documents=chunks,
        embedding=embeddings,
        connection=engine
    )


if __name__ == "__main__":
    ingest_pdf()