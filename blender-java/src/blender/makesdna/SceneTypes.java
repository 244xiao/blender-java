/**
 * $Id: DNA_scene_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $ 
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

package blender.makesdna;

import blender.makesdna.sdna.Scene;

//#ifndef DNA_SCENE_TYPES_H
//#define DNA_SCENE_TYPES_H
//
//#ifdef __cplusplus
//extern "C" {
//#endif
//
//#include "DNA_vec_types.h"
//#include "DNA_listBase.h"
//#include "DNA_scriptlink_types.h"
//#include "DNA_ID.h"
//#include "DNA_scriptlink_types.h"
//
//struct Radio;
//struct Object;
//struct World;
//struct Scene;
//struct Image;
//struct Group;
//struct bNodeTree;
//

public class SceneTypes {
//typedef struct Base {
//	struct Base *next, *prev;
//	unsigned int lay, selcol;
//	int flag;
//	short sx, sy;
//	struct Object *object;
//} Base;
//
//typedef struct AviCodecData {
//	void			*lpFormat;  /* save format */
//	void			*lpParms;   /* compressor options */
//	unsigned int	cbFormat;	    /* size of lpFormat buffer */
//	unsigned int	cbParms;	    /* size of lpParms buffer */
//
//	unsigned int	fccType;            /* stream type, for consistency */
//	unsigned int	fccHandler;         /* compressor */
//	unsigned int	dwKeyFrameEvery;    /* keyframe rate */
//	unsigned int	dwQuality;          /* compress quality 0-10,000 */
//	unsigned int	dwBytesPerSecond;   /* bytes per second */
//	unsigned int	dwFlags;            /* flags... see below */
//	unsigned int	dwInterleaveEvery;  /* for non-video streams only */
//	unsigned int	pad;
//
//	char			avicodecname[128];
//} AviCodecData;
//
//typedef struct QuicktimeCodecData {
//
//	void			*cdParms;   /* codec/compressor options */
//	void			*pad;	    /* padding */
//
//	unsigned int	cdSize;		    /* size of cdParms buffer */
//	unsigned int	pad2;		    /* padding */
//
//	char			qtcodecname[128];
//} QuicktimeCodecData;
//
//typedef struct FFMpegCodecData {
//	int type;
//	int codec;
//	int audio_codec;
//	int video_bitrate;
//	int audio_bitrate;
//	int gop_size;
//	int flags;
//
//	int rc_min_rate;
//	int rc_max_rate;
//	int rc_buffer_size;
//	int mux_packet_size;
//	int mux_rate;
//	IDProperty *properties;
//} FFMpegCodecData;
//
//
//typedef struct AudioData {
//	int mixrate;
//	float main;		/* Main mix in dB */
//	short flag;
//	short pad[3];
//} AudioData;
//
//typedef struct SceneRenderLayer {
//	struct SceneRenderLayer *next, *prev;
//
//	char name[32];
//
//	struct Material *mat_override;
//	struct Group *light_override;
//
//	unsigned int lay;		/* scene->lay itself has priority over this */
//	unsigned int lay_zmask;	/* has to be after lay, this is for Z-masking */
//	int layflag;
//
//	int pad;
//
//	int passflag;			/* pass_xor has to be after passflag */
//	int pass_xor;
//} SceneRenderLayer;

/* srl->layflag */
public static final int SCE_LAY_SOLID=	1;
public static final int SCE_LAY_ZTRA=	2;
public static final int SCE_LAY_HALO=	4;
public static final int SCE_LAY_EDGE=	8;
public static final int SCE_LAY_SKY=	16;
public static final int SCE_LAY_STRAND=	32;
	/* flags between 32 and 0x8000 are set to 1 already, for future options */

public static final int SCE_LAY_ALL_Z=		0x8000;
public static final int SCE_LAY_XOR=		0x10000;
public static final int SCE_LAY_DISABLE=	0x20000;
public static final int SCE_LAY_ZMASK=		0x40000;
public static final int SCE_LAY_NEG_ZMASK=	0x80000;

/* srl->passflag */
//public static final int SCE_PASS_COMBINED=	1;
//public static final int SCE_PASS_Z=			2;
//public static final int SCE_PASS_RGBA=		4;
//public static final int SCE_PASS_DIFFUSE=	8;
//public static final int SCE_PASS_SPEC=		16;
//public static final int SCE_PASS_SHADOW=	32;
//public static final int SCE_PASS_AO=		64;
//public static final int SCE_PASS_REFLECT=	128;
//public static final int SCE_PASS_NORMAL=	256;
//public static final int SCE_PASS_VECTOR=	512;
//public static final int SCE_PASS_REFRACT=	1024;
//public static final int SCE_PASS_INDEXOB=	2048;
//public static final int SCE_PASS_UV=		4096;
//public static final int SCE_PASS_RADIO=		8192;
//public static final int SCE_PASS_MIST=		16384;
public static final int SCE_PASS_COMBINED=		(1<<0);
public static final int SCE_PASS_Z=				(1<<1);
public static final int SCE_PASS_RGBA=			(1<<2);
public static final int SCE_PASS_DIFFUSE=		(1<<3);
public static final int SCE_PASS_SPEC=			(1<<4);
public static final int SCE_PASS_SHADOW=			(1<<5);
public static final int SCE_PASS_AO=				(1<<6);
public static final int SCE_PASS_REFLECT=		(1<<7);
public static final int SCE_PASS_NORMAL=			(1<<8);
public static final int SCE_PASS_VECTOR=			(1<<9);
public static final int SCE_PASS_REFRACT=		(1<<10);
public static final int SCE_PASS_INDEXOB=		(1<<11);
public static final int SCE_PASS_UV=				(1<<12);
public static final int SCE_PASS_INDIRECT=		(1<<13);
public static final int SCE_PASS_MIST=			(1<<14);
public static final int SCE_PASS_RAYHITS=		(1<<15);
public static final int SCE_PASS_EMIT=			(1<<16);
public static final int SCE_PASS_ENVIRONMENT=	(1<<17);

