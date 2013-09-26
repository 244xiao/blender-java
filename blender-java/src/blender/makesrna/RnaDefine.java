/**
 * $Id: RnaDefine.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation (2008).
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.makesrna;

//#include <float.h>

import java.util.Arrays;

import blender.editors.uinterface.Resources.BIFIconID;
import blender.makesdna.DNAGenfile;
import blender.makesdna.SDNATypes.SDNA;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesrna.Makesrna.FString;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.StructUnregisterFunc;
import blender.makesrna.rna_internal_types.BlenderDefRNA;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.ContainerDefRNA;
import blender.makesrna.rna_internal_types.ContainerRNA;
import blender.makesrna.rna_internal_types.EditableFunc;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FloatPropertyRNA;
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

//#include <limits.h>
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#include <ctype.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_genfile.h"
//#include "DNA_sdna_types.h"
//
//#include "RNA_access.h"
//#include "RNA_define.h"
//#include "RNA_types.h"
//
//#include "BLI_ghash.h"
//#include "BLI_string.h"
//
//#include "rna_internal.h"

public class RnaDefine {
	
	public static final float FLT_MAX = Float.MAX_VALUE;
	public static final int INT_MIN = Integer.MIN_VALUE;
	public static final int INT_MAX = Integer.MAX_VALUE;
	public static final int CHAR_MIN = Byte.MIN_VALUE;
	public static final int CHAR_MAX = Byte.MAX_VALUE;
	public static final int SHRT_MIN = Short.MIN_VALUE;
	public static final int SHRT_MAX = Short.MAX_VALUE;

/* Global used during defining */

public static BlenderDefRNA DefRNA = new BlenderDefRNA(null, new ListBase(), new ListBase(), null, 0, 0, 0, 1);

///* Duplicated code since we can't link in blenkernel or blenlib */
//
//#ifndef MIN2
public static double MIN2(double x, double y) { return ((x)<(y)? (x): (y)); }
public static double MAX2(double x, double y) { return ((x)>(y)? (x): (y)); }
//#endif

public static <T> T castMe(Integer value, T type) {
	if (type instanceof Short) return (T)(Object)value.shortValue();
	return (T)value;
}

public static void rna_addtail(ListBase listbase, Link vlink)
{
	Link link= vlink;

	link.next = null;
	link.prev = listbase.last;
	
//	if (link instanceof FunctionRNA) System.out.println("rna_addtail listbase.last 1: "+listbase.last);

	if (listbase.last!=null) ((Link)listbase.last).next = link;
	if (listbase.first == null) listbase.first = link;
	listbase.last = link;
	
//	if (link instanceof FunctionRNA) System.out.println("rna_addtail listbase.last 2: "+listbase.last);
//	if (link instanceof FunctionRNA) System.out.println("rna_addtail link.prev: "+vlink.prev);
}

//void rna_remlink(ListBase *listbase, void *vlink)
//{
//	Link *link= vlink;
//
//	if (link.next) link.next.prev = link.prev;
//	if (link.prev) link.prev.next = link.next;
//
//	if (listbase.last == link) listbase.last = link.prev;
//	if (listbase.first == link) listbase.first = link.next;
//}
//
//void rna_freelinkN(ListBase *listbase, void *vlink)
//{
//	rna_remlink(listbase, vlink);
//	MEM_freeN(vlink);
//}
//
//void rna_freelistN(ListBase *listbase)
//{
//	Link *link, *next;
//
//	for(link=listbase.first; link; link=next) {
//		next= link.next;
//		MEM_freeN(link);
//	}
//
//	listbase.first= listbase.last= NULL;
//}

public static StructDefRNA rna_find_struct_def(StructRNA srna)
{
	StructDefRNA dsrna;

	if(DefRNA.preprocess==0) {
		/* we should never get here */
//		fprintf(stderr, "rna_find_struct_def: only at preprocess time.\n");
		System.err.printf("rna_find_struct_def: only at preprocess time.\n");
		return null;
	}

	dsrna= DefRNA.structs.last;
	for (; dsrna!=null; dsrna= (StructDefRNA)dsrna.cont.prev)
		if (dsrna.srna==srna)
			return dsrna;

//	System.out.println("rna_find_struct_def: "+srna);
	return null;
}

public static PropertyDefRNA rna_find_struct_property_def(StructRNA srna, PropertyRNA prop)
{
	StructDefRNA dsrna;
	PropertyDefRNA dprop;

	if(DefRNA.preprocess==0) {
		/* we should never get here */
//		fprintf(stderr, "rna_find_property_def: only at preprocess time.\n");
		System.err.printf("rna_find_property_def: only at preprocess time.\n");
		return null;
	}

	dsrna= rna_find_struct_def(srna);
	dprop= dsrna.cont.properties.last;
	for (; dprop!=null; dprop= dprop.prev)
		if (dprop.prop==prop)
			return dprop;

	dsrna= DefRNA.structs.last;
	for (; dsrna!=null; dsrna= (StructDefRNA)dsrna.cont.prev) {
		dprop= dsrna.cont.properties.last;
		for (; dprop!=null; dprop= dprop.prev)
			if (dprop.prop==prop)
				return dprop;
	}

	return null;
}

//PropertyDefRNA *rna_find_property_def(PropertyRNA *prop)
//{
//	PropertyDefRNA *dprop;
//
//	if(!DefRNA.preprocess) {
//		/* we should never get here */
//		fprintf(stderr, "rna_find_property_def: only at preprocess time.\n");
//		return NULL;
//	}
//
//	dprop= rna_find_struct_property_def(DefRNA.laststruct, prop);
//	if (dprop)
//		return dprop;
//
//	dprop= rna_find_parameter_def(prop);
//	if (dprop)
//		return dprop;
//
//	return NULL;
//}

static FunctionDefRNA rna_find_function_def(FunctionRNA func)
{
	StructDefRNA dsrna;
	FunctionDefRNA dfunc;

	if(DefRNA.preprocess==0) {
		/* we should never get here */
//		fprintf(stderr, "rna_find_function_def: only at preprocess time.\n");
		System.err.printf("rna_find_function_def: only at preprocess time.\n");
		return null;
	}

	dsrna= rna_find_struct_def(DefRNA.laststruct);
	dfunc= dsrna.functions.last;
	for (; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.prev)
		if (dfunc.func==func)
			return dfunc;

	dsrna= DefRNA.structs.last;
	for (; dsrna!=null; dsrna= (StructDefRNA)dsrna.cont.prev) {
		dfunc= dsrna.functions.last;
		for (; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.prev)
			if (dfunc.func==func)
				return dfunc;
	}

	return null;
}

public static PropertyDefRNA rna_find_parameter_def(PropertyRNA parm)
{
	StructDefRNA dsrna;
	FunctionDefRNA dfunc;
	PropertyDefRNA dparm;

	if(DefRNA.preprocess==0) {
		/* we should never get here */
//		fprintf(stderr, "rna_find_parameter_def: only at preprocess time.\n");
		System.err.printf("rna_find_parameter_def: only at preprocess time.\n");
		return null;
	}

	dsrna= rna_find_struct_def(DefRNA.laststruct);
	dfunc= dsrna.functions.last;
	for (; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.prev) {
		dparm= dfunc.cont.properties.last;
		for (; dparm!=null; dparm= dparm.prev)
			if (dparm.prop==parm)
				return dparm;
	}

	dsrna= DefRNA.structs.last;
	for (; dsrna!=null; dsrna= (StructDefRNA)dsrna.cont.prev) {
		dfunc= dsrna.functions.last;
		for (; dfunc!=null; dfunc= (FunctionDefRNA)dfunc.cont.prev) {
			dparm= dfunc.cont.properties.last;
			for (; dparm!=null; dparm= dparm.prev)
				if (dparm.prop==parm)
					return dparm;
		}
	}

	return null;
}

static ContainerDefRNA rna_find_container_def(ContainerRNA cont)
{
	StructDefRNA ds;
	FunctionDefRNA dfunc;

	if(DefRNA.preprocess==0) {
		/* we should never get here */
//		fprintf(stderr, "rna_find_container_def: only at preprocess time.\n");
		System.err.printf("rna_find_container_def: only at preprocess time.\n");
		return null;
	}

	ds= rna_find_struct_def((StructRNA)cont);
	if(ds!=null)
		return ds.cont;

	dfunc= rna_find_function_def((FunctionRNA)cont);
	if(dfunc!=null)
		return dfunc.cont;

	return null;
}

/* DNA utility function for looking up members */

public static class DNAStructMember {
	public String type;
	public String name;
	public int arraylength;
	public int pointerlevel;
};

static int rna_member_cmp(String name, String oname)
{
	int a=0;
	int name_p = 0;
	int oname_p = 0;

	/* compare without pointer or array part */
	while(name.charAt(name_p)=='*')
		name_p++;
	while(oname.charAt(oname_p)=='*')
		oname_p++;

	while(true) {
		if(name.charAt(name_p+a)=='[' && oname.charAt(oname_p+a)==0) return 1;
		if(name.charAt(name_p+a)=='[' && oname.charAt(oname_p+a)=='[') return 1;
		if(name.charAt(name_p+a)==0) break;
		if(name.charAt(name_p+a) != oname.charAt(oname_p+a)) return 0;
		a++;
	}
	if(name.charAt(name_p+a)==0 && oname.charAt(oname_p+a) == '.') return 2;
	if(name.charAt(name_p+a)==0 && oname.charAt(oname_p+a) == '-' && oname.charAt(oname_p+a+1) == '>') return 3;

	return (name.charAt(name_p+a) == oname.charAt(oname_p+a))?1:0;
}

static int rna_find_sdna_member(SDNA sdna, String structname, String membername, DNAStructMember smember)
{
	String dnaname;
	short[] sp;
	int sp_p = 0;
	int a, b, structnr, totmember, cmp;
	
	// TMP
	if (sdna==null)
		return 0;

	structnr= DNAGenfile.DNA_struct_find_nr(sdna, structname);
	if(structnr == -1)
		return 0;

	sp= sdna.structs[structnr];
	totmember= sp[sp_p+1];
	sp_p+= 2;

	for(a=0; a<totmember; a++, sp_p+=2) {
		dnaname= sdna.names[sp[sp_p+1]];

		cmp= rna_member_cmp(dnaname, membername);

		if(cmp == 1) {
			smember.type= sdna.types[sp[sp_p+0]];
			smember.name= dnaname;

//			if(strstr(membername, "["))
			if(membername.indexOf("[")>=0)
				smember.arraylength= 0;
			else
				smember.arraylength= DNAGenfile.DNA_elem_array_size(smember.name, smember.name.length());

			smember.pointerlevel= 0;
			for(b=0; dnaname.charAt(b) == '*'; b++)
				smember.pointerlevel++;

			return 1;
		}
		else if(cmp == 2) {
			smember.type= "";
			smember.name= dnaname;
			smember.pointerlevel= 0;
			smember.arraylength= 0;

//			membername= strstr(membername, ".") + strlen(".");
			membername= membername.substring(membername.indexOf(".")+1);
			rna_find_sdna_member(sdna, sdna.types[sp[sp_p+0]], membername, smember);

			return 1;
		}
		else if(cmp == 3) {
			smember.type= "";
			smember.name= dnaname;
			smember.pointerlevel= 0;
			smember.arraylength= 0;

//			membername= strstr(membername, ".") + strlen(".");
			membername= membername.substring(membername.indexOf(".")+1);
			rna_find_sdna_member(sdna, sdna.types[sp[sp_p+0]], membername, smember);

			return 1;
		}
	}

	return 0;
}

