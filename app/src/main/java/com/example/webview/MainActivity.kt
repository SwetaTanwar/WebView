package com.example.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_FINE_LOCATION = 1
    private val READ_CONTACTS = 0
    private var geolocationOrigin: String? = null
    private var geolocationCallback: GeolocationPermissions.Callback? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setGeolocationEnabled(true);

        webView.loadUrl("https://www.kalatmak.co.in/")

        webView.webChromeClient = mWebChromeClient;
    }


    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {

        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            val perm: String = android.Manifest.permission.ACCESS_FINE_LOCATION
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this@MainActivity, perm) == PackageManager.PERMISSION_GRANTED) {
                // we're on SDK < 23 OR user has already granted permission
                callback!!.invoke(origin, true, false)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, perm)) {
                    // ask the user for permission
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(perm), REQUEST_FINE_LOCATION)

                    // we will use these when user responds
                    geolocationOrigin = origin
                    geolocationCallback = callback
                }
            }
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
            val perm: String = android.Manifest.permission.READ_CONTACTS
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(perm), READ_CONTACTS);
        }

    }
         override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
            when (requestCode) {
                REQUEST_FINE_LOCATION -> {
                    var allow = false
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // user has allowed this permission
                        allow = true
                    }
                    geolocationCallback?.invoke(geolocationOrigin, allow, false)
                }
                READ_CONTACTS -> {
                    var allow = false
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // user has allowed this permission
                        allow = true
                    }

                }
            }
        }
}


