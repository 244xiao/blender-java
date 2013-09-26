/**
 * $Id: UIPanel.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation, 2003-2009 full recode.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import static blender.blenkernel.Blender.U;

import java.util.Arrays;
import java.util.Comparator;

import javax.media.opengl.GL2;

import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.blenlib.StringUtil;
import blender.blenlib.Time;
import blender.editors.screen.Area;
import blender.editors.screen.GlUtil;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceButs;
import blender.makesdna.sdna.rctf;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.uiStyle;
import blender.makesdna.sdna.wmWindow;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmWindow;
import blender.windowmanager.WmTypes.WmUIHandlerFunc;
import blender.windowmanager.WmTypes.WmUIHandlerRemoveFunc;
import blender.windowmanager.WmTypes.wmEvent;
import blender.windowmanager.WmTypes.wmTimer;

/* a full doc with API notes can be found in bf-blender/blender/doc/interface_API.txt */
 
//#include <math.h>
//#include <stdlib.h>
//#include <string.h>
//#include <ctype.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "PIL_time.h"
//
//#include "BLI_blenlib.h"
//#include "BLI_arithb.h"
//
//#include "DNA_screen_types.h"
//#include "DNA_space_types.h"
//#include "DNA_userdef_types.h"
//
//#include "BKE_context.h"
//#include "BKE_screen.h"
//#include "BKE_utildefines.h"
//
//#include "BIF_gl.h"
//#include "BIF_glutil.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "ED_screen.h"
//
//#include "UI_interface.h"
//#include "UI_view2d.h"
//
//#include "interface_intern.h"

public class UIPanel {

/*********************** defines and structs ************************/

public static final float ANIMATION_TIME=		0.30f;
public static final float ANIMATION_INTERVAL=	0.02f;

public static final int PNL_LAST_ADDED=		1;
public static final int PNL_ACTIVE=			2;
public static final int PNL_WAS_ACTIVE=		4;
public static final int PNL_ANIM_ALIGN=		8;
public static final int PNL_NEW_ADDED=		16;
public static final int PNL_FIRST=			32;

public static enum uiHandlePanelState {
	PANEL_STATE_DRAG,
	PANEL_STATE_DRAG_SCALE,
	PANEL_STATE_WAIT_UNTAB,
	PANEL_STATE_ANIMATION,
	PANEL_STATE_EXIT
};

public static class uiHandlePanelData {
	public uiHandlePanelState state;

	/* animation */
	public wmTimer animtimer;
	public double starttime;

