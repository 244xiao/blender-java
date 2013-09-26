/**
 * $Id: RNATypes.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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

//#ifndef RNA_TYPES

import java.util.HashMap;
import java.util.Map;

import org.python.core.PyObject;

import blender.blenkernel.bContext;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ReportList;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

//#define RNA_TYPES
//
//#ifdef __cplusplus
//extern "C" {
//#endif
//
//struct ParameterList;
//struct FunctionRNA;
//struct PropertyRNA;
//struct StructRNA;
//struct BlenderRNA;
//struct IDProperty;
//struct bContext;
//struct ReportList;
//
public class RNATypes {
	
	public static final int RNA_MAX_ARRAY_DIMENSION = 3;

/* Pointer
 *
 * RNA pointers are not a single C pointer but include the type,
 * and a pointer to the ID struct that owns the struct, since
 * in some cases this information is needed to correctly get/set
 * the properties and validate them. */

public static class PointerRNA extends PyObject {
    public static class IDPointer {
        public Object data;
    }
//	struct {
//		void *data;
//	} id;
    public IDPointer id = new IDPointer();

	public StructRNA type;
	public Object data;

    // TMP
    public Map<String, Object> attr = new HashMap<String, Object>();
       
    public PointerRNA() {
    	
    }
    public PointerRNA(StructRNA type, Object data) {
    	this.type = type;
    	this.data = data;
    }
};

///* Property */
//
//typedef enum PropertyType {
	public static final int PROP_BOOLEAN = 0;
	public static final int PROP_INT = 1;
	public static final int PROP_FLOAT = 2;
	public static final int PROP_STRING = 3;
	public static final int PROP_ENUM = 4;
	public static final int PROP_POINTER = 5;
	public static final int PROP_COLLECTION = 6;
//} PropertyType;
	
	/* also update rna_property_subtype_unit when you change this */
//	typedef enum PropertyUnit {
	public static final int PROP_UNIT_NONE = (0<<16);
	public static final int PROP_UNIT_LENGTH = (1<<16);			/* m */
	public static final int PROP_UNIT_AREA = (2<<16);			/* m^2 */
	public static final int PROP_UNIT_VOLUME = (3<<16);			/* m^3 */
	public static final int PROP_UNIT_MASS = (4<<16);			/* kg */
	public static final int PROP_UNIT_ROTATION = (5<<16);		/* radians */
	public static final int PROP_UNIT_TIME = (6<<16);			/* frame */
	public static final int PROP_UNIT_VELOCITY = (7<<16);		/* m/s */
	public static final int PROP_UNIT_ACCELERATION = (8<<16);	/* m/(s^2) */
//	} PropertyUnit;
	
	public static final int RNA_SUBTYPE_UNIT(int subtype) { return subtype & 0x00FF0000; }
//	#define RNA_SUBTYPE_VALUE(subtype) ((subtype) & ~0x00FF0000)
//	#define RNA_SUBTYPE_UNIT_VALUE(subtype) ((subtype)>>16)
//
//	#define RNA_ENUM_BITFLAG_SIZE 32

//typedef enum PropertySubType {
//	public static final int PROP_NONE = 0;
//	public static final int PROP_UNSIGNED = 1;
//	public static final int PROP_FILEPATH = 2;
//	public static final int PROP_DIRPATH = 3;
//	public static final int PROP_COLOR = 4;
//	public static final int PROP_VECTOR = 5;
//	public static final int PROP_MATRIX = 6;
//	public static final int PROP_ROTATION = 7;
//	public static final int PROP_NEVER_NULL = 8;
//	public static final int PROP_PERCENTAGE = 9;
//} PropertySubType;
	/* also update enums in bpy_props.c when adding items here */
