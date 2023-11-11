package com.example.android_booksreader.ui.slideshow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_booksreader.MainActivity
import com.example.android_booksreader.R
import com.example.android_booksreader.databinding.FragmentUploadBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import android.content.ContentResolver
import android.content.Context
import android.os.Debug


class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val PICK_PDF_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(UploadViewModel::class.java)

        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val uploadButton = binding.root.findViewById<Button>(R.id.selectFileButton)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, PICK_PDF_REQUEST)
        }
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val context : Context = requireContext()
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { pdfUri ->
                val pdfName = getFileName(context, pdfUri)
                uploadPdfToFirebase(pdfUri, pdfName)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getFileName(context : Context, uri: Uri): String {
        var displayName = ""
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index : Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if(index >= 0){
                    displayName = it.getString(index)
                }
                return displayName
            }
        }
        return displayName
    }

    private fun uploadPdfToFirebase(uri: Uri, fileName: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("pdfs").child(fileName)

        fileRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // File uploaded successfully
                val downloadUrl = taskSnapshot.storage.downloadUrl.toString()
                binding.textSlideshow.text = "Upload Successful"
            }
            .addOnFailureListener { exception ->
                // Handle unsuccessful uploads
                Toast.makeText(context, "Upload failed: $exception", Toast.LENGTH_SHORT).show()
                binding.textSlideshow.text = exception.toString()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}