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
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate a simple usage of the Label, SpanLabel, and ScaleImageLabel components.
 * The Label are one of the most basic Components of Codename One that allow to display text/image on the form.
 *
 * @author Sergey Gerashenko.
 */
public class LabelsDemo extends Demo {
    
    public LabelsDemo(Form parentForm) {
        init("Labels", getGlobalResources().getImage("demo-labels.png"), parentForm,
                "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/LabelsDemo.java");
    }
     
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);
        
        demoContainer.add(createComponent(getGlobalResources().getImage("label.png"),
                                                                "Label",
                                                                "Allows displaying a single line of text and",
                                                                "icon (both optional) with different alignment options. This class is a base class for several "+ 
                                                                "components allowing them to declare alignment/icon appearance universally.", e->{                                                       
                                                                    showDemo("Label", createLabelDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("span-label.png"),
                                                                "Span Label",
                                                                "A multi line label component that can be",
                                                                "easily localized, this is simply based on a text area combined with a label.", e->{
                                                                   
                                                                    showDemo("SpanLabel", createSpanLabelDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("scale-image-label.png"),
                                                                "Scale Image Label",
                                                                "Label that simplifies the usage of scale to",
                                                                "fill/fit. This is effectively equivalent to just setting "+
                                                                "the style image on a label but more convenient for some special circumstances\n\nOne major difference is "+
                                                                "that preferred size equals the image in this case. The default UIID for this component is label", e->{
                                                                    showDemo("Scale image label", createScaleImageLabelDemo());
                                                                }));
        
        return demoContainer;
    }
    
    private Container createLabelDemo(){
        Container cnt1 = BoxLayout.encloseY(new Label("Text Label:", "DemoHeader"),
                new Label("label", "DemoLabel"));
        cnt1.setUIID("LabelContainer");

        Container cnt2 = BoxLayout.encloseY(new Label("Image Label:", "DemoHeader"),
                new Label(getGlobalResources().getImage("code-name-one-icon.png"), "DemoLabel"));
        cnt2.setUIID("LabelContainer");

        Container cnt3 = BoxLayout.encloseY(new Label("text and image Label:", "DemoHeader"),
                new Label("label", getGlobalResources().getImage("code-name-one-icon.png"), "DemoLabel"));
        cnt3.setUIID("LabelContainer");

        return BoxLayout.encloseY(cnt1, cnt2, cnt3);
    }
    
    private Container createSpanLabelDemo(){
        Container cnt1 = BoxLayout.encloseY(new Label("SpanLabel:", "DemoHeader"), 
                new SpanLabel("A multi line label component that can be easily localized, this is simply based on a text area combined with a label.", "DemoLabel"));
        cnt1.setUIID("LabelContainer");
        
        SpanLabel labelWithIconWest = new SpanLabel("A multi line label component that can be easily localized, this is simply based on a text area combined with a label.", "DemoLabel");
        labelWithIconWest.setMaterialIcon(FontImage.MATERIAL_INFO);
        labelWithIconWest.setIconUIID("DemoLabel");
        labelWithIconWest.setIconPosition("West");
        Container cnt2 = BoxLayout.encloseY(new Label("SpanLabel with icon (West):", "DemoHeader"), labelWithIconWest);
        cnt2.setUIID("LabelContainer");        
        
        SpanLabel labelWithIconNorth = new SpanLabel("A multi line label component that can be easily localized, this is simply based on a text area combined with a label.", "DemoLabel");
        labelWithIconNorth.setMaterialIcon(FontImage.MATERIAL_INFO);
        labelWithIconNorth.setIconUIID("DemoLabel");
        labelWithIconNorth.setIconPosition("North");
        Container cnt3 = BoxLayout.encloseY(new Label("SpanLabel with icon (North):", "DemoHeader"), labelWithIconNorth);
        cnt3.setUIID("LabelContainer");
        
        return BoxLayout.encloseY(cnt1, cnt2, cnt3);
    }
    
    private Container createScaleImageLabelDemo(){
        Container labelContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        labelContainer.add(new Label("Scale image label:", "DemoLabel"));
        ScaleImageLabel imageLabel = new ScaleImageLabel(getGlobalResources().getImage("scale-image-label.png")){
            @Override
            protected Dimension calcPreferredSize(){
                Dimension d = super.calcPreferredSize();
                d.setHeight(Display.getInstance().getDisplayHeight() / 7);
                return d;
            }
        };
        imageLabel.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
        labelContainer.add(imageLabel);
        labelContainer.add(new Label("   "));
        labelContainer.add(new Label("3 Scale image labels", "DemoLabel"));
        labelContainer.add(new Label("(auto scaled to fit available space)", "GreyLabel"));
        
        Container threeImagesContainer = new Container(new GridLayout(1, 3));
        ScaleImageLabel label1 = new ScaleImageLabel(getGlobalResources().getImage("scale-image-label.png"));
        label1.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
        label1.setUIID("DemoScaleImageLabel");

        ScaleImageLabel label2 = new ScaleImageLabel(getGlobalResources().getImage("scale-image-label.png"));
        label2.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
        label2.setUIID("DemoScaleImageLabel");

        ScaleImageLabel label3 = new ScaleImageLabel(getGlobalResources().getImage("scale-image-label.png"));
        label3.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
        label3.setUIID("DemoScaleImageLabel");

        threeImagesContainer.addAll(label1, label2, label3);                           
        labelContainer.add(threeImagesContainer);
        labelContainer.setUIID("LabelContainer");
                                                                                                                                  
        return BoxLayout.encloseY(labelContainer);
    }
}
