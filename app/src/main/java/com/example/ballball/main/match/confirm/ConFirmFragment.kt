package com.example.ballball.main.match.confirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ballball.`interface`.OnItemClickListerner
import com.example.ballball.adapter.ConfirmAdapter
import com.example.ballball.databinding.FragmentConFirmBinding
import com.example.ballball.main.match.confirm.details.ConfirmDetailsActivity
import com.example.ballball.main.match.wait.details.WaitDetailsActivity
import com.example.ballball.model.CreateMatchModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConFirmFragment : Fragment() {

    private lateinit var conFirmBinding: FragmentConFirmBinding
    private lateinit var confirmAdapter: ConfirmAdapter
    private val confirmViewModel : ConfirmViewModel by viewModels()
    private val userUID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initObserve()
        if (userUID != null) {
            confirmViewModel.loadConfirmList(userUID)
        }
    }

    private fun initObserve() {
        confirmViewModel.loadConfirm.observe(viewLifecycleOwner) {result ->
            when (result) {
                is ConfirmViewModel.LoadConfirmResult.ResultOk -> {
                    confirmAdapter.addNewData(result.list)
                }
                is ConfirmViewModel.LoadConfirmResult.ResultError -> {}
            }
        }
    }

    private fun initList() {
        conFirmBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            confirmAdapter = ConfirmAdapter(arrayListOf())
            adapter = confirmAdapter

            confirmAdapter.setOnItemClickListerner(object :
                OnItemClickListerner {
                override fun onItemClick(requestData: CreateMatchModel) {
                    ConfirmDetailsActivity.startDetails(context, requestData)
                    }
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        conFirmBinding = FragmentConFirmBinding.inflate(inflater, container, false)
        return conFirmBinding.root
    }
}