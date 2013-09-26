package blender.makesrna.srna;

//import static blender.makesrna.RNATypes.PROP_ACCELERATION;
//import static blender.makesrna.RNATypes.PROP_BOOLEAN;
//import static blender.makesrna.RNATypes.PROP_COLLECTION;
//import static blender.makesrna.RNATypes.PROP_ENUM;
//import static blender.makesrna.RNATypes.PROP_FLOAT;
//import static blender.makesrna.RNATypes.PROP_INT;
//import static blender.makesrna.RNATypes.PROP_LAYER_MEMBER;
//import static blender.makesrna.RNATypes.PROP_NONE;
//import static blender.makesrna.RNATypes.PROP_POINTER;
//import static blender.makesrna.RNATypes.PROP_STRING;
//import static blender.makesrna.RNATypes.PROP_TIME;
//import static blender.makesrna.RNATypes.PROP_UNIT_ACCELERATION;
//import static blender.makesrna.RNATypes.PROP_UNIT_LENGTH;
//import static blender.makesrna.RNATypes.PROP_UNIT_NONE;
//import static blender.makesrna.RNATypes.PROP_UNIT_TIME;
//import static blender.makesrna.RNATypes.PROP_XYZ;
//import static blender.makesrna.RnaAccess.RNA_GreasePencil;
//import static blender.makesrna.RnaAccess.RNA_KeyingSet;
//import static blender.makesrna.RnaAccess.RNA_Object;
//import static blender.makesrna.RnaAccess.RNA_ObjectBase;
//import static blender.makesrna.RnaAccess.RNA_RenderSettings;
//import static blender.makesrna.RnaAccess.RNA_SceneGameData;
//import static blender.makesrna.RnaAccess.RNA_SequenceEditor;
//import static blender.makesrna.RnaAccess.RNA_TimelineMarker;
//import static blender.makesrna.RnaAccess.RNA_UnitSettings;
//import static blender.makesrna.RnaAccess.rna_iterator_listbase_end;
//import static blender.makesrna.RnaDefine.FLT_MAX;
//import static blender.makesrna.RnaDefine.INT_MAX;
//import static blender.makesrna.RnaDefine.INT_MIN;
//import static blender.makesrna.RnaRna.rna_builtin_properties_begin;
//import static blender.makesrna.RnaRna.rna_builtin_properties_get;
//import static blender.makesrna.RnaRna.rna_builtin_properties_lookup_string;
//import static blender.makesrna.RnaRna.rna_builtin_properties_next;
//import static blender.makesrna.RnaSceneUtil.rna_Scene_current_frame_set;
//import static blender.makesrna.RnaSceneUtil.rna_Scene_end_frame_set;
//import static blender.makesrna.RnaSceneUtil.rna_Scene_start_frame_set;
//import static blender.makesrna.RnaSceneUtil.rna_Scene_use_preview_range_set;
//import blender.makesrna.RNATypes.CollectionPropertyIterator;
//import blender.makesrna.RNATypes.EnumPropertyItem;
//import blender.makesrna.RNATypes.PointerRNA;
//import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
//import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
//import blender.makesrna.rna_internal_types.EnumPropertyRNA;
//import blender.makesrna.rna_internal_types.FloatPropertyRNA;
//import blender.makesrna.rna_internal_types.IntPropertyRNA;
//import blender.makesrna.rna_internal_types.PointerPropertyRNA;
//import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
//import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
//import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
//import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
//import blender.makesrna.rna_internal_types.PropertyRNA;
//import blender.makesrna.rna_internal_types.StringPropertyRNA;
//import blender.makesrna.rna_internal_types.StructRNA;

import blender.makesdna.sdna.*;

import static blender.makesrna.RnaRna.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaDefine.*;
import static blender.makesrna.RNATypes.*;
import blender.makesrna.rna_internal_types.*;
import blender.blenlib.*;

import static blender.makesrna.RnaSceneUtil.*;
import static blender.makesrna.RnaSceneApi.*;

public class RnaScene extends RnaStruct {
	
	public RnaRenderSettings render;
	public Object keying_sets_all;
	public RnaKeyingSet keying_sets = new RnaKeyingSet(new PointerRNA());
	public RnaTimelineMarker[] timeline_markers = new RnaTimelineMarker[2];
	public boolean use_frame_drop = false;
	public RnaUnitSettings unit_settings = new RnaUnitSettings(null);
	public boolean use_gravity = false;
	
