package com.example.cookaplication_a043302.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookaplication_a043302.data.local.FavoritesStore
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreenLocal(
    recipeId: Int,
    receitaViewModel: ReceitaViewModel,
    onStartCookingClick: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val allReceitas = receitaViewModel.allReceitas.observeAsState().value ?: emptyList()
    val receita = remember(recipeId, allReceitas) { allReceitas.find { it.idReceita == recipeId } }
    val ingredients = remember(recipeId, allReceitas) { 
        // Implementar busca de ingredientes
        emptyList<IngredienteBd>()
    }
    
    val favorites by FavoritesStore.favorites.collectAsState()

    if (receita == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            receita.titulo,
                            fontWeight = FontWeight.Bold
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Descrição
                if (receita.descricao.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Descrição",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(receita.descricao)
                            }
                        }
                    }
                }

                // Informações
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Informações",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text("Pessoas", fontSize = 12.sp, color = Color.Gray)
                                    Text("${receita.pessoasServidas}", fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Preparação", fontSize = 12.sp, color = Color.Gray)
                                    Text("${receita.tempoPreparacao}min", fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Cozimento", fontSize = 12.sp, color = Color.Gray)
                                    Text("${receita.tempoCozimento}min", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text("Dificuldade", fontSize = 12.sp, color = Color.Gray)
                                    Text(receita.dificuldade, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text("Categoria", fontSize = 12.sp, color = Color.Gray)
                                    Text(receita.categoria.ifBlank { "-" }, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Criada em: ${dateFormat.format(Date(receita.criadoEm))}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Ingredientes (placeholder - na versão futura vamos buscar da BD)
                item {
                    Text(
                        "Ingredientes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Detalhes de ingredientes não disponíveis nesta versão",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Passos (placeholder - na versão futura vamos buscar da BD)
                item {
                    Text(
                        "Modo de Preparação",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Detalhes de passos não disponíveis nesta versão",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { onStartCookingClick(receita.idReceita.toString()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE)
                        )
                    ) {
                        Text("Começar a Cozinhar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
