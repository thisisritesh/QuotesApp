package com.riteshmaagadh.quotesapp.ui.share

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.riteshmaagadh.quotesapp.R
import com.riteshmaagadh.quotesapp.data.callbacks.OnShareButtonClicked
import com.riteshmaagadh.quotesapp.databinding.ShareBottomSheetItemLayoutBinding


class ShareBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.NoBackgroundDialogTheme

    private lateinit var binding: ShareBottomSheetItemLayoutBinding
    private lateinit var onShareButtonClicked: OnShareButtonClicked

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onShareButtonClicked = context as OnShareButtonClicked
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShareBottomSheetItemLayoutBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        val view = binding.root
        view.setBackgroundResource(R.drawable.bottom_sheet_bg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareWithYourFriends.setOnClickListener {
            share()
        }


        binding.copyText.setOnClickListener {
            val clipboard: ClipboardManager? = requireActivity().getSystemService(CLIPBOARD_SERVICE)!! as ClipboardManager?
            val clip = ClipData.newPlainText("label", "Text to copy")
            clipboard!!.setPrimaryClip(clip)

            Toast.makeText(requireContext(),"Copied!",Toast.LENGTH_SHORT).show()
        }

        binding.saveImage.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                onShareButtonClicked.onShareButtonClicked()
                dialog?.dismiss()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1100)
            }
        }

    }

    private fun share(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share with your friends!")
        intent.putExtra(Intent.EXTRA_TEXT, "mQuote.quote")
        startActivity(Intent.createChooser(intent, "Share"))
    }


}