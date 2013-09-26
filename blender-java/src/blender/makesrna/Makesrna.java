package blender.makesrna;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import blender.blenkernel.bContext;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.ReportList;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RNATypes.StructCallbackFunc;
import blender.makesrna.RNATypes.StructFreeFunc;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.StructUnregisterFunc;
import blender.makesrna.RNATypes.StructValidateFunc;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EditableFunc;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FloatPropertyRNA;
import blender.makesrna.rna_internal_types.Func;
import blender.makesrna.rna_internal_types.FunctionDefRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.IDPropertiesFunc;
import blender.makesrna.rna_internal_types.IntPropertyRNA;
import blender.makesrna.rna_internal_types.ItemEditableFunc;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropBooleanArrayGetFunc;
import blender.makesrna.rna_internal_types.PropBooleanArraySetFunc;
import blender.makesrna.rna_internal_types.PropBooleanGetFunc;
import blender.makesrna.rna_internal_types.PropBooleanSetFunc;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionLengthFunc;
import blender.makesrna.rna_internal_types.PropCollectionLookupIntFunc;
import blender.makesrna.rna_internal_types.PropCollectionLookupStringFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropEnumGetFunc;
import blender.makesrna.rna_internal_types.PropEnumItemFunc;
import blender.makesrna.rna_internal_types.PropEnumSetFunc;
import blender.makesrna.rna_internal_types.PropFloatArrayGetFunc;
import blender.makesrna.rna_internal_types.PropFloatArraySetFunc;
import blender.makesrna.rna_internal_types.PropFloatGetFunc;
import blender.makesrna.rna_internal_types.PropFloatRangeFunc;
import blender.makesrna.rna_internal_types.PropFloatSetFunc;
import blender.makesrna.rna_internal_types.PropIntArrayGetFunc;
import blender.makesrna.rna_internal_types.PropIntArraySetFunc;
import blender.makesrna.rna_internal_types.PropIntGetFunc;
import blender.makesrna.rna_internal_types.PropIntRangeFunc;
import blender.makesrna.rna_internal_types.PropIntSetFunc;
import blender.makesrna.rna_internal_types.PropPointerGetFunc;
import blender.makesrna.rna_internal_types.PropPointerPollFunc;
import blender.makesrna.rna_internal_types.PropPointerSetFunc;
import blender.makesrna.rna_internal_types.PropPointerTypeFunc;
import blender.makesrna.rna_internal_types.PropStringGetFunc;
import blender.makesrna.rna_internal_types.PropStringLengthFunc;
import blender.makesrna.rna_internal_types.PropStringSetFunc;
import blender.makesrna.rna_internal_types.PropertyDefRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StringPropertyRNA;
import blender.makesrna.rna_internal_types.StructDefRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.rna_internal_types.StructRefineFunc;
import blender.makesrna.rna_internal_types.UpdateFunc;

public class Makesrna {
	
//	/**
//	 * $Id$
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
//	 * Contributor(s): Blender Foundation (2008).
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <float.h>
//	#include <limits.h>
//	#include <stdio.h>
//	#include <stdlib.h>
//	#include <string.h>
//
//	#include "MEM_guardedalloc.h"
//
//	#include "RNA_access.h"
//	#include "RNA_define.h"
//	#include "RNA_types.h"
//
//	#include "rna_internal.h"

	public static final String RNA_VERSION_DATE = "$Id$";

//	#ifdef _WIN32
//	#ifndef snprintf
//	#define snprintf _snprintf
//	#endif
//	#endif
	
	public static class FString implements PropCollectionBeginFunc, PropCollectionNextFunc, PropCollectionEndFunc,
	PropCollectionGetFunc, PropCollectionLengthFunc, PropCollectionLookupIntFunc, PropCollectionLookupStringFunc,
	PropStringGetFunc, PropStringLengthFunc, PropStringSetFunc, PropBooleanGetFunc, PropBooleanSetFunc, PropBooleanArrayGetFunc, PropBooleanArraySetFunc,
	PropIntGetFunc, PropIntSetFunc, PropIntArrayGetFunc, PropIntArraySetFunc, PropIntRangeFunc,
	PropFloatGetFunc, PropFloatSetFunc, PropFloatArrayGetFunc, PropFloatArraySetFunc, PropFloatRangeFunc,
	PropEnumGetFunc, PropEnumSetFunc, PropEnumItemFunc,
	PropPointerGetFunc, PropPointerTypeFunc, PropPointerSetFunc, PropPointerPollFunc,
	UpdateFunc, StructRefineFunc, StructRegisterFunc, StructUnregisterFunc,
	EditableFunc, ItemEditableFunc, IDPropertiesFunc {
		private String name;
		public FString(String name) { this.name = name; }
		public static FString toFunc(String name) { return new FString(name); }
		public static String toString(Func func) { return (func != null) ? func.toString() : null; }
		public void begin(CollectionPropertyIterator iter, PointerRNA ptr) { }
		public void next(CollectionPropertyIterator iter) { }
		public void end(CollectionPropertyIterator iter) { }
		public PointerRNA get(CollectionPropertyIterator iter) { return null; }
		public int length(PointerRNA ptr) { return 0; }
		public PointerRNA lookupInt(PointerRNA ptr, int key) { return null; }
		public PointerRNA lookupStr(PointerRNA ptr, String key) { return null; }
		public void getStr(PointerRNA ptr, byte[] value, int offset) { }
		public int lenStr(PointerRNA ptr) { return 0; }
		public void setStr(PointerRNA ptr, byte[] value, int offset) { }
		public boolean getBool(PointerRNA ptr) { return false; }
		public void setBool(PointerRNA ptr, boolean value) { }
		public void getBoolArray(PointerRNA ptr, boolean[] values) { }
		public void setBoolArray(PointerRNA ptr, boolean[] values) { }
		public int getInt(PointerRNA ptr) { return 0; }
		public void setInt(PointerRNA ptr, int value) { }
		public void getIntArray(PointerRNA ptr, int[] values) { }
		public void setIntArray(PointerRNA ptr, int[] values) { }
		public void rangeInt(PointerRNA ptr, int[] min, int[] max) { }
		public float getFloat(PointerRNA ptr) { return 0; }
		public void setFloat(PointerRNA ptr, float value) { }
		public void getFloatArray(PointerRNA ptr, float[] values) { }
		public void setFloatArray(PointerRNA ptr, float[] values) { }
		public void rangeFloat(PointerRNA ptr, float[] min, float[] max) { }
		public int getEnum(PointerRNA ptr) { return 0; }
		public void setEnum(PointerRNA ptr, int value) { }
		public EnumPropertyItem[] itemEnum(bContext C, PointerRNA ptr, int[] free) { return null; }
		public PointerRNA getPtr(PointerRNA ptr) { return null; }
		public StructRNA typePtr(PointerRNA ptr) { return null; }
		public void setPtr(PointerRNA ptr, PointerRNA value) { }
		public int pollPtr(PointerRNA ptr, PointerRNA value) { return 0; }
		public void update(bContext C, PointerRNA ptr) { }
		public StructRNA refine(PointerRNA ptr) { return null; }
		public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free) { return null; }
		public void run(bContext C, StructRNA type) { }
		public int edit(PointerRNA ptr) { return 0; }
		public int editItem(PointerRNA ptr, int index) { return 0; }
		public IDProperty prop(PointerRNA ptr, boolean create) { return null; }
		
		public String toString() {
			return name;
		}

	}

//	/* Replace if different */
//	#define TMP_EXT ".tmp"
//
//	static int replace_if_different(char *tmpfile)
//	{
//		// return 0; // use for testing had edited rna
//
//	#define REN_IF_DIFF \
//		remove(orgfile); \
//		if(rename(tmpfile, orgfile) != 0) { \
//			fprintf(stderr, "%s:%d, rename error: \"%s\" -> \"%s\"\n", __FILE__, __LINE__, tmpfile, orgfile); \
//			return -1; \
//		} \
//		remove(tmpfile); \
//		return 1; \
//	/* end REN_IF_DIFF */
//
//
//		FILE *fp_new, *fp_org;
//		int len_new, len_org;
//		char *arr_new, *arr_org;
//		int cmp;
//
//		char orgfile[4096];
//
//		strcpy(orgfile, tmpfile);
//		orgfile[strlen(orgfile) - strlen(TMP_EXT)] = '\0'; /* strip '.tmp' */
//
//		fp_org= fopen(orgfile, "rb");
//
//		if(fp_org==NULL) {
//			REN_IF_DIFF;
//		}
//
//		fp_new= fopen(tmpfile, "rb");
//
//		if(fp_new==NULL) {
//			/* shouldn't happen, just to be safe */
//			fprintf(stderr, "%s:%d, open error: \"%s\"\n", __FILE__, __LINE__, tmpfile);
//			fclose(fp_org);
//			return -1;
//		}
//
//		fseek(fp_new, 0L, SEEK_END); len_new = ftell(fp_new); fseek(fp_new, 0L, SEEK_SET);
//		fseek(fp_org, 0L, SEEK_END); len_org = ftell(fp_org); fseek(fp_org, 0L, SEEK_SET);
//
//
//		if(len_new != len_org) {
//			fclose(fp_new);
//			fclose(fp_org);
//			REN_IF_DIFF;
//		}
//
//		/* now compare the files... */
//		arr_new= MEM_mallocN(sizeof(char)*len_new, "rna_cmp_file_new");
//		arr_org= MEM_mallocN(sizeof(char)*len_org, "rna_cmp_file_org");
//
//		if(fread(arr_new, sizeof(char), len_new, fp_new) != len_new)
//			fprintf(stderr, "%s:%d, error reading file %s for comparison.\n", __FILE__, __LINE__, tmpfile);
//		if(fread(arr_org, sizeof(char), len_org, fp_org) != len_org)
//			fprintf(stderr, "%s:%d, error reading file %s for comparison.\n", __FILE__, __LINE__, orgfile);
//
//		fclose(fp_new);
//		fclose(fp_org);
//
//		cmp= memcmp(arr_new, arr_org, len_new);
//
//		MEM_freeN(arr_new);
//		MEM_freeN(arr_org);
//
//		if(cmp) {
//			REN_IF_DIFF;
//		}
//		else {
//			remove(tmpfile);
//			return 0;
//		}
//
//	#undef REN_IF_DIFF
//	}
//
//
//
//	/* Sorting */
//
//	static int cmp_struct(const void *a, const void *b)
//	{
//		const StructRNA *structa= *(const StructRNA**)a;
//		const StructRNA *structb= *(const StructRNA**)b;
//
//		return strcmp(structa->identifier, structb->identifier);
//	}
//
//	static int cmp_property(const void *a, const void *b)
//	{
//		const PropertyRNA *propa= *(const PropertyRNA**)a;
//		const PropertyRNA *propb= *(const PropertyRNA**)b;
//
//		if(strcmp(propa->identifier, "rna_type") == 0) return -1;
//		else if(strcmp(propb->identifier, "rna_type") == 0) return 1;
//
//		if(strcmp(propa->identifier, "name") == 0) return -1;
//		else if(strcmp(propb->identifier, "name") == 0) return 1;
//
//		return strcmp(propa->name, propb->name);
//	}
//
//	static int cmp_def_struct(const void *a, const void *b)
//	{
//		const StructDefRNA *dsa= *(const StructDefRNA**)a;
//		const StructDefRNA *dsb= *(const StructDefRNA**)b;
//
//		return cmp_struct(&dsa->srna, &dsb->srna);
//	}
//
//	static int cmp_def_property(const void *a, const void *b)
//	{
//		const PropertyDefRNA *dpa= *(const PropertyDefRNA**)a;
//		const PropertyDefRNA *dpb= *(const PropertyDefRNA**)b;
//
//		return cmp_property(&dpa->prop, &dpb->prop);
//	}
//
//	static void rna_sortlist(ListBase *listbase, int(*cmp)(const void*, const void*))
//	{
//		Link *link;
//		void **array;
//		int a, size;
//		
//		if(listbase->first == listbase->last)
//			return;
//
//		for(size=0, link=listbase->first; link; link=link->next)
//			size++;
//
//		array= MEM_mallocN(sizeof(void*)*size, "rna_sortlist");
//		for(a=0, link=listbase->first; link; link=link->next, a++)
//			array[a]= link;
//
//		qsort(array, size, sizeof(void*), cmp);
//
//		listbase->first= listbase->last= NULL;
//		for(a=0; a<size; a++) {
//			link= array[a];
//			link->next= link->prev= NULL;
//			rna_addtail(listbase, link);
//		}
//
//		MEM_freeN(array);
//	}
//
//	/* Preprocessing */
	private static final String[] escape = {"\''", "\"\"", "\\\\", "\bb", "\ff", "\nn", "\rr", "\tt", null};
	static void rna_print_c_string(PrintStream f, String str)
	{
//		static char *escape[] = {"\''", "\"\"", "\??", "\\\\","\aa", "\bb", "\ff", "\nn", "\rr", "\tt", "\vv", NULL};
		int i, j;

		if(str==null) {
			fprintf(f, "null");
			return;
		}

		fprintf(f, "\"");
		for(i=0; i<str.length(); i++) {
			for(j=0; escape[j]!=null; j++)
				if(str.charAt(i) == escape[j].charAt(0))
					break;

			if(escape[j]!=null) fprintf(f, "\\%c", escape[j].charAt(1));
			else fprintf(f, "%c", str.charAt(i));
		}
		fprintf(f, "\"");
	}

	static void rna_print_data_get(PrintStream f, PropertyDefRNA dp)
	{
		if(dp.dnastructfromname!=null && dp.dnastructfromprop!=null)
			fprintf(f, "	%s data= (%s)(((%s)ptr.data).%s);\n", dp.dnastructname, dp.dnastructname, dp.dnastructfromname, dp.dnastructfromprop);
		else
			fprintf(f, "	%s data= (%s)(ptr.data);\n", dp.dnastructname, dp.dnastructname);
	}

	static void rna_print_id_get(PrintStream f, PropertyDefRNA dp)
	{
		fprintf(f, "	ID *id= ptr->id.data;\n");
	}

	static String rna_alloc_function_name(String structname, String propname, String type)
	{
//		AllocDefRNA *alloc;
//		char buffer[2048];
		String result;

//		snprintf(buffer, sizeof(buffer), "%s_%s_%s", structname, propname, type);
//		result= MEM_callocN(sizeof(char)*strlen(buffer)+1, "rna_alloc_function_name");
//		strcpy(result, buffer);
		result = String.format("%s_%s_%s", structname, propname, type);

//		alloc= MEM_callocN(sizeof(AllocDefRNA), "AllocDefRNA");
//		alloc->mem= result;
//		rna_addtail(&DefRNA.allocs, alloc);

		return result;
	}

	static StructRNA rna_find_struct(String identifier)
	{
		StructDefRNA ds;

		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
//			if(strcmp(ds.srna.identifier, identifier)==0)
			if(ds.srna.identifier.equals(identifier))
				return ds.srna;

		return null;
	}

