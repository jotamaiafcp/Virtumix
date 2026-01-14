package com.example.cookaplication_a043302.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookaplication_a043302.data.local.entities.IngredienteBd
import com.example.cookaplication_a043302.data.local.entities.PassoBd
import com.example.cookaplication_a043302.data.local.entities.ReceitaBd
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReceitaScreen(
    viewModel: ReceitaViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    
    // Estados para receita base
    val titulo = remember { mutableStateOf("") }
    val descricao = remember { mutableStateOf("") }
    val categoria = remember { mutableStateOf("") }
    val pessoasServidas = remember { mutableStateOf("1") }
    val tempoPreparacao = remember { mutableStateOf("") }
    val tempoCozimento = remember { mutableStateOf("") }
    val dificuldade = remember { mutableStateOf("Médio") }

    // Estados para ingredientes
    val nomeIngrediente = remember { mutableStateOf("") }
    val quantidadeIngrediente = remember { mutableStateOf("") }
    val unidadeIngrediente = remember { mutableStateOf("") }
    val ingredientes = remember { mutableStateOf<List<IngredienteBd>>(emptyList()) }

    // Estados para passos
    val descricaoPasso = remember { mutableStateOf("") }
    val duracao = remember { mutableStateOf("") }
    val passos = remember { mutableStateOf<List<PassoBd>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Receita") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        // Seção Receita Base
        item {
            Text(
                text = "Informações da Receita",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = titulo.value,
                onValueChange = { titulo.value = it },
                label = { Text("Título da Receita *") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = descricao.value,
                onValueChange = { descricao.value = it },
                label = { Text("Descrição") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                maxLines = 3
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = categoria.value,
                    onValueChange = { categoria.value = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = pessoasServidas.value,
                    onValueChange = { pessoasServidas.value = it },
                    label = { Text("Pessoas") },
                    modifier = Modifier.weight(0.8f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tempoPreparacao.value,
                    onValueChange = { tempoPreparacao.value = it },
                    label = { Text("Prep. (min)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = tempoCozimento.value,
                    onValueChange = { tempoCozimento.value = it },
                    label = { Text("Cozimento (min)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dificuldade:", modifier = Modifier.weight(1f))
                FilterChip(
                    selected = dificuldade.value == "Fácil",
                    onClick = { dificuldade.value = "Fácil" },
                    label = { Text("Fácil") },
                    modifier = Modifier.padding(4.dp)
                )
                FilterChip(
                    selected = dificuldade.value == "Médio",
                    onClick = { dificuldade.value = "Médio" },
                    label = { Text("Médio") },
                    modifier = Modifier.padding(4.dp)
                )
                FilterChip(
                    selected = dificuldade.value == "Difícil",
                    onClick = { dificuldade.value = "Difícil" },
                    label = { Text("Difícil") },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Seção Ingredientes
        item {
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Text(
                text = "Ingredientes (${ingredientes.value.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedTextField(
                    value = nomeIngrediente.value,
                    onValueChange = { nomeIngrediente.value = it },
                    label = { Text("Nome") },
                    modifier = Modifier.weight(1.5f)
                )
                OutlinedTextField(
                    value = quantidadeIngrediente.value,
                    onValueChange = { quantidadeIngrediente.value = it },
                    label = { Text("Qtd") },
                    modifier = Modifier.weight(0.8f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
                OutlinedTextField(
                    value = unidadeIngrediente.value,
                    onValueChange = { unidadeIngrediente.value = it },
                    label = { Text("Unidade") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Button(
                onClick = {
                    if (nomeIngrediente.value.isNotBlank() && 
                        quantidadeIngrediente.value.isNotBlank() && 
                        unidadeIngrediente.value.isNotBlank()) {
                        val novoIngrediente = IngredienteBd(
                            receitaId = 0,
                            nome = nomeIngrediente.value,
                            quantidade = quantidadeIngrediente.value.toDoubleOrNull() ?: 0.0,
                            unidade = unidadeIngrediente.value
                        )
                        ingredientes.value = ingredientes.value + novoIngrediente
                        nomeIngrediente.value = ""
                        quantidadeIngrediente.value = ""
                        unidadeIngrediente.value = ""
                    } else {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Ingrediente")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Adicionar Ingrediente")
            }
        }

        items(ingredientes.value) { ing ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ing.nome, fontWeight = FontWeight.Bold)
                        Text("${ing.quantidade} ${ing.unidade}", fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        ingredientes.value = ingredientes.value.filter { it != ing }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }

        // Seção Passos
        item {
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Text(
                text = "Modo de Preparação (${passos.value.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = descricaoPasso.value,
                onValueChange = { descricaoPasso.value = it },
                label = { Text("Descrição do Passo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                maxLines = 2
            )
        }

        item {
            OutlinedTextField(
                value = duracao.value,
                onValueChange = { duracao.value = it },
                label = { Text("Duração (minutos) - Opcional") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        item {
            Button(
                onClick = {
                    if (descricaoPasso.value.isNotBlank()) {
                        val novoPasso = PassoBd(
                            receitaId = 0,
                            ordem = passos.value.size + 1,
                            instrucao = descricaoPasso.value,
                            duracao = duracao.value.toIntOrNull() ?: 0
                        )
                        passos.value = passos.value + novoPasso
                        descricaoPasso.value = ""
                        duracao.value = ""
                    } else {
                        Toast.makeText(context, "Preencha a descrição do passo", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Passo")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Adicionar Passo")
            }
        }

        items(passos.value) { passo ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Passo ${passo.ordem}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(passo.instrucao, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        if (passo.duracao > 0) {
                            Text("Duração: ${passo.duracao} min", fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                    IconButton(onClick = {
                        passos.value = passos.value.filter { it != passo }
                            .mapIndexed { idx, p -> p.copy(ordem = idx + 1) }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }

        // Botão Guardar
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (titulo.value.isBlank()) {
                        Toast.makeText(context, "O título da receita é obrigatório", Toast.LENGTH_SHORT).show()
                    } else if (ingredientes.value.isEmpty()) {
                        Toast.makeText(context, "Adicione pelo menos um ingrediente", Toast.LENGTH_SHORT).show()
                    } else if (passos.value.isEmpty()) {
                        Toast.makeText(context, "Adicione pelo menos um passo", Toast.LENGTH_SHORT).show()
                    } else {
                        val novaReceita = ReceitaBd(
                            titulo = titulo.value,
                            descricao = descricao.value,
                            categoria = categoria.value,
                            pessoasServidas = pessoasServidas.value.toIntOrNull() ?: 1,
                            tempoPreparacao = tempoPreparacao.value.toIntOrNull() ?: 0,
                            tempoCozimento = tempoCozimento.value.toIntOrNull() ?: 0,
                            dificuldade = dificuldade.value
                        )
                        viewModel.insertReceita(novaReceita, ingredientes.value, passos.value)

                        // Resetar formulário
                        titulo.value = ""
                        descricao.value = ""
                        categoria.value = ""
                        pessoasServidas.value = "1"
                        tempoPreparacao.value = ""
                        tempoCozimento.value = ""
                        dificuldade.value = "Médio"
                        ingredientes.value = emptyList()
                        passos.value = emptyList()

                        Toast.makeText(context, "Receita criada com sucesso!", Toast.LENGTH_SHORT).show()
                        
                        // Voltar para o dashboard
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Guardar Receita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    }
}
