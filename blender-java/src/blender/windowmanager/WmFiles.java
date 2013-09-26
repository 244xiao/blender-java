/**
 * $Id: wm_files.c
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
 * Contributor(s): Blender Foundation 2007
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.GL2;

import java.io.File;
import java.net.URL;

import resources.ResourceAnchor;
import blender.blenkernel.Blender;
import blender.blenkernel.Global;
import blender.blenkernel.bContext;
import blender.blenkernel.LibraryUtil;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.blenlib.Util;
import blender.editors.screen.ScreenEdit;
import blender.editors.uinterface.UI;
import blender.editors.util.EdUtil;
import blender.ghost.GhostAPI;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.ReportList;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmSubWindow.wmSubWindow;
import blender.windowmanager.WmTypes.wmEvent;

public class WmFiles {

/* To be able to read files without windows closing, opening, moving
   we try to prepare for worst case:
   - active window gets active screen from file
   - restoring the screens from non-active windows
   Best case is all screens match, in that case they get assigned to proper window
*/
public static void wm_window_match_init(bContext C, ListBase<wmWindowManager>[] wmlist)
{
	wmWindowManager wm= G.main.wm.first;
	wmWindow win, active_win;

	wmlist[0]= G.main.wm.copy();
	G.main.wm.first= G.main.wm.last= null;
	
	active_win = bContext.CTX_wm_window(C);

	/* first wrap up running stuff */
	/* code copied from wm_init_exit.c */
	for(wm= wmlist[0].first; wm!=null; wm= (wmWindowManager)wm.id.next) {

//		WM_jobs_stop_all(wm);

		for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {

			bContext.CTX_wm_window_set(C, win);	/* needed by operator close callbacks */
			WmEventSystem.WM_event_remove_handlers(C, win.handlers);
//			WmEventSystem.WM_event_remove_handlers(C, win.modalhandlers);
			ScreenEdit.ED_screen_exit(C, win, win.screen);
		}
	}
	
	/* reset active window */
	bContext.CTX_wm_window_set(C, active_win);

	EdUtil.ED_editors_exit(C);

return;
//	if(wm==NULL) return;
//	if(G.fileflags & G_FILE_NO_UI) return;
//
//	/* we take apart the used screens from non-active window */
//	for(win= wm.windows.first; win; win= win.next) {
//		BLI_strncpy(win.screenname, win.screen.id.name, MAX_ID_NAME);
//		if(win!=wm.winactive) {
//			BLI_remlink(&G.main.screen, win.screen);
//			//BLI_addtail(screenbase, win.screen);
//		}
//	}
}

/* match old WM with new, 4 cases:
  1- no current wm, no read wm: make new default
  2- no current wm, but read wm: that's OK, do nothing
  3- current wm, but not in file: try match screen names
  4- current wm, and wm in file: try match ghostwin
*/

