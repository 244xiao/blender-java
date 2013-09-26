/**
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
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

//#include <stdarg.h>

import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenkernel.Pointer;
import blender.blenkernel.ScreenUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenEdit;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBlockCreateFunc;
import blender.editors.uinterface.UI.uiBlockHandleCreateFunc;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UI.uiMenuCreateFunc;
import blender.editors.uinterface.UI.uiPopupBlockHandle;
import blender.editors.uinterface.UI.uiSafetyRct;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.ScreenTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rctf;
import blender.makesdna.sdna.uiStyle;
import blender.makesdna.sdna.wmWindow;
import blender.windowmanager.WmDraw;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmSubWindow;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmEvent;
import blender.windowmanager.WmWindow;

//#include <stdlib.h>
//#include <string.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_screen_types.h"
//#include "DNA_view2d_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "BLI_arithb.h"
//#include "BLI_blenlib.h"
//#include "BLI_dynstr.h"
//
//#include "BKE_context.h"
//#include "BKE_icons.h"
//#include "BKE_report.h"
//#include "BKE_screen.h"
//#include "BKE_texture.h"
//#include "BKE_utildefines.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//#include "wm_draw.h"
//#include "wm_subwindow.h"
//#include "wm_window.h"
//
//#include "RNA_access.h"
//
//#include "BIF_gl.h"
//
//#include "UI_interface.h"
//#include "UI_interface_icons.h"
//#include "UI_view2d.h"
//
//#include "BLF_api.h"
//
//#include "ED_screen.h"
//
//#include "interface_intern.h"

public class UIRegions {

public static final int MENU_BUTTON_HEIGHT=	20;
public static final int MENU_SEPR_HEIGHT=	6;
public static final int B_NOP=              	-1;
public static final int MENU_SHADOW_SIDE=	8;
public static final int MENU_SHADOW_BOTTOM=	10;
public static final int MENU_TOP=			8;

/*********************** Menu Data Parsing ********************* */

public static class MenuEntry {
	public byte[] str;
	public int retval;
	public int icon;
	public int sepr;

    public MenuEntry() {
    }

    public MenuEntry(MenuEntry me) {
        str = me.str==null?null:new byte[me.str.length];
        if (str!=null) System.arraycopy(me.str, 0, str, 0, me.str.length);
        retval = me.retval;
        icon = me.icon;
        sepr = me.sepr;
    }
};

public static class MenuData {
	public byte[] instr;
	public byte[] title;
	public int titleicon;

	public MenuEntry[] items;
	public int nitems, itemssize;
};

public static MenuData menudata_new(byte[] instr)
{
    MenuData md= new MenuData();

	md.instr= instr;
	md.title= null;
	md.titleicon= 0;
	md.items= null;
	md.nitems= md.itemssize= 0;

	return md;
}

public static void menudata_set_title(MenuData md, byte[] title, int offset, int titleicon)
{
	if (md.title==null)
            md.title= StringUtil.toCString(StringUtil.toJString(title, offset));
	if (md.titleicon==0)
		md.titleicon= titleicon;
}

public static void menudata_add_item(MenuData md, byte[] str, int offset, int retval, int icon, int sepr)
{
	if (md.nitems==md.itemssize) {
		int nsize= md.itemssize!=0?(md.itemssize<<1):1;
		MenuEntry[] oitems= md.items;

        md.items= new MenuEntry[nsize];
		if (oitems!=null) {
            for(int i=0; i<oitems.length; i++) {
                md.items[i] = new MenuEntry(oitems[i]);
            }
//			MEM_freeN(oitems);
		}

		md.itemssize= nsize;
	}

    if (md.items[md.nitems]==null)
        md.items[md.nitems] = new MenuEntry();
    md.items[md.nitems].str= StringUtil.toCString(StringUtil.toJString(str, offset));
	md.items[md.nitems].retval= retval;
	md.items[md.nitems].icon= icon;
	md.items[md.nitems].sepr= sepr;
	md.nitems++;
}

public static void menudata_free(MenuData md) {
//	MEM_freeN(md.instr);
//	if (md.items)
//		MEM_freeN(md.items);
//	MEM_freeN(md);
}

	/**
	 * Parse menu description strings, string is of the
	 * form "[sss%t|]{(sss[%xNN]|), (%l|)}", ssss%t indicates the
	 * menu title, sss or sss%xNN indicates an option,
	 * if %xNN is given then NN is the return value if
	 * that option is selected otherwise the return value
	 * is the index of the option (starting with 1). %l
	 * indicates a seperator.
	 *
	 * @param str String to be parsed.
	 * @retval new menudata structure, free with menudata_free()
	 */
public static MenuData decompose_menu_string(byte[] str)
{
	byte[] instr= StringUtil.BLI_strdup(str,0);
	MenuData md= menudata_new(instr);
//	char *nitem= NULL, *s= instr;
    int nitem=-1;
    int s=0;
	int nicon=0, nretval= 1, nitem_is_title= 0, nitem_is_sepr= 0;

	while (true) {
		char c= (char)(instr[s]&0xFF);

		if (c=='%') {
			if (instr[s+1]=='x') {
				nretval= StringUtil.atoi(instr,s+2);

				instr[s]= '\0';
				s++;
			} else if (instr[s+1]=='t') {
				nitem_is_title= 1;

				instr[s]= '\0';
				s++;
			} else if (instr[s+1]=='l') {
				nitem_is_sepr= 1;
				if(nitem==-1) nitem= -2;
				instr[s]= '\0';
				s++;
			} else if (instr[s+1]=='i') {
				nicon= StringUtil.atoi(instr,s+2);

				instr[s]= '\0';
				s++;
			}
		} else if (c=='|' || c=='\n' || c=='\0') {
			if (nitem!=-1) {
				instr[s]= '\0';

				if (nitem_is_title!=0) {
					menudata_set_title(md, instr,nitem, nicon);
					nitem_is_title= 0;
				}
				else if(nitem_is_sepr!=0) {
					/* prevent separator to get a value */
					menudata_add_item(md, StringUtil.toCString(""),0, -1, nicon, 1);
					nretval= md.nitems+1;
					nitem_is_sepr= 0;
				}
				else {
//					/* prevent separator to get a value */
//                    if(nitem==-2)
//						menudata_add_item(md, StringUtil.toCString("%l"),0, -1, nicon, 0);
//					else
						menudata_add_item(md, instr,nitem, nretval, nicon, 0);
					nretval= md.nitems+1;
				}

				nitem= -1;
				nicon= 0;
			}

			if (c=='\0')
				break;
		} else if (nitem==-1) {
			nitem= s;
        }

		s++;
	}

	return md;
}

public static void ui_set_name_menu(uiBut but, int value)
{
	MenuData md;
	int i;

	md= decompose_menu_string(but.str);
	for (i=0; i<md.nitems; i++) {
		if (md.items[i].retval==value) {
			StringUtil.strcpy(but.drawstr,0, md.items[i].str,0);
        }
    }

//	menudata_free(md);
}

//int ui_step_name_menu(uiBut *but, int step)
//{
//	MenuData *md;
//	int value= ui_get_but_val(but);
//	int i;
//
//	md= decompose_menu_string(but.str);
//	for (i=0; i<md.nitems; i++)
//		if (md.items[i].retval==value)
//			break;
//
//	if(step==1) {
//		/* skip separators */
//		for(; i<md.nitems-1; i++) {
//			if(md.items[i+1].retval != -1) {
//				value= md.items[i+1].retval;
//				break;
//			}
//		}
//	}
//	else {
//		if(i>0) {
//			/* skip separators */
//			for(; i>0; i--) {
//				if(md.items[i-1].retval != -1) {
//					value= md.items[i-1].retval;
//					break;
//				}
//			}
//		}
//	}
//
//	menudata_free(md);
//
//	return value;
//}


/******************** Creating Temporary regions ******************/

public static ARegion ui_add_temporary_region(bScreen sc)
{
	ARegion ar;

	ar= new ARegion();
	ListBaseUtil.BLI_addtail(sc.regionbase, ar);

	ar.regiontype= ScreenTypes.RGN_TYPE_TEMPORARY;
	ar.alignment= ScreenTypes.RGN_ALIGN_FLOAT;

	return ar;
}

public static void ui_remove_temporary_region(bContext C, bScreen sc, ARegion ar)
{
	if(bContext.CTX_wm_window(C)!=null)
		WmDraw.wm_draw_region_clear(bContext.CTX_wm_window(C), ar);

	ScreenEdit.ED_region_exit(C, ar);
	ScreenUtil.BKE_area_region_free(null, ar);		/* NULL: no spacetype */
	ListBaseUtil.BLI_freelinkN(sc.regionbase, ar);
}

///************************* Creating Tooltips **********************/
//
//#define MAX_TOOLTIP_LINES 8
//
//typedef struct uiTooltipData {
//	rcti bbox;
//	uiFontStyle fstyle;
//	char lines[MAX_TOOLTIP_LINES][512];
//	int linedark[MAX_TOOLTIP_LINES];
//	int totline;
//	int toth, spaceh, lineh;
//} uiTooltipData;
//
//static void ui_tooltip_region_draw(const bContext *C, ARegion *ar)
//{
//	uiTooltipData *data= ar.regiondata;
//	rcti bbox= data.bbox;
//	int a;
//
//	ui_draw_menu_back(U.uistyles.first, NULL, &data.bbox);
//
//	/* draw text */
//	uiStyleFontSet(&data.fstyle);
//
//	bbox.ymax= bbox.ymax - 0.5f*((bbox.ymax - bbox.ymin) - data.toth);
//	bbox.ymin= bbox.ymax - data.lineh;
//
//	for(a=0; a<data.totline; a++) {
//		if(!data.linedark[a]) glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//		else glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
//
//		uiStyleFontDraw(&data.fstyle, &bbox, data.lines[a]);
//		bbox.ymin -= data.lineh + data.spaceh;
//		bbox.ymax -= data.lineh + data.spaceh;
//	}
//}
//
//static void ui_tooltip_region_free(ARegion *ar)
//{
//	uiTooltipData *data;
//
//	data= ar.regiondata;
//	MEM_freeN(data);
//	ar.regiondata= NULL;
//}
//
//ARegion *ui_tooltip_create(bContext *C, ARegion *butregion, uiBut *but)
//{
//	uiStyle *style= U.uistyles.first;	// XXX pass on as arg
//	static ARegionType type;
//	ARegion *ar;
//	uiTooltipData *data;
//	IDProperty *prop;
//	char buf[512];
//	float fonth, fontw, aspect= but.block.aspect;
//	float x1f, x2f, y1f, y2f;
//	int x1, x2, y1, y2, winx, winy, ofsx, ofsy, w, h, a;
//
//	/* create tooltip data */
//	data= MEM_callocN(sizeof(uiTooltipData), "uiTooltipData");
//
//	if(but.tip && strlen(but.tip)) {
//		BLI_strncpy(data.lines[data.totline], but.tip, sizeof(data.lines[0]));
//		data.totline++;
//	}
//
//	if(but.optype && !(but.block.flag & UI_BLOCK_LOOP)) {
//		/* operator keymap (not menus, they already have it) */
//		prop= (but.opptr)? but.opptr.data: NULL;
//
//		if(WM_key_event_operator_string(C, but.optype.idname, but.opcontext, prop, buf, sizeof(buf))) {
//			BLI_snprintf(data.lines[data.totline], sizeof(data.lines[0]), "Shortcut: %s", buf);
//			data.linedark[data.totline]= 1;
//			data.totline++;
//		}
//	}
//
//	if(ELEM3(but.type, TEX, IDPOIN, SEARCH_MENU)) {
//		/* full string */
//		ui_get_but_string(but, buf, sizeof(buf));
//		if(buf[0]) {
//			BLI_snprintf(data.lines[data.totline], sizeof(data.lines[0]), "Value: %s", buf);
//			data.linedark[data.totline]= 1;
//			data.totline++;
//		}
//	}
//
//	if(but.rnaprop) {
//		if(but.flag & UI_BUT_DRIVEN) {
//			if(ui_but_anim_expression_get(but, buf, sizeof(buf))) {
//				/* expression */
//				BLI_snprintf(data.lines[data.totline], sizeof(data.lines[0]), "Expression: %s", buf);
//				data.linedark[data.totline]= 1;
//				data.totline++;
//			}
//		}
//
//		/* rna info */
//		BLI_snprintf(data.lines[data.totline], sizeof(data.lines[0]), "Python: %s.%s", RNA_struct_identifier(but.rnapoin.type), RNA_property_identifier(but.rnaprop));
//		data.linedark[data.totline]= 1;
//		data.totline++;
//	}
//
//	if(data.totline == 0) {
//		MEM_freeN(data);
//		return NULL;
//	}
//
//	/* create area region */
//	ar= ui_add_temporary_region(CTX_wm_screen(C));
//
//	memset(&type, 0, sizeof(ARegionType));
//	type.draw= ui_tooltip_region_draw;
//	type.free= ui_tooltip_region_free;
//	ar.type= &type;
//
//	/* set font, get bb */
//	data.fstyle= style.widget; /* copy struct */
//	data.fstyle.align= UI_STYLE_TEXT_CENTER;
//	ui_fontscale(&data.fstyle.points, aspect);
//	uiStyleFontSet(&data.fstyle);
//
//	h= BLF_height(data.lines[0]);
//
//	for(a=0, fontw=0, fonth=0; a<data.totline; a++) {
//		w= BLF_width(data.lines[a]);
//		fontw= MAX2(fontw, w);
//		fonth += (a == 0)? h: h+5;
//	}
//
//	fontw *= aspect;
//	fonth *= aspect;
//
//	ar.regiondata= data;
//
//	data.toth= fonth;
//	data.lineh= h*aspect;
//	data.spaceh= 5*aspect;
//
//	/* compute position */
//	ofsx= (but.block.panel)? but.block.panel.ofsx: 0;
//	ofsy= (but.block.panel)? but.block.panel.ofsy: 0;
//
//	x1f= (but.x1+but.x2)/2.0f + ofsx - 16.0f*aspect;
//	x2f= x1f + fontw + 16.0f*aspect;
//	y2f= but.y1 + ofsy - 15.0f*aspect;
//	y1f= y2f - fonth - 10.0f*aspect;
//
//	/* copy to int, gets projected if possible too */
//	x1= x1f; y1= y1f; x2= x2f; y2= y2f;
//
//	if(butregion) {
//		/* XXX temp, region v2ds can be empty still */
//		if(butregion.v2d.cur.xmin != butregion.v2d.cur.xmax) {
//			UI_view2d_to_region_no_clip(&butregion.v2d, x1f, y1f, &x1, &y1);
//			UI_view2d_to_region_no_clip(&butregion.v2d, x2f, y2f, &x2, &y2);
//		}
//
//		x1 += butregion.winrct.xmin;
//		x2 += butregion.winrct.xmin;
//		y1 += butregion.winrct.ymin;
//		y2 += butregion.winrct.ymin;
//	}
//
//	wm_window_get_size(CTX_wm_window(C), &winx, &winy);
//
//	if(x2 > winx) {
//		/* super size */
//		if(x2 > winx + x1) {
//			x2= winx;
//			x1= 0;
//		}
//		else {
//			x1 -= x2-winx;
//			x2= winx;
//		}
//	}
//	if(y1 < 0) {
//		y1 += 56*aspect;
//		y2 += 56*aspect;
//	}
//
//	/* widget rect, in region coords */
//	data.bbox.xmin= MENU_SHADOW_SIDE;
//	data.bbox.xmax= x2-x1 + MENU_SHADOW_SIDE;
//	data.bbox.ymin= MENU_SHADOW_BOTTOM;
//	data.bbox.ymax= y2-y1 + MENU_SHADOW_BOTTOM;
//
//	/* region bigger for shadow */
//	ar.winrct.xmin= x1 - MENU_SHADOW_SIDE;
//	ar.winrct.xmax= x2 + MENU_SHADOW_SIDE;
//	ar.winrct.ymin= y1 - MENU_SHADOW_BOTTOM;
//	ar.winrct.ymax= y2 + MENU_TOP;
//
//	/* adds subwindow */
//	ED_region_init(C, ar);
//
//	/* notify change and redraw */
//	ED_region_tag_redraw(ar);
//
//	return ar;
//}
//
//void ui_tooltip_free(bContext *C, ARegion *ar)
//{
//	ui_remove_temporary_region(C, CTX_wm_screen(C), ar);
//}
//
//
///************************* Creating Search Box **********************/
//
//struct uiSearchItems {
//	int maxitem, totitem, maxstrlen;
//
//	int offset, offset_i; /* offset for inserting in array */
//	int more;  /* flag indicating there are more items */
//
//	char **names;
//	void **pointers;
//	int *icons;
//
//	AutoComplete *autocpl;
//	void *active;
//};
//
//typedef struct uiSearchboxData {
//	rcti bbox;
//	uiFontStyle fstyle;
//	uiSearchItems items;
//	int active;		/* index in items array */
//	int noback;		/* when menu opened with enough space for this */
//} uiSearchboxData;

