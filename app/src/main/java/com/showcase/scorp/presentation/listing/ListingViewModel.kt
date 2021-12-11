package com.showcase.scorp.presentation.listing

import androidx.lifecycle.*
import com.showcase.scorp.common.Resource
import com.showcase.scorp.data.*
import kotlinx.coroutines.launch

class ListingViewModel : ViewModel() {

    private var next: String? = null
    private var personList: List<Person> = emptyList()
    private var errorMessage: String? = null

    private val _peopleLiveData = MutableLiveData<Resource<PersonModel>>()
    val peopleLiveData: LiveData<Resource<PersonModel>> get() = _peopleLiveData

    init {
        fetchPeople()
    }

    fun fetchPeople() = viewModelScope.launch {
        _peopleLiveData.postValue(Resource.Loading())

        val fetchCompletionHandler: FetchCompletionHandler = { fetchResponse, fetchError ->
            try {
                next = fetchResponse?.next!!
                personList = fetchResponse.people
                errorMessage = fetchError?.errorDescription

                if (errorMessage.isNullOrEmpty()) {
                    _peopleLiveData.postValue(Resource.Success(PersonModel(personList, next!!, "")))
                } else {
                    _peopleLiveData.postValue(Resource.Error(errorMessage ?: "A Problem occurred..."))
                }
            } catch (e: NullPointerException) {
                _peopleLiveData.postValue(Resource.Error("There is no one, trying again..."))
            }
        }

        PeopleRepository().fetchPeople(next, fetchCompletionHandler)
    }
}