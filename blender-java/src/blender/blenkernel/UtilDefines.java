/* 
$Id: UtilDefines.java,v 1.2 2009/09/18 05:20:13 jladere Exp $

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

public class UtilDefines {

/* these values need to be hardcoded in structs, dna does not recognize defines */
/* also defined in DNA_space_types.h */

//#define ELEM(a, b, c)           ( (a)==(b) || (a)==(c) )
//#define ELEM3(a, b, c, d)       ( ELEM(a, b, c) || (a)==(d) )
//#define ELEM4(a, b, c, d, e)    ( ELEM(a, b, c) || ELEM(a, d, e) )
//#define ELEM5(a, b, c, d, e, f) ( ELEM(a, b, c) || ELEM3(a, d, e, f) )
//#define ELEM6(a, b, c, d, e, f, g)      ( ELEM(a, b, c) || ELEM4(a, d, e, f, g) )
//#define ELEM7(a, b, c, d, e, f, g, h)   ( ELEM3(a, b, c, d) || ELEM4(a, e, f, g, h) )
//#define ELEM8(a, b, c, d, e, f, g, h, i)        ( ELEM4(a, b, c, d, e) || ELEM4(a, f, g, h, i) )
//#define ELEM9(a, b, c, d, e, f, g, h, i, j)        ( ELEM4(a, b, c, d, e) || ELEM5(a, f, g, h, i, j) )
//#define ELEM10(a, b, c, d, e, f, g, h, i, j, k)        ( ELEM4(a, b, c, d, e) || ELEM6(a, f, g, h, i, j, k) )
//#define ELEM11(a, b, c, d, e, f, g, h, i, j, k, l)        ( ELEM4(a, b, c, d, e) || ELEM7(a, f, g, h, i, j, k, l) )
//
///* shift around elements */
//#define SHIFT3(type, a, b, c) { type tmp; tmp = a; a = c; c = b; b = tmp; }
//#define SHIFT4(type, a, b, c, d) { type tmp; tmp = a; a = d; d = c; c = b; b = tmp; }
//
///* string compare */
//#define STREQ(str, a)           ( strcmp((str), (a))==0 )
//#define STREQ2(str, a, b)       ( STREQ(str, a) || STREQ(str, b) )
//#define STREQ3(str, a, b, c)    ( STREQ2(str, a, b) || STREQ(str, c) )

    /* min/max */

    public static final int MIN2(int x, int y) {
        return x < y ? x : y;
    }

    public static final float MIN2(float x, float y) {
        return x < y ? x : y;
    }

    public static final int MIN3(int x, int y, int z) {
        return MIN2(MIN2(x, y), z);
    }

    public static final float MIN3(float x, float y, float z) {
        return MIN2(MIN2(x, y), z);
    }

    public static final int MIN4(int x, int y, int z, int a) {
        return MIN2(MIN2(x, y), MIN2(z, a));
    }

    public static final float MIN4(float x, float y, float z, float a) {
        return MIN2(MIN2(x, y), MIN2(z, a));
    }

    public static final int MAX2(int x, int y) {
        return x > y ? x : y;
    }

    public static final float MAX2(float x, float y) {
        return x > y ? x : y;
    }

    public static final int MAX3(int x, int y, int z) {
        return MAX2(MAX2(x, y), z);
    }

    public static final float MAX3(float x, float y, float z) {
        return MAX2(MAX2(x, y), z);
    }

    public static final int MAX4(int x, int y, int z, int a) {
        return MAX2(MAX2(x, y), MAX2(z, a));
    }

    public static final float MAX4(float x, float y, float z, float a) {
        return MAX2(MAX2(x, y), MAX2(z, a));
    }

    public static final void INIT_MINMAX(float[] min, float[] max) {
        min[0] = min[1] = min[2] = 1.0e30f;
        max[0] = max[1] = max[2] = -1.0e30f;
    }

//#define INIT_MINMAX2(min, max) { (min)[0]= (min)[1]= 1.0e30f; (max)[0]= (max)[1]= -1.0e30f; }

    public static final void DO_MINMAX(float[] vec, float[] min, float[] max) {
        if (min[0] > vec[0]) {
            min[0] = vec[0];
        }
        if (min[1] > vec[1]) {
            min[1] = vec[1];
        }
        if (min[2] > vec[2]) {
            min[2] = vec[2];
        }
        if (max[0] < vec[0]) {
            max[0] = vec[0];
        }
        if (max[1] < vec[1]) {
            max[1] = vec[1];
        }
        if (max[2] < vec[2]) {
            max[2] = vec[2];
        }
    }

