/**
 * $Id: UIIcons.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributors: Blender Foundation, full recode
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import blender.blenkernel.Icons;
import blender.blenkernel.Icons.Icon;
import blender.imbuf.ImBuf;
import blender.imbuf.ReadImage;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.PreviewImage;
import blender.makesdna.sdna.bTheme;
import java.net.URL;
import static blender.blenkernel.Blender.U;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
//import resources.ResourceAnchor;

public class UIIcons {

public static final int ICON_DEFAULT_HEIGHT= 16;
public static final int ICON_DEFAULT_WIDTH=	16;
public static final int PREVIEW_DEFAULT_HEIGHT= 96;


public static final int ICON_IMAGE_W=		600;
public static final int ICON_IMAGE_H=		512; //640;

public static final int ICON_GRID_COLS=		26;
public static final int ICON_GRID_ROWS=		24; //30;

public static final int ICON_GRID_MARGIN=	5;
public static final int ICON_GRID_W=		16;
public static final int ICON_GRID_H=		16;

public static class IconImage {
	public int w;
	public int h;
	public ByteBuffer rect;
};

//typedef void (*VectorDrawFunc)(int x, int y, int w, int h, float alpha);
public static interface VectorDrawFunc {
    public void run(GL2 gl, int x, int y, int w, int h, float alpha);
}

public static final int ICON_TYPE_PREVIEW=	0;
public static final int ICON_TYPE_TEXTURE=	1;
public static final int ICON_TYPE_BUFFER=	2;
public static final int ICON_TYPE_VECTOR=	3;

public static class DrawInfo {
	public int w;
	public int h;
	public float aspect;
	public VectorDrawFunc drawFunc; /* If drawFunc is defined then it is a vector icon, otherwise use rect */
	public IconImage icon;
};

///* ******************* STATIC LOCAL VARS ******************* */
///* static here to cache results of icon directory scan, so it's not
// * scanning the filesystem each time the menu is drawn */
//static struct ListBase iconfilelist = {0, 0};


/* **************************************************** */

static void def_internal_icon(ImBuf bbuf, int icon_id, int xofs, int yofs, int size)
{
	Icon new_icon = null;
	IconImage iimg = null;
	DrawInfo di;
	int y = 0;
	int imgsize = 0;

	new_icon = new Icon();

	new_icon.obj = 0; /* icon is not for library object */
	new_icon.type = 0;

	di = new DrawInfo();
	di.drawFunc = null;
	di.w = size;
	di.h = size;
	di.aspect = 1.0f;

	iimg = new IconImage();
        iimg.rect = ByteBuffer.allocate(size*size*4);
	iimg.w = size;
	iimg.h = size;

	/* Here we store the rect in the icon - same as before */
	imgsize = bbuf.x;
//        System.out.println("from: " + bbuf.rect.array().length);
//        System.out.println("to: " + iimg.rect.array().length);
//        System.out.println("from off: " + ((yofs)*4*600+(xofs*4)) + " yofs: " + yofs + " imgsize: " + imgsize + " xofs: " + xofs);
//        System.out.println("to off: " + (size*4) + " size: " + size);
	for (y=0; y<size; y++) {
//		memcpy(&iimg.rect[y*size], &bbuf.rect[(y+yofs)*imgsize+xofs], size*sizeof(int));
            System.arraycopy(bbuf.rect.array(), (y+yofs)*4*imgsize+(xofs*4), iimg.rect.array(), y*size*4, size*4);
	}

	di.icon = iimg;

//	new_icon.drawinfo_free = UI_icons_free_drawinfo;
	new_icon.drawinfo = di;

	Icons.BKE_icon_set(icon_id, new_icon);
}

public static void def_internal_vicon( int icon_id, VectorDrawFunc drawFunc)
{
	Icon new_icon = null;
	DrawInfo di;

        new_icon = new Icon();

	new_icon.obj = null; /* icon is not for library object */
	new_icon.type = 0;

        di = new DrawInfo();
	di.drawFunc =drawFunc;
	di.w = ICON_DEFAULT_HEIGHT;
	di.h = ICON_DEFAULT_HEIGHT;
	di.aspect = 1.0f;
	di.icon = null;

//	new_icon.drawinfo_free = null;
	new_icon.drawinfo = di;

	Icons.BKE_icon_set(icon_id, new_icon);
}

