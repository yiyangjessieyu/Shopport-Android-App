package nz.ac.uclive.shopport.common.camera

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import nz.ac.uclive.shopport.R
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                context.getString(R.string.selected_image_),
                context.getString(R.string.jpg),
                directory,
            )
            val authority = context.packageName + context.getString(R.string.fileprovider)
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}
