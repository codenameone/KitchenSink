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

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;

/**
 * Demonstrates some of the basic layout types available in Codename One with explanation and a smooth animation
 *
 * @author Shai Almog
 */
public class Layouts  extends Demo {
    public String getDisplayName() {
        return "Layouts";
    }

    public Image getDemoIcon() {
        return getResources().getImage("layout.png");
    }

    private void resetMargin(Container c) {
        for(Component cc : c) {
            cc.setUIID(cc.getUIID());
        }
    }
    
    public Container createDemo() {
        Button borderLayout = new Button("Border");
        Button borderAbsoluteLayout = new Button("Border - Absolute");
        Button boxYLayout = new Button("Box Y");
        Button boxXLayout = new Button("Box X");
        Button flowLayout = new Button("Flow");
        Button flowCenterLayout = new Button("Flow Center");
        Button gridLayout = new Button("Grid");
        Button tableLayout = new Button("Table");
        Button layeredLayout = new Button("Layered");
        Container selection = BoxLayout.encloseY(flowLayout, flowCenterLayout, borderLayout, borderAbsoluteLayout, boxYLayout, boxXLayout, gridLayout, tableLayout, layeredLayout);
        selection.setScrollableY(true);
        
        Component[] cmps = new Component[] {
            new Label("          ", "Blank1"),
            new Label("          ", "Blank2"),
            new Label("          ", "Blank3"),
            new Label("          ", "Blank4"),
            new Label("          ", "Blank5"),
            new Label("          ", "Blank6"),
            new Label("          ", "Blank7")
        };
        Container layouts = FlowLayout.encloseIn(cmps);
        
        borderLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new BorderLayout());
            
            // need to re-add the components since the layout requires a contraint
            layouts.removeAll();
            layouts.add(BorderLayout.CENTER, cmps[0]).
                    add(BorderLayout.EAST, cmps[1]).
                    add(BorderLayout.WEST, cmps[2]).
                    add(BorderLayout.NORTH, cmps[3]).
                    add(BorderLayout.SOUTH, cmps[4]);
            
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        borderAbsoluteLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));
            
            // need to re-add the components since the layout requires a contraint
            layouts.removeAll();
            layouts.add(BorderLayout.CENTER, cmps[0]).
                    add(BorderLayout.EAST, cmps[1]).
                    add(BorderLayout.WEST, cmps[2]).
                    add(BorderLayout.NORTH, cmps[3]).
                    add(BorderLayout.SOUTH, cmps[4]);
            
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        boxYLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        boxXLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new BoxLayout(BoxLayout.X_AXIS));
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        flowLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new FlowLayout());
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        flowCenterLayout.addActionListener(e -> {
            resetMargin(layouts);
            layouts.setLayout(new FlowLayout(Component.CENTER));
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        gridLayout.addActionListener(e -> {
            resetMargin(layouts);
            GridLayout g = new GridLayout(1, 1);
            g.setAutoFit(true);
            layouts.setLayout(g);
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        tableLayout.addActionListener(e -> {
            resetMargin(layouts);
            TableLayout tl = new TableLayout(2, 4);
            layouts.setLayout(tl);
            
            // need to re-add the components since the layout requires a contraint
            layouts.removeAll();
            layouts.add(cmps[0]).
                    add(cmps[1]).
                    add(cmps[2]).
                    add(cmps[3]).
                    add(tl.createConstraint().horizontalSpan(2), cmps[4]).
                    add(cmps[5]).
                    add(cmps[6]);
            
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        
        layeredLayout.addActionListener(e -> {
            int margin = 0;
            for(Component c : layouts) {
                c.getUnselectedStyle().setMargin(margin, margin, margin, margin);
                c.getUnselectedStyle().setMarginUnit(Style.UNIT_TYPE_DIPS);
                margin += 2;
            }
            
            layouts.setLayout(new LayeredLayout());
            if(!layouts.contains(cmps[5])) {
                layouts.add(cmps[5]).add(cmps[6]);
            }
            layouts.setShouldCalcPreferredSize(true);
            layouts.animateLayout(800);
        });
        
        return GridLayout.encloseIn(1, layouts, selection);
    }

    @Override
    public String getDescription() {
        return "Layouts allow the UI of Codename One to adapt to the different resolutions and DPI's supported by the various OS's. This is just the tip of the iceberg. Layouts can be nested deeply and there are very complex layouts such as MiG, Group, GridBag etc. that aren't fully represented here...";
    }
    
    
}
