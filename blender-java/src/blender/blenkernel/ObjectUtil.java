/* object.c
 *
 * 
 * $Id: ObjectUtil.java,v 1.2 2009/09/18 05:20:13 jladere Exp $
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

import blender.blenlib.Arithb;
import blender.makesdna.ObjectTypes;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bObject;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.CameraTypes;
import blender.makesdna.DNA_ID;
import blender.makesdna.LampTypes;
import blender.makesdna.MaterialTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.TextureTypes;
import blender.makesdna.sdna.BoundBox;
import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.Curve;
import blender.makesdna.sdna.Group;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.Lamp;
import blender.makesdna.sdna.Material;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.ModifierData;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.SpaceOops;
import blender.makesdna.sdna.Tex;
import blender.makesdna.sdna.TreeStoreElem;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.World;
import blender.makesdna.sdna.bConstraint;
import blender.makesdna.sdna.bPoseChannel;
import blender.makesdna.sdna.bScreen;
import static blender.blenkernel.Blender.G;

public class ObjectUtil {

///* Local function protos */
//static void solve_parenting (Scene *scene, Object *ob, Object *par, float obmat[][4], float slowmat[][4], int simul);
//
//float originmat[3][3];	/* after where_is_object(), can be used in other functions (bad!) */
//
//void clear_workob(Object *workob)
//{
//	memset(workob, 0, sizeof(Object));
//
//	workob.size[0]= workob.size[1]= workob.size[2]= 1.0;
//
//}

    public static void copy_baseflags(Scene scene) {
        Base base = (Base) scene.base.first;

        while (base != null) {
            base.object.flag = (short) base.flag;
            base = base.next;
        }
    }

//void copy_objectflags(struct Scene *scene)
//{
//	Base *base= scene.base.first;
//
//	while(base) {
//		base.flag= base.object.flag;
//		base= base.next;
//	}
//}
//
//void update_base_layer(struct Scene *scene, Object *ob)
//{
//	Base *base= scene.base.first;
//
//	while (base) {
//		if (base.object == ob) base.lay= ob.lay;
//		base= base.next;
//	}
//}
//
//void object_free_particlesystems(Object *ob)
//{
//	while(ob.particlesystem.first){
//		ParticleSystem *psys = ob.particlesystem.first;
//
//		BLI_remlink(&ob.particlesystem,psys);
//
//		psys_free(ob,psys);
//	}
//}
//
//void object_free_softbody(Object *ob)
//{
//	if(ob.soft) {
//		sbFree(ob.soft);
//		ob.soft= NULL;
//	}
//}
//
//void object_free_bulletsoftbody(Object *ob)
//{
//	if(ob.bsoft) {
//		bsbFree(ob.bsoft);
//		ob.bsoft= NULL;
//	}
//}
//
//void object_free_modifiers(Object *ob)
//{
//	while (ob.modifiers.first) {
//		ModifierData *md = ob.modifiers.first;
//
//		BLI_remlink(&ob.modifiers, md);
//
//		modifier_free(md);
//	}
//
//	/* particle modifiers were freed, so free the particlesystems as well */
//	object_free_particlesystems(ob);
//
//	/* same for softbody */
//	object_free_softbody(ob);
//}

    /* here we will collect all local displist stuff */
    /* also (ab)used in depsgraph */
    public static void object_free_display(bObject ob) {
        if (ob.derivedDeform != null) {
//            ob.derivedDeform.needsFree = 1;
//            ob.derivedDeform.release(ob.derivedDeform);
            ob.derivedDeform = null;
        }
        if (ob.derivedFinal != null) {
//            ob.derivedFinal.needsFree = 1;
//            ob.derivedFinal.release(ob.derivedFinal);
            ob.derivedFinal = null;
        }

//	freedisplist(ob.disp);
    }

/* do not free object itself */
public static void free_object(bObject ob)
{
	int a;

	object_free_display(ob);

	/* disconnect specific data */
	if(ob.data!=null) {
		ID id= (ID)ob.data;
		id.us--;
		if(id.us==0) {
			if(ob.type==ObjectTypes.OB_MESH) MeshUtil.unlink_mesh((Mesh)ob.data);
//			else if(ob.type==ObjectType.OB_CURVE) unlink_curve(ob.data);
//			else if(ob.type==ObjectType.OB_MBALL) unlink_mball(ob.data);
		}
		ob.data= null;
	}

//	for(a=0; a<ob.totcol; a++) {
//		if(ob.mat[a]) ob.mat[a].id.us--;
//	}
//	if(ob.mat) MEM_freeN(ob.mat);
//	if(ob.matbits) MEM_freeN(ob.matbits);
	ob.mat= null;
//	ob.matbits= 0;
//	if(ob.bb) MEM_freeN(ob.bb);
	ob.bb= null;
//	if(ob.path) free_path(ob.path);
	//ob.path= null;
//	if(ob.adt) BKE_free_animdata((ID *)ob);
	if(ob.poselib!=null) ob.poselib.id.us--;
	if(ob.dup_group!=null) ob.dup_group.id.us--;
	if(ob.defbase.first!=null)
		ListBaseUtil.BLI_freelistN(ob.defbase);
//	if(ob.pose)
//		free_pose(ob.pose);
//	free_properties(&ob.prop);
//	object_free_modifiers(ob);
//
//	free_sensors(&ob.sensors);
//	free_controllers(&ob.controllers);
//	free_actuators(&ob.actuators);
//
//	free_constraints(&ob.constraints);

	if(ob.pd!=null){
		if(ob.pd.tex!=null)
			ob.pd.tex.id.us--;
//		MEM_freeN(ob.pd);
	}
//	if(ob.soft) sbFree(ob.soft);
//	if(ob.bsoft) bsbFree(ob.bsoft);
//	if(ob.gpulamp.first) GPU_lamp_free(ob);
}

//static void unlink_object__unlinkModifierLinks(void *userData, Object *ob, Object **obpoin)
//{
//	Object *unlinkOb = userData;
//
//	if (*obpoin==unlinkOb) {
//		*obpoin = NULL;
//		ob.recalc |= OB_RECALC;
//	}
//}

