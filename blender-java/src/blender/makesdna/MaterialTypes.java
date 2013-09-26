/**
 * blenlib/DNA_material_types.h (mar-2001 nzc)
 *
 * $Id: DNA_material_types.java,v 1.1.1.1 2009/07/11 21:54:58 jladere Exp $ 
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

//#ifndef DNA_MATERIAL_TYPES_H
//public static final int DNA_MATERIAL_TYPES_H
//
//#include "DNA_ID.h"
//#include "DNA_scriptlink_types.h"
//#include "DNA_listBase.h"
//
//#ifndef MAX_MTEX
//#define MAX_MTEX	18
//#endif
//
//struct MTex;
//struct Ipo;
//struct Material;
//struct ColorBand;
//struct Group;
//struct bNodeTree;
//
public class MaterialTypes {
///* WATCH IT: change type? also make changes in ipo.h  */
//
//typedef struct Material {
//	ID id;
//
//	short colormodel, flag;
//	/* note, keep this below synced with render_types.h */
//	float r, g, b;
//	float specr, specg, specb;
//	float mirr, mirg, mirb;
//	float ambr, ambb, ambg;
//	float amb, emit, ang, spectra, ray_mirror;
//	float alpha, ref, spec, zoffs, add;
//	float translucency;
//	/* end synced with render_types.h */
//
//	float fresnel_mir, fresnel_mir_i;
//	float fresnel_tra, fresnel_tra_i;
//	float filter;		/* filter added, for raytrace transparency and transmissivity */
//	float tx_limit, tx_falloff;
//	short ray_depth, ray_depth_tra;
//	short har;
//	char seed1, seed2;
//
//	float gloss_mir, gloss_tra;
//	short samp_gloss_mir, samp_gloss_tra;
//	float adapt_thresh_mir, adapt_thresh_tra;
//	float aniso_gloss_mir;
//	float dist_mir;
//	short fadeto_mir;
//	short shade_flag;		/* like Cubic interpolation */
//
//	int mode, mode_l;		/* mode_l is the or-ed result of all layer modes */
//	short flarec, starc, linec, ringc;
//	float hasize, flaresize, subsize, flareboost;
//	float strand_sta, strand_end, strand_ease, strand_surfnor;
//	float strand_min, strand_widthfade;
//	char strand_uvname[32];
//
//	float sbias;			/* shadow bias to prevent terminator prob */
//	float lbias;			/* factor to multiply lampbias with (0.0 = no mult) */
//	float shad_alpha;		/* in use for irregular shadowbuffer */
//	int	septex;
//
//	/* for buttons and render*/
//	char rgbsel, texact, pr_type, use_nodes;
//	short pr_back, pr_lamp, pad4, ml_flag;	/* ml_flag is for disable base material */
//
//	/* shaders */
//	short diff_shader, spec_shader;
//	float roughness, refrac;
//	float param[4];		/* size, smooth, size, smooth, for toonshader */
//	float rms;
//	float darkness;
//	short texco, mapto;
//
//	/* ramp colors */
//	struct ColorBand *ramp_col;
//	struct ColorBand *ramp_spec;
//	char rampin_col, rampin_spec;
//	char rampblend_col, rampblend_spec;
//	short ramp_show, pad3;
//	float rampfac_col, rampfac_spec;
//
//	struct MTex *mtex[18];		/* MAX_MTEX */
//	struct bNodeTree *nodetree;
//	struct Ipo *ipo;
//	struct Group *group;	/* light group */
//	struct PreviewImage * preview;
//
//	/* dynamic properties */
//	float friction, fh, reflect;
//	float fhdist, xyfrict;
//	short dynamode, pad2;
//
//	/* subsurface scattering */
//	float sss_radius[3], sss_col[3];
//	float sss_error, sss_scale, sss_ior;
//	float sss_colfac, sss_texfac;
//	float sss_front, sss_back;
//	short sss_flag, sss_preset;
//
//	/* yafray: absorption color, dispersion parameters and material preset menu */
//	float YF_ar, YF_ag, YF_ab, YF_dscale, YF_dpwr;
//	int YF_dsmp, YF_preset, YF_djit;
//
//	ScriptLink scriptlink;
//
//	ListBase gpumaterial;		/* runtime */
//} Material;

