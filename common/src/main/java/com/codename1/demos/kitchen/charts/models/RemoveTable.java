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

package com.codename1.demos.kitchen.charts.models;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableModel;

/**
 * Special table whose last column is a "remove" button
 *
 * @author Shai Almog
 */
public class RemoveTable extends Table {
    private FontImage removeImage;
    private OnRemove onRemove;

    public RemoveTable(TableModel m, OnRemove onRemove) {
        super(m);
        this.onRemove = onRemove;
    }
    
    private FontImage getRemoveImage() {
        if(removeImage == null) {
            Style s = UIManager.getInstance().getComponentStyle("TableCell");
            s.setFgColor(0xff0000);
            removeImage = FontImage.createMaterial(FontImage.MATERIAL_DELETE, s, 3.5f);
        }
        return removeImage;
    }
    
    public static interface OnRemove {
        public void removed(int row, int column);
    }

    @Override
    protected Component createCell(Object value, int row, int column, boolean editable) {
        if(column == getModel().getColumnCount() - 1) {
            Button removeButton = new Button(getRemoveImage());
            removeButton.addActionListener(e -> {
                onRemove.removed(row, column);
                setModel(getModel());
            });
            return removeButton;
        }
        return super.createCell(value, row, column, editable); 
    }
}
