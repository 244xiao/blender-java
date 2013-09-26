/**
 * blenlib/BKE_screen.h (mar-2001 nzc)
 *	
 * $Id: ScreenUtil.java,v 1.2 2009/09/18 05:20:13 jladere Exp $ 
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
package blender.blenkernel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import javax.media.opengl.GL2;

import blender.blenkernel.bContext.bContextDataCallback;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.DNA;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.View2D;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RNATypes.ExtensionRNA;
import blender.windowmanager.WmTypes.wmNotifier;

public class ScreenUtil {

/* spacetype has everything stored to get an editor working, it gets initialized via 
   ED_spacetypes_init() in editors/area/spacetypes.c   */
/* an editor in Blender is a combined ScrArea + SpaceType + SpaceData */

public static final int BKE_ST_MAXNAME=	32;

public static class SpaceType extends Link<SpaceType> {

    public static interface New {
        public SpaceLink run(bContext C);
    }

    public static interface Free {
        public void run(SpaceLink sl);
    }

    public static interface Init {
        public void run(wmWindowManager wm, ScrArea sa);
    }

    public static interface Listener {
        public void run(ScrArea sa, wmNotifier wmn);
    }

    public static interface Refresh {
        public void run(bContext C, ScrArea sa);
    }

    public static interface Duplicate {
        public SpaceLink run(SpaceLink sl);
    }

    public static interface OperatorTypes {
        public void run();
    }

    public static interface KeyMap {
//        public void run(wmWindowManager wm);
    	public void run(wmKeyConfig keyconf);
    }

//    public static interface Context {
//        public boolean run(bContext C, byte[] member, bContextDataResult result);
//    }

	public byte[]			name=new byte[BKE_ST_MAXNAME];		/* for menus */
	public int				spaceid;					/* unique space identifier */
//	int				iconid;						/* icon lookup for menus */
//
//	/* initial allocation, after this WM will call init() too */
//	struct SpaceLink	*(*new)(const struct bContext *C);
        public New newInstance;
	/* not free spacelink itself */
	public Free free;

//	/* init is to cope with file load, screen (size) changes, check handlers */
//	void		(*init)(struct wmWindowManager *, struct ScrArea *);
        public Init init;
//	/* Listeners can react to bContext changes */
//	void		(*listener)(struct ScrArea *, struct wmNotifier *);
        public Listener listener;
//
//	/* refresh context, called after filereads, ED_area_tag_refresh() */
//	void		(*refresh)(const struct bContext *, struct ScrArea *);
        public Refresh refresh;
//
//	/* after a spacedata copy, an init should result in exact same situation */
//	struct SpaceLink	*(*duplicate)(struct SpaceLink *);
        public Duplicate duplicate;
//
//	/* register operator types on startup */
//	void		(*operatortypes)(void);
        public OperatorTypes operatortypes;
//	/* add default items to WM keymap */
//	void		(*keymap)(struct wmWindowManager *);
        public KeyMap keymap;
//
//	/* return context data */
//	int			(*context)(const struct bContext *, const char*, struct bContextDataResult *);
        public bContextDataCallback context;

	/* region type definitions */
	public ListBase<ARegionType>	regiontypes = new ListBase<ARegionType>();

	/* tool shelf definitions */
	public ListBase toolshelf = new ListBase();

	/* read and write... */

	/* default keymaps to add */
	public int			keymapflag;

};

/* region types are also defined using spacetypes_init, via a callback */

public static class ARegionType extends Link<ARegionType> {

    public static interface Init {
        public void run(wmWindowManager wm, ARegion ar);
    }

    public static interface Draw {
        public void run(GL2 gl, bContext C, ARegion ar);
    }

    public static interface Listener {
        public void run(ARegion ar, wmNotifier wmn);
    }

    public static interface Free {
        public void run(ARegion ar);
    }

    public static interface Duplicate {
        public Object run(Object data);
    }

