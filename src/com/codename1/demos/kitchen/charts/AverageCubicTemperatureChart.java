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

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;

import com.codename1.charts.util.ColorUtil;
import com.codename1.demos.kitchen.charts.models.XYMultipleSeriesEditor;

/**
 * Average temperature demo chart.
 */
public class AverageCubicTemperatureChart extends AbstractDemoChart {
    private XYMultipleSeriesDataset dataSet;
    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Average temperature";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The average temperature in 4 Greek islands (cubic line chart)";
    }

    @Override
    public Component getChartModelEditor() {
        XYMultipleSeriesEditor x = new XYMultipleSeriesEditor();
        x.init(getDataSet());
        return x;
    }

    @Override
    public String getChartTitle() {
        return "Avg. Cubic Temperature";
    }
    
    private XYMultipleSeriesDataset getDataSet() {
        if(dataSet == null) {
            dataSet = createTemperatureDataset();
        }
        return dataSet;
    }

    @Override
    public Component execute() {
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.CYAN, ColorUtil.MAGENTA};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND,
            PointStyle.TRIANGLE, PointStyle.SQUARE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{-10, 20, -10, 40});
        renderer.setPanEnabled(true);
        renderer.setZoomEnabled(true);
        renderer.setZoomLimits(new double[]{-10, 20, -10, 40});
        renderer.setMargins(new int[]{20, 30, 80, 0});
        initRenderer(renderer);
        CubicLineChart chart = new CubicLineChart(
                getDataSet(),
                renderer,
                0.33f
        );
        ChartComponent c = newChart(chart);
        return c;

    }

}
