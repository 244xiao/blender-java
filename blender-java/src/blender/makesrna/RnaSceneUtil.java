package blender.makesrna;

import static blender.editors.uinterface.Resources.BIFIconID.*;
import static blender.makesdna.SceneTypes.*;
import static blender.windowmanager.WmTypes.*;
import static blender.makesrna.RnaDefine.*;
import static blender.makesrna.RNATypes.*;
import static blender.makesdna.UserDefTypes.*;
import static blender.blenkernel.Writeffmpeg.*;
import static blender.render.RePipeline.*;
import blender.blenlib.threads;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.RenderData;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.SceneRenderLayer;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.*;
import blender.makesrna.srna.RnaSceneRenderLayer;

public class RnaSceneUtil {
//	/**
//	 * $Id: rna_scene.c 33939 2010-12-29 18:21:57Z ton $
//	 *
//	 * ***** BEGIN GPL LICENSE BLOCK *****
//	 *
//	 * This program is free software; you can redistribute it and/or
//	 * modify it under the terms of the GNU General Public License
//	 * as published by the Free Software Foundation; either version 2
//	 * of the License, or (at your option) any later version.
//	 *
//	 * This program is distributed in the hope that it will be useful,
//	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
//	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	 * GNU General Public License for more details.
//	 *
//	 * You should have received a copy of the GNU General Public License
//	 * along with this program; if not, write to the Free Software Foundation,
//	 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
//	 *
//	 * Contributor(s): Blender Foundation (2008).
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdlib.h>
//
//	#include "RNA_define.h"
//	#include "RNA_enum_types.h"
//
//	#include "rna_internal.h"
//
//	#include "DNA_group_types.h"
//	#include "DNA_modifier_types.h"
//	#include "DNA_particle_types.h"
//	#include "DNA_scene_types.h"
//	#include "DNA_userdef_types.h"
//
//	/* Include for Bake Options */
//	#include "RE_pipeline.h"
//
//	#ifdef WITH_QUICKTIME
//	#include "quicktime_export.h"
//	#include "AUD_C-API.h"
//	#endif
//
//	#ifdef WITH_FFMPEG
//	#include "BKE_writeffmpeg.h"
//	#include <libavcodec/avcodec.h> 
//	#include <libavformat/avformat.h>
//	#endif
//
//	#include "WM_types.h"
//
//	#include "BLI_threads.h"

	static EnumPropertyItem snap_target_items[] = {
		new EnumPropertyItem(SCE_SNAP_TARGET_CLOSEST, "CLOSEST", 0, "Closest", "Snap closest point onto target"),
		new EnumPropertyItem(SCE_SNAP_TARGET_CENTER, "CENTER", 0, "Center", "Snap center onto target"),
		new EnumPropertyItem(SCE_SNAP_TARGET_MEDIAN, "MEDIAN", 0, "Median", "Snap median onto target"),
		new EnumPropertyItem(SCE_SNAP_TARGET_ACTIVE, "ACTIVE", 0, "Active", "Snap active onto target"),
		new EnumPropertyItem(0, null, 0, null, null)};
		
	static EnumPropertyItem proportional_falloff_items[] ={
		new EnumPropertyItem(PROP_SMOOTH, "SMOOTH", ICON_SMOOTHCURVE, "Smooth", ""),
		new EnumPropertyItem(PROP_SPHERE, "SPHERE", ICON_SPHERECURVE, "Sphere", ""),
		new EnumPropertyItem(PROP_ROOT, "ROOT", ICON_ROOTCURVE, "Root", ""),
		new EnumPropertyItem(PROP_SHARP, "SHARP", ICON_SHARPCURVE, "Sharp", ""),
		new EnumPropertyItem(PROP_LIN, "LINEAR", ICON_LINCURVE, "Linear", ""),
		new EnumPropertyItem(PROP_CONST, "CONSTANT", ICON_NOCURVE, "Constant", ""),
		new EnumPropertyItem(PROP_RANDOM, "RANDOM", ICON_RNDCURVE, "Random", ""),
		new EnumPropertyItem(0, null, 0, null, null)};


	static EnumPropertyItem proportional_editing_items[] = {
		new EnumPropertyItem(PROP_EDIT_OFF, "DISABLED", ICON_PROP_OFF, "Disable", ""),
		new EnumPropertyItem(PROP_EDIT_ON, "ENABLED", ICON_PROP_ON, "Enable", ""),
		new EnumPropertyItem(PROP_EDIT_CONNECTED, "CONNECTED", ICON_PROP_CON, "Connected", ""),
		new EnumPropertyItem(0, null, 0, null, null)};

//	/* keep for operators, not used here */
//	EnumPropertyItem mesh_select_mode_items[] = {
//		{SCE_SELECT_VERTEX, "VERTEX", ICON_VERTEXSEL, "Vertex", "Vertex selection mode"},
//		{SCE_SELECT_EDGE, "EDGE", ICON_EDGESEL, "Edge", "Edge selection mode"},
//		{SCE_SELECT_FACE, "FACE", ICON_FACESEL, "Face", "Face selection mode"},
//		{0, NULL, 0, NULL, NULL}};

	static EnumPropertyItem snap_element_items[] = {
		new EnumPropertyItem(SCE_SNAP_MODE_INCREMENT, "INCREMENT", ICON_SNAP_INCREMENT, "Increment", "Snap to increments of grid"),
		new EnumPropertyItem(SCE_SNAP_MODE_VERTEX, "VERTEX", ICON_SNAP_VERTEX, "Vertex", "Snap to vertices"),
		new EnumPropertyItem(SCE_SNAP_MODE_EDGE, "EDGE", ICON_SNAP_EDGE, "Edge", "Snap to edges"),
		new EnumPropertyItem(SCE_SNAP_MODE_FACE, "FACE", ICON_SNAP_FACE, "Face", "Snap to faces"),
		new EnumPropertyItem(SCE_SNAP_MODE_VOLUME, "VOLUME", ICON_SNAP_VOLUME, "Volume", "Snap to volume"),
		new EnumPropertyItem(0, null, 0, null, null)};

	static EnumPropertyItem image_type_items[] = {
		new EnumPropertyItem(0, "", 0, "Image", null),
		new EnumPropertyItem(R_BMP, "BMP", ICON_FILE_IMAGE, "BMP", ""),
		//{R_DDS, "DDS", ICON_FILE_IMAGE, "DDS", ""}, // XXX not yet implemented
		new EnumPropertyItem(R_IRIS, "IRIS", ICON_FILE_IMAGE, "Iris", ""),
		new EnumPropertyItem(R_PNG, "PNG", ICON_FILE_IMAGE, "PNG", ""),
		new EnumPropertyItem(R_JPEG90, "JPEG", ICON_FILE_IMAGE, "JPEG", ""),
//	#ifdef WITH_OPENJPEG
//		{R_JP2, "JPEG2000", ICON_FILE_IMAGE, "JPEG 2000", ""},
//	#endif
		new EnumPropertyItem(R_TARGA, "TARGA", ICON_FILE_IMAGE, "Targa", ""),
		new EnumPropertyItem(R_RAWTGA, "TARGA_RAW", ICON_FILE_IMAGE, "Targa Raw", ""),
		new EnumPropertyItem(0, "", 0, " ", null),
//	#ifdef WITH_CINEON
//		{R_CINEON, "CINEON", ICON_FILE_IMAGE, "Cineon", ""},
//		{R_DPX, "DPX",ICON_FILE_IMAGE, "DPX", ""},
//	#endif
//	#ifdef WITH_OPENEXR
//		{R_MULTILAYER, "MULTILAYER", ICON_FILE_IMAGE, "MultiLayer", ""},
//		{R_OPENEXR, "OPEN_EXR", ICON_FILE_IMAGE, "OpenEXR", ""},
//	#endif
//	#ifdef WITH_HDR
//		{R_RADHDR, "HDR", ICON_FILE_IMAGE, "Radiance HDR", ""},
//	#endif
//	#ifdef WITH_TIFF
//		{R_TIFF, "TIFF", ICON_FILE_IMAGE, "TIFF", ""},
//	#endif
		new EnumPropertyItem(0, "", 0, "Movie", null),
//	#ifdef _WIN32
		new EnumPropertyItem(R_AVICODEC, "AVICODEC", ICON_FILE_MOVIE, "AVI Codec", ""), // XXX Missing codec menu
//	#endif
		new EnumPropertyItem(R_AVIJPEG, "AVI_JPEG", ICON_FILE_MOVIE, "AVI JPEG", ""),
		new EnumPropertyItem(R_AVIRAW, "AVI_RAW", ICON_FILE_MOVIE, "AVI Raw", ""),
		new EnumPropertyItem(R_FRAMESERVER, "FRAMESERVER", ICON_FILE_SCRIPT, "Frame Server", ""),
//	#ifdef WITH_FFMPEG
//		{R_H264, "H264", ICON_FILE_MOVIE, "H.264", ""},
//		{R_FFMPEG, "FFMPEG", ICON_FILE_MOVIE, "MPEG", ""},
//		{R_THEORA, "THEORA", ICON_FILE_MOVIE, "Ogg Theora", ""},
//	#endif
//	#ifdef WITH_QUICKTIME
//	#	ifdef USE_QTKIT
//		{R_QUICKTIME, "QUICKTIME_QTKIT", ICON_FILE_MOVIE, "QuickTime", ""},
//	#	else
//		{R_QUICKTIME, "QUICKTIME_CARBON", ICON_FILE_MOVIE, "QuickTime", ""},
//	#	endif
//	#endif
//	#ifdef WITH_FFMPEG
//		{R_XVID, "XVID", ICON_FILE_MOVIE, "Xvid", ""},
//	#endif
		new EnumPropertyItem(0, null, 0, null, null)};

//	#ifdef RNA_RUNTIME
//
//	#include "DNA_anim_types.h"
//	#include "DNA_node_types.h"
//	#include "DNA_object_types.h"
//	#include "DNA_mesh_types.h"
//
//	#include "RNA_access.h"
//
//	#include "MEM_guardedalloc.h"
//
//	#include "BKE_context.h"
//	#include "BKE_global.h"
//	#include "BKE_image.h"
//	#include "BKE_main.h"
//	#include "BKE_node.h"
//	#include "BKE_pointcache.h"
//	#include "BKE_scene.h"
//	#include "BKE_depsgraph.h"
//	#include "BKE_image.h"
//	#include "BKE_mesh.h"
//	#include "BKE_sound.h"
//	#include "BKE_screen.h"
//	#include "BKE_animsys.h"
//
//	#include "BLI_threads.h"
//	#include "BLI_editVert.h"
//	#include "BLI_blenlib.h"
//
//	#include "WM_api.h"
//
//	#include "ED_info.h"
//	#include "ED_node.h"
//	#include "ED_view3d.h"
//	#include "ED_mesh.h"
//	#include "ED_keyframing.h"
//
//	#include "RE_pipeline.h"
//
//	static PointerRNA rna_Scene_object_bases_lookup_string(PointerRNA *ptr, const char *key)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		Base *base;
//
//		for(base= scene->base.first; base; base= base->next) {
//			if(strncmp(base->object->id.name+2, key, sizeof(base->object->id.name)-2)==0) {
//				return rna_pointer_inherit_refine(ptr, &RNA_ObjectBase, base);
//			}
//		}
//
//		return PointerRNA_NULL;
//	}
//
//	static PointerRNA rna_Scene_objects_get(CollectionPropertyIterator *iter)
//	{
//		ListBaseIterator *internal= iter->internal;
//
//		/* we are actually iterating a Base list, so override get */
//		return rna_pointer_inherit_refine(&iter->parent, &RNA_Object, ((Base*)internal->link)->object);
//	}
//
//	static Base *rna_Scene_object_link(Scene *scene, bContext *C, ReportList *reports, Object *ob)
//	{
//		Scene *scene_act= CTX_data_scene(C);
//		Base *base;
//
//		if (object_in_scene(ob, scene)) {
//			BKE_reportf(reports, RPT_ERROR, "Object \"%s\" is already in scene \"%s\".", ob->id.name+2, scene->id.name+2);
//			return NULL;
//		}
//
//		base= scene_add_base(scene, ob);
//		ob->id.us++;
//
//		/* this is similar to what object_add_type and add_object do */
//		base->lay= scene->lay;
//
//		/* when linking to an inactive scene dont touch the layer */
//		if(scene == scene_act)
//			ob->lay= base->lay;
//
//		ob->recalc |= OB_RECALC_OB|OB_RECALC_DATA|OB_RECALC_TIME;
//
//		/* slows down importers too much, run scene.update() */
//		/* DAG_scene_sort(G.main, scene); */
//
//		WM_main_add_notifier(NC_SCENE|ND_OB_ACTIVE, scene);
//
//		return base;
//	}
//
//	static void rna_Scene_object_unlink(Scene *scene, ReportList *reports, Object *ob)
//	{
//		Base *base= object_in_scene(ob, scene);
//		if (!base) {
//			BKE_reportf(reports, RPT_ERROR, "Object '%s' is not in this scene '%s'.", ob->id.name+2, scene->id.name+2);
//			return;
//		}
//		if (base==scene->basact && ob->mode != OB_MODE_OBJECT) {
//			BKE_reportf(reports, RPT_ERROR, "Object '%s' must be in 'Object Mode' to unlink.", ob->id.name+2);
//			return;
//		}
//		if(scene->basact==base) {
//			scene->basact= NULL;
//		}
//
//		BLI_remlink(&scene->base, base);
//		MEM_freeN(base);
//
//		ob->id.us--;
//
//		/* needed otherwise the depgraph will contain free'd objects which can crash, see [#20958] */
//		DAG_scene_sort(G.main, scene);
//		DAG_ids_flush_update(G.main, 0);
//
//		WM_main_add_notifier(NC_SCENE|ND_OB_ACTIVE, scene);
//	}
//
//	static void rna_Scene_skgen_etch_template_set(PointerRNA *ptr, PointerRNA value)
//	{
//		ToolSettings *ts = (ToolSettings*)ptr->data;
//		if(value.data && ((Object*)value.data)->type == OB_ARMATURE)
//			ts->skgen_template = value.data;
//		else
//			ts->skgen_template = NULL;
//	}
//
//	static PointerRNA rna_Scene_active_object_get(PointerRNA *ptr)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		return rna_pointer_inherit_refine(ptr, &RNA_Object, scene->basact ? scene->basact->object : NULL);
//	}
//
//	static void rna_Scene_active_object_set(PointerRNA *ptr, PointerRNA value)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		if(value.data)
//			scene->basact= object_in_scene((Object*)value.data, scene);
//		else
//			scene->basact= NULL;
//	}
//
//	static void rna_Scene_set_set(PointerRNA *ptr, PointerRNA value)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		Scene *set= (Scene*)value.data;
//		Scene *nested_set;
//
//		for(nested_set= set; nested_set; nested_set= nested_set->set) {
//			if(nested_set==scene)
//				return;
//		}
//
//		scene->set= set;
//	}
//
//	static void rna_Scene_layer_set(PointerRNA *ptr, const int *values)
//	{
//		Scene *scene= (Scene*)ptr->data;
//
//		scene->lay= ED_view3d_scene_layer_set(scene->lay, values, &scene->layact);
//	}

	public static UpdateFunc rna_Scene_view3d_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Scene_view3d_update(Main *bmain, Scene *unused, PointerRNA *ptr)
	{
		Scene scene= (Scene)ptr.data;

//		BKE_screen_view3d_main_sync(bmain.screen, scene);
	}};

	public static UpdateFunc rna_Scene_framelen_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Scene_framelen_update(Main *bmain, Scene *scene, PointerRNA *ptr)
	{	
//		scene.r.framelen= (float)scene.r.framapto/(float)scene.r.images;
	}};

	public static PropIntSetFunc rna_Scene_current_frame_set = new PropIntSetFunc() {
		public void setInt(PointerRNA ptr, int value)
//	public static void rna_Scene_current_frame_set(PointerRNA ptr, int value)
	{
		Scene data= (Scene)ptr.data;
		
		/* if negative frames aren't allowed, then we can't use them */
		value = FRAMENUMBER_MIN_CLAMP(value);
		data.r.cfra= value;
	}};

	public static PropIntSetFunc rna_Scene_start_frame_set = new PropIntSetFunc() {
		public void setInt(PointerRNA ptr, int value)
//	public static void rna_Scene_start_frame_set(PointerRNA ptr, int value)
	{
		Scene data= (Scene)ptr.data;
		/* MINFRAME not MINAFRAME, since some output formats can't taken negative frames */
		value = Arithb.CLAMP(value, MINFRAME, data.r.efra);
		data.r.sfra= value;
	}};

	public static PropIntSetFunc rna_Scene_end_frame_set = new PropIntSetFunc() {
		public void setInt(PointerRNA ptr, int value)
//	public static void rna_Scene_end_frame_set(PointerRNA ptr, int value)
	{
		Scene data= (Scene)ptr.data;
		value = Arithb.CLAMP(value, data.r.sfra, MAXFRAME);
		data.r.efra= value;
//		System.out.println("rna_Scene_end_frame_set: "+value);
	}};

	public static void rna_Scene_use_preview_range_set(PointerRNA ptr, boolean value)
	{
		Scene data= (Scene)ptr.data;
		
		if (value) {
			/* copy range from scene if not set before */
			if ((data.r.psfra == data.r.pefra) && (data.r.psfra == 0)) {
				data.r.psfra= data.r.sfra;
				data.r.pefra= data.r.efra;
			}
			
			data.r.flag |= SCER_PRV_RANGE;
		}
		else
			data.r.flag &= ~SCER_PRV_RANGE;
	}


//	static void rna_Scene_preview_range_start_frame_set(PointerRNA *ptr, int value)
//	{
//		Scene *data= (Scene*)ptr->data;
//		
//		/* check if enabled already */
//		if ((data->r.flag & SCER_PRV_RANGE) == 0) {
//			/* set end of preview range to end frame, then clamp as per normal */
//			// TODO: or just refuse to set instead?
//			data->r.pefra= data->r.efra;
//		}
//		
//		/* now set normally */
//		CLAMP(value, MINAFRAME, data->r.pefra);
//		data->r.psfra= value;
//	}
//
//	static void rna_Scene_preview_range_end_frame_set(PointerRNA *ptr, int value)
//	{
//		Scene *data= (Scene*)ptr->data;
//		
//		/* check if enabled already */
//		if ((data->r.flag & SCER_PRV_RANGE) == 0) {
//			/* set start of preview range to start frame, then clamp as per normal */
//			// TODO: or just refuse to set instead?
//			data->r.psfra= data->r.sfra; 
//		}
//		
//		/* now set normally */
//		CLAMP(value, data->r.psfra, MAXFRAME);
//		data->r.pefra= value;
//	}

	public static UpdateFunc rna_Scene_frame_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Scene_frame_update(bContext *C, PointerRNA *ptr)
	{
		//Scene *scene= ptr->id.data;
		//ED_update_for_newframe(C);
//		sound_seek_scene(C);
	}};

