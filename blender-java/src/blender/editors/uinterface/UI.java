/**
 * $Id: UI.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation 2002-2008, full recode.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL2;

import blender.blenfont.Blf;
import blender.blenkernel.Pointer;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.bContext.bContextStore;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UIHandlers.uiHandleButtonData;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.editors.uinterface.UILayout.uiLayoutRoot;
import blender.makesdna.UserDefTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.rctf;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.uiStyle;
import blender.makesdna.sdna.wmWindow;
import blender.makesrna.RNATypes;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaDefine;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmSubWindow;
import blender.windowmanager.WmWindow;
import blender.windowmanager.WmTypes.wmEvent;

public class UI {

/* Defines */

/* uiBlock.dt */
public static final int UI_EMBOSS=		0;	/* use widget style for drawing */
public static final int UI_EMBOSSN=		1;	/* Nothing, only icon and/or text */
public static final int UI_EMBOSSP=		2;	/* Pulldown menu style */
public static final int UI_EMBOSST=		3;	/* Table */

/* uiBlock.direction */
public static final int UI_TOP=		1;
public static final int UI_DOWN=		2;
public static final int UI_LEFT=		4;
public static final int UI_RIGHT=	8;
public static final int UI_DIRECTION=	15;
public static final int UI_CENTER=		16;
public static final int UI_SHIFT_FLIPPED=	32;

/* uiBlock.autofill (not yet used) */
public static final int UI_BLOCK_COLLUMNS=	1;
public static final int UI_BLOCK_ROWS=		2;

/* uiBlock.flag (controls) */
public static final int UI_BLOCK_LOOP=			1;
public static final int UI_BLOCK_REDRAW=			2;
public static final int UI_BLOCK_RET_1=			4;		/* XXX 2.5 not implemented */
public static final int UI_BLOCK_NUMSELECT=		8;
public static final int UI_BLOCK_ENTER_OK=		16;
public static final int UI_BLOCK_NOSHADOW=		32;
public static final int UI_BLOCK_UNUSED=			64;
public static final int UI_BLOCK_MOVEMOUSE_QUIT=	128;
public static final int UI_BLOCK_KEEP_OPEN=		256;
public static final int UI_BLOCK_POPUP=			512;
public static final int UI_BLOCK_OUT_1=			1024;
public static final int UI_BLOCK_NO_FLIP=		2048;
public static final int UI_BLOCK_POPUP_MEMORY=	4096;

/* uiPopupBlockHandle.menuretval */
public static final int UI_RETURN_CANCEL=	1;       /* cancel all menus cascading */
public static final int UI_RETURN_OK=        2;       /* choice made */
public static final int UI_RETURN_OUT=       4;       /* left the menu */
public static final int UI_RETURN_UPDATE=    8;       /* update the button that opened */

	/* block.flag bits 12-15 are identical to but.flag bits */

/* panel controls */
public static final int UI_PNL_TRANSP=	1;
public static final int UI_PNL_SOLID=	2;

public static final int UI_PNL_CLOSE=	32;
public static final int UI_PNL_STOW=		64;
public static final int UI_PNL_TO_MOUSE=	128;
public static final int UI_PNL_UNSTOW=	256;
public static final int UI_PNL_SCALE=	512;

/* warning the first 6 flags are internal */
/* but.flag */
public static final int UI_TEXT_LEFT=	64;
public static final int UI_ICON_LEFT=	128;
public static final int UI_ICON_SUBMENU=	256;
	/* control for button type block */
public static final int UI_MAKE_TOP=		512;
public static final int UI_MAKE_DOWN=	1024;
public static final int UI_MAKE_LEFT=	2048;
public static final int UI_MAKE_RIGHT=	4096;

	/* button align flag, for drawing groups together */
//public static final int UI_BUT_ALIGN=		(15<<14);
//public static final int UI_BUT_ALIGN_TOP=	(1<<14);
//public static final int UI_BUT_ALIGN_LEFT=	(1<<15);
//public static final int UI_BUT_ALIGN_RIGHT=	(1<<16);
//public static final int UI_BUT_ALIGN_DOWN=	(1<<17);
//
//public static final int UI_BUT_DISABLED=		(1<<18);
//public static final int UI_BUT_UNUSED=		(1<<19);
//public static final int UI_BUT_ANIMATED=		(1<<20);
//public static final int UI_BUT_ANIMATED_KEY=	(1<<21);
//public static final int UI_BUT_DRIVEN=		(1<<22);
//public static final int UI_BUT_INACTIVE=		(1<<23);
//public static final int UI_BUT_LAST_ACTIVE=	(1<<24);
//
//public static final int UI_PANEL_WIDTH=			340;
//public static final int UI_COMPACT_PANEL_WIDTH=	160;

public static final int  UI_BUT_ALIGN_TOP=	(1<<14);
public static final int  UI_BUT_ALIGN_LEFT=	(1<<15);
public static final int  UI_BUT_ALIGN_RIGHT=	(1<<16);
public static final int  UI_BUT_ALIGN_DOWN=	(1<<17);
public static final int  UI_BUT_ALIGN=		(UI_BUT_ALIGN_TOP|UI_BUT_ALIGN_LEFT|UI_BUT_ALIGN_RIGHT|UI_BUT_ALIGN_DOWN);

public static final int  UI_BUT_DISABLED=		(1<<18);
public static final int  UI_BUT_COLOR_LOCK=	(1<<19);
public static final int  UI_BUT_ANIMATED=		(1<<20);
public static final int  UI_BUT_ANIMATED_KEY=	(1<<21);
public static final int  UI_BUT_DRIVEN=		(1<<22);
public static final int  UI_BUT_REDALERT=		(1<<23);
public static final int  UI_BUT_INACTIVE=		(1<<24);
public static final int  UI_BUT_LAST_ACTIVE=	(1<<25);
public static final int  UI_BUT_UNDO=			(1<<26);
public static final int  UI_BUT_IMMEDIATE=	(1<<27);
public static final int  UI_BUT_NO_TOOLTIP=	(1<<28);
public static final int  UI_BUT_NO_UTF8=		(1<<29);

public static final int  UI_BUT_VEC_SIZE_LOCK= (1<<30); /* used to flag if color hsv-circle should keep luminance */
public static final int  UI_BUT_COLOR_CUBIC=	(1<<31); /* cubic saturation for the color wheel */

public static final int  UI_PANEL_WIDTH=			340;
public static final int  UI_COMPACT_PANEL_WIDTH=	160;

/* Button types, bits stored in 1 value... and a short even!
- bits 0-4:  bitnr (0-31)
- bits 5-7:  pointer type
- bit  8:    for 'bit'
- bit  9-15: button type (now 6 bits, 64 types)
*/

public static final int CHA=	32;
public static final int SHO=	64;
public static final int INT=	96;
public static final int FLO=	128;
public static final int FUN=	192;
public static final int BIT=	256;

public static final int BUTPOIN=	(128+64+32);

public static final int BUT=	(1<<9);
public static final int ROW=	(2<<9);
public static final int TOG=	(3<<9);
public static final int SLI=	(4<<9);
public static final int	NUM=	(5<<9);
public static final int TEX=	(6<<9);
public static final int TOG3=	(7<<9);
public static final int TOGR=	(8<<9);
public static final int TOGN=	(9<<9);
public static final int LABEL=	(10<<9);
public static final int MENU=	(11<<9);
public static final int ICONROW=	(12<<9);
public static final int ICONTOG=	(13<<9);
public static final int NUMSLI=	(14<<9);
public static final int COL=		(15<<9);
public static final int IDPOIN=	(16<<9);
public static final int HSVSLI= 	(17<<9);
public static final int SCROLL=	(18<<9);
public static final int BLOCK=	(19<<9);
public static final int BUTM=	(20<<9);
public static final int SEPR=	(21<<9);
public static final int LINK=	(22<<9);
public static final int INLINK=	(23<<9);
public static final int KEYEVT=	(24<<9);
public static final int ICONTEXTROW= (25<<9);
public static final int HSVCUBE=		(26<<9);
public static final int PULLDOWN=	(27<<9);
public static final int ROUNDBOX=	(28<<9);
public static final int CHARTAB=		(29<<9);
public static final int BUT_COLORBAND= (30<<9);
public static final int BUT_NORMAL=	(31<<9);
public static final int BUT_CURVE=	(32<<9);
public static final int BUT_TOGDUAL= (33<<9);
public static final int ICONTOGN=	(34<<9);
public static final int FTPREVIEW=	(35<<9);
public static final int NUMABS=		(36<<9);
public static final int TOGBUT=		(37<<9);
public static final int OPTION=		(38<<9);
public static final int OPTIONN=		(39<<9);
public static final int SEARCH_MENU=	(40<<9);
public static final int BUT_EXTRA=	(41<<9);
public static final int HSVCIRCLE=	(42<<9);
public static final int LISTBOX=		(43<<9);
public static final int LISTROW=		(44<<9);
public static final int HOTKEYEVT=	(45<<9);
public static final int BUT_IMAGE=	(46<<9);
public static final int HISTOGRAM=	(47<<9);
public static final int WAVEFORM=	(48<<9);
public static final int VECTORSCOPE=	(49<<9);
public static final int PROGRESSBAR=	(50<<9);

public static final int BUTTYPE=		(63<<9);

/* state for scrolldrawing */
public static final int UI_SCROLL_PRESSED=	1;
public static final int UI_SCROLL_ARROWS=	2;
public static final int UI_SCROLL_NO_OUTLINE=	4;

public static final int UI_ID_RENAME=		1;
public static final int UI_ID_BROWSE=		2;
public static final int UI_ID_ADD_NEW=		4;
public static final int UI_ID_OPEN=			8;
public static final int UI_ID_ALONE=			16;
public static final int UI_ID_DELETE=		32;
public static final int UI_ID_LOCAL=			64;
public static final int UI_ID_AUTO_NAME=		128;
public static final int UI_ID_FAKE_USER=		256;
public static final int UI_ID_PIN=			512;
public static final int UI_ID_BROWSE_RENDER=	1024;
public static final int UI_ID_PREVIEWS=		2048;
public static final int UI_ID_FULL=			(UI_ID_RENAME|UI_ID_BROWSE|UI_ID_ADD_NEW|UI_ID_OPEN|UI_ID_ALONE|UI_ID_DELETE|UI_ID_LOCAL);

/* layout */
public static final int UI_LAYOUT_HORIZONTAL=	0;
public static final int UI_LAYOUT_VERTICAL=		1;

public static final int UI_LAYOUT_PANEL=			0;
public static final int UI_LAYOUT_HEADER=		1;
public static final int UI_LAYOUT_MENU=			2;
public static final int UI_LAYOUT_TOOLBAR=			3;

public static final int UI_UNIT_X=				20;
public static final int UI_UNIT_Y=				20;

public static final int UI_LAYOUT_ALIGN_EXPAND=	0;
public static final int UI_LAYOUT_ALIGN_LEFT=	1;
public static final int UI_LAYOUT_ALIGN_CENTER=	2;
public static final int UI_LAYOUT_ALIGN_RIGHT=	3;

public static final int UI_ITEM_O_RETURN_PROPS=	1;
public static final int UI_ITEM_R_EXPAND=		2;
public static final int UI_ITEM_R_SLIDER=		4;
public static final int UI_ITEM_R_TOGGLE=		8;
public static final int UI_ITEM_R_ICON_ONLY=		16;
public static final int UI_ITEM_R_EVENT=			32;
public static final int UI_ITEM_R_FULL_EVENT=	64;
public static final int UI_ITEM_R_NO_BG=			128;
public static final int UI_ITEM_R_IMMEDIATE=		256;

/* for more readable function names */
public static final int ICON_NULL = 0;

//typedef struct uiListItem {
//	struct uiListItem *next, *prev;
//
//	struct PointerRNA data;
//	uiLayout *layout;
//} uiListItem;

public static interface uiBlockCreateFunc {
    public uiBlock run(bContext C, ARegion ar, Object arg1);
};

/* ****************** general defines ************** */

/* visual types for drawing */
/* for time being separated from functional types */
public static enum uiWidgetTypeEnum {
	/* default */
	UI_WTYPE_REGULAR,

	/* standard set */
	UI_WTYPE_LABEL,
	UI_WTYPE_TOGGLE,
	UI_WTYPE_OPTION,
	UI_WTYPE_RADIO,
	UI_WTYPE_NUMBER,
	UI_WTYPE_SLIDER,
	UI_WTYPE_EXEC,

	/* strings */
	UI_WTYPE_NAME,
	UI_WTYPE_NAME_LINK,
	UI_WTYPE_POINTER_LINK,
	UI_WTYPE_FILENAME,

	/* menus */
	UI_WTYPE_MENU_RADIO,
	UI_WTYPE_MENU_ICON_RADIO,
	UI_WTYPE_MENU_POINTER_LINK,

	UI_WTYPE_PULLDOWN,
	UI_WTYPE_MENU_ITEM,
	UI_WTYPE_MENU_BACK,

	/* specials */
	UI_WTYPE_ICON,
	UI_WTYPE_SWATCH,
	UI_WTYPE_RGB_PICKER,
	UI_WTYPE_NORMAL,
	UI_WTYPE_BOX,
	UI_WTYPE_SCROLL,
	UI_WTYPE_LISTITEM,
	UI_WTYPE_PROGRESSBAR,

};

public static final int UI_MAX_DRAW_STR=	400;
public static final int UI_MAX_NAME_STR=	64;
public static final int UI_ARRAY=	29;

/* panel limits */
public static final int UI_PANEL_MINX=	100;
public static final int UI_PANEL_MINY=	70;

/* uiBut.flag */
public static final int UI_SELECT=		1;
public static final int UI_MOUSE_OVER=	2;
public static final int UI_ACTIVE=		4;
public static final int UI_HAS_ICON=		8;
public static final int UI_TEXTINPUT=	16;
public static final int UI_HIDDEN=		32;
/* warn: rest of uiBut.flag in UI_interface.h */

/* internal panel drawing defines */
public static final int PNL_GRID=	4;
public static final int PNL_HEADER=  20;

/* panel.flag */
public static final int PNL_SELECT=	1;
public static final int PNL_CLOSEDX=	2;
public static final int PNL_CLOSEDY=	4;
public static final int PNL_CLOSED=	6;
public static final int PNL_TABBED=	8;
public static final int PNL_OVERLAP=	16;

/* Button text selection:
 * extension direction, selextend, inside ui_do_but_TEX */
public static final int EXTEND_LEFT=		1;
public static final int EXTEND_RIGHT=	2;

/* block bounds/position calculation */
//enum {
public static final int UI_BLOCK_BOUNDS=1;
public static final int UI_BLOCK_BOUNDS_TEXT=2;
public static final int UI_BLOCK_BOUNDS_POPUP_MOUSE=3;
public static final int UI_BLOCK_BOUNDS_POPUP_MENU=4;
public static final int UI_BLOCK_BOUNDS_POPUP_CENTER=5;
//} eBlockBoundsCalc;

//typedef struct {
//	short xim, yim;
//	unsigned int *rect;
//	short xofs, yofs;
//} uiIconImage;
//
//
//typedef struct uiLinkLine {				/* only for draw/edit */
//	struct uiLinkLine *next, *prev;
//
//	short flag, pad;
//
//	struct uiBut *from, *to;
//} uiLinkLine;
//
//typedef struct {
//	void **poin;		/* pointer to original pointer */
//	void ***ppoin;		/* pointer to original pointer-array */
//	short *totlink;		/* if pointer-array, here is the total */
//
//	short maxlink, pad;
//	short fromcode, tocode;
//
//	ListBase lines;
//} uiLink;

public static class uiBut extends Link<uiBut> {
	public short type, pointype, bit, bitnr, retval, strwidth, ofs, pos, selsta, selend;
	public short alignnr;
	public int flag;

	public byte[] str;
	public byte[] strdata=new byte[UI_MAX_NAME_STR];
	public byte[] drawstr=new byte[UI_MAX_DRAW_STR];

	public float x1, y1, x2, y2;

	public Pointer poin;
	public float hardmin, hardmax, softmin, softmax;
	public float a1, a2;
        public float[] hsv=new float[3];	// hsv is temp memory for hsv buttons
	public float aspect;

	public uiHandleFunc func;
	public Object func_arg1;
	public Object func_arg2;
	public Object func_arg3;

	public uiHandleFunc funcN;
	public Object func_argN;

	public bContextStore context;

//	void (*embossfunc)(int , int , float, float, float, float, float, int);
//	void (*sliderfunc)(int , float, float, float, float, float, float, int);

//	public uiButCompleteFunc autocomplete_func;
	public Object autocomplete_func;
//	void *autofunc_arg;

//	uiButSearchFunc search_func;
//	void *search_arg;

