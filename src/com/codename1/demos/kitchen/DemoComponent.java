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

import com.codename1.components.ScaleImageLabel;
import com.codename1.io.Preferences;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.animations.BubbleTransition;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.ComponentAnimation;
import com.codename1.ui.animations.FlipTransition;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;

/**
 * Represents a component within the demo grid that can appear as a card or as a list item
 *
 * @author Shai Almog
 */
public class DemoComponent extends Container {
    private Button button;
    private Label iconLabel;
    private Image icon;
    private Object cardMask;
    private int maskWidth;
    private int maskHeight;
    private Image cardBackground;
    private Object circleMask;
    private Image circle;
    private int circleWidth;
    private int circleHeight;
    private String currentColor;
    
    public DemoComponent(String title, Image icon, Object cardMask, int maskWidth, int maskHeight, Image cardBackground,
            Object circleMask, Image circle, int circleWidth, int circleHeight, String currentColor) {
        super(new BorderLayout());
        this.icon = icon;
        this.cardMask = cardMask;
        this.maskWidth = maskWidth;
        this.maskHeight = maskHeight;
        this.cardBackground = cardBackground;
        this.circleMask = circleMask;
        this.circle = circle;
        this.circleWidth = circleWidth;
        this.circleHeight = circleHeight;
        this.currentColor = currentColor;
        button = new Button(title);
        setLeadComponent(button);
        
        if(Preferences.get("gridLayout", true) && !isTablet()) {
            iconLabel = new Label(icon.fill(maskWidth, maskHeight).applyMask(cardMask));
            button.setUIID("CardBottom");
            iconLabel.setUIID("CardTop");
            Label backgroundLabel = new Label(cardBackground, "CardBottom");

            add(CENTER, iconLabel);
            add(SOUTH, 
                    LayeredLayout.encloseIn(backgroundLabel, button));
        } else {
            iconLabel = new Label(icon.fill(circleWidth, circleHeight).applyMask(circleMask), "CenterIcon");
            setUIID(currentColor);
            button.setUIID("DemoTitleText");
            add(WEST, LayeredLayout.encloseIn(iconLabel, new Label(circle, "CenterIcon")));
            add(CENTER, button);
        }
    }

    public ComponentAnimation lineToGridStage2() {
        Component iconLabelParent = iconLabel.getParent();
        iconLabel = new Label(icon.fill(maskWidth, maskHeight).applyMask(cardMask));
        iconLabel.setUIID("CardTop");
        setUIID("Container");
        ComponentAnimation cn2 = createReplaceTransition(iconLabelParent, iconLabel, new FlipTransition(-1, 500));
        cn2.addOnCompleteCall(() ->  ((BorderLayout)getLayout()).setCenterBehavior(CENTER_BEHAVIOR_SCALE));
        return cn2;
    }
    
    public ComponentAnimation lineToGridStage1() {
        Label backgroundLabel = new Label(cardBackground, "Label");
        int bx = button.getX();
        int by = button.getY();
        int bw = button.getWidth();
        int bh = button.getHeight();
        button.remove();
        Container ll = LayeredLayout.encloseIn(backgroundLabel, button);
        ll.setX(bx);
        ll.setY(by);
        ll.setWidth(bw);
        ll.setHeight(bh);
        Component iconLabelParent = iconLabel.getParent();
        iconLabelParent.setPreferredSize(new Dimension(maskWidth, maskHeight));
        removeAll();
        add(CENTER, iconLabelParent);
        add(SOUTH,ll);
        ((BorderLayout)getLayout()).setCenterBehavior(CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        ComponentAnimation cn1 = createAnimateLayout(1000);
        ComponentAnimation cn4 = createStyleAnimation("Container", 200);
        button.setUIID("CardBottom");
        backgroundLabel.setUIID("CardBottom");
                
        return ComponentAnimation.sequentialAnimation(cn1, cn4); 
    }
    
    public ComponentAnimation gridToLineStage1() {
        Label oldIconLabel = iconLabel;
        iconLabel = new Label(icon.fill(circleWidth, circleHeight).applyMask(circleMask), "CenterIcon");
        
        ComponentAnimation cn2 = createReplaceTransition(oldIconLabel, 
                LayeredLayout.encloseIn(iconLabel, new Label(circle, "CenterIcon")), new FlipTransition(-1, 500));
        return cn2;
    }
    
    public ComponentAnimation gridToLineStage2() {
        button.remove();
        removeAll();
        add(WEST, iconLabel.getParent());
        add(CENTER, button);
        ComponentAnimation cn2 = createStyleAnimation(currentColor, 1000);
        //setUIID(currentColor);
        button.setUIID("DemoTitleText");
                
        ComponentAnimation cn1 = createAnimateLayout(1000);
        
        return ComponentAnimation.compoundAnimation(cn1, cn2);
    }
    
    public String getText() {
        return button.getText();
    }
    
    public void addActionListener(ActionListener<ActionEvent> l) {
        button.addActionListener(l);
    }

    public void removeActionListener(ActionListener<ActionEvent> l) {
        button.removeActionListener(l);        
    }
}