public static void wm_window_match_do(bContext C, ListBase<wmWindowManager> oldwmlist)
{
	wmWindowManager oldwm, wm;
	wmWindow oldwin, win;

	/* cases 1 and 2 */
	if(oldwmlist.first==null) {
		if(G.main.wm.first!=null) { /* nothing todo */
//			System.out.println("2- no current wm, but read wm: that's OK, do nothing");
		}
		else {
			Wm.wm_add_default(C, (wmWindowManager)LibraryUtil.alloc_libblock(bContext.CTX_data_main(C).wm, DNA_ID.ID_WM, StringUtil.toCString("WinMan"),0));
//			System.out.println("1- no current wm, no read wm: make new default");
		}
	}
	else {
		/* cases 3 and 4 */

		/* we've read file without wm..., keep current one entirely alive */
		if(G.main.wm.first==null) {
//			System.out.println("3- current wm, but not in file: try match screen names");
			/* when loading without UI, no matching needed */
			if((G.fileflags & Global.G_FILE_NO_UI)==0) {
				bScreen screen= bContext.CTX_wm_screen(C);
	
				/* match oldwm to new dbase, only old files */
				for(wm= oldwmlist.first; wm!=null; wm= (wmWindowManager)wm.id.next) {
	
					for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
						/* all windows get active screen from file */
						if(screen.winid==0)
							win.screen= screen;
						else
							win.screen= ScreenEdit.ED_screen_duplicate(win, screen);
	
						StringUtil.BLI_strncpy(win.screenname,0, win.screen.id.name,2, 21);
						win.screen.winid= (short)win.winid;
					}
				}
			}

			G.main.wm= oldwmlist.copy();

			/* screens were read from file! */
			ScreenEdit.ED_screens_initialize((GL2)GLU.getCurrentGL(), G.main.wm.first);
//			ScreenEdit.ED_screens_initialize(G.main.wm.first);
		}
		else {
//			System.out.println("4- current wm, and wm in file: try match ghostwin");
			/* what if old was 3, and loaded 1? */
			/* this code could move to setup_appdata */
			oldwm= oldwmlist.first;
			wm= G.main.wm.first;

			/* ensure making new keymaps and set space types */
			wm.initialized= 0;
			wm.winactive= null;

			/* only first wm in list has ghostwins */
			for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
				for(oldwin= (wmWindow)oldwm.windows.first; oldwin!=null; oldwin= oldwin.next) {

					if(oldwin.winid == win.winid) {
//						System.out.println("wm_window_match_do: oldwin.winid == win.winid");
						win.ghostwin= oldwin.ghostwin;
						win.active= oldwin.active;
						if(win.active!=0)
							wm.winactive= win;
						
						if(G.background==0) /* file loading in background mode still calls this */
							GhostAPI.GHOST_SetWindowUserData(win.ghostwin, win);	/* pointer back */

						oldwin.ghostwin= null;

						win.eventstate= oldwin.eventstate;
//						System.out.println("wm_window_match_do: win.eventstate= oldwin.eventstate;");
//						System.out.println("wm_window_match_do: win.eventstate 1: "+win.eventstate);
						oldwin.eventstate= null;
//						System.out.println("wm_window_match_do: win.eventstate 2: "+win.eventstate);

						/* ensure proper screen rescaling */
						win.sizex= oldwin.sizex;
						win.sizey= oldwin.sizey;
						win.posx= oldwin.posx;
						win.posy= oldwin.posy;
					}
				}
			}
			Wm.wm_close_and_free_all(C, oldwmlist);
		}
	}
}

/* in case UserDef was read, we re-initialize all, and do versioning */
public static void wm_init_userdef(bContext C)
{
	UI.UI_init_userdef();
//	MEM_CacheLimiter_set_maximum(U.memcachelimit * 1024 * 1024);
//	sound_init(CTX_data_main(C));
//
//	/* needed so loading a file from the command line respects user-pref [#26156] */
//	if(U.flag & USER_FILENOUI)	G.fileflags |= G_FILE_NO_UI;
//	else						G.fileflags &= ~G_FILE_NO_UI;
//
//	/* set the python auto-execute setting from user prefs */
//	/* enabled by default, unless explicitly enabled in the command line which overrides */
//	if((G.f & G_SCRIPT_OVERRIDE_PREF) == 0) {
//		if ((U.flag & USER_SCRIPT_AUTOEXEC_DISABLE) == 0) G.f |=  G_SCRIPT_AUTOEXEC;
//		else											  G.f &= ~G_SCRIPT_AUTOEXEC;
//	}
//	if(U.tempdir[0]) BLI_where_is_temp(btempdir, FILE_MAX, 1);
}

