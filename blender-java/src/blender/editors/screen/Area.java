/**
 * $Id:
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
 * The Original Code is Copyright (C) 2008 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.screen;

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import blender.blenfont.Blf;
import blender.blenkernel.Blender;
import blender.blenkernel.Pointer;
import blender.blenkernel.ScreenUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.Header;
import blender.blenkernel.ScreenUtil.HeaderType;
import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.blenlib.StringUtil;
import blender.editors.space_api.SpaceTypeUtil;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UIHandlers;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UIPanel;
import blender.editors.uinterface.View2dUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UI.uiHandleFunc;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.editors.uinterface.View2dUtil.View2DScrollers;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View2dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.View2D;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.uiStyle;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmSubWindow;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class Area {

    /* default keymaps, bitflags */
    public static final int ED_KEYMAP_UI=		1;
    public static final int ED_KEYMAP_VIEW2D=	2;
    public static final int ED_KEYMAP_MARKERS=	4;
    public static final int ED_KEYMAP_ANIMATION=	8;
    public static final int ED_KEYMAP_FRAMES=	16;
    public static final int ED_KEYMAP_GPENCIL=	32;
    public static final int ED_KEYMAP_HEADER=	64;

///* for animplayer */
//typedef struct ScreenAnimData {
//	ARegion *ar;	/* do not read from this, only for comparing if region exists */
//	int redraws;
//	int reverse;
//} ScreenAnimData;
    
/* Enum for Action Zone Edges. Which edge of area is action zone. */
//typedef enum {
    public static final int AE_RIGHT_TO_TOPLEFT=0;	/* Region located on the left, _right_ edge is action zone. Region minimised to the top left */
    public static final int AE_LEFT_TO_TOPRIGHT=1;	/* Region located on the right, _left_ edge is action zone. Region minimised to the top right */
    public static final int AE_TOP_TO_BOTTOMRIGHT=2;		/* Region located at the bottom, _top_ edge is action zone. Region minimised to the bottom right */
    public static final int AE_BOTTOM_TO_TOPLEFT=3;	/* Region located at the top, _bottom_edge is action zone. Region minimised to the top left */
//} AZEdge;

public static class AZone extends Link<AZone> {
	public ARegion ar;
	public int type;
	/* region-azone, which of the edges */
	public short edge;
	/* internal */
	public short do_draw;
	/* for draw */
	public short x1, y1, x2, y2, x3, y3;
	/* for clip */
	public rcti rect = new rcti();
};

/* actionzone type */
public static final int	AZONE_AREA=			1;
public static final int AZONE_REGION=		2;

//////////////////////////////////////////////////////////////////////////////////

/* general area and region code */

public static void region_draw_emboss(GL2 gl, ARegion ar, rcti scirct)
{
	rcti rect = new rcti();

	/* translate scissor rect to region space */
	rect.xmin= scirct.xmin - ar.winrct.xmin;
	rect.ymin= scirct.ymin - ar.winrct.ymin;
	rect.xmax= scirct.xmax - ar.winrct.xmin;
	rect.ymax= scirct.ymax - ar.winrct.ymin;

	/* set transp line */
	gl.glEnable( GL2.GL_BLEND );
	gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA );

	/* right  */
	gl.glColor4ub((byte)0,(byte)0,(byte)0, (byte)50);
	GlUtil.sdrawline(gl, rect.xmax, rect.ymin, rect.xmax, rect.ymax);

	/* bottom  */
	gl.glColor4ub((byte)0,(byte)0,(byte)0, (byte)80);
	GlUtil.sdrawline(gl, rect.xmin, rect.ymin, rect.xmax, rect.ymin);

	/* top  */
	gl.glColor4ub((byte)255,(byte)255,(byte)255, (byte)60);
	GlUtil.sdrawline(gl, rect.xmin, rect.ymax, rect.xmax, rect.ymax);

	/* left  */
	gl.glColor4ub((byte)255,(byte)255,(byte)255, (byte)50);
	GlUtil.sdrawline(gl, rect.xmin, rect.ymin, rect.xmin, rect.ymax);

	gl.glDisable( GL2.GL_BLEND );
}

public static void ED_region_pixelspace(GL2 gl, ARegion ar)
{
	int width= ar.winrct.xmax-ar.winrct.xmin+1;
	int height= ar.winrct.ymax-ar.winrct.ymin+1;

	WmSubWindow.wmOrtho2(gl, -0.375f, (float)width-0.375f, -0.375f, (float)height-0.375f);
//	WmSubWindow.wmLoadIdentity(gl);
	gl.glLoadIdentity();
}

/* only exported for WM */
public static void ED_region_do_listen(ARegion ar, wmNotifier note)
{
	/* generic notes first */
	switch(note.category) {
		case WmTypes.NC_WM:
			if(note.data==WmTypes.ND_FILEREAD)
				ED_region_tag_redraw(ar);
			break;
		case WmTypes.NC_WINDOW:
			ED_region_tag_redraw(ar);
			break;
//		case WmTypes.NC_SCREEN:
//			if(note.action==WmTypes.NA_EDITED)
//				ED_region_tag_redraw(ar);
//			/* pass on */
//		default:
//			if(ar.type!=null && ((ARegionType)ar.type).listener!=null)
//				((ARegionType)ar.type).listener.run(ar, note);
	}
	
	if(ar.type!=null && ((ARegionType)ar.type).listener!=null)
		((ARegionType)ar.type).listener.run(ar, note);
}

/* only exported for WM */
public static void ED_area_do_listen(ScrArea sa, wmNotifier note)
{
	/* no generic notes? */
	if(sa.type!=null && ((SpaceType)sa.type).listener!=null) {
		((SpaceType)sa.type).listener.run(sa, note);
	}
}

/* only exported for WM */
public static void ED_area_do_refresh(bContext C, ScrArea sa)
{
	/* no generic notes? */
	if(sa.type!=null && ((SpaceType)sa.type).refresh!=null) {
		((SpaceType)sa.type).refresh.run(C, sa);
	}
	sa.do_refresh= 0;
}

/* based on screen region draw tags, set draw tags in azones, and future region tabs etc */
/* only exported for WM */
public static void ED_area_overdraw_flush(ScrArea sa, ARegion ar)
{
	AZone az;

	for(az= (AZone)sa.actionzones.first; az!=null; az= az.next) {
		int xs, ys;

//		if(az.type==AZONE_AREA) {
			xs= (az.x1+az.x2)/2;
			ys= (az.y1+az.y2)/2;
//		}
//		else {
//			xs= az.x3;
//			ys= az.y3;
//		}

		/* test if inside */
		if(Rct.BLI_in_rcti(ar.winrct, xs, ys)) {
			az.do_draw= 1;
		}
	}
}

public static void area_draw_azone(GL2 gl, int x1, int y1, int x2, int y2)
{
	float xmin = x1;
	float xmax = x2-2;
	float ymin = y1-1;
	float ymax = y2-3;

	float dx= 0.3f*(xmax-xmin);
	float dy= 0.3f*(ymax-ymin);

	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)180);
	GlUtil.fdrawline(gl, xmin, ymax, xmax, ymin);
	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)130);
	GlUtil.fdrawline(gl, xmin, ymax-dy, xmax-dx, ymin);
	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)80);
	GlUtil.fdrawline(gl, xmin, ymax-2*dy, xmax-2*dx, ymin);

	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)210);
	GlUtil.fdrawline(gl, xmin, ymax+1, xmax+1, ymin);
	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)180);
	GlUtil.fdrawline(gl, xmin, ymax-dy+1, xmax-dx+1, ymin);
	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)150);
	GlUtil.fdrawline(gl, xmin, ymax-2*dy+1, xmax-2*dx+1, ymin);
}

