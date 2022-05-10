/*
 * @(#)Converter.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Nadir Doghmane & Niquet Fabien("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Nadir Doghmane & Niquet Fabien.
 */

package fr.lifl.magique.gui.draw;


import java.awt.*;

/**
 * A class that is used to set the shown strings in the agent box
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */

public class Converter {

    /**
     * Returns the string shown in the agent box in accordance with the FontMetrics
     *
     * @param S     the string to be "converted"
     * @param F     the current FontMetrics
     * @param width the width of a GraphicAgent
     * @return the string to be shown
     */
    public String show(String S, FontMetrics F, int width) {

        int compchar = Math.min(width / F.getMaxAdvance(), S.length());
        String show = S.substring(0, compchar);

        if (compchar + 1 > S.length()) return show;

        String tmp = S.substring(0, ++compchar);
        while (F.stringWidth(tmp) < width) {
            show = tmp;
            compchar++;
            if (compchar > S.length()) return show;
            tmp = S.substring(0, compchar);
        }

        return show;
    }

}
