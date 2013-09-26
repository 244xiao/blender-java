/**
 * blenkernel/DNA_userdef_types.h (mar-2001 nzc)
 *
 *	$Id: UserDefTypes.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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

import static blender.blenkernel.Blender.U;

//#ifndef DNA_USERDEF_TYPES_H
//#define DNA_USERDEF_TYPES_H
//
//#include "DNA_listBase.h"
//#include "DNA_texture_types.h"
//
public class UserDefTypes {

public static final int MAX_STYLE_NAME=	64;
public static final int MAX_FONT_NAME=	256;

/* default uifont_id offered by Blender */
public static final int UIFONT_DEFAULT=	0;
public static final int UIFONT_BITMAP=	1;
/* free slots */
public static final int UIFONT_CUSTOM1=	2;
public static final int UIFONT_CUSTOM2=	3;

/* uiFontStyle->align */
public static final int UI_STYLE_TEXT_LEFT=		0;
public static final int UI_STYLE_TEXT_CENTER=	1;
public static final int UI_STYLE_TEXT_RIGHT=		2;

///* themes; defines in BIF_resource.h */
//struct ColorBand;
//
///* global, button colors */
//typedef struct ThemeUI {
//	char outline[4];
//	char neutral[4];
//	char action[4];
//	char setting[4];
//	char setting1[4];
//	char setting2[4];
//	char num[4];
//	char textfield[4];
//	char textfield_hi[4];
//	char popup[4];
//	char text[4];
//	char text_hi[4];
//	char menu_back[4];
//	char menu_item[4];
//	char menu_hilite[4];
//	char menu_text[4];
//	char menu_text_hi[4];
//
//	char but_drawtype;
//	char pad[3];
//	char iconfile[80];	// FILE_MAXFILE length
//} ThemeUI;
//
///* try to put them all in one, if needed a special struct can be created as well
// * for example later on, when we introduce wire colors for ob types or so...
// */
//typedef struct ThemeSpace {
//	char back[4];
//	char text[4];
//	char text_hi[4];
//	char header[4];
//	char panel[4];
//
//	char shade1[4];
//	char shade2[4];
//
//	char hilite[4];
//	char grid[4];
//
//	char wire[4], select[4];
//	char lamp[4];
//	char active[4], group[4], group_active[4], transform[4];
//	char vertex[4], vertex_select[4];
//	char edge[4], edge_select[4];
//	char edge_seam[4], edge_sharp[4], edge_facesel[4];
//	char face[4], face_select[4];	// solid faces
//	char face_dot[4];				// selected color
//	char normal[4];
//	char bone_solid[4], bone_pose[4];
//	char strip[4], strip_select[4];
//	char cframe[4], pad[4];
//
//	char vertex_size, facedot_size;
//	char bpad[2];
//
//	char syntaxl[4], syntaxn[4], syntaxb[4]; // syntax for textwindow and nodes
//	char syntaxv[4], syntaxc[4];
//
//	char movie[4], image[4], scene[4], audio[4];		// for sequence editor
//	char effect[4], plugin[4], transition[4], meta[4];
//	char editmesh_active[4];
//
//	char handle_vertex[4];
//	char handle_vertex_select[4];
//	char handle_vertex_size;
//	char hpad[7];
//} ThemeSpace;
//
//
///* set of colors for use as a custom color set for Objects/Bones wire drawing */
//typedef struct ThemeWireColor {
//	char 	solid[4];
//	char	select[4];
//	char 	active[4];
//
//	short 	flag;
//	short 	pad;
//} ThemeWireColor;

/* flags for ThemeWireColor */
public static final int TH_WIRECOLOR_CONSTCOLS=	(1<<0);
public static final int TH_WIRECOLOR_TEXTCOLS=	(1<<1);

