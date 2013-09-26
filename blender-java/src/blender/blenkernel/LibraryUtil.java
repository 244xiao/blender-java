/** 
 * $Id: library.c 20440 2009-05-27 00:03:49Z blendix $
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

import blender.makesdna.sdna.ID;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.Brush;
import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.Curve;
import blender.makesdna.sdna.Group;
import blender.makesdna.sdna.Image;
import blender.makesdna.sdna.Ipo;
import blender.makesdna.sdna.Key;
import blender.makesdna.sdna.Lamp;
import blender.makesdna.sdna.Lattice;
import blender.makesdna.sdna.Library;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Material;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.MetaBall;
import blender.makesdna.sdna.ParticleSettings;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.Tex;
import blender.makesdna.sdna.Text;
import blender.makesdna.sdna.VFont;
import blender.makesdna.sdna.World;
import blender.makesdna.sdna.bAction;
import blender.makesdna.sdna.bArmature;
import blender.makesdna.sdna.bNodeTree;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.bSound;
import blender.blenlib.StringUtil;
import blender.blenlib.ListBaseUtil;
import java.util.Arrays;
import static blender.blenkernel.Blender.G;

/*
 *  Contains management of ID's and libraries
 *  allocate and free of all library data
 * 
 */
public class LibraryUtil {

public static final int MAX_LIBARRAY=	40;

public static final int MAX_IDPUP=		60;	/* was 24 */

    /* ************* general ************************ */

//void id_lib_extern(ID *id)
//{
//	if(id) {
//		if(id.flag & LIB_INDIRECT) {
//			id.flag -= LIB_INDIRECT;
//			id.flag |= LIB_EXTERN;
//		}
//	}
//}

    public static void id_us_plus(ID id) {
        if (id != null) {
            id.us++;
            if ((id.flag & DNA_ID.LIB_INDIRECT) != 0) {
                id.flag -= DNA_ID.LIB_INDIRECT;
                id.flag |= DNA_ID.LIB_EXTERN;
            }
        }
    }

//void id_us_min(ID *id)
//{
//	if(id)
//		id.us--;
//}

    public static ListBase wich_libbase(Main mainlib, short type) {
        switch (type) {
            case DNA_ID.ID_SCE:
                return (mainlib.scene);
            case DNA_ID.ID_LI:
                return (mainlib.library);
            case DNA_ID.ID_OB:
                return (mainlib.object);
            case DNA_ID.ID_ME:
                return (mainlib.mesh);
            case DNA_ID.ID_CU:
                return (mainlib.curve);
            case DNA_ID.ID_MB:
                return (mainlib.mball);
            case DNA_ID.ID_MA:
                return (mainlib.mat);
            case DNA_ID.ID_TE:
                return (mainlib.tex);
            case DNA_ID.ID_IM:
                return (mainlib.image);
//            case DNA_ID.ID_WV:
//                return (mainlib.wave);
            case DNA_ID.ID_LT:
                return (mainlib.latt);
            case DNA_ID.ID_LA:
                return (mainlib.lamp);
            case DNA_ID.ID_CA:
                return (mainlib.camera);
            case DNA_ID.ID_IP:
                return (mainlib.ipo);
            case DNA_ID.ID_KE:
                return (mainlib.key);
            case DNA_ID.ID_WO:
                return (mainlib.world);
            case DNA_ID.ID_SCR:
                return (mainlib.screen);
            case DNA_ID.ID_VF:
                return (mainlib.vfont);
            case DNA_ID.ID_TXT:
                return (mainlib.text);
            case DNA_ID.ID_SCRIPT:
                return (mainlib.script);
            case DNA_ID.ID_SO:
                return (mainlib.sound);
            case DNA_ID.ID_GR:
                return (mainlib.group);
            case DNA_ID.ID_AR:
                return (mainlib.armature);
            case DNA_ID.ID_AC:
                return (mainlib.action);
            case DNA_ID.ID_NT:
                return (mainlib.nodetree);
            case DNA_ID.ID_BR:
                return (mainlib.brush);
            case DNA_ID.ID_PA:
                return (mainlib.particle);
            case DNA_ID.ID_WM:
                return (mainlib.wm);
            case DNA_ID.ID_GD:
                return (mainlib.gpencil);
        }
        return null;
    }

///* Flag all ids in listbase */
//void flag_listbase_ids(ListBase *lb, short flag, short value)
//{
//	ID *id;
//	if (value) {
//		for(id= lb.first; id; id= id.next) id.flag |= flag;
//	} else {
//		flag = ~flag;
//		for(id= lb.first; id; id= id.next) id.flag &= flag;
//	}
//}
//
///* Flag all ids in listbase */
//void flag_all_listbases_ids(short flag, short value)
//{
//	ListBase *lbarray[MAX_LIBARRAY];
//	int a;
//	a= set_listbasepointers(G.main, lbarray);
//	while(a--)	flag_listbase_ids(lbarray[a], flag, value);
//}

