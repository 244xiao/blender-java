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

//#include <float.h>

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenkernel.Pointer;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.bContext.bContextStore;
import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.blenlib.StringUtil;
import blender.blenlib.Time;
import blender.editors.screen.Area;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBlockCreateFunc;
import blender.editors.uinterface.UI.uiBlockHandleCreateFunc;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UI.uiHandleFunc;
import blender.editors.uinterface.UI.uiMenuCreateFunc;
import blender.editors.uinterface.UI.uiPopupBlockHandle;
import blender.editors.uinterface.UI.uiSafetyRct;
import blender.makesdna.ScreenTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.CBData;
import blender.makesdna.sdna.ColorBand;
import blender.makesdna.sdna.CurveMapping;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmWindow;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.WmUIHandlerFunc;
import blender.windowmanager.WmTypes.WmUIHandlerRemoveFunc;
import blender.windowmanager.WmTypes.wmEvent;

//#include <math.h>
//#include <stdlib.h>
//#include <string.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_color_types.h"
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_texture_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "BLI_arithb.h"
//#include "BLI_blenlib.h"
//#include "PIL_time.h"
//
//#include "BKE_colortools.h"
//#include "BKE_context.h"
//#include "BKE_idprop.h"
//#include "BKE_report.h"
//#include "BKE_texture.h"
//#include "BKE_utildefines.h"
//
//#include "ED_screen.h"
//#include "ED_util.h"
//
//#include "UI_interface.h"
//
//#include "BLF_api.h"
//
//#include "interface_intern.h"
//
//#include "RNA_access.h"
//
//#include "WM_api.h"
//#include "WM_types.h"

public class UIHandlers {

/***************** structs and defines ****************/

public static final float BUTTON_TOOLTIP_DELAY=		0.500f;
public static final float BUTTON_FLASH_DELAY=			0.020f;
public static final float BUTTON_AUTO_OPEN_THRESH=		0.3f;
public static final float BUTTON_MOUSE_TOWARDS_THRESH=	1.0f;

public static enum uiButtonActivateType {
	BUTTON_ACTIVATE_OVER,
	BUTTON_ACTIVATE,
	BUTTON_ACTIVATE_APPLY,
	BUTTON_ACTIVATE_TEXT_EDITING,
	BUTTON_ACTIVATE_OPEN
};

public static enum uiHandleButtonState {
	BUTTON_STATE_INIT,
	BUTTON_STATE_HIGHLIGHT,
	BUTTON_STATE_WAIT_FLASH,
	BUTTON_STATE_WAIT_RELEASE,
	BUTTON_STATE_WAIT_KEY_EVENT,
	BUTTON_STATE_NUM_EDITING,
	BUTTON_STATE_TEXT_EDITING,
	BUTTON_STATE_TEXT_SELECTING,
	BUTTON_STATE_MENU_OPEN,
	BUTTON_STATE_EXIT
};

public static class uiHandleButtonData {
	public wmWindow window;
	public ARegion region;

	public int interactive;

	/* overall state */
	public uiHandleButtonState state;
	public int cancel, escapecancel, retval;
	public int applied, appliedinteractive;
//	public wmTimer flashtimer;

	/* edited value */
	public byte[] str, origstr;
	public double value, origvalue, startvalue;
	public float[] vec=new float[3], origvec=new float[3];
	public int togdual, togonly;
	public ColorBand coba;
	public CurveMapping cumap;

	/* tooltip */
//	public ARegion tooltip;
//	public wmTimer tooltiptimer;

	/* auto open */
	public int used_mouse;
//	public wmTimer autoopentimer;

	/* text selection/editing */
	public int maxlen, selextend, selstartx;

	/* number editing / dragging */
	public int draglastx, draglasty;
	public int dragstartx, dragstarty;
	public int dragchange, draglock, dragsel;
	public float dragf, dragfstart;
	public CBData dragcbd;

	/* menu open */
	public uiPopupBlockHandle menu;
	public int menuretval;

	/* search box */
	public ARegion searchbox;

	/* post activate */
	public uiButtonActivateType posttype;
	public uiBut postbut;
};

public static class uiAfterFunc extends Link<uiAfterFunc> implements Cloneable {
	public uiHandleFunc func;
	public Object func_arg1;
	public Object func_arg2;
	public Object func_arg3;

	public uiHandleFunc funcN;
	public Object func_argN;

	public uiHandleFunc rename_func;
	public Object rename_arg1;
	public Object rename_orig;

	public uiHandleFunc handle_func;
	public Object handle_func_arg;
	public int retval;

	public uiHandleFunc butm_func;
	public Object butm_func_arg;
	public int a2;

	public wmOperatorType optype;
	public int opcontext;
	public PointerRNA opptr;

	public PointerRNA rnapoin = new PointerRNA();
	public PropertyRNA rnaprop;

	public bContextStore context;

//	public char undostr[512];

	public int autokey;

