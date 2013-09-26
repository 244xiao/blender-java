/**
 * $Id: EditMeshUtil.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation, full recode 2002-2008
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.mesh;

import blender.blenkernel.Blender;
import blender.blenkernel.CustomDataUtil;
import blender.blenkernel.Global;
import blender.blenkernel.MeshUtil;
import blender.blenkernel.UtilDefines;
import blender.blenlib.Arithb;
import blender.blenlib.EditVertUtil;
import blender.blenlib.EditVertUtil.EditEdge;
import blender.blenlib.EditVertUtil.EditFace;
import blender.blenlib.EditVertUtil.EditMesh;
import blender.blenlib.EditVertUtil.EditSelection;
import blender.blenlib.EditVertUtil.EditVert;
import blender.blenlib.EditVertUtil.HashEdge;
import blender.blenlib.ListBaseUtil;
import blender.makesdna.CustomDataTypes;
import blender.makesdna.MeshDataTypes;
import blender.makesdna.MeshTypes;
import blender.makesdna.ObjectTypes;
import blender.makesdna.SceneTypes;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.MEdge;
import blender.makesdna.sdna.MFace;
import blender.makesdna.sdna.MSelect;
import blender.makesdna.sdna.MVert;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.ModifierData;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bObject;
import static blender.blenkernel.Blender.G;

public class EditMeshUtil {

public static final int EM_FGON_DRAW=	1; // face flag
public static final int EM_FGON=			2; // edge and face flag both

/* editbutflag */
public static final int B_CLOCKWISE=			1;
public static final int B_KEEPORIG=			2;
public static final int B_BEAUTY=			4;
public static final int B_SMOOTH=			8;
public static final int B_BEAUTY_SHORT=  	0x10;
public static final int B_AUTOFGON=			0x20;
public static final int B_KNIFE=				0x80;
public static final int B_PERCENTSUBD=		0x40;
public static final int B_MESH_X_MIRROR=		0x100;
public static final int B_JOINTRIA_UV=		0x200;
public static final int B_JOINTRIA_VCOL=		0X400;
public static final int B_JOINTRIA_SHARP=	0X800;
public static final int B_JOINTRIA_MAT=		0X1000;
public static final int B_FRACTAL=			0x2000;
public static final int B_SPHERE=			0x4000;

    /* ***************** HASH ********************* */

public static final int EDHASHSIZE = (512*512);

public static int EDHASH(int a, int b) {
    return a % EDHASHSIZE;
}

    /* ************ ADD / REMOVE / FIND ****************** */

public static interface Calloc {
    public Object run(EditMesh em, Class size, int nr);
}

/* used to bypass normal calloc with fast one */
public static Calloc callocvert = null;
public static Calloc callocedge = null;
public static Calloc callocface = null;

private static int hashnr= 0;

public static EditVert addvertlist(EditMesh em, float[] vec, EditVert example)
{
	EditVert eve;

	eve= (EditVert)callocvert.run(em, EditVert.class, 1);
	ListBaseUtil.BLI_addtail(em.verts, eve);
	em.totvert++;

	if(vec!=null) UtilDefines.VECCOPY(eve.co, vec);

	eve.hash= hashnr++;
	if( hashnr>=EDHASHSIZE) hashnr= 0;

	/* new verts get keyindex of -1 since they did not
	 * have a pre-editmode vertex order
	 */
	eve.keyindex = -1;

	if(example!=null) {
                Object[] ptr = {eve.data};
		CustomDataUtil.CustomData_em_copy_data(em.vdata, em.vdata, example.data, ptr);
                eve.data=ptr[0];
		eve.bweight = example.bweight;
	}
	else {
                Object[] ptr = {eve.data};
		CustomDataUtil.CustomData_em_set_default(em.vdata, ptr);
                eve.data=ptr[0];
	}

	return eve;
}

public static void free_editvert (EditMesh em, EditVert eve)
{

	EditMeshLib.EM_remove_selection(em, eve, EditVertUtil.EDITVERT);

	Object[] ptr = {eve.data};
	CustomDataUtil.CustomData_em_free_block(em.vdata, ptr);
        eve.data=ptr[0];

//	if(eve.fast==0)
//		free(eve);

	em.totvert--;
}

public static EditEdge findedgelist(EditMesh em, EditVert v1, EditVert v2)
{
	EditVert v3;
	HashEdge he;

	/* swap ? */
	if( v1.hashCode() > v2.hashCode()) {
		v3= v2;
		v2= v1;
		v1= v3;
	}

	if(em.hashedgetab==null) {
		em.hashedgetab= new HashEdge[EDHASHSIZE];
                for (int i=0; i<EDHASHSIZE; i++) em.hashedgetab[i] = new HashEdge();
        }

	he= em.hashedgetab[EDHASH(v1.hash, v2.hash)];

	while(he!=null) {

		if(he.eed!=null && he.eed.v1==v1 && he.eed.v2==v2)
                    return he.eed;

		he= he.next;
	}
	return null;
}

public static void insert_hashedge(EditMesh em, EditEdge eed)
{
	/* assuming that eed is not in the list yet, and that a find has been done before */

	HashEdge first, he;

	first= em.hashedgetab[EDHASH(eed.v1.hash, eed.v2.hash)];

	if( first.eed==null ) {
		first.eed= eed;
	}
	else {
		he= eed.hash;
		he.eed= eed;
		he.next= first.next;
		first.next= he;
	}
}

public static void remove_hashedge(EditMesh em, EditEdge eed)
{
	/* assuming eed is in the list */

	HashEdge first, he, prev=null;

	he=first= em.hashedgetab[EDHASH(eed.v1.hash, eed.v2.hash)];

	while(he!=null) {
		if(he.eed == eed) {
			/* remove from list */
			if(he==first) {
				if(first.next!=null) {
					he= first.next;
					first.eed= he.eed;
					first.next= he.next;
				}
				else he.eed= null;
			}
			else {
				prev.next= he.next;
			}
			return;
		}
		prev= he;
		he= he.next;
	}
}

public static EditEdge addedgelist(EditMesh em, EditVert v1, EditVert v2, EditEdge example)
{
	EditVert v3;
	EditEdge eed;
	int swap= 0;

	if(v1==v2)
        return null;
	if(v1==null || v2==null)
        return null;

	/* swap ? */
	if(v1.hashCode() > v2.hashCode()) {
		v3= v2;
		v2= v1;
		v1= v3;
		swap= 1;
	}

	/* find in hashlist */
	eed= findedgelist(em, v1, v2);

	if(eed==null) {

		eed= (EditEdge)callocedge.run(em, EditEdge.class, 1);
		eed.v1= v1;
		eed.v2= v2;
		ListBaseUtil.BLI_addtail(em.edges, eed);
		eed.dir= (short)swap;
		insert_hashedge(em, eed);
                em.totedge++;

		/* copy edge data:
		   rule is to do this with addedgelist call, before addfacelist */
		if(example!=null) {
			eed.crease= example.crease;
			eed.bweight= example.bweight;
			eed.sharp = example.sharp;
			eed.seam = example.seam;
			eed.h |= (example.h & EM_FGON);
		}
	}

	return eed;
}

public static void remedge(EditMesh em, EditEdge eed)
{
	ListBaseUtil.BLI_remlink(em.edges, eed);
	remove_hashedge(em, eed);

        em.totedge--;
}

public static void free_editedge(EditMesh em, EditEdge eed)
{
	EditMeshLib.EM_remove_selection(em, eed, EditVertUtil.EDITEDGE);
//	if(eed.fast==0){
//		free(eed);
//	}
}

public static void free_editface(EditMesh em, EditFace efa)
{
	EditMeshLib.EM_remove_selection(em, efa, EditVertUtil.EDITFACE);

	if (em.act_face==efa) {
		EditMeshLib.EM_set_actFace(em, em.faces.first == efa ? null : em.faces.first);
	}

	Object[] ptr = {efa.data};
	CustomDataUtil.CustomData_em_free_block(em.fdata, ptr);
        efa.data=ptr[0];
	
//        if(efa.fast==0)
//		free(efa);

	em.totface--;
}

public static void free_vertlist(EditMesh em, ListBase<EditVert> edve)
{
	EditVert eve, next;

	if (edve==null) return;

	eve= edve.first;
	while(eve!=null) {
		next= eve.next;
		free_editvert(em, eve);
		eve= next;
	}
	edve.first= edve.last= null;
	em.totvert= em.totvertsel= 0;
}

public static void free_edgelist(EditMesh em, ListBase<EditEdge> lb)
{
	EditEdge eed, next;

	eed= lb.first;
	while(eed!=null) {
		next= eed.next;
		free_editedge(em, eed);
		eed= next;
	}
	lb.first= lb.last= null;
	em.totedge= em.totedgesel= 0;
}

public static void free_facelist(EditMesh em, ListBase<EditFace> lb)
{
	EditFace efa, next;

	efa= lb.first;
	while(efa!=null) {
		next= efa.next;
		free_editface(em, efa);
		efa= next;
	}
	lb.first= lb.last= null;
	em.totface= em.totfacesel= 0;
}

