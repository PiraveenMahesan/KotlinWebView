package com.piraveen.identosinterview

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var urlEditText: EditText
    private lateinit var backButton: Button
    private lateinit var forwardButton: Button
    private lateinit var goButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        urlEditText = findViewById(R.id.urlEditText)
        backButton = findViewById(R.id.backButton)
        forwardButton = findViewById(R.id.forwardButton)
        goButton = findViewById(R.id.goButton)

        // Configure WebView settings
        webView.settings.javaScriptEnabled = true

        // Set WebViewClient to control navigation and visibility
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                progressBar.visibility = View.VISIBLE // Show progress bar
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE // Hide progress bar
                urlEditText.setText(url)
                updateNavigationButtons() // Update button states
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                progressBar.visibility = View.GONE // Hide ProgressBar
                showErrorDialog("Failed to load the page. Please check the URL or your network connection.")
            }
        }
        webView.loadUrl("https://www.google.com")

        // Set up the Go button click listener
        goButton.setOnClickListener {
            val url = urlEditText.text.toString()
            if (url.isNotEmpty()) {
                // Ensure the URL has a valid scheme (http/https)
                val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    "https://$url"
                } else {
                    url
                }
                webView.loadUrl(formattedUrl)
            }
        }

        // Set up Back button click listener
        backButton.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }

        // Set up Forward button click listener
        forwardButton.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }
    }

    // Update Back and Forward buttons based on WebView history availability
    private fun updateNavigationButtons() {
        backButton.isEnabled = webView.canGoBack()
        forwardButton.isEnabled = webView.canGoForward()
    }

    // Show an error dialog when the WebView fails to load a URL
    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

}