///* Vector Icon Drawing Routines */
//
//	/* Utilities */
//
//static void viconutil_set_point(GLint pt[2], int x, int y)
//{
//	pt[0] = x;
//	pt[1] = y;
//}
//
//static void viconutil_draw_tri(GLint (*pts)[2])
//{
//	glBegin(GL_TRIANGLES);
//	glVertex2iv(pts[0]);
//	glVertex2iv(pts[1]);
//	glVertex2iv(pts[2]);
//	glEnd();
//}
//
//static void viconutil_draw_lineloop(GLint (*pts)[2], int numPoints)
//{
//	int i;
//
//	glBegin(GL_LINE_LOOP);
//	for (i=0; i<numPoints; i++) {
//		glVertex2iv(pts[i]);
//	}
//	glEnd();
//}
//
//static void viconutil_draw_lineloop_smooth(GLint (*pts)[2], int numPoints)
//{
//	glEnable(GL_LINE_SMOOTH);
//	viconutil_draw_lineloop(pts, numPoints);
//	glDisable(GL_LINE_SMOOTH);
//}
//
//static void viconutil_draw_points(GLint (*pts)[2], int numPoints, int pointSize)
//{
//	int i;
//
//	glBegin(GL_QUADS);
//	for (i=0; i<numPoints; i++) {
//		int x = pts[i][0], y = pts[i][1];
//
//		glVertex2i(x-pointSize,y-pointSize);
//		glVertex2i(x+pointSize,y-pointSize);
//		glVertex2i(x+pointSize,y+pointSize);
//		glVertex2i(x-pointSize,y+pointSize);
//	}
//	glEnd();
//}
//
//	/* Drawing functions */
//
//static void vicon_x_draw(int x, int y, int w, int h, float alpha)
//{
//	x += 3;
//	y += 3;
//	w -= 6;
//	h -= 6;
//
//	glEnable( GL_LINE_SMOOTH );
//
//	glLineWidth(2.5);
//
//	glColor4f(0.0, 0.0, 0.0, alpha);
//	glBegin(GL_LINES);
//	glVertex2i(x  ,y  );
//	glVertex2i(x+w,y+h);
//	glVertex2i(x+w,y  );
//	glVertex2i(x  ,y+h);
//	glEnd();
//
//	glLineWidth(1.0);
//
//	glDisable( GL_LINE_SMOOTH );
//}
//
//static void vicon_view3d_draw(int x, int y, int w, int h, float alpha)
//{
//	int cx = x + w/2;
//	int cy = y + h/2;
//	int d = MAX2(2, h/3);
//
//	glColor4f(0.5, 0.5, 0.5, alpha);
//	glBegin(GL_LINES);
//	glVertex2i(x  , cy-d);
//	glVertex2i(x+w, cy-d);
//	glVertex2i(x  , cy+d);
//	glVertex2i(x+w, cy+d);
//
//	glVertex2i(cx-d, y  );
//	glVertex2i(cx-d, y+h);
//	glVertex2i(cx+d, y  );
//	glVertex2i(cx+d, y+h);
//	glEnd();
//
//	glColor4f(0.0, 0.0, 0.0, alpha);
//	glBegin(GL_LINES);
//	glVertex2i(x  , cy);
//	glVertex2i(x+w, cy);
//	glVertex2i(cx, y  );
//	glVertex2i(cx, y+h);
//	glEnd();
//}
//
//static void vicon_edit_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[4][2];
//
//	viconutil_set_point(pts[0], x+3  , y+3  );
//	viconutil_set_point(pts[1], x+w-3, y+3  );
//	viconutil_set_point(pts[2], x+w-3, y+h-3);
//	viconutil_set_point(pts[3], x+3  , y+h-3);
//
//	glColor4f(0.0, 0.0, 0.0, alpha);
//	viconutil_draw_lineloop(pts, 4);
//
//	glColor3f(1, 1, 0.0);
//	viconutil_draw_points(pts, 4, 1);
//}
//
//static void vicon_editmode_hlt_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[3][2];
//
//	viconutil_set_point(pts[0], x+w/2, y+h-2);
//	viconutil_set_point(pts[1], x+3, y+4);
//	viconutil_set_point(pts[2], x+w-3, y+4);
//
//	glColor4f(0.5, 0.5, 0.5, alpha);
//	viconutil_draw_tri(pts);
//
//	glColor4f(0.0, 0.0, 0.0, 1);
//	viconutil_draw_lineloop_smooth(pts, 3);
//
//	glColor3f(1, 1, 0.0);
//	viconutil_draw_points(pts, 3, 1);
//}
//
//static void vicon_editmode_dehlt_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[3][2];
//
//	viconutil_set_point(pts[0], x+w/2, y+h-2);
//	viconutil_set_point(pts[1], x+3, y+4);
//	viconutil_set_point(pts[2], x+w-3, y+4);
//
//	glColor4f(0.0f, 0.0f, 0.0f, 1);
//	viconutil_draw_lineloop_smooth(pts, 3);
//
//	glColor3f(.9f, .9f, .9f);
//	viconutil_draw_points(pts, 3, 1);
//}
//
//static void vicon_disclosure_tri_right_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[3][2];
//	int cx = x+w/2;
//	int cy = y+w/2;
//	int d = w/3, d2 = w/5;
//
//	viconutil_set_point(pts[0], cx-d2, cy+d);
//	viconutil_set_point(pts[1], cx-d2, cy-d);
//	viconutil_set_point(pts[2], cx+d2, cy);
//
//	glShadeModel(GL_SMOOTH);
//	glBegin(GL_TRIANGLES);
//	glColor4f(0.8f, 0.8f, 0.8f, alpha);
//	glVertex2iv(pts[0]);
//	glVertex2iv(pts[1]);
//	glColor4f(0.3f, 0.3f, 0.3f, alpha);
//	glVertex2iv(pts[2]);
//	glEnd();
//	glShadeModel(GL_FLAT);
//
//	glColor4f(0.0f, 0.0f, 0.0f, 1);
//	viconutil_draw_lineloop_smooth(pts, 3);
//}
//
//static void vicon_small_tri_right_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[3][2];
//	int cx = x+w/2-4;
//	int cy = y+w/2;
//	int d = w/5, d2 = w/7;
//
//	viconutil_set_point(pts[0], cx-d2, cy+d);
//	viconutil_set_point(pts[1], cx-d2, cy-d);
//	viconutil_set_point(pts[2], cx+d2, cy);
//
//	glColor4f(0.2f, 0.2f, 0.2f, alpha);
//
//	glShadeModel(GL_SMOOTH);
//	glBegin(GL_TRIANGLES);
//	glVertex2iv(pts[0]);
//	glVertex2iv(pts[1]);
//	glVertex2iv(pts[2]);
//	glEnd();
//	glShadeModel(GL_FLAT);
//}
//
//static void vicon_disclosure_tri_down_draw(int x, int y, int w, int h, float alpha)
//{
//	GLint pts[3][2];
//	int cx = x+w/2;
//	int cy = y+w/2;
//	int d = w/3, d2 = w/5;
//
//	viconutil_set_point(pts[0], cx+d, cy+d2);
//	viconutil_set_point(pts[1], cx-d, cy+d2);
//	viconutil_set_point(pts[2], cx, cy-d2);
//
//	glShadeModel(GL_SMOOTH);
//	glBegin(GL_TRIANGLES);
//	glColor4f(0.8f, 0.8f, 0.8f, alpha);
//	glVertex2iv(pts[0]);
//	glVertex2iv(pts[1]);
//	glColor4f(0.3f, 0.3f, 0.3f, alpha);
//	glVertex2iv(pts[2]);
//	glEnd();
//	glShadeModel(GL_FLAT);
//
//	glColor4f(0.0f, 0.0f, 0.0f, 1);
//	viconutil_draw_lineloop_smooth(pts, 3);
//}
//
//static void vicon_move_up_draw(int x, int y, int w, int h, float alpha)
//{
//	int d=-2;
//
//	glEnable(GL_LINE_SMOOTH);
//	glLineWidth(1);
//	glColor3f(0.0, 0.0, 0.0);
//
//	glBegin(GL_LINE_STRIP);
//	glVertex2i(x+w/2-d*2, y+h/2+d);
//	glVertex2i(x+w/2, y+h/2-d + 1);
//	glVertex2i(x+w/2+d*2, y+h/2+d);
//	glEnd();
//
//	glLineWidth(1.0);
//	glDisable(GL_LINE_SMOOTH);
//}
//
//static void vicon_move_down_draw(int x, int y, int w, int h, float alpha)
//{
//	int d=2;
//
//	glEnable(GL_LINE_SMOOTH);
//	glLineWidth(1);
//	glColor3f(0.0, 0.0, 0.0);
//
//	glBegin(GL_LINE_STRIP);
//	glVertex2i(x+w/2-d*2, y+h/2+d);
//	glVertex2i(x+w/2, y+h/2-d - 1);
//	glVertex2i(x+w/2+d*2, y+h/2+d);
//	glEnd();
//
//	glLineWidth(1.0);
//	glDisable(GL_LINE_SMOOTH);
//}

