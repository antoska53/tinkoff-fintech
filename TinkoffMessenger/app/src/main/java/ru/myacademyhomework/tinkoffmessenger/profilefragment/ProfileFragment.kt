package ru.myacademyhomework.tinkoffmessenger.profilefragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.myacademyhomework.tinkoffmessenger.database.ChatDatabase
import ru.myacademyhomework.tinkoffmessenger.R
import ru.myacademyhomework.tinkoffmessenger.network.RetrofitModule
import ru.myacademyhomework.tinkoffmessenger.network.User


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var avatar: ImageView? = null
    private var nameUser: TextView? = null
    private var status: TextView? = null
    private var shimmerProfile: ShimmerFrameLayout? = null
    private var errorView: View? = null
    private val compositeDisposable = CompositeDisposable()
    private val userId: Int? by lazy {
        arguments?.getInt(USER_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        avatar = view.findViewById(R.id.imageview_avatar_profile)
        nameUser = view.findViewById(R.id.textview_name_profile)
        status = view.findViewById(R.id.textview_status_profile)
        errorView = view.findViewById(R.id.error_view)
        shimmerProfile = view.findViewById(R.id.shimmer_profile_layout)
        val buttonReload = view.findViewById<Button>(R.id.button_reload)
        buttonReload.setOnClickListener { getOwnUser(view) }
        if (userId == USER_OWNER) {
            getOwnUser(view)
        } else {
            getUserFromDb(view)
        }
    }


    private fun getUserFromDb(view: View) {
        val chatDao = ChatDatabase.getDatabase(requireContext()).chatDao()
        chatDao.getUser(userId!!)
            .map {
                User(
                    avatarURL = it.avatarURL,
                    email = it.email,
                    fullName = it.fullName,
                    userID = it.userID
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    nameUser?.text = it.fullName
                    Glide.with(view)
                        .load(it.avatarURL)
                        .circleCrop()
                        .into(avatar!!)
                    getStatus(view)
                }, {

                }
            )
            .addTo(compositeDisposable)
    }

    private fun getOwnUser(view: View) {
        RetrofitModule.chatApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                shimmerProfile?.visibility = View.VISIBLE
                shimmerProfile?.startShimmer()
                avatar?.visibility = View.GONE
                nameUser?.visibility = View.GONE
                status?.visibility = View.GONE
                errorView?.visibility = View.GONE
            }
            .subscribe({
                nameUser?.text = it.fullName
                Glide.with(view)
                    .load(it.avatarURL)
                    .circleCrop()
                    .into(avatar!!)
                shimmerProfile?.visibility = View.GONE
                shimmerProfile?.stopShimmer()
                avatar?.visibility = View.VISIBLE
                nameUser?.visibility = View.VISIBLE
                status?.visibility = View.VISIBLE
                errorView?.visibility = View.GONE

            }, {
                shimmerProfile?.visibility = View.GONE
                shimmerProfile?.stopShimmer()
                errorView?.visibility = View.VISIBLE
            })
            .addTo(compositeDisposable)
    }

    private fun getStatus(view: View) {
        RetrofitModule.chatApi.getUserPresence(userId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setStatus(it.presence.userStatus.status)
            }, {
                Snackbar.make(
                    view,
                    "Неудалось загрузить статус",
                    Snackbar.LENGTH_SHORT
                ).show()
            })
            .addTo(compositeDisposable)
    }

    private fun setStatus(userStatus: String) {
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


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
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