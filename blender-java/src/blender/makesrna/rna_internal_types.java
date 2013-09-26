/**
 * $Id: rna_internal_types.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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

//#ifndef RNA_INTERNAL_TYPES

import blender.blenkernel.bContext;
import blender.makesdna.SDNATypes.SDNA;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesrna.RNATypes.CallFunc;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.StructUnregisterFunc;

//#define RNA_INTERNAL_TYPES
//
//#include "DNA_listBase.h"
//
public class rna_internal_types {
	
	public static final int RNA_MAGIC = ((int)~0);
	
	public static class ContainerDefRNA extends Link {
//		void *next, *prev;

		public ContainerRNA cont;
		public ListBase<PropertyDefRNA> properties = new ListBase<PropertyDefRNA>();
	};
	
	public static class FunctionDefRNA extends ContainerDefRNA {
		public ContainerDefRNA cont = (ContainerDefRNA)this;

		public FunctionRNA func;
//		const char *srna;
		public String call;
		public String gencall;
	};
	
	public static class PropertyDefRNA extends Link<PropertyDefRNA> {
//		struct PropertyDefRNA *next, *prev;

		public ContainerRNA cont;
		public PropertyRNA prop;

		/* struct */
		public String dnastructname;
		public String dnastructfromname;
		public String dnastructfromprop;

		/* property */
		public String dnaname;
		public String dnatype;
		public int dnaarraylength;
		public int dnapointerlevel;

		/* for finding length of array collections */
		public String dnalengthstructname;
		public String dnalengthname;
		public int dnalengthfixed;

		public int booleanbit, booleannegative;

		/* not to be confused with PROP_ENUM_FLAG
		 * this only allows one of the flags to be set at a time, clearing all others */
		public int enumbitflags;
	};
	
	public static class StructDefRNA extends ContainerDefRNA {
//		public ContainerDefRNA cont = new ContainerDefRNA();
		public ContainerDefRNA cont = (ContainerDefRNA)this;

		public StructRNA srna;
		public String filename;

		public String dnaname;

		/* for derived structs to find data in some property */
		public String dnafromname;
		public String dnafromprop;

		public ListBase<FunctionDefRNA> functions = new ListBase<FunctionDefRNA>();
	};
	
	public static class BlenderDefRNA {
		public SDNA sdna;
		public ListBase<StructDefRNA> structs = new ListBase<StructDefRNA>();
		public ListBase allocs = new ListBase();
		public StructRNA laststruct;
		public int error, silent, preprocess, verify;
		public BlenderDefRNA(SDNA sdna, ListBase structs, ListBase allocs, StructRNA laststruct, int error, int silent, int preprocess, int verify) {
			this.sdna = sdna;
			this.structs = structs;
			this.allocs = allocs;
			this.laststruct = laststruct;
			this.error = error;
			this.silent = silent;
			this.preprocess = preprocess;
			this.verify = verify;
		}
	};

//	extern BlenderDefRNA DefRNA;
	
//struct BlenderRNA;
//struct ContainerRNA;
//struct StructRNA;
//struct PropertyRNA;
//struct PointerRNA;
//struct FunctionRNA;
//struct ReportList;
//struct CollectionPropertyIterator;
//struct bContext;
//struct IDProperty;
//struct GHash;
	
	public static interface IteratorSkipFunc extends Func {
		public boolean skip(CollectionPropertyIterator iter, Object data);
	}
	
	public static class ArrayIterator {
		public Object[] array;
//		public String ptr;
		public int ptr;
		public int endptr;
		public Object[] free_ptr; /* will be free'd if set */
		public int itemsize;
		public IteratorSkipFunc skip;
	};

public static final int RNA_MAX_ARRAY = 32;

/* Function Callbacks */

public static interface Func {
//	public String getName();
}

public static interface UpdateFunc extends Func { public void update(bContext C, PointerRNA ptr); };
//typedef void (*ContextUpdateFunc)(struct bContext *C, struct PointerRNA *ptr);
public static interface EditableFunc extends Func { public int edit(PointerRNA ptr); };
public static interface ItemEditableFunc extends Func { public int editItem(PointerRNA ptr, int index); };
//typedef struct IDProperty* (*IDPropertiesFunc)(struct PointerRNA *ptr, int create);
public static interface IDPropertiesFunc extends Func {
    public IDProperty prop(PointerRNA ptr, boolean create);
};
//typedef struct StructRNA *(*StructRefineFunc)(struct PointerRNA *ptr);
public static interface StructRefineFunc extends Func {
    public StructRNA refine(PointerRNA ptr);
};
//typedef char *(*StructPathFunc)(struct PointerRNA *ptr);
//
public static interface PropFunc extends Func {
    public Object getProp(PointerRNA ptr, Object value);
};

