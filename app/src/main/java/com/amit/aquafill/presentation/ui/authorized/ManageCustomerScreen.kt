package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amit.aquafill.R
import com.amit.aquafill.domain.model.Customer
import com.amit.aquafill.ui.theme.AquaFillTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    AquaFillTheme {
        ManageCustomerScreen()
    }
}

@Composable
fun ManageCustomerScreen() {
    val manageCustomerViewModel = hiltViewModel<ManageCustomerViewModel>()
    val uiState = manageCustomerViewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)){
        items(uiState.value.customers.size) {
            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(uiState.value.customers[it].name)
                        Text(uiState.value.customers[it].address)
                        Text(uiState.value.customers[it].phone.toString())
                    }
                    Row {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.af_delete_outline_24),
                            contentDescription = "delete"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.af_edit_24),
                            contentDescription = "edit"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ExtendedFloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(all = 20.dp)
                .align(alignment = Alignment.BottomEnd),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "add")
        }
    }
}