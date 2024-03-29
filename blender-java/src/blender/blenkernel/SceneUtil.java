/*  scene.c
 *  
 * 
 * $Id: SceneUtil.java,v 1.2 2009/09/18 05:20:13 jladere Exp $
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

import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.Scene;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.DNA_ID;
import blender.makesdna.ObjectTypes;
import blender.makesdna.sdna.Group;
import blender.makesdna.sdna.GroupObject;
import blender.makesdna.sdna.bObject;
import static blender.blenkernel.Blender.G;

public class SceneUtil {

public static final double FPS(Scene scene) {
    return (((double) scene.r.frs_sec) / scene.r.frs_sec_base);
}

//void free_avicodecdata(AviCodecData *acd)
//{
//	if (acd) {
//		if (acd.lpFormat){
//			MEM_freeN(acd.lpFormat);
//			acd.lpFormat = NULL;
//			acd.cbFormat = 0;
//		}
//		if (acd.lpParms){
//			MEM_freeN(acd.lpParms);
//			acd.lpParms = NULL;
//			acd.cbParms = 0;
//		}
//	}
//}
//
//void free_qtcodecdata(QuicktimeCodecData *qcd)
//{
//	if (qcd) {
//		if (qcd.cdParms){
//			MEM_freeN(qcd.cdParms);
//			qcd.cdParms = NULL;
//			qcd.cdSize = 0;
//		}
//	}
//}

    /* copy_scene moved to src/header_info.c... should be back */

    /* do not free scene itself */
    public static void free_scene(Scene sce) {
        Base base;

        base = (Base) sce.base.first;
        while (base != null) {
            base.object.id.us--;
            base = base.next;
        }
        /* do not free objects! */

        ListBaseUtil.BLI_freelistN(sce.base);
//	seq_free_editing(sce.ed);

//	BKE_free_animdata((ID *)sce);
//	BKE_keyingsets_free(&sce.keyingsets);

//	if (sce.r.avicodecdata) {
//		free_avicodecdata(sce.r.avicodecdata);
//		MEM_freeN(sce.r.avicodecdata);
//		sce.r.avicodecdata = NULL;
//	}
//	if (sce.r.qtcodecdata) {
//		free_qtcodecdata(sce.r.qtcodecdata);
//		MEM_freeN(sce.r.qtcodecdata);
//		sce.r.qtcodecdata = NULL;
//	}
//	if (sce.r.ffcodecdata.properties) {
//		IDP_FreeProperty(sce.r.ffcodecdata.properties);
//		MEM_freeN(sce.r.ffcodecdata.properties);
//		sce.r.ffcodecdata.properties = NULL;
//	}

        ListBaseUtil.BLI_freelistN(sce.markers);
        ListBaseUtil.BLI_freelistN(sce.transform_spaces);
        ListBaseUtil.BLI_freelistN(sce.r.layers);

        if (sce.toolsettings != null) {
//		if(sce.toolsettings.vpaint)
//			MEM_freeN(sce.toolsettings.vpaint);
//		if(sce.toolsettings.wpaint)
//			MEM_freeN(sce.toolsettings.wpaint);
//		if(sce.toolsettings.sculpt) {
//			sculptsession_free(sce.toolsettings.sculpt);
//			MEM_freeN(sce.toolsettings.sculpt);
//		}
//
//		MEM_freeN(sce.toolsettings);
            sce.toolsettings = null;
        }

//	if (sce.theDag) {
//		free_forest(sce.theDag);
//		MEM_freeN(sce.theDag);
//	}
//
//	if(sce.nodetree) {
//		ntreeFreeTree(sce.nodetree);
//		MEM_freeN(sce.nodetree);
//	}
    }