//	static const char *rna_find_type(const char *type)
//	{
//		StructDefRNA *ds;
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next)
//			if(ds->dnaname && strcmp(ds->dnaname, type)==0)
//				return ds->srna->identifier;
//
//		return NULL;
//	}

	static String rna_find_dna_type(String type)
	{
		StructDefRNA ds;

		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
//			if(strcmp(ds.srna.identifier, type)==0)
			if(ds.srna.identifier.equals(type))
				return ds.dnaname;

		return null;
	}

	static String rna_type_type_name(PropertyRNA prop)
	{
		switch(prop.type) {
			case RNATypes.PROP_BOOLEAN:
				return "boolean";
			case RNATypes.PROP_INT:
			case RNATypes.PROP_ENUM:
				return "int";
			case RNATypes.PROP_FLOAT:
				return "float";
			case RNATypes.PROP_STRING:
				if((prop.flag & RNATypes.PROP_THICK_WRAP)!=0) {
					return "byte[]";
				}
				else {
					return "byte[]";
				}
			default:
				return null;
		}
	}
	
	static String rna_type_type_name_java(PropertyRNA prop)
	{
		switch(prop.type) {
			case RNATypes.PROP_BOOLEAN:
			case RNATypes.PROP_INT:
			case RNATypes.PROP_ENUM:
				return "Integer";
			case RNATypes.PROP_FLOAT:
				return "Float";
			case RNATypes.PROP_STRING:
				if((prop.flag & RNATypes.PROP_THICK_WRAP)!=0) {
//					return "char*";
					return "String";
				}
				else {
//					return "const char*";
					return "String";
				}
			default:
				return null;
		}
	}

	static String rna_type_type(PropertyRNA prop)
	{
		String type;

		type= rna_type_type_name(prop);

		if(type!=null)
			return type;

		return "PointerRNA";
	}

	static String rna_type_struct(PropertyRNA prop)
	{
//		String type;
//
//		type= rna_type_type_name(prop);

//		if(type!=null)
			return "";

//		return "struct ";
	}

	static String rna_parameter_type_name(PropertyRNA parm)
	{
		String type;

		type= rna_type_type_name(parm);

		if(type!=null)
			return type;

		switch(parm.type) {
			case RNATypes.PROP_POINTER:  {
				PointerPropertyRNA pparm= (PointerPropertyRNA)parm;

				if((parm.flag & RNATypes.PROP_RNAPTR)!=0)
					return "PointerRNA";
				else
					return rna_find_dna_type(makeString(pparm.type));
			}
			case RNATypes.PROP_COLLECTION: {
				return "ListBase";
			}
			default:
				return "<error, no type specified>";
		}
	}

	static int rna_enum_bitmask(PropertyRNA prop)
	{
		EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
		int a, mask= 0;

		if(eprop.item!=null) {
			for(a=0; a<eprop.totitem; a++)
//				if(eprop.item[a].identifier[0])
				if(eprop.item[a].identifier!=null && !eprop.item[a].identifier.equals(""))
					mask |= eprop.item[a].value;
		}
		
		return mask;
	}

	static int rna_color_quantize(PropertyRNA prop, PropertyDefRNA dp)
	{
		if((prop.type) == RNATypes.PROP_FLOAT && ((prop.subtype)==RNATypes.PROP_COLOR || (prop.subtype)==RNATypes.PROP_COLOR_GAMMA))
//			if(strcmp(dp.dnatype, "float") != 0 && strcmp(dp.dnatype, "double") != 0)
			if(!dp.dnatype.equals("float") && !dp.dnatype.equals("double"))
				return 1;
		
		return 0;
	}

	static String rna_function_string(Object func)
	{
		return (func!=null)? func.toString(): "null";
	}

	static void rna_float_print(PrintStream f, float num)
	{
		if(num == -RnaDefine.FLT_MAX) fprintf(f, "-FLT_MAX");
		else if(num == RnaDefine.FLT_MAX) fprintf(f, "FLT_MAX");
		else if((int)num == num) fprintf(f, "%.1ff", num);
		else fprintf(f, "%.10ff", num);
	}

	static void rna_int_print(PrintStream f, int num)
	{
		if(num == RnaDefine.INT_MIN) fprintf(f, "INT_MIN");
		else if(num == RnaDefine.INT_MAX) fprintf(f, "INT_MAX");
		else fprintf(f, "%d", num);
	}

	static String rna_def_property_get_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func;

		if((prop.flag & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if(manualfunc==null) {
			if(dp.dnastructname==null || dp.dnaname==null) {
				System.err.printf("rna_def_property_get_func: %s.%s has no valid dna info.\n", srna.identifier, prop.identifier);
				RnaDefine.DefRNA.error= 1;
				return null;
			}
		}

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "get");

		switch(prop.type) {
			case RNATypes.PROP_STRING: {
				StringPropertyRNA sprop= (StringPropertyRNA)prop;
				String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
				fprintf(f, "public String get%s() { %s.getStr(ptr, DEFAULT_BUF,0); return StringUtil.toJString(DEFAULT_BUF,0);}\n", pyName, func);
				fprintf(f, "private static PropStringGetFunc %s = new PropStringGetFunc() {\n", func);
				fprintf(f, "public void getStr(PointerRNA ptr, byte[] value, int offset)\n", func);
				fprintf(f, "{\n");
				if(manualfunc!=null) {
					fprintf(f, "	%s.getStr(ptr, value, offset);\n", manualfunc);
				}
				else {
					rna_print_data_get(f, dp);
					if(sprop.maxlength!=0)
						fprintf(f, "	StringUtil.BLI_strncpy(value,offset, data.%s,0, %d);\n", dp.dnaname, sprop.maxlength);
					else
						fprintf(f, "	StringUtil.BLI_strncpy(value,offset, data.%s,0, data.%s.length);\n", dp.dnaname, dp.dnaname);
				}
				fprintf(f, "}};\n\n");
				break;
			}
			case RNATypes.PROP_POINTER: {
				fprintf(f, "private static PropPointerGetFunc %s = new PropPointerGetFunc() {\n", func);
				fprintf(f, "public PointerRNA getPtr(PointerRNA ptr)\n", func);
				fprintf(f, "{\n");
				if(manualfunc!=null) {
					fprintf(f, "	return %s.getPtr(ptr);\n", manualfunc);
				}
				else {
					PointerPropertyRNA pprop= (PointerPropertyRNA)prop;
					rna_print_data_get(f, dp);
					if(dp.dnapointerlevel == 0)
						fprintf(f, "	return rna_pointer_inherit_refine(ptr, RNA_%s, data.%s);\n", makeString(pprop.type), dp.dnaname);
					else
						fprintf(f, "	return rna_pointer_inherit_refine(ptr, RNA_%s, data.%s);\n", makeString(pprop.type), dp.dnaname);
				}
				fprintf(f, "}};\n\n");
				break;
			}
			case RNATypes.PROP_COLLECTION: {
				CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;

				fprintf(f, "private static PropCollectionGetFunc %s = new PropCollectionGetFunc() {\n", func);
				fprintf(f, "public PointerRNA get(CollectionPropertyIterator iter)\n");
				fprintf(f, "{\n");
				if(manualfunc!=null) {
					if(manualfunc.equals("rna_iterator_listbase_get") ||
						manualfunc.equals("rna_iterator_array_get") ||
						manualfunc.equals("rna_iterator_array_dereference_get"))
						fprintf(f, "	return rna_pointer_inherit_refine(iter.parent, RNA_%s, %s.get(iter));\n", (cprop.item_type!=null)? cprop.item_type.toString(): "UnknownType", manualfunc);
					else
						fprintf(f, "	return %s.get(iter);\n", manualfunc);
				}
				else {
					fprintf(f, "	return null;\n");
				}
				fprintf(f, "}};\n\n");
				break;
			}
			default:
				if(prop.arraydimension!=0) {
					if((prop.flag & RNATypes.PROP_DYNAMIC)!=0)
						fprintf(f, "void %s(PointerRNA *ptr, %s values[])\n", func, rna_type_type(prop));
					else
						fprintf(f, "void %s(PointerRNA *ptr, %s values[%d])\n", func, rna_type_type(prop), prop.totarraylength);
					fprintf(f, "{\n");

					if(manualfunc!=null) {
						fprintf(f, "	%s(ptr, values);\n", manualfunc);
					}
					else {
						rna_print_data_get(f, dp);

						if((prop.flag & RNATypes.PROP_DYNAMIC)!=0) {
							String lenfunc= rna_alloc_function_name(srna.identifier, prop.identifier, "get_length");
							fprintf(f, "	int i, arraylen[RNA_MAX_ARRAY_DIMENSION];\n");
							fprintf(f, "	int len= %s(ptr, arraylen);\n\n", lenfunc);
							fprintf(f, "	for(i=0; i<len; i++) {\n");
//							MEM_freeN(lenfunc);
						}
						else {
							fprintf(f, "	int i;\n\n");
							fprintf(f, "	for(i=0; i<%d; i++) {\n", prop.totarraylength);
						}

						if(dp.dnaarraylength == 1) {
							if((prop.type&0xFF) == RNATypes.PROP_BOOLEAN && dp.booleanbit!=0)
								fprintf(f, "		values[i]= %s((data->%s & (%d<<i)) != 0);\n", (dp.booleannegative!=0)? "!": "", dp.dnaname, dp.booleanbit);
							else
								fprintf(f, "		values[i]= (%s)%s((&data->%s)[i]);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						else {
							if((prop.type&0xFF) == RNATypes.PROP_BOOLEAN && dp.booleanbit!=0) {
								fprintf(f, "		values[i]= %s((data->%s[i] & ", (dp.booleannegative!=0)? "!": "", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ") != 0);\n");
							}
							else if(rna_color_quantize(prop, dp)!=0)
								fprintf(f, "		values[i]= (%s)(data->%s[i]*(1.0f/255.0f));\n", rna_type_type(prop), dp.dnaname);
							else if(dp.dnatype!=null)
								fprintf(f, "		values[i]= (%s)%s(((%s*)data->%s)[i]);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnatype, dp.dnaname);
							else
								fprintf(f, "		values[i]= (%s)%s((data->%s)[i]);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						fprintf(f, "	}\n");
					}
					fprintf(f, "}\n\n");
				}
				else {
					switch(prop.type) {
					case RNATypes.PROP_INT: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public int get%s() { return %s.getInt(ptr); }\n", pyName, func);
						fprintf(f, "private static PropIntGetFunc %s = new PropIntGetFunc() {\n", func);
						fprintf(f, "public %s getInt(PointerRNA ptr)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	return %s.getInt(ptr);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							fprintf(f, "	return (%s)%s(data.%s);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						break;
					}
					case RNATypes.PROP_ENUM: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public int get%s() { return %s.getEnum(ptr); }\n", pyName, func);
						fprintf(f, "private static PropEnumGetFunc %s = new PropEnumGetFunc() {\n", func);
						fprintf(f, "public %s getEnum(PointerRNA ptr)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	return %s.getEnum(ptr);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							if(dp.enumbitflags!=0) {
								fprintf(f, "	return ((data.%s) & ", dp.dnaname);
								rna_int_print(f, rna_enum_bitmask(prop));
								fprintf(f, ");\n");
							}
							else
								fprintf(f, "	return (%s)%s(data.%s);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						break;
					}
					case RNATypes.PROP_FLOAT: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public float get%s() { return %s.getFloat(ptr); }\n", pyName, func);
						fprintf(f, "private static PropFloatGetFunc %s = new PropFloatGetFunc() {\n", func);
						fprintf(f, "public %s getFloat(PointerRNA ptr)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	return %s.getFloat(ptr);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							fprintf(f, "	return (%s)%s(data.%s);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						break;
					}
					case RNATypes.PROP_BOOLEAN: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public boolean get%s() { return %s.getBool(ptr); }\n", pyName, func);
						fprintf(f, "private static PropBooleanGetFunc %s = new PropBooleanGetFunc() {\n", func);
						fprintf(f, "public %s getBool(PointerRNA ptr)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	return %s.getBool(ptr);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							if(dp.booleanbit!=0) {
								fprintf(f, "	return %s(((data.%s) & ", (dp.booleannegative!=0)? "!": "", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ") != 0);\n");
							}
							else
								fprintf(f, "	return (%s)%s(data.%s);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
						break;
					}
					default: {
						fprintf(f, "%s %s(PointerRNA ptr)\n", rna_type_type(prop), func);
						fprintf(f, "{{\n");

						if(manualfunc!=null) {
							fprintf(f, "	return %s(ptr);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							fprintf(f, "	return (%s)%s(data.%s);\n", rna_type_type(prop), (dp.booleannegative!=0)? "!": "", dp.dnaname);
						}
					}
					}
					fprintf(f, "}};\n\n");
				}
				break;
		}

		return func;
	}

	static void rna_clamp_value(PrintStream f, PropertyRNA prop, int array)
	{
		if((prop.type) == RNATypes.PROP_INT) {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;

			if(iprop.hardmin != RnaDefine.INT_MIN || iprop.hardmax != RnaDefine.INT_MAX) {
				if(array!=0) fprintf(f, "UtilDefines.CLAMPIS(values[i], ");
				else fprintf(f, "UtilDefines.CLAMPIS(value, ");
				rna_int_print(f, iprop.hardmin); fprintf(f, ", ");
				rna_int_print(f, iprop.hardmax); fprintf(f, ");\n");
				return;
			}
		}
		else if((prop.type) == RNATypes.PROP_FLOAT) {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;

			if(fprop.hardmin != -RnaDefine.FLT_MAX || fprop.hardmax != RnaDefine.FLT_MAX) {
				if(array!=0) fprintf(f, "UtilDefines.CLAMPIS(values[i], ");
				else fprintf(f, "UtilDefines.CLAMPIS(value, ");
				rna_float_print(f, fprop.hardmin); fprintf(f, ", ");
				rna_float_print(f, fprop.hardmax); fprintf(f, ");\n");
				return;
			}
		}

		if(array!=0)
			fprintf(f, "values[i];\n");
		else
			fprintf(f, "value;\n");
	}

	static String rna_def_property_set_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func;

		if(((prop.flag) & RNATypes.PROP_EDITABLE)==0)
			return null;
		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if(manualfunc==null) {
			if(dp.dnastructname==null || dp.dnaname==null) {
				if(((prop.flag&0xFFFF) & RNATypes.PROP_EDITABLE)!=0) {
					System.err.printf("rna_def_property_set_func: %s.%s has no valid dna info.\n", srna.identifier, prop.identifier);
					RnaDefine.DefRNA.error= 1;
				}
				return null;
			}
		}

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "set");

		switch(prop.type) {
			case RNATypes.PROP_STRING: {
				StringPropertyRNA sprop= (StringPropertyRNA)prop;
				String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
				fprintf(f, "public void set%s(String value) { %s.setStr(ptr, StringUtil.toCString(value),0); }\n", pyName, func);
				fprintf(f, "private static PropStringSetFunc %s = new PropStringSetFunc() {\n", func);
				fprintf(f, "public void setStr(PointerRNA ptr, byte[] value, int offset)\n", func);
				fprintf(f, "{\n");
				if(manualfunc!=null) {
					fprintf(f, "	%s.setStr(ptr, value, offset);\n", manualfunc);
				}
				else {
					rna_print_data_get(f, dp);
					if(sprop.maxlength!=0)
						fprintf(f, "	StringUtil.BLI_strncpy(data.%s,0, value,offset, %d);\n", dp.dnaname, sprop.maxlength);
					else
						fprintf(f, "	StringUtil.BLI_strncpy(data.%s,0, value,offset, data.%s.length);\n", dp.dnaname, dp.dnaname);
				}
				fprintf(f, "}};\n\n");
				break;
			}
			case RNATypes.PROP_POINTER: {
				fprintf(f, "private static PropPointerSetFunc %s = new PropPointerSetFunc() {\n", func);
				fprintf(f, "public void setPtr(PointerRNA ptr, PointerRNA value)\n", func);
				fprintf(f, "{\n");
				if(manualfunc!=null) {
					fprintf(f, "	%s.setPtr(ptr, value);\n", manualfunc);
				}
				else {
					rna_print_data_get(f, dp);

					if(((prop.flag&0xFFFF) & RNATypes.PROP_ID_SELF_CHECK)!=0) {
						rna_print_id_get(f, dp);
						fprintf(f, "	if(id==value.data) return;\n\n");
					}

					if(((prop.flag&0xFFFF) & RNATypes.PROP_ID_REFCOUNT)!=0) {
						fprintf(f, "\n	if(data->%s)\n", dp.dnaname);
						fprintf(f, "		id_us_min((ID*)data->%s);\n", dp.dnaname);
						fprintf(f, "	if(value.data)\n");
						fprintf(f, "		id_us_plus((ID*)value.data);\n\n");
					}
					else {
						PointerPropertyRNA pprop= (PointerPropertyRNA)dp.prop;
						StructRNA type= rna_find_struct(makeString(pprop.type));
						if(type!=null && (type.flag & RNATypes.STRUCT_ID)!=0) {
							fprintf(f, "	if(value.data)\n");
							fprintf(f, "		id_lib_extern((ID*)value.data);\n\n");
						}
					}

					fprintf(f, "	data.%s= value.data;\n", dp.dnaname);

				}
				fprintf(f, "}};\n\n");
				break;
			}
			default:
				if(prop.arraydimension!=0) {
					if(((prop.flag&0xFFFF) & RNATypes.PROP_DYNAMIC)!=0)
						fprintf(f, "void %s(PointerRNA *ptr, const %s values[])\n", func, rna_type_type(prop));
					else
						fprintf(f, "void %s(PointerRNA *ptr, const %s values[%d])\n", func, rna_type_type(prop), prop.totarraylength);
					fprintf(f, "{\n");

					if(manualfunc!=null) {
						fprintf(f, "	%s(ptr, values);\n", manualfunc);
					}
					else {
						rna_print_data_get(f, dp);

						if(((prop.flag&0xFFFF) & RNATypes.PROP_DYNAMIC)!=0) {
							String lenfunc= rna_alloc_function_name(srna.identifier, prop.identifier, "set_length");
							fprintf(f, "	int i, arraylen[RNA_MAX_ARRAY_DIMENSION];\n");
							fprintf(f, "	int len= %s(ptr, arraylen);\n\n", lenfunc);
							fprintf(f, "	for(i=0; i<len; i++) {\n");
//							MEM_freeN(lenfunc);
						}
						else {
							fprintf(f, "	int i;\n\n");
							fprintf(f, "	for(i=0; i<%d; i++) {\n", prop.totarraylength);
						}

						if(dp.dnaarraylength == 1) {
							if((prop.type&0xFF) == RNATypes.PROP_BOOLEAN && dp.booleanbit!=0) {
								fprintf(f, "		if(%svalues[i]) data->%s |= (%d<<i);\n", (dp.booleannegative!=0)? "!": "", dp.dnaname, dp.booleanbit);
								fprintf(f, "		else data->%s &= ~(%d<<i);\n", dp.dnaname, dp.booleanbit);
							}
							else {
								fprintf(f, "		(&data->%s)[i]= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
								rna_clamp_value(f, prop, 1);
							}
						}
						else {
							if((prop.type&0xFF) == RNATypes.PROP_BOOLEAN && dp.booleanbit!=0) {
								fprintf(f, "		if(%svalues[i]) data->%s[i] |= ", (dp.booleannegative!=0)? "!": "", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ";\n");
								fprintf(f, "		else data->%s[i] &= ~", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ";\n");
							}
							else if(rna_color_quantize(prop, dp)!=0) {
								fprintf(f, "		data->%s[i]= FTOCHAR(values[i]);\n", dp.dnaname);
							}
							else {
								if(dp.dnatype!=null)
									fprintf(f, "		((%s*)data->%s)[i]= %s", dp.dnatype, dp.dnaname, (dp.booleannegative!=0)? "!": "");
								else
									fprintf(f, "		(data->%s)[i]= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
								rna_clamp_value(f, prop, 1);
							}
						}
						fprintf(f, "	}\n");
					}
					fprintf(f, "}\n\n");
				}
				else {
					switch(prop.type) {
					case RNATypes.PROP_INT: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public void set%s(int value) { %s.setInt(ptr, value); }\n", pyName, func);
						fprintf(f, "private static PropIntSetFunc %s = new PropIntSetFunc() {\n", func);
						fprintf(f, "public void setInt(PointerRNA ptr, %s value)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	%s.setInt(ptr, value);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
//							fprintf(f, "	data.%s= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
							fprintf(f, "	value= %s", (dp.booleannegative!=0)? "!": "");
							rna_clamp_value(f, prop, 0);
							fprintf(f, "	data.%s= castMe(value, data.%s);\n", dp.dnaname, dp.dnaname);
							//rna_clamp_value(f, prop, 0);
						}
						break;
					}
					case RNATypes.PROP_ENUM: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public void set%s(int value) { %s.setEnum(ptr, value); }\n", pyName, func);
						fprintf(f, "private static PropEnumSetFunc %s = new PropEnumSetFunc() {\n", func);
						fprintf(f, "public void setEnum(PointerRNA ptr, %s value)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	%s.setEnum(ptr, value);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							if(dp.enumbitflags!=0) {
								fprintf(f, "	data.%s &= ~", dp.dnaname);
								rna_int_print(f, rna_enum_bitmask(prop));
								fprintf(f, ";\n");
								fprintf(f, "	data.%s |= value;\n", dp.dnaname);
							}
							else {
//								fprintf(f, "	data.%s= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
								fprintf(f, "	value= %s", (dp.booleannegative!=0)? "!": "");
								rna_clamp_value(f, prop, 0);
								fprintf(f, "	data.%s= castMe(value, data.%s);\n", dp.dnaname, dp.dnaname);
							}
						}
						break;
					}
					case RNATypes.PROP_FLOAT: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public void set%s(float value) { %s.setFloat(ptr, value); }\n", pyName, func);
						fprintf(f, "private static PropFloatSetFunc %s = new PropFloatSetFunc() {\n", func);
						fprintf(f, "public void setFloat(PointerRNA ptr, %s value)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	%s.setFloat(ptr, value);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
//							fprintf(f, "	data.%s= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
							fprintf(f, "	value= %s", (dp.booleannegative!=0)? "!": "");
							rna_clamp_value(f, prop, 0);
							fprintf(f, "	data.%s= value;\n", dp.dnaname);
							//rna_clamp_value(f, prop, 0);
						}
						break;
					}
					case RNATypes.PROP_BOOLEAN: {
						String pyName = prop.identifier.substring(0,1).toUpperCase()+prop.identifier.substring(1);
						fprintf(f, "public void set%s(boolean value) { %s.setBool(ptr, value); }\n", pyName, func);
						fprintf(f, "private static PropBooleanSetFunc %s = new PropBooleanSetFunc() {\n", func);
						fprintf(f, "public void setBool(PointerRNA ptr, %s value)\n", rna_type_type(prop));
						fprintf(f, "{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	%s.setBool(ptr, value);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							if(dp.booleanbit!=0) {
								fprintf(f, "	if(%svalue) data.%s |= ", (dp.booleannegative!=0)? "!": "", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ";\n");
								fprintf(f, "	else data.%s &= ~", dp.dnaname);
								rna_int_print(f, dp.booleanbit);
								fprintf(f, ";\n");
							}
							else {
								fprintf(f, "	data.%s= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
								rna_clamp_value(f, prop, 0);
							}
						}
						break;
					}
					default: {
						fprintf(f, "void %s(PointerRNA ptr, %s value)\n", func, rna_type_type(prop));
						fprintf(f, "{{\n");
	
						if(manualfunc!=null) {
							fprintf(f, "	%s(ptr, value);\n", manualfunc);
						}
						else {
							rna_print_data_get(f, dp);
							fprintf(f, "	data.%s= %s", dp.dnaname, (dp.booleannegative!=0)? "!": "");
							rna_clamp_value(f, prop, 0);
						}
						break;
					}
					}
					fprintf(f, "}};\n\n");
				}
				break;
		}

		return func;
	}

	static String rna_def_property_length_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func= null;

		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if((prop.type) == RNATypes.PROP_STRING) {
			if(manualfunc==null) {
				if(dp.dnastructname==null || dp.dnaname==null) {
					System.err.printf("rna_def_property_length_func: %s.%s has no valid dna info.\n", srna.identifier, prop.identifier);
					RnaDefine.DefRNA.error= 1;
					return null;
				}
			}

			func= rna_alloc_function_name(srna.identifier, prop.identifier, "length");

			fprintf(f, "private static PropStringLengthFunc %s = new PropStringLengthFunc() {\n", func);
			fprintf(f, "public int lenStr(PointerRNA ptr)\n", func);
			fprintf(f, "{\n");
			if(manualfunc!=null) {
				fprintf(f, "	return %s.lenStr(ptr);\n", manualfunc);
			}
			else {
				rna_print_data_get(f, dp);
				fprintf(f, "	return StringUtil.strlen(data.%s,0);\n", dp.dnaname);
			}
			fprintf(f, "}};\n\n");
		}
		else if((prop.type&0xFF) == RNATypes.PROP_COLLECTION) {
			if(manualfunc==null) {
				if((prop.type&0xFF) == RNATypes.PROP_COLLECTION && (!(dp.dnalengthname!=null || dp.dnalengthfixed!=0)|| dp.dnaname==null)) {
					System.err.printf("rna_def_property_length_func: %s.%s has no valid dna info.\n", srna.identifier, prop.identifier);
					RnaDefine.DefRNA.error= 1;
					return null;
				}
			}

			func= rna_alloc_function_name(srna.identifier, prop.identifier, "length");

			fprintf(f, "int %s(PointerRNA *ptr)\n", func);
			fprintf(f, "{\n");
			if(manualfunc!=null) {
				fprintf(f, "	return %s(ptr);\n", manualfunc);
			}
			else {
				rna_print_data_get(f, dp);
				if(dp.dnalengthname!=null)
					fprintf(f, "	return (data->%s == NULL)? 0: data->%s;\n", dp.dnaname, dp.dnalengthname);
				else
					fprintf(f, "	return (data->%s == NULL)? 0: %d;\n", dp.dnaname, dp.dnalengthfixed);
			}
			fprintf(f, "}\n\n");
		}

		return func;
	}

	static String rna_def_property_begin_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func, getfunc;

		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if(manualfunc==null) {
			if(dp.dnastructname==null || dp.dnaname==null) {
				System.err.printf("rna_def_property_begin_func: %s.%s has no valid dna info.\n", srna.identifier, prop.identifier);
				RnaDefine.DefRNA.error= 1;
				return null;
			}
		}

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "begin");

		fprintf(f, "private static PropCollectionBeginFunc %s = new PropCollectionBeginFunc() {\n", func);
		fprintf(f, "public void begin(CollectionPropertyIterator iter, PointerRNA ptr)\n", func);
		fprintf(f, "{\n");

		if(manualfunc==null)
			rna_print_data_get(f, dp);

		fprintf(f, "\n	iter.clear();\n");
		fprintf(f, "	iter.parent= ptr;\n");
		fprintf(f, "	iter.prop= (PropertyRNA)rna_%s_%s;\n", srna.identifier, prop.identifier);

		if(dp.dnalengthname!=null || dp.dnalengthfixed!=0) {
			if(manualfunc!=null) {
				fprintf(f, "\n	%s.begin(iter, ptr);\n", manualfunc);
			}
			else {
				if(dp.dnalengthname!=null)
					fprintf(f, "\n	rna_iterator_array_begin(iter, data.%s, sizeof(data->%s[0]), data->%s, 0, NULL);\n", dp.dnaname, dp.dnaname, dp.dnalengthname);
				else
					fprintf(f, "\n	rna_iterator_array_begin(iter, data->%s, sizeof(data->%s[0]), %d, 0, NULL);\n", dp.dnaname, dp.dnaname, dp.dnalengthfixed);
			}
		}
		else {
			if(manualfunc!=null)
				fprintf(f, "\n	%s.begin(iter, ptr);\n", manualfunc);
			else if(dp.dnapointerlevel == 0)
				fprintf(f, "\n	rna_iterator_listbase_begin(iter, data.%s, null);\n", dp.dnaname);
			else
				fprintf(f, "\n	rna_iterator_listbase_begin(iter, data.%s, null);\n", dp.dnaname);
		}

		getfunc= rna_alloc_function_name(srna.identifier, prop.identifier, "get");

		fprintf(f, "\n	if(iter.valid)\n");
		fprintf(f, "		iter.ptr= %s.get(iter);\n", getfunc);

		fprintf(f, "}};\n\n");


		return func;
	}

	static String rna_def_property_lookup_int_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc, String nextfunc)
	{
		String func;

		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if(manualfunc==null) {
			if(dp.dnastructname==null || dp.dnaname==null)
				return null;

			/* only supported in case of standard next functions */
			if(nextfunc!=null && nextfunc.equals("rna_iterator_array_next"));
			else if(nextfunc!=null && nextfunc.equals("rna_iterator_listbase_next"));
			else return null;
		}

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "lookup_int");

		fprintf(f, "PointerRNA %s(PointerRNA *ptr, int index)\n", func);
		fprintf(f, "{\n");

		if(manualfunc!=null) {
			fprintf(f, "\n	return %s(ptr, index);\n", manualfunc);
			fprintf(f, "}\n\n");
			return func;
		}

		fprintf(f, "	PointerRNA r_ptr;\n");
		fprintf(f, "	CollectionPropertyIterator iter;\n\n");

		fprintf(f, "	%s_%s_begin(&iter, ptr);\n\n", srna.identifier, prop.identifier);
		fprintf(f, "	{\n");

		if(nextfunc.equals("rna_iterator_array_next")) {
			fprintf(f, "		ArrayIterator *internal= iter.internal;\n");
			fprintf(f, "		if(internal->skip) {\n");
			fprintf(f, "			while(index-- > 0) {\n");
			fprintf(f, "				do {\n");
			fprintf(f, "					internal->ptr += internal->itemsize;\n");
			fprintf(f, "				} while(internal->skip(&iter, internal->ptr));\n");
			fprintf(f, "			}\n");
			fprintf(f, "		}\n");
			fprintf(f, "		else {\n");
			fprintf(f, "			internal->ptr += internal->itemsize*index;\n");
			fprintf(f, "		}\n");
		}
		else if(nextfunc.equals("rna_iterator_listbase_next")) {
			fprintf(f, "		ListBaseIterator *internal= iter.internal;\n");
			fprintf(f, "		if(internal->skip) {\n");
			fprintf(f, "			while(index-- > 0) {\n");
			fprintf(f, "				do {\n");
			fprintf(f, "					internal->link= internal->link->next;\n");
			fprintf(f, "				} while(internal->skip(&iter, internal->link));\n");
			fprintf(f, "			}\n");
			fprintf(f, "		}\n");
			fprintf(f, "		else {\n");
			fprintf(f, "			while(index-- > 0)\n");
			fprintf(f, "				internal->link= internal->link->next;\n");
			fprintf(f, "		}\n");
		}

		fprintf(f, "	}\n\n");

		fprintf(f, "	r_ptr = %s_%s_get(&iter);\n", srna.identifier, prop.identifier);
		fprintf(f, "	%s_%s_end(&iter);\n\n", srna.identifier, prop.identifier);

		fprintf(f, "	return r_ptr;\n");

