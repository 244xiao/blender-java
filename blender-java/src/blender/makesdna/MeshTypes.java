/**
 * $Id: DNA_mesh_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $ 
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

//#ifndef DNA_MESH_TYPES_H
//#define DNA_MESH_TYPES_H
//
//#include "DNA_listBase.h"
//#include "DNA_ID.h"
//#include "DNA_customdata_types.h"
//
public class MeshTypes {
//struct DerivedMesh;
//struct Ipo;
//struct Key;
//struct Material;
//struct MVert;
//struct MEdge;
//struct MFace;
//struct MCol;
//struct MSticky;
//struct Mesh;
//struct OcInfo;
//struct Multires;
//struct PartialVisibility;
//
//typedef struct Mesh {
//	ID id;
//
//	struct BoundBox *bb;
//
//	ListBase effect;
//
//	struct Ipo *ipo;
//	struct Key *key;
//	struct Material **mat;
//
//	struct MFace *mface;	/* array of mesh object mode faces */
//	struct MTFace *mtface;	/* store face UV's and texture here */
//	struct TFace *tface;	/* depecrated, use mtface */
//	struct MVert *mvert;	/* array of verts */
//	struct MEdge *medge;	/* array of edges */
//	struct MDeformVert *dvert;	/* __NLA */
//	struct MCol *mcol;		/* array of colors, this must be the number of faces * 4 */
//	struct MSticky *msticky;
//	struct Mesh *texcomesh;
//	struct MSelect *mselect;
//
//	struct CustomData vdata, edata, fdata;
//
//	int totvert, totedge, totface, totselect;
//
//	/* the last selected vertex/edge/face are used for the active face however
//	 * this means the active face must always be selected, this is to keep track
//	 * of the last selected face and is similar to the old active face flag where
//	 * the face does not need to be selected, -1 is inactive */
//	int act_face;
//
//	int texflag;
//
//	/* texture space, copied as one block in editobject.c */
//	float loc[3];
//	float size[3];
//	float rot[3];
//
//	float cubemapsize, pad;
//
//	short smoothresh, flag;
//
//	short subdiv, subdivr;
//	short totcol;
//	short subsurftype;		/* only kept for backwards compat, not used anymore */
//
//	struct Multires *mr;		/* Multiresolution modeling data */
//	struct PartialVisibility *pv;
///*ifdef WITH_VERSE*/
//	/* not written in file, pointer at geometry VerseNode */
//	void *vnode;
///*#endif*/
//} Mesh;
//
///* deprecated by MTFace, only here for file reading */
//typedef struct TFace {
//	void *tpage;	/* the faces image for the active UVLayer */
//	float uv[4][2];
//	unsigned int col[4];
//	char flag, transp;
//	short mode, tile, unwrap;
//} TFace;

/* **************** MESH ********************* */

/* texflag */
public static final int AUTOSPACE=	1;

/* me->flag */
public static final int ME_ISDONE=		1;
public static final int ME_NOPUNOFLIP=	2;
public static final int ME_TWOSIDED=		4;
public static final int ME_UVEFFECT=		8;
public static final int ME_VCOLEFFECT=	16;
public static final int ME_AUTOSMOOTH=	32;
public static final int ME_SMESH=		64;
public static final int ME_SUBSURF=		128;
public static final int ME_OPT_EDGES=	256;

/* me->drawflag, int */
public static final int ME_DRAWEDGES=	(1 << 0);
public static final int ME_DRAWFACES=	(1 << 1);
public static final int ME_DRAWNORMALS=	(1 << 2);
public static final int ME_DRAW_VNORMALS= (1 << 3);

public static final int ME_ALLEDGES=		(1 << 4);
public static final int ME_HIDDENEDGES=  (1 << 5);

public static final int ME_DRAWCREASES=	(1 << 6);
public static final int ME_DRAWSEAMS=    (1 << 7);
public static final int ME_DRAWSHARP=    (1 << 8);
public static final int ME_DRAWBWEIGHTS=	(1 << 8);

public static final int ME_DRAW_EDGELEN=  (1 << 10);
public static final int ME_DRAW_FACEAREA= (1 << 11);
public static final int ME_DRAW_EDGEANG=  (1 << 12);

/* Subsurf Type */
public static final int ME_CC_SUBSURF= 		0;
public static final int ME_SIMPLE_SUBSURF= 	1;

public static final long MESH_MAX_VERTS= 2000000000L;

//#endif
}
