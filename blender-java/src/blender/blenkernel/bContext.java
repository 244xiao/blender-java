/**
 * $Id: bContext.java,v 1.2 2009/09/18 05:20:13 jladere Exp $
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
 * Contributor(s): Blender Foundation (2008).
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.blenkernel;

import static blender.blenkernel.Blender.G;

//import javax.media.opengl.GL2;

import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.ObjectTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceButs;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.SpaceNode;
import blender.makesdna.sdna.SpaceOops;
import blender.makesdna.sdna.SpaceTime;
import blender.makesdna.sdna.ToolSettings;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.CollectionPointerLink;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class bContext {
	
	/* for the conrtext's rna mode enum
	 * keep aligned with data_mode_strings in context.c */
//	public static enum {
		public static final int CTX_MODE_EDIT_MESH = 0;
		public static final int CTX_MODE_EDIT_CURVE = 1;
		public static final int CTX_MODE_EDIT_SURFACE = 2;
		public static final int CTX_MODE_EDIT_TEXT = 3;
		public static final int CTX_MODE_EDIT_ARMATURE = 4;
		public static final int CTX_MODE_EDIT_METABALL = 5;
		public static final int CTX_MODE_EDIT_LATTICE = 6;
		public static final int CTX_MODE_POSE = 7;
		public static final int CTX_MODE_SCULPT = 8;
		public static final int CTX_MODE_PAINT_WEIGHT = 9;
		public static final int CTX_MODE_PAINT_VERTEX = 10;
		public static final int CTX_MODE_PAINT_TEXTURE = 11;
		public static final int CTX_MODE_PARTICLE = 12;
		public static final int CTX_MODE_OBJECT = 13;
//	};
		
	public static interface bContextWMCloseCallback {
        public void run(bContext C);
    }

    public static interface bContextDataCallback {
        public boolean run(bContext C, byte[] member, bContextDataResult result);
    }

    public static class bContextStoreEntry extends Link<bContextStoreEntry> {
	public byte[] name = new byte[128];
	public PointerRNA ptr = new PointerRNA();
    };

    public static class bContextStore extends Link<bContextStore> {
	public ListBase entries = new ListBase();
	public int used;
    };

    public static class bContextWM {
        public wmWindowManager manager;
        public wmWindow window;
        public bScreen screen;
        public ScrArea area;
        public ARegion region;
        public ARegion menu;
        public bContextStore store;
        public bContextWMCloseCallback cb;
    };

    public static class bContextData {
        public Main main;
        public Scene scene;

        public int recursion;
        public int py_init; /* true if python is initialized */
    }

    public static class bContextEval {
        public int render;
    }

//struct bContext {
	public int thread;
	
	// TMP
	//public GL2 gl;

	/* windowmanager context */
//	struct {
//		struct wmWindowManager *manager;
//		struct wmWindow *window;
//		struct bScreen *screen;
//		struct ScrArea *area;
//		struct ARegion *region;
//		struct ARegion *menu;
//		struct bContextStore *store;
//	} wm;
        public bContextWM wm = new bContextWM();

	/* data context */
//	struct {
//		struct Main *main;
//		struct Scene *scene;
//
//		int recursion;
//		int py_init; /* true if python is initialized */
//	} data;
        public bContextData data = new bContextData();

	/* data evaluation */
//	struct {
//		int render;
//	} eval;
        public bContextEval eval = new bContextEval();
//};

    /* context */

public static bContext CTX_create()
{
	bContext C;

        C= new bContext();

	return C;
}