        public uiAfterFunc copy() { try {return (uiAfterFunc)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
};

//static int ui_mouse_inside_button(ARegion *ar, uiBut *but, int x, int y);
//static void button_activate_state(bContext *C, uiBut *but, uiHandleButtonState state);
//static int ui_handler_region_menu(bContext *C, wmEvent *event, void *userdata);
//static int ui_handler_popup(bContext *C, wmEvent *event, void *userdata);
//static void ui_handler_remove_popup(bContext *C, void *userdata);
//static void ui_handle_button_activate(bContext *C, ARegion *ar, uiBut *but, uiButtonActivateType type);
//static void button_timers_tooltip_remove(bContext *C, uiBut *but);

/* ******************** menu navigation helpers ************** */

static uiBut ui_but_prev(uiBut but)
{
	while(but.prev!=null) {
		but= but.prev;
		if(!(but.type==UI.LABEL || but.type==UI.SEPR || but.type==UI.ROUNDBOX || but.type==UI.LISTBOX))
                    return but;
	}
	return null;
}

static uiBut ui_but_next(uiBut but)
{
	while(but.next!=null) {
		but= but.next;
		if(!(but.type==UI.LABEL || but.type==UI.SEPR || but.type==UI.ROUNDBOX || but.type==UI.LISTBOX))
                    return but;
	}
	return null;
}

static uiBut ui_but_first(uiBlock block)
{
	uiBut but;

	but= block.buttons.first;
	while(but!=null) {
		if(!(but.type==UI.LABEL || but.type==UI.SEPR || but.type==UI.ROUNDBOX || but.type==UI.LISTBOX))
                    return but;
		but= but.next;
	}
	return null;
}

static uiBut ui_but_last(uiBlock block)
{
	uiBut but;

	but= block.buttons.last;
	while(but!=null) {
		if(!(but.type==UI.LABEL || but.type==UI.SEPR || but.type==UI.ROUNDBOX || but.type==UI.LISTBOX))
                    return but;
		but= but.prev;
	}
	return null;
}

/* ********************** button apply/revert ************************/

static ListBase<uiAfterFunc> UIAfterFuncs = new ListBase<uiAfterFunc>();

static void ui_apply_but_func(bContext C, uiBut but)
{
        System.out.println("ui_apply_but_func");
	uiAfterFunc after;
	uiBlock block= but.block;

	/* these functions are postponed and only executed after all other
	 * handling is done, i.e. menus are closed, in order to avoid conflicts
	 * with these functions removing the buttons we are working with */

	if(but.func!=null || but.funcN!=null || block.handle_func!=null || but.rename_func!=null || (but.type == UI.BUTM && block.butm_func!=null) || but.optype!=null || but.rnaprop!=null) {
		after= new uiAfterFunc();

		after.func= but.func;
		after.func_arg1= but.func_arg1;
		after.func_arg2= but.func_arg2;
		after.func_arg3= but.func_arg3;

		after.funcN= but.funcN;
		after.func_argN= but.func_argN;

		after.rename_func= but.rename_func;
		after.rename_arg1= but.rename_arg1;
		after.rename_orig= but.rename_orig; /* needs free! */

		after.handle_func= block.handle_func;
		after.handle_func_arg= block.handle_func_arg;
		after.retval= but.retval;

		if(but.type == UI.BUTM) {
			after.butm_func= block.butm_func;
			after.butm_func_arg= block.butm_func_arg;
			after.a2= (int)but.a2;
		}

		after.optype= but.optype;
		after.opcontext= but.opcontext;
		after.opptr= but.opptr;
		if (but.opptr==null) {
			System.out.println("null but opptr");
		}

		after.rnapoin= but.rnapoin;
		after.rnaprop= but.rnaprop;

//		if(but.context!=null)
//			after.context= bContext.CTX_store_copy(but.context);

		but.optype= null;
		but.opcontext= 0;
		but.opptr= null;

		ListBaseUtil.BLI_addtail(UIAfterFuncs, after);
	}
}

//static void ui_apply_autokey_undo(bContext *C, uiBut *but)
//{
//	Scene *scene= CTX_data_scene(C);
//	uiAfterFunc *after;
//	char *str= NULL;
//
//	if ELEM6(but.type, BLOCK, BUT, LABEL, PULLDOWN, ROUNDBOX, LISTBOX);
//	else {
//		/* define which string to use for undo */
//		if ELEM(but.type, LINK, INLINK) str= "Add button link";
//		else if ELEM(but.type, MENU, ICONTEXTROW) str= but.drawstr;
//		else if(but.drawstr[0]) str= but.drawstr;
//		else str= but.tip;
//	}
//
//	/* delayed, after all other funcs run, popups are closed, etc */
//	if(str) {
//		after= MEM_callocN(sizeof(uiAfterFunc), "uiAfterFunc");
//		BLI_strncpy(after.undostr, str, sizeof(after.undostr));
//		BLI_addtail(&UIAfterFuncs, after);
//	}
//
//	/* try autokey */
//	ui_but_anim_autokey(but, scene, scene.r.cfra);
//}

static void ui_apply_but_funcs_after(bContext C)
{
//        System.out.println("ui_apply_but_funcs_after");
	uiAfterFunc afterf, after;
	ListBase<uiAfterFunc> funcs;

	/* copy to avoid recursive calls */
	funcs= UIAfterFuncs.copy();
	UIAfterFuncs.first= UIAfterFuncs.last= null;

	for(afterf=funcs.first; afterf!=null; afterf=after.next) {
		after= afterf.copy(); /* copy to avoid memleak on exit() */
		ListBaseUtil.BLI_freelinkN(funcs, afterf);

//		if(after.context!=null)
//			bContext.CTX_store_set(C, after.context);
		
		if (after.opptr==null) {
			System.out.println("invalid after popinter");
		}
		if (after.opptr!=null && after.opptr.id==null) {
			System.out.println("invalid after popinter id");
		}

		if(after.optype!=null)
			WmEventSystem.WM_operator_name_call(C, after.optype.idname, after.opcontext, after.opptr);
//		if(after.opptr!=null) {
//			WmEventSystem.WM_operator_properties_free(after.opptr);
//			MEM_freeN(after.opptr);
//		}

//		if(after.rnapoin.data)
//			RNA_property_update(C, &after.rnapoin, after.rnaprop);
//
//		if(after.context) {
//			CTX_store_set(C, NULL);
//			CTX_store_free(after.context);
//		}

		if(after.func!=null)
			after.func.run(C, after.func_arg1, after.func_arg2);
		if(after.funcN!=null)
			after.funcN.run(C, after.func_argN, after.func_arg2);

		if(after.handle_func!=null)
			after.handle_func.run(C, after.handle_func_arg, after.retval);
		if(after.butm_func!=null)
			after.butm_func.run(C, after.butm_func_arg, after.a2);

		if(after.rename_func!=null)
			after.rename_func.run(C, after.rename_arg1, after.rename_orig);
//		if(after.rename_orig!=null)
//			MEM_freeN(after.rename_orig);

//		if(after.undostr[0])
//			ED_undo_push(C, after.undostr);
	}
}

static void ui_apply_but_BUT(bContext C, uiBut but, uiHandleButtonData data)
{
	ui_apply_but_func(C, but);

	data.retval= but.retval;
	data.applied= 1;
}

static void ui_apply_but_BUTM(bContext C, uiBut but, uiHandleButtonData data)
{
	UI.ui_set_but_val(but, but.hardmin);
	ui_apply_but_func(C, but);

	data.retval= but.retval;
	data.applied= 1;
}

static void ui_apply_but_BLOCK(bContext C, uiBut but, uiHandleButtonData data)
{
	if(but.type == UI.COL) {
//		if(but.a1 != -1) // this is not a color picker (weak!)
//			ui_set_but_vectorf(but, data.vec);
	}
	else if(but.type==UI.MENU || but.type==UI.ICONROW || but.type==UI.ICONTEXTROW)
		UI.ui_set_but_val(but, data.value);

	UI.ui_check_but(but);
	ui_apply_but_func(C, but);
	data.retval= but.retval;
	data.applied= 1;
}

static void ui_apply_but_TOG(bContext C, uiBlock block, uiBut but, uiHandleButtonData data)
{
    System.out.println("ui_apply_but_TOG");
	double value;
        boolean w;
	int lvalue, push;

//	/* local hack... */
//	if(but.type==BUT_TOGDUAL && data.togdual) {
//		if(but.pointype==SHO)
//			but.poin += 2;
//		else if(but.pointype==INT)
//			but.poin += 4;
//	}

	value= UI.ui_get_but_val(but);
	lvalue= (int)value;

	if(but.bit!=0) {
		w= UtilDefines.BTST(lvalue, but.bitnr);
		if(w) lvalue = UtilDefines.BCLR(lvalue, but.bitnr);
		else lvalue = UtilDefines.BSET(lvalue, but.bitnr);

		if(but.type==UI.TOGR) {
//			if(!data.togonly) {
//				lvalue= 1<<(but.bitnr);
//
//				ui_set_but_val(but, (double)lvalue);
//			}
//			else {
//				if(lvalue==0) lvalue= 1<<(but.bitnr);
//			}
		}

		UI.ui_set_but_val(but, (double)lvalue);
		if(but.type==UI.ICONTOG || but.type==UI.ICONTOGN)
                    UI.ui_check_but(but);
	}
	else {

		if(value==0.0) push= 1;
		else push= 0;

		if(but.type==UI.TOGN || but.type==UI.ICONTOGN || but.type==UI.OPTIONN)
                    push = (push==0?1:0);
		UI.ui_set_but_val(but, (double)push);
		if(but.type==UI.ICONTOG || but.type==UI.ICONTOGN)
                    UI.ui_check_but(but);
	}

//	/* end local hack... */
//	if(but.type==BUT_TOGDUAL && data.togdual) {
//		if(but.pointype==SHO)
//			but.poin -= 2;
//		else if(but.pointype==INT)
//			but.poin -= 4;
//	}

	ui_apply_but_func(C, but);

	data.retval= but.retval;
	data.applied= 1;
}

static void ui_apply_but_ROW(bContext C, uiBlock block, uiBut but, uiHandleButtonData data)
{
	uiBut bt;

	UI.ui_set_but_val(but, but.hardmax);

	/* states of other row buttons */
	for(bt= block.buttons.first; bt!=null; bt= bt.next)
		if(bt!=but && bt.poin==but.poin && (bt.type==UI.ROW || bt.type==UI.LISTROW))
			UI.ui_check_but(bt);

	ui_apply_but_func(C, but);

	data.retval= but.retval;
	data.applied= 1;
}

//static void ui_apply_but_TEX(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	if(!data.str)
//		return;
//
//	ui_set_but_string(C, but, data.str);
//	ui_check_but(but);
//
//	/* give butfunc the original text too */
//	/* feature used for bone renaming, channels, etc */
//	/* afterfunc frees origstr */
//	but.rename_orig= data.origstr;
//	data.origstr= NULL;
//	ui_apply_but_func(C, but);
//
//	data.retval= but.retval;
//	data.applied= 1;
//}

static void ui_apply_but_NUM(bContext C, uiBut but, uiHandleButtonData data)
{
//	System.out.println("ui_apply_but_NUM");
	if(data.str!=null) {
		if(UI.ui_set_but_string(C, but, data.str,0)!=0) {
			data.value= UI.ui_get_but_val(but);
//			System.out.println("ui_apply_but_NUM value: "+data.value);
		}
		else {
			data.cancel= 1;
//			System.out.println("ui_apply_but_NUM cancel");
			return;
		}
	}
	else {
		UI.ui_set_but_val(but, data.value);
		System.out.println("ui_apply_but_NUM set: "+data.value);
	}

	UI.ui_check_but(but);
	ui_apply_but_func(C, but);

	data.retval= but.retval;
	data.applied= 1;
}

//static void ui_apply_but_TOG3(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	if(but.pointype==SHO ) {
//		short *sp= (short *)but.poin;
//
//		if( BTST(sp[1], but.bitnr)) {
//			sp[1]= BCLR(sp[1], but.bitnr);
//			sp[0]= BCLR(sp[0], but.bitnr);
//		}
//		else if( BTST(sp[0], but.bitnr)) {
//			sp[1]= BSET(sp[1], but.bitnr);
//		} else {
//			sp[0]= BSET(sp[0], but.bitnr);
//		}
//	}
//	else {
//		if( BTST(*(but.poin+2), but.bitnr)) {
//			*(but.poin+2)= BCLR(*(but.poin+2), but.bitnr);
//			*(but.poin)= BCLR(*(but.poin), but.bitnr);
//		}
//		else if( BTST(*(but.poin), but.bitnr)) {
//			*(but.poin+2)= BSET(*(but.poin+2), but.bitnr);
//		} else {
//			*(but.poin)= BSET(*(but.poin), but.bitnr);
//		}
//	}
//
//	ui_check_but(but);
//	ui_apply_but_func(C, but);
//	data.retval= but.retval;
//	data.applied= 1;
//}
//
//static void ui_apply_but_VEC(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ui_set_but_vectorf(but, data.vec);
//	ui_check_but(but);
//	ui_apply_but_func(C, but);
//
//	data.retval= but.retval;
//	data.applied= 1;
//}
//
//static void ui_apply_but_COLORBAND(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ui_apply_but_func(C, but);
//	data.retval= but.retval;
//	data.applied= 1;
//}
//
//static void ui_apply_but_CURVE(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ui_apply_but_func(C, but);
//	data.retval= but.retval;
//	data.applied= 1;
//}
//
//static void ui_apply_but_IDPOIN(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ui_set_but_string(C, but, data.str);
//	ui_check_but(but);
//	ui_apply_but_func(C, but);
//	data.retval= but.retval;
//	data.applied= 1;
//}
//
//#ifdef INTERNATIONAL
//static void ui_apply_but_CHARTAB(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ui_apply_but_func(C, but);
//	data.retval= but.retval;
//	data.applied= 1;
//}
//#endif
//
//
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
//}
//
//
//static uiLinkLine *ui_is_a_link(uiBut *from, uiBut *to)
//{
//	uiLinkLine *line;
//	uiLink *link;
//
//	link= from.link;
//	if(link) {
//		line= link.lines.first;
//		while(line) {
//			if(line.from==from && line.to==to) return line;
//			line= line.next;
//		}
//	}
//	return NULL;
//}
//
//static void ui_add_link(uiBut *from, uiBut *to)
//{
//	/* in 'from' we have to add a link to 'to' */
//	uiLink *link;
//	uiLinkLine *line;
//	void **oldppoin;
//	int a;
//
//	if( (line= ui_is_a_link(from, to)) ) {
//		line.flag |= UI_SELECT;
//		ui_delete_active_linkline(from.block);
//		printf("already exists, means deletion now\n");
//		return;
//	}
//
//	if (from.type==LINK && to.type==INLINK) {
//		if( from.link.tocode != (int)to.hardmin ) {
//			printf("cannot link\n");
//			return;
//		}
//	}
//	else if(from.type==INLINK && to.type==LINK) {
//		if( to.link.tocode == (int)from.hardmin ) {
//			printf("cannot link\n");
//			return;
//		}
//	}
//
//	link= from.link;
//
//	/* are there more pointers allowed? */
//	if(link.ppoin) {
//		oldppoin= *(link.ppoin);
//
//		(*(link.totlink))++;
//		*(link.ppoin)= MEM_callocN( *(link.totlink)*sizeof(void *), "new link");
//
//		for(a=0; a< (*(link.totlink))-1; a++) {
//			(*(link.ppoin))[a]= oldppoin[a];
//		}
//		(*(link.ppoin))[a]= to.poin;
//
//		if(oldppoin) MEM_freeN(oldppoin);
//	}
//	else {
//		*(link.poin)= to.poin;
//	}
//
//}
//
//
//static void ui_apply_but_LINK(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	ARegion *ar= CTX_wm_region(C);
//	uiBut *bt;
//
//	for(bt= but.block.buttons.first; bt; bt= bt.next) {
//		if( ui_mouse_inside_button(ar, bt, but.linkto[0]+ar.winrct.xmin, but.linkto[1]+ar.winrct.ymin) )
//			break;
//	}
//	if(bt && bt!=but) {
//
//		if(but.type==LINK) ui_add_link(but, bt);
//		else ui_add_link(bt, but);
//
//		ui_apply_but_func(C, but);
//		data.retval= but.retval;
//	}
//	data.applied= 1;
//}


static void ui_apply_button(bContext C, uiBlock block, uiBut but, uiHandleButtonData data, int interactive)
{
//    System.out.println("ui_apply_button: "+(but.type>>9));
	byte[] editstr;
	Pointer<Double> editval;
	float[] editvec;
//	ColorBand *editcoba;
//	CurveMapping *editcumap;

	data.retval= 0;

	/* if we cancel and have not applied yet, there is nothing to do,
	 * otherwise we have to restore the original value again */
	if(data.cancel!=0) {
		if(data.applied==0)
			return;

		if(data.str!=null) {
//                    MEM_freeN(data.str);
        }
		data.str= data.origstr;
		data.origstr= null;
		data.value= data.origvalue;
		data.origvalue= 0.0;
		UtilDefines.VECCOPY(data.vec, data.origvec);
		data.origvec[0]= data.origvec[1]= data.origvec[2]= 0.0f;
	}
	else {
		/* we avoid applying interactive edits a second time
		 * at the end with the appliedinteractive flag */
		if(interactive!=0)
			data.appliedinteractive= 1;
		else if(data.appliedinteractive!=0)
			return;
	}

	/* ensures we are writing actual values */
	editstr= but.editstr;
	editval= but.editval;
	editvec= but.editvec;
//	editcoba= but.editcoba;
//	editcumap= but.editcumap;
	but.editstr= null;
	but.editval= null;
	but.editvec= null;
//	but.editcoba= null;
//	but.editcumap= null;

	/* handle different types */
	switch(but.type) {
		case UI.BUT:
			ui_apply_but_BUT(C, but, data);
			break;
		case UI.TEX:
		case UI.SEARCH_MENU:
//			ui_apply_but_TEX(C, but, data);
			break;
		case UI.TOGBUT:
		case UI.TOG:
		case UI.TOGR:
		case UI.ICONTOG:
		case UI.ICONTOGN:
		case UI.TOGN:
		case UI.BUT_TOGDUAL:
		case UI.OPTION:
		case UI.OPTIONN:
			ui_apply_but_TOG(C, block, but, data);
			break;
		case UI.ROW:
		case UI.LISTROW:
			ui_apply_but_ROW(C, block, but, data);
			break;
		case UI.SCROLL:
		case UI.NUM:
		case UI.NUMABS:
		case UI.SLI:
		case UI.NUMSLI:
			ui_apply_but_NUM(C, but, data);
			break;
		case UI.HSVSLI:
			break;
		case UI.TOG3:
//			ui_apply_but_TOG3(C, but, data);
			break;
		case UI.MENU:
		case UI.ICONROW:
		case UI.ICONTEXTROW:
		case UI.BLOCK:
		case UI.PULLDOWN:
		case UI.COL:
			ui_apply_but_BLOCK(C, but, data);
			break;
		case UI.BUTM:
			ui_apply_but_BUTM(C, but, data);
			break;
		case UI.BUT_NORMAL:
		case UI.HSVCUBE:
		case UI.HSVCIRCLE:
//			ui_apply_but_VEC(C, but, data);
			break;
		case UI.BUT_COLORBAND:
//			ui_apply_but_COLORBAND(C, but, data);
			break;
		case UI.BUT_CURVE:
//			ui_apply_but_CURVE(C, but, data);
			break;
		case UI.IDPOIN:
//			ui_apply_but_IDPOIN(C, but, data);
			break;
//#ifdef INTERNATIONAL
//		case CHARTAB:
//			ui_apply_but_CHARTAB(C, but, data);
//			break;
//#endif
		case UI.HOTKEYEVT:
//			ui_apply_but_BUT(C, but, data);
			break;
		case UI.LINK:
		case UI.INLINK:
//			ui_apply_but_LINK(C, but, data);
			break;
		default:
			break;
	}

	but.editstr= editstr;
	but.editval= editval;
	but.editvec= editvec;
//	but.editcoba= editcoba;
//	but.editcumap= editcumap;
}

///* ******************* copy and paste ********************  */
//
///* c = copy, v = paste */
//static void ui_but_copy_paste(bContext *C, uiBut *but, uiHandleButtonData *data, char mode)
//{
//	static ColorBand but_copypaste_coba = {0};
//	char buf[UI_MAX_DRAW_STR+1]= {0};
//	double val;
//
//	if(mode=='v' && but.lock)
//		return;
//
//	if(mode=='v') {
//		/* extract first line from clipboard in case of multi-line copies */
//		char *p, *pbuf= WM_clipboard_text_get(0);
//		p= pbuf;
//		if(p) {
//			int i = 0;
//			while (*p && *p!='\r' && *p!='\n' && i<UI_MAX_DRAW_STR) {
//				buf[i++]=*p;
//				p++;
//			}
//			buf[i]= 0;
//			MEM_freeN(pbuf);
//		}
//	}
//
//	/* numeric value */
//	if ELEM4(but.type, NUM, NUMABS, NUMSLI, HSVSLI) {
//
//		if(but.poin==NULL && but.rnapoin.data==NULL);
//		else if(mode=='c') {
//			sprintf(buf, "%f", ui_get_but_val(but));
//			WM_clipboard_text_set(buf, 0);
//		}
//		else {
//			if (sscanf(buf, " %lf ", &val) == 1) {
//				button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//				data.value= val;
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//			}
//		}
//	}
//
//	/* RGB triple */
//	else if(but.type==COL) {
//		float rgb[3];
//
//		if(but.poin==NULL && but.rnapoin.data==NULL);
//		else if(mode=='c') {
//
//			ui_get_but_vectorf(but, rgb);
//			sprintf(buf, "[%f, %f, %f]", rgb[0], rgb[1], rgb[2]);
//			WM_clipboard_text_set(buf, 0);
//
//		}
//		else {
//			if (sscanf(buf, "[%f, %f, %f]", &rgb[0], &rgb[1], &rgb[2]) == 3) {
//				button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//				VECCOPY(data.vec, rgb);
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//			}
//		}
//	}
//
//	/* text/string and ID data */
//	else if(ELEM(but.type, TEX, IDPOIN)) {
//		uiHandleButtonData *data= but.active;
//
//		if(but.poin==NULL && but.rnapoin.data==NULL);
//		else if(mode=='c') {
//			button_activate_state(C, but, BUTTON_STATE_TEXT_EDITING);
//			BLI_strncpy(buf, data.str, UI_MAX_DRAW_STR);
//			WM_clipboard_text_set(data.str, 0);
//			data.cancel= 1;
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//		else {
//			button_activate_state(C, but, BUTTON_STATE_TEXT_EDITING);
//			BLI_strncpy(data.str, buf, data.maxlen);
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//	}
//	/* colorband (not supported by system clipboard) */
//	else if(but.type==BUT_COLORBAND) {
//		if(mode=='c') {
//			if(but.poin)
//				return;
//
//			memcpy(&but_copypaste_coba, but.poin, sizeof(ColorBand));
//		}
//		else {
//			if(but_copypaste_coba.tot==0)
//				return;
//
//			if(!but.poin)
//				but.poin= MEM_callocN(sizeof(ColorBand), "colorband");
//
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//			memcpy(data.coba, &but_copypaste_coba, sizeof(ColorBand) );
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//	}
//}
//
///* ************* in-button text selection/editing ************* */
//
///* return 1 if char ch is special character, otherwise return 0 */
//static short test_special_char(char ch)
//{
//	switch(ch) {
//		case '\\':
//		case '/':
//		case '~':
//		case '!':
//		case '@':
//		case '#':
//		case '$':
//		case '%':
//		case '^':
//		case '&':
//		case '*':
//		case '(':
//		case ')':
//		case '+':
//		case '=':
//		case '{':
//		case '}':
//		case '[':
//		case ']':
//		case ':':
//		case ';':
//		case '\'':
//		case '\"':
//		case '<':
//		case '>':
//		case ',':
//		case '.':
//		case '?':
//		case '_':
//		case '-':
//		case ' ':
//			return 1;
//			break;
//		default:
//			break;
//	}
//	return 0;
//}
//
//static int ui_textedit_delete_selection(uiBut *but, uiHandleButtonData *data)
//{
//	char *str;
//	int x, changed;
//
//	str= data.str;
//	changed= (but.selsta != but.selend);
//
//	for(x=0; x< strlen(str); x++) {
//		if (but.selend + x <= strlen(str) ) {
//			str[but.selsta + x]= str[but.selend + x];
//		} else {
//			str[but.selsta + x]= '\0';
//			break;
//		}
//	}
//
//	but.pos = but.selend = but.selsta;
//
//	return changed;
//}

static void ui_textedit_set_cursor_pos(uiBut but, uiHandleButtonData data, short x)
{
//	uiStyle *style= U.uistyles.first;	// XXX pass on as arg
//	int startx= but.x1;
//	char *origstr;
//
//	uiStyleFontSet(&style.widget);
//
//	origstr= MEM_callocN(sizeof(char)*(data.maxlen+1), "ui_textedit origstr");
//
//	BLI_strncpy(origstr, but.drawstr, data.maxlen+1);
//	but.pos= strlen(origstr)-but.ofs;
//
//	/* XXX solve generic */
//	if(but.type==NUM || but.type==NUMSLI)
//		startx += (int)(0.5f*(but.y2 - but.y1));
//	else if(but.type==TEX)
//		startx += 5;
//
//	/* XXX does not take zoom level into account */
//	while((BLF_width(origstr+but.ofs) + startx) > x) {
//		if (but.pos <= 0) break;
//		but.pos--;
//		origstr[but.pos+but.ofs] = 0;
//	}
//
//	but.pos += but.ofs;
//	if(but.pos<0) but.pos= 0;
//
//	MEM_freeN(origstr);
}

static void ui_textedit_set_cursor_select(uiBut but, uiHandleButtonData data, short x)
{
//	if (x > data.selstartx) data.selextend = EXTEND_RIGHT;
//	else if (x < data.selstartx) data.selextend = EXTEND_LEFT;
//
//	ui_textedit_set_cursor_pos(but, data, x);
//
//	if (data.selextend == EXTEND_RIGHT) but.selend = but.pos;
//	if (data.selextend == EXTEND_LEFT) but.selsta = but.pos;
//
//	ui_check_but(but);
}

static int ui_textedit_type_ascii(uiBut but, uiHandleButtonData data, byte ascii)
{
//	char *str;
	int len, x, changed= 0;

//	str= data.str;
//	len= strlen(str);
//
//	if(len-(but.selend - but.selsta)+1 <= data.maxlen) {
//		/* type over the current selection */
//		if ((but.selend - but.selsta) > 0)
//			changed= ui_textedit_delete_selection(but, data);
//
//		len= strlen(str);
//		if(len < data.maxlen) {
//			for(x= data.maxlen; x>but.pos; x--)
//				str[x]= str[x-1];
//			str[but.pos]= ascii;
//			str[len+1]= '\0';
//
//			but.pos++;
//			changed= 1;
//		}
//	}

	return changed;
}

public static void ui_textedit_move(uiBut but, uiHandleButtonData data, int direction, int select, int jump)
{
//	char *str;
//	int len;
//
//	str= data.str;
//	len= strlen(str);
//
//	if(direction) { /* right*/
//		/* if there's a selection */
//		if ((but.selend - but.selsta) > 0) {
//			/* extend the selection based on the first direction taken */
//			if(select) {
//				if (!data.selextend) {
//					data.selextend = EXTEND_RIGHT;
//				}
//				if (data.selextend == EXTEND_RIGHT) {
//					but.selend++;
//					if (but.selend > len) but.selend = len;
//				} else if (data.selextend == EXTEND_LEFT) {
//					but.selsta++;
//					/* if the selection start has gone past the end,
//					* flip them so they're in sync again */
//					if (but.selsta == but.selend) {
//						but.pos = but.selsta;
//						data.selextend = EXTEND_RIGHT;
//					}
//				}
//			} else {
//				but.selsta = but.pos = but.selend;
//				data.selextend = 0;
//			}
//		} else {
//			if(select) {
//				/* make a selection, starting from the cursor position */
//				but.selsta = but.pos;
//
//				but.pos++;
//				if(but.pos>strlen(str)) but.pos= strlen(str);
//
//				but.selend = but.pos;
//			} else if(jump) {
//				/* jump betweenn special characters (/,\,_,-, etc.),
//				 * look at function test_special_char() for complete
//				 * list of special character, ctr . */
//				while(but.pos < len) {
//					but.pos++;
//					if(test_special_char(str[but.pos])) break;
//				}
//			} else {
//				but.pos++;
//				if(but.pos>strlen(str)) but.pos= strlen(str);
//			}
//		}
//	}
//	else { /* left */
//		/* if there's a selection */
//		if ((but.selend - but.selsta) > 0) {
//			/* extend the selection based on the first direction taken */
//			if(select) {
//				if (!data.selextend) {
//					data.selextend = EXTEND_LEFT;
//				}
//				if (data.selextend == EXTEND_LEFT) {
//					but.selsta--;
//					if (but.selsta < 0) but.selsta = 0;
//				} else if (data.selextend == EXTEND_RIGHT) {
//					but.selend--;
//					/* if the selection start has gone past the end,
//					* flip them so they're in sync again */
//					if (but.selsta == but.selend) {
//						but.pos = but.selsta;
//						data.selextend = EXTEND_LEFT;
//					}
//				}
//			} else {
//				but.pos = but.selend = but.selsta;
//				data.selextend = 0;
//			}
//		} else {
//			if(select) {
//				/* make a selection, starting from the cursor position */
//				but.selend = but.pos;
//
//				but.pos--;
//				if(but.pos<0) but.pos= 0;
//
//				but.selsta = but.pos;
//			} else if(jump) {
//				/* jump betweenn special characters (/,\,_,-, etc.),
//				 * look at function test_special_char() for complete
//				 * list of special character, ctr . */
//				while(but.pos > 0){
//					but.pos--;
//					if(test_special_char(str[but.pos])) break;
//				}
//			} else {
//				if(but.pos>0) but.pos--;
//			}
//		}
//	}
}

public static void ui_textedit_move_end(uiBut but, uiHandleButtonData data, int direction, int select)
{
//	char *str;
//
//	str= data.str;
//
//	if(direction) { /* right */
//		if(select) {
//			but.selsta = but.pos;
//			but.selend = strlen(str);
//			data.selextend = EXTEND_RIGHT;
//		} else {
//			but.selsta = but.selend = but.pos= strlen(str);
//		}
//	}
//	else { /* left */
//		if(select) {
//			but.selend = but.pos;
//			but.selsta = 0;
//			data.selextend = EXTEND_LEFT;
//		} else {
//			but.selsta = but.selend = but.pos= 0;
//		}
//	}
}

static int ui_textedit_delete(uiBut but, uiHandleButtonData data, int direction, int all)
{
//	char *str;
	int len, x, changed= 0;

//	str= data.str;
//	len= strlen(str);
//
//	if(all) {
//		if(len) changed=1;
//		str[0]= 0;
//		but.pos= 0;
//	}
//	else if(direction) { /* delete */
//		if ((but.selend - but.selsta) > 0) {
//			changed= ui_textedit_delete_selection(but, data);
//		}
//		else if(but.pos>=0 && but.pos<len) {
//			for(x=but.pos; x<len; x++)
//				str[x]= str[x+1];
//			str[len-1]='\0';
//			changed= 1;
//		}
//	}
//	else { /* backspace */
//		if(len!=0) {
//			if ((but.selend - but.selsta) > 0) {
//				changed= ui_textedit_delete_selection(but, data);
//			}
//			else if(but.pos>0) {
//				for(x=but.pos; x<len; x++)
//					str[x-1]= str[x];
//				str[len-1]='\0';
//
//				but.pos--;
//				changed= 1;
//			}
//		}
//	}

	return changed;
}

static int ui_textedit_autocomplete(bContext C, uiBut but, uiHandleButtonData data)
{
//	char *str;
	int changed= 1;

//	str= data.str;
//
//	if(data.searchbox)
//		ui_searchbox_autocomplete(C, data.searchbox, but, data.str);
//	else
//		but.autocomplete_func(C, str, but.autofunc_arg);
//
//	but.pos= strlen(str);
//	but.selsta= but.selend= but.pos;

	return changed;
}

static int ui_textedit_copypaste(uiBut but, uiHandleButtonData data, int paste, int copy, int cut)
{
//	char buf[UI_MAX_DRAW_STR]={0};
//	char *str, *p, *pbuf;
	int len, x, y, i, changed= 0;

//	str= data.str;
//	len= strlen(str);
//
//	/* paste */
//	if (paste) {
//		/* extract the first line from the clipboard */
//		p = pbuf= WM_clipboard_text_get(0);
//
//		if(p && p[0]) {
//			i= 0;
//			while (*p && *p!='\r' && *p!='\n' && i<UI_MAX_DRAW_STR-1) {
//				buf[i++]=*p;
//				p++;
//			}
//			buf[i]= 0;
//
//			/* paste over the current selection */
//			if ((but.selend - but.selsta) > 0)
//				ui_textedit_delete_selection(but, data);
//
//			for (y=0; y<strlen(buf); y++)
//			{
//				/* add contents of buffer */
//				if(len < data.maxlen) {
//					for(x= data.maxlen; x>but.pos; x--)
//						str[x]= str[x-1];
//					str[but.pos]= buf[y];
//					but.pos++;
//					len++;
//					str[len]= '\0';
//				}
//			}
//
//			changed= 1;
//		}
//
//		if(pbuf)
//			MEM_freeN(pbuf);
//	}
//	/* cut & copy */
//	else if (copy || cut) {
//		/* copy the contents to the copypaste buffer */
//		for(x= but.selsta; x <= but.selend; x++) {
//			if (x==but.selend)
//				buf[x] = '\0';
//			else
//				buf[(x - but.selsta)] = str[x];
//		}
//
//		WM_clipboard_text_set(buf, 0);
//
//		/* for cut only, delete the selection afterwards */
//		if(cut)
//			if((but.selend - but.selsta) > 0)
//				changed= ui_textedit_delete_selection(but, data);
//	}

	return changed;
}

//static void ui_textedit_begin(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	if(data.str) {
//		MEM_freeN(data.str);
//		data.str= NULL;
//	}
//
//	/* retrieve string */
//	data.maxlen= ui_get_but_string_max_length(but);
//	data.str= MEM_callocN(sizeof(char)*(data.maxlen+1), "textedit str");
//	ui_get_but_string(but, data.str, data.maxlen+1);
//
//	data.origstr= BLI_strdup(data.str);
//	data.selextend= 0;
//	data.selstartx= 0;
//
//	/* set cursor pos to the end of the text */
//	but.editstr= data.str;
//	but.pos= strlen(data.str);
//	but.selsta= 0;
//	but.selend= strlen(data.str);
//
//	/* optional searchbox */
//	if(but.type==SEARCH_MENU) {
//		data.searchbox= ui_searchbox_create(C, data.region, but);
//		ui_searchbox_update(C, data.searchbox, but, 1); /* 1= reset */
//	}
//
//	ui_check_but(but);
//}
//
//static void ui_textedit_end(bContext *C, uiBut *but, uiHandleButtonData *data)
//{
//	if(but) {
//		if(data.searchbox) {
//			if(data.cancel==0)
//				ui_searchbox_apply(but, data.searchbox);
//
//			ui_searchbox_free(C, data.searchbox);
//			data.searchbox= NULL;
//		}
//
//		but.editstr= NULL;
//		but.pos= -1;
//	}
//}

static void ui_textedit_next_but(uiBlock block, uiBut actbut, uiHandleButtonData data)
{
	uiBut but;

	/* label and roundbox can overlap real buttons (backdrops...) */
	if(actbut.type == UI.LABEL || actbut.type == UI.SEPR || actbut.type == UI.ROUNDBOX || actbut.type == UI.LISTBOX)
		return;

	for(but= actbut.next; but!=null; but= but.next) {
		if(but.type == UI.TEX || but.type == UI.NUM || but.type == UI.NUMABS || but.type == UI.NUMSLI || but.type == UI.HSVSLI || but.type == UI.IDPOIN || but.type == UI.SEARCH_MENU) {
			data.postbut= but;
			data.posttype= uiButtonActivateType.BUTTON_ACTIVATE_TEXT_EDITING;
			return;
		}
	}
	for(but= block.buttons.first; but!=actbut; but= but.next) {
		if(but.type == UI.TEX || but.type == UI.NUM || but.type == UI.NUMABS || but.type == UI.NUMSLI || but.type == UI.HSVSLI || but.type == UI.IDPOIN || but.type == UI.SEARCH_MENU) {
			data.postbut= but;
			data.posttype= uiButtonActivateType.BUTTON_ACTIVATE_TEXT_EDITING;
			return;
		}
	}
}

static void ui_textedit_prev_but(uiBlock block, uiBut actbut, uiHandleButtonData data)
{
	uiBut but;

	/* label and roundbox can overlap real buttons (backdrops...) */
	if(actbut.type == UI.LABEL || actbut.type == UI.SEPR || actbut.type == UI.ROUNDBOX || actbut.type == UI.LISTBOX)
		return;

	for(but= actbut.prev; but!=null; but= but.prev) {
		if(but.type == UI.TEX || but.type == UI.NUM || but.type == UI.NUMABS || but.type == UI.NUMSLI || but.type == UI.HSVSLI || but.type == UI.IDPOIN || but.type == UI.SEARCH_MENU) {
			data.postbut= but;
			data.posttype= uiButtonActivateType.BUTTON_ACTIVATE_TEXT_EDITING;
			return;
		}
	}
	for(but= block.buttons.last; but!=actbut; but= but.prev) {
		if(but.type == UI.TEX || but.type == UI.NUM || but.type == UI.NUMABS || but.type == UI.NUMSLI || but.type == UI.HSVSLI || but.type == UI.IDPOIN || but.type == UI.SEARCH_MENU) {
			data.postbut= but;
			data.posttype= uiButtonActivateType.BUTTON_ACTIVATE_TEXT_EDITING;
			return;
		}
	}
}


static void ui_do_but_textedit(bContext C, uiBlock block, uiBut but, uiHandleButtonData data, wmEvent event)
{
	int[] mx={0}, my={0};
	int changed= 0, inbox=0, update= 0, retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	switch(event.type) {
		case WmEventTypes.WHEELUPMOUSE:
		case WmEventTypes.WHEELDOWNMOUSE:
		case WmEventTypes.MOUSEMOVE:
			if(data.searchbox!=null)
				UIRegions.ui_searchbox_event(C, data.searchbox, but, event);

			break;
		case WmEventTypes.RIGHTMOUSE:
		case WmEventTypes.ESCKEY:
			data.cancel= 1;
			data.escapecancel= 1;
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			retval= WmTypes.WM_UI_HANDLER_BREAK;
			break;
		case WmEventTypes.LEFTMOUSE: {

			/* exit on LMB only on RELEASE for searchbox, to mimic other popups, and allow multiple menu levels */
			if(data.searchbox!=null)
				inbox= UIRegions.ui_searchbox_inside(data.searchbox, event.x, event.y);

			if(event.val==WmTypes.KM_PRESS) {
				mx[0]= event.x;
				my[0]= event.y;
				UI.ui_window_to_block(data.region, block, mx, my);

				if ((but.y1 <= my[0]) && (my[0] <= but.y2) && (but.x1 <= mx[0]) && (mx[0] <= but.x2)) {
					ui_textedit_set_cursor_pos(but, data, (short)mx[0]);
					but.selsta = but.selend = but.pos;
					data.selstartx= mx[0];

					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_SELECTING);
					retval= WmTypes.WM_UI_HANDLER_BREAK;
				}
				else if(inbox==0) {
					/* if searchbox, click outside will cancel */
					if(data.searchbox!=null)
						data.cancel= data.escapecancel= 1;
					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
					retval= WmTypes.WM_UI_HANDLER_BREAK;
				}
			}
			else if(inbox!=0) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
			}
			break;
		}
	}

