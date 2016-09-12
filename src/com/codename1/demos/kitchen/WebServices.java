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

import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.InfiniteContainer;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.EventDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Webservice tutorial showing off a faux dog picture set and the ability to browse thru images from the 
 * network using ImageViewer.
 *
 * @author Shai Almog
 */
public class WebServices extends Demo {
    private static final String WEBSERVICE_URL =  "https://www.codenameone.com/files/kitchensink/dogs/list2.json";
    private EncodedImage placeholder;
    
    public String getDisplayName() {
        return "Dogs";
    }

    public Image getDemoIcon() {
        return getResources().getImage("dog.jpg");
    }

    @Override
    public String getDescription() {
        return "Webservice tutorial showing off a faux dog picture set and the ability to browse thru images from the "
                + "network using ImageViewer.";
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/WebServices.java";
    }

    
    /**
     * List model for the image viewer that fetches full sized versions of the images. For the sake of simplicity
     * this model doesn't invoke the webservice and uses the existing webservice data!
     */
    class ImageListModel implements ListModel<Image> {
        private List allItems;
        private int selection;
        private HashMap<String, Image> cache = new HashMap<>();
        private EventDispatcher listeners = new EventDispatcher();
        private EventDispatcher selectionListener = new EventDispatcher();
        public ImageListModel(List allItems, int selection) {
            this.allItems = allItems;
            this.selection = selection;
        }
        
        @Override
        public Image getItemAt(int index) {
            Map<String, Object> m = (Map<String, Object>)allItems.get(index);
            String url = (String)m.get("url");
            String filename = "fullsize-" + url.substring(url.lastIndexOf("/") + 1);
            FileSystemStorage fs = FileSystemStorage.getInstance();
            if(fs.exists(fs.getAppHomePath() + filename)) {
                Image i = cache.get(filename);
                if(i != null) {
                    return i;
                }
                try {
                    i = Image.createImage(fs.getAppHomePath() + filename);
                    cache.put(filename, i);
                    return i;
                } catch(IOException err) {
                    Log.e(err);
                }
            }
            if(cache.get(filename) == null){
                ConnectionRequest cn = new ConnectionRequest(url);
                cn.setPost(false);
                cn.downloadImageToFileSystem(fs.getAppHomePath() + filename, i -> {
                    cache.put(filename, i);
                    listeners.fireDataChangeEvent(index, DataChangedListener.CHANGED);
                });
                cache.put(filename, placeholder);
            }
            return placeholder;
        }

        @Override
        public int getSize() {
            return allItems.size();
        }

        @Override
        public int getSelectedIndex() {
            return selection;
        }

        @Override
        public void setSelectedIndex(int index) {
            if(index != selection) {
                int o = selection;
                selection = index;
                selectionListener.fireSelectionEvent(o, index);
            }
        }

        @Override
        public void addDataChangedListener(DataChangedListener l) {
            listeners.addListener(l);
        }

        @Override
        public void removeDataChangedListener(DataChangedListener l) {
            listeners.removeListener(l);
        }

        @Override
        public void addSelectionListener(SelectionListener l) {
            selectionListener.addListener(l);
        }

        @Override
        public void removeSelectionListener(SelectionListener l) {
            selectionListener.removeListener(l);
        }

        @Override
        public void addItem(Image item) {
        }

        @Override
        public void removeItem(int index) {
        }        
    }
    
