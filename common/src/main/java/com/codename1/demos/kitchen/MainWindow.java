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

import com.codename1.components.MultiButton;
import com.codename1.demos.kitchen.util.Util;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import static com.codename1.ui.CN.*;

public class MainWindow {
       
    public Form buildForm(){
        Form mainWindow = new Form("Components", new GridLayout(7, 2, 7, 3));
        Container contentPane = mainWindow.getContentPane();
        contentPane.setUIID("MainWindowContainer");
        contentPane.setScrollableY(true);
        Toolbar tb = mainWindow.getToolbar();
        tb.setUIID("MainWindowToolbar");
        tb.getTitleComponent().setUIID("MainWindowTitle");

        Demo[] demos = {
                new LabelsDemo(mainWindow),
                new ButtonsDemo(mainWindow),
                new TogglesDemo(mainWindow),
                new TextInputDemo(mainWindow),
                new SelectionDemo(mainWindow),
                new ContainersDemo(mainWindow),
                new DialogDemo(mainWindow),
                new ProgressDemo(mainWindow),
                new ToolbarDemo(mainWindow),
                new ChartsDemo(mainWindow),
                new AdvancedDemo(mainWindow),
                new MediaDemo(mainWindow),
                new MapsDemo(mainWindow),
                new ClockDemo(mainWindow)
        };
        
        if (isTablet()){
            mainWindow.setLayout(new GridLayout(5, 3));
        }

        for(Demo demo : demos){
            Component demoComponent = createDemoComponent(demo);
            mainWindow.add(demoComponent);
        }
        return mainWindow;
    }
    
    private Component createDemoComponent(Demo demo){
        MultiButton demoComponent = new MultiButton(demo.getDemoId());
        demoComponent.setUIID("MainWindowDemoComponent");
        demoComponent.setIcon(demo.getDemoImage().fill(Util.getDemoImageWidth(), Util.getDemoImageHeight()));
        demoComponent.setIconPosition(BorderLayout.NORTH);
        demoComponent.addActionListener(e-> createAndShowForm(demo));
        demoComponent.setIconUIID("DemoComponentIcon");
        demoComponent.setUIIDLine1("MainWindowDemoName");
        return demoComponent;
    }
    
    private void createAndShowForm(Demo demo){
        Container demoContent = demo.createContentPane();
        if (demoContent == null){
            return;
        }
        Form demoForm = new Form(demo.getDemoId(), new BorderLayout());
        Toolbar toolbar = demoForm.getToolbar();
        toolbar.setUIID("DemoToolbar");
        toolbar.getTitleComponent().setUIID("DemoTitle");
        
        // Toolbar add source and back buttons.
        Style commandStyle = UIManager.getInstance().getComponentStyle("TitleCommand");
        Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, commandStyle),
                e-> demo.getParentForm().showBack());

        Command sourceCommand = Command.create("", FontImage.create("{ }", UIManager.getInstance().getComponentStyle("TitleCommand")),
                e-> execute(demo.getSourceCode()));

        toolbar.addCommandToRightBar(sourceCommand);
        toolbar.setBackCommand(backCommand);
        if(isTablet()){
            Demo.adjustToTablet(demoContent);
        }
        // Change the UIID of the source Button.
        toolbar.getComponentAt(1).setUIID("SourceCommand");

        demoForm.add(BorderLayout.CENTER, demoContent);
        demoForm.show();
    }
}