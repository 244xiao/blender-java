/**
 * $Id: DNA_customdata_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $
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

//#ifndef DNA_CUSTOMDATA_TYPES_H
//#define DNA_CUSTOMDATA_TYPES_H
//

public class CustomDataTypes {
///* descriptor and storage for a custom data layer */
//typedef struct CustomDataLayer {
//	int type;       /* type of data in layer */
//	int offset;     /* in editmode, offset of layer in block */
//	int flag;       /* general purpose flag */
//	int active;     /* number of the active layer of this type */
//	int active_rnd; /* number of the layer to render*/
//	char pad[4];
//	char name[32];  /* layer name */
//	void *data;     /* layer data */
//} CustomDataLayer;
//
///* structure which stores custom element data associated with mesh elements
// * (vertices, edges or faces). The custom data is organised into a series of
// * layers, each with a data type (e.g. MTFace, MDeformVert, etc.). */
//typedef struct CustomData {
//	CustomDataLayer *layers;  /* CustomDataLayers, ordered by type */
//	int totlayer, maxlayer;   /* number of layers, size of layers array */
//	int totsize, pad;         /* in editmode, total size of all data layers */
//	void *pool;		  /* for Bmesh: Memory pool for allocation of blocks*/
//} CustomData;

/* CustomData.type */
public static final int CD_MVERT=		0;
public static final int CD_MSTICKY=		1;
public static final int CD_MDEFORMVERT=	2;
public static final int CD_MEDGE=		3;
public static final int CD_MFACE=		4;
public static final int CD_MTFACE=		5;
public static final int CD_MCOL=		6;
public static final int CD_ORIGINDEX=	7;
public static final int CD_NORMAL=		8;
public static final int CD_FLAGS=		9;
public static final int CD_PROP_FLT=	10;
public static final int CD_PROP_INT=	11;
public static final int CD_PROP_STR=	12;
public static final int CD_ORIGSPACE=	13; /* for modifier stack face location mapping */
public static final int CD_ORCO=		14;
public static final int CD_MTEXPOLY=	15;
public static final int CD_MLOOPUV=		16;
public static final int CD_MLOOPCOL=	17;
public static final int CD_TANGENT=		18;
public static final int CD_MDISPS=	19;
public static final int CD_WEIGHT_MCOL=	20; /* for displaying weightpaint colors */
public static final int CD_NUMTYPES=		21;


/* Bits for CustomDataMask */
public static final int CD_MASK_MVERT=		(1 << CD_MVERT);
public static final int CD_MASK_MSTICKY=	(1 << CD_MSTICKY);
public static final int CD_MASK_MDEFORMVERT=(1 << CD_MDEFORMVERT);
public static final int CD_MASK_MEDGE=		(1 << CD_MEDGE);
public static final int CD_MASK_MFACE=		(1 << CD_MFACE);
public static final int CD_MASK_MTFACE=		(1 << CD_MTFACE);
public static final int CD_MASK_MCOL=		(1 << CD_MCOL);
public static final int CD_MASK_ORIGINDEX=	(1 << CD_ORIGINDEX);
public static final int CD_MASK_NORMAL=		(1 << CD_NORMAL);
public static final int CD_MASK_FLAGS=		(1 << CD_FLAGS);
public static final int CD_MASK_PROP_FLT=	(1 << CD_PROP_FLT);
public static final int CD_MASK_PROP_INT=	(1 << CD_PROP_INT);
public static final int CD_MASK_PROP_STR=	(1 << CD_PROP_STR);
public static final int CD_MASK_ORIGSPACE=	(1 << CD_ORIGSPACE);
public static final int CD_MASK_ORCO=		(1 << CD_ORCO);
public static final int CD_MASK_MTEXPOLY=	(1 << CD_MTEXPOLY);
public static final int CD_MASK_MLOOPUV=	(1 << CD_MLOOPUV);
public static final int CD_MASK_MLOOPCOL=	(1 << CD_MLOOPCOL);
public static final int CD_MASK_TANGENT=	(1 << CD_TANGENT);
public static final int CD_MASK_MDISPS=		(1 << CD_MDISPS);
public static final int CD_MASK_WEIGHT_MCOL=	(1 << CD_WEIGHT_MCOL);


/* CustomData.flag */

/* indicates layer should not be copied by CustomData_from_template or
 * CustomData_copy_data */
public static final int CD_FLAG_NOCOPY=    (1<<0);
/* indicates layer should not be freed (for layers backed by external data) */
public static final int CD_FLAG_NOFREE=    (1<<1);
/* indicates the layer is only temporary, also implies no copy */
public static final int CD_FLAG_TEMPORARY= ((1<<2)|CD_FLAG_NOCOPY);

/* Limits */
public static final int MAX_MTFACE= 8;
public static final int MAX_MCOL=   8;

//#endif
}