//public static void region_draw_azone(GL2 gl, ScrArea sa, AZone az)
public static void region_draw_azone(GL2 gl, AZone az)
{
//	if(az.ar==null) return;
//
//	Resources.UI_SetTheme(sa.spacetype, ((ARegionType)az.ar.type).regionid);
//
//	Resources.UI_ThemeColor(gl, Resources.TH_BACK);
//	gl.glBegin(GL2.GL_TRIANGLES);
//	gl.glVertex2s(az.x1, az.y1);
//	gl.glVertex2s(az.x2, az.y2);
//	gl.glVertex2s(az.x3, az.y3);
//	gl.glEnd();
//
//	Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, 50);
//	GlUtil.sdrawline(gl, az.x1, az.y1, az.x3, az.y3);
//
//	Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -50);
//	GlUtil.sdrawline(gl, az.x2, az.y2, az.x3, az.y3);
	
	GLU glu = new GLU();
	GLUquadric qobj = null; 
	short midx = (short)(az.x1 + (az.x2 - az.x1)/2);
	short midy = (short)(az.y1 + (az.y2 - az.y1)/2);
	
	if(az.ar==null) return;
	
	/* only display action zone icons when the region is hidden */
	if ((az.ar.flag & ScreenTypes.RGN_FLAG_HIDDEN)==0) return;
	
	qobj = glu.gluNewQuadric();
	
	gl.glPushMatrix(); 	
	gl.glTranslatef(midx, midy, 0.0f); 
	
	/* outlined circle */
	gl.glEnable(GL2.GL_LINE_SMOOTH);

	gl.glColor4f(1.f, 1.f, 1.f, 0.8f);

	glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL); 
	glu.gluDisk( qobj, 0.0,  4.25f, 16, 1); 

	gl.glColor4f(0.2f, 0.2f, 0.2f, 0.9f);
	
	glu.gluQuadricDrawStyle(qobj, GLU.GLU_SILHOUETTE); 
	glu.gluDisk( qobj, 0.0,  4.25f, 16, 1); 
	
	gl.glDisable(GL2.GL_LINE_SMOOTH);
	
	gl.glPopMatrix();
	glu.gluDeleteQuadric(qobj);
	
	/* + */
	GlUtil.sdrawline(gl, midx, midy-2, midx, midy+3);
	GlUtil.sdrawline(gl, midx-2, midy, midx+3, midy);
}


/* only exported for WM */
public static void ED_area_overdraw(GL2 gl, bContext C)
{
	wmWindow win= bContext.CTX_wm_window(C);
	bScreen screen= bContext.CTX_wm_screen(C);
	ScrArea sa;

	/* Draw AZones, in screenspace */
	WmSubWindow.wmSubWindowSet(gl, win, screen.mainwin);

	gl.glEnable( GL2.GL_BLEND );
	gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA );

	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
		AZone az;
		for(az= (AZone)sa.actionzones.first; az!=null; az= az.next) {
			if(az.do_draw!=0) {
				if(az.type==AZONE_AREA) {
					area_draw_azone(gl, az.x1, az.y1, az.x2, az.y2);
				} else if(az.type==AZONE_REGION) {
					region_draw_azone(gl, az);
				}

				az.do_draw= 0;
			}
		}
	}
	gl.glDisable( GL2.GL_BLEND );
}

/* get scissor rect, checking overlapping regions */
public static void region_scissor_winrct(ARegion ar, rcti winrct)
{
//	*winrct= ar.winrct;
        winrct.xmax = ar.winrct.xmax;
        winrct.xmin = ar.winrct.xmin;
        winrct.ymax = ar.winrct.ymax;
        winrct.ymin = ar.winrct.ymin;

	if(ar.alignment==ScreenTypes.RGN_OVERLAP_LEFT || ar.alignment==ScreenTypes.RGN_OVERLAP_RIGHT)
		return;

	while(ar.prev!=null) {
		ar= ar.prev;

		if(Rct.BLI_isect_rcti(winrct, ar.winrct, null)) {
			if((ar.flag & ScreenTypes.RGN_FLAG_HIDDEN)!=0);
			else if((ar.alignment & ScreenTypes.RGN_SPLIT_PREV)!=0);
			else if(ar.alignment==ScreenTypes.RGN_OVERLAP_LEFT) {
				winrct.xmin= ar.winrct.xmax + 1;
			}
			else if(ar.alignment==ScreenTypes.RGN_OVERLAP_RIGHT) {
				winrct.xmax= ar.winrct.xmin - 1;
			}
			else break;
		}
	}
}

/* only exported for WM */
/* makes region ready for drawing, sets pixelspace */
public static void ED_region_set(GL2 gl, bContext C, ARegion ar)
{
	wmWindow win= bContext.CTX_wm_window(C);
	ScrArea sa= bContext.CTX_wm_area(C);
	rcti winrct = new rcti();

	/* checks other overlapping regions */
	region_scissor_winrct(ar, winrct);

	ar.drawrct= winrct;

	/* note; this sets state, so we can use wmOrtho and friends */
	WmSubWindow.wmSubWindowScissorSet(gl, win, ar.swinid, ar.drawrct);

	Resources.UI_SetTheme(sa!=null?sa.spacetype:0, ar.type!=null?((ARegionType)ar.type).regionid:0);

	Area.ED_region_pixelspace(gl, ar);
}


/* only exported for WM */
public static void ED_region_do_draw(GL2 gl, bContext C, ARegion ar)
{
	wmWindow win= bContext.CTX_wm_window(C);
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegionType at= (ARegionType)ar.type;
	rcti winrct = new rcti();

	/* checks other overlapping regions */
	region_scissor_winrct(ar, winrct);

	/* if no partial draw rect set, full rect */
	if(ar.drawrct.xmin == ar.drawrct.xmax)
		ar.drawrct= winrct;
	else {
		/* extra clip for safety */
		ar.drawrct.xmin= UtilDefines.MAX2(winrct.xmin, ar.drawrct.xmin);
		ar.drawrct.ymin= UtilDefines.MAX2(winrct.ymin, ar.drawrct.ymin);
		ar.drawrct.xmax= UtilDefines.MIN2(winrct.xmax, ar.drawrct.xmax);
		ar.drawrct.ymax= UtilDefines.MIN2(winrct.ymax, ar.drawrct.ymax);
	}

	/* note; this sets state, so we can use wmOrtho and friends */
	WmSubWindow.wmSubWindowScissorSet(gl, win, ar.swinid, ar.drawrct);

	Resources.UI_SetTheme(sa!=null?sa.spacetype:0, ar.type!=null?((ARegionType)ar.type).regionid:0);

	/* optional header info instead? */
	if(ar.headerstr!=null) {
		Resources.UI_ThemeClearColor(gl, Resources.TH_HEADER);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		Resources.UI_ThemeColor(gl, Resources.TH_TEXT);
		Blf.BLF_draw_default(20.0f, 8.0f, 0.0f, (byte[])ar.headerstr,0, 65535); /* XXX, use real length */
	}
	else if(at.draw!=null) {
		at.draw.run(gl, C, ar);
	}
	
	SpaceTypeUtil.ED_region_draw_cb_draw(gl, C, ar, SpaceTypeUtil.REGION_DRAW_POST_PIXEL);

	UI.uiFreeInactiveBlocks(C, ar.uiblocks);

	if(sa!=null)
		region_draw_emboss(gl, ar, winrct);

	/* XXX test: add convention to end regions always in pixel space, for drawing of borders/gestures etc */
	ED_region_pixelspace(gl, ar);

	ar.do_draw= 0;
//	memset(&ar.drawrct, 0, sizeof(ar.drawrct));
    ar.drawrct = new rcti();
}

/* **********************************
   maybe silly, but let's try for now
   to keep these tags protected
   ********************************** */

public static void ED_region_tag_redraw(ARegion ar)
{
	if(ar!=null) {
		/* zero region means full region redraw */
		ar.do_draw= 1;
//		memset(&ar.drawrct, 0, sizeof(ar.drawrct));
//        ar.drawrct = new rcti();
        Rct.clear(ar.drawrct);
	}
}

//void ED_region_tag_redraw_overlay(ARegion *ar)
//{
//	if(ar)
//		ar->do_draw_overlay= RGN_DRAW;
//}