//	typedef enum PropertySubType {
	public static final int PROP_NONE = 0;

		/* strings */
	public static final int PROP_FILEPATH = 1;
	public static final int PROP_DIRPATH = 2;
	public static final int PROP_FILENAME = 3;

		/* numbers */
	public static final int PROP_UNSIGNED = 13;
	public static final int PROP_PERCENTAGE = 14;
	public static final int PROP_FACTOR = 15;
	public static final int PROP_ANGLE = 16|PROP_UNIT_ROTATION;
	public static final int PROP_TIME = 17|PROP_UNIT_TIME;
	public static final int PROP_DISTANCE = 18|PROP_UNIT_LENGTH;

		/* number arrays */
	public static final int PROP_COLOR = 20;
	public static final int PROP_TRANSLATION = 21|PROP_UNIT_LENGTH;
	public static final int PROP_DIRECTION = 22;
	public static final int PROP_VELOCITY = 23|PROP_UNIT_VELOCITY;
	public static final int PROP_ACCELERATION = 24|PROP_UNIT_ACCELERATION;
	public static final int PROP_MATRIX = 25;
	public static final int PROP_EULER = 26|PROP_UNIT_ROTATION;
	public static final int PROP_QUATERNION = 27;
	public static final int PROP_AXISANGLE = 28;
	public static final int PROP_XYZ = 29;
	public static final int PROP_XYZ_LENGTH = 29|PROP_UNIT_LENGTH;
	public static final int PROP_COLOR_GAMMA = 30;

		/* booleans */
	public static final int PROP_LAYER = 40;
	public static final int PROP_LAYER_MEMBER = 41;
//	} PropertySubType;
//
//typedef enum PropertyFlag {
//	/* editable means the property is editable in the user
//	 * interface, properties are editable by default except
//	 * for pointers and collections. */
//	public static final int PROP_EDITABLE = 1;
//
//	/* this property is editable even if it is lib linked,
//	 * meaning it will get lost on reload, but it's useful
//	 * for editing. */
//	public static final int PROP_LIB_EXCEPTION = 65536;
//
//	/* animateable means the property can be driven by some
//	 * other input, be it animation curves, expressions, ..
//	 * properties are animateable by default except for pointers
//	 * and collections */
//	public static final int PROP_ANIMATEABLE = 2;
//
//	/* icon */
//	public static final int PROP_ICONS_CONSECUTIVE = 4096;
//
//	/* function paramater flags */
//	public static final int PROP_REQUIRED = 4;
//	public static final int PROP_RETURN = 8;
//	public static final int PROP_RNAPTR = 2048;
//
//	/* registering */
//	public static final int PROP_REGISTER = 16;
//	public static final int PROP_REGISTER_OPTIONAL = 16|32;
//
//	/* pointers */
//	public static final int PROP_ID_REFCOUNT = 64;
//
//	/* internal flags */
//	public static final int PROP_BUILTIN = 128;
//	public static final int PROP_EXPORT = 256;
//	public static final int PROP_RUNTIME = 512;
//	public static final int PROP_IDPROPERTY = 1024;
//	public static final int PROP_RAW_ACCESS = 8192;
//	public static final int PROP_RAW_ARRAY = 16384;
//	public static final int PROP_FREE_POINTERS = 32768;
////} PropertyFlag;
	/* Make sure enums are updated with thses */
