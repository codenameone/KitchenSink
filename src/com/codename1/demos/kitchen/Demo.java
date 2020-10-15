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

import com.codename1.components.ScaleImageButton;
import com.codename1.components.SpanButton;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import java.util.List;

import static com.codename1.ui.CN.getCurrentForm;
import static com.codename1.ui.ComponentSelector.select;

/** 
 * This is the base class for all the demos.
 */
public abstract class Demo{
    private String id;
    private Image demoImage;
    private Form parentForm;
    private String sourceCode;
    
    protected void init(String id, Image demoImage, Form parentForm, String sourceCode){
        this.id = id;
        this.demoImage = demoImage;
        this.parentForm = parentForm;
        this.sourceCode = sourceCode;
    }
    
    protected String getSourceCode(){
        return sourceCode;
    }

    protected String getDemoId(){
        return id;
    }
   
    protected Image getDemoImage(){
        return demoImage;
    }
    
    protected Form getParentForm(){
        return parentForm;
    }

    /**
     * Build the content of the demo that derives this class.
     *
     * @return container that contain the demo content.
     */
    abstract public Container createContentPane();

    protected void showDemo(String title, Component content){
            Form demoForm = new Form(title, new BorderLayout());
            content.setUIID("ComponentDemoContainer");
            Toolbar toolbar = demoForm.getToolbar();
            toolbar.setUIID("ComponentDemoToolbar");
            toolbar.getTitleComponent().setUIID("ComponentDemoTitle");

            Form lastForm = getCurrentForm();
            Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("DemoTitleCommand")),
                    e-> lastForm.showBack());

            toolbar.setBackCommand(backCommand);
            demoForm.add(BorderLayout.CENTER, content);
            demoForm.show();
    }
    
    public static void adjustToTablet(Container cnt){
        // Create anonymous class and override the calcPreferredSize() function to fit execly half of the scree.
        Container leftSide = new Container(new BoxLayout(BoxLayout.Y_AXIS)){
            @Override
            protected Dimension calcPreferredSize() {
                Dimension dim = super.calcPreferredSize();
                dim.setWidth(Display.getInstance().getDisplayWidth() / 2);
                return dim;
            }
        };
        
        Container rightSide = new Container(new BoxLayout(BoxLayout.Y_AXIS)){
            @Override
            protected Dimension calcPreferredSize() {
                Dimension dim = super.calcPreferredSize();
                dim.setWidth(Display.getInstance().getDisplayWidth() / 2);
                return dim;
            }
            
        };
        int i = 0;
        for(Component currComponent : cnt.getChildrenAsList(true)){
            cnt.removeComponent(currComponent);
            if(i++ % 2 == 0){
                leftSide.add(currComponent);
            }else{
                rightSide.add(currComponent);
            }
        }
        cnt.setLayout(new BoxLayout(BoxLayout.X_AXIS));
        cnt.addAll(leftSide, rightSide);
    }

    public Component createComponent(Image image, String header, String firstLine, String body, ActionListener listener){
        Container demoContent = new AccordionComponent(image, header, firstLine, body, listener);
        return demoContent;
    }

    public Component createComponent(Image image, String header, String firstLine, ActionListener listener){
        ScaleImageButton contentImage = new ScaleImageButton(image){
            @Override
            protected Dimension calcPreferredSize() {

                Dimension preferredSize =  super.calcPreferredSize();
                preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                return preferredSize;
            }
        };
        contentImage.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);

        contentImage.addActionListener(listener);
        contentImage.setUIID("DemoContentImage");
        Label contentHeader = new Button(header, "DemoContentHeader");
        Label contentFirstLine = new Button(firstLine, "DemoContentBody");

        Container demoContent = BoxLayout.encloseY(contentImage, contentHeader, contentFirstLine);
        contentImage.setWidth(demoContent.getWidth() - demoContent.getAllStyles().getPadding(Component.LEFT) - demoContent.getAllStyles().getPadding(Component.RIGHT) - contentImage.getAllStyles().getPadding(Component.LEFT) - contentImage.getAllStyles().getPadding(Component.RIGHT));
        demoContent.setLeadComponent(contentImage);
        demoContent.setUIID("DemoContentRegular");
        return demoContent;
    }

    private class AccordionComponent extends Container{
        private boolean isOpen = false;
        private Button firstLine;
        private SpanButton body;
        private Image openedIcon;
        private Image closedIcon;
        private Button openClose;

        /**
         * Demo component that have more then one line of description.
         *
         * @param image the image of the component.
         * @param header the header of the component.
         * @param firstLine first line of description.
         * @param body the rest of the description.
         * @param listener add ActionListener to the image of the component.
         */
        private AccordionComponent(Image image, String header, String firstLine, String body, ActionListener listener) {
            super(new BorderLayout());
            this.firstLine = new Button(firstLine, "DemoContentBody");
            this.body = new SpanButton(body, "DemoContentBody");
            this.body.setHidden(true);

            this.firstLine.addActionListener(listener);
            this.body.addActionListener(listener);

            setUIID("DemoContentAccordion");
            ScaleImageButton contentImage = new ScaleImageButton(image){
                @Override
                protected Dimension calcPreferredSize() {

                    Dimension preferredSize =  super.calcPreferredSize();
                    preferredSize.setHeight(Display.getInstance().getDisplayHeight() / 10);
                    return preferredSize;
                }
            };
            contentImage.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
            contentImage.addActionListener(listener);
            contentImage.setUIID("DemoContentImage");
            Button contentHeader = new Button(header, "DemoContentHeader");
            contentHeader.addActionListener(listener);

            Style buttonStyle = UIManager.getInstance().getComponentStyle("AccordionButton");
            openedIcon = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_UP, buttonStyle);
            closedIcon = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_DOWN, buttonStyle);
            openClose = new Button("", closedIcon, "AccordionButton");
            openClose.addActionListener(e->{
                if(isOpen){
                    close();
                }else{
                    open();
                }
            });

            Container cnt = new Container(new BorderLayout());
            cnt.add(BorderLayout.NORTH, contentImage);
            cnt.add(BorderLayout.WEST, BoxLayout.encloseY(contentHeader, this.firstLine));
            cnt.add(BorderLayout.EAST, openClose);

            add(BorderLayout.NORTH, cnt);
            add(BorderLayout.CENTER, this.body);
        }

        public void open(){
            // Select all AccordionComponent objects to close them when we open another one.
            List<Component> accordionList = select("DemoContentAccordion").asList();
            for(Component currComponent: accordionList){
                ((AccordionComponent)currComponent).close();
            }

            if (!isOpen){
                isOpen = true;
                openClose.setIcon(openedIcon);
                body.setHidden(false);
                body.animateLayout(1);
                animateLayout(250);
            }
        }

        public void close(){
            if (isOpen){
                isOpen = false;
                openClose.setIcon(closedIcon);
                body.setHidden(true);
                body.animateLayout(1);
                animateLayout(250);
            }
        }
    }
}