    public static interface OperatorTypes {
        public void run();
    }

    public static interface KeyMap {
//        public void run(wmWindowManager wm);
    	public void run(wmKeyConfig keyconf);
    }

    public static interface Cursor {
        public void run(wmWindow win, ScrArea sa, ARegion ar);
    }

//    public static interface Context {
//        public boolean run(bContext C, byte[] member, bContextDataResult dr);
//    }

	public int			regionid;	/* unique identifier within this space */
	
	/* add handlers, stuff you only do once or on area/region type/size changes */
        public Init init;
	/* draw entirely, view changes should be handled here */
        public Draw draw;
	/* contextual changes should be handled here */
	public Listener listener;
	
        public Free free;

	/* split region, copy data optionally */
        public Duplicate duplicate;

	/* register operator types on startup */
        public OperatorTypes operatortypes;
	/* add own items to keymap */
        public KeyMap keymap;
	/* allows default cursor per region */
        public Cursor cursor;

	/* return context data */
        public bContextDataCallback context;

	/* custom drawing callbacks */
	public ListBase	drawcalls = new ListBase();

	/* panels type definitions */
	public ListBase paneltypes = new ListBase();

	/* header type definitions */
	public ListBase<HeaderType> headertypes = new ListBase<HeaderType>();

	/* menu type definitions */
	public ListBase menutypes = new ListBase();
	
	/* hardcoded constraints, smaller than these values region is not visible */
	public int			minsizex, minsizey;
	/* default keymaps to add */
	public int			keymapflag;

        public void clear() {
            next = null;
            prev = null;
            regionid = 0;
            init = null;
            draw = null;
            listener = null;
            free = null;
            duplicate = null;
            operatortypes = null;
            keymap = null;
            cursor = null;
            context = null;
            drawcalls = new ListBase();
            paneltypes = new ListBase();
            headertypes = new ListBase<HeaderType>();
            menutypes = new ListBase();
            minsizex = minsizey = 0;
            keymapflag = 0;
        }
};

/* panel types */

public static class PanelType extends Link<PanelType> {

    public static interface Poll {
        public boolean run(bContext C, PanelType pt);
    }

    public static interface Draw {
        public void run(bContext C, Panel pa);
    }

	public byte[]		idname=new byte[BKE_ST_MAXNAME];		/* unique name */
	public byte[]		label=new byte[BKE_ST_MAXNAME];		/* for panel header */
	public byte[]		context=new byte[BKE_ST_MAXNAME];	/* for buttons window */
	public int			space_type;
	public int			region_type;

	public int 		flag;

	/* verify if the panel should draw or not */
	public Poll poll;
	/* draw header (optional) */
	public Draw draw_header;
	/* draw entirely, view changes should be handled here */
	public Draw draw;

	/* RNA integration */
	public ExtensionRNA ext = new ExtensionRNA();
};

/* header types */

public static class HeaderType extends Link<HeaderType> {

    public static interface Draw {
        //public void run(GL2 gl, bContext C, Header header);
        public void run(bContext C, Header header);
    }

	public byte[]		idname=new byte[BKE_ST_MAXNAME];	/* unique name */
	public int 		space_type;

	/* draw entirely, view changes should be handled here */
	public Draw		draw;

	/* RNA integration */
	public ExtensionRNA ext = new ExtensionRNA();
};

public static class Header {
	public HeaderType type;	/* runtime */
	public uiLayout layout;	/* runtime for drawing */
};


/* menu types */

public static class MenuType extends Link<MenuType> {
	
	public static interface Draw {
        public void run(bContext C, Menu menu);
    }

	public byte[]		idname=new byte[BKE_ST_MAXNAME];	/* unique name */
	public byte[]		label=new byte[BKE_ST_MAXNAME];	/* for button text */
	public int 		space_type;

	/* verify if the menu should draw or not */
//	int			(*poll)(const struct bContext *, struct MenuType *);
	/* draw entirely, view changes should be handled here */
	public Draw		draw;

