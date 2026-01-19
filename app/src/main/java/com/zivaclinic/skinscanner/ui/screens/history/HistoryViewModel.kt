package com.zivaclinic.skinscanner.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivaclinic.skinscanner.data.model.ScanResult
import com.zivaclinic.skinscanner.data.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryState(
    val scans: List<ScanResult> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HistoryViewModel(
    private val repository: ScanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repository.getAllScans().collect { scans ->
                    _state.value = _state.value.copy(
                        scans = scans,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load history"
                )
            }
        }
    }

    fun deleteScan(scanResult: ScanResult) {
        viewModelScope.launch {
            try {
                repository.deleteScan(scanResult)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to delete scan"
                )
            }
        }
    }
}