/*  list from http://docs.python.org/reference/lexical_analysis.html#id5 */
private static String [] kwlist = {
	"and", "as", "assert", "break",
	"class", "continue", "def", "del",
	"elif", "else", "except", "exec",
	"finally", "for", "from", "global",
	"if", "import", "in", "is",
	"lambda", "not", "or", "pass",
	"print", "raise", "return", "try",
	"while", "with", "yield", null
};
static int rna_validate_identifier(String identifier, String[] error, int property)
{
	int a=0;

//	/*  list from http://docs.python.org/reference/lexical_analysis.html#id5 */
//	static char *kwlist[] = {
//		"and", "as", "assert", "break",
//		"class", "continue", "def", "del",
//		"elif", "else", "except", "exec",
//		"finally", "for", "from", "global",
//		"if", "import", "in", "is",
//		"lambda", "not", "or", "pass",
//		"print", "raise", "return", "try",
//		"while", "with", "yield", NULL
//	};


//	if (!isalpha(identifier[0])) {
	if (!Character.isLetter(identifier.charAt(0))) {
//		strcpy(error, "first character failed isalpha() check");
		error[0] = "first character failed isalpha() check";
		return 0;
	}

//	for(a=0; identifier[a]; a++) {
	for(a=0; a<identifier.length(); a++) {
		if(DefRNA.preprocess!=0 && property!=0) {
//			if(isalpha(identifier[a]) && isupper(identifier[a])) {
			if(Character.isLetter(identifier.charAt(a)) && Character.isUpperCase(identifier.charAt(a))) {
//				strcpy(error, "property names must contain lower case characters only");
				error[0] = "property names must contain lower case characters only";
				return 0;
			}
		}

//		if (identifier[a]=='_') {
		if (identifier.charAt(a)=='_') {
			continue;
		}

//		if (identifier[a]==' ') {
		if (identifier.charAt(a)==' ') {
//			strcpy(error, "spaces are not ok in identifier names");
			error[0] = "spaces are not ok in identifier names";
			return 0;
		}

//		if (isalnum(identifier[a])==0) {
		if (!Character.isLetterOrDigit(identifier.charAt(a))) {
//			strcpy(error, "one of the characters failed an isalnum() check and is not an underscore");
			error[0] = "one of the characters failed an isalnum() check and is not an underscore";
			return 0;
		}
	}

	for(a=0; kwlist[a]!=null; a++) {
//		if (strcmp(identifier, kwlist[a]) == 0) {
		if (identifier.equals(kwlist[a])) {
//			strcpy(error, "this keyword is reserved by python");
			error[0] = "this keyword is reserved by python";
			return 0;
		}
	}

	return 1;
}

/* Blender Data Definition */

public static BlenderRNA RNA_create()
{
	BlenderRNA brna;

	brna= new BlenderRNA();

//	DefRNA.sdna= DNAGenfile.DNA_sdna_from_data(DNAstr,  DNAlen, 0);
	DefRNA.structs.first= DefRNA.structs.last= null;
	DefRNA.error= 0;
	DefRNA.preprocess= 1;

	return brna;
}

//void RNA_define_free(BlenderRNA *brna)
//{
//	StructDefRNA *ds;
//	FunctionDefRNA *dfunc;
//	AllocDefRNA *alloc;
//
//	for(alloc=DefRNA.allocs.first; alloc; alloc=alloc.next)
//		MEM_freeN(alloc.mem);
//	rna_freelistN(&DefRNA.allocs);
//
//	for(ds=DefRNA.structs.first; ds; ds=ds.cont.next) {
//		for (dfunc= ds.functions.first; dfunc; dfunc= dfunc.cont.next)
//			rna_freelistN(&dfunc.cont.properties);
//
//		rna_freelistN(&ds.cont.properties);
//		rna_freelistN(&ds.functions);
//	}
//
//	rna_freelistN(&DefRNA.structs);
//
//	if(DefRNA.sdna) {
//		DNA_sdna_free(DefRNA.sdna);
//		DefRNA.sdna= NULL;
//	}
//
//	DefRNA.error= 0;
//}

public static void RNA_define_verify_sdna(int verify)
{
	DefRNA.verify= verify;
}

//void RNA_struct_free(BlenderRNA *brna, StructRNA *srna)
//{
//#ifdef RNA_RUNTIME
//	FunctionRNA *func, *nextfunc;
//	PropertyRNA *prop, *nextprop;
//	PropertyRNA *parm, *nextparm;
//
//	for(prop=srna.cont.properties.first; prop; prop=nextprop) {
//		nextprop= prop.next;
//
//		RNA_def_property_free_pointers(prop);
//
//		if(prop.flag & PROP_RUNTIME)
//			rna_freelinkN(&srna.cont.properties, prop);
//	}
//
//	for(func=srna.functions.first; func; func=nextfunc) {
//		nextfunc= func.cont.next;
//
//		for(parm=func.cont.properties.first; parm; parm=nextparm) {
//			nextparm= parm.next;
//
//			RNA_def_property_free_pointers(parm);
//
//			if(parm.flag & PROP_RUNTIME)
//				rna_freelinkN(&func.cont.properties, parm);
//		}
//
//		RNA_def_func_free_pointers(func);
//
//		if(func.flag & FUNC_RUNTIME)
//			rna_freelinkN(&srna.functions, func);
//	}
//
//	RNA_def_struct_free_pointers(srna);
//
//	if(srna.flag & STRUCT_RUNTIME)
//		rna_freelinkN(&brna.structs, srna);
//#endif
//}
//
//void RNA_free(BlenderRNA *brna)
//{
//	StructRNA *srna, *nextsrna;
//	FunctionRNA *func;
//
//	if(DefRNA.preprocess) {
//		RNA_define_free(brna);
//
//		for(srna=brna.structs.first; srna; srna=srna.cont.next) {
//			for (func= srna.functions.first; func; func= func.cont.next)
//				rna_freelistN(&func.cont.properties);
//
//			rna_freelistN(&srna.cont.properties);
//			rna_freelistN(&srna.functions);
//		}
//
//		rna_freelistN(&brna.structs);
//
//		MEM_freeN(brna);
//	}
//	else {
//		for(srna=brna.structs.first; srna; srna=nextsrna) {
//			nextsrna= srna.cont.next;
//			RNA_struct_free(brna, srna);
//		}
//	}
//}

static PropertyRNA rna_property_type_sizeof(byte type)
{
	switch(type&0xFF) {
		case RNATypes.PROP_BOOLEAN: return new BooleanPropertyRNA();
		case RNATypes.PROP_INT: return new IntPropertyRNA();
		case RNATypes.PROP_FLOAT: return new FloatPropertyRNA();
		case RNATypes.PROP_STRING: return new StringPropertyRNA();
		case RNATypes.PROP_ENUM: return new EnumPropertyRNA();
		case RNATypes.PROP_POINTER: return new PointerPropertyRNA();
		case RNATypes.PROP_COLLECTION: return new CollectionPropertyRNA();
		default: return null;
	}
}

static StructDefRNA rna_find_def_struct(StructRNA srna)
{
	StructDefRNA ds;

	for(ds=DefRNA.structs.first; ds!=null; ds=(StructDefRNA)ds.cont.next)
		if(ds.srna == srna)
			return ds;

	return null;
}

/* Struct Definition */

public static StructRNA RNA_def_struct(BlenderRNA brna, String identifier, String from)
{
	StructRNA srna, srnafrom= null;
	StructDefRNA ds= null, dsfrom= null;
	PropertyRNA prop;
	
	if(DefRNA.preprocess!=0) {
//		char error[512];
		String[] error = new String[1];

		if (rna_validate_identifier(identifier, error, 0) == 0) {
//			fprintf(stderr, "RNA_def_struct: struct identifier \"%s\" error - %s\n", identifier, error);
			System.err.printf("RNA_def_struct: struct identifier \"%s\" error - %s\n", identifier, error[0]);
			DefRNA.error= 1;
		}
	}
	
//	if(from!=null) {
//		/* find struct to derive from */
//		for(srnafrom= brna.structs.first; srnafrom!=null; srnafrom=(StructRNA)srnafrom.cont.next)
////			if(strcmp(srnafrom->identifier, from) == 0)
//			if(srnafrom.identifier.equals(from))
//				break;
//
//		if(srnafrom==null) {
////			fprintf(stderr, "RNA_def_struct: struct %s not found to define %s.\n", from, identifier);
//			System.err.printf("RNA_def_struct: struct %s not found to define %s.\n", from, identifier);
//			DefRNA.error= 1;
//		}
//	}

//	srna= MEM_callocN(sizeof(StructRNA), "StructRNA");
	srna= new StructRNA();
	DefRNA.laststruct= srna;

//	if(srnafrom!=null) {
//		/* copy from struct to derive stuff, a bit clumsy since we can't
//		 * use MEM_dupallocN, data structs may not be alloced but builtin */
////		memcpy(srna, srnafrom, sizeof(StructRNA));
//		srna = (StructRNA)srnafrom.copy();
////		srna.cont.prophash= null;
//		srna.cont.properties.first= srna.cont.properties.last= null;
//		srna.functions.first= srna.functions.last= null;
//		srna.py_type= null;
//
//		if(DefRNA.preprocess!=0) {
//			srna.base= srnafrom;
//			dsfrom= rna_find_def_struct(srnafrom);
//		}
//		else
//			srna.base= srnafrom;
//	}
	
	srna.identifier= identifier;
	srna.name= identifier; /* may be overwritten later RNA_def_struct_ui_text */
	srna.description= "";
	if(srnafrom==null)
		srna.icon= BIFIconID.ICON_DOT.ordinal();

	rna_addtail(brna.structs, srna);

	if(DefRNA.preprocess!=0) {
//		ds= MEM_callocN(sizeof(StructDefRNA), "StructDefRNA");
		ds= new StructDefRNA();
		ds.srna= srna;
		rna_addtail(DefRNA.structs, ds);

		if(dsfrom!=null)
			ds.dnafromname= dsfrom.dnaname;
	}

	/* in preprocess, try to find sdna */
//	if(DefRNA.preprocess!=0)
//		RNA_def_struct_sdna(srna, srna.identifier);
//	else
//		srna.flag |= RNATypes.STRUCT_RUNTIME;

//	if(srnafrom!=null) {
//		srna.nameproperty= srnafrom.nameproperty;
//		srna.iteratorproperty= srnafrom.iteratorproperty;
//	}
//	else {
		/* define some builtin properties */
		prop= RNA_def_property(srna.cont, "rna_properties", RNATypes.PROP_COLLECTION, RNATypes.PROP_NONE);
		RNA_def_property_flag(prop, RNATypes.PROP_BUILTIN);
		RNA_def_property_ui_text(prop, "Properties", "RNA property collection");

		if(DefRNA.preprocess!=0) {
			RNA_def_property_struct_type(prop, "Property");
			RNA_def_property_collection_funcs(prop, "rna_builtin_properties_begin", "rna_builtin_properties_next", "rna_iterator_listbase_end", "rna_builtin_properties_get", null, null, "rna_builtin_properties_lookup_string");
		}
		else {
//#ifdef RNA_RUNTIME
//			CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//			cprop->begin= rna_builtin_properties_begin;
//			cprop->next= rna_builtin_properties_next;
//			cprop->get= rna_builtin_properties_get;
//			cprop->item_type= &RNA_Property;
//#endif
		}

		prop= RNA_def_property(srna.cont, "rna_type", RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
		RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
		RNA_def_property_ui_text(prop, "RNA", "RNA type definition");

		if(DefRNA.preprocess!=0) {
			RNA_def_property_struct_type(prop, "Struct");
			RNA_def_property_pointer_funcs(prop, "rna_builtin_type_get", null, null, null);
		}
		else {
//#ifdef RNA_RUNTIME
//			PointerPropertyRNA *pprop= (PointerPropertyRNA*)prop;
//			pprop->get= rna_builtin_type_get;
//			pprop->type= &RNA_Struct;
//#endif
		}
//	}

	return srna;
}

public static void RNA_def_struct_sdna(StructRNA srna, String structname)
{
	StructDefRNA ds;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_struct_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_struct_sdna: only during preprocessing.\n");
		return;
	}

	ds= rna_find_def_struct(srna);

//	if(DNAGenfile.DNA_struct_find_nr(DefRNA.sdna, structname)==0) {
//		if(DefRNA.silent==0) {
////			fprintf(stderr, "RNA_def_struct_sdna: %s not found.\n", structname);
//			System.err.printf("RNA_def_struct_sdna: %s not found.\n", structname);
//			DefRNA.error= 1;
//		}
//		return;
//	}

	ds.dnaname= structname;
}

