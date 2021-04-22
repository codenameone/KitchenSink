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
package com.codename1.demos.kitchen;

import com.codename1.ui.*;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.geom.Shape;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import java.util.Calendar;
import java.util.TimeZone;

import static com.codename1.ui.CN.*;

/**
 * This demo shows off low level graphics in Codename One and drawing of shapes, it also demonstrates the
 * flexibility of the image class
 *
 * @author Sergey Gerashenko.
 */
public class ClockDemo extends Demo{
    private long lastRenderedTime = 0;
    private int shortTickLen;  // at 1-minute intervals
    private int medTickLen;  // at 5-minute intervals
    private int longTickLen; // at 15-minute intervals

    private float minuteHandWidth;
    private float hourHandWidth;

    private static int clockColor;

    public ClockDemo(Form parentForm) {
        init("Clock", new ClockImage(), parentForm,
                "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/ClockDemo.java");
    }

    @Override
    public Container createContentPane() {
        Form demoForm = new Form(getDemoId(), new BorderLayout());
        demoForm.getContentPane().setUIID("ComponentDemoContainer");
        Toolbar toolbar = demoForm.getToolbar();
        toolbar.setUIID("DemoToolbar");
        toolbar.getTitleComponent().setUIID("DemoTitle");

        // Toolbar add source and back buttons.
        Style commandStyle = UIManager.getInstance().getComponentStyle("DemoTitleCommand");
        Command backCommand = Command.create("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, commandStyle),
                e-> getParentForm().showBack());

        Command sourceCommand = Command.create("", FontImage.create("{ }", commandStyle),
                e-> execute(getSourceCode()));

        toolbar.addCommandToRightBar(sourceCommand);
        toolbar.setBackCommand(backCommand);

        AnalogClock clock = new AnalogClock("ClockComponent");
        if(isDesktop()){
            minuteHandWidth = 3f;
            hourHandWidth = 1f;
        }else{
            minuteHandWidth = 6f;
            hourHandWidth = 3f;
        }
        refreshClockColor();
        demoForm.add(BorderLayout.CENTER, clock);
        demoForm.show();

        return null;
    }

    public static void  refreshClockColor(){
        clockColor = UIManager.getInstance().getComponentStyle("ClockComponent").getFgColor();
    }

    private class AnalogClock extends Component {
        private final int PADDING = convertToPixels(2);

        private AnalogClock(String uiid){
            this.setUIID(uiid);
        }

        public void start(){
            getComponentForm().registerAnimated(this);
        }

        public void stop(){
            getComponentForm().deregisterAnimated(this);
        }

        @Override
        public boolean animate() {
            if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                lastRenderedTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        @Override
        public void paintBackground(Graphics g) {
            super.paintBackground(g);
            // Center point.
            int centerX = getX() + getWidth() / 2;
            int centerY = getY() + getHeight() / 2;

            int radius = Math.min(getWidth(), getHeight()) / 2 - PADDING;
            drawClock(g, centerX, centerY, radius, 50, 30, 10, false);
            start();
        }
    }

    private void drawTicks(Graphics g, int centerX, int centerY, int radius){
        Stroke tickStroke = new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_ROUND, 1f);
        GeneralPath ticksPath = new GeneralPath();

        // Draw a tick for each "second" (1 through 60)
        for (int i = 1; i<= 60; i++){

            // default tick length is short
            int len = shortTickLen;

            if (i % 15 == 0){
                // Longest tick at 15-minute intervals.
                len = longTickLen;
            } else if (i % 5 == 0){
                // Medium ticks at 5-minute intervals.
                len = medTickLen;
            }

            // Get the angle from 12 O'Clock to this tick (radians)
            double angleFrom12 = (double)i / 60.0 * 2.0 * Math.PI;

            // Get the angle from 3 O'Clock to this tick
            // Note: 3 O'Clock corresponds with zero angle in unit circle
            // Makes it easier to do the math.
            double angleFrom3 = Math.PI / 2.0 - angleFrom12;

            // Move to the outer edge of the circle at correct position
            // for this tick.
            ticksPath.moveTo(
                    (float)(centerX + Math.cos(angleFrom3) * radius),
                    (float)(centerY - Math.sin(angleFrom3) * radius)
            );

            // Draw line inward along radius for length of tick mark.
            ticksPath.lineTo(
                    (float)(centerX + Math.cos(angleFrom3) * (radius - len)),
                    (float)(centerY - Math.sin(angleFrom3) * (radius - len))
            );
        }

        // Draw the ticks.
        g.setColor(clockColor);
        g.drawShape(ticksPath, tickStroke);
    }

    private void drawNumbers(Graphics g, int centerX, int centerY, int radius){
        Coordinate[] coordinates = new Coordinate[12];

        // Calculate all the numbers coordinates.
        for (int i = 1; i <= 12; i++){
            // Calculate the string width and height so we can center it properly
            String hourString = ((Integer)i).toString();
            int charWidth = g.getFont().stringWidth(hourString);
            int charHeight = g.getFont().getHeight();

            // Calculate the position along the edge of the clock where the number should
            // be drawn.
            // Get the angle from 12 O'Clock to this tick (radians).
            double angleFrom12 = (double)i / 12.0 * 2.0 * Math.PI;

            // Get the angle from 3 O'Clock to this tick
            // Note: 3 O'Clock corresponds with zero angle in unit circle
            // Makes it easier to do the math.
            double angleFrom3 = Math.PI / 2.0 - angleFrom12;

            int extraRange = convertToPixels(2);

            // Get diff between number position and clock center
            int tx = (int)(Math.cos(angleFrom3) * (radius - longTickLen - extraRange));
            int ty = (int)(-Math.sin(angleFrom3) * (radius - longTickLen - extraRange));

            coordinates[i - 1] = new Coordinate(tx + centerX - charWidth / 2, ty + centerY - charHeight / 2);
        }

        // Draw all the numbers.
        for (int i = 1; i <= 12; i++) {
            String hourString = ((Integer)i).toString();
            g.drawString(hourString, coordinates[i - 1].x, coordinates[i - 1].y);
        }
    }

