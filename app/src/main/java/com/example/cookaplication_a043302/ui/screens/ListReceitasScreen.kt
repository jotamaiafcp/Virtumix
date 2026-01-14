package com.example.cookaplication_a043302.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListReceitasScreen(
    viewModel: ReceitaViewModel,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val receitas = viewModel.allReceitas.observeAsState().value ?: emptyList()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Receitas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (receitas.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Criar Receita",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Nenhuma receita criada",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "Comece a criar suas próprias receitas!",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Minhas Receitas (${receitas.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(receitas) { receita ->
                ReceitaCard(
                    receita = receita,
                    dateFormat = dateFormat,
                    onRecipeClick = { onRecipeClick(receita.idReceita) },
                    onDeleteClick = { viewModel.deleteReceita(receita) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        }
    }
}

@Composable
fun ReceitaCard(
    receita: ReceitaBd,
    dateFormat: SimpleDateFormat,
    onRecipeClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        receita.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    
                    if (receita.descricao.isNotBlank()) {
                        Text(
                            receita.descricao,
                            fontSize = 13.sp,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (receita.categoria.isNotBlank()) {
                            AssistChip(
                                onClick = {},
                                label = { Text(receita.categoria, fontSize = 11.sp) },
                                modifier = Modifier.height(24.dp)
                            )
                        }

                        AssistChip(
                            onClick = {},
                            label = { Text("${receita.pessoasServidas} pessoas", fontSize = 11.sp) },
                            modifier = Modifier.height(24.dp)
                        )

                        if (receita.dificuldade.isNotBlank()) {
                            AssistChip(
                                onClick = {},
                                label = { Text(receita.dificuldade, fontSize = 11.sp) },
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (receita.tempoPreparacao > 0) {
                            Text(
                                "Prep: ${receita.tempoPreparacao}min",
                                fontSize = 12.sp
                            )
                        }
                        if (receita.tempoCozimento > 0) {
                            Text(
                                "Cozimento: ${receita.tempoCozimento}min",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        "Criada em: ${dateFormat.format(Date(receita.criadoEm))}",
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar receita",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onRecipeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ver Detalhes", fontSize = 13.sp)
                }
            }
        }
    }
}
