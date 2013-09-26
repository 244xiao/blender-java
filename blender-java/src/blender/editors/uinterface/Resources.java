/**
 * $Id: Resources.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
 *
 * ***** BEGIN GPL/BL DUAL LICENSE BLOCK *****
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. The Blender
 * Foundation also sells licenses for use in proprietary software under
 * the Blender License.  See http://www.blender.org/BL/ for information
 * about this.
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
 * ***** END GPL/BL DUAL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ThemeSpace;
import blender.makesdna.sdna.bTheme;
import resources.ResourceAnchor;
//import blender.makesrna.RNATypes.EnumPropertyItem;
import static blender.blenkernel.Blender.U;
import javax.media.opengl.GL2;

public class Resources {

    /* global for themes */

    public static interface VectorDrawFunc {
        public void run(GL2 gl, int x, int y, int w, int h, float alpha);
    }

    public static enum BIFIconID {
//#define BIFICONID_FIRST		(ICON_BLENDER)
	/* ui */
ICON_BLENDER,
ICON_QUESTION,
ICON_ERROR,
ICON_BLANK1,	// XXX this is used lots, it's not actually 'blank'
ICON_TRIA_RIGHT,
ICON_TRIA_DOWN,
ICON_TRIA_LEFT,
ICON_TRIA_UP,
ICON_ARROW_LEFTRIGHT,
ICON_PLUS,
ICON_DISCLOSURE_TRI_DOWN,
ICON_DISCLOSURE_TRI_RIGHT,
ICON_RADIOBUT_OFF,
ICON_RADIOBUT_ON,
ICON_MENU_PANEL,
ICON_PYTHON,
ICON_BLANK003,
ICON_DOT,
ICON_BLANK004,
ICON_X,
ICON_BLANK005,
ICON_GO_LEFT,
ICON_BLANK006,
ICON_BLANK007,
ICON_BLANK008,
ICON_BLANK008b,

	/* ui */
ICON_FULLSCREEN,
ICON_SPLITSCREEN,
ICON_RIGHTARROW_THIN,
ICON_BORDERMOVE,
ICON_VIEWZOOM,
ICON_ZOOMIN,
ICON_ZOOMOUT,
ICON_PANEL_CLOSE,
ICON_BLANK009,
ICON_EYEDROPPER,
ICON_BLANK010,
ICON_AUTO,
ICON_CHECKBOX_DEHLT,
ICON_CHECKBOX_HLT,
ICON_UNLOCKED,
ICON_LOCKED,
ICON_UNPINNED,
ICON_PINNED,
ICON_BLANK015,
ICON_RIGHTARROW,
ICON_DOWNARROW_HLT,
ICON_DOTSUP,
ICON_DOTSDOWN,
ICON_LINK,
ICON_INLINK,
ICON_BLANK012b,

	/* various ui */
ICON_HELP,
ICON_GHOSTDRAW,
ICON_COLOR,
ICON_LINKED,
ICON_UNLINKED,
ICON_HAND,
ICON_ZOOM_ALL,
ICON_ZOOM_SELECTED,
ICON_ZOOM_PREVIOUS,
ICON_ZOOM_IN,
ICON_ZOOM_OUT,
ICON_RENDER_REGION,
ICON_BORDER_RECT,
ICON_BORDER_LASSO,
ICON_FREEZE,
ICON_BLANK031,
ICON_BLANK032,
ICON_BLANK033,
ICON_BLANK034,
ICON_BLANK035,
ICON_BLANK036,
ICON_BLANK037,
ICON_BLANK038,
ICON_BLANK039,
ICON_BLANK040,
ICON_BLANK040b,

	/* BUTTONS */
ICON_LAMP,
ICON_MATERIAL,
ICON_TEXTURE,
ICON_ANIM,
ICON_WORLD,
ICON_SCENE,
ICON_EDIT,
ICON_GAME,
ICON_RADIO,
ICON_SCRIPT,
ICON_PARTICLES,
ICON_PHYSICS,
ICON_SPEAKER,
ICON_BLANK041,
ICON_BLANK042,
ICON_BLANK043,
ICON_BLANK044,
ICON_BLANK045,
ICON_BLANK046,
ICON_BLANK047,
ICON_BLANK048,
ICON_BLANK049,
ICON_BLANK050,
ICON_BLANK051,
ICON_BLANK052,
ICON_BLANK052b,

	/* EDITORS */
ICON_VIEW3D,
ICON_IPO,
ICON_OOPS,
ICON_BUTS,
ICON_FILESEL,
ICON_IMAGE_COL,
ICON_INFO,
ICON_SEQUENCE,
ICON_TEXT,
ICON_IMASEL,
ICON_SOUND,
ICON_ACTION,
ICON_NLA,
ICON_SCRIPTWIN,
ICON_TIME,
ICON_NODE,
ICON_LOGIC,
ICON_CONSOLE,
ICON_PREFERENCES,
ICON_BLANK056,
ICON_BLANK057,
ICON_BLANK058,
ICON_BLANK059,
ICON_BLANK060,
ICON_BLANK061,
ICON_BLANK061b,

	/* MODES */
ICON_OBJECT_DATAMODE,	// XXX fix this up
ICON_EDITMODE_HLT,
ICON_FACESEL_HLT,
ICON_VPAINT_HLT,
ICON_TPAINT_HLT,
ICON_WPAINT_HLT,
ICON_SCULPTMODE_HLT,
ICON_POSE_HLT,
ICON_PARTICLEMODE,
ICON_BLANK062,
ICON_BLANK063,
ICON_BLANK064,
ICON_BLANK065,
ICON_BLANK066,
ICON_BLANK067,
ICON_BLANK068,
ICON_BLANK069,
ICON_BLANK070,
ICON_BLANK071,
ICON_BLANK072,
ICON_BLANK073,
ICON_BLANK074,
ICON_BLANK075,
ICON_BLANK076,
ICON_BLANK077,
ICON_BLANK077b,

	/* DATA */
ICON_SCENE_DATA,
ICON_RENDERLAYERS,
ICON_WORLD_DATA,
ICON_OBJECT_DATA,
ICON_MESH_DATA,
ICON_CURVE_DATA,
ICON_META_DATA,
ICON_LATTICE_DATA,
ICON_LAMP_DATA,
ICON_MATERIAL_DATA,
ICON_TEXTURE_DATA,
ICON_ANIM_DATA,
ICON_CAMERA_DATA,
ICON_PARTICLE_DATA,
ICON_LIBRARY_DATA_DIRECT,
ICON_GROUP,
ICON_ARMATURE_DATA,
ICON_POSE_DATA,
ICON_BONE_DATA,
ICON_CONSTRAINT,
ICON_SHAPEKEY_DATA,
ICON_BLANK079a,
ICON_BLANK079,
ICON_PACKAGE,
ICON_UGLYPACKAGE,
ICON_BLANK079b,

	/* DATA */
ICON_BRUSH_DATA,
ICON_IMAGE_DATA,
ICON_FILE,
ICON_FCURVE,
ICON_FONT_DATA,
ICON_RENDER_RESULT,
ICON_SURFACE_DATA,
ICON_EMPTY_DATA,
ICON_SETTINGS,
ICON_BLANK080D,
ICON_BLANK080E,
ICON_BLANK080F,
ICON_BLANK080,
ICON_STRANDS,
ICON_LIBRARY_DATA_INDIRECT,
ICON_BLANK082,
ICON_BLANK083,
ICON_BLANK084,
ICON_GROUP_BONE,
ICON_GROUP_VERTEX,
ICON_GROUP_VCOL,
ICON_GROUP_UVS,
ICON_BLANK089,
ICON_BLANK090,
ICON_RNA,
ICON_BLANK090b,

	/* available */
ICON_BLANK092,
ICON_BLANK093,
ICON_BLANK094,
ICON_BLANK095,
ICON_BLANK096,
ICON_BLANK097,
ICON_BLANK098,
ICON_BLANK099,
ICON_BLANK100,
ICON_BLANK101,
ICON_BLANK102,
ICON_BLANK103,
ICON_BLANK104,
ICON_BLANK105,
ICON_BLANK106,
ICON_BLANK107,
ICON_BLANK108,
ICON_BLANK109,
ICON_BLANK110,
ICON_BLANK111,
ICON_BLANK112,
ICON_BLANK113,
ICON_BLANK114,
ICON_BLANK115,
ICON_BLANK116,
ICON_BLANK116b,

	/* OUTLINER */
ICON_OUTLINER_OB_EMPTY,
ICON_OUTLINER_OB_MESH,
ICON_OUTLINER_OB_CURVE,
ICON_OUTLINER_OB_LATTICE,
ICON_OUTLINER_OB_META,
ICON_OUTLINER_OB_LAMP,
ICON_OUTLINER_OB_CAMERA,
ICON_OUTLINER_OB_ARMATURE,
ICON_OUTLINER_OB_FONT,
ICON_OUTLINER_OB_SURFACE,
ICON_BLANK119,
ICON_BLANK120,
ICON_BLANK121,
ICON_BLANK122,
ICON_BLANK123,
ICON_BLANK124,
ICON_BLANK125,
ICON_BLANK126,
ICON_BLANK127,
ICON_RESTRICT_VIEW_OFF,
ICON_RESTRICT_VIEW_ON,
ICON_RESTRICT_SELECT_OFF,
ICON_RESTRICT_SELECT_ON,
ICON_RESTRICT_RENDER_OFF,
ICON_RESTRICT_RENDER_ON,
ICON_BLANK127b,

	/* OUTLINER */
ICON_OUTLINER_DATA_EMPTY,
ICON_OUTLINER_DATA_MESH,
ICON_OUTLINER_DATA_CURVE,
ICON_OUTLINER_DATA_LATTICE,
ICON_OUTLINER_DATA_META,
ICON_OUTLINER_DATA_LAMP,
ICON_OUTLINER_DATA_CAMERA,
ICON_OUTLINER_DATA_ARMATURE,
ICON_OUTLINER_DATA_FONT,
ICON_OUTLINER_DATA_SURFACE,
ICON_OUTLINER_DATA_POSE,
ICON_BLANK129,
ICON_BLANK130,
ICON_BLANK131,
ICON_BLANK132,
ICON_BLANK133,
ICON_BLANK134,
ICON_BLANK135,
ICON_BLANK136,
ICON_BLANK137,
ICON_BLANK138,
ICON_BLANK139,
ICON_BLANK140,
ICON_BLANK141,
ICON_BLANK142,
ICON_BLANK142b,

//	/* PRIMITIVES */
//ICON_MESH_PLANE,
//ICON_MESH_CUBE,
//ICON_MESH_CIRCLE,
//ICON_MESH_UVSPHERE,
//ICON_MESH_ICOSPHERE,
//ICON_MESH_GRID,
//ICON_MESH_MONKEY,
//ICON_MESH_TUBE,
//ICON_MESH_DONUT,
//ICON_MESH_CONE,
//ICON_BLANK610,
//ICON_BLANK611,
//ICON_LAMP_POINT,
//ICON_LAMP_SUN,
//ICON_LAMP_SPOT,
//ICON_LAMP_HEMI,
//ICON_LAMP_AREA,
//ICON_BLANK617,
//ICON_BLANK618,
//ICON_BLANK619,
//ICON_META_PLANE,
//ICON_META_CUBE,
//ICON_META_BALL,
//ICON_META_ELLIPSOID,
//ICON_META_TUBE,
//ICON_BLANK625,

//	/* PRIMITIVES */
//ICON_SURFACE_NCURVE,
//ICON_SURFACE_NCIRCLE,
//ICON_SURFACE_NSURFACE,
//ICON_SURFACE_NTUBE,
//ICON_SURFACE_NSPHERE,
//ICON_SURFACE_NDONUT,
//ICON_BLANK636,
//ICON_BLANK637,
//ICON_BLANK638,
//ICON_CURVE_BEZCURVE,
//ICON_CURVE_BEZCIRCLE,
//ICON_CURVE_NCURVE,
//ICON_CURVE_NCIRCLE,
//ICON_CURVE_PATH,
//ICON_BLANK644,
//ICON_BLANK645,
//ICON_BLANK646,
//ICON_BLANK647,
//ICON_BLANK648,
//ICON_BLANK649,
//ICON_BLANK650,
//ICON_BLANK651,
//ICON_BLANK652,
//ICON_BLANK653,
//ICON_BLANK654,
//ICON_BLANK655,
//
//	/* EMPTY */
//ICON_BLANK660,
//ICON_BLANK661,
//ICON_BLANK662,
//ICON_BLANK663,
//ICON_BLANK664,
//ICON_BLANK665,
//ICON_BLANK666,
//ICON_BLANK667,
//ICON_BLANK668,
//ICON_BLANK669,
//ICON_BLANK670,
//ICON_BLANK671,
//ICON_BLANK672,
//ICON_BLANK673,
//ICON_BLANK674,
//ICON_BLANK675,
//ICON_BLANK676,
//ICON_BLANK677,
//ICON_BLANK678,
//ICON_BLANK679,
//ICON_BLANK680,
//ICON_BLANK681,
//ICON_BLANK682,
//ICON_BLANK683,
//ICON_BLANK684,
//ICON_BLANK685,
//
//	/* EMPTY */
//ICON_BLANK690,
//ICON_BLANK691,
//ICON_BLANK692,
//ICON_BLANK693,
//ICON_BLANK694,
//ICON_BLANK695,
//ICON_BLANK696,
//ICON_BLANK697,
//ICON_BLANK698,
//ICON_BLANK699,
//ICON_BLANK700,
//ICON_BLANK701,
//ICON_BLANK702,
//ICON_BLANK703,
//ICON_BLANK704,
//ICON_BLANK705,
//ICON_BLANK706,
//ICON_BLANK707,
//ICON_BLANK708,
//ICON_BLANK709,
//ICON_BLANK710,
//ICON_BLANK711,
//ICON_BLANK712,
//ICON_BLANK713,
//ICON_BLANK714,
//ICON_BLANK715,
//
//	/* EMPTY */
//ICON_BLANK720,
//ICON_BLANK721,
//ICON_BLANK722,
//ICON_BLANK733,
//ICON_BLANK734,
//ICON_BLANK735,
//ICON_BLANK736,
//ICON_BLANK737,
//ICON_BLANK738,
//ICON_BLANK739,
//ICON_BLANK740,
//ICON_BLANK741,
//ICON_BLANK742,
//ICON_BLANK743,
//ICON_BLANK744,
//ICON_BLANK745,
//ICON_BLANK746,
//ICON_BLANK747,
//ICON_BLANK748,
//ICON_BLANK749,
//ICON_BLANK750,
//ICON_BLANK751,
//ICON_BLANK752,
//ICON_BLANK753,
//ICON_BLANK754,
//ICON_BLANK755,
//
//	/* EMPTY */
//ICON_BLANK760,
//ICON_BLANK761,
//ICON_BLANK762,
//ICON_BLANK763,
//ICON_BLANK764,
//ICON_BLANK765,
//ICON_BLANK766,
//ICON_BLANK767,
//ICON_BLANK768,
//ICON_BLANK769,
//ICON_BLANK770,
//ICON_BLANK771,
//ICON_BLANK772,
//ICON_BLANK773,
//ICON_BLANK774,
//ICON_BLANK775,
//ICON_BLANK776,
//ICON_BLANK777,
//ICON_BLANK778,
//ICON_BLANK779,
//ICON_BLANK780,
//ICON_BLANK781,
//ICON_BLANK782,
//ICON_BLANK783,
//ICON_BLANK784,
//ICON_BLANK785,

	/* MODIFIERS */
ICON_MODIFIER,
ICON_MOD_WAVE,
ICON_MOD_BUILD,
ICON_MOD_DECIM,
ICON_MOD_MIRROR,
ICON_MOD_SOFT,
ICON_MOD_SUBSURF,
ICON_HOOK,
ICON_MOD_PHYSICS,
ICON_MOD_PARTICLES,
ICON_MOD_BOOLEAN,
ICON_MOD_EDGESPLIT,
ICON_MOD_ARRAY,
ICON_MOD_UVPROJECT,
ICON_MOD_DISPLACE,
ICON_MOD_CURVE,
ICON_MOD_LATTICE,
ICON_BLANK143,
ICON_MOD_ARMATURE,
ICON_MOD_SHRINKWRAP,
ICON_MOD_CAST,
ICON_MOD_MESHDEFORM,
ICON_MOD_BEVEL,
ICON_MOD_SMOOTH,
ICON_MOD_SIMPLEDEFORM,
ICON_MOD_MASK,

	/* MODIFIERS */
ICON_MOD_CLOTH,
ICON_MOD_EXPLODE,
ICON_MOD_FLUIDSIM,
ICON_MOD_MULTIRES,
ICON_BLANK157,
ICON_BLANK158,
ICON_BLANK159,
ICON_BLANK160,
ICON_BLANK161,
ICON_BLANK162,
ICON_BLANK163,
ICON_BLANK164,
ICON_BLANK165,
ICON_BLANK166,
ICON_BLANK167,
ICON_BLANK168,
ICON_BLANK169,
ICON_BLANK170,
ICON_BLANK171,
ICON_BLANK172,
ICON_BLANK173,
ICON_BLANK174,
ICON_BLANK175,
ICON_BLANK176,
ICON_BLANK177,
ICON_BLANK177b,

	/* ANIMATION */
ICON_REC,
ICON_PLAY,
ICON_FF,
ICON_REW,
ICON_PAUSE,
ICON_PREV_KEYFRAME,
ICON_NEXT_KEYFRAME,
ICON_PLAY_AUDIO,
ICON_PLAY_REVERSE,
ICON_BLANK179,
ICON_BLANK180,
ICON_PMARKER_ACT,
ICON_PMARKER_SEL,
ICON_PMARKER,
ICON_MARKER_HLT,
ICON_MARKER,
ICON_SPACE2,	// XXX
ICON_SPACE3,	// XXX
ICON_BLANK181,
ICON_KEY_DEHLT,
ICON_KEY_HLT,
ICON_MUTE_IPO_OFF,
ICON_MUTE_IPO_ON,
ICON_BLANK182,
ICON_BLANK183,
ICON_BLANK183b,

	/* available */
ICON_BLANK184,
ICON_BLANK185,
ICON_BLANK186,
ICON_BLANK187,
ICON_BLANK188,
ICON_BLANK189,
ICON_BLANK190,
ICON_BLANK191,
ICON_BLANK192,
ICON_BLANK193,
ICON_BLANK194,
ICON_BLANK195,
ICON_BLANK196,
ICON_BLANK197,
ICON_BLANK198,
ICON_BLANK199,
ICON_BLANK200,
ICON_BLANK201,
ICON_BLANK202,
ICON_BLANK203,
ICON_BLANK204,
ICON_BLANK205,
ICON_BLANK206,
ICON_BLANK207,
ICON_BLANK208,
ICON_BLANK208b,

	/* EDITING */
ICON_VERTEXSEL,
ICON_EDGESEL,
ICON_FACESEL,
ICON_LINKEDSEL,
ICON_BLANK210,
ICON_ROTATE,
ICON_CURSOR,
ICON_ROTATECOLLECTION,
ICON_ROTATECENTER,
ICON_ROTACTIVE,
ICON_ALIGN,
ICON_BLANK211,
ICON_SMOOTHCURVE,
ICON_SPHERECURVE,
ICON_ROOTCURVE,
ICON_SHARPCURVE,
ICON_LINCURVE,
ICON_NOCURVE,
ICON_RNDCURVE,
ICON_PROP_OFF,
ICON_PROP_ON,
ICON_PROP_CON,
ICON_BLANK212,
ICON_BLANK213,
ICON_BLANK214,
ICON_BLANK214b,

	/* EDITING */
ICON_MAN_TRANS,
ICON_MAN_ROT,
ICON_MAN_SCALE,
ICON_MANIPUL,
//ICON_BLANK215,
ICON_SNAP_OFF,
//ICON_SNAP_GEAR,
ICON_SNAP_ON,
//ICON_SNAP_GEO,
ICON_SNAP_NORMAL,
//ICON_SNAP_NORMAL,
ICON_SNAP_INCREMENT,
ICON_SNAP_VERTEX,
ICON_SNAP_EDGE,
ICON_SNAP_FACE,
ICON_SNAP_VOLUME,
ICON_UVS_FACE,
ICON_STICKY_UVS_LOC,
ICON_STICKY_UVS_DISABLE,
ICON_STICKY_UVS_VERT,
ICON_CLIPUV_DEHLT,
ICON_CLIPUV_HLT,
ICON_SNAP_PEEL_OBJECT,
ICON_BLANK221,
ICON_GRID,
ICON_GEARS,
ICON_BLANK224,
ICON_BLANK225,
ICON_BLANK226,
ICON_BLANK226b,

	/* EDITING */
ICON_PASTEDOWN,
ICON_COPYDOWN,
ICON_PASTEFLIPUP,
ICON_PASTEFLIPDOWN,
ICON_BLANK227,
ICON_BLANK228,
ICON_BLANK229,
ICON_BLANK230,
//ICON_BLANK231,
ICON_SNAP_SURFACE,
ICON_BLANK232,
ICON_BLANK233,
//ICON_BLANK234,
ICON_RETOPO,
//ICON_BLANK235,
ICON_UV_VERTEXSEL,
//ICON_BLANK236,
ICON_UV_EDGESEL,
//ICON_BLANK237,
ICON_UV_FACESEL,
//ICON_BLANK238,
ICON_UV_ISLANDSEL,
//ICON_BLANK239,
ICON_UV_SYNC_SELECT,
ICON_BLANK240,
ICON_BLANK241,
ICON_BLANK242,
ICON_BLANK243,
ICON_BLANK244,
ICON_BLANK245,
ICON_BLANK246,
ICON_BLANK247,
ICON_BLANK247b,

	/* 3D VIEW */
ICON_BBOX,
ICON_WIRE,
ICON_SOLID,
ICON_SMOOTH,
ICON_POTATO,
ICON_BLANK248,
ICON_ORTHO,
ICON_BLANK249,
ICON_CAMERA,
ICON_LOCKVIEW_OFF,
ICON_LOCKVIEW_ON,
ICON_BLANK250,
ICON_AXIS_SIDE,
ICON_AXIS_FRONT,
ICON_AXIS_TOP,
ICON_NDOF_DOM,
ICON_NDOF_TURN,
ICON_NDOF_FLY,
ICON_NDOF_TRANS,
ICON_LAYER_USED,
ICON_LAYER_ACTIVE,
ICON_BLANK254,
ICON_BLANK255,
ICON_BLANK256,
ICON_BLANK257,
ICON_BLANK257b,

	/* available */
ICON_BLANK258,
ICON_BLANK259,
ICON_BLANK260,
ICON_BLANK261,
ICON_BLANK262,
ICON_BLANK263,
ICON_BLANK264,
ICON_BLANK265,
ICON_BLANK266,
ICON_BLANK267,
ICON_BLANK268,
ICON_BLANK269,
ICON_BLANK270,
ICON_BLANK271,
ICON_BLANK272,
ICON_BLANK273,
ICON_BLANK274,
ICON_BLANK275,
ICON_BLANK276,
ICON_BLANK277,
ICON_BLANK278,
ICON_BLANK279,
ICON_BLANK280,
ICON_BLANK281,
ICON_BLANK282,
ICON_BLANK282b,

	/* FILE SELECT */
ICON_SORTALPHA,
ICON_SORTBYEXT,
ICON_SORTTIME,
ICON_SORTSIZE,
ICON_LONGDISPLAY,
ICON_SHORTDISPLAY,
ICON_GHOST,
ICON_IMGDISPLAY,
ICON_BLANK284,
ICON_BLANK285,
ICON_BOOKMARKS,
ICON_FONTPREVIEW,
ICON_FILTER,
ICON_NEWFOLDER,
ICON_BLANK285F,
ICON_FILE_PARENT,
ICON_FILE_REFRESH,
ICON_FILE_FOLDER,
ICON_FILE_BLANK,
ICON_FILE_BLEND,
ICON_FILE_IMAGE,
ICON_FILE_MOVIE,
ICON_FILE_SCRIPT,
ICON_FILE_SOUND,
ICON_FILE_FONT,
ICON_BLANK291b,

	/* available */
ICON_BLANK292,
ICON_BLANK293,
ICON_BLANK294,
ICON_BLANK295,
ICON_BLANK296,
ICON_BLANK297,
ICON_BLANK298,
ICON_BLANK299,
ICON_BLANK300,
ICON_BLANK301,
ICON_BLANK302,
ICON_BLANK303,
ICON_BLANK304,
ICON_BLANK305,
ICON_BLANK306,
ICON_BLANK307,
ICON_BLANK308,
ICON_BLANK309,
ICON_BLANK310,
ICON_BLANK311,
ICON_BLANK312,
ICON_BLANK313,
ICON_BLANK314,
ICON_BLANK315,
ICON_BLANK316,
ICON_DISK_DRIVE,

	/* SHADING / TEXT */
ICON_MATPLANE,
ICON_MATSPHERE,
ICON_MATCUBE,
ICON_MONKEY,
ICON_HAIR,
ICON_RING,
ICON_BLANK317,
ICON_BLANK318,
ICON_BLANK319,
ICON_BLANK320,
ICON_BLANK321,
ICON_BLANK322,
ICON_WORDWRAP_OFF,
ICON_WORDWRAP_ON,
ICON_SYNTAX_OFF,
ICON_SYNTAX_ON,
ICON_LINENUMBERS_OFF,
ICON_LINENUMBERS_ON,
ICON_SCRIPTPLUGINS,		// XXX CREATE NEW
ICON_BLANK323,
ICON_BLANK324,
ICON_BLANK325,
ICON_BLANK326,
ICON_BLANK327,
ICON_BLANK328,
ICON_BLANK328b,

	/* SEQUENCE / IMAGE EDITOR */
ICON_SEQ_SEQUENCER,
ICON_SEQ_PREVIEW,
ICON_SEQ_LUMA_WAVEFORM,
ICON_SEQ_CHROMA_SCOPE,
ICON_SEQ_HISTOGRAM,
ICON_BLANK330,
ICON_BLANK331,
ICON_BLANK332,
ICON_BLANK333,
ICON_IMAGE_RGB,	// XXX CHANGE TO STRAIGHT ALPHA, Z ETC
ICON_IMAGE_RGB_ALPHA,
ICON_IMAGE_ALPHA,
ICON_IMAGE_ZDEPTH,
ICON_IMAGEFILE,
ICON_BLANK336,
ICON_BLANK337,
ICON_BLANK338,
ICON_BLANK339,
ICON_BLANK340,
ICON_BLANK341,
ICON_BLANK342,
ICON_BLANK343,
ICON_BLANK344,
ICON_BLANK345,
ICON_BLANK346,
ICON_BLANK346b,

	/* vector icons */

VICON_VIEW3D,
VICON_EDIT,
VICON_EDITMODE_DEHLT,
VICON_EDITMODE_HLT,
VICON_DISCLOSURE_TRI_RIGHT,
VICON_DISCLOSURE_TRI_DOWN,
VICON_MOVE_UP,
VICON_MOVE_DOWN,
VICON_X,
VICON_SMALL_TRI_RIGHT,

//#include "UI_icons.h"
	BIFICONID_LAST
//#define BIFNICONIDS			(BIFICONID_LAST-BIFICONID_FIRST + 1)
};