public static final int SEARCH_ITEMS=	10;

///* exported for use by search callbacks */
///* returns zero if nothing to add */
//int uiSearchItemAdd(uiSearchItems *items, const char *name, void *poin, int iconid)
//{
//	/* hijack for autocomplete */
//	if(items.autocpl) {
//		autocomplete_do_name(items.autocpl, name);
//		return 1;
//	}
//
//	/* hijack for finding active item */
//	if(items.active) {
//		if(poin==items.active)
//			items.offset_i= items.totitem;
//		items.totitem++;
//		return 1;
//	}
//
//	if(items.totitem>=items.maxitem) {
//		items.more= 1;
//		return 0;
//	}
//
//	/* skip first items in list */
//	if(items.offset_i > 0) {
//		items.offset_i--;
//		return 1;
//	}
//
//	BLI_strncpy(items.names[items.totitem], name, items.maxstrlen);
//	items.pointers[items.totitem]= poin;
//	items.icons[items.totitem]= iconid;
//
//	items.totitem++;
//
//	return 1;
//}

public static int uiSearchBoxhHeight()
{
	return SEARCH_ITEMS*MENU_BUTTON_HEIGHT + 2*MENU_TOP;
}

///* ar is the search box itself */
//static void ui_searchbox_select(bContext *C, ARegion *ar, uiBut *but, int step)
//{
//	uiSearchboxData *data= ar.regiondata;
//
//	/* apply step */
//	data.active+= step;
//
//	if(data.items.totitem==0)
//		data.active= 0;
//	else if(data.active > data.items.totitem) {
//		if(data.items.more) {
//			data.items.offset++;
//			data.active= data.items.totitem;
//			ui_searchbox_update(C, ar, but, 0);
//		}
//		else
//			data.active= data.items.totitem;
//	}
//	else if(data.active < 1) {
//		if(data.items.offset) {
//			data.items.offset--;
//			data.active= 1;
//			ui_searchbox_update(C, ar, but, 0);
//		}
//		else if(data.active < 0)
//			data.active= 0;
//	}
//
//	ED_region_tag_redraw(ar);
//}
//
//static void ui_searchbox_butrect(rcti *rect, uiSearchboxData *data, int itemnr)
//{
//	int buth= (data.bbox.ymax-data.bbox.ymin - 2*MENU_TOP)/SEARCH_ITEMS;
//
//	*rect= data.bbox;
//	rect.xmin= data.bbox.xmin + 3.0f;
//	rect.xmax= data.bbox.xmax - 3.0f;
//
//	rect.ymax= data.bbox.ymax - MENU_TOP - itemnr*buth;
//	rect.ymin= rect.ymax - buth;
//
//}

/* x and y in screencoords */
public static int ui_searchbox_inside(ARegion ar, int x, int y)
{
//	uiSearchboxData data= ar.regiondata;
//
//	return(BLI_in_rcti(data.bbox, x-ar.winrct.xmin, y-ar.winrct.ymin));
	
	return 0;
}

///* string validated to be of correct length (but.hardmax) */
//void ui_searchbox_apply(uiBut *but, ARegion *ar)
//{
//	uiSearchboxData *data= ar.regiondata;
//
//	but.func_arg2= NULL;
//
//	if(data.active) {
//		char *name= data.items.names[data.active-1];
//		char *cpoin= strchr(name, '|');
//
//		if(cpoin) cpoin[0]= 0;
//		BLI_strncpy(but.editstr, name, data.items.maxstrlen);
//		if(cpoin) cpoin[0]= '|';
//
//		but.func_arg2= data.items.pointers[data.active-1];
//	}
//}

public static void ui_searchbox_event(bContext C, ARegion ar, uiBut but, wmEvent event)
{
//	uiSearchboxData *data= ar.regiondata;
//
//	switch(event.type) {
//		case WHEELUPMOUSE:
//		case UPARROWKEY:
//			ui_searchbox_select(C, ar, but, -1);
//			break;
//		case WHEELDOWNMOUSE:
//		case DOWNARROWKEY:
//			ui_searchbox_select(C, ar, but, 1);
//			break;
//		case MOUSEMOVE:
//			if(BLI_in_rcti(&ar.winrct, event.x, event.y)) {
//				rcti rect;
//				int a;
//
//				for(a=0; a<data.items.totitem; a++) {
//					ui_searchbox_butrect(&rect, data, a);
//					if(BLI_in_rcti(&rect, event.x - ar.winrct.xmin, event.y - ar.winrct.ymin)) {
//						if( data.active!= a+1) {
//							data.active= a+1;
//							ui_searchbox_select(C, ar, but, 0);
//							break;
//						}
//					}
//				}
//			}
//			break;
//	}
}

/* ar is the search box itself */
public static void ui_searchbox_update(bContext C, ARegion ar, uiBut but, int reset)
{
//	uiSearchboxData *data= ar.regiondata;
//
//	/* reset vars */
//	data.items.totitem= 0;
//	data.items.more= 0;
//	if(reset==0) {
//		data.items.offset_i= data.items.offset;
//	}
//	else {
//		data.items.offset_i= data.items.offset= 0;
//		data.active= 0;
//
//		/* handle active */
//		if(but.search_func && but.func_arg2) {
//			data.items.active= but.func_arg2;
//			but.search_func(C, but.search_arg, but.editstr, &data.items);
//			data.items.active= NULL;
//
//			/* found active item, calculate real offset by centering it */
//			if(data.items.totitem) {
//				/* first case, begin of list */
//				if(data.items.offset_i < data.items.maxitem) {
//					data.active= data.items.offset_i+1;
//					data.items.offset_i= 0;
//				}
//				else {
//					/* second case, end of list */
//					if(data.items.totitem - data.items.offset_i <= data.items.maxitem) {
//						data.active= 1 + data.items.offset_i - data.items.totitem + data.items.maxitem;
//						data.items.offset_i= data.items.totitem - data.items.maxitem;
//					}
//					else {
//						/* center active item */
//						data.items.offset_i -= data.items.maxitem/2;
//						data.active= 1 + data.items.maxitem/2;
//					}
//				}
//			}
//			data.items.offset= data.items.offset_i;
//			data.items.totitem= 0;
//		}
//	}
//
//	/* callback */
//	if(but.search_func)
//		but.search_func(C, but.search_arg, but.editstr, &data.items);
//
//	/* handle case where editstr is equal to one of items */
//	if(reset && data.active==0) {
//		int a;
//
//		for(a=0; a<data.items.totitem; a++) {
//			char *cpoin= strchr(data.items.names[a], '|');
//
//			if(cpoin) cpoin[0]= 0;
//			if(0==strcmp(but.editstr, data.items.names[a]))
//				data.active= a+1;
//			if(cpoin) cpoin[0]= '|';
//		}
//		if(data.items.totitem==1)
//			data.active= 1;
//	}
//
//	/* validate selected item */
//	ui_searchbox_select(C, ar, but, 0);
//
//	ED_region_tag_redraw(ar);
}

//void ui_searchbox_autocomplete(bContext *C, ARegion *ar, uiBut *but, char *str)
//{
//	uiSearchboxData *data= ar.regiondata;
//
//	data.items.autocpl= autocomplete_begin(str, ui_get_but_string_max_length(but));
//
//	but.search_func(C, but.search_arg, but.editstr, &data.items);
//
//	autocomplete_end(data.items.autocpl, str);
//	data.items.autocpl= NULL;
//}
//
//static void ui_searchbox_region_draw(const bContext *C, ARegion *ar)
//{
//	uiSearchboxData *data= ar.regiondata;
//
//	/* pixel space */
//	wmOrtho2(-0.01f, ar.winx-0.01f, -0.01f, ar.winy-0.01f);
//
//	if(!data.noback)
//		ui_draw_search_back(U.uistyles.first, NULL, &data.bbox);
//
//	/* draw text */
//	if(data.items.totitem) {
//		rcti rect;
//		int a;
//
//		/* draw items */
//		for(a=0; a<data.items.totitem; a++) {
//			ui_searchbox_butrect(&rect, data, a);
//
//			/* widget itself */
//			ui_draw_menu_item(&data.fstyle, &rect, data.items.names[a], data.items.icons[a], (a+1)==data.active?UI_ACTIVE:0);
//
//		}
//		/* indicate more */
//		if(data.items.more) {
//			ui_searchbox_butrect(&rect, data, data.items.maxitem-1);
//			glEnable(GL_BLEND);
//			UI_icon_draw((rect.xmax-rect.xmin)/2, rect.ymin-9, ICON_TRIA_DOWN);
//			glDisable(GL_BLEND);
//		}
//		if(data.items.offset) {
//			ui_searchbox_butrect(&rect, data, 0);
//			glEnable(GL_BLEND);
//			UI_icon_draw((rect.xmax-rect.xmin)/2, rect.ymax-7, ICON_TRIA_UP);
//			glDisable(GL_BLEND);
//		}
//	}
//}
//
//static void ui_searchbox_region_free(ARegion *ar)
//{
//	uiSearchboxData *data= ar.regiondata;
//	int a;
//
//	/* free search data */
//	for(a=0; a<SEARCH_ITEMS; a++)
//		MEM_freeN(data.items.names[a]);
//	MEM_freeN(data.items.names);
//	MEM_freeN(data.items.pointers);
//	MEM_freeN(data.items.icons);
//
//	MEM_freeN(data);
//	ar.regiondata= NULL;
//}
//
//ARegion *ui_searchbox_create(bContext *C, ARegion *butregion, uiBut *but)
//{
//	uiStyle *style= U.uistyles.first;	// XXX pass on as arg
//	static ARegionType type;
//	ARegion *ar;
//	uiSearchboxData *data;
//	float aspect= but.block.aspect;
//	float x1f, x2f, y1f, y2f;
//	int x1, x2, y1, y2, winx, winy, ofsx, ofsy;
//
//	/* create area region */
//	ar= ui_add_temporary_region(CTX_wm_screen(C));
//
//	memset(&type, 0, sizeof(ARegionType));
//	type.draw= ui_searchbox_region_draw;
//	type.free= ui_searchbox_region_free;
//	ar.type= &type;
//
//	/* create searchbox data */
//	data= MEM_callocN(sizeof(uiSearchboxData), "uiSearchboxData");
//
//	/* set font, get bb */
//	data.fstyle= style.widget; /* copy struct */
//	data.fstyle.align= UI_STYLE_TEXT_CENTER;
//	ui_fontscale(&data.fstyle.points, aspect);
//	uiStyleFontSet(&data.fstyle);
//
//	ar.regiondata= data;
//
//	/* special case, hardcoded feature, not draw backdrop when called from menus,
//	   assume for design that popup already added it */
//	if(but.block.flag & UI_BLOCK_LOOP)
//		data.noback= 1;
//
//	/* compute position */
//
//	if(but.block.flag & UI_BLOCK_LOOP) {
//		/* this case is search menu inside other menu */
//		/* we copy region size */
//
//		ar.winrct= butregion.winrct;
//
//		/* widget rect, in region coords */
//		data.bbox.xmin= MENU_SHADOW_SIDE;
//		data.bbox.xmax= (ar.winrct.xmax-ar.winrct.xmin) - MENU_SHADOW_SIDE;
//		data.bbox.ymin= MENU_SHADOW_BOTTOM;
//		data.bbox.ymax= (ar.winrct.ymax-ar.winrct.ymin) - MENU_SHADOW_BOTTOM;
//
//		/* check if button is lower half */
//		if( but.y2 < (but.block.minx+but.block.maxx)/2 ) {
//			data.bbox.ymin += (but.y2-but.y1);
//		}
//		else {
//			data.bbox.ymax -= (but.y2-but.y1);
//		}
//	}
//	else {
//		x1f= but.x1 - 5;	/* align text with button */
//		x2f= but.x2 + 5;	/* symmetrical */
//		y2f= but.y1;
//		y1f= y2f - uiSearchBoxhHeight();
//
//		ofsx= (but.block.panel)? but.block.panel.ofsx: 0;
//		ofsy= (but.block.panel)? but.block.panel.ofsy: 0;
//
//		x1f += ofsx;
//		x2f += ofsx;
//		y1f += ofsy;
//		y2f += ofsy;
//
//		/* minimal width */
//		if(x2f - x1f < 150) x2f= x1f+150; // XXX arbitrary
//
//		/* copy to int, gets projected if possible too */
//		x1= x1f; y1= y1f; x2= x2f; y2= y2f;
//
//		if(butregion) {
//			if(butregion.v2d.cur.xmin != butregion.v2d.cur.xmax) {
//				UI_view2d_to_region_no_clip(&butregion.v2d, x1f, y1f, &x1, &y1);
//				UI_view2d_to_region_no_clip(&butregion.v2d, x2f, y2f, &x2, &y2);
//			}
//
//			x1 += butregion.winrct.xmin;
//			x2 += butregion.winrct.xmin;
//			y1 += butregion.winrct.ymin;
//			y2 += butregion.winrct.ymin;
//		}
//
//		wm_window_get_size(CTX_wm_window(C), &winx, &winy);
//
//		if(x2 > winx) {
//			/* super size */
//			if(x2 > winx + x1) {
//				x2= winx;
//				x1= 0;
//			}
//			else {
//				x1 -= x2-winx;
//				x2= winx;
//			}
//		}
//		if(y1 < 0) {
//			y1 += 36;
//			y2 += 36;
//		}
//
//		/* widget rect, in region coords */
//		data.bbox.xmin= MENU_SHADOW_SIDE;
//		data.bbox.xmax= x2-x1 + MENU_SHADOW_SIDE;
//		data.bbox.ymin= MENU_SHADOW_BOTTOM;
//		data.bbox.ymax= y2-y1 + MENU_SHADOW_BOTTOM;
//
//		/* region bigger for shadow */
//		ar.winrct.xmin= x1 - MENU_SHADOW_SIDE;
//		ar.winrct.xmax= x2 + MENU_SHADOW_SIDE;
//		ar.winrct.ymin= y1 - MENU_SHADOW_BOTTOM;
//		ar.winrct.ymax= y2;
//	}
//
//	/* adds subwindow */
//	ED_region_init(C, ar);
//
//	/* notify change and redraw */
//	ED_region_tag_redraw(ar);
//
//	/* prepare search data */
//	data.items.maxitem= SEARCH_ITEMS;
//	data.items.maxstrlen= but.hardmax;
//	data.items.totitem= 0;
//	data.items.names= MEM_callocN(SEARCH_ITEMS*sizeof(void *), "search names");
//	data.items.pointers= MEM_callocN(SEARCH_ITEMS*sizeof(void *), "search pointers");
//	data.items.icons= MEM_callocN(SEARCH_ITEMS*sizeof(int), "search icons");
//	for(x1=0; x1<SEARCH_ITEMS; x1++)
//		data.items.names[x1]= MEM_callocN(but.hardmax+1, "search pointers");
//
//	return ar;
//}
//
//void ui_searchbox_free(bContext *C, ARegion *ar)
//{
//	ui_remove_temporary_region(C, CTX_wm_screen(C), ar);
//}


