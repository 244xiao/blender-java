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
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.screen;

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL2;

import blender.blenkernel.LibraryUtil;
import blender.blenkernel.SceneUtil;
import blender.blenkernel.ScreenUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area.AZone;
import blender.makesdna.DNA_ID;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.UserDefTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.ScrEdge;
import blender.makesdna.sdna.ScrVert;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.vec2f;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmCursors;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmSubWindow;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmEvent;
import blender.windowmanager.WmTypes.wmNotifier;

public class ScreenEdit {

/* ******************* screen vert, edge, area managing *********************** */

static ScrVert screen_addvert(bScreen sc, int x, int y)
{
	ScrVert sv= new ScrVert();
	sv.vec.x= (short)x;
	sv.vec.y= (short)y;

	ListBaseUtil.BLI_addtail(sc.vertbase, sv);
	return sv;
}

//static void sortscrvert(ScrVert **v1, ScrVert **v2)
//{
//	ScrVert *tmp;
//
//	if (*v1 > *v2) {
//		tmp= *v1;
//		*v1= *v2;
//		*v2= tmp;
//	}
//}

static ScrEdge screen_addedge(bScreen sc, ScrVert v1, ScrVert v2)
{
	ScrEdge se= new ScrEdge();

//	sortscrvert(&v1, &v2);
	se.v1= v1;
	se.v2= v2;

	ListBaseUtil.BLI_addtail(sc.edgebase, se);
	return se;
}


public static ScrEdge screen_findedge(bScreen sc, ScrVert v1, ScrVert v2)
{
//	sortscrvert(&v1, &v2);
	for (ScrEdge se= (ScrEdge)sc.edgebase.first; se!=null; se= se.next)
		if((se.v1==v1 && se.v2==v2) || (se.v1 == v2 && se.v2 == v1))
			return se;

	return null;
}

public static void removedouble_scrverts(bScreen sc)
{
	ScrVert v1, verg;
	ScrEdge se;
	ScrArea sa;

	verg= (ScrVert)sc.vertbase.first;
	while(verg!=null) {
		if(verg.newv==null) {	/* !!! */
			v1= verg.next;
			while(v1!=null) {
				if(v1.newv==null) {	/* !?! */
					if(v1.vec.x==verg.vec.x && v1.vec.y==verg.vec.y) {
						/* printf("doublevert\n"); */
						v1.newv= verg;
					}
				}
				v1= v1.next;
			}
		}
		verg= verg.next;
	}

	/* replace pointers in edges and faces */
	se= (ScrEdge)sc.edgebase.first;
	while(se!=null) {
		if(se.v1.newv!=null) se.v1= se.v1.newv;
		if(se.v2.newv!=null) se.v2= se.v2.newv;
		/* edges changed: so.... */
//		sortscrvert(&(se.v1), &(se.v2));
		se= se.next;
	}
	sa= (ScrArea)sc.areabase.first;
	while(sa!=null) {
		if(sa.v1.newv!=null) sa.v1= sa.v1.newv;
		if(sa.v2.newv!=null) sa.v2= sa.v2.newv;
		if(sa.v3.newv!=null) sa.v3= sa.v3.newv;
		if(sa.v4.newv!=null) sa.v4= sa.v4.newv;
		sa= sa.next;
	}

	/* remove */
	verg= (ScrVert)sc.vertbase.first;
	while(verg!=null) {
		v1= verg.next;
		if(verg.newv!=null) {
			ListBaseUtil.BLI_remlink(sc.vertbase, verg);
//			MEM_freeN(verg);
		}
		verg= v1;
	}

}

public static void removenotused_scrverts(bScreen sc)
{
	ScrVert sv, svn;
	ScrEdge se;

	/* we assume edges are ok */

	se= (ScrEdge)sc.edgebase.first;
	while(se!=null) {
		se.v1.flag= 1;
		se.v2.flag= 1;
		se= se.next;
	}

	sv= (ScrVert)sc.vertbase.first;
	while(sv!=null) {
		svn= sv.next;
		if(sv.flag==0) {
			ListBaseUtil.BLI_remlink(sc.vertbase, sv);
//			MEM_freeN(sv);
		}
		else sv.flag= 0;
		sv= svn;
	}
}

public static void removedouble_scredges(bScreen sc)
{
	ScrEdge verg, se, sn;

	/* compare */
	verg= (ScrEdge)sc.edgebase.first;
	while(verg!=null) {
		se= verg.next;
		while(se!=null) {
			sn= se.next;
			if((verg.v1==se.v1 && verg.v2==se.v2) || (verg.v1==se.v2 && verg.v2==se.v1)) {
				ListBaseUtil.BLI_remlink(sc.edgebase, se);
//				MEM_freeN(se);
			}
			se= sn;
		}
		verg= verg.next;
	}
}

public static void removenotused_scredges(bScreen sc)
{
	ScrEdge se, sen;
	ScrArea sa;
	int a=0;

	/* sets flags when edge is used in area */
	sa= (ScrArea)sc.areabase.first;
	while(sa!=null) {
		se= screen_findedge(sc, sa.v1, sa.v2);
		if(se==null) System.out.printf("error: area %d edge 1 doesn't exist\n", a);
		else se.flag= 1;
		se= screen_findedge(sc, sa.v2, sa.v3);
		if(se==null) System.out.printf("error: area %d edge 2 doesn't exist\n", a);
		else se.flag= 1;
		se= screen_findedge(sc, sa.v3, sa.v4);
		if(se==null) System.out.printf("error: area %d edge 3 doesn't exist\n", a);
		else se.flag= 1;
		se= screen_findedge(sc, sa.v4, sa.v1);
		if(se==null) System.out.printf("error: area %d edge 4 doesn't exist\n", a);
		else se.flag= 1;
		sa= sa.next;
		a++;
	}
	se= (ScrEdge)sc.edgebase.first;
	while(se!=null) {
		sen= se.next;
		if(se.flag==0) {
			ListBaseUtil.BLI_remlink(sc.edgebase, se);
//			MEM_freeN(se);
		}
		else se.flag= 0;
		se= sen;
	}
}

