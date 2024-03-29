/*  blender.c   jan 94     MIXED MODEL
 * 
 * common help functions and data
 * 
 * $Id: Blender.java,v 1.2 2009/09/18 05:20:13 jladere Exp $
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

import java.net.URL;

import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.blenloader.ReadBlenEntry;
import blender.blenloader.ReadFile;
import blender.blenloader.ReadFile.BlendFileData;
import blender.makesdna.ObjectTypes;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.ReportList;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.UserDef;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.bScreen;

public class Blender {

    /* also fill in structs itself, dna cannot handle defines, duplicate with utildefines.h still */
    public static final int FILE_MAXDIR = 160;
    public static final int FILE_MAXFILE = 80;
    public static final int FILE_MAX = 240;
    
    public static final int SELECT = 1;
    public static final int HIDDEN = 1;
    public static final int FIRST = 1;
    public static final int ACTIVE = 2;

    public static final int XIC= 20;
    public static final int YIC= 20;

    public static final int VERSE = 3;

    public static final int DESELECT = 0;
    public static final int VISIBLE = 0;
    public static final int LAST = 0;

    public static final int BLENDER_VERSION=		250;
    public static final int BLENDER_SUBVERSION=		0;

    public static final int BLENDER_MINVERSION=		250;
    public static final int BLENDER_MINSUBVERSION=	0;


    public static Global G = new Global();
    public static UserDef U = new UserDef();
//ListBase WMlist= {NULL, NULL};
    public static short ENDIAN_ORDER;

//    char versionstr[48]= "";
    public static String versionstr = "";

    /* nonstandard define, sometimes in math.h */
    public static final float MAXFLOAT=  ((float)3.40282347e+38);

    public static final bObject OBACT(Scene scene) {
        return scene.basact != null ? scene.basact.object : null;
    }

    public static final boolean TESTBASELIB(View3D v3d, Base base) {
        return ((base.flag & SELECT) != 0 && (base.lay & v3d.lay) != 0 && (base.object.id.lib == null) && ((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW) == 0));
    }


