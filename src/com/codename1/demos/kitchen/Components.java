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

import com.codename1.components.OnOffSwitch;
import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.JSONParser;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.DateTimeSpinner;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.Table;
import com.codename1.ui.tree.Tree;
import com.codename1.ui.tree.TreeModel;
import com.codename1.xml.Element;
import com.codename1.xml.XMLParser;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Small gallery of some choice Codename One components
 *
 * @author Shai Almog
 */
public class Components  extends Demo {
    private static final String DEFAULT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<tree>\n" +
                    "	<node>Node</node>\n" +
                    "	<node>Something</node>\n" +
                    "	<somethingelse>Else</somethingelse>\n" +
                    "	<sub><node>Within...</node></sub>\n" +
                    "</tree>";
    
    
    public String getDisplayName() {
        return "Components";
    }

    public Image getDemoIcon() {
        return getResources().getImage("applications-engineering.png");
    }

    private Container createPickerDemo() {
        Picker datePicker = new Picker();
        datePicker.setType(Display.PICKER_TYPE_DATE);
        Picker dateTimePicker = new Picker();
        dateTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        Picker timePicker = new Picker();
        timePicker.setType(Display.PICKER_TYPE_TIME);
        Picker stringPicker = new Picker();
        stringPicker.setType(Display.PICKER_TYPE_STRINGS);

        datePicker.setDate(new Date());
        dateTimePicker.setDate(new Date());
        timePicker.setTime(10 * 60); // 10:00AM = Minutes since midnight
        stringPicker.setStrings("First String", "Second String", "Third String");
        stringPicker.setSelectedString("First String");

        return BoxLayout.encloseY(datePicker, dateTimePicker, timePicker, stringPicker);
    }
    
    private void updateTree(String xml, Tree t, boolean json) {
        try (Reader r = new CharArrayReader(xml.toCharArray())) {
            TreeModel mdl;
            if(json) {
                JSONParser jp = new JSONParser();
                Map<String, Object> result = jp.parseJSON(r);
                if(result == null) {
                    KSUtil.showErrorMessage("Parsing failed");
                    return;
                }
                mdl = new JSONTreeModel(result);
            } else {
                XMLParser xp = new XMLParser();
                Element em = xp.parse(r);
                if(em == null) {
                    KSUtil.showErrorMessage("Parsing failed");
                    return;
                }
                mdl = new XMLTreeModel(em);
            }
            t.setModel(mdl);
            t.revalidate();
        } catch(Exception err) {
            KSUtil.showErrorMessage("Parsing error: " + err);
        }
    }
    
    private Container createTreeDemo() {
        TextArea ta = new TextArea(DEFAULT_XML);
        
        OnOffSwitch treeMode = new OnOffSwitch();
        treeMode.setOn("XML");
        treeMode.setOff("JSON");
        treeMode.setValue(true);
        
        Tree dt = new Tree() {
            protected String childToDisplayLabel(Object child) {
                if(child instanceof Element) {
                    Element e = (Element)child;
                    if(e.isTextElement()) {
                        return e.getText();
                    }
                    return e.getTagName();
                }
                if(child instanceof Map) {
                    return "Map";
                }
                if(child instanceof Collection) {
                    return "Array";
                }
                return child.toString();
            }
        };
        updateTree(ta.getText(), dt, false);
        ta.addActionListener(e -> {
            updateTree(ta.getText(), dt, !treeMode.isValue());
        });
        
        return BorderLayout.center(
                GridLayout.encloseIn(1, ta, dt)).
                add(BorderLayout.NORTH, 
                        GridLayout.encloseIn(2, new Label("XML/JSON"), treeMode));
    }
    private Container createTableDemo() {
        Container tableContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Table dt = new Table();
        tableContainer.addComponent(dt);
        return tableContainer;
    }
    
    public Container createDemo() {
        Tabs t = new Tabs();
        Style tabStyle = t.getUIManager().getComponentStyle("Tab");
        FontImage tree = FontImage.createMaterial(FontImage.MATERIAL_FOLDER, tabStyle, 3);
        FontImage picker = FontImage.createMaterial(FontImage.MATERIAL_LIST, tabStyle, 3);
        FontImage table = FontImage.createMaterial(FontImage.MATERIAL_GRID_ON, tabStyle, 3);
        
        t.addTab("Tree", tree, createTreeDemo());
        t.addTab("Picker", picker, createPickerDemo());
        t.addTab("Table", table, createTableDemo());
        return t;
    }
    
}
