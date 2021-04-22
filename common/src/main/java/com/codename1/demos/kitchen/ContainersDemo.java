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
import com.codename1.demos.kitchen.charts.DemoCharts;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.properties.*;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codename1.ui.CN.*;
import static com.codename1.ui.FontImage.createMaterial;
import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the Accordion, InfiniteContainer, SplitPane, and Tabs Containers.
 * The Containers are Components that allow to Contain other Components inside them and to arrange them using layout manager.
 * The Containers are derived from Component class so they able to contain another containers as well.
 *
 * @author Sergey Gerashenko.
 */
public class ContainersDemo extends Demo{
    private List<Component> colorLabelList;
    private Container colorsContainer;

    public ContainersDemo(Form parentForm) {
        init("Containers", getGlobalResources().getImage("containers-demo.png"), parentForm, "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/ContainersDemo.java");
    }

    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);

        demoContainer.add(createComponent(getGlobalResources().getImage("accordion.png"),
                "Accordion",
                "This Accordion ui pattern is a vertically",
                "stacked list of items. Each items can be opened/closed to reveal more content similar to a Tree however "+
                        "unlike the the Tree the Accordion is designed to include containers or arbitrary components rather than model "+
                        "based data.\n\nThis makes the Accordion more convenient as a tool for folding/collapsing UI elements known in "+
                        "advance whereas a tree makes more sense as a tool to map data e.g filesystem structure, XML hierarchy etc.\n\n"+
                        "Note that the Accordion like many composite components in Codename One is scrollable by default which means "+
                        "you should use it within a non-scrollable hierarchy. If you wish to add it into a scrollable Container you "+
                        "should disable it's default scrollability using setScrollable(false).",
                e-> showDemo("Accordion", createAccordionDemo())));

        demoContainer.add(createComponent(getGlobalResources().getImage("infinite-container.png"),
                  "Infinite Container",
                 "This abstract Container can scroll",
                   "indefinitely (or at least until we run out of data). This class uses the "+
                        "InfiniteScrollAdapter to bring more data and the pull to refresh feature to "+
                        "refresh current displayed data.\n\nThe sample code shows the usage of the nestoria "+
                        "API to fill out an infinitely scrolling list.",
                        e-> showDemo("Infinite Container", createInfiniteContainerDemo())));

        demoContainer.add(createComponent(getGlobalResources().getImage("split-pane.png"),
                 "Split Pane",
                 "A split pane can either be horizontal or",
                   "vertical, and provides a draggable divider between two components. If the orientation is HORIZONTAL_SPLIT, "+
                        "then the child components will be laid out horizontally (side by side with a vertical bar as a divider). If "+
                        "the orientation is VERTICAL_SPLIT, then the components are laid out vertically. One above the other.\n\nThe bar "+
                        "divider bar includes to collapse and expand the divider also.",
                        e-> showDemo("Split Pane", createSplitPaneDemo())));

        demoContainer.add(createComponent(getGlobalResources().getImage("tabs.png"),
                  "Tabs",
                 "A component that lets the user switch",
                   "between a group if components by clicking on a tab with a given "+
                        "title and/or icon.\n\nTabs/components are added to a Tabs object by using the addTab and insertTab methods. "+
                        "A tab is represented by an index corresponding to the position it was added in, where the first tab has an "+
                        "index equal to 0 and the last tab has an index equal to the tab count minus 1. The Tabs uses a "+
                        "SingleSelectionModel to represent the set of tab indices and the currently selected index. If the tab "+
                        "count is greater that 0, then there will always be a selected index, which by default will be initialized "+
                        "to the first tab. If the tab count is 0, then the selected index will be -1. A simple Tabs looks like a "+
                        "bit like this.",
                        e-> showDemo("Tabs", createTabsDemo())));

        return demoContainer;
    }

    private Container createAccordionDemo(){
        Accordion accordion = new Accordion();
        accordion.setHeaderUIID("DemoAccordionHeader");
        accordion.setOpenCloseIconUIID("DemoAccordionIcon");
        accordion.setOpenIcon(FontImage.MATERIAL_EXPAND_LESS);
        accordion.setCloseIcon(FontImage.MATERIAL_EXPAND_MORE);
        accordion.addContent("Does this product have what I need?", new SpanLabel("Totally. Totally does", "DemoAccordionLabel"));
        accordion.addContent("Can I use it all the time?", new SpanLabel("Of course you can, we won't stop you", "DemoAccordionLabel"));
        accordion.addContent("Are there any restrictions?", new SpanLabel("Only your imagination my friend. Go for it!", "DemoAccordionLabel"));
        return accordion;
    }

    private Container createInfiniteContainerDemo(){
        final String firstURL =  "https://www.codenameone.com/files/kitchensink/dogs/list.json";
        Image tempPlaceHolder = getGlobalResources().getImage("blurred-puppy.jpg");
        EncodedImage placeholder = EncodedImage.createFromImage(tempPlaceHolder, true);

        InfiniteContainer infiniteContainer = new InfiniteContainer(10) {
            String nextURL = firstURL;

            @Override
            public Component[] fetchComponents(int index, int amount) {
                // pull to refresh resets the position
                if(index == 0) {
                    nextURL = firstURL;
                }
                // nextUrl is null when there is no more data to fetch.
                if(nextURL == null) {
                    return null;
                }

                // Request the data from the server.
                Response<Map> resultData = Rest.get(nextURL).acceptJson().getAsJsonMap();
                if(resultData == null || resultData.getResponseCode() != 200) {
                    callSerially(()-> ToastBar.showErrorMessage("Error code from the server"));
                    return null;
                }
                ItemList itemListData = new ItemList();
                itemListData.getPropertyIndex().populateFromMap(resultData.getResponseData());


                nextURL = itemListData.nextPage.get();
                int itemsCount = itemListData.elements.get();

                if(itemListData.items.asList() == null) {
                    return null;
                }

                ArrayList<Item> itemList = new ArrayList<>();
                for(Map<String, Object> currItemMap : itemListData.items.asList()){
                    Item currItem = new Item();
                    currItem.getPropertyIndex().populateFromMap(currItemMap);
                    itemList.add(currItem);
                }
                Component[] result = new Component[itemsCount];

                for(int i = 0; i < itemsCount; ++i) {
                    // Get all the necessary data.
                    Item currItem = itemList.get(i);

                    String title = currItem.title.get();
                    String url = currItem.url.get();
                    String fileName = url.substring(url.lastIndexOf("/") + 1);
                    URLImage image = URLImage.createToStorage(placeholder, fileName, url, URLImage.RESIZE_SCALE_TO_FILL);

                    // Build the components.
                    ScaleImageLabel imageLabel = new ScaleImageLabel(image){
                        @Override
                        protected Dimension calcPreferredSize() {
                            Dimension dm = super.calcPreferredSize();
                            dm.setHeight(convertToPixels(30));
                            return dm;
                        }
                    };
                    imageLabel.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

                    result[i] = LayeredLayout.encloseIn(imageLabel,
                            BorderLayout.south(new Label(title, "InfiniteComponentTitle")));
                }
                return result;
            }
        };
        return BorderLayout.center(infiniteContainer);
    }
    private Container createTabsDemo(){
        Tabs tabsContainer = new Tabs();
        tabsContainer.setUIID("DemoTabsContainer");
        tabsContainer.addTab("Categories", createMaterial(FontImage.MATERIAL_PIE_CHART, UIManager.getInstance().getComponentStyle("Tab")),
                DemoCharts.createCategoriesContainer());

        tabsContainer.addTab("Annual review", createMaterial(FontImage.MATERIAL_SHOW_CHART, UIManager.getInstance().getComponentStyle("Tab")),
                DemoCharts.createAnnualContainer());
        return tabsContainer;
    }

    private Container createSplitPaneDemo(){
        Button flow = new Button("Flow Layout");
        flow.setUIID("DemoButton");
        flow.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new FlowLayout());
            colorsContainer.animateLayout(1000);
        });

        Button flowCenter = new Button("Flow Center Layout");
        flowCenter.setUIID("DemoButton");
        flowCenter.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new FlowLayout(Component.CENTER));
            colorsContainer.animateLayout(1000);
        });

        Button border = new Button("border Layout");
        border.setUIID("DemoButton");
        border.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.removeAll();
            colorsContainer.setLayout(new BorderLayout());
            colorsContainer.add(BorderLayout.CENTER, colorLabelList.get(0)).
                    add(BorderLayout.WEST, colorLabelList.get(1)).
                    add(BorderLayout.EAST, colorLabelList.get(2)).
                    add(BorderLayout.NORTH, colorLabelList.get(3)).
                    add(BorderLayout.SOUTH, colorLabelList.get(4));
            colorsContainer.animateLayout(1000);
        });

        Button absoluteBorder = new Button("Absolute Border Layout");
        absoluteBorder.setUIID("DemoButton");
        absoluteBorder.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.removeAll();
            colorsContainer.setLayout(new BorderLayout(CENTER_BEHAVIOR_CENTER));
            colorsContainer.add(BorderLayout.CENTER, colorLabelList.get(0)).
                    add(BorderLayout.WEST, colorLabelList.get(1)).
                    add(BorderLayout.EAST, colorLabelList.get(2)).
                    add(BorderLayout.NORTH, colorLabelList.get(3)).
                    add(BorderLayout.SOUTH, colorLabelList.get(4));
            colorsContainer.animateLayout(1000);
        });

        Button boxX = new Button("Box X Layout");
        boxX.setUIID("DemoButton");
        boxX.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new BoxLayout(BoxLayout.X_AXIS));
            colorsContainer.animateLayout(1000);
        });


        Button boxY = new Button("Box Y Layout");
        boxY.setUIID("DemoButton");
        boxY.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            colorsContainer.animateLayout(1000);
        });

        Button grid = new Button("Grid Layout");
        grid.setUIID("DemoButton");
        grid.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new GridLayout(1, 1));
            colorsContainer.animateLayout(1000);
        });

        Button simpleTable = new Button("Table Layout(simple)");
        simpleTable.setUIID("DemoButton");
        simpleTable.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new TableLayout(3, 2));
            colorsContainer.removeAll();
            colorsContainer.addAll(colorLabelList.get(0),
                    colorLabelList.get(1),
                    colorLabelList.get(2),
                    colorLabelList.get(3),
                    colorLabelList.get(4));


            colorsContainer.animateLayout(1000);
        });

        Button complexTable = new Button("Table Layout(complex)");
        complexTable.setUIID("DemoButton");
        complexTable.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.removeAll();
            buildComplexTableUI(colorsContainer);
            colorsContainer.animateLayout(1000);
        });

        Button layered = new Button("Layered Layout");
        layered.setUIID("DemoButton");
        layered.addActionListener(e-> {
            resetMargin(colorsContainer);
            colorsContainer.setLayout(new LayeredLayout());

            // Increase the margin by 3 mm for every Component in the container for better
            //   visual effect of the LayeredLayout.
            setMarginForLayeredLayout(colorsContainer);
            colorsContainer.animateLayout(1000);
        });

        
        // Make a Button container 
        Container buttonList = BoxLayout.encloseY(flow,
                flowCenter,
                border,
                absoluteBorder,
                boxX,
                boxY,
                grid,
                simpleTable,
                complexTable,
                layered);
        buttonList.setScrollableY(true);


        // Make some blank Labels with background colors from the CSS file.
        colorLabelList = new ArrayList<>();
        colorLabelList.add(new Label("                    ", "RedLabel"));
        colorLabelList.add(new Label("                    ", "BlueLabel"));
        colorLabelList.add(new Label("                    ", "GreenLabel"));
        colorLabelList.add(new Label("                    ", "OrangeLabel"));
        colorLabelList.add(new Label("                    ", "PurpleLabel"));

        // Make an anonymous claas that override calcPreferredSize to fit exactly a half of the screen.
        // Alternatively you could use TableLayout instead of BorderLayout where i could explicitly define the height in percentages.
        // or GridLayout that would divide the ContentPane by 2 for every Component within it.
        colorsContainer = new Container (new BoxLayout(BoxLayout.Y_AXIS));

        colorsContainer.addAll( colorLabelList.get(0),
                colorLabelList.get(1),
                colorLabelList.get(2),
                colorLabelList.get(3),
                colorLabelList.get(4)
        );
        colorsContainer.setShouldCalcPreferredSize(true);

        return new SplitPane(new SplitPane.Settings().orientation(SplitPane.VERTICAL_SPLIT), colorsContainer, buttonList);

    }

    private static class ItemList implements PropertyBusinessObject {
        public final Property<String, ItemList> title = new Property<>("title");
        public final IntProperty<ItemList> elements = new IntProperty<>("elements");
        public final Property<String, ItemList> copyright = new Property<>("copyright");
        public final Property<String, ItemList>  page = new Property<>("page");
        public final Property<String, ItemList> nextPage = new Property<>("nextPage");
        public final ListProperty<Map<String, Object>, ItemList> items = new ListProperty<>("items");

        private final PropertyIndex idx = new PropertyIndex(this, "Item", title, elements, copyright, page, nextPage, items);

        @Override
        public PropertyIndex getPropertyIndex() {
            return idx;
        }
    }

    private static class Item implements PropertyBusinessObject{
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

    // Reset the margin for all the components inside the given container.
    private void resetMargin(Container colorsContainer){
        float margin = 0.5f;
        for(Component cmp : colorsContainer){
            cmp.getAllStyles().setMargin(margin, margin, margin, margin);
        }
    }

    private void setMarginForLayeredLayout(Container colorsContainer){
        float margin = 0f;
        for(Component cmp : colorsContainer){
            cmp.getAllStyles().setMargin(margin, margin, margin, margin);
            margin += 3f;
        }
    }

    private void buildComplexTableUI(Container colorsContainer){
        TableLayout tl = new TableLayout(2, 3);
        colorsContainer.setLayout(tl);
        colorsContainer.add(tl.createConstraint().widthPercentage(20),
                colorLabelList.get(0));

        colorsContainer.add(tl.createConstraint().
                        horizontalSpan(2).heightPercentage(80).
                        verticalAlign(Component.CENTER).
                        horizontalAlign(Component.CENTER),
                colorLabelList.get(1));

        colorsContainer.add(colorLabelList.get(2));

        colorsContainer.add(tl.createConstraint().
                        widthPercentage(60).
                        heightPercentage(20),
                colorLabelList.get(3));

        colorsContainer.add(tl.createConstraint().
                        widthPercentage(20),
                colorLabelList.get(4));
    }

}