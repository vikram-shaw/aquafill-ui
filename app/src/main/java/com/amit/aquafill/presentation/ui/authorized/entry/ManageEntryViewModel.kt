package com.amit.aquafill.presentation.ui.authorized.entry

import android.R.attr.data
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
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

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted) {
            visiblePermissionDialogQueue.add(0, permission);
        }
    }

    fun generateBill(context: Context) {
//        Log.d("Bill", "generateBill: started")
//            Log.d("Bill", "generateBill: inside")
//            val pdf = PdfDocument()
//            val paint = Paint()
//
//            val pageInfo = PdfDocument.PageInfo.Builder(400, 600, 1).create()
//            val page = pdf.startPage(pageInfo)
//            val canvas = page.canvas
//            canvas.drawText("Welcome ", 40.0f, 50.0f, paint)
//            pdf.finishPage(page)
//
//            val file = File(Environment.getExternalStorageDirectory(),"/bill.pdf")
//
//            try{
//                pdf.writeTo(FileOutputStream(file))
//            } catch (e: IOException) {
//                Log.d("Bill", e.message.toString())
//            }
//            pdf.close()
        Log.v("abc", "start")
        val extstoragedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        Log.v("abc", extstoragedir)
        val fol: File = File(extstoragedir)
        Log.v("abc", fol.toString())
        val folder = File(fol, "AquaBill")
        if(!folder.exists()) {
            Log.d("abc", "is side create new folder")
            folder.mkdir()
        }
        Log.v("abc", folder.toString())
        try {
            Log.v("abc", "inside try")
            val file = File(folder, "${LocalDateTime.now().toString().replace(":",".")}.pdf")
            Log.v("abc", file.toString())
            file.createNewFile()
            Log.v("abc", file.toString())
            val fOut = FileOutputStream(file)
            Log.v("abc", fOut.toString())
            val document = PdfDocument()
            Log.v("abc", document.toString())
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(210, 297, 1).create()
            Log.v("abc", pageInfo.toString())
            val page: PdfDocument.Page = document.startPage(pageInfo)
            Log.v("abc", page.toString())
            val canvas: Canvas = page.canvas
            Log.v("abc", canvas.toString())
            val paint = Paint()
            Log.v("abc", paint.toString())
            canvas.drawText("My name is vikram", 10.0f, 10.0f, paint)

            Log.v("abc", "drawn")
            document.finishPage(page)
            Log.v("abc", "document finish page done")
            document.writeTo(fOut)
            Log.v("abc", "write to fOut")
            document.close()
            Log.v("abc", "document closed")
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