/**
 * imbuf.h (mar-2001 nzc)
 *
 * This header might have to become external...
 *
 * $Id: imbuf.h 21059 2009-06-21 16:18:38Z campbellbarton $ 
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
package blender.imbuf;

import blender.makesdna.sdna.Link;
import java.nio.ByteBuffer;

public class ImBuf extends Link<ImBuf> {

//#if !defined(WIN32)
//#define O_BINARY 0
//#endif
//
//#define SWAP_SHORT(x) (((x & 0xff) << 8) | ((x >> 8) & 0xff))
//#define SWAP_LONG(x) (((x) << 24) | (((x) & 0xff00) << 8) | (((x) >> 8) & 0xff00) | (((x) >> 24) & 0xff))
//
//#define ENDIAN_NOP(x) (x)
//
//#if defined(__sgi) || defined(__sparc) || defined(__sparc__) || defined (__PPC__) || defined (__hppa__) || (defined (__APPLE__) && !defined(__LITTLE_ENDIAN__))
//#define LITTLE_SHORT SWAP_SHORT
//#define LITTLE_LONG SWAP_LONG
//#define BIG_SHORT ENDIAN_NOP
//#define BIG_LONG ENDIAN_NOP
//#else
//#define LITTLE_SHORT ENDIAN_NOP
//#define LITTLE_LONG ENDIAN_NOP
//#define BIG_SHORT SWAP_SHORT
//#define BIG_LONG SWAP_LONG
//#endif
//
//#define malloc(x) MEM_mallocN(x, __FILE__)
//#define free(x) MEM_freeN(x)
//#define calloc(x,y) MEM_callocN((x)*(y), __FILE__)
//#define freelist(x) BLI_freelistN(x)
//
//#ifdef SHLIB
//void *(*ib_calloc)();
//#define calloc(x,y) ib_calloc((x),(y))
//void *(*ib_malloc)();
//#define malloc(x) ib_malloc(x)
//void (*ib_free)();
//#define free(x) ib_free(x)
//void (*ib_memcpy)();
//#define memcpy(x,y,z) ib_memcpy((x),(y),(z))
//int (*ib_abs)();
//#define abs(x) ib_abs(x)
//void (*ib_fprin_tf)();
//#define fprintf ib_fprin_tf
//int (*ib_sprin_tf)();
//#define sprintf ib_sprin_tf
//void (*ib_prin_tf)();
//#define printf ib_prin_tf
//int (*ib_lseek)();
//#define lseek(x,y,z) ib_lseek((x),(y),(z))
//void *(*ib_mmap)();
//#define mmap(u,v,w,x,y,z) ib_mmap((u),(v),(w),(x),(y),(z))
//int (*ib_munmap)();
//#define munmap(x,y) ib_munmap((x),(y))
//int (*ib_open)();
//#define open(x,y) ib_open((x),(y))
//void (*ib_close)();
//#define close(x) ib_close(x)
//int (*ib_write)();
//#define write(x,y,z) ib_write((x),(y),(z))
//int (*ib_read)();
//#define read(x,y,z) ib_read((x),(y),(z))
//int (*ib_fchmod)();
//#define fchmod(x,y) ib_fchmod((x),(y))
//int (*ib_remove)();
//#define remove(x) ib_remove(x)
//size_t (*ib_strlen)();
//#define strlen(x) ib_strlen(x)
//int (*ib_isdigit)();
//#define isdigit(x) ib_isdigit(x)
//char *(*ib_strcpy)();
//#define strcpy(x,y) ib_strcpy((x),(y))
//int (*ib_atoi)();
//#define atoi(x) ib_atoi(x)
//char *(*ib_strcat)();
//#define strcat(x,y) ib_strcat((x),(y))
//int (*ib_stat)();
///* #define stat(x,y) ib_stat((x),(y)) */
//FILE *ib_iob;
//#define _iob ib_iob
//
//#else
//
//#define ib_stat stat
//
//#endif /* SHLIB */
//
//
//#define WIDTHB(x) (((x+15)>>4)<<1)
//
//extern unsigned short *quadr;
//extern float dyuvrgb[4][4];
//extern float rgbdyuv[4][4];
//
//
//typedef struct Adat
//{
//	unsigned short w, h;
//	unsigned short type;
//	unsigned short xorig, yorig;
//	unsigned short pad;
//	float gamma;
//	float distort;
//}Adat;
//
//struct BitMapHeader
//{
//	unsigned short	w, h;		/* in pixels */
//	unsigned short	x, y;
//	char	nPlanes;
//	char	masking;
//	char	compression;
//	char	pad1;
//	unsigned short	transparentColor;
//	char	xAspect, yAspect;
//	short	pageWidth, pageHeight;
//};
//
//#endif	/* IMBUF_H */

