package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val customers = mutableListOf(
        Customer("vikram", "vikram", "18 abhay guha road", 8481958080),
        Customer("amit", "amit", "18 abhay guha road", 8276938891),
        Customer("pritam", "pritam", "18 abhay guha road", 9123798914),
        Customer("ankit", "ankit", "18 abhay guha road", 8276938891),
    )
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(customers.size) {
            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(customers[it].name)
                        Text(customers[it].address)
                        Text(customers[it].phone.toString())
                    }
                    Row {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.af_delete_outline_24),
                            contentDescription = "delete"
                        )
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.af_edit_24),
                            contentDescription = "edit"
                        )
                    }
                }
            }
        }
    }
}