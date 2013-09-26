/*
 * $Id: creator.c 21829 2009-07-23 21:50:40Z blendix $
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
package creator;

import javax.swing.UIManager;

import blender.blenkernel.Blender;
import blender.blenkernel.bContext;
import blender.blenlib.Args;
import blender.blenlib.Args.BA_ArgCallback;
import blender.blenlib.Args.bArgs;
import blender.makesrna.RnaAccess;
import blender.windowmanager.Wm;
import blender.windowmanager.WmInitExit;
import static blender.blenkernel.Blender.G;

public class Creator {

//// from buildinfo.c
//#ifdef BUILD_DATE
//extern char * build_date;
//extern char * build_time;
//extern char * build_platform;
//extern char * build_type;
//#endif
//
///*	Local Function prototypes */
//static void print_help(void);
//static void print_version(void);
//
///* for the callbacks: */
//
//extern int pluginapi_force_ref(void);  /* from blenpluginapi:pluginapi.c */
//
//char bprogname[FILE_MAXDIR+FILE_MAXFILE]; /* from blenpluginapi:pluginapi.c */
//char btempdir[FILE_MAXDIR+FILE_MAXFILE];
//
///* Initialise callbacks for the modules that need them */
//static void setCallbacks(void);
//
//#if defined(__sgi) || defined(__alpha__)
//static void fpe_handler(int sig)
//{
//	// printf("SIGFPE trapped\n");
//}
//#endif
//
///* handling ctrl-c event in console */
//static void blender_esc(int sig)
//{
//	static int count = 0;
//
//	G.afbreek = 1;	/* forces render loop to read queue, not sure if its needed */
//
//	if (sig == 2) {
//		if (count) {
//			printf("\nBlender killed\n");
//			exit(2);
//		}
//		printf("\nSent an internal break event. Press ^C again to kill Blender\n");
//		count++;
//	}
//}

public static BA_ArgCallback print_version = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	System.out.printf (BLEND_VERSION_STRING_FMT);
//#ifdef BUILD_DATE
	System.out.printf ("Blender %d.%02d (sub %d) Build\n", Blender.BLENDER_VERSION/100, Blender.BLENDER_VERSION%100, Blender.BLENDER_SUBVERSION);
//	printf ("\tbuild date: %s\n", build_date);
//	printf ("\tbuild time: %s\n", build_time);
//	printf ("\tbuild revision: %s\n", build_rev);
//	printf ("\tbuild platform: %s\n", build_platform);
//	printf ("\tbuild type: %s\n", build_type);
//	printf ("\tbuild c flags: %s\n", build_cflags);
//	printf ("\tbuild c++ flags: %s\n", build_cxxflags);
//	printf ("\tbuild link flags: %s\n", build_linkflags);
//	printf ("\tbuild system: %s\n", build_system);
//#endif
//	exit(0);

	return 0;
}};
	
public static void print_version()
{
//#ifdef BUILD_DATE
//	printf ("Blender %d.%02d (sub %d) Build\n", BLENDER_VERSION/100, BLENDER_VERSION%100, BLENDER_SUBVERSION);
//	printf ("\tbuild date: %s\n", build_date);
//	printf ("\tbuild time: %s\n", build_time);
//	printf ("\tbuild revision: %s\n", build_rev);
//	printf ("\tbuild platform: %s\n", build_platform);
//	printf ("\tbuild type: %s\n", build_type);
//#else
	System.out.printf ("Blender %d.%02d (sub %d) Build\n", Blender.BLENDER_VERSION/100, Blender.BLENDER_VERSION%100, Blender.BLENDER_SUBVERSION);
//#endif
}

public static BA_ArgCallback print_help = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bArgs ba = (bArgs)data;