public static void unlink_object(Scene scene, bObject ob)
{
	bObject obt;
	Material mat;
	World wrld;
	bScreen sc;
	Scene sce;
	Curve cu;
	Tex tex;
	Group group;
	Camera camera;
	bConstraint con;
	//bActionStrip *strip; // XXX animsys
	ModifierData md;
//	int a;

//	unlink_controllers(&ob.controllers);
//	unlink_actuators(&ob.actuators);

	/* check all objects: parents en bevels and fields, also from libraries */
	obt= G.main.object.first;
	while(obt!=null) {
		if(obt.proxy==ob)
			obt.proxy= null;
		if(obt.proxy_from==ob) {
			obt.proxy_from= null;
			obt.recalc |= ObjectTypes.OB_RECALC_OB;
		}
		if(obt.proxy_group==ob)
			obt.proxy_group= null;

		if(obt.parent==ob) {
			obt.parent= null;
			obt.recalc |= ObjectTypes.OB_RECALC;
		}

		if(obt.track==ob) {
			obt.track= null;
			obt.recalc |= ObjectTypes.OB_RECALC_OB;
		}

//		modifiers_foreachObjectLink(obt, unlink_object__unlinkModifierLinks, ob);
//
//		if ELEM(obt.type, OB_CURVE, OB_FONT) {
//			cu= obt.data;
//
//			if(cu.bevobj==ob) {
//				cu.bevobj= NULL;
//				obt.recalc |= OB_RECALC;
//			}
//			if(cu.taperobj==ob) {
//				cu.taperobj= NULL;
//				obt.recalc |= OB_RECALC;
//			}
//			if(cu.textoncurve==ob) {
//				cu.textoncurve= NULL;
//				obt.recalc |= OB_RECALC;
//			}
//		}
//		else if(obt.type==OB_ARMATURE && obt.pose) {
//			bPoseChannel *pchan;
//			for(pchan= obt.pose.chanbase.first; pchan; pchan= pchan.next) {
//				for (con = pchan.constraints.first; con; con=con.next) {
//					bConstraintTypeInfo *cti= constraint_get_typeinfo(con);
//					ListBase targets = {NULL, NULL};
//					bConstraintTarget *ct;
//
//					if (cti && cti.get_constraint_targets) {
//						cti.get_constraint_targets(con, &targets);
//
//						for (ct= targets.first; ct; ct= ct.next) {
//							if (ct.tar == ob) {
//								ct.tar = NULL;
//								strcpy(ct.subtarget, "");
//								obt.recalc |= OB_RECALC_DATA;
//							}
//						}
//
//						if (cti.flush_constraint_targets)
//							cti.flush_constraint_targets(con, &targets, 0);
//					}
//				}
//				if(pchan.custom==ob)
//					pchan.custom= NULL;
//			}
//		}
//
//		sca_remove_ob_poin(obt, ob);
//
//		for (con = obt.constraints.first; con; con=con.next) {
//			bConstraintTypeInfo *cti= constraint_get_typeinfo(con);
//			ListBase targets = {NULL, NULL};
//			bConstraintTarget *ct;
//
//			if (cti && cti.get_constraint_targets) {
//				cti.get_constraint_targets(con, &targets);
//
//				for (ct= targets.first; ct; ct= ct.next) {
//					if (ct.tar == ob) {
//						ct.tar = NULL;
//						strcpy(ct.subtarget, "");
//						obt.recalc |= OB_RECALC_DATA;
//					}
//				}
//
//				if (cti.flush_constraint_targets)
//					cti.flush_constraint_targets(con, &targets, 0);
//			}
//		}
//
//		/* object is deflector or field */
//		if(ob.pd) {
//			if(obt.soft)
//				obt.recalc |= OB_RECALC_DATA;
//
//			/* cloth */
//			for(md=obt.modifiers.first; md; md=md.next)
//				if(md.type == eModifierType_Cloth)
//					obt.recalc |= OB_RECALC_DATA;
//		}
//
//		/* strips */
//#if 0 // XXX old animation system
//		for(strip= obt.nlastrips.first; strip; strip= strip.next) {
//			if(strip.object==ob)
//				strip.object= NULL;
//
//			if(strip.modifiers.first) {
//				bActionModifier *amod;
//				for(amod= strip.modifiers.first; amod; amod= amod.next)
//					if(amod.ob==ob)
//						amod.ob= NULL;
//			}
//		}
//#endif // XXX old animation system
//
//		/* particle systems */
//		if(obt.particlesystem.first) {
//			ParticleSystem *tpsys= obt.particlesystem.first;
//			for(; tpsys; tpsys=tpsys.next) {
//				BoidState *state = NULL;
//				BoidRule *rule = NULL;
//
//				ParticleTarget *pt = tpsys.targets.first;
//				for(; pt; pt=pt.next) {
//					if(pt.ob==ob) {
//						pt.ob = NULL;
//						obt.recalc |= OB_RECALC_DATA;
//						break;
//					}
//				}
//
//				if(tpsys.target_ob==ob) {
//					tpsys.target_ob= NULL;
//					obt.recalc |= OB_RECALC_DATA;
//				}
//
//				if(tpsys.part.dup_ob==ob)
//					tpsys.part.dup_ob= NULL;
//
//				if(tpsys.part.flag&PART_STICKY) {
//					ParticleData *pa;
//					int p;
//
//					for(p=0,pa=tpsys.particles; p<tpsys.totpart; p++,pa++) {
//						if(pa.stick_ob==ob) {
//							pa.stick_ob= 0;
//							pa.flag &= ~PARS_STICKY;
//						}
//					}
//				}
//				if(tpsys.part.boids) {
//					for(state = tpsys.part.boids.states.first; state; state=state.next) {
//						for(rule = state.rules.first; rule; rule=rule.next) {
//							if(rule.type==eBoidRuleType_Avoid) {
//								BoidRuleGoalAvoid *gabr = (BoidRuleGoalAvoid*)rule;
//								if(gabr.ob==ob)
//									gabr.ob= NULL;
//							}
//							else if(rule.type==eBoidRuleType_FollowLeader) {
//								BoidRuleFollowLeader *flbr = (BoidRuleFollowLeader*)rule;
//								if(flbr.ob==ob)
//									flbr.ob= NULL;
//							}
//						}
//					}
//				}
//			}
//			if(ob.pd)
//				obt.recalc |= OB_RECALC_DATA;
//		}

		obt= (bObject)obt.id.next;
	}

	/* materials */
	mat= (Material)G.main.mat.first;
	while(mat!=null) {

		for(int a=0; a<TextureTypes.MAX_MTEX; a++) {
			if(mat.mtex[a]!=null && ob==mat.mtex[a].object) {
				/* actually, test for lib here... to do */
				mat.mtex[a].object= null;
			}
		}

		mat= (Material)mat.id.next;
	}

	/* textures */
	tex= (Tex)G.main.tex.first;
	while(tex!=null) {
		if(tex.env!=null) {
			if(tex.env.object == ob) tex.env.object= null;
		}
		tex= (Tex)tex.id.next;
	}

//	/* mballs (scene==NULL when called from library.c) */
//	if(scene && ob.type==OB_MBALL) {
//		obt= find_basis_mball(scene, ob);
//		if(obt) freedisplist(&obt.disp);
//	}

	/* worlds */
	wrld= G.main.world.first;
	while(wrld!=null) {
		if(wrld.id.lib==null) {
			for(int a=0; a<TextureTypes.MAX_MTEX; a++) {
				if(wrld.mtex[a]!=null && ob==wrld.mtex[a].object)
					wrld.mtex[a].object= null;
			}
		}

		wrld= (World)wrld.id.next;
	}

	/* scenes */
	sce= G.main.scene.first;
	while(sce!=null) {
		if(sce.id.lib==null) {
			if(sce.camera==ob) sce.camera= null;
//			if(sce.toolsettings.skgen_template==ob) sce.toolsettings.skgen_template = NULL;
		}
		sce= (Scene)sce.id.next;
	}

	/* screens */
	sc= G.main.screen.first;
	while(sc!=null) {
		ScrArea sa= (ScrArea)sc.areabase.first;
		while(sa!=null) {
			SpaceLink sl;

			for (sl= (SpaceLink)sa.spacedata.first; sl!=null; sl= (SpaceLink)sl.next) {
				if(sl.spacetype==SpaceTypes.SPACE_VIEW3D) {
					View3D v3d= (View3D) sl;

					if(v3d.camera==ob) {
						v3d.camera= null;
						// XXX if(v3d.persp==V3D_CAMOB) v3d.persp= V3D_PERSP;
					}
					if(v3d.localvd!=null && v3d.localvd.camera==ob ) {
						v3d.localvd.camera= null;
						// XXX if(v3d.localvd.persp==V3D_CAMOB) v3d.localvd.persp= V3D_PERSP;
					}
				}
				else if(sl.spacetype==SpaceTypes.SPACE_OUTLINER) {
					SpaceOops so= (SpaceOops)sl;

					if(so.treestore!=null) {
						TreeStoreElem[] tselems= so.treestore.data.myarray;
                                                int tselem_p = 0;
//						int a;
						for(int a=0; a<so.treestore.usedelem; a++, tselem_p++) {
                                                    TreeStoreElem tselem = tselems[tselem_p];
                                                    if(tselem.id==(ID)ob)
                                                        tselem.id= null;
						}
					}
				}
			}

			sa= sa.next;
		}
		sc= (bScreen)sc.id.next;
	}

//	/* groups */
//	group= G.main.group.first;
//	while(group) {
//		rem_from_group(group, ob);
//		group= group.id.next;
//	}

	/* cameras */
	camera= G.main.camera.first;
	while(camera!=null) {
		if (camera.dof_ob==ob) {
			camera.dof_ob = null;
		}
		camera= (Camera)camera.id.next;
	}
}

//int exist_object(Object *obtest)
//{
//	Object *ob;
//
//	if(obtest==NULL) return 0;
//
//	ob= G.main.object.first;
//	while(ob) {
//		if(ob==obtest) return 1;
//		ob= ob.id.next;
//	}
//	return 0;
//}

    public static Object add_camera(String name) {
        Camera cam = (Camera) LibraryUtil.alloc_libblock(G.main.camera, DNA_ID.ID_CA, StringUtil.toCString(name),0);

        cam.lens = 35.0f;
//        cam.angle = 49.14f;
        cam.clipsta = 0.1f;
        cam.clipend = 100.0f;
        cam.drawsize = 0.5f;
        cam.ortho_scale = 6.0f;
        cam.flag |= CameraTypes.CAM_SHOWTITLESAFE;
        cam.passepartalpha = 0.2f;

        return cam;
    }

//Camera *copy_camera(Camera *cam)
//{
//	Camera *camn;
//
//	camn= copy_libblock(cam);
//	camn.adt= BKE_copy_animdata(cam.adt);
//
//	return camn;
//}
//
//
//
//void make_local_camera(Camera *cam)
//{
//	Object *ob;
//	Camera *camn;
//	int local=0, lib=0;
//
//	/* - only lib users: do nothing
//	    * - only local users: set flag
//	    * - mixed: make copy
//	    */
//
//	if(cam.id.lib==0) return;
//	if(cam.id.us==1) {
//		cam.id.lib= 0;
//		cam.id.flag= LIB_LOCAL;
//		new_id(0, (ID *)cam, 0);
//		return;
//	}
//
//	ob= G.main.object.first;
//	while(ob) {
//		if(ob.data==cam) {
//			if(ob.id.lib) lib= 1;
//			else local= 1;
//		}
//		ob= ob.id.next;
//	}
//
//	if(local && lib==0) {
//		cam.id.lib= 0;
//		cam.id.flag= LIB_LOCAL;
//		new_id(0, (ID *)cam, 0);
//	}
//	else if(local && lib) {
//		camn= copy_camera(cam);
//		camn.id.us= 0;
//
//		ob= G.main.object.first;
//		while(ob) {
//			if(ob.data==cam) {
//
//				if(ob.id.lib==0) {
//					ob.data= camn;
//					camn.id.us++;
//					cam.id.us--;
//				}
//			}
//			ob= ob.id.next;
//		}
//	}
//}

    /* get the camera's dof value, takes the dof object into account */
    public static float dof_camera(bObject ob) {
        Camera cam = (Camera) ob.data;
        if (ob.type != ObjectTypes.OB_CAMERA) {
            return 0.0f;
        }
        if (cam.dof_ob != null) {
            /* too simple, better to return the distance on the view axis only
             * return VecLenf(ob.obmat[3], cam.dof_ob.obmat[3]); */
            float[][] mat = new float[4][4], obmat = new float[4][4];

            Arithb.Mat4CpyMat4(obmat, ob.obmat);
            Arithb.Mat4Ortho(obmat);
            Arithb.Mat4Invert(ob.imat, obmat);
            Arithb.Mat4MulMat4(mat, cam.dof_ob.obmat, ob.imat);
            return StrictMath.abs(mat[3][2]);
        }
        return cam.YF_dofdist;
    }

    public static Object add_lamp(String name) {
        Lamp la;

        la = (Lamp) LibraryUtil.alloc_libblock(G.main.lamp, DNA_ID.ID_LA, StringUtil.toCString(name),0);

        la.r = la.g = la.b = la.k = 1.0f;
        la.haint = la.energy = 1.0f;
        la.dist = 25.0f;
        la.spotsize = 45.0f;
        la.spotblend = 0.15f;
        la.att2 = 1.0f;
        la.mode = LampTypes.LA_SHAD_BUF;
        la.bufsize = 512;
        la.clipsta = 0.5f;
        la.clipend = 40.0f;
        la.shadspotsize = 45.0f;
        la.samp = 3;
        la.bias = 1.0f;
        la.soft = 3.0f;
        la.ray_samp = la.ray_sampy = la.ray_sampz = 1;
        la.area_size = la.area_sizey = la.area_sizez = 1.0f;
        la.buffers = 1;
        la.buftype = LampTypes.LA_SHADBUF_HALFWAY;
        la.ray_samp_method = LampTypes.LA_SAMP_HALTON;
        la.adapt_thresh = 0.001f;
        la.preview = null;
        la.falloff_type = LampTypes.LA_FALLOFF_INVLINEAR;
//	la.curfalloff = colortools.curvemapping_add(1, 0.0f, 1.0f, 1.0f, 0.0f);
        la.sun_effect_type = 0;
        la.horizon_brightness = 1.0f;
        la.spread = 1.0f;
        la.sun_brightness = 1.0f;
        la.sun_size = 1.0f;
        la.backscattered_light = 1.0f;
        la.atm_turbidity = 2.0f;
        la.atm_inscattering_factor = 1.0f;
        la.atm_extinction_factor = 1.0f;
        la.atm_distance_factor = 1.0f;
        la.sun_intensity = 1.0f;
        la.skyblendtype = MaterialTypes.MA_RAMP_ADD;
        la.skyblendfac = 1.0f;
        la.sky_colorspace = Arithb.BLI_CS_CIE;
        la.sky_exposure = 1.0f;

//	colortools.curvemapping_initialize(la.curfalloff);
        return la;
    }

