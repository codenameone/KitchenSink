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

package com.codename1.demos.kitchen.charts.models;

import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.components.Accordion;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

/**
 * Allows editing a multiple series set
 *
 * @author Shai Almog
 */
public class XYMultipleSeriesEditor extends Container {
    private XYMultipleSeriesDataset xy;
    public XYMultipleSeriesEditor() {
        super(new BorderLayout());
    }
    
    public void init(XYMultipleSeriesDataset xy) {
        Accordion acc = new Accordion();
        add(BorderLayout.CENTER, acc);
        this.xy = xy;
        Style s = UIManager.getInstance().getComponentStyle("Button");
        s.setFgColor(0xff0000);
        FontImage removeImage = FontImage.createMaterial(FontImage.MATERIAL_DELETE, s, 3.5f);
        FontImage addImage = FontImage.createMaterial(FontImage.MATERIAL_ADD, s, 3.5f);
        for(XYSeries xx : xy.getSeries()) {
            addSeries(xx, acc, removeImage);
        }
        Button add = new Button(addImage);
        add(BorderLayout.SOUTH, add);
        add.addActionListener(e -> {
            XYSeries x  = new XYSeries("New Series");
            addSeries(x, acc, removeImage);
            acc.animateLayout(200);
        });
    }

    private void addSeries(XYSeries xx, Accordion acc, FontImage removeImage) {
        XYSeriesEditor edit = new XYSeriesEditor();
        edit.init(xx, title -> acc.setHeader(title, edit));
        edit.setScrollable(false);
        Button remove = new Button(removeImage);
        remove.addActionListener((e) -> {
            xy.removeSeries(xx);
            acc.removeContent(edit);
            acc.animateLayout(200);
        });
        acc.addContent(xx.getTitle(), edit);
    }
}
