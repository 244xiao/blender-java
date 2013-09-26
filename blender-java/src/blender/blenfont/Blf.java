/**
 * $Id:
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
 * The Original Code is Copyright (C) 2009 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.blenfont;

import java.nio.ByteBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenfont.BlfTypes.FontBLF;
import blender.blenlib.StringUtil;

public class Blf {

///* font.flags. */
//public static final int BLF_ROTATION= (1<<0);
//public static final int BLF_CLIPPING= (1<<1);
//public static final int BLF_SHADOW= (1<<2);
//public static final int BLF_KERNING_DEFAULT= (1<<3);
//
///* font.mode. */
//public static final int BLF_MODE_TEXTURE= 0;
//public static final int BLF_MODE_BITMAP= 1;

//public static class GlyphCacheBLF extends Link<GlyphCacheBLF> {
//	/* font size. */
//	public int size;
//
//	/* and dpi. */
//	public int dpi;
//
//	/* and the glyphs. */
//	public ListBase[] bucket=new ListBase[257];
//
//	/* texture array, to draw the glyphs. */
//	public int[] textures;
//
//	/* size of the array. */
//	public int ntex;
//
//	/* and the last texture, aka. the current texture. */
//	public int cur_tex;
//
//	/* like bftgl, we draw every glyph in a big texture, so this is the
//	 * current position inside the texture.
//	 */
//	public int x_offs;
//	public int y_offs;
//
//	/* and the space from one to other. */
//	public int pad;
//
//	/* and the bigger glyph in the font. */
//	public int max_glyph_width;
//	public int max_glyph_height;
//
//	/* next two integer power of two, to build the texture. */
//	public int p2_width;
//	public int p2_height;
//
//	/* number of glyphs in the font. */
//	public int num_glyphs;
//
//	/* number of glyphs that we load here. */
//	public int rem_glyphs;
//
//	/* ascender and descender value. */
//	public float ascender;
//	public float descender;
//};

//typedef struct GlyphTextureBLF {
//	/* texture id where this glyph is store. */
//	GLuint tex;
//
//	/* position inside the texture where this glyph is store. */
//	int xoff;
//	int yoff;
//
//	/* glyph width and height. */
//	int width;
//	int height;
//
//	/* uv coords. */
//	float uv[2][2];
//
//	/* X and Y bearing of the glyph.
//	 * The X bearing is from the origin to the glyph left bbox edge.
//	 * The Y bearing is from the baseline to the top of the glyph edge.
//	 */
//	float pos_x;
//	float pos_y;
//} GlyphTextureBLF;

//public static class GlyphBitmapBLF {
//	/* image data. */
////	public unsigned char *image;
//        public byte[] image;
//
//	public int width;
//	public int height;
//	public int pitch;
//
//	public float pos_x;
//	public float pos_y;
//};

//public static class GlyphBLF extends Link<GlyphBLF>{
//
//	/* and the character, as UTF8 */
//	public int c;
//
//	/* glyph box. */
//	public rctf box = new rctf();
//
//	/* advance size. */
//	public float advance;
//
////	/* texture information. */
////	public GlyphTextureBLF *tex_data;
//
//	/* bitmap information. */
//	public GlyphBitmapBLF bitmap_data;
//};

//public static class FontBLF {
//	/* font name. */
//	public byte[] name;
//
//	/* filename or NULL. */
//	public byte[] filename;
//
//	/* draw mode, texture or bitmap. */
//	public int mode;
//
//	/* aspect ratio or scale. */
//	public float aspect;
//
//	/* initial position for draw the text. */
//	public float[] pos=new float[3];
//
//	/* angle in degrees. */
//	public float angle;
//
//	/* blur: 3 or 5 large kernel */
//	public int blur;
//
//	/* shadow level. */
//	public int shadow;
//
//	/* and shadow offset. */
//	public int shadow_x;
//	public int shadow_y;
//
//	/* shadow color. */
//	public float[] shadow_col=new float[4];
//
//	/* this is the matrix that we load before rotate/scale/translate. */
//	public float[][] mat=new float[4][4];
//
//	/* clipping rectangle. */
//	public rctf clip_rec = new rctf();
//
//	/* font dpi (default 72). */
//	public int dpi;
//
//	/* font size. */
//	public int size;
//
//	/* max texture size. */
//	public int max_tex_size;
//
//	/* font options. */
//	public int flags;
//
//	/* list of glyph cache for this font. */
//	public ListBase cache = new ListBase();
//
//	/* current glyph cache, size and dpi. */
//	public GlyphCacheBLF glyph_cache;
//
//	/* freetype2 face. */
////	public FT_Face face;
//};

//typedef struct DirBLF {
//	struct DirBLF *next;
//	struct DirBLF *prev;
//
//	/* full path where search fonts. */
//	char *path;
//} DirBLF;

