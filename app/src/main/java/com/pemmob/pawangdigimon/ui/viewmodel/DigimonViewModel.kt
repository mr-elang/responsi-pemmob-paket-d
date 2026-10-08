package com.pemmob.pawangdigimon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.pawangdigimon.data.model.DigimonSummary
import com.pemmob.pawangdigimon.data.repository.DigimonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DigimonViewModel(
    private val repository: DigimonRepository = DigimonRepository()
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    private val currentList = mutableListOf<DigimonSummary>()
    private var currentPage = 0
    private var totalPages = 1
    private var isFetching = false
    private var searchJob: Job? = null
    var currentSearchQuery = ""
        private set

    init {
        loadDigimons()
    }

    fun loadDigimons(pageSize: Int = 30) {
        currentSearchQuery = ""
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading
            currentList.clear()
            currentPage = 0
            isFetching = true

            repository.getDigimons(page = currentPage, pageSize = pageSize)
                .onSuccess { response ->
                    isFetching = false
                    currentList.addAll(response.content)
                    totalPages = response.pageable?.totalPages ?: 1

                    if (currentList.isEmpty()) {
                        _homeUiState.value = HomeUiState.Error("Tidak ada data Digimon yang ditemukan.")
                    } else {
                        _homeUiState.value = HomeUiState.Success(
                            digimons = currentList.toList(),
                            canLoadMore = currentPage < totalPages - 1,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { exception ->
                    isFetching = false
                    val errorMsg = exception.localizedMessage ?: "Terjadi kesalahan saat memuat data."
                    _homeUiState.value = HomeUiState.Error(errorMsg)
                }
        }
    }

    fun searchDigimons(query: String) {
        searchJob?.cancel()
        currentSearchQuery = query.trim()

        if (currentSearchQuery.isEmpty()) {
            _homeUiState.value = HomeUiState.Success(
                digimons = currentList.toList(),
                canLoadMore = currentPage < totalPages - 1,
                isLoadingMore = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce 400ms while user is typing
            _homeUiState.value = HomeUiState.Loading

            val numericId = currentSearchQuery.toIntOrNull()

            repository.getDigimons(name = currentSearchQuery, page = 0, pageSize = 100)
                .onSuccess { response ->
                    val searchResults = response.content.toMutableList()

                    if (searchResults.isEmpty() && numericId != null) {
                        repository.getDigimonDetail(numericId)
                            .onSuccess { detail ->
                                searchResults.add(
                                    DigimonSummary(
                                        id = detail.id,
                                        name = detail.name,
                                        image = detail.images.firstOrNull()?.href
                                    )
                                )
                            }
                    }

                    if (searchResults.isEmpty()) {
                        _homeUiState.value = HomeUiState.Error("Digimon '$query' tidak ditemukan.")
                    } else {
                        _homeUiState.value = HomeUiState.Success(
                            digimons = searchResults,
                            canLoadMore = false,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { exception ->
                    val errorMsg = exception.localizedMessage ?: "Gagal melakukan pencarian."
                    _homeUiState.value = HomeUiState.Error(errorMsg)
                }
        }
    }

    fun loadMoreDigimons(pageSize: Int = 30) {
        if (isFetching || currentSearchQuery.isNotEmpty() || currentPage >= totalPages - 1) return

        val currentState = _homeUiState.value
        if (currentState is HomeUiState.Success) {
            _homeUiState.value = currentState.copy(isLoadingMore = true)
        }

        isFetching = true
        val nextPage = currentPage + 1

        viewModelScope.launch {
            repository.getDigimons(page = nextPage, pageSize = pageSize)
                .onSuccess { response ->
                    isFetching = false
                    currentPage = nextPage
                    totalPages = response.pageable?.totalPages ?: totalPages
                    currentList.addAll(response.content)

                    _homeUiState.value = HomeUiState.Success(
                        digimons = currentList.toList(),
                        canLoadMore = currentPage < totalPages - 1,
                        isLoadingMore = false
                    )
                }
                .onFailure {
                    isFetching = false
                    if (currentState is HomeUiState.Success) {
                        _homeUiState.value = currentState.copy(isLoadingMore = false)
                    }
                }
        }
    }

    fun loadDigimonDetail(id: Int) {
        viewModelScope.launch {
            _detailUiState.value = DetailUiState.Loading
            repository.getDigimonDetail(id)
                .onSuccess { detail ->
                    _detailUiState.value = DetailUiState.Success(detail)
                }
                .onFailure { exception ->
                    val errorMsg = exception.localizedMessage ?: "Gagal memuat detail Digimon."
                    _detailUiState.value = DetailUiState.Error(errorMsg)
                }
        }
    }
}