//	typedef enum PropertyFlag {
		/* editable means the property is editable in the user
		 * interface, properties are editable by default except
		 * for pointers and collections. */
	public static final int PROP_EDITABLE = 1<<0;

		/* this property is editable even if it is lib linked,
		 * meaning it will get lost on reload, but it's useful
		 * for editing. */
	public static final int PROP_LIB_EXCEPTION = 1<<16;

		/* animateable means the property can be driven by some
		 * other input, be it animation curves, expressions, ..
		 * properties are animateable by default except for pointers
		 * and collections */
	public static final int PROP_ANIMATABLE = 1<<1;

		/* icon */
	public static final int PROP_ICONS_CONSECUTIVE = 1<<12;

		/* hidden in  the user interface */
	public static final int PROP_HIDDEN = 1<<19;

		/* function paramater flags */
	public static final int PROP_REQUIRED = 1<<2;
	public static final int PROP_OUTPUT = 1<<3;
	public static final int PROP_RNAPTR = 1<<11;
		/* registering */
	public static final int PROP_REGISTER = 1<<4;
	public static final int PROP_REGISTER_OPTIONAL = (1<<4)|(1<<5);

		/* pointers */
	public static final int PROP_ID_REFCOUNT = 1<<6;

		/* disallow assigning a variable to its self, eg an object tracking its self
		 * only apply this to types that are derived from an ID ()*/
	public static final int PROP_ID_SELF_CHECK = 1<<20;
	public static final int PROP_NEVER_NULL = 1<<18;

		/* flag contains multiple enums.
		 * note: not to be confused with prop->enumbitflags
		 * this exposes the flag as multiple options in python and the UI.
		 *
		 * note: these can't be animated so use with care.
		  */
	public static final int PROP_ENUM_FLAG = 1<<21;

		/* need context for update function */
	public static final int PROP_CONTEXT_UPDATE = 1<<22;

		/* Use for arrays or for any data that should not have a referene kept
		 * most common case is functions that return arrays where the array */
	public static final int PROP_THICK_WRAP = 1<<23;

		/* internal flags */
	public static final int PROP_BUILTIN = 1<<7;
	public static final int PROP_EXPORT = 1<<8;
	public static final int PROP_RUNTIME = 1<<9;
	public static final int PROP_IDPROPERTY = 1<<10;
	public static final int PROP_RAW_ACCESS = 1<<13;
	public static final int PROP_RAW_ARRAY = 1<<14;
	public static final int PROP_FREE_POINTERS = 1<<15;
	public static final int PROP_DYNAMIC = 1<<17; /* for dynamic arrays, and retvals of type string */
	public static final int PROP_ENUM_NO_CONTEXT = 1<<24; /* for enum that shouldn't be contextual */
//	} PropertyFlag;

public static class CollectionPropertyIterator {
	/* internal */
	public PointerRNA parent;
	public PointerRNA builtin_parent;
	public PropertyRNA prop;
	public Object internal;
	public int idprop;
	public int level;

	/* external */
	public boolean valid;
	public PointerRNA ptr;

    public void clear() {
        parent = null;
        builtin_parent = null;
        prop = null;
        internal = null;
        idprop = 0;
        level = 0;
        valid = false;
        ptr = null;
    }
};

public static class CollectionPointerLink extends Link<CollectionPointerLink> {
	public PointerRNA ptr = new PointerRNA();
};

//typedef enum RawPropertyType {
	public static final int PROP_RAW_UNSET=-1;
	public static final int PROP_RAW_INT=0; // XXX - abused for types that are not set, eg. MFace.verts, needs fixing.
	public static final int PROP_RAW_SHORT=1;
	public static final int PROP_RAW_CHAR=2;
	public static final int PROP_RAW_DOUBLE=3;
	public static final int PROP_RAW_FLOAT=4;
//} RawPropertyType;

//typedef struct RawArray {
//	void *array;
//	RawPropertyType type;
//	int len;
//	int stride;
//} RawArray;

public static class EnumPropertyItem {
	public int value;
	public String identifier;
	public int icon;
	public String name;
	public String description;
    public EnumPropertyItem(int value, String identifier, Object icon, String name, String descr) {
        this.value = value;
        this.identifier = identifier;
        this.icon = (icon instanceof BIFIconID)?((BIFIconID)icon).ordinal():(Integer)icon;
        this.name = name;
        this.description = descr;
    }
    
    public static int valueOf(EnumPropertyItem[] e, String s) {
    	for (EnumPropertyItem i : e) {
    		if (i.identifier.equals(s)) {
    			return i.value;
    		}
    	}
    	return -1;
    }
    
    public static String name(EnumPropertyItem[] e, int n) {
    	for (EnumPropertyItem i : e) {
    		if (i.value == n) {
    			return i.identifier;
    		}
    	}
    	return null;
    }
};

//typedef EnumPropertyItem *(*EnumPropertyItemFunc)(struct bContext *C, PointerRNA *ptr, int *free);
//public static interface EnumPropertyItemFunc {
//	public EnumPropertyItem run(bContext C, PointerRNA ptr, int[] free);
//}

