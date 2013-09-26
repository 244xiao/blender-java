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

//#ifndef WM_TYPES_H

import blender.blenkernel.bContext;
import blender.makesdna.sdna.Link;
//import blender.makesdna.sdna.World;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;

//#define WM_TYPES_H
//
//struct bContext;
//struct wmEvent;
//struct wmWindowManager;
//
///* exported types for WM */
//
//#include "wm_cursors.h"
//#include "wm_event_types.h"
//
public class WmTypes {
	
/* flags for WM_operator_properties_filesel */
public static final int WM_FILESEL_RELPATH=		(1 << 0);

public static final int WM_FILESEL_DIRECTORY=	(1 << 1);
public static final int WM_FILESEL_FILENAME=		(1 << 2);
public static final int WM_FILESEL_FILEPATH=		(1 << 3);

/* ************** wmOperatorType ************************ */

/* flag */
public static final int OPTYPE_REGISTER=		1;	/* register operators in stack after finishing */
public static final int OPTYPE_UNDO=			2;	/* do undo push after after */
public static final int OPTYPE_BLOCKING=		4;	/* let blender grab all input from the WM (X11) */
public static final int OPTYPE_MACRO=		8;
public static final int OPTYPE_GRAB_POINTER=	16;	/* */
public static final int OPTYPE_PRESET=		32;	/* show preset menu */

/* context to call operator in for WM_operator_name_call */
/* rna_ui.c contains EnumPropertyItem's of these, keep in sync */
//enum {
//	/* if there's invoke, call it, otherwise exec */
//	public static final int WM_OP_INVOKE_DEFAULT= 0;
//	public static final int WM_OP_INVOKE_REGION_WIN= 1;
//	public static final int WM_OP_INVOKE_AREA= 2;
//	public static final int WM_OP_INVOKE_SCREEN= 3;
//	/* only call exec */
//	public static final int WM_OP_EXEC_DEFAULT= 4;
//	public static final int WM_OP_EXEC_REGION_WIN= 5;
//	public static final int WM_OP_EXEC_AREA= 6;
//	public static final int WM_OP_EXEC_SCREEN= 7;
//};
public static final int WM_OP_INVOKE_DEFAULT = 0;
public static final int WM_OP_INVOKE_REGION_WIN = 1;
public static final int WM_OP_INVOKE_REGION_CHANNELS = 2;
public static final int WM_OP_INVOKE_REGION_PREVIEW = 3;
public static final int WM_OP_INVOKE_AREA = 4;
public static final int WM_OP_INVOKE_SCREEN = 5;
	/* only call exec */
public static final int WM_OP_EXEC_DEFAULT = 6;
public static final int WM_OP_EXEC_REGION_WIN = 7;
public static final int WM_OP_EXEC_REGION_CHANNELS = 8;
public static final int WM_OP_EXEC_REGION_PREVIEW = 9;
public static final int WM_OP_EXEC_AREA = 10;
public static final int WM_OP_EXEC_SCREEN = 11;

/* ************** wmKeyMap ************************ */

/* modifier */
public static final int KM_SHIFT=	1;
public static final int KM_CTRL=		2;
public static final int KM_ALT=		4;
public static final int KM_OSKEY=	8;
	/* means modifier should be pressed 2nd */
public static final int KM_SHIFT2=	16;
public static final int KM_CTRL2=	32;
public static final int KM_ALT2=		64;
public static final int KM_OSKEY2=	128;

/* type: defined in wm_event_types.c */
public static final int KM_TEXTINPUT=	-2;

/* val */
public static final int KM_ANY=		-1;
public static final int KM_NOTHING=	0;
public static final int KM_PRESS=	1;
public static final int KM_RELEASE=	2;


/* ************** UI Handler ***************** */

public static final int WM_UI_HANDLER_CONTINUE=	0;
public static final int WM_UI_HANDLER_BREAK=		1;

//typedef int (*wmUIHandlerFunc)(struct bContext *C, struct wmEvent *event, void *userdata);
public static interface WmUIHandlerFunc {
    public int run(bContext C, wmEvent event, Object userdata);
};
//typedef void (*wmUIHandlerRemoveFunc)(struct bContext *C, void *userdata);
public static interface WmUIHandlerRemoveFunc {
    public void run(bContext C, Object userdata);
};

/* ************** Notifiers ****************** */

public static class wmNotifier extends Link<wmNotifier> {
	public wmWindowManager wm;
	public wmWindow window;
	