//bContext *CTX_copy(const bContext *C)
//{
//	bContext *newC= MEM_dupallocN((void*)C);
//
//	return newC;
//}
//
//void CTX_free(bContext *C)
//{
//	MEM_freeN(C);
//}
//
///* store */
//
//bContextStore *CTX_store_add(ListBase *contexts, char *name, PointerRNA *ptr)
//{
//	bContextStoreEntry *entry;
//	bContextStore *ctx, *lastctx;
//
//	/* ensure we have a context to put the entry in, if it was already used
//	 * we have to copy the context to ensure */
//	ctx= contexts.last;
//
//	if(!ctx || ctx.used) {
//		if(ctx) {
//			lastctx= ctx;
//			ctx= MEM_dupallocN(lastctx);
//			BLI_duplicatelist(&ctx.entries, &lastctx.entries);
//		}
//		else
//			ctx= MEM_callocN(sizeof(bContextStore), "bContextStore");
//
//		BLI_addtail(contexts, ctx);
//	}
//
//	entry= MEM_callocN(sizeof(bContextStoreEntry), "bContextStoreEntry");
//	BLI_strncpy(entry.name, name, sizeof(entry.name));
//	entry.ptr= *ptr;
//
//	BLI_addtail(&ctx.entries, entry);
//
//	return ctx;
//}
//
//void CTX_store_set(bContext *C, bContextStore *store)
//{
//	C.wm.store= store;
//}
//
//bContextStore *CTX_store_copy(bContextStore *store)
//{
//	bContextStore *ctx;
//
//	ctx= MEM_dupallocN(store);
//	BLI_duplicatelist(&ctx.entries, &store.entries);
//
//	return ctx;
//}
//
//void CTX_store_free(bContextStore *store)
//{
//	BLI_freelistN(&store.entries);
//	MEM_freeN(store);
//}
//
//void CTX_store_free_list(ListBase *contexts)
//{
//	bContextStore *ctx;
//
//	while((ctx= contexts.first)) {
//		BLI_remlink(contexts, ctx);
//		CTX_store_free(ctx);
//	}
//}

/* is python initialied? */
public static boolean CTX_py_init_get(bContext C)
{
//	return C.data.py_init;
        return true; // TMP
}
//void CTX_py_init_set(bContext *C, int value)
//{
//	C.data.py_init= value;
//}

public static String wm_title(bContext C) {
	return StringUtil.toJString(G.main.name,0);
}

/* window manager context */

public static wmWindowManager CTX_wm_manager(bContext C)
{
	return C.wm.manager;
}

public static wmWindow CTX_wm_window(bContext C)
{
	return C.wm.window;
}

public static bScreen CTX_wm_screen(bContext C)
{
	return C.wm.screen;
}

public static ScrArea CTX_wm_area(bContext C)
{
	return C.wm.area;
}

public static SpaceLink CTX_wm_space_data(bContext C)
{
	return (C.wm.area!=null)? (SpaceLink)C.wm.area.spacedata.first: null;
}

public static ARegion CTX_wm_region(bContext C)
{
	return C.wm.region;
}

//void *CTX_wm_region_data(const bContext *C)
//{
//	return (C.wm.region)? C.wm.region.regiondata: NULL;
//}

public static ARegion CTX_wm_menu(bContext C)
{
	return C.wm.menu;
}

//struct ReportList *CTX_wm_reports(const bContext *C)
//{
//	return &(C.wm.manager.reports);
//}

public static View3D CTX_wm_view3d(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_VIEW3D)
		return (View3D)C.wm.area.spacedata.first;
	return null;
}

public static RegionView3D CTX_wm_region_view3d(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_VIEW3D)
		if(C.wm.region!=null)
			return (RegionView3D)C.wm.region.regiondata;
	return null;
}

//struct SpaceText *CTX_wm_space_text(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_TEXT)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceConsole *CTX_wm_space_console(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_CONSOLE)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceImage *CTX_wm_space_image(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_IMAGE)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}

public static SpaceButs CTX_wm_space_buts(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_BUTS)
		return (SpaceButs)C.wm.area.spacedata.first;
	return null;
}

//struct SpaceFile *CTX_wm_space_file(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_FILE)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceSeq *CTX_wm_space_seq(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_SEQ)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}

public static SpaceOops CTX_wm_space_outliner(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_OUTLINER)
		return (SpaceOops)C.wm.area.spacedata.first;
	return null;
}

//struct SpaceNla *CTX_wm_space_nla(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_NLA)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}

