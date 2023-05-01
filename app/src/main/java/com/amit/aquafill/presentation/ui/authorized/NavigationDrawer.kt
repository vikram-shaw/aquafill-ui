package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amit.aquafill.R
import com.amit.aquafill.routes.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@ExperimentalMaterial3Api
@Composable
fun NavigationDrawer() {
    val state = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = remember { mutableStateOf(Routes.Customers.name) }

    ModalNavigationDrawer(drawerContent = {
            NavigationDrawerContent(currentRoute, state, scope)
        },
        content = {
            if(currentRoute.value == Routes.Entries.name) {
                ManageEntitiesScreen()
            } else {
                ManageCustomerScreen()
            }
        },
        scrimColor = Color.LightGray,
        drawerState = state
    )
}

@Composable
fun NavigationDrawerContent(currentRoute: MutableState<String>, state: DrawerState, scope: CoroutineScope) {
    val navigationDrawerViewModel = hiltViewModel<NavigationDrawerViewModel>()
    val uiState = navigationDrawerViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InitialName(uiState)
        }
        Column {
            Spacer(modifier = Modifier.padding(7.dp))
            Text(uiState.value.name)
            Spacer(modifier = Modifier.padding(7.dp))
            Text(uiState.value.email)
            Spacer(modifier = Modifier.padding(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    scope.launch {
                        state.close()
                        currentRoute.value = Routes.Entries.name
                    }
                }) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.af_entries_24),
                    contentDescription = "people alt",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Manage Entries")
            }

            Spacer(modifier = Modifier.padding(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    scope.launch {
                        state.close()
                        currentRoute.value = Routes.Customers.name
                    }
                }
            ) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.af_manage_customer_24),
                    contentDescription = "people alt",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Manage Customer")
            }
            Spacer(modifier = Modifier.padding(7.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {

                }) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.af_logout_24),
                    contentDescription = "people alt",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Logout")
            }
        }
    }
}
@Composable
private fun InitialName(uiState: State<NavigationDrawerUIState>) {
    Card(
        shape = CircleShape,
        modifier = Modifier.padding(30.dp)
    ) {
        Column(
            modifier = Modifier.size(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.value.initialName,
                fontSize = 30.sp,
            )
        }
    }
}
