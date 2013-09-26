/**
 * $Id: Transform.java,v 1.1 2009/09/18 05:15:09 jladere Exp $
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
 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
 * All rights reserved.
 *
 * The Original Code is: all of this file.
 *
 * Contributor(s): none yet.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.transform;

//#include <stdlib.h>

import javax.media.opengl.GL2;

import blender.blenkernel.Pointer;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenlib.Arithb;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.GlUtil;
import blender.editors.space_api.SpaceTypeUtil;
import blender.editors.space_api.SpaceTypeUtil.RegionDrawCB;
import blender.editors.space_view3d.View3dView;
import blender.editors.transform.Transform.TransInfo.TransformFunc;
import blender.editors.uinterface.Resources;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.ToolSettings;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bConstraint;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmOperator;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmEvent;

//#include <stdio.h>
//#include <string.h>
//#include <math.h>
//#include <float.h>
//
//#ifdef HAVE_CONFIG_H
//#include <config.h>
//#endif
//
//#ifndef WIN32
//#include <unistd.h>
//#else
//#include <io.h>
//#endif
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_anim_types.h"
//#include "DNA_armature_types.h"
//#include "DNA_action_types.h"  /* for some special action-editor settings */
//#include "DNA_constraint_types.h"
//#include "DNA_ipo_types.h"		/* some silly ipo flag	*/
//#include "DNA_listBase.h"
//#include "DNA_meshdata_types.h"
//#include "DNA_mesh_types.h"
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"		/* PET modes			*/
//#include "DNA_screen_types.h"	/* area dimensions		*/
//#include "DNA_texture_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_view3d_types.h"
//#include "DNA_space_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "RNA_access.h"
//
////#include "BIF_editview.h"		/* arrows_move_cursor	*/
//#include "BIF_gl.h"
//#include "BIF_glutil.h"
////#include "BIF_mywindow.h"
////#include "BIF_resources.h"
////#include "BIF_screen.h"
////#include "BIF_space.h"			/* undo					*/
////#include "BIF_toets.h"			/* persptoetsen			*/
////#include "BIF_mywindow.h"		/* warp_pointer			*/
////#include "BIF_toolbox.h"			/* notice				*/
////#include "BIF_editmesh.h"
////#include "BIF_editsima.h"
////#include "BIF_editparticle.h"
//
//#include "BKE_action.h"
//#include "BKE_nla.h"
////#include "BKE_bad_level_calls.h"/* popmenu and error	*/
//#include "BKE_bmesh.h"
//#include "BKE_context.h"
//#include "BKE_constraint.h"
//#include "BKE_global.h"
//#include "BKE_particle.h"
//#include "BKE_pointcache.h"
//#include "BKE_utildefines.h"
//#include "BKE_context.h"
//
////#include "BSE_view.h"
//
//#include "ED_image.h"
//#include "ED_screen.h"
//#include "ED_space_api.h"
//#include "ED_markers.h"
//#include "ED_util.h"
//#include "ED_view3d.h"
//
//#include "UI_view2d.h"
//#include "WM_types.h"
//#include "WM_api.h"
//
//#include "BLI_arithb.h"
//#include "BLI_blenlib.h"
//#include "BLI_editVert.h"
//
//#include "PIL_time.h"			/* sleep				*/
//
//#include "UI_resources.h"
//
////#include "blendef.h"
////
////#include "mydevice.h"
//
//#include "transform.h"

public class Transform {

/* DRAWLINE options flags */
public static final int DRAWLIGHT=	1;
public static final int DRAWDASHED=	2;
public static final int DRAWBOLD=	4;

public static class NDofInput {
    public int		flag;
    public int		axis;
    public float[]	fval = new float[7];
    public float[]	factor = new float[3];
};

public static class NumInput {
    public short  idx;
    public short  idx_max;
    public short  flag;        /* Different flags to indicate different behaviors                                */
    public byte[] inv = new byte[3];      /* If the value is inverted or not                                                */
    public float[] val = new float[3];      /* Direct value of the input                                                      */
    public int[] ctrl = new int[3];     /* Control to indicate what to do with the numbers that are typed                 */
};

/*
	The ctrl value has different meaning:
		0			: No value has been typed

		otherwise, |value| - 1 is where the cursor is located after the period
		Positive	: number is positive
		Negative	: number is negative
*/

public static class TransSnap {
    public short	modePoint;
    public short	modeTarget;
    public short	mode;
    public short	align;
    public short  	status;
    public float[]	snapPoint = new float[3]; /* snapping from this point */
    public float[]	snapTarget = new float[3]; /* to this point */
    public float[]	snapNormal = new float[3];
    public float[]	snapTangent = new float[3];
    public float	dist; // Distance from snapPoint to snapTarget
    public double	last;
//    public void  (*applySnap)(struct TransInfo *, float *);
//    public void  (*calcSnap)(struct TransInfo *, float *);
//    public void  (*targetSnap)(struct TransInfo *);
//    public float  (*distance)(struct TransInfo *, float p1[3], float p2[3]); // Get the transform distance between two points (used by Closest snap)
};

public static class TransCon {
    public static interface ApplyVec {
        public void run(TransInfo t, TransData td, float[] in, float[] out, float[] pvec);
    };
    public static interface ApplySize {
        public void run(TransInfo t, TransData td, float[][] smat);
    };
    public static interface ApplyRot {
        public void run(TransInfo t, TransData td, float[] vec, float[] angle);
    };
    public static interface DrawExtra {
        public void run(GL2 gl, TransInfo t);
    };

    public byte[]  text = new byte[50];      /* Description of the Constraint for header_print                            */
    public float[][] mtx = new float[3][3];     /* Matrix of the Constraint space                                            */
    public float[][] imtx = new float[3][3];    /* Inverse Matrix of the Constraint space                                    */
    public float[][] pmtx = new float[3][3];    /* Projection Constraint Matrix (same as imtx with some axis == 0)           */
    public float[] center = new float[3];     /* transformation center to define where to draw the view widget
                            ALWAYS in global space. Unlike the transformation center                  */
    public short[] imval = new short[2];	     /* initial mouse value for visual calculation                                */
	                     /* the one in TransInfo is not garanty to stay the same (Rotates change it)  */
    public int   mode;          /* Mode flags of the Constraint                                              */
    public DrawExtra drawExtra;
						 /* For constraints that needs to draw differently from the other
							uses this instead of the generic draw function							  */
    public ApplyVec applyVec;
                         /* Apply function pointer for linear vectorial transformation                */
                         /* The last three parameters are pointers to the in/out/printable vectors    */
    public ApplySize applySize;
                         /* Apply function pointer for size transformation */
    public ApplyRot applyRot;
                         /* Apply function pointer for rotation transformation */
};

//typedef struct TransDataIpokey {
//	int flag;					/* which keys */
//	float *locx, *locy, *locz;	/* channel pointers */
//	float *rotx, *roty, *rotz;
//	float *quatx, *quaty, *quatz, *quatw;
//	float *sizex, *sizey, *sizez;
//	float oldloc[9];			/* storage old values */
//	float oldrot[9];
//	float oldsize[9];
//	float oldquat[12];
//} TransDataIpokey;

public static class TransDataExtension {
    public float[] drot = new float[3];		 /* Initial object drot */
    public float[] dsize = new float[3];		 /* Initial object dsize */
    public float[] rot;          /* Rotation of the data to transform (Faculative)                                 */
    public float[] irot = new float[3];      /* Initial rotation                                                               */
    public float[] quat;         /* Rotation quaternion of the data to transform (Faculative)                      */
    public float[] iquat = new float[4];	 /* Initial rotation quaternion                                                    */
    public float[] size;         /* Size of the data to transform (Faculative)                                     */
    public float[] isize = new float[3];	 /* Initial size                                                                   */
    public float[][] obmat = new float[4][4];	 /* Object matrix */
};

//typedef struct TransData2D {
//	float loc[3];		/* Location of data used to transform (x,y,0) */
//	float *loc2d;		/* Pointer to real 2d location of data */
//} TransData2D;

///* we need to store 2 handles for each transdata incase the other handle wasnt selected */
//typedef struct TransDataCurveHandleFlags {
//	char ih1, ih2;
//	char *h1, *h2;
//} TransDataCurveHandleFlags;

///* for sequencer transform */
//typedef struct TransDataSeq {
//	struct Sequence *seq;
//	int flag;		/* a copy of seq.flag that may be modified for nested strips */
//	short start_offset; /* use this so we can have transform data at the strips start, but apply correctly to the start frame  */
//	short sel_flag; /* one of SELECT, SEQ_LEFTSEL and SEQ_RIGHTSEL */
//
//} TransDataSeq;

///* for NLA transform (stored in td.extra pointer) */
//typedef struct TransDataNla {
//	ID *id;						/* ID-block NLA-data is attached to */
//
//	struct NlaTrack *oldTrack;	/* Original NLA-Track that the strip belongs to */
//	struct NlaTrack *nlt;		/* Current NLA-Track that the strip belongs to */
//
//	struct NlaStrip *strip;		/* NLA-strip this data represents */
//
//	/* dummy values for transform to write in - must have 3 elements... */
//	float h1[3];				/* start handle */
//	float h2[3];				/* end handle */
//
//	int trackIndex;				/* index of track that strip is currently in */
//	int handle;					/* handle-index: 0 for dummy entry, -1 for start, 1 for end, 2 for both ends */
//} TransDataNla;

public static class TransData {
    public float  dist;         /* Distance needed to affect element (for Proportionnal Editing)                  */
    public float  rdist;        /* Distance to the nearest element (for Proportionnal Editing)                    */
    public float  factor;       /* Factor of the transformation (for Proportionnal Editing)                       */
    public float[] loc;          /* Location of the data to transform                                              */
    public float[]  iloc = new float[3];      /* Initial location                                                               */
    public Pointer<Float> val;          /* Value pointer for special transforms */
    public float  ival;         /* Old value*/
    public float[]  center = new float[3];	 /* Individual data center                                                         */
    public float[][]  mtx = new float[3][3];    /* Transformation matrix from data space to global space                          */
    public float[][]  smtx = new float[3][3];   /* Transformation matrix from global space to data space                          */
    public float[][]  axismtx = new float[3][3];/* Axis orientation matrix of the data                                            */
    public bObject ob;
    public bConstraint con;	/* for objects/bones, the first constraint in its constraint stack */
    public TransDataExtension ext;	/* for objects, poses. 1 single malloc per TransInfo! */
//    public TransDataIpokey tdi;		/* for objects, ipo keys. per transdata a malloc */
//    public TransDataCurveHandleFlags *hdata; /* for curves, stores handle flags for modification/cancel */
    public Object extra;		 /* extra data (mirrored element pointer, in editmode mesh to EditVert) (editbone for roll fixing) (...) */
    public short  flag;         /* Various flags */
    public short  protectflag;	 /* If set, copy of Object or PoseChannel protection */
};

public static class MouseInput {
    public static interface Apply {
        public void run(TransInfo t, MouseInput mi, short[] mval, float[] output);
    };

//    public void	(*apply)(struct TransInfo *, struct MouseInput *, short [2], float [3]);
    public Apply apply;

    public short[]   imval = new short[2];       	/* initial mouse position                */
    public byte	precision;
    public short[]	precision_mval = new short[2];	/* mouse position when precision key was pressed */
    public int[]		center = new int[2];
    public float	factor;
};

public static class TransInfo {
    public static interface TransformFunc {
        public int run(TransInfo t, short[] mval);
    };
    public static interface HandleEventFunc {
        public int run(TransInfo t, wmEvent evt);
    };

    public int mode;           /* current mode                         */
    public int flag;           /* generic flags for special behaviors  */
    public int modifiers;		/* special modifiers, by function, not key */
    public short state;			/* current state (running, canceled,...)*/
    public int options;        /* current context/options for transform                      */
    public float val;            /* init value for some transformations (and rotation angle)  */
    public float fac;            /* factor for distance based transform  */
    public TransformFunc transform;
                                /* transform function pointer           */
    public HandleEventFunc handleEvent;
								/* event handler function pointer  RETURN 1 if redraw is needed */
    public int total;          /* total number of transformed data     */
    public TransData[]  data;           /* transformed data (array)             */
    public TransDataExtension[] ext;	/* transformed data extension (array)   */
//    public TransData2D *data2d;		/* transformed data for 2d (array)      */
    public TransCon con = new TransCon();            /* transformed constraint               */
    public TransSnap tsnap = new TransSnap();
    public NumInput num = new NumInput();            /* numerical input                      */
    public NDofInput ndof = new NDofInput();           /* ndof input                           */
    public MouseInput mouse = new MouseInput();			/* mouse input                          */
    public byte redraw;         /* redraw flag                          */
    public float prop_size;		/* proportional circle radius           */
    public byte[] proptext = new byte[20];	/* proportional falloff text			*/
    public float[] center =new float[3];      /* center of transformation             */
    public int[] center2d = new int[2];    /* center in screen coordinates         */
    public short[] imval = new short[2];       /* initial mouse position               */
    public short event_type;		/* event.type used to invoke transform */
    public short idx_max;		/* maximum index on the input vector	*/
    public float[] snap = new float[3];		/* Snapping Gears						*/
//    public char		frame_side;		/* Mouse side of the cfra, 'L', 'R' or 'B' */

    public float[][] viewmat = new float[4][4];	/* copy from G.vd, prevents feedback,   */
    public float[][] viewinv = new float[4][4];  /* and to make sure we don't have to    */
    public float[][] persmat = new float[4][4];  /* access G.vd from other space types   */
    public float[][] persinv = new float[4][4];
    public short persp;
    public short around;
    public byte spacetype;		/* spacetype where transforming is      */
    public byte		helpline;		/* helpline modes (not to be confused with hotline) */

    public float[] vec = new float[3];			/* translation, to show for widget   	*/
    public float[][] mat = new float[3][3];		/* rot/rescale, to show for widget   	*/

//    public char		*undostr;		/* if set, uses this string for undo		*/
    public float[][] spacemtx = new float[3][3];	/* orientation matrix of the current space	*/
    public byte[] spacename = new byte[32];	/* name of the current space				*/

    public bObject poseobj;		/* if t.flag & T_POSE, this denotes pose object */

//    public void       *customData;		/* Per Transform custom data */

    /*************** NEW STUFF *********************/

    public short current_orientation;

    public short prop_mode;

    public float[] values = new float[4];
    public float[] auto_values = new float[4];
    public Object view;
    public ScrArea sa;
    public ARegion ar;
    public Scene scene;
    public ToolSettings settings;
//    public wmTimer animtimer;
    public short[] mval = new short[2];        /* current mouse position               */
    public bObject obedit;
    public Object draw_handle;
};

/* ******************** Macros & Prototypes *********************** */

/* NUMINPUT FLAGS */
public static final int NUM_NULL_ONE=		2;
public static final int NUM_NO_NEGATIVE=		4;
public static final int	NUM_NO_ZERO=			8;
public static final int NUM_NO_FRACTION=		16;
public static final int	NUM_AFFECT_ALL=		32;

/* NDOFINPUT FLAGS */
public static final int NDOF_INIT=			1;

/* transinfo->state */
public static final int TRANS_RUNNING=	0;
public static final int TRANS_CONFIRM=	1;
public static final int TRANS_CANCEL=	2;

/* transinfo->flag */
public static final int T_OBJECT=		(1 << 0);
public static final int T_EDIT=			(1 << 1);
public static final int T_POSE=			(1 << 2);
public static final int T_TEXTURE=		(1 << 3);
public static final int T_CAMERA=		(1 << 4);
	 	// trans on points, having no rotation/scale
public static final int T_POINTS=		(1 << 6);
		// for manipulator exceptions, like scaling using center point, drawing help lines
public static final int T_USES_MANIPULATOR=	(1 << 7);

	/* restrictions flags */
public static final int T_ALL_RESTRICTIONS=	((1 << 8)|(1 << 9)|(1 << 10));
public static final int T_NO_CONSTRAINT=		(1 << 8);
public static final int T_NULL_ONE=			(1 << 9);
public static final int T_NO_ZERO=			(1 << 10);

public static final int T_PROP_EDIT=			(1 << 11);
public static final int T_PROP_CONNECTED=	(1 << 12);

public static final int T_V3D_ALIGN=			(1 << 14);
	/* for 2d views like uv or ipo */
public static final int T_2D_EDIT=			(1 << 15);
public static final int T_CLIP_UV=			(1 << 16);

public static final int T_FREE_CUSTOMDATA=	(1 << 17);
	/* auto-ik is on */
public static final int T_AUTOIK=			(1 << 18);

public static final int T_MIRROR=			(1 << 19);

public static final int T_AUTOVALUES=		(1 << 20);

	/* to specificy if we save back settings at the end */
public static final int	T_MODAL=				(1 << 21);

/* TransInfo->modifiers */
public static final int	MOD_CONSTRAINT_SELECT=	0x01;
public static final int	MOD_PRECISION=			0x02;
public static final int	MOD_SNAP_GEARS=			0x04;
public static final int	MOD_CONSTRAINT_PLANE=	0x08;


/* ******************************************************************************** */

/* transinfo->helpline */
public static final int HLP_NONE=		0;
public static final int HLP_SPRING=		1;
public static final int HLP_ANGLE=		2;
public static final int HLP_HARROW=		3;
public static final int HLP_VARROW=		4;
public static final int HLP_TRACKBALL=	5;

/* transinfo->con->mode */
public static final int CON_APPLY=		1;
public static final int CON_AXIS0=		2;
public static final int CON_AXIS1=		4;
public static final int CON_AXIS2=		8;
public static final int CON_SELECT=		16;
public static final int CON_NOFLIP=		32;	/* does not reorient vector to face viewport when on */
public static final int CON_USER=		64;

/* transdata->flag */
public static final int TD_SELECTED=			1;
public static final int TD_ACTIVE=			(1 << 1);
public static final int	TD_NOACTION=			(1 << 2);
public static final int	TD_USEQUAT=			(1 << 3);
public static final int TD_NOTCONNECTED=		(1 << 4);
public static final int TD_SINGLESIZE=		(1 << 5);	/* used for scaling of MetaElem->rad */
public static final int TD_TIMEONLY=			(1 << 8);
public static final int TD_NOCENTER=			(1 << 9);
public static final int TD_NO_EXT=			(1 << 10);	/* ext abused for particle key timing */
public static final int TD_SKIP=				(1 << 11);	/* don't transform this data */
public static final int TD_BEZTRIPLE=		(1 << 12);	/* if this is a bez triple, we need to restore the handles, if this is set transdata->misc.hdata needs freeing */
public static final int TD_NO_LOC=			(1 << 13);	/* when this is set, don't apply translation changes to this element */
public static final int TD_NOTIMESNAP=		(1 << 14);	/* for Graph Editor autosnap, indicates that point should not undergo autosnapping */
public static final int TD_INTVALUES=	 	(1 << 15); 	/* for Graph Editor - curves that can only have int-values need their keyframes tagged with this */

/* transsnap->status */
public static final int SNAP_ON=			1;
public static final int SNAP_FORCED=		2;
public static final int TARGET_INIT=		4;
public static final int POINT_INIT=		8;

/* transsnap->modePoint */
public static final int SNAP_GRID=			0;
public static final int SNAP_GEO=			1;

/* transsnap->modeTarget */
public static final int SNAP_CLOSEST=		0;
public static final int SNAP_CENTER=			1;
public static final int SNAP_MEDIAN=			2;
public static final int SNAP_ACTIVE=			3;

/* MODE AND NUMINPUT FLAGS */
//enum {
	public static final int TFM_INIT = -1;
	public static final int TFM_DUMMY = 0;
	public static final int TFM_TRANSLATION = 1;
	public static final int TFM_ROTATION = 2;
	public static final int TFM_RESIZE = 3;
	public static final int TFM_TOSPHERE = 4;
	public static final int TFM_SHEAR = 5;
	public static final int TFM_WARP = 6;
	public static final int TFM_SHRINKFATTEN = 7;
	public static final int TFM_TILT = 8;
	public static final int TFM_TRACKBALL = 9;
	public static final int TFM_PUSHPULL = 10;
	public static final int TFM_CREASE = 11;
	public static final int TFM_MIRROR = 12;
	public static final int TFM_BONESIZE = 13;
	public static final int TFM_BONE_ENVELOPE = 14;
	public static final int TFM_CURVE_SHRINKFATTEN = 15;
	public static final int TFM_BONE_ROLL = 16;
	public static final int TFM_TIME_TRANSLATE = 17;
	public static final int TFM_TIME_SLIDE = 18;
	public static final int TFM_TIME_SCALE = 19;
	public static final int TFM_TIME_EXTEND = 20;
	public static final int TFM_BAKE_TIME = 21;
	public static final int TFM_BEVEL = 22;
	public static final int TFM_BWEIGHT = 23;
	public static final int TFM_ALIGN = 24;
//} TfmMode;

/* TRANSFORM CONTEXTS */
public static final int CTX_NONE=			0;
public static final int CTX_TEXTURE=			1;
public static final int CTX_EDGE=			2;
public static final int CTX_NO_PET=			4;
public static final int CTX_TWEAK=			8;
public static final int CTX_NO_MIRROR=		16;
public static final int CTX_AUTOCONFIRM=		32;
public static final int CTX_BMESH=			64;
public static final int CTX_NDOF=			128;

/*********************** TransSpace ******************************/