    /* note: MAX_LIBARRAY define should match this code */
    public static int set_listbasepointers(Main main, ListBase[] lb) {
        int a = 0;

        /* BACKWARDS! also watch order of free-ing! (mesh<.mat) */

	lb[a++]= (main.ipo);
        lb[a++] = (main.action); // xxx moved here to avoid problems when freeing with animato (aligorith)
        lb[a++] = (main.key);
        lb[a++] = (main.nodetree);
        lb[a++] = (main.image);
        lb[a++] = (main.tex);
        lb[a++] = (main.mat);
        lb[a++] = (main.vfont);

        /* Important!: When adding a new object type,
         * the specific data should be inserted here
         */

        lb[a++] = (main.armature);

        lb[a++] = (main.mesh);
        lb[a++] = (main.curve);
        lb[a++] = (main.mball);

        lb[a++] = (main.wave);
        lb[a++] = (main.latt);
        lb[a++] = (main.lamp);
        lb[a++] = (main.camera);

        lb[a++] = (main.text);
        lb[a++] = (main.sound);
        lb[a++] = (main.group);
        lb[a++] = (main.brush);
        lb[a++] = (main.script);
        lb[a++] = (main.particle);

        lb[a++] = (main.world);
        lb[a++] = (main.screen);
        lb[a++] = (main.object);
        lb[a++] = (main.scene);
        lb[a++] = (main.library);
        lb[a++] = (main.wm);
        lb[a++] = (main.gpencil);

        lb[a] = null;

        return a;
    }

///* *********** ALLOC AND FREE *****************
//
//free_libblock(ListBase *lb, ID *id )
//	provide a list-basis and datablock, but only ID is read
//
//void *alloc_libblock(ListBase *lb, type, name)
//	inserts in list and returns a new ID
//
// ***************************** */

//static ID *alloc_libblock_notest(short type)
//{
//	ID *id= NULL;
//
//	switch( type ) {
//		case ID_SCE:
//			id= MEM_callocN(sizeof(Scene), "scene");
//			break;
//		case ID_LI:
//			id= MEM_callocN(sizeof(Library), "library");
//			break;
//		case ID_OB:
//			id= MEM_callocN(sizeof(Object), "object");
//			break;
//		case ID_ME:
//			id= MEM_callocN(sizeof(Mesh), "mesh");
//			break;
//		case ID_CU:
//			id= MEM_callocN(sizeof(Curve), "curve");
//			break;
//		case ID_MB:
//			id= MEM_callocN(sizeof(MetaBall), "mball");
//			break;
//		case ID_MA:
//			id= MEM_callocN(sizeof(Material), "mat");
//			break;
//		case ID_TE:
//			id= MEM_callocN(sizeof(Tex), "tex");
//			break;
//		case ID_IM:
//			id= MEM_callocN(sizeof(Image), "image");
//			break;
//		case ID_WV:
//			id= MEM_callocN(sizeof(Wave), "wave");
//			break;
//		case ID_LT:
//			id= MEM_callocN(sizeof(Lattice), "latt");
//			break;
//		case ID_LA:
//			id= MEM_callocN(sizeof(Lamp), "lamp");
//			break;
//		case ID_CA:
//			id= MEM_callocN(sizeof(Camera), "camera");
//			break;
//		case ID_IP:
//			id= MEM_callocN(sizeof(Ipo), "ipo");
//			break;
//		case ID_KE:
//			id= MEM_callocN(sizeof(Key), "key");
//			break;
//		case ID_WO:
//			id= MEM_callocN(sizeof(World), "world");
//			break;
//		case ID_SCR:
//			id= MEM_callocN(sizeof(bScreen), "screen");
//			break;
//		case ID_VF:
//			id= MEM_callocN(sizeof(VFont), "vfont");
//			break;
//		case ID_TXT:
//			id= MEM_callocN(sizeof(Text), "text");
//			break;
//		case ID_SCRIPT:
//			//XXX id= MEM_callocN(sizeof(Script), "script");
//			break;
//		case ID_SO:
//			id= MEM_callocN(sizeof(bSound), "sound");
//			break;
//		case ID_GR:
//			id= MEM_callocN(sizeof(Group), "group");
//			break;
//		case ID_AR:
//			id = MEM_callocN(sizeof(bArmature), "armature");
//			break;
//		case ID_AC:
//			id = MEM_callocN(sizeof(bAction), "action");
//			break;
//		case ID_NT:
//			id = MEM_callocN(sizeof(bNodeTree), "nodetree");
//			break;
//		case ID_BR:
//			id = MEM_callocN(sizeof(Brush), "brush");
//			break;
//		case ID_PA:
//			id = MEM_callocN(sizeof(ParticleSettings), "ParticleSettings");
//  			break;
//		case ID_WM:
//			id = MEM_callocN(sizeof(wmWindowManager), "Window manager");
//  			break;
//		case ID_GD:
//			id = MEM_callocN(sizeof(bGPdata), "Grease Pencil");
//			break;
//	}
//	return id;
//}

