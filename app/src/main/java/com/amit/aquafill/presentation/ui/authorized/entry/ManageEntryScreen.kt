package com.amit.aquafill.presentation.ui.authorized.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amit.aquafill.R
import com.amit.aquafill.network.model.EntryDto
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date


class BottleType {
    companion object {
        const val Normal = "Normal"
        const val MotherDairy = "Mother Dairy"
    }
    enum class Type {
        Normal,
        MotherDairy
    }
}

enum class PaymentStatus {
    Paid,
    Unpaid
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RenderPreview() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntry(viewModel: CustomerViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val options = viewModel.uiState.collectAsState()

    val currentDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val dateState = rememberDatePickerState(currentDate, currentDate)
    var openedStart by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var bottleTypeExpanded by remember { mutableStateOf(false) }
    var paymentStatusExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            viewModel.updateIsAddEntry(false)
        },
        title = { Text(text = "Add Entry") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if(selectedIndex != -1) options.value.customers[selectedIndex].text else "Please select a customer")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .padding(5.dp)
                            .width(260.dp)
                    ) {
                        options.value.customers.forEachIndexed { i, option ->
                            DropdownMenuItem(
                                text = { Text(option.text) },
                                onClick = {
                                    selectedIndex = i
                                    options.value.selectedCustomerId = options.value.customers[i].key
                                    expanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = options.value.givenBottle,
                    onValueChange = {
                        viewModel.updateGivenBottle(it)
                    },
                    label = { Text(text = "Given Bottle") },
                    placeholder = { Text(text = "No.of bottle given") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = options.value.takenBottle,
                    onValueChange = {
                        viewModel.updateTakenBottle(it)
                    },
                    label = { Text(text = "Taken Bottle") },
                    placeholder = { Text(text = "No.of bottle taken") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = options.value.remainingBottle,
                    onValueChange = {
                        viewModel.updateRemainingBottle(it)
                    },
                    label = { Text(text = "Remaining Bottle") },
                    placeholder = { Text(text = "No.of bottle remaining") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row {
                    OutlinedTextField(
                        value = buildString {
                            dateState.selectedDateMillis?.let { startDateMillis ->
                                append(
                                    Instant.ofEpochMilli(startDateMillis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .format(dateFormatter)
                                )
                            }
                        },
                        label = { Text("Date") },
                        onValueChange = { },
                        readOnly = true,
                        placeholder = { Text(text = "Order date") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.af_calendar_24),
                                contentDescription = "start date",
                                modifier = Modifier.clickable {
                                    openedStart = true
                                }
                            )
                        }
                    )
                }
                if (openedStart) {
                    DatePickerDialog(
                        onDismissRequest = { openedStart = false },
                        confirmButton = {
                            viewModel.updateDate(Date(dateState.selectedDateMillis!!))
                        },
                        dismissButton = {}
                    ) {
                        DatePicker(
                            state = dateState,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Box {
                    Row {
                        OutlinedButton(
                            onClick = { bottleTypeExpanded = true },
                        ) {
                            Text(options.value.bottleType)
                        }
                        DropdownMenu(
                            expanded = bottleTypeExpanded,
                            onDismissRequest = { bottleTypeExpanded = false },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text(BottleType.Normal) },
                                onClick = {
                                    viewModel.updateBottleType(BottleType.Type.Normal.name)
                                    bottleTypeExpanded = false
                                })
                            DropdownMenuItem(
                                text = { Text(BottleType.MotherDairy)},
                                onClick = {
                                    viewModel.updateBottleType(BottleType.Type.MotherDairy.name)
                                    bottleTypeExpanded = false
                                })
                        }

                        OutlinedButton(
                            onClick = { paymentStatusExpanded = true },
                        ) {
                            Text(options.value.status.name)
                        }
                        DropdownMenu(
                            expanded = paymentStatusExpanded,
                            onDismissRequest = { paymentStatusExpanded = false },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text(PaymentStatus.Paid.name) },
                                onClick = {
                                    viewModel.updatePaymentStatus(PaymentStatus.Paid)
                                    paymentStatusExpanded = false
                                })
                            DropdownMenuItem(
                                text = { Text(PaymentStatus.Unpaid.name)},
                                onClick = {
                                    viewModel.updatePaymentStatus(PaymentStatus.Unpaid)
                                    paymentStatusExpanded = false
                                })
                        }
                    }
                }

                OutlinedTextField(
                    value = options.value.perBottleCost,
                    onValueChange = {
                        viewModel.updatePerBottleCost(it)
                    },
                    label = { Text(text = "Cost per bottle") },
                    placeholder = { Text(text = "Cost per bottle") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Text(
                text = "Add",
                modifier = Modifier.clickable {
                    viewModel.addEntry()
                }
            )
        },
        dismissButton = {
            Text(
                text = "Cancel",
                modifier = Modifier.clickable {
                    viewModel.updateIsAddEntry(false)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEntitiesScreen() {
    val viewModel = hiltViewModel<CustomerViewModel>()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.updateIsAddEntry(true) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableIntStateOf(-1) }
        val options = viewModel.uiState.collectAsState()
        if(options.value.isAddEntry) {
            AddEntry(viewModel)
        }
        Column(
            modifier = Modifier.padding(it)
        ) {
            Column {
                Box {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { expanded = true },
                        ) {
                        Text(if(selectedIndex != -1) options.value.customers[selectedIndex].text else "Please select a customer")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        options.value.customers.forEachIndexed { i, option ->
                            DropdownMenuItem(
                                text = { Text(option.text) },
                                onClick = {
                                    selectedIndex = i
                                    expanded = false
                                },
                            )
                        }
                    }
                }

                val currentDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                val dateState = rememberDateRangePickerState(currentDate, currentDate)
                var openedStart by remember { mutableStateOf(false) }
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                Row {
                    OutlinedTextField(
                        value = buildString {
                            dateState.selectedStartDateMillis?.let { startDateMillis ->
                                append(
                                    Instant.ofEpochMilli(startDateMillis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .format(dateFormatter)
                                )
                            }
                            append(" - ")
                            dateState.selectedEndDateMillis?.let { endDateMillis ->
                                append(
                                    Instant.ofEpochMilli(endDateMillis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .format(dateFormatter)
                                )
                            }
                        },
                        label = { Text("Start date") },
                        onValueChange = { },
                        readOnly = true,
                        placeholder = { Text(text = "Start date") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.af_calendar_24),
                                contentDescription = "start date",
                                modifier = Modifier.clickable {
                                    openedStart = true
                                }
                            )
                        }
                    )
                }

                if (openedStart) {
                    DatePickerDialog(
                        onDismissRequest = { openedStart = false },
                        confirmButton = {},
                        dismissButton = {}
                    ) {
                        DateRangePicker(
                            state = dateState,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Entry()
                Entry()
            }
        }
    }
}

@Composable
fun Entry(
    entryDto: EntryDto = EntryDto("entryId", "Amit Shaw", 4, 3, 5, Date.from(LocalDateTime.ofInstant(Date().toInstant(), ZoneId.systemDefault()).atZone(
        ZoneId.systemDefault()).toInstant()), BottleType.Normal, 40.0, PaymentStatus.Paid, "customerId")
) {
    Card (
        modifier = Modifier
            .fillMaxWidth(9f)
            .padding(5.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(entryDto.name.uppercase())
                    Text(text = "Given: ${entryDto.given}")
                    Text(text = "Taken: ${entryDto.taken}")
                    Text(text = "Remaining: ${entryDto.remaining}")
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card {
                            Text(
                                entryDto.status.name,
                                modifier = Modifier
                                    .background(
                                        color = if (entryDto.status == PaymentStatus.Paid) Color(
                                            0xFF99FF99
                                        ) else Color(0xFFFF0066),
                                    )
                                    .padding(horizontal = 4.dp),
                                style = TextStyle(color = if(entryDto.status == PaymentStatus.Paid) Color.Black else Color.White)
                            )
                        }
                        Row{
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.af_edit_24),
                                contentDescription = "edit"
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.af_delete_outline_24),
                                contentDescription = "delete"
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Date: ${
                                entryDto.date.toInstant()
                                .atOffset(ZoneOffset.UTC).toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            }"
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Type: ${entryDto.bottleType}"
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Price: ${entryDto.given} x ${entryDto.pricePerBottle} = ${entryDto.given * entryDto.pricePerBottle}"
                        )
                    }
                }
            }
        }
    }
}