    public static final int ORIENTATION_NONE = 0;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_VERT = 2;
    public static final int ORIENTATION_EDGE = 3;
    public static final int ORIENTATION_FACE = 4;

/********************** Mouse Input ******************************/

public static enum MouseInputMode {
	INPUT_NONE,
	INPUT_VECTOR,
	INPUT_SPRING,
	INPUT_SPRING_FLIP,
	INPUT_ANGLE,
	INPUT_TRACKBALL,
	INPUT_HORIZONTAL_RATIO,
	INPUT_HORIZONTAL_ABSOLUTE,
	INPUT_VERTICAL_RATIO,
	INPUT_VERTICAL_ABSOLUTE
};

/* ************************** SPACE DEPENDANT CODE **************************** */

public static void setTransformViewMatrices(TransInfo t)
{
	if(t.spacetype==SpaceTypes.SPACE_VIEW3D && t.ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW) {
		RegionView3D rv3d = (RegionView3D)t.ar.regiondata;

		Arithb.Mat4CpyMat4(t.viewmat, rv3d.viewmat);
		Arithb.Mat4CpyMat4(t.viewinv, rv3d.viewinv);
		Arithb.Mat4CpyMat4(t.persmat, rv3d.persmat);
		Arithb.Mat4CpyMat4(t.persinv, rv3d.persinv);
		t.persp = rv3d.persp;
	}
	else {
		Arithb.Mat4One(t.viewmat);
		Arithb.Mat4One(t.viewinv);
		Arithb.Mat4One(t.persmat);
		Arithb.Mat4One(t.persinv);
		t.persp = View3dTypes.V3D_ORTHO;
	}

	TransformGenerics.calculateCenter2D(t);
}

public static void convertViewVec(TransInfo t, float[] vec, short dx, short dy)
{
	if (t.spacetype==SpaceTypes.SPACE_VIEW3D) {
		if (t.ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW)
		{
			View3dView.window_to_3d_delta(t.ar, vec, dx, dy);
		}
	}
//	else if(t.spacetype==SpaceTypes.SPACE_IMAGE) {
//		View2D *v2d = t.view;
//		float divx, divy, aspx, aspy;
//
//		ED_space_image_uv_aspect(t.sa.spacedata.first, &aspx, &aspy);
//
//		divx= v2d.mask.xmax-v2d.mask.xmin;
//		divy= v2d.mask.ymax-v2d.mask.ymin;
//
//		vec[0]= aspx*(v2d.cur.xmax-v2d.cur.xmin)*(dx)/divx;
//		vec[1]= aspy*(v2d.cur.ymax-v2d.cur.ymin)*(dy)/divy;
//		vec[2]= 0.0f;
//	}
//	else if(ELEM(t.spacetype, SPACE_IPO, SPACE_NLA)) {
//		View2D *v2d = t.view;
//		float divx, divy;
//
//		divx= v2d.mask.xmax-v2d.mask.xmin;
//		divy= v2d.mask.ymax-v2d.mask.ymin;
//
//		vec[0]= (v2d.cur.xmax-v2d.cur.xmin)*(dx) / (divx);
//		vec[1]= (v2d.cur.ymax-v2d.cur.ymin)*(dy) / (divy);
//		vec[2]= 0.0f;
//	}
//	else if(t.spacetype==SPACE_NODE) {
//		View2D *v2d = &t.ar.v2d;
//		float divx, divy;
//
//		divx= v2d.mask.xmax-v2d.mask.xmin;
//		divy= v2d.mask.ymax-v2d.mask.ymin;
//
//		vec[0]= (v2d.cur.xmax-v2d.cur.xmin)*(dx)/divx;
//		vec[1]= (v2d.cur.ymax-v2d.cur.ymin)*(dy)/divy;
//		vec[2]= 0.0f;
//	}
//	else if(t.spacetype==SPACE_SEQ) {
//		View2D *v2d = &t.ar.v2d;
//		float divx, divy;
//
//		divx= v2d.mask.xmax-v2d.mask.xmin;
//		divy= v2d.mask.ymax-v2d.mask.ymin;
//
//		vec[0]= (v2d.cur.xmax-v2d.cur.xmin)*(dx)/divx;
//		vec[1]= (v2d.cur.ymax-v2d.cur.ymin)*(dy)/divy;
//		vec[2]= 0.0f;
//	}
}

public static void projectIntView(TransInfo t, float[] vec, int[] adr)
{
	if (t.spacetype==SpaceTypes.SPACE_VIEW3D) {
		if(t.ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW)
			View3dView.project_int_noclip(t.ar, vec, adr);
	}
//	else if(t.spacetype==SPACE_IMAGE) {
//		float aspx, aspy, v[2];
//
//		ED_space_image_uv_aspect(t.sa.spacedata.first, &aspx, &aspy);
//		v[0]= vec[0]/aspx;
//		v[1]= vec[1]/aspy;
//
//		UI_view2d_to_region_no_clip(t.view, v[0], v[1], adr, adr+1);
//	}
//	else if(ELEM(t.spacetype, SPACE_IPO, SPACE_NLA)) {
//		int out[2] = {0, 0};
//
//		UI_view2d_view_to_region((View2D *)t.view, vec[0], vec[1], out, out+1);
//		adr[0]= out[0];
//		adr[1]= out[1];
//	}
//	else if(t.spacetype==SPACE_SEQ) { /* XXX not tested yet, but should work */
//		int out[2] = {0, 0};
//
//		UI_view2d_view_to_region((View2D *)t.view, vec[0], vec[1], out, out+1);
//		adr[0]= out[0];
//		adr[1]= out[1];
//	}
}

public static void projectFloatView(TransInfo t, float[] vec, float[] adr)
{
	if (t.spacetype==SpaceTypes.SPACE_VIEW3D) {
		if(t.ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW)
			View3dView.project_float_noclip(t.ar, vec, adr);
	}
//	else if(t.spacetype==SPACE_IMAGE) {
//		int a[2];
//
//		projectIntView(t, vec, a);
//		adr[0]= a[0];
//		adr[1]= a[1];
//	}
//	else if(ELEM(t.spacetype, SPACE_IPO, SPACE_NLA)) {
//		int a[2];
//
//		projectIntView(t, vec, a);
//		adr[0]= a[0];
//		adr[1]= a[1];
//	}
}

//void applyAspectRatio(TransInfo *t, float *vec)
//{
//	SpaceImage *sima= t.sa.spacedata.first;
//
//	if ((t.spacetype==SPACE_IMAGE) && (t.mode==TFM_TRANSLATION)) {
//		float aspx, aspy;
//
//		if((sima.flag & SI_COORDFLOATS)==0) {
//			int width, height;
//			ED_space_image_size(sima, &width, &height);
//
//			vec[0] *= width;
//			vec[1] *= height;
//		}
//
//		ED_space_image_uv_aspect(sima, &aspx, &aspy);
//		vec[0] /= aspx;
//		vec[1] /= aspy;
//	}
//}
//
//void removeAspectRatio(TransInfo *t, float *vec)
//{
//	SpaceImage *sima= t.sa.spacedata.first;
//
//	if ((t.spacetype==SPACE_IMAGE) && (t.mode==TFM_TRANSLATION)) {
//		float aspx, aspy;
//
//		if((sima.flag & SI_COORDFLOATS)==0) {
//			int width, height;
//			ED_space_image_size(sima, &width, &height);
//
//			vec[0] /= width;
//			vec[1] /= height;
//		}
//
//		ED_space_image_uv_aspect(sima, &aspx, &aspy);
//		vec[0] *= aspx;
//		vec[1] *= aspy;
//	}
//}

static void viewRedrawForce(bContext C, TransInfo t)
{
	if (t.spacetype == SpaceTypes.SPACE_VIEW3D)
	{
		/* Do we need more refined tags? */
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_OBJECT|WmTypes.ND_TRANSFORM, null);
	}
//	else if (t.spacetype == SPACE_ACTION) {
//		//SpaceAction *saction= (SpaceAction *)t.sa.spacedata.first;
//		WM_event_add_notifier(C, NC_ANIMATION|ND_KEYFRAME_EDIT, NULL);
//	}
//	else if (t.spacetype == SPACE_IPO) {
//		//SpaceIpo *sipo= (SpaceIpo *)t.sa.spacedata.first;
//		WM_event_add_notifier(C, NC_ANIMATION|ND_KEYFRAME_EDIT, NULL);
//	}
//	else if (t.spacetype == SPACE_NLA) {
//		WM_event_add_notifier(C, NC_ANIMATION|ND_NLA_EDIT, NULL);
//	}
//	else if(t.spacetype == SPACE_NODE)
//	{
//		//ED_area_tag_redraw(t.sa);
//		WM_event_add_notifier(C, NC_SCENE|ND_NODES, NULL);
//	}
//	else if(t.spacetype == SPACE_SEQ)
//	{
//		WM_event_add_notifier(C, NC_SCENE|ND_SEQUENCER, NULL);
//	}
//	else if (t.spacetype==SPACE_IMAGE) {
//		// XXX how to deal with lock?
//#if 0
//		SpaceImage *sima= (SpaceImage*)t.sa.spacedata.first;
//		if(sima.lock) force_draw_plus(SPACE_VIEW3D, 0);
//		else force_draw(0);
//#endif
//
//		WM_event_add_notifier(C, NC_OBJECT|ND_GEOM_DATA, t.obedit);
//	}
}

static void viewRedrawPost(TransInfo t)
{
//	ED_area_headerprint(t.sa, null);

//#if 0 // TRANSFORM_FIX_ME
//	if(t.spacetype==SPACE_VIEW3D) {
//		allqueue(REDRAWBUTSOBJECT, 0);
//		allqueue(REDRAWVIEW3D, 0);
//	}
//	else if(t.spacetype==SPACE_IMAGE) {
//		allqueue(REDRAWIMAGE, 0);
//		allqueue(REDRAWVIEW3D, 0);
//	}
//	else if(ELEM3(t.spacetype, SPACE_ACTION, SPACE_NLA, SPACE_IPO)) {
//		allqueue(REDRAWVIEW3D, 0);
//		allqueue(REDRAWACTION, 0);
//		allqueue(REDRAWNLA, 0);
//		allqueue(REDRAWIPO, 0);
//		allqueue(REDRAWTIME, 0);
//		allqueue(REDRAWBUTSOBJECT, 0);
//	}
//
//	scrarea_queue_headredraw(curarea);
//#endif
}

///* ************************** TRANSFORMATIONS **************************** */
//
//void BIF_selectOrientation() {
//#if 0 // TRANSFORM_FIX_ME
//	short val;
//	char *str_menu = BIF_menustringTransformOrientation("Orientation");
//	val= pupmenu(str_menu);
//	MEM_freeN(str_menu);
//
//	if(val >= 0) {
//		G.vd.twmode = val;
//	}
//#endif
//}
//
//static void view_editmove(unsigned short event)
//{
//#if 0 // TRANSFORM_FIX_ME
//	int refresh = 0;
//	/* Regular:   Zoom in */
//	/* Shift:     Scroll up */
//	/* Ctrl:      Scroll right */
//	/* Alt-Shift: Rotate up */
//	/* Alt-Ctrl:  Rotate right */
//
//	/* only work in 3D window for now
//	 * In the end, will have to send to event to a 2D window handler instead
//	 */
//	if (Trans.flag & T_2D_EDIT)
//		return;
//
//	switch(event) {
//		case WHEELUPMOUSE:
//
//			if( G.qual & LR_SHIFTKEY ) {
//				if( G.qual & LR_ALTKEY ) {
//					G.qual &= ~LR_SHIFTKEY;
//					persptoetsen(PAD2);
//					G.qual |= LR_SHIFTKEY;
//				} else {
//					persptoetsen(PAD2);
//				}
//			} else if( G.qual & LR_CTRLKEY ) {
//				if( G.qual & LR_ALTKEY ) {
//					G.qual &= ~LR_CTRLKEY;
//					persptoetsen(PAD4);
//					G.qual |= LR_CTRLKEY;
//				} else {
//					persptoetsen(PAD4);
//				}
//			} else if(U.uiflag & USER_WHEELZOOMDIR)
//				persptoetsen(PADMINUS);
//			else
//				persptoetsen(PADPLUSKEY);
//
//			refresh = 1;
//			break;
//		case WHEELDOWNMOUSE:
//			if( G.qual & LR_SHIFTKEY ) {
//				if( G.qual & LR_ALTKEY ) {
//					G.qual &= ~LR_SHIFTKEY;
//					persptoetsen(PAD8);
//					G.qual |= LR_SHIFTKEY;
//				} else {
//					persptoetsen(PAD8);
//				}
//			} else if( G.qual & LR_CTRLKEY ) {
//				if( G.qual & LR_ALTKEY ) {
//					G.qual &= ~LR_CTRLKEY;
//					persptoetsen(PAD6);
//					G.qual |= LR_CTRLKEY;
//				} else {
//					persptoetsen(PAD6);
//				}
//			} else if(U.uiflag & USER_WHEELZOOMDIR)
//				persptoetsen(PADPLUSKEY);
//			else
//				persptoetsen(PADMINUS);
//
//			refresh = 1;
//			break;
//	}
//
//	if (refresh)
//		setTransformViewMatrices(&Trans);
//#endif
//}
//
//#if 0
//static char *transform_to_undostr(TransInfo *t)
//{
//	switch (t.mode) {
//		case TFM_TRANSLATION:
//			return "Translate";
//		case TFM_ROTATION:
//			return "Rotate";
//		case TFM_RESIZE:
//			return "Scale";
//		case TFM_TOSPHERE:
//			return "To Sphere";
//		case TFM_SHEAR:
//			return "Shear";
//		case TFM_WARP:
//			return "Warp";
//		case TFM_SHRINKFATTEN:
//			return "Shrink/Fatten";
//		case TFM_TILT:
//			return "Tilt";
//		case TFM_TRACKBALL:
//			return "Trackball";
//		case TFM_PUSHPULL:
//			return "Push/Pull";
//		case TFM_BEVEL:
//			return "Bevel";
//		case TFM_BWEIGHT:
//			return "Bevel Weight";
//		case TFM_CREASE:
//			return "Crease";
//		case TFM_BONESIZE:
//			return "Bone Width";
//		case TFM_BONE_ENVELOPE:
//			return "Bone Envelope";
//		case TFM_TIME_TRANSLATE:
//			return "Translate Anim. Data";
//		case TFM_TIME_SCALE:
//			return "Scale Anim. Data";
//		case TFM_TIME_SLIDE:
//			return "Time Slide";
//		case TFM_BAKE_TIME:
//			return "Key Time";
//		case TFM_MIRROR:
//			return "Mirror";
//	}
//	return "Transform";
//}
//#endif

/* ************************************************* */

/* NOTE: these defines are saved in keymap files, do not change values but just add new ones */
public static final int TFM_MODAL_CANCEL=			1;
public static final int TFM_MODAL_CONFIRM=			2;
public static final int TFM_MODAL_TRANSLATE=			3;
public static final int TFM_MODAL_ROTATE=			4;
public static final int TFM_MODAL_RESIZE=			5;
public static final int TFM_MODAL_SNAP_GEARS=		6;
public static final int TFM_MODAL_SNAP_GEARS_OFF=	7;

static EnumPropertyItem[] modal_items = {
	new EnumPropertyItem(TFM_MODAL_CANCEL, "CANCEL", 0, "Cancel", ""),
	new EnumPropertyItem(TFM_MODAL_CONFIRM, "CONFIRM", 0, "Confirm", ""),
	new EnumPropertyItem(TFM_MODAL_TRANSLATE, "TRANSLATE", 0, "Translate", ""),
	new EnumPropertyItem(TFM_MODAL_ROTATE, "ROTATE", 0, "Rotate", ""),
	new EnumPropertyItem(TFM_MODAL_RESIZE, "RESIZE", 0, "Resize", ""),
	new EnumPropertyItem(TFM_MODAL_SNAP_GEARS, "SNAP_GEARS", 0, "Snap On", ""),
	new EnumPropertyItem(TFM_MODAL_SNAP_GEARS_OFF, "SNAP_GEARS_OFF", 0, "Snap Off", ""),
	new EnumPropertyItem(0, null, 0, null, null)};

/* called in transform_ops.c, on each regeneration of keymaps */
public static void transform_modal_keymap(wmKeyConfig keyconf)
{
//	static EnumPropertyItem modal_items[] = {
//	{TFM_MODAL_CANCEL, "CANCEL", 0, "Cancel", ""},
//	{TFM_MODAL_CONFIRM, "CONFIRM", 0, "Confirm", ""},
//	{TFM_MODAL_TRANSLATE, "TRANSLATE", 0, "Translate", ""},
//	{TFM_MODAL_ROTATE, "ROTATE", 0, "Rotate", ""},
//	{TFM_MODAL_RESIZE, "RESIZE", 0, "Resize", ""},
//	{TFM_MODAL_SNAP_GEARS, "SNAP_GEARS", 0, "Snap On", ""},
//	{TFM_MODAL_SNAP_GEARS_OFF, "SNAP_GEARS_OFF", 0, "Snap Off", ""},
//	{0, NULL, 0, NULL, NULL}};
	
	wmKeyMap keymap= WmKeymap.WM_modalkeymap_get(keyconf, "Transform Modal Map");
	
	/* this function is called for each spacetype, only needs to add map once */
	if(keymap!=null) return;
	
	keymap= WmKeymap.WM_modalkeymap_add(keyconf, "Transform Modal Map", modal_items);
	
	/* items for modal map */
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.ESCKEY,    WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, TFM_MODAL_CANCEL);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTMOUSE, WmTypes.KM_ANY, WmTypes.KM_ANY, 0, TFM_MODAL_CONFIRM);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.RETKEY, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, TFM_MODAL_CONFIRM);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.PADENTER, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, TFM_MODAL_CONFIRM);

	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.GKEY, WmTypes.KM_PRESS, 0, 0, TFM_MODAL_TRANSLATE);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.RKEY, WmTypes.KM_PRESS, 0, 0, TFM_MODAL_ROTATE);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.SKEY, WmTypes.KM_PRESS, 0, 0, TFM_MODAL_RESIZE);
	
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTCTRLKEY, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, TFM_MODAL_SNAP_GEARS);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTCTRLKEY, WmTypes.KM_RELEASE, WmTypes.KM_ANY, 0, TFM_MODAL_SNAP_GEARS_OFF);
	
	/* assign map to operators */
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_transform");
	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_translate");
	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_rotate");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_tosphere");
	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_resize");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_shear");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_warp");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_shrink_fatten");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_tilt");
//	WmKeymap.WM_modalkeymap_assign(keymap, "TFM_OT_trackball");
	
}


