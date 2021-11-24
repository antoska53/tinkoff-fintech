package ru.myacademyhomework.tinkoffmessenger.profilefragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.User


class ProfileFragment : MvpAppCompatFragment(R.layout.fragment_profile), ProfileView {

    private var avatar: ImageView? = null
    private var nameUser: TextView? = null
    private var status: TextView? = null
    private var shimmerProfile: ShimmerFrameLayout? = null
    private var errorView: View? = null
    private val userId: Int? by lazy {
        arguments?.getInt(USER_ID)
    }
    private val chatDao by lazy { ChatDatabase.getDatabase(requireContext()).chatDao()}
    private val profilePresenter by moxyPresenter {
//        ProfilePresenter(chatDao, userId!!)
        ProfilePresenterTest()
    }
//
//    @ProvidePresenter
//    fun providePresenter(): ProfilePresenter{
//        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
//        return ProfilePresenter(chatDao, userId!!)
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        avatar = view.findViewById(R.id.imageview_avatar_profile)
        nameUser = view.findViewById(R.id.textview_name_profile)
        status = view.findViewById(R.id.textview_status_profile)
        errorView = view.findViewById(R.id.error_view)
        shimmerProfile = view.findViewById(R.id.shimmer_profile_layout)

        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { profilePresenter?.getOwnUser() }

        refreshData()
    }

    private fun refreshData() {
        if (userId == USER_OWNER) {
            profilePresenter?.getOwnUser()
        } else {
            profilePresenter?.getUserFromDb()
            profilePresenter?.getStatus()
        }
    }

    override fun showUserProfile(user: User) {
        nameUser?.text = user.fullName
        Glide.with(requireContext())
            .load(user.avatarURL)
            .circleCrop()
            .into(avatar!!)
    }

    override fun showStatus(userStatus: String) {
        when (userStatus) {
            "active" -> {
                status?.text = getString(R.string.status_online)
                status?.setTextColor(requireContext().getColor(R.color.status_online_color))
            }
            "offline" -> {
                status?.text = getString(R.string.status_offline)
                status?.setTextColor(requireContext().getColor(R.color.status_offline_color))
            }
            "idle" -> {
                status?.text = getString(R.string.status_idle)
                status?.setTextColor(requireContext().getColor(R.color.status_idle_color))
            }
        }
    }

    override fun showErrorLoadStatus() {
        Snackbar.make(
            requireView(),
            "Неудалось загрузить статус",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showRefresh() {
        shimmerProfile?.isVisible = true
        shimmerProfile?.startShimmer()
        avatar?.isVisible = false
        nameUser?.isVisible = false
        status?.isVisible = false
        errorView?.isVisible = false
    }

    override fun hideRefresh() {
        shimmerProfile?.isVisible = false
        shimmerProfile?.stopShimmer()
        avatar?.isVisible = true
        nameUser?.isVisible = true
        status?.isVisible = true
        errorView?.isVisible = false
    }

    override fun showError() {
        shimmerProfile?.isVisible = false
        shimmerProfile?.stopShimmer()
        errorView?.isVisible = true
    }

//    override fun getPresenter(): ProfilePresenter? = profilePresenter

    companion object {
        const val USER_ID = "USER_ID"
        const val USER_OWNER = 0

        @JvmStatic
        fun newInstance(userId: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(USER_ID, userId)
            }
        }
    }
}
