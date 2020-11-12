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
import com.codename1.charts.models.XYValueSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.BubbleChart;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;

/**
 * Project status demo bubble chart.
 */
public class ProjectStatusBubbleChart extends AbstractDemoChart {

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Project tickets status";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The opened tickets and the fixed tickets (bubble chart)";
    }

    @Override
    public Component getChartModelEditor() {
        return null;
    }

    @Override
    public String getChartTitle() {
        return "Project tickets";
    }

    @Override
    public Component execute() {
        XYMultipleSeriesDataset series = new XYMultipleSeriesDataset();
        XYValueSeries newTicketSeries = new XYValueSeries("New Tickets");
        newTicketSeries.add(1f, 2, 14);
        newTicketSeries.add(2f, 2, 12);
        newTicketSeries.add(3f, 2, 18);
        newTicketSeries.add(4f, 2, 5);
        newTicketSeries.add(5f, 2, 1);
        series.addSeries(newTicketSeries);
        XYValueSeries fixedTicketSeries = new XYValueSeries("Fixed Tickets");
        fixedTicketSeries.add(1f, 1, 7);
        fixedTicketSeries.add(2f, 1, 4);
        fixedTicketSeries.add(3f, 1, 18);
        fixedTicketSeries.add(4f, 1, 3);
        fixedTicketSeries.add(5f, 1, 1);
        series.addSeries(fixedTicketSeries);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextFont(medFont);
        renderer.setChartTitleTextFont(largeFont);
        renderer.setLabelsTextFont(medFont);
        renderer.setLegendTextFont(medFont);
        renderer.setMargins(new int[]{20, 30, 80, 0});
        XYSeriesRenderer newTicketRenderer = new XYSeriesRenderer();
        newTicketRenderer.setColor(ColorUtil.BLUE);
        renderer.addSeriesRenderer(newTicketRenderer);
        XYSeriesRenderer fixedTicketRenderer = new XYSeriesRenderer();
        fixedTicketRenderer.setColor(ColorUtil.GREEN);
        renderer.addSeriesRenderer(fixedTicketRenderer);
        initRenderer(renderer);

        setChartSettings(renderer, "Project work status", "Priority", "", 0.5, 5.5, 0, 5, ColorUtil.GRAY,
                ColorUtil.LTGRAY);
        renderer.setXLabels(7);
        renderer.setYLabels(0);
        renderer.setShowGrid(false);

        BubbleChart chart = new BubbleChart(series, renderer);
        return newChart(chart);

    }

}