public static EditFace addfacelist(EditMesh em, EditVert v1, EditVert v2, EditVert v3, EditVert v4, EditFace example, EditFace exampleEdges)
{
	EditFace efa;
	EditEdge e1, e2=null, e3=null, e4=null;

	/* added sanity check... seems to happen for some tools, or for enter editmode for corrupted meshes */
	if(v1==v4 || v2==v4 || v3==v4) v4= null;

    /* add face to list and do the edges */
	if(exampleEdges!=null) {
		e1= addedgelist(em, v1, v2, exampleEdges.e1);
		e2= addedgelist(em, v2, v3, exampleEdges.e2);
		if(v4!=null) e3= addedgelist(em, v3, v4, exampleEdges.e3);
		else e3= addedgelist(em, v3, v1, exampleEdges.e3);
		if(v4!=null) e4= addedgelist(em, v4, v1, exampleEdges.e4);
	}
	else {
		e1= addedgelist(em, v1, v2, null);
		e2= addedgelist(em, v2, v3, null);
		if(v4!=null) e3= addedgelist(em, v3, v4, null);
		else e3= addedgelist(em, v3, v1, null);
		if(v4!=null) e4= addedgelist(em, v4, v1, null);
	}

        if(v1==v2 || v2==v3 || v1==v3)
        return null;
	if(e2==null)
        return null;

	efa= (EditFace)callocface.run(em, EditFace.class, 1);
	efa.v1= v1;
	efa.v2= v2;
	efa.v3= v3;
	efa.v4= v4;

	efa.e1= e1;
	efa.e2= e2;
	efa.e3= e3;
	efa.e4= e4;

        if(example!=null) {
		efa.mat_nr= example.mat_nr;
		efa.flag= example.flag;
                Object[] ptr = {efa.data};
		CustomDataUtil.CustomData_em_copy_data(em.fdata, em.fdata, example.data, ptr);
                efa.data=ptr[0];
	}
	else {
		efa.mat_nr= em.mat_nr;

		Object[] ptr = {efa.data};
		CustomDataUtil.CustomData_em_set_default(em.fdata, ptr);
                efa.data=ptr[0];
	}

        ListBaseUtil.BLI_addtail(em.faces, efa);
	em.totface++;

        if(efa.v4!=null) {
		Arithb.CalcNormFloat4(efa.v1.co, efa.v2.co, efa.v3.co, efa.v4.co, efa.n);
		Arithb.CalcCent4f(efa.cent, efa.v1.co, efa.v2.co, efa.v3.co, efa.v4.co);
	}
	else {
		Arithb.CalcNormFloat(efa.v1.co, efa.v2.co, efa.v3.co, efa.n);
		Arithb.CalcCent3f(efa.cent, efa.v1.co, efa.v2.co, efa.v3.co);
	}

	return efa;
}

///* ************************ end add/new/find ************  */
//
///* ************************ Edit{Vert,Edge,Face} utilss ***************************** */
//
///* some nice utility functions */
//
//EditVert *editedge_getOtherVert(EditEdge *eed, EditVert *eve)
//{
//	if (eve==eed.v1) {
//		return eed.v2;
//	} else if (eve==eed.v2) {
//		return eed.v1;
//	} else {
//		return NULL;
//	}
//}
//
//EditVert *editedge_getSharedVert(EditEdge *eed, EditEdge *eed2)
//{
//	if (eed.v1==eed2.v1 || eed.v1==eed2.v2) {
//		return eed.v1;
//	} else if (eed.v2==eed2.v1 || eed.v2==eed2.v2) {
//		return eed.v2;
//	} else {
//		return NULL;
//	}
//}
//
//int editedge_containsVert(EditEdge *eed, EditVert *eve)
//{
//	return (eed.v1==eve || eed.v2==eve);
//}
//
//int editface_containsVert(EditFace *efa, EditVert *eve)
//{
//	return (efa.v1==eve || efa.v2==eve || efa.v3==eve || (efa.v4 && efa.v4==eve));
//}
//
//int editface_containsEdge(EditFace *efa, EditEdge *eed)
//{
//	return (efa.e1==eed || efa.e2==eed || efa.e3==eed || (efa.e4 && efa.e4==eed));
//}


    /* ************************ stuct EditMesh manipulation ***************************** */

/* fake callocs for fastmalloc below */
public static Calloc calloc_fastvert = new Calloc() {
public Object run(EditMesh em, Class size, int nr)
{
	EditVert eve= em.allverts[em.curvert++] = new EditVert();
	eve.fast= 1;
	return eve;
}
};

public static Calloc calloc_fastedge = new Calloc() {
public Object run(EditMesh em, Class size, int nr)
{
	EditEdge eed= em.alledges[em.curedge++] = new EditEdge();
	eed.fast= 1;
	return eed;
}
};

public static Calloc calloc_fastface = new Calloc() {
public Object run(EditMesh em, Class size, int nr)
{
	EditFace efa= em.allfaces[em.curface++] = new EditFace();
	efa.fast= 1;
	return efa;
}
};

/* allocate 1 chunk for all vertices, edges, faces. These get tagged to
   prevent it from being freed
*/
public static void init_editmesh_fastmalloc(EditMesh em, int totvert, int totedge, int totface)
{
	if(totvert!=0)
        em.allverts= new EditVert[totvert];
	else
        em.allverts= null;
	em.curvert= 0;

	if(totedge==0) totedge= 4*totface;	// max possible

	if(totedge!=0)
        em.alledges= new EditEdge[totedge];
	else
        em.alledges= null;
	em.curedge= 0;

	if(totface!=0)
        em.allfaces= new EditFace[totface];
	else
        em.allfaces= null;
	em.curface= 0;

	callocvert= calloc_fastvert;
	callocedge= calloc_fastedge;
	callocface= calloc_fastface;
}

public static void end_editmesh_fastmalloc()
{
    callocvert= null;
    callocedge= null;
    callocface= null;
}

/* do not free editmesh itself here */
public static void free_editMesh(EditMesh em)
{
	if(em==null) return;

        if(em.verts.first!=null) free_vertlist(em, em.verts);
	if(em.edges.first!=null) free_edgelist(em, em.edges);
	if(em.faces.first!=null) free_facelist(em, em.faces);
	if(em.selected.first!=null) ListBaseUtil.BLI_freelistN(em.selected);

	CustomDataUtil.CustomData_free(em.vdata, 0);
	CustomDataUtil.CustomData_free(em.fdata, 0);

        if(em.derivedFinal!=null) {
		if (em.derivedFinal!=em.derivedCage) {
			em.derivedFinal.needsFree= 1;
			em.derivedFinal.release.run(em.derivedFinal);
		}
		em.derivedFinal= null;
	}
	if(em.derivedCage!=null) {
		em.derivedCage.needsFree= 1;
		em.derivedCage.release.run(em.derivedCage);
		em.derivedCage= null;
	}

//	if(em.hashedgetab) MEM_freeN(em.hashedgetab);
//	em.hashedgetab= NULL;
//
//	if(em.allverts) MEM_freeN(em.allverts);
//	if(em.alledges) MEM_freeN(em.alledges);
//	if(em.allfaces) MEM_freeN(em.allfaces);

	em.allverts= null; em.curvert= 0;
	em.alledges= null; em.curedge= 0;
	em.allfaces= null; em.curface= 0;

//	mesh_octree_table(NULL, NULL, NULL, 'e');

	em.totvert= em.totedge= em.totface= 0;

//// XXX	if(em.retopo_paint_data) retopo_free_paint_data(em.retopo_paint_data);
//	em.retopo_paint_data= NULL;
	em.act_face = null;
}

//static void editMesh_set_hash(EditMesh *em)
//{
//	EditEdge *eed;
//
//	if(em.hashedgetab) MEM_freeN(em.hashedgetab);
//	em.hashedgetab= NULL;
//
//	for(eed=em.edges.first; eed; eed= eed.next)  {
//		if( findedgelist(em, eed.v1, eed.v2)==NULL )
//			insert_hashedge(em, eed);
//	}
//
//}

    /* ************************ IN & OUT EDITMODE ***************************** */

public static void edge_normal_compare(EditEdge eed, EditFace efa1)
{
	EditFace efa2;
	float[] cent1=new float[3], cent2=new float[3];
	float inp;

	efa2 = eed.tmp.f();
	if(efa1==efa2)
        return;

	inp= efa1.n[0]*efa2.n[0] + efa1.n[1]*efa2.n[1] + efa1.n[2]*efa2.n[2];
	if(inp<0.999f && inp >-0.999f)
        eed.f2= 1;

	if(efa1.v4!=null)
        Arithb.CalcCent4f(cent1, efa1.v1.co, efa1.v2.co, efa1.v3.co, efa1.v4.co);
	else
        Arithb.CalcCent3f(cent1, efa1.v1.co, efa1.v2.co, efa1.v3.co);
	if(efa2.v4!=null)
        Arithb.CalcCent4f(cent2, efa2.v1.co, efa2.v2.co, efa2.v3.co, efa2.v4.co);
	else
        Arithb.CalcCent3f(cent2, efa2.v1.co, efa2.v2.co, efa2.v3.co);

	Arithb.VecSubf(cent1, cent2, cent1);
	Arithb.Normalize(cent1);
	inp= cent1[0]*efa1.n[0] + cent1[1]*efa1.n[1] + cent1[2]*efa1.n[2];

	if(inp < -0.001f )
        eed.f1= 1;
}

