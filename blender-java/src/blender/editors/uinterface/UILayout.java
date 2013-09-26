/**
 * $Id: UILayout.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation 2009.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

//#include <limits.h>

import static blender.blenkernel.Blender.G;
import blender.blenkernel.Pointer;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.Menu;
import blender.blenkernel.ScreenUtil.MenuType;
import blender.blenkernel.bContext.bContextStore;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UI.uiHandleFunc;
import blender.editors.uinterface.UI.uiMenuCreateFunc;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.uiStyle;
import blender.makesrna.RNATypes;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.windowmanager.Wm;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmTypes;

//#include <math.h>
//#include <stdlib.h>
//#include <string.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_ID.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "BLI_listbase.h"
//#include "BLI_string.h"
//
//#include "BKE_context.h"
//#include "BKE_global.h"
//#include "BKE_idprop.h"
//#include "BKE_library.h"
//#include "BKE_screen.h"
//#include "BKE_utildefines.h"
//
//#include "RNA_access.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"
//#include "UI_view2d.h"
//
//#include "ED_util.h"
//#include "ED_types.h"
//#include "ED_screen.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "interface_intern.h"
//
public class UILayout {
/************************ Structs and Defines *************************/

public static final int RNA_NO_INDEX=	-1;
public static final int RNA_ENUM_VALUE=	-2;

public static final int EM_SEPR_X=		6;
public static final int EM_SEPR_Y=		6;

/* uiLayoutRoot */

public static class uiLayoutRoot extends Link<uiLayoutRoot> {
	public int type;
	public int opcontext;

	public int emw, emh;

//	public uiMenuHandleFunc handlefunc;
//	public void *argv;

	public uiStyle style;
	public uiBlock block;
	public uiLayout layout;
};

/* Item */

public static enum uiItemType {
//	ITEM_BUTTON,
//
//	ITEM_LAYOUT_ROW,
//	ITEM_LAYOUT_COLUMN,
//	ITEM_LAYOUT_COLUMN_FLOW,
//	ITEM_LAYOUT_ROW_FLOW,
//	ITEM_LAYOUT_BOX,
//	ITEM_LAYOUT_FREE,
//	ITEM_LAYOUT_SPLIT,
//
//	ITEM_LAYOUT_ROOT
	
	ITEM_BUTTON,

	ITEM_LAYOUT_ROW,
	ITEM_LAYOUT_COLUMN,
	ITEM_LAYOUT_COLUMN_FLOW,
	ITEM_LAYOUT_ROW_FLOW,
	ITEM_LAYOUT_BOX,
	ITEM_LAYOUT_ABSOLUTE,
	ITEM_LAYOUT_SPLIT,
	ITEM_LAYOUT_OVERLAP,

	ITEM_LAYOUT_ROOT
//#if 0
//	TEMPLATE_COLUMN_FLOW,
//	TEMPLATE_SPLIT,
//	TEMPLATE_BOX,
//
//	TEMPLATE_HEADER,
//	TEMPLATE_HEADER_ID
//#endif
};

public static class uiItem extends Link<uiItem> {
	public uiItemType type;
	public int flag;
};

public static class uiButtonItem extends uiItem {
	public uiItem item = this;
	public uiBut but;
};

public static class uiLayout extends uiItem {
	public uiItem item = this;

	public uiLayoutRoot root;
	public bContextStore context;
	public ListBase<uiItem> items = new ListBase<uiItem>();

	public int x, y, w, h;
	public float[] scale=new float[2];
	public short space;
	public char align;
	public char active;
	public char enabled;
	public char redalert;
	public char keepaspect;
	public char alignment;
};

public static class uiLayoutItemFlow extends uiLayout {
	public uiLayout litem = this;
	public int number;
	public int totcol;
};

public static class uiLayoutItemBx extends uiLayout {
	public uiLayout litem = this;
	public uiBut roundbox;
//	public ListBase items = new ListBase();
};

public static class uiLayoutItemSplt extends uiLayout {
	public uiLayout litem = this;
	public float percentage;
};

//typedef struct uiLayoutItemRoot {
//	uiLayout litem;
//} uiLayoutItemRoot;

/************************** Item ***************************/

public static String ui_item_name_add_colon(String name)
{
//	int len= strlen(name);

//	if(len != 0 && len+1 < UI_MAX_NAME_STR) {
	if(name != null && !name.isEmpty()) {
//		BLI_strncpy(namestr, name, UI_MAX_NAME_STR);
//		namestr[len]= ':';
//		namestr[len+1]= '\0';
//		return namestr;
		return name + ":";
	}

	return name;
}

static int ui_item_fit(int item, int pos, int all, int available, boolean last, int alignment, int[] offset)
{
	/* available == 0 is unlimited */
	if(available == 0)
		return item;

	if(offset!=null)
		offset[0]= 0;

	if(all > available) {
		/* contents is bigger than available space */
		if(last)
			return available-pos;
		else
			return (item*available)/all;
	}
	else {
		/* contents is smaller or equal to available space */
		if(alignment == UI.UI_LAYOUT_ALIGN_EXPAND) {
			if(last)
				return available-pos;
			else
				return (item*available)/all;
		}
		else
			return item;
	}
}

/* variable button size in which direction? */
public static final int UI_ITEM_VARY_X=	1;
public static final int UI_ITEM_VARY_Y=	2;

static int ui_layout_vary_direction(uiLayout layout)
{
	return (layout.root.type == UI.UI_LAYOUT_HEADER || layout.alignment != UI.UI_LAYOUT_ALIGN_EXPAND)? UI_ITEM_VARY_X: UI_ITEM_VARY_Y;
}

/* estimated size of text + icon */
static int ui_text_icon_width(uiLayout layout, byte[] name, int icon, int compact)
{
	boolean variable = ui_layout_vary_direction(layout) == UI_ITEM_VARY_X;

	if(icon!=0 && name[0]==0)
		return UI.UI_UNIT_X; /* icon only */
	else if(icon!=0)
		return (variable)? UIStyle.UI_GetStringWidth(name,0) + (compact!=0? 5: 10) + UI.UI_UNIT_X: 10*UI.UI_UNIT_X; /* icon + text */
	else
		return (variable)? UIStyle.UI_GetStringWidth(name,0) + (compact!=0? 5: 10) + UI.UI_UNIT_X: 10*UI.UI_UNIT_X; /* text only */
}

static void ui_item_size(uiItem item, int[] r_w, int[] r_h)
{
	if(item.type == uiItemType.ITEM_BUTTON) {
		uiButtonItem bitem= (uiButtonItem)item;

		if(r_w!=null) r_w[0]= (int)(bitem.but.x2 - bitem.but.x1);
		if(r_h!=null) r_h[0]= (int)(bitem.but.y2 - bitem.but.y1);
	}
	else {
		uiLayout litem= (uiLayout)item;

		if(r_w!=null) r_w[0]= litem.w;
		if(r_h!=null) r_h[0]= litem.h;
	}
}

static void ui_item_offset(uiItem item, int[] r_x, int[] r_y)
{
	if(item.type == uiItemType.ITEM_BUTTON) {
		uiButtonItem bitem= (uiButtonItem)item;

		if(r_x!=null) r_x[0]= (int)bitem.but.x1;
		if(r_y!=null) r_y[0]= (int)bitem.but.y1;
	}
	else {
		if(r_x!=null) r_x[0]= 0;
		if(r_y!=null) r_y[0]= 0;
	}
}

static void ui_item_position(uiItem item, int x, int y, int w, int h)
{
	if(item.type == uiItemType.ITEM_BUTTON) {
		uiButtonItem bitem= (uiButtonItem)item;

		bitem.but.x1= x;
		bitem.but.y1= y;
		bitem.but.x2= x+w;
		bitem.but.y2= y+h;

		UI.ui_check_but(bitem.but); /* for strlen */
	}
	else {
		uiLayout litem= (uiLayout)item;

		litem.x= x;
		litem.y= y+h;
		litem.w= w;
		litem.h= h;
	}
}

/******************** Special RNA Items *********************/

public static int ui_layout_local_dir(uiLayout layout)
{
	switch(layout.item.type) {
		case ITEM_LAYOUT_ROW:
		case ITEM_LAYOUT_ROOT:
		case ITEM_LAYOUT_OVERLAP:
			return UI.UI_LAYOUT_HORIZONTAL;
		case ITEM_LAYOUT_COLUMN:
		case ITEM_LAYOUT_COLUMN_FLOW:
		case ITEM_LAYOUT_SPLIT:
		case ITEM_LAYOUT_ABSOLUTE:
		case ITEM_LAYOUT_BOX:
		default:
			return UI.UI_LAYOUT_VERTICAL;
	}
}

public static uiLayout ui_item_local_sublayout(uiLayout test, uiLayout layout, int align)
{
	uiLayout sub;

	if(ui_layout_local_dir(test) == UI.UI_LAYOUT_HORIZONTAL)
		sub= uiLayoutRow(layout, align);
	else
		sub= uiLayoutColumn(layout, align);

	sub.space= 0;
	return sub;
}

public static uiHandleFunc ui_layer_but_cb = new uiHandleFunc() {
public void run(bContext C, Object arg_but, Object arg_index)
//static void ui_layer_but_cb(bContext *C, void *arg_but, void *arg_index)
{
//	wmWindow *win= CTX_wm_window(C);
//	uiBut *but= arg_but, *cbut;
//	PointerRNA *ptr= &but->rnapoin;
//	PropertyRNA *prop= but->rnaprop;
//	int i, index= GET_INT_FROM_POINTER(arg_index);
//	int shift= win->eventstate->shift;
//	int len= RNA_property_array_length(ptr, prop);
//
//	if(!shift) {
//		RNA_property_boolean_set_index(ptr, prop, index, 1);
//
//		for(i=0; i<len; i++)
//			if(i != index)
//				RNA_property_boolean_set_index(ptr, prop, i, 0);
//
//		RNA_property_update(C, ptr, prop);
//
//		for(cbut=but->block->buttons.first; cbut; cbut=cbut->next)
//			ui_check_but(cbut);
//	}
}};