	if(event.val==WmTypes.KM_PRESS) {
		switch (event.type) {
			case WmEventTypes.VKEY:
			case WmEventTypes.XKEY:
			case WmEventTypes.CKEY:
				if(event.ctrl!=0 || event.oskey!=0) {
					if(event.type == WmEventTypes.VKEY)
						changed= ui_textedit_copypaste(but, data, 1, 0, 0);
					else if(event.type == WmEventTypes.CKEY)
						changed= ui_textedit_copypaste(but, data, 0, 1, 0);
					else if(event.type == WmEventTypes.XKEY)
						changed= ui_textedit_copypaste(but, data, 0, 0, 1);

					retval= WmTypes.WM_UI_HANDLER_BREAK;
				}
				break;
			case WmEventTypes.RIGHTARROWKEY:
				ui_textedit_move(but, data, 1, event.shift, event.ctrl);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
			case WmEventTypes.LEFTARROWKEY:
				ui_textedit_move(but, data, 0, event.shift, event.ctrl);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
			case WmEventTypes.DOWNARROWKEY:
				if(data.searchbox!=null) {
					UIRegions.ui_searchbox_event(C, data.searchbox, but, event);
					break;
				}
				/* pass on purposedly */
			case WmEventTypes.ENDKEY:
				ui_textedit_move_end(but, data, 1, event.shift);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
			case WmEventTypes.UPARROWKEY:
				if(data.searchbox!=null) {
					UIRegions.ui_searchbox_event(C, data.searchbox, but, event);
					break;
				}
				/* pass on purposedly */
			case WmEventTypes.HOMEKEY:
				ui_textedit_move_end(but, data, 0, event.shift);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
			case WmEventTypes.PADENTER:
			case WmEventTypes.RETKEY:
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
			case WmEventTypes.DELKEY:
				changed= ui_textedit_delete(but, data, 1, 0);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;

			case WmEventTypes.BACKSPACEKEY:
				changed= ui_textedit_delete(but, data, 0, event.shift);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;

			case WmEventTypes.TABKEY:
				/* there is a key conflict here, we can't tab with autocomplete */
				if(but.autocomplete_func!=null || data.searchbox!=null) {
					changed= ui_textedit_autocomplete(C, but, data);
					update= 1; /* do live update for tab key */
					retval= WmTypes.WM_UI_HANDLER_BREAK;
				}
				/* the hotkey here is not well defined, was G.qual so we check all */
				else if(event.shift!=0 || event.ctrl!=0 || event.alt!=0 || event.oskey!=0) {
					ui_textedit_prev_but(block, but, data);
					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				}
				else {
					ui_textedit_next_but(block, but, data);
					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				}
				retval= WmTypes.WM_UI_HANDLER_BREAK;
				break;
		}

		if(event.ascii!=0 && (retval == WmTypes.WM_UI_HANDLER_CONTINUE)) {
			changed= ui_textedit_type_ascii(but, data, event.ascii);
			retval= WmTypes.WM_UI_HANDLER_BREAK;
		}
	}

	if(changed!=0) {
		/* never update while typing for now */
		if(update!=0 && data.interactive!=0) ui_apply_button(C, block, but, data, 1);
		else UI.ui_check_but(but);

		if(data.searchbox!=null)
			UIRegions.ui_searchbox_update(C, data.searchbox, but, 1); /* 1 = reset */
	}

	if(changed!=0 || (retval == WmTypes.WM_UI_HANDLER_BREAK))
		Area.ED_region_tag_redraw(data.region);
}

static void ui_do_but_textedit_select(bContext C, uiBlock block, uiBut but, uiHandleButtonData data, wmEvent event)
{
	int[] mx={0}, my={0};
	int retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	switch(event.type) {
		case WmEventTypes.MOUSEMOVE: {
			mx[0]= event.x;
			my[0]= event.y;
			UI.ui_window_to_block(data.region, block, mx, my);

			ui_textedit_set_cursor_select(but, data, (short)mx[0]);
			retval= WmTypes.WM_UI_HANDLER_BREAK;
			break;
		}
		case WmEventTypes.LEFTMOUSE:
			if(event.val == WmTypes.KM_RELEASE)
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_EDITING);
			retval= WmTypes.WM_UI_HANDLER_BREAK;
			break;
	}

	if(retval == WmTypes.WM_UI_HANDLER_BREAK) {
		UI.ui_check_but(but);
		Area.ED_region_tag_redraw(data.region);
	}
}

/* ************* number editing for various types ************* */

//static void ui_numedit_begin(uiBut *but, uiHandleButtonData *data)
//{
//	float softrange, softmin, softmax;
//
//	if(but.type == BUT_CURVE) {
//		data.cumap= (CurveMapping*)but.poin;
//		but.editcumap= data.coba;
//	}
//	else if(but.type == BUT_COLORBAND) {
//		data.coba= (ColorBand*)but.poin;
//		but.editcoba= data.coba;
//	}
//	else if(ELEM3(but.type, BUT_NORMAL, HSVCUBE, HSVCIRCLE)) {
//		ui_get_but_vectorf(but, data.origvec);
//		VECCOPY(data.vec, data.origvec);
//		but.editvec= data.vec;
//	}
//	else {
//		data.startvalue= ui_get_but_val(but);
//		data.origvalue= data.startvalue;
//		data.value= data.origvalue;
//		but.editval= &data.value;
//
//		softmin= but.softmin;
//		softmax= but.softmax;
//		softrange= softmax - softmin;
//
//		data.dragfstart= (softrange == 0.0)? 0.0: (data.value - softmin)/softrange;
//		data.dragf= data.dragfstart;
//	}
//
//	data.dragchange= 0;
//	data.draglock= 1;
//}
//
//static void ui_numedit_end(uiBut *but, uiHandleButtonData *data)
//{
//	but.editval= NULL;
//	but.editvec= NULL;
//	but.editcoba= NULL;
//	but.editcumap= NULL;
//
//	data.dragstartx= 0;
//	data.draglastx= 0;
//	data.dragchange= 0;
//	data.dragcbd= NULL;
//	data.dragsel= 0;
//}

static void ui_numedit_apply(bContext C, uiBlock block, uiBut but, uiHandleButtonData data)
{
	if(data.interactive!=0) ui_apply_button(C, block, but, data, 1);
	else UI.ui_check_but(but);

	Area.ED_region_tag_redraw(data.region);
}

/* ****************** menu opening for various types **************** */

//static void ui_blockopen_begin(bContext C, uiBut but, final uiHandleButtonData data)
//{
//	uiBlockCreateFunc func= null;
//	uiBlockHandleCreateFunc handlefunc= null;
//	uiMenuCreateFunc menufunc= null;
//	Object arg= null;
//
//	switch(but.type) {
//		case UI.BLOCK:
//		case UI.PULLDOWN:
//			if(but.menu_create_func!=null) {
//				menufunc= but.menu_create_func;
//				arg= but.poin;
//			}
//			else {
//				func= but.block_create_func;
//				arg= but.poin!=null?but.poin:but.func_argN;
//			}
//			break;
//		case UI.MENU:
//			if(but.menu_create_func!=null) {
//				menufunc= but.menu_create_func;
//				arg= but.poin;
//			}
//			else {
//				data.origvalue= UI.ui_get_but_val(but);
//				data.value= data.origvalue;
//                                Pointer<Double> data_value = new Pointer<Double>() {
//                                    public Double get() { return data.value; }
//                                    public void set(Double obj) { data.value = obj; }
//                                };
//				but.editval= data_value;
//
//				handlefunc= UIRegions.ui_block_func_MENU;
//				arg= but;
//			}
//			break;
//		case UI.ICONROW:
////			handlefunc= ui_block_func_ICONROW;
////			arg= but;
//			break;
//		case UI.ICONTEXTROW:
//			handlefunc= UIRegions.ui_block_func_ICONTEXTROW;
//			arg= but;
//			break;
//		case UI.COL:
////			ui_get_but_vectorf(but, data.origvec);
////			VECCOPY(data.vec, data.origvec);
////			but.editvec= data.vec;
////
////			handlefunc= ui_block_func_COL;
////			arg= but;
//			break;
//	}
//
//	if(func!=null || handlefunc!=null) {
//		data.menu= UIRegions.ui_popup_block_create((GL2)GLU.getCurrentGL(), C, data.region, but, func, handlefunc, arg);
//		if(but.block.handle!=null)
//			data.menu.popup= but.block.handle.popup;
//	}
//	else if(menufunc!=null) {
//		data.menu= UIRegions.ui_popup_menu_create((GL2)GLU.getCurrentGL(), C, data.region, but, menufunc, arg);
//		if(but.block.handle!=null)
//			data.menu.popup= but.block.handle.popup;
//	}
//
//	/* this makes adjacent blocks auto open from now on */
//	//if(but.block.auto_open==0) but.block.auto_open= 1;
//}

static void ui_blockopen_begin(bContext C, uiBut but, final uiHandleButtonData data)
{
	uiBlockCreateFunc func= null;
	uiBlockHandleCreateFunc handlefunc= null;
	uiMenuCreateFunc menufunc= null;
	byte[] menustr= null;
	Object arg= null;

	switch(but.type) {
		case UI.BLOCK:
		case UI.PULLDOWN:
			if(but.menu_create_func!=null) {
				menufunc= but.menu_create_func;
				arg= but.poin;
			}
			else {
				func= but.block_create_func;
				arg= but.poin!=null?but.poin:but.func_argN;
			}
			break;
		case UI.MENU:
			if(but.menu_create_func!=null) {
				menufunc= but.menu_create_func;
				arg= but.poin;
			}
			else {
				data.origvalue= UI.ui_get_but_val(but);
				data.value= data.origvalue;
                Pointer<Double> data_value = new Pointer<Double>() {
                    public Double get() { return data.value; }
                    public void set(Double obj) { data.value = obj; }
                };
				but.editval= data_value;

//				handlefunc= UIRegions.ui_block_func_MENU;
//				arg= but;
				menustr= but.str;
			}
			break;
		case UI.ICONROW:
//			menufunc= ui_block_func_ICONROW;
//			arg= but;
			break;
		case UI.ICONTEXTROW:
			menufunc= UIRegions.ui_block_func_ICONTEXTROW;
			arg= but;
			break;
		case UI.COL:
//			ui_get_but_vectorf(but, data.origvec);
//			VECCOPY(data.vec, data.origvec);
//			but.editvec= data.vec;
//
//			handlefunc= ui_block_func_COL;
//			arg= but;
			break;
	}

	if(func!=null || handlefunc!=null) {
		data.menu= UIRegions.ui_popup_block_create((GL2)GLU.getCurrentGL(), C, data.region, but, func, handlefunc, arg);
		if(but.block.handle!=null)
			data.menu.popup= but.block.handle.popup;
	}
	else if(menufunc!=null || menustr!=null) {
		data.menu= UIRegions.ui_popup_menu_create((GL2)GLU.getCurrentGL(), C, data.region, but, menufunc, arg, menustr);
		if(but.block.handle!=null)
			data.menu.popup= but.block.handle.popup;
	}

	/* this makes adjacent blocks auto open from now on */
	//if(but.block.auto_open==0) but.block.auto_open= 1;
}

static void ui_blockopen_end(bContext C, uiBut but, uiHandleButtonData data)
{
	if(but!=null) {
		but.editval= null;
		but.editvec= null;

		but.block.auto_open_last= Time.PIL_check_seconds_timer();
	}

	if(data.menu!=null) {
		UIRegions.ui_popup_block_free(C, data.menu);
		data.menu= null;
	}
}

/* ***************** events for different button types *************** */

static int ui_do_but_BUT(bContext C, uiBut but, uiHandleButtonData data, wmEvent event)
{
        System.out.println("ui_do_but_BUT");
	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		if(event.type == WmEventTypes.LEFTMOUSE && event.val==WmTypes.KM_PRESS) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_WAIT_RELEASE);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
		else if(event.type == WmEventTypes.LEFTMOUSE && but.block.handle!=null) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
		else if((event.type==WmEventTypes.PADENTER || event.type==WmEventTypes.RETKEY) && event.val==WmTypes.KM_PRESS) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_WAIT_FLASH);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_WAIT_RELEASE) {
		if(event.type == WmEventTypes.LEFTMOUSE && event.val!=WmTypes.KM_PRESS) {
			if((but.flag & UI.UI_SELECT)==0)
				data.cancel= 1;
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
	}

	return WmTypes.WM_UI_HANDLER_CONTINUE;
}

