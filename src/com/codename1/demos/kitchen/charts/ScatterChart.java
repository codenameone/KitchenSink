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
package com.codename1.demos.kitchen.charts;

import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.PointStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.demos.kitchen.charts.models.XYMultipleSeriesEditor;
import com.codename1.ui.Component;

/**
 * Scatter demo chart.
 */
public class ScatterChart extends AbstractDemoChart {
    private XYMultipleSeriesDataset dataSet;

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Scatter chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "Randomly generated values for the scatter chart";
    }

    @Override
    public Component getChartModelEditor() {
        XYMultipleSeriesEditor x = new XYMultipleSeriesEditor();
        x.init(getDataSet());
        return x;
    }

    private XYMultipleSeriesDataset getDataSet() {
        if(dataSet == null) {
            String[] titles = new String[]{"Series 1", "Series 2", "Series 3", "Series 4", "Series 5"};
            List<double[]> x = new ArrayList<double[]>();
            List<double[]> values = new ArrayList<double[]>();
            int count = 20;
            int length = titles.length;
            Random r = new Random();
            for (int i = 0; i < length; i++) {
                double[] xValues = new double[count];
                double[] yValues = new double[count];
                for (int k = 0; k < count; k++) {
                    xValues[k] = k + r.nextInt() % 10;
                    yValues[k] = k * 2 + r.nextInt() % 10;
                }
                x.add(xValues);
                values.add(yValues);
            }
            dataSet = buildDataset(titles, x, values);
        }
        return dataSet;
    }
    
    @Override
    public String getChartTitle() {
        return "Scatter chart";
    }

    @Override
    public Component execute() {
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN, ColorUtil.MAGENTA, ColorUtil.LTGRAY, ColorUtil.GREEN};
        PointStyle[] styles = new PointStyle[]{PointStyle.X, PointStyle.DIAMOND, PointStyle.TRIANGLE,
            PointStyle.SQUARE, PointStyle.CIRCLE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Scatter chart", "X", "Y", -10, 30, -10, 51, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        for (int i = 0; i < 5 ; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        initRendererer(renderer);

        com.codename1.charts.views.ScatterChart chart = new com.codename1.charts.views.ScatterChart(getDataSet(), renderer);
        return newChart(chart);

    }

}
