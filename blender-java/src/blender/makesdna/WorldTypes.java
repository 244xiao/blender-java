/**
 * blenlib/DNA_world_types.h (mar-2001 nzc)
 *
 * $Id: DNA_world_types.java,v 1.1.1.1 2009/07/11 21:55:00 jladere Exp $ 
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

//#ifndef DNA_WORLD_TYPES_H
//#define DNA_WORLD_TYPES_H
//
//#include "DNA_ID.h"
//#include "DNA_scriptlink_types.h"
//
public class WorldTypes {
//struct Ipo;
//struct MTex;
//
//#ifndef MAX_MTEX
//#define MAX_MTEX	18
//#endif
//
//
///**
// * World defines general modeling data such as a background fill,
// * gravity, color model, stars, etc. It mixes game-data, rendering
// * data and modeling data. */
//typedef struct World {
//	ID id;
//
//	short colormodel, totex;
//	short texact, mistype;
//
//	/* TODO - hork, zenk and ambk are not used, should remove at some point (Campbell) */
//	float horr, horg, horb, hork;
//	float zenr, zeng, zenb, zenk;
//	float ambr, ambg, ambb, ambk;
//
//	unsigned int fastcol;
//
//	/**
//	 * Exposure= mult factor. unused now, but maybe back later. Kept in to be upward compat.
//	 * New is exp/range control. linfac & logfac are constants... don't belong in
//	 * file, but allocating 8 bytes for temp mem isnt useful either.
//	 */
//	float exposure, exp, range;
//	float linfac, logfac;
//
//	/**
//	 * Gravitation constant for the game world
//	 */
//	float gravity;
//
//	/**
//	 * Radius of the activity bubble, in Manhattan length. Objects
//	 * outside the box are activity-culled. */
//	float activityBoxRadius;
//
//	short skytype;
//	/**
//	 * Some world modes
//	 * bit 0: Do mist
//	 * bit 1: Do stars
//	 * bit 2: (reserved) depth of field
//	 * bit 3: (gameengine): Activity culling is enabled.
//	 */
//	short mode;
//	int physicsEngine;	/* here it's aligned */
//
//	float misi, miststa, mistdist, misthi;
//
//	float starr, starg, starb, stark;
//	float starsize, starmindist;
//	float stardist, starcolnoise;
//
//	/* unused now: DOF */
//	short dofsta, dofend, dofmin, dofmax;
//
//	/* ambient occlusion */
//	float aodist, aodistfac, aoenergy, aobias;
//	short aomode, aosamp, aomix, aocolor;
//	float ao_adapt_thresh, ao_adapt_speed_fac;
//	float ao_approx_error, ao_approx_correction;
//	short ao_samp_method, ao_gather_method, ao_approx_passes, pad1;
//
//	float *aosphere, *aotables;
//
//
//	struct Ipo *ipo;
//	struct MTex *mtex[18];		/* MAX_MTEX */
//
//	/* previews */
//	struct PreviewImage *preview;
//
//	ScriptLink scriptlink;
//
//} World;

/* **************** WORLD ********************* */

/* skytype */
public static final int WO_SKYBLEND=		1;
public static final int WO_SKYREAL=		2;
public static final int WO_SKYPAPER=		4;
/* while render: */
public static final int WO_SKYTEX=		8;
public static final int WO_ZENUP=		16;

/* mode */
public static final int WO_MIST=	               1;
public static final int WO_STARS=               2;
public static final int WO_DOF=                 4;
public static final int WO_ACTIVITY_CULLING=	   8;
public static final int WO_AMB_OCC=	   		  16;

/* aomix */
public static final int WO_AOADD=	0;
public static final int WO_AOSUB=	1;
public static final int WO_AOADDSUB=	2;

/* ao_samp_method - methods for sampling the AO hemi */
public static final int WO_AOSAMP_CONSTANT=			0;
public static final int WO_AOSAMP_HALTON=			1;
public static final int WO_AOSAMP_HAMMERSLEY=		2;

/* aomode (use distances & random sampling modes) */
public static final int WO_AODIST=		1;
public static final int WO_AORNDSMP=		2;
public static final int WO_AOCACHE=		4;

/* aocolor */
public static final int WO_AOPLAIN=	0;
public static final int WO_AOSKYCOL=	1;
public static final int WO_AOSKYTEX=	2;

/* ao_gather_method */
public static final int WO_AOGATHER_RAYTRACE=	0;
public static final int WO_AOGATHER_APPROX=		1;

/* texco (also in DNA_material_types.h) */
public static final int TEXCO_ANGMAP=	64;
public static final int TEXCO_H_SPHEREMAP=	256;
public static final int TEXCO_H_TUBEMAP=	1024;

/* mapto */
public static final int WOMAP_BLEND=		1;
public static final int WOMAP_HORIZ=		2;
public static final int WOMAP_ZENUP=		4;
public static final int WOMAP_ZENDOWN=	8;
public static final int WOMAP_MIST=		16;

/* physicsEngine */
public static final int WOPHY_NONE=		0;
public static final int WOPHY_ENJI=		1;
public static final int WOPHY_SUMO=		2;
public static final int WOPHY_DYNAMO=	3;
public static final int WOPHY_ODE=		4;
public static final int WOPHY_BULLET=	5;

//#endif
}
