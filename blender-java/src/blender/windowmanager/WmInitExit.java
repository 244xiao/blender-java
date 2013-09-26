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

//#include <stdlib.h>

import org.python.core.PySystemState;

import blender.blenkernel.bContext;
import blender.editors.screen.ScreenEdit;
import blender.editors.space_api.SpaceTypeUtil;
import blender.editors.uinterface.UI;
import blender.editors.util.EdUtil;
import blender.makesdna.sdna.wmWindow;
import blender.python.BpyInterface;
import static blender.blenkernel.Blender.G;

//#include <stdio.h>
//#include <string.h>
//
//#include "MEM_guardedalloc.h"
//#include "MEM_CacheLimiterC-Api.h"
//
//#include "IMB_imbuf_types.h"
//#include "IMB_imbuf.h"
//
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_sound_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "BKE_blender.h"
//#include "BKE_context.h"
//#include "BKE_curve.h"
//#include "BKE_displist.h"
//#include "BKE_DerivedMesh.h"
//#include "BKE_font.h"
//#include "BKE_global.h"
//#include "BKE_library.h"
//#include "BKE_mball.h"
//#include "BKE_report.h"
//#include "BKE_utildefines.h"
//#include "BKE_packedFile.h"
//
//#include "BLI_blenlib.h"
//
//#include "RE_pipeline.h"		/* RE_ free stuff */
//
//#ifndef DISABLE_PYTHON
//#include "BPY_extern.h"
//#endif
//
//#include "SYS_System.h"
//
//#include "RNA_define.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "wm_cursors.h"
//#include "wm_event_system.h"
//#include "wm.h"
//#include "wm_files.h"
//#include "wm_window.h"
//
//#include "ED_armature.h"
//#include "ED_keyframing.h"
//#include "ED_node.h"
//#include "ED_previewrender.h"
//#include "ED_space_api.h"
//#include "ED_screen.h"
//#include "ED_util.h"
//
//#include "UI_interface.h"
//#include "BLF_api.h"
//
//#include "GPU_extensions.h"
//#include "GPU_draw.h"
//