/************************* Creating Menu Blocks **********************/

/* position block relative to but, result is in window space */
static void ui_block_position(wmWindow window, ARegion butregion, uiBut but, uiBlock block)
{
	uiBut bt;
	uiSafetyRct saferct;
	rctf butrct = new rctf();
	float aspect;
	int xsize, ysize, xof=0, yof=0, center;
	short dir1= 0, dir2=0;

	/* transform to window coordinates, using the source button region/block */
	butrct.xmin= but.x1; butrct.xmax= but.x2;
	butrct.ymin= but.y1; butrct.ymax= but.y2;

    float[] xmin={butrct.xmin}, ymin={butrct.ymin}, xmax={butrct.xmax}, ymax={butrct.ymax};
	UI.ui_block_to_window_fl(butregion, but.block, xmin, ymin);
	UI.ui_block_to_window_fl(butregion, but.block, xmax, ymax);
    butrct.xmin=xmin[0]; butrct.ymin=ymin[0]; butrct.xmax=xmax[0]; butrct.ymax=ymax[0];

	/* calc block rect */
	if(block.minx == 0.0f && block.maxx == 0.0f) {
		if(block.buttons.first!=null) {
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
		}
		else {
			/* we're nice and allow empty blocks too */
			block.minx= block.miny= 0;
			block.maxx= block.maxy= 20;
		}
	}

	aspect= (float)(block.maxx - block.minx + 4);
    float[] minx={block.minx}, miny={block.miny}, maxx={block.maxx}, maxy={block.maxy};
	UI.ui_block_to_window_fl(butregion, but.block, minx, miny);
	UI.ui_block_to_window_fl(butregion, but.block, maxx, maxy);
    block.minx=minx[0]; block.miny=miny[0]; block.maxx=maxx[0]; block.maxy=maxy[0];

	//block.minx-= 2.0; block.miny-= 2.0;
	//block.maxx+= 2.0; block.maxy+= 2.0;

	xsize= (int)(block.maxx - block.minx+4); // 4 for shadow
	ysize= (int)(block.maxy - block.miny+4);
	aspect/= (float)xsize;

	if(but!=null) {
		int left=0, right=0, top=0, down=0;
		int[] winx={0}, winy={0};
		int offscreen;

		WmWindow.wm_window_get_size(window, winx, winy);

		if((block.direction & UI.UI_CENTER)!=0) center= ysize/2;
		else center= 0;

		if( butrct.xmin-xsize > 0.0) left= 1;
		if( butrct.xmax+xsize < winx[0]) right= 1;
		if( butrct.ymin-ysize+center > 0.0) down= 1;
		if( butrct.ymax+ysize-center < winy[0]) top= 1;

		dir1= (short)(block.direction & UI.UI_DIRECTION);

		/* secundary directions */
		if((dir1 & (UI.UI_TOP|UI.UI_DOWN))!=0) {
			if((dir1 & UI.UI_LEFT)!=0) dir2= UI.UI_LEFT;
			else if((dir1 & UI.UI_RIGHT)!=0) dir2= UI.UI_RIGHT;
			dir1 &= (UI.UI_TOP|UI.UI_DOWN);
		}

		if(dir2==0) if(dir1==UI.UI_LEFT || dir1==UI.UI_RIGHT) dir2= UI.UI_DOWN;
		if(dir2==0) if(dir1==UI.UI_TOP || dir1==UI.UI_DOWN) dir2= UI.UI_LEFT;

		/* no space at all? dont change */
		if(left!=0 || right!=0) {
			if(dir1==UI.UI_LEFT && left==0) dir1= UI.UI_RIGHT;
			if(dir1==UI.UI_RIGHT && right==0) dir1= UI.UI_LEFT;
			/* this is aligning, not append! */
			if(dir2==UI.UI_LEFT && right==0) dir2= UI.UI_RIGHT;
			if(dir2==UI.UI_RIGHT && left==0) dir2= UI.UI_LEFT;
		}
		if(down!=0 || top!=0) {
			if(dir1==UI.UI_TOP && top==0) dir1= UI.UI_DOWN;
			if(dir1==UI.UI_DOWN && down==0) dir1= UI.UI_TOP;
			if(dir2==UI.UI_TOP && top==0) dir2= UI.UI_DOWN;
			if(dir2==UI.UI_DOWN && down==0) dir2= UI.UI_TOP;
		}

		if(dir1==UI.UI_LEFT) {
			xof= (int)(butrct.xmin - block.maxx);
			if(dir2==UI.UI_TOP) yof= (int)(butrct.ymin - block.miny-center);
			else yof= (int)(butrct.ymax - block.maxy+center);
		}
		else if(dir1==UI.UI_RIGHT) {
			xof= (int)(butrct.xmax - block.minx);
			if(dir2==UI.UI_TOP) yof= (int)(butrct.ymin - block.miny-center);
			else yof= (int)(butrct.ymax - block.maxy+center);
		}
		else if(dir1==UI.UI_TOP) {
			yof= (int)(butrct.ymax - block.miny);
			if(dir2==UI.UI_RIGHT) xof= (int)(butrct.xmax - block.maxx);
			else xof= (int)(butrct.xmin - block.minx);
			// changed direction?
			if((dir1 & block.direction)==0) {
				if((block.direction & UI.UI_SHIFT_FLIPPED)!=0)
					xof+= dir2==UI.UI_LEFT?25:-25;
				UI.uiBlockFlipOrder(block);
			}
		}
		else if(dir1==UI.UI_DOWN) {
			yof= (int)(butrct.ymin - block.maxy);
			if(dir2==UI.UI_RIGHT) xof= (int)(butrct.xmax - block.maxx);
			else xof= (int)(butrct.xmin - block.minx);
			// changed direction?
			if((dir1 & block.direction)==0) {
				if((block.direction & UI.UI_SHIFT_FLIPPED)!=0)
					xof+= dir2==UI.UI_LEFT?25:-25;
				UI.uiBlockFlipOrder(block);
			}
		}

		/* and now we handle the exception; no space below or to top */
		if(top==0 && down==0) {
			if(dir1==UI.UI_LEFT || dir1==UI.UI_RIGHT) {
				// align with bottom of screen
				yof= ysize;
			}
		}

		/* or no space left or right */
		if(left==0 && right==0) {
			if(dir1==UI.UI_TOP || dir1==UI.UI_DOWN) {
				// align with left size of screen
				xof= (int)(-block.minx+5);
			}
		}

		// apply requested offset in the block
		xof += block.xofs/block.aspect;
		yof += block.yofs/block.aspect;
		
		/* clamp to window bounds, could be made into an option if its ever annoying */
		if(     (offscreen= (int)(block.miny+yof)) < 0)      yof -= offscreen; /* bottom */
		else if((offscreen= (int)((block.maxy+yof)-winy[0])) > 0) yof -= offscreen; /* top */
		if(     (offscreen= (int)(block.minx+xof)) < 0)      xof -= offscreen; /* left */
		else if((offscreen= (int)((block.maxx+xof)-winx[0])) > 0) xof -= offscreen; /* right */
	}

	/* apply */

	for(bt= block.buttons.first; bt!=null; bt= bt.next) {
        float[] x1={bt.x1}, y1={bt.y1}, x2={bt.x2}, y2={bt.y2};
		UI.ui_block_to_window_fl(butregion, but.block, x1, y1);
		UI.ui_block_to_window_fl(butregion, but.block, x2, y2);
        bt.x1=x1[0]; bt.y1=y1[0]; bt.x2=x2[0]; bt.y2=y2[0];

		bt.x1 += xof;
		bt.x2 += xof;
		bt.y1 += yof;
		bt.y2 += yof;

		bt.aspect= 1.0f;
		// ui_check_but recalculates drawstring size in pixels
		UI.ui_check_but(bt);
	}

	block.minx += xof;
	block.miny += yof;
	block.maxx += xof;
	block.maxy += yof;

	/* safety calculus */
	if(but!=null) {
		float midx= (butrct.xmin+butrct.xmax)/2.0f;
		float midy= (butrct.ymin+butrct.ymax)/2.0f;

		/* when you are outside parent button, safety there should be smaller */

		// parent button to left
		if( midx < block.minx ) block.safety.xmin= block.minx-3;
		else block.safety.xmin= block.minx-40;
		// parent button to right
		if( midx > block.maxx ) block.safety.xmax= block.maxx+3;
		else block.safety.xmax= block.maxx+40;

		// parent button on bottom
		if( midy < block.miny ) block.safety.ymin= block.miny-3;
		else block.safety.ymin= block.miny-40;
		// parent button on top
		if( midy > block.maxy ) block.safety.ymax= block.maxy+3;
		else block.safety.ymax= block.maxy+40;

		// exception for switched pulldowns...
		if(dir1!=0 && (dir1 & block.direction)==0) {
			if(dir2==UI.UI_RIGHT) block.safety.xmax= block.maxx+3;
			if(dir2==UI.UI_LEFT) block.safety.xmin= block.minx-3;
		}
		block.direction= dir1;
	}
	else {
		block.safety.xmin= block.minx-40;
		block.safety.ymin= block.miny-40;
		block.safety.xmax= block.maxx+40;
		block.safety.ymax= block.maxy+40;
	}

	/* keep a list of these, needed for pulldown menus */
	saferct= new uiSafetyRct();
	saferct.parent= butrct;
	saferct.safety= block.safety;
	ListBaseUtil.BLI_freelistN(block.saferct);
	if(but!=null)
		ListBaseUtil.BLI_duplicatelist(block.saferct, but.block.saferct);
	ListBaseUtil.BLI_addhead(block.saferct, saferct);
}

public static ARegionType.Draw ui_block_region_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	uiBlock block;

	for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next)
		UI.uiDrawBlock(gl, C, block);
}};

static ARegionType type = new ARegionType();
public static uiPopupBlockHandle ui_popup_block_create(GL2 gl, bContext C, ARegion butregion, uiBut but, uiBlockCreateFunc create_func, uiBlockHandleCreateFunc handle_create_func, Object arg)
{
	wmWindow window= bContext.CTX_wm_window(C);
	ARegion ar;
	uiBlock block;
	uiBut bt;
	uiPopupBlockHandle handle;
	uiSafetyRct saferct = new uiSafetyRct();

	/* create handle */
	handle= new uiPopupBlockHandle();

	/* store context for operator */
	handle.ctx_area= bContext.CTX_wm_area(C);
	handle.ctx_region= bContext.CTX_wm_region(C);

	/* create area region */
	ar= ui_add_temporary_region(bContext.CTX_wm_screen(C));
	handle.region= ar;

//	memset(&type, 0, sizeof(ARegionType));
    type.clear();
	type.draw= ui_block_region_draw;
	ar.type= type;

	UIHandlers.UI_add_region_handlers(ar.handlers);

	/* create ui block */
	if(create_func!=null)
		block= create_func.run(C, handle.region, arg);
	else
		block= handle_create_func.run(C, handle, arg);

	if(block.handle!=null) {
//		memcpy(block.handle, handle, sizeof(uiPopupBlockHandle));
        block.handle.set(handle);
//		MEM_freeN(handle);
		handle= block.handle;
	}
	else
		block.handle= handle;

	ar.regiondata= handle;

	if(block.endblock==0)
		UI.uiEndBlock(C, block);

	/* if this is being created from a button */
	if(but!=null) {
		if(but.type==UI.BLOCK || but.type==UI.PULLDOWN)
			block.xofs = -2;	/* for proper alignment */

		/* only used for automatic toolbox, so can set the shift flag */
		if((but.flag & UI.UI_MAKE_TOP)!=0) {
			block.direction= UI.UI_TOP|UI.UI_SHIFT_FLIPPED;
			UI.uiBlockFlipOrder(block);
		}
		if((but.flag & UI.UI_MAKE_DOWN)!=0) block.direction= UI.UI_DOWN|UI.UI_SHIFT_FLIPPED;
		if((but.flag & UI.UI_MAKE_LEFT)!=0) block.direction |= UI.UI_LEFT;
		if((but.flag & UI.UI_MAKE_RIGHT)!=0) block.direction |= UI.UI_RIGHT;

		ui_block_position(window, butregion, but, block);
	}
	else {
		/* keep a list of these, needed for pulldown menus */
		saferct= new uiSafetyRct();
		saferct.safety= block.safety;
		ListBaseUtil.BLI_addhead(block.saferct, saferct);
		block.flag |= UI.UI_BLOCK_POPUP;
	}

	/* the block and buttons were positioned in window space as in 2.4x, now
	 * these menu blocks are regions so we bring it back to region space.
	 * additionally we add some padding for the menu shadow or rounded menus */
	ar.winrct.xmin= (int)(block.minx - MENU_SHADOW_SIDE);
	ar.winrct.xmax= (int)(block.maxx + MENU_SHADOW_SIDE);
	ar.winrct.ymin= (int)(block.miny - MENU_SHADOW_BOTTOM);
	ar.winrct.ymax= (int)(block.maxy + MENU_TOP);

	block.minx -= ar.winrct.xmin;
	block.maxx -= ar.winrct.xmin;
	block.miny -= ar.winrct.ymin;
	block.maxy -= ar.winrct.ymin;

	for(bt= block.buttons.first; bt!=null; bt= bt.next) {
		bt.x1 -= ar.winrct.xmin;
		bt.x2 -= ar.winrct.xmin;
		bt.y1 -= ar.winrct.ymin;
		bt.y2 -= ar.winrct.ymin;
	}

//	block.flag |= UI.UI_BLOCK_LOOP|UI.UI_BLOCK_MOVEMOUSE_QUIT;
	block.flag |= UI.UI_BLOCK_LOOP;

	/* adds subwindow */
	Area.ED_region_init(gl, C, ar);
//	Area.ED_region_init(C, ar);

	/* get winmat now that we actually have the subwindow */
	WmSubWindow.wmSubWindowSet(gl, window, ar.swinid);

	WmSubWindow.wm_subwindow_getmatrix(window, ar.swinid, block.winmat);

	/* notify change and redraw */
	Area.ED_region_tag_redraw(ar);

	return handle;
}