static void init_internal_icons(URL iconfilestr)
{
	bTheme btheme= (bTheme)U.themes.first;
	ImBuf bbuf= null;
	int x, y;
//	char iconfilestr[FILE_MAXDIR+FILE_MAXFILE];
//	char filenamestr[FILE_MAXFILE+16];	// 16 == strlen(".blender/icons/")+1
//
//	if ((btheme!=NULL) && (strlen(btheme.tui.iconfile) > 0)) {
//
//#ifdef WIN32
//		sprintf(filenamestr, "icons/%s", btheme.tui.iconfile);
//#else
//		sprintf(filenamestr, ".blender/icons/%s", btheme.tui.iconfile);
//#endif
//                ClassLoader cl = ResourceAnchor.class.getClassLoader();
                //URL iconfilestr = ResourceAnchor.class.getClassLoader().getResource("resources/icons/blenderbuttons.png");
                if (iconfilestr != null) {
//    				System.out.println("resource found: "+"resources/icons/blenderbuttons.png");
    			}
    			else {
    				System.out.println("resource not found: " + "resources/icons/blenderbuttons.png");
                    return;
    			}
//                if (iconfilestr == null) {
//                    System.out.println("can't find resource: " + "resources/icons/blenderbuttons.png");
//                    return;
//                }
//		BLI_make_file_string("/", iconfilestr, BLI_gethome(), filenamestr);

                boolean ok;
                try {
                    if (iconfilestr.openConnection().getContentLength() > 0)
                        ok = true;
                    else
                        ok = false;
                } catch(Exception ex) {
                    ok = false;
                }
//		if (BLI_exists(iconfilestr)) {
                if (ok) {
			bbuf = ReadImage.IMB_loadiffname(iconfilestr, ImBuf.IB_rect);
//			if(bbuf.x < ICON_IMAGE_W || bbuf.y < ICON_IMAGE_H) {
//				printf("\n***WARNING***\nIcons file %s too small.\nUsing built-in Icons instead\n", iconfilestr);
//				IMB_freeImBuf(bbuf);
//				bbuf= NULL;
		}
//	}
//	}
//	if(bbuf==NULL)
//		bbuf = IMB_ibImageFromMemory((int *)datatoc_blenderbuttons, datatoc_blenderbuttons_size, IB_rect);

	for (y=0; y<ICON_GRID_ROWS; y++) {
		for (x=0; x<ICON_GRID_COLS; x++) {
			def_internal_icon(bbuf, Resources.BIFICONID_FIRST.ordinal() + y*ICON_GRID_COLS + x,
				x*(ICON_GRID_W+ICON_GRID_MARGIN)+ICON_GRID_MARGIN,
				y*(ICON_GRID_H+ICON_GRID_MARGIN)+ICON_GRID_MARGIN, ICON_GRID_W);
		}
	}

//	def_internal_vicon(BIFIconID.VICON_VIEW3D.ordinal(), vicon_view3d_draw);
//	def_internal_vicon(BIFIconID.VICON_EDIT.ordinal(), vicon_edit_draw);
//	def_internal_vicon(BIFIconID.VICON_EDITMODE_DEHLT.ordinal(), vicon_editmode_dehlt_draw);
//	def_internal_vicon(BIFIconID.VICON_EDITMODE_HLT.ordinal(), vicon_editmode_hlt_draw);
//	def_internal_vicon(BIFIconID.VICON_DISCLOSURE_TRI_RIGHT.ordinal(), vicon_disclosure_tri_right_draw);
//	def_internal_vicon(BIFIconID.VICON_DISCLOSURE_TRI_DOWN.ordinal(), vicon_disclosure_tri_down_draw);
//	def_internal_vicon(BIFIconID.VICON_MOVE_UP.ordinal(), vicon_move_up_draw);
//	def_internal_vicon(BIFIconID.VICON_MOVE_DOWN.ordinal(), vicon_move_down_draw);
//	def_internal_vicon(BIFIconID.VICON_X.ordinal(), vicon_x_draw);
//	def_internal_vicon(BIFIconID.VICON_SMALL_TRI_RIGHT.ordinal(), vicon_small_tri_right_draw);

//	IMB_freeImBuf(bbuf);
}