//void ED_region_tag_redraw_partial(ARegion *ar, rcti *rct)
//{
//	if(ar) {
//		if(!ar.do_draw) {
//			/* no redraw set yet, set partial region */
//			ar.do_draw= 1;
//			ar.drawrct= *rct;
//		}
//		else if(ar.drawrct.xmin != ar.drawrct.xmax) {
//			/* partial redraw already set, expand region */
//			ar.drawrct.xmin= MIN2(ar.drawrct.xmin, rct.xmin);
//			ar.drawrct.ymin= MIN2(ar.drawrct.ymin, rct.ymin);
//			ar.drawrct.xmax= MAX2(ar.drawrct.xmax, rct.xmax);
//			ar.drawrct.ymax= MAX2(ar.drawrct.ymax, rct.ymax);
//		}
//	}
//}

public static void ED_area_tag_redraw(ScrArea sa)
{
	ARegion ar;

	if(sa!=null)
		for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
			ED_region_tag_redraw(ar);
}

public static void ED_area_tag_refresh(ScrArea sa)
{
	if(sa!=null)
		sa.do_refresh= 1;
}

///* *************************************************************** */
//
///* use NULL to disable it */
//void ED_area_headerprint(ScrArea *sa, const char *str)
//{
//	ARegion *ar;
//
//	for(ar= sa.regionbase.first; ar; ar= ar.next) {
//		if(ar.regiontype==RGN_TYPE_HEADER) {
//			if(str) {
//				if(ar.headerstr==NULL)
//					ar.headerstr= MEM_mallocN(256, "headerprint");
//				BLI_strncpy(ar.headerstr, str, 256);
//			}
//			else if(ar.headerstr) {
//				MEM_freeN(ar.headerstr);
//				ar.headerstr= NULL;
//			}
//			ED_region_tag_redraw(ar);
//		}
//	}
//}

/* ************************************************************ */


public static final int AZONESPOT=		12;
static void area_azone_initialize(ScrArea sa)
{
	AZone az;

	/* reinitalize entirely, regions add azones too */
	ListBaseUtil.BLI_freelistN(sa.actionzones);

	/* set area action zones */
	az= new AZone();
	ListBaseUtil.BLI_addtail(sa.actionzones, az);
	az.type= AZONE_AREA;
	az.x1= (short)sa.totrct.xmin;
	az.y1= (short)sa.totrct.ymin;
	az.x2= (short)(sa.totrct.xmin + AZONESPOT-1);
	az.y2= (short)(sa.totrct.ymin + AZONESPOT-1);
	Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y2);

	az= new AZone();
	ListBaseUtil.BLI_addtail(sa.actionzones, az);
	az.type= AZONE_AREA;
	az.x1= (short)(sa.totrct.xmax+1);
	az.y1= (short)(sa.totrct.ymax+1);
	az.x2= (short)(sa.totrct.xmax-AZONESPOT+1);
	az.y2= (short)(sa.totrct.ymax-AZONESPOT+1);
	Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y2);
}

public static final int AZONEPAD_EDGE=	4;
public static final int AZONEPAD_ICON=	8;
static void region_azone_edge(AZone az, ARegion ar)
{
	switch(az.edge) {
		case AE_TOP_TO_BOTTOMRIGHT:
			az.x1= (short)(ar.winrct.xmin);
			az.y1= (short)(ar.winrct.ymax - AZONEPAD_EDGE);
			az.x2= (short)(ar.winrct.xmax);
			az.y2= (short)(ar.winrct.ymax);
			break;
		case AE_BOTTOM_TO_TOPLEFT:
			az.x1= (short)(ar.winrct.xmin);
			az.y1= (short)(ar.winrct.ymin + AZONEPAD_EDGE);
			az.x2= (short)(ar.winrct.xmax);
			az.y2= (short)(ar.winrct.ymin);
			break;
		case AE_LEFT_TO_TOPRIGHT:
			az.x1= (short)(ar.winrct.xmin);
			az.y1= (short)(ar.winrct.ymin);
			az.x2= (short)(ar.winrct.xmin + AZONEPAD_EDGE);
			az.y2= (short)(ar.winrct.ymax);
			break;
		case AE_RIGHT_TO_TOPLEFT:
			az.x1= (short)(ar.winrct.xmax);
			az.y1= (short)(ar.winrct.ymin);
			az.x2= (short)(ar.winrct.xmax - AZONEPAD_EDGE);
			az.y2= (short)(ar.winrct.ymax);
			break;
	}

	Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y2);
}

static void region_azone_icon(ScrArea sa, AZone az, ARegion ar)
{
	AZone azt;
	int tot=0;
	
	/* count how many actionzones with along same edge are available.
	   This allows for adding more action zones in the future without
	   having to worry about correct offset */
	for(azt= (AZone)sa.actionzones.first; azt!=null; azt= azt.next) {
		if(azt.edge == az.edge) tot++;
	}
	
	switch(az.edge) {
		case AE_TOP_TO_BOTTOMRIGHT:
			az.x1= (short)(ar.winrct.xmax - tot*2*AZONEPAD_ICON);
			az.y1= (short)(ar.winrct.ymax + AZONEPAD_ICON);
			az.x2= (short)(ar.winrct.xmax - tot*AZONEPAD_ICON);
			az.y2= (short)(ar.winrct.ymax + 2*AZONEPAD_ICON);
			break;
		case AE_BOTTOM_TO_TOPLEFT:
			az.x1= (short)(ar.winrct.xmin + AZONEPAD_ICON);
			az.y1= (short)(ar.winrct.ymin - 2*AZONEPAD_ICON);
			az.x2= (short)(ar.winrct.xmin + 2*AZONEPAD_ICON);
			az.y2= (short)(ar.winrct.ymin - AZONEPAD_ICON);
			break;
		case AE_LEFT_TO_TOPRIGHT:
			az.x1= (short)(ar.winrct.xmin - 2*AZONEPAD_ICON);
			az.y1= (short)(ar.winrct.ymax - tot*2*AZONEPAD_ICON);
			az.x2= (short)(ar.winrct.xmin - AZONEPAD_ICON);
			az.y2= (short)(ar.winrct.ymax - tot*AZONEPAD_ICON);
			break;
		case AE_RIGHT_TO_TOPLEFT:
			az.x1= (short)(ar.winrct.xmax + AZONEPAD_ICON);
			az.y1= (short)(ar.winrct.ymax - tot*2*AZONEPAD_ICON);
			az.x2= (short)(ar.winrct.xmax + 2*AZONEPAD_ICON);
			az.y2= (short)(ar.winrct.ymax - tot*AZONEPAD_ICON);
			break;
	}

	Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y2);
	
	/* if more azones on 1 spot, set offset */
	for(azt= (AZone)sa.actionzones.first; azt!=null; azt= azt.next) {
		if(az!=azt) {
			if( UtilDefines.ABS(az.x1-azt.x1) < 2 && UtilDefines.ABS(az.y1-azt.y1) < 2) {
				if(az.edge==AE_TOP_TO_BOTTOMRIGHT || az.edge==AE_BOTTOM_TO_TOPLEFT) {
					az.x1+= AZONESPOT;
					az.x2+= AZONESPOT;
				}
				else{
					az.y1-= AZONESPOT;
					az.y2-= AZONESPOT;
				}
				Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y2);
			}
		}
	}
}

