# VirtuMix 🍳

Aplicação Android para planear, cozinhar e gerir receitas com modo guiado, integração com API externa e base de dados na nuvem.

---

## 📱 Funcionalidades

- **Dashboard de Receitas**: Pesquisa e filtros por tags, paginação (4 receitas/página), sistema de favoritos
- **Criação de Receitas**: Adicione receitas personalizadas com ingredientes, passos, tempos e imagens
- **Planeador Semanal**: Calendário Segunda-Domingo para atribuir receitas aos dias da semana
- **Lista de Compras**: Importa automaticamente ingredientes das receitas planeadas
- **Modo Cozinha Guiada**: Passos sequenciais com temporizadores integrados e ecrã sempre ativo
- **Perfil de Utilizador**: Estatísticas (receitas totais, favoritos, horas cozinhadas)

---

## 🛠️ Requisitos Técnicos

### Software Necessário
- **Android Studio**: Hedgehog ou superior
- **JDK**: 21+ (incluído no Android Studio)
- **Gradle**: 8.13
- **Kotlin**: 2.0.21
- **Android SDK**: API 28 (mínimo) / API 34 (alvo)

### Testado Em
- Emulador Android API 34 (Android 14)
- Dispositivos físicos com Android 13+

### Dependências Externas
- **RapidAPI** (Low Carb Recipes): Para pesquisa de receitas online
- **Supabase**: Para armazenamento de receitas criadas pelo utilizador

---

## ⚙️ Configuração Inicial

### 1. Obter Chaves de API

