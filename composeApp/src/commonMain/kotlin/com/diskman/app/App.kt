package com.diskman.app

import androidx.compose.runtime.Composable
import com.diskman.ui.Screen
import com.diskman.state.InventoryState
import com.diskman.ui.menus.StandardMenuScreen

/**
 * Different screens of the application, part of UI interaction
 *
 * @param state Inventory State (MV)
 */
@Composable
fun App(state: InventoryState) {
    when (state.currentScreen.value) {
        Screen.LOGIN -> com.diskman.ui.entry.LoginScreen(state)
        Screen.REGISTER -> com.diskman.ui.entry.RegisterScreen(
            state
        )
        Screen.ADMIN_MENU -> com.diskman.ui.menus.AdminMenuScreen(
            state
        )
        Screen.STANDARD_MENU -> StandardMenuScreen(state)
        Screen.CREATE_V -> com.diskman.ui.vinyls.AddVinylScreen(
            state
        )
        Screen.DELETE_V -> com.diskman.ui.vinyls.DeleteVinylScreen(
            state
        )
        Screen.FIND_V -> com.diskman.ui.vinyls.FindVinylScreen(
            state
        )
        Screen.SHOW_CV -> com.diskman.ui.vinyls.ShowCollVinylScreen(
            state
        )
        Screen.SHOW_V -> com.diskman.ui.vinyls.ShowVinylScreen(
            state
        )
        Screen.CREATE_U -> com.diskman.ui.users.AddUserScreen(state)
        Screen.DELETE_U -> com.diskman.ui.users.DeleteUserScreen(
            state
        )
        Screen.FIND_U -> com.diskman.ui.users.FindUserScreen(state)
        Screen.SHOW_U -> com.diskman.ui.users.ShowUserScreen(state)
        Screen.CREATE_P -> com.diskman.ui.purchases.AddPurchaseScreen(
            state
        )
        Screen.DELETE_P -> com.diskman.ui.purchases.DeletePurchaseScreen(
            state
        )
        Screen.SHOW_P -> com.diskman.ui.purchases.ShowPurchasesScreen(
            state
        )
        Screen.FIND_P -> com.diskman.ui.purchases.FindPurchaseScreen(
            state
        )
        Screen.CREATE_S -> com.diskman.ui.sales.AddSaleScreen(state)
        Screen.FIND_S -> com.diskman.ui.sales.FindSaleScreen(state)
        Screen.DELETE_S -> com.diskman.ui.sales.DeleteSaleScreen(
            state
        )
        Screen.SHOW_S -> com.diskman.ui.sales.ShowSalesScreen(state)
    }
}