	/* dragging */
	public int startx, starty;
	public int startofsx, startofsy;
	public int startsizex, startsizey;
};

//static void panel_activate_state(const bContext *C, Panel *pa, uiHandlePanelState state);

/*********************** space specific code ************************/
/* temporary code to remove all sbuts stuff from panel code         */

static int panel_aligned(ScrArea sa, ARegion ar)
{
	if(sa.spacetype==SpaceTypes.SPACE_BUTS && ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW) {
		SpaceButs sbuts= (SpaceButs)sa.spacedata.first;
		return sbuts.align;
	}
	else if(sa.spacetype==SpaceTypes.SPACE_USERPREF && ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW)
		return SpaceTypes.BUT_VERTICAL;
	else if(sa.spacetype==SpaceTypes.SPACE_FILE && ar.regiontype == ScreenTypes.RGN_TYPE_CHANNELS)
		return SpaceTypes.BUT_VERTICAL;
	else if(sa.spacetype==SpaceTypes.SPACE_IMAGE && ar.regiontype == ScreenTypes.RGN_TYPE_PREVIEW)
		return SpaceTypes.BUT_VERTICAL;
	else if(ar.regiontype==ScreenTypes.RGN_TYPE_UI || ar.regiontype==ScreenTypes.RGN_TYPE_TOOLS || ar.regiontype==ScreenTypes.RGN_TYPE_TOOL_PROPS)
		return SpaceTypes.BUT_VERTICAL;

	return 0;
}

static boolean panels_re_align(ScrArea sa, ARegion ar, Panel[] r_pa)
{
	Panel pa;
	int active= 0;

	r_pa[0]= null;

	if(sa.spacetype==SpaceTypes.SPACE_BUTS && ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW) {
		SpaceButs sbuts= (SpaceButs)sa.spacedata.first;

		if(sbuts.align!=0)
			if(sbuts.re_align!=0 || sbuts.mainbo!=sbuts.mainb)
				return true;
	}
	else if(ar.regiontype==ScreenTypes.RGN_TYPE_UI)
		return true;
	else if(sa.spacetype==SpaceTypes.SPACE_IMAGE && ar.regiontype == ScreenTypes.RGN_TYPE_PREVIEW)
		return true;
	else if(sa.spacetype==SpaceTypes.SPACE_FILE && ar.regiontype == ScreenTypes.RGN_TYPE_CHANNELS)
		return true;

	/* in case panel is added or disappears */
	for(pa=(Panel)ar.panels.first; pa!=null; pa=pa.next) {
		if((pa.runtime_flag & PNL_WAS_ACTIVE)!=0 && (pa.runtime_flag & PNL_ACTIVE)==0)
			return true;
		if((pa.runtime_flag & PNL_WAS_ACTIVE)==0 && (pa.runtime_flag & PNL_ACTIVE)!=0)
			return true;
		if(pa.activedata!=null)
			active= 1;
	}

	/* in case we need to do an animation (size changes) */
	for(pa=(Panel)ar.panels.first; pa!=null; pa=pa.next) {
		if((pa.runtime_flag & PNL_ANIM_ALIGN)!=0) {
			if(active==0)
				r_pa[0]= pa;
			return true;
		}
	}

	return false;
}

/****************************** panels ******************************/

static void ui_panel_copy_offset(Panel pa, Panel papar)
{
	/* with respect to sizes... papar is parent */

	pa.ofsx= papar.ofsx;
	pa.ofsy= (short)(papar.ofsy + papar.sizey-pa.sizey);
}

public static Panel uiBeginPanel(ScrArea sa, ARegion ar, uiBlock block, PanelType pt, int[] open)
{
	uiStyle style= (uiStyle)U.uistyles.first;
	Panel pa, patab, palast, panext;
	byte[] drawname= pt.label;
	byte[] idname= pt.idname;
	byte[] tabname= pt.idname;
	byte[] hookname= null;
	boolean newpanel;
	int align= panel_aligned(sa, ar);

	/* check if Panel exists, then use that one */
	for(pa=(Panel)ar.panels.first; pa!=null; pa=pa.next)
		if(StringUtil.strncmp(pa.panelname,0, idname,0, UI.UI_MAX_NAME_STR)==0)
			if(StringUtil.strncmp(pa.tabname,0, tabname,0, UI.UI_MAX_NAME_STR)==0)
				break;

	newpanel= (pa == null);

	if(!newpanel) {
		pa.type= pt;
	}
	else {
		/* new panel */
		pa= new Panel();
		pa.type= pt;
		StringUtil.BLI_strncpy(pa.panelname,0, idname,0, UI.UI_MAX_NAME_STR);
		StringUtil.BLI_strncpy(pa.tabname,0, tabname,0, UI.UI_MAX_NAME_STR);

		if((pt.flag & ScreenTypes.PNL_DEFAULT_CLOSED)!=0) {
			if(align == SpaceTypes.BUT_VERTICAL)
				pa.flag |= UI.PNL_CLOSEDY;
			else
				pa.flag |= UI.PNL_CLOSEDX;
		}

		pa.ofsx= 0;
		pa.ofsy= style.panelouter;
		pa.sizex= 0;
		pa.sizey= 0;
		pa.runtime_flag |= PNL_NEW_ADDED;

		ListBaseUtil.BLI_addtail(ar.panels, pa);

		/* make new Panel tabbed? */
		if(hookname!=null) {
			for(patab= (Panel)ar.panels.first; patab!=null; patab= patab.next) {
				if((patab.runtime_flag & PNL_ACTIVE)!=0 && patab.paneltab==null) {
					if(StringUtil.strncmp(hookname,0, patab.panelname,0, UI.UI_MAX_NAME_STR)==0) {
						if(StringUtil.strncmp(tabname,0, patab.tabname,0, UI.UI_MAX_NAME_STR)==0) {
							pa.paneltab= patab;
							ui_panel_copy_offset(pa, patab);
							break;
						}
					}
				}
			}
		}
	}

	StringUtil.BLI_strncpy(pa.drawname,0, drawname,0, UI.UI_MAX_NAME_STR);

	/* if a new panel is added, we insert it right after the panel
	 * that was last added. this way new panels are inserted in the
	 * right place between versions */
	for(palast=(Panel)ar.panels.first; palast!=null; palast=palast.next)
		if((palast.runtime_flag & PNL_LAST_ADDED)!=0)
			break;

	if(newpanel) {
		pa.sortorder= (palast!=null)? palast.sortorder+1: 0;

		for(panext=(Panel)ar.panels.first; panext!=null; panext=panext.next)
			if(panext != pa && panext.sortorder >= pa.sortorder)
				panext.sortorder++;
	}

	if(palast!=null)
		palast.runtime_flag &= ~PNL_LAST_ADDED;

	/* assign to block */
	block.panel= pa;
	pa.runtime_flag |= PNL_ACTIVE|PNL_LAST_ADDED;

	open[0]= 0;

	if(pa.paneltab!=null) return pa;
	if((pa.flag & UI.PNL_CLOSED)!=0) return pa;

	open[0]= 1;

	return pa;
}

public static void uiEndPanel(uiBlock block, int width, int height)
{
	Panel pa= block.panel;

	if((pa.runtime_flag & PNL_NEW_ADDED)!=0) {
		pa.runtime_flag &= ~PNL_NEW_ADDED;
		pa.sizex= (short)width;
		pa.sizey= (short)height;
	}
	else {
		/* check if we need to do an animation */
		if(!(width==0 || width==pa.sizex) || !(height==0 || height==pa.sizey)) {
			pa.runtime_flag |= PNL_ANIM_ALIGN;
			if(height != 0)
				pa.ofsy += pa.sizey-height;
		}

		/* update width/height if non-zero */
		if(width != 0)
			pa.sizex= (short)width;
		if(height != 0)
			pa.sizey= (short)height;
	}
}

//#if 0
//void uiPanelToMouse(const bContext *C, Panel *pa)
//{
//	/* global control over this feature; UI_PNL_TO_MOUSE only called for hotkey panels */
//	if(U.uiflag & USER_PANELPINNED);
//	else if(pa.control & UI_PNL_TO_MOUSE) {
//		int mx, my;
//
//		mx= CTX_wm_window(C).eventstate.x;
//		my= CTX_wm_window(C).eventstate.y;
//
//		pa.ofsx= mx-pa.sizex/2;
//		pa.ofsy= my-pa.sizey/2;
//
//		if(pa.flag & PNL_CLOSED) pa.flag &= ~PNL_CLOSED;
//	}
//
//	if(pa.control & UI_PNL_UNSTOW) {
//		if(pa.flag & PNL_CLOSEDY) {
//			pa.flag &= ~PNL_CLOSED;
//		}
//	}
//}
//#endif

//static boolean panel_has_tabs(ARegion ar, Panel panel)
//{
//	Panel pa= (Panel)ar.panels.first;
//
//	if(panel==null) return false;
//
//	while(pa!=null) {
//		if((pa.runtime_flag & PNL_ACTIVE)!=0 && pa.paneltab==panel) {
//			return true;
//		}
//		pa= pa.next;
//	}
//	return false;
//}

static void ui_offset_panel_block(uiBlock block)
{
	uiStyle style= (uiStyle)U.uistyles.first;
	uiBut but;
	int ofsy;

	/* compute bounds and offset */
	UI.ui_bounds_block(block);

	ofsy= block.panel.sizey - style.panelspace;

	for(but= block.buttons.first; but!=null; but=but.next) {
		but.y1 += ofsy;
		but.y2 += ofsy;
	}

	block.maxx= block.panel.sizex;
	block.maxy= block.panel.sizey;
	block.minx= block.miny= 0.0f;
}

///**************************** drawing *******************************/
//
///* extern used by previewrender */
//void uiPanelPush(uiBlock *block)
//{
//	glPushMatrix();
//
//	if(block.panel)
//		glTranslatef((float)block.panel.ofsx, (float)block.panel.ofsy, 0.0);
//}
//
//void uiPanelPop(uiBlock *block)
//{
//	glPopMatrix();
//}
//
///* triangle 'icon' for panel header */
//void ui_draw_tria_icon(float x, float y, char dir)
//{
//	if(dir=='h') {
//		ui_draw_anti_tria(x-1, y, x-1, y+11.0, x+9, y+6.25);
//	}
//	else {
//		ui_draw_anti_tria(x-3, y+10,  x+8-1, y+10, x+4.25-2, y);
//	}
//}

/* triangle 'icon' inside rect */
public static void ui_draw_tria_rect(GL2 gl, rctf rect, char dir)
{
	if(dir=='h') {
		float half= 0.5f*(rect.ymax - rect.ymin);
		UIWidgets.ui_draw_anti_tria(gl, rect.xmin, rect.ymin, rect.xmin, rect.ymax, rect.xmax, rect.ymin+half);
	}
	else {
		float half= 0.5f*(rect.xmax - rect.xmin);
		UIWidgets.ui_draw_anti_tria(gl, rect.xmin, rect.ymax, rect.xmax, rect.ymax, rect.xmin+half, rect.ymin);
	}
}

public static void ui_draw_anti_x(GL2 gl, float x1, float y1, float x2, float y2)
{

	/* set antialias line */
	gl.glEnable(GL2.GL_LINE_SMOOTH);
	gl.glEnable(GL2.GL_BLEND);

	gl.glLineWidth(2.0f);

	GlUtil.fdrawline(gl, x1, y1, x2, y2);
	GlUtil.fdrawline(gl, x1, y2, x2, y1);

	gl.glLineWidth(1.0f);

	gl.glDisable(GL2.GL_LINE_SMOOTH);
	gl.glDisable(GL2.GL_BLEND);

}

/* x 'icon' for panel header */
static void ui_draw_x_icon(GL2 gl, float x, float y)
{

	ui_draw_anti_x(gl, x, y, x+9.375f, y+9.375f);

}

public static final int PNL_ICON= 	20;

static void ui_draw_panel_scalewidget(GL2 gl, rcti rect)
{
	float xmin, xmax, dx;
	float ymin, ymax, dy;

	xmin= rect.xmax-UI.PNL_HEADER+2;
	xmax= rect.xmax-3;
	ymin= rect.ymin+3;
	ymax= rect.ymin+UI.PNL_HEADER-2;

	dx= 0.5f*(xmax-xmin);
	dy= 0.5f*(ymax-ymin);

	gl.glEnable(GL2.GL_BLEND);
	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)50);
	GlUtil.fdrawline(gl, xmin, ymin, xmax, ymax);
	GlUtil.fdrawline(gl, xmin+dx, ymin, xmax, ymax-dy);

	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)50);
	GlUtil.fdrawline(gl, xmin, ymin+1, xmax, ymax+1);
	GlUtil.fdrawline(gl, xmin+dx, ymin+1, xmax, ymax-dy+1);
	gl.glDisable(GL2.GL_BLEND);
}