	public uiHandleFunc rename_func;
	public Object rename_arg1;
	public Object rename_orig;

//	uiLink *link;
//	short linkto[2];

	public byte[] tip, lockstr;

	public BIFIconID icon;
	public short but_align;	/* aligning buttons, horiz/vertical */
	public short lock, win;
	public short iconadd, dt;

	/* IDPOIN data */
//	uiIDPoinFuncFP idpoin_func;
//	ID **idpoin_idpp;

	/* BLOCK data */
	public uiBlockCreateFunc block_create_func;

	/* PULLDOWN/MENU data */
	public uiMenuCreateFunc menu_create_func;

	/* RNA data */
	public PointerRNA rnapoin;
	public PropertyRNA rnaprop;
	public int rnaindex;

	public PointerRNA rnasearchpoin = new PointerRNA();
	public PropertyRNA rnasearchprop;

	/* Operator data */
	public wmOperatorType optype;
	public int opcontext;
	public IDProperty opproperties;
	public PointerRNA opptr;

		/* active button data */
	public uiHandleButtonData active;

	public byte[] editstr;
	public Pointer<Double> editval;
	public float[] editvec;
//	void *editcoba;
//	void *editcumap;

		/* pointer back */
	public uiBlock block;
};

public static class uiBlock extends Link<uiBlock> {
	public ListBase<uiBut> buttons = new ListBase<uiBut>();
	public Panel panel;
	public uiBlock oldblock;

	public ListBase<uiLayoutRoot> layouts = new ListBase<uiLayoutRoot>();
	public uiLayout curlayout;

//	ListBase contexts;

	public byte[] name=new byte[UI_MAX_NAME_STR];

	public float[][] winmat=new float[4][4];

	public float minx, miny, maxx, maxy;
	public float aspect;

	public short alignnr;

	public uiHandleFunc func;
	public Object func_arg1;
	public Object func_arg2;

	public uiHandleFunc butm_func;
	public Object butm_func_arg;

	public uiHandleFunc handle_func;
	public Object handle_func_arg;

	/* custom extra handling */
//	int (*block_event_func)(const struct bContext *C, struct uiBlock *, struct wmEvent *);

	/* extra draw function for custom blocks */
//	void (*drawextra)(const struct bContext *C, void *idv, void *argv, rcti *rect);
//	void *drawextra_arg;

	public int afterval, flag;

	public short direction, dt;
	public short auto_open, in_use;
	public double auto_open_last;

	public int lock;
	public byte[] lockstr;

	public float xofs, yofs;				// offset to parent button
	public int dobounds, mx, my;	// for doing delayed
	public int bounds, minbounds;   // for doing delayed
	public int endblock;					// uiEndBlock done?

	public rctf safety = new rctf();				// pulldowns, to detect outside, can differ per case how it is created
	public ListBase saferct = new ListBase();			// uiSafetyRct list

	public uiPopupBlockHandle handle;	// handle
	public int tooltipdisabled;		// to avoid tooltip after click

	public int active;					// to keep blocks while drawing and free them afterwards

	public Object evil_C;				// XXX hack for dynamic operator enums
};

public static class uiSafetyRct extends Link<uiSafetyRct> {
	public rctf parent = new rctf();
	public rctf safety = new rctf();
};

public static class uiPopupBlockHandle {
        public static interface PopupFunc {
            public void run(bContext C, Object arg, int event);
        }

        public static interface CancelFunc {
            public void run(Object arg);
        }

	/* internal */
	public ARegion region;
	public int towardsx, towardsy;
	public double towardstime;
	public int dotowards;

	public int popup;
	public PopupFunc popup_func;
	public CancelFunc cancel_func;
	public Object popup_arg;

	/* for operator popups */
	public wmOperatorType optype;
	public int opcontext;
	public ScrArea ctx_area;
	public ARegion ctx_region;

	/* return values */
	public int butretval;
	public int menuretval;
	public float retvalue;
	public float[] retvec=new float[3];

        public void set(uiPopupBlockHandle handle) {
            region = handle.region;
            towardsx = handle.towardsx;
            towardsy = handle.towardsy;
            towardstime = handle.towardstime;
            dotowards =handle.dotowards;
            popup = handle.popup;
            popup_arg = handle.popup_arg;
            optype = handle.optype;
            opcontext = handle.opcontext;
            ctx_area = handle.ctx_area;
            ctx_region = handle.ctx_region;
            butretval = handle.butretval;
            menuretval = handle.menuretval;
            retvalue = handle.retvalue;
            System.arraycopy(handle.retvec, 0, retvec, 0, retvec.length);
        }
};

/* Callbacks
 *
 * uiBlockSetHandleFunc/ButmFunc are for handling events through a callback.
 * HandleFunc gets the retval passed on, and ButmFunc gets a2. The latter is
 * mostly for compatibility with older code.
 *
 * uiButSetCompleteFunc is for tab completion.
 *
 * uiButSearchFunc is for name buttons, showing a popup with matches
 *
 * uiBlockSetFunc and uiButSetFunc are callbacks run when a button is used,
 * in case events, operators or RNA are not sufficient to handle the button.
 *
 * uiButSetNFunc will free the argument with MEM_freeN. */

public static interface uiHandleFunc {
    public void run(bContext C, Object arg1, Object arg2);
}
//typedef void (*uiButHandleFunc)(struct bContext *C, void *arg1, void *arg2);
//typedef void (*uiButHandleRenameFunc)(struct bContext *C, void *arg, char *origstr);
//typedef void (*uiButHandleNFunc)(struct bContext *C, void *argN, void *arg2);
//typedef void (*uiButCompleteFunc)(struct bContext *C, char *str, void *arg);
//typedef void (*uiButSearchFunc)(const struct bContext *C, void *arg, char *str, uiSearchItems *items);
//typedef void (*uiBlockHandleFunc)(struct bContext *C, void *arg, int event);

public static interface uiBlockHandleCreateFunc {
    public uiBlock run(bContext C, uiPopupBlockHandle handle, Object arg1);
};

/* Menu Callbacks */

public static interface uiMenuCreateFunc {
    public void run(bContext C, uiLayout layout, Object arg1);
};

//public static interface uiMenuHandleFunc {
//    public void run(bContext C, Object arg, int event);
//};

///////////////////////////////////////////////////////////////////////////////////////

public static final int MENU_WIDTH= 			120;
public static final int MENU_ITEM_HEIGHT=	20;
public static final int MENU_SEP_HEIGHT=		6;

/*
 * a full doc with API notes can be found in bf-blender/blender/doc/interface_API.txt
 *
 * uiBlahBlah()		external function
 * ui_blah_blah()	internal function
 */

//static void ui_free_but(const bContext *C, uiBut *but);

/* ************* translation ************** */

//int ui_translate_buttons()
//{
//	return (U.transopts & USER_TR_BUTTONS);
//}
//
//int ui_translate_menus()
//{
//	return (U.transopts & USER_TR_MENUS);
//}
//
//int ui_translate_tooltips()
//{
//	return (U.transopts & USER_TR_TOOLTIPS);
//}

/* ************* window matrix ************** */

public static void ui_block_to_window_fl(ARegion ar, uiBlock block, float[] x, float[] y)
{
	float gx, gy;
	int sx, sy, getsizex, getsizey;

	getsizex= ar.winrct.xmax-ar.winrct.xmin+1;
	getsizey= ar.winrct.ymax-ar.winrct.ymin+1;
	sx= ar.winrct.xmin;
	sy= ar.winrct.ymin;

	gx= x[0];
	gy= y[0];

	if(block.panel!=null) {
		gx += block.panel.ofsx;
		gy += block.panel.ofsy;
	}

	x[0]= ((float)sx) + ((float)getsizex)*(0.5f+ 0.5f*(gx*block.winmat[0][0]+ gy*block.winmat[1][0]+ block.winmat[3][0]));
	y[0]= ((float)sy) + ((float)getsizey)*(0.5f+ 0.5f*(gx*block.winmat[0][1]+ gy*block.winmat[1][1]+ block.winmat[3][1]));
}

//void ui_block_to_window(const ARegion *ar, uiBlock *block, int *x, int *y)
//{
//	float fx, fy;
//
//	fx= *x;
//	fy= *y;
//
//	ui_block_to_window_fl(ar, block, &fx, &fy);
//
//	*x= (int)(fx+0.5f);
//	*y= (int)(fy+0.5f);
//}
//
//void ui_block_to_window_rct(const ARegion *ar, uiBlock *block, rctf *graph, rcti *winr)
//{
//	rctf tmpr;
//
//	tmpr= *graph;
//	ui_block_to_window_fl(ar, block, &tmpr.xmin, &tmpr.ymin);
//	ui_block_to_window_fl(ar, block, &tmpr.xmax, &tmpr.ymax);
//
//	winr.xmin= tmpr.xmin;
//	winr.ymin= tmpr.ymin;
//	winr.xmax= tmpr.xmax;
//	winr.ymax= tmpr.ymax;
//}

public static void ui_window_to_block_fl(ARegion ar, uiBlock block, float[] x, float[] y)	/* for mouse cursor */
{
	float a, b, c, d, e, f, px, py;
	int sx, sy, getsizex, getsizey;

	getsizex= ar.winrct.xmax-ar.winrct.xmin+1;
	getsizey= ar.winrct.ymax-ar.winrct.ymin+1;
	sx= ar.winrct.xmin;
	sy= ar.winrct.ymin;

	a= .5f*((float)getsizex)*block.winmat[0][0];
	b= .5f*((float)getsizex)*block.winmat[1][0];
	c= .5f*((float)getsizex)*(1.0f+block.winmat[3][0]);

	d= .5f*((float)getsizey)*block.winmat[0][1];
	e= .5f*((float)getsizey)*block.winmat[1][1];
	f= .5f*((float)getsizey)*(1.0f+block.winmat[3][1]);

	px= x[0] - sx;
	py= y[0] - sy;

	y[0]=  (a*(py-f) + d*(c-px))/(a*e-d*b);
	x[0]= (px- b*(y[0])- c)/a;

	if(block.panel!=null) {
		x[0] -= block.panel.ofsx;
		y[0] -= block.panel.ofsy;
	}
}

public static void ui_window_to_block(ARegion ar, uiBlock block, int[] x, int[] y)
{
	float[] fx={0}, fy={0};

	fx[0]= x[0];
	fy[0]= y[0];

	ui_window_to_block_fl(ar, block, fx, fy);

	x[0]= (int)(fx[0]+0.5f);
	y[0]= (int)(fy[0]+0.5f);
}

public static void ui_window_to_region(ARegion ar, int[] x, int[] y)
{
	x[0]-= ar.winrct.xmin;
	y[0]-= ar.winrct.ymin;
}

/* ******************* block calc ************************* */

public static void ui_block_translate(uiBlock block, int x, int y)
{
	uiBut bt;

	for(bt= block.buttons.first; bt!=null; bt=bt.next) {
		bt.x1 += x;
		bt.y1 += y;
		bt.x2 += x;
		bt.y2 += y;
	}

	block.minx += x;
	block.miny += y;
	block.maxx += x;
	block.maxy += y;
}

static void ui_text_bounds_block(uiBlock block, float offset)
{
	uiStyle style= (uiStyle)U.uistyles.first;	// XXX pass on as arg
	uiBut bt;
	int i = 0, j, x1addval= (int)offset, nextcol;
	int lastcol= 0, col= 0;

	UIStyle.uiStyleFontSet(style.widget);

	for(bt= block.buttons.first; bt!=null; bt= bt.next) {
		if(bt.type!=SEPR) {
			//int transopts= ui_translate_buttons();
			//if(bt.type==TEX || bt.type==IDPOIN) transopts= 0;

			j= (int)Blf.BLF_width(style.widget.uifont_id, bt.drawstr,0);

			if(j > i) i = j;
		}
		
		if(bt.next!=null && bt.x1 < bt.next.x1)
			lastcol++;
	}

	/* cope with multi collumns */
	bt= block.buttons.first;
	while(bt!=null) {
		if(bt.next!=null && bt.x1 < bt.next.x1) {
			nextcol= 1;
			col++;
		}
		else nextcol= 0;

		bt.x1 = x1addval;
		bt.x2 = bt.x1 + i + block.bounds;
		
		if(col == lastcol)
			bt.x2= UtilDefines.MAX2(bt.x2, offset + block.minbounds);

		ui_check_but(bt);	// clips text again

		if(nextcol!=0)
			x1addval+= i + block.bounds;

		bt= bt.next;
	}
}

public static void ui_bounds_block(uiBlock block)
{
	uiBut bt;
	int xof;

	if(block.buttons.first==null) {
		if(block.panel!=null) {
			block.minx= 0.0f; block.maxx= block.panel.sizex;
			block.miny= 0.0f; block.maxy= block.panel.sizey;
		}
	}
	else {

		block.minx= block.miny= 10000;
		block.maxx= block.maxy= -10000;

		bt= block.buttons.first;
		while(bt!=null) {
			if(bt.x1 < block.minx) block.minx= bt.x1;
			if(bt.y1 < block.miny) block.miny= bt.y1;

  			if(bt.x2 > block.maxx) block.maxx= bt.x2;
			if(bt.y2 > block.maxy) block.maxy= bt.y2;

			bt= bt.next;
		}

		block.minx -= block.bounds;
		block.miny -= block.bounds;
		block.maxx += block.bounds;
		block.maxy += block.bounds;
	}
	
	block.maxx= block.minx + UtilDefines.MAX2(block.maxx - block.minx, block.minbounds);

	/* hardcoded exception... but that one is annoying with larger safety */
	bt= block.buttons.first;
	if(bt!=null && StringUtil.strncmp(bt.str,0, StringUtil.toCString("ERROR"),0, 5)==0)
            xof= 10;
	else
            xof= 40;

	block.safety.xmin= block.minx-xof;
	block.safety.ymin= block.miny-xof;
	block.safety.xmax= block.maxx+xof;
	block.safety.ymax= block.maxy+xof;
}

public static void ui_centered_bounds_block(bContext C, uiBlock block)
{
//	wmWindow window= bContext.CTX_wm_window(C);
//	int[] xmax={0}, ymax={0};
//	int startx, starty;
//	int width, height;
//	
//	/* note: this is used for the splash where window bounds event has not been
//	 * updated by ghost, get the window bounds from ghost directly */
//
//	// wm_window_get_size(window, &xmax, &ymax);
//	wm_window_get_size_ghost(window, xmax, ymax);
//	
//	ui_bounds_block(block);
//	
//	width= (int)(block.maxx - block.minx);
//	height= (int)(block.maxy - block.miny);
//	
//	startx = (int)((xmax[0] * 0.5f) - (width * 0.5f));
//	starty = (int)((ymax[0] * 0.5f) - (height * 0.5f));
//	
//	ui_block_translate(block, (int)(startx - block.minx), (int)(starty - block.miny));
//	
//	/* now recompute bounds and safety */
//	ui_bounds_block(block);
	
}

//static void ui_popup_bounds_block(bContext C, uiBlock block, boolean menu)
static void ui_popup_bounds_block(bContext C, uiBlock block, int bounds_calc)
{
	wmWindow window= bContext.CTX_wm_window(C);
	int startx, starty, endx, endy, width, height, oldwidth, oldheight;
	int oldbounds; //, mx, my;
    int[] xmax = {0}, ymax = {0};

	oldbounds= block.bounds;

	/* compute mouse position with user defined offset */
	ui_bounds_block(block);
	
//	mx= (int)(window.eventstate.x + block.minx + block.mx);
//	my= (int)(window.eventstate.y + block.miny + block.my);

	WmWindow.wm_window_get_size(window, xmax, ymax);
	
	oldwidth= (int)(block.maxx - block.minx);
	oldheight= (int)(block.maxy - block.miny);

	/* first we ensure wide enough text bounds */
//	if(menu) {
	if(bounds_calc==UI_BLOCK_BOUNDS_POPUP_MENU) {
		if((block.flag & UI_BLOCK_LOOP)!=0) {
			block.bounds= 50;
			ui_text_bounds_block(block, block.minx);
		}
	}

	/* next we recompute bounds */
	block.bounds= oldbounds;
	ui_bounds_block(block);

	/* and we adjust the position to fit within window */
	width= (int)(block.maxx - block.minx);
	height= (int)(block.maxy - block.miny);
	
	/* avoid divide by zero below, caused by calling with no UI, but better not crash */
	oldwidth= oldwidth > 0 ? oldwidth : UtilDefines.MAX2(1, width);
	oldheight= oldheight > 0 ? oldheight : UtilDefines.MAX2(1, height);

	/* offset block based on mouse position, user offset is scaled
	   along in case we resized the block in ui_text_bounds_block */
	startx= (int)(((wmEvent)window.eventstate).x + block.minx + (block.mx*width)/oldwidth);
	starty= (int)(((wmEvent)window.eventstate).y + block.miny + (block.my*height)/oldheight);
//	startx= (int)(mx-(0.8f*(width)));
//	starty= my;

	if(startx<10)
		startx= 10;
	if(starty<10)
		starty= 10;

	endx= startx+width;
	endy= starty+height;

	if(endx>xmax[0]) {
		endx= xmax[0]-10;
		startx= endx-width;
	}
	if(endy>ymax[0]-20) {
		endy= ymax[0]-20;
		starty= endy-height;
	}

	ui_block_translate(block, (int)(startx - block.minx), (int)(starty - block.miny));

	/* now recompute bounds and safety */
	ui_bounds_block(block);
}

