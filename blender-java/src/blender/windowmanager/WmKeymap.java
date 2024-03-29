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
 * The Original Code is Copyright (C) 2007 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.ScreenTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmKeyMapItem;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaWm;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.windowmanager.WmEventSystem.wmEventHandler;

public class WmKeymap {

/* ********************* key config ***********************/

static void keymap_properties_set(wmKeyMapItem kmi)
{
	PointerRNA[] ptr = {(PointerRNA)kmi.ptr};
	IDProperty[] properties = {kmi.properties};
	WmOperators.WM_operator_properties_alloc(ptr, properties, kmi.idname,0);
	kmi.ptr = ptr[0];
	kmi.properties = properties[0];
//	WmOperators.WM_operator_properties_alloc(kmi);
	WmOperators.WM_operator_properties_sanitize((PointerRNA)kmi.ptr, true);
}

///* properties can be NULL, otherwise the arg passed is used and ownership is given to the kmi */
//void WM_keymap_properties_reset(wmKeyMapItem *kmi, struct IDProperty *properties)
//{
//	WM_operator_properties_free(kmi->ptr);
//	MEM_freeN(kmi->ptr);
//
//	kmi->ptr = NULL;
//	kmi->properties = properties;
//
//	keymap_properties_set(kmi);
//}

public static wmKeyConfig WM_keyconfig_new(wmWindowManager wm, String idname)
{
	wmKeyConfig keyconf;
	
	keyconf= new wmKeyConfig();
	StringUtil.BLI_strncpy(keyconf.idname,0, StringUtil.toCString(idname),0, keyconf.idname.length);
	ListBaseUtil.BLI_addtail(wm.keyconfigs, keyconf);

	return keyconf;
}

//wmKeyConfig *WM_keyconfig_new_user(wmWindowManager *wm, const char *idname)
//{
//	wmKeyConfig *keyconf = WM_keyconfig_new(wm, idname);
//
//	keyconf->flag |= KEYCONF_USER;
//
//	return keyconf;
//}
//
//void WM_keyconfig_remove(wmWindowManager *wm, wmKeyConfig *keyconf)
//{
//	if (keyconf) {
//		if (BLI_streq(U.keyconfigstr, keyconf->idname)) {
//			BLI_strncpy(U.keyconfigstr, wm->defaultconf->idname, sizeof(U.keyconfigstr));
//		}
//
//		BLI_remlink(&wm->keyconfigs, keyconf);
//		WM_keyconfig_free(keyconf);
//	}
//}
//
//void WM_keyconfig_free(wmKeyConfig *keyconf)
//{
//	wmKeyMap *km;
//
//	while((km= keyconf->keymaps.first)) {
//		WM_keymap_free(km);
//		BLI_freelinkN(&keyconf->keymaps, km);
//	}
//
//	MEM_freeN(keyconf);
//}

public static void WM_keyconfig_userdef()
{
//	wmKeyMap *km;
//	wmKeyMapItem *kmi;
//
//	for(km=U.keymaps.first; km; km=km->next) {
//		/* modal keymaps don't have operator properties */
//		if ((km->flag & KEYMAP_MODAL) == 0) {
//			for(kmi=km->items.first; kmi; kmi=kmi->next) {
//				keymap_properties_set(kmi);
//			}
//		}
//	}
}

//static wmKeyConfig *wm_keyconfig_list_find(ListBase *lb, char *idname)
//{
//	wmKeyConfig *kc;
//
//	for(kc= lb->first; kc; kc= kc->next)
//		if(0==strncmp(idname, kc->idname, KMAP_MAX_NAME))
//			return kc;
//	
//	return NULL;
//}

/* ************************ free ************************* */

//void WM_keymap_free(wmKeyMap *keymap)
//{
//	wmKeyMapItem *kmi;
//
//	for(kmi=keymap->items.first; kmi; kmi=kmi->next) {
//		if(kmi->ptr) {
//			WM_operator_properties_free(kmi->ptr);
//			MEM_freeN(kmi->ptr);
//		}
//	}
//
//	BLI_freelistN(&keymap->items);
//}

/* ***************** generic call, exported **************** */

static void keymap_event_set(wmKeyMapItem kmi, int type, int val, int modifier, int keymodifier)
{
	kmi.type= (short)type;
	kmi.val= (short)val;
	kmi.keymodifier= (short)keymodifier;
	
	if(modifier == WmTypes.KM_ANY) {
		kmi.shift= kmi.ctrl= kmi.alt= kmi.oskey= WmTypes.KM_ANY;
	}
	else {
		
		kmi.shift= kmi.ctrl= kmi.alt= kmi.oskey= 0;
		
		/* defines? */
		if((modifier & WmTypes.KM_SHIFT)!=0)
			kmi.shift= 1;
		else if((modifier & WmTypes.KM_SHIFT2)!=0)
			kmi.shift= 2;
		if((modifier & WmTypes.KM_CTRL)!=0)
			kmi.ctrl= 1;
		else if((modifier & WmTypes.KM_CTRL2)!=0)
			kmi.ctrl= 2;
		if((modifier & WmTypes.KM_ALT)!=0)
			kmi.alt= 1;
		else if((modifier & WmTypes.KM_ALT2)!=0)
			kmi.alt= 2;
		if((modifier & WmTypes.KM_OSKEY)!=0)
			kmi.oskey= 1;
		else if((modifier & WmTypes.KM_OSKEY2)!=0)
			kmi.oskey= 2;
	}
}

static void keymap_item_set_id(wmKeyMap keymap, wmKeyMapItem kmi)
{
	keymap.kmi_id++;
	if ((keymap.flag & WindowManagerTypes.KEYMAP_USER) == 0) {
		kmi.id = keymap.kmi_id;
	} else {
		kmi.id = (short)(-keymap.kmi_id); // User defined keymap entries have negative ids
	}
}

/* if item was added, then bail out */
public static wmKeyMapItem WM_keymap_verify_item(wmKeyMap keymap, String idname, int type, int val, int modifier, int keymodifier)
{
	wmKeyMapItem kmi;

	for(kmi= (wmKeyMapItem)keymap.items.first; kmi!=null; kmi= kmi.next)
		if(StringUtil.strncmp(kmi.idname,0, StringUtil.toCString(idname),0, WindowManagerTypes.OP_MAX_TYPENAME)==0)
			break;
	if(kmi==null) {
		kmi= new wmKeyMapItem();

		ListBaseUtil.BLI_addtail(keymap.items, kmi);
		StringUtil.BLI_strncpy(kmi.idname,0, StringUtil.toCString(idname),0, WindowManagerTypes.OP_MAX_TYPENAME);

		keymap_item_set_id(keymap, kmi);
		
		keymap_event_set(kmi, type, val, modifier, keymodifier);
		keymap_properties_set(kmi);
	}
	return kmi;
}

/* always add item */
public static wmKeyMapItem WM_keymap_add_item(wmKeyMap keymap, String idname, int type, int val, int modifier, int keymodifier)
{
	wmKeyMapItem kmi= new wmKeyMapItem();
	
	ListBaseUtil.BLI_addtail(keymap.items, kmi);
	StringUtil.BLI_strncpy(kmi.idname,0, StringUtil.toCString(idname),0, WindowManagerTypes.OP_MAX_TYPENAME);

	keymap_event_set(kmi, type, val, modifier, keymodifier);
	keymap_properties_set(kmi);
	
	keymap_item_set_id(keymap, kmi);
	
	return kmi;
}

/* menu wrapper for WM_keymap_add_item */
public static wmKeyMapItem WM_keymap_add_menu(wmKeyMap keymap, String idname, int type, int val, int modifier, int keymodifier)
{
	wmKeyMapItem kmi= WM_keymap_add_item(keymap, "WM_OT_call_menu", type, val, modifier, keymodifier);
	RnaAccess.RNA_string_set(kmi.ptr, "name", idname);
	return kmi;
}

//void WM_keymap_remove_item(wmKeyMap *keymap, wmKeyMapItem *kmi)
//{
//	if(BLI_findindex(&keymap->items, kmi) != -1) {
//		if(kmi->ptr) {
//			WM_operator_properties_free(kmi->ptr);
//			MEM_freeN(kmi->ptr);
//		}
//		BLI_freelinkN(&keymap->items, kmi);
//	}
//}

/* ****************** storage in WM ************ */

/* name id's are for storing general or multiple keymaps, 
   space/region ids are same as DNA_space_types.h */
/* gets free'd in wm.c */

static wmKeyMap WM_keymap_list_find(ListBase<wmKeyMap> lb, String idname, int spaceid, int regionid)
{
	wmKeyMap km;
	
	for(km= lb.first; km!=null; km= km.next)
		if(km.spaceid==spaceid && km.regionid==regionid)
			if(0==StringUtil.strncmp(StringUtil.toCString(idname),0, km.idname,0, WindowManagerTypes.KMAP_MAX_NAME))
				return km;
	
	return null;
}

//public static wmKeyMap WM_keymap_find(wmWindowManager wm, String idname, int spaceid, int regionid)
public static wmKeyMap WM_keymap_find(wmKeyConfig keyconf, String idname, int spaceid, int regionid)
{
//	wmKeyMap km= WM_keymap_list_find(wm.keyconfigs, idname, spaceid, regionid);
	wmKeyMap km= WM_keymap_list_find(keyconf.keymaps, idname, spaceid, regionid);
	
	if(km==null) {
		km= new wmKeyMap();
		StringUtil.BLI_strncpy(km.idname,0, StringUtil.toCString(idname),0, WindowManagerTypes.KMAP_MAX_NAME);
		km.spaceid= (short)spaceid;
		km.regionid= (short)regionid;
//		ListBaseUtil.BLI_addtail(wm.keyconfigs, km);
		ListBaseUtil.BLI_addtail(keyconf.keymaps, km);
	}
	
	return km;
}

//wmKeyMap *WM_keymap_find_all(const bContext *C, const char *idname, int spaceid, int regionid)
//{
//	wmWindowManager *wm = CTX_wm_manager(C);
//	wmKeyConfig *keyconf;
//	wmKeyMap *km;
//	
//	/* first user defined keymaps */
//	km= WM_keymap_list_find(&U.keymaps, idname, spaceid, regionid);
//	if (km)
//		return km;
//	
//	/* then user key config */
//	keyconf= wm_keyconfig_list_find(&wm->keyconfigs, U.keyconfigstr);
//	if(keyconf) {
//		km= WM_keymap_list_find(&keyconf->keymaps, idname, spaceid, regionid);
//		if (km)
//			return km;
//	}
//	
//	/* then use default */
//	km= WM_keymap_list_find(&wm->defaultconf->keymaps, idname, spaceid, regionid);
//	if (km)
//		return km;
//	else
//		return NULL;
//}

/* ****************** modal keymaps ************ */

/* modal maps get linked to a running operator, and filter the keys before sending to modal() callback */

//public static wmKeyMap WM_modalkeymap_add(wmWindowManager wm, String nameid, EnumPropertyItem[] items)
public static wmKeyMap WM_modalkeymap_add(wmKeyConfig keyconf, String nameid, EnumPropertyItem[] items)
{
	wmKeyMap km= WM_keymap_find(keyconf, nameid, 0, 0);
	km.flag |= WindowManagerTypes.KEYMAP_MODAL;
	km.modal_items= items;

	return km;
}

//public static wmKeyMap WM_modalkeymap_get(wmWindowManager wm, String nameid)
public static wmKeyMap WM_modalkeymap_get(wmKeyConfig keyconf, String nameid)
{
	wmKeyMap km;

//	for(km= (wmKeyMap)wm.keyconfigs.first; km!=null; km= km.next)
	for(km= (wmKeyMap)keyconf.keymaps.first; km!=null; km= km.next)
		if((km.flag & WindowManagerTypes.KEYMAP_MODAL)!=0)
			if(0==StringUtil.strncmp(StringUtil.toCString(nameid),0, km.idname,0, WindowManagerTypes.KMAP_MAX_NAME))
				break;

	return km;
}


public static wmKeyMapItem WM_modalkeymap_add_item(wmKeyMap km, int type, int val, int modifier, int keymodifier, int value)
{
	wmKeyMapItem kmi= new wmKeyMapItem();

	ListBaseUtil.BLI_addtail(km.items, kmi);
	kmi.propvalue= (short)value;

	keymap_event_set(kmi, type, val, modifier, keymodifier);
	
	keymap_item_set_id(km, kmi);
	
	return kmi;
}

public static void WM_modalkeymap_assign(wmKeyMap km, String opname)
{
	wmOperatorType ot= WmOperators.WM_operatortype_find(opname, false);

	if(ot!=null)
		ot.modalkeymap= km;
	else
		System.out.printf("error: modalkeymap_assign, unknown operator %s\n", opname);
}

/* ***************** get string from key events **************** */

public static String WM_key_event_string(short type)
{
    String[] name= {null};
	if(RnaAccess.RNA_enum_name(RnaWm.event_type_items, (int)type, name))
		return name[0];

	return "";
}

static byte[] wm_keymap_item_to_string(wmKeyMapItem kmi, byte[] str, int len)
{
	byte[] buf = new byte[128];

	buf[0]= 0;

	if (kmi.shift == WmTypes.KM_ANY &&
		kmi.ctrl == WmTypes.KM_ANY &&
		kmi.alt == WmTypes.KM_ANY &&
		kmi.oskey == WmTypes.KM_ANY) {

		StringUtil.strcat(buf, StringUtil.toCString("Any "),0);
	} else {
		if(kmi.shift!=0)
			StringUtil.strcat(buf, StringUtil.toCString("Shift "),0);
	
		if(kmi.ctrl!=0)
			StringUtil.strcat(buf, StringUtil.toCString("Ctrl "),0);
	
		if(kmi.alt!=0)
			StringUtil.strcat(buf, StringUtil.toCString("Alt "),0);
	
		if(kmi.oskey!=0)
			StringUtil.strcat(buf, StringUtil.toCString("Cmd "),0);
	}
	
	if(kmi.keymodifier!=0) {
		StringUtil.strcat(buf, StringUtil.toCString(WM_key_event_string(kmi.keymodifier)),0);
		StringUtil.strcat(buf, StringUtil.toCString(" "),0);
	}

	StringUtil.strcat(buf, StringUtil.toCString(WM_key_event_string(kmi.type)),0);
	StringUtil.BLI_strncpy(str,0, buf,0, len);

	return str;
}

static wmKeyMapItem wm_keymap_item_find_handlers(bContext C, ListBase<wmEventHandler> handlers, String opname, int opcontext, IDProperty properties, int compare_props, int hotkey, wmKeyMap[][] keymap_r)
{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmEventHandler *handler;
//	wmKeyMap *keymap;
//	wmKeyMapItem *kmi;
//
//	/* find keymap item in handlers */
//	for(handler=handlers->first; handler; handler=handler->next) {
//		keymap= WM_keymap_active(wm, handler->keymap);
//
//		if(keymap && (!keymap->poll || keymap->poll((bContext*)C))) {
//			for(kmi=keymap->items.first; kmi; kmi=kmi->next) {
//				if(strcmp(kmi->idname, opname) == 0 && WM_key_event_string(kmi->type)[0]) {
//					if (hotkey)
//						if (!ISHOTKEY(kmi->type))
//							continue;
//					
//					if(compare_props) {
//						if(kmi->ptr && IDP_EqualsProperties(properties, kmi->ptr->data)) {
//							if(keymap_r) *keymap_r= keymap;
//							return kmi;
//						}
//					}
//					else {
//						if(keymap_r) *keymap_r= keymap;
//						return kmi;
//					}
//				}
//			}
//		}
//	}
//	
//	/* ensure un-initialized keymap is never used */
//	if(keymap_r) *keymap_r= NULL;
	return null;
}

static wmKeyMapItem wm_keymap_item_find_props(bContext C, String opname, int opcontext, IDProperty properties, int compare_props, int hotkey, wmKeyMap[][] keymap_r)
{
	wmWindow win= bContext.CTX_wm_window(C);
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	wmKeyMapItem found= null;

	/* look into multiple handler lists to find the item */
	if(win!=null)
		found= wm_keymap_item_find_handlers(C, win.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);
	

	if(sa!=null && found==null)
		found= wm_keymap_item_find_handlers(C, sa.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);

	if(found==null) {
		if((opcontext == WmTypes.WM_OP_EXEC_REGION_WIN || opcontext == WmTypes.WM_OP_INVOKE_REGION_WIN)) {
			if(sa!=null) {
				if (!(ar!=null && ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW))
					ar= ScreenUtil.BKE_area_find_region_type(sa, ScreenTypes.RGN_TYPE_WINDOW);
				
				if(ar!=null)
					found= wm_keymap_item_find_handlers(C, ar.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);
			}
		}
		else if((opcontext == WmTypes.WM_OP_EXEC_REGION_CHANNELS || opcontext == WmTypes.WM_OP_INVOKE_REGION_CHANNELS)) {
			if (!(ar!=null && ar.regiontype == ScreenTypes.RGN_TYPE_CHANNELS))
					ar= ScreenUtil.BKE_area_find_region_type(sa, ScreenTypes.RGN_TYPE_CHANNELS);
				
				if(ar!=null)
					found= wm_keymap_item_find_handlers(C, ar.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);
		}
		else if((opcontext == WmTypes.WM_OP_EXEC_REGION_PREVIEW || opcontext == WmTypes.WM_OP_INVOKE_REGION_PREVIEW)) {
			if (!(ar!=null && ar.regiontype == ScreenTypes.RGN_TYPE_PREVIEW))
					ar= ScreenUtil.BKE_area_find_region_type(sa, ScreenTypes.RGN_TYPE_PREVIEW);
				
				if(ar!=null)
					found= wm_keymap_item_find_handlers(C, ar.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);
		}
		else {
			if(ar!=null)
				found= wm_keymap_item_find_handlers(C, ar.handlers, opname, opcontext, properties, compare_props, hotkey, keymap_r);
		}
	}
	
	return found;
}

static wmKeyMapItem wm_keymap_item_find(bContext C, String opname, int opcontext, IDProperty properties, int hotkey, wmKeyMap[][] keymap_r)
{
	wmKeyMapItem found= wm_keymap_item_find_props(C, opname, opcontext, properties, 1, hotkey, keymap_r);

	if(found==null)
		found= wm_keymap_item_find_props(C, opname, opcontext, null, 0, hotkey, keymap_r);

	return found;
}

public static byte[] WM_key_event_operator_string(bContext C, String opname, int opcontext, IDProperty properties, byte[] str, int len)
{
	wmKeyMapItem kmi= wm_keymap_item_find(C, opname, opcontext, properties, 0, null);

	if(kmi!=null) {
		wm_keymap_item_to_string(kmi, str, len);
		return str;
	}

	return null;
}

//int WM_key_event_operator_id(const bContext *C, const char *opname, int opcontext, IDProperty *properties, int hotkey, wmKeyMap **keymap_r)
//{
//	wmKeyMapItem *kmi= wm_keymap_item_find(C, opname, opcontext, properties, hotkey, keymap_r);
//	
//	if(kmi)
//		return kmi->id;
//	else
//		return 0;
//}
//
//int	WM_keymap_item_compare(wmKeyMapItem *k1, wmKeyMapItem *k2)
//{
//	int k1type, k2type;
//
//	if (k1->flag & KMI_INACTIVE || k2->flag & KMI_INACTIVE)
//		return 0;
//
//	/* take event mapping into account */
//	k1type = WM_userdef_event_map(k1->type);
//	k2type = WM_userdef_event_map(k2->type);
//
//	if(k1type != KM_ANY && k2type != KM_ANY && k1type != k2type)
//		return 0;
//
//	if(k1->val != KM_ANY && k2->val != KM_ANY) {
//		/* take click, press, release conflict into account */
//		if (k1->val == KM_CLICK && ELEM3(k2->val, KM_PRESS, KM_RELEASE, KM_CLICK) == 0)
//			return 0;
//		if (k2->val == KM_CLICK && ELEM3(k1->val, KM_PRESS, KM_RELEASE, KM_CLICK) == 0)
//			return 0;
//		if (k1->val != k2->val)
//			return 0;
//	}
//
//	if(k1->shift != KM_ANY && k2->shift != KM_ANY && k1->shift != k2->shift)
//		return 0;
//
//	if(k1->ctrl != KM_ANY && k2->ctrl != KM_ANY && k1->ctrl != k2->ctrl)
//		return 0;
//
//	if(k1->alt != KM_ANY && k2->alt != KM_ANY && k1->alt != k2->alt)
//		return 0;
//
//	if(k1->oskey != KM_ANY && k2->oskey != KM_ANY && k1->oskey != k2->oskey)
//		return 0;
//
//	if(k1->keymodifier != k2->keymodifier)
//		return 0;
//
//	return 1;
//}
//
///* ***************** user preferences ******************* */
//
//int WM_keymap_user_init(wmWindowManager *wm, wmKeyMap *keymap)
//{
//	wmKeyConfig *keyconf;
//	wmKeyMap *km;
//
//	if(!keymap)
//		return 0;
//
//	/* init from user key config */
//	keyconf= wm_keyconfig_list_find(&wm->keyconfigs, U.keyconfigstr);
//	if(keyconf) {
//		km= WM_keymap_list_find(&keyconf->keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//		if(km) {
//			keymap->poll= km->poll; /* lazy init */
//			keymap->modal_items= km->modal_items;
//			return 1;
//		}
//	}
//
//	/* or from default */
//	km= WM_keymap_list_find(&wm->defaultconf->keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//	if(km) {
//		keymap->poll= km->poll; /* lazy init */
//		keymap->modal_items= km->modal_items;
//		return 1;
//	}
//
//	return 0;
//}

public static wmKeyMap WM_keymap_active(wmWindowManager wm, wmKeyMap keymap)
{
	wmKeyConfig keyconf;
	wmKeyMap km;

	if(keymap==null)
		return null;
	
	/* first user defined keymaps */
//	km= WM_keymap_list_find(&U.keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//	if(km) {
//		km->poll= keymap->poll; /* lazy init */
//		km->modal_items= keymap->modal_items;
//		return km;
//	}
	
	/* then user key config */
//	keyconf= wm_keyconfig_list_find(&wm->keyconfigs, U.keyconfigstr);
//	if(keyconf) {
//		km= WM_keymap_list_find(&keyconf->keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//		if(km) {
//			km->poll= keymap->poll; /* lazy init */
//			km->modal_items= keymap->modal_items;
//			return km;
//		}
//	}

	/* then use default */
	km= WM_keymap_list_find(wm.defaultconf.keymaps, StringUtil.toJString(keymap.idname,0), keymap.spaceid, keymap.regionid);
	return km;
}

//wmKeyMap *WM_keymap_copy_to_user(wmKeyMap *keymap)
//{
//	wmKeyMap *usermap;
//	wmKeyMapItem *kmi;
//
//	usermap= WM_keymap_list_find(&U.keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//
//	/* XXX this function is only used by RMB setting hotkeys, and it clears maps on 2nd try this way */
//	if(keymap==usermap)
//		return keymap;
//	
//	if(!usermap) {
//		/* not saved yet, duplicate existing */
//		usermap= MEM_dupallocN(keymap);
//		usermap->modal_items= NULL;
//		usermap->poll= NULL;
//		usermap->flag |= KEYMAP_USER;
//
//		BLI_addtail(&U.keymaps, usermap);
//	}
//	else {
//		/* already saved, free items for re-copy */
//		WM_keymap_free(usermap);
//	}
//
//	BLI_duplicatelist(&usermap->items, &keymap->items);
//
//	for(kmi=usermap->items.first; kmi; kmi=kmi->next) {
//		if(kmi->properties) {
//			kmi->ptr= MEM_callocN(sizeof(PointerRNA), "UserKeyMapItemPtr");
//			WM_operator_properties_create(kmi->ptr, kmi->idname);
//
//			kmi->properties= IDP_CopyProperty(kmi->properties);
//			kmi->ptr->data= kmi->properties;
//		}
//	}
//
//	for(kmi=keymap->items.first; kmi; kmi=kmi->next)
//		kmi->flag &= ~KMI_EXPANDED;
//
//	return usermap;
//}
//
//void WM_keymap_restore_item_to_default(bContext *C, wmKeyMap *keymap, wmKeyMapItem *kmi)
//{
//	wmWindowManager *wm = CTX_wm_manager(C);
//	wmKeyConfig *keyconf;
//	wmKeyMap *km = NULL;
//
//	/* look in user key config */
//	keyconf= wm_keyconfig_list_find(&wm->keyconfigs, U.keyconfigstr);
//	if(keyconf) {
//		km= WM_keymap_list_find(&keyconf->keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//	}
//
//	if (!km) {
//		/* or from default */
//		km= WM_keymap_list_find(&wm->defaultconf->keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//	}
//
//	if (km) {
//		wmKeyMapItem *orig = WM_keymap_item_find_id(km, kmi->id);
//
//		if (orig) {
//			if(strcmp(orig->idname, kmi->idname) != 0) {
//				BLI_strncpy(kmi->idname, orig->idname, sizeof(kmi->idname));
//
//				WM_keymap_properties_reset(kmi, NULL);
//			}
//			
//			if (orig->properties) {
//				kmi->properties= IDP_CopyProperty(orig->properties);
//				kmi->ptr->data= kmi->properties;
//			}
//
//			kmi->propvalue = orig->propvalue;
//			kmi->type = orig->type;
//			kmi->val = orig->val;
//			kmi->shift = orig->shift;
//			kmi->ctrl = orig->ctrl;
//			kmi->alt = orig->alt;
//			kmi->oskey = orig->oskey;
//			kmi->keymodifier = orig->keymodifier;
//			kmi->maptype = orig->maptype;
//
//		}
//
//	}
//}
//
//void WM_keymap_restore_to_default(wmKeyMap *keymap)
//{
//	wmKeyMap *usermap;
//
//	usermap= WM_keymap_list_find(&U.keymaps, keymap->idname, keymap->spaceid, keymap->regionid);
//
//	if(usermap) {
//		WM_keymap_free(usermap);
//		BLI_freelinkN(&U.keymaps, usermap);
//	}
//}
//
//wmKeyMapItem *WM_keymap_item_find_id(wmKeyMap *keymap, int id)
//{
//	wmKeyMapItem *kmi;
//	
//	for (kmi=keymap->items.first; kmi; kmi=kmi->next) {
//		if (kmi->id == id) {
//			return kmi;
//		}
//	}
//	
//	return NULL;
//}
//
///* Guess an appropriate keymap from the operator name */
///* Needs to be kept up to date with Keymap and Operator naming */
//wmKeyMap *WM_keymap_guess_opname(const bContext *C, const char *opname)
//{
//	wmKeyMap *km=NULL;
//	SpaceLink *sl = CTX_wm_space_data(C);
//	
//	/* Window */
//	if (strstr(opname, "WM_OT")) {
//		km = WM_keymap_find_all(C, "Window", 0, 0);
//	}
//	/* Screen */
//	else if (strstr(opname, "SCREEN_OT")) {
//		km = WM_keymap_find_all(C, "Screen", 0, 0);
//	}
//	/* Grease Pencil */
//	else if (strstr(opname, "GPENCIL_OT")) {
//		km = WM_keymap_find_all(C, "Grease Pencil", 0, 0);
//	}
//	/* Markers */
//	else if (strstr(opname, "MARKER_OT")) {
//		km = WM_keymap_find_all(C, "Markers", 0, 0);
//	}
//	
//	
//	/* 3D View */
//	else if (strstr(opname, "VIEW3D_OT")) {
//		km = WM_keymap_find_all(C, "3D View", sl->spacetype, 0);
//	}
//	else if (strstr(opname, "OBJECT_OT")) {
//		km = WM_keymap_find_all(C, "Object Mode", 0, 0);
//	}
//
//	
//	/* Editing Modes */
//	else if (strstr(opname, "MESH_OT")) {
//		km = WM_keymap_find_all(C, "Mesh", 0, 0);
//	}
//	else if (strstr(opname, "CURVE_OT")) {
//		km = WM_keymap_find_all(C, "Curve", 0, 0);
//	}
//	else if (strstr(opname, "ARMATURE_OT")) {
//		km = WM_keymap_find_all(C, "Armature", 0, 0);
//	}
//	else if (strstr(opname, "POSE_OT")) {
//		km = WM_keymap_find_all(C, "Pose", 0, 0);
//	}
//	else if (strstr(opname, "SCULPT_OT")) {
//		km = WM_keymap_find_all(C, "Sculpt", 0, 0);
//	}
//	else if (strstr(opname, "MBALL_OT")) {
//		km = WM_keymap_find_all(C, "Metaball", 0, 0);
//	}
//	else if (strstr(opname, "LATTICE_OT")) {
//		km = WM_keymap_find_all(C, "Lattice", 0, 0);
//	}
//	else if (strstr(opname, "PARTICLE_OT")) {
//		km = WM_keymap_find_all(C, "Particle", 0, 0);
//	}
//	else if (strstr(opname, "FONT_OT")) {
//		km = WM_keymap_find_all(C, "Font", 0, 0);
//	}
//	else if (strstr(opname, "PAINT_OT")) {
//		
//		/* check for relevant mode */
//		switch(CTX_data_mode_enum(C)) {
//			case OB_MODE_WEIGHT_PAINT:
//				km = WM_keymap_find_all(C, "Weight Paint", 0, 0);
//				break;
//			case OB_MODE_VERTEX_PAINT:
//				km = WM_keymap_find_all(C, "Vertex Paint", 0, 0);
//				break;
//			case OB_MODE_TEXTURE_PAINT:
//				km = WM_keymap_find_all(C, "Image Paint", 0, 0);
//				break;
//		}
//	}
//	/* Paint Face Mask */
//	else if (strstr(opname, "PAINT_OT_face_select")) {
//		km = WM_keymap_find_all(C, "Face Mask", sl->spacetype, 0);
//	}
//	/* Timeline */
//	else if (strstr(opname, "TIME_OT")) {
//		km = WM_keymap_find_all(C, "Timeline", sl->spacetype, 0);
//	}
//	/* Image Editor */
//	else if (strstr(opname, "IMAGE_OT")) {
//		km = WM_keymap_find_all(C, "Image", sl->spacetype, 0);
//	}
//	/* UV Editor */
//	else if (strstr(opname, "UV_OT")) {
//		km = WM_keymap_find_all(C, "UV Editor", sl->spacetype, 0);
//	}
//	/* Node Editor */
//	else if (strstr(opname, "NODE_OT")) {
//		km = WM_keymap_find_all(C, "Node Editor", sl->spacetype, 0);
//	}
//	/* Animation Editor Channels */
//	else if (strstr(opname, "ANIM_OT_channels")) {
//		km = WM_keymap_find_all(C, "Animation Channels", sl->spacetype, 0);
//	}
//	/* Animation Generic - after channels */
//	else if (strstr(opname, "ANIM_OT")) {
//		km = WM_keymap_find_all(C, "Animation", 0, 0);
//	}
//	/* Graph Editor */
//	else if (strstr(opname, "GRAPH_OT")) {
//		km = WM_keymap_find_all(C, "Graph Editor", sl->spacetype, 0);
//	}
//	/* Dopesheet Editor */
//	else if (strstr(opname, "ACTION_OT")) {
//		km = WM_keymap_find_all(C, "Dopesheet", sl->spacetype, 0);
//	}
//	/* NLA Editor */
//	else if (strstr(opname, "NLA_OT")) {
//		km = WM_keymap_find_all(C, "NLA Editor", sl->spacetype, 0);
//	}
//	/* Script */
//	else if (strstr(opname, "SCRIPT_OT")) {
//		km = WM_keymap_find_all(C, "Script", sl->spacetype, 0);
//	}
//	/* Text */
//	else if (strstr(opname, "TEXT_OT")) {
//		km = WM_keymap_find_all(C, "Text", sl->spacetype, 0);
//	}
//	/* Sequencer */
//	else if (strstr(opname, "SEQUENCER_OT")) {
//		km = WM_keymap_find_all(C, "Sequencer", sl->spacetype, 0);
//	}
//	/* Console */
//	else if (strstr(opname, "CONSOLE_OT")) {
//		km = WM_keymap_find_all(C, "Console", sl->spacetype, 0);
//	}
//	/* Console */
//	else if (strstr(opname, "INFO_OT")) {
//		km = WM_keymap_find_all(C, "Info", sl->spacetype, 0);
//	}
//	
//	/* Transform */
//	else if (strstr(opname, "TRANSFORM_OT")) {
//		
//		/* check for relevant editor */
//		switch(sl->spacetype) {
//			case SPACE_VIEW3D:
//				km = WM_keymap_find_all(C, "3D View", sl->spacetype, 0);
//				break;
//			case SPACE_IPO:
//				km = WM_keymap_find_all(C, "Graph Editor", sl->spacetype, 0);
//				break;
//			case SPACE_ACTION:
//				km = WM_keymap_find_all(C, "Dopesheet", sl->spacetype, 0);
//				break;
//			case SPACE_NLA:
//				km = WM_keymap_find_all(C, "NLA Editor", sl->spacetype, 0);
//				break;
//			case SPACE_IMAGE:
//				km = WM_keymap_find_all(C, "UV Editor", sl->spacetype, 0);
//				break;
//			case SPACE_NODE:
//				km = WM_keymap_find_all(C, "Node Editor", sl->spacetype, 0);
//				break;
//			case SPACE_SEQ:
//				km = WM_keymap_find_all(C, "Sequencer", sl->spacetype, 0);
//				break;
//		}
//	}
//	
//	return km;
//}

}

