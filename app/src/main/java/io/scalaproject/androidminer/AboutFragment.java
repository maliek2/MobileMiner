// Copyright (c) 2020, Scala Project
//
// Please see the included LICENSE file for more information.
package io.scalaproject.androidminer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class AboutFragment extends Fragment {

    private Button bDonate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false  );

        TextView tvSystemInfo, tvScala, tvMine2gether, tvMonerominer, tvFontAwesome;

        tvSystemInfo = view.findViewById(R.id.systemInfo);
        tvScala = view.findViewById(R.id.ScalaURL);
        tvMine2gether = view.findViewById(R.id.Mine2getherURL);
        tvMonerominer = view.findViewById(R.id.MoneroMinerURL);
        tvFontAwesome = view.findViewById(R.id.FontAwesomeURL);

        Button btnGitHub = view.findViewById(R.id.btnGitHub);
        btnGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.githubLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnDiscord = view.findViewById(R.id.btnDiscord);
        btnDiscord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.discordLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnMedium = view.findViewById(R.id.btnMedium);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.mediumLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnTwitter = view.findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.twitterLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnTelegram = view.findViewById(R.id.btnTelegram);
        btnTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.telegramLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        String cpuinfo = Config.read("CPUINFO");
        if(cpuinfo.isEmpty()) {
            try {
                Map<String, String> m = Tools.getCPUInfo();

                cpuinfo = "ABI: " + Tools.getABI() + "\n";
                for (Map.Entry<String, String> pair : m.entrySet()) {
                    cpuinfo += pair.getKey() + ": " + pair.getValue() + "\n";
                }

            } catch (Exception e) {
                cpuinfo = "";
            }

            Config.write("CPUINFO", cpuinfo);
        }

        tvSystemInfo.setText(cpuinfo);

        tvScala.setText(Html.fromHtml(getString(R.string.ScalaLink)));
        tvScala.setMovementMethod(LinkMovementMethod.getInstance());

        tvMine2gether.setText(Html.fromHtml(getString(R.string.Mine2getherLink)));
        tvMine2gether.setMovementMethod(LinkMovementMethod.getInstance());

        tvMonerominer.setText(Html.fromHtml(getString(R.string.MoneroMinerLink)));
        tvMonerominer.setMovementMethod(LinkMovementMethod.getInstance());

        tvFontAwesome.setText(Html.fromHtml(getString(R.string.FontAwesomeLink)));
        tvFontAwesome.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}