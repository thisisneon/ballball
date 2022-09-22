package com.example.ballball.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ballball.model.UsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {
    val loadChatList = MutableLiveData<LoadChatList>()
    val chatFilter = MutableLiveData<ChatFilter>()

    sealed class LoadChatList {
        object Loading : LoadChatList()
        class ResultOk(val list : ArrayList<UsersModel>) : LoadChatList()
        object ResultError : LoadChatList()
    }
    sealed class ChatFilter {
        class ResultOk(val list: ArrayList<UsersModel>) : ChatFilter()
        object ResultError : ChatFilter()
    }

    fun loadChatList(userUID : String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            chatRepository.loadChatChannel(userUID, {
                loadChatList.value = LoadChatList.ResultOk(it)
            }, {
                loadChatList.value = LoadChatList.ResultError
            })
        }
    }

    fun loadFilterList(
        userUID: String,
        text : String
    ) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            chatRepository.filter(userUID, text, {
                chatFilter.value = ChatFilter.ResultOk(it)
            }, {
                chatFilter.value = ChatFilter.ResultError
            })
        }
    }
}