static void ui_draw_panel_dragwidget(GL2 gl, rctf rect)
{
	float xmin, xmax, dx;
	float ymin, ymax, dy;

	xmin= rect.xmin;
	xmax= rect.xmax;
	ymin= rect.ymin;
	ymax= rect.ymax;

	dx= 0.333f*(xmax-xmin);
	dy= 0.333f*(ymax-ymin);

	gl.glEnable(GL2.GL_BLEND);
	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)50);
	GlUtil.fdrawline(gl, xmin, ymax, xmax, ymin);
	GlUtil.fdrawline(gl, xmin+dx, ymax, xmax, ymin+dy);
	GlUtil.fdrawline(gl, xmin+2*dx, ymax, xmax, ymin+2*dy);

	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)50);
	GlUtil.fdrawline(gl, xmin, ymax+1, xmax, ymin+1);
	GlUtil.fdrawline(gl, xmin+dx, ymax+1, xmax, ymin+dy+1);
	GlUtil.fdrawline(gl, xmin+2*dx, ymax+1, xmax, ymin+2*dy+1);
	gl.glDisable(GL2.GL_BLEND);
}


static void ui_draw_aligned_panel_header(GL2 gl, uiStyle style, uiBlock block, rcti rect, char dir)
{
	Panel panel= block.panel;
	rcti hrect = new rcti();
	int pnl_icons;
	byte[] activename= panel.drawname[0]!=0?panel.drawname:panel.panelname;
	
	/* + 0.001f to avoid flirting with float inaccuracy */
	if((panel.control & UI.UI_PNL_CLOSE)!=0) pnl_icons=(int)((panel.labelofs+2*PNL_ICON+5)/block.aspect + 0.001f);
	else pnl_icons= (int)((panel.labelofs+PNL_ICON+5)/block.aspect + 0.001f);
	
	/* active tab */
	/* draw text label */
	Resources.UI_ThemeColor(gl, Resources.TH_TITLE);
	
	hrect= rect.copy();
	if(dir == 'h') {
		hrect.xmin= rect.xmin+pnl_icons;
		UIStyle.uiStyleFontDraw(style.paneltitle, hrect, activename,0);
	}
	else {
		/* ignore 'pnl_icons', otherwise the text gets offset horizontally 
		 * + 0.001f to avoid flirting with float inaccuracy
		 */
		hrect.xmin= (int)(rect.xmin + (PNL_ICON+5)/block.aspect + 0.001f);
		UIStyle.uiStyleFontDrawRotated(style.paneltitle, hrect, activename,0);
	}
}

static void rectf_scale(rctf rect, float scale)
{
	float centx= 0.5f*(rect.xmin+rect.xmax);
	float centy= 0.5f*(rect.ymin+rect.ymax);
	float sizex= 0.5f*scale*(rect.xmax - rect.xmin);
	float sizey= 0.5f*scale*(rect.ymax - rect.ymin);

	rect.xmin= centx - sizex;
	rect.xmax= centx + sizex;
	rect.ymin= centy - sizey;
	rect.ymax= centy + sizey;
}

