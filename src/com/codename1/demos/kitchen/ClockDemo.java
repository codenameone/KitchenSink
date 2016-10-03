/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
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

import com.codename1.ui.Display;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Stroke;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.geom.Shape;
import com.codename1.ui.layouts.BorderLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This demo shows off low level graphics in Codename One and drawing of shapes, it also demonstrates the 
 * flexibility of the image class
 */
public class ClockDemo extends Demo {
    private Font numbersFont;
    long lastRenderedTime = 0;
    Date currentTime = new Date();

    public ClockDemo() {
        if(Font.isNativeFontSchemeSupported()) {
            numbersFont = Font.createTrueTypeFont("native:MainThin", "native:MainThin");
            numbersFont = numbersFont.derive(Display.getInstance().convertToPixels(3.5f), Font.STYLE_PLAIN);
        } else {
            numbersFont = Font.getDefaultFont();
        }
    }

    
    @Override
    public Container createDemo() {
        AnalogClock clock = new AnalogClock();
        return BorderLayout.center(clock);
    }

    @Override
    public String getDisplayName() {
        return "Time";
    }

    @Override
    public String getDescription() {
        return "This section shows off low level graphics in Codename One and drawing of shapes, it also demonstrates the "
                + "flexibility of the image class. You can learn more about this code within the developer guide in the graphics chapter";
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/ClockDemo.java";
    }

    
    @Override
    public Image getDemoIcon() {
        class ClockImage extends Image {
            int w = 250, h = 250;
            
            ClockImage() {
                super(null);
            }

            ClockImage(int w, int h) {
                super(null);
                this.w = w;
                this.h = h;
            }
            
            @Override
            public int getWidth() {
                return w;
            }

            @Override
            public int getHeight() {
                return h;
            }

            @Override
            public void scale(int width, int height) {
                w = width;
                h = height;
            }

            @Override
            public Image fill(int width, int height) {
                return new ClockImage(width, height);
            }

            @Override
            public Image applyMask(Object mask) {
                return new ClockImage(w, h);
            }

            
            @Override
            public boolean isAnimation() {
                return true;
            }

            @Override
            public boolean requiresDrawImage() {
                return true;
            }

            @Override
            protected void drawImage(Graphics g, Object nativeGraphics, int x, int y) {
                Image img = getResources().getImage("card-full.png");
                g.drawImage(img, x, y, w, h * 4 / 3);
                paintClock(g, x, y, w, h, x + g.getTranslateX(), y + g.getTranslateY(), 0);
            }

            @Override
            protected void drawImage(Graphics g, Object nativeGraphics, int x, int y, int w, int h) {
                Image img = getResources().getImage("card-full.png");
                g.drawImage(img, x, y, w, h * 4 / 3);
                paintClock(g, x, y, w, h, x + g.getTranslateX(), y + g.getTranslateY(), 0);
            }
            
            @Override
            public boolean animate() {
                if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                    currentTime.setTime(System.currentTimeMillis());
                    return true;
                }
                return false;
            }
        };
        return new ClockImage();
    }

    public class AnalogClock extends Component {
        @Override
        public boolean animate() {
            if (System.currentTimeMillis() / 1000 != lastRenderedTime / 1000) {
                currentTime.setTime(System.currentTimeMillis());
                return true;
            }
            return false;
        }

        @Override
        protected void initComponent() {
            this.getComponentForm().registerAnimated(this);
        }

        @Override
        protected void deinitialize() {
            this.getComponentForm().deregisterAnimated(this);
        }

        @Override
        public void paintBackground(Graphics g) {
            super.paintBackground(g);
            int handColor = 0;
            if(getStyle().getBgColor() == 0) {
                handColor = 0xffffff;
            }
            paintClock(g, getX(), getY(), getWidth(), getHeight(), getAbsoluteX(), getAbsoluteY(), handColor);
        }
    }

    void paintClock(Graphics g, int x, int y, int w, int h, int absx, int absy, int handColor) {
        boolean oldAntialiased = g.isAntiAliased();
        g.setAntiAliased(true);
        double padding = 10;
        double r = Math.min(w, h) / 2 - padding;
        double cX = x + w / 2;
        double cY = y + h / 2;
        
        // draw the ticks
        int tickLen = 10;
        int medTickLen = 30;
        int longTickLen = 50;
        int tickColor = 0xCCCCCC;
        Stroke tickStroke = new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_ROUND, 1f);

        Font fnt = numbersFont;
        Display dd = Display.getInstance();
        boolean small = false;
        boolean tiny = false;
        int minuteHandWidthLarge = 6, minuteHandWidthSmall = 2;
        int hourHandWidth = 4;
        if(w < dd.getDisplayWidth() / 2) {
            fnt = numbersFont.derive(Display.getInstance().convertToPixels(1.5f), Font.STYLE_PLAIN);
            small = true;
            tickLen = 4;
            medTickLen = 8;
            longTickLen = 16;
            tiny = w < dd.getDisplayWidth() / 4;
            minuteHandWidthLarge = 3; 
            minuteHandWidthSmall = 1;
            hourHandWidth = 2;
        }        
        
        GeneralPath ticksPath = new GeneralPath();

        for (int i = 1; i <= 60; i++) {
            if(tiny && i % 5 != 0) {
                continue;
            }
            int len = tickLen;
            if (i % 15 == 0) {
                len = longTickLen;
            } else if (i % 5 == 0) {
                len = medTickLen;
            }
            double di = (double) i;
            double angleFrom12 = di / 60.0 * 2.0 * Math.PI;
            double angleFrom3 = Math.PI / 2.0 - angleFrom12;
            ticksPath.moveTo(
                    (float) (cX + Math.cos(angleFrom3) * r),
                    (float) (cY - Math.sin(angleFrom3) * r)
            );

            ticksPath.lineTo(
                    (float) (cX + Math.cos(angleFrom3) * (r - len)),
                    (float) (cY - Math.sin(angleFrom3) * (r - len))
            );
        }
        g.setColor(tickColor);
        g.drawShape(ticksPath, tickStroke);

        g.setColor(handColor);
        
        g.setFont(fnt);
        int charHeight = fnt.getHeight();

        if(!tiny) {
            // Draw the numbers
            for (int i = 1; i <= 12; i++) {
                if(small && i % 3 != 0) {
                    continue;
                }
                String numStr = "" + i;
                int charWidth = fnt.stringWidth(numStr);
                double di = (double) i;
                double angleFrom12 = di / 12.0 * 2.0 * Math.PI;
                double angleFrom3 = Math.PI / 2.0 - angleFrom12;

                int tx = (int) (Math.cos(angleFrom3) * (r - longTickLen));
                int ty = (int) (-Math.sin(angleFrom3) * (r - longTickLen));

                g.translate(
                        tx,
                        ty
                );

                g.drawString(numStr, (int) cX - charWidth / 2, (int) cY - charHeight / 2);
                g.translate(-tx, -ty);
            }
        }

        // Draw the hands of the clock
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        GeneralPath secondHand = new GeneralPath();
        secondHand.moveTo((float) cX, (float) cY);
        secondHand.lineTo((float) cX, (float) (cY - (r - medTickLen)));

        Shape translatedSecondHand = secondHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));

        double second = (double) (calendar.get(Calendar.SECOND));

        double secondAngle = second / 60.0 * 2.0 * Math.PI;

        double absCX = absx + cX - x;
        double absCY = absy + cY - y;

        g.rotate((float) secondAngle, (int) absCX, (int) absCY);
        g.setColor(0xff0000);
        g.drawShape(translatedSecondHand, new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_BEVEL, 1f));
        g.resetAffine();

        GeneralPath minuteHand = new GeneralPath();
        minuteHand.moveTo((float) cX, (float) cY);
        minuteHand.lineTo((float) cX + minuteHandWidthLarge, (float) cY);
        minuteHand.lineTo((float) cX + minuteHandWidthSmall, (float) (cY - (r - tickLen)));
        minuteHand.lineTo((float) cX - minuteHandWidthSmall, (float) (cY - (r - tickLen)));
        minuteHand.lineTo((float) cX - minuteHandWidthLarge, (float) cY);
        minuteHand.closePath();

        Shape translatedMinuteHand = minuteHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));

        double minute = (double) (calendar.get(Calendar.MINUTE))
                + (double) (calendar.get(Calendar.SECOND)) / 60.0;

        double minuteAngle = minute / 60.0 * 2.0 * Math.PI;
        g.rotate((float) minuteAngle, (int) absCX, (int) absCY);
        g.setColor(handColor);
        g.fillShape(translatedMinuteHand);
        g.resetAffine();

        GeneralPath hourHand = new GeneralPath();
        hourHand.moveTo((float) cX, (float) cY);
        hourHand.lineTo((float) cX + hourHandWidth, (float) cY);
        hourHand.lineTo((float) cX + 1, (float) (cY - (r - longTickLen) * 0.75));
        hourHand.lineTo((float) cX - 1, (float) (cY - (r - longTickLen) * 0.75));
        hourHand.lineTo((float) cX - hourHandWidth, (float) cY);
        hourHand.closePath();

        Shape translatedHourHand = hourHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));

        double hour = (double) (calendar.get(Calendar.HOUR_OF_DAY) % 12)
                + (double) (calendar.get(Calendar.MINUTE)) / 60.0;

        double angle = hour / 12.0 * 2.0 * Math.PI;
        g.rotate((float) angle, (int) absCX, (int) absCY);
        g.setColor(handColor);
        g.fillShape(translatedHourHand);
        g.resetAffine();

        g.setAntiAliased(oldAntialiased);
        lastRenderedTime = System.currentTimeMillis();    
    }
}
