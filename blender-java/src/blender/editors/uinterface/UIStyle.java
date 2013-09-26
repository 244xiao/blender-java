/**
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
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import static blender.blenkernel.Blender.U;
import blender.blenfont.Blf;
import blender.blenfont.BlfTypes;
import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.makesdna.UserDefTypes;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.uiFont;
import blender.makesdna.sdna.uiFontStyle;
import blender.makesdna.sdna.uiStyle;

public class UIStyle {

/* style + theme + layout-engine = UI */

/*
 This is a complete set of layout rules, the 'state' of the Layout
 Engine. Multiple styles are possible, defined via C or Python. Styles
 get a name, and will typically get activated per region type, like
 "Header", or "Listview" or "Toolbar". Properties of Style definitions
 are:

 - default collumn properties, internal spacing, aligning, min/max width
 - button alignment rules (for groups)
 - label placement rules
 - internal labeling or external labeling default
 - default minimum widths for buttons/labels (in amount of characters)
 - font types, styles and relative sizes for Panel titles, labels, etc.

*/


/* ********************************************** */

static uiStyle ui_style_new(ListBase<uiStyle> styles, String name)
{
	uiStyle style= new uiStyle();

	ListBaseUtil.BLI_addtail(styles, style);
	StringUtil.BLI_strncpy(style.name,0, StringUtil.toCString(name),0, UserDefTypes.MAX_STYLE_NAME);

	style.panelzoom= 1.0f;

	style.paneltitle.uifont_id= UserDefTypes.UIFONT_DEFAULT;
	style.paneltitle.points= 12;
	style.paneltitle.kerning= 1;
	style.paneltitle.shadow= 1;
	style.paneltitle.shadx= 0;
	style.paneltitle.shady= -1;
	style.paneltitle.shadowalpha= 0.15f;
	style.paneltitle.shadowcolor= 1.0f;

	style.grouplabel.uifont_id= UserDefTypes.UIFONT_DEFAULT;
	style.grouplabel.points= 12;
	style.grouplabel.kerning= 1;
	style.grouplabel.shadow= 3;
	style.grouplabel.shadx= 0;
	style.grouplabel.shady= -1;
	style.grouplabel.shadowalpha= 0.25f;

	style.widgetlabel.uifont_id= UserDefTypes.UIFONT_DEFAULT;
	style.widgetlabel.points= 11;
	style.widgetlabel.kerning= 1;
	style.widgetlabel.shadow= 3;
	style.widgetlabel.shadx= 0;
	style.widgetlabel.shady= -1;
	style.widgetlabel.shadowalpha= 0.15f;
	style.widgetlabel.shadowcolor= 1.0f;

	style.widget.uifont_id= UserDefTypes.UIFONT_DEFAULT;
	style.widget.points= 11;
	style.widget.kerning= 1;
	style.widget.shadowalpha= 0.25f;

	style.columnspace= 8;
	style.templatespace= 5;
	style.boxspace= 5;
	style.buttonspacex= 8;
	style.buttonspacey= 2;
	style.panelspace= 8;
	style.panelouter= 4;

	return style;
}

static uiFont uifont_to_blfont(int id)
{
	uiFont font= (uiFont)U.uifonts.first;
	
	for(; font!=null; font= font.next) {
		if(font.uifont_id==id) {
			return font;
		}
	}
	return (uiFont)U.uifonts.first;
}

/* *************** draw ************************ */

