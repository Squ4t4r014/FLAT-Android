package com.websarva.wings.android.flat.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.websarva.wings.android.flat.api.PostData.PostAddFriend
import com.websarva.wings.android.flat.api.ResponseData.ResponseAddFriend
import com.websarva.wings.android.flat.api.ResponseData.ResponseGetUser
import com.websarva.wings.android.flat.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class AddFriendViewModel(
    saveStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = ApiRepository.instance

    //TODO::repositoryでroomか何かと繋いで自分のIDを取ってくるようにする？
    private val myId = "000001"

    private val _user = MutableLiveData<ResponseGetUser>()
    val user: LiveData<ResponseGetUser> get() = _user

    private val _getCode = MutableLiveData<Int>()
    val getCode: LiveData<Int> get() = _getCode

    private val _postCode = MutableLiveData<Int>()
    val postCode: LiveData<Int> get() = _postCode

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getUserInfo(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUser(id)
                _getCode.postValue((response.code()))
                if (response.isSuccessful) {
                    Log.d("getSuccess", "${response}\n${response.body()}")
                    _user.postValue(response.body())
                } else {
                    Log.d("getFailure", "$response")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                e.printStackTrace()
            }
        }
    }

    fun postFriendRequest(targetId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postId = PostAddFriend(myId, targetId)
                val response = repository.postAddFriend(postId)
                _postCode.postValue(response.code())
                if (response.isSuccessful) {
                    Log.d("postSuccess", "$response")
                } else {
                    Log.d("postFailure", "$response")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                e.printStackTrace()
            }
        }
    }
}