/* note, srl->passflag is treestore element 'nr' in outliner, short still... */


//typedef struct RenderData {
//
//	struct AviCodecData *avicodecdata;
//	struct QuicktimeCodecData *qtcodecdata;
//	struct FFMpegCodecData ffcodecdata;
//
//	int cfra, sfra, efra;	/* frames as in 'images' */
//	int psfra, pefra;		/* start+end frames of preview range */
//
//	int images, framapto;
//	short flag, threads;
//
//	float ctime;			/* use for calcutions */
//	float framelen, blurfac;
//
//	/** For UR edge rendering: give the edges this color */
//	float edgeR, edgeG, edgeB;
//
//	short fullscreen, xplay, yplay, freqplay;	/* standalone player */
//	short depth, attrib, rt1, rt2;			/* standalone player */
//
//	short stereomode;	/* standalone player stereo settings */
//
//	short dimensionspreset;		/* for the dimensions presets menu */
//
// 	short filtertype;	/* filter is box, tent, gauss, mitch, etc */
//
//	short size, maximsize;	/* size in %, max in Kb */
//	/* from buttons: */
//	/**
//	 * The desired number of pixels in the x direction
//	 */
//	short xsch;
//	/**
//	 * The desired number of pixels in the y direction
//	 */
//	short ysch;
//	/**
//	 * The number of part to use in the x direction
//	 */
//	short xparts;
//	/**
//	 * The number of part to use in the y direction
//	 */
//	short yparts;
//
//	short winpos, planes, imtype, subimtype;
//
//	/** Mode bits:                                                           */
//	/* 0: Enable backbuffering for images                                    */
//	short bufflag;
// 	short quality;
//
//	short rpad, rpad1, rpad2;
//
//	/**
//	 * Flags for render settings. Use bit-masking to access the settings.
//	 */
//	int scemode;
//
//	/**
//	 * Flags for render settings. Use bit-masking to access the settings.
//	 */
//	int mode;
//
//	/* render engine, octree resolution */
//	short renderer, ocres;
//
//	/**
//	 * What to do with the sky/background. Picks sky/premul/key
//	 * blending for the background
//	 */
//	short alphamode;
//
//	/**
//	 * The number of samples to use per pixel.
//	 */
//	short osa;
//
//	short frs_sec, edgeint;
//
//	/* safety, border and display rect */
//	rctf safety, border;
//	rcti disprect;
//
//	/* information on different layers to be rendered */
//	ListBase layers;
//	short actlay, pad;
//
//	/**
//	 * Adjustment factors for the aspect ratio in the x direction, was a short in 2.45
//	 */
//	float xasp;
//	/**
//	 * Adjustment factors for the aspect ratio in the x direction, was a short in 2.45
//	 */
//	float yasp;
//
//	float frs_sec_base;
//
//	/**
//	 * Value used to define filter size for all filter options  */
//	float gauss;
//
//	/** post-production settings. Depricated, but here for upwards compat (initialized to 1) */
//	float postmul, postgamma, posthue, postsat;
//
// 	/* Dither noise intensity */
//	float dither_intensity;
//
//	/* Bake Render options */
//	short bake_osa, bake_filter, bake_mode, bake_flag;
//	short bake_normal_space, bake_quad_split;
//	float bake_maxdist, bake_biasdist, bake_pad;
//
//	/* yafray: global panel params. TODO: move elsewhere */
//	short GIquality, GIcache, GImethod, GIphotons, GIdirect;
//	short YF_AA, YFexportxml, YF_nobump, YF_clamprgb, yfpad1;
//	int GIdepth, GIcausdepth, GIpixelspersample;
//	int GIphotoncount, GImixphotons;
//	float GIphotonradius;
//	int YF_raydepth, YF_AApasses, YF_AAsamples, yfpad2;
//	float GIshadowquality, GIrefinement, GIpower, GIindirpower;
//	float YF_gamma, YF_exposure, YF_raybias, YF_AApixelsize, YF_AAthreshold;
//
//	/* paths to backbufffer, output, ftype */
//	char backbuf[160], pic[160];
//
//	/* stamps flags. */
//	int stamp;
//	short stamp_font_id, pad3; /* select one of blenders bitmap fonts */
//
//	/* stamp info user data. */
//	char stamp_udata[160];
//
//	/* foreground/background color. */
//	float fg_stamp[4];
//	float bg_stamp[4];
//
//	/* render simplify */
//	int simplify_subsurf;
//	int simplify_shadowsamples;
//	float simplify_particles;
//	float simplify_aosss;
//
//	/* cineon */
//	short cineonwhite, cineonblack;
//	float cineongamma;
//} RenderData;
//
///* control render convert and shading engine */
//typedef struct RenderProfile {
//	struct RenderProfile *next, *prev;
//	char name[32];
//
//	short particle_perc;
//	short subsurf_max;
//	short shadbufsample_max;
//	short pad1;
//
//	float ao_error, pad2;
//
//} RenderProfile;