//	System.out.printf (BLEND_VERSION_STRING_FMT);
	System.out.printf ("Usage: blender [args ...] [file] [args ...]\n\n");

	System.out.printf ("Render Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "--background");
	Args.BLI_argsPrintArgDoc(ba, "--render-anim");
	Args.BLI_argsPrintArgDoc(ba, "--scene");
	Args.BLI_argsPrintArgDoc(ba, "--render-frame");
	Args.BLI_argsPrintArgDoc(ba, "--frame-start");
	Args.BLI_argsPrintArgDoc(ba, "--frame-end");
	Args.BLI_argsPrintArgDoc(ba, "--frame-jump");
	Args.BLI_argsPrintArgDoc(ba, "--render-output");
	Args.BLI_argsPrintArgDoc(ba, "--engine");
	
	System.out.printf("\n");
	System.out.printf ("Format Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "--render-format");
	Args.BLI_argsPrintArgDoc(ba, "--use-extension");
	Args.BLI_argsPrintArgDoc(ba, "--threads");

	System.out.printf("\n");
	System.out.printf ("Animation Playback Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "-a");
				
	System.out.printf("\n");
	System.out.printf ("Window Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "--window-border");
	Args.BLI_argsPrintArgDoc(ba, "--window-borderless");
	Args.BLI_argsPrintArgDoc(ba, "--window-geometry");
	Args.BLI_argsPrintArgDoc(ba, "--start-console");

	System.out.printf("\n");
	System.out.printf ("Game Engine Specific Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "-g");

	System.out.printf("\n");
	System.out.printf ("Misc Options:\n");
	Args.BLI_argsPrintArgDoc(ba, "--debug");
	Args.BLI_argsPrintArgDoc(ba, "--debug-fpe");
	System.out.printf("\n");
	Args.BLI_argsPrintArgDoc(ba, "--factory-startup");
	System.out.printf("\n");
	Args.BLI_argsPrintArgDoc(ba, "--env-system-config");
	Args.BLI_argsPrintArgDoc(ba, "--env-system-datafiles");
	Args.BLI_argsPrintArgDoc(ba, "--env-system-scripts");
	Args.BLI_argsPrintArgDoc(ba, "--env-system-plugins");
	Args.BLI_argsPrintArgDoc(ba, "--env-system-python");
	System.out.printf("\n");
	Args.BLI_argsPrintArgDoc(ba, "-nojoystick");
	Args.BLI_argsPrintArgDoc(ba, "-noglsl");
	Args.BLI_argsPrintArgDoc(ba, "-noaudio");
	Args.BLI_argsPrintArgDoc(ba, "-setaudio");

	System.out.printf("\n");

	Args.BLI_argsPrintArgDoc(ba, "--help");

	System.out.printf("\n");

	Args.BLI_argsPrintArgDoc(ba, "--enable-autoexec");
	Args.BLI_argsPrintArgDoc(ba, "--disable-autoexec");

	System.out.printf("\n");

	Args.BLI_argsPrintArgDoc(ba, "--python");
	Args.BLI_argsPrintArgDoc(ba, "--python-console");
	Args.BLI_argsPrintArgDoc(ba, "--addons");

//#ifdef WIN32
	Args.BLI_argsPrintArgDoc(ba, "-R");
	Args.BLI_argsPrintArgDoc(ba, "-r");
//#endif
	Args.BLI_argsPrintArgDoc(ba, "--version");

	Args.BLI_argsPrintArgDoc(ba, "--");

	System.out.printf ("Other Options:\n");
	Args.BLI_argsPrintOtherDoc(ba);

	System.out.printf ("Argument Parsing:\n");
	System.out.printf ("\targuments must be separated by white space. eg\n");
	System.out.printf ("\t\t\"blender -ba test.blend\"\n");
	System.out.printf ("\t...will ignore the 'a'\n");
	System.out.printf ("\t\t\"blender -b test.blend -f8\"\n");
	System.out.printf ("\t...will ignore 8 because there is no space between the -f and the frame value\n\n");

	System.out.printf ("Argument Order:\n");
	System.out.printf ("Arguments are executed in the order they are given. eg\n");
	System.out.printf ("\t\t\"blender --background test.blend --render-frame 1 --render-output /tmp\"\n");
	System.out.printf ("\t...will not render to /tmp because '--render-frame 1' renders before the output path is set\n");
	System.out.printf ("\t\t\"blender --background --render-output /tmp test.blend --render-frame 1\"\n");
	System.out.printf ("\t...will not render to /tmp because loading the blend file overwrites the render output that was set\n");
	System.out.printf ("\t\t\"blender --background test.blend --render-output /tmp --render-frame 1\" works as expected.\n\n");

	System.out.printf ("\nEnvironment Variables:\n");
	System.out.printf ("  $BLENDER_USER_CONFIG      Directory for user configuration files.\n");
	System.out.printf ("  $BLENDER_SYSTEM_CONFIG    Directory for system wide configuration files.\n");
	System.out.printf ("  $BLENDER_USER_SCRIPTS     Directory for user scripts.\n");
	System.out.printf ("  $BLENDER_SYSTEM_SCRIPTS   Directory for system wide scripts.\n");
	System.out.printf ("  $BLENDER_USER_DATAFILES   Directory for user data files (icons, translations, ..).\n");
	System.out.printf ("  $BLENDER_SYSTEM_DATAFILES Directory for system wide data files.\n");
	System.out.printf ("  $BLENDER_SYSTEM_PYTHON    Directory for system python libraries.\n");
//#ifdef WIN32
	System.out.printf ("  $TEMP                     Store temporary files here.\n");
//#else
//	printf ("  $TMP or $TMPDIR           Store temporary files here.\n");
//#endif
//#ifndef DISABLE_SDL
//	printf ("  $SDL_AUDIODRIVER          LibSDL audio driver - alsa, esd, dma.\n");
//#endif
	System.out.printf ("  $PYTHONHOME               Path to the python directory, eg. /usr/lib/python.\n\n");

//	exit(0);

	return 0;
}};

public static void print_help()
{
//	printf ("Blender %d.%02d (sub %d) Build\n", BLENDER_VERSION/100, BLENDER_VERSION%100, BLENDER_SUBVERSION);
        System.out.printf ("Usage: blender [args ...] [file] [args ...]\n");
//	printf ("\nRender options:\n");
//	printf ("  -b <file>\tRender <file> in background (doesn't load the user defaults .B.blend file)\n");
//	printf ("    -a render frames from start to end (inclusive), only works when used after -b\n");
//	printf ("    -S <name>\tSet scene <name>\n");
//	printf ("    -f <frame>\tRender frame <frame> and save it\n");
//	printf ("    -s <frame>\tSet start to frame <frame> (use before the -a argument)\n");
//	printf ("    -e <frame>\tSet end to frame <frame> (use before the -a argument)\n");
//	printf ("    -o <path>\tSet the render path and file name.\n");
//	printf ("      Use // at the start of the path to\n");
//	printf ("        render relative to the blend file.\n");
//	printf ("      The # characters are replaced by the frame number, and used to define zero padding.\n");
//	printf ("        ani_##_test.png becomes ani_01_test.png\n");
//	printf ("        test-######.png becomes test-000001.png\n");
//	printf ("        When the filename has no #, The suffix #### is added to the filename\n");
//	printf ("      The frame number will be added at the end of the filename.\n");
//	printf ("      eg: blender -b foobar.blend -o //render_ -F PNG -x 1 -a\n");
//	printf ("\nFormat options:\n");
//	printf ("    -F <format>\tSet the render format, Valid options are...\n");
//	printf ("    \tTGA IRIS HAMX JPEG MOVIE IRIZ RAWTGA\n");
//	printf ("    \tAVIRAW AVIJPEG PNG BMP FRAMESERVER\n");
//	printf ("    (formats that can be compiled into blender, not available on all systems)\n");
//	printf ("    \tHDR TIFF EXR MULTILAYER MPEG AVICODEC QUICKTIME CINEON DPX DDS\n");
//	printf ("    -x <bool>\tSet option to add the file extension to the end of the file.\n");
//	printf ("    -t <threads>\tUse amount of <threads> for rendering (background mode only).\n");
//	printf ("      [1-8], 0 for systems processor count.\n");
//	printf ("\nAnimation playback options:\n");
//	printf ("  -a <options> <file(s)>\tPlayback <file(s)>, only operates this way when -b is not used.\n");
//	printf ("    -p <sx> <sy>\tOpen with lower left corner at <sx>, <sy>\n");
//	printf ("    -m\t\tRead from disk (Don't buffer)\n");
//	printf ("    -f <fps> <fps-base>\t\tSpecify FPS to start with\n");
//	printf ("    -j <frame>\tSet frame step to <frame>\n");
//
//	printf ("\nWindow options:\n");
//	printf ("  -w\t\tForce opening with borders (default)\n");
//	printf ("  -W\t\tForce opening without borders\n");
//	printf ("  -p <sx> <sy> <w> <h>\tOpen with lower left corner at <sx>, <sy>\n");
//	printf ("                      \tand width and height <w>, <h>\n");
//	printf ("\nGame Engine specific options:\n");
//	printf ("  -g fixedtime\t\tRun on 50 hertz without dropping frames\n");
//	printf ("  -g vertexarrays\tUse Vertex Arrays for rendering (usually faster)\n");
//	printf ("  -g noaudio\t\tNo audio in Game Engine\n");
//	printf ("  -g nomipmap\t\tNo Texture Mipmapping\n");
//	printf ("  -g linearmipmap\tLinear Texture Mipmapping instead of Nearest (default)\n");
//
//	printf ("\nMisc options:\n");
//	printf ("  -d\t\tTurn debugging on\n");
//	printf ("  -noaudio\tDisable audio on systems that support audio\n");
//	printf ("  -nojoystick\tDisable joystick support\n");
//	printf ("  -noglsl\tDisable GLSL shading\n");
//	printf ("  -h\t\tPrint this help text\n");
//	printf ("  -y\t\tDisable automatic python script execution (pydrivers, pyconstraints, pynodes)\n");
//	printf ("  -P <filename>\tRun the given Python script (filename or Blender Text)\n");
//#ifdef WIN32
//	printf ("  -R\t\tRegister .blend extension\n");
//#endif
//	printf ("  -v\t\tPrint Blender version and exit\n");
//	printf ("  --\t\tEnds option processing.  Following arguments are \n");
//	printf ("    \t\t   passed unchanged.  Access via Python's sys.argv\n");
//	printf ("\nEnvironment Variables:\n");
//	printf ("  $HOME\t\t\tStore files such as .blender/ .B.blend .Bfs .Blog here.\n");
//#ifdef WIN32
//	printf ("  $TEMP\t\tStore temporary files here.\n");
//#else
//	printf ("  $TMP or $TMPDIR\tStore temporary files here.\n");
//	printf ("  $BF_TIFF_LIB\t\tUse an alternative libtiff.so for loading tiff image files.\n");
//#endif
//#ifndef DISABLE_SDL
//	printf ("  $SDL_AUDIODRIVER\tLibSDL audio driver - alsa, esd, alsa, dma.\n");
//#endif
//	printf ("  $IMAGEEDITOR\t\tImage editor executable, launch with the IKey from the file selector.\n");
//	printf ("  $WINEDITOR\t\tText editor executable, launch with the EKey from the file selector.\n");
//	printf ("  $PYTHONHOME\t\tPath to the python directory, eg. /usr/lib/python.\n");
//	printf ("\nNote: Arguments must be separated by white space. eg:\n");
//	printf ("    \"blender -ba test.blend\"\n");
//	printf ("  ...will ignore the 'a'\n");
//	printf ("    \"blender -b test.blend -f8\"\n");
//	printf ("  ...will ignore 8 because there is no space between the -f and the frame value\n");
//	printf ("Note: Arguments are executed in the order they are given. eg:\n");
//	printf ("    \"blender -b test.blend -f 1 -o /tmp\"\n");
//	printf ("  ...may not render to /tmp because '-f 1' renders before the output path is set\n");
//	printf ("    \"blender -b -o /tmp test.blend -f 1\"\n");
//	printf ("  ...may not render to /tmp because loading the blend file overwrites the output path that was set\n");
//	printf ("    \"blender -b test.blend -o /tmp -f 1\" works as expected.\n\n");
}

//double PIL_check_seconds_timer(void);

/* XXX This was here to fix a crash when running python scripts
 * with -P that used the screen.
 *
 * static void main_init_screen( void )
{
	setscreen(G.curscreen);

	if(G.main->scene.first==0) {
		set_scene( add_scene("1") );
	}
}*/

public static BA_ArgCallback end_arguments = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	return -1;
}};

public static BA_ArgCallback enable_python = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	G.f |= G_SCRIPT_AUTOEXEC;
//	G.f |= G_SCRIPT_OVERRIDE_PREF;
	return 0;
}};

