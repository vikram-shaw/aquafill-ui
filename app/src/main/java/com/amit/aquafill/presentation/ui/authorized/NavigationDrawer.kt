package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amit.aquafill.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//@ExperimentalMaterial3Api
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    AquaFillTheme {
//        NavigationDrawer(rememberNavController())
//    }
//}


@ExperimentalMaterial3Api
@Composable
fun NavigationDrawer(navController: NavHostController) {
    val state = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerContent = {
        NavigationDrawerContent(navController, state, scope)
    },
        scrimColor = Color.LightGray,
        drawerState = state
    ) {
//        TopAppBar(
//            title = { },
//            navigationIcon = {
//                IconButton(onClick = {
//                    scope.launch {
//                        if(state.isOpen)
//                            state.close()
//                        else
//                            state.open()
//                    }
//                }) {
//                    Icon(Icons.Rounded.Menu, contentDescription = null)
//                }
//            },
//        )
    }
}

@Composable
fun NavigationDrawerContent(navController: NavHostController, state: DrawerState, scope: CoroutineScope) {
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
        Divider(color = Color.LightGray)
        Column {
            Spacer(modifier = Modifier.padding(7.dp))
            Text("Amit Shaw")
            Spacer(modifier = Modifier.padding(7.dp))
            Text("shaw2amit@gmail.com")
            Spacer(modifier = Modifier.padding(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.popBackStack("entries", false)
                    scope.launch {
                        state.close()
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
                    navController.popBackStack("entries", false)
                    navController.navigate("customer")
                    scope.launch {
                        state.close()
                    }
                }) {
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