/* panel integrated in buttonswindow, tool/property lists etc */
public static void ui_draw_aligned_panel(GL2 gl, uiStyle style, uiBlock block, rcti rect)
{
	Panel panel= block.panel;
	rcti headrect = new rcti();
	rctf itemrect = new rctf();
	int ofsx;

	if(panel.paneltab!=null)
            return;
	if(panel.type!=null && (((PanelType)panel.type).flag & ScreenTypes.PNL_NO_HEADER)!=0)
            return;

	/* calculate header rect */
	/* + 0.001f to prevent flicker due to float inaccuracy */
	headrect= rect.copy();
	headrect.ymin= headrect.ymax;
	headrect.ymax= (int)(headrect.ymin + StrictMath.floor(UI.PNL_HEADER/block.aspect + 0.001f));

	if((panel.runtime_flag & PNL_FIRST)==0) {
		float minx= rect.xmin+5.0f/block.aspect;
		float maxx= rect.xmax-5.0f/block.aspect;
		float y= headrect.ymax;

		gl.glEnable(GL2.GL_BLEND);
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
		GlUtil.fdrawline(gl, minx, y+1, maxx, y+1);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.25f);
		GlUtil.fdrawline(gl, minx, y, maxx, y);
		gl.glDisable(GL2.GL_BLEND);
	}

	/* horizontal title */
	if((panel.flag & UI.PNL_CLOSEDX)==0) {
		ui_draw_aligned_panel_header(gl, style, block, headrect, 'h');

		/* itemrect smaller */
		itemrect.xmax= headrect.xmax - 5.0f/block.aspect;
		itemrect.xmin= itemrect.xmax - (headrect.ymax-headrect.ymin);
		itemrect.ymin= headrect.ymin;
		itemrect.ymax= headrect.ymax;
		rectf_scale(itemrect, 0.8f);
		ui_draw_panel_dragwidget(gl, itemrect);
	}

	/* if the panel is minimized vertically:
		* (------)
		*/
	if((panel.flag & UI.PNL_CLOSEDY)!=0) {

	}
	else if((panel.flag & UI.PNL_CLOSEDX)!=0) {
		/* draw vertical title */
		ui_draw_aligned_panel_header(gl, style, block, headrect, 'v');
	}
	/* an open panel */
	else {
		
		/* in some occasions, draw a border */
		if((panel.flag & UI.PNL_SELECT)!=0) {
			if((panel.control & UI.UI_PNL_SOLID)!=0)
                            UIDraw.uiSetRoundBox(15);
			else
                            UIDraw.uiSetRoundBox(3);

			Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -120);
			UIDraw.uiRoundRect(gl, 0.5f + rect.xmin, 0.5f + rect.ymin, 0.5f + rect.xmax, 0.5f + headrect.ymax+1, 8);
		}

		if((panel.control & UI.UI_PNL_SCALE)!=0)
			ui_draw_panel_scalewidget(gl, rect);
	}

	/* draw optional close icon */

	ofsx= 6;
	if((panel.control & UI.UI_PNL_CLOSE)!=0) {

		Resources.UI_ThemeColor(gl, Resources.TH_TEXT);
		ui_draw_x_icon(gl, rect.xmin+2+ofsx, rect.ymax+2);
		ofsx= 22;
	}

	/* draw collapse icon */
	Resources.UI_ThemeColor(gl, Resources.TH_TEXT);

	/* itemrect smaller */
	itemrect.xmin= headrect.xmin + 5.0f/block.aspect;
	itemrect.xmax= itemrect.xmin + (headrect.ymax-headrect.ymin);
	itemrect.ymin= headrect.ymin;
	itemrect.ymax= headrect.ymax;

	rectf_scale(itemrect, 0.5f);

	if((panel.flag & UI.PNL_CLOSEDY)!=0)
		ui_draw_tria_rect(gl, itemrect, 'h');
	else if((panel.flag & UI.PNL_CLOSEDX)!=0)
		ui_draw_tria_rect(gl, itemrect, 'h');
	else
		ui_draw_tria_rect(gl, itemrect, 'v');
}

/************************** panel alignment *************************/

static int get_panel_header(Panel pa)
{
	if(pa.type!=null && (((PanelType)pa.type).flag & ScreenTypes.PNL_NO_HEADER)!=0)
		return 0;

	return UI.PNL_HEADER;
}

static int get_panel_size_y(uiStyle style, Panel pa)
{
	if(pa.type!=null && (((PanelType)pa.type).flag & ScreenTypes.PNL_NO_HEADER)!=0)
		return pa.sizey;

	return UI.PNL_HEADER + pa.sizey + style.panelouter;
}

/* this function is needed because uiBlock and Panel itself dont
change sizey or location when closed */
static int get_panel_real_ofsy(Panel pa)
{
	if((pa.flag & UI.PNL_CLOSEDY)!=0) return pa.ofsy+pa.sizey;
	else if(pa.paneltab!=null && (pa.paneltab.flag & UI.PNL_CLOSEDY)!=0) return pa.ofsy+pa.sizey;
	else if(pa.paneltab!=null) return pa.paneltab.ofsy;
	else return pa.ofsy;
}

static int get_panel_real_ofsx(Panel pa)
{
	if((pa.flag & UI.PNL_CLOSEDX)!=0) return pa.ofsx+get_panel_header(pa);
	else if(pa.paneltab!=null && (pa.paneltab.flag & UI.PNL_CLOSEDX)!=0) return pa.ofsx+get_panel_header(pa);
	else return pa.ofsx+pa.sizex;
}

public static class PanelSort {
	public Panel pa, orig;
};

/* note about sorting;
   the sortorder has a lower value for new panels being added.
   however, that only works to insert a single panel, when more new panels get
   added the coordinates of existing panels and the previously stored to-be-insterted
   panels do not match for sorting */

public static Comparator<PanelSort> find_leftmost_panel = new Comparator<PanelSort>() {
    public int compare(PanelSort a1, PanelSort a2) {
        PanelSort ps1=a1, ps2=a2;

        if( ps1.pa.ofsx > ps2.pa.ofsx) return 1;
        else if( ps1.pa.ofsx < ps2.pa.ofsx) return -1;
        else if( ps1.pa.sortorder > ps2.pa.sortorder) return 1;
        else if( ps1.pa.sortorder < ps2.pa.sortorder) return -1;

        return 0;
    }
};

public static Comparator<PanelSort> find_highest_panel = new Comparator<PanelSort>() {
    public int compare(PanelSort a1, PanelSort a2) {
        PanelSort ps1=a1, ps2=a2;
        
        /* stick uppermost header-less panels to the top of the region -
    	 * prevent them from being sorted */
    	if (ps1.pa.sortorder < ps2.pa.sortorder && (((PanelType)ps1.pa.type).flag & ScreenTypes.PNL_NO_HEADER)!=0) return -1;

        if( ps1.pa.ofsy+ps1.pa.sizey < ps2.pa.ofsy+ps2.pa.sizey) return 1;
        else if( ps1.pa.ofsy+ps1.pa.sizey > ps2.pa.ofsy+ps2.pa.sizey) return -1;
        else if( ps1.pa.sortorder > ps2.pa.sortorder) return 1;
        else if( ps1.pa.sortorder < ps2.pa.sortorder) return -1;

        return 0;
    }
};

public static Comparator<PanelSort> compare_panel = new Comparator<PanelSort>() {
    public int compare(PanelSort a1, PanelSort a2) {
        PanelSort ps1=a1, ps2=a2;

	if(ps1.pa.sortorder > ps2.pa.sortorder) return 1;
	else if(ps1.pa.sortorder < ps2.pa.sortorder) return -1;

        return 0;
    }
};