public static BA_ArgCallback disable_python = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	G.f &= ~G_SCRIPT_AUTOEXEC;
//	G.f |= G_SCRIPT_OVERRIDE_PREF;
	return 0;
}};

public static BA_ArgCallback background_mode = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	G.background = 1;
	return 0;
}};

public static BA_ArgCallback debug_mode = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	G.f |= G_DEBUG;		/* std output printf's */
//	printf(BLEND_VERSION_STRING_FMT);
//	MEM_set_memory_debug();
//
//#ifdef NAN_BUILDINFO
//	printf("Build: %s %s %s %s\n", build_date, build_time, build_platform, build_type);
//#endif // NAN_BUILDINFO
//
//	BLI_argsPrint(data);
	return 0;
}};

public static BA_ArgCallback set_fpe = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//#if defined(__sgi) || defined(__linux__) || defined(_WIN32) || defined(OSX_SSE_FPE)
//	/* zealous but makes float issues a heck of a lot easier to find!
//	 * set breakpoints on fpe_handler */
//	signal(SIGFPE, fpe_handler);
//
//# if defined(__linux__) && defined(__GNUC__)
//	feenableexcept(FE_DIVBYZERO | FE_INVALID | FE_OVERFLOW );
//# endif	/* defined(__linux__) && defined(__GNUC__) */
//# if defined(OSX_SSE_FPE)
//	/* OSX uses SSE for floating point by default, so here 
//	 * use SSE instructions to throw floating point exceptions */
//	_MM_SET_EXCEPTION_MASK(_MM_MASK_MASK &~
//			(_MM_MASK_OVERFLOW|_MM_MASK_INVALID|_MM_MASK_DIV_ZERO));
//# endif	/* OSX_SSE_FPE */
//# if defined(_WIN32) && defined(_MSC_VER)
//	_controlfp_s(NULL, 0, _MCW_EM); /* enables all fp exceptions */
//	_controlfp_s(NULL, _EM_DENORMAL | _EM_UNDERFLOW | _EM_INEXACT, _MCW_EM); /* hide the ones we don't care about */
//# endif /* _WIN32 && _MSC_VER */
//#endif

	return 0;
}};

public static BA_ArgCallback set_factory_startup = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	G.factory_startup= 1;
	return 0;
}};

public static BA_ArgCallback set_env = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	/* "--env-system-scripts" --> "BLENDER_SYSTEM_SCRIPTS" */
//
//	char env[64]= "BLENDER";
//	char *ch_dst= env + 7; /* skip BLENDER */
//	const char *ch_src= argv[0] + 5; /* skip --env */
//
//	if (argc < 2) {
//		printf("%s requires one argument\n", argv[0]);
//		exit(1);
//	}
//
//	for(; *ch_src; ch_src++, ch_dst++) {
//		*ch_dst= (*ch_src == '-') ? '_' : (*ch_src)-32; /* toupper() */
//	}
//
//	*ch_dst= '\0';
//	BLI_setenv(env, argv[1]);
	return 1;
}};

public static BA_ArgCallback playback_mode = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	/* not if -b was given first */
//	if (G.background == 0) {
//
//// XXX				playanim(argc, argv); /* not the same argc and argv as before */
//		exit(0);
//	}

	return -2;
}};

public static BA_ArgCallback prefsize = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	int stax, stay, sizx, sizy;
//
//	if (argc < 5) {
//		printf ("-p requires four arguments\n");
//		exit(1);
//	}
//
//	stax= atoi(argv[1]);
//	stay= atoi(argv[2]);
//	sizx= atoi(argv[3]);
//	sizy= atoi(argv[4]);
//
//	WM_setprefsize(stax, stay, sizx, sizy);

	return 4;
}};