    public static ID alloc_libblock_notest(short type) {
        ID id = null;

        switch (type) {
            case DNA_ID.ID_SCE:
                id = new Scene();
                break;
            case DNA_ID.ID_LI:
                id = new Library();
                break;
            case DNA_ID.ID_OB:
                id = new bObject();
                break;
            case DNA_ID.ID_ME:
                id = new Mesh();
                break;
            case DNA_ID.ID_CU:
                id = new Curve();
                break;
            case DNA_ID.ID_MB:
                id = new MetaBall();
                break;
            case DNA_ID.ID_MA:
                id = new Material();
                break;
            case DNA_ID.ID_TE:
                id = new Tex();
                break;
            case DNA_ID.ID_IM:
                id = new Image();
                break;
//            case DNA_ID.ID_WV:
//                id = new Wave();
//                break;
            case DNA_ID.ID_LT:
                id = new Lattice();
                break;
            case DNA_ID.ID_LA:
                id = new Lamp();
                break;
            case DNA_ID.ID_CA:
                id = new Camera();
                break;
            case DNA_ID.ID_IP:
                id = new Ipo();
                break;
            case DNA_ID.ID_KE:
                id = new Key();
                break;
            case DNA_ID.ID_WO:
                id = new World();
                break;
            case DNA_ID.ID_SCR:
                id = new bScreen();
                break;
            case DNA_ID.ID_VF:
                id = new VFont();
                break;
            case DNA_ID.ID_TXT:
                id = new Text();
                break;
            case DNA_ID.ID_SCRIPT:
                //XXX id= new Script();
                break;
            case DNA_ID.ID_SO:
                id = new bSound();
                break;
            case DNA_ID.ID_GR:
                id = new Group();
                break;
            case DNA_ID.ID_AR:
                id = new bArmature();
                break;
            case DNA_ID.ID_AC:
                id = new bAction();
                break;
            case DNA_ID.ID_NT:
                id = new bNodeTree();
                break;
            case DNA_ID.ID_BR:
                id = new Brush();
                break;
            case DNA_ID.ID_PA:
                id = new ParticleSettings();
                break;
            case DNA_ID.ID_WM:
//                id = new wmWindowManager();
                break;
            case DNA_ID.ID_GD:
//                id = new bGPdata();
                break;
        }
        return id;
    }

    /* used everywhere in blenkernel and text.c */
    public static ID alloc_libblock(ListBase lb, short type, byte[] name, int offset) {
        ID id = null;

        id = alloc_libblock_notest(type);
        if (id != null) {
            ListBaseUtil.BLI_addtail(lb, id);
            id.us = 1;
            id.icon_id = 0;
            id.name[0] = (byte) (type >> 8);
            id.name[1] = (byte) (type);
            new_id(lb, id, name, offset);
            /* alphabetic insterion: is in new_id */
        }
        return id;
    }

    /* GS reads the memory pointed at in a specific ordering.
       only use this definition, makes little and big endian systems
       work fine, in conjunction with MAKE_ID */
    public static final short GS(byte[] a) {
        return (short) (((a[0] & 0xFF) << 8) | ((a[1] & 0xFF) << 0));
    }

///* by spec, animdata is first item after ID */
///* we still read .adt itself, to ensure compiler warns when it doesnt exist */
//static void id_copy_animdata(ID *id)
//{
//	switch(GS(id.name)) {
//		case ID_OB:
//			((Object *)id).adt= BKE_copy_animdata(((Object *)id).adt);
//			break;
//		case ID_CU:
//			((Curve *)id).adt= BKE_copy_animdata(((Curve *)id).adt);
//			break;
//		case ID_CA:
//			((Camera *)id).adt= BKE_copy_animdata(((Camera *)id).adt);
//			break;
//		case ID_KE:
//			((Key *)id).adt= BKE_copy_animdata(((Key *)id).adt);
//			break;
//		case ID_LA:
//			((Lamp *)id).adt= BKE_copy_animdata(((Lamp *)id).adt);
//			break;
//		case ID_MA:
//			((Material *)id).adt= BKE_copy_animdata(((Material *)id).adt);
//			break;
//		case ID_NT:
//			((bNodeTree *)id).adt= BKE_copy_animdata(((bNodeTree *)id).adt);
//			break;
//		case ID_SCE:
//			((Scene *)id).adt= BKE_copy_animdata(((Scene *)id).adt);
//			break;
//		case ID_TE:
//			((Tex *)id).adt= BKE_copy_animdata(((Tex *)id).adt);
//			break;
//		case ID_WO:
//			((World *)id).adt= BKE_copy_animdata(((World *)id).adt);
//			break;
//	}
//
//}
//
///* used everywhere in blenkernel and text.c */
//void *copy_libblock(void *rt)
//{
//	ID *idn, *id;
//	ListBase *lb;
//	char *cp, *cpn;
//	int idn_len;
//
//	id= rt;
//
//	lb= wich_libbase(G.main, GS(id.name));
//	idn= alloc_libblock(lb, GS(id.name), id.name+2);
//
//	if(idn==NULL) {
//		printf("ERROR: Illegal ID name for %s (Crashing now)\n", id.name);
//	}
//
//	idn_len= MEM_allocN_len(idn);
//	if(idn_len - sizeof(ID) > 0) {
//		cp= (char *)id;
//		cpn= (char *)idn;
//		memcpy(cpn+sizeof(ID), cp+sizeof(ID), idn_len - sizeof(ID));
//	}
//
//	id.newid= idn;
//	idn.flag |= LIB_NEW;
//	if (id.properties) idn.properties = IDP_CopyProperty(id.properties);
//
//	id_copy_animdata(id);
//
//	return idn;
//}

