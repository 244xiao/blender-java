package blender.blenfont;

import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.rctf;

public class BlfTypes {
	/**
	 * $Id: blf_internal_types.h 33579 2010-12-09 22:27:55Z bdiego $
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
	 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
	 *
	 * The Original Code is Copyright (C) 2008 Blender Foundation.
	 * All rights reserved.
	 * 
	 * Contributor(s): Blender Foundation.
	 *
	 * ***** END GPL LICENSE BLOCK *****
	 */

//	#ifndef BLF_INTERNAL_TYPES_H
//	#define BLF_INTERNAL_TYPES_H
	
	/* font.flags. */
	public static final int BLF_ROTATION= (1<<0);
	public static final int BLF_CLIPPING= (1<<1);
	public static final int BLF_SHADOW= (1<<2);
	public static final int BLF_KERNING_DEFAULT= (1<<3);
	public static final int BLF_MATRIX= (1<<4);
	public static final int BLF_ASPECT= (1<<5);

	/* font.mode. */
	public static final int BLF_MODE_TEXTURE= 0;
	public static final int BLF_MODE_BITMAP= 1;

	static class GlyphCacheBLF extends Link<GlyphCacheBLF> {
//		struct GlyphCacheBLF *next;
//		struct GlyphCacheBLF *prev;

		/* font size. */
		public int size;

		/* and dpi. */
		public int dpi;

		/* and the glyphs. */
		public ListBase[] bucket=new ListBase[257];

		/* texture array, to draw the glyphs. */
		public int[] textures;

		/* size of the array. */
		public int ntex;

		/* and the last texture, aka. the current texture. */
		public int cur_tex;

		/* like bftgl, we draw every glyph in a big texture, so this is the
		 * current position inside the texture.
		 */
		public int x_offs;
		public int y_offs;

		/* and the space from one to other. */
		public int pad;

		/* and the bigger glyph in the font. */
		public int max_glyph_width;
		public int max_glyph_height;

		/* next two integer power of two, to build the texture. */
		public int p2_width;
		public int p2_height;

		/* number of glyphs in the font. */
		public int num_glyphs;

		/* number of glyphs that we load here. */
		public int rem_glyphs;

		/* ascender and descender value. */
		public float ascender;
		public float descender;
	};

	static class GlyphBLF extends Link<GlyphBLF> {
//		struct GlyphBLF *next;
//		struct GlyphBLF *prev;

		/* and the character, as UTF8 */
		public int c;

		/* freetype2 index, to speed-up the search. */
//		public FT_UInt idx;

		/* glyph box. */
		public rctf box=new rctf();

		/* advance size. */
		public float advance;

		/* texture id where this glyph is store. */
		public int tex;

		/* position inside the texture where this glyph is store. */
		public int xoff;
		public int yoff;

		/* Bitmap data, from freetype. Take care that this
		 * can be NULL.
		 */
//		public unsigned char *bitmap;
		public Object bitmap;

		/* glyph width and height. */
		public int width;
		public int height;
		public int pitch;

		/* uv coords. */
		public float[][] uv=new float[2][2];

		/* X and Y bearing of the glyph.
		 * The X bearing is from the origin to the glyph left bbox edge.
		 * The Y bearing is from the baseline to the top of the glyph edge.
		 */
		public float pos_x;
		public float pos_y;

		/* with value of zero mean that we need build the texture. */
		public short build_tex;
	};

	static class FontBLF {
		/* font name. */
		public byte[] name;

		/* filename or NULL. */
		public byte[] filename;

		/* aspect ratio or scale. */
		public float[] aspect=new float[3];

		/* initial position for draw the text. */
		public float[] pos=new float[3];

		/* angle in degrees. */
		public float angle;
		
		/* blur: 3 or 5 large kernel */
		public int blur;

		/* shadow level. */
		public int shadow;

		/* and shadow offset. */
		public int shadow_x;
		public int shadow_y;

		/* shadow color. */
		public float[] shadow_col=new float[4];
		
		/* Multiplied this matrix with the current one before
		 * draw the text! see blf_draw__start.
		 */
		public double[] m=new double[16];

		/* clipping rectangle. */
		public rctf clip_rec=new rctf();

		/* font dpi (default 72). */
		public int dpi;

		/* font size. */
		public int size;

		/* max texture size. */
		public int max_tex_size;

		/* font options. */
		public int flags;

		/* list of glyph cache for this font. */
		public ListBase cache = new ListBase();

		/* current glyph cache, size and dpi. */
		public GlyphCacheBLF glyph_cache;
		
		/* fast ascii lookip */
//		public GlyphBLF *glyph_ascii_table[256];

		/* freetype2 face. */
//		public FT_Face face;

		/* for draw to buffer, always set this to NULL after finish! */
		public float[] b_fbuf;

		/* the same but unsigned char */
//		public unsigned char *b_cbuf;
		public byte[] b_cbuf;

		/* buffer size. */
		public int bw;
		public int bh;

		/* number of channels. */
		public int bch;

		/* and the color, the alphas is get from the glyph! */
		public float[] b_col=new float[4];
	};

//	typedef struct DirBLF {
//		struct DirBLF *next;
//		struct DirBLF *prev;
//
//		/* full path where search fonts. */
//		char *path;
//	} DirBLF;

//	#endif /* BLF_INTERNAL_TYPES_H */

}