public static interface PropRangeFunc extends Func {
    public void range(PointerRNA ptr, Object min, Object max);
};

//public static interface PropCollectionIteratorFunc extends Func {
//    public Object iter(CollectionPropertyIterator iter, Object value);
//};

//typedef int (*PropBooleanGetFunc)(struct PointerRNA *ptr);
public static interface PropBooleanGetFunc extends Func {
	public boolean getBool(PointerRNA ptr);
}
//typedef void (*PropBooleanSetFunc)(struct PointerRNA *ptr, int value);
public static interface PropBooleanSetFunc extends Func {
	public void setBool(PointerRNA ptr, boolean value);
}
//typedef void (*PropBooleanArrayGetFunc)(struct PointerRNA *ptr, int *values);
public static interface PropBooleanArrayGetFunc extends Func {
	public void getBoolArray(PointerRNA ptr, boolean[] values);
}
//typedef void (*PropBooleanArraySetFunc)(struct PointerRNA *ptr, const int *values);
public static interface PropBooleanArraySetFunc extends Func {
	public void setBoolArray(PointerRNA ptr, boolean[] values);
}
//typedef int (*PropIntGetFunc)(struct PointerRNA *ptr);
public static interface PropIntGetFunc extends Func {
	public int getInt(PointerRNA ptr);
}
//typedef void (*PropIntSetFunc)(struct PointerRNA *ptr, int value);
public static interface PropIntSetFunc extends Func {
	public void setInt(PointerRNA ptr, int value);
}
//typedef void (*PropIntArrayGetFunc)(struct PointerRNA *ptr, int *values);
public static interface PropIntArrayGetFunc extends Func {
	public void getIntArray(PointerRNA ptr, int[] values);
}
//typedef void (*PropIntArraySetFunc)(struct PointerRNA *ptr, const int *values);
public static interface PropIntArraySetFunc extends Func {
	public void setIntArray(PointerRNA ptr, int[] values);
}
//typedef void (*PropIntRangeFunc)(struct PointerRNA *ptr, int *min, int *max);
public static interface PropIntRangeFunc extends Func {
	public void rangeInt(PointerRNA ptr, int[] min, int[] max);
}
//typedef float (*PropFloatGetFunc)(struct PointerRNA *ptr);
public static interface PropFloatGetFunc extends Func {
	public float getFloat(PointerRNA ptr);
}
//typedef void (*PropFloatSetFunc)(struct PointerRNA *ptr, float value);
public static interface PropFloatSetFunc extends Func {
	public void setFloat(PointerRNA ptr, float value);
}
//typedef void (*PropFloatArrayGetFunc)(struct PointerRNA *ptr, float *values);
public static interface PropFloatArrayGetFunc extends Func {
	public void getFloatArray(PointerRNA ptr, float[] values);
}
//typedef void (*PropFloatArraySetFunc)(struct PointerRNA *ptr, const float *values);
public static interface PropFloatArraySetFunc extends Func {
	public void setFloatArray(PointerRNA ptr, float[] values);
}
//typedef void (*PropFloatRangeFunc)(struct PointerRNA *ptr, float *min, float *max);
public static interface PropFloatRangeFunc extends Func {
	public void rangeFloat(PointerRNA ptr, float[] min, float[] max);
}
//typedef void (*PropStringGetFunc)(struct PointerRNA *ptr, char *value);
public static interface PropStringGetFunc extends Func {
	public void getStr(PointerRNA ptr, byte[] value, int offset);
}
//typedef int (*PropStringLengthFunc)(struct PointerRNA *ptr);
public static interface PropStringLengthFunc extends Func {
	public int lenStr(PointerRNA ptr);
}
//typedef void (*PropStringSetFunc)(struct PointerRNA *ptr, const char *value);
public static interface PropStringSetFunc extends Func {
	public void setStr(PointerRNA ptr, byte[] value, int offset);
}
//typedef int (*PropEnumGetFunc)(struct PointerRNA *ptr);
public static interface PropEnumGetFunc extends Func {
	public int getEnum(PointerRNA ptr);
}
//typedef void (*PropEnumSetFunc)(struct PointerRNA *ptr, int value);
public static interface PropEnumSetFunc extends Func {
	public void setEnum(PointerRNA ptr, int value);
}
//typedef EnumPropertyItem *(*PropEnumItemFunc)(struct bContext *C, struct PointerRNA *ptr, int *free);
public static interface PropEnumItemFunc extends Func {
	public EnumPropertyItem[] itemEnum(bContext C, PointerRNA ptr, int[] free);
}
//typedef PointerRNA (*PropPointerGetFunc)(struct PointerRNA *ptr);
public static interface PropPointerGetFunc extends Func {
	public PointerRNA getPtr(PointerRNA ptr);
}
//typedef StructRNA* (*PropPointerTypeFunc)(struct PointerRNA *ptr);
public static interface PropPointerTypeFunc extends Func {
	public StructRNA typePtr(PointerRNA ptr);
}
//typedef void (*PropPointerSetFunc)(struct PointerRNA *ptr, const PointerRNA value);
public static interface PropPointerSetFunc extends Func {
	public void setPtr(PointerRNA ptr, PointerRNA value);
}
//typedef int (*PropPointerPollFunc)(struct PointerRNA *ptr, const PointerRNA value);
public static interface PropPointerPollFunc extends Func {
	public int pollPtr(PointerRNA ptr, PointerRNA value);
}
//typedef void (*PropCollectionBeginFunc)(struct CollectionPropertyIterator *iter, struct PointerRNA *ptr);
public static interface PropCollectionBeginFunc extends Func {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr);
}
//typedef void (*PropCollectionNextFunc)(struct CollectionPropertyIterator *iter);
public static interface PropCollectionNextFunc extends Func {
	public void next(CollectionPropertyIterator iter);
}
//typedef void (*PropCollectionEndFunc)(struct CollectionPropertyIterator *iter);
public static interface PropCollectionEndFunc extends Func {
	public void end(CollectionPropertyIterator iter);
}
//typedef PointerRNA (*PropCollectionGetFunc)(struct CollectionPropertyIterator *iter);
public static interface PropCollectionGetFunc extends Func {
	public PointerRNA get(CollectionPropertyIterator iter);
}
//typedef int (*PropCollectionLengthFunc)(struct PointerRNA *ptr);
public static interface PropCollectionLengthFunc extends Func {
	public int length(PointerRNA ptr);
}
//typedef PointerRNA (*PropCollectionLookupIntFunc)(struct PointerRNA *ptr, int key);
public static interface PropCollectionLookupIntFunc extends Func {
	public PointerRNA lookupInt(PointerRNA ptr, int key);
}
//typedef PointerRNA (*PropCollectionLookupStringFunc)(struct PointerRNA *ptr, const char *key);
public static interface PropCollectionLookupStringFunc extends Func {
	public PointerRNA lookupStr(PointerRNA ptr, String key);
}