	public RnaScene(PointerRNA ptr) {
		super(ptr);
		for (int i=0; i<timeline_markers.length; i++) {
			timeline_markers[i] = new RnaTimelineMarker(null);
		}
		render = new RnaRenderSettings(new PointerRNA(RnaRenderSettings.RNA_RenderSettings, ((Scene)ptr.data).r));
	}
	
	public float[] getCursor_location() {
		return ((Scene)ptr.data).cursor;
	}
	
	public void setFrame_current(int value)	{
		rna_Scene_current_frame_set.setInt(ptr, value);
	}
	
	public void setFrame_start(int value)	{
		rna_Scene_start_frame_set.setInt(ptr, value);
	}
	
	public void setFrame_end(int value)	{
		rna_Scene_end_frame_set.setInt(ptr, value);
	}
	
	public void setUse_preview_range(boolean value)	{
		rna_Scene_use_preview_range_set(ptr, value);
	}
	
	// default
	public boolean getUse_preview_range()	{
		return false;
	}
	
	public String statistics() {
		return "tmp scene statistics";
	}
	
	private static PropCollectionGetFunc Scene_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc Scene_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_Scene_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= Scene_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc Scene_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= Scene_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc Scene_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	/* Scene */
	private static CollectionPropertyRNA rna_Scene_rna_properties = new CollectionPropertyRNA(
			null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Scene_rna_properties_begin, Scene_rna_properties_next, Scene_rna_properties_end, Scene_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);

	private static PointerPropertyRNA rna_Scene_rna_type = new PointerPropertyRNA(
			null, null,
		-1, "rna_type", 524288, "RNA",
		"RNA type definition",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		rna_builtin_type_get, null, null, null,RNA_Struct
	);

	private static PointerPropertyRNA rna_Scene_camera = new PointerPropertyRNA(
			null, null,
		-1, "camera", 1, "Camera",
		"Active camera used for rendering the scene",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_Scene_view3d_update, 67108865, null, null,
		0, -1, null,
//		null, null, null, "rna_Camera_object_poll",null
		null, null, null, null,null
	);

	private static PointerPropertyRNA rna_Scene_background_set = new PointerPropertyRNA(
			null, null,
		-1, "background_set", 1048577, "Background Scene",
		"Background set scene",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108865, null, null,
		0, -1, null,
//		null, "rna_Scene_set_set", null, null,RNA_Scene
		null, null, null, null,null
	);

	private static PointerPropertyRNA rna_Scene_world = new PointerPropertyRNA(
			null, null,
		-1, "world", 1, "World",
		"World used for rendering the scene",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 73138176, null, null,
		0, -1, null,
		null, null, null, null,null
	);

	private static FloatPropertyRNA rna_Scene_cursor_location = new FloatPropertyRNA(
			null, null,
		-1, "cursor_location", 3, "Cursor Location",
		"3D cursor location",
		0,
		PROP_FLOAT, PROP_XYZ|PROP_UNIT_LENGTH, null, 0, new int[]{0, 0, 0}, 0,
		null, 33554432, null, null,
		0, -1, null,
		null, null, null, null, null, -10000.0f, 10000.0f, -FLT_MAX, FLT_MAX, 10.0f, 4, 0.0f, null
	);

	private static CollectionPropertyRNA rna_Scene_object_bases = new CollectionPropertyRNA(
			null, null,
		-1, "object_bases", 0, "Bases",
		"",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, null, null, null, RNA_ObjectBase
	);

	private static CollectionPropertyRNA rna_Scene_objects = new CollectionPropertyRNA(
			null, null,
		-1, "objects", 0, "Objects",
		"",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, null, null, null, RNA_Object
	);

	private static BooleanPropertyRNA rna_Scene_layers = new BooleanPropertyRNA(
			null, null,
		-1, "layers", 3, "Layers",
		"Layers visible when rendering the scene",
		0,
		PROP_BOOLEAN, PROP_LAYER_MEMBER|PROP_UNIT_NONE, null, 0, new int[]{20, 0, 0}, 0,
		rna_Scene_view3d_update, 68157440, null, null,
		0, -1, null,
//		null, null, null, "rna_Scene_layer_set", false, null
		null, null, null, null, false, null
	);

