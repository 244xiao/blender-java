package blender.blenkernel;

import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.blenlib.ListBaseUtil.OffsetOf;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;

public class IdProp {

//	/**
//	 * $Id: BKE_idprop.h 34680 2011-02-07 05:05:41Z campbellbarton $
//	 *
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
//	 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
//	 * All rights reserved.
//	 *
//	 * Contributor(s): Joseph Eagar
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//	 
//	#ifndef _BKE_IDPROP_H
//	#define _BKE_IDPROP_H
//
//	#include "DNA_ID.h"
//
//	struct IDProperty;
//	struct ID;

	public static class IDPropertyTemplate {
		private Object hi;
		private Object lo;

//		public int i;
//		public float f;
//		public double d;
//		public char *str;
//		public struct ID *id;
//		public struct {
//			short type;
//			short len;
//		} array;
//		public struct {
//			int matvec_size;
//			float *example;
//		} matrix_or_vector;
		
		public int i() { return (Integer)lo; }
		public void i(int i) { lo = i; }
		public float f() { return Float.intBitsToFloat((Integer)lo); }
		public void f(float f) { lo = Float.floatToIntBits(f); }
		public double d() { return Double.longBitsToDouble((((Integer)hi)<<32)|((Integer)lo)); }
		public void d(double d) { hi = (int)(Double.doubleToLongBits(d)>>32); lo = (int)Double.doubleToLongBits(d); }
		public byte[] str() { return (byte[])lo; }
		public void str(byte[] str) { lo = str; }
		public ID id() { return (ID)lo; }
		public void id(ID id) { lo = id; }
		public short array_type() { return (short)i(); }
		public void array_type(short type) { i((i()<<16)|(type&0xFFFF)); }
		public short array_len() { return (short)(i()>>16); }
		public void array_len(short len) { i((len<<16)|(i()&0xFFFF)); }
		public int matrix_or_vector_matvec_size() { return i(); }
		public void matrix_or_vector_matvec_size(int matvec_size) { i(matvec_size); }
		public float[] matrix_or_vector_example() { return (float[])hi; }
		public void matrix_or_vector_example(float[] example) { hi = example; }
	};

//	/* ----------- Property Array Type ---------- */
//
//	/*note: as a start to move away from the stupid IDP_New function, this type
//	  has it's own allocation function.*/
//	IDProperty *IDP_NewIDPArray(const char *name);
//	IDProperty *IDP_CopyIDPArray(IDProperty *array);
//
//	void IDP_FreeIDPArray(IDProperty *prop);
//
//	/* shallow copies item */
//	void IDP_SetIndexArray(struct IDProperty *prop, int index, struct IDProperty *item);
//	struct IDProperty *IDP_GetIndexArray(struct IDProperty *prop, int index);
//	struct IDProperty *IDP_AppendArray(struct IDProperty *prop, struct IDProperty *item);
//	void IDP_ResizeIDPArray(struct IDProperty *prop, int len);
//
//	/* ----------- Numeric Array Type ----------- */
//	/*this function works for strings too!*/
//	void IDP_ResizeArray(struct IDProperty *prop, int newlen);
//	void IDP_FreeArray(struct IDProperty *prop);
//	void IDP_UnlinkArray(struct IDProperty *prop);
//
//	/* ---------- String Type ------------ */
//	IDProperty *IDP_NewString(const char *st, const char *name, int maxlen);/* maxlen excludes '\0' */
//	void IDP_AssignString(struct IDProperty *prop, const char *st, int maxlen);	/* maxlen excludes '\0' */
//	void IDP_ConcatStringC(struct IDProperty *prop, const char *st);
//	void IDP_ConcatString(struct IDProperty *str1, struct IDProperty *append);
//	void IDP_FreeString(struct IDProperty *prop);
//
//	/*-------- ID Type -------*/
//	void IDP_LinkID(struct IDProperty *prop, ID *id);
//	void IDP_UnlinkID(struct IDProperty *prop);
//
//	/*-------- Group Functions -------*/
//
//	/* Sync values from one group to another, only where they match */
//	void IDP_SyncGroupValues(struct IDProperty *dest, struct IDProperty *src);
//
//	/*
//	 replaces all properties with the same name in a destination group from a source group.
//	*/
//	void IDP_ReplaceGroupInGroup(struct IDProperty *dest, struct IDProperty *src);
//
//	/*checks if a property with the same name as prop exists, and if so replaces it.
//	  Use this to preserve order!*/
//	void IDP_ReplaceInGroup(struct IDProperty *group, struct IDProperty *prop);
//
//	/*
//	This function has a sanity check to make sure ID properties with the same name don't
//	get added to the group.
//
//	The sanity check just means the property is not added to the group if another property
//	exists with the same name; the client code using ID properties then needs to detect this 
//	(the function that adds new properties to groups, IDP_AddToGroup, returns 0 if a property can't
//	be added to the group, and 1 if it can) and free the property.
//
//	Currently the code to free ID properties is designesd to leave the actual struct
//	you pass it un-freed, this is needed for how the system works.  This means
//	to free an ID property, you first call IDP_FreeProperty then MEM_freeN the
//	struct.  In the future this will just be IDP_FreeProperty and the code will
//	be reorganized to work properly.
//	*/
//	int IDP_AddToGroup(struct IDProperty *group, struct IDProperty *prop);
//
//	/*this is the same as IDP_AddToGroup, only you pass an item
//	  in the group list to be inserted after.*/
//	int IDP_InsertToGroup(struct IDProperty *group, struct IDProperty *previous, 
//						  struct IDProperty *pnew);
//
//	/*NOTE: this does not free the property!!
//
//	 To free the property, you have to do:
//	 IDP_FreeProperty(prop); //free all subdata
//	 MEM_freeN(prop); //free property struct itself
//
//	*/
//	void IDP_RemFromGroup(struct IDProperty *group, struct IDProperty *prop);
//
//	IDProperty *IDP_GetPropertyFromGroup(struct IDProperty *prop, const char *name);
//	/* same as above but ensure type match */
//	IDProperty *IDP_GetPropertyTypeFromGroup(struct IDProperty *prop, const char *name, const char type);
//
//	/*Get an iterator to iterate over the members of an id property group.
//	 Note that this will automatically free the iterator once iteration is complete;
//	 if you stop the iteration before hitting the end, make sure to call
//	 IDP_FreeIterBeforeEnd().*/
//	void *IDP_GetGroupIterator(struct IDProperty *prop);
//
//	/*Returns the next item in the iteration.  To use, simple for a loop like the following:
//	 while (IDP_GroupIterNext(iter) != NULL) {
//		. . .
//	 }*/
//	IDProperty *IDP_GroupIterNext(void *vself);
//
//	/*Frees the iterator pointed to at vself, only use this if iteration is stopped early; 
//	  when the iterator hits the end of the list it'll automatially free itself.*/
//	void IDP_FreeIterBeforeEnd(void *vself);
//
//	/*-------- Main Functions --------*/
//	/*Get the Group property that contains the id properties for ID id.  Set create_if_needed
//	  to create the Group property and attach it to id if it doesn't exist; otherwise
//	  the function will return NULL if there's no Group property attached to the ID.*/
//	struct IDProperty *IDP_GetProperties(struct ID *id, int create_if_needed);
//	struct IDProperty *IDP_CopyProperty(struct IDProperty *prop);
//
//	int IDP_EqualsProperties(struct IDProperty *prop1, struct IDProperty *prop2);
//
//	/*
//	Allocate a new ID.
//
//	This function takes three arguments: the ID property type, a union which defines
//	it's initial value, and a name.
//
//	The union is simple to use; see the top of this header file for its definition. 
//	An example of using this function:
//
//	 IDPropertyTemplate val;
//	 IDProperty *group, *idgroup, *color;
//	 group = IDP_New(IDP_GROUP, val, "group1"); //groups don't need a template.
//
//	 val.array.len = 4
//	 val.array.type = IDP_FLOAT;
//	 color = IDP_New(IDP_ARRAY, val, "color1");
//
//	 idgroup = IDP_GetProperties(some_id, 1);
//	 IDP_AddToGroup(idgroup, color);
//	 IDP_AddToGroup(idgroup, group);
//
//	Note that you MUST either attach the id property to an id property group with 
//	IDP_AddToGroup or MEM_freeN the property, doing anything else might result in
//	a memory leak.
//	*/
//	struct IDProperty *IDP_New(int type, IDPropertyTemplate val, const char *name);
//	\
//	/*NOTE: this will free all child properties of list arrays and groups!
//	  Also, note that this does NOT unlink anything!  Plus it doesn't free
//	  the actual struct IDProperty struct either.*/
//	void IDP_FreeProperty(struct IDProperty *prop);
//
//	/*Unlinks any struct IDProperty<->ID linkage that might be going on.*/
//	void IDP_UnlinkProperty(struct IDProperty *prop);
//
//	#define IDP_Int(prop) ((prop)->data.val)
	public static final int IDP_Int(IDProperty prop) { return prop.data.val; }
	public static final void IDP_Int(IDProperty prop, int value) { prop.data.val = value; }
//	#define IDP_Float(prop) (*(float*)&(prop)->data.val)
//	#define IDP_String(prop) ((char*)(prop)->data.pointer)
//	#define IDP_Array(prop) ((prop)->data.pointer)
	public static final Object IDP_Array(IDProperty prop) { return prop.data.pointer; }
	public static final void IDP_Array(IDProperty prop, Object value) { prop.data.pointer = value; }
//	#define IDP_IDPArray(prop) ((IDProperty*)(prop)->data.pointer)
//	#define IDP_Double(prop) (*(double*)&(prop)->data.val)
//
//	#endif /* _BKE_IDPROP_H */
//
//	/**
//	 * $Id: idprop.c 34680 2011-02-07 05:05:41Z campbellbarton $
//	 *
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
//	 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
//	 * All rights reserved.
//	 *
//	 * Contributor(s): Joseph Eagar
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//	 
//	#include <stdio.h>
//	#include <stdlib.h>
//	#include <stddef.h>
//	#include <string.h>
//
//	#include "BKE_idprop.h"
//	#include "BKE_library.h"
//
//	#include "BLI_blenlib.h"
//
//	#include "MEM_guardedalloc.h"
//
//	#define BSTR_EQ(a, b)	(*(a) == *(b) && !strcmp(a, b))
//
//	/* IDPropertyTemplate is a union in DNA_ID.h */
//
//	/*local size table.*/
//	static char idp_size_table[] = {
//		1, /*strings*/
//		sizeof(int),
//		sizeof(float),
//		sizeof(float)*3, /*Vector type, deprecated*/
//		sizeof(float)*16, /*Matrix type, deprecated*/
//		0, /*arrays don't have a fixed size*/
//		sizeof(ListBase), /*Group type*/
//		sizeof(void*),
//		sizeof(double)
//	};
//
//	/* ------------Property Array Type ----------- */
//	#define GETPROP(prop, i) (((IDProperty*)(prop)->data.pointer)+(i))
//
//	/* --------- property array type -------------*/
//
//	/*note: as a start to move away from the stupid IDP_New function, this type
//	  has it's own allocation function.*/
//	IDProperty *IDP_NewIDPArray(const char *name)
//	{
//		IDProperty *prop = MEM_callocN(sizeof(IDProperty), "IDProperty prop array");
//		prop->type = IDP_IDPARRAY;
//		prop->len = 0;
//		BLI_strncpy(prop->name, name, MAX_IDPROP_NAME);
//		
//		return prop;
//	}
//
//	IDProperty *IDP_CopyIDPArray(IDProperty *array)
//	{
//		IDProperty *narray = MEM_dupallocN(array), *tmp;
//		int i;
//		
//		narray->data.pointer = MEM_dupallocN(array->data.pointer);
//		for (i=0; i<narray->len; i++) {
//			/*ok, the copy functions always allocate a new structure,
//			  which doesn't work here.  instead, simply copy the
//			  contents of the new structure into the array cell,
//			  then free it.  this makes for more maintainable
//			  code than simply reimplementing the copy functions
//			  in this loop.*/
//			tmp = IDP_CopyProperty(GETPROP(narray, i));
//			memcpy(GETPROP(narray, i), tmp, sizeof(IDProperty));
//			MEM_freeN(tmp);
//		}
//		
//		return narray;
//	}
//
//	void IDP_FreeIDPArray(IDProperty *prop)
//	{
//		int i;
//		
//		for (i=0; i<prop->len; i++)
//			IDP_FreeProperty(GETPROP(prop, i));
//
//		if(prop->data.pointer)
//			MEM_freeN(prop->data.pointer);
//	}
//
//	/*shallow copies item*/
//	void IDP_SetIndexArray(IDProperty *prop, int index, IDProperty *item)
//	{
//		IDProperty *old = GETPROP(prop, index);
//		if (index >= prop->len || index < 0) return;
//		if (item != old) IDP_FreeProperty(old);
//		
//		memcpy(GETPROP(prop, index), item, sizeof(IDProperty));
//	}
//
//	IDProperty *IDP_GetIndexArray(IDProperty *prop, int index)
//	{
//		return GETPROP(prop, index);
//	}
//
//	IDProperty *IDP_AppendArray(IDProperty *prop, IDProperty *item)
//	{
//		IDP_ResizeIDPArray(prop, prop->len+1);
//		IDP_SetIndexArray(prop, prop->len-1, item);
//		return item;
//	}
//
//	void IDP_ResizeIDPArray(IDProperty *prop, int newlen)
//	{
//		void *newarr;
//		int newsize=newlen;
//
//		/*first check if the array buffer size has room*/
//		/*if newlen is 200 chars less then totallen, reallocate anyway*/
//		if (newlen <= prop->totallen && prop->totallen - newlen < 200) {
//			int i;
//
//			for(i=newlen; i<prop->len; i++)
//				IDP_FreeProperty(GETPROP(prop, i));
//
//			prop->len = newlen;
//			return;
//		}
//
//		/* - Note: This code comes from python, here's the corrusponding comment. - */
//		/* This over-allocates proportional to the list size, making room
//		 * for additional growth.  The over-allocation is mild, but is
//		 * enough to give linear-time amortized behavior over a long
//		 * sequence of appends() in the presence of a poorly-performing
//		 * system realloc().
//		 * The growth pattern is:  0, 4, 8, 16, 25, 35, 46, 58, 72, 88, ...
//		 */
//		newsize = (newsize >> 3) + (newsize < 9 ? 3 : 6) + newsize;
//
//		newarr = MEM_callocN(sizeof(IDProperty)*newsize, "idproperty array resized");
//		if (newlen >= prop->len) {
//			/* newlen is bigger*/
//			memcpy(newarr, prop->data.pointer, prop->len*sizeof(IDProperty));
//		}
//		else {
//			int i;
//			/* newlen is smaller*/
//			for (i=newlen; i<prop->len; i++) {
//				IDP_FreeProperty(GETPROP(prop, i));
//			}
//			memcpy(newarr, prop->data.pointer, newlen*sizeof(IDProperty));
//		}
//
//		if(prop->data.pointer)
//			MEM_freeN(prop->data.pointer);
//		prop->data.pointer = newarr;
//		prop->len = newlen;
//		prop->totallen = newsize;
//	}
//
//	/* ----------- Numerical Array Type ----------- */
//	static void idp_resize_group_array(IDProperty *prop, int newlen, void *newarr)
//	{
//		if(prop->subtype != IDP_GROUP)
//			return;
//
//		if(newlen >= prop->len) {
//			/* bigger */
//			IDProperty **array= newarr;
//			IDPropertyTemplate val;
//			int a;
//
//			for(a=prop->len; a<newlen; a++) {
//				val.i = 0; /* silence MSVC warning about uninitialized var when debugging */
//				array[a]= IDP_New(IDP_GROUP, val, "IDP_ResizeArray group");
//			}
//		}
//		else {
//			/* smaller */
//			IDProperty **array= prop->data.pointer;
//			int a;
//
//			for(a=newlen; a<prop->len; a++) {
//				IDP_FreeProperty(array[a]);
//				MEM_freeN(array[a]);
//			}
//		}
//	}
//
//	/*this function works for strings too!*/
//	void IDP_ResizeArray(IDProperty *prop, int newlen)
//	{
//		void *newarr;
//		int newsize=newlen;
//
//		/*first check if the array buffer size has room*/
//		/*if newlen is 200 chars less then totallen, reallocate anyway*/
//		if (newlen <= prop->totallen && prop->totallen - newlen < 200) {
//			idp_resize_group_array(prop, newlen, prop->data.pointer);
//			prop->len = newlen;
//			return;
//		}
//
//		/* - Note: This code comes from python, here's the corrusponding comment. - */
//		/* This over-allocates proportional to the list size, making room
//		 * for additional growth.  The over-allocation is mild, but is
//		 * enough to give linear-time amortized behavior over a long
//		 * sequence of appends() in the presence of a poorly-performing
//		 * system realloc().
//		 * The growth pattern is:  0, 4, 8, 16, 25, 35, 46, 58, 72, 88, ...
//		 */
//		newsize = (newsize >> 3) + (newsize < 9 ? 3 : 6) + newsize;
//
//		newarr = MEM_callocN(idp_size_table[(int)prop->subtype]*newsize, "idproperty array resized");
//		if (newlen >= prop->len) {
//			/* newlen is bigger*/
//			memcpy(newarr, prop->data.pointer, prop->len*idp_size_table[(int)prop->subtype]);
//			idp_resize_group_array(prop, newlen, newarr);
//		}
//		else {
//			/* newlen is smaller*/
//			idp_resize_group_array(prop, newlen, newarr);
//			memcpy(newarr, prop->data.pointer, newlen*prop->len*idp_size_table[(int)prop->subtype]);
//		}
//
//		MEM_freeN(prop->data.pointer);
//		prop->data.pointer = newarr;
//		prop->len = newlen;
//		prop->totallen = newsize;
//	}
//
//	void IDP_FreeArray(IDProperty *prop)
//	{
//		if (prop->data.pointer) {
//			idp_resize_group_array(prop, 0, NULL);
//			MEM_freeN(prop->data.pointer);
//		}
//	}
//
//
//	 static IDProperty *idp_generic_copy(IDProperty *prop)
//	 {
//		IDProperty *newp = MEM_callocN(sizeof(IDProperty), "IDProperty array dup");
//
//		BLI_strncpy(newp->name, prop->name, MAX_IDPROP_NAME);
//		newp->type = prop->type;
//		newp->flag = prop->flag;
//		newp->data.val = prop->data.val;
//		newp->data.val2 = prop->data.val2;
//
//		return newp;
//	 }
//
//	IDProperty *IDP_CopyArray(IDProperty *prop)
//	{
//		IDProperty *newp = idp_generic_copy(prop);
//
//		if (prop->data.pointer) {
//			newp->data.pointer = MEM_dupallocN(prop->data.pointer);
//
//			if(prop->type == IDP_GROUP) {
//				IDProperty **array= newp->data.pointer;
//				int a;
//
//				for(a=0; a<prop->len; a++)
//					array[a]= IDP_CopyProperty(array[a]);
//			}
//		}
//		newp->len = prop->len;
//		newp->subtype = prop->subtype;
//		newp->totallen = prop->totallen;
//
//		return newp;
//	}
//
//	/*taken from readfile.c*/
//	#define SWITCH_LONGINT(a) { \
//		char s_i, *p_i; \
//		p_i= (char *)&(a);  \
//		s_i=p_i[0]; p_i[0]=p_i[7]; p_i[7]=s_i; \
//		s_i=p_i[1]; p_i[1]=p_i[6]; p_i[6]=s_i; \
//		s_i=p_i[2]; p_i[2]=p_i[5]; p_i[5]=s_i; \
//		s_i=p_i[3]; p_i[3]=p_i[4]; p_i[4]=s_i; }
//
//
//
//	/* ---------- String Type ------------ */
//	IDProperty *IDP_NewString(const char *st, const char *name, int maxlen)
//	{
//		IDProperty *prop = MEM_callocN(sizeof(IDProperty), "IDProperty string");
//
//		if (st == NULL) {
//			prop->data.pointer = MEM_callocN(DEFAULT_ALLOC_FOR_NULL_STRINGS, "id property string 1");
//			prop->totallen = DEFAULT_ALLOC_FOR_NULL_STRINGS;
//			prop->len = 1; /*NULL string, has len of 1 to account for null byte.*/
//		}
//		else {
//			int stlen = strlen(st);
//
//			if(maxlen > 0 && maxlen < stlen)
//				stlen = maxlen;
//
//			stlen++; /* null terminator '\0' */
//
//			prop->data.pointer = MEM_callocN(stlen, "id property string 2");
//			prop->len = prop->totallen = stlen;
//			BLI_strncpy(prop->data.pointer, st, stlen);
//		}
//
//		prop->type = IDP_STRING;
//		BLI_strncpy(prop->name, name, MAX_IDPROP_NAME);
//
//		return prop;
//	}
//
//	IDProperty *IDP_CopyString(IDProperty *prop)
//	{
//		IDProperty *newp = idp_generic_copy(prop);
//
//		if (prop->data.pointer) newp->data.pointer = MEM_dupallocN(prop->data.pointer);
//		newp->len = prop->len;
//		newp->subtype = prop->subtype;
//		newp->totallen = prop->totallen;
//
//		return newp;
//	}
//
//
//	void IDP_AssignString(IDProperty *prop, const char *st, int maxlen)
//	{
//		int stlen;
//
//		stlen = strlen(st);
//
//		if(maxlen > 0 && maxlen < stlen)
//			stlen= maxlen;
//
//		stlen++; /* make room for null byte */
//
//		IDP_ResizeArray(prop, stlen);
//		BLI_strncpy(prop->data.pointer, st, stlen);
//	}
//
//	void IDP_ConcatStringC(IDProperty *prop, const char *st)
//	{
//		int newlen;
//
//		newlen = prop->len + strlen(st);
//		/*we have to remember that prop->len includes the null byte for strings.
//		 so there's no need to add +1 to the resize function.*/
//		IDP_ResizeArray(prop, newlen);
//		strcat(prop->data.pointer, st);
//	}
//
//	void IDP_ConcatString(IDProperty *str1, IDProperty *append)
//	{
//		int newlen;
//
//		/*since ->len for strings includes the NULL byte, we have to subtract one or
//		 we'll get an extra null byte after each concatination operation.*/
//		newlen = str1->len + append->len - 1;
//		IDP_ResizeArray(str1, newlen);
//		strcat(str1->data.pointer, append->data.pointer);
//	}
//
//	void IDP_FreeString(IDProperty *prop)
//	{
//		if(prop->data.pointer)
//			MEM_freeN(prop->data.pointer);
//	}
//
//
//	/*-------- ID Type, not in use yet -------*/
//
//	void IDP_LinkID(IDProperty *prop, ID *id)
//	{
//		if (prop->data.pointer) ((ID*)prop->data.pointer)->us--;
//		prop->data.pointer = id;
//		id_us_plus(id);
//	}
//
//	void IDP_UnlinkID(IDProperty *prop)
//	{
//		((ID*)prop->data.pointer)->us--;
//	}
//
//	/*-------- Group Functions -------*/
//
//	/*checks if a property with the same name as prop exists, and if so replaces it.*/
//	IDProperty *IDP_CopyGroup(IDProperty *prop)
//	{
//		IDProperty *newp = idp_generic_copy(prop), *link;
//		newp->len = prop->len;
//		
//		for (link=prop->data.group.first; link; link=link->next) {
//			BLI_addtail(&newp->data.group, IDP_CopyProperty(link));
//		}
//
//		return newp;
//	}
//
//	/* use for syncing proxies.
//	 * When values name and types match, copy the values, else ignore */
//	void IDP_SyncGroupValues(IDProperty *dest, IDProperty *src)
//	{
//		IDProperty *loop, *prop;
//		for (prop=src->data.group.first; prop; prop=prop->next) {
//			for (loop=dest->data.group.first; loop; loop=loop->next) {
//				if (strcmp(loop->name, prop->name)==0) {
//					if(prop->type==loop->type) {
//
//						switch (prop->type) {
//							case IDP_INT:
//							case IDP_FLOAT:
//							case IDP_DOUBLE:
//								loop->data= prop->data;
//								break;
//							case IDP_GROUP:
//								IDP_SyncGroupValues(loop, prop);
//								break;
//							default:
//							{
//								IDProperty *tmp= loop;
//								IDProperty *copy= IDP_CopyProperty(prop);
//
//								BLI_insertlinkafter(&dest->data.group, loop, copy);
//								BLI_remlink(&dest->data.group, tmp);
//
//								IDP_FreeProperty(tmp);
//								MEM_freeN(tmp);
//							}
//						}
//					}
//					break;
//				}
//			}
//		}
//	}
//
//	/*
//	 replaces all properties with the same name in a destination group from a source group.
//	*/
//	void IDP_ReplaceGroupInGroup(IDProperty *dest, IDProperty *src)
//	{
//		IDProperty *loop, *prop;
//		for (prop=src->data.group.first; prop; prop=prop->next) {
//			for (loop=dest->data.group.first; loop; loop=loop->next) {
//				if (BSTR_EQ(loop->name, prop->name)) {
//					IDProperty *copy = IDP_CopyProperty(prop);
//
//					BLI_insertlink(&dest->data.group, loop, copy);
//
//					BLI_remlink(&dest->data.group, loop);
//					IDP_FreeProperty(loop);
//					MEM_freeN(loop);
//					break;
//				}
//			}
//
//			/* only add at end if not added yet */
//			if (loop == NULL) {
//				IDProperty *copy = IDP_CopyProperty(prop);
//				dest->len++;
//				BLI_addtail(&dest->data.group, copy);
//			}
//		}
//	}
//	/*
//	 replaces a property with the same name in a group, or adds 
//	 it if the propery doesn't exist.
//	*/
//	void IDP_ReplaceInGroup(IDProperty *group, IDProperty *prop)
//	{
//		IDProperty *loop;
//		if((loop= IDP_GetPropertyFromGroup(group, prop->name)))  {
//			BLI_insertlink(&group->data.group, loop, prop);
//			
//			BLI_remlink(&group->data.group, loop);
//			IDP_FreeProperty(loop);
//			MEM_freeN(loop);			
//		}
//		else {
//			group->len++;
//			BLI_addtail(&group->data.group, prop);
//		}
//	}