public static void ui_popup_block_free(bContext C, uiPopupBlockHandle handle)
{
	/* XXX ton added, chrash on load file with popup open... need investigate */
//	if(bContext.CTX_wm_screen(C)!=null)
		ui_remove_temporary_region(C, bContext.CTX_wm_screen(C), handle.region);
//	MEM_freeN(handle);
}

/***************************** Menu Button ***************************/

//public static uiBlockHandleCreateFunc ui_block_func_MENU = new uiBlockHandleCreateFunc() {
//public uiBlock run(bContext C, final uiPopupBlockHandle handle, Object arg_but)
////uiBlock *ui_block_func_MENU(bContext *C, uiPopupBlockHandle *handle, void *arg_but)
//{
//	uiBut but= (uiBut)arg_but;
//	uiBlock block;
//	uiBut bt;
//	MenuData md;
//	ListBase lb = new ListBase();
//	float aspect;
//	int width, height, boxh, columns, rows, startx, starty, x1, y1, xmax, a;
//
//	/* create the block */
//	block= UI.uiBeginBlock(C, handle.region, "menu", UI.UI_EMBOSSP);
//	block.flag= UI.UI_BLOCK_LOOP|UI.UI_BLOCK_REDRAW|UI.UI_BLOCK_NUMSELECT;
//
//	/* compute menu data */
//	md= decompose_menu_string(but.str);
//
//	/* columns and row calculation */
//	columns= (md.nitems+20)/20;
//	if(columns<1)
//		columns= 1;
//	if(columns>8)
//		columns= (md.nitems+25)/25;
//
//	rows= md.nitems/columns;
//	if(rows<1)
//		rows= 1;
//	while(rows*columns<md.nitems)
//		rows++;
//
//	/* prevent scaling up of pupmenu */
//	aspect= but.block.aspect;
//	if(aspect < 1.0f)
//		aspect = 1.0f;
//
//	/* size and location */
//	if(md.title!=null)
//		width= (int)(1.5f*aspect*StringUtil.strlen(md.title,0)+UIStyle.UI_GetStringWidth(md.title,0));
//	else
//		width= 0;
//
//	for(a=0; a<md.nitems; a++) {
//		xmax= (int)(aspect*UIStyle.UI_GetStringWidth(md.items[a].str,0));
//		if(md.items[a].icon!=0)
//			xmax += 20*aspect;
//		if(xmax>width)
//			width= xmax;
//	}
//
//	width+= 10;
//	if(width < (but.x2 - but.x1))
//		width = (int)(but.x2 - but.x1);
//	if(width<50)
//		width=50;
//
//	boxh= MENU_BUTTON_HEIGHT;
//
//	height= rows*boxh;
//	if(md.title!=null)
//		height+= boxh;
//
//	/* here we go! */
//	startx= (int)but.x1;
//	starty= (int)but.y1;
//
//	if(md.title!=null) {
////		uiBut bt2;
//
//		if (md.titleicon!=0) {
//			bt= UI.uiDefIconTextBut(block, UI.LABEL, 0, BIFIconID.values()[md.titleicon], StringUtil.toJString(md.title,0), startx, (short)(starty+rows*boxh), (short)width, (short)boxh, null, 0.0f, 0.0f, 0, 0, "");
//		} else {
//			bt= UI.uiDefBut(block, UI.LABEL, 0, StringUtil.toJString(md.title,0), startx, (short)(starty+rows*boxh), (short)width, (short)boxh, null, 0.0f, 0.0f, 0, 0, "");
//			bt.flag= UI.UI_TEXT_LEFT;
//		}
//	}
//
//	for(a=0; a<md.nitems; a++) {
//
//		x1= startx + width*((int)(md.nitems-a-1)/rows);
//		y1= starty - boxh*(rows - ((md.nitems - a - 1)%rows)) + (rows*boxh);
//
//                Pointer<Float> handle_retvalue = new Pointer<Float>() {
//                    public Float get() {
//                        return handle.retvalue;
//                    }
//                    public void set(Float obj) {
//                        handle.retvalue = obj;
//                    }
//                };
//
//		if (StringUtil.strcmp(md.items[md.nitems-a-1].str,0, StringUtil.toCString("%l"),0)==0) {
//			bt= UI.uiDefBut(block, UI.SEPR, B_NOP, "", x1, y1,(short)(width-(rows>1?1:0)), (short)(boxh-1), null, 0.0f, 0.0f, 0, 0, "");
//		}
//		else if(md.items[md.nitems-a-1].icon!=0) {
//			bt= UI.uiDefIconTextButF(block, UI.BUTM|UI.FLO, B_NOP, BIFIconID.values()[md.items[md.nitems-a-1].icon] ,StringUtil.toJString(md.items[md.nitems-a-1].str,0), x1, y1,(short)(width-(rows>1?1:0)), (short)(boxh-1), handle_retvalue, (float) md.items[md.nitems-a-1].retval, 0.0f, 0, 0, "");
//		}
//		else {
//			bt= UI.uiDefButF(block, UI.BUTM|UI.FLO, B_NOP, StringUtil.toJString(md.items[md.nitems-a-1].str,0), x1, y1,(short)(width-(rows>1?1:0)), (short)(boxh-1), handle_retvalue, (float) md.items[md.nitems-a-1].retval, 0.0f, 0, 0, "");
//		}
//	}
//
//	menudata_free(md);
//
//	/* the code up here has flipped locations, because of change of preferred order */
//	/* thats why we have to switch list order too, to make arrowkeys work */
//
//	lb.first= lb.last= null;
//	bt= block.buttons.first;
//	while(bt!=null) {
//		uiBut next= bt.next;
//		ListBaseUtil.BLI_remlink(block.buttons, bt);
//		ListBaseUtil.BLI_addhead(lb, bt);
//		bt= next;
//	}
//	block.buttons= lb;
//
//	block.direction= UI.UI_TOP;
//	UI.uiEndBlock(C, block);
//
//	return block;
//}};

public static uiMenuCreateFunc ui_block_func_MENUSTR = new uiMenuCreateFunc() {
public void run(bContext C, uiLayout layout, Object arg_str)
{
	uiBlock block= UILayout.uiLayoutGetBlock(layout);
	final uiPopupBlockHandle handle= block.handle;
	uiLayout split, column=null;
	uiBut bt;
	MenuData md;
	MenuEntry entry;
	byte[] instr= (byte[])arg_str;
	int columns, rows, a, b;

	UI.uiBlockSetFlag(block, UI.UI_BLOCK_MOVEMOUSE_QUIT);
	
	/* compute menu data */
	md= decompose_menu_string(instr);

	/* columns and row estimation */
	columns= (md.nitems+20)/20;
	if(columns<1)
		columns= 1;
	if(columns>8)
		columns= (md.nitems+25)/25;
	
	rows= md.nitems/columns;
	if(rows<1)
		rows= 1;
	while(rows*columns<md.nitems)
		rows++;

	/* create title */
	if(md.title!=null) {
		if(md.titleicon!=0) {
			UILayout.uiItemL(layout, StringUtil.toJString(md.title,0), md.titleicon);
		}
		else {
			UILayout.uiItemL(layout, StringUtil.toJString(md.title,0), UI.ICON_NULL);
			bt= block.buttons.last;
			bt.flag= UI.UI_TEXT_LEFT;
		}
	}

	/* inconsistent, but menus with labels do not look good flipped */
	for(a=0, b=0; a<md.nitems; a++, b++) {
		entry= md.items[a];

		if(entry.sepr!=0 && entry.str[0]!=0)
			block.flag |= UI.UI_BLOCK_NO_FLIP;
	}

	/* create items */
	split= UILayout.uiLayoutSplit(layout, 0, 0);

	for(a=0, b=0; a<md.nitems; a++, b++) {
		if((block.flag & UI.UI_BLOCK_NO_FLIP)!=0)
			entry= md.items[a];
		else
			entry= md.items[md.nitems-a-1];
		
		/* new column on N rows or on separation label */
		if((b % rows == 0) || (entry.sepr!=0 && entry.str[0]!=0)) {
			column= UILayout.uiLayoutColumn(split, 0);
			b= 0;
		}
		
		Pointer<Float> handle_retvalue = new Pointer<Float>() {
            public Float get() {
                return handle.retvalue;
            }
            public void set(Float obj) {
                handle.retvalue = obj;
            }
        };

		if(entry.sepr!=0) {
			UILayout.uiItemL(column, StringUtil.toJString(entry.str,0), entry.icon);
			bt= block.buttons.last;
			bt.flag= UI.UI_TEXT_LEFT;
		}
		else if(entry.icon!=0) {
			UI.uiDefIconTextButF(block, UI.BUTM|UI.FLO, B_NOP, BIFIconID.values()[entry.icon], StringUtil.toJString(entry.str,0), 0, 0,
					UI.UI_UNIT_X*5, UI.UI_UNIT_Y, handle_retvalue, (float) entry.retval, 0.0f, 0, 0, "");
		}
		else {
			UI.uiDefButF(block, UI.BUTM|UI.FLO, B_NOP, StringUtil.toJString(entry.str,0), 0, 0,
					UI.UI_UNIT_X*5, UI.UI_UNIT_X, handle_retvalue, (float) entry.retval, 0.0f, 0, 0, "");
		}
	}
	
	menudata_free(md);
}};

//uiBlock *ui_block_func_ICONROW(bContext *C, uiPopupBlockHandle *handle, void *arg_but)
//{
//	uiBut *but= arg_but;
//	uiBlock *block;
//	int a;
//
//	block= uiBeginBlock(C, handle.region, "menu", UI_EMBOSSP);
//	block.flag= UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_NUMSELECT;
//
//	for(a=(int)but.hardmin; a<=(int)but.hardmax; a++) {
//		uiDefIconButF(block, BUTM|FLO, B_NOP, but.icon+(a-but.hardmin), 0, (short)(18*a), (short)(but.x2-but.x1-4), 18, &handle.retvalue, (float)a, 0.0, 0, 0, "");
//	}
//
//	block.direction= UI_TOP;
//
//	uiEndBlock(C, block);
//
//	return block;
//}

//public static uiBlockHandleCreateFunc ui_block_func_ICONTEXTROW = new uiBlockHandleCreateFunc() {
//public uiBlock run(bContext C, final uiPopupBlockHandle handle, Object arg_but)
////uiBlock *ui_block_func_ICONTEXTROW(bContext *C, uiPopupBlockHandle *handle, void *arg_but)
//{
//	uiBut but= (uiBut)arg_but;
//	uiBlock block;
//	MenuData md;
//	int width, xmax, ypos, a;
//
//	block= UI.uiBeginBlock(C, handle.region, "menu", UI.UI_EMBOSSP);
//	block.flag= UI.UI_BLOCK_LOOP|UI.UI_BLOCK_REDRAW|UI.UI_BLOCK_NUMSELECT;
//
//	md= decompose_menu_string(but.str);
//
//	/* size and location */
//	/* expand menu width to fit labels */
//	if(md.title!=null)
//		width= 2*StringUtil.strlen(md.title,0)+UIStyle.UI_GetStringWidth(md.title,0);
//	else
//		width= 0;
//
//	for(a=0; a<md.nitems; a++) {
//		xmax= UIStyle.UI_GetStringWidth(md.items[a].str,0);
//		if(xmax>width) width= xmax;
//	}
//
//	width+= 30;
//	if (width<50) width=50;
//
//	ypos = 1;
//
//	/* loop through the menu options and draw them out with icons & text labels */
//	for(a=0; a<md.nitems; a++) {
//
//		/* add a space if there's a separator (%l) */
//	        if (StringUtil.strcmp(md.items[a].str,0, StringUtil.toCString("%l"),0)==0) {
//			ypos +=3;
//		}
//		else {
//                        Pointer<Float> handle_retvalue = new Pointer<Float>() {
//
//                            public Float get() {
//                                return handle.retvalue;
//                            }
//
//                            public void set(Float obj) {
//                                handle.retvalue = obj;
//                            }
//
//                        };
//			UI.uiDefIconTextButF(block, UI.BUTM|UI.FLO, B_NOP, BIFIconID.values()[but.icon.ordinal()+(int)(md.items[a].retval-but.hardmin)], StringUtil.toJString(md.items[a].str,0), 0, ypos,(short)width, 19, handle_retvalue, (float) md.items[a].retval, 0.0f, 0, 0, "");
//			ypos += 20;
//		}
//	}
//
//	if(md.title!=null) {
//		uiBut bt;
//
//		bt= UI.uiDefBut(block, UI.LABEL, 0, StringUtil.toJString(md.title,0), 0, ypos, (short)width, 19, null, 0.0f, 0.0f, 0, 0, "");
//		bt.flag= UI.UI_TEXT_LEFT;
//	}
//
////	menudata_free(md);
//
//	block.direction= UI.UI_TOP;
//
//	UI.uiBoundsBlock(block, 3);
//	UI.uiEndBlock(C, block);
//
//	return block;
//}};

