package nz.ac.uclive.shopport.common.camera

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.FlipCameraAndroid
import androidx.compose.material.icons.twotone.Lens
import androidx.compose.material.icons.twotone.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraView(
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onClose: () -> Unit,
) {

    val context = LocalContext.current
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onImageCaptured(uri, true)
    }

    CameraPreviewView(
        imageCapture,
        lensFacing,
    ) { cameraUIAction ->
        when (cameraUIAction) {
            is CameraUIAction.OnCameraClick -> {
                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
            }
            is CameraUIAction.OnSwitchCameraClick -> {
                lensFacing =
                    if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                    else
                        CameraSelector.LENS_FACING_BACK
            }
            is CameraUIAction.OnGalleryViewClick -> {
                if (context.getOutputDirectory().listFiles()?.isNotEmpty() == true) {
                    galleryLauncher.launch("image/*")
                }
            }
            is CameraUIAction.OnClose -> {
                onClose()
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun CameraPreviewView(
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    cameraUIAction: (CameraUIAction) -> Unit,
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val previewView = remember { PreviewView(context) }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { previewView },
            modifier = Modifier.fillMaxSize()) {
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
          IconButton(onClick = {
              cameraUIAction(CameraUIAction.OnClose)
          }) {
              Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(72.dp))
          }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.8f)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            CameraControls(cameraUIAction)
        }

    }
}

@Composable
fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CameraControl(
            Icons.TwoTone.FlipCameraAndroid,
            modifier= Modifier.size(32.dp),
            onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
        )

        CameraControl(
            Icons.TwoTone.Lens,
            modifier= Modifier
                .size(72.dp)
                .padding(1.dp)
                .border(1.dp, Color.White, CircleShape),
            onClick = {
                cameraUIAction(CameraUIAction.OnCameraClick)
              },
            tint = MaterialTheme.colorScheme.primary
        )

        CameraControl(
            Icons.TwoTone.PhotoLibrary,
            modifier= Modifier.size(32.dp),
            onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) },
        )

    }
}


@Composable
fun CameraControl(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    }

}