	public int swinid;
	public int category, data, subtype, action;
	
	public Object reference;
};


///* 4 levels
//
//0xFF000000; category
//0x00FF0000; data
//0x0000FF00; data subtype (unused?)
//0x000000FF; action
//*/

/* category */
//public static final int NOTE_CATEGORY=		0xFF000000;
//public static final int	NC_WM=				(1<<24);
//public static final int	NC_WINDOW=			(2<<24);
//public static final int	NC_SCREEN=			(3<<24);
//public static final int	NC_SCENE=			(4<<24);
//public static final int	NC_OBJECT=			(5<<24);
//public static final int	NC_MATERIAL=			(6<<24);
//public static final int	NC_TEXTURE=			(7<<24);
//public static final int	NC_LAMP=				(8<<24);
//public static final int	NC_GROUP=			(9<<24);
//public static final int	NC_IMAGE=			(10<<24);
//public static final int	NC_BRUSH=			(11<<24);
//public static final int	NC_TEXT=				(12<<24);
//public static final int NC_WORLD=			(13<<24);
//public static final int NC_FILE=				(14<<24);
//public static final int NC_ANIMATION=		(15<<24);
//public static final int NC_CONSOLE=			(16<<24);
public static final int NOTE_CATEGORY=		0xFF000000;
public static final int	NC_WM=				(1<<24);
public static final int	NC_WINDOW=			(2<<24);
public static final int	NC_SCREEN=			(3<<24);
public static final int	NC_SCENE=			(4<<24);
public static final int	NC_OBJECT=			(5<<24);
public static final int	NC_MATERIAL=			(6<<24);
public static final int	NC_TEXTURE=			(7<<24);
public static final int	NC_LAMP=				(8<<24);
public static final int	NC_GROUP=			(9<<24);
public static final int	NC_IMAGE=			(10<<24);
public static final int	NC_BRUSH=			(11<<24);
public static final int	NC_TEXT=				(12<<24);
public static final int NC_WORLD=			(13<<24);
public static final int NC_ANIMATION=		(14<<24);
public static final int NC_SPACE=			(15<<24);
public static final int	NC_GEOM=				(16<<24);
public static final int NC_NODE=				(17<<24);
public static final int NC_ID=				(18<<24);
public static final int NC_LOGIC=			(19<<24);

/* data type, 256 entries is enough, it can overlap */
public static final int NOTE_DATA=			0x00FF0000;

	/* NC_WM windowmanager */
public static final int ND_FILEREAD=			(1<<16);
public static final int ND_FILESAVE=			(2<<16);
public static final int ND_DATACHANGED=		(3<<16);
public static final int ND_HISTORY=			(4<<16);
public static final int ND_JOB=				(5<<16);

	/* NC_SCREEN screen */
public static final int ND_SCREENBROWSE=		(1<<16);
public static final int ND_SCREENCAST=		(2<<16);
public static final int ND_ANIMPLAY=			(3<<16);

