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
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
}

enum class PaymentStatus {
    Paid,
    Unpaid
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RenderPreview() {

}

data class Option<T>(val key: T, val text: String, var isSelected: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEntitiesScreen() {
    Column {
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(-1) }
        var options by remember {
            mutableStateOf(
                listOf(
                    Option("1", "vikram"),
                    Option("2", "pritam"),
                    Option("3","amit"),
                    Option("4", "ankit")
                )
            )
        }
        Column {
            Box {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expanded = true },
                ) {
                    Text(if(selectedIndex != -1) options[selectedIndex].text else "")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                        .padding(5.dp)
                ) {
                    options.forEachIndexed { i, option ->
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

@Composable
fun Entry(
    entryDto: EntryDto = EntryDto("entryId", "Amit Shaw", 4, 3, 5, Date.from(LocalDateTime.ofInstant(Date().toInstant(), ZoneId.systemDefault()).atZone(
        ZoneId.systemDefault()).toInstant()), BottleType.Normal, 40.0, PaymentStatus.Paid, "userId", "customerId"
    )
) {
    Card (
        modifier = Modifier.fillMaxWidth(9f).padding(5.dp)
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
                                modifier = Modifier.background(
                                    color = if(entryDto.status == PaymentStatus.Paid) Color(0xFF99FF99) else Color(0xFFFF0066),
                                ).padding(horizontal = 4.dp),
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