public static final int IB_MIPMAP_LEVELS=	10;

/**
 * \brief The basic imbuf type
 * \ingroup imbuf
 * This is the abstraction of an image.  ImBuf is the basic type used for all
 * imbuf operations.
 *
 * REMINDER: if any changes take place, they need to be carried over
 * to source/blender/blenpluginapi/iff.h too, OTHERWISE PLUGINS WON'T
 * WORK CORRECTLY!
 *
 * Also; add new variables to the end to save pain!
 *
 * Also, that iff.h needs to be in the final release "plugins/include" dir, too!
 */
//typedef struct ImBuf {
//	struct ImBuf *next, *prev;	/**< allow lists of ImBufs, for caches or flipbooks */
	public short	x, y;				/**< width and Height of our image buffer */
	public short	skipx;				/**< Width in ints to get to the next scanline */
	public short	depth;		/**< Active amount of bits/bitplanes */
	public short	cbits;		/**< Amount of active bits in cmap */
	public int	mincol;		/**< smallest color in colormap */
	public int	maxcol;		/**< Largest color in colormap */
	public int	type;					/**< 0=abgr, 1=bitplanes */
	public int	ftype;					/**< File type we are going to save as */
	public int[]	cmap;		/**< Color map data. */
	public ByteBuffer rect;		/**< pixel values stored here */
//	public unsigned int	*crect;		/**< color corrected pixel values stored here */
//	public unsigned int	**planes;	/**< bitplanes */
	public int	flags;				/**< Controls which components should exist. */
	public int	mall;				/**< what is malloced internal, and can be freed */
	public short	xorig, yorig;		/**< Cordinates of first pixel of an image used in some formats (example: targa) */
	public byte[]	name=new byte[1023];		/**< The file name assocated with this image */
//	public char	namenull;		/**< Unused don't want to remove it thought messes things up */
	public int	userflags;			/**< Used to set imbuf to Dirty and other stuff */
	public int[]	zbuf;				/**< z buffer data, original zbuffer */
	public float[]  zbuf_float;		/**< z buffer data, camera coordinates */
	public Object   userdata;			/**< temporary storage, only used by baking at the moment */
	public ByteBuffer encodedbuffer;     /**< Compressed image only used with png currently */
	public long   encodedsize;       /**< Size of data written to encodedbuffer */
	public long   encodedbuffersize; /**< Size of encodedbuffer */

	public float[] rect_float;		/**< floating point Rect equivalent
								Linear RGB color space - may need gamma correction to
								sRGB when generating 8bit representations */
	public int channels;			/**< amount of channels in rect_float (0 = 4 channel default) */
	public float dither;			/**< random dither value, for conversion from float -> byte rect */
//	public short profile;			/** color space/profile preset that the byte rect buffer represents */
//	public char profile_filename[256];		/** to be implemented properly, specific filename for custom profiles */

//	public struct MEM_CacheLimiterHandle_s * c_handle; /**< handle for cache limiter */
	public ImgInfo img_info;
	public int refcounter;			/**< Refcounter for multiple users */
	public int index;				/**< reference index for ImBuf lists */

	public ImBuf[] mipmap=new ImBuf[IB_MIPMAP_LEVELS]; /**< MipMap levels, a series of halved images */
//} ImBuf;

/* Moved from BKE_bmfont_types.h because it is a userflag bit mask. */
/**
 * \brief Flags used internally by blender for imagebuffers
 */
//typedef enum {
//	IB_BITMAPFONT = 1 << 0,		/* This image is a font */
//	IB_BITMAPDIRTY = 1 << 1		/* Image needs to be saved is not the same as filename */
//} ImBuf_userflagsMask;


/* From iff.h. This was once moved away by Frank, now Nzc moves it
 * back. Such is the way it is... It is a long list of defines, and
 * there are a few external defines in the back. Most of the stuff is
 * probably imbuf_intern only. This will need to be merged later
 * on. */

/**
 * \name Imbuf Component flags
 * \brief These flags determine the components of an ImBuf struct.
 */
/**@{*/
/** \brief Flag defining the components of the ImBuf struct. */
public static final int IB_rect=			(1 << 0);
public static final int IB_planes=		(1 << 1);
public static final int IB_cmap=			(1 << 2);

public static final int IB_vert=			(1 << 4);
public static final int IB_freem=		(1 << 6);
public static final int IB_test=			(1 << 7);

public static final int IB_ttob=			(1 << 8);
public static final int IB_subdlta=		(1 << 9);
public static final int IB_fields=		(1 << 11);
public static final int IB_zbuf=			(1 << 13);