///////////////////////////////////////////////////////////////////////////

/* Max number of font in memory.
 * Take care that now every font have a glyph cache per size/dpi,
 * so we don't need load the same font with different size, just
 * load one and call BLF_size.
 */
public static final int BLF_MAX_FONT= 16;

/* Font array. */
static FontBLF[] global_font = new FontBLF[BLF_MAX_FONT];

/* Number of font. */
static int global_font_num= 0;

///* Current font. */
//static int global_font_cur= 0;

/* Default size and dpi, for BLF_draw_default. */
static int global_font_default= -1;
static int global_font_points= 11;
static int global_font_dpi= 72;

//XXX, should these be made into global_font_'s too?
static int blf_mono_font= -1;
static int blf_mono_font_render= -1;

static FontBLF BLF_get(int fontid)
{
	if (fontid >= 0 && fontid < BLF_MAX_FONT)
		return(global_font[fontid]);
	return(null);
}

//public static int BLF_init(int points, int dpi)
//{
//	int i;
//
//	for (i= 0; i < BLF_MAX_FONT; i++)
//		global_font[i]= null;
//
//	global_font_points= points;
//	global_font_dpi= dpi;
//	return blf_font_init();
//}

//void BLF_exit(void)
//{
//	FontBLF *font;
//	int i;
//
//	for (i= 0; i < global_font_num; i++) {
//		font= global_font[i];
//		if (font)
//			blf_font_free(font);
//	}
//
//	blf_font_exit();
//}

public static int blf_search(byte[] name, int offset)
{
	FontBLF font;
	int i;

	for (i= 0; i < BLF_MAX_FONT; i++) {
		font= global_font[i];
		if (font!=null && (StringUtil.strcmp(font.name,0, name,offset)==0))
			return(i);
	}
	return(-1);
}

//int BLF_load(char *name)
//{
//	FontBLF *font;
//	char *filename;
//	int i;
//
//	if (!name)
//		return(-1);
//
//	/* check if we already load this font. */
//	i= blf_search(name);
//	if (i >= 0) {
//		font= global_font[i];
//		return(i);
//	}
//
//	if (global_font_num+1 >= BLF_MAX_FONT) {
//		printf("Too many fonts!!!\n");
//		return(-1);
//	}
//
//	filename= blf_dir_search(name);
//	if (!filename) {
//		printf("Can't find font: %s\n", name);
//		return(-1);
//	}
//
//	font= blf_font_new(name, filename);
//	MEM_freeN(filename);
//
//	if (!font) {
//		printf("Can't load font: %s\n", name);
//		return(-1);
//	}
//
//	global_font[global_font_num]= font;
//	i= global_font_num;
//	global_font_num++;
//	return(i);
//}
//
//void BLF_metrics_attach(unsigned char *mem, int mem_size)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		blf_font_attach_from_mem(font, mem, mem_size);
//}

public static int BLF_load_mem(String name, ByteBuffer mem, int mem_size)
{
	FontBLF font;
	int i;
//
//	if (!name)
//		return(-1);
//
//	i= blf_search(name);
//	if (i >= 0) {
//		font= global_font[i];
//		return(i);
//	}
//
//	if (global_font_num+1 >= BLF_MAX_FONT) {
//		printf("Too many fonts!!!\n");
//		return(-1);
//	}
//
//	if (!mem || !mem_size) {
//		printf("Can't load font: %s from memory!!\n", name);
//		return(-1);
//	}
//
//	font= blf_font_new_from_mem(name, mem, mem_size);

    // TMP
    font= new FontBLF();

//	if (!font) {
//		printf("Can't load font: %s from memory!!\n", name);
//		return(-1);
//	}

	global_font[global_font_num]= font;
	i= global_font_num;
	global_font_num++;
	return i;
}

//public static void BLF_set(int fontid)
//{
//	if (fontid >= 0 && fontid < BLF_MAX_FONT)
//		global_font_cur= fontid;
//}

public static void BLF_enable(int fontid, int option)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null)
		font.flags |= option;
}

public static void BLF_disable(int fontid, int option)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null)
		font.flags &= ~option;
}

public static void BLF_enable_default(int option)
{
	FontBLF font;

	font= BLF_get(global_font_default);
	if (font!=null)
		font.flags |= option;
}

public static void BLF_disable_default(int option)
{
	FontBLF font;

	font= BLF_get(global_font_default);
	if (font!=null)
		font.flags &= ~option;
}

public static void BLF_aspect(int fontid, float x, float y, float z)
{
	FontBLF font;

	font= BLF_get(fontid);
	if (font!=null) {
		font.aspect[0]= x;
		font.aspect[1]= y;
		font.aspect[2]= z;
	}
}