//	static PointerRNA rna_Scene_active_keying_set_get(PointerRNA *ptr)
//	{
//		Scene *scene= (Scene *)ptr->data;
//		return rna_pointer_inherit_refine(ptr, &RNA_KeyingSet, ANIM_scene_get_active_keyingset(scene));
//	}
//
//	static void rna_Scene_active_keying_set_set(PointerRNA *ptr, PointerRNA value)
//	{
//		Scene *scene= (Scene *)ptr->data;
//		KeyingSet *ks= (KeyingSet*)value.data;
//		
//		scene->active_keyingset= ANIM_scene_get_keyingset_index(scene, ks);
//	}
//
//	/* get KeyingSet index stuff for list of Keying Sets editing UI
//	 *	- active_keyingset-1 since 0 is reserved for 'none'
//	 *	- don't clamp, otherwise can never set builtins types as active... 
//	 */
//	static int rna_Scene_active_keying_set_index_get(PointerRNA *ptr)
//	{
//		Scene *scene= (Scene *)ptr->data; 	
//		return scene->active_keyingset-1;
//	}
//
//	/* get KeyingSet index stuff for list of Keying Sets editing UI
//	 *	- value+1 since 0 is reserved for 'none'
//	 */
//	static void rna_Scene_active_keying_set_index_set(PointerRNA *ptr, int value) 
//	{
//		Scene *scene= (Scene *)ptr->data; 
//		scene->active_keyingset= value+1; 	 
//	}
//
//	// XXX: evil... builtin_keyingsets is defined in keyingsets.c!
//	// TODO: make API function to retrieve this...
//	extern ListBase builtin_keyingsets;
//
//	static void rna_Scene_all_keyingsets_begin(CollectionPropertyIterator *iter, PointerRNA *ptr)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		
//		/* start going over the scene KeyingSets first, while we still have pointer to it 
//		 * but only if we have any Keying Sets to use...
//		 */
//		if (scene->keyingsets.first)
//			rna_iterator_listbase_begin(iter, &scene->keyingsets, NULL);
//		else
//			rna_iterator_listbase_begin(iter, &builtin_keyingsets, NULL);
//	}
//
//	static void rna_Scene_all_keyingsets_next(CollectionPropertyIterator *iter)
//	{
//		ListBaseIterator *internal= iter->internal;
//		KeyingSet *ks= (KeyingSet*)internal->link;
//		
//		/* if we've run out of links in Scene list, jump over to the builtins list unless we're there already */
//		if ((ks->next == NULL) && (ks != builtin_keyingsets.last))
//			internal->link= (Link*)builtin_keyingsets.first;
//		else
//			internal->link= (Link*)ks->next;
//			
//		iter->valid= (internal->link != NULL);
//	}
//
//
//	static char *rna_RenderSettings_path(PointerRNA *ptr)
//	{
//		return BLI_sprintfN("render");
//	}

	public static PropIntGetFunc rna_RenderSettings_threads_get = new PropIntGetFunc() {
	public int getInt(PointerRNA ptr)
//	static int rna_RenderSettings_threads_get(PointerRNA *ptr)
	{
//		RenderData rd= (RenderData)ptr.data;
//
//		if(rd.mode & R_FIXED_THREADS)
//			return rd.threads;
//		else
//			return BLI_system_thread_count();
		return 1;
	}};

	public static PropBooleanGetFunc rna_RenderSettings_is_movie_fomat_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//	static int rna_RenderSettings_is_movie_fomat_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		return BKE_imtype_is_movie(rd->imtype);
		return false;
	}};

	public static PropBooleanGetFunc rna_RenderSettings_save_buffers_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//	static int rna_RenderSettings_save_buffers_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		if(rd->mode & R_BORDER)
//			return 0;
//		else
//			return (rd->scemode & (R_EXR_TILE_FILE|R_FULL_SAMPLE)) != 0;
		return false;
	}};

	public static PropBooleanGetFunc rna_RenderSettings_full_sample_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//	static int rna_RenderSettings_full_sample_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//
//		return (rd->scemode & R_FULL_SAMPLE) && !(rd->mode & R_BORDER);
		return false;
	}};

	public static PropEnumSetFunc rna_RenderSettings_file_format_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
//	static void rna_RenderSettings_file_format_set(PointerRNA *ptr, int value)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//
//		rd->imtype= value;
//	#ifdef WITH_FFMPEG
//		ffmpeg_verify_image_type(rd);
//	#endif
//	#ifdef WITH_QUICKTIME
//		quicktime_verify_image_type(rd);
//	#endif
	}};

	public static PropStringLengthFunc rna_SceneRender_file_ext_length = new PropStringLengthFunc() {
	public int lenStr(PointerRNA ptr)
//	static int rna_SceneRender_file_ext_length(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		char ext[8];
//		ext[0]= '\0';
//		BKE_add_image_extension(ext, rd->imtype);
//		return strlen(ext);
		return 0;
	}};

	public static PropStringGetFunc rna_SceneRender_file_ext_get = new PropStringGetFunc() {
	public void getStr(PointerRNA ptr, byte[] str, int offset)
//	static void rna_SceneRender_file_ext_get(PointerRNA *ptr, char *str)
	{
//		RenderData rd= (RenderData)ptr.data;
//		BKE_add_image_extension(str, rd.imtype);
	}};

//	void rna_RenderSettings_jpeg2k_preset_update(RenderData *rd)
//	{
//		rd->subimtype &= ~(R_JPEG2K_12BIT|R_JPEG2K_16BIT | R_JPEG2K_CINE_PRESET|R_JPEG2K_CINE_48FPS);
//		
//		switch(rd->jp2_depth) {
//		case 8:		break;
//		case 12:	rd->subimtype |= R_JPEG2K_12BIT; break;
//		case 16:	rd->subimtype |= R_JPEG2K_16BIT; break;
//		}
//		
//		switch(rd->jp2_preset) {
//		case 1: rd->subimtype |= R_JPEG2K_CINE_PRESET;						break;
//		case 2: rd->subimtype |= R_JPEG2K_CINE_PRESET|R_JPEG2K_CINE_48FPS;	break;
//		case 3: rd->subimtype |= R_JPEG2K_CINE_PRESET;						break;
//		case 4: rd->subimtype |= R_JPEG2K_CINE_PRESET;						break;
//		case 5: rd->subimtype |= R_JPEG2K_CINE_PRESET|R_JPEG2K_CINE_48FPS;	break;
//		case 6: rd->subimtype |= R_JPEG2K_CINE_PRESET;						break;
//		case 7: rd->subimtype |= R_JPEG2K_CINE_PRESET|R_JPEG2K_CINE_48FPS;	break;
//		}
//	}
//
//	#ifdef WITH_OPENJPEG
//	static void rna_RenderSettings_jpeg2k_preset_set(PointerRNA *ptr, int value)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//		rd->jp2_preset= value;
//		rna_RenderSettings_jpeg2k_preset_update(rd);
//	}
//
//	static void rna_RenderSettings_jpeg2k_depth_set(PointerRNA *ptr, int value)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//		rd->jp2_depth= value;
//		rna_RenderSettings_jpeg2k_preset_update(rd);
//	}
//	#endif
//
//	#ifdef WITH_QUICKTIME
//	static int rna_RenderSettings_qtcodecsettings_codecType_get(PointerRNA *ptr)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//		
//		return quicktime_rnatmpvalue_from_videocodectype(rd->qtcodecsettings.codecType);
//	}
//
//	static void rna_RenderSettings_qtcodecsettings_codecType_set(PointerRNA *ptr, int value)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//
//		rd->qtcodecsettings.codecType = quicktime_videocodecType_from_rnatmpvalue(value);
//	}
//
//	static EnumPropertyItem *rna_RenderSettings_qtcodecsettings_codecType_itemf(bContext *C, PointerRNA *ptr, int *free)
//	{
//		EnumPropertyItem *item= NULL;
//		EnumPropertyItem tmp = {0, "", 0, "", ""};
//		QuicktimeCodecTypeDesc *codecTypeDesc;
//		int i=1, totitem= 0;
//		char id[5];
//		
//		for(i=0;i<quicktime_get_num_videocodecs();i++) {
//			codecTypeDesc = quicktime_get_videocodecType_desc(i);
//			if (!codecTypeDesc) break;
//			
//			tmp.value= codecTypeDesc->rnatmpvalue;
//			*((int*)id) = codecTypeDesc->codecType;
//			id[4] = 0;
//			tmp.identifier= id; 
//			tmp.name= codecTypeDesc->codecName;
//			RNA_enum_item_add(&item, &totitem, &tmp);
//		}
//		
//		RNA_enum_item_end(&item, &totitem);
//		*free= 1;
//		
//		return item;	
//	}
//
//	#ifdef USE_QTKIT
//	static int rna_RenderSettings_qtcodecsettings_audiocodecType_get(PointerRNA *ptr)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//		
//		return quicktime_rnatmpvalue_from_audiocodectype(rd->qtcodecsettings.audiocodecType);
//	}
//
//	static void rna_RenderSettings_qtcodecsettings_audiocodecType_set(PointerRNA *ptr, int value)
//	{
//		RenderData *rd= (RenderData*)ptr->data;
//		
//		rd->qtcodecsettings.audiocodecType = quicktime_audiocodecType_from_rnatmpvalue(value);
//	}
//
//	static EnumPropertyItem *rna_RenderSettings_qtcodecsettings_audiocodecType_itemf(bContext *C, PointerRNA *ptr, int *free)
//	{
//		EnumPropertyItem *item= NULL;
//		EnumPropertyItem tmp = {0, "", 0, "", ""};
//		QuicktimeCodecTypeDesc *codecTypeDesc;
//		int i=1, totitem= 0;
//		
//		for(i=0;i<quicktime_get_num_audiocodecs();i++) {
//			codecTypeDesc = quicktime_get_audiocodecType_desc(i);
//			if (!codecTypeDesc) break;
//			
//			tmp.value= codecTypeDesc->rnatmpvalue;
//			tmp.identifier= codecTypeDesc->codecName; 
//			tmp.name= codecTypeDesc->codecName;
//			RNA_enum_item_add(&item, &totitem, &tmp);
//		}
//		
//		RNA_enum_item_end(&item, &totitem);
//		*free= 1;
//		
//		return item;	
//	}	
//	#endif
//	#endif

	public static PropIntGetFunc rna_RenderSettings_active_layer_index_get = new PropIntGetFunc() {
	public int getInt(PointerRNA ptr)
//	static int rna_RenderSettings_active_layer_index_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		return rd->actlay;
		return 0;
	}};

	public static PropIntSetFunc rna_RenderSettings_active_layer_index_set = new PropIntSetFunc() {
	public void setInt(PointerRNA ptr, int value)
//	static void rna_RenderSettings_active_layer_index_set(PointerRNA *ptr, int value)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		rd->actlay= value;
	}};

	public static PropIntRangeFunc rna_RenderSettings_active_layer_index_range = new PropIntRangeFunc() {
	public void rangeInt(PointerRNA ptr, int[] min, int[] max)
//	static void rna_RenderSettings_active_layer_index_range(PointerRNA *ptr, int *min, int *max)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//
//		*min= 0;
//		*max= BLI_countlist(&rd->layers)-1;
//		*max= MAX2(0, *max);
	}};

	public static PropPointerGetFunc rna_RenderSettings_active_layer_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		RenderData rd= (RenderData)ptr.data;
		SceneRenderLayer srl = (SceneRenderLayer)ListBaseUtil.BLI_findlink(rd.layers, rd.actlay);
		
		return RnaAccess.rna_pointer_inherit_refine(ptr, RnaSceneRenderLayer.RNA_SceneRenderLayer, srl);
//		return null;
	}};

	public static PropPointerSetFunc rna_RenderSettings_active_layer_set = new PropPointerSetFunc() {
	public void setPtr(PointerRNA ptr, PointerRNA value)
//	static void rna_RenderSettings_active_layer_set(PointerRNA *ptr, PointerRNA value)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		SceneRenderLayer *srl= (SceneRenderLayer*)value.data;
//		
//		rd->actlay = BLI_findindex(&rd->layers, srl);
	}};

	public static PropEnumSetFunc rna_RenderSettings_engine_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
//	static void rna_RenderSettings_engine_set(PointerRNA *ptr, int value)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		RenderEngineType *type= BLI_findlink(&R_engines, value);
//
//		if(type)
//			BLI_strncpy(rd->engine, type->idname, sizeof(rd->engine));
	}};

	public static PropEnumItemFunc rna_RenderSettings_engine_itemf = new PropEnumItemFunc() {
	public EnumPropertyItem[] itemEnum(bContext C, PointerRNA ptr, int[] free)
//	static EnumPropertyItem *rna_RenderSettings_engine_itemf(bContext *C, PointerRNA *ptr, int *free)
	{
//		RenderEngineType *type;
		EnumPropertyItem[] item= null;
//		EnumPropertyItem tmp = {0, "", 0, "", ""};
//		int a=0, totitem= 0;
//
//		for(type=R_engines.first; type; type=type->next, a++) {
//			tmp.value= a;
//			tmp.identifier= type->idname;
//			tmp.name= type->name;
//			RNA_enum_item_add(&item, &totitem, &tmp);
//		}
//		
//		RNA_enum_item_end(&item, &totitem);
//		*free= 1;

		return item;
	}};

	public static PropEnumGetFunc rna_RenderSettings_engine_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
//	static int rna_RenderSettings_engine_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		RenderEngineType *type;
//		int a= 0;
//
//		for(type=R_engines.first; type; type=type->next, a++)
//			if(strcmp(type->idname, rd->engine) == 0)
//				return a;
		
		return 0;
	}};

//	static void rna_Scene_glsl_update(Main *bmain, Scene *unused, PointerRNA *ptr)
//	{
//		Scene *scene= (Scene*)ptr->id.data;
//
//		DAG_id_tag_update(&scene->id, 0);
//	}

	public static UpdateFunc rna_RenderSettings_color_management_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_RenderSettings_color_management_update(Main *bmain, Scene *unused, PointerRNA *ptr)
	{
//		/* reset image nodes */
//		Scene *scene= (Scene*)ptr->id.data;
//		bNodeTree *ntree=scene->nodetree;
//		bNode *node;
//		
//		if(ntree && scene->use_nodes) {
//			for (node=ntree->nodes.first; node; node=node->next) {
//				if (ELEM(node->type, CMP_NODE_VIEWER, CMP_NODE_IMAGE)) {
//					ED_node_changed_update(&scene->id, node);
//					WM_main_add_notifier(NC_NODE|NA_EDITED, node);
//					
//					if (node->type == CMP_NODE_IMAGE)
//						BKE_image_signal((Image *)node->id, NULL, IMA_SIGNAL_RELOAD);
//				}
//			}
//		}
//
//		rna_Scene_glsl_update(bmain, unused, ptr);
	}};

	public static PropStringSetFunc rna_SceneRenderLayer_name_set = new PropStringSetFunc() {
	public void setStr(PointerRNA ptr, byte[] str, int offset)
	{
//		Scene *scene= (Scene*)ptr->id.data;
//		SceneRenderLayer *rl= (SceneRenderLayer*)ptr->data;
//
//		BLI_strncpy(rl->name, value, sizeof(rl->name));
//
//		if(scene->nodetree) {
//			bNode *node;
//			int index= BLI_findindex(&scene->r.layers, rl);
//
//			for(node= scene->nodetree->nodes.first; node; node= node->next) {
//				if(node->type==CMP_NODE_R_LAYERS && node->id==NULL) {
//					if(node->custom1==index)
//						BLI_strncpy(node->name, rl->name, NODE_MAXSTR);
//				}
//			}
//		}
	}};

	public static PropBooleanGetFunc rna_RenderSettings_multiple_engines_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//	static int rna_RenderSettings_multiple_engines_get(PointerRNA *ptr)
	{
//		return (BLI_countlist(&R_engines) > 1);
		return false;
	}};

	public static PropBooleanGetFunc rna_RenderSettings_use_game_engine_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//	static int rna_RenderSettings_use_game_engine_get(PointerRNA *ptr)
	{
//		RenderData *rd= (RenderData*)ptr->data;
//		RenderEngineType *type;
//
//		for(type=R_engines.first; type; type=type->next)
//			if(strcmp(type->idname, rd->engine) == 0)
//				return (type->flag & RE_GAME);
		
		return false;
	}};

	public static PropBooleanArraySetFunc rna_SceneRenderLayer_layer_set = new PropBooleanArraySetFunc() {
	public void setBoolArray(PointerRNA ptr, boolean[] values)
	{
//		SceneRenderLayer *rl= (SceneRenderLayer*)ptr->data;
//		rl->lay= ED_view3d_scene_layer_set(rl->lay, values, NULL);
	}};

	public static UpdateFunc rna_SceneRenderLayer_pass_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
	{
//		Scene *scene= (Scene*)ptr->id.data;
//
//		if(scene->nodetree)
//			ntreeCompositForceHidden(scene->nodetree, scene);
	}};

//	static void rna_Scene_use_nodes_set(PointerRNA *ptr, int value)
//	{
//		Scene *scene= (Scene*)ptr->data;
//
//		scene->use_nodes= value;
//		if(scene->use_nodes && scene->nodetree==NULL)
//			ED_node_composit_default(scene);
//	}

	public static UpdateFunc rna_Physics_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Physics_update(Main *bmain, Scene *unused, PointerRNA *ptr)
	{
		Scene scene= (Scene)ptr.id.data;
		Base base;

//		for(base = scene.base.first; base!=null; base=base.next)
//			BKE_ptcache_object_reset(scene, base.object, PTCACHE_RESET_DEPSGRAPH);
	}};

	public static PropBooleanArraySetFunc rna_Scene_editmesh_select_mode_set = new PropBooleanArraySetFunc() {
	public void setBoolArray(PointerRNA ptr, boolean[] values)
	//static void rna_Scene_editmesh_select_mode_set(PointerRNA *ptr, const int *value)
	{
//		Scene *scene= (Scene*)ptr->id.data;
//		ToolSettings *ts = (ToolSettings*)ptr->data;
//		int flag = (value[0] ? SCE_SELECT_VERTEX:0) | (value[1] ? SCE_SELECT_EDGE:0) | (value[2] ? SCE_SELECT_FACE:0);
//
//		if(flag) {
//			ts->selectmode = flag;
//
//			if(scene->basact) {
//				Mesh *me= get_mesh(scene->basact->object);
//				if(me && me->edit_mesh && me->edit_mesh->selectmode != flag) {
//					me->edit_mesh->selectmode= flag;
//					EM_selectmode_set(me->edit_mesh);
//				}
//			}
//		}
	}};

	public static UpdateFunc rna_Scene_editmesh_select_mode_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
	//static void rna_Scene_editmesh_select_mode_update(Main *bmain, Scene *scene, PointerRNA *ptr)
	{
//		Mesh *me= NULL;
//
//		if(scene->basact) {
//			me= get_mesh(scene->basact->object);
//			if(me && me->edit_mesh==NULL)
//				me= NULL;
//		}
//
//		WM_main_add_notifier(NC_GEOM|ND_SELECT, me);
//		WM_main_add_notifier(NC_SCENE|ND_TOOLSETTINGS, NULL);
	}};