public static final int DOME_FISHEYE=			1;
public static final int DOME_TRUNCATED_FRONT=	2;
public static final int DOME_TRUNCATED_REAR=		3;
public static final int DOME_ENVMAP=				4;
public static final int DOME_PANORAM_SPH=		5;
public static final int DOME_NUM_MODES=			6;

//typedef struct GameFraming {
//	float col[3];
//	char type, pad1, pad2, pad3;
//} GameFraming;

public static final int SCE_GAMEFRAMING_BARS=   0;
public static final int SCE_GAMEFRAMING_EXTEND= 1;
public static final int SCE_GAMEFRAMING_SCALE=  2;

public static final int STEREO_NOSTEREO=		1;
public static final int STEREO_ENABLED= 		2;
public static final int STEREO_DOME=	 		3;

//#define STEREO_NOSTEREO		 1
public static final int STEREO_QUADBUFFERED= 2;
public static final int STEREO_ABOVEBELOW=	 3;
public static final int STEREO_INTERLACED=	 4;
public static final int STEREO_ANAGLYPH=		5;
public static final int STEREO_SIDEBYSIDE=	6;
public static final int STEREO_VINTERLACE=	7;
//#define STEREO_DOME		8

/* physicsEngine */
public static final int WOPHY_NONE=		0;
public static final int WOPHY_ENJI=		1;
public static final int WOPHY_SUMO=		2;
public static final int WOPHY_DYNAMO=	3;
public static final int WOPHY_ODE=		4;
public static final int WOPHY_BULLET=	5;

/* GameData.flag */
public static final int GAME_ENABLE_ALL_FRAMES=				(1 << 1);
public static final int GAME_SHOW_DEBUG_PROPS=				(1 << 2);
public static final int GAME_SHOW_FRAMERATE=					(1 << 3);
public static final int GAME_SHOW_PHYSICS=					(1 << 4);
public static final int GAME_DISPLAY_LISTS=					(1 << 5);
public static final int GAME_GLSL_NO_LIGHTS=					(1 << 6);
public static final int GAME_GLSL_NO_SHADERS=				(1 << 7);
public static final int GAME_GLSL_NO_SHADOWS=				(1 << 8);
public static final int GAME_GLSL_NO_RAMPS=					(1 << 9);
public static final int GAME_GLSL_NO_NODES=					(1 << 10);
public static final int GAME_GLSL_NO_EXTRA_TEX=				(1 << 11);
public static final int GAME_IGNORE_DEPRECATION_WARNINGS=	(1 << 12);
public static final int GAME_ENABLE_ANIMATION_RECORD=		(1 << 13);

/* GameData.matmode */
public static final int GAME_MAT_TEXFACE=	0;
public static final int GAME_MAT_MULTITEX=	1;
public static final int GAME_MAT_GLSL=		2;