static void region_azone_initialize(ScrArea sa, ARegion ar, int edge)
{
	AZone az, azt;

	az= new AZone();
	ListBaseUtil.BLI_addtail(sa.actionzones, az);
	az.type= AZONE_REGION;
	az.ar= ar;
	az.edge= (short)edge;
	
	if ((ar.flag & ScreenTypes.RGN_FLAG_HIDDEN)!=0) {
		region_azone_icon(sa, az, ar);
	} else {
		region_azone_edge(az, ar);
	}

//	if(edge=='t') {
//		az.x1= (short)(ar.winrct.xmin+AZONESPOT);
//		az.y1= (short)(ar.winrct.ymax);
//		az.x2= (short)(ar.winrct.xmin+2*AZONESPOT);
//		az.y2= (short)(ar.winrct.ymax);
//		az.x3= (short)((az.x1+az.x2)/2);
//		az.y3= (short)(az.y2+AZONESPOT/2);
//		Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y3);
//	}
//	else if(edge=='b') {
//		az.x1= (short)(ar.winrct.xmin+AZONESPOT);
//		az.y1= (short)(ar.winrct.ymin);
//		az.x2= (short)(ar.winrct.xmin+2*AZONESPOT);
//		az.y2= (short)(ar.winrct.ymin);
//		az.x3= (short)((az.x1+az.x2)/2);
//		az.y3= (short)(az.y2-AZONESPOT/2);
//		Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y3, az.y1);
//	}
//	else if(edge=='l') {
//		az.x1= (short)(ar.winrct.xmin);
//		az.y1= (short)(ar.winrct.ymax-AZONESPOT);
//		az.x2= (short)(ar.winrct.xmin);
//		az.y2= (short)(ar.winrct.ymax-2*AZONESPOT);
//		az.x3= (short)(az.x2-AZONESPOT/2);
//		az.y3= (short)((az.y1+az.y2)/2);
//		Rct.BLI_init_rcti(az.rect, az.x3, az.x1, az.y1, az.y2);
//	}
//	else { // if(edge=='r') {
//		az.x1= (short)(ar.winrct.xmax);
//		az.y1= (short)(ar.winrct.ymax-AZONESPOT);
//		az.x2= (short)(ar.winrct.xmax);
//		az.y2= (short)(ar.winrct.ymax-2*AZONESPOT);
//		az.x3= (short)(az.x2+AZONESPOT/2);
//		az.y3= (short)((az.y1+az.y2)/2);
//		Rct.BLI_init_rcti(az.rect, az.x1, az.x3, az.y1, az.y2);
//	}
//
//	/* if more azones on 1 spot, set offset */
//	for(azt= (AZone)sa.actionzones.first; azt!=null; azt= azt.next) {
//		if(az!=azt) {
//			if(az.x1==azt.x1 && az.y1==azt.y1) {
//				if(edge=='t' || edge=='b') {
//					az.x1+= AZONESPOT;
//					az.x2+= AZONESPOT;
//					az.x3+= AZONESPOT;
//					Rct.BLI_init_rcti(az.rect, az.x1, az.x2, az.y1, az.y3);
//				}
//				else {
//					az.y1-= AZONESPOT;
//					az.y2-= AZONESPOT;
//					az.y3-= AZONESPOT;
//					Rct.BLI_init_rcti(az.rect, az.x1, az.x3, az.y1, az.y2);
//				}
//			}
//		}
//	}

}


/* *************************************************************** */

static void region_azone_add(ScrArea sa, ARegion ar, int alignment)
{
	 /* edge code (t b l r) is where azone will be drawn */

	if(alignment==ScreenTypes.RGN_ALIGN_TOP)
//		region_azone_initialize(sa, ar, 'b');
		region_azone_initialize(sa, ar, AE_BOTTOM_TO_TOPLEFT);
	else if(alignment==ScreenTypes.RGN_ALIGN_BOTTOM)
//		region_azone_initialize(sa, ar, 't');
		region_azone_initialize(sa, ar, AE_TOP_TO_BOTTOMRIGHT);
	else if(alignment==ScreenTypes.RGN_ALIGN_RIGHT || alignment==ScreenTypes.RGN_OVERLAP_RIGHT)
//		region_azone_initialize(sa, ar, 'l');
		region_azone_initialize(sa, ar, AE_LEFT_TO_TOPRIGHT);
	else if(alignment==ScreenTypes.RGN_ALIGN_LEFT || alignment==ScreenTypes.RGN_OVERLAP_LEFT)
//		region_azone_initialize(sa, ar, 'r');
		region_azone_initialize(sa, ar, AE_RIGHT_TO_TOPLEFT);
}

/* dir is direction to check, not the splitting edge direction! */
static int rct_fits(rcti rect, char dir, int size)
{
	if(dir=='h') {
		return rect.xmax-rect.xmin - size;
	}
	else { // 'v'
		return rect.ymax-rect.ymin - size;
	}
}

