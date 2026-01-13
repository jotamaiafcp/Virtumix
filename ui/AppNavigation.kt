package com.example.cookaplication_a043302.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cookaplication_a043302.data.local.ShoppingListStore
import com.example.cookaplication_a043302.ui.screens.CreateReceitaScreen
import com.example.cookaplication_a043302.ui.screens.DashboardScreen
import com.example.cookaplication_a043302.ui.screens.ExecutionScreen
import com.example.cookaplication_a043302.ui.screens.ExecutionScreenLocal
import com.example.cookaplication_a043302.ui.screens.ListReceitasScreen
import com.example.cookaplication_a043302.ui.screens.PlannerScreen
import com.example.cookaplication_a043302.ui.screens.ProfileScreen
import com.example.cookaplication_a043302.ui.screens.RecipeDetailScreen
import com.example.cookaplication_a043302.ui.screens.RecipeDetailScreenLocal
import com.example.cookaplication_a043302.ui.screens.ShoppingListScreen
import com.example.cookaplication_a043302.ui.screens.AddRecipeScreen
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModel
import com.example.cookaplication_a043302.viewmodel.ReceitaViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

// --- Navigation Routes ---
object Routes {
	const val DASHBOARD = "dashboard"
	const val CREATE_RECEITA = "createReceita"
	const val LIST_RECEITAS = "listReceitas"
	const val ADD_RECIPE = "addRecipe"
	const val PLANNER = "planner"
	const val PROFILE = "profile"
	const val SHOPPING = "shopping"
	const val RECIPE_DETAIL = "recipeDetail/{recipeId}"
	const val EXECUTION = "execution/{recipeId}"

	fun recipeDetail(recipeId: String) = "recipeDetail/$recipeId"
	fun execution(recipeId: String) = "execution/$recipeId"
}

@Composable
fun AppNavigation() {
	val navController = rememberNavController()
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination

	val showBottomNav = currentDestination?.route == Routes.DASHBOARD ||
			currentDestination?.route == Routes.PLANNER ||
			currentDestination?.route == Routes.PROFILE ||
			currentDestination?.route == Routes.SHOPPING

	// Inicializar ReceitaViewModel
	val owner = LocalViewModelStoreOwner.current
	val receitaViewModel: ReceitaViewModel = if (owner != null) {
		viewModel(
			owner,
			"ReceitaViewModel",
			ReceitaViewModelFactory(
				LocalContext.current.applicationContext as Application
			)
		)
	} else {
		// Fallback - não deve acontecer
		ReceitaViewModel(LocalContext.current.applicationContext as Application)
	}

	Scaffold(
		bottomBar = {
			if (showBottomNav) {
				NavigationBar {
					NavigationBarItem(
						icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
						label = { Text("Receitas") },
						selected = currentDestination?.hierarchy?.any { it.route == Routes.DASHBOARD } == true,
						onClick = {
							navController.navigate(Routes.DASHBOARD) {
								popUpTo(navController.graph.findStartDestination().id) { saveState = true }
								launchSingleTop = true
								restoreState = true
							}
						}
					)
					NavigationBarItem(
						icon = { Icon(Icons.Default.DateRange, contentDescription = "Planner") },
						label = { Text("Planeador") },
						selected = currentDestination?.hierarchy?.any { it.route == Routes.PLANNER } == true,
						onClick = {
							navController.navigate(Routes.PLANNER) {
								popUpTo(navController.graph.findStartDestination().id) { saveState = true }
								launchSingleTop = true
								restoreState = true
							}
						}
					)
					NavigationBarItem(
						icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Compras") },
						label = { Text("Compras") },
						selected = currentDestination?.hierarchy?.any { it.route == Routes.SHOPPING } == true,
						onClick = {
							navController.navigate(Routes.SHOPPING) {
								popUpTo(navController.graph.findStartDestination().id) { saveState = true }
								launchSingleTop = true
								restoreState = true
							}
						}
					)
					NavigationBarItem(
						icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
						label = { Text("Perfil") },
						selected = currentDestination?.hierarchy?.any { it.route == Routes.PROFILE } == true,
						onClick = {
							navController.navigate(Routes.PROFILE) {
								popUpTo(navController.graph.findStartDestination().id) { saveState = true }
								launchSingleTop = true
								restoreState = true
							}
						}
					)
				}
			}
		}
	) { padding ->
		Box(modifier = Modifier
			.fillMaxSize()
			.padding(padding)) {
			NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
				composable(Routes.DASHBOARD) {
					DashboardScreen(
						onRecipeClick = { recipeId -> navController.navigate(Routes.recipeDetail(recipeId)) },
						onCreateReceitaClick = { navController.navigate(Routes.CREATE_RECEITA) },
						onListReceitasClick = { navController.navigate(Routes.LIST_RECEITAS) },
						onAddRecipeClick = { navController.navigate(Routes.ADD_RECIPE) }
					)
				}

				composable(Routes.CREATE_RECEITA) {
					CreateReceitaScreen(
						viewModel = receitaViewModel,
						onBack = { navController.popBackStack() }
					)
				}

				composable(Routes.LIST_RECEITAS) {
					ListReceitasScreen(
						viewModel = receitaViewModel,
						onRecipeClick = { recipeId -> navController.navigate(Routes.recipeDetail(recipeId.toString())) },
						onBack = { navController.popBackStack() }
					)
				}
				
				composable(Routes.ADD_RECIPE) {
					AddRecipeScreen(
						onBack = { navController.popBackStack() },
						onRecipeSaved = {
							navController.popBackStack()
							navController.navigate(Routes.DASHBOARD)
						}
					)
				}

				composable(Routes.PLANNER) {
					PlannerScreen(
						onBack = { navController.popBackStack() },
						onOpenShoppingList = { plans ->
							ShoppingListStore.importFromRecipes(plans.values.filterNotNull())
							navController.navigate(Routes.SHOPPING)
						}
					)
				}

				composable(Routes.SHOPPING) {
					ShoppingListScreen(onBack = { navController.popBackStack() })
				}

				composable(Routes.PROFILE) {
					ProfileScreen(onLogout = { navController.popBackStack() })
				}

				composable(
					route = Routes.RECIPE_DETAIL,
					arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
				) { backStackEntry ->
					val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "ID_NULO"
					
					// Tentar converter para Int - se for de receita local
					val recipeIdInt = recipeId.toIntOrNull()
					
					if (recipeIdInt != null) {
						// Receita criada localmente
						RecipeDetailScreenLocal(
							recipeId = recipeIdInt,
							receitaViewModel = receitaViewModel,
							onStartCookingClick = { id -> navController.navigate(Routes.execution(id)) },
							onBack = { navController.popBackStack() }
						)
					} else {
						// Receita remota da API
						RecipeDetailScreen(
							recipeId = recipeId,
							onStartCookingClick = { id -> navController.navigate(Routes.execution(id)) },
							onBack = { navController.popBackStack() }
						)
					}
				}

				composable(
					route = Routes.EXECUTION,
					arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
				) { backStackEntry ->
					val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "ID_NULO"
					
					// Tentar converter para Int - se for de receita local
					val recipeIdInt = recipeId.toIntOrNull()
					
					if (recipeIdInt != null) {
						// Receita criada localmente
						ExecutionScreenLocal(
							recipeId = recipeIdInt,
							receitaViewModel = receitaViewModel,
							onExit = { navController.popBackStack() }
						)
					} else {
						// Receita remota da API
						ExecutionScreen(
							recipeId = recipeId,
							onExit = { navController.popBackStack() }
						)
					}
				}
			}
		}
	}
}
