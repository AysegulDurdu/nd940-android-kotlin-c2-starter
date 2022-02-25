package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = AsteroidListAdapter(AsteroidClickListener {
            view?.findNavController()?.navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it) {
                binding.asteroidRecycler.scrollToPosition(0)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.week_menu -> {
                viewModel.updateFilter(FILTER_TYPE.WEEK)
                return true
            }
            R.id.today_menu -> {
                viewModel.updateFilter(FILTER_TYPE.TODAY)
                return true
            }
            R.id.saved_menu -> {
                viewModel.updateFilter(FILTER_TYPE.SAVE)
                return true
            }
        }
        return true
    }
}