static void region_rect_recursive(ScrArea sa, ARegion ar, rcti remainder, int quad)
{
	rcti remainder_prev= remainder;
	int prefsizex, prefsizey;
	int alignment;

	if(ar==null)
		return;

	/* no returns in function, winrct gets set in the end again */
	Rct.BLI_init_rcti(ar.winrct, 0, 0, 0, 0);

	/* for test; allow split of previously defined region */
	if((ar.alignment & ScreenTypes.RGN_SPLIT_PREV)!=0)
		if(ar.prev!=null)
			remainder= ar.prev.winrct;

	alignment = ar.alignment & ~ScreenTypes.RGN_SPLIT_PREV;

	/* clear state flags first */
	ar.flag &= ~ScreenTypes.RGN_FLAG_TOO_SMALL;
	/* user errors */
	if(ar.next==null && alignment!=ScreenTypes.RGN_ALIGN_QSPLIT)
		alignment= ScreenTypes.RGN_ALIGN_NONE;

	prefsizex= ((ARegionType)ar.type).minsizex;
	prefsizey= ((ARegionType)ar.type).minsizey;
//	prefsizex= ar.sizex?ar.sizex:ar.type.prefsizex;
//	prefsizey= ar.sizey?ar.sizey:ar.type.prefsizey;

	/* hidden is user flag */
	if((ar.flag & ScreenTypes.RGN_FLAG_HIDDEN)!=0);
	/* XXX floating area region, not handled yet here */
	else if(alignment == ScreenTypes.RGN_ALIGN_FLOAT);
	/* remainder is too small for any usage */
	else if( rct_fits(remainder, 'v', 1)<0 || rct_fits(remainder, 'h', 1) < 0 ) {
		ar.flag |= ScreenTypes.RGN_FLAG_TOO_SMALL;
	}
	else if(alignment==ScreenTypes.RGN_ALIGN_NONE) {
		/* typically last region */
        ar.winrct= remainder.copy();
		Rct.BLI_init_rcti(remainder, 0, 0, 0, 0);
	}
	else if(alignment==ScreenTypes.RGN_ALIGN_TOP || alignment==ScreenTypes.RGN_ALIGN_BOTTOM) {

		if( rct_fits(remainder, 'v', prefsizey) < 0 ) {
			ar.flag |= ScreenTypes.RGN_FLAG_TOO_SMALL;
		}
		else {
			int fac= rct_fits(remainder, 'v', prefsizey);

			if(fac < 0 )
				prefsizey += fac;

            ar.winrct= remainder.copy();

			if(alignment==ScreenTypes.RGN_ALIGN_TOP) {
				ar.winrct.ymin= ar.winrct.ymax - prefsizey + 1;
				remainder.ymax= ar.winrct.ymin - 1;
			}
			else {
				ar.winrct.ymax= ar.winrct.ymin + prefsizey - 1;
				remainder.ymin= ar.winrct.ymax + 1;
			}
		}
	}
	else if(alignment==ScreenTypes.RGN_ALIGN_LEFT || alignment==ScreenTypes.RGN_ALIGN_RIGHT || alignment==ScreenTypes.RGN_OVERLAP_LEFT || alignment==ScreenTypes.RGN_OVERLAP_RIGHT) {

		if( rct_fits(remainder, 'h', prefsizex) < 0 ) {
			ar.flag |= ScreenTypes.RGN_FLAG_TOO_SMALL;
		}
		else {
			int fac= rct_fits(remainder, 'h', prefsizex);

			if(fac < 0 )
				prefsizex += fac;

            ar.winrct= remainder.copy();

			if(alignment==ScreenTypes.RGN_ALIGN_RIGHT || alignment==ScreenTypes.RGN_OVERLAP_RIGHT) {
				ar.winrct.xmin= ar.winrct.xmax - prefsizex + 1;
				if(alignment==ScreenTypes.RGN_ALIGN_RIGHT)
					remainder.xmax= ar.winrct.xmin - 1;
			}
			else {
				ar.winrct.xmax= ar.winrct.xmin + prefsizex - 1;
				if(alignment==ScreenTypes.RGN_ALIGN_LEFT)
					remainder.xmin= ar.winrct.xmax + 1;
			}
		}
	}
	else if(alignment==ScreenTypes.RGN_ALIGN_VSPLIT || alignment==ScreenTypes.RGN_ALIGN_HSPLIT) {
		/* percentage subdiv*/
        ar.winrct= remainder.copy();

		if(alignment==ScreenTypes.RGN_ALIGN_HSPLIT) {
			if( rct_fits(remainder, 'h', prefsizex) > 4) {
				ar.winrct.xmax= (remainder.xmin+remainder.xmax)/2;
				remainder.xmin= ar.winrct.xmax+1;
			}
			else {
				Rct.BLI_init_rcti(remainder, 0, 0, 0, 0);
			}
		}
		else {
			if( rct_fits(remainder, 'v', prefsizey) > 4) {
				ar.winrct.ymax= (remainder.ymin+remainder.ymax)/2;
				remainder.ymin= ar.winrct.ymax+1;
			}
			else {
				Rct.BLI_init_rcti(remainder, 0, 0, 0, 0);
			}
		}
	}
	else if(alignment==ScreenTypes.RGN_ALIGN_QSPLIT) {
        ar.winrct= remainder.copy();

		/* test if there's still 4 regions left */
		if(quad==0) {
			ARegion artest= ar.next;
			int count= 1;

			while(artest!=null) {
				artest.alignment= ScreenTypes.RGN_ALIGN_QSPLIT;
				artest= artest.next;
				count++;
			}

			if(count!=4) {
				/* let's stop adding regions */
				Rct.BLI_init_rcti(remainder, 0, 0, 0, 0);
				System.out.printf("region quadsplit failed\n");
			}
			else quad= 1;
		}
		if(quad!=0) {
			if(quad==1) { /* left bottom */
				ar.winrct.xmax = (remainder.xmin + remainder.xmax)/2;
				ar.winrct.ymax = (remainder.ymin + remainder.ymax)/2;
			}
			else if(quad==2) { /* left top */
				ar.winrct.xmax = (remainder.xmin + remainder.xmax)/2;
				ar.winrct.ymin = 1 + (remainder.ymin + remainder.ymax)/2;
			}
			else if(quad==3) { /* right bottom */
				ar.winrct.xmin = 1 + (remainder.xmin + remainder.xmax)/2;
				ar.winrct.ymax = (remainder.ymin + remainder.ymax)/2;
			}
			else {	/* right top */
				ar.winrct.xmin = 1 + (remainder.xmin + remainder.xmax)/2;
				ar.winrct.ymin = 1 + (remainder.ymin + remainder.ymax)/2;
				Rct.BLI_init_rcti(remainder, 0, 0, 0, 0);
			}

			quad++;
		}
	}

	/* for speedup */
	ar.winx= (short)(ar.winrct.xmax - ar.winrct.xmin + 1);
	ar.winy= (short)(ar.winrct.ymax - ar.winrct.ymin + 1);

	/* restore test exception */
	if((ar.alignment & ScreenTypes.RGN_SPLIT_PREV)!=0) {
		if(ar.prev!=null) {
			remainder= remainder_prev;
			ar.prev.winx= (short)(ar.prev.winrct.xmax - ar.prev.winrct.xmin + 1);
			ar.prev.winy= (short)(ar.prev.winrct.ymax - ar.prev.winrct.ymin + 1);
		}
	}

	/* set winrect for azones */
	if((ar.flag & (ScreenTypes.RGN_FLAG_HIDDEN|ScreenTypes.RGN_FLAG_TOO_SMALL))!=0) {
        ar.winrct= remainder.copy();

		if(alignment==ScreenTypes.RGN_ALIGN_TOP)
			ar.winrct.ymin= ar.winrct.ymax;
		else if(alignment==ScreenTypes.RGN_ALIGN_BOTTOM)
			ar.winrct.ymax= ar.winrct.ymin;
		else if(alignment==ScreenTypes.RGN_ALIGN_RIGHT || alignment==ScreenTypes.RGN_OVERLAP_RIGHT)
			ar.winrct.xmin= ar.winrct.xmax;
		else if(alignment==ScreenTypes.RGN_ALIGN_LEFT || alignment==ScreenTypes.RGN_OVERLAP_LEFT)
			ar.winrct.xmax= ar.winrct.xmin;
		else /* prevent winrct to be valid */
			ar.winrct.xmax= ar.winrct.xmin;
	}
	/* in end, add azones, where appropriate */
	region_azone_add(sa, ar, alignment);


	region_rect_recursive(sa, ar.next, remainder, quad);
}

static void area_calc_totrct(ScrArea sa, int sizex, int sizey)
{
	short rt= (short)UtilDefines.CLAMPIS(G.rt, 0, 16);

	if(sa.v1.vec.x>0) sa.totrct.xmin= sa.v1.vec.x+1+rt;
	else sa.totrct.xmin= sa.v1.vec.x;
	if(sa.v4.vec.x<sizex-1) sa.totrct.xmax= sa.v4.vec.x-1-rt;
	else sa.totrct.xmax= sa.v4.vec.x;

	if(sa.v1.vec.y>0) sa.totrct.ymin= sa.v1.vec.y+1+rt;
	else sa.totrct.ymin= sa.v1.vec.y;
	if(sa.v2.vec.y<sizey-1) sa.totrct.ymax= sa.v2.vec.y-1-rt;
	else sa.totrct.ymax= sa.v2.vec.y;

	/* for speedup */
	sa.winx= (short)(sa.totrct.xmax-sa.totrct.xmin+1);
	sa.winy= (short)(sa.totrct.ymax-sa.totrct.ymin+1);
}


/* used for area initialize below */
static void region_subwindow(GL2 gl, wmWindow win, ARegion ar)
//static void region_subwindow(wmWindow win, ARegion ar)
{
	if((ar.flag & (ScreenTypes.RGN_FLAG_HIDDEN|ScreenTypes.RGN_FLAG_TOO_SMALL))!=0) {
		if(ar.swinid!=0)
			WmSubWindow.wm_subwindow_close(win, ar.swinid);
		ar.swinid= 0;
	}
	else if(ar.swinid==0)
		ar.swinid= (short)WmSubWindow.wm_subwindow_open(gl, win, ar.winrct);
//		ar.swinid= (short)WmSubWindow.wm_subwindow_open(win, ar.winrct);
	else
		WmSubWindow.wm_subwindow_position(gl, win, ar.swinid, ar.winrct);
//		WmSubWindow.wm_subwindow_position(win, ar.swinid, ar.winrct);
}

static void ed_default_handlers(wmWindowManager wm, ListBase handlers, int flag)
{
	/* note, add-handler checks if it already exists */

	// XXX it would be good to have boundbox checks for some of these...
	if((flag & ED_KEYMAP_UI)!=0) {
		/* user interface widgets */
		UIHandlers.UI_add_region_handlers(handlers);
	}
	if((flag & ED_KEYMAP_VIEW2D)!=0) {
		/* 2d-viewport handling+manipulation */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View2D", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
	}
	if((flag & ED_KEYMAP_MARKERS)!=0) {
		/* time-markers */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Markers", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
		// XXX need boundbox check urgently!!!
	}
	if((flag & ED_KEYMAP_ANIMATION)!=0) {
		/* frame changing and timeline operators (for time spaces) */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Animation", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
	}
	if((flag & ED_KEYMAP_FRAMES)!=0) {
		/* frame changing/jumping (for all spaces) */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Frames", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
	}
	if((flag & ED_KEYMAP_GPENCIL)!=0) {
		/* grease pencil */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Grease Pencil", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
	}
	if((flag & ED_KEYMAP_HEADER)!=0) {
		/* standard keymap for headers regions */
		wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Header", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(handlers, keymap);
	}
}