public static uiMenuCreateFunc ui_block_func_ICONTEXTROW = new uiMenuCreateFunc() {
public void run(bContext C, uiLayout layout, Object arg_but)
{
	uiBlock block= UILayout.uiLayoutGetBlock(layout);
	final uiPopupBlockHandle handle= block.handle;
	uiBut but= (uiBut)arg_but, bt;
	MenuData md;
	MenuEntry entry;
	int a;
	
	UI.uiBlockSetFlag(block, UI.UI_BLOCK_MOVEMOUSE_QUIT);

	md= decompose_menu_string(but.str);

	/* title */
	if(md.title!=null) {
		bt= UI.uiDefBut(block, UI.LABEL, 0, StringUtil.toJString(md.title,0), 0, 0, UI.UI_UNIT_X*5, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");
		bt.flag= UI.UI_TEXT_LEFT;
	}

	/* loop through the menu options and draw them out with icons & text labels */
	for(a=0; a<md.nitems; a++) {
		entry= md.items[md.nitems-a-1];

		if(entry.sepr!=0)
			UILayout.uiItemS(layout);
		else {
			Pointer<Float> handle_retvalue = new Pointer<Float>() {
	            public Float get() {
	                return handle.retvalue;
	            }
	            public void set(Float obj) {
	                handle.retvalue = obj;
	            }
	        };
			UI.uiDefIconTextButF(block, UI.BUTM|UI.FLO, B_NOP, BIFIconID.values()[(short)((but.icon.ordinal())+(entry.retval-but.hardmin))], StringUtil.toJString(entry.str,0),
					0, 0, UI.UI_UNIT_X*5, UI.UI_UNIT_Y, handle_retvalue, (float) entry.retval, 0.0f, 0, 0, "");
		}
	}

	menudata_free(md);
}};

//static void ui_warp_pointer(short x, short y)
//{
//	/* XXX 2.50 which function to use for this? */
//#if 0
//	/* OSX has very poor mousewarp support, it sends events;
//	   this causes a menu being pressed immediately ... */
//	#ifndef __APPLE__
//	warp_pointer(x, y);
//	#endif
//#endif
//}
//
///********************* Color Button ****************/
//
///* picker sizes S hsize, F full size, D spacer, B button/pallette height  */
//#define SPICK	110.0
//#define FPICK	180.0
//#define DPICK	6.0
//#define BPICK	24.0
//
//#define UI_PALETTE_TOT 16
///* note; in tot+1 the old color is stored */
//static float palette[UI_PALETTE_TOT+1][3]= {
//{0.93, 0.83, 0.81}, {0.88, 0.89, 0.73}, {0.69, 0.81, 0.57}, {0.51, 0.76, 0.64},
//{0.37, 0.56, 0.61}, {0.33, 0.29, 0.55}, {0.46, 0.21, 0.51}, {0.40, 0.12, 0.18},
//{1.0, 1.0, 1.0}, {0.85, 0.85, 0.85}, {0.7, 0.7, 0.7}, {0.56, 0.56, 0.56},
//{0.42, 0.42, 0.42}, {0.28, 0.28, 0.28}, {0.14, 0.14, 0.14}, {0.0, 0.0, 0.0}
//};
//
///* for picker, while editing hsv */
//void ui_set_but_hsv(uiBut *but)
//{
//	float col[3];
//
//	hsv_to_rgb(but.hsv[0], but.hsv[1], but.hsv[2], col, col+1, col+2);
//	ui_set_but_vectorf(but, col);
//}
//
//static void update_picker_hex(uiBlock *block, float *rgb)
//{
//	uiBut *bt;
//	char col[16];
//
//	sprintf(col, "%02X%02X%02X", (unsigned int)(rgb[0]*255.0), (unsigned int)(rgb[1]*255.0), (unsigned int)(rgb[2]*255.0));
//
//	// this updates button strings, is hackish... but button pointers are on stack of caller function
//
//	for(bt= block.buttons.first; bt; bt= bt.next) {
//		if(strcmp(bt.str, "Hex: ")==0)
//			strcpy(bt.poin, col);
//
//		ui_check_but(bt);
//	}
//}
//
///* also used by small picker, be careful with name checks below... */
//void ui_update_block_buts_hsv(uiBlock *block, float *hsv)
//{
//	uiBut *bt;
//	float r, g, b;
//	float rgb[3];
//
//	// this updates button strings, is hackish... but button pointers are on stack of caller function
//	hsv_to_rgb(hsv[0], hsv[1], hsv[2], &r, &g, &b);
//
//	rgb[0] = r; rgb[1] = g; rgb[2] = b;
//	update_picker_hex(block, rgb);
//
//	for(bt= block.buttons.first; bt; bt= bt.next) {
//		if(ELEM(bt.type, HSVCUBE, HSVCIRCLE)) {
//			VECCOPY(bt.hsv, hsv);
//			ui_set_but_hsv(bt);
//		}
//		else if(bt.str[1]==' ') {
//			if(bt.str[0]=='R') {
//				ui_set_but_val(bt, r);
//			}
//			else if(bt.str[0]=='G') {
//				ui_set_but_val(bt, g);
//			}
//			else if(bt.str[0]=='B') {
//				ui_set_but_val(bt, b);
//			}
//			else if(bt.str[0]=='H') {
//				ui_set_but_val(bt, hsv[0]);
//			}
//			else if(bt.str[0]=='S') {
//				ui_set_but_val(bt, hsv[1]);
//			}
//			else if(bt.str[0]=='V') {
//				ui_set_but_val(bt, hsv[2]);
//			}
//		}
//
//		ui_check_but(bt);
//	}
//}
//
//static void ui_update_block_buts_hex(uiBlock *block, char *hexcol)
//{
//	uiBut *bt;
//	float r=0, g=0, b=0;
//	float h, s, v;
//
//
//	// this updates button strings, is hackish... but button pointers are on stack of caller function
//	hex_to_rgb(hexcol, &r, &g, &b);
//	rgb_to_hsv(r, g, b, &h, &s, &v);
//
//	for(bt= block.buttons.first; bt; bt= bt.next) {
//		if(bt.type==HSVCUBE) {
//			bt.hsv[0] = h;
//			bt.hsv[1] = s;
//			bt.hsv[2] = v;
//			ui_set_but_hsv(bt);
//		}
//		else if(bt.str[1]==' ') {
//			if(bt.str[0]=='R') {
//				ui_set_but_val(bt, r);
//			}
//			else if(bt.str[0]=='G') {
//				ui_set_but_val(bt, g);
//			}
//			else if(bt.str[0]=='B') {
//				ui_set_but_val(bt, b);
//			}
//			else if(bt.str[0]=='H') {
//				ui_set_but_val(bt, h);
//			}
//			else if(bt.str[0]=='S') {
//				ui_set_but_val(bt, s);
//			}
//			else if(bt.str[0]=='V') {
//				ui_set_but_val(bt, v);
//			}
//		}
//
//		ui_check_but(bt);
//	}
//}
//
///* bt1 is palette but, col1 is original color */
///* callback to copy from/to palette */
//static void do_palette_cb(bContext *C, void *bt1, void *col1)
//{
//	wmWindow *win= CTX_wm_window(C);
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//	float *col= (float *)col1;
//	float *fp, hsv[3];
//
//	fp= (float *)but1.poin;
//
//	if(win.eventstate.ctrl) {
//		VECCOPY(fp, col);
//	}
//	else {
//		VECCOPY(col, fp);
//	}
//
//	rgb_to_hsv(col[0], col[1], col[2], hsv, hsv+1, hsv+2);
//	ui_update_block_buts_hsv(but1.block, hsv);
//	update_picker_hex(but1.block, col);
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
//static void do_hsv_cb(bContext *C, void *bt1, void *unused)
//{
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
///* bt1 is num but, hsv1 is pointer to original color in hsv space*/
///* callback to handle changes in num-buts in picker */
//static void do_palette1_cb(bContext *C, void *bt1, void *hsv1)
//{
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//	float *hsv= (float *)hsv1;
//	float *fp= NULL;
//
//	if(but1.str[1]==' ') {
//		if(but1.str[0]=='R') fp= (float *)but1.poin;
//		else if(but1.str[0]=='G') fp= ((float *)but1.poin)-1;
//		else if(but1.str[0]=='B') fp= ((float *)but1.poin)-2;
//	}
//	if(fp) {
//		rgb_to_hsv(fp[0], fp[1], fp[2], hsv, hsv+1, hsv+2);
//	}
//	ui_update_block_buts_hsv(but1.block, hsv);
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
///* bt1 is num but, col1 is pointer to original color */
///* callback to handle changes in num-buts in picker */
//static void do_palette2_cb(bContext *C, void *bt1, void *col1)
//{
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//	float *rgb= (float *)col1;
//	float *fp= NULL;
//
//	if(but1.str[1]==' ') {
//		if(but1.str[0]=='H') fp= (float *)but1.poin;
//		else if(but1.str[0]=='S') fp= ((float *)but1.poin)-1;
//		else if(but1.str[0]=='V') fp= ((float *)but1.poin)-2;
//	}
//	if(fp) {
//		hsv_to_rgb(fp[0], fp[1], fp[2], rgb, rgb+1, rgb+2);
//	}
//	ui_update_block_buts_hsv(but1.block, fp);
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
//static void do_palette_hex_cb(bContext *C, void *bt1, void *hexcl)
//{
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//	char *hexcol= (char *)hexcl;
//
//	ui_update_block_buts_hex(but1.block, hexcol);
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
///* used for both 3d view and image window */
//static void do_palette_sample_cb(bContext *C, void *bt1, void *col1)	/* frontbuf */
//{
//	/* XXX 2.50 this should become an operator? */
//#if 0
//	uiBut *but1= (uiBut *)bt1;
//	uiBut *but;
//	float tempcol[4];
//	int x=0, y=0;
//	short mval[2];
//	float hsv[3];
//	short capturing;
//	int oldcursor;
//	Window *win;
//	unsigned short dev;
//
//	oldcursor=get_cursor();
//	win=winlay_get_active_window();
//
//	while (get_mbut() & L_MOUSE) UI_wait_for_statechange();
//
//	SetBlenderCursor(BC_EYEDROPPER_CURSOR);
//
//	/* loop and wait for a mouse click */
//	capturing = TRUE;
//	while(capturing) {
//		char ascii;
//		short val;
//
//		dev = extern_qread_ext(&val, &ascii);
//
//		if(dev==INPUTCHANGE) break;
//		if(get_mbut() & R_MOUSE) break;
//		else if(get_mbut() & L_MOUSE) {
//			uiGetMouse(mywinget(), mval);
//			x= mval[0]; y= mval[1];
//
//			capturing = FALSE;
//			break;
//		}
//		else if(dev==ESCKEY) break;
//	}
//	window_set_cursor(win, oldcursor);
//
//	if(capturing) return;
//
//	if(x<0 || y<0) return;
//
//	/* if we've got a glick, use OpenGL to sample the color under the mouse pointer */
//	glReadBuffer(GL_FRONT);
//	glReadPixels(x, y, 1, 1, GL_RGBA, GL_FLOAT, tempcol);
//	glReadBuffer(GL_BACK);
//
//	/* and send that color back to the picker */
//	rgb_to_hsv(tempcol[0], tempcol[1], tempcol[2], hsv, hsv+1, hsv+2);
//	ui_update_block_buts_hsv(but1.block, hsv);
//	update_picker_hex(but1.block, tempcol);
//
//	for (but= but1.block.buttons.first; but; but= but.next) {
//		ui_check_but(but);
//		ui_draw_but(but);
//	}
//
//	but= but1.block.buttons.first;
//	ui_block_flush_back(but.block);
//#endif
//}
//
///* color picker, Gimp version. mode: 'f' = floating panel, 'p' =  popup */
///* col = read/write to, hsv/old/hexcol = memory for temporal use */
//void uiBlockPickerButtons(uiBlock *block, float *col, float *hsv, float *old, char *hexcol, char mode, short retval)
//{
//	uiBut *bt;
//	float h, offs;
//	int a;
//
//	VECCOPY(old, col);	// old color stored there, for palette_cb to work
//
//	// the cube intersection
//	bt= uiDefButF(block, HSVCUBE, retval, "",	0,DPICK+BPICK,FPICK,FPICK, col, 0.0, 0.0, 2, 0, "");
//	uiButSetFunc(bt, do_hsv_cb, bt, NULL);
//
//	bt= uiDefButF(block, HSVCUBE, retval, "",	0,0,FPICK,BPICK, col, 0.0, 0.0, 3, 0, "");
//	uiButSetFunc(bt, do_hsv_cb, bt, NULL);
//
//	// palette
//
//	bt=uiDefButF(block, COL, retval, "",		FPICK+DPICK, 0, BPICK,BPICK, old, 0.0, 0.0, -1, 0, "Old color, click to restore");
//	uiButSetFunc(bt, do_palette_cb, bt, col);
//	uiDefButF(block, COL, retval, "",		FPICK+DPICK, BPICK+DPICK, BPICK,60-BPICK-DPICK, col, 0.0, 0.0, -1, 0, "Active color");
//
//	h= (DPICK+BPICK+FPICK-64)/(UI_PALETTE_TOT/2.0);
//	uiBlockBeginAlign(block);
//	for(a= -1+UI_PALETTE_TOT/2; a>=0; a--) {
//		bt= uiDefButF(block, COL, retval, "",	FPICK+DPICK, 65.0+(float)a*h, BPICK/2, h, palette[a+UI_PALETTE_TOT/2], 0.0, 0.0, -1, 0, "Click to choose, hold CTRL to store in palette");
//		uiButSetFunc(bt, do_palette_cb, bt, col);
//		bt= uiDefButF(block, COL, retval, "",	FPICK+DPICK+BPICK/2, 65.0+(float)a*h, BPICK/2, h, palette[a], 0.0, 0.0, -1, 0, "Click to choose, hold CTRL to store in palette");
//		uiButSetFunc(bt, do_palette_cb, bt, col);
//	}
//	uiBlockEndAlign(block);
//
//	// buttons
//	rgb_to_hsv(col[0], col[1], col[2], hsv, hsv+1, hsv+2);
//	sprintf(hexcol, "%02X%02X%02X", (unsigned int)(col[0]*255.0), (unsigned int)(col[1]*255.0), (unsigned int)(col[2]*255.0));
//
//	offs= FPICK+2*DPICK+BPICK;
//
//	/* note; made this a TOG now, with NULL pointer. Is because BUT now gets handled with a afterfunc */
//	bt= uiDefIconTextBut(block, TOG, UI_RETURN_OK, ICON_EYEDROPPER, "Sample", offs+55, 170, 85, 20, NULL, 0, 0, 0, 0, "Sample the color underneath the following mouse click (ESC or RMB to cancel)");
//	uiButSetFunc(bt, do_palette_sample_cb, bt, col);
//	uiButSetFlag(bt, UI_TEXT_LEFT);
//
//	bt= uiDefBut(block, TEX, retval, "Hex: ", offs, 140, 140, 20, hexcol, 0, 8, 0, 0, "Hex triplet for color (#RRGGBB)");
//	uiButSetFunc(bt, do_palette_hex_cb, bt, hexcol);
//
//	uiBlockBeginAlign(block);
//	bt= uiDefButF(block, NUMSLI, retval, "R ",	offs, 110, 140,20, col, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//	bt= uiDefButF(block, NUMSLI, retval, "G ",	offs, 90, 140,20, col+1, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//	bt= uiDefButF(block, NUMSLI, retval, "B ",	offs, 70, 140,20, col+2, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//
//	uiBlockBeginAlign(block);
//	bt= uiDefButF(block, NUMSLI, retval, "H ",	offs, 40, 140,20, hsv, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	bt= uiDefButF(block, NUMSLI, retval, "S ",	offs, 20, 140,20, hsv+1, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	bt= uiDefButF(block, NUMSLI, retval, "V ",	offs, 0, 140,20, hsv+2, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	uiBlockEndAlign(block);
//}
//
///* bt1 is num but, hsv1 is pointer to original color in hsv space*/
///* callback to handle changes */
//static void do_picker_small_cb(bContext *C, void *bt1, void *hsv1)
//{
//	uiBut *but1= (uiBut *)bt1;
//	uiPopupBlockHandle *popup= but1.block.handle;
//	float *hsv= (float *)hsv1;
//	float *fp= NULL;
//
//	fp= (float *)but1.poin;
//	rgb_to_hsv(fp[0], fp[1], fp[2], hsv, hsv+1, hsv+2);
//
//	ui_update_block_buts_hsv(but1.block, hsv);
//
//	if(popup)
//		popup.menuretval= UI_RETURN_UPDATE;
//}
//
///* picker sizes S hsize, F full size, D spacer, B button/pallette height  */
//#define SPICK1	150.0
//#define DPICK1	6.0
//
///* only the color, a HS circle and V slider */
//static void uiBlockPickerSmall(uiBlock *block, float *col, float *hsv, float *old, char *hexcol, char mode, short retval)
//{
//	uiBut *bt;
//
//	VECCOPY(old, col);	// old color stored there, for palette_cb to work
//
//	/* HS circle */
//	bt= uiDefButF(block, HSVCIRCLE, retval, "",	0, 0,SPICK1,SPICK1, col, 0.0, 0.0, 0, 0, "");
//	uiButSetFunc(bt, do_picker_small_cb, bt, hsv);
//
//	/* value */
//	bt= uiDefButF(block, HSVCUBE, retval, "",	SPICK1+DPICK1,0,14,SPICK1, col, 0.0, 0.0, 4, 0, "");
//	uiButSetFunc(bt, do_picker_small_cb, bt, hsv);
//}
//
//
//static void picker_new_hide_reveal(uiBlock *block, short colormode)
//{
//	uiBut *bt;
//
//	/* tag buttons */
//	for(bt= block.buttons.first; bt; bt= bt.next) {
//
//		if(bt.type==NUMSLI || bt.type==TEX) {
//			if( bt.str[1]=='e') {
//				if(colormode==2) bt.flag &= ~UI_HIDDEN;
//				else bt.flag |= UI_HIDDEN;
//			}
//			else if( ELEM3(bt.str[0], 'R', 'G', 'B')) {
//				if(colormode==0) bt.flag &= ~UI_HIDDEN;
//				else bt.flag |= UI_HIDDEN;
//			}
//			else if( ELEM3(bt.str[0], 'H', 'S', 'V')) {
//				if(colormode==1) bt.flag &= ~UI_HIDDEN;
//				else bt.flag |= UI_HIDDEN;
//			}
//		}
//	}
//}
//
//static void do_picker_new_mode_cb(bContext *C, void *bt1, void *colv)
//{
//	uiBut *bt= bt1;
//	short colormode= ui_get_but_val(bt);
//
//	picker_new_hide_reveal(bt.block, colormode);
//}
//
//
///* a HS circle, V slider, rgb/hsv/hex sliders */
//static void uiBlockPickerNew(uiBlock *block, float *col, float *hsv, float *old, char *hexcol, char mode, short retval)
//{
//	static short colormode= 0;	/* temp? 0=rgb, 1=hsv, 2=hex */
//	uiBut *bt;
//	int width;
//
//	VECCOPY(old, col);	// old color stored there, for palette_cb to work
//
//	/* HS circle */
//	bt= uiDefButF(block, HSVCIRCLE, retval, "",	0, 0,SPICK1,SPICK1, col, 0.0, 0.0, 0, 0, "");
//	uiButSetFunc(bt, do_picker_small_cb, bt, hsv);
//
//	/* value */
//	bt= uiDefButF(block, HSVCUBE, retval, "",	SPICK1+DPICK1,0,14,SPICK1, col, 0.0, 0.0, 4, 0, "");
//	uiButSetFunc(bt, do_picker_small_cb, bt, hsv);
//
//	/* mode */
//	width= (SPICK1+DPICK1+14)/3;
//	uiBlockBeginAlign(block);
//	bt= uiDefButS(block, ROW, retval, "RGB",	0, -30, width, 19, &colormode, 0.0, 0.0, 0, 0, "");
//	uiButSetFunc(bt, do_picker_new_mode_cb, bt, col);
//	bt= uiDefButS(block, ROW, retval, "HSV",	width, -30, width, 19, &colormode, 0.0, 1.0, 0, 0, "");
//	uiButSetFunc(bt, do_picker_new_mode_cb, bt, hsv);
//	bt= uiDefButS(block, ROW, retval, "Hex",	2*width, -30, width, 19, &colormode, 0.0, 2.0, 0, 0, "");
//	uiButSetFunc(bt, do_picker_new_mode_cb, bt, hexcol);
//	uiBlockEndAlign(block);
//
//	/* sliders or hex */
//	width= (SPICK1+DPICK1+14);
//	rgb_to_hsv(col[0], col[1], col[2], hsv, hsv+1, hsv+2);
//	sprintf(hexcol, "%02X%02X%02X", (unsigned int)(col[0]*255.0), (unsigned int)(col[1]*255.0), (unsigned int)(col[2]*255.0));
//
//	uiBlockBeginAlign(block);
//	bt= uiDefButF(block, NUMSLI, 0, "R ",	0, -60, width, 19, col, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//	bt= uiDefButF(block, NUMSLI, 0, "G ",	0, -80, width, 19, col+1, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//	bt= uiDefButF(block, NUMSLI, 0, "B ",	0, -100, width, 19, col+2, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette1_cb, bt, hsv);
//	uiBlockEndAlign(block);
//
//	uiBlockBeginAlign(block);
//	bt= uiDefButF(block, NUMSLI, 0, "H ",	0, -60, width, 19, hsv, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	bt= uiDefButF(block, NUMSLI, 0, "S ",	0, -80, width, 19, hsv+1, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	bt= uiDefButF(block, NUMSLI, 0, "V ",	0, -100, width, 19, hsv+2, 0.0, 1.0, 10, 3, "");
//	uiButSetFunc(bt, do_palette2_cb, bt, col);
//	uiBlockEndAlign(block);
//
//	bt= uiDefBut(block, TEX, 0, "Hex: ", 0, -80, width, 19, hexcol, 0, 8, 0, 0, "Hex triplet for color (#RRGGBB)");
//	uiButSetFunc(bt, do_palette_hex_cb, bt, hexcol);
//
//	picker_new_hide_reveal(block, colormode);
//}
//
//
//static int ui_picker_small_wheel(const bContext *C, uiBlock *block, wmEvent *event)
//{
//	float add= 0.0f;
//
//	if(event.type==WHEELUPMOUSE)
//		add= 0.05f;
//	else if(event.type==WHEELDOWNMOUSE)
//		add= -0.05f;
//
//	if(add!=0.0f) {
//		uiBut *but;
//
//		for(but= block.buttons.first; but; but= but.next) {
//			if(but.type==HSVCUBE && but.active==NULL) {
//				uiPopupBlockHandle *popup= block.handle;
//				float col[3];
//
//				ui_get_but_vectorf(but, col);
//
//				rgb_to_hsv(col[0], col[1], col[2], but.hsv, but.hsv+1, but.hsv+2);
//				but.hsv[2]= CLAMPIS(but.hsv[2]+add, 0.0f, 1.0f);
//				hsv_to_rgb(but.hsv[0], but.hsv[1], but.hsv[2], col, col+1, col+2);
//
//				ui_set_but_vectorf(but, col);
//
//				ui_update_block_buts_hsv(block, but.hsv);
//				if(popup)
//					popup.menuretval= UI_RETURN_UPDATE;
//
//				return 1;
//			}
//		}
//	}
//	return 0;
//}
//
//uiBlock *ui_block_func_COL(bContext *C, uiPopupBlockHandle *handle, void *arg_but)
//{
//	wmWindow *win= CTX_wm_window(C); // XXX temp, needs to become keymap to detect type?
//	uiBut *but= arg_but;
//	uiBlock *block;
//	static float hsvcol[3], oldcol[3];
//	static char hexcol[128];
//
//	block= uiBeginBlock(C, handle.region, "colorpicker", UI_EMBOSS);
//
//	VECCOPY(handle.retvec, but.editvec);
//	if(win.eventstate.shift) {
//		uiBlockPickerButtons(block, handle.retvec, hsvcol, oldcol, hexcol, 'p', 0);
//		block.flag= UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_KEEP_OPEN;
//		uiBoundsBlock(block, 3);
//	}
//	else if(win.eventstate.alt) {
//		uiBlockPickerSmall(block, handle.retvec, hsvcol, oldcol, hexcol, 'p', 0);
//		block.flag= UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_RET_1|UI_BLOCK_OUT_1;
//		uiBoundsBlock(block, 10);
//
//		block.block_event_func= ui_picker_small_wheel;
//	}
//	else {
//		uiBlockPickerNew(block, handle.retvec, hsvcol, oldcol, hexcol, 'p', 0);
//		block.flag= UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_KEEP_OPEN;
//		uiBoundsBlock(block, 10);
//
//		block.block_event_func= ui_picker_small_wheel;
//	}
//
//
//	/* and lets go */
//	block.direction= UI_TOP;
//
//	return block;
//}
//
///* ******************** Color band *************** */
//
//static int vergcband(const void *a1, const void *a2)
//{
//	const CBData *x1=a1, *x2=a2;
//
//	if( x1.pos > x2.pos ) return 1;
//	else if( x1.pos < x2.pos) return -1;
//	return 0;
//}
//
//static void colorband_pos_cb(bContext *C, void *coba_v, void *unused_v)
//{
//	ColorBand *coba= coba_v;
//	int a;
//
//	if(coba.tot<2) return;
//
//	for(a=0; a<coba.tot; a++) coba.data[a].cur= a;
//	qsort(coba.data, coba.tot, sizeof(CBData), vergcband);
//	for(a=0; a<coba.tot; a++) {
//		if(coba.data[a].cur==coba.cur) {
//			/* if(coba.cur!=a) addqueue(curarea.win, REDRAW, 0); */	/* button cur */
//			coba.cur= a;
//			break;
//		}
//	}
//}
//
//static void colorband_add_cb(bContext *C, void *coba_v, void *unused_v)
//{
//	ColorBand *coba= coba_v;
//
//	if(coba.tot < MAXCOLORBAND-1) coba.tot++;
//	coba.cur= coba.tot-1;
//
//	colorband_pos_cb(C, coba, NULL);
////	BIF_undo_push("Add colorband");
//
//}
//
//static void colorband_del_cb(bContext *C, void *coba_v, void *unused_v)
//{
//	ColorBand *coba= coba_v;
//	int a;
//
//	if(coba.tot<2) return;
//
//	for(a=coba.cur; a<coba.tot; a++) {
//		coba.data[a]= coba.data[a+1];
//	}
//	if(coba.cur) coba.cur--;
//	coba.tot--;
//
////	BIF_undo_push("Delete colorband");
////	BIF_preview_changed(ID_TE);
//}
//
//void uiBlockColorbandButtons(uiBlock *block, ColorBand *coba, rctf *butr, int event)
//{
//	CBData *cbd;
//	uiBut *bt;
//	float unit= (butr.xmax-butr.xmin)/14.0f;
//	float xs= butr.xmin;
//
//	cbd= coba.data + coba.cur;
//
//	uiBlockBeginAlign(block);
//	uiDefButF(block, COL, event,		"",			xs,butr.ymin+20.0f,2.0f*unit,20,				&(cbd.r), 0, 0, 0, 0, "");
//	uiDefButF(block, NUM, event,		"A:",		xs+2.0f*unit,butr.ymin+20.0f,4.0f*unit,20,	&(cbd.a), 0.0f, 1.0f, 10, 2, "");
//	bt= uiDefBut(block, BUT, event,	"Add",		xs+6.0f*unit,butr.ymin+20.0f,2.0f*unit,20,	NULL, 0, 0, 0, 0, "Adds a new color position to the colorband");
//	uiButSetFunc(bt, colorband_add_cb, coba, NULL);
//	bt= uiDefBut(block, BUT, event,	"Del",		xs+8.0f*unit, butr.ymin+20.0f, 2.0f*unit, 20,	NULL, 0, 0, 0, 0, "Deletes the active position");
//	uiButSetFunc(bt, colorband_del_cb, coba, NULL);
//
//	uiDefButS(block, MENU, event,		"Interpolation %t|Ease %x1|Cardinal %x3|Linear %x0|B-Spline %x2|Constant %x4",
//			  xs + 10.0f*unit, butr.ymin+20.0f, unit*4.0f, 20,		&coba.ipotype, 0.0, 0.0, 0, 0, "Sets interpolation type");
//
//	uiDefBut(block, BUT_COLORBAND, event, "",		xs, butr.ymin, butr.xmax-butr.xmin, 20.0f, coba, 0, 0, 0, 0, "");
//	uiBlockEndAlign(block);
//
//}
//
//
///* ******************** PUPmenu ****************** */
//
//static int pupmenu_set= 0;
//
//void uiPupMenuSetActive(int val)
//{
//	pupmenu_set= val;
//}
//
///* value== -1 read, otherwise set */
//static int pupmenu_memory(char *str, int value)
//{
//	static char mem[256], first=1;
//	int val=0, nr=0;
//
//	if(first) {
//		memset(mem, 0, 256);
//		first= 0;
//	}
//	while(str[nr]) {
//		val+= str[nr];
//		nr++;
//	}
//
//	if(value >= 0) mem[ val & 255 ]= value;
//	else return mem[ val & 255 ];
//
//	return 0;
//}
//
//#define PUP_LABELH	6
//
//typedef struct uiPupMenuInfo {
//	char *instr;
//	int mx, my;
//	int startx, starty;
//	int maxrow;
//} uiPupMenuInfo;
//
//uiBlock *ui_block_func_PUPMENU(bContext *C, uiPopupBlockHandle *handle, void *arg_info)
//{
//	uiBlock *block;
//	uiPupMenuInfo *info;
//	int columns, rows, mousemove[2]= {0, 0}, mousewarp= 0;
//	int width, height, xmax, ymax, maxrow;
//	int a, startx, starty, endx, endy, x1, y1;
//	int lastselected;
//	MenuData *md;
//
//	info= arg_info;
//	maxrow= info.maxrow;
//	height= 0;
//
//	/* block stuff first, need to know the font */
//	block= uiBeginBlock(C, handle.region, "menu", UI_EMBOSSP);
//	uiBlockSetFlag(block, UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_RET_1|UI_BLOCK_NUMSELECT);
//	block.direction= UI_DOWN;
//
//	md= decompose_menu_string(info.instr);
//
//	rows= md.nitems;
//	if(rows<1)
//		rows= 1;
//
//	columns= 1;
//
//	/* size and location, title slightly bigger for bold */
//	if(md.title) {
//		width= 2*strlen(md.title)+UI_GetStringWidth(md.title);
//		width /= columns;
//	}
//	else width= 0;
//
//	for(a=0; a<md.nitems; a++) {
//		xmax= UI_GetStringWidth(md.items[a].str);
//		if(xmax>width) width= xmax;
//
//		if(strcmp(md.items[a].str, "%l")==0) height+= PUP_LABELH;
//		else height+= MENU_BUTTON_HEIGHT;
//	}
//
//	width+= 10;
//	if (width<50) width=50;
//
//	wm_window_get_size(CTX_wm_window(C), &xmax, &ymax);
//
//	/* set first item */
//	lastselected= 0;
//	if(pupmenu_set) {
//		lastselected= pupmenu_set-1;
//		pupmenu_set= 0;
//	}
//	else if(md.nitems>1) {
//		lastselected= pupmenu_memory(info.instr, -1);
//	}
//
//	startx= info.mx-(0.8*(width));
//	starty= info.my-height+MENU_BUTTON_HEIGHT/2;
//	if(lastselected>=0 && lastselected<md.nitems) {
//		for(a=0; a<md.nitems; a++) {
//			if(a==lastselected) break;
//			if( strcmp(md.items[a].str, "%l")==0) starty+= PUP_LABELH;
//			else starty+=MENU_BUTTON_HEIGHT;
//		}
//
//		//starty= info.my-height+MENU_BUTTON_HEIGHT/2+lastselected*MENU_BUTTON_HEIGHT;
//	}
//
//	if(startx<10) {
//		startx= 10;
//	}
//	if(starty<10) {
//		mousemove[1]= 10-starty;
//		starty= 10;
//	}
//
//	endx= startx+width*columns;
//	endy= starty+height;
//
//	if(endx>xmax) {
//		endx= xmax-10;
//		startx= endx-width*columns;
//	}
//	if(endy>ymax-20) {
//		mousemove[1]= ymax-endy-20;
//		endy= ymax-20;
//		starty= endy-height;
//	}
//
//	if(mousemove[0] || mousemove[1]) {
//		ui_warp_pointer(info.mx+mousemove[0], info.my+mousemove[1]);
//		mousemove[0]= info.mx;
//		mousemove[1]= info.my;
//		mousewarp= 1;
//	}
//
//	/* here we go! */
//	if(md.title) {
//		uiBut *bt;
//		char titlestr[256];
//
//		if(md.titleicon) {
//			width+= 20;
//			sprintf(titlestr, " %s", md.title);
//			uiDefIconTextBut(block, LABEL, 0, md.titleicon, titlestr, startx, (short)(starty+height), width, MENU_BUTTON_HEIGHT, NULL, 0.0, 0.0, 0, 0, "");
//		}
//		else {
//			bt= uiDefBut(block, LABEL, 0, md.title, startx, (short)(starty+height), columns*width, MENU_BUTTON_HEIGHT, NULL, 0.0, 0.0, 0, 0, "");
//			bt.flag= UI_TEXT_LEFT;
//		}
//
//		//uiDefBut(block, SEPR, 0, "", startx, (short)(starty+height)-MENU_SEPR_HEIGHT, width, MENU_SEPR_HEIGHT, NULL, 0.0, 0.0, 0, 0, "");
//	}
//
//	x1= startx + width*((int)a/rows);
//	y1= starty + height - MENU_BUTTON_HEIGHT; // - MENU_SEPR_HEIGHT;
//
//	for(a=0; a<md.nitems; a++) {
//		char *name= md.items[a].str;
//		int icon = md.items[a].icon;
//
//		if(strcmp(name, "%l")==0) {
//			uiDefBut(block, SEPR, B_NOP, "", x1, y1, width, PUP_LABELH, NULL, 0, 0.0, 0, 0, "");
//			y1 -= PUP_LABELH;
//		}
//		else if (icon) {
//			uiDefIconButF(block, BUTM, B_NOP, icon, x1, y1, width+16, MENU_BUTTON_HEIGHT-1, &handle.retvalue, (float) md.items[a].retval, 0.0, 0, 0, "");
//			y1 -= MENU_BUTTON_HEIGHT;
//		}
//		else {
//			uiDefButF(block, BUTM, B_NOP, name, x1, y1, width, MENU_BUTTON_HEIGHT-1, &handle.retvalue, (float) md.items[a].retval, 0.0, 0, 0, "");
//			y1 -= MENU_BUTTON_HEIGHT;
//		}
//	}
//
//	uiBoundsBlock(block, 1);
//	uiEndBlock(C, block);
//
//	menudata_free(md);
//
//	/* XXX 2.5 need to store last selected */
//#if 0
//	/* calculate last selected */
//	if(event & ui_return_ok) {
//		lastselected= 0;
//		for(a=0; a<md.nitems; a++) {
//			if(val==md.items[a].retval) lastselected= a;
//		}
//
//		pupmenu_memory(info.instr, lastselected);
//	}
//#endif
//
//	/* XXX 2.5 need to warp back */
//#if 0
//	if(mousemove[1] && (event & ui_return_out)==0)
//		ui_warp_pointer(mousemove[0], mousemove[1]);
//	return val;
//#endif
//
//	return block;
//}
//
//uiBlock *ui_block_func_PUPMENUCOL(bContext *C, uiPopupBlockHandle *handle, void *arg_info)
//{
//	uiBlock *block;
//	uiPupMenuInfo *info;
//	int columns, rows, mousemove[2]= {0, 0}, mousewarp;
//	int width, height, xmax, ymax, maxrow;
//	int a, startx, starty, endx, endy, x1, y1;
//	float fvalue;
//	MenuData *md;
//
//	info= arg_info;
//	maxrow= info.maxrow;
//	height= 0;
//
//	/* block stuff first, need to know the font */
//	block= uiBeginBlock(C, handle.region, "menu", UI_EMBOSSP);
//	uiBlockSetFlag(block, UI_BLOCK_LOOP|UI_BLOCK_REDRAW|UI_BLOCK_RET_1|UI_BLOCK_NUMSELECT);
//	block.direction= UI_DOWN;
//
//	md= decompose_menu_string(info.instr);
//
//	/* columns and row calculation */
//	columns= (md.nitems+maxrow)/maxrow;
//	if (columns<1) columns= 1;
//
//	if(columns > 8) {
//		maxrow += 5;
//		columns= (md.nitems+maxrow)/maxrow;
//	}
//
//	rows= (int) md.nitems/columns;
//	if (rows<1) rows= 1;
//
//	while (rows*columns<(md.nitems+columns) ) rows++;
//
//	/* size and location, title slightly bigger for bold */
//	if(md.title) {
//		width= 2*strlen(md.title)+UI_GetStringWidth(md.title);
//		width /= columns;
//	}
//	else width= 0;
//
//	for(a=0; a<md.nitems; a++) {
//		xmax= UI_GetStringWidth(md.items[a].str);
//		if(xmax>width) width= xmax;
//	}
//
//	width+= 10;
//	if (width<50) width=50;
//
//	height= rows*MENU_BUTTON_HEIGHT;
//	if (md.title) height+= MENU_BUTTON_HEIGHT;
//
//	wm_window_get_size(CTX_wm_window(C), &xmax, &ymax);
//
//	/* find active item */
//	fvalue= handle.retvalue;
//	for(a=0; a<md.nitems; a++) {
//		if( md.items[a].retval== (int)fvalue ) break;
//	}
//
//	/* no active item? */
//	if(a==md.nitems) {
//		if(md.title) a= -1;
//		else a= 0;
//	}
//
//	if(a>0)
//		startx = info.mx-width/2 - ((int)(a)/rows)*width;
//	else
//		startx= info.mx-width/2;
//	starty = info.my-height + MENU_BUTTON_HEIGHT/2 + ((a)%rows)*MENU_BUTTON_HEIGHT;
//
//	if (md.title) starty+= MENU_BUTTON_HEIGHT;
//
//	if(startx<10) {
//		mousemove[0]= 10-startx;
//		startx= 10;
//	}
//	if(starty<10) {
//		mousemove[1]= 10-starty;
//		starty= 10;
//	}
//
//	endx= startx+width*columns;
//	endy= starty+height;
//
//	if(endx>xmax) {
//		mousemove[0]= xmax-endx-10;
//		endx= xmax-10;
//		startx= endx-width*columns;
//	}
//	if(endy>ymax) {
//		mousemove[1]= ymax-endy-10;
//		endy= ymax-10;
//		starty= endy-height;
//	}
//
//	if(mousemove[0] || mousemove[1]) {
//		ui_warp_pointer(info.mx+mousemove[0], info.my+mousemove[1]);
//		mousemove[0]= info.mx;
//		mousemove[1]= info.my;
//		mousewarp= 1;
//	}
//
//	/* here we go! */
//	if(md.title) {
//		uiBut *bt;
//
//		if(md.titleicon) {
//		}
//		else {
//			bt= uiDefBut(block, LABEL, 0, md.title, startx, (short)(starty+rows*MENU_BUTTON_HEIGHT), columns*width, MENU_BUTTON_HEIGHT, NULL, 0.0, 0.0, 0, 0, "");
//			bt.flag= UI_TEXT_LEFT;
//		}
//	}
//
//	for(a=0; a<md.nitems; a++) {
//		char *name= md.items[a].str;
//		int icon = md.items[a].icon;
//
//		x1= startx + width*((int)a/rows);
//		y1= starty - MENU_BUTTON_HEIGHT*(a%rows) + (rows-1)*MENU_BUTTON_HEIGHT;
//
//		if(strcmp(name, "%l")==0) {
//			uiDefBut(block, SEPR, B_NOP, "", x1, y1, width, PUP_LABELH, NULL, 0, 0.0, 0, 0, "");
//			y1 -= PUP_LABELH;
//		}
//		else if (icon) {
//			uiDefIconButF(block, BUTM, B_NOP, icon, x1, y1, width+16, MENU_BUTTON_HEIGHT-1, &handle.retvalue, (float) md.items[a].retval, 0.0, 0, 0, "");
//			y1 -= MENU_BUTTON_HEIGHT;
//		}
//		else {
//			uiDefButF(block, BUTM, B_NOP, name, x1, y1, width, MENU_BUTTON_HEIGHT-1, &handle.retvalue, (float) md.items[a].retval, 0.0, 0, 0, "");
//			y1 -= MENU_BUTTON_HEIGHT;
//		}
//	}
//
//	uiBoundsBlock(block, 1);
//	uiEndBlock(C, block);
//
//	menudata_free(md);
//
//	/* XXX 2.5 need to warp back */
//#if 0
//	if((event & UI_RETURN_OUT)==0)
//		ui_warp_pointer(mousemove[0], mousemove[1]);
//#endif
//
//	return block;
//}

/* but == NULL read, otherwise set */
public static uiBut ui_popup_menu_memory(uiBlock block, uiBut but)
{
//	static int mem[256], first=1;
//	int hash= block->puphash;
//	
//	if(first) {
//		/* init */
//		memset(mem, -1, sizeof(mem));
//		first= 0;
//	}
//
//	if(but) {
//		/* set */
//		mem[hash & 255 ]= ui_popup_string_hash(but->str);
//		return NULL;
//	}
//	else {
//		/* get */
//		for(but=block->buttons.first; but; but=but->next)
//			if(ui_popup_string_hash(but->str) == mem[hash & 255])
//				return but;
//
		return null;
//	}
}

/************************** Menu Definitions ***************************/

/* prototype */
//static uiBlock *ui_block_func_MENU_ITEM(bContext *C, uiPopupBlockHandle *handle, void *arg_info);

public static class uiPopupMenu {
	public uiBlock block;
	public uiLayout layout;
	public uiBut but;

	public int mx, my, popup, slideout;
	public int startx, starty, maxrow;

	public uiMenuCreateFunc menu_func;
	public Object menu_arg;
};

//public static class uiMenuInfo {
//	public uiPopupMenu pup;
//	public int mx, my, popup, slideout;
//	public int startx, starty;
//};

/************************ Menu Definitions to uiBlocks ***********************/

public static uiBlockHandleCreateFunc ui_block_func_POPUP = new uiBlockHandleCreateFunc() {
public uiBlock run(bContext C, uiPopupBlockHandle handle, Object arg_pup)
{
	uiBlock block;
	uiBut bt;
	ScrArea sa;
	ARegion ar;
	uiPopupMenu pup= (uiPopupMenu)arg_pup;
	int[] offset=new int[2];
	int[] width={0}, height={0};
	int direction, minwidth, flip;

	if(pup.menu_func!=null) {
		pup.block.handle= handle;
		pup.menu_func.run(C, pup.layout, pup.menu_arg);
		pup.block.handle= null;
	}

	if(pup.but!=null) {
		/* minimum width to enforece */
		minwidth= (int)(pup.but.x2 - pup.but.x1);

		if(pup.but.type == UI.PULLDOWN || pup.but.menu_create_func!=null) {
			direction= UI.UI_DOWN;
			flip= 1;
		}
		else {
			direction= UI.UI_TOP;
			flip= 0;
		}
	}
	else {
		minwidth= 50;
		direction= UI.UI_DOWN;
		flip= 1;
	}

	block= pup.block;
	
	/* in some cases we create the block before the region,
	   so we set it delayed here if necessary */
	if(ListBaseUtil.BLI_findindex(handle.region.uiblocks, block) == -1)
		UI.uiBlockSetRegion(block, handle.region);

	block.direction= (short)direction;

	UILayout.uiBlockLayoutResolve(block, width, height);

	UI.uiBlockSetFlag(block, UI.UI_BLOCK_MOVEMOUSE_QUIT);
	
	if(pup.popup!=0) {
		UI.uiBlockSetFlag(block, UI.UI_BLOCK_LOOP|UI.UI_BLOCK_REDRAW|UI.UI_BLOCK_NUMSELECT|UI.UI_BLOCK_RET_1);
		UI.uiBlockSetDirection(block, direction);

		/* offset the mouse position, possibly based on earlier selection */
//		if((block.flag & UI.UI_BLOCK_POPUP_MEMORY)!=0 &&
//			(bt= ui_popup_menu_memory(block, null))) {
//			/* position mouse on last clicked item, at 0.8*width of the
//			   button, so it doesn't overlap the text too much, also note
//			   the offset is negative because we are inverse moving the
//			   block to be under the mouse */
//			offset[0]= (int)(-(bt.x1 + 0.8f*(bt.x2 - bt.x1)));
//			offset[1]= (int)(-(bt.y1 + 0.5f*MENU_BUTTON_HEIGHT));
//		}
//		else {
			/* position mouse at 0.8*width of the button and below the tile
			   on the first item */
			offset[0]= 0;
			for(bt=block.buttons.first; bt!=null; bt=bt.next)
				offset[0]= (int)UtilDefines.MIN2(offset[0], -(bt.x1 + 0.8f*(bt.x2 - bt.x1)));

			offset[1]= (int)(1.5f*MENU_BUTTON_HEIGHT);
//		}

		block.minbounds= minwidth;
		UI.uiMenuPopupBoundsBlock(block, 1, offset[0], offset[1]);
	}
	else {
		/* for a header menu we set the direction automatic */
		if(pup.slideout==0 && flip!=0) {
			sa= bContext.CTX_wm_area(C);
			ar= bContext.CTX_wm_region(C);

			if(sa!=null && sa.headertype==ScreenTypes.HEADERDOWN) {
				if(ar!=null && ar.regiontype == ScreenTypes.RGN_TYPE_HEADER) {
					UI.uiBlockSetDirection(block, UI.UI_TOP);
					UI.uiBlockFlipOrder(block);
				}
			}
		}

		block.minbounds= minwidth;
		UI.uiTextBoundsBlock(block, 50);
	}

	/* if menu slides out of other menu, override direction */
	if(pup.slideout!=0)
		UI.uiBlockSetDirection(block, UI.UI_RIGHT);

	UI.uiEndBlock(C, block);

	return pup.block;
}};

//public static uiBlockHandleCreateFunc ui_block_func_MENU_ITEM = new uiBlockHandleCreateFunc() {
//public uiBlock run(bContext C, uiPopupBlockHandle handle, Object arg_info)
////static uiBlock *ui_block_func_MENU_ITEM(bContext *C, uiPopupBlockHandle *handle, void *arg_info)
//{
//	uiBlock block;
//	uiMenuInfo info= (uiMenuInfo)arg_info;
//	uiPopupMenu pup;
//	ScrArea sa;
//	ARegion ar;
//
//	pup= info.pup;
//	block= pup.block;
//
//	/* block stuff first, need to know the font */
//	UI.uiBlockSetRegion(block, handle.region);
//	block.direction= UI.UI_DOWN;
//
//	UILayout.uiBlockLayoutResolve(block, null, null);
//
//	if(info.popup!=0) {
//		UI.uiBlockSetFlag(block, UI.UI_BLOCK_LOOP|UI.UI_BLOCK_REDRAW|UI.UI_BLOCK_NUMSELECT|UI.UI_BLOCK_RET_1);
//		UI.uiBlockSetDirection(block, UI.UI_DOWN);
//
//		/* here we set an offset for the mouse position */
//		UI.uiMenuPopupBoundsBlock(block, 1, 0, (int)(1.5*MENU_BUTTON_HEIGHT));
//	}
//	else {
//		/* for a header menu we set the direction automatic */
//		if(info.slideout==0) {
//			sa= bContext.CTX_wm_area(C);
//			ar= bContext.CTX_wm_region(C);
//
//			if(sa!=null && sa.headertype==ScreenTypes.HEADERDOWN) {
//				if(ar!=null && ar.regiontype == ScreenTypes.RGN_TYPE_HEADER) {
//					UI.uiBlockSetDirection(block, UI.UI_TOP);
//					UI.uiBlockFlipOrder(block);
//				}
//			}
//		}
//
//		UI.uiTextBoundsBlock(block, 50);
//	}
//
//	/* if menu slides out of other menu, override direction */
//	if(info.slideout!=0)
//		UI.uiBlockSetDirection(block, UI.UI_RIGHT);
//
//	UI.uiEndBlock(C, block);
//
//	return block;
//}};

public static uiPopupBlockHandle ui_popup_menu_create(GL2 gl, bContext C, ARegion butregion, uiBut but, uiMenuCreateFunc menu_func, Object arg, String str)
{
	wmWindow window= bContext.CTX_wm_window(C);
	uiStyle style= (uiStyle)U.uistyles.first;
	uiPopupBlockHandle handle;
	uiPopupMenu pup;
//	uiMenuInfo info = new uiMenuInfo();

	pup= new uiPopupMenu();
	pup.block= UI.uiBeginBlock(C, null, "ui_popup_menu_create", UI.UI_EMBOSSP);
	pup.layout= UILayout.uiBlockLayout(pup.block, UI.UI_LAYOUT_VERTICAL, UI.UI_LAYOUT_MENU, 0, 0, 200, 0, style);
	pup.slideout= (but!=null && (but.block.flag & UI.UI_BLOCK_LOOP)!=0)?1:0;
	pup.but= but;
	UILayout.uiLayoutSetOperatorContext(pup.layout, WmTypes.WM_OP_INVOKE_REGION_WIN);

//	/* create in advance so we can let buttons point to retval already */
//	pup.block.handle= new uiPopupBlockHandle();
//
//	menu_func.run(C, pup.layout, arg);
//
////	memset(&info, 0, sizeof(info));
//	info.pup= pup;
//	info.slideout= (but!=null && (but.block.flag & UI.UI_BLOCK_LOOP)!=0)?1:0;
	
	if(but==null) {
		/* no button to start from, means we are a popup */
		pup.mx= ((wmEvent)window.eventstate).x;
		pup.my= ((wmEvent)window.eventstate).y;
		pup.popup= 1;
		pup.block.flag |= UI.UI_BLOCK_NO_FLIP;
	}

	if(str!=null) {
		/* menu is created from a string */
		pup.menu_func= ui_block_func_MENUSTR;
		pup.menu_arg= str;
	}
	else {
		/* menu is created from a callback */
		pup.menu_func= menu_func;
		pup.menu_arg= arg;
	}

//	handle= ui_popup_block_create(gl, C, butregion, but, null, ui_block_func_MENU_ITEM, info);
	handle= ui_popup_block_create(gl, C, butregion, but, null, ui_block_func_POPUP, pup);
	
	if(but==null) {
		handle.popup= 1;

//		UI_add_popup_handlers(C, window.modalhandlers, handle);
		UIHandlers.UI_add_popup_handlers(C, window.handlers, handle);
		WmEventSystem.WM_event_add_mousemove(C);
	}

//	MEM_freeN(pup);

	return handle;
}
public static uiPopupBlockHandle ui_popup_menu_create(GL2 gl, bContext C, ARegion butregion, uiBut but, uiMenuCreateFunc menu_func, Object arg, byte[] str)
{
	wmWindow window= bContext.CTX_wm_window(C);
	uiStyle style= (uiStyle)U.uistyles.first;
	uiPopupBlockHandle handle;
	uiPopupMenu pup;
	
	pup= new uiPopupMenu();
	pup.block= UI.uiBeginBlock(C, null, "ui_button_menu_create", UI.UI_EMBOSSP);
	pup.layout= UILayout.uiBlockLayout(pup.block, UI.UI_LAYOUT_VERTICAL, UI.UI_LAYOUT_MENU, 0, 0, 200, 0, style);
	pup.slideout= (but!=null && (but.block.flag & UI.UI_BLOCK_LOOP)!=0)?1:0;
	pup.but= but;
	UILayout.uiLayoutSetOperatorContext(pup.layout, WmTypes.WM_OP_INVOKE_REGION_WIN);

	if(but==null) {
		/* no button to start from, means we are a popup */
		pup.mx= ((wmEvent)window.eventstate).x;
		pup.my= ((wmEvent)window.eventstate).y;
		pup.popup= 1;
		pup.block.flag |= UI.UI_BLOCK_NO_FLIP;
	}

	if(str!=null) {
		/* menu is created from a string */
		pup.menu_func= ui_block_func_MENUSTR;
		pup.menu_arg= str;
	}
	else {
		/* menu is created from a callback */
		pup.menu_func= menu_func;
		pup.menu_arg= arg;
	}
	
	handle= ui_popup_block_create(gl, C, butregion, but, null, ui_block_func_POPUP, pup);

	if(but==null) {
		handle.popup= 1;

//		UIHandlers.UI_add_popup_handlers(C, window.modalhandlers, handle);
		UIHandlers.UI_add_popup_handlers(C, window.handlers, handle);
		WmEventSystem.WM_event_add_mousemove(C);
	}
	
//	MEM_freeN(pup);

	return handle;
}

/*************************** Menu Creating API **************************/


/*************************** Popup Menu API **************************/

/* only return handler, and set optional title */
public static uiPopupMenu uiPupMenuBegin(bContext C, String title, int icon)
{
	uiStyle style= (uiStyle)U.uistyles.first;
	uiPopupMenu pup= new uiPopupMenu();
	uiBut but;
	
	pup.block= UI.uiBeginBlock(C, null, "uiPupMenuBegin", UI.UI_EMBOSSP);
	pup.block.flag |= UI.UI_BLOCK_POPUP_MEMORY;
//	pup.block.puphash= ui_popup_menu_hash((char*)title);
	pup.layout= UILayout.uiBlockLayout(pup.block, UI.UI_LAYOUT_VERTICAL, UI.UI_LAYOUT_MENU, 0, 0, 200, 0, style);
	UILayout.uiLayoutSetOperatorContext(pup.layout, WmTypes.WM_OP_EXEC_REGION_WIN);

	/* create in advance so we can let buttons point to retval already */
	pup.block.handle= new uiPopupBlockHandle();
	
	/* create title button */
	if(title!=null && !title.isEmpty()) {
//		char titlestr[256];
        String titlestr;
		
		if(icon!=0) {
//			sprintf(titlestr, " %s", title);
            titlestr = String.format(" %s", title);
			UI.uiDefIconTextBut(pup.block, UI.LABEL, 0, BIFIconID.values()[icon], titlestr, 0, 0, 200, MENU_BUTTON_HEIGHT, null, 0.0f, 0.0f, 0, 0, "");
		}
		else {
			but= UI.uiDefBut(pup.block, UI.LABEL, 0, title, 0, 0, 200, MENU_BUTTON_HEIGHT, null, 0.0f, 0.0f, 0, 0, "");
			but.flag= UI.UI_TEXT_LEFT;
		}
	}

	return pup;
}

/* set the whole structure to work */
public static void uiPupMenuEnd(bContext C, uiPopupMenu pup)
{
	wmWindow window= bContext.CTX_wm_window(C);
//	uiMenuInfo info = new uiMenuInfo();
	uiPopupBlockHandle menu;

//	memset(&info, 0, sizeof(info));
	pup.popup= 1;
	pup.mx= ((wmEvent)window.eventstate).x;
	pup.my= ((wmEvent)window.eventstate).y;
//	info.pup= pup;

//	menu= ui_popup_block_create((GL2)GLU.getCurrentGL(), C, null, null, null, ui_block_func_MENU_ITEM, info);
	menu= ui_popup_block_create((GL2)GLU.getCurrentGL(), C, null, null, null, ui_block_func_POPUP, pup);
	menu.popup= 1;

	UIHandlers.UI_add_popup_handlers(C, window.handlers, menu);
	WmEventSystem.WM_event_add_mousemove(C);

//	MEM_freeN(pup);
}

public static uiLayout uiPupMenuLayout(uiPopupMenu pup)
{
	return pup.layout;
}

///* ************** standard pupmenus *************** */
//
///* this one can called with operatortype name and operators */
//static uiPopupBlockHandle *ui_pup_menu(bContext *C, int maxrow, uiMenuHandleFunc func, void *arg, char *str, ...)
//{
//	wmWindow *window= CTX_wm_window(C);
//	uiPupMenuInfo info;
//	uiPopupBlockHandle *menu;
//
//	memset(&info, 0, sizeof(info));
//	info.mx= window.eventstate.x;
//	info.my= window.eventstate.y;
//	info.maxrow= maxrow;
//	info.instr= str;
//
//	menu= ui_popup_block_create(C, NULL, NULL, NULL, ui_block_func_PUPMENU, &info);
//	menu.popup= 1;
//
//	UI_add_popup_handlers(C, &window.handlers, menu);
//	WM_event_add_mousemove(C);
//
//	menu.popup_func= func;
//	menu.popup_arg= arg;
//
//	return menu;
//}
//
//static void operator_name_cb(bContext *C, void *arg, int retval)
//{
//	const char *opname= arg;
//
//	if(opname && retval > 0)
//		WM_operator_name_call(C, opname, WM_OP_EXEC_DEFAULT, NULL);
//}
//
//static void vconfirm_opname(bContext *C, char *opname, char *title, char *itemfmt, va_list ap)
//{
//	char *s, buf[512];
//
//	s= buf;
//	if (title) s+= sprintf(s, "%s%%t|", title);
//	vsprintf(s, itemfmt, ap);
//
//	ui_pup_menu(C, 0, operator_name_cb, opname, buf);
//}
//
//static void operator_cb(bContext *C, void *arg, int retval)
//{
//	wmOperator *op= arg;
//
//	if(op && retval > 0)
//		WM_operator_call(C, op);
//	else
//		WM_operator_free(op);
//}
//
//static void confirm_cancel_operator(void *opv)
//{
//	WM_operator_free(opv);
//}
//
//static void confirm_operator(bContext *C, wmOperator *op, char *title, char *item)
//{
//	uiPopupBlockHandle *handle;
//	char *s, buf[512];
//
//	s= buf;
//	if (title) s+= sprintf(s, "%s%%t|%s", title, item);
//
//	handle= ui_pup_menu(C, 0, operator_cb, op, buf);
//	handle.cancel_func= confirm_cancel_operator;
//}
//
//
//void uiPupMenuOkee(bContext *C, char *opname, char *str, ...)
//{
//	va_list ap;
//	char titlestr[256];
//
//	sprintf(titlestr, "OK? %%i%d", ICON_QUESTION);
//
//	va_start(ap, str);
//	vconfirm_opname(C, opname, titlestr, str, ap);
//	va_end(ap);
//}
//
//
//void uiPupMenuSaveOver(bContext *C, wmOperator *op, char *filename)
//{
//	size_t len= strlen(filename);
//
//	if(len==0)
//		return;
//
//	if(filename[len-1]=='/' || filename[len-1]=='\\') {
//		uiPupMenuError(C, "Cannot overwrite a directory");
//		WM_operator_free(op);
//		return;
//	}
//	if(BLI_exists(filename)==0)
//		operator_cb(C, op, 1);
//	else
//		confirm_operator(C, op, "Save over", filename);
//}
//
//void uiPupMenuNotice(bContext *C, char *str, ...)
//{
//	va_list ap;
//
//	va_start(ap, str);
//	vconfirm_opname(C, NULL, NULL, str, ap);
//	va_end(ap);
//}
//
//void uiPupMenuError(bContext *C, char *str, ...)
//{
//	va_list ap;
//	char nfmt[256];
//	char titlestr[256];
//
//	sprintf(titlestr, "Error %%i%d", ICON_ERROR);
//
//	sprintf(nfmt, "%s", str);
//
//	va_start(ap, str);
//	vconfirm_opname(C, NULL, titlestr, nfmt, ap);
//	va_end(ap);
//}
//
//void uiPupMenuReports(bContext *C, ReportList *reports)
//{
//	Report *report;
//	DynStr *ds;
//	char *str;
//
//	if(!reports || !reports.list.first)
//		return;
//	if(!CTX_wm_window(C))
//		return;
//
//	ds= BLI_dynstr_new();
//
//	for(report=reports.list.first; report; report=report.next) {
//		if(report.type >= RPT_ERROR)
//			BLI_dynstr_appendf(ds, "Error %%i%d%%t|%s", ICON_ERROR, report.message);
//		else if(report.type >= RPT_WARNING)
//			BLI_dynstr_appendf(ds, "Warning %%i%d%%t|%s", ICON_ERROR, report.message);
//		else if(report.type >= RPT_INFO)
//			BLI_dynstr_appendf(ds, "Info %%t|%s", report.message);
//	}
//
//	str= BLI_dynstr_get_cstring(ds);
//	ui_pup_menu(C, 0, NULL, NULL, str);
//	MEM_freeN(str);
//
//	BLI_dynstr_free(ds);
//}
//
///*************************** Popup Block API **************************/
//
//void uiPupBlockO(bContext *C, uiBlockCreateFunc func, void *arg, char *opname, int opcontext)
//{
//	wmWindow *window= CTX_wm_window(C);
//	uiPopupBlockHandle *handle;
//
//	handle= ui_popup_block_create(C, NULL, NULL, func, NULL, arg);
//	handle.popup= 1;
//	handle.optype= (opname)? WM_operatortype_find(opname, 0): NULL;
//	handle.opcontext= opcontext;
//
//	UI_add_popup_handlers(C, &window.handlers, handle);
//	WM_event_add_mousemove(C);
//}
//
//void uiPupBlock(bContext *C, uiBlockCreateFunc func, void *arg)
//{
//	uiPupBlockO(C, func, arg, NULL, 0);
//}
//
//void uiPupBlockOperator(bContext *C, uiBlockCreateFunc func, wmOperator *op, int opcontext)
//{
//	wmWindow *window= CTX_wm_window(C);
//	uiPopupBlockHandle *handle;
//
//	handle= ui_popup_block_create(C, NULL, NULL, func, NULL, op);
//	handle.popup= 1;
//	handle.retvalue= 1;
//
//	handle.popup_arg= op;
//	handle.popup_func= operator_cb;
//	handle.cancel_func= confirm_cancel_operator;
//	handle.opcontext= opcontext;
//
//	UI_add_popup_handlers(C, &window.handlers, handle);
//	WM_event_add_mousemove(C);
//}
}