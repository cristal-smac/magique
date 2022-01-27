/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id: MessageDescription.java,v 1.1.1.1 2001/02/15 17:27:51 ejb Exp $
 */

package at.dms.util;

import java.text.MessageFormat;

/**
 * This class defines message descriptions (errors, warnings, notices, ...)
 *
 * The message format is a text message with placeholders for its arguments
 * of the form {0}, {1}, ... . Each placeholder will be replaced by the string
 * representation of the corresponding argument.
 */
public class MessageDescription
{
    /**
     * Message severity level: Bad level or level not specified.
     */
    public static final int LVL_UNDEFINED  = -1;

    /**
     * Message severity level: error.
     */
    public static final int LVL_ERROR  = 0;

    /**
     * Message severity level: caution.
     */
    public static final int LVL_CAUTION  = 1;

    /**
     * Message severity level: warning level 1.
     */
    public static final int LVL_WARNING_1  = 2;

    /**
     * Message severity level: warning level 2.
     */
    public static final int LVL_WARNING_2  = 3;

    /**
     * Message severity level: warning level 3.
     */
    public static final int LVL_WARNING_3  = 4;

    /**
     * Message severity level: notice.
     */
    public static final int LVL_NOTICE  = 5;

    /**
     * Message severity level: info.
     */
    public static final int LVL_INFO  = 6;

    /**
     * The formatting pattern that will be combined with arguments into
     * resulting message.
     */
    private final String format;

    /**
     * Reference to the right chapter in the Java Language Specification,
     * or another document.
     */
    private final String jlsReference;

    /**
     * Severity level of the message.
     */
    private final int level;

    /**
     * Constructs a message description
     * @param format  the textual message format (with placeholders)
     * @param jlsReference the document describing the reason for this message
     * @param level  the severity level of this message 
     */
    public MessageDescription(String format, String jlsReference, int level)
    {
        this.format = format;
        this.jlsReference = jlsReference;
        this.level = level;
    }

    /**
     * Returns the message format.
     */
    public String getFormat()
    {
        return format;
    }

    /**
     * Returns a text that refers to the Java Language Specification or other
     * documentation that provides background about this message.
     */
    public String getReference()
    {
        return jlsReference;
    }

    /**
     * Returns the severity level of this message.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Returns a textual description of the severity level.
     */
    public static String getLevelText(int level)
    {
        switch (level) {
        case LVL_UNDEFINED:
            return "";
        case LVL_ERROR:
            return "error";
        case LVL_CAUTION:
            return "caution";
        case LVL_WARNING_1:
            return "warning(1)";
        case LVL_WARNING_2:
            return "warning(2)";
        case LVL_WARNING_3:
            return "warning(3)";
        case LVL_NOTICE:
            return "notice";
        case LVL_INFO:
            return "info";
        default:
            return "unknown severity";
        }
    }

    /**
     * Returns a string explaining the error.
     *
     * @param parameters  the array of parameters
     */
    public String format(Object[] parameters)
    {
        String prefix;   // the text for the severity level
        String body;   // the formatted message
        String suffix;   // the jlsReference

        prefix = getLevelText(level) + ": ";

        try {
            body = MessageFormat.format(format, parameters);
        }
        catch (RuntimeException e) {
            // wrong number of parameters: give at least message text with
            // placeholders
            body = format;
        }

        if (jlsReference == null) {
            return prefix + body;
        }
        else {
            return prefix + body + " [" + jlsReference + "]";
        }
    }
}
