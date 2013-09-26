/**
 * blenlib/DNA_space_types.h (mar-2001 nzc)
 *	
 * $Id: DNA_space_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $ 
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

//#ifndef DNA_SPACE_TYPES_H
//#define DNA_SPACE_TYPES_H
//
//#include "DNA_listBase.h"
//#include "DNA_vec_types.h"
//#include "DNA_oops_types.h"		/* for TreeStoreElem */
//#include "DNA_image_types.h"	/* ImageUser */
///* Hum ... Not really nice... but needed for spacebuts. */
//#include "DNA_view2d_types.h"
//
//struct Ipo;
//struct ID;
//struct Text;
//struct Script;
//struct ImBuf;
//struct Image;
//struct SpaceIpo;
//struct BlendHandle;
//struct RenderInfo;
//struct bNodeTree;
//struct uiBlock;
//struct FileList;
//struct bGPdata;
//

public class SpaceTypes {
//	/**
//	 * The base structure all the other spaces
//	 * are derived (implicitly) from. Would be
//	 * good to make this explicit.
//	 */
//typedef struct SpaceLink SpaceLink;
//struct SpaceLink {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//	short blockhandler[8];
//};
//
//typedef struct SpaceInfo {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//} SpaceInfo;
	
	public static final int INFO_RPT_DEBUG	= 1<<0;
	public static final int INFO_RPT_INFO	= 1<<1;
	public static final int INFO_RPT_OP		= 1<<2;
	public static final int INFO_RPT_WARN	= 1<<3;
	public static final int INFO_RPT_ERR		= 1<<4;

//typedef struct SpaceIpo {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	unsigned int rowbut, pad2;
//	View2D v2d;
//
//	void *editipo;
//	ListBase ipokey;
//
//	/* the ipo context we need to store */
//	struct Ipo *ipo;
//	struct ID *from;
//	char actname[32], constname[32], bonename[32];
//
//	short totipo, pin;
//	short butofs, channel;
//	short showkey, blocktype;
//	short menunr, lock;
//	int flag;
//	float median[3];
//	rctf tot;
//} SpaceIpo;
//
//typedef struct SpaceButs {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//	struct RenderInfo *ri;
//
//	short blockhandler[8];
//
//	short cursens, curact;
//	short align, tabo;		/* align for panels, tab is old tab */
//	View2D v2d;
//
//	short mainb, menunr;	/* texnr and menunr have to remain shorts */
//	short pin, mainbo;
//	void *lockpoin;
//
//	short texnr;
//	char texfrom, showgroup;
//
//	short modeltype;
//	short scriptblock;
//	short scaflag;
//	short re_align;
//
//	short oldkeypress;		/* for keeping track of the sub tab key cycling */
//	char pad, flag;
//
//	char texact, tab[7];	/* storing tabs for each context */
//
//} SpaceButs;
//
//typedef struct SpaceSeq {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	View2D v2d;
//
//	float xof, yof;	/* offset for drawing the image preview */
//	short mainb, pad;
//	short chanshown;
//	short zebra;
//	int flag;
//	float zoom;
//
//	struct bGPdata *gpd;		/* grease-pencil data */
//} SpaceSeq;
//
//typedef struct SpaceFile {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	struct direntry *filelist;
//	int totfile;
//
//	char title[24];
//	char dir[240];
//	char file[80];
//
//	short type, ofs, flag, sort;
//	short maxnamelen, collums, f_fp, pad1;
//	int pad2;
//	char fp_str[8];
//
//	struct BlendHandle *libfiledata;
//
//	unsigned short retval;		/* event */
//	short menu, act, ipotype;
//
//	/* one day we'll add unions to dna */
//	void (*returnfunc)(char *);
//	void (*returnfunc_event)(unsigned short);
//	void (*returnfunc_args)(char *, void *, void *);
//
//	void *arg1, *arg2;
//	short *menup;	/* pointer to menu result or ID browsing */
//	char *pupmenu;	/* optional menu in header */
//} SpaceFile;
//
//typedef struct SpaceOops {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	View2D v2d;
//
//	ListBase oops;
//	short pin, visiflag, flag, rt;
//	void *lockpoin;
//
//	ListBase tree;
//	struct TreeStore *treestore;
//
//	/* search stuff */
//	char search_string[32];
//	struct TreeStoreElem search_tse;
//	int search_flags, do_;
//
//	short type, outlinevis, storeflag;
//	short deps_flags;
//
//} SpaceOops;
//
//typedef struct SpaceImage {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	View2D v2d;
//
//	struct Image *image;
//	struct ImageUser iuser;
//
//	struct CurveMapping *cumap;
//	short mode, menunr;
//	short imanr;
//	short curtile; /* the currently active tile of the image when tile is enabled, is kept in sync with the active faces tile */
//	int flag;
//	short selectmode;
//	short imtypenr, lock;
//	short pin;
//	float zoom;
//	char dt_uv; /* UV draw type */
//	char sticky; /* sticky selection type */
//	char dt_uvstretch;
//	char pad[5];
//
//	float xof, yof;					/* user defined offset, image is centered */
//	float centx, centy;				/* storage for offset while render drawing */
//
//	struct bGPdata *gpd;			/* grease pencil data */
//} SpaceImage;
//
//typedef struct SpaceNla {
//	struct SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	short menunr, lock;
//	short autosnap;			/* this uses the same settings as autosnap for Action Editor */
//	short flag;
//
//	View2D v2d;
//} SpaceNla;
//
//typedef struct SpaceText {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	struct Text *text;
//
//	int top, viewlines;
//	short flags, menunr;
//	int font_id;
//
//	int lheight;
//	int left;
//	int showlinenrs;
//	int tabnumber;
//
//	int currtab_set;
//	int showsyntax;
//	int overwrite;
//	float pix_per_line;
//
//	struct rcti txtscroll, txtbar;
//
//	int wordwrap, doplugins;
//
//} SpaceText;
//
//typedef struct Script {
//	ID id;
//
//	void *py_draw;
//	void *py_event;
//	void *py_button;
//	void *py_browsercallback;
//	void *py_globaldict;
//
//	int flags, lastspace;
//	char scriptname[256]; /* store the script file here so we can re-run it on loading blender, if "Enable Scripts" is on */
//	char scriptarg[256];
//} Script;
//#define SCRIPT_SET_NULL(_script) _script->py_draw = _script->py_event = _script->py_button = _script->py_browsercallback = _script->py_globaldict = NULL; _script->flags = 0;
//#define SCRIPT_RUNNING	0x01
//#define SCRIPT_GUI		0x02
//#define SCRIPT_FILESEL	0x04
//
//typedef struct SpaceScript {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//	struct Script *script;
//
//	short flags, menunr;
//	int pad1;
//
//	void *but_refs;
//} SpaceScript;
//
//typedef struct SpaceTime {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	View2D v2d;
//
//	int flag, redraws;
//
//} SpaceTime;
//
//typedef struct SpaceNode {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	View2D v2d;
//
//	struct ID *id, *from;		/* context, no need to save in file? well... pinning... */
//	short flag, menunr;			/* menunr: browse id block in header */
//	float aspect;
//	void *curfont;
//
//	float xof, yof;	/* offset for drawing the backdrop */
//
//	struct bNodeTree *nodetree, *edittree;
//	int treetype, pad;			/* treetype: as same nodetree->type */
//
//	struct bGPdata *gpd;		/* grease-pencil data */
//} SpaceNode;

/* snode->flag */
public static final int SNODE_BACKDRAW=		2;
public static final int SNODE_DISPGP=		4;
public static final int SNODE_USE_ALPHA=		8;
public static final int SNODE_SHOW_ALPHA=	16;
public static final int SNODE_AUTO_RENDER=	32;

/* snode->texfrom */
public static final int SNODE_TEX_OBJECT=	0;
public static final int SNODE_TEX_WORLD=		1;
public static final int SNODE_TEX_BRUSH=		2;

//typedef struct SpaceImaSel {
//	SpaceLink *next, *prev;
//	int spacetype;
//	float blockscale;
//	struct ScrArea *area;
//
//	short blockhandler[8];
//
//	View2D v2d;
//
//	struct FileList *files;
//
//	/* specific stuff for drawing */
//	char title[24];
//	char dir[240];
//	char file[80];
//
//	short type, menu, flag, sort;
//
//	void *curfont;
//	int	active_file;
//
//	int numtilesx;
//	int numtilesy;
//
//	int selstate;
//
//	struct rcti viewrect;
//	struct rcti bookmarkrect;
//
//	float scrollpos; /* current position of scrollhandle */
//	float scrollheight; /* height of the scrollhandle */
//	float scrollarea; /* scroll region, scrollpos is from 0 to scrollarea */
//
//	float aspect;
//	unsigned short retval;		/* event */
//
//	short ipotype;
//
//	short filter;
//	short active_bookmark;
//	short pad, pad1;
//
//	/* view settings */
//	short prv_w;
//	short prv_h;
//
//	/* one day we'll add unions to dna */
//	void (*returnfunc)(char *);
//	void (*returnfunc_event)(unsigned short);
//	void (*returnfunc_args)(char *, void *, void *);
//
//	void *arg1, *arg2;
//	short *menup;	/* pointer to menu result or ID browsing */
//	char *pupmenu;	/* optional menu in header */
//
//	struct ImBuf *img;
//} SpaceImaSel;


/* **************** SPACE DEFINES ********************* */

/* button defines (deprecated) */
/* warning: the values of these defines are used in sbuts->tabs[8] */
/* sbuts->mainb new */
public static final int CONTEXT_SCENE=	0;
public static final int CONTEXT_OBJECT=	1;
public static final int CONTEXT_TYPES=	2;
public static final int CONTEXT_SHADING=	3;
public static final int CONTEXT_EDITING=	4;
public static final int CONTEXT_SCRIPT=	5;
public static final int CONTEXT_LOGIC=	6;

/* sbuts->mainb old (deprecated) */
public static final int BUTS_VIEW=			0;
public static final int BUTS_LAMP=			1;
public static final int BUTS_MAT=			2;
public static final int BUTS_TEX=			3;
public static final int BUTS_ANIM=			4;
public static final int BUTS_WORLD=			5;
public static final int BUTS_RENDER=			6;
public static final int BUTS_EDIT=			7;
public static final int BUTS_GAME=			8;
public static final int BUTS_FPAINT=			9;
public static final int BUTS_RADIO=			10;
public static final int BUTS_SCRIPT=			11;
public static final int BUTS_SOUND=			12;
public static final int BUTS_CONSTRAINT=		13;
public static final int BUTS_EFFECTS=		14;

/* sbuts->tab new (deprecated) */
public static final int TAB_SHADING_MAT= 	0;
public static final int TAB_SHADING_TEX= 	1;
public static final int TAB_SHADING_RAD= 	2;
public static final int TAB_SHADING_WORLD=	3;
public static final int TAB_SHADING_LAMP=	4;

public static final int TAB_OBJECT_OBJECT=	0;
public static final int TAB_OBJECT_PHYSICS= 	1;
public static final int TAB_OBJECT_PARTICLE=	2;

public static final int TAB_SCENE_RENDER=	0;
public static final int TAB_SCENE_WORLD=     	1;
public static final int TAB_SCENE_ANIM=		2;
public static final int TAB_SCENE_SOUND=		3;
public static final int TAB_SCENE_SEQUENCER=	4;


/* buts->mainb new */
//public static final int BCONTEXT_SCENE=		0;
//public static final int BCONTEXT_WORLD=		1;
//public static final int BCONTEXT_OBJECT=		2;
//public static final int BCONTEXT_DATA=		3;
//public static final int BCONTEXT_MATERIAL=	4;
//public static final int BCONTEXT_TEXTURE=	5;
//public static final int BCONTEXT_PARTICLE=	6;
//public static final int BCONTEXT_PHYSICS=	7;
//public static final int BCONTEXT_BONE=		9;
//public static final int BCONTEXT_MODIFIER=	10;
//public static final int BCONTEXT_CONSTRAINT= 12;
//public static final int BCONTEXT_TOT=		13;
public static final int BCONTEXT_RENDER=				0;
public static final int BCONTEXT_SCENE=				1;
public static final int BCONTEXT_WORLD=				2;
public static final int BCONTEXT_OBJECT=				3;
public static final int BCONTEXT_DATA=				4;
public static final int BCONTEXT_MATERIAL=			5;
public static final int BCONTEXT_TEXTURE=			6;
public static final int BCONTEXT_PARTICLE=			7;
public static final int BCONTEXT_PHYSICS=			8;
public static final int BCONTEXT_BONE=				9;
public static final int BCONTEXT_MODIFIER=			10;
public static final int BCONTEXT_CONSTRAINT=			12;
public static final int BCONTEXT_BONE_CONSTRAINT=	13;
public static final int BCONTEXT_TOT=				14;

/* sbuts->flag */
public static final int SB_PRV_OSA=			1;
public static final int SB_PIN_CONTEXT=		2;
public static final int SB_WORLD_TEX=		4;
public static final int SB_BRUSH_TEX=		8;

/* sbuts->align */
public static final int BUT_FREE=  		0;
public static final int BUT_HORIZONTAL=  1;
public static final int BUT_VERTICAL=    2;
public static final int BUT_AUTO=		3;

/* sbuts->scaflag */
public static final int BUTS_SENS_SEL=		1;
public static final int BUTS_SENS_ACT=		2;
public static final int BUTS_SENS_LINK=		4;
public static final int BUTS_CONT_SEL=		8;
public static final int BUTS_CONT_ACT=		16;
public static final int BUTS_CONT_LINK=		32;
public static final int BUTS_ACT_SEL=		64;
public static final int BUTS_ACT_ACT=		128;
public static final int BUTS_ACT_LINK=		256;
public static final int BUTS_SENS_STATE=		512;
public static final int BUTS_ACT_STATE=		1024;

/* these values need to be hardcoded in structs, dna does not recognize defines */
/* also defined in BKE */
//public static final int FILE_MAXDIR=		160;
//public static final int FILE_MAXFILE=		80;
//public static final int FILE_MAX=			240;

/* filesel types */
public static final int FILE_UNIX=			8;
public static final int FILE_BLENDER=		8; /* dont display relative paths */
public static final int FILE_SPECIAL=		9;

public static final int FILE_LOADLIB=		1;
public static final int FILE_MAIN=			2;
public static final int FILE_LOADFONT=		3;
/* filesel op property -> action */
public static final int FILE_OPENFILE=		0;
public static final int FILE_SAVE=			1;

/* sfile->flag and simasel->flag */
public static final int FILE_SHOWSHORT=		1;
public static final int FILE_STRINGCODE=	2;
public static final int FILE_LINK=			4;
public static final int FILE_HIDE_DOT=		8;
public static final int FILE_AUTOSELECT=	16;
public static final int FILE_ACTIVELAY=		32;
public static final int FILE_ATCURSOR=		64;
public static final int FILE_SYNCPOSE=		128;
public static final int FILE_FILTER=		256;
public static final int FILE_BOOKMARKS=		512;

/* sfile->sort */
public static final int FILE_SORTALPHA=		0;
public static final int FILE_SORTDATE=		1;
public static final int FILE_SORTSIZE=		2;
public static final int FILE_SORTEXTENS=	3;

///* files in filesel list: 2=ACTIVE  */
//public static final int HILITE=				1;
//public static final int ACTIVE=				2;
//public static final int BLENDERFILE=		4;
//public static final int PSXFILE=			8;
//public static final int IMAGEFILE=			16;
//public static final int MOVIEFILE=			32;
//public static final int PYSCRIPTFILE=		64;
//public static final int FTFONTFILE=			128;
//public static final int SOUNDFILE=			256;
//public static final int TEXTFILE=			512;
//public static final int MOVIEFILE_ICON=		1024; /* movie file that preview can't load */
//public static final int FOLDERFILE=			2048; /* represents folders for filtering */
/* files in filesel list: file types */
public static final int BLENDERFILE=			(1<<2);
public static final int BLENDERFILE_BACKUP=	(1<<3);
public static final int IMAGEFILE=			(1<<4);
public static final int MOVIEFILE=			(1<<5);
public static final int PYSCRIPTFILE=		(1<<6);
public static final int FTFONTFILE=			(1<<7);
public static final int SOUNDFILE=			(1<<8);
public static final int TEXTFILE=			(1<<9);
public static final int MOVIEFILE_ICON=		(1<<10); /* movie file that preview can't load */
public static final int FOLDERFILE=			(1<<11); /* represents folders for filtering */
public static final int BTXFILE=				(1<<12);
public static final int COLLADAFILE=			(1<<13);
public static final int OPERATORFILE=		(1<<14); /* from filter_glob operator property */

public static final int SCROLLH=	16;			/* height scrollbar */
public static final int SCROLLB=	16;			/* width scrollbar */

/* SpaceImage->mode */
public static final int SI_TEXTURE=		0;
public static final int SI_SHOW=		1;

/* SpaceImage->dt_uv */
public static final int SI_UVDT_OUTLINE=0;
public static final int SI_UVDT_DASH=	1;
public static final int SI_UVDT_BLACK=	2;
public static final int SI_UVDT_WHITE=	3;

/* SpaceImage->dt_uvstretch */
public static final int SI_UVDT_STRETCH_ANGLE=	0;
public static final int SI_UVDT_STRETCH_AREA=	1;

/* SpaceImage->sticky
 * Note DISABLE should be 0, however would also need to re-arrange icon order,
 * also, sticky loc is the default mode so this means we dont need to 'do_versons' */
public static final int SI_STICKY_LOC=		0;
public static final int SI_STICKY_DISABLE=	1;
public static final int SI_STICKY_VERTEX=	2;

/* SpaceImage->selectmode */
public static final int SI_SELECT_VERTEX=	0;
public static final int SI_SELECT_EDGE=		1; /* not implemented */
public static final int SI_SELECT_FACE=		2;
public static final int SI_SELECT_ISLAND=	3;

/* SpaceImage->flag */
public static final int SI_BE_SQUARE=	1<<0;
public static final int SI_EDITTILE=	1<<1;
public static final int SI_CLIP_UV=		1<<2;
public static final int SI_DRAWTOOL=	1<<3;
public static final int SI_DEPRECATED1= 1<<4;	/* stick UVs to others in the same location */
public static final int SI_DRAWSHADOW=  1<<5;
public static final int SI_SELACTFACE=  1<<6;	/* deprecated */
public static final int SI_DEPRECATED2=	1<<7;
public static final int SI_DEPRECATED3= 1<<8;	/* stick UV selection to mesh vertex (UVs wont always be touching) */
public static final int SI_COORDFLOATS= 1<<9;
public static final int SI_PIXELSNAP=	1<<10;
public static final int SI_LIVE_UNWRAP=	1<<11;
public static final int SI_USE_ALPHA=	1<<12;
public static final int SI_SHOW_ALPHA=	1<<13;
public static final int SI_SHOW_ZBUF=	1<<14;
		/* next two for render window dislay */
public static final int SI_PREVSPACE=	1<<15;
public static final int SI_FULLWINDOW=	1<<16;
public static final int SI_SYNC_UVSEL=	1<<17;
public static final int SI_LOCAL_UV=	1<<18;
		/* this means that the image is drawn until it reaches the view edge,
		 * in the image view, its unrelated to the 'tile' mode for texface */
public static final int SI_DRAW_TILE=	1<<19;
public static final int SI_SMOOTH_UV=	1<<20;
public static final int SI_DRAW_STRETCH=1<<21;
public static final int SI_DISPGP=		1<<22;
public static final int SI_DRAW_OTHER=	1<<23;
public static final int SI_COLOR_CORRECTION=	1<<24;

/* SpaceIpo->flag */
public static final int SIPO_LOCK_VIEW=			1<<0;
public static final int SIPO_NOTRANSKEYCULL=	1<<1;

/* SpaceText flags (moved from DNA_text_types.h) */

public static final int ST_SCROLL_SELECT=        0x0001; // scrollable
public static final int ST_CLEAR_NAMESPACE=      0x0010; // clear namespace after script
                                       // execution (see BPY_main.c)

public static final int	ST_FIND_WRAP=			0x0020;
public static final int	ST_FIND_ALL=				0x0040;

/* stext->findstr/replacestr */
public static final int ST_MAX_FIND_STR=		256;

///* SpaceOops->type */
//public static final int SO_OOPS=		0;
//public static final int SO_OUTLINER=	1;
//public static final int SO_DEPSGRAPH=   2;

/* SpaceOops->flag */
public static final int SO_TESTBLOCKS=	1;
public static final int SO_NEWSELECTED=	2;
public static final int SO_HIDE_RESTRICTCOLS=	4;
public static final int SO_HIDE_KEYINGSETINFO=		8;

///* SpaceOops->visiflag */
//public static final int OOPS_SCE=	1;
//public static final int OOPS_OB=	2;
//public static final int OOPS_ME=	4;
//public static final int OOPS_CU=	8;
//public static final int OOPS_MB=	16;
//public static final int OOPS_LT=	32;
//public static final int OOPS_LA=	64;
//public static final int OOPS_MA=	128;
//public static final int OOPS_TE=	256;
//public static final int OOPS_IP=	512;
//public static final int OOPS_LAY=	1024;
//public static final int OOPS_LI=	2048;
//public static final int OOPS_IM=	4096;
//public static final int OOPS_AR=	8192;
//public static final int OOPS_GR=	16384;
//public static final int OOPS_CA=	32768;

/* SpaceOops->outlinevis */
public static final int SO_ALL_SCENES=	0;
public static final int SO_CUR_SCENE=	1;
public static final int SO_VISIBLE=		2;
public static final int SO_SELECTED=	3;
public static final int SO_ACTIVE=		4;
public static final int SO_SAME_TYPE=	5;
public static final int SO_GROUPS=		6;
public static final int SO_LIBRARIES=	7;
public static final int SO_VERSE_SESSION=8;
public static final int SO_VERSE_MS=	9;
public static final int SO_SEQUENCE=	10;
public static final int SO_DATABLOCKS=	11;
public static final int SO_USERDEF=		12;
public static final int SO_KEYMAP=		13;

/* SpaceOops->storeflag */
public static final int SO_TREESTORE_CLEANUP=	1;
		/* if set, it allows redraws. gets set for some allqueue events */
public static final int SO_TREESTORE_REDRAW=	2;

/* outliner search flags (SpaceOops->search_flags) */
public static final int SO_FIND_CASE_SENSITIVE=		(1<<0);
public static final int SO_FIND_COMPLETE=			(1<<1);

/* headerbuttons: 450-499 */

public static final int B_IMASELHOME=		451;
public static final int B_IMASELREMOVEBIP=	452;

public static final int C_BACK= 0xBAAAAA;
public static final int C_DARK= 0x665656;
public static final int C_DERK= 0x766666;
public static final int C_HI=	0xCBBBBB;
public static final int C_LO=	0x544444;

/* queue settings */
public static final int IMS_KNOW_WIN=       1;
public static final int IMS_KNOW_BIP=       2;
public static final int IMS_KNOW_DIR=       4;
public static final int IMS_DOTHE_INF=		8;
public static final int IMS_KNOW_INF=	   16;
public static final int IMS_DOTHE_IMA=	   32;
public static final int IMS_KNOW_IMA=	   64;
public static final int IMS_FOUND_BIP=	  128;
public static final int IMS_DOTHE_BIP=	  256;
public static final int IMS_WRITE_NO_BIP= 512;

/* imasel->mode */
public static final int IMS_NOIMA=			0;
public static final int IMS_IMA=			1;
public static final int IMS_ANIM=			2;
public static final int IMS_DIR=			4;
public static final int IMS_FILE=			8;
public static final int IMS_STRINGCODE=		16;

public static final int IMS_INDIR=			1;
public static final int IMS_INDIRSLI=		2;
public static final int IMS_INFILE=			3;
public static final int IMS_INFILESLI=		4;

/* nla->flag */
//public static final int SNLA_ALLKEYED=		1;
//public static final int SNLA_ACTIVELAYERS=	2;
//public static final int SNLA_DRAWTIME=		4;
//public static final int SNLA_NOTRANSKEYCULL=8;

public static final int SNLA_DRAWTIME=		(1<<2);
public static final int SNLA_NOTRANSKEYCULL=	(1<<3);
public static final int SNLA_NODRAWCFRANUM=	(1<<4);
public static final int SNLA_NOSTRIPCURVES=	(1<<5);

/* time->flag */
	/* show timing in frames instead of in seconds */
public static final int TIME_DRAWFRAMES=	1;
	/* temporary flag set when scrubbing time */
public static final int TIME_CFRA_NUM=		2;
	/* only keyframes from active/selected channels get shown */
public static final int TIME_ONLYACTSEL=	4;

/* time->redraws */
public static final int TIME_REGION=	1;
public static final int TIME_ALL_3D_WIN=		2;
public static final int TIME_ALL_ANIM_WIN=		4;
public static final int TIME_ALL_BUTS_WIN=		8;
public static final int TIME_WITH_SEQ_AUDIO=	16;
public static final int TIME_SEQ=				32;
public static final int TIME_ALL_IMAGE_WIN=		64;
public static final int TIME_CONTINUE_PHYSICS=	128;
public static final int TIME_NODES=				256;

/* time->cache */
public static final int TIME_CACHE_DISPLAY=		1;
public static final int TIME_CACHE_SOFTBODY=		2;
public static final int TIME_CACHE_PARTICLES=	4;
public static final int TIME_CACHE_CLOTH=		8;
public static final int TIME_CACHE_SMOKE=		16;

/* sseq->mainb */
public static final int SEQ_DRAW_SEQUENCE=         0;
public static final int SEQ_DRAW_IMG_IMBUF=        1;
public static final int SEQ_DRAW_IMG_WAVEFORM=     2;
public static final int SEQ_DRAW_IMG_VECTORSCOPE=  3;
public static final int SEQ_DRAW_IMG_HISTOGRAM=    4;

/* sseq->flag */
public static final int SEQ_DRAWFRAMES=   1;
public static final int SEQ_MARKER_TRANS= 2;
public static final int SEQ_DRAW_COLOR_SEPERATED=    4;
public static final int SEQ_DRAW_SAFE_MARGINS=       8;
public static final int SEQ_DRAW_GPENCIL=			16;

///* space types, moved from DNA_screen_types.h */
//enum {
	public static final int SPACE_EMPTY = 0;
	public static final int SPACE_VIEW3D = 1;
	public static final int SPACE_IPO = 2;
	public static final int SPACE_OUTLINER = 3;
	public static final int SPACE_BUTS = 4;
	public static final int SPACE_FILE = 5;
	public static final int SPACE_IMAGE = 6;
	public static final int SPACE_INFO = 7;
	public static final int SPACE_SEQ = 8;
	public static final int SPACE_TEXT = 9;
	public static final int SPACE_IMASEL = 10;
	public static final int SPACE_SOUND = 11;
	public static final int SPACE_ACTION = 12;
	public static final int SPACE_NLA = 13;
	public static final int SPACE_SCRIPT = 14;
	public static final int SPACE_TIME = 15;
	public static final int SPACE_NODE = 16;
    public static final int SPACE_LOGIC = 17;
	public static final int SPACE_CONSOLE = 18;
	public static final int SPACE_USERPREF = 19;

	public static final int SPACEICONMAX = SPACE_USERPREF;
///*	SPACE_LOGIC	*/
//};
//
//#endif
}