    public static boolean scredge_is_horizontal(ScrEdge se) {
        return (se.v1.vec.y == se.v2.vec.y);
    }

public static ScrEdge screen_find_active_scredge(bScreen sc, int mx, int my)
{
	ScrEdge se;

	for (se= (ScrEdge)sc.edgebase.first; se!=null; se= se.next) {
		if (scredge_is_horizontal(se)) {
			short min, max;
			min= (short)UtilDefines.MIN2(se.v1.vec.x, se.v2.vec.x);
			max= (short)UtilDefines.MAX2(se.v1.vec.x, se.v2.vec.x);

			if (StrictMath.abs(my-se.v1.vec.y)<=2 && mx>=min && mx<=max)
				return se;
		}
		else {
			short min, max;
			min= (short)UtilDefines.MIN2(se.v1.vec.y, se.v2.vec.y);
			max= (short)UtilDefines.MAX2(se.v1.vec.y, se.v2.vec.y);

			if (StrictMath.abs(mx-se.v1.vec.x)<=2 && my>=min && my<=max)
				return se;
		}
	}

	return null;
}



/* adds no space data */
static ScrArea screen_addarea(bScreen sc, ScrVert v1, ScrVert v2, ScrVert v3, ScrVert v4, int headertype, int spacetype)
{
	ScrArea sa= new ScrArea();
	sa.v1= v1;
	sa.v2= v2;
	sa.v3= v3;
	sa.v4= v4;
	sa.headertype= (short)headertype;
	sa.spacetype= sa.butspacetype= (byte)spacetype;

	ListBaseUtil.BLI_addtail(sc.areabase, sa);

	return sa;
}

static void screen_delarea(bContext C, bScreen sc, ScrArea sa)
{

	ED_area_exit(C, sa);

	ScreenUtil.BKE_screen_area_free(sa);

	ListBaseUtil.BLI_remlink(sc.areabase, sa);
//	MEM_freeN(sa);
}

/* return 0: no split possible */
/* else return (integer) screencoordinate split point */
static short testsplitpoint(wmWindow win, ScrArea sa, char dir, float fac)
{
	short x, y;

	// area big enough?
	if(dir=='v' && (sa.v4.vec.x- sa.v1.vec.x <= 2*ScreenTypes.AREAMINX)) return 0;
	if(dir=='h' && (sa.v2.vec.y- sa.v1.vec.y <= 2*ScreenTypes.AREAMINY)) return 0;

	// to be sure
	if(fac<0.0f) fac= 0.0f;
	if(fac>1.0f) fac= 1.0f;

	if(dir=='h') {
		y= (short)(sa.v1.vec.y+ fac*(sa.v2.vec.y- sa.v1.vec.y));

		if(y- sa.v1.vec.y < ScreenTypes.AREAMINY)
			y= (short)(sa.v1.vec.y+ ScreenTypes.AREAMINY);
		else if(sa.v2.vec.y- y < ScreenTypes.AREAMINY)
			y= (short)(sa.v2.vec.y- ScreenTypes.AREAMINY);
		else y-= (y % ScreenTypes.AREAGRID);

		return y;
	}
	else {
		x= (short)(sa.v1.vec.x+ fac*(sa.v4.vec.x- sa.v1.vec.x));

		if(x- sa.v1.vec.x < ScreenTypes.AREAMINX)
			x= (short)(sa.v1.vec.x+ ScreenTypes.AREAMINX);
		else if(sa.v4.vec.x- x < ScreenTypes.AREAMINX)
			x= (short)(sa.v4.vec.x- ScreenTypes.AREAMINX);
		else x-= (x % ScreenTypes.AREAGRID);

		return x;
	}
}

public static ScrArea area_split(wmWindow win, bScreen sc, ScrArea sa, char dir, float fac)
{
	ScrArea newa=null;
	ScrVert sv1, sv2;
	short split;

	if(sa==null) return null;

	split= testsplitpoint(win, sa, dir, fac);
	if(split==0) return null;

	if(dir=='h') {
		/* new vertices */
		sv1= screen_addvert(sc, sa.v1.vec.x, split);
		sv2= screen_addvert(sc, sa.v4.vec.x, split);

		/* new edges */
		screen_addedge(sc, sa.v1, sv1);
		screen_addedge(sc, sv1, sa.v2);
		screen_addedge(sc, sa.v3, sv2);
		screen_addedge(sc, sv2, sa.v4);
		screen_addedge(sc, sv1, sv2);

		/* new areas: top */
		newa= screen_addarea(sc, sv1, sa.v2, sa.v3, sv2, sa.headertype, sa.spacetype);
		Area.area_copy_data(newa, sa, 0);

		/* area below */
		sa.v2= sv1;
		sa.v3= sv2;

	}
	else {
		/* new vertices */
		sv1= screen_addvert(sc, split, sa.v1.vec.y);
		sv2= screen_addvert(sc, split, sa.v2.vec.y);

		/* new edges */
		screen_addedge(sc, sa.v1, sv1);
		screen_addedge(sc, sv1, sa.v4);
		screen_addedge(sc, sa.v2, sv2);
		screen_addedge(sc, sv2, sa.v3);
		screen_addedge(sc, sv1, sv2);

		/* new areas: left */
		newa= screen_addarea(sc, sa.v1, sa.v2, sv2, sv1, sa.headertype, sa.spacetype);
		Area.area_copy_data(newa, sa, 0);

		/* area right */
		sa.v1= sv1;
		sa.v2= sv2;
	}

	/* remove double vertices en edges */
	removedouble_scrverts(sc);
	removedouble_scredges(sc);
	removenotused_scredges(sc);

	return newa;
}

/* empty screen, with 1 dummy area without spacedata */
/* uses window size */
public static bScreen ED_screen_add(wmWindow win, Scene scene, byte[] name, int offset)
{
	bScreen sc;
	ScrVert sv1, sv2, sv3, sv4;

	sc= (bScreen)LibraryUtil.alloc_libblock(G.main.screen, DNA_ID.ID_SCR, name, offset);
	sc.scene= scene;
	sc.do_refresh= 1;
	sc.winid= (short)win.winid;

	sv1= screen_addvert(sc, 0, 0);
	sv2= screen_addvert(sc, 0, win.sizey-1);
	sv3= screen_addvert(sc, win.sizex-1, win.sizey-1);
	sv4= screen_addvert(sc, win.sizex-1, 0);

	screen_addedge(sc, sv1, sv2);
	screen_addedge(sc, sv2, sv3);
	screen_addedge(sc, sv3, sv4);
	screen_addedge(sc, sv4, sv1);

	/* dummy type, no spacedata */
	screen_addarea(sc, sv1, sv2, sv3, sv4, ScreenTypes.HEADERDOWN, SpaceTypes.SPACE_EMPTY);

	return sc;
}

static void screen_copy(bScreen to, bScreen from)
{
	ScrVert s1, s2;
	ScrEdge se;
	ScrArea sa, saf;

	/* free contents of 'to', is from blenkernel screen.c */
	ScreenUtil.free_screen(to);

	ListBaseUtil.BLI_duplicatelist(to.vertbase, from.vertbase);
	ListBaseUtil.BLI_duplicatelist(to.edgebase, from.edgebase);
	ListBaseUtil.BLI_duplicatelist(to.areabase, from.areabase);
	to.regionbase.first= to.regionbase.last= null;

	s2= (ScrVert)to.vertbase.first;
	for(s1= (ScrVert)from.vertbase.first; s1!=null; s1= s1.next, s2= s2.next) {
		s1.newv= s2;
	}

	for(se= (ScrEdge)to.edgebase.first; se!=null; se= se.next) {
		se.v1= se.v1.newv;
		se.v2= se.v2.newv;
//		sortscrvert(&(se.v1), &(se.v2));
	}

	saf= (ScrArea)from.areabase.first;
	for(sa= (ScrArea)to.areabase.first; sa!=null; sa= sa.next, saf= saf.next) {
		sa.v1= sa.v1.newv;
		sa.v2= sa.v2.newv;
		sa.v3= sa.v3.newv;
		sa.v4= sa.v4.newv;

		sa.spacedata.first= sa.spacedata.last= null;
		sa.regionbase.first= sa.regionbase.last= null;
		sa.actionzones.first= sa.actionzones.last= null;

		Area.area_copy_data(sa, saf, 0);
	}

	/* put at zero (needed?) */
	for(s1= (ScrVert)from.vertbase.first; s1!=null; s1= s1.next)
		s1.newv= null;

}


/* with sa as center, sb is located at: 0=W, 1=N, 2=E, 3=S */
/* -1 = not valid check */
/* used with join operator */
public static int area_getorientation(bScreen screen, ScrArea sa, ScrArea sb)
{
	ScrVert sav1, sav2, sav3, sav4;
	ScrVert sbv1, sbv2, sbv3, sbv4;

	if(sa==null || sb==null) return -1;

	sav1= sa.v1;
	sav2= sa.v2;
	sav3= sa.v3;
	sav4= sa.v4;
	sbv1= sb.v1;
	sbv2= sb.v2;
	sbv3= sb.v3;
	sbv4= sb.v4;

	if(sav1==sbv4 && sav2==sbv3) { /* sa to right of sb = W */
		return 0;
	}
	else if(sav2==sbv1 && sav3==sbv4) { /* sa to bottom of sb = N */
		return 1;
	}
	else if(sav3==sbv2 && sav4==sbv1) { /* sa to left of sb = E */
		return 2;
	}
	else if(sav1==sbv2 && sav4==sbv3) { /* sa on top of sb = S*/
		return 3;
	}

	return -1;
}

/* Helper function to join 2 areas, it has a return value, 0=failed 1=success
* 	used by the split, join operators
*/
public static boolean screen_area_join(bContext C, bScreen scr, ScrArea sa1, ScrArea sa2)
{
	int dir;

	dir = area_getorientation(scr, sa1, sa2);
	/*printf("dir is : %i \n", dir);*/

	if (dir < 0)
	{
		if (sa1!=null ) sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;
		if (sa2!=null ) sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
		return false;
	}

	if(dir == 0) {
		sa1.v1= sa2.v1;
		sa1.v2= sa2.v2;
		screen_addedge(scr, sa1.v2, sa1.v3);
		screen_addedge(scr, sa1.v1, sa1.v4);
	}
	else if(dir == 1) {
		sa1.v2= sa2.v2;
		sa1.v3= sa2.v3;
		screen_addedge(scr, sa1.v1, sa1.v2);
		screen_addedge(scr, sa1.v3, sa1.v4);
	}
	else if(dir == 2) {
		sa1.v3= sa2.v3;
		sa1.v4= sa2.v4;
		screen_addedge(scr, sa1.v2, sa1.v3);
		screen_addedge(scr, sa1.v1, sa1.v4);
	}
	else if(dir == 3) {
		sa1.v1= sa2.v1;
		sa1.v4= sa2.v4;
		screen_addedge(scr, sa1.v1, sa1.v2);
		screen_addedge(scr, sa1.v3, sa1.v4);
	}

	screen_delarea(C, scr, sa2);
	removedouble_scrverts(scr);
	sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;

	return true;
}

public static void select_connected_scredge(bScreen sc, ScrEdge edge)
{
	ScrEdge se;
	ScrVert sv;
	boolean oneselected;
	char dir;

	/* select connected, only in the right direction */
	/* 'dir' is the direction of EDGE */

	if(edge.v1.vec.x==edge.v2.vec.x) dir= 'v';
	else dir= 'h';

	sv= (ScrVert)sc.vertbase.first;
	while(sv!=null) {
		sv.flag = 0;
		sv= sv.next;
	}

	edge.v1.flag= 1;
	edge.v2.flag= 1;

	oneselected= true;
	while(oneselected) {
		se= (ScrEdge)sc.edgebase.first;
		oneselected= false;
		while(se!=null) {
			if(se.v1.flag + se.v2.flag==1) {
				if(dir=='h') if(se.v1.vec.y==se.v2.vec.y) {
					se.v1.flag= se.v2.flag= 1;
					oneselected= true;
				}
					if(dir=='v') if(se.v1.vec.x==se.v2.vec.x) {
						se.v1.flag= se.v2.flag= 1;
						oneselected= true;
					}
			}
				se= se.next;
		}
	}
}

/* helper call for below, correct buttons view */
private static void screen_test_scale_region(ListBase<ARegion> regionbase, float facx, float facy)
{
	for(ARegion ar= regionbase.first; ar!=null; ar= ar.next) {
		if(ar.regiontype==ScreenTypes.RGN_TYPE_WINDOW) {
			ar.v2d.cur.xmin *= facx;
			ar.v2d.cur.xmax *= facx;
			ar.v2d.cur.ymin *= facy;
			ar.v2d.cur.ymax *= facy;
		}
	}
}

/* test if screen vertices should be scaled */
public static void screen_test_scale(bScreen sc, int winsizex, int winsizey)
{
	ScrVert sv=null;
	ScrArea sa;
	int sizex, sizey;
	float facx, facy, tempf;
        float[] min = new float[2], max = new float[2];

	/* calculate size */
	min[0]= min[1]= 10000.0f;
	max[0]= max[1]= 0.0f;

	for(sv= (ScrVert)sc.vertbase.first; sv!=null; sv= sv.next) {
		min[0]= UtilDefines.MIN2(min[0], sv.vec.x);
		min[1]= UtilDefines.MIN2(min[1], sv.vec.y);
		max[0]= UtilDefines.MAX2(max[0], sv.vec.x);
		max[1]= UtilDefines.MAX2(max[1], sv.vec.y);
	}

	/* always make 0.0 left under */
	for(sv= (ScrVert)sc.vertbase.first; sv!=null; sv= sv.next) {
		sv.vec.x -= min[0];
		sv.vec.y -= min[1];
	}

	sizex= (short)(max[0]-min[0]);
	sizey= (short)(max[1]-min[1]);

	if(sizex!= winsizex || sizey!= winsizey) {
		facx= winsizex;
		facx/= (float)sizex;
		facy= winsizey;
		facy/= (float)sizey;

		/* make sure it fits! */
		for(sv= (ScrVert)sc.vertbase.first; sv!=null; sv= sv.next) {
			tempf= ((float)sv.vec.x)*facx;
			sv.vec.x= (short)(tempf+0.5);
			sv.vec.x+= ScreenTypes.AREAGRID-1;
			sv.vec.x-=  (sv.vec.x % ScreenTypes.AREAGRID);

			sv.vec.x = (short)Arithb.CLAMP(sv.vec.x, 0, winsizex);

			tempf= ((float)sv.vec.y )*facy;
			sv.vec.y= (short)(tempf+0.5);
			sv.vec.y+= ScreenTypes.AREAGRID-1;
			sv.vec.y-=  (sv.vec.y % ScreenTypes.AREAGRID);

			sv.vec.y = (short)Arithb.CLAMP(sv.vec.y, 0, winsizey);
		}

		/* keep buttons view2d same size */
		for(sa= (ScrArea)sc.areabase.first; sa!=null; sa= sa.next) {
			SpaceLink sl;

			if(sa.spacetype==SpaceTypes.SPACE_BUTS)
				screen_test_scale_region(sa.regionbase, facx, facy);

			for(sl= (SpaceLink)sa.spacedata.first; sl!=null; sl= (SpaceLink)sl.next)
				if(sl.spacetype==SpaceTypes.SPACE_BUTS)
					screen_test_scale_region(sl.regionbase, facx, facy);
		}
	}

	/* test for collapsed areas. This could happen in some blender version... */
	/* ton: removed option now, it needs Context... */

	/* make each window at least HEADERY high */
	for(sa= (ScrArea)sc.areabase.first; sa!=null; sa= sa.next) {
		int headery= ScreenTypes.HEADERY+1;

		if(sa.v1.vec.y+headery > sa.v2.vec.y) {
			/* lower edge */
			ScrEdge se= screen_findedge(sc, sa.v4, sa.v1);
			if(se!=null && sa.v1!=sa.v2 ) {
				int yval;

				select_connected_scredge(sc, se);

				/* all selected vertices get the right offset */
				yval= sa.v2.vec.y-headery;
				sv= (ScrVert)sc.vertbase.first;
				while(sv!=null) {
					/* if is a collapsed area */
					if(sv!=sa.v2 && sv!=sa.v3) {
						if(sv.flag!=0) sv.vec.y= (short)yval;
					}
					sv= sv.next;
				}
			}
		}
	}
}

/* *********************** DRAWING **************************************** */


public static final float SCR_BACK= 0.55f;
public static final int SCR_ROUND= 12;

/* draw vertical shape visualising future joining (left as well
 * right direction of future joining) */
public static void draw_horizontal_join_shape(GL2 gl, ScrArea sa, char dir)
{
	vec2f[] points=new vec2f[10];
	short i;
	float w, h;
	float width = sa.v3.vec.x - sa.v1.vec.x;
	float height = sa.v3.vec.y - sa.v1.vec.y;

	if(height<width) {
		h = height/8;
		w = height/4;
	}
	else {
		h = width/8;
		w = width/4;
	}

        points[0] = new vec2f();
	points[0].x = sa.v1.vec.x;
	points[0].y = sa.v1.vec.y + height/2;

        points[1] = new vec2f();
	points[1].x = sa.v1.vec.x;
	points[1].y = sa.v1.vec.y;

        points[2] = new vec2f();
	points[2].x = sa.v4.vec.x - w;
	points[2].y = sa.v4.vec.y;

        points[3] = new vec2f();
	points[3].x = sa.v4.vec.x - w;
	points[3].y = sa.v4.vec.y + height/2 - 2*h;

        points[4] = new vec2f();
	points[4].x = sa.v4.vec.x - 2*w;
	points[4].y = sa.v4.vec.y + height/2;

        points[5] = new vec2f();
	points[5].x = sa.v4.vec.x - w;
	points[5].y = sa.v4.vec.y + height/2 + 2*h;

        points[6] = new vec2f();
	points[6].x = sa.v3.vec.x - w;
	points[6].y = sa.v3.vec.y;

        points[7] = new vec2f();
	points[7].x = sa.v2.vec.x;
	points[7].y = sa.v2.vec.y;

        points[8] = new vec2f();
	points[8].x = sa.v4.vec.x;
	points[8].y = sa.v4.vec.y + height/2 - h;

        points[9] = new vec2f();
	points[9].x = sa.v4.vec.x;
	points[9].y = sa.v4.vec.y + height/2 + h;

	if(dir=='l') {
		/* when direction is left, then we flip direction of arrow */
		float cx = sa.v1.vec.x + width;
		for(i=0;i<10;i++) {
			points[i].x -= cx;
			points[i].x = -points[i].x;
			points[i].x += sa.v1.vec.x;
		}
	}

	gl.glBegin(GL2.GL_POLYGON);
	for(i=0;i<5;i++)
		gl.glVertex2f(points[i].x, points[i].y);
	gl.glEnd();
	gl.glBegin(GL2.GL_POLYGON);
	for(i=4;i<8;i++)
		gl.glVertex2f(points[i].x, points[i].y);
	gl.glVertex2f(points[0].x, points[0].y);
	gl.glEnd();

	gl.glRectf(points[2].x, points[2].y, points[8].x, points[8].y);
	gl.glRectf(points[6].x, points[6].y, points[9].x, points[9].y);
}

/* draw vertical shape visualising future joining (up/down direction) */
public static void draw_vertical_join_shape(GL2 gl, ScrArea sa, char dir)
{
	vec2f[] points=new vec2f[10];
	short i;
	float w, h;
	float width = sa.v3.vec.x - sa.v1.vec.x;
	float height = sa.v3.vec.y - sa.v1.vec.y;

	if(height<width) {
		h = height/4;
		w = height/8;
	}
	else {
		h = width/4;
		w = width/8;
	}

        points[0] = new vec2f();
	points[0].x = sa.v1.vec.x + width/2;
	points[0].y = sa.v3.vec.y;

        points[1] = new vec2f();
	points[1].x = sa.v2.vec.x;
	points[1].y = sa.v2.vec.y;

        points[2] = new vec2f();
	points[2].x = sa.v1.vec.x;
	points[2].y = sa.v1.vec.y + h;

        points[3] = new vec2f();
	points[3].x = sa.v1.vec.x + width/2 - 2*w;
	points[3].y = sa.v1.vec.y + h;

        points[4] = new vec2f();
	points[4].x = sa.v1.vec.x + width/2;
	points[4].y = sa.v1.vec.y + 2*h;

        points[5] = new vec2f();
	points[5].x = sa.v1.vec.x + width/2 + 2*w;
	points[5].y = sa.v1.vec.y + h;

        points[6] = new vec2f();
	points[6].x = sa.v4.vec.x;
	points[6].y = sa.v4.vec.y + h;

        points[7] = new vec2f();
	points[7].x = sa.v3.vec.x;
	points[7].y = sa.v3.vec.y;

        points[8] = new vec2f();
	points[8].x = sa.v1.vec.x + width/2 - w;
	points[8].y = sa.v1.vec.y;

        points[9] = new vec2f();
	points[9].x = sa.v1.vec.x + width/2 + w;
	points[9].y = sa.v1.vec.y;

	if(dir=='u') {
		/* when direction is up, then we flip direction of arrow */
		float cy = sa.v1.vec.y + height;
		for(i=0;i<10;i++) {
			points[i].y -= cy;
			points[i].y = -points[i].y;
			points[i].y += sa.v1.vec.y;
		}
	}

	gl.glBegin(GL2.GL_POLYGON);
	for(i=0;i<5;i++)
		gl.glVertex2f(points[i].x, points[i].y);
	gl.glEnd();
	gl.glBegin(GL2.GL_POLYGON);
	for(i=4;i<8;i++)
		gl.glVertex2f(points[i].x, points[i].y);
	gl.glVertex2f(points[0].x, points[0].y);
	gl.glEnd();

	gl.glRectf(points[2].x, points[2].y, points[8].x, points[8].y);
	gl.glRectf(points[6].x, points[6].y, points[9].x, points[9].y);
}

/* draw join shape due to direction of joining */
public static void draw_join_shape(GL2 gl, ScrArea sa, char dir)
{
	if(dir=='u' || dir=='d')
		draw_vertical_join_shape(gl, sa, dir);
	else
		draw_horizontal_join_shape(gl, sa, dir);
}

/* draw screen area darker with arrow (visualisation of future joining) */
public static void scrarea_draw_shape_dark(GL2 gl, ScrArea sa, char dir)
{
	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	gl.glEnable(GL2.GL_BLEND);
	gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)50);
	draw_join_shape(gl, sa, dir);
	gl.glDisable(GL2.GL_BLEND);
}