	/*returns 0 if an id property with the same name exists and it failed,
	  or 1 if it succeeded in adding to the group.*/
	public static boolean IDP_AddToGroup(IDProperty group, IDProperty prop)
	{
		if(IDP_GetPropertyFromGroup(group, prop.name,0) == null)  {
			group.len++;
			ListBaseUtil.BLI_addtail(group.data.group, prop);
			return true;
		}

		return false;
	}

//	int IDP_InsertToGroup(IDProperty *group, IDProperty *previous, IDProperty *pnew)
//	{
//		if(IDP_GetPropertyFromGroup(group, pnew->name) == NULL)  {
//			group->len++;
//			BLI_insertlink(&group->data.group, previous, pnew);
//			return 1;
//		}
//
//		return 0;
//	}
//
//	void IDP_RemFromGroup(IDProperty *group, IDProperty *prop)
//	{
//		group->len--;
//		BLI_remlink(&group->data.group, prop);
//	}

	public static Object IDP_GetPropertyFromGroup(IDProperty prop, byte[] name, int offset)
	{
		OffsetOf offsetof_name = new OffsetOf() {
			public String get(Link link) {
				return StringUtil.toJString(((IDProperty)link).name,0);
			}
		};
		return ListBaseUtil.BLI_findstring(prop.data.group, StringUtil.toJString(name,offset), offsetof_name);
	}

//	IDProperty *IDP_GetPropertyTypeFromGroup(IDProperty *prop, const char *name, const char type)
//	{
//		IDProperty *idprop= IDP_GetPropertyFromGroup(prop, name);
//		return (idprop && idprop->type == type) ? idprop : NULL;
//	}
//
//	typedef struct IDPIter {
//		void *next;
//		IDProperty *parent;
//	} IDPIter;
//
//	void *IDP_GetGroupIterator(IDProperty *prop)
//	{
//		IDPIter *iter = MEM_callocN(sizeof(IDPIter), "IDPIter");
//		iter->next = prop->data.group.first;
//		iter->parent = prop;
//		return (void*) iter;
//	}
//
//	IDProperty *IDP_GroupIterNext(void *vself)
//	{
//		IDPIter *self = (IDPIter*) vself;
//		Link *next = (Link*) self->next;
//		if (self->next == NULL) {
//			MEM_freeN(self);
//			return NULL;
//		}
//
//		self->next = next->next;
//		return (void*) next;
//	}
//
//	void IDP_FreeIterBeforeEnd(void *vself)
//	{
//		MEM_freeN(vself);
//	}
//
//	/*Ok, the way things work, Groups free the ID Property structs of their children.
//	  This is because all ID Property freeing functions free only direct data (not the ID Property
//	  struct itself), but for Groups the child properties *are* considered
//	  direct data.*/
//	static void IDP_FreeGroup(IDProperty *prop)
//	{
//		IDProperty *loop;
//		for (loop=prop->data.group.first; loop; loop=loop->next)
//		{
//			IDP_FreeProperty(loop);
//		}
//		BLI_freelistN(&prop->data.group);
//	}
//
//
//	/*-------- Main Functions --------*/
//	IDProperty *IDP_CopyProperty(IDProperty *prop)
//	{
//		switch (prop->type) {
//			case IDP_GROUP: return IDP_CopyGroup(prop);
//			case IDP_STRING: return IDP_CopyString(prop);
//			case IDP_ARRAY: return IDP_CopyArray(prop);
//			case IDP_IDPARRAY: return IDP_CopyIDPArray(prop);
//			default: return idp_generic_copy(prop);
//		}
//	}
//
//	IDProperty *IDP_GetProperties(ID *id, int create_if_needed)
//	{
//		if (id->properties) return id->properties;
//		else {
//			if (create_if_needed) {
//				id->properties = MEM_callocN(sizeof(IDProperty), "IDProperty");
//				id->properties->type = IDP_GROUP;
//				/* dont overwite the data's name and type
//				 * some functions might need this if they
//				 * dont have a real ID, should be named elsewhere - Campbell */
//				/* strcpy(id->name, "top_level_group");*/
//			}
//			return id->properties;
//		}
//	}
//
//	int IDP_EqualsProperties(IDProperty *prop1, IDProperty *prop2)
//	{
//		if(prop1 == NULL && prop2 == NULL)
//			return 1;
//		else if(prop1 == NULL || prop2 == NULL)
//			return 0;
//		else if(prop1->type != prop2->type)
//			return 0;
//
//		if(prop1->type == IDP_INT)
//			return (IDP_Int(prop1) == IDP_Int(prop2));
//		else if(prop1->type == IDP_FLOAT)
//			return (IDP_Float(prop1) == IDP_Float(prop2));
//		else if(prop1->type == IDP_DOUBLE)
//			return (IDP_Double(prop1) == IDP_Double(prop2));
//		else if(prop1->type == IDP_STRING)
//			return BSTR_EQ(IDP_String(prop1), IDP_String(prop2));
//		else if(prop1->type == IDP_ARRAY) {
//			if(prop1->len == prop2->len && prop1->subtype == prop2->subtype)
//				return memcmp(IDP_Array(prop1), IDP_Array(prop2), idp_size_table[(int)prop1->subtype]*prop1->len);
//			else
//				return 0;
//		}
//		else if(prop1->type == IDP_GROUP) {
//			IDProperty *link1, *link2;
//
//			if(BLI_countlist(&prop1->data.group) != BLI_countlist(&prop2->data.group))
//				return 0;
//
//			for(link1=prop1->data.group.first; link1; link1=link1->next) {
//				link2= IDP_GetPropertyFromGroup(prop2, link1->name);
//
//				if(!IDP_EqualsProperties(link1, link2))
//					return 0;
//			}
//
//			return 1;
//		}
//		else if(prop1->type == IDP_IDPARRAY) {
//			IDProperty *array1= IDP_IDPArray(prop1);
//			IDProperty *array2= IDP_IDPArray(prop2);
//			int i;
//
//			if(prop1->len != prop2->len)
//				return 0;
//			
//			for(i=0; i<prop1->len; i++)
//				if(!IDP_EqualsProperties(&array1[i], &array2[i]))
//					return 0;
//		}
//		
//		return 1;
//	}