public static void transformEvent(TransInfo t, wmEvent event)
{
	float[][] mati = {{1.0f, 0.0f, 0.0f}, {0.0f, 1.0f, 0.0f}, {0.0f, 0.0f, 1.0f}};
	char cmode = TransformConstraints.constraintModeToChar(t);

	t.redraw |= TransformInput.handleMouseInput(t, t.mouse, event);

	if (event.type == WmEventTypes.MOUSEMOVE)
	{
		t.mval[0] = (short)(event.x - t.ar.winrct.xmin);
		t.mval[1] = (short)(event.y - t.ar.winrct.ymin);

		t.redraw = 1;

		TransformInput.applyMouseInput(t, t.mouse, t.mval, t.values);
	}

	/* handle modal keymap first */
	if (event.type == WmEventTypes.EVT_MODAL_MAP) {
		switch (event.val) {
			case TFM_MODAL_CANCEL:
				t.state = TRANS_CANCEL;
				break;
			case TFM_MODAL_CONFIRM:
				t.state = TRANS_CONFIRM;
				break;

			case TFM_MODAL_TRANSLATE:
				/* only switch when... */
				if(t.mode==TFM_ROTATION || t.mode==TFM_RESIZE || t.mode==TFM_TRACKBALL) {
					TransformGenerics.resetTransRestrictions(t);
					TransformGenerics.restoreTransObjects(t);
					initTranslation(t);
					TransformSnap.initSnapping(t, null); // need to reinit after mode change
					t.redraw = 1;
				}
				break;
			case TFM_MODAL_ROTATE:
				/* only switch when... */
				if(t.mode==TFM_ROTATION || t.mode==TFM_RESIZE || t.mode==TFM_TRACKBALL || t.mode==TFM_TRANSLATION) {

					TransformGenerics.resetTransRestrictions(t);

					if (t.mode == TFM_ROTATION) {
						TransformGenerics.restoreTransObjects(t);
						initTrackball(t);
					}
					else {
						TransformGenerics.restoreTransObjects(t);
						initRotation(t);
					}
					TransformSnap.initSnapping(t, null); // need to reinit after mode change
					t.redraw = 1;
				}
				break;
			case TFM_MODAL_RESIZE:
				/* only switch when... */
				if(t.mode==TFM_ROTATION || t.mode==TFM_TRANSLATION || t.mode==TFM_TRACKBALL) {
					TransformGenerics.resetTransRestrictions(t);
					TransformGenerics.restoreTransObjects(t);
					initResize(t);
					TransformSnap.initSnapping(t, null); // need to reinit after mode change
					t.redraw = 1;
				}
				break;

			case TFM_MODAL_SNAP_GEARS:
				t.modifiers |= MOD_SNAP_GEARS;
				t.redraw = 1;
				break;
			case TFM_MODAL_SNAP_GEARS_OFF:
				t.modifiers &= ~MOD_SNAP_GEARS;
				t.redraw = 1;
				break;
		}
	}
	/* else do non-mapped events */
	else if (event.val==WmTypes.KM_PRESS) {
		switch (event.type){
		case WmEventTypes.RIGHTMOUSE:
			t.state = TRANS_CANCEL;
			break;
		/* enforce redraw of transform when modifiers are used */
		case WmEventTypes.LEFTCTRLKEY:
		case WmEventTypes.RIGHTCTRLKEY:
			t.modifiers |= MOD_SNAP_GEARS;
			t.redraw = 1;
			break;

		case WmEventTypes.LEFTSHIFTKEY:
		case WmEventTypes.RIGHTSHIFTKEY:
			t.modifiers |= MOD_CONSTRAINT_PLANE;
			t.redraw = 1;
			break;

		case WmEventTypes.SPACEKEY:
			if ((t.spacetype==SpaceTypes.SPACE_VIEW3D) && event.alt!=0) {
//#if 0 // TRANSFORM_FIX_ME
//				short mval[2];
//
//				getmouseco_sc(mval);
//				BIF_selectOrientation();
//				calc_manipulator_stats(curarea);
//				Mat3CpyMat4(t.spacemtx, G.vd.twmat);
//				warp_pointer(mval[0], mval[1]);
//#endif
			}
			else {
				t.state = TRANS_CONFIRM;
			}
			break;

		case WmEventTypes.MIDDLEMOUSE:
			if ((t.flag & T_NO_CONSTRAINT)==0) {
				/* exception for switching to dolly, or trackball, in camera view */
				if ((t.flag & T_CAMERA)!=0) {
					if (t.mode==TFM_TRANSLATION) {
						TransformConstraints.setLocalConstraint(t, (CON_AXIS2), "along local Z");
                                        }
					else if (t.mode==TFM_ROTATION) {
						TransformGenerics.restoreTransObjects(t);
						initTrackball(t);
					}
				}
				else {
					t.modifiers |= MOD_CONSTRAINT_SELECT;
					if ((t.con.mode & CON_APPLY)!=0) {
						TransformConstraints.stopConstraint(t);
					}
					else {
						if (event.shift!=0) {
							TransformConstraints.initSelectConstraint(t, t.spacemtx);
						}
						else {
							/* bit hackish... but it prevents mmb select to print the orientation from menu */
							StringUtil.strcpy(t.spacename,0, StringUtil.toCString("global"),0);
							TransformConstraints.initSelectConstraint(t, mati);
						}
						TransformConstraints.postSelectConstraint(t);
					}
				}
				t.redraw = 1;
			}
			break;
		case WmEventTypes.ESCKEY:
			t.state = TRANS_CANCEL;
			break;
		case WmEventTypes.PADENTER:
		case WmEventTypes.RETKEY:
			t.state = TRANS_CONFIRM;
			break;
		case WmEventTypes.GKEY:
			/* only switch when... */
			if(t.mode==TFM_ROTATION || t.mode==TFM_RESIZE || t.mode==TFM_TRACKBALL) {
				TransformGenerics.resetTransRestrictions(t);
				TransformGenerics.restoreTransObjects(t);
				initTranslation(t);
				TransformSnap.initSnapping(t, null); // need to reinit after mode change
				t.redraw = 1;
			}
			break;
		case WmEventTypes.SKEY:
			/* only switch when... */
			if(t.mode==TFM_ROTATION || t.mode==TFM_TRANSLATION || t.mode==TFM_TRACKBALL) {
				TransformGenerics.resetTransRestrictions(t);
				TransformGenerics.restoreTransObjects(t);
				initResize(t);
				TransformSnap.initSnapping(t, null); // need to reinit after mode change
				t.redraw = 1;
			}
			break;
		case WmEventTypes.RKEY:
			/* only switch when... */
			if(t.mode==TFM_ROTATION || t.mode==TFM_RESIZE || t.mode==TFM_TRACKBALL || t.mode==TFM_TRANSLATION) {

				TransformGenerics.resetTransRestrictions(t);

				if (t.mode == TFM_ROTATION) {
					TransformGenerics.restoreTransObjects(t);
					initTrackball(t);
				}
				else {
					TransformGenerics.restoreTransObjects(t);
					initRotation(t);
				}
				TransformSnap.initSnapping(t, null); // need to reinit after mode change
				t.redraw = 1;
			}
			break;
		case WmEventTypes.CKEY:
			if (event.alt!=0) {
//				t.flag ^= T_PROP_CONNECTED;
//				sort_trans_data_dist(t);
//				calculatePropRatio(t);
//				t.redraw= 1;
			}
			else {
				TransformConstraints.stopConstraint(t);
				t.redraw = 1;
			}
			break;
		case WmEventTypes.XKEY:
			if ((t.flag & T_NO_CONSTRAINT)==0) {
				if (cmode == 'X') {
					if ((t.flag & T_2D_EDIT)!=0) {
						TransformConstraints.stopConstraint(t);
					}
					else {
						if ((t.con.mode & CON_USER)!=0) {
							TransformConstraints.stopConstraint(t);
						}
						else {
							if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
								TransformConstraints.setUserConstraint(t, (CON_AXIS0), "along %s X");
							else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0)
								TransformConstraints.setUserConstraint(t, (CON_AXIS1|CON_AXIS2), "locking %s X");
						}
					}
				}
				else {
					if ((t.flag & T_2D_EDIT)!=0) {
						TransformConstraints.setConstraint(t, mati, (CON_AXIS0), "along X axis");
					}
					else {
						if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
							TransformConstraints.setConstraint(t, mati, (CON_AXIS0), "along global X");
						else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0)
							TransformConstraints.setConstraint(t, mati, (CON_AXIS1|CON_AXIS2), "locking global X");
					}
				}
				t.redraw = 1;
			}
			break;
		case WmEventTypes.YKEY:
			if ((t.flag & T_NO_CONSTRAINT)==0) {
				if (cmode == 'Y') {
					if ((t.flag & T_2D_EDIT)!=0) {
						TransformConstraints.stopConstraint(t);
					}
					else {
						if ((t.con.mode & CON_USER)!=0) {
							TransformConstraints.stopConstraint(t);
						}
						else {
							if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
								TransformConstraints.setUserConstraint(t, (CON_AXIS1), "along %s Y");
							else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0)
								TransformConstraints.setUserConstraint(t, (CON_AXIS0|CON_AXIS2), "locking %s Y");
						}
					}
				}
				else {
					if ((t.flag & T_2D_EDIT)!=0) {
						TransformConstraints.setConstraint(t, mati, (CON_AXIS1), "along Y axis");
					}
					else {
						if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
							TransformConstraints.setConstraint(t, mati, (CON_AXIS1), "along global Y");
						else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0)
							TransformConstraints.setConstraint(t, mati, (CON_AXIS0|CON_AXIS2), "locking global Y");
					}
				}
				t.redraw = 1;
			}
			break;
		case WmEventTypes.ZKEY:
			if ((t.flag & T_NO_CONSTRAINT)==0) {
				if (cmode == 'Z') {
					if ((t.con.mode & CON_USER)!=0) {
						TransformConstraints.stopConstraint(t);
					}
					else {
						if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
							TransformConstraints.setUserConstraint(t, (CON_AXIS2), "along %s Z");
						else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0 && ((t.flag & T_2D_EDIT)==0))
							TransformConstraints.setUserConstraint(t, (CON_AXIS0|CON_AXIS1), "locking %s Z");
					}
				}
				else if ((t.flag & T_2D_EDIT)==0) {
					if ((t.modifiers & MOD_CONSTRAINT_PLANE) == 0)
						TransformConstraints.setConstraint(t, mati, (CON_AXIS2), "along global Z");
					else if ((t.modifiers & MOD_CONSTRAINT_PLANE)!=0)
						TransformConstraints.setConstraint(t, mati, (CON_AXIS0|CON_AXIS1), "locking global Z");
				}
				t.redraw = 1;
			}
			break;
		case WmEventTypes.OKEY:
			if ((t.flag & T_PROP_EDIT)!=0 && event.shift!=0) {
//				t.prop_mode = (t.prop_mode + 1) % 6;
//				calculatePropRatio(t);
//				t.redraw = 1;
			}
			break;
		case WmEventTypes.PADPLUSKEY:
			if(event.alt!=0 && (t.flag & T_PROP_EDIT)!=0) {
//				t.prop_size *= 1.1f;
//				calculatePropRatio(t);
			}
			t.redraw= 1;
			break;
		case WmEventTypes.PAGEUPKEY:
		case WmEventTypes.WHEELDOWNMOUSE:
			if ((t.flag & T_AUTOIK)!=0) {
//				transform_autoik_update(t, 1);
			}
			else if((t.flag & T_PROP_EDIT)!=0) {
//				t.prop_size*= 1.1f;
//				calculatePropRatio(t);
			}
//			else view_editmove(event.type);
			t.redraw= 1;
			break;
		case WmEventTypes.PADMINUS:
			if(event.alt!=0 && (t.flag & T_PROP_EDIT)!=0) {
//				t.prop_size*= 0.90909090f;
//				calculatePropRatio(t);
			}
			t.redraw= 1;
			break;
		case WmEventTypes.PAGEDOWNKEY:
		case WmEventTypes.WHEELUPMOUSE:
			if ((t.flag & T_AUTOIK)!=0) {
//				transform_autoik_update(t, -1);
			}
			else if ((t.flag & T_PROP_EDIT)!=0) {
//				t.prop_size*= 0.90909090f;
//				calculatePropRatio(t);
			}
//			else view_editmove(event.type);
			t.redraw= 1;
			break;
//		case NDOFMOTION:
//            viewmoveNDOF(1);
  //         break;
		}

		// Numerical input events
		t.redraw |= TransformNumInput.handleNumInput(t.num, event);

		// NDof input events
//		switch(TransformNDOFInput.handleNDofInput(t.ndof, event))
//		{
//			case NDOF_CONFIRM:
//				if ((t.options & CTX_NDOF) == 0)
//				{
//					/* Confirm on normal transform only */
//					t.state = TRANS_CONFIRM;
//				}
//				break;
//			case NDOF_CANCEL:
//				if (t.options & CTX_NDOF)
//				{
//					/* Cancel on pure NDOF transform */
//					t.state = TRANS_CANCEL;
//				}
//				else
//				{
//					/* Otherwise, just redraw, NDof input was cancelled */
//					t.redraw = 1;
//				}
//				break;
//			case NDOF_NOMOVE:
//				if (t.options & CTX_NDOF)
//				{
//					/* Confirm on pure NDOF transform */
//					t.state = TRANS_CONFIRM;
//				}
//				break;
//			case NDOF_REFRESH:
//				t.redraw = 1;
//				break;
//
//		}

		// Snapping events
//		t.redraw |= handleSnapping(t, event);

		//arrows_move_cursor(event.type);
	}
	else {
		switch (event.type){
		case WmEventTypes.LEFTMOUSE:
			t.state = TRANS_CONFIRM;
			break;
		case WmEventTypes.LEFTSHIFTKEY:
		case WmEventTypes.RIGHTSHIFTKEY:
			t.modifiers &= ~MOD_CONSTRAINT_PLANE;
			t.redraw = 1;
			break;

		case WmEventTypes.LEFTCTRLKEY:
		case WmEventTypes.RIGHTCTRLKEY:
			t.modifiers &= ~MOD_SNAP_GEARS;
			/* no redraw on release modifier keys! this makes sure you can assign the 'grid' still
			   after releasing modifer key */
			//t.redraw = 1;
			break;
		case WmEventTypes.MIDDLEMOUSE:
			if ((t.flag & T_NO_CONSTRAINT)==0) {
				t.modifiers &= ~MOD_CONSTRAINT_SELECT;
				TransformConstraints.postSelectConstraint(t);
				t.redraw = 1;
			}
			break;
//		case LEFTMOUSE:
//		case RIGHTMOUSE:
//			if(WM_modal_tweak_exit(event, t.event_type))
////			if (t.options & CTX_TWEAK)
//				t.state = TRANS_CONFIRM;
//			break;
		}
	}

	// Per transform event, if present
	if (t.handleEvent!=null)
		t.redraw |= t.handleEvent.run(t, event);
}

//int calculateTransformCenter(bContext *C, wmEvent *event, int centerMode, float *vec)
//{
//	TransInfo *t = MEM_callocN(sizeof(TransInfo), "TransInfo data");
//	int success = 1;
//
//	t.state = TRANS_RUNNING;
//
//	t.options = CTX_NONE;
//
//	t.mode = TFM_DUMMY;
//
//	initTransInfo(C, t, NULL, event);					// internal data, mouse, vectors
//
//	createTransData(C, t);			// make TransData structs from selection
//
//	t.around = centerMode; 			// override userdefined mode
//
//	if (t.total == 0) {
//		success = 0;
//	}
//	else {
//		success = 1;
//
//		calculateCenter(t);
//
//		// Copy center from constraint center. Transform center can be local
//		VECCOPY(vec, t.con.center);
//	}
//
//	postTrans(t);
//
//	/* aftertrans does insert ipos and action channels, and clears base flags, doesnt read transdata */
//	special_aftertrans_update(t);
//
//	MEM_freeN(t);
//
//	return success;
//}

static enum ArrowDirection {
	UP,
	DOWN,
	LEFT,
	RIGHT
};

static void drawArrow(GL2 gl, ArrowDirection d, int offset, int length, int size)
{
	switch(d)
	{
		case LEFT:
			offset = -offset;
			length = -length;
			size = -size;
		case RIGHT:
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2i( offset, 0);
			gl.glVertex2i( offset + length, 0);
			gl.glVertex2i( offset + length, 0);
			gl.glVertex2i( offset + length - size, -size);
			gl.glVertex2i( offset + length, 0);
			gl.glVertex2i( offset + length - size,  size);
			gl.glEnd();
			break;
		case DOWN:
			offset = -offset;
			length = -length;
			size = -size;
		case UP:
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2i( 0, offset);
			gl.glVertex2i( 0, offset + length);
			gl.glVertex2i( 0, offset + length);
			gl.glVertex2i(-size, offset + length - size);
			gl.glVertex2i( 0, offset + length);
			gl.glVertex2i( size, offset + length - size);
			gl.glEnd();
			break;
	}
}

static void drawArrowHead(GL2 gl, ArrowDirection d, int size)
{
	switch(d)
	{
		case LEFT:
			size = -size;
		case RIGHT:
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2i( 0, 0);
			gl.glVertex2i( -size, -size);
			gl.glVertex2i( 0, 0);
			gl.glVertex2i( -size,  size);
			gl.glEnd();
			break;
		case DOWN:
			size = -size;
		case UP:
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2i( 0, 0);
			gl.glVertex2i(-size, -size);
			gl.glVertex2i( 0, 0);
			gl.glVertex2i( size, -size);
			gl.glEnd();
			break;
	}
}

static void drawArc(GL2 gl, float size, float angle_start, float angle_end, int segments)
{
	float delta = (angle_end - angle_start) / segments;
	float angle;

	gl.glBegin(GL2.GL_LINE_STRIP);

	for( angle = angle_start; angle < angle_end; angle += delta)
	{
		gl.glVertex2f( (float)StrictMath.cos(angle) * size, (float)StrictMath.sin(angle) * size);
	}
	gl.glVertex2f( (float)StrictMath.cos(angle_end) * size, (float)StrictMath.sin(angle_end) * size);

	gl.glEnd();
}

public static void drawHelpline(GL2 gl, bContext C, TransInfo t)
{
	if (t.helpline != HLP_NONE && (t.flag & T_USES_MANIPULATOR)==0)
	{
		float[] vecrot = new float[3], cent = new float[2];

		UtilDefines.VECCOPY(vecrot, t.center);
		if((t.flag & T_EDIT)!=0) {
			bObject ob= t.obedit;
			if(ob!=null) Arithb.Mat4MulVecfl(ob.obmat, vecrot);
		}
		else if((t.flag & T_POSE)!=0) {
			bObject ob=t.poseobj;
			if(ob!=null) Arithb.Mat4MulVecfl(ob.obmat, vecrot);
		}

		projectFloatView(t, vecrot, cent);	// no overflow in extreme cases

		gl.glDisable(GL2.GL_DEPTH_TEST);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		Area.ED_region_pixelspace(gl, t.ar);

		switch(t.helpline)
		{
			case HLP_SPRING:
				Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

				GlUtil.setlinestyle(gl, 3);
				gl.glBegin(GL2.GL_LINE_STRIP);
				gl.glVertex2sv(t.mval,0);
				gl.glVertex2fv(cent,0);
				gl.glEnd();

				gl.glTranslatef(t.mval[0], t.mval[1], 0);
				gl.glRotatef(-180 / Arithb.M_PI * (float)StrictMath.atan2(cent[0] - t.mval[0], cent[1] - t.mval[1]), 0, 0, 1);

				GlUtil.setlinestyle(gl, 0);
				gl.glLineWidth(3.0f);
				drawArrow(gl, ArrowDirection.UP, 5, 10, 5);
				drawArrow(gl, ArrowDirection.DOWN, 5, 10, 5);
				gl.glLineWidth(1.0f);
				break;
			case HLP_HARROW:
				Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

				gl.glTranslatef(t.mval[0], t.mval[1], 0);

				gl.glLineWidth(3.0f);
				drawArrow(gl, ArrowDirection.RIGHT, 5, 10, 5);
				drawArrow(gl, ArrowDirection.LEFT, 5, 10, 5);
				gl.glLineWidth(1.0f);
				break;
			case HLP_VARROW:
				Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

				gl.glTranslatef(t.mval[0], t.mval[1], 0);

				gl.glLineWidth(3.0f);
				gl.glBegin(GL2.GL_LINES);
				drawArrow(gl, ArrowDirection.UP, 5, 10, 5);
				drawArrow(gl, ArrowDirection.DOWN, 5, 10, 5);
				gl.glLineWidth(1.0f);
				break;
			case HLP_ANGLE:
				{
					float dx = t.mval[0] - cent[0], dy = t.mval[1] - cent[1];
					float angle = (float)StrictMath.atan2(dy, dx);
					float dist = (float)StrictMath.sqrt(dx*dx + dy*dy);
					float delta_angle = UtilDefines.MIN2(15 / dist, Arithb.M_PI/4);
					float spacing_angle = UtilDefines.MIN2(5 / dist, Arithb.M_PI/12);
					Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

					GlUtil.setlinestyle(gl, 3);
					gl.glBegin(GL2.GL_LINE_STRIP);
					gl.glVertex2sv(t.mval,0);
					gl.glVertex2fv(cent,0);
					gl.glEnd();

					gl.glTranslatef(cent[0], cent[1], 0);

					GlUtil.setlinestyle(gl, 0);
					gl.glLineWidth(3.0f);
					drawArc(gl, dist, angle - delta_angle, angle - spacing_angle, 10);
					drawArc(gl, dist, angle + spacing_angle, angle + delta_angle, 10);

					gl.glPushMatrix();

					gl.glTranslatef((float)StrictMath.cos(angle - delta_angle) * dist, (float)StrictMath.sin(angle - delta_angle) * dist, 0);
					gl.glRotatef(180 / Arithb.M_PI * (angle - delta_angle), 0, 0, 1);

					drawArrowHead(gl, ArrowDirection.DOWN, 5);

					gl.glPopMatrix();

					gl.glTranslatef((float)StrictMath.cos(angle + delta_angle) * dist, (float)StrictMath.sin(angle + delta_angle) * dist, 0);
					gl.glRotatef(180 / Arithb.M_PI * (angle + delta_angle), 0, 0, 1);

					drawArrowHead(gl, ArrowDirection.UP, 5);

					gl.glLineWidth(1.0f);
					break;
				}
				case HLP_TRACKBALL:
				{
					byte[] col = new byte[3], col2 = new byte[3];
					Resources.UI_GetThemeColor3ubv(Resources.TH_GRID, col);

					gl.glTranslatef(t.mval[0], t.mval[1], 0);

					gl.glLineWidth(3.0f);

					Resources.UI_make_axis_color(col, col2, 'x');
					gl.glColor3ubv(col2,0);

					drawArrow(gl, ArrowDirection.RIGHT, 5, 10, 5);
					drawArrow(gl, ArrowDirection.LEFT, 5, 10, 5);

					Resources.UI_make_axis_color(col, col2, 'y');
					gl.glColor3ubv(col2,0);

					drawArrow(gl, ArrowDirection.UP, 5, 10, 5);
					drawArrow(gl, ArrowDirection.DOWN, 5, 10, 5);
					gl.glLineWidth(1.0f);
					break;
				}
		}

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPopMatrix();

		gl.glEnable(GL2.GL_DEPTH_TEST);
	}
}