	/* NC_SCENE Scene */
//public static final int ND_SCENEBROWSE=		(1<<16);
//public static final int	ND_MARKERS=			(2<<16);
//public static final int	ND_FRAME=			(3<<16);
//public static final int	ND_RENDER_OPTIONS=	(4<<16);
//public static final int	ND_NODES=			(5<<16);
//public static final int	ND_SEQUENCER=		(6<<16);
//public static final int ND_OB_ACTIVE=		(7<<16);
//public static final int ND_OB_SELECT=		(8<<16);
//public static final int ND_MODE=				(9<<16);
//public static final int ND_RENDER_RESULT=	(10<<16);
//public static final int ND_COMPO_RESULT=		(11<<16);
//public static final int ND_KEYINGSET=		(12<<16);
public static final int ND_SCENEBROWSE=		(1<<16);
public static final int	ND_MARKERS=			(2<<16);
public static final int	ND_FRAME=			(3<<16);
public static final int	ND_RENDER_OPTIONS=	(4<<16);
public static final int	ND_NODES=			(5<<16);
public static final int	ND_SEQUENCER=		(6<<16);
public static final int ND_OB_ACTIVE=		(7<<16);
public static final int ND_OB_SELECT=		(8<<16);
public static final int ND_OB_VISIBLE=		(9<<16);
public static final int ND_OB_RENDER=		(10<<16);
public static final int ND_MODE=				(11<<16);
public static final int ND_RENDER_RESULT=	(12<<16);
public static final int ND_COMPO_RESULT=		(13<<16);
public static final int ND_KEYINGSET=		(14<<16);
public static final int ND_TOOLSETTINGS=		(15<<16);
public static final int ND_LAYER=			(16<<16);
public static final int ND_FRAME_RANGE=		(17<<16);
public static final int ND_WORLD=			(92<<16);
public static final int ND_LAYER_CONTENT=	(101<<16);

	/* NC_OBJECT Object */
public static final int	ND_TRANSFORM=		(16<<16);
public static final int ND_OB_SHADING=		(17<<16);
public static final int ND_POSE=				(18<<16);
public static final int ND_BONE_ACTIVE=		(19<<16);
public static final int ND_BONE_SELECT=		(20<<16);
public static final int ND_GEOM_SELECT=		(21<<16);
public static final int ND_DRAW=				(22<<16);
public static final int ND_MODIFIER=			(23<<16);
public static final int ND_KEYS=				(24<<16);
public static final int ND_GEOM_DATA=		(25<<16);
public static final int ND_CONSTRAINT=		(26<<16);
public static final int ND_PARTICLE=			(27<<16);
//public static final int	ND_TRANSFORM=		(18<<16);
//public static final int ND_OB_SHADING=		(19<<16);
//public static final int ND_POSE=				(20<<16);
//public static final int ND_BONE_ACTIVE=		(21<<16);
//public static final int ND_BONE_SELECT=		(22<<16);
//public static final int ND_DRAW=				(23<<16);
//public static final int ND_MODIFIER=			(24<<16);
//public static final int ND_KEYS=				(25<<16);
//public static final int ND_CONSTRAINT=		(26<<16);
//public static final int ND_PARTICLE=			(27<<16);
//public static final int ND_POINTCACHE=		(28<<16);
//public static final int ND_PARENT=			(29<<16);

	/* NC_MATERIAL Material */
public static final int	ND_SHADING=			(30<<16);
public static final int	ND_SHADING_DRAW=		(31<<16);

	/* NC_LAMP Lamp */
public static final int	ND_LIGHTING=			(44<<16);
public static final int	ND_LIGHTING_DRAW=	(45<<16);
public static final int ND_SKY=				(46<<16);

/* NC_WORLD World */
public static final int	ND_WORLD_DRAW=		(45<<16);
public static final int	ND_WORLD_STARS=		(46<<16);

	/* NC_TEXT Text */
public static final int ND_CURSOR=			(50<<16);
public static final int ND_DISPLAY=			(51<<16);

//	/* NC_FILE Filebrowser */
//public static final int ND_PARAMS=			(60<<16);
//public static final int ND_FILELIST=			(61<<16);