/* called in screen_refresh, or screens_init, also area size changes */
public static void ED_area_initialize(GL2 gl, wmWindowManager wm, wmWindow win, ScrArea sa)
//public static void ED_area_initialize(wmWindowManager wm, wmWindow win, ScrArea sa)
{
	ARegion ar;
	rcti rect = new rcti();

	/* set typedefinitions */
	sa.type= ScreenUtil.BKE_spacetype_from_id(sa.spacetype);

	if(sa.type==null) {
		sa.butspacetype= sa.spacetype= SpaceTypes.SPACE_VIEW3D;
		sa.type= ScreenUtil.BKE_spacetype_from_id(sa.spacetype);
	}

	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
		ar.type= ScreenUtil.BKE_regiontype_from_id((SpaceType)sa.type, ar.regiontype);

	/* area sizes */
	area_calc_totrct(sa, win.sizex, win.sizey);

	/* clear all azones, add the area triange widgets */
	area_azone_initialize(sa);

	/* region rect sizes */
    rect= sa.totrct.copy();
	region_rect_recursive(sa, (ARegion)sa.regionbase.first, rect, 0);

	/* default area handlers */
	ed_default_handlers(wm, sa.handlers, ((SpaceType)sa.type).keymapflag);
	/* checks spacedata, adds own handlers */
	if(((SpaceType)sa.type).init!=null)
		((SpaceType)sa.type).init.run(wm, sa);

	/* region windows, default and own handlers */
	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
		region_subwindow(gl, win, ar);
//		region_subwindow(win, ar);

		if(ar.swinid!=0) {
			/* default region handlers */
			ed_default_handlers(wm, ar.handlers, ((ARegionType)ar.type).keymapflag);

			if(((ARegionType)ar.type).init!=null)
				((ARegionType)ar.type).init.run(wm, ar);
		}
		else {
			/* prevent uiblocks to run */
			UI.uiFreeBlocks(null, ar.uiblocks);
		}

	}
}

/* externally called for floating regions like menus */
public static void ED_region_init(GL2 gl, bContext C, ARegion ar)
//public static void ED_region_init(bContext C, ARegion ar)
{
//	ARegionType *at= ar.type;

	/* refresh can be called before window opened */
	region_subwindow(gl, bContext.CTX_wm_window(C), ar);
//	region_subwindow(bContext.CTX_wm_window(C), ar);

	ar.winx= (short)(ar.winrct.xmax - ar.winrct.xmin + 1);
	ar.winy= (short)(ar.winrct.ymax - ar.winrct.ymin + 1);

	/* UI convention */
//	try {
//		GL2 gl = (GL2)GLU.getCurrentGL();
		WmSubWindow.wmOrtho2(gl, -0.01f, ar.winx-0.01f, -0.01f, ar.winy-0.01f);
		gl.glLoadIdentity();
//	} catch (GLException e) {
//		
//	}
}


/* sa2 to sa1, we swap spaces for fullscreen to keep all allocated data */
/* area vertices were set */
public static void area_copy_data(ScrArea sa1, ScrArea sa2, int swap_space)
{
	SpaceType st;
	ARegion ar;
	int spacetype= sa1.spacetype;

	sa1.headertype= sa2.headertype;
	sa1.spacetype= sa2.spacetype;
	sa1.butspacetype= sa2.butspacetype;

	if(swap_space == 1) {
//		SWAP(ListBase, sa1.spacedata, sa2.spacedata);
        ListBase sw_ap; sw_ap=sa1.spacedata; sa1.spacedata=sa2.spacedata; sa2.spacedata=sw_ap;
		/* exception: ensure preview is reset */
//		if(sa1.spacetype==SPACE_VIEW3D)
// XXX			BIF_view3d_previewrender_free(sa1.spacedata.first);
	}
	else if (swap_space == 2) {
		ScreenUtil.BKE_spacedata_copylist(sa1.spacedata, sa2.spacedata);
	}
	else {
		ScreenUtil.BKE_spacedata_freelist(sa1.spacedata);
		ScreenUtil.BKE_spacedata_copylist(sa1.spacedata, sa2.spacedata);
	}

	/* Note; SPACE_EMPTY is possible on new screens */

	/* regions */
	if(swap_space == 1) {
//		SWAP(ListBase, sa1.regionbase, sa2.regionbase);
		ListBase sw_ap; sw_ap=sa1.regionbase; sa1.regionbase=sa2.regionbase; sa2.regionbase=sw_ap;
	}
	else {
		if(swap_space<2) {
			st= ScreenUtil.BKE_spacetype_from_id(sa1.spacetype);
			for(ar= (ARegion)sa1.regionbase.first; ar!=null; ar= ar.next)
				ScreenUtil.BKE_area_region_free(st, ar);
			ListBaseUtil.BLI_freelistN(sa1.regionbase);
		}
	
		st= ScreenUtil.BKE_spacetype_from_id(sa2.spacetype);
		for(ar= (ARegion)sa2.regionbase.first; ar!=null; ar= ar.next) {
			ARegion newar= ScreenUtil.BKE_area_region_copy(st, ar);
			ListBaseUtil.BLI_addtail(sa1.regionbase, newar);
		}
	}
}

/* *********** Space switching code *********** */

public static void ED_area_swapspace(GL2 gl, bContext C, ScrArea sa1, ScrArea sa2)
//public static void ED_area_swapspace(bContext C, ScrArea sa1, ScrArea sa2)
{
	ScrArea tmp= new ScrArea();

	ScreenEdit.ED_area_exit(C, sa1);
	ScreenEdit.ED_area_exit(C, sa2);

//	tmp.spacetype= sa1.spacetype;
//	tmp.butspacetype= sa1.butspacetype;
//	ScreenUtil.BKE_spacedata_copyfirst(tmp.spacedata, sa1.spacedata);

	area_copy_data(tmp, sa1, 2);
	area_copy_data(sa1, sa2, 0);
	area_copy_data(sa2, tmp, 0);
	ED_area_initialize(gl, bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa1);
	ED_area_initialize(gl, bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa2);
//	ED_area_initialize(bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa1);
//	ED_area_initialize(bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa2);

	ScreenUtil.BKE_screen_area_free(tmp);
//	MEM_freeN(tmp);

	/* tell WM to refresh, cursor types etc */
	WmEventSystem.WM_event_add_mousemove(C);

	ED_area_tag_redraw(sa1);
	ED_area_tag_refresh(sa1);
	ED_area_tag_redraw(sa2);
	ED_area_tag_refresh(sa2);
}

public static void ED_area_newspace(bContext C, ScrArea sa, int type)
{
//    System.out.println("ED_area_newspace");
	if(sa.spacetype != type) {
		SpaceType st;
		SpaceLink slold;
		SpaceLink sl;

		ScreenEdit.ED_area_exit(C, sa);

		st= ScreenUtil.BKE_spacetype_from_id(type);
		slold= (SpaceLink)sa.spacedata.first;

		sa.spacetype= (byte)type;
		sa.butspacetype= (byte)type;
		sa.type= st;

		/* check previously stored space */
		for (sl= (SpaceLink)sa.spacedata.first; sl!=null; sl= (SpaceLink)sl.next)
			if(sl.spacetype==type)
				break;

		/* old spacedata... happened during work on 2.50, remove */
		if(sl!=null && sl.regionbase.first==null) {
			st.free.run(sl);
			ListBaseUtil.BLI_freelinkN(sa.spacedata, sl);
			sl= null;
		}

		if (sl!=null) {

			/* swap regions */
			slold.regionbase= sa.regionbase;
			sa.regionbase= sl.regionbase;
			sl.regionbase.first= sl.regionbase.last= null;

			/* put in front of list */
			ListBaseUtil.BLI_remlink(sa.spacedata, sl);
			ListBaseUtil.BLI_addhead(sa.spacedata, sl);
		}
		else {
			/* new space */
			if(st!=null) {
				sl= st.newInstance.run(C);
				ListBaseUtil.BLI_addhead(sa.spacedata, sl);

				/* swap regions */
				if(slold!=null)
					slold.regionbase= sa.regionbase;
				sa.regionbase= sl.regionbase;
				sl.regionbase.first= sl.regionbase.last= null;
			}
		}

		ED_area_initialize((GL2)GLU.getCurrentGL(), bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa);
//		ED_area_initialize(bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa);

		/* tell WM to refresh, cursor types etc */
		WmEventSystem.WM_event_add_mousemove(C);
		
		/*send space change notifyer*/
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SPACE|WmTypes.ND_SPACE_CHANGED, sa);

		ED_area_tag_redraw(sa);
		ED_area_tag_refresh(sa);
	}
}

