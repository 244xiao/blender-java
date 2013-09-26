/**
 * blenlib/DNA_ID.h (mar-2001 nzc)
 *
 * ID and Library types, which are fundamental for sdna,
 *
 * $Id: DNA_ID.h 19811 2009-04-20 10:13:55Z ton $
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

public class DNA_ID {

//typedef struct IDPropertyData {
//	void *pointer;
//	ListBase group;
//	int val, val2; /*note, we actually fit a double into these two ints*/
//} IDPropertyData;
//
//typedef struct IDProperty {
//	struct IDProperty *next, *prev;
//	char type, subtype;
//	short flag;
//	char name[32];
//	int saved; /*saved is used to indicate if this struct has been saved yet.
//				seemed like a good idea as a pad var was needed anyway :)*/
//	IDPropertyData data;	/* note, alignment for 64 bits */
//	int len; /* array length, also (this is important!) string length + 1.
//	            the idea is to be able to reuse array realloc functions on strings.*/
//	/*totallen is total length of allocated array/string, including a buffer.
//	  Note that the buffering is mild; the code comes from python's list implementation.*/
//	int totallen; /*strings and arrays are both buffered, though the buffer isn't
//	                saved.*/
//} IDProperty;

public static final int MAX_IDPROP_NAME=	32;
public static final int DEFAULT_ALLOC_FOR_NULL_STRINGS=	64;

/*->type*/
public static final int IDP_STRING=		0;
public static final int IDP_INT=			1;
public static final int IDP_FLOAT=		2;
public static final int IDP_ARRAY=		5;
public static final int IDP_GROUP=		6;
/* the ID link property type hasn't been implemented yet, this will require
   some cleanup of blenkernel, most likely.*/
public static final int IDP_ID=			7;
public static final int IDP_DOUBLE=		8;
public static final int IDP_IDPARRAY=	9;
public static final int IDP_NUMTYPES=	10;

/* add any future new id property types here.*/

/* watch it: Sequence has identical beginning. */
/**
 * ID is the first thing included in all serializable types. It
 * provides a common handle to place all data in double-linked lists.
 * */

public static final int MAX_ID_NAME=	24;

///* There's a nasty circular dependency here.... void* to the rescue! I
// * really wonder why this is needed. */
//typedef struct ID {
//	void *next, *prev;
//	struct ID *newid;
//	struct Library *lib;
//	char name[24];
//	short us;
//	/**
//	 * LIB_... flags report on status of the datablock this ID belongs
//	 * to.
//	 */
//	short flag;
//	int icon_id;
//	IDProperty *properties;
//} ID;
//
///**
// * For each library file used, a Library struct is added to Main
// * WARNING: readfile.c, expand_doit() reads this struct without DNA check!
// */
//typedef struct Library {
//	ID id;
//	ID *idblock;
//	struct FileData *filedata;
//	char name[240];			/* revealed in the UI, can store relative path */
//	char filename[240];		/* expanded name, not relative, used while reading */
//	int tot, pad;			/* tot, idblock and filedata are only fo read and write */
//	struct Library *parent;	/* for outliner, showing dependency */
//} Library;

public static final int PREVIEW_MIPMAPS= 2;
public static final int PREVIEW_MIPMAP_ZERO= 0;
public static final int PREVIEW_MIPMAP_LARGE= 1;

//typedef struct PreviewImage {
//	unsigned int w[2];
//	unsigned int h[2];
//	short changed[2];
//	short pad0, pad1;
//	unsigned int * rect[2];
//} PreviewImage;

/**
 * Defines for working with IDs.
 *
 * The tags represent types! This is a dirty way of enabling RTTI. The
 * sig_byte end endian defines aren't really used much.
 *
 **/

//#if defined(__sgi) || defined(__sparc) || defined(__sparc__) || defined (__PPC__) || defined (__ppc__)  || defined (__hppa__) || defined (__BIG_ENDIAN__)
///* big endian */
//public static final int MAKE_ID2(c, d)		( (c)<<8 | (d) )
//public static final int MOST_SIG_BYTE				0
//public static final int BBIG_ENDIAN
//#else
///* little endian  */
//public static final int MAKE_ID2(c, d)		( (d)<<8 | (c) )
//public static final int MOST_SIG_BYTE				1
//public static final int BLITTLE_ENDIAN
//#endif

/* ID from database */
public static final short ID_SCE=		('S' << 8) | ('C');
public static final short ID_LI=		('L' << 8) | ('I');
public static final short ID_OB=		('O' << 8) | ('B');
public static final short ID_ME=		('M' << 8) | ('E');
public static final short ID_CU=		('C' << 8) | ('U');
public static final short ID_MB=		('M' << 8) | ('B');
public static final short ID_MA=		('M' << 8) | ('A');
public static final short ID_TE=		('T' << 8) | ('E');
public static final short ID_IM=		('I' << 8) | ('M');
//public static final short ID_IK=		('I' << 8) | ('K');
//public static final short ID_WV=		('W' << 8) | ('V');
public static final short ID_LT=		('L' << 8) | ('T');
//public static final short ID_SE=		('S' << 8) | ('E');
//public static final short ID_LF=		('L' << 8) | ('F');
public static final short ID_LA=		('L' << 8) | ('A');
public static final short ID_CA=		('C' << 8) | ('A');
public static final short ID_IP=		('I' << 8) | ('P');
public static final short ID_KE=		('K' << 8) | ('E');
public static final short ID_WO=		('W' << 8) | ('O');
public static final short ID_SCR=		('S' << 8) | ('R');
public static final short ID_SCRN=		('S' << 8) | ('N');
public static final short ID_VF=		('V' << 8) | ('F');
public static final short ID_TXT=		('T' << 8) | ('X');
public static final short ID_SO=		('S' << 8) | ('O');
public static final short ID_GR=		('G' << 8) | ('R');
public static final short ID_ID=		('I' << 8) | ('D');
public static final short ID_AR=		('A' << 8) | ('R');
public static final short ID_AC=		('A' << 8) | ('C');
public static final short ID_SCRIPT=            ('P' << 8) | ('Y');
public static final short ID_NT=		('N' << 8) | ('T');
public static final short ID_BR=		('B' << 8) | ('R');
public static final short ID_PA=		('P' << 8) | ('A');
public static final short ID_GD=		('G' << 8) | ('D');
public static final short ID_WM=		('W' << 8) | ('M');

	/* NOTE! Fake IDs, needed for g.sipo->blocktype or outliner */
public static final short ID_SEQ=		('S' << 8) | ('Q');
			/* constraint */
public static final short ID_CO=		('C' << 8) | ('O');
			/* pose (action channel, used to be ID_AC in code, so we keep code for backwards compat) */
public static final short ID_PO=		('A' << 8) | ('C');
			/* used in outliner... */
public static final short ID_NLA=		('N' << 8) | ('L');
			/* fluidsim Ipo */
public static final short ID_FLUIDSIM=          ('F' << 8) | ('S');

/* id->flag: set frist 8 bits always at zero while reading */
public static final int LIB_LOCAL=		0;
public static final int LIB_EXTERN=		1;
public static final int LIB_INDIRECT=	2;
public static final int LIB_TEST=		8;
public static final int LIB_TESTEXT=		9;
public static final int LIB_TESTIND=		10;
public static final int LIB_READ=		16;
public static final int LIB_NEEDLINK=	32;

public static final int LIB_NEW=			256;
public static final int LIB_FAKEUSER=	512;
/* free test flag */
public static final int LIB_DOIT=		1024;
/*  */
public static final int LIB_APPEND_TAG=	2048;

}
