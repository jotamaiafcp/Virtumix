
package com.example.cookaplication_a043302.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookaplication_a043302.viewmodel.ExecutionViewModel
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutionScreen(
    recipeId: String?,
    onExit: () -> Unit,
    viewModel: ExecutionViewModel = viewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()

    val activity = (LocalContext.current as? Activity)
    DisposableEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Text(
                        "Passo ${currentStepIndex + 1} de $totalSteps",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.Close, contentDescription = "Sair")
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Barra de progresso
            LinearProgressIndicator(
                progress = { if (totalSteps > 0) (currentStepIndex + 1).toFloat() / totalSteps else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF6200EE),
                trackColor = Color.LightGray
            )
            
            // Card com instrução
            if (currentStep != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "👨‍🍳",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Text(
                            text = currentStep?.instruction ?: "Carregando...",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp
                        )
                    }
                }

                // Timer
                currentStep?.timerSeconds?.let { seconds ->
                    CountdownTimerButton(seconds = seconds)
                }
            } else {
                Text("Nenhum passo disponível")
            }

            // Botões de navegação
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.previousStep() }, 
                    enabled = currentStepIndex > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Anterior", fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = { viewModel.nextStep() }, 
                    enabled = currentStepIndex < totalSteps - 1,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Próxima", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Próxima")
                }
            }
        }
    }
}

@Composable
fun CountdownTimerButton(seconds: Int) {
    var remainingTime by remember { mutableStateOf(seconds) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingTime) {
        if (isRunning && remainingTime > 0) {
            delay(1000L)
            remainingTime--
        } else if (remainingTime == 0) {
            isRunning = false
        }
    }
    
    val minutes = remainingTime / 60
    val secs = remainingTime % 60
    val displayTime = String.format("%02d:%02d", minutes, secs)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "⏱️ Timer",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                displayTime,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Button(
                onClick = { if (remainingTime > 0) isRunning = !isRunning },
                enabled = remainingTime > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color(0xFFFF5252) else Color(0xFF6200EE),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    if (isRunning) "⏸️ Pausar" else "▶️ Iniciar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}