//Lamp *copy_lamp(Lamp *la)
//{
//	Lamp *lan;
//	int a;
//
//	lan= copy_libblock(la);
//
//	for(a=0; a<MAX_MTEX; a++) {
//		if(lan.mtex[a]) {
//			lan.mtex[a]= MEM_mallocN(sizeof(MTex), "copylamptex");
//			memcpy(lan.mtex[a], la.mtex[a], sizeof(MTex));
//			id_us_plus((ID *)lan.mtex[a].tex);
//		}
//	}
//
//	lan.curfalloff = curvemapping_copy(la.curfalloff);
//
//#if 0 // XXX old animation system
//	id_us_plus((ID *)lan.ipo);
//#endif // XXX old animation system
//
//	if (la.preview) lan.preview = BKE_previewimg_copy(la.preview);
//
//	return lan;
//}
//
//void make_local_lamp(Lamp *la)
//{
//	Object *ob;
//	Lamp *lan;
//	int local=0, lib=0;
//
//	/* - only lib users: do nothing
//	    * - only local users: set flag
//	    * - mixed: make copy
//	    */
//
//	if(la.id.lib==0) return;
//	if(la.id.us==1) {
//		la.id.lib= 0;
//		la.id.flag= LIB_LOCAL;
//		new_id(0, (ID *)la, 0);
//		return;
//	}
//
//	ob= G.main.object.first;
//	while(ob) {
//		if(ob.data==la) {
//			if(ob.id.lib) lib= 1;
//			else local= 1;
//		}
//		ob= ob.id.next;
//	}
//
//	if(local && lib==0) {
//		la.id.lib= 0;
//		la.id.flag= LIB_LOCAL;
//		new_id(0, (ID *)la, 0);
//	}
//	else if(local && lib) {
//		lan= copy_lamp(la);
//		lan.id.us= 0;
//
//		ob= G.main.object.first;
//		while(ob) {
//			if(ob.data==la) {
//
//				if(ob.id.lib==0) {
//					ob.data= lan;
//					lan.id.us++;
//					la.id.us--;
//				}
//			}
//			ob= ob.id.next;
//		}
//	}
//}

    public static void free_camera(Camera ca) {
//        BKE_free_animdata((ID *)ca);
    }

    public static void free_lamp(Lamp la) {
//	MTex *mtex;
//	int a;
//
//	for(a=0; a<MAX_MTEX; a++) {
//		mtex= la.mtex[a];
//		if(mtex && mtex.tex) mtex.tex.id.us--;
//		if(mtex) MEM_freeN(mtex);
//	}
//
//	BKE_free_animdata((ID *)la);
//
//	curvemapping_free(la.curfalloff);
//
//	BKE_previewimg_free(&la.preview);
//	BKE_icon_delete(&la.id);
        la.id.icon_id = 0;
    }

//void *add_wave()
//{
//	return 0;
//}


/* *************************************************** */

public static Object add_obdata_from_type(int type)
{
	switch (type) {
	case ObjectTypes.OB_MESH: return MeshUtil.add_mesh("Mesh");
//	case ObjectTypes.OB_CURVE: return add_curve("Curve", ObjectTypes.OB_CURVE);
//	case ObjectTypes.OB_SURF: return add_curve("Surf", ObjectTypes.OB_SURF);
//	case ObjectTypes.OB_FONT: return add_curve("Text", ObjectTypes.OB_FONT);
//	case ObjectTypes.OB_MBALL: return add_mball("Meta");
	case ObjectTypes.OB_CAMERA: return add_camera("Camera");
	case ObjectTypes.OB_LAMP: return add_lamp("Lamp");
//	case ObjectTypes.OB_LATTICE: return add_lattice("Lattice");
//	case ObjectTypes.OB_WAVE: return add_wave();
//	case ObjectTypes.OB_ARMATURE: return add_armature("Armature");
	case ObjectTypes.OB_EMPTY: return null;
	default:
		System.out.printf("add_obdata_from_type: Internal error, bad type: %d\n", type);
		return null;
	}
}

public static String get_obdata_defname(int type)
{
	switch (type) {
	case ObjectTypes.OB_MESH: return "Mesh";
	case ObjectTypes.OB_CURVE: return "Curve";
	case ObjectTypes.OB_SURF: return "Surf";
	case ObjectTypes.OB_FONT: return "Font";
	case ObjectTypes.OB_MBALL: return "Mball";
	case ObjectTypes.OB_CAMERA: return "Camera";
	case ObjectTypes.OB_LAMP: return "Lamp";
	case ObjectTypes.OB_LATTICE: return "Lattice";
	case ObjectTypes.OB_WAVE: return "Wave";
	case ObjectTypes.OB_ARMATURE: return "Armature";
	case ObjectTypes.OB_EMPTY: return "Empty";
	default:
		System.out.printf("get_obdata_defname: Internal error, bad type: %d\n", type);
		return "Empty";
	}
}

/* more general add: creates minimum required data, but without vertices etc. */
public static bObject add_only_object(int type, byte[] name)
{
	bObject ob;

	ob= (bObject)LibraryUtil.alloc_libblock(G.main.object, DNA_ID.ID_OB, name,0);

	/* default object vars */
	ob.type= (short)type;
	/* ob.transflag= OB_QUAT; */

//#if 0 /* not used yet */
//	QuatOne(ob.quat);
//	QuatOne(ob.dquat);
//#endif

	ob.col[0]= ob.col[1]= ob.col[2]= 1.0f;
	ob.col[3]= 1.0f;

	ob.loc[0]= ob.loc[1]= ob.loc[2]= 0.0f;
	ob.rot[0]= ob.rot[1]= ob.rot[2]= 0.0f;
	ob.size[0]= ob.size[1]= ob.size[2]= 1.0f;

	Arithb.Mat4One(ob.constinv);
	Arithb.Mat4One(ob.parentinv);
	Arithb.Mat4One(ob.obmat);
	ob.dt= ObjectTypes.OB_SHADED;
	ob.empty_drawtype= ObjectTypes.OB_ARROWS;
	ob.empty_drawsize= 1.0f;

	if(type==ObjectTypes.OB_CAMERA || type==ObjectTypes.OB_LAMP) {
		ob.trackflag= ObjectTypes.OB_NEGZ;
		ob.upflag= ObjectTypes.OB_POSY;
	}
	else {
		ob.trackflag= ObjectTypes.OB_POSY;
		ob.upflag= ObjectTypes.OB_POSZ;
	}

//#if 0 // XXX old animation system
//	ob.ipoflag = OB_OFFS_OB+OB_OFFS_PARENT;
//	ob.ipowin= ID_OB;	/* the ipowin shown */
//#endif // XXX old animation system

	ob.dupon= 1; ob.dupoff= 0;
	ob.dupsta= 1; ob.dupend= 100;
	ob.dupfacesca = 1.0f;

	/* Game engine defaults*/
	ob.mass= ob.inertia= 1.0f;
	ob.formfactor= 0.4f;
	ob.damping= 0.04f;
	ob.rdamping= 0.1f;
	ob.anisotropicFriction[0] = 1.0f;
	ob.anisotropicFriction[1] = 1.0f;
	ob.anisotropicFriction[2] = 1.0f;
	ob.gameflag= ObjectTypes.OB_PROP|ObjectTypes.OB_COLLISION;
	ob.margin = 0.0f;
	/* ob.pad3 == Contact Processing Threshold */
	ob.m_contactProcessingThreshold = 1.0f;

	/* NT fluid sim defaults */
	//ob.fluidsimFlag = 0;
	ob.fluidsimSettings = null;

	return ob;
}

/* general add: to scene, with layer from area and default name */
/* creates minimum required data, but without vertices etc. */
public static bObject add_object(Scene scene, int type)
{
	bObject ob;
	Base base;
//	char name[32];
	byte[] name = new byte[32];

	StringUtil.strcpy(name,0, StringUtil.toCString(get_obdata_defname(type)),0);
	ob = add_only_object(type, name);

	ob.data= add_obdata_from_type(type);

	ob.lay= scene.lay;

	base= SceneUtil.scene_add_base(scene, ob);
	SceneUtil.scene_select_base(scene, base);
	ob.recalc |= ObjectTypes.OB_RECALC;

	return ob;
}