public static void edge_drawflags(Mesh me, EditMesh em)
{
	EditVert eve;
	EditEdge eed, e1, e2, e3, e4;
	EditFace efa;

	/* - count number of times edges are used in faces: 0 en 1 time means draw edge
	 * - edges more than 1 time used: in *tmp.f is pointer to first face
	 * - check all faces, when normal differs to much: draw (flag becomes 1)
	 */

	/* later on: added flags for 'cylinder' and 'sphere' intersection tests in old
	   game engine (2.04)
	 */

	EditMeshLib.recalc_editnormals(em);

	/* init */
	eve= em.verts.first;
	while(eve!=null) {
		eve.f1= 1;		/* during test it's set at zero */
		eve= eve.next;
	}
	eed= em.edges.first;
	while(eed!=null) {
		eed.f2= eed.f1= 0;
		eed.tmp.f(null);
		eed= eed.next;
	}

        efa= em.faces.first;
	while(efa!=null) {
		e1= efa.e1;
		e2= efa.e2;
		e3= efa.e3;
		e4= efa.e4;
		if(e1.f2<4) e1.f2+= 1;
		if(e2.f2<4) e2.f2+= 1;
		if(e3.f2<4) e3.f2+= 1;
		if(e4!=null && e4.f2<4) e4.f2+= 1;

		if(e1.tmp.f() == null) e1.tmp.f(efa);
		if(e2.tmp.f() == null) e2.tmp.f(efa);
		if(e3.tmp.f() ==null) e3.tmp.f(efa);
		if(e4!=null && (e4.tmp.f() == null)) e4.tmp.f(efa);

		efa= efa.next;
	}

	if((me.drawflag & MeshTypes.ME_ALLEDGES)!=0) {
                efa= em.faces.first;
		while(efa!=null) {
			if(efa.e1.f2>=2) efa.e1.f2= 1;
			if(efa.e2.f2>=2) efa.e2.f2= 1;
			if(efa.e3.f2>=2) efa.e3.f2= 1;
			if(efa.e4!=null && efa.e4.f2>=2) efa.e4.f2= 1;

			efa= efa.next;
		}
	}
	else {

		/* handle single-edges for 'test cylinder flag' (old engine) */

                eed= em.edges.first;
		while(eed!=null) {
			if(eed.f2==1) eed.f1= 1;
			eed= eed.next;
		}

                /* all faces, all edges with flag==2: compare normal */
		efa= em.faces.first;
		while(efa!=null) {
			if(efa.e1.f2==2) edge_normal_compare(efa.e1, efa);
			else efa.e1.f2= 1;
			if(efa.e2.f2==2) edge_normal_compare(efa.e2, efa);
			else efa.e2.f2= 1;
			if(efa.e3.f2==2) edge_normal_compare(efa.e3, efa);
			else efa.e3.f2= 1;
			if(efa.e4!=null) {
				if(efa.e4.f2==2) edge_normal_compare(efa.e4, efa);
				else efa.e4.f2= 1;
			}
			efa= efa.next;
		}

                /* sphere collision flag */

		eed= em.edges.first;
		while(eed!=null) {
			if(eed.f1!=1) {
				eed.v1.f1= eed.v2.f1= 0;
			}
			eed= eed.next;
		}

	}
}

//static int editmesh_pointcache_edit(Scene *scene, Object *ob, int totvert, PTCacheID *pid_p, float mat[][4], int load)
//{
//	Cloth *cloth;
//	SoftBody *sb;
//	ClothModifierData *clmd;
//	PTCacheID pid, tmpid;
//	int cfra= (int)scene.r.cfra, found= 0;
//
//	pid.cache= NULL;
//
//	/* check for cloth */
//	if(modifiers_isClothEnabled(ob)) {
//		clmd= (ClothModifierData*)modifiers_findByType(ob, eModifierType_Cloth);
//		cloth= clmd.clothObject;
//
//		BKE_ptcache_id_from_cloth(&tmpid, ob, clmd);
//
//		/* verify vertex count and baked status */
//		if(cloth && (totvert == cloth.numverts)) {
//			if((tmpid.cache.flag & PTCACHE_BAKED) && (tmpid.cache.flag & PTCACHE_BAKE_EDIT)) {
//				pid= tmpid;
//
//				if(load && (pid.cache.flag & PTCACHE_BAKE_EDIT_ACTIVE))
//					found= 1;
//			}
//		}
//	}
//
//	/* check for softbody */
//	if(!found && ob.soft) {
//		sb= ob.soft;
//
//		BKE_ptcache_id_from_softbody(&tmpid, ob, sb);
//
//		/* verify vertex count and baked status */
//		if(sb.bpoint && (totvert == sb.totpoint)) {
//			if((tmpid.cache.flag & PTCACHE_BAKED) && (tmpid.cache.flag & PTCACHE_BAKE_EDIT)) {
//				pid= tmpid;
//
//				if(load && (pid.cache.flag & PTCACHE_BAKE_EDIT_ACTIVE))
//					found= 1;
//			}
//		}
//	}
//
//	/* if not making editmesh verify editing was active for this point cache */
//	if(load) {
//		if(found)
//			pid.cache.flag &= ~PTCACHE_BAKE_EDIT_ACTIVE;
//		else
//			return 0;
//	}
//
//	/* check if we have cache for this frame */
//	if(pid.cache && BKE_ptcache_id_exist(&pid, cfra)) {
//		*pid_p = pid;
//
//		if(load) {
//			Mat4CpyMat4(mat, ob.obmat);
//		}
//		else {
//			pid.cache.editframe= cfra;
//			pid.cache.flag |= PTCACHE_BAKE_EDIT_ACTIVE;
//			Mat4Invert(mat, ob.obmat); /* ob.imat is not up to date */
//		}
//
//		return 1;
//	}
//
//	return 0;
//}