/* used for various cases */
public static void uiBoundsBlock(uiBlock block, int addval)
{
	if(block==null)
		return;

	block.bounds= addval;
//	block.dobounds= 1;
	block.dobounds= UI_BLOCK_BOUNDS;
}

/* used for pulldowns */
public static void uiTextBoundsBlock(uiBlock block, int addval)
{
	block.bounds= addval;
//	block.dobounds= 2;
	block.dobounds= UI_BLOCK_BOUNDS_TEXT;
}

/* used for block popups */
public static void uiPopupBoundsBlock(uiBlock block, int addval, int mx, int my)
{
	block.bounds= addval;
//	block.dobounds= 3;
	block.dobounds= UI_BLOCK_BOUNDS_POPUP_MOUSE;
	block.mx= mx;
	block.my= my;
}

/* used for menu popups */
public static void uiMenuPopupBoundsBlock(uiBlock block, int addval, int mx, int my)
{
	block.bounds= addval;
//	block.dobounds= 4;
	block.dobounds= UI_BLOCK_BOUNDS_POPUP_MENU;
	block.mx= mx;
	block.my= my;
}

/* used for centered popups, i.e. splash */
public static void uiCenteredBoundsBlock(uiBlock block, int addval)
{
	block.bounds= addval;
	block.dobounds= UI_BLOCK_BOUNDS_POPUP_CENTER;
}

///* ************** LINK LINE DRAWING  ************* */
//
///* link line drawing is not part of buttons or theme.. so we stick with it here */
//
//static void ui_draw_linkline(uiBut *but, uiLinkLine *line)
//{
//	rcti rect;
//
//	if(line.from==NULL || line.to==NULL) return;
//
//	rect.xmin= (line.from.x1+line.from.x2)/2.0;
//	rect.ymin= (line.from.y1+line.from.y2)/2.0;
//	rect.xmax= (line.to.x1+line.to.x2)/2.0;
//	rect.ymax= (line.to.y1+line.to.y2)/2.0;
//
//	if(line.flag & UI_SELECT)
//		glColor3ub(100,100,100);
//	else
//		glColor3ub(0,0,0);
//
//	ui_draw_link_bezier(&rect);
//}
//
//static void ui_draw_links(uiBlock *block)
//{
//	uiBut *but;
//	uiLinkLine *line;
//
//	but= block.buttons.first;
//	while(but) {
//		if(but.type==LINK && but.link) {
//			line= but.link.lines.first;
//			while(line) {
//				ui_draw_linkline(but, line);
//				line= line.next;
//			}
//		}
//		but= but.next;
//	}
//}

/* ************** BLOCK ENDING FUNCTION ************* */

/* NOTE: if but.poin is allocated memory for every defbut, things fail... */
static boolean ui_but_equals_old(uiBut but, uiBut oldbut)
{
	/* various properties are being compared here, hopfully sufficient
	 * to catch all cases, but it is simple to add more checks later */
	if(but.retval != oldbut.retval) return false;
//	if(but.rnapoin.data != oldbut.rnapoin.data) return false;
//	if(but.rnaprop != oldbut.rnaprop)
//		if(but.rnaindex != oldbut.rnaindex) return false;
	if(but.func != oldbut.func) return false;
	if(but.funcN != oldbut.funcN) return false;
	if(oldbut.func_arg1 != oldbut && but.func_arg1 != oldbut.func_arg1) return false;
	if(oldbut.func_arg2 != oldbut && but.func_arg2 != oldbut.func_arg2) return false;
//	if(but.funcN==null && ((but.poin != oldbut.poin && oldbut.poin != oldbut) || but.pointype != oldbut.pointype)) return false;

	return true;
}

static boolean ui_but_update_from_old_block(bContext C, uiBlock block, uiBut but)
{
	uiBlock oldblock;
	uiBut oldbut;
	boolean found= false;

	oldblock= block.oldblock;
	if(oldblock==null)
		return found;

	for(oldbut=oldblock.buttons.first; oldbut!=null; oldbut=oldbut.next) {
		if(ui_but_equals_old(oldbut, but)) {
			if(oldbut.active!=null) {
				/* exception! redalert flag can't be update from old button. 
				 * perhaps it should only copy spesific flags rather then all. */
				but.flag= (oldbut.flag & ~UI_BUT_REDALERT) | (but.flag & UI_BUT_REDALERT);
//				but.flag= oldbut.flag;
				but.active= oldbut.active;
				but.pos= oldbut.pos;
				but.editstr= oldbut.editstr;
				but.editval= oldbut.editval;
				but.editvec= oldbut.editvec;
//				but.editcoba= oldbut.editcoba;
//				but.editcumap= oldbut.editcumap;
				but.selsta= oldbut.selsta;
				but.selend= oldbut.selend;
				but.softmin= oldbut.softmin;
				but.softmax= oldbut.softmax;
//				but.linkto[0]= oldbut.linkto[0];
//				but.linkto[1]= oldbut.linkto[1];
				found= true;

				oldbut.active= null;
			}

			/* ensures one button can get activated, and in case the buttons
			 * draw are the same this gives O(1) lookup for each button */
			ListBaseUtil.BLI_remlink(oldblock.buttons, oldbut);
			ui_free_but(C, oldbut);

			break;
		}
	}

	return found;
}

///* needed for temporarily rename buttons, such as in outliner or fileselect,
//   they should keep calling uiDefButs to keep them alive */
///* returns 0 when button removed */
//int uiButActiveOnly(const bContext *C, uiBlock *block, uiBut *but)
//{
//	uiBlock *oldblock;
//	uiBut *oldbut;
//	int activate= 0, found= 0, isactive= 0;
//
//	oldblock= block.oldblock;
//	if(!oldblock)
//		activate= 1;
//	else {
//		for(oldbut=oldblock.buttons.first; oldbut; oldbut=oldbut.next) {
//			if(ui_but_equals_old(oldbut, but)) {
//				found= 1;
//
//				if(oldbut.active)
//					isactive= 1;
//
//				break;
//			}
//		}
//	}
//	if(activate || found==0) {
//		ui_button_activate_do( (bContext *)C, CTX_wm_region(C), but);
//	}
//	else if(found && isactive==0) {
//
//		BLI_remlink(&block.buttons, but);
//		ui_free_but(C, but);
//		return 0;
//	}
//
//	return 1;
//}

///* assigns automatic keybindings to menu items for fast access
// * (underline key in menu) */
//static void ui_menu_block_set_keyaccels(uiBlock *block)
//{
//	uiBut *but;
//
//	unsigned int meny_key_mask= 0;
//	unsigned char menu_key;
//	const char *str_pt;
//	int pass;
//	int tot_missing= 0;
//
//	/* only do it before bounding */
//	if(block->minx != block->maxx)
//		return;
//
//	for(pass=0; pass<2; pass++) {
//		/* 2 Passes, on for first letter only, second for any letter if first fails
//		 * fun first pass on all buttons so first word chars always get first priority */
//
//		for(but=block->buttons.first; but; but=but->next) {
//			if(!ELEM4(but->type, BUT, MENU, BLOCK, PULLDOWN) || (but->flag & UI_HIDDEN)) {
//				/* pass */
//			}
//			else if(but->menu_key=='\0') {
//				if(but->str) {
//					for(str_pt= but->str; *str_pt; ) {
//						menu_key= tolower(*str_pt);
//						if((menu_key >= 'a' && menu_key <= 'z') && !(meny_key_mask & 1<<(menu_key-'a'))) {
//							meny_key_mask |= 1<<(menu_key-'a');
//							break;
//						}
//
//						if(pass==0) {
//							/* Skip to next delimeter on first pass (be picky) */
//							while(isalpha(*str_pt))
//								str_pt++;
//
//							if(*str_pt)
//								str_pt++;
//						}
//						else {
//							/* just step over every char second pass and find first usable key */
//							str_pt++;
//						}
//					}
//
//					if(*str_pt) {
//						but->menu_key= menu_key;
//					}
//					else {
//						/* run second pass */
//						tot_missing++;
//					}
//
//					/* if all keys have been used just exit, unlikely */
//					if(meny_key_mask == (1<<26)-1) {
//						return;
//					}
//				}
//			}
//		}
//
//		/* check if second pass is needed */
//		if(!tot_missing) {
//			break;
//		}
//	}
//}

public static void ui_menu_block_set_keymaps(bContext C, uiBlock block)
{
	uiBut but;
	IDProperty prop;
	byte[] buf = new byte[512], butstr;

	/* only do it before bounding */
	if(block.minx != block.maxx)
		return;

	for(but=block.buttons.first; but!=null; but=but.next) {
		if(but.optype!=null) {
			prop= (but.opptr!=null)? (IDProperty)but.opptr.data: null;

			if(WmKeymap.WM_key_event_operator_string(C, but.optype.idname, but.opcontext, prop, buf, buf.length)!=null) {
//				butstr= MEM_mallocN(strlen(but.str)+strlen(buf)+2, "menu_block_set_keymaps");
                butstr= new byte[StringUtil.strlen(but.str,0)+StringUtil.strlen(buf,0)+2];
				StringUtil.strcpy(butstr,0, but.str,0);
				StringUtil.strcat(butstr, StringUtil.toCString("|"),0);
				StringUtil.strcat(butstr, buf,0);

				but.str= but.strdata;
				StringUtil.BLI_strncpy(but.str,0, butstr,0, but.strdata.length);
//				MEM_freeN(butstr);

				ui_check_but(but);
			}
		}
	}
}

public static void uiEndBlock(bContext C, uiBlock block)
{
	uiBut but;
	Scene scene= bContext.CTX_data_scene(C);

	/* inherit flags from 'old' buttons that was drawn here previous, based
	 * on matching buttons, we need this to make button event handling non
	 * blocking, while still alowing buttons to be remade each redraw as it
	 * is expected by blender code */
	for(but=block.buttons.first; but!=null; but=but.next) {
		if(ui_but_update_from_old_block(C, block, but))
			ui_check_but(but);

		/* temp? Proper check for greying out */
		if(but.optype!=null) {
			wmOperatorType ot= but.optype;

//			if(but.context!=null)
//				CTX_store_set((bContext)C, but.context);

//			if(ot==null || (ot.poll!=null && ot.poll.run(C, null, null)==0)) {
			if(ot==null || WmEventSystem.WM_operator_poll_context((bContext)C, ot, but.opcontext)==false) {
				but.flag |= UI_BUT_DISABLED;
				but.lock = 1;
			}

//			if(but.context)
//				CTX_store_set((bContext*)C, NULL);
		}

//		ui_but_anim_flag(but, (scene)? scene.r.cfra: 0.0f);
	}

	if(block.oldblock!=null) {
		block.auto_open= block.oldblock.auto_open;
		block.auto_open_last= block.oldblock.auto_open_last;
		block.tooltipdisabled= block.oldblock.tooltipdisabled;

		block.oldblock= null;
	}

	/* handle pending stuff */
	if(block.layouts.first!=null)
            UILayout.uiBlockLayoutResolve(block, null, null);
	ui_block_do_align(block);
//	if((block->flag & UI_BLOCK_LOOP) && (block->flag & UI_BLOCK_NUMSELECT)) ui_menu_block_set_keyaccels(block); /* could use a different flag to check */
	if((block.flag & UI.UI_BLOCK_LOOP)!=0) ui_menu_block_set_keymaps(C, block);

	/* after keymaps! */
	if(block.dobounds == UI_BLOCK_BOUNDS) ui_bounds_block(block);
	else if(block.dobounds == UI_BLOCK_BOUNDS_TEXT) ui_text_bounds_block(block, 0.0f);
	else if(block.dobounds == UI_BLOCK_BOUNDS_POPUP_CENTER) ui_centered_bounds_block(C, block);
	else if(block.dobounds!=0) ui_popup_bounds_block(C, block, block.dobounds);

	if(block.minx==0.0f && block.maxx==0.0f) uiBoundsBlock(block, 0);
	if((block.flag & UI.UI_BUT_ALIGN)!=0) uiBlockEndAlign(block);

	block.endblock= 1;
}

///* ************** BLOCK DRAWING FUNCTION ************* */
//
//void ui_fontscale(short *points, float aspect)
//{
//	if(aspect < 0.9f || aspect > 1.1f) {
//		float pointsf= *points;
//
//		/* for some reason scaling fonts goes too fast compared to widget size */
//		aspect= sqrt(aspect);
//		pointsf /= aspect;
//
//		if(aspect > 1.0)
//			*points= ceil(pointsf);
//		else
//			*points= floor(pointsf);
//	}
//}

/* project button or block (but==NULL) to pixels in regionspace */
static void ui_but_to_pixelrect(rcti rect, ARegion ar, uiBlock block, uiBut but)
{
	float gx, gy;
	float getsizex, getsizey;

	getsizex= ar.winx;
	getsizey= ar.winy;

	gx= (but!=null?but.x1:block.minx) + (block.panel!=null?block.panel.ofsx:0.0f);
	gy= (but!=null?but.y1:block.miny) + (block.panel!=null?block.panel.ofsy:0.0f);

	rect.xmin= (int)StrictMath.floor(getsizex*(0.5+ 0.5*(gx*block.winmat[0][0]+ gy*block.winmat[1][0]+ block.winmat[3][0])));
	rect.ymin= (int)StrictMath.floor(getsizey*(0.5+ 0.5*(gx*block.winmat[0][1]+ gy*block.winmat[1][1]+ block.winmat[3][1])));

	gx= (but!=null?but.x2:block.maxx) + (block.panel!=null?block.panel.ofsx:0.0f);
	gy= (but!=null?but.y2:block.maxy) + (block.panel!=null?block.panel.ofsy:0.0f);

	rect.xmax= (int)StrictMath.floor(getsizex*(0.5+ 0.5*(gx*block.winmat[0][0]+ gy*block.winmat[1][0]+ block.winmat[3][0])));
	rect.ymax= (int)StrictMath.floor(getsizey*(0.5+ 0.5*(gx*block.winmat[0][1]+ gy*block.winmat[1][1]+ block.winmat[3][1])));
}

/* uses local copy of style, to scale things down, and allow widgets to change stuff */
public static void uiDrawBlock(GL2 gl, bContext C, uiBlock block)
{
	uiStyle style= (uiStyle)U.uistyles.first;	// XXX pass on as arg
	ARegion ar;
	uiBut but;
	rcti rect = new rcti();

	/* get menu region or area region */
	ar= bContext.CTX_wm_menu(C);
	if(ar==null)
		ar= bContext.CTX_wm_region(C);

	if(block.endblock==0)
		uiEndBlock(C, block);

	/* we set this only once */
	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

//	/* scale fonts */
//	ui_fontscale(&style.paneltitle.points, block.aspect);
//	ui_fontscale(&style.grouplabel.points, block.aspect);
//	ui_fontscale(&style.widgetlabel.points, block.aspect);
//	ui_fontscale(&style.widget.points, block.aspect);

	/* scale block min/max to rect */
	ui_but_to_pixelrect(rect, ar, block, null);

//        // TMP
//        if (rect.xmax == 0)
//            rect.xmax = 100;
//        if (rect.ymax == 0)
//            rect.ymax = 100;

	/* pixel space for AA widgets */
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glPushMatrix();
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glPushMatrix();
	gl.glLoadIdentity();
//	WmSubWindow.wmPushMatrix();
//	WmSubWindow.wmLoadIdentity(gl);

	WmSubWindow.wmOrtho2(gl, -0.01f, ar.winx-0.01f, -0.01f, ar.winy-0.01f);

	/* back */
	if((block.flag & UI_BLOCK_LOOP)!=0)
		UIWidgets.ui_draw_menu_back(gl, style, block, rect);
	else if(block.panel!=null)
		UIPanel.ui_draw_aligned_panel(gl, style, block, rect);

	/* widgets */
	for(but= block.buttons.first; but!=null; but= but.next) {
		ui_but_to_pixelrect(rect, ar, block, but);
		
		if((but.flag & UI_HIDDEN)==0 && 
			/* XXX: figure out why invalid coordinates happen when closing render window */
			/* and material preview is redrawn in main window (temp fix for bug #23848) */
			rect.xmin < rect.xmax && rect.ymin < rect.ymax)
			UIWidgets.ui_draw_but(gl, C, ar, style, but, rect);
	}

	/* restore matrix */
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glPopMatrix();
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glPopMatrix();
//	WmSubWindow.wmPopMatrix(gl);

//	ui_draw_links(block);
}

