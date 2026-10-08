package com.pemmob.pawangdigimon.ui.viewmodel

import com.pemmob.pawangdigimon.data.model.DigimonDetail
import com.pemmob.pawangdigimon.data.model.DigimonSummary

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val digimons: List<DigimonSummary>,
        val canLoadMore: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val digimon: DigimonDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
