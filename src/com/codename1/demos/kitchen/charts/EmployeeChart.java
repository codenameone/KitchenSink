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

import com.codename1.charts.models.AreaSeries;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.RadarChart;
import com.codename1.ui.Component;

public class EmployeeChart extends AbstractDemoChart {

    @Override
    public String getName() {
        return "Radar chart";
    }

    @Override
    public String getDesc() {
        return "Generate Radar chart";
    }

    @Override
    public Component execute() {
        int[] colors = {ColorUtil.BLUE};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        initRendererer(renderer);
        
        RadarChart chart = new RadarChart(getDataSet(), renderer);
           
        return newChart(chart);
    }

    @Override
    public String getChartTitle() {
        return "Employee";
    }

    @Override
    public Component getChartModelEditor() {
        return null;
    }
    
    private AreaSeries getDataSet() {
        CategorySeries series = new CategorySeries("Employee");
        series.add("technical skills", 0.5f);
        series.add("sense of humor", 0.3f);
        series.add("personality", 0.8f);
        series.add("accomplishment", 0.4f);
        series.add("experience", 0.9f);
        AreaSeries dataSet = new AreaSeries();
        
        dataSet.addSeries(series);
        
        return dataSet;
    }
    
}