public static RegionDrawCB.Draw drawTransform = new RegionDrawCB.Draw() {
public void run(GL2 gl, bContext C, ARegion ar, Object arg)
//void drawTransform(const struct bContext *C, struct ARegion *ar, void *arg)
{
	TransInfo t = (TransInfo)arg;

	TransformConstraints.drawConstraint(gl, C, t);
	TransformConstraints.drawPropCircle(gl, C, t);
//	drawSnapping(C, t);
	drawHelpline(gl, C, t);
}};

public static void saveTransform(bContext C, TransInfo t, wmOperator op)
{
//        System.out.println("saveTransform");
	ToolSettings ts = bContext.CTX_data_tool_settings(C);
	boolean[] constraint_axis = {false, false, false};
	int proportional = 0;

//	if ((t.flag & T_AUTOVALUES)!=0)
//	{
//		RnaAccess.RNA_float_set_array(op.ptr, "value", t.auto_values);
//	}
//	else
//	{
//		RnaAccess.RNA_float_set_array(op.ptr, "value", t.values);
//	}

	/* XXX convert stupid flag to enum */
	switch(t.flag & (T_PROP_EDIT|T_PROP_CONNECTED))
	{
	case (T_PROP_EDIT|T_PROP_CONNECTED):
		proportional = 2;
		break;
	case T_PROP_EDIT:
		proportional = 1;
		break;
	default:
		proportional = 0;
	}

	if (RnaAccess.RNA_struct_find_property(op.ptr, "proportional", true)!=null)
	{
		RnaAccess.RNA_enum_set(op.ptr, "proportional", proportional);
		RnaAccess.RNA_enum_set(op.ptr, "proportional_editing_falloff", t.prop_mode);
		RnaAccess.RNA_float_set(op.ptr, "proportional_size", t.prop_size);
	}

	if (RnaAccess.RNA_struct_find_property(op.ptr, "mirror", true)!=null)
	{
		RnaAccess.RNA_boolean_set(op.ptr, "mirror", (t.flag & T_MIRROR)!=0);
	}

	if (RnaAccess.RNA_struct_find_property(op.ptr, "constraint_axis", true)!=null)
	{
		RnaAccess.RNA_enum_set(op.ptr, "constraint_orientation", t.current_orientation);

		if ((t.con.mode & CON_APPLY)!=0)
		{
			if ((t.con.mode & CON_AXIS0)!=0) {
				constraint_axis[0] = true;
			}
			if ((t.con.mode & CON_AXIS1)!=0) {
				constraint_axis[1] = true;
			}
			if ((t.con.mode & CON_AXIS2)!=0) {
				constraint_axis[2] = true;
			}
		}

		RnaAccess.RNA_boolean_set_array(op.ptr, "constraint_axis", constraint_axis);
	}

	// XXX If modal, save settings back in scene
	if ((t.flag & T_MODAL)!=0)
	{
		ts.prop_mode = t.prop_mode;
		ts.proportional = (short)proportional;

		if(t.spacetype == SpaceTypes.SPACE_VIEW3D)
		{
			View3D v3d = (View3D)t.view;

			v3d.twmode = (byte)t.current_orientation;
		}
	}
}

public static int initTransform(bContext C, TransInfo t, wmOperator op, wmEvent event, int mode)
{
	int options = 0;

	/* added initialize, for external calls to set stuff in TransInfo, like undo string */

	t.state = TRANS_RUNNING;

	t.options = options;

	t.mode = mode;

	if (TransformGenerics.initTransInfo(C, t, op, event)==0)					// internal data, mouse, vectors
	{
		return 0;
	}

	if(t.spacetype == SpaceTypes.SPACE_VIEW3D)
	{
		//calc_manipulator_stats(curarea);
		TransformOrientations.initTransformOrientation(C, t);

		t.draw_handle = SpaceTypeUtil.ED_region_draw_cb_activate((ARegionType)t.ar.type, drawTransform, t, SpaceTypeUtil.REGION_DRAW_POST);
	}
	else if(t.spacetype == SpaceTypes.SPACE_IMAGE) {
		Arithb.Mat3One(t.spacemtx);
		t.draw_handle = SpaceTypeUtil.ED_region_draw_cb_activate((ARegionType)t.ar.type, drawTransform, t, SpaceTypeUtil.REGION_DRAW_POST);
	}
	else
		Arithb.Mat3One(t.spacemtx);

	TransformConversions.createTransData(C, t);			// make TransData structs from selection

	if (t.total == 0) {
		TransformGenerics.postTrans(t);
		return 0;
	}

	TransformSnap.initSnapping(t, op); // Initialize snapping data AFTER mode flags

	/* EVIL! posemode code can switch translation to rotate when 1 bone is selected. will be removed (ton) */
	/* EVIL2: we gave as argument also texture space context bit... was cleared */
	/* EVIL3: extend mode for animation editors also switches modes... but is best way to avoid duplicate code */
	mode = t.mode;

	TransformGenerics.calculatePropRatio(t);
	TransformGenerics.calculateCenter(t);

	TransformInput.initMouseInput(t, t.mouse, t.center2d, t.imval);

	switch (mode) {
	case Transform.TFM_TRANSLATION:
		initTranslation(t);
		break;
	case TFM_ROTATION:
		initRotation(t);
		break;
	case TFM_RESIZE:
		initResize(t);
		break;
//	case TFM_TOSPHERE:
//		initToSphere(t);
//		break;
//	case TFM_SHEAR:
//		initShear(t);
//		break;
//	case TFM_WARP:
//		initWarp(t);
//		break;
//	case TFM_SHRINKFATTEN:
//		initShrinkFatten(t);
//		break;
//	case TFM_TILT:
//		initTilt(t);
//		break;
//	case TFM_CURVE_SHRINKFATTEN:
//		initCurveShrinkFatten(t);
//		break;
//	case TFM_TRACKBALL:
//		initTrackball(t);
//		break;
//	case TFM_PUSHPULL:
//		initPushPull(t);
//		break;
//	case TFM_CREASE:
//		initCrease(t);
//		break;
//	case TFM_BONESIZE:
//		{	/* used for both B-Bone width (bonesize) as for deform-dist (envelope) */
//			bArmature *arm= t.poseobj.data;
//			if(arm.drawtype==ARM_ENVELOPE)
//				initBoneEnvelope(t);
//			else
//				initBoneSize(t);
//		}
//		break;
//	case TFM_BONE_ENVELOPE:
//		initBoneEnvelope(t);
//		break;
//	case TFM_BONE_ROLL:
//		initBoneRoll(t);
//		break;
//	case TFM_TIME_TRANSLATE:
//		initTimeTranslate(t);
//		break;
//	case TFM_TIME_SLIDE:
//		initTimeSlide(t);
//		break;
//	case TFM_TIME_SCALE:
//		initTimeScale(t);
//		break;
//	case TFM_TIME_EXTEND:
//		/* now that transdata has been made, do like for TFM_TIME_TRANSLATE (for most Animation
//		 * Editors because they have only 1D transforms for time values) or TFM_TRANSLATION
//		 * (for Graph/NLA Editors only since they uses 'standard' transforms to get 2D movement)
//		 * depending on which editor this was called from
//		 */
//		if ELEM(t.spacetype, SPACE_IPO, SPACE_NLA)
//			initTranslation(t);
//		else
//			initTimeTranslate(t);
//		break;
//	case TFM_BAKE_TIME:
//		initBakeTime(t);
//		break;
//	case TFM_MIRROR:
//		initMirror(t);
//		break;
//	case TFM_BEVEL:
//		initBevel(t);
//		break;
//	case TFM_BWEIGHT:
//		initBevelWeight(t);
//		break;
//	case TFM_ALIGN:
//		initAlign(t);
//		break;
	}

	/* overwrite initial values if operator supplied a non-null vector */
	if (RnaAccess.RNA_property_is_set(op.ptr, "value"))
	{
		float[] values = new float[4];
		RnaAccess.RNA_float_get_array(op.ptr, "value", values);
		UtilDefines.QUATCOPY(t.values, values);
		UtilDefines.QUATCOPY(t.auto_values, values);
		t.flag |= T_AUTOVALUES;
	}

	/* Constraint init from operator */
	if (RnaAccess.RNA_struct_find_property(op.ptr, "constraint_axis", true)!=null && RnaAccess.RNA_property_is_set(op.ptr, "constraint_axis"))
	{
		boolean[] constraint_axis = new boolean[3];

		RnaAccess.RNA_boolean_get_array(op.ptr, "constraint_axis", constraint_axis);

		if (constraint_axis[0] || constraint_axis[1] || constraint_axis[2])
		{
			t.con.mode |= CON_APPLY;

			if (constraint_axis[0]) {
				t.con.mode |= CON_AXIS0;
			}
			if (constraint_axis[1]) {
				t.con.mode |= CON_AXIS1;
			}
			if (constraint_axis[2]) {
				t.con.mode |= CON_AXIS2;
			}

			TransformConstraints.setUserConstraint(t, t.con.mode, "%s");
		}
	}

	return 1;
}

public static void transformApply(bContext C, TransInfo t)
{
//        System.out.println("transformApply");
	if (t.redraw!=0)
	{
		if ((t.modifiers & MOD_CONSTRAINT_SELECT)!=0)
			t.con.mode |= CON_SELECT;

		TransformConstraints.selectConstraint(t);
		if (t.transform!=null) {
			t.transform.run(t, t.mval);  // calls recalcData()
//                        System.out.println("transformApply viewRedrawForce:");
			viewRedrawForce(C, t);
		}
		t.redraw = 0;
	}

	/* If auto confirm is on, break after one pass */
	if ((t.options & CTX_AUTOCONFIRM)!=0)
	{
		t.state = TRANS_CONFIRM;
	}

//	if (BKE_ptcache_get_continue_physics())
//	{
//		// TRANSFORM_FIX_ME
//		//do_screenhandlers(G.curscreen);
//		t.redraw = 1;
//	}
}

public static int transformEnd(bContext C, TransInfo t)
{
//        System.out.println("transformEnd");
	int exit_code = WindowManagerTypes.OPERATOR_RUNNING_MODAL;

	if (t.state != TRANS_RUNNING)
	{
		/* handle restoring objects */
		if(t.state == TRANS_CANCEL)
		{
			exit_code = WindowManagerTypes.OPERATOR_CANCELLED;
//                        System.out.println("transformEnd OPERATOR_CANCELLED");
			TransformGenerics.restoreTransObjects(t);	// calls recalcData()
		}
		else
		{
			exit_code = WindowManagerTypes.OPERATOR_FINISHED;
		}

		/* free data */
		TransformGenerics.postTrans(t);

		/* aftertrans does insert keyframes, and clears base flags, doesnt read transdata */
		TransformConversions.special_aftertrans_update(t);

		/* send events out for redraws */
		viewRedrawPost(t);

		/*  Undo as last, certainly after special_trans_update! */

		if(t.state == TRANS_CANCEL) {
//			if(t.undostr) ED_undo_push(C, t.undostr);
		}
		else {
//			if(t.undostr) ED_undo_push(C, t.undostr);
//			else ED_undo_push(C, transform_to_undostr(t));
		}
//		t.undostr= null;

		viewRedrawForce(C, t);
	}

	return exit_code;
}

/* ************************** TRANSFORM LOCKS **************************** */

static void protectedTransBits(short protectflag, float[] vec)
{
	if((protectflag & ObjectTypes.OB_LOCK_LOCX)!=0)
		vec[0]= 0.0f;
	if((protectflag & ObjectTypes.OB_LOCK_LOCY)!=0)
		vec[1]= 0.0f;
	if((protectflag & ObjectTypes.OB_LOCK_LOCZ)!=0)
		vec[2]= 0.0f;
}

static void protectedSizeBits(short protectflag, float[] size)
{
	if((protectflag & ObjectTypes.OB_LOCK_SCALEX)!=0)
		size[0]= 1.0f;
	if((protectflag & ObjectTypes.OB_LOCK_SCALEY)!=0)
		size[1]= 1.0f;
	if((protectflag & ObjectTypes.OB_LOCK_SCALEZ)!=0)
		size[2]= 1.0f;
}

static void protectedRotateBits(short protectflag, float[] eul, float[] oldeul)
{
	if((protectflag & ObjectTypes.OB_LOCK_ROTX)!=0)
		eul[0]= oldeul[0];
	if((protectflag & ObjectTypes.OB_LOCK_ROTY)!=0)
		eul[1]= oldeul[1];
	if((protectflag & ObjectTypes.OB_LOCK_ROTZ)!=0)
		eul[2]= oldeul[2];
}

//static void protectedQuaternionBits(short protectflag, float *quat, float *oldquat)
//{
//	/* quaternions get limited with euler... */
//	/* this function only does the delta rotation */
//
//	if(protectflag) {
//		float eul[3], oldeul[3], quat1[4];
//
//		QUATCOPY(quat1, quat);
//		QuatToEul(quat, eul);
//		QuatToEul(oldquat, oldeul);
//
//		if(protectflag & OB_LOCK_ROTX)
//			eul[0]= oldeul[0];
//		if(protectflag & OB_LOCK_ROTY)
//			eul[1]= oldeul[1];
//		if(protectflag & OB_LOCK_ROTZ)
//			eul[2]= oldeul[2];
//
//		EulToQuat(eul, quat);
//		/* quaternions flip w sign to accumulate rotations correctly */
//		if( (quat1[0]<0.0f && quat[0]>0.0f) || (quat1[0]>0.0f && quat[0]<0.0f) ) {
//			QuatMulf(quat, -1.0f);
//		}
//	}
//}

/* ******************* TRANSFORM LIMITS ********************** */

static void constraintTransLim(TransInfo t, TransData td)
{
//	if (td.con!=null) {
//		bConstraintTypeInfo *cti= get_constraint_typeinfo(CONSTRAINT_TYPE_LOCLIMIT);
//		bConstraintOb cob;
//		bConstraint *con;
//
//		/* Make a temporary bConstraintOb for using these limit constraints
//		 * 	- they only care that cob.matrix is correctly set ;-)
//		 *	- current space should be local
//		 */
//		memset(&cob, 0, sizeof(bConstraintOb));
//		Mat4One(cob.matrix);
//		if (td.tdi) {
//			TransDataIpokey *tdi= td.tdi;
//			cob.matrix[3][0]= tdi.locx[0];
//			cob.matrix[3][1]= tdi.locy[0];
//			cob.matrix[3][2]= tdi.locz[0];
//		}
//		else {
//			VECCOPY(cob.matrix[3], td.loc);
//		}
//
//		/* Evaluate valid constraints */
//		for (con= td.con; con; con= con.next) {
//			float tmat[4][4];
//
//			/* only consider constraint if enabled */
//			if (con.flag & CONSTRAINT_DISABLE) continue;
//			if (con.enforce == 0.0f) continue;
//
//			/* only use it if it's tagged for this purpose (and the right type) */
//			if (con.type == CONSTRAINT_TYPE_LOCLIMIT) {
//				bLocLimitConstraint *data= con.data;
//
//				if ((data.flag2 & LIMIT_TRANSFORM)==0)
//					continue;
//
//				/* do space conversions */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.mtx, tmat);
//				}
//				else if (con.ownspace != CONSTRAINT_SPACE_LOCAL) {
//					/* skip... incompatable spacetype */
//					continue;
//				}
//
//				/* do constraint */
//				cti.evaluate_constraint(con, &cob, NULL);
//
//				/* convert spaces again */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.smtx, tmat);
//				}
//			}
//		}
//
//		/* copy results from cob.matrix */
//		if (td.tdi) {
//			TransDataIpokey *tdi= td.tdi;
//			tdi.locx[0]= cob.matrix[3][0];
//			tdi.locy[0]= cob.matrix[3][1];
//			tdi.locz[0]= cob.matrix[3][2];
//		}
//		else {
//			VECCOPY(td.loc, cob.matrix[3]);
//		}
//	}
}

static void constraintRotLim(TransInfo t, TransData td)
{
//	if (td.con) {
//		bConstraintTypeInfo *cti= get_constraint_typeinfo(CONSTRAINT_TYPE_ROTLIMIT);
//		bConstraintOb cob;
//		bConstraint *con;
//
//		/* Make a temporary bConstraintOb for using these limit constraints
//		 * 	- they only care that cob.matrix is correctly set ;-)
//		 *	- current space should be local
//		 */
//		memset(&cob, 0, sizeof(bConstraintOb));
//		if (td.flag & TD_USEQUAT) {
//			/* quats */
//			if (td.ext)
//				QuatToMat4(td.ext.quat, cob.matrix);
//			else
//				return;
//		}
//		else if (td.tdi) {
//			/* ipo-keys eulers */
//			TransDataIpokey *tdi= td.tdi;
//			float eul[3];
//
//			eul[0]= tdi.rotx[0];
//			eul[1]= tdi.roty[0];
//			eul[2]= tdi.rotz[0];
//
//			EulToMat4(eul, cob.matrix);
//		}
//		else {
//			/* eulers */
//			if (td.ext)
//				EulToMat4(td.ext.rot, cob.matrix);
//			else
//				return;
//		}
//
//		/* Evaluate valid constraints */
//		for (con= td.con; con; con= con.next) {
//			/* only consider constraint if enabled */
//			if (con.flag & CONSTRAINT_DISABLE) continue;
//			if (con.enforce == 0.0f) continue;
//
//			/* we're only interested in Limit-Rotation constraints */
//			if (con.type == CONSTRAINT_TYPE_ROTLIMIT) {
//				bRotLimitConstraint *data= con.data;
//				float tmat[4][4];
//
//				/* only use it if it's tagged for this purpose */
//				if ((data.flag2 & LIMIT_TRANSFORM)==0)
//					continue;
//
//				/* do space conversions */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.mtx, tmat);
//				}
//				else if (con.ownspace != CONSTRAINT_SPACE_LOCAL) {
//					/* skip... incompatable spacetype */
//					continue;
//				}
//
//				/* do constraint */
//				cti.evaluate_constraint(con, &cob, NULL);
//
//				/* convert spaces again */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.smtx, tmat);
//				}
//			}
//		}
//
//		/* copy results from cob.matrix */
//		if (td.flag & TD_USEQUAT) {
//			/* quats */
//			Mat4ToQuat(cob.matrix, td.ext.quat);
//		}
//		else if (td.tdi) {
//			/* ipo-keys eulers */
//			TransDataIpokey *tdi= td.tdi;
//			float eul[3];
//
//			Mat4ToEul(cob.matrix, eul);
//
//			tdi.rotx[0]= eul[0];
//			tdi.roty[0]= eul[1];
//			tdi.rotz[0]= eul[2];
//		}
//		else {
//			/* eulers */
//			Mat4ToEul(cob.matrix, td.ext.rot);
//		}
//	}
}

