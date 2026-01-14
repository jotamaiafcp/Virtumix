
package com.example.cookaplication_a043302.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cookaplication_a043302.data.model.Recipe
import com.example.cookaplication_a043302.viewmodel.PlannerViewModel
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(
    onBack: () -> Unit = {},
    onOpenShoppingList: (Map<DayOfWeek, Recipe?>) -> Unit = {},
    viewModel: PlannerViewModel = viewModel()
) {
    val recipes by viewModel.allRecipes.collectAsState()
    val dayPlans by viewModel.dayPlans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var selectedDay by remember { mutableStateOf<DayOfWeek?>(null) }
    var showRecipeDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Planeador Semanal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6200EE))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFFAFAFA))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Mensagem informativa
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "📅 Planeie as suas refeições da semana",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Seleccione as receitas para cada dia da semana",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { onOpenShoppingList(dayPlans) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Lista")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Enviar para lista de compras")
                            }
                        }
                    }
                }
                
                // Dias da semana
                items((DayOfWeek.MONDAY..DayOfWeek.SUNDAY)) { dayOfWeek ->
                    DayPlanCard(
                        dayOfWeek = dayOfWeek,
                        recipe = dayPlans[dayOfWeek],
                        onAddClick = {
                            selectedDay = dayOfWeek
                            showRecipeDialog = true
                        },
                        onRemoveClick = {
                            viewModel.assignRecipeToDay(dayOfWeek, null)
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo para selecionar receita
    if (showRecipeDialog && selectedDay != null) {
        RecipeSelectionDialog(
            recipes = recipes,
            selectedDay = selectedDay!!,
            onRecipeSelected = { recipe ->
                viewModel.assignRecipeToDay(selectedDay!!, recipe)
                showRecipeDialog = false
                selectedDay = null
            },
            onDismiss = {
                showRecipeDialog = false
                selectedDay = null
            }
        )
    }
}

@Composable
fun DayPlanCard(
    dayOfWeek: DayOfWeek,
    recipe: Recipe?,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pt", "PT")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        recipe?.name ?: "Sem receita atribuída",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (recipe != null) Color.Black else Color.Gray,
                        fontWeight = if (recipe != null) FontWeight.Medium else FontWeight.Normal
                    )
                }
                
                if (recipe != null) {
                    IconButton(onClick = onRemoveClick) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remover receita",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onAddClick,
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar receita",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            // Mostrar preview da receita se existir
            if (recipe != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (recipe.image.isNotBlank()) {
                        AsyncImage(
                            model = recipe.image,
                            contentDescription = recipe.name,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "⏱️ ${recipe.cookTime?.let { "$it min" } ?: "Tempo não especificado"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            "👥 ${recipe.servings?.let { "$it porções" } ?: "Porções não especificadas"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeSelectionDialog(
    recipes: List<Recipe>,
    selectedDay: DayOfWeek,
    onRecipeSelected: (Recipe) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Seleccione uma receita para ${selectedDay.getDisplayName(TextStyle.FULL, Locale("pt", "PT"))}",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (recipe.image.isNotBlank()) {
                                AsyncImage(
                                    model = recipe.image,
                                    contentDescription = recipe.name,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.LightGray, RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    recipe.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (recipe.cookTime != null) {
                                    Text(
                                        "⏱️ ${recipe.cookTime} min",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                            
                            Button(
                                onClick = { onRecipeSelected(recipe) },
                                modifier = Modifier.size(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6200EE)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Seleccionar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

// Range para DayOfWeek
operator fun DayOfWeek.rangeTo(other: DayOfWeek): List<DayOfWeek> {
    val days = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )
    val startIndex = days.indexOf(this)
    val endIndex = days.indexOf(other)
    return if (startIndex <= endIndex) {
        days.subList(startIndex, endIndex + 1)
    } else {
        emptyList()
    }
}