	/* RNA integration */
	public ExtensionRNA ext = new ExtensionRNA();
};

public static class Menu {
	public MenuType type;		/* runtime */
	public uiLayout layout;	/* runtime for drawing */
};

/* ************ Spacetype/regiontype handling ************** */

/* keep global; this has to be accessible outside of windowmanager */
static ListBase<SpaceType> spacetypes= new ListBase<SpaceType>();

/* not SpaceType itself */
static void spacetype_free(SpaceType st)
{
	ARegionType art;
//	PanelType pt;
//	HeaderType ht;
//	MenuType mt;

	for(art= st.regiontypes.first; art!=null; art= art.next) {
		ListBaseUtil.BLI_freelistN(art.drawcalls);

//		for(pt= art.paneltypes.first; pt; pt= pt.next)
//			if(pt.ext.free)
//				pt.ext.free(pt.ext.data);
//
//		for(ht= art.headertypes.first; ht; ht= ht.next)
//			if(ht.ext.free)
//				ht.ext.free(ht.ext.data);
//
//		for(mt= art.menutypes.first; mt; mt= mt.next)
//			if(mt.ext.free)
//				mt.ext.free(mt.ext.data);

		ListBaseUtil.BLI_freelistN(art.paneltypes);
		ListBaseUtil.BLI_freelistN(art.headertypes);
		ListBaseUtil.BLI_freelistN(art.menutypes);
	}

	ListBaseUtil.BLI_freelistN(st.regiontypes);
//	ListBaseUtil.BLI_freelistN(st.toolshelf);

}

//void BKE_spacetypes_free(void)
//{
//	SpaceType *st;
//
//	for(st= spacetypes.first; st; st= st.next) {
//		spacetype_free(st);
//	}
//
//	BLI_freelistN(&spacetypes);
//}

public static SpaceType BKE_spacetype_from_id(int spaceid)
{
	SpaceType st;

	for(st= spacetypes.first; st!=null; st= st.next) {
		if(st.spaceid==spaceid)
			return st;
	}
	return null;
}

public static ARegionType BKE_regiontype_from_id(SpaceType st, int regionid)
{
	ARegionType art;

	for(art= st.regiontypes.first; art!=null; art= art.next)
		if(art.regionid==regionid)
			return art;

//	System.out.printf("Error, region type missing in - name:\"%s\", id:%d\n", st.name, st.spaceid);
	return st.regiontypes.first;
}


public static ListBase<SpaceType> BKE_spacetypes_list()
{
	return spacetypes;
}

public static void BKE_spacetype_register(SpaceType st)
{
	SpaceType stype;

	/* sanity check */
	stype= BKE_spacetype_from_id(st.spaceid);
	if(stype!=null) {
		System.out.printf("error: redefinition of spacetype %s\n", StringUtil.toJString(stype.name,0));
		spacetype_free(stype);
//		MEM_freeN(stype);
	}

	ListBaseUtil.BLI_addtail(spacetypes, st);
}

/* ***************** Space handling ********************** */

public static void BKE_spacedata_freelist(ListBase<SpaceLink> lb)
{
	SpaceLink sl;
	ARegion ar;

	for (sl= lb.first; sl!=null; sl= (SpaceLink)sl.next) {
		SpaceType st= BKE_spacetype_from_id(sl.spacetype);

		/* free regions for pushed spaces */
		for(ar=(ARegion)sl.regionbase.first; ar!=null; ar=ar.next)
			BKE_area_region_free(st, ar);

		ListBaseUtil.BLI_freelistN(sl.regionbase);

		if(st!=null)
			st.free.run(sl);
	}

	ListBaseUtil.BLI_freelistN(lb);
}