static void constraintSizeLim(TransInfo t, TransData td)
{
//	if (td.con && td.ext) {
//		bConstraintTypeInfo *cti= get_constraint_typeinfo(CONSTRAINT_TYPE_SIZELIMIT);
//		bConstraintOb cob;
//		bConstraint *con;
//
//		/* Make a temporary bConstraintOb for using these limit constraints
//		 * 	- they only care that cob.matrix is correctly set ;-)
//		 *	- current space should be local
//		 */
//		memset(&cob, 0, sizeof(bConstraintOb));
//		if (td.tdi) {
//			TransDataIpokey *tdi= td.tdi;
//			float size[3];
//
//			size[0]= tdi.sizex[0];
//			size[1]= tdi.sizey[0];
//			size[2]= tdi.sizez[0];
//			SizeToMat4(size, cob.matrix);
//		}
//		else if ((td.flag & TD_SINGLESIZE) && !(t.con.mode & CON_APPLY)) {
//			/* scale val and reset size */
//			return; // TODO: fix this case
//		}
//		else {
//			/* Reset val if SINGLESIZE but using a constraint */
//			if (td.flag & TD_SINGLESIZE)
//				return;
//
//			SizeToMat4(td.ext.size, cob.matrix);
//		}
//
//		/* Evaluate valid constraints */
//		for (con= td.con; con; con= con.next) {
//			/* only consider constraint if enabled */
//			if (con.flag & CONSTRAINT_DISABLE) continue;
//			if (con.enforce == 0.0f) continue;
//
//			/* we're only interested in Limit-Scale constraints */
//			if (con.type == CONSTRAINT_TYPE_SIZELIMIT) {
//				bSizeLimitConstraint *data= con.data;
//				float tmat[4][4];
//
//				/* only use it if it's tagged for this purpose */
//				if ((data.flag2 & LIMIT_TRANSFORM)==0)
//					continue;
//
//				/* do space conversions */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.mtx, tmat);
//				}
//				else if (con.ownspace != CONSTRAINT_SPACE_LOCAL) {
//					/* skip... incompatable spacetype */
//					continue;
//				}
//
//				/* do constraint */
//				cti.evaluate_constraint(con, &cob, NULL);
//
//				/* convert spaces again */
//				if (con.ownspace == CONSTRAINT_SPACE_WORLD) {
//					/* just multiply by td.mtx (this should be ok) */
//					Mat4CpyMat4(tmat, cob.matrix);
//					Mat4MulMat34(cob.matrix, td.smtx, tmat);
//				}
//			}
//		}
//
//		/* copy results from cob.matrix */
//		if (td.tdi) {
//			TransDataIpokey *tdi= td.tdi;
//			float size[3];
//
//			Mat4ToSize(cob.matrix, size);
//
//			tdi.sizex[0]= size[0];
//			tdi.sizey[0]= size[1];
//			tdi.sizez[0]= size[2];
//		}
//		else if ((td.flag & TD_SINGLESIZE) && !(t.con.mode & CON_APPLY)) {
//			/* scale val and reset size */
//			return; // TODO: fix this case
//		}
//		else {
//			/* Reset val if SINGLESIZE but using a constraint */
//			if (td.flag & TD_SINGLESIZE)
//				return;
//
//			Mat4ToSize(cob.matrix, td.ext.size);
//		}
//	}
}

///* ************************** WARP *************************** */
//
//void initWarp(TransInfo *t)
//{
//	float max[3], min[3];
//	int i;
//
//	t.mode = TFM_WARP;
//	t.transform = Warp;
//	t.handleEvent = handleEventWarp;
//
//	initMouseInputMode(t, &t.mouse, INPUT_HORIZONTAL_RATIO);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 5.0f;
//	t.snap[2] = 1.0f;
//
//	t.flag |= T_NO_CONSTRAINT;
//
//	/* we need min/max in view space */
//	for(i = 0; i < t.total; i++) {
//		float center[3];
//		VECCOPY(center, t.data[i].center);
//		Mat3MulVecfl(t.data[i].mtx, center);
//		Mat4MulVecfl(t.viewmat, center);
//		VecSubf(center, center, t.viewmat[3]);
//		if (i)
//			MinMax3(min, max, center);
//		else {
//			VECCOPY(max, center);
//			VECCOPY(min, center);
//		}
//	}
//
//	t.center[0]= (min[0]+max[0])/2.0f;
//	t.center[1]= (min[1]+max[1])/2.0f;
//	t.center[2]= (min[2]+max[2])/2.0f;
//
//	if (max[0] == min[0]) max[0] += 0.1; /* not optimal, but flipping is better than invalid garbage (i.e. division by zero!) */
//	t.val= (max[0]-min[0])/2.0f; /* t.val is X dimension projected boundbox */
//}
//
//int handleEventWarp(TransInfo *t, wmEvent *event)
//{
//	int status = 0;
//
//	if (event.type == MIDDLEMOUSE && event.val)
//	{
//		// Use customData pointer to signal warp direction
//		if	(t.customData == 0)
//			t.customData = (void*)1;
//		else
//			t.customData = 0;
//
//		status = 1;
//	}
//
//	return status;
//}
//
//int Warp(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float vec[3], circumfac, dist, phi0, co, si, *curs, cursor[3], gcursor[3];
//	int i;
//	char str[50];
//
//	curs= give_cursor(t.scene, t.view);
//	/*
//	 * gcursor is the one used for helpline.
//	 * It has to be in the same space as the drawing loop
//	 * (that means it needs to be in the object's space when in edit mode and
//	 *  in global space in object mode)
//	 *
//	 * cursor is used for calculations.
//	 * It needs to be in view space, but we need to take object's offset
//	 * into account if in Edit mode.
//	 */
//	VECCOPY(cursor, curs);
//	VECCOPY(gcursor, cursor);
//	if (t.flag & T_EDIT) {
//		VecSubf(cursor, cursor, t.obedit.obmat[3]);
//		VecSubf(gcursor, gcursor, t.obedit.obmat[3]);
//		Mat3MulVecfl(t.data.smtx, gcursor);
//	}
//	Mat4MulVecfl(t.viewmat, cursor);
//	VecSubf(cursor, cursor, t.viewmat[3]);
//
//	/* amount of degrees for warp */
//	circumfac = 360.0f * t.values[0];
//
//	if (t.customData) /* non-null value indicates reversed input */
//	{
//		circumfac *= -1;
//	}
//
//	snapGrid(t, &circumfac);
//	applyNumInput(&t.num, &circumfac);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Warp: %s", c);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "Warp: %.3f", circumfac);
//	}
//
//	circumfac*= (float)(-M_PI/360.0);
//
//	for(i = 0; i < t.total; i++, td++) {
//		float loc[3];
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		/* translate point to center, rotate in such a way that outline==distance */
//		VECCOPY(vec, td.iloc);
//		Mat3MulVecfl(td.mtx, vec);
//		Mat4MulVecfl(t.viewmat, vec);
//		VecSubf(vec, vec, t.viewmat[3]);
//
//		dist= vec[0]-cursor[0];
//
//		/* t.val is X dimension projected boundbox */
//		phi0= (circumfac*dist/t.val);
//
//		vec[1]= (vec[1]-cursor[1]);
//
//		co= (float)cos(phi0);
//		si= (float)sin(phi0);
//		loc[0]= -si*vec[1]+cursor[0];
//		loc[1]= co*vec[1]+cursor[1];
//		loc[2]= vec[2];
//
//		Mat4MulVecfl(t.viewinv, loc);
//		VecSubf(loc, loc, t.viewinv[3]);
//		Mat3MulVecfl(td.smtx, loc);
//
//		VecSubf(loc, loc, td.iloc);
//		VecMulf(loc, td.factor);
//		VecAddf(td.loc, td.iloc, loc);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** SHEAR *************************** */
//
//void initShear(TransInfo *t)
//{
//	t.mode = TFM_SHEAR;
//	t.transform = Shear;
//	t.handleEvent = handleEventShear;
//
//	initMouseInputMode(t, &t.mouse, INPUT_HORIZONTAL_ABSOLUTE);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int handleEventShear(TransInfo *t, wmEvent *event)
//{
//	int status = 0;
//
//	if (event.type == MIDDLEMOUSE && event.val)
//	{
//		// Use customData pointer to signal Shear direction
//		if	(t.customData == 0)
//		{
//			initMouseInputMode(t, &t.mouse, INPUT_VERTICAL_ABSOLUTE);
//			t.customData = (void*)1;
//		}
//		else
//		{
//			initMouseInputMode(t, &t.mouse, INPUT_HORIZONTAL_ABSOLUTE);
//			t.customData = 0;
//		}
//
//		status = 1;
//	}
//
//	return status;
//}
//
//
//int Shear(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float vec[3];
//	float smat[3][3], tmat[3][3], totmat[3][3], persmat[3][3], persinv[3][3];
//	float value;
//	int i;
//	char str[50];
//
//	Mat3CpyMat4(persmat, t.viewmat);
//	Mat3Inv(persinv, persmat);
//
//	value = 0.05f * t.values[0];
//
//	snapGrid(t, &value);
//
//	applyNumInput(&t.num, &value);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Shear: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "Shear: %.3f %s", value, t.proptext);
//	}
//
//	Mat3One(smat);
//
//	// Custom data signals shear direction
//	if (t.customData == 0)
//		smat[1][0] = value;
//	else
//		smat[0][1] = value;
//
//	Mat3MulMat3(tmat, smat, persmat);
//	Mat3MulMat3(totmat, persinv, tmat);
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if (t.obedit) {
//			float mat3[3][3];
//			Mat3MulMat3(mat3, totmat, td.mtx);
//			Mat3MulMat3(tmat, td.smtx, mat3);
//		}
//		else {
//			Mat3CpyMat3(tmat, totmat);
//		}
//		VecSubf(vec, td.center, t.center);
//
//		Mat3MulVecfl(tmat, vec);
//
//		VecAddf(vec, vec, t.center);
//		VecSubf(vec, vec, td.center);
//
//		VecMulf(vec, td.factor);
//
//		VecAddf(td.loc, td.iloc, vec);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}

/* ************************** RESIZE *************************** */

public static void initResize(TransInfo t)
{
	t.mode = TFM_RESIZE;
	t.transform = Resize;

	TransformInput.initMouseInputMode(t, t.mouse, MouseInputMode.INPUT_SPRING_FLIP);

	t.flag |= T_NULL_ONE;
	t.num.flag |= NUM_NULL_ONE;
	t.num.flag |= NUM_AFFECT_ALL;
	if (t.obedit==null) {
		t.flag |= T_NO_ZERO;
		t.num.flag |= NUM_NO_ZERO;
	}

	t.idx_max = 2;
	t.num.idx_max = 2;
	t.snap[0] = 0.0f;
	t.snap[1] = 0.1f;
	t.snap[2] = t.snap[1] * 0.1f;
}

//static void headerResize(TransInfo *t, float vec[3], char *str) {
//	char tvec[60];
//	if (hasNumInput(&t.num)) {
//		outputNumInput(&(t.num), tvec);
//	}
//	else {
//		sprintf(&tvec[0], "%.4f", vec[0]);
//		sprintf(&tvec[20], "%.4f", vec[1]);
//		sprintf(&tvec[40], "%.4f", vec[2]);
//	}
//
//	if (t.con.mode & CON_APPLY) {
//		switch(t.num.idx_max) {
//		case 0:
//			sprintf(str, "Scale: %s%s %s", &tvec[0], t.con.text, t.proptext);
//			break;
//		case 1:
//			sprintf(str, "Scale: %s : %s%s %s", &tvec[0], &tvec[20], t.con.text, t.proptext);
//			break;
//		case 2:
//			sprintf(str, "Scale: %s : %s : %s%s %s", &tvec[0], &tvec[20], &tvec[40], t.con.text, t.proptext);
//		}
//	}
//	else {
//		if (t.flag & T_2D_EDIT)
//			sprintf(str, "Scale X: %s   Y: %s%s %s", &tvec[0], &tvec[20], t.con.text, t.proptext);
//		else
//			sprintf(str, "Scale X: %s   Y: %s  Z: %s%s %s", &tvec[0], &tvec[20], &tvec[40], t.con.text, t.proptext);
//	}
//}

    private static final int SIGN(float a) {
        return (a < -Arithb.FLT_EPSILON) ? 1 : (a > Arithb.FLT_EPSILON) ? 2 : 3;
    }

    private static final boolean VECSIGNFLIP(float[] a, float[] b) {
        return (SIGN(a[0]) & SIGN(b[0])) == 0 || (SIGN(a[1]) & SIGN(b[1])) == 0 || (SIGN(a[2]) & SIGN(b[2])) == 0;
    }

/* smat is reference matrix, only scaled */
static void TransMat3ToSize(float[][] mat, float[][] smat, float[] size)
{
	float[] vec = new float[3];

	Arithb.VecCopyf(vec, mat[0]);
	size[0]= Arithb.Normalize(vec);
	Arithb.VecCopyf(vec, mat[1]);
	size[1]= Arithb.Normalize(vec);
	Arithb.VecCopyf(vec, mat[2]);
	size[2]= Arithb.Normalize(vec);

	/* first tried with dotproduct... but the sign flip is crucial */
	if( VECSIGNFLIP(mat[0], smat[0]) ) size[0]= -size[0];
	if( VECSIGNFLIP(mat[1], smat[1]) ) size[1]= -size[1];
	if( VECSIGNFLIP(mat[2], smat[2]) ) size[2]= -size[2];
}


static void ElementResize(TransInfo t, TransData td, float[][] mat) {
	float[][] tmat = new float[3][3], smat = new float[3][3];
        float[] center = new float[3];
	float[] vec = new float[3];

	if ((t.flag & T_EDIT)!=0) {
		Arithb.Mat3MulMat3(smat, mat, td.mtx);
		Arithb.Mat3MulMat3(tmat, td.smtx, smat);
	}
	else {
		Arithb.Mat3CpyMat3(tmat, mat);
	}

	if (t.con.applySize!=null) {
		t.con.applySize.run(t, td, tmat);
	}

	/* local constraint shouldn't alter center */
	if (t.around == View3dTypes.V3D_LOCAL) {
//		if (t.flag & T_OBJECT) {
//			VECCOPY(center, td.center);
//		}
//		else if (t.flag & T_EDIT) {
//
//			if(t.around==V3D_LOCAL && (t.settings.selectmode & SCE_SELECT_FACE)) {
//				VECCOPY(center, td.center);
//			}
//			else {
//				VECCOPY(center, t.center);
//			}
//		}
//		else {
//			VECCOPY(center, t.center);
//		}
	}
	else {
		UtilDefines.VECCOPY(center, t.center);
	}

	if (td.ext!=null) {
		float[] fsize = new float[3];

		if ((t.flag & (T_OBJECT|T_TEXTURE|T_POSE))!=0) {
			float[][] obsizemat = new float[3][3];
			// Reorient the size mat to fit the oriented object.
			Arithb.Mat3MulMat3(obsizemat, tmat, td.axismtx);
			//printmatrix3("obsizemat", obsizemat);
			TransMat3ToSize(obsizemat, td.axismtx, fsize);
			//printvecf("fsize", fsize);
		}
		else {
			Arithb.Mat3ToSize(tmat, fsize);
		}

		protectedSizeBits(td.protectflag, fsize);

		if ((t.flag & T_V3D_ALIGN)==0) {	// align mode doesn't resize objects itself
			/* handle ipokeys? */
//			if(td.tdi) {
//				TransDataIpokey *tdi= td.tdi;
//				/* calculate delta size (equal for size and dsize) */
//
//				vec[0]= (tdi.oldsize[0])*(fsize[0] -1.0f) * td.factor;
//				vec[1]= (tdi.oldsize[1])*(fsize[1] -1.0f) * td.factor;
//				vec[2]= (tdi.oldsize[2])*(fsize[2] -1.0f) * td.factor;
//
//				add_tdi_poin(tdi.sizex, tdi.oldsize,   vec[0]);
//				add_tdi_poin(tdi.sizey, tdi.oldsize+1, vec[1]);
//				add_tdi_poin(tdi.sizez, tdi.oldsize+2, vec[2]);
//
//			}
//			else if((td.flag & TD_SINGLESIZE) && !(t.con.mode & CON_APPLY)){
                        if((td.flag & TD_SINGLESIZE)!=0 && (t.con.mode & CON_APPLY)==0){
				/* scale val and reset size */
 				td.val.set(td.ival * fsize[0] * td.factor);

				td.ext.size[0] = td.ext.isize[0];
				td.ext.size[1] = td.ext.isize[1];
				td.ext.size[2] = td.ext.isize[2];
 			}
			else {
				/* Reset val if SINGLESIZE but using a constraint */
				if ((td.flag & TD_SINGLESIZE)!=0)
	 				td.val.set(td.ival);

				td.ext.size[0] = td.ext.isize[0] * (fsize[0]) * td.factor;
				td.ext.size[1] = td.ext.isize[1] * (fsize[1]) * td.factor;
				td.ext.size[2] = td.ext.isize[2] * (fsize[2]) * td.factor;
			}
		}

		constraintSizeLim(t, td);
	}

	/* For individual element center, Editmode need to use iloc */
	if ((t.flag & T_POINTS)!=0)
		Arithb.VecSubf(vec, td.iloc, center);
	else
		Arithb.VecSubf(vec, td.center, center);

	Arithb.Mat3MulVecfl(tmat, vec);

	Arithb.VecAddf(vec, vec, center);
	if ((t.flag & T_POINTS)!=0)
		Arithb.VecSubf(vec, vec, td.iloc);
	else
		Arithb.VecSubf(vec, vec, td.center);

	Arithb.VecMulf(vec, td.factor);

	if ((t.flag & (T_OBJECT|T_POSE))!=0) {
		Arithb.Mat3MulVecfl(td.smtx, vec);
	}

	protectedTransBits(td.protectflag, vec);

//	if(td.tdi) {
//		TransDataIpokey *tdi= td.tdi;
//		add_tdi_poin(tdi.locx, tdi.oldloc, vec[0]);
//		add_tdi_poin(tdi.locy, tdi.oldloc+1, vec[1]);
//		add_tdi_poin(tdi.locz, tdi.oldloc+2, vec[2]);
//	}
//	else
            Arithb.VecAddf(td.loc, td.iloc, vec);

	constraintTransLim(t, td);
}

public static TransformFunc Resize = new TransformFunc() {
public int run(TransInfo t, short[] mval)
//int Resize(TransInfo *t, short mval[2])
{
	TransData[] tds;
        int td_p = 0;
	float[] size = new float[3];
        float[][] mat = new float[3][3];
	float ratio;
	int i;
//	char str[200];

	/* for manipulator, center handle, the scaling can't be done relative to center */
	if( (t.flag & T_USES_MANIPULATOR)!=0 && t.con.mode==0)
	{
		ratio = 1.0f - ((t.imval[0] - mval[0]) + (t.imval[1] - mval[1]))/100.0f;
	}
	else
	{
		ratio = t.values[0];
	}

	size[0] = size[1] = size[2] = ratio;

	TransformSnap.snapGrid(t, size);

//	if (hasNumInput(&t.num)) {
//		applyNumInput(&t.num, size);
//		constraintNumInput(t, size);
//	}

	TransformSnap.applySnapping(t, size);

	if ((t.flag & T_AUTOVALUES)!=0)
	{
		UtilDefines.VECCOPY(size, t.auto_values);
	}

	UtilDefines.VECCOPY(t.values, size);

	Arithb.SizeToMat3(size, mat);

	if (t.con.applySize!=null) {
		t.con.applySize.run(t, null, mat);
	}

	Arithb.Mat3CpyMat3(t.mat, mat);	// used in manipulator

//	headerResize(t, size, str);

	for(i = 0, tds=t.data; i < t.total; i++, td_p++) {
                TransData td = t.data[td_p];
		if ((td.flag & TD_NOACTION)!=0)
			break;

		if ((td.flag & TD_SKIP)!=0)
			continue;

		ElementResize(t, td, mat);
	}

	/* evil hack - redo resize if cliping needed */
//	if (t.flag & T_CLIP_UV && clipUVTransform(t, size, 1)) {
//		SizeToMat3(size, mat);
//
//		if (t.con.applySize)
//			t.con.applySize(t, NULL, mat);
//
//		for(i = 0, td=t.data; i < t.total; i++, td++)
//			ElementResize(t, td, mat);
//	}

	TransformGenerics.recalcData(t);

//	ED_area_headerprint(t.sa, str);

	return 1;
}};

///* ************************** TOSPHERE *************************** */
//
//void initToSphere(TransInfo *t)
//{
//	TransData *td = t.data;
//	int i;
//
//	t.mode = TFM_TOSPHERE;
//	t.transform = ToSphere;
//
//	initMouseInputMode(t, &t.mouse, INPUT_HORIZONTAL_RATIO);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.num.flag |= NUM_NULL_ONE | NUM_NO_NEGATIVE;
//	t.flag |= T_NO_CONSTRAINT;
//
//	// Calculate average radius
//	for(i = 0 ; i < t.total; i++, td++) {
//		t.val += VecLenf(t.center, td.iloc);
//	}
//
//	t.val /= (float)t.total;
//}
//
//int ToSphere(TransInfo *t, short mval[2])
//{
//	float vec[3];
//	float ratio, radius;
//	int i;
//	char str[64];
//	TransData *td = t.data;
//
//	ratio = t.values[0];
//
//	snapGrid(t, &ratio);
//
//	applyNumInput(&t.num, &ratio);
//
//	if (ratio < 0)
//		ratio = 0.0f;
//	else if (ratio > 1)
//		ratio = 1.0f;
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "To Sphere: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "To Sphere: %.4f %s", ratio, t.proptext);
//	}
//
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		float tratio;
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		VecSubf(vec, td.iloc, t.center);
//
//		radius = Normalize(vec);
//
//		tratio = ratio * td.factor;
//
//		VecMulf(vec, radius * (1.0f - tratio) + t.val * tratio);
//
//		VecAddf(td.loc, t.center, vec);
//	}
//
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}