/* turns Mesh into editmesh */
public static void make_editMesh(Scene scene, bObject ob)
{
	Mesh me= (Mesh)ob.data;
	MFace[] mfaces;
        int mface_p;
	MVert[] mverts;
        int mvert_p;
//	MSelect *mselect;
//	KeyBlock *actkey;
	EditMesh em;
	EditVert eve, eve1, eve2, eve3, eve4;
        EditVert[] evlist;
	EditFace efa;
	EditEdge eed;
//	EditSelection *ese;
//	PTCacheID pid;
//	Cloth *cloth;
//	SoftBody *sb;
//	float cacheco[3], cachemat[4][4], *co;
        float[] co;
	int tot, a, cacheedit= 0, eekadoodle= 0;

	if(me.edit_mesh==null)
            me.edit_mesh= new EditMesh();
	else
		/* because of reload */
		free_editMesh((EditMesh)me.edit_mesh);

	em= (EditMesh)me.edit_mesh;

	em.selectmode= scene.toolsettings.selectmode; // warning needs to be synced
	em.act_face = null;
	em.totvert= tot= me.totvert;
	em.totedge= me.totedge;
	em.totface= me.totface;

	if(tot==0) {
		return;
	}

	/* initialize fastmalloc for editmesh */
	init_editmesh_fastmalloc(em, me.totvert, me.totedge, me.totface);

//	actkey = ob_get_keyblock(ob);
//	if(actkey) {
//		tot= actkey.totelem;
//		/* undo-ing in past for previous editmode sessions gives corrupt 'keyindex' values */
//		undo_editmode_clear();
//	}


	/* make editverts */
	CustomDataUtil.CustomData_copy(me.vdata, em.vdata, CustomDataUtil.CD_MASK_EDITMESH, CustomDataUtil.CD_CALLOC, 0);
	mverts= me.mvert.myarray;
        mvert_p = 0;

//	cacheedit= editmesh_pointcache_edit(scene, ob, tot, &pid, cachemat, 0);

        evlist= new EditVert[tot];
        for(a=0; a<tot; a++, mvert_p++) {
		MVert mvert = mverts[mvert_p];
//
//		if(cacheedit) {
//			if(pid.type == PTCACHE_TYPE_CLOTH) {
//				cloth= ((ClothModifierData*)pid.data).clothObject;
//				VECCOPY(cacheco, cloth.verts[a].x)
//			}
//			else if(pid.type == PTCACHE_TYPE_SOFTBODY) {
//				sb= (SoftBody*)pid.data;
//				VECCOPY(cacheco, sb.bpoint[a].pos)
//			}
//
//			Mat4MulVecfl(cachemat, cacheco);
//			co= cacheco;
//		}
//		else
			co= mvert.co;

		eve= addvertlist(em, co, null);
		evlist[a]= eve;

                // face select sets selection in next loop
		if(!Global.FACESEL_PAINT_TEST())
			eve.f |= (mvert.flag & 1);

		if ((mvert.flag & MeshDataTypes.ME_HIDE)!=0)
                    eve.h= 1;
		eve.no[0]= mvert.no[0]/32767.0f;
		eve.no[1]= mvert.no[1]/32767.0f;
		eve.no[2]= mvert.no[2]/32767.0f;

		eve.bweight= ((float)mvert.bweight)/255.0f;

		/* lets overwrite the keyindex of the editvert
		 * with the order it used to be in before
		 * editmode
		 */
		eve.keyindex = a;

                Object[] ptr = {eve.data};
		CustomDataUtil.CustomData_to_em_block(me.vdata, em.vdata, a, ptr);
                eve.data=ptr[0];
	}

//	if(actkey && actkey.totelem!=me.totvert);
//	else {
                MEdge[] medges= me.medge.myarray;
                int medge_p = 0;

                CustomDataUtil.CustomData_copy(me.edata, em.edata, CustomDataUtil.CD_MASK_EDITMESH, CustomDataUtil.CD_CALLOC, 0);
		/* make edges */
                for(a=0; a<me.totedge; a++, medge_p++) {
                        MEdge medge= medges[medge_p];
			eed= addedgelist(em, evlist[medge.v1], evlist[medge.v2], null);
			/* eed can be zero when v1 and v2 are identical, dxf import does this... */
			if(eed!=null) {
				eed.crease= ((float)medge.crease)/255.0f;
				eed.bweight= ((float)medge.bweight)/255.0f;

				if((medge.flag & MeshDataTypes.ME_SEAM)!=0) eed.seam= 1;
				if((medge.flag & MeshDataTypes.ME_SHARP)!=0) eed.sharp = 1;
				if((medge.flag & Blender.SELECT)!=0) eed.f |= Blender.SELECT;
				if((medge.flag & MeshDataTypes.ME_FGON)!=0) eed.h= EM_FGON;	// 2 different defines!
				if((medge.flag & MeshDataTypes.ME_HIDE)!=0) eed.h |= 1;
				if(em.selectmode==SceneTypes.SCE_SELECT_EDGE)
					EditMeshLib.EM_select_edge(eed, (eed.f & Blender.SELECT)!=0);		// force edge selection to vertices, seems to be needed ...
				Object[] ptr = {eed.data};
                                CustomDataUtil.CustomData_to_em_block(me.edata,em.edata, a, ptr);
                                eed.data=ptr[0];
			}
		}

		CustomDataUtil.CustomData_copy(me.fdata, em.fdata, CustomDataUtil.CD_MASK_EDITMESH, CustomDataUtil.CD_CALLOC, 0);

		/* make faces */
                mfaces= me.mface.myarray;
                mface_p = 0;

                for(a=0; a<me.totface; a++, mface_p++) {
                    MFace mface = mfaces[mface_p];
			eve1= evlist[mface.v1];
			eve2= evlist[mface.v2];
			if(mface.v3==0) eekadoodle= 1;
                            eve3= evlist[mface.v3];
			if(mface.v4!=0)
                            eve4= evlist[mface.v4];
                        else
                            eve4= null;

			efa= addfacelist(em, eve1, eve2, eve3, eve4, null, null);

                        if(efa!=null) {
                                Object[] ptr = {efa.data};
				CustomDataUtil.CustomData_to_em_block(me.fdata, em.fdata, a, ptr);
                                efa.data=ptr[0];

				efa.mat_nr= mface.mat_nr;
				efa.flag= (short)(mface.flag & ~MeshDataTypes.ME_HIDE);

				/* select and hide face flag */
				if((mface.flag & MeshDataTypes.ME_HIDE)!=0) {
					efa.h= 1;
				} else {
					if (a==me.act_face) {
						EditMeshLib.EM_set_actFace(em, efa);
					}

					/* dont allow hidden and selected */
					if((mface.flag & MeshDataTypes.ME_FACE_SEL)!=0) {
						efa.f |= Blender.SELECT;

						if(Global.FACESEL_PAINT_TEST()) {
							EditMeshLib.EM_select_face(efa, true); /* flush down */
						}
					}
				}
			}
		}
//	}

        if(eekadoodle!=0)
//		error("This Mesh has old style edgecodes, please put it in the bugtracker!");
                System.err.println("This Mesh has old style edgecodes, please put it in the bugtracker!");

//	MEM_freeN(evlist);

	end_editmesh_fastmalloc();	// resets global function pointers

	if(me.mselect!=null){
//		//restore editselections
//		EM_init_index_arrays(em, 1,1,1);
//		mselect = me.mselect;
//
//		for(a=0; a<me.totselect; a++, mselect++){
//			/*check if recorded selection is still valid, if so copy into editmesh*/
//			if( (mselect.type == EDITVERT && me.mvert[mselect.index].flag & SELECT) || (mselect.type == EDITEDGE && me.medge[mselect.index].flag & SELECT) || (mselect.type == EDITFACE && me.mface[mselect.index].flag & ME_FACE_SEL) ){
//				ese = MEM_callocN(sizeof(EditSelection), "Edit Selection");
//				ese.type = mselect.type;
//				if(ese.type == EDITVERT) ese.data = EM_get_vert_for_index(mselect.index); else
//				if(ese.type == EDITEDGE) ese.data = EM_get_edge_for_index(mselect.index); else
//				if(ese.type == EDITFACE) ese.data = EM_get_face_for_index(mselect.index);
//				BLI_addtail(&(em.selected),ese);
//			}
//		}
//		EM_free_index_arrays();
	}
        /* this creates coherent selections. also needed for older files */
	EditMeshLib.EM_selectmode_set(em);
	/* paranoia check to enforce hide rules */
	EditMeshLib.EM_hide_reset(em);
	/* sets helper flags which arent saved */
	EditMeshLib.EM_fgon_flags(em);

        if (EditMeshLib.EM_get_actFace(em, false)==null) {
		EditMeshLib.EM_set_actFace(em, em.faces.first); /* will use the first face, this is so we alwats have an active face */
	}

	/* vertex coordinates change with cache edit, need to recalc */
	if(cacheedit!=0)
		EditMeshLib.recalc_editnormals(em);

}