//typedef struct TimeMarker {
//	struct TimeMarker *next, *prev;
//	int frame;
//	char name[64];
//	unsigned int flag;
//} TimeMarker;
//
//typedef struct ImagePaintSettings {
//	struct Brush *brush;
//	short flag, tool;
//	int pad3;
//} ImagePaintSettings;
//
//typedef struct ParticleBrushData {
//	short size, strength;	/* common settings */
//	short step, invert;		/* for specific brushes only */
//} ParticleBrushData;
//
//typedef struct ParticleEditSettings {
//	short flag;
//	short totrekey;
//	short totaddkey;
//	short brushtype;
//
//	ParticleBrushData brush[7]; /* 7 = PE_TOT_BRUSH */
//
//	float emitterdist;
//	int draw_timed;
//} ParticleEditSettings;
//
//typedef struct TransformOrientation {
//	struct TransformOrientation *next, *prev;
//	char name[36];
//	float mat[3][3];
//} TransformOrientation;
//
//typedef struct ToolSettings {
//	/* Subdivide Settings */
//	short cornertype;
//	short editbutflag;
//	/*Triangle to Quad conversion threshold*/
//	float jointrilimit;
//	/* Extrude Tools */
//	float degr;
//	short step;
//	short turn;
//
//	float extr_offs;
//	float doublimit;
//
//	/* Primitive Settings */
//	/* UV Sphere */
//	short segments;
//	short rings;
//
//	/* Cylinder - Tube - Circle */
//	short vertices;
//
//	/* UV Calculation */
//	short unwrapper;
//	float uvcalc_radius;
//	float uvcalc_cubesize;
//	short uvcalc_mapdir;
//	short uvcalc_mapalign;
//	short uvcalc_flag;
//
//	/* Auto-IK */
//	short autoik_chainlen;
//
//	/* Image Paint (8 byttse aligned please!) */
//	struct ImagePaintSettings imapaint;
//
//	/* Particle Editing */
//	struct ParticleEditSettings particle;
//
//	/* Select Group Threshold */
//	float select_thresh;
//
//	/* IPO-Editor */
//	float clean_thresh;
//
//	/* Retopo */
//	char retopo_mode;
//	char retopo_paint_tool;
//	char line_div, ellipse_div, retopo_hotspot;
//
//	/* Multires */
//	char multires_subdiv_type;
//
//	/* Skeleton generation */
//	short skgen_resolution;
//	float skgen_threshold_internal;
//	float skgen_threshold_external;
//	float skgen_length_ratio;
//	float skgen_length_limit;
//	float skgen_angle_limit;
//	float skgen_correlation_limit;
//	float skgen_symmetry_limit;
//	short skgen_options;
//	char  skgen_postpro;
//	char  skgen_postpro_passes;
//	char  skgen_subdivisions[3];
//
//	/* Alt+RMB option */
//	char edge_mode;
//	char pad3[4];
//} ToolSettings;
//
///* Used by all brushes to store their properties, which can be directly set
//   by the interface code. Note that not all properties are actually used by
//   all the brushes. */
//typedef struct BrushData
//{
//	short size;
//	char strength, dir; /* Not used for smooth brush */
//	char view;
//	char flag;
//	char pad[2];
//} BrushData;
//
//struct SculptSession;
//typedef struct SculptData
//{
//	/* Note! all pointers in this struct must be duplicated header_info.c's copy_scene function */
//
//	/* Data stored only from entering sculptmode until exiting sculptmode */
//	struct SculptSession *session;
//
//	/* Pointers to all of sculptmodes's textures */
//	struct MTex *mtex[18];
//
//	/* Editable brush shape */
//	struct CurveMapping *cumap;
//
//	/* Settings for each brush */
//	BrushData drawbrush, smoothbrush, pinchbrush, inflatebrush, grabbrush, layerbrush, flattenbrush;
//
//	/* For rotating around a pivot point */
//	float pivot[3];
//
//	short brush_type;
//
//	/* For the Brush Shape */
//	short texact, texnr;
//	short spacing;
//	char texrept;
//	char texfade;
//	char texsep;
//
//	char averaging;
//	char flags;
//
//	/* Control tablet input */
//	char tablet_size, tablet_strength;
//
//	/* Symmetry is separate from the other BrushData because the same
//	   settings are always used for all brush types */
//	char symm;
//
//	/* Added to store if the 'Rake' setting has been set */
//	char rake;
//	char axislock;
//	char pad[2];
//} SculptData;
//
//typedef struct Scene {
//	ID id;
//	struct Object *camera;
//	struct World *world;
//
//	struct Scene *set;
//	struct Image *ima;
//
//	ListBase base;
//	struct Base *basact;
//
//	float cursor[3];
//	float twcent[3];			/* center for transform widget */
//	float twmin[3], twmax[3];	/* boundbox of selection for transform widget */
//	unsigned int lay;
//
//	/* editmode stuff */
//	float editbutsize;                      /* size of normals */
//	short selectmode;						/* for mesh only! */
//	short proportional, prop_mode;
//	short automerge, pad5, pad6;
//
//	short autokey_mode; 					/* mode for autokeying (defines in DNA_userdef_types.h */
//
//	short use_nodes;
//
//	struct bNodeTree *nodetree;
//
//	void *ed;								/* sequence editor data is allocated here */
//	struct Radio *radio;
//
//	struct GameFraming framing;
//
//	struct ToolSettings *toolsettings;
//
//	/* migrate or replace? depends on some internal things... */
//	/* no, is on the right place (ton) */
//	struct RenderData r;
//	struct AudioData audio;
//
//	ScriptLink scriptlink;
//
//	ListBase markers;
//	ListBase transform_spaces;
//
//	short jumpframe;
//	short snap_mode, snap_flag, snap_target;
//
//	/* none of the dependancy graph  vars is mean to be saved */
//	struct  DagForest *theDag;
//	short dagisvalid, dagflags;
//	short pad4, recalc;				/* recalc = counterpart of ob->recalc */
//
//	/* Sculptmode data */
//	struct SculptData sculptdata;
//
//	/* frame step. */
//	int frame_step;
//	int pad;
//} Scene;


/* **************** RENDERDATA ********************* */

/* bufflag */
public static final int R_BACKBUF=		1;
public static final int R_BACKBUFANIM=	2;
public static final int R_FRONTBUF=		4;
public static final int R_FRONTBUFANIM=	8;

/* flag */
/* use preview range */
public static final int SCER_PRV_RANGE=	(1<<0);

