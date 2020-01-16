package com.linagora.android.linshare.view.myspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.linagora.android.linshare.R
import com.linagora.android.linshare.databinding.FragmentMySpaceBinding
import com.linagora.android.linshare.util.getViewModel
import com.linagora.android.linshare.view.MainNavigationFragment
import kotlinx.android.synthetic.main.fragment_my_space.swipeLayoutMySpace
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MySpaceFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mySpaceViewModel: MySpaceViewModel

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MySpaceFragment::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMySpaceBinding.inflate(inflater, container, false)
        initViewModel(binding)
        return binding.root
    }

    private fun initViewModel(binding: FragmentMySpaceBinding) {
        mySpaceViewModel = getViewModel(viewModelFactory)
        binding.lifecycleOwner = this
        binding.viewModel = mySpaceViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LOGGER.info("onViewCreated")
        setUpSwipeRefreshLayout()
        getAllDocuments()
    }

    private fun setUpSwipeRefreshLayout() {
        swipeLayoutMySpace.setColorSchemeResources(R.color.colorPrimary)
    }

    private fun getAllDocuments() {
        LOGGER.info("getAllDocuments")
        mySpaceViewModel.getAllDocuments()
    }
}
