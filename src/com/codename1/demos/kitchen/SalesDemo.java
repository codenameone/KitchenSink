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

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.views.PieChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;

/**
 * Demonstrates a typical chart/graph UI that is editable using Tables. Common for business applications
 *
 * @author Shai Almog
 */
public class SalesDemo extends Demo {
    private static final String[] DEFAULT_COLUMNS =  {
        "Category", "Sales"
    };
    private static final Object[][] DEFAULT_DATA = {
        { "Products", new Double(500000)},
        { "Virtual", new Double(300000) },
        { "Services", new Double(200000) }
    };
    private static final String[] DEFAULT_COLUMNS_LINE =  {
        "Year", "Sales"
    };
    private static final Object[][] DEFAULT_DATA_LINE = {
        { new Double(2011), new Double(2500000)},
        { new Double(2012), new Double(2800000)},
        { new Double(2013), new Double(3000000)},
        { new Double(2014), new Double(2900000) },
        { new Double(2015), new Double(3300000) },
        { new Double(2016), new Double(3600000) }
    };
    private static final int[] COLORS = {0x9688, 0x3f51b5, 0x9c27b0};
    
    public String getDisplayName() {
        return "Sales";
    }

    @Override
    public String getDescription() {
        return "Demonstrates a typical chart/graph UI that is editable using Tables. Common for business applications";
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/SalesDemo.java";
    }
    
    public Image getDemoIcon() {
        return getResources().getImage("charts.png");
    }

    boolean inUpdate;

    private void updateAxis(TableModel tm, int col, CategorySeries cs) {
        cs.clear();
        for(int iter = 0 ; iter < tm.getRowCount() ; iter++) {
            Object tt = tm.getValueAt(iter, 1);
            double v;
            if(tt instanceof Double) {
                v = ((Double)tt).doubleValue();
            } else {
                v = L10NManager.getInstance().parseCurrency((String)tt);
                tm.setValueAt(iter, col, new Double(v));
            }
            cs.add((String)tm.getValueAt(iter, 0), v);
        }
    }
    
    private void updateSeriesFromTable(TableModel tm, XYSeries cs) {
        cs.clear();
        for(int iter = 0 ; iter < tm.getRowCount() ; iter++) {
            Object tt = tm.getValueAt(iter, 1);
            double v;
            if(tt instanceof Double) {
                v = ((Double)tt).doubleValue();
            } else {
                v = L10NManager.getInstance().parseCurrency((String)tt);
                tm.setValueAt(iter, 1, new Double(v));
            }
            double x = asDouble(tm, iter, 0);
            cs.add(x, v);
        }
    }
    
    private Container encloseInMaximize(Container parent, Component cmp) {
        Button max = new Button();
        max.setUIID("Label");
        FontImage.setMaterialIcon(max, FontImage.MATERIAL_FULLSCREEN);
        Container maximize = LayeredLayout.encloseIn(cmp, FlowLayout.encloseRight(max));
        Image originalIcon = max.getIcon();
        Layout original = parent.getLayout();
        
        max.addActionListener(e -> {
            // should maximize
            if(max.getIcon() == originalIcon) {
                parent.setLayout(BoxLayout.y());
                FontImage.setMaterialIcon(max, FontImage.MATERIAL_FULLSCREEN_EXIT);
                for(Component c : parent) {
                    if(c != maximize) {
                        c.setHidden(true);
                    }
                }
                parent.setShouldCalcPreferredSize(true);
                parent.animateLayoutAndWait(400);
            } else {
                parent.setLayout(original);
                max.setIcon(originalIcon);
                for(Component c : parent) {
                    if(c != maximize) {
                        c.setHidden(false);
                    }
                }
                parent.animateLayoutAndWait(400);
            }
        });
        return maximize;
    } 
    
    private Container encloseInMaximizableGrid(Component cmp1, Component cmp2) {
        GridLayout gl = new GridLayout(2, 1);
        Container grid = new Container(gl);
        gl.setHideZeroSized(true);
        
        grid.add(encloseInMaximize(grid, cmp1)).
                add(encloseInMaximize(grid, cmp2));
        return grid;
    }
    