public static Scene add_scene(String name)
{
	Scene sce;
//	ParticleEditSettings *pset;
//	int a;

	sce= (Scene)LibraryUtil.alloc_libblock(G.main.scene, DNA_ID.ID_SCE, StringUtil.toCString(name),0);
//	sce.lay= 1;
//
//	sce.r.mode= R_GAMMA|R_OSA|R_SHADOW|R_SSS|R_ENVMAP|R_RAYTRACE;
//	sce.r.cfra= 1;
//	sce.r.sfra= 1;
//	sce.r.efra= 250;
//	sce.r.xsch= 1920;
//	sce.r.ysch= 1080;
//	sce.r.xasp= 1;
//	sce.r.yasp= 1;
//	sce.r.xparts= 8;
//	sce.r.yparts= 8;
//	sce.r.size= 25;
//	sce.r.planes= 24;
//	sce.r.quality= 90;
//	sce.r.framapto= 100;
//	sce.r.images= 100;
//	sce.r.framelen= 1.0;
//	sce.r.frs_sec= 25;
//	sce.r.frs_sec_base= 1;
//	sce.r.ocres = 128;
//	sce.r.color_mgt_flag |= R_COLOR_MANAGEMENT;
//
//	sce.r.bake_mode= 1;	/* prevent to include render stuff here */
//	sce.r.bake_filter= 8;
//	sce.r.bake_osa= 5;
//	sce.r.bake_flag= R_BAKE_CLEAR;
//	sce.r.bake_normal_space= R_BAKE_SPACE_TANGENT;
//
//	sce.r.scemode= R_DOCOMP|R_DOSEQ|R_EXTENSION;
//	sce.r.stamp= R_STAMP_TIME|R_STAMP_FRAME|R_STAMP_DATE|R_STAMP_SCENE|R_STAMP_CAMERA;
//
//	sce.r.threads= 1;
//
//	sce.r.simplify_subsurf= 6;
//	sce.r.simplify_particles= 1.0f;
//	sce.r.simplify_shadowsamples= 16;
//	sce.r.simplify_aosss= 1.0f;
//
//	sce.r.cineonblack= 95;
//	sce.r.cineonwhite= 685;
//	sce.r.cineongamma= 1.7f;
//
//	sce.toolsettings = MEM_callocN(sizeof(struct ToolSettings),"Tool Settings Struct");
//	sce.toolsettings.cornertype=1;
//	sce.toolsettings.degr = 90;
//	sce.toolsettings.step = 9;
//	sce.toolsettings.turn = 1;
//	sce.toolsettings.extr_offs = 1;
//	sce.toolsettings.doublimit = 0.001;
//	sce.toolsettings.segments = 32;
//	sce.toolsettings.rings = 32;
//	sce.toolsettings.vertices = 32;
//	sce.toolsettings.editbutflag = 1;
//	sce.toolsettings.uvcalc_radius = 1.0f;
//	sce.toolsettings.uvcalc_cubesize = 1.0f;
//	sce.toolsettings.uvcalc_mapdir = 1;
//	sce.toolsettings.uvcalc_mapalign = 1;
//	sce.toolsettings.unwrapper = 1;
//	sce.toolsettings.select_thresh= 0.01f;
//	sce.toolsettings.jointrilimit = 0.8f;
//
//	sce.toolsettings.selectmode= SCE_SELECT_VERTEX;
//	sce.toolsettings.normalsize= 0.1;
//	sce.toolsettings.autokey_mode= U.autokey_mode;
//
//	sce.toolsettings.skgen_resolution = 100;
//	sce.toolsettings.skgen_threshold_internal 	= 0.01f;
//	sce.toolsettings.skgen_threshold_external 	= 0.01f;
//	sce.toolsettings.skgen_angle_limit	 		= 45.0f;
//	sce.toolsettings.skgen_length_ratio			= 1.3f;
//	sce.toolsettings.skgen_length_limit			= 1.5f;
//	sce.toolsettings.skgen_correlation_limit		= 0.98f;
//	sce.toolsettings.skgen_symmetry_limit			= 0.1f;
//	sce.toolsettings.skgen_postpro = SKGEN_SMOOTH;
//	sce.toolsettings.skgen_postpro_passes = 1;
//	sce.toolsettings.skgen_options = SKGEN_FILTER_INTERNAL|SKGEN_FILTER_EXTERNAL|SKGEN_FILTER_SMART|SKGEN_HARMONIC|SKGEN_SUB_CORRELATION|SKGEN_STICK_TO_EMBEDDING;
//	sce.toolsettings.skgen_subdivisions[0] = SKGEN_SUB_CORRELATION;
//	sce.toolsettings.skgen_subdivisions[1] = SKGEN_SUB_LENGTH;
//	sce.toolsettings.skgen_subdivisions[2] = SKGEN_SUB_ANGLE;
//
//	sce.toolsettings.proportional_size = 1.0f;
//
//	pset= &sce.toolsettings.particle;
//	pset.flag= PE_KEEP_LENGTHS|PE_LOCK_FIRST|PE_DEFLECT_EMITTER;
//	pset.emitterdist= 0.25f;
//	pset.totrekey= 5;
//	pset.totaddkey= 5;
//	pset.brushtype= PE_BRUSH_NONE;
//	for(a=0; a<PE_TOT_BRUSH; a++) {
//		pset.brush[a].strength= 50;
//		pset.brush[a].size= 50;
//		pset.brush[a].step= 10;
//	}
//	pset.brush[PE_BRUSH_CUT].strength= 100;
//
//	sce.jumpframe = 10;
//	sce.audio.mixrate = 44100;
//
//	strcpy(sce.r.backbuf, "//backbuf");
//	strcpy(sce.r.pic, U.renderdir);
//
//	BLI_init_rctf(&sce.r.safety, 0.1f, 0.9f, 0.1f, 0.9f);
//	sce.r.osa= 8;
//
//	/* note; in header_info.c the scene copy happens..., if you add more to renderdata it has to be checked there */
//	scene_add_render_layer(sce);
//
//	/* game data */
//	sce.gm.stereoflag = STEREO_NOSTEREO;
//	sce.gm.stereomode = STEREO_ANAGLYPH;
//	sce.gm.dome.angle = 180;
//	sce.gm.dome.mode = DOME_FISHEYE;
//	sce.gm.dome.res = 4;
//	sce.gm.dome.resbuf = 1.0f;
//	sce.gm.dome.tilt = 0;
//
//	sce.gm.xplay= 800;
//	sce.gm.yplay= 600;
//	sce.gm.freqplay= 60;
//	sce.gm.depth= 32;
//
//	sce.gm.gravity= 9.8f;
//	sce.gm.physicsEngine= WOPHY_BULLET;
//	sce.gm.mode = 32; //XXX ugly harcoding, still not sure we should drop mode. 32 == 1 << 5 == use_occlusion_culling
//	sce.gm.occlusionRes = 128;
//	sce.gm.ticrate = 60;
//	sce.gm.maxlogicstep = 5;
//	sce.gm.physubstep = 1;
//	sce.gm.maxphystep = 5;

	return sce;
}

