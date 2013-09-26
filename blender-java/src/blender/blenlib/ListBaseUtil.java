/* util.c
 *
 * various string, file, list operations.
 *
 *
 * $Id: listbase.c 21094 2009-06-23 00:09:26Z gsrb3d $
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
 * 
 */
package blender.blenlib;

import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;

public class ListBaseUtil {

    /* implementation */

    /* Ripped this from blender.c */
    public static void addlisttolist(ListBase list1, ListBase list2) {
        if (list2.first == null) {
            return;
        }

        if (list1.first == null) {
            list1.first = list2.first;
            list1.last = list2.last;
        } else {
            ((Link) list1.last).next = list2.first;
            ((Link) list2.first).prev = list1.last;
            list1.last = list2.last;
        }
        list2.first = list2.last = null;
    }

    public static void BLI_addhead(ListBase listbase, Link vlink) {
        Link link = vlink;

        if (link == null) {
            return;
        }
        if (listbase == null) {
            return;
        }

        link.next = (Link) listbase.first;
        link.prev = null;

        if (listbase.first != null) {
            ((Link) listbase.first).prev = link;
        }
        if (listbase.last == null) {
            listbase.last = link;
        }
        listbase.first = link;
    }

    public static void BLI_addtail(ListBase listbase, Link vlink) {
        Link link = vlink;

        if (link == null) {
            return;
        }
        if (listbase == null) {
            return;
        }

        link.next = null;
        link.prev = (Link) listbase.last;

        if (listbase.last != null) {
            ((Link) listbase.last).next = link;
        }
        if (listbase.first == null) {
            listbase.first = link;
        }
        listbase.last = link;
    }

    public static void BLI_remlink(ListBase listbase, Link vlink) {
        Link link = vlink;

        if (link == null) {
            return;
        }
        if (listbase == null) {
            return;
        }

        if (link.next != null) {
            ((Link) link.next).prev = link.prev;
        }
        if (link.prev != null) {
            ((Link) link.prev).next = link.next;
        }

        if (listbase.last == link) {
            listbase.last = link.prev;
        }
        if (listbase.first == link) {
            listbase.first = link.next;
        }
    }

    public static void BLI_freelinkN(ListBase listbase, Link vlink) {
        Link link = vlink;

        if (link == null) {
            return;
        }
        if (listbase == null) {
            return;
        }

        BLI_remlink(listbase, link);
    }

//void BLI_insertlink(ListBase *listbase, void *vprevlink, void *vnewlink)
//{
//	Link *prevlink= vprevlink;
//	Link *newlink= vnewlink;
//
//	/* newlink comes after prevlink */
//	if (newlink == NULL) return;
//	if (listbase == NULL) return;
//
//	/* empty list */
//	if (listbase.first == NULL) {
//
//		listbase.first= newlink;
//		listbase.last= newlink;
//		return;
//	}
//
//	/* insert before first element */
//	if (prevlink == NULL) {
//		newlink.next= listbase.first;
//		newlink.prev= 0;
//		newlink.next.prev= newlink;
//		listbase.first= newlink;
//		return;
//	}
//
//	/* at end of list */
//	if (listbase.last== prevlink)
//		listbase.last = newlink;
//
//	newlink.next= prevlink.next;
//	prevlink.next= newlink;
//	if (newlink.next) newlink.next.prev= newlink;
//	newlink.prev= prevlink;
//}
//
///* This uses insertion sort, so NOT ok for large list */
//void BLI_sortlist(ListBase *listbase, int (*cmp)(void *, void *))
//{
//	Link *current = NULL;
//	Link *previous = NULL;
//	Link *next = NULL;
//
//	if (cmp == NULL) return;
//	if (listbase == NULL) return;
//
//	if (listbase.first != listbase.last)
//	{
//		for( previous = listbase.first, current = previous.next; current; current = next )
//		{
//			next = current.next;
//			previous = current.prev;
//
//			BLI_remlink(listbase, current);
//
//			while(previous && cmp(previous, current) == 1)
//			{
//				previous = previous.prev;
//			}
//
//			BLI_insertlinkafter(listbase, previous, current);
//		}
//	}
//}

public static void BLI_insertlinkafter(ListBase listbase, Link vprevlink, Link vnewlink)
{
	Link prevlink= vprevlink;
	Link newlink= vnewlink;

	/* newlink before nextlink */
	if (newlink == null) return;
	if (listbase == null) return;

	/* empty list */
	if (listbase.first == null) {
		listbase.first= newlink;
		listbase.last= newlink;
		return;
	}

	/* insert at head of list */
	if (prevlink == null) {
		newlink.prev = null;
		newlink.next = listbase.first;
		((Link)listbase.first).prev = newlink;
		listbase.first = newlink;
		return;
	}

	/* at end of list */
	if (listbase.last == prevlink)
		listbase.last = newlink;

	newlink.next = prevlink.next;
	newlink.prev = prevlink;
	prevlink.next = newlink;
	if (newlink.next!=null) ((Link)newlink.next).prev = newlink;
}

//void BLI_insertlinkbefore(ListBase *listbase, void *vnextlink, void *vnewlink)
//{
//	Link *nextlink= vnextlink;
//	Link *newlink= vnewlink;
//
//	/* newlink before nextlink */
//	if (newlink == NULL) return;
//	if (listbase == NULL) return;
//
//	/* empty list */
//	if (listbase.first == NULL) {
//		listbase.first= newlink;
//		listbase.last= newlink;
//		return;
//	}
//
//	/* insert at end of list */
//	if (nextlink == NULL) {
//		newlink.prev= listbase.last;
//		newlink.next= 0;
//		((Link *)listbase.last).next= newlink;
//		listbase.last= newlink;
//		return;
//	}
//
//	/* at beginning of list */
//	if (listbase.first== nextlink)
//		listbase.first = newlink;
//
//	newlink.next= nextlink;
//	newlink.prev= nextlink.prev;
//	nextlink.prev= newlink;
//	if (newlink.prev) newlink.prev.next= newlink;
//}
//
//
//void BLI_freelist(ListBase *listbase)
//{
//	Link *link, *next;
//
//	if (listbase == NULL)
//		return;
//
//	link= listbase.first;
//	while (link) {
//		next= link.next;
//		free(link);
//		link= next;
//	}
//
//	listbase.first= NULL;
//	listbase.last= NULL;
//}

