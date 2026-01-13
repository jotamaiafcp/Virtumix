package com.example.cookaplication_a043302.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookaplication_a043302.data.local.entities.PassoBd
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutionScreenLocal(
    recipeId: Int,
    receitaViewModel: ReceitaViewModel,
    onExit: () -> Unit = {}
) {
    val allReceitas = receitaViewModel.allReceitas.observeAsState().value ?: emptyList()
    val receita = remember(recipeId, allReceitas) { allReceitas.find { it.idReceita == recipeId } }
    
    val currentStepIndex = remember { mutableStateOf(0) }
    val activity = (LocalContext.current as? Activity)
    
    // Manter ecrã ligado durante cozinha
    DisposableEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    if (receita == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // TODO: Buscar passos da BD
        // Por enquanto, criar passos de exemplo
        val passos = remember { 
            listOf(
                PassoBd(idPasso = 1, receitaId = recipeId, ordem = 1, instrucao = "Preparar ingredientes", duracao = 5),
                PassoBd(idPasso = 2, receitaId = recipeId, ordem = 2, instrucao = "Misturar os ingredientes", duracao = 10),
                PassoBd(idPasso = 3, receitaId = recipeId, ordem = 3, instrucao = "Cozer no forno", duracao = 30)
            )
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "${receita.titulo} - Passo ${currentStepIndex.value + 1}/${passos.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onExit) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    actions = {
                        IconButton(onClick = onExit) {
                            Icon(Icons.Default.Close, contentDescription = "Sair")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6200EE),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            if (passos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum passo disponível", fontSize = 16.sp)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Passo Atual
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Passo ${passos[currentStepIndex.value].ordem}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                passos[currentStepIndex.value].instrucao,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )

                            if (passos[currentStepIndex.value].duracao > 0) {
                                Spacer(modifier = Modifier.height(24.dp))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFF5F5F5)
                                    )
                                ) {
                                    Text(
                                        "Duração estimada: ${passos[currentStepIndex.value].duracao} minutos",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navegação
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (currentStepIndex.value > 0) {
                                    currentStepIndex.value--
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            enabled = currentStepIndex.value > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF03DAC6)
                            )
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Anterior")
                        }

                        Button(
                            onClick = {
                                if (currentStepIndex.value < passos.size - 1) {
                                    currentStepIndex.value++
                                } else {
                                    onExit()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6200EE)
                            )
                        ) {
                            Text(
                                if (currentStepIndex.value < passos.size - 1) "Próximo" else "Concluído"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = "Próximo")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Indicador de progresso
                    LinearProgressIndicator(
                        progress = (currentStepIndex.value + 1) / passos.size.toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
