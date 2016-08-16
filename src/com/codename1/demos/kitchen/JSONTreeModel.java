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

import com.codename1.ui.tree.TreeModel;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * Simple tree model that can display JSON in a tree hierarchy
 *
 * @author Shai Almog
 */
class JSONTreeModel implements TreeModel {
    private Map<String, Object> root;

    public JSONTreeModel(Map<String, Object> h) {
        root = h;
    }

    public Vector getChildren(Object parent) {
        if (parent == null) {
            Vector c = new Vector();
            c.addElement(root);
            return c;
        }
        if (parent instanceof Map) {
            Map<String, Object> h = (Map<String, Object>) parent;
            Vector response = new Vector();
            for (Object key : h.keySet()) {
                response.addElement("Key: " + key);
                response.addElement(h.get(key));
            }
            return response;
        } else {
            return (Vector) parent;
        }
    }

    public boolean isLeaf(Object node) {
        return !(node instanceof Collection || node instanceof Map);
    }

}