    public static void BLI_freelistN(ListBase listbase) {
        Link link, next;

        if (listbase == null) {
            return;
        }

        link = (Link) listbase.first;
        while (link != null) {
            next = (Link) link.next;
            link = next;
        }

        listbase.first = null;
        listbase.last = null;
    }

    public static int BLI_countlist(ListBase listbase) {
        Link link;
        int count = 0;

        if (listbase != null) {
            link = (Link) listbase.first;
            while (link != null) {
                count++;
                link = (Link) link.next;
            }
        }
        return count;
    }

    public static Object BLI_findlink(ListBase listbase, int number) {
        Link link = null;

        if (number >= 0) {
            link = (Link) listbase.first;
            while (link != null && number != 0) {
                number--;
                link = (Link) link.next;
            }
        }

        return link;
    }

    public static int BLI_findindex(ListBase listbase, Object vlink) {
        Link link = null;
        int number = 0;

        if (listbase == null) {
            return -1;
        }
        if (vlink == null) {
            return -1;
        }

        link = (Link) listbase.first;
        while (link != null) {
            if (link == vlink) {
                return number;
            }

            number++;
            link = (Link) link.next;
        }

        return -1;
    }
    
    public static interface OffsetOf {
    	public String get(Link link);
    }
    
    public static Object BLI_findstring(ListBase listbase, String id, OffsetOf offset)
    {
    	Link link= null;
    	String id_iter;

    	if (listbase == null) return null;

    	link= (Link)listbase.first;
    	while (link!=null) {
//    		id_iter= ((const char *)link) + offset;
    		id_iter= offset.get(link);

//    		if(id[0] == id_iter[0] && strcmp(id, id_iter)==0)
    		if(id.equals(id_iter))
    			return link;

    		link= (Link)link.next;
    	}

    	return null;
    }

    public static Object BLI_findstring_ptr(ListBase listbase, String id, OffsetOf offset)
    {
    	Link link= null;
    	String id_iter;

    	if (listbase == null) return null;

    	link= (Link)listbase.first;
    	while (link!=null) {
    		/* exact copy of BLI_findstring(), except for this line */
//    		id_iter= *((const char **)(((const char *)link) + offset));
    		id_iter= offset.get(link);

//    		if(id[0] == id_iter[0] && strcmp(id, id_iter)==0)
    		if(id.equals(id_iter))
    			return link;

    		link= (Link)link.next;
    	}

    	return null;
    }

    public static int BLI_findstringindex(ListBase listbase, String id, OffsetOf offset)
    {
    	Link link= null;
    	String id_iter;
    	int i= 0;

    	if (listbase == null) return -1;

    	link= (Link)listbase.first;
    	while (link!=null) {
//    		id_iter= ((const char *)link) + offset;
    		id_iter= offset.get(link);

//    		if(id[0] == id_iter[0] && strcmp(id, id_iter)==0)
    		if(id.equals(id_iter))
    			return i;
    		i++;
    		link= (Link)link.next;
    	}

    	return -1;
    }

    /* copy from 2 to 1 */
    public static void BLI_duplicatelist(ListBase list1, ListBase list2) {
        Link link1, link2;

        list1.first = list1.last = null;

        link2 = (Link) list2.first;
        while (link2 != null) {
            try {
//                link1 = (Link) link2.clone();
                link1 = link2.copy();
                BLI_addtail(list1, link1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            link2 = (Link) link2.next;
        }
    }

}