//	static void object_simplify_update(Object *ob)
//	{
//		ModifierData *md;
//		ParticleSystem *psys;
//
//		for(md=ob->modifiers.first; md; md=md->next)
//			if(ELEM3(md->type, eModifierType_Subsurf, eModifierType_Multires, eModifierType_ParticleSystem))
//				ob->recalc |= OB_RECALC_DATA|PSYS_RECALC_CHILD;
//
//		for(psys=ob->particlesystem.first; psys; psys=psys->next)
//			psys->recalc |= PSYS_RECALC_CHILD;
//		
//		if(ob->dup_group) {
//			GroupObject *gob;
//
//			for(gob=ob->dup_group->gobject.first; gob; gob=gob->next)
//				object_simplify_update(gob->ob);
//		}
//	}

	public static UpdateFunc rna_Scene_use_simplify_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Scene_use_simplify_update(Main *bmain, Scene *scene, PointerRNA *ptr)
	{
//		Scene *sce_iter;
//		Base *base;
//
//		for(SETLOOPER(scene, sce_iter, base))
//			object_simplify_update(base->object);
//		
//		DAG_ids_flush_update(bmain, 0);
//		WM_main_add_notifier(NC_GEOM|ND_DATA, NULL);
	}};

	public static UpdateFunc rna_Scene_simplify_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
//	static void rna_Scene_simplify_update(Main *bmain, Scene *scene, PointerRNA *ptr)
	{
//		if(scene->r.mode & R_SIMPLIFY)
//			rna_Scene_use_simplify_update(bmain, scene, ptr);
	}};

