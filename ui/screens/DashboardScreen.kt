package com.example.cookaplication_a043302.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: DashboardViewModel = viewModel(),
    onCreateReceitaClick: () -> Unit = {},
    onListReceitasClick: () -> Unit = {},
    onAddRecipeClick: () -> Unit = {}
) {
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    // Track current index to force recomposition when pagination changes
    val currentIndex by viewModel.currentIndex.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val availableTags by viewModel.availableTags.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "VirtuMix",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = onAddRecipeClick) {
                        Icon(Icons.Default.Add, contentDescription = "Nova Receita", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (!isLoading && recipes.isNotEmpty() && error == null) {
                PaginationControls(
                    onPrevious = { viewModel.previousPage() },
                    onNext = { viewModel.nextPage() },
                    canGoPrevious = viewModel.canGoPrevious(),
                    canGoNext = viewModel.canGoNext(),
                    pageInfo = viewModel.getCurrentPageInfo(),
                    currentIndex = currentIndex // consumed to trigger recomposition
                )
            }
        }
    ) { padding ->
        when {
            isLoading && recipes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp),
                            color = Color(0xFF6200EE)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "A carregar receitas...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "⚠️ Erro ao carregar receitas",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            error ?: "Erro desconhecido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
            recipes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Nenhuma receita disponível",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // Botões para criar/listar receitas próprias
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onCreateReceitaClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6200EE)
                            )
                        ) {
                            Text("Criar Receita", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onListReceitasClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF03DAC6)
                            )
                        ) {
                            Text("Minhas Receitas", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

                    SearchAndFilterBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.setSearchQuery(it) },
                        availableTags = availableTags,
                        selectedTags = selectedTags,
                        onTagToggle = { viewModel.toggleTag(it) },
                        onClearFilters = { viewModel.clearFilters() }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recipes) { recipe ->
                                RecipeListItem(
                                    recipe = recipe,
                                    isFavorite = favorites.contains(recipe.id),
                                    onToggleFavorite = { viewModel.toggleFavorite(recipe.id) },
                                    onClick = { onRecipeClick(recipe.id) }
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAndFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    availableTags: Set<String>,
    selectedTags: Set<String>,
    onTagToggle: (String) -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar") },
            placeholder = { Text("Pesquisar receitas") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        if (availableTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, contentDescription = "Filtrar", tint = Color(0xFF6200EE))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Filtros", fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onClearFilters, enabled = query.isNotBlank() || selectedTags.isNotEmpty()) {
                    Text("Limpar")
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableTags.toList()) { tag ->
                    FilterChip(
                        selected = selectedTags.contains(tag),
                        onClick = { onTagToggle(tag) },
                        label = { Text(tag) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagem de fundo
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Gradiente escuro sobre imagem
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )
            
            // Conteúdo (texto e tags) sobre a imagem
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Tags com chips bonitos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    recipe.tags.take(2).forEach { tag ->
                        Text(
                            text = tag,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF6200EE).copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(40.dp)
            ) {
                if (isFavorite) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Remover favorito", tint = Color.Red)
                } else {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Adicionar favorito", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun PaginationControls(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    pageInfo: String,
    currentIndex: Int
) {
    // Consume currentIndex to ensure recomposition when page changes
    val currentPageMarker = currentIndex
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPrevious,
                enabled = canGoPrevious,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Anterior")
            }
            
            Text(
                pageInfo,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = onNext,
                enabled = canGoNext,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Próxima")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Próxima")
            }
        }
    }
}