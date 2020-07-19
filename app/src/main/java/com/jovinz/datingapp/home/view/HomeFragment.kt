package com.jovinz.datingapp.home.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jovinz.datingapp.R
import com.jovinz.datingapp.app.BaseApplication
import com.jovinz.datingapp.common.preferences.UserPrefs
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.home.viewmodel.HomeViewModel
import com.jovinz.datingapp.network.Resource
import com.jovinz.datingapp.network.RetrofitBuilder
import com.jovinz.datingapp.utils.Constants.GENDER
import com.jovinz.datingapp.utils.Constants.MAX_REWINDS
import com.jovinz.datingapp.utils.ViewModelFactory
import com.jovinz.datingapp.utils.gone
import com.jovinz.datingapp.utils.visible
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.android.synthetic.main.fragment_cards.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_cards), CardStackListener {

    private val navController: NavController by lazy { findNavController() }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory((RetrofitBuilder.apiService))
        ).get(HomeViewModel::class.java)
    }
    private var rewindCounter = 0
    private val profileCardsAdapter: ProfileCardsAdapter by lazy { ProfileCardsAdapter() }
    private var filteredList = mutableListOf<ProfilesResponseItem>()
    private var currentPosition = 0


    //--------------------------------------------FRAGMENT LIFECYCLE-----------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        homeViewModel.getProfiles()
        homeViewModel.getProfilesFromLocal()
        setObservers()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.my_profile ->
                navController.navigate(R.id.action_fragment_home_to_fragment_sign_up)
            R.id.my_liked_profiles ->
                navController.navigate(R.id.action_fragment_home_to_fragment_liked_profs)
            R.id.logout -> {
                clearDataAndLogOut()
            }
        }
        return true
    }

    //--------------------------------------------DISPLAY DATA--------------------------------------------//

    private fun initView() {
        val layoutManager = CardStackLayoutManager(context, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }

        cardStackProfiles.layoutManager = layoutManager
        cardStackProfiles.adapter = profileCardsAdapter
        cardStackProfiles.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        tvRewind.text = if (UserPrefs.getString(GENDER)
                ?.equals(getString(R.string.female), true)!!
        ) getString(R.string.rewind_female) else getString(R.string.rewind_male)

        tvRewind.setOnClickListener {
            checkRewindsAndNavigate()
        }
    }

    private fun setObservers() {
        homeViewModel.getProfilesLiveData().observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    progressBarHome.visible()
                    tvError.gone()
                    cardStackProfiles.gone()
                    tvRewind.gone()
                }
                Resource.Status.ERROR -> {
                    progressBarHome.gone()
                    tvError.visible()
                    cardStackProfiles.gone()
                    tvRewind.gone()
                }
                Resource.Status.SUCCESS -> {
                    progressBarHome.gone()
                    cardStackProfiles.visible()
                    if (response.data.isNullOrEmpty()) {
                        tvError.visible()
                    } else {
                        tvError.gone()
                        tvRewind.visible()
                        filteredList.clear()
                        filteredList.addAll(response.data)
                        profileCardsAdapter.setProfiles(filteredList.toMutableList())
                    }
                }
            }
        })
    }


    //--------------------------------------------LOG OUT-----------------------------------------//

    private fun clearDataAndLogOut() {
        CoroutineScope(Dispatchers.IO).launch {
            BaseApplication.appDatabase?.profilesDao()?.nukeTable()
        }
        UserPrefs.clearAllData()
        val navHost =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)
        navController.graph = graph
        graph.startDestination = R.id.fragment_sign_up
    }

    //--------------------------------------------CARD STACKS-----------------------------------------//

    private fun checkRewindsAndNavigate() {
        if (rewindCounter < MAX_REWINDS) {
            rewindCounter++
            cardStackProfiles.rewind()
        } else {
            navController.navigate(R.id.action_fragment_home_to_fragment_pay)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            homeViewModel.insertProfileIntoDb(filteredList[currentPosition])
        }
    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
        currentPosition = position
    }

    override fun onCardRewound() {
    }


}