///* ************************************************ */
///* pushpop facility: to store data temporally, FIFO! */
//
//ListBase ppmain={0, 0};
//
//typedef struct PushPop {
//	struct PushPop *next, *prev;
//	void *data;
//	int len;
//} PushPop;
//
//void pushdata(void *data, int len)
//{
//	PushPop *pp;
//
//	pp= MEM_mallocN(sizeof(PushPop), "pushpop");
//	BLI_addtail(&ppmain, pp);
//	pp.data= MEM_mallocN(len, "pushpop");
//	pp.len= len;
//	memcpy(pp.data, data, len);
//}
//
//void popfirst(void *data)
//{
//	PushPop *pp;
//
//	pp= ppmain.first;
//	if(pp) {
//		memcpy(data, pp.data, pp.len);
//		BLI_remlink(&ppmain, pp);
//		MEM_freeN(pp.data);
//		MEM_freeN(pp);
//	}
//	else printf("error in popfirst\n");
//}
//
//void poplast(void *data)
//{
//	PushPop *pp;
//
//	pp= ppmain.last;
//	if(pp) {
//		memcpy(data, pp.data, pp.len);
//		BLI_remlink(&ppmain, pp);
//		MEM_freeN(pp.data);
//		MEM_freeN(pp);
//	}
//	else printf("error in poplast\n");
//}
//
//void free_pushpop()
//{
//	PushPop *pp;
//
//	pp= ppmain.first;
//	while(pp) {
//		BLI_remlink(&ppmain, pp);
//		MEM_freeN(pp.data);
//		MEM_freeN(pp);
//	}
//}
//
//void pushpop_test()
//{
//	if(ppmain.first) printf("pushpop not empty\n");
//	free_pushpop();
//}
//
//
//
///* ********** free ********** */
//
///* only to be called on exit blender */
//void free_blender(void)
//{
//	/* samples are in a global list..., also sets G.main.sound.sample NULL */
//	sound_free_all_samples();
//
//	free_main(G.main);
//	G.main= NULL;
//
//	BKE_spacetypes_free();		/* after free main, it uses space callbacks */
//
//	IMB_freeImBufdata();		/* imbuf lib */
//
//	free_nodesystem();
//}
//
//void initglobals(void)
//{
//	memset(&G, 0, sizeof(Global));
//
//	U.savetime= 1;
//
//	G.main= MEM_callocN(sizeof(Main), "initglobals");
//
//	strcpy(G.ima, "//");
//
//	ENDIAN_ORDER= 1;
//	ENDIAN_ORDER= (((char*)&ENDIAN_ORDER)[0])? L_ENDIAN: B_ENDIAN;
//
//	if(BLENDER_SUBVERSION)
//		sprintf(versionstr, "www.blender.org %d.%d", BLENDER_VERSION, BLENDER_SUBVERSION);
//	else
//		sprintf(versionstr, "www.blender.org %d", BLENDER_VERSION);
//
//#ifdef _WIN32	// FULLSCREEN
//	G.windowstate = G_WINDOWSTATE_USERDEF;
//#endif
//
//	G.charstart = 0x0000;
//	G.charmin = 0x0000;
//	G.charmax = 0xffff;
//}
    public static void initglobals() {
        G = new Global();

//        theEditMesh = new EditMesh();
//        G.editMesh = theEditMesh;

        U.savetime = 1;

        G.main = new Main();

//	strcpy(G.ima, "//");

//        G.version = BLENDER_VERSION;

	ENDIAN_ORDER= Global.B_ENDIAN;

        if (BLENDER_SUBVERSION != 0) {
            versionstr = String.format("www.blender.org %d.%d", BLENDER_VERSION, BLENDER_SUBVERSION);
        } else {
            versionstr = String.format("www.blender.org %d", BLENDER_VERSION);
        }

//#ifdef _WIN32	// FULLSCREEN
//	G.windowstate = G_WINDOWSTATE_USERDEF;
//#endif

//	clear_workob();	/* object.c */

//	G.charstart = 0x0000;
//	G.charmin = 0x0000;
//	G.charmax = 0xffff;
    }

public static void clear_global()
{
//	fastshade_free_render();	/* lamps hang otherwise */
	LibraryUtil.free_main(G.main);			/* free all lib data */

//	free_vertexpaint();

	G.main= null;

	G.f &= ~(Global.G_WEIGHTPAINT + Global.G_VERTEXPAINT + Global.G_FACESELECT + Global.G_PARTICLEEDIT);
}

///* make sure path names are correct for OS */
//static void clean_paths(Main *main)
//{
//	Image *image= main.image.first;
//	bSound *sound= main.sound.first;
//	Scene *scene= main.scene.first;
//	Editing *ed;
//	Sequence *seq;
//	Strip *strip;
//
//	while(image) {
//		BLI_clean(image.name);
//		image= image.id.next;
//	}
//
//	while(sound) {
//		BLI_clean(sound.name);
//		sound= sound.id.next;
//	}
//
//	while(scene) {
//		ed= seq_give_editing(scene, 0);
//		if(ed) {
//			seq= ed.seqbasep.first;
//			while(seq) {
//				if(seq.plugin) {
//					BLI_clean(seq.plugin.name);
//				}
//				strip= seq.strip;
//				while(strip) {
//					BLI_clean(strip.dir);
//					strip= strip.next;
//				}
//				seq= seq.next;
//			}
//		}
//		BLI_clean(scene.r.backbuf);
//		BLI_clean(scene.r.pic);
//
//		scene= scene.id.next;
//	}
//}

/* context matching */
/* handle no-ui case */