/* create buttons for an item with an RNA array */
public static void ui_item_array(uiLayout layout, uiBlock block, String name, int icon, PointerRNA ptr, PropertyRNA prop, int len, int x, int y, int w, int h, int expand, int slider, int toggle, int icon_only)
{
	uiStyle style= layout.root.style;
	uiBut but;
	int type;
	int subtype;
	uiLayout sub;
	int a, b;

	/* retrieve type and subtype */
	type= RnaAccess.RNA_property_type(prop);
	subtype= RnaAccess.RNA_property_subtype(prop);

	sub= ui_item_local_sublayout(layout, layout, 1);
	uiBlockSetCurLayout(block, sub);

	/* create label */
	if(!name.equals(""))
		UI.uiDefBut(block, UI.LABEL, 0, name, 0, 0, w, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");

	/* create buttons */
	if(type == RNATypes.PROP_BOOLEAN && (subtype == RNATypes.PROP_LAYER || subtype == RNATypes.PROP_LAYER_MEMBER)) {
//		/* special check for layer layout */
//		int butw, buth, unit;
//		int cols= (len >= 20)? 2: 1;
//		int colbuts= len/(2*cols);
//		int layer_used= 0;
//
//		uiBlockSetCurLayout(block, uiLayoutAbsolute(layout, 0));
//
//		unit= (int)(UI.UI_UNIT_X*0.75);
//		butw= unit;
//		buth= unit;
//		
////		if(ptr.type == &RNA_Armature) {
////			bArmature *arm= (bArmature *)ptr->data;
////			layer_used= arm->layer_used;
////		}
//
//		for(b=0; b<cols; b++) {
//			UI.uiBlockBeginAlign(block);
//
//			for(a=0; a<colbuts; a++) {
//				if((layer_used & (1<<(a+b*colbuts)))!=0) icon= BIFIconID.ICON_LAYER_USED.ordinal();
//				else icon= BIFIconID.ICON_BLANK1.ordinal();
//					
//				but= UIUtils.uiDefAutoButR(block, ptr, prop, a+b*colbuts, "", icon, x + butw*a, y+buth, butw, buth);
//				if(subtype == RNATypes.PROP_LAYER_MEMBER)
////					UI.uiButSetFunc(but, ui_layer_but_cb, but, SET_INT_IN_POINTER(a+b*colbuts));
//					UI.uiButSetFunc(but, ui_layer_but_cb, but, new Integer(a+b*colbuts));
//			}
//			for(a=0; a<colbuts; a++) {
//				if((layer_used & (1<<(a+len/2+b*colbuts)))!=0) icon= BIFIconID.ICON_LAYER_USED.ordinal();
//				else icon= BIFIconID.ICON_BLANK1.ordinal();
//				
//				but= UIUtils.uiDefAutoButR(block, ptr, prop, a+len/2+b*colbuts, "", icon, x + butw*a, y, butw, buth);
//				if(subtype == RNATypes.PROP_LAYER_MEMBER)
////					UI.uiButSetFunc(but, ui_layer_but_cb, but, SET_INT_IN_POINTER(a+len/2+b*colbuts));
//					UI.uiButSetFunc(but, ui_layer_but_cb, but, new Integer(a+len/2+b*colbuts));
//			}
//			UI.uiBlockEndAlign(block);
//
//			x += colbuts*butw + style.buttonspacex;
//		}
	}
	else if(subtype == RNATypes.PROP_MATRIX) {
//		int totdim;
//		int[] dim_size=new int[3];	/* 3 == RNA_MAX_ARRAY_DIMENSION */
//		int row, col;
//
//		uiBlockSetCurLayout(block, uiLayoutAbsolute(layout, 1));
//
//		totdim= RnaAccess.RNA_property_array_dimension(ptr, prop, dim_size);
//		if (totdim != 2) return;	/* only 2D matrices supported in UI so far */
//		
//		w /= dim_size[0];
//		h /= dim_size[1];
//
//		for(a=0; a<len; a++) {
//			col= a % dim_size[0];
//			row= a / dim_size[0];
//			
//			but= UIUtils.uiDefAutoButR(block, ptr, prop, a, "", UI.ICON_NULL, x + w*col, y+(dim_size[1]*UI.UI_UNIT_Y)-(row*UI.UI_UNIT_Y), w, UI.UI_UNIT_Y);
//			if(slider!=0 && but.type==UI.NUM)
//				but.type= UI.NUMSLI;
//		}
	}
	else {
		if((subtype == RNATypes.PROP_COLOR || subtype == RNATypes.PROP_COLOR_GAMMA) && expand==0) {
//			UIUtils.uiDefAutoButR(block, ptr, prop, -1, "", UI.ICON_NULL, 0, 0, w, UI.UI_UNIT_Y);
		}

		if(!(subtype == RNATypes.PROP_COLOR || subtype == RNATypes.PROP_COLOR_GAMMA) || expand!=0) {
			/* layout for known array subtypes */
			byte[] str = new byte[3];

			for(a=0; a<len; a++) {
				str[0]= RnaAccess.RNA_property_array_item_char(prop, a);

				if(str[0]!=0) {
					if (icon_only!=0) {
						str[0] = '\0';
					}
					else if(type == RNATypes.PROP_BOOLEAN) {
						str[1]= '\0';
					}
					else {
						str[1]= ':';
						str[2]= '\0';
					}
				}

				but= UIUtils.uiDefAutoButR(block, ptr, prop, a, StringUtil.toJString(str,0), icon, 0, 0, w, UI.UI_UNIT_Y);
				if(slider!=0 && but.type==UI.NUM)
					but.type= UI.NUMSLI;
				if(toggle!=0 && but.type==UI.OPTION)
					but.type= UI.TOG;
			}
		}
	}

	uiBlockSetCurLayout(block, layout);
}

public static void ui_item_enum_expand(uiLayout layout, uiBlock block, PointerRNA ptr, PropertyRNA prop, String uiname, int h, int icon_only)
{
	uiBut but;
	EnumPropertyItem[][] item = new EnumPropertyItem[1][];
	String identifier;
	String name;
	int[] totitem={0}, free={0};
	int a, itemw, icon, value;

	identifier= RnaAccess.RNA_property_identifier(prop);
	RnaAccess.RNA_property_enum_items((bContext)block.evil_C, ptr, prop, item, totitem, free);

	uiBlockSetCurLayout(block, ui_item_local_sublayout(layout, layout, 1));
	for(a=0; a<totitem[0]; a++) {
//		if(!item[a].identifier[0])
//		if(item[0][a].identifier == null || item[0][a].identifier.equals(""))
		if(StringUtil.toCString(item[0][a].identifier)[0]==0)
			continue;

//		name= (!uiname || uiname[0])? (char*)item[a].name: "";
//		name= (uiname==null || !uiname.equals(""))? (String)item[0][a].name: "";
		name= (uiname==null || StringUtil.toCString(uiname)[0]!=0)? (String)item[0][a].name: "";
		icon= item[0][a].icon;
		value= item[0][a].value;
		itemw= ui_text_icon_width(block.curlayout, StringUtil.toCString(name), icon, 0);

//		if(icon && strcmp(name, "") != 0 && !icon_only)
//		if(icon!=0 && !name.equals("") && icon_only==0)
		if(icon!=0 && StringUtil.strcmp(StringUtil.toCString(name),0, StringUtil.toCString(""),0) != 0 && icon_only==0)
			but= UI.uiDefIconTextButR(block, UI.ROW, 0, BIFIconID.values()[icon], name, 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, null);
		else if(icon!=0)
			but= UI.uiDefIconButR(block, UI.ROW, 0, BIFIconID.values()[icon], 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, null);
		else
			but= UI.uiDefButR(block, UI.ROW, 0, name, 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, null);

		if(ui_layout_local_dir(layout) != UI.UI_LAYOUT_HORIZONTAL)
			but.flag |= UI.UI_TEXT_LEFT;
	}
	uiBlockSetCurLayout(block, layout);

//	if(free)
//		MEM_freeN(item);
}

//static void ui_item_enum_row(uiLayout *layout, uiBlock *block, PointerRNA *ptr, PropertyRNA *prop, char *uiname, int x, int y, int w, int h)
//{
//	EnumPropertyItem *item;
//	const char *identifier;
//	char *name;
//	int a, totitem, itemw, icon, value, free;
//
//	identifier= RNA_property_identifier(prop);
//	RNA_property_enum_items(block.evil_C, ptr, prop, &item, &totitem, &free);
//
//	uiBlockSetCurLayout(block, ui_item_local_sublayout(layout, layout, 1));
//	for(a=0; a<totitem; a++) {
//		if(!item[a].identifier[0])
//			continue;
//
//		name= (!uiname || uiname[0])? (char*)item[a].name: "";
//		icon= item[a].icon;
//		value= item[a].value;
//		itemw= ui_text_icon_width(block.curlayout, name, icon);
//
//		if(icon && strcmp(name, "") != 0)
//			uiDefIconTextButR(block, ROW, 0, icon, name, 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, NULL);
//		else if(icon)
//			uiDefIconButR(block, ROW, 0, icon, 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, NULL);
//		else
//			uiDefButR(block, ROW, 0, name, 0, 0, itemw, h, ptr, identifier, -1, 0, value, -1, -1, NULL);
//	}
//	uiBlockSetCurLayout(block, layout);
//
//	if(free)
//		MEM_freeN(item);
//}

/* create label + button for RNA property */
public static uiBut ui_item_with_label(uiLayout layout, uiBlock block, String name, int icon, PointerRNA ptr, PropertyRNA prop, int index, int x, int y, int w, int h, int flag)
{
	uiLayout sub;
	uiBut but=null;
	int type;
	int subtype;
	int labelw;

	sub= uiLayoutRow(layout, 0);
	uiBlockSetCurLayout(block, sub);

//	if(!name.equals("")) {
	if(StringUtil.toCString(name)[0]!=0) {
		/* XXX UI_GetStringWidth is not accurate
		labelw= UI_GetStringWidth(name);
		CLAMP(labelw, w/4, 3*w/4);*/
		labelw= w/3;
		UI.uiDefBut(block, UI.LABEL, 0, name, x, y, labelw, h, null, 0.0f, 0.0f, 0, 0, "");
		w= w-labelw;
	}

	type= RnaAccess.RNA_property_type(prop);
	subtype= RnaAccess.RNA_property_subtype(prop);

	if(subtype == RNATypes.PROP_FILEPATH || subtype == RNATypes.PROP_DIRPATH) {
		uiBlockSetCurLayout(block, uiLayoutRow(sub, 1));
		UIUtils.uiDefAutoButR(block, ptr, prop, index, "", icon, x, y, w-UI.UI_UNIT_X, h);

		/* BUTTONS_OT_file_browse calls uiFileBrowseContextProperty */
		but= UI.uiDefIconButO(block, UI.BUT, "BUTTONS_OT_file_browse", WmTypes.WM_OP_INVOKE_DEFAULT, BIFIconID.ICON_FILESEL, x, y, UI.UI_UNIT_X, h, null);
	}
	else if(subtype == RNATypes.PROP_DIRECTION) {
		UI.uiDefButR(block, UI.BUT_NORMAL, 0, name, x, y, 100, 100, ptr, RnaAccess.RNA_property_identifier(prop), index, 0, 0, -1, -1, null);
	}
	else if((flag & UI.UI_ITEM_R_EVENT)!=0) {
		UI.uiDefButR(block, UI.KEYEVT, 0, name, x, y, w, h, ptr, RnaAccess.RNA_property_identifier(prop), index, 0, 0, -1, -1, null);
	}
	else if((flag & UI.UI_ITEM_R_FULL_EVENT)!=0) {
//		if(RNA_struct_is_a(ptr->type, &RNA_KeyMapItem)) {
//			char buf[128];
//
//			WM_keymap_item_to_string(ptr->data, buf, sizeof(buf));
//
//			but= uiDefButR(block, HOTKEYEVT, 0, buf, x, y, w, h, ptr, RNA_property_identifier(prop), 0, 0, 0, -1, -1, null);
//			uiButSetFunc(but, ui_keymap_but_cb, but, NULL);
//			if (flag & UI_ITEM_R_IMMEDIATE)
//				uiButSetFlag(but, UI_BUT_IMMEDIATE);
//		}
	}
	else {
//		if (prop.identifier.equals("viewport_shade")) {
//			System.out.println("viewport_shade");
//		}
//		else if (prop.identifier.equals("pivot_point")) {
//			System.out.println("pivot_point");
//		}
		but= UIUtils.uiDefAutoButR(block, ptr, prop, index, (type == RNATypes.PROP_ENUM && (flag & UI.UI_ITEM_R_ICON_ONLY)==0)? null: "", icon, x, y, w, h);
//		but= UIUtils.uiDefAutoButR(block, ptr, prop, index, null, icon, x, y, w, h);
	}

	uiBlockSetCurLayout(block, layout);
	return but;
}

//void uiFileBrowseContextProperty(const bContext *C, PointerRNA *ptr, PropertyRNA **prop)
//{
//	ARegion *ar= CTX_wm_region(C);
//	uiBlock *block;
//	uiBut *but, *prevbut;
//
//	memset(ptr, 0, sizeof(*ptr));
//	*prop= NULL;
//
//	if(!ar)
//		return;
//
//	for(block=ar.uiblocks.first; block; block=block.next) {
//		for(but=block.buttons.first; but; but= but.next) {
//			prevbut= but.prev;
//
//			/* find the button before the active one */
//			if((but.flag & UI_BUT_LAST_ACTIVE) && prevbut && prevbut.rnapoin.id.data) {
//				if(RNA_property_type(prevbut.rnaprop) == PROP_STRING) {
//					*ptr= prevbut.rnapoin;
//					*prop= prevbut.rnaprop;
//					return;
//				}
//			}
//		}
//	}
//}

/********************* Button Items *************************/

/* disabled item */
static void ui_item_disabled(uiLayout layout, String name)
{
	uiBlock block= layout.root.block;
	uiBut but;
	int w;

	uiBlockSetCurLayout(block, layout);

	if(name==null)
		name= "";

	w= ui_text_icon_width(layout, StringUtil.toCString(name), 0, 0);

	but= UI.uiDefBut(block, UI.LABEL, 0, name, 0, 0, w, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");
	but.flag |= UI.UI_BUT_DISABLED;
	but.lock = 1;
	but.lockstr = StringUtil.toCString("");
}

/* operator items */
public static PointerRNA uiItemFullO(uiLayout layout, String idname, String name, int icon, IDProperty properties, int context, int flag)
{
	uiBlock block= layout.root.block;
	wmOperatorType ot= WmOperators.WM_operatortype_find(idname, false);
	uiBut but;
	int w;

	if(ot==null) {
		ui_item_disabled(layout, idname);
		return RnaAccess.PointerRNA_NULL;
	}

	if(name==null)
		name= ot.name;
	if(layout.root.type == UI.UI_LAYOUT_MENU && icon==0)
		icon= BIFIconID.ICON_BLANK1.ordinal();

	/* create button */
	uiBlockSetCurLayout(block, layout);

	w= ui_text_icon_width(layout, StringUtil.toCString(name), icon, 0);

	if ((flag & UI.UI_ITEM_R_NO_BG)!=0)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSSN);
	
//	if(icon!=null && strcmp(name, "") != 0)
    if(icon!=0 && !name.isEmpty())
		but= UI.uiDefIconTextButO(block, UI.BUT, ot.idname, context, BIFIconID.values()[icon], name, 0, 0, w, UI.UI_UNIT_Y, null);
	else if(icon!=0)
		but= UI.uiDefIconButO(block, UI.BUT, ot.idname, context, BIFIconID.values()[icon], 0, 0, w, UI.UI_UNIT_Y, null);
	else
		but= UI.uiDefButO(block, UI.BUT, ot.idname, context, name, 0, 0, w, UI.UI_UNIT_Y, null);

    /* text alignment for toolbar buttons */
	if((layout.root.type == UI.UI_LAYOUT_TOOLBAR) && icon==0)
		but.flag |= UI.UI_TEXT_LEFT;

	if ((flag & UI.UI_ITEM_R_NO_BG)!=0)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSS);
    
	/* assign properties */
	if(properties!=null || (flag & UI.UI_ITEM_O_RETURN_PROPS)!=0) {
		PointerRNA opptr= UI.uiButGetOperatorPtrRNA(but);
		
		if (properties!=null) {
			opptr.data= properties;
		}
		else {
//			IDPropertyTemplate val = {0};
//			opptr->data= IDP_New(IDP_GROUP, val, "wmOperatorProperties");
			opptr.data= new IDProperty();
		}
		
		return opptr;
	}
	
	return RnaAccess.PointerRNA_NULL;
}

