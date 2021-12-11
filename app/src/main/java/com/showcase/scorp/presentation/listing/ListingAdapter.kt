package com.showcase.scorp.presentation.listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.showcase.scorp.data.Person
import com.showcase.scorp.databinding.RowDesignBinding

class ListingAdapter :
    RecyclerView.Adapter<ListingAdapter.ViewHolder>() {
    private var personList: MutableList<Person> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowDesignBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(personList[position])

    override fun getItemCount(): Int = personList.size

    inner class ViewHolder(private val binding: RowDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.apply {
                this.person = person
            }
        }
    }

    fun addToPeopleList(people: List<Person>) {
        if (this.personList.size > 0) this.personList.addAll(getFilteredPeopleList(people))
        else this.personList.addAll(people)
        notifyItemInserted(this.personList.size)
    }

    private fun createIdList(): List<Int> {
        val idList = mutableListOf<Int>()
        for (person in this.personList) idList.add(person.id)
        return idList
    }

    private fun isUserAlreadyAdded(userId: Int): Boolean {
        val idList = createIdList()
        return idList.contains(userId)
    }

    private fun getFilteredPeopleList(people: List<Person>): List<Person> {
        val filteredList = mutableListOf<Person>()

        people.forEach { person ->
            if (!isUserAlreadyAdded(person.id)) filteredList.add(person)
        }
        return filteredList
    }
}