public static final BIFIconID BIFICONID_FIRST= BIFIconID.ICON_BLENDER;
public static final int BIFNICONIDS= BIFIconID.BIFICONID_LAST.ordinal() - BIFICONID_FIRST.ordinal() + 1;

//public static EnumPropertyItem[] icon_items() {
//	EnumPropertyItem[] iconEnums = new EnumPropertyItem[BIFIconID.values().length+1];
//	BIFIconID[] icons = BIFIconID.values();
//	for (int i=0; i<iconEnums.length-1; i++) {
//		BIFIconID icon = icons[i];
//		iconEnums[i] = new EnumPropertyItem(icon.ordinal(), icon.name(), 0, icon.name(), "");
//	}
//	iconEnums[iconEnums.length-1] = new EnumPropertyItem(0, null, 0, null, null);
//	return iconEnums;
//}

//    /* elubie: TODO: move the typedef for icons to BIF_interface_icons.h */
//    /* and add/replace include of BIF_resources.h by BIF_interface_icons.h */
//    public static enum BIFIconID {
//        ICON_VIEW3D,
//        ICON_IPO,
//        ICON_OOPS,
//        ICON_BUTS,
//        ICON_FILESEL,
//        ICON_IMAGE_COL,
//        ICON_INFO,
//        ICON_SEQUENCE,
//        ICON_TEXT,
//        ICON_IMASEL,
//        ICON_SOUND,
//        ICON_ACTION,
//        ICON_NLA,
//        ICON_SCRIPTWIN,
//        ICON_TIME,
//        ICON_NODE,
//        ICON_SPACE2,
//        ICON_SPACE3,
//        ICON_SPACE4,
//        ICON_TRIA_LEFT,
//        ICON_TRIA_UP,
//        ICON_FONTPREVIEW,
//        ICON_BLANK4,
//        ICON_WORDWRAP,
//        ICON_WORDWRAP_OFF,
//        ICON_ORTHO,
//        ICON_PERSP,
//        ICON_CAMERA,
//        ICON_PARTICLES,
//        ICON_BBOX,
//        ICON_WIRE,
//        ICON_SOLID,
//        ICON_SMOOTH,
//        ICON_POTATO,
//        ICON_MARKER_HLT,
//        ICON_PMARKER_ACT,
//        ICON_PMARKER_SEL,
//        ICON_PMARKER,
//        ICON_VIEWZOOM,
//        ICON_SORTALPHA,
//        ICON_SORTTIME,
//        ICON_SORTSIZE,
//        ICON_LONGDISPLAY,
//        ICON_SHORTDISPLAY,
//        ICON_TRIA_DOWN,
//        ICON_TRIA_RIGHT,
//        ICON_NDOF_TURN,
//        ICON_NDOF_FLY,
//        ICON_NDOF_TRANS,
//        ICON_NDOF_DOM,
//        ICON_VIEW_AXIS_ALL,
//        ICON_VIEW_AXIS_NONE,
//        ICON_VIEW_AXIS_NONE2,
//        ICON_VIEW_AXIS_TOP,
//        ICON_VIEW_AXIS_FRONT,
//        ICON_VIEW_AXIS_SIDE,
//        ICON_POSE_DEHLT,
//        ICON_POSE_HLT,
//        ICON_BORDERMOVE,
//        ICON_MAYBE_ITS_A_LASSO,
//        ICON_BLANK1, /* ATTENTION, someone decided to use this throughout blender
//        and didn't care to neither rename it nor update the PNG */
//        ICON_VERSE,
//        ICON_MOD_BOOLEAN,
//        ICON_ARMATURE,
//        ICON_PAUSE,
//        ICON_ALIGN,
//        ICON_REC,
//        ICON_PLAY,
//        ICON_FF,
//        ICON_REW,
//        ICON_PYTHON,
//        ICON_PYTHON_ON,
//        ICON_BLANK12,
//        ICON_BLANK13,
//        ICON_BLANK14,
//        ICON_DOTSUP,
//        ICON_DOTSDOWN,
//        ICON_MENU_PANEL,
//        ICON_AXIS_SIDE,
//        ICON_AXIS_FRONT,
//        ICON_AXIS_TOP,
//        ICON_STICKY_UVS_LOC,
//        ICON_STICKY_UVS_DISABLE,
//        ICON_STICKY_UVS_VERT,
//        ICON_PREV_KEYFRAME,
//        ICON_NEXT_KEYFRAME,
//        ICON_ENVMAP,
//        ICON_TRANSP_HLT,
//        ICON_TRANSP_DEHLT,
//        ICON_CIRCLE_DEHLT,
//        ICON_CIRCLE_HLT,
//        ICON_TPAINT_DEHLT,
//        ICON_TPAINT_HLT,
//        ICON_WPAINT_DEHLT,
//        ICON_WPAINT_HLT,
//        ICON_MARKER,
//        ICON_BLANK15,
//        ICON_BLANK16,
//        ICON_BLANK17,
//        ICON_BLANK18,
//        ICON_X,
//        ICON_GO_LEFT,
//        ICON_NO_GO_LEFT,
//        ICON_UNLOCKED,
//        ICON_LOCKED,
//        ICON_PARLIB,
//        ICON_DATALIB,
//        ICON_AUTO,
//        ICON_MATERIAL_DEHLT2,
//        ICON_RING,
//        ICON_GRID,
//        ICON_PROPEDIT,
//        ICON_KEEPRECT,
//        ICON_DESEL_CUBE_VERTS,
//        ICON_EDITMODE_DEHLT,
//        ICON_EDITMODE_HLT,
//        ICON_VPAINT_DEHLT,
//        ICON_VPAINT_HLT,
//        ICON_FACESEL_DEHLT,
//        ICON_FACESEL_HLT,
//        ICON_EDIT_DEHLT,
//        ICON_BOOKMARKS,
//        ICON_BLANK20,
//        ICON_BLANK21,
//        ICON_BLANK22,
//        ICON_HELP,
//        ICON_ERROR,
//        ICON_FOLDER_DEHLT,
//        ICON_FOLDER_HLT,
//        ICON_BLUEIMAGE_DEHLT,
//        ICON_BLUEIMAGE_HLT,
//        ICON_BPIBFOLDER_DEHLT,
//        ICON_BPIBFOLDER_HLT,
//        ICON_BPIBFOLDER_ERR,
//        ICON_UGLY_GREEN_RING,
//        ICON_GHOST,
//        ICON_SORTBYEXT,
//        ICON_SCULPTMODE_HLT,
//        ICON_VERTEXSEL,
//        ICON_EDGESEL,
//        ICON_FACESEL,
//        ICON_PLUS,
//        ICON_BPIBFOLDER_X,
//        ICON_BPIBFOLDERGREY,
//        ICON_MAGNIFY,
//        ICON_INFO2,
//        ICON_BLANK23,
//        ICON_BLANK24,
//        ICON_BLANK25,
//        ICON_BLANK26,
//        ICON_RIGHTARROW,
//        ICON_DOWNARROW_HLT,
//        ICON_ROUNDBEVELTHING,
//        ICON_FULLTEXTURE,
//        ICON_HOOK,
//        ICON_DOT,
//        ICON_WORLD_DEHLT,
//        ICON_CHECKBOX_DEHLT,
//        ICON_CHECKBOX_HLT,
//        ICON_LINK,
//        ICON_INLINK,
//        ICON_ZOOMIN,
//        ICON_ZOOMOUT,
//        ICON_PASTEDOWN,
//        ICON_COPYDOWN,
//        ICON_CONSTANT,
//        ICON_LINEAR,
//        ICON_CYCLIC,
//        ICON_KEY_DEHLT,
//        ICON_KEY_HLT,
//        ICON_GRID2,
//        ICON_BLANK27,
//        ICON_BLANK28,
//        ICON_BLANK29,
//        ICON_BLANK30,
//        ICON_EYE,
//        ICON_LAMP,
//        ICON_MATERIAL,
//        ICON_TEXTURE,
//        ICON_ANIM,
//        ICON_WORLD,
//        ICON_SCENE,
//        ICON_EDIT,
//        ICON_GAME,
//        ICON_PAINT,
//        ICON_RADIO,
//        ICON_SCRIPT,
//        ICON_SPEAKER,
//        ICON_PASTEUP,
//        ICON_COPYUP,
//        ICON_PASTEFLIPUP,
//        ICON_PASTEFLIPDOWN,
//        ICON_CYCLICLINEAR,
//        ICON_PIN_DEHLT,
//        ICON_PIN_HLT,
//        ICON_LITTLEGRID,
//        ICON_BLANK31,
//        ICON_BLANK32,
//        ICON_BLANK33,
//        ICON_BLANK34,
//        ICON_FULLSCREEN,
//        ICON_SPLITSCREEN,
//        ICON_RIGHTARROW_THIN,
//        ICON_DISCLOSURE_TRI_RIGHT,
//        ICON_DISCLOSURE_TRI_DOWN,
//        ICON_SCENE_SEPIA,
//        ICON_SCENE_DEHLT,
//        ICON_OBJECT,
//        ICON_MESH,
//        ICON_CURVE,
//        ICON_MBALL,
//        ICON_LATTICE,
//        ICON_LAMP_DEHLT,
//        ICON_MATERIAL_DEHLT,
//        ICON_TEXTURE_DEHLT,
//        ICON_IPO_DEHLT,
//        ICON_LIBRARY_DEHLT,
//        ICON_IMAGE_DEHLT,
//        ICON_EYEDROPPER,
//        ICON_WINDOW_WINDOW,
//        ICON_PANEL_CLOSE,
//        ICON_PHYSICS,
//        ICON_LAYER_USED,
//        ICON_LAYER_ACTIVE,
//        ICON_BLANK38,
//        ICON_BLENDER,
//        ICON_PACKAGE,
//        ICON_UGLYPACKAGE,
//        ICON_MATPLANE,
//        ICON_MATSPHERE,
//        ICON_MATCUBE,
//        ICON_SCENE_HLT,
//        ICON_OBJECT_HLT,
//        ICON_MESH_HLT,
//        ICON_CURVE_HLT,
//        ICON_MBALL_HLT,
//        ICON_LATTICE_HLT,
//        ICON_LAMP_HLT,
//        ICON_MATERIAL_HLT,
//        ICON_TEXTURE_HLT,
//        ICON_IPO_HLT,
//        ICON_LIBRARY_HLT,
//        ICON_IMAGE_HLT,
//        ICON_CONSTRAINT,
//        ICON_CAMERA_DEHLT,
//        ICON_ARMATURE_DEHLT,
//        ICON_SNAP_GEAR,
//        ICON_SNAP_GEO,
//        ICON_SNAP_NORMAL,
//        ICON_BLANK42,
//        ICON_SMOOTHCURVE,
//        ICON_SPHERECURVE,
//        ICON_ROOTCURVE,
//        ICON_SHARPCURVE,
//        ICON_LINCURVE,
//        ICON_NOCURVE,
//        ICON_RNDCURVE,
//        ICON_PROP_OFF,
//        ICON_PROP_ON,
//        ICON_PROP_CON,
//        ICON_SYNTAX,
//        ICON_SYNTAX_OFF,
//        ICON_MONKEY,
//        ICON_HAIR,
//        ICON_VIEWMOVE,
//        ICON_HOME,
//        ICON_CLIPUV_DEHLT,
//        ICON_CLIPUV_HLT,
//        ICON_BLANK2,
//        ICON_BLANK3,
//        ICON_VPAINT_COL,
//        ICON_RESTRICT_SELECT_OFF,
//        ICON_RESTRICT_SELECT_ON,
//        ICON_MUTE_IPO_OFF,
//        ICON_MUTE_IPO_ON,
//        ICON_MAN_TRANS,
//        ICON_MAN_ROT,
//        ICON_MAN_SCALE,
//        ICON_MANIPUL,
//        ICON_BLANK_47,
//        ICON_MODIFIER,
//        ICON_MOD_WAVE,
//        ICON_MOD_BUILD,
//        ICON_MOD_DECIM,
//        ICON_MOD_MIRROR,
//        ICON_MOD_SOFT,
//        ICON_MOD_SUBSURF,
//        ICON_SEQ_SEQUENCER,
//        ICON_SEQ_PREVIEW,
//        ICON_SEQ_LUMA_WAVEFORM,
//        ICON_SEQ_CHROMA_SCOPE,
//        ICON_ROTATE,
//        ICON_CURSOR,
//        ICON_ROTATECOLLECTION,
//        ICON_ROTATECENTER,
//        ICON_ROTACTIVE,
//        ICON_RESTRICT_VIEW_OFF,
//        ICON_RESTRICT_VIEW_ON,
//        ICON_RESTRICT_RENDER_OFF,
//        ICON_RESTRICT_RENDER_ON,
//        VICON_VIEW3D,
//        VICON_EDIT,
//        VICON_EDITMODE_DEHLT,
//        VICON_EDITMODE_HLT,
//        VICON_DISCLOSURE_TRI_RIGHT,
//        VICON_DISCLOSURE_TRI_DOWN,
//        VICON_MOVE_UP,
//        VICON_MOVE_DOWN,
//        VICON_X
//    };

