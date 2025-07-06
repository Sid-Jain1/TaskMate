package edu.vt.mobiledev.taskmate

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs

import edu.vt.mobiledev.taskmate.databinding.FragmentPhotoDialogBinding
import java.io.File

class PhotoDialogFragment : DialogFragment() {

    private val args: PhotoDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentPhotoDialogBinding.inflate(LayoutInflater.from(context))
        val photoView: ImageView = binding.photoView

        val photoFile = File(
            requireContext().applicationContext.filesDir,
            args.photoFilename
        )

        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, 800, 800)
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageResource(R.drawable.ic_broken_image) // fallback icon
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }
}
