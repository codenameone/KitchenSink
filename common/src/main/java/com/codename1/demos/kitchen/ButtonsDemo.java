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

import com.codename1.components.*;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.ImageIO;

import java.io.OutputStream;

import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate a simple usage of the Button, SpanButton, MultiButton, ScaleImageButton
 * FloatingActionButton, and ShareButton components.
 * The buttons are components that allowing clickability.
 *
 * @author Sergey Gerashenko.
 */
public class ButtonsDemo extends Demo{
    FloatingActionButton badge = null;

    public ButtonsDemo(Form parentForm) {
        init("Buttons", getGlobalResources().getImage("demo-buttons.png"), parentForm,
                "https://github.com/codenameone/KitchenSink/tree/master/common/src/main/java/com/codename1/demos/kitchen/ButtonsDemo.java");
    }
     
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);

        demoContainer.add(createComponent(getGlobalResources().getImage("buttons.png"),
                                                                "Buttons",
                                                                "Button is the base class for several UI",
                                                                "widgets allowing clickability. It has 3 States: rollover, pressed and the default state. Button can also "+
                                                                "have an Action Listener that react when the button is clicked or handle actions via a Command.Button UIID by "+
                                                                "default.", e-> showDemo("Buttons", createButtonsDemo())));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("span-button.png"),
                                                                "Span Buttons",
                                                                "A complex button similar to MultiButton",
                                                                "that breaks lines automatically and looks like a regular button(more or less). Unlike the multi button the "+
                                                                "span buttons has the UIID style of a button.",
                                                                 e-> showDemo("Span Buttons", createSpanButtonsDemo())));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("multi-buttons.png"),
                                                                "Multi Buttons",
                                                                "A powerful button like component that",
                                                                "allows multiple rows/and an icon to be added every row/icon can have its own UIID.\n\nInternally the "+
                                                                "multi-button is a container with a lead component. Up to 4 rows are supported.", e-> showDemo("Multi Buttons", createMultiButtonsDemo())));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("scale-image-label.png"),
                                                                "Scale Image Button",
                                                                "Button that simplifies the usage of scale to",
                                                                "fill/fit. This is effectively equivalent to just setting the style image on a button but more convenient "+
                                                                "for some special circumstances.\n\nOne major difference is that preferred size equals the image in this case.",
                                                                e-> showDemo("Scale Image Button", createScaleImageButton())));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("floating-action-button.png"),
                                                                "Floating Action Button",
                                                                "Floating action buttons are a material design",
                                                                "element used to promote a special action in a form. They are represented as a floating circle with a "+
                                                                "flat icon floating above the UI typically in the bottom right area.", e-> showDemo("Floating Action Button", createFloatingActionButtonDemo())));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("share-button.png"),
                                                                "Share Button",
                                                                "The share button allows sharing a String",
                                                                "or an image either thru the defined sharing services or thru native OS sharing support. On Android & iOS the "+
                                                                "native sharing API is invoked for this class.\n\nThe code below demonstrates image sharing, notice that an "+
                                                                "image must be stored using the FileSystemStorage API and shouldn't use a different API like Storage.", e-> showDemo("Share Button", createShareButtonDemo())));
        return demoContainer;
    }
    
        
    private Container createButtonsDemo(){ 
        Button firstButton = new Button("Button", "DemoButton");
        firstButton.addActionListener(e-> ToastBar.showInfoMessage("First Button was pressed") );
        
        Button secondButton = new Button("", FontImage.MATERIAL_INFO, "DemoButton");
        secondButton.addActionListener(e-> ToastBar.showInfoMessage("Second Button was pressed") );
        
        Button thirdButton = new Button("Button", FontImage.MATERIAL_INFO, "DemoButton");
        thirdButton.addActionListener(e-> ToastBar.showInfoMessage("Third Button was pressed") );

        Container demoContainer =  BoxLayout.encloseY(new Label("Button with text:", "DemoLabel"),
                                                        firstButton,
                                                        new Label("Button with icon:", "DemoLabel"),
                                                        secondButton,
                                                        new Label("Button with text and icon:", "DemoLabel"),
                                                        thirdButton);
        demoContainer.setUIID("ButtonContainer");
        
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createSpanButtonsDemo(){ 
        SpanButton button = new SpanButton("A complex button similar to MultiButton that breaks lines automatically and looks like "+
                                                "a regular button(more or less). Unlike the multi button the "+
                                                "span buttons has the UIID style of a button.", "WhiteText");
        button.setUIID("DemoButton");
        button.addActionListener(e-> ToastBar.showInfoMessage("Button was pressed"));
        
        Container spanLabelContainer = BoxLayout.encloseY(new Label("SpanButton:", "DemoLabel"), button);
        spanLabelContainer.setUIID("ButtonContainer");
        return BoxLayout.encloseY(spanLabelContainer);
    }
    
    private Container createMultiButtonsDemo(){ 
        MultiButton twoLinesNoIcon = new MultiButton("MultiButton");
        twoLinesNoIcon.setUIID("DemoButton");
        twoLinesNoIcon.setUIIDLine1("DemoMultiButtonHeader");
        twoLinesNoIcon.setTextLine2("Line 2");
        twoLinesNoIcon.setUIIDLine2("DemoMultiButtonText");

        Image emblem = FontImage.createMaterial(FontImage.MATERIAL_ARROW_RIGHT, UIManager.getInstance().getComponentStyle("DemoMultiIcon"));
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_INFO, UIManager.getInstance().getComponentStyle("DemoMultiIcon"));

        MultiButton oneLineIconEmblem = new MultiButton("Icon + Emblem");
        oneLineIconEmblem.setUIIDLine1("DemoMultiButtonHeader");
        oneLineIconEmblem.setIcon(icon);
        oneLineIconEmblem.setEmblem(emblem);
        oneLineIconEmblem.setIconUIID("DemoMultiButtonIcon");
        oneLineIconEmblem.setUIID("DemoButton");

        MultiButton twoLinesIconEmblem = new MultiButton("Icon + Emblem");
        twoLinesIconEmblem.setUIIDLine1("DemoMultiButtonHeader");
        twoLinesIconEmblem.setUIID("DemoButton");
        twoLinesIconEmblem.setIcon(icon);
        twoLinesIconEmblem.setIconUIID("DemoMultiButtonIcon");
        twoLinesIconEmblem.setEmblem(emblem);
        twoLinesIconEmblem.setTextLine2("Line 2");
        twoLinesIconEmblem.setUIIDLine2("DemoMultiButtonText");

        MultiButton twoLinesIconEmblemHorizontal = new MultiButton("Icon + Emblem");
        twoLinesIconEmblemHorizontal.setUIIDLine1("DemoMultiButtonHeader");
        twoLinesIconEmblemHorizontal.setUIID("DemoButton");
        twoLinesIconEmblemHorizontal.setIcon(icon);
        twoLinesIconEmblemHorizontal.setIconUIID("DemoMultiButtonIcon");
        twoLinesIconEmblemHorizontal.setEmblem(emblem);
        twoLinesIconEmblemHorizontal.setTextLine2("Line 2");
        twoLinesIconEmblemHorizontal.setUIIDLine2("DemoMultiButtonText");
        twoLinesIconEmblemHorizontal.setHorizontalLayout(true);

        MultiButton fourLinesIcon = new MultiButton("With Icon");
        fourLinesIcon.setUIID("DemoButton");
        fourLinesIcon.setIcon(icon);
        fourLinesIcon.setIconUIID("DemoMultiButtonIcon");
        fourLinesIcon.setUIIDLine1("DemoMultiButtonHeader");
        fourLinesIcon.setTextLine2("Line 2");
        fourLinesIcon.setUIIDLine2("DemoMultiButtonText");
        fourLinesIcon.setTextLine3("Line 3");
        fourLinesIcon.setUIIDLine3("DemoMultiButtonText");
        fourLinesIcon.setTextLine4("Line 4");
        fourLinesIcon.setUIIDLine4("DemoMultiButtonText");

        Container demoContainer = BoxLayout.encloseY(oneLineIconEmblem,
                                                    twoLinesNoIcon,
                                                    twoLinesIconEmblem,
                                                    twoLinesIconEmblemHorizontal,
                                                    fourLinesIcon);
        
        demoContainer.setUIID("ButtonContainer");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createShareButtonDemo(){
        ShareButton textShare = new ShareButton();
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_SHARE, UIManager.getInstance().getComponentStyle("DemoButtonIcon"));
        textShare.setIcon(icon);
        textShare.setUIID("DemoButton");
        textShare.setTextToShare("Hello there");
        textShare.setText("share text");
        
        String imagePath = FileSystemStorage.getInstance().getAppHomePath() + "icon.png";
        Image imageToShare = getGlobalResources().getImage("code-name-one-icon.png");
        
        try {
            OutputStream os = FileSystemStorage.getInstance().openOutputStream(imagePath);
            ImageIO.getImageIO().save(imageToShare, os, ImageIO.FORMAT_PNG, 1f);
            os.close();
        }
        catch (Exception error) {
            Log.e(error);
        }
        
        ShareButton imageShare = new ShareButton();
        imageShare.setUIID("DemoButton");
        imageShare.setIcon(icon);
        imageShare.setText("share image");
        imageShare.setImageToShare(imagePath, "image/png");

        Container shareButtonContainer = BoxLayout.encloseY(textShare, imageShare);
        shareButtonContainer.setUIID("ButtonContainer");
        
        return BoxLayout.encloseY(shareButtonContainer);
    }
    
    private Container createScaleImageButton(){
        TableLayout tableLayout = new TableLayout(2, 2);
        Container buttonsContainer = new Container(tableLayout);
        
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_IMAGE, UIManager.getInstance().getComponentStyle("DemoScaleImageButton"));
        
        ScaleImageLabel fillLabel = new ScaleImageLabel(icon);
        fillLabel.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        fillLabel.setUIID("DemoScaleImageButton");
        
        ScaleImageButton fillButton = new ScaleImageButton(icon);
        fillButton.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        fillButton.setUIID("DemoScaleImageButton");
        
        ScaleImageButton button1 = new ScaleImageButton(icon);
        button1.setUIID("DemoScaleImageButton");
        
        ScaleImageButton button2 = new ScaleImageButton(icon);
        button2.setUIID("DemoScaleImageButton");
        
        buttonsContainer.add(tableLayout.createConstraint().widthPercentage(20), button1).
                add(tableLayout.createConstraint().widthPercentage(80), new Label("<- 20% of the screen", "ScaleComponentDemoLabel")).
                add(new SpanLabel("80% of the screen->", "ScaleComponentDemoLabel")).
                add(button2).
                add(fillLabel).
                add(new Label("<-image fill", "ScaleComponentDemoLabel")).
                add(new SpanLabel("image fill->", "ScaleComponentDemoLabel")).
                add(fillButton);
        
        Container demoContainer = BoxLayout.encloseY(buttonsContainer);
        demoContainer.setUIID("ButtonContainer");
        
        return BoxLayout.encloseY(demoContainer);
    }

    private Container createFloatingActionButtonDemo(){
        Container tasksContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        tasksContainer.setScrollableY(true);
        FloatingActionButton addNew = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD, "RedFabDemo");
        badge = FloatingActionButton.createBadge("0");

        FloatingActionButton greenButton = addNew.createSubFAB(FontImage.MATERIAL_ADD_TASK, "");
        greenButton.setUIID("GreenFabDemo");
        greenButton.addActionListener(e->{
            TextComponent header = new TextComponent().labelAndHint("note header: ");
            TextComponent body = new TextComponent().labelAndHint("note body: ");
            Command ok = new Command("Ok");
            Command cancel = new Command("Cancel");
            if (Dialog.show("Enter Note", BoxLayout.encloseY(header, body), ok, cancel) == ok && header.getText().length() != 0){
                tasksContainer.add(createNote(header.getText(), body.getText(), tasksContainer, true));
                int taskCount = Integer.parseInt(badge.getText()) + 1;
                badge.setText(String.valueOf(taskCount));
                tasksContainer.revalidate();
            }
        });

        FloatingActionButton purpleButton = addNew.createSubFAB(FontImage.MATERIAL_ADD_TASK, "");
        purpleButton.setUIID("PurpleFabDemo");
        purpleButton.addActionListener(e->{
            TextComponent header = new TextComponent().labelAndHint("note header: ");
            TextComponent body = new TextComponent().labelAndHint("note body: ");
            Command ok = new Command("Ok");
            Command cancel = new Command("Cancel");
            if (Dialog.show("Enter Note", BoxLayout.encloseY(header, body), ok, cancel) == ok && header.getText().length() != 0){
                tasksContainer.add(createNote(header.getText(), body.getText(), tasksContainer, false));
                int taskCount = Integer.parseInt(badge.getText()) + 1;
                badge.setText(String.valueOf(taskCount));
                tasksContainer.revalidate();
            }
        });

        Label header = new Label("My Tasks", "DemoHeaderLabel");
        tasksContainer.add(header);
        tasksContainer.setUIID("Wrapper");
        Container demoContainer = badge.bindFabToContainer(tasksContainer, Component.RIGHT, Component.TOP);
        return addNew.bindFabToContainer(BorderLayout.center(demoContainer));
    }
    
    private Component createNote(String header, String body, Container notes, boolean isGreen){
        Button deleteButton;
        Label emptyLabel = new Label(" ", "EmptyGreenLabel");
        SpanLabel noteHeaderLabel = new SpanLabel(header, "NoteHeaderLabel");
        SpanLabel noteBodyLabel = new SpanLabel(body, "NoteBodyLabel");
        Container noteContainer = BoxLayout.encloseY(emptyLabel, noteHeaderLabel, noteBodyLabel);
        if(isGreen) {
            deleteButton = new Button("", FontImage.createMaterial(FontImage.MATERIAL_DELETE, UIManager.getInstance().getComponentStyle("DeleteButton")), "DeleteButton");
            noteContainer.setUIID("NoteGreenContainer");
        }else{
            deleteButton = new Button("", FontImage.createMaterial(FontImage.MATERIAL_DELETE, UIManager.getInstance().getComponentStyle("PurpleDeleteButton")), "DeleteButton");
            noteContainer.setUIID("NotePurpleContainer");
            emptyLabel.setUIID("EmptyPurpleLabel");
        }

        SwipeableContainer note = new SwipeableContainer(deleteButton, noteContainer);
        deleteButton.addActionListener(e->{
            notes.removeComponent(note);
            int taskCount = Integer.parseInt(badge.getText()) - 1;
            badge.setText(String.valueOf(taskCount));
            notes.revalidate();
        });
        
        return note;
    }
}
