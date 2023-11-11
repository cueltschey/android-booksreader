package com.example.android_booksreader.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.barteksc.pdfviewer.PDFView

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _pdfView = MutableLiveData<PDFView>().apply {
        value = null
    }

    val pdfView: LiveData<PDFView> = _pdfView
}