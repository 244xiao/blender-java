/**
 * $Id: ButtonsHeader.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
package blender.editors.space_buttons;

//#include <string.h>

import blender.blenkernel.Blender;
import blender.blenkernel.Pointer;
import blender.blenkernel.bContext;
import blender.editors.screen.Area;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiHandleFunc;
import blender.editors.uinterface.View2dUtil;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.SpaceButs;
import javax.media.opengl.GL2;

//#include <stdio.h>
//
//#include "DNA_object_types.h"
//#include "DNA_space_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "MEM_guardedalloc.h"
//
//#include "BLI_blenlib.h"
//
//#include "BKE_context.h"
//#include "BKE_screen.h"
//#include "BKE_utildefines.h"
//
//#include "ED_screen.h"
//#include "ED_types.h"
//#include "ED_util.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "BIF_gl.h"
//#include "BIF_glutil.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"
//#include "UI_view2d.h"
//
//#include "buttons_intern.h"
//
public class ButtonsHeader {

public static final int B_CONTEXT_SWITCH=	101;
public static final int B_BUTSPREVIEW=		102;

public static uiHandleFunc do_buttons_buttons = new uiHandleFunc() {
public void run(bContext C, Object arg, Object event)
//static void do_buttons_buttons(bContext *C, void *arg, int event)
{
	SpaceButs sbuts= bContext.CTX_wm_space_buts(C);

	if(sbuts==null) /* window type switch */
		return;

	switch((Integer)event) {
		case B_CONTEXT_SWITCH:
		case B_BUTSPREVIEW:
			Area.ED_area_tag_redraw(bContext.CTX_wm_area(C));

			/* silly exception */
			if(sbuts.mainb == SpaceTypes.BCONTEXT_WORLD)
				sbuts.flag |= SpaceTypes.SB_WORLD_TEX;
			else if(sbuts.mainb != SpaceTypes.BCONTEXT_TEXTURE)
				sbuts.flag &= ~SpaceTypes.SB_WORLD_TEX;

			sbuts.preview= 1;
			break;
	}

	sbuts.mainbuser= sbuts.mainb;
}};

public static void buttons_header_buttons(GL2 gl, bContext C, ARegion ar)
{
	final SpaceButs sbuts= bContext.CTX_wm_space_buts(C);
	uiBlock block;
	int xco, yco= 3;

	ButtonsContext.buttons_context_compute(C, sbuts);
	
	block= UI.uiBeginBlock(C, ar, "header buttons", UI.UI_EMBOSS);
	UI.uiBlockSetHandleFunc(block, do_buttons_buttons, null);
	
	xco= Area.ED_area_header_switchbutton(C, block, yco);
	
	UI.uiBlockSetEmboss(block, UI.UI_EMBOSS);

	xco -= Blender.XIC;
	
	// Default panels
	UI.uiBlockBeginAlign(block);
        Pointer<Short> sbuts_mainb = new Pointer<Short>() {
            public Short get() {
                return sbuts.mainb;
            }
            public void set(Short obj) {
                sbuts.mainb = obj;
            }
        };
    if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_RENDER))!=0)
    	UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_SCENE,			xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_RENDER, 0, 0, "Render");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_SCENE))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_SCENE_DATA,		xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_SCENE, 0, 0, "Scene");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_WORLD))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_WORLD,		xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_WORLD, 0, 0, "World");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_OBJECT))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_OBJECT_DATA,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_OBJECT, 0, 0, "Object");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_CONSTRAINT))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_CONSTRAINT,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_CONSTRAINT, 0, 0, "Constraint");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_MODIFIER))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_MODIFIER,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_MODIFIER, 0, 0, "Modifier");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_DATA))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,BIFIconID.values()[sbuts.dataicon],    xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_DATA, 0, 0, "Object Data");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_BONE))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_BONE_DATA,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_BONE, 0, 0, "Bone");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_BONE_CONSTRAINT))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_CONSTRAINT,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_BONE_CONSTRAINT, 0, 0, "Bone Constraints");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_MATERIAL))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_MATERIAL,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_MATERIAL, 0, 0, "Material");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_TEXTURE))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_BUTSPREVIEW,          BIFIconID.ICON_TEXTURE,         xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_TEXTURE, 0, 0, "Texture");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_PARTICLE))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_PARTICLES,	xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_PARTICLE, 0, 0, "Particles");
	if((sbuts.pathflag & (1<<SpaceTypes.BCONTEXT_PHYSICS))!=0)
		UI.uiDefIconButS(block, UI.ROW, B_CONTEXT_SWITCH,	BIFIconID.ICON_PHYSICS,         xco+=SpaceButtonsUtil.BUTS_UI_UNIT, yco, (int)SpaceButtonsUtil.BUTS_UI_UNIT, (int)SpaceButtonsUtil.BUTS_UI_UNIT, sbuts_mainb, 0.0f, (float)SpaceTypes.BCONTEXT_PHYSICS, 0, 0, "Physics");
	xco+= SpaceButtonsUtil.BUTS_UI_UNIT;
	
	UI.uiBlockEndAlign(block);
	
	/* always as last  */
	View2dUtil.UI_view2d_totRect_set(ar.v2d, xco+Blender.XIC+80, (int)(ar.v2d.tot.ymax-ar.v2d.tot.ymin));
	
	UI.uiEndBlock(C, block);
	UI.uiDrawBlock(gl, C, block);
}

}