//void RNA_def_struct_sdna_from(StructRNA *srna, const char *structname, const char *propname)
//{
//	StructDefRNA *ds;
//
//	if(!DefRNA.preprocess) {
//		fprintf(stderr, "RNA_def_struct_sdna_from: only during preprocessing.\n");
//		return;
//	}
//
//	ds= rna_find_def_struct(srna);
//
//	if(!ds.dnaname) {
//		fprintf(stderr, "RNA_def_struct_sdna_from: %s base struct must know DNA already.\n", structname);
//		return;
//	}
//
//	if(!DNA_struct_find_nr(DefRNA.sdna, structname)) {
//		if(!DefRNA.silent) {
//			fprintf(stderr, "RNA_def_struct_sdna_from: %s not found.\n", structname);
//			DefRNA.error= 1;
//		}
//		return;
//	}
//
//	ds.dnafromprop= propname;
//	ds.dnaname= structname;
//}

public static void RNA_def_struct_name_property(StructRNA srna, PropertyRNA prop)
{
	if(prop.type != RNATypes.PROP_STRING) {
//		fprintf(stderr, "RNA_def_struct_name_property: %s.%s, must be a string property.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_struct_name_property: %s.%s, must be a string property.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
	}
	else
		srna.nameproperty= prop;
}

public static void RNA_def_struct_nested(BlenderRNA brna, StructRNA srna, String structname)
{
	StructRNA srnafrom;

	/* find struct to derive from */
	for(srnafrom= brna.structs.first; srnafrom!=null; srnafrom=(StructRNA)srnafrom.cont.next)
//		if(strcmp(srnafrom.identifier, structname) == 0)
		if(srnafrom.identifier.equals(structname))
			break;

	if(srnafrom==null) {
		System.err.printf("RNA_def_struct_nested: struct %s not found for %s.\n", structname, srna.identifier);
		DefRNA.error= 1;
	}

	srna.nested= srnafrom;
}

//void RNA_def_struct_flag(StructRNA *srna, int flag)
//{
//	srna.flag |= flag;
//}

public static void RNA_def_struct_clear_flag(StructRNA srna, int flag)
{
	srna.flag &= ~flag;
}

//public static void RNA_def_struct_refine_func(StructRNA srna, StructRefineFunc refine)
public static void RNA_def_struct_refine_func(StructRNA srna, String refine)
{
	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_struct_refine_func: only during preprocessing.\n");
		System.err.printf("RNA_def_struct_refine_func: only during preprocessing.\n");
		return;
	}

//	if(refine!=null) srna.refine= (StructRefineFunc)refine;
	if(refine!=null) srna.refine= (StructRefineFunc)FString.toFunc(refine);
}

public static void RNA_def_struct_idprops_func(StructRNA srna, String idproperties)
{
	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_struct_idprops_func: only during preprocessing.\n");
		System.err.printf("RNA_def_struct_idprops_func: only during preprocessing.\n");
		return;
	}

//	if(idproperties!=null) srna.idproperties= (IDPropertiesFunc)idproperties;
	if(idproperties!=null) srna.idproperties= (IDPropertiesFunc)FString.toFunc(idproperties);
}

//public static void RNA_def_struct_register_funcs(StructRNA srna, StructRegisterFunc reg, StructUnregisterFunc unreg)
public static void RNA_def_struct_register_funcs(StructRNA srna, String reg, String unreg)
{
	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_struct_register_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_struct_register_funcs: only during preprocessing.\n");
		return;
	}

//	if(reg!=null) srna.reg= (StructRegisterFunc)reg;
	if(reg!=null) srna.reg= (StructRegisterFunc)FString.toFunc(reg);
//	if(unreg!=null) srna.unreg= (StructUnregisterFunc)unreg;
	if(unreg!=null) srna.unreg= (StructUnregisterFunc)FString.toFunc(unreg);
}

public static void RNA_def_struct_path_func(StructRNA srna, String path)
{
	if(DefRNA.preprocess==0) {
		System.err.printf("RNA_def_struct_path_func: only during preprocessing.\n");
		return;
	}

//	if(path!=null) srna.path= (StructPathFunc)path;
}

//void RNA_def_struct_identifier(StructRNA *srna, const char *identifier)
//{
//	if(DefRNA.preprocess) {
//		fprintf(stderr, "RNA_def_struct_name_runtime: only at runtime.\n");
//		return;
//	}
//
//	srna.identifier= identifier;
//}

public static void RNA_def_struct_ui_text(StructRNA srna, String name, String description)
{
	srna.name= name;
	srna.description= description;
}

public static void RNA_def_struct_ui_icon(StructRNA srna, Object icon)
{
	srna.icon= (icon instanceof BIFIconID)?((BIFIconID)icon).ordinal():(Integer)icon;
}

/* Property Definition */

public static PropertyRNA RNA_def_property(Object cont_, String identifier, int type, int subtype)
{
	StructRNA srna= DefRNA.laststruct;
	ContainerRNA cont= (ContainerRNA)cont_;
	ContainerDefRNA dcont;
	PropertyDefRNA dprop= null;
	PropertyRNA prop;

	if(DefRNA.preprocess!=0) {
//		char error[512];
		String[] error = new String[1];

		if (rna_validate_identifier(identifier, error, 1) == 0) {
//			fprintf(stderr, "RNA_def_property: property identifier \"%s\" - %s\n", identifier, error);
			System.err.printf("RNA_def_property: property identifier \"%s\" - %s\n", identifier, error[0]);
			DefRNA.error= 1;
		}

		dcont= rna_find_container_def(cont);
//		dprop= MEM_callocN(sizeof(PropertyDefRNA), "PropertyDefRNA");
		dprop= new PropertyDefRNA();
		rna_addtail(dcont.properties, dprop);
	}

//	prop= MEM_callocN(rna_property_type_sizeof(type), "PropertyRNA");
    prop= rna_property_type_sizeof((byte)type);

	switch(type) {
		case RNATypes.PROP_BOOLEAN:
			break;
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;

			iprop.hardmin= (subtype == RNATypes.PROP_UNSIGNED)? 0: INT_MIN;
			iprop.hardmax= INT_MAX;

			iprop.softmin= (subtype == RNATypes.PROP_UNSIGNED)? 0: -10000; /* rather arbitrary .. */
			iprop.softmax= 10000;
			iprop.step= 1;
			break;
		}
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;

			fprop.hardmin= (subtype == RNATypes.PROP_UNSIGNED)? 0.0f: -FLT_MAX;
			fprop.hardmax= FLT_MAX;

			if(subtype == RNATypes.PROP_COLOR) {
				fprop.softmin= 0.0f;
				fprop.softmax= 1.0f;
			}
			else if(subtype == RNATypes.PROP_PERCENTAGE) {
				fprop.softmin= fprop.hardmin= 0.0f;
				fprop.softmax= fprop.hardmax= 1.0f;
			}
			else {
				fprop.softmin= (subtype == RNATypes.PROP_UNSIGNED)? 0.0f: -10000.0f; /* rather arbitrary .. */
				fprop.softmax= 10000.0f;
			}
			fprop.step= 10;
			fprop.precision= 3;
			break;
		}
		case RNATypes.PROP_STRING: {
			StringPropertyRNA sprop= (StringPropertyRNA)prop;

			sprop.defaultvalue= "";
			sprop.maxlength= 0;
			break;
		}
		case RNATypes.PROP_ENUM:
		case RNATypes.PROP_POINTER:
		case RNATypes.PROP_COLLECTION:
			break;
		default:
//			fprintf(stderr, "RNA_def_property: %s.%s, invalid property type.\n", srna.identifier, identifier);
			System.err.printf("RNA_def_property: %s.%s, invalid property type.\n", srna.identifier, identifier);
			DefRNA.error= 1;
			return null;
	}

	if(DefRNA.preprocess!=0) {
		dprop.cont= cont;
		dprop.prop= prop;
	}

	prop.magic= rna_internal_types.RNA_MAGIC;
	prop.identifier= identifier;
	prop.type= type;
	prop.subtype= subtype;
	prop.name= identifier;
	prop.description= "";

	if(type != RNATypes.PROP_COLLECTION && type != RNATypes.PROP_POINTER) {
		prop.flag= RNATypes.PROP_EDITABLE;

		if(type != RNATypes.PROP_STRING)
			prop.flag |= RNATypes.PROP_ANIMATABLE;
	}

	if(DefRNA.preprocess!=0) {
		switch(type) {
			case RNATypes.PROP_BOOLEAN:
				DefRNA.silent= 1;
				RNA_def_property_boolean_sdna(prop, null, identifier, 0);
				DefRNA.silent= 0;
				break;
			case RNATypes.PROP_INT: {
				DefRNA.silent= 1;
				RNA_def_property_int_sdna(prop, null, identifier);
				DefRNA.silent= 0;
				break;
			}
			case RNATypes.PROP_FLOAT: {
				DefRNA.silent= 1;
				RNA_def_property_float_sdna(prop, null, identifier);
				DefRNA.silent= 0;
				break;
			}
			case RNATypes.PROP_STRING: {
				DefRNA.silent= 1;
				RNA_def_property_string_sdna(prop, null, identifier);
				DefRNA.silent= 0;
				break;
			}
			case RNATypes.PROP_ENUM:
				DefRNA.silent= 1;
				RNA_def_property_enum_sdna(prop, null, identifier);
				DefRNA.silent= 0;
				break;
			case RNATypes.PROP_POINTER:
				DefRNA.silent= 1;
				RNA_def_property_pointer_sdna(prop, null, identifier);
				DefRNA.silent= 0;
				break;
			case RNATypes.PROP_COLLECTION:
				DefRNA.silent= 1;
				RNA_def_property_collection_sdna(prop, null, identifier, null);
				DefRNA.silent= 0;
				break;
		}
	}
	else {
		prop.flag |= RNATypes.PROP_IDPROPERTY|RNATypes.PROP_RUNTIME;
//		System.out.println("RNA_def_property PROP_IDPROPERTY: "+identifier);
//		Thread.dumpStack();
//#ifdef RNA_RUNTIME
//		if(cont.prophash)
//			BLI_ghash_insert(cont.prophash, (void*)prop.identifier, prop);
//#endif
	}

	rna_addtail(cont.properties, prop);

	return prop;
}