/* ************* EVENTS ************* */

static void ui_is_but_sel(uiBut but)
{
	double value;
	int lvalue;
	short push=0, _true=1;

	value= ui_get_but_val(but);

	if(but.type==TOGN || but.type==ICONTOGN || but.type==OPTIONN)
            _true= 0;

	if( but.bit!=0 ) {
		lvalue= (int)value;
		if( UtilDefines.BTST(lvalue, (but.bitnr)) )
                    push= _true;
		else push= (short)(_true!=0?0:1);
	}
	else {
		switch(but.type) {
		case BUT:
			push= 2;
			break;
		case HOTKEYEVT:
		case KEYEVT:
			push= 2;
			break;
		case TOGBUT:
		case TOG:
		case TOGR:
		case TOG3:
		case BUT_TOGDUAL:
		case ICONTOG:
		case OPTION:
			if(value!=but.hardmin) push= 1;
			break;
		case ICONTOGN:
		case TOGN:
		case OPTIONN:
			if(value==0.0) push= 1;
			break;
		case ROW:
		case LISTROW:
//			if(value == but.hardmax) push= 1;
			/* support for rna enum buts */
			if(but.rnaprop!=null && (RnaAccess.RNA_property_flag(but.rnaprop) & RNATypes.PROP_ENUM_FLAG)!=0) {
				if(((int)value & (int)but.hardmax)!=0) push= 1;
			}
			else {
				if(value == but.hardmax) push= 1;
			}
			break;
		case COL:
//			push= 1;
			push= 2;
			break;
		default:
			push= 2;
			break;
		}
	}

	if(push==2);
	else if(push==1) but.flag |= UI_SELECT;
	else but.flag &= ~UI_SELECT;
}

///* XXX 2.50 no links supported yet */
//
//static int uibut_contains_pt(uiBut *but, short *mval)
//{
//	return 0;
//
//}
//
//uiBut *ui_get_valid_link_button(uiBlock *block, uiBut *but, short *mval)
//{
//	uiBut *bt;
//
//		/* find button to link to */
//	for (bt= block.buttons.first; bt; bt= bt.next)
//		if(bt!=but && uibut_contains_pt(bt, mval))
//			break;
//
//	if (bt) {
//		if (but.type==LINK && bt.type==INLINK) {
//			if( but.link.tocode == (int)bt.hardmin ) {
//				return bt;
//			}
//		}
//		else if(but.type==INLINK && bt.type==LINK) {
//			if( bt.link.tocode == (int)but.hardmin ) {
//				return bt;
//			}
//		}
//	}
//
//	return NULL;
//}
//
//
//static uiBut *ui_find_inlink(uiBlock *block, void *poin)
//{
//	uiBut *but;
//
//	but= block.buttons.first;
//	while(but) {
//		if(but.type==INLINK) {
//			if(but.poin == poin) return but;
//		}
//		but= but.next;
//	}
//	return NULL;
//}
//
//static void ui_add_link_line(ListBase *listb, uiBut *but, uiBut *bt)
//{
//	uiLinkLine *line;
//
//	line= MEM_callocN(sizeof(uiLinkLine), "linkline");
//	BLI_addtail(listb, line);
//	line.from= but;
//	line.to= bt;
//}
//
//uiBut *uiFindInlink(uiBlock *block, void *poin)
//{
//	return ui_find_inlink(block, poin);
//}
//
//void uiComposeLinks(uiBlock *block)
//{
//	uiBut *but, *bt;
//	uiLink *link;
//	void ***ppoin;
//	int a;
//
//	but= block.buttons.first;
//	while(but) {
//		if(but.type==LINK) {
//			link= but.link;
//
//			/* for all pointers in the array */
//			if(link) {
//				if(link.ppoin) {
//					ppoin= link.ppoin;
//					for(a=0; a < *(link.totlink); a++) {
//						bt= ui_find_inlink(block, (*ppoin)[a] );
//						if(bt) {
//							ui_add_link_line(&link.lines, but, bt);
//						}
//					}
//				}
//				else if(link.poin) {
//					bt= ui_find_inlink(block, *(link.poin) );
//					if(bt) {
//						ui_add_link_line(&link.lines, but, bt);
//					}
//				}
//			}
//		}
//		but= but.next;
//	}
//}
//
//
///* ************************************************ */
//
//void uiBlockSetButLock(uiBlock *block, int val, char *lockstr)
//{
//	if(val) {
//		block.lock |= val;
//		block.lockstr= lockstr;
//	}
//}
//
//void uiBlockClearButLock(uiBlock *block)
//{
//	block.lock= 0;
//	block.lockstr= NULL;
//}
//
///* *************************************************************** */
//
//
///* XXX 2.50 no links supported yet */
//#if 0
//static void ui_delete_active_linkline(uiBlock *block)
//{
//	uiBut *but;
//	uiLink *link;
//	uiLinkLine *line, *nline;
//	int a, b;
//
//	but= block.buttons.first;
//	while(but) {
//		if(but.type==LINK && but.link) {
//			line= but.link.lines.first;
//			while(line) {
//
//				nline= line.next;
//
//				if(line.flag & UI_SELECT) {
//					BLI_remlink(&but.link.lines, line);
//
//					link= line.from.link;
//
//					/* are there more pointers allowed? */
//					if(link.ppoin) {
//
//						if(*(link.totlink)==1) {
//							*(link.totlink)= 0;
//							MEM_freeN(*(link.ppoin));
//							*(link.ppoin)= NULL;
//						}
//						else {
//							b= 0;
//							for(a=0; a< (*(link.totlink)); a++) {
//
//								if( (*(link.ppoin))[a] != line.to.poin ) {
//									(*(link.ppoin))[b]= (*(link.ppoin))[a];
//									b++;
//								}
//							}
//							(*(link.totlink))--;
//						}
//					}
//					else {
//						*(link.poin)= NULL;
//					}
//
//					MEM_freeN(line);
//				}
//				line= nline;
//			}
//		}
//		but= but.next;
//	}
//
//	/* temporal! these buttons can be everywhere... */
//	allqueue(REDRAWBUTSLOGIC, 0);
//}
//
//static void ui_do_active_linklines(uiBlock *block, short *mval)
//{
//	uiBut *but;
//	uiLinkLine *line, *act= NULL;
//	float mindist= 12.0, fac, v1[2], v2[2], v3[3];
//	int foundone= 0;
//
//	if(mval) {
//		v1[0]= mval[0];
//		v1[1]= mval[1];
//
//		/* find a line close to the mouse */
//		but= block.buttons.first;
//		while(but) {
//			if(but.type==LINK && but.link) {
//				foundone= 1;
//				line= but.link.lines.first;
//				while(line) {
//					v2[0]= line.from.x2;
//					v2[1]= (line.from.y1+line.from.y2)/2.0;
//					v3[0]= line.to.x1;
//					v3[1]= (line.to.y1+line.to.y2)/2.0;
//
//					fac= PdistVL2Dfl(v1, v2, v3);
//					if(fac < mindist) {
//						mindist= fac;
//						act= line;
//					}
//					line= line.next;
//				}
//			}
//			but= but.next;
//		}
//	}
//
//	/* check for a 'found one' to prevent going to 'frontbuffer' mode.
//		this slows done gfx quite some, and at OSX the 'finish' forces a swapbuffer */
//	if(foundone) {
//		glDrawBuffer(GL_FRONT);
//
//		/* draw */
//		but= block.buttons.first;
//		while(but) {
//			if(but.type==LINK && but.link) {
//				line= but.link.lines.first;
//				while(line) {
//					if(line==act) {
//						if((line.flag & UI_SELECT)==0) {
//							line.flag |= UI_SELECT;
//							ui_draw_linkline(but, line);
//						}
//					}
//					else if(line.flag & UI_SELECT) {
//						line.flag &= ~UI_SELECT;
//						ui_draw_linkline(but, line);
//					}
//					line= line.next;
//				}
//			}
//			but= but.next;
//		}
//		bglFlush();
//		glDrawBuffer(GL_BACK);
//	}
//}
//#endif
//
///* ******************************************************* */
//
///* XXX 2.50 no screendump supported yet */
//
//#if 0
///* nasty but safe way to store screendump rect */
//static int scr_x=0, scr_y=0, scr_sizex=0, scr_sizey=0;
//
//static void ui_set_screendump_bbox(uiBlock *block)
//{
//	if(block) {
//		scr_x= block.minx;
//		scr_y= block.miny;
//		scr_sizex= block.maxx - block.minx;
//		scr_sizey= block.maxy - block.miny;
//	}
//	else {
//		scr_sizex= scr_sizey= 0;
//	}
//}
//
///* used for making screenshots for menus, called in screendump.c */
//int uiIsMenu(int *x, int *y, int *sizex, int *sizey)
//{
//	if(scr_sizex!=0 && scr_sizey!=0) {
//		*x= scr_x;
//		*y= scr_y;
//		*sizex= scr_sizex;
//		*sizey= scr_sizey;
//		return 1;
//	}
//
//	return 0;
//}
//#endif

/* *********************** data get/set ***********************
 * this either works with the pointed to data, or can work with
 * an edit override pointer while dragging for example */

/* for buttons pointing to color for example */
public static void ui_get_but_vectorf(uiBut but, float[] vec)
{
//	PropertyRNA *prop;
	int a, tot;

	if(but.editvec!=null) {
		UtilDefines.VECCOPY(vec, but.editvec);
		return;
	}

	if(but.rnaprop!=null) {
//		prop= but.rnaprop;
//
//		vec[0]= vec[1]= vec[2]= 0.0f;
//
//		if(RNA_property_type(prop) == PROP_FLOAT) {
//			tot= RNA_property_array_length(prop);
//			tot= MIN2(tot, 3);
//
//			for(a=0; a<tot; a++)
//				vec[a]= RNA_property_float_get_index(&but.rnapoin, prop, a);
//		}
	}
	else if(but.pointype == CHA) {
		byte[] cp= (byte[])but.poin.get();
		vec[0]= ((float)(cp[0]&0xFF))/255.0f;
		vec[1]= ((float)(cp[1]&0xFF))/255.0f;
		vec[2]= ((float)(cp[2]&0xFF))/255.0f;
	}
	else if(but.pointype == FLO) {
		float[] fp= (float[])but.poin.get();
		UtilDefines.VECCOPY(vec, fp);
	}
	else {
		if (but.editvec==null) {
			System.err.printf("ui_get_but_vectorf: can't get color, should never happen\n");
			vec[0]= vec[1]= vec[2]= 0.0f;
		}
	}
}

///* for buttons pointing to color for example */
//void ui_set_but_vectorf(uiBut *but, float *vec)
//{
//	PropertyRNA *prop;
//	int a, tot;
//
//	if(but.editvec) {
//		VECCOPY(but.editvec, vec);
//		return;
//	}
//
//	if(but.rnaprop) {
//		prop= but.rnaprop;
//
//		if(RNA_property_type(prop) == PROP_FLOAT) {
//			tot= RNA_property_array_length(prop);
//			tot= MIN2(tot, 3);
//
//			for(a=0; a<tot; a++)
//				RNA_property_float_set_index(&but.rnapoin, prop, a, vec[a]);
//		}
//	}
//	else if(but.pointype == CHA) {
//		char *cp= (char *)but.poin;
//		cp[0]= (char)(0.5 +vec[0]*255.0);
//		cp[1]= (char)(0.5 +vec[1]*255.0);
//		cp[2]= (char)(0.5 +vec[2]*255.0);
//	}
//	else if(but.pointype == FLO) {
//		float *fp= (float *)but.poin;
//		VECCOPY(fp, vec);
//	}
//}

public static boolean ui_is_but_float(uiBut but)
{
	if(but.pointype==FLO && but.poin!=null)
		return true;

	if(but.rnaprop!=null && RnaAccess.RNA_property_type(but.rnaprop) == RNATypes.PROP_FLOAT)
		return true;

	return false;
}

//int ui_is_but_unit(uiBut *but)
//{
//	Scene *scene= CTX_data_scene((bContext *)but->block->evil_C);
//	int unit_type= uiButGetUnitType(but);
//
//	if(unit_type == PROP_UNIT_NONE)
//		return 0;
//
//#if 1 // removed so angle buttons get correct snapping
//	if (scene->unit.system_rotation == USER_UNIT_ROT_RADIANS && unit_type == PROP_UNIT_ROTATION)
//		return 0;
//#endif
//	
//	/* for now disable time unit conversion */	
//	if (unit_type == PROP_UNIT_TIME)
//		return 0;
//
//	if (scene->unit.system == USER_UNIT_NONE) {
//		if (unit_type != PROP_UNIT_ROTATION) {
//			return 0;
//		}
//	}
//
//	return 1;
//}

public static double ui_get_but_val(uiBut but)
{
	PropertyRNA prop;
	double value = 0.0;

	if(but.editval!=null) { return but.editval.get(); }
//	if(but.poin==null && but.rnapoin.data==null) return 0.0;
//    if(but.poin==null) return 0.0;

	if(but.rnaprop!=null) {
		prop= but.rnaprop;
//		System.out.println("ui_get_but_val: "+prop.identifier);

		switch(RnaAccess.RNA_property_type(prop)) {
			case RNATypes.PROP_BOOLEAN:
				if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
					value= RnaAccess.RNA_property_boolean_get_index(but.rnapoin, prop, but.rnaindex)?1:0;
				else
					value= RnaAccess.RNA_property_boolean_get(but.rnapoin, prop);
				break;
			case RNATypes.PROP_INT:
				if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
					value= RnaAccess.RNA_property_int_get_index(but.rnapoin, prop, but.rnaindex);
				else
					value= RnaAccess.RNA_property_int_get(but.rnapoin, prop);
				break;
			case RNATypes.PROP_FLOAT:
				if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
					value= RnaAccess.RNA_property_float_get_index(but.rnapoin, prop, but.rnaindex);
				else
					value= RnaAccess.RNA_property_float_get(but.rnapoin, prop);
				break;
			case RNATypes.PROP_ENUM:
				value= RnaAccess.RNA_property_enum_get(but.rnapoin, prop);
				break;
			default:
				value= 0.0;
				break;
		}
	}
	else if(but.type== HSVSLI) {
//		float h, s, v, *fp;
//
//		fp= (but.editvec)? but.editvec: (float *)but.poin;
//		rgb_to_hsv(fp[0], fp[1], fp[2], &h, &s, &v);
//
//		switch(but.str[0]) {
//			case 'H': value= h; break;
//			case 'S': value= s; break;
//			case 'V': value= v; break;
//		}
	}
	else if( but.pointype == CHA ) {
		value= (Byte)but.poin.get();
	}
	else if( but.pointype == SHO ) {
		value= (Short)but.poin.get();
	}
	else if( but.pointype == INT ) {
		value= but.poin.get().hashCode();
	}
	else if( but.pointype == FLO ) {
		value= (Float)but.poin.get();
	}

	return value;
}