public static void setup_app_data(bContext C, BlendFileData bfd, String filename)
{
	bObject ob;
	bScreen curscreen= null;
	Scene curscene= null;
	char mode;

	/* 'u' = undo save, 'n' = no UI load */
	if(bfd.main.screen.first==null) mode= 'u';
	else if((G.fileflags & Global.G_FILE_NO_UI)!=0) mode= 'n';
	else mode= 0;

//	clean_paths(bfd.main);

	/* XXX here the complex windowmanager matching */

	/* no load screens? */
	if(mode!=0) {
		/* comes from readfile.c */
//		extern void lib_link_screen_restore(Main *, bScreen *, Scene *);

//		SWAP(ListBase, G.main.wm, bfd.main.wm);
                ListBase sw_ap=G.main.wm; G.main.wm=bfd.main.wm; bfd.main.wm=sw_ap;
//		SWAP(ListBase, G.main.screen, bfd.main.screen);
                sw_ap=G.main.screen; G.main.screen=bfd.main.screen; bfd.main.screen=sw_ap;
//		SWAP(ListBase, G.main.script, bfd.main.script);
                sw_ap=G.main.script; G.main.script=bfd.main.script; bfd.main.script=sw_ap;

		/* we re-use current screen */
		curscreen= bContext.CTX_wm_screen(C);
		/* but use new Scene pointer */
		curscene= bfd.curscene;
		if(curscene==null) curscene= bfd.main.scene.first;
		/* and we enforce curscene to be in current screen */
		curscreen.scene= curscene;

		/* clear_global will free G.main, here we can still restore pointers */
		ReadFile.lib_link_screen_restore(bfd.main, curscreen, curscene);
	}

	/* free G.main Main database */
	clear_global();

	G.main= bfd.main;

	bContext.CTX_data_main_set(C, G.main);

	if (bfd.user!=null) {

		/* only here free userdef themes... */
		BKE_userdef_free();

		U= bfd.user;
//		MEM_freeN(bfd.user);
	}

	/* samples is a global list... */
//	sound_free_all_samples();

	/* case G_FILE_NO_UI or no screens in file */
	if(mode!=0) {
		/* leave entire context further unaltered? */
		bContext.CTX_data_scene_set(C, curscene);
	}
	else {
		G.winpos= (short)bfd.winpos;
		G.displaymode= (short)bfd.displaymode;
		G.fileflags= bfd.fileflags;

		bContext.CTX_wm_screen_set(C, bfd.curscreen);
		bContext.CTX_data_scene_set(C, bfd.curscreen.scene);
		bContext.CTX_wm_area_set(C, null);
		bContext.CTX_wm_region_set(C, null);
		bContext.CTX_wm_menu_set(C, null);
	}

	/* this can happen when active scene was lib-linked, and doesnt exist anymore */
	if(bContext.CTX_data_scene(C)==null) {
		bContext.CTX_data_scene_set(C, bfd.main.scene.first);
		bContext.CTX_wm_screen(C).scene= bContext.CTX_data_scene(C);
		curscene= bContext.CTX_data_scene(C);
	}

	/* special cases, override loaded flags: */
	if ((G.f & Global.G_DEBUG)!=0) bfd.globalf |= Global.G_DEBUG;
	else bfd.globalf &= ~Global.G_DEBUG;
	if ((G.f & Global.G_SWAP_EXCHANGE)!=0) bfd.globalf |= Global.G_SWAP_EXCHANGE;
	else bfd.globalf &= ~Global.G_SWAP_EXCHANGE;

//	if ((U.flag & USER_DONT_DOSCRIPTLINKS)) bfd.globalf &= ~G_DOSCRIPTLINKS;

	G.f= bfd.globalf;

	if (G.background==0) {
		//setscreen(G.curscreen);
	}

//	// XXX temporarily here
//	if(G.main.versionfile < 250)
//		do_versions_ipos_to_animato(G.main); // XXX fixme... complicated versionpatching

	/* baseflags, groups, make depsgraph, etc */
	SceneUtil.set_scene_bg(bContext.CTX_data_scene(C));

//	/* last stage of do_versions actually, that sets recalc flags for recalc poses */
//	for(ob= G.main.object.first; ob; ob= ob.id.next) {
//		if(ob.type==OB_ARMATURE)
//			if(ob.recalc) object_handle_update(CTX_data_scene(C), ob);
//	}
//
//	/* now tag update flags, to ensure deformers get calculated on redraw */
//	DAG_scene_update_flags(CTX_data_scene(C), CTX_data_scene(C).lay);

//	if (G.sce != filename) /* these are the same at times, should never copy to the same location */
		StringUtil.strcpy(G.sce,0, StringUtil.toCString(filename),0);

	StringUtil.BLI_strncpy(G.main.name,0, StringUtil.toCString(filename),0, FILE_MAX); /* is guaranteed current file */

//	MEM_freeN(bfd);
}