public static void RNA_def_property_flag(PropertyRNA prop, int flag)
{
	prop.flag |= flag;
}

public static void RNA_def_property_clear_flag(PropertyRNA prop, int flag)
{
	prop.flag &= ~flag;
}

public static void RNA_def_property_array(PropertyRNA prop, int arraylength)
{
	StructRNA srna= DefRNA.laststruct;

	if(arraylength<0) {
//		fprintf(stderr, "RNA_def_property_array: %s.%s, array length must be zero of greater.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_array: %s.%s, array length must be zero of greater.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if(arraylength>rna_internal_types.RNA_MAX_ARRAY) {
//		fprintf(stderr, "RNA_def_property_array: %s.%s, array length must be smaller than %d.\n", srna.identifier, prop.identifier, rna_internal_types.RNA_MAX_ARRAY);
		System.err.printf("RNA_def_property_array: %s.%s, array length must be smaller than %d.\n", srna.identifier, prop.identifier, rna_internal_types.RNA_MAX_ARRAY);
		DefRNA.error= 1;
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_BOOLEAN:
		case RNATypes.PROP_INT:
		case RNATypes.PROP_FLOAT:
			prop.arraylength[0]= arraylength;
			break;
		default:
//			fprintf(stderr, "RNA_def_property_array: %s.%s, only boolean/int/float can be array.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_array: %s.%s, only boolean/int/float can be array.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_multi_array(PropertyRNA prop, int dimension, int[] length)
{
	StructRNA srna= DefRNA.laststruct;
	int i;
	
	if (dimension < 1 || dimension > RNATypes.RNA_MAX_ARRAY_DIMENSION) {
//		fprintf(stderr, "RNA_def_property_multi_array: \"%s.%s\", array dimension must be between 1 and %d.\n", srna.identifier, prop.identifier, RNATypes.RNA_MAX_ARRAY_DIMENSION);
		System.err.printf("RNA_def_property_multi_array: \"%s.%s\", array dimension must be between 1 and %d.\n", srna.identifier, prop.identifier, RNATypes.RNA_MAX_ARRAY_DIMENSION);
		DefRNA.error= 1;
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_BOOLEAN:
		case RNATypes.PROP_INT:
		case RNATypes.PROP_FLOAT:
			break;
		default:
//			fprintf(stderr, "RNA_def_property_multi_array: \"%s.%s\", only boolean/int/float can be array.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_multi_array: \"%s.%s\", only boolean/int/float can be array.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}

	prop.arraydimension= dimension;
	prop.totarraylength= 0;

	if(length!=null) {
//		memcpy(prop.arraylength, length, sizeof(int)*dimension);
		System.arraycopy(length, 0, prop.arraylength, 0, dimension);

		prop.totarraylength= length[0];
		for(i=1; i<dimension; i++)
			prop.totarraylength *= length[i];
	}
	else
//		memset(prop.arraylength, 0, sizeof(prop.arraylength));
		Arrays.fill(prop.arraylength, 0);

	/* TODO make sure arraylength values are sane  */
}

public static void RNA_def_property_ui_text(PropertyRNA prop, String name, String description)
{
	prop.name= name;
	prop.description= description;
}

public static void RNA_def_property_ui_icon(PropertyRNA prop, Object icon, int consecutive)
{
	prop.icon= (icon instanceof BIFIconID)?((BIFIconID)icon).ordinal():(Integer)icon;
	if(consecutive!=0)
		prop.flag |= RNATypes.PROP_ICONS_CONSECUTIVE;
}

public static void RNA_def_property_ui_range(PropertyRNA prop, double min, double max, double step, int precision)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;
			iprop.softmin= (int)min;
			iprop.softmax= (int)max;
			iprop.step= (int)step;
			break;
		}
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
			fprop.softmin= (float)min;
			fprop.softmax= (float)max;
			fprop.step= (float)step;
			fprop.precision= (int)precision;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_ui_range: \"%s.%s\", invalid type for ui range.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_ui_range: \"%s.%s\", invalid type for ui range.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_range(PropertyRNA prop, double min, double max)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;
			iprop.hardmin= (int)min;
			iprop.hardmax= (int)max;
			iprop.softmin= (int)MAX2((int)min, iprop.hardmin);
			iprop.softmax= (int)MIN2((int)max, iprop.hardmax);
			break;
		}
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
			fprop.hardmin= (float)min;
			fprop.hardmax= (float)max;
			fprop.softmin= (float)MAX2((float)min, fprop.hardmin);
			fprop.softmax= (float)MIN2((float)max, fprop.hardmax);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_range: \"%s.%s\", invalid type for range.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_range: \"%s.%s\", invalid type for range.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_struct_type(PropertyRNA prop, String type)
{
	StructRNA srna= DefRNA.laststruct;
	
//	System.out.println("RNA_def_property_struct_type prop.type: "+prop.type+" prop.identifier: "+prop.identifier);
//	Thread.dumpStack();

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_struct_type: only during preprocessing.\n");
		System.err.printf("RNA_def_property_struct_type: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_POINTER: {
			PointerPropertyRNA pprop= (PointerPropertyRNA)prop;
//			pprop.type = (StructRNA)type;
			pprop.type = new StructRNA(type);
			break;
		}
		case RNATypes.PROP_COLLECTION: {
			CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
//			cprop.item_type = (StructRNA)type;
			cprop.item_type = new StructRNA(type);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_struct_type: %s.%s, invalid type for struct type.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_struct_type: %s.%s, invalid type for struct type.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

//void RNA_def_property_struct_runtime(PropertyRNA *prop, StructRNA *type)
//{
//	StructRNA *srna= DefRNA.laststruct;
//
//	if(DefRNA.preprocess) {
//		fprintf(stderr, "RNA_def_property_struct_runtime: only at runtime.\n");
//		return;
//	}
//
//	switch(prop.type) {
//		case PROP_POINTER: {
//			PointerPropertyRNA *pprop= (PointerPropertyRNA*)prop;
//			pprop.type = type;
//
//			if(type && (type.flag & STRUCT_ID_REFCOUNT))
//				prop.flag |= PROP_ID_REFCOUNT;
//
//			break;
//		}
//		case PROP_COLLECTION: {
//			CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//			cprop.type = type;
//			break;
//		}
//		default:
//			fprintf(stderr, "RNA_def_property_struct_runtime: %s.%s, invalid type for struct type.\n", srna.identifier, prop.identifier);
//			DefRNA.error= 1;
//			break;
//	}
//}

public static void RNA_def_property_enum_items(PropertyRNA prop, EnumPropertyItem[] item)
{
	StructRNA srna= DefRNA.laststruct;
	int i, defaultfound= 0;

	switch(prop.type) {
		case RNATypes.PROP_ENUM: {
			EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
			eprop.item= (EnumPropertyItem[])item;
			eprop.totitem= 0;
			for(i=0; item[i].identifier!=null; i++) {
				eprop.totitem++;

//				if(item[i].identifier[0] && item[i].value == eprop.defaultvalue)
				if(!item[i].identifier.equals("") && item[i].value == eprop.defaultvalue)
					defaultfound= 1;
			}

			if(defaultfound==0)
				eprop.defaultvalue= item[0].value;

			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_enum_items: %s.%s, invalid type for struct type.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_enum_items: %s.%s, invalid type for struct type.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_string_maxlength(PropertyRNA prop, int maxlength)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_STRING: {
			StringPropertyRNA sprop= (StringPropertyRNA)prop;
			sprop.maxlength= maxlength;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_string_maxlength: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_string_maxlength: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_boolean_default(PropertyRNA prop, int value)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_BOOLEAN: {
			BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
			bprop.defaultvalue= value!=0;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_boolean_default: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_boolean_default: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_boolean_array_default(PropertyRNA prop, boolean[] array)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_BOOLEAN: {
			BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
			bprop.defaultarray= array;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_boolean_default: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_boolean_default: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_int_default(PropertyRNA prop, int value)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;
			iprop.defaultvalue= value;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_int_default: %s.%s, type is not int.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_int_default: %s.%s, type is not int.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

static void RNA_def_property_int_array_default(PropertyRNA prop, int[] array)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;
			iprop.defaultarray= array;
			break;
		}
		default:
			System.err.printf("RNA_def_property_int_default: %s.%s, type is not int.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_float_default(PropertyRNA prop, float value)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
			fprop.defaultvalue= value;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_float_default: \"%s.%s\", type is not float.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_float_default: \"%s.%s\", type is not float.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}
/* array must remain valid after this function finishes */
public static void RNA_def_property_float_array_default(PropertyRNA prop, float[] array)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
			fprop.defaultarray= array; /* WARNING, this array must not come from the stack and lost */
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_float_default: \"%s.%s\", type is not float.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_float_default: \"%s.%s\", type is not float.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_string_default(PropertyRNA prop, String value)
{
	StructRNA srna= DefRNA.laststruct;

	switch(prop.type) {
		case RNATypes.PROP_STRING: {
			StringPropertyRNA sprop= (StringPropertyRNA)prop;
			sprop.defaultvalue= value;
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_string_default: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_string_default: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_enum_default(PropertyRNA prop, int value)
{
	StructRNA srna= DefRNA.laststruct;
	int i, defaultfound= 0;

	switch(prop.type) {
		case RNATypes.PROP_ENUM: {
			EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
			eprop.defaultvalue= value;

			for(i=0; i<eprop.totitem; i++) {
//				if(eprop.item[i].identifier[0] && eprop.item[i].value == eprop.defaultvalue)
				if(eprop.item[i].identifier!=null && !eprop.item[i].identifier.equals("") && eprop.item[i].value == eprop.defaultvalue)
					defaultfound= 1;
			}

			if(defaultfound==0 && eprop.totitem!=0) {
				if(value == 0) {
					eprop.defaultvalue= eprop.item[0].value;
				}
				else {
//					fprintf(stderr, "RNA_def_property_enum_default: \"%s.%s\", default is not in items.\n", srna.identifier, prop.identifier);
					System.err.printf("RNA_def_property_enum_default: \"%s.%s\", default is not in items.\n", srna.identifier, prop.identifier);
					DefRNA.error= 1;
				}
			}

			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_enum_default: \"%s.%s\", type is not enum.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_enum_default: \"%s.%s\", type is not enum.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

/* SDNA */

static PropertyDefRNA rna_def_property_sdna(PropertyRNA prop, String structname, String propname)
{
	DNAStructMember smember = new DNAStructMember();
	StructDefRNA ds;
	PropertyDefRNA dp;

	dp= rna_find_struct_property_def(DefRNA.laststruct, prop);
	if (dp==null) return null;

	ds= rna_find_struct_def((StructRNA)dp.cont);

	if(structname==null)
		structname= ds.dnaname;
	if(propname==null)
		propname= prop.identifier;

//	if(rna_find_sdna_member(DefRNA.sdna, structname, propname, smember)==0) {
//		if(DefRNA.silent!=0) {
//			return null;
//		}
//		else if(DefRNA.verify==0) {
//			/* some basic values to survive even with sdna info */
//			dp.dnastructname= structname;
//			dp.dnaname= propname;
//			if(prop.type == RNATypes.PROP_BOOLEAN)
//				dp.dnaarraylength= 1;
//			if(prop.type == RNATypes.PROP_POINTER)
//				dp.dnapointerlevel= 1;
//			return dp;
//		}
//		else {
////			fprintf(stderr, "rna_def_property_sdna: %s.%s not found.\n", structname, propname);
//			System.err.printf("rna_def_property_sdna: %s.%s not found.\n", structname, propname);
//			DefRNA.error= 1;
//			return null;
//		}
//	}

	if(smember.arraylength > 1)
		prop.arraylength[0]= smember.arraylength;
	else
		prop.arraylength[0]= 0;

	dp.dnastructname= structname;
	dp.dnastructfromname= ds.dnafromname;
	dp.dnastructfromprop= ds.dnafromprop;
	dp.dnaname= propname;
	dp.dnatype= smember.type;
	dp.dnaarraylength= smember.arraylength;
	dp.dnapointerlevel= smember.pointerlevel;

	return dp;
}

static void RNA_def_property_boolean_sdna(PropertyRNA prop, String structname, String propname, int bit)
{
	PropertyDefRNA dp;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_BOOLEAN) {
//		fprintf(stderr, "RNA_def_property_boolean_sdna: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_boolean_sdna: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if((dp=rna_def_property_sdna(prop, structname, propname))!=null)
		dp.booleanbit= bit;
}

public static void RNA_def_property_boolean_negative_sdna(PropertyRNA prop, String structname, String propname, int booleanbit)
{
	PropertyDefRNA dp;

	RNA_def_property_boolean_sdna(prop, structname, propname, booleanbit);

	dp= rna_find_struct_property_def(DefRNA.laststruct, prop);

	if(dp!=null)
		dp.booleannegative= 1;
}

static void RNA_def_property_int_sdna(PropertyRNA prop, String structname, String propname)
{
	PropertyDefRNA dp;
	IntPropertyRNA iprop= (IntPropertyRNA)prop;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_INT) {
//		fprintf(stderr, "RNA_def_property_int_sdna: %s.%s, type is not int.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_int_sdna: %s.%s, type is not int.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if((dp= rna_def_property_sdna(prop, structname, propname))!=null) {
		/* SDNA doesn't pass us unsigned unfortunately .. */
//		if(dp.dnatype!=null && strcmp(dp.dnatype, "char") == 0) {
		if(dp.dnatype!=null && dp.dnatype.equals("char")) {
			iprop.hardmin= iprop.softmin= CHAR_MIN;
			iprop.hardmax= iprop.softmax= CHAR_MAX;
		}
//		else if(dp.dnatype!=null && strcmp(dp.dnatype, "short") == 0) {
		else if(dp.dnatype!=null && dp.dnatype.equals("short")) {
			iprop.hardmin= iprop.softmin= SHRT_MIN;
			iprop.hardmax= iprop.softmax= SHRT_MAX;
		}
//		else if(dp.dnatype!=null && strcmp(dp.dnatype, "int") == 0) {
		else if(dp.dnatype!=null && dp.dnatype.equals("int")) {
			iprop.hardmin= INT_MIN;
			iprop.hardmax= INT_MAX;

			iprop.softmin= -10000; /* rather arbitrary .. */
			iprop.softmax= 10000;
		}

		if((prop.subtype) == RNATypes.PROP_UNSIGNED || (prop.subtype) == RNATypes.PROP_PERCENTAGE)
			iprop.hardmin= iprop.softmin= 0;
	}
}

static void RNA_def_property_float_sdna(PropertyRNA prop, String structname, String propname)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_FLOAT) {
//		fprintf(stderr, "RNA_def_property_float_sdna: %s.%s, type is not float.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_float_sdna: %s.%s, type is not float.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	rna_def_property_sdna(prop, structname, propname);
}

public static void RNA_def_property_enum_sdna(PropertyRNA prop, String structname, String propname)
{
	PropertyDefRNA dp;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_ENUM) {
//		fprintf(stderr, "RNA_def_property_enum_sdna: %s.%s, type is not enum.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_enum_sdna: %s.%s, type is not enum.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if((dp=rna_def_property_sdna(prop, structname, propname))!=null) {
		if(prop.arraylength[0]!=0) {
			prop.arraylength[0]= 0;
			if(DefRNA.silent==0) {
//				fprintf(stderr, "RNA_def_property_enum_sdna: %s.%s, array not supported for enum type.\n", structname, propname);
				System.err.printf("RNA_def_property_enum_sdna: %s.%s, array not supported for enum type.\n", structname, propname);
				DefRNA.error= 1;
			}
		}
	}
}

public static void RNA_def_property_enum_bitflag_sdna(PropertyRNA prop, String structname, String propname)
{
	PropertyDefRNA dp;

	RNA_def_property_enum_sdna(prop, structname, propname);

	dp= rna_find_struct_property_def(DefRNA.laststruct, prop);

	if(dp!=null)
		dp.enumbitflags= 1;
}

public static void RNA_def_property_string_sdna(PropertyRNA prop, String structname, String propname)
{
	PropertyDefRNA dp;
	StringPropertyRNA sprop= (StringPropertyRNA)prop;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_STRING) {
//		fprintf(stderr, "RNA_def_property_string_sdna: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_string_sdna: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}
	
	// TMP
	if (propname.contains("->")) {
		propname = propname.replace("->", ".");
	}

	if((dp=rna_def_property_sdna(prop, structname, propname))!=null) {
		if(prop.arraylength[0]!=0) {
			sprop.maxlength= prop.arraylength[0];
			prop.arraylength[0]= 0;
		}
	}
}

public static void RNA_def_property_pointer_sdna(PropertyRNA prop, String structname, String propname)
{
	PropertyDefRNA dp;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_POINTER) {
//		fprintf(stderr, "RNA_def_property_pointer_sdna: %s.%s, type is not pointer.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_pointer_sdna: %s.%s, type is not pointer.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if((dp=rna_def_property_sdna(prop, structname, propname))!=null) {
		if(prop.arraylength[0]!=0) {
			prop.arraylength[0]= 0;
			if(DefRNA.silent==0) {
//				fprintf(stderr, "RNA_def_property_pointer_sdna: %s.%s, array not supported for pointer type.\n", structname, propname);
				System.err.printf("RNA_def_property_pointer_sdna: %s.%s, array not supported for pointer type.\n", structname, propname);
				DefRNA.error= 1;
			}
		}
	}
}

static void RNA_def_property_collection_sdna(PropertyRNA prop, String structname, String propname, String lengthpropname)
{
	PropertyDefRNA dp;
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_sdna: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_sdna: only during preprocessing.\n");
		return;
	}

	if((prop.type) != RNATypes.PROP_COLLECTION) {
//		fprintf(stderr, "RNA_def_property_collection_sdna: %s.%s, type is not collection.\n", srna.identifier, prop.identifier);
		System.err.printf("RNA_def_property_collection_sdna: %s.%s, type is not collection.\n", srna.identifier, prop.identifier);
		DefRNA.error= 1;
		return;
	}

	if((dp=rna_def_property_sdna(prop, structname, propname))!=null) {
		if(prop.arraylength[0]!=0 && lengthpropname==null) {
			prop.arraylength[0]= 0;

			if(DefRNA.silent==0) {
//				fprintf(stderr, "RNA_def_property_collection_sdna: %s.%s, array of collections not supported.\n", structname, propname);
				System.err.printf("RNA_def_property_collection_sdna: %s.%s, array of collections not supported.\n", structname, propname);
				DefRNA.error= 1;
			}
		}

//		if(dp.dnatype!=null && strcmp(dp.dnatype, "ListBase") == 0) {
		if(dp.dnatype!=null && dp.dnatype.equals("ListBase")) {
//			cprop.next= (PropCollectionNextFunc)"rna_iterator_listbase_next";
			cprop.next= (PropCollectionNextFunc)FString.toFunc("rna_iterator_listbase_next");
//			cprop.get= (PropCollectionGetFunc)"rna_iterator_listbase_get";
			cprop.get= (PropCollectionGetFunc)FString.toFunc("rna_iterator_listbase_get");
//			cprop.end= (PropCollectionEndFunc)"rna_iterator_listbase_end";
			cprop.end= (PropCollectionEndFunc)FString.toFunc("rna_iterator_listbase_end");
		}
	}

	if(dp!=null && lengthpropname!=null) {
		DNAStructMember smember = new DNAStructMember();
		StructDefRNA ds= rna_find_struct_def((StructRNA)dp.cont);

		if(structname==null)
			structname= ds.dnaname;

//		if(lengthpropname[0] == 0 || rna_find_sdna_member(DefRNA.sdna, structname, lengthpropname, smember)) {
		if(lengthpropname.equals("") || rna_find_sdna_member(DefRNA.sdna, structname, lengthpropname, smember)!=0) {
//		if(lengthpropname.equals("")) {
//			if(lengthpropname[0] == 0) {
			if(lengthpropname.equals("")) {
				dp.dnalengthfixed= prop.arraylength[0];
				prop.arraylength[0]= 0;
			}
			else {
				dp.dnalengthstructname= structname;
				dp.dnalengthname= lengthpropname;
			}

//			cprop.next= (PropCollectionNextFunc)"rna_iterator_array_next";
			cprop.next= (PropCollectionNextFunc)FString.toFunc("rna_iterator_array_next");
//			cprop.end= (PropCollectionEndFunc)"rna_iterator_array_end";
			cprop.end= (PropCollectionEndFunc)FString.toFunc("rna_iterator_array_end");

			if(dp.dnapointerlevel >= 2)
//				cprop.get= (PropCollectionGetFunc)"rna_iterator_array_dereference_get";
				cprop.get= (PropCollectionGetFunc)FString.toFunc("rna_iterator_array_dereference_get");
			else
//				cprop.get= (PropCollectionGetFunc)"rna_iterator_array_get";
				cprop.get= (PropCollectionGetFunc)FString.toFunc("rna_iterator_array_get");
		}
		else {
			if(DefRNA.silent==0) {
//				fprintf(stderr, "RNA_def_property_collection_sdna: %s.%s not found.\n", structname, lengthpropname);
				System.err.printf("RNA_def_property_collection_sdna: %s.%s not found.\n", structname, lengthpropname);
				DefRNA.error= 1;
			}
		}
	}
}

/* Functions */

public static void RNA_def_property_editable_func(PropertyRNA prop, String editable)
{
	if(DefRNA.preprocess==0) {
		System.err.printf("RNA_def_property_editable_func: only during preprocessing.\n");
		return;
	}

	if(editable!=null) prop.editable= (EditableFunc)FString.toFunc(editable);
}

public static void RNA_def_property_editable_array_func(PropertyRNA prop, String editable)
{
	if(DefRNA.preprocess==0) {
		System.err.printf("RNA_def_property_editable_array_func: only during preprocessing.\n");
		return;
	}

	if(editable!=null) prop.itemeditable= (ItemEditableFunc)FString.toFunc(editable);
}

public static void RNA_def_property_update(PropertyRNA prop, int noteflag, String func)
{
	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_struct_refine_func: only during preprocessing.\n");
		System.err.printf("RNA_def_struct_refine_func: only during preprocessing.\n");
		return;
	}

	prop.noteflag= noteflag;
//	prop.update= (UpdateFunc)func;
	prop.update= (UpdateFunc)FString.toFunc(func);
}

public static void RNA_def_property_boolean_funcs(PropertyRNA prop, String get, String set)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_BOOLEAN: {
			BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;

			if(prop.arraylength[0]!=0) {
//				if(get!=null) bprop.getarray= (PropBooleanArrayGetFunc)get;
				if(get!=null) bprop.getarray= (PropBooleanArrayGetFunc)FString.toFunc(get);
//				if(set!=null) bprop.setarray= (PropBooleanArraySetFunc)set;
				if(set!=null) bprop.setarray= (PropBooleanArraySetFunc)FString.toFunc(set);
			}
			else {
//				if(get!=null) bprop.get= (PropBooleanGetFunc)get;
				if(get!=null) bprop.get= (PropBooleanGetFunc)FString.toFunc(get);
//				if(set!=null) bprop.set= (PropBooleanSetFunc)set;
				if(set!=null) bprop.set= (PropBooleanSetFunc)FString.toFunc(set);
			}
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_boolean_funcs: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			System.out.printf("RNA_def_property_boolean_funcs: %s.%s, type is not boolean.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_int_funcs(PropertyRNA prop, String get, String set, String range)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_INT: {
			IntPropertyRNA iprop= (IntPropertyRNA)prop;

			if(prop.arraydimension!=0) {
//				if(get!=null) iprop.getarray= (PropIntArrayGetFunc)get;
				if(get!=null) iprop.getarray= (PropIntArrayGetFunc)FString.toFunc(get);
//				if(set!=null) iprop.setarray= (PropIntArraySetFunc)set;
				if(set!=null) iprop.setarray= (PropIntArraySetFunc)FString.toFunc(set);
			}
			else {
//				if(get!=null) iprop.get= (PropIntGetFunc)get;
				if(get!=null) iprop.get= (PropIntGetFunc)FString.toFunc(get);
//				if(set!=null) iprop.set= (PropIntSetFunc)set;
				if(set!=null) iprop.set= (PropIntSetFunc)FString.toFunc(set);
			}
//			if(range!=null) iprop.range= (PropIntRangeFunc)range;
			if(range!=null) iprop.range= (PropIntRangeFunc)FString.toFunc(range);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_int_funcs: \"%s.%s\", type is not int.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_int_funcs: \"%s.%s\", type is not int.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_float_funcs(PropertyRNA prop, String get, String set, String range)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_FLOAT: {
			FloatPropertyRNA fprop= (FloatPropertyRNA)prop;

			if(prop.arraylength[0]!=0) {
//				if(get!=null) fprop.getarray= (PropFloatArrayGetFunc)get;
				if(get!=null) fprop.getarray= (PropFloatArrayGetFunc)FString.toFunc(get);
//				if(set!=null) fprop.setarray= (PropFloatArraySetFunc)set;
				if(set!=null) fprop.setarray= (PropFloatArraySetFunc)FString.toFunc(set);
			}
			else {
//				if(get!=null) fprop.get= (PropFloatGetFunc)get;
				if(get!=null) fprop.get= (PropFloatGetFunc)FString.toFunc(get);
//				if(set!=null) fprop.set= (PropFloatSetFunc)set;
				if(set!=null) fprop.set= (PropFloatSetFunc)FString.toFunc(set);
			}
//			if(range!=null) fprop.range= (PropFloatRangeFunc)range;
			if(range!=null) fprop.range= (PropFloatRangeFunc)FString.toFunc(range);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_float_funcs: %s.%s, type is not float.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_float_funcs: %s.%s, type is not float.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_enum_funcs(PropertyRNA prop, String get, String set, String item)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_ENUM: {
			EnumPropertyRNA eprop= (EnumPropertyRNA)prop;

//			if(get!=null) eprop.get= (PropEnumGetFunc)get;
			if(get!=null) eprop.get= (PropEnumGetFunc)FString.toFunc(get);
//			if(set!=null) eprop.set= (PropEnumSetFunc)set;
			if(set!=null) eprop.set= (PropEnumSetFunc)FString.toFunc(set);
//			if(item!=null) eprop.itemf= (PropEnumItemFunc)item;
			if(item!=null) eprop.itemf= (PropEnumItemFunc)FString.toFunc(item);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_enum_funcs: %s.%s, type is not enum.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_enum_funcs: %s.%s, type is not enum.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_string_funcs(PropertyRNA prop, String get, String length, String set)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_STRING: {
			StringPropertyRNA sprop= (StringPropertyRNA)prop;

//			if(get!=null) sprop.get= (PropStringGetFunc)get;
			if(get!=null) sprop.get= (PropStringGetFunc)FString.toFunc(get);
//			if(length!=null) sprop.length= (PropStringLengthFunc)length;
			if(length!=null) sprop.length= (PropStringLengthFunc)FString.toFunc(length);
//			if(set!=null) sprop.set= (PropStringSetFunc)set;
			if(set!=null) sprop.set= (PropStringSetFunc)FString.toFunc(set);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_string_funcs: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_string_funcs: %s.%s, type is not string.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

static void RNA_def_property_pointer_funcs(PropertyRNA prop, String get, String set, String typef, String poll)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_POINTER: {
			PointerPropertyRNA pprop= (PointerPropertyRNA)prop;

//			if(get!=null) pprop.get= (PropPointerGetFunc)get;
			if(get!=null) pprop.get= (PropPointerGetFunc)FString.toFunc(get);
//			if(set!=null) pprop.set= (PropPointerSetFunc)set;
			if(set!=null) pprop.set= (PropPointerSetFunc)FString.toFunc(set);
//			if(typef!=null) pprop.typef= (PropPointerTypeFunc)typef;
			if(typef!=null) pprop.typef= (PropPointerTypeFunc)FString.toFunc(typef);
//			if(poll!=null) pprop.poll= (PropPointerPollFunc)poll;
			if(poll!=null) pprop.poll= (PropPointerPollFunc)FString.toFunc(poll);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_pointer_funcs: %s.%s, type is not pointer.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_pointer_funcs: %s.%s, type is not pointer.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

static void RNA_def_property_collection_funcs(PropertyRNA prop, String begin, String next, String end, String get, String length, String lookupint, String lookupstring)
{
	StructRNA srna= DefRNA.laststruct;

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_property_*_funcs: only during preprocessing.\n");
		System.err.printf("RNA_def_property_*_funcs: only during preprocessing.\n");
		return;
	}

	switch(prop.type) {
		case RNATypes.PROP_COLLECTION: {
			CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;

//			if(begin!=null) cprop.begin= (PropCollectionBeginFunc)begin;
			if(begin!=null) cprop.begin= (PropCollectionBeginFunc)FString.toFunc(begin);
//			if(next!=null) cprop.next= (PropCollectionNextFunc)next;
			if(next!=null) cprop.next= (PropCollectionNextFunc)FString.toFunc(next);
//			if(end!=null) cprop.end= (PropCollectionEndFunc)end;
			if(end!=null) cprop.end= (PropCollectionEndFunc)FString.toFunc(end);
//			if(get!=null) cprop.get= (PropCollectionGetFunc)get;
			if(get!=null) cprop.get= (PropCollectionGetFunc)FString.toFunc(get);
//			if(length!=null) cprop.length= (PropCollectionLengthFunc)length;
			if(length!=null) cprop.length= (PropCollectionLengthFunc)FString.toFunc(length);
//			if(lookupint!=null) cprop.lookupint= (PropCollectionLookupIntFunc)lookupint;
			if(lookupint!=null) cprop.lookupint= (PropCollectionLookupIntFunc)FString.toFunc(lookupint);
//			if(lookupstring!=null) cprop.lookupstring= (PropCollectionLookupStringFunc)lookupstring;
			if(lookupstring!=null) cprop.lookupstring= (PropCollectionLookupStringFunc)FString.toFunc(lookupstring);
			break;
		}
		default:
//			fprintf(stderr, "RNA_def_property_collection_funcs: %s.%s, type is not collection.\n", srna.identifier, prop.identifier);
			System.err.printf("RNA_def_property_collection_funcs: %s.%s, type is not collection.\n", srna.identifier, prop.identifier);
			DefRNA.error= 1;
			break;
	}
}

public static void RNA_def_property_srna(PropertyRNA prop, String type)
{
//	prop.srna= (StructRNA)type;
	prop.srna= new StructRNA(type);
}

/* Compact definitions */

public static PropertyRNA RNA_def_boolean(Object cont_, String identifier, int default_value, String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_BOOLEAN, RNATypes.PROP_NONE);
	RNA_def_property_boolean_default(prop, default_value);
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

//PropertyRNA *RNA_def_boolean_array(StructOrFunctionRNA *cont_, const char *identifier, int len, int *default_value,
//	const char *ui_name, const char *ui_description)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_BOOLEAN, PROP_NONE);
//	if(len != 0) RNA_def_property_array(prop, len);
//	if(default_value) RNA_def_property_boolean_array_default(prop, default_value);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//
//	return prop;
//}

public static PropertyRNA RNA_def_boolean_vector(Object cont_, String identifier, int len, boolean[] default_value,
	String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_BOOLEAN, RNATypes.PROP_XYZ); // XXX
	if(len != 0) RNA_def_property_array(prop, len);
	if(default_value!=null) RNA_def_property_boolean_array_default(prop, default_value);
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

public static PropertyRNA RNA_def_int(Object cont_, String identifier, int default_value, int hardmin, int hardmax,
	String ui_name, String ui_description, int softmin, int softmax)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_INT, RNATypes.PROP_NONE);
	RNA_def_property_int_default(prop, default_value);
	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
	RNA_def_property_ui_text(prop, ui_name, ui_description);
	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);

	return prop;
}

//PropertyRNA *RNA_def_int_vector(StructOrFunctionRNA *cont_, const char *identifier, int len, const int *default_value,
//	int hardmin, int hardmax, const char *ui_name, const char *ui_description, int softmin, int softmax)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_INT, PROP_VECTOR);
//	if(len != 0) RNA_def_property_array(prop, len);
//	if(default_value) RNA_def_property_int_array_default(prop, default_value);
//	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);
//
//	return prop;
//}

public static PropertyRNA RNA_def_int_array(Object cont_, String identifier, int len, int[] default_value,
	int hardmin, int hardmax, String ui_name, String ui_description, int softmin, int softmax)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;

	prop= RNA_def_property(cont, identifier, RNATypes.PROP_INT, RNATypes.PROP_NONE);
	if(len != 0) RNA_def_property_array(prop, len);
	if(default_value!=null) RNA_def_property_int_array_default(prop, default_value);
	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
	RNA_def_property_ui_text(prop, ui_name, ui_description);
	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);

	return prop;
}

public static PropertyRNA RNA_def_string(Object cont_, String identifier, String default_value, int maxlen, 
		String ui_name, String ui_description)
	{
		ContainerRNA cont= (ContainerRNA)cont_;
		PropertyRNA prop;
		
		prop= RNA_def_property(cont, identifier, RNATypes.PROP_STRING, RNATypes.PROP_NONE);
		if(maxlen != 0) RNA_def_property_string_maxlength(prop, maxlen);
		if(default_value!=null ) RNA_def_property_string_default(prop, default_value);
		RNA_def_property_ui_text(prop, ui_name, ui_description);

		return prop;
	}

public static PropertyRNA RNA_def_string_file_path(Object cont_, String identifier, String default_value, int maxlen,
		String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_STRING, RNATypes.PROP_FILEPATH);
	if(maxlen != 0) RNA_def_property_string_maxlength(prop, maxlen);
	if(default_value!=null) RNA_def_property_string_default(prop, default_value);
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

public static PropertyRNA RNA_def_string_dir_path(Object cont_, String identifier, String default_value, int maxlen,
	String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;

	prop= RNA_def_property(cont, identifier, RNATypes.PROP_STRING, RNATypes.PROP_DIRPATH);
	if(maxlen != 0) RNA_def_property_string_maxlength(prop, maxlen);
	if(default_value!=null) RNA_def_property_string_default(prop, default_value);
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

public static PropertyRNA RNA_def_string_file_name(Object cont_, String identifier, String default_value, int maxlen, 
		String ui_name, String ui_description)
	{
		ContainerRNA cont= (ContainerRNA)cont_;
		PropertyRNA prop;
		
		prop= RNA_def_property(cont, identifier, RNATypes.PROP_STRING, RNATypes.PROP_FILENAME);
		if(maxlen != 0) RNA_def_property_string_maxlength(prop, maxlen);
		if(default_value!=null) RNA_def_property_string_default(prop, default_value);
		RNA_def_property_ui_text(prop, ui_name, ui_description);

		return prop;
	}

public static PropertyRNA RNA_def_enum(Object cont_, String identifier, EnumPropertyItem[] items, int default_value,
	String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;

	if(items==null) {
//		printf("RNA_def_enum: items not allowed to be NULL.\n");
		System.out.printf("RNA_def_enum: items not allowed to be NULL.\n");
		return null;
	}
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	if(items!=null) RNA_def_property_enum_items(prop, items);
	RNA_def_property_enum_default(prop, default_value);
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

public static void RNA_def_enum_funcs(PropertyRNA prop, PropEnumItemFunc itemfunc)
{
	EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
	eprop.itemf= itemfunc;
}

public static PropertyRNA RNA_def_float(Object cont_, String identifier, float default_value,
	float hardmin, float hardmax, String ui_name, String ui_description, float softmin, float softmax)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_FLOAT, RNATypes.PROP_NONE);
	RNA_def_property_float_default(prop, default_value);
	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
	RNA_def_property_ui_text(prop, ui_name, ui_description);
	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);

	return prop;
}

public static PropertyRNA RNA_def_float_vector(Object cont_, String identifier, int len, float[] default_value,
	float hardmin, float hardmax, String ui_name, String ui_description, float softmin, float softmax)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_FLOAT, RNATypes.PROP_XYZ);
	if(len != 0) RNA_def_property_array(prop, len);
	if(default_value!=null) RNA_def_property_float_array_default(prop, default_value);
	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
	RNA_def_property_ui_text(prop, ui_name, ui_description);
	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);

	return prop;
}

//PropertyRNA *RNA_def_float_color(StructOrFunctionRNA *cont_, const char *identifier, int len, const float *default_value,
//	float hardmin, float hardmax, const char *ui_name, const char *ui_description, float softmin, float softmax)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_FLOAT, PROP_COLOR);
//	if(len != 0) RNA_def_property_array(prop, len);
//	if(default_value) RNA_def_property_float_array_default(prop, default_value);
//	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);
//
//	return prop;
//}
//
//
//PropertyRNA *RNA_def_float_matrix(StructOrFunctionRNA *cont_, const char *identifier, int len, const float *default_value,
//	float hardmin, float hardmax, const char *ui_name, const char *ui_description, float softmin, float softmax)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_FLOAT, PROP_MATRIX);
//	if(len != 0) RNA_def_property_array(prop, len);
//	if(default_value) RNA_def_property_float_array_default(prop, default_value);
//	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);
//
//	return prop;
//}

public static PropertyRNA RNA_def_float_rotation(Object cont_, String identifier, int len, float[] default_value,
	float hardmin, float hardmax, String ui_name, String ui_description, float softmin, float softmax)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;
	
	prop= RNA_def_property(cont, identifier, RNATypes.PROP_FLOAT, RNATypes.PROP_EULER); // XXX
	if(len != 0) RNA_def_property_array(prop, len);
	if(default_value!=null) RNA_def_property_float_array_default(prop, default_value);
	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
	RNA_def_property_ui_text(prop, ui_name, ui_description);
	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);

	return prop;
}

//PropertyRNA *RNA_def_float_array(StructOrFunctionRNA *cont_, const char *identifier, int len, const float *default_value,
//	float hardmin, float hardmax, const char *ui_name, const char *ui_description, float softmin, float softmax)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_FLOAT, PROP_NONE);
//	if(len != 0) RNA_def_property_array(prop, len);
//	if(default_value) RNA_def_property_float_array_default(prop, default_value);
//	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);
//
//	return prop;
//}
//
//PropertyRNA *RNA_def_float_percentage(StructOrFunctionRNA *cont_, const char *identifier, float default_value,
//	float hardmin, float hardmax, const char *ui_name, const char *ui_description, float softmin, float softmax)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_FLOAT, PROP_PERCENTAGE);
//	RNA_def_property_float_default(prop, default_value);
//	if(hardmin != hardmax) RNA_def_property_range(prop, hardmin, hardmax);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//	RNA_def_property_ui_range(prop, softmin, softmax, 1, 3);
//
//	return prop;
//}

public static PropertyRNA RNA_def_pointer(Object cont_, String identifier, String type,
	String ui_name, String ui_description)
{
	ContainerRNA cont= (ContainerRNA)cont_;
	PropertyRNA prop;

	prop= RNA_def_property(cont, identifier, RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
	RNA_def_property_struct_type(prop, type.toString());
	RNA_def_property_ui_text(prop, ui_name, ui_description);

	return prop;
}

//public static PropertyRNA RNA_def_pointer_runtime(Object cont_, String identifier, StructRNA type,
//	String ui_name, String ui_description)
//{
////	ContainerRNA *cont= cont_;
//        Object cont= cont_;
//	PropertyRNA prop;
//
//	prop= RNA_def_property(cont, identifier, RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
////	RNA_def_property_struct_runtime(prop, type);
////	RNA_def_property_ui_text(prop, ui_name, ui_description);
//
//	return prop;
//}

//PropertyRNA *RNA_def_collection(StructOrFunctionRNA *cont_, const char *identifier, const char *type,
//	const char *ui_name, const char *ui_description)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_COLLECTION, PROP_NONE);
//	RNA_def_property_struct_type(prop, type);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//
//	return prop;
//}
//
//PropertyRNA *RNA_def_collection_runtime(StructOrFunctionRNA *cont_, const char *identifier, StructRNA *type,
//	const char *ui_name, const char *ui_description)
//{
//	ContainerRNA *cont= cont_;
//	PropertyRNA *prop;
//
//	prop= RNA_def_property(cont, identifier, PROP_COLLECTION, PROP_NONE);
//	RNA_def_property_struct_runtime(prop, type);
//	RNA_def_property_ui_text(prop, ui_name, ui_description);
//
//	return prop;
//}

/* Function */

static FunctionRNA rna_def_function(StructRNA srna, String identifier)
{
	FunctionRNA func;
	StructDefRNA dsrna;
	FunctionDefRNA dfunc;

	if(DefRNA.preprocess!=0) {
//		char error[512];
		String[] error = new String[1];

		if (rna_validate_identifier(identifier, error, 0) == 0) {
//			fprintf(stderr, "RNA_def_function: function identifier \"%s\" - %s\n", identifier, error);
			System.err.printf("RNA_def_function: function identifier \"%s\" - %s\n", identifier, error);
			DefRNA.error= 1;
		}
	}

//	func= MEM_callocN(sizeof(FunctionRNA), "FunctionRNA");
	func= new FunctionRNA();
	func.identifier= identifier;
	func.description= identifier;

	rna_addtail(srna.functions, func);
//	System.out.println("rna_def_function func.prev: "+func.prev);
//	System.out.println("rna_def_function ((Link)func).prev: "+((Link)func).prev);
	
//	System.out.println("rna_def_function: "+func+", prev: "+func.prev+", next: "+func.next);

	if(DefRNA.preprocess!=0) {
		dsrna= rna_find_struct_def(srna);
//		dfunc= MEM_callocN(sizeof(FunctionDefRNA), "FunctionDefRNA");
		dfunc= new FunctionDefRNA();
		rna_addtail(dsrna.functions, dfunc);
		dfunc.func= func;
	}
	else
		func.flag|= RNATypes.FUNC_RUNTIME;

	return func;
}

public static FunctionRNA RNA_def_function(StructRNA srna, String identifier, String call)
{
	FunctionRNA func;
	FunctionDefRNA dfunc;

	func= rna_def_function(srna, identifier);

	if(DefRNA.preprocess==0) {
//		fprintf(stderr, "RNA_def_function: only at preprocess time.\n");
		System.err.printf("RNA_def_function: only at preprocess time.\n");
		return func;
	}

	dfunc= rna_find_function_def(func);
	dfunc.call= call;

	return func;
}
//
//FunctionRNA *RNA_def_function_runtime(StructRNA *srna, const char *identifier, CallFunc call)
//{
//	FunctionRNA *func;
//
//	func= rna_def_function(srna, identifier);
//
//	if(DefRNA.preprocess) {
//		fprintf(stderr, "RNA_def_function_call_runtime: only at runtime.\n");
//		return func;
//	}
//
//	func.call= call;
//
//
//	return func;
//}

/* C return value only!, multiple RNA returns can be done with RNA_def_function_output */
public static void RNA_def_function_return(FunctionRNA func, PropertyRNA ret)
{
	if ((ret.flag & RNATypes.PROP_DYNAMIC)!=0) {
//		fprintf(stderr, "RNA_def_function_return: \"%s.%s\", dynamic values are not allowed as strict returns, use RNA_def_function_output instead.\n", func.identifier, ret.identifier);
		System.err.printf("RNA_def_function_return: \"%s.%s\", dynamic values are not allowed as strict returns, use RNA_def_function_output instead.\n", func.identifier, ret.identifier);
		return;
	}
	else if (ret.arraydimension!=0) {
//		fprintf(stderr, "RNA_def_function_return: \"%s.%s\", arrays are not allowed as strict returns, use RNA_def_function_output instead.\n", func.identifier, ret.identifier);
		System.err.printf("RNA_def_function_return: \"%s.%s\", arrays are not allowed as strict returns, use RNA_def_function_output instead.\n", func.identifier, ret.identifier);
		return;
	}

	func.c_ret= ret;

	RNA_def_function_output(func, ret);
}

public static void RNA_def_function_output(FunctionRNA func, PropertyRNA ret)
{
	ret.flag|= RNATypes.PROP_OUTPUT;
}

public static void RNA_def_function_flag(FunctionRNA func, int flag)
{
	func.flag |= flag;
}

public static void RNA_def_function_ui_description(FunctionRNA func, String description)
{
	func.description= description;
}

public static int rna_parameter_size(PropertyRNA parm)
{
//	PropertyType ptype= parm->type;
//	int len= parm->totarraylength; /* only supports fixed length at the moment */
//
//	if(len > 0) {
//		/* XXX in other parts is mentioned that strings can be dynamic as well */
//		if (parm->flag & PROP_DYNAMIC)
//			return sizeof(void *);
//
//		switch (ptype) {
//			case PROP_BOOLEAN:
//			case PROP_INT:
//				return sizeof(int)*len;
//			case PROP_FLOAT:
//				return sizeof(float)*len;
//			default:
//				break;
//		}
//	}
//	else {
//		switch (ptype) {
//			case PROP_BOOLEAN:
//			case PROP_INT:
//			case PROP_ENUM:
//				return sizeof(int);
//			case PROP_FLOAT:
//				return sizeof(float);
//			case PROP_STRING:
//				/* return  valyes dont store a pointer to the original */
//				if(parm->flag & PROP_THICK_WRAP) {
//					StringPropertyRNA *sparm= (StringPropertyRNA*)parm;
//					return sizeof(char) * sparm->maxlength;
//				} else
//					return sizeof(char *);
//			case PROP_POINTER: {
//#ifdef RNA_RUNTIME
//				if(parm->flag & PROP_RNAPTR)
//					return sizeof(PointerRNA);
//				else
//					return sizeof(void *);
//#else
//				if(parm->flag & PROP_RNAPTR)
//					return sizeof(PointerRNA);
//				else
//					return sizeof(void *);
//#endif
//			}
//			case PROP_COLLECTION:
//				return sizeof(ListBase);
//		}
//	}
//
//	return sizeof(void *);
	return 1;
}

/* this function returns the size of the memory allocated for the parameter,
   useful for instance for memory alignment or for storing additional information */
public static int rna_parameter_size_alloc(PropertyRNA parm)
{
//	int size = rna_parameter_size(parm);
//
//	if (parm->flag & PROP_DYNAMIC)
//		size+= sizeof(((ParameterDynAlloc *)NULL)->array_tot);
//
//	return size;
	return 0;
}

///* Dynamic Enums */
//
//void RNA_enum_item_add(EnumPropertyItem **items, int *totitem, EnumPropertyItem *item)
//{
//	EnumPropertyItem *newitems;
//	int tot= *totitem;
//
//	if(tot == 0) {
//		*items= MEM_callocN(sizeof(EnumPropertyItem)*8, "RNA_enum_items_add");
//	}
//	else if(tot >= 8 && (tot&(tot-1)) == 0){
//		/* power of two > 8 */
//		newitems= MEM_callocN(sizeof(EnumPropertyItem)*tot*2, "RNA_enum_items_add");
//		memcpy(newitems, *items, sizeof(EnumPropertyItem)*tot);
//		MEM_freeN(*items);
//		*items= newitems;
//	}
//
//	(*items)[tot]= *item;
//	*totitem= tot+1;
//}
//
//void RNA_enum_item_add_separator(EnumPropertyItem **items, int *totitem)
//{
//	static EnumPropertyItem sepr = {0, "", 0, NULL, NULL};
//	RNA_enum_item_add(items, totitem, &sepr);
//}
//
//void RNA_enum_items_add(EnumPropertyItem **items, int *totitem, EnumPropertyItem *item)
//{
//	for(; item.identifier; item++)
//		RNA_enum_item_add(items, totitem, item);
//}
//
//void RNA_enum_items_add_value(EnumPropertyItem **items, int *totitem, EnumPropertyItem *item, int value)
//{
//	for(; item.identifier; item++) {
//		if(item.value == value) {
//			RNA_enum_item_add(items, totitem, item);
//			break; // break on first match - does this break anything? (is quick hack to get object.parent_type working ok for armature/lattice)
//		}
//	}
//}
//
//void RNA_enum_item_end(EnumPropertyItem **items, int *totitem)
//{
//	static EnumPropertyItem empty = {0, NULL, 0, NULL, NULL};
//	RNA_enum_item_add(items, totitem, &empty);
//}
//
///* Memory management */
//
//#ifdef RNA_RUNTIME
//void RNA_def_struct_duplicate_pointers(StructRNA *srna)
//{
//	if(srna.identifier) srna.identifier= BLI_strdup(srna.identifier);
//	if(srna.name) srna.name= BLI_strdup(srna.name);
//	if(srna.description) srna.description= BLI_strdup(srna.description);
//
//	srna.flag |= STRUCT_FREE_POINTERS;
//}
//
//void RNA_def_struct_free_pointers(StructRNA *srna)
//{
//	if(srna.flag & STRUCT_FREE_POINTERS) {
//		if(srna.identifier) MEM_freeN((void*)srna.identifier);
//		if(srna.name) MEM_freeN((void*)srna.name);
//		if(srna.description) MEM_freeN((void*)srna.description);
//	}
//}
//
//void RNA_def_func_duplicate_pointers(FunctionRNA *func)
//{
//	if(func.identifier) func.identifier= BLI_strdup(func.identifier);
//	if(func.description) func.description= BLI_strdup(func.description);
//
//	func.flag |= FUNC_FREE_POINTERS;
//}
//
//void RNA_def_func_free_pointers(FunctionRNA *func)
//{
//	if(func.flag & FUNC_FREE_POINTERS) {
//		if(func.identifier) MEM_freeN((void*)func.identifier);
//		if(func.description) MEM_freeN((void*)func.description);
//	}
//}
//
//void RNA_def_property_duplicate_pointers(PropertyRNA *prop)
//{
//	EnumPropertyItem *earray;
//	float *farray;
//	int *iarray;
//
//	if(prop.identifier) prop.identifier= BLI_strdup(prop.identifier);
//	if(prop.name) prop.name= BLI_strdup(prop.name);
//	if(prop.description) prop.description= BLI_strdup(prop.description);
//
//	switch(prop.type) {
//		case PROP_BOOLEAN: {
//			BooleanPropertyRNA *bprop= (BooleanPropertyRNA*)prop;
//
//			if(bprop.defaultarray) {
//				iarray= MEM_callocN(sizeof(int)*prop.arraylength, "RNA_def_property_store");
//				memcpy(iarray, bprop.defaultarray, sizeof(int)*prop.arraylength);
//				bprop.defaultarray= iarray;
//			}
//			break;
//		}
//		case PROP_INT: {
//			IntPropertyRNA *iprop= (IntPropertyRNA*)prop;
//
//			if(iprop.defaultarray) {
//				iarray= MEM_callocN(sizeof(int)*prop.arraylength, "RNA_def_property_store");
//				memcpy(iarray, iprop.defaultarray, sizeof(int)*prop.arraylength);
//				iprop.defaultarray= iarray;
//			}
//			break;
//		}
//		case PROP_ENUM: {
//			EnumPropertyRNA *eprop= (EnumPropertyRNA*)prop;
//
//			if(eprop.item) {
//				earray= MEM_callocN(sizeof(EnumPropertyItem)*(eprop.totitem+1), "RNA_def_property_store"),
//				memcpy(earray, eprop.item, sizeof(EnumPropertyItem)*(eprop.totitem+1));
//				eprop.item= earray;
//			}
//		}
//		case PROP_FLOAT: {
//			FloatPropertyRNA *fprop= (FloatPropertyRNA*)prop;
//
//			if(fprop.defaultarray) {
//				farray= MEM_callocN(sizeof(float)*prop.arraylength, "RNA_def_property_store");
//				memcpy(farray, fprop.defaultarray, sizeof(float)*prop.arraylength);
//				fprop.defaultarray= farray;
//			}
//			break;
//		}
//		case PROP_STRING: {
//			StringPropertyRNA *sprop= (StringPropertyRNA*)prop;
//			if(sprop.defaultvalue) sprop.defaultvalue= BLI_strdup(sprop.defaultvalue);
//			break;
//		}
//		default:
//			break;
//	}
//
//	prop.flag |= PROP_FREE_POINTERS;
//}
//
//void RNA_def_property_free_pointers(PropertyRNA *prop)
//{
//	if(prop.flag & PROP_FREE_POINTERS) {
//		if(prop.identifier) MEM_freeN((void*)prop.identifier);
//		if(prop.name) MEM_freeN((void*)prop.name);
//		if(prop.description) MEM_freeN((void*)prop.description);
//
//		switch(prop.type) {
//			case PROP_BOOLEAN: {
//				BooleanPropertyRNA *bprop= (BooleanPropertyRNA*)prop;
//				if(bprop.defaultarray) MEM_freeN((void*)bprop.defaultarray);
//				break;
//			}
//			case PROP_INT: {
//				IntPropertyRNA *iprop= (IntPropertyRNA*)prop;
//				if(iprop.defaultarray) MEM_freeN((void*)iprop.defaultarray);
//				break;
//			}
//			case PROP_FLOAT: {
//				FloatPropertyRNA *fprop= (FloatPropertyRNA*)prop;
//				if(fprop.defaultarray) MEM_freeN((void*)fprop.defaultarray);
//				break;
//			}
//			case PROP_ENUM: {
//				EnumPropertyRNA *eprop= (EnumPropertyRNA*)prop;
//				if(eprop.item) MEM_freeN((void*)eprop.item);
//			}
//			case PROP_STRING: {
//				StringPropertyRNA *sprop= (StringPropertyRNA*)prop;
//				if(sprop.defaultvalue) MEM_freeN((void*)sprop.defaultvalue);
//				break;
//			}
//			default:
//				break;
//		}
//	}
//}
//#endif
}
