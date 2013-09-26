/**
 * blenlib/BLI_editVert.h    mar 2001 Nzc
 *
 * Some editing types needed in the lib (unfortunately) for
 * scanfill.c
 *
 * $Id: BLI_editVert.h 21553 2009-07-13 00:40:20Z blendix $
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
package blender.blenlib;

import blender.blenkernel.DerivedMesh;
import blender.makesdna.sdna.CustomData;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;

public class EditVertUtil {

    public static class Storage {

        private Object obj;

        public EditVert v() {
            return (EditVert) obj;
        }

        public EditEdge e() {
            return (EditEdge) obj;
        }

        public EditFace f() {
            return (EditFace) obj;
        }

        public Object p() {
            return obj;
        }

        public int l() {
            return (Integer) obj;
        }

        public float fp() {
            return (Float) obj;
        }

        public void v(EditVert v) {
            obj = v;
        }

        public void e(EditEdge e) {
            obj = e;
        }

        public void f(EditFace f) {
            obj = f;
        }

        public void p(Object p) {
            obj = p;
        }

        public void l(int l) {
            obj = l;
        }

        public void fp(float fp) {
            obj = fp;
        }
    }

    /* note; changing this also might affect the undo copy in editmesh.c */
    public static class EditVert extends Link<EditVert> {

        public Storage tmp = new Storage();
        public float[] no = new float[3]; /*vertex normal */

        public float[] co = new float[3]; /*vertex location */

        public short xs, ys; /* used to store a screenspace 2d projection of the verts */

        /* f stores selection eg. if (eve->f & SELECT) {...
        h for hidden. if (!eve->h) {...
        f1 and f2 can be used for temp data, clear them first*/
        public short f, h, f1, f2;
        public float bweight;
        public short fast;	/* only 0 or 1, for editmesh_fastmalloc, do not store temp data here! */

        public int hash;
        public int keyindex; /* original index #, for restoring  key information */

        public Object data;		/* custom vertex data */

    };

    public static class HashEdge {

        public EditEdge eed;
        public HashEdge next;
    };

    /* note; changing this also might affect the undo copy in editmesh.c */
    public static class EditEdge extends Link<EditEdge> {

        public EditVert v1, v2;
        public Storage tmp = new Storage();
        public short f1, f2;	/* short, f1 is (ab)used in subdiv */

        public short f, h, dir, seam, sharp;
        public float crease;
        public float bweight;
        public short fast; 		/* only 0 or 1, for editmesh_fastmalloc */

        public short fgoni;		/* index for fgon, for search */

        public HashEdge hash = new HashEdge();
        public Object data;			/*custom edge data*/

    };

    /* note; changing this also might affect the undo copy in editmesh.c */
    public static class EditFace extends Link<EditFace> {

        public EditVert v1, v2, v3, v4;
        public EditEdge e1, e2, e3, e4;
        public Storage tmp = new Storage();
        public float[] n = new float[3], cent = new float[3];
        public short flag;
        public short f, f1, h;
        public short fast;			/* only 0 or 1, for editmesh_fastmalloc */

        public short fgonf;		/* flag for fgon options */

        public short mat_nr;
        public Object data;		/* custom face data */

    };

    /*selection types*/
    public static final int EDITVERT = 0;
    public static final int EDITEDGE = 1;
    public static final int EDITFACE = 2;

    public static class EditSelection extends Link<EditSelection> {

        public short type;
        public Object data;
    };

    public static class EditMesh {

        public ListBase<EditVert> verts = new ListBase<EditVert>();
        public ListBase<EditEdge> edges = new ListBase<EditEdge>();
        public ListBase<EditFace> faces = new ListBase<EditFace>();
        public ListBase<EditSelection> selected = new ListBase<EditSelection>(); /*EditSelections. Used to store the order in which things are selected.*/

        public HashEdge[] hashedgetab;

        /* this is for the editmesh_fastmalloc */
        public EditVert[] allverts;
        public int curvert;
        public EditEdge[] alledges;
        public int curedge;
        public EditFace[] allfaces;
        public int curface;
        /* DerivedMesh caches... note that derived cage can be equivalent
         * to derived final, care should be taken on release.
         */

        /* used for keeping track of the last clicked on face - so the space image
         * when using the last selected face - (EditSelection) the space image flickered too much
         *
         * never access this directly, use EM_set_actFace and EM_get_actFace */
        public EditFace act_face;

	/* copy from scene */
	public short selectmode;
	/* copy from object actcol */
	public short mat_nr;
	/* stats */
	public int totvert, totedge, totface, totvertsel, totedgesel, totfacesel;

        public DerivedMesh derivedCage, derivedFinal;
        /* the custom data layer mask that was last used to calculate
         * derivedCage and derivedFinal
         */
        public int lastDataMask;
//	struct RetopoPaintData *retopo_paint_data;
        public CustomData vdata = new CustomData(), edata = new CustomData(), fdata = new CustomData();
    };
}