public static Base object_in_scene(bObject ob, Scene sce)
{
	Base base;

	base= (Base)sce.base.first;
	while(base!=null) {
		if(base.object == ob) return base;
		base= base.next;
	}
	return null;
}

    public static void set_scene_bg(Scene scene) {
//	Scene *sce;
        Base base;
        bObject ob;
        Group group;
        GroupObject go;
        int flag;

//	/* check for cyclic sets, for reading old files but also for definite security (py?) */
//	scene_check_setscene(scene);
//
//	/* deselect objects (for dataselect) */
        for (ob = (bObject) G.main.object.first; ob != null; ob = (bObject) ob.next) {
            ob.flag &= ~(Blender.SELECT | ObjectTypes.OB_FROMGROUP);
        }
//
//	/* group flags again */
        for (group = (Group) G.main.group.first; group != null; group = (Group) group.next) {
            go = (GroupObject) group.gobject.first;
            while (go != null) {
                if (go.ob != null) {
                    go.ob.flag |= ObjectTypes.OB_FROMGROUP;
                }
                go = (GroupObject) go.next;
            }
        }

//	/* sort baselist */
//	DAG_scene_sort(scene);
//
//	/* ensure dags are built for sets */
//	for(sce= scene.set; sce; sce= sce.set)
//		if(sce.theDag==NULL)
//			DAG_scene_sort(sce);

        /* copy layers and flags from bases to objects */
        for (base = (Base) scene.base.first; base != null; base = base.next) {
            ob = base.object;
            ob.lay = base.lay;

            /* group patch... */
            base.flag &= ~(ObjectTypes.OB_FROMGROUP);
            flag = ob.flag & (ObjectTypes.OB_FROMGROUP);
            base.flag |= flag;

            /* not too nice... for recovering objects with lost data */
            if (ob.pose == null) {
                base.flag &= ~ObjectTypes.OB_POSEMODE;
            }
            ob.flag = (short) (base.flag >> 16);

            ob.ctime = -1234567.0f;	/* force ipo to be calculated later */
        }
        /* no full animation update, this to enable render code to work (render code calls own animation updates) */
    }

    /* called from creator.c */
    public static void set_scene_name(String name) {
        Scene sce;

        for (sce = (Scene) G.main.scene.first; sce != null; sce = (Scene) sce.next) {
            if (StringUtil.BLI_streq(StringUtil.toCString(name), 0, sce.name, 2)) {
                set_scene_bg(sce);
                return;
            }
        }

        //XXX error("Can't find scene: %s", name);
    }