//    public static final int BIFICONID_FIRST = BIFIconID.ICON_VIEW3D.ordinal();
//    public static final int BIFICONID_LAST = BIFIconID.VICON_X.ordinal();
//    public static final int BIFNICONIDS = BIFICONID_LAST - BIFICONID_FIRST + 1;

    public static enum BIFColorShade {
        COLORSHADE_DARK,
        COLORSHADE_GREY,
        COLORSHADE_MEDIUM,
        COLORSHADE_HILITE,
        COLORSHADE_LIGHT,
        COLORSHADE_WHITE
    };

    public static final int BIFCOLORSHADE_FIRST = BIFColorShade.COLORSHADE_DARK.ordinal();
    public static final int BIFCOLORSHADE_LAST = BIFColorShade.COLORSHADE_WHITE.ordinal();
    public static final int BIFNCOLORSHADES = BIFCOLORSHADE_LAST - BIFCOLORSHADE_FIRST + 1;

    public static enum BIFColorID {
        BUTGREY,
        BUTGREEN,
        BUTBLUE,
        BUTSALMON,
        MIDGREY,
        BUTPURPLE,
        BUTYELLOW,
        REDALERT,
        BUTRUST,
        BUTWHITE,
        BUTDBLUE,
        BUTPINK,
        BUTDPINK,
        BUTMACTIVE,
        BUTIPO,
        BUTAUDIO,
        BUTCAMERA,
        BUTRANDOM,
        BUTEDITOBJECT,
        BUTPROPERTY,
        BUTSCENE,
        BUTMOTION,
        BUTMESSAGE,
        BUTACTION,
        BUTCD,
        BUTGAME,
        BUTVISIBILITY,
        BUTYUCK,
        BUTSEASICK,
        BUTCHOKE,
        BUTIMPERIAL,
        BUTTEXTCOLOR,
        BUTTEXTPRESSED,
        BUTSBACKGROUND,
        VIEWPORTBACKCOLOR,
        VIEWPORTGRIDCOLOR,
        VIEWPORTACTIVECOLOR,
        VIEWPORTSELECTEDCOLOR,
        VIEWPORTUNSELCOLOR,
        EDITVERTSEL,
        EDITVERTUNSEL,
        EDITEDGESEL,
        EDITEDGEUNSEL
    };

    public static final int BIFCOLORID_FIRST = BIFColorID.BUTGREY.ordinal();
    public static final int BIFCOLORID_LAST = BIFColorID.EDITEDGEUNSEL.ordinal();
    public static final int BIFNCOLORIDS = BIFCOLORID_LAST - BIFCOLORID_FIRST + 1;

