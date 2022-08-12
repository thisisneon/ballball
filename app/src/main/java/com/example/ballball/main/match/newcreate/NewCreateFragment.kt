package com.example.ballball.main.match.newcreate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ballball.R
import com.example.ballball.`interface`.OnItemClickListerner
import com.example.ballball.adapter.HomeAdapter
import com.example.ballball.adapter.NewCreateAdapter
import com.example.ballball.databinding.FragmentNewCreateBinding
import com.example.ballball.main.home.all.details.AllDetailsActivity
import com.example.ballball.main.match.newcreate.details.NewCreateDetailsActivity
import com.example.ballball.main.match.upcoming.UpComingViewModel
import com.example.ballball.model.CreateMatchModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewCreateFragment : Fragment() {

    private lateinit var newCreateBinding: FragmentNewCreateBinding
    private lateinit var newCreateAdapter : NewCreateAdapter
    private val newCreateViewModel : NewCreateViewModel by viewModels()
    private val userUID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initObserve()
        if (userUID != null) {
            newCreateViewModel.loadNewCreate(userUID)
        }
    }

    private fun initObserve() {
        loadNewCreateObserve()
    }

    private fun loadNewCreateObserve() {
        newCreateViewModel.loadNewCreate.observe(viewLifecycleOwner) {result ->
            when (result) {
                is NewCreateViewModel.LoadNewCreate.ResultOk -> {
                    newCreateAdapter.addNewData(result.list)
                }
                is NewCreateViewModel.LoadNewCreate.ResultError -> {}
            }
        }
    }

    private fun initList() {
        newCreateBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            newCreateAdapter = NewCreateAdapter(arrayListOf())
            adapter = newCreateAdapter

            newCreateAdapter.setOnItemClickListerner(object :
                OnItemClickListerner {
                override fun onItemClick(requestData: CreateMatchModel) {
                    NewCreateDetailsActivity.startDetails(context, requestData)
                    }
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newCreateBinding = FragmentNewCreateBinding.inflate(layoutInflater)
        return newCreateBinding.root
    }
}