/* draw screen area ligher with arrow shape ("eraser" of previous dark shape) */
public static void scrarea_draw_shape_light(GL2 gl, ScrArea sa, char dir)
{
	gl.glBlendFunc(GL2.GL_DST_COLOR, GL2.GL_SRC_ALPHA);
	gl.glEnable(GL2.GL_BLEND);
	/* value 181 was hardly computed: 181~105 */
	gl.glColor4ub((byte)255, (byte)255, (byte)255, (byte)50);
	/* draw_join_shape(sa, dir); */
	gl.glRecti(sa.v1.vec.x, sa.v1.vec.y, sa.v3.vec.x, sa.v3.vec.y);
	gl.glDisable(GL2.GL_BLEND);
}

public static void drawscredge_area_draw(GL2 gl, int sizex, int sizey, int x1, int y1, int x2, int y2, int a)
{
	/* right border area */
	if(x2<sizex-1)
		GlUtil.sdrawline(gl, x2+a, y1, x2+a, y2);

	/* left border area */
	if(x1>0)  /* otherwise it draws the emboss of window over */
		GlUtil.sdrawline(gl, x1+a, y1, x1+a, y2);

	/* top border area */
	if(y2<sizey-1)
		GlUtil.sdrawline(gl, x1, y2+a, x2, y2+a);

	/* bottom border area */
	if(y1>0)
		GlUtil.sdrawline(gl, x1, y1+a, x2, y1+a);

}

