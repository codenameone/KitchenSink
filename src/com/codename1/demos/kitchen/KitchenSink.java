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


import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.ComponentAnimation;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

public class KitchenSink  {
    private Resources res;
    private Form currentForm;
    private Container tabletSurface;
    private Command gridCommand;
    private Command listCommand;

    private Object imageMask;
    private int maskWidth;
    private int maskHeight;
    private Object circleMask;
    private int circleMaskWidth;
    private int circleMaskHeight;

    private int[] colors;
    private Image[] colorBottoms;
    private int currentColor;
    
    public void init(Object context){
        // use 2 network threads for slightly faster networking but not too much to overburden the UI
        NetworkManager.getInstance().updateThreadCount(2);
        res = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);
        Dialog.setDefaultBlurBackgroundRadius(10);
        Log.bindCrashProtection(false);
    }
        
    private void showDemoInformation(Form back, Demo d) {
        Form f = new Form("Information", new BorderLayout());
        Button sourceCode = new Button("View Source");
        FontImage.setMaterialIcon(sourceCode, FontImage.MATERIAL_WEB);
        sourceCode.addActionListener(e -> Display.getInstance().execute(d.getSourceCodeURL()));
        f.add(BorderLayout.CENTER, new SpanLabel(d.getDescription())).
                add(BorderLayout.SOUTH, sourceCode);
        f.getToolbar().setBackCommand("", e -> back.showBack());
        f.show();
    }
    
    private DemoComponent createDemoButton(Demo d) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(), d.getDemoIcon(), imageMask, 
                maskWidth, maskHeight, colorBottoms[currentColor], 
                circleMask, res.getImage("circle-line.png"), circleMaskWidth, circleMaskHeight,
                "Blank" + (currentColor + 1));
        currentColor++;
        if(currentColor == colorBottoms.length) {
            currentColor = 0;
        }
        dc.addActionListener(e -> {
            if(Display.getInstance().isTablet()) {
                tabletSurface.getAnimationManager().flushAnimation(() -> {
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemo(Display.getInstance().getCurrent()), 
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemo(f));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if(d.onBack()){
                        previous.showBack();
                    }
                });
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
                    showDemoInformation(f, d);
                });
                f.show();
            }
        });
        return dc;
    }
        
    private void showSplashAnimation() {
        Form splash = new Form(new LayeredLayout());
        splash.setUIID("Splash");
        splash.getContentPane().setUIID("Container");
        splash.getToolbar().setUIID("Container");
        ScaleImageLabel iconBackground = new ScaleImageLabel(res.getImage("codenameone-icon-background.png"));
        iconBackground.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Container centerBackground = BorderLayout.center(iconBackground);
        splash.add(centerBackground);
        Label iconForeground = new Label(res.getImage("codenameone-icon-foreground.png"));
        Container centerIcon = BorderLayout.centerAbsolute(iconForeground);
        splash.add(centerIcon);
        
        splash.show();
        Display.getInstance().callSerially(() -> {
            ((BorderLayout)centerBackground.getLayout()).setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
            centerBackground.setShouldCalcPreferredSize(true);
            centerBackground.animateLayoutAndWait(350);
            
            iconForeground.remove();
            iconBackground.remove();
            centerIcon.remove();
            Container layers = LayeredLayout.encloseIn(
                    new Label(iconBackground.getIcon(), "CenterIcon"), 
                    new Label(iconForeground.getIcon(), "CenterIcon"));
            Container boxy = BoxLayout.encloseY(layers);
            Label placeholder = new Label();
            placeholder.setShowEvenIfBlank(true);
            Label kitchenSink = new Label("KitchenSink", "SplashTitle");
            Component.setSameHeight(placeholder, kitchenSink);
            Component.setSameWidth(placeholder, kitchenSink, boxy);
            centerBackground.add(BorderLayout.CENTER, boxy);
            splash.revalidate();
            Display.getInstance().callSerially(() -> {
                placeholder.setText(" ");
                boxy.add(placeholder);
                boxy.setShouldCalcPreferredSize(true);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(placeholder, kitchenSink, CommonTransitions.createFade(500));
                
                Label newPlaceholder = new Label(" ");
                Label byCodenameOne = new Label("by Codename One", "SplashSubTitle");
                Component.setSameHeight(newPlaceholder, byCodenameOne);
                Component.setSameWidth(newPlaceholder, byCodenameOne);
                boxy.add(newPlaceholder);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(newPlaceholder, byCodenameOne, CommonTransitions.createFade(500));
                
                byCodenameOne.setY(splash.getHeight());                
                kitchenSink.setY(splash.getHeight());
                layers.setY(splash.getHeight());
                boxy.setHeight(splash.getHeight());
                
                boxy.animateUnlayoutAndWait(450, 20);
                splash.setTransitionOutAnimator(CommonTransitions.createEmpty());
                
                // create image masks for card effects
                Image mask = res.getImage("card-full.png");
                maskWidth = mask.getWidth();
                maskHeight = mask.getHeight() / 4 * 3;
                Image top = mask.subImage(0, 0, maskWidth, maskHeight, true);
                Image bottom = mask.subImage(0, maskHeight, maskWidth, mask.getHeight() / 4, true);
                imageMask = top.createMask();
                
                Image circleMaskImage = res.getImage("circle.png");
                circleMask = circleMaskImage.createMask();
                circleMaskWidth = circleMaskImage.getWidth();
                circleMaskHeight = circleMaskImage.getHeight();
                
                colorBottoms = new Image[7];
                colors = new int[colorBottoms.length];
                Object bottomMask = bottom.createMask();
                for(int iter = 0 ; iter < colorBottoms.length ; iter++) {
                    colors[iter] = splash.getUIManager().getComponentStyle("Blank" + (iter + 1)).getBgColor();
                    colorBottoms[iter] = Image.createImage(bottom.getWidth(), bottom.getHeight(), 0xff000000 | colors[iter]);
                    colorBottoms[iter] = colorBottoms[iter].applyMask(bottomMask);
                }
                
                showMainUI();
            });
        });
    }
    
    public void start(){
        if(getAppstoreURL() != null) {
            RatingWidget.bindRatingListener(180000, getAppstoreURL(), "apps@codenameone.com");
        }
        if(currentForm != null && !(currentForm instanceof Dialog)) {
            currentForm.show();
            return;
        }
        showSplashAnimation();
    }

    private void showMainUI() {
        final Form f = new Form("KitchenSink", new BorderLayout());
        
        Demo[] demos = new Demo[] {
            new Layouts(), new WebServices(), 
            new ClockDemo(),
            new Themes(), new Contacts(),
            new Input(), 
            new Video(), new SalesDemo(),
        };
        
        for(Demo d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");
        
        if(Display.getInstance().isTablet()) {
            Toolbar.setPermanentSideMenu(true);
            f.getToolbar().addComponentToSideMenu(dukeContainer);
            for(Demo d : demos) {
                f.getToolbar().addComponentToSideMenu(createDemoButton(d));
            }
            tabletSurface = f.getContentPane();
            f.add(BorderLayout.CENTER, demos[0].createDemo(f));
            f.show();
            return;
        }
        
        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if(Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for(Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for(Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);
        
        f.getToolbar().addSearchCommand(e -> {
            String t = (String)e.getSource();
            if(t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for(Component c : cnt) {
                DemoComponent mb = (DemoComponent)c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if(cnt.getAnimationManager().isAnimating()){
                return;
            }
            if(!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true); 
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for(Component c : cnt) {
                    DemoComponent mb = (DemoComponent)c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();
                
                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for(Component c : cnt) {
                    DemoComponent mb = (DemoComponent)c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);
                
                cnt.animateLayout(300);
                                
            }
        });
        
        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if(!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false); 
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for(Component c : cnt) {
                    DemoComponent mb = (DemoComponent)c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                
                offset = 0;
                for(Component c : cnt) {
                    DemoComponent mb = (DemoComponent)c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if(cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne.com", 
                FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Getting Started", FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Developer Guide", FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/files/developer-guide.pdf"));
        f.getToolbar().addMaterialCommandToSideMenu("JavaDoc (Reference)", FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/javadoc/"));
        f.getToolbar().addMaterialCommandToSideMenu("Source Code", FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://github.com/codenameone/KitchenSink"));
        if(Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About", 
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "KitchenSink provides an overview of the core Codename One capaiblities. "
                            + "Codename One allows Java developers to create native mobile applications that work everywhere!", "OK", null);
                });
        
        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for(Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for(Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }
    
    public  String getAppstoreURL() {
        if(Display.getInstance().getPlatformName().equals("ios")) {
            return "https://itunes.apple.com/us/app/kitchen-sink-codename-one/id635048865";
        }
        if(Display.getInstance().getPlatformName().equals("and")) {
            return "https://play.google.com/store/apps/details?id=com.codename1.demos.kitchen";
        }
        return null;
    }
    
    public void stop(){
        currentForm = Display.getInstance().getCurrent();
        RatingWidget.suspendRating();
    }
    
    public void destroy(){
    }
}