//	#if 0
//		rna_print_data_get(f, dp);
//		item_type= (cprop->item_type)? (char*)cprop->item_type: "UnknownType";
//
//		if(dp->dnalengthname || dp->dnalengthfixed) {
//			if(dp->dnalengthname)
//				fprintf(f, "\n	rna_array_lookup_int(ptr, &RNA_%s, data->%s, sizeof(data->%s[0]), data->%s, index);\n", item_type, dp->dnaname, dp->dnaname, dp->dnalengthname);
//			else
//				fprintf(f, "\n	rna_array_lookup_int(ptr, &RNA_%s, data->%s, sizeof(data->%s[0]), %d, index);\n", item_type, dp->dnaname, dp->dnaname, dp->dnalengthfixed);
//		}
//		else {
//			if(dp->dnapointerlevel == 0)
//				fprintf(f, "\n	return rna_listbase_lookup_int(ptr, &RNA_%s, &data->%s, index);\n", item_type, dp->dnaname);
//			else
//				fprintf(f, "\n	return rna_listbase_lookup_int(ptr, &RNA_%s, data->%s, index);\n", item_type, dp->dnaname);
//		}
//	#endif

		fprintf(f, "}\n\n");

		return func;
	}

	static String rna_def_property_next_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func, getfunc;

		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		if(manualfunc==null)
			return null;

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "next");

		fprintf(f, "private static PropCollectionNextFunc %s = new PropCollectionNextFunc() {\n", func);
		fprintf(f, "public void next(CollectionPropertyIterator iter)\n", func);
		fprintf(f, "{\n");
		fprintf(f, "	%s.next(iter);\n", manualfunc);

		getfunc= rna_alloc_function_name(srna.identifier, prop.identifier, "get");

		fprintf(f, "\n	if(iter.valid)\n");
		fprintf(f, "		iter.ptr= %s.get(iter);\n", getfunc);

		fprintf(f, "}};\n\n");

		return func;
	}

	static String rna_def_property_end_func(PrintStream f, StructRNA srna, PropertyRNA prop, PropertyDefRNA dp, String manualfunc)
	{
		String func;

		if(((prop.flag) & RNATypes.PROP_IDPROPERTY)!=0 && manualfunc==null)
			return null;

		func= rna_alloc_function_name(srna.identifier, prop.identifier, "end");

		fprintf(f, "private static PropCollectionEndFunc %s = new PropCollectionEndFunc() {\n", func);
		fprintf(f, "public void end(CollectionPropertyIterator iter)\n", func);
		fprintf(f, "{\n");
		if(manualfunc!=null)
			fprintf(f, "	%s.end(iter);\n", manualfunc);
		fprintf(f, "}};\n\n");

		return func;
	}

	static void rna_set_raw_property(PropertyDefRNA dp, PropertyRNA prop)
	{
		if(dp.dnapointerlevel != 0)
			return;
		if(dp.dnatype==null || dp.dnaname==null || dp.dnastructname==null)
			return;
		
		if(dp.dnatype.equals("char")) {
			prop.rawtype= RNATypes.PROP_RAW_CHAR;
			prop.flag |= RNATypes.PROP_RAW_ACCESS;
		}
		else if(dp.dnatype.equals("short")) {
			prop.rawtype= RNATypes.PROP_RAW_SHORT;
			prop.flag |= RNATypes.PROP_RAW_ACCESS;
		}
		else if(dp.dnatype.equals("int")) {
			prop.rawtype= RNATypes.PROP_RAW_INT;
			prop.flag |= RNATypes.PROP_RAW_ACCESS;
		}
		else if(dp.dnatype.equals("float")) {
			prop.rawtype= RNATypes.PROP_RAW_FLOAT;
			prop.flag |= RNATypes.PROP_RAW_ACCESS;
		}
		else if(dp.dnatype.equals("double")) {
			prop.rawtype= RNATypes.PROP_RAW_DOUBLE;
			prop.flag |= RNATypes.PROP_RAW_ACCESS;
		}
	}

	static void rna_set_raw_offset(PrintStream f, StructRNA srna, PropertyRNA prop)
	{
		PropertyDefRNA dp= RnaDefine.rna_find_struct_property_def(srna, prop);

		fprintf(f, "\toffsetof(%s, %s), %d", dp.dnastructname, dp.dnaname, prop.rawtype);
	}

	static void rna_def_property_funcs(PrintStream f, StructRNA srna, PropertyDefRNA dp)
	{
		PropertyRNA prop;

		prop= dp.prop;

		switch(prop.type) {
			case RNATypes.PROP_BOOLEAN: {
				BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;

				if(prop.arraydimension==0) {
					if(bprop.get==null && bprop.set==null && dp.booleanbit==0)
						rna_set_raw_property(dp, prop);

					bprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(bprop.get)));
					bprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(bprop.set)));
				}
				else {
					bprop.getarray= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(bprop.getarray)));
					bprop.setarray= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(bprop.setarray)));
				}
				break;
			}
			case RNATypes.PROP_INT: {
				IntPropertyRNA iprop= (IntPropertyRNA)prop;

				if(prop.arraydimension==0) {
					if(iprop.get==null && iprop.set==null)
						rna_set_raw_property(dp, prop);

					iprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(iprop.get)));
					iprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(iprop.set)));
				}
				else {
					if(iprop.getarray==null && iprop.setarray==null)
						rna_set_raw_property(dp, prop);

					iprop.getarray= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(iprop.getarray)));
					iprop.setarray= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(iprop.setarray)));
				}
				break;
			}
			case RNATypes.PROP_FLOAT: {
				FloatPropertyRNA fprop= (FloatPropertyRNA)prop;

				if(prop.arraydimension==0) {
					if(fprop.get==null && fprop.set==null)
						rna_set_raw_property(dp, prop);

					fprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(fprop.get)));
					fprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(fprop.set)));
				}
				else {
					if(fprop.getarray==null && fprop.setarray==null)
						rna_set_raw_property(dp, prop);

					fprop.getarray= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(fprop.getarray)));
					fprop.setarray= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(fprop.setarray)));
				}
				break;
			}
			case RNATypes.PROP_ENUM: {
				EnumPropertyRNA eprop= (EnumPropertyRNA)prop;

				eprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(eprop.get)));
				eprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(eprop.set)));
				break;
			}
			case RNATypes.PROP_STRING: {
				StringPropertyRNA sprop= (StringPropertyRNA)prop;

				sprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(sprop.get)));
				sprop.length= FString.toFunc(rna_def_property_length_func(f, srna, prop, dp, FString.toString(sprop.length)));
				sprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(sprop.set)));
				break;
			}
			case RNATypes.PROP_POINTER: {
				PointerPropertyRNA pprop= (PointerPropertyRNA)prop;

				pprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(pprop.get)));
				pprop.set= FString.toFunc(rna_def_property_set_func(f, srna, prop, dp, FString.toString(pprop.set)));
				if(pprop.type==null) {
					System.err.printf("rna_def_property_funcs: %s.%s, pointer must have a struct type.\n", srna.identifier, prop.identifier);
					RnaDefine.DefRNA.error= 1;
				}
				break;
			}
			case RNATypes.PROP_COLLECTION: {
				CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
				String nextfunc= FString.toString(cprop.next);

				if(dp.dnatype!=null && dp.dnatype.equals("ListBase"));
				else if(dp.dnalengthname!=null || dp.dnalengthfixed!=0)
					cprop.length= FString.toFunc(rna_def_property_length_func(f, srna, prop, dp, FString.toString(cprop.length)));

				/* test if we can allow raw array access, if it is using our standard
				 * array get/next function, we can be sure it is an actual array */
				if(cprop.next!=null && cprop.get!=null)
					if((cprop.next.toString()).equals("rna_iterator_array_next") &&
						(cprop.get.toString()).equals("rna_iterator_array_get"))
						prop.flag |= RNATypes.PROP_RAW_ARRAY;

				cprop.get= FString.toFunc(rna_def_property_get_func(f, srna, prop, dp, FString.toString(cprop.get)));
				cprop.begin= FString.toFunc(rna_def_property_begin_func(f, srna, prop, dp, FString.toString(cprop.begin)));
				cprop.next= FString.toFunc(rna_def_property_next_func(f, srna, prop, dp, FString.toString(cprop.next)));
				cprop.end= FString.toFunc(rna_def_property_end_func(f, srna, prop, dp, FString.toString(cprop.end)));
				cprop.lookupint= FString.toFunc(rna_def_property_lookup_int_func(f, srna, prop, dp, FString.toString(cprop.lookupint), nextfunc));

				if(((prop.flag&0xFFFF) & RNATypes.PROP_IDPROPERTY)==0) {
					if(cprop.begin==null) {
						System.err.printf("rna_def_property_funcs: %s.%s, collection must have a begin function.\n", srna.identifier, prop.identifier);
						RnaDefine.DefRNA.error= 1;
					}
					if(cprop.next==null) {
						System.err.printf("rna_def_property_funcs: %s.%s, collection must have a next function.\n", srna.identifier, prop.identifier);
						RnaDefine.DefRNA.error= 1;
					}
					if(cprop.get==null) {
						System.err.printf("rna_def_property_funcs: %s.%s, collection must have a get function.\n", srna.identifier, prop.identifier);
						RnaDefine.DefRNA.error= 1;
					}
				}
				if(cprop.item_type==null) {
					System.err.printf("rna_def_property_funcs: %s.%s, collection must have a struct type.\n", srna.identifier, prop.identifier);
					RnaDefine.DefRNA.error= 1;
				}
				break;
			}
		}
	}