    private Table createTable(String[] columns, Object[][] tableData) {
        TableModel tm = new DefaultTableModel(columns, tableData) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        Table data = new Table(tm) {
            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) {
                Component cmp = super.createCell(value, row, column, editable); ;
                if(row > -1) {
                    if(row % 2 != 0) {
                        cmp.setUIID("TableCellOdd");
                    } 
                    TextArea t= (TextArea)cmp;
                    if(column == 1) {
                        t.setConstraint(TextArea.DECIMAL);
                        t.setText(L10NManager.getInstance().formatCurrency(((Double)value).doubleValue()));
                        t.addActionListener(e -> {
                            tm.setValueAt(row, column, L10NManager.getInstance().parseCurrency(t.getText()));
                        });
                    } else {
                        t.addActionListener(e -> tm.setValueAt(row, column, t.getText()));
                    }
                    Container centeredContent = FlowLayout.encloseCenter(cmp);
                    centeredContent.setUIID(cmp.getUIID());
                    return centeredContent;
                }
                return cmp;
            }

            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint c = super.createCellConstraint(value, row, column); 
                return c.widthPercentage(50);
            }
        };
        data.setDrawBorder(false);
        data.setScrollableY(true);
        return data;
    }

    private static int darkerColor(int color, int factor) {
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;
        r = Math.max(0, r - factor);
        g = Math.max(0, g - factor);
        b = Math.max(0, b - factor);
        return ((r << 16) & 0xff0000) | ((g << 8) & 0xff00) | (b & 0xff);
    }

    private DefaultRenderer createChartRenderer(TableModel tm) {
        DefaultRenderer renderer = new DefaultRenderer();
        for(int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setGradientEnabled(true);
            r.setGradientStart(0, color);
            int cc = darkerColor(color, -50);
            r.setGradientStop(0, cc);
            renderer.addSeriesRenderer(r);
        }
        renderer.setLabelsColor(0);
        renderer.setBackgroundColor(0xffffffff);
        renderer.setApplyBackgroundColor(true);
        renderer.setLabelsTextFont(Font.createTrueTypeFont("native:MainThin", "native:MainThin"));
        renderer.setLabelsTextSize(Display.getInstance().convertToPixels(5));
        return renderer;
    }

    double asDouble(TableModel tm, int row, int col) {
        Object v = tm.getValueAt(row, col);
        if(v instanceof Double) {
            return ((Double)v).doubleValue();
        }
        if(col == 1) {
            return L10NManager.getInstance().parseCurrency(v.toString());
        }
        return Double.parseDouble(v.toString());
    }
    
    
    void updateRendererMinMax(TableModel tm, XYMultipleSeriesRenderer renderer) {
        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymin = Double.MAX_VALUE;
        double ymax = Double.MIN_VALUE;
        for(int iter = 0 ; iter < tm.getRowCount() ; iter++) {
            double currentX = asDouble(tm, iter, 0);
            double currentY = asDouble(tm, iter, 1);
            xmin = Math.min(xmin, currentX);
            xmax = Math.max(xmax, currentX);
            ymin = Math.min(ymin, currentY);
            ymax = Math.max(ymax, currentY);
        }
        renderer.setXAxisMin(xmin);
        renderer.setXAxisMax(xmax);
        renderer.setYAxisMin(ymin);
        renderer.setYAxisMax(ymax);
    }
    
    private XYMultipleSeriesRenderer createChartMultiRenderer(TableModel tm) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        for(int color : COLORS) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
            r.setFillPoints(true);
            r.setLineWidth(5);
            r.setPointStyle(PointStyle.CIRCLE);
        }
        renderer.setPointSize(5f);
        renderer.setLabelsColor(0);
        renderer.setBackgroundColor(0xffffffff);
        renderer.setApplyBackgroundColor(true);
        renderer.setAxesColor(COLORS[0]);
        renderer.setLabelsTextFont(Font.createTrueTypeFont("native:MainThin", "native:MainThin"));
        renderer.setLabelsTextSize(Display.getInstance().convertToPixels(2));

        renderer.setXTitle(DEFAULT_COLUMNS_LINE[0]);
        renderer.setYTitle(DEFAULT_COLUMNS_LINE[1]);
        updateRendererMinMax(tm, renderer);
        renderer.setAxesColor(0xcccccc);
        renderer.setLabelsColor(0);
        renderer.setXLabels(5);
        renderer.setYLabels(5);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        
        renderer.setMargins(new int[] {0, 0, 0, 0});
        renderer.setMarginsColor(0xffffff);
        return renderer;
    }
    
    public Container createDemo() {
        Table data = createTable(DEFAULT_COLUMNS, DEFAULT_DATA);
        TableModel tm = data.getModel();
        CategorySeries series = new CategorySeries("Sales");
        updateAxis(tm, 1, series);
        ChartComponent pieChart = new ChartComponent(new PieChart(series, createChartRenderer(tm)));
        pieChart.setZoomEnabled(true);
        
        tm.addDataChangeListener((i, ii) -> {
            if(inUpdate) {
                return;
            }
            inUpdate = true;
            updateAxis(tm, 1, series);
            pieChart.repaint();
            inUpdate = false;
        });
                
        
        Table lineTable = createTable(DEFAULT_COLUMNS_LINE, DEFAULT_DATA_LINE);
        TableModel lineTm = lineTable.getModel();
        XYMultipleSeriesDataset multi = new XYMultipleSeriesDataset();
        XYSeries seriesXY = new XYSeries("Sales", 0);
        multi.addSeries(seriesXY);
        updateSeriesFromTable(lineTm, seriesXY);
        XYMultipleSeriesRenderer renderer = createChartMultiRenderer(lineTm);
        ChartComponent lineChart = new ChartComponent(new LineChart(multi, renderer));
        
        lineTm.addDataChangeListener((i, ii) -> {
            if(inUpdate) {
                return;
            }
            inUpdate = true;
            updateSeriesFromTable(lineTm, seriesXY);
            updateRendererMinMax(lineTm, renderer);
            lineChart.repaint();
            inUpdate = false;
        });
        
        Tabs sales = new Tabs();
        sales.addTab("Categories", FontImage.MATERIAL_INSERT_CHART, 4, encloseInMaximizableGrid(pieChart, data));
        sales.addTab("Annual", FontImage.MATERIAL_DASHBOARD, 4, encloseInMaximizableGrid(lineChart, lineTable));
        return sales;
    }
    
}