public static void ui_set_but_val(uiBut but, double value)
{
	PropertyRNA prop;
//	System.out.println("ui_set_but_val");
	
	/* value is a hsv value: convert to rgb */
	if(but.rnaprop!=null) {
		prop= but.rnaprop;
//		System.out.println("ui_set_but_val rna: "+prop.identifier);

		if(RnaAccess.RNA_property_editable(but.rnapoin, prop)) {
//			System.out.println("ui_set_but_val editable");
			switch(RnaAccess.RNA_property_type(prop)) {
				case RNATypes.PROP_BOOLEAN:
					if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
						RnaAccess.RNA_property_boolean_set_index(but.rnapoin, prop, but.rnaindex, (int)value);
					else
						RnaAccess.RNA_property_boolean_set(but.rnapoin, prop, (int)value);
					break;
				case RNATypes.PROP_INT:
					if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
						RnaAccess.RNA_property_int_set_index(but.rnapoin, prop, but.rnaindex, (int)value);
					else
						RnaAccess.RNA_property_int_set(but.rnapoin, prop, (int)value);
					break;
				case RNATypes.PROP_FLOAT:
					if(RnaAccess.RNA_property_array_length(but.rnapoin, prop)!=0)
						RnaAccess.RNA_property_float_set_index(but.rnapoin, prop, but.rnaindex, (float)value);
					else
						RnaAccess.RNA_property_float_set(but.rnapoin, prop, (float)value);
					break;
				case RNATypes.PROP_ENUM:
					if((RnaAccess.RNA_property_flag(prop) & RNATypes.PROP_ENUM_FLAG)!=0) {
						int ivalue= (int)value;
						ivalue ^= RnaAccess.RNA_property_enum_get(but.rnapoin, prop); /* toggle for enum/flag buttons */
						RnaAccess.RNA_property_enum_set(but.rnapoin, prop, ivalue);
					}
					else {
						RnaAccess.RNA_property_enum_set(but.rnapoin, prop, (int)value);
					}
					break;
				default:
					break;
			}
		}
	}
	else if(but.pointype==0);
	else if(but.type==HSVSLI ) {
//		float h, s, v, *fp;
//
//		fp= (but.editvec)? but.editvec: (float *)but.poin;
//		rgb_to_hsv(fp[0], fp[1], fp[2], &h, &s, &v);
//
//		switch(but.str[0]) {
//		case 'H': h= value; break;
//		case 'S': s= value; break;
//		case 'V': v= value; break;
//		}
//
//		hsv_to_rgb(h, s, v, fp, fp+1, fp+2);

	}
	else {
		System.out.println("ui_set_but_val else");
		/* first do rounding */
		if(but.pointype==CHA)
			value= (char)StrictMath.floor(value+0.5);
		else if(but.pointype==SHO ) {
			/* gcc 3.2.1 seems to have problems
			 * casting a double like 32772.0 to
			 * a short so we cast to an int, then
			 to a short */
			int gcckludge;
			gcckludge = (int)StrictMath.floor(value+0.5);
			value= (short)gcckludge;
		}
		else if(but.pointype==INT )
			value= (int)StrictMath.floor(value+0.5);
		else if(but.pointype==FLO ) {
			float fval= (float)value;
			if(fval>= -0.00001f && fval<= 0.00001f) fval= 0.0f;	/* prevent negative zero */
			value= fval;
		}

		/* then set value with possible edit override */
		if(but.editval!=null)
            but.editval.set(value);
		else if(but.pointype==CHA)
            but.poin.set((byte)value);
		else if(but.pointype==SHO)
			but.poin.set((short)value);
		else if(but.pointype==INT)
			but.poin.set((int)value);
		else if(but.pointype==FLO)
			but.poin.set((float)value);
	}

	/* update select flag */
	ui_is_but_sel(but);
}

//int ui_get_but_string_max_length(uiBut *but)
//{
//	if(but.type == TEX)
//		return but.hardmax;
//	else if(but.type == IDPOIN)
//		return sizeof(((ID*)NULL).name)-2;
//	else
//		return UI_MAX_DRAW_STR;
//}
//
//void ui_get_but_string(uiBut *but, char *str, int maxlen)
//{
//	if(but.rnaprop && ELEM3(but.type, TEX, IDPOIN, SEARCH_MENU)) {
//		PropertyType type;
//		char *buf= NULL;
//
//		type= RNA_property_type(but.rnaprop);
//
//		if(type == PROP_STRING) {
//			/* RNA string */
//			buf= RNA_property_string_get_alloc(&but.rnapoin, but.rnaprop, str, maxlen);
//		}
//		else if(type == PROP_POINTER) {
//			/* RNA pointer */
//			PointerRNA ptr= RNA_property_pointer_get(&but.rnapoin, but.rnaprop);
//			buf= RNA_struct_name_get_alloc(&ptr, str, maxlen);
//		}
//
//		if(!buf) {
//			BLI_strncpy(str, "", maxlen);
//		}
//		else if(buf && buf != str) {
//			/* string was too long, we have to truncate */
//			BLI_strncpy(str, buf, maxlen);
//			MEM_freeN(buf);
//		}
//	}
//	else if(but.type == IDPOIN) {
//		/* ID pointer */
//		ID *id= *(but.idpoin_idpp);
//
//		if(id) BLI_strncpy(str, id.name+2, maxlen);
//		else BLI_strncpy(str, "", maxlen);
//
//		return;
//	}
//	else if(but.type == TEX) {
//		/* string */
//		BLI_strncpy(str, but.poin, maxlen);
//		return;
//	}
//	else if(but.type == SEARCH_MENU) {
//		/* string */
//		BLI_strncpy(str, but.poin, maxlen);
//		return;
//	}
//	else if(ui_but_anim_expression_get(but, str, maxlen))
//		; /* driver expression */
//	else {
//		/* number editing */
//		double value;
//
//		value= ui_get_but_val(but);
//
//		if(ui_is_but_float(but)) {
//			if(but.a2) { /* amount of digits defined */
//				if(but.a2==1) BLI_snprintf(str, maxlen, "%.1f", value);
//				else if(but.a2==2) BLI_snprintf(str, maxlen, "%.2f", value);
//				else if(but.a2==3) BLI_snprintf(str, maxlen, "%.3f", value);
//				else BLI_snprintf(str, maxlen, "%.4f", value);
//			}
//			else
//				BLI_snprintf(str, maxlen, "%.3f", value);
//		}
//		else
//			BLI_snprintf(str, maxlen, "%d", (int)value);
//	}
//}

public static int ui_set_but_string(bContext C, uiBut but, byte[] str, int offset)
{
	if(but.rnaprop!=null && (but.type == TEX || but.type == IDPOIN || but.type == SEARCH_MENU)) {
//		if(RNA_property_editable(&but.rnapoin, but.rnaprop)) {
//			PropertyType type;
//
//			type= RNA_property_type(but.rnaprop);
//
//			if(type == PROP_STRING) {
//				/* RNA string */
//				RNA_property_string_set(&but.rnapoin, but.rnaprop, str);
//				return 1;
//			}
//			else if(type == PROP_POINTER) {
//				/* RNA pointer */
//				PointerRNA ptr, rptr;
//				PropertyRNA *prop;
//
//				if(str == NULL || str[0] == '\0') {
//					RNA_property_pointer_set(&but.rnapoin, but.rnaprop, PointerRNA_NULL);
//					return 1;
//				}
//				else {
//					ptr= but.rnasearchpoin;
//					prop= but.rnasearchprop;
//
//					if(prop && RNA_property_collection_lookup_string(&ptr, prop, str, &rptr))
//						RNA_property_pointer_set(&but.rnapoin, but.rnaprop, rptr);
//
//					return 1;
//				}
//
//				return 0;
//			}
//		}
	}
	else if(but.type == IDPOIN) {
		/* ID pointer */
//    	but.idpoin_func(C, (char*)str, but.idpoin_idpp);
		return 1;
	}
	else if(but.type == TEX) {
		/* string */
		StringUtil.BLI_strncpy((byte[])but.poin.get(),0, str,offset, (int)but.hardmax);
		return 1;
	}
	else if(but.type == SEARCH_MENU) {
		/* string */
		StringUtil.BLI_strncpy((byte[])but.poin.get(),0, str,offset, (int)but.hardmax);
		return 1;
	}
//	else if(ui_but_anim_expression_set(but, str)) {
//		/* driver expression */
//		return 1;
//	}
	else {
		/* number editing */
		double value;

		/* XXX 2.50 missing python api */
//#if 0
//		if(BPY_button_eval(str, &value)) {
//			BKE_report(CTX_reports(C), RPT_WARNING, "Invalid Python expression, check console");
//			value = 0.0f; /* Zero out value on error */
//
//			if(str[0])
//				return 0;
//		}
//#else
//		value= atof(str);
		value= Double.parseDouble(StringUtil.toJString(str,offset));
//#endif

		if(!ui_is_but_float(but)) value= (int)value;
		if(but.type==NUMABS) value= Math.abs(value);

		/* not that we use hard limits here */
		if(value<but.hardmin) value= but.hardmin;
		if(value>but.hardmax) value= but.hardmax;

		ui_set_but_val(but, value);
		return 1;
	}

	return 0;
}

//static double soft_range_round_up(double value, double max)
//{
//	/* round up to .., 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, .. */
//	double newmax= pow(10.0, ceil(log(value)/log(10.0)));
//
//	if(newmax*0.2 >= max && newmax*0.2 >= value)
//		return newmax*0.2;
//	else if(newmax*0.5 >= max && newmax*0.5 >= value)
//		return newmax*0.5;
//	else
//		return newmax;
//}
//
//static double soft_range_round_down(double value, double max)
//{
//	/* round down to .., 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, .. */
//	double newmax= pow(10.0, floor(log(value)/log(10.0)));
//
//	if(newmax*5.0 <= max && newmax*5.0 <= value)
//		return newmax*5.0;
//	else if(newmax*2.0 <= max && newmax*2.0 <= value)
//		return newmax*2.0;
//	else
//		return newmax;
//}
//
//void ui_set_but_soft_range(uiBut *but, double value)
//{
//	PropertyType type;
//	double softmin, softmax, step, precision;
//
//	if(but.rnaprop) {
//		type= RNA_property_type(but.rnaprop);
//
//		if(type == PROP_INT) {
//			int imin, imax, istep;
//
//			RNA_property_int_ui_range(&but.rnapoin, but.rnaprop, &imin, &imax, &istep);
//			softmin= imin;
//			softmax= imax;
//			step= istep;
//			precision= 1;
//		}
//		else if(type == PROP_FLOAT) {
//			float fmin, fmax, fstep, fprecision;
//
//			RNA_property_float_ui_range(&but.rnapoin, but.rnaprop, &fmin, &fmax, &fstep, &fprecision);
//			softmin= fmin;
//			softmax= fmax;
//			step= fstep;
//			precision= fprecision;
//		}
//		else
//			return;
//
//		/* clamp button range to something reasonable in case
//		 * we get -inf/inf from RNA properties */
//		softmin= MAX2(softmin, -1e4);
//		softmax= MIN2(softmax, 1e4);
//
//		/* if the value goes out of the soft/max range, adapt the range */
//		if(value+1e-10 < softmin) {
//			if(value < 0.0)
//				softmin= -soft_range_round_up(-value, -softmin);
//			else
//				softmin= soft_range_round_down(value, softmin);
//
//			if(softmin < but.hardmin)
//				softmin= but.hardmin;
//		}
//		else if(value-1e-10 > softmax) {
//			if(value < 0.0)
//				softmax= -soft_range_round_down(-value, -softmax);
//			else
//				softmax= soft_range_round_up(value, softmax);
//
//			if(softmax > but.hardmax)
//				softmax= but.hardmax;
//		}
//
//		but.softmin= softmin;
//		but.softmax= softmax;
//	}
//}
//
///* ******************* Free ********************/
//
//static void ui_free_link(uiLink *link)
//{
//	if(link) {
//		BLI_freelistN(&link.lines);
//		MEM_freeN(link);
//	}
//}

/* can be called with C==NULL */
static void ui_free_but(bContext C, uiBut but)
{
//	if(but.opptr) {
//		WM_operator_properties_free(but.opptr);
//		MEM_freeN(but.opptr);
//	}
//	if(but.func_argN) MEM_freeN(but.func_argN);
	if(but.active!=null) {
		/* XXX solve later, buttons should be free-able without context? */
		if(C!=null)
			UIHandlers.ui_button_active_cancel(C, but);
//		else
//			if(but.active!=null)
//				MEM_freeN(but.active);
	}
//	if(but.str && but.str != but.strdata) MEM_freeN(but.str);
//	ui_free_link(but.link);

//	MEM_freeN(but);
}

/* can be called with C==NULL */
public static void uiFreeBlock(bContext C, uiBlock block)
{
	uiBut but;

	while( (but= block.buttons.first) != null ) {
		ListBaseUtil.BLI_remlink(block.buttons, but);
		ui_free_but(C, but);
	}

//	bContext.CTX_store_free_list(block.contexts);

	ListBaseUtil.BLI_freelistN(block.saferct);

//	MEM_freeN(block);
}

/* can be called with C==NULL */
public static void uiFreeBlocks(bContext C, ListBase lb)
{
	uiBlock block;

	while( (block= (uiBlock)lb.first) != null ) {
		ListBaseUtil.BLI_remlink(lb, block);
		uiFreeBlock(C, block);
	}
}

public static void uiFreeInactiveBlocks(bContext C, ListBase lb)
{
	uiBlock block, nextblock;

	for(block=(uiBlock)lb.first; block!=null; block=nextblock) {
		nextblock= block.next;

		if(block.handle==null) {
			if(block.active==0) {
				ListBaseUtil.BLI_remlink(lb, block);
				uiFreeBlock(C, block);
			}
			else
				block.active= 0;
		}
	}
}

public static void uiBlockSetRegion(uiBlock block, ARegion region)
{
	ListBase<uiBlock> lb;
	uiBlock oldblock= null;

	lb= region.uiblocks;

	/* each listbase only has one block with this name, free block
	 * if is already there so it can be rebuilt from scratch */
	if(lb!=null) {
		for (oldblock= lb.first; oldblock!=null; oldblock= oldblock.next)
			if (StringUtil.BLI_streq(oldblock.name,0, block.name,0))
				break;

		if (oldblock!=null) {
			oldblock.active= 0;
			oldblock.panel= null;
		}
	}

	block.oldblock= oldblock;

	/* at the beginning of the list! for dynamical menus/blocks */
	if(lb!=null)
		ListBaseUtil.BLI_addhead(lb, block);
}

public static uiBlock uiBeginBlock(bContext C, ARegion region, String name, int dt)
{
	uiBlock block;
	wmWindow window;
	Scene scn;
	int[] getsizex={0}, getsizey={0};

	window= bContext.CTX_wm_window(C);
	scn = bContext.CTX_data_scene(C);

	block= new uiBlock();
	block.active= 1;
	block.dt= (short)dt;
//	block.evil_C= (void*)C; // XXX
//	if (scn!=null) block.color_profile= (scn.r.color_mgt_flag & SceneTypes.R_COLOR_MANAGEMENT);
	StringUtil.BLI_strncpy(block.name,0, StringUtil.toCString(name),0, block.name.length);

	if(region!=null)
		uiBlockSetRegion(block, region);

	/* window matrix and aspect */
	if(region!=null && region.swinid!=0) {
		WmSubWindow.wm_subwindow_getmatrix(window, region.swinid, block.winmat);
		WmSubWindow.wm_subwindow_getsize(window, region.swinid, getsizex, getsizey);

		/* TODO - investigate why block.winmat[0][0] is negative
		 * in the image view when viewRedrawForce is called */
		block.aspect= 2.0f/StrictMath.abs( (getsizex[0])*block.winmat[0][0]);
	}
	else {
		/* no subwindow created yet, for menus for example, so we
		 * use the main window instead, since buttons are created
		 * there anyway */
		WmSubWindow.wm_subwindow_getmatrix(window, window.screen.mainwin, block.winmat);
		WmSubWindow.wm_subwindow_getsize(window, window.screen.mainwin, getsizex, getsizey);

		block.aspect= 2.0f/StrictMath.abs(getsizex[0]*block.winmat[0][0]);
		block.auto_open= 2;
		block.flag |= UI_BLOCK_LOOP; /* tag as menu */
	}

	return block;
}

//uiBlock *uiGetBlock(char *name, ARegion *ar)
//{
//	uiBlock *block= ar.uiblocks.first;
//
//	while(block) {
//		if( strcmp(name, block.name)==0 ) return block;
//		block= block.next;
//	}
//
//	return NULL;
//}

public static void uiBlockSetEmboss(uiBlock block, int dt)
{
	block.dt= (short)dt;
}

