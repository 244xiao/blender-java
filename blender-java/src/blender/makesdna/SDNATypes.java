/**
 * blenlib/DNA_sdna.h (mar-2001 nzc)
 *
 * $Id: DNA_sdna_types.h 14444 2008-04-16 22:40:48Z hos $
 *
 * ***** BEGIN GPL LICENSE BLOCK *****
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
 * All rights reserved.
 *
 * The Original Code is: all of this file.
 *
 * Contributor(s): none yet.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.makesdna;

import blender.makesdna.sdna.Link;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SDNATypes {

    public static class SDNA {
        public ByteBuffer data;
        public int datalen, nr_names;
        public String[] names;
        public int nr_types, pointerlen;
        public String[] types;
        public short[] typelens;
        public int nr_structs;
        public short[][] structs;
        /* wrong place for this really, its a simple
         * cache for findstruct_nr.
         */
        public int lastfind;
    }

    public static class BHeadN extends Link<BHeadN> {
        public BHead bhead;
        public ByteBuffer data;

        public BHeadN(int size, BHead bh) {
            bhead = bh;
            bhead.bheadN = this;
            data = ByteBuffer.allocate(size);
        }
    };

    public static class BHead {
        public int code, len;
        public int old;
        public int SDNAnr, nr;
        public BHeadN bheadN;

        public byte[] getName(int offset) {
            byte[] name = new byte[1024];
            for (int i = 0; i < name.length; i++) {
                if ((name[i] = bheadN.data.get(offset++)) == 0) {
                    break;
                }
            }
            return name;
        }

        public static int getCodeInt(ByteBuffer buffer) {
            return ((buffer.get(0) & 0xFF) << 24) | ((buffer.get(1) & 0xFF) << 16) | ((buffer.get(2) & 0xFF) << 8) | ((buffer.get(3) & 0xFF) << 0);
        }

        public void read(ByteBuffer buffer, int pointerSize) {
        	switch (pointerSize) {
        	case 4:
	            code = ((buffer.get(0) & 0xFF) << 24) | ((buffer.get(1) & 0xFF) << 16) | ((buffer.get(2) & 0xFF) << 8) | ((buffer.get(3) & 0xFF) << 0);
	            len = buffer.getInt(4);
	            old = buffer.getInt(8);
	            SDNAnr = buffer.getInt(12);
	            nr = buffer.getInt(16);
	            break;
        	case 8:
        		code = ((buffer.get(0) & 0xFF) << 24) | ((buffer.get(1) & 0xFF) << 16) | ((buffer.get(2) & 0xFF) << 8) | ((buffer.get(3) & 0xFF) << 0);
	            len = buffer.getInt(4);
	            old = (int)buffer.getLong(8);
	            SDNAnr = buffer.getInt(16);
	            nr = buffer.getInt(20);
	            break;
        	}
        }

        public void write(DataOutput buffer) throws IOException {
            buffer.writeInt(code);
            buffer.writeInt(len);
            buffer.writeInt(old);
            buffer.writeInt(SDNAnr);
            buffer.writeInt(nr);
        }
    }

}