/* ************************** ROTATION *************************** */


public static void initRotation(TransInfo t)
{
	t.mode = TFM_ROTATION;
	t.transform = Rotation;

	TransformInput.initMouseInputMode(t, t.mouse, MouseInputMode.INPUT_ANGLE);

	t.ndof.axis = 16;
	/* Scale down and flip input for rotation */
	t.ndof.factor[0] = -0.2f;

	t.idx_max = 0;
	t.num.idx_max = 0;
	t.snap[0] = 0.0f;
	t.snap[1] = (float)((5.0/180)*Arithb.M_PI);
	t.snap[2] = t.snap[1] * 0.2f;

	if ((t.flag & T_2D_EDIT)!=0)
		t.flag |= T_NO_CONSTRAINT;
}

static void ElementRotation(TransInfo t, TransData td, float[][] mat, int around) {
	float[] vec = new float[3];
        float[][] totmat = new float[3][3], smat = new float[3][3];
	float[] eul = new float[3];
        float[][] fmat = new float[3][3];
        float[] quat = new float[4];
	float[] center = t.center;

	/* local constraint shouldn't alter center */
//	if (around == V3D_LOCAL) {
//		if (t.flag & (T_OBJECT|T_POSE)) {
//			center = td.center;
//		}
//		else {
//			/* !TODO! Make this if not rely on G */
//			if(around==V3D_LOCAL && (t.settings.selectmode & SCE_SELECT_FACE)) {
//				center = td.center;
//			}
//		}
//	}

	if ((t.flag & T_POINTS)!=0) {
		Arithb.Mat3MulMat3(totmat, mat, td.mtx);
		Arithb.Mat3MulMat3(smat, td.smtx, totmat);

		Arithb.VecSubf(vec, td.iloc, center);
		Arithb.Mat3MulVecfl(smat, vec);

		Arithb.VecAddf(td.loc, vec, center);

		Arithb.VecSubf(vec,td.loc,td.iloc);
		protectedTransBits(td.protectflag, vec);
		Arithb.VecAddf(td.loc, td.iloc, vec);

//		if(td.flag & TD_USEQUAT) {
//			Mat3MulSerie(fmat, td.mtx, mat, td.smtx, 0, 0, 0, 0, 0);
//			Mat3ToQuat(fmat, quat);	// Actual transform
//
//			if(td.ext.quat){
//				QuatMul(td.ext.quat, quat, td.ext.iquat);
//
//				/* is there a reason not to have this here? -jahka */
//				protectedQuaternionBits(td.protectflag, td.ext.quat, td.ext.iquat);
//			}
//		}
	}
	/**
	 * HACK WARNING
	 *
	 * This is some VERY ugly special case to deal with pose mode.
	 *
	 * The problem is that mtx and smtx include each bone orientation.
	 *
	 * That is needed to rotate each bone properly, HOWEVER, to calculate
	 * the translation component, we only need the actual armature object's
	 * matrix (and inverse). That is not all though. Once the proper translation
	 * has been computed, it has to be converted back into the bone's space.
	 */
//	else if (t.flag & T_POSE) {
//		float pmtx[3][3], imtx[3][3];
//
//		// Extract and invert armature object matrix
//		Mat3CpyMat4(pmtx, t.poseobj.obmat);
//		Mat3Inv(imtx, pmtx);
//
//		if ((td.flag & TD_NO_LOC) == 0)
//		{
//			VecSubf(vec, td.center, center);
//
//			Mat3MulVecfl(pmtx, vec);	// To Global space
//			Mat3MulVecfl(mat, vec);		// Applying rotation
//			Mat3MulVecfl(imtx, vec);	// To Local space
//
//			VecAddf(vec, vec, center);
//			/* vec now is the location where the object has to be */
//
//			VecSubf(vec, vec, td.center); // Translation needed from the initial location
//
//			Mat3MulVecfl(pmtx, vec);	// To Global space
//			Mat3MulVecfl(td.smtx, vec);// To Pose space
//
//			protectedTransBits(td.protectflag, vec);
//
//			VecAddf(td.loc, td.iloc, vec);
//
//			constraintTransLim(t, td);
//		}
//
//		/* rotation */
//		if ((t.flag & T_V3D_ALIGN)==0) { // align mode doesn't rotate objects itself
//			/* euler or quaternion? */
//			if (td.flag & TD_USEQUAT) {
//				Mat3MulSerie(fmat, td.mtx, mat, td.smtx, 0, 0, 0, 0, 0);
//
//				Mat3ToQuat(fmat, quat);	// Actual transform
//
//				QuatMul(td.ext.quat, quat, td.ext.iquat);
//				/* this function works on end result */
//				protectedQuaternionBits(td.protectflag, td.ext.quat, td.ext.iquat);
//			}
//			else {
//				float eulmat[3][3];
//
//				Mat3MulMat3(totmat, mat, td.mtx);
//				Mat3MulMat3(smat, td.smtx, totmat);
//
//				/* calculate the total rotatation in eulers */
//				VECCOPY(eul, td.ext.irot);
//				EulToMat3(eul, eulmat);
//
//				/* mat = transform, obmat = bone rotation */
//				Mat3MulMat3(fmat, smat, eulmat);
//
//				Mat3ToCompatibleEul(fmat, eul, td.ext.rot);
//
//				/* and apply (to end result only) */
//				protectedRotateBits(td.protectflag, eul, td.ext.irot);
//				VECCOPY(td.ext.rot, eul);
//			}
//
//			constraintRotLim(t, td);
//		}
//	}
	else {
		if ((td.flag & TD_NO_LOC) == 0)
		{
			/* translation */
			Arithb.VecSubf(vec, td.center, center);
			Arithb.Mat3MulVecfl(mat, vec);
			Arithb.VecAddf(vec, vec, center);
			/* vec now is the location where the object has to be */
			Arithb.VecSubf(vec, vec, td.center);
			Arithb.Mat3MulVecfl(td.smtx, vec);

			protectedTransBits(td.protectflag, vec);

//			if(td.tdi!=null) {
//				TransDataIpokey *tdi= td.tdi;
//				add_tdi_poin(tdi.locx, tdi.oldloc, vec[0]);
//				add_tdi_poin(tdi.locy, tdi.oldloc+1, vec[1]);
//				add_tdi_poin(tdi.locz, tdi.oldloc+2, vec[2]);
//			}
//			else
                            Arithb.VecAddf(td.loc, td.iloc, vec);
		}


		constraintTransLim(t, td);

		/* rotation */
		if ((t.flag & T_V3D_ALIGN)==0) { // align mode doesn't rotate objects itself
			/* euler or quaternion? */
 	  	    if ((td.flag & TD_USEQUAT)!=0) {
//				Mat3MulSerie(fmat, td.mtx, mat, td.smtx, 0, 0, 0, 0, 0);
//				Mat3ToQuat(fmat, quat);	// Actual transform
//
//				QuatMul(td.ext.quat, quat, td.ext.iquat);
//				/* this function works on end result */
//				protectedQuaternionBits(td.protectflag, td.ext.quat, td.ext.iquat);
			}
			else {
				float[][] obmat = new float[3][3];

				/* are there ipo keys? */
//				if(td.tdi) {
//					TransDataIpokey *tdi= td.tdi;
//					float current_rot[3];
//					float rot[3];
//
//					/* current IPO value for compatible euler */
//					current_rot[0] = (tdi.rotx) ? tdi.rotx[0] : 0.0f;
//					current_rot[1] = (tdi.roty) ? tdi.roty[0] : 0.0f;
//					current_rot[2] = (tdi.rotz) ? tdi.rotz[0] : 0.0f;
//					VecMulf(current_rot, (float)(M_PI_2 / 9.0));
//
//					/* calculate the total rotatation in eulers */
//					VecAddf(eul, td.ext.irot, td.ext.drot);
//					EulToMat3(eul, obmat);
//					/* mat = transform, obmat = object rotation */
//					Mat3MulMat3(fmat, mat, obmat);
//
//					Mat3ToCompatibleEul(fmat, eul, current_rot);
//
//					/* correct back for delta rot */
//					if(tdi.flag & TOB_IPODROT) {
//						VecSubf(rot, eul, td.ext.irot);
//					}
//					else {
//						VecSubf(rot, eul, td.ext.drot);
//					}
//
//					VecMulf(rot, (float)(9.0/M_PI_2));
//					VecSubf(rot, rot, tdi.oldrot);
//
//					protectedRotateBits(td.protectflag, rot, tdi.oldrot);
//
//					add_tdi_poin(tdi.rotx, tdi.oldrot, rot[0]);
//					add_tdi_poin(tdi.roty, tdi.oldrot+1, rot[1]);
//					add_tdi_poin(tdi.rotz, tdi.oldrot+2, rot[2]);
//				}
//				else {
					Arithb.Mat3MulMat3(totmat, mat, td.mtx);
					Arithb.Mat3MulMat3(smat, td.smtx, totmat);

					/* calculate the total rotatation in eulers */
					Arithb.VecAddf(eul, td.ext.irot, td.ext.drot); /* we have to correct for delta rot */
					Arithb.EulToMat3(eul, obmat);
					/* mat = transform, obmat = object rotation */
					Arithb.Mat3MulMat3(fmat, smat, obmat);

					Arithb.Mat3ToCompatibleEul(fmat, eul, td.ext.rot);

					/* correct back for delta rot */
					Arithb.VecSubf(eul, eul, td.ext.drot);

					/* and apply */
					protectedRotateBits(td.protectflag, eul, td.ext.irot);
					UtilDefines.VECCOPY(td.ext.rot, eul);
//				}
			}

			constraintRotLim(t, td);
		}
	}
}

static void applyRotation(TransInfo t, float angle, float[] axis)
{
	TransData[] tds = t.data;
        int td_p = 0;
	float[][] mat = new float[3][3];
	int i;

	Arithb.VecRotToMat3(axis, angle, mat);

	for(i = 0 ; i < t.total; i++, td_p++) {
                TransData td = tds[td_p];

		if ((td.flag & TD_NOACTION)!=0)
			break;

		if ((td.flag & TD_SKIP)!=0)
			continue;

		if (t.con.applyRot!=null) {
			t.con.applyRot.run(t, td, axis, null);
			Arithb.VecRotToMat3(axis, angle * td.factor, mat);
		}
		else if ((t.flag & T_PROP_EDIT)!=0) {
			Arithb.VecRotToMat3(axis, angle * td.factor, mat);
		}

		ElementRotation(t, td, mat, t.around);
	}
}

public static TransformFunc Rotation = new TransformFunc() {
public int run(TransInfo t, short[] mval)
//int Rotation(TransInfo *t, short mval[2])
{
//	char str[64];

	float[] finalfac = {0};

	float[] axis = new float[3];
	float[][] mat = new float[3][3];

	UtilDefines.VECCOPY(axis, t.viewinv[2]);
	Arithb.VecMulf(axis, -1.0f);
	Arithb.Normalize(axis);

	finalfac[0] = t.values[0];

//	applyNDofInput(&t.ndof, &final);

	TransformSnap.snapGrid(t, finalfac);

	if (t.con.applyRot!=null) {
		t.con.applyRot.run(t, null, axis, finalfac);
	}

	TransformSnap.applySnapping(t, finalfac);

//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		applyNumInput(&t.num, &final);
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Rot: %s %s %s", &c[0], t.con.text, t.proptext);
//
//		/* Clamp between -180 and 180 */
//		while (final >= 180.0)
//			final -= 360.0;
//
//		while (final <= -180.0)
//			final += 360.0;
//
//		final *= (float)(M_PI / 180.0);
//	}
//	else {
//		sprintf(str, "Rot: %.2f%s %s", 180.0*final/M_PI, t.con.text, t.proptext);
//	}

	Arithb.VecRotToMat3(axis, finalfac[0], mat);

	// TRANSFORM_FIX_ME
//	t.values[0] = final;		// used in manipulator
//	Mat3CpyMat3(t.mat, mat);	// used in manipulator

	applyRotation(t, finalfac[0], axis);

	TransformGenerics.recalcData(t);

//	ED_area_headerprint(t.sa, str);

	return 1;
}};


/* ************************** TRACKBALL *************************** */

public static void initTrackball(TransInfo t)
{
	t.mode = TFM_TRACKBALL;
	t.transform = Trackball;

	TransformInput.initMouseInputMode(t, t.mouse, MouseInputMode.INPUT_TRACKBALL);

	t.ndof.axis = 40;
	/* Scale down input for rotation */
	t.ndof.factor[0] = 0.2f;
	t.ndof.factor[1] = 0.2f;

	t.idx_max = 1;
	t.num.idx_max = 1;
	t.snap[0] = 0.0f;
	t.snap[1] = (float)((5.0/180)*Arithb.M_PI);
	t.snap[2] = t.snap[1] * 0.2f;

	t.flag |= T_NO_CONSTRAINT;
}

static void applyTrackball(TransInfo t, float[] axis1, float[] axis2, float[] angles)
{
	TransData[] tds = t.data;
        int td_p = 0;
	float[][] mat = new float[3][3], smat = new float[3][3], totmat = new float[3][3];
	int i;

	Arithb.VecRotToMat3(axis1, angles[0], smat);
	Arithb.VecRotToMat3(axis2, angles[1], totmat);

	Arithb.Mat3MulMat3(mat, smat, totmat);

	for(i = 0 ; i < t.total; i++, td_p++) {
                TransData td = tds[td_p];
		if ((td.flag & TD_NOACTION)!=0)
			break;

		if ((td.flag & TD_SKIP)!=0)
			continue;

		if ((t.flag & T_PROP_EDIT)!=0) {
			Arithb.VecRotToMat3(axis1, td.factor * angles[0], smat);
			Arithb.VecRotToMat3(axis2, td.factor * angles[1], totmat);

			Arithb.Mat3MulMat3(mat, smat, totmat);
		}

		ElementRotation(t, td, mat, t.around);
	}
}

public static TransformFunc Trackball = new TransformFunc() {
public int run(TransInfo t, short[] mval)
//int Trackball(TransInfo *t, short mval[2])
{
//	char str[128];
	float[] axis1 = new float[3], axis2 = new float[3];
	float[][] mat = new float[3][3], totmat = new float[3][3], smat = new float[3][3];
	float[] phi = new float[2];

	UtilDefines.VECCOPY(axis1, t.persinv[0]);
	UtilDefines.VECCOPY(axis2, t.persinv[1]);
	Arithb.Normalize(axis1);
	Arithb.Normalize(axis2);

	phi[0] = t.values[0];
	phi[1] = t.values[1];

//	applyNDofInput(&t.ndof, phi);

	TransformSnap.snapGrid(t, phi);

//	if (hasNumInput(&t.num)) {
//		char c[40];
//
//		applyNumInput(&t.num, phi);
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Trackball: %s %s %s", &c[0], &c[20], t.proptext);
//
//		phi[0] *= (float)(M_PI / 180.0);
//		phi[1] *= (float)(M_PI / 180.0);
//	}
//	else {
//		sprintf(str, "Trackball: %.2f %.2f %s", 180.0*phi[0]/M_PI, 180.0*phi[1]/M_PI, t.proptext);
//	}

	Arithb.VecRotToMat3(axis1, phi[0], smat);
	Arithb.VecRotToMat3(axis2, phi[1], totmat);

	Arithb.Mat3MulMat3(mat, smat, totmat);

	// TRANSFORM_FIX_ME
	//Mat3CpyMat3(t.mat, mat);	// used in manipulator

	applyTrackball(t, axis1, axis2, phi);

	TransformGenerics.recalcData(t);

//	ED_area_headerprint(t.sa, str);

	return 1;
}};

/* ************************** TRANSLATION *************************** */

public static void initTranslation(TransInfo t)
{
	t.mode = TFM_TRANSLATION;
	t.transform = Translation;

	TransformInput.initMouseInputMode(t, t.mouse, MouseInputMode.INPUT_VECTOR);

	t.idx_max = (short)((t.flag & T_2D_EDIT)!=0? 1: 2);
	t.num.flag = 0;
	t.num.idx_max = t.idx_max;

	t.ndof.axis = (t.flag & T_2D_EDIT)!=0? 1|2: 1|2|4;

	if(t.spacetype == SpaceTypes.SPACE_VIEW3D) {
		//View3D v3d = (View3D)t.view;
		RegionView3D rv3d = (RegionView3D)t.ar.regiondata;

		t.snap[0] = 0.0f;
		//t.snap[1] = v3d.gridview * 1.0f;
		t.snap[1] = rv3d.gridview * 1.0f;
		t.snap[2] = t.snap[1] * 0.1f;
	}
	else if(t.spacetype == SpaceTypes.SPACE_IMAGE) {
		t.snap[0] = 0.0f;
		t.snap[1] = 0.125f;
		t.snap[2] = 0.0625f;
	}
	else {
		t.snap[0] = 0.0f;
		t.snap[1] = t.snap[2] = 1.0f;
	}
}

//static void headerTranslation(TransInfo *t, float vec[3], char *str) {
//	char tvec[60];
//	char distvec[20];
//	char autoik[20];
//	float dist;
//
//	if (hasNumInput(&t.num)) {
//		outputNumInput(&(t.num), tvec);
//		dist = VecLength(t.num.val);
//	}
//	else {
//		float dvec[3];
//
//		VECCOPY(dvec, vec);
//		applyAspectRatio(t, dvec);
//
//		dist = VecLength(vec);
//		sprintf(&tvec[0], "%.4f", dvec[0]);
//		sprintf(&tvec[20], "%.4f", dvec[1]);
//		sprintf(&tvec[40], "%.4f", dvec[2]);
//	}
//
//	if( dist > 1e10 || dist < -1e10 )	/* prevent string buffer overflow */
//		sprintf(distvec, "%.4e", dist);
//	else
//		sprintf(distvec, "%.4f", dist);
//
//	if(t.flag & T_AUTOIK) {
//		short chainlen= t.settings.autoik_chainlen;
//
//		if(chainlen)
//			sprintf(autoik, "AutoIK-Len: %d", chainlen);
//		else
//			strcpy(autoik, "");
//	}
//	else
//		strcpy(autoik, "");
//
//	if (t.con.mode & CON_APPLY) {
//		switch(t.num.idx_max) {
//		case 0:
//			sprintf(str, "D: %s (%s)%s %s  %s", &tvec[0], distvec, t.con.text, t.proptext, &autoik[0]);
//			break;
//		case 1:
//			sprintf(str, "D: %s   D: %s (%s)%s %s  %s", &tvec[0], &tvec[20], distvec, t.con.text, t.proptext, &autoik[0]);
//			break;
//		case 2:
//			sprintf(str, "D: %s   D: %s  D: %s (%s)%s %s  %s", &tvec[0], &tvec[20], &tvec[40], distvec, t.con.text, t.proptext, &autoik[0]);
//		}
//	}
//	else {
//		if(t.flag & T_2D_EDIT)
//			sprintf(str, "Dx: %s   Dy: %s (%s)%s %s", &tvec[0], &tvec[20], distvec, t.con.text, t.proptext);
//		else
//			sprintf(str, "Dx: %s   Dy: %s  Dz: %s (%s)%s %s  %s", &tvec[0], &tvec[20], &tvec[40], distvec, t.con.text, t.proptext, &autoik[0]);
//	}
//}