///* XXX WARNING: this is saved in file, so do not change order! */
//public static enum TH {
//	TH_AUTO,	/* for buttons, to signal automatic color assignment */
//
//// uibutton colors
//	TH_BUT_OUTLINE,
//	TH_BUT_NEUTRAL,
//	TH_BUT_ACTION,
//	TH_BUT_SETTING,
//	TH_BUT_SETTING1,
//	TH_BUT_SETTING2,
//	TH_BUT_NUM,
//	TH_BUT_TEXTFIELD,
//	TH_BUT_POPUP,
//	TH_BUT_TEXT,
//	TH_BUT_TEXT_HI,
//	TH_MENU_BACK,
//	TH_MENU_ITEM,
//	TH_MENU_HILITE,
//	TH_MENU_TEXT,
//	TH_MENU_TEXT_HI,
//
//	TH_BUT_DRAWTYPE,
//
//	TH_REDALERT,
//	TH_CUSTOM,
//
//	TH_BUT_TEXTFIELD_HI,
//	TH_ICONFILE,
//
//	TH_THEMEUI,
//// common colors among spaces
//
//	TH_BACK,
//	TH_TEXT,
//	TH_TEXT_HI,
//	TH_HEADER,
//	TH_HEADERDESEL,
//	TH_PANEL,
//	TH_SHADE1,
//	TH_SHADE2,
//	TH_HILITE,
//
//	TH_GRID,
//	TH_WIRE,
//	TH_SELECT,
//	TH_ACTIVE,
//	TH_GROUP,
//	TH_GROUP_ACTIVE,
//	TH_TRANSFORM,
//	TH_VERTEX,
//	TH_VERTEX_SELECT,
//	TH_VERTEX_SIZE,
//	TH_EDGE,
//	TH_EDGE_SELECT,
//	TH_EDGE_SEAM,
//	TH_EDGE_FACESEL,
//	TH_FACE,
//	TH_FACE_SELECT,
//	TH_NORMAL,
//	TH_FACE_DOT,
//	TH_FACEDOT_SIZE,
//	TH_CFRAME,
//
//	TH_SYNTAX_B,
//	TH_SYNTAX_V,
//	TH_SYNTAX_C,
//	TH_SYNTAX_L,
//	TH_SYNTAX_N,
//
//	TH_BONE_SOLID,
//	TH_BONE_POSE,
//
//	TH_STRIP,
//	TH_STRIP_SELECT,
//
//	TH_LAMP,
//
//	TH_NODE,
//	TH_NODE_IN_OUT,
//	TH_NODE_OPERATOR,
//	TH_NODE_CONVERTOR,
//	TH_NODE_GROUP,
//
//	TH_SEQ_MOVIE,
//	TH_SEQ_IMAGE,
//	TH_SEQ_SCENE,
//	TH_SEQ_AUDIO,
//	TH_SEQ_EFFECT,
//	TH_SEQ_PLUGIN,
//	TH_SEQ_TRANSITION,
//	TH_SEQ_META,
//
//	TH_EDGE_SHARP,
//	TH_EDITMESH_ACTIVE,
//
//	TH_HANDLE_VERTEX,
//	TH_HANDLE_VERTEX_SELECT,
//	TH_HANDLE_VERTEX_SIZE,
//};
///* XXX WARNING: previous is saved in file, so do not change order! */

    /* theme drawtypes */
    public static final int TH_MINIMAL = 0;
    public static final int TH_SHADED = 1;
    public static final int TH_ROUNDED = 2;
    public static final int TH_OLDSKOOL = 3;

    /* specific defines per space should have higher define values */

        public static final int TH_REDALERT=0;

	public static final int TH_THEMEUI=1;
// common colors among spaces

	public static final int TH_BACK=2;
	public static final int TH_TEXT=3;
	public static final int TH_TEXT_HI=4;
	public static final int TH_TITLE=5;

	public static final int TH_HEADER=6;
	public static final int TH_HEADERDESEL=7;
	public static final int TH_HEADER_TEXT=8;
	public static final int TH_HEADER_TEXT_HI=9;

	/* float panels */
	public static final int TH_PANEL=10;
	public static final int TH_PANEL_TEXT=11;
	public static final int TH_PANEL_TEXT_HI=12;

	public static final int TH_BUTBACK=13;
	public static final int TH_BUTBACK_TEXT=14;
	public static final int TH_BUTBACK_TEXT_HI=15;

	public static final int TH_SHADE1=16;
	public static final int TH_SHADE2=17;
	public static final int TH_HILITE=18;

	public static final int TH_GRID=19;
	public static final int TH_WIRE=20;
	public static final int TH_SELECT=21;
	public static final int TH_ACTIVE=22;
	public static final int TH_GROUP=23;
	public static final int TH_GROUP_ACTIVE=24;
	public static final int TH_TRANSFORM=25;
	public static final int TH_VERTEX=26;
	public static final int TH_VERTEX_SELECT=27;
	public static final int TH_VERTEX_SIZE=28;
	public static final int TH_EDGE=29;
	public static final int TH_EDGE_SELECT=30;
	public static final int TH_EDGE_SEAM=31;
	public static final int TH_EDGE_FACESEL=32;
	public static final int TH_FACE=33;
	public static final int TH_FACE_SELECT=34;
	public static final int TH_NORMAL=35;
	public static final int TH_FACE_DOT=36;
	public static final int TH_FACEDOT_SIZE=37;
	public static final int TH_CFRAME=38;

	public static final int TH_SYNTAX_B=39;
	public static final int TH_SYNTAX_V=40;
	public static final int TH_SYNTAX_C=41;
	public static final int TH_SYNTAX_L=42;
	public static final int TH_SYNTAX_N=43;

	public static final int TH_BONE_SOLID=44;
	public static final int TH_BONE_POSE=45;

	public static final int TH_STRIP=46;
	public static final int TH_STRIP_SELECT=47;

	public static final int TH_LAMP=48;

	public static final int TH_NODE=49;
	public static final int TH_NODE_IN_OUT=50;
	public static final int TH_NODE_OPERATOR=51;
	public static final int TH_NODE_CONVERTOR=52;
	public static final int TH_NODE_GROUP=53;

	public static final int TH_SEQ_MOVIE=54;
	public static final int TH_SEQ_IMAGE=55;
	public static final int TH_SEQ_SCENE=56;
	public static final int TH_SEQ_AUDIO=57;
	public static final int TH_SEQ_EFFECT=58;
	public static final int TH_SEQ_PLUGIN=59;
	public static final int TH_SEQ_TRANSITION=60;
	public static final int TH_SEQ_META=61;

	public static final int TH_EDGE_SHARP=62;
	public static final int TH_EDITMESH_ACTIVE=63;

	public static final int TH_HANDLE_VERTEX=64;
	public static final int TH_HANDLE_VERTEX_SELECT=65;
	public static final int TH_HANDLE_VERTEX_SIZE=66;

	public static final int TH_DOPESHEET_CHANNELOB=67;
	public static final int TH_DOPESHEET_CHANNELSUBOB=68;
//	public static final int TH_PREVIEW_BACK=69;

    public static bTheme theme_active = null;
    public static int theme_spacetype = SpaceTypes.SPACE_VIEW3D;
    public static int theme_regionid= ScreenTypes.RGN_TYPE_WINDOW;

    public static void ui_resources_init() {
        UIIcons.UI_icons_init(BIFIconID.BIFICONID_LAST.ordinal(), ResourceAnchor.class.getClassLoader().getResource("resources/icons/blenderbuttons.png"));
    }

//void ui_resources_free(void)
//{
//	UI_icons_free();
//}


    /* ******************************************************** */
    /*    THEMES */
    /* ******************************************************** */

    private static byte[] error = {(byte) 240, (byte) 0, (byte) 240, (byte) 255};
    private static byte[] alert = {(byte) 240, (byte) 60, (byte) 60, (byte) 255};
    private static byte[] headerdesel = {(byte) 0, (byte) 0, (byte) 0, (byte) 255};

