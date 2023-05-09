package ru.myacademyhomework.tinkoffmessenger.presentation.streamfragment.newstreamfragment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myacademyhomework.tinkoffmessenger.presentation.App
import ru.myacademyhomework.tinkoffmessenger.presentation.FlowFragment
import ru.myacademyhomework.tinkoffmessenger.R
import javax.inject.Inject
import javax.inject.Provider


class NewStreamFragment : MvpAppCompatFragment(R.layout.fragment_new_stream), NewStreamView {

    @Inject
    lateinit var providePresenter: Provider<NewStreamPresenter>
    private val newStreamPresenter by moxyPresenter {
        providePresenter.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.getNewStreamComponent().inject(this)
        super.onCreate(savedInstanceState)

        setStatusBarColor(FlowFragment.LIGHT_COLOR)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edittextNameStream = view.findViewById<EditText>(R.id.edittext_name_new_stream)
        val edittextDescriptionStream = view.findViewById<EditText>(R.id.edittext_description_new_stream)
        val buttonCreateStream = view.findViewById<MaterialButton>(R.id.button_create_new_stream)
        buttonCreateStream.setOnClickListener {
            newStreamPresenter.createNewStream(
                edittextNameStream.text.toString(), edittextDescriptionStream.text.toString())
        }
        val buttonBack = view.findViewById<ImageView>(R.id.imageView_arrow_back)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun setStatusBarColor(color: Int) {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(color, null)
    }

    override fun showRefresh() {}

    override fun hideRefresh() {}

    private fun showSnackbar(message: String){
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showSuccessCreate(){
        showSnackbar(getString(R.string.stream_created))
    }

    override fun showError() {
        showSnackbar(getString(R.string.error_stream_created))
    }

    override fun showEmptyNameDescription() {
        showSnackbar(getString(R.string.enter_name_and_description))
    }

    override fun onDestroy() {
        super.onDestroy()

        setStatusBarColor(FlowFragment.DARK_COLOR)
    }

    companion object {

        @JvmStatic
        fun newInstance() = NewStreamFragment()
    }

}