//SoftBody *copy_softbody(SoftBody *sb)
//{
//	SoftBody *sbn;
//
//	if (sb==NULL) return(NULL);
//
//	sbn= MEM_dupallocN(sb);
//	sbn.totspring= sbn.totpoint= 0;
//	sbn.bpoint= NULL;
//	sbn.bspring= NULL;
//
//	sbn.keys= NULL;
//	sbn.totkey= sbn.totpointkey= 0;
//
//	sbn.scratch= NULL;
//
//	sbn.pointcache= BKE_ptcache_copy(sb.pointcache);
//
//	return sbn;
//}
//
//BulletSoftBody *copy_bulletsoftbody(BulletSoftBody *bsb)
//{
//	BulletSoftBody *bsbn;
//
//	if (bsb == NULL)
//		return NULL;
//	bsbn = MEM_dupallocN(bsb);
//	/* no pointer in this structure yet */
//	return bsbn;
//}
//
//ParticleSystem *copy_particlesystem(ParticleSystem *psys)
//{
//	ParticleSystem *psysn;
//	ParticleData *pa;
//	int a;
//
//	psysn= MEM_dupallocN(psys);
//	psysn.particles= MEM_dupallocN(psys.particles);
//	psysn.child= MEM_dupallocN(psys.child);
//	if(psysn.particles.keys)
//		psysn.particles.keys = MEM_dupallocN(psys.particles.keys);
//
//	for(a=0, pa=psysn.particles; a<psysn.totpart; a++, pa++) {
//		if(pa.hair)
//			pa.hair= MEM_dupallocN(pa.hair);
//		if(a)
//			pa.keys= (pa-1).keys + (pa-1).totkey;
//	}
//
//	if(psys.soft) {
//		psysn.soft= copy_softbody(psys.soft);
//		psysn.soft.particles = psysn;
//	}
//
//	if(psys.particles.boid) {
//		psysn.particles.boid = MEM_dupallocN(psys.particles.boid);
//		for(a=1, pa=psysn.particles+1; a<psysn.totpart; a++, pa++)
//			pa.boid = (pa-1).boid + 1;
//	}
//
//	if(psys.targets.first)
//		BLI_duplicatelist(&psysn.targets, &psys.targets);
//
//	psysn.pathcache= NULL;
//	psysn.childcache= NULL;
//	psysn.edit= NULL;
//	psysn.effectors.first= psysn.effectors.last= 0;
//
//	psysn.pathcachebufs.first = psysn.pathcachebufs.last = NULL;
//	psysn.childcachebufs.first = psysn.childcachebufs.last = NULL;
//	psysn.reactevents.first = psysn.reactevents.last = NULL;
//	psysn.renderdata = NULL;
//
//	psysn.pointcache= BKE_ptcache_copy(psys.pointcache);
//
//	id_us_plus((ID *)psysn.part);
//
//	return psysn;
//}
//
//void copy_object_particlesystems(Object *obn, Object *ob)
//{
//	ParticleSystemModifierData *psmd;
//	ParticleSystem *psys, *npsys;
//	ModifierData *md;
//
//	obn.particlesystem.first= obn.particlesystem.last= NULL;
//	for(psys=ob.particlesystem.first; psys; psys=psys.next) {
//		npsys= copy_particlesystem(psys);
//
//		BLI_addtail(&obn.particlesystem, npsys);
//
//		/* need to update particle modifiers too */
//		for(md=obn.modifiers.first; md; md=md.next) {
//			if(md.type==eModifierType_ParticleSystem) {
//				psmd= (ParticleSystemModifierData*)md;
//				if(psmd.psys==psys)
//					psmd.psys= npsys;
//			}
//		}
//	}
//}
//
//void copy_object_softbody(Object *obn, Object *ob)
//{
//	if(ob.soft)
//		obn.soft= copy_softbody(ob.soft);
//}
//
//static void copy_object_pose(Object *obn, Object *ob)
//{
//	bPoseChannel *chan;
//
//	/* note: need to clear obn.pose pointer first, so that copy_pose works (otherwise there's a crash) */
//	obn.pose= NULL;
//	copy_pose(&obn.pose, ob.pose, 1);	/* 1 = copy constraints */
//
//	for (chan = obn.pose.chanbase.first; chan; chan=chan.next){
//		bConstraint *con;
//
//		chan.flag &= ~(POSE_LOC|POSE_ROT|POSE_SIZE);
//
//		for (con= chan.constraints.first; con; con= con.next) {
//			bConstraintTypeInfo *cti= constraint_get_typeinfo(con);
//			ListBase targets = {NULL, NULL};
//			bConstraintTarget *ct;
//
//#if 0 // XXX old animation system
//			/* note that we can't change lib linked ipo blocks. for making
//			 * proxies this still works correct however because the object
//			 * is changed to object.proxy_from when evaluating the driver. */
//			if(con.ipo && !con.ipo.id.lib) {
//				IpoCurve *icu;
//
//				con.ipo= copy_ipo(con.ipo);
//
//				for(icu= con.ipo.curve.first; icu; icu= icu.next) {
//					if(icu.driver && icu.driver.ob==ob)
//						icu.driver.ob= obn;
//				}
//			}
//#endif // XXX old animation system
//
//			if (cti && cti.get_constraint_targets) {
//				cti.get_constraint_targets(con, &targets);
//
//				for (ct= targets.first; ct; ct= ct.next) {
//					if (ct.tar == ob)
//						ct.tar = obn;
//				}
//
//				if (cti.flush_constraint_targets)
//					cti.flush_constraint_targets(con, &targets, 0);
//			}
//		}
//	}
//}
//
//Object *copy_object(Object *ob)
//{
//	Object *obn;
//	ModifierData *md;
//	int a;
//
//	obn= copy_libblock(ob);
//
//	if(ob.totcol) {
//		obn.mat= MEM_dupallocN(ob.mat);
//		obn.matbits= MEM_dupallocN(ob.matbits);
//	}
//
//	if(ob.bb) obn.bb= MEM_dupallocN(ob.bb);
//	obn.path= NULL;
//	obn.flag &= ~OB_FROMGROUP;
//
//	obn.modifiers.first = obn.modifiers.last= NULL;
//
//	for (md=ob.modifiers.first; md; md=md.next) {
//		ModifierData *nmd = modifier_new(md.type);
//		modifier_copyData(md, nmd);
//		BLI_addtail(&obn.modifiers, nmd);
//	}
//
//	obn.prop.first = obn.prop.last = NULL;
//	copy_properties(&obn.prop, &ob.prop);
//
//	copy_sensors(&obn.sensors, &ob.sensors);
//	copy_controllers(&obn.controllers, &ob.controllers);
//	copy_actuators(&obn.actuators, &ob.actuators);
//
//	if(ob.pose) {
//		copy_object_pose(obn, ob);
//		/* backwards compat... non-armatures can get poses in older files? */
//		if(ob.type==OB_ARMATURE)
//			armature_rebuild_pose(obn, obn.data);
//	}
//	copy_defgroups(&obn.defbase, &ob.defbase);
//	copy_constraints(&obn.constraints, &ob.constraints);
//
//	/* increase user numbers */
//	id_us_plus((ID *)obn.data);
//	id_us_plus((ID *)obn.dup_group);
//	// FIXME: add this for animdata too...
//
//	for(a=0; a<obn.totcol; a++) id_us_plus((ID *)obn.mat[a]);
//
//	obn.disp.first= obn.disp.last= NULL;
//
//	if(ob.pd){
//		obn.pd= MEM_dupallocN(ob.pd);
//		if(obn.pd.tex)
//			id_us_plus(&(obn.pd.tex.id));
//	}
//	obn.soft= copy_softbody(ob.soft);
//	obn.bsoft = copy_bulletsoftbody(ob.bsoft);
//
//	copy_object_particlesystems(obn, ob);
//
//	obn.derivedDeform = NULL;
//	obn.derivedFinal = NULL;
//
//	obn.gpulamp.first = obn.gpulamp.last = NULL;
//
//	return obn;
//}
//
//void expand_local_object(Object *ob)
//{
//	//bActionStrip *strip;
//	ParticleSystem *psys;
//	int a;
//
//#if 0 // XXX old animation system
//	id_lib_extern((ID *)ob.action);
//	id_lib_extern((ID *)ob.ipo);
//#endif // XXX old animation system
//	id_lib_extern((ID *)ob.data);
//	id_lib_extern((ID *)ob.dup_group);
//
//	for(a=0; a<ob.totcol; a++) {
//		id_lib_extern((ID *)ob.mat[a]);
//	}
//#if 0 // XXX old animation system
//	for (strip=ob.nlastrips.first; strip; strip=strip.next) {
//		id_lib_extern((ID *)strip.act);
//	}
//#endif // XXX old animation system
//	for(psys=ob.particlesystem.first; psys; psys=psys.next)
//		id_lib_extern((ID *)psys.part);
//}
//
//void make_local_object(Object *ob)
//{
//	Object *obn;
//	Scene *sce;
//	Base *base;
//	int local=0, lib=0;
//
//	/* - only lib users: do nothing
//	    * - only local users: set flag
//	    * - mixed: make copy
//	    */
//
//	if(ob.id.lib==NULL) return;
//
//	ob.proxy= ob.proxy_from= NULL;
//
//	if(ob.id.us==1) {
//		ob.id.lib= NULL;
//		ob.id.flag= LIB_LOCAL;
//		new_id(0, (ID *)ob, 0);
//
//	}
//	else {
//		sce= G.main.scene.first;
//		while(sce) {
//			base= sce.base.first;
//			while(base) {
//				if(base.object==ob) {
//					if(sce.id.lib) lib++;
//					else local++;
//					break;
//				}
//				base= base.next;
//			}
//			sce= sce.id.next;
//		}
//
//		if(local && lib==0) {
//			ob.id.lib= 0;
//			ob.id.flag= LIB_LOCAL;
//			new_id(0, (ID *)ob, 0);
//		}
//		else if(local && lib) {
//			obn= copy_object(ob);
//			obn.id.us= 0;
//
//			sce= G.main.scene.first;
//			while(sce) {
//				if(sce.id.lib==0) {
//					base= sce.base.first;
//					while(base) {
//						if(base.object==ob) {
//							base.object= obn;
//							obn.id.us++;
//							ob.id.us--;
//						}
//						base= base.next;
//					}
//				}
//				sce= sce.id.next;
//			}
//		}
//	}
//
//	expand_local_object(ob);
//}

