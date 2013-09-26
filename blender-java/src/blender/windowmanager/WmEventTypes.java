/*
 * $Id: wm_event_types.h
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
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

/*
 *  These define have its origin at sgi, where all device defines were written down in device.h.
 *  Blender copied the conventions quite some, and expanded it with internal new defines (ton)
 *
 */ 
public class WmEventTypes {

/* customdata type */
public static final int EVT_DATA_TABLET=		1;
public static final int EVT_DATA_GESTURE=	2;
public static final int EVT_DATA_TIMER=		3;

/* tablet active */
public static final int EVT_TABLET_NONE=		0;
public static final int EVT_TABLET_STYLUS=	1;
public static final int EVT_TABLET_ERASER=	2;

public static final int MOUSEX=		0x004;
public static final int MOUSEY=		0x005;

/* MOUSE : 0x00x */
public static final int LEFTMOUSE=		1;
public static final int MIDDLEMOUSE=		2;
public static final int RIGHTMOUSE=		3;
public static final int MOUSEMOVE=		4;
		/* only use if you want user option switch possible */
public static final int ACTIONMOUSE=		5;
public static final int SELECTMOUSE=		6;
//		/* drag & drop support */
//public static final int MOUSEDRAG=		0x007;
//public static final int MOUSEDROP=		0x008;
		/* Extra mouse buttons */
public static final int BUTTON4MOUSE=	7;
public static final int BUTTON5MOUSE=	8;
		/* Extra trackpad gestures */
public static final int MOUSEPAN=		14;
public static final int MOUSEZOOM=		15;
public static final int MOUSEROTATE=		16;
		/* defaults from ghost */
public static final int WHEELUPMOUSE=	10;
public static final int WHEELDOWNMOUSE=	11;
		/* mapped with userdef */
public static final int WHEELINMOUSE=	12;
public static final int WHEELOUTMOUSE=	13;
public static final int INBETWEEN_MOUSEMOVE=	17;


/* SYSTEM : 0x01x */
public static final int	INPUTCHANGE=		0x0103;	/* input connected or disconnected */

public static final int TIMER=			0x0110;	/* timer event, passed on to all queues */
public static final int TIMER0=			0x0111;	/* timer event, slot for internal use */
public static final int TIMER1=			0x0112;	/* timer event, slot for internal use */
public static final int TIMER2=			0x0113;	/* timer event, slot for internal use */
public static final int TIMERJOBS=		0x0114;  /* timer event, internal use */

/* standard keyboard */
public static final int AKEY=		'a';
public static final int BKEY=		'b';
public static final int CKEY=		'c';
public static final int DKEY=		'd';
public static final int EKEY=		'e';
public static final int FKEY=		'f';
public static final int GKEY=		'g';
public static final int HKEY=		'h';
public static final int IKEY=		'i';
public static final int JKEY=		'j';
public static final int KKEY=		'k';
public static final int LKEY=		'l';
public static final int MKEY=		'm';
public static final int NKEY=		'n';
public static final int OKEY=		'o';
public static final int PKEY=		'p';
public static final int QKEY=		'q';
public static final int RKEY=		'r';
public static final int SKEY=		's';
public static final int TKEY=		't';
public static final int UKEY=		'u';
public static final int VKEY=		'v';
public static final int WKEY=		'w';
public static final int XKEY=		'x';
public static final int YKEY=		'y';
public static final int ZKEY=		'z';

public static final int ZEROKEY=		'0';
public static final int ONEKEY=		'1';
public static final int TWOKEY=		'2';
public static final int THREEKEY=	'3';
public static final int FOURKEY=		'4';
public static final int FIVEKEY=		'5';
public static final int SIXKEY=		'6';
public static final int SEVENKEY=	'7';
public static final int EIGHTKEY=	'8';
public static final int NINEKEY=		'9';

public static final int CAPSLOCKKEY=		211;

public static final int LEFTCTRLKEY=		212;
public static final int LEFTALTKEY= 		213;
public static final int	RIGHTALTKEY= 	214;
public static final int	RIGHTCTRLKEY= 	215;
public static final int RIGHTSHIFTKEY=	216;
public static final int LEFTSHIFTKEY=	217;

public static final int ESCKEY=			218;
public static final int TABKEY=			219;
public static final int RETKEY=			220;
public static final int SPACEKEY=		221;
public static final int LINEFEEDKEY=		222;
public static final int BACKSPACEKEY=	223;
public static final int DELKEY=			224;
public static final int SEMICOLONKEY=	225;
public static final int PERIODKEY=		226;
public static final int COMMAKEY=		227;
public static final int QUOTEKEY=		228;
public static final int ACCENTGRAVEKEY=	229;
public static final int MINUSKEY=		230;
public static final int SLASHKEY=		232;
public static final int BACKSLASHKEY=	233;
public static final int EQUALKEY=		234;
public static final int LEFTBRACKETKEY=	235;
public static final int RIGHTBRACKETKEY=	236;

public static final int LEFTARROWKEY=	137;
public static final int DOWNARROWKEY=	138;
public static final int RIGHTARROWKEY=	139;
public static final int UPARROWKEY=		140;

public static final int PAD0=			150;
public static final int PAD1=			151;
public static final int PAD2=			152;
public static final int PAD3=			153;
public static final int PAD4=			154;
public static final int PAD5=			155;
public static final int PAD6=			156;
public static final int PAD7=			157;
public static final int PAD8=			158;
public static final int PAD9=			159;


public static final int PADPERIOD=		199;
public static final int	PADSLASHKEY= 	161;
public static final int PADASTERKEY= 	160;

public static final int PADMINUS=		162;
public static final int PADENTER=		163;
public static final int PADPLUSKEY= 		164;

public static final int	F1KEY= 		300;
public static final int	F2KEY= 		301;
public static final int	F3KEY= 		302;
public static final int	F4KEY= 		303;
public static final int	F5KEY= 		304;
public static final int	F6KEY= 		305;
public static final int	F7KEY= 		306;
public static final int	F8KEY= 		307;
public static final int	F9KEY= 		308;
public static final int	F10KEY=		309;
public static final int	F11KEY=		310;
public static final int	F12KEY=		311;

public static final int	PAUSEKEY=	165;
public static final int	INSERTKEY=	166;
public static final int	HOMEKEY= 	167;
public static final int	PAGEUPKEY= 	168;
public static final int	PAGEDOWNKEY=	169;
public static final int	ENDKEY=		170;

public static final int UNKNOWNKEY=	171;
public static final int COMMANDKEY=	172;
public static final int GRLESSKEY=	173;

/* for event checks */
	/* only used for KM_TEXTINPUT, so assume that we want all user-inputtable ascii codes included */
public static final boolean ISKEYBOARD(int event) {
    return (event >=' ' && event <=255);
}

/* test whether event type is acceptable as hotkey, excluding modifiers */
public static final boolean ISHOTKEY(int event) {
    return (event >=' ' && event <=320 && !(event>=LEFTCTRLKEY && event<=ESCKEY) && !(event>=UNKNOWNKEY && event<=GRLESSKEY));
}


/* **************** BLENDER GESTURE EVENTS ********************* */

public static final int EVT_ACTIONZONE_AREA=		0x5000;
public static final int EVT_ACTIONZONE_REGION=	0x5001;

