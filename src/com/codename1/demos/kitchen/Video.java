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

import com.codename1.capture.Capture;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MediaPlayer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;

/**
 * You can play videos either from remote or local sources very easily in Codename One, here we also
 * show the ability to record a video that we can playback later.
 *
 * @author Shai Almog
 */
public class Video  extends Demo {

    public String getDisplayName() {
        return "Video";
    }

    public Image getDemoIcon() {
        return getResources().getImage("video.png");
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/Video.java";
    }

    @Override
    public String getDescription() {
        return "You can play videos either from remote or local sources very easily in Codename One, here we also "
                + "show the ability to record a video that we can playback later.";
    }

    
    
    public Container createDemo(Form parent) {
        MultiButton helloOnline = new MultiButton("Hello (Online)");
        MultiButton helloOffline = new MultiButton("Hello (Offline)");
        helloOnline.setTextLine2("Play thru http");
        helloOffline.setTextLine2("Download & play");
        MultiButton capture = new MultiButton("Capture Video");
        capture.setTextLine2("Record video");
        MultiButton playCapturedFile = new MultiButton("Play Captured Video");
        playCapturedFile.setTextLine2("Last capture...");
        String capturedFile = FileSystemStorage.getInstance().getAppHomePath() + "captured-file.mp4";
        
        FontImage.setMaterialIcon(helloOnline, FontImage.MATERIAL_VIDEO_LIBRARY);
        FontImage.setMaterialIcon(helloOffline, FontImage.MATERIAL_VIDEO_LIBRARY);
        FontImage.setMaterialIcon(capture, FontImage.MATERIAL_VIDEOCAM);
        FontImage.setMaterialIcon(playCapturedFile, FontImage.MATERIAL_PERSONAL_VIDEO);
        
        Container cnt = BoxLayout.encloseY(ComponentGroup.enclose(helloOnline, helloOffline, capture, playCapturedFile));
        cnt.setScrollableY(true);
        
        helloOffline.addActionListener(e -> {
            FileSystemStorage fs = FileSystemStorage.getInstance();
            if(!fs.exists(fs.getAppHomePath() + "hello-codenameone.mp4")) {
                downloadFile(parent);
            } else {
                playVideo(parent, fs.getAppHomePath() + "hello-codenameone.mp4");
            }
        });
        
        // special case: the simulator doesn't support https URLs for media due to JavaFX limitations
        if(Display.getInstance().isSimulator()) {
            helloOnline.addActionListener(e -> playVideo(parent, "http://www.codenameone.com/files/hello-codenameone.mp4"));
        } else {
            helloOnline.addActionListener(e -> playVideo(parent, "https://www.codenameone.com/files/hello-codenameone.mp4"));
        }
        
        capture.addActionListener(e -> {
            String result = Capture.captureVideo();
            if(result != null) {
                FileSystemStorage fs = FileSystemStorage.getInstance();
                try {
                    Util.copy(fs.openInputStream(result), fs.openOutputStream(capturedFile));
                } catch(IOException err) {
                    Log.e(err);
                    ToastBar.showErrorMessage("Error in copying captured file: " + err);
                }
            }
        });
        
        playCapturedFile.addActionListener(e -> {
            if(FileSystemStorage.getInstance().exists(capturedFile)) {
                playVideo(parent, capturedFile);
            } else {
                ToastBar.showErrorMessage("You need to capture a video first...");
            }
        });
        
        return cnt;
    }

    
    void downloadFile(final Form parent) {
        ConnectionRequest cr = new ConnectionRequest("https://www.codenameone.com/files/hello-codenameone.mp4") {
            @Override
            protected void postResponse() {
                if(parent != Display.getInstance().getCurrent()) {
                    if(!Dialog.show("Download Finished", "Downloading the video completed!\nDo you want to show it now?",
                            "Show", "Later")) {
                        return;
                    }
                    parent.show();
                }
                playVideo(parent, FileSystemStorage.getInstance().getAppHomePath() + "hello-codenameone.mp4");
            }
        };
        cr.setPost(false);
        cr.setDestinationFile(FileSystemStorage.getInstance().getAppHomePath() + "hello-codenameone.mp4");
        ToastBar.showConnectionProgress("Downloading video", cr, null, null);
        NetworkManager.getInstance().addToQueue(cr);        
    }

    private void playVideo(Form parent, String videoUrl) {
        Form player = new Form(new BorderLayout());
        player.getToolbar().setBackCommand("", e -> parent.showBack());
        player.add(BorderLayout.CENTER, new InfiniteProgress());
        Display.getInstance().scheduleBackgroundTask(() -> {
            try {
                Media video = MediaManager.createMedia(videoUrl, true, () -> parent.showBack());        
                video.prepare();
                Display.getInstance().callSerially(() ->{
                    final MediaPlayer mp = new MediaPlayer(video);
                    mp.setAutoplay(true);
                    video.setNativePlayerMode(true);
                    player.removeAll();
                    player.addComponent(BorderLayout.CENTER, mp);
                    player.revalidate();
                });
            } catch(IOException err) {
                Log.e(err);
                ToastBar.showErrorMessage("Error loading video: " + err);
            }
        });
        player.show();
    }
}
