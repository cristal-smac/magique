/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package at.dms.classfile;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * A placeholder for a stream of data. When requested, this class will
 * return an optimized DataInput object for the stream that was passed in the
 * constructor.
 */
class Data
{
    /**
     * The inputstream to read from.
     */
    private InputStream is;

    public Data(InputStream is)
    {
        this.is = is;
    }

    public DataInput getDataInput()
        throws IOException
    {
        return new DataInputStream(new BufferedInputStream(is));
    }

    public void release()
    {
    }
}
