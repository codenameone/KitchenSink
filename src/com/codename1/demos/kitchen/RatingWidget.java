/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */

package com.codename1.demos.kitchen;

import com.codename1.components.InteractionDialog;
import com.codename1.io.Preferences;
import com.codename1.io.Util;
import com.codename1.messaging.Message;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Slider;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;

/**
 * Follows the status of the user within the app, after a given amount of time pops up a non-intrusive dialog asking 
 * the user to rate the app. If rating is low asks the user to provide feedback, if the rating is high asks the user 
 * to rate in the appstore.
 *
 * @author Shai Almog
 */
public class RatingWidget {
    private static RatingWidget instance;
    private boolean running;
    
    private int timeForPrompt;
    
    private String appstoreUrl;
    private String supportEmail;
    
    private RatingWidget() {        
    }

    private void init(String appstoreUrl, String supportEmail) {
        this.appstoreUrl = appstoreUrl;
        this.supportEmail = supportEmail;
        running = true;
        Thread t = Display.getInstance().startThread(() -> checkTimerThread(), "Review thread");
        t.start();
    }
    
    void checkTimerThread() {
        while(running) {
            long lastTime = System.currentTimeMillis();
            int timeEllapsedInApp = Preferences.get("timeElapsedInApp", 0);
            Util.wait(this, timeForPrompt - timeEllapsedInApp);
            long total = System.currentTimeMillis() - lastTime;
            if(total + timeEllapsedInApp < timeForPrompt) {
                Preferences.set("timeElapsedInApp", (int)(total + timeEllapsedInApp));
            } else {
                Display.getInstance().callSerially(() -> showReviewWidget());
                running = false;
                instance  = null;
                return;
            }
        }
    }
    
    void showReviewWidget() {
        // block this from happening twice
        Preferences.set("alreadyRated", true);
        InteractionDialog id = new InteractionDialog("Please Rate "  + Display.getInstance().getProperty("AppName", "The App"));
        Form f = Display.getInstance().getCurrent();
        id.setLayout(new BorderLayout());
        Slider rate = createStarRankSlider();
        Button ok = new Button("OK");
        Button no = new Button("No Thanks");
        id.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(rate)).
                add(BorderLayout.SOUTH, GridLayout.encloseIn(2, no, ok));
        int height = id.getPreferredH();
        id.show(f.getHeight()  - height - f.getTitleArea().getHeight(), 0, 0, 0);
        no.addActionListener(e -> id.dispose());
        ok.addActionListener(e -> {
            id.dispose();
            if(rate.getProgress() >= 9) {
                if(Dialog.show("Rate On Store", "Would you mind rating us in the appstore?", "Go To Store", "Dismiss")) {
                    Display.getInstance().execute(appstoreUrl);
                }
            } else {
                if(Dialog.show("Tell Us Why?", "Would you mind writing us a short message explaining how we can improve?", "Write", "Dismiss")) {
                    Message m = new Message("Heres how you can improve  " + Display.getInstance().getProperty("AppName", "the app"));
                    Display.getInstance().sendMessage(new String[] {supportEmail}, "Improvement suggestions for " + Display.getInstance().getProperty("AppName", "your app"), m);
                }
            }
        });
    }

    private void initStarRankStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    private Slider createStarRankSlider() {
        Font fnt = Font.createTrueTypeFont("native:MainLight", "native:MainLight").
                derive(Display.getInstance().convertToPixels(5, true), Font.STYLE_PLAIN);
        Style s = new Style(0xffff33, 0, fnt, (byte)0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        s.setOpacity(100);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        Slider starRank = new Slider() {
            public void refreshTheme(boolean merge) {
                // special case when changing the theme while the dialog is showing
                initStarRankStyle(getSliderEmptySelectedStyle(), emptyStar);
                initStarRankStyle(getSliderEmptyUnselectedStyle(), emptyStar);
                initStarRankStyle(getSliderFullSelectedStyle(), fullStar);
                initStarRankStyle(getSliderFullUnselectedStyle(), fullStar);
            }
        };
        starRank.setEditable(true);
        starRank.setMinValue(0);
        starRank.setMaxValue(10);
        initStarRankStyle(starRank.getSliderEmptySelectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderEmptyUnselectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderFullSelectedStyle(), fullStar);
        initStarRankStyle(starRank.getSliderFullUnselectedStyle(), fullStar);
        starRank.setPreferredSize(new Dimension(fullStar.getWidth() * 5, fullStar.getHeight()));
        return starRank;
    }    
    
    /**
     * Binds the rating widget to the UI if the app wasn't rated yet
     * 
     * @param time time in milliseconds for the widget to appear
     * @param appstoreUrl the app URL in the store
     * @param supportEmail support email address if the rating is low
     */
    public static void bindRatingListener(int time, String appstoreUrl, String supportEmail) {
        if(Preferences.get("alreadyRated", false)) {
            return;
        }
        instance = new RatingWidget();
        instance.timeForPrompt = time;
        instance.init(appstoreUrl, supportEmail);
    }
    
    /**
     * This should be invoked by the stop() method as we don't want rating countdown to proceed when the app isn't
     * running
     */
    public static void suspendRating() {
        if(instance != null) {
            synchronized(instance) {
                instance.notify();
            }
            instance.running  = false;
            instance = null;
        }
    }
}