//static int ui_do_but_HOTKEYEVT(bContext *C, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(ELEM3(event.type, LEFTMOUSE, PADENTER, RETKEY) && event.val==KM_PRESS) {
//			but.drawstr[0]= 0;
//			button_activate_state(C, but, BUTTON_STATE_WAIT_KEY_EVENT);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_WAIT_KEY_EVENT) {
//		short *sp= (short *)but.func_arg3;
//
//		if(event.type == MOUSEMOVE)
//			return WM_UI_HANDLER_CONTINUE;
//
//		if(ELEM(event.type, ESCKEY, LEFTMOUSE)) {
//			/* data.cancel doesnt work, this button opens immediate */
//			ui_set_but_val(but, 0);
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//			return WM_UI_HANDLER_BREAK;
//		}
//
//		/* always set */
//		*sp= 0;
//		if(event.shift)
//			*sp |= KM_SHIFT;
//		if(event.alt)
//			*sp |= KM_ALT;
//		if(event.ctrl)
//			*sp |= KM_CTRL;
//		if(event.oskey)
//			*sp |= KM_OSKEY;
//
//		ui_check_but(but);
//		ED_region_tag_redraw(data.region);
//
//		if(event.val==KM_PRESS) {
//			if(ISHOTKEY(event.type)) {
//
//				if(WM_key_event_string(event.type)[0])
//					ui_set_but_val(but, event.type);
//				else
//					data.cancel= 1;
//
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//				return WM_UI_HANDLER_BREAK;
//			}
//		}
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//
//static int ui_do_but_KEYEVT(bContext *C, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(ELEM3(event.type, LEFTMOUSE, PADENTER, RETKEY) && event.val==KM_PRESS) {
//			short event= (short)ui_get_but_val(but);
//			/* hardcoded prevention from editing or assigning ESC */
//			if(event!=ESCKEY)
//				button_activate_state(C, but, BUTTON_STATE_WAIT_KEY_EVENT);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_WAIT_KEY_EVENT) {
//		if(event.type == MOUSEMOVE)
//			return WM_UI_HANDLER_CONTINUE;
//
//		if(event.val==KM_PRESS) {
//			if(event.type!=ESCKEY && WM_key_event_string(event.type)[0])
//				ui_set_but_val(but, event.type);
//			else
//				data.cancel= 1;
//
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//static int ui_do_but_TEX(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(ELEM4(event.type, LEFTMOUSE, PADENTER, RETKEY, EVT_BUT_OPEN) && event.val==KM_PRESS) {
//			button_activate_state(C, but, BUTTON_STATE_TEXT_EDITING);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_TEXT_EDITING) {
//		ui_do_but_textedit(C, block, but, data, event);
//		return WM_UI_HANDLER_BREAK;
//	}
//	else if(data.state == BUTTON_STATE_TEXT_SELECTING) {
//		ui_do_but_textedit_select(C, block, but, data, event);
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}

static int ui_do_but_TOG(bContext C, uiBut but, uiHandleButtonData data, wmEvent event)
{
    System.out.println("ui_do_but_TOG");
	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		if((event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.PADENTER || event.type==WmEventTypes.RETKEY) && event.val==WmTypes.KM_PRESS) {
			data.togdual= event.ctrl;
			data.togonly= event.shift!=0?0:1;
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
	}
	return WmTypes.WM_UI_HANDLER_CONTINUE;
}

static int ui_do_but_EXIT(bContext C, uiBut but, uiHandleButtonData data, wmEvent event)
{
//        System.out.println("ui_do_but_EXIT");
	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		if((event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.PADENTER || event.type==WmEventTypes.RETKEY) && event.val==WmTypes.KM_PRESS) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
	}

	return WmTypes.WM_UI_HANDLER_CONTINUE;
}

static int ui_numedit_but_NUM(uiBut but, uiHandleButtonData data, float fac, int snap, int mx)
{
	float deler, tempf, softmin, softmax, softrange;
	int lvalue, temp, changed= 0;

	if(mx == data.draglastx)
		return changed;

	/* drag-lock - prevent unwanted scroll adjustments */
	/* change value (now 3) to adjust threshold in pixels */
	if(data.draglock!=0) {
		if(Math.abs(mx-data.dragstartx) <= 3)
			return changed;

		data.draglock= 0;
		data.dragstartx= mx;  /* ignore mouse movement within drag-lock */
	}

	softmin= but.softmin;
	softmax= but.softmax;
	softrange= softmax - softmin;

	deler= 500;
	if(!UI.ui_is_but_float(but)) {
		if((softrange)<100) deler= 200.0f;
		if((softrange)<25) deler= 50.0f;
	}
	deler /= fac;

	if(UI.ui_is_but_float(but) && softrange > 11) {
		/* non linear change in mouse input- good for high precicsion */
		data.dragf+= (((float)(mx-data.draglastx))/deler) * (Math.abs(data.dragstartx-mx)*0.002);
	} else if (!UI.ui_is_but_float(but) && softrange > 129) { /* only scale large int buttons */
		/* non linear change in mouse input- good for high precicsionm ints need less fine tuning */
		data.dragf+= (((float)(mx-data.draglastx))/deler) * (Math.abs(data.dragstartx-mx)*0.004);
	} else {
		/*no scaling */
		data.dragf+= ((float)(mx-data.draglastx))/deler ;
	}

	if(data.dragf>1.0) data.dragf= 1.0f;
	if(data.dragf<0.0) data.dragf= 0.0f;
	data.draglastx= mx;
	tempf= (softmin + data.dragf*softrange);

	if(!UI.ui_is_but_float(but)) {
		temp= (int)Math.floor(tempf+.5);

		if(tempf==softmin || tempf==softmax);
		else if(snap!=0) {
			if(snap == 2) temp= 100*(temp/100);
			else temp= 10*(temp/10);
		}

		temp = (int)Arithb.CLAMP(temp, softmin, softmax);
		lvalue= (int)data.value;

		if(temp != lvalue) {
			data.dragchange= 1;
			data.value= (double)temp;
			changed= 1;
		}
	}
	else {
		temp= 0;

		if(snap!=0) {
			if(snap == 2) {
				if(tempf==softmin || tempf==softmax);
				else if(softrange < 2.10) tempf= (float)(0.01*Math.floor(100.0*tempf));
				else if(softrange < 21.0) tempf= (float)(0.1*Math.floor(10.0*tempf));
				else tempf= (float)Math.floor(tempf);
			}
			else {
				if(tempf==softmin || tempf==softmax);
				else if(softrange < 2.10) tempf= (float)(0.1*Math.floor(10*tempf));
				else if(softrange < 21.0) tempf= (float)Math.floor(tempf);
				else tempf= (float)(10.0*Math.floor(tempf/10.0));
			}
		}

		tempf = Arithb.CLAMP(tempf, softmin, softmax);

		if(tempf != data.value) {
			data.dragchange= 1;
			data.value= tempf;
			changed= 1;
		}
	}

	return changed;
}

static int ui_do_but_NUM(bContext C, uiBlock block, uiBut but, uiHandleButtonData data, wmEvent event)
{
//	System.out.println("ui_do_but_NUM");
	int[] mx={0}, my={0};
	boolean click= false;
	int retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	mx[0]= event.x;
	my[0]= event.y;
	UI.ui_window_to_block(data.region, block, mx, my);

	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		/* XXX hardcoded keymap check.... */
		if(event.type == WmEventTypes.WHEELDOWNMOUSE && event.alt!=0) {
			mx[0]= (int)but.x1;
			click= true;
		}
		else if(event.type == WmEventTypes.WHEELUPMOUSE && event.alt!=0) {
			mx[0]= (int)but.x2;
			click= true;
		}
		else if(event.val==WmTypes.KM_PRESS) {
			if((event.type == WmEventTypes.LEFTMOUSE || event.type == WmEventTypes.PADENTER || event.type == WmEventTypes.RETKEY) && event.ctrl!=0) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_EDITING);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
			}
			else if(event.type == WmEventTypes.LEFTMOUSE) {
				data.dragstartx= mx[0];
				data.draglastx= mx[0];
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_NUM_EDITING);
				retval= WmTypes.WM_UI_HANDLER_BREAK;
			}
			else if((event.type == WmEventTypes.PADENTER || event.type == WmEventTypes.RETKEY) && event.val==WmTypes.KM_PRESS)
				click= true;
		}

	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_NUM_EDITING) {
		if(event.type == WmEventTypes.ESCKEY) {
			data.cancel= 1;
			data.escapecancel= 1;
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
		}
		else if(event.type == WmEventTypes.LEFTMOUSE && event.val!=WmTypes.KM_PRESS) {
			if(data.dragchange!=0)
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			else
				click= true;
		}
		else if(event.type == WmEventTypes.MOUSEMOVE) {
			float fac;
			int snap;

			fac= 1.0f;
			if(event.shift!=0) fac /= 10.0f;
			if(event.alt!=0) fac /= 20.0f;

//			if(event.custom == EVT_DATA_TABLET) {
//				wmTabletData *wmtab= event.customdata;
//
//				/* de-sensitise based on tablet pressure */
//				if (ELEM(wmtab.Active, DEV_STYLUS, DEV_ERASER))
//				 	fac *= wmtab.Pressure;
//			}

			snap= (event.ctrl!=0)? (event.shift!=0)? 2: 1: 0;

			if(ui_numedit_but_NUM(but, data, fac, snap, mx[0])!=0)
				ui_numedit_apply(C, block, but, data);
		}
		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_TEXT_EDITING) {
		ui_do_but_textedit(C, block, but, data, event);
		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_TEXT_SELECTING) {
		ui_do_but_textedit_select(C, block, but, data, event);
		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}

	if(click) {
//		System.out.println("click");
		/* we can click on the side arrows to increment/decrement,
		 * or click inside to edit the value directly */
		float tempf, softmin, softmax;
		int temp;

		softmin= but.softmin;
		softmax= but.softmax;

		if(!UI.ui_is_but_float(but)) {
//			System.out.println("not float");
			if(mx[0] < (but.x1 + (but.x2 - but.x1)/3 - 3)) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_NUM_EDITING);

				temp= (int)data.value - 1;
//				System.out.println("click NUM -: "+data.value);
				if(temp>=softmin && temp<=softmax)
					data.value= (double)temp;
				else
					data.cancel= 1;

				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			}
			else if(mx[0] > (but.x1 + (2*(but.x2 - but.x1)/3) + 3)) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_NUM_EDITING);

				temp= (int)data.value + 1;
				if(temp>=softmin && temp<=softmax) {
					data.value= (double)temp;
//					System.out.println("click NUM +: ("+data+") "+data.value+", min:"+softmin+", max:"+softmax);
				}
				else
					data.cancel= 1;

				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			}
			else
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_EDITING);
		}
		else {
//			System.out.println("float");
			if(mx[0] < (but.x1 + (but.x2 - but.x1)/3 - 3)) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_NUM_EDITING);

				tempf= (float)(data.value - 0.01*but.a1);
				if (tempf < softmin) tempf = softmin;
				data.value= tempf;

				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			}
			else if(mx[0] > but.x1 + (2*((but.x2 - but.x1)/3) + 3)) {
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_NUM_EDITING);

				tempf= (float)(data.value + 0.01*but.a1);
				if (tempf > softmax) tempf = softmax;
				data.value= tempf;

				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			}
			else
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_EDITING);
		}

		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}

	return retval;
}

//static int ui_numedit_but_SLI(uiBut *but, uiHandleButtonData *data, int shift, int ctrl, int mx)
//{
//	float deler, f, tempf, softmin, softmax, softrange;
//	int temp, lvalue, changed= 0;
//
//	softmin= but.softmin;
//	softmax= but.softmax;
//	softrange= softmax - softmin;
//
//	if(but.type==NUMSLI) deler= ((but.x2-but.x1) - 5.0*but.aspect);
//	else if(but.type==HSVSLI) deler= ((but.x2-but.x1)/2 - 5.0*but.aspect);
//	else if(but.type==SCROLL) {
//		int horizontal= (but.x2 - but.x1 > but.y2 - but.y1);
//		float size= (horizontal)? (but.x2-but.x1): -(but.y2-but.y1);
//		deler= size*(but.softmax - but.softmin)/(but.softmax - but.softmin + but.a1);
//	}
//	else deler= (but.x2-but.x1- 5.0*but.aspect);
//
//	f= (float)(mx-data.dragstartx)/deler + data.dragfstart;
//
//	if(shift)
//		f= (f-data.dragfstart)/10.0 + data.dragfstart;
//
//	CLAMP(f, 0.0, 1.0);
//	tempf= softmin + f*softrange;
//	temp= floor(tempf+.5);
//
//	if(ctrl) {
//		if(tempf==softmin || tempf==softmax);
//		else if(ui_is_but_float(but)) {
//
//			if(shift) {
//				if(tempf==softmin || tempf==softmax);
//				else if(softmax-softmin < 2.10) tempf= 0.01*floor(100.0*tempf);
//				else if(softmax-softmin < 21.0) tempf= 0.1*floor(10.0*tempf);
//				else tempf= floor(tempf);
//			}
//			else {
//				if(softmax-softmin < 2.10) tempf= 0.1*floor(10*tempf);
//				else if(softmax-softmin < 21.0) tempf= floor(tempf);
//				else tempf= 10.0*floor(tempf/10.0);
//			}
//		}
//		else {
//			temp= 10*(temp/10);
//			tempf= temp;
//		}
//	}
//
//	if(!ui_is_but_float(but)) {
//		lvalue= floor(data.value+0.5);
//
//		CLAMP(temp, softmin, softmax);
//
//		if(temp != lvalue) {
//			data.value= temp;
//			data.dragchange= 1;
//			changed= 1;
//		}
//	}
//	else {
//		CLAMP(tempf, softmin, softmax);
//
//		if(tempf != data.value) {
//			data.value= tempf;
//			data.dragchange= 1;
//			changed= 1;
//		}
//	}
//
//	return changed;
//}
//
//static int ui_do_but_SLI(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my, click= 0;
//	int retval= WM_UI_HANDLER_CONTINUE;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		/* XXX hardcoded keymap check.... */
//		if(event.type == WHEELDOWNMOUSE && event.alt) {
//			mx= but.x1;
//			click= 2;
//		}
//		else if(event.type == WHEELUPMOUSE && event.alt) {
//			mx= but.x2;
//			click= 2;
//		}
//		else if(event.val==KM_PRESS) {
//			if(ELEM3(event.type, LEFTMOUSE, PADENTER, RETKEY) && event.ctrl) {
//				button_activate_state(C, but, BUTTON_STATE_TEXT_EDITING);
//				retval= WM_UI_HANDLER_BREAK;
//			}
//			else if(event.type == LEFTMOUSE) {
//				data.dragstartx= mx;
//				data.draglastx= mx;
//				button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//				retval= WM_UI_HANDLER_BREAK;
//			}
//			else if(ELEM(event.type, PADENTER, RETKEY) && event.val==KM_PRESS)
//				click= 1;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == ESCKEY) {
//			data.cancel= 1;
//			data.escapecancel= 1;
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//		else if(event.type == LEFTMOUSE && event.val!=KM_PRESS) {
//			if(data.dragchange)
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//			else
//				click= 1;
//		}
//		else if(event.type == MOUSEMOVE) {
//			if(ui_numedit_but_SLI(but, data, event.shift, event.ctrl, mx))
//				ui_numedit_apply(C, block, but, data);
//		}
//		retval= WM_UI_HANDLER_BREAK;
//	}
//	else if(data.state == BUTTON_STATE_TEXT_EDITING) {
//		ui_do_but_textedit(C, block, but, data, event);
//		retval= WM_UI_HANDLER_BREAK;
//	}
//	else if(data.state == BUTTON_STATE_TEXT_SELECTING) {
//		ui_do_but_textedit_select(C, block, but, data, event);
//		retval= WM_UI_HANDLER_BREAK;
//	}
//
//	if(click) {
//		if (click==2) {
//			/* nudge slider to the left or right */
//			float f, tempf, softmin, softmax, softrange;
//			int temp;
//
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//
//			softmin= but.softmin;
//			softmax= but.softmax;
//			softrange= softmax - softmin;
//
//			tempf= data.value;
//			temp= (int)data.value;
//
//			if(but.type==SLI) f= (float)(mx-but.x1)/(but.x2-but.x1);
//			else f= (float)(mx- but.x1)/(but.x2-but.x1);
//
//			f= softmin + f*softrange;
//
//			if(!ui_is_but_float(but)) {
//				if(f<temp) temp--;
//				else temp++;
//
//				if(temp>=softmin && temp<=softmax)
//					data.value= temp;
//				else
//					data.cancel= 1;
//			}
//			else {
//				if(f<tempf) tempf-=.01;
//				else tempf+=.01;
//
//				if(tempf>=softmin && tempf<=softmax)
//					data.value= tempf;
//				else
//					data.cancel= 1;
//			}
//
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//			retval= WM_UI_HANDLER_BREAK;
//		}
//		else {
//			/* edit the value directly */
//			button_activate_state(C, but, BUTTON_STATE_TEXT_EDITING);
//			retval= WM_UI_HANDLER_BREAK;
//		}
//	}
//
//	return retval;
//}
//
//static int ui_do_but_SCROLL(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my, click= 0;
//	int retval= WM_UI_HANDLER_CONTINUE;
//	int horizontal= (but.x2 - but.x1 > but.y2 - but.y1);
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.val==KM_PRESS) {
//			if(event.type == LEFTMOUSE) {
//				if(horizontal) {
//					data.dragstartx= mx;
//					data.draglastx= mx;
//				}
//				else {
//					data.dragstartx= my;
//					data.draglastx= my;
//				}
//				button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//				retval= WM_UI_HANDLER_BREAK;
//			}
//			else if(ELEM(event.type, PADENTER, RETKEY) && event.val==KM_PRESS)
//				click= 1;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == ESCKEY) {
//			data.cancel= 1;
//			data.escapecancel= 1;
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//		else if(event.type == LEFTMOUSE && event.val!=KM_PRESS) {
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//		else if(event.type == MOUSEMOVE) {
//			if(ui_numedit_but_SLI(but, data, 0, 0, (horizontal)? mx: my))
//				ui_numedit_apply(C, block, but, data);
//		}
//
//		retval= WM_UI_HANDLER_BREAK;
//	}
//
//	return retval;
//}

static int ui_do_but_BLOCK(bContext C, uiBut but, uiHandleButtonData data, wmEvent event)
{
//        System.out.println("ui_do_but_BLOCK");
	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		if((event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.PADENTER || event.type==WmEventTypes.RETKEY) && event.val==WmTypes.KM_PRESS) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_MENU_OPEN);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
		else if(but.type==UI.MENU || but.type==UI.ICONROW || but.type==UI.ICONTEXTROW) {

//			if(event.type == WmEventTypes.WHEELDOWNMOUSE && event.alt!=0) {
//				data.value= ui_step_name_menu(but, -1);
//				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
//				ui_apply_button(C, but.block, but, data, 1);
//				return WmTypes.WM_UI_HANDLER_BREAK;
//			}
//			else if(event.type == WmEventTypes.WHEELUPMOUSE && event.alt!=0) {
//				data.value= ui_step_name_menu(but, 1);
//				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
//				ui_apply_button(C, but.block, but, data, 1);
//				return WmTypes.WM_UI_HANDLER_BREAK;
//			}
		}
		else if(but.type==UI.COL) {
//			if( ELEM(event.type, WHEELDOWNMOUSE, WHEELUPMOUSE) && event.alt) {
//				float col[3];
//
//				ui_get_but_vectorf(but, col);
//				rgb_to_hsv(col[0], col[1], col[2], but.hsv, but.hsv+1, but.hsv+2);
//
//				if(event.type==WHEELDOWNMOUSE)
//					but.hsv[2]= CLAMPIS(but.hsv[2]-0.05f, 0.0f, 1.0f);
//				else
//					but.hsv[2]= CLAMPIS(but.hsv[2]+0.05f, 0.0f, 1.0f);
//
//				hsv_to_rgb(but.hsv[0], but.hsv[1], but.hsv[2], data.vec, data.vec+1, data.vec+2);
//
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//				ui_apply_button(C, but.block, but, data, 1);
//				return WM_UI_HANDLER_BREAK;
//			}
		}
	}

	return WmTypes.WM_UI_HANDLER_CONTINUE;
}

