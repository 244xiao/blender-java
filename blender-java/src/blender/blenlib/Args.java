package blender.blenlib;

import java.util.HashMap;
import java.util.Map;

import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;

public class Args {
	/*
	 * A general argument parsing module
	 *
	 * $Id: BLI_args.c 35246 2011-02-27 20:37:56Z jesterking $
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
	 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
	 * All rights reserved.
	 *
	 * The Original Code is: all of this file.
	 *
	 * Contributor(s): none yet.
	 *
	 * ***** END GPL LICENSE BLOCK *****
	 */

	/** \file blender/blenlib/intern/BLI_args.c
	 *  \ingroup bli
	 */


//	#include <ctype.h> /* for tolower */
//
//	#include "MEM_guardedalloc.h"
//
//	#include "BLI_listbase.h"
//	#include "BLI_string.h"
//	#include "BLI_utildefines.h"
//	#include "BLI_args.h"
//	#include "BLI_ghash.h"
	
	/* returns the number of extra arguments consumed by the function. 0 is normal value, -1 stops parsing arguments, other negative indicates skip */
	public static interface BA_ArgCallback {
		public int call(int argc, String[] argv, int offset, Object data);
	};

//	static char NO_DOCS[] = "NO DOCUMENTATION SPECIFIED";
	static final String NO_DOCS = "NO DOCUMENTATION SPECIFIED";

//	struct bArgDoc;
	public static class bArgDoc extends Link<bArgDoc> {
		//public bArgDoc next, prev;
		public String short_arg;
		public String long_arg;
		public String documentation;
		public int done;
	};

	public static class bAKey {
		public String arg;
//		public uintptr_t pass; /* cast easier */
		public int pass;
		public int case_str; /* case specific or not */
	};

	public static class bArgument {
		public bAKey key;
		public BA_ArgCallback func;
		public Object data;
		public bArgDoc doc;
	};

	public static class bArgs {
		public ListBase<bArgDoc> docs = new ListBase<bArgDoc>();
//		public GHash  *items;
		public Map<bAKey, bArgument> items;
		public int 	argc;
		public String[] argv;
		public int[] passes;
	};

//	static unsigned int case_strhash(const void *ptr) {
//		const char *s= ptr;
//		unsigned int i= 0;
//		unsigned char c;
//
//		while ( (c= tolower(*s++)) )
//			i= i*37 + c;
//
//		return i;
//	}
//
//	static unsigned int	keyhash(const void *ptr)
//	{
//		const bAKey *k = ptr;
//		return case_strhash(k->arg); // ^ BLI_ghashutil_inthash((void*)k->pass);
//	}
//
//	static int keycmp(const void *a, const void *b)
//	{
//		const bAKey *ka = a;
//		const bAKey *kb = b;
//		if (ka->pass == kb->pass || ka->pass == -1 || kb->pass == -1) { /* -1 is wildcard for pass */
//			if (ka->case_str == 1 || kb->case_str == 1)
//				return BLI_strcasecmp(ka->arg, kb->arg);
//			else
//				return strcmp(ka->arg, kb->arg);
//		} else {
//			return BLI_ghashutil_intcmp((const void*)ka->pass, (const void*)kb->pass);
//		}
//	}

	static bArgument lookUp(bArgs ba, String arg, int pass, int case_str)
	{
		bAKey key = new bAKey();

		key.case_str = case_str;
		key.pass = pass;
		key.arg = arg;

//		return BLI_ghash_lookup(ba.items, key);
		return ba.items.get(key);
	}

	public static bArgs BLI_argsInit(int argc, String[] argv)
	{
		bArgs ba = new bArgs();
		ba.passes = new int[argc];
//		ba.items = BLI_ghash_new(keyhash, keycmp, "bArgs passes gh");
		ba.items = new HashMap<bAKey, bArgument>();
		ba.docs.first = ba.docs.last = null;
		ba.argc = argc;
		ba.argv = argv;

		return ba;
	}

//	static void freeItem(void *val)
//	{
//		MEM_freeN(val);
//	}

	public static void BLI_argsFree(bArgs ba)
	{
//		BLI_ghash_free(ba.items, freeItem, freeItem);
		ba.items = null;
//		MEM_freeN(ba->passes);
		ba.passes = null;
		ListBaseUtil.BLI_freelistN(ba.docs);
//		MEM_freeN(ba);
	}

//	void BLI_argsPrint(struct bArgs *ba)
//	{
//		int i;
//		for (i = 0; i < ba->argc; i++) {
//			printf("argv[%d] = %s\n", i, ba->argv[i]);
//		}
//	}
//
//	const char **BLI_argsArgv(struct bArgs *ba)
//	{
//		return ba->argv;
//	}

