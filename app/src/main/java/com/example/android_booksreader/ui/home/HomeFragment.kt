package com.example.android_booksreader.ui.home

import android.media.SoundPool
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.android_booksreader.R
import com.example.android_booksreader.databinding.FragmentHomeBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var pdfView: PDFView
    private lateinit var storageRef: StorageReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        pdfView = root.findViewById(R.id.pdfView)

        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        downloadPdf("pdfs/lab 7 screenshots.pdf")

        return root
    }
    private fun downloadPdf(pdfFilePath: String) {
        val localFile = File.createTempFile("tempPdf", "pdf")
        val localFileUri = localFile.toUri()

        val pdfRef = storageRef.child(pdfFilePath)

        pdfRef.getFile(localFile)
            .addOnSuccessListener {
                // PDF downloaded successfully
                displayPdf(localFileUri)
            }
            .addOnFailureListener { exception ->
                // Handle unsuccessful download
                Toast.makeText(context, "Unable to load: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayPdf(pdfUri: Uri) {
        pdfView.visibility = View.VISIBLE
        pdfView.fromUri(pdfUri)
            .onLoad(object : OnLoadCompleteListener {
                override fun loadComplete(nbPages: Int) {
                    // Do something when PDF is loaded
                }
            })
            .onPageChange(object : OnPageChangeListener {
                override fun onPageChanged(page: Int, pageCount: Int) {
                    // Do something when page changes
                }
            })
            .load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}