/* Container - generic abstracted container of RNA properties */
public static class ContainerRNA extends Link {
//	Object next, prev;

//	GHash prophash;
	public Object prophash;
	public ListBase properties = new ListBase();
	public ContainerRNA() { }
	public ContainerRNA(Object next, Object prev, Object prophash, Object propertiesFirst, Object propertiesLast) {
		this.next = next;
		this.prev = prev;
		this.prophash = prophash;
		properties.first = propertiesFirst;
		properties.last = propertiesLast;
	}
};

//public static class FunctionRNA extends ContainerRNA {
public static class FunctionRNA extends StructRNA {
	/* structs are containers of properties */
//	public ContainerRNA cont = (ContainerRNA)this;

	/* unique identifier */
//	public String identifier;
	/* various options */
//	public int flag;

	/* single line description, displayed in the tooltip for example */
//	public String description;

	/* callback to execute the function */
	public CallFunc call;

	/* parameter for the return value
	 * note: this is only the C return value, rna functions can have multiple return values */
	public PropertyRNA c_ret;
	
	public FunctionRNA() { }
	public FunctionRNA(String identifier) {
		this.identifier = identifier;
	}
	public FunctionRNA(Object next, Object prev, Object prophash, Object propertiesFirst, Object propertiesLast,
			String identifier, int flag, String description, CallFunc call, PropertyRNA c_ret) {
//		super(next, prev, prophash, propertiesFirst, propertiesLast);
		super.next = next;
		super.prev = prev;
		super.prophash = prophash;
		super.cont.properties.first = propertiesFirst;
		super.cont.properties.last = propertiesLast;
		this.identifier = identifier;
		this.flag = flag;
		this.description = description;
		this.call = call;
		this.c_ret = c_ret;
	}
	