public static void ui_check_but(uiBut but)
{
	/* if something changed in the button */
	double value;
	float okwidth;
//	int transopts= ui_translate_buttons();

	ui_is_but_sel(but);

//	if(but.type==TEX || but.type==IDPOIN) transopts= 0;
	
//	/* only update soft range while not editing */
//	if(but.rnaprop && !(but.editval || but.editstr || but.editvec))
//		ui_set_but_soft_range(but, ui_get_but_val(but));

	/* test for min and max, icon sliders, etc */
	switch( but.type ) {
		case NUM:
		case SLI:
		case SCROLL:
		case NUMSLI:
		case HSVSLI:
			value= ui_get_but_val(but);
			if(value < but.hardmin) ui_set_but_val(but, but.hardmin);
			else if(value > but.hardmax) ui_set_but_val(but, but.hardmax);
			break;

		case NUMABS:
			value= StrictMath.abs( ui_get_but_val(but) );
			if(value < but.hardmin) ui_set_but_val(but, but.hardmin);
			else if(value > but.hardmax) ui_set_but_val(but, but.hardmax);
			break;

		case ICONTOG:
		case ICONTOGN:
//			if(but.rnaprop==null || (RNA_property_flag(but.rnaprop) & PROP_ICONS_CONSECUTIVE)) {
				if((but.flag & UI_SELECT)!=0) but.iconadd= 1;
				else but.iconadd= 0;
//			}
			break;

		case ICONROW:
//			if(!but.rnaprop || (RNA_property_flag(but.rnaprop) & PROP_ICONS_CONSECUTIVE)) {
				value= ui_get_but_val(but);
				but.iconadd= (short)((int)value- (int)(but.hardmin));
//			}
			break;

		case ICONTEXTROW:
//			if(!but.rnaprop || (RNA_property_flag(but.rnaprop) & PROP_ICONS_CONSECUTIVE)) {
				value= ui_get_but_val(but);
				but.iconadd= (short)((int)value- (int)(but.hardmin));
//			}
			break;
	}


	/* safety is 4 to enable small number buttons (like 'users') */
	okwidth= -4 + (but.x2 - but.x1);

	/* name: */
	switch( but.type ) {

	case MENU:
	case ICONTEXTROW:

		if(but.x2 - but.x1 > 24) {
			value= ui_get_but_val(but);
			UIRegions.ui_set_name_menu(but, (int)value);
		}
		break;

	case NUM:
	case NUMSLI:
	case HSVSLI:
	case NUMABS:

		value= ui_get_but_val(but);

        String but_drawstr;
		if(ui_is_but_float(but)) {
			if(value == RnaDefine.FLT_MAX) but_drawstr = String.format("%sinf", StringUtil.toJString(but.str,0));
			else if(value == -RnaDefine.FLT_MAX) but_drawstr = String.format("%s-inf", StringUtil.toJString(but.str,0));
			/* support length type buttons */
//			else if(ui_is_but_unit(but)) {
//				char new_str[sizeof(but->drawstr)];
//				ui_get_but_string_unit(but, new_str, sizeof(new_str), value, TRUE);
//				BLI_snprintf(but->drawstr, sizeof(but->drawstr), "%s%s", but->str, new_str);
//			}
			else if(but.a2!=0.0f) { /* amount of digits defined */
				if(but.a2==1.0f) but_drawstr = String.format("%s%.1f", StringUtil.toJString(but.str,0), value);
				else if(but.a2==2.0f) but_drawstr = String.format("%s%.2f", StringUtil.toJString(but.str,0), value);
				else if(but.a2==3.0f) but_drawstr = String.format("%s%.3f", StringUtil.toJString(but.str,0), value);
				else but_drawstr = String.format("%s%.4f", StringUtil.toJString(but.str,0), value);
			}
			else {
                if(but.hardmax<10.001f) but_drawstr = String.format("%s%.3f", StringUtil.toJString(but.str,0), value);
				else but_drawstr = String.format("%s%.2f", StringUtil.toJString(but.str,0), value);
			}
		}
		else {
            but_drawstr = String.format("%s%d", StringUtil.toJString(but.str,0), (int)value);
		}

//		if(but.rnaprop) {
//			PropertySubType pstype = RNA_property_subtype(but.rnaprop);
//
//			if (pstype == PROP_PERCENTAGE)
//				strcat(but.drawstr, "%");
//		}

        StringUtil.strcpy(but.drawstr,0, StringUtil.toCString(but_drawstr),0);
		break;

	case LABEL:
		if(ui_is_but_float(but)) {
			value= ui_get_but_val(but);
			if(but.a2!=0.0f) { /* amount of digits defined */
				if(but.a2==1.0f) but_drawstr = String.format("%s%.1f", StringUtil.toJString(but.str,0), value);
				else if(but.a2==2.0f) but_drawstr = String.format("%s%.2f", StringUtil.toJString(but.str,0), value);
				else if(but.a2==3.0f) but_drawstr = String.format("%s%.3f", StringUtil.toJString(but.str,0), value);
				else but_drawstr = String.format("%s%.4f", StringUtil.toJString(but.str,0), value);
			}
			else {
				but_drawstr = String.format("%s%.2f", StringUtil.toJString(but.str,0), value);
			}
            StringUtil.strcpy(but.drawstr,0, StringUtil.toCString(but_drawstr),0);
		}
		else StringUtil.strncpy(but.drawstr,0, but.str,0, UI_MAX_DRAW_STR);

		break;

	case IDPOIN:
	case TEX:
	case SEARCH_MENU:
//		if(!but.editstr) {
//			char str[UI_MAX_DRAW_STR];
//
//			ui_get_but_string(but, str, UI_MAX_DRAW_STR-strlen(but.str));
//
//			strcpy(but.drawstr, but.str);
//			strcat(but.drawstr, str);
//		}
		break;

	case KEYEVT:
//		strcpy(but.drawstr, but.str);
//		if (but.flag & UI_SELECT) {
//			strcat(but.drawstr, "Press a key");
//		} else {
//			strcat(but.drawstr, WM_key_event_string((short) ui_get_but_val(but)));
//		}
		break;

	case HOTKEYEVT:
//		if (but.flag & UI_SELECT) {
//			short *sp= (short *)but.func_arg3;
//
//			strcpy(but.drawstr, but.str);
//
//			if(*sp) {
//				char *str= but.drawstr;
//
//				if(*sp & KM_SHIFT)
//					str= strcat(str, "Shift ");
//				if(*sp & KM_CTRL)
//					str= strcat(str, "Ctrl ");
//				if(*sp & KM_ALT)
//					str= strcat(str, "Alt ");
//				if(*sp & KM_OSKEY)
//					str= strcat(str, "Cmd ");
//			}
//			else
//				strcat(but.drawstr, "Press a key  ");
//		} else {
//			/* XXX todo, button currently only used temporarily */
//			strcpy(but.drawstr, WM_key_event_string((short) ui_get_but_val(but)));
//		}
		break;

	case BUT_TOGDUAL:
		/* trying to get the dual-icon to left of text... not very nice */
		if(but.str[0]!=0) {
			StringUtil.strncpy(but.drawstr,0, StringUtil.toCString("  "),0, UI_MAX_DRAW_STR);
			StringUtil.strncpy(but.drawstr,2, but.str,0, UI_MAX_DRAW_STR-2);
		}
		break;
		
	case HSVCUBE:
	case HSVCIRCLE:
		break;
	default:
		StringUtil.strncpy(but.drawstr,0, but.str,0, UI_MAX_DRAW_STR);

	}

	/* if we are doing text editing, this will override the drawstr */
	if(but.editstr!=null)
		StringUtil.strncpy(but.drawstr,0, but.editstr,0, UI_MAX_DRAW_STR);

	/* text clipping moved to widget drawing code itself */
}


public static void uiBlockBeginAlign(uiBlock block)
{
	/* if other align was active, end it */
	if((block.flag & UI_BUT_ALIGN)!=0)
            uiBlockEndAlign(block);

	block.flag |= UI_BUT_ALIGN_DOWN;
	block.alignnr++;

	/* buttons declared after this call will get this align nr */ // XXX flag?
}

public static boolean buts_are_horiz(uiBut but1, uiBut but2)
{
	float dx, dy;

	dx= StrictMath.abs( but1.x2 - but2.x1);
	dy= StrictMath.abs( but1.y1 - but2.y2);

	if(dx > dy) return false;
	return true;
}

public static void uiBlockEndAlign(uiBlock block)
{
	block.flag &= ~UI_BUT_ALIGN;	// all 4 flags
}

public static boolean ui_but_can_align(uiBut but)
{
	return !(but.type==LABEL || but.type==OPTION || but.type==OPTIONN);
}

//static void ui_block_do_align_but(uiBlock block, uiBut first, int nr)
static void ui_block_do_align_but(uiBut first, int nr)
{
	uiBut prev, but=null, next;
	int flag= 0, cols=0, rows=0;

	/* auto align */

	for(but=first; but!=null && but.alignnr == nr; but=but.next) {
		if(but.next!=null && but.next.alignnr == nr) {
			if(buts_are_horiz(but, but.next)) cols++;
			else rows++;
		}
	}

	/* rows==0: 1 row, cols==0: 1 collumn */

	/* note;  how it uses 'flag' in loop below (either set it, or OR it) is confusing */
	for(but=first, prev=null; but!=null && but.alignnr == nr; prev=but, but=but.next) {
		next= but.next;
		if(next!=null && next.alignnr != nr)
			next= null;

		/* clear old flag */
		but.flag &= ~UI_BUT_ALIGN;

		if(flag==0) {	/* first case */
			if(next!=null) {
				if(buts_are_horiz(but, next)) {
					if(rows==0)
						flag= UI_BUT_ALIGN_RIGHT;
					else
						flag= UI_BUT_ALIGN_DOWN|UI_BUT_ALIGN_RIGHT;
				}
				else {
					flag= UI_BUT_ALIGN_DOWN;
				}
			}
		}
		else if(next==null) {	/* last case */
			if(prev!=null) {
				if(buts_are_horiz(prev, but)) {
					if(rows==0)
						flag= UI_BUT_ALIGN_LEFT;
					else
						flag= UI_BUT_ALIGN_TOP|UI_BUT_ALIGN_LEFT;
				}
				else flag= UI_BUT_ALIGN_TOP;
			}
		}
		else if(buts_are_horiz(but, next)) {
			/* check if this is already second row */
			if( prev!=null && buts_are_horiz(prev, but)==false) {
				flag &= ~UI_BUT_ALIGN_LEFT;
				flag |= UI_BUT_ALIGN_TOP;
				/* exception case: bottom row */
				if(rows>0) {
					uiBut bt= but;
					while(bt!=null && bt.alignnr == nr) {
						if(bt.next!=null && bt.next.alignnr == nr && buts_are_horiz(bt, bt.next)==false )
                            break;
						bt= bt.next;
					}
					if(bt==null || bt.alignnr != nr)
                        flag= UI_BUT_ALIGN_TOP|UI_BUT_ALIGN_RIGHT;
				}
			}
			else flag |= UI_BUT_ALIGN_LEFT;
		}
		else {
			if(cols==0) {
				flag |= UI_BUT_ALIGN_TOP;
			}
			else {	/* next button switches to new row */

				if(prev!=null && buts_are_horiz(prev, but))
				   flag |= UI_BUT_ALIGN_LEFT;
				else {
					flag &= ~UI_BUT_ALIGN_LEFT;
					flag |= UI_BUT_ALIGN_TOP;
				}

				if( (flag & UI_BUT_ALIGN_TOP)==0) {	/* stil top row */
//					if(prev!=null)
//						flag= UI_BUT_ALIGN_DOWN|UI_BUT_ALIGN_LEFT;
//					else
//						flag |= UI_BUT_ALIGN_DOWN;
					if(prev!=null) {
						if(next!=null && buts_are_horiz(but, next))
							flag = UI_BUT_ALIGN_DOWN|UI_BUT_ALIGN_LEFT|UI_BUT_ALIGN_RIGHT;
						else {
							/* last button in top row */
							flag = UI_BUT_ALIGN_DOWN|UI_BUT_ALIGN_LEFT;
						}
					}
					else 
						flag |= UI_BUT_ALIGN_DOWN;
				}
				else
					flag |= UI_BUT_ALIGN_TOP;
			}
		}

		but.flag |= flag;

		/* merge coordinates */
		if(prev!=null) {
			// simple cases
			if(rows==0) {
				but.x1= (prev.x2+but.x1)/2.0f;
				prev.x2= but.x1;
			}
			else if(cols==0) {
				but.y2= (prev.y1+but.y2)/2.0f;
				prev.y1= but.y2;
			}
			else {
				if(buts_are_horiz(prev, but)) {
					but.x1= (prev.x2+but.x1)/2.0f;
					prev.x2= but.x1;
					/* copy height too */
					but.y2= prev.y2;
				}
				else if(prev.prev!=null && buts_are_horiz(prev.prev, prev)==false) {
					/* the previous button is a single one in its row */
					but.y2= (prev.y1+but.y2)/2.0f;
					prev.y1= but.y2;
					
					but.x1= prev.x1;
					if(next!=null && buts_are_horiz(but, next)==false)
						but.x2= prev.x2;
				}
				else {
					/* the previous button is not a single one in its row */
					but.y2= prev.y1;
				}
			}
		}
	}
}

public static void ui_block_do_align(uiBlock block)
{
	uiBut but;
	int nr;

	/* align buttons with same align nr */
	for(but=block.buttons.first; but!=null;) {
		if(but.alignnr!=0) {
			nr= but.alignnr;
			ui_block_do_align_but(but, nr);

			/* skip with same number */
			for(; but!=null && but.alignnr == nr; but=but.next);

			if(but==null)
				break;
		}
		else
			but= but.next;
	}
}

/*
ui_def_but is the function that draws many button types

for float buttons:
	"a1" Click Step (how much to change the value each click)
	"a2" Number of decimal point values to display. 0 defaults to 3 (0.000) 1,2,3, and a maximum of 4,
       all greater values will be clamped to 4.

*/
static uiBut ui_def_but(uiBlock block, int type, int retval, byte[] str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  byte[] tip)
{
	uiBut but;
	int slen;

	if((type & BUTPOIN)!=0) {		/* a pointer is required */
		if(poin==null)
			return null;
	}

	but= new uiBut();

	but.type= (short)(type & BUTTYPE);
	but.pointype= (short)(type & BUTPOIN);
	but.bit= (short)(type & BIT);
	but.bitnr= (short)(type & 31);
	but.icon = null;
	but.iconadd=0;

	but.retval= (short)retval;
	
	slen= StringUtil.strlen(str,0);
    if(slen >= UI_MAX_NAME_STR-1) {
        but.str= new byte[StringUtil.strlen(str,0)+2];
//        StringUtil.strcpy(but.str,0, str,0);
	}
	else {
	    but.str= but.strdata;
//	    StringUtil.strcpy(but.str,0, str,0);
	}
    System.arraycopy(str,0, but.str,0, slen+1);
        
	but.x1= x1;
	but.y1= y1;
	but.x2= (x1+x2);
	but.y2= (y1+y2);

	but.poin= poin;
	but.hardmin= but.softmin= min;
	but.hardmax= but.softmax= max;
	but.a1= a1;
	but.a2= a2;
	but.tip= tip;

	but.lock= (short)block.lock;
	but.lockstr= block.lockstr;
	but.dt= block.dt;

	but.aspect= 1.0f; //XXX block.aspect;
	but.block= block;		// pointer back, used for frontbuffer status, and picker

	if((block.flag & UI_BUT_ALIGN)!=0 && ui_but_can_align(but))
		but.alignnr= block.alignnr;

	but.func= block.func;
	but.func_arg1= block.func_arg1;
	but.func_arg2= block.func_arg2;
	
//	but.funcN= block.funcN;
//	if(block.func_argN)
//		but.func_argN= MEM_dupallocN(block.func_argN);

	but.pos= -1;	/* cursor invisible */

	if(but.type==NUM || but.type==NUMABS || but.type==NUMSLI || but.type==HSVSLI) {	/* add a space to name */
		slen= (short)StringUtil.strlen(but.str,0);
		if(slen>0 && slen<UI_MAX_NAME_STR-2) {
			if(but.str[slen-1]!=' ') {
				but.str[slen]= ' ';
				but.str[slen+1]= 0;
			}
		}
	}

//	if(but.type==HSVCUBE || but.type==HSVCIRCLE) { /* hsv buttons temp storage */
//		float[] rgb=new float[3];
//		ui_get_but_vectorf(but, rgb);
//		Arithb.rgb_to_hsv(rgb[0], rgb[1], rgb[2], but.hsv);
//	}

	if((block.flag & UI_BLOCK_LOOP)!=0 || (but.type==MENU || but.type==TEX || but.type==LABEL || but.type==IDPOIN || but.type==BLOCK || but.type==BUTM || but.type==SEARCH_MENU || but.type==PROGRESSBAR))
		but.flag |= (UI_TEXT_LEFT|UI_ICON_LEFT);
	else if(but.type==BUT_TOGDUAL)
		but.flag |= UI_ICON_LEFT;

	but.flag |= (block.flag & UI_BUT_ALIGN);

	if (but.lock!=0) {
		if (but.lockstr!=null) {
			but.flag |= UI_BUT_DISABLED;
		}
	}
	
	if((but.type == BLOCK || but.type == BUT || but.type == LABEL || but.type == PULLDOWN || but.type == ROUNDBOX || but.type == LISTBOX || but.type == SEARCH_MENU || but.type == BUTM));
	else if((but.type == SCROLL || but.type == SEPR || but.type == LINK || but.type == INLINK || but.type == FTPREVIEW));
	else but.flag |= UI_BUT_UNDO;

	ListBaseUtil.BLI_addtail(block.buttons, but);

	if(block.curlayout!=null)
		UILayout.ui_layout_add_but(block.curlayout, but);

	return but;
}