//static void init_iconfile_list(struct ListBase *list)
//{
//	IconFile *ifile;
//	ImBuf *bbuf= NULL;
//	struct direntry *dir;
//	int restoredir = 1; /* restore to current directory */
//	int totfile, i, index=1;
//	int ifilex, ifiley;
//	char icondirstr[FILE_MAX];
//	char iconfilestr[FILE_MAX+16]; /* allow 256 chars for file+dir */
//	char olddir[FILE_MAX];
//
//	list.first = list.last = NULL;
//
//#ifdef WIN32
//	BLI_make_file_string("/", icondirstr, BLI_gethome(), "icons");
//#else
//	BLI_make_file_string("/", icondirstr, BLI_gethome(), ".blender/icons");
//#endif
//
//	if(BLI_exists(icondirstr)==0)
//		return;
//
//	/* since BLI_getdir changes the current working directory, restore it
//	   back to old value afterwards */
//	if(!BLI_getwdN(olddir))
//		restoredir = 0;
//	totfile = BLI_getdir(icondirstr, &dir);
//	if (restoredir)
//		chdir(olddir);
//
//	for(i=0; i<totfile; i++) {
//		if( (dir[i].type & S_IFREG) ) {
//			char *filename = dir[i].relname;
//
//			if(BLI_testextensie(filename, ".png")) {
//
//				/* check to see if the image is the right size, continue if not */
//				/* copying strings here should go ok, assuming that we never get back
//				   a complete path to file longer than 256 chars */
//				sprintf(iconfilestr, "%s/%s", icondirstr, filename);
//				if(BLI_exists(iconfilestr)) bbuf = IMB_loadiffname(iconfilestr, IB_rect);
//
//				ifilex = bbuf.x;
//				ifiley = bbuf.y;
//				IMB_freeImBuf(bbuf);
//
//				if ((ifilex != ICON_IMAGE_W) || (ifiley != ICON_IMAGE_H))
//					continue;
//
//				/* found a potential icon file, so make an entry for it in the cache list */
//				ifile = MEM_callocN(sizeof(IconFile), "IconFile");
//
//				BLI_strncpy(ifile.filename, filename, sizeof(ifile.filename));
//				ifile.index = index;
//
//				BLI_addtail(list, ifile);
//
//				index++;
//			}
//		}
//	}
//
//	/* free temporary direntry structure that's been created by BLI_getdir() */
//	i= totfile-1;
//
//	for(; i>=0; i--){
//		MEM_freeN(dir[i].relname);
//		if (dir[i].string) MEM_freeN(dir[i].string);
//	}
//	free(dir);
//	dir= 0;
//}
//
//static void free_iconfile_list(struct ListBase *list)
//{
//	IconFile *ifile=NULL, *next_ifile=NULL;
//
//	for(ifile=list.first; ifile; ifile=next_ifile) {
//		next_ifile = ifile.next;
//		BLI_freelinkN(list, ifile);
//	}
//}
//
//int UI_iconfile_get_index(char *filename)
//{
//	IconFile *ifile;
//	ListBase *list=&(iconfilelist);
//
//	for(ifile=list.first; ifile; ifile=ifile.next) {
//		if ( BLI_streq(filename, ifile.filename)) {
//			return ifile.index;
//		}
//	}
//
//	return 0;
//}
//
//ListBase *UI_iconfile_list(void)
//{
//	ListBase *list=&(iconfilelist);
//
//	return list;
//}
//
//
//void UI_icons_free()
//{
//	free_iconfile_list(&iconfilelist);
//	BKE_icons_free();
//}
//
//void UI_icons_free_drawinfo(void *drawinfo)
//{
//	DrawInfo *di = drawinfo;
//
//	if (di)
//	{
//		if (di.icon) {
//			MEM_freeN(di.icon.rect);
//			MEM_freeN(di.icon);
//		}
//		MEM_freeN(di);
//	}
//}