    private void drawSecondHand(Graphics g, int centerX, int centerY, int radius){
        GeneralPath secondHand = new GeneralPath();
        secondHand.moveTo((float)centerX, (float)centerY);
        secondHand.lineTo((float)centerX, (float)(centerY - (radius - medTickLen)));
        Shape translatedSecondHand = secondHand.createTransformedShape(
                Transform.makeTranslation(0f, 5)
        );
        Calendar calendar =  Calendar.getInstance(TimeZone.getDefault());

        // Calculate the angle of the second hand
        double second = calendar.get(Calendar.SECOND);
        double secondAngle = second / 60.0 * 2.0 * Math.PI;

        g.rotateRadians((float)secondAngle, centerX, centerY);
        g.setColor(0xff0000);
        g.drawShape(
                translatedSecondHand,
                new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_BEVEL, 1f)
        );
        g.resetAffine();
    }

    private void drawMinuteHand(Graphics g, int centerX, int centerY, int radius){
        // Draw the minute hand
        GeneralPath minuteHand = new GeneralPath();
        minuteHand.moveTo((float)centerX, (float)centerY);
        minuteHand.lineTo((float)centerX + minuteHandWidth, (float)centerY);
        minuteHand.lineTo((float)centerX + 1, (float)(centerY - (radius - shortTickLen)));
        minuteHand.lineTo((float)centerX - 1, (float)(centerY - (radius - shortTickLen)));
        minuteHand.lineTo((float)centerX - minuteHandWidth, (float)centerY);
        minuteHand.closePath();

        // Translate the minute hand slightly down so it overlaps the center
        Shape translatedMinuteHand = minuteHand.createTransformedShape(
                Transform.makeTranslation(0f, 5)
        );

        Calendar calendar =  Calendar.getInstance(TimeZone.getDefault());

        double minute = (double)(calendar.get(Calendar.MINUTE)) +
                (double)(calendar.get(Calendar.SECOND)) / 60.0;

        double minuteAngle = minute / 60.0 * 2.0 * Math.PI;

        // Rotate and draw the minute hand
        g.rotateRadians((float)minuteAngle, centerX, centerY);
        g.setColor(clockColor);
        g.fillShape(translatedMinuteHand);
        g.resetAffine();
    }

    private void drawHourHand(Graphics g, int centerX, int centerY, int radius){
        // Draw the hour hand
        GeneralPath hourHand = new GeneralPath();
        hourHand.moveTo((float)centerX, (float)centerY);
        hourHand.lineTo((float)centerX + hourHandWidth, (float)centerY);
        hourHand.lineTo((float)centerX + 1, (float)(centerY - (radius - longTickLen) * 0.75));
        hourHand.lineTo((float)centerX - 1, (float)(centerY - (radius - longTickLen) * 0.75));
        hourHand.lineTo((float)centerX - hourHandWidth, (float)centerY);
        hourHand.closePath();

        Shape translatedHourHand = hourHand.createTransformedShape(
                Transform.makeTranslation(0f, 5)
        );

        Calendar calendar =  Calendar.getInstance(TimeZone.getDefault());

        double hour = (double)(calendar.get(Calendar.HOUR_OF_DAY) % 12) +
                (double)(calendar.get(Calendar.MINUTE)) / 60.0;

        double angle = hour / 12.0 * 2.0 * Math.PI;
        g.rotateRadians((float)angle, centerX, centerY);
        g.setColor(clockColor);
        g.fillShape(translatedHourHand);
        g.resetAffine();
    }

    private void drawClock(Graphics g, int centerX, int centerY, int radius,
                           int longTickLen, int medTickLen, int shortTickLen, boolean smallVersion){

        this.longTickLen = longTickLen;
        this.medTickLen = medTickLen;
        this.shortTickLen = shortTickLen;
        drawTicks(g, centerX, centerY, radius);
        if (!smallVersion){
            drawNumbers(g, centerX, centerY, radius);
        }
        drawSecondHand(g, centerX, centerY, radius);
        drawMinuteHand(g, centerX, centerY, radius);
        drawHourHand(g, centerX, centerY, radius);
    }

    private class ClockImage extends Image{
        private long lastRenderedTime = 0;

        private int width;
        private int height;
        private static final int DEFAULT_WIDTH = 250;
        private static final int DEFAULT_HEIGHT = 250;

        ClockImage() {
            super(null);
            this.width = DEFAULT_WIDTH;
            this.height = DEFAULT_HEIGHT;
        }

        ClockImage(int width, int height) {
            super(null);
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean isAnimation() {
            return true;
        }

        @Override
        public void scale(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean requiresDrawImage() {
            return true;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public Image fill(int width, int height) {
            return new ClockImage(width, height);
        }

        @Override
        protected void drawImage(Graphics g, Object nativeGraphics, int x, int y) {
            drawImage(g, nativeGraphics, x, y, width, height);
        }

        @Override
        protected void drawImage(Graphics g, Object nativeGraphics, int x, int y, int w, int h) {
            int radius = Math.min(getWidth(), getHeight()) / 2;
            int centerX = x + w / 2;
            int centerY = y + h / 2;
            drawClock(g, centerX, centerY, radius, 10, 6 , 2, true);
        }

        @Override
        public boolean animate() {
            if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                lastRenderedTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }
    }

    private static class Coordinate {
        private final int x;
        private final int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