/* this doesnt draw */
/* returns 1 when it did something */
public static int uiAlignPanelStep(ScrArea sa, ARegion ar, float fac, boolean drag)
{
	uiStyle style= (uiStyle)U.uistyles.first;
	Panel pa;
    PanelSort[] panelsort;
    PanelSort ps, psnext;
    int ps_p;
	int a, tot=0, done;
	int align= panel_aligned(sa, ar);

	/* count active, not tabbed panels */
	for(pa= (Panel)ar.panels.first; pa!=null; pa= pa.next)
		if((pa.runtime_flag & PNL_ACTIVE)!=0 && pa.paneltab==null)
			tot++;

	if(tot==0) return 0;

	/* extra; change close direction? */
	for(pa= (Panel)ar.panels.first; pa!=null; pa= pa.next) {
		if((pa.runtime_flag & PNL_ACTIVE)!=0 && pa.paneltab==null) {
			if((pa.flag & UI.PNL_CLOSEDX)!=0 && (align==SpaceTypes.BUT_VERTICAL))
				pa.flag ^= UI.PNL_CLOSED;
			else if((pa.flag & UI.PNL_CLOSEDY)!=0 && (align==SpaceTypes.BUT_HORIZONTAL))
				pa.flag ^= UI.PNL_CLOSED;
		}
	}

	/* sort panels */
    panelsort = new PanelSort[tot];
    for (int i=0; i<tot; i++) panelsort[i] = new PanelSort();

//	ps= panelsort;
    ps_p=0;
	for(pa= (Panel)ar.panels.first; pa!=null; pa= pa.next) {
		if((pa.runtime_flag & PNL_ACTIVE)!=0 && pa.paneltab==null) {
            ps = panelsort[ps_p];
//			ps.pa= MEM_dupallocN(pa);
//            ps.pa= pa.copy();
            ps.pa= panelCopy(pa);
			ps.orig= pa;
			ps_p++;
		}
	}

	if(drag) {
		/* while we are dragging, we sort on location and update sortorder */
		if(align==SpaceTypes.BUT_VERTICAL)
//			qsort(panelsort, tot, sizeof(PanelSort), find_highest_panel);
            Arrays.sort(panelsort, find_highest_panel);
		else
//			qsort(panelsort, tot, sizeof(PanelSort), find_leftmost_panel);
            Arrays.sort(panelsort, find_leftmost_panel);

		for(ps_p=0, a=0; a<tot; a++, ps_p++) {
            ps = panelsort[ps_p];
			ps.orig.sortorder= a;
        }
	}
	else
		/* otherwise use sortorder */
//		qsort(panelsort, tot, sizeof(PanelSort), compare_panel);
        Arrays.sort(panelsort, compare_panel);

	/* no smart other default start loc! this keeps switching f5/f6/etc compatible */
//	ps= panelsort;
    ps = panelsort[ps_p=0];
	ps.pa.ofsx= 0;
	ps.pa.ofsy= (short)(-get_panel_size_y(style, ps.pa));

	for(a=0; a<tot-1; a++, ps_p++) {
        ps = panelsort[ps_p];
		psnext= panelsort[ps_p+1];

		if(align==SpaceTypes.BUT_VERTICAL) {
			psnext.pa.ofsx= ps.pa.ofsx;
			psnext.pa.ofsy= (short)(get_panel_real_ofsy(ps.pa) - get_panel_size_y(style, psnext.pa));
		}
		else {
			psnext.pa.ofsx= (short)get_panel_real_ofsx(ps.pa);
			psnext.pa.ofsy= (short)(ps.pa.ofsy + get_panel_size_y(style, ps.pa) - get_panel_size_y(style, psnext.pa));
		}
	}

	/* we interpolate */
	done= 0;
//	ps= panelsort;
    ps_p=0;
	for(a=0; a<tot; a++, ps_p++) {
        ps = panelsort[ps_p];
		if((ps.pa.flag & UI.PNL_SELECT)==0) {
			if((ps.orig.ofsx != ps.pa.ofsx) || (ps.orig.ofsy != ps.pa.ofsy)) {
				ps.orig.ofsx= (short)StrictMath.floor(0.5 + fac*ps.pa.ofsx + (1.0-fac)*ps.orig.ofsx);
				ps.orig.ofsy= (short)StrictMath.floor(0.5 + fac*ps.pa.ofsy + (1.0-fac)*ps.orig.ofsy);
				done= 1;
			}
		}
	}

	/* copy locations to tabs */
	for(pa= (Panel)ar.panels.first; pa!=null; pa= pa.next)
		if(pa.paneltab!=null && (pa.runtime_flag & PNL_ACTIVE)!=0)
			ui_panel_copy_offset(pa, pa.paneltab);

	/* free panelsort array */
//	for(ps= panelsort, a=0; a<tot; a++, ps++)
//		MEM_freeN(ps.pa);
//	MEM_freeN(panelsort);

	return done;
}


static void ui_do_animate(bContext C, Panel panel)
{
//	System.out.println("ui_do_animate");
	uiHandlePanelData data= (uiHandlePanelData)panel.activedata;
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	float fac;

	fac= (float)((Time.PIL_check_seconds_timer()-data.starttime)/ANIMATION_TIME);
	fac= (float)Math.sqrt(fac);
	fac= UtilDefines.MIN2(fac, 1.0f);

	/* for max 1 second, interpolate positions */
	if(uiAlignPanelStep(sa, ar, fac, false)!=0)
		Area.ED_region_tag_redraw(ar);
	else
		fac= 1.0f;

	if(fac >= 1.0f) {
		panel_activate_state(C, panel, uiHandlePanelState.PANEL_STATE_EXIT);
		return;
	}
}

public static void uiBeginPanels(bContext C, ARegion ar)
{
	Panel pa;

  	/* set all panels as inactive, so that at the end we know
	 * which ones were used */
	for(pa=(Panel)ar.panels.first; pa!=null; pa=pa.next) {
		if((pa.runtime_flag & PNL_ACTIVE)!=0)
			pa.runtime_flag= PNL_WAS_ACTIVE;
		else
			pa.runtime_flag= 0;
	}
}

/* only draws blocks with panels */
public static void uiEndPanels(GL2 gl, bContext C, ARegion ar)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	uiBlock block;
	Panel panot, panew, patest, firstpa;
    Panel[] pa = {null};

	/* offset contents */
	for(block= (uiBlock)ar.uiblocks.first; block!=null; block= block.next)
		if(block.active!=0 && block.panel!=null)
			ui_offset_panel_block(block);

	/* consistancy; are panels not made, whilst they have tabs */
	for(panot= (Panel)ar.panels.first; panot!=null; panot= panot.next) {
		if((panot.runtime_flag & PNL_ACTIVE)==0) { // not made

			for(panew= (Panel)ar.panels.first; panew!=null; panew= panew.next) {
				if((panew.runtime_flag & PNL_ACTIVE)!=0) {
					if(panew.paneltab==panot) { // panew is tab in notmade pa
						break;
					}
				}
			}
			/* now panew can become the new parent, check all other tabs */
			if(panew!=null) {
				for(patest= (Panel)ar.panels.first; patest!=null; patest= patest.next) {
					if(patest.paneltab == panot) {
						patest.paneltab= panew;
					}
				}
				panot.paneltab= panew;
				panew.paneltab= null;
				Area.ED_region_tag_redraw(ar); // the buttons panew were not made
			}
		}
	}

	/* re-align, possibly with animation */
	if(panels_re_align(sa, ar, pa)) {
		if(pa[0]!=null)
			panel_activate_state(C, pa[0], uiHandlePanelState.PANEL_STATE_ANIMATION);
		else
			uiAlignPanelStep(sa, ar, 1.0f, false);
	}

	/* tag first panel */
	firstpa= null;
	for(block= (uiBlock)ar.uiblocks.first; block!=null; block=block.next)
		if(block.active!=0 && block.panel!=null)
			if(firstpa==null || block.panel.sortorder < firstpa.sortorder)
				firstpa= block.panel;

	if(firstpa!=null)
		firstpa.runtime_flag |= PNL_FIRST;
	
	Resources.UI_ThemeClearColor(gl, Resources.TH_BACK);

	/* draw panels, selected on top */
	for(block= (uiBlock)ar.uiblocks.first; block!=null; block=block.next) {
		if(block.active!=0 && block.panel!=null && (block.panel.flag & UI.PNL_SELECT)==0) {
			UI.uiDrawBlock(gl, C, block);
		}
	}

	for(block= (uiBlock)ar.uiblocks.first; block!=null; block=block.next) {
		if(block.active!=0 && block.panel!=null && (block.panel.flag & UI.PNL_SELECT)!=0) {
			UI.uiDrawBlock(gl, C, block);
		}
	}
}

