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

import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.GenericListCellRenderer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.codename1.ui.util.Resources.getGlobalResources;

/**
 * Class that demonstrate the usage of the ComboBox and PickerComponent components.
 * The Selection components are lists of items that allow to select one or more items at a time.
 *
 * @author Sergey Gerashenko.
 */
public class SelectionDemo extends Demo{
    
    public SelectionDemo(Form parentForm) {
        init("Selection", getGlobalResources().getImage("selection-demo.png"), parentForm, "https://github.com/sergeyCodenameOne/KitchenSinkDemo/blob/master/src/com/codename1/demos/kitchen/SelectionDemo.java");
    }
     
    @Override
    public Container createContentPane() {
        Container demoContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS), "DemoContainer");
        demoContainer.setScrollableY(true);
        
        demoContainer.add(createComponent(getGlobalResources().getImage("combo-box.png"),
                                                                "Combo Box",
                                                                "ComboBox is a list that allows only one",
                                                                "selection at a time, when a user clicks * the code ComboBox a popup button with the full list of elements allows the "+
                                                                "selection of * a single element. The ComboBox is a driven by the list model and allows all the rendere * features of the "+
                                                                "list as well.", e->{
                                                                    showDemo("Combo Box", createComboBoxDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("date-picker.png"),
                                                                "Date Picker",
                                                                "Date Picker is a PickerComponent use",
                                                                "PickerComponent.createDate(null).label(\"Se- lect Birthday\")", e->{
                                                                    showDemo("Date Picker", createDatePickerDemo());                                           
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("time-picker.png"),
                                                                "Time Picker",
                                                                "Time Picker is a PickerComponent use",
                                                                "PickerComponent.createTime(null).label(\"Se- lect Alarm time\")", e->{
                                                                    showDemo("Time Picker", createTimePickerDemo());     
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("date-time-picker.png"),
                                                                "Date Time Picker",
                                                                "DateTime Picker is a PickerComponent use ",
                                                                "PickerComponent.createDateTime(null).label (\"Select Meeting schedule\")", e->{
                                                                    showDemo("Date Time Picker", createDateTimePickerDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("minute-picker.png"),
                                                                "Minute Duration Picker",
                                                                "Minute Picker is a PickerComponent use",
                                                                "PickerComponent.createDurationMMInutes (0).label(\"Select Duration\")", e->{
                                                                    showDemo("Minute Duration Picker", createMinuteDurationPickerDemo());
                                                                }));
        
        demoContainer.add(createComponent(getGlobalResources().getImage("hour-picker.png"),
                                                                "Minute, Hour, Duration Picker",
                                                                "Hour Minute Picker is a PickerComponent",
                                                                "use PickerComponent.createDurationHoursMinutes(0,0).label(\"Select Duration\")", e->{
                                                                    showDemo("Minute, Hour, Duration Picker", createMinuteHourPickerDemo());                                                           
                                                                }));
        return demoContainer;
    }
    
    private Container createComboBoxDemo(){
        Container demoContainer = new Container(new BorderLayout());
        ComboBox<Map<String, Object>> combo = new ComboBox<> (
            createListEntry("A Game of Thrones", "1996", "4.45"),
            createListEntry("A Clash Of Kings", "1998", "4.41"),
            createListEntry("A Storm Of Swords", "2000", "4.54"),
            createListEntry("A Feast For Crows", "2005", "4.14"),
            createListEntry("A Dance With Dragons", "2011", "4.33"),
            createListEntry("The Winds of Winter", "2016", "4.40"),
            createListEntry("A Dream of Spring", "Unpublished", "unknown")
        );

        
        MultiButton mb1 = new MultiButton();
        mb1.setUIID("DemoMultiButton");
        mb1.setUIIDLine1("DemoMultiLine1");
        mb1.setUIIDLine2("DemoMultiLine2");
        
        MultiButton mb2 = new MultiButton();
        mb2.setUIID("DemoMultiButton");
        mb2.setUIIDLine1("DemoMultiLine1");
        mb2.setUIIDLine2("DemoMultiLine2");
        
        combo.setRenderer(new GenericListCellRenderer<>(mb1, mb2));
        combo.setSelectedIndex(1);
        demoContainer.add(BorderLayout.CENTER, BoxLayout.encloseY(combo));
        
        Button showRating = new Button("Show Rating", "DemoButton");
        showRating.addActionListener(e->{
            Map selectedItem = combo.getSelectedItem();
            ToastBar.showInfoMessage((String)selectedItem.get("Line1") + " rating is: " + (String)selectedItem.get("rating"));
        });
        demoContainer.add(BorderLayout.SOUTH, showRating);
        
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createDatePickerDemo(){
        PickerComponent datePicker = PickerComponent.createDate(null).label("Select Birthday: ");
        datePicker.setUIID("DemoPicker");
        
        Button save = new Button("Save Birthday", "DemoButton");
        save.addActionListener(e->{
            ToastBar.showInfoMessage("Birthday was saved: " + datePicker.getPicker().getText());
        });
        Container demoContainer = BorderLayout.center(BoxLayout.encloseY(datePicker));
        demoContainer.add(BorderLayout.SOUTH, save);
        
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createTimePickerDemo(){
        Calendar calendar =  Calendar.getInstance(TimeZone.getDefault());
        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR);
        
        PickerComponent timePicker = PickerComponent.createTime(hours * 60 + minutes).label("Select Alarm Time ");
        timePicker.setUIID("DemoPicker");
        Button setAlarm = new Button("Set Alarm", "DemoButton");
        setAlarm.addActionListener(e->{
            ToastBar.showInfoMessage("Alarm set for: " + timePicker.getPicker().getText());
        });
        Container demoContainer = BorderLayout.center(BoxLayout.encloseY(timePicker));
        demoContainer.add(BorderLayout.SOUTH, setAlarm);
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createDateTimePickerDemo(){        
        PickerComponent meetingPicker = PickerComponent.createDateTime(null).label("Select meeting schedule");
        meetingPicker.setUIID("DemoPicker");
        Button scheduleMeeting = new Button("Schedule Meeting", "DemoButton");
        scheduleMeeting.addActionListener(e->{
            ToastBar.showInfoMessage("Meeting was scheduled at: " + meetingPicker.getPicker().getText());
        });
        Container demoContainer = BorderLayout.center(BoxLayout.encloseY(meetingPicker));
        demoContainer.add(BorderLayout.SOUTH, scheduleMeeting);
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
        
    }
    
    private Container createMinuteDurationPickerDemo(){
        PickerComponent durationPicker = PickerComponent.createDurationMinutes(0).label("Select Duration");
        durationPicker.setUIID("DemoPicker");
        
        Button setTimer = new Button("Set Timer", "DemoButton");
        setTimer.addActionListener(e->{
            ToastBar.showInfoMessage("Timer set for: " + durationPicker.getPicker().getText());
        });
        Container demoContainer = BorderLayout.center(BoxLayout.encloseY(durationPicker));
        demoContainer.add(BorderLayout.SOUTH, setTimer);
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Container createMinuteHourPickerDemo(){
        PickerComponent durationPicker = PickerComponent.createDurationHoursMinutes(0, 0).label("Select Duration");
        durationPicker.setUIID("DemoPicker");

        Button setTimer = new Button("Set Timer", "DemoButton");
        setTimer.addActionListener(e->{
            ToastBar.showInfoMessage("Timer set for: " + durationPicker.getPicker().getText());
        });
        Container demoContainer = BorderLayout.center(BoxLayout.encloseY(durationPicker));
        demoContainer.add(BorderLayout.SOUTH, setTimer);
        demoContainer.setUIID("Wrapper");
        return BoxLayout.encloseY(demoContainer);
    }
    
    private Map<String, Object> createListEntry(String name, String date, String rating) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("Line1", name);
        entry.put("Line2", date);
        entry.put("rating", rating);

        return entry;
    }
    
}