/* mode (int now) */
public static final int R_OSA=			0x0001;
public static final int R_SHADOW=		0x0002;
public static final int R_GAMMA=		0x0004;
public static final int R_ORTHO=		0x0008;
public static final int R_ENVMAP=		0x0010;
public static final int R_EDGE=			0x0020;
public static final int R_FIELDS=		0x0040;
public static final int R_FIELDSTILL=	0x0080;
public static final int R_RADIO=		0x0100;
public static final int R_BORDER=		0x0200;
public static final int R_PANORAMA=		0x0400;
public static final int R_CROP=			0x0800;
public static final int R_COSMO=		0x1000;
public static final int R_ODDFIELD=		0x2000;
public static final int R_MBLUR=		0x4000;
		/* unified was here */
public static final int R_RAYTRACE=     0x10000;
		/* R_GAUSS is obsolete, but used to retrieve setting from old files */
public static final int R_GAUSS=      	0x20000;
		/* fbuf obsolete... */
public static final int R_FBUF=			0x40000;
		/* threads obsolete... is there for old files, now use for autodetect threads */
public static final int R_THREADS=		0x80000;
		/* Use the same flag for autothreads */
public static final int R_FIXED_THREADS=0x80000;

public static final int R_SPEED=		0x100000;
public static final int R_SSS=			0x200000;
public static final int R_NO_OVERWRITE=	0x400000; /* skip existing files */
public static final int R_TOUCH=		0x800000; /* touch files before rendering */
public static final int R_SIMPLIFY=		0x1000000;

/* seq_flag */
public static final int R_SEQ_GL_PREV= 1;
public static final int R_SEQ_GL_REND= 2;

/* displaymode */

public static final int R_OUTPUT_SCREEN=	0;
public static final int R_OUTPUT_AREA=	1;
public static final int R_OUTPUT_WINDOW=	2;
public static final int R_OUTPUT_FORKED=	3;

/* filtertype */
public static final int R_FILTER_BOX=	0;
public static final int R_FILTER_TENT=	1;
public static final int R_FILTER_QUAD=	2;
public static final int R_FILTER_CUBIC=	3;
public static final int R_FILTER_CATROM=4;
public static final int R_FILTER_GAUSS=	5;
public static final int R_FILTER_MITCH=	6;
public static final int R_FILTER_FAST_GAUSS=7; /* note, this is only used for nodes at the moment */

/* yafray: renderer flag (not only exclusive to yafray) */
public static final int R_INTERN=	0;
public static final int R_YAFRAY=	1;

/* raytrace structure */
public static final int R_RAYSTRUCTURE_AUTO=				0;
public static final int R_RAYSTRUCTURE_OCTREE=			1;
public static final int R_RAYSTRUCTURE_BLIBVH=			2;
public static final int R_RAYSTRUCTURE_VBVH=				3;
public static final int R_RAYSTRUCTURE_SIMD_SVBVH=		4;	/* needs SIMD */
public static final int R_RAYSTRUCTURE_SIMD_QBVH=		5;	/* needs SIMD */

/* raytrace_options */
public static final int R_RAYTRACE_USE_LOCAL_COORDS=		0x0001;
public static final int R_RAYTRACE_USE_INSTANCES=		0x0002;

/* scemode (int now) */
public static final int R_DOSEQ=			0x0001;
public static final int R_BG_RENDER=		0x0002;
		/* passepartout is camera option now, keep this for backward compatibility */
public static final int R_PASSEPARTOUT=		0x0004;
public static final int R_PREVIEWBUTS=		0x0008;
public static final int R_EXTENSION=		0x0010;
public static final int R_NODE_PREVIEW=		0x0020;
public static final int R_DOCOMP=			0x0040;
public static final int R_COMP_CROP=		0x0080;
public static final int R_FREE_IMAGE=		0x0100;
public static final int R_SINGLE_LAYER=		0x0200;
public static final int R_EXR_TILE_FILE=	0x0400;
public static final int R_COMP_FREE=		0x0800;
public static final int R_NO_IMAGE_LOAD=	0x1000;
public static final int R_NO_TEX=			0x2000;
public static final int R_STAMP_INFO=		0x4000;
public static final int R_FULL_SAMPLE=		0x8000;
public static final int R_COMP_RERENDER=	0x10000;

/* r->stamp */
public static final int R_STAMP_TIME= 	0x0001;
public static final int R_STAMP_FRAME=	0x0002;
public static final int R_STAMP_DATE=	0x0004;
public static final int R_STAMP_CAMERA=	0x0008;
public static final int R_STAMP_SCENE=	0x0010;
public static final int R_STAMP_NOTE=	0x0020;
public static final int R_STAMP_DRAW=	0x0040; /* draw in the image */
public static final int R_STAMP_MARKER=	0x0080;
public static final int R_STAMP_FILENAME=0x0100;
public static final int R_STAMP_SEQSTRIP=0x0200;
public static final int R_STAMP_RENDERTIME=	0x0400;
public static final int R_STAMP_ALL=		(R_STAMP_TIME|R_STAMP_FRAME|R_STAMP_DATE|R_STAMP_CAMERA|R_STAMP_SCENE|R_STAMP_NOTE|R_STAMP_MARKER|R_STAMP_FILENAME|R_STAMP_SEQSTRIP|R_STAMP_RENDERTIME);

