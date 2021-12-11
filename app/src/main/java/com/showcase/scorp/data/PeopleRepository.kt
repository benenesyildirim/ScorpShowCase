package com.showcase.scorp.data

class PeopleRepository {
    fun fetchPeople(next: String?, fetchCompletionHandler: FetchCompletionHandler) {
        DataSource().fetch(next, fetchCompletionHandler)
    }
}