/* makes Mesh out of editmesh */
public static void load_editMesh(Scene scene, bObject ob)
{
	Mesh me= (Mesh)ob.data;
	MVert[] mverts, oldverts;
        int mvert_p = 0;
	MEdge[] medges;
        int medge_p = 0;
	MFace[] mfaces;
	MSelect[] mselects;
        int mselect_p = 0;
	EditMesh em= (EditMesh)me.edit_mesh;
	EditVert eve;
	EditFace efa, efa_act;
	EditEdge eed;
	EditSelection ese;
//	SoftBody *sb;
//	Cloth *cloth;
//	ClothModifierData *clmd;
//	PTCacheID pid;
	float[] fp, newkey, oldkey;
        float[] nor=new float[3], cacheco=new float[3];
        float[][] cachemat=new float[4][4];
	int i, a, ototvert, cacheedit= 0;

	/* this one also tests of edges are not in faces: */
	/* eed.f2==0: not in face, f2==1: draw it */
	/* eed.f1 : flag for dynaface (cylindertest, old engine) */
	/* eve.f1 : flag for dynaface (sphere test, old engine) */
	/* eve.f2 : being used in vertexnormals */
	edge_drawflags(me, em);

	EditMeshLib.EM_stats_update(em);

	/* new Vertex block */
	if(em.totvert==0) mverts= null;
	else {
            mverts= new MVert[em.totvert];
            for (int n=0; n<mverts.length; n++) mverts[n] = new MVert();
        }

	/* new Edge block */
	if(em.totedge==0) medges= null;
	else {
            medges= new MEdge[em.totedge];
            for (int n=0; n<medges.length; n++) medges[n] = new MEdge();
        }

	/* new Face block */
	if(em.totface==0) mfaces= null;
	else {
            mfaces= new MFace[em.totface];
            for (int n=0; n<mfaces.length; n++) mfaces[n] = new MFace();
        }

	/* lets save the old verts just in case we are actually working on
	 * a key ... we now do processing of the keys at the end */
	oldverts= me.mvert.myarray;
	ototvert= me.totvert;

	/* don't free this yet */
	CustomDataUtil.CustomData_set_layer(me.vdata, CustomDataTypes.CD_MVERT, null);

	/* free custom data */
	CustomDataUtil.CustomData_free(me.vdata, me.totvert);
	CustomDataUtil.CustomData_free(me.edata, me.totedge);
	CustomDataUtil.CustomData_free(me.fdata, me.totface);

	/* add new custom data */
	me.totvert= em.totvert;
	me.totedge= em.totedge;
	me.totface= em.totface;

	CustomDataUtil.CustomData_copy(em.vdata, me.vdata, CustomDataUtil.CD_MASK_MESH, CustomDataUtil.CD_CALLOC, me.totvert);
	CustomDataUtil.CustomData_copy(em.edata, me.edata, CustomDataUtil.CD_MASK_MESH, CustomDataUtil.CD_CALLOC, me.totedge);
	CustomDataUtil.CustomData_copy(em.fdata, me.fdata, CustomDataUtil.CD_MASK_MESH, CustomDataUtil.CD_CALLOC, me.totface);

	CustomDataUtil.CustomData_add_layer(me.vdata, CustomDataTypes.CD_MVERT, CustomDataUtil.CD_ASSIGN, mverts[0].setmyarray(mverts), me.totvert);
	CustomDataUtil.CustomData_add_layer(me.edata, CustomDataTypes.CD_MEDGE, CustomDataUtil.CD_ASSIGN, medges[0].setmyarray(medges), me.totedge);
	CustomDataUtil.CustomData_add_layer(me.fdata, CustomDataTypes.CD_MFACE, CustomDataUtil.CD_ASSIGN, mfaces[0].setmyarray(mfaces), me.totface);
	MeshUtil.mesh_update_customdata_pointers(me);

	/* the vertices, use .tmp.l as counter */
	eve= em.verts.first;
	a= 0;

//	/* check for point cache editing */
//	cacheedit= editmesh_pointcache_edit(scene, ob, em.totvert, &pid, cachemat, 1);

	while(eve!=null) {
                MVert mvert = mverts[mvert_p];
//		if(cacheedit) {
//			if(pid.type == PTCACHE_TYPE_CLOTH) {
//				clmd= (ClothModifierData*)pid.data;
//				cloth= clmd.clothObject;
//
//				/* assign position */
//				VECCOPY(cacheco, cloth.verts[a].x)
//				VECCOPY(cloth.verts[a].x, eve.co);
//				Mat4MulVecfl(cachemat, cloth.verts[a].x);
//
//				/* find plausible velocity, not physical correct but gives
//				 * nicer results when commented */
//				VECSUB(cacheco, cloth.verts[a].x, cacheco);
//				VecMulf(cacheco, clmd.sim_parms.stepsPerFrame*10.0f);
//				VECADD(cloth.verts[a].v, cloth.verts[a].v, cacheco);
//			}
//			else if(pid.type == PTCACHE_TYPE_SOFTBODY) {
//				sb= (SoftBody*)pid.data;
//
//				/* assign position */
//				VECCOPY(cacheco, sb.bpoint[a].pos)
//				VECCOPY(sb.bpoint[a].pos, eve.co);
//				Mat4MulVecfl(cachemat, sb.bpoint[a].pos);
//
//				/* changing velocity for softbody doesn't seem to give
//				 * good results? */
//#if 0
//				VECSUB(cacheco, sb.bpoint[a].pos, cacheco);
//				VecMulf(cacheco, sb.minloops*10.0f);
//				VECADD(sb.bpoint[a].vec, sb.bpoint[a].pos, cacheco);
//#endif
//			}
//
//			if(oldverts)
//				VECCOPY(mvert.co, oldverts[a].co)
//		}
//		else
			UtilDefines.VECCOPY(mvert.co, eve.co);

//		mvert.mat_nr= 32767;  /* what was this for, halos? */

		/* vertex normal */
		UtilDefines.VECCOPY(nor, eve.no);
		Arithb.VecMulf(nor, 32767.0f);
		UtilDefines.VECCOPY(mvert.no, nor);

		/* note: it used to remove me.dvert when it was not in use, cancelled
		   that... annoying when you have a fresh vgroup */
		CustomDataUtil.CustomData_from_em_block(em.vdata, me.vdata, eve.data, a);

		eve.tmp.l(a++);  /* counter */

		mvert.flag= 0;
		mvert.flag |= (eve.f & Blender.SELECT);
		if (eve.h!=0) mvert.flag |= MeshDataTypes.ME_HIDE;

		mvert.bweight= (byte)(255.0*eve.bweight);

		eve= eve.next;
		mvert_p++;
	}

//	/* write changes to cache */
//	if(cacheedit) {
//		if(pid.type == PTCACHE_TYPE_CLOTH)
//			cloth_write_cache(ob, pid.data, pid.cache.editframe);
//		else if(pid.type == PTCACHE_TYPE_SOFTBODY)
//			sbWriteCache(ob, pid.cache.editframe);
//	}

	/* the edges */
	a= 0;
	eed= em.edges.first;
	while(eed!=null) {
                MEdge medge = medges[medge_p];
		medge.v1= eed.v1.tmp.l();
		medge.v2= eed.v2.tmp.l();

		medge.flag= (short)((eed.f & Blender.SELECT) | MeshDataTypes.ME_EDGERENDER);
		if(eed.f2<2) medge.flag |= MeshDataTypes.ME_EDGEDRAW;
		if(eed.f2==0) medge.flag |= MeshDataTypes.ME_LOOSEEDGE;
		if(eed.sharp!=0) medge.flag |= MeshDataTypes.ME_SHARP;
		if(eed.seam!=0) medge.flag |= MeshDataTypes.ME_SEAM;
		if((eed.h & EM_FGON)!=0) medge.flag |= MeshDataTypes.ME_FGON;	// different defines yes
		if((eed.h & 1)!=0) medge.flag |= MeshDataTypes.ME_HIDE;

		medge.crease= (byte)(255.0*eed.crease);
		medge.bweight= (byte)(255.0*eed.bweight);
		CustomDataUtil.CustomData_from_em_block(em.edata, me.edata, eed.data, a);

		eed.tmp.l(a++);

		medge_p++;
		eed= eed.next;
	}

	/* the faces */
	a = 0;
	efa= em.faces.first;
	efa_act= EditMeshLib.EM_get_actFace(em, false);
	i = 0;
	me.act_face = -1;
	while(efa!=null) {
		MFace mface= me.mface.myarray[i];

		mface.v1= efa.v1.tmp.l();
		mface.v2= efa.v2.tmp.l();
		mface.v3= efa.v3.tmp.l();
		if (efa.v4!=null) mface.v4 = efa.v4.tmp.l();

		mface.mat_nr= efa.mat_nr;

		mface.flag= (byte)efa.flag;
		/* bit 0 of flag is already taken for smooth... */

		if(efa.h!=0) {
			mface.flag |= MeshDataTypes.ME_HIDE;
			mface.flag &= ~MeshDataTypes.ME_FACE_SEL;
		} else {
			if((efa.f & 1)!=0) mface.flag |= MeshDataTypes.ME_FACE_SEL;
			else mface.flag &= ~MeshDataTypes.ME_FACE_SEL;
		}

//		/* mat_nr in vertex */
//		if(me.totcol>1) {
//			MVert mvert= me.mvert.myarray[mface.v1];
//			if(mvert.mat_nr == (byte)32767) mvert.mat_nr= mface.mat_nr;
//			mvert= me.mvert.myarray[mface.v2];
//			if(mvert.mat_nr == (byte)32767) mvert.mat_nr= mface.mat_nr;
//			mvert= me.mvert.myarray[mface.v3];
//			if(mvert.mat_nr == (byte)32767) mvert.mat_nr= mface.mat_nr;
//			if(mface.v4!=0) {
//				mvert= me.mvert.myarray[mface.v4];
//				if(mvert.mat_nr == (byte)32767) mvert.mat_nr= mface.mat_nr;
//			}
//		}

		/* watch: efa.e1.f2==0 means loose edge */

		if(efa.e1.f2==1) {
			efa.e1.f2= 2;
		}
		if(efa.e2.f2==1) {
			efa.e2.f2= 2;
		}
		if(efa.e3.f2==1) {
			efa.e3.f2= 2;
		}
		if(efa.e4!=null && efa.e4.f2==1) {
			efa.e4.f2= 2;
		}

		CustomDataUtil.CustomData_from_em_block(em.fdata, me.fdata, efa.data, i);

		/* no index '0' at location 3 or 4 */
		MeshUtil.test_index_face(mface, me.fdata, i, efa.v4!=null?4:3);

		if (efa_act == efa)
			me.act_face = a;

		efa.tmp.l(a++);
		i++;
		efa= efa.next;
	}

	/* patch hook indices and vertex parents */
	{
//		bObject ob;
		ModifierData md;
		EditVert[] vertMap = null;
		int j;
                i = 0;

		for (ob=G.main.object.first; ob!=null; ob=(bObject)ob.id.next) {
			if (ob.parent==ob && (ob.partype==ObjectTypes.PARVERT1 || ob.partype==ObjectTypes.PARVERT3)) {

				/* duplicate code from below, make it function later...? */
				if (vertMap==null) {
					vertMap = new EditVert[ototvert];

					for (eve=em.verts.first; eve!=null; eve=eve.next) {
						if (eve.keyindex!=-1)
							vertMap[eve.keyindex] = eve;
					}
				}
				if(ob.par1 < ototvert) {
					eve = vertMap[ob.par1];
					if(eve!=null) ob.par1= eve.tmp.l();
				}
				if(ob.par2 < ototvert) {
					eve = vertMap[ob.par2];
					if(eve!=null) ob.par2= eve.tmp.l();
				}
				if(ob.par3 < ototvert) {
					eve = vertMap[ob.par3];
					if(eve!=null) ob.par3= eve.tmp.l();
				}

			}
			if (ob.data==me) {
//				for (md=ob.modifiers.first; md; md=md.next) {
//					if (md.type==eModifierType_Hook) {
//						HookModifierData *hmd = (HookModifierData*) md;
//
//						if (!vertMap) {
//							vertMap = MEM_callocN(sizeof(*vertMap)*ototvert, "vertMap");
//
//							for (eve=em.verts.first; eve; eve=eve.next) {
//								if (eve.keyindex!=-1)
//									vertMap[eve.keyindex] = eve;
//							}
//						}
//
//						for (i=j=0; i<hmd.totindex; i++) {
//							if(hmd.indexar[i] < ototvert) {
//								eve = vertMap[hmd.indexar[i]];
//
//								if (eve) {
//									hmd.indexar[j++] = eve.tmp.l;
//								}
//							}
//							else j++;
//						}
//
//						hmd.totindex = j;
//					}
//				}
			}
		}

//		if (vertMap) MEM_freeN(vertMap);
	}

	/* are there keys? */
	if(me.key!=null) {
//		KeyBlock *currkey, *actkey = ob_get_keyblock(ob);
//
//		/* Lets reorder the key data so that things line up roughly
//		 * with the way things were before editmode */
//		currkey = me.key.block.first;
//		while(currkey) {
//
//			fp= newkey= MEM_callocN(me.key.elemsize*em.totvert,  "currkey.data");
//			oldkey = currkey.data;
//
//			eve= em.verts.first;
//
//			i = 0;
//			mvert = me.mvert;
//			while(eve) {
//				if (eve.keyindex >= 0 && eve.keyindex < currkey.totelem) { // valid old vertex
//					if(currkey == actkey) {
//						if (actkey == me.key.refkey) {
//							VECCOPY(fp, mvert.co);
//						}
//						else {
//							VECCOPY(fp, mvert.co);
//							if(oldverts) {
//								VECCOPY(mvert.co, oldverts[eve.keyindex].co);
//							}
//						}
//					}
//					else {
//						if(oldkey) {
//							VECCOPY(fp, oldkey + 3 * eve.keyindex);
//						}
//					}
//				}
//				else {
//					VECCOPY(fp, mvert.co);
//				}
//				fp+= 3;
//				++i;
//				++mvert;
//				eve= eve.next;
//			}
//			currkey.totelem= em.totvert;
//			if(currkey.data) MEM_freeN(currkey.data);
//			currkey.data = newkey;
//
//			currkey= currkey.next;
//		}
	}

//	if(oldverts) MEM_freeN(oldverts);

	i = 0;
	for(ese=em.selected.first; ese!=null; ese=ese.next) i++;
	me.totselect = i;
	if(i==0) mselects= null;
	else {
            mselects= new MSelect[i];
            for (int n=0; n<mselects.length; n++) mselects[n] = new MSelect();
        }

//	if(me.mselect) MEM_freeN(me.mselect);
	me.mselect= mselects!=null?(MSelect)mselects[0].setmyarray(mselects):null;

	for(ese=em.selected.first; ese!=null; ese=ese.next){
                MSelect mselect = mselects[mselect_p];
		mselect.type = ese.type;
		if(ese.type == EditVertUtil.EDITVERT) mselect.index = ((EditVert)ese.data).tmp.l();
		else if(ese.type == EditVertUtil.EDITEDGE) mselect.index = ((EditEdge)ese.data).tmp.l();
		else if(ese.type == EditVertUtil.EDITFACE) mselect.index = ((EditFace)ese.data).tmp.l();
		mselect_p++;
	}

	/* to be sure: clear .tmp.l pointers */
	eve= em.verts.first;
	while(eve!=null) {
		eve.tmp.l(0);
		eve= eve.next;
	}

	eed= em.edges.first;
	while(eed!=null) {
		eed.tmp.l(0);
		eed= eed.next;
	}

	efa= em.faces.first;
	while(efa!=null) {
		efa.tmp.l(0);
		efa= efa.next;
	}

	/* remake softbody of all users */
	if(me.id.us>1) {
		Base base;
		for(base= (Base)scene.base.first; base!=null; base= base.next)
			if(base.object.data==me)
				base.object.recalc |= ObjectTypes.OB_RECALC_DATA;
	}

	MeshUtil.mesh_calc_normals(me.mvert.myarray, me.totvert, me.mface.myarray, me.totface, null);
}