public static String ui_menu_enumpropname(uiLayout layout, String opname, String propname, int retval)
{
	wmOperatorType ot= WmOperators.WM_operatortype_find(opname, false);
	PointerRNA ptr = new PointerRNA();
	PropertyRNA prop;

	if(ot==null || ot.srna==null)
		return "";

	RnaAccess.RNA_pointer_create(null, ot.srna, null, ptr);
	prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0);

	if(prop!=null) {
		EnumPropertyItem[][] item = new EnumPropertyItem[1][];
		int[] totitem={0}, free={0};
		String[] name = new String[1];

		RnaAccess.RNA_property_enum_items((bContext)layout.root.block.evil_C, ptr, prop, item, totitem, free);
		if(RnaAccess.RNA_enum_name(item[0], retval, name)) {
//			if(free) MEM_freeN(item);
			return (String)name[0];
		}
		
//		if(free)
//			MEM_freeN(item);
	}

	return "";
}

public static void uiItemEnumO(uiLayout layout, String opname, String name, int icon, String propname, int value)
{
	PointerRNA ptr = new PointerRNA();

	WmOperators.WM_operator_properties_create(ptr, opname);
	RnaAccess.RNA_enum_set(ptr, propname, value);

	if(name==null)
		name= ui_menu_enumpropname(layout, opname, propname, value);

	uiItemFullO(layout, opname, name, icon, (IDProperty)ptr.data, layout.root.opcontext, 0);
}

public static void uiItemsFullEnumO(uiLayout layout, String opname, String propname, IDProperty properties, int context, int flag)
{
	wmOperatorType ot= WmOperators.WM_operatortype_find(opname, false);
	PointerRNA ptr = new PointerRNA();
	PropertyRNA prop;
	uiBut bt;
	uiBlock block= layout.root.block;

	if(ot==null || ot.srna==null) {
		ui_item_disabled(layout, opname);
		return;
	}

	RnaAccess.RNA_pointer_create(null, ot.srna, null, ptr);
	prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0);

	if(prop!=null && RnaAccess.RNA_property_type(prop) == RNATypes.PROP_ENUM) {
		EnumPropertyItem[][] item = new EnumPropertyItem[1][];
		int i;
		int[] totitem={0}, free={0};
		uiLayout split= uiLayoutSplit(layout, 0, 0);
		uiLayout column= uiLayoutColumn(split, 0);

		RnaAccess.RNA_property_enum_items((bContext)block.evil_C, ptr, prop, item, totitem, free);

		for(i=0; i<totitem[0]; i++) {
//			if(item[0][i].identifier[0]) {
			if(!item[0][i].identifier.isEmpty()) {
				if(properties!=null) {
//					PointerRNA tptr;
//
//					WM_operator_properties_create(&tptr, opname);
//					if(tptr.data) {
//						IDP_FreeProperty(tptr.data);
//						MEM_freeN(tptr.data);
//					}
//					tptr.data= IDP_CopyProperty(properties);
//					RNA_enum_set(&tptr, propname, item[i].value);
//
//					uiItemFullO(column, opname, (char*)item[i].name, item[i].icon, tptr.data, context, flag);
				}
				else
					uiItemEnumO(column, opname, (String)item[0][i].name, item[0][i].icon, propname, item[0][i].value);
			}
			else {
				if(item[0][i].name!=null) {
					if(i != 0) {
						column= uiLayoutColumn(split, 0);
						/* inconsistent, but menus with labels do not look good flipped */
						block.flag |= UI.UI_BLOCK_NO_FLIP;
					}

					uiItemL(column, (String)item[0][i].name, UI.ICON_NULL);
					bt= block.buttons.last;
					bt.flag= UI.UI_TEXT_LEFT;
				}
				else /* XXX bug here, collums draw bottom item badly */
					uiItemS(column);
			}
		}

//		if(free)
//			MEM_freeN(item);
	}
}

public static void uiItemsEnumO(uiLayout layout, String opname, String propname)
{
	uiItemsFullEnumO(layout, opname, propname, null, layout.root.opcontext, 0);
	
//	wmOperatorType ot= WmOperators.WM_operatortype_find(opname, false);
//	PointerRNA ptr = new PointerRNA();
//	PropertyRNA prop;
//
//	if(ot==null || ot.srna==null) {
//		ui_item_disabled(layout, opname);
//		return;
//	}
//
//	RnaAccess.RNA_pointer_create(null, ot.srna, null, ptr);
//	prop= (PropertyRNA)RnaAccess.RNA_struct_find_property(ptr, propname, true);
//
////	if(prop!=null && RNA_property_type(prop) == RNATypes.PROP_ENUM) {
////		EnumPropertyItem item;
////		int totitem, i, free;
//
////		RNA_property_enum_items(layout.root.block.evil_C, &ptr, prop, &item, &totitem, &free);
//
////		for(i=0; i<totitem; i++)
////			if(item[i].identifier[0])
////				uiItemEnumO(layout, (char*)item[i].name, item[i].icon, opname, propname, item[i].value);
////			else
//				uiItemS(layout);
//
////		if(free)
////			MEM_freeN(item);
////	}
}

/* for use in cases where we have */
public static void uiItemEnumO_string(uiLayout layout, String name, int icon, String opname, String propname, String value_str)
{
	PointerRNA ptr = new PointerRNA();

	/* for getting the enum */
	PropertyRNA prop;
	EnumPropertyItem[][] item = new EnumPropertyItem[1][];
	int[] value={0}, free={0};

	WmOperators.WM_operator_properties_create(ptr, opname);

	/* enum lookup */
	if((prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0))!=null) {
		RnaAccess.RNA_property_enum_items((bContext)layout.root.block.evil_C, ptr, prop, item, null, free);
		if(RnaAccess.RNA_enum_value_from_id(item[0],0, value_str, value)==false) {
//			if(free) MEM_freeN(item);
			System.out.printf("uiItemEnumO_string: %s.%s, enum %s not found.\n", RnaAccess.RNA_struct_identifier(ptr.type), propname, value_str);
			return;
		}

//		if(free)
//			MEM_freeN(item);
	}
	else {
		System.out.printf("uiItemEnumO_string: %s.%s not found.\n", RnaAccess.RNA_struct_identifier(ptr.type), propname);
		return;
	}

	RnaAccess.RNA_property_enum_set(ptr, prop, value[0]);

	/* same as uiItemEnumO */
	if(name==null)
		name= ui_menu_enumpropname(layout, opname, propname, value[0]);

	uiItemFullO(layout, opname, name, icon, (IDProperty)ptr.data, layout.root.opcontext, 0);
}

public static void uiItemBooleanO(uiLayout layout, String name, int icon, String opname, String propname, boolean value)
{
	PointerRNA ptr = new PointerRNA();

	WmOperators.WM_operator_properties_create(ptr, opname);
	RnaAccess.RNA_boolean_set(ptr, propname, value);

	uiItemFullO(layout, opname, name, icon, (IDProperty)ptr.data, layout.root.opcontext, 0);
}

