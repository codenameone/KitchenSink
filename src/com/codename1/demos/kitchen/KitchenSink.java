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

import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import java.io.IOException;
import java.util.Hashtable;

import static com.codename1.ui.Button.setButtonRippleEffectDefault;
import static com.codename1.ui.CN.*;

public class KitchenSink {
    
    private Form current;
    private Resources theme;
    private boolean darkMode = false;
    private Command darkModeCommand;
    private Image darkModeImageDark;
    private Image darkModeImageLight;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        try {
            theme = Resources.openLayered("/theme");
            setPopupDialogBorder(theme.getTheme(theme.getThemeResourceNames()[0]));
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            Resources.setGlobalResources(theme);
        }catch(IOException e){
            Log.e(e);
        }

        darkModeImageLight = FontImage.createMaterial(FontImage.MATERIAL_BRIGHTNESS_MEDIUM, UIManager.getInstance().getComponentStyle("DemoTitleCommand"));
        darkModeImageDark = FontImage.createMaterial(FontImage.MATERIAL_BRIGHTNESS_MEDIUM, UIManager.getInstance().getComponentStyle("DemoTitleCommandDark"));

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);
        
        setButtonRippleEffectDefault(false);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        Form mainForm =  new MainWindow().buildForm();
        darkModeCommand = mainForm.getToolbar().addCommandToRightBar("",
                darkModeImageLight,
                e-> initTheme());
        mainForm.show();
    }
    
    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
        
    }
    
    private void initTheme() {
        darkMode = !darkMode;
        String themeFileName = darkMode ? "/dark-theme" : "/theme";
        try {
            Resources theme = Resources.openLayered(themeFileName);
            UIManager.getInstance().addThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
        }catch(IOException e){
            Log.e(e);
        }
        Button darkModeCmd = Display.getInstance().getCurrent().getToolbar().findCommandComponent(darkModeCommand);
        if(darkMode){
            darkModeCmd.setIcon(darkModeImageDark);
        }else{
            darkModeCmd.setIcon(darkModeImageLight);
        }

        ClockDemo.refreshClockColor();
        Display.getInstance().getCurrent().refreshTheme();
    }
    
    private void setPopupDialogBorder(Hashtable themeProps){
        themeProps.put("PopupDialog.derive", "Dialog");
        themeProps.put("PopupDialog.border", RoundRectBorder.create().
                cornerRadius(2f).
                shadowOpacity(60).shadowSpread(3.0f));
        themeProps.put("PopupDialog.transparency", "255");
        themeProps.put("PopupDialog.padding", "4,4,4,4");
        themeProps.put("PopupDialog.padUnit", new byte[]{Style.UNIT_TYPE_DIPS, Style.UNIT_TYPE_DIPS, Style.UNIT_TYPE_DIPS, Style.UNIT_TYPE_DIPS});
    }
}


