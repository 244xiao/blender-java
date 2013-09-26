/**
 *
 * $Id: DNA_color_types.java,v 1.1.1.1 2009/07/11 21:55:00 jladere Exp $ 
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
 * The Original Code is Copyright (C) 2006 Blender Foundation.
 * All rights reserved.
 *
 * The Original Code is: all of this file.
 *
 * Contributor(s): none yet.
 *
 * ***** END GPL/BL DUAL LICENSE BLOCK *****
 */

package blender.makesdna;

//#ifndef DNA_COLOR_TYPES_H
//#define DNA_COLOR_TYPES_H
//
//#include "DNA_vec_types.h"
//
public class ColorTypes {
/* general defines for kernel functions */
public static final int CM_RESOL= 32;
public static final int CM_TABLE= 256;
public static final float CM_TABLEDIV=		(1.0f/256.0f);

public static final int CM_TOT=	4;

//typedef struct CurveMapPoint {
//	float x, y;
//	short flag, shorty;		/* shorty for result lookup */
//} CurveMapPoint;

/* curvepoint->flag */
public static final int CUMA_SELECT=		1;
public static final int CUMA_VECTOR=		2;

//typedef struct CurveMap {
//	short totpoint, flag;
//
//	float range;					/* quick multiply value for reading table */
//	float mintable, maxtable;		/* the x-axis range for the table */
//	float ext_in[2], ext_out[2];	/* for extrapolated curves, the direction vector */
//	CurveMapPoint *curve;			/* actual curve */
//	CurveMapPoint *table;			/* display and evaluate table */
//	CurveMapPoint *premultable;		/* for RGB curves, premulled table */
//} CurveMap;

/* cuma->flag */
public static final int CUMA_EXTEND_EXTRAPOLATE=	1;

//typedef struct CurveMapping {
//	int flag, cur;					/* cur; for buttons, to show active curve */
//
//	rctf curr, clipr;				/* current rect, clip rect (is default rect too) */
//
//	CurveMap cm[4];					/* max 4 builtin curves per mapping struct now */
//	float black[3], white[3];		/* black/white point (black[0] abused for current frame) */
//	float bwmul[3];					/* black/white point multiply value, for speed */
//
//	float sample[3];				/* sample values, if flag set it draws line and intersection */
//} CurveMapping;

/* cumapping->flag */
public static final int CUMA_DO_CLIP=			1;
public static final int CUMA_PREMULLED=			2;
public static final int CUMA_DRAW_CFRA=			4;
public static final int CUMA_DRAW_SAMPLE=		8;

//#endif
}