    public static void free_library(Library lib) {
        /* no freeing needed for libraries yet */
    }

//static void (*free_windowmanager_cb)(bContext *, wmWindowManager *)= NULL;
//
//void set_free_windowmanager_cb(void (*func)(bContext *C, wmWindowManager *) )
//{
//	free_windowmanager_cb= func;
//}
//
///* used in headerbuttons.c image.c mesh.c screen.c sound.c and library.c */
//void free_libblock(ListBase *lb, void *idv)
//{
//	ID *id= idv;
//
//	switch( GS(id.name) ) {	/* GetShort from util.h */
//		case ID_SCE:
//			free_scene((Scene *)id);
//			break;
//		case ID_LI:
//			free_library((Library *)id);
//			break;
//		case ID_OB:
//			free_object((Object *)id);
//			break;
//		case ID_ME:
//			free_mesh((Mesh *)id);
//			break;
//		case ID_CU:
//			free_curve((Curve *)id);
//			break;
//		case ID_MB:
//			free_mball((MetaBall *)id);
//			break;
//		case ID_MA:
//			free_material((Material *)id);
//			break;
//		case ID_TE:
//			free_texture((Tex *)id);
//			break;
//		case ID_IM:
//			free_image((Image *)id);
//			break;
//		case ID_WV:
//			/* free_wave(id); */
//			break;
//		case ID_LT:
//			free_lattice((Lattice *)id);
//			break;
//		case ID_LA:
//			free_lamp((Lamp *)id);
//			break;
//		case ID_CA:
//			free_camera((Camera*) id);
//			break;
//		case ID_IP:
//			free_ipo((Ipo *)id);
//			break;
//		case ID_KE:
//			free_key((Key *)id);
//			break;
//		case ID_WO:
//			free_world((World *)id);
//			break;
//		case ID_SCR:
//			free_screen((bScreen *)id);
//			break;
//		case ID_VF:
//			free_vfont((VFont *)id);
//			break;
//		case ID_TXT:
//			free_text((Text *)id);
//			break;
//		case ID_SCRIPT:
//			//XXX free_script((Script *)id);
//			break;
//		case ID_SO:
//			sound_free_sound((bSound *)id);
//			break;
//		case ID_GR:
//			free_group((Group *)id);
//			break;
//		case ID_AR:
//			free_armature((bArmature *)id);
//			break;
//		case ID_AC:
//			free_action((bAction *)id);
//			break;
//		case ID_NT:
//			ntreeFreeTree((bNodeTree *)id);
//			break;
//		case ID_BR:
//			free_brush((Brush *)id);
//			break;
//		case ID_PA:
//			psys_free_settings((ParticleSettings *)id);
//			break;
//		case ID_WM:
//			if(free_windowmanager_cb)
//				free_windowmanager_cb(NULL, (wmWindowManager *)id);
//			break;
//		case ID_GD:
//			free_gpencil_data((bGPdata *)id);
//			break;
//	}
//
//	if (id.properties) {
//		IDP_FreeProperty(id.properties);
//		MEM_freeN(id.properties);
//	}
//	BLI_remlink(lb, id);
//	MEM_freeN(id);
//
//}
    /* used in headerbuttons.c image.c mesh.c screen.c sound.c and library.c */
    public static void free_libblock(ListBase lb, Object idv) {
        ID id = (ID) idv;

        switch (GS(id.name)) {	/* GetShort from util.h */
            case DNA_ID.ID_SCE:
                SceneUtil.free_scene((Scene) id);
                break;
            case DNA_ID.ID_LI:
                free_library((Library) id);
                break;
            case DNA_ID.ID_OB:
                ObjectUtil.free_object((bObject) id);
                break;
            case DNA_ID.ID_ME:
                MeshUtil.free_mesh((Mesh) id);
                break;
            case DNA_ID.ID_CU:
//                    free_curve((Curve *)id);
                    break;
            case DNA_ID.ID_MB:
//                    free_mball((MetaBall *)id);
                    break;
            case DNA_ID.ID_MA:
//                    free_material((Material *)id);
                    break;
            case DNA_ID.ID_TE:
//                    free_texture((Tex *)id);
                    break;
            case DNA_ID.ID_IM:
//                    free_image((Image *)id);
                    break;
//            case DNA_ID.ID_WV:
////                    /* free_wave(id); */
//                    break;
            case DNA_ID.ID_LT:
//                    free_lattice((Lattice *)id);
                    break;
            case DNA_ID.ID_LA:
                ObjectUtil.free_lamp((Lamp) id);
                break;
            case DNA_ID.ID_CA:
                ObjectUtil.free_camera((Camera) id);
                break;
            case DNA_ID.ID_IP:
//                    free_ipo((Ipo *)id);
                    break;
            case DNA_ID.ID_KE:
//                    free_key((Key *)id);
                    break;
            case DNA_ID.ID_WO:
//                    free_world((World)id);
                    break;
            case DNA_ID.ID_SCR:
//                    free_screen((bScreen)id);
                    break;
            case DNA_ID.ID_VF:
//                    free_vfont((VFont *)id);
                    break;
            case DNA_ID.ID_TXT:
//                    free_text((Text *)id);
                    break;
            case DNA_ID.ID_SCRIPT:
//                    //XXX free_script((Script *)id);
                    break;
            case DNA_ID.ID_SO:
//                    sound_free_sound((bSound *)id);
                    break;
            case DNA_ID.ID_GR:
//                    free_group((Group *)id);
                    break;
            case DNA_ID.ID_AR:
//                    free_armature((bArmature *)id);
                    break;
            case DNA_ID.ID_AC:
//                    free_action((bAction *)id);
                    break;
            case DNA_ID.ID_NT:
//                    ntreeFreeTree((bNodeTree *)id);
                    break;
            case DNA_ID.ID_BR:
//                    free_brush((Brush *)id);
                    break;
            case DNA_ID.ID_PA:
//                    psys_free_settings((ParticleSettings *)id);
                    break;
            case DNA_ID.ID_WM:
//                if(free_windowmanager_cb)
//                    free_windowmanager_cb(NULL, (wmWindowManager *)id);
                break;
            case DNA_ID.ID_GD:
//                free_gpencil_data((bGPdata *)id);
                break;
        }

//	if (id.properties) {
//		IDP_FreeProperty(id.properties);
//		MEM_freeN(id.properties);
//	}
        ListBaseUtil.BLI_remlink(lb, id);
    }