//void remake_editMesh(Scene *scene, Object *ob)
//{
//	make_editMesh(scene, ob);
//	DAG_object_flush_update(scene, ob, OB_RECALC_DATA);
//	BIF_undo_push("Undo all changes");
//}
//
///* *************** Operator: separate parts *************/
//
//static EnumPropertyItem prop_separate_types[] = {
//	{0, "SELECTED", 0, "Selection", ""},
//	{1, "MATERIAL", 0, "By Material", ""},
//	{2, "LOOSE", 0, "By loose parts", ""},
//	{0, NULL, 0, NULL, NULL}
//};
//
///* return 1: success */
//static int mesh_separate_selected(Scene *scene, Base *editbase)
//{
//	EditMesh *em, *emnew;
//	EditVert *eve, *v1;
//	EditEdge *eed, *e1;
//	EditFace *efa, *f1;
//	Object *obedit;
//	Mesh *me, *menew;
//	Base *basenew;
//
//	if(editbase==NULL) return 0;
//
//	obedit= editbase.object;
//	me= obedit.data;
//	em= BKE_mesh_get_editmesh(me);
//	if(me.key) {
//		error("Can't separate with vertex keys");
//		BKE_mesh_end_editmesh(me, em);
//		return 0;
//	}
//
//	if(em.selected.first)
//		BLI_freelistN(&(em.selected)); /* clear the selection order */
//
//	EM_selectmode_set(em);	// enforce full consistent selection flags
//
//	EM_stats_update(em);
//
//	if(em.totvertsel==0) {
//		BKE_mesh_end_editmesh(me, em);
//		return 0;
//	}
//
//	/* we are going to work as follows:
//	 * 1. add a linked duplicate object: this will be the new one, we remember old pointer
//	 * 2. give new object empty mesh and put in editmode
//	 * 3: do a split if needed on current editmesh.
//	 * 4. copy over: all NOT selected verts, edges, faces
//	 * 5. call load_editMesh() on the new object
//	 */
//
//	/* 1 */
//	basenew= ED_object_add_duplicate(scene, editbase, 0);	/* 0 = fully linked */
//	ED_base_object_select(basenew, BA_DESELECT);
//
//	/* 2 */
//	basenew.object.data= menew= add_mesh(me.id.name);	/* empty */
//	me.id.us--;
//	make_editMesh(scene, basenew.object);
//	emnew= menew.edit_mesh;
//
//	/* 3 */
//	/* SPLIT: first make duplicate */
//	adduplicateflag(em, SELECT);
//	/* SPLIT: old faces have 3x flag 128 set, delete these ones */
//	delfaceflag(em, 128);
//	/* since we do tricky things with verts/edges/faces, this makes sure all is selected coherent */
//	EM_selectmode_set(em);
//
//	/* 4 */
//	/* move over: everything that is selected */
//	for(eve= em.verts.first; eve; eve= v1) {
//		v1= eve.next;
//		if(eve.f & SELECT) {
//			BLI_remlink(&em.verts, eve);
//			BLI_addtail(&emnew.verts, eve);
//		}
//	}
//
//	for(eed= em.edges.first; eed; eed= e1) {
//		e1= eed.next;
//		if(eed.f & SELECT) {
//			BLI_remlink(&em.edges, eed);
//			BLI_addtail(&emnew.edges, eed);
//		}
//	}
//
//	for(efa= em.faces.first; efa; efa= f1) {
//		f1= efa.next;
//		if (efa == em.act_face && (efa.f & SELECT)) {
//			EM_set_actFace(em, NULL);
//		}
//
//		if(efa.f & SELECT) {
//			BLI_remlink(&em.faces, efa);
//			BLI_addtail(&emnew.faces, efa);
//		}
//	}
//
//	/* 5 */
//	load_editMesh(scene, basenew.object);
//	free_editMesh(emnew);
//
//	/* hashedges are invalid now, make new! */
//	editMesh_set_hash(em);
//
//	DAG_object_flush_update(scene, obedit, OB_RECALC_DATA);
//	DAG_object_flush_update(scene, basenew.object, OB_RECALC_DATA);
//
//	BKE_mesh_end_editmesh(me, em);
//
//	return 1;
//}
//
///* return 1: success */
//static int mesh_separate_material(Scene *scene, Base *editbase)
//{
//	Mesh *me= editbase.object.data;
//	EditMesh *em= BKE_mesh_get_editmesh(me);
//	unsigned char curr_mat;
//
//	for (curr_mat = 1; curr_mat < editbase.object.totcol; ++curr_mat) {
//		/* clear selection, we're going to use that to select material group */
//		EM_clear_flag_all(em, SELECT);
//		/* select the material */
//		EM_select_by_material(em, curr_mat);
//		/* and now separate */
//		if(0==mesh_separate_selected(scene, editbase)) {
//			BKE_mesh_end_editmesh(me, em);
//			return 0;
//		}
//	}
//
//	BKE_mesh_end_editmesh(me, em);
//	return 1;
//}
//
///* return 1: success */
//static int mesh_separate_loose(Scene *scene, Base *editbase)
//{
//	Mesh *me;
//	EditMesh *em;
//	int doit= 1;
//
//	me= editbase.object.data;
//	em= BKE_mesh_get_editmesh(me);
//
//	if(me.key) {
//		error("Can't separate with vertex keys");
//		BKE_mesh_end_editmesh(me, em);
//		return 0;
//	}
//
//	EM_clear_flag_all(em, SELECT);
//
//	while(doit && em.verts.first) {
//		/* Select a random vert to start with */
//		EditVert *eve= em.verts.first;
//		eve.f |= SELECT;
//
//		selectconnected_mesh_all(em);
//
//		/* and now separate */
//		doit= mesh_separate_selected(scene, editbase);
//	}
//
//	BKE_mesh_end_editmesh(me, em);
//	return 1;
//}
//
//
//static int mesh_separate_exec(bContext *C, wmOperator *op)
//{
//	Scene *scene= CTX_data_scene(C);
//	Base *base= CTX_data_active_base(C);
//	int retval= 0, type= RNA_enum_get(op.ptr, "type");
//
//	if(type == 0)
//		retval= mesh_separate_selected(scene, base);
//	else if(type == 1)
//		retval= mesh_separate_material (scene, base);
//	else if(type == 2)
//		retval= mesh_separate_loose(scene, base);
//
//	if(retval) {
//		WM_event_add_notifier(C, NC_OBJECT|ND_GEOM_SELECT, base.object);
//		return OPERATOR_FINISHED;
//	}
//	return OPERATOR_CANCELLED;
//}
//
//void MESH_OT_separate(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Separate";
//	ot.idname= "MESH_OT_separate";
//
//	/* api callbacks */
//	ot.invoke= WM_menu_invoke;
//	ot.exec= mesh_separate_exec;
//	ot.poll= ED_operator_editmesh;
//
//	/* flags */
//	ot.flag= OPTYPE_REGISTER|OPTYPE_UNDO;
//
//	RNA_def_enum(ot.srna, "type", prop_separate_types, 0, "Type", "");
//}
//
//
///* ******************************************** */
//
///* *************** UNDO ***************************** */
///* new mesh undo, based on pushing editmesh data itself */
///* reuses same code as for global and curve undo... unify that (ton) */
//
///* only one 'hack', to save memory it doesn't store the first push, but does a remake editmesh */
//
///* a compressed version of editmesh data */
//
//typedef struct EditVertC
//{
//	float no[3];
//	float co[3];
//	unsigned char f, h;
//	short bweight;
//	int keyindex;
//} EditVertC;
//
//typedef struct EditEdgeC
//{
//	int v1, v2;
//	unsigned char f, h, seam, sharp, pad;
//	short crease, bweight, fgoni;
//} EditEdgeC;
//
//typedef struct EditFaceC
//{
//	int v1, v2, v3, v4;
//	unsigned char flag, f, h, fgonf, pad1;
//	short mat_nr;
//} EditFaceC;
//
//typedef struct EditSelectionC{
//	short type;
//	int index;
//}EditSelectionC;
//
//typedef struct UndoMesh {
//	EditVertC *verts;
//	EditEdgeC *edges;
//	EditFaceC *faces;
//	EditSelectionC *selected;
//	int totvert, totedge, totface, totsel;
//	short selectmode;
//	RetopoPaintData *retopo_paint_data;
//	char retopo_mode;
//	CustomData vdata, edata, fdata;
//} UndoMesh;
//
///* for callbacks */
//
//static void free_undoMesh(void *umv)
//{
//	UndoMesh *um= umv;
//
//	if(um.verts) MEM_freeN(um.verts);
//	if(um.edges) MEM_freeN(um.edges);
//	if(um.faces) MEM_freeN(um.faces);
//	if(um.selected) MEM_freeN(um.selected);
//// XXX	if(um.retopo_paint_data) retopo_free_paint_data(um.retopo_paint_data);
//	CustomData_free(&um.vdata, um.totvert);
//	CustomData_free(&um.edata, um.totedge);
//	CustomData_free(&um.fdata, um.totface);
//	MEM_freeN(um);
//}
//
//static void *editMesh_to_undoMesh(void *emv)
//{
//	EditMesh *em= (EditMesh *)emv;
//	UndoMesh *um;
//	EditVert *eve;
//	EditEdge *eed;
//	EditFace *efa;
//	EditSelection *ese;
//	EditVertC *evec=NULL;
//	EditEdgeC *eedc=NULL;
//	EditFaceC *efac=NULL;
//	EditSelectionC *esec=NULL;
//	int a;
//
//	um= MEM_callocN(sizeof(UndoMesh), "undomesh");
//
//	um.selectmode = em.selectmode;
//
//	for(eve=em.verts.first; eve; eve= eve.next) um.totvert++;
//	for(eed=em.edges.first; eed; eed= eed.next) um.totedge++;
//	for(efa=em.faces.first; efa; efa= efa.next) um.totface++;
//	for(ese=em.selected.first; ese; ese=ese.next) um.totsel++;
//	/* malloc blocks */
//
//	if(um.totvert) evec= um.verts= MEM_callocN(um.totvert*sizeof(EditVertC), "allvertsC");
//	if(um.totedge) eedc= um.edges= MEM_callocN(um.totedge*sizeof(EditEdgeC), "alledgesC");
//	if(um.totface) efac= um.faces= MEM_callocN(um.totface*sizeof(EditFaceC), "allfacesC");
//	if(um.totsel) esec= um.selected= MEM_callocN(um.totsel*sizeof(EditSelectionC), "allselections");
//
//	if(um.totvert) CustomData_copy(&em.vdata, &um.vdata, CD_MASK_EDITMESH, CD_CALLOC, um.totvert);
//	if(um.totedge) CustomData_copy(&em.edata, &um.edata, CD_MASK_EDITMESH, CD_CALLOC, um.totedge);
//	if(um.totface) CustomData_copy(&em.fdata, &um.fdata, CD_MASK_EDITMESH, CD_CALLOC, um.totface);
//
//	/* now copy vertices */
//	a = 0;
//	for(eve=em.verts.first; eve; eve= eve.next, evec++, a++) {
//		VECCOPY(evec.co, eve.co);
//		VECCOPY(evec.no, eve.no);
//
//		evec.f= eve.f;
//		evec.h= eve.h;
//		evec.keyindex= eve.keyindex;
//		eve.tmp.l = a; /*store index*/
//		evec.bweight= (short)(eve.bweight*255.0);
//
//		CustomData_from_em_block(&em.vdata, &um.vdata, eve.data, a);
//	}
//
//	/* copy edges */
//	a = 0;
//	for(eed=em.edges.first; eed; eed= eed.next, eedc++, a++)  {
//		eedc.v1= (int)eed.v1.tmp.l;
//		eedc.v2= (int)eed.v2.tmp.l;
//		eedc.f= eed.f;
//		eedc.h= eed.h;
//		eedc.seam= eed.seam;
//		eedc.sharp= eed.sharp;
//		eedc.crease= (short)(eed.crease*255.0);
//		eedc.bweight= (short)(eed.bweight*255.0);
//		eedc.fgoni= eed.fgoni;
//		eed.tmp.l = a; /*store index*/
//		CustomData_from_em_block(&em.edata, &um.edata, eed.data, a);
//
//	}
//
//	/* copy faces */
//	a = 0;
//	for(efa=em.faces.first; efa; efa= efa.next, efac++, a++) {
//		efac.v1= (int)efa.v1.tmp.l;
//		efac.v2= (int)efa.v2.tmp.l;
//		efac.v3= (int)efa.v3.tmp.l;
//		if(efa.v4) efac.v4= (int)efa.v4.tmp.l;
//		else efac.v4= -1;
//
//		efac.mat_nr= efa.mat_nr;
//		efac.flag= efa.flag;
//		efac.f= efa.f;
//		efac.h= efa.h;
//		efac.fgonf= efa.fgonf;
//
//		efa.tmp.l = a; /*store index*/
//
//		CustomData_from_em_block(&em.fdata, &um.fdata, efa.data, a);
//	}
//
//	a = 0;
//	for(ese=em.selected.first; ese; ese=ese.next, esec++){
//		esec.type = ese.type;
//		if(ese.type == EDITVERT) a = esec.index = ((EditVert*)ese.data).tmp.l;
//		else if(ese.type == EDITEDGE) a = esec.index = ((EditEdge*)ese.data).tmp.l;
//		else if(ese.type == EDITFACE) a = esec.index = ((EditFace*)ese.data).tmp.l;
//	}
//
//// XXX	um.retopo_paint_data= retopo_paint_data_copy(em.retopo_paint_data);
////	um.retopo_mode= scene.toolsettings.retopo_mode;
//
//	return um;
//}
//
//static void undoMesh_to_editMesh(void *umv, void *emv)
//{
//	EditMesh *em= (EditMesh *)emv;
//	UndoMesh *um= (UndoMesh *)umv;
//	EditVert *eve, **evar=NULL;
//	EditEdge *eed;
//	EditFace *efa;
//	EditSelection *ese;
//	EditVertC *evec;
//	EditEdgeC *eedc;
//	EditFaceC *efac;
//	EditSelectionC *esec;
//	int a=0;
//
//	free_editMesh(em);
//
//	/* malloc blocks */
//	memset(em, 0, sizeof(EditMesh));
//
//	em.selectmode = um.selectmode;
//
//	init_editmesh_fastmalloc(em, um.totvert, um.totedge, um.totface);
//
//	CustomData_free(&em.vdata, 0);
//	CustomData_free(&em.edata, 0);
//	CustomData_free(&em.fdata, 0);
//
//	CustomData_copy(&um.vdata, &em.vdata, CD_MASK_EDITMESH, CD_CALLOC, 0);
//	CustomData_copy(&um.edata, &em.edata, CD_MASK_EDITMESH, CD_CALLOC, 0);
//	CustomData_copy(&um.fdata, &em.fdata, CD_MASK_EDITMESH, CD_CALLOC, 0);
//
//	/* now copy vertices */
//
//	if(um.totvert) evar= MEM_mallocN(um.totvert*sizeof(EditVert *), "vertex ar");
//	for(a=0, evec= um.verts; a<um.totvert; a++, evec++) {
//		eve= addvertlist(em, evec.co, NULL);
//		evar[a]= eve;
//
//		VECCOPY(eve.no, evec.no);
//		eve.f= evec.f;
//		eve.h= evec.h;
//		eve.keyindex= evec.keyindex;
//		eve.bweight= ((float)evec.bweight)/255.0f;
//
//		CustomData_to_em_block(&um.vdata, &em.vdata, a, &eve.data);
//	}
//
//	/* copy edges */
//	for(a=0, eedc= um.edges; a<um.totedge; a++, eedc++) {
//		eed= addedgelist(em, evar[eedc.v1], evar[eedc.v2], NULL);
//
//		eed.f= eedc.f;
//		eed.h= eedc.h;
//		eed.seam= eedc.seam;
//		eed.sharp= eedc.sharp;
//		eed.fgoni= eedc.fgoni;
//		eed.crease= ((float)eedc.crease)/255.0f;
//		eed.bweight= ((float)eedc.bweight)/255.0f;
//		CustomData_to_em_block(&um.edata, &em.edata, a, &eed.data);
//	}
//
//	/* copy faces */
//	for(a=0, efac= um.faces; a<um.totface; a++, efac++) {
//		if(efac.v4 != -1)
//			efa= addfacelist(em, evar[efac.v1], evar[efac.v2], evar[efac.v3], evar[efac.v4], NULL, NULL);
//		else
//			efa= addfacelist(em, evar[efac.v1], evar[efac.v2], evar[efac.v3], NULL, NULL ,NULL);
//
//		efa.mat_nr= efac.mat_nr;
//		efa.flag= efac.flag;
//		efa.f= efac.f;
//		efa.h= efac.h;
//		efa.fgonf= efac.fgonf;
//
//		CustomData_to_em_block(&um.fdata, &em.fdata, a, &efa.data);
//	}
//
//	end_editmesh_fastmalloc();
//	if(evar) MEM_freeN(evar);
//
//	em.totvert = um.totvert;
//	em.totedge = um.totedge;
//	em.totface = um.totface;
//	/*restore stored editselections*/
//	if(um.totsel){
//		EM_init_index_arrays(em, 1,1,1);
//		for(a=0, esec= um.selected; a<um.totsel; a++, esec++){
//			ese = MEM_callocN(sizeof(EditSelection), "Edit Selection");
//			ese.type = esec.type;
//			if(ese.type == EDITVERT) ese.data = EM_get_vert_for_index(esec.index); else
//			if(ese.type == EDITEDGE) ese.data = EM_get_edge_for_index(esec.index); else
//			if(ese.type == EDITFACE) ese.data = EM_get_face_for_index(esec.index);
//			BLI_addtail(&(em.selected),ese);
//		}
//		EM_free_index_arrays();
//	}
//
//// XXX	retopo_free_paint();
////	em.retopo_paint_data= retopo_paint_data_copy(um.retopo_paint_data);
////	scene.toolsettings.retopo_mode= um.retopo_mode;
////	if(scene.toolsettings.retopo_mode) {
//// XXX		if(G.vd.depths) G.vd.depths.damaged= 1;
////		retopo_queue_updates(G.vd);
////		retopo_paint_view_update(G.vd);
////	}
//
//}
//
//static void *getEditMesh(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_MESH) {
//		Mesh *me= obedit.data;
//		return me.edit_mesh;
//	}
//	return NULL;
//}
//
///* and this is all the undo system needs to know */
//void undo_push_mesh(bContext *C, char *name)
//{
//	undo_editmode_push(C, name, getEditMesh, free_undoMesh, undoMesh_to_editMesh, editMesh_to_undoMesh, NULL);
//}

    /* *************** END UNDO *************/