public static void uiStyleFontDrawExt(uiFontStyle fs, rcti rect, byte[] str, int offset, float[] r_xofs, float[] r_yofs)
{
	float height;
	int xofs=0, yofs;
	
	uiStyleFontSet(fs);
	
	height= Blf.BLF_height(fs.uifont_id, StringUtil.toCString("2"),0); /* correct offset is on baseline, the j is below that */
	yofs= (int)Math.floor( 0.5f*(rect.ymax - rect.ymin - height));

	if(fs.align==UserDefTypes.UI_STYLE_TEXT_CENTER)
		xofs= (int)(Math.floor( 0.5f*(rect.xmax - rect.xmin - Blf.BLF_width(fs.uifont_id, str,offset))));
	else if(fs.align==UserDefTypes.UI_STYLE_TEXT_RIGHT)
		xofs= (int)(rect.xmax - rect.xmin - Blf.BLF_width(fs.uifont_id, str,offset) - 1);
	
	/* clip is very strict, so we give it some space */
	Blf.BLF_clipping(fs.uifont_id, rect.xmin-1, rect.ymin-4, rect.xmax+1, rect.ymax+4);
	Blf.BLF_enable(fs.uifont_id, BlfTypes.BLF_CLIPPING);
	Blf.BLF_position(fs.uifont_id, rect.xmin+xofs, rect.ymin+yofs, 0.0f);

//	if (fs->shadow) {
//		BLF_enable(fs->uifont_id, BLF_SHADOW);
//		BLF_shadow(fs->uifont_id, fs->shadow, fs->shadowcolor, fs->shadowcolor, fs->shadowcolor, fs->shadowalpha);
//		BLF_shadow_offset(fs->uifont_id, fs->shadx, fs->shady);
//	}

//	if (fs->kerning == 1)
//		BLF_enable(fs->uifont_id, BLF_KERNING_DEFAULT);

	Blf.BLF_draw(fs.uifont_id, str,offset, 65535); /* XXX, use real length */
	Blf.BLF_disable(fs.uifont_id, BlfTypes.BLF_CLIPPING);
//	if (fs->shadow)
//		BLF_disable(fs->uifont_id, BLF_SHADOW);
//	if (fs->kerning == 1)
//		BLF_disable(fs->uifont_id, BLF_KERNING_DEFAULT);

	r_xofs[0]= xofs;
	r_yofs[0]= yofs;
}

public static void uiStyleFontDraw(uiFontStyle fs, rcti rect, byte[] str, int offset)
{
	float[] xofs={0}, yofs={0};
	uiStyleFontDrawExt(fs, rect, str, offset, xofs, yofs);
}

/* drawn same as above, but at 90 degree angle */
public static void uiStyleFontDrawRotated(uiFontStyle fs, rcti rect, byte[] str, int offset)
{
	float height;
	int xofs, yofs;
	float angle;
	rcti txtrect = new rcti();

	uiStyleFontSet(fs);

	height= Blf.BLF_height(fs.uifont_id, StringUtil.toCString("2"),0);	/* correct offset is on baseline, the j is below that */
	/* becomes x-offset when rotated */
	xofs= (int)(Math.floor( 0.5f*(rect.ymax - rect.ymin - height)) + 1);

	/* ignore UI_STYLE, always aligned to top */

	/* rotate counter-clockwise for now (assumes left-to-right language)*/
	xofs+= height;
	yofs= (int)(Blf.BLF_width(fs.uifont_id, str,offset) + 5);
	angle= 90.0f;

	/* translate rect to vertical */
	txtrect.xmin= rect.xmin - (rect.ymax - rect.ymin);
	txtrect.ymin= rect.ymin - (rect.xmax - rect.xmin);
	txtrect.xmax= rect.xmin;
	txtrect.ymax= rect.ymin;

//	/* clip is very strict, so we give it some space */
//	/* clipping is done without rotation, so make rect big enough to contain both positions */
	Blf.BLF_clipping(fs.uifont_id, txtrect.xmin-1, txtrect.ymin-yofs-xofs-4, rect.xmax+1, rect.ymax+4);
	Blf.BLF_enable(fs.uifont_id, BlfTypes.BLF_CLIPPING);
	Blf.BLF_position(fs.uifont_id, txtrect.xmin+xofs, txtrect.ymin-yofs, 0.0f);

	Blf.BLF_enable(fs.uifont_id, BlfTypes.BLF_ROTATION);
//	BLF_rotation(fs->uifont_id, angle);

//	if (fs->shadow) {
//		BLF_enable(fs->uifont_id, BLF_SHADOW);
//		BLF_shadow(fs->uifont_id, fs->shadow, fs->shadowcolor, fs->shadowcolor, fs->shadowcolor, fs->shadowalpha);
//		BLF_shadow_offset(fs->uifont_id, fs->shadx, fs->shady);
//	}
//
//	if (fs->kerning == 1)
//		BLF_enable(fs->uifont_id, BLF_KERNING_DEFAULT);

	Blf.BLF_draw(fs.uifont_id, str,offset, 65535); /* XXX, use real length */
	Blf.BLF_disable(fs.uifont_id, BlfTypes.BLF_ROTATION);
	Blf.BLF_disable(fs.uifont_id, BlfTypes.BLF_CLIPPING);
//	if (fs->shadow)
//		BLF_disable(fs->uifont_id, BLF_SHADOW);
//	if (fs->kerning == 1)
//		BLF_disable(fs->uifont_id, BLF_KERNING_DEFAULT);
}

