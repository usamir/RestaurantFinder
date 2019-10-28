package com.example.usamir.restaurantfinder

import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by usamir on 10.7.2016.
 */
class Http {
    /**
     * Read from provided httpUrl
     * @param httpUrl
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun read(httpUrl: String): String {
        var httpData = ""
        var inputStream: InputStream? = null
        var httpURLConnection: HttpURLConnection? = null
        try {
            // Connect to provided URL
            val url = URL(httpUrl)
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.connect()

            // Read from url
            inputStream = httpURLConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream!!))
            val stringBuffer = StringBuffer()
            while ((bufferedReader.readLine()) != null) {
                stringBuffer.append(bufferedReader.readLine())
            }
            httpData = stringBuffer.toString()
            bufferedReader.close()
        } catch (e: Exception) {
            Log.d("HTTP", "Error: $e")
        } finally {
            inputStream!!.close()
            httpURLConnection!!.disconnect()
        }
        return httpData
    }
}