	public String toString() {
		return "identifier: "+identifier;
	}
};

//public static class PropertyRNA extends IDProperty {
public static class PropertyRNA extends Link<PropertyRNA> {
//	struct PropertyRNA *next, *prev;

	/* magic bytes to distinguish with IDProperty */
	public int magic;

	/* unique identifier */
	public String identifier;
	/* various options */
	public int flag;

	/* user readable name */
	public String name;
	/* single line description, displayed in the tooltip for example */
	public String description;
	/* icon ID */
	public int icon;

	/* property type as it appears to the outside */
	public int type;
	/* subtype, 'interpretation' of the property */
	public int subtype;
	/* if non-NULL, overrides arraylength. Must not return 0? */
//	PropArrayLengthGetFunc getlength;
//	public Object getlength = new String("getlength");
	public Object getlength;
	/* dimension of array */
	public int arraydimension;
	/* array lengths lengths for all dimensions (when arraydimension > 0) */
	public int[] arraylength = new int[RNATypes.RNA_MAX_ARRAY_DIMENSION];
	public int totarraylength;
	
//	/* callback for updates on change */
	public UpdateFunc update;
//	public Object update;
	public int noteflag;

//	/* callback for testing if editable */
//	EditableFunc editable;
//	public Object editable = new String("editable");
	public EditableFunc editable;
//	/* callback for testing if array-item editable (if applicable) */
//	ItemEditableFunc itemeditable;
//	public Object itemeditable = new String("itemeditable");
	public Object itemeditable;

	/* raw access */
	public int rawoffset;
//	RawPropertyType rawtype;
	public int rawtype;

	/* This is used for accessing props/functions of this property
	 * any property can have this but should only be used for collections and arrays
	 * since python will convert int/bool/pointer's */
	public StructRNA srna;	/* attributes attached directly to this collection */
	
	public PropertyRNA() { }
	public PropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna) {
		this.next = next;
		this.prev = prev;
		this.magic = magic;
		this.identifier = identifier;
		this.flag = flag;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.type = type;
		this.subtype = subtype;
		this.getlength = getlength;
		this.arraydimension = arraydimension;
		this.arraylength = arraylength;
		this.totarraylength = totarraylength;
		this.update = update;
		this.noteflag = noteflag;
		this.editable = editable;
		this.itemeditable = itemeditable;
		this.rawoffset = rawoffset;
		this.rawtype = rawtype;
		this.srna = srna;
	}
};

/* Property Types */

public static class BooleanPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropBooleanGetFunc get;
//	public Object get;
	public PropBooleanSetFunc set;
//	public Object set;

	public PropBooleanArrayGetFunc getarray;
//	public Object getarray;
	public PropBooleanArraySetFunc setarray;
//	public Object setarray;

	public boolean defaultvalue;
	public boolean[] defaultarray;
	
	public BooleanPropertyRNA() { }
	public BooleanPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropBooleanGetFunc get, PropBooleanSetFunc set, PropBooleanArrayGetFunc getarray, PropBooleanArraySetFunc setarray, boolean defaultvalue, boolean[] defaultarray) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.set = set;
		this.getarray = getarray;
		this.setarray = setarray;
		this.defaultvalue = defaultvalue;
		this.defaultarray = defaultarray;
	}
};

public static class IntPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropIntGetFunc get;
//	public Object get;
	public PropIntSetFunc set;
//	public Object set;

	public PropIntArrayGetFunc getarray;
//	public Object getarray;
	public PropIntArraySetFunc setarray;
//	public Object setarray;

	public PropIntRangeFunc range;
//	public Object range;

	public int softmin, softmax;
	public int hardmin, hardmax;
	public int step;

	public int defaultvalue;
	public int[] defaultarray;
	
	public IntPropertyRNA() { }
	public IntPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropIntGetFunc get, PropIntSetFunc set, PropIntArrayGetFunc getarray, PropIntArraySetFunc setarray, PropIntRangeFunc range,
			int softmin, int softmax, int hardmin, int hardmax, int step, int defaultvalue, int[] defaultarray) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.set = set;
		this.getarray = getarray;
		this.setarray = setarray;
		this.range = range;
		this.softmin = softmin;
		this.softmax = softmax;
		this.hardmin = hardmin;
		this.hardmax = hardmax;
		this.step = step;
		this.defaultvalue = defaultvalue;
		this.defaultarray = defaultarray;
	}
};