public static byte[] UI_ThemeGetColorPtr(bTheme btheme, int spacetype, int colorid)
{
	ThemeSpace ts= null;
	
	byte[] cp= error;
	
	if(btheme!=null) {
	
		// first check for ui buttons theme
		if(colorid < TH_THEMEUI) {
		
			switch(colorid) {

			case TH_REDALERT:
				cp= alert; break;
			}
		}
		else {
		
			switch(spacetype) {
			case SpaceTypes.SPACE_BUTS:
				ts= btheme.tbuts;
				break;
			case SpaceTypes.SPACE_VIEW3D:
				ts= btheme.tv3d;
				break;
			case SpaceTypes.SPACE_IPO:
				ts= btheme.tipo;
				break;
			case SpaceTypes.SPACE_FILE:
				ts= btheme.tfile;
				break;
			case SpaceTypes.SPACE_NLA:
				ts= btheme.tnla;
				break;
			case SpaceTypes.SPACE_ACTION:
				ts= btheme.tact;
				break;
			case SpaceTypes.SPACE_SEQ:
				ts= btheme.tseq;
				break;
			case SpaceTypes.SPACE_IMAGE:
				ts= btheme.tima;
				break;
//			case SpaceTypes.SPACE_IMASEL:
//				ts= btheme.timasel;
//				break;
			case SpaceTypes.SPACE_TEXT:
				ts= btheme.text;
				break;
			case SpaceTypes.SPACE_OUTLINER:
				ts= btheme.toops;
				break;
//			case SpaceTypes.SPACE_SOUND:
//				ts= btheme.tsnd;
//				break;
			case SpaceTypes.SPACE_INFO:
				ts= btheme.tinfo;
				break;
			case SpaceTypes.SPACE_TIME:
				ts= btheme.ttime;
				break;
			case SpaceTypes.SPACE_NODE:
				ts= btheme.tnode;
				break;
			case SpaceTypes.SPACE_LOGIC:
				ts= btheme.tlogic;
				break;
			default:
				ts= btheme.tv3d;
				break;
			}
			
			switch(colorid) {
			case TH_BACK:
				if(theme_regionid==ScreenTypes.RGN_TYPE_WINDOW)
					cp= ts.back;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_CHANNELS)
					cp= ts.list;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_HEADER)
					cp= ts.header;
				else
					cp= ts.button;
				break;
			case TH_TEXT:
				if(theme_regionid==ScreenTypes.RGN_TYPE_WINDOW)
					cp= ts.text;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_CHANNELS)
					cp= ts.list_text;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_HEADER)
					cp= ts.header_text;
				else
					cp= ts.button_text;
				break;
			case TH_TEXT_HI:
				if(theme_regionid==ScreenTypes.RGN_TYPE_WINDOW)
					cp= ts.text_hi;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_CHANNELS)
					cp= ts.list_text_hi;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_HEADER)
					cp= ts.header_text_hi;
				else
					cp= ts.button_text_hi;
				break;
			case TH_TITLE:
				if(theme_regionid==ScreenTypes.RGN_TYPE_WINDOW)
					cp= ts.title;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_CHANNELS)
					cp= ts.list_title;
				else if(theme_regionid==ScreenTypes.RGN_TYPE_HEADER)
					cp= ts.header_title;
				else
					cp= ts.button_title;
				break;
				
			case TH_HEADER:
				cp= ts.header; break;
			case TH_HEADERDESEL:
				/* we calculate a dynamic builtin header deselect color, also for pulldowns... */
				cp= ts.header;
				headerdesel[0]= (byte)((cp[0]&0xFF)>10?(cp[0]&0xFF)-10:0);
				headerdesel[1]= (byte)((cp[1]&0xFF)>10?(cp[1]&0xFF)-10:0);
				headerdesel[2]= (byte)((cp[2]&0xFF)>10?(cp[2]&0xFF)-10:0);
				cp= headerdesel;
				break;
			case TH_HEADER_TEXT:
				cp= ts.header_text; break;
			case TH_HEADER_TEXT_HI:
				cp= ts.header_text_hi; break;
				
			case TH_PANEL:
				cp= ts.panel; break;
			case TH_PANEL_TEXT:
				cp= ts.panel_text; break;
			case TH_PANEL_TEXT_HI:
				cp= ts.panel_text_hi; break;
				
			case TH_BUTBACK:
				cp= ts.button; break;
			case TH_BUTBACK_TEXT:
				cp= ts.button_text; break;
			case TH_BUTBACK_TEXT_HI:
				cp= ts.button_text_hi; break;
				
			case TH_SHADE1:
				cp= ts.shade1; break;
			case TH_SHADE2:
				cp= ts.shade2; break;
			case TH_HILITE:
				cp= ts.hilite; break;
				
			case TH_GRID:
				cp= ts.grid; break;
			case TH_WIRE:
				cp= ts.wire; break;
			case TH_LAMP:
				cp= ts.lamp; break;
			case TH_SELECT:
				cp= ts.select; break;
			case TH_ACTIVE:
				cp= ts.active; break;
			case TH_GROUP:
				cp= ts.group; break;
			case TH_GROUP_ACTIVE:
				cp= ts.group_active; break;
			case TH_TRANSFORM:
				cp= ts.transform; break;
			case TH_VERTEX:
				cp= ts.vertex; break;
			case TH_VERTEX_SELECT:
				cp= ts.vertex_select; break;
			case TH_VERTEX_SIZE:
				cp= new byte[]{ts.vertex_size}; break;
			case TH_EDGE:
				cp= ts.edge; break;
			case TH_EDGE_SELECT:
				cp= ts.edge_select; break;
			case TH_EDGE_SEAM:
				cp= ts.edge_seam; break;
			case TH_EDGE_SHARP:
				cp= ts.edge_sharp; break;
			case TH_EDITMESH_ACTIVE:
				cp= ts.editmesh_active; break;
			case TH_EDGE_FACESEL:
				cp= ts.edge_facesel; break;
			case TH_FACE:
				cp= ts.face; break;
			case TH_FACE_SELECT:
				cp= ts.face_select; break;
			case TH_FACE_DOT:
				cp= ts.face_dot; break;
			case TH_FACEDOT_SIZE:
				cp= new byte[]{ts.facedot_size}; break;
			case TH_NORMAL:
				cp= ts.normal; break;
			case TH_BONE_SOLID:
				cp= ts.bone_solid; break;
			case TH_BONE_POSE:
				cp= ts.bone_pose; break;
			case TH_STRIP:
				cp= ts.strip; break;
			case TH_STRIP_SELECT:
				cp= ts.strip_select; break;
			case TH_CFRAME:
				cp= ts.cframe; break;
				
			case TH_SYNTAX_B:
				cp= ts.syntaxb; break;
			case TH_SYNTAX_V:
				cp= ts.syntaxv; break;
			case TH_SYNTAX_C:
				cp= ts.syntaxc; break;
			case TH_SYNTAX_L:
				cp= ts.syntaxl; break;
			case TH_SYNTAX_N:
				cp= ts.syntaxn; break;

			case TH_NODE:
				cp= ts.syntaxl; break;
			case TH_NODE_IN_OUT:
				cp= ts.syntaxn; break;
			case TH_NODE_OPERATOR:
				cp= ts.syntaxb; break;
			case TH_NODE_CONVERTOR:
				cp= ts.syntaxv; break;
			case TH_NODE_GROUP:
				cp= ts.syntaxc; break;
				
			case TH_SEQ_MOVIE:
				cp= ts.movie; break;
			case TH_SEQ_IMAGE:
				cp= ts.image; break;
			case TH_SEQ_SCENE:
				cp= ts.scene; break;
			case TH_SEQ_AUDIO:
				cp= ts.audio; break;
			case TH_SEQ_EFFECT:
				cp= ts.effect; break;
			case TH_SEQ_PLUGIN:
				cp= ts.plugin; break;
			case TH_SEQ_TRANSITION:
				cp= ts.transition; break;
			case TH_SEQ_META:
				cp= ts.meta; break;
				
			case TH_HANDLE_VERTEX:
				cp= ts.handle_vertex;
				break;
			case TH_HANDLE_VERTEX_SELECT:
				cp= ts.handle_vertex_select;
				break;
			case TH_HANDLE_VERTEX_SIZE:
				cp= new byte[]{ts.handle_vertex_size};
				break;
				
			case TH_DOPESHEET_CHANNELOB:
				cp= ts.ds_channel;
				break;
			case TH_DOPESHEET_CHANNELSUBOB:
				cp= ts.ds_subchannel;
				break;	
//			case TH_PREVIEW_BACK:
//				cp= ts.preview_back;
//				break;	
			}
		}
	}
	
	return cp;
}

    public static final void SETCOLTEST(byte[] col, int r, int g, int b, int a) {
        if(col[3]==0) {
            col[0]= (byte)r;
            col[1]= (byte)g;
            col[2]= (byte)b;
            col[3]= (byte)a;
        }
    }

    /* use this call to init new variables in themespace, if they're same for all */
    public static void ui_theme_init_new_do(ThemeSpace ts) {
        SETCOLTEST(ts.header_text, 0, 0, 0, 255);
        SETCOLTEST(ts.header_title, 0, 0, 0, 255);
        SETCOLTEST(ts.header_text_hi, 255, 255, 255, 255);

        SETCOLTEST(ts.panel_text, 0, 0, 0, 255);
        SETCOLTEST(ts.panel_title, 0, 0, 0, 255);
        SETCOLTEST(ts.panel_text_hi, 255, 255, 255, 255);

        SETCOLTEST(ts.button, 145, 145, 145, 245);
        SETCOLTEST(ts.button_title, 0, 0, 0, 255);
        SETCOLTEST(ts.button_text, 0, 0, 0, 255);
        SETCOLTEST(ts.button_text_hi, 255, 255, 255, 255);

        SETCOLTEST(ts.list, 165, 165, 165, 255);
        SETCOLTEST(ts.list_title, 0, 0, 0, 255);
        SETCOLTEST(ts.list_text, 0, 0, 0, 255);
        SETCOLTEST(ts.list_text_hi, 255, 255, 255, 255);
    }

    public static void ui_theme_init_new(bTheme btheme) {
        ui_theme_init_new_do(btheme.tbuts);
        ui_theme_init_new_do(btheme.tv3d);
        ui_theme_init_new_do(btheme.tfile);
        ui_theme_init_new_do(btheme.tipo);
        ui_theme_init_new_do(btheme.tinfo);
        //ui_theme_init_new_do(btheme.tsnd);
        ui_theme_init_new_do(btheme.tact);
        ui_theme_init_new_do(btheme.tnla);
        ui_theme_init_new_do(btheme.tseq);
        ui_theme_init_new_do(btheme.tima);
        //ui_theme_init_new_do(btheme.timasel);
        ui_theme_init_new_do(btheme.text);
        ui_theme_init_new_do(btheme.toops);
        ui_theme_init_new_do(btheme.ttime);
        ui_theme_init_new_do(btheme.tnode);
        ui_theme_init_new_do(btheme.tlogic);
    }

    public static final void SETCOL(byte[] col, int r, int g, int b, int a) {
        col[0] = (byte) r;
        col[1] = (byte) g;
        col[2] = (byte) b;
        col[3] = (byte) a;
    }

    public static final void SETCOLF(byte[] col, float r, float g, float b, float a) {
        col[0] = (byte) (r*255);
        col[1] = (byte) (g*255);
        col[2] = (byte) (b*255);
        col[3] = (byte) (a*255);
    }

