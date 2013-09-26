/**
 * blenlib/DNA_screen_types.h (mar-2001 nzc)
 *
 * $Id: DNA_screen_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $ 
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

//#ifndef DNA_SCREEN_TYPES_H
//#define DNA_SCREEN_TYPES_H
//
//#include "DNA_listBase.h"
//#include "DNA_vec_types.h"
//
//#include "DNA_ID.h"
//#include "DNA_scriptlink_types.h"
//

public class ScreenTypes {
//struct Scene;
//
//typedef struct bScreen {
//	ID id;
//	ListBase vertbase, edgebase, areabase;
//	struct Scene *scene;
//	short startx, endx, starty, endy;	/* framebuffer coords */
//	short sizex, sizey;
//	short scenenr, screennr;			/* only for pupmenu */
//	short full, pad;
//	short mainwin, winakt;
//	short handler[8];					/* similar to space handler now */
//} bScreen;
//
//typedef struct ScrVert {
//	struct ScrVert *next, *prev, *newv;
//	vec2s vec;
//	int flag;
//} ScrVert;
//
//typedef struct ScrEdge {
//	struct ScrEdge *next, *prev;
//	ScrVert *v1, *v2;
//	short border;			/* 1 when at edge of screen */
//	short flag;
//	int pad;
//} ScrEdge;
//
//#ifndef DNA_USHORT_FIX
//#define DNA_USHORT_FIX
///**
// * @deprecated This typedef serves to avoid badly typed functions when
// * @deprecated compiling while delivering a proper dna.c. Do not use
// * @deprecated it in any case.
// */
//typedef unsigned short dna_ushort_fix;
//#endif
//
//
//
//typedef struct Panel {		/* the part from uiBlock that needs saved in file */
//	struct Panel *next, *prev;
//	char panelname[64], tabname[64];	/* defined as UI_MAX_NAME_STR */
//	char drawname[64];					/* panelname is identifier for restoring location */
//	short ofsx, ofsy, sizex, sizey;
//	short flag, active;					/* active= used currently by a uiBlock */
//	short control;
//	short snap;
//	short old_ofsx, old_ofsy;		/* for stow */
//	int sortcounter;			/* when sorting panels, it uses this to put new ones in right place */
//	struct Panel *paneltab;		/* this panel is tabbed in *paneltab */
//} Panel;
//
//typedef struct ScrArea {
//	struct ScrArea *next, *prev;
//	ScrVert *v1, *v2, *v3, *v4;
//	bScreen *full;			/* if area==full, this is the parent */
//	float winmat[4][4];
//	rcti totrct, headrct, winrct;
//
//	short headwin, win;
//	short headertype;		/* 0=no header, 1= down, 2= up */
//	char spacetype, butspacetype;	/* SPACE_...  */
//	short winx, winy;		/* size */
//	char head_swap, head_equal;
//	char win_swap, win_equal;
//
//	short headbutlen, headbutofs;
//	short cursor, flag;
//
//	ScriptLink scriptlink;
//
//	ListBase spacedata;
//	ListBase uiblocks;
//	ListBase panels;
//} ScrArea;

//public static final int MAXWIN=		128;

/* swap */
public static final int WIN_BACK_OK=		1;
public static final int WIN_FRONT_OK=	2;
public static final int WIN_EQUAL=		3;

/* area->flag */
public static final int HEADER_NO_PULLDOWN=	1;
public static final int AREA_FLAG_DRAWJOINTO= 2;
public static final int AREA_FLAG_DRAWJOINFROM= 4;

/* If you change EDGEWIDTH, also do the global arrat edcol[]  */
public static final int EDGEWIDTH=	1;
public static final int AREAGRID=	4;
public static final int AREAMINX=	32;
public static final int HEADERY=	26;
public static final int AREAMINY=	(HEADERY+EDGEWIDTH);

public static final int HEADERDOWN=	1;
public static final int HEADERTOP=	2;

public static final int SCREENNORMAL=    0;
public static final int SCREENFULL=      1;
public static final int SCREENAUTOPLAY=  2;
public static final int SCREENTEMP=		3;

/* Panel->snap - for snapping to screen edges */
public static final int PNL_SNAP_NONE=		0;
public static final int PNL_SNAP_TOP=		1;
public static final int PNL_SNAP_RIGHT=		2;
public static final int PNL_SNAP_BOTTOM=	4;
public static final int PNL_SNAP_LEFT=		8;

public static final float PNL_SNAP_DIST=		9.0f;

/* paneltype flag */
public static final int PNL_DEFAULT_CLOSED=		1;
public static final int PNL_NO_HEADER=			2;

/* screen handlers */
public static final int SCREEN_MAXHANDLER=		8;

public static final int SCREEN_HANDLER_ANIM=	1;
public static final int SCREEN_HANDLER_PYTHON=  2;
public static final int SCREEN_HANDLER_VERSE=	3;

/* regiontype, first two are the default set */
public static final int RGN_TYPE_WINDOW=		0;
public static final int RGN_TYPE_HEADER=		1;
public static final int RGN_TYPE_CHANNELS=	2;
public static final int RGN_TYPE_TEMPORARY=	3;
public static final int RGN_TYPE_UI=			4;
public static final int RGN_TYPE_TOOLS=		5;
public static final int RGN_TYPE_TOOL_PROPS=	6;
public static final int RGN_TYPE_PREVIEW=	7;

/* region alignment */
public static final int RGN_ALIGN_NONE=		0;
public static final int RGN_ALIGN_TOP=		1;
public static final int RGN_ALIGN_BOTTOM=	2;
public static final int RGN_ALIGN_LEFT=		3;
public static final int RGN_ALIGN_RIGHT=		4;
public static final int RGN_ALIGN_HSPLIT=	5;
public static final int RGN_ALIGN_VSPLIT=	6;
public static final int RGN_ALIGN_FLOAT=		7;
public static final int RGN_ALIGN_QSPLIT=	8;
public static final int RGN_OVERLAP_TOP=		9;
public static final int RGN_OVERLAP_BOTTOM=	10;
public static final int RGN_OVERLAP_LEFT=	11;
public static final int RGN_OVERLAP_RIGHT=	12;

public static final int RGN_SPLIT_PREV=		32;

/* region flag */
public static final int RGN_FLAG_HIDDEN=		1;
public static final int RGN_FLAG_TOO_SMALL=	2;

}