/* alphamode */
public static final int R_ADDSKY=		0;
public static final int R_ALPHAPREMUL=	1;
public static final int R_ALPHAKEY=		2;

/* planes */
public static final int R_PLANES24=		24;
public static final int R_PLANES32=		32;
public static final int R_PLANESBW=		8;

/* color_mgt_flag */
public static final int R_COLOR_MANAGEMENT=	1;

/* imtype */
public static final int R_TARGA=	0;
public static final int R_IRIS=		1;
public static final int R_HAMX=		2;
public static final int R_FTYPE=	3; /* ftype is nomore */
public static final int R_JPEG90=	4;
public static final int R_MOVIE=	5;
public static final int R_IRIZ=		7;
public static final int R_RAWTGA=	14;
public static final int R_AVIRAW=	15;
public static final int R_AVIJPEG=	16;
public static final int R_PNG=		17;
public static final int R_AVICODEC=	18;
public static final int R_QUICKTIME=19;
public static final int R_BMP=		20;
public static final int R_RADHDR=	21;
public static final int R_TIFF=		22;
public static final int R_OPENEXR=	23;
public static final int R_FFMPEG=       24;
public static final int R_FRAMESERVER=  25;
public static final int R_CINEON=		26;
public static final int R_DPX=			27;
public static final int R_MULTILAYER=	28;
public static final int R_DDS=			29;

/* subimtype, flag options for imtype */
public static final int R_OPENEXR_HALF=	1;
public static final int R_OPENEXR_ZBUF=	2;
public static final int R_PREVIEW_JPG=	4;
public static final int R_CINEON_LOG= 	8;
public static final int R_TIFF_16BIT=	16;

/* bake_mode: same as RE_BAKE_xxx defines */
/* bake_flag: */
public static final int R_BAKE_CLEAR=		1;
public static final int R_BAKE_OSA=			2;
public static final int R_BAKE_TO_ACTIVE=	4;
public static final int R_BAKE_NORMALIZE=	8;

/* bake_normal_space */
public static final int R_BAKE_SPACE_CAMERA=    0;
public static final int R_BAKE_SPACE_WORLD=     1;
public static final int R_BAKE_SPACE_OBJECT=    2;
public static final int R_BAKE_SPACE_TANGENT=   3;

/* simplify_flag */
public static final int R_SIMPLE_NO_TRIANGULATE=		1;

/* **************** SCENE ********************* */

/* for general use */
public static final int MAXFRAME=	300000;
public static final float MAXFRAMEF=	300000.0f;

public static final int MINFRAME=	1;
public static final float MINFRAMEF=	1.0f;

/* (minimum frame number for current-frame) */
public static final int MINAFRAME=	-300000;
public static final float MINAFRAMEF=	-300000.0f;

//#define ID_NEW(a)		if( (a) && (a)->id.newid ) (a)= (void *)(a)->id.newid
//#define ID_NEW_US(a)	if( (a)->id.newid) {(a)= (void *)(a)->id.newid; (a)->id.us++;}
//#define ID_NEW_US2(a)	if( ((ID *)a)->newid) {(a)= ((ID *)a)->newid; ((ID *)a)->us++;}
//#define	CFRA			(scene->r.cfra)
//#define SUBFRA			(scene->r.subframe)
//#define	F_CFRA			((float)(scene->r.cfra))
//#define	SFRA			(scene->r.sfra)
//#define	EFRA			(scene->r.efra)
public static final int PRVRANGEON(Scene scene)		{return scene.r.flag & SceneTypes.SCER_PRV_RANGE;}
public static final int PSFRA(Scene scene)			{return (PRVRANGEON(scene)!=0)? (scene.r.psfra): (scene.r.sfra);}
public static final int PEFRA(Scene scene)			{return (PRVRANGEON(scene)!=0)? (scene.r.pefra): (scene.r.efra);}
//#define FRA2TIME(a)           ((((double) scene->r.frs_sec_base) * (a)) / scene->r.frs_sec)
//#define TIME2FRA(a)           ((((double) scene->r.frs_sec) * (a)) / scene->r.frs_sec_base)
//#define FPS                     (((double) scene->r.frs_sec) / scene->r.frs_sec_base)

public static final int RAD_PHASE_PATCHES=	1;
public static final int RAD_PHASE_FACES=	2;

/* base->flag is in DNA_object_types.h */

/* scene->snap_flag */
public static final int SCE_SNAP=				1;
public static final int SCE_SNAP_ROTATE=		2;
public static final int SCE_SNAP_PEEL_OBJECT=	4;
public static final int SCE_SNAP_PROJECT=		8;
/* scene->snap_target */
public static final int SCE_SNAP_TARGET_CLOSEST=0;
public static final int SCE_SNAP_TARGET_CENTER=	1;
public static final int SCE_SNAP_TARGET_MEDIAN=	2;
public static final int SCE_SNAP_TARGET_ACTIVE=	3;
/* scene->snap_mode */
//public static final int SCE_SNAP_MODE_VERTEX=	0;
//public static final int SCE_SNAP_MODE_EDGE=		1;
//public static final int SCE_SNAP_MODE_FACE=		2;
public static final int SCE_SNAP_MODE_INCREMENT=	0;
public static final int SCE_SNAP_MODE_VERTEX=	1;
public static final int SCE_SNAP_MODE_EDGE=		2;
public static final int SCE_SNAP_MODE_FACE=		3;
public static final int SCE_SNAP_MODE_VOLUME=	4;