/** screen edges drawing **/
public static void drawscredge_area(GL2 gl, ScrArea sa, int sizex, int sizey, int center)
{
	short x1= sa.v1.vec.x;
	short y1= sa.v1.vec.y;
	short x2= sa.v3.vec.x;
	short y2= sa.v3.vec.y;
	int a, rt;

	rt= UtilDefines.CLAMPIS(G.rt, 0, 16);

	if(center==0) {
		GlUtil.cpack(gl, 0x505050);
		for(a=-rt; a<=rt; a++)
			if(a!=0)
				drawscredge_area_draw(gl, sizex, sizey, x1, y1, x2, y2, a);
	}
	else {
		GlUtil.cpack(gl, 0x0);
		drawscredge_area_draw(gl, sizex, sizey, x1, y1, x2, y2, 0);
	}
}

/* ****************** EXPORTED API TO OTHER MODULES *************************** */

public static bScreen ED_screen_duplicate(wmWindow win, bScreen sc)
{
	bScreen newsc;

	if(sc.full != ScreenTypes.SCREENNORMAL) return null; /* XXX handle this case! */

	/* make new empty screen: */
	newsc= ED_screen_add(win, sc.scene, sc.id.name,2);
	/* copy all data */
	screen_copy(newsc, sc);
	/* set in window */
	win.screen= newsc;

	/* store identifier */
	win.screen.winid= (short)win.winid;
	StringUtil.BLI_strncpy(win.screenname,0, win.screen.id.name,2, 21);

	return newsc;
}