//
///* used by metaballs
// * doesnt return the original duplicated object, only dupli's
// */
//int next_object(Scene *scene, int val, Base **base, Object **ob)
//{
//	static ListBase *duplilist= NULL;
//	static DupliObject *dupob;
//	static int fase= F_START, in_next_object= 0;
//	int run_again=1;
//
//	/* init */
//	if(val==0) {
//		fase= F_START;
//		dupob= NULL;
//
//		/* XXX particle systems with metas+dupligroups call this recursively */
//		/* see bug #18725 */
//		if(in_next_object) {
//			printf("ERROR: MetaBall generation called recursively, not supported\n");
//
//			return F_ERROR;
//		}
//	}
//	else {
//		in_next_object= 1;
//
//		/* run_again is set when a duplilist has been ended */
//		while(run_again) {
//			run_again= 0;
//
//			/* the first base */
//			if(fase==F_START) {
//				*base= scene.base.first;
//				if(*base) {
//					*ob= (*base).object;
//					fase= F_SCENE;
//				}
//				else {
//				    /* exception: empty scene */
//					if(scene.set && scene.set.base.first) {
//						*base= scene.set.base.first;
//						*ob= (*base).object;
//						fase= F_SET;
//					}
//				}
//			}
//			else {
//				if(*base && fase!=F_DUPLI) {
//					*base= (*base).next;
//					if(*base) *ob= (*base).object;
//					else {
//						if(fase==F_SCENE) {
//							/* scene is finished, now do the set */
//							if(scene.set && scene.set.base.first) {
//								*base= scene.set.base.first;
//								*ob= (*base).object;
//								fase= F_SET;
//							}
//						}
//					}
//				}
//			}
//
//			if(*base == NULL) fase= F_START;
//			else {
//				if(fase!=F_DUPLI) {
//					if( (*base).object.transflag & OB_DUPLI) {
//						/* groups cannot be duplicated for mballs yet,
//						this enters eternal loop because of
//						makeDispListMBall getting called inside of group_duplilist */
//						if((*base).object.dup_group == NULL) {
//							duplilist= object_duplilist(scene, (*base).object);
//
//							dupob= duplilist.first;
//
//							if(!dupob)
//								free_object_duplilist(duplilist);
//						}
//					}
//				}
//				/* handle dupli's */
//				if(dupob) {
//
//					Mat4CpyMat4(dupob.ob.obmat, dupob.mat);
//
//					(*base).flag |= OB_FROMDUPLI;
//					*ob= dupob.ob;
//					fase= F_DUPLI;
//
//					dupob= dupob.next;
//				}
//				else if(fase==F_DUPLI) {
//					fase= F_SCENE;
//					(*base).flag &= ~OB_FROMDUPLI;
//
//					for(dupob= duplilist.first; dupob; dupob= dupob.next) {
//						Mat4CpyMat4(dupob.ob.obmat, dupob.omat);
//					}
//
//					free_object_duplilist(duplilist);
//					duplilist= NULL;
//					run_again= 1;
//				}
//			}
//		}
//	}
//
//	/* reset recursion test */
//	in_next_object= 0;
//
//	return fase;
//}

    public static bObject scene_find_camera(Scene sc) {
        Base base;

        for (base = (Base) sc.base.first; base != null; base = base.next) {
            if (base.object.type == ObjectTypes.OB_CAMERA) {
                return base.object;
            }
        }

        return null;
    }

    public static Base scene_add_base(Scene sce, bObject ob) {
        Base b = new Base();
        ListBaseUtil.BLI_addhead(sce.base, b);

        b.object = ob;
        b.flag = ob.flag;
        b.lay = ob.lay;

        return b;
    }

    public static void scene_deselect_all(Scene sce) {
        Base b;

        for (b = (Base) sce.base.first; b != null; b = b.next) {
            b.flag &= ~Blender.SELECT;
            b.object.flag = (short) b.flag;
        }
    }

    public static void scene_select_base(Scene sce, Base selbase) {
        scene_deselect_all(sce);

        selbase.flag |= Blender.SELECT;
        selbase.object.flag = (short) selbase.flag;

        sce.basact = selbase;
    }

///* checks for cycle, returns 1 if it's all OK */
//int scene_check_setscene(Scene *sce)
//{
//	Scene *scene;
//	int a, totscene;
//
//	if(sce.set==NULL) return 1;
//
//	totscene= 0;
//	for(scene= G.main.scene.first; scene; scene= scene.id.next)
//		totscene++;
//
//	for(a=0, scene=sce; scene.set; scene=scene.set, a++) {
//		/* more iterations than scenes means we have a cycle */
//		if(a > totscene) {
//			/* the tested scene gets zero'ed, that's typically current scene */
//			sce.set= NULL;
//			return 0;
//		}
//	}
//
//	return 1;
//}
//
///* This (evil) function is needed to cope with two legacy Blender rendering features
//* mblur (motion blur that renders 'subframes' and blurs them together), and fields
//* rendering. Thus, the use of ugly globals from object.c
//*/
//// BAD... EVIL... JUJU...!!!!
//// XXX moved here temporarily
//float frame_to_float (Scene *scene, int cfra)		/* see also bsystem_time in object.c */
//{
//	extern float bluroffs;	/* bad stuff borrowed from object.c */
//	extern float fieldoffs;
//	float ctime;
//
//	ctime= (float)cfra;
//	ctime+= bluroffs+fieldoffs;
//	ctime*= scene.r.framelen;
//
//	return ctime;
//}