static DrawInfo icon_create_drawinfo()
{
	DrawInfo di = null;

        di = new DrawInfo();
	di.drawFunc = null;
	di.w = ICON_DEFAULT_HEIGHT;
	di.h = ICON_DEFAULT_HEIGHT;
	di.icon = null;
	di.aspect = 1.0f;

	return di;
}

public static int UI_icon_get_width(int icon_id)
{
	Icon icon = null;
	DrawInfo di = null;

	icon = Icons.BKE_icon_get(icon_id);

	if (icon==null) {
		System.out.printf("UI_icon_get_width: Internal error, no icon for icon ID: %d\n", icon_id);
		return 0;
	}

	di = (DrawInfo)icon.drawinfo;
	if (di==null) {
		di = icon_create_drawinfo();
		icon.drawinfo = di;
	}

	if (di!=null)
		return di.w;

	return 0;
}

//int UI_icon_get_height(int icon_id)
//{
//	Icon *icon = NULL;
//	DrawInfo *di = NULL;
//
//	icon = BKE_icon_get(icon_id);
//
//	if (!icon) {
//		printf("UI_icon_get_height: Internal error, no icon for icon ID: %d\n", icon_id);
//		return 0;
//	}
//
//	di = (DrawInfo*)icon.drawinfo;
//
//	if (!di) {
//		di = icon_create_drawinfo();
//		icon.drawinfo = di;
//	}
//
//	if (di)
//		return di.h;
//
//	return 0;
//}

public static void UI_icons_init(int first_dyn_id, URL iconfilestr)
{
//	init_iconfile_list(&iconfilelist);
	Icons.BKE_icons_init(first_dyn_id);
	init_internal_icons(iconfilestr);
}