/*
 * Returns true if the Object is a from an external blend file (libdata)
 */
public static boolean object_is_libdata(bObject ob)
{
	if (ob==null) return false;
	if (ob.proxy!=null) return false;
	if (ob.id.lib!=null) return true;
	return false;
}

/* Returns true if the Object data is a from an external blend file (libdata) */
public static boolean object_data_is_libdata(bObject ob)
{
	if(ob==null) return false;
	if(ob.proxy!=null) return false;
	if(ob.id.lib!=null) return true;
	if(ob.data==null) return false;
	if(((ID)ob.data).lib!=null) return true;

	return false;
}

///* *************** PROXY **************** */
//
///* when you make proxy, ensure the exposed layers are extern */
//void armature_set_id_extern(Object *ob)
//{
//	bArmature *arm= ob.data;
//	bPoseChannel *pchan;
//	int lay= arm.layer_protected;
//
//	for (pchan = ob.pose.chanbase.first; pchan; pchan=pchan.next) {
//		if(!(pchan.bone.layer & lay))
//			id_lib_extern((ID *)pchan.custom);
//	}
//
//}
//
///* proxy rule: lib_object.proxy_from == the one we borrow from, set temporally while object_update */
///*             local_object.proxy == pointer to library object, saved in files and read */
///*             local_object.proxy_group == pointer to group dupli-object, saved in files and read */
//
//void object_make_proxy(Object *ob, Object *target, Object *gob)
//{
//	/* paranoia checks */
//	if(ob.id.lib || target.id.lib==NULL) {
//		printf("cannot make proxy\n");
//		return;
//	}
//
//	ob.proxy= target;
//	ob.proxy_group= gob;
//	id_lib_extern(&target.id);
//
//	ob.recalc= target.recalc= OB_RECALC;
//
//	/* copy transform */
//	if(gob) {
//		VECCOPY(ob.loc, gob.loc);
//		VECCOPY(ob.rot, gob.rot);
//		VECCOPY(ob.size, gob.size);
//
//		group_tag_recalc(gob.dup_group);
//	}
//	else {
//		VECCOPY(ob.loc, target.loc);
//		VECCOPY(ob.rot, target.rot);
//		VECCOPY(ob.size, target.size);
//	}
//
//	ob.parent= target.parent;	/* libdata */
//	Mat4CpyMat4(ob.parentinv, target.parentinv);
//#if 0 // XXX old animation system
//	ob.ipo= target.ipo;		/* libdata */
//#endif // XXX old animation system
//
//	/* skip constraints, constraintchannels, nla? */
//
//	/* set object type and link to data */
//	ob.type= target.type;
//	ob.data= target.data;
//	id_us_plus((ID *)ob.data);		/* ensures lib data becomes LIB_EXTERN */
//
//	/* copy material and index information */
//	ob.actcol= ob.totcol= 0;
//	if(ob.mat) MEM_freeN(ob.mat);
//	if(ob.matbits) MEM_freeN(ob.matbits);
//	ob.mat = NULL;
//	ob.matbits= NULL;
//	if ((target.totcol) && (target.mat) && ELEM5(ob.type, OB_MESH, OB_CURVE, OB_SURF, OB_FONT, OB_MBALL)) { //XXX OB_SUPPORT_MATERIAL
//		int i;
//		ob.colbits = target.colbits;
//
//		ob.actcol= target.actcol;
//		ob.totcol= target.totcol;
//
//		ob.mat = MEM_dupallocN(target.mat);
//		ob.matbits = MEM_dupallocN(target.matbits);
//		for(i=0; i<target.totcol; i++) {
//			/* dont need to run test_object_materials since we know this object is new and not used elsewhere */
//			id_us_plus((ID *)ob.mat[i]);
//		}
//	}
//
//	/* type conversions */
//	if(target.type == OB_ARMATURE) {
//		copy_object_pose(ob, target);	/* data copy, object pointers in constraints */
//		rest_pose(ob.pose);			/* clear all transforms in channels */
//		armature_rebuild_pose(ob, ob.data);	/* set all internal links */
//
//		armature_set_id_extern(ob);
//	}
//}
//
//
///* *************** CALC ****************** */
//
///* there is also a timing calculation in drawobject() */
//
//float bluroffs= 0.0f, fieldoffs= 0.0f;
//int no_speed_curve= 0;
//
///* ugly calls from render */
//void set_mblur_offs(float blur)
//{
//	bluroffs= blur;
//}
//
//void set_field_offs(float field)
//{
//	fieldoffs= field;
//}
//
//void disable_speed_curve(int val)
//{
//	no_speed_curve= val;
//}
//
//// XXX THIS CRUFT NEEDS SERIOUS RECODING ASAP!
///* ob can be NULL */
//float bsystem_time(struct Scene *scene, Object *ob, float cfra, float ofs)
//{
//	/* returns float ( see frame_to_float in ipo.c) */
//
//	/* bluroffs and fieldoffs are ugly globals that are set by render */
//	cfra+= bluroffs+fieldoffs;
//
//	/* global time */
//	cfra*= scene.r.framelen;
//
//#if 0 // XXX old animation system
//	if (ob) {
//		if (no_speed_curve==0 && ob.ipo)
//			cfra= calc_ipo_time(ob.ipo, cfra);
//
//		/* ofset frames */
//		if ((ob.ipoflag & OB_OFFS_PARENT) && (ob.partype & PARSLOW)==0)
//			cfra-= give_timeoffset(ob);
//	}
//#endif // XXX old animation system
//
//	cfra-= ofs;
//
//	return cfra;
//}

    public static void object_scale_to_mat3(bObject ob, float[][] mat) {
//	float vec[3];
//	if(ob.ipo!=null) {
//		vec[0]= ob.size[0]+ob.dsize[0];
//		vec[1]= ob.size[1]+ob.dsize[1];
//		vec[2]= ob.size[2]+ob.dsize[2];
//		SizeToMat3(vec, mat);
//	}
//	else {
        Arithb.SizeToMat3(ob.size, mat);
//	}
    }

    public static void object_rot_to_mat3(bObject ob, float[][] mat) {
//	float vec[3];
//	if(ob.ipo) {
//		vec[0]= ob.rot[0]+ob.drot[0];
//		vec[1]= ob.rot[1]+ob.drot[1];
//		vec[2]= ob.rot[2]+ob.drot[2];
//		EulToMat3(vec, mat);
//	}
//	else {
        Arithb.EulToMat3(ob.rot, mat);
//	}
    }

    public static void object_to_mat3(bObject ob, float[][] mat) /* no parent */ {
        float[][] smat = new float[3][3];
        float[][] rmat = new float[3][3];
//	/*float q1[4];*/

        /* size */
        object_scale_to_mat3(ob, smat);

        /* rot */
        /* Quats arnt used yet */
        /*if(ob.transflag & OB_QUAT) {
            if(ob.ipo) {
                QuatMul(q1, ob.quat, ob.dquat);
                QuatToMat3(q1, rmat);
            }
            else {
                QuatToMat3(ob.quat, rmat);
            }
        }
        else {*/
        object_rot_to_mat3(ob, rmat);
        /*}*/
        Arithb.Mat3MulMat3(mat, rmat, smat);
    }

    public static void object_to_mat4(bObject ob, float[][] mat) {
        float[][] tmat = new float[3][3];

        object_to_mat3(ob, tmat);

        Arithb.Mat4CpyMat3(mat, tmat);

        UtilDefines.VECCOPY(mat[3], ob.loc);
//	if(ob.ipo!=null) {
//		mat[3][0]+= ob.dloc[0];
//		mat[3][1]+= ob.dloc[1];
//		mat[3][2]+= ob.dloc[2];
//	}
    }