/* sce->selectmode */
public static final int SCE_SELECT_VERTEX=	1; /* for mesh */
public static final int SCE_SELECT_EDGE=	2;
public static final int SCE_SELECT_FACE=	4;

/* sce->selectmode for particles */
public static final int SCE_SELECT_PATH=	1;
public static final int SCE_SELECT_POINT=	2;
public static final int SCE_SELECT_END=		4;

/* sce->recalc (now in use by previewrender) */
public static final int SCE_PRV_CHANGED=	1;

/* sce->prop_mode (proportional falloff) */
public static final int PROP_SMOOTH=        0;
public static final int PROP_SPHERE=        1;
public static final int PROP_ROOT=          2;
public static final int PROP_SHARP=         3;
public static final int PROP_LIN=           4;
public static final int PROP_CONST=         5;
public static final int PROP_RANDOM=		6;

/* toolsettings->proportional */
public static final int PROP_EDIT_OFF=			0;
public static final int PROP_EDIT_ON=			1;
public static final int PROP_EDIT_CONNECTED=	2;

/* sce->flag */
public static final int SCE_DS_SELECTED=			(1<<0);
public static final int SCE_DS_COLLAPSED=		(1<<1);
public static final int SCE_NLA_EDIT_ON=			(1<<2);
public static final int SCE_FRAME_DROP=			(1<<3);

	/* return flag next_object function */
public static final int F_ERROR=		-1;
public static final int F_START=		0;
public static final int F_SCENE=		1;
//public static final int F_SET=			2;
public static final int F_DUPLI=		3;

/* audio->flag */
public static final int AUDIO_MUTE=		1;
public static final int AUDIO_SYNC=		2;
public static final int AUDIO_SCRUB=	4;

public static final int FFMPEG_MULTIPLEX_AUDIO=  1;
public static final int FFMPEG_AUTOSPLIT_OUTPUT= 2;

///* Sculpt brush flags */
//public static final int SCULPT_BRUSH_AIRBRUSH= 1;
//public static final int SCULPT_BRUSH_ANCHORED= 2;
///* SculptData.flags */
//public static final int SCULPT_INPUT_SMOOTH= 1;
//public static final int SCULPT_DRAW_FAST=    2;
//public static final int SCULPT_DRAW_BRUSH=   4;
///* SculptData.brushtype */
//public static final int DRAW_BRUSH=    1;
//public static final int SMOOTH_BRUSH=  2;
//public static final int PINCH_BRUSH=   3;
//public static final int INFLATE_BRUSH= 4;
//public static final int GRAB_BRUSH=    5;
//public static final int LAYER_BRUSH=   6;
//public static final int FLATTEN_BRUSH= 7;
///* SculptData.texrept */
//public static final int SCULPTREPT_DRAG= 1;
//public static final int SCULPTREPT_TILE= 2;
//public static final int SCULPTREPT_3D=   3;
//
//public static final int SYMM_X= 1;
//public static final int SYMM_Y= 2;
//public static final int SYMM_Z= 4;
//
//public static final int AXISLOCK_X= 1;
//public static final int AXISLOCK_Y= 2;
//public static final int AXISLOCK_Z= 4;
//
///* toolsettings->imagepaint_flag */
//public static final int IMAGEPAINT_DRAWING=				1;
//public static final int IMAGEPAINT_DRAW_TOOL=			2;
//public static final int IMAGEPAINT_DRAW_TOOL_DRAWING=	4;

/* sculpt_paint_settings */
public static final int SCULPT_PAINT_USE_UNIFIED_SIZE=        (1<<0);
public static final int SCULPT_PAINT_USE_UNIFIED_ALPHA=       (1<<1);
public static final int SCULPT_PAINT_UNIFIED_LOCK_BRUSH_SIZE= (1<<2);
public static final int SCULPT_PAINT_UNIFIED_SIZE_PRESSURE=   (1<<3);
public static final int SCULPT_PAINT_UNIFIED_ALPHA_PRESSURE=  (1<<4);

/* ImagePaintSettings.flag */
public static final int IMAGEPAINT_DRAWING=				1;
// #define IMAGEPAINT_DRAW_TOOL			2 // deprecated
// #define IMAGEPAINT_DRAW_TOOL_DRAWING	4 // deprecated

/* projection painting only */
public static final int IMAGEPAINT_PROJECT_DISABLE=		8;	/* Non projection 3D painting */
public static final int IMAGEPAINT_PROJECT_XRAY=			16;
public static final int IMAGEPAINT_PROJECT_BACKFACE=		32;
public static final int IMAGEPAINT_PROJECT_FLAT=			64;
public static final int IMAGEPAINT_PROJECT_LAYER_CLONE=	128;
public static final int IMAGEPAINT_PROJECT_LAYER_STENCIL=	256;
public static final int IMAGEPAINT_PROJECT_LAYER_STENCIL_INV=	512;

