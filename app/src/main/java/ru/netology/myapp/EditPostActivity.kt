package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.databinding.AcEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AcEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oldContent = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
        binding.editPostString.setText(oldContent)

        binding.commitChanges.setOnClickListener {
            val newContent = binding.editPostString.text.toString()
            if(newContent.isBlank()) {
                setResult(RESULT_CANCELED)
            }else {
                val result = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, newContent)
                }
                setResult(RESULT_OK, result)
            }
            finish()
        }
        binding.cancelChanges.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

    }
}