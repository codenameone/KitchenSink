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

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SliderBridge;
import com.codename1.components.SpanLabel;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.ui.*;
import com.codename1.ui.CommonProgressAnimations.CircleProgress;
import com.codename1.ui.CommonProgressAnimations.LoadingTextAnimation;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.EasyThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codename1.io.Util.sleep;
import static com.codename1.ui.CN.callSerially;
import static com.codename1.ui.CN.invokeAndBlock;
import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the infiniteProgress, Slider, CircleAnimation and TextLoadingAnimation Components.
 * The progress components are used to inform the user that certain operation is in progress.
 *
 * @author Sergey Gerashenko.
 */
public class ProgressDemo extends Demo {
    
    public ProgressDemo(Form parentForm) {
        init("Progress", getGlobalResources().getImage("progress-demo.png"), parentForm,
                "https://github.com/sergeyCodenameOne/KitchenSinkDemo/blob/master/src/com/codename1/demos/kitchen/ProgressDemo.java");
    }
     
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);
        
        demoContainer.add(createComponent(getGlobalResources().getImage("infinite-progress.png"),
                                                    "Infinite Progress",
                                                    "Shows a \"Washing Machine\" infinite",
                                                    "progress indication animation, to customize the image "+
                                                    "you can either use the infiniteImage theme constant or the setAnimation method. The image "+
                                                    "is rotated automatically so don't use an animated image or anything like that as it would "+
                                                    "fail with the rotation logic.",
                                                    e->{
                                                        showDemo("Infinite Progess", createInfiniteProgessDemo());
                                                    }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("slider.png"),
                                                    "Slider",
                                                    "The slider component serves both as a",
                                                    "slider widget to allow users to select a value on a scale via touch/arrows and also to indicate progress. The slider "+
                                                    "defaults to percentage display but can represent any positive set of values.",
                                                    e->{
                                                        showDemo("Slider", createSliderDemo());
                                                    }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("circle-animation.png"),
                                                    "Circle Animation",
                                                    "A CommonProgrssAnimations which shows",
                                                    "radial coloring to show circular progress, like a Pac-Man",
                                                    e->{
                                                        showDemo("Circle Animation", createCircleAnimationDemo());
                                                    }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("text-loading-animation.png"),
                                                    "Text Loading Animation",
                                                    "A CommonProgressAnimations item used ",
                                                    "to show the text is loading when we are fetching some text data from network/database",
                                                    e->{
                                                        showDemo("Text Loading Animation", createTextLoadAnimationDemo());
                                                    }));
        return demoContainer;
    }
    
    private Container createInfiniteProgessDemo(){
        Dialog ip = new InfiniteProgress().showInfiniteBlocking();
        invokeAndBlock(()->{
            sleep(3000); // do some long operation here.
            callSerially(()-> ip.dispose());
        });
        InfiniteProgress prog = new InfiniteProgress();
        prog.setAnimation(FontImage.createMaterial(FontImage.MATERIAL_AUTORENEW, UIManager.getInstance().getComponentStyle("DemoInfiniteProgress")));
        return BorderLayout.centerAbsolute(prog);   
    }
    
    private Container createSliderDemo(){
        Slider progress = new Slider();

        Button download = new Button("Download", "DemoButton");
        Container demoContainer = BorderLayout.south(progress).
                                    add(BorderLayout.NORTH, download);
        
        download.addActionListener((e) -> {
            ConnectionRequest cr = new ConnectionRequest("https://www.codenameone.com/img/blog/new_icon.png", false);
            SliderBridge.bindProgress(cr, progress);
            NetworkManager.getInstance().addToQueueAndWait(cr);
            if(cr.getResponseCode() == 200) {
                demoContainer.add(BorderLayout.CENTER, new ScaleImageLabel(EncodedImage.create(cr.getResponseData())));
                demoContainer.revalidate();
            }
        });
        return demoContainer;
    }
    
    private Container createCircleAnimationDemo(){
        Label nameLabel = new Label("placeholder", "CenterAlignmentLabel");
        Container demoContainer = BorderLayout.centerCenter(nameLabel);
        // Replace the label by a CircleProgress to indicate that it is loading.
        CircleProgress.markComponentLoading(nameLabel).setUIID("BlueColor");
 
        /**
        * This code block should work without the EasyThread and callSerially()
        * its here only for the demonstration purpose. 
        **/
        EasyThread.start("").run(()->{
            sleep(3000);
            Response<Map> jsonData = Rest.
                    get("https://anapioficeandfire.com/api/characters/583").
                    acceptJson().
                    getAsJsonMap();

            callSerially(()->{
                nameLabel.setText(((Map<String, String>)jsonData.getResponseData()).get("name"));
                    // Replace the progress with the nameLabel now that
                    // it is ready, using a fade transition
                    CircleProgress.markComponentReady(nameLabel, CommonTransitions.createFade(300));
            });
        });
        return demoContainer;
    }
    
    private Container createTextLoadAnimationDemo(){
        SpanLabel profileText = new SpanLabel("placeholder", "CenterAlignmentLabel");

        Container demoContainer = BorderLayout.center(profileText);
        // Replace the label by a CircleProgress to indicate that it is loading.
        LoadingTextAnimation.markComponentLoading(profileText);
        
        /**
        * This code block should work without the EasyThread and callSerially()
        * its here only for the demonstration purpose. 
        */
        EasyThread.start("").run(()->{
            sleep(3000);
            Response<Map> response = Rest.
                    get("https://anapioficeandfire.com/api/characters/583").
                    acceptJson().
                    getAsJsonMap();

            Map<String, Object> data = response.getResponseData();
            StringBuilder sb = new StringBuilder();
            sb.append("name: " + data.get("name") + "\n");
            sb.append("gender: " + data.get("gender") + "\n");
            sb.append("culture: " + data.get("culture") + "\n");
            sb.append("born: " + data.get("born") + "\n");
            List<String> aliases = (ArrayList<String>)data.get("aliases");

            sb.append("aliases: \n");
            for (String alias : aliases){
                sb.append(alias + "\n");
            }
            callSerially(()->{
                profileText.setText(sb.toString());
                // Replace the progress with the nameLabel now that
                // it is ready, using a fade transition
                LoadingTextAnimation.markComponentReady(profileText, CommonTransitions.createFade(2000));
            });
        });
        return demoContainer;
    }
}
