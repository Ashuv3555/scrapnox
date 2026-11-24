package in.scrapnox.app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(false)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                return handleUrl(url)
            }

            @Suppress("DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return handleUrl(url ?: "")
            }
        }

        // Landing page app ke andar bundled hai — offline bhi kaam karta hai
        webView.loadUrl("file:///android_asset/index.html")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) webView.goBack() else finish()
            }
        })
    }

    /** tel:, mailto:, wa.me aur bahar ke links ko native apps se handle karta hai */
    private fun handleUrl(url: String): Boolean {
        val lower = url.lowercase()
        return when {
            lower.startsWith("tel:") -> {
                runCatching { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url))) }
                true
            }
            lower.startsWith("mailto:") -> {
                runCatching { startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(url))) }
                true
            }
            lower.contains("wa.me") || lower.contains("api.whatsapp.com") -> {
                val opened = runCatching {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }.isSuccess
                if (!opened) Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                true
            }
            lower.startsWith("http://") || lower.startsWith("https://") -> {
                runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
                true
            }
            else -> false
        }
    }
}