public static BA_ArgCallback with_borders = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	WM_setinitialstate_normal();
	return 0;
}};

public static BA_ArgCallback without_borders = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	WM_setinitialstate_fullscreen();
	return 0;
}};

//extern int wm_start_with_console; // blender/windowmanager/intern/wm_init_exit.c
public static BA_ArgCallback start_with_console = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	wm_start_with_console = 1;
	return 0;
}};

public static BA_ArgCallback register_extension = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//#ifdef WIN32
//	if (data)
//		G.background = 1;
//	RegisterBlendExtension();
//#else
//	(void)data; /* unused */
//#endif
	return 0;
}};

public static BA_ArgCallback no_joystick = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//#ifndef WITH_GAMEENGINE
//	(void)data;
//#else
//	SYS_SystemHandle *syshandle = data;
//
//	/**
//	 	don't initialize joysticks if user doesn't want to use joysticks
//		failed joystick initialization delays over 5 seconds, before game engine start
//	*/
//	SYS_WriteCommandLineInt(*syshandle, "nojoystick",1);
//	if (G.f & G_DEBUG) printf("disabling nojoystick\n");
//#endif

	return 0;
}};

public static BA_ArgCallback no_glsl = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	GPU_extensions_disable();
	return 0;
}};

public static BA_ArgCallback no_audio = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	sound_force_device(0);
	return 0;
}};

public static BA_ArgCallback set_audio = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	if (argc < 1) {
//		printf("-setaudio require one argument\n");
//		exit(1);
//	}
//
//	sound_force_device(sound_define_from_str(argv[1]));
	return 1;
}};