public static void WM_read_file(bContext C, String name, ReportList reports)
{
	int retval;

//	/* first try to append data from exotic file formats... */
//	/* it throws error box when file doesnt exist and returns -1 */
//	/* note; it should set some error message somewhere... (ton) */
//	retval= BKE_read_exotic(CTX_data_scene(C), name);
	retval= 0;

	/* we didn't succeed, now try to read Blender file */
	if (retval== 0) {
//		System.out.println("WM_read_file: "+name);
//		ListBase[] wmbase = {new ListBase<wmWindowManager>()};
		ListBase<wmWindowManager>[] wmbase = new ListBase[1];
		wmbase[0] = new ListBase<wmWindowManager>();

		/* put aside screens to match with persistant windows later */
		/* also exit screens and editors */
		WmFiles.wm_window_match_init(C, wmbase);
		
		wmWindow oldwin = (wmWindow)wmbase[0].first.windows.first;
//		System.out.println("WM_read_file: oldwin.eventstate 1: "+oldwin.eventstate);
//		System.out.println("WM_read_file: U.themes.first 1: "+(U.themes.first!=null));

		try {
//			retval= Blender.BKE_read_file(C, new URL("file:/"+name), null, reports);
			retval= Blender.BKE_read_file(C, new URL(name), null, reports);
		} catch (Exception e) {
			retval= -1;
		}
		G.save_over = 1;

//		System.out.println("WM_read_file retval: "+retval);
		
		/* match the read WM with current WM */
//		System.out.println("WM_read_file: oldwin.eventstate 2: "+oldwin.eventstate);
		WmFiles.wm_window_match_do(C, wmbase[0]);
		Wm.wm_check(C); /* opens window(s), checks keymaps */

// XXX		mainwindow_set_filename_to_title(G.main.name);
// XXX		sound_initialize_sounds();

		if(retval==2) wm_init_userdef(C);	// in case a userdef is read from regular .blend
		
//		System.out.println("WM_read_file: U.themes.first 2: "+(U.themes.first!=null));

		if (retval!=0) G.relbase_valid = 1;

//// XXX		undo_editmode_clear();
//		BKE_reset_undo();
//		BKE_write_undo(C, "original");	/* save current state */
//
//		WM_event_add_notifier(C, NC_WM|ND_FILEREAD, NULL);
////		refresh_interface_font();
//
//		CTX_wm_window_set(C, NULL); /* exits queues */
	}
//	else if(retval==1)
//		BKE_write_undo(C, "Import file");
	else if(retval == -1) {
		System.out.println("Cannot read file.");
//		if(reports && reports.list.first == NULL)
//			BKE_report(reports, RPT_ERROR, "Cannot read file.");
	}
}