    /* test users */
    public static void free_libblock_us(ListBase lb, Object idv) {
        ID id = (ID) idv;

        id.us--;

        if (id.us < 0) {
            if (id.lib != null) {
                System.out.printf("ERROR block %s %s users %d\n", StringUtil.toJString(id.lib.name, 0), StringUtil.toJString(id.name, 0), id.us);
            } else {
                System.out.printf("ERROR block %s users %d\n", StringUtil.toJString(id.name, 0), id.us);
            }
        }
        if (id.us == 0) {
            if (GS(id.name) == DNA_ID.ID_OB) {
                ObjectUtil.unlink_object(null, (bObject) id);
            }

            free_libblock(lb, id);
        }
    }

    public static void free_main(Main mainvar) {
        /* also call when reading a file, erase all, etc */
        ListBase[] lbarray = new ListBase[MAX_LIBARRAY];
        int a;

        a = set_listbasepointers(mainvar, lbarray);
        while (a-- != 0) {
            ListBase lb = lbarray[a];
            ID id;

            while ((id = (ID) lb.first) != null) {
                free_libblock(lb, id);
            }
        }
    }

///* ***************** ID ************************ */
//
//
//ID *find_id(char *type, char *name)		/* type: "OB" or "MA" etc */
//{
//	ID *id;
//	ListBase *lb;
//
//	lb= wich_libbase(G.main, GS(type));
//
//	id= lb.first;
//	while(id) {
//		if(id.name[2]==name[0] && strcmp(id.name+2, name)==0 )
//			return id;
//		id= id.next;
//	}
//	return 0;
//}

    public static void get_flags_for_id(ID id, byte[] buf) {
        boolean isfake = (id.flag & DNA_ID.LIB_FAKEUSER)!=0;
        boolean isnode = false;
        /* Writeout the flags for the entry, note there
         * is a small hack that writes 5 spaces instead
         * of 4 if no flags are displayed... this makes
         * things usually line up ok - better would be
         * to have that explicit, oh well - zr
         */

        if (GS(id.name) == DNA_ID.ID_MA) {
            isnode = ((Material) id).use_nodes != 0;
        }
//	if(GS(id.name) == DNA_ID.ID_TE) {
//            isnode = ((Tex) id).use_nodes;
//        }

        String str;
        if (id.us < 0) {
            str = String.format("-1W ");
        } else if (id.lib == null && !isfake && id.us != 0 && !isnode) {
            str = String.format("     ");
        } else if (isnode) {
            str = String.format("%c%cN%c ", id.lib != null ? 'L' : ' ', isfake ? 'F' : ' ', (id.us == 0) ? 'O' : ' ');
        } else {
            str = String.format("%c%c%c ", id.lib != null ? 'L' : ' ', isfake ? 'F' : ' ', (id.us == 0) ? 'O' : ' ');
        }
        StringUtil.strcpy(buf, 0, StringUtil.toCString(str), 0);
    }

