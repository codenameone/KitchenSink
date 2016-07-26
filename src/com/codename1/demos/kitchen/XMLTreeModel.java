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

import com.codename1.ui.tree.TreeModel;
import com.codename1.xml.Element;
import java.util.Vector;

/**
 * Simple tree model that can display XML in a tree hierarchy
 *
 * @author shai
 */
class XMLTreeModel implements TreeModel {
    private Element root;

    public XMLTreeModel(Element e) {
        root = e;
    }

    public Vector getChildren(Object parent) {
        if (parent == null) {
            Vector c = new Vector();
            c.addElement(root);
            return c;
        }
        Vector result = new Vector();
        Element e = (Element) parent;
        for (int iter = 0; iter < e.getNumChildren(); iter++) {
            result.addElement(e.getChildAt(iter));
        }
        return result;
    }

    public boolean isLeaf(Object node) {
        Element e = (Element) node;
        return e.getNumChildren() == 0;
    }

}