//int enable_cu_speed= 1;
//
//static void ob_parcurve(Scene *scene, Object *ob, Object *par, float mat[][4])
//{
//	Curve *cu;
//	float q[4], vec[4], dir[3], quat[4], x1, ctime;
//	float timeoffs = 0.0, sf_orig = 0.0;
//
//	Mat4One(mat);
//
//	cu= par.data;
//	if(cu.path==NULL || cu.path.data==NULL) /* only happens on reload file, but violates depsgraph still... fix! */
//		makeDispListCurveTypes(scene, par, 0);
//	if(cu.path==NULL) return;
//
//	/* exception, timeoffset is regarded as distance offset */
//	if(cu.flag & CU_OFFS_PATHDIST) {
//		timeoffs = give_timeoffset(ob);
//		SWAP(float, sf_orig, ob.sf);
//	}
//
//	/* catch exceptions: feature for nla stride editing */
//	if(ob.ipoflag & OB_DISABLE_PATH) {
//		ctime= 0.0f;
//	}
//	/* catch exceptions: curve paths used as a duplicator */
//	else if(enable_cu_speed) {
//		/* ctime is now a proper var setting of Curve which gets set by Animato like any other var that's animated,
//		 * but this will only work if it actually is animated...
//		 *
//		 * we firstly calculate the modulus of cu.ctime/cu.pathlen to clamp ctime within the 0.0 to 1.0 times pathlen
//		 * range, then divide this (the modulus) by pathlen to get a value between 0.0 and 1.0
//		 */
//		ctime= fmod(cu.ctime, cu.pathlen) / cu.pathlen;
//		CLAMP(ctime, 0.0, 1.0);
//	}
//	else {
//		ctime= scene.r.cfra - give_timeoffset(ob);
//		ctime /= cu.pathlen;
//
//		CLAMP(ctime, 0.0, 1.0);
//	}
//
//	/* time calculus is correct, now apply distance offset */
//	if(cu.flag & CU_OFFS_PATHDIST) {
//		ctime += timeoffs/cu.path.totdist;
//
//		/* restore */
//		SWAP(float, sf_orig, ob.sf);
//	}
//
//
//	/* vec: 4 items! */
// 	if( where_on_path(par, ctime, vec, dir) ) {
//
//		if(cu.flag & CU_FOLLOW) {
//			vectoquat(dir, ob.trackflag, ob.upflag, quat);
//
//			/* the tilt */
//			Normalize(dir);
//			q[0]= (float)cos(0.5*vec[3]);
//			x1= (float)sin(0.5*vec[3]);
//			q[1]= -x1*dir[0];
//			q[2]= -x1*dir[1];
//			q[3]= -x1*dir[2];
//			QuatMul(quat, q, quat);
//
//			QuatToMat4(quat, mat);
//		}
//
//		VECCOPY(mat[3], vec);
//
//	}
//}
//
//static void ob_parbone(Object *ob, Object *par, float mat[][4])
//{
//	bPoseChannel *pchan;
//	float vec[3];
//
//	if (par.type!=OB_ARMATURE) {
//		Mat4One(mat);
//		return;
//	}
//
//	/* Make sure the bone is still valid */
//	pchan= get_pose_channel(par.pose, ob.parsubstr);
//	if (!pchan){
//		printf ("Object %s with Bone parent: bone %s doesn't exist\n", ob.id.name+2, ob.parsubstr);
//		Mat4One(mat);
//		return;
//	}
//
//	/* get bone transform */
//	Mat4CpyMat4(mat, pchan.pose_mat);
//
//	/* but for backwards compatibility, the child has to move to the tail */
//	VECCOPY(vec, mat[1]);
//	VecMulf(vec, pchan.bone.length);
//	VecAddf(mat[3], mat[3], vec);
//}
//
//static void give_parvert(Object *par, int nr, float *vec)
//{
//	EditMesh *em;
//	int a, count;
//
//	vec[0]=vec[1]=vec[2]= 0.0f;
//
//	if(par.type==OB_MESH) {
//		Mesh *me= par.data;
//		em = BKE_mesh_get_editmesh(me);
//
//		if(em) {
//			EditVert *eve;
//
//			for(eve= em.verts.first; eve; eve= eve.next) {
//				if(eve.keyindex==nr) {
//					memcpy(vec, eve.co, sizeof(float)*3);
//					break;
//				}
//			}
//			BKE_mesh_end_editmesh(me, em);
//		}
//		else {
//			DerivedMesh *dm = par.derivedFinal;
//
//			if(dm) {
//				int i, count = 0, numVerts = dm.getNumVerts(dm);
//				int *index = (int *)dm.getVertDataArray(dm, CD_ORIGINDEX);
//				float co[3];
//
//				/* get the average of all verts with (original index == nr) */
//				for(i = 0; i < numVerts; ++i, ++index) {
//					if(*index == nr) {
//						dm.getVertCo(dm, i, co);
//						VecAddf(vec, vec, co);
//						count++;
//					}
//				}
//
//				if (count==0) {
//					/* keep as 0,0,0 */
//				} else if(count > 0) {
//					VecMulf(vec, 1.0f / count);
//				} else {
//					/* use first index if its out of range */
//					dm.getVertCo(dm, 0, vec);
//				}
//			}
//		}
//	}
//	else if (ELEM(par.type, OB_CURVE, OB_SURF)) {
//		Nurb *nu;
//		Curve *cu;
//		BPoint *bp;
//		BezTriple *bezt;
//		int found= 0;
//
//		cu= par.data;
//		if(cu.editnurb)
//			nu= cu.editnurb.first;
//		else
//			nu= cu.nurb.first;
//
//		count= 0;
//		while(nu && !found) {
//			if((nu.type & 7)==CU_BEZIER) {
//				bezt= nu.bezt;
//				a= nu.pntsu;
//				while(a--) {
//					if(count==nr) {
//						found= 1;
//						VECCOPY(vec, bezt.vec[1]);
//						break;
//					}
//					count++;
//					bezt++;
//				}
//			}
//			else {
//				bp= nu.bp;
//				a= nu.pntsu*nu.pntsv;
//				while(a--) {
//					if(count==nr) {
//						found= 1;
//						memcpy(vec, bp.vec, sizeof(float)*3);
//						break;
//					}
//					count++;
//					bp++;
//				}
//			}
//			nu= nu.next;
//		}
//
//	}
//	else if(par.type==OB_LATTICE) {
//		Lattice *latt= par.data;
//		BPoint *bp;
//		DispList *dl = find_displist(&par.disp, DL_VERTS);
//		float *co = dl?dl.verts:NULL;
//
//		if(latt.editlatt) latt= latt.editlatt;
//
//		a= latt.pntsu*latt.pntsv*latt.pntsw;
//		count= 0;
//		bp= latt.def;
//		while(a--) {
//			if(count==nr) {
//				if(co)
//					memcpy(vec, co, 3*sizeof(float));
//				else
//					memcpy(vec, bp.vec, 3*sizeof(float));
//				break;
//			}
//			count++;
//			if(co) co+= 3;
//			else bp++;
//		}
//	}
//}
//
//static void ob_parvert3(Object *ob, Object *par, float mat[][4])
//{
//	float cmat[3][3], v1[3], v2[3], v3[3], q[4];
//
//	/* in local ob space */
//	Mat4One(mat);
//
//	if (ELEM4(par.type, OB_MESH, OB_SURF, OB_CURVE, OB_LATTICE)) {
//
//		give_parvert(par, ob.par1, v1);
//		give_parvert(par, ob.par2, v2);
//		give_parvert(par, ob.par3, v3);
//
//		triatoquat(v1, v2, v3, q);
//		QuatToMat3(q, cmat);
//		Mat4CpyMat3(mat, cmat);
//
//		if(ob.type==OB_CURVE) {
//			VECCOPY(mat[3], v1);
//		}
//		else {
//			VecAddf(mat[3], v1, v2);
//			VecAddf(mat[3], mat[3], v3);
//			VecMulf(mat[3], 0.3333333f);
//		}
//	}
//}
//
//// XXX what the hell is this?
//static int no_parent_ipo=0;
//void set_no_parent_ipo(int val)
//{
//	no_parent_ipo= val;
//}

public static void where_is_object_time(Scene scene, bObject ob, float ctime)
{
//	float *fp1, *fp2, slowmat[4][4] = MAT4_UNITY;
//	float stime=ctime, fac1, fac2, vec[3];
    float[] vec = new float[3];
//	int a;
//	int pop;
//
//	/* new version: correct parent+vertexparent and track+parent */
//	/* this one only calculates direct attached parent and track */
//	/* is faster, but should keep track of timeoffs */

	if(ob==null) return;

//	/* execute drivers only, as animation has already been done */
//	BKE_animsys_evaluate_animdata(&ob.id, ob.adt, ctime, ADT_RECALC_DRIVERS);

	if(ob.parent!=null) {
//		Object *par= ob.parent;
//
//		// XXX depreceated - animsys
//		if(ob.ipoflag & OB_OFFS_PARENT) ctime-= give_timeoffset(ob);
//
//		/* hurms, code below conflicts with depgraph... (ton) */
//		/* and even worse, it gives bad effects for NLA stride too (try ctime != par.ctime, with MBlur) */
//		pop= 0;
//		if(no_parent_ipo==0 && stime != par.ctime) {
//			// only for ipo systems?
//			pushdata(par, sizeof(Object));
//			pop= 1;
//
//			if(par.proxy_from);	// was a copied matrix, no where_is! bad...
//			else where_is_object_time(scene, par, ctime);
//		}
//
//		solve_parenting(scene, ob, par, ob.obmat, slowmat, 0);
//
//		if(pop) {
//			poplast(par);
//		}
//
//		if(ob.partype & PARSLOW) {
//			// include framerate
//			fac1= ( 1.0f / (1.0f + (float)fabs(give_timeoffset(ob))) );
//			if(fac1 >= 1.0f) return;
//			fac2= 1.0f-fac1;
//
//			fp1= ob.obmat[0];
//			fp2= slowmat[0];
//			for(a=0; a<16; a++, fp1++, fp2++) {
//				fp1[0]= fac1*fp1[0] + fac2*fp2[0];
//			}
//		}
	}
	else {
		object_to_mat4(ob, ob.obmat);
	}

//	/* Handle tracking */
//	if(ob.track) {
//		if( ctime != ob.track.ctime) where_is_object_time(scene, ob.track, ctime);
//		solve_tracking (ob, ob.track.obmat);
//	}
//
//	/* solve constraints */
//	if (ob.constraints.first) {
//		bConstraintOb *cob;
//
//		cob= constraints_make_evalob(scene, ob, NULL, CONSTRAINT_OBTYPE_OBJECT);
//
//		/* constraints need ctime, not stime. Some call where_is_object_time and bsystem_time */
//		solve_constraints (&ob.constraints, cob, ctime);
//
//		constraints_clear_evalob(cob);
//	}

	/* set negative scale flag in object */
	Arithb.Crossf(vec, ob.obmat[0], ob.obmat[1]);
	if( Arithb.Inpf(vec, ob.obmat[2]) < 0.0f ) {
            ob.transflag |= ObjectTypes.OB_NEG_SCALE;
        }
	else {
            ob.transflag &= ~ObjectTypes.OB_NEG_SCALE;
        }
}