public static uiBut ui_def_but_rna(uiBlock block, int type, int retval, String str, int x1, int y1, int x2, int y2, PointerRNA ptr, String propname, int index, float min, float max, float a1, float a2, String tip)
{
	uiBut but;
	PropertyRNA prop;
	int proptype;
	int freestr= 0, icon= 0;

	prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0);

	if(prop!=null) {
//		System.out.println("ui_def_but_rna: "+propname);
//		if ((prop.flag & RNATypes.PROP_IDPROPERTY)!=0) {
//			System.out.println("ui_def_but_rna: PROP_IDPROPERTY");
//		}
		
		proptype= RnaAccess.RNA_property_type(prop);

		/* use rna values if parameters are not specified */
		if(str==null) {
			if(type == MENU && proptype == RNATypes.PROP_ENUM) {
				EnumPropertyItem[][] item = new EnumPropertyItem[1][];
				StringBuilder dynstr;
				int i, value;
				int[] totitem={0}, free={0};

				RnaAccess.RNA_property_enum_items((bContext)block.evil_C, ptr, prop, item, totitem, free);
				value= RnaAccess.RNA_property_enum_get(ptr, prop);

				dynstr= new StringBuilder();
				dynstr.append(String.format("%s%%t", RnaAccess.RNA_property_ui_name(prop)));
				for(i=0; i<totitem[0]; i++) {
					if(StringUtil.toCString(item[0][i].identifier)[0]==0) {
						if(item[0][i].name!=null)
							dynstr.append(String.format("|%s%%l", item[0][i].name));
						else
							dynstr.append("|%l");
					}
					else if(item[0][i].icon!=0)
						dynstr.append(String.format("|%s %%i%d %%x%d", item[0][i].name, item[0][i].icon, item[0][i].value));
					else
						dynstr.append(String.format("|%s %%x%d", item[0][i].name, item[0][i].value));

					if(value == item[0][i].value)
						icon= item[0][i].icon;
				}
				str= dynstr.toString();

//				if(free)
//					MEM_freeN(item);
//
//				freestr= 1;
			}
			else if((type == ROW || type == LISTROW) && proptype == RNATypes.PROP_ENUM) {
				EnumPropertyItem[][] item = new EnumPropertyItem[1][];
				int i;
				int[] totitem={0}, free={0};

				RnaAccess.RNA_property_enum_items((bContext)block.evil_C, ptr, prop, item, totitem, free);
				for(i=0; i<totitem[0]; i++) {
//					if(item[0][i].identifier[0] && item[0][i].value == (int)max) {
					if(StringUtil.toCString(item[0][i].identifier)[0]!=0 && item[0][i].value == (int)max) {
						str= (String)item[0][i].name;
						icon= item[0][i].icon;
					}
				}

				if(str==null)
					str= (String)RnaAccess.RNA_property_ui_name(prop);
//				if(free[0]!=0)
//					MEM_freeN(item);
			}
			else {
				str= (String)RnaAccess.RNA_property_ui_name(prop);
				icon= RnaAccess.RNA_property_ui_icon(prop);
			}
		}

//		if(!tip) {
//			if(type == ROW && proptype == PROP_ENUM) {
//				EnumPropertyItem *item;
//				int i, totitem, free;
//
//				RNA_property_enum_items(block.evil_C, ptr, prop, &item, &totitem, &free);
//
//				for(i=0; i<totitem; i++) {
//					if(item[i].identifier[0] && item[i].value == (int)max) {
//						if(item[i].description[0])
//							tip= (char*)item[i].description;
//						break;
//					}
//				}
//
//				if(free)
//					MEM_freeN(item);
//			}
//		}

		if(tip==null)
			tip= (String)RnaAccess.RNA_property_ui_description(prop);

		if(min == max || a1 == -1 || a2 == -1) {
			if(proptype == RNATypes.PROP_INT) {
				int[] hardmin={0}, hardmax={0}, softmin={0}, softmax={0}, step={0};

				RnaAccess.RNA_property_int_range(ptr, prop, hardmin, hardmax);
				RnaAccess.RNA_property_int_ui_range(ptr, prop, softmin, softmax, step);

				if(!(type == ROW || type == LISTROW) && min == max) {
					min= hardmin[0];
					max= hardmax[0];
				}
				if(a1 == -1)
					a1= step[0];
				if(a2 == -1)
					a2= 0;
			}
			else if(proptype == RNATypes.PROP_FLOAT) {
				float[] hardmin={0}, hardmax={0}, softmin={0}, softmax={0}, step={0}, precision={0};

				RnaAccess.RNA_property_float_range(ptr, prop, hardmin, hardmax);
				RnaAccess.RNA_property_float_ui_range(ptr, prop, softmin, softmax, step, precision);

				if(!(type == ROW || type == LISTROW) && min == max) {
					min= hardmin[0];
					max= hardmax[0];
				}
				if(a1 == -1)
					a1= step[0];
				if(a2 == -1)
					a2= precision[0];
			}
			else if(proptype == RNATypes.PROP_STRING) {
				min= 0;
				max= RnaAccess.RNA_property_string_maxlength(prop);
				if(max == 0) /* interface code should ideally support unlimited length */
					max= UI_MAX_DRAW_STR;
			}
		}
	}
	else
		str= (String)propname;

	/* now create button */
	but= ui_def_but(block, type, retval, StringUtil.toCString(str), x1, y1, x2, y2, null, min, max, a1, a2, StringUtil.toCString(tip));

	if(prop!=null) {
		but.rnapoin= ptr;
		but.rnaprop= prop;
//		System.out.println("ui_def_but_rna rnaprop: "+prop.identifier);

		if(RnaAccess.RNA_property_array_length(but.rnapoin, but.rnaprop)!=0)
			but.rnaindex= index;
		else
			but.rnaindex= 0;
	}

	if(icon!=0) {
		but.icon= BIFIconID.values()[icon];
		but.flag |= UI_HAS_ICON;
		but.flag|= UI_ICON_LEFT;
	}

//	if (!prop || !RNA_property_editable(&but.rnapoin, prop)) {
//		but.flag |= UI_BUT_DISABLED;
//		but.lock = 1;
//		but.lockstr = "";
//	}
	
//	/* If this button uses units, calculate the step from this */
//	if(ui_is_but_unit(but))
//		but->a1= ui_get_but_step_unit(but, but->a1);

//	if(freestr)
//		MEM_freeN(str);

	return but;
}

public static uiBut ui_def_but_operator(uiBlock block, int type, String opname, int opcontext, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but;
	wmOperatorType ot;

	ot= WmOperators.WM_operatortype_find(opname, false);

	if(str==null) {
		if(ot!=null) str= ot.name;
		else str= opname;
	}

    if ((tip==null || tip.isEmpty()) && ot!=null && ot.description!=null) {
		tip= ot.description;
	}

	but= ui_def_but(block, type, -1, StringUtil.toCString(str), x1, y1, x2, y2, null, 0, 0, 0, 0, StringUtil.toCString(tip));
	but.optype= ot;
	but.opcontext= opcontext;

	if(ot==null) {
		but.flag |= UI_BUT_DISABLED;
		but.lock = 1;
		but.lockstr = StringUtil.toCString("");
	}

	return but;
}

public static uiBut uiDefBut(uiBlock block, int type, int retval, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	uiBut but= ui_def_but(block, type, retval, StringUtil.toCString(str), x1, y1, x2, y2, poin, min, max, a1, a2, StringUtil.toCString(tip));

	ui_check_but(but);

	return but;
}

	/* if _x_ is a power of two (only one bit) return the power,
	 * otherwise return -1.
	 * (1<<findBitIndex(x))==x for powers of two.
	 */
public static int findBitIndex(int x) {
	if (x==0 || (x&(x-1))!=0) {	/* x&(x-1) strips lowest bit */
		return -1;
	} else {
		int idx= 0;

		if ((x&0xFFFF0000)!=0)	{idx+=16; x>>=16;}
		if ((x&0xFF00)!=0)		{idx+=8; x>>=8;}
		if ((x&0xF0)!=0)		{idx+=4; x>>=4;}
		if ((x&0xC)!=0)			{idx+=2; x>>=2;}
		if ((x&0x2)!=0)			{idx+=1;}

		return idx;
	}
}