//void ED_area_prevspace(bContext *C)
//{
//	SpaceLink *sl= CTX_wm_space_data(C);
//	ScrArea *sa= CTX_wm_area(C);
//
//	/* cleanup */
//#if 0 // XXX needs to be space type specific
//	if(sfile.spacetype==SPACE_FILE) {
//		if(sfile.pupmenu) {
//			MEM_freeN(sfile.pupmenu);
//			sfile.pupmenu= NULL;
//		}
//	}
//#endif
//
//	if(sl.next) {
//
//#if 0 // XXX check whether this is still needed
//		if (sfile.spacetype == SPACE_SCRIPT) {
//			SpaceScript *sc = (SpaceScript *)sfile;
//			if (sc.script) sc.script.flags &=~SCRIPT_FILESEL;
//		}
//#endif
//
//		ED_area_newspace(C, sa, sl.next.spacetype);
//	}
//	else {
//		ED_area_newspace(C, sa, SPACE_INFO);
//	}
//	ED_area_tag_redraw(sa);
//}

public static String editortype_pup()
{
	return(
		   "Editor type:%t"+ //14
		   "|3D View %x1"+ //30

		   "|%l"+ // 33

		   "|Timeline %x15"+
		   "|Graph Editor %x2"+ //54
		   "|DopeSheet %x12"+ //73
		   "|NLA Editor %x13"+ //94

		   "|%l"+ //97

		   "|UV/Image Editor %x6"+ //117

		   "|Video Sequence Editor %x8"+ //143
		   //"|Timeline %x15"+ //163
		   // "|Audio Window %x11" //163
		   "|Text Editor %x9"+ //179
		   "|Node Editor %x16"+
		   "|Logic Editor %x17"+

		   "|%l"+ //192

		   "|Properties %x4"+
		   //"|User Preferences %x7"+ //213
		   "|Outliner %x3"+ //232
		   "|User Preferences %x19"+
		   "|Info%x7"+
		   //"|Buttons Window %x4"+ //251
		   //"|Node Editor %x16"+
		   //"|Logic Editor %x17"+
		   
		   "|%l"+ //254

		   "|File Browser %x5"+ //290

		   "|%l"+ //293

		   // "|Scripts Window %x14"//313
		   "|Python Console %x18"
		   );
}

public static uiHandleFunc spacefunc = new uiHandleFunc() {
public void run(bContext C, Object arg1, Object arg2)
{
	ED_area_newspace(C, bContext.CTX_wm_area(C), bContext.CTX_wm_area(C).butspacetype);
	ED_area_tag_redraw(bContext.CTX_wm_area(C));
	
	/*send space change notifyer*/
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SPACE|WmTypes.ND_SPACE_CHANGED, bContext.CTX_wm_area(C));
}};

/* returns offset for next button in header */
public static int ED_area_header_switchbutton(bContext C, uiBlock block, int yco)
{
	final ScrArea sa= bContext.CTX_wm_area(C);
	uiBut but;
	int xco= 8;

        Pointer<Byte> sa_butspacetype = new Pointer<Byte>() {
            public Byte get() {
                return sa.butspacetype;
            }
            public void set(Byte obj) {
                sa.butspacetype = obj;
            }
        };
	but= UI.uiDefIconTextButC(block, UI.ICONTEXTROW, 0, BIFIconID.ICON_VIEW3D,
						   editortype_pup(), xco, yco, Blender.XIC+10, Blender.YIC,
						   sa_butspacetype, 1.0f, SpaceTypes.SPACEICONMAX, 0, 0,
						   "Displays Current editor Type. "+
						   "Click for menu of available types.");
	UI.uiButSetFunc(but, spacefunc, null, null);

	return xco + Blender.XIC + 14;
}

public static int ED_area_header_standardbuttons(bContext C, uiBlock block, int yco)
{
	final ScrArea sa= bContext.CTX_wm_area(C);
	int xco= 8;

	if (sa.full==null)
		xco= ED_area_header_switchbutton(C, block, yco);

	UI.uiBlockSetEmboss(block, (short)UI.UI_EMBOSSN);

        Pointer<Short> sa_flag = new Pointer<Short>() {
            public Short get() {
                return sa.flag;
            }
            public void set(Short obj) {
                sa.flag = obj;
            }
        };
	if ((sa.flag & ScreenTypes.HEADER_NO_PULLDOWN)!=0) {
		UI.uiDefIconButBitS(block, UI.TOG, ScreenTypes.HEADER_NO_PULLDOWN, 0,
						 BIFIconID.ICON_DISCLOSURE_TRI_RIGHT,
						 xco,yco,Blender.XIC,Blender.YIC-2,
						 sa_flag, 0, 0, 0, 0,
						 "Show pulldown menus");
	}
	else {
		UI.uiDefIconButBitS(block, UI.TOG, ScreenTypes.HEADER_NO_PULLDOWN, 0,
						 BIFIconID.ICON_DISCLOSURE_TRI_DOWN,
						 xco,yco,Blender.XIC,Blender.YIC-2,
						 sa_flag, 0, 0, 0, 0,
						 "Hide pulldown menus");
	}

	UI.uiBlockSetEmboss(block, (short)UI.UI_EMBOSS);

	return xco + Blender.XIC;
}

/************************ standard UI regions ************************/