#### RapidAPI (Low Carb Recipes)
1. Criar conta em [RapidAPI](https://rapidapi.com/)
2. Subscrever a API "Low Carb Recipes" (plano gratuito disponível)
3. Copiar a chave da API (`x-rapidapi-key`)

#### Supabase
1. Criar projeto em [Supabase](https://supabase.com/)
2. Criar tabela `recipes` executando o script SQL:
   ```sql
   CREATE TABLE recipes (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     name TEXT NOT NULL,
     description TEXT,
     image_url TEXT,
     tags TEXT[] DEFAULT '{}',
     prepare_time INTEGER,
     cook_time INTEGER,
     servings INTEGER,
     ingredients TEXT[] DEFAULT '{}',
     steps TEXT[] DEFAULT '{}',
     user_id TEXT,
     created_at TIMESTAMP DEFAULT NOW()
   );
   ```
3. Copiar a **Project URL** e **anon/public key** nas configurações do projeto

### 2. Configurar Credenciais no Projeto

Editar o ficheiro `app/src/main/java/com/example/cookaplication_a043302/network/RetrofitClient.kt`:

```kotlin
private const val RAPIDAPI_KEY = "SUA_CHAVE_RAPIDAPI_AQUI"
```

Editar o ficheiro `app/src/main/java/com/example/cookaplication_a043302/data/repository/SupabaseClient.kt`:

```kotlin
val supabase = createSupabaseClient(
    supabaseUrl = "https://SEU_PROJETO.supabase.co",
    supabaseKey = "SUA_ANON_KEY_AQUI"
)
```

---

## 🚀 Como Compilar e Executar

### Passo 1: Clonar o Repositório
```bash
git clone https://github.com/seu-usuario/virtumix.git
cd virtumix
```

### Passo 2: Abrir no Android Studio
1. Abrir o Android Studio
2. **File → Open** e selecionar a pasta do projeto
3. Aguardar indexação e sincronização do Gradle

### Passo 3: Configurar Emulador/Dispositivo
- **Emulador**: Menu **Device Manager** → Criar AVD com API 34
- **Dispositivo Físico**: Ativar modo de programador e depuração USB

### Passo 4: Compilar e Executar
1. Clicar em **Build → Rebuild Project** (ou `Ctrl+Shift+F9`)
2. Selecionar emulador/dispositivo no topo
3. Clicar no botão **Run** (ou `Shift+F10`)

### Resolução de Problemas Comuns
- **Erro de sincronização Gradle**: `File → Sync Project with Gradle Files`
- **Falta de internet**: Verificar permissão INTERNET no AndroidManifest.xml
- **Erro de serialização**: Verificar plugin `kotlin("plugin.serialization")` no build.gradle.kts

---

## 📖 Como Usar a Aplicação

### 1. Dashboard (Ecrã Principal)
- **Visualizar Receitas**: Navegue pelas 20 receitas da API com paginação
- **Pesquisar**: Use a barra de pesquisa para filtrar por nome
- **Filtrar por Tags**: Clique nas tags para filtrar receitas (ex: "keto", "breakfast")
- **Adicionar Favoritos**: Toque no ícone de coração para marcar favoritos
- **Criar Receita**: Toque no botão **+** no canto superior direito

### 2. Adicionar Nova Receita
1. Preencher **Nome da Receita**
2. Adicionar **Descrição** (opcional)
3. Inserir **URL da Imagem**
4. Adicionar **Tags** separadas por vírgula (ex: "jantar, saudável")
5. Definir **Tempo de Preparação** e **Tempo de Cozedura** (minutos)
6. Indicar **Número de Doses**
7. Listar **Ingredientes** (um por linha)
8. Descrever **Passos** (um por linha)
9. Clicar em **Guardar Receita**

### 3. Planeador Semanal
1. Aceder ao separador **Planeador**
2. Clicar num dia da semana (Segunda a Domingo)
3. Selecionar uma receita da lista
4. Clicar em **Enviar para Compras** para exportar ingredientes

### 4. Lista de Compras
1. Aceder ao separador **Compras**
2. Ver todos os ingredientes importados do planeador
3. Marcar itens como comprados (checkbox)
4. Adicionar itens manualmente com o botão **+**
5. Remover itens deslizando para o lado
6. Limpar lista completa com **Limpar Tudo**

### 5. Detalhe da Receita
1. Clicar numa receita no Dashboard
2. Ver imagem, ingredientes, passos e informações nutricionais
3. Adicionar/remover favoritos com o ícone no topo
4. Iniciar modo guiado com **Iniciar Cozinha**

### 6. Modo Cozinha Guiada
1. Ler o passo atual em letras grandes
2. Usar **Anterior/Próximo** para navegar
3. Se o passo incluir tempo (ex: "Cozinhar por 15 minutos"):
   - O temporizador inicia automaticamente
   - O ecrã permanece ativo durante a cozedura
   - Recebe notificação quando o tempo termina

### 7. Perfil de Utilizador
- Ver estatísticas: total de receitas, favoritos, horas cozinhadas
- Aceder a definições (em desenvolvimento)

---

## 📂 Estrutura do Projeto

```
app/src/main/java/com/example/cookaplication_a043302/
├── data/
│   ├── model/          # Recipe, UserRecipe, Ingredient, ShoppingItem
│   └── repository/     # RecipeRepository, SupabaseClient
├── network/            # RetrofitClient, RecipeApiService
├── store/              # FavoritesStore, ShoppingListStore (in-memory)
├── ui/
│   ├── screens/        # DashboardScreen, PlannerScreen, AddRecipeScreen, etc.
│   └── navigation/     # AppNavigation, Routes
├── viewmodel/          # DashboardViewModel, AddRecipeViewModel, etc.
└── MainActivity.kt
```

---

## 🌐 Requisitos de Rede

- **Internet obrigatória**: Sem conexão, não há pesquisa de receitas nem carregamento de imagens
- **Permissões no AndroidManifest.xml**:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```

---

## 🗄️ Armazenamento e Persistência

- **Receitas da API**: Carregadas dinamicamente (RapidAPI)
- **Receitas do Utilizador**: Armazenadas no Supabase (tabela `recipes`)
- **Favoritos**: Armazenamento em memória (a persistir no Supabase futuramente)
- **Lista de Compras**: Armazenamento em memória (a persistir no Supabase futuramente)
- **Planeador Semanal**: Armazenamento em memória (a persistir no Supabase futuramente)

---

## 🔧 Stack Tecnológica

- **Linguagem**: Kotlin 2.0.21
- **UI**: Jetpack Compose com Material 3
- **Navegação**: Compose Navigation
- **Networking**: Retrofit 2.9.0 + OkHttp 4.11.0
- **Serialização**: Gson 2.10.1 + kotlinx-serialization 1.6.3
- **Base de Dados**: Supabase (Postgrest-kt 2.6.1)
- **Imagens**: Coil 2.6.0
- **Assíncrono**: Kotlin Coroutines + StateFlow

---

## 📝 Notas Importantes

1. **Autenticação**: Atualmente usa `userId="guest"` para todas as receitas criadas
2. **RLS no Supabase**: Desativar Row Level Security na tabela `recipes` para testes:
   ```sql
   ALTER TABLE recipes DISABLE ROW LEVEL SECURITY;
   ```
3. **Limite de API**: RapidAPI free tier tem limite de 100 requests/mês
4. **Imagens**: URLs devem ser HTTPS válidas

---

## 🐛 Problemas Conhecidos

- Favoritos não persistem após fechar a app (armazenamento em memória)
- Lista de compras não persiste após fechar a app
- Planeador semanal não persiste após fechar a app
- Receitas criadas requerem UUID válido (gerado automaticamente)

---

## 🚧 Funcionalidades Futuras

- [ ] Autenticação de utilizadores (Supabase Auth)
- [ ] Persistência de favoritos, lista de compras e planeador no Supabase
- [ ] Upload de imagens (Supabase Storage)
- [ ] Edição e eliminação de receitas criadas
- [ ] Partilha de receitas entre utilizadores
- [ ] Modo offline com cache local
- [ ] Notificações push para lembretes de refeições

---

## 📄 Licença

Este projeto é académico e não possui licença comercial.

---

## 👤 Autor

**João Maia**  
Projeto desenvolvido para a disciplina de Computação Móvel