/* **************** MATERIAL ********************* */

	/* maximum number of materials per material array
	 * (on object, mesh, lamp, etc.)
	 */
public static final int MAXMAT=			16;

/* colormodel */
public static final int MA_RGB=			0;
public static final int MA_CMYK=			1;
public static final int MA_YUV=			2;
public static final int MA_HSV=			3;

/* flag */
		/* for render */
public static final int MA_IS_USED=		1;

/* mode (is int) */
public static final int MA_TRACEBLE=		1;
public static final int MA_SHADOW=		2;
public static final int MA_SHLESS=		4;
public static final int MA_WIRE=			8;
public static final int MA_VERTEXCOL=	16;
public static final int MA_HALO_SOFT=	16;
public static final int MA_HALO=			32;
public static final int MA_ZTRA=			64;
public static final int MA_VERTEXCOLP=	128;
public static final int MA_ZINV=			256;
public static final int MA_HALO_RINGS=	256;
public static final int MA_ENV=			512;
public static final int MA_HALO_LINES=	512;
public static final int MA_ONLYSHADOW=	1024;
public static final int MA_HALO_XALPHA=	1024;
public static final int MA_STAR=			0x800;
public static final int MA_FACETEXTURE=	0x800;
public static final int MA_HALOTEX=		0x1000;
public static final int MA_HALOPUNO=		0x2000;
public static final int MA_ONLYCAST=		0x2000;
public static final int MA_NOMIST=		0x4000;
public static final int MA_HALO_SHADE=	0x4000;
public static final int MA_HALO_FLARE=	0x8000;
public static final int MA_RADIO=		0x10000;
public static final int MA_RAYTRANSP=	0x20000;
public static final int MA_RAYMIRROR=	0x40000;
public static final int MA_SHADOW_TRA=	0x80000;
public static final int MA_RAMP_COL=		0x100000;
public static final int MA_RAMP_SPEC=	0x200000;
public static final int MA_RAYBIAS=		0x400000;
public static final int MA_FULL_OSA=		0x800000;
public static final int MA_TANGENT_STR=	0x1000000;
public static final int MA_SHADBUF=		0x2000000;
		/* note; we drop MA_TANGENT_STR later to become tangent_u */
public static final int MA_TANGENT_V=	0x4000000;
/* qdn: a bit clumsy this, tangents needed for normal maps separated from shading */
public static final int MA_NORMAP_TANG=	0x8000000;
public static final int MA_GROUP_NOLAY=	0x10000000;
public static final int MA_FACETEXTURE_ALPHA=	0x20000000;
public static final int MA_STR_B_UNITS=	0x40000000;
public static final int MA_STR_SURFDIFF= 0x80000000;

public static final int	MA_MODE_MASK=	0x6fffffff;	/* all valid mode bits */

/* ray mirror fadeout */
public static final int MA_RAYMIR_FADETOSKY=	0;
public static final int MA_RAYMIR_FADETOMAT=	1;

/* shade_flag */
public static final int MA_CUBIC=			1;
public static final int MA_OBCOLOR=			2;

/* diff_shader */
public static final int MA_DIFF_LAMBERT=		0;
public static final int MA_DIFF_ORENNAYAR=	1;
public static final int MA_DIFF_TOON=		2;
public static final int MA_DIFF_MINNAERT=    3;
public static final int MA_DIFF_FRESNEL=     4;

/* spec_shader */
public static final int MA_SPEC_COOKTORR=	0;
public static final int MA_SPEC_PHONG=		1;
public static final int MA_SPEC_BLINN=		2;
public static final int MA_SPEC_TOON=		3;
public static final int MA_SPEC_WARDISO=		4;

/* dynamode */
public static final int MA_DRAW_DYNABUTS=    1;
public static final int MA_FH_NOR=	        2;