/* initialize default theme, can't be edited
   Note: when you add new colors, created & saved themes need initialized
   use function below, init_userdef_do_versions()
*/
public static void ui_theme_init_userdef()
{
	bTheme btheme= (bTheme)U.themes.first;

	/* we search for the theme with name Default */
	for(btheme= (bTheme)U.themes.first; btheme!=null; btheme= btheme.next) {
		if (StringUtil.strcmp(StringUtil.toCString("Default"), 0, btheme.name, 0) == 0) {
                break;
            }
	}

	if(btheme==null) {
		btheme = new bTheme();
		ListBaseUtil.BLI_addtail(U.themes, btheme);
		StringUtil.strcpy(btheme.name, 0, StringUtil.toCString("Default"), 0);
	}

	UI_SetTheme(0, 0);	// make sure the global used in this file is set

	/* UI buttons */
//	ui_widget_color_init(btheme.tui);

	/* common (new) variables */
	ui_theme_init_new(btheme);

	/* space view3d */
	SETCOLF(btheme.tv3d.back,       0.225f, 0.225f, 0.225f, 1.0f);
	SETCOL(btheme.tv3d.text,       0, 0, 0, 255);
	SETCOL(btheme.tv3d.text_hi, 255, 255, 255, 255);

	SETCOLF(btheme.tv3d.header,	0.45f, 0.45f, 0.45f, 1.0f);
	SETCOL(btheme.tv3d.panel,      165, 165, 165, 127);

	SETCOL(btheme.tv3d.shade1,  160, 160, 160, 100);
	SETCOL(btheme.tv3d.shade2,  0x7f, 0x70, 0x70, 100);

	SETCOLF(btheme.tv3d.grid,     0.251f, 0.251f, 0.251f, 1.0f);
	SETCOL(btheme.tv3d.wire,       0x0, 0x0, 0x0, 255);
	SETCOL(btheme.tv3d.lamp,       0, 0, 0, 40);
	SETCOL(btheme.tv3d.select, 241, 88, 0, 255);
	SETCOL(btheme.tv3d.active, 255, 140, 25, 255);
	SETCOL(btheme.tv3d.group,      16, 64, 16, 255);
	SETCOL(btheme.tv3d.group_active, 85, 187, 85, 255);
	SETCOL(btheme.tv3d.transform, 0xff, 0xff, 0xff, 255);
	SETCOL(btheme.tv3d.vertex, 0, 0, 0, 255);
	SETCOL(btheme.tv3d.vertex_select, 255, 133, 0, 255);
	btheme.tv3d.vertex_size= 3;
	SETCOL(btheme.tv3d.edge,       0x0, 0x0, 0x0, 255);
	SETCOL(btheme.tv3d.edge_select, 255, 160, 0, 255);
	SETCOL(btheme.tv3d.edge_seam, 219, 37, 18, 255);
	SETCOL(btheme.tv3d.edge_facesel, 75, 75, 75, 255);
	SETCOL(btheme.tv3d.face,       0, 0, 0, 18);
	SETCOL(btheme.tv3d.face_select, 255, 133, 0, 60);
	SETCOL(btheme.tv3d.normal, 0x22, 0xDD, 0xDD, 255);
	SETCOL(btheme.tv3d.face_dot, 255, 133, 0, 255);
	btheme.tv3d.facedot_size= 4;
	SETCOL(btheme.tv3d.cframe, 0x60, 0xc0,	 0x40, 255);

	SETCOL(btheme.tv3d.bone_solid, 200, 200, 200, 255);
	SETCOL(btheme.tv3d.bone_pose, 80, 200, 255, 80);               // alpha 80 is not meant editable, used for wire+action draw


	/* space buttons */
	/* to have something initialized */
	btheme.tbuts= btheme.tv3d;

	SETCOLF(btheme.tbuts.back, 	0.45f, 0.45f, 0.45f, 1.0f);
	SETCOL(btheme.tbuts.panel, 0x82, 0x82, 0x82, 255);

	/* graph editor */
	btheme.tipo= btheme.tv3d;
	SETCOLF(btheme.tipo.back, 	0.42f, 0.42f, 0.42f, 1.0f);
	SETCOLF(btheme.tipo.list, 	0.4f, 0.4f, 0.4f, 1.0f);
	SETCOL(btheme.tipo.grid, 	94, 94, 94, 255);
	SETCOL(btheme.tipo.panel,  255, 255, 255, 150);
	SETCOL(btheme.tipo.shade1,		150, 150, 150, 100);	/* scrollbars */
	SETCOL(btheme.tipo.shade2,		0x70, 0x70, 0x70, 100);
	SETCOL(btheme.tipo.vertex,		0, 0, 0, 255);
	SETCOL(btheme.tipo.vertex_select, 255, 133, 0, 255);
	SETCOL(btheme.tipo.hilite, 0x60, 0xc0, 0x40, 255);
	btheme.tipo.vertex_size= 3;

	SETCOL(btheme.tipo.handle_vertex, 		0, 0, 0, 255);
	SETCOL(btheme.tipo.handle_vertex_select, 255, 133, 0, 255);
	btheme.tipo.handle_vertex_size= 3;

	SETCOL(btheme.tipo.ds_channel, 	82, 96, 110, 255);
	SETCOL(btheme.tipo.ds_subchannel,	124, 137, 150, 255);
	SETCOL(btheme.tipo.group, 79, 101, 73, 255);
	SETCOL(btheme.tipo.group_active, 135, 177, 125, 255);

	/* dopesheet */
	btheme.tact= btheme.tipo;
	SETCOL(btheme.tact.strip, 			12, 10, 10, 128);
	SETCOL(btheme.tact.strip_select, 	255, 140, 0, 255);

	/* space nla */
	btheme.tnla= btheme.tact;

	/* space file */
	/* to have something initialized */
	btheme.tfile= btheme.tv3d;
	SETCOL(btheme.tfile.back, 	90, 90, 90, 255);
	SETCOL(btheme.tfile.text, 	250, 250, 250, 255);
	SETCOL(btheme.tfile.text_hi, 15, 15, 15, 255);
	SETCOL(btheme.tfile.panel, 180, 180, 180, 255);	// bookmark/ui regions
	SETCOL(btheme.tfile.active, 130, 130, 130, 255); // selected files
	SETCOL(btheme.tfile.hilite, 255, 140, 25, 255); // selected files

	SETCOL(btheme.tfile.grid,	250, 250, 250, 255);
	SETCOL(btheme.tfile.image,	250, 250, 250, 255);
	SETCOL(btheme.tfile.movie,	250, 250, 250, 255);
	SETCOL(btheme.tfile.scene,	250, 250, 250, 255);


	/* space seq */
	btheme.tseq= btheme.tv3d;
	SETCOL(btheme.tseq.back, 	116, 116, 116, 255);
	SETCOL(btheme.tseq.movie, 	81, 105, 135, 255);
	SETCOL(btheme.tseq.image, 	109, 88, 129, 255);
	SETCOL(btheme.tseq.scene, 	78, 152, 62, 255);
	SETCOL(btheme.tseq.audio, 	46, 143, 143, 255);
	SETCOL(btheme.tseq.effect, 	169, 84, 124, 255);
	SETCOL(btheme.tseq.plugin, 	126, 126, 80, 255);
	SETCOL(btheme.tseq.transition, 162, 95, 111, 255);
	SETCOL(btheme.tseq.meta, 	109, 145, 131, 255);


	/* space image */
	btheme.tima= btheme.tv3d;
	SETCOL(btheme.tima.back, 	53, 53, 53, 255);
	SETCOL(btheme.tima.vertex, 0, 0, 0, 255);
	SETCOL(btheme.tima.vertex_select, 255, 133, 0, 255);
	btheme.tima.vertex_size= 3;
	btheme.tima.facedot_size= 3;
	SETCOL(btheme.tima.face,   255, 255, 255, 10);
	SETCOL(btheme.tima.face_select, 255, 133, 0, 60);
	SETCOL(btheme.tima.editmesh_active, 255, 255, 255, 128);

//	/* space imageselect */
//	btheme.timasel= btheme.tv3d;
//	SETCOL(btheme.timasel.active, 	195, 195, 195, 255); /* active tile */
//	SETCOL(btheme.timasel.grid,  94, 94, 94, 255); /* active file text */
//	SETCOL(btheme.timasel.back, 	110, 110, 110, 255);
//	SETCOL(btheme.timasel.shade1,  94, 94, 94, 255);	/* bar */
//	SETCOL(btheme.timasel.shade2,  172, 172, 172, 255); /* sliders */
//	SETCOL(btheme.timasel.hilite,  17, 27, 60, 100);	/* selected tile */
//	SETCOL(btheme.timasel.text, 	0, 0, 0, 255);
//	SETCOL(btheme.timasel.text_hi, 255, 255, 255, 255);
//	SETCOL(btheme.timasel.panel, 	132, 132, 132, 255);

	/* space text */
	btheme.text= btheme.tv3d;
	SETCOL(btheme.text.back, 	153, 153, 153, 255);
	SETCOL(btheme.text.shade1, 	143, 143, 143, 255);
	SETCOL(btheme.text.shade2, 	0xc6, 0x77, 0x77, 255);
	SETCOL(btheme.text.hilite, 	255, 0, 0, 255);

	/* syntax highlighting */
	SETCOL(btheme.text.syntaxn,	0, 0, 200, 255);	/* Numbers  Blue*/
	SETCOL(btheme.text.syntaxl,	100, 0, 0, 255);	/* Strings  red */
	SETCOL(btheme.text.syntaxc,	0, 100, 50, 255);	/* Comments greenish */
	SETCOL(btheme.text.syntaxv,	95, 95, 0, 255);	/* Special */
	SETCOL(btheme.text.syntaxb,	128, 0, 80, 255);	/* Builtin, red-purple */

	/* space oops */
	btheme.toops= btheme.tv3d;
	SETCOLF(btheme.toops.back, 	0.45f, 0.45f, 0.45f, 1.0f);

	/* space info */
	btheme.tinfo= btheme.tv3d;
	SETCOLF(btheme.tinfo.back, 	0.45f, 0.45f, 0.45f, 1.0f);

//	/* space sound */
//	btheme.tsnd= btheme.tv3d;
//	SETCOLF(btheme.tsnd.back, 	0.45f, 0.45f, 0.45f, 1.0f);
//	SETCOLF(btheme.tsnd.grid, 	0.36f, 0.36f, 0.36f, 1.0f);
//	SETCOL(btheme.tsnd.shade1,  173, 173, 173, 255);		// sliders

//	/* space time */
//	btheme.ttime= btheme.tsnd;	// same as sound space

	/* space node, re-uses syntax color storage */
	btheme.tnode= btheme.tv3d;
	SETCOL(btheme.tnode.edge_select, 255, 255, 255, 255);
	SETCOL(btheme.tnode.syntaxl, 150, 150, 150, 255);	/* TH_NODE, backdrop */
	SETCOL(btheme.tnode.syntaxn, 129, 131, 144, 255);	/* in/output */
	SETCOL(btheme.tnode.syntaxb, 127,127,127, 255);	/* operator */
	SETCOL(btheme.tnode.syntaxv, 142, 138, 145, 255);	/* generator */
	SETCOL(btheme.tnode.syntaxc, 120, 145, 120, 255);	/* group */

	/* space logic */
	btheme.tlogic= btheme.tv3d;
	SETCOL(btheme.tlogic.back, 100, 100, 100, 255);
}


public static void UI_SetTheme(int spacetype, int regionid)
{
	if(spacetype==0) {	// called for safety, when delete themes
		theme_active= (bTheme)U.themes.first;
		theme_spacetype= SpaceTypes.SPACE_VIEW3D;
		theme_regionid= ScreenTypes.RGN_TYPE_WINDOW;
	}
	else {
		// later on, a local theme can be found too
		theme_active= (bTheme)U.themes.first;
		theme_spacetype= spacetype;
		theme_regionid= regionid;
	}
}

    // for space windows only
    public static void UI_ThemeColor(GL2 gl, int colorid) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        gl.glColor3ub(cp[0], cp[1], cp[2]);

    }

    // plus alpha
    public static void UI_ThemeColor4(GL2 gl, int colorid) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        gl.glColor4ub(cp[0], cp[1], cp[2], cp[3]);

    }

    public static void UI_ThemeColorShade(GL2 gl, int colorid, int offset) {
        int r, g, b;
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        r = offset + (cp[0] & 0xFF);
        r = Arithb.CLAMP(r, 0, 255);
        g = offset + (cp[1] & 0xFF);
        g = Arithb.CLAMP(g, 0, 255);
        b = offset + (cp[2] & 0xFF);
        b = Arithb.CLAMP(b, 0, 255);
        gl.glColor4ub((byte) r, (byte) g, (byte) b, cp[3]);
    }

    public static void UI_ThemeColorShadeAlpha(GL2 gl, int colorid, int coloffset, int alphaoffset) {
        int r, g, b, a;
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        r = coloffset + (cp[0] & 0xFF);
        r = Arithb.CLAMP(r, 0, 255);
        g = coloffset + (cp[1] & 0xFF);
        g = Arithb.CLAMP(g, 0, 255);
        b = coloffset + (cp[2] & 0xFF);
        b = Arithb.CLAMP(b, 0, 255);
        a = alphaoffset + (cp[3] & 0xFF);
        a = Arithb.CLAMP(a, 0, 255);
        gl.glColor4ub((byte) r, (byte) g, (byte) b, (byte) a);
    }

    // blend between to theme colors, and set it
    public static void UI_ThemeColorBlend(GL2 gl, int colorid1, int colorid2, float fac) {
        int r, g, b;
        byte[] cp1 = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid1);
        byte[] cp2 = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid2);
        if (fac < 0.0f) {
            fac = 0.0f;
        } else if (fac > 1.0f) {
            fac = 1.0f;
        }
        r = (int) (StrictMath.floor((1.0 - fac) * (cp1[0] & 0xFF) + fac * (cp2[0] & 0xFF)));
        g = (int) (StrictMath.floor((1.0 - fac) * (cp1[1] & 0xFF) + fac * (cp2[1] & 0xFF)));
        b = (int) (StrictMath.floor((1.0 - fac) * (cp1[2] & 0xFF) + fac * (cp2[2] & 0xFF)));
        gl.glColor3ub((byte) r, (byte) g, (byte) b);
    }

// blend between to theme colors, shade it, and set it
    public static void UI_ThemeColorBlendShade(GL2 gl, int colorid1, int colorid2, float fac, int offset) {
        int r, g, b;
        byte[] cp1 = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid1);
        byte[] cp2 = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid2);
        if (fac < 0.0f) {
            fac = 0.0f;
        } else if (fac > 1.0f) {
            fac = 1.0f;
        }
        r = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[0] & 0xFF) + fac * (cp2[0] & 0xFF)));
        g = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[1] & 0xFF) + fac * (cp2[1] & 0xFF)));
        b = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[2] & 0xFF) + fac * (cp2[2] & 0xFF)));
        r = Arithb.CLAMP(r, 0, 255);
        g = Arithb.CLAMP(g, 0, 255);
        b = Arithb.CLAMP(b, 0, 255);
        gl.glColor3ub((byte) r, (byte) g, (byte) b);
    }

//// blend between to theme colors, shade it, and set it
//void UI_ThemeColorBlendShadeAlpha(int colorid1, int colorid2, float fac, int offset, int alphaoffset)
//{
//	int r, g, b, a;
//	char *cp1, *cp2;
//
//	cp1= UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid1);
//	cp2= UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid2);
//
//	if(fac<0.0) fac=0.0; else if(fac>1.0) fac= 1.0;
//	r= offset+floor((1.0-fac)*cp1[0] + fac*cp2[0]);
//	g= offset+floor((1.0-fac)*cp1[1] + fac*cp2[1]);
//	b= offset+floor((1.0-fac)*cp1[2] + fac*cp2[2]);
//	a= alphaoffset+floor((1.0-fac)*cp1[3] + fac*cp2[3]);
//
//	CLAMP(r, 0, 255);
//	CLAMP(g, 0, 255);
//	CLAMP(b, 0, 255);
//	CLAMP(a, 0, 255);
//
//	glColor4ub(r, g, b, a);
//}

    // get individual values, not scaled
    public static float UI_GetThemeValuef(int colorid) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        return (float) (cp[0] & 0xFF);

    }

    // get individual values, not scaled
    public static int UI_GetThemeValue(int colorid) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        return cp[0] & 0xFF;

    }

    // get the color, range 0.0-1.0
    public static void UI_GetThemeColor3fv(int colorid, float[] col) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        col[0] = ((float) (cp[0] & 0xFF)) / 255.0f;
        col[1] = ((float) (cp[1] & 0xFF)) / 255.0f;
        col[2] = ((float) (cp[2] & 0xFF)) / 255.0f;
    }

    // get the color, in char pointer
    public static void UI_GetThemeColor3ubv(int colorid, byte[] col) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        col[0] = cp[0];
        col[1] = cp[1];
        col[2] = cp[2];
    }

    // get the color, in char pointer
    public static void UI_GetThemeColor4ubv(int colorid, byte[] col) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, theme_spacetype, colorid);
        col[0] = cp[0];
        col[1] = cp[1];
        col[2] = cp[2];
        col[3] = cp[3];
    }

    public static void UI_GetThemeColorType4ubv(int colorid, int spacetype, byte[] col) {
        byte[] cp = UI_ThemeGetColorPtr(theme_active, spacetype, colorid);
        col[0] = cp[0];
        col[1] = cp[1];
        col[2] = cp[2];
        col[3] = cp[3];
    }

    // blends and shades between two char color pointers
    public static void UI_ColorPtrBlendShade3ubv(GL2 gl, byte[] cp1, byte[] cp2, float fac, int offset) {
        int r, g, b;
        if (fac < 0.0f) {
            fac = 0.0f;
        } else if (fac > 1.0f) {
            fac = 1.0f;
        }
        r = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[0] & 0xFF) + fac * (cp2[0] & 0xFF)));
        g = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[1] & 0xFF) + fac * (cp2[1] & 0xFF)));
        b = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[2] & 0xFF) + fac * (cp2[2] & 0xFF)));
        r = r < 0 ? 0 : (r > 255 ? 255 : r);
        g = g < 0 ? 0 : (g > 255 ? 255 : g);
        b = b < 0 ? 0 : (b > 255 ? 255 : b);
        gl.glColor3ub((byte) r, (byte) g, (byte) b);
    }

    // get a 3 byte color, blended and shaded between two other char color pointers
    public static void UI_GetColorPtrBlendShade3ubv(byte[] cp1, byte[] cp2, byte[] col, float fac, int offset) {
        int r, g, b;
        if (fac < 0.0f) {
            fac = 0.0f;
        } else if (fac > 1.0f) {
            fac = 1.0f;
        }
        r = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[0] & 0xFF) + fac * (cp2[0] & 0xFF)));
        g = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[1] & 0xFF) + fac * (cp2[1] & 0xFF)));
        b = (int) (offset + StrictMath.floor((1.0 - fac) * (cp1[2] & 0xFF) + fac * (cp2[2] & 0xFF)));
        r = r < 0 ? 0 : (r > 255 ? 255 : r);
        g = g < 0 ? 0 : (g > 255 ? 255 : g);
        b = b < 0 ? 0 : (b > 255 ? 255 : b);
        col[0] = (byte) r;
        col[1] = (byte) g;
        col[2] = (byte) b;
    }
    
    public static void UI_ThemeClearColor(GL2 gl, int colorid)
    {
    	float[] col=new float[3];
    	
    	UI_GetThemeColor3fv(colorid, col);
    	gl.glClearColor(col[0], col[1], col[2], 0.0f);
    }

