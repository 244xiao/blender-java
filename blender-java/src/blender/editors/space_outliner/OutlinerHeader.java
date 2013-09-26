/**
 * $Id: OutlinerHeader.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
package blender.editors.space_outliner;

import blender.blenkernel.Blender;
import blender.blenkernel.Pointer;
import blender.blenkernel.bContext;
import blender.editors.screen.Area;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBlockCreateFunc;
import blender.editors.uinterface.UI.uiHandleFunc;
import blender.editors.uinterface.View2dUtil;
import blender.editors.util.EdUtil;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceOops;
import javax.media.opengl.GL2;
import static blender.blenkernel.Blender.G;

public class OutlinerHeader {
//
///* ************************ header area region *********************** */
//
//static void do_viewmenu(bContext *C, void *arg, int event)
//{
//	ScrArea *curarea= CTX_wm_area(C);
//	SpaceOops *soops= curarea.spacedata.first;
//
//	switch(event) {
//		case 0: /* Shuffle Selected Blocks */
//			//shuffle_oops();
//			break;
//		case 1: /* Shrink Selected Blocks */
//			//shrink_oops();
//			break;
//		case 2: /* View All */
//			//do_oops_buttons(B_OOPSHOME);
//			break;
//		case 3: /* View All */
//			//do_oops_buttons(B_OOPSVIEWSEL);
//			break;
//		case 4: /* Maximize Window */
//			/* using event B_FULL */
//			break;
//			break;
//		case 6:
//			//outliner_toggle_visible(curarea);
//			break;
//		case 7:
//			//outliner_show_hierarchy(curarea);
//			break;
//		case 8:
//			//outliner_show_active(curarea);
//			break;
//		case 9:
//			//outliner_one_level(curarea, 1);
//			break;
//		case 10:
//			//outliner_one_level(curarea, -1);
//			break;
//		case 12:
//			if (soops.flag & SO_HIDE_RESTRICTCOLS) soops.flag &= ~SO_HIDE_RESTRICTCOLS;
//			else soops.flag |= SO_HIDE_RESTRICTCOLS;
//			break;
//	}
//	ED_area_tag_redraw(curarea);
//}
//
public static uiBlockCreateFunc outliner_viewmenu = new uiBlockCreateFunc() {
public uiBlock run(bContext C, ARegion ar, Object arg_unused)
//static uiBlock *outliner_viewmenu(bContext *C, ARegion *ar, void *arg_unused)
{
	ScrArea curarea= bContext.CTX_wm_area(C);
	SpaceOops soops= (SpaceOops)curarea.spacedata.first;
	uiBlock block;
	short yco= 0, menuwidth=120;

	block= UI.uiBeginBlock(C, ar, "outliner_viewmenu", UI.UI_EMBOSSP);
//	uiBlockSetButmFunc(block, do_viewmenu, NULL);

	if ((soops.flag & SpaceTypes.SO_HIDE_RESTRICTCOLS)!=0)
		UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_CHECKBOX_DEHLT, "Show Restriction Columns", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 12, "");
	else
		UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_CHECKBOX_HLT, "Show Restriction Columns", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 12, "");

	UI.uiDefBut(block, UI.SEPR, 0, "",        0, yco-=6, menuwidth, 6, null, 0.0f, 0.0f, 0, 0, "");

	UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_BLANK1, "Expand One Level|NumPad +", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 9, "");
	UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_BLANK1, "Collapse One Level|NumPad -", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 10, "");

	UI.uiDefBut(block, UI.SEPR, 0, "",        0, yco-=6, menuwidth, 6, null, 0.0f, 0.0f, 0, 0, "");

	UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_BLANK1, "Show/Hide All", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 6, "");
	UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_BLANK1, "Show Hierarchy|Home", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 7, "");
	UI.uiDefIconTextBut(block, UI.BUTM, 1, BIFIconID.ICON_BLANK1, "Show Active|NumPad .", 0, yco-=20, menuwidth, 19, null, 0.0f, 0.0f, 1, 8, "");

