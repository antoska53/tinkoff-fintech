package ru.myacademyhomework.tinkoffmessenger.common

import androidx.fragment.app.Fragment

abstract class PresenterFragment<P : BasePresenter>(val layout: Int) : Fragment(layout) {

    abstract fun getPresenter(): P?

    override fun onDetach() {
        if (getPresenter() != null) {
            getPresenter()?.disposeAll()
        }
        super.onDetach()
    }
}