//static void solve_parenting (Scene *scene, Object *ob, Object *par, float obmat[][4], float slowmat[][4], int simul)
//{
//	float totmat[4][4];
//	float tmat[4][4];
//	float locmat[4][4];
//	float vec[3];
//	int ok;
//
//	object_to_mat4(ob, locmat);
//
//	if(ob.partype & PARSLOW) Mat4CpyMat4(slowmat, obmat);
//
//	switch(ob.partype & PARTYPE) {
//	case PAROBJECT:
//		ok= 0;
//		if(par.type==OB_CURVE) {
//			if( ((Curve *)par.data).flag & CU_PATH ) {
//				ob_parcurve(scene, ob, par, tmat);
//				ok= 1;
//			}
//		}
//
//		if(ok) Mat4MulSerie(totmat, par.obmat, tmat,
//			NULL, NULL, NULL, NULL, NULL, NULL);
//		else Mat4CpyMat4(totmat, par.obmat);
//
//		break;
//	case PARBONE:
//		ob_parbone(ob, par, tmat);
//		Mat4MulSerie(totmat, par.obmat, tmat,
//			NULL, NULL, NULL, NULL, NULL, NULL);
//		break;
//
//	case PARVERT1:
//		Mat4One(totmat);
//		if (simul){
//			VECCOPY(totmat[3], par.obmat[3]);
//		}
//		else{
//			give_parvert(par, ob.par1, vec);
//			VecMat4MulVecfl(totmat[3], par.obmat, vec);
//		}
//		break;
//	case PARVERT3:
//		ob_parvert3(ob, par, tmat);
//
//		Mat4MulSerie(totmat, par.obmat, tmat,
//			NULL, NULL, NULL, NULL, NULL, NULL);
//		break;
//
//	case PARSKEL:
//		Mat4CpyMat4(totmat, par.obmat);
//		break;
//	}
//
//	// total
//	Mat4MulSerie(tmat, totmat, ob.parentinv,
//		NULL, NULL, NULL, NULL, NULL, NULL);
//	Mat4MulSerie(obmat, tmat, locmat,
//		NULL, NULL, NULL, NULL, NULL, NULL);
//
//	if (simul) {
//
//	}
//	else{
//		// external usable originmat
//		Mat3CpyMat4(originmat, tmat);
//
//		// origin, voor help line
//		if( (ob.partype & 15)==PARSKEL ) {
//			VECCOPY(ob.orig, par.obmat[3]);
//		}
//		else {
//			VECCOPY(ob.orig, totmat[3]);
//		}
//	}
//
//}
//void solve_tracking (Object *ob, float targetmat[][4])
//{
//	float quat[4];
//	float vec[3];
//	float totmat[3][3];
//	float tmat[4][4];
//
//	VecSubf(vec, ob.obmat[3], targetmat[3]);
//	vectoquat(vec, ob.trackflag, ob.upflag, quat);
//	QuatToMat3(quat, totmat);
//
//	if(ob.parent && (ob.transflag & OB_POWERTRACK)) {
//		/* 'temporal' : clear parent info */
//		object_to_mat4(ob, tmat);
//		tmat[0][3]= ob.obmat[0][3];
//		tmat[1][3]= ob.obmat[1][3];
//		tmat[2][3]= ob.obmat[2][3];
//		tmat[3][0]= ob.obmat[3][0];
//		tmat[3][1]= ob.obmat[3][1];
//		tmat[3][2]= ob.obmat[3][2];
//		tmat[3][3]= ob.obmat[3][3];
//	}
//	else Mat4CpyMat4(tmat, ob.obmat);
//
//	Mat4MulMat34(ob.obmat, totmat, tmat);
//
//}

    public static void where_is_object(Scene scene, bObject ob) {
        where_is_object_time(scene, ob, (float) scene.r.cfra);
    }

//void where_is_object_simul(Scene *scene, Object *ob)
///* was written for the old game engine (until 2.04) */
///* It seems that this function is only called
//for a lamp that is the child of another object */
//{
//	Object *par;
//	//Ipo *ipo;
//	float *fp1, *fp2;
//	float slowmat[4][4];
//	float fac1, fac2;
//	int a;
//
//	/* NO TIMEOFFS */
//
//	/* no ipo! (because of dloc and realtime-ipos) */
//		// XXX old animation system
//	//ipo= ob.ipo;
//	//ob.ipo= NULL;
//
//	if(ob.parent) {
//		par= ob.parent;
//
//		solve_parenting(scene, ob, par, ob.obmat, slowmat, 1);
//
//		if(ob.partype & PARSLOW) {
//
//			fac1= (float)(1.0/(1.0+ fabs(give_timeoffset(ob))));
//			fac2= 1.0f-fac1;
//			fp1= ob.obmat[0];
//			fp2= slowmat[0];
//			for(a=0; a<16; a++, fp1++, fp2++) {
//				fp1[0]= fac1*fp1[0] + fac2*fp2[0];
//			}
//		}
//
//	}
//	else {
//		object_to_mat4(ob, ob.obmat);
//	}
//
//	if(ob.track)
//		solve_tracking(ob, ob.track.obmat);
//
//	/* solve constraints */
//	if (ob.constraints.first) {
//		bConstraintOb *cob;
//
//		cob= constraints_make_evalob(scene, ob, NULL, CONSTRAINT_OBTYPE_OBJECT);
//		solve_constraints(&ob.constraints, cob, (float)scene.r.cfra);
//		constraints_clear_evalob(cob);
//	}
//
//	/*  WATCH IT!!! */
//		// XXX old animation system
//	//ob.ipo= ipo;
//}
//
///* for calculation of the inverse parent transform, only used for editor */
//void what_does_parent(Scene *scene, Object *ob, Object *workob)
//{
//	clear_workob(workob);
//
//	Mat4One(workob.obmat);
//	Mat4One(workob.parentinv);
//	Mat4One(workob.constinv);
//	workob.parent= ob.parent;
//	workob.track= ob.track;
//
//	workob.trackflag= ob.trackflag;
//	workob.upflag= ob.upflag;
//
//	workob.partype= ob.partype;
//	workob.par1= ob.par1;
//	workob.par2= ob.par2;
//	workob.par3= ob.par3;
//
//	workob.constraints.first = ob.constraints.first;
//	workob.constraints.last = ob.constraints.last;
//
//	strcpy(workob.parsubstr, ob.parsubstr);
//
//	where_is_object(scene, workob);
//}

public static BoundBox unit_boundbox()
{
	BoundBox bb;
	float[] min = {-1.0f,-1.0f,-1.0f}, max = {-1.0f,-1.0f,-1.0f};

//	bb= MEM_callocN(sizeof(BoundBox), "bb");
	bb= new BoundBox();
	boundbox_set_from_min_max(bb, min, max);

	return bb;
}

public static void boundbox_set_from_min_max(BoundBox bb, float[] min, float[] max)
{
	bb.vec[0][0]=bb.vec[1][0]=bb.vec[2][0]=bb.vec[3][0]= min[0];
	bb.vec[4][0]=bb.vec[5][0]=bb.vec[6][0]=bb.vec[7][0]= max[0];

	bb.vec[0][1]=bb.vec[1][1]=bb.vec[4][1]=bb.vec[5][1]= min[1];
	bb.vec[2][1]=bb.vec[3][1]=bb.vec[6][1]=bb.vec[7][1]= max[1];

	bb.vec[0][2]=bb.vec[3][2]=bb.vec[4][2]=bb.vec[7][2]= min[2];
	bb.vec[1][2]=bb.vec[2][2]=bb.vec[5][2]=bb.vec[6][2]= max[2];
}

public static BoundBox object_get_boundbox(bObject ob)
{
	BoundBox bb= null;

	if(ob.type==ObjectTypes.OB_MESH) {
		bb = MeshUtil.mesh_get_bb(ob);
	}
	else if (ob.type==ObjectTypes.OB_CURVE || ob.type==ObjectTypes.OB_SURF || ob.type==ObjectTypes.OB_FONT) {
		bb= ((Curve)ob.data).bb;
	}
	else if(ob.type==ObjectTypes.OB_MBALL) {
		bb= ob.bb;
	}
	return bb;
}

///* used to temporally disable/enable boundbox */
//void object_boundbox_flag(Object *ob, int flag, int set)
//{
//	BoundBox *bb= object_get_boundbox(ob);
//	if(bb) {
//		if(set) bb.flag |= flag;
//		else bb.flag &= ~flag;
//	}
//}

public static void minmax_object(bObject ob, float[] min, float[] max)
{
	BoundBox bb = new BoundBox();
	Mesh me;
	Curve cu;
	float[] vec=new float[3];
	int a;

	switch(ob.type) {

	case ObjectTypes.OB_CURVE:
	case ObjectTypes.OB_FONT:
	case ObjectTypes.OB_SURF:
//		cu= (Curve)ob.data;
//
//		if(cu.bb==null) tex_space_curve(cu);
////		bb= cu.bb.copy();
//                bb= boundBoxCopy(cu.bb);
//
//		for(a=0; a<8; a++) {
//			Arithb.Mat4MulVecfl(ob.obmat, bb.vec[a]);
//			UtilDefines.DO_MINMAX(bb.vec[a], min, max);
//		}
		break;
	case ObjectTypes.OB_ARMATURE:
//		if(ob.pose) {
//			bPoseChannel *pchan;
//			for(pchan= ob.pose.chanbase.first; pchan; pchan= pchan.next) {
//				VECCOPY(vec, pchan.pose_head);
//				Mat4MulVecfl(ob.obmat, vec);
//				DO_MINMAX(vec, min, max);
//				VECCOPY(vec, pchan.pose_tail);
//				Mat4MulVecfl(ob.obmat, vec);
//				DO_MINMAX(vec, min, max);
//			}
//			break;
//		}
		/* no break, get_mesh will give NULL and it passes on to default */
	case ObjectTypes.OB_MESH:
		me= MeshUtil.get_mesh(ob);

		if(me!=null) {
//			bb = MeshUtil.mesh_get_bb(ob).copy();
                        bb = boundBoxCopy(MeshUtil.mesh_get_bb(ob));

			for(a=0; a<8; a++) {
				Arithb.Mat4MulVecfl(ob.obmat, bb.vec[a]);
				UtilDefines.DO_MINMAX(bb.vec[a], min, max);
			}
		}
		if(min[0] < max[0] ) break;

		/* else here no break!!!, mesh can be zero sized */

	default:
		UtilDefines.DO_MINMAX(ob.obmat[3], min, max);

		UtilDefines.VECCOPY(vec, ob.obmat[3]);
		Arithb.VecAddf(vec, vec, ob.size);
		UtilDefines.DO_MINMAX(vec, min, max);

		UtilDefines.VECCOPY(vec, ob.obmat[3]);
		Arithb.VecSubf(vec, vec, ob.size);
		UtilDefines.DO_MINMAX(vec, min, max);
		break;
	}
}

