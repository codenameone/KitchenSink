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
import com.codename1.contacts.Contact;
import com.codename1.demos.kitchen.util.Util;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.FlipTransition;
import com.codename1.ui.animations.Transition;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.GenericListCellRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codename1.contacts.ContactsManager.deleteContact;
import static com.codename1.ui.CN.*;
import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the InteractionDialog, Dialog, Sheet, and ToastBar Components.
 * The Dialog are Forms that occupies a part of the screen, and used mostly to display information and to get information from the user.
 *
 * @author Sergey Gerashenko.
 */
public class DialogDemo extends Demo {
    boolean isInteractionDialogOpen = false;
    private List<ToastBar.Status> statusList = new ArrayList<>();
    
    public DialogDemo(Form parentForm) {
        init("Dialog", getGlobalResources().getImage("dialog-demo.png"), parentForm,
                "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/DialogDemo.java");
    }
     
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);

        demoContainer.add(createComponent(getGlobalResources().getImage("interaction-dialog.png"),
                                                                "Interaction Dialog",
                                                                "Unlike a regular dialog the interaction",
                                                                "dialog only looks like a dialog, it resides in the layered pane and can be used to implement features "+
                                                                "where interaction with the background form is still required. Since this code is designed for interaction "+
                                                                "all \"dialogs\" created thru there are modless and never block.",
                                                                e->{
                                                                    showDemo("Interaction Dialog", createInteractionDialogDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("dialog.png"),
                                                                "Dialog",
                                                                "A dialog is a form that occupies a part of",
                                                                "the screen and appears as a modal entity to the developer. Dialogs allow us to prompt users for information "+
                                                                "and rely on the information being available on the next line after the show method.\n\nModality indicates that "+
                                                                "a dialog will block the calling thread even if the calling thread is the EDT. Notice that a dialog will not "+
                                                                "release the block until dispose is called even if show() from another form is called! Events are still "+
                                                                "performed thanks to the Display.invokeAnd Block(java.lang.Runnable) capability of the Display class.\n\nTo "+
                                                                "determine the size of the dialog use the show method that accepts 4 integer values, notice that these "+
                                                                "values accept margin from the four sides than x,y, width and height values!\n\nIt's important to style a Dialog "+
                                                                "using getDialogStyle() or setDialogUIID(java.lang. String) methods rather than styling the dialog object "+
                                                                "directly.\n\nThe Dialog class also includes support for popup dialog which is a dialog type that is positioned "+
                                                                "text to a component or screen area and points an arrow at the location.",
                                                                e->{
                                                                    showDemo("Dialog", createDialogDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("sheet.png"),
                                                                "Sheet",
                                                                "A light-weight dialog that slides up from",
                                                                "the bottom of the screen on mobile devices. Sheets include a \"title\" bar, with a back/close button, a title "+
                                                                "label and a \"commands container\" (getCommandsContainer()) which allows you to insert your own custom "+
                                                                "components (usually buttons) in the upper right. Custom content should be placed inside the content pane "+
                                                                "which can be retrieved via getContentPane()\n\nUsage:\nThe general usage is to create new sheet instance "+
                                                                "(or subclass), then call show() to make it appear over the current form. If a different sheet that is "+
                                                                "currently being displayed, then calling show() will replace it.",
                                                                e->{
                                                                    showDemo("Sheet", createSheetDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("toast-bar.png"),
                                                                "ToastBar",
                                                                "An API to present status messages to the",
                                                                "user in an unobtrusive manner. This is useful if there are background tasks that need to display "+
                                                                "information to the user. E.g.p If a network request fails, of let the user know that \"Jobs are being "+
                                                                "synchronized\"",
                                                                e->{
                                                                    showDemo("ToastBar", createToastBarDemo());
                                                                }));
        return demoContainer;
    }
    
    private Container createInteractionDialogDemo(){
        InteractionDialog dlg = new InteractionDialog("Header", new BorderLayout());
        dlg.add(BorderLayout.CENTER, new SpanLabel("Dialog body", "DialogDemoSpanLabel"));
        Button openClose = new Button("Open/Close", "DialogDemoButton");
        openClose.addActionListener((ee) ->{
            if(isInteractionDialogOpen){ 
                isInteractionDialogOpen = !isInteractionDialogOpen;
                dlg.dispose();
            }else {
                isInteractionDialogOpen = !isInteractionDialogOpen;
                dlg.show(0, Display.getInstance().getDisplayHeight() / 2, 0, 0);
            }   
            
        });        
        return BorderLayout.south(openClose);
    }
    
    private Container createDialogDemo(){
        final Button showDialog = new Button("Show Dialog", "DialogDemoButton");
        final Button showPopup = new Button("Show Popup", "DialogDemoButton");

        ComboBox<Map<String, Object>> combo = new ComboBox<> (
                createListEntry("Fade"),
                createListEntry("Slide Horizontal"),
                createListEntry("Slide Vertical"),
                createListEntry("Flip")
        );

        MultiButton mb1 = new MultiButton();
        mb1.setUIID("DemoMultiButton");
        mb1.setUIIDLine1("DemoMultiLine1");

        MultiButton mb2 = new MultiButton();
        mb2.setUIID("DemoMultiButton");
        mb2.setUIIDLine1("DemoMultiLine1");

        combo.setRenderer(new GenericListCellRenderer<>(mb1, mb2));
        combo.setSelectedIndex(1);

        showDialog.addActionListener(e-> {
            Dialog d = new Dialog("Dialog Title");
            Command ok = new Command("Ok");
            Command cancel = new Command("Cancel");
            SpanLabel body = new SpanLabel("This is the body of the popup", "DialogDemoSpanLabel");
            Command[] cmds = {ok, cancel};
            Transition t = createTransition(combo.getSelectedIndex());
            Dialog.show("Dialog Title", body, cmds, Dialog.TYPE_INFO, null, 0, t);
        });

        showPopup.addActionListener(e-> {
            Dialog d = new Dialog("Popup Title");
            SpanLabel body = new SpanLabel("This is the body of the popup", "DialogDemoSpanLabel");
            d.add(body);
            d.setTransitionInAnimator(createTransition(combo.getSelectedIndex()));
            d.setTransitionOutAnimator(createTransition(combo.getSelectedIndex()));
            d.showPopupDialog(showPopup);
        });

        Container demoContainer = BoxLayout.encloseY(new Label("Choose Transition", "DemoHeaderLabel"), combo, showDialog, showPopup);
        demoContainer.setUIID("Wrapper");

        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createSheetDemo(){
        Container demoContainer = new Container(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE));

        // getAllContacts can take long time so we add infiniteProgress to modify user experience.
        demoContainer.add(BorderLayout.CENTER, new InfiniteProgress());

        // Create new background Thread that will get all the contacts.
        scheduleBackgroundTask(()->{
            Contact contacts[] = Display.getInstance().getAllContacts(true, true, false, true, false, false);

            // Return to the EDT for edit the UI (the UI should be edited only within the EDT).
            callSerially(()->{
                demoContainer.removeAll();
                demoContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                demoContainer.setScrollableY(true);
                for (Contact currentContact : contacts){
                    demoContainer.add(createContactComponent(currentContact));
                    demoContainer.revalidate();
                }
            });
        });
        return demoContainer;
    }

    private Container createToastBarDemo(){
        Button basic = new Button("Basic", "DialogDemoButton");
        Button progress = new Button("Progress", "DialogDemoButton");
        Button expires = new Button("Expires (after 3 seconds)", "DialogDemoButton");
        Button delayed = new Button("Delayed (by 1 second)", "DialogDemoButton");
        Button clear = new Button("Clear All", "DialogDemoButton");
        
        clear.addActionListener(e->{
            for(ToastBar.Status currStatus : statusList){
                currStatus.clear();
            }
            statusList.clear();
        });
        
        basic.addActionListener(e -> {
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setMessage("Hello world");
            statusList.add(status);
            status.show();
            //...  Some time later you must clear the status
            // status.clear();
        });

        progress.addActionListener(e -> {
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setMessage("Hello world");
            status.setShowProgressIndicator(true);
            statusList.add(status);
            status.show();
            // ... Some time later you must clear it
        });

        expires.addActionListener(e -> {
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setMessage("Hello world");
            status.setExpires(3000);  // only show the status for 3 seconds, then have it automatically clear
            status.show();
        });

        delayed.addActionListener(e -> {
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setMessage("Hello world");
            statusList.add(status);
            status.showDelayed(1000); // Wait 1000 ms to show the status
            // ... Some time later, clear the status... this may be before it shows at all
        });
        
        return BoxLayout.encloseY(basic, progress, expires, delayed, clear);
    }

    private Component createContactComponent(Contact contact){
        MultiButton contactComponent = new MultiButton(contact.getDisplayName());
        contactComponent.setUIID("ContactComponent");

        Image contactImage = contact.getPhoto();
        // Set default avatar for contacts without avatar picture.
        if (contactImage == null){
            contactImage = getGlobalResources().getImage("default-contact-pic.jpg");
        }
        contactImage = contactImage.fill(convertToPixels(8), convertToPixels(8));
        contactImage = contactImage.applyMask(Util.getRoundMask(contactImage.getHeight()));
        contactComponent.setIcon(contactImage);

        contactComponent.addActionListener(e->{
            Sheet contactInfo = new Sheet(null, contact.getDisplayName());
            contactInfo.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            contactInfo.add(new Label("Phone: " + contact.getPrimaryPhoneNumber(), "ContactDetails"));
            if (contact.getPrimaryEmail() != null){
                contactInfo.add(new Label("Email: " + contact.getPrimaryEmail(), "ContactDetails"));
            }
            if (contact.getBirthday() != 0){
                contactInfo.add(new Label("Birthday: " + contact.getBirthday(), "ContactDetails"));
            }

            Button call = new Button(FontImage.MATERIAL_CALL, 6f, "ContactsGreenButton");
            call.addActionListener(ev-> dial(contact.getPrimaryPhoneNumber()));

            ShareButton share = new ShareButton();
            share.setUIID("ContactsGreenButton");
            share.setMaterialIcon(FontImage.MATERIAL_SHARE, 6f);  // Change the size of the icon.
            share.setTextToShare(contact.getDisplayName() + " phone: " + contact.getPrimaryPhoneNumber());

            Button delete = new Button(FontImage.MATERIAL_DELETE, 6f, "ContactsRedButton");
            delete.addActionListener(ev->{
                if (Dialog.show("Delete", "This will delete the contact permanently!\nAre you sure?", "Delete", "Cancel")){
                    if (contact.getId()!= null){
                        deleteContact(contact.getId());
                        contactComponent.remove();
                        Display.getInstance().getCurrent().revalidate();
                    }
                }
            });
            Container contactActions = FlowLayout.encloseCenter(call, share, delete);
            contactActions.setUIID("contactActions");
            contactInfo.add(contactActions);
            contactInfo.show();
        });

        return contactComponent;
    }

    private Map<String, Object> createListEntry(String name) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        return entry;
    }

    private Transition createTransition(int selectedIndex){
        switch (selectedIndex){
            case 0:
                return CommonTransitions.createFade(1000);
            case 1:
                return CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 1000);
            case 2:
                return CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 1000);
            case 3:
                return new FlipTransition(-1, 1000);
            default:
                return CommonTransitions.createFade(1000);
        }
    }
}



