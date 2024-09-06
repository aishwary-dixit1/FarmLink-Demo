package com.example.farmlinkapp1.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.farmlinkapp1.common.AppScaffold
import com.example.farmlinkapp1.data.MongoDB
import com.example.farmlinkapp1.ui.for_seller.active_items.ActiveItemsScreen
import com.example.farmlinkapp1.ui.for_seller.add_item.AddItemScreen
import com.example.farmlinkapp1.ui.for_seller.seller_dashboard.SellerDashboardScreen
import com.example.farmlinkapp1.ui.for_seller.sold_items.SoldItemsScreen

fun NavGraphBuilder.sellerApp(navController: NavHostController) {
    navigation<SellerApp>(startDestination = SellerDashboard) {
        sellerDashboard(navController)
        soldItemsScreen(navController)
        activeItemsScreen(navController)
        addItemScreen(navController)
    }
}

fun NavGraphBuilder.sellerDashboard(navController: NavHostController) {
    composable<SellerDashboard> {
        AppScaffold(
            currentScreenTitle = "Farm Link",
            onNavigateUp = {},
            canNavigateUp = false
        ) {
            SellerDashboardScreen(
                user = MongoDB.getUser(),
                modifier = it,
                onNavigateToActiveItems = { navController.navigate(ActiveItems) },
                onNavigateToSoldItems = { navController.navigate(SoldItems) },
                onNavigateToAddItem = { navController.navigate(AddItem) }
            )
        }
    }
}

fun NavGraphBuilder.soldItemsScreen(navController: NavHostController) {
    composable<SoldItems> {
        AppScaffold(currentScreenTitle = "Sold Items", onNavigateUp = { navController.popBackStack() }) {
            SoldItemsScreen(allSaleItems = MongoDB.getAllSaleItemsForSeller(), modifier = it)
        }
    }
}

fun NavGraphBuilder.activeItemsScreen(navController: NavHostController) {
    composable<ActiveItems> {
        AppScaffold(currentScreenTitle = "Active Items", onNavigateUp = { navController.popBackStack() }) {
            ActiveItemsScreen(allSaleItems = MongoDB.getAllSaleItemsForSeller(), modifier = it)
        }
    }
}

fun NavGraphBuilder.addItemScreen(navController: NavHostController) {
    composable<AddItem> {
        AppScaffold(currentScreenTitle = "Add New Item", onNavigateUp = { navController.popBackStack() }) {
            AddItemScreen(navigateBack = { navController.navigateUp() }, modifier = it)
        }
    }
}
