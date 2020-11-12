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
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.DoughnutChart;
import com.codename1.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Budget demo pie chart.
 */
public class BudgetDoughnutChart extends AbstractDemoChart {

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Budget chart for several years";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The budget per project for several years (doughnut chart)";
    }

    @Override
    public Component getChartModelEditor() {
        return null;
    }

    @Override
    public String getChartTitle() {
        return "Doughnut Chart Demo";
    }

    @Override
    public Component execute() {
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{12, 14, 11, 10, 19});
        values.add(new double[]{10, 9, 14, 20, 11});
        List<String[]> titles = new ArrayList<String[]>();
        titles.add(new String[]{"P1", "P2", "P3", "P4", "P5"});
        titles.add(new String[]{"Project1", "Project2", "Project3", "Project4", "Project5"});
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};

        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setApplyBackgroundColor(true);
        renderer.setLabelsColor(ColorUtil.GRAY);
        initRenderer(renderer);

        DoughnutChart chart = new DoughnutChart(buildMultipleCategoryDataset("Project budget", titles, values), renderer);
        ChartComponent c = newChart(chart);
        return c;

    }

}