//void uiItemIntO(uiLayout *layout, char *name, int icon, char *opname, char *propname, int value)
//{
//	PointerRNA ptr;
//
//	WM_operator_properties_create(&ptr, opname);
//	RNA_int_set(&ptr, propname, value);
//
//	uiItemFullO(layout, name, icon, opname, ptr.data, layout.root.opcontext);
//}
//
//void uiItemFloatO(uiLayout *layout, char *name, int icon, char *opname, char *propname, float value)
//{
//	PointerRNA ptr;
//
//	WM_operator_properties_create(&ptr, opname);
//	RNA_float_set(&ptr, propname, value);
//
//	uiItemFullO(layout, name, icon, opname, ptr.data, layout.root.opcontext);
//}
//
//void uiItemStringO(uiLayout *layout, char *name, int icon, char *opname, char *propname, char *value)
//{
//	PointerRNA ptr;
//
//	WM_operator_properties_create(&ptr, opname);
//	RNA_string_set(&ptr, propname, value);
//
//	uiItemFullO(layout, name, icon, opname, ptr.data, layout.root.opcontext);
//}

public static void uiItemO(uiLayout layout, String name, int icon, String opname)
{
	uiItemFullO(layout, opname, name, icon, null, layout.root.opcontext, 0);
}

/* RNA property items */

public static void ui_item_rna_size(uiLayout layout, String name, int icon, PointerRNA ptr, PropertyRNA prop, int index, int icon_only, int[] r_w, int[] r_h)
{
	int type;
	int subtype;
	int len, w, h;

	/* arbitrary extended width by type */
	type= RnaAccess.RNA_property_type(prop);
	subtype= RnaAccess.RNA_property_subtype(prop);
	len= RnaAccess.RNA_property_array_length(ptr, prop);

	if((type == RNATypes.PROP_STRING || type == RNATypes.PROP_POINTER || type == RNATypes.PROP_ENUM) && name.isEmpty() && icon_only==0)
		name= "non-empty text";
	else if(type == RNATypes.PROP_BOOLEAN && name.isEmpty() && icon_only==0)
		icon= BIFIconID.ICON_DOT.ordinal();

	w= ui_text_icon_width(layout, StringUtil.toCString(name), icon, 0);
	h= UI.UI_UNIT_Y;

	/* increase height for arrays */
	if(index == RNA_NO_INDEX && len > 0) {
		if(name.isEmpty() && icon == UI.ICON_NULL)
			h= 0;

		if((subtype == RNATypes.PROP_LAYER || subtype == RNATypes.PROP_LAYER_MEMBER))
			h += 2*UI.UI_UNIT_Y;
		else if(subtype == RNATypes.PROP_MATRIX)
			h += Math.ceil(Math.sqrt(len))*UI.UI_UNIT_Y;
		else
			h += len*UI.UI_UNIT_Y;
	}
	else if(ui_layout_vary_direction(layout) == UI_ITEM_VARY_X) {
		if(type == RNATypes.PROP_BOOLEAN && !name.equals(""))
			w += UI.UI_UNIT_X/5;
		else if(type == RNATypes.PROP_ENUM)
			w += UI.UI_UNIT_X/4;
		else if(type == RNATypes.PROP_FLOAT || type == RNATypes.PROP_INT)
			w += UI.UI_UNIT_X*3;
	}

	r_w[0]= w;
	r_h[0]= h;
}

public static void uiItemFullR(uiLayout layout, PointerRNA ptr, PropertyRNA prop, int index, int value, int flag, String name, int icon)
{
	uiBlock block= layout.root.block;
	uiBut but;
	int type;
	int[] w={0}, h={0};
	int len, slider, toggle, expand, icon_only, no_bg;

	uiBlockSetCurLayout(block, layout);

	/* retrieve info */
	type= RnaAccess.RNA_property_type(prop);
	len= RnaAccess.RNA_property_array_length(ptr, prop);

	/* set name and icon */
	if(name==null)
		name= (String)RnaAccess.RNA_property_ui_name(prop);
	if(icon == UI.ICON_NULL) {
		icon= RnaAccess.RNA_property_ui_icon(prop);
	}
	
	if((type == RNATypes.PROP_INT || type == RNATypes.PROP_FLOAT || type == RNATypes.PROP_STRING || type == RNATypes.PROP_POINTER))
		name= ui_item_name_add_colon(name);
	else if(type == RNATypes.PROP_BOOLEAN && len!=0 && index == RNA_NO_INDEX)
		name= ui_item_name_add_colon(name);
	else if(type == RNATypes.PROP_ENUM && index != RNA_ENUM_VALUE)
		name= ui_item_name_add_colon(name);

	if(layout.root.type == UI.UI_LAYOUT_MENU) {
		if(type == RNATypes.PROP_BOOLEAN)
			icon= (RnaAccess.RNA_property_boolean_get(ptr, prop)!=0)? BIFIconID.ICON_CHECKBOX_HLT.ordinal(): BIFIconID.ICON_CHECKBOX_DEHLT.ordinal();
		else if(type == RNATypes.PROP_ENUM && index == RNA_ENUM_VALUE) {
			int enum_value= RnaAccess.RNA_property_enum_get(ptr, prop);
			if((RnaAccess.RNA_property_flag(prop) & RNATypes.PROP_ENUM_FLAG)!=0) {
				icon= ((enum_value & value)!=0)? BIFIconID.ICON_CHECKBOX_HLT.ordinal(): BIFIconID.ICON_CHECKBOX_DEHLT.ordinal();
			}
			else {
				icon= (enum_value == value)? BIFIconID.ICON_CHECKBOX_HLT.ordinal(): BIFIconID.ICON_CHECKBOX_DEHLT.ordinal();
			}
		}
	}

	slider= (flag & UI.UI_ITEM_R_SLIDER);
	toggle= (flag & UI.UI_ITEM_R_TOGGLE);
	expand= (flag & UI.UI_ITEM_R_EXPAND);
	icon_only= (flag & UI.UI_ITEM_R_ICON_ONLY);
	no_bg= (flag & UI.UI_ITEM_R_NO_BG);

	/* get size */
	ui_item_rna_size(layout, name, icon, ptr, prop, index, icon_only, w, h);

	if (no_bg!=0)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSSN);
	
	/* array property */
	if(index == RNA_NO_INDEX && len > 0) {
		System.out.println("uiItemFullR 1: "+name);
		ui_item_array(layout, block, name, icon, ptr, prop, len, 0, 0, w[0], h[0], expand, slider, toggle, icon_only);
	}
	/* enum item */
	else if(type == RNATypes.PROP_ENUM && index == RNA_ENUM_VALUE) {
		System.out.println("uiItemFullR 2: "+name);
		String identifier= (String)RnaAccess.RNA_property_identifier(prop);

		if(icon!=0 && StringUtil.strcmp(StringUtil.toCString(name),0, StringUtil.toCString(""),0) != 0 && icon_only==0)
			UI.uiDefIconTextButR(block, UI.ROW, 0, BIFIconID.values()[icon], name, 0, 0, w[0], h[0], ptr, identifier, -1, 0, value, -1, -1, null);
		else if(icon!=0)
			UI.uiDefIconButR(block, UI.ROW, 0, BIFIconID.values()[icon], 0, 0, w[0], h[0], ptr, identifier, -1, 0, value, -1, -1, null);
		else
			UI.uiDefButR(block, UI.ROW, 0, name, 0, 0, w[0], h[0], ptr, identifier, -1, 0, value, -1, -1, null);
	}
	/* expanded enum */
	else if(type == RNATypes.PROP_ENUM && (expand!=0 || (RnaAccess.RNA_property_flag(prop) & RNATypes.PROP_ENUM_FLAG)!=0)) {
//		System.out.println("uiItemFullR 3: "+name);
		UILayout.ui_item_enum_expand(layout, block, ptr, prop, name, h[0], icon_only);
	}
	/* property with separate label */
	else if(type == RNATypes.PROP_ENUM || type == RNATypes.PROP_STRING || type == RNATypes.PROP_POINTER) {
//		System.out.println("uiItemFullR 4: "+name);
		but= ui_item_with_label(layout, block, name, icon, ptr, prop, index, 0, 0, w[0], h[0], flag);
		ui_but_add_search(but, ptr, prop, null, null);
		
		if(layout.redalert!=0)
			UI.uiButSetFlag(but, UI.UI_BUT_REDALERT);
	}
	/* single button */
	else {
//		System.out.println("uiItemFullR 5: "+name);
		but= UIUtils.uiDefAutoButR(block, ptr, prop, index, (String)name, icon, 0, 0, w[0], h[0]);

		if(slider!=0 && but.type==UI.NUM)
			but.type= UI.NUMSLI;

		if(toggle!=0 && but.type==UI.OPTION)
			but.type= UI.TOG;
		
		if(layout.redalert!=0)
			UI.uiButSetFlag(but, UI.UI_BUT_REDALERT);
	}
	
	if (no_bg!=0)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSS);
}

public static void uiItemR(uiLayout layout, PointerRNA ptr, String propname, int flag, String name, int icon)
{
	PropertyRNA prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0);

	if(prop==null) {
		ui_item_disabled(layout, propname);
		System.out.printf("uiItemR: property not found: %s.%s\n", RnaAccess.RNA_struct_identifier(ptr.type), propname);
		return;
	}

	uiItemFullR(layout, ptr, prop, RNA_NO_INDEX, 0, flag, name, icon);
}

//	uiItemFullR(layout, name, icon, ptr, prop, RNA_NO_INDEX, 0, expand, slider, toggle);
//}
//
//void uiItemEnumR(uiLayout *layout, char *name, int icon, struct PointerRNA *ptr, char *propname, int value)
//{
//	PropertyRNA *prop;
//
//	if(!ptr.data || !propname)
//		return;
//
//	prop= RNA_struct_find_property(ptr, propname);
//
//	if(!prop || RNA_property_type(prop) != PROP_ENUM) {
//		ui_item_disabled(layout, propname);
//		printf("uiItemEnumR: enum property not found: %s\n", propname);
//		return;
//	}
//
//	uiItemFullR(layout, name, icon, ptr, prop, RNA_ENUM_VALUE, value, 0, 0, 0);
//}
//
//void uiItemEnumR_string(uiLayout *layout, char *name, int icon, struct PointerRNA *ptr, char *propname, char *value)
//{
//	PropertyRNA *prop;
//	EnumPropertyItem *item;
//	int ivalue, a, free;
//
//	if(!ptr.data || !propname)
//		return;
//
//	prop= RNA_struct_find_property(ptr, propname);
//
//	if(!prop || RNA_property_type(prop) != PROP_ENUM) {
//		ui_item_disabled(layout, propname);
//		printf("uiItemEnumR: enum property not found: %s\n", propname);
//		return;
//	}
//
//	RNA_property_enum_items(layout.root.block.evil_C, ptr, prop, &item, NULL, &free);
//
//	if(!RNA_enum_value_from_id(item, value, &ivalue)) {
//		if(free) MEM_freeN(item);
//		ui_item_disabled(layout, propname);
//		printf("uiItemEnumR: enum property value not found: %s\n", value);
//		return;
//	}
//
//	for(a=0; item[a].identifier; a++) {
//		if(item[a].value == ivalue) {
//			uiItemFullR(layout, (char*)item[a].name, item[a].icon, ptr, prop, RNA_ENUM_VALUE, ivalue, 0, 0, 0);
//			break;
//		}
//	}
//
//	if(free)
//		MEM_freeN(item);
//}
//
//void uiItemsEnumR(uiLayout *layout, struct PointerRNA *ptr, char *propname)
//{
//	PropertyRNA *prop;
//
//	prop= RNA_struct_find_property(ptr, propname);
//
//	if(!prop) {
//		ui_item_disabled(layout, propname);
//		return;
//	}
//
//	if(RNA_property_type(prop) == PROP_ENUM) {
//		EnumPropertyItem *item;
//		int totitem, i, free;
//
//		RNA_property_enum_items(layout.root.block.evil_C, ptr, prop, &item, &totitem, &free);
//
//		for(i=0; i<totitem; i++)
//			if(item[i].identifier[0])
//				uiItemEnumR(layout, (char*)item[i].name, 0, ptr, propname, item[i].value);
//			else
//				uiItemS(layout);
//
//		if(free)
//			MEM_freeN(item);
//	}
//}
//
///* Pointer RNA button with search */
//
//static void rna_search_cb(const struct bContext *C, void *arg_but, char *str, uiSearchItems *items)
//{
//	Scene *scene= CTX_data_scene(C);
//	uiBut *but= arg_but;
//	char *name;
//	int i, iconid;
//
//	i = 0;
//	RNA_PROP_BEGIN(&but.rnasearchpoin, itemptr, but.rnasearchprop) {
//		iconid= 0;
//		if(RNA_struct_is_ID(itemptr.type))
//			iconid= ui_id_icon_get(scene, itemptr.data);
//
//		name= RNA_struct_name_get_alloc(&itemptr, NULL, 0);
//
//		if(name) {
//			if(BLI_strcasestr(name, str)) {
//				if(!uiSearchItemAdd(items, name, SET_INT_IN_POINTER(i), iconid)) {
//					MEM_freeN(name);
//					break;
//				}
//			}
//
//			MEM_freeN(name);
//		}
//
//		i++;
//	}
//	RNA_PROP_END;
//}