//static int ui_numedit_but_NORMAL(uiBut *but, uiHandleButtonData *data, int mx, int my)
//{
//	float dx, dy, rad, radsq, mrad, *fp;
//	int mdx, mdy, changed= 1;
//
//	/* button is presumed square */
//	/* if mouse moves outside of sphere, it does negative normal */
//
//	fp= data.origvec;
//	rad= (but.x2 - but.x1);
//	radsq= rad*rad;
//
//	if(fp[2]>0.0f) {
//		mdx= (rad*fp[0]);
//		mdy= (rad*fp[1]);
//	}
//	else if(fp[2]> -1.0f) {
//		mrad= rad/sqrt(fp[0]*fp[0] + fp[1]*fp[1]);
//
//		mdx= 2.0f*mrad*fp[0] - (rad*fp[0]);
//		mdy= 2.0f*mrad*fp[1] - (rad*fp[1]);
//	}
//	else mdx= mdy= 0;
//
//	dx= (float)(mx+mdx-data.dragstartx);
//	dy= (float)(my+mdy-data.dragstarty);
//
//	fp= data.vec;
//	mrad= dx*dx+dy*dy;
//	if(mrad < radsq) {	/* inner circle */
//		fp[0]= dx;
//		fp[1]= dy;
//		fp[2]= sqrt( radsq-dx*dx-dy*dy );
//	}
//	else {	/* outer circle */
//
//		mrad= rad/sqrt(mrad);	// veclen
//
//		dx*= (2.0f*mrad - 1.0f);
//		dy*= (2.0f*mrad - 1.0f);
//
//		mrad= dx*dx+dy*dy;
//		if(mrad < radsq) {
//			fp[0]= dx;
//			fp[1]= dy;
//			fp[2]= -sqrt( radsq-dx*dx-dy*dy );
//		}
//	}
//	Normalize(fp);
//
//	data.draglastx= mx;
//	data.draglasty= my;
//
//	return changed;
//}
//
//static int ui_do_but_NORMAL(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type==LEFTMOUSE && event.val==KM_PRESS) {
//			data.dragstartx= mx;
//			data.dragstarty= my;
//			data.draglastx= mx;
//			data.draglasty= my;
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//
//			/* also do drag the first time */
//			if(ui_numedit_but_NORMAL(but, data, mx, my))
//				ui_numedit_apply(C, block, but, data);
//
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == MOUSEMOVE) {
//			if(mx!=data.draglastx || my!=data.draglasty) {
//				if(ui_numedit_but_NORMAL(but, data, mx, my))
//					ui_numedit_apply(C, block, but, data);
//			}
//		}
//		else if(event.type==LEFTMOUSE && event.val!=KM_PRESS)
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//static int ui_numedit_but_HSVCUBE(uiBut *but, uiHandleButtonData *data, int mx, int my)
//{
//	float x, y;
//	int changed= 1;
//
//	/* relative position within box */
//	x= ((float)mx-but.x1)/(but.x2-but.x1);
//	y= ((float)my-but.y1)/(but.y2-but.y1);
//	CLAMP(x, 0.0, 1.0);
//	CLAMP(y, 0.0, 1.0);
//
//	if(but.a1==0) {
//		but.hsv[0]= x;
//		but.hsv[2]= y;
//	}
//	else if(but.a1==1) {
//		but.hsv[0]= x;
//		but.hsv[1]= y;
//	}
//	else if(but.a1==2) {
//		but.hsv[2]= x;
//		but.hsv[1]= y;
//	}
//	else if(but.a1==3) {
//		but.hsv[0]= x;
//	}
//	else
//		but.hsv[2]= y;
//
//	ui_set_but_hsv(but);	// converts to rgb
//
//	// update button values and strings
//	ui_update_block_buts_hsv(but.block, but.hsv);
//
//	data.draglastx= mx;
//	data.draglasty= my;
//
//	return changed;
//}
//
//static int ui_do_but_HSVCUBE(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type==LEFTMOUSE && event.val==KM_PRESS) {
//			data.dragstartx= mx;
//			data.dragstarty= my;
//			data.draglastx= mx;
//			data.draglasty= my;
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//
//			/* also do drag the first time */
//			if(ui_numedit_but_HSVCUBE(but, data, mx, my))
//				ui_numedit_apply(C, block, but, data);
//
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == MOUSEMOVE) {
//			if(mx!=data.draglastx || my!=data.draglasty) {
//				if(ui_numedit_but_HSVCUBE(but, data, mx, my))
//					ui_numedit_apply(C, block, but, data);
//			}
//		}
//		else if(event.type==LEFTMOUSE && event.val!=KM_PRESS)
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//static int ui_numedit_but_HSVCIRCLE(uiBut *but, uiHandleButtonData *data, int mx, int my)
//{
//	rcti rect;
//	int changed= 1;
//
//	rect.xmin= but.x1; rect.xmax= but.x2;
//	rect.ymin= but.y1; rect.ymax= but.y2;
//
//	ui_hsvcircle_vals_from_pos(but.hsv, but.hsv+1, &rect, (float)mx, (float)my);
//
//	ui_set_but_hsv(but);	// converts to rgb
//
//	// update button values and strings
//	// XXX ui_update_block_buts_hsv(but.block, but.hsv);
//
//	data.draglastx= mx;
//	data.draglasty= my;
//
//	return changed;
//}
//
//
//static int ui_do_but_HSVCIRCLE(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type==LEFTMOUSE && event.val==KM_PRESS) {
//			data.dragstartx= mx;
//			data.dragstarty= my;
//			data.draglastx= mx;
//			data.draglasty= my;
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//
//			/* also do drag the first time */
//			if(ui_numedit_but_HSVCIRCLE(but, data, mx, my))
//				ui_numedit_apply(C, block, but, data);
//
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		/* XXX hardcoded keymap check.... */
//		if(event.type == WHEELDOWNMOUSE) {
//			but.hsv[2]= CLAMPIS(but.hsv[2]-0.05f, 0.0f, 1.0f);
//			ui_set_but_hsv(but);	// converts to rgb
//			ui_numedit_apply(C, block, but, data);
//		}
//		else if(event.type == WHEELUPMOUSE) {
//			but.hsv[2]= CLAMPIS(but.hsv[2]+0.05f, 0.0f, 1.0f);
//			ui_set_but_hsv(but);	// converts to rgb
//			ui_numedit_apply(C, block, but, data);
//		}
//		else if(event.type == MOUSEMOVE) {
//			if(mx!=data.draglastx || my!=data.draglasty) {
//				if(ui_numedit_but_HSVCIRCLE(but, data, mx, my))
//					ui_numedit_apply(C, block, but, data);
//			}
//		}
//		else if(event.type==LEFTMOUSE && event.val!=KM_PRESS)
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//
//static int verg_colorband(const void *a1, const void *a2)
//{
//	const CBData *x1=a1, *x2=a2;
//
//	if( x1.pos > x2.pos ) return 1;
//	else if( x1.pos < x2.pos) return -1;
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//static void ui_colorband_update(ColorBand *coba)
//{
//	int a;
//
//	if(coba.tot<2) return;
//
//	for(a=0; a<coba.tot; a++) coba.data[a].cur= a;
//		qsort(coba.data, coba.tot, sizeof(CBData), verg_colorband);
//	for(a=0; a<coba.tot; a++) {
//		if(coba.data[a].cur==coba.cur) {
//			coba.cur= a;
//			break;
//		}
//	}
//}
//
//static int ui_numedit_but_COLORBAND(uiBut *but, uiHandleButtonData *data, int mx)
//{
//	float dx;
//	int changed= 0;
//
//	if(data.draglastx == mx)
//		return changed;
//
//	dx= ((float)(mx - data.draglastx))/(but.x2-but.x1);
//	data.dragcbd.pos += dx;
//	CLAMP(data.dragcbd.pos, 0.0, 1.0);
//
//	ui_colorband_update(data.coba);
//	data.dragcbd= data.coba.data + data.coba.cur;	/* because qsort */
//
//	data.draglastx= mx;
//	changed= 1;
//
//	return changed;
//}
//
//static int ui_do_but_COLORBAND(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	ColorBand *coba;
//	CBData *cbd;
//	int mx, my, a, xco, mindist= 12;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type==LEFTMOUSE && event.val==KM_PRESS) {
//			coba= (ColorBand*)but.poin;
//
//			if(event.ctrl) {
//				/* insert new key on mouse location */
//				if(coba.tot < MAXCOLORBAND-1) {
//					float pos= ((float)(mx - but.x1))/(but.x2-but.x1);
//					float col[4];
//
//					do_colorband(coba, pos, col);	/* executes it */
//
//					coba.tot++;
//					coba.cur= coba.tot-1;
//
//					coba.data[coba.cur].r= col[0];
//					coba.data[coba.cur].g= col[1];
//					coba.data[coba.cur].b= col[2];
//					coba.data[coba.cur].a= col[3];
//					coba.data[coba.cur].pos= pos;
//
//					ui_colorband_update(coba);
//				}
//
//				button_activate_state(C, but, BUTTON_STATE_EXIT);
//			}
//			else {
//				data.dragstartx= mx;
//				data.dragstarty= my;
//				data.draglastx= mx;
//				data.draglasty= my;
//
//				/* activate new key when mouse is close */
//				for(a=0, cbd= coba.data; a<coba.tot; a++, cbd++) {
//					xco= but.x1 + (cbd.pos*(but.x2-but.x1));
//					xco= ABS(xco-mx);
//					if(a==coba.cur) xco+= 5; // selected one disadvantage
//					if(xco<mindist) {
//						coba.cur= a;
//						mindist= xco;
//					}
//				}
//
//				data.dragcbd= coba.data + coba.cur;
//				button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//			}
//
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == MOUSEMOVE) {
//			if(mx!=data.draglastx || my!=data.draglasty) {
//				if(ui_numedit_but_COLORBAND(but, data, mx))
//					ui_numedit_apply(C, block, but, data);
//			}
//		}
//		else if(event.type==LEFTMOUSE && event.val!=KM_PRESS)
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//static int ui_numedit_but_CURVE(uiBut *but, uiHandleButtonData *data, int snap, int mx, int my)
//{
//	CurveMapping *cumap= data.cumap;
//	CurveMap *cuma= cumap.cm+cumap.cur;
//	CurveMapPoint *cmp= cuma.curve;
//	float fx, fy, zoomx, zoomy, offsx, offsy;
//	int a, changed= 0;
//
//	zoomx= (but.x2-but.x1)/(cumap.curr.xmax-cumap.curr.xmin);
//	zoomy= (but.y2-but.y1)/(cumap.curr.ymax-cumap.curr.ymin);
//	offsx= cumap.curr.xmin;
//	offsy= cumap.curr.ymin;
//
//	if(data.dragsel != -1) {
//		int moved_point= 0;		/* for ctrl grid, can't use orig coords because of sorting */
//
//		fx= (mx-data.draglastx)/zoomx;
//		fy= (my-data.draglasty)/zoomy;
//		for(a=0; a<cuma.totpoint; a++) {
//			if(cmp[a].flag & SELECT) {
//				float origx= cmp[a].x, origy= cmp[a].y;
//				cmp[a].x+= fx;
//				cmp[a].y+= fy;
//				if(snap) {
//					cmp[a].x= 0.125f*floor(0.5f + 8.0f*cmp[a].x);
//					cmp[a].y= 0.125f*floor(0.5f + 8.0f*cmp[a].y);
//				}
//				if(cmp[a].x!=origx || cmp[a].y!=origy)
//					moved_point= 1;
//			}
//		}
//
//		curvemapping_changed(cumap, 0);	/* no remove doubles */
//
//		if(moved_point) {
//			data.draglastx= mx;
//			data.draglasty= my;
//			changed= 1;
//		}
//
//		data.dragchange= 1; /* mark for selection */
//	}
//	else {
//		fx= (mx-data.draglastx)/zoomx;
//		fy= (my-data.draglasty)/zoomy;
//
//		/* clamp for clip */
//		if(cumap.flag & CUMA_DO_CLIP) {
//			if(cumap.curr.xmin-fx < cumap.clipr.xmin)
//				fx= cumap.curr.xmin - cumap.clipr.xmin;
//			else if(cumap.curr.xmax-fx > cumap.clipr.xmax)
//				fx= cumap.curr.xmax - cumap.clipr.xmax;
//			if(cumap.curr.ymin-fy < cumap.clipr.ymin)
//				fy= cumap.curr.ymin - cumap.clipr.ymin;
//			else if(cumap.curr.ymax-fy > cumap.clipr.ymax)
//				fy= cumap.curr.ymax - cumap.clipr.ymax;
//		}
//
//		cumap.curr.xmin-=fx;
//		cumap.curr.ymin-=fy;
//		cumap.curr.xmax-=fx;
//		cumap.curr.ymax-=fy;
//
//		data.draglastx= mx;
//		data.draglasty= my;
//
//		changed= 1;
//	}
//
//	return changed;
//}
//
//static int ui_do_but_CURVE(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	int mx, my, a, changed= 0;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type==LEFTMOUSE && event.val==KM_PRESS) {
//			CurveMapping *cumap= (CurveMapping*)but.poin;
//			CurveMap *cuma= cumap.cm+cumap.cur;
//			CurveMapPoint *cmp= cuma.curve;
//			float fx, fy, zoomx, zoomy, offsx, offsy;
//			float dist, mindist= 200.0f; // 14 pixels radius
//			int sel= -1;
//
//			zoomx= (but.x2-but.x1)/(cumap.curr.xmax-cumap.curr.xmin);
//			zoomy= (but.y2-but.y1)/(cumap.curr.ymax-cumap.curr.ymin);
//			offsx= cumap.curr.xmin;
//			offsy= cumap.curr.ymin;
//
//			if(event.ctrl) {
//				fx= ((float)my - but.x1)/zoomx + offsx;
//				fy= ((float)my - but.y1)/zoomy + offsy;
//
//				curvemap_insert(cuma, fx, fy);
//				curvemapping_changed(cumap, 0);
//				changed= 1;
//			}
//
//			/* check for selecting of a point */
//			cmp= cuma.curve;	/* ctrl adds point, new malloc */
//			for(a=0; a<cuma.totpoint; a++) {
//				fx= but.x1 + zoomx*(cmp[a].x-offsx);
//				fy= but.y1 + zoomy*(cmp[a].y-offsy);
//				dist= (fx-mx)*(fx-mx) + (fy-my)*(fy-my);
//				if(dist < mindist) {
//					sel= a;
//					mindist= dist;
//				}
//			}
//
//			if (sel == -1) {
//				/* if the click didn't select anything, check if it's clicked on the
//				 * curve itself, and if so, add a point */
//				fx= ((float)mx - but.x1)/zoomx + offsx;
//				fy= ((float)my - but.y1)/zoomy + offsy;
//
//				cmp= cuma.table;
//
//				/* loop through the curve segment table and find what's near the mouse.
//				 * 0.05 is kinda arbitrary, but seems to be what works nicely. */
//				for(a=0; a<=CM_TABLE; a++) {
//					if ( ( fabs(fx - cmp[a].x) < (0.05) ) && ( fabs(fy - cmp[a].y) < (0.05) ) ) {
//
//						curvemap_insert(cuma, fx, fy);
//						curvemapping_changed(cumap, 0);
//
//						changed= 1;
//
//						/* reset cmp back to the curve points again, rather than drawing segments */
//						cmp= cuma.curve;
//
//						/* find newly added point and make it 'sel' */
//						for(a=0; a<cuma.totpoint; a++)
//							if(cmp[a].x == fx)
//								sel = a;
//
//						break;
//					}
//				}
//			}
//
//			if(sel!= -1) {
//				/* ok, we move a point */
//				/* deselect all if this one is deselect. except if we hold shift */
//				if(event.shift==0 && (cmp[sel].flag & SELECT)==0)
//					for(a=0; a<cuma.totpoint; a++)
//						cmp[a].flag &= ~SELECT;
//				cmp[sel].flag |= SELECT;
//			}
//			else {
//				/* move the view */
//				data.cancel= 1;
//			}
//
//			data.dragsel= sel;
//
//			data.dragstartx= mx;
//			data.dragstarty= my;
//			data.draglastx= mx;
//			data.draglasty= my;
//
//			button_activate_state(C, but, BUTTON_STATE_NUM_EDITING);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_NUM_EDITING) {
//		if(event.type == MOUSEMOVE) {
//			if(mx!=data.draglastx || my!=data.draglasty) {
//				if(ui_numedit_but_CURVE(but, data, event.shift, mx, my))
//					ui_numedit_apply(C, block, but, data);
//			}
//		}
//		else if(event.type==LEFTMOUSE && event.val!=KM_PRESS) {
//			if(data.dragsel != -1) {
//				CurveMapping *cumap= data.cumap;
//				CurveMap *cuma= cumap.cm+cumap.cur;
//				CurveMapPoint *cmp= cuma.curve;
//
//				if(!data.dragchange) {
//					/* deselect all, select one */
//					if(event.shift==0) {
//						for(a=0; a<cuma.totpoint; a++)
//							cmp[a].flag &= ~SELECT;
//						cmp[data.dragsel].flag |= SELECT;
//					}
//				}
//				else
//					curvemapping_changed(cumap, 1);	/* remove doubles */
//			}
//
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//		}
//
//		return WM_UI_HANDLER_BREAK;
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
//#ifdef INTERNATIONAL
//static int ui_do_but_CHARTAB(bContext *C, uiBlock *block, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	/* XXX 2.50 bad global and state access */
//#if 0
//	float sx, sy, ex, ey;
//	float width, height;
//	float butw, buth;
//	int mx, my, x, y, cs, che;
//
//	mx= event.x;
//	my= event.y;
//	ui_window_to_block(data.region, block, &mx, &my);
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(ELEM3(event.type, LEFTMOUSE, PADENTER, RETKEY) && event.val==KM_PRESS) {
//			/* Calculate the size of the button */
//			width = abs(but.x2 - but.x1);
//			height = abs(but.y2 - but.y1);
//
//			butw = floor(width / 12);
//			buth = floor(height / 6);
//
//			/* Initialize variables */
//			sx = but.x1;
//			ex = but.x1 + butw;
//			sy = but.y1 + height - buth;
//			ey = but.y1 + height;
//
//			cs = G.charstart;
//
//			/* And the character is */
//			x = (int) ((mx / butw) - 0.5);
//			y = (int) (6 - ((my / buth) - 0.5));
//
//			che = cs + (y*12) + x;
//
//			if(che > G.charmax)
//				che = 0;
//
//			if(G.obedit)
//			{
//				do_textedit(0,0,che);
//			}
//
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//			return WM_UI_HANDLER_BREAK;
//		}
//		else if(ELEM(event.type, WHEELUPMOUSE, PAGEUPKEY)) {
//			for(but= block.buttons.first; but; but= but.next) {
//				if(but.type == CHARTAB) {
//					G.charstart = G.charstart - (12*6);
//					if(G.charstart < 0)
//						G.charstart = 0;
//					if(G.charstart < G.charmin)
//						G.charstart = G.charmin;
//					ui_draw_but(but);
//
//					//Really nasty... to update the num button from the same butblock
//					for(bt= block.buttons.first; bt; bt= bt.next)
//					{
//						if(ELEM(bt.type, NUM, NUMABS)) {
//							ui_check_but(bt);
//							ui_draw_but(bt);
//						}
//					}
//					retval=UI_CONT;
//					break;
//				}
//			}
//
//			return WM_UI_HANDLER_BREAK;
//		}
//		else if(ELEM(event.type, WHEELDOWNMOUSE, PAGEDOWNKEY)) {
//			for(but= block.buttons.first; but; but= but.next)
//			{
//				if(but.type == CHARTAB)
//				{
//					G.charstart = G.charstart + (12*6);
//					if(G.charstart > (0xffff - 12*6))
//						G.charstart = 0xffff - (12*6);
//					if(G.charstart > G.charmax - 12*6)
//						G.charstart = G.charmax - 12*6;
//					ui_draw_but(but);
//
//					for(bt= block.buttons.first; bt; bt= bt.next)
//					{
//						if(ELEM(bt.type, NUM, NUMABS)) {
//							ui_check_but(bt);
//							ui_draw_but(bt);
//						}
//					}
//
//					but.flag |= UI_ACTIVE;
//					retval=UI_RETURN_OK;
//					break;
//				}
//			}
//
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//#endif
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//#endif
//
//
//static int ui_do_but_LINK(bContext *C, uiBut *but, uiHandleButtonData *data, wmEvent *event)
//{
//	ARegion *ar= CTX_wm_region(C);
//
//	but.linkto[0]= event.x-ar.winrct.xmin;
//	but.linkto[1]= event.y-ar.winrct.ymin;
//
//	if(data.state == BUTTON_STATE_HIGHLIGHT) {
//		if(event.type == LEFTMOUSE && event.val==KM_PRESS) {
//			button_activate_state(C, but, BUTTON_STATE_WAIT_RELEASE);
//			return WM_UI_HANDLER_BREAK;
//		}
//		else if(event.type == LEFTMOUSE && but.block.handle) {
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//	else if(data.state == BUTTON_STATE_WAIT_RELEASE) {
//
//		if(event.type == LEFTMOUSE && event.val!=KM_PRESS) {
//			if(!(but.flag & UI_SELECT))
//				data.cancel= 1;
//			button_activate_state(C, but, BUTTON_STATE_EXIT);
//			return WM_UI_HANDLER_BREAK;
//		}
//	}
//
//	return WM_UI_HANDLER_CONTINUE;
//}
//
///* callback for hotkey change button/menu */
//static void do_menu_change_hotkey(bContext *C, void *but_v, void *key_v)
//{
//	uiBut *but= but_v;
//	IDProperty *prop= (but.opptr)? but.opptr.data: NULL;
//	short *key= key_v;
//	char buf[512], *butstr, *cpoin;
//
//	/* signal for escape */
//	if(key[0]==0) return;
//
//	WM_key_event_operator_change(C, but.optype.idname, but.opcontext, prop, key[0], key[1]);
//
//	/* complex code to change name of button */
//	if(WM_key_event_operator_string(C, but.optype.idname, but.opcontext, prop, buf, sizeof(buf))) {
//
//		butstr= MEM_mallocN(strlen(but.str)+strlen(buf)+2, "menu_block_set_keymaps");
//
//		/* XXX but.str changed... should not, remove the hotkey from it */
//		cpoin= strchr(but.str, '|');
//		if(cpoin) *cpoin= 0;
//
//		strcpy(butstr, but.str);
//		strcat(butstr, "|");
//		strcat(butstr, buf);
//
//		but.str= but.strdata;
//		BLI_strncpy(but.str, butstr, sizeof(but.strdata));
//		MEM_freeN(butstr);
//
//		ui_check_but(but);
//	}
//
//}
//
//
//static uiBlock *menu_change_hotkey(bContext *C, ARegion *ar, void *arg_but)
//{
//	uiBlock *block;
//	uiBut *but= arg_but;
//	wmOperatorType *ot= WM_operatortype_find(but.optype.idname, 1);
//	static short dummy[2];
//	char buf[OP_MAX_TYPENAME+10];
//
//	dummy[0]= 0;
//	dummy[1]= 0;
//
//	block= uiBeginBlock(C, ar, "_popup", UI_EMBOSSP);
//	uiBlockSetFlag(block, UI_BLOCK_LOOP|UI_BLOCK_MOVEMOUSE_QUIT|UI_BLOCK_RET_1);
//
//	BLI_strncpy(buf, ot.name, OP_MAX_TYPENAME);
//	strcat(buf, " |");
//
//	but= uiDefHotKeyevtButS(block, 0, buf, 0, 0, 200, 20, dummy, dummy+1, "");
//	uiButSetFunc(but, do_menu_change_hotkey, arg_but, dummy);
//
//	uiPopupBoundsBlock(block, 6.0f, 50, -10);
//	uiEndBlock(C, block);
//
//	return block;
//}

static int ui_do_button(bContext C, uiBlock block, uiBut but, wmEvent event)
{
//        System.out.println("ui_do_button");
	uiHandleButtonData data;
	int retval;

	data= but.active;
	retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	if((but.flag & UI.UI_BUT_DISABLED)!=0)
		return WmTypes.WM_UI_HANDLER_BREAK;

	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		/* handle copy-paste */
		if((event.type==WmEventTypes.CKEY || event.type==WmEventTypes.VKEY) && event.val==WmTypes.KM_PRESS && (event.ctrl!=0 || event.oskey!=0)) {
//			ui_but_copy_paste(C, but, data, (event.type == CKEY)? 'c': 'v');
//			return WM_UI_HANDLER_BREAK;
		}
		/* handle keyframeing */
		else if(event.type == WmEventTypes.IKEY && event.val == WmTypes.KM_PRESS) {
//			if(event.alt)
//				ui_but_anim_delete_keyframe(C);
//			else
//				ui_but_anim_insert_keyframe(C);
//
//			ED_region_tag_redraw(CTX_wm_region(C));
//
//			return WM_UI_HANDLER_BREAK;
		}
		/* handle driver adding */
		else if(event.type == WmEventTypes.DKEY && event.val == WmTypes.KM_PRESS) {
//			if(event.alt)
//				ui_but_anim_remove_driver(C);
//			else
//				ui_but_anim_add_driver(C);
//
//			ED_region_tag_redraw(CTX_wm_region(C));
//
//			return WM_UI_HANDLER_BREAK;
		}
		/* handle menu */
		else if(event.type == WmEventTypes.RIGHTMOUSE && event.val == WmTypes.KM_PRESS) {
			/* RMB has two options now */
//			if(but.rnapoin.data && but.rnaprop) {
//				button_timers_tooltip_remove(C, but);
//				ui_but_anim_menu(C, but);
//				return WM_UI_HANDLER_BREAK;
//			}
//			else if((but.block.flag & UI_BLOCK_LOOP) && but.optype) {
//                        if((but.block.flag & UI.UI_BLOCK_LOOP)!=0 && but.optype) {
//				IDProperty *prop= (but.opptr)? but.opptr.data: NULL;
//				char buf[512];
//
//				if(WM_key_event_operator_string(C, but.optype.idname, but.opcontext, prop, buf, sizeof(buf))) {
//
//					uiPupBlock(C, menu_change_hotkey, but);
//
//				}
//			}
		}
	}

	/* verify if we can edit this button */
	if(event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.RETKEY) {
		/* this should become disabled button .. */
		if(but.lock!=0) {
			if(but.lockstr!=null) {
//				BKE_report(NULL, RPT_WARNING, but.lockstr);
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				return WmTypes.WM_UI_HANDLER_BREAK;
			}
		}
		else if(but.pointype!=0 && but.poin==null) {
			/* there's a pointer needed */
//			BKE_reportf(NULL, RPT_WARNING, "DoButton pointer error: %s", but.str);
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
			return WmTypes.WM_UI_HANDLER_BREAK;
		}
	}

	switch(but.type) {
	case UI.BUT:
		retval= ui_do_but_BUT(C, but, data, event);
		break;
	case UI.KEYEVT:
//		retval= ui_do_but_KEYEVT(C, but, data, event);
		break;
	case UI.HOTKEYEVT:
//		retval= ui_do_but_HOTKEYEVT(C, but, data, event);
		break;
	case UI.TOGBUT:
	case UI.TOG:
	case UI.TOGR:
	case UI.ICONTOG:
	case UI.ICONTOGN:
	case UI.TOGN:
	case UI.BUT_TOGDUAL:
	case UI.OPTION:
	case UI.OPTIONN:
		retval= ui_do_but_TOG(C, but, data, event);
		break;
	case UI.SCROLL:
//		retval= ui_do_but_SCROLL(C, block, but, data, event);
		break;
	case UI.NUM:
	case UI.NUMABS:
		retval= ui_do_but_NUM(C, block, but, data, event);
		break;
	case UI.SLI:
	case UI.NUMSLI:
	case UI.HSVSLI:
//		retval= ui_do_but_SLI(C, block, but, data, event);
		break;
	case UI.ROUNDBOX:
	case UI.LISTBOX:
	case UI.LABEL:
	case UI.TOG3:
	case UI.ROW:
	case UI.LISTROW:
		retval= ui_do_but_EXIT(C, but, data, event);
		break;
	case UI.TEX:
	case UI.IDPOIN:
	case UI.SEARCH_MENU:
//		retval= ui_do_but_TEX(C, block, but, data, event);
		break;
	case UI.MENU:
	case UI.ICONROW:
	case UI.ICONTEXTROW:
	case UI.BLOCK:
	case UI.PULLDOWN:
		retval= ui_do_but_BLOCK(C, but, data, event);
		break;
	case UI.BUTM:
		retval= ui_do_but_BUT(C, but, data, event);
		break;
	case UI.COL:
//		if(but.a1 == -1)  // signal to prevent calling up color picker
//			retval= ui_do_but_EXIT(C, but, data, event);
//		else
//			retval= ui_do_but_BLOCK(C, but, data, event);
		break;
	case UI.BUT_NORMAL:
//		retval= ui_do_but_NORMAL(C, block, but, data, event);
		break;
	case UI.BUT_COLORBAND:
//		retval= ui_do_but_COLORBAND(C, block, but, data, event);
		break;
	case UI.BUT_CURVE:
//		retval= ui_do_but_CURVE(C, block, but, data, event);
		break;
	case UI.HSVCUBE:
//		retval= ui_do_but_HSVCUBE(C, block, but, data, event);
		break;
	case UI.HSVCIRCLE:
//		retval= ui_do_but_HSVCIRCLE(C, block, but, data, event);
		break;
//#ifdef INTERNATIONAL
//	case CHARTAB:
//		retval= ui_do_but_CHARTAB(C, block, but, data, event);
//		break;
//#endif

	case UI.LINK:
	case UI.INLINK:
//		retval= ui_do_but_LINK(C, but, data, event);
		break;
	}

	return retval;
}

/* ************************ button utilities *********************** */

static boolean ui_but_contains_pt(uiBut but, int mx, int my)
{
	return ((but.x1<mx && but.x2>=mx) && (but.y1<my && but.y2>=my));
}

static uiBut ui_but_find_activated(ARegion ar)
{
	uiBlock block;
	uiBut but;

	for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next)
		for(but=block.buttons.first; but!=null; but= but.next)
			if(but.active!=null)
				return but;

	return null;
}

