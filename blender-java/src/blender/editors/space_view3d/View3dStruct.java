/**
 * $Id:
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
 * The Original Code is Copyright (C) 2008 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_view3d;

import blender.blenlib.EditVertUtil.EditMesh;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;

public class View3dStruct {

    /* ********* exports for space_view3d/ module ********** */

    /* for derivedmesh drawing callbacks, for view3d_select, .... */
    public static class ViewContext {

        public Scene scene;
        public bObject obact;
        public bObject obedit;
        public ARegion ar;
        public View3D v3d;
        public RegionView3D rv3d;
        public EditMesh em;
        public short[] mval = new short[2];

        public void clear() {
            scene = null;
            obact = null;
            obedit = null;
            ar = null;
            v3d = null;
            rv3d = null;
            em = null;
            mval = new short[2];
        }
    };

    public static class ViewDepths {

        public int w, h;
        public float[] depths;
        public double[] depth_range = new double[2];
        public char damaged;
    };

    /* Projection */
    public static final int IS_CLIPPED = 12000;

    /* select */
    public static final int MAXPICKBUF = 10000;
}