///* A theme */
//typedef struct bTheme {
//	struct bTheme *next, *prev;
//	char name[32];
//
//	/* Interface Elements (buttons, menus, icons) */
//	ThemeUI tui;
//
//	/* Individual Spacetypes */
//	ThemeSpace tbuts;
//	ThemeSpace tv3d;
//	ThemeSpace tfile;
//	ThemeSpace tipo;
//	ThemeSpace tinfo;
//	ThemeSpace tsnd;
//	ThemeSpace tact;
//	ThemeSpace tnla;
//	ThemeSpace tseq;
//	ThemeSpace tima;
//	ThemeSpace timasel;
//	ThemeSpace text;
//	ThemeSpace toops;
//	ThemeSpace ttime;
//	ThemeSpace tnode;
//
//	/* 20 sets of bone colors for this theme */
//	ThemeWireColor tarm[20];
//	/*ThemeWireColor tobj[20];*/
//
//	unsigned char bpad[4], bpad1[4];
//} bTheme;
//
//typedef struct SolidLight {
//	int flag, pad;
//	float col[4], spec[4], vec[4];
//} SolidLight;
//
//typedef struct UserDef {
//	int flag, dupflag;
//	int savetime;
//	char tempdir[160];	// FILE_MAXDIR length
//	char fontdir[160];
//	char renderdir[160];
//	char textudir[160];
//	char plugtexdir[160];
//	char plugseqdir[160];
//	char pythondir[160];
//	char sounddir[160];
//	/* yafray: temporary xml export directory */
//	char yfexportdir[160];
//	short versions, vrmlflag;	// tmp for export, will be replaced by strubi
//	int gameflags;
//	int wheellinescroll;
//	int uiflag, language;
//	short userpref, viewzoom;
//	short console_buffer;	//console vars here for tuhopuu compat, --phase
//	short console_out;
//	int mixbufsize;
//	int fontsize;
//	short encoding;
//	short transopts;
//	short menuthreshold1, menuthreshold2;
//	char fontname[256];		// FILE_MAXDIR+FILE length
//	struct ListBase themes;
//	short undosteps;
//	short undomemory;
//	short gp_manhattendist, gp_euclideandist, gp_eraser;
//	short gp_settings;
//	short tb_leftmouse, tb_rightmouse;
//	struct SolidLight light[3];
//	short tw_hotspot, tw_flag, tw_handlesize, tw_size;
//	int textimeout, texcollectrate;
//	int memcachelimit;
//	int prefetchframes;
//	short frameserverport;
//	short pad_rot_angle;	/*control the rotation step of the view when PAD2,PAD4,PAD6&PAD8 is use*/
//	short obcenter_dia;
//	short rvisize;			/* rotating view icon size */
//	short rvibright;		/* rotating view icon brightness */
//	short recent_files;		/* maximum number of recently used files to remember  */
//	short smooth_viewtx;	/* miliseconds to spend spinning the view */
//	short glreslimit;
//	short ndof_pan, ndof_rotate;
//	short curssize, pad;
////	char pad[8];
//	char versemaster[160];
//	char verseuser[160];
//	float glalphaclip;
//
//	short autokey_mode;		/* autokeying mode */
//	short autokey_flag;		/* flags for autokeying */
//
//	struct ColorBand coba_weight;	/* from texture.h */
//} UserDef;
//
//extern UserDef U; /* from usiblender.c !!!! */

/* ***************** USERDEF ****************** */

/* flag */
public static final int USER_AUTOSAVE=			(1 << 0);
public static final int USER_AUTOGRABGRID=		(1 << 1);
public static final int USER_AUTOROTGRID=		(1 << 2);
public static final int USER_AUTOSIZEGRID=		(1 << 3);
public static final int USER_SCENEGLOBAL=		(1 << 4);
public static final int USER_TRACKBALL=			(1 << 5);
public static final int USER_DUPLILINK=			(1 << 6);
public static final int USER_FSCOLLUM=			(1 << 7);
public static final int USER_MAT_ON_OB=			(1 << 8);
/*public static final int USER_NO_CAPSLOCK		(1 << 9)*/ /* not used anywhere */
public static final int USER_VIEWMOVE=			(1 << 10);
public static final int USER_TOOLTIPS=			(1 << 11);
public static final int USER_TWOBUTTONMOUSE=		(1 << 12);
public static final int USER_NONUMPAD=			(1 << 13);
public static final int USER_LMOUSESELECT=		(1 << 14);
public static final int USER_FILECOMPRESS=		(1 << 15);
public static final int USER_SAVE_PREVIEWS=		(1 << 16);
public static final int USER_CUSTOM_RANGE=		(1 << 17);
public static final int USER_ADD_EDITMODE=		(1 << 18);
public static final int USER_ADD_VIEWALIGNED=	(1 << 19);
public static final int USER_RELPATHS=			(1 << 20);
public static final int USER_RELEASECONFIRM=		(1 << 21);
public static final int USER_SCRIPT_AUTOEXEC_DISABLE=	(1 << 22);
public static final int USER_FILENOUI=			(1 << 23);
public static final int USER_NONEGFRAMES=		(1 << 24);
public static final int USER_TXT_TABSTOSPACES_DISABLE=	(1 << 25);
public static final int USER_TOOLTIPS_PYTHON=    (1 << 26);

/* helper macro for checking frame clamping */
public static final int FRAMENUMBER_MIN_CLAMP(int cfra)
{
	if ((U.flag & USER_NONEGFRAMES)!=0 && (cfra < 0))
		return 0;
	return cfra;
}

/* viewzom */
public static final int USER_ZOOM_CONT=			0;
public static final int USER_ZOOM_SCALE=			1;
public static final int USER_ZOOM_DOLLY=			2;

/* uiflag */
// old flag for public static final int	USER_KEYINSERTACT		(1 << 0)
// old flag for public static final int	USER_KEYINSERTOBJ		(1 << 1)
public static final int USER_WHEELZOOMDIR=		(1 << 2);
public static final int USER_FILTERFILEEXTS=		(1 << 3);
public static final int USER_DRAWVIEWINFO=		(1 << 4);
public static final int USER_PLAINMENUS=			(1 << 5);		// old EVTTOCONSOLE print ghost events, here for tuhopuu compat. --phase
								// old flag for hide pulldown was here 