//	uiDefBut(block, SEPR, 0, "",        0, yco-=6, menuwidth, 6, NULL, 0.0, 0.0, 0, 0, "");
//	if(!curarea.full) uiDefIconTextBut(block, BUTM, B_FULL, ICON_BLANK1, "Maximize Window|Ctrl UpArrow", 0, yco-=20, menuwidth, 19, NULL, 0.0, 0.0, 0, 4, "");
//	else uiDefIconTextBut(block, BUTM, B_FULL, ICON_BLANK1, "Tile Window|Ctrl DownArrow", 0, yco-=20, menuwidth, 19, NULL, 0.0, 0.0, 0, 4, "");


	if(curarea.headertype==ScreenTypes.HEADERTOP) {
		UI.uiBlockSetDirection(block, UI.UI_DOWN);
	}
	else {
		UI.uiBlockSetDirection(block, UI.UI_TOP);
		UI.uiBlockFlipOrder(block);
	}

	UI.uiTextBoundsBlock(block, 50);
	UI.uiEndBlock(C, block);

	return block;
}};

//enum {
	public static final int B_REDR	= 1;

	public static final int B_KEYINGSET_CHANGE = 2;
	public static final int B_KEYINGSET_REMOVE = 3;
//} eOutlinerHeader_Events;

public static uiHandleFunc do_outliner_buttons = new uiHandleFunc() {
public void run(bContext C, Object arg, Object event)
//static void do_outliner_buttons(bContext *C, void *arg, int event)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	Scene scene= bContext.CTX_data_scene(C);

	switch((Integer)event) {
		case B_REDR:
			Area.ED_area_tag_redraw(sa);
			break;

		case B_KEYINGSET_CHANGE:
//			/* add a new KeyingSet if active is -1 */
//			if (scene.active_keyingset == -1) {
//				// XXX the default settings have yet to evolve... need to keep this in sync with the
//				BKE_keyingset_add(&scene.keyingsets, "KeyingSet", KEYINGSET_ABSOLUTE, 0);
//				scene.active_keyingset= BLI_countlist(&scene.keyingsets);
//			}
//
//			/* redraw regions with KeyingSet info */
//			WM_event_add_notifier(C, NC_SCENE|ND_KEYINGSET, scene);
			break;

		case B_KEYINGSET_REMOVE:
//			/* remove the active KeyingSet */
//			if (scene.active_keyingset) {
//				KeyingSet *ks= BLI_findlink(&scene.keyingsets, scene.active_keyingset-1);
//
//				/* firstly free KeyingSet's data, then free the KeyingSet itself */
//				if (ks) {
//					BKE_keyingset_free(ks);
//					BLI_freelinkN(&scene.keyingsets, ks);
//					scene.active_keyingset= 0;
//				}
//			}
//
//			/* redraw regions with KeyingSet info */
//			WM_event_add_notifier(C, NC_SCENE|ND_KEYINGSET, scene);
			break;
	}
}};