//static void icon_copy_rect(ImBuf *ibuf, unsigned int w, unsigned int h, unsigned int *rect)
//{
//	struct ImBuf *ima;
//	unsigned int *drect, *srect;
//	float scaledx, scaledy;
//	short ex, ey, dx, dy;
//
//	/* paranoia test */
//	if(ibuf==NULL || (ibuf.rect==NULL && ibuf.rect_float==NULL))
//		return;
//
//	/* waste of cpu cyles... but the imbuf API has no other way to scale fast (ton) */
//	ima = IMB_dupImBuf(ibuf);
//
//	if (!ima)
//		return;
//
//	if (ima.x > ima.y) {
//		scaledx = (float)w;
//		scaledy =  ( (float)ima.y/(float)ima.x )*(float)w;
//	}
//	else {
//		scaledx =  ( (float)ima.x/(float)ima.y )*(float)h;
//		scaledy = (float)h;
//	}
//
//	ex = (short)scaledx;
//	ey = (short)scaledy;
//
//	dx = (w - ex) / 2;
//	dy = (h - ey) / 2;
//
//	IMB_scalefastImBuf(ima, ex, ey);
//
//	/* if needed, convert to 32 bits */
//	if(ima.rect==NULL)
//		IMB_rect_from_float(ima);
//
//	srect = ima.rect;
//	drect = rect;
//
//	drect+= dy*w+dx;
//	for (;ey > 0; ey--){
//		memcpy(drect,srect, ex * sizeof(int));
//		drect += w;
//		srect += ima.x;
//	}
//	IMB_freeImBuf(ima);
//}
//
///* Render size for preview images at level miplevel */
//static int preview_render_size(int miplevel)
//{
//	switch (miplevel) {
//		case 0: return 32;
//		case 1: return PREVIEW_DEFAULT_HEIGHT;
//	}
//	return 0;
//}
//
//static void icon_create_mipmap(struct PreviewImage* prv_img, int miplevel)
//{
//	unsigned int size = preview_render_size(miplevel);
//
//	if (!prv_img) {
//		printf("Error: requested preview image does not exist");
//	}
//	if (!prv_img.rect[miplevel]) {
//		prv_img.w[miplevel] = size;
//		prv_img.h[miplevel] = size;
//		prv_img.changed[miplevel] = 1;
//		prv_img.rect[miplevel] = MEM_callocN(size*size*sizeof(unsigned int), "prv_rect");
//	}
//}
//
///* create single icon from jpg, png etc. */
//static void icon_from_image(Scene *scene, Image *img, int miplevel)
//{
//	ImBuf *ibuf= NULL;
//	ImageUser iuser;
//	PreviewImage *pi;
//	unsigned int pr_size;
//	short image_loaded = 0;
//
//	/* img.ok is zero when Image cannot load */
//	if (img==NULL || img.ok==0)
//		return;
//
//	/* setup dummy image user */
//	memset(&iuser, 0, sizeof(ImageUser));
//	iuser.ok= iuser.framenr= 1;
//	iuser.scene= scene;
//
//	/* elubie: this needs to be changed: here image is always loaded if not
//	   already there. Very expensive for large images. Need to find a way to
//	   only get existing ibuf */
//
//	ibuf = BKE_image_get_ibuf(img, &iuser);
//	if(ibuf==NULL || ibuf.rect==NULL) {
//		return;
//	}
//
//	pi = BKE_previewimg_get((ID*)img);
//
//	if(!pi) {
//		printf("preview image could'nt be allocated");
//		return;
//	}
//	/* we can only create the preview rect here, since loading possibly deallocated
//	   old preview */
//	icon_create_mipmap(pi, miplevel);
//
//	pr_size = img.preview.w[miplevel]*img.preview.h[miplevel]*sizeof(unsigned int);
//
//	image_loaded = 1;
//	icon_copy_rect(ibuf, img.preview.w[miplevel], img.preview.h[miplevel], img.preview.rect[miplevel]);
//}
//
//static void set_alpha(char* cp, int sizex, int sizey, char alpha)
//{
//	int x,y;
//	for(y=0; y<sizey; y++) {
//		for(x=0; x<sizex; x++, cp+=4) {
//			cp[3]= alpha;
//		}
//	}
//}
//
///* only called when icon has changed */
///* only call with valid pointer from UI_icon_draw */
//static void icon_set_image(Scene *scene, ID *id, PreviewImage* prv_img, int miplevel)
//{
//	RenderInfo ri;
//	unsigned int pr_size = 0;
//
//	if (!prv_img) {
//		printf("No preview image for this ID: %s\n", id.name);
//		return;
//	}
//
//	/* no drawing (see last parameter doDraw, just calculate preview image
//		- hopefully small enough to be fast */
//	if (GS(id.name) == ID_IM)
//		icon_from_image(scene, (struct Image*)id, miplevel);
//	else {
//		/* create the preview rect */
//		icon_create_mipmap(prv_img, miplevel);
//
//		ri.curtile= 0;
//		ri.tottile= 0;
//		ri.pr_rectx = prv_img.w[miplevel];
//		ri.pr_recty = prv_img.h[miplevel];
//		pr_size = ri.pr_rectx*ri.pr_recty*sizeof(unsigned int);
//		ri.rect = MEM_callocN(pr_size, "pr icon rect");
//
//		ED_preview_iconrender(scene, id, ri.rect, ri.pr_rectx, ri.pr_recty);
//
//		/* world is rendered with alpha=0, so it wasn't displayed
//		   this could be render option for sky to, for later */
//		if (GS(id.name) == ID_WO) {
//			set_alpha( (char*) ri.rect, ri.pr_rectx, ri.pr_recty, 255);
//		}
//		else if (GS(id.name) == ID_MA) {
//			Material* mat = (Material*)id;
//			if (mat.material_type == MA_TYPE_HALO) {
//				set_alpha( (char*) ri.rect, ri.pr_rectx, ri.pr_recty, 255);
//			}
//		}
//
//		memcpy(prv_img.rect[miplevel], ri.rect, pr_size);
//
//		/* and clean up */
//		MEM_freeN(ri.rect);
//	}
//}