public static final int USER_FLIPFULLSCREEN=		(1 << 7);
public static final int USER_ALLWINCODECS=		(1 << 8);
public static final int USER_MENUOPENAUTO=		(1 << 9);
public static final int USER_PANELPINNED=		(1 << 10);
public static final int USER_AUTOPERSP=     		(1 << 11);
public static final int USER_LOCKAROUND=     	(1 << 12);
public static final int USER_GLOBALUNDO=     	(1 << 13);
public static final int USER_ORBIT_SELECTION=	(1 << 14);
// old flag for public static final int USER_KEYINSERTAVAI		(1 << 15)
public static final int USER_ORBIT_ZBUF=			(1 << 15);
public static final int USER_HIDE_DOT=			(1 << 16);
public static final int USER_SHOW_ROTVIEWICON=	(1 << 17);
public static final int USER_SHOW_VIEWPORTNAME=	(1 << 18);
// old flag for public static final int USER_KEYINSERTNEED		(1 << 19)
public static final int USER_ZOOM_TO_MOUSEPOS=	(1 << 20);
public static final int USER_SHOW_FPS=			(1 << 21);
public static final int USER_MMB_PASTE=			(1 << 22);
public static final int USER_MENUFIXEDORDER=		(1 << 23);

/* Auto-Keying mode */
	/* AUTOKEY_ON is a bitflag */
public static final int 	AUTOKEY_ON=				1;
	/* AUTOKEY_ON + 2**n...  (i.e. AUTOKEY_MODE_NORMAL = AUTOKEY_ON + 2) to preserve setting, even when autokey turned off  */
public static final int		AUTOKEY_MODE_NORMAL=		3;
public static final int		AUTOKEY_MODE_EDITKEYS=	5;

/* Auto-Keying flag
 * U.autokey_flag (not strictly used when autokeying only - is also used when keyframing these days)
 * note: AUTOKEY_FLAG_* is used with a macro, search for lines like IS_AUTOKEY_FLAG(INSERTAVAIL)
 */
public static final int		AUTOKEY_FLAG_INSERTAVAIL=	(1<<0);
public static final int		AUTOKEY_FLAG_INSERTNEEDED=	(1<<1);
public static final int		AUTOKEY_FLAG_AUTOMATKEY=		(1<<2);
public static final int		AUTOKEY_FLAG_XYZ2RGB=		(1<<3);
/* U.autokey_flag (strictly autokeying only) */
public static final int 	AUTOKEY_FLAG_ONLYKEYINGSET=	(1<<6);
/* toolsettings->autokey_flag */
public static final int 	ANIMRECORD_FLAG_WITHNLA=		(1<<10);

/* transopts */
public static final int	USER_TR_TOOLTIPS=		(1 << 0);
public static final int	USER_TR_BUTTONS=			(1 << 1);
public static final int USER_TR_MENUS=			(1 << 2);
public static final int USER_TR_FILESELECT=		(1 << 3);
public static final int USER_TR_TEXTEDIT=		(1 << 4);
public static final int USER_DOTRANSLATE=		(1 << 5);
public static final int USER_USETEXTUREFONT=		(1 << 6);
public static final int CONVERT_TO_UTF8=			(1 << 7);

/* dupflag */
public static final int USER_DUP_MESH=			(1 << 0);
public static final int USER_DUP_CURVE=			(1 << 1);
public static final int USER_DUP_SURF=			(1 << 2);
public static final int USER_DUP_FONT=			(1 << 3);
public static final int USER_DUP_MBALL=			(1 << 4);
public static final int USER_DUP_LAMP=			(1 << 5);
public static final int USER_DUP_IPO=			(1 << 6);
public static final int USER_DUP_MAT=			(1 << 7);
public static final int USER_DUP_TEX=			(1 << 8);
public static final int	USER_DUP_ARM=			(1 << 9);
public static final int	USER_DUP_ACT=			(1 << 10);

/* gameflags */
public static final int USER_DEPRECATED_FLAG=	1;
public static final int USER_DISABLE_SOUND=		2;
public static final int USER_DISABLE_MIPMAP=		4;

/* wm draw method */
public static final int USER_DRAW_TRIPLE=		0;
public static final int USER_DRAW_OVERLAP=		1;
public static final int USER_DRAW_FULL=			2;

/* tw_flag (transform widget) */

/* gp_settings (Grease Pencil Settings) */
public static final int GP_PAINT_DOSMOOTH=		(1<<0);
public static final int GP_PAINT_DOSIMPLIFY=		(1<<1);

/* theme drawtypes */
public static final int TH_MINIMAL=  	0;
public static final int TH_ROUNDSHADED=	1;
public static final int TH_ROUNDED=  	2;
public static final int TH_OLDSKOOL= 	3;
public static final int TH_SHADED=   	4;

}