public static void UI_make_axis_color(byte[] src_col, byte[] dst_col, char axis)
{
	switch(axis)
	{
		case 'x':
		case 'X':
			dst_col[0]= (byte)((src_col[0]&0xFF)>219?255:(src_col[0]&0xFF)+36);
			dst_col[1]= (byte)((src_col[1]&0xFF)<26?0:(src_col[1]&0xFF)-26);
			dst_col[2]= (byte)((src_col[2]&0xFF)<26?0:(src_col[2]&0xFF)-26);
			break;
		case 'y':
		case 'Y':
			dst_col[0]= (byte)((src_col[0]&0xFF)<46?0:(src_col[0]&0xFF)-36);
			dst_col[1]= (byte)((src_col[1]&0xFF)>189?255:(src_col[1]&0xFF)+66);
			dst_col[2]= (byte)((src_col[2]&0xFF)<46?0:(src_col[2]&0xFF)-36);
			break;
		default:
			dst_col[0]= (byte)((src_col[0]&0xFF)<26?0:(src_col[0]&0xFF)-26);
			dst_col[1]= (byte)((src_col[1]&0xFF)<26?0:(src_col[1]&0xFF)-26);
			dst_col[2]= (byte)((src_col[2]&0xFF)>209?255:(src_col[2]&0xFF)+46);
	}
}

/* ************************************************************* */