public static void BLF_matrix(int fontid, double[] m)
{
	FontBLF font;
	int i;

	font= BLF_get(fontid);
	if (font!=null) {
		for (i= 0; i < 16; i++)
			font.m[i]= m[i];
	}
}

public static void BLF_position(int fontid, float x, float y, float z)
{
	FontBLF font;
	float remainder;
	float xa, ya, za;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null) {
		if ((font.flags & BlfTypes.BLF_ASPECT)!=0) {
			xa= font.aspect[0];
			ya= font.aspect[1];
			za= font.aspect[2];
		}
		else {
			xa= 1.0f;
			ya= 1.0f;
			za= 1.0f;
		}
		
		remainder= x - (float)StrictMath.floor(x);
		if (remainder > 0.4f && remainder < 0.6f) {
			if (remainder < 0.5f)
				x -= 0.1f * xa;
			else
				x += 0.1f * xa;
		}

		remainder= y - (float)StrictMath.floor(y);
		if (remainder > 0.4f && remainder < 0.6f) {
			if (remainder < 0.5f)
				y -= 0.1f * ya;
			else
				y += 0.1f * ya;
		}
		
		remainder= z - (float)StrictMath.floor(z);
		if (remainder > 0.4f && remainder < 0.6f) {
			if (remainder < 0.5f)
				z -= 0.1f * za;
			else
				z += 0.1f * za;
		}

		font.pos[0]= x;
		font.pos[1]= y;
		font.pos[2]= z;
	}
}

public static void BLF_size(int fontid, int size, int dpi)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null)
		BlfFont.blf_font_size(font, size, dpi);
}

//void BLF_blur(int size)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		font.blur= size;
//}

public static void BLF_draw_default(float x, float y, float z, byte[] str, int offset, int len)
{
	if (str==null)
		return;

	if (global_font_default == -1)
		global_font_default= blf_search(StringUtil.toCString("default"),0);

	if (global_font_default == -1) {
		System.out.printf("Warning: Can't found default font!!\n");
		return;
	}

	BLF_size(global_font_default, global_font_points, global_font_dpi);
	BLF_position(global_font_default, x, y, z);
	BLF_draw(global_font_default, str,offset, len);
}

//void BLF_default_rotation(float angle)
//{
//
//	if (global_font_default>=0) {
//		global_font[global_font_default].angle= angle;
//		if(angle)
//			global_font[global_font_default].flags |= BLF_ROTATION;
//		else
//			global_font[global_font_default].flags &= ~BLF_ROTATION;
//	}
//}

public static void blf_draw__start(FontBLF font)
{
	/*
	 * The pixmap alignment hack is handle
	 * in BLF_position (old ui_rasterpos_safe).
	 */

	GL2 gl = (GL2)GLU.getCurrentGL();
	
//	gl.glEnable(GL2.GL_BLEND);
//	gl.glEnable(GL2.GL_TEXTURE_2D);
//	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
//
//	gl.glPushMatrix();
//
//	if ((font.flags & BLF_MATRIX)!=0)
//		gl.glMultMatrixd((GLdouble)font.m);
//
//	gl.glTranslatef(font.pos[0], font.pos[1], font.pos[2]);
//
//	if (font.flags & BLF_ASPECT)
//		gl.glScalef(font.aspect[0], font.aspect[1], font.aspect[2]);
//
//	if (font.flags & BLF_ROTATION)
//		gl.glRotatef(font.angle, 0.0f, 0.0f, 1.0f);
	
	// TMP
	gl.glPushClientAttrib(GL2.GL_CLIENT_PIXEL_STORE_BIT);
	gl.glPushAttrib(GL2.GL_ENABLE_BIT);
	gl.glPixelStorei(GL2.GL_UNPACK_LSB_FIRST, GL2.GL_FALSE);
	gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
	gl.glDisable(GL2.GL_BLEND);
	gl.glRasterPos3f(font.pos[0], font.pos[1], font.pos[2]);
}

public static void blf_draw__end()
{
	GL2 gl = (GL2)GLU.getCurrentGL();
	
//	gl.glPopMatrix();
//	gl.glDisable(GL2.GL_BLEND);
//	gl.glDisable(GL2.GL_TEXTURE_2D);
	
	// TMP
	gl.glPopAttrib();
	gl.glPopClientAttrib();
}

