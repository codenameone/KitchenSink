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

import com.codename1.components.ScaleImageLabel;
import com.codename1.components.Switch;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import static com.codename1.ui.CN.*;
import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the Toolbar component.
 * Toolbar replaces the default title area with a powerful abstraction that allows functionality
 * ranging from side menus (hamburger) to title animations and any arbitrary component type.
 * Toolbar allows customizing the Form title with different commands on the title area, within the side menu or the overflow menu.
 *
 * @author Sergey Gerashenko.
 */
public class ToolbarDemo extends Demo{
    public ToolbarDemo(Form parentForm) {
        init("Toolbar", getGlobalResources().getImage("toolbar-demo.png"), parentForm,
                "https://github.com/sergeyCodenameOne/KitchenSinkDemo/blob/master/src/com/codename1/demos/kitchen/ToolbarDemo.java");
    }

    @Override
    public Container createContentPane() {
        Form toolBarForm = new Form("Toolbar", new FlowLayout(Component.CENTER));
        toolBarForm.getContentPane().setUIID("ComponentDemoContainer");
        Toolbar tb = toolBarForm.getToolbar();
        tb.setUIID("DemoToolbar");
        tb.getTitleComponent().setUIID("DemoTitle");

        Button searchButton = new Button("Show Searchbar", ("ToolbarDemoButton"));
        searchButton.addActionListener(e->{
            Display.getInstance().getCurrent().setToolbar(tb);
            tb.showSearchBar(ee->{
                String text = (String)ee.getSource();
                // Update the UI depending on the text.
            });
            Display.getInstance().getCurrent().revalidate();
        });

        // Toolbar add source and back buttons.
        Style commandStyle = UIManager.getInstance().getComponentStyle("DemoTitleCommand");

        Command sourceCommand = Command.create("", FontImage.create("{ }", commandStyle),
                e-> execute(getSourceCode()));

        tb.addCommandToRightBar(sourceCommand);
        if (isTablet()){
            tb.setPermanentSideMenu(true);
        }

        Form lastForm = Display.getInstance().getCurrent();
        Button homeButton = new Button("Home", FontImage.MATERIAL_HOME, ("ToolbarDemoButton"));
        homeButton.addActionListener(e-> lastForm.showBack());

        Button showSearchButton = new Button("Search", FontImage.MATERIAL_SEARCH, ("ToolbarDemoButton"));
        showSearchButton.addActionListener(e-> {
            Container contentPane = toolBarForm.getContentPane();
            contentPane.setLayout(new FlowLayout(Component.CENTER));
            contentPane.removeAll();
            contentPane.add(searchButton);
            if (!isTablet()){
                tb.closeSideMenu();
            }
            contentPane.revalidate();
        });

        Button settings = new Button("Settings", FontImage.MATERIAL_SETTINGS, ("ToolbarDemoButton"));
        settings.addActionListener(e-> {
            Container contentPane = toolBarForm.getContentPane();
            contentPane.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            contentPane.removeAll();
            Container settingsLabel = FlowLayout.encloseCenter(new Label("Settings", "DemoHeader"));
            Container wifi = BorderLayout.west(new Label("Wi-Fi", "DemoToolbarLabel")).add(BorderLayout.EAST, new Switch());
            Container mobileData = BorderLayout.west(new Label("Mobile data", "DemoToolbarLabel")).add(BorderLayout.EAST, new Switch());
            Container airplaneMode = BorderLayout.west(new Label("Airplane mode", "DemoToolbarLabel")).add(BorderLayout.EAST, new Switch());
            contentPane.addAll(settingsLabel, wifi, mobileData, airplaneMode);
            if (!isTablet()){
                tb.closeSideMenu();
            }
            contentPane.revalidate();
        });

        Button logoutButton = new Button("Logout", FontImage.MATERIAL_EXIT_TO_APP, ("ToolbarDemoButton"));
        logoutButton.addActionListener(e-> {
            if (!isTablet()){
                tb.closeSideMenu();
            }
            ToastBar.showInfoMessage("You have successfully logged out");
        });

        if (!isTablet()){
            ScaleImageLabel cn1Icon = new ScaleImageLabel(getGlobalResources().getImage("code-name-one-icon.png"));
            cn1Icon.setUIID("SideMenuIconDemo");
            int size = convertToPixels(20);
            cn1Icon.setPreferredH(size);
            cn1Icon.setPreferredW(size);
            Container sideMenuHeader = BoxLayout.encloseY(cn1Icon, new Label("Codename One", "SideMenuHeader"));
            tb.addComponentToSideMenu(sideMenuHeader);
        }

        tb.addComponentToSideMenu(homeButton);
        tb.addComponentToSideMenu(showSearchButton);
        tb.addComponentToSideMenu(settings);
        tb.addComponentToSideMenu(logoutButton);

        toolBarForm.add(searchButton);
        
        toolBarForm.show();
        return null;
    }
}
