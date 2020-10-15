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
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.models.XYValueSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.BarChart;
import com.codename1.charts.views.BubbleChart;
import com.codename1.charts.views.CombinedXYChart;
import com.codename1.charts.views.CombinedXYChart.XYCombinedChartDef;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;
import java.util.ArrayList;
import java.util.List;

import com.codename1.charts.util.ColorUtil;

/**
 * Combined temperature demo chart.
 */
public class CombinedTemperatureChart extends AbstractDemoChart {

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Combined temperature";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The average temperature in 2 Greek islands, water temperature and sun shine hours (combined chart)";
    }

    @Override
    public Component getChartModelEditor() {
        return null;
    }

    @Override
    public String getChartTitle() {
        return "Weather parameters";
    }

    @Override
    public Component execute() {
        String[] titles = new String[]{"Crete Air Temperature", "Skiathos Air Temperature"};
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
            13.9});
        values.add(new double[]{9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10});
        int[] colors = new int[]{ColorUtil.GREEN, ColorUtil.rgb(200, 150, 0)};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        renderer.setPointSize(5.5f);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            r.setLineWidth(2);
            r.setFillPoints(true);
        }
        setChartSettings(renderer, "Weather data", "Month", "Temperature", 0.5, 12.5, 0, 40,
                ColorUtil.LTGRAY, ColorUtil.LTGRAY);

        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setMargins(new int[]{20, 30, 80, 0});
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{-10, 20, -10, 40});
        renderer.setZoomLimits(new double[]{-10, 20, -10, 40});

        XYValueSeries sunSeries = new XYValueSeries("Sunshine hours");
        sunSeries.add(1f, 35, 4.3);
        sunSeries.add(2f, 35, 4.9);
        sunSeries.add(3f, 35, 5.9);
        sunSeries.add(4f, 35, 8.8);
        sunSeries.add(5f, 35, 10.8);
        sunSeries.add(6f, 35, 11.9);
        sunSeries.add(7f, 35, 13.6);
        sunSeries.add(8f, 35, 12.8);
        sunSeries.add(9f, 35, 11.4);
        sunSeries.add(10f, 35, 9.5);
        sunSeries.add(11f, 35, 7.5);
        sunSeries.add(12f, 35, 5.5);
        XYSeriesRenderer lightRenderer = new XYSeriesRenderer();
        lightRenderer.setColor(ColorUtil.MAGENTA);

        XYSeries waterSeries = new XYSeries("Crete Water Temperature");
        waterSeries.add(1, 16);
        waterSeries.add(2, 15);
        waterSeries.add(3, 16);
        waterSeries.add(4, 17);
        waterSeries.add(5, 20);
        waterSeries.add(6, 23);
        waterSeries.add(7, 25);
        waterSeries.add(8, 25.5);
        waterSeries.add(9, 26.5);
        waterSeries.add(10, 24);
        waterSeries.add(11, 22);
        waterSeries.add(12, 18);
        XYSeries waterSeries2 = new XYSeries("Skiathos Water Temperature");
        waterSeries2.add(1, 15);
        waterSeries2.add(2, 14);
        waterSeries2.add(3, 14);
        waterSeries2.add(4, 15);
        waterSeries2.add(5, 18);
        waterSeries2.add(6, 22);
        waterSeries2.add(7, 24);
        waterSeries2.add(8, 25);
        waterSeries2.add(9, 24);
        waterSeries2.add(10, 21);
        waterSeries2.add(11, 18);
        waterSeries2.add(12, 16);
        renderer.setBarSpacing(0.3);
        XYSeriesRenderer waterRenderer1 = new XYSeriesRenderer();
        waterRenderer1.setColor(0xff0099cc);
        waterRenderer1.setChartValuesTextAlign(Component.CENTER);
        XYSeriesRenderer waterRenderer2 = new XYSeriesRenderer();
        waterRenderer2.setColor(0xff9933cc);
        waterRenderer2.setChartValuesTextAlign(Component.RIGHT);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        dataset.addSeries(0, sunSeries);
        dataset.addSeries(0, waterSeries);
        dataset.addSeries(0, waterSeries2);
        renderer.addSeriesRenderer(0, lightRenderer);
        renderer.addSeriesRenderer(0, waterRenderer1);
        renderer.addSeriesRenderer(0, waterRenderer2);
        waterRenderer1.setDisplayChartValues(true);
        waterRenderer1.setChartValuesTextSize(smallFont.getHeight() / 2);
        waterRenderer2.setDisplayChartValues(true);
        waterRenderer2.setChartValuesTextSize(smallFont.getHeight() / 2);

        XYCombinedChartDef[] types = new XYCombinedChartDef[]{
            new XYCombinedChartDef(BarChart.TYPE, 0, 1), new XYCombinedChartDef(BubbleChart.TYPE, 2),
            new XYCombinedChartDef(LineChart.TYPE, 3), new XYCombinedChartDef(CubicLineChart.TYPE, 4)};

        initRendererer(renderer);
        CombinedXYChart chart = new CombinedXYChart(dataset, renderer, types);
        return newChart(chart);

    }

}