public static ARegion BKE_area_region_copy(SpaceType st, ARegion ar)
{
//	ARegion newar= ar.copy();
        ARegion newar= aRegionCopy(ar);
	Panel pa, newpa, patab;

	newar.prev= newar.next= null;
	newar.handlers.first= newar.handlers.last= null;
	newar.uiblocks.first= newar.uiblocks.last= null;
	newar.swinid= 0;

	/* use optional regiondata callback */
	if(ar.regiondata!=null) {
		ARegionType art= BKE_regiontype_from_id(st, ar.regiontype);

		if(art!=null && art.duplicate!=null)
			newar.regiondata= art.duplicate.run(ar.regiondata);
		else {
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ((DNA)ar.regiondata).write(new DataOutputStream(out));
                        ((DNA)newar.regiondata).read(ByteBuffer.wrap(out.toByteArray()));
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
	}

	newar.panels.first= newar.panels.last= null;
	ListBaseUtil.BLI_duplicatelist(newar.panels, ar.panels);

	/* copy panel pointers */
	for(newpa= (Panel)newar.panels.first; newpa!=null; newpa= newpa.next) {
		patab= (Panel)newar.panels.first;
		pa= (Panel)ar.panels.first;
		while(patab!=null) {
			if(newpa.paneltab == pa) {
				newpa.paneltab = patab;
				break;
			}
			patab= patab.next;
			pa= pa.next;
		}
	}

	return newar;
}


/* from lb2 to lb1, lb1 is supposed to be free'd */
static void region_copylist(SpaceType st, ListBase lb1, ListBase lb2)
{
	ARegion ar;

	/* to be sure */
	lb1.first= lb1.last= null;

	for(ar= (ARegion)lb2.first; ar!=null; ar= ar.next) {
		ARegion arnew= BKE_area_region_copy(st, ar);
		ListBaseUtil.BLI_addtail(lb1, arnew);
	}
}


/* lb1 should be empty */
public static void BKE_spacedata_copylist(ListBase lb1, ListBase lb2)
{
	SpaceLink sl;

	lb1.first= lb1.last= null;	/* to be sure */

	for (sl= (SpaceLink)lb2.first; sl!=null; sl= (SpaceLink)sl.next) {
		SpaceType st= BKE_spacetype_from_id(sl.spacetype);

		if(st!=null && st.duplicate!=null) {
			SpaceLink slnew= st.duplicate.run(sl);

			ListBaseUtil.BLI_addtail(lb1, slnew);

			region_copylist(st, slnew.regionbase, sl.regionbase);
		}
	}
}

/* lb1 should be empty */
public static void BKE_spacedata_copyfirst(ListBase lb1, ListBase lb2)
{
	SpaceLink sl;

	lb1.first= lb1.last= null;	/* to be sure */

	sl= (SpaceLink)lb2.first;
	if(sl!=null) {
		SpaceType st= BKE_spacetype_from_id(sl.spacetype);

		if(st!=null && st.duplicate!=null) {
			SpaceLink slnew= st.duplicate.run(sl);

			ListBaseUtil.BLI_addtail(lb1, slnew);

			region_copylist(st, slnew.regionbase, sl.regionbase);
		}
	}
}

/* not region itself */
public static void BKE_area_region_free(SpaceType st, ARegion ar)
{
	if(st!=null) {
		ARegionType art= BKE_regiontype_from_id(st, ar.regiontype);

		if(art != null && art.free != null)
			art.free.run(ar);

		if(ar.regiondata!=null)
			System.out.printf("regiondata free error\n");
	}
	else if(ar.type != null && ((ARegionType)ar.type).free != null)
		((ARegionType)ar.type).free.run(ar);
	
	if(ar.v2d.tab_offset != null) {
//		MEM_freeN(ar.v2d.tab_offset);
		ar.v2d.tab_offset= null;
	}

	if(ar!=null) {
		ListBaseUtil.BLI_freelistN(ar.panels);
	}
}

/* not area itself */
public static void BKE_screen_area_free(ScrArea sa)
{
	SpaceType st= BKE_spacetype_from_id(sa.spacetype);
	ARegion ar;

	for(ar=(ARegion)sa.regionbase.first; ar!=null; ar=ar.next)
		BKE_area_region_free(st, ar);

	ListBaseUtil.BLI_freelistN(sa.regionbase);

	BKE_spacedata_freelist(sa.spacedata);

	ListBaseUtil.BLI_freelistN(sa.actionzones);
}

/* don't free screen itself */
public static void free_screen(bScreen sc)
{
	ScrArea sa, san;
	ARegion ar;

	for(ar=(ARegion)sc.regionbase.first; ar!=null; ar=ar.next)
		BKE_area_region_free(null, ar);

	ListBaseUtil.BLI_freelistN(sc.regionbase);

	for(sa= (ScrArea)sc.areabase.first; sa!=null; sa= san) {
		san= sa.next;
		BKE_screen_area_free(sa);
	}

	ListBaseUtil.BLI_freelistN(sc.vertbase);
	ListBaseUtil.BLI_freelistN(sc.edgebase);
	ListBaseUtil.BLI_freelistN(sc.areabase);
}

/* ***************** Utilities ********************** */

/* Find a region of the specified type from the given area */
public static ARegion BKE_area_find_region_type(ScrArea sa, int type)
{
	if (sa!=null) {
		ARegion ar;
		
		for (ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if (ar.regiontype == type)
				return ar;
		}
	}
	return null;
}

static ARegion aRegionCopy(ARegion aRegion) {
    ARegion ar = new ARegion();
    ar.prev = aRegion.prev;
    ar.next = aRegion.next;
    ar.v2d = view2DCopy(aRegion.v2d);
    ar.winrct = aRegion.winrct.copy();
    ar.drawrct = aRegion.drawrct.copy();
    ar.winx = aRegion.winx;
    ar.winy = aRegion.winy;
    ar.swinid = aRegion.swinid;
    ar.regiontype = aRegion.regiontype;
    ar.alignment = aRegion.alignment;
    ar.flag = aRegion.flag;
    ar.fsize = aRegion.fsize;
    ar.do_draw = aRegion.do_draw;
    ar.swap = aRegion.swap;
//    ar.pad1 = aRegion.pad1;
    ar.type = aRegion.type;
    ar.uiblocks = aRegion.uiblocks.copy();
    ar.panels = aRegion.panels.copy();
    ar.handlers = aRegion.handlers.copy();
    ar.headerstr = aRegion.headerstr;
    ar.regiondata = aRegion.regiondata;
    return ar;
}

static View2D view2DCopy(View2D view2D) {
    View2D v2d = new View2D();
    v2d.tot = view2D.tot.copy();
    v2d.cur = view2D.cur.copy();
    v2d.vert = view2D.vert.copy();
    v2d.hor = view2D.hor.copy();
    v2d.mask = view2D.mask.copy();
    System.arraycopy(view2D.min, 0, v2d.min, 0, v2d.min.length);
    System.arraycopy(view2D.max, 0, v2d.max, 0, v2d.max.length);
    v2d.minzoom = view2D.minzoom;
    v2d.maxzoom = view2D.maxzoom;
    v2d.scroll = view2D.scroll;
    v2d.scroll_ui = view2D.scroll_ui;
    v2d.keeptot = view2D.keeptot;
    v2d.keepzoom = view2D.keepzoom;
    v2d.keepofs = view2D.keepofs;
    v2d.flag = view2D.flag;
    v2d.align = view2D.align;
    v2d.winx = view2D.winx;
    v2d.winy = view2D.winy;
    v2d.oldwinx = view2D.oldwinx;
    v2d.oldwiny = view2D.oldwiny;
    v2d.around = view2D.around;
//    System.arraycopy(view2D.cursor, 0, v2d.cursor, 0, v2d.cursor.length);
    return v2d;
}

}