/* patching UserDef struct and Themes */
public static void init_userdef_do_versions()
{
//	Main *bmain= G.main;
////	countall();
//	
//	/* the UserDef struct is not corrected with do_versions() .... ugh! */
//	if(U.wheellinescroll == 0) U.wheellinescroll = 3;
//	if(U.menuthreshold1==0) {
//		U.menuthreshold1= 5;
//		U.menuthreshold2= 2;
//	}
//	if(U.tb_leftmouse==0) {
//		U.tb_leftmouse= 5;
//		U.tb_rightmouse= 5;
//	}
//	if(U.mixbufsize==0) U.mixbufsize= 2048;
//	if (strcmp(U.tempdir, "/") == 0) {
//		BLI_where_is_temp(U.tempdir, sizeof(U.tempdir), FALSE);
//	}
//	if (U.autokey_mode == 0) {
//		/* 'add/replace' but not on */
//		U.autokey_mode = 2;
//	}
//	if (U.savetime <= 0) {
//		U.savetime = 1;
//// XXX		error(STRINGIFY(BLENDER_STARTUP_FILE)" is buggy, please consider removing it.\n");
//	}
//	/* transform widget settings */
//	if(U.tw_hotspot==0) {
//		U.tw_hotspot= 14;
//		U.tw_size= 20;			// percentage of window size
//		U.tw_handlesize= 16;	// percentage of widget radius
//	}
//	if(U.pad_rot_angle==0)
//		U.pad_rot_angle= 15;
//	
//	if(U.flag & USER_CUSTOM_RANGE) 
//		vDM_ColorBand_store(&U.coba_weight); /* signal for derivedmesh to use colorband */
//	
//	if (bmain->versionfile <= 191) {
//		strcpy(U.plugtexdir, U.textudir);
//		strcpy(U.sounddir, "/");
//	}
//	
//	/* patch to set Dupli Armature */
//	if (bmain->versionfile < 220) {
//		U.dupflag |= USER_DUP_ARM;
//	}
//	
//	/* added seam, normal color, undo */
//	if (bmain->versionfile <= 234) {
//		bTheme *btheme;
//		
//		U.uiflag |= USER_GLOBALUNDO;
//		if (U.undosteps==0) U.undosteps=32;
//		
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* check for alpha==0 is safe, then color was never set */
//			if(btheme->tv3d.edge_seam[3]==0) {
//				SETCOL(btheme->tv3d.edge_seam, 230, 150, 50, 255);
//			}
//			if(btheme->tv3d.normal[3]==0) {
//				SETCOL(btheme->tv3d.normal, 0x22, 0xDD, 0xDD, 255);
//			}
//			if(btheme->tv3d.vertex_normal[3]==0) {
//				SETCOL(btheme->tv3d.vertex_normal, 0x23, 0x61, 0xDD, 255);
//			}
//			if(btheme->tv3d.face_dot[3]==0) {
//				SETCOL(btheme->tv3d.face_dot, 255, 138, 48, 255);
//				btheme->tv3d.facedot_size= 4;
//			}
//		}
//	}
//	if (bmain->versionfile <= 235) {
//		/* illegal combo... */
//		if (U.flag & USER_LMOUSESELECT) 
//			U.flag &= ~USER_TWOBUTTONMOUSE;
//	}
//	if (bmain->versionfile <= 236) {
//		bTheme *btheme;
//		/* new space type */
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* check for alpha==0 is safe, then color was never set */
//			if(btheme->ttime.back[3]==0) {
//				btheme->ttime = btheme->tsnd;	// copy from sound
//			}
//			if(btheme->text.syntaxn[3]==0) {
//				SETCOL(btheme->text.syntaxn,	0, 0, 200, 255);	/* Numbers  Blue*/
//				SETCOL(btheme->text.syntaxl,	100, 0, 0, 255);	/* Strings  red */
//				SETCOL(btheme->text.syntaxc,	0, 100, 50, 255);	/* Comments greenish */
//				SETCOL(btheme->text.syntaxv,	95, 95, 0, 255);	/* Special */
//				SETCOL(btheme->text.syntaxb,	128, 0, 80, 255);	/* Builtin, red-purple */
//			}
//		}
//	}
//	if (bmain->versionfile <= 237) {
//		bTheme *btheme;
//		/* bone colors */
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* check for alpha==0 is safe, then color was never set */
//			if(btheme->tv3d.bone_solid[3]==0) {
//				SETCOL(btheme->tv3d.bone_solid, 200, 200, 200, 255);
//				SETCOL(btheme->tv3d.bone_pose, 80, 200, 255, 80);
//			}
//		}
//	}
//	if (bmain->versionfile <= 238) {
//		bTheme *btheme;
//		/* bone colors */
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* check for alpha==0 is safe, then color was never set */
//			if(btheme->tnla.strip[3]==0) {
//				SETCOL(btheme->tnla.strip_select, 	0xff, 0xff, 0xaa, 255);
//				SETCOL(btheme->tnla.strip, 0xe4, 0x9c, 0xc6, 255);
//			}
//		}
//	}
//	if (bmain->versionfile <= 239) {
//		bTheme *btheme;
//		
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* Lamp theme, check for alpha==0 is safe, then color was never set */
//			if(btheme->tv3d.lamp[3]==0) {
//				SETCOL(btheme->tv3d.lamp, 	0, 0, 0, 40);
///* TEMPORAL, remove me! (ton) */				
//				U.uiflag |= USER_PLAINMENUS;
//			}
//			
//		}
//		if(U.obcenter_dia==0) U.obcenter_dia= 6;
//	}
//	if (bmain->versionfile <= 241) {
//		bTheme *btheme;
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* Node editor theme, check for alpha==0 is safe, then color was never set */
//			if(btheme->tnode.syntaxn[3]==0) {
//				/* re-uses syntax color storage */
//				btheme->tnode= btheme->tv3d;
//				SETCOL(btheme->tnode.edge_select, 255, 255, 255, 255);
//				SETCOL(btheme->tnode.syntaxl, 150, 150, 150, 255);	/* TH_NODE, backdrop */
//				SETCOL(btheme->tnode.syntaxn, 129, 131, 144, 255);	/* in/output */
//				SETCOL(btheme->tnode.syntaxb, 127,127,127, 255);	/* operator */
//				SETCOL(btheme->tnode.syntaxv, 142, 138, 145, 255);	/* generator */
//				SETCOL(btheme->tnode.syntaxc, 120, 145, 120, 255);	/* group */
//			}
//			/* Group theme colors */
//			if(btheme->tv3d.group[3]==0) {
//				SETCOL(btheme->tv3d.group, 0x0C, 0x30, 0x0C, 255);
//				SETCOL(btheme->tv3d.group_active, 0x66, 0xFF, 0x66, 255);
//			}
//			/* Sequence editor theme*/
//			if(btheme->tseq.movie[3]==0) {
//				SETCOL(btheme->tseq.movie, 	81, 105, 135, 255);
//				SETCOL(btheme->tseq.image, 	109, 88, 129, 255);
//				SETCOL(btheme->tseq.scene, 	78, 152, 62, 255);
//				SETCOL(btheme->tseq.audio, 	46, 143, 143, 255);
//				SETCOL(btheme->tseq.effect, 	169, 84, 124, 255);
//				SETCOL(btheme->tseq.plugin, 	126, 126, 80, 255);
//				SETCOL(btheme->tseq.transition, 162, 95, 111, 255);
//				SETCOL(btheme->tseq.meta, 	109, 145, 131, 255);
//			}
//		}
//		
//		/* set defaults for 3D View rotating axis indicator */ 
//		/* since size can't be set to 0, this indicates it's not saved in startup.blend */
//		if (U.rvisize == 0) {
//			U.rvisize = 15;
//			U.rvibright = 8;
//			U.uiflag |= USER_SHOW_ROTVIEWICON;
//		}
//		
//	}
//	if (bmain->versionfile <= 242) {
//		bTheme *btheme;
//		
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* long keyframe color */
//			/* check for alpha==0 is safe, then color was never set */
//			if(btheme->tact.strip[3]==0) {
//				SETCOL(btheme->tv3d.edge_sharp, 255, 32, 32, 255);
//				SETCOL(btheme->tact.strip_select, 	0xff, 0xff, 0xaa, 204);
//				SETCOL(btheme->tact.strip, 0xe4, 0x9c, 0xc6, 204);
//			}
//			
//			/* IPO-Editor - Vertex Size*/
//			if(btheme->tipo.vertex_size == 0) {
//				btheme->tipo.vertex_size= 3;
//			}
//		}
//	}
//	if (bmain->versionfile <= 243) {
//		/* set default number of recently-used files (if not set) */
//		if (U.recent_files == 0) U.recent_files = 10;
//	}
//	if (bmain->versionfile < 245 || (bmain->versionfile == 245 && bmain->subversionfile < 3)) {
//		bTheme *btheme;
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			SETCOL(btheme->tv3d.editmesh_active, 255, 255, 255, 128);
//		}
//		if(U.coba_weight.tot==0)
//			init_colorband(&U.coba_weight, 1);
//	}
//	if ((bmain->versionfile < 245) || (bmain->versionfile == 245 && bmain->subversionfile < 11)) {
//		bTheme *btheme;
//		for (btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* these should all use the same color */
//			SETCOL(btheme->tv3d.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->tipo.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->tact.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->tnla.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->tseq.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->tsnd.cframe, 0x60, 0xc0, 0x40, 255);
//			SETCOL(btheme->ttime.cframe, 0x60, 0xc0, 0x40, 255);
//		}
//	}
//	if ((bmain->versionfile < 245) || (bmain->versionfile == 245 && bmain->subversionfile < 13)) {
//		bTheme *btheme;
//		for (btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* action channel groups (recolor anyway) */
//			SETCOL(btheme->tact.group, 0x39, 0x7d, 0x1b, 255);
//			SETCOL(btheme->tact.group_active, 0x7d, 0xe9, 0x60, 255);
//			
//			/* bone custom-color sets */
//			if (btheme->tarm[0].solid[3] == 0)
//				ui_theme_init_boneColorSets(btheme);
//		}
//	}
//	if ((bmain->versionfile < 245) || (bmain->versionfile == 245 && bmain->subversionfile < 16)) {
//		U.flag |= USER_ADD_VIEWALIGNED|USER_ADD_EDITMODE;
//	}
//	if ((bmain->versionfile < 247) || (bmain->versionfile == 247 && bmain->subversionfile <= 2)) {
//		bTheme *btheme;
//		
//		/* adjust themes */
//		for (btheme= U.themes.first; btheme; btheme= btheme->next) {
//			char *col;
//			
//			/* IPO Editor: Handles/Vertices */
//			col = btheme->tipo.vertex;
//			SETCOL(btheme->tipo.handle_vertex, col[0], col[1], col[2], 255);
//			col = btheme->tipo.vertex_select;
//			SETCOL(btheme->tipo.handle_vertex_select, col[0], col[1], col[2], 255);
//			btheme->tipo.handle_vertex_size= btheme->tipo.vertex_size;
//			
//			/* Sequence/Image Editor: colors for GPencil text */
//			col = btheme->tv3d.bone_pose;
//			SETCOL(btheme->tseq.bone_pose, col[0], col[1], col[2], 255);
//			SETCOL(btheme->tima.bone_pose, col[0], col[1], col[2], 255);
//			col = btheme->tv3d.vertex_select;
//			SETCOL(btheme->tseq.vertex_select, col[0], col[1], col[2], 255);
//		}
//	}
//	if (bmain->versionfile < 250) {
//		bTheme *btheme;
//		
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* this was not properly initialized in 2.45 */
//			if(btheme->tima.face_dot[3]==0) {
//				SETCOL(btheme->tima.editmesh_active, 255, 255, 255, 128);
//				SETCOL(btheme->tima.face_dot, 255, 133, 0, 255);
//				btheme->tima.facedot_size= 2;
//			}
//			
//			/* DopeSheet - (Object) Channel color */
//			SETCOL(btheme->tact.ds_channel, 	82, 96, 110, 255);
//			SETCOL(btheme->tact.ds_subchannel,	124, 137, 150, 255);
//			/* DopeSheet - Group Channel color (saner version) */
//			SETCOL(btheme->tact.group, 79, 101, 73, 255);
//			SETCOL(btheme->tact.group_active, 135, 177, 125, 255);
//			
//			/* Graph Editor - (Object) Channel color */
//			SETCOL(btheme->tipo.ds_channel, 	82, 96, 110, 255);
//			SETCOL(btheme->tipo.ds_subchannel,	124, 137, 150, 255);
//			/* Graph Editor - Group Channel color */
//			SETCOL(btheme->tipo.group, 79, 101, 73, 255);
//			SETCOL(btheme->tipo.group_active, 135, 177, 125, 255);
//			
//			/* Nla Editor - (Object) Channel color */
//			SETCOL(btheme->tnla.ds_channel, 	82, 96, 110, 255);
//			SETCOL(btheme->tnla.ds_subchannel,	124, 137, 150, 255);
//			/* NLA Editor - New Strip colors */
//			SETCOL(btheme->tnla.strip, 			12, 10, 10, 128); 
//			SETCOL(btheme->tnla.strip_select, 	255, 140, 0, 255);
//		}
//		
//		/* adjust grease-pencil distances */
//		U.gp_manhattendist= 1;
//		U.gp_euclideandist= 2;
//		
//		/* adjust default interpolation for new IPO-curves */
//		U.ipo_new= BEZT_IPO_BEZ;
//	}
//	
//	if (bmain->versionfile < 250 || (bmain->versionfile == 250 && bmain->subversionfile < 1)) {
//		bTheme *btheme;
//
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			
//			/* common (new) variables, it checks for alpha==0 */
//			ui_theme_init_new(btheme);
//
//			if(btheme->tui.wcol_num.outline[3]==0)
//				ui_widget_color_init(&btheme->tui);
//			
//			/* Logic editor theme, check for alpha==0 is safe, then color was never set */
//			if(btheme->tlogic.syntaxn[3]==0) {
//				/* re-uses syntax color storage */
//				btheme->tlogic= btheme->tv3d;
//				SETCOL(btheme->tlogic.back, 100, 100, 100, 255);
//			}
//
//			SETCOLF(btheme->tinfo.back, 0.45, 0.45, 0.45, 1.0);
//			SETCOLF(btheme->tuserpref.back, 0.45, 0.45, 0.45, 1.0);
//		}
//	}
//
//	if (bmain->versionfile < 250 || (bmain->versionfile == 250 && bmain->subversionfile < 3)) {
//		/* new audio system */
//		if(U.audiochannels == 0)
//			U.audiochannels = 2;
//		if(U.audiodevice == 0) {
//#ifdef WITH_OPENAL
//			U.audiodevice = 2;
//#endif
//#ifdef WITH_SDL
//			U.audiodevice = 1;
//#endif
//		}
//		if(U.audioformat == 0)
//			U.audioformat = 0x24;
//		if(U.audiorate == 0)
//			U.audiorate = 44100;
//	}
//
//	if (bmain->versionfile < 250 || (bmain->versionfile == 250 && bmain->subversionfile < 5))
//		U.gameflags |= USER_DISABLE_VBO;
//	
//	if (bmain->versionfile < 250 || (bmain->versionfile == 250 && bmain->subversionfile < 8)) {
//		wmKeyMap *km;
//		
//		for(km=U.keymaps.first; km; km=km->next) {
//			if (strcmp(km->idname, "Armature_Sketch")==0)
//				strcpy(km->idname, "Armature Sketch");
//			else if (strcmp(km->idname, "View3D")==0)
//				strcpy(km->idname, "3D View");
//			else if (strcmp(km->idname, "View3D Generic")==0)
//				strcpy(km->idname, "3D View Generic");
//			else if (strcmp(km->idname, "EditMesh")==0)
//				strcpy(km->idname, "Mesh");
//			else if (strcmp(km->idname, "TimeLine")==0)
//				strcpy(km->idname, "Timeline");
//			else if (strcmp(km->idname, "UVEdit")==0)
//				strcpy(km->idname, "UV Editor");
//			else if (strcmp(km->idname, "Animation_Channels")==0)
//				strcpy(km->idname, "Animation Channels");
//			else if (strcmp(km->idname, "GraphEdit Keys")==0)
//				strcpy(km->idname, "Graph Editor");
//			else if (strcmp(km->idname, "GraphEdit Generic")==0)
//				strcpy(km->idname, "Graph Editor Generic");
//			else if (strcmp(km->idname, "Action_Keys")==0)
//				strcpy(km->idname, "Dopesheet");
//			else if (strcmp(km->idname, "NLA Data")==0)
//				strcpy(km->idname, "NLA Editor");
//			else if (strcmp(km->idname, "Node Generic")==0)
//				strcpy(km->idname, "Node Editor");
//			else if (strcmp(km->idname, "Logic Generic")==0)
//				strcpy(km->idname, "Logic Editor");
//			else if (strcmp(km->idname, "File")==0)
//				strcpy(km->idname, "File Browser");
//			else if (strcmp(km->idname, "FileMain")==0)
//				strcpy(km->idname, "File Browser Main");
//			else if (strcmp(km->idname, "FileButtons")==0)
//				strcpy(km->idname, "File Browser Buttons");
//			else if (strcmp(km->idname, "Buttons Generic")==0)
//				strcpy(km->idname, "Property Editor");
//		}
//	}
//	if (bmain->versionfile < 250 || (bmain->versionfile == 250 && bmain->subversionfile < 16)) {
//		if(U.wmdrawmethod == USER_DRAW_TRIPLE)
//			U.wmdrawmethod = USER_DRAW_AUTOMATIC;
//	}
//	
//	if (bmain->versionfile < 252 || (bmain->versionfile == 252 && bmain->subversionfile < 3)) {
//		if (U.flag & USER_LMOUSESELECT) 
//			U.flag &= ~USER_TWOBUTTONMOUSE;
//	}
//	if (bmain->versionfile < 252 || (bmain->versionfile == 252 && bmain->subversionfile < 4)) {
//		bTheme *btheme;
//		
//		/* default new handle type is auto handles */
//		U.keyhandles_new = HD_AUTO;
//		
//		/* init new curve colors */
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* init colors used for handles in 3D-View  */
//			SETCOL(btheme->tv3d.handle_free, 0, 0, 0, 255);
//			SETCOL(btheme->tv3d.handle_auto, 0x90, 0x90, 0x00, 255);
//			SETCOL(btheme->tv3d.handle_vect, 0x40, 0x90, 0x30, 255);
//			SETCOL(btheme->tv3d.handle_align, 0x80, 0x30, 0x60, 255);
//			SETCOL(btheme->tv3d.handle_sel_free, 0, 0, 0, 255);
//			SETCOL(btheme->tv3d.handle_sel_auto, 0xf0, 0xff, 0x40, 255);
//			SETCOL(btheme->tv3d.handle_sel_vect, 0x40, 0xc0, 0x30, 255);
//			SETCOL(btheme->tv3d.handle_sel_align, 0xf0, 0x90, 0xa0, 255);
//			SETCOL(btheme->tv3d.act_spline, 0xdb, 0x25, 0x12, 255);
//			
//			/* same colors again for Graph Editor... */
//			SETCOL(btheme->tipo.handle_free, 0, 0, 0, 255);
//			SETCOL(btheme->tipo.handle_auto, 0x90, 0x90, 0x00, 255);
//			SETCOL(btheme->tipo.handle_vect, 0x40, 0x90, 0x30, 255);
//			SETCOL(btheme->tipo.handle_align, 0x80, 0x30, 0x60, 255);
//			SETCOL(btheme->tipo.handle_sel_free, 0, 0, 0, 255);
//			SETCOL(btheme->tipo.handle_sel_auto, 0xf0, 0xff, 0x40, 255);
//			SETCOL(btheme->tipo.handle_sel_vect, 0x40, 0xc0, 0x30, 255);
//			SETCOL(btheme->tipo.handle_sel_align, 0xf0, 0x90, 0xa0, 255);
//			
//			/* edge crease */
//			SETCOLF(btheme->tv3d.edge_crease, 0.8, 0, 0.6, 1.0);
//		}
//	}
//	if (bmain->versionfile <= 252) {
//		bTheme *btheme;
//
//		/* init new curve colors */
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			if (btheme->tv3d.lastsel_point[3] == 0)
//				SETCOL(btheme->tv3d.lastsel_point, 0xff, 0xff, 0xff, 255);
//		}
//	}
//	if (bmain->versionfile < 252 || (bmain->versionfile == 252 && bmain->subversionfile < 5)) {
//		bTheme *btheme;
//		
//		/* interface_widgets.c */
//		struct uiWidgetColors wcol_progress= {
//			{0, 0, 0, 255},
//			{190, 190, 190, 255},
//			{100, 100, 100, 180},
//			{68, 68, 68, 255},
//			
//			{0, 0, 0, 255},
//			{255, 255, 255, 255},
//			
//			0,
//			5, -5
//		};
//		
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			/* init progress bar theme */
//			btheme->tui.wcol_progress= wcol_progress;
//		}
//	}
//
//	if (bmain->versionfile < 255 || (bmain->versionfile == 255 && bmain->subversionfile < 2)) {
//		bTheme *btheme;
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			SETCOL(btheme->tv3d.extra_edge_len, 32, 0, 0, 255);
//			SETCOL(btheme->tv3d.extra_face_angle, 0, 32, 0, 255);
//			SETCOL(btheme->tv3d.extra_face_area, 0, 0, 128, 255);
//		}
//	}
//	
//	if (bmain->versionfile < 256 || (bmain->versionfile == 256 && bmain->subversionfile < 4)) {
//		bTheme *btheme;
//		for(btheme= U.themes.first; btheme; btheme= btheme->next) {
//			if((btheme->tv3d.outline_width) == 0) btheme->tv3d.outline_width= 1;
//		}
//	}
//
//	if (bmain->versionfile < 257) {
//		/* clear "AUTOKEY_FLAG_ONLYKEYINGSET" flag from userprefs, so that it doesn't linger around from old configs like a ghost */
//		U.autokey_flag &= ~AUTOKEY_FLAG_ONLYKEYINGSET;
//	}
//	
//	/* GL Texture Garbage Collection (variable abused above!) */
//	if (U.textimeout == 0) {
//		U.texcollectrate = 60;
//		U.textimeout = 120;
//	}
//	if (U.memcachelimit <= 0) {
//		U.memcachelimit = 32;
//	}
//	if (U.frameserverport == 0) {
//		U.frameserverport = 8080;
//	}
//	if (U.dbl_click_time == 0) {
//		U.dbl_click_time = 350;
//	}
//	if (U.anim_player_preset == 0) {
//		U.anim_player_preset = 1 ;
//	}
//	if (U.scrcastfps == 0) {
//		U.scrcastfps = 10;
//		U.scrcastwait = 50;
//	}
//	if (U.v2d_min_gridsize == 0) {
//		U.v2d_min_gridsize= 35;
//	}
//	if (U.dragthreshold == 0 )
//		U.dragthreshold= 5;
//
//	/* funny name, but it is GE stuff, moves userdef stuff to engine */
//// XXX	space_set_commmandline_options();
//	/* this timer uses U */
//// XXX	reset_autosave();

}

}