	private static IntPropertyRNA rna_Scene_frame_current = new IntPropertyRNA(
			null, null,
		-1, "frame_current", 4194305, "Current Frame",
		"",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		rna_Scene_frame_update, 67305472, null, null,
		0, -1, null,
		null, rna_Scene_current_frame_set, null, null, null,
//		null, null, null, null, null,
		-300000, 300000, -300000, 300000, 1, 0, null
	);

	private static IntPropertyRNA rna_Scene_frame_start = new IntPropertyRNA(
			null, null,
		-1, "frame_start", 1, "Start Frame",
		"First frame of the playback/rendering range",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		null, 68222976, null, null,
		0, -1, null,
		null, rna_Scene_start_frame_set, null, null, null,
//		null, null, null, null, null,
		1, 300000, 1, 300000, 1, 0, null
	);

	private static IntPropertyRNA rna_Scene_frame_end = new IntPropertyRNA(
			null, null,
		-1, "frame_end", 1, "End Frame",
		"Final frame of the playback/rendering range",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		null, 68222976, null, null,
		0, -1, null,
		null, rna_Scene_end_frame_set, null, null, null,
//		null, null, null, null, null,
		1, 300000, 1, 300000, 1, 0, null
	);

	private static IntPropertyRNA rna_Scene_frame_step = new IntPropertyRNA(
			null, null,
		-1, "frame_step", 1, "Frame Step",
		"Number of frames to skip forward while rendering/playing back each frame",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		null, 67305472, null, null,
		0, -1, null,
		null, null, null, null, null,
		1, 100, 0, 300000, 1, 0, null
	);

	private static BooleanPropertyRNA rna_Scene_use_preview_range = new BooleanPropertyRNA(
		null, null,
		-1, "use_preview_range", 1, "Use Preview Range",
		"Use an alternative start/end frame for UI playback, rather than the scene start/end frame",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67305472, null, null,
		0, -1, null,
//		null, "rna_Scene_use_preview_range_set", null, null, false, null
		null, null, null, null, false, null
	);

	private static IntPropertyRNA rna_Scene_frame_preview_start = new IntPropertyRNA(
			null, null,
		-1, "frame_preview_start", 1, "Preview Range Start Frame",
		"Alternative start frame for UI playback",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		null, 67305472, null, null,
		0, -1, null,
//		null, "rna_Scene_preview_range_start_frame_set", null, null, null,
		null, null, null, null, null,
		-10000, 10000, INT_MIN, INT_MAX, 1, 0, null
	);

	private static IntPropertyRNA rna_Scene_frame_preview_end = new IntPropertyRNA(
			null, null,
		-1, "frame_preview_end", 1, "Preview Range End Frame",
		"Alternative end frame for UI playback",
		0,
		PROP_INT, PROP_TIME|PROP_UNIT_TIME, null, 0, new int[]{0, 0, 0}, 0,
		null, 67305472, null, null,
		0, -1, null,
//		null, "rna_Scene_preview_range_end_frame_set", null, null, null,
		null, null, null, null, null,
		-10000, 10000, INT_MIN, INT_MAX, 1, 0, null
	);

	private static StringPropertyRNA rna_Scene_use_stamp_note = new StringPropertyRNA(
			null, null,
		-1, "use_stamp_note", 1, "Stamp Note",
		"User define note for the render stamping",
		0,
		PROP_STRING, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67371008, null, null,
		0, -1, null,
		null, null, null, 0, ""
	);

