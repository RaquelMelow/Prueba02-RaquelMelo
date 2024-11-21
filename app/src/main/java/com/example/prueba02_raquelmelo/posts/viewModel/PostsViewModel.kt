package com.example.prueba02_raquelmelo.posts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba02_raquelmelo.domain.model.Post
import com.example.prueba02_raquelmelo.domain.util.ApiResult
import com.example.prueba02_raquelmelo.repository.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    data object Loading : UiState()
    data class Success(val posts: List<Post>) : UiState()
}

class PostsViewModel : ViewModel() {
    private val repository = PostsRepository()

    private val _uiState: MutableStateFlow<UiState?> = MutableStateFlow(null)
    val uiState: StateFlow<UiState?> = _uiState

    private val _dialogState: MutableStateFlow<String?> = MutableStateFlow(null)
    val dialogState: StateFlow<String?> = _dialogState

    fun getPosts(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getPosts().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _uiState.value = UiState.Success(result.data)
                        _dialogState.value = null
                    }
                    is ApiResult.Error -> {
                        _dialogState.value = result.error.toString()
                    }
                }
            }
        }
    }
}