public static void outliner_header_buttons(GL2 gl, bContext C, ARegion ar)
{
	final ScrArea sa= bContext.CTX_wm_area(C);
	Scene scene= bContext.CTX_data_scene(C);
	final SpaceOops soutliner= bContext.CTX_wm_space_outliner(C);
	uiBlock block;
	int xco, yco= 3, xmax;

	block= UI.uiBeginBlock(C, ar, "header buttons", UI.UI_EMBOSS);
	UI.uiBlockSetHandleFunc(block, do_outliner_buttons, null);

	xco= Area.ED_area_header_standardbuttons(C, block, yco);

	if((sa.flag & ScreenTypes.HEADER_NO_PULLDOWN)==0) {

		xmax= EdUtil.GetButStringLength("View");

                Pointer<ScrArea> sa_p = new Pointer<ScrArea>() {
                    public ScrArea get() {
                        return sa;
                    }
                    public void set(ScrArea obj) {

                    }
                };

//                System.out.println("outliner_header_buttons view");
		UI.uiDefPulldownBut(block, outliner_viewmenu, sa_p,
						 "View", xco, yco-2, xmax-3, 24, "");
		xco += xmax;

		/* header text */
		xco += Blender.XIC*2;

		UI.uiBlockSetEmboss(block, (short)UI.UI_EMBOSS);
	}

	/* data selector*/
        Pointer<Short> soutliner_outlinevis = new Pointer<Short>() {
            public Short get() {
                return soutliner.outlinevis;
            }
            public void set(Short obj) {
                soutliner.outlinevis = obj;
            }
        };
	if(G.main.library.first!=null)
		UI.uiDefButS(block, UI.MENU, B_REDR, "Outliner Display%t|Libraries %x7|All Scenes %x0|Current Scene %x1|Visible Layers %x2|Groups %x6|Same Types %x5|Selected %x3|Active %x4|Sequence %x10|Datablocks %x11|User Preferences %x12||Key Maps %x13",	 xco, yco, 120, 20,  soutliner_outlinevis, 0, 0, 0, 0, "");
	else
		UI.uiDefButS(block, UI.MENU, B_REDR, "Outliner Display%t|All Scenes %x0|Current Scene %x1|Visible Layers %x2|Groups %x6|Same Types %x5|Selected %x3|Active %x4|Sequence %x10|Datablocks %x11|User Preferences %x12||Key Maps %x13",	 xco, yco, 120, 20,  soutliner_outlinevis, 0, 0, 0, 0, "");
	xco += 120;

//	/* KeyingSet editing buttons */
//	if ((soutliner.flag & SO_HIDE_KEYINGSETINFO)==0 && (soutliner.outlinevis==SO_DATABLOCKS)) {
//		KeyingSet *ks= NULL;
//		char *menustr= NULL;
//
//		xco+= (int)(XIC*1.5);
//
//		if (scene.active_keyingset)
//			ks= (KeyingSet *)BLI_findlink(&scene.keyingsets, scene.active_keyingset-1);
//
//		uiBlockBeginAlign(block);
//			/* currently 'active' KeyingSet */
//			menustr= ANIM_build_keyingsets_menu(&scene.keyingsets, 1);
//			uiDefButI(block, MENU, B_KEYINGSET_CHANGE, menustr, xco,yco, 18,20, &scene.active_keyingset, 0, 0, 0, 0, "Browse Keying Sets");
//			MEM_freeN(menustr);
//			xco += 18;
//
//			/* currently 'active' KeyingSet - change name */
//			if (ks) {
//				/* active KeyingSet */
//				uiDefBut(block, TEX, B_KEYINGSET_CHANGE,"", xco,yco,120,20, ks.name, 0, 63, 0, 0, "Name of Active Keying Set");
//				xco += 120;
//				uiDefIconBut(block, BUT, B_KEYINGSET_REMOVE, VICON_X, xco, yco, 20, 20, NULL, 0.0, 0.0, 0.0, 0.0, "Remove this Keying Set");
//				xco += 20;
//			}
//			else {
//				/* no active KeyingSet... so placeholder instead */
//				uiDefBut(block, LABEL, 0,"<No Keying Set Active>", xco,yco,140,20, NULL, 0, 63, 0, 0, "Name of Active Keying Set");
//				xco += 140;
//			}
//		uiBlockEndAlign(block);
//
//		/* current 'active' KeyingSet */
//		if (ks) {
//			xco += 5;
//
//			/* operator buttons to add/remove selected items from set */
//			uiBlockBeginAlign(block);
//					// XXX the icons here are temporary
//				uiDefIconButO(block, BUT, "OUTLINER_OT_keyingset_remove_selected", WM_OP_INVOKE_REGION_WIN, ICON_ZOOMOUT, xco,yco,XIC,YIC, "Remove selected properties from active Keying Set (Alt-K)");
//				xco += XIC;
//				uiDefIconButO(block, BUT, "OUTLINER_OT_keyingset_add_selected", WM_OP_INVOKE_REGION_WIN, ICON_ZOOMIN, xco,yco,XIC,YIC, "Add selected properties to active Keying Set (K)");
//				xco += XIC;
//			uiBlockEndAlign(block);
//
//			xco += 10;
//
//			/* operator buttons to insert/delete keyframes for the active set */
//			uiBlockBeginAlign(block);
//				uiDefIconButO(block, BUT, "ANIM_OT_delete_keyframe", WM_OP_INVOKE_REGION_WIN, ICON_KEY_DEHLT, xco,yco,XIC,YIC, "Delete Keyframes for the Active Keying Set (Alt-I)");
//				xco+= XIC;
//				uiDefIconButO(block, BUT, "ANIM_OT_insert_keyframe", WM_OP_INVOKE_REGION_WIN, ICON_KEY_HLT, xco,yco,XIC,YIC, "Insert Keyframes for the Active Keying Set (I)");
//				xco+= XIC;
//			uiBlockEndAlign(block);
//		}
//
//		xco += XIC*2;
//	}

	/* always as last  */
	View2dUtil.UI_view2d_totRect_set(ar.v2d, xco+Blender.XIC+100, (int)(ar.v2d.tot.ymax-ar.v2d.tot.ymin));

	UI.uiEndBlock(C, block);
	UI.uiDrawBlock(gl, C, block);
}

}