	/* NC_ANIMATION Animato */
//public static final int ND_KEYFRAME_SELECT=	(70<<16);
//public static final int ND_KEYFRAME_EDIT=	(71<<16);
//public static final int ND_KEYFRAME_PROP=	(72<<16);
//public static final int ND_ANIMCHAN_SELECT=	(73<<16);
//public static final int ND_ANIMCHAN_EDIT=	(74<<16);
//public static final int ND_NLA_SELECT=		(75<<16);
//public static final int ND_NLA_EDIT=			(76<<16);
//public static final int ND_NLA_ACTCHANGE=	(77<<16);
public static final int ND_KEYFRAME=			(70<<16);
public static final int ND_KEYFRAME_PROP=	(71<<16);
public static final int ND_ANIMCHAN=			(72<<16);
public static final int ND_NLA=				(73<<16);
public static final int ND_NLA_ACTCHANGE=	(74<<16);
public static final int ND_FCURVES_ORDER=	(75<<16);

	/* console */
//public static final int ND_CONSOLE=			(78<<16); /* general redraw */
//public static final int ND_CONSOLE_REPORT=	(79<<16); /* update for reports, could spesify type */

/* NC_GEOM Geometry */
/* Mesh, Curve, MetaBall, Armature, .. */
public static final int ND_SELECT=			(90<<16);
public static final int ND_DATA=				(91<<16);

/* NC_NODE Nodes */

/* NC_SPACE */
public static final int ND_SPACE_CONSOLE=		(1<<16); /* general redraw */
public static final int ND_SPACE_INFO_REPORT=	(2<<16); /* update for reports, could specify type */
public static final int ND_SPACE_INFO=			(3<<16);
public static final int ND_SPACE_IMAGE=			(4<<16);
public static final int ND_SPACE_FILE_PARAMS=	(5<<16);
public static final int ND_SPACE_FILE_LIST=		(6<<16);
public static final int ND_SPACE_NODE=			(7<<16);
public static final int ND_SPACE_OUTLINER=		(8<<16);
public static final int ND_SPACE_VIEW3D=			(9<<16);
public static final int ND_SPACE_PROPERTIES=		(10<<16);
public static final int ND_SPACE_TEXT=			(11<<16);
public static final int ND_SPACE_TIME=			(12<<16);
public static final int ND_SPACE_GRAPH=			(13<<16);
public static final int ND_SPACE_DOPESHEET=		(14<<16);
public static final int ND_SPACE_NLA=			(15<<16);
public static final int ND_SPACE_SEQUENCER=		(16<<16);
public static final int ND_SPACE_NODE_VIEW=		(17<<16);
public static final int ND_SPACE_CHANGED=		(18<<16); /*sent to a new editor type after it's replaced an old one*/

/* subtype, 256 entries too */
public static final int NOTE_SUBTYPE=		0x0000FF00;

/* subtype scene mode */
public static final int NS_MODE_OBJECT=		(1<<8);

public static final int NS_EDITMODE_MESH=	(2<<8);
public static final int NS_EDITMODE_CURVE=	(3<<8);
public static final int NS_EDITMODE_SURFACE=	(4<<8);
public static final int NS_EDITMODE_TEXT=	(5<<8);
public static final int NS_EDITMODE_MBALL=	(6<<8);
public static final int NS_EDITMODE_LATTICE=	(7<<8);
public static final int NS_EDITMODE_ARMATURE=	(8<<8);
public static final int NS_MODE_POSE=		(9<<8);
public static final int NS_MODE_PARTICLE=	(10<<8);


/* action classification */
public static final int NOTE_ACTION=			(0x000000FF);
public static final int NA_EDITED=			1;
public static final int NA_EVALUATED=		2;
public static final int NA_ADDED=			3;
public static final int NA_REMOVED=			4;
public static final int NA_RENAME=			5;
public static final int NA_SELECTED=			6;


/* ************** Gesture Manager data ************** */

/* wmGesture->type */
public static final int WM_GESTURE_TWEAK=		0;
public static final int WM_GESTURE_LINES=		1;
public static final int WM_GESTURE_RECT=			2;
public static final int WM_GESTURE_CROSS_RECT=	3;
public static final int WM_GESTURE_LASSO=		4;
public static final int WM_GESTURE_CIRCLE=		5;

///* wmGesture is registered to window listbase, handled by operator callbacks */
///* tweak gesture is builtin feature */
//typedef struct wmGesture {
//	struct wmGesture *next, *prev;
//	int event_type;	/* event->type */
//	int mode;		/* for modal callback */
//	int type;		/* gesture type define */
//	int swinid;		/* initial subwindow id where it started */
//	int points;		/* optional, amount of points stored */
//
//	void *customdata;
//	/* customdata for border is a recti */
//	/* customdata for circle is recti, (xmin, ymin) is center, xmax radius */
//	/* customdata for lasso is short array */
//} wmGesture;

/* ************** wmEvent ************************ */

/* each event should have full modifier state */
/* event comes from eventmanager and from keymap */
public static class wmEvent extends Link<wmEvent> {
//	struct wmEvent *next, *prev;
	
