package ru.myacademyhomework.tinkoffmessenger

import androidx.fragment.app.Fragment

interface FragmentNavigation {
    fun changeFlowFragment(fragment: Fragment)
    fun changeBottomNavFragment(fragment: Fragment, toBackstack:Boolean)
}