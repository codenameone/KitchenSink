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

package com.codename1.demos.kitchen.charts.models;

import com.codename1.charts.models.XYSeries;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableModel;
import com.codename1.util.SuccessCallback;

/**
 * A tool to edit an XYSeries
 *
 * @author Shai Almog
 */
public class XYSeriesEditor extends Container {
    private XYSeries xy;
    private static final String[] SERIES_COLUMN_NAMES = {"X", "Y", "Remove"};
    private static final String[] ANNOTATION_COLUMN_NAMES = {"Annotation", "X", "Y", "Remove"};
    
    
    public void init(XYSeries xy, SuccessCallback<String> titleChanged) {
        setScrollableY(true);
        this.xy = xy;
        TextField title = new TextField(xy.getTitle(), "Series Title", 20, TextField.ANY);
        title.addActionListener(e -> {
            xy.setTitle(title.getText());
            if(titleChanged != null) {
                titleChanged.onSucess(title.getText());
            }
        });
        
        Table series = new RemoveTable(new TableModel() {
            @Override
            public int getRowCount() {
                return xy.getItemCount();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public String getColumnName(int i) {
                return SERIES_COLUMN_NAMES[i];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public Object getValueAt(int row, int column) {
                switch(column) {
                    case 0:
                        return xy.getX(row);
                    case 1: 
                        return xy.getY(row);
                    default:
                        return null;
                }
            }

            @Override
            public void setValueAt(int row, int column, Object o) {
                switch(column) {
                    case 0:
                        double y = xy.getY(row);
                        xy.remove(row);
                        xy.add(row, Double.parseDouble(o.toString()), y);
                        break;
                    case 1: 
                        double x = xy.getX(row);
                        xy.remove(row);
                        xy.add(row, x, Double.parseDouble(o.toString()));
                        break;
                }
            }

            @Override
            public void addDataChangeListener(DataChangedListener d) {                
            }

            @Override
            public void removeDataChangeListener(DataChangedListener d) {
            }
        }, (row, column) -> xy.remove(row));
        series.setScrollable(false);

        Table annotations = new RemoveTable(new TableModel() {
            @Override
            public int getRowCount() {
                return xy.getAnnotationCount();
            }

            @Override
            public int getColumnCount() {
                return ANNOTATION_COLUMN_NAMES.length;
            }

            @Override
            public String getColumnName(int i) {
                return ANNOTATION_COLUMN_NAMES[i];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public Object getValueAt(int row, int column) {
                switch(column) {
                    case 0:
                        return xy.getAnnotationAt(row);
                    case 1:
                        return xy.getAnnotationX(row);
                    case 2:
                        return xy.getAnnotationY(row);
                    default:
                        return null;
                }
            }

            @Override
            public void setValueAt(int row, int column, Object o) {
                String a = xy.getAnnotationAt(row);
                double x = xy.getAnnotationX(row);
                double y = xy.getAnnotationY(row);
                switch(column) {
                    case 0:
                        xy.removeAnnotation(row);
                        xy.addAnnotation(o.toString(), row, x, y);
                        break;
                    case 1:
                        xy.removeAnnotation(row);
                        xy.addAnnotation(a, row, Double.parseDouble(o.toString()), y);
                        break;
                    case 2:
                        xy.removeAnnotation(row);
                        xy.addAnnotation(a, row, x, Double.parseDouble(o.toString()));
                        break;
                }
            }

            @Override
            public void addDataChangeListener(DataChangedListener d) {
            }

            @Override
            public void removeDataChangeListener(DataChangedListener d) {
            }
        }, (row, column) -> xy.removeAnnotation(row));
        annotations.setScrollable(false);
        
        Button addSeries = new Button("+");
        Button addAnnotation = new Button("+");
        
        addSeries.addActionListener(e -> {
            xy.add(0, 0);
            series.setModel(series.getModel());
        });
        
        addAnnotation.addActionListener(e -> {
            xy.add(0, 0);
            series.setModel(series.getModel());
        });
        
        add("Series Title").
                add(title).
                add(BorderLayout.center(new Label("Series")).
                        add(BorderLayout.EAST, addSeries)).
                add(series).
                add(BorderLayout.center(new Label("Annotations")).
                        add(BorderLayout.EAST, addAnnotation)).
                add(annotations);
    }
    
    public XYSeriesEditor() {
        super(BoxLayout.y());
    }
}