static void applyTranslation(TransInfo t, float[] vec) {
	TransData[] tds = t.data;
        int td_p = 0;
	float[] tvec = new float[3];
	int i;

	for(i = 0 ; i < t.total; i++, td_p++) {
                TransData td = tds[td_p];
		if ((td.flag & TD_NOACTION)!=0)
			break;

		if ((td.flag & TD_SKIP)!=0)
			continue;

		/* handle snapping rotation before doing the translation */
		if (TransformSnap.usingSnappingNormal(t))
		{
			if (TransformSnap.validSnappingNormal(t))
			{
				float[] original_normal = td.axismtx[2];
				float[] axis = new float[3];
				float[] quat = new float[4];
				float[][] mat = new float[3][3];
				float angle;

				Arithb.Crossf(axis, original_normal, t.tsnap.snapNormal);
				angle = Arithb.saacos(Arithb.Inpf(original_normal, t.tsnap.snapNormal));

				Arithb.AxisAngleToQuat(quat, axis, angle);

				Arithb.QuatToMat3(quat, mat);

				ElementRotation(t, td, mat, View3dTypes.V3D_LOCAL);
			}
			else
			{
				float[][] mat = new float[3][3];

				Arithb.Mat3One(mat);

				ElementRotation(t, td, mat, View3dTypes.V3D_LOCAL);
			}
		}

		if (t.con.applyVec!=null) {
			float[] pvec = new float[3];
			t.con.applyVec.run(t, td, vec, tvec, pvec);
		}
		else {
			UtilDefines.VECCOPY(tvec, vec);
		}

		Arithb.Mat3MulVecfl(td.smtx, tvec);
		Arithb.VecMulf(tvec, td.factor);

		protectedTransBits(td.protectflag, tvec);

		/* transdata ipokey */
//		if(td.tdi) {
//			TransDataIpokey *tdi= td.tdi;
//			add_tdi_poin(tdi.locx, tdi.oldloc, tvec[0]);
//			add_tdi_poin(tdi.locy, tdi.oldloc+1, tvec[1]);
//			add_tdi_poin(tdi.locz, tdi.oldloc+2, tvec[2]);
//		}
//		else
                    Arithb.VecAddf(td.loc, td.iloc, tvec);
//                    System.out.println("applyTranslation td.loc="+Arrays.toString(td.loc));

		constraintTransLim(t, td);
	}
}

/* uses t.vec to store actual translation in */
public static TransformFunc Translation = new TransformFunc() {
public int run(TransInfo t, short[] mval)
//int Translation(TransInfo *t, short mval[2])
{
//        System.out.println("Translation: "+Arrays.toString(t.values));
	float[] tvec = new float[3];
//	char str[250];

	if ((t.con.mode & CON_APPLY)!=0) {
		float[] pvec = {0.0f, 0.0f, 0.0f};
//		TransformSnap.applySnapping(t, t.values);
		t.con.applyVec.run(t, null, t.values, tvec, pvec);
		UtilDefines.VECCOPY(t.values, tvec);
//		headerTranslation(t, pvec, str);
	}
	else {
//		applyNDofInput(&t.ndof, t.values);
//		TransformSnap.snapGrid(t, t.values);
//		applyNumInput(&t.num, t.values);
//		if (hasNumInput(&t.num))
//		{
//			removeAspectRatio(t, t.values);
//		}

//		TransformSnap.applySnapping(t, t.values);
//		headerTranslation(t, t.values, str);
	}

	applyTranslation(t, t.values);

	/* evil hack - redo translation if clipping needed */
//	if (t.flag & T_CLIP_UV && clipUVTransform(t, t.values, 0))
//		applyTranslation(t, t.values);

	TransformGenerics.recalcData(t);

//	ED_area_headerprint(t.sa, str);

	return 1;
}};

