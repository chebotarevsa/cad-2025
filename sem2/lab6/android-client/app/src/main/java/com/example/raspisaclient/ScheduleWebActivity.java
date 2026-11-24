package com.example.raspisaclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class ScheduleWebActivity extends AppCompatActivity {
    private static final String HTML =
            "<!doctype html><html><head><meta charset='utf-8'>"
            + "<meta name='viewport' content='width=device-width,initial-scale=1'>"
            + "<style>body{font-family:sans-serif;margin:12px;background:#f7f7f7}"
            + "h2{margin:0 0 12px} .card{background:#fff;padding:10px;margin-bottom:8px;border-radius:6px;border:1px solid #ddd}"
            + ".t{font-weight:bold;font-size:15px} .m{color:#555;font-size:13px;margin-top:4px}</style></head><body>"
            + "<h2>Расписание занятий</h2><div id='list'>Загрузка...</div>"
            + "<script>"
            + "var xhr = new XMLHttpRequest();"
            + "xhr.open('GET', '/api/lessons', true);"
            + "xhr.onload = function() {"
            + "  var data = JSON.parse(xhr.responseText);"
            + "  var html = '';"
            + "  for (var i = 0; i < Math.min(data.length, 15); i++) {"
            + "    var l = data[i];"
            + "    html += '<div class=\"card\"><div class=\"t\">' + (l.studioName || l.title || 'Занятие') + '</div>'"
            + "      + '<div class=\"m\">' + l.dayOfWeek + ' · ' + (l.time || '') + ' · Группа ' + (l.groupNumber || '-') + '</div>'"
            + "      + '<div class=\"m\">' + (l.teacherName || '') + '</div></div>';"
            + "  }"
            + "  document.getElementById('list').innerHTML = html || 'Нет занятий';"
            + "};"
            + "xhr.onerror = function() { document.getElementById('list').innerHTML = 'Ошибка загрузки'; };"
            + "xhr.send();"
            + "</script></body></html>";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_web);

        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadDataWithBaseURL(ApiClient.BASE_URL, HTML, "text/html", "UTF-8", null);
    }
}
