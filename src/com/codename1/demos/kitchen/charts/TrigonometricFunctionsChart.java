/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codename1.demos.kitchen.charts;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.views.PointStyle;
import java.util.ArrayList;
import java.util.List;

import com.codename1.charts.util.ColorUtil;
import com.codename1.demos.kitchen.charts.models.XYMultipleSeriesEditor;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.animations.Motion;

/**
 * Trigonometric functions demo chart.
 */
public class TrigonometricFunctionsChart extends AbstractDemoChart {
    private XYMultipleSeriesDataset dataSet;

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Trigonometric functions";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The graphical representations of the sin and cos functions (line chart)";
    }

    @Override
    public Component getChartModelEditor() {
        XYMultipleSeriesEditor x = new XYMultipleSeriesEditor();
        x.init(getDataSet());
        return x;
    }

    private XYMultipleSeriesDataset getDataSet() {
        if(dataSet == null) {
            String[] titles = new String[]{"sin", "cos"};
            List<double[]> x = new ArrayList<double[]>();
            List<double[]> values = new ArrayList<double[]>();
            int step = 4;
            int count = 360 / step + 1;
            x.add(new double[count]);
            x.add(new double[count]);
            double[] sinValues = new double[count];
            double[] cosValues = new double[count];
            values.add(sinValues);
            values.add(cosValues);
            for (int i = 0; i < count; i++) {
                int angle = i * step;
                x.get(0)[i] = angle;
                x.get(1)[i] = angle;
                double rAngle = Math.toRadians(angle);
                sinValues[i] = Math.sin(rAngle);
                cosValues[i] = Math.cos(rAngle);
            }
            dataSet = buildDataset(titles, x, values);
        }
        return dataSet;
    }

    @Override
    public String getChartTitle() {
        return "Trigonometric function";
    }

    @Override
    public Component execute() {
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.CYAN};
        PointStyle[] styles = new PointStyle[]{PointStyle.POINT, PointStyle.POINT};
        final XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "Trigonometric functions", "X (in degrees)", "Y", 0, 360, -1, 1,
                ColorUtil.GRAY, ColorUtil.LTGRAY);

        int strWidth = Display.getInstance().convertToPixels(10);
        int numXLabels = Display.getInstance().getDisplayWidth() / (strWidth + 20);
        renderer.setXLabels(numXLabels);
        renderer.setYLabels(10);
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(50);
        initRendererer(renderer);

        final Motion m = Motion.createLinearMotion(0, 310, 10000);

        final LineChart chart = new LineChart(getDataSet(), renderer);
        final ChartComponent cmp = new ChartComponent(chart) {

            @Override
            protected void initComponent() {
                super.initComponent();
                getComponentForm().registerAnimated(this);
            }

            public boolean animate() {
                boolean b = super.animate();
                if (m.isFinished()) {
                    return b;
                } else {
                    renderer.setXAxisMin(m.getValue());
                    renderer.setXAxisMax(m.getValue() + 50);
                    return true;
                }
            }
        };
        m.start();
        return cmp;

    }

}