public static final int IB_mem=			(1 << 14);
public static final int IB_rectfloat=	(1 << 15);
public static final int IB_zbuffloat=	(1 << 16);
public static final int IB_multilayer=	(1 << 17);
public static final int IB_imginfo=		(1 << 18);
public static final int IB_animdeinterlace=      (1 << 19);

/*
 * The bit flag is stored in the ImBuf.ftype variable.
 * Note that the lower 10 bits is used for storing custom flags
 */
public static final int AMI=				(1 << 31);
public static final int PNG=				(1 << 30);
public static final int Anim=	        (1 << 29);
public static final int TGA=				(1 << 28);
public static final int JPG=				(1 << 27);
public static final int BMP=				(1 << 26);

//#ifdef WITH_QUICKTIME
//#define QUICKTIME		(1 << 25)
//#endif

public static final int RADHDR=			(1 << 24);
public static final int TIF=				(1 << 23);
public static final int TIF_16BIT=		(1 << 8 );

public static final int OPENEXR=			(1 << 22);
public static final int OPENEXR_HALF=	(1 << 8 );
public static final int OPENEXR_COMPRESS= (7);

public static final int CINEON=			(1 << 21);
public static final int DPX=				(1 << 20);

//#ifdef WITH_DDS
//#define DDS				(1 << 19)
//#endif

//#ifdef WITH_OPENJPEG
//#define JP2				(1 << 18)
//#define JP2_12BIT			(1 << 17)
//#define JP2_16BIT			(1 << 16)
//#define JP2_YCC			(1 << 15)
//#define JP2_CINE			(1 << 14)
//#define JP2_CINE_48FPS		(1 << 13)
//#endif

public static final int RAWTGA=	        (TGA | 1);

public static final int JPG_STD=	        (JPG | (0 << 8));
public static final int JPG_VID=	        (JPG | (1 << 8));
public static final int JPG_JST=	        (JPG | (2 << 8));
public static final int JPG_MAX=	        (JPG | (3 << 8));
public static final int JPG_MSK=	        (0xffffff00);

public static final int AM_ham=	        (0x0800 | AMI);
public static final int AM_hbrite=       (0x0080 | AMI);

public static final int C233=	1;
public static final int YUVX=	2;
public static final int HAMX=	3;
public static final int TANX=	4;

public static final int AN_c233=			(Anim | C233);
public static final int AN_yuvx=			(Anim | YUVX);
public static final int AN_hamx=			(Anim | HAMX);
public static final int AN_tanx=			(Anim | TANX);
/**@}*/

/**
 * \name Imbuf preset profile tags
 * \brief Some predefined color space profiles that 8 bit imbufs can represent
 */
/**@{*/
//#define IB_PROFILE_NONE			0
//#define IB_PROFILE_LINEAR_RGB	1
//#define IB_PROFILE_SRGB			2
//#define IB_PROFILE_CUSTOM		3
/**@}*/


/** \name Imbuf File Type Tests
 * \brief These macros test if an ImBuf struct is the corresponding file type.
 */
/**@{*/
/** \brief Tests the ImBuf.ftype variable for the file format. */
//#define IS_amiga(x)		(x->ftype & AMI)
//#define IS_ham(x)		((x->ftype & AM_ham) == AM_ham)
//#define IS_hbrite(x)	((x->ftype & AM_hbrite) == AM_hbrite)
//
//#define IS_anim(x)		(x->ftype & Anim)
//#define IS_hamx(x)		(x->ftype == AN_hamx)
//#define IS_tga(x)		(x->ftype & TGA)
//#define IS_png(x)		(x->ftype & PNG)
//#define IS_openexr(x)	(x->ftype & OPENEXR)
//#define IS_jp2(x)              (x->ftype & JP2)
//#define IS_cineon(x)	(x->ftype & CINEON)
//#define IS_dpx(x)		(x->ftype & DPX)
//#define IS_bmp(x)		(x->ftype & BMP)
//#define IS_tiff(x)		(x->ftype & TIF)
//#define IS_radhdr(x)	(x->ftype & RADHDR)
//
//#ifdef WITH_DDS
//#define IS_dds(x)		(x->ftype & DDS)
//#endif
//
//#define IMAGIC 	0732
//#define IS_iris(x)		(x->ftype == IMAGIC)

public static boolean IS_jpg(ImBuf x) { return (x.ftype & JPG)!=0; }
public static boolean IS_stdjpg(ImBuf x) { return (x.ftype & JPG_STD)!=0; }
public static boolean IS_vidjpg(ImBuf x) { return (x.ftype & JPG_VID)!=0; }
public static boolean IS_jstjpg(ImBuf x) { return (x.ftype & JPG_JST)!=0; }
public static boolean IS_maxjpg(ImBuf x) { return (x.ftype & JPG_MAX)!=0; }
/**@}*/

}
