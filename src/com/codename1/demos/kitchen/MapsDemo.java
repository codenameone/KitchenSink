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

import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.execute;
import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the Google Maps within the app.
 *
 * @author Sergey Gerashenko.
 */
public class MapsDemo extends Demo {
    // Should be replaced with real api key in order to activate the demo. 
    private String googleMapsHTMLKey = null;

    public MapsDemo(Form parentForm) {
        init("Maps", getGlobalResources().getImage("demo-maps.png"), parentForm,
                "https://github.com/sergeyCodenameOne/KitchenSinkDemo/blob/master/src/com/codename1/demos/kitchen/MapsDemo.java");
    }
    
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BorderLayout(), "DemoContainer");
        
        demoContainer.add(BorderLayout.NORTH, createComponent(getGlobalResources().getImage("map-google-component.png"),
                                                                "Google Map",
                                                                "Google Map class", e->{
                                                                    if(googleMapsHTMLKey == null){
                                                                        showDemo("Google Map", createKeysGuide());
                                                                    }else{
                                                                        showDemo("Google Map", createGoogleMapComponent());
                                                                    }
                                                                }));
        return demoContainer;
    }
    
    private Component createGoogleMapComponent(){
        final MapContainer cnt = new MapContainer(googleMapsHTMLKey);

        Button btnMoveCamera = new Button("Move Camera");
        btnMoveCamera.addActionListener(e->{
            cnt.setCameraPosition(new Coord(-33.867, 151.206));
        });
        
        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, Display.getInstance().convertToPixels(3));

        Button btnAddMarker = new Button("Add Marker");
        btnAddMarker.addActionListener(e->{
            cnt.setCameraPosition(new Coord(41.889, -87.622));
            cnt.addMarker(
                EncodedImage.createFromImage(markerImg, false),
                cnt.getCameraPosition(),
                "Hi marker",
                "Optional long description",
                evt -> {
                    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                }
            );
        });

        Button btnAddPath = new Button("Add Path");
        btnAddPath.addActionListener(e->{
            cnt.addPath(
                cnt.getCameraPosition(),
                new Coord(-33.866, 151.195), // Sydney
                new Coord(-18.142, 178.431),  // Fiji
                new Coord(21.291, -157.821),  // Hawaii
                new Coord(37.423, -122.091)  // Mountain View
            );
        });

        Button btnClearAll = new Button("Clear All");
        btnClearAll.addActionListener(e-> {
            cnt.clearMapLayers();
        });

        cnt.addTapListener(e-> {
            TextField enterName = new TextField();
            Container wrapper = BoxLayout.encloseY(new Label("Name:"), enterName);
            InteractionDialog dlg = new InteractionDialog("Add Marker");
            dlg.getContentPane().add(wrapper);
            enterName.setDoneListener(e2->{
                String txt = enterName.getText();
                cnt.addMarker(
                        EncodedImage.createFromImage(markerImg, false),
                        cnt.getCoordAtPosition(e.getX(), e.getY()),
                        enterName.getText(),
                        "",
                        e3->{
                            ToastBar.showMessage("You clicked " + txt, FontImage.MATERIAL_PLACE);
                        }
                );
                dlg.dispose();
            });
            dlg.showPopupDialog(new Rectangle(e.getX(), e.getY(), 10, 10));
            enterName.startEditingAsync();
        });

        Container root = LayeredLayout.encloseIn(
                BorderLayout.center(cnt),
                BorderLayout.south(
                        FlowLayout.encloseBottom(btnMoveCamera, btnAddMarker, btnAddPath, btnClearAll)
                )
        );
        return root;
    }
    
    private Container createKeysGuide(){
        Container keysGuideContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        keysGuideContainer.add(new Label("Configuration", "DemoHeaderLabel"));
        keysGuideContainer.add(new SpanLabel("In order to make this demo work you should generate Api Keys and define the following build arguments within your project:", "DemoLabel"));

        TextArea javascriptKey = new TextArea("javascript.googlemaps.key=YOUR_JAVASCRIPT_API_KEY");
        javascriptKey.setUIID("DemoTextArea");

        TextArea androidKey = new TextArea("android.xapplication=<meta-data android:name=\"com.google.android.maps.v2.API_KEY\" android:value=\"YOUR_ANDROID_API_KEY\"/>");
        androidKey.setUIID("DemoTextArea");

        TextArea iosKey = new TextArea("ios.afterFinishLaunching=[GMSServices provideAPIKey:@\"YOUR_IOS_API_KEY\"];");
        iosKey.setUIID("DemoTextArea");
        keysGuideContainer.addAll(javascriptKey, androidKey, iosKey);


        keysGuideContainer.add(new SpanLabel("Make sure to replace the values YOUR_ANDROID_API_KEY, YOUR_IOS_API_KEY, "+
                "and YOUR_JAVASCRIPT_API_KEY with the values you obtained from the Google Cloud console by following the instructions.\n\nAlso you need to replace "+
                "the \"googleMapsHTMLKey\" attribute in the source code.", "DemoLabel"));
        
        Button javascriptButton = new Button("Generate Javascript key", "DemoButton");
        javascriptButton.addActionListener(e-> execute("https://developers.google.com/maps/documentation/javascript/overview"));
        
        Button iosButton = new Button("Generate IOS Key", "DemoButton");
        iosButton.addActionListener(e-> execute("https://developers.google.com/maps/documentation/ios-sdk/start"));
        
        Button androidButton = new Button("Generate Android Key", "DemoButton");
        androidButton.addActionListener(e-> execute("https://developers.google.com/maps/documentation/android-sdk/start"));
        
        Button infoButton = new Button("For More Information", "DemoButton");
        infoButton.addActionListener(e-> execute("https://www.codenameone.com/blog/new-improved-native-google-maps.html"));
        
        keysGuideContainer.addAll(javascriptButton, iosButton, androidButton, infoButton);
        keysGuideContainer.setUIID("Wrapper");
        Container demoContainer =  BoxLayout.encloseY(keysGuideContainer);
        demoContainer.setScrollableY(true);
        return demoContainer;
    }
}
