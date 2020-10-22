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
package com.codename1.demos.kitchen.ComponentDemos;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Storage;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.properties.*;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.util.EventDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codename1.ui.CN.*;
import static com.codename1.ui.util.Resources.getGlobalResources;



public class ImageViewerDemo {
    private static final String WEBSERVICE_URL =  "https://www.codenameone.com/files/kitchensink/dogs/list.json";
    private EncodedImage placeholder;
    
    public Container createContentPane() {
        Container imageViewerContainer = new Container(new BorderLayout());
        if(placeholder == null) {
            Image tempPlaceHolder = getGlobalResources().getImage("blurred-puppy.jpg");
            placeholder = EncodedImage.createFromImage(tempPlaceHolder, true);
        }
        
        // Request the data from the server.
        Response<Map> resultData = Rest.get(WEBSERVICE_URL).acceptJson().getAsJsonMap();
        ItemList itemListData = new ItemList();
        itemListData.getPropertyIndex().populateFromMap(resultData.getResponseData());

                        
        if(resultData.getResponseCode() != 200) {
            callSerially(() -> {
                ToastBar.showErrorMessage("Error code from the server");
            });
            return null;
        }
                
        // Make list of items
        List<Item> itemList = new ArrayList();
        for(Map currItemMap : itemListData.items.asList()){
            Item currItem = new Item();
            currItem.getPropertyIndex().populateFromMap(currItemMap);
            itemList.add(currItem);
        }

        Item firstItem = itemList.get(0);

        ImageList model = new ImageList(itemList, 0);
        ImageViewer imageViewer = new ImageViewer(model.getItemAt(0));
        imageViewer.setImageList(model);

        SpanLabel details = new SpanLabel(firstItem.details.get(), "WebServicesDetails");
        imageViewerContainer.add(BorderLayout.SOUTH, details);
        imageViewerContainer.add(BorderLayout.CENTER, imageViewer);

        // Refresh the details when switching between the images.
        model.addSelectionListener((oldIndex, newIndex)->{
            String currDetails = model.getDetails(newIndex);
            details.setText(currDetails);
            getCurrentForm().revalidate();
        });
        return imageViewerContainer;
    }
    
    /**
     * Image model for the ImageViewer
     */
    private class ImageList implements ListModel<Image>{
        private List<Item> itemList;
        private int selection;
        private EventDispatcher selectionListeners = new EventDispatcher();

        public ImageList(List<Item> itemList, int selection) {
            this.itemList = itemList;
            this.selection = selection;
        }
        
        @Override
        public Image getItemAt(int index) {
            Item item = itemList.get(index);
            String url = item.url.get();
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            Image currImage;
            if (!existsInStorage(getAppHomePath() + fileName)){
                currImage = URLImage.createToStorage(placeholder, fileName, url, URLImage.RESIZE_SCALE_TO_FILL);
            }else{
                currImage = (Image)Storage.getInstance().readObject(fileName);
            }
            return currImage;
        }
        
        @Override
        public void addSelectionListener(SelectionListener l) {
            selectionListeners.addListener(l);
        }

        @Override
        public void removeSelectionListener(SelectionListener l) {
            selectionListeners.removeListener(l);
        }
        @Override
        public int getSelectedIndex() {
            return selection;
        }

        @Override
        public int getSize() {
            return itemList.size();
        }

        @Override
        public void setSelectedIndex(int index) {
            int oldIndex = selection;
            selection = index;
            selectionListeners.fireSelectionEvent(oldIndex, selection);
        }
        
        
        public String getDetails(int index){
            return itemList.get(index).details.get();
        }

        @Override
        public void removeDataChangedListener(DataChangedListener l) {
            
        }
        
        @Override
        public void addDataChangedListener(DataChangedListener l) {
            
        }
        
        @Override
        public void addItem(Image item) {
            
        }
        
        @Override
        public void removeItem(int index) {
            
        }
    }
    
    class ItemList implements PropertyBusinessObject {
        public final Property<String, ItemList> title = new Property<>("title");
        public final IntProperty<ItemList> elements = new IntProperty<>("elements");
        public final Property<String, ItemList> copyright = new Property<>("copyright");
        public final Property<String, ItemList>  page = new Property<>("page");
        public final Property<String, ItemList> nextPage = new Property<>("nextPage");
        public final ListProperty<Map, ItemList> items = new ListProperty<>("items");
        
        private final PropertyIndex idx = new PropertyIndex(this, "Item", title, elements, copyright, page, nextPage, items);
        
        @Override 
        public PropertyIndex getPropertyIndex() {
            return idx;     
        }
    }
    
    class Item implements PropertyBusinessObject{
        public final Property<String, ItemList> title = new Property<>("title");
        public final Property<String, ItemList> details = new Property<>("details");
        public final Property<String, ItemList> url = new Property<>("url");
        public final Property<String, ItemList> thumb = new Property<>("thumb");
        
        private final PropertyIndex idx = new PropertyIndex(this, "Item", title, details, url, thumb);
        
        @Override 
        public PropertyIndex getPropertyIndex() {
            return idx;     
        }
    }
}
