package com.amit.aquafill.presentation.ui.authorized.entry

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.model.AddEntryDto
import com.amit.aquafill.network.response.EntryResponse
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.customer.ICustomerRepository
import com.amit.aquafill.repository.entry.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject


data class Option<T>(val key: T, val text: String, var isSelected: Boolean = false)

data class EntryUIState(
    val customers: List<Option<String>>,
    var selectedCustomerId: String,
    val givenBottle: String,
    val takenBottle: String,
    val remainingBottle: String,
    val date: Date,
    val bottleType: String,
    val perBottleCost: String,
    val status: PaymentStatus,
    val isAddEntry: Boolean,
    val selectedIndex: Int,
    val startDate: Date,
    val endDate: Date,
    val paymentStatus: List<String>,
)

data class EntriesUIState (
    val entries: List<EntryResponse>,
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: ICustomerRepository,
    private val entryRepository: EntryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(EntryUIState(emptyList(),
        "",
        "",
        "",
        "",
        Date(),
        BottleType.Normal,
        "",
        PaymentStatus.Paid,
        isAddEntry = false,
        -1,
        Date(),
        Date(),
        emptyList()
    ))
    val uiState = _uiState.asStateFlow()

    private val _entriesUiState = MutableStateFlow(EntriesUIState(emptyList()))
    val entriesUiState = _entriesUiState.asStateFlow()

    init {
        getCustomers()
    }

    private fun getCustomers() {
        viewModelScope.launch {
            when(val response = customerRepository.customers()){
                is NetworkResult.Success -> {
                    val customers = response.data!!.sortedBy {
                        it.name
                    }
                    _uiState.value = _uiState.value.copy(customers = customers.map { customer ->
                        Option(key = customer.id, text = customer.name)
                    })
                    Log.d("entry customer info", response.toString())
                }
                is NetworkResult.Error -> {
                    Log.d("customer info", response.message.toString())
                }
                is NetworkResult.Loading -> {
                    Log.d("customer info", response.toString())
                }
            }
        }
    }

    fun updateIndex(index: Int) {
        _uiState.value = _uiState.value.copy(
            selectedIndex = index
        )
    }
    fun addEntry() {
        viewModelScope.launch {
            val entry = AddEntryDto(
                given = uiState.value.givenBottle.toInt(),
                taken = uiState.value.takenBottle.toInt(),
                remaining = uiState.value.remainingBottle.toInt(),
                date = uiState.value.date,
                bottleType = uiState.value.bottleType,
                pricePerBottle = uiState.value.perBottleCost.toDouble(),
                status = uiState.value.status,
                customerId = uiState.value.selectedCustomerId
            )
            when(val response = entryRepository.add(entry)) {
                is NetworkResult.Success -> {
                    Log.d("entry success", response.data.toString())
                    updateIsAddEntry(false)
                }
                is NetworkResult.Error -> {
                    Log.e("entry", response.message.toString())
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted) {
            visiblePermissionDialogQueue.add(0, permission)
        }
    }

    fun generateBill(context: Context) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val fol = File(dir)
        val directory = "AquaBill"
        val folder = File(fol, directory)
        if(!folder.exists()) {
            folder.mkdir()
        }
        try {
            val file = File(folder, "${LocalDateTime.now().toString().replace(":","")}.pdf")
            file.createNewFile()
            val fOut = FileOutputStream(file)
            val document = PdfDocument()
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(210, 297, 1).create()
            val page: PdfDocument.Page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            val paint = Paint()
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 10.0f
            paint.isUnderlineText = true
            paint.isFakeBoldText = true
            canvas.drawText("BABLU WATER", pageInfo.pageWidth/2.0f, 20.0f, paint)

//            val startDateZone: ZonedDateTime = _uiState.value.startDate.toInstant().atZone(ZoneId.of("Asia/Kolkata"))
//            val endDateZone: ZonedDateTime = _uiState.value.endDate.toInstant().atZone(ZoneId.of("Asia/Kolkata"))
//            canvas.drawText("BILL (${YearMonth.from(startDateZone)} - ${YearMonth.from(endDateZone)})", pageInfo.pageWidth/2.0f, 10.0f, paint)

            paint.textSize = 5.0f
            paint.isFakeBoldText = false
            paint.isUnderlineText = false
            canvas.drawText("18 ABHAY GUHA ROAD, LILUAH HOWRAH - 711204", pageInfo.pageWidth/2.0f, 30.0f, paint)
            canvas.drawText("Contact - 6291154800, 8276938891", pageInfo.pageWidth/2.0f, 40.0f, paint)

            paint.textAlign = Paint.Align.LEFT
            val width = pageInfo.pageWidth
            val netWidth = width - 20.0f
            val partSize = netWidth / 4.0f

            paint.style = Paint.Style.STROKE
            canvas.drawRect(10.0f, 50.0f, width - 10.0f, 60.0f, paint)

            val headers = arrayListOf("DATE", "QUANTITY", "QTY X AMOUNT", "AMOUNT(₹)")
            paint.style = Paint.Style.FILL
            canvas.drawLine(10.0f, 50.0f, 10.0f, 60.0f, paint)
            for (i in 0..3)
            {
                canvas.drawLine(10.0f + (i + 1) * partSize, 50.0f, 10.0f + (i + 1) * partSize, 60.0f, paint)
                canvas.drawText(headers[i], 11.0f + i * partSize, 57.0f, paint)
            }

            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            var y = 60.0f
            var totalQuantity = 0; var totalAmount = 0.0
            _entriesUiState.value.entries.forEach{ entry ->
                canvas.drawLine(10.0f, y + 10, width - 10.0f, y + 10, paint)
                canvas.drawLine(10.0f, y, 10.0f, y + 10, paint)
                val date = Instant.ofEpochMilli(entry.date.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(dateFormatter)
                totalQuantity += entry.given
                totalAmount += (entry.given * entry.pricePerBottle)
                val data = arrayListOf(date.toString(), entry.given.toString(), "${entry.given} X ${entry.pricePerBottle}", "₹ ${(entry.given * entry.pricePerBottle)}")
                for(i in 0..3)
                {
                    canvas.drawLine(10.0f + (i + 1) * partSize, y, 10.0f + (i + 1) * partSize, y + 10, paint)
                    canvas.drawText(data[i], 11.0f + i * partSize, y + 7.0f, paint)
                }
                y += 10
            }

            paint.isFakeBoldText = true

            canvas.drawText("TOTAL", 11.0f, y + 7.0f, paint)
            canvas.drawText(totalQuantity.toString(), 11.0f + partSize, y + 7.0f, paint)
            canvas.drawText("₹ $totalAmount", 11.0f + 3 * partSize, y + 7.0f, paint)
            canvas.drawText("SIGNATURE", 11.0f, pageInfo.pageHeight - 10.0f, paint)

            document.finishPage(page)
            document.writeTo(fOut)
            document.close()
            Toast.makeText(context, "${file.name} saved to Download/${directory}", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.localizedMessage?.let { Log.i("abc", it) }
        }
    }

    fun getEntry() {
        viewModelScope.launch {
            when(val response = entryRepository.entries(
                uiState.value.customers[uiState.value.selectedIndex].key,
                uiState.value.startDate,
                uiState.value.endDate,
                uiState.value.paymentStatus
            )
            ) {
                is NetworkResult.Success -> {
                    Log.d("get entries", response.data!!.toString())
                    _entriesUiState.value = _entriesUiState.value.copy(
                        entries = response.data
                    )
                }
                is NetworkResult.Error -> {
                    Log.d("get entries", response.message.toString())
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun updateIsAddEntry(isEntry: Boolean) {
        _uiState.value = _uiState.value.copy(
            isAddEntry = isEntry
        )
    }

    fun updateGivenBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(givenBottle = bottle)
    }
    fun updateTakenBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(takenBottle = bottle)
    }
    fun updateRemainingBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(remainingBottle = bottle)
    }

    fun updateDate(date: Date) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun updateDateRange(startDate: Date, endDate: Date) {
        _uiState.value = _uiState.value.copy(
            startDate = startDate,
            endDate = endDate
        )
    }

    fun updateBottleType(bottleType: String) {
        _uiState.value = _uiState.value.copy(bottleType = bottleType)
    }

    fun updatePaymentStatus(paymentStatus: PaymentStatus) {
        _uiState.value = _uiState.value.copy(status = paymentStatus)
    }

    fun updatePerBottleCost(cost: String) {
        _uiState.value = _uiState.value.copy(perBottleCost = cost)
    }
}