public static void search_id_collection(StructRNA ptype, PointerRNA ptr, PropertyRNA[] prop)
{
	StructRNA srna;

	/* look for collection property in Main */
	RnaAccess.RNA_main_pointer_create(G.main, ptr);

	prop[0]= null;

//	RNA_STRUCT_BEGIN(ptr, iprop)
	{
		CollectionPropertyIterator rna_macro_iter = new CollectionPropertyIterator();
		for(RnaAccess.RNA_property_collection_begin(ptr, RnaAccess.RNA_struct_iterator_property(ptr.type), rna_macro_iter); rna_macro_iter.valid; RnaAccess.RNA_property_collection_next(rna_macro_iter)) {
			PropertyRNA iprop= (PropertyRNA)rna_macro_iter.ptr.data;
			{
				/* if it's a collection and has same pointer type, we've got it */
				if(RnaAccess.RNA_property_type(iprop) == RNATypes.PROP_COLLECTION) {
					srna= RnaAccess.RNA_property_pointer_type(ptr, iprop);
		
					if(ptype == srna) {
						prop[0]= iprop;
						break;
					}
				}
			}
//	RNA_STRUCT_END;
		}
		RnaAccess.RNA_property_collection_end(rna_macro_iter);
	}
}

public static void ui_but_add_search(uiBut but, PointerRNA ptr, PropertyRNA prop, PointerRNA searchptr, PropertyRNA searchprop)
{
	StructRNA ptype;
	PointerRNA sptr = new PointerRNA();

//	/* for ID's we do automatic lookup */
//	if(searchprop==null) {
//		if(RnaAccess.RNA_property_type(prop) == RNATypes.PROP_POINTER) {
//			ptype= RnaAccess.RNA_property_pointer_type(ptr, prop);
//			PropertyRNA[] searchprop_p={searchprop};
//			search_id_collection(ptype, sptr, searchprop_p);
//			searchprop = searchprop_p[0];
//			searchptr= sptr;
//		}
//	}

	/* turn button into search button */
	if(searchprop!=null) {
		but.type= UI.SEARCH_MENU;
		but.hardmax= UtilDefines.MAX2(but.hardmax, 256);
		but.rnasearchpoin= searchptr;
		but.rnasearchprop= searchprop;
		but.flag |= UI.UI_ICON_LEFT|UI.UI_TEXT_LEFT|UI.UI_BUT_UNDO;

//		uiButSetSearchFunc(but, rna_search_cb, but, null, null);
	}
}

public static void uiItemPointerR(uiLayout layout, PointerRNA ptr, String propname, PointerRNA searchptr, String searchpropname, String name, int icon)
{
	PropertyRNA prop, searchprop;
	int type;
	uiBut but;
	uiBlock block;
	StructRNA icontype;
	int[] w={0}, h={0};
	
	/* validate arguments */
	prop= RnaAccess.RNA_struct_find_property(ptr, StringUtil.toCString(propname),0);

	if(prop==null) {
		System.out.printf("uiItemPointerR: property not found: %s.%s\n", RnaAccess.RNA_struct_identifier(ptr.type), propname);
		return;
	}
	
	type= RnaAccess.RNA_property_type(prop);
	if(!(type == RNATypes.PROP_POINTER || type == RNATypes.PROP_STRING)) {
		System.out.printf("uiItemPointerR: property %s must be a pointer or string.\n", propname);
		return;
	}

	searchprop= RnaAccess.RNA_struct_find_property(searchptr, StringUtil.toCString(searchpropname),0);


	if(searchprop==null) {
		System.out.printf("uiItemPointerR: search collection property not found: %s.%s\n", RnaAccess.RNA_struct_identifier(ptr.type), searchpropname);
		return;
	}
	else if (RnaAccess.RNA_property_type(searchprop) != RNATypes.PROP_COLLECTION) {
		System.out.printf("uiItemPointerR: search collection property is not a collection type: %s.%s\n", RnaAccess.RNA_struct_identifier(ptr.type), searchpropname);
		return;
	}

	/* get icon & name */
	if(icon==UI.ICON_NULL) {
		if(type == RNATypes.PROP_POINTER)
			icontype= RnaAccess.RNA_property_pointer_type(ptr, prop);
		else
			icontype= RnaAccess.RNA_property_pointer_type(searchptr, searchprop);

		icon= RnaAccess.RNA_struct_ui_icon(icontype);
	}
	if(name==null)
		name= (String)RnaAccess.RNA_property_ui_name(prop);

	/* create button */
	block= uiLayoutGetBlock(layout);

	ui_item_rna_size(layout, name, icon, ptr, prop, 0, 0, w, h);
	but= ui_item_with_label(layout, block, name, icon, ptr, prop, 0, 0, 0, w[0], h[0], 0);

	ui_but_add_search(but, ptr, prop, searchptr, searchprop);
}

/* menu item */
public static UI.uiMenuCreateFunc ui_item_menutype_func = new UI.uiMenuCreateFunc() {
public void run(bContext C, uiLayout layout, Object arg_mt)
//static void ui_item_menutype_func(bContext *C, uiLayout *layout, void *arg_mt)
{
	MenuType mt= ((Pointer<MenuType>)arg_mt).get();
	Menu menu = new Menu();

	menu.type= mt;
	menu.layout= layout;
	mt.draw.run(C, menu);
}
};

public static void ui_item_menu(uiLayout layout, String name, int icon, UI.uiMenuCreateFunc func, Pointer arg, Object argN)
{
	uiBlock block= layout.root.block;
	final uiBut but;
	int w, h;

	uiBlockSetCurLayout(block, layout);

	if(layout.root.type == UI.UI_LAYOUT_HEADER)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSS);

	if(name==null)
		name= "";
	if(layout.root.type == UI.UI_LAYOUT_MENU && icon==0)
		icon= BIFIconID.ICON_BLANK1.ordinal();

	w= ui_text_icon_width(layout, StringUtil.toCString(name), icon, 1);
	h= UI.UI_UNIT_Y;

	if(layout.root.type == UI.UI_LAYOUT_HEADER) /* ugly .. */
		w -= 10;

	if(!name.isEmpty() && icon!=0)
		but= UI.uiDefIconTextMenuBut(block, func, arg, BIFIconID.values()[icon], name, 0, 0, w, h, "");
	else if(icon!=0)
		but= UI.uiDefIconMenuBut(block, func, arg, BIFIconID.values()[icon], 0, 0, w, h, "");
	else
		but= UI.uiDefMenuBut(block, func, arg, name, 0, 0, w, h, "");

//	if(argN!=null) { /* ugly .. */
//		Pointer<uiBut> but_p = new Pointer<uiBut>() {
//			@Override
//			public uiBut get() { return but; }
//			@Override
//			public void set(uiBut obj) {
//				throw new UnsupportedOperationException("Not supported yet.");
//			}
//		};
////		but.poin= (char*)but;
//		but.poin= but_p;
//		but.func_argN= argN;
//	}

	if(layout.root.type == UI.UI_LAYOUT_HEADER)
		UI.uiBlockSetEmboss(block, UI.UI_EMBOSS);
	else if(layout.root.type == UI.UI_LAYOUT_PANEL) {
		but.type= UI.MENU;
		but.flag |= UI.UI_TEXT_LEFT;
	}
}

public static void uiItemM(uiLayout layout, bContext C, String menuname, String name, int icon)
{
	final MenuType mt= Wm.WM_menutype_find(menuname, false);

	if(mt==null) {
		System.out.printf("uiItemM: not found %s\n", menuname);
		return;
	}

	if(name==null)
		name= StringUtil.toJString(mt.label,0);
	if(layout.root.type == UI.UI_LAYOUT_MENU && icon==0)
		icon= BIFIconID.ICON_BLANK1.ordinal();
	
	Pointer<MenuType> mt_p = new Pointer<MenuType>() {
		public MenuType get() { return mt; }
		public void set(MenuType obj) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	};
	ui_item_menu(layout, name, icon, ui_item_menutype_func, mt_p, null);
}