public static BA_ArgCallback set_output = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	bContext *C = data;
	if (argc >= 1){
//		if (CTX_data_scene(C)) {
//			Scene *scene= CTX_data_scene(C);
//			BLI_strncpy(scene->r.pic, argv[1], FILE_MAXDIR);
//		} else {
//			printf("\nError: no blend loaded. cannot use '-o / --render-output'.\n");
//		}
		return 1;
	} else {
		System.out.printf("\nError: you must specify a path after '-o  / --render-output'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_engine = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	bContext *C = data;
	if (argc >= 1)
	{
//		if (!strcmp(argv[1],"help"))
//		{
//			RenderEngineType *type = NULL;
//
//			for( type = R_engines.first; type; type = type->next )
//			{
//				printf("\t%s\n", type->idname);
//			}
//			exit(0);
//		}
//		else
//		{
//			if (CTX_data_scene(C)==NULL)
//			{
//				printf("\nError: no blend loaded. order the arguments so '-E  / --engine ' is after a blend is loaded.\n");
//			}
//			else {
//				Scene *scene= CTX_data_scene(C);
//				RenderData *rd = &scene->r;
//
//				if(BLI_findstring(&R_engines, argv[1], offsetof(RenderEngineType, idname))) {
//					BLI_strncpy(rd->engine, argv[1], sizeof(rd->engine));
//				}
//			}
//		}

		return 1;
	}
	else
	{
		System.out.printf("\nEngine not specified.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_image_type = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	bContext *C = data;
	if (argc >= 1){
//		const char *imtype = argv[1];
//		if (CTX_data_scene(C)==NULL) {
//			printf("\nError: no blend loaded. order the arguments so '-F  / --render-format' is after the blend is loaded.\n");
//		} else {
//			Scene *scene= CTX_data_scene(C);
//			if      (!strcmp(imtype,"TGA")) scene->r.imtype = R_TARGA;
//			else if (!strcmp(imtype,"IRIS")) scene->r.imtype = R_IRIS;
//#ifdef WITH_DDS
//			else if (!strcmp(imtype,"DDS")) scene->r.imtype = R_DDS;
//#endif
//			else if (!strcmp(imtype,"JPEG")) scene->r.imtype = R_JPEG90;
//			else if (!strcmp(imtype,"IRIZ")) scene->r.imtype = R_IRIZ;
//			else if (!strcmp(imtype,"RAWTGA")) scene->r.imtype = R_RAWTGA;
//			else if (!strcmp(imtype,"AVIRAW")) scene->r.imtype = R_AVIRAW;
//			else if (!strcmp(imtype,"AVIJPEG")) scene->r.imtype = R_AVIJPEG;
//			else if (!strcmp(imtype,"PNG")) scene->r.imtype = R_PNG;
//			else if (!strcmp(imtype,"AVICODEC")) scene->r.imtype = R_AVICODEC;
//			else if (!strcmp(imtype,"QUICKTIME")) scene->r.imtype = R_QUICKTIME;
//			else if (!strcmp(imtype,"BMP")) scene->r.imtype = R_BMP;
//#ifdef WITH_HDR
//			else if (!strcmp(imtype,"HDR")) scene->r.imtype = R_RADHDR;
//#endif
//#ifdef WITH_TIFF
//			else if (!strcmp(imtype,"TIFF")) scene->r.imtype = R_TIFF;
//#endif
//#ifdef WITH_OPENEXR
//			else if (!strcmp(imtype,"EXR")) scene->r.imtype = R_OPENEXR;
//			else if (!strcmp(imtype,"MULTILAYER")) scene->r.imtype = R_MULTILAYER;
//#endif
//			else if (!strcmp(imtype,"MPEG")) scene->r.imtype = R_FFMPEG;
//			else if (!strcmp(imtype,"FRAMESERVER")) scene->r.imtype = R_FRAMESERVER;
//#ifdef WITH_CINEON
//			else if (!strcmp(imtype,"CINEON")) scene->r.imtype = R_CINEON;
//			else if (!strcmp(imtype,"DPX")) scene->r.imtype = R_DPX;
//#endif
//#if WITH_OPENJPEG
//			else if (!strcmp(imtype,"JP2")) scene->r.imtype = R_JP2;
//#endif
//			else printf("\nError: Format from '-F / --render-format' not known or not compiled in this release.\n");
//		}
		return 1;
	} else {
		System.out.printf("\nError: you must specify a format after '-F  / --render-foramt'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_threads = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	if (argc >= 1) {
//		if(G.background) {
//			RE_set_max_threads(atoi(argv[1]));
//		} else {
//			printf("Warning: threads can only be set in background mode\n");
//		}
		return 1;
	} else {
		System.out.printf("\nError: you must specify a number of threads between 0 and 8 '-t  / --threads'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_extension = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	bContext *C = data;
	if (argc >= 1) {
//		if (CTX_data_scene(C)) {
//			Scene *scene= CTX_data_scene(C);
//			if (argv[1][0] == '0') {
//				scene->r.scemode &= ~R_EXTENSION;
//			} else if (argv[1][0] == '1') {
//				scene->r.scemode |= R_EXTENSION;
//			} else {
//				printf("\nError: Use '-x 1 / -x 0' To set the extension option or '--use-extension'\n");
//			}
//		} else {
//			printf("\nError: no blend loaded. order the arguments so '-o ' is after '-x '.\n");
//		}
		return 1;
	} else {
		System.out.printf("\nError: you must specify a path after '- '.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_ge_parameters = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	int a = 0;
//#ifdef WITH_GAMEENGINE
//	SYS_SystemHandle syshandle = *(SYS_SystemHandle*)data;
//#else
//	(void)data;
//#endif
//
///**
//gameengine parameters are automaticly put into system
//-g [paramname = value]
//-g [boolparamname]
//example:
//-g novertexarrays
//-g maxvertexarraysize = 512
//*/
//
//	if(argc >= 1)
//	{
//		const char *paramname = argv[a];
//		/* check for single value versus assignment */
//		if (a+1 < argc && (*(argv[a+1]) == '='))
//		{
//			a++;
//			if (a+1 < argc)
//			{
//				a++;
//				/* assignment */
//#ifdef WITH_GAMEENGINE
//				SYS_WriteCommandLineString(syshandle,paramname,argv[a]);
//#endif
//			}  else
//			{
//				printf("error: argument assignment (%s) without value.\n",paramname);
//				return 0;
//			}
//			/* name arg eaten */
//
//		} else {
//#ifdef WITH_GAMEENGINE
//			SYS_WriteCommandLineInt(syshandle,argv[a],1);
//#endif
//			/* doMipMap */
//			if (!strcmp(argv[a],"nomipmap"))
//			{
//				GPU_set_mipmap(0); //doMipMap = 0;
//			}
//			/* linearMipMap */
//			if (!strcmp(argv[a],"linearmipmap"))
//			{
//				GPU_set_linear_mipmap(1); //linearMipMap = 1;
//			}
//
//
//		} /* if (*(argv[a+1]) == '=') */
//	}

	return a;
}};

public static BA_ArgCallback render_frame = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bContext C = (bContext)data;
	if (bContext.CTX_data_scene(C)!=null) {
//		Main *bmain= CTX_data_main(C);
//		Scene *scene= CTX_data_scene(C);

		if (argc > 1) {
//			Render *re = RE_NewRender(scene->id.name);
//			int frame;
//			ReportList reports;
//
//			switch(*argv[1]) {
//			case '+':
//				frame= scene->r.sfra + atoi(argv[1]+1);
//				break;
//			case '-':
//				frame= (scene->r.efra - atoi(argv[1]+1)) + 1;
//				break;
//			default:
//				frame= atoi(argv[1]);
//				break;
//			}
//
//			BKE_reports_init(&reports, RPT_PRINT);
//
//			frame = MIN2(MAXFRAME, MAX2(MINAFRAME, frame));
//
//			RE_BlenderAnim(re, bmain, scene, NULL, scene->lay, frame, frame, scene->r.frame_step, &reports);
			return 1;
		} else {
			System.out.printf("\nError: frame number must follow '-f / --render-frame'.\n");
			return 0;
		}
	} else {
		System.out.printf("\nError: no blend loaded. cannot use '-f / --render-frame'.\n");
		return 0;
	}
}};

public static BA_ArgCallback render_animation = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bContext C = (bContext)data;
	if (bContext.CTX_data_scene(C)!=null) {
//		Main *bmain= CTX_data_main(C);
//		Scene *scene= CTX_data_scene(C);
//		Render *re= RE_NewRender(scene->id.name);
//		ReportList reports;
//		BKE_reports_init(&reports, RPT_PRINT);
//		RE_BlenderAnim(re, bmain, scene, NULL, scene->lay, scene->r.sfra, scene->r.efra, scene->r.frame_step, &reports);
	} else {
		System.out.printf("\nError: no blend loaded. cannot use '-a'.\n");
	}
	return 0;
}};

public static BA_ArgCallback set_scene = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	if(argc > 1) {
//		bContext *C= data;
//		Scene *sce= set_scene_name(CTX_data_main(C), argv[1]);
//		if(sce) {
//			CTX_data_scene_set(C, sce);
//		}
		return 1;
	} else {
		System.out.printf("\nError: Scene name must follow '-S / --scene'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_start_frame = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bContext C = (bContext)data;
	if (bContext.CTX_data_scene(C)!=null) {
//		Scene *scene= CTX_data_scene(C);
		if (argc > 1) {
//			int frame = atoi(argv[1]);
//			(scene->r.sfra) = CLAMPIS(frame, MINFRAME, MAXFRAME);
			return 1;
		} else {
			System.out.printf("\nError: frame number must follow '-s / --frame-start'.\n");
			return 0;
		}
	} else {
		System.out.printf("\nError: no blend loaded. cannot use '-s / --frame-start'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_end_frame = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bContext C = (bContext)data;
	if (bContext.CTX_data_scene(C)!=null) {
//		Scene *scene= CTX_data_scene(C);
		if (argc > 1) {
//			int frame = atoi(argv[1]);
//			(scene->r.efra) = CLAMPIS(frame, MINFRAME, MAXFRAME);
			return 1;
		} else {
			System.out.printf("\nError: frame number must follow '-e / --frame-end'.\n");
			return 0;
		}
	} else {
		System.out.printf("\nError: no blend loaded. cannot use '-e / --frame-end'.\n");
		return 0;
	}
}};

public static BA_ArgCallback set_skip_frame = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	bContext C = (bContext)data;
	if (bContext.CTX_data_scene(C)!=null) {
//		Scene *scene= CTX_data_scene(C);
		if (argc > 1) {
//			int frame = atoi(argv[1]);
//			(scene->r.frame_step) = CLAMPIS(frame, 1, MAXFRAME);
			return 1;
		} else {
			System.out.printf("\nError: number of frames to step must follow '-j / --frame-jump'.\n");
			return 0;
		}
	} else {
		System.out.printf("\nError: no blend loaded. cannot use '-j / --frame-jump'.\n");
		return 0;
	}
}};

///* macro for ugly context setup/reset */
//#ifdef WITH_PYTHON
//#define BPY_CTX_SETUP(_cmd) \
//{ \
//	wmWindowManager *wm= CTX_wm_manager(C); \
//	wmWindow *prevwin= CTX_wm_window(C); \
//	Scene *prevscene= CTX_data_scene(C); \
//	if(wm->windows.first) { \
//		CTX_wm_window_set(C, wm->windows.first); \
//		_cmd; \
//		CTX_wm_window_set(C, prevwin); \
//	} \
//	else { \
//		fprintf(stderr, "Python script \"%s\" running with missing context data.\n", argv[1]); \
//		_cmd; \
//	} \
//	CTX_data_scene_set(C, prevscene); \
//} \
//
//#endif /* WITH_PYTHON */

public static BA_ArgCallback run_python = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//#ifdef WITH_PYTHON
//	bContext *C = data;

	/* workaround for scripts not getting a bpy.context.scene, causes internal errors elsewhere */
	if (argc > 1) {
//		/* Make the path absolute because its needed for relative linked blends to be found */
//		char filename[FILE_MAXDIR + FILE_MAXFILE];
//		BLI_strncpy(filename, argv[1], sizeof(filename));
//		BLI_path_cwd(filename);
//
//		BPY_CTX_SETUP(BPY_filepath_exec(C, filename, NULL))

		return 1;
	} else {
		System.out.printf("\nError: you must specify a Python script after '-P / --python'.\n");
		return 0;
	}
//#else
//	(void)argc; (void)argv; (void)data; /* unused */
//	printf("This blender was built without python support\n");
//	return 0;
//#endif /* WITH_PYTHON */
}};

public static BA_ArgCallback run_python_console = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//#ifdef WITH_PYTHON
//	bContext *C = data;
//
//	BPY_CTX_SETUP(BPY_string_exec(C, "__import__('code').interact()"))

	return 0;
//#else
//	(void)argv; (void)data; /* unused */
//	printf("This blender was built without python support\n");
//	return 0;
//#endif /* WITH_PYTHON */
}};

