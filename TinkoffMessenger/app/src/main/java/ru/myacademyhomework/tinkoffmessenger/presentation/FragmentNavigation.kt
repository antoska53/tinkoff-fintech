package ru.myacademyhomework.tinkoffmessenger.presentation

import androidx.fragment.app.Fragment

interface FragmentNavigation {
    fun openChatFragment(fragment: Fragment)
    fun changeFragment(fragment: Fragment, toBackstack:Boolean)
    fun changeFlowFragment(fragment: Fragment, toBackstack: Boolean)
}