public static boolean ui_button_is_active(ARegion ar)
{
	return (ui_but_find_activated(ar) != null);
}

static void ui_blocks_set_tooltips(ARegion ar, int enable)
{
	uiBlock block;

	if(ar==null)
		return;

	/* we disabled buttons when when they were already shown, and
	 * re-enable them on mouse move */
	for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next)
		block.tooltipdisabled= enable!=0?0:1;
}

static boolean ui_mouse_inside_region(ARegion ar, int x, int y)
{
	uiBlock block;
	int[] mx={0}, my={0};

	/* check if the mouse is in the region, and in case of a view2d also check
	 * if it is inside the view2d itself, not over scrollbars for example */
	if(!Rct.BLI_in_rcti(ar.winrct, x, y)) {
		for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next)
			block.auto_open= 0;

		return false;
	}

	if(ar.v2d.mask.xmin!=ar.v2d.mask.xmax) {
		mx[0]= x;
		my[0]= y;
		UI.ui_window_to_region(ar, mx, my);

		if(!Rct.BLI_in_rcti(ar.v2d.mask, mx[0], my[0]))
			return false;
	}

	return true;
}

static boolean ui_mouse_inside_button(ARegion ar, uiBut but, int[] x, int[] y)
{
	if(!ui_mouse_inside_region(ar, x[0], y[0]))
		return false;

	UI.ui_window_to_block(ar, but.block, x, y);

	if(!ui_but_contains_pt(but, x[0], y[0]))
		return false;

	return true;
}

static uiBut ui_but_find_mouse_over(ARegion ar, int x, int y)
{
	uiBlock block;
	uiBut but, butover= null;
	int[] mx={0}, my={0};

	if(!ui_mouse_inside_region(ar, x, y))
		return null;

	for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next) {
		mx[0]= x;
		my[0]= y;
		UI.ui_window_to_block(ar, block, mx, my);

		for(but=block.buttons.first; but!=null; but= but.next) {
			if(but.type==UI.LABEL || but.type==UI.ROUNDBOX || but.type==UI.SEPR || but.type==UI.LISTBOX)
				continue;
			if((but.flag & UI.UI_HIDDEN)!=0)
				continue;
			if(ui_but_contains_pt(but, mx[0], my[0]))
				/* give precedence to already activated buttons */
				if(butover==null || (butover.active==null && but.active!=null))
					butover= but;
		}
	}

	return butover;
}

/* ****************** button state handling **************************/

static boolean button_modal_state(uiHandleButtonState state)
{
	return (state==uiHandleButtonState.BUTTON_STATE_WAIT_RELEASE || state==uiHandleButtonState.BUTTON_STATE_WAIT_KEY_EVENT
		|| state==uiHandleButtonState.BUTTON_STATE_NUM_EDITING || state==uiHandleButtonState.BUTTON_STATE_TEXT_EDITING
		|| state==uiHandleButtonState.BUTTON_STATE_TEXT_SELECTING || state==uiHandleButtonState.BUTTON_STATE_MENU_OPEN);
}

//static void button_timers_tooltip_remove(bContext *C, uiBut *but)
//{
//	uiHandleButtonData *data;
//
//	data= but.active;
//
//	if(data.tooltiptimer) {
//		WM_event_remove_window_timer(data.window, data.tooltiptimer);
//		data.tooltiptimer= NULL;
//	}
//	if(data.tooltip) {
//		ui_tooltip_free(C, data.tooltip);
//		data.tooltip= NULL;
//	}
//
//	if(data.autoopentimer) {
//		WM_event_remove_window_timer(data.window, data.autoopentimer);
//		data.autoopentimer= NULL;
//	}
//}
//
//static void button_tooltip_timer_reset(uiBut *but)
//{
//	uiHandleButtonData *data;
//
//	data= but.active;
//
//	if(data.tooltiptimer) {
//		WM_event_remove_window_timer(data.window, data.tooltiptimer);
//		data.tooltiptimer= NULL;
//	}
//
//	if(U.flag & USER_TOOLTIPS)
//		if(!but.block.tooltipdisabled)
//			data.tooltiptimer= WM_event_add_window_timer(data.window, TIMER, BUTTON_TOOLTIP_DELAY);
//}

static void button_activate_state(bContext C, uiBut but, uiHandleButtonState state)
{
//        System.out.println("button_activate_state: "+state);
	uiHandleButtonData data;

	data= but.active;
	if(data.state == state)
		return;

	/* highlight has timers for tooltips and auto open */
	if(state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		but.flag &= ~UI.UI_SELECT;

//		button_tooltip_timer_reset(but);

		/* automatic open pulldown block timer */
		if(but.type==UI.BLOCK || but.type==UI.PULLDOWN || but.type==UI.ICONTEXTROW) {
//			if(data.used_mouse!=0 && !data.autoopentimer) {
//				int time;
//
//				if(but.block.auto_open==2) time= 1;    // test for toolbox
//				else if((but.block.flag & UI.UI_BLOCK_LOOP)!=0 || but.block.auto_open!=0) time= 5*U.menuthreshold2;
//				else if((U.uiflag & UserDefTypes.USER_MENUOPENAUTO)!=0) time= 5*U.menuthreshold1;
//				else time= -1;
//
//				if(time >= 0)
//					data.autoopentimer= WM_event_add_window_timer(data.window, TIMER, 0.02*(double)time);
//			}
		}
	}
	else {
		but.flag |= UI.UI_SELECT;
//		button_timers_tooltip_remove(C, but);
	}

//	/* text editing */
//	if(state == BUTTON_STATE_TEXT_EDITING && data.state != BUTTON_STATE_TEXT_SELECTING)
//		ui_textedit_begin(C, but, data);
//	else if(data.state == BUTTON_STATE_TEXT_EDITING && state != BUTTON_STATE_TEXT_SELECTING)
//		ui_textedit_end(C, but, data);
//
//	/* number editing */
//	if(state == BUTTON_STATE_NUM_EDITING)
//		ui_numedit_begin(but, data);
//	else if(data.state == BUTTON_STATE_NUM_EDITING)
//		ui_numedit_end(but, data);

	/* menu open */
	if(state == uiHandleButtonState.BUTTON_STATE_MENU_OPEN)
		ui_blockopen_begin(C, but, data);
	else if(data.state == uiHandleButtonState.BUTTON_STATE_MENU_OPEN)
		ui_blockopen_end(C, but, data);

//	/* add a short delay before exiting, to ensure there is some feedback */
//	if(state == BUTTON_STATE_WAIT_FLASH) {
//		data.flashtimer= WM_event_add_window_timer(data.window, TIMER, BUTTON_FLASH_DELAY);
//	}
//	else if(data.flashtimer) {
//		WM_event_remove_window_timer(data.window, data.flashtimer);
//		data.flashtimer= NULL;
//	}

	/* add a blocking ui handler at the window handler for blocking, modal states
	 * but not for popups, because we already have a window level handler*/
	if(!(but.block.handle!=null && but.block.handle.popup!=0)) {
		if(button_modal_state(state)) {
			if(!button_modal_state(data.state))
				WmEventSystem.WM_event_add_ui_handler(C, data.window.handlers, ui_handler_region_menu, null, data);
		}
		else {
			if(button_modal_state(data.state))
				WmEventSystem.WM_event_remove_ui_handler(data.window.handlers, ui_handler_region_menu, null, data);
		}
	}

	data.state= state;

	UI.ui_check_but(but);

	/* redraw */
	Area.ED_region_tag_redraw(data.region);
}

