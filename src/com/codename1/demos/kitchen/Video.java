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

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MediaPlayer;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import java.io.IOException;
import java.io.InputStream;

/**
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

    public Container createDemo(Form parent) {
        FileSystemStorage fs = FileSystemStorage.getInstance();
        if(!fs.exists(fs.getAppHomePath() + "hello-codenameone.mp4")) {
            Container downloading = BorderLayout.center(new Label("Downloading"));
            if(Display.getInstance().isTablet()) {
                downloadFile(downloading);
            } else {
                parent.addShowListener(e -> downloadFile(downloading));
            }
            return downloading;
        }
        return createVideoPlayer();
    }

    void downloadFile(final Container downloading) {
        ConnectionRequest cr = new ConnectionRequest("https://www.codenameone.com/files/hello-codenameone.mp4") {
            @Override
            protected void postResponse() {
                if(downloading.getParent() != null) {
                    downloading.getParent().replace(downloading, createVideoPlayer(), null);
                }
            }
        };
        cr.setPost(false);
        cr.setDestinationFile(FileSystemStorage.getInstance().getAppHomePath() + "hello-codenameone.mp4");
        ToastBar.showConnectionProgress("Downloading video", cr, null, null);
        NetworkManager.getInstance().addToQueue(cr);        
    }
    
    private Container createVideoPlayer() {
        FileSystemStorage fs = FileSystemStorage.getInstance();
        Container player = new Container(new BorderLayout());
        player.add(BorderLayout.CENTER, new InfiniteProgress());
        Display.getInstance().scheduleBackgroundTask(() -> {
            try {
                Media video = MediaManager.createMedia(fs.getAppHomePath() + "hello-codenameone.mp4", true);        
                video.prepare();
                Display.getInstance().callSerially(() ->{
                    final MediaPlayer mp = new MediaPlayer(video);
                    mp.setAutoplay(true);
                    mp.setLoop(true);
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
        return player;
    }
}
