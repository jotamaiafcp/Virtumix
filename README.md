# 📱 VirtuMix - Aplicação de Receitas Android

> **Aplicação móvel para gerir receitas, planear refeições semanais e criar listas de compras**  
> Tecnologias: Kotlin + Jetpack Compose + Room Database + Supabase

---

## 📚 Índice

1. [Visão Geral](#-visão-geral)
2. [Arquitetura do Projeto](#-arquitetura-do-projeto)
3. [Estrutura de Pastas](#-estrutura-de-pastas)
4. [Ficheiros Importantes Explicados](#-ficheiros-importantes-explicados)
5. [Estruturas de Código](#-estruturas-de-código-e-sua-influência)
6. [Fluxo de Dados](#-fluxo-de-dados)
7. [Como Funciona](#-como-funciona)

---

## 🎯 Visão Geral

### O que é esta aplicação?

Uma app Android moderna que permite:
- ✅ Ver receitas de uma API/Supabase (online)
- ✅ Criar receitas próprias guardadas localmente (offline)
- ✅ Planear refeições para cada dia da semana
- ✅ Gerir lista de compras
- ✅ Executar receitas passo-a-passo
- ✅ Funciona offline com sincronização quando há internet

### Porquê 2 fontes de dados?

**🗄️ Base de Dados Local (Room) - PRINCIPAL**
- Guarda dados no telemóvel
- Funciona sem internet
- Arranque rápido
- **Todas as receitas criadas por ti vão aqui** (CreateReceitaScreen)
- Dados pessoais (planeador, favoritos, lista de compras, execução)
- Sistema principal da aplicação

**🌐 API Externa (RapidAPI) - CONTEÚDO DEMONSTRATIVO**
- 20 receitas Low Carb para popular o Dashboard
- Read-only (não podes criar/editar)
- Fornece conteúdo inicial para a app não parecer vazia
- Usado apenas para exibição no Dashboard

---

## 🏗️ Arquitetura do Projeto

### Padrão MVVM (Model-View-ViewModel)

```
┌──────────────────────────────────────────────┐
│            📱 UI (ECRÃS)                     │
│    DashboardScreen, CreateReceitaScreen...   │
│                                              │
│  O que vês e tocas                           │
└──────────────┬───────────────────────────────┘
               ↓ "Quero ver/criar receitas!"
┌──────────────▼───────────────────────────────┐
│        🧠 VIEWMODELS                         │
│    ReceitaViewModel, PlannerViewModel...     │
│                                              │
│  Gere o estado e decide o que mostrar        │
└──────────────┬───────────────────────────────┘
               ↓ "Preciso de dados"
┌──────────────▼───────────────────────────────┐
│        📦 REPOSITORIES                       │
│    ReceitaRepository (Room - PRINCIPAL)      │
│    RecipeRepository (Supabase - secundário)  │
│                                              │
│  Gere acesso aos dados                       │
└──────────────┬───────────────────────────────┘
               ↓ Procura em 3 fontes
        ┌──────┴───────┬─────────┐
        ↓              ↓         ↓
┌───────▼─────┐  ┌─────▼───┐  ┌─▼──────┐
│  💾 ROOM    │  │ 🌐 API   │  │ ☁️ SUP │
│ (Principal) │  │ (Público)│  │ (Opt.) │
│             │  │          │  │        │
│ - Receitas  │  │ RapidAPI │  │ Backend│
│ - Ingred.   │  │ Low Carb │  │ Cloud  │
│ - Passos    │  │ Recipes  │  │ (demo) │
└─────────────┘  └──────────┘  └────────┘
  ↑ SEMPRE         ↑ Dashboard   ↑ Raro
```

### Camadas Explicadas

| Camada | Responsabilidade | Analogia |
|--------|------------------|----------|
| **UI (Views)** | Mostra dados ao utilizador e recebe interações | Montra da loja |
| **ViewModel** | Prepara dados para a UI, gere estado | Gerente que organiza |
| **Repository** | Fonte única de dados, gere acesso | Armazém inteligente |
| **Data Sources** | Room (local - tuas receitas) + RapidAPI (público - receitas demo) | Teu caderno + Livros da biblioteca |

---

## 📁 Estrutura de Pastas

```
app/src/main/java/com/example/cookaplication_a043302/
│
├── 📱 ui/                          # INTERFACE DO UTILIZADOR
│   ├── screens/                    # Ecrãs da aplicação
│   │   ├── DashboardScreen.kt      # Página inicial
│   │   ├── ListReceitasScreen.kt   # Lista de receitas
│   │   ├── CreateReceitaScreen.kt  # Criar receita local
│   │   ├── RecipeDetailScreen.kt   # Detalhes (remoto)
│   │   ├── RecipeDetailScreenLocal.kt # Detalhes (local)
│   │   ├── PlannerScreen.kt        # Planeador semanal
│   │   ├── ExecutionScreen.kt      # Executar receita
│   │   └── ShoppingListScreen.kt   # Lista de compras
│   │
│   ├── navigation/                 # Navegação entre ecrãs
│   │   └── AppNavigation.kt        # Define rotas
│   │
│   └── theme/                      # Tema visual (cores, tipografia)
│
├── 🧠 viewmodel/                   # GESTÃO DE ESTADO
│   ├── ReceitaViewModel.kt         # Estado das receitas locais
│   ├── PlannerViewModel.kt         # Estado do planeador
│   ├── DashboardViewModel.kt       # Estado do dashboard
│   └── ViewModelFactory.kt         # Cria ViewModels com dependências
│
├── 📦 data/                        # CAMADA DE DADOS
│   │
│   ├── local/                      # BASE DE DADOS LOCAL (ROOM)
│   │   ├── entities/
│   │   │   └── ReceitaEntities.kt  # Define tabelas (Receitas, Ingredientes, Passos)
│   │   │
│   │   ├── dao/
│   │   │   └── ReceitaDao.kt       # Operações CRUD (Insert, Update, Delete, Query)
│   │   │
│   │   ├── database/
│   │   │   └── ReceitaRoomDatabase.kt # Instância Singleton da BD
│   │   │
│   │   ├── PlannerEntity.kt        # Dados do planeador
│   │   ├── FavoritesStore.kt       # Favoritos
│   │   └── ShoppingListStore.kt    # Lista de compras
│   │
│   ├── remote/                     # API REMOTA
│   │   ├── SupabaseClient.kt       # Cliente Supabase
│   │   ├── RetrofitClient.kt       # Cliente HTTP
│   │   └── ApiService.kt           # Endpoints da API
│   │
│   ├── repository/                 # INTEGRAÇÃO DE DADOS
│   │   ├── ReceitaRepository.kt    # Gere Room (local)
│   │   └── RecipeRepository.kt     # Gere API (remoto)
│   │
│   └── model/                      # MODELOS DE DADOS
│       ├── UserRecipe.kt           # Receita do utilizador
│       └── ShoppingItem.kt         # Item de compras
│
├── model/
│   └── Recipe.kt                   # Modelo de receita (API)
│
└── MainActivity.kt                 # PONTO DE ENTRADA da aplicação
```

---

## 🔍 Ficheiros Importantes Explicados

### 1. MainActivity.kt - Ponto de Entrada

**Caminho:** `app/src/main/java/.../MainActivity.kt`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            VirtuMixTheme {  // ← Aplica o tema da app
                Surface {
                    AppNavigation()  // ← Inicia a navegação
                }
            }
        }
    }
}
```

**O que faz:**
- É o **primeiro ficheiro executado** quando abres a app
- `onCreate()` é chamado quando a app arranca
- `setContent {}` define o conteúdo em Jetpack Compose
- `AppNavigation()` controla que ecrã mostrar

**Analogia:** É como a porta de entrada de um edifício - por aqui tudo começa.

---

### 2. ReceitaEntities.kt - Definição das Tabelas

**Caminho:** `data/local/entities/ReceitaEntities.kt`

```kotlin
// TABELA DE RECEITAS
@Entity(
    tableName = "receitas",  // ← Nome da tabela na BD
    indices = [Index(value = ["titulo"], unique = true)]  // ← Títulos únicos
)
data class ReceitaBd(
    @PrimaryKey(autoGenerate = true)  // ← ID gerado automaticamente
    val receitaId: Int = 0,
    
    @ColumnInfo(name = "titulo")  // ← Coluna "titulo"
    val titulo: String,
    
    val descricao: String,
    val categoria: String,
    val pessoasServidas: Int,
    val tempoPreparacao: Int,  // em minutos
    val tempoCozimento: Int,
    val dificuldade: String,  // Fácil/Médio/Difícil
    val criadoEm: Long = System.currentTimeMillis()
)

// TABELA DE INGREDIENTES
@Entity(
    tableName = "ingredientes",
    foreignKeys = [
        ForeignKey(
            entity = ReceitaBd::class,  // ← Relaciona com Receitas
            parentColumns = ["receitaId"],
            childColumns = ["receitaId"],
            onDelete = CASCADE  // ← Se apagar receita, apaga ingredientes
        )
    ]
)
data class IngredienteBd(
    @PrimaryKey(autoGenerate = true)
    val ingredienteId: Int = 0,
    
    val receitaId: Int,  // ← FK: a que receita pertence
    val nome: String,    // ex: "Sal"
    val quantidade: Double,  // ex: 2.0
    val unidade: String  // ex: "colheres"
)

// TABELA DE PASSOS
@Entity(
    tableName = "passos",
    foreignKeys = [
        ForeignKey(
            entity = ReceitaBd::class,
            parentColumns = ["receitaId"],
            childColumns = ["receitaId"],
            onDelete = CASCADE
        )
    ]
)
data class PassoBd(
    @PrimaryKey(autoGenerate = true)
    val passoId: Int = 0,
    
    val receitaId: Int,  // ← FK
    val ordem: Int,      // 1º passo, 2º passo, etc.
    val descricao: String  // "Pré-aquecer forno a 180°"
)
```

**Estruturas de Código Importantes:**

#### `@Entity` - Define uma Tabela
```kotlin
@Entity(tableName = "receitas")
```
- Transforma a classe numa **tabela da base de dados**
- `tableName` define o nome da tabela SQL

#### `@PrimaryKey` - Chave Primária
```kotlin
@PrimaryKey(autoGenerate = true)
val receitaId: Int = 0
```
- Identifica **unicamente** cada linha
- `autoGenerate = true` → BD gera IDs automaticamente (1, 2, 3...)

#### `@ForeignKey` - Relacionamentos
```kotlin
foreignKeys = [ForeignKey(
    entity = ReceitaBd::class,
    parentColumns = ["receitaId"],
    childColumns = ["receitaId"],
    onDelete = CASCADE
)]
```
- **Liga duas tabelas**
- Cada ingrediente/passo pertence a uma receita
- `CASCADE` → se apagar receita, apaga também ingredientes/passos

#### `@ColumnInfo` - Personalizar Colunas
```kotlin
@ColumnInfo(name = "titulo")
val titulo: String
```
- Define o nome da coluna na BD
- Útil se quiseres nome diferente da propriedade

**Analogia das Tabelas:**
```
RECEITA #1: "Lasanha"
├─ Ingrediente 1: Massa (500g)
├─ Ingrediente 2: Molho (300ml)
├─ Passo 1: Pré-aquecer forno
└─ Passo 2: Cozinhar massa
```

---

### 3. ReceitaDao.kt - Operações na Base de Dados

**Caminho:** `data/local/dao/ReceitaDao.kt`

```kotlin
@Dao  // ← Data Access Object
interface ReceitaDao {
    
    // INSERIR nova receita
    @Insert
    suspend fun insertReceita(receita: ReceitaBd): Long
    
    // ATUALIZAR receita existente
    @Update
    suspend fun updateReceita(receita: ReceitaBd)
    
    // APAGAR receita
    @Delete
    suspend fun deleteReceita(receita: ReceitaBd)
    
    // BUSCAR por ID
    @Query("SELECT * FROM receitas WHERE receitaId = :id")
    suspend fun findReceitaById(id: Int): ReceitaBd?
    
    // BUSCAR TODAS (com reatividade)
    @Query("SELECT * FROM receitas ORDER BY criadoEm DESC")
    fun getAllReceitas(): LiveData<List<ReceitaBd>>
    
    // PESQUISAR por termo
    @Query("SELECT * FROM receitas WHERE titulo LIKE '%' || :termo || '%'")
    fun searchReceitas(termo: String): LiveData<List<ReceitaBd>>
    
    // FILTRAR por categoria
    @Query("SELECT * FROM receitas WHERE categoria = :categoria")
    fun getReceitasByCategoria(categoria: String): LiveData<List<ReceitaBd>>
}
```

**Estruturas de Código Importantes:**

#### `@Dao` - Define Operações
```kotlin
@Dao
interface ReceitaDao
```
- **DAO = Data Access Object**
- Interface que define **o que podes fazer** com a BD
- Room gera o código automaticamente

#### `suspend fun` - Funções Assíncronas
```kotlin
suspend fun insertReceita(receita: ReceitaBd): Long
```
- `suspend` = **pode demorar**, não trava a app
- Executada em background (thread separada)
- Perfeito para operações de BD que demoram

#### `@Insert`, `@Update`, `@Delete` - Operações Básicas
```kotlin
@Insert
suspend fun insertReceita(receita: ReceitaBd): Long  // ← Retorna ID gerado
```
- Room gera o SQL automaticamente
- `@Insert` → `INSERT INTO receitas VALUES (...)`
- `@Update` → `UPDATE receitas SET ... WHERE id = ...`
- `@Delete` → `DELETE FROM receitas WHERE id = ...`

#### `@Query` - Consultas Personalizadas
```kotlin
@Query("SELECT * FROM receitas WHERE titulo LIKE '%' || :termo || '%'")
fun searchReceitas(termo: String): LiveData<List<ReceitaBd>>
```
- Escreves SQL diretamente
- `:termo` é substituído pelo parâmetro `termo`
- `LIKE '%termo%'` → pesquisa parcial ("las" encontra "Lasanha")

#### `LiveData<T>` - Dados Reativos
```kotlin
fun getAllReceitas(): LiveData<List<ReceitaBd>>
```
- **LiveData observa mudanças** na BD
- Quando dados mudam, **UI atualiza automaticamente**
- Não precisas fazer refresh manual

**Exemplo de Uso:**
```kotlin
// Na UI ou ViewModel
dao.getAllReceitas().observe(lifecycleOwner) { receitas ->
    // Este bloco executa automaticamente quando dados mudam
    println("Agora temos ${receitas.size} receitas!")
}
```

---

### 4. ReceitaRoomDatabase.kt - Instância da Base de Dados

**Caminho:** `data/local/database/ReceitaRoomDatabase.kt`

```kotlin
@Database(
    entities = [
        ReceitaBd::class,      // ← Tabelas que existem
        IngredienteBd::class,
        PassoBd::class
    ],
    version = 1,  // ← Versão do schema
    exportSchema = true  // ← Exporta schema para versionamento
)
abstract class ReceitaRoomDatabase : RoomDatabase() {
    
    // DAOs disponíveis
    abstract fun receitaDao(): ReceitaDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun passoDao(): PassoDao
    
    companion object {
        @Volatile
        private var INSTANCE: ReceitaRoomDatabase? = null
        
        fun getInstance(context: Context): ReceitaRoomDatabase {
            return INSTANCE ?: synchronized(this) {  // ← Thread-safe
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReceitaRoomDatabase::class.java,
                    "receitas_database"  // ← Nome do ficheiro BD
                )
                .fallbackToDestructiveMigration()  // ← Se erro, recria BD
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
```

**Estruturas de Código Importantes:**

#### `@Database` - Configuração da BD
```kotlin
@Database(
    entities = [...],  // Quais tabelas
    version = 1,       // Versão do schema
    exportSchema = true
)
```
- Define **todas as tabelas** e versão
- Room cria a BD baseado nisto

#### **Singleton Pattern** - Uma Única Instância
```kotlin
companion object {
    @Volatile
    private var INSTANCE: ReceitaRoomDatabase? = null
    
    fun getInstance(context: Context): ReceitaRoomDatabase {
        return INSTANCE ?: synchronized(this) {
            // Cria apenas se não existir
        }
    }
}
```

**Porquê Singleton?**
- **Apenas 1 conexão** à BD durante toda a vida da app
- Evita múltiplas conexões (usa memória)
- Thread-safe com `synchronized`

#### `@Volatile` - Visibilidade entre Threads
```kotlin
@Volatile
private var INSTANCE: ReceitaRoomDatabase? = null
```
- Garante que todas as threads vêem o valor mais recente
- Importante em ambientes multi-thread

#### `synchronized(this)` - Evita Condições de Corrida
```kotlin
synchronized(this) {
    // Apenas 1 thread pode executar isto de cada vez
}
```
- Se 2 threads tentarem criar ao mesmo tempo, uma espera
- Garante que criamos apenas 1 instância

**Fluxo de Criação:**
```
Thread A pede getInstance() ─┐
                             ├─→ synchronized block
Thread B pede getInstance() ─┘    │
                                   ▼
                              INSTANCE existe?
                                   │
                        ┌──────────┴──────────┐
                        ↓ NÃO                 ↓ SIM
                   Cria nova             Retorna existente
                   Guarda em INSTANCE
                   Retorna instância
```

---

### 5. ReceitaRepository.kt - Camada de Integração

**Caminho:** `data/repository/ReceitaRepository.kt`

```kotlin
class ReceitaRepository(
    private val receitaDao: ReceitaDao,
    private val ingredienteDao: IngredienteDao,
    private val passoDao: PassoDao
) {
    // Dados reativos
    val allReceitas: LiveData<List<ReceitaBd>> = receitaDao.getAllReceitas()
    val searchResults = MutableLiveData<List<ReceitaBd>>()
    
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    
    // INSERIR receita completa (receita + ingredientes + passos)
    fun insertReceita(
        receita: ReceitaBd, 
        ingredientes: List<IngredienteBd>, 
        passos: List<PassoBd>
    ) {
        coroutineScope.launch(Dispatchers.IO) {  // ← Background thread
            try {
                // 1. Inserir receita e obter ID
                val receitaId = receitaDao.insertReceita(receita).toInt()
                
                // 2. Inserir ingredientes com o receitaId
                ingredientes.forEach { ingrediente ->
                    ingredienteDao.insertIngrediente(
                        ingrediente.copy(receitaId = receitaId)
                    )
                }
                
                // 3. Inserir passos com o receitaId
                passos.forEach { passo ->
                    passoDao.insertPasso(
                        passo.copy(receitaId = receitaId)
                    )
                }
                
                Log.i("APP", "Receita $receitaId criada com sucesso")
            } catch (e: Exception) {
                Log.e("APP", "Erro ao criar receita: ${e.message}")
            }
        }
    }
    
    // ATUALIZAR
    fun updateReceita(receita: ReceitaBd) {
        coroutineScope.launch(Dispatchers.IO) {
            receitaDao.updateReceita(receita)
        }
    }
    
    // APAGAR
    fun deleteReceita(receita: ReceitaBd) {
        coroutineScope.launch(Dispatchers.IO) {
            receitaDao.deleteReceita(receita)
            // Ingredientes e passos apagam-se automaticamente (CASCADE)
        }
    }
    
    // PESQUISAR
    fun searchReceitas(termo: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val results = receitaDao.searchReceitas(termo).value ?: emptyList()
            searchResults.postValue(results)
        }
    }
}
```

**Estruturas de Código Importantes:**

#### **Repository Pattern** - Fonte Única de Dados
```kotlin
class ReceitaRepository(
    private val receitaDao: ReceitaDao,
    ...
)
```

**Responsabilidades:**
1. **Abstração** - ViewModel não sabe se dados vêm de Room ou API
2. **Lógica de negócio** - Transações complexas (inserir receita + ingredientes)
3. **Cache** - Decide quando usar local vs. remoto
4. **Tratamento de erros** - try/catch centralizado

#### `CoroutineScope` - Gestão de Corrotinas
```kotlin
private val coroutineScope = CoroutineScope(Dispatchers.Main)

coroutineScope.launch(Dispatchers.IO) {
    // Código que corre em background
}
```

**Dispatchers Explicados:**
- `Dispatchers.Main` → UI thread (atualizar ecrã)
- `Dispatchers.IO` → Input/Output (BD, rede, ficheiros)
- `Dispatchers.Default` → Computação pesada (processamento)

#### `launch` vs `async`
```kotlin
// launch - Fire and forget (não retorna valor)
coroutineScope.launch {
    dao.insertReceita(receita)
}

// async - Retorna resultado
val deferred = coroutineScope.async {
    dao.findReceitaById(1)
}
val receita = deferred.await()  // Espera pelo resultado
```

#### Transação Complexa com IDs
```kotlin
fun insertReceita(...) {
    // 1. Inserir receita
    val receitaId = receitaDao.insertReceita(receita).toInt()
    
    // 2. Usar o ID para ingredientes
    ingredientes.forEach { ingrediente ->
        ingredienteDao.insertIngrediente(
            ingrediente.copy(receitaId = receitaId)  // ← Liga ao pai
        )
    }
}
```

**Porquê esta ordem?**
1. Receita é inserida **primeiro** → gera ID
2. Ingredientes usam esse ID → `ForeignKey` válida
3. Se falhar, BD reverte tudo (transação atómica)

---

### 6. ReceitaViewModel.kt - Gestão de Estado

**Caminho:** `viewmodel/ReceitaViewModel.kt`

```kotlin
class ReceitaViewModel(application: Application) : ViewModel() {
    
    // Dados reativos expostos à UI
    val allReceitas: LiveData<List<ReceitaBd>>
    val searchResults: MutableLiveData<List<ReceitaBd>>
    
    private val repository: ReceitaRepository
    
    init {
        // 1. Obter instância da BD
        val database = ReceitaRoomDatabase.getInstance(application)
        
        // 2. Obter DAOs
        val receitaDao = database.receitaDao()
        val ingredienteDao = database.ingredienteDao()
        val passoDao = database.passoDao()
        
        // 3. Criar repository
        repository = ReceitaRepository(receitaDao, ingredienteDao, passoDao)
        
        // 4. Expor dados
        allReceitas = repository.allReceitas
        searchResults = repository.searchResults
    }
    
    // Métodos públicos para a UI
    fun insertReceita(
        receita: ReceitaBd,
        ingredientes: List<IngredienteBd> = emptyList(),
        passos: List<PassoBd> = emptyList()
    ) {
        repository.insertReceita(receita, ingredientes, passos)
    }
    
    fun deleteReceita(receita: ReceitaBd) {
        repository.deleteReceita(receita)
    }
    
    fun searchReceitas(termo: String) {
        repository.searchReceitas(termo)
    }
}
```

**Estruturas de Código Importantes:**

#### **ViewModel** - Sobrevive a Mudanças de Configuração
```kotlin
class ReceitaViewModel : ViewModel() {
    // Dados aqui sobrevivem a rotações de ecrã
}
```

**Ciclo de Vida:**
```
Activity criada ──→ ViewModel criado
      │                   │
      ↓                   │ (dados mantidos)
Activity destruída        │
(rotação de ecrã)         │
      ↓                   │
Activity recriada ──→ Reutiliza mesmo ViewModel
```

#### `init {}` - Construtor do ViewModel
```kotlin
init {
    // Executado quando ViewModel é criado
    val database = ReceitaRoomDatabase.getInstance(application)
    repository = ReceitaRepository(...)
}
```
- Corre **uma vez** quando ViewModel é instanciado
- Inicializa dependências (BD, repository)

#### Separação de Responsabilidades
```kotlin
// ❌ ERRADO - UI fala diretamente com BD
Button(onClick = { 
    database.receitaDao().insert(receita)
})

// ✅ CERTO - UI → ViewModel → Repository → BD
Button(onClick = { 
    viewModel.insertReceita(receita)
})
```

**Vantagens:**
- UI não sabe que Room existe
- Fácil trocar Room por outra BD
- Testes mais simples (mock do repository)

---

### 7. Ecrãs (Jetpack Compose)

**Exemplo:** `ui/screens/ListReceitasScreen.kt`

```kotlin
@Composable
fun ListReceitasScreen(
    viewModel: ReceitaViewModel = viewModel(),
    onReceitaClick: (ReceitaBd) -> Unit
) {
    // Observar dados do ViewModel
    val receitas by viewModel.allReceitas.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    
    Column {
        // Barra de pesquisa
        SearchBar(
            onSearch = { termo ->
                viewModel.searchReceitas(termo)
            }
        )
        
        // Lista de receitas
        if (isLoading) {
            CircularProgressIndicator()  // Loading spinner
        } else {
            LazyColumn {
                items(receitas) { receita ->
                    ReceitaCard(
                        receita = receita,
                        onClick = { onReceitaClick(receita) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReceitaCard(receita: ReceitaBd, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = receita.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text("Tempo: ${receita.tempoCozimento} min")
            Text("Serve: ${receita.pessoasServidas} pessoas")
        }
    }
}
```

**Estruturas de Código Importantes:**

#### `@Composable` - Função que Desenha UI
```kotlin
@Composable
fun ListReceitasScreen() {
    // Esta função "compõe" a interface
}
```
- **Declarativa** - descreves o que queres, não como fazer
- Re-executa quando dados mudam
- Sem XML (tudo em Kotlin)

#### `observeAsState` - Reatividade
```kotlin
val receitas by viewModel.allReceitas.observeAsState(emptyList())
```
- Converte `LiveData` em `State` do Compose
- Quando `allReceitas` muda → `receitas` atualiza → UI redesenha
- `by` é **delegação de propriedade** (sintaxe Kotlin)

#### `LazyColumn` - Lista Preguiçosa
```kotlin
LazyColumn {
    items(receitas) { receita ->
        ReceitaCard(receita)
    }
}
```
- Como `RecyclerView` no Android clássico
- Apenas renderiza itens visíveis (performance)
- `items()` cria um item para cada receita

#### `Modifier` - Estilização
```kotlin
modifier = Modifier
    .fillMaxWidth()       // Largura total
    .padding(8.dp)        // Espaçamento
    .clickable { ... }    // Clicável
```
- **Encadeável** - podes combinar múltiplos modificadores
- Define tamanho, posição, padding, cliques, etc.

#### Composição e Recomposição
```
1️⃣ Composição inicial
   ListReceitasScreen() executada
   └─ receitas = []
   └─ Desenha lista vazia

2️⃣ Dados chegam
   allReceitas.value = [Receita1, Receita2]

3️⃣ Recomposição
   ListReceitasScreen() executada NOVAMENTE
   └─ receitas = [Receita1, Receita2]
   └─ Redesenha lista com 2 itens
```

---

## 🔄 Estruturas de Código e Sua Influência

### 1. Coroutines e `suspend` - Concorrência

```kotlin
// Função normal - bloqueia thread
fun insertReceita() {
    dao.insert(receita)  // App trava até acabar
}

// Função suspend - não bloqueia
suspend fun insertReceita() {
    dao.insert(receita)  // Roda em background
}

// Como usar
viewModelScope.launch {  // ← Cria coroutine
    insertReceita()  // ← Chama função suspend
}
```

**Influência:**
- App não trava durante operações demoradas
- Melhor experiência do utilizador
- Menos crashes (ANR - Application Not Responding)

### 2. LiveData e StateFlow - Reatividade

```kotlin
// LiveData (Room)
val receitas: LiveData<List<ReceitaBd>> = dao.getAllReceitas()

// Observar mudanças
receitas.observe(lifecycleOwner) { novasReceitas ->
    // Atualiza UI automaticamente
    adapter.submitList(novasReceitas)
}

// StateFlow (mais moderno)
val receitas: StateFlow<List<ReceitaBd>> = _receitas.asStateFlow()

// No Compose
val receitas by viewModel.receitas.collectAsState()
```

**Influência:**
- **UI sempre sincronizada** com dados
- Sem refresh manual
- Menos bugs (impossível mostrar dados velhos)

### 3. Data Classes - Imutabilidade

```kotlin
data class ReceitaBd(
    val titulo: String,
    val descricao: String
)

// Copiar com alterações
val receitaOriginal = ReceitaBd("Lasanha", "Deliciosa")
val receitaEditada = receitaOriginal.copy(descricao = "Muito deliciosa")
```

**Influência:**
- Imutável → não muda por acidente
- `copy()` cria nova instância
- Seguro em ambientes multi-thread

### 4. Sealed Classes - Estados Controlados

```kotlin
sealed class ReceitaState {
    object Loading : ReceitaState()
    data class Success(val receitas: List<ReceitaBd>) : ReceitaState()
    data class Error(val message: String) : ReceitaState()
}

// No ViewModel
val state: MutableStateFlow<ReceitaState> = MutableStateFlow(Loading)

// Na UI
when (val currentState = state.value) {
    is Loading -> ShowLoader()
    is Success -> ShowList(currentState.receitas)
    is Error -> ShowError(currentState.message)
}
```

**Influência:**
- Estados explícitos e seguros
- Impossível esquecer casos
- Código mais legível

### 5. Extension Functions - Código Limpo

```kotlin
// Extensão para String
fun String.toReceitaDificuldade(): Dificuldade {
    return when(this.lowercase()) {
        "fácil" -> Dificuldade.FACIL
        "médio" -> Dificuldade.MEDIO
        else -> Dificuldade.DIFICIL
    }
}

// Uso
val dificuldade = "Fácil".toReceitaDificuldade()
```

**Influência:**
- Adiciona funcionalidades sem herança
- Código mais idiomático
- Reutilização

---

## 🌊 Fluxo de Dados Completo

### Exemplo: Criar uma Receita

```
┌─────────────────────────────────────────────────────────────┐
│ 1️⃣ UTILIZADOR PREENCHE FORMULÁRIO                          │
│    CreateReceitaScreen.kt                                   │
└──────────────┬──────────────────────────────────────────────┘
               ↓ Clica "Guardar"
┌──────────────▼──────────────────────────────────────────────┐
│ 2️⃣ ECRÃ CHAMA VIEWMODEL                                    │
│    Button(onClick = {                                       │
│        viewModel.insertReceita(                             │
│            receita = ReceitaBd(...),                        │
│            ingredientes = [...],                            │
│            passos = [...]                                   │
│        )                                                    │
│    })                                                       │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 3️⃣ VIEWMODEL DELEGA AO REPOSITORY                          │
│    ReceitaViewModel.kt                                      │
│    fun insertReceita(...) {                                 │
│        repository.insertReceita(...)                        │
│    }                                                        │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 4️⃣ REPOSITORY EXECUTA TRANSAÇÃO                            │
│    ReceitaRepository.kt                                     │
│    coroutineScope.launch(Dispatchers.IO) {                 │
│        val id = receitaDao.insertReceita(receita)          │
│        ingredientes.forEach { ... }                         │
│        passos.forEach { ... }                               │
│    }                                                        │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 5️⃣ DAO INSERE NA BASE DE DADOS                             │
│    ReceitaDao.kt                                            │
│    @Insert                                                  │
│    suspend fun insertReceita(receita: ReceitaBd): Long     │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 6️⃣ ROOM EXECUTA SQL                                        │
│    INSERT INTO receitas VALUES (...)                        │
│    INSERT INTO ingredientes VALUES (...)                    │
│    INSERT INTO passos VALUES (...)                          │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 7️⃣ LIVEDATA NOTIFICA MUDANÇA                               │
│    allReceitas emite nova lista                             │
└──────────────┬──────────────────────────────────────────────┘
               ↓
┌──────────────▼──────────────────────────────────────────────┐
│ 8️⃣ UI ATUALIZA AUTOMATICAMENTE                             │
│    ListReceitasScreen vê nova receita na lista              │
│    ✅ Receita aparece sem refresh manual                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧪 Como Funciona (Exemplos Práticos)

### Cenário 1: Ver Lista de Receitas

1. Abres a app → `MainActivity` executa
2. `AppNavigation` mostra `DashboardScreen`
3. `DashboardScreen` pede receitas ao `ReceitaViewModel`
4. `ReceitaViewModel` expõe `allReceitas: LiveData`
5. `allReceitas` vem de `ReceitaRepository`
6. `Repository` retorna `receitaDao.getAllReceitas()`
7. Room executa `SELECT * FROM receitas`
8. Dados chegam → LiveData notifica → UI redesenha

### Cenário 2: Criar Receita Offline

1. Preenches formulário em `CreateReceitaScreen`
2. Clicas "Guardar"
3. ViewModel → Repository → DAO → Room
4. Room guarda no SQLite local (ficheiro `.db`)
5. LiveData notifica mudança
6. Lista atualiza com nova receita
7. **Tudo sem internet!**

### Cenário 3: Modo Avião (Demonstração)

```kotlin
// RecipeRepository.kt (gestão de online/offline)
fun getRecipes(): Flow<List<Recipe>> = flow {
    try {
        // 1️⃣ Tenta buscar online
        val remotas = apiService.getRecipes()
        
        // 2️⃣ Guarda em cache (Room)
        roomDatabase.insertAll(remotas)
        
        // 3️⃣ Emite dados frescos
        emit(remotas)
    } catch (e: IOException) {
        // 4️⃣ Se falhar (sem internet), usa cache
        val locais = roomDatabase.getAllRecipes()
        emit(locais)
    }
}
```

**Comportamento:**
- **Com internet:** Mostra dados atualizados + guarda em cache
- **Sem internet:** Mostra cache (última versão guardada)
- **App funciona sempre!**

---

## 📊 Comparação: Antes vs Depois

### Abordagem Antiga (sem arquitetura)

```kotlin
// Activity faz tudo ❌
class MainActivity : AppCompatActivity() {
    private val database = ReceitaRoomDatabase.getInstance(this)
    
    fun onCreate() {
        // Acesso direto à BD
        Thread {
            val receitas = database.receitaDao().getAll()
            runOnUiThread {
                adapter.submitList(receitas)
            }
        }.start()
    }
}
```

**Problemas:**
- Activity faz tudo (BD + UI)
- Thread manual (propenso a erros)
- Dados perdidos em rotações
- Difícil de testar

### Abordagem MVVM (este projeto) ✅

```kotlin
// Activity apenas UI
class MainActivity : ComponentActivity() {
    fun onCreate() {
        setContent {
            AppNavigation()
        }
    }
}

// ViewModel gere estado
class ReceitaViewModel(app: Application) : ViewModel() {
    val receitas = repository.allReceitas
}

// Repository gere dados
class ReceitaRepository(...) {
    val allReceitas = receitaDao.getAllReceitas()
}
```

**Vantagens:**
- Separação clara de responsabilidades
- Coroutines automáticas (viewModelScope)
- Dados sobrevivem a mudanças
- Testável (mock de cada camada)

---

## 🎓 Conceitos-Chave para Apresentação

### 1. **MVVM** - Separação de Preocupações
- **Model:** Dados (Room, API)
- **View:** UI (Compose)
- **ViewModel:** Ponte entre os dois

### 2. **Room** - ORM Android
- Abstração sobre SQLite
- Gera SQL automaticamente
- Type-safe (erros em compile-time)

### 3. **Coroutines** - Concorrência Moderna
- Alternativa a Threads e Callbacks
- Código linear (parece síncrono)
- Eficiente (lightweight)

### 4. **Jetpack Compose** - UI Declarativa
- Sem XML
- Reativa (atualiza automaticamente)
- Menos código boilerplate

### 5. **Repository Pattern** - Abstração de Dados
- Fonte única de verdade
- Facilita testes
- Isola lógica de negócio

---

## 💡 Dicas para Apresentação ao Professor

### 1. Mostra o Fluxo Visual
Desenha no quadro:
```
UI → ViewModel → Repository → Room/API
```

### 2. Explica as Anotações
- `@Entity` = Tabela
- `@Dao` = Operações
- `@Composable` = UI

### 3. Demonstra Reatividade
```kotlin
// Quando isto muda...
dao.insertReceita(receita)

// ...isto atualiza automaticamente
val receitas = dao.getAllReceitas().observeAsState()
```

### 4. Mostra Offline-First
- Liga modo avião
- App continua funcional
- Explica cache com Room

### 5. Código Limpo
- `suspend` vs bloqueante
- `LiveData` vs polling manual
- MVVM vs Activity God Object

---

## 📝 Resumo Final

| Ficheiro | Responsabilidade | Analogia |
|----------|------------------|----------|
| **MainActivity.kt** | Ponto de entrada | Porta de entrada |
| **ReceitaEntities.kt** | Define tabelas | Planta da casa |
| **ReceitaDao.kt** | Operações CRUD | Receituário médico |
| **ReceitaRoomDatabase.kt** | Instância BD | Arquivo físico |
| **ReceitaRepository.kt** | Lógica negócio | Bibliotecário |
| **ReceitaViewModel.kt** | Estado UI | Gerente |
| **ListReceitasScreen.kt** | Interface | Montra da loja |

**Fluxo Simplificado:**
```
👤 Utilizador → 📱 Ecrã → 🧠 ViewModel → 📦 Repository → 💾 Room
```

---

**Boa sorte na tua apresentação! 🚀**

Se tiveres dúvidas sobre algum ficheiro ou conceito específico, consulta este README ou pergunta!