public static class FloatPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropFloatGetFunc get;
//	public Object get;
	public PropFloatSetFunc set;
//	public Object set;

	public PropFloatArrayGetFunc getarray;
//	public Object getarray;
	public PropFloatArraySetFunc setarray;
//	public Object setarray;

	public PropFloatRangeFunc range;
//	public Object range;

	public float softmin, softmax;
	public float hardmin, hardmax;
	public float step;
	public int precision;

	public float defaultvalue;
	public float[] defaultarray;
	
	public FloatPropertyRNA() { }
	public FloatPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropFloatGetFunc get, PropFloatSetFunc set, PropFloatArrayGetFunc getarray, PropFloatArraySetFunc setarray, PropFloatRangeFunc range,
			float softmin, float softmax, float hardmin, float hardmax, float step, int precision, float defaultvalue, float[] defaultarray) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.set = set;
		this.getarray = getarray;
		this.setarray = setarray;
		this.range = range;
		this.softmin = softmin;
		this.softmax = softmax;
		this.hardmin = hardmin;
		this.hardmax = hardmax;
		this.step = step;
		this.precision = precision;
		this.defaultvalue = defaultvalue;
		this.defaultarray = defaultarray;
	}
};

public static class StringPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

//	PropStringGetFunc get;
	public PropStringGetFunc get;
//	PropStringLengthFunc length;
	public PropStringLengthFunc length;
//	PropStringSetFunc set;
	public PropStringSetFunc set;

	public int maxlength;	/* includes string terminator! */

	public String defaultvalue;
	
	public StringPropertyRNA() { }
	public StringPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropStringGetFunc get, PropStringLengthFunc length, PropStringSetFunc set, int maxlength, String defaultvalue) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.length = length;
		this.set = set;
		this.maxlength = maxlength;
		this.defaultvalue = defaultvalue;
	}
};

public static class EnumPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropEnumGetFunc get;
//	public Object get;
	public PropEnumSetFunc set;
//	public Object set;
	public PropEnumItemFunc itemf;
//	public Object itemf;

	public EnumPropertyItem[] item;
	public int totitem;

	public int defaultvalue;
	
	public EnumPropertyRNA() { }
	public EnumPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropEnumGetFunc get, PropEnumSetFunc set, PropEnumItemFunc itemf, EnumPropertyItem[] item, int totitem, int defaultvalue) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.set = set;
		this.itemf = itemf;
		this.item = item;
		this.totitem = totitem;
		this.defaultvalue = defaultvalue;
	}
};

public static class PointerPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropPointerGetFunc get;
//	public Object get;
	public PropPointerSetFunc set;
//	public Object set;
	public PropPointerTypeFunc typef;
//	public Object typef;
	public PropPointerPollFunc poll; /* unlike operators, 'set' can still run if poll fails, used for filtering display */
//	public Object poll;

	public StructRNA type;
	
	public PointerPropertyRNA() { }
	public PointerPropertyRNA(
			PropertyRNA next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropPointerGetFunc get, PropPointerSetFunc set, PropPointerTypeFunc typef, PropPointerPollFunc poll, StructRNA type_rna) {
		super(next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.get = get;
		this.set = set;
		this.typef = typef;
		this.poll = poll;
		this.type = type_rna;
	}
};

public static class CollectionPropertyRNA extends PropertyRNA {
	public PropertyRNA property = (PropertyRNA)this;

	public PropCollectionBeginFunc begin;
//	public Object begin;
	public PropCollectionNextFunc next;
//	public Object next;
	public PropCollectionEndFunc end;                      /* optional */
//	public Object end;
	public PropCollectionGetFunc get;
//	public Object get;
	public PropCollectionLengthFunc length;			/* optional */
//	public Object length;
	public PropCollectionLookupIntFunc lookupint;		/* optional */
//	public Object lookupint;
	public PropCollectionLookupStringFunc lookupstring;	/* optional */
//	public Object lookupstring;

	public StructRNA item_type;
	