//    public static void setup_app_data(BlendFileData bfd, String filename) {
//        bObject ob;
//        bScreen curscreen = null;
//        Scene curscene = null;
//        byte mode;
//
//        /* 'u' = undo save, 'n' = no UI load */
//        if (bfd.main.screen.first == null) {
//            mode = 'u';
//        } else if ((G.fileflags & Global.G_FILE_NO_UI) != 0) {
//            mode = 'n';
//        } else {
//            mode = 0;
//        }
//
////	clean_paths(bfd.main);
//
//        /* no load screens? */
//        if (mode != 0) {
////            SWAP(ListBase, G.main.screen, bfd.main.screen);
////            SWAP(ListBase, G.main.script, bfd.main.script);
//
//            /* we re-use current screen */
//            curscreen = G.curscreen;
//            /* but use new Scene pointer */
//            curscene = bfd.curscene;
//            if (curscene == null) {
//                curscene = (Scene) bfd.main.scene.first;
//            }
//            /* and we enforce curscene to be in current screen */
//            curscreen.scene = curscene;
//
//            /* clear_global will free G.main, here we can still restore pointers */
//            ReadFile.lib_link_screen_restore(bfd.main, curscene);
//        }
//
//        clear_global();	/* free Main database */
//
//        if (mode != 'u') {
//            G.save_over = 1;
//        }
//
//        G.main = bfd.main;
//        if (bfd.user != null) {
//            /* only here free userdef themes... */
//            Util.BLI_freelistN(U.themes);
//
//            U = bfd.user;
////            MEM_freeN(bfd.user);
//        }
//
//	/* samples is a Global list... */
////	sound_free_all_samples();
//
//        /* case G_FILE_NO_UI or no screens in file */
//        if (mode != 0) {
//            G.curscreen = curscreen;
//            G.scene = curscene;
//        } else {
//            G.winpos = (short) bfd.winpos;
//            G.displaymode = (short) bfd.displaymode;
//            G.fileflags = bfd.fileflags;
//            G.curscreen = bfd.curscreen;
//            G.scene = G.curscreen.scene;
//        }
//        /* this can happen when active scene was lib-linked, and doesnt exist anymore */
//        if (G.scene == null) {
//            G.scene = (Scene) G.main.scene.first;
//            G.curscreen.scene = G.scene;
//        }
//
//        /* special cases, override loaded flags: */
//        if ((G.f & Global.G_DEBUG) != 0) {
//            bfd.globalf |= Global.G_DEBUG;
//        } else {
//            bfd.globalf &= ~Global.G_DEBUG;
//        }
//
////	if ((U.flag & USER_DONT_DOSCRIPTLINKS)) bfd.globalf &= ~G_DOSCRIPTLINKS;
//
//        G.f = bfd.globalf;
//
//        if (G.background == 0) {
//            EditScreen.setscreen(G.curscreen);
//        }
//
//        /* baseflags, groups, make depsgraph, etc */
//        SceneUtil.set_scene_bg(G.scene);
//
////	/* clear BONE_UNKEYED flags, these are not valid anymore for proxies */
////	framechange_poses_clear_unkeyed();
////
////	/* last stage of do_versions actually, that sets recalc flags for recalc poses */
////	for(ob= G.main.object.first; ob; ob= ob.id.next) {
////		if(ob.type==OB_ARMATURE)
////			if(ob.recalc) object_handle_update(ob);
////	}
////
////	/* now tag update flags, to ensure deformers get calculated on redraw */
////	DAG_scene_update_flags(G.scene, G.scene.lay);
////
////	if (G.f & G_DOSCRIPTLINKS) {
////		/* there's an onload scriptlink to execute in screenmain */
////		mainqenter(ONLOAD_SCRIPT, 1);
////	}
////	if (G.sce != filename) /* these are the same at times, should never copy to the same location */
//        Util.strcpy(G.sce, 0, Util.toCString(filename), 0);
//
//        Util.BLI_strncpy(G.main.name, Util.toCString(filename), FILE_MAX); /* is guaranteed current file */
//
//        G.currentSDNA = bfd.curSDNA;
////	MEM_freeN(bfd);
//    }

