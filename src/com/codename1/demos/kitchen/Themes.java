/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
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

import com.codename1.components.ScaleImageButton;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.util.FailureCallback;
import com.codename1.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 *
 * @author Shai Almog
 */
public class Themes  extends Demo {
    private static final String[] THEME_FILES = {
        null,
        "BusinessTheme.res",    
        "FlatBlueTheme.res",
        "FlatOrangeTheme.res",
        "FlatRedTheme.res",
        "Leather.res",
        "SocialBoo.res",
        "chrome.res"
    };

    private static final String[] THEME_NAMES = {
        "Native (Default)",
        "Business",    
        "Flat Blue",
        "Flat Orange",
        "Flat Red",
        "Leather",
        "Social Boo",
        "Chrome"
    };
    
    private static final String BASE_URL = "https://www.codenameone.com/files/Themes/";
    
    public String getDisplayName() {
        return "Themes";
    }

    public Image getDemoIcon() {
        return getResources().getImage("themes.png");
    }

    public void setTheme(Form oldForm, String fileName) {
        try(InputStream is = Storage.getInstance().createInputStream(fileName)) {
            Resources res = Resources.open(is);
            UIManager.getInstance().setThemeProps(res.getTheme(res.getThemeResourceNames()[0]));
            Hashtable builtin = getResources().getTheme("Theme");
            Hashtable h = new Hashtable(builtin);
            h.remove("@includeNativeBool");
            UIManager.getInstance().addThemeProps(h);
            Form c = Display.getInstance().getCurrent();
            c.refreshTheme();
            oldForm.refreshTheme();
            oldForm.revalidate();
        } catch(IOException err) {
            Log.e(err);
            ToastBar.showErrorMessage("Error loading theme file: " + fileName);
        }
    }
    
    public Container createDemo() {
        final Form parentForm = Display.getInstance().getCurrent();
        
        Container themes = new Container(BoxLayout.y());
        
        Image nativeThemeImage = getResources().getImage("nativeTheme.jpg");
        EncodedImage blurredPlaceholder;
        if(Display.getInstance().isGaussianBlurSupported()) {
            blurredPlaceholder = EncodedImage.createFromImage(Display.getInstance().gaussianBlurImage(nativeThemeImage, 20), true);
        } else {
            blurredPlaceholder = EncodedImage.createFromImage(nativeThemeImage.modifyAlpha((byte)80), true);
        }
        for(int iter = 0 ; iter < THEME_FILES.length ; iter++) {
            String currentThemeFile = THEME_FILES[iter];
            String currentThemeName = THEME_NAMES[iter];
            ScaleImageButton theme = new ScaleImageButton();
            
            if(currentThemeFile == null) {
                theme.setIcon(nativeThemeImage);
            } else {
                String imageFile = StringUtil.replaceAll(currentThemeFile, ".res", ".jpg");
                theme.setIcon(URLImage.createToStorage(blurredPlaceholder, imageFile, BASE_URL + imageFile));
            }
            
            theme.addActionListener(e -> {
                if(currentThemeFile == null) {
                    UIManager.initFirstTheme("/theme");
                    Form c = Display.getInstance().getCurrent();
                    c.refreshTheme();
                    parentForm.refreshTheme();
                    parentForm.revalidate();
                    return;
                }
                // we are in the middle of downloading this theme...
                if(theme.getClientProperty("downloading") != null) {
                    return;
                }
                if(Storage.getInstance().exists(currentThemeFile)) {
                    setTheme(parentForm, currentThemeFile);
                } else {
                    theme.putClientProperty("downloading", Boolean.TRUE);
                    ConnectionRequest cr = new ConnectionRequest(BASE_URL + currentThemeFile);
                    cr.setDestinationStorage(currentThemeFile);
                    ToastBar.showConnectionProgress("Downloading theme", cr, ee -> {
                        setTheme(parentForm, currentThemeFile);
                        theme.putClientProperty("downloading", null);
                    }, (sender, err, errorCode, errorMessage) -> {
                        ToastBar.showErrorMessage("There was an error downloading the file: " + err);
                        Log.e(err);
                    });
                    cr.setFailSilently(true);
                    NetworkManager.getInstance().addToQueue(cr);
                }
            });
            
            Container cnt = LayeredLayout.encloseIn(theme, 
                    BorderLayout.south(new Label(currentThemeName, "TintOverlay")));
            cnt.setLeadComponent(theme);
            themes.add(cnt);
        }
        
        themes.setScrollableY(true);
        
        return themes;
    }
}