/* ************** helpers ************************ */

/* temporarily, does widget font */
public static int UI_GetStringWidth(byte[] str, int offset)
{
////	uiStyle style= (uiStyle)U.uistyles.first;
////
////	uiStyleFontSet(style.widget);
//	return (int)Blf.BLF_width(str,offset);
	
	uiStyle style= (uiStyle)U.uistyles.first;
	uiFontStyle fstyle= style.widget;
	int width;
	
	if (fstyle.kerning==1)	/* for BLF_width */
		Blf.BLF_enable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
	
	uiStyleFontSet(fstyle);
	width= (int)Blf.BLF_width(fstyle.uifont_id, str,offset);	
	
	if (fstyle.kerning==1)
		Blf.BLF_disable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
	
	return width;
}

/* temporarily, does widget font */
public static void UI_DrawString(float x, float y, byte[] str, int offset)
{
//	uiStyle style= (uiStyle)U.uistyles.first;
//
//	uiStyleFontSet(style.widget);
//	Blf.BLF_position(x, y, 0.0f);
//	Blf.BLF_draw(str,offset);
	
	uiStyle style= (uiStyle)U.uistyles.first;
	
	if (style.widget.kerning == 1)
		Blf.BLF_enable(style.widget.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);

	uiStyleFontSet(style.widget);
	Blf.BLF_position(style.widget.uifont_id, x, y, 0.0f);
	Blf.BLF_draw(style.widget.uifont_id, str,offset, 65535); /* XXX, use real length */

	if (style.widget.kerning == 1)
		Blf.BLF_disable(style.widget.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
}

/* ************** init exit ************************ */

/* called on each .B.blend read */
/* reading without uifont will create one */
public static void uiStyleInit()
{
	uiFont font= (uiFont)U.uifonts.first;
	uiStyle style= (uiStyle)U.uistyles.first;

	/* recover from uninitialized dpi */
	U.dpi = Arithb.CLAMP(U.dpi, 72, 240);

	/* default builtin */
	if(font==null) {
		font= new uiFont();
		ListBaseUtil.BLI_addtail(U.uifonts, font);

		StringUtil.strcpy(font.filename,0, StringUtil.toCString("default"),0);
		font.uifont_id= UserDefTypes.UIFONT_DEFAULT;
	}

	for(font= (uiFont)U.uifonts.first; font!=null; font= font.next) {

        // TMP
        font.blf_id= (short)Blf.BLF_load_mem("default", null, 0);

//		if(font.uifont_id==UIFONT_DEFAULT) {
//			font.blf_id= BLF_load_mem("default", (unsigned char*)datatoc_bfont_ttf, datatoc_bfont_ttf_size);
//		}
//		else {
//			font.blf_id= BLF_load(font.filename);
//			if(font.blf_id == -1)
//				font.blf_id= BLF_load_mem("default", (unsigned char*)datatoc_bfont_ttf, datatoc_bfont_ttf_size);
//		}

		if (font.blf_id == -1) {
			System.out.printf("uiStyleInit error, no fonts available\n");
		}
		else {
//			Blf.BLF_set(font.blf_id);
			/* ? just for speed to initialize?
			 * Yes, this build the glyph cache and create
			 * the texture.
			 */
			Blf.BLF_size(font.blf_id, 11, U.dpi);
			Blf.BLF_size(font.blf_id, 12, U.dpi);
			Blf.BLF_size(font.blf_id, 14, U.dpi);
		}
	}

	if(style==null) {
		ui_style_new(U.uistyles, "Default Style");
	}
	
//	// XXX, this should be moved into a style, but for now best only load the monospaced font once.
//	if (blf_mono_font == -1)
//		blf_mono_font= BLF_load_mem_unique("monospace", (unsigned char *)datatoc_bmonofont_ttf, datatoc_bmonofont_ttf_size);
//
//	BLF_size(blf_mono_font, 12, 72);
//	
//	/* second for rendering else we get threading problems */
//	if (blf_mono_font_render == -1)
//		blf_mono_font_render= BLF_load_mem_unique("monospace", (unsigned char *)datatoc_bmonofont_ttf, datatoc_bmonofont_ttf_size);
//
//	BLF_size(blf_mono_font_render, 12, 72);
}

public static void uiStyleFontSet(uiFontStyle fs)
{
//	uiFont font= uifont_to_blfont(fs.uifont_id);
//
//	BLF_set(font.blf_id);
//	BLF_size(fs.points, U.dpi);
}

}