/* toolsettings->uvcalc_flag */
public static final int UVCALC_FILLHOLES=			1;
public static final int UVCALC_NO_ASPECT_CORRECT=	2;	/* would call this UVCALC_ASPECT_CORRECT, except it should be default with old file */
public static final int UVCALC_TRANSFORM_CORRECT=	4;	/* adjust UV's while transforming to avoid distortion */

/* toolsettings->uv_flag */
public static final int UV_SYNC_SELECTION=	1;
public static final int UV_SHOW_SAME_IMAGE=	2;

/* toolsettings->uv_selectmode */
public static final int UV_SELECT_VERTEX=	1;
public static final int UV_SELECT_EDGE=		2;
public static final int UV_SELECT_FACE=		4;
public static final int UV_SELECT_ISLAND=	8;

/* toolsettings->edge_mode */
public static final int EDGE_MODE_SELECT=				0;
public static final int EDGE_MODE_TAG_SEAM=				1;
public static final int EDGE_MODE_TAG_SHARP=			2;
public static final int EDGE_MODE_TAG_CREASE=			3;
public static final int EDGE_MODE_TAG_BEVEL=			4;

/* toolsettings->particle flag */
public static final int PE_KEEP_LENGTHS=		1;
public static final int PE_LOCK_FIRST=			2;
public static final int PE_DEFLECT_EMITTER=		4;
public static final int PE_INTERPOLATE_ADDED=	8;
public static final int PE_SHOW_CHILD=			16;
public static final int PE_SHOW_TIME=			32;
public static final int PE_X_MIRROR=			64;

/* toolsetting->particle brushtype */
public static final int PE_BRUSH_NONE=		-1;
public static final int PE_BRUSH_COMB=		0;
public static final int PE_BRUSH_CUT=		1;
public static final int PE_BRUSH_LENGTH=	2;
public static final int PE_BRUSH_PUFF=		3;
public static final int PE_BRUSH_ADD=		4;
public static final int PE_BRUSH_WEIGHT=	5;
public static final int PE_BRUSH_SMOOTH=	6;

/* this must equal ParticleEditSettings.brush array size */
public static final int PE_TOT_BRUSH=		7;

/* toolsettings->retopo_mode */
public static final int RETOPO= 1;
public static final int RETOPO_PAINT= 2;

/* toolsettings->retopo_paint_tool */
public static final int RETOPO_PEN= 1;
public static final int RETOPO_LINE= 2;
public static final int RETOPO_ELLIPSE= 4;

/* toolsettings->skgen_options */
public static final int SKGEN_FILTER_INTERNAL=	1;
public static final int SKGEN_FILTER_EXTERNAL=	2;
public static final int	SKGEN_SYMMETRY=			4;
public static final int	SKGEN_CUT_LENGTH=		8;
public static final int	SKGEN_CUT_ANGLE=		16;
public static final int	SKGEN_CUT_CORRELATION=	32;

public static final int	SKGEN_SUB_LENGTH=		0;
public static final int	SKGEN_SUB_ANGLE=		1;
public static final int	SKGEN_SUB_CORRELATION=	2;
public static final int	SKGEN_SUB_TOTAL=		3;

/* toolsettings->skgen_postpro */
public static final int SKGEN_SMOOTH=			0;
public static final int SKGEN_AVERAGE=			1;
public static final int SKGEN_SHARPEN=			2;

/* toolsettings->bone_sketching */
public static final int BONE_SKETCHING=			1;
public static final int BONE_SKETCHING_QUICK=	2;
public static final int BONE_SKETCHING_ADJUST=	4;

/* toolsettings->bone_sketching_convert */
public static final int	SK_CONVERT_CUT_FIXED=			0;
public static final int	SK_CONVERT_CUT_LENGTH=			1;
public static final int	SK_CONVERT_CUT_ADAPTATIVE=		2;
public static final int	SK_CONVERT_RETARGET=				3;

/* toolsettings->skgen_retarget_options */
public static final int	SK_RETARGET_AUTONAME=			1;

/* toolsettings->skgen_retarget_roll */
public static final int	SK_RETARGET_ROLL_NONE=			0;
public static final int	SK_RETARGET_ROLL_VIEW=			1;
public static final int	SK_RETARGET_ROLL_JOINT=			2;

/* physics_settings->flag */
public static final int PHYS_GLOBAL_GRAVITY=		1;

/* UnitSettings */

/* UnitSettings->system */
public static final int	USER_UNIT_NONE=			0;
public static final int	USER_UNIT_METRIC=		1;
public static final int	USER_UNIT_IMPERIAL=		2;
/* UnitSettings->flag */
public static final int	USER_UNIT_OPT_SPLIT=		1;
public static final int USER_UNIT_ROT_RADIANS=	2;

//#ifdef __cplusplus
//}
//#endif
//
//#endif
}