//typedef struct PropertyRNA PropertyRNA;

/* Parameter List */

public static class ParameterList {
	/* storage for parameters */
	public Object[] data;

	/* store the parameter count */
	public int tot;

	/* function passed at creation time */
	public FunctionRNA func;
};

public static class ParameterIterator {
	public ParameterList parms;
	public PointerRNA funcptr = new PointerRNA();
//	public Object data;
	public int size, offset;

	public PropertyRNA parm;
	public boolean valid;
	
	public void data(Object value) { parms.data[offset] = value; }
	public Object data() { return parms.data[offset]; }
};

/* Function */

//typedef enum FunctionFlag {
	public static final int FUNC_NO_SELF = 1; /* for static functions */
	public static final int FUNC_USE_CONTEXT = 2;
	public static final int FUNC_USE_REPORTS = 4;
	public static final int FUNC_USE_SELF_ID = 2048;

	/* registering */
	public static final int FUNC_REGISTER = 8;
	public static final int FUNC_REGISTER_OPTIONAL = 8|16;

	/* internal flags */
	public static final int FUNC_BUILTIN = 128;
	public static final int FUNC_EXPORT = 256;
	public static final int FUNC_RUNTIME = 512;
	public static final int FUNC_FREE_POINTERS = 1024;
//} FunctionFlag;

//typedef void (*CallFunc)(struct bContext *C, struct ReportList *reports, PointerRNA *ptr, ParameterList *parms);
public static interface CallFunc {
	public void call(bContext C, ReportList reports, PointerRNA ptr, ParameterList parms);
}
//
//typedef struct FunctionRNA FunctionRNA;

/* Struct */

//	typedef enum StructFlag {
		/* indicates that this struct is an ID struct, and to use refcounting */
	public static final int STRUCT_ID = 1;
	public static final int STRUCT_ID_REFCOUNT = 2;

		/* internal flags */
	public static final int STRUCT_RUNTIME = 4;
	public static final int STRUCT_GENERATED = 8;
	public static final int STRUCT_FREE_POINTERS = 16;
	public static final int STRUCT_NO_IDPROPERTIES = 32; /* Menu's and Panels don't need properties */
//	} StructFlag;

//typedef int (*StructValidateFunc)(struct PointerRNA *ptr, void *data, int *have_function);
public static interface StructValidateFunc {
    public int run(PointerRNA ptr, Object data, int[] have_function);
}
//typedef int (*StructCallbackFunc)(struct PointerRNA *ptr, struct FunctionRNA *func, ParameterList *list);
public static interface StructCallbackFunc {
//    public int run(PointerRNA ptr, FunctionRNA func, ParameterList list);
	public int run(bContext C, PointerRNA ptr, FunctionRNA func, ParameterList list);
}
//typedef void (*StructFreeFunc)(void *data);
public static interface StructFreeFunc {
    public void run(Object data);
}
//typedef struct StructRNA *(*StructRegisterFunc)(struct bContext *C, struct ReportList *reports, void *data,
//		const char *identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free);
public static interface StructRegisterFunc {
    public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free);
    public String toString();
}
//typedef void (*StructUnregisterFunc)(const struct bContext *C, struct StructRNA *type);
public static interface StructUnregisterFunc {
    public void run(bContext C, StructRNA type);
    public String toString();
}
//
//typedef struct StructRNA StructRNA;
//
///* Blender RNA
// *
// * Root RNA data structure that lists all struct types. */
//
//typedef struct BlenderRNA BlenderRNA;

/* Extending
 *
 * This struct must be embedded in *Type structs in
 * order to make then definable through RNA. */

public static class ExtensionRNA {
	public Object data;
	public StructRNA srna;

//	public int (*call)(PointerRNA *, FunctionRNA *, ParameterList *);
        public StructCallbackFunc call;
//	public void (*free)(void *data);
        public StructFreeFunc free;
};

//#ifdef __cplusplus
//}
//#endif
//
//#endif /* RNA_TYPES */
}

