package com.exomind.feature.fetch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomind.R
import com.exomind.base.BaseFragment
import com.exomind.data.utils.Resource
import com.exomind.databinding.FragmentFetchWeatherBinding
import com.exomind.domain.model.WeatherEntity
import com.exomind.extensions.hide
import com.exomind.extensions.setupItemDecoration
import com.exomind.extensions.show
import com.exomind.feature.fetch.adapter.FetchWeatherAdapter
import com.exomind.feature.home.HomeActivity
import com.exomind.feature.home.HomeViewModel
import kotlinx.coroutines.flow.collect

class FetchWeatherFragment : BaseFragment() {

    private lateinit var binding: FragmentFetchWeatherBinding
    private lateinit var viewModel: HomeViewModel
    private val citiesWithData = mutableListOf<WeatherEntity>()

    /**
     * Fragment Callbacks
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as HomeActivity).viewModel
        initObservationsAndCollections()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetProgress()
    }

    /**
     * Initialize observing livedata and stateflow
     */
    private fun initObservationsAndCollections() {
        viewModel.apply {
            startFetchingWeather()
            sendNewWaitingMessage()

            lifecycleScope.launchWhenStarted {
                weatherState.collect { resource ->
                    manageWeatherStates(resource)
                }
            }

            progressState.observe(viewLifecycleOwner, { progress ->
                updateProgressBar(progress)
                setProgressText(progress)

                if (progress >= PROGRESS_COMPLETE_VALUE) {
                    manageRetryFetch()
                    showWeatherDataList(citiesWithData)
                }
            })

            waitingMessageState.observe(viewLifecycleOwner, {
                setWaitingMessage(it)
            })
        }
    }

    /**
     * Manage all states of the response (Loading, Success, Error)
     */
    private fun manageWeatherStates(resource: Resource<WeatherEntity>) {
        when (resource.status) {
            Resource.Status.LOADING -> {
                Log.i(TAG, "Status: Loading")
            }
            Resource.Status.SUCCESS -> {
                viewModel.incrementProgress()
                resource.data?.let { citiesWithData.add(it) }
            }
            Resource.Status.ERROR -> {
                // Show a long toast when request fails
                resource.message?.let { context?.toast(it) }
            }
        }
    }

    /**
     * Update progress of the bar every 10sec after the request
     */
    private fun updateProgressBar(value: Int) {
        binding.progressBar.apply {
            progress = value
        }
    }

    /**
     * Set text to current progress with percentage sign
     */
    private fun setProgressText(progress: Int) {
        binding.progressText.text = getString(
            R.string.fetch_progress,
            if (progress < PROGRESS_COMPLETE_VALUE) progress else PROGRESS_COMPLETE_VALUE
        )
    }

    /**
     * Set new waiting message every 6sec
     */
    private fun setWaitingMessage(message: String) {
        binding.fetchMessage.text = message
    }

    /**
     * Hide waiting message & loading (progressbar)
     * Show button that allows to retry fetching
     */
    private fun manageRetryFetch() {
        showRetryFetch(true)
        binding.retryFetchButton.setOnClickListener {
            viewModel.apply {
                showRetryFetch(false)
                resetProgress()
                startFetchingWeather()
                sendNewWaitingMessage()
                // FIXME: Not a good practice, should be changed
                citiesWithData.clear()
                binding.weatherList.adapter = FetchWeatherAdapter(requireContext(), citiesWithData)
            }
        }
    }

    /**
     * Manage visibility of retry button / header
     */
    private fun showRetryFetch(isVisible: Boolean) {
        if (isVisible) {
            binding.fetchWeatherHeader.hide()
            binding.retryFetchButton.show()
        } else {
            binding.retryFetchButton.hide()
            binding.fetchWeatherHeader.show()
        }
    }

    /**
     * Initialize RecyclerView
     */
    private fun showWeatherDataList(list: List<WeatherEntity>) {
        binding.weatherList.apply {
            setupItemDecoration(context, LinearLayoutManager.VERTICAL)
            adapter = FetchWeatherAdapter(
                requireContext(),
                // FIXME: Same here, not a good practice, it was just to test
                list.take(viewModel.numberOfCities())
            )
        }
    }

    /**
     * Companion object
     */
    companion object {
        private const val PROGRESS_COMPLETE_VALUE = 100
        private const val TAG = "FetchWeatherFragment"
    }
}