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
package com.codename1.demos.kitchen.util;

import com.codename1.ui.CN;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;

public class Util {
    private static Image roundMaskImage = null;
    private static int imageWidth = -1;
    private static int imageHeight = -1;
    
    public static int getDemoImageWidth() {
        if(imageWidth < 0) {
             imageWidth = CN.convertToPixels(15);
        }
        return imageWidth;
    }
    
    public static int getDemoImageHeight() {
        if(imageHeight < 0) {
             imageHeight = CN.convertToPixels(12);
        }
        return imageHeight;
    }

    public static Object getRoundMask(int width){
        if (roundMaskImage == null) {
            roundMaskImage = Image.createImage(width, width, 0xff000000);
            Graphics gr = roundMaskImage.getGraphics();
            gr.setColor(0xffffff);
            gr.setAntiAliased(true);
            gr.fillArc(0, 0, width, width, 0, 360);
        }else{
            roundMaskImage = roundMaskImage.fill(width, width);
        }
        return roundMaskImage.createMask();
    }
}