/* screen sets cursor based on swinid */
static void region_cursor_set(wmWindow win, int swinid)
{
	ScrArea sa= (ScrArea)win.screen.areabase.first;

	for(;sa!=null; sa= sa.next) {
		ARegion ar= (ARegion)sa.regionbase.first;
		for(;ar!=null; ar= ar.next) {
			if(ar.swinid == swinid) {
//				if(ar.type && ar.type.cursor)
//					ar.type.cursor(win, sa, ar);
//				else
					WmCursors.WM_cursor_set(win, WmCursors.CURSOR_STD);
				return;
			}
		}
	}
}

public static void ED_screen_do_listen(wmWindow win, wmNotifier note)
{

	/* generic notes */
	switch(note.category) {
		case WmTypes.NC_WM:
			if(note.data==WmTypes.ND_FILEREAD)
				win.screen.do_draw= 1;
			break;
		case WmTypes.NC_WINDOW:
			win.screen.do_draw= 1;
			break;
		case WmTypes.NC_SCREEN:
			if(note.action==WmTypes.NA_EDITED)
				win.screen.do_draw= win.screen.do_refresh= 1;
			break;
		case WmTypes.NC_SCENE:
			if(note.data==WmTypes.ND_MODE)
				region_cursor_set(win, note.swinid);
			break;
	}
}

