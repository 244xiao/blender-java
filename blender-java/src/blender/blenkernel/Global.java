/**
 * blenlib/BKE_global.h (mar-2001 nzc)
 *
 * Global settings, handles, pointers. This is the root for finding
 * any data in Blender. This block is not serialized, but built anew
 * for every fresh Blender run.
 *
 * $Id: BKE_global.h 20073 2009-05-05 23:10:32Z bdiego $ 
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
package blender.blenkernel;

import static blender.blenkernel.Blender.G;
import blender.makesdna.sdna.ListBase;
import blender.windowmanager.WmTypes.RecentFile;

public class Global {

//    public SDNA currentSDNA;

	/* active pointers */
	public Main main;

	/* strings: lastsaved */
        public byte[] ima = new byte[256], sce = new byte[256], lib = new byte[256];

	/* flag: if != 0 G.sce contains valid relative base path */
	public int relbase_valid;

	/* strings of recent opend files */
	public ListBase<RecentFile> recent_files = new ListBase<RecentFile>();

	public short afbreek, moving;
	public short background;
	public short winpos, displaymode;	/* used to be in Render */
	public short rendering;			/* to indicate render is busy, prevent renderwindow events etc */

	public short rt;
	public int f;

//	/* Used for BMesh transformations */
//	struct BME_Glob *editBMesh;

	/* Frank's variables */
	public int save_over;

//	/* Rob's variables (keep here for WM recode) */
//	int have_quicktime;
//	int ui_international;
//	int charstart;
//	int charmin;
//	int charmax;
//	struct VFont *selfont;
//	struct ListBase ttfdata;

	/* libtiff flag used to determine if shared library loaded for libtiff*/
	public boolean have_libtiff;

	/* this variable is written to / read from FileGlobal->fileflags */
	public int fileflags;

	/* save the allowed windowstate of blender when using -W or -w */
	public int windowstate;

//	/* Janco's playing ground */
//	struct bSoundListener* listener;

	/* ndof device found ? */
	public int ndofdevice;

	/* confusing... G.f and G.flags */
	public int flags;

    /* **************** GLOBAL ********************* */

    /* G.f */
	public static final int G_RENDER_OGL=	(1 <<  0);
	public static final int G_SWAP_EXCHANGE=	(1 <<  1);
	/* also uses G_FILE_AUTOPLAY */
	/* #define G_RENDER_SHADOW	(1 <<  3) */ /* temp flag, removed */
	public static final int G_BACKBUFSEL=	(1 <<  4);
	public static final int G_PICKSEL=		(1 <<  5);

	/* #define G_FACESELECT	(1 <<  8) use (mesh->editflag & ME_EDIT_PAINT_MASK) */

	public static final int G_DEBUG=			(1 << 12);
	public static final int G_SCRIPT_AUTOEXEC= (1 << 13);
	public static final int G_SCRIPT_OVERRIDE_PREF= (1 << 14); /* when this flag is set ignore the userprefs */

	/* #define G_NOFROZEN	(1 << 17) also removed */
	public static final int G_GREASEPENCIL= 	(1 << 17);

	/* #define G_AUTOMATKEYS	(1 << 30)   also removed */
	
//    public static final int G_RENDER_OGL = (1 << 0);
//    public static final int G_SWAP_EXCHANGE = (1 << 1);
    /* also uses G_FILE_AUTOPLAY */
    public static final int G_RENDER_SHADOW = (1 << 3);
//    public static final int G_BACKBUFSEL = (1 << 4);
//    public static final int G_PICKSEL = (1 << 5);

    public static final int G_FACESELECT = (1 << 8);

    public static final int G_VERTEXPAINT = (1 << 10);

//    public static final int G_DEBUG = (1 << 12);
//    public static final int G_DOSCRIPTLINKS = (1 << 13);

    public static final int G_WEIGHTPAINT = (1 << 15);
    public static final int G_TEXTUREPAINT = (1 << 16);
    /* #define G_NOFROZEN	(1 << 17) also removed */
//    public static final int G_GREASEPENCIL = (1 << 17);

    public static final int G_SCULPTMODE = (1 << 29);
    public static final int G_PARTICLEEDIT = (1 << 30);

    /* #define G_AUTOMATKEYS	(1 << 30)   also removed */
    public static final int G_HIDDENHANDLES = (1 << 31); /* used for curves only */

    /* macro for testing face select mode
     * Texture paint could be removed since selected faces are not used
     * however hiding faces is useful */
    public static boolean FACESEL_PAINT_TEST() {
        return (G.f & G_FACESELECT) != 0 && (G.f & (G_VERTEXPAINT | G_WEIGHTPAINT | G_TEXTUREPAINT)) != 0;
    }

    /* G.fileflags */
    public static final int G_AUTOPACK = (1 << 0);
    public static final int G_FILE_COMPRESS = (1 << 1);
    public static final int G_FILE_AUTOPLAY = (1 << 2);
    public static final int G_FILE_ENABLE_ALL_FRAMES = (1 << 3);
    public static final int G_FILE_SHOW_DEBUG_PROPS = (1 << 4);
    public static final int G_FILE_SHOW_FRAMERATE = (1 << 5);
    public static final int G_FILE_SHOW_PROFILE = (1 << 6);
    public static final int G_FILE_LOCK = (1 << 7);
    public static final int G_FILE_SIGN = (1 << 8);
    public static final int G_FIle_PUBLISH = (1 << 9);
    public static final int G_FILE_NO_UI = (1 << 10);
    public static final int G_FILE_GAME_TO_IPO = (1 << 11);
    public static final int G_FILE_GAME_MAT = (1 << 12);
    public static final int G_FILE_DISPLAY_LISTS = (1 << 13);
    public static final int G_FILE_SHOW_PHYSICS = (1 << 14);
    public static final int G_FILE_GAME_MAT_GLSL = (1 << 15);
    public static final int G_FILE_GLSL_NO_LIGHTS = (1 << 16);
    public static final int G_FILE_GLSL_NO_SHADERS = (1 << 17);
    public static final int G_FILE_GLSL_NO_SHADOWS = (1 << 18);
    public static final int G_FILE_GLSL_NO_RAMPS = (1 << 19);
    public static final int G_FILE_GLSL_NO_NODES = (1 << 20);
    public static final int G_FILE_GLSL_NO_EXTRA_TEX = (1 << 21);
    public static final int G_FILE_IGNORE_DEPRECATION_WARNINGS = (1 << 22);

    /* G.windowstate */
    public static final int G_WINDOWSTATE_USERDEF = 0;
    public static final int G_WINDOWSTATE_BORDER = 1;
    public static final int G_WINDOWSTATE_FULLSCREEN = 2;

    /* ENDIAN_ORDER: indicates what endianness the platform where the file was
     * written had. */
    public static final int L_ENDIAN = 1;
    public static final int B_ENDIAN = 0;

    /* G.moving, signals drawing in (3d) window to denote transform */
    public static final int G_TRANSFORM_OBJ = 1;
    public static final int G_TRANSFORM_EDIT = 2;
    public static final int G_TRANSFORM_MANIP = 4;
    public static final int G_TRANSFORM_PARTICLE = 8;

    /* G.special1 */

}