static void icon_draw_rect(GL2 gl, float x, float y, int w, int h, float aspect, int rw, int rh, ByteBuffer rect)
{
	
	gl.glRasterPos2f(x, y);
//	// XXX ui_rasterpos_safe(x, y, aspect);
	
//	if((w<1 || h<1)) {
        if((w!=16 || h!=16)) {
		// XXX - TODO 2.5 verify whether this case can happen
		// and only print in debug
		System.out.printf("what the heck! - icons are %d x %d pixels?\n", w, h);
                w = 16; // TMP
                h = 16; // TMP
	}
//	/* rect contains image in 'rendersize', we only scale if needed */
//	else if(rw!=w && rh!=h) {
//		ImBuf *ima;
//		if(w>2000 || h>2000) { /* something has gone wrong! */
//			printf("insane icon size w=%d h=%d\n",w,h);
//			return;
//		}
//		/* first allocate imbuf for scaling and copy preview into it */
//		ima = IMB_allocImBuf(rw, rh, 32, IB_rect, 0);
//		memcpy(ima.rect, rect, rw*rh*sizeof(unsigned int));
//
//		/* scale it */
//		IMB_scaleImBuf(ima, w, h);
//		glDrawPixels(w, h, GL_RGBA, GL_UNSIGNED_BYTE, ima.rect);
//
//		IMB_freeImBuf(ima);
//	}
//	else
//                System.out.printf("icon_draw_rect (%dx%d) @ (%f,%f)\n", w, h, x, y);
		gl.glDrawPixels(w, h, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, rect);
}

/* Drawing size for preview images at level miplevel */
static int preview_size(int miplevel)
{
	switch (miplevel) {
		case 0: return ICON_DEFAULT_HEIGHT;
		case 1: return PREVIEW_DEFAULT_HEIGHT;
	}
	return 0;
}

static void icon_draw_size(GL2 gl, float x, float y, int icon_id, float aspect, float alpha, float[] rgb, int miplevel, int draw_size, int nocreate, boolean is_preview)
{
	Icon icon = null;
	DrawInfo di = null;
	IconImage iimg;
	int w, h;
	
	icon = Icons.BKE_icon_get(icon_id);
	
	if (icon==null) {
//		System.out.printf("icon_draw_mipmap: Internal error, no icon for icon ID: %d\n", icon_id);
		return;
	}
	
	di = (DrawInfo)icon.drawinfo;
	
	if (di==null) {
		di = icon_create_drawinfo();
	
		icon.drawinfo = di;
//		icon.drawinfo_free = UI_icons_free_drawinfo;
	}
	
//	/* scale width and height according to aspect */
//	w = (int)(draw_size/aspect + 0.5f);
//	h = (int)(draw_size/aspect + 0.5f);
//	
//	if(di.type == ICON_TYPE_VECTOR) {
//		/* vector icons use the uiBlock transformation, they are not drawn
//		with untransformed coordinates like the other icons */
//		di.data.vector.func(x, y, ICON_DEFAULT_HEIGHT, ICON_DEFAULT_HEIGHT, 1.0f); 
//	} 
//	else if(di.type == ICON_TYPE_TEXTURE) {
//		icon_draw_texture(x, y, w, h, di.data.texture.x, di.data.texture.y,
//			di.data.texture.w, di.data.texture.h, alpha, rgb);
//	}
//	else if(di.type == ICON_TYPE_BUFFER) {
//		/* it is a builtin icon */		
//		iimg= di.data.buffer.image;
//
//		if(iimg.rect==null) return; /* something has gone wrong! */
//
//		icon_draw_rect(x, y, w, h, aspect, iimg.w, iimg.h, iimg.rect, alpha, rgb, is_preview);
//	}
//	else if(di.type == ICON_TYPE_PREVIEW) {
//		PreviewImage pi = Icons.BKE_previewimg_get((ID)icon.obj); 
//
//		if(pi!=null) {			
//			/* no create icon on this level in code */
//			if(pi.rect[miplevel]==null) return; /* something has gone wrong! */
//			
//			/* preview images use premul alpha ... */
//			gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
//			icon_draw_rect(x, y, w, h, aspect, pi.w[miplevel], pi.h[miplevel], pi.rect[miplevel], 1.0f, null, is_preview);
//			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
//		}
//	}
	
	di.aspect = aspect;
	/* scale width and height according to aspect */
	di.w = (int)(draw_size/di.aspect + 0.5f);
	di.h = (int)(draw_size/di.aspect + 0.5f);
	
	if (di.drawFunc!=null) {
		/* vector icons use the uiBlock transformation, they are not drawn
		with untransformed coordinates like the other icons */
		di.drawFunc.run(gl, (int)x, (int)y, ICON_DEFAULT_HEIGHT, ICON_DEFAULT_HEIGHT, 1.0f);
	} 
	else if (di.icon!=null) {
		/* it is a builtin icon */		
		if (di.icon.rect==null) return; /* something has gone wrong! */

		icon_draw_rect(gl, x,y,di.w, di.h, di.aspect, di.icon.w, di.icon.h, di.icon.rect);
	}
	else {
		PreviewImage pi = Icons.BKE_previewimg_get((ID)icon.obj);

		if (pi!=null) {
			/* no create icon on this level in code */
			
			if (pi.rect[miplevel]==null) return; /* something has gone wrong! */
			
			icon_draw_rect(gl, x,y,di.w, di.h, di.aspect, pi.w[miplevel], pi.h[miplevel], (ByteBuffer)pi.rect[miplevel]);
		}
	}
}