//	static void rna_def_property_funcs_header(FILE *f, StructRNA *srna, PropertyDefRNA *dp)
//	{
//		PropertyRNA *prop;
//		char *func;
//
//		prop= dp->prop;
//
//		if(prop->flag & (PROP_IDPROPERTY|PROP_BUILTIN))
//			return;
//
//		func= rna_alloc_function_name(srna->identifier, prop->identifier, "");
//
//		switch(prop->type) {
//			case PROP_BOOLEAN:
//			case PROP_INT: {
//				if(!prop->arraydimension) {
//					fprintf(f, "int %sget(PointerRNA *ptr);\n", func);
//					//fprintf(f, "void %sset(PointerRNA *ptr, int value);\n", func);
//				}
//				else {
//					fprintf(f, "void %sget(PointerRNA *ptr, int values[%d]);\n", func, prop->totarraylength);
//					//fprintf(f, "void %sset(PointerRNA *ptr, const int values[%d]);\n", func, prop->arraylength);
//				}
//				break;
//			}
//			case PROP_FLOAT: {
//				if(!prop->arraydimension) {
//					fprintf(f, "float %sget(PointerRNA *ptr);\n", func);
//					//fprintf(f, "void %sset(PointerRNA *ptr, float value);\n", func);
//				}
//				else {
//					fprintf(f, "void %sget(PointerRNA *ptr, float values[%d]);\n", func, prop->totarraylength);
//					//fprintf(f, "void %sset(PointerRNA *ptr, const float values[%d]);\n", func, prop->arraylength);
//				}
//				break;
//			}
//			case PROP_ENUM: {
//				EnumPropertyRNA *eprop= (EnumPropertyRNA*)prop;
//				int i;
//
//				if(eprop->item) {
//					fprintf(f, "enum {\n");
//
//					for(i=0; i<eprop->totitem; i++)
//						if(eprop->item[i].identifier[0])
//							fprintf(f, "\t%s_%s_%s = %d,\n", srna->identifier, prop->identifier, eprop->item[i].identifier, eprop->item[i].value);
//
//					fprintf(f, "};\n\n");
//				}
//
//				fprintf(f, "int %sget(PointerRNA *ptr);\n", func);
//				//fprintf(f, "void %sset(PointerRNA *ptr, int value);\n", func);
//
//				break;
//			}
//			case PROP_STRING: {
//				StringPropertyRNA *sprop= (StringPropertyRNA*)prop;
//
//				if(sprop->maxlength) {
//					fprintf(f, "#define %s_%s_MAX %d\n\n", srna->identifier, prop->identifier, sprop->maxlength);
//				}
//				
//				fprintf(f, "void %sget(PointerRNA *ptr, char *value);\n", func);
//				fprintf(f, "int %slength(PointerRNA *ptr);\n", func);
//				//fprintf(f, "void %sset(PointerRNA *ptr, const char *value);\n", func);
//
//				break;
//			}
//			case PROP_POINTER: {
//				fprintf(f, "PointerRNA %sget(PointerRNA *ptr);\n", func);
//				//fprintf(f, "void %sset(PointerRNA *ptr, PointerRNA value);\n", func);
//				break;
//			}
//			case PROP_COLLECTION: {
//				fprintf(f, "void %sbegin(CollectionPropertyIterator *iter, PointerRNA *ptr);\n", func);
//				fprintf(f, "void %snext(CollectionPropertyIterator *iter);\n", func);
//				fprintf(f, "void %send(CollectionPropertyIterator *iter);\n", func);
//				//fprintf(f, "int %slength(PointerRNA *ptr);\n", func);
//				//fprintf(f, "void %slookup_int(PointerRNA *ptr, int key, StructRNA **type);\n", func);
//				//fprintf(f, "void %slookup_string(PointerRNA *ptr, const char *key, StructRNA **type);\n", func);
//				break;
//			}
//		}
//
//		fprintf(f, "\n");
//	}
//
//	static void rna_def_property_funcs_header_cpp(FILE *f, StructRNA *srna, PropertyDefRNA *dp)
//	{
//		PropertyRNA *prop;
//
//		prop= dp->prop;
//
//		if(prop->flag & (PROP_IDPROPERTY|PROP_BUILTIN))
//			return;
//		
//		if(prop->name && prop->description && strcmp(prop->description, "") != 0)
//			fprintf(f, "\t/* %s: %s */\n", prop->name, prop->description);
//		else if(prop->name)
//			fprintf(f, "\t/* %s */\n", prop->name);
//		else
//			fprintf(f, "\t/* */\n");
//
//		switch(prop->type) {
//			case PROP_BOOLEAN: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tbool %s(void);", prop->identifier);
//				else
//					fprintf(f, "\tArray<int, %d> %s(void);", prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_INT: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tint %s(void);", prop->identifier);
//				else
//					fprintf(f, "\tArray<int, %d> %s(void);", prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_FLOAT: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tfloat %s(void);", prop->identifier);
//				else
//					fprintf(f, "\tArray<float, %d> %s(void);", prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_ENUM: {
//				EnumPropertyRNA *eprop= (EnumPropertyRNA*)prop;
//				int i;
//
//				if(eprop->item) {
//					fprintf(f, "\tenum %s_enum {\n", prop->identifier);
//
//					for(i=0; i<eprop->totitem; i++)
//						if(eprop->item[i].identifier[0])
//							fprintf(f, "\t\t%s_%s = %d,\n", prop->identifier, eprop->item[i].identifier, eprop->item[i].value);
//
//					fprintf(f, "\t};\n");
//				}
//
//				fprintf(f, "\t%s_enum %s(void);", prop->identifier, prop->identifier);
//				break;
//			}
//			case PROP_STRING: {
//				fprintf(f, "\tstd::string %s(void);", prop->identifier);
//				break;
//			}
//			case PROP_POINTER: {
//				PointerPropertyRNA *pprop= (PointerPropertyRNA*)dp->prop;
//
//				if(pprop->type)
//					fprintf(f, "\t%s %s(void);", (char*)pprop->type, prop->identifier);
//				else
//					fprintf(f, "\t%s %s(void);", "UnknownType", prop->identifier);
//				break;
//			}
//			case PROP_COLLECTION: {
//				CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)dp->prop;
//
//				if(cprop->item_type)
//					fprintf(f, "\tCOLLECTION_PROPERTY(%s, %s, %s)", (char*)cprop->item_type, srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tCOLLECTION_PROPERTY(%s, %s, %s)", "UnknownType", srna->identifier, prop->identifier);
//				break;
//			}
//		}
//
//		fprintf(f, "\n");
//	}
//
//	static void rna_def_property_funcs_impl_cpp(FILE *f, StructRNA *srna, PropertyDefRNA *dp)
//	{
//		PropertyRNA *prop;
//
//		prop= dp->prop;
//
//		if(prop->flag & (PROP_IDPROPERTY|PROP_BUILTIN))
//			return;
//
//		switch(prop->type) {
//			case PROP_BOOLEAN: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tBOOLEAN_PROPERTY(%s, %s)", srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tBOOLEAN_ARRAY_PROPERTY(%s, %d, %s)", srna->identifier, prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_INT: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tINT_PROPERTY(%s, %s)", srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tINT_ARRAY_PROPERTY(%s, %d, %s)", srna->identifier, prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_FLOAT: {
//				if(!prop->arraydimension)
//					fprintf(f, "\tFLOAT_PROPERTY(%s, %s)", srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tFLOAT_ARRAY_PROPERTY(%s, %d, %s)", srna->identifier, prop->totarraylength, prop->identifier);
//				break;
//			}
//			case PROP_ENUM: {
//				fprintf(f, "\tENUM_PROPERTY(%s_enum, %s, %s)", prop->identifier, srna->identifier, prop->identifier);
//
//				break;
//			}
//			case PROP_STRING: {
//				fprintf(f, "\tSTRING_PROPERTY(%s, %s)", srna->identifier, prop->identifier);
//				break;
//			}
//			case PROP_POINTER: {
//				PointerPropertyRNA *pprop= (PointerPropertyRNA*)dp->prop;
//
//				if(pprop->type)
//					fprintf(f, "\tPOINTER_PROPERTY(%s, %s, %s)", (char*)pprop->type, srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tPOINTER_PROPERTY(%s, %s, %s)", "UnknownType", srna->identifier, prop->identifier);
//				break;
//			}
//			case PROP_COLLECTION: {
//				/*CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)dp->prop;
//
//				if(cprop->type)
//					fprintf(f, "\tCOLLECTION_PROPERTY(%s, %s, %s)", (char*)cprop->type, srna->identifier, prop->identifier);
//				else
//					fprintf(f, "\tCOLLECTION_PROPERTY(%s, %s, %s)", "UnknownType", srna->identifier, prop->identifier);*/
//				break;
//			}
//		}
//
//		fprintf(f, "\n");
//	}

	static void rna_def_function_funcs(PrintStream f, StructDefRNA dsrna, FunctionDefRNA dfunc)
	{
		StructRNA srna;
		FunctionRNA func;
		PropertyDefRNA dparm;
		int type;
		String funcname, ptrstr, valstr;
		int flag, pout, cptr, first;

		srna= dsrna.srna;
		func= dfunc.func;

		if(dfunc.call==null)
			return;

		funcname= rna_alloc_function_name(srna.identifier, func.identifier, "call");

		/* function definition */
		fprintf(f, "private void %s(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)", funcname);
		fprintf(f, "\n{\n");

		/* variable definitions */
		
		if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
			fprintf(f, "\tID _selfid;\n");
		}

		if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
			if(dsrna.dnaname!=null) fprintf(f, "\t%s _self;\n", dsrna.dnaname);
			else fprintf(f, "\t%s _self;\n", srna.identifier);
		}

		dparm= dfunc.cont.properties.first;
		for(; dparm!=null; dparm= dparm.next) {
			type = dparm.prop.type;
			flag = dparm.prop.flag;
			pout = (flag & RNATypes.PROP_OUTPUT);
			cptr = ((type == RNATypes.PROP_POINTER) && (flag & RNATypes.PROP_RNAPTR)==0)?1:0;

//			if(dparm.prop==func.c_ret)
//				ptrstr= cptr!=0 || dparm.prop.arraydimension!=0 ? "*" : "";
//			/* XXX only arrays and strings are allowed to be dynamic, is this checked anywhere? */
//			else if (cptr!=0 || (flag & RNATypes.PROP_DYNAMIC)!=0)
//				ptrstr= pout!=0 ? "**" : "*";
//			/* fixed size arrays and RNA pointers are pre-allocated on the ParameterList stack, pass a pointer to it */
//			else if (type == RNATypes.PROP_POINTER || dparm.prop.arraydimension!=0)
//				ptrstr= "*";
//			/* PROP_THICK_WRAP strings are pre-allocated on the ParameterList stack, but type name for string props is already char*, so leave empty */
//			else if (type == RNATypes.PROP_STRING && (flag & RNATypes.PROP_THICK_WRAP)!=0)
//				ptrstr= "";
//			else
//				ptrstr= pout!=0 ? "*" : "";
			ptrstr= "";

			/* for dynamic parameters we pass an additional int for the length of the parameter */
			if ((flag & RNATypes.PROP_DYNAMIC)!=0)
				fprintf(f, "\tint %s_len;\n", dparm.prop.identifier);
			
			fprintf(f, "\t%s%s %s%s;\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, dparm.prop.identifier);
		}

		fprintf(f, "\tObject[] _data");
		if(func.c_ret!=null) fprintf(f, ", _retdata");
		fprintf(f, ";\n");
		fprintf(f, "\tint _data_p=0");
		if(func.c_ret!=null) fprintf(f, ", _retdata_p=0");
		fprintf(f, ";\n");
		fprintf(f, "\t\n");

		/* assign self */
		if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
			fprintf(f, "\t_selfid= (ID)_ptr.id.data;\n");
		}
		
		if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
			if(dsrna.dnaname!=null) fprintf(f, "\t_self= (%s)_ptr.data;\n", dsrna.dnaname);
			else fprintf(f, "\t_self= (%s)_ptr.data;\n", srna.identifier);
		}

		fprintf(f, "\t_data= (Object[])_parms.data;\n");

		dparm= dfunc.cont.properties.first;
		for(; dparm!=null; dparm= dparm.next) {
			type = dparm.prop.type&0xFF;
			flag = dparm.prop.flag&0xFFFF;
			pout = (flag & RNATypes.PROP_OUTPUT);
			cptr = ((type == RNATypes.PROP_POINTER) && (flag & RNATypes.PROP_RNAPTR)==0)?1:0;

			if(dparm.prop==func.c_ret) {
				fprintf(f, "\t_retdata= _data;\n");
				fprintf(f, "\t_retdata_p= _data_p;\n");
			}
			else  {
				String data_str;
//				if (cptr!=0 || (flag & RNATypes.PROP_DYNAMIC)!=0) {
//					ptrstr= "**";
//					valstr= "*";
//				}
//				else if (type == RNATypes.PROP_POINTER || dparm.prop.arraydimension!=0) {
//					ptrstr= "*";
//					valstr= "";
//				}
//				else if (type == RNATypes.PROP_STRING && (flag & RNATypes.PROP_THICK_WRAP)!=0) {
//					ptrstr= "";
//					valstr= "";
//				}
//				else {
//					ptrstr= "*";
//					valstr= "*";
//				}
				ptrstr= "";
				valstr= "";

				/* this must be kept in sync with RNA_parameter_length_get_data, we could just call the function directly, but this is faster */
				if ((flag & RNATypes.PROP_DYNAMIC)!=0) {
					fprintf(f, "\t%s_len= (_data.length);\n", dparm.prop.identifier);
					data_str= "(_data[_data_p])";
				}
				else {
					data_str= "_data[_data_p]";
				}
				fprintf(f, "\t%s= ", dparm.prop.identifier);

				if (pout==0)
					fprintf(f, "%s", valstr);

				fprintf(f, "((%s%s%s)%s);\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, data_str);
			}

			if(dparm.next!=null)
				fprintf(f, "\t_data_p++;\n");
		}

		if(dfunc.call!=null) {
			fprintf(f, "\t\n");
			fprintf(f, "\t");
			if(func.c_ret!=null) fprintf(f, "%s= ", func.c_ret.identifier);
			fprintf(f, "%s(", dfunc.call);

			first= 1;

			if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
				fprintf(f, "_selfid");
				first= 0;
			}

			if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
				if(first==0) fprintf(f, ", ");
				fprintf(f, "_self");
				first= 0;
			}

			if((func.flag & RNATypes.FUNC_USE_CONTEXT)!=0) {
				if(first==0) fprintf(f, ", ");
				first= 0;
				fprintf(f, "C");
			}

			if((func.flag & RNATypes.FUNC_USE_REPORTS)!=0) {
				if(first==0) fprintf(f, ", ");
				first= 0;
				fprintf(f, "reports");
			}

			dparm= dfunc.cont.properties.first;
			for(; dparm!=null; dparm= dparm.next) {
				if(dparm.prop==func.c_ret)
					continue;

				if(first==0) fprintf(f, ", ");
				first= 0;

				if (((dparm.prop.flag&0xFFFF) & RNATypes.PROP_DYNAMIC)!=0)
					fprintf(f, "%s_len, %s", dparm.prop.identifier, dparm.prop.identifier);
				else
					fprintf(f, "%s", dparm.prop.identifier);
			}

			fprintf(f, ");\n");

			if(func.c_ret!=null) {
				dparm= RnaDefine.rna_find_parameter_def(func.c_ret);
				ptrstr= ((((dparm.prop.type&0xFF) == RNATypes.PROP_POINTER) && ((dparm.prop.flag&0xFFFF) & RNATypes.PROP_RNAPTR)==0) || (dparm.prop.arraydimension!=0))? "*": "";
				fprintf(f, "\t_retdata[_retdata_p]= %s;\n", func.c_ret.identifier);
			}
		}

		fprintf(f, "}\n\n");

		dfunc.gencall= funcname;
	}
	
	static void rna_def_function_funcs_java(PrintStream f, StructDefRNA dsrna, FunctionDefRNA dfunc)
	{
		StructRNA srna;
		FunctionRNA func;
		PropertyDefRNA dparm;
//		PropertyType type;
		int type;
		String funcname, ptrstr, valstr;
		int flag, pout, cptr, first;

		srna= dsrna.srna;
		func= dfunc.func;

		if(dfunc.call==null)
			return;

		funcname= rna_alloc_function_name(srna.identifier, func.identifier, "call");

		/* function definition */
//		fprintf(f, "void %s(bContext *C, ReportList *reports, PointerRNA *_ptr, ParameterList *_parms)", funcname);
		fprintf(f, "private void %s(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)", funcname);
		fprintf(f, "\n{\n");

		/* variable definitions */
		
		if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
//			fprintf(f, "\tstruct ID *_selfid;\n");
			fprintf(f, "\tID _selfid;\n");
		}

		if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
//			if(dsrna.dnaname!=null) fprintf(f, "\tstruct %s *_self;\n", dsrna.dnaname);
			if(dsrna.dnaname!=null) fprintf(f, "\t%s _self;\n", dsrna.dnaname);
//			else fprintf(f, "\tstruct %s *_self;\n", srna.identifier);
			else fprintf(f, "\t%s _self;\n", srna.identifier);
		}

		dparm= dfunc.cont.properties.first;
		for(; dparm!=null; dparm= dparm.next) {
			type = dparm.prop.type;
			flag = dparm.prop.flag;
			pout = (flag & RNATypes.PROP_OUTPUT);
			cptr = ((type == RNATypes.PROP_POINTER) && (flag & RNATypes.PROP_RNAPTR)==0)?1:0;

//			if(dparm.prop==func.c_ret)
//				ptrstr= cptr!=0 || dparm.prop.arraydimension!=0 ? "*" : "";
//			/* XXX only arrays and strings are allowed to be dynamic, is this checked anywhere? */
//			else if (cptr!=0 || (flag & RNATypes.PROP_DYNAMIC)!=0)
//				ptrstr= pout!=0 ? "**" : "*";
//			/* fixed size arrays and RNA pointers are pre-allocated on the ParameterList stack, pass a pointer to it */
//			else if (type == RNATypes.PROP_POINTER || dparm.prop.arraydimension!=0)
//				ptrstr= "*";
//			/* PROP_THICK_WRAP strings are pre-allocated on the ParameterList stack, but type name for string props is already char*, so leave empty */
//			else if (type == RNATypes.PROP_STRING && (flag & RNATypes.PROP_THICK_WRAP)!=0)
//				ptrstr= "";
//			else
//				ptrstr= pout!=0 ? "*" : "";
			ptrstr = "";

			/* for dynamic parameters we pass an additional int for the length of the parameter */
			if ((flag & RNATypes.PROP_DYNAMIC)!=0)
				fprintf(f, "\tint %s%s_len;\n", pout!=0 ? "*" : "", dparm.prop.identifier);
			
			fprintf(f, "\t%s%s %s%s;\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, dparm.prop.identifier);
		}

//		fprintf(f, "\tchar *_data");
		fprintf(f, "\tObject[] _data");
//		if(func.c_ret!=null) fprintf(f, ", *_retdata");
		if(func.c_ret!=null) fprintf(f, ", _retdata");
		fprintf(f, ";\n");
		fprintf(f, "\tint _data_p=0");
		if(func.c_ret!=null) fprintf(f, ", _retdata_p=0");
		fprintf(f, ";\n");
		fprintf(f, "\t\n");

		/* assign self */
		if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
//			fprintf(f, "\t_selfid= (struct ID*)_ptr->id.data;\n");
			fprintf(f, "\t_selfid= (ID)_ptr.id.data;\n");
		}
		
		if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
//			if(dsrna.dnaname!=null) fprintf(f, "\t_self= (struct %s *)_ptr->data;\n", dsrna.dnaname);
			if(dsrna.dnaname!=null) fprintf(f, "\t_self= (%s)_ptr.data;\n", dsrna.dnaname);
//			else fprintf(f, "\t_self= (struct %s *)_ptr->data;\n", srna.identifier);
			else fprintf(f, "\t_self= (%s)_ptr.data;\n", srna.identifier);
		}

//		fprintf(f, "\t_data= (char *)_parms->data;\n");
		fprintf(f, "\t_data= (Object[])_parms.data;\n");

		dparm= dfunc.cont.properties.first;
		for(; dparm!=null; dparm= dparm.next) {
			type = dparm.prop.type&0xFF;
			flag = dparm.prop.flag&0xFFFF;
			pout = (flag & RNATypes.PROP_OUTPUT);
			cptr = ((type == RNATypes.PROP_POINTER) && (flag & RNATypes.PROP_RNAPTR)==0)?1:0;

			if(dparm.prop==func.c_ret) {
				fprintf(f, "\t_retdata= _data;\n");
				fprintf(f, "\t_retdata_p= _data_p;\n");
			}
			else  {
				String data_str;
//				if (cptr!=0 || (flag & RNATypes.PROP_DYNAMIC)!=0) {
//					ptrstr= "**";
//					valstr= "*";
//				}
//				else if (type == RNATypes.PROP_POINTER || dparm.prop.arraydimension!=0) {
//					ptrstr= "*";
//					valstr= "";
//				}
//				else if (type == RNATypes.PROP_STRING && (flag & RNATypes.PROP_THICK_WRAP)!=0) {
//					ptrstr= "";
//					valstr= "";
//				}
//				else {
//					ptrstr= "*";
//					valstr= "*";
//				}
				ptrstr= "";
				valstr= "";

				/* this must be kept in sync with RNA_parameter_length_get_data, we could just call the function directly, but this is faster */
				if ((flag & RNATypes.PROP_DYNAMIC)!=0) {
//					fprintf(f, "\t%s_len= %s((int *)_data);\n", dparm.prop.identifier, pout!=0 ? "" : "*");
					fprintf(f, "\t%s_len= (Integer)_data;\n", dparm.prop.identifier);
//					data_str= "(&(((char *)_data)[sizeof(void *)]))";
					data_str= "_data";
				}
				else {
					data_str= "_data";
				}
				fprintf(f, "\t%s= ", dparm.prop.identifier);

				if (pout==0)
					fprintf(f, "%s", valstr);

//				fprintf(f, "((%s%s%s)%s);\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, data_str);
				fprintf(f, "(%s%s%s)%s[_data_p];\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, data_str);
			}

			if(dparm.next!=null)
//				fprintf(f, "\t_data+= %d;\n", RnaDefine.rna_parameter_size_alloc(dparm.prop));
				fprintf(f, "\t_data_p++;\n");
		}

		if(dfunc.call!=null) {
			fprintf(f, "\t\n");
			fprintf(f, "\t");
			if(func.c_ret!=null) fprintf(f, "%s= ", func.c_ret.identifier);
			fprintf(f, "%s(", dfunc.call);

			first= 1;

			if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
				fprintf(f, "_selfid");
				first= 0;
			}

			if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
				if(first==0) fprintf(f, ", ");
				fprintf(f, "_self");
				first= 0;
			}

			if((func.flag & RNATypes.FUNC_USE_CONTEXT)!=0) {
				if(first==0) fprintf(f, ", ");
				first= 0;
				fprintf(f, "C");
			}

			if((func.flag & RNATypes.FUNC_USE_REPORTS)!=0) {
				if(first==0) fprintf(f, ", ");
				first= 0;
				fprintf(f, "reports");
			}

			dparm= dfunc.cont.properties.first;
			for(; dparm!=null; dparm= dparm.next) {
				if(dparm.prop==func.c_ret)
					continue;

				if(first==0) fprintf(f, ", ");
				first= 0;

				if (((dparm.prop.flag&0xFFFF) & RNATypes.PROP_DYNAMIC)!=0)
					fprintf(f, "%s_len, %s", dparm.prop.identifier, dparm.prop.identifier);
				else
					fprintf(f, "%s", dparm.prop.identifier);
			}

			fprintf(f, ");\n");

			if(func.c_ret!=null) {
				dparm= RnaDefine.rna_find_parameter_def(func.c_ret);
//				ptrstr= ((((dparm.prop.type&0xFF) == RNATypes.PROP_POINTER) && ((dparm.prop.flag&0xFFFF) & RNATypes.PROP_RNAPTR)==0) || (dparm.prop.arraydimension!=0))? "*": "";
//				fprintf(f, "\t*((%s%s%s*)_retdata)= %s;\n", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, func.c_ret.identifier);
				fprintf(f, "\t_retdata[_retdata_p]= %s;\n", func.c_ret.identifier);
			}
		}

		fprintf(f, "}\n\n");

		dfunc.gencall= funcname;
	}

