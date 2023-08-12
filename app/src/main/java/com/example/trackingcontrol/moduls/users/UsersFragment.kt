package com.example.trackingcontrol.moduls.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingcontrol.R
import com.example.trackingcontrol.databinding.UsersFragmentBinding
import com.example.trackingcontrol.moduls.users.adapters.UsersAdapter
import com.example.trackingcontrol.moduls.users.viewmodels.UsersViewModel
import com.example.trackingcontrol.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView

class UsersFragment : Fragment() {

    private var _binding: UsersFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var usersAdapter: UsersAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UsersFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setAdapter()

        return root
    }

    private fun observeUsersData() {
        usersViewModel.usersData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    Log.d("suka_blya",it.result.toString())
                    usersAdapter.setUsers(it.result)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUsersData()
    }


    private fun setAdapter()
    {
        usersAdapter = UsersAdapter(requireContext(),findNavController())
        viewManager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            adapter = usersAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}