//static int handle_subversion_warning(Main *main)
//{
//	if(main.minversionfile > BLENDER_VERSION ||
//	   (main.minversionfile == BLENDER_VERSION &&
//		 main.minsubversionfile > BLENDER_SUBVERSION)) {
//
//		char str[128];
//
//		sprintf(str, "File written by newer Blender binary: %d.%d , expect loss of data!", main.minversionfile, main.minsubversionfile);
//// XXX		error(str);
//	}
//	return 1;
//}

public static void BKE_userdef_free()
{

	ListBaseUtil.BLI_freelistN(U.uistyles);
	ListBaseUtil.BLI_freelistN(U.uifonts);
	ListBaseUtil.BLI_freelistN(U.themes);

}

/* returns:
   0: no load file
   1: OK
   2: OK, and with new user settings
*/

//public static int BKE_read_file(bContext C, String dir, Object unused, ReportList reports)
public static int BKE_read_file(bContext C, URL dir, Object unused, ReportList reports)
{
	System.out.println("BKE_read_file: "+dir.toString());
	BlendFileData bfd;
	int retval= 1;

	bfd= ReadBlenEntry.BLO_read_from_file(dir, reports);
	if (bfd!=null) {
		if(bfd.user!=null) retval= 2;

//		if(0==handle_subversion_warning(bfd.main)) {
//			free_main(bfd.main);
//			MEM_freeN(bfd);
//			bfd= NULL;
//			retval= 0;
//		}
//		else
			setup_app_data(C, bfd, dir.toString()); // frees BFD
	}
//	else
//		BKE_reports_prependf(reports, "Loading %s failed: ", dir);

	return (bfd!=null?retval:0);
}