/* label item */
public static uiBut uiItemL_(uiLayout layout, String name, int icon)
{
	uiBlock block= layout.root.block;
	uiBut but;
	int w;

	uiBlockSetCurLayout(block, layout);

	if(name==null)
		name= "";
	if(layout.root.type == UI.UI_LAYOUT_MENU && icon==0)
		icon= BIFIconID.ICON_BLANK1.ordinal();

	w= ui_text_icon_width(layout, StringUtil.toCString(name), icon, 0);

//	if(icon!=0 && StringUtil.strcmp(name, "") != 0)
    if(icon!=0 && !name.isEmpty())
		but= UI.uiDefIconTextBut(block, UI.LABEL, 0, BIFIconID.values()[icon], name, 0, 0, w, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");
	else if(icon!=0)
		but= UI.uiDefIconBut(block, UI.LABEL, 0, BIFIconID.values()[icon], 0, 0, w, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");
	else
		but= UI.uiDefBut(block, UI.LABEL, 0, name, 0, 0, w, UI.UI_UNIT_Y, null, 0.0f, 0.0f, 0, 0, "");
    
    return but;
}

public static void uiItemL(uiLayout layout, String name, int icon)
{
	uiItemL_(layout, name, icon);
}

///* value item */
//void uiItemV(uiLayout *layout, char *name, int icon, int argval)
//{
//	/* label */
//	uiBlock *block= layout.root.block;
//	float *retvalue= (block.handle)? &block.handle.retvalue: NULL;
//	int w;
//
//	uiBlockSetCurLayout(block, layout);
//
//	if(!name)
//		name= "";
//	if(layout.root.type == UI_LAYOUT_MENU && !icon)
//		icon= ICON_BLANK1;
//
//	w= ui_text_icon_width(layout, name, icon);
//
//	if(icon && strcmp(name, "") != 0)
//		uiDefIconTextButF(block, BUTM, 0, icon, (char*)name, 0, 0, w, UI_UNIT_Y, retvalue, 0.0, 0.0, 0, argval, "");
//	else if(icon)
//		uiDefIconButF(block, BUTM, 0, icon, 0, 0, w, UI_UNIT_Y, retvalue, 0.0, 0.0, 0, argval, "");
//	else
//		uiDefButF(block, BUTM, 0, (char*)name, 0, 0, w, UI_UNIT_Y, retvalue, 0.0, 0.0, 0, argval, "");
//}

/* separator item */
public static void uiItemS(uiLayout layout)
{
	uiBlock block= layout.root.block;

	uiBlockSetCurLayout(block, layout);
	UI.uiDefBut(block, UI.SEPR, 0, "", 0, 0, EM_SEPR_X, EM_SEPR_Y, null, 0.0f, 0.0f, 0, 0, "");
}

/* level items */
public static void uiItemMenuF(uiLayout layout, String name, int icon, uiMenuCreateFunc func, Pointer arg)
{
	if(func==null)
		return;

	ui_item_menu(layout, name, icon, func, arg, null);
}

public static class MenuItemLevel {
	public int opcontext;
	public String opname;
	public String propname;
	public PointerRNA rnapoin = new PointerRNA();
};

public static UI.uiMenuCreateFunc menu_item_enum_opname_menu = new UI.uiMenuCreateFunc() {
public void run(bContext C, uiLayout layout, Object arg)
{
	MenuItemLevel lvl= (MenuItemLevel)(((uiBut)arg).func_argN);

	uiLayoutSetOperatorContext(layout, WmTypes.WM_OP_EXEC_REGION_WIN);
	uiItemsEnumO(layout, lvl.opname, lvl.propname);
}};

public static void uiItemMenuEnumO(uiLayout layout, String opname, String propname, String name, int icon)
{
	wmOperatorType ot= WmOperators.WM_operatortype_find(opname, false);
	MenuItemLevel lvl;

	if(ot==null || ot.srna==null) {
		ui_item_disabled(layout, opname);
		return;
	}

	if(name==null)
		name= ot.name;
	if(layout.root.type == UI.UI_LAYOUT_MENU && icon==0)
		icon= BIFIconID.ICON_BLANK1.ordinal();

	lvl= new MenuItemLevel();
	lvl.opname= opname;
	lvl.propname= propname;
	lvl.opcontext= layout.root.opcontext;

	ui_item_menu(layout, name, icon, menu_item_enum_opname_menu, null, lvl);
}

//static void menu_item_enum_rna_menu(bContext *C, uiLayout *layout, void *arg)
//{
//	MenuItemLevel *lvl= (MenuItemLevel*)(((uiBut*)arg).func_argN);
//
//	uiLayoutSetOperatorContext(layout, lvl.opcontext);
//	uiItemsEnumR(layout, &lvl.rnapoin, lvl.propname);
//}
//
//void uiItemMenuEnumR(uiLayout *layout, char *name, int icon, struct PointerRNA *ptr, char *propname)
//{
//	MenuItemLevel *lvl;
//	PropertyRNA *prop;
//
//	prop= RNA_struct_find_property(ptr, propname);
//	if(!prop) {
//		ui_item_disabled(layout, propname);
//		return;
//	}
//
//	if(!name)
//		name= (char*)RNA_property_ui_name(prop);
//	if(layout.root.type == UI_LAYOUT_MENU && !icon)
//		icon= ICON_BLANK1;
//
//	lvl= MEM_callocN(sizeof(MenuItemLevel), "MenuItemLevel");
//	lvl.rnapoin= *ptr;
//	lvl.propname= propname;
//	lvl.opcontext= layout.root.opcontext;
//
//	ui_item_menu(layout, name, icon, menu_item_enum_rna_menu, NULL, lvl);
//}

/**************************** Layout Items ***************************/

/* single-row layout */
static void ui_litem_estimate_row(uiLayout litem)
{
	uiItem item;
	int[] itemw={0}, itemh={0};

	litem.w= 0;
	litem.h= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);

		litem.w += itemw[0];
		litem.h= UtilDefines.MAX2(itemh[0], litem.h);

		if(item.next!=null)
			litem.w += litem.space;
	}
}

static int ui_litem_min_width(int itemw)
{
	return UtilDefines.MIN2(2*UI.UI_UNIT_X, itemw);
}

static void ui_litem_layout_row(uiLayout litem)
{
	uiItem item;
	int x, y, w, tot, totw, neww, minw, offset;
    int[] itemw={0}, itemh={0};
	int fixedw, freew, fixedx, freex, flag= 0, lastw= 0;

	x= litem.x;
	y= litem.y;
	w= litem.w;
	totw= 0;
	tot= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);
		totw += itemw[0];
		tot++;
	}

	if(totw == 0)
		return;

	if(w != 0)
		w -= (tot-1)*litem.space;
	fixedw= 0;

	/* keep clamping items to fixed minimum size until all are done */
	do {
		freew= 0;
		x= 0;
		flag= 0;

		for(item=litem.items.first; item!=null; item=item.next) {
			if(item.flag!=0)
				continue;

			ui_item_size(item, itemw, itemh);
			minw= ui_litem_min_width(itemw[0]);

			if(w - lastw > 0)
				neww= ui_item_fit(itemw[0], x, totw, w-lastw, item.next==null, litem.alignment, null);
			else
				neww= 0; /* no space left, all will need clamping to minimum size */

			x += neww;

			if((neww < minw || itemw[0] == minw) && w != 0) {
				/* fixed size */
				item.flag= 1;
				fixedw += minw;
				flag= 1;
				totw -= itemw[0];
			}
			else {
				/* keep free size */
				item.flag= 0;
				freew += itemw[0];
			}
		}

		lastw= fixedw;
	} while(flag!=0);

	freex= 0;
	fixedx= 0;
	x= litem.x;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);
		minw= ui_litem_min_width(itemw[0]);

		if(item.flag!=0) {
			/* fixed minimum size items */
			itemw[0]= ui_item_fit(minw, fixedx, fixedw, UtilDefines.MIN2(w, fixedw), item.next==null, litem.alignment, null);
			fixedx += itemw[0];
		}
		else {
			/* free size item */
			itemw[0]= ui_item_fit(itemw[0], freex, freew, w-fixedw, item.next==null, litem.alignment, null);
			freex += itemw[0];
		}

		/* align right/center */
		offset= 0;
		if(litem.alignment == UI.UI_LAYOUT_ALIGN_RIGHT) {
			if(fixedw == 0 && freew < w-fixedw)
				offset= (w - fixedw) - freew;
		}
		else if(litem.alignment == UI.UI_LAYOUT_ALIGN_CENTER) {
			if(fixedw == 0 && freew < w-fixedw)
				offset= ((w - fixedw) - freew)/2;
		}

		/* position item */
		ui_item_position(item, x+offset, y-itemh[0], itemw[0], itemh[0]);

		x += itemw[0];
		if(item.next!=null)
			x += litem.space;
	}

	litem.w= x - litem.x;
	litem.h= litem.y - y;
	litem.x= x;
	litem.y= y;
}

/* single-column layout */
static void ui_litem_estimate_column(uiLayout litem)
{
	uiItem item;
	int[] itemw={0}, itemh={0};

	litem.w= 0;
	litem.h= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);

		litem.w= UtilDefines.MAX2(litem.w, itemw[0]);
		litem.h += itemh[0];

		if(item.next!=null)
			litem.h += litem.space;
	}
}

static void ui_litem_layout_column(uiLayout litem)
{
	uiItem item;
	int[] itemh={0};
    int x, y;

	x= litem.x;
	y= litem.y;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, null, itemh);

		y -= itemh[0];
		ui_item_position(item, x, y, litem.w, itemh[0]);

		if(item.next!=null)
			y -= litem.space;
	}

	litem.h= litem.y - y;
	litem.x= x;
	litem.y= y;
}

/* root layout */
static void ui_litem_estimate_root(uiLayout litem)
{
	/* nothing to do */
}

static void ui_litem_layout_root(uiLayout litem)
{
	if(litem.root.type == UI.UI_LAYOUT_HEADER)
		ui_litem_layout_row(litem);
	else
		ui_litem_layout_column(litem);
}

/* box layout */
static void ui_litem_estimate_box(uiLayout litem)
{
	uiStyle style= litem.root.style;

	ui_litem_estimate_column(litem);
	litem.w += 2*style.boxspace;
	litem.h += style.boxspace;
}

static void ui_litem_layout_box(uiLayout litem)
{
	uiLayoutItemBx box= (uiLayoutItemBx)litem;
	uiStyle style= litem.root.style;
	uiBut but;
	int w, h;

	w= litem.w;
	h= litem.h;

	litem.x += style.boxspace;

	if(w != 0) litem.w -= 2*style.boxspace;
	if(h != 0) litem.h -= 2*style.boxspace;

	ui_litem_layout_column(litem);

	litem.x -= style.boxspace;
	litem.y -= style.boxspace;

	if(w != 0) litem.w += 2*style.boxspace;
	if(h != 0) litem.h += style.boxspace;

	/* roundbox around the sublayout */
	but= box.roundbox;
	but.x1= litem.x;
	but.y1= litem.y;
	but.x2= litem.x+litem.w;
	but.y2= litem.y+litem.h;
}

/* multi-column layout, automatically flowing to the next */
static void ui_litem_estimate_column_flow(uiLayout litem)
{
	uiStyle style= litem.root.style;
	uiLayoutItemFlow flow= (uiLayoutItemFlow)litem;
	uiItem item;
	int col, x, y, emh, emy, miny, maxw=0;
    int[] itemw={0}, itemh={0};
	int toth, totitem;

	/* compute max needed width and total height */
	toth= 0;
	totitem= 0;
	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);
		maxw= UtilDefines.MAX2(maxw, itemw[0]);
		toth += itemh[0];
		totitem++;
	}

	if(flow.number <= 0) {
		/* auto compute number of columns, not very good */
		if(maxw == 0) {
			flow.totcol= 1;
			return;
		}

		flow.totcol= UtilDefines.MAX2(litem.root.emw/maxw, 1);
		flow.totcol= UtilDefines.MIN2(flow.totcol, totitem);
	}
	else
		flow.totcol= flow.number;

	/* compute sizes */
	x= 0;
	y= 0;
	emy= 0;
	miny= 0;

	maxw= 0;
	emh= toth/flow.totcol;

	/* create column per column */
	col= 0;
	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);

		y -= itemh[0] + style.buttonspacey;
		miny= UtilDefines.MIN2(miny, y);
		emy -= itemh[0];
		maxw= UtilDefines.MAX2(itemw[0], maxw);

		/* decide to go to next one */
		if(col < flow.totcol-1 && emy <= -emh) {
			x += maxw + litem.space;
			maxw= 0;
			y= 0;
			col++;
		}
	}

	litem.w= x;
	litem.h= litem.y - miny;
}