/* only for edge lines between areas, and the blended join arrows */
public static void ED_screen_draw(GL2 gl, wmWindow win)
{
//        System.out.println("ED_screen_draw");
	ScrArea sa;
	ScrArea sa1=null;
	ScrArea sa2=null;
	int dir = -1;
	int dira = -1;

	WmSubWindow.wmSubWindowSet(gl, win, win.screen.mainwin);

	for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next) {
		if ((sa.flag & ScreenTypes.AREA_FLAG_DRAWJOINFROM)!=0) sa1 = sa;
		if ((sa.flag & ScreenTypes.AREA_FLAG_DRAWJOINTO)!=0) sa2 = sa;
		drawscredge_area(gl, sa, win.sizex, win.sizey, 0);
	}
	for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next)
		drawscredge_area(gl, sa, win.sizex, win.sizey, 1);

	/* blended join arrow */
	if (sa1!=null && sa2!=null) {
		dir = area_getorientation(win.screen, sa1, sa2);
		if (dir >= 0) {
			switch(dir) {
				case 0: /* W */
					dir = 'r';
					dira = 'l';
					break;
				case 1: /* N */
					dir = 'd';
					dira = 'u';
					break;
				case 2: /* E */
					dir = 'l';
					dira = 'r';
					break;
				case 3: /* S */
					dir = 'u';
					dira = 'd';
					break;
			}
		}
		scrarea_draw_shape_dark(gl, sa2, (char)dir);
		scrarea_draw_shape_light(gl, sa1, (char)dira);
	}

//	if(G.f & G_DEBUG) printf("draw screen\n");
	win.screen.do_draw= 0;
}

/* make this screen usable */
/* for file read and first use, for scaling window, area moves */
public static void ED_screen_refresh(GL2 gl, wmWindowManager wm, wmWindow win)
//public static void ED_screen_refresh(wmWindowManager wm, wmWindow win)
{
	ScrArea sa;
    rcti winrct= new rcti();
    winrct.xmax = win.sizex-1;
    winrct.ymax = win.sizey-1;

	screen_test_scale(win.screen, win.sizex, win.sizey);

	if(win.screen.mainwin==0)
		win.screen.mainwin= (short)WmSubWindow.wm_subwindow_open(gl, win, winrct);
//		win.screen.mainwin= (short)WmSubWindow.wm_subwindow_open(win, winrct);
	else
		WmSubWindow.wm_subwindow_position(gl, win, win.screen.mainwin, winrct);
//		WmSubWindow.wm_subwindow_position(win, win.screen.mainwin, winrct);

	for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next) {
		/* set spacetype and region callbacks, calls init() */
		/* sets subwindows for regions, adds handlers */
		Area.ED_area_initialize(gl, wm, win, sa);
//		Area.ED_area_initialize(wm, win, sa);
	}

	/* wake up animtimer */
//	if(win.screen.animtimer)
//		WM_event_window_timer_sleep(win, win.screen.animtimer, 0);

//	if(G.f & G_DEBUG) printf("set screen\n");
	win.screen.do_refresh= 0;

	win.screen.context= ScreenContext.ed_screen_context;
}

/* file read, set all screens, ... */
public static void ED_screens_initialize(GL2 gl, wmWindowManager wm)
//public static void ED_screens_initialize(wmWindowManager wm)
{
	wmWindow win;

	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {

		if(win.screen==null)
			win.screen= G.main.screen.first;

		ED_screen_refresh(gl, wm, win);
//		ED_screen_refresh(wm, win);
	}
}


/* *********** exit calls are for closing running stuff ******** */

public static void ED_region_exit(bContext C, ARegion ar)
{
	ARegion prevar= bContext.CTX_wm_region(C);

	bContext.CTX_wm_region_set(C, ar);
	WmEventSystem.WM_event_remove_handlers(C, ar.handlers);
	if(ar.swinid!=0)
		WmSubWindow.wm_subwindow_close(bContext.CTX_wm_window(C), ar.swinid);
	ar.swinid= 0;

//	if(ar.headerstr!=null)
//		MEM_freeN(ar.headerstr);
	ar.headerstr= null;

	bContext.CTX_wm_region_set(C, prevar);
}

public static void ED_area_exit(bContext C, ScrArea sa)
{
	ScrArea prevsa= bContext.CTX_wm_area(C);
	ARegion ar;

	bContext.CTX_wm_area_set(C, sa);
	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
		ED_region_exit(C, ar);

	WmEventSystem.WM_event_remove_handlers(C, sa.handlers);
	bContext.CTX_wm_area_set(C, prevsa);
}

public static void ED_screen_exit(bContext C, wmWindow window, bScreen screen)
{
	wmWindow prevwin= bContext.CTX_wm_window(C);
	ScrArea sa;
	ARegion ar;

	bContext.CTX_wm_window_set(C, window);

//	if(screen.animtimer!=null)
//		WM_event_remove_window_timer(window, screen.animtimer);
	screen.animtimer= null;

	if(screen.mainwin!=0)
		WmSubWindow.wm_subwindow_close(window, screen.mainwin);
	screen.mainwin= 0;
	screen.subwinactive= 0;

	for(ar= (ARegion)screen.regionbase.first; ar!=null; ar= ar.next)
		ED_region_exit(C, ar);

	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
		ED_area_exit(C, sa);

	/* mark it available for use for other windows */
	screen.winid= 0;

	bContext.CTX_wm_window_set(C, prevwin);
}

/* *********************************** */

/* case when on area-edge or in azones, or outside window */
static void screen_cursor_set(wmWindow win, wmEvent event)
{
	AZone az= null;
	ScrArea sa;

	for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next)
		if((az=ScreenOps.is_in_area_actionzone(sa, event.x, event.y))!=null)
			break;

	if(sa!=null) {
		if(az.type==Area.AZONE_AREA)
			WmCursors.WM_cursor_set(win, WmCursors.CURSOR_EDIT);
		else if(az.type==Area.AZONE_REGION) {
			if(az.x1==az.x2)
				WmCursors.WM_cursor_set(win, WmCursors.CURSOR_X_MOVE);
			else
				WmCursors.WM_cursor_set(win, WmCursors.CURSOR_Y_MOVE);
		}
	}
	else {
		ScrEdge actedge= screen_find_active_scredge(win.screen, event.x, event.y);

		if (actedge!=null) {
			if(scredge_is_horizontal(actedge))
				WmCursors.WM_cursor_set(win, WmCursors.CURSOR_Y_MOVE);
			else
				WmCursors.WM_cursor_set(win, WmCursors.CURSOR_X_MOVE);
		}
		else
			WmCursors.WM_cursor_set(win, WmCursors.CURSOR_STD);
	}
}


