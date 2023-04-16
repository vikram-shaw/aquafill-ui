package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amit.aquafill.R
import com.amit.aquafill.network.model.UserDto
import com.amit.aquafill.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@ExperimentalMaterial3Api
@Composable
fun NavigationDrawer() {
    val state = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = remember { mutableStateOf("entries") }

    ModalNavigationDrawer(drawerContent = {
            NavigationDrawerContent(currentRoute, state, scope)
        },
        content = {
            if(currentRoute.value == "entries") {
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
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(shape = RoundedCornerShape(200.dp)) {
                Text(
                    text = "AS",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
        Column {
            Spacer(modifier = Modifier.padding(7.dp))
            Text("Amit shaw")
            Spacer(modifier = Modifier.padding(7.dp))
            Text("shaw2amit@gmail.com")
            Spacer(modifier = Modifier.padding(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    scope.launch {
                        state.close()
                        currentRoute.value = "entries"
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
                        currentRoute.value = "customer"
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
