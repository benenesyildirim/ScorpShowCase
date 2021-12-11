package com.showcase.scorp.presentation.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.showcase.scorp.common.Resource
import com.showcase.scorp.data.Person
import com.showcase.scorp.databinding.FragmentListingBinding

class ListingFragment : Fragment() {
    private lateinit var listingAdapter: ListingAdapter
    private val listingViewModel: ListingViewModel by viewModels()
    private lateinit var fragmentListingBinding: FragmentListingBinding
    private var personList = listOf<Person>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListingBinding = FragmentListingBinding.inflate(inflater)
        fetchPeople()

        setPullToRefreshListener()
        setPeopleList()
        return fragmentListingBinding.root
    }

    private fun setPeopleList() {
        listingAdapter = ListingAdapter()

        fragmentListingBinding.peopleListRv.apply {
            adapter = listingAdapter
            smoothScrollToPosition(personList.size)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        fragmentListingBinding.peopleListSwiper.isRefreshing = true
                        listingViewModel.fetchPeople()
                    }
                }
            })
        }
    }

    private fun setPullToRefreshListener() {
        fragmentListingBinding.peopleListSwiper.setOnRefreshListener {
            fetchPeople()
        }
    }

    private fun fetchPeople() {
        with(listingViewModel) {
            peopleLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    is Resource.Loading -> {
                        fragmentListingBinding.peopleListSwiper.isRefreshing = true
                    }
                    is Resource.Success -> {
                        state.data?.people?.let{
                            personList = state.data.people
                            listingAdapter.addToPeopleList(it)
                        }
                        fragmentListingBinding.peopleListSwiper.isRefreshing = false
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, "${state.message}", Toast.LENGTH_SHORT).show()
                        fragmentListingBinding.peopleListSwiper.isRefreshing = false
                        listingViewModel.fetchPeople()
                    }
                }
            })
        }
    }
}