public static SpaceTime CTX_wm_space_time(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_TIME)
		return (SpaceTime)C.wm.area.spacedata.first;
	return null;
}

public static SpaceNode CTX_wm_space_node(bContext C)
{
	if(C.wm.area!=null && C.wm.area.spacetype==SpaceTypes.SPACE_NODE)
		return (SpaceNode)C.wm.area.spacedata.first;
	return null;
}

//struct SpaceLogic *CTX_wm_space_logic(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_LOGIC)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceIpo *CTX_wm_space_graph(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_IPO)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceAction *CTX_wm_space_action(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_ACTION)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}
//
//struct SpaceInfo *CTX_wm_space_info(const bContext *C)
//{
//	if(C.wm.area && C.wm.area.spacetype==SPACE_INFO)
//		return C.wm.area.spacedata.first;
//	return NULL;
//}

public static void CTX_wm_manager_close(bContext C)
{
	if (C.wm.cb != null) {
		C.wm.cb.run(C);
	}
}

public static void CTX_wm_manager_close_set(bContext C, bContextWMCloseCallback cb)
{
	C.wm.cb = cb;
}

public static void CTX_wm_manager_set(bContext C, wmWindowManager wm)
{
	C.wm.manager= wm;
	C.wm.window= null;
	C.wm.screen= null;
	C.wm.area= null;
	C.wm.region= null;
}

public static void CTX_wm_window_set(bContext C, wmWindow win)
{
	C.wm.window= win;
	C.wm.screen= (win!=null)? win.screen: null;
	C.data.scene= (C.wm.screen!=null)? C.wm.screen.scene: null;
	C.wm.area= null;
	C.wm.region= null;
}

public static void CTX_wm_screen_set(bContext C, bScreen screen)
{
	C.wm.screen= screen;
	C.data.scene= (C.wm.screen!=null)? C.wm.screen.scene: null;
	C.wm.area= null;
	C.wm.region= null;
}

public static void CTX_wm_area_set(bContext C, ScrArea area)
{
	C.wm.area= area;
	C.wm.region= null;
}

public static void CTX_wm_region_set(bContext C, ARegion region)
{
	C.wm.region= region;
}

public static void CTX_wm_menu_set(bContext C, ARegion menu)
{
	C.wm.menu= menu;
}

public static void CTX_wm_operator_poll_msg_set(bContext C, String msg)
{
//	C.wm.operator_poll_msg= msg;
}

/* data context utility functions */

    public static class bContextDataResult {
	public PointerRNA ptr = new PointerRNA();
	public ListBase list = new ListBase();
        public String[] dir;
        public void clear() {
            ptr = new PointerRNA();
            list = new ListBase();
            dir = null;
        }
    };


public static boolean ctx_data_get(bContext C, String member, bContextDataResult result)
{
	boolean done= false;
        int recursion= C.data.recursion;

//	memset(result, 0, sizeof(bContextDataResult));
        result.clear();

	/* we check recursion to ensure that we do not get infinite
	 * loops requesting data from ourselfs in a context callback */
	if(!done && recursion < 1 && C.wm.store!=null) {
		bContextStoreEntry entry;

		C.data.recursion= 1;

		for(entry=(bContextStoreEntry)C.wm.store.entries.first; entry!=null; entry=entry.next) {
			if(StringUtil.strcmp(entry.name,0, StringUtil.toCString(member),0) == 0) {
				result.ptr= entry.ptr;
				done= true;
			}
		}
	}
	if(!done && recursion < 2 && C.wm.region!=null) {
		C.data.recursion= 2;
//		if(C.wm.region.type!=null)
//			done= ((ARegionType)C.wm.region.type).context.run(C, StringUtil.toCString(member), result);
	}
	if(!done && recursion < 3 && C.wm.area!=null) {
		C.data.recursion= 3;
//		if(C.wm.area.type!=null)
//			done= ((SpaceType)C.wm.area.type).context.run(C, StringUtil.toCString(member), result);
	}
	if(!done && recursion < 4 && C.wm.screen!=null) {
		bContextDataCallback cb= (bContextDataCallback)C.wm.screen.context;
		C.data.recursion= 4;
		if(cb!=null)
			done= cb.run(C, StringUtil.toCString(member), result);
	}

	C.data.recursion= recursion;

	return done;
}

