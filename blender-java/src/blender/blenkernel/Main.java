/**
 * blenlib/BKE_main.h (mar-2001 nzc)
 *
 * Main is the root of the 'database' of a Blender context. All data
 * is stuffed into lists, and all these lists are knotted to here. A
 * Blender file is not much more but a binary dump of these
 * lists. This list of lists is not serialized itself.
 *
 * Oops... this should be a _types.h file.
 *
 * $Id: BKE_main.h 19811 2009-04-20 10:13:55Z ton $ 
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

import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.Group;
import blender.makesdna.sdna.Ipo;
import blender.makesdna.sdna.Key;
import blender.makesdna.sdna.Lamp;
import blender.makesdna.sdna.Library;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.World;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmWindowManager;

public class Main extends Link<Main>
{

    public byte[] name = new byte[240];
    public short versionfile, subversionfile;
    public short minversionfile, minsubversionfile;
    
    public Library curlib;
    public ListBase<Scene> scene = new ListBase<Scene>();
    public ListBase<Library> library = new ListBase<Library>();
    public ListBase<bObject> object = new ListBase<bObject>();
    public ListBase<Mesh> mesh = new ListBase<Mesh>();
    public ListBase curve = new ListBase();
    public ListBase mball = new ListBase();
    public ListBase mat = new ListBase();
    public ListBase tex = new ListBase();
    public ListBase image = new ListBase();
    public ListBase wave = new ListBase();
    public ListBase latt = new ListBase();
    public ListBase<Lamp> lamp = new ListBase<Lamp>();
    public ListBase<Camera> camera = new ListBase<Camera>();
    public ListBase<Ipo> ipo = new ListBase<Ipo>(); // XXX depreceated
    public ListBase<Key> key = new ListBase<Key>();
    public ListBase<World> world = new ListBase<World>();
    public ListBase<bScreen> screen = new ListBase<bScreen>();
    public ListBase script = new ListBase();
    public ListBase vfont = new ListBase();
    public ListBase text = new ListBase();
    public ListBase sound = new ListBase();
    public ListBase<Group> group = new ListBase<Group>();
    public ListBase armature = new ListBase();
    public ListBase action = new ListBase();
    public ListBase nodetree = new ListBase();
    public ListBase brush = new ListBase();
    public ListBase particle = new ListBase();
    public ListBase<wmWindowManager> wm = new ListBase<wmWindowManager>();
    public ListBase gpencil = new ListBase();
    
    public String toString() {
    	return wm + "\n";
    }
    
}