//	static int rna_Scene_sync_mode_get(PointerRNA *ptr)
//	{
//		Scene *scene= (Scene*)ptr->data;
//		if(scene->audio.flag & AUDIO_SYNC)
//			return AUDIO_SYNC;
//		return scene->flag & SCE_FRAME_DROP;
//	}
//
//	static void rna_Scene_sync_mode_set(PointerRNA *ptr, int value)
//	{
//		Scene *scene= (Scene*)ptr->data;
//
//		if(value == AUDIO_SYNC)
//			scene->audio.flag |= AUDIO_SYNC;
//		else if(value == SCE_FRAME_DROP)
//		{
//			scene->audio.flag &= ~AUDIO_SYNC;
//			scene->flag |= SCE_FRAME_DROP;
//		}
//		else
//		{
//			scene->audio.flag &= ~AUDIO_SYNC;
//			scene->flag &= ~SCE_FRAME_DROP;
//		}
//	}
//
//	static int rna_GameSettings_auto_start_get(PointerRNA *ptr)
//	{
//		if (G.fileflags & G_FILE_AUTOPLAY)
//			return 1;
//
//		return 0;
//	}
//
//	static void rna_GameSettings_auto_start_set(PointerRNA *ptr, int value)
//	{
//		if(value)
//			G.fileflags |= G_FILE_AUTOPLAY;
//		else
//			G.fileflags &= ~G_FILE_AUTOPLAY;
//	}
//
//
//	static TimeMarker *rna_TimeLine_add(Scene *scene, const char name[])
//	{
//		TimeMarker *marker = MEM_callocN(sizeof(TimeMarker), "TimeMarker");
//		marker->flag= SELECT;
//		marker->frame= 1;
//		BLI_strncpy(marker->name, name, sizeof(marker->name));
//		BLI_addtail(&scene->markers, marker);
//		return marker;
//	}
//
//	static void rna_TimeLine_remove(Scene *scene, ReportList *reports, TimeMarker *marker)
//	{
//		/* try to remove the F-Curve from the action */
//		if (!BLI_remlink_safe(&scene->markers, marker)) {
//			BKE_reportf(reports, RPT_ERROR, "TimelineMarker '%s' not found in action '%s'", marker->name, scene->id.name+2);
//			return;
//		}
//
//		/* XXX, invalidates PyObject */
//		MEM_freeN(marker);
//	}
//
//	static KeyingSet *rna_Scene_keying_set_new(Scene *sce, ReportList *reports, const char name[])
//	{
//		KeyingSet *ks= NULL;
//
//		/* call the API func, and set the active keyingset index */
//		ks= BKE_keyingset_add(&sce->keyingsets, name, KEYINGSET_ABSOLUTE, 0);
//		
//		if (ks) {
//			sce->active_keyingset= BLI_countlist(&sce->keyingsets);
//			return ks;
//		}
//		else {
//			BKE_report(reports, RPT_ERROR, "Keying Set could not be added.");
//			return NULL;
//		}
//	}
//
//	#else

	static void rna_def_transform_orientation(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;

		final int matrix_dimsize[]= {3, 3};
		
		srna= RNA_def_struct(brna, "TransformOrientation", null);
		
		prop= RNA_def_property(srna, "matrix", PROP_FLOAT, PROP_MATRIX);
		RNA_def_property_float_sdna(prop, null, "mat");
		RNA_def_property_multi_array(prop, 2, matrix_dimsize);
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);
		
		prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
		RNA_def_property_string_sdna(prop, null, "name");
		RNA_def_struct_name_property(srna, prop);
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);
	}

	public static void rna_def_tool_settings(BlenderRNA  brna)
	{
		StructRNA srna;
		PropertyRNA prop;

		EnumPropertyItem uv_select_mode_items[] = {
			new EnumPropertyItem(UV_SELECT_VERTEX, "VERTEX", BIFIconID.ICON_UV_VERTEXSEL.ordinal(), "Vertex", "Vertex selection mode"),
			new EnumPropertyItem(UV_SELECT_EDGE, "EDGE", BIFIconID.ICON_UV_EDGESEL.ordinal(), "Edge", "Edge selection mode"),
			new EnumPropertyItem(UV_SELECT_FACE, "FACE", BIFIconID.ICON_UV_FACESEL.ordinal(), "Face", "Face selection mode"),
			new EnumPropertyItem(UV_SELECT_ISLAND, "ISLAND", BIFIconID.ICON_UV_ISLANDSEL.ordinal(), "Island", "Island selection mode"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem auto_key_items[] = {
			new EnumPropertyItem(AUTOKEY_MODE_NORMAL, "ADD_REPLACE_KEYS", 0, "Add & Replace", ""),
			new EnumPropertyItem(AUTOKEY_MODE_EDITKEYS, "REPLACE_KEYS", 0, "Replace", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem retarget_roll_items[] = {
			new EnumPropertyItem(SK_RETARGET_ROLL_NONE, "NONE", 0, "None", "Don't adjust roll"),
			new EnumPropertyItem(SK_RETARGET_ROLL_VIEW, "VIEW", 0, "View", "Roll bones to face the view"),
			new EnumPropertyItem(SK_RETARGET_ROLL_JOINT, "JOINT", 0, "Joint", "Roll bone to original joint plane offset"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem sketch_convert_items[] = {
			new EnumPropertyItem(SK_CONVERT_CUT_FIXED, "FIXED", 0, "Fixed", "Subdivide stroke in fixed number of bones"),
			new EnumPropertyItem(SK_CONVERT_CUT_LENGTH, "LENGTH", 0, "Length", "Subdivide stroke in bones of specific length"),
			new EnumPropertyItem(SK_CONVERT_CUT_ADAPTATIVE, "ADAPTIVE", 0, "Adaptive", "Subdivide stroke adaptively, with more subdivision in curvier parts"),
			new EnumPropertyItem(SK_CONVERT_RETARGET, "RETARGET", 0, "Retarget", "Retarget template bone chain to stroke"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem edge_tag_items[] = {
			new EnumPropertyItem(EDGE_MODE_SELECT, "SELECT", 0, "Select", ""),
			new EnumPropertyItem(EDGE_MODE_TAG_SEAM, "SEAM", 0, "Tag Seam", ""),
			new EnumPropertyItem(EDGE_MODE_TAG_SHARP, "SHARP", 0, "Tag Sharp", ""),
			new EnumPropertyItem(EDGE_MODE_TAG_CREASE, "CREASE", 0, "Tag Crease", ""),
			new EnumPropertyItem(EDGE_MODE_TAG_BEVEL, "BEVEL", 0, "Tag Bevel", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		srna= RNA_def_struct(brna, "ToolSettings", null);
		RNA_def_struct_ui_text(srna, "Tool Settings", "");
		
		prop= RNA_def_property(srna, "sculpt", PROP_POINTER, PROP_NONE);
		RNA_def_property_struct_type(prop, "Sculpt");
		RNA_def_property_ui_text(prop, "Sculpt", "");
		
		prop = RNA_def_property(srna, "use_auto_normalize", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "auto_normalize", 1);
		RNA_def_property_ui_text(prop, "WPaint Auto-Normalize", 
			"Ensure all bone-deforming vertex groups add up to 1.0 while "
			 +"weight painting");

		prop= RNA_def_property(srna, "vertex_paint", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "vpaint");
		RNA_def_property_ui_text(prop, "Vertex Paint", "");

		prop= RNA_def_property(srna, "weight_paint", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "wpaint");
		RNA_def_property_ui_text(prop, "Weight Paint", "");

		prop= RNA_def_property(srna, "image_paint", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "imapaint");
		RNA_def_property_ui_text(prop, "Image Paint", "");

		prop= RNA_def_property(srna, "particle_edit", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "particle");
		RNA_def_property_ui_text(prop, "Particle Edit", "");

		/* Transform */
		prop= RNA_def_property(srna, "proportional_edit", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "proportional");
		RNA_def_property_enum_items(prop, proportional_editing_items);
		RNA_def_property_ui_text(prop, "Proportional Editing", "Proportional editing mode");
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "use_proportional_edit_objects", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "proportional_objects", 0);
		RNA_def_property_ui_text(prop, "Proportional Editing Objects", "Proportional editing object mode");
		RNA_def_property_ui_icon(prop, ICON_PROP_OFF, 1);
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "proportional_edit_falloff", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "prop_mode");
		RNA_def_property_enum_items(prop, proportional_falloff_items);
		RNA_def_property_ui_text(prop, "Proportional Editing Falloff", "Falloff type for proportional editing mode");
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "normal_size", PROP_FLOAT, PROP_DISTANCE);
		RNA_def_property_float_sdna(prop, null, "normalsize");
		RNA_def_property_ui_text(prop, "Normal Size", "Display size for normals in the 3D view");
		RNA_def_property_range(prop, 0.00001, 1000.0);
		RNA_def_property_ui_range(prop, 0.01, 10.0, 10.0, 2);
		RNA_def_property_update(prop, NC_GEOM|ND_DATA, null);

		prop= RNA_def_property(srna, "use_mesh_automerge", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "automerge", 0);
		RNA_def_property_ui_text(prop, "AutoMerge Editing", "Automatically merge vertices moved to the same location");

		prop= RNA_def_property(srna, "use_snap", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "snap_flag", SCE_SNAP);
		RNA_def_property_ui_text(prop, "Snap", "Snap during transform");
		RNA_def_property_ui_icon(prop, ICON_SNAP_OFF, 1);
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "use_snap_align_rotation", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "snap_flag", SCE_SNAP_ROTATE);
		RNA_def_property_ui_text(prop, "Snap Align Rotation", "Align rotation with the snapping target");
		RNA_def_property_ui_icon(prop, ICON_SNAP_NORMAL, 0);
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "snap_element", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "snap_mode");
		RNA_def_property_enum_items(prop, snap_element_items);
		RNA_def_property_ui_text(prop, "Snap Element", "Type of element to snap to");
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "snap_target", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "snap_target");
		RNA_def_property_enum_items(prop, snap_target_items);
		RNA_def_property_ui_text(prop, "Snap Target", "Which part to snap onto the target");
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		prop= RNA_def_property(srna, "use_snap_peel_object", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "snap_flag", SCE_SNAP_PEEL_OBJECT);
		RNA_def_property_ui_text(prop, "Snap Peel Object", "Consider objects as whole when finding volume center");
		RNA_def_property_ui_icon(prop, ICON_SNAP_PEEL_OBJECT, 0);
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */
		
		prop= RNA_def_property(srna, "use_snap_project", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "snap_flag", SCE_SNAP_PROJECT);
		RNA_def_property_ui_text(prop, "Project Individual Elements", "Project individual elements on the surface of other objects");
		RNA_def_property_ui_icon(prop, ICON_RETOPO, 0);
		RNA_def_property_update(prop, NC_SCENE|ND_TOOLSETTINGS, null); /* header redraw */

		/* Auto Keying */
		prop= RNA_def_property(srna, "use_keyframe_insert_auto", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "autokey_mode", AUTOKEY_ON);
		RNA_def_property_ui_text(prop, "Auto Keying", "Automatic keyframe insertion for Objects and Bones");
		RNA_def_property_ui_icon(prop, ICON_REC, 0);
		
		prop= RNA_def_property(srna, "auto_keying_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "autokey_mode");
		RNA_def_property_enum_items(prop, auto_key_items);
		RNA_def_property_ui_text(prop, "Auto-Keying Mode", "Mode of automatic keyframe insertion for Objects and Bones");
		
		prop= RNA_def_property(srna, "use_record_with_nla", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "autokey_flag", ANIMRECORD_FLAG_WITHNLA);
		RNA_def_property_ui_text(prop, "Layered", "Add a new NLA Track + Strip for every loop/pass made over the animation to allow non-destructive tweaking");

		/* UV */
		prop= RNA_def_property(srna, "uv_select_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "uv_selectmode");
		RNA_def_property_enum_items(prop, uv_select_mode_items);
		RNA_def_property_ui_text(prop, "UV Selection Mode", "UV selection and display mode");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_IMAGE, null);

		prop= RNA_def_property(srna, "use_uv_select_sync", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "uv_flag", UV_SYNC_SELECTION);
		RNA_def_property_ui_text(prop, "UV Sync Selection", "Keep UV and edit mode mesh selection in sync");
		RNA_def_property_ui_icon(prop, ICON_EDIT, 0);
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_IMAGE, null);

		prop= RNA_def_property(srna, "show_uv_local_view", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "uv_flag", UV_SHOW_SAME_IMAGE);
		RNA_def_property_ui_text(prop, "UV Local View", "Draw only faces with the currently displayed image assigned");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_IMAGE, null);

		/* Mesh */
		prop= RNA_def_property(srna, "mesh_select_mode", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "selectmode", 1);
		RNA_def_property_array(prop, 3);
		RNA_def_property_boolean_funcs(prop, null, "rna_Scene_editmesh_select_mode_set");
		RNA_def_property_ui_text(prop, "Mesh Selection Mode", "Which mesh elements selection works on");
		RNA_def_property_update(prop, 0, "rna_Scene_editmesh_select_mode_update");

		prop= RNA_def_property(srna, "vertex_group_weight", PROP_FLOAT, PROP_FACTOR);
		RNA_def_property_float_sdna(prop, null, "vgroup_weight");
		RNA_def_property_ui_text(prop, "Vertex Group Weight", "Weight to assign in vertex groups");

		/* use with MESH_OT_select_shortest_path */
		prop= RNA_def_property(srna, "edge_path_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "edge_mode");
		RNA_def_property_enum_items(prop, edge_tag_items);
		RNA_def_property_ui_text(prop, "Edge Tag Mode", "The edge flag to tag when selecting the shortest path");

		/* etch-a-ton */
		prop= RNA_def_property(srna, "use_bone_sketching", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bone_sketching", BONE_SKETCHING);
		RNA_def_property_ui_text(prop, "Use Bone Sketching", "DOC BROKEN");
//		RNA_def_property_ui_icon(prop, ICON_EDIT, 0);

		prop= RNA_def_property(srna, "use_etch_quick", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bone_sketching", BONE_SKETCHING_QUICK);
		RNA_def_property_ui_text(prop, "Quick Sketching", "DOC BROKEN");

		prop= RNA_def_property(srna, "use_etch_overdraw", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bone_sketching", BONE_SKETCHING_ADJUST);
		RNA_def_property_ui_text(prop, "Overdraw Sketching", "DOC BROKEN");
		
		prop= RNA_def_property(srna, "use_etch_autoname", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "skgen_retarget_options", SK_RETARGET_AUTONAME);
		RNA_def_property_ui_text(prop, "Autoname", "DOC BROKEN");

		prop= RNA_def_property(srna, "etch_number", PROP_STRING, PROP_NONE);
		RNA_def_property_string_sdna(prop, null, "skgen_num_string");
		RNA_def_property_ui_text(prop, "Number", "DOC BROKEN");

		prop= RNA_def_property(srna, "etch_side", PROP_STRING, PROP_NONE);
		RNA_def_property_string_sdna(prop, null, "skgen_num_string");
		RNA_def_property_ui_text(prop, "Side", "DOC BROKEN");

		prop= RNA_def_property(srna, "etch_template", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "skgen_template");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Object");
		RNA_def_property_pointer_funcs(prop, null, "rna_Scene_skgen_etch_template_set", null, null);
		RNA_def_property_ui_text(prop, "Template", "Template armature that will be retargeted to the stroke");

		prop= RNA_def_property(srna, "etch_subdivision_number", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "skgen_subdivision_number");
		RNA_def_property_range(prop, 1, 255);
		RNA_def_property_ui_text(prop, "Subdivisions", "Number of bones in the subdivided stroke");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);

		prop= RNA_def_property(srna, "etch_adaptive_limit", PROP_FLOAT, PROP_FACTOR);
		RNA_def_property_float_sdna(prop, null, "skgen_correlation_limit");
		RNA_def_property_range(prop, 0.00001, 1.0);
		RNA_def_property_ui_range(prop, 0.01, 1.0, 0.01, 2);
		RNA_def_property_ui_text(prop, "Limit", "Number of bones in the subdivided stroke");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);

		prop= RNA_def_property(srna, "etch_length_limit", PROP_FLOAT, PROP_DISTANCE);
		RNA_def_property_float_sdna(prop, null, "skgen_length_limit");
		RNA_def_property_range(prop, 0.00001, 100000.0);
		RNA_def_property_ui_range(prop, 0.001, 100.0, 0.1, 3);
		RNA_def_property_ui_text(prop, "Length", "Number of bones in the subdivided stroke");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);

		prop= RNA_def_property(srna, "etch_roll_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "skgen_retarget_roll");
		RNA_def_property_enum_items(prop, retarget_roll_items);
		RNA_def_property_ui_text(prop, "Retarget roll mode", "Method used to adjust the roll of bones when retargeting");
		
		prop= RNA_def_property(srna, "etch_convert_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "bone_sketching_convert");
		RNA_def_property_enum_items(prop, sketch_convert_items);
		RNA_def_property_ui_text(prop, "Stroke conversion method", "Method used to convert stroke to bones");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_VIEW3D, null);

		/* Sculpt/Paint Unified Size and Strength */

		prop= RNA_def_property(srna, "sculpt_paint_use_unified_size", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "sculpt_paint_settings", SCULPT_PAINT_USE_UNIFIED_SIZE);
		RNA_def_property_ui_text(prop, "Sculpt/Paint Use Unified Radius", "Instead of per brush radius, the radius is shared across brushes");

		prop= RNA_def_property(srna, "sculpt_paint_use_unified_strength", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "sculpt_paint_settings", SCULPT_PAINT_USE_UNIFIED_ALPHA);
		RNA_def_property_ui_text(prop, "Sculpt/Paint Use Unified Strength", "Instead of per brush strength, the strength is shared across brushes");
	}


	static void rna_def_unit_settings(BlenderRNA  brna)
	{
		StructRNA srna;
		PropertyRNA prop;

		EnumPropertyItem unit_systems[] = {
			new EnumPropertyItem(USER_UNIT_NONE, "NONE", 0, "None", ""),
			new EnumPropertyItem(USER_UNIT_METRIC, "METRIC", 0, "Metric", ""),
			new EnumPropertyItem(USER_UNIT_IMPERIAL, "IMPERIAL", 0, "Imperial", ""),
			new EnumPropertyItem(0, null, 0, null, null)};
		
		EnumPropertyItem rotation_units[] = {
			new EnumPropertyItem(0, "DEGREES", 0, "Degrees", ""),
			new EnumPropertyItem(USER_UNIT_ROT_RADIANS, "RADIANS", 0, "Radians", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		srna= RNA_def_struct(brna, "UnitSettings", null);
		RNA_def_struct_ui_text(srna, "Unit Settings", "");

		/* Units */
		prop= RNA_def_property(srna, "system", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_items(prop, unit_systems);
		RNA_def_property_ui_text(prop, "Unit System", "The unit system to use for button display");
		RNA_def_property_update(prop, NC_WINDOW, null);
		
		prop= RNA_def_property(srna, "system_rotation", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_items(prop, rotation_units);
		RNA_def_property_ui_text(prop, "Rotation Units", "Unit to use for displaying/editing rotation values");
		RNA_def_property_update(prop, NC_WINDOW, null);

		prop= RNA_def_property(srna, "scale_length", PROP_FLOAT, PROP_UNSIGNED);
		RNA_def_property_ui_text(prop, "Unit Scale", "Scale to use when converting between blender units and dimensions");
		RNA_def_property_range(prop, 0.00001, 100000.0);
		RNA_def_property_ui_range(prop, 0.001, 100.0, 0.1, 3);
		RNA_def_property_update(prop, NC_WINDOW, null);

		prop= RNA_def_property(srna, "use_separate", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", USER_UNIT_OPT_SPLIT);
		RNA_def_property_ui_text(prop, "Separate Units", "Display units in pairs");
		RNA_def_property_update(prop, NC_WINDOW, null);
	}

	static void rna_def_render_layer_common(StructRNA srna, int scene)
	{
		PropertyRNA prop;

		prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
		if(scene!=0) RNA_def_property_string_funcs(prop, null, null, "rna_SceneRenderLayer_name_set");
		else RNA_def_property_string_sdna(prop, null, "name");
		RNA_def_property_ui_text(prop, "Name", "Render layer name");
		RNA_def_struct_name_property(srna, prop);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "material_override", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "mat_override");
		RNA_def_property_struct_type(prop, "Material");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Material Override", "Material to override all other materials in this render layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "light_override", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "light_override");
		RNA_def_property_struct_type(prop, "Group");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Light Override", "Group to override all other lights in this render layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		/* layers */
		prop= RNA_def_property(srna, "layers", PROP_BOOLEAN, PROP_LAYER_MEMBER);
		RNA_def_property_boolean_sdna(prop, null, "lay", 1);
		RNA_def_property_array(prop, 20);
		RNA_def_property_ui_text(prop, "Visible Layers", "Scene layers included in this render layer");
		if(scene!=0) RNA_def_property_boolean_funcs(prop, null, "rna_SceneRenderLayer_layer_set");
		else RNA_def_property_boolean_funcs(prop, null, "rna_RenderLayer_layer_set");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "layers_zmask", PROP_BOOLEAN, PROP_LAYER);
		RNA_def_property_boolean_sdna(prop, null, "lay_zmask", 1);
		RNA_def_property_array(prop, 20);
		RNA_def_property_ui_text(prop, "Zmask Layers", "Zmask scene layers");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		/* layer options */
		prop= RNA_def_property(srna, "use", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "layflag", SCE_LAY_DISABLE);
		RNA_def_property_ui_text(prop, "Enabled", "Disable or enable the render layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_zmask", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_ZMASK);
		RNA_def_property_ui_text(prop, "Zmask", "Only render what's in front of the solid z values");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "invert_zmask", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_NEG_ZMASK);
		RNA_def_property_ui_text(prop, "Zmask Negate", "For Zmask, only render what is behind solid z values instead of in front");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_all_z", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_ALL_Z);
		RNA_def_property_ui_text(prop, "All Z", "Fill in Z values for solid faces in invisible layers, for masking");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_solid", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_SOLID);
		RNA_def_property_ui_text(prop, "Solid", "Render Solid faces in this Layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_halo", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_HALO);
		RNA_def_property_ui_text(prop, "Halo", "Render Halos in this Layer (on top of Solid)");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_ztransp", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_ZTRA);
		RNA_def_property_ui_text(prop, "ZTransp", "Render Z-Transparent faces in this Layer (On top of Solid and Halos)");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_sky", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_SKY);
		RNA_def_property_ui_text(prop, "Sky", "Render Sky in this Layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_edge_enhance", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_EDGE);
		RNA_def_property_ui_text(prop, "Edge", "Render Edge-enhance in this Layer (only works for Solid faces)");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_strand", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "layflag", SCE_LAY_STRAND);
		RNA_def_property_ui_text(prop, "Strand", "Render Strands in this Layer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		/* passes */
		prop= RNA_def_property(srna, "use_pass_combined", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_COMBINED);
		RNA_def_property_ui_text(prop, "Combined", "Deliver full combined RGBA buffer");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_z", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_Z);
		RNA_def_property_ui_text(prop, "Z", "Deliver Z values pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		
		prop= RNA_def_property(srna, "use_pass_vector", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_VECTOR);
		RNA_def_property_ui_text(prop, "Vector", "Deliver speed vector pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_normal", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_NORMAL);
		RNA_def_property_ui_text(prop, "Normal", "Deliver normal pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_uv", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_UV);
		RNA_def_property_ui_text(prop, "UV", "Deliver texture UV pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_mist", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_MIST);
		RNA_def_property_ui_text(prop, "Mist", "Deliver mist factor pass (0.0-1.0)");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_object_index", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_INDEXOB);
		RNA_def_property_ui_text(prop, "Object Index", "Deliver object index pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_color", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_RGBA);
		RNA_def_property_ui_text(prop, "Color", "Deliver shade-less color pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_diffuse", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_DIFFUSE);
		RNA_def_property_ui_text(prop, "Diffuse", "Deliver diffuse pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_specular", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_SPEC);
		RNA_def_property_ui_text(prop, "Specular", "Deliver specular pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_shadow", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_SHADOW);
		RNA_def_property_ui_text(prop, "Shadow", "Deliver shadow pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_ambient_occlusion", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_AO);
		RNA_def_property_ui_text(prop, "AO", "Deliver AO pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		
		prop= RNA_def_property(srna, "use_pass_reflection", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_REFLECT);
		RNA_def_property_ui_text(prop, "Reflection", "Deliver raytraced reflection pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_refraction", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_REFRACT);
		RNA_def_property_ui_text(prop, "Refraction", "Deliver raytraced refraction pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_emit", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_EMIT);
		RNA_def_property_ui_text(prop, "Emit", "Deliver emission pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_environment", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_ENVIRONMENT);
		RNA_def_property_ui_text(prop, "Environment", "Deliver environment lighting pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "use_pass_indirect", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "passflag", SCE_PASS_INDIRECT);
		RNA_def_property_ui_text(prop, "Indirect", "Deliver indirect lighting pass");
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_specular", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_SPEC);
		RNA_def_property_ui_text(prop, "Specular Exclude", "Exclude specular pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_shadow", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_SHADOW);
		RNA_def_property_ui_text(prop, "Shadow Exclude", "Exclude shadow pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_ambient_occlusion", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_AO);
		RNA_def_property_ui_text(prop, "AO Exclude", "Exclude AO pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		
		prop= RNA_def_property(srna, "exclude_reflection", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_REFLECT);
		RNA_def_property_ui_text(prop, "Reflection Exclude", "Exclude raytraced reflection pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_refraction", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_REFRACT);
		RNA_def_property_ui_text(prop, "Refraction Exclude", "Exclude raytraced refraction pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_emit", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_EMIT);
		RNA_def_property_ui_text(prop, "Emit Exclude", "Exclude emission pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_environment", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_ENVIRONMENT);
		RNA_def_property_ui_text(prop, "Environment Exclude", "Exclude environment pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "exclude_indirect", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "pass_xor", SCE_PASS_INDIRECT);
		RNA_def_property_ui_text(prop, "Indirect Exclude", "Exclude indirect pass from combined");
		RNA_def_property_ui_icon(prop, ICON_RESTRICT_RENDER_OFF, 1);
		if(scene!=0) RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_SceneRenderLayer_pass_update");
		else RNA_def_property_clear_flag(prop, PROP_EDITABLE);
	}

	static void rna_def_scene_game_data(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;

		EnumPropertyItem framing_types_items[] ={
			new EnumPropertyItem(SCE_GAMEFRAMING_BARS, "LETTERBOX", 0, "Letterbox", "Show the entire viewport in the display window, using bar horizontally or vertically"),
			new EnumPropertyItem(SCE_GAMEFRAMING_EXTEND, "EXTEND", 0, "Extend", "Show the entire viewport in the display window, viewing more horizontally or vertically"),
			new EnumPropertyItem(SCE_GAMEFRAMING_SCALE, "SCALE", 0, "Scale", "Stretch or squeeze the viewport to fill the display window"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem dome_modes_items[] ={
			new EnumPropertyItem(DOME_FISHEYE, "FISHEYE", 0, "Fisheye", ""),
			new EnumPropertyItem(DOME_TRUNCATED_FRONT, "TRUNCATED_FRONT", 0, "Front-Truncated", ""),
			new EnumPropertyItem(DOME_TRUNCATED_REAR, "TRUNCATED_REAR", 0, "Rear-Truncated", ""),
			new EnumPropertyItem(DOME_ENVMAP, "ENVMAP", 0, "Cube Map", ""),
			new EnumPropertyItem(DOME_PANORAM_SPH, "PANORAM_SPH", 0, "Spherical Panoramic", ""),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		 EnumPropertyItem stereo_modes_items[] ={
			new EnumPropertyItem(STEREO_QUADBUFFERED, "QUADBUFFERED", 0, "Quad-Buffer", ""),
			new EnumPropertyItem(STEREO_ABOVEBELOW, "ABOVEBELOW", 0, "Above-Below", ""),
			new EnumPropertyItem(STEREO_INTERLACED, "INTERLACED", 0, "Interlaced", ""),
			new EnumPropertyItem(STEREO_ANAGLYPH, "ANAGLYPH", 0, "Anaglyph", ""),
			new EnumPropertyItem(STEREO_SIDEBYSIDE, "SIDEBYSIDE", 0, "Side-by-side", ""),
			new EnumPropertyItem(STEREO_VINTERLACE, "VINTERLACE", 0, "Vinterlace", ""),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		 EnumPropertyItem stereo_items[] ={
			new EnumPropertyItem(STEREO_NOSTEREO, "NONE", 0, "None", "Disable Stereo and Dome environments"),
			new EnumPropertyItem(STEREO_ENABLED, "STEREO", 0, "Stereo", "Enable Stereo environment"),
			new EnumPropertyItem(STEREO_DOME, "DOME", 0, "Dome", "Enable Dome environment"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem physics_engine_items[] = {
			new EnumPropertyItem(WOPHY_NONE, "NONE", 0, "None", ""),
			//{WOPHY_ENJI, "ENJI", 0, "Enji", ""),
			//{WOPHY_SUMO, "SUMO", 0, "Sumo (Deprecated)", ""),
			//{WOPHY_DYNAMO, "DYNAMO", 0, "Dynamo", ""),
			//{WOPHY_ODE, "ODE", 0, "ODE", ""),
			new EnumPropertyItem(WOPHY_BULLET, "BULLET", 0, "Bullet", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem material_items[] ={
			new EnumPropertyItem(GAME_MAT_TEXFACE, "TEXTURE_FACE", 0, "Texture Face", "Single texture face materials"),
			new EnumPropertyItem(GAME_MAT_MULTITEX, "MULTITEXTURE", 0, "Multitexture", "Multitexture materials"),
			new EnumPropertyItem(GAME_MAT_GLSL, "GLSL", 0, "GLSL", "OpenGL shading language shaders"),
			new EnumPropertyItem(0, null, 0, null, null)};

		srna= RNA_def_struct(brna, "SceneGameData", null);
		RNA_def_struct_sdna(srna, "GameData");
		RNA_def_struct_nested(brna, srna, "Scene");
		RNA_def_struct_ui_text(srna, "Game Data", "Game data for a Scene datablock");
		
		prop= RNA_def_property(srna, "resolution_x", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "xplay");
		RNA_def_property_range(prop, 4, 10000);
		RNA_def_property_ui_text(prop, "Resolution X", "Number of horizontal pixels in the screen");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "resolution_y", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "yplay");
		RNA_def_property_range(prop, 4, 10000);
		RNA_def_property_ui_text(prop, "Resolution Y", "Number of vertical pixels in the screen");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "depth", PROP_INT, PROP_UNSIGNED);
		RNA_def_property_int_sdna(prop, null, "depth");
		RNA_def_property_range(prop, 8, 32);
		RNA_def_property_ui_text(prop, "Bits", "Displays bit depth of full screen display");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		// Do we need it here ? (since we already have it in World
		prop= RNA_def_property(srna, "frequency", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "freqplay");
		RNA_def_property_range(prop, 4, 2000);
		RNA_def_property_ui_text(prop, "Freq", "Displays clock frequency of fullscreen display");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "show_fullscreen", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "fullscreen", 1);
		RNA_def_property_ui_text(prop, "Fullscreen", "Starts player in a new fullscreen display");
		RNA_def_property_update(prop, NC_SCENE, null);

		/* Framing */
		prop= RNA_def_property(srna, "frame_type", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "framing.type");
		RNA_def_property_enum_items(prop, framing_types_items);
		RNA_def_property_ui_text(prop, "Framing Types", "Select the type of Framing you want");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "frame_color", PROP_FLOAT, PROP_COLOR);
		RNA_def_property_float_sdna(prop, null, "framing.col");
		RNA_def_property_range(prop, 0.0f, 1.0f);
		RNA_def_property_array(prop, 3);
		RNA_def_property_ui_text(prop, "Framing Color", "Set color of the bars");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		/* Stereo */
		prop= RNA_def_property(srna, "stereo", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "stereoflag");
		RNA_def_property_enum_items(prop, stereo_items);
		RNA_def_property_ui_text(prop, "Stereo Options", "");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "stereo_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "stereomode");
		RNA_def_property_enum_items(prop, stereo_modes_items);
		RNA_def_property_ui_text(prop, "Stereo Mode", "Stereographic techniques");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "stereo_eye_separation", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "eyeseparation");
		RNA_def_property_range(prop, 0.01, 5.0);
		RNA_def_property_ui_text(prop, "Eye Separation", "Set the distance between the eyes - the camera focal length/30 should be fine");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		/* Dome */
		prop= RNA_def_property(srna, "dome_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "dome.mode");
		RNA_def_property_enum_items(prop, dome_modes_items);
		RNA_def_property_ui_text(prop, "Dome Mode", "Dome physical configurations");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "dome_tesselation", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "dome.res");
		RNA_def_property_ui_range(prop, 1, 8, 1, 1);
		RNA_def_property_ui_text(prop, "Tessellation", "Tessellation level - check the generated mesh in wireframe mode");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "dome_buffer_resolution", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "dome.resbuf");
		RNA_def_property_ui_range(prop, 0.1, 1.0, 0.1, 1);
		RNA_def_property_ui_text(prop, "Buffer Resolution", "Buffer Resolution - decrease it to increase speed");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "dome_angle", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "dome.angle");
		RNA_def_property_ui_range(prop, 90, 250, 1, 1);
		RNA_def_property_ui_text(prop, "Angle", "Field of View of the Dome - it only works in mode Fisheye and Truncated");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "dome_tilt", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "dome.tilt");
		RNA_def_property_ui_range(prop, -180, 180, 1, 1);
		RNA_def_property_ui_text(prop, "Tilt", "Camera rotation in horizontal axis");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		prop= RNA_def_property(srna, "dome_text", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "dome.warptext");
		RNA_def_property_struct_type(prop, "Text");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Warp Data", "Custom Warp Mesh data file");
		RNA_def_property_update(prop, NC_SCENE, null);
		
		/* physics */
		prop= RNA_def_property(srna, "physics_engine", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "physicsEngine");
		RNA_def_property_enum_items(prop, physics_engine_items);
		RNA_def_property_ui_text(prop, "Physics Engine", "Physics engine used for physics simulation in the game engine");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "physics_gravity", PROP_FLOAT, PROP_ACCELERATION);
		RNA_def_property_float_sdna(prop, null, "gravity");
		RNA_def_property_range(prop, 0.0, 25.0);
		RNA_def_property_ui_text(prop, "Physics Gravity", "Gravitational constant used for physics simulation in the game engine");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "occlusion_culling_resolution", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "occlusionRes");
		RNA_def_property_range(prop, 128.0, 1024.0);
		RNA_def_property_ui_text(prop, "Occlusion Resolution", "The size of the occlusion buffer in pixel, use higher value for better precision (slower)");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "fps", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "ticrate");
		RNA_def_property_ui_range(prop, 1, 60, 1, 1);
		RNA_def_property_range(prop, 1, 250);
		RNA_def_property_ui_text(prop, "Frames Per Second", "The nominal number of game frames per second. Physics fixed timestep = 1/fps, independently of actual frame rate");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "logic_step_max", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "maxlogicstep");
		RNA_def_property_ui_range(prop, 1, 5, 1, 1);
		RNA_def_property_range(prop, 1, 5);
		RNA_def_property_ui_text(prop, "Max Logic Steps", "Sets the maximum number of logic frame per game frame if graphics slows down the game, higher value allows better synchronization with physics");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "physics_step_max", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "maxphystep");
		RNA_def_property_ui_range(prop, 1, 5, 1, 1);
		RNA_def_property_range(prop, 1, 5);
		RNA_def_property_ui_text(prop, "Max Physics Steps", "Sets the maximum number of physics step per game frame if graphics slows down the game, higher value allows physics to keep up with realtime");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "physics_step_sub", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "physubstep");
		RNA_def_property_ui_range(prop, 1, 5, 1, 1);
		RNA_def_property_range(prop, 1, 5);
		RNA_def_property_ui_text(prop, "Physics Sub Steps", "Sets the number of simulation substep per physic timestep, higher value give better physics precision");
		RNA_def_property_update(prop, NC_SCENE, null);

		/* mode */
		prop= RNA_def_property(srna, "use_occlusion_culling", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", (1 << 5)); //XXX mode hardcoded // WO_DBVT_CULLING
		RNA_def_property_ui_text(prop, "DBVT culling", "Use optimized Bullet DBVT tree for view frustum and occlusion culling");
		
		// not used // deprecated !!!!!!!!!!!!!
		prop= RNA_def_property(srna, "use_activity_culling", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", (1 << 3)); //XXX mode hardcoded
		RNA_def_property_ui_text(prop, "Activity Culling", "Activity culling is enabled");

		// not used // deprecated !!!!!!!!!!!!!
		prop= RNA_def_property(srna, "activity_culling_box_radius", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "activityBoxRadius");
		RNA_def_property_range(prop, 0.0, 1000.0);
		RNA_def_property_ui_text(prop, "box radius", "Radius of the activity bubble, in Manhattan length. Objects outside the box are activity-culled");

		/* booleans */
		prop= RNA_def_property(srna, "show_debug_properties", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", GAME_SHOW_DEBUG_PROPS);
		RNA_def_property_ui_text(prop, "Show Debug Properties", "Show properties marked for debugging while the game runs");

		prop= RNA_def_property(srna, "show_framerate_profile", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", GAME_SHOW_FRAMERATE);
		RNA_def_property_ui_text(prop, "Show Framerate and Profile", "Show framerate and profiling information while the game runs");

		prop= RNA_def_property(srna, "show_physics_visualization", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", GAME_SHOW_PHYSICS);
		RNA_def_property_ui_text(prop, "Show Physics Visualization", "Show a visualization of physics bounds and interactions");

		prop= RNA_def_property(srna, "use_frame_rate", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_ENABLE_ALL_FRAMES);
		RNA_def_property_ui_text(prop, "Use Frame Rate", "Respect the frame rate rather than rendering as many frames as possible");

		prop= RNA_def_property(srna, "use_display_lists", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", GAME_DISPLAY_LISTS);
		RNA_def_property_ui_text(prop, "Display Lists", "Use display lists to speed up rendering by keeping geometry on the GPU");

		prop= RNA_def_property(srna, "use_deprecation_warnings", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_IGNORE_DEPRECATION_WARNINGS);
		RNA_def_property_ui_text(prop, "Deprecation Warnings", "Print warnings when using deprecated features in the python API");

		prop= RNA_def_property(srna, "use_animation_record", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", GAME_ENABLE_ANIMATION_RECORD);
		RNA_def_property_ui_text(prop, "Record Animation", "Record animation to fcurves");

		prop= RNA_def_property(srna, "use_auto_start", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_funcs(prop, "rna_GameSettings_auto_start_get", "rna_GameSettings_auto_start_set");
		RNA_def_property_ui_text(prop, "Auto Start", "Automatically start game at load time");
		
		/* materials */
		prop= RNA_def_property(srna, "material_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "matmode");
		RNA_def_property_enum_items(prop, material_items);
		RNA_def_property_ui_text(prop, "Material Mode", "Material mode to use for rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, null);

		prop= RNA_def_property(srna, "use_glsl_lights", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_LIGHTS);
		RNA_def_property_ui_text(prop, "GLSL Lights", "Use lights for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");

		prop= RNA_def_property(srna, "use_glsl_shaders", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_SHADERS);
		RNA_def_property_ui_text(prop, "GLSL Shaders", "Use shaders for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");

		prop= RNA_def_property(srna, "use_glsl_shadows", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_SHADOWS);
		RNA_def_property_ui_text(prop, "GLSL Shadows", "Use shadows for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");

		prop= RNA_def_property(srna, "use_glsl_ramps", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_RAMPS);
		RNA_def_property_ui_text(prop, "GLSL Ramps", "Use ramps for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");

		prop= RNA_def_property(srna, "use_glsl_nodes", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_NODES);
		RNA_def_property_ui_text(prop, "GLSL Nodes", "Use nodes for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");

		prop= RNA_def_property(srna, "use_glsl_extra_textures", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", GAME_GLSL_NO_EXTRA_TEX);
		RNA_def_property_ui_text(prop, "GLSL Extra Textures", "Use extra textures like normal or specular maps for GLSL rendering");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_glsl_update");
	}

	static void rna_def_scene_render_layer(BlenderRNA brna)
	{
		StructRNA srna;

		srna= RNA_def_struct(brna, "SceneRenderLayer", null);
		RNA_def_struct_ui_text(srna, "Scene Render Layer", "Render layer");
		RNA_def_struct_ui_icon(srna, ICON_RENDERLAYERS);

		rna_def_render_layer_common(srna, 1);
	}

	/* curve.splines */
	static void rna_def_render_layers(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;
		PropertyRNA prop;

		// FunctionRNA *func;
		// PropertyRNA *parm; 

		RNA_def_property_srna(cprop, "RenderLayers");
		srna= RNA_def_struct(brna, "RenderLayers", null);
		RNA_def_struct_sdna(srna, "RenderData");
		RNA_def_struct_ui_text(srna, "Render Layers", "Collection of render layers");

		prop= RNA_def_property(srna, "active_index", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "actlay");
		RNA_def_property_int_funcs(prop, "rna_RenderSettings_active_layer_index_get", "rna_RenderSettings_active_layer_index_set", "rna_RenderSettings_active_layer_index_range");
		RNA_def_property_ui_text(prop, "Active Layer Index", "Active index in render layer array");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "active", PROP_POINTER, PROP_UNSIGNED);
		RNA_def_property_struct_type(prop, "SceneRenderLayer");
		RNA_def_property_pointer_funcs(prop, "rna_RenderSettings_active_layer_get", "rna_RenderSettings_active_layer_set", null, null);
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Active Render Layer", "Active Render Layer");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

	}

	static void rna_def_scene_render_data(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		
		EnumPropertyItem pixel_filter_items[] ={
			new EnumPropertyItem(R_FILTER_BOX, "BOX", 0, "Box", ""),
			new EnumPropertyItem(R_FILTER_TENT, "TENT", 0, "Tent", ""),
			new EnumPropertyItem(R_FILTER_QUAD, "QUADRATIC", 0, "Quadratic", ""),
			new EnumPropertyItem(R_FILTER_CUBIC, "CUBIC", 0, "Cubic", ""),
			new EnumPropertyItem(R_FILTER_CATROM, "CATMULLROM", 0, "Catmull-Rom", ""),
			new EnumPropertyItem(R_FILTER_GAUSS, "GAUSSIAN", 0, "Gaussian", ""),
			new EnumPropertyItem(R_FILTER_MITCH, "MITCHELL", 0, "Mitchell-Netravali", ""),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		EnumPropertyItem alpha_mode_items[] ={
			new EnumPropertyItem(R_ADDSKY, "SKY", 0, "Sky", "Transparent pixels are filled with sky color"),
			new EnumPropertyItem(R_ALPHAPREMUL, "PREMUL", 0, "Premultiplied", "Transparent RGB pixels are multiplied by the alpha channel"),
			new EnumPropertyItem(R_ALPHAKEY, "STRAIGHT", 0, "Straight Alpha", "Transparent RGB and alpha pixels are unmodified"),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		EnumPropertyItem color_mode_items[] ={
			new EnumPropertyItem(R_PLANESBW, "BW", 0, "BW", "Images are saved with BW (grayscale) data"),
			new EnumPropertyItem(R_PLANES24, "RGB", 0, "RGB", "Images are saved with RGB (color) data"),
			new EnumPropertyItem(R_PLANES32, "RGBA", 0, "RGBA", "Images are saved with RGB and Alpha data (if supported)"),
			new EnumPropertyItem(0, null, 0, null, null)};
		
		EnumPropertyItem display_mode_items[] ={
			new EnumPropertyItem(R_OUTPUT_SCREEN, "SCREEN", 0, "Full Screen", "Images are rendered in full Screen"),
			new EnumPropertyItem(R_OUTPUT_AREA, "AREA", 0, "Image Editor", "Images are rendered in Image Editor"),
			new EnumPropertyItem(R_OUTPUT_WINDOW, "WINDOW", 0, "New Window", "Images are rendered in new Window"),
			new EnumPropertyItem(0, null, 0, null, null)};
		
		/* Bake */
		EnumPropertyItem bake_mode_items[] ={
			new EnumPropertyItem(RE_BAKE_ALL, "FULL", 0, "Full Render", ""),
			new EnumPropertyItem(RE_BAKE_AO, "AO", 0, "Ambient Occlusion", ""),
			new EnumPropertyItem(RE_BAKE_SHADOW, "SHADOW", 0, "Shadow", ""),
			new EnumPropertyItem(RE_BAKE_NORMALS, "NORMALS", 0, "Normals", ""),
			new EnumPropertyItem(RE_BAKE_TEXTURE, "TEXTURE", 0, "Textures", ""),
			new EnumPropertyItem(RE_BAKE_DISPLACEMENT, "DISPLACEMENT", 0, "Displacement", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem bake_normal_space_items[] ={
			new EnumPropertyItem(R_BAKE_SPACE_CAMERA, "CAMERA", 0, "Camera", ""),
			new EnumPropertyItem(R_BAKE_SPACE_WORLD, "WORLD", 0, "World", ""),
			new EnumPropertyItem(R_BAKE_SPACE_OBJECT, "OBJECT", 0, "Object", ""),
			new EnumPropertyItem(R_BAKE_SPACE_TANGENT, "TANGENT", 0, "Tangent", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem bake_qyad_split_items[] ={
			new EnumPropertyItem(0, "AUTO", 0, "Automatic", "Split quads to give the least distortion while baking"),
			new EnumPropertyItem(1, "FIXED", 0, "Fixed", "Split quads predictably (0,1,2) (0,2,3)"),
			new EnumPropertyItem(2, "FIXED_ALT", 0, "Fixed Alternate", "Split quads predictably (1,2,3) (1,3,0)"),
			new EnumPropertyItem(0, null, 0, null, null)};
		
		EnumPropertyItem octree_resolution_items[] = {
			new EnumPropertyItem(64, "64", 0, "64", ""),
			new EnumPropertyItem(128, "128", 0, "128", ""),
			new EnumPropertyItem(256, "256", 0, "256", ""),
			new EnumPropertyItem(512, "512", 0, "512", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem raytrace_structure_items[] = {
			new EnumPropertyItem(R_RAYSTRUCTURE_AUTO, "AUTO", 0, "Auto", ""),
			new EnumPropertyItem(R_RAYSTRUCTURE_OCTREE, "OCTREE", 0, "Octree", "Use old Octree structure"),
			new EnumPropertyItem(R_RAYSTRUCTURE_BLIBVH, "BLIBVH", 0, "BLI BVH", "Use BLI K-Dop BVH.c"),
			new EnumPropertyItem(R_RAYSTRUCTURE_VBVH, "VBVH", 0, "vBVH", ""),
			new EnumPropertyItem(R_RAYSTRUCTURE_SIMD_SVBVH, "SIMD_SVBVH", 0, "SIMD SVBVH", ""),
			new EnumPropertyItem(R_RAYSTRUCTURE_SIMD_QBVH, "SIMD_QBVH", 0, "SIMD QBVH", ""),
			new EnumPropertyItem(0, null, 0, null, null)
			};

		EnumPropertyItem fixed_oversample_items[] = {
			new EnumPropertyItem(5, "5", 0, "5", ""),
			new EnumPropertyItem(8, "8", 0, "8", ""),
			new EnumPropertyItem(11, "11", 0, "11", ""),
			new EnumPropertyItem(16, "16", 0, "16", ""),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		EnumPropertyItem field_order_items[] = {
			new EnumPropertyItem(0, "EVEN_FIRST", 0, "Upper First", "Upper field first"),
			new EnumPropertyItem(R_ODDFIELD, "ODD_FIRST", 0, "Lower First", "Lower field first"),
			new EnumPropertyItem(0, null, 0, null, null)};
			
		EnumPropertyItem threads_mode_items[] = {
			new EnumPropertyItem(0, "AUTO", 0, "Auto-detect", "Automatically determine the number of threads, based on CPUs"),
			new EnumPropertyItem(R_FIXED_THREADS, "FIXED", 0, "Fixed", "Manually determine the number of threads"),
			new EnumPropertyItem(0, null, 0, null, null)};
			
//	#ifdef WITH_OPENEXR	
//		static EnumPropertyItem exr_codec_items[] = {
//			{0, "NONE", 0, "None", ""},
//			{1, "PXR24", 0, "Pxr24 (lossy)", ""},
//			{2, "ZIP", 0, "ZIP (lossless)", ""},
//			{3, "PIZ", 0, "PIZ (lossless)", ""},
//			{4, "RLE", 0, "RLE (lossless)", ""},
//			{0, null, 0, null, null}};
//	#endif

//	#ifdef WITH_OPENJPEG
//		static EnumPropertyItem jp2_preset_items[] = {
//			{0, "NO_PRESET", 0, "No Preset", ""},
//			{1, "CINE_24FPS", 0, "Cinema 24fps 2048x1080", ""},
//			{2, "CINE_48FPS", 0, "Cinema 48fps 2048x1080", ""},
//			{3, "CINE_24FPS_4K", 0, "Cinema 24fps 4096x2160", ""},
//			{4, "CINE_SCOPE_48FPS", 0, "Cine-Scope 24fps 2048x858", ""},
//			{5, "CINE_SCOPE_48FPS", 0, "Cine-Scope 48fps 2048x858", ""},
//			{6, "CINE_FLAT_24FPS", 0, "Cine-Flat 24fps 1998x1080", ""},
//			{7, "CINE_FLAT_48FPS", 0, "Cine-Flat 48fps 1998x1080", ""},
//			{0, null, 0, null, null}};
//			
//		static EnumPropertyItem jp2_depth_items[] = {
//			{8, "8", 0, "8", "8 bit color channels"},
//			{12, "12", 0, "12", "12 bit color channels"},
//			{16, "16", 0, "16", "16 bit color channels"},
//			{0, null, 0, null, null}};
//	#endif
		
//	#ifdef	WITH_QUICKTIME
//		static EnumPropertyItem quicktime_codec_type_items[] = {
//			{0, "codec", 0, "codec", ""},
//			{0, null, 0, null, null}};
//		
//	#ifdef USE_QTKIT
//		static EnumPropertyItem quicktime_audio_samplerate_items[] = {
//			{22050, "22050", 0, "22kHz", ""},
//			{44100, "44100", 0, "44.1kHz", ""},
//			{48000, "48000", 0, "48kHz", ""},
//			{88200, "88200", 0, "88.2kHz", ""},
//			{96000, "96000", 0, "96kHz", ""},
//			{192000, "192000", 0, "192kHz", ""},
//			{0, null, 0, null, null}};
//		
//		static EnumPropertyItem quicktime_audio_bitdepth_items[] = {
//			{AUD_FORMAT_U8, "8BIT", 0, "8bit", ""},
//			{AUD_FORMAT_S16, "16BIT", 0, "16bit", ""},
//			{AUD_FORMAT_S24, "24BIT", 0, "24bit", ""},
//			{AUD_FORMAT_S32, "32BIT", 0, "32bit", ""},
//			{AUD_FORMAT_FLOAT32, "FLOAT32", 0, "float32", ""},
//			{AUD_FORMAT_FLOAT64, "FLOAT64", 0, "float64", ""},
//			{0, null, 0, null, null}};
//		
//		static EnumPropertyItem quicktime_audio_bitrate_items[] = {
//			{64000, "64000", 0, "64kbps", ""},
//			{112000, "112000", 0, "112kpbs", ""},
//			{128000, "128000", 0, "128kbps", ""},
//			{192000, "192000", 0, "192kbps", ""},
//			{256000, "256000", 0, "256kbps", ""},
//			{320000, "320000", 0, "320kbps", ""},
//			{0, null, 0, null, null}};
//	#endif
//	#endif

//	#ifdef WITH_FFMPEG
		EnumPropertyItem ffmpeg_format_items[] = {
			new EnumPropertyItem(FFMPEG_MPEG1, "MPEG1", 0, "MPEG-1", ""),
			new EnumPropertyItem(FFMPEG_MPEG2, "MPEG2", 0, "MPEG-2", ""),
			new EnumPropertyItem(FFMPEG_MPEG4, "MPEG4", 0, "MPEG-4", ""),
			new EnumPropertyItem(FFMPEG_AVI, "AVI", 0, "AVI", ""),
			new EnumPropertyItem(FFMPEG_MOV, "QUICKTIME", 0, "Quicktime", ""),
			new EnumPropertyItem(FFMPEG_DV, "DV", 0, "DV", ""),
			new EnumPropertyItem(FFMPEG_H264, "H264", 0, "H.264", ""),
			new EnumPropertyItem(FFMPEG_XVID, "XVID", 0, "Xvid", ""),
			new EnumPropertyItem(FFMPEG_OGG, "OGG", 0, "Ogg", ""),
			new EnumPropertyItem(FFMPEG_MKV, "MKV", 0, "Matroska", ""),
			new EnumPropertyItem(FFMPEG_FLV, "FLASH", 0, "Flash", ""),
			new EnumPropertyItem(FFMPEG_WAV, "WAV", 0, "Wav", ""),
			new EnumPropertyItem(FFMPEG_MP3, "MP3", 0, "Mp3", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

//		static EnumPropertyItem ffmpeg_codec_items[] = {
//			{CODEC_ID_NONE, "NONE", 0, "None", ""},
//			{CODEC_ID_MPEG1VIDEO, "MPEG1", 0, "MPEG-1", ""},
//			{CODEC_ID_MPEG2VIDEO, "MPEG2", 0, "MPEG-2", ""},
//			{CODEC_ID_MPEG4, "MPEG4", 0, "MPEG-4(divx)", ""},
//			{CODEC_ID_HUFFYUV, "HUFFYUV", 0, "HuffYUV", ""},
//			{CODEC_ID_DVVIDEO, "DV", 0, "DV", ""},
//			{CODEC_ID_H264, "H264", 0, "H.264", ""},
//			{CODEC_ID_XVID, "XVID", 0, "Xvid", ""},
//			{CODEC_ID_THEORA, "THEORA", 0, "Theora", ""},
//			{CODEC_ID_FLV1, "FLASH", 0, "Flash Video", ""},
//			{CODEC_ID_FFV1, "FFV1", 0, "FFmpeg video codec #1", ""},
//			{0, null, 0, null, null}};
//
//		static EnumPropertyItem ffmpeg_audio_codec_items[] = {
//			{CODEC_ID_NONE, "NONE", 0, "None", ""},
//			{CODEC_ID_MP2, "MP2", 0, "MP2", ""},
//			{CODEC_ID_MP3, "MP3", 0, "MP3", ""},
//			{CODEC_ID_AC3, "AC3", 0, "AC3", ""},
//			{CODEC_ID_AAC, "AAC", 0, "AAC", ""},
//			{CODEC_ID_VORBIS, "VORBIS", 0, "Vorbis", ""},
//			{CODEC_ID_FLAC, "FLAC", 0, "FLAC", ""},
//			{CODEC_ID_PCM_S16LE, "PCM", 0, "PCM", ""},
//			{0, null, 0, null, null}};
//	#endif

		EnumPropertyItem engine_items[] = {
			new EnumPropertyItem(0, "BLENDER_RENDER", 0, "Blender Render", ""),
			new EnumPropertyItem(0, null, 0, null, null)};

		srna= RNA_def_struct(brna, "RenderSettings", null);
		RNA_def_struct_sdna(srna, "RenderData");
		RNA_def_struct_nested(brna, srna, "Scene");
		RNA_def_struct_path_func(srna, "rna_RenderSettings_path");
		RNA_def_struct_ui_text(srna, "Render Data", "Rendering settings for a Scene datablock");
		
		prop= RNA_def_property(srna, "color_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "planes");
		RNA_def_property_enum_items(prop, color_mode_items);
		RNA_def_property_ui_text(prop, "Color Mode", "Choose BW for saving greyscale images, RGB for saving red, green and blue channels, AND RGBA for saving red, green, blue + alpha channels");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "resolution_x", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "xsch");
		RNA_def_property_range(prop, 4, 10000);
		RNA_def_property_ui_text(prop, "Resolution X", "Number of horizontal pixels in the rendered image");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "resolution_y", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "ysch");
		RNA_def_property_range(prop, 4, 10000);
		RNA_def_property_ui_text(prop, "Resolution Y", "Number of vertical pixels in the rendered image");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "resolution_percentage", PROP_INT, PROP_PERCENTAGE);
		RNA_def_property_int_sdna(prop, null, "size");
		RNA_def_property_ui_range(prop, 1, 100, 10, 1);
		RNA_def_property_ui_text(prop, "Resolution %", "Percentage scale for render resolution");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "parts_x", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "xparts");
		RNA_def_property_range(prop, 1, 512);
		RNA_def_property_ui_text(prop, "Parts X", "Number of horizontal tiles to use while rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "parts_y", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "yparts");
		RNA_def_property_range(prop, 1, 512);
		RNA_def_property_ui_text(prop, "Parts Y", "Number of vertical tiles to use while rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "pixel_aspect_x", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "xasp");
		RNA_def_property_range(prop, 1.0f, 200.0f);
		RNA_def_property_ui_text(prop, "Pixel Aspect X", "Horizontal aspect ratio - for anamorphic or non-square pixel output");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "pixel_aspect_y", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "yasp");
		RNA_def_property_range(prop, 1.0f, 200.0f);
		RNA_def_property_ui_text(prop, "Pixel Aspect Y", "Vertical aspect ratio - for anamorphic or non-square pixel output");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* JPEG and AVI JPEG */
		
		prop= RNA_def_property(srna, "file_quality", PROP_INT, PROP_PERCENTAGE);
		RNA_def_property_int_sdna(prop, null, "quality");
		RNA_def_property_range(prop, 0, 100); /* 0 is needed for compression. */
		RNA_def_property_ui_text(prop, "Quality", "Quality of JPEG images, AVI Jpeg and SGI movies, Compression for PNG's");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* Tiff */
		
		prop= RNA_def_property(srna, "use_tiff_16bit", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_TIFF_16BIT);
		RNA_def_property_ui_text(prop, "16 Bit", "Save TIFF with 16 bits per channel");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* Cineon and DPX */
		
		prop= RNA_def_property(srna, "use_cineon_log", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_CINEON_LOG);
		RNA_def_property_ui_text(prop, "Log", "Convert to logarithmic color space");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "cineon_black", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "cineonblack");
		RNA_def_property_range(prop, 0, 1024);
		RNA_def_property_ui_text(prop, "B", "Log conversion reference blackpoint");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "cineon_white", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "cineonwhite");
		RNA_def_property_range(prop, 0, 1024);
		RNA_def_property_ui_text(prop, "W", "Log conversion reference whitepoint");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "cineon_gamma", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "cineongamma");
		RNA_def_property_range(prop, 0.0f, 10.0f);
		RNA_def_property_ui_text(prop, "G", "Log conversion gamma");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

//	#ifdef WITH_OPENEXR	
//		/* OpenEXR */
//
//		prop= RNA_def_property(srna, "exr_codec", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "quality");
//		RNA_def_property_enum_items(prop, exr_codec_items);
//		RNA_def_property_ui_text(prop, "Codec", "Codec settings for OpenEXR");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "use_exr_half", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_OPENEXR_HALF);
//		RNA_def_property_ui_text(prop, "Half", "Use 16 bit floats instead of 32 bit floats per channel");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "exr_zbuf", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_OPENEXR_ZBUF);
//		RNA_def_property_ui_text(prop, "Zbuf", "Save the z-depth per pixel (32 bit unsigned int zbuffer)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "exr_preview", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_PREVIEW_JPG);
//		RNA_def_property_ui_text(prop, "Preview", "When rendering animations, save JPG preview images in same directory");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//	#endif

//	#ifdef WITH_OPENJPEG
//		/* Jpeg 2000 */
//
//		prop= RNA_def_property(srna, "jpeg2k_preset", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_sdna(prop, null, "jp2_preset");
//		RNA_def_property_enum_items(prop, jp2_preset_items);
//		RNA_def_property_enum_funcs(prop, null, "rna_RenderSettings_jpeg2k_preset_set", null);
//		RNA_def_property_ui_text(prop, "Preset", "Use a DCI Standard preset for saving jpeg2000");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "jpeg2k_depth", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "jp2_depth");
//		RNA_def_property_enum_items(prop, jp2_depth_items);
//		RNA_def_property_enum_funcs(prop, null, "rna_RenderSettings_jpeg2k_depth_set", null);
//		RNA_def_property_ui_text(prop, "Depth", "Bit depth per channel");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "jpeg2k_ycc", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "subimtype", R_JPEG2K_YCC);
//		RNA_def_property_ui_text(prop, "YCC", "Save luminance-chrominance-chrominance channels instead of RGB colors");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//	#endif

//	#ifdef WITH_QUICKTIME
//		/* QuickTime */
//		
//		prop= RNA_def_property(srna, "quicktime_codec_type", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "qtcodecsettings.codecType");
//		RNA_def_property_enum_items(prop, quicktime_codec_type_items);
//		RNA_def_property_enum_funcs(prop, "rna_RenderSettings_qtcodecsettings_codecType_get",
//									"rna_RenderSettings_qtcodecsettings_codecType_set",
//									"rna_RenderSettings_qtcodecsettings_codecType_itemf");
//		RNA_def_property_ui_text(prop, "Codec", "QuickTime codec type");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "quicktime_codec_spatial_quality", PROP_INT, PROP_PERCENTAGE);
//		RNA_def_property_int_sdna(prop, null, "qtcodecsettings.codecSpatialQuality");
//		RNA_def_property_range(prop, 0, 100);
//		RNA_def_property_ui_text(prop, "Spatial quality", "Intra-frame spatial quality level");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//
//	#ifdef USE_QTKIT
//		prop= RNA_def_property(srna, "quicktime_audiocodec_type", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "qtcodecsettings.audiocodecType");
//		RNA_def_property_enum_items(prop, quicktime_codec_type_items);
//		RNA_def_property_enum_funcs(prop, "rna_RenderSettings_qtcodecsettings_audiocodecType_get",
//									"rna_RenderSettings_qtcodecsettings_audiocodecType_set",
//									"rna_RenderSettings_qtcodecsettings_audiocodecType_itemf");
//		RNA_def_property_ui_text(prop, "Audio Codec", "QuickTime audio codec type");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "quicktime_audio_samplerate", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "qtcodecsettings.audioSampleRate");
//		RNA_def_property_enum_items(prop, quicktime_audio_samplerate_items);
//		RNA_def_property_ui_text(prop, "Smp Rate", "Sample Rate");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "quicktime_audio_bitdepth", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "qtcodecsettings.audioBitDepth");
//		RNA_def_property_enum_items(prop, quicktime_audio_bitdepth_items);
//		RNA_def_property_ui_text(prop, "Bit Depth", "Bit Depth");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "quicktime_audio_resampling_hq", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_negative_sdna(prop, null, "qtcodecsettings.audioCodecFlags", QTAUDIO_FLAG_RESAMPLE_NOHQ);
//		RNA_def_property_ui_text(prop, "HQ", "Use High Quality resampling algorithm");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "quicktime_audio_codec_isvbr", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_negative_sdna(prop, null, "qtcodecsettings.audioCodecFlags", QTAUDIO_FLAG_CODEC_ISCBR);
//		RNA_def_property_ui_text(prop, "VBR", "Use Variable Bit Rate compression (improves quality at same bitrate)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//
//		prop= RNA_def_property(srna, "quicktime_audio_bitrate", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "qtcodecsettings.audioBitRate");
//		RNA_def_property_enum_items(prop, quicktime_audio_bitrate_items);
//		RNA_def_property_ui_text(prop, "Bitrate", "Compressed audio bitrate");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);	
//	#endif
//	#endif
		
//	#ifdef WITH_FFMPEG
		/* FFMPEG Video*/
		
		prop= RNA_def_property(srna, "ffmpeg_format", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "ffcodecdata.type");
		RNA_def_property_enum_items(prop, ffmpeg_format_items);
		RNA_def_property_ui_text(prop, "Format", "Output file format");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
//		prop= RNA_def_property(srna, "ffmpeg_codec", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "ffcodecdata.codec");
//		RNA_def_property_enum_items(prop, ffmpeg_codec_items);
//		RNA_def_property_ui_text(prop, "Codec", "FFMpeg codec to use");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_video_bitrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.video_bitrate");
//		RNA_def_property_range(prop, 1, 14000);
//		RNA_def_property_ui_text(prop, "Bitrate", "Video bitrate(kb/s)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_minrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.rc_min_rate");
//		RNA_def_property_range(prop, 0, 9000);
//		RNA_def_property_ui_text(prop, "Min Rate", "Rate control: min rate(kb/s)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_maxrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.rc_max_rate");
//		RNA_def_property_range(prop, 1, 14000);
//		RNA_def_property_ui_text(prop, "Max Rate", "Rate control: max rate(kb/s)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_muxrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.mux_rate");
//		RNA_def_property_range(prop, 0, 100000000);
//		RNA_def_property_ui_text(prop, "Mux Rate", "Mux rate (bits/s(!))");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_gopsize", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.gop_size");
//		RNA_def_property_range(prop, 0, 100);
//		RNA_def_property_ui_text(prop, "GOP Size", "Distance between key frames");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_buffersize", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.rc_buffer_size");
//		RNA_def_property_range(prop, 0, 2000);
//		RNA_def_property_ui_text(prop, "Buffersize", "Rate control: buffer size (kb)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_packetsize", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.mux_packet_size");
//		RNA_def_property_range(prop, 0, 16384);
//		RNA_def_property_ui_text(prop, "Mux Packet Size", "Mux packet size (byte)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_autosplit", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "ffcodecdata.flags", FFMPEG_AUTOSPLIT_OUTPUT);
//		RNA_def_property_ui_text(prop, "Autosplit Output", "Autosplit output at 2GB boundary");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		/* FFMPEG Audio*/
//		prop= RNA_def_property(srna, "ffmpeg_audio_codec", PROP_ENUM, PROP_NONE);
//		RNA_def_property_enum_bitflag_sdna(prop, null, "ffcodecdata.audio_codec");
//		RNA_def_property_enum_items(prop, ffmpeg_audio_codec_items);
//		RNA_def_property_ui_text(prop, "Audio Codec", "FFMpeg audio codec to use");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_audio_bitrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.audio_bitrate");
//		RNA_def_property_range(prop, 32, 384);
//		RNA_def_property_ui_text(prop, "Bitrate", "Audio bitrate(kb/s)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//		
//		prop= RNA_def_property(srna, "ffmpeg_audio_mixrate", PROP_INT, PROP_NONE);
//		RNA_def_property_int_sdna(prop, null, "ffcodecdata.audio_mixrate");
//		RNA_def_property_range(prop, 8000, 192000);
//		RNA_def_property_ui_text(prop, "Samplerate", "Audio samplerate(samples/s)");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//
//		prop= RNA_def_property(srna, "ffmpeg_audio_volume", PROP_FLOAT, PROP_NONE);
//		RNA_def_property_float_sdna(prop, null, "ffcodecdata.audio_volume");
//		RNA_def_property_range(prop, 0.0f, 1.0f);
//		RNA_def_property_ui_text(prop, "Volume", "Audio volume");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
//
//	#endif

		prop= RNA_def_property(srna, "fps", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "frs_sec");
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_range(prop, 1, 120);
		RNA_def_property_ui_text(prop, "FPS", "Framerate, expressed in frames per second");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "fps_base", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "frs_sec_base");
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_range(prop, 0.1f, 120.0f);
		RNA_def_property_ui_text(prop, "FPS Base", "Framerate base");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* frame mapping */
		prop= RNA_def_property(srna, "frame_map_old", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "framapto");
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_range(prop, 1, 900);
		RNA_def_property_ui_text(prop, "Frame Map Old", "Specify old mapping value in frames");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, "rna_Scene_framelen_update");
		
		prop= RNA_def_property(srna, "frame_map_new", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "images");
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_range(prop, 1, 900);
		RNA_def_property_ui_text(prop, "Frame Map New", "Specify how many frames the Map Old will last");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, "rna_Scene_framelen_update");

		
		prop= RNA_def_property(srna, "dither_intensity", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "dither_intensity");
		RNA_def_property_range(prop, 0.0f, 2.0f);
		RNA_def_property_ui_text(prop, "Dither Intensity", "Amount of dithering noise added to the rendered image to break up banding");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "pixel_filter_type", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "filtertype");
		RNA_def_property_enum_items(prop, pixel_filter_items);
		RNA_def_property_ui_text(prop, "Pixel Filter", "Reconstruction filter used for combining anti-aliasing samples");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "filter_size", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "gauss");
		RNA_def_property_range(prop, 0.5f, 1.5f);
		RNA_def_property_ui_text(prop, "Filter Size", "Pixel width over which the reconstruction filter combines samples");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "alpha_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "alphamode");
		RNA_def_property_enum_items(prop, alpha_mode_items);
		RNA_def_property_ui_text(prop, "Alpha Mode", "Representation of alpha information in the RGBA pixels");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "octree_resolution", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "ocres");
		RNA_def_property_enum_items(prop, octree_resolution_items);
		RNA_def_property_ui_text(prop, "Octree Resolution", "Resolution of raytrace accelerator. Use higher resolutions for larger scenes");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "raytrace_method", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "raytrace_structure");
		RNA_def_property_enum_items(prop, raytrace_structure_items);
		RNA_def_property_ui_text(prop, "Raytrace Acceleration Structure", "Type of raytrace accelerator structure");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_instances", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "raytrace_options", R_RAYTRACE_USE_INSTANCES);
		RNA_def_property_ui_text(prop, "Use Instances", "Instance support leads to effective memory reduction when using duplicates");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_local_coords", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "raytrace_options", R_RAYTRACE_USE_LOCAL_COORDS);
		RNA_def_property_ui_text(prop, "Use Local Coords", "Vertex coordinates are stored localy on each primitive. Increases memory usage, but may have impact on speed");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_antialiasing", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_OSA);
		RNA_def_property_ui_text(prop, "Anti-Aliasing", "Render and combine multiple samples per pixel to prevent jagged edges");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "antialiasing_samples", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "osa");
		RNA_def_property_enum_items(prop, fixed_oversample_items);
		RNA_def_property_ui_text(prop, "Anti-Aliasing Samples", "Amount of anti-aliasing samples per pixel");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_fields", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_FIELDS);
		RNA_def_property_ui_text(prop, "Fields", "Render image to two fields per frame, for interlaced TV output");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "field_order", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "mode");
		RNA_def_property_enum_items(prop, field_order_items);
		RNA_def_property_ui_text(prop, "Field Order", "Order of video fields. Select which lines get rendered first, to create smooth motion for TV output");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_fields_still", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_FIELDSTILL);
		RNA_def_property_ui_text(prop, "Fields Still", "Disable the time difference between fields");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* rendering features */
		prop= RNA_def_property(srna, "use_shadows", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_SHADOW);
		RNA_def_property_ui_text(prop, "Shadows", "Calculate shadows while rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_envmaps", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_ENVMAP);
		RNA_def_property_ui_text(prop, "Environment Maps", "Calculate environment maps while rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_radiosity", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_RADIO);
		RNA_def_property_ui_text(prop, "Radiosity", "Calculate radiosity in a pre-process before rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_sss", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_SSS);
		RNA_def_property_ui_text(prop, "Subsurface Scattering", "Calculate sub-surface scattering in materials rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_raytrace", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_RAYTRACE);
		RNA_def_property_ui_text(prop, "Raytracing", "Pre-calculate the raytrace accelerator and render raytracing effects");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_textures", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "scemode", R_NO_TEX);
		RNA_def_property_ui_text(prop, "Textures", "Use textures to affect material properties");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_edge_enhance", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_EDGE);
		RNA_def_property_ui_text(prop, "Edge", "Create a toon outline around the edges of geometry");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "edge_threshold", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "edgeint");
		RNA_def_property_range(prop, 0, 255);
		RNA_def_property_ui_text(prop, "Edge Threshold", "Threshold for drawing outlines on geometry edges");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "edge_color", PROP_FLOAT, PROP_COLOR);
		RNA_def_property_float_sdna(prop, null, "edgeR");
		RNA_def_property_array(prop, 3);
		RNA_def_property_ui_text(prop, "Edge Color", "");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* threads */
		prop= RNA_def_property(srna, "threads", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "threads");
		RNA_def_property_range(prop, 1, threads.BLENDER_MAX_THREADS);
		RNA_def_property_int_funcs(prop, "rna_RenderSettings_threads_get", null, null);
		RNA_def_property_ui_text(prop, "Threads", "Number of CPU threads to use simultaneously while rendering (for multi-core/CPU systems)");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "threads_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "mode");
		RNA_def_property_enum_items(prop, threads_mode_items);
		RNA_def_property_ui_text(prop, "Threads Mode", "Determine the amount of render threads used");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* motion blur */
		prop= RNA_def_property(srna, "use_motion_blur", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_MBLUR);
		RNA_def_property_ui_text(prop, "Motion Blur", "Use multi-sampled 3D scene motion blur");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "motion_blur_samples", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "mblur_samples");
		RNA_def_property_range(prop, 1, 32);
		RNA_def_property_ui_text(prop, "Motion Samples", "Number of scene samples to take with motion blur");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "motion_blur_shutter", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "blurfac");
		RNA_def_property_range(prop, 0.01f, 10.0f);
		RNA_def_property_ui_range(prop, 0.01, 2.0f, 1, 0);
		RNA_def_property_ui_text(prop, "Shutter", "Time taken in frames between shutter open and close");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* border */
		prop= RNA_def_property(srna, "use_border", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_BORDER);
		RNA_def_property_ui_text(prop, "Border", "Render a user-defined border region, within the frame size. Note, this disables save_buffers and full_sample");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "border_min_x", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "border.xmin");
		RNA_def_property_range(prop, 0.0f, 1.0f);
		RNA_def_property_ui_text(prop, "Border Minimum X", "Sets minimum X value to for the render border");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "border_min_y", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "border.ymin");
		RNA_def_property_range(prop, 0.0f, 1.0f);
		RNA_def_property_ui_text(prop, "Border Minimum Y", "Sets minimum Y value for the render border");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "border_max_x", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "border.xmax");
		RNA_def_property_range(prop, 0.0f, 1.0f);
		RNA_def_property_ui_text(prop, "Border Maximum X", "Sets maximum X value for the render border");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "border_max_y", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "border.ymax");
		RNA_def_property_range(prop, 0.0f, 1.0f);
		RNA_def_property_ui_text(prop, "Border Maximum Y", "Sets maximum Y value for the render border");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_crop_to_border", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_CROP);
		RNA_def_property_ui_text(prop, "Crop to Border", "Crop the rendered frame to the defined border size");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_placeholder", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_TOUCH);
		RNA_def_property_ui_text(prop, "Placeholders", "Create empty placeholder files while rendering frames (similar to Unix 'touch')");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_overwrite", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "mode", R_NO_OVERWRITE);
		RNA_def_property_ui_text(prop, "Overwrite", "Overwrite existing files while rendering");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_compositing", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_DOCOMP);
		RNA_def_property_ui_text(prop, "Compositing", "Process the render result through the compositing pipeline, if compositing nodes are enabled");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_sequencer", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_DOSEQ);
		RNA_def_property_ui_text(prop, "Sequencer", "Process the render (and composited) result through the video sequence editor pipeline, if sequencer strips exist");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_color_management", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "color_mgt_flag", R_COLOR_MANAGEMENT);
		RNA_def_property_ui_text(prop, "Color Management", "Use linear workflow - gamma corrected imaging pipeline");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, "rna_RenderSettings_color_management_update");

		
		prop= RNA_def_property(srna, "use_file_extension", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_EXTENSION);
		RNA_def_property_ui_text(prop, "File Extensions", "Add the file format extensions to the rendered file name (eg: filename + .jpg)");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "file_format", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "imtype");
		RNA_def_property_enum_items(prop, image_type_items);
		RNA_def_property_enum_funcs(prop, null, "rna_RenderSettings_file_format_set", null);
		RNA_def_property_ui_text(prop, "File Format", "File format to save the rendered images as");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "file_extension", PROP_STRING, PROP_NONE);
		RNA_def_property_string_funcs(prop, "rna_SceneRender_file_ext_get", "rna_SceneRender_file_ext_length", null);
		RNA_def_property_ui_text(prop, "Extension", "The file extension used for saving renders");
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);

		prop= RNA_def_property(srna, "is_movie_format", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_funcs(prop, "rna_RenderSettings_is_movie_fomat_get", null);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Movie Format", "When true the format is a movie");

		prop= RNA_def_property(srna, "use_free_image_textures", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_FREE_IMAGE);
		RNA_def_property_ui_text(prop, "Free Image Textures", "Free all image texture from memory after render, to save memory before compositing");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_free_unused_nodes", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_COMP_FREE);
		RNA_def_property_ui_text(prop, "Free Unused Nodes", "Free Nodes that are not used while compositing, to save memory");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_save_buffers", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_EXR_TILE_FILE);
		RNA_def_property_boolean_funcs(prop, "rna_RenderSettings_save_buffers_get", null);
		RNA_def_property_ui_text(prop, "Save Buffers","Save tiles for all RenderLayers and SceneNodes to files in the temp directory (saves memory, required for Full Sample)");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_full_sample", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_FULL_SAMPLE);
		 RNA_def_property_boolean_funcs(prop, "rna_RenderSettings_full_sample_get", null);
		RNA_def_property_ui_text(prop, "Full Sample","Save for every anti-aliasing sample the entire RenderLayer results. This solves anti-aliasing issues with compositing");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
//		prop= RNA_def_property(srna, "use_backbuf", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, null, "bufflag", R_BACKBUF);
//		RNA_def_property_ui_text(prop, "Back Buffer", "Render backbuffer image");
//		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "display_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "displaymode");
		RNA_def_property_enum_items(prop, display_mode_items);
		RNA_def_property_ui_text(prop, "Display", "Select where rendered images will be displayed");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "filepath", PROP_STRING, PROP_FILEPATH);
		RNA_def_property_string_sdna(prop, null, "pic");
		RNA_def_property_ui_text(prop, "Output Path", "Directory/name to save animations, # characters defines the position and length of frame numbers");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		/* Bake */
		
		prop= RNA_def_property(srna, "bake_type", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "bake_mode");
		RNA_def_property_enum_items(prop, bake_mode_items);
		RNA_def_property_ui_text(prop, "Bake Mode", "Choose shading information to bake into the image");
		
		prop= RNA_def_property(srna, "bake_normal_space", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "bake_normal_space");
		RNA_def_property_enum_items(prop, bake_normal_space_items);
		RNA_def_property_ui_text(prop, "Normal Space", "Choose normal space for baking");
		
		prop= RNA_def_property(srna, "bake_quad_split", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_items(prop, bake_qyad_split_items);
		RNA_def_property_ui_text(prop, "Quad Split", "Choose the method used to split a quad into 2 triangles for baking");
		
		prop= RNA_def_property(srna, "bake_aa_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "bake_osa");
		RNA_def_property_enum_items(prop, fixed_oversample_items);
		RNA_def_property_ui_text(prop, "Anti-Aliasing Level", "");
		
		prop= RNA_def_property(srna, "use_bake_selected_to_active", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bake_flag", R_BAKE_TO_ACTIVE);
		RNA_def_property_ui_text(prop, "Selected to Active", "Bake shading on the surface of selected objects to the active object");
		
		prop= RNA_def_property(srna, "use_bake_normalize", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bake_flag", R_BAKE_NORMALIZE);
		RNA_def_property_ui_text(prop, "Normalized", "With displacement normalize to the distance, with ambient occlusion normalize without using material settings");
		
		prop= RNA_def_property(srna, "use_bake_clear", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bake_flag", R_BAKE_CLEAR);
		RNA_def_property_ui_text(prop, "Clear", "Clear Images before baking");
		
		prop= RNA_def_property(srna, "use_bake_antialiasing", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "bake_flag", R_BAKE_OSA);
		RNA_def_property_ui_text(prop, "Anti-Aliasing", "Enables Anti-aliasing");
		
		prop= RNA_def_property(srna, "bake_margin", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "bake_filter");
		RNA_def_property_range(prop, 0, 32);
		RNA_def_property_ui_text(prop, "Margin", "Amount of pixels to extend the baked result with, as post process filter");

		prop= RNA_def_property(srna, "bake_distance", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "bake_maxdist");
		RNA_def_property_range(prop, 0.0, 1000.0);
		RNA_def_property_ui_text(prop, "Distance", "Maximum distance from active object to other object (in blender units");
		
		prop= RNA_def_property(srna, "bake_bias", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "bake_biasdist");
		RNA_def_property_range(prop, 0.0, 1000.0);
		RNA_def_property_ui_text(prop, "Bias", "Bias towards faces further away from the object (in blender units)");
		
		/* stamp */
		
		prop= RNA_def_property(srna, "use_stamp_time", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_TIME);
		RNA_def_property_ui_text(prop, "Stamp Time", "Include the render frame as HH:MM:SS.FF in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_date", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_DATE);
		RNA_def_property_ui_text(prop, "Stamp Date", "Include the current date in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_frame", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_FRAME);
		RNA_def_property_ui_text(prop, "Stamp Frame", "Include the frame number in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_camera", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_CAMERA);
		RNA_def_property_ui_text(prop, "Stamp Camera", "Include the name of the active camera in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_scene", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_SCENE);
		RNA_def_property_ui_text(prop, "Stamp Scene", "Include the name of the active scene in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_note", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_NOTE);
		RNA_def_property_ui_text(prop, "Stamp Note", "Include a custom note in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_marker", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_MARKER);
		RNA_def_property_ui_text(prop, "Stamp Marker", "Include the name of the last marker in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_filename", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_FILENAME);
		RNA_def_property_ui_text(prop, "Stamp Filename", "Include the filename of the .blend file in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "use_stamp_sequencer_strip", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_SEQSTRIP);
		RNA_def_property_ui_text(prop, "Stamp Sequence Strip", "Include the name of the foreground sequence strip in image metadata");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_stamp_render_time", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_RENDERTIME);
		RNA_def_property_ui_text(prop, "Stamp Render Time", "Include the render time in the stamp image");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "stamp_note_text", PROP_STRING, PROP_NONE);
		RNA_def_property_string_sdna(prop, null, "stamp_udata");
		RNA_def_property_ui_text(prop, "Stamp Note Text", "Custom text to appear in the stamp note");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "use_stamp", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "stamp", R_STAMP_DRAW);
		RNA_def_property_ui_text(prop, "Render Stamp", "Render the stamp info text in the rendered image");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "stamp_font_size", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "stamp_font_id");
		RNA_def_property_range(prop, 8, 64);
		RNA_def_property_ui_text(prop, "Font Size", "Size of the font used when rendering stamp text");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		prop= RNA_def_property(srna, "stamp_foreground", PROP_FLOAT, PROP_COLOR);
		RNA_def_property_float_sdna(prop, null, "fg_stamp");
		RNA_def_property_array(prop, 4);
		RNA_def_property_range(prop,0.0,1.0);
		RNA_def_property_ui_text(prop, "Stamp Text Color", "Color to use for stamp text");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		prop= RNA_def_property(srna, "stamp_background", PROP_FLOAT, PROP_COLOR);
		RNA_def_property_float_sdna(prop, null, "bg_stamp");
		RNA_def_property_array(prop, 4);
		RNA_def_property_range(prop,0.0,1.0);
		RNA_def_property_ui_text(prop, "Stamp Background", "Color to use behind stamp text");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		/* sequencer draw options */

		prop= RNA_def_property(srna, "use_sequencer_gl_preview", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "seq_flag", R_SEQ_GL_PREV);
		RNA_def_property_ui_text(prop, "Sequencer OpenGL", "");

		prop= RNA_def_property(srna, "use_sequencer_gl_render", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "seq_flag", R_SEQ_GL_REND);
		RNA_def_property_ui_text(prop, "Sequencer OpenGL", "");


		prop= RNA_def_property(srna, "sequencer_gl_preview", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "seq_prev_type");
		RNA_def_property_enum_items(prop, RnaSpaceUtil.viewport_shade_items);
		RNA_def_property_ui_text(prop, "Sequencer Preview Shading", "Method to draw in the sequencer view");

		prop= RNA_def_property(srna, "sequencer_gl_render", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "seq_rend_type");
		RNA_def_property_enum_items(prop, RnaSpaceUtil.viewport_shade_items);
		RNA_def_property_ui_text(prop, "Sequencer Preview Shading", "Method to draw in the sequencer view");

		/* layers */
		
		prop= RNA_def_property(srna, "layers", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "layers", null);
		RNA_def_property_struct_type(prop, "SceneRenderLayer");
		RNA_def_property_ui_text(prop, "Render Layers", "");
		rna_def_render_layers(brna, prop);

		
		prop= RNA_def_property(srna, "use_single_layer", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "scemode", R_SINGLE_LAYER);
		RNA_def_property_ui_text(prop, "Single Layer", "Only render the active layer");
		RNA_def_property_ui_icon(prop, ICON_UNPINNED, 1);
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);

		/* engine */
		prop= RNA_def_property(srna, "engine", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_items(prop, engine_items);
		RNA_def_property_enum_funcs(prop, "rna_RenderSettings_engine_get", "rna_RenderSettings_engine_set", "rna_RenderSettings_engine_itemf");
		RNA_def_property_ui_text(prop, "Engine", "Engine to use for rendering");
		RNA_def_property_update(prop, NC_WINDOW, null);

		prop= RNA_def_property(srna, "has_multiple_engines", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_funcs(prop, "rna_RenderSettings_multiple_engines_get", null);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Multiple Engines", "More than one rendering engine is available");

		prop= RNA_def_property(srna, "use_game_engine", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_funcs(prop, "rna_RenderSettings_use_game_engine_get", null);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Use Game Engine", "Current rendering engine is a game engine");

		/* simplify */
		prop= RNA_def_property(srna, "use_simplify", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "mode", R_SIMPLIFY);
		RNA_def_property_ui_text(prop, "Use Simplify", "Enable simplification of scene for quicker preview renders");
		RNA_def_property_update(prop, 0, "rna_Scene_use_simplify_update");

		prop= RNA_def_property(srna, "simplify_subdivision", PROP_INT, PROP_UNSIGNED);
		RNA_def_property_int_sdna(prop, null, "simplify_subsurf");
		RNA_def_property_ui_range(prop, 0, 6, 1, 0);
		RNA_def_property_ui_text(prop, "Simplify Subdivision", "Global maximum subdivision level");
		RNA_def_property_update(prop, 0, "rna_Scene_simplify_update");

		prop= RNA_def_property(srna, "simplify_child_particles", PROP_FLOAT, PROP_FACTOR);
		RNA_def_property_float_sdna(prop, null, "simplify_particles");
		RNA_def_property_ui_text(prop, "Simplify Child Particles", "Global child particles percentage");
		RNA_def_property_update(prop, 0, "rna_Scene_simplify_update");

		prop= RNA_def_property(srna, "simplify_shadow_samples", PROP_INT, PROP_UNSIGNED);
		RNA_def_property_int_sdna(prop, null, "simplify_shadowsamples");
		RNA_def_property_ui_range(prop, 1, 16, 1, 0);
		RNA_def_property_ui_text(prop, "Simplify Shadow Samples", "Global maximum shadow samples");
		RNA_def_property_update(prop, 0, "rna_Scene_simplify_update");

		prop= RNA_def_property(srna, "simplify_ao_sss", PROP_FLOAT, PROP_FACTOR);
		RNA_def_property_float_sdna(prop, null, "simplify_aosss");
		RNA_def_property_ui_text(prop, "Simplify AO and SSS", "Global approximate AA and SSS quality factor");
		RNA_def_property_update(prop, 0, "rna_Scene_simplify_update");

		prop= RNA_def_property(srna, "use_simplify_triangulate", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "simplify_flag", R_SIMPLE_NO_TRIANGULATE);
		RNA_def_property_ui_text(prop, "Skip Quad to Triangles", "Disables non-planer quads being triangulated");

		/* Scene API */
//		RNA_api_scene_render(srna);
	}

	/* scene.objects */
	static void rna_def_scene_objects(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;
		PropertyRNA prop;

		FunctionRNA func;
		PropertyRNA parm;
		
		RNA_def_property_srna(cprop, "SceneObjects");
		srna= RNA_def_struct(brna, "SceneObjects", null);
		RNA_def_struct_sdna(srna, "Scene");
		RNA_def_struct_ui_text(srna, "Scene Objects", "Collection of scene objects");

		func= RNA_def_function(srna, "link", "rna_Scene_object_link");
		RNA_def_function_ui_description(func, "Link object to scene, run scene.update() after.");
		RNA_def_function_flag(func, FUNC_USE_CONTEXT|FUNC_USE_REPORTS);
		parm= RNA_def_pointer(func, "object", "Object", "", "Object to add to scene.");
		RNA_def_property_flag(parm, PROP_REQUIRED);
		parm= RNA_def_pointer(func, "base", "ObjectBase", "", "The newly created base.");
		RNA_def_function_return(func, parm);

		func= RNA_def_function(srna, "unlink", "rna_Scene_object_unlink");
		RNA_def_function_ui_description(func, "Unlink object from scene.");
		RNA_def_function_flag(func, FUNC_USE_REPORTS);
		parm= RNA_def_pointer(func, "object", "Object", "", "Object to remove from scene.");
		RNA_def_property_flag(parm, PROP_REQUIRED);

		prop= RNA_def_property(srna, "active", PROP_POINTER, PROP_NONE);
		RNA_def_property_struct_type(prop, "Object");
		RNA_def_property_pointer_funcs(prop, "rna_Scene_active_object_get", "rna_Scene_active_object_set", null, null);
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Active Object", "Active object for this scene");
		/* Could call: ED_base_object_activate(C, scene->basact);
		 * but would be a bad level call and it seems the notifier is enough */
		RNA_def_property_update(prop, NC_SCENE|ND_OB_ACTIVE, null);
	}


	/* scene.bases.* */
	static void rna_def_scene_bases(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;
		PropertyRNA prop;

//		FunctionRNA *func;
//		PropertyRNA *parm;

		RNA_def_property_srna(cprop, "SceneBases");
		srna= RNA_def_struct(brna, "SceneBases", null);
		RNA_def_struct_sdna(srna, "Scene");
		RNA_def_struct_ui_text(srna, "Scene Bases", "Collection of scene bases");

		prop= RNA_def_property(srna, "active", PROP_POINTER, PROP_NONE);
		RNA_def_property_struct_type(prop, "ObjectBase");
		RNA_def_property_pointer_sdna(prop, null, "basact");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Active Base", "Active object base in the scene");
		RNA_def_property_update(prop, NC_SCENE|ND_OB_ACTIVE, null);
	}

	/* scene.timeline_markers */
	static void rna_def_timeline_markers(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;

		FunctionRNA func;
		PropertyRNA parm;

		RNA_def_property_srna(cprop, "TimelineMarkers");
		srna= RNA_def_struct(brna, "TimelineMarkers", null);
		RNA_def_struct_sdna(srna, "Scene");
		RNA_def_struct_ui_text(srna, "Timeline Markers", "Collection of timeline markers");

		func= RNA_def_function(srna, "new", "rna_TimeLine_add");
		RNA_def_function_ui_description(func, "Add a keyframe to the curve.");
		parm= RNA_def_string(func, "name", "Marker", 0, "", "New name for the marker (not unique).");
		RNA_def_property_flag(parm, PROP_REQUIRED);

		parm= RNA_def_pointer(func, "marker", "TimelineMarker", "", "Newly created timeline marker");
		RNA_def_function_return(func, parm);


		func= RNA_def_function(srna, "remove", "rna_TimeLine_remove");
		RNA_def_function_ui_description(func, "Remove a timeline marker.");
		RNA_def_function_flag(func, FUNC_USE_REPORTS);
		parm= RNA_def_pointer(func, "marker", "TimelineMarker", "", "Timeline marker to remove.");
		RNA_def_property_flag(parm, PROP_REQUIRED|PROP_NEVER_NULL);
	}

	/* scene.keying_sets */
	static void rna_def_scene_keying_sets(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;
		PropertyRNA prop;

		FunctionRNA func;
		PropertyRNA parm;

		RNA_def_property_srna(cprop, "KeyingSets");
		srna= RNA_def_struct(brna, "KeyingSets", null);
		RNA_def_struct_sdna(srna, "Scene");
		RNA_def_struct_ui_text(srna, "Keying Sets", "Scene keying sets");

		/* Add Keying Set */
		func= RNA_def_function(srna, "new", "rna_Scene_keying_set_new");
		RNA_def_function_ui_description(func, "Add a new Keying Set to Scene.");
		RNA_def_function_flag(func, FUNC_USE_REPORTS);
		/* name */
		RNA_def_string(func, "name", "KeyingSet", 64, "Name", "Name of Keying Set");

		/* returns the new KeyingSet */
		parm= RNA_def_pointer(func, "keyingset", "KeyingSet", "", "Newly created Keying Set.");
		RNA_def_function_return(func, parm);

		prop= RNA_def_property(srna, "active", PROP_POINTER, PROP_NONE);
		RNA_def_property_struct_type(prop, "KeyingSet");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_pointer_funcs(prop, "rna_Scene_active_keying_set_get", "rna_Scene_active_keying_set_set", null, null);
		RNA_def_property_ui_text(prop, "Active Keying Set", "Active Keying Set used to insert/delete keyframes");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
		
		prop= RNA_def_property(srna, "active_index", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "active_keyingset");
		RNA_def_property_int_funcs(prop, "rna_Scene_active_keying_set_index_get", "rna_Scene_active_keying_set_index_set", null);
		RNA_def_property_ui_text(prop, "Active Keying Set Index", "Current Keying Set index (negative for 'builtin' and positive for 'absolute')");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
	}

	static void rna_def_scene_keying_sets_all(BlenderRNA brna, PropertyRNA cprop)
	{
		StructRNA srna;
		PropertyRNA prop;
		
		RNA_def_property_srna(cprop, "KeyingSetsAll");
		srna= RNA_def_struct(brna, "KeyingSetsAll", null);
		RNA_def_struct_sdna(srna, "Scene");
		RNA_def_struct_ui_text(srna, "Keying Sets All", "All available keying sets");
		
		/* NOTE: no add/remove available here, without screwing up this amalgamated list... */
		
		prop= RNA_def_property(srna, "active", PROP_POINTER, PROP_NONE);
		RNA_def_property_struct_type(prop, "KeyingSet");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_pointer_funcs(prop, "rna_Scene_active_keying_set_get", "rna_Scene_active_keying_set_set", null, null);
		RNA_def_property_ui_text(prop, "Active Keying Set", "Active Keying Set used to insert/delete keyframes");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
		
		prop= RNA_def_property(srna, "active_index", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "active_keyingset");
		RNA_def_property_int_funcs(prop, "rna_Scene_active_keying_set_index_get", "rna_Scene_active_keying_set_index_set", null);
		RNA_def_property_ui_text(prop, "Active Keying Set Index", "Current Keying Set index (negative for 'builtin' and positive for 'absolute')");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
	}

	public static RNAProcess RNA_def_scene = new RNAProcess() {
	public void run(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		FunctionRNA func;
		
		EnumPropertyItem[] audio_distance_model_items = {
			new EnumPropertyItem(0, "NONE", 0, "None", "No distance attenuation"),
			new EnumPropertyItem(1, "INVERSE", 0, "Inverse", "Inverse distance model"),
			new EnumPropertyItem(2, "INVERSE_CLAMPED", 0, "Inverse Clamped", "Inverse distance model with clamping"),
			new EnumPropertyItem(3, "LINEAR", 0, "Linear", "Linear distance model"),
			new EnumPropertyItem(4, "LINEAR_CLAMPED", 0, "Linear Clamped", "Linear distance model with clamping"),
			new EnumPropertyItem(5, "EXPONENT", 0, "Exponent", "Exponent distance model"),
			new EnumPropertyItem(6, "EXPONENT_CLAMPED", 0, "Exponent Clamped", "Exponent distance model with clamping"),
			new EnumPropertyItem(0, null, 0, null, null)};

		EnumPropertyItem[] sync_mode_items = {
			new EnumPropertyItem(0, "NONE", 0, "No Sync", "Do not sync, play every frame"),
			new EnumPropertyItem(SCE_FRAME_DROP, "FRAME_DROP", 0, "Frame Dropping", "Drop frames if playback is too slow"),
			new EnumPropertyItem(AUDIO_SYNC, "AUDIO_SYNC", 0, "AV-sync", "Sync to audio playback, dropping frames"),
			new EnumPropertyItem(0, null, 0, null, null)};

		/* Struct definition */
		srna= RNA_def_struct(brna, "Scene", "ID");
		RNA_def_struct_ui_text(srna, "Scene", "Scene consisting objects and defining time and render related settings");
		RNA_def_struct_ui_icon(srna, BIFIconID.ICON_SCENE_DATA.ordinal());
		RNA_def_struct_clear_flag(srna, STRUCT_ID_REFCOUNT);
		
		/* Global Settings */
		prop= RNA_def_property(srna, "camera", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_pointer_funcs(prop, null, null, null, "rna_Camera_object_poll");
		RNA_def_property_ui_text(prop, "Camera", "Active camera used for rendering the scene");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, "rna_Scene_view3d_update");

		prop= RNA_def_property(srna, "background_set", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "set");
		RNA_def_property_struct_type(prop, "Scene");
		RNA_def_property_flag(prop, PROP_EDITABLE|PROP_ID_SELF_CHECK);
		RNA_def_property_pointer_funcs(prop, null, "rna_Scene_set_set", null, null);
		RNA_def_property_ui_text(prop, "Background Scene", "Background set scene");
		RNA_def_property_update(prop, NC_SCENE|NA_EDITED, null);

		prop= RNA_def_property(srna, "world", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "World", "World used for rendering the scene");
		RNA_def_property_update(prop, NC_SCENE|ND_WORLD, null);

		prop= RNA_def_property(srna, "cursor_location", PROP_FLOAT, PROP_XYZ_LENGTH);
		RNA_def_property_float_sdna(prop, null, "cursor");
		RNA_def_property_ui_text(prop, "Cursor Location", "3D cursor location");
		RNA_def_property_ui_range(prop, -10000.0, 10000.0, 10, 4);
		RNA_def_property_update(prop, NC_WINDOW, null);
		
		/* Bases/Objects */
		prop= RNA_def_property(srna, "object_bases", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "base", null);
		RNA_def_property_struct_type(prop, "ObjectBase");
		RNA_def_property_ui_text(prop, "Bases", "");
		RNA_def_property_collection_funcs(prop, null, null, null, null, null, null, "rna_Scene_object_bases_lookup_string");
//		rna_def_scene_bases(brna, prop);

		prop= RNA_def_property(srna, "objects", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "base", null);
		RNA_def_property_struct_type(prop, "Object");
		RNA_def_property_ui_text(prop, "Objects", "");
		RNA_def_property_collection_funcs(prop, null, null, null, "rna_Scene_objects_get", null, null, null);
//		rna_def_scene_objects(brna, prop);

		/* Layers */
		prop= RNA_def_property(srna, "layers", PROP_BOOLEAN, PROP_LAYER_MEMBER);
		RNA_def_property_boolean_sdna(prop, null, "lay", 1);
		RNA_def_property_array(prop, 20);
		RNA_def_property_boolean_funcs(prop, null, "rna_Scene_layer_set");
		RNA_def_property_ui_text(prop, "Layers", "Layers visible when rendering the scene");
		RNA_def_property_update(prop, NC_SCENE|ND_LAYER, "rna_Scene_view3d_update");
		
		/* Frame Range Stuff */
		prop= RNA_def_property(srna, "frame_current", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.cfra");
		RNA_def_property_range(prop, MINAFRAME, MAXFRAME);
		RNA_def_property_int_funcs(prop, null, "rna_Scene_current_frame_set", null);
		RNA_def_property_ui_text(prop, "Current Frame", "");
		RNA_def_property_flag(prop, PROP_CONTEXT_UPDATE);
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, "rna_Scene_frame_update");
		
		prop= RNA_def_property(srna, "frame_start", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.sfra");
		RNA_def_property_int_funcs(prop, null, "rna_Scene_start_frame_set", null);
		RNA_def_property_range(prop, MINFRAME, MAXFRAME);
		RNA_def_property_ui_text(prop, "Start Frame", "First frame of the playback/rendering range");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME_RANGE, null);
		
		prop= RNA_def_property(srna, "frame_end", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.efra");
		RNA_def_property_int_funcs(prop, null, "rna_Scene_end_frame_set", null);
		RNA_def_property_range(prop, MINFRAME, MAXFRAME);
		RNA_def_property_ui_text(prop, "End Frame", "Final frame of the playback/rendering range");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME_RANGE, null);
		
		prop= RNA_def_property(srna, "frame_step", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.frame_step");
		RNA_def_property_range(prop, 0, MAXFRAME);
		RNA_def_property_ui_range(prop, 1, 100, 1, 0);
		RNA_def_property_ui_text(prop, "Frame Step", "Number of frames to skip forward while rendering/playing back each frame");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, null);
		
		/* Preview Range (frame-range for UI playback) */
		prop=RNA_def_property(srna, "use_preview_range", PROP_BOOLEAN, PROP_NONE); 
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_boolean_sdna(prop, null, "r.flag", SCER_PRV_RANGE);
		RNA_def_property_boolean_funcs(prop, null, "rna_Scene_use_preview_range_set");
		RNA_def_property_ui_text(prop, "Use Preview Range", "Use an alternative start/end frame for UI playback, rather than the scene start/end frame");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, null);
//		RNA_def_property_ui_icon(prop, BIFIconID.ICON_PREVIEW_RANGE.ordinal(), 0);
		
		prop= RNA_def_property(srna, "frame_preview_start", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.psfra");
		RNA_def_property_int_funcs(prop, null, "rna_Scene_preview_range_start_frame_set", null);
		RNA_def_property_ui_text(prop, "Preview Range Start Frame", "Alternative start frame for UI playback");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, null);
		
		prop= RNA_def_property(srna, "frame_preview_end", PROP_INT, PROP_TIME);
		RNA_def_property_clear_flag(prop, PROP_ANIMATABLE);
		RNA_def_property_int_sdna(prop, null, "r.pefra");
		RNA_def_property_int_funcs(prop, null, "rna_Scene_preview_range_end_frame_set", null);
		RNA_def_property_ui_text(prop, "Preview Range End Frame", "Alternative end frame for UI playback");
		RNA_def_property_update(prop, NC_SCENE|ND_FRAME, null);
		
		/* Stamp */
		prop= RNA_def_property(srna, "use_stamp_note", PROP_STRING, PROP_NONE);
		RNA_def_property_string_sdna(prop, null, "r.stamp_udata");
		RNA_def_property_ui_text(prop, "Stamp Note", "User define note for the render stamping");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* Animation Data (for Scene) */
//		rna_def_animdata_common(srna);
		
		/* Readonly Properties */
		prop= RNA_def_property(srna, "is_nla_tweakmode", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", SCE_NLA_EDIT_ON);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE); /* DO NOT MAKE THIS EDITABLE, OR NLA EDITOR BREAKS */
		RNA_def_property_ui_text(prop, "NLA TweakMode", "Indicates whether there is any action referenced by NLA being edited. Strictly read-only");
		RNA_def_property_update(prop, NC_SPACE|ND_SPACE_GRAPH, null);
		
		/* Frame dropping flag for playback and sync enum */
		prop= RNA_def_property(srna, "use_frame_drop", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "flag", SCE_FRAME_DROP);
		RNA_def_property_ui_text(prop, "Frame Dropping", "Play back dropping frames if frame display is too slow");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "sync_mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_funcs(prop, "rna_Scene_sync_mode_get", "rna_Scene_sync_mode_set", null);
		RNA_def_property_enum_items(prop, sync_mode_items);
		RNA_def_property_ui_text(prop, "Sync Mode", "How to sync playback");
		RNA_def_property_update(prop, NC_SCENE, null);


		/* Nodes (Compositing) */
		prop= RNA_def_property(srna, "node_tree", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "nodetree");
		RNA_def_property_ui_text(prop, "Node Tree", "Compositing node tree");

		prop= RNA_def_property(srna, "use_nodes", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "use_nodes", 1);
		RNA_def_property_boolean_funcs(prop, null, "rna_Scene_use_nodes_set");
		RNA_def_property_ui_text(prop, "Use Nodes", "Enable the compositing node tree");
		RNA_def_property_update(prop, NC_SCENE|ND_RENDER_OPTIONS, null);
		
		/* Sequencer */
		prop= RNA_def_property(srna, "sequence_editor", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "ed");
		RNA_def_property_struct_type(prop, "SequenceEditor");
		RNA_def_property_ui_text(prop, "Sequence Editor", "");
		
		/* Keying Sets */
		prop= RNA_def_property(srna, "keying_sets", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "keyingsets", null);
		RNA_def_property_struct_type(prop, "KeyingSet");
		RNA_def_property_ui_text(prop, "Absolute Keying Sets", "Absolute Keying Sets for this Scene");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
//		rna_def_scene_keying_sets(brna, prop);
		
		prop= RNA_def_property(srna, "keying_sets_all", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_funcs(prop, "rna_Scene_all_keyingsets_begin", "rna_Scene_all_keyingsets_next", "rna_iterator_listbase_end", "rna_iterator_listbase_get", null, null, null);
		RNA_def_property_struct_type(prop, "KeyingSet");
		RNA_def_property_ui_text(prop, "All Keying Sets", "All Keying Sets available for use (Builtins and Absolute Keying Sets for this Scene)");
		RNA_def_property_update(prop, NC_SCENE|ND_KEYINGSET, null);
//		rna_def_scene_keying_sets_all(brna, prop);
		
		/* Tool Settings */
		prop= RNA_def_property(srna, "tool_settings", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_NEVER_NULL);
		RNA_def_property_pointer_sdna(prop, null, "toolsettings");
		RNA_def_property_struct_type(prop, "ToolSettings");
		RNA_def_property_ui_text(prop, "Tool Settings", "");

		/* Unit Settings */
		prop= RNA_def_property(srna, "unit_settings", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_NEVER_NULL);
		RNA_def_property_pointer_sdna(prop, null, "unit");
		RNA_def_property_struct_type(prop, "UnitSettings");
		RNA_def_property_ui_text(prop, "Unit Settings", "Unit editing settings");

		/* Physics Settings */
		prop= RNA_def_property(srna, "gravity", PROP_FLOAT, PROP_ACCELERATION);
		RNA_def_property_float_sdna(prop, null, "physics_settings.gravity");
		RNA_def_property_array(prop, 3);
		RNA_def_property_range(prop, -200.0f, 200.0f);
		RNA_def_property_ui_text(prop, "Gravity", "Constant acceleration in a given direction");
		RNA_def_property_update(prop, 0, "rna_Physics_update");

		prop= RNA_def_property(srna, "use_gravity", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "physics_settings.flag", PHYS_GLOBAL_GRAVITY);
		RNA_def_property_ui_text(prop, "Global Gravity", "Use global gravity for all dynamics");
		RNA_def_property_update(prop, 0, "rna_Physics_update");
		
		/* Render Data */
		prop= RNA_def_property(srna, "render", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_NEVER_NULL);
		RNA_def_property_pointer_sdna(prop, null, "r");
		RNA_def_property_struct_type(prop, "RenderSettings");
		RNA_def_property_ui_text(prop, "Render Data", "");
		
		/* Markers */
		prop= RNA_def_property(srna, "timeline_markers", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "markers", null);
		RNA_def_property_struct_type(prop, "TimelineMarker");
		RNA_def_property_ui_text(prop, "Timeline Markers", "Markers used in all timelines for the current scene");
//		rna_def_timeline_markers(brna, prop);

		/* Audio Settings */
		prop= RNA_def_property(srna, "use_audio", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "audio.flag", AUDIO_MUTE);
		RNA_def_property_ui_text(prop, "Audio Muted", "Play back of audio from Sequence Editor will be muted");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "use_audio_sync", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "audio.flag", AUDIO_SYNC);
		RNA_def_property_ui_text(prop, "Audio Sync", "Play back and sync with audio clock, dropping frames if frame display is too slow");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "use_audio_scrub", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_sdna(prop, null, "audio.flag", AUDIO_SCRUB);
		RNA_def_property_ui_text(prop, "Audio Scrubbing", "Play audio from Sequence Editor while scrubbing");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "audio_doppler_speed", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "audio.speed_of_sound");
		RNA_def_property_range(prop, 0.01f, FLT_MAX);
		RNA_def_property_ui_text(prop, "Speed of Sound", "Speed of sound for Doppler effect calculation");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "audio_doppler_factor", PROP_FLOAT, PROP_NONE);
		RNA_def_property_float_sdna(prop, null, "audio.doppler_factor");
		RNA_def_property_range(prop, 0.0, FLT_MAX);
		RNA_def_property_ui_text(prop, "Doppler Factor", "Pitch factor for Doppler effect calculation");
		RNA_def_property_update(prop, NC_SCENE, null);

		prop= RNA_def_property(srna, "audio_distance_model", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_bitflag_sdna(prop, null, "audio.distance_model");
		RNA_def_property_enum_items(prop, audio_distance_model_items);
		RNA_def_property_ui_text(prop, "Distance Model", "Distance model for distance attenuation calculation");
		RNA_def_property_update(prop, NC_SCENE, null);

		/* Game Settings */
		prop= RNA_def_property(srna, "game_settings", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_NEVER_NULL);
		RNA_def_property_pointer_sdna(prop, null, "gm");
		RNA_def_property_struct_type(prop, "SceneGameData");
		RNA_def_property_ui_text(prop, "Game Data", "");

		/* Statistics */
		func= RNA_def_function(srna, "statistics", "ED_info_stats_string");
		prop= RNA_def_string(func, "statistics", "", 0, "Statistics", "");
		RNA_def_function_return(func, prop);
		
		/* Grease Pencil */
		prop= RNA_def_property(srna, "grease_pencil", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "gpd");
		RNA_def_property_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "GreasePencil");
		RNA_def_property_ui_text(prop, "Grease Pencil Data", "Grease Pencil datablock");
		
		/* Transform Orientations */
		prop= RNA_def_property(srna, "orientations", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "transform_spaces", null);
		RNA_def_property_struct_type(prop, "TransformOrientation");
		RNA_def_property_ui_text(prop, "Transform Orientations", "");

		/* Nestled Data  */
		rna_def_tool_settings(brna);
//		rna_def_unit_settings(brna);
		rna_def_scene_render_data(brna);
//		rna_def_scene_game_data(brna);
		rna_def_scene_render_layer(brna);
//		rna_def_transform_orientation(brna);
		
		/* Scene API */
//		RNA_api_scene(srna);
	}};
	
//	public static RNAProcess RNA_def_tool_settings = new RNAProcess() {
//	public void run(BlenderRNA brna)
//	{
//		rna_def_tool_settings(brna);
//	}};

//	#endif

}