static Object ctx_data_pointer_get(bContext C, String member)
{
	bContextDataResult result = new bContextDataResult();

	if(ctx_data_get(C, member, result))
		return result.ptr.data;

	return null;
}

//public static boolean ctx_data_pointer_verify(bContext C, byte[] member, Object[] pointer)
//{
//	bContextDataResult result = new bContextDataResult();
//
//	if(ctx_data_get(C, member, result)) {
//		pointer[0]= result.ptr.data;
//		return true;
//	}
//	else {
//		pointer[0]= null;
//		return false;
//	}
//}

static boolean ctx_data_collection_get(bContext C, String member, ListBase list)
{
	bContextDataResult result = new bContextDataResult();

	if(ctx_data_get(C, member, result)) {
//		*list= result.list;
                list.first= result.list.first;
                list.last= result.list.last;
		return true;
	}

	list.first= null;
	list.last= null;
	return false;
}

//PointerRNA CTX_data_pointer_get(const bContext *C, const char *member)
//{
//	bContextDataResult result;
//
//	if(ctx_data_get((bContext*)C, member, &result))
//		return result.ptr;
//	else
//		return PointerRNA_NULL;
//}
//
//PointerRNA CTX_data_pointer_get_type(const bContext *C, const char *member, StructRNA *type)
//{
//	PointerRNA ptr = CTX_data_pointer_get(C, member);
//
//	if(ptr.data && RNA_struct_is_a(ptr.type, type))
//		return ptr;
//
//	return PointerRNA_NULL;
//}
//
//ListBase CTX_data_collection_get(const bContext *C, const char *member)
//{
//	bContextDataResult result;
//
//	if(ctx_data_get((bContext*)C, member, &result)) {
//		return result.list;
//	}
//	else {
//		ListBase list;
//		memset(&list, 0, sizeof(list));
//		return list;
//	}
//}

//public static void CTX_data_get(bContext C, const char *member, PointerRNA *r_ptr, ListBase *r_lb)
//{
//	bContextDataResult result = new bContextDataResult();
//
//	if(ctx_data_get((bContext*)C, member, &result)) {
//		*r_ptr= result.ptr;
//		*r_lb= result.list;
//	}
//	else {
//		memset(r_ptr, 0, sizeof(*r_ptr));
//		memset(r_lb, 0, sizeof(*r_lb));
//	}
//}

//static void data_dir_add(ListBase *lb, const char *member)
//{
//	LinkData *link;
//
//	if(strcmp(member, "scene") == 0) /* exception */
//		return;
//
//	for(link=lb.first; link; link=link.next)
//		if(strcmp(link.data, member) == 0)
//			return;
//
//	link= MEM_callocN(sizeof(LinkData), "LinkData");
//	link.data= (void*)member;
//	BLI_addtail(lb, link);
//}
//
//ListBase CTX_data_dir_get(const bContext *C)
//{
//	bContextDataResult result;
//	ListBase lb;
//	int a;
//
//	memset(&lb, 0, sizeof(lb));
//
//	if(C.wm.store) {
//		bContextStoreEntry *entry;
//
//		for(entry=C.wm.store.entries.first; entry; entry=entry.next)
//			data_dir_add(&lb, entry.name);
//	}
//	if(C.wm.region && C.wm.region.type && C.wm.region.type.context) {
//		memset(&result, 0, sizeof(result));
//		C.wm.region.type.context(C, "", &result);
//
//		if(result.dir)
//			for(a=0; result.dir[a]; a++)
//				data_dir_add(&lb, result.dir[a]);
//	}
//	if(C.wm.area && C.wm.area.type && C.wm.area.type.context) {
//		memset(&result, 0, sizeof(result));
//		C.wm.area.type.context(C, "", &result);
//
//		if(result.dir)
//			for(a=0; result.dir[a]; a++)
//				data_dir_add(&lb, result.dir[a]);
//	}
//	if(C.wm.screen && C.wm.screen.context) {
//		bContextDataCallback cb= C.wm.screen.context;
//		memset(&result, 0, sizeof(result));
//		cb(C, "", &result);
//
//		if(result.dir)
//			for(a=0; result.dir[a]; a++)
//				data_dir_add(&lb, result.dir[a]);
//	}
//
//	return lb;
//}