	static bArgDoc internalDocs(bArgs ba, String short_arg, String long_arg, String doc)
	{
		bArgDoc d;

		d = new bArgDoc();

		if (doc == null)
			doc = NO_DOCS;

		d.short_arg = short_arg;
		d.long_arg = long_arg;
		d.documentation = doc;

		ListBaseUtil.BLI_addtail(ba.docs, d);

		return d;
	}

	static void internalAdd(bArgs ba, String arg, int pass, int case_str, BA_ArgCallback cb, Object data, bArgDoc d)
	{
		bArgument a;
		bAKey key;

		a = lookUp(ba, arg, pass, case_str);

		if (a!=null) {
			System.out.printf("WARNING: conflicting argument\n");
			System.out.printf("\ttrying to add '%s' on pass %i, %scase sensitive\n", arg, pass, case_str == 1? "not ": "");
			System.out.printf("\tconflict with '%s' on pass %i, %scase sensitive\n\n", a.key.arg, (int)a.key.pass, a.key.case_str == 1? "not ": "");
		}

		a = new bArgument();
		key = new bAKey();

		key.arg = arg;
		key.pass = pass;
		key.case_str = case_str;

		a.key = key;
		a.func = cb;
		a.data = data;
		a.doc = d;

//		BLI_ghash_insert(ba.items, key, a);
		ba.items.put(key, a);
	}

	public static void BLI_argsAddCase(bArgs ba, int pass, String short_arg, int short_case, String long_arg, int long_case, String doc, BA_ArgCallback cb, Object data)
	{
		bArgDoc d = internalDocs(ba, short_arg, long_arg, doc);

		if (short_arg!=null)
			internalAdd(ba, short_arg, pass, short_case, cb, data, d);

		if (long_arg!=null)
			internalAdd(ba, long_arg, pass, long_case, cb, data, d);


	}

	public static void BLI_argsAdd(bArgs ba, int pass, String short_arg, String long_arg, String doc, BA_ArgCallback cb, Object data)
	{
		BLI_argsAddCase(ba, pass, short_arg, 0, long_arg, 0, doc, cb, data);
	}

	static void internalDocPrint(bArgDoc d)
	{
		if (d.short_arg!=null && d.long_arg!=null)
			System.out.printf("%s or %s", d.short_arg, d.long_arg);
		else if (d.short_arg!=null)
			System.out.printf("%s", d.short_arg);
		else if (d.long_arg!=null)
			System.out.printf("%s", d.long_arg);

		System.out.printf(" %s\n\n", d.documentation);
	}

	public static void BLI_argsPrintArgDoc(bArgs ba, String arg)
	{
		bArgument a = lookUp(ba, arg, -1, -1);

		if (a!=null)
		{
			bArgDoc d = a.doc;

			internalDocPrint(d);

			d.done = 1;
		}
	}

	public static void BLI_argsPrintOtherDoc(bArgs ba)
	{
		bArgDoc d;

		for( d = ba.docs.first; d!=null; d = d.next)
		{
			if (d.done == 0)
			{
				internalDocPrint(d);
			}
		}
	}

	public static void BLI_argsParse(bArgs ba, int pass, BA_ArgCallback default_cb, Object default_data)
	{
		int i = 0;

		for( i = 1; i < ba.argc; i++) { /* skip argv[0] */
			if (ba.passes[i] == 0) {
				 /* -1 signal what side of the comparison it is */
				bArgument a = lookUp(ba, ba.argv[i], pass, -1);
				BA_ArgCallback func = null;
				Object data = null;

				if (a!=null) {
					func = a.func;
					data = a.data;
				} else {
					func = default_cb;
					data = default_data;
				}

				if (func!=null) {
					int retval = func.call(ba.argc - i, ba.argv,i, data);

					if (retval >= 0) {
						int j;

						/* use extra arguments */
						for (j = 0; j <= retval; j++) {
							ba.passes[i + j] = pass;
						}
						i += retval;
					} else if (retval == -1){
						if (a.key.pass != -1)
							ba.passes[i] = pass;
						break;
					}
				}
			}
		}
	}

}