static void ui_litem_layout_column_flow(uiLayout litem)
{
	uiStyle style= litem.root.style;
	uiLayoutItemFlow flow= (uiLayoutItemFlow)litem;
	uiItem item;
	int col, x, y, w, emh, emy, miny;
    int[] itemw={0}, itemh={0};
	int toth, totitem;
    int[] offset={0};

	/* compute max needed width and total height */
	toth= 0;
	totitem= 0;
	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);
		toth += itemh[0];
		totitem++;
	}

	/* compute sizes */
	x= litem.x;
	y= litem.y;
	emy= 0;
	miny= 0;

	w= litem.w - (flow.totcol-1)*style.columnspace;
	emh= toth/flow.totcol;

	/* create column per column */
	col= 0;
	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, null, itemh);
		itemw[0]= ui_item_fit(1, x-litem.x, flow.totcol, w, col == flow.totcol-1, litem.alignment, offset);

		y -= itemh[0];
		emy -= itemh[0];
		ui_item_position(item, x+offset[0], y, itemw[0], itemh[0]);
		y -= style.buttonspacey;
		miny= UtilDefines.MIN2(miny, y);

		/* decide to go to next one */
		if(col < flow.totcol-1 && emy <= -emh) {
			x += itemw[0] + style.columnspace;
			y= litem.y;
			col++;
		}
	}

	litem.h= litem.y - miny;
	litem.x= x;
	litem.y= miny;
}

/* free layout */
static void ui_litem_estimate_absolute(uiLayout litem)
{
	uiItem item;
	int[] itemx={0}, itemy={0}, itemw={0}, itemh={0};
    int minx, miny;

	minx= (int)(1e6);
	miny= (int)(1e6);
	litem.w= 0;
	litem.h= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_offset(item, itemx, itemy);
		ui_item_size(item, itemw, itemh);

		minx= UtilDefines.MIN2(minx, itemx[0]);
		miny= UtilDefines.MIN2(miny, itemy[0]);

		litem.w= UtilDefines.MAX2(litem.w, itemx[0]+itemw[0]);
		litem.h= UtilDefines.MAX2(litem.h, itemy[0]+itemh[0]);
	}

	litem.w -= minx;
	litem.h -= miny;
}

static void ui_litem_layout_absolute(uiLayout litem)
{
	uiItem item;
	float scalex=1.0f, scaley=1.0f;
	int x, y, newx, newy, minx, miny, totw, toth;
    int[] itemx={0}, itemy={0}, itemh={0}, itemw={0};

	minx= (int)(1e6);
	miny= (int)(1e6);
	totw= 0;
	toth= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_offset(item, itemx, itemy);
		ui_item_size(item, itemw, itemh);

		minx= UtilDefines.MIN2(minx, itemx[0]);
		miny= UtilDefines.MIN2(miny, itemy[0]);

		totw= UtilDefines.MAX2(totw, itemx[0]+itemw[0]);
		toth= UtilDefines.MAX2(toth, itemy[0]+itemh[0]);
	}

	totw -= minx;
	toth -= miny;

	if(litem.w!=0 && totw > 0)
		scalex= (float)litem.w/(float)totw;
	if(litem.h!=0 && toth > 0)
		scaley= (float)litem.h/(float)toth;

	x= litem.x;
	y= (int)(litem.y - scaley*toth);

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_offset(item, itemx, itemy);
		ui_item_size(item, itemw, itemh);

		if(scalex != 1.0f) {
			newx= (int)((itemx[0] - minx)*scalex);
			itemw[0]= (int)((itemx[0] - minx + itemw[0])*scalex - newx);
			itemx[0]= minx + newx;
		}

		if(scaley != 1.0f) {
			newy= (int)((itemy[0] - miny)*scaley);
			itemh[0]= (int)((itemy[0] - miny + itemh[0])*scaley - newy);
			itemy[0]= miny + newy;
		}

		ui_item_position(item, x+itemx[0]-minx, y+itemy[0]-miny, itemw[0], itemh[0]);
	}

	litem.w= (int)(scalex*totw);
	litem.h= litem.y - y;
	litem.x= x + litem.w;
	litem.y= y;
}

/* split layout */
static void ui_litem_estimate_split(uiLayout litem)
{
	ui_litem_estimate_row(litem);
}

static void ui_litem_layout_split(uiLayout litem)
{
	uiLayoutItemSplt split= (uiLayoutItemSplt)litem;
	uiItem item;
	float percentage;
	int[] itemh={0};
    int x, y, w, tot=0, colw=0;

	x= litem.x;
	y= litem.y;

	for(item=litem.items.first; item!=null; item=item.next)
		tot++;

	if(tot == 0)
		return;

	percentage= (split.percentage == 0.0f)? 1.0f/(float)tot: split.percentage;

	w= (litem.w - (tot-1)*litem.space);
	colw= (int)(w*percentage);
	colw= UtilDefines.MAX2(colw, 0);

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, null, itemh);

		ui_item_position(item, x, y-itemh[0], colw, itemh[0]);
		x += colw;

		if(item.next!=null) {
			colw= (w - (int)(w*percentage))/(tot-1);
			colw= UtilDefines.MAX2(colw, 0);

			x += litem.space;
		}
	}

	litem.w= x - litem.x;
	litem.h= litem.y - y;
	litem.x= x;
	litem.y= y;
}

/* overlap layout */
public static void ui_litem_estimate_overlap(uiLayout litem)
{
	uiItem item;
	int[] itemw={0}, itemh={0};

	litem.w= 0;
	litem.h= 0;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);

		litem.w= UtilDefines.MAX2(itemw[0], litem.w);
		litem.h= UtilDefines.MAX2(itemh[0], litem.h);
	}
}

public static void ui_litem_layout_overlap(uiLayout litem)
{
	uiItem item;
	int[] itemw={0}, itemh={0};
	int x, y;

	x= litem.x;
	y= litem.y;

	for(item=litem.items.first; item!=null; item=item.next) {
		ui_item_size(item, itemw, itemh);
		ui_item_position(item, x, y-itemh[0], litem.w, itemh[0]);

		litem.h= UtilDefines.MAX2(litem.h, itemh[0]);
	}

	litem.x= x;
	litem.y= y - litem.h;
}

/* layout create functions */
public static uiLayout uiLayoutRow(uiLayout layout, int align)
{
	uiLayout litem;

	litem= new uiLayout();
	litem.item.type= uiItemType.ITEM_LAYOUT_ROW;
	litem.root= layout.root;
	litem.align= (char)align;
	litem.active= 1;
	litem.enabled= 1;
	litem.context= layout.context;
	litem.space= (align!=0)? 0: layout.root.style.buttonspacex;
	litem.w = layout.w;
	ListBaseUtil.BLI_addtail(layout.items, litem);

	uiBlockSetCurLayout(layout.root.block, litem);

	return litem;
}

public static uiLayout uiLayoutColumn(uiLayout layout, int align)
{
	uiLayout litem;

	litem= new uiLayout();
	litem.item.type= uiItemType.ITEM_LAYOUT_COLUMN;
	litem.root= layout.root;
	litem.align= (char)align;
	litem.active= 1;
	litem.enabled= 1;
	litem.context= layout.context;
	litem.space= (litem.align!=0)? 0: layout.root.style.buttonspacey;
	litem.w = layout.w;
	ListBaseUtil.BLI_addtail(layout.items, litem);

	uiBlockSetCurLayout(layout.root.block, litem);

	return litem;
}

//uiLayout *uiLayoutColumnFlow(uiLayout *layout, int number, int align)
//{
//	uiLayoutItemFlow *flow;
//
//	flow= MEM_callocN(sizeof(uiLayoutItemFlow), "uiLayoutItemFlow");
//	flow.litem.item.type= ITEM_LAYOUT_COLUMN_FLOW;
//	flow.litem.root= layout.root;
//	flow.litem.align= align;
//	flow.litem.active= 1;
//	flow.litem.enabled= 1;
//	flow.litem.context= layout.context;
//	flow.litem.space= (flow.litem.align)? 0: layout.root.style.columnspace;
//	flow.number= number;
//	BLI_addtail(&layout.items, flow);
//
//	uiBlockSetCurLayout(layout.root.block, &flow.litem);
//
//	return &flow.litem;
//}
//
//static uiLayout *ui_layout_box(uiLayout *layout, int type)
//{
//	uiLayoutItemBx *box;
//
//	box= MEM_callocN(sizeof(uiLayoutItemBx), "uiLayoutItemBx");
//	box.litem.item.type= ITEM_LAYOUT_BOX;
//	box.litem.root= layout.root;
//	box.litem.active= 1;
//	box.litem.enabled= 1;
//	box.litem.context= layout.context;
//	box.litem.space= layout.root.style.columnspace;
//	BLI_addtail(&layout.items, box);
//
//	uiBlockSetCurLayout(layout.root.block, &box.litem);
//
//	box.roundbox= uiDefBut(layout.root.block, type, 0, "", 0, 0, 0, 0, NULL, 0.0, 0.0, 0, 0, "");
//
//	return &box.litem;
//}
//
//uiLayout *uiLayoutBox(uiLayout *layout)
//{
//	return ui_layout_box(layout, ROUNDBOX);
//}
//
//uiLayout *uiLayoutListBox(uiLayout *layout)
//{
//	return ui_layout_box(layout, LISTBOX);
//}
//
//ListBase *uiLayoutBoxGetList(uiLayout *layout)
//{
//	uiLayoutItemBx *box= (uiLayoutItemBx*)layout;
//	return &box.items;
//}

public static uiLayout uiLayoutAbsolute(uiLayout layout, int align)
{
	uiLayout litem;

	litem= new uiLayout();
	litem.item.type= uiItemType.ITEM_LAYOUT_ABSOLUTE;
	litem.root= layout.root;
	litem.align= (char)align;
	litem.active= 1;
	litem.enabled= 1;
	litem.context= layout.context;
	ListBaseUtil.BLI_addtail(layout.items, litem);

	uiBlockSetCurLayout(layout.root.block, litem);

	return litem;
}

public static uiBlock uiLayoutAbsoluteBlock(uiLayout layout)
{
	uiBlock block;

	block= uiLayoutGetBlock(layout);
	uiLayoutAbsolute(layout, 0);

	return block;
}

public static uiLayout uiLayoutSplit(uiLayout layout, float percentage, int align)
{
	uiLayoutItemSplt split;

	split= new uiLayoutItemSplt();
	split.litem.item.type= uiItemType.ITEM_LAYOUT_SPLIT;
	split.litem.root= layout.root;
	split.litem.align= (char)align;
	split.litem.active= 1;
	split.litem.enabled= 1;
	split.litem.context= layout.context;
	split.litem.space= layout.root.style.columnspace;
	split.litem.w= layout.w;
	split.percentage= percentage;
	ListBaseUtil.BLI_addtail(layout.items, split);

	uiBlockSetCurLayout(layout.root.block, split.litem);

	return split.litem;
}

public static void uiLayoutSetActive(uiLayout layout, int active)
{
	layout.active= (char)active;
}

public static void uiLayoutSetEnabled(uiLayout layout, int enabled)
{
	layout.enabled= (char)enabled;
}

public static void uiLayoutSetRedAlert(uiLayout layout, int redalert)
{
	layout.redalert= (char)redalert;
}