public static boolean CTX_data_equals(byte[] member, String str)
{
	return (StringUtil.strcmp(member,0, StringUtil.toCString(str),0) == 0);
}

public static boolean CTX_data_dir(byte[] member)
{
	return (StringUtil.strcmp(member,0, StringUtil.toCString(""),0) == 0);
}

public static void CTX_data_id_pointer_set(bContextDataResult result, ID id)
{
	RnaAccess.RNA_id_pointer_create(id, result.ptr);
}

public static void CTX_data_pointer_set(bContextDataResult result, ID id, StructRNA type, Object data)
{
	RnaAccess.RNA_pointer_create(id, type, data, result.ptr);
}

public static void CTX_data_id_list_add(bContextDataResult result, ID id)
{
	CollectionPointerLink link;

	link= new CollectionPointerLink();
	RnaAccess.RNA_id_pointer_create(id, link.ptr);

	ListBaseUtil.BLI_addtail(result.list, link);
}

public static void CTX_data_list_add(bContextDataResult result, ID id, StructRNA type, Object data)
{
	CollectionPointerLink link;

	link= new CollectionPointerLink();
	RnaAccess.RNA_pointer_create(id, type, data, link.ptr);

	ListBaseUtil.BLI_addtail(result.list, link);
}

//int ctx_data_list_count(const bContext *C, int (*func)(const bContext*, ListBase*))
//{
//	ListBase list;
//
//	if(func(C, &list)) {
//		int tot= BLI_countlist(&list);
//		BLI_freelistN(&list);
//		return tot;
//	}
//	else
//		return 0;
//}

public static void CTX_data_dir_set(bContextDataResult result, String[] dir)
{
	result.dir= dir;
}

/* data context */

public static Main CTX_data_main(bContext C)
{
//	Main[] bmain={null};

//	if(ctx_data_pointer_verify(C, StringUtil.toCString("main"), bmain))
//		return bmain[0];
//	else
		return C.data.main;
}

public static void CTX_data_main_set(bContext C, Main bmain)
{
	C.data.main= bmain;
}

public static Scene CTX_data_scene(bContext C)
{
//	Scene scene;

//	if(ctx_data_pointer_verify(C, "scene", (void*)&scene))
//		return scene;
//	else
		return C.data.scene;
}

public static double scene_fps(bContext C) {
	return (((double) C.data.scene.r.frs_sec) / C.data.scene.r.frs_sec_base);
}

public static int CTX_data_mode_enum(bContext C)
{
	bObject obedit= CTX_data_edit_object(C);

	if(obedit!=null) {
		switch(obedit.type) {
			case ObjectTypes.OB_MESH:
				return CTX_MODE_EDIT_MESH;
			case ObjectTypes.OB_CURVE:
				return CTX_MODE_EDIT_CURVE;
			case ObjectTypes.OB_SURF:
				return CTX_MODE_EDIT_SURFACE;
			case ObjectTypes.OB_FONT:
				return CTX_MODE_EDIT_TEXT;
			case ObjectTypes.OB_ARMATURE:
				return CTX_MODE_EDIT_ARMATURE;
			case ObjectTypes.OB_MBALL:
				return CTX_MODE_EDIT_METABALL;
			case ObjectTypes.OB_LATTICE:
				return CTX_MODE_EDIT_LATTICE;
		}
	}
	else {
//		bObject ob = CTX_data_active_object(C);
//
//		if(ob!=null) {
//			if((ob.mode & ObjectTypes.OB_MODE_POSE)!=0) return CTX_MODE_POSE;
//			else if(ob->mode & OB_MODE_SCULPT)  return CTX_MODE_SCULPT;
//			else if(ob->mode & OB_MODE_WEIGHT_PAINT) return CTX_MODE_PAINT_WEIGHT;
//			else if(ob->mode & OB_MODE_VERTEX_PAINT) return CTX_MODE_PAINT_VERTEX;
//			else if(ob->mode & OB_MODE_TEXTURE_PAINT) return CTX_MODE_PAINT_TEXTURE;
//			else if(ob->mode & OB_MODE_PARTICLE_EDIT) return CTX_MODE_PARTICLE;
//		}
	}

	return CTX_MODE_OBJECT;
}