public static void scene_update(Scene sce, int lay)
{
	Base base;
	bObject ob;
//	float ctime = frame_to_float(sce, sce.r.cfra);

//	if(sce.theDag==NULL)
//		DAG_scene_sort(sce);
//
//	DAG_scene_update_flags(sce, lay);   // only stuff that moves or needs display still

//	/* All 'standard' (i.e. without any dependencies) animation is handled here,
//		* with an 'local' to 'macro' order of evaluation. This should ensure that
//		* settings stored nestled within a hierarchy (i.e. settings in a Texture block
//	    * can be overridden by settings from Scene, which owns the Texture through a hierarchy
//	    * such as Scene.World.MTex/Texture) can still get correctly overridden.
//		*/
//	BKE_animsys_evaluate_all_animation(G.main, ctime);

	for(base= (Base)sce.base.first; base!=null; base= base.next) {
		ob= base.object;

		ObjectUtil.object_handle_update(sce, ob);   // bke_object.h

		/* only update layer when an ipo */
			// XXX old animation system
		//if(ob.ipo && has_ipo_code(ob.ipo, OB_LAY) ) {
		//	base.lay= ob.lay;
		//}
	}
}

/* applies changes right away, does all sets too */
public static void scene_update_for_newframe(Scene sce, int lay)
{
	Scene scene= sce;

	/* clear animation overrides */
	// XXX TODO...

	/* sets first, we allow per definition current scene to have dependencies on sets */
	for(sce= sce.set; sce!=null; sce= sce.set)
		scene_update(sce, lay);

	scene_update(scene, lay);
}

///* return default layer, also used to patch old files */
//void scene_add_render_layer(Scene *sce)
//{
//	SceneRenderLayer *srl;
//	int tot= 1 + BLI_countlist(&sce.r.layers);
//
//	srl= MEM_callocN(sizeof(SceneRenderLayer), "new render layer");
//	sprintf(srl.name, "%d RenderLayer", tot);
//	BLI_addtail(&sce.r.layers, srl);
//
//	/* note, this is also in render, pipeline.c, to make layer when scenedata doesnt have it */
//	srl.lay= (1<<20) -1;
//	srl.layflag= 0x7FFF;	/* solid ztra halo edge strand */
//	srl.passflag= SCE_PASS_COMBINED|SCE_PASS_Z;
//}
//
//void sculptsession_free(Sculpt *sculpt)
//{
//	SculptSession *ss= sculpt.session;
//	if(ss) {
//		if(ss.projverts)
//			MEM_freeN(ss.projverts);
//
//		if(ss.fmap)
//			MEM_freeN(ss.fmap);
//
//		if(ss.fmap_mem)
//			MEM_freeN(ss.fmap_mem);
//
//		if(ss.texcache)
//			MEM_freeN(ss.texcache);
//		MEM_freeN(ss);
//		sculpt.session= NULL;
//	}
//}
//
///* render simplification */
//
//int get_render_subsurf_level(RenderData *r, int lvl)
//{
//	if(G.rt == 1 && (r.mode & R_SIMPLIFY))
//		return MIN2(r.simplify_subsurf, lvl);
//	else
//		return lvl;
//}
//
//int get_render_child_particle_number(RenderData *r, int num)
//{
//	if(G.rt == 1 && (r.mode & R_SIMPLIFY))
//		return (int)(r.simplify_particles*num);
//	else
//		return num;
//}
//
//int get_render_shadow_samples(RenderData *r, int samples)
//{
//	if(G.rt == 1 && (r.mode & R_SIMPLIFY) && samples > 0)
//		return MIN2(r.simplify_shadowsamples, samples);
//	else
//		return samples;
//}
//
//float get_render_aosss_error(RenderData *r, float error)
//{
//	if(G.rt == 1 && (r.mode & R_SIMPLIFY))
//		return ((1.0f-r.simplify_aosss)*10.0f + 1.0f)*error;
//	else
//		return error;
//}
//
//void free_dome_warp_text(struct Text *txt)
//{
//	Scene *scene;
//
//	scene = G.main.scene.first;
//	while(scene) {
//		if (scene.r.dometext == txt)
//			scene.r.dometext = NULL;
//		scene = scene.id.next;
//	}
//}
}