public static BA_ArgCallback set_addons = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
	/* workaround for scripts not getting a bpy.context.scene, causes internal errors elsewhere */
	if (argc > 1) {
//#ifdef WITH_PYTHON
//		const int slen= strlen(argv[1]) + 128;
//		char *str= malloc(slen);
//		bContext *C= data;
//		BLI_snprintf(str, slen, "[__import__('addon_utils').enable(i, default_set=False) for i in '%s'.split(',')]", argv[1]);
//		BPY_CTX_SETUP(BPY_string_exec(C, str));
//		free(str);
//#else
//		(void)argv; (void)data; /* unused */
//#endif /* WITH_PYTHON */
		return 1;
	}
	else {
		System.out.printf("\nError: you must specify a comma separated list after '--addons'.\n");
		return 0;
	}
}};

public static BA_ArgCallback load_file = new BA_ArgCallback() {
public int call(int argc, String[] argv, int offset, Object data)
{
//	bContext *C = data;
//
//	/* Make the path absolute because its needed for relative linked blends to be found */
//	char filename[FILE_MAXDIR + FILE_MAXFILE];
//	BLI_strncpy(filename, argv[0], sizeof(filename));
//	BLI_path_cwd(filename);
//
//	if (G.background) {
//		int retval = BKE_read_file(C, filename, NULL);
//
//		/*we successfully loaded a blend file, get sure that
//		pointcache works */
//		if (retval != BKE_READ_FILE_FAIL) {
//			wmWindowManager *wm= CTX_wm_manager(C);
//
//			/* special case, 2.4x files */
//			if(wm==NULL && CTX_data_main(C)->wm.first==NULL) {
//				extern void wm_add_default(bContext *C);
//
//				/* wm_add_default() needs the screen to be set. */
//				CTX_wm_screen_set(C, CTX_data_main(C)->screen.first);
//				wm_add_default(C);
//			}
//
//			CTX_wm_manager_set(C, NULL); /* remove wm to force check */
//			WM_check(C);
//			G.relbase_valid = 1;
//			if (CTX_wm_manager(C) == NULL) CTX_wm_manager_set(C, wm); /* reset wm */
//
//			DAG_on_visible_update(CTX_data_main(C), TRUE);
//		}
//
//		/* WM_read_file() runs normally but since we're in background mode do here */
//#ifdef WITH_PYTHON
//		/* run any texts that were loaded in and flagged as modules */
//		BPY_driver_reset();
//		BPY_modules_load_user(C);
//#endif
//
//		/* happens for the UI on file reading too (huh? (ton))*/
//	// XXX			BKE_reset_undo();
//	//				BKE_write_undo("original");	/* save current state */
//	} else {
//		/* we are not running in background mode here, but start blender in UI mode with
//		   a file - this should do everything a 'load file' does */
//		ReportList reports;
//		BKE_reports_init(&reports, RPT_PRINT);
//		WM_read_file(C, filename, &reports);
//		BKE_reports_clear(&reports);
//	}
//
//	G.file_loaded = 1;

	return 0;
}};