public static void BLF_draw(int fontid, byte[] str, int offset, int len)
{
	FontBLF font= BLF_get(fontid);
	if (font!=null) {
		blf_draw__start(font);
		BlfFont.blf_font_draw(font, str,offset, len);
		blf_draw__end();
	}
	
//    GL2 gl = (GL2)GLU.getCurrentGL();
//	FontBLF font;
//
//	/*
//	 * The pixmap alignment hack is handle
//	 * in BLF_position (old ui_rasterpos_safe).
//	 */
//
//	font= global_font[global_font_cur];
//	if (font!=null) {
////		if (font.mode == BLF_MODE_BITMAP) {
//			gl.glPushClientAttrib(GL2.GL_CLIENT_PIXEL_STORE_BIT);
//			gl.glPushAttrib(GL2.GL_ENABLE_BIT);
//			gl.glPixelStorei(GL2.GL_UNPACK_LSB_FIRST, GL2.GL_FALSE);
//			gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
//			gl.glDisable(GL2.GL_BLEND);
//			gl.glRasterPos3f(font.pos[0], font.pos[1], font.pos[2]);
//
////                        System.out.printf("BLF_draw: %f, %f, %f\n", font.pos[0], font.pos[1], font.pos[2]);
//			BlfFont.blf_font_draw(font, str, offset);
//
//			gl.glPopAttrib();
//			gl.glPopClientAttrib();
////		}
////		else {
////			glEnable(GL_BLEND);
////			glEnable(GL_TEXTURE_2D);
////			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
////
////			glPushMatrix();
////			glTranslatef(font.pos[0], font.pos[1], font.pos[2]);
////			glScalef(font.aspect, font.aspect, 1.0);
////
////			if (font.flags & BLF_ROTATION)
////				glRotatef(font.angle, 0.0f, 0.0f, 1.0f);
////
////			BLFFont.blf_font_draw(font, str, offset);
////
////			glPopMatrix();
////			glDisable(GL_BLEND);
////			glDisable(GL_TEXTURE_2D);
////		}
//	}
}

//void BLF_boundbox(char *str, rctf *box)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		blf_font_boundbox(font, str, box);
//}

public static float BLF_width(int fontid, byte[] str, int offset)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null)
		return BlfFont.blf_font_width(font, str,offset);
	return 0.0f;
}

//float BLF_fixed_width(void)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		return(blf_font_fixed_width(font));
//	return(0.0f);
//}
//
//float BLF_width_default(char *str)
//{
//	FontBLF *font;
//	float width;
//	int old_font, old_point, old_dpi;
//
//	if (global_font_default == -1)
//		global_font_default= blf_search("default");
//
//	if (global_font_default == -1) {
//		printf("Error: Can't found default font!!\n");
//		return(0.0f);
//	}
//
//	font= global_font[global_font_cur];
//	if (font) {
//		old_font= global_font_cur;
//		old_point= font.size;
//		old_dpi= font.dpi;
//	}
//
//	global_font_cur= global_font_default;
//	BLF_size(global_font_points, global_font_dpi);
//	width= BLF_width(str);
//
//	/* restore the old font. */
//	if (font) {
//		global_font_cur= old_font;
//		BLF_size(old_point, old_dpi);
//	}
//	return(width);
//}

public static float BLF_height(int fontid, byte[] str, int offset)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null)
		return BlfFont.blf_font_height(font, str,offset);
	return 0.0f;
}

//float BLF_height_default(char *str)
//{
//	FontBLF *font;
//	float height;
//	int old_font, old_point, old_dpi;
//
//	if (global_font_default == -1)
//		global_font_default= blf_search("default");
//
//	if (global_font_default == -1) {
//		printf("Error: Can't found default font!!\n");
//		return(0.0f);
//	}
//
//	font= global_font[global_font_cur];
//	if (font) {
//		old_font= global_font_cur;
//		old_point= font.size;
//		old_dpi= font.dpi;
//	}
//
//	global_font_cur= global_font_default;
//	BLF_size(global_font_points, global_font_dpi);
//	height= BLF_height(str);
//
//	/* restore the old font. */
//	if (font) {
//		global_font_cur= old_font;
//		BLF_size(old_point, old_dpi);
//	}
//	return(height);
//}
//
//void BLF_rotation(float angle)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		font.angle= angle;
//}

public static void BLF_clipping(int fontid, float xmin, float ymin, float xmax, float ymax)
{
	FontBLF font;

//	font= global_font[global_font_cur];
	font= BLF_get(fontid);
	if (font!=null) {
		font.clip_rec.xmin= xmin;
		font.clip_rec.ymin= ymin;
		font.clip_rec.xmax= xmax;
		font.clip_rec.ymax= ymax;
	}
}

//void BLF_mode(int mode)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font)
//		font.mode= mode;
//}
//
//void BLF_shadow(int level, float r, float g, float b, float a)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font) {
//		font.shadow= level;
//		font.shadow_col[0]= r;
//		font.shadow_col[1]= g;
//		font.shadow_col[2]= b;
//		font.shadow_col[3]= a;
//	}
//}
//
//void BLF_shadow_offset(int x, int y)
//{
//	FontBLF *font;
//
//	font= global_font[global_font_cur];
//	if (font) {
//		font.shadow_x= x;
//		font.shadow_y= y;
//	}
//}
}