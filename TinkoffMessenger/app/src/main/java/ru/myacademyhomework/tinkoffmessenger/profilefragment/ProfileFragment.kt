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
import ru.myacademyhomework.tinkoffmessenger.AppDelegate
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.di.profile.ProfileModule
import ru.myacademyhomework.tinkoffmessenger.network.User
import javax.inject.Inject
import javax.inject.Provider


class ProfileFragment : MvpAppCompatFragment(R.layout.fragment_profile), ProfileView {

    private var avatar: ImageView? = null
    private var nameUser: TextView? = null
    private var status: TextView? = null
    private var shimmerProfile: ShimmerFrameLayout? = null
    private var errorView: View? = null
    private val userId: Int by lazy {
        arguments?.getInt(USER_ID) ?: -1
    }

    @Inject
    lateinit var providePresenter: Provider<ProfilePresenter>
    private val profilePresenter by moxyPresenter {
        providePresenter.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppDelegate.appComponent.getProfileComponent(ProfileModule()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePresenter.loadUserId(userId)

        avatar = view.findViewById(R.id.imageview_avatar_profile)
        nameUser = view.findViewById(R.id.textview_name_profile)
        status = view.findViewById(R.id.textview_status_profile)
        errorView = view.findViewById(R.id.error_view)
        shimmerProfile = view.findViewById(R.id.shimmer_profile_layout)

        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { profilePresenter.getOwnUser() }

        profilePresenter.refreshData()
    }

    override fun showUserProfile(user: User) {
        nameUser?.text = user.fullName
        Glide.with(requireContext())
            .load(user.avatarURL)
            .circleCrop()
            .into(avatar!!)
    }

    override fun showStatus(idStatus: Int, idColor: Int) {
        status?.text = getString(idStatus)
        status?.setTextColor(requireContext().getColor(idColor))
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