	public int type;			/* event code itself (short, is also in keymap) */
	public int val;			/* press, release, scrollvalue */
	public int x, y;			/* mouse pointer position, screen coord */
	public int[] mval = new int[2];		/* region mouse position, name convention pre 2.5 :) */
//	public short unicode;		/* future, ghost? */
//	public char ascii;			/* from ghost */
	public byte ascii;			/* from ghost */
//	public char pad;

	/* previous state */
	public int prevtype;
	public int prevval;
	public int prevx, prevy;
	public double prevclicktime;
	public int prevclickx, prevclicky;
	
	/* modifier states */
	public int shift, ctrl, alt, oskey;	/* oskey is apple or windowskey, value denotes order of pressed */
	public int keymodifier;				/* rawkey modifier */
	
//	public short pad1;
	
	/* keymap item, set by handler (weak?) */
	public byte[] keymap_idname;
	
	/* custom data */
	public int custom;		/* custom data type, stylus, 6dof, see wm_event_types.h */
	public int customdatafree;
//	public int pad2;
	public Object customdata;	/* ascii, unicode, mouse coords, angles, vectors, dragdrop info */
	
	public wmEvent copy() { try {return (wmEvent)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
	
};

///* ************** custom wmEvent data ************** */
//
//#define DEV_STYLUS	1
//#define DEV_ERASER  2
//
//typedef struct wmTabletData {
//	int Active;			/* 0=None, 1=Stylus, 2=Eraser */
//	float Pressure;		/* range 0.0 (not touching) to 1.0 (full pressure) */
//	float Xtilt;		/* range 0.0 (upright) to 1.0 (tilted fully against the tablet surface) */
//	float Ytilt;		/* as above */
//} wmTabletData;

public static class wmTimer extends Link<wmTimer> {
	public wmWindow win;	/* window this timer is attached to (optional) */
	
	public double timestep;		/* set by timer user */
	public int event_type;			/* set by timer user, goes to event system */
	public Object customdata;		/* set by timer user, to allow custom values */

	public double duration;		/* total running time in seconds */
	public double delta;			/* time since previous step in seconds */

	public double ltime;			/* internal, last time timer was activated */
	public double ntime;			/* internal, next time we want to activate the timer */
	public double stime;			/* internal, when the timer started */
	public int sleep;				/* internal, put timers to sleep when needed */
};


///* ****************** Messages ********************* */
//
//enum {
//	WM_LOG_DEBUG				= 0,
//	WM_LOG_INFO					= 1000,
//	WM_LOG_WARNING				= 2000,
//	WM_ERROR_UNDEFINED			= 3000,
//	WM_ERROR_INVALID_INPUT		= 3001,
//	WM_ERROR_INVALID_CONTEXT	= 3002,
//	WM_ERROR_OUT_OF_MEMORY		= 3003
//};
//
//typedef struct wmReport {
//	struct wmReport *next, *prev;
//	int type;
//	const char *typestr;
//	char *message;
//} wmReport;

/* *************** migrated stuff, clean later? ******************************** */

public static class RecentFile extends Link<RecentFile>{
//	public struct RecentFile *next, *prev;
	public String filename;
};


//#endif /* WM_TYPES_H */
}
