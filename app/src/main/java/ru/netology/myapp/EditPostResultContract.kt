package ru.netology.myapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditPostResultContract: ActivityResultContract<String, String?>() {
    override fun createIntent(
        context: Context,
        input: String
    ): Intent {
        return Intent(context, EditPostActivity::class.java).apply {
            putExtra(Intent.EXTRA_TEXT, input)
        }
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): String? {
        if (resultCode != AppCompatActivity.RESULT_OK) return null
        return intent?.getStringExtra(Intent.EXTRA_TEXT)
    }

}