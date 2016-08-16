/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 *  in the LICENSE file that accompanied this code.
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

import com.codename1.capture.Capture;
import com.codename1.components.MediaPlayer;
import com.codename1.io.Log;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import java.io.IOException;

/**
 * Demo of the camera related functionality including QR code etc.
 *
 * @author Shai Almog
 */
public class Camera extends Demo {
    public String getDisplayName() {
        return "Camera";
    }

    public Image getDemoIcon() {
        return getResources().getImage("f-spot.png");
    }

    public Container createDemo() {
        Label captured = new Label(Image.createImage(10, 10, 0));
        Button gallery = new Button("Pick From Gallery");
        gallery.addActionListener(evt -> {
            Display.getInstance().openGallery(e -> {
                try {
                    if (e == null) {
                        System.out.println("user cancelled");
                        return;                        
                    }
                    String path = (String) e.getSource();
                    // we are opening the image with the file handle since the image
                    // is large this method can scale it down dynamically to a manageable
                    // size that doesn't exceed the heap
                    Image i = Image.createImage(path);
                    captured.setIcon(i.scaledWidth(Display.getInstance().getDisplayWidth() / 2));
                    captured.getComponentForm().revalidate();
                }catch (Exception ex) {
                    Log.e(ex);
                }
            }, Display.GALLERY_IMAGE);
        });
        
        
        Button capture = new Button("Capture");
        capture.addActionListener(evt -> {
            try {
                String value = Capture.capturePhoto(Display.getInstance().getDisplayWidth() / 2, -1);
                if(value != null) {
                    captured.setIcon(Image.createImage(value));
                }                        
            } catch (Exception ex) {
                Log.e(ex);
                Dialog.show("Error", "" + ex, "OK", null);
            }
        });

        Button captureVideo = new Button("Capture Video");
        captureVideo.addActionListener(evt -> {
            try {
                String value = Capture.captureVideo();
                if(value != null) {
                    final Form previous = Display.getInstance().getCurrent();
                    Form preview = new Form("Preview");
                    preview.setLayout(new BorderLayout());
                    MediaPlayer pl = new MediaPlayer();
                    if(!value.startsWith("file:/")) {
                        value = "file:/" + value;
                    }
                    pl.setDataSource(value);
                    preview.addComponent(BorderLayout.CENTER, pl);
                    preview.setBackCommand(new Command("Back") {
                        public void actionPerformed(ActionEvent evt) {                        
                            previous.showBack();
                        }
                    });
                    preview.show();
                }
            } catch (Exception ex) {
                Log.e(ex);
                Dialog.show("Error", "" + ex, "OK", null);
            }
        });

        Button captureAudio = new Button("Capture Audio");
        captureAudio.addActionListener(evt -> {
            final String value = Capture.captureAudio();
            if(value != null) {
                Button playCapturedAudio = new Button("Play Captured Audio");
                Container cnt = captured.getParent();
                cnt.add(playCapturedAudio);
                cnt.getComponentForm().revalidate();
                playCapturedAudio.addActionListener(e -> {
                    try {
                        Media m = MediaManager.createMedia(value, false);
                        m.play();
                    } catch (IOException ex) {
                        Log.e(ex);
                        Dialog.show("Error", "" + ex, "OK", null);
                    }
                });
            }
        });
                
        return ComponentGroup.enclose(gallery, capture, captureVideo, captureAudio, captured);
    }
    
}