    public static final int IDPUP_NO_VIEWER = 1;

//    public static void IDnames_to_dyn_pupstring(StringBuilder pupds, ListBase lb, ID link, short[] nr, int hideflag) {
//        int i, nids = listbase.BLI_countlist(lb);
//
//        if (nr != null) {
//            nr[0] = -1;
//        }
//
//        if (nr != null && nids > MAX_IDPUP) {
//            pupds.append("DataBrowse %x-2");
//            nr[0] = -2;
//        } else {
//            ID id;
//
//            for (i = 0, id = (ID) lb.first; id != null; id = (ID) id.next, i++) {
//                byte[] buf = new byte[32];
//
//                if (nr != null && id == link) {
//                    nr[0] = (short) (i + 1);
//                }
//
//                if ((U.uiflag & UserDefType.USER_HIDE_DOT) != 0 && id.name[2] == '.') {
//                    continue;
//                }
//                if ((hideflag & IDPUP_NO_VIEWER) != 0) {
//                    if (GS(id.name) == DNA_ID.ID_IM) {
//                        if (((Image) id).source == ImageUtil.IMA_SRC_VIEWER) {
//                            continue;
//                        }
//                    }
//                }
//
//                get_flags_for_id(id, buf);
//
//                pupds.append(string.toJString(buf, 0));
//                pupds.append(string.toJString(id.name, 2));
//                pupds.append(String.format("%%x%d", i + 1));
//
//                /* icon */
//                switch (GS(id.name)) {
//                    case DNA_ID.ID_MA: /* fall through */
//                    case DNA_ID.ID_TE: /* fall through */
//                    case DNA_ID.ID_IM: /* fall through */
//                    case DNA_ID.ID_WO: /* fall through */
//                    case DNA_ID.ID_LA: /* fall through */
//                        pupds.append(String.format("%%i%d", Icons.BKE_icon_getid(id)));
//                        break;
//                    default:
//                        break;
//                }
//
//                if (id.next != null) {
//                    pupds.append("|");
//                }
//            }
//        }
//    }

    /* used by headerbuttons.c buttons.c editobject.c editseq.c */
    /* if nr==NULL no MAX_IDPUP, this for non-header browsing */
//    public static String IDnames_to_pupstring(String title, String extraops, ListBase lb, ID link, short[] nr) {
//        StringBuilder pupds = new StringBuilder();
//
//        if (title != null) {
//            pupds.append(title);
//            pupds.append("%t|");
//        }
//
//        if (extraops != null) {
//            pupds.append(extraops);
//            if (pupds.length() != 0) {
//                pupds.append("|");
//            }
//        }
//
//        IDnames_to_dyn_pupstring(pupds, lb, link, nr, 0);
//
//        return pupds.toString();
//    }

///* skips viewer images */
//void IMAnames_to_pupstring(char **str, char *title, char *extraops, ListBase *lb, ID *link, short *nr)
//{
//	DynStr *pupds= BLI_dynstr_new();
//
//	if (title) {
//		BLI_dynstr_append(pupds, title);
//		BLI_dynstr_append(pupds, "%t|");
//	}
//
//	if (extraops) {
//		BLI_dynstr_append(pupds, extraops);
//		if (BLI_dynstr_get_len(pupds))
//			BLI_dynstr_append(pupds, "|");
//	}
//
//	IDnames_to_dyn_pupstring(pupds, lb, link, nr, IDPUP_NO_VIEWER);
//
//	*str= BLI_dynstr_get_cstring(pupds);
//	BLI_dynstr_free(pupds);
//}

/* used by buttons.c library.c mball.c */
    public static void splitIDname(byte[] name, int offset, byte[] left, int[] nr) {
        int a;

        nr[0] = 0;
        StringUtil.strncpy(left, 0, name, offset, 21);

        a = StringUtil.strlen(name, offset);
        if (a > 1 && name[offset + a - 1] == '.') {
            return;
        }

        while ((a--) != 0) {
            if (name[offset + a] == '.') {
                left[a] = 0;
                nr[0] = (int) StringUtil.atol(name, offset + a + 1);
                return;
            }
            if (Character.isDigit(name[offset + a] & 0xFF) == false) {
                break;
            }

            left[a] = 0;
        }
        StringUtil.strcpy(left, 0, name, offset);
    }

//static void sort_alpha_id(ListBase *lb, ID *id)
//{
//	ID *idtest;
//
//	/* insert alphabetically */
//	if(lb.first!=lb.last) {
//		BLI_remlink(lb, id);
//
//		idtest= lb.first;
//		while(idtest) {
//			if(BLI_strcasecmp(idtest.name, id.name)>0 || idtest.lib) {
//				BLI_insertlinkbefore(lb, idtest, id);
//				break;
//			}
//			idtest= idtest.next;
//		}
//		/* as last */
//		if(idtest==0) {
//			BLI_addtail(lb, id);
//		}
//	}
//
//}