public static void ED_region_panels(GL2 gl, bContext C, ARegion ar, boolean vertical, String context, int contextnr)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	uiStyle style= (uiStyle)U.uistyles.first;
	uiBlock block;
	PanelType pt;
	Panel panel;
	View2D v2d= ar.v2d;
	View2DScrollers scrollers;
	int x, y, miny=0, w, em, header, triangle, newcontext= 0;
	int[] xco={0}, yco={0}, open={0};

	if(contextnr >= 0)
		newcontext= View2dUtil.UI_view2d_tab_set(v2d, contextnr);

	if(vertical) {
		w= (int)(v2d.cur.xmax - v2d.cur.xmin);
//		em= (((ARegionType)ar.type).prefsizex)? 10: 20;
		em= (((ARegionType)ar.type).minsizex)!=0? 10: 20;
	}
	else {
		w= UI.UI_PANEL_WIDTH;
//		em= (((ARegionType)ar.type).prefsizex)? 10: 20;
		em= (((ARegionType)ar.type).minsizex)!=0? 10: 20;
	}

	x= 0;
	y= -style.panelouter;

	/* create panels */
	UIPanel.uiBeginPanels(C, ar);

	/* set view2d view matrix for scrolling (without scrollers) */
	View2dUtil.UI_view2d_view_ortho(gl, v2d);

	for(pt= (PanelType)((ARegionType)ar.type).paneltypes.first; pt!=null; pt= pt.next) {
		/* verify context */
		if(context!=null)
			if(pt.context[0]!=0 && StringUtil.strcmp(StringUtil.toCString(context),0, pt.context,0) != 0)
				continue;

		/* draw panel */
		if(pt.draw!=null && (pt.poll==null || pt.poll.run(C, pt))) {
			block= UI.uiBeginBlock(C, ar, StringUtil.toJString(pt.idname,0), UI.UI_EMBOSS);
			panel= UIPanel.uiBeginPanel(sa, ar, block, pt, open);

			/* bad fixed values */
			header= (pt.flag & ScreenTypes.PNL_NO_HEADER)!=0? 0: 20;
			triangle= 22;

			if(vertical)
				y -= header;

			if(pt.draw_header!=null && header!=0 && (open[0]!=0 || vertical)) {
				/* for enabled buttons */
				panel.layout= UILayout.uiBlockLayout(block, UI.UI_LAYOUT_HORIZONTAL, UI.UI_LAYOUT_HEADER,
					triangle, header+style.panelspace, header, 1, style);

				pt.draw_header.run(C, panel);

				UILayout.uiBlockLayoutResolve(block, xco, yco);
				panel.labelofs= (short)(xco[0] - triangle);
				panel.layout= null;
			}

			if(open[0]!=0) {
				short panelContext;
				
				/* panel context can either be toolbar region or normal panels region */
				if (ar.regiontype == ScreenTypes.RGN_TYPE_TOOLS)
					panelContext= UI.UI_LAYOUT_TOOLBAR;
				else
					panelContext= UI.UI_LAYOUT_PANEL;
				
				panel.layout= UILayout.uiBlockLayout(block, UI.UI_LAYOUT_VERTICAL, panelContext,
					style.panelspace, 0, w-2*style.panelspace, em, style);

				pt.draw.run(C, panel);

				UILayout.uiBlockLayoutResolve(block, xco, yco);
				panel.layout= null;

				yco[0] -= 2*style.panelspace;
				UIPanel.uiEndPanel(block, w, -yco[0]);
			}
			else {
				yco[0]= 0;
				UIPanel.uiEndPanel(block, w, 0);
			}

			UI.uiEndBlock(C, block);

			if(vertical) {
				if((pt.flag & ScreenTypes.PNL_NO_HEADER)!=0)
					y += yco[0];
				else
					y += yco[0]-style.panelouter;
			}
			else {
				x += w;
				miny= UtilDefines.MIN2(y, yco[0]-style.panelouter-header);
			}
		}
	}

	if(vertical)
		x += w;
	else
		y= miny;
	
	/* in case there are no panels */
	if(x == 0 || y == 0) {
		x= UI.UI_PANEL_WIDTH;
		y= UI.UI_PANEL_WIDTH;
	}

	/* clear */
	Resources.UI_ThemeClearColor(gl, (((ARegionType)ar.type).regionid == ScreenTypes.RGN_TYPE_PREVIEW)?Resources.TH_BACK:Resources.TH_BACK);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
	
	/* before setting the view */
	if(vertical) {
		/* only allow scrolling in vertical direction */
		v2d.keepofs |= View2dTypes.V2D_LOCKOFS_X|View2dTypes.V2D_KEEPOFS_Y;
		v2d.keepofs &= ~(View2dTypes.V2D_LOCKOFS_Y|View2dTypes.V2D_KEEPOFS_X);
		v2d.scroll |= View2dTypes.V2D_SCROLL_HORIZONTAL_HIDE;
		v2d.scroll &= ~View2dTypes.V2D_SCROLL_VERTICAL_HIDE;
		
		// don't jump back when panels close or hide
		if(newcontext==0)
			y= (int)UtilDefines.MAX2(-y, -v2d.cur.ymin);
		else
			y= -y;
	}
	else {
		/* for now, allow scrolling in both directions (since layouts are optimised for vertical,
		 * they often don't fit in horizontal layout)
		 */
		v2d.keepofs &= ~(View2dTypes.V2D_LOCKOFS_X|View2dTypes.V2D_LOCKOFS_Y|View2dTypes.V2D_KEEPOFS_X|View2dTypes.V2D_KEEPOFS_Y);
		//v2d->keepofs |= V2D_LOCKOFS_Y|V2D_KEEPOFS_X;
		//v2d->keepofs &= ~(V2D_LOCKOFS_X|V2D_KEEPOFS_Y);
		v2d.scroll |= View2dTypes.V2D_SCROLL_VERTICAL_HIDE;
		v2d.scroll &= ~View2dTypes.V2D_SCROLL_HORIZONTAL_HIDE;
		
		// don't jump back when panels close or hide
		if(newcontext==0)
			x= (int)UtilDefines.MAX2(x, v2d.cur.xmax);
		y= -y;
	}

	// +V2D_SCROLL_HEIGHT is workaround to set the actual height
	View2dUtil.UI_view2d_totRect_set(v2d, x+View2dUtil.V2D_SCROLL_WIDTH, y+View2dUtil.V2D_SCROLL_HEIGHT);

	/* set the view */
	View2dUtil.UI_view2d_view_ortho(gl, v2d);

	/* this does the actual drawing! */
	UIPanel.uiEndPanels(gl, C, ar);
	
	/* restore view matrix */
	View2dUtil.UI_view2d_view_restore(gl, C);
	
	/* scrollers */
	scrollers= View2dUtil.UI_view2d_scrollers_calc(C, v2d, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY);
	View2dUtil.UI_view2d_scrollers_draw(gl, C, v2d, scrollers);
	View2dUtil.UI_view2d_scrollers_free(scrollers);
}

public static void ED_region_panels_init(wmWindowManager wm, ARegion ar)
{
	wmKeyMap keymap;

	// XXX quick hacks for files saved with 2.5 already (i.e. the builtin defaults file)
		// scrollbars for button regions
	ar.v2d.scroll |= (View2dTypes.V2D_SCROLL_RIGHT|View2dTypes.V2D_SCROLL_BOTTOM);
	ar.v2d.scroll |= View2dTypes.V2D_SCROLL_HORIZONTAL_HIDE;
	ar.v2d.scroll &= ~View2dTypes.V2D_SCROLL_VERTICAL_HIDE;
	ar.v2d.keepzoom |= View2dTypes.V2D_KEEPZOOM;
	
		// correctly initialised User-Prefs?
	if((ar.v2d.align & View2dTypes.V2D_ALIGN_NO_POS_Y)==0)
		ar.v2d.flag &= ~View2dTypes.V2D_IS_INITIALISED;

	View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_PANELS_UI, ar.winx, ar.winy);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View2D Buttons List", 0, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
}

public static void ED_region_header(GL2 gl, bContext C, ARegion ar)
{
	uiStyle style= (uiStyle)U.uistyles.first;
	uiBlock block;
	uiLayout layout;
	HeaderType ht;
	Header header = new Header();
//	float[] col=new float[3];
	int maxco;
	int[] xco={0}, yco={0};

	/* clear */
//	if(ScreenEdit.ED_screen_area_active(C))
//		Resources.UI_GetThemeColor3fv(Resources.TH_HEADER, col);
//	else
//		Resources.UI_GetThemeColor3fv(Resources.TH_HEADERDESEL, col);
//	gl.glClearColor(col[0], col[1], col[2], 0.0f);
	Resources.UI_ThemeClearColor(gl, (ScreenEdit.ED_screen_area_active(C))?Resources.TH_HEADER:Resources.TH_HEADERDESEL);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

	/* set view2d view matrix for scrolling (without scrollers) */
	View2dUtil.UI_view2d_view_ortho(gl, ar.v2d);

	xco[0]= maxco= 8;
	yco[0]= ScreenTypes.HEADERY-3;

	/* draw all headers types */
	for(ht= (HeaderType)((ARegionType)ar.type).headertypes.first; ht!=null; ht= ht.next) {
//		block= UI.uiBeginBlock(C, ar, "header buttons", UI.UI_EMBOSS);
		block= UI.uiBeginBlock(C, ar, StringUtil.toJString(ht.idname,0), UI.UI_EMBOSS);
		layout= UILayout.uiBlockLayout(block, UI.UI_LAYOUT_HORIZONTAL, UI.UI_LAYOUT_HEADER, xco[0], yco[0], ScreenTypes.HEADERY-6, 1, style);
		
		if(ht.draw!=null) {
			header.type= ht;
			header.layout= layout;
			//ht.draw.run(gl, C, header);
			ht.draw.run(C, header);
			
			/* for view2d */
			xco[0]= UILayout.uiLayoutGetWidth(layout);
			if(xco[0] > maxco)
				maxco= xco[0];
		}

		UILayout.uiBlockLayoutResolve(block, xco, yco);
		
		/* for view2d */
		if(xco[0] > maxco)
			maxco= xco[0];
		
		UI.uiEndBlock(C, block);
		UI.uiDrawBlock(gl, C, block);
	}

	/* always as last  */
	View2dUtil.UI_view2d_totRect_set(ar.v2d, maxco+Blender.XIC+80, (int)(ar.v2d.tot.ymax-ar.v2d.tot.ymin));

	/* restore view matrix? */
	View2dUtil.UI_view2d_view_restore(gl, C);
}

public static void ED_region_header_init(ARegion ar)
{
	View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_HEADER, ar.winx, ar.winy);
}

}
