package com.example.sabidus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Alterar a cor da ActionBar para azul
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_blue_color))); // Definido em colors.xml
        }

        // Alterar a cor da barra de status para azul (somente para Android 5.0 ou superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.my_blue_color)); // Definido em colors.xml
        }

        // Inicializa a WebView
        webview = findViewById(R.id.webview);

        // Configurações da WebView
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilita JavaScript
        webSettings.setDomStorageEnabled(true); // Habilita armazenamento DOM
        webSettings.setLoadWithOverviewMode(true); // Ajusta o conteúdo para caber na tela
        webSettings.setUseWideViewPort(true); // Habilita viewport amplo
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // Evita uso de cache
        webSettings.setAllowContentAccess(true); // Permite acesso a conteúdo interno
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // Permite conteúdo misto (HTTP + HTTPS)

        // Configura o cliente da WebView para capturar navegação
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("WebView", "Carregando página: " + url);
                Toast.makeText(MainActivity.this, "Carregando: " + url, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebView", "Página carregada: " + url);
                Toast.makeText(MainActivity.this, "Página carregada: " + url, Toast.LENGTH_SHORT).show();
            }
        });

        // Configura o cliente da WebChrome para lidar com alertas JavaScript
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Alerta")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmação")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Prompt")
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm(defaultValue))
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                        .create()
                        .show();
                return true;
            }
        });

        // Carrega a URL ou restaura o estado anterior
        if (savedInstanceState == null) {
            webview.loadUrl("https://sabidus-front-end.vercel.app"); // Substitua pelo URL do seu Frontend
        } else {
            webview.restoreState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webview.saveState(outState); // Salva o estado da WebView
    }

    @Override
    public void onBackPressed() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack();  // Volta para a página anterior na WebView
        } else {
            super.onBackPressed();  // Comportamento padrão: fecha a aplicação
        }
    }
}
