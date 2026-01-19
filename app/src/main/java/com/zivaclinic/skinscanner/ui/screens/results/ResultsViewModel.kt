package com.zivaclinic.skinscanner.ui.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivaclinic.skinscanner.data.model.ScanResult
import com.zivaclinic.skinscanner.data.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultsState(
    val scanResult: ScanResult? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ResultsViewModel(
    private val repository: ScanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultsState())
    val state: StateFlow<ResultsState> = _state.asStateFlow()

    fun loadScanResult(scanId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val result = repository.getScanById(scanId)
                
                if (result != null) {
                    _state.value = _state.value.copy(
                        scanResult = result,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Scan result not found"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load scan result"
                )
            }
        }
    }
}
