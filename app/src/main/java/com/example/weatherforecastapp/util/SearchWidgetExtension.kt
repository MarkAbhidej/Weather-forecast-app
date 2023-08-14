package com.example.weatherforecastapp.util

import android.text.Editable
import android.text.TextWatcher
import com.mancj.materialsearchbar.MaterialSearchBar

fun MaterialSearchBar.doOnAfterTextChange(action: (String) -> Unit) {
    this.addTextChangeListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                action(it.toString())
            }
        }

    })
}

fun MaterialSearchBar.doOnConfirm(action: (String) -> Unit) {
    this.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
        override fun onSearchStateChanged(enabled: Boolean) {
            if (!enabled) {
                hideSuggestionsList()
            }
        }

        override fun onSearchConfirmed(text: CharSequence?) {
            text?.let { action(it.toString()) }
        }

        override fun onButtonClicked(buttonCode: Int) {}

    })
}