public static void uiLayoutSetKeepAspect(uiLayout layout, int keepaspect)
{
	layout.keepaspect= (char)keepaspect;
}

public static void uiLayoutSetAlignment(uiLayout layout, int alignment)
{
	layout.alignment= (char)alignment;
}

public static void uiLayoutSetScaleX(uiLayout layout, float scale)
{
	layout.scale[0]= scale;
}

public static void uiLayoutSetScaleY(uiLayout layout, float scale)
{
	layout.scale[1]= scale;
}

public static int uiLayoutGetActive(uiLayout layout)
{
	return layout.active;
}

public static int uiLayoutGetEnabled(uiLayout layout)
{
	return layout.enabled;
}

public static int uiLayoutGetRedAlert(uiLayout layout)
{
	return layout.redalert;
}

public static int uiLayoutGetKeepAspect(uiLayout layout)
{
	return layout.keepaspect;
}

public static int uiLayoutGetAlignment(uiLayout layout)
{
	return layout.alignment;
}

public static int uiLayoutGetWidth(uiLayout layout)
{
	return layout.w;
}

public static float uiLayoutGetScaleX(uiLayout layout)
{
	return layout.scale[0];
}

public static float uiLayoutGetScaleY(uiLayout layout)
{
	return layout.scale[0];
}

/********************** Layout *******************/

static void ui_item_scale(uiLayout litem, float[] scale)
{
	uiItem item;
	int[] x={0}, y={0}, w={0}, h={0};

	for(item=litem.items.last; item!=null; item=item.prev) {
		ui_item_size(item, w, h);
		ui_item_offset(item, x, y);

		if(scale[0] != 0.0f) {
			x[0] *= scale[0];
			w[0] *= scale[0];
		}

		if(scale[1] != 0.0f) {
			y[0] *= scale[1];
			h[0] *= scale[1];
		}

		ui_item_position(item, x[0], y[0], w[0], h[0]);
	}
}

static void ui_item_estimate(uiItem item)
{
	uiItem subitem;

	if(item.type != uiItemType.ITEM_BUTTON) {
		uiLayout litem= (uiLayout)item;

		for(subitem=litem.items.first; subitem!=null; subitem=subitem.next)
			ui_item_estimate(subitem);

		if(litem.items.first == null)
			return;

		if(litem.scale[0] != 0.0f || litem.scale[1] != 0.0f)
			ui_item_scale(litem, litem.scale);

		switch(litem.item.type) {
			case ITEM_LAYOUT_COLUMN:
				ui_litem_estimate_column(litem);
				break;
			case ITEM_LAYOUT_COLUMN_FLOW:
				ui_litem_estimate_column_flow(litem);
				break;
			case ITEM_LAYOUT_ROW:
				ui_litem_estimate_row(litem);
				break;
			case ITEM_LAYOUT_BOX:
				ui_litem_estimate_box(litem);
				break;
			case ITEM_LAYOUT_ROOT:
				ui_litem_estimate_root(litem);
				break;
			case ITEM_LAYOUT_ABSOLUTE:
				ui_litem_estimate_absolute(litem);
				break;
			case ITEM_LAYOUT_SPLIT:
				ui_litem_estimate_split(litem);
				break;
			case ITEM_LAYOUT_OVERLAP:
				ui_litem_estimate_overlap(litem);
				break;
			default:
				break;
		}
	}
}

static void ui_item_align(uiLayout litem, int nr)
{
	uiItem item;
	uiButtonItem bitem;
	uiLayoutItemBx box;

	for(item=litem.items.last; item!=null; item=item.prev) {
		if(item.type == uiItemType.ITEM_BUTTON) {
			bitem= (uiButtonItem)item;
			if(UI.ui_but_can_align(bitem.but))
				if(bitem.but.alignnr==0)
					bitem.but.alignnr= (short)nr;
		}
		else if(item.type == uiItemType.ITEM_LAYOUT_ABSOLUTE);
		else if(item.type == uiItemType.ITEM_LAYOUT_OVERLAP);
		else if(item.type == uiItemType.ITEM_LAYOUT_BOX) {
			box= (uiLayoutItemBx)item;
			box.roundbox.alignnr= (short)nr;
			ListBaseUtil.BLI_remlink(litem.root.block.buttons, box.roundbox);
			ListBaseUtil.BLI_addhead(litem.root.block.buttons, box.roundbox);
		}
		else
			ui_item_align((uiLayout)item, nr);
	}
}

static void ui_item_flag(uiLayout litem, int flag)
{
	uiItem item;
	uiButtonItem bitem;

	for(item=litem.items.last; item!=null; item=item.prev) {
		if(item.type == uiItemType.ITEM_BUTTON) {
			bitem= (uiButtonItem)item;
			bitem.but.flag |= flag;
		}
		else
			ui_item_flag((uiLayout)item, flag);
	}
}

static void ui_item_layout(uiItem item)
{
	uiItem subitem;

	if(item.type != uiItemType.ITEM_BUTTON) {
		uiLayout litem= (uiLayout)item;

		if(litem.items.first == null)
			return;

		if(litem.align!=0)
			ui_item_align(litem, ++litem.root.block.alignnr);
		if(litem.active==0)
			ui_item_flag(litem, UI.UI_BUT_INACTIVE);
		if(litem.enabled==0)
			ui_item_flag(litem, UI.UI_BUT_DISABLED);

		switch(litem.item.type) {
			case ITEM_LAYOUT_COLUMN:
				ui_litem_layout_column(litem);
				break;
			case ITEM_LAYOUT_COLUMN_FLOW:
				ui_litem_layout_column_flow(litem);
				break;
			case ITEM_LAYOUT_ROW:
				ui_litem_layout_row(litem);
				break;
			case ITEM_LAYOUT_BOX:
				ui_litem_layout_box(litem);
				break;
			case ITEM_LAYOUT_ROOT:
				ui_litem_layout_root(litem);
				break;
			case ITEM_LAYOUT_ABSOLUTE:
				ui_litem_layout_absolute(litem);
				break;
			case ITEM_LAYOUT_SPLIT:
				ui_litem_layout_split(litem);
				break;
			case ITEM_LAYOUT_OVERLAP:
				ui_litem_layout_overlap(litem);
				break;
			default:
				break;
		}

		for(subitem=litem.items.first; subitem!=null; subitem=subitem.next)
			ui_item_layout(subitem);
	}
}

//static void ui_layout_items(bContext C, uiBlock block, uiLayout layout)
//{
//	ui_item_estimate(layout.item);
//	ui_item_layout(layout.item);
//}

//static void ui_layout_end(bContext C, uiBlock block, uiLayout layout, int[] x, int[] y)
static void ui_layout_end(uiBlock block, uiLayout layout, int[] x, int[] y)
{
//	if(layout.root.handlefunc!=null)
//		uiBlockSetButmFunc(block, layout.root.handlefunc, layout.root.argv);

//	ui_layout_items(C, block, layout);
	ui_item_estimate(layout.item);
	ui_item_layout(layout.item);

	if(x!=null) x[0]= layout.x;
	if(y!=null) y[0]= layout.y;
}

static void ui_layout_free(uiLayout layout)
{
//	uiItem item, next;
//
//	for(item=layout.items.first; item!=null; item=next) {
//		next= item.next;
//
//		if(item.type == ITEM_BUTTON)
//			MEM_freeN(item);
//		else
//			ui_layout_free((uiLayout*)item);
//	}
//
//	if(layout.item.type == ITEM_LAYOUT_BOX)
//		BLI_freelistN(&((uiLayoutItemBx*)layout).items);
//
//	MEM_freeN(layout);
}

public static uiLayout uiBlockLayout(uiBlock block, int dir, int type, int x, int y, int size, int em, uiStyle style)
{
	uiLayout layout;
	uiLayoutRoot root;

	root= new uiLayoutRoot();
	root.type= type;
	root.style= style;
	root.block= block;
	root.opcontext= WmTypes.WM_OP_INVOKE_REGION_WIN;

	layout= new uiLayout();
	layout.item.type= uiItemType.ITEM_LAYOUT_ROOT;

	layout.x= x;
	layout.y= y;
	layout.root= root;
	layout.space= style.templatespace;
	layout.active= 1;
	layout.enabled= 1;
	layout.context= null;

	if(type == UI.UI_LAYOUT_MENU)
		layout.space= 0;

	if(dir == UI.UI_LAYOUT_HORIZONTAL) {
		layout.h= size;
		layout.root.emh= em*UI.UI_UNIT_Y;
	}
	else {
		layout.w= size;
		layout.root.emw= em*UI.UI_UNIT_X;
	}

	block.curlayout= layout;
	root.layout= layout;
	ListBaseUtil.BLI_addtail(block.layouts, root);

	return layout;
}

public static uiBlock uiLayoutGetBlock(uiLayout layout)
{
	return layout.root.block;
}

public static int uiLayoutGetOperatorContext(uiLayout layout)
{
	return layout.root.opcontext;
}


public static void uiBlockSetCurLayout(uiBlock block, uiLayout layout)
{
	block.curlayout= layout;
}

public static void ui_layout_add_but(uiLayout layout, uiBut but)
{
	uiButtonItem bitem;

	bitem= new uiButtonItem();
	bitem.item.type= uiItemType.ITEM_BUTTON;
	bitem.but= but;
	ListBaseUtil.BLI_addtail(layout.items, bitem);

	if(layout.context!=null) {
		but.context= layout.context;
		but.context.used= 1;
	}
}

public static void uiLayoutSetOperatorContext(uiLayout layout, int opcontext)
{
	layout.root.opcontext= opcontext;
}

//void uiLayoutSetFunc(uiLayout *layout, uiMenuHandleFunc handlefunc, void *argv)
//{
//	layout.root.handlefunc= handlefunc;
//	layout.root.argv= argv;
//}

//public static void uiBlockLayoutResolve(bContext C, uiBlock block, int[] x, int[] y)
public static void uiBlockLayoutResolve(uiBlock block, int[] x, int[] y)
{
	uiLayoutRoot root;

	if(x!=null) x[0]= 0;
	if(y!=null) y[0]= 0;

	block.curlayout= null;

	for(root=block.layouts.first; root!=null; root=root.next) {
		/* NULL in advance so we don't interfere when adding button */
		ui_layout_end(block, root.layout, x, y);
		ui_layout_free(root.layout);
	}

	ListBaseUtil.BLI_freelistN(block.layouts);

//	/* XXX silly trick, interface_templates.c doesn't get linked
//	 * because it's not used by other files in this module? */
//	{
//		void ui_template_fix_linking();
//		ui_template_fix_linking();
//	}
}

//void uiLayoutSetContextPointer(uiLayout *layout, char *name, PointerRNA *ptr)
//{
//	uiBlock *block= layout.root.block;
//	layout.context= CTX_store_add(&block.contexts, name, ptr);
//}

//static char *str = NULL; // XXX, constant re-freeing, far from ideal.
public static String uiLayoutIntrospect(uiLayout layout)
{
//	DynStr *ds= BLI_dynstr_new();
//
//	if(str)
//		MEM_freeN(str);
//
//	ui_intro_uiLayout(ds, layout);
//
//	str = BLI_dynstr_get_cstring(ds);
//	BLI_dynstr_free(ds);
//
//	return str;
	return "uiLayoutIntrospect";
}

}
