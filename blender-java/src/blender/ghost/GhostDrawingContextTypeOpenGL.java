package blender.ghost;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import blender.blenkernel.bContext;
import blender.windowmanager.Wm;

public class GhostDrawingContextTypeOpenGL implements GLEventListener {
	
	private bContext wmContext;
	
	public GhostDrawingContextTypeOpenGL(bContext C) {
		wmContext = C;
	}
	
	public void init(GLAutoDrawable drawable) {
//        System.out.println("GLEventListener: init");
        GL2 gl = (GL2)drawable.getGL();
        
        Wm.WM_init(gl, wmContext);

//        wmWindowManager wm= bContext.CTX_wm_manager(Wm.wmContext);
//
//        /* case: fileread */
//        if((wm.initialized & WindowManagerTypes.WM_INIT_WINDOW) == 0) {
////        	WM_keymap_init(C);
////            ScreenEdit.ED_screens_initialize(gl, wm);
//            ScreenEdit.ED_screens_initialize(wm);
//            wm.initialized |= WindowManagerTypes.WM_INIT_WINDOW;
//        }

        // all below moved from WM_read_homefile()

        //	strcpy(G.sce, scestr); /* restore */

//        WmFiles.wm_init_userdef();

//		/* When loading factory settings, the reset solid OpenGL lights need to be applied. */
//		GPU_default_lights();
//	
//		/* XXX */
//		G.save_over = 0;	// start with save preference untitled.blend
//		G.fileflags &= ~G_FILE_AUTOPLAY;	/*  disable autoplay in .B.blend... */
//	//	mainwindow_set_filename_to_title("");	// empty string re-initializes title to "Blender"
//	
//	//	refresh_interface_font();
//	
//	//	undo_editmode_clear();
//		BKE_reset_undo();
//		BKE_write_undo(C, "original");	/* save current state */
//	
//		WM_event_add_notifier(C, NC_WM|ND_FILEREAD, NULL);
//        bContext.CTX_wm_window_set(Wm.wmContext, null); /* exits queues */

        // all below moved from editscreen.addscreen()

//        window_set_handler(EditScreen.mainwin, EditScreen.add_to_mainqueue, null);
////        window_open_ndof(mainwin); /* needs to occur once the mainwin handler is set */
//        EditScreen.init_mainwin(gl);
//        MyWindow.mywinset(gl, 1);
//
//        MainQueue.mainqenter(MyDevice.REDRAW, 1);

        // all below moved from usiblender.BIF_init()

//        USIBlender.initbuttons(gl);
////        InitCursorData();
////        sound_init_listener();
////        init_node_butfuncs();
////
////        BIF_preview_init_dbase();
//        USIBlender.BIF_read_homefile(false);
//
//        Resources.BIF_resources_init();	/* after homefile, to dynamically load an icon file based on theme settings */
//
////        BIF_filelist_init_icons();
//
//        GpuDraw.GPU_state_init(gl);
////        GPU_extensions_init();
//
////        readBlog();
////        BLI_strncpy(G.lib, G.sce, FILE_MAX);
//
//        initialized = true;
    }

    public void display(GLAutoDrawable drawable) {
//        GL2 gl = new DebugGL(drawable.getGL());
        GL2 gl = (GL2)drawable.getGL();

        /* main display loop */
    	if(Wm.initialized) {
            //Wm.WM_main(gl, Wm.wmContext);
            Wm.WM_main(gl, wmContext);
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = (GL2)drawable.getGL();
//        rcti rect = new rcti();
//        rect.xmin = x;
//        rect.ymin = y;
//        rect.xmax = width;
//        rect.ymax = height;
//        GhostWinLay.event_proc(GHOST_TEventType.GHOST_kEventWindowSize, rect);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        System.out.println("GLEventListener: displayChanged");
    }

    public void dispose(GLAutoDrawable arg0) {
        //WmInitExit.WM_exit(null);
    	if (wmContext != null) {
    		bContext.CTX_wm_manager_close(wmContext);
    	}
    }

}