    public Container createDemo() {
        if(placeholder == null) {
            Image placeholderTmp = getResources().getImage("dog.jpg");
            if(Display.getInstance().isGaussianBlurSupported()) {
                placeholderTmp = Display.getInstance().gaussianBlurImage(placeholderTmp, 10);
            } else {
                placeholderTmp = placeholderTmp.modifyAlpha((byte)100);
            }
            placeholder = EncodedImage.createFromImage(placeholderTmp, true);
        }
        InfiniteContainer ic = new InfiniteContainer(10) {
            List items;
            List allItems = new ArrayList();
            String nextURL = WEBSERVICE_URL;            
            
            @Override
            public Component[] fetchComponents(int index, int amount) {
                // pull to refresh resets the position
                if(index == 0) {
                    nextURL = WEBSERVICE_URL;
                }
                // downloaded all the content from the webservice
                if(nextURL == null) {
                    return null;
                }
                
                ConnectionRequest req = new ConnectionRequest(nextURL) {
                    @Override
                    protected void readResponse(InputStream input) throws IOException {
                        items = null;
                        JSONParser parser = new JSONParser();
                        Map response = parser.parseJSON(new InputStreamReader(input, "UTF-8"));
                        items = (List)response.get("items");
                        nextURL = (String)response.get("nextPage");
                    }

                    @Override
                    protected void handleException(Exception err) {
                        Log.e(err);
                        Display.getInstance().callSerially(() -> {
                            ToastBar.showErrorMessage("An error occured while connecting to the server: " + err);
                        });
                    }

                    @Override
                    protected void handleErrorResponseCode(int code, String message) {
                        Display.getInstance().callSerially(() -> {
                            ToastBar.showErrorMessage("Error code from the server: " + code + "\n" + message);
                        });
                    }
                    
                };
                req.setPost(false);
                NetworkManager.getInstance().addToQueueAndWait(req);
                
                if(items == null) {
                    return null;
                }
                
                // used by the list model for the image viewer
                allItems.addAll(items);
                
                Component[] result = new Component[items.size()];
                
                for(int iter = 0 ; iter < result.length ; iter++) {
                    Map<String, Object> m = (Map<String, Object>)items.get(iter);
                    String title = (String)m.get("title");
                    String details = (String)m.get("details");
                    String url = (String)m.get("url");
                    String thumb = (String)m.get("thumb");
                    URLImage thumbImage = URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), thumb, URLImage.RESIZE_SCALE_TO_FILL);
                    ScaleImageButton btn = new ScaleImageButton(thumbImage);
                    btn.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
                    Label titleLabel = new Label(title, "TintOverlay");
                    result[iter] = LayeredLayout.encloseIn(btn, 
                            BorderLayout.south(titleLabel));
                    
                    btn.addActionListener(e -> {
                        int offset = btn.getParent().getParent().getComponentIndex(btn.getParent());
                        ImageListModel imlm = new ImageListModel(allItems, offset);
                        Form viewer = new Form(title + " - " + (offset + 1) + " of " + imlm.getSize(), new BorderLayout());
                        Image img = (Image)imlm.getItemAt(offset);
                        if(img == placeholder) {
                            img = thumbImage;
                        }
                        ImageViewer v = new ImageViewer(img);
                        v.setImageList(imlm);
                        SpanLabel desc = new SpanLabel(details);
                        desc.setTextUIID("TintOverlaySmall");
                        desc.setUIID("Container");
                        imlm.addSelectionListener((o, n) -> {
                            Map<String, Object> mm = (Map<String, Object>)allItems.get(n);
                            desc.setText((String)mm.get("details"));
                            viewer.setTitle((String)mm.get("title") + " - " + (n + 1) + " of " + imlm.getSize());
                        });
                        viewer.add(BorderLayout.CENTER, 
                                LayeredLayout.encloseIn(v, BorderLayout.south(desc)));
                        viewer.getToolbar().setBackCommand("", ee -> btn.getComponentForm().showBack());
                        viewer.show();
                    });
                }
                Layout l = getLayout();
                if(l instanceof GridLayout) {
                    int cmps = getComponentCount() - 1 + result.length;
                    int extra = 0;
                    if(cmps % 3 != 0) {
                        extra = 1;
                    }
                    setLayout(new GridLayout(cmps / 3 + extra, 3));
                }
                return result;
            }
        };
        
        if(Display.getInstance().isTablet()) {
            GridLayout gl = new GridLayout(3);
            ic.setLayout(gl);
        }
        
        return ic;
    }
    

}