//	static void rna_auto_types()
//	{
//		StructDefRNA *ds;
//		PropertyDefRNA *dp;
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next) {
//			/* DNA name for Screen is patched in 2.5, we do the reverse here .. */
//			if(ds->dnaname && strcmp(ds->dnaname, "Screen") == 0)
//				ds->dnaname= "bScreen";
//
//			for(dp=ds->cont.properties.first; dp; dp=dp->next) {
//				if(dp->dnastructname && strcmp(dp->dnastructname, "Screen") == 0)
//					dp->dnastructname= "bScreen";
//
//				if(dp->dnatype) {
//					if(dp->prop->type == PROP_POINTER) {
//						PointerPropertyRNA *pprop= (PointerPropertyRNA*)dp->prop;
//						StructRNA *type;
//
//						if(!pprop->type && !pprop->get)
//							pprop->type= (StructRNA*)rna_find_type(dp->dnatype);
//
//						if(pprop->type) {
//							type= rna_find_struct((char*)pprop->type);
//							if(type && (type->flag & STRUCT_ID_REFCOUNT))
//								pprop->property.flag |= PROP_ID_REFCOUNT;
//						}
//					}
//					else if(dp->prop->type== PROP_COLLECTION) {
//						CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)dp->prop;
//
//						if(!cprop->item_type && !cprop->get && strcmp(dp->dnatype, "ListBase")==0)
//							cprop->item_type= (StructRNA*)rna_find_type(dp->dnatype);
//					}
//				}
//			}
//		}
//	}
//
//	static void rna_sort(BlenderRNA *brna)
//	{
//		StructDefRNA *ds;
//		StructRNA *srna;
//
//		rna_sortlist(&brna->structs, cmp_struct);
//		rna_sortlist(&DefRNA.structs, cmp_def_struct);
//
//		for(srna=brna->structs.first; srna; srna=srna->cont.next)
//			rna_sortlist(&srna->cont.properties, cmp_property);
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next)
//			rna_sortlist(&ds->cont.properties, cmp_def_property);
//	}

	static String rna_property_structname(int type)
	{
		switch(type) {
			case RNATypes.PROP_BOOLEAN: return "BooleanPropertyRNA";
			case RNATypes.PROP_INT: return "IntPropertyRNA";
			case RNATypes.PROP_FLOAT: return "FloatPropertyRNA";
			case RNATypes.PROP_STRING: return "StringPropertyRNA";
			case RNATypes.PROP_ENUM: return "EnumPropertyRNA";
			case RNATypes.PROP_POINTER: return "PointerPropertyRNA";
			case RNATypes.PROP_COLLECTION: return "CollectionPropertyRNA";
			default: return "UnknownPropertyRNA";
		}
	}
	
	static String rna_property_structname_java(int type)
	{
		switch(type) {
			case RNATypes.PROP_BOOLEAN: return "BooleanPropertyRNA";
//			case RNATypes.PROP_BOOLEAN: return "boolean";
			case RNATypes.PROP_INT: return "IntPropertyRNA";
//			case RNATypes.PROP_INT: return "int";
			case RNATypes.PROP_FLOAT: return "FloatPropertyRNA";
//			case RNATypes.PROP_FLOAT: return "float";
			case RNATypes.PROP_STRING: return "StringPropertyRNA";
//			case RNATypes.PROP_STRING: return "String";
			case RNATypes.PROP_ENUM: return "EnumPropertyRNA";
//			case RNATypes.PROP_ENUM: return "/*enum*/ int";
			case RNATypes.PROP_POINTER: return "PointerPropertyRNA";
//			case RNATypes.PROP_POINTER: return "Object";
			case RNATypes.PROP_COLLECTION: return "CollectionPropertyRNA";
//			case RNATypes.PROP_COLLECTION: return "Object[]";
			default: return "UnknownPropertyRNA";
		}
	}

//	static String rna_property_typename(PropertyType type)
	static String rna_property_typename(int type)
	{
		switch(type) {
			case RNATypes.PROP_BOOLEAN: return "PROP_BOOLEAN";
			case RNATypes.PROP_INT: return "PROP_INT";
			case RNATypes.PROP_FLOAT: return "PROP_FLOAT";
			case RNATypes.PROP_STRING: return "PROP_STRING";
			case RNATypes.PROP_ENUM: return "PROP_ENUM";
			case RNATypes.PROP_POINTER: return "PROP_POINTER";
			case RNATypes.PROP_COLLECTION: return "PROP_COLLECTION";
			default: return "PROP_UNKNOWN";
		}
	}

	static String rna_property_subtypename(int type)
	{
		switch(type) {
			case RNATypes.PROP_NONE: return "PROP_NONE";
			case RNATypes.PROP_FILEPATH: return "PROP_FILEPATH";
			case RNATypes.PROP_FILENAME: return "PROP_FILENAME";
			case RNATypes.PROP_DIRPATH: return "PROP_DIRPATH";
			case RNATypes.PROP_UNSIGNED: return "PROP_UNSIGNED";
			case RNATypes.PROP_PERCENTAGE: return "PROP_PERCENTAGE";
			case RNATypes.PROP_FACTOR: return "PROP_FACTOR";
			case RNATypes.PROP_ANGLE: return "PROP_ANGLE";
			case RNATypes.PROP_TIME: return "PROP_TIME";
			case RNATypes.PROP_DISTANCE: return "PROP_DISTANCE";
			case RNATypes.PROP_COLOR: return "PROP_COLOR";
			case RNATypes.PROP_TRANSLATION: return "PROP_TRANSLATION";
			case RNATypes.PROP_DIRECTION: return "PROP_DIRECTION";
			case RNATypes.PROP_MATRIX: return "PROP_MATRIX";
			case RNATypes.PROP_EULER: return "PROP_EULER";
			case RNATypes.PROP_QUATERNION: return "PROP_QUATERNION";
			case RNATypes.PROP_AXISANGLE: return "PROP_AXISANGLE";
			case RNATypes.PROP_VELOCITY: return "PROP_VELOCITY";
			case RNATypes.PROP_ACCELERATION: return "PROP_ACCELERATION";
			case RNATypes.PROP_XYZ: return "PROP_XYZ";
			case RNATypes.PROP_COLOR_GAMMA: return "PROP_COLOR_GAMMA";
			case RNATypes.PROP_LAYER: return "PROP_LAYER";
			case RNATypes.PROP_LAYER_MEMBER: return "PROP_LAYER_MEMBER";
			default: {
				/* incase we dont have a type preset that includes the subtype */
				if(RNATypes.RNA_SUBTYPE_UNIT(type)!=0) {
					return rna_property_subtypename((type & ~RNATypes.RNA_SUBTYPE_UNIT(type)));
				}
				else {
					return "PROP_SUBTYPE_UNKNOWN";
				}
			}
		}
	}

	static String rna_property_subtype_unit(int type)
	{
		switch(RNATypes.RNA_SUBTYPE_UNIT(type)) {
			case RNATypes.PROP_UNIT_NONE:		return "PROP_UNIT_NONE";
			case RNATypes.PROP_UNIT_LENGTH:		return "PROP_UNIT_LENGTH";
			case RNATypes.PROP_UNIT_AREA:		return "PROP_UNIT_AREA";
			case RNATypes.PROP_UNIT_VOLUME:		return "PROP_UNIT_VOLUME";
			case RNATypes.PROP_UNIT_MASS:		return "PROP_UNIT_MASS";
			case RNATypes.PROP_UNIT_ROTATION:	return "PROP_UNIT_ROTATION";
			case RNATypes.PROP_UNIT_TIME:		return "PROP_UNIT_TIME";
			case RNATypes.PROP_UNIT_VELOCITY:	return "PROP_UNIT_VELOCITY";
			case RNATypes.PROP_UNIT_ACCELERATION:return "PROP_UNIT_ACCELERATION";
			default:					return "PROP_UNIT_UNKNOWN";
		}
	}

	static void rna_generate_prototypes(BlenderRNA brna, PrintStream f)
	{
		StructRNA srna;

		for(srna=brna.structs.first; srna!=null; srna=(StructRNA)srna.cont.next)
			fprintf(f, "extern StructRNA RNA_%s;\n", srna.identifier);
		fprintf(f, "\n");
	}

