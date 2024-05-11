/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jinkeloid.yapdcn.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


public class AndroidGame extends Activity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException (Thread thread, Throwable e) {
                handleUncaughtException (thread, e);
            }
        });

        //there are some things we only need to set up on first launch
        if (instance == null) {

            instance = this;

            try {
                Game.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Game.version = "???";
            }
            try {
                Game.versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                Game.versionCode = 0;
            }

            if (UpdateImpl.supportsUpdates()) {
                Updates.service = UpdateImpl.getUpdateService();
            }
            if (NewsImpl.supportsNews()) {
                News.service = NewsImpl.getNewsService();
            }

            FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

            // grab preferences directly using our instance first
            // so that we don't need to rely on Gdx.app, which isn't initialized yet.
            // Note that we use a different prefs name on android for legacy purposes,
            // this is the default prefs filename given to an android app (.xml is automatically added to it)
            MISPDSettings.set(instance.getPreferences("MISPD"));

        } else {
            instance = this;
        }

        //set desired orientation (if it exists) before initializing the app.
        if (MISPDSettings.landscape() != null) {
            instance.setRequestedOrientation( MISPDSettings.landscape() ?
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.depth = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //use rgb888 on more modern devices for better visuals
            config.r = config.g = config.b = 8;
        } else {
            //and rgb565 (default) on older ones for better performance
        }

        config.useCompass = false;
        config.useAccelerometer = false;

        if (support == null) support = new AndroidPlatformSupport();
        else                 support.reloadGenerators();

        support.updateSystemUI();

        initialize(new MusicImplantSPD(support), config);
    }
}