///* ************************** SHRINK/FATTEN *************************** */
//
//void initShrinkFatten(TransInfo *t)
//{
//	// If not in mesh edit mode, fallback to Resize
//	if (t.obedit==NULL || t.obedit.type != OB_MESH) {
//		initResize(t);
//	}
//	else {
//		t.mode = TFM_SHRINKFATTEN;
//		t.transform = ShrinkFatten;
//
//		initMouseInputMode(t, &t.mouse, INPUT_VERTICAL_ABSOLUTE);
//
//		t.idx_max = 0;
//		t.num.idx_max = 0;
//		t.snap[0] = 0.0f;
//		t.snap[1] = 1.0f;
//		t.snap[2] = t.snap[1] * 0.1f;
//
//		t.flag |= T_NO_CONSTRAINT;
//	}
//}
//
//
//
//int ShrinkFatten(TransInfo *t, short mval[2])
//{
//	float vec[3];
//	float distance;
//	int i;
//	char str[64];
//	TransData *td = t.data;
//
//	distance = -t.values[0];
//
//	snapGrid(t, &distance);
//
//	applyNumInput(&t.num, &distance);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Shrink/Fatten: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "Shrink/Fatten: %.4f %s", distance, t.proptext);
//	}
//
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		VECCOPY(vec, td.axismtx[2]);
//		VecMulf(vec, distance);
//		VecMulf(vec, td.factor);
//
//		VecAddf(td.loc, td.iloc, vec);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** TILT *************************** */
//
//void initTilt(TransInfo *t)
//{
//	t.mode = TFM_TILT;
//	t.transform = Tilt;
//
//	initMouseInputMode(t, &t.mouse, INPUT_ANGLE);
//
//	t.ndof.axis = 16;
//	/* Scale down and flip input for rotation */
//	t.ndof.factor[0] = -0.2f;
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = (float)((5.0/180)*M_PI);
//	t.snap[2] = t.snap[1] * 0.2f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//
//
//int Tilt(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	int i;
//	char str[50];
//
//	float final;
//
//	final = t.values[0];
//
//	applyNDofInput(&t.ndof, &final);
//
//	snapGrid(t, &final);
//
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		applyNumInput(&t.num, &final);
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Tilt: %s %s", &c[0], t.proptext);
//
//		final *= (float)(M_PI / 180.0);
//	}
//	else {
//		sprintf(str, "Tilt: %.2f %s", 180.0*final/M_PI, t.proptext);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if (td.val) {
//			*td.val = td.ival + final * td.factor;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
//
///* ******************** Curve Shrink/Fatten *************** */
//
//void initCurveShrinkFatten(TransInfo *t)
//{
//	t.mode = TFM_CURVE_SHRINKFATTEN;
//	t.transform = CurveShrinkFatten;
//
//	initMouseInputMode(t, &t.mouse, INPUT_SPRING);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int CurveShrinkFatten(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float ratio;
//	int i;
//	char str[50];
//
//	ratio = t.values[0];
//
//	snapGrid(t, &ratio);
//
//	applyNumInput(&t.num, &ratio);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//		sprintf(str, "Shrink/Fatten: %s", c);
//	}
//	else {
//		sprintf(str, "Shrink/Fatten: %3f", ratio);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if(td.val) {
//			//*td.val= ratio;
//			*td.val= td.ival*ratio;
//			if (*td.val <= 0.0f) *td.val = 0.0001f;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** PUSH/PULL *************************** */
//
//void initPushPull(TransInfo *t)
//{
//	t.mode = TFM_PUSHPULL;
//	t.transform = PushPull;
//
//	initMouseInputMode(t, &t.mouse, INPUT_VERTICAL_ABSOLUTE);
//
//	t.ndof.axis = 4;
//	/* Flip direction */
//	t.ndof.factor[0] = -1.0f;
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 1.0f;
//	t.snap[2] = t.snap[1] * 0.1f;
//}
//
//
//int PushPull(TransInfo *t, short mval[2])
//{
//	float vec[3], axis[3];
//	float distance;
//	int i;
//	char str[128];
//	TransData *td = t.data;
//
//	distance = t.values[0];
//
//	applyNDofInput(&t.ndof, &distance);
//
//	snapGrid(t, &distance);
//
//	applyNumInput(&t.num, &distance);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Push/Pull: %s%s %s", c, t.con.text, t.proptext);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "Push/Pull: %.4f%s %s", distance, t.con.text, t.proptext);
//	}
//
//	if (t.con.applyRot && t.con.mode & CON_APPLY) {
//		t.con.applyRot(t, NULL, axis, NULL);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		VecSubf(vec, t.center, td.center);
//		if (t.con.applyRot && t.con.mode & CON_APPLY) {
//			t.con.applyRot(t, td, axis, NULL);
//			if (isLockConstraint(t)) {
//				float dvec[3];
//				Projf(dvec, vec, axis);
//				VecSubf(vec, vec, dvec);
//			}
//			else {
//				Projf(vec, vec, axis);
//			}
//		}
//		Normalize(vec);
//		VecMulf(vec, distance);
//		VecMulf(vec, td.factor);
//
//		VecAddf(td.loc, td.iloc, vec);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** BEVEL **************************** */
//
//void initBevel(TransInfo *t)
//{
//	t.transform = Bevel;
//	t.handleEvent = handleEventBevel;
//
//	initMouseInputMode(t, &t.mouse, INPUT_HORIZONTAL_ABSOLUTE);
//
//	t.mode = TFM_BEVEL;
//	t.flag |= T_NO_CONSTRAINT;
//	t.num.flag |= NUM_NO_NEGATIVE;
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	/* DON'T KNOW WHY THIS IS NEEDED */
//	if (G.editBMesh.imval[0] == 0 && G.editBMesh.imval[1] == 0) {
//		/* save the initial mouse co */
//		G.editBMesh.imval[0] = t.imval[0];
//		G.editBMesh.imval[1] = t.imval[1];
//	}
//	else {
//		/* restore the mouse co from a previous call to initTransform() */
//		t.imval[0] = G.editBMesh.imval[0];
//		t.imval[1] = G.editBMesh.imval[1];
//	}
//}
//
//int handleEventBevel(TransInfo *t, wmEvent *event)
//{
//	if (event.val) {
//		if(!G.editBMesh) return 0;
//
//		switch (event.type) {
//		case MIDDLEMOUSE:
//			G.editBMesh.options ^= BME_BEVEL_VERT;
//			t.state = TRANS_CANCEL;
//			return 1;
//		//case PADPLUSKEY:
//		//	G.editBMesh.options ^= BME_BEVEL_RES;
//		//	G.editBMesh.res += 1;
//		//	if (G.editBMesh.res > 4) {
//		//		G.editBMesh.res = 4;
//		//	}
//		//	t.state = TRANS_CANCEL;
//		//	return 1;
//		//case PADMINUS:
//		//	G.editBMesh.options ^= BME_BEVEL_RES;
//		//	G.editBMesh.res -= 1;
//		//	if (G.editBMesh.res < 0) {
//		//		G.editBMesh.res = 0;
//		//	}
//		//	t.state = TRANS_CANCEL;
//		//	return 1;
//		default:
//			return 0;
//		}
//	}
//	return 0;
//}
//
//int Bevel(TransInfo *t, short mval[2])
//{
//	float distance,d;
//	int i;
//	char str[128];
//	char *mode;
//	TransData *td = t.data;
//
//	mode = (G.editBMesh.options & BME_BEVEL_VERT) ? "verts only" : "normal";
//	distance = t.values[0] / 4; /* 4 just seemed a nice value to me, nothing special */
//
//	distance = fabs(distance);
//
//	snapGrid(t, &distance);
//
//	applyNumInput(&t.num, &distance);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Bevel - Dist: %s, Mode: %s (MMB to toggle))", c, mode);
//	}
//	else {
//		/* default header print */
//		sprintf(str, "Bevel - Dist: %.4f, Mode: %s (MMB to toggle))", distance, mode);
//	}
//
//	if (distance < 0) distance = -distance;
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.axismtx[1][0] > 0 && distance > td.axismtx[1][0]) {
//			d = td.axismtx[1][0];
//		}
//		else {
//			d = distance;
//		}
//		VECADDFAC(td.loc,td.center,td.axismtx[0],(*td.val)*d);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** BEVEL WEIGHT *************************** */
//
//void initBevelWeight(TransInfo *t)
//{
//	t.mode = TFM_BWEIGHT;
//	t.transform = BevelWeight;
//
//	initMouseInputMode(t, &t.mouse, INPUT_SPRING);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int BevelWeight(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float weight;
//	int i;
//	char str[50];
//
//	weight = t.values[0];
//
//	weight -= 1.0f;
//	if (weight > 1.0f) weight = 1.0f;
//
//	snapGrid(t, &weight);
//
//	applyNumInput(&t.num, &weight);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		if (weight >= 0.0f)
//			sprintf(str, "Bevel Weight: +%s %s", c, t.proptext);
//		else
//			sprintf(str, "Bevel Weight: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		if (weight >= 0.0f)
//			sprintf(str, "Bevel Weight: +%.3f %s", weight, t.proptext);
//		else
//			sprintf(str, "Bevel Weight: %.3f %s", weight, t.proptext);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.val) {
//			*td.val = td.ival + weight * td.factor;
//			if (*td.val < 0.0f) *td.val = 0.0f;
//			if (*td.val > 1.0f) *td.val = 1.0f;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** CREASE *************************** */
//
//void initCrease(TransInfo *t)
//{
//	t.mode = TFM_CREASE;
//	t.transform = Crease;
//
//	initMouseInputMode(t, &t.mouse, INPUT_SPRING);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int Crease(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float crease;
//	int i;
//	char str[50];
//
//	crease = t.values[0];
//
//	crease -= 1.0f;
//	if (crease > 1.0f) crease = 1.0f;
//
//	snapGrid(t, &crease);
//
//	applyNumInput(&t.num, &crease);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		if (crease >= 0.0f)
//			sprintf(str, "Crease: +%s %s", c, t.proptext);
//		else
//			sprintf(str, "Crease: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		if (crease >= 0.0f)
//			sprintf(str, "Crease: +%.3f %s", crease, t.proptext);
//		else
//			sprintf(str, "Crease: %.3f %s", crease, t.proptext);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if (td.val) {
//			*td.val = td.ival + crease * td.factor;
//			if (*td.val < 0.0f) *td.val = 0.0f;
//			if (*td.val > 1.0f) *td.val = 1.0f;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ******************** EditBone (B-bone) width scaling *************** */
//
//void initBoneSize(TransInfo *t)
//{
//	t.mode = TFM_BONESIZE;
//	t.transform = BoneSize;
//
//	initMouseInputMode(t, &t.mouse, INPUT_SPRING_FLIP);
//
//	t.idx_max = 2;
//	t.num.idx_max = 2;
//	t.num.flag |= NUM_NULL_ONE;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//}
//
//static void headerBoneSize(TransInfo *t, float vec[3], char *str) {
//	char tvec[60];
//	if (hasNumInput(&t.num)) {
//		outputNumInput(&(t.num), tvec);
//	}
//	else {
//		sprintf(&tvec[0], "%.4f", vec[0]);
//		sprintf(&tvec[20], "%.4f", vec[1]);
//		sprintf(&tvec[40], "%.4f", vec[2]);
//	}
//
//	/* hmm... perhaps the y-axis values don't need to be shown? */
//	if (t.con.mode & CON_APPLY) {
//		if (t.num.idx_max == 0)
//			sprintf(str, "ScaleB: %s%s %s", &tvec[0], t.con.text, t.proptext);
//		else
//			sprintf(str, "ScaleB: %s : %s : %s%s %s", &tvec[0], &tvec[20], &tvec[40], t.con.text, t.proptext);
//	}
//	else {
//		sprintf(str, "ScaleB X: %s  Y: %s  Z: %s%s %s", &tvec[0], &tvec[20], &tvec[40], t.con.text, t.proptext);
//	}
//}
//
//static void ElementBoneSize(TransInfo *t, TransData *td, float mat[3][3])
//{
//	float tmat[3][3], smat[3][3], oldy;
//	float sizemat[3][3];
//
//	Mat3MulMat3(smat, mat, td.mtx);
//	Mat3MulMat3(tmat, td.smtx, smat);
//
//	if (t.con.applySize) {
//		t.con.applySize(t, td, tmat);
//	}
//
//	/* we've tucked the scale in loc */
//	oldy= td.iloc[1];
//	SizeToMat3(td.iloc, sizemat);
//	Mat3MulMat3(tmat, tmat, sizemat);
//	Mat3ToSize(tmat, td.loc);
//	td.loc[1]= oldy;
//}
//
//int BoneSize(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float size[3], mat[3][3];
//	float ratio;
//	int i;
//	char str[60];
//
//	// TRANSFORM_FIX_ME MOVE TO MOUSE INPUT
//	/* for manipulator, center handle, the scaling can't be done relative to center */
//	if( (t.flag & T_USES_MANIPULATOR) && t.con.mode==0)
//	{
//		ratio = 1.0f - ((t.imval[0] - mval[0]) + (t.imval[1] - mval[1]))/100.0f;
//	}
//	else
//	{
//		ratio = t.values[0];
//	}
//
//	size[0] = size[1] = size[2] = ratio;
//
//	snapGrid(t, size);
//
//	if (hasNumInput(&t.num)) {
//		applyNumInput(&t.num, size);
//		constraintNumInput(t, size);
//	}
//
//	SizeToMat3(size, mat);
//
//	if (t.con.applySize) {
//		t.con.applySize(t, NULL, mat);
//	}
//
//	Mat3CpyMat3(t.mat, mat);	// used in manipulator
//
//	headerBoneSize(t, size, str);
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		ElementBoneSize(t, td, mat);
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
//
///* ******************** EditBone envelope *************** */
//
//void initBoneEnvelope(TransInfo *t)
//{
//	t.mode = TFM_BONE_ENVELOPE;
//	t.transform = BoneEnvelope;
//
//	initMouseInputMode(t, &t.mouse, INPUT_SPRING);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 0.1f;
//	t.snap[2] = t.snap[1] * 0.1f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int BoneEnvelope(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float ratio;
//	int i;
//	char str[50];
//
//	ratio = t.values[0];
//
//	snapGrid(t, &ratio);
//
//	applyNumInput(&t.num, &ratio);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//		sprintf(str, "Envelope: %s", c);
//	}
//	else {
//		sprintf(str, "Envelope: %3f", ratio);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if (td.val) {
//			/* if the old/original value was 0.0f, then just use ratio */
//			if (td.ival)
//				*td.val= td.ival*ratio;
//			else
//				*td.val= ratio;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
//
///* ******************** EditBone roll *************** */
//
//void initBoneRoll(TransInfo *t)
//{
//	t.mode = TFM_BONE_ROLL;
//	t.transform = BoneRoll;
//
//	initMouseInputMode(t, &t.mouse, INPUT_ANGLE);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = (float)((5.0/180)*M_PI);
//	t.snap[2] = t.snap[1] * 0.2f;
//
//	t.flag |= T_NO_CONSTRAINT;
//}
//
//int BoneRoll(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	int i;
//	char str[50];
//
//	float final;
//
//	final = t.values[0];
//
//	snapGrid(t, &final);
//
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		applyNumInput(&t.num, &final);
//
//		outputNumInput(&(t.num), c);
//
//		sprintf(str, "Roll: %s", &c[0]);
//
//		final *= (float)(M_PI / 180.0);
//	}
//	else {
//		sprintf(str, "Roll: %.2f", 180.0*final/M_PI);
//	}
//
//	/* set roll values */
//	for (i = 0; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		*(td.val) = td.ival - final;
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** BAKE TIME ******************* */
//
//void initBakeTime(TransInfo *t)
//{
//	t.transform = BakeTime;
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//
//	t.idx_max = 0;
//	t.num.idx_max = 0;
//	t.snap[0] = 0.0f;
//	t.snap[1] = 1.0f;
//	t.snap[2] = t.snap[1] * 0.1f;
//}
//
//int BakeTime(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float time;
//	int i;
//	char str[50];
//
//	float fac = 0.1f;
//
//	if(t.mouse.precision) {
//		/* calculate ratio for shiftkey pos, and for total, and blend these for precision */
//		time= (float)(t.center2d[0] - t.mouse.precision_mval[0]) * fac;
//		time+= 0.1f*((float)(t.center2d[0]*fac - mval[0]) -time);
//	}
//	else {
//		time = (float)(t.center2d[0] - mval[0])*fac;
//	}
//
//	snapGrid(t, &time);
//
//	applyNumInput(&t.num, &time);
//
//	/* header print for NumInput */
//	if (hasNumInput(&t.num)) {
//		char c[20];
//
//		outputNumInput(&(t.num), c);
//
//		if (time >= 0.0f)
//			sprintf(str, "Time: +%s %s", c, t.proptext);
//		else
//			sprintf(str, "Time: %s %s", c, t.proptext);
//	}
//	else {
//		/* default header print */
//		if (time >= 0.0f)
//			sprintf(str, "Time: +%.3f %s", time, t.proptext);
//		else
//			sprintf(str, "Time: %.3f %s", time, t.proptext);
//	}
//
//	for(i = 0 ; i < t.total; i++, td++) {
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		if (td.val) {
//			*td.val = td.ival + time * td.factor;
//			if (td.ext.size && *td.val < *td.ext.size) *td.val = *td.ext.size;
//			if (td.ext.quat && *td.val > *td.ext.quat) *td.val = *td.ext.quat;
//		}
//	}
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************** MIRROR *************************** */
//
//void initMirror(TransInfo *t)
//{
//	t.transform = Mirror;
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//
//	t.flag |= T_NULL_ONE;
//	if (!t.obedit) {
//		t.flag |= T_NO_ZERO;
//	}
//}
//
//int Mirror(TransInfo *t, short mval[2])
//{
//	TransData *td;
//	float size[3], mat[3][3];
//	int i;
//	char str[200];
//
//	/*
//	 * OPTIMISATION:
//	 * This still recalcs transformation on mouse move
//	 * while it should only recalc on constraint change
//	 * */
//
//	/* if an axis has been selected */
//	if (t.con.mode & CON_APPLY) {
//		size[0] = size[1] = size[2] = -1;
//
//		SizeToMat3(size, mat);
//
//		if (t.con.applySize) {
//			t.con.applySize(t, NULL, mat);
//		}
//
//		sprintf(str, "Mirror%s", t.con.text);
//
//		for(i = 0, td=t.data; i < t.total; i++, td++) {
//			if (td.flag & TD_NOACTION)
//				break;
//
//			if (td.flag & TD_SKIP)
//				continue;
//
//			ElementResize(t, td, mat);
//		}
//
//		recalcData(t);
//
//		ED_area_headerprint(t.sa, str);
//	}
//	else
//	{
//		size[0] = size[1] = size[2] = 1;
//
//		SizeToMat3(size, mat);
//
//		for(i = 0, td=t.data; i < t.total; i++, td++) {
//			if (td.flag & TD_NOACTION)
//				break;
//
//			if (td.flag & TD_SKIP)
//				continue;
//
//			ElementResize(t, td, mat);
//		}
//
//		recalcData(t);
//
//		ED_area_headerprint(t.sa, "Select a mirror axis (X, Y, Z)");
//	}
//
//	return 1;
//}
//
///* ************************** ALIGN *************************** */
//
//void initAlign(TransInfo *t)
//{
//	t.flag |= T_NO_CONSTRAINT;
//
//	t.transform = Align;
//
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//}
//
//int Align(TransInfo *t, short mval[2])
//{
//	TransData *td = t.data;
//	float center[3];
//	int i;
//
//	/* saving original center */
//	VECCOPY(center, t.center);
//
//	for(i = 0 ; i < t.total; i++, td++)
//	{
//		float mat[3][3], invmat[3][3];
//
//		if (td.flag & TD_NOACTION)
//			break;
//
//		if (td.flag & TD_SKIP)
//			continue;
//
//		/* around local centers */
//		if (t.flag & (T_OBJECT|T_POSE)) {
//			VECCOPY(t.center, td.center);
//		}
//		else {
//			if(t.settings.selectmode & SCE_SELECT_FACE) {
//				VECCOPY(t.center, td.center);
//			}
//		}
//
//		Mat3Inv(invmat, td.axismtx);
//
//		Mat3MulMat3(mat, t.spacemtx, invmat);
//
//		ElementRotation(t, td, mat, t.around);
//	}
//
//	/* restoring original center */
//	VECCOPY(t.center, center);
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, "Align");
//
//	return 1;
//}
//
///* ************************** ANIM EDITORS - TRANSFORM TOOLS *************************** */
//
///* ---------------- Special Helpers for Various Settings ------------- */
//
//
///* This function returns the snapping 'mode' for Animation Editors only
// * We cannot use the standard snapping due to NLA-strip scaling complexities.
// */
//// XXX these modifier checks should be keymappable
//static short getAnimEdit_SnapMode(TransInfo *t)
//{
//	short autosnap= SACTSNAP_OFF;
//
//	/* currently, some of these are only for the action editor */
//	if (t.spacetype == SPACE_ACTION) {
//		SpaceAction *saction= (SpaceAction *)t.sa.spacedata.first;
//
//		if (saction)
//			autosnap= saction.autosnap;
//	}
//	else if (t.spacetype == SPACE_IPO) {
//		SpaceIpo *sipo= (SpaceIpo *)t.sa.spacedata.first;
//
//		if (sipo)
//			autosnap= sipo.autosnap;
//	}
//	else if (t.spacetype == SPACE_NLA) {
//		SpaceNla *snla= (SpaceNla *)t.sa.spacedata.first;
//
//		if (snla)
//			autosnap= snla.autosnap;
//	}
//	else {
//		// TRANSFORM_FIX_ME This needs to use proper defines for t.modifiers
////		// FIXME: this still toggles the modes...
////		if (ctrl)
////			autosnap= SACTSNAP_STEP;
////		else if (shift)
////			autosnap= SACTSNAP_FRAME;
////		else if (alt)
////			autosnap= SACTSNAP_MARKER;
////		else
//			autosnap= SACTSNAP_OFF;
//	}
//
//	return autosnap;
//}
//
///* This function is used for testing if an Animation Editor is displaying
// * its data in frames or seconds (and the data needing to be edited as such).
// * Returns 1 if in seconds, 0 if in frames
// */
//static short getAnimEdit_DrawTime(TransInfo *t)
//{
//	short drawtime;
//
//	/* currently, some of these are only for the action editor */
//	if (t.spacetype == SPACE_ACTION) {
//		SpaceAction *saction= (SpaceAction *)t.sa.spacedata.first;
//
//		drawtime = (saction.flag & SACTION_DRAWTIME)? 1 : 0;
//	}
//	else if (t.spacetype == SPACE_NLA) {
//		SpaceNla *snla= (SpaceNla *)t.sa.spacedata.first;
//
//		drawtime = (snla.flag & SNLA_DRAWTIME)? 1 : 0;
//	}
//	else {
//		drawtime = 0;
//	}
//
//	return drawtime;
//}
//
//
///* This function is used by Animation Editor specific transform functions to do
// * the Snap Keyframe to Nearest Frame/Marker
// */
//static void doAnimEdit_SnapFrame(TransInfo *t, TransData *td, AnimData *adt, short autosnap)
//{
//	/* snap key to nearest frame? */
//	if (autosnap == SACTSNAP_FRAME) {
//		const Scene *scene= t.scene;
//		const short doTime= getAnimEdit_DrawTime(t);
//		const double secf= FPS;
//		double val;
//
//		/* convert frame to nla-action time (if needed) */
//		if (adt)
//			val= BKE_nla_tweakedit_remap(adt, *(td.val), NLATIME_CONVERT_MAP);
//		else
//			val= *(td.val);
//
//		/* do the snapping to nearest frame/second */
//		if (doTime)
//			val= (float)( floor((val/secf) + 0.5f) * secf );
//		else
//			val= (float)( floor(val+0.5f) );
//
//		/* convert frame out of nla-action time */
//		if (adt)
//			*(td.val)= BKE_nla_tweakedit_remap(adt, val, NLATIME_CONVERT_UNMAP);
//		else
//			*(td.val)= val;
//	}
//	/* snap key to nearest marker? */
//	else if (autosnap == SACTSNAP_MARKER) {
//		float val;
//
//		/* convert frame to nla-action time (if needed) */
//		if (adt)
//			val= BKE_nla_tweakedit_remap(adt, *(td.val), NLATIME_CONVERT_MAP);
//		else
//			val= *(td.val);
//
//		/* snap to nearest marker */
//		// TODO: need some more careful checks for where data comes from
//		val= (float)ED_markers_find_nearest_marker_time(&t.scene.markers, val);
//
//		/* convert frame out of nla-action time */
//		if (adt)
//			*(td.val)= BKE_nla_tweakedit_remap(adt, val, NLATIME_CONVERT_UNMAP);
//		else
//			*(td.val)= val;
//	}
//}
//
///* ----------------- Translation ----------------------- */
//
//void initTimeTranslate(TransInfo *t)
//{
//	t.mode = TFM_TIME_TRANSLATE;
//	t.transform = TimeTranslate;
//
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//
//	/* num-input has max of (n-1) */
//	t.idx_max = 0;
//	t.num.flag = 0;
//	t.num.idx_max = t.idx_max;
//
//	/* initialise snap like for everything else */
//	t.snap[0] = 0.0f;
//	t.snap[1] = t.snap[2] = 1.0f;
//}
//
//static void headerTimeTranslate(TransInfo *t, char *str)
//{
//	char tvec[60];
//
//	/* if numeric input is active, use results from that, otherwise apply snapping to result */
//	if (hasNumInput(&t.num)) {
//		outputNumInput(&(t.num), tvec);
//	}
//	else {
//		const Scene *scene = t.scene;
//		const short autosnap= getAnimEdit_SnapMode(t);
//		const short doTime = getAnimEdit_DrawTime(t);
//		const double secf= FPS;
//		float val = t.values[0];
//
//		/* apply snapping + frame.seconds conversions */
//		if (autosnap == SACTSNAP_STEP) {
//			if (doTime)
//				val= floor(val/secf + 0.5f);
//			else
//				val= floor(val + 0.5f);
//		}
//		else {
//			if (doTime)
//				val= val / secf;
//		}
//
//		sprintf(&tvec[0], "%.4f", val);
//	}
//
//	sprintf(str, "DeltaX: %s", &tvec[0]);
//}
//
//static void applyTimeTranslate(TransInfo *t, float sval)
//{
//	TransData *td = t.data;
//	Scene *scene = t.scene;
//	int i;
//
//	const short doTime= getAnimEdit_DrawTime(t);
//	const double secf= FPS;
//
//	const short autosnap= getAnimEdit_SnapMode(t);
//
//	float deltax, val;
//
//	/* it doesn't matter whether we apply to t.data or t.data2d, but t.data2d is more convenient */
//	for (i = 0 ; i < t.total; i++, td++) {
//		/* it is assumed that td.extra is a pointer to the AnimData,
//		 * whose active action is where this keyframe comes from
//		 * (this is only valid when not in NLA)
//		 */
//		AnimData *adt= (t.spacetype != SPACE_NLA) ? td.extra : NULL;
//
//		/* check if any need to apply nla-mapping */
//		if (adt) {
//			deltax = t.values[0];
//
//			if (autosnap == SACTSNAP_STEP) {
//				if (doTime)
//					deltax= (float)( floor((deltax/secf) + 0.5f) * secf );
//				else
//					deltax= (float)( floor(deltax + 0.5f) );
//			}
//
//			val = BKE_nla_tweakedit_remap(adt, td.ival, NLATIME_CONVERT_MAP);
//			val += deltax;
//			*(td.val) = BKE_nla_tweakedit_remap(adt, val, NLATIME_CONVERT_UNMAP);
//		}
//		else {
//			deltax = val = t.values[0];
//
//			if (autosnap == SACTSNAP_STEP) {
//				if (doTime)
//					val= (float)( floor((deltax/secf) + 0.5f) * secf );
//				else
//					val= (float)( floor(val + 0.5f) );
//			}
//
//			*(td.val) = td.ival + val;
//		}
//
//		/* apply nearest snapping */
//		doAnimEdit_SnapFrame(t, td, adt, autosnap);
//	}
//}
//
//int TimeTranslate(TransInfo *t, short mval[2])
//{
//	View2D *v2d = (View2D *)t.view;
//	float cval[2], sval[2];
//	char str[200];
//
//	/* calculate translation amount from mouse movement - in 'time-grid space' */
//	UI_view2d_region_to_view(v2d, mval[0], mval[0], &cval[0], &cval[1]);
//	UI_view2d_region_to_view(v2d, t.imval[0], t.imval[0], &sval[0], &sval[1]);
//
//	/* we only need to calculate effect for time (applyTimeTranslate only needs that) */
//	t.values[0] = cval[0] - sval[0];
//
//	/* handle numeric-input stuff */
//	t.vec[0] = t.values[0];
//	applyNumInput(&t.num, &t.vec[0]);
//	t.values[0] = t.vec[0];
//	headerTimeTranslate(t, str);
//
//	applyTimeTranslate(t, sval[0]);
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ----------------- Time Slide ----------------------- */
//
//void initTimeSlide(TransInfo *t)
//{
//	/* this tool is only really available in the Action Editor... */
//	if (t.spacetype == SPACE_ACTION) {
//		SpaceAction *saction= (SpaceAction *)t.sa.spacedata.first;
//
//		/* set flag for drawing stuff */
//		saction.flag |= SACTION_MOVING;
//	}
//
//	t.mode = TFM_TIME_SLIDE;
//	t.transform = TimeSlide;
//	t.flag |= T_FREE_CUSTOMDATA;
//
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//
//	/* num-input has max of (n-1) */
//	t.idx_max = 0;
//	t.num.flag = 0;
//	t.num.idx_max = t.idx_max;
//
//	/* initialise snap like for everything else */
//	t.snap[0] = 0.0f;
//	t.snap[1] = t.snap[2] = 1.0f;
//}
//
//static void headerTimeSlide(TransInfo *t, float sval, char *str)
//{
//	char tvec[60];
//
//	if (hasNumInput(&t.num)) {
//		outputNumInput(&(t.num), tvec);
//	}
//	else {
//		float minx= *((float *)(t.customData));
//		float maxx= *((float *)(t.customData) + 1);
//		float cval= t.values[0];
//		float val;
//
//		val= 2.0f*(cval-sval) / (maxx-minx);
//		CLAMP(val, -1.0f, 1.0f);
//
//		sprintf(&tvec[0], "%.4f", val);
//	}
//
//	sprintf(str, "TimeSlide: %s", &tvec[0]);
//}
//
//static void applyTimeSlide(TransInfo *t, float sval)
//{
//	TransData *td = t.data;
//	int i;
//
//	float minx= *((float *)(t.customData));
//	float maxx= *((float *)(t.customData) + 1);
//
//	/* set value for drawing black line */
//	if (t.spacetype == SPACE_ACTION) {
//		SpaceAction *saction= (SpaceAction *)t.sa.spacedata.first;
//		float cvalf = t.values[0];
//
//		saction.timeslide= cvalf;
//	}
//
//	/* it doesn't matter whether we apply to t.data or t.data2d, but t.data2d is more convenient */
//	for (i = 0 ; i < t.total; i++, td++) {
//		/* it is assumed that td.extra is a pointer to the AnimData,
//		 * whose active action is where this keyframe comes from
//		 * (this is only valid when not in NLA)
//		 */
//		AnimData *adt= (t.spacetype != SPACE_NLA) ? td.extra : NULL;
//		float cval = t.values[0];
//
//		/* apply NLA-mapping to necessary values */
//		if (adt)
//			cval= BKE_nla_tweakedit_remap(adt, cval, NLATIME_CONVERT_UNMAP);
//
//		/* only apply to data if in range */
//		if ((sval > minx) && (sval < maxx)) {
//			float cvalc= CLAMPIS(cval, minx, maxx);
//			float timefac;
//
//			/* left half? */
//			if (td.ival < sval) {
//				timefac= (sval - td.ival) / (sval - minx);
//				*(td.val)= cvalc - timefac * (cvalc - minx);
//			}
//			else {
//				timefac= (td.ival - sval) / (maxx - sval);
//				*(td.val)= cvalc + timefac * (maxx - cvalc);
//			}
//		}
//	}
//}
//
//int TimeSlide(TransInfo *t, short mval[2])
//{
//	View2D *v2d = (View2D *)t.view;
//	float cval[2], sval[2];
//	float minx= *((float *)(t.customData));
//	float maxx= *((float *)(t.customData) + 1);
//	char str[200];
//
//	/* calculate mouse co-ordinates */
//	UI_view2d_region_to_view(v2d, mval[0], mval[0], &cval[0], &cval[1]);
//	UI_view2d_region_to_view(v2d, t.imval[0], t.imval[0], &sval[0], &sval[1]);
//
//	/* t.values[0] stores cval[0], which is the current mouse-pointer location (in frames) */
//	t.values[0] = cval[0];
//
//	/* handle numeric-input stuff */
//	t.vec[0] = 2.0f*(cval[0]-sval[0]) / (maxx-minx);
//	applyNumInput(&t.num, &t.vec[0]);
//	t.values[0] = (maxx-minx) * t.vec[0] / 2.0 + sval[0];
//
//	headerTimeSlide(t, sval[0], str);
//	applyTimeSlide(t, sval[0]);
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ----------------- Scaling ----------------------- */
//
//void initTimeScale(TransInfo *t)
//{
//	t.mode = TFM_TIME_SCALE;
//	t.transform = TimeScale;
//
//	initMouseInputMode(t, &t.mouse, INPUT_NONE);
//	t.helpline = HLP_SPRING; /* set manually because we don't use a predefined input */
//
//	t.flag |= T_NULL_ONE;
//	t.num.flag |= NUM_NULL_ONE;
//
//	/* num-input has max of (n-1) */
//	t.idx_max = 0;
//	t.num.flag = 0;
//	t.num.idx_max = t.idx_max;
//
//	/* initialise snap like for everything else */
//	t.snap[0] = 0.0f;
//	t.snap[1] = t.snap[2] = 1.0f;
//}
//
//static void headerTimeScale(TransInfo *t, char *str) {
//	char tvec[60];
//
//	if (hasNumInput(&t.num))
//		outputNumInput(&(t.num), tvec);
//	else
//		sprintf(&tvec[0], "%.4f", t.values[0]);
//
//	sprintf(str, "ScaleX: %s", &tvec[0]);
//}
//
//static void applyTimeScale(TransInfo *t) {
//	Scene *scene = t.scene;
//	TransData *td = t.data;
//	int i;
//
//	const short autosnap= getAnimEdit_SnapMode(t);
//	const short doTime= getAnimEdit_DrawTime(t);
//	const double secf= FPS;
//
//
//	for (i = 0 ; i < t.total; i++, td++) {
//		/* it is assumed that td.extra is a pointer to the AnimData,
//		 * whose active action is where this keyframe comes from
//		 * (this is only valid when not in NLA)
//		 */
//		AnimData *adt= (t.spacetype != SPACE_NLA) ? td.extra : NULL;
//		float startx= CFRA;
//		float fac= t.values[0];
//
//		if (autosnap == SACTSNAP_STEP) {
//			if (doTime)
//				fac= (float)( floor(fac/secf + 0.5f) * secf );
//			else
//				fac= (float)( floor(fac + 0.5f) );
//		}
//
//		/* check if any need to apply nla-mapping */
//		if (adt)
//			startx= BKE_nla_tweakedit_remap(adt, startx, NLATIME_CONVERT_UNMAP);
//
//		/* now, calculate the new value */
//		*(td.val) = td.ival - startx;
//		*(td.val) *= fac;
//		*(td.val) += startx;
//
//		/* apply nearest snapping */
//		doAnimEdit_SnapFrame(t, td, adt, autosnap);
//	}
//}
//
//int TimeScale(TransInfo *t, short mval[2])
//{
//	float cval, sval;
//	float deltax, startx;
//	float width= 0.0f;
//	char str[200];
//
//	sval= t.imval[0];
//	cval= mval[0];
//
//	/* calculate scaling factor */
//	startx= sval-(width/2+(t.ar.winx)/2);
//	deltax= cval-(width/2+(t.ar.winx)/2);
//	t.values[0] = deltax / startx;
//
//	/* handle numeric-input stuff */
//	t.vec[0] = t.values[0];
//	applyNumInput(&t.num, &t.vec[0]);
//	t.values[0] = t.vec[0];
//	headerTimeScale(t, str);
//
//	applyTimeScale(t);
//
//	recalcData(t);
//
//	ED_area_headerprint(t.sa, str);
//
//	return 1;
//}
//
///* ************************************ */
//
//void BIF_TransformSetUndo(char *str)
//{
//	// TRANSFORM_FIX_ME
//	//Trans.undostr= str;
//}
//
//
//void NDofTransform()
//{
//#if 0 // TRANSFORM_FIX_ME
//    float fval[7];
//    float maxval = 50.0f; // also serves as threshold
//    int axis = -1;
//    int mode = 0;
//    int i;
//
//	getndof(fval);
//
//	for(i = 0; i < 6; i++)
//	{
//		float val = fabs(fval[i]);
//		if (val > maxval)
//		{
//			axis = i;
//			maxval = val;
//		}
//	}
//
//	switch(axis)
//	{
//		case -1:
//			/* No proper axis found */
//			break;
//		case 0:
//		case 1:
//		case 2:
//			mode = TFM_TRANSLATION;
//			break;
//		case 4:
//			mode = TFM_ROTATION;
//			break;
//		case 3:
//		case 5:
//			mode = TFM_TRACKBALL;
//			break;
//		default:
//			printf("ndof: what we are doing here ?");
//	}
//
//	if (mode != 0)
//	{
//		initTransform(mode, CTX_NDOF);
//		Transform();
//	}
//#endif
//}
}