/* called on startup,  (context entirely filled with NULLs) */
/* or called for 'Erase All' */
/* op can be NULL */
public static wmOperatorType.Operator WM_read_homefile = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//public static int WM_read_homefile(bContext C, wmOperator op)
{
	ListBase[] wmbase = {new ListBase()};
//	char tstr[FILE_MAXDIR+FILE_MAXFILE], scestr[FILE_MAXDIR];
    String tstr = "";
//	char *home= BLI_gethome();
    String home= Util.BLI_gethome();
//	int from_memory= op?RNA_boolean_get(op.ptr, "factory"):0;
    boolean from_memory= false;
	int success;

//	BLI_clean(home);
//
//	free_ttfont(); /* still weird... what does it here? */

	G.relbase_valid = 0;
	if (!from_memory) {
//		BLI_make_file_string(G.sce, tstr, home, ".B25.blend");
//        tstr = home + "\\.B25.blend";
		tstr = home + "\\startup.blend";
	}
//	strcpy(scestr, G.sce);	/* temporary store */

	/* prevent loading no UI */
	G.fileflags &= ~Global.G_FILE_NO_UI;

	/* put aside screens to match with persistant windows later */
	WmFiles.wm_window_match_init(C, wmbase);

	if (!from_memory) {
//		if (ResourceAnchor.class.getClassLoader().getResource("resources/icons/blenderbuttons.png") != null) {
//			System.out.println("found icons");
//		}
//		if (ResourceAnchor.class.getClassLoader().getResource("resources/startup.blend") != null) {
//			System.out.println("found startup");
//		}
		
		if (new File(tstr).exists()) {
			try {
				success = Blender.BKE_read_file(C, new URL("file:/"+tstr), null, null);
			} catch (Exception e) {
				e.printStackTrace();
				success = 0;
			}
		}
		else if (ResourceAnchor.class.getClassLoader().getResource("resources/startup.blend") != null) {
//			System.out.println("resource found: "+"resources/startup.blend");
//			System.out.println("found startup");
            URL url = ResourceAnchor.class.getClassLoader().getResource("resources/startup.blend");
//            System.out.println("WM_read_homefile url: "+url.toString());
            success = Blender.BKE_read_file(C, url, null, null);
		}
		else {
			System.out.println("Can't find file: "+tstr);
			success = 0;
		}
	} else {
//		success = BKE_read_file_from_memory(C, datatoc_B_blend, datatoc_B_blend_size, NULL, NULL);
//		if (wmbase.first == NULL) wm_clear_default_size(C);
                System.out.println("read_file_from_memory ??");
                success = 0;
	}

	/* match the read WM with current WM */
	WmFiles.wm_window_match_do(C, wmbase[0]);
	Wm.wm_check(C); /* opens window(s), checks keymaps */

        // moved to GLEventListener Init
////	strcpy(G.sce, scestr); /* restore */

	wm_init_userdef(C);

////	/* When loading factory settings, the reset solid OpenGL lights need to be applied. */
////	GPU_default_lights();
////
////	/* XXX */
////	G.save_over = 0;	// start with save preference untitled.blend
////	G.fileflags &= ~G_FILE_AUTOPLAY;	/*  disable autoplay in .B.blend... */
//////	mainwindow_set_filename_to_title("");	// empty string re-initializes title to "Blender"
////
//////	refresh_interface_font();
////
//////	undo_editmode_clear();
////	BKE_reset_undo();
////	BKE_write_undo(C, "original");	/* save current state */
////
////	WM_event_add_notifier(C, NC_WM|ND_FILEREAD, NULL);
//	bContext.CTX_wm_window_set(C, null); /* exits queues */
	
//	System.out.println("Main:");
//	System.out.println(G.main);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};


//static void get_autosave_location(char buf[FILE_MAXDIR+FILE_MAXFILE])
//{
//	char pidstr[32];
//#ifdef WIN32
//	char subdir[9];
//	char savedir[FILE_MAXDIR];
//#endif
//
//	sprintf(pidstr, "%d.blend", abs(getpid()));
//
//#ifdef WIN32
//	if (!BLI_exists(U.tempdir)) {
//		BLI_strncpy(subdir, "autosave", sizeof(subdir));
//		BLI_make_file_string("/", savedir, BLI_gethome(), subdir);
//
//		/* create a new autosave dir
//		 * function already checks for existence or not */
//		BLI_recurdir_fileops(savedir);
//
//		BLI_make_file_string("/", buf, savedir, pidstr);
//		return;
//	}
//#endif
//
//	BLI_make_file_string("/", buf, U.tempdir, pidstr);
//}
//
//void WM_read_autosavefile(bContext *C)
//{
//	char tstr[FILE_MAX], scestr[FILE_MAX];
//	int save_over;
//
//	BLI_strncpy(scestr, G.sce, FILE_MAX);	/* temporal store */
//
//	get_autosave_location(tstr);
//
//	save_over = G.save_over;
//	BKE_read_file(C, tstr, NULL, NULL);
//	G.save_over = save_over;
//	BLI_strncpy(G.sce, scestr, FILE_MAX);
//}
//
//
//void read_Blog(void)
//{
//	char name[FILE_MAX], filename[FILE_MAX];
//	LinkNode *l, *lines;
//	struct RecentFile *recent;
//	char *line;
//	int num;
//
//	BLI_make_file_string("/", name, BLI_gethome(), ".Blog");
//	lines= BLI_read_file_as_lines(name);
//
//	G.recent_files.first = G.recent_files.last = NULL;
//
//	/* read list of recent opend files from .Blog to memory */
//	for (l= lines, num= 0; l && (num<U.recent_files); l= l.next, num++) {
//		line = l.link;
//		if (!BLI_streq(line, "")) {
//			if (num==0)
//				strcpy(G.sce, line);
//
//			recent = (RecentFile*)MEM_mallocN(sizeof(RecentFile),"RecentFile");
//			BLI_addtail(&(G.recent_files), recent);
//			recent.filename = (char*)MEM_mallocN(sizeof(char)*(strlen(line)+1), "name of file");
//			recent.filename[0] = '\0';
//
//			strcpy(recent.filename, line);
//		}
//	}
//
//	if(G.sce[0] == 0)
//		BLI_make_file_string("/", G.sce, BLI_gethome(), "untitled.blend");
//
//	BLI_free_file_lines(lines);
//
//#ifdef WIN32
//	/* Add the drive names to the listing */
//	{
//		__int64 tmp;
//		char folder[MAX_PATH];
//		char tmps[4];
//		int i;
//
//		tmp= GetLogicalDrives();
//
//		for (i=2; i < 26; i++) {
//			if ((tmp>>i) & 1) {
//				tmps[0]='a'+i;
//				tmps[1]=':';
//				tmps[2]='\\';
//				tmps[3]=0;
//
//// XX				fsmenu_insert_entry(tmps, 0, 0);
//			}
//		}
//
//		/* Adding Desktop and My Documents */
//// XXX		fsmenu_append_separator();
//
//		SHGetSpecialFolderPath(0, folder, CSIDL_PERSONAL, 0);
//// XXX		fsmenu_insert_entry(folder, 0, 0);
//		SHGetSpecialFolderPath(0, folder, CSIDL_DESKTOPDIRECTORY, 0);
//// XXX		fsmenu_insert_entry(folder, 0, 0);
//
//// XXX		fsmenu_append_separator();
//	}
//#endif
//
//	BLI_make_file_string(G.sce, name, BLI_gethome(), ".Bfs");
//	lines= BLI_read_file_as_lines(name);
//
//	for (l= lines; l; l= l.next) {
//		char *line= l.link;
//
//		if (!BLI_streq(line, "")) {
//// XXX			fsmenu_insert_entry(line, 0, 1);
//		}
//	}
//
//// XXX	fsmenu_append_separator();
//
//	/* add last saved file */
//	BLI_split_dirfile(G.sce, name, filename); /* G.sce shouldn't be relative */
//
//// XXX	fsmenu_insert_entry(name, 0, 0);
//
//	BLI_free_file_lines(lines);
//}
//
//static void writeBlog(void)
//{
//	struct RecentFile *recent, *next_recent;
//	char name[FILE_MAXDIR+FILE_MAXFILE];
//	FILE *fp;
//	int i;
//
//	BLI_make_file_string("/", name, BLI_gethome(), ".Blog");
//
//	recent = G.recent_files.first;
//	/* refresh .Blog of recent opened files, when current file was changed */
//	if(!(recent) || (strcmp(recent.filename, G.sce)!=0)) {
//		fp= fopen(name, "w");
//		if (fp) {
//			/* add current file to the beginning of list */
//			recent = (RecentFile*)MEM_mallocN(sizeof(RecentFile),"RecentFile");
//			recent.filename = (char*)MEM_mallocN(sizeof(char)*(strlen(G.sce)+1), "name of file");
//			recent.filename[0] = '\0';
//			strcpy(recent.filename, G.sce);
//			BLI_addhead(&(G.recent_files), recent);
//			/* write current file to .Blog */
//			fprintf(fp, "%s\n", recent.filename);
//			recent = recent.next;
//			i=1;
//			/* write rest of recent opened files to .Blog */
//			while((i<U.recent_files) && (recent)){
//				/* this prevents to have duplicities in list */
//				if (strcmp(recent.filename, G.sce)!=0) {
//					fprintf(fp, "%s\n", recent.filename);
//					recent = recent.next;
//				}
//				else {
//					next_recent = recent.next;
//					MEM_freeN(recent.filename);
//					BLI_freelinkN(&(G.recent_files), recent);
//					recent = next_recent;
//				}
//				i++;
//			}
//			fclose(fp);
//		}
//	}
//}
//
//static void do_history(char *name, ReportList *reports)
//{
//	char tempname1[FILE_MAXDIR+FILE_MAXFILE], tempname2[FILE_MAXDIR+FILE_MAXFILE];
//	int hisnr= U.versions;
//
//	if(U.versions==0) return;
//	if(strlen(name)<2) return;
//
//	while(hisnr > 1) {
//		sprintf(tempname1, "%s%d", name, hisnr-1);
//		sprintf(tempname2, "%s%d", name, hisnr);
//
//		if(BLI_rename(tempname1, tempname2))
//			BKE_report(reports, RPT_ERROR, "Unable to make version backup");
//
//		hisnr--;
//	}
//
//	/* is needed when hisnr==1 */
//	sprintf(tempname1, "%s%d", name, hisnr);
//
//	if(BLI_rename(name, tempname1))
//		BKE_report(reports, RPT_ERROR, "Unable to make version backup");
//}
//
//void WM_write_file(bContext *C, char *target, int compress, ReportList *reports)
//{
//	Library *li;
//	int writeflags, len;
//	char di[FILE_MAX];
//
//	len = strlen(target);
//
//	if (len == 0) return;
//	if (len >= FILE_MAX) {
//		BKE_report(reports, RPT_ERROR, "Path too long, cannot save");
//		return;
//	}
//
//	/* send the OnSave event */
//	for (li= G.main.library.first; li; li= li.id.next) {
//		if (BLI_streq(li.name, target)) {
//			BKE_report(reports, RPT_ERROR, "Cannot overwrite used library");
//			return;
//		}
//	}
//
//	if (!BLO_has_bfile_extension(target) && (len+6 < FILE_MAX)) {
//		sprintf(di, "%s.blend", target);
//	} else {
//		strcpy(di, target);
//	}
//
////	if (BLI_exists(di)) {
//// XXX		if(!saveover(di))
//// XXX			return;
////	}
//
//	if (G.fileflags & G_AUTOPACK) {
//		packAll(G.main, reports);
//	}
//
//	ED_object_exit_editmode(C, 0);
//
//	do_history(di, reports);
//
//	writeflags= G.fileflags;
//
//	/* set compression flag */
//	if(compress) writeflags |= G_FILE_COMPRESS;
//	else writeflags &= ~G_FILE_COMPRESS;
//
//	if (BLO_write_file(CTX_data_main(C), di, writeflags, reports)) {
//		strcpy(G.sce, di);
//		G.relbase_valid = 1;
//		strcpy(G.main.name, di);	/* is guaranteed current file */
//
//		G.save_over = 1; /* disable untitled.blend convention */
//
//		if(compress) G.fileflags |= G_FILE_COMPRESS;
//		else G.fileflags &= ~G_FILE_COMPRESS;
//
//		writeBlog();
//	}
//
//// XXX	waitcursor(0);
//}

/* operator entry */
public static wmOperatorType.Operator WM_write_homefile = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	wmWindow *win= CTX_wm_window(C);
//	char tstr[FILE_MAXDIR+FILE_MAXFILE];
//	int write_flags;
//
//	/* check current window and close it if temp */
//	if(win.screen.full == SCREENTEMP) {
//		wm_window_close(C, win);
//	}
//
//	BLI_make_file_string("/", tstr, BLI_gethome(), ".B25.blend");
//
//	/*  force save as regular blend file */
//	write_flags = G.fileflags & ~(G_FILE_COMPRESS | G_FILE_LOCK | G_FILE_SIGN);
//
//	BLO_write_file(CTX_data_main(C), tstr, write_flags, op.reports);
//
//	G.save_over= 0;

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

//void WM_write_autosave(bContext *C)
//{
//	char tstr[FILE_MAXDIR+FILE_MAXFILE];
//	int write_flags;
//
//	get_autosave_location(tstr);
//
//		/*  force save as regular blend file */
//	write_flags = G.fileflags & ~(G_FILE_COMPRESS | G_FILE_LOCK | G_FILE_SIGN);
//
//		/* error reporting to console */
//	BLO_write_file(CTX_data_main(C), tstr, write_flags, NULL);
//}
//
///* if global undo; remove tempsave, otherwise rename */
//void delete_autosave(void)
//{
//	char tstr[FILE_MAXDIR+FILE_MAXFILE];
//
//	get_autosave_location(tstr);
//
//	if (BLI_exists(tstr)) {
//		char str[FILE_MAXDIR+FILE_MAXFILE];
//		BLI_make_file_string("/", str, U.tempdir, "quit.blend");
//
//		if(U.uiflag & USER_GLOBALUNDO) BLI_delete(tstr, 0, 0);
//		else BLI_rename(tstr, str);
//	}
//}
//
///***/
}