/* called in wm_event_system.c. sets state vars in screen, cursors */
/* event type is mouse move */
public static void ED_screen_set_subwinactive(wmWindow win, wmEvent event)
{
	if(win.screen!=null) {
		bScreen scr= win.screen;
		ScrArea sa;
		ARegion ar;
		int oldswin= scr.subwinactive;

		for(sa= (ScrArea)scr.areabase.first; sa!=null; sa= sa.next) {
			if(event.x > sa.totrct.xmin && event.x < sa.totrct.xmax)
				if(event.y > sa.totrct.ymin && event.y < sa.totrct.ymax)
					if(null==ScreenOps.is_in_area_actionzone(sa, event.x, event.y))
						break;
		}
		if(sa!=null) {
			for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
				if(Rct.BLI_in_rcti(ar.winrct, event.x, event.y))
					scr.subwinactive= ar.swinid;
			}
		}
		else
			scr.subwinactive= scr.mainwin;

		/* check for redraw headers */
		if(oldswin!=scr.subwinactive) {

			for(sa= (ScrArea)scr.areabase.first; sa!=null; sa= sa.next) {
				boolean do_draw= false;

				for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
					if(ar.swinid==oldswin || ar.swinid==scr.subwinactive)
						do_draw= true;

				if(do_draw) {
					for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
						if(ar.regiontype==ScreenTypes.RGN_TYPE_HEADER)
							Area.ED_region_tag_redraw(ar);
				}
			}
		}

		/* cursors, for time being set always on edges, otherwise aregion doesnt switch */
		if(scr.subwinactive==scr.mainwin) {
			screen_cursor_set(win, event);
		}
		else if(oldswin!=scr.subwinactive) {
			region_cursor_set(win, scr.subwinactive);
		}
	}
}

public static boolean ED_screen_area_active(bContext C)
{
	bScreen sc= bContext.CTX_wm_screen(C);
	ScrArea sa= bContext.CTX_wm_area(C);

	if(sc!=null && sa!=null) {
		ARegion ar;
		for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
			if(ar.swinid == sc.subwinactive)
				return true;
	}
	return false;
}

/* operator call, WM + Window + screen already existed before */
/* Do NOT call in area/region queues! */
public static void ED_screen_set(GL2 gl, bContext C, bScreen sc)
//public static void ED_screen_set(bContext C, bScreen sc)
{
	wmWindow win= bContext.CTX_wm_window(C);
	bScreen oldscreen= bContext.CTX_wm_screen(C);
	ID id;

	/* validate screen, it's called with notifier reference */
	for(id= (ID)bContext.CTX_data_main(C).screen.first; id!=null; id= (ID)id.next)
		if(sc == (bScreen)id)
			break;
	if(id==null)
		return;

	/* check for valid winid */
	if(sc.winid!=0 && sc.winid!=win.winid)
		return;

	if(sc.full!=0) {				/* find associated full */
		bScreen sc1;
		for(sc1= (bScreen)bContext.CTX_data_main(C).screen.first; sc1!=null; sc1= (bScreen)sc1.id.next) {
			ScrArea sa= (ScrArea)sc1.areabase.first;
			if(sa.full==sc) {
				sc= sc1;
				break;
			}
		}
	}

	if (oldscreen != sc) {
//		wmTimer wt= oldscreen.animtimer;

		/* we put timer to sleep, so screen_exit has to think there's no timer */
		oldscreen.animtimer= null;
//		if(wt)
//			WM_event_window_timer_sleep(win, wt, 1);

		ED_screen_exit(C, win, oldscreen);
//		oldscreen.animtimer= wt;

		win.screen= sc;
		bContext.CTX_wm_window_set(C, win);	// stores C.wm.screen... hrmf

		/* prevent multiwin errors */
		sc.winid= (short)win.winid;

		ED_screen_refresh(gl, bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C));
//		ED_screen_refresh(bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C));
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

		/* makes button hilites work */
		WmEventSystem.WM_event_add_mousemove(C);
	}
}

/* only call outside of area/region loops */
public static void ED_screen_set_scene(bContext C, Scene scene)
{
	bScreen sc;
	bScreen curscreen= bContext.CTX_wm_screen(C);

	for(sc= (bScreen)bContext.CTX_data_main(C).screen.first; sc!=null; sc= (bScreen)sc.id.next) {
		if((U.flag & UserDefTypes.USER_SCENEGLOBAL)!=0 || sc==curscreen) {

			if(scene != sc.scene) {
				/* all areas endlocalview */
			// XXX	ScrArea *sa= sc.areabase.first;
			//	while(sa) {
			//		endlocalview(sa);
			//		sa= sa.next;
			//	}
				sc.scene= scene;
			}

		}
	}

	//  copy_view3d_lock(0);	/* space.c */

	/* are there cameras in the views that are not in the scene? */
	for(sc= (bScreen)bContext.CTX_data_main(C).screen.first; sc!=null; sc= (bScreen)sc.id.next) {
		if( (U.flag & UserDefTypes.USER_SCENEGLOBAL)!=0 || sc==curscreen) {
			ScrArea sa= (ScrArea)sc.areabase.first;
			while(sa!=null) {
				SpaceLink sl= (SpaceLink)sa.spacedata.first;
				while(sl!=null) {
					if(sl.spacetype==SpaceTypes.SPACE_VIEW3D) {
						View3D v3d= (View3D) sl;
						if (v3d.camera==null || SceneUtil.object_in_scene(v3d.camera, scene)==null) {
							v3d.camera= SceneUtil.scene_find_camera(sc.scene);
							// XXX if (sc==curscreen) handle_view3d_lock();
							if (v3d.camera==null && v3d.persp==View3dTypes.V3D_CAMOB)
								v3d.persp= View3dTypes.V3D_PERSP;
						}
					}
					sl= (SpaceLink)sl.next;
				}
				sa= sa.next;
			}
		}
	}

	bContext.CTX_data_scene_set(C, scene);
	SceneUtil.set_scene_bg(scene);

	ED_update_for_newframe(C, 1);

	/* complete redraw */
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

}