//void ui_id_icon_render(Scene *scene, ID *id)
//{
//	PreviewImage *pi = BKE_previewimg_get(id);
//
//	if (pi) {
//		if ((pi.changed[0] ||!pi.rect[0])) /* changed only ever set by dynamic icons */
//		{
//			/* create the preview rect if necessary */
//			icon_set_image(scene, id, pi, 0);
//			pi.changed[0] = 0;
//		}
//	}
//}
//
//int ui_id_icon_get(Scene *scene, ID *id)
//{
//	int iconid= 0;
//
//	/* icon */
//	switch(GS(id.name))
//	{
//		case ID_MA: /* fall through */
//		case ID_TE: /* fall through */
//		case ID_IM: /* fall through */
//		case ID_WO: /* fall through */
//		case ID_LA: /* fall through */
//			iconid= BKE_icon_getid(id);
//			/* checks if not exists, or changed */
//			ui_id_icon_render(scene, id);
//			break;
//		default:
//			break;
//	}
//
//	return iconid;
//}

static void icon_draw_mipmap(GL2 gl, float x, float y, int icon_id, float aspect, float alpha, int miplevel, int nocreate)
{
	int draw_size = preview_size(miplevel);
	icon_draw_size(gl, x, y, icon_id, aspect, alpha, null, miplevel, draw_size, nocreate, false);
}


public static void UI_icon_draw_aspect(GL2 gl, float x, float y, int icon_id, float aspect, float alpha)
{
	icon_draw_mipmap(gl, x, y, icon_id, aspect, alpha, DNA_ID.PREVIEW_MIPMAP_ZERO, 0);
}

public static void UI_icon_draw(GL2 gl, float x, float y, int icon_id)
{
	UI_icon_draw_aspect(gl, x, y, icon_id, 1.0f, 1.0f);
}

//void UI_icon_draw_size_blended(float x, float y, int size, int icon_id, int shade)
//{
//	if(shade < 0) {
//		float r= (128+shade)/128.0f;
//		glPixelTransferf(GL_ALPHA_SCALE, r);
//	}
//
//	icon_draw_size(x,y,icon_id, 1.0f, 0, size, 1);
//
//	if(shade < 0)
//		glPixelTransferf(GL_ALPHA_SCALE, 1.0f);
//}
//
//void UI_icon_draw_preview(float x, float y, int icon_id, int nocreate)
//{
//	icon_draw_mipmap(x,y,icon_id, 1.0f, PREVIEW_MIPMAP_LARGE, nocreate);
//}

//public static void UI_icon_draw_aspect_blended(GL2 gl, float x, float y, int icon_id, float aspect, int shade)
//{
////        System.out.println("UI_icon_draw_aspect_blended");
//	if(shade < 0) {
//		float r= (128+shade)/128.0f;
//		gl.glPixelTransferf(GL2.GL_ALPHA_SCALE, r);
//	}
//
//	UI_icon_draw_aspect(gl, x, y, icon_id, aspect);
//
//	if(shade < 0)
//		gl.glPixelTransferf(GL2.GL_ALPHA_SCALE, 1.0f);
//}

}