///* TODO - use dupli objects bounding boxes */
//void minmax_object_duplis(Scene *scene, Object *ob, float *min, float *max)
//{
//	if ((ob.transflag & OB_DUPLI)==0) {
//		return;
//	} else {
//		ListBase *lb;
//		DupliObject *dob;
//
//		lb= object_duplilist(scene, ob);
//		for(dob= lb.first; dob; dob= dob.next) {
//			if(dob.no_draw);
//			else {
//				/* should really use bound box of dup object */
//				DO_MINMAX(dob.mat[3], min, max);
//			}
//		}
//		free_object_duplilist(lb);	/* does restore */
//	}
//}


/* proxy rule: lib_object.proxy_from == the one we borrow from, only set temporal and cleared here */
/*           local_object.proxy      == pointer to library object, saved in files and read */

/* function below is polluted with proxy exceptions, cleanup will follow! */

/* the main object update call, for object matrix, constraints, keys and displist (modifiers) */
/* requires flags to be set! */
public static void object_handle_update(Scene scene, bObject ob)
{
	if((ob.recalc & ObjectTypes.OB_RECALC)!=0) {

		/* XXX new animsys warning: depsgraph tag OB_RECALC_DATA should not skip drivers,
		   which is only in where_is_object now */
		if((ob.recalc & ObjectTypes.OB_RECALC)!=0) {

//			if (G.f & G_DEBUG)
//				printf("recalcob %s\n", ob.id.name+2);
//
//			/* handle proxy copy for target */
//			if(ob.id.lib && ob.proxy_from) {
//				// printf("ob proxy copy, lib ob %s proxy %s\n", ob.id.name, ob.proxy_from.id.name);
//				if(ob.proxy_from.proxy_group) {/* transform proxy into group space */
//					Object *obg= ob.proxy_from.proxy_group;
//					Mat4Invert(obg.imat, obg.obmat);
//					Mat4MulMat4(ob.obmat, ob.proxy_from.obmat, obg.imat);
//				}
//				else
//					Mat4CpyMat4(ob.obmat, ob.proxy_from.obmat);
//			}
//			else
				where_is_object(scene, ob);
		}

		if((ob.recalc & ObjectTypes.OB_RECALC_DATA)!=0) {

//			if (G.f & G_DEBUG)
//				printf("recalcdata %s\n", ob.id.name+2);
//
//			/* includes all keys and modifiers */
			if(ob.type==ObjectTypes.OB_MESH) {
//				EditMesh *em = BKE_mesh_get_editmesh(ob.data);
//
//					// here was vieweditdatamask? XXX
//				if(ob==scene.obedit) {
//					makeDerivedMesh(scene, ob, em, CD_MASK_BAREMESH);
//					BKE_mesh_end_editmesh(ob.data, em);
//				} else
//					makeDerivedMesh(scene, ob, NULL, CD_MASK_BAREMESH);
			}
//			else if(ob.type==OB_MBALL) {
//				makeDispListMBall(scene, ob);
//			}
//			else if(ELEM3(ob.type, OB_CURVE, OB_SURF, OB_FONT)) {
//				makeDispListCurveTypes(scene, ob, 0);
//			}
//			else if(ELEM(ob.type, OB_CAMERA, OB_LAMP)) {
//				ID *data_id= (ID *)ob.data;
//				AnimData *adt= BKE_animdata_from_id(data_id);
//				float ctime= (float)scene.r.cfra; // XXX this is bad...
//
//				/* evaluate drivers */
//				BKE_animsys_evaluate_animdata(data_id, adt, ctime, ADT_RECALC_DRIVERS);
//			}
//			else if(ob.type==OB_LATTICE) {
//				lattice_calc_modifiers(scene, ob);
//			}
//			else if(ob.type==OB_ARMATURE) {
//				/* this happens for reading old files and to match library armatures with poses */
//				// XXX this won't screw up the pose set already...
//				if(ob.pose==NULL || (ob.pose.flag & POSE_RECALC))
//					armature_rebuild_pose(ob, ob.data);
//
//				if(ob.id.lib && ob.proxy_from) {
//					copy_pose_result(ob.pose, ob.proxy_from.pose);
//					// printf("pose proxy copy, lib ob %s proxy %s\n", ob.id.name, ob.proxy_from.id.name);
//				}
//				else {
//					where_is_pose(scene, ob);
//				}
//			}
//
//			if(ob.particlesystem.first) {
//				ParticleSystem *tpsys, *psys;
//				DerivedMesh *dm;
//				ob.transflag &= ~OB_DUPLIPARTS;
//
//				psys= ob.particlesystem.first;
//				while(psys) {
//					if(psys_check_enabled(ob, psys)) {
//						/* check use of dupli objects here */
//						if(psys.part && psys.part.draw_as == PART_DRAW_REND &&
//							((psys.part.ren_as == PART_DRAW_OB && psys.part.dup_ob)
//							|| (psys.part.ren_as == PART_DRAW_GR && psys.part.dup_group)))
//							ob.transflag |= OB_DUPLIPARTS;
//
//						particle_system_update(scene, ob, psys);
//						psys= psys.next;
//					}
//					else if(psys.flag & PSYS_DELETE) {
//						tpsys=psys.next;
//						BLI_remlink(&ob.particlesystem, psys);
//						psys_free(ob,psys);
//						psys= tpsys;
//					}
//					else
//						psys= psys.next;
//				}
//
//				if(G.rendering && ob.transflag & OB_DUPLIPARTS) {
//					/* this is to make sure we get render level duplis in groups:
//					 * the derivedmesh must be created before init_render_mesh,
//					 * since object_duplilist does dupliparticles before that */
//					dm = mesh_create_derived_render(scene, ob, CD_MASK_BAREMESH|CD_MASK_MTFACE|CD_MASK_MCOL);
//					dm.release(dm);
//
//					for(psys=ob.particlesystem.first; psys; psys=psys.next)
//						psys_get_modifier(ob, psys).flag &= ~eParticleSystemFlag_psys_updated;
//				}
//			}
		}

//		/* the no-group proxy case, we call update */
//		if(ob.proxy && ob.proxy_group==NULL) {
//			/* set pointer in library proxy target, for copying, but restore it */
//			ob.proxy.proxy_from= ob;
//			// printf("call update, lib ob %s proxy %s\n", ob.proxy.id.name, ob.id.name);
//			object_handle_update(scene, ob.proxy);
//		}

		ob.recalc &= ~ObjectTypes.OB_RECALC;
	}

//	/* the case when this is a group proxy, object_update is called in group.c */
//	if(ob.proxy) {
//		ob.proxy.proxy_from= ob;
//		// printf("set proxy pointer for later group stuff %s\n", ob.id.name);
//	}
}

//float give_timeoffset(Object *ob) {
//	if ((ob.ipoflag & OB_OFFS_PARENTADD) && ob.parent) {
//		return ob.sf + give_timeoffset(ob.parent);
//	} else {
//		return ob.sf;
//	}
//}
//
//int give_obdata_texspace(Object *ob, int **texflag, float **loc, float **size, float **rot) {
//
//	if (ob.data==NULL)
//		return 0;
//
//	switch (GS(((ID *)ob.data).name)) {
//	case ID_ME:
//	{
//		Mesh *me= ob.data;
//		if (texflag)	*texflag = &me.texflag;
//		if (loc)		*loc = me.loc;
//		if (size)		*size = me.size;
//		if (rot)		*rot = me.rot;
//		break;
//	}
//	case ID_CU:
//	{
//		Curve *cu= ob.data;
//		if (texflag)	*texflag = &cu.texflag;
//		if (loc)		*loc = cu.loc;
//		if (size)		*size = cu.size;
//		if (rot)		*rot = cu.rot;
//		break;
//	}
//	case ID_MB:
//	{
//		MetaBall *mb= ob.data;
//		if (texflag)	*texflag = &mb.texflag;
//		if (loc)		*loc = mb.loc;
//		if (size)		*size = mb.size;
//		if (rot)		*rot = mb.rot;
//		break;
//	}
//	default:
//		return 0;
//	}
//	return 1;
//}
//
///*
// * Test a bounding box for ray intersection
// * assumes the ray is already local to the boundbox space
// */
//int ray_hit_boundbox(struct BoundBox *bb, float ray_start[3], float ray_normal[3])
//{
//	static int triangle_indexes[12][3] = {{0, 1, 2}, {0, 2, 3},
//										  {3, 2, 6}, {3, 6, 7},
//										  {1, 2, 6}, {1, 6, 5},
//										  {5, 6, 7}, {4, 5, 7},
//										  {0, 3, 7}, {0, 4, 7},
//										  {0, 1, 5}, {0, 4, 5}};
//	int result = 0;
//	int i;
//
//	for (i = 0; i < 12 && result == 0; i++)
//	{
//		float lambda;
//		int v1, v2, v3;
//		v1 = triangle_indexes[i][0];
//		v2 = triangle_indexes[i][1];
//		v3 = triangle_indexes[i][2];
//		result = RayIntersectsTriangle(ray_start, ray_normal, bb.vec[v1], bb.vec[v2], bb.vec[v3], &lambda, NULL);
//	}
//
//	return result;
//}

static BoundBox boundBoxCopy(BoundBox boundBox) {
    BoundBox bb = new BoundBox();
    for (int i=0; i<bb.vec.length; i++) {
        System.arraycopy(boundBox.vec[i], 0, bb.vec[i], 0, bb.vec[i].length);
    }
    bb.flag = boundBox.flag;
    bb.pad = boundBox.pad;
    return bb;
}

}