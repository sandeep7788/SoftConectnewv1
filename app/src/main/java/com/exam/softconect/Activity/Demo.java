package com.exam.softconect.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.exam.softconect.R;
import com.nishant.math.MathView;

import java.nio.charset.StandardCharsets;



public class Demo extends AppCompatActivity {

    MathView mathJaxWebView;
    MathView t2;
    TextView t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mathJaxWebView=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);



        byte[] data = Base64.decode("V2hlbiBcKGEgXG5lIDBcKSwgdGhlcmUgYXJlIHR3byBzb2x1dGlvbnMgdG8gXChheF4yICsgYnggKyBjID0gMFwpICBhbmQgdGhleSBhcmUgDQoNClwoeCA9IHstYiBccG0gXHNxcnR7Yl4yLTRhY30gXG92ZXIgMmF9LiBcKQ==", Base64.DEFAULT);
        String question = new String(data, StandardCharsets.UTF_8);
        question = question.replace("\\", "\\\\");

//When \\(a \\ne 0\\), there are two solutions to \\(ax^2 + bx + c = 0\\)  and they are
//    \\(x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}. \\)
        Log.e("@@data1",data.toString());
        Log.e("@@data2",question.toString());
        //t1.setText(String.valueOf(Html.fromHtml("<div style=\"text-align: left\">this text should be in center of view</div>")));
//        String centerNRed = "<body style='text-align:left' ><span style='color:red' ><h1 align=\"left\">Hello World Its me.....</h1></span></body>";

//        t2.loadData("<html><body  align='left'>You scored <b>\""+'{' + question + '}'+"</b> points.</body></html>", "text/html", null);
        String centeredHtml = "<div style=\"text-align: center\">" + "hello world" + "</div>";
        //t2.setText(question);
//        t1.setText( question);
//        t1.setText("<html><body  align='left'>You scored <b>\"\""+'{' + question + '}'+"</b> points.</body></html>");

        mathJaxWebView.setText(question.trim());
//        t3.setText(question.trim());



String a="<html><head><script Type='text/javascript' src='file:///android_asset/latexit.js'></script></head><body><span lang=\"\""+'{' + question + '}'+"\"\">\\int3x</span></body></html>\"";




//        mathJaxWebView =findViewById(R.id.mathJaxWebView);
        mathJaxWebView.getSettings().setJavaScriptEnabled(true);
        mathJaxWebView.setText(question.trim());
    }
}