	public CollectionPropertyRNA() { }
	public CollectionPropertyRNA(
			PropertyRNA _next, PropertyRNA prev,
			int magic, String identifier, int flag, String name,
			String description,
			int icon,
			int type, int subtype, Object getlength, int arraydimension, int[] arraylength, int totarraylength,
			UpdateFunc update, int noteflag, EditableFunc editable, Object itemeditable,
			int rawoffset, int rawtype, StructRNA srna,
			PropCollectionBeginFunc begin, PropCollectionNextFunc next, PropCollectionEndFunc end, PropCollectionGetFunc get, PropCollectionLengthFunc length, PropCollectionLookupIntFunc lookupint, PropCollectionLookupStringFunc lookupstring, StructRNA item_type) {
		super(_next, prev, magic, identifier, flag, name,
			description,
			icon,
			type, subtype, getlength, arraydimension, arraylength, totarraylength,
			update, noteflag, editable, itemeditable,
			rawoffset, rawtype, srna);
		this.begin = begin;
		this.next = next;
		this.end = end;
		this.get = get;
		this.length = length;
		this.lookupint = lookupint;
		this.lookupstring = lookupstring;
		this.item_type = item_type;
	}
};


/* changes to this struct require updating rna_generate_struct in makesrna.c */
public static class StructRNA extends ContainerRNA {
	/* structs are containers of properties */
	public ContainerRNA cont = (ContainerRNA)this;

	/* python type, this is a subtype of pyrna_struct_Type but used so each struct can have its own type
	 * which is useful for subclassing RNA */
	public Object py_type;
	public Object blender_type;

	/* unique identifier */
	public String identifier;
	/* various options */
	public int flag;

	/* user readable name */
	public String name;
	/* single line description, displayed in the tooltip for example */
	public String description;
	/* icon ID */
	public int icon;

	/* property that defines the name */
	public PropertyRNA nameproperty;

	/* property to iterate over properties */
	public PropertyRNA iteratorproperty;

	/* struct this is derivedfrom */
	public StructRNA base;

	/* only use for nested structs, where both the parent and child access
	 * the same C Struct but nesting is used for grouping properties.
	 * The parent property is used so we know NULL checks are not needed,
	 * and that this struct will never exist without its parent */
	public StructRNA nested;

	/* function to give the more specific type */
	public StructRefineFunc refine;

//	/* function to find path to this struct in an ID */
//	StructPathFunc path;
	public Object path;

	/* function to register/unregister subclasses */
	public StructRegisterFunc reg;
	public StructUnregisterFunc unreg;

//	/* callback to get id properties */
//	IDPropertiesFunc idproperties;
	public IDPropertiesFunc idproperties;

	/* functions of this struct */
	public ListBase functions = new ListBase();

//        // TMP
//        public Map<String, Object> attr = new HashMap<String, Object>();
	public StructRNA() {
		
	}
	public StructRNA(StructRegisterFunc reg) {
		this.reg = reg;
	}
	public StructRNA(String identifier) {
		this.identifier = identifier;
	}
	public StructRNA(Object next, Object prev, Object prophash, Object propertiesFirst, Object propertiesLast,
			Object py_type, Object blender_type, String identifier, int flag, String name, String description,
			int icon, PropertyRNA nameproperty, PropertyRNA iteratorproperty, StructRNA base, StructRNA nested,
			StructRefineFunc refine, Object path, StructRegisterFunc reg, StructUnregisterFunc unreg, IDPropertiesFunc idproperties,
			Object functionsFirst, Object functionsLast) {
		super(next, prev, prophash, propertiesFirst, propertiesLast);
		this.py_type = py_type;
		this.blender_type = blender_type;
		this.identifier = identifier;
		this.flag = flag;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.nameproperty = nameproperty;
		this.iteratorproperty = iteratorproperty;
		this.base = base;
		this.nested = nested;
		this.refine = refine;
		this.path = path;
		this.reg = reg;
		this.unreg = unreg;
		this.idproperties = idproperties;
		functions.first = functionsFirst;
		functions.last = functionsLast;
	}
	
	public String toString() {
		return identifier;
	}
	
//	public void copy(StructRNA srna) {
//		
//	}
};

/* Blender RNA
 *
 * Root RNA data structure that lists all struct types. */

public static class BlenderRNA {
	public ListBase<StructRNA> structs = new ListBase<StructRNA>();
};

//#endif /* RNA_INTERNAL_TYPES */

}