/* would prefer if we can use the enum version below over this one - Campbell */
/* must be aligned with above enum  */
static final String[] data_mode_strings = {
	"mesh_edit",
	"curve_edit",
	"surface_edit",
	"text_edit",
	"armature_edit",
	"mball_edit",
	"lattice_edit",
	"posemode",
	"sculpt_mode",
	"weightpaint",
	"vertexpaint",
	"texturepaint",
	"particlemode",
	"objectmode",
};
public static String CTX_data_mode_string(bContext C)
{
	return data_mode_strings[CTX_data_mode_enum(C)];
}

public static void CTX_data_scene_set(bContext C, Scene scene)
{
	C.data.scene= scene;
}

public static ToolSettings CTX_data_tool_settings(bContext C)
{
	Scene scene = bContext.CTX_data_scene(C);

	if(scene!=null)
		return scene.toolsettings;
	else
		return null;
}

//int CTX_data_selected_nodes(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_nodes", list);
//}
//
//int CTX_data_selected_editable_objects(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_editable_objects", list);
//}
//
//int CTX_data_selected_editable_bases(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_editable_bases", list);
//}

public static boolean CTX_data_selected_objects(bContext C, ListBase list)
{
	return ctx_data_collection_get(C, "selected_objects", list);

//    // TMP
//    return 0;
}

public static boolean CTX_data_selected_bases(bContext C, ListBase list)
{
	return ctx_data_collection_get(C, "selected_bases", list);

//    // TMP
//    return 0;
}

//int CTX_data_visible_objects(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "visible_objects", list);
//}
//
//int CTX_data_visible_bases(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "visible_bases", list);
//}
//
//int CTX_data_selectable_objects(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selectable_objects", list);
//}
//
//int CTX_data_selectable_bases(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selectable_bases", list);
//}

public static bObject CTX_data_active_object(bContext C)
{
	return (bObject)ctx_data_pointer_get(C, "active_object");
//    // TMP
//    return null;
//	return new bObject();
}

public static Base CTX_data_active_base(bContext C)
{
	return (Base)ctx_data_pointer_get(C, "active_base");
//    // TMP
//    return null;
}

public static bObject CTX_data_edit_object(bContext C)
{
	return (bObject)ctx_data_pointer_get(C, "edit_object");
//    // TMP
//    return null;
}

//struct Image *CTX_data_edit_image(const bContext *C)
//{
//	return ctx_data_pointer_get(C, "edit_image");
//}
//
//struct Text *CTX_data_edit_text(const bContext *C)
//{
//	return ctx_data_pointer_get(C, "edit_text");
//}
//
//struct EditBone *CTX_data_active_bone(const bContext *C)
//{
//	return ctx_data_pointer_get(C, "active_bone");
//}
//
//int CTX_data_selected_bones(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_bones", list);
//}
//
//int CTX_data_selected_editable_bones(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_editable_bones", list);
//}
//
//int CTX_data_visible_bones(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "visible_bones", list);
//}
//
//int CTX_data_editable_bones(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "editable_bones", list);
//}
//
//struct bPoseChannel *CTX_data_active_pchan(const bContext *C)
//{
//	return ctx_data_pointer_get(C, "active_pchan");
//}
//
//int CTX_data_selected_pchans(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "selected_pchans", list);
//}
//
//int CTX_data_visible_pchans(const bContext *C, ListBase *list)
//{
//	return ctx_data_collection_get(C, "visible_pchans", list);
//}
}