public class WmInitExit {
//
//
///* XXX */
//static void sound_init_listener(void)
//{
//	G.listener = MEM_callocN(sizeof(bSoundListener), "soundlistener");
//	G.listener.gain = 1.0;
//	G.listener.dopplerfactor = 1.0;
//	G.listener.dopplervelocity = 340.29f;
//}
//
//
//static void wm_init_reports(bContext *C)
//{
//	BKE_reports_init(CTX_wm_reports(C), RPT_STORE);
//}
//static void wm_free_reports(bContext *C)
//{
//	BKE_reports_clear(CTX_wm_reports(C));
//}

/* only called once, for startup */
public static void WM_init(bContext C, int argc, String[] argv)
{
	// this init method is taking too long
	
	if (G.background==0) {
		WmWindow.wm_ghost_init(C);	/* note: it assigns C to ghost! */
//		wm_init_cursor_data();
	}
//	GHOST_CreateSystemPaths();
	WmOperators.wm_operatortype_init();
	
//	set_free_windowmanager_cb(wm_close_and_free);	/* library.c */
//	set_blender_test_break_cb(wm_window_testbreak); /* blender.c */
//	DAG_editors_update_cb(ED_render_id_flush_update); /* depsgraph.c */
	
	SpaceTypeUtil.ED_spacetypes_init();	/* editors/space_api/spacetype.c */
	
//	ED_file_init();			/* for fsmenu */
//	ED_init_node_butfuncs();	
	
//	BLF_init(11, U.dpi); /* Please update source/gamengine/GamePlayer/GPG_ghost.cpp if you change this */
//	BLF_lang_init();
	/* get the default database, plus a wm */
//	WmFiles.WM_read_homefile(C, null, G.factory_startup);
	WmFiles.WM_read_homefile.run(C, null, null);

	/* note: there is a bug where python needs initializing before loading the
	 * startup.blend because it may contain PyDrivers. It also needs to be after
	 * initializing space types and other internal data.
	 *
	 * However cant redo this at the moment. Solution is to load python
	 * before WM_read_homefile() or make py-drivers check if python is running.
	 * Will try fix when the crash can be repeated. - campbell. */

//#ifdef WITH_PYTHON
//	BPY_context_set(C); /* necessary evil */
//	BPY_python_start(argc, argv);
	BpyInterface.BPY_start_python(argc, argv);

//	BPY_driver_reset();
//	BPY_modules_load_user(C);
//#else
//	(void)argc; /* unused */
//	(void)argv; /* unused */
//#endif

//	if (!G.background && !wm_start_with_console)
//		GHOST_toggleConsole(3);

//	wm_init_reports(C); /* reports cant be initialized before the wm */

	if (G.background==0) {
//		GPU_extensions_init();
//		GPU_set_mipmap(!(U.gameflags & USER_DISABLE_MIPMAP));
	
		UI.UI_init();
	}
	
//	clear_matcopybuf();
//	ED_render_clear_mtex_copybuf();

	//	glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		
//	ED_preview_init_dbase();
	
//	G.ndofdevice = -1;	/* XXX bad initializer, needs set otherwise buttons show! */
	
//	WM_read_history();

	/* allow a path of "", this is what happens when making a new file */
	/*
	if(G.main->name[0] == 0)
		BLI_make_file_string("/", G.main->name, BLI_getDefaultDocumentFolder(), "untitled.blend");
	*/

//	BLI_strncpy(G.lib, G.main->name, FILE_MAX);
	
	Wm.initialized = true;
}

///* free strings of open recent files */
//static void free_openrecent(void)
//{
//	struct RecentFile *recent;
//
//	for(recent = G.recent_files.first; recent; recent=recent.next)
//		MEM_freeN(recent.filename);
//
//	BLI_freelistN(&(G.recent_files));
//}
//
//
///* bad stuff*/
//
//extern ListBase editelems;
//extern wchar_t *copybuf;
//extern wchar_t *copybufinfo;
//
//	// XXX copy/paste buffer stuff...
//extern void free_anim_copybuf();
//extern void free_posebuf();

/* called in creator.c even... tsk, split this! */
public static bContext.bContextWMCloseCallback WM_exit = new bContext.bContextWMCloseCallback() {
public void run(bContext C)
{
	wmWindow win;

	/* first wrap up running stuff, we assume only the active WM is running */
	/* modal handlers are on window level freed, others too? */
	/* note; same code copied in wm_files.c */
	if(C!=null && bContext.CTX_wm_manager(C)!=null) {

//		WM_jobs_stop_all(CTX_wm_manager(C));

		for(win= (wmWindow)bContext.CTX_wm_manager(C).windows.first; win!=null; win= win.next) {

			bContext.CTX_wm_window_set(C, win);	/* needed by operator close callbacks */
			WmEventSystem.WM_event_remove_handlers(C, win.handlers);
			ScreenEdit.ED_screen_exit(C, win, win.screen);
		}
	}
	WmOperators.wm_operatortype_free();

	/* all non-screen and non-space stuff editors did, like editmode */
	if(C!=null)
		EdUtil.ED_editors_exit(C);

	//XXX
	//BIF_GlobalReebFree();
	//BIF_freeRetarget();
//	BIF_freeTemplates(C);

//	free_ttfont(); /* bke_font.h */

//	free_openrecent();

//	BKE_freecubetable();

//	if (G.background == 0)
//		sound_end_all_sounds();


	/* before free_blender so py's gc happens while library still exists */
	/* needed at least for a rare sigsegv that can happen in pydrivers */
//#ifndef DISABLE_PYTHON
//	BPY_end_python();
//#endif

//	fastshade_free_render();	/* shaded view */
//	ED_preview_free_dbase();	/* frees a Main dbase, before free_blender! */
//	wm_free_reports(C);			/* before free_blender! - since the ListBases get freed there */
//	free_blender();				/* blender.c, does entire library and spacetypes */
	//free_matcopybuf();
//	free_anim_copybuf();
//	free_posebuf();
	//free_vertexpaint();
	//free_imagepaint();

	//fsmenu_free();

//	BLF_exit();

//	RE_FreeAllRender();
//	RE_engines_exit();

	//free_txt_data();

	//sound_exit_audio();
//	if(G.listener) MEM_freeN(G.listener);


//	libtiff_exit();

//#ifdef WITH_QUICKTIME
//	quicktime_exit();
//#endif
//
//	if (!G.background) {
//// XXX		UI_filelist_free_icons();
//	}
//
//	GPU_extensions_exit();
//
////	if (copybuf) MEM_freeN(copybuf);
////	if (copybufinfo) MEM_freeN(copybufinfo);
//
//	BKE_undo_save_quit();	// saves quit.blend if global undo is on
//	BKE_reset_undo();
//
//	ED_file_exit(); /* for fsmenu */
//
//	UI_exit();
//	BKE_userdef_free();
//
//	RNA_exit();
//
//	CTX_free(C);
//
//	if(MEM_get_memory_blocks_in_use()!=0) {
//		printf("Error Totblock: %d\n", MEM_get_memory_blocks_in_use());
//		MEM_printmemlist();
//	}
////	delete_autosave();

	System.out.printf("\nBlender quit\n");

//#ifdef WIN32
//	/* ask user to press enter when in debug mode */
//	if(G.f & G_DEBUG) {
//		printf("press enter key to exit...\n\n");
//		getchar();
//	}
//#endif
//
//
//	SYS_DeleteSystem(SYS_GetSystem());

	new Thread(new Runnable() { public void run() { System.exit(G.afbreek==1 ? 1 : 0); } }).start();
}
};

}