//static void setupArguments(bContext C, bArgs ba, SYS_SystemHandle syshandle)
static void setupArguments(bContext C, bArgs ba, Object syshandle)
{
	final String output_doc = "<path>"+
		"\n\tSet the render path and file name."+
		"\n\tUse // at the start of the path to"+
		"\n\t\trender relative to the blend file."+
		"\n\tThe # characters are replaced by the frame number, and used to define zero padding."+
		"\n\t\tani_##_test.png becomes ani_01_test.png"+
		"\n\t\ttest-######.png becomes test-000001.png"+
		"\n\t\tWhen the filename does not contain #, The suffix #### is added to the filename"+
		"\n\tThe frame number will be added at the end of the filename."+
		"\n\t\teg: blender -b foobar.blend -o //render_ -F PNG -x 1 -a"+
		"\n\t\t//render_ becomes //render_####, writing frames as //render_0001.png//";

	final String format_doc = "<format>"+
		"\n\tSet the render format, Valid options are..."+
		"\n\t\tTGA IRIS JPEG MOVIE IRIZ RAWTGA"+
		"\n\t\tAVIRAW AVIJPEG PNG BMP FRAMESERVER"+
		"\n\t(formats that can be compiled into blender, not available on all systems)"+
		"\n\t\tHDR TIFF EXR MULTILAYER MPEG AVICODEC QUICKTIME CINEON DPX DDS";

	final String playback_doc = "<options> <file(s)>"+
		"\n\tPlayback <file(s)>, only operates this way when not running in background."+
		"\n\t\t-p <sx> <sy>\tOpen with lower left corner at <sx>, <sy>"+
		"\n\t\t-m\t\tRead from disk (Don't buffer)"+
		"\n\t\t-f <fps> <fps-base>\t\tSpecify FPS to start with"+
		"\n\t\t-j <frame>\tSet frame step to <frame>";

	final String game_doc = "Game Engine specific options"+
		"\n\t-g fixedtime\t\tRun on 50 hertz without dropping frames"+
		"\n\t-g vertexarrays\t\tUse Vertex Arrays for rendering (usually faster)"+
		"\n\t-g nomipmap\t\tNo Texture Mipmapping"+
		"\n\t-g linearmipmap\t\tLinear Texture Mipmapping instead of Nearest (default)";

	final String debug_doc = "\n\tTurn debugging on\n"+
		"\n\t* Prints every operator call and their arguments"+
		"\n\t* Disables mouse grab (to interact with a debugger in some cases)"+
		"\n\t* Keeps python sys.stdin rather then setting it to None";

	//BLI_argsAdd(ba, pass, short_arg, long_arg, doc, cb, C);

	/* end argument processing after -- */
	Args.BLI_argsAdd(ba, -1, "--", null, "\n\tEnds option processing, following arguments passed unchanged. Access via python's sys.argv", end_arguments, null);

	/* first pass: background mode, disable python and commands that exit after usage */
	Args.BLI_argsAdd(ba, 1, "-h", "--help", "\n\tPrint this help text and exit", print_help, ba);
	/* Windows only */
	Args.BLI_argsAdd(ba, 1, "/?", null, "\n\tPrint this help text and exit (windows only)", print_help, ba);

	Args.BLI_argsAdd(ba, 1, "-v", "--version", "\n\tPrint Blender version and exit", print_version, null);
	
	/* only to give help message */
//#ifndef WITH_PYTHON_SECURITY /* default */
final String PY_ENABLE_AUTO= ", (default)";
final String PY_DISABLE_AUTO= "";
//#else
//#  define 	PY_ENABLE_AUTO ""
//#  define 	PY_DISABLE_AUTO ", (compiled as non-standard default)"
//#endif

	Args.BLI_argsAdd(ba, 1, "-y", "--enable-autoexec", "\n\tEnable automatic python script execution"+PY_ENABLE_AUTO, enable_python, null);
	Args.BLI_argsAdd(ba, 1, "-Y", "--disable-autoexec", "\n\tDisable automatic python script execution (pydrivers, pyconstraints, pynodes)"+PY_DISABLE_AUTO, disable_python, null);

//#undef PY_ENABLE_AUTO
//#undef PY_DISABLE_AUTO
	
	Args.BLI_argsAdd(ba, 1, "-b", "--background", "<file>\n\tLoad <file> in background (often used for UI-less rendering)", background_mode, null);

	Args.BLI_argsAdd(ba, 1, "-a", null, playback_doc, playback_mode, null);

	Args.BLI_argsAdd(ba, 1, "-d", "--debug", debug_doc, debug_mode, ba);
	Args.BLI_argsAdd(ba, 1, null, "--debug-fpe", "\n\tEnable floating point exceptions", set_fpe, null);

	Args.BLI_argsAdd(ba, 1, null, "--factory-startup", "\n\tSkip reading the "+"BLENDER_STARTUP_FILE"+" in the users home directory", set_factory_startup, null);

	/* TODO, add user env vars? */
	Args.BLI_argsAdd(ba, 1, null, "--env-system-config",		"\n\tSet the "+"BLENDER_SYSTEM_CONFIG"+" environment variable", set_env, null);
	Args.BLI_argsAdd(ba, 1, null, "--env-system-datafiles",	"\n\tSet the "+"BLENDER_SYSTEM_DATAFILES"+" environment variable", set_env, null);
	Args.BLI_argsAdd(ba, 1, null, "--env-system-scripts",	"\n\tSet the "+"BLENDER_SYSTEM_SCRIPTS"+" environment variable", set_env, null);
	Args.BLI_argsAdd(ba, 1, null, "--env-system-plugins",	"\n\tSet the "+"BLENDER_SYSTEM_PLUGINS"+" environment variable", set_env, null);
	Args.BLI_argsAdd(ba, 1, null, "--env-system-python",		"\n\tSet the "+"BLENDER_SYSTEM_PYTHON"+" environment variable", set_env, null);

	/* second pass: custom window stuff */
	Args.BLI_argsAdd(ba, 2, "-p", "--window-geometry", "<sx> <sy> <w> <h>\n\tOpen with lower left corner at <sx>, <sy> and width and height as <w>, <h>", prefsize, null);
	Args.BLI_argsAdd(ba, 2, "-w", "--window-border", "\n\tForce opening with borders (default)", with_borders, null);
	Args.BLI_argsAdd(ba, 2, "-W", "--window-borderless", "\n\tForce opening without borders", without_borders, null);
	Args.BLI_argsAdd(ba, 2, "-con", "--start-console", "\n\tStart with the console window open (ignored if -b is set)", start_with_console, null);
	Args.BLI_argsAdd(ba, 2, "-R", null, "\n\tRegister .blend extension, then exit (Windows only)", register_extension, null);
	Args.BLI_argsAdd(ba, 2, "-r", null, "\n\tSilently register .blend extension, then exit (Windows only)", register_extension, ba);

	/* third pass: disabling things and forcing settings */
	Args.BLI_argsAddCase(ba, 3, "-nojoystick", 1, null, 0, "\n\tDisable joystick support", no_joystick, syshandle);
	Args.BLI_argsAddCase(ba, 3, "-noglsl", 1, null, 0, "\n\tDisable GLSL shading", no_glsl, null);
	Args.BLI_argsAddCase(ba, 3, "-noaudio", 1, null, 0, "\n\tForce sound system to None", no_audio, null);
	Args.BLI_argsAddCase(ba, 3, "-setaudio", 1, null, 0, "\n\tForce sound system to a specific device\n\tNULL SDL OPENAL JACK", set_audio, null);

	/* fourth pass: processing arguments */
	Args.BLI_argsAdd(ba, 4, "-g", null, game_doc, set_ge_parameters, syshandle);
	Args.BLI_argsAdd(ba, 4, "-f", "--render-frame", "<frame>\n\tRender frame <frame> and save it.\n\t+<frame> start frame relative, -<frame> end frame relative.", render_frame, C);
	Args.BLI_argsAdd(ba, 4, "-a", "--render-anim", "\n\tRender frames from start to end (inclusive)", render_animation, C);
	Args.BLI_argsAdd(ba, 4, "-S", "--scene", "<name>\n\tSet the active scene <name> for rendering", set_scene, C);
	Args.BLI_argsAdd(ba, 4, "-s", "--frame-start", "<frame>\n\tSet start to frame <frame> (use before the -a argument)", set_start_frame, C);
	Args.BLI_argsAdd(ba, 4, "-e", "--frame-end", "<frame>\n\tSet end to frame <frame> (use before the -a argument)", set_end_frame, C);
	Args.BLI_argsAdd(ba, 4, "-j", "--frame-jump", "<frames>\n\tSet number of frames to step forward after each rendered frame", set_skip_frame, C);
	Args.BLI_argsAdd(ba, 4, "-P", "--python", "<filename>\n\tRun the given Python script (filename or Blender Text)", run_python, C);
	Args.BLI_argsAdd(ba, 4, null, "--python-console", "\n\tRun blender with an interactive console", run_python_console, C);
	Args.BLI_argsAdd(ba, 4, null, "--addons", "\n\tComma separated list of addons (no spaces)", set_addons, C);

	Args.BLI_argsAdd(ba, 4, "-o", "--render-output", output_doc, set_output, C);
	Args.BLI_argsAdd(ba, 4, "-E", "--engine", "<engine>\n\tSpecify the render engine\n\tuse -E help to list available engines", set_engine, C);

	Args.BLI_argsAdd(ba, 4, "-F", "--render-format", format_doc, set_image_type, C);
	Args.BLI_argsAdd(ba, 4, "-t", "--threads", "<threads>\n\tUse amount of <threads> for rendering in background\n\t[1-"+"BLENDER_MAX_THREADS"+"], 0 for systems processor count.", set_threads, null);
	Args.BLI_argsAdd(ba, 4, "-x", "--use-extension", "<bool>\n\tSet option to add the file extension to the end of the file", set_extension, C);

}

