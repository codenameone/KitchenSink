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
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.util.MathHelper;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.views.TimeChart;
import com.codename1.demos.kitchen.charts.models.XYMultipleSeriesEditor;
import com.codename1.ui.Component;
import com.codename1.ui.Display;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Temperature sensor demo chart.
 */
public class SensorValuesChart extends AbstractDemoChart {
    private XYMultipleSeriesDataset dataSet;
    private List<Date[]> x;

    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;

    private static final int HOURS = 24;

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sensor data";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The temperature, as read from an outside and an inside sensors";
    }

    @Override
    public Component getChartModelEditor() {
        XYMultipleSeriesEditor x = new XYMultipleSeriesEditor();
        x.init(getDataSet());
        return x;
    }

    private XYMultipleSeriesDataset getDataSet() {
        if(dataSet == null) {
            String[] titles = new String[]{"Inside", "Outside"};
            long now = Math.round(new Date().getTime() / DAY) * DAY;
            x = new ArrayList<Date[]>();
            for (int i = 0; i < titles.length; i++) {
                Date[] dates = new Date[HOURS];
                for (int j = 0; j < HOURS; j++) {
                    dates[j] = new Date(now - (HOURS - j) * HOUR);
                }
                x.add(dates);
            }
            List<double[]> values = new ArrayList<double[]>();

            values.add(new double[]{21.2, 21.5, 21.7, 21.5, 21.4, 21.4, 21.3, 21.1, 20.6, 20.3, 20.2,
                19.9, 19.7, 19.6, 19.9, 20.3, 20.6, 20.9, 21.2, 21.6, 21.9, 22.1, 21.7, 21.5});
            values.add(new double[]{1.9, 1.2, 0.9, 0.5, 0.1, -0.5, -0.6, MathHelper.NULL_VALUE,
                MathHelper.NULL_VALUE, -1.8, -0.3, 1.4, 3.4, 4.9, 7.0, 6.4, 3.4, 2.0, 1.5, 0.9, -0.5,
                MathHelper.NULL_VALUE, -1.9, -2.5, -4.3});
            dataSet = buildDateDataset(titles, x, values);
        }
        return dataSet;
    }

    @Override
    public String getChartTitle() {
        return "Temperature";
    }

    @Override
    public Component execute() {
        int[] colors = new int[]{ColorUtil.GREEN, ColorUtil.BLUE};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        
        // lazy initialization of x...
        getDataSet();
        setChartSettings(renderer, "Sensor temperature", "Hour", "Celsius degrees",
                x.get(0)[0].getTime(), x.get(0)[HOURS - 1].getTime(), -5, 30, ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        int strWidth = Display.getInstance().convertToPixels(25);

        int numXLabels = Display.getInstance().getDisplayWidth() / (strWidth + 20);

        renderer.setXLabels(numXLabels);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.CENTER);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setMargins(new int[]{20, 30, 80, 0});
        initRenderer(renderer);

        TimeChart chart = new TimeChart(getDataSet(),
                renderer);
        return newChart(chart);

    }
}
