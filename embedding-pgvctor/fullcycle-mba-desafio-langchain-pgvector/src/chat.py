from search import search_prompt

def main():
    print("🤖 Chat com RAG - Sistema de Perguntas e Respostas")
    print("=" * 50)
    print("Digite 'sair' para encerrar o chat\n")
    
    while True:
        # Obter pergunta do usuário
        question = input("❓ Sua pergunta: ").strip()
        
        if question.lower() in ['sair', 'exit', 'quit']:
            print("👋 Até logo!")
            break
            
        if not question:
            print("Por favor, digite uma pergunta válida.\n")
            continue
        
        try:
            # Usar search_prompt para buscar documentos relevantes
            print("🔍 Buscando informações...")
            results = search_prompt(question)
            
            if not results:
                print("❌ Nenhum documento relevante encontrado.\n")
                continue
            
            print(results)
            
        except Exception as e:
            print(f"❌ Erro ao processar pergunta: {e}\n")

if __name__ == "__main__":
    main()