static EditVert[] g_em_vert_array = null;
static EditEdge[] g_em_edge_array = null;
static EditFace[] g_em_face_array = null;

public static void EM_init_index_arrays(EditMesh em, boolean forVert, boolean forEdge, boolean forFace)
{
	EditVert eve;
	EditEdge eed;
	EditFace efa;
	int i;

	if (forVert) {
		em.totvert= ListBaseUtil.BLI_countlist(em.verts);

		if(em.totvert!=0) {
			g_em_vert_array = new EditVert[em.totvert];

			for (i=0,eve=em.verts.first; eve!=null; i++,eve=eve.next)
				g_em_vert_array[i] = eve;
		}
	}

	if (forEdge) {
		em.totedge= ListBaseUtil.BLI_countlist(em.edges);

		if(em.totedge!=0) {
			g_em_edge_array = new EditEdge[em.totedge];

			for (i=0,eed=em.edges.first; eed!=null; i++,eed=eed.next)
				g_em_edge_array[i] = eed;
		}
	}

	if (forFace) {
		em.totface= ListBaseUtil.BLI_countlist(em.faces);

		if(em.totface!=0) {
			g_em_face_array = new EditFace[em.totface];

			for (i=0,efa=em.faces.first; efa!=null; i++,efa=efa.next)
				g_em_face_array[i] = efa;
		}
	}
}