//	static void rna_generate_blender(BlenderRNA *brna, FILE *f)
//	{
//		StructRNA *srna;
//
//		fprintf(f, "BlenderRNA BLENDER_RNA = {");
//
//		srna= brna->structs.first;
//		if(srna) fprintf(f, "{&RNA_%s, ", srna->identifier);
//		else fprintf(f, "{NULL, ");
//
//		srna= brna->structs.last;
//		if(srna) fprintf(f, "&RNA_%s}", srna->identifier);
//		else fprintf(f, "NULL}");
//
//		fprintf(f, "};\n\n");
//	}

	static void rna_generate_property_prototypes(BlenderRNA brna, StructRNA srna, PrintStream f)
	{
		PropertyRNA prop;
		StructRNA base;

		base= srna.base;
		while (base!=null) {
			fprintf(f, "\n");
			for(prop=(PropertyRNA)base.cont.properties.first; prop!=null; prop=(PropertyRNA)prop.next)
				fprintf(f, "%s%s rna_%s_%s;\n", "extern ", rna_property_structname(prop.type), base.identifier, prop.identifier);
			base= base.base;
		}

		if(srna.cont.properties.first!=null)
			fprintf(f, "\n");

		for(prop=(PropertyRNA)srna.cont.properties.first; prop!=null; prop=(PropertyRNA)prop.next)
			fprintf(f, "%s%s rna_%s_%s;\n", (prop.flag & RNATypes.PROP_EXPORT)!=0? "": "", rna_property_structname(prop.type), srna.identifier, prop.identifier);
		fprintf(f, "\n");
	}

	static void rna_generate_parameter_prototypes(BlenderRNA brna, StructRNA srna, FunctionRNA func, PrintStream f)
	{
		PropertyRNA parm;

		for(parm= (PropertyRNA)func.cont.properties.first; parm!=null; parm= (PropertyRNA)parm.next)
			fprintf(f, "%s%s rna_%s_%s_%s;\n", "extern ", rna_property_structname(parm.type), srna.identifier, func.identifier, parm.identifier);

		if(func.cont.properties.first!=null)
			fprintf(f, "\n");
	}

	static void rna_generate_function_prototypes(BlenderRNA brna, StructRNA srna, PrintStream f)
	{
		FunctionRNA func;
		StructRNA base;

		base= srna.base;
		while (base!=null) {
			for(func= (FunctionRNA)base.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
				fprintf(f, "%s%s rna_%s_%s_func;\n", "extern ", "FunctionRNA", base.identifier, func.identifier);
				rna_generate_parameter_prototypes(brna, base, func, f);
			}

			if(base.functions.first!=null)
				fprintf(f, "\n");

			base= base.base;
		}

		for(func= (FunctionRNA)srna.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
			fprintf(f, "%s%s rna_%s_%s_func;\n", "extern ", "FunctionRNA", srna.identifier, func.identifier);
			rna_generate_parameter_prototypes(brna, srna, func, f);
		}

		if(srna.functions.first!=null)
			fprintf(f, "\n");
	}

	static void rna_generate_static_parameter_prototypes(BlenderRNA brna, StructRNA srna, FunctionDefRNA dfunc, PrintStream f)
	{
		FunctionRNA func;
		PropertyDefRNA dparm;
		StructDefRNA dsrna;
		int type;
		int flag, pout, cptr, first;
		String ptrstr;

		dsrna= RnaDefine.rna_find_struct_def(srna);
		func= dfunc.func;

		/* return type */
		for(dparm= dfunc.cont.properties.first; dparm!=null; dparm= dparm.next) {
			if(dparm.prop==func.c_ret) {
				if(dparm.prop.arraydimension!=0)
					fprintf(f, "XXX no array return types yet"); /* XXX not supported */
				else if((dparm.prop.type) == RNATypes.PROP_POINTER && ((dparm.prop.flag) & RNATypes.PROP_RNAPTR)==0)
					fprintf(f, "%s%s *", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop));
				else
					fprintf(f, "%s%s ", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop));

				break;
			}
		}

		/* void if nothing to return */
		if(dparm==null)
			fprintf(f, "void ");

		/* function name */
		fprintf(f, "%s(", dfunc.call);

		first= 1;

		/* self, context and reports parameters */
		if((func.flag & RNATypes.FUNC_USE_SELF_ID)!=0) {
			fprintf(f, "struct ID *_selfid");
			first= 0;		
		}
		
		if((func.flag & RNATypes.FUNC_NO_SELF)==0) {
			if(first==0) fprintf(f, ", ");
			if(dsrna.dnaname!=null) fprintf(f, "struct %s *_self", dsrna.dnaname);
			else fprintf(f, "struct %s *_self", srna.identifier);
			first= 0;
		}

		if((func.flag & RNATypes.FUNC_USE_CONTEXT)!=0) {
			if(first==0) fprintf(f, ", ");
			first= 0;
			fprintf(f, "bContext *C");
		}

		if((func.flag & RNATypes.FUNC_USE_REPORTS)!=0) {
			if(first==0) fprintf(f, ", ");
			first= 0;
			fprintf(f, "ReportList *reports");
		}

		/* defined parameters */
		for(dparm= dfunc.cont.properties.first; dparm!=null; dparm= dparm.next) {
			type = dparm.prop.type&0xFF;
			flag = dparm.prop.flag&0xFFFF;
			pout = (flag & RNATypes.PROP_OUTPUT);
			cptr = ((type == RNATypes.PROP_POINTER) && (flag & RNATypes.PROP_RNAPTR)==0)?1:0;

			if(dparm.prop==func.c_ret)
				continue;

			if (cptr!=0 || (flag & RNATypes.PROP_DYNAMIC)!=0)
				ptrstr= pout!=0 ? "**" : "*";
			else if (type == RNATypes.PROP_POINTER || dparm.prop.arraydimension!=0)
				ptrstr= "*";
			else if (type == RNATypes.PROP_STRING && (flag & RNATypes.PROP_THICK_WRAP)!=0)
				ptrstr= "";
			else
				ptrstr= pout!=0 ? "*" : "";

			if(first==0) fprintf(f, ", ");
			first= 0;

			if ((flag & RNATypes.PROP_DYNAMIC)!=0)
				fprintf(f, "int %s%s_len, ", pout!=0 ? "*" : "", dparm.prop.identifier);

			if((flag & RNATypes.PROP_DYNAMIC)==0 && dparm.prop.arraydimension!=0)
				fprintf(f, "%s%s %s[%d]", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), dparm.prop.identifier, dparm.prop.totarraylength);
			else
				fprintf(f, "%s%s %s%s", rna_type_struct(dparm.prop), rna_parameter_type_name(dparm.prop), ptrstr, dparm.prop.identifier);

		}

		fprintf(f, ");\n");
	}

	static void rna_generate_static_function_prototypes(BlenderRNA brna, StructRNA srna, PrintStream f)
	{
		FunctionRNA func;
		FunctionDefRNA dfunc;
		int first= 1;

		for(func= (FunctionRNA)srna.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
			dfunc= RnaDefine.rna_find_function_def(func);

			if(dfunc.call!=null) {
				if(first!=0) {
					fprintf(f, "/* Repeated prototypes to detect errors */\n\n");
					first= 0;
				}

				rna_generate_static_parameter_prototypes(brna, srna, dfunc, f);
			}
		}

		fprintf(f, "\n");
	}

	static void rna_generate_property(PrintStream f, StructRNA srna, String nest, PropertyRNA prop) 
	{
		String strnest= "", errnest= "";
		int len, freenest= 0;
		
		if(nest != null) {
//			len= strlen(nest);
			len= nest.length();

//			strnest= MEM_mallocN(sizeof(char)*(len+2), "rna_generate_property -> strnest");
//			errnest= MEM_mallocN(sizeof(char)*(len+2), "rna_generate_property -> errnest");

//			strcpy(strnest, "_"); strcat(strnest, nest);
			strnest = "_"; strnest += nest;
//			strcpy(errnest, "."); strcat(errnest, nest);
			errnest = "."; errnest += nest;

			freenest= 1;
		}

		switch(prop.type) {
				case RNATypes.PROP_ENUM: {
					EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
					int i, defaultfound= 0;

					if(eprop.item!=null) {
						fprintf(f, "private static EnumPropertyItem[] rna_%s%s_%s_items = {\n\t", srna.identifier, strnest, prop.identifier);

						for(i=0; i<eprop.totitem; i++) {
							fprintf(f, "new EnumPropertyItem(%d, ", eprop.item[i].value);
							rna_print_c_string(f, eprop.item[i].identifier); fprintf(f, ", ");
							fprintf(f, "%d, ", eprop.item[i].icon);
							rna_print_c_string(f, eprop.item[i].name); fprintf(f, ", ");
							rna_print_c_string(f, eprop.item[i].description); fprintf(f, "),\n\t");

//							if(eprop.item[i].identifier[0])
							if(!eprop.item[i].identifier.isEmpty())
								if(eprop.defaultvalue == eprop.item[i].value)
									defaultfound= 1;
						}

						fprintf(f, "new EnumPropertyItem(0, null, 0, null, null)\n};\n\n");

						if(defaultfound==0) {
//							fprintf(stderr, "rna_generate_structs: %s%s.%s, enum default is not in items.\n", srna.identifier, errnest, prop.identifier);
							System.err.printf("rna_generate_structs: %s%s.%s, enum default is not in items.\n", srna.identifier, errnest, prop.identifier);
							RnaDefine.DefRNA.error= 1;
						}
					}
					else {
//						fprintf(stderr, "rna_generate_structs: %s%s.%s, enum must have items defined.\n", srna.identifier, errnest, prop.identifier);
						System.err.printf("rna_generate_structs: %s%s.%s, enum must have items defined.\n", srna.identifier, errnest, prop.identifier);
						RnaDefine.DefRNA.error= 1;
					}
					break;
				}
				case RNATypes.PROP_BOOLEAN: {
					BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static int rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(bprop.defaultarray!=null)
								fprintf(f, "%d", bprop.defaultarray[i]);
							else
								fprintf(f, "%d", bprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				case RNATypes.PROP_INT: {
					IntPropertyRNA iprop= (IntPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static int rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(iprop.defaultarray!=null)
								fprintf(f, "%d", iprop.defaultarray[i]);
							else
								fprintf(f, "%d", iprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				case RNATypes.PROP_FLOAT: {
					FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static float rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(fprop.defaultarray!=null)
								rna_float_print(f, fprop.defaultarray[i]);
							else
								rna_float_print(f, fprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				default:
					break;
		}

		fprintf(f, "private static %s%s rna_%s%s_%s = new %s(\n", (prop.flag & RNATypes.PROP_EXPORT)!=0? "": "", rna_property_structname(prop.type), srna.identifier, strnest, prop.identifier, rna_property_structname(prop.type));

//		if(prop.next!=null) fprintf(f, "\trna_%s%s_%s, ", srna.identifier, strnest, prop.next.identifier);
//		else
			fprintf(f, "\tnull, ");
//		if(prop.prev!=null) fprintf(f, "rna_%s%s_%s,\n", srna.identifier, strnest, prop.prev.identifier);
//		else
			fprintf(f, "null,\n");
		fprintf(f, "\t%d, ", prop.magic);
		rna_print_c_string(f, prop.identifier);
		fprintf(f, ", %d, ", prop.flag);
		rna_print_c_string(f, prop.name); fprintf(f, ",\n\t");
		rna_print_c_string(f, prop.description); fprintf(f, ",\n\t");
		fprintf(f, "%d,\n", prop.icon);
		fprintf(f, "\t%s, %s|%s, %s, %d, new int[]{%d, %d, %d}, %d,\n", rna_property_typename(prop.type), rna_property_subtypename(prop.subtype), rna_property_subtype_unit(prop.subtype), rna_function_string(prop.getlength), prop.arraydimension, prop.arraylength[0], prop.arraylength[1], prop.arraylength[2], prop.totarraylength);
		fprintf(f, "\t%s%s, %d, %s, %s,\n", (prop.flag & RNATypes.PROP_CONTEXT_UPDATE)!=0? "": "", rna_function_string(prop.update), prop.noteflag, rna_function_string(prop.editable), rna_function_string(prop.itemeditable));

		if((prop.flag & RNATypes.PROP_RAW_ACCESS)!=0) rna_set_raw_offset(f, srna, prop);
		else fprintf(f, "\t0, -1");

		/* our own type - collections/arrays only */
		if(prop.srna!=null) fprintf(f, ", Rna%s.RNA_%s", prop.srna.toString(), prop.srna.toString());
		else fprintf(f, ", null");

		fprintf(f, ",\n");

		switch(prop.type) {
				case RNATypes.PROP_BOOLEAN: {
					BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s, ", rna_function_string(bprop.get), rna_function_string(bprop.set), rna_function_string(bprop.getarray), rna_function_string(bprop.setarray), bprop.defaultvalue);
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				}
				case RNATypes.PROP_INT: {
					IntPropertyRNA iprop= (IntPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s,\n\t", rna_function_string(iprop.get), rna_function_string(iprop.set), rna_function_string(iprop.getarray), rna_function_string(iprop.setarray), rna_function_string(iprop.range));
					rna_int_print(f, iprop.softmin); fprintf(f, ", ");
					rna_int_print(f, iprop.softmax); fprintf(f, ", ");
					rna_int_print(f, iprop.hardmin); fprintf(f, ", ");
					rna_int_print(f, iprop.hardmax); fprintf(f, ", ");
					rna_int_print(f, iprop.step); fprintf(f, ", ");
					rna_int_print(f, iprop.defaultvalue); fprintf(f, ", ");
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				 }
				case RNATypes.PROP_FLOAT: {
					FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s, ", rna_function_string(fprop.get), rna_function_string(fprop.set), rna_function_string(fprop.getarray), rna_function_string(fprop.setarray), rna_function_string(fprop.range));
					rna_float_print(f, fprop.softmin); fprintf(f, ", ");
					rna_float_print(f, fprop.softmax); fprintf(f, ", ");
					rna_float_print(f, fprop.hardmin); fprintf(f, ", ");
					rna_float_print(f, fprop.hardmax); fprintf(f, ", ");
					rna_float_print(f, fprop.step); fprintf(f, ", ");
					rna_int_print(f, (int)fprop.precision); fprintf(f, ", ");
					rna_float_print(f, fprop.defaultvalue); fprintf(f, ", ");
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				 }
				case RNATypes.PROP_STRING: {
					StringPropertyRNA sprop= (StringPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %d, ", rna_function_string(sprop.get), rna_function_string(sprop.length), rna_function_string(sprop.set), sprop.maxlength);
					rna_print_c_string(f, sprop.defaultvalue); fprintf(f, "\n");
					break;
				}
				case RNATypes.PROP_ENUM: {
					EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, ", rna_function_string(eprop.get), rna_function_string(eprop.set), rna_function_string(eprop.itemf));
					if(eprop.item!=null)
						fprintf(f, "rna_%s%s_%s_items, ", srna.identifier, strnest, prop.identifier);
					else
						fprintf(f, "null, ");
					fprintf(f, "%d, %d\n", eprop.totitem, eprop.defaultvalue);
					break;
				}
				case RNATypes.PROP_POINTER: {
					PointerPropertyRNA pprop= (PointerPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s,", rna_function_string(pprop.get), rna_function_string(pprop.set), rna_function_string(pprop.typef), rna_function_string(pprop.poll));
					if(pprop.type!=null) fprintf(f, "Rna%s.RNA_%s\n", pprop.type.toString(), pprop.type.toString());
					else fprintf(f, "null\n");
					break;
				 }
				case RNATypes.PROP_COLLECTION: {
					CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s, %s, %s, ", rna_function_string(cprop.begin), rna_function_string(cprop.next), rna_function_string(cprop.end), rna_function_string(cprop.get), rna_function_string(cprop.length), rna_function_string(cprop.lookupint), rna_function_string(cprop.lookupstring));
					if(cprop.item_type!=null) fprintf(f, "Rna%s.RNA_%s\n", cprop.item_type.toString(), cprop.item_type.toString());
					else fprintf(f, "null\n");
					break;
				}
		}

		fprintf(f, ");\n\n");

//		if(freenest) {
//			MEM_freeN(strnest);
//			MEM_freeN(errnest);
//		}
	}
	
	static void rna_generate_property_java(PrintStream f, StructRNA srna, String nest, PropertyRNA prop) 
	{
		String strnest= "", errnest= "";
//		int len, freenest= 0;
		
		if(nest != null) {
//			len= nest.length();
//
//			strnest= MEM_mallocN(sizeof(char)*(len+2), "rna_generate_property -> strnest");
//			errnest= MEM_mallocN(sizeof(char)*(len+2), "rna_generate_property -> errnest");

//			strcpy(strnest, "_"); strcat(strnest, nest);
			strnest = "_"; strnest += nest;
//			strcpy(errnest, "."); strcat(errnest, nest);
			errnest = "."; errnest += nest;

//			freenest= 1;
		}

		switch(prop.type) {
				case RNATypes.PROP_ENUM: {
					EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
					int i, defaultfound= 0;

					if(eprop.item!=null) {
//						fprintf(f, "static EnumPropertyItem rna_%s%s_%s_items[%d] = {\n\t", srna.identifier, strnest, prop.identifier, eprop.totitem+1);
//
//						for(i=0; i<eprop.totitem; i++) {
//							fprintf(f, "{%d, ", eprop.item[i].value);
//							rna_print_c_string(f, eprop.item[i].identifier); fprintf(f, ", ");
//							fprintf(f, "%d, ", eprop.item[i].icon);
//							rna_print_c_string(f, eprop.item[i].name); fprintf(f, ", ");
//							rna_print_c_string(f, eprop.item[i].description); fprintf(f, "},\n\t");
//
////							if(eprop.item[i].identifier[0])
//							if(eprop.item[i].identifier!=null && !eprop.item[i].identifier.equals(""))
//								if(eprop.defaultvalue == eprop.item[i].value)
//									defaultfound= 1;
//						}

//						fprintf(f, "{0, NULL, 0, NULL, NULL}\n};\n\n");

//						if(defaultfound==0) {
////							fprintf(stderr, "rna_generate_structs: %s%s.%s, enum default is not in items.\n", srna.identifier, errnest, prop.identifier);
//							System.err.printf("rna_generate_structs: %s%s.%s, enum default is not in items.\n", srna.identifier, errnest, prop.identifier);
//							RnaDefine.DefRNA.error= 1;
//						}
					}
					else {
//						fprintf(stderr, "rna_generate_structs: %s%s.%s, enum must have items defined.\n", srna.identifier, errnest, prop.identifier);
						System.err.printf("rna_generate_structs: %s%s.%s, enum must have items defined.\n", srna.identifier, errnest, prop.identifier);
						RnaDefine.DefRNA.error= 1;
					}
					break;
				}
				case RNATypes.PROP_BOOLEAN: {
					BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static int rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(bprop.defaultarray!=null)
								fprintf(f, "%d", bprop.defaultarray[i]);
							else
								fprintf(f, "%d", bprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				case RNATypes.PROP_INT: {
					IntPropertyRNA iprop= (IntPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static int rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(iprop.defaultarray!=null)
								fprintf(f, "%d", iprop.defaultarray[i]);
							else
								fprintf(f, "%d", iprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				case RNATypes.PROP_FLOAT: {
					FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
					int i;

					if(prop.arraydimension!=0 && prop.totarraylength!=0) {
						fprintf(f, "static float rna_%s%s_%s_default[%d] = {\n\t", srna.identifier, strnest, prop.identifier, prop.totarraylength);

						for(i=0; i<prop.totarraylength; i++) {
							if(fprop.defaultarray!=null)
								rna_float_print(f, fprop.defaultarray[i]);
							else
								rna_float_print(f, fprop.defaultvalue);
							if(i != prop.totarraylength-1)
								fprintf(f, ",\n\t");
						}

						fprintf(f, "\n};\n\n");
					}
					break;
				}
				default:
					break;
		}

		fprintf(f, "%s%s rna_%s%s_%s = new %s(\n", (prop.flag & RNATypes.PROP_EXPORT)!=0? "": "", rna_property_structname(prop.type), srna.identifier, strnest, prop.identifier, rna_property_structname(prop.type));

//		if(prop.next!=null) fprintf(f, "\t{(PropertyRNA*)&rna_%s%s_%s, ", srna.identifier, strnest, ((PropertyRNA)prop.next).identifier);
//		else fprintf(f, "\t{NULL, ");
//		if(prop.prev!=null) fprintf(f, "(PropertyRNA*)&rna_%s%s_%s,\n", srna.identifier, strnest, ((PropertyRNA)prop.prev).identifier);
//		else fprintf(f, "NULL,\n");
		fprintf(f, "\t%d, ", prop.magic);
		rna_print_c_string(f, prop.identifier);
		fprintf(f, ", %d, ", prop.flag);
		rna_print_c_string(f, prop.name); fprintf(f, ",\n\t");
		rna_print_c_string(f, prop.description); fprintf(f, ",\n\t");
		fprintf(f, "%d,\n", prop.icon);
		fprintf(f, "\t%s, %s|%s, %s, %d, new int[]{%d, %d, %d}, %d,\n", rna_property_typename(prop.type), rna_property_subtypename(prop.subtype), rna_property_subtype_unit(prop.subtype), rna_function_string(prop.getlength), prop.arraydimension, prop.arraylength[0], prop.arraylength[1], prop.arraylength[2], prop.totarraylength);
//		fprintf(f, "\t%s%s, %d, %s, %s,\n", (prop.flag & RNATypes.PROP_CONTEXT_UPDATE)!=0? "(UpdateFunc)": "", rna_function_string(prop.update), prop.noteflag, rna_function_string(prop.editable), rna_function_string(prop.itemeditable));
		fprintf(f, "\t%s%s, %d, %s, %s,\n", "", rna_function_string(prop.update), prop.noteflag, rna_function_string(prop.editable), rna_function_string(prop.itemeditable));

		if((prop.flag & RNATypes.PROP_RAW_ACCESS)!=0) rna_set_raw_offset(f, srna, prop);
		else fprintf(f, "\t0, -1");

		/* our own type - collections/arrays only */
		if(prop.srna!=null) fprintf(f, ", &RNA_%s", prop.srna.toString());
		else fprintf(f, ", null");

//		fprintf(f, "},\n");
		fprintf(f, ",\n");

		switch(prop.type&0xFF) {
				case RNATypes.PROP_BOOLEAN: {
					BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
//					fprintf(f, "\t%s, %s, %s, %s, %d, ", rna_function_string(bprop.get), rna_function_string(bprop.set), rna_function_string(bprop.getarray), rna_function_string(bprop.setarray), bprop.defaultvalue?1:0);
					fprintf(f, "\t%s, %s, %s, %s, %s, ", rna_function_string(bprop.get), rna_function_string(bprop.set), rna_function_string(bprop.getarray), rna_function_string(bprop.setarray), bprop.defaultvalue);
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				}
				case RNATypes.PROP_INT: {
					IntPropertyRNA iprop= (IntPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s,\n\t", rna_function_string(iprop.get), rna_function_string(iprop.set), rna_function_string(iprop.getarray), rna_function_string(iprop.setarray), rna_function_string(iprop.range));
					rna_int_print(f, iprop.softmin); fprintf(f, ", ");
					rna_int_print(f, iprop.softmax); fprintf(f, ", ");
					rna_int_print(f, iprop.hardmin); fprintf(f, ", ");
					rna_int_print(f, iprop.hardmax); fprintf(f, ", ");
					rna_int_print(f, iprop.step); fprintf(f, ", ");
					rna_int_print(f, iprop.defaultvalue); fprintf(f, ", ");
//					rna_int_print(f, iprop.defaultvalue); fprintf(f, ";\n");
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				 }
				case RNATypes.PROP_FLOAT: {
					FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s, ", rna_function_string(fprop.get), rna_function_string(fprop.set), rna_function_string(fprop.getarray), rna_function_string(fprop.setarray), rna_function_string(fprop.range));
					rna_float_print(f, fprop.softmin); fprintf(f, ", ");
					rna_float_print(f, fprop.softmax); fprintf(f, ", ");
					rna_float_print(f, fprop.hardmin); fprintf(f, ", ");
					rna_float_print(f, fprop.hardmax); fprintf(f, ", ");
					rna_float_print(f, fprop.step); fprintf(f, ", ");
					rna_int_print(f, (int)fprop.precision); fprintf(f, ", ");
					rna_float_print(f, fprop.defaultvalue); fprintf(f, ", ");
//					rna_float_print(f, fprop.defaultvalue); fprintf(f, ";\n");
					if(prop.arraydimension!=0 && prop.totarraylength!=0) fprintf(f, "rna_%s%s_%s_default\n", srna.identifier, strnest, prop.identifier);
					else fprintf(f, "null\n");
					break;
				 }
				case RNATypes.PROP_STRING: {
					StringPropertyRNA sprop= (StringPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %d, ", rna_function_string(sprop.get), rna_function_string(sprop.length), rna_function_string(sprop.set), sprop.maxlength);
					rna_print_c_string(f, sprop.defaultvalue); fprintf(f, "\n");
//					rna_print_c_string(f, sprop.defaultvalue); fprintf(f, ";\n");
					break;
				}
				case RNATypes.PROP_ENUM: {
					EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, ", rna_function_string(eprop.get), rna_function_string(eprop.set), rna_function_string(eprop.itemf));
					if(eprop.item!=null)
//						fprintf(f, "rna_%s%s_%s_items, ", srna.identifier, strnest, prop.identifier);
						fprintf(f, "%s_items, ", prop.identifier);
					else
						fprintf(f, "null, ");
					fprintf(f, "%d, %d\n", eprop.totitem, eprop.defaultvalue);
//					fprintf(f, "%d;\n", eprop.defaultvalue);
					break;
				}
				case RNATypes.PROP_POINTER: {
					PointerPropertyRNA pprop= (PointerPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s,", rna_function_string(pprop.get), rna_function_string(pprop.set), rna_function_string(pprop.typef), rna_function_string(pprop.poll));
					if(pprop.type!=null) fprintf(f, "RNA_%s\n", pprop.type.toString());
//					if(pprop.type!=null) fprintf(f, "new RNA_%s();\n", pprop.type.toString());
					else fprintf(f, "null\n");
//					else fprintf(f, "null;\n");
					break;
				 }
				case RNATypes.PROP_COLLECTION: {
					CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
					fprintf(f, "\t%s, %s, %s, %s, %s, %s, %s, ", rna_function_string(cprop.begin), rna_function_string(cprop.next), rna_function_string(cprop.end), rna_function_string(cprop.get), rna_function_string(cprop.length), rna_function_string(cprop.lookupint), rna_function_string(cprop.lookupstring));
					if(cprop.item_type!=null) fprintf(f, "RNA_%s\n", cprop.item_type.toString());
//					if(cprop.item_type!=null) fprintf(f, "new RNA_%s[%d]\n", cprop.item_type.toString(), cprop.arraydimension);
					else fprintf(f, "null\n");
//					else fprintf(f, "null\n");
					break;
				}
		}

//		fprintf(f, "};\n\n");
		fprintf(f, ");\n\n");
//		fprintf(f, "\n\n");
		
//		fprintf(f, "%s %s %s = ", ((prop.flag&0xFFFF) & RNATypes.PROP_REQUIRED)!=0 ? "/*required*/" : "", rna_property_structname(prop.type), prop.identifier);

//		if(freenest) {
//			MEM_freeN(strnest);
//			MEM_freeN(errnest);
//		}
	}

	static void rna_generate_struct(BlenderRNA brna, StructRNA srna, PrintStream f)
	{
		FunctionRNA func;
		FunctionDefRNA dfunc;
		PropertyRNA prop, parm;
		StructRNA base;

		fprintf(f, "/* %s */\n", srna.name);

		for(prop= (PropertyRNA)srna.cont.properties.first; prop!=null; prop= prop.next)
			rna_generate_property(f, srna, null, prop);
		// link properties together
		fprintf(f, "static { ListBase tmp = new ListBase();\n");
		for(prop= (PropertyRNA)srna.cont.properties.first; prop!=null; prop= prop.next) {
			fprintf(f, "\tListBaseUtil.BLI_addtail(tmp, rna_%s_%s);\n", srna.identifier, prop.identifier);
		}
		fprintf(f, "}\n\n");

		for(func= (FunctionRNA)srna.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
			for(parm= (PropertyRNA)func.cont.properties.first; parm!=null; parm= parm.next) {
				rna_generate_property(f, srna, func.identifier, parm);
			}
			// link properties together
			fprintf(f, "static { ListBase tmp = new ListBase();\n");
			for(parm= (PropertyRNA)func.cont.properties.first; parm!=null; parm= parm.next) {
				fprintf(f, "\tListBaseUtil.BLI_addtail(tmp, rna_%s_%s_%s);\n", srna.identifier, func.identifier, parm.identifier);
			}
			fprintf(f, "}\n\n");

			fprintf(f, "private static FunctionRNA rna_%s_%s_func = new FunctionRNA(\n", srna.identifier, func.identifier);

//			if(func.cont.next!=null) fprintf(f, "\t{(FunctionRNA*)&rna_%s_%s_func, ", srna.identifier, ((FunctionRNA)func.cont.next).identifier);
//			else
				fprintf(f, "\tnull, ");
//			if(func.cont.prev!=null) fprintf(f, "(FunctionRNA*)&rna_%s_%s_func,\n", srna.identifier, ((FunctionRNA)func.cont.prev).identifier);
//			else
				fprintf(f, "null,\n");

			fprintf(f, "\tnull,\n");

			parm= (PropertyRNA)func.cont.properties.first;
			if(parm!=null) fprintf(f, "\trna_%s_%s_%s, ", srna.identifier, func.identifier, parm.identifier);
			else fprintf(f, "\tnull, ");

			parm= (PropertyRNA)func.cont.properties.last;
			if(parm!=null) fprintf(f, "rna_%s_%s_%s,\n", srna.identifier, func.identifier, parm.identifier);
			else fprintf(f, "null,\n");

			fprintf(f, "\t");
			rna_print_c_string(f, func.identifier);
			fprintf(f, ", %d, ", func.flag);
			rna_print_c_string(f, func.description); fprintf(f, ",\n");

			dfunc= RnaDefine.rna_find_function_def(func);
			if(dfunc.gencall!=null) fprintf(f, "\t%s,\n", dfunc.gencall);
			else fprintf(f, "\tnull,\n");

			if(func.c_ret!=null) fprintf(f, "\trna_%s_%s_%s\n", srna.identifier, func.identifier, func.c_ret.identifier);
			else fprintf(f, "\tnull\n");

			fprintf(f, ");\n");
			fprintf(f, "\n");
		}
		// link functions together
		fprintf(f, "static { ListBase tmp = new ListBase();\n");
		for(func= (FunctionRNA)srna.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
			fprintf(f, "\tListBaseUtil.BLI_addtail(tmp, rna_%s_%s_func);\n", srna.identifier, func.identifier);
		}
		fprintf(f, "}\n\n");

		fprintf(f, "public static StructRNA RNA_%s = new StructRNA(\n", srna.identifier);

		if(srna.cont.next!=null) fprintf(f, "\tRna%s.RNA_%s, ", ((StructRNA)srna.cont.next).identifier, ((StructRNA)srna.cont.next).identifier);
		else
			fprintf(f, "\tnull, ");
		if(srna.cont.prev!=null) fprintf(f, "Rna%s.RNA_%s,\n", ((StructRNA)srna.cont.prev).identifier, ((StructRNA)srna.cont.prev).identifier);
		else
			fprintf(f, "null,\n");

		fprintf(f, "\tnull,\n");

		prop= (PropertyRNA)srna.cont.properties.first;
		if(prop!=null) fprintf(f, "\trna_%s_%s, ", srna.identifier, prop.identifier);
		else fprintf(f, "\tnull, ");

		prop= (PropertyRNA)srna.cont.properties.last;
		if(prop!=null) fprintf(f, "rna_%s_%s,\n", srna.identifier, prop.identifier);
		else fprintf(f, "null,\n");

		fprintf(f, "\tnull,null,\n"); /* PyType - Cant initialize here */
		
		fprintf(f, "\t");
		rna_print_c_string(f, srna.identifier);
		fprintf(f, ", %d, ", srna.flag);
		rna_print_c_string(f, srna.name);
		fprintf(f, ", ");
		rna_print_c_string(f, srna.description);
		fprintf(f, ",\n\t%d,\n", srna.icon);

		prop= srna.nameproperty;
		if(prop!=null) {
			base= srna;
			while (base.base!=null && base.base.nameproperty==prop)
				base= base.base;

			fprintf(f, "\trna_%s_%s, ", base.identifier, prop.identifier);
		}
		else fprintf(f, "\tnull, ");

		prop= srna.iteratorproperty;
		base= srna;
		while (base.base!=null && base.base.iteratorproperty==prop)
			base= base.base;
		fprintf(f, "rna_%s_rna_properties,\n", base.identifier);

		if(srna.base!=null) fprintf(f, "\tRna%s.RNA_%s,\n", srna.base.identifier, srna.base.identifier);
		else fprintf(f, "\tnull,\n");

		if(srna.nested!=null) fprintf(f, "\tRna%s.RNA_%s,\n", srna.nested.identifier, srna.nested.identifier);
		else fprintf(f, "\tnull,\n");

		fprintf(f, "\t%s,\n", rna_function_string(srna.refine));
		fprintf(f, "\t%s,\n", rna_function_string(srna.path));
		fprintf(f, "\t%s,\n", rna_function_string(srna.reg));
		fprintf(f, "\t%s,\n", rna_function_string(srna.unreg));
		fprintf(f, "\t%s,\n", rna_function_string(srna.idproperties));

		if(srna.reg!=null && srna.refine==null) {
//			fprintf(stderr, "rna_generate_struct: %s has a register function, must also have refine function.\n", srna.identifier);
			System.err.printf("rna_generate_struct: %s has a register function, must also have refine function.\n", srna.identifier);
			RnaDefine.DefRNA.error= 1;
		}

		func= (FunctionRNA)srna.functions.first;
		if(func!=null) fprintf(f, "\trna_%s_%s_func, ", srna.identifier, func.identifier);
		else fprintf(f, "\tnull, ");

		func= (FunctionRNA)srna.functions.last;
		if(func!=null) fprintf(f, "rna_%s_%s_func\n", srna.identifier, func.identifier);
		else fprintf(f, "null\n");

		fprintf(f, ");\n");

		fprintf(f, "\n");
	}
	
	static void rna_generate_struct_java(BlenderRNA brna, StructRNA srna, PrintStream f)
	{
		FunctionRNA func;
		FunctionDefRNA dfunc;
		PropertyRNA prop, parm;
		StructRNA base;

		fprintf(f, "/* %s */\n\n", srna.name);

		for(prop= (PropertyRNA)srna.cont.properties.first; prop!=null; prop= (PropertyRNA)prop.next) {
			fprintf(f, "// %s\nprivate ", prop.description);
			rna_generate_property(f, srna, null, prop);
		}

//		for(func= (FunctionRNA)srna.functions.first; func!=null; func= (FunctionRNA)func.cont.next) {
//			
//			fprintf(f, "// %s\n", func.description);
//			if(func.c_ret!=null) fprintf(f, "public %s ", rna_property_structname(func.c_ret.type));
//			else fprintf(f, "public void ");
//			fprintf(f, "%s(PyObject[] values, String[] names) {\n", func.identifier);
//			
//			for(parm= (PropertyRNA)func.cont.properties.first; parm!=null; parm= (PropertyRNA)parm.next) {
//				fprintf(f, "\t// %s\n\t", parm.description);
//				rna_generate_property(f, srna, func.identifier, parm);
//			}
//			
//			fprintf(f, "}\n");
//
////			fprintf(f, "%s%s rna_%s_%s_func = {\n", "", "FunctionRNA", srna.identifier, func.identifier);
////
////			if(func.cont.next!=null) fprintf(f, "\t{(FunctionRNA*)&rna_%s_%s_func, ", srna.identifier, ((FunctionRNA)func.cont.next).identifier);
////			else fprintf(f, "\t{NULL, ");
////			if(func.cont.prev!=null) fprintf(f, "(FunctionRNA*)&rna_%s_%s_func,\n", srna.identifier, ((FunctionRNA)func.cont.prev).identifier);
////			else fprintf(f, "NULL,\n");
////
////			fprintf(f, "\tNULL,\n");
////
////			parm= (PropertyRNA)func.cont.properties.first;
////			if(parm!=null) fprintf(f, "\t{(PropertyRNA*)&rna_%s_%s_%s, ", srna.identifier, func.identifier, parm.identifier);
////			else fprintf(f, "\t{NULL, ");
////
////			parm= (PropertyRNA)func.cont.properties.last;
////			if(parm!=null) fprintf(f, "(PropertyRNA*)&rna_%s_%s_%s}},\n", srna.identifier, func.identifier, parm.identifier);
////			else fprintf(f, "NULL}},\n");
////
////			fprintf(f, "\t");
////			rna_print_c_string(f, func.identifier);
////			fprintf(f, ", %d, ", func.flag);
////			rna_print_c_string(f, func.description); fprintf(f, ",\n");
////
////			dfunc= RnaDefine.rna_find_function_def(func);
////			if(dfunc.gencall!=null) fprintf(f, "\t%s,\n", dfunc.gencall);
////			else fprintf(f, "\tNULL,\n");
////
////			if(func.c_ret!=null) fprintf(f, "\t(PropertyRNA*)&rna_%s_%s_%s\n", srna.identifier, func.identifier, func.c_ret.identifier);
////			else fprintf(f, "\tNULL\n");
////
////			fprintf(f, "};\n");
//			fprintf(f, "\n");
//		}

//		fprintf(f, "StructRNA RNA_%s = {\n", srna->identifier);
//
//		if(srna->cont.next) fprintf(f, "\t{(ContainerRNA *)&RNA_%s, ", ((StructRNA*)srna->cont.next)->identifier);
//		else fprintf(f, "\t{NULL, ");
//		if(srna->cont.prev) fprintf(f, "(ContainerRNA *)&RNA_%s,\n", ((StructRNA*)srna->cont.prev)->identifier);
//		else fprintf(f, "NULL,\n");
//
//		fprintf(f, "\tNULL,\n");
//
//		prop= srna->cont.properties.first;
//		if(prop) fprintf(f, "\t{(PropertyRNA*)&rna_%s_%s, ", srna->identifier, prop->identifier);
//		else fprintf(f, "\t{NULL, ");
//
//		prop= srna->cont.properties.last;
//		if(prop) fprintf(f, "(PropertyRNA*)&rna_%s_%s}},\n", srna->identifier, prop->identifier);
//		else fprintf(f, "NULL}},\n");
//
//		fprintf(f, "\tNULL,NULL,\n"); /* PyType - Cant initialize here */
//		
//		fprintf(f, "\t");
//		rna_print_c_string(f, srna->identifier);
//		fprintf(f, ", %d, ", srna->flag);
//		rna_print_c_string(f, srna->name);
//		fprintf(f, ", ");
//		rna_print_c_string(f, srna->description);
//		fprintf(f, ",\n\t%d,\n", srna->icon);
//
//		prop= srna->nameproperty;
//		if(prop) {
//			base= srna;
//			while (base->base && base->base->nameproperty==prop)
//				base= base->base;
//
//			fprintf(f, "\t(PropertyRNA*)&rna_%s_%s, ", base->identifier, prop->identifier);
//		}
//		else fprintf(f, "\tNULL, ");
//
//		prop= srna->iteratorproperty;
//		base= srna;
//		while (base->base && base->base->iteratorproperty==prop)
//			base= base->base;
//		fprintf(f, "(PropertyRNA*)&rna_%s_rna_properties,\n", base->identifier);
//
//		if(srna->base) fprintf(f, "\t&RNA_%s,\n", srna->base->identifier);
//		else fprintf(f, "\tNULL,\n");
//
//		if(srna->nested) fprintf(f, "\t&RNA_%s,\n", srna->nested->identifier);
//		else fprintf(f, "\tNULL,\n");
//
//		fprintf(f, "\t%s,\n", rna_function_string(srna->refine));
//		fprintf(f, "\t%s,\n", rna_function_string(srna->path));
//		fprintf(f, "\t%s,\n", rna_function_string(srna->reg));
//		fprintf(f, "\t%s,\n", rna_function_string(srna->unreg));
//		fprintf(f, "\t%s,\n", rna_function_string(srna->idproperties));
//
//		if(srna->reg && !srna->refine) {
//			fprintf(stderr, "rna_generate_struct: %s has a register function, must also have refine function.\n", srna->identifier);
//			DefRNA.error= 1;
//		}
//
//		func= srna->functions.first;
//		if(func) fprintf(f, "\t{(FunctionRNA*)&rna_%s_%s_func, ", srna->identifier, func->identifier);
//		else fprintf(f, "\t{NULL, ");
//
//		func= srna->functions.last;
//		if(func) fprintf(f, "(FunctionRNA*)&rna_%s_%s_func}\n", srna->identifier, func->identifier);
//		else fprintf(f, "NULL}\n");
//
//		fprintf(f, "};\n");
//
//		fprintf(f, "\n");
	}
	
	public static interface RNAProcess {
		public void run(BlenderRNA brna);
	}

	public static class RNAProcessItem {
		public String filename;
		public String api_filename;
		public RNAProcess define;
		public RNAProcessItem(String filename, String api_filename, RNAProcess define) {
			this.filename = filename;
			this.api_filename = api_filename;
			this.define = define;
		}
	};

	public static RNAProcessItem[] PROCESS_ITEMS= {
//		new RNAProcessItem("rna_rna.c", null, RNA_def_rna),
		new RNAProcessItem("RnaIDUtil", null, RnaIDUtil.RNA_def_ID),
//		new RNAProcessItem("rna_texture.c", null, RNA_def_texture),
//		new RNAProcessItem("rna_action.c", "rna_action_api.c", RNA_def_action),
//		new RNAProcessItem("rna_animation.c", "rna_animation_api.c", RNA_def_animation),
//		new RNAProcessItem("rna_animviz.c", null, RNA_def_animviz),
//		new RNAProcessItem("rna_actuator.c", "rna_actuator_api.c", RNA_def_actuator),
//		new RNAProcessItem("rna_armature.c", "rna_armature_api.c", RNA_def_armature),
//		new RNAProcessItem("rna_boid.c", null, RNA_def_boid),
//		new RNAProcessItem("rna_brush.c", null, RNA_def_brush),
//		new RNAProcessItem("rna_camera.c", null, RNA_def_camera),
//		new RNAProcessItem("rna_cloth.c", null, RNA_def_cloth),
//		new RNAProcessItem("rna_color.c", null, RNA_def_color),
//		new RNAProcessItem("rna_constraint.c", null, RNA_def_constraint),
		new RNAProcessItem("RnaContextUtil", null, RnaContextUtil.RNA_def_context),
//		new RNAProcessItem("rna_controller.c", "rna_controller_api.c", RNA_def_controller),
//		new RNAProcessItem("rna_curve.c", null, RNA_def_curve),
//		new RNAProcessItem("rna_fcurve.c", "rna_fcurve_api.c", RNA_def_fcurve),
//		new RNAProcessItem("rna_fluidsim.c", null, RNA_def_fluidsim),
//		new RNAProcessItem("rna_gpencil.c", null, RNA_def_gpencil),
//		new RNAProcessItem("rna_group.c", null, RNA_def_group),
//		new RNAProcessItem("rna_image.c", "rna_image_api.c", RNA_def_image),
//		new RNAProcessItem("rna_key.c", null, RNA_def_key),
//		new RNAProcessItem("rna_lamp.c", null, RNA_def_lamp),
//		new RNAProcessItem("rna_lattice.c", null, RNA_def_lattice),
		new RNAProcessItem("RnaMainUtil", "RnaMainApi", RnaMainUtil.RNA_def_main),
//		new RNAProcessItem("rna_material.c", "rna_material_api.c", RNA_def_material),
//		new RNAProcessItem("rna_mesh.c", "rna_mesh_api.c", RNA_def_mesh),
//		new RNAProcessItem("rna_meta.c", null, RNA_def_meta),
//		new RNAProcessItem("rna_modifier.c", null, RNA_def_modifier),
//		new RNAProcessItem("rna_nla.c", null, RNA_def_nla),
//		new RNAProcessItem("rna_nodetree.c", null, RNA_def_nodetree),
		new RNAProcessItem("RnaObjectUtil", "RnaObjectApi", RnaObjectUtil.RNA_def_object),
//		new RNAProcessItem("rna_object_force.c", null, RNA_def_object_force),
//		new RNAProcessItem("rna_packedfile.c", null, RNA_def_packedfile),
//		new RNAProcessItem("rna_particle.c", null, RNA_def_particle),
//		new RNAProcessItem("rna_pose.c", "rna_pose_api.c", RNA_def_pose),
//		new RNAProcessItem("rna_property.c", null, RNA_def_gameproperty),
//		new RNAProcessItem("rna_render.c", null, RNA_def_render),
		new RNAProcessItem("RnaSceneUtil", "RnaSceneApi", RnaSceneUtil.RNA_def_scene),
//			new RNAProcessItem("rna_tool_settings.c", "rna_scene_api.c", RnaSceneUtil.RNA_def_tool_settings),
		new RNAProcessItem("RnaScreenUtil", null, RnaScreenUtil.RNA_def_screen),
//			new RNAProcessItem("rna_area.c", null, RnaScreenUtil.RNA_def_area),
//			new RNAProcessItem("rna_region.c", null, RnaScreenUtil.RNA_def_region),
//		new RNAProcessItem("rna_sculpt_paint.c", NULL, RNA_def_sculpt_paint),
//		new RNAProcessItem("rna_sensor.c", "rna_sensor_api.c", RNA_def_sensor),
//		new RNAProcessItem("rna_sequencer.c", "rna_sequencer_api.c", RNA_def_sequencer),
//		new RNAProcessItem("rna_smoke.c", null, RNA_def_smoke),
		new RNAProcessItem("RnaSpaceUtil", null, RnaSpaceUtil.RNA_def_space),
//			new RNAProcessItem("rna_space_outliner.c", null, RnaSpaceUtil.RNA_def_space_outliner),
//			new RNAProcessItem("rna_space_view3d.c", null, RnaSpaceUtil.RNA_def_space_view3d),
//			new RNAProcessItem("rna_space_buttons.c", null, RnaSpaceUtil.RNA_def_space_buttons),
//			new RNAProcessItem("rna_space_time.c", null, RnaSpaceUtil.RNA_def_space_time),
//			new RNAProcessItem("rna_space_info.c", null, RnaSpaceUtil.RNA_def_space_info),
//		new RNAProcessItem("rna_test.c", null, RNA_def_test),
//		new RNAProcessItem("rna_text.c", null, RNA_def_text),
//		new RNAProcessItem("rna_timeline.c", null, RNA_def_timeline_marker),
//		new RNAProcessItem("rna_sound.c", null, RNA_def_sound),
		new RNAProcessItem("RnaUI", "RnaUIApi", RnaUI.RNA_def_ui),
//			new RNAProcessItem("rna_panel.c", "rna_ui_api.c", RnaUI.RNA_def_panel),
//			new RNAProcessItem("rna_header.c", "rna_ui_api.c", RnaUI.RNA_def_header),
//			new RNAProcessItem("rna_menu.c", "rna_ui_api.c", RnaUI.RNA_def_menu),
//		new RNAProcessItem("rna_userdef.c", null, RNA_def_userdef),
//		new RNAProcessItem("rna_vfont.c", null, RNA_def_vfont),
		new RNAProcessItem("RnaWm", "RnaWmApi", RnaWm.RNA_def_wm),
//			new RNAProcessItem("rna_window.c", "rna_wm_api.c", RnaWm.RNA_def_window),
//		new RNAProcessItem("rna_world.c", null, RNA_def_world),	
		new RNAProcessItem(null, null, null)};

	static void rna_generate(BlenderRNA brna, StructDefRNA ds, PrintStream f, String filename, String api_filename)
	{
//		StructDefRNA ds;
		PropertyDefRNA dp;
		FunctionDefRNA dfunc;
		
		fprintf(f, "\n/* Automatically generated struct definitions for the Data API.\n"+
					 "   Do not edit manually, changes will be overwritten.           */\n\n");//+
					  //"#define RNA_RUNTIME\n\n");
		fprintf(f, "package blender.makesrna.srna;\n\n");

//		fprintf(f, "#include <float.h>\n");
//		fprintf(f, "#include <limits.h>\n");
//		fprintf(f, "#include <string.h>\n\n");
//		fprintf(f, "#include <stddef.h>\n\n");
//
//		fprintf(f, "#include \"DNA_ID.h\"\n");
//		fprintf(f, "#include \"DNA_scene_types.h\"\n");
//
//		fprintf(f, "#include \"BLI_blenlib.h\"\n\n");
//
//		fprintf(f, "#include \"BKE_context.h\"\n");
//		fprintf(f, "#include \"BKE_library.h\"\n");
//		fprintf(f, "#include \"BKE_main.h\"\n");
//		fprintf(f, "#include \"BKE_report.h\"\n");
//		fprintf(f, "#include \"BKE_utildefines.h\"\n\n");

		fprintf(f, "import blender.makesdna.sdna.*;\n\n");
		
		fprintf(f, "import static blender.makesrna.RnaRna.*;\n");
		fprintf(f, "import static blender.makesrna.RnaAccess.*;\n");
		fprintf(f, "import static blender.makesrna.RnaDefine.*;\n");
		fprintf(f, "import static blender.makesrna.RNATypes.*;\n");
		fprintf(f, "import blender.makesrna.rna_internal_types.*;\n");
		fprintf(f, "import blender.blenlib.*;\n");
		fprintf(f, "import blender.blenkernel.*;\n\n");
		
//		rna_generate_prototypes(brna, f);

		fprintf(f, "import static blender.makesrna.%s.*;\n", filename);
		if(api_filename!=null)
			fprintf(f, "import static blender.makesrna.%s.*;\n", api_filename);
		fprintf(f, "\n");
		
//		fprintf(f, "public class Rna%s extends RNA_Struct {\n\n", filename);
		fprintf(f, "public class Rna%s extends RnaStruct {\n\n", ds.srna.identifier);
		
		fprintf(f, "/* Autogenerated Functions */\n\n");
		
		fprintf(f, "public Rna%s(PointerRNA ptr) { super(ptr); }\n\n", ds.srna.identifier);

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next) {
//			if(filename==null || ds.filename == filename) {
//				rna_generate_property_prototypes(brna, ds.srna, f);
//				rna_generate_function_prototypes(brna, ds.srna, f);
//			}
//		}

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
//			if(filename==null || ds.filename == filename)
				for(dp=ds.cont.properties.first; dp!=null; dp=dp.next)
					rna_def_property_funcs(f, ds.srna, dp);

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next) {
//			if(filename==null || ds.filename == filename) {
				for(dfunc=ds.functions.first; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.next)
					rna_def_function_funcs(f, ds, dfunc);

//				rna_generate_static_function_prototypes(brna, ds.srna, f);
//			}
//		}

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
//			if(filename==null || ds.filename == filename)
				rna_generate_struct(brna, ds.srna, f);

//		if(strcmp(filename, "rna_ID.c") == 0) {
//			/* this is ugly, but we cannot have c files compiled for both
//			 * makesrna and blender with some build systems at the moment */
//			fprintf(f, "#include \"rna_define.c\"\n\n");
//
//			rna_generate_blender(brna, f);
//		}
		
		fprintf(f, "}\n");
	}
	
	static void rna_generate_java(BlenderRNA brna, PrintStream f, String filename, String api_filename)
	{
		StructDefRNA ds;
		PropertyDefRNA dp;
		FunctionDefRNA dfunc;
		
		fprintf(f, "\n/* Automatically generated struct definitions for the Data API.\n"+
					 "   Do not edit manually, changes will be overwritten.           */\n\n"+
					  "//#define RNA_RUNTIME\n\n");

//		fprintf(f, "//#include <float.h>\n");
//		fprintf(f, "//#include <limits.h>\n");
//		fprintf(f, "//#include <string.h>\n\n");
//		fprintf(f, "//#include <stddef.h>\n\n");
//
//		fprintf(f, "//#include \"DNA_ID.h\"\n");
//		fprintf(f, "//#include \"DNA_scene_types.h\"\n");
//
//		fprintf(f, "//#include \"BLI_blenlib.h\"\n\n");
//
//		fprintf(f, "//#include \"BKE_context.h\"\n");
//		fprintf(f, "//#include \"BKE_library.h\"\n");
//		fprintf(f, "//#include \"BKE_main.h\"\n");
//		fprintf(f, "//#include \"BKE_report.h\"\n");
//		fprintf(f, "//#include \"BKE_utildefines.h\"\n\n");
//
//		fprintf(f, "//#include \"RNA_define.h\"\n");
//		fprintf(f, "//#include \"RNA_types.h\"\n");
//		fprintf(f, "//#include \"rna_internal.h\"\n\n");

//		rna_generate_prototypes(brna, f);

//		fprintf(f, "//#include \"%s\"\n", filename);
//		if(api_filename!=null)
//			fprintf(f, "//#include \"%s\"\n", api_filename);
//		fprintf(f, "\n");

		fprintf(f, "/* Autogenerated Functions */\n\n");

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next) {
//			if(filename==null || ds.filename == filename) {
//				rna_generate_property_prototypes(brna, ds.srna, f);
//				rna_generate_function_prototypes(brna, ds.srna, f);
//			}
//		}

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
//			if(filename==null || ds.filename == filename)
//				for(dp=ds.cont.properties.first; dp!=null; dp=dp.next)
//					rna_def_property_funcs(f, ds.srna, dp);

//		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next) {
//			if(filename==null || ds.filename == filename) {
//				for(dfunc=ds.functions.first; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.next)
//					rna_def_function_funcs(f, ds, dfunc);
//
//				rna_generate_static_function_prototypes(brna, ds.srna, f);
//			}
//		}

		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
			if(filename==null || ds.filename == filename)
				rna_generate_struct_java(brna, ds.srna, f);

//		if(strcmp(filename, "rna_ID.c") == 0) {
//			/* this is ugly, but we cannot have c files compiled for both
//			 * makesrna and blender with some build systems at the moment */
//			fprintf(f, "#include \"rna_define.c\"\n\n");
//
//			rna_generate_blender(brna, f);
//		}
	}

//	static void rna_generate_header(BlenderRNA *brna, FILE *f)
//	{
//		StructDefRNA *ds;
//		PropertyDefRNA *dp;
//		StructRNA *srna;
//
//		fprintf(f, "\n#ifndef __RNA_BLENDER_H__\n");
//		fprintf(f, "#define __RNA_BLENDER_H__\n\n");
//
//		fprintf(f, "/* Automatically generated function declarations for the Data API.\n"
//					 "   Do not edit manually, changes will be overwritten.              */\n\n");
//
//		fprintf(f, "#include \"RNA_types.h\"\n\n");
//
//		fprintf(f, "#ifdef __cplusplus\nextern \"C\" {\n#endif\n\n");
//
//		fprintf(f, "#define FOREACH_BEGIN(property, sptr, itemptr) \\\n");
//		fprintf(f, "	{ \\\n");
//		fprintf(f, "		CollectionPropertyIterator rna_macro_iter; \\\n");
//		fprintf(f, "		for(property##_begin(&rna_macro_iter, sptr); rna_macro_iter.valid; property##_next(&rna_macro_iter)) { \\\n");
//		fprintf(f, "			itemptr= rna_macro_iter.ptr;\n\n");
//
//		fprintf(f, "#define FOREACH_END(property) \\\n");
//		fprintf(f, "		} \\\n");
//		fprintf(f, "		property##_end(&rna_macro_iter); \\\n");
//		fprintf(f, "	}\n\n");
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next) {
//			srna= ds->srna;
//
//			fprintf(f, "/**************** %s ****************/\n\n", srna->name);
//
//			while(srna) {
//				fprintf(f, "extern StructRNA RNA_%s;\n", srna->identifier);
//				srna= srna->base;
//			}
//			fprintf(f, "\n");
//
//			for(dp=ds->cont.properties.first; dp; dp=dp->next)
//				rna_def_property_funcs_header(f, ds->srna, dp);
//		}
//
//		fprintf(f, "#ifdef __cplusplus\n}\n#endif\n\n");
//
//		fprintf(f, "#endif /* __RNA_BLENDER_H__ */\n\n");
//	}
//
//	static const char *cpp_classes = ""
//	"\n"
//	"#include <string>\n"
//	"\n"
//	"namespace RNA {\n"
//	"\n"
//	"#define BOOLEAN_PROPERTY(sname, identifier) \\\n"
//	"	bool sname::identifier(void) { return (bool)sname##_##identifier##_get(&ptr); }\n"
//	"\n"
//	"#define BOOLEAN_ARRAY_PROPERTY(sname, size, identifier) \\\n"
//	"	Array<int,size> sname::identifier(void) \\\n"
//	"		{ Array<int, size> ar; sname##_##identifier##_get(&ptr, ar.data); return ar; }\n"
//	"\n"
//	"#define INT_PROPERTY(sname, identifier) \\\n"
//	"	int sname::identifier(void) { return sname##_##identifier##_get(&ptr); }\n"
//	"\n"
//	"#define INT_ARRAY_PROPERTY(sname, size, identifier) \\\n"
//	"	Array<int,size> sname::identifier(void) \\\n"
//	"		{ Array<int, size> ar; sname##_##identifier##_get(&ptr, ar.data); return ar; }\n"
//	"\n"
//	"#define FLOAT_PROPERTY(sname, identifier) \\\n"
//	"	float sname::identifier(void) { return sname##_##identifier##_get(&ptr); }\n"
//	"\n"
//	"#define FLOAT_ARRAY_PROPERTY(sname, size, identifier) \\\n"
//	"	Array<float,size> sname::identifier(void) \\\n"
//	"		{ Array<float, size> ar; sname##_##identifier##_get(&ptr, ar.data); return ar; }\n"
//	"\n"
//	"#define ENUM_PROPERTY(type, sname, identifier) \\\n"
//	"	sname::type sname::identifier(void) { return (type)sname##_##identifier##_get(&ptr); }\n"
//	"\n"
//	"#define STRING_PROPERTY(sname, identifier) \\\n"
//	"	std::string sname::identifier(void) { \\\n"
//	"		int len= sname##_##identifier##_length(&ptr); \\\n"
//	"		std::string str; str.resize(len); \\\n"
//	"		sname##_##identifier##_get(&ptr, &str[0]); return str; } \\\n"
//	"\n"
//	"#define POINTER_PROPERTY(type, sname, identifier) \\\n"
//	"	type sname::identifier(void) { return type(sname##_##identifier##_get(&ptr)); }\n"
//	"\n"
//	"#define COLLECTION_PROPERTY(type, sname, identifier) \\\n"
//	"	typedef CollectionIterator<type, sname##_##identifier##_begin, \\\n"
//	"		sname##_##identifier##_next, sname##_##identifier##_end> identifier##_iterator; \\\n"
//	"	Collection<sname, type, sname##_##identifier##_begin, \\\n"
//	"		sname##_##identifier##_next, sname##_##identifier##_end> identifier;\n"
//	"\n"
//	"class Pointer {\n"
//	"public:\n"
//	"	Pointer(const PointerRNA& p) : ptr(p) { }\n"
//	"	operator const PointerRNA&() { return ptr; }\n"
//	"	bool is_a(StructRNA *type) { return RNA_struct_is_a(&ptr, type); }\n"
//	"	operator void*() { return ptr.data; }\n"
//	"	operator bool() { return ptr.data != NULL; }\n"
//	"\n"
//	"	PointerRNA ptr;\n"
//	"};\n"
//	"\n"
//	"\n"
//	"template<typename T, int Tsize>\n"
//	"class Array {\n"
//	"public:\n"
//	"	T data[Tsize];\n"
//	"	operator T*() { return data; }\n"
//	"};\n"
//	"\n"
//	"typedef void (*TBeginFunc)(CollectionPropertyIterator *iter, PointerRNA *ptr);\n"
//	"typedef void (*TNextFunc)(CollectionPropertyIterator *iter);\n"
//	"typedef void (*TEndFunc)(CollectionPropertyIterator *iter);\n"
//	"\n"
//	"template<typename T, TBeginFunc Tbegin, TNextFunc Tnext, TEndFunc Tend>\n"
//	"class CollectionIterator {\n"
//	"public:\n"
//	"	CollectionIterator() : t(iter.ptr), init(false) { iter.valid= false; }\n"
//	"	~CollectionIterator(void) { if(init) Tend(&iter); };\n"
//	"	const CollectionIterator<T, Tbegin, Tnext, Tend>& operator=(const CollectionIterator<T, Tbegin, Tnext, Tend>& copy)\n"
//	"	{ if(init) Tend(&iter); iter= copy.iter; if(iter.internal) iter.internal= MEM_dupallocN(iter.internal); t= copy.t; init= copy.init; return *this; }\n"
//	"\n"
//	"	operator bool(void)\n"
//	"	{ return iter.valid != 0; }\n"
//	"	const CollectionIterator<T, Tbegin, Tnext, Tend>& operator++() { Tnext(&iter); t = T(iter.ptr); return *this; }\n"
//	"	T& operator*(void) { return t; }\n"
//	"	T* operator->(void) { return &t; }\n"
//	"	bool operator==(const CollectionIterator<T, Tbegin, Tnext, Tend>& other) { return iter.valid == other.iter.valid; }\n"
//	"	bool operator!=(const CollectionIterator<T, Tbegin, Tnext, Tend>& other) { return iter.valid != other.iter.valid; }\n"
//	"\n"
//	"	void begin(const Pointer& ptr)\n"
//	"	{ if(init) Tend(&iter); Tbegin(&iter, (PointerRNA*)&ptr.ptr); t = T(iter.ptr); init = true; }\n"
//	"\n"
//	"private:\n"
//	"	CollectionPropertyIterator iter;\n"
//	"	T t;\n"
//	"	bool init;\n"
//	"};\n"
//	"\n"
//	"template<typename Tp, typename T, TBeginFunc Tbegin, TNextFunc Tnext, TEndFunc Tend>\n"
//	"class Collection {\n"
//	"public:\n"
//	"	Collection(const PointerRNA& p) : ptr(p) {}\n"
//	"\n"
//	"	CollectionIterator<T, Tbegin, Tnext, Tend> begin()\n"
//	"	{ CollectionIterator<T, Tbegin, Tnext, Tend> iter; iter.begin(ptr); return iter; }\n"
//	"	CollectionIterator<T, Tbegin, Tnext, Tend> end()\n"
//	"	{ return CollectionIterator<T, Tbegin, Tnext, Tend>(); } /* test */ \n"
//	"\n"
//	"private:\n"
//	"	PointerRNA ptr;\n"
//	"};\n"
//	"\n";
//
//	static void rna_generate_header_cpp(BlenderRNA *brna, FILE *f)
//	{
//		StructDefRNA *ds;
//		PropertyDefRNA *dp;
//		StructRNA *srna;
//
//		fprintf(f, "\n#ifndef __RNA_BLENDER_CPP_H__\n");
//		fprintf(f, "#define __RNA_BLENDER_CPP_H__\n\n");
//
//		fprintf(f, "/* Automatically generated classes for the Data API.\n"
//					 "   Do not edit manually, changes will be overwritten. */\n\n");
//
//		fprintf(f, "#include \"RNA_blender.h\"\n");
//		fprintf(f, "#include \"RNA_types.h\"\n");
//
//		fprintf(f, "%s", cpp_classes);
//
//		fprintf(f, "/**************** Declarations ****************/\n\n");
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next)
//			fprintf(f, "class %s;\n", ds->srna->identifier);
//		fprintf(f, "\n");
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next) {
//			srna= ds->srna;
//
//			fprintf(f, "/**************** %s ****************/\n\n", srna->name);
//
//			fprintf(f, "class %s : public %s {\n", srna->identifier, (srna->base)? srna->base->identifier: "Pointer");
//			fprintf(f, "public:\n");
//			fprintf(f, "\t%s(const PointerRNA& ptr) :\n\t\t%s(ptr)", srna->identifier, (srna->base)? srna->base->identifier: "Pointer");
//			for(dp=ds->cont.properties.first; dp; dp=dp->next)
//				if(!(dp->prop->flag & (PROP_IDPROPERTY|PROP_BUILTIN)))
//					if(dp->prop->type == PROP_COLLECTION)
//						fprintf(f, ",\n\t\t%s(ptr)", dp->prop->identifier);
//			fprintf(f, "\n\t\t{}\n\n");
//
//			for(dp=ds->cont.properties.first; dp; dp=dp->next)
//				rna_def_property_funcs_header_cpp(f, ds->srna, dp);
//			fprintf(f, "};\n\n");
//		}
//
//
//		fprintf(f, "/**************** Implementation ****************/\n");
//
//		for(ds=DefRNA.structs.first; ds; ds=ds->cont.next) {
//			for(dp=ds->cont.properties.first; dp; dp=dp->next)
//				rna_def_property_funcs_impl_cpp(f, ds->srna, dp);
//
//			fprintf(f, "\n");
//		}
//
//		fprintf(f, "}\n\n#endif /* __RNA_BLENDER_CPP_H__ */\n\n");
//	}
//
//	static void make_bad_file(char *file, int line)
//	{
//		FILE *fp= fopen(file, "w");
//		fprintf(fp, "#error \"Error! can't make correct RNA file from %s:%d, STUPID!\"\n", __FILE__, line);
//		fclose(fp);
//	}

	static boolean rna_preprocess(String outfile)
	{
		BlenderRNA brna;
		StructDefRNA ds;
		PrintStream file;
//		char deffile[4096];
		String deffile;
		int i;
		boolean status = false;

		/* define rna */
		brna= RnaDefine.RNA_create();

		for(i=0; PROCESS_ITEMS[i].filename!=null; i++) {
			if(PROCESS_ITEMS[i].define!=null) {
				PROCESS_ITEMS[i].define.run(brna);

				for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
					if(ds.filename==null) {
						ds.filename= PROCESS_ITEMS[i].filename;
						if (PROCESS_ITEMS[i].api_filename!=null)
							ds.filename += ":"+PROCESS_ITEMS[i].api_filename;
					}
			}
		}

//		rna_auto_types();
//
//
//		/* create RNA_blender_cpp.h */
//		strcpy(deffile, outfile);
//		strcat(deffile, "RNA_blender_cpp.h" TMP_EXT);
//
//		status= (DefRNA.error != 0);
//
//		if(status) {
//			make_bad_file(deffile, __LINE__);
//		}
//		else {
//			file = fopen(deffile, "w");
//
//			if(!file) {
//				printf ("Unable to open file: %s\n", deffile);
//				status = 1;
//			}
//			else {
//				rna_generate_header_cpp(brna, file);
//				fclose(file);
//				status= (DefRNA.error != 0);
//			}
//		}
//
//		replace_if_different(deffile);
//
//		rna_sort(brna);

		/* create rna_gen_*.c files */
//		for(i=0; PROCESS_ITEMS[i].filename!=null; i++) {
////			strcpy(deffile, outfile);
//			deffile = outfile;
////			strcat(deffile, PROCESS_ITEMS[i].filename);
//			deffile += PROCESS_ITEMS[i].filename;
////			deffile[strlen(deffile)-2] = '\0';
//			deffile = deffile.substring(0, deffile.length()-2);
////			strcat(deffile, "_gen.c" TMP_EXT);
//			deffile += "_gen.c";
////
////			if(status) {
////				make_bad_file(deffile, __LINE__);
////			}
////			else {
//				try {
////					file = fopen(deffile, "w");
//					file = new PrintStream(deffile);
//					rna_generate(brna, file, PROCESS_ITEMS[i].filename, PROCESS_ITEMS[i].api_filename);
//					file.close();
////					System.out.println(file.toString());
////					System.out.println("// END OF FILE");
//					status= (RnaDefine.DefRNA.error != 0);
//				} catch (FileNotFoundException ex) {
////					printf ("Unable to open file: %s\n", deffile);
//					System.out.printf("Unable to open file: %s\n", deffile);
//					status = true;
//				}
////			}
////
////			replace_if_different(deffile);
//		}
		
		// TMP
		for(ds=RnaDefine.DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next) {
			deffile = outfile;
			deffile += ("Rna"+ds.srna.identifier+".java");
//			deffile = deffile.substring(0, deffile.length()-2);
//			deffile += "_gen.c";
//			String apifilename = ds.filename;
			String[] filenames = ds.filename.split(":");
//			ds.filename = ds.srna.identifier;
			try {
//				file = fopen(deffile, "w");
				file = new PrintStream(deffile);
				rna_generate(brna, ds, file, filenames[0], filenames.length>1?filenames[1]:null);
				file.close();
//				System.out.println(file.toString());
//				System.out.println("// END OF FILE");
				status= (RnaDefine.DefRNA.error != 0);
			} catch (FileNotFoundException ex) {
//				printf ("Unable to open file: %s\n", deffile);
				System.out.printf("Unable to open file: %s\n", deffile);
				status = true;
			}
		}

//		/* create RNA_blender.h */
//		strcpy(deffile, outfile);
//		strcat(deffile, "RNA_blender.h" TMP_EXT);
//
//		if(status) {
//			make_bad_file(deffile, __LINE__);
//		}
//		else {
//			file = fopen(deffile, "w");
//
//			if(!file) {
//				printf ("Unable to open file: %s\n", deffile);
//				status = 1;
//			}
//			else {
//				rna_generate_header(brna, file);
//				fclose(file);
//				status= (DefRNA.error != 0);
//			}
//		}
//
//		replace_if_different(deffile);
//
//		/* free RNA */
//		RNA_define_free(brna);
//		RNA_free(brna);

		return status;
	}

//	static void mem_error_cb(const char *errorStr)
//	{
//		fprintf(stderr, "%s", errorStr);
//		fflush(stderr);
//	}
//
//	int main(int argc, char **argv)
	public static void main(String[] args)
	{
		int totblock;
		boolean return_status = false;

		if (args.length < 1) {
    		System.out.println("Usage: Makesrna <outputpath>");
    		return;
    	}
		
		System.out.printf("Running makesrna, program versions %s\n",  RNA_VERSION_DATE);
		return_status= rna_preprocess(args[0]);
		//return_status= rna_preprocess("C:/Users/jladere/Desktop/rna/");

//		totblock= MEM_get_memory_blocks_in_use();
//		if(totblock!=0) {
//			printf("Error Totblock: %d\n",totblock);
//			MEM_set_error_callback(mem_error_cb);
//			MEM_printmemlist();
//		}
			
		System.out.println("Finished.");

//		return return_status;
	}
	
	private static void fprintf(PrintStream f, String format, Object... args) {
		f.print(String.format(format, args));
	}
	
	private static String makeString(Object obj) {
		return (obj != null) ? obj.toString() : null;
	}

}