/* ------------ panel merging ---------------- */

static void check_panel_overlap(ARegion ar, Panel panel)
{
	Panel pa;

	/* also called with panel==NULL for clear */

	for(pa=(Panel)ar.panels.first; pa!=null; pa=pa.next) {
		pa.flag &= ~UI.PNL_OVERLAP;
		if(panel!=null && (pa != panel)) {
			if(pa.paneltab==null && (pa.runtime_flag & PNL_ACTIVE)!=0) {
				float safex= 0.2f, safey= 0.2f;

				if((pa.flag & UI.PNL_CLOSEDX)!=0) safex= 0.05f;
				else if((pa.flag & UI.PNL_CLOSEDY)!=0) safey= 0.05f;
				else if((panel.flag & UI.PNL_CLOSEDX)!=0) safex= 0.05f;
				else if((panel.flag & UI.PNL_CLOSEDY)!=0) safey= 0.05f;

				if(pa.ofsx > panel.ofsx- safex*panel.sizex)
				if(pa.ofsx+pa.sizex < panel.ofsx+ (1.0+safex)*panel.sizex)
				if(pa.ofsy > panel.ofsy- safey*panel.sizey)
				if(pa.ofsy+pa.sizey < panel.ofsy+ (1.0+safey)*panel.sizey)
					pa.flag |= UI.PNL_OVERLAP;
			}
		}
	}
}

//static void test_add_new_tabs(ARegion ar)
//{
//	Panel pa, pasel=null, palap=null;
//	/* search selected and overlapped panel */
//
//	pa= (Panel)ar.panels.first;
//	while(pa!=null) {
//		if((pa.runtime_flag & PNL_ACTIVE)!=0) {
//			if((pa.flag & UI.PNL_SELECT)!=0)  pasel= pa;
//			if((pa.flag & UI.PNL_OVERLAP)!=0)  palap= pa;
//		}
//		pa= pa.next;
//	}
//
//	if(pasel!=null && palap==null) {
//
//		/* copy locations */
//		pa= (Panel)ar.panels.first;
//		while(pa!=null) {
//			if(pa.paneltab==pasel) {
//				ui_panel_copy_offset(pa, pasel);
//			}
//			pa= pa.next;
//		}
//	}
//
//	if(pasel==null || palap==null) return;
//
//	/* the overlapped panel becomes a tab */
//	palap.paneltab= pasel;
//
//	/* the selected panel gets coords of overlapped one */
//	ui_panel_copy_offset(pasel, palap);
//
//	/* and its tabs */
//	pa= (Panel)ar.panels.first;
//	while(pa!=null) {
//		if(pa.paneltab == pasel) {
//			ui_panel_copy_offset(pa, palap);
//		}
//		pa= pa.next;
//	}
//
//	/* but, the overlapped panel already can have tabs too! */
//	pa= (Panel)ar.panels.first;
//	while(pa!=null) {
//		if(pa.paneltab == palap) {
//			pa.paneltab = pasel;
//		}
//		pa= pa.next;
//	}
//}

/************************ panel dragging ****************************/

static void ui_do_drag(bContext C, wmEvent event, Panel panel)
{
	uiHandlePanelData data= (uiHandlePanelData)panel.activedata;
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	short align= (short)panel_aligned(sa, ar), dx=0, dy=0;

	/* first clip for window, no dragging outside */
	if(!Rct.BLI_in_rcti(ar.winrct, event.x, event.y))
		return;

	dx= (short)((event.x-data.startx) & ~(UI.PNL_GRID-1));
	dy= (short)((event.y-data.starty) & ~(UI.PNL_GRID-1));

	dx *= (float)(ar.v2d.cur.xmax - ar.v2d.cur.xmin)/(float)(ar.winrct.xmax - ar.winrct.xmin);
	dy *= (float)(ar.v2d.cur.ymax - ar.v2d.cur.ymin)/(float)(ar.winrct.ymax - ar.winrct.ymin);

	if(data.state == uiHandlePanelState.PANEL_STATE_DRAG_SCALE) {
		panel.sizex = (short)UtilDefines.MAX2(data.startsizex+dx, UI.UI_PANEL_MINX);

		if(data.startsizey-dy < UI.UI_PANEL_MINY)
			dy= (short)(-UI.UI_PANEL_MINY+data.startsizey);

		panel.sizey= (short)(data.startsizey-dy);
		panel.ofsy= (short)(data.startofsy+dy);
	}
	else {
		/* reset the panel snapping, to allow dragging away from snapped edges */
		panel.snap = ScreenTypes.PNL_SNAP_NONE;

		panel.ofsx = (short)(data.startofsx+dx);
		panel.ofsy = (short)(data.startofsy+dy);
		check_panel_overlap(ar, panel);

		if(align!=0) uiAlignPanelStep(sa, ar, 0.2f, true);
	}

	Area.ED_region_tag_redraw(ar);
}

/******************* region level panel interaction *****************/

