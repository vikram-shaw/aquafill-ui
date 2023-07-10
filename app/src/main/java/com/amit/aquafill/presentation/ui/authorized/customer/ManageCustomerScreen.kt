package com.amit.aquafill.presentation.ui.authorized.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.amit.aquafill.R
import com.amit.aquafill.ui.theme.AquaFillTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    AquaFillTheme {

    }
}

@Composable
fun AddCustomer(
    manageCustomerViewModel: ManageCustomerViewModel,
    close: () -> Unit = {}
) {
    val addOrUpdateCustomerUIState by manageCustomerViewModel.uiAddOrUpdateCustomerState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Text(
            text = if(addOrUpdateCustomerUIState.id == null ) "Add Customer" else "Update Customer",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            modifier = Modifier.width(210.dp),
            value = addOrUpdateCustomerUIState.name,
            onValueChange = {
                manageCustomerViewModel.updateName(it)
            },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Enter customer name") },
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            modifier = Modifier.width(210.dp),
            value = addOrUpdateCustomerUIState.address,
            onValueChange = {
                manageCustomerViewModel.updateAddress(it)
            },
            label = { Text(text = "Address") },
            placeholder = { Text(text = "Enter customer name") },
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            modifier = Modifier.width(210.dp),
            value = addOrUpdateCustomerUIState.phone,
            onValueChange = {
                manageCustomerViewModel.updatePhone(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            label = { Text(text = "Phone") },
            placeholder = { Text(text = "Enter customer name") },
        )
        Spacer(modifier = Modifier.width(5.dp))
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                modifier = Modifier.width(100.dp),
                onClick = { manageCustomerViewModel.addOrUpdateCustomer(close) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.DarkGray
                ),
                border = BorderStroke(1.dp, color = Color.DarkGray)
            ) {
                if(addOrUpdateCustomerUIState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(if (addOrUpdateCustomerUIState.id == null) "Add" else "Update")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = { close() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                border = BorderStroke(1.dp, color = Color.DarkGray)
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun manageCustomerScreen() {
    val manageCustomerViewModel = hiltViewModel<ManageCustomerViewModel>()
    val customerUIState = manageCustomerViewModel.uiCustomerState.collectAsState()
    var search by remember { mutableStateOf("") }
    var expended by remember { mutableStateOf(false) }
    var popup by remember { mutableStateOf(false) }
    var deleteCustomerId: String? = null;

    Scaffold(
        topBar = {
            CustomerSearchBar(search) {
                search = it
                manageCustomerViewModel.updateCustomerStateBy(search)
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                FloatingActionButton(
                    onClick = {
                        expended = true
                        manageCustomerViewModel.resetForm()
                    },
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .align(alignment = Alignment.BottomEnd),
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            }
        }
    ) {
        if(popup) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { popup = false}
            ){
                Card(
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.8f)
                            .background(color = Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Do you want to delete?")
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                manageCustomerViewModel.deleteCustomer(deleteCustomerId) {
                                    popup = false
                                }
                            }
                            ) {

                                Text("Yes")
                            }
                            Button(onClick = { popup = false }) {
                                Text("No")
                            }
                        }
                    }
                }
            }
        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.7f)
                .background(color = Color(0f, 0f, 0f, 0f)),
            expanded = expended,
            onDismissRequest = { expended = false },
            offset = DpOffset(x = 40.dp, y = 10.dp),
        ) {
            AddCustomer(manageCustomerViewModel) {
                expended = false
            }
        }

        Column(modifier = Modifier
            .padding(it)
            .padding(top = 10.dp)
            .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Customers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                items(customerUIState.value.customers.size) {
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(customerUIState.value.customers[it].name)
                                Text(customerUIState.value.customers[it].address)
                                Text(customerUIState.value.customers[it].phone.toString())
                            }
                            Row {
                                Icon(
                                    ImageVector.vectorResource(id = R.drawable.af_delete_outline_24),
                                    contentDescription = "delete",
                                    modifier = Modifier.clickable {
                                        popup = true
                                        deleteCustomerId = customerUIState.value.customers[it].id
                                    }
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Icon(
                                    ImageVector.vectorResource(id = R.drawable.af_edit_24),
                                    contentDescription = "edit",
                                    modifier = Modifier.clickable {
                                        expended = true
                                        manageCustomerViewModel.fillUpdateCustomer(
                                            id = customerUIState.value.customers[it].id,
                                            name = customerUIState.value.customers[it].name,
                                            address = customerUIState.value.customers[it].address,
                                            phone = customerUIState.value.customers[it].phone.toString(),
                                        )
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSearchBar(
    text: String = "",
    onChange : (String)-> Unit = {}
) {
    TopAppBar(title = {
        TextField(
            value = text,
            onValueChange = onChange,
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp, top = 15.dp),
            singleLine = true
        )}
    )
}