public static void main(String[] argv) {
	try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception ex) { System.out.println(ex); };
	
    int argc = argv.length;
//	SYS_SystemHandle syshandle;
//    Object syshandle;
	bContext C= bContext.CTX_create();
	bContext.CTX_wm_manager_close_set(C, WmInitExit.WM_exit);
//	bArgs ba;

//#ifdef WITH_PYTHON_MODULE
//#undef main
//#endif

//#ifdef WITH_BINRELOC
//	br_init( NULL );
//#endif

//	setCallbacks();
//#ifdef __APPLE__
//		/* patch to ignore argument finder gives us (pid?) */
//	if (argc==2 && strncmp(argv[1], "-psn_", 5)==0) {
//		extern int GHOST_HACK_getFirstFile(char buf[]);
//		static char firstfilebuf[512];
//
//		argc= 1;
//
//		if (GHOST_HACK_getFirstFile(firstfilebuf)) {
//			argc= 2;
//			argv[1]= firstfilebuf;
//		}
//	}
//
//#endif

//#ifdef __FreeBSD__
//	fpsetmask(0);
//#endif

	// copy path to executable in bprogname. playanim and creting runtimes
	// need this.

//	BLI_where_am_i(bprogname, sizeof(bprogname), argv[0]);
	
//#ifdef BUILD_DATE	
//	strip_quotes(build_date);
//	strip_quotes(build_time);
//	strip_quotes(build_rev);
//	strip_quotes(build_platform);
//	strip_quotes(build_type);
//	strip_quotes(build_cflags);
//	strip_quotes(build_cxxflags);
//	strip_quotes(build_linkflags);
//	strip_quotes(build_system);
//#endif

//	BLI_threadapi_init();

	RnaAccess.RNA_init();
//	RE_engines_init();

		/* Hack - force inclusion of the plugin api functions,
		 * see blenpluginapi:pluginapi.c
		 */
//	pluginapi_force_ref();

//	init_nodesystem();
	
	Blender.initglobals();	/* blender.c */

//	IMB_init();

//#ifdef WITH_GAMEENGINE
//	syshandle = SYS_GetSystem();
//#else
//	syshandle= null;
//#endif

	/* first test for background */
//	ba = Args.BLI_argsInit(argc, argv); /* skip binary path */
//	setupArguments(C, ba, syshandle);
//
//	Args.BLI_argsParse(ba, 1, null, null);

//#ifdef __sgi
//	setuid(getuid()); /* end superuser */
//#endif

//#ifdef WITH_PYTHON_MODULE
//	G.background= 1; /* python module mode ALWAYS runs in background mode (for now) */
//#else
//	/* for all platforms, even windos has it! */
//	if(G.background) signal(SIGINT, blender_esc);	/* ctrl c out bg render */
//#endif

	/* background render uses this font too */
//	BKE_font_register_builtin(datatoc_Bfont, datatoc_Bfont_size);

	/* Initialiaze ffmpeg if built in, also needed for bg mode if videos are
	   rendered via ffmpeg */
//	sound_init_once();
	
//	init_def_material();

	if(G.background==0) {
//		Args.BLI_argsParse(ba, 2, null, null);
//		Args.BLI_argsParse(ba, 3, null, null);

		WmInitExit.WM_init(C, argc, argv);

		/* this is properly initialized with user defs, but this is default */
//		BLI_where_is_temp(btempdir, FILE_MAX, 1); /* call after loading the startup.blend so we can read U.tempdir */

//#ifndef DISABLE_SDL
//	BLI_setenv("SDL_VIDEODRIVER", "dummy");
//#endif
	}
//	else {
//		Args.BLI_argsParse(ba, 3, null, null);
//
//		WmInitExit.WM_init(C, argc, argv);
//
//		BLI_where_is_temp(btempdir, FILE_MAX, 0); /* call after loading the startup.blend so we can read U.tempdir */
//	}
//#ifdef WITH_PYTHON
	/**
	 * NOTE: the U.pythondir string is NULL until WM_init() is executed,
	 * so we provide the BPY_ function below to append the user defined
	 * pythondir to Python's sys.path at this point.  Simply putting
	 * WM_init() before BPY_python_start() crashes Blender at startup.
	 * Update: now this function also inits the bpymenus, which also depend
	 * on U.pythondir.
	 */

	// TODO - U.pythondir
//#else
//	printf("\n* WARNING * - Blender compiled without Python!\nthis is not intended for typical usage\n\n");
//#endif
	
//	CTX_py_init_set(C, 1);
	Wm.WM_keymap_init(C);

	/* OK we are ready for it */
//	Args.BLI_argsParse(ba, 4, load_file, C);

//	Args.BLI_argsFree(ba);

//#ifdef WITH_PYTHON_MODULE
//	return 0; /* keep blender in background mode running */
//#endif

//	if(G.background) {
//		/* actually incorrect, but works for now (ton) */
//		WM_exit(C);
//	}
//
//	else {
//		if((G.fileflags & G_FILE_AUTOPLAY) && (G.f & G_SCRIPT_AUTOEXEC))
//		{
//			if(WM_init_game(C))
//				return 0;
//		}
//		else if(!G.file_loaded)
//			WM_init_splash(C);
//	}
//
//	WM_main(C);
//
//
//	/*XXX if (scr_init==0) {
//		main_init_screen();
//	}
//	
//	screenmain();*/ /* main display loop */
//
//	return 0;
}

//static void error_cb(char *err)
//{
//
//	printf("%s\n", err);	/* XXX do this in WM too */
//}
//
//static void mem_error_cb(char *errorStr)
//{
//	fputs(errorStr, stderr);
//	fflush(stderr);
//}
//
//static void setCallbacks(void)
//{
//	/* Error output from the alloc routines: */
//	MEM_set_error_callback(mem_error_cb);
//
//
//	/* BLI_blenlib: */
//
//	BLI_setErrorCallBack(error_cb); /* */
//// XXX	BLI_setInterruptCallBack(blender_test_break);
//
//}
}