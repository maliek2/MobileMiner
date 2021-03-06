package io.scalaproject.androidminer.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.scalaproject.androidminer.Config;

public final class ProviderManager {

    static private ArrayList<PoolItem> mPools = new ArrayList<PoolItem>();

    static public void add(PoolItem poolItem) {
        mPools.add(poolItem);
    }

    static public void add(String key, String pool,String port, int poolType, String poolUrl, String poolIP) {
        mPools.add(new PoolItem(key, pool, port, poolType, poolUrl, poolIP));
    }
    static public void add(String key, String pool, String port, int poolType, String poolUrl, String poolIP, String poolApi) {
        mPools.add(new PoolItem(key, pool, port, poolType, poolUrl, poolIP, poolApi, "",""));
    }

    static public PoolItem[] getPools() {
        return mPools.toArray(new PoolItem[mPools.size()]);
    }

    static public PoolItem getPoolById(int idx) {
        return mPools.get(idx);
    }

    static public PoolItem getPoolById(String idx) {
        int index = Integer.parseInt(idx);

        if (idx.equals("") || mPools.size() < index) {
            return null;
        }
        return mPools.get(index);
    }

    static final public ProviderData data = new ProviderData();

    static public PoolItem getSelectedPool() {
        if(request.mPoolItem != null) {
            return  request.mPoolItem;
        }

        String sp = Config.read("selected_pool");
        if (sp.equals("")) {
            return null;
        }

        PoolItem pi = getPoolById(sp);

        return pi;
    }
    static public void afterSave() {
        if(request.mPoolItem != null)  {
            return;
        }

        PoolItem pi = getSelectedPool();
        if(pi == null) {
            return;
        }
        mPools.clear();
        request.mPoolItem = pi;
        data.isNew = true;
        request.start();
    }

    static final public ProviderRequest request = new ProviderRequest();
    static public void generate() {
        request.stop();
        request.mPoolItem = null;

        //User Defined
        add("custom", "custom", "3333", 0, "", "");

        // Scala Official pool
        add(
                "Scalaproject.io (Official Pool)",
                "mine.scalaproject.io",
                "3333",
                1,
                "https://pool.scalaproject.io",
                "198.204.241.13"
        );

        // Another Scala pool : Miner.Rocks
        add(
                "Miner.Rocks",
                "stellite.miner.rocks",
                "5003",
                2,
                "https://stellite.miner.rocks",
                "54.38.232.67"
        );

        // Another Scala pool : HeroMiners
        add(
                "HeroMiners",
                "scala.herominers.com",
                "10130",
                2,
                "https://scala.herominers.com",
                "138.201.217.40"
        );

        // Another Scala pool : GNTL
        add(
                "GNTL",
                "xla.pool.gntl.co.uk",
                "3333",
                1,
                "https://xla.pool.gntl.co.uk",
                "83.151.238.38"
        );
    }
}