//int BKE_read_file_from_memory(bContext *C, char* filebuf, int filelength, void *unused, ReportList *reports)
//{
//	BlendFileData *bfd;
//
//	bfd= BLO_read_from_memory(filebuf, filelength, reports);
//	if (bfd)
//		setup_app_data(C, bfd, "<memory2>");
//	else
//		BKE_reports_prepend(reports, "Loading failed: ");
//
//	return (bfd?1:0);
//}
//
///* memfile is the undo buffer */
//int BKE_read_file_from_memfile(bContext *C, MemFile *memfile, ReportList *reports)
//{
//	BlendFileData *bfd;
//
//	bfd= BLO_read_from_memfile(CTX_data_main(C), G.sce, memfile, reports);
//	if (bfd)
//		setup_app_data(C, bfd, "<memory1>");
//	else
//		BKE_reports_prepend(reports, "Loading failed: ");
//
//	return (bfd?1:0);
//}
//
///* *****************  testing for break ************* */
//
//static void (*blender_test_break_cb)(void)= NULL;
//
//void set_blender_test_break_cb(void (*func)(void) )
//{
//	blender_test_break_cb= func;
//}
//
//
//int blender_test_break(void)
//{
//	if (!G.background) {
//		if (blender_test_break_cb)
//			blender_test_break_cb();
//	}
//
//	return (G.afbreek==1);
//}
//
//
///* ***************** GLOBAL UNDO *************** */
//
//#define UNDO_DISK	0
//
//#define MAXUNDONAME	64
//typedef struct UndoElem {
//	struct UndoElem *next, *prev;
//	char str[FILE_MAXDIR+FILE_MAXFILE];
//	char name[MAXUNDONAME];
//	MemFile memfile;
//	uintptr_t undosize;
//} UndoElem;
//
//static ListBase undobase={NULL, NULL};
//static UndoElem *curundo= NULL;
//
//
//static int read_undosave(bContext *C, UndoElem *uel)
//{
//	char scestr[FILE_MAXDIR+FILE_MAXFILE];
//	int success=0, fileflags;
//
//	strcpy(scestr, G.sce);	/* temporal store */
//	fileflags= G.fileflags;
//	G.fileflags |= G_FILE_NO_UI;
//
//	if(UNDO_DISK)
//		success= BKE_read_file(C, uel.str, NULL, NULL);
//	else
//		success= BKE_read_file_from_memfile(C, &uel.memfile, NULL);
//
//	/* restore */
//	strcpy(G.sce, scestr);
//	G.fileflags= fileflags;
//
//	return success;
//}
//
///* name can be a dynamic string */
//void BKE_write_undo(bContext *C, char *name)
//{
//	uintptr_t maxmem, totmem, memused;
//	int nr, success;
//	UndoElem *uel;
//
//	if( (U.uiflag & USER_GLOBALUNDO)==0) return;
//	if( U.undosteps==0) return;
//
//	/* remove all undos after (also when curundo==NULL) */
//	while(undobase.last != curundo) {
//		uel= undobase.last;
//		BLI_remlink(&undobase, uel);
//		BLO_free_memfile(&uel.memfile);
//		MEM_freeN(uel);
//	}
//
//	/* make new */
//	curundo= uel= MEM_callocN(sizeof(UndoElem), "undo file");
//	strncpy(uel.name, name, MAXUNDONAME-1);
//	BLI_addtail(&undobase, uel);
//
//	/* and limit amount to the maximum */
//	nr= 0;
//	uel= undobase.last;
//	while(uel) {
//		nr++;
//		if(nr==U.undosteps) break;
//		uel= uel.prev;
//	}
//	if(uel) {
//		while(undobase.first!=uel) {
//			UndoElem *first= undobase.first;
//			BLI_remlink(&undobase, first);
//			/* the merge is because of compression */
//			BLO_merge_memfile(&first.memfile, &first.next.memfile);
//			MEM_freeN(first);
//		}
//	}
//
//
//	/* disk save version */
//	if(UNDO_DISK) {
//		static int counter= 0;
//		char tstr[FILE_MAXDIR+FILE_MAXFILE];
//		char numstr[32];
//
//		/* calculate current filename */
//		counter++;
//		counter= counter % U.undosteps;
//
//		sprintf(numstr, "%d.blend", counter);
//		BLI_make_file_string("/", tstr, btempdir, numstr);
//
//		success= BLO_write_file(CTX_data_main(C), tstr, G.fileflags, NULL);
//
//		strcpy(curundo.str, tstr);
//	}
//	else {
//		MemFile *prevfile=NULL;
//
//		if(curundo.prev) prevfile= &(curundo.prev.memfile);
//
//		memused= MEM_get_memory_in_use();
//		success= BLO_write_file_mem(CTX_data_main(C), prevfile, &curundo.memfile, G.fileflags, NULL);
//		curundo.undosize= MEM_get_memory_in_use() - memused;
//	}
//
//	if(U.undomemory != 0) {
//		/* limit to maximum memory (afterwards, we can't know in advance) */
//		totmem= 0;
//		maxmem= ((uintptr_t)U.undomemory)*1024*1024;
//
//		/* keep at least two (original + other) */
//		uel= undobase.last;
//		while(uel && uel.prev) {
//			totmem+= uel.undosize;
//			if(totmem>maxmem) break;
//			uel= uel.prev;
//		}
//
//		if(uel) {
//			if(uel.prev && uel.prev.prev)
//				uel= uel.prev;
//
//			while(undobase.first!=uel) {
//				UndoElem *first= undobase.first;
//				BLI_remlink(&undobase, first);
//				/* the merge is because of compression */
//				BLO_merge_memfile(&first.memfile, &first.next.memfile);
//				MEM_freeN(first);
//			}
//		}
//	}
//}
//
///* 1= an undo, -1 is a redo. we have to make sure 'curundo' remains at current situation
// * Note, ALWAYS call sound_initialize_sounds after BKE_undo_step() */
//void BKE_undo_step(bContext *C, int step)
//{
//
//	if(step==0) {
//		read_undosave(C, curundo);
//	}
//	else if(step==1) {
//		/* curundo should never be NULL, after restart or load file it should call undo_save */
//		if(curundo==NULL || curundo.prev==NULL) ; // XXX error("No undo available");
//		else {
//			if(G.f & G_DEBUG) printf("undo %s\n", curundo.name);
//			curundo= curundo.prev;
//			read_undosave(C, curundo);
//		}
//	}
//	else {
//
//		/* curundo has to remain current situation! */
//
//		if(curundo==NULL || curundo.next==NULL) ; // XXX error("No redo available");
//		else {
//			read_undosave(C, curundo.next);
//			curundo= curundo.next;
//			if(G.f & G_DEBUG) printf("redo %s\n", curundo.name);
//		}
//	}
//}
//
//void BKE_reset_undo(void)
//{
//	UndoElem *uel;
//
//	uel= undobase.first;
//	while(uel) {
//		BLO_free_memfile(&uel.memfile);
//		uel= uel.next;
//	}
//
//	BLI_freelistN(&undobase);
//	curundo= NULL;
//}
//
///* based on index nr it does a restore */
//void BKE_undo_number(bContext *C, int nr)
//{
//	UndoElem *uel;
//	int a=1;
//
//	for(uel= undobase.first; uel; uel= uel.next, a++) {
//		if(a==nr) break;
//	}
//	curundo= uel;
//	BKE_undo_step(C, 0);
//}
//
//char *BKE_undo_menu_string(void)
//{
//	UndoElem *uel;
//	DynStr *ds= BLI_dynstr_new();
//	char *menu;
//
//	BLI_dynstr_append(ds, "Global Undo History %t");
//
//	for(uel= undobase.first; uel; uel= uel.next) {
//		BLI_dynstr_append(ds, "|");
//		BLI_dynstr_append(ds, uel.name);
//	}
//
//	menu= BLI_dynstr_get_cstring(ds);
//	BLI_dynstr_free(ds);
//
//	return menu;
//}
//
//	/* saves quit.blend */
//void BKE_undo_save_quit(void)
//{
//	UndoElem *uel;
//	MemFileChunk *chunk;
//	int file;
//	char str[FILE_MAXDIR+FILE_MAXFILE];
//
//	if( (U.uiflag & USER_GLOBALUNDO)==0) return;
//
//	uel= curundo;
//	if(uel==NULL) {
//		printf("No undo buffer to save recovery file\n");
//		return;
//	}
//
//	/* no undo state to save */
//	if(undobase.first==undobase.last) return;
//
//	BLI_make_file_string("/", str, btempdir, "quit.blend");
//
//	file = open(str,O_BINARY+O_WRONLY+O_CREAT+O_TRUNC, 0666);
//	if(file == -1) {
//		//XXX error("Unable to save %s, check you have permissions", str);
//		return;
//	}
//
//	chunk= uel.memfile.chunks.first;
//	while(chunk) {
//		if( write(file, chunk.buf, chunk.size) != chunk.size) break;
//		chunk= chunk.next;
//	}
//
//	close(file);
//
//	if(chunk) ; //XXX error("Unable to save %s, internal error", str);
//	else printf("Saved session recovery to %s\n", str);
//}
}