static void button_activate_init(bContext C, ARegion ar, uiBut but, uiButtonActivateType type)
{
//    System.out.println("button_activate_init");
	uiHandleButtonData data;

	/* setup struct */
	data= new uiHandleButtonData();
	data.window= bContext.CTX_wm_window(C);
	data.region= ar;
	if( but.type==UI.TEX || but.type==UI.BUT_CURVE || but.type==UI.SEARCH_MENU );  // XXX curve is temp
	else data.interactive= 1;

	data.state = uiHandleButtonState.BUTTON_STATE_INIT;

	/* activate button */
	but.flag |= UI.UI_ACTIVE;
//    System.out.printf("button_activate_init active set: %f,%f %f,%f\n", but.x1, but.y1, but.x2, but.y2);
//    Thread.dumpStack();
	but.active= data;

	/* we disable auto_open in the block after a threshold, because we still
	 * want to allow auto opening adjacent menus even if no button is activated
	 * inbetween going over to the other button, but only for a short while */
	if(type == uiButtonActivateType.BUTTON_ACTIVATE_OVER && but.block.auto_open!=0)
		if(but.block.auto_open_last+BUTTON_AUTO_OPEN_THRESH < Time.PIL_check_seconds_timer())
			but.block.auto_open= 0;

	if(type == uiButtonActivateType.BUTTON_ACTIVATE_OVER) {
		data.used_mouse= 1;
	}
	button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_HIGHLIGHT);

	/* activate right away */
	if(but.type==UI.HOTKEYEVT)
		button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_WAIT_KEY_EVENT);

	if(type == uiButtonActivateType.BUTTON_ACTIVATE_OPEN) {
		button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_MENU_OPEN);

		/* activate first button in submenu */
		if(data.menu!=null && data.menu.region!=null) {
			ARegion subar= data.menu.region;
			uiBlock subblock= (uiBlock)subar.uiblocks.first;
			uiBut subbut;

			if(subblock!=null) {
				subbut= ui_but_first(subblock);

				if(subbut!=null)
					ui_handle_button_activate(C, subar, subbut, uiButtonActivateType.BUTTON_ACTIVATE);
			}
		}
	}
	else if(type == uiButtonActivateType.BUTTON_ACTIVATE_TEXT_EDITING)
		button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_TEXT_EDITING);
	else if(type == uiButtonActivateType.BUTTON_ACTIVATE_APPLY)
		button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_WAIT_FLASH);
}

static void button_activate_exit(bContext C, uiHandleButtonData data, uiBut but, boolean mousemove)
{
	uiBlock block= but.block;
	uiBut bt;

	/* ensure we are in the exit state */
	if(data.state != uiHandleButtonState.BUTTON_STATE_EXIT)
		button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);

	/* apply the button action or value */
	ui_apply_button(C, block, but, data, 0);

	/* if this button is in a menu, this will set the button return
	 * value to the button value and the menu return value to ok, the
	 * menu return value will be picked up and the menu will close */
	if(block.handle!=null && (block.flag & UI.UI_BLOCK_KEEP_OPEN)==0) {
		if(data.cancel==0 || data.escapecancel!=0) {
			uiPopupBlockHandle menu;

			menu= block.handle;
			menu.butretval= data.retval;
			menu.menuretval= (data.cancel!=0)? UI.UI_RETURN_CANCEL: UI.UI_RETURN_OK;
		}
	}

	/* autokey & undo push */
//	if(data.cancel==0)
//		ui_apply_autokey_undo(C, but);

	/* disable tooltips until mousemove + last active flag */
	for(block=(uiBlock)data.region.uiblocks.first; block!=null; block=block.next) {
		for(bt=block.buttons.first; bt!=null; bt=bt.next)
			bt.flag &= ~UI.UI_BUT_LAST_ACTIVE;

		block.tooltipdisabled= 1;
	}

//	ui_blocks_set_tooltips(data.region, 0);

	/* clean up */
//	if(data.str!=null)
//		MEM_freeN(data.str);
//	if(data.origstr)
//		MEM_freeN(data.origstr);
        data.str = null;
        data.origstr = null;

	/* redraw (data is but.active!) */
	Area.ED_region_tag_redraw(data.region);

	/* clean up button */
//	MEM_freeN(but.active);
	but.active= null;
//        System.out.println("button_activate_exit active = null");
	but.flag &= ~(UI.UI_ACTIVE|UI.UI_SELECT);
	but.flag |= UI.UI_BUT_LAST_ACTIVE;
	UI.ui_check_but(but);

	/* adds empty mousemove in queue for re-init handler, in case mouse is
	 * still over a button. we cannot just check for this ourselfs because
	 * at this point the mouse may be over a button in another region */
	if(mousemove)
		WmEventSystem.WM_event_add_mousemove(C);
}

public static void ui_button_active_cancel(bContext C, uiBut but)
{
//        System.out.println("ui_button_active_cancel");
	uiHandleButtonData data;

	/* this gets called when the button somehow disappears while it is still
	 * active, this is bad for user interaction, but we need to handle this
	 * case cleanly anyway in case it happens */
	if(but.active!=null) {
		data= but.active;
		data.cancel= 1;
		button_activate_exit(C, data, but, false);
	}
}

/************** handle activating a button *************/

static uiBut uit_but_find_open_event(ARegion ar, wmEvent event)
{
	uiBlock block;
	uiBut but;

	for(block=(uiBlock)ar.uiblocks.first; block!=null; block=block.next) {
		for(but=block.buttons.first; but!=null; but= but.next)
			if(but==event.customdata)
				return but;
	}
	return null;
}

static int ui_handle_button_over(bContext C, wmEvent event, ARegion ar)
{
//        System.out.println("ui_handle_button_over: "+event.type);
	uiBut but;

	if(event.type == WmEventTypes.MOUSEMOVE) {
		but= ui_but_find_mouse_over(ar, event.x, event.y);
		if(but!=null) {
			button_activate_init(C, ar, but, uiButtonActivateType.BUTTON_ACTIVATE_OVER);
//			System.out.printf("ui_handle_button_over event: %d,%d\n", event.x, event.y);
//			System.out.printf("ui_handle_button_over: %f,%f %f,%f\n", but.x1, but.y1, but.x2, but.y2);
//			Thread.dumpStack();
		}
	}
	else if(event.type == WmEventTypes.EVT_BUT_OPEN) {
		but= uit_but_find_open_event(ar, event);
		if(but!=null) {
			button_activate_init(C, ar, but, uiButtonActivateType.BUTTON_ACTIVATE_OVER);
			ui_do_button(C, but.block, but, event);
		}
	}

	return WmTypes.WM_UI_HANDLER_CONTINUE;
}

///* exported to interface.c: uiButActiveOnly() */
//void ui_button_activate_do(bContext *C, ARegion *ar, uiBut *but)
//{
//	wmWindow *win= CTX_wm_window(C);
//	wmEvent event;
//
//	button_activate_init(C, ar, but, BUTTON_ACTIVATE_OVER);
//
//	event= *(win.eventstate);	/* XXX huh huh? make api call */
//	event.type= EVT_BUT_OPEN;
//	event.val= KM_PRESS;
//	event.customdata= but;
//	event.customdatafree= FALSE;
//
//	ui_do_button(C, but.block, but, &event);
//}

static void ui_handle_button_activate(bContext C, ARegion ar, uiBut but, uiButtonActivateType type)
{
	uiBut oldbut;
	uiHandleButtonData data;

	oldbut= ui_but_find_activated(ar);
	if(oldbut!=null) {
		data= oldbut.active;
		data.cancel= 1;
		button_activate_exit(C, data, oldbut, false);
	}

	button_activate_init(C, ar, but, type);
}

/************ handle events for an activated button ***********/

static int ui_handle_button_event(bContext C, wmEvent event, uiBut but)
{
//        System.out.println("ui_handle_button_event");
	uiHandleButtonData data;
	uiBlock block;
	ARegion ar;
	uiBut postbut;
	uiButtonActivateType posttype;
	int retval;

	data= but.active;
	block= but.block;
	ar= data.region;

	retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	if(data.state == uiHandleButtonState.BUTTON_STATE_HIGHLIGHT) {
		switch(event.type) {

			case WmEventTypes.MOUSEMOVE:
				/* verify if we are still over the button, if not exit */
                                int[] x={event.x}, y={event.y};
                                boolean result = ui_mouse_inside_button(ar, but, x, y);
                                event.x=(short)x[0]; event.y=(short)y[0];
				if(!result) {
					data.cancel= 1;
					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				}
				else if(event.x!=event.prevx || event.y!=event.prevy) {
					/* re-enable tooltip on mouse move */
//					ui_blocks_set_tooltips(ar, 1);
//					button_tooltip_timer_reset(but);
				}

				break;
			case WmEventTypes.TIMER: //{
//				/* handle tooltip timer */
//				if(event.customdata == data.tooltiptimer) {
//					WM_event_remove_window_timer(data.window, data.tooltiptimer);
//					data.tooltiptimer= NULL;
//
//					if(!data.tooltip)
//						data.tooltip= ui_tooltip_create(C, data.region, but);
//				}
//				/* handle menu auto open timer */
//				else if(event.customdata == data.autoopentimer) {
//					WM_event_remove_window_timer(data.window, data.autoopentimer);
//					data.autoopentimer= NULL;
//
//					if(ui_mouse_inside_button(ar, but, event.x, event.y))
//						button_activate_state(C, but, BUTTON_STATE_MENU_OPEN);
//				}

				retval= WmTypes.WM_UI_HANDLER_CONTINUE;
				break;
			case WmEventTypes.WHEELUPMOUSE:
			case WmEventTypes.WHEELDOWNMOUSE:
			case WmEventTypes.MIDDLEMOUSE:
				/* XXX hardcoded keymap check... but anyway, while view changes, tooltips should be removed */
//				if(data.tooltiptimer) {
//					WM_event_remove_window_timer(data.window, data.tooltiptimer);
//					data.tooltiptimer= NULL;
//				}
				/* pass on purposedly */
			default:
				/* handle button type specific events */
				retval= ui_do_button(C, block, but, event);
//			}
		}
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_WAIT_RELEASE) {
		switch(event.type) {
			case WmEventTypes.MOUSEMOVE:

				if(but.type==UI.LINK || but.type==UI.INLINK) {
					but.flag |= UI.UI_SELECT;
					ui_do_button(C, block, but, event);
					Area.ED_region_tag_redraw(data.region);
				}
				else {
					/* deselect the button when moving the mouse away */
                                        int[] x={event.x}, y={event.y};
                                        boolean inside = ui_mouse_inside_button(ar, but, x, y);
                                        event.x=(short)x[0]; event.y=(short)y[0];
					if(inside) {
						if((but.flag & UI.UI_SELECT)==0) {
							but.flag |= UI.UI_SELECT;
							data.cancel= 0;
							Area.ED_region_tag_redraw(data.region);
						}
					}
					else {
						if((but.flag & UI.UI_SELECT)!=0) {
							but.flag &= ~UI.UI_SELECT;
							data.cancel= 1;
							Area.ED_region_tag_redraw(data.region);
						}
					}
				}
				break;
			default:
				/* otherwise catch mouse release event */
				ui_do_button(C, block, but, event);
				break;
		}

		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_WAIT_FLASH) {
		switch(event.type) {
			case WmEventTypes.TIMER: {
//				if(event.customdata == data.flashtimer)
//					button_activate_state(C, but, BUTTON_STATE_EXIT);
			}
		}

		retval= WmTypes.WM_UI_HANDLER_CONTINUE;
	}
	else if(data.state == uiHandleButtonState.BUTTON_STATE_MENU_OPEN) {
		switch(event.type) {
			case WmEventTypes.MOUSEMOVE: {
				uiBut bt= ui_but_find_mouse_over(ar, event.x, event.y);

				if(bt!=null && bt.active != data) {
					if(but.type != UI.COL) /* exception */
						data.cancel= 1;
					button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_EXIT);
				}
				break;
			}
		}

		ui_do_button(C, block, but, event);
		retval= WmTypes.WM_UI_HANDLER_CONTINUE;
	}
	else {
		ui_do_button(C, block, but, event);
		retval= WmTypes.WM_UI_HANDLER_BREAK;
	}

	if(data.state == uiHandleButtonState.BUTTON_STATE_EXIT) {
		postbut= data.postbut;
		posttype= data.posttype;

		button_activate_exit(C, data, but, (postbut == null));

		/* for jumping to the next button with tab while text editing */
		if(postbut!=null)
			button_activate_init(C, ar, postbut, posttype);
	}

	return retval;
}

static void ui_handle_button_return_submenu(bContext C, wmEvent event, uiBut but)
{
	uiHandleButtonData data;
	uiPopupBlockHandle menu;

	data= but.active;
	menu= data.menu;

	/* copy over return values from the closing menu */
	if(menu.menuretval == UI.UI_RETURN_OK || menu.menuretval == UI.UI_RETURN_UPDATE) {
		if(but.type == UI.COL)
			UtilDefines.VECCOPY(data.vec, menu.retvec);
		else if(but.type==UI.MENU || but.type==UI.ICONROW || but.type==UI.ICONTEXTROW)
			data.value= menu.retvalue;
	}

	if(menu.menuretval == UI.UI_RETURN_UPDATE) {
		if(data.interactive!=0) ui_apply_button(C, but.block, but, data, 1);
		else UI.ui_check_but(but);

		menu.menuretval= 0;
	}

	/* now change button state or exit, which will close the submenu */
	if(menu.menuretval==UI.UI_RETURN_OK || menu.menuretval==UI.UI_RETURN_CANCEL) {
		if(menu.menuretval != UI.UI_RETURN_OK)
			data.cancel= 1;

		button_activate_exit(C, data, but, true);
	}
	else if(menu.menuretval == UI.UI_RETURN_OUT) {
                boolean inside = false;
                if (event.type==WmEventTypes.MOUSEMOVE) {
                    int[] x={event.x}, y={event.y};
                    inside = ui_mouse_inside_button(data.region, but, x, y);
                    event.x=(short)x[0]; event.y=(short)y[0];
                }
		if(inside) {
			button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_HIGHLIGHT);
		}
		else {
			if(event.type != WmEventTypes.MOUSEMOVE) {
				but.active.used_mouse= 0;
				button_activate_state(C, but, uiHandleButtonState.BUTTON_STATE_HIGHLIGHT);
			}
			else {
				data.cancel= 1;
				button_activate_exit(C, data, but, true);
			}
		}
	}
}

/* ************************* menu handling *******************************/

/* function used to prevent loosing the open menu when using nested pulldowns,
 * when moving mouse towards the pulldown menu over other buttons that could
 * steal the highlight from the current button, only checks:
 *
 * - while mouse moves in triangular area defined old mouse position and
 *   left/right side of new menu
 * - only for 1 second
 */

static void ui_mouse_motion_towards_init(uiPopupBlockHandle menu, int mx, int my, boolean force)
{
	if(menu.dotowards==0 || force) {
		menu.dotowards= 1;
		menu.towardsx= mx;
		menu.towardsy= my;

		if(force)
			menu.towardstime= Double.MAX_VALUE; /* unlimited time */
		else
            menu.towardstime= Time.PIL_check_seconds_timer();
	}
}

static int ui_mouse_motion_towards_check(uiBlock block, uiPopupBlockHandle menu, int mx, int my)
{
	float[] p1=new float[2], p2=new float[2], p3=new float[2], p4=new float[2], oldp=new float[2], newp=new float[2];
	int closer;

	if(menu.dotowards==0) return 0;
	if((block.direction & UI.UI_TOP)!=0 || (block.direction & UI.UI_DOWN)!=0) {
		menu.dotowards= 0;
		return menu.dotowards;
	}

	/* verify that we are moving towards one of the edges of the
	 * menu block, in other words, in the triangle formed by the
	 * initial mouse location and two edge points. */
	p1[0]= block.minx-20;
	p1[1]= block.miny-20;

	p2[0]= block.maxx+20;
	p2[1]= block.miny-20;

	p3[0]= block.maxx+20;
	p3[1]= block.maxy+20;

	p4[0]= block.minx-20;
	p4[1]= block.maxy+20;

	oldp[0]= menu.towardsx;
	oldp[1]= menu.towardsy;

	newp[0]= mx;
	newp[1]= my;

	if(Arithb.Vec2Lenf(oldp, newp) < 4.0f)
		return menu.dotowards;

	closer= 0;
	closer |= Arithb.IsectPT2Df(newp, oldp, p1, p2);
	closer |= Arithb.IsectPT2Df(newp, oldp, p2, p3);
	closer |= Arithb.IsectPT2Df(newp, oldp, p3, p4);
	closer |= Arithb.IsectPT2Df(newp, oldp, p4, p1);

	if(closer==0)
		menu.dotowards= 0;

	/* 1 second timer */
	if(Time.PIL_check_seconds_timer() - menu.towardstime > BUTTON_MOUSE_TOWARDS_THRESH)
		menu.dotowards= 0;

	return menu.dotowards;
}