/* this function is supposed to call general window drawing too */
/* also it supposes a block has panel, and isnt a menu */
static void ui_handle_panel_header(bContext C, uiBlock block, int mx, int my, int event)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	Panel pa;
	int align= panel_aligned(sa, ar), button= 0;

	/* mouse coordinates in panel space! */

	/* XXX weak code, currently it assumes layout style for location of widgets */

	/* check open/collapsed button */
	if(event==WmEventTypes.RETKEY)
		button= 1;
	else if(event==WmEventTypes.AKEY)
		button= 1;
	else if((block.panel.flag & UI.PNL_CLOSEDX)!=0) {
		if(my >= block.maxy) button= 1;
	}
	else if((block.panel.control & UI.UI_PNL_CLOSE)!=0) {
		/* whole of header can be used to collapse panel (except top-right corner) */
//		if(mx <= block.minx+10+PNL_ICON-2) button= 2;
		if(mx <= block.maxx-8-PNL_ICON) button= 2;
		//else if(mx <= block.minx+10+2*PNL_ICON+2) button= 1;
	}
//	else if(mx <= block.minx+10+PNL_ICON+2) {
	else if(mx <= block.maxx-PNL_ICON-12) {
		button= 1;
	}

	if(button!=0) {
		if(button==2) { // close
			Area.ED_region_tag_redraw(ar);
		}
		else {	// collapse
			if((block.panel.flag & UI.PNL_CLOSED)!=0) {
				block.panel.flag &= ~UI.PNL_CLOSED;
				/* snap back up so full panel aligns with screen edge */
				if ((block.panel.snap & ScreenTypes.PNL_SNAP_BOTTOM)!=0)
					block.panel.ofsy= 0;
			}
			else if(align==SpaceTypes.BUT_HORIZONTAL) {
				block.panel.flag |= UI.PNL_CLOSEDX;
			}
			else {
				/* snap down to bottom screen edge*/
				block.panel.flag |= UI.PNL_CLOSEDY;
				if ((block.panel.snap & ScreenTypes.PNL_SNAP_BOTTOM)!=0) {
					block.panel.ofsy= (short)(-block.panel.sizey);
				}
			}

			for(pa= (Panel)ar.panels.first; pa!=null; pa= pa.next) {
				if(pa.paneltab==block.panel) {
					if((block.panel.flag & UI.PNL_CLOSED)!=0) pa.flag |= UI.PNL_CLOSED;
					else pa.flag &= ~UI.PNL_CLOSED;
				}
			}
		}

		if(align!=0)
			panel_activate_state(C, block.panel, uiHandlePanelState.PANEL_STATE_ANIMATION);
		else
			Area.ED_region_tag_redraw(ar);
	}
	else if(mx <= (block.maxx-PNL_ICON-12)+PNL_ICON+2) {
		panel_activate_state(C, block.panel, uiHandlePanelState.PANEL_STATE_DRAG);
	}
}

/* XXX should become modal keymap */
/* AKey is opening/closing panels, independent of button state now */

public static int ui_handler_panel_region(bContext C, wmEvent event)
{
	ARegion ar= bContext.CTX_wm_region(C);
	uiBlock block;
	Panel pa;
	int retval, inside_header= 0, inside_scale= 0, inside;
    int[] mx={0}, my={0};

	retval= WmTypes.WM_UI_HANDLER_CONTINUE;
	for(block=(uiBlock)ar.uiblocks.last; block!=null; block=block.prev) {
		mx[0]= event.x;
		my[0]= event.y;
		UI.ui_window_to_block(ar, block, mx, my);

		/* check if inside boundbox */
		inside= 0;
		pa= block.panel;

		if(pa==null || pa.paneltab!=null)
			continue;
		if(pa.type!=null && (((PanelType)pa.type).flag & ScreenTypes.PNL_NO_HEADER)!=0) // XXX - accessed freed panels when scripts reload, need to fix.
			continue;

		if(block.minx <= mx[0] && block.maxx >= mx[0])
			if(block.miny <= my[0] && block.maxy+UI.PNL_HEADER >= my[0])
				inside= 1;
		
		if(inside!=0 && event.val==WmTypes.KM_PRESS) {
			if(event.type == WmEventTypes.AKEY && !(1 == event.ctrl || 1 == event.oskey || 1 == event.shift || 1 == event.alt)) {
				
				if((pa.flag & UI.PNL_CLOSEDY)!=0) {
					if((block.maxy <= my[0]) && (block.maxy+UI.PNL_HEADER >= my[0]))
						ui_handle_panel_header(C, block, mx[0], my[0], event.type);
				}
				else
					ui_handle_panel_header(C, block, mx[0], my[0], event.type);
				
				continue;
			}
		}
		
		/* on active button, do not handle panels */
		if(UIHandlers.ui_button_is_active(ar))
			continue;

		if(inside!=0) {
			/* clicked at panel header? */
			if((pa.flag & UI.PNL_CLOSEDX)!=0) {
				if(block.minx <= mx[0] && block.minx+UI.PNL_HEADER >= mx[0])
					inside_header= 1;
			}
			else if((block.maxy <= my[0]) && (block.maxy+UI.PNL_HEADER >= my[0])) {
				inside_header= 1;
			}
			else if((pa.control & UI.UI_PNL_SCALE)!=0) {
				if(block.maxx-UI.PNL_HEADER <= mx[0])
					if(block.miny+UI.PNL_HEADER >= my[0])
						inside_scale= 1;
			}

			if(event.val==WmTypes.KM_PRESS) {
				/* open close on header */
				if((event.type == WmEventTypes.RETKEY || event.type == WmEventTypes.PADENTER)) {
					if(inside_header!=0) {
						ui_handle_panel_header(C, block, mx[0], my[0], WmEventTypes.RETKEY);
						break;
					}
				}
				if(event.type == WmEventTypes.LEFTMOUSE) {
					if(inside_header!=0) {
						ui_handle_panel_header(C, block, mx[0], my[0], 0);
						break;
					}
					else if(inside_scale!=0 && (pa.flag & UI.PNL_CLOSED)==0) {
						panel_activate_state(C, pa, uiHandlePanelState.PANEL_STATE_DRAG_SCALE);
						break;
					}
				}
//				else if(event.type == WmEventTypes.ESCKEY) {
//					/*XXX 2.50 if(block.handler) {
//						rem_blockhandler(sa, block.handler);
//						ED_region_tag_redraw(ar);
//						retval= WM_UI_HANDLER_BREAK;
//					}*/
//				}
//				else if(event.type==WmEventTypes.PADPLUSKEY || event.type==WmEventTypes.PADMINUS) {
//					int zoom=0;
//
//					/* if panel is closed, only zoom if mouse is over the header */
//					if (pa.flag & (PNL_CLOSEDX|PNL_CLOSEDY)) {
//						if (inside_header)
//							zoom=1;
//					}
//					else
//						zoom=1;
//
//#if 0 // XXX make float panel exception?
//					if(zoom) {
//						ScrArea *sa= CTX_wm_area(C);
//						SpaceLink *sl= sa.spacedata.first;
//
//						if(sa.spacetype!=SPACE_BUTS) {
//							if(!(pa.control & UI_PNL_SCALE)) {
//								if(event.type==PADPLUSKEY) sl.blockscale+= 0.1;
//								else sl.blockscale-= 0.1;
//								CLAMP(sl.blockscale, 0.6, 1.0);
//
//								ED_region_tag_redraw(ar);
//								retval= WM_UI_HANDLER_BREAK;
//							}
//						}
//					}
//#endif
//				}
			}
		}
	}

	return retval;
}