		/* tweak events, for L M R mousebuttons */
public static final int EVT_TWEAK_L=		0x5002;
public static final int EVT_TWEAK_M=		0x5003;
public static final int EVT_TWEAK_R=		0x5004;
		/* tweak events for action or select mousebutton */
public static final int EVT_TWEAK_A=		0x5005;
public static final int EVT_TWEAK_S=		0x5006;

public static final int EVT_GESTURE=		0x5010;

/* value of tweaks and line gestures, note, KM_ANY (-1) works for this case too */
public static final int EVT_GESTURE_N=		1;
public static final int EVT_GESTURE_NE=		2;
public static final int EVT_GESTURE_E=		3;
public static final int EVT_GESTURE_SE=		4;
public static final int EVT_GESTURE_S=		5;
public static final int EVT_GESTURE_SW=		6;
public static final int EVT_GESTURE_W=		7;
public static final int EVT_GESTURE_NW=		8;
/* value of corner gestures */
public static final int EVT_GESTURE_N_E=		9;
public static final int EVT_GESTURE_N_W=		10;
public static final int EVT_GESTURE_E_N=		11;
public static final int EVT_GESTURE_E_S=		12;
public static final int EVT_GESTURE_S_E=		13;
public static final int EVT_GESTURE_S_W=		14;
public static final int EVT_GESTURE_W_S=		15;
public static final int EVT_GESTURE_W_N=		16;

/* **************** OTHER BLENDER EVENTS ********************* */

/* event->type */
public static final int EVT_FILESELECT=	0x5020;

/* event->val */
public static final int EVT_FILESELECT_OPEN=			1;
public static final int EVT_FILESELECT_FULL_OPEN=	2;
public static final int EVT_FILESELECT_EXEC=			3;
public static final int EVT_FILESELECT_CANCEL=		4;

/* event->type */
public static final int EVT_BUT_OPEN=	0x5021;
public static final int EVT_MODAL_MAP=	0x5022;

}