	private static BooleanPropertyRNA rna_Scene_is_nla_tweakmode = new BooleanPropertyRNA(
		null, null,
		-1, "is_nla_tweakmode", 2, "NLA TweakMode",
		"Indicates whether there is any action referenced by NLA being edited. Strictly read-only",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252510208, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_Scene_use_frame_drop = new BooleanPropertyRNA(
		null, null,
		-1, "use_frame_drop", 3, "Frame Dropping",
		"Play back dropping frames if frame display is too slow",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	private static EnumPropertyItem[] rna_Scene_sync_mode_items = {
		new EnumPropertyItem(0, "NONE", 0, "No Sync", "Do not sync, play every frame"),
		new EnumPropertyItem(8, "FRAME_DROP", 0, "Frame Dropping", "Drop frames if playback is too slow"),
		new EnumPropertyItem(2, "AUDIO_SYNC", 0, "AV-sync", "Sync to audio playback, dropping frames"),
		new EnumPropertyItem(0, null, 0, null, null)
	};

	private static EnumPropertyRNA rna_Scene_sync_mode = new EnumPropertyRNA(
			null, null,
		-1, "sync_mode", 3, "Sync Mode",
		"How to sync playback",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
//		"rna_Scene_sync_mode_get", "rna_Scene_sync_mode_set", null, sync_mode_items, 3, 0
		null, null, null, rna_Scene_sync_mode_items, 3, 0
	);

	private static PointerPropertyRNA rna_Scene_node_tree = new PointerPropertyRNA(
			null, null,
		-1, "node_tree", 0, "Node Tree",
		"Compositing node tree",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,null
	);

	private static BooleanPropertyRNA rna_Scene_use_nodes = new BooleanPropertyRNA(
		null, null,
		-1, "use_nodes", 3, "Use Nodes",
		"Enable the compositing node tree",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67371008, null, null,
		0, -1, null,
//		null, "rna_Scene_use_nodes_set", null, null, false, null
		null, null, null, null, false, null
	);

	private static PointerPropertyRNA rna_Scene_sequence_editor = new PointerPropertyRNA(
			null, null,
		-1, "sequence_editor", 0, "Sequence Editor",
		"",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RNA_SequenceEditor
	);

	private static CollectionPropertyRNA rna_Scene_keying_sets = new CollectionPropertyRNA(
			null, null,
		-1, "keying_sets", 0, "Absolute Keying Sets",
		"Absolute Keying Sets for this Scene",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 68026368, null, null,
		0, -1, null,
		null, null, null, null, null, null, null, RNA_KeyingSet
	);

	private static CollectionPropertyRNA rna_Scene_keying_sets_all = new CollectionPropertyRNA(
			null, null,
		-1, "keying_sets_all", 0, "All Keying Sets",
		"All Keying Sets available for use (Builtins and Absolute Keying Sets for this Scene)",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 68026368, null, null,
		0, -1, null,
		null, null, null, null, null, null, null, RNA_KeyingSet
	);

	private static PointerPropertyRNA rna_Scene_tool_settings = new PointerPropertyRNA(
			null, null,
		-1, "tool_settings", 262144, "Tool Settings",
		"",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RnaToolSettings.RNA_ToolSettings
	);

	private static PointerPropertyRNA rna_Scene_unit_settings = new PointerPropertyRNA(
			null, null,
		-1, "unit_settings", 262144, "Unit Settings",
		"Unit editing settings",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RNA_UnitSettings
	);

	private static FloatPropertyRNA rna_Scene_gravity = new FloatPropertyRNA(
			null, null,
		-1, "gravity", 3, "Gravity",
		"Constant acceleration in a given direction",
		0,
		PROP_FLOAT, PROP_ACCELERATION|PROP_UNIT_ACCELERATION, null, 0, new int[]{3, 0, 0}, 0,
		rna_Physics_update, 0, null, null,
		0, -1, null,
		null, null, null, null, null, -200.0f, 200.0f, -200.0f, 200.0f, 10.0f, 3, 0.0f, null
	);

	private static BooleanPropertyRNA rna_Scene_use_gravity = new BooleanPropertyRNA(
			null, null,
		-1, "use_gravity", 3, "Global Gravity",
		"Use global gravity for all dynamics",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_Physics_update, 0, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static PointerPropertyRNA rna_Scene_render = new PointerPropertyRNA(
			null, null,
		-1, "render", 262144, "Render Data",
		"",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RNA_RenderSettings
	);

	private static CollectionPropertyRNA rna_Scene_timeline_markers = new CollectionPropertyRNA(
			null, null,
		-1, "timeline_markers", 0, "Timeline Markers",
		"Markers used in all timelines for the current scene",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, null, null, null, RNA_TimelineMarker
	);

	private static BooleanPropertyRNA rna_Scene_use_audio = new BooleanPropertyRNA(
		null, null,
		-1, "use_audio", 3, "Audio Muted",
		"Play back of audio from Sequence Editor will be muted",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_Scene_use_audio_sync = new BooleanPropertyRNA(
		null, null,
		-1, "use_audio_sync", 3, "Audio Sync",
		"Play back and sync with audio clock, dropping frames if frame display is too slow",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_Scene_use_audio_scrub = new BooleanPropertyRNA(
			null, null,
		-1, "use_audio_scrub", 3, "Audio Scrubbing",
		"Play audio from Sequence Editor while scrubbing",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static FloatPropertyRNA rna_Scene_audio_doppler_speed = new FloatPropertyRNA(
			null, null,
		-1, "audio_doppler_speed", 3, "Speed of Sound",
		"Speed of sound for Doppler effect calculation",
		0,
		PROP_FLOAT, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, null, 0.0099999998f, FLT_MAX, 0.0099999998f, FLT_MAX, 10.0f, 3, 0.0f, null
	);

	private static FloatPropertyRNA rna_Scene_audio_doppler_factor = new FloatPropertyRNA(
			null, null,
		-1, "audio_doppler_factor", 3, "Doppler Factor",
		"Pitch factor for Doppler effect calculation",
		0,
		PROP_FLOAT, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, null, null, 0.0f, FLT_MAX, 0.0f, FLT_MAX, 10.0f, 3, 0.0f, null
	);
	
	private static EnumPropertyItem[] rna_Scene_audio_distance_model_items = {
		new EnumPropertyItem(0, "NONE", 0, "None", "No distance attenuation"),
		new EnumPropertyItem(1, "INVERSE", 0, "Inverse", "Inverse distance model"),
		new EnumPropertyItem(2, "INVERSE_CLAMPED", 0, "Inverse Clamped", "Inverse distance model with clamping"),
		new EnumPropertyItem(3, "LINEAR", 0, "Linear", "Linear distance model"),
		new EnumPropertyItem(4, "LINEAR_CLAMPED", 0, "Linear Clamped", "Linear distance model with clamping"),
		new EnumPropertyItem(5, "EXPONENT", 0, "Exponent", "Exponent distance model"),
		new EnumPropertyItem(6, "EXPONENT_CLAMPED", 0, "Exponent Clamped", "Exponent distance model with clamping"),
		new EnumPropertyItem(0, null, 0, null, null)
	};

	private static EnumPropertyRNA rna_Scene_audio_distance_model = new EnumPropertyRNA(
			null, null,
		-1, "audio_distance_model", 3, "Distance Model",
		"Distance model for distance attenuation calculation",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 67108864, null, null,
		0, -1, null,
		null, null, null, rna_Scene_audio_distance_model_items, 7, 0
	);

	private static PointerPropertyRNA rna_Scene_game_settings = new PointerPropertyRNA(
			null, null,
		-1, "game_settings", 262144, "Game Data",
		"",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RNA_SceneGameData
	);

	private static PointerPropertyRNA rna_Scene_grease_pencil = new PointerPropertyRNA(
			null, null,
		-1, "grease_pencil", 1, "Grease Pencil Data",
		"Grease Pencil datablock",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null,RNA_GreasePencil
	);
	
	private static CollectionPropertyRNA rna_Scene_orientations = new CollectionPropertyRNA(
		null, null,
		-1, "orientations", 0, "Transform Orientations",
		"",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
//		null, null, Scene_orientations_end, null, null, null, null, RNA_TransformOrientation
		null, null, null, null, null, null, null, null
	);
	
	static { ListBase tmp = new ListBase();
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_rna_properties);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_rna_type);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_camera);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_background_set);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_world);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_cursor_location);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_object_bases);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_objects);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_layers);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_current);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_start);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_end);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_step);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_preview_range);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_preview_start);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_frame_preview_end);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_stamp_note);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_is_nla_tweakmode);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_frame_drop);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_sync_mode);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_node_tree);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_nodes);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_sequence_editor);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_keying_sets);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_keying_sets_all);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_tool_settings);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_unit_settings);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_gravity);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_gravity);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_render);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_timeline_markers);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_audio);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_audio_sync);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_use_audio_scrub);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_audio_doppler_speed);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_audio_doppler_factor);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_audio_distance_model);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_game_settings);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_grease_pencil);
	ListBaseUtil.BLI_addtail(tmp, rna_Scene_orientations);
}
	
	public static StructRNA RNA_Scene = new StructRNA(
			RnaToolSettings.RNA_ToolSettings, RnaContext.RNA_Context,
			//null, null,
			null,
		rna_Scene_rna_properties, rna_Scene_orientations,
		null,null,
		"Scene", 0, "Scene", "Scene consisting objects and defining time and render related settings",
		156,
		null, (PropertyRNA)rna_Scene_rna_properties,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
//		(FunctionRNA)rna_Scene_statistics_func, (FunctionRNA)rna_Scene_statistics_func
		null, null
	);
	
	static {
//		((PropertyRNA)rna_Scene_rna_properties).next = rna_Scene_rna_type;
//		((PropertyRNA)rna_Scene_rna_type).next = rna_Scene_camera;
//		((PropertyRNA)rna_Scene_camera).next = rna_Scene_background_set;
//		((PropertyRNA)rna_Scene_background_set).next = rna_Scene_world;
//		((PropertyRNA)rna_Scene_world).next = rna_Scene_cursor_location;
//		((PropertyRNA)rna_Scene_cursor_location).next = rna_Scene_object_bases;
//		((PropertyRNA)rna_Scene_object_bases).next = rna_Scene_objects;
//		((PropertyRNA)rna_Scene_objects).next = rna_Scene_layers;
//		((PropertyRNA)rna_Scene_layers).next = rna_Scene_frame_current;
//		((PropertyRNA)rna_Scene_frame_current).next = rna_Scene_frame_start;
//		((PropertyRNA)rna_Scene_frame_start).next = rna_Scene_frame_end;
//		((PropertyRNA)rna_Scene_frame_end).next = rna_Scene_frame_step;
//		((PropertyRNA)rna_Scene_frame_step).next = rna_Scene_use_preview_range;
//		((PropertyRNA)rna_Scene_use_preview_range).next = rna_Scene_frame_preview_start;
//		((PropertyRNA)rna_Scene_frame_preview_start).next = rna_Scene_frame_preview_end;
//		((PropertyRNA)rna_Scene_frame_preview_end).next = rna_Scene_use_stamp_note;
//		((PropertyRNA)rna_Scene_use_stamp_note).next = rna_Scene_is_nla_tweakmode;
//		((PropertyRNA)rna_Scene_is_nla_tweakmode).next = rna_Scene_use_frame_drop;
//		((PropertyRNA)rna_Scene_use_frame_drop).next = rna_Scene_sync_mode;
//		((PropertyRNA)rna_Scene_sync_mode).next = rna_Scene_node_tree;
//		((PropertyRNA)rna_Scene_node_tree).next = rna_Scene_use_nodes;
//		((PropertyRNA)rna_Scene_use_nodes).next = rna_Scene_sequence_editor;
//		((PropertyRNA)rna_Scene_sequence_editor).next = rna_Scene_keying_sets;
//		((PropertyRNA)rna_Scene_keying_sets).next = rna_Scene_keying_sets_all;
//		((PropertyRNA)rna_Scene_keying_sets_all).next = rna_Scene_tool_settings;
//		((PropertyRNA)rna_Scene_tool_settings).next = rna_Scene_unit_settings;
//		((PropertyRNA)rna_Scene_unit_settings).next = rna_Scene_gravity;
//		((PropertyRNA)rna_Scene_gravity).next = rna_Scene_use_gravity;
//		((PropertyRNA)rna_Scene_use_gravity).next = rna_Scene_render;
//		((PropertyRNA)rna_Scene_render).next = rna_Scene_timeline_markers;
//		((PropertyRNA)rna_Scene_timeline_markers).next = rna_Scene_use_audio;
//		((PropertyRNA)rna_Scene_use_audio).next = rna_Scene_use_audio_sync;
//		((PropertyRNA)rna_Scene_use_audio_sync).next = rna_Scene_use_audio_scrub;
//		((PropertyRNA)rna_Scene_use_audio_scrub).next = rna_Scene_audio_doppler_speed;
//		((PropertyRNA)rna_Scene_audio_doppler_speed).next = rna_Scene_audio_doppler_factor;
//		((PropertyRNA)rna_Scene_audio_doppler_factor).next = rna_Scene_audio_distance_model;
//		((PropertyRNA)rna_Scene_audio_distance_model).next = rna_Scene_game_settings;
//		((PropertyRNA)rna_Scene_game_settings).next = rna_Scene_grease_pencil;
//		((PropertyRNA)rna_Scene_grease_pencil).next = rna_Scene_orientations;
		rna_Scene_background_set.type = RNA_Scene;
	}

}