///* this function toggles: if area is full then the parent will be restored */
//void ed_screen_fullarea(bContext *C, ScrArea *sa)
//{
//	bScreen *sc, *oldscreen;
//
//	if(sa==NULL) {
//		return;
//	}
//	else if(sa.full) {
//		short fulltype;
//
//		sc= sa.full;		/* the old screen to restore */
//		oldscreen= CTX_wm_screen(C);	/* the one disappearing */
//
//		fulltype = sc.full;
//
//		/* refuse to go out of SCREENAUTOPLAY as long as G_FLAGS_AUTOPLAY
//		   is set */
//
//		if (fulltype != SCREENAUTOPLAY || (G.flags & G_FILE_AUTOPLAY) == 0) {
//			ScrArea *old;
//
//			sc.full= 0;
//
//			/* find old area */
//			for(old= sc.areabase.first; old; old= old.next)
//				if(old.full) break;
//			if(old==NULL) {
//				printf("something wrong in areafullscreen\n");
//				return;
//			}
//			    // old feature described below (ton)
//				// in autoplay screens the headers are disabled by
//				// default. So use the old headertype instead
//
//			area_copy_data(old, sa, 1);	/*  1 = swap spacelist */
//
//			old.full= NULL;
//
//			/* animtimer back */
//			sc.animtimer= oldscreen.animtimer;
//			oldscreen.animtimer= NULL;
//
//			ED_screen_set(C, sc);
//
//			free_screen(oldscreen);
//			free_libblock(&CTX_data_main(C).screen, oldscreen);
//		}
//	}
//	else {
//		ScrArea *newa;
//
//		oldscreen= CTX_wm_screen(C);
//
//		/* is there only 1 area? */
//		if(oldscreen.areabase.first==oldscreen.areabase.last) return;
//
//		oldscreen.full = SCREENFULL;
//
//		sc= ED_screen_add(CTX_wm_window(C), CTX_data_scene(C), "temp");
//		sc.full = SCREENFULL; // XXX
//
//		/* timer */
//		sc.animtimer= oldscreen.animtimer;
//		oldscreen.animtimer= NULL;
//
//		/* returns the top small area */
//		newa= area_split(CTX_wm_window(C), sc, (ScrArea *)sc.areabase.first, 'h', 0.99f);
//		ED_area_newspace(C, newa, SPACE_INFO);
//
//		/* copy area */
//		newa= newa.prev;
//		area_copy_data(newa, sa, 1);	/* 1 = swap spacelist */
//
//		sa.full= oldscreen;
//		newa.full= oldscreen;
//		newa.next.full= oldscreen; // XXX
//
//		ED_screen_set(C, sc);
//	}
//
//	/* XXX bad code: setscreen() ends with first area active. fullscreen render assumes this too */
//	CTX_wm_area_set(C, sc.areabase.first);
//
//	/* XXX retopo_force_update(); */
//
//}
//
//int ED_screen_full_newspace(bContext *C, ScrArea *sa, int type)
//{
//	if(sa==NULL)
//		return 0;
//
//	if(sa.full==0)
//		ed_screen_fullarea(C, sa);
//
//	/* CTX_wm_area(C) is new area */
//	ED_area_newspace(C, CTX_wm_area(C), type);
//
//	return 1;
//}
//
//void ED_screen_full_prevspace(bContext *C)
//{
//	ScrArea *sa= CTX_wm_area(C);
//
//	ED_area_prevspace(C);
//
//	if(sa.full)
//		ed_screen_fullarea(C, sa);
//}
//
///* redraws: uses defines from stime.redraws
// * enable: 1 - forward on, -1 - backwards on, 0 - off
// */
//void ED_screen_animation_timer(bContext *C, int redraws, int enable)
//{
//	bScreen *screen= CTX_wm_screen(C);
//	wmWindow *win= CTX_wm_window(C);
//	Scene *scene= CTX_data_scene(C);
//
//	if(screen.animtimer)
//		WM_event_remove_window_timer(win, screen.animtimer);
//	screen.animtimer= NULL;
//
//	if(enable) {
//		struct ScreenAnimData *sad= MEM_mallocN(sizeof(ScreenAnimData), "ScreenAnimData");
//
//		screen.animtimer= WM_event_add_window_timer(win, TIMER0, (1.0/FPS));
//		sad.ar= CTX_wm_region(C);
//		sad.redraws= redraws;
//		sad.reverse= (enable < 0);
//		screen.animtimer.customdata= sad;
//
//	}
//	/* notifier catched by top header, for button */
//	WM_event_add_notifier(C, NC_SCREEN|ND_ANIMPLAY, screen);
//}
//
//unsigned int ED_screen_view3d_layers(bScreen *screen)
//{
//	if(screen) {
//		unsigned int layer= screen.scene.lay;	/* as minimum this */
//		ScrArea *sa;
//
//		/* get all used view3d layers */
//		for(sa= screen.areabase.first; sa; sa= sa.next) {
//			if(sa.spacetype==SPACE_VIEW3D)
//				layer |= ((View3D *)sa.spacedata.first).lay;
//		}
//		return layer;
//	}
//	return 0;
//}


/* results in fully updated anim system */
/* in future sound should be on WM level, only 1 sound can play! */
public static void ED_update_for_newframe(bContext C, int mute)
{
//	bScreen *screen= CTX_wm_screen(C);
//	Scene *scene= screen.scene;
//
//	//extern void audiostream_scrub(unsigned int frame);	/* seqaudio.c */
//
//	/* this function applies the changes too */
//	/* XXX future: do all windows */
//	scene_update_for_newframe(scene, ED_screen_view3d_layers(screen)); /* BKE_scene.h */
//
//	//if ( (CFRA>1) && (!mute) && (scene.audio.flag & AUDIO_SCRUB))
//	//	audiostream_scrub( CFRA );
//
//	/* 3d window, preview */
//	//BIF_view3d_previewrender_signal(curarea, PR_DBASE|PR_DISPRECT);
//
//	/* all movie/sequence images */
//	//BIF_image_update_frame();
//
//	/* composite */
//	if(scene.use_nodes && scene.nodetree)
//		ntreeCompositTagAnimated(scene.nodetree);
//
//	/* update animated texture nodes */
//	{
//		Tex *tex;
//		for(tex= CTX_data_main(C).tex.first; tex; tex= tex.id.next)
//			if( tex.use_nodes && tex.nodetree ) {
//				ntreeTexTagAnimated( tex.nodetree );
//			}
//	}
}

}