//#define DO_MINMAX2(vec, min, max) { if( (min)[0]>(vec)[0] ) (min)[0]= (vec)[0]; \
//							  if( (min)[1]>(vec)[1] ) (min)[1]= (vec)[1]; \
//							  if( (max)[0]<(vec)[0] ) (max)[0]= (vec)[0]; \
//							  if( (max)[1]<(vec)[1] ) (max)[1]= (vec)[1]; }
//
//#define MINSIZE(val, size)	( ((val)>=0.0) ? (((val)<(size)) ? (size): (val)) : ( ((val)>(-size)) ? (-size) : (val)))

    /* some math and copy defines */

//#define SWAP(type, a, b)        { type sw_ap; sw_ap=(a); (a)=(b); (b)=sw_ap; }

    public static final int ABS(int a) {
        return a < 0 ? -a : a;
    }

    public static final float ABS(float a) {
        return a < 0 ? -a : a;
    }

//#define AVG2(x, y)		( 0.5 * ((x) + (y)) )

    public static final byte FTOCHAR(float val) {
        return (val <= 0.0f) ? (byte) 0 : ((val > (1.0f - 0.5f / 255.0f)) ? (byte) 255 : (byte) ((255.0f * val) + 0.5f));
    }

    public static final void VECCOPY(Object v1, Object v2) {
        System.arraycopy(v2, 0, v1, 0, 3);
    }

//    public static final void VECCOPY(float[] v1, float[] v2) {
////    v1[0]= v2[0]; v1[1]= v2[1]; v1[2]= v2[2];
//        System.arraycopy(v2, 0, v1, 0, 3);
//    }

    public static final void VECCOPY(float[] v1, int offset1, float[] v2, int offset2) {
//    v1[0]= v2[0]; v1[1]= v2[1]; v1[2]= v2[2];
        System.arraycopy(v2, offset1, v1, offset2, 3);
    }

    public static final void VECCOPY(short[] v1, float[] v2) {
        v1[0] = (short) v2[0];
        v1[1] = (short) v2[1];
        v1[2] = (short) v2[2];
    }

    public static final void VECCOPY(int[] v1, float[] v2) {
        v1[0] = (int) v2[0];
        v1[1] = (int) v2[1];
        v1[2] = (int) v2[2];
    }

    public static final void VECCOPY(double[] v1, float[] v2) {
        v1[0] = v2[0];
        v1[1] = v2[1];
        v1[2] = v2[2];
    }

//#define VECCOPY2D(v1,v2)          {*(v1)= *(v2); *(v1+1)= *(v2+1);}

    public static final void QUATCOPY(Object v1, Object v2) {
//    v1[0]= v2[0]; v1[1]= v2[1]; v1[2]= v2[2]; v1[3]= v2[3];
        System.arraycopy(v2, 0, v1, 0, 4);
    }

//    public static final void QUATCOPY(float[] v1, float[] v2) {
////    v1[0]= v2[0]; v1[1]= v2[1]; v1[2]= v2[2]; v1[3]= v2[3];
//        System.arraycopy(v2, 0, v1, 0, 4);
//    }

    public static final void QUATCOPY(double[] v1, float[] v2) {
        v1[0] = v2[0];
        v1[1] = v2[1];
        v1[2] = v2[2];
        v1[3] = v2[3];
    }

//#define LONGCOPY(a, b, c)	{int lcpc=c, *lcpa=(int *)a, *lcpb=(int *)b; while(lcpc-->0) *(lcpa++)= *(lcpb++);}

    public static final void VECADD(float[] v1, float[] v2, float[] v3) {
        v1[0] = v2[0] + v3[0];
        v1[1] = v2[1] + v3[1];
        v1[2] = v2[2] + v3[2];
    }

    public static final void VECSUB(float[] v1, float[] v2, float[] v3) {
        v1[0] = v2[0] - v3[0];
        v1[1] = v2[1] - v3[1];
        v1[2] = v2[2] - v3[2];
    }