/**************** window level modal panel interaction **************/

/* note, this is modal handler and should not swallow events for animation */
public static WmUIHandlerFunc ui_handler_panel = new WmUIHandlerFunc() {
public int run(bContext C, wmEvent event, Object userdata)
{
	Panel panel= (Panel)userdata;
	uiHandlePanelData data= (uiHandlePanelData)panel.activedata;

	/* verify if we can stop */
	if(event.type == WmEventTypes.LEFTMOUSE && event.val!=WmTypes.KM_PRESS) {
		ScrArea sa= bContext.CTX_wm_area(C);
		ARegion ar= bContext.CTX_wm_region(C);
		int align= panel_aligned(sa, ar);

		if(align!=0)
			panel_activate_state(C, panel, uiHandlePanelState.PANEL_STATE_ANIMATION);
		else
			panel_activate_state(C, panel, uiHandlePanelState.PANEL_STATE_EXIT);
	}
	else if(event.type == WmEventTypes.MOUSEMOVE) {
		if(data.state == uiHandlePanelState.PANEL_STATE_DRAG)
			ui_do_drag(C, event, panel);
	}
	else if(event.type == WmEventTypes.TIMER && event.customdata == data.animtimer) {
		if(data.state == uiHandlePanelState.PANEL_STATE_ANIMATION)
			ui_do_animate(C, panel);
		else if(data.state == uiHandlePanelState.PANEL_STATE_DRAG)
			ui_do_drag(C, event, panel);
	}

	data= (uiHandlePanelData)panel.activedata;

	if(data!=null && data.state == uiHandlePanelState.PANEL_STATE_ANIMATION)
		return WmTypes.WM_UI_HANDLER_CONTINUE;
	else
		return WmTypes.WM_UI_HANDLER_BREAK;
}};

public static WmUIHandlerRemoveFunc ui_handler_remove_panel = new WmUIHandlerRemoveFunc() {
public void run(bContext C, Object userdata)
{
	Panel pa= (Panel)userdata;

	panel_activate_state(C, pa, uiHandlePanelState.PANEL_STATE_EXIT);
}};

static void panel_activate_state(bContext C, Panel pa, uiHandlePanelState state)
{
	uiHandlePanelData data= (uiHandlePanelData)pa.activedata;
	wmWindow win= bContext.CTX_wm_window(C);
	ARegion ar= bContext.CTX_wm_region(C);

	if(data!=null && data.state == state)
		return;

	if(state == uiHandlePanelState.PANEL_STATE_EXIT || state == uiHandlePanelState.PANEL_STATE_ANIMATION) {
		if(data!=null && data.state != uiHandlePanelState.PANEL_STATE_ANIMATION) {
			/* XXX:
			 *	- the panel tabbing function call below (test_add_new_tabs()) has been commented out
			 *	  "It is too easy to do by accident when reordering panels, is very hard to control and use, and has no real benefit." - BillRey
			 * Aligorith, 2009Sep
			 */
//			test_add_new_tabs(ar);   // also copies locations of tabs in dragged panel
			check_panel_overlap(ar, null);  // clears
		}

		pa.flag &= ~UI.PNL_SELECT;
	}
	else
		pa.flag |= UI.PNL_SELECT;

	if(data!=null && data.animtimer!=null) {
		WmWindow.WM_event_remove_timer(bContext.CTX_wm_manager(C), win, data.animtimer);
		data.animtimer= null;
	}

	if(state == uiHandlePanelState.PANEL_STATE_EXIT) {
//		MEM_freeN(data);
		pa.activedata= null;

		WmEventSystem.WM_event_remove_ui_handler(win.handlers, ui_handler_panel, ui_handler_remove_panel, pa);
	}
	else {
		if(data==null) {
			data= new uiHandlePanelData();
			pa.activedata= data;

			WmEventSystem.WM_event_add_ui_handler(C, win.handlers, ui_handler_panel, ui_handler_remove_panel, pa);
		}

		if((state == uiHandlePanelState.PANEL_STATE_ANIMATION || state == uiHandlePanelState.PANEL_STATE_DRAG))
			data.animtimer= WmWindow.WM_event_add_timer(bContext.CTX_wm_manager(C), win, WmEventTypes.TIMER, ANIMATION_INTERVAL);

		data.state= state;
		data.startx= ((wmEvent)win.eventstate).x;
		data.starty= ((wmEvent)win.eventstate).y;
		data.startofsx= pa.ofsx;
		data.startofsy= pa.ofsy;
		data.startsizex= pa.sizex;
		data.startsizey= pa.sizey;
		data.starttime= Time.PIL_check_seconds_timer();
	}

	Area.ED_region_tag_redraw(ar);

//	/* XXX exception handling, 3d window preview panel */
//	/* if(block.drawextra==BIF_view3d_previewdraw)
//		BIF_view3d_previewrender_clear(curarea);*/
//
//	/* XXX exception handling, 3d window preview panel */
//	/* if(block.drawextra==BIF_view3d_previewdraw)
//		BIF_view3d_previewrender_signal(curarea, PR_DISPRECT);
//	else if(strcmp(block.name, "image_panel_preview")==0)
//		image_preview_event(2); */
}

static Panel panelCopy(Panel panel) {
    Panel pa = new Panel();
    pa.prev = panel.prev;
    pa.next = panel.next;
    pa.type = panel.type;
    pa.layout = panel.layout;
    System.arraycopy(panel.panelname, 0, pa.panelname, 0, pa.panelname.length);
    System.arraycopy(panel.tabname, 0, pa.tabname, 0, pa.tabname.length);
    System.arraycopy(panel.drawname, 0, pa.drawname, 0, pa.drawname.length);
    pa.ofsx = panel.ofsx;
    pa.ofsy = panel.ofsy;
    pa.sizex = panel.sizex;
    pa.sizey = panel.sizey;
    pa.labelofs = panel.labelofs;
    pa.pad = panel.pad;
    pa.flag = panel.flag;
    pa.runtime_flag = panel.runtime_flag;
    pa.control = panel.control;
    pa.snap = panel.snap;
    pa.sortorder = panel.sortorder;
    pa.paneltab = panel.paneltab;
    pa.activedata = panel.activedata;
    pa.list_scroll = panel.list_scroll;
    pa.list_size = panel.list_size;
    System.arraycopy(panel.list_search, 0, pa.list_search, 0, pa.list_search.length);
    return pa;
}

}