///* autocomplete helper functions */
//struct AutoComplete {
//	int maxlen;
//	char *truncate;
//	char *startname;
//};
//
//AutoComplete *autocomplete_begin(char *startname, int maxlen)
//{
//	AutoComplete *autocpl;
//
//	autocpl= MEM_callocN(sizeof(AutoComplete), "AutoComplete");
//	autocpl.maxlen= maxlen;
//	autocpl.truncate= MEM_callocN(sizeof(char)*maxlen, "AutoCompleteTruncate");
//	autocpl.startname= startname;
//
//	return autocpl;
//}
//
//void autocomplete_do_name(AutoComplete *autocpl, const char *name)
//{
//	char *truncate= autocpl.truncate;
//	char *startname= autocpl.startname;
//	int a;
//
//	for(a=0; a<autocpl.maxlen-1; a++) {
//		if(startname[a]==0 || startname[a]!=name[a])
//			break;
//	}
//	/* found a match */
//	if(startname[a]==0) {
//		/* first match */
//		if(truncate[0]==0)
//			BLI_strncpy(truncate, name, autocpl.maxlen);
//		else {
//			/* remove from truncate what is not in bone.name */
//			for(a=0; a<autocpl.maxlen-1; a++) {
//				if(name[a] == 0) {
//					truncate[a]= 0;
//					break;
//				}
//				else if(truncate[a]!=name[a])
//					truncate[a]= 0;
//			}
//		}
//	}
//}
//
//void autocomplete_end(AutoComplete *autocpl, char *autoname)
//{
//	if(autocpl.truncate[0])
//		BLI_strncpy(autoname, autocpl.truncate, autocpl.maxlen);
//	else {
//		if (autoname != autocpl.startname) /* dont copy a string over its self */
//			BLI_strncpy(autoname, autocpl.startname, autocpl.maxlen);
//	}
//	MEM_freeN(autocpl.truncate);
//	MEM_freeN(autocpl);
//}
//
///* autocomplete callback for ID buttons */
//static void autocomplete_id(bContext *C, char *str, void *arg_v)
//{
//	int blocktype= (intptr_t)arg_v;
//	ListBase *listb= wich_libbase(CTX_data_main(C), blocktype);
//
//	if(listb==NULL) return;
//
//	/* search if str matches the beginning of an ID struct */
//	if(str[0]) {
//		AutoComplete *autocpl= autocomplete_begin(str, 22);
//		ID *id;
//
//		for(id= listb.first; id; id= id.next)
//			autocomplete_do_name(autocpl, id.name+2);
//
//		autocomplete_end(autocpl, str);
//	}
//}
//
//static uiBut *uiDefButBit(uiBlock *block, int type, int bit, int retval, char *str, short x1, short y1, short x2, short y2, void *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	int bitIdx= findBitIndex(bit);
//	if (bitIdx==-1) {
//		return NULL;
//	} else {
//		return uiDefBut(block, type|BIT|bitIdx, retval, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
//	}
//}
public static uiBut uiDefButF(uiBlock block, int type, int retval, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefBut(block, type|FLO, retval, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefButBitF(uiBlock *block, int type, int bit, int retval, char *str, short x1, short y1, short x2, short y2, float *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefButBit(block, type|FLO, bit, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefButI(uiBlock *block, int type, int retval, char *str, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefBut(block, type|INT, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefButBitI(uiBlock *block, int type, int bit, int retval, char *str, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefButBit(block, type|INT, bit, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefButS(uiBlock block, int type, int retval, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefBut(block, type|SHO, retval, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefButBitS(uiBlock *block, int type, int bit, int retval, char *str, short x1, short y1, short x2, short y2, short *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefButBit(block, type|SHO, bit, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefButC(uiBlock *block, int type, int retval, char *str, short x1, short y1, short x2, short y2, char *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefBut(block, type|CHA, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefButBitC(uiBlock *block, int type, int bit, int retval, char *str, short x1, short y1, short x2, short y2, char *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefButBit(block, type|CHA, bit, retval, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefButR(uiBlock block, int type, int retval, String str, int x1, int y1, int x2, int y2, PointerRNA ptr, String propname, int index, float min, float max, float a1, float a2,  String tip)
{
	uiBut but;

	but= ui_def_but_rna(block, type, retval, str, x1, y1, x2, y2, ptr, propname, index, min, max, a1, a2, tip);
	if(but!=null)
		ui_check_but(but);

	return but;
}

public static uiBut uiDefButO(uiBlock block, int type, String opname, int opcontext, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but;

	but= ui_def_but_operator(block, type, opname, opcontext, str, x1, y1, x2, y2, tip);
	if(but!=null)
		ui_check_but(but);

	return but;
}

//uiBut *uiDefButTextO(uiBlock *block, int type, const char *opname, int opcontext, const char *str, int x1, int y1, short x2, short y2, void *poin, float min, float max, float a1, float a2,  const char *tip)
//{
//	uiBut *but= ui_def_but_operator_text(block, type, opname, opcontext, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
//
//	if(but)
//		ui_check_but(but);
//	
//	return but;
//}

/* if a1==1.0 then a2 is an extra icon blending factor (alpha 0.0 - 1.0) */
public static uiBut uiDefIconBut(uiBlock block, int type, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	uiBut but= ui_def_but(block, type, retval, StringUtil.toCString(""), x1, y1, x2, y2, poin, min, max, a1, a2, StringUtil.toCString(tip));

	but.icon= icon;
	but.flag|= UI_HAS_ICON;

	ui_check_but(but);

	return but;
}

public static uiBut uiDefIconButBit(uiBlock block, int type, int bit, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	int bitIdx= findBitIndex(bit);
	if (bitIdx==-1) {
		return null;
	} else {
		return uiDefIconBut(block, type|BIT|bitIdx, retval, icon, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
	}
}

//uiBut *uiDefIconButF(uiBlock *block, int type, int retval, int icon, short x1, short y1, short x2, short y2, float *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconBut(block, type|FLO, retval, icon, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefIconButBitF(uiBlock *block, int type, int bit, int retval, int icon, short x1, short y1, short x2, short y2, float *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconButBit(block, type|FLO, bit, retval, icon, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefIconButI(uiBlock *block, int type, int retval, int icon, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconBut(block, type|INT, retval, icon, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefIconButBitI(uiBlock *block, int type, int bit, int retval, int icon, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconButBit(block, type|INT, bit, retval, icon, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefIconButS(uiBlock block, int type, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconBut(block, type|SHO, retval, icon, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
public static uiBut uiDefIconButBitS(uiBlock block, int type, int bit, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconButBit(block, type|SHO, bit, retval, icon, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefIconButC(uiBlock *block, int type, int retval, int icon, short x1, short y1, short x2, short y2, char *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconBut(block, type|CHA, retval, icon, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefIconButBitC(uiBlock block, int type, int bit, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconButBit(block, type|CHA, bit, retval, icon, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
public static uiBut uiDefIconButR(uiBlock block, int type, int retval, BIFIconID icon, int x1, int y1, int x2, int y2, PointerRNA ptr, String propname, int index, float min, float max, float a1, float a2,  String tip)
{
	uiBut but;

	but= ui_def_but_rna(block, type, retval, "", x1, y1, x2, y2, ptr, propname, index, min, max, a1, a2, tip);
	if(but!=null) {
		if(icon!=null) {
			but.icon= (BIFIconID) icon;
			but.flag|= UI_HAS_ICON;
		}
		ui_check_but(but);
	}

	return but;
}

public static uiBut uiDefIconButO(uiBlock block, int type, String opname, int opcontext, BIFIconID icon, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but;

	but= ui_def_but_operator(block, type, opname, opcontext, "", x1, y1, x2, y2, tip);
	if(but!=null) {
		but.icon= icon;
		but.flag|= UI_HAS_ICON;
		ui_check_but(but);
	}

	return but;
}

/* Button containing both string label and icon */
public static uiBut uiDefIconTextBut(uiBlock block, int type, int retval, BIFIconID icon, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	uiBut but= ui_def_but(block, type, retval, StringUtil.toCString(str), x1, y1, x2, y2, poin, min, max, a1, a2, StringUtil.toCString(tip));

	but.icon= (BIFIconID) icon;
	but.flag|= UI_HAS_ICON;

	but.flag|= UI_ICON_LEFT;

	ui_check_but(but);

	return but;
}
//static uiBut *uiDefIconTextButBit(uiBlock *block, int type, int bit, int retval, int icon, char *str, short x1, short y1, short x2, short y2, void *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	int bitIdx= findBitIndex(bit);
//	if (bitIdx==-1) {
//		return NULL;
//	} else {
//		return uiDefIconTextBut(block, type|BIT|bitIdx, retval, icon, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
//	}
//}

public static uiBut uiDefIconTextButF(uiBlock block, int type, int retval, BIFIconID icon, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconTextBut(block, type|FLO, retval, icon, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefIconTextButBitF(uiBlock *block, int type, int bit, int retval, int icon, char *str, short x1, short y1, short x2, short y2, float *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconTextButBit(block, type|FLO, bit, retval, icon, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefIconTextButI(uiBlock *block, int type, int retval, int icon, char *str, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconTextBut(block, type|INT, retval, icon, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
//uiBut *uiDefIconTextButBitI(uiBlock *block, int type, int bit, int retval, int icon, char *str, short x1, short y1, short x2, short y2, int *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconTextButBit(block, type|INT, bit, retval, icon, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefIconTextButS(uiBlock block, int type, int retval, BIFIconID icon, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconTextBut(block, type|SHO, retval, icon, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefIconTextButBitS(uiBlock *block, int type, int bit, int retval, int icon, char *str, short x1, short y1, short x2, short y2, short *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconTextButBit(block, type|SHO, bit, retval, icon, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefIconTextButC(uiBlock block, int type, int retval, BIFIconID icon, String str, int x1, int y1, int x2, int y2, Pointer poin, float min, float max, float a1, float a2,  String tip)
{
	return uiDefIconTextBut(block, type|CHA, retval, icon, str, x1, y1, x2, y2, poin, min, max, a1, a2, tip);
}
//uiBut *uiDefIconTextButBitC(uiBlock *block, int type, int bit, int retval, int icon, char *str, short x1, short y1, short x2, short y2, char *poin, float min, float max, float a1, float a2,  char *tip)
//{
//	return uiDefIconTextButBit(block, type|CHA, bit, retval, icon, str, x1, y1, x2, y2, (void*) poin, min, max, a1, a2, tip);
//}
public static uiBut uiDefIconTextButR(uiBlock block, int type, int retval, BIFIconID icon, String str, int x1, int y1, int x2, int y2, PointerRNA ptr, String propname, int index, float min, float max, float a1, float a2,  String tip)
{
	uiBut but;

	but= ui_def_but_rna(block, type, retval, str, x1, y1, x2, y2, ptr, propname, index, min, max, a1, a2, tip);
	if(but!=null) {
		if(icon!=null) {
			but.icon= (BIFIconID) icon;
			but.flag|= UI_HAS_ICON;
		}
		but.flag|= UI_ICON_LEFT;
		ui_check_but(but);
	}

	return but;
}

public static uiBut uiDefIconTextButO(uiBlock block, int type, String opname, int opcontext, BIFIconID icon, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but;

	but= ui_def_but_operator(block, type, opname, opcontext, str, x1, y1, x2, y2, tip);
	if(but!=null) {
		but.icon= icon;
		but.flag|= UI_HAS_ICON;
		but.flag|= UI_ICON_LEFT;
		ui_check_but(but);
	}

	return but;
}

///* END Button containing both string label and icon */
//
//void uiSetButLink(uiBut *but, void **poin, void ***ppoin, short *tot, int from, int to)
//{
//	uiLink *link;
//
//	link= but.link= MEM_callocN(sizeof(uiLink), "new uilink");
//
//	link.poin= poin;
//	link.ppoin= ppoin;
//	link.totlink= tot;
//	link.fromcode= from;
//	link.tocode= to;
//}
//
///* cruft to make uiBlock and uiBut private */
//
//int uiBlocksGetYMin(ListBase *lb)
//{
//	uiBlock *block;
//	int min= 0;
//
//	for (block= lb.first; block; block= block.next)
//		if (block==lb.first || block.miny<min)
//			min= block.miny;
//
//	return min;
//}

public static void uiBlockSetDirection(uiBlock block, int direction)
{
	block.direction= (short)direction;
}

/* this call escapes if there's alignment flags */
public static void uiBlockFlipOrder(uiBlock block)
{
	ListBase lb = new ListBase();
	uiBut but, next;
	float centy, miny=10000, maxy= -10000;

	if((U.uiflag & UserDefTypes.USER_MENUFIXEDORDER)!=0)
		return;
	else if((block.flag & UI_BLOCK_NO_FLIP)!=0)
		return;

	for(but= block.buttons.first; but!=null; but= but.next) {
		if((but.flag & UI_BUT_ALIGN)!=0) return;
		if(but.y1 < miny) miny= but.y1;
		if(but.y2 > maxy) maxy= but.y2;
	}
	/* mirror trick */
	centy= (miny+maxy)/2.0f;
	for(but= block.buttons.first; but!=null; but= but.next) {
		but.y1 = centy-(but.y1-centy);
		but.y2 = centy-(but.y2-centy);
//		SWAP(float, but.y1, but.y2);
        float sw_ap=but.y1; but.y1=but.y2; but.y2=sw_ap;
	}

	/* also flip order in block itself, for example for arrowkey */
	lb.first= lb.last= null;
	but= block.buttons.first;
	while(but!=null) {
		next= but.next;
		ListBaseUtil.BLI_remlink(block.buttons, but);
		ListBaseUtil.BLI_addtail(lb, but);
		but= next;
	}
	block.buttons= lb;
}


public static void uiBlockSetFlag(uiBlock block, int flag)
{
	block.flag|= flag;
}

public static void uiBlockClearFlag(uiBlock block, int flag)
{
	block.flag&= ~flag;
}

public static void uiBlockSetXOfs(uiBlock block, int xofs)
{
	block.xofs= xofs;
}

public static void uiButSetFlag(uiBut but, int flag)
{
	but.flag|= flag;
}

public static void uiButClearFlag(uiBut but, int flag)
{
	but.flag&= ~flag;
}

public static int uiButGetRetVal(uiBut but)
{
	return but.retval;
}

//void uiButSetDragID(uiBut *but, ID *id)
//{
//	but->dragtype= WM_DRAG_ID;
//	but->dragpoin= (void *)id;
//}
//
//void uiButSetDragRNA(uiBut *but, PointerRNA *ptr)
//{
//	but->dragtype= WM_DRAG_RNA;
//	but->dragpoin= (void *)ptr;
//}
//
//void uiButSetDragPath(uiBut *but, const char *path)
//{
//	but->dragtype= WM_DRAG_PATH;
//	but->dragpoin= (void *)path;
//}
//
//void uiButSetDragName(uiBut *but, const char *name)
//{
//	but->dragtype= WM_DRAG_NAME;
//	but->dragpoin= (void *)name;
//}
//
///* value from button itself */
//void uiButSetDragValue(uiBut *but)
//{
//	but->dragtype= WM_DRAG_VALUE;
//}
//
//void uiButSetDragImage(uiBut *but, const char *path, int icon, struct ImBuf *imb, float scale)
//{
//	but->dragtype= WM_DRAG_PATH;
//	but->icon= icon; /* no flag UI_HAS_ICON, so icon doesnt draw in button */
//	but->dragpoin= (void *)path;
//	but->imb= imb;
//	but->imb_scale= scale;
//}

public static PointerRNA uiButGetOperatorPtrRNA(uiBut but)
{
	if(but.optype!=null && but.opptr==null) {
		but.opptr= new PointerRNA();
		WmOperators.WM_operator_properties_create_ptr(but.opptr, but.optype);
	}

	return but.opptr;
}

public static void uiBlockSetHandleFunc(uiBlock block, uiHandleFunc func, Object arg)
{
	block.handle_func= func;
	block.handle_func_arg= arg;
}

//void uiBlockSetButmFunc(uiBlock *block, uiMenuHandleFunc func, void *arg)
//{
//	block.butm_func= func;
//	block.butm_func_arg= arg;
//}
//
//void uiBlockSetFunc(uiBlock *block, uiButHandleFunc func, void *arg1, void *arg2)
//{
//	block.func= func;
//	block.func_arg1= arg1;
//	block.func_arg2= arg2;
//}
//
//void uiButSetRenameFunc(uiBut *but, uiButHandleRenameFunc func, void *arg1)
//{
//	but.rename_func= func;
//	but.rename_arg1= arg1;
//}
//
//void uiBlockSetDrawExtraFunc(uiBlock *block, void (*func)(const bContext *C, void *idv, void *argv, rcti *rect), void *arg)
//{
//	block.drawextra= func;
//	block.drawextra_arg= arg;
//}

public static void uiButSetFunc(uiBut but, uiHandleFunc func, Object arg1, Object arg2)
{
	but.func= func;
	but.func_arg1= arg1;
	but.func_arg2= arg2;
}

//void uiButSetNFunc(uiBut *but, uiButHandleNFunc funcN, void *argN, void *arg2)
//{
//	but.funcN= funcN;
//	but.func_argN= argN;
//	but.func_arg2= arg2;
//}
//
//void uiButSetCompleteFunc(uiBut *but, uiButCompleteFunc func, void *arg)
//{
//	but.autocomplete_func= func;
//	but.autofunc_arg= arg;
//}
//
//uiBut *uiDefIDPoinBut(uiBlock *block, uiIDPoinFuncFP func, short blocktype, int retval, char *str, short x1, short y1, short x2, short y2, void *idpp, char *tip)
//{
//	uiBut *but= ui_def_but(block, IDPOIN, retval, str, x1, y1, x2, y2, NULL, 0.0, 0.0, 0.0, 0.0, tip);
//	but.idpoin_func= func;
//	but.idpoin_idpp= (ID**) idpp;
//	ui_check_but(but);
//
//	if(blocktype)
//		uiButSetCompleteFunc(but, autocomplete_id, (void *)(intptr_t)blocktype);
//
//	return but;
//}

public static uiBut uiDefBlockBut(uiBlock block, uiBlockCreateFunc func, Pointer arg, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, BLOCK, 0, StringUtil.toCString(str), x1, y1, x2, y2, arg, 0.0f, 0.0f, 0.0f, 0.0f, StringUtil.toCString(tip));
	but.block_create_func= func;
	ui_check_but(but);
	return but;
}

//uiBut *uiDefBlockButN(uiBlock *block, uiBlockCreateFunc func, void *argN, char *str, short x1, short y1, short x2, short y2, char *tip)
//{
//	uiBut *but= ui_def_but(block, BLOCK, 0, str, x1, y1, x2, y2, NULL, 0.0, 0.0, 0.0, 0.0, tip);
//	but.block_create_func= func;
//	but.func_argN= argN;
//	ui_check_but(but);
//	return but;
//}


public static uiBut uiDefPulldownBut(uiBlock block, uiBlockCreateFunc func, Pointer arg, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, PULLDOWN, 0, StringUtil.toCString(str), x1, y1, x2, y2, arg, 0.0f, 0.0f, 0.0f, 0.0f, StringUtil.toCString(tip));
	but.block_create_func= func;
	ui_check_but(but);
	return but;
}

public static uiBut uiDefMenuBut(uiBlock block, uiMenuCreateFunc func, Pointer arg, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, PULLDOWN, 0, StringUtil.toCString(str), x1, y1, x2, y2, arg, 0.0f, 0.0f, 0.0f, 0.0f, StringUtil.toCString(tip));
	but.menu_create_func= func;
	ui_check_but(but);
	return but;
}

public static uiBut uiDefIconTextMenuBut(uiBlock block, uiMenuCreateFunc func, Pointer arg, BIFIconID icon, String str, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, PULLDOWN, 0, StringUtil.toCString(str), x1, y1, x2, y2, arg, 0.0f, 0.0f, 0.0f, 0.0f, StringUtil.toCString(tip));

	but.icon= icon;
	but.flag|= UI_HAS_ICON;

	but.flag|= UI_ICON_LEFT;
	but.flag|= UI_ICON_SUBMENU;

	but.menu_create_func= func;
	ui_check_but(but);

	return but;
}

public static uiBut uiDefIconMenuBut(uiBlock block, uiMenuCreateFunc func, Pointer arg, BIFIconID icon, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, PULLDOWN, 0, StringUtil.toCString(""), x1, y1, x2, y2, arg, 0.0f, 0.0f, 0.0f, 0.0f, StringUtil.toCString(tip));

	but.icon= icon;
	but.flag |= UI_HAS_ICON;
	but.flag &=~ UI_ICON_LEFT;

	but.menu_create_func= func;
	ui_check_but(but);

	return but;
}

///* Block button containing both string label and icon */
//uiBut *uiDefIconTextBlockBut(uiBlock *block, uiBlockCreateFunc func, void *arg, int icon, char *str, short x1, short y1, short x2, short y2, char *tip)
//{
//	uiBut *but= ui_def_but(block, BLOCK, 0, str, x1, y1, x2, y2, arg, 0.0, 0.0, 0.0, 0.0, tip);
//
//	/* XXX temp, old menu calls pass on icon arrow, which is now UI_ICON_SUBMENU flag */
//	if(icon!=ICON_RIGHTARROW_THIN) {
//		but.icon= (BIFIconID) icon;
//		but.flag|= UI_ICON_LEFT;
//	}
//	but.flag|= UI_HAS_ICON;
//	but.flag|= UI_ICON_SUBMENU;
//
//	but.block_create_func= func;
//	ui_check_but(but);
//
//	return but;
//}
//
///* Block button containing icon */
//uiBut *uiDefIconBlockBut(uiBlock *block, uiBlockCreateFunc func, void *arg, int retval, int icon, short x1, short y1, short x2, short y2, char *tip)
//{
//	uiBut *but= ui_def_but(block, BLOCK, retval, "", x1, y1, x2, y2, arg, 0.0, 0.0, 0.0, 0.0, tip);
//
//	but.icon= (BIFIconID) icon;
//	but.flag|= UI_HAS_ICON;
//
//	but.flag|= UI_ICON_LEFT;
//	but.flag|= UI_ICON_SUBMENU;
//
//	but.block_create_func= func;
//	ui_check_but(but);
//
//	return but;
//}
//
//void uiDefKeyevtButS(uiBlock *block, int retval, char *str, short x1, short y1, short x2, short y2, short *spoin, char *tip)
//{
//	uiBut *but= ui_def_but(block, KEYEVT|SHO, retval, str, x1, y1, x2, y2, spoin, 0.0, 0.0, 0.0, 0.0, tip);
//	ui_check_but(but);
//}
//
///* short pointers hardcoded */
///* modkeypoin will be set to KM_SHIFT, KM_ALT, KM_CTRL, KM_OSKEY bits */
//uiBut *uiDefHotKeyevtButS(uiBlock *block, int retval, char *str, short x1, short y1, short x2, short y2, short *keypoin, short *modkeypoin, char *tip)
//{
//	uiBut *but= ui_def_but(block, HOTKEYEVT|SHO, retval, str, x1, y1, x2, y2, keypoin, 0.0, 0.0, 0.0, 0.0, tip);
//	but.func_arg3= modkeypoin; /* XXX hrmf, abuse! */
//	ui_check_but(but);
//	return but;
//}


/* arg is pointer to string/name, use uiButSetSearchFunc() below to make this work */
/* here a1 and a2, if set, control thumbnail preview rows/cols */
public static uiBut uiDefSearchBut(uiBlock block, Pointer arg, int retval, BIFIconID icon, int maxlen, int x1, int y1, int x2, int y2, String tip)
{
	uiBut but= ui_def_but(block, SEARCH_MENU, retval, StringUtil.toCString(""), x1, y1, x2, y2, arg, 0.0f, maxlen, 0.0f, 0.0f, StringUtil.toCString(tip));

	but.icon= icon;
	but.flag|= UI_HAS_ICON;

	but.flag|= UI_ICON_LEFT|UI_TEXT_LEFT;

	ui_check_but(but);

	return but;
}

///* arg is user value, searchfunc and handlefunc both get it as arg */
///* if active set, button opens with this item visible and selected */
//public static void uiButSetSearchFunc(uiBut but, uiButSearchFunc sfunc, Object arg, uiButHandleFunc bfunc, Object active)
//{
//	but.search_func= sfunc;
//	but.search_arg= arg;
//	
//	uiButSetFunc(but, bfunc, arg, active);
//	
//	/* search buttons show red-alert if item doesn't exist, not for menus */
//	if(0==(but.block.flag & UI_BLOCK_LOOP)) {
//		/* skip empty buttons, not all buttons need input, we only show invalid */
//		if(but.drawstr[0])
//			ui_but_search_test(but);
//	}
//}

/* Program Init/Exit */

public static void UI_init()
{
	Resources.ui_resources_init();
}

/* after reading userdef file */
public static void UI_init_userdef()
{
	/* fix saved themes */
	Resources.init_userdef_do_versions();
	UIStyle.uiStyleInit();
}

//void UI_exit(void)
//{
//	ui_resources_free();
//}
}