	public static IDProperty IDP_New(int type, IDPropertyTemplate val, String name)
	{
		IDProperty prop=null;

		switch (type) {
			case DNA_ID.IDP_INT:
				prop = new IDProperty();
				prop.data.val = val.i();
				break;
			case DNA_ID.IDP_FLOAT:
				prop = new IDProperty();
//				*(float*)&prop.data.val = val.f;
				prop.data.val = Float.floatToIntBits(val.f());
				break;
			case DNA_ID.IDP_DOUBLE:
				prop = new IDProperty();
//				*(double*)&prop.data.val = val.d();
				prop.data.val = (int)Double.doubleToLongBits(val.d());
				prop.data.val2 = (int)(Double.doubleToLongBits(val.d())>>32);
				break;		
			case DNA_ID.IDP_ARRAY:
			{
				/*for now, we only support float and int and double arrays*/
				if (val.array_type() == DNA_ID.IDP_FLOAT || val.array_type() == DNA_ID.IDP_INT || val.array_type() == DNA_ID.IDP_DOUBLE || val.array_type() == DNA_ID.IDP_GROUP) {
					prop = new IDProperty();
					prop.subtype = (byte)val.array_type();
					if (val.array_len()!=0)
//						prop.data.pointer = MEM_callocN(idp_size_table[val.array.type]*val.array.len, "id property array");
						prop.data.pointer = new Object[val.array_len()];
					prop.len = prop.totallen = val.array_len();
					break;
				} else {
					return null;
				}
			}
			case DNA_ID.IDP_STRING:
			{
				byte[] st = val.str();

				prop = new IDProperty();
				if (st == null) {
					prop.data.pointer = new byte[DNA_ID.DEFAULT_ALLOC_FOR_NULL_STRINGS];
					prop.totallen = DNA_ID.DEFAULT_ALLOC_FOR_NULL_STRINGS;
					prop.len = 1; /*NULL string, has len of 1 to account for null byte.*/
				} else {
					int stlen = StringUtil.strlen(st,0) + 1;
					prop.data.pointer = new byte[stlen];
					prop.len = prop.totallen = stlen;
					StringUtil.strcpy((byte[])prop.data.pointer,0, st,0);
				}
				break;
			}
			case DNA_ID.IDP_GROUP:
			{
				prop = new IDProperty();
				/* heh I think all needed values are set properly by calloc anyway :) */
				break;
			}
			default:
			{
				prop = new IDProperty();
				break;
			}
		}

		prop.type = (byte)type;
		StringUtil.BLI_strncpy(prop.name,0, StringUtil.toCString(name),0, DNA_ID.MAX_IDPROP_NAME);
		
		return prop;
	}

//	/*NOTE: this will free all child properties including list arrays and groups!
//	  Also, note that this does NOT unlink anything!  Plus it doesn't free
//	  the actual IDProperty struct either.*/
//	void IDP_FreeProperty(IDProperty *prop)
//	{
//		switch (prop->type) {
//			case IDP_ARRAY:
//				IDP_FreeArray(prop);
//				break;
//			case IDP_STRING:
//				IDP_FreeString(prop);
//				break;
//			case IDP_GROUP:
//				IDP_FreeGroup(prop);
//				break;
//			case IDP_IDPARRAY:
//				IDP_FreeIDPArray(prop);
//				break;
//		}
//	}
//
//	/*Unlinks any IDProperty<->ID linkage that might be going on.
//	  note: currently unused.*/
//	void IDP_UnlinkProperty(IDProperty *prop)
//	{
//		switch (prop->type) {
//			case IDP_ID:
//				IDP_UnlinkID(prop);
//		}
//	}
	
}
