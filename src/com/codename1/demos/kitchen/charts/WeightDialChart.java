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

import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DialRenderer;
import com.codename1.charts.renderers.DialRenderer.Type;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.views.DialChart;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;

/**
 * Budget demo pie chart.
 */
public class WeightDialChart extends AbstractDemoChart {

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Weight chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The weight indicator (dial chart)";
    }

    @Override
    public Component getChartModelEditor() {
        return null;
    }

    @Override
    public String getChartTitle() {
        return "Weight indicator";
    }

    @Override
    public Component execute() {
        CategorySeries category = new CategorySeries("Weight indic");
        category.add("Current", 75);
        category.add("Minimum", 65);
        category.add("Maximum", 90);
        DialRenderer renderer = new DialRenderer();
        renderer.setChartTitleTextFont(largeFont);
        renderer.setLabelsTextFont(medFont);
        renderer.setLegendTextFont(medFont);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.rgb(0, 150, 0));
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(ColorUtil.GREEN);
        renderer.addSeriesRenderer(r);
        renderer.setLabelsTextSize(smallFont.getHeight() / 2);
        renderer.setLabelsColor(ColorUtil.WHITE);
        renderer.setShowLabels(true);
        renderer.setVisualTypes(new DialRenderer.Type[]{Type.ARROW, Type.NEEDLE, Type.NEEDLE});
        renderer.setMinValue(0);
        renderer.setMaxValue(150);
        initRendererer(renderer);

        DialChart chart = new DialChart(category, renderer);
        return newChart(chart);

    }

}