public static int ui_handle_menu_event(bContext C, wmEvent event, uiPopupBlockHandle menu, boolean topmenu)
{
	ARegion ar;
	uiBlock block;
	uiBut but, bt;
	int inside, act, count, retval;
        int[] mx={0}, my={0};

	ar= menu.region;
	block= (uiBlock)ar.uiblocks.first;

	act= 0;
	retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	mx[0]= event.x;
	my[0]= event.y;
	UI.ui_window_to_block(ar, block, mx, my);

	/* check if mouse is inside block */
	inside= 0;
	if(block.minx <= mx[0] && block.maxx >= mx[0])
		if(block.miny <= my[0] && block.maxy >= my[0])
			inside= 1;

	/* if there's an active modal button, don't check events or outside, except for search menu */
	but= ui_but_find_activated(ar);
	if(but!=null && button_modal_state(but.active.state) && but.type!=UI.SEARCH_MENU) {
		/* if a button is activated modal, always reset the start mouse
		 * position of the towards mechanism to avoid loosing focus,
		 * and don't handle events */
		ui_mouse_motion_towards_init(menu, mx[0], my[0], true);
	}
	else if(event.type != WmEventTypes.TIMER) {
		/* for ui_mouse_motion_towards_block */
		if(event.type == WmEventTypes.MOUSEMOVE)
			ui_mouse_motion_towards_init(menu, mx[0], my[0], false);

		/* first block own event func */
//		if(block.block_event_func!=null && block.block_event_func.run(C, block, event));
		/* events not for active search menu button */
//		else if(but==NULL || but.type!=SEARCH_MENU) {
                if(but==null || but.type!=UI.SEARCH_MENU) {
			switch(event.type) {
				/* closing sublevels of pulldowns */
				case WmEventTypes.LEFTARROWKEY:
					if(event.val==WmTypes.KM_PRESS && (block.flag & UI.UI_BLOCK_LOOP)!=0)
						if(ListBaseUtil.BLI_countlist(block.saferct) > 0)
							menu.menuretval= UI.UI_RETURN_OUT;

					retval= WmTypes.WM_UI_HANDLER_BREAK;
					break;

				/* opening sublevels of pulldowns */
				case WmEventTypes.RIGHTARROWKEY:
					if(event.val==WmTypes.KM_PRESS && (block.flag & UI.UI_BLOCK_LOOP)!=0) {
						but= ui_but_find_activated(ar);

						if(but==null) {
							/* no item active, we make first active */
							if((block.direction & UI.UI_TOP)!=0) but= ui_but_last(block);
							else but= ui_but_first(block);
						}

						if(but!=null && (but.type==UI.BLOCK || but.type==UI.PULLDOWN))
							ui_handle_button_activate(C, ar, but, uiButtonActivateType.BUTTON_ACTIVATE_OPEN);
					}

					retval= WmTypes.WM_UI_HANDLER_BREAK;
					break;

				case WmEventTypes.UPARROWKEY:
				case WmEventTypes.DOWNARROWKEY:
				case WmEventTypes.WHEELUPMOUSE:
				case WmEventTypes.WHEELDOWNMOUSE:
					/* arrowkeys: only handle for block_loop blocks */
					if(inside!=0 || (block.flag & UI.UI_BLOCK_LOOP)!=0) {
						if(event.val==WmTypes.KM_PRESS) {
							but= ui_but_find_activated(ar);
							if(but!=null) {
								if(event.type==WmEventTypes.DOWNARROWKEY || event.type==WmEventTypes.WHEELDOWNMOUSE) {
									if((block.direction & UI.UI_TOP)!=0) but= ui_but_prev(but);
									else but= ui_but_next(but);
								}
								else {
									if((block.direction & UI.UI_TOP)!=0) but= ui_but_next(but);
									else but= ui_but_prev(but);
								}

								if(but!=null)
									ui_handle_button_activate(C, ar, but, uiButtonActivateType.BUTTON_ACTIVATE);
							}

							if(but==null) {
								if(event.type==WmEventTypes.UPARROWKEY || event.type==WmEventTypes.WHEELUPMOUSE) {
									if((block.direction & UI.UI_TOP)!=0) bt= ui_but_first(block);
									else bt= ui_but_last(block);
								}
								else {
									if((block.direction & UI.UI_TOP)!=0) bt= ui_but_last(block);
									else bt= ui_but_first(block);
								}

								if(bt!=null)
									ui_handle_button_activate(C, ar, bt, uiButtonActivateType.BUTTON_ACTIVATE);
							}
						}
					}

					retval= WmTypes.WM_UI_HANDLER_BREAK;
					break;

				case WmEventTypes.ONEKEY: 	case WmEventTypes.PAD1:
					act= 1;
				case WmEventTypes.TWOKEY: 	case WmEventTypes.PAD2:
					if(act==0) act= 2;
				case WmEventTypes.THREEKEY: 	case WmEventTypes.PAD3:
					if(act==0) act= 3;
				case WmEventTypes.FOURKEY: 	case WmEventTypes.PAD4:
					if(act==0) act= 4;
				case WmEventTypes.FIVEKEY: 	case WmEventTypes.PAD5:
					if(act==0) act= 5;
				case WmEventTypes.SIXKEY: 	case WmEventTypes.PAD6:
					if(act==0) act= 6;
				case WmEventTypes.SEVENKEY: 	case WmEventTypes.PAD7:
					if(act==0) act= 7;
				case WmEventTypes.EIGHTKEY: 	case WmEventTypes.PAD8:
					if(act==0) act= 8;
				case WmEventTypes.NINEKEY: 	case WmEventTypes.PAD9:
					if(act==0) act= 9;
				case WmEventTypes.ZEROKEY: 	case WmEventTypes.PAD0:
					if(act==0) act= 10;

//					if(block.flag & UI_BLOCK_NUMSELECT) {
//						if(event.alt) act+= 10;
//
//						count= 0;
//						for(but= block.buttons.first; but; but= but.next) {
//							int doit= 0;
//
//							if(but.type!=LABEL && but.type!=SEPR)
//								count++;
//
//							/* exception for menus like layer buts, with button aligning they're not drawn in order */
//							if(but.type==TOGR) {
//								if(but.bitnr==act-1)
//									doit= 1;
//							}
//							else if(count==act)
//								doit=1;
//
//							if(doit) {
//								ui_handle_button_activate(C, ar, but, BUTTON_ACTIVATE_APPLY);
//								break;
//							}
//						}
//					}

					retval= WmTypes.WM_UI_HANDLER_BREAK;
					break;
			}
		}

		/* here we check return conditions for menus */
		if((block.flag & UI.UI_BLOCK_LOOP)!=0) {
			/* if we click outside the block, verify if we clicked on the
			 * button that opened us, otherwise we need to close */
			if(inside==0) {
				uiSafetyRct saferct= (uiSafetyRct)block.saferct.first;

				if((event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.MIDDLEMOUSE || event.type==WmEventTypes.RIGHTMOUSE) && event.val==WmTypes.KM_PRESS)
					if(saferct!=null && !Rct.BLI_in_rctf(saferct.parent, event.x, event.y))
						menu.menuretval= UI.UI_RETURN_OK;
			}

			if(menu.menuretval!=0);
			else if(event.type==WmEventTypes.ESCKEY && event.val==WmTypes.KM_PRESS) {
				/* esc cancels this and all preceding menus */
				menu.menuretval= UI.UI_RETURN_CANCEL;
			}
			else if((event.type==WmEventTypes.RETKEY || event.type==WmEventTypes.PADENTER) && event.val==WmTypes.KM_PRESS) {
				/* enter will always close this block, but we let the event
				 * get handled by the button if it is activated */
				if(ui_but_find_activated(ar)==null)
					menu.menuretval= UI.UI_RETURN_OK;
			}
			else {
				ui_mouse_motion_towards_check(block, menu, mx[0], my[0]);

				/* check mouse moving outside of the menu */
				if(inside==0 && (block.flag & UI.UI_BLOCK_MOVEMOUSE_QUIT)!=0) {
					uiSafetyRct saferct;

					/* check for all parent rects, enables arrowkeys to be used */
					for(saferct=(uiSafetyRct)block.saferct.first; saferct!=null; saferct= saferct.next) {
						/* for mouse move we only check our own rect, for other
						 * events we check all preceding block rects too to make
						 * arrow keys navigation work */
						if(event.type!=WmEventTypes.MOUSEMOVE || saferct==block.saferct.first) {
							if(Rct.BLI_in_rctf(saferct.parent, (float)event.x, (float)event.y))
								break;
							if(Rct.BLI_in_rctf(saferct.safety, (float)event.x, (float)event.y))
								break;
						}
					}

					/* strict check, and include the parent rect */
					if(menu.dotowards==0 && saferct==null) {
						if((block.flag & UI.UI_BLOCK_OUT_1)!=0)
							menu.menuretval= UI.UI_RETURN_OK;
						else
							menu.menuretval= (block.flag & UI.UI_BLOCK_KEEP_OPEN)!=0? UI.UI_RETURN_OK: UI.UI_RETURN_OUT;
					}
					else if(menu.dotowards!=0 && event.type==WmEventTypes.MOUSEMOVE)
						retval= WmTypes.WM_UI_HANDLER_BREAK;
				}
			}
		}
	}

	/* if we are didn't handle the event yet, lets pass it on to
	 * buttons inside this region. disabled inside check .. not sure
	 * anymore why it was there? but i meant enter enter didn't work
	 * for example when mouse was not over submenu */
	if((/*inside &&*/ (menu.menuretval==0 || menu.menuretval == UI.UI_RETURN_UPDATE) && retval == WmTypes.WM_UI_HANDLER_CONTINUE) || event.type == WmEventTypes.TIMER) {
		but= ui_but_find_activated(ar);

		if(but!=null) {
			ScrArea ctx_area= bContext.CTX_wm_area(C);
			ARegion ctx_region= bContext.CTX_wm_region(C);

			if(menu.ctx_area!=null) bContext.CTX_wm_area_set(C, menu.ctx_area);
			if(menu.ctx_region!=null) bContext.CTX_wm_region_set(C, menu.ctx_region);

			retval= ui_handle_button_event(C, event, but);

			if(menu.ctx_area!=null) bContext.CTX_wm_area_set(C, ctx_area);
			if(menu.ctx_region!=null) bContext.CTX_wm_region_set(C, ctx_region);
		}
		else
			retval= ui_handle_button_over(C, event, ar);
	}

	/* if we set a menu return value, ensure we continue passing this on to
	 * lower menus and buttons, so always set continue then, and if we are
	 * inside the region otherwise, ensure we swallow the event */
	if(menu.menuretval!=0)
		return WmTypes.WM_UI_HANDLER_CONTINUE;
	else if(inside!=0)
		return WmTypes.WM_UI_HANDLER_BREAK;
	else
		return retval;
}

static int ui_handle_menu_return_submenu(bContext C, wmEvent event, uiPopupBlockHandle menu)
{
	ARegion ar;
	uiBut but;
	uiBlock block;
	uiHandleButtonData data;
	uiPopupBlockHandle submenu;
	int[] mx={0}, my={0};
        boolean update;

	ar= menu.region;
	block= (uiBlock)ar.uiblocks.first;

	but= ui_but_find_activated(ar);
	data= but.active;
	submenu= data.menu;

	if(submenu.menuretval!=0) {
		/* first decide if we want to close our own menu cascading, if
		 * so pass on the sub menu return value to our own menu handle */
		if(submenu.menuretval==UI.UI_RETURN_OK || submenu.menuretval==UI.UI_RETURN_CANCEL) {
			if((block.flag & UI.UI_BLOCK_KEEP_OPEN)==0) {
				menu.menuretval= submenu.menuretval;
				menu.butretval= data.retval;
			}
		}

		update= (submenu.menuretval == UI.UI_RETURN_UPDATE);
		if(update)
			menu.menuretval = UI.UI_RETURN_UPDATE;

		/* now let activated button in this menu exit, which
		 * will actually close the submenu too */
		ui_handle_button_return_submenu(C, event, but);

		if(update)
			submenu.menuretval = 0;
	}

	/* for cases where close does not cascade, allow the user to
	 * move the mouse back towards the menu without closing */
	mx[0]= event.x;
	my[0]= event.y;
	UI.ui_window_to_block(ar, block, mx, my);
	ui_mouse_motion_towards_init(menu, mx[0], my[0], true);

	if(menu.menuretval!=0)
		return WmTypes.WM_UI_HANDLER_CONTINUE;
	else
		return WmTypes.WM_UI_HANDLER_BREAK;
}

static int ui_handle_menus_recursive(bContext C, wmEvent event, uiPopupBlockHandle menu)
{
	uiBut but;
	uiHandleButtonData data;
	uiPopupBlockHandle submenu;
	int retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	/* check if we have a submenu, and handle events for it first */
	but= ui_but_find_activated(menu.region);
	data= (but!=null)? but.active: null;
	submenu= (data!=null)? data.menu: null;

	if(submenu!=null)
		retval= ui_handle_menus_recursive(C, event, submenu);

	/* now handle events for our own menu */
	if(retval == WmTypes.WM_UI_HANDLER_CONTINUE || event.type == WmEventTypes.TIMER) {
		if(submenu!=null && submenu.menuretval!=0)
			retval= ui_handle_menu_return_submenu(C, event, menu);
		else
			retval= ui_handle_menu_event(C, event, menu, (submenu == null));
	}

	return retval;
}

/* *************** UI event handlers **************** */

public static WmUIHandlerFunc ui_handler_region = new WmUIHandlerFunc() {
public int run(bContext C, wmEvent event, Object userdata)
//static int ui_handler_region(bContext *C, wmEvent *event, void *userdata)
{
//        if (event.type == WmEventTypes.LEFTMOUSE) {
//            System.out.println("ui_handler_region");
//        }
	
	ARegion ar;
	uiBut but;
	int retval;

	/* here we handle buttons at the region level, non-modal */
	ar= bContext.CTX_wm_region(C);
	retval= WmTypes.WM_UI_HANDLER_CONTINUE;

	if(ar==null) return retval;
	if(ar.uiblocks.first==null) return retval;
	
//	if (event.type == WmEventTypes.MOUSEMOVE && ar.regiontype == ScreenTypes.RGN_TYPE_HEADER) {
//		System.out.printf("ui_handler_region: %d,%d\n", event.x, event.y);
//	}

	/* either handle events for already activated button or try to activate */
	but= ui_but_find_activated(ar);

//        if (but==null) {
//            System.out.println("ui_but_find_activated no active set");
//        }

	if(but==null || !button_modal_state(but.active.state))
		retval= UIPanel.ui_handler_panel_region(C, event);

	if(retval == WmTypes.WM_UI_HANDLER_CONTINUE) {
		if(but!=null)
			retval= ui_handle_button_event(C, event, but);
		else
			retval= ui_handle_button_over(C, event, ar);
	}

	/* re-enable tooltips */
	if(event.type == WmEventTypes.MOUSEMOVE && (event.x!=event.prevx || event.y!=event.prevy))
		ui_blocks_set_tooltips(ar, 1);

	/* delayed apply callbacks */
	ui_apply_but_funcs_after(C);

	return retval;
}};

public static WmUIHandlerRemoveFunc ui_handler_remove_region = new WmUIHandlerRemoveFunc() {
public void run(bContext C, Object userdata)
//static void ui_handler_remove_region(bContext *C, void *userdata)
{
	bScreen sc;
	ARegion ar;

	ar= bContext.CTX_wm_region(C);
	if(ar == null) return;

	UI.uiFreeBlocks(C, ar.uiblocks);

	sc= bContext.CTX_wm_screen(C);
	if(sc == null) return;

	/* delayed apply callbacks, but not for screen level regions, those
	 * we rather do at the very end after closing them all, which will
	 * be done in ui_handler_region/window */
	if(ListBaseUtil.BLI_findindex(sc.regionbase, ar) == -1)
		ui_apply_but_funcs_after(C);
}};

public static WmUIHandlerFunc ui_handler_region_menu = new WmUIHandlerFunc() {
public int run(bContext C, wmEvent event, Object userdata)
//static int ui_handler_region_menu(bContext *C, wmEvent *event, void *userdata)
{
	ARegion ar;
	uiBut but;
	uiHandleButtonData data;
	int retval;

	/* here we handle buttons at the window level, modal, for example
	 * while number sliding, text editing, or when a menu block is open */
	ar= bContext.CTX_wm_menu(C);
	if(ar==null)
		ar= bContext.CTX_wm_region(C);

	but= ui_but_find_activated(ar);

	if(but!=null) {
		/* handle activated button events */
		data= but.active;

		if(data.state == uiHandleButtonState.BUTTON_STATE_MENU_OPEN) {
			/* handle events for menus and their buttons recursively,
			 * this will handle events from the top to the bottom menu */
			retval= ui_handle_menus_recursive(C, event, data.menu);

			/* handle events for the activated button */
			if(retval == WmTypes.WM_UI_HANDLER_CONTINUE || event.type == WmEventTypes.TIMER) {
				if(data.menu.menuretval!=0)
					ui_handle_button_return_submenu(C, event, but);
				else
					ui_handle_button_event(C, event, but);
			}
		}
		else {
			/* handle events for the activated button */
			ui_handle_button_event(C, event, but);
		}
	}

	/* re-enable tooltips */
//	if(event.type == WmEventTypes.MOUSEMOVE && (event.x!=event.prevx || event.y!=event.prevy))
//		ui_blocks_set_tooltips(ar, 1);

	/* delayed apply callbacks */
	ui_apply_but_funcs_after(C);

	/* we block all events, this is modal interaction */
	return WmTypes.WM_UI_HANDLER_BREAK;
}};

/* two types of popups, one with operator + enum, other with regular callbacks */
public static WmUIHandlerFunc ui_handler_popup = new WmUIHandlerFunc() {
public int run(bContext C, wmEvent event, Object userdata)
//static int ui_handler_popup(bContext *C, wmEvent *event, void *userdata)
{
	uiPopupBlockHandle menu= (uiPopupBlockHandle)userdata;

	ui_handle_menus_recursive(C, event, menu);

	/* free if done, does not free handle itself */
	if(menu.menuretval!=0) {
		/* copy values, we have to free first (closes region) */
//		uiPopupBlockHandle temp= *menu;
                uiPopupBlockHandle temp= new uiPopupBlockHandle();
                temp.set(menu);

		UIRegions.ui_popup_block_free(C, menu);
		WmEventSystem.WM_event_remove_ui_handler(bContext.CTX_wm_window(C).handlers, ui_handler_popup, ui_handler_remove_popup, menu);

		if(temp.menuretval == UI.UI_RETURN_OK) {
			if(temp.popup_func!=null)
				temp.popup_func.run(C, temp.popup_arg, (int)temp.retvalue);
			if(temp.optype!=null)
				WmEventSystem.WM_operator_name_call(C, temp.optype.idname, temp.opcontext, null);
		}
		else if(temp.cancel_func!=null)
			temp.cancel_func.run(temp.popup_arg);
	}
//	else {
//		/* re-enable tooltips */
//		if(event.type == MOUSEMOVE && (event.x!=event.prevx || event.y!=event.prevy))
//			ui_blocks_set_tooltips(menu.region, 1);
//	}

	/* delayed apply callbacks */
	ui_apply_but_funcs_after(C);

	/* we block all events, this is modal interaction */
	return WmTypes.WM_UI_HANDLER_BREAK;
}};

public static WmUIHandlerRemoveFunc ui_handler_remove_popup = new WmUIHandlerRemoveFunc() {
public void run(bContext C, Object userdata)
//static void ui_handler_remove_popup(bContext *C, void *userdata)
{
	uiPopupBlockHandle menu= (uiPopupBlockHandle)userdata;

	/* free menu block if window is closed for some reason */
	UIRegions.ui_popup_block_free(C, menu);

	/* delayed apply callbacks */
	ui_apply_but_funcs_after(C);
}};

public static void UI_add_region_handlers(ListBase handlers)
{
//        System.out.println("UI_add_region_handlers");
	WmEventSystem.WM_event_remove_ui_handler(handlers, ui_handler_region, ui_handler_remove_region, null);
	WmEventSystem.WM_event_add_ui_handler(null, handlers, ui_handler_region, ui_handler_remove_region, null);
}

public static void UI_add_popup_handlers(bContext C, ListBase handlers, uiPopupBlockHandle menu)
{
	WmEventSystem.WM_event_add_ui_handler(C, handlers, ui_handler_popup, ui_handler_remove_popup, menu);
}

}
