package blender.editors.uinterface;

import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.makesrna.RNATypes;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;

public class UIUtils {
//	/**
//	 * ***** BEGIN GPL LICENSE BLOCK *****
//	 *
//	 * This program is free software; you can redistribute it and/or
//	 * modify it under the terms of the GNU General Public License
//	 * as published by the Free Software Foundation; either version 2
//	 * of the License, or (at your option) any later version. 
//	 *
//	 * This program is distributed in the hope that it will be useful,
//	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
//	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	 * GNU General Public License for more details.
//	 *
//	 * You should have received a copy of the GNU General Public License
//	 * along with this program; if not, write to the Free Software Foundation,
//	 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
//	 *
//	 * The Original Code is Copyright (C) 2009 Blender Foundation.
//	 * All rights reserved.
//	 * 
//	 * Contributor(s): Blender Foundation
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdio.h>
//	#include <stdlib.h>
//	#include <string.h>
//	#include <assert.h>
//
//	#include "DNA_object_types.h"
//
//	#include "BKE_context.h"
//	#include "BKE_utildefines.h"
//
//	#include "RNA_access.h"
//
//	#include "UI_interface.h"
//	#include "UI_resources.h"


	/*************************** RNA Utilities ******************************/

	public static uiBut uiDefAutoButR(uiBlock block, PointerRNA ptr, PropertyRNA prop, int index, String name, int icon, int x1, int y1, int x2, int y2)
	{
		uiBut but=null;
		String propname= RnaAccess.RNA_property_identifier(prop);
//		char prop_item[sizeof(((IDProperty *)NULL)->name)+4]; /* size of the ID prop name + room for [""] */
		int arraylen= RnaAccess.RNA_property_array_length(ptr, prop);

//		/* support for custom props */
//		if(RNA_property_is_idprop(prop)) {
//			sprintf(prop_item, "[\"%s\"]", propname);
//			propname= prop_item;
//		}

		switch(RnaAccess.RNA_property_type(prop)) {
			case RNATypes.PROP_BOOLEAN: {
//				int value, length;

				if(arraylen!=0 && index == -1)
					return null;

//				length= RnaAccess.RNA_property_array_length(ptr, prop);

//				if(length!=0)
//					value= RNA_property_boolean_get_index(ptr, prop, index);
//				else
//					value= RNA_property_boolean_get(ptr, prop);
				
				if(icon!=0 && name!=null && StringUtil.toCString(name)[0] == 0)
					but= UI.uiDefIconButR(block, UI.ICONTOG, 0, BIFIconID.values()[icon], x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				else if(icon!=0)
					but= UI.uiDefIconTextButR(block, UI.ICONTOG, 0, BIFIconID.values()[icon], name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				else
					but= UI.uiDefButR(block, UI.OPTION, 0, name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				break;
			}
			case RNATypes.PROP_INT:
			case RNATypes.PROP_FLOAT:
				if(arraylen!=0 && index == -1) {
					if((RnaAccess.RNA_property_subtype(prop) == RNATypes.PROP_COLOR || RnaAccess.RNA_property_subtype(prop) == RNATypes.PROP_COLOR_GAMMA))
						but= UI.uiDefButR(block, UI.COL, 0, name, x1, y1, x2, y2, ptr, propname, 0, 0, 0, -1, -1, null);
				}
				else if(RnaAccess.RNA_property_subtype(prop) == RNATypes.PROP_PERCENTAGE || RnaAccess.RNA_property_subtype(prop) == RNATypes.PROP_FACTOR)
					but= UI.uiDefButR(block, UI.NUMSLI, 0, name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				else
					but= UI.uiDefButR(block, UI.NUM, 0, name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				break;
			case RNATypes.PROP_ENUM:
				if(icon!=0 && name!=null && StringUtil.toCString(name)[0] == 0) {
					but= UI.uiDefIconButR(block, UI.MENU, 0, BIFIconID.values()[icon], x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				}
				else if(icon!=0) {
					but= UI.uiDefIconTextButR(block, UI.MENU, 0, BIFIconID.values()[icon], null, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				}
				else {
					but= UI.uiDefButR(block, UI.MENU, 0, null, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
//					if (prop.identifier.equals("viewport_shade")) {
//						System.out.println("viewport_shade: <"+name+">, icon: "+icon);
//						System.out.println("viewport_shade: <"+StringUtil.toJString(but.drawstr,0)+">, icon: "+but.icon);
//					}
//					else if (prop.identifier.equals("pivot_point")) {
//						System.out.println("pivot_point: <"+name+">, icon: "+icon);
//						System.out.println("pivot_point: <"+StringUtil.toJString(but.drawstr,0)+">, icon: "+but.icon);
//					}
				}
				break;
			case RNATypes.PROP_STRING:
				if(icon!=0 && name!=null && StringUtil.toCString(name)[0] == 0)
					but= UI.uiDefIconButR(block, UI.TEX, 0, BIFIconID.values()[icon], x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				else if(icon!=0)
					but= UI.uiDefIconTextButR(block, UI.TEX, 0, BIFIconID.values()[icon], name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				else
					but= UI.uiDefButR(block, UI.TEX, 0, name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				break;
			case RNATypes.PROP_POINTER: {
//				System.out.println("uiDefAutoButR: PROP_POINTER");
				PointerRNA pptr;
				int _icon;

				pptr= RnaAccess.RNA_property_pointer_get(ptr, prop);
				if(pptr.type==null)
					pptr.type= RnaAccess.RNA_property_pointer_type(ptr, prop);
				_icon= RnaAccess.RNA_struct_ui_icon(pptr.type);
				if(_icon == BIFIconID.ICON_DOT.ordinal())
					_icon= 0;

				but= UI.uiDefIconTextButR(block, UI.IDPOIN, 0, BIFIconID.values()[_icon], name, x1, y1, x2, y2, ptr, propname, index, 0, 0, -1, -1, null);
				break;
			}
			case RNATypes.PROP_COLLECTION: {
				System.out.println("uiDefAutoButR: PROP_COLLECTION");
//				char text[256];
//				sprintf(text, "%d items", RNA_property_collection_length(ptr, prop));
//				but= uiDefBut(block, LABEL, 0, text, x1, y1, x2, y2, NULL, 0, 0, 0, 0, NULL);
//				uiButSetFlag(but, UI_BUT_DISABLED);
//				break;
			}
			default:
				but= null;
				break;
		}

		return but;
	}

//	int uiDefAutoButsRNA(uiLayout *layout, PointerRNA *ptr, int (*check_prop)(PropertyRNA *), const char label_align)
//	{
//		uiLayout *split, *col;
//		int flag;
//		const char *name;
//		int tot= 0;
//
//		assert(ELEM3(label_align, '\0', 'H', 'V'));
//
//		RNA_STRUCT_BEGIN(ptr, prop) {
//			flag= RNA_property_flag(prop);
//			if(flag & PROP_HIDDEN || (check_prop && check_prop(prop)==FALSE))
//				continue;
//
//			if(label_align != '\0') {
//				name= RNA_property_ui_name(prop);
//
//				if(label_align=='V') {
//					col= uiLayoutColumn(layout, 1);
//					uiItemL(col, name, ICON_NULL);
//				}
//				else if(label_align=='H') {
//					split = uiLayoutSplit(layout, 0.5f, 0);
//
//					uiItemL(uiLayoutColumn(split, 0), name, ICON_NULL);
//					col= uiLayoutColumn(split, 0);
//				}
//				else {
//					col= NULL;
//				}
//
//				/* may meed to add more cases here.
//				 * don't override enum flag names */
//				if(flag & PROP_ENUM_FLAG) {
//					name= NULL;
//				}
//				else {
//					name= ""; /* name is shown above, empty name for button below */
//				}
//			}
//			else {
//				col= layout;
//				name= NULL; /* no smart label alignment, show default name with button */
//			}
//
//			uiItemFullR(col, ptr, prop, -1, 0, 0, name, ICON_NULL);
//			tot++;
//		}
//		RNA_STRUCT_END;
//
//		return tot;
//	}
//
//	/***************************** ID Utilities *******************************/
//
//	int uiIconFromID(ID *id)
//	{
//		Object *ob;
//		PointerRNA ptr;
//		short idcode;
//
//		if(id==NULL)
//			return ICON_NULL;
//		
//		idcode= GS(id->name);
//
//		/* exception for objects */
//		if(idcode == ID_OB) {
//			ob= (Object*)id;
//
//			if(ob->type == OB_EMPTY)
//				return ICON_EMPTY_DATA;
//			else
//				return uiIconFromID(ob->data);
//		}
//
//		/* otherwise get it through RNA, creating the pointer
//		   will set the right type, also with subclassing */
//		RNA_id_pointer_create(id, &ptr);
//
//		return (ptr.type)? RNA_struct_ui_icon(ptr.type) : ICON_NULL;
//	}

}
