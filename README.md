# VirtuMix 🍳

Aplicação Android para planear, cozinhar e gerir receitas com modo guiado, integração com API externa e base de dados na nuvem.

---

## 📱 Funcionalidades

- Dashboard de receitas: pesquisa, filtros por tags, paginação (4 receitas/página) e favoritos
- Criação de receitas: ingredientes, passos, tempos e imagens
- Planeador semanal: calendário Segunda-Domingo para atribuir receitas
- Lista de compras: importa ingredientes das receitas planeadas
- Modo cozinha guiada: passos sequenciais com temporizadores e ecrã ativo
- Perfil: estatísticas (receitas, favoritos, horas cozinhadas)

---

## 🛠️ Requisitos Técnicos

- Android Studio Hedgehog ou superior
- JDK 21+ (embutido no Android Studio)
- Gradle 8.13
- Kotlin 2.0.21
- SDK: minSdk 28 / targetSdk 34

Testado em: Emulador API 34 (Android 14) e dispositivos Android 13+

---

## ⚙️ Configuração de Chaves

### RapidAPI (Low Carb Recipes)
1. Criar conta em https://rapidapi.com/
2. Subscrever "Low Carb Recipes" (plano gratuito)
3. Copiar `x-rapidapi-key`

Em `app/src/main/java/com/example/cookaplication_a043302/network/RetrofitClient.kt` defina:
```kotlin
private const val RAPIDAPI_KEY = "SUA_CHAVE_RAPIDAPI_AQUI"
```

### Supabase
1. Criar projeto em https://supabase.com/
2. Criar tabela `recipes` (exemplo):
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
3. Copiar Project URL e anon key

Em `app/src/main/java/com/example/cookaplication_a043302/data/repository/SupabaseClient.kt` defina:
```kotlin
val supabase = createSupabaseClient(
		supabaseUrl = "https://SEU_PROJETO.supabase.co",
		supabaseKey = "SUA_ANON_KEY_AQUI"
)
```

> Para testes rápidos, pode desativar RLS na tabela:
```sql
ALTER TABLE recipes DISABLE ROW LEVEL SECURITY;
```

---

## 🚀 Como Executar

1) Clonar
```bash
git clone https://github.com/seu-usuario/virtumix.git
cd virtumix
```
2) Abrir no Android Studio → File > Open (pasta do projeto)
3) Sync Gradle (Sync Now)
4) Configurar emulador (API 34) ou dispositivo com depuração USB
5) Build → Rebuild Project
6) Run (Shift+F10) selecionando o dispositivo/emulador

Problemas comuns:
- Falha de sync: File → Sync Project with Gradle Files
- Serialização: garantir plugin `kotlin("plugin.serialization")` ativo em `app/build.gradle.kts`
- Sem internet: confirmar permissões no AndroidManifest

---

## 📖 Uso Rápido

- **Dashboard**: ver receitas, pesquisar, filtrar por tags, favoritar, criar nova (+)
- **Adicionar Receita**: preencher nome, descrição, imagem (URL), tags, tempos, doses, ingredientes (um por linha) e passos (um por linha), guardar
- **Planeador**: escolher um dia, selecionar receita, enviar para compras
- **Compras**: ver lista, marcar check, adicionar itens, remover, limpar tudo
- **Detalhe**: ver imagem, ingredientes, passos, favoritar, iniciar modo guiado
- **Cozinha Guiada**: passos com texto grande, temporizador automático quando existir tempo indicado, ecrã mantém-se ativo
- **Perfil**: ver estatísticas

---

## 📂 Estrutura (resumo)

```
app/src/main/java/com/example/cookaplication_a043302/
├── data/            # models e repositórios (Supabase)
├── network/         # Retrofit/API
├── store/           # estados em memória (favoritos, compras)
├── ui/screens/      # ecrãs Compose
├── ui/navigation/   # rotas e nav host
├── viewmodel/       # lógicas de UI (StateFlow)
└── MainActivity.kt
```

---

## 🌐 Rede
- Internet obrigatória para API e imagens
- Manifest:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 🧱 Stack
- Kotlin 2.0.21, Jetpack Compose + Material 3
- Navigation Compose
- Retrofit 2.9.0 + OkHttp 4.11.0
- Gson 2.10.1 + kotlinx-serialization 1.6.3
- Supabase (postgrest-kt 2.6.1), Ktor client Android
- Coil 2.6.0 para imagens
- Coroutines + StateFlow

---

## 🐛 Conhecidos
- Favoritos, lista de compras e planeador ainda não persistem (em memória)
- Requer UUID válido (já gerado automaticamente ao criar receita)

---

## 🚧 Futuro
- Autenticação Supabase Auth
- Persistir favoritos, compras, planeador no Supabase
- Upload de imagens (Supabase Storage)
- Edição/remoção de receitas
- Cache offline e notificações

---

## 📄 Licença
Projeto académico; sem licença comercial.

## 👤 Autor
João Maia
