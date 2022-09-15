package nz.ac.uclive.shopport.explore

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

fun parameteriseUrl(url: String, parameters: Map<String, String>): URL {
    val builder = Uri.parse(url).buildUpon()
    parameters.forEach { (key, value) -> builder.appendQueryParameter(key, value) }
    val uri = builder.build()
    return URL(uri.toString())
}

suspend fun getJson(url: URL): JSONObject {
    val result = withContext(Dispatchers.IO) {
        val connection = url.openConnection() as HttpsURLConnection
        try {
            val json = BufferedInputStream(connection.inputStream).readBytes()
                .toString(Charset.defaultCharset())
            JSONObject(json)
        } finally {
            connection.disconnect()
        }
    }
    return result
}