//#define VECSUB2D(v1,v2,v3) 	{*(v1)= *(v2) - *(v3); *(v1+1)= *(v2+1) - *(v3+1);}
//#define VECADDFAC(v1,v2,v3,fac) {*(v1)= *(v2) + *(v3)*(fac); *(v1+1)= *(v2+1) + *(v3+1)*(fac); *(v1+2)= *(v2+2) + *(v3+2)*(fac);}
//#define QUATADDFAC(v1,v2,v3,fac) {*(v1)= *(v2) + *(v3)*(fac); *(v1+1)= *(v2+1) + *(v3+1)*(fac); *(v1+2)= *(v2+2) + *(v3+2)*(fac); *(v1+3)= *(v2+3) + *(v3+3)*(fac);}

    public static final float INPR(float[] v1, float[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    /* some misc stuff.... */

//#define CLAMP(a, b, c)		if((a)<(b)) (a)=(b); else if((a)>(c)) (a)=(c)

    public static final int CLAMPIS(int a, int b, int c) {
        return a < b ? b : a > c ? c : a;
    }
    
    public static final float CLAMPIS(float a, float b, float c) {
        return a < b ? b : a > c ? c : a;
    }

//#define CLAMPTEST(a, b, c)	if((b)<(c)) {CLAMP(a, b, c);} else {CLAMP(a, c, b);}
//
//#define IS_EQ(a,b) ((fabs((double)(a)-(b)) >= (double) FLT_EPSILON) ? 0 : 1)
//
//#define IS_EQT(a, b, c) ((a > b)? (((a-b) <= c)? 1:0) : ((((b-a) <= c)? 1:0)))
//#define IN_RANGE(a, b, c) ((b < c)? ((b<a && a<c)? 1:0) : ((c<a && a<b)? 1:0))

    /* INTEGER CODES */

//#if defined(__sgi) || defined (__sparc) || defined (__sparc__) || defined (__PPC__) || defined (__ppc__) || defined (__hppa__) || defined (__BIG_ENDIAN__)
//	/* Big Endian */
//public static int MAKE_ID(int a, int b, int c, int d) {
//        return (a<<24) | (b<<16) | (c<<8) | (d);
//}
//#else
//	/* Little Endian */
//#define MAKE_ID(a,b,c,d) ( (int)(d)<<24 | (int)(c)<<16 | (b)<<8 | (a) )
//#endif
//
//#define ID_NEW(a)		if( (a) && (a)->id.newid ) (a)= (void *)(a)->id.newid
//
//#define FORM MAKE_ID('F','O','R','M')
//#define DDG1 MAKE_ID('3','D','G','1')
//#define DDG2 MAKE_ID('3','D','G','2')
//#define DDG3 MAKE_ID('3','D','G','3')
//#define DDG4 MAKE_ID('3','D','G','4')
//
//#define GOUR MAKE_ID('G','O','U','R')
//
//#define BLEN MAKE_ID('B','L','E','N')
//#define DER_ MAKE_ID('D','E','R','_')
//#define V100 MAKE_ID('V','1','0','0')

    public static final int DATA = ('D' << 24) | ('A' << 16) | ('T' << 8) | ('A');
    public static final int GLOB = ('G' << 24) | ('L' << 16) | ('O' << 8) | ('B');
    public static final int IMAG = ('I' << 24) | ('M' << 16) | ('A' << 8) | ('G');
    public static final int DNA1 = ('D' << 24) | ('N' << 16) | ('A' << 8) | ('1');
    public static final int TEST = ('T' << 24) | ('E' << 16) | ('S' << 8) | ('T');
    public static final int REND = ('R' << 24) | ('E' << 16) | ('N' << 8) | ('D');
    public static final int USER = ('U' << 24) | ('S' << 16) | ('E' << 8) | ('R');
    public static final int ENDB = ('E' << 24) | ('N' << 16) | ('D' << 8) | ('B');

    /* This one rotates the bytes in an int */
    public static final int SWITCH_INT(int a) {
        int b0 = (a) & 0xFF;
        int b1 = (a >> 8) & 0xFF;
        int b2 = (a >> 16) & 0xFF;
        int b3 = (a >> 24) & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | (b3);
    }

//#define SWITCH_SHORT(a)	{ \
//	char s_i, *p_i; \
//	p_i= (char *)&(a); \
//	s_i=p_i[0]; p_i[0]=p_i[1]; p_i[1]=s_i; }


    /* Bit operations */

    public static final boolean BTST(int a, int b) {
        return (a & 1 << b) != 0;
    }

//#define BNTST(a,b)	( ( (a) & 1<<(b) )==0 )
//#define BTST2(a,b,c)	( BTST( (a), (b) ) || BTST( (a), (c) ) )

    public static final int BSET(int a, int b) {
        return (a | 1 << b);
    }

    public static final int BCLR(int a, int b) {
        return (a & ~(1 << b));
    }

    /* bit-row */

//#define BROW(min, max)	(((max)>=31? 0xFFFFFFFF: (1<<(max+1))-1) - ((min)? ((1<<(min))-1):0) )

///* Warning-free macros for storing ints in pointers. Use these _only_
// * for storing an int in a pointer, not a pointer in an int (64bit)! */
//#define SET_INT_IN_POINTER(i) ((void*)(intptr_t)(i))
//#define GET_INT_FROM_POINTER(i) ((int)(intptr_t)(i))

}