/* ramps */
public static final int MA_RAMP_IN_SHADER=	0;
public static final int MA_RAMP_IN_ENERGY=	1;
public static final int MA_RAMP_IN_NOR=		2;
public static final int MA_RAMP_IN_RESULT=	3;

public static final int MA_RAMP_BLEND=		0;
public static final int MA_RAMP_ADD=			1;
public static final int MA_RAMP_MULT=		2;
public static final int MA_RAMP_SUB=			3;
public static final int MA_RAMP_SCREEN=		4;
public static final int MA_RAMP_DIV=			5;
public static final int MA_RAMP_DIFF=		6;
public static final int MA_RAMP_DARK=		7;
public static final int MA_RAMP_LIGHT=		8;
public static final int MA_RAMP_OVERLAY=		9;
public static final int MA_RAMP_DODGE=		10;
public static final int MA_RAMP_BURN=		11;
public static final int MA_RAMP_HUE=			12;
public static final int MA_RAMP_SAT=			13;
public static final int MA_RAMP_VAL=			14;
public static final int MA_RAMP_COLOR=		15;

/* texco */
public static final int TEXCO_ORCO=		1;
public static final int TEXCO_REFL=		2;
public static final int TEXCO_NORM=		4;
public static final int TEXCO_GLOB=		8;
public static final int TEXCO_UV=		16;
public static final int TEXCO_OBJECT=	32;
public static final int TEXCO_LAVECTOR=	64;
public static final int TEXCO_VIEW=		128;
public static final int TEXCO_STICKY=	256;
public static final int TEXCO_OSA=		512;
public static final int TEXCO_WINDOW=	1024;
public static final int NEED_UV=			2048;
public static final int TEXCO_TANGENT=	4096;
	/* still stored in vertex->accum, 1 D */
public static final int TEXCO_STRAND=	8192;
public static final int TEXCO_STRESS=	16384;
public static final int TEXCO_SPEED=		32768;

/* mapto */
public static final int MAP_COL=			1;
public static final int MAP_NORM=		2;
public static final int MAP_COLSPEC=		4;
public static final int MAP_COLMIR=		8;
public static final int MAP_VARS=		(0xFFF0);
public static final int MAP_REF=			16;
public static final int MAP_SPEC=		32;
public static final int MAP_EMIT=		64;
public static final int MAP_ALPHA=		128;
public static final int MAP_HAR=			256;
public static final int MAP_RAYMIRR=		512;
public static final int MAP_TRANSLU=		1024;
public static final int MAP_AMB=			2048;
public static final int MAP_DISPLACE=	4096;
public static final int MAP_WARP=		8192;
public static final int MAP_LAYER=		16384;

/* mapto for halo */
//public static final int MAP_HA_COL		1
//public static final int MAP_HA_ALPHA	128
//public static final int MAP_HA_HAR		256
//public static final int MAP_HA_SIZE		2
//public static final int MAP_HA_ADD		64

/* pmapto */
/* init */
public static final int MAP_PA_INIT=		31;
public static final int MAP_PA_TIME=		1;
public static final int MAP_PA_LIFE=		2;
public static final int MAP_PA_DENS=		4;
public static final int MAP_PA_SIZE=		8;
public static final int MAP_PA_LENGTH=	16;
/* reset */
public static final int MAP_PA_IVEL=		32;
/* physics */
public static final int MAP_PA_PVEL=		64;
/* path cache */
public static final int MAP_PA_CACHE=	912;
public static final int MAP_PA_CLUMP=	128;
public static final int MAP_PA_KINK=		256;
public static final int MAP_PA_ROUGH=	512;

/* pr_type */
public static final int MA_FLAT=			0;
public static final int MA_SPHERE=		1;
public static final int MA_CUBE=			2;
public static final int MA_MONKEY=		3;
public static final int MA_SPHERE_A=		4;
public static final int MA_TEXTURE=		5;
public static final int MA_LAMP=			6;
public static final int MA_SKY=			7;
public static final int MA_HAIR=			10;
public static final int MA_ATMOS=		11;

/* pr_back */
public static final int MA_DARK=			1;

/* sss_flag */
public static final int MA_DIFF_SSS=		1;

//#endif
}