public static void EM_free_index_arrays()
{
//	if (g_em_vert_array) MEM_freeN(g_em_vert_array);
//	if (g_em_edge_array) MEM_freeN(g_em_edge_array);
//	if (g_em_face_array) MEM_freeN(g_em_face_array);
	g_em_vert_array = null;
	g_em_edge_array = null;
	g_em_face_array = null;
}

public static EditVert EM_get_vert_for_index(int index)
{
	return g_em_vert_array!=null?g_em_vert_array[index]:null;
}

public static EditEdge EM_get_edge_for_index(int index)
{
	return g_em_edge_array!=null?g_em_edge_array[index]:null;
}

public static EditFace EM_get_face_for_index(int index)
{
	return g_em_face_array!=null?g_em_face_array[index]:null;
}

/* can we edit UV's for this mesh?*/
public static boolean EM_texFaceCheck(EditMesh em)
{
	/* some of these checks could be a touch overkill */
	if (	(em!=null) &&
			(em.faces.first!=null) &&
			(CustomDataUtil.CustomData_has_layer(em.fdata, CustomDataTypes.CD_MTFACE)))
		return true;
	return false;
}

///* can we edit colors for this mesh?*/
//int EM_vertColorCheck(EditMesh *em)
//{
//	/* some of these checks could be a touch overkill */
//	if (	(em) &&
//			(em.faces.first) &&
//			(CustomData_has_layer(&em.fdata, CD_MCOL)))
//		return 1;
//	return 0;
//}
//
//
//void em_setup_viewcontext(bContext *C, ViewContext *vc)
//{
//	view3d_set_viewcontext(C, vc);
//
//	if(vc.obedit) {
//		Mesh *me= vc.obedit.data;
//		vc.em= me.edit_mesh;
//	}
//}
}