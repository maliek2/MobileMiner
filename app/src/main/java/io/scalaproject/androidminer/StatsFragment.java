// Copyright (c) 2020, Scala Project
//
// Please see the included LICENSE file for more information.

package io.scalaproject.androidminer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import io.scalaproject.androidminer.api.ProviderData;
import io.scalaproject.androidminer.api.PoolItem;
import io.scalaproject.androidminer.api.IProviderListener;
import io.scalaproject.androidminer.api.ProviderManager;

public class StatsFragment extends Fragment {

    private static final String LOG_TAG = "MiningSvc";

    private Button bStatCheckOnline;

    protected IProviderListener statsListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        bStatCheckOnline = view.findViewById(R.id.checkstatsonline);

        statsListener = new IProviderListener(){
            public void onStatsChange(ProviderData d) {
                updateFields(d, view);
            }

            @Override
            public boolean onEnabledRequest() {
                return checkValidState();
            }
        };

        ProviderManager.request.setListener(statsListener).start();
        ProviderManager.afterSave();
        updateFields(ProviderManager.data, view);

        return view;
    }

    private void updateFields(ProviderData d, View view) {
        if(d.isNew) {
            enableOnlineStats(false);
            return;
        }

        PoolItem pm = ProviderManager.getSelectedPool();

        // Network

        TextView tvNetworkHashrate = view.findViewById(R.id.hashratenetwork);
        tvNetworkHashrate.setText(d.network.hashrate);

        TextView tvNetworkDifficulty = view.findViewById(R.id.difficultypool);
        tvNetworkDifficulty.setText(d.network.difficulty);

        TextView tvNetworkBlocks = view.findViewById(R.id.lastblocknetwork);
        tvNetworkBlocks.setText(d.network.lastBlockTime);

        TextView tvNetworkHeight = view.findViewById(R.id.height);
        tvNetworkHeight.setText(d.network.lastBlockHeight);

        TextView tvNetworkRewards = view.findViewById(R.id.rewards);
        tvNetworkRewards.setText(d.network.lastRewardAmount);

        // Pool

        TextView tvPoolURL = view.findViewById(R.id.poolurl);
        tvPoolURL.setText(pm.getPool());

        TextView tvPoolHashrate = view.findViewById(R.id.hashratepool);
        tvPoolHashrate.setText(d.pool.hashrate);

        TextView tvPoolDifficulty = view.findViewById(R.id.difficultypool);
        tvPoolDifficulty.setText(d.pool.difficulty);

        TextView tvPoolBlocks = view.findViewById(R.id.lastblockpool);
        tvPoolBlocks.setText(d.pool.lastBlockTime);

        TextView tvPoolLastBlock = view.findViewById(R.id.blockspool);
        tvPoolLastBlock.setText(d.pool.blocks);

        // Address

        String wallet = Config.read("address");
        String prettyaddress = wallet.substring(0, 7) + "..." + wallet.substring(wallet.length() - 7);

        TextView tvWalletAddress = view.findViewById(R.id.walletaddress);
        tvWalletAddress.setText(prettyaddress);

        String sHashrate = d.miner.hashrate;
        if(sHashrate != null) {
            sHashrate = sHashrate.replace("H", "").trim();
            TextView tvAddressHashrate = view.findViewById(R.id.hashrateminer);
            tvAddressHashrate.setText(sHashrate);

            TextView tvAddressLastShare = view.findViewById(R.id.lastshareminer);
            tvAddressLastShare.setText(d.miner.lastShare);

            TextView tvAddressBlocks = view.findViewById(R.id.blocksminedminer);
            tvAddressBlocks.setText(d.miner.blocks);

            String sBalance = d.miner.balance.replace("XLA", "").trim();
            TextView tvBalance = view.findViewById(R.id.balance);
            tvBalance.setText(sBalance);

            String sPaid = d.miner.paid.replace("XLA", "").trim();
            TextView tvPaid = view.findViewById(R.id.paid);
            tvPaid.setText(sPaid);
        }

        enableOnlineStats(true);

        String statsUrlWallet = pm.getStatsURL() + "?wallet=" + wallet;
        bStatCheckOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(statsUrlWallet);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void enableOnlineStats(boolean enable) {
        Drawable buttonDrawable = bStatCheckOnline.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);

        bStatCheckOnline.setEnabled(enable);

        if (enable) {
            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.bg_lighter));
            bStatCheckOnline.setBackground(buttonDrawable);
            bStatCheckOnline.setTextColor(getResources().getColor(R.color.c_white));
        }
        else {
            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.bg_black));
            bStatCheckOnline.setBackground(buttonDrawable);
            bStatCheckOnline.setTextColor(getResources().getColor(R.color.c_black));
        }
    }

    private boolean checkValidState() {
        if(Config.read("address").equals("")) {
            Toast.makeText(getContext(),"Wallet address is empty", Toast.LENGTH_LONG);
            enableOnlineStats(false);
            return false;
        }

        PoolItem pi = ProviderManager.getSelectedPool();

        if (Config.read("init").equals("1") == false || pi == null) {
            Toast.makeText(getContext(),"Start mining to view statistics", Toast.LENGTH_LONG);
            enableOnlineStats(false);
            return false;
        }

        if (pi.getPoolType() == 0) {
            Toast.makeText(getContext(),"Statistics are not available for custom pools", Toast.LENGTH_LONG);
            enableOnlineStats(false);
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ProviderManager.request.setListener(statsListener).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableOnlineStats(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        enableOnlineStats(false);
    }
}