    /*
     * Check to see if an ID name is already used, and find a new one if so.
     * Return 1 if created a new name (returned in name).
     *
     * Normally the ID that's being check is already in the ListBase, so ID *id
     * points at the new entry.  The Python library module needs to know what
     * the name of a datablock will be before it is appended; in this case ID *id
     * id is NULL;
     */
    public static int check_for_dupid(ListBase<ID> lb, ID id, byte[] name) {
        ID idtest;
        int nr = 0, nrtest, a;
        final int maxtest = 32;
        byte[] left = new byte[32], leftest = new byte[32], in_use = new byte[32];

        /* make sure input name is terminated properly */
        if (StringUtil.strlen(name, 0) > 21) {
            name[21] = 0;
        }

        while (true) {

            /* phase 1: id already exists? */
            for (idtest = (ID) lb.first; idtest != null; idtest = (ID) idtest.next) {
                /* if idtest is not a lib */
                if (id != idtest && idtest.lib == null) {
                    /* do not test alphabetic! */
                    /* optimized */
                    if (idtest.name[2] == name[0]) {
                        if (StringUtil.strcmp(name, 0, idtest.name, 2) == 0) {
                            break;
                        }
                    }
                }
            }

            /* if there is no double, done */
            if (idtest == null) {
                return 0;
            }

            /* we have a dup; need to make a new name */
            /* quick check so we can reuse one of first 32 ids if vacant */
            Arrays.fill(in_use, (byte) 0);

            /* get name portion, number portion ("name.number") */
            int[] nr_p = {nr};
            splitIDname(name, 0, left, nr_p);
            nr = nr_p[0];

            /* if new name will be too long, truncate it */
            if (nr > 999 && StringUtil.strlen(left, 0) > 16) {
                left[16] = 0;
            } else if (StringUtil.strlen(left, 0) > 17) {
                left[17] = 0;
            }

            for (idtest = (ID) lb.first; idtest != null; idtest = (ID) idtest.next) {
                if (id != idtest && idtest.lib == null) {
                    int[] nrtest_p = new int[1];
                    splitIDname(idtest.name, 2, leftest, nrtest_p);
                    nrtest = nrtest_p[0];
                    /* if base names match... */
                    /* optimized */
                    if (left[0] == leftest[0] && StringUtil.strcmp(left, 0, leftest, 0) == 0) {
                        if (nrtest < maxtest) {
                            in_use[nrtest] = 1;	/* mark as used */
                        }
                        if (nr <= nrtest) {
                            nr = nrtest + 1;		/* track largest unused */
                        }
                    }
                }
            }

            /* decide which value of nr to use */
            for (a = 0; a < maxtest; a++) {
                if (a >= nr) {
                    break;	/* stop when we've check up to biggest */
                }
                if (in_use[a] == 0) { /* found an unused value */
                    nr = a;
                    break;
                }
            }

            /* if non-numbered name was not in use, reuse it */
            if (nr == 0) {
                StringUtil.strcpy(name, 0, left, 0);
            } else {
                if (nr > 999 && StringUtil.strlen(left, 0) > 16) {
                    /* this would overflow name buffer */
                    left[16] = 0;
                    StringUtil.strcpy(name, 0, left, 0);
                    continue;
                }
                /* this format specifier is from hell... */
                String namef = String.format("%s.%d", StringUtil.toJString(left, 0), nr);
                StringUtil.strcpy(name, 0, StringUtil.toCString(namef), 0);
            }
            return 1;
        }
    }

    /*
     * Only for local blocks: external en indirect blocks already have a
     * unique ID.
     *
     * return 1: created a new name
     */
    public static int new_id(ListBase<ID> lb, ID id, byte[] tname, int offset) {
        int result = 0;
        byte[] name = new byte[22];

        /* if library, don't rename */
        if (id.lib != null) {
            return 0;
        }

        /* if no libdata given, look up based on ID */
        if (lb == null) {
            lb = wich_libbase(G.main, GS(id.name));
        }

        if (tname == null) {
            /* if no name given, use name of current ID */
            StringUtil.strncpy(name, 0, id.name, 2, 21);
            result = StringUtil.strlen(id.name, 2);
        } else {
            /* else make a copy (tname args can be const) */
            StringUtil.strncpy(name, 0, tname, offset, 21);
            result = StringUtil.strlen(tname, offset);
        }

        /* if result > 21, strncpy don't put the final '\0' to name. */
        if (result >= 21) {
            name[21] = 0;
        }

        result = check_for_dupid(lb, id, name);
        StringUtil.strcpy(id.name, 2, name, 0);

	/* This was in 2.43 and previous releases
	 * however all data in blender should be sorted, not just duplicate names
	 * sorting should not hurt, but noting just incause it alters the way other
	 * functions work, so sort every time */
	/* if( result )
		sort_alpha_id(lb, id);*/

//	sort_alpha_id(lb, id);

        return result;
    }

///* next to indirect usage in read/writefile also in editobject.c scene.c */
//void clear_id_newpoins()
//{
//	ListBase *lbarray[MAX_LIBARRAY];
//	ID *id;
//	int a;
//
//	a= set_listbasepointers(G.main, lbarray);
//	while(a--) {
//		id= lbarray[a].first;
//		while(id) {
//			id.newid= 0;
//			id.flag &= ~LIB_NEW;
//			id= id.next;
//		}
//	}
//}
//
///* only for library fixes */
//static void image_fix_relative_path(Image *ima)
//{
//	if(ima.id.lib==NULL) return;
//	if(strncmp(ima.name, "//", 2)==0) {
//		BLI_convertstringcode(ima.name, ima.id.lib.filename);
//		BLI_makestringcode(G.sce, ima.name);
//	}
//}
//
//#define LIBTAG(a)	if(a && a.id.lib) {a.id.flag &=~LIB_INDIRECT; a.id.flag |= LIB_EXTERN;}
//
//static void lib_indirect_test_id(ID *id)
//{
//
//	if(id.lib)
//		return;
//
//	if(GS(id.name)==ID_OB) {
//		Object *ob= (Object *)id;
//		bActionStrip *strip;
//		Mesh *me;
//
//		int a;
//
//		// XXX old animation system!
//		for (strip=ob.nlastrips.first; strip; strip=strip.next){
//			LIBTAG(strip.object);
//			LIBTAG(strip.act);
//			LIBTAG(strip.ipo);
//		}
//
//		for(a=0; a<ob.totcol; a++) {
//			LIBTAG(ob.mat[a]);
//		}
//
//		LIBTAG(ob.dup_group);
//		LIBTAG(ob.proxy);
//
//		me= ob.data;
//		LIBTAG(me);
//	}
//}
//
//
///* if lib!=NULL, only all from lib local */
//void all_local(Library *lib, int untagged_only)
//{
//	ListBase *lbarray[MAX_LIBARRAY], tempbase={0, 0};
//	ID *id, *idn;
//	int a;
//
//	a= set_listbasepointers(G.main, lbarray);
//	while(a--) {
//		id= lbarray[a].first;
//
//		while(id) {
//			id.newid= NULL;
//			idn= id.next;		/* id is possibly being inserted again */
//
//			/* The check on the second line (LIB_APPEND_TAG) is done so its
//			 * possible to tag data you dont want to be made local, used for
//			 * appending data, so any libdata alredy linked wont become local
//			 * (very nasty to discover all your links are lost after appending)
//			 * */
//			if(id.flag & (LIB_EXTERN|LIB_INDIRECT|LIB_NEW) &&
//			  (untagged_only==0 || !(id.flag & LIB_APPEND_TAG)))
//			{
//				if(lib==NULL || id.lib==lib) {
//					id.flag &= ~(LIB_EXTERN|LIB_INDIRECT|LIB_NEW);
//
//					if(id.lib) {
//						/* relative file patch */
//						if(GS(id.name)==ID_IM)
//							image_fix_relative_path((Image *)id);
//
//						id.lib= NULL;
//						new_id(lbarray[a], id, 0);	/* new_id only does it with double names */
//						sort_alpha_id(lbarray[a], id);
//					}
//				}
//			}
//			id= idn;
//		}
//
//		/* patch2: make it aphabetically */
//		while( (id=tempbase.first) ) {
//			BLI_remlink(&tempbase, id);
//			BLI_addtail(lbarray[a], id);
//			new_id(lbarray[a], id, 0);
//		}
//	}
//
//	/* patch 3: make sure library data isn't indirect falsely... */
//	a= set_listbasepointers(G.main, lbarray);
//	while(a--) {
//		for(id= lbarray[a].first; id; id=id.next)
//			lib_indirect_test_id(id);
//	}
//}

public static void test_idbutton(byte[] name, int offset) {
        /* called from buttons: when name already exists: call new_id */
        ListBase lb;
        ID idtest;

//        lb = wich_libbase(G.main, GS(name-2));
        lb = wich_libbase(G.main, GS(name));
        if (lb == null) {
            return;
        }

        /* search for id */
        idtest = (ID) lb.first;
        while (idtest != null) {
            if (StringUtil.strcmp(idtest.name, 2, name, offset) == 0) {
                break;
            }
            idtest = (ID) idtest.next;
        }

        if (idtest != null) {
            if (new_id(lb, idtest, name, offset) == 0) {
//                sort_alpha_id(lb, idtest);
            }
        }
    }

//void text_idbutton(struct ID *id, char *text)
//{
//	if(id) {
//		if(GS(id.name)==ID_SCE)
//			strcpy(text, "SCE: ");
//        else if(GS(id.name)==ID_SCE)
//			strcpy(text, "SCR: ");
//        else if(GS(id.name)==ID_MA && ((Material*)id).use_nodes)
//			strcpy(text, "NT: ");
//        else {
//			text[0]= id.name[0];
//			text[1]= id.name[1];
//			text[2]= ':';
//			text[3]= ' ';
//			text[4]= 0;
//        }
//	}
//	else
//		strcpy(text, "");
//}
//
//void rename_id(ID *id, char *name)
//{
//	ListBase *lb;
//
//	strncpy(id.name+2, name, 21);
//	lb= wich_libbase(G.main, GS(id.name) );
//
//	new_id(lb, id, name);
//}
}
