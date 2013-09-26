/* util.c
 *
 * various string, file, list operations.
 *
 *
 * $Id: string.c 21094 2009-06-23 00:09:26Z gsrb3d $
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

public class StringUtil {

    public static String toJString(byte[] s, int offset) {
    	if (s==null) return null;
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = (char) (s[offset++] & 0xFF)) != 0) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static byte[] toCString(String s) {
    	if (s==null) return null;
        byte[] b = new byte[s.length() + 1];
        for (int i = 0; i < s.length(); i++) {
            b[i] = (byte) s.charAt(i);
        }
        b[s.length()] = 0;
        return b;
    }

    public static int atoi(byte[] s, int offset) {
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = (char) (s[offset++] & 0xFF)) != 0 && Character.isDigit(c)) {
            sb.append(c);
        }
        if (sb.length() == 0) {
            return 0;
        }
        return Integer.parseInt(sb.toString());
    }

    public static long atol(byte[] s, int offset) {
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = (char) (s[offset++] & 0xFF)) != 0 && Character.isDigit(c)) {
            sb.append(c);
        }
        if (sb.length() == 0) {
            return 0L;
        }
        return Long.parseLong(sb.toString());
    }

    public static int strlen(byte[] s, int offset) {
        int n = 0;
        while (s[offset++] != 0) {
            n++;
        }
        return n;
    }

    public static int strcmp(byte[] s1, int offset1, byte[] s2, int offset2) {
        while (s1[offset1] != '\0' && s1[offset1] == s2[offset2]) {
            offset1++;
            offset2++;
        }
        return (s1[offset1] & 0xFF) - (s2[offset2] & 0xFF);
    }

    public static int strncmp(byte[] s1, int offset1, byte[] s2, int offset2, int n) {
        int u1, u2;
        while (n-- > 0) {
            u1 = s1[offset1++] & 0xFF;
            u2 = s2[offset2++] & 0xFF;
            if (u1 != u2) {
                return u1 - u2;
            }
            if (u1 == 0) {
                return 0;
            }
        }
        return 0;
    }

    public static byte[] strcpy(byte[] dst0, int offset1, byte[] src0, int offset2) {
        while ((dst0[offset1++] = src0[offset2++]) != 0) { }
        return dst0;
    }

    public static byte[] strncpy(byte[] dst0, int offset1, byte[] src0, int offset2, int count) {
        while (count > 0) {
            --count;
            if ((dst0[offset1++] = src0[offset2++]) == 0) {
                break;
            }
        }
        while (count-- > 0) {
            dst0[offset1++] = 0;
        }
        return dst0;
    }

    public static int strchr(byte[] s1, int offset, int c) {
        while (s1[offset] != 0 && (s1[offset] & 0xFF) != c) {
            offset++;
        }

        if ((s1[offset] & 0xFF) != c) {
            offset = -1;
        }

        return offset;
    }
    
    public static int strrchr(byte[] s, int offset, int c) {
//      char *rtnval = 0;
    	int rtnval = -1;

      do {
        if ((s[offset]&0xFF) == c)
//          rtnval = (char*) s;
        	rtnval = offset;
      } while (s[offset++]!=0);
      return (rtnval);
    }


    public static int strstr(byte[] buf, int offset1, byte[] sub, int offset2) {
        int bp;

        if (sub[offset2] == 0) {
            return -1;
        }
        for (;;) {
            if (buf[offset1] == 0) {
                break;
            }
            bp = offset1;
            for (;;) {
                if (sub[offset2] == 0) {
                    return bp;
                }
                if (buf[offset1++] != sub[offset2++]) {
                    break;
                }
            }
            offset2 -= offset1;
            offset2 += bp;
            bp += 1;
        }
        return -1;
    }

    public static byte[] strcat(byte[] s1, byte[] s2, int offset2) {
        int s = 0;
        while (s1[s] != 0) {
            s++;
        }
        while ((s1[s++] = s2[offset2++]) != 0) { }
        return s1;
    }

    public static byte[] BLI_strdupn(byte[] str, int offset, int len) {
        byte[] n = new byte[len + 1];
        System.arraycopy(str, offset, n, 0, len);
        n[len] = '\0';
        return n;
    }

    public static byte[] BLI_strdup(byte[] str, int offset) {
        return BLI_strdupn(str, offset, strlen(str, offset));
    }

    public static byte[] BLI_strncpy(byte[] dst, int offsetDst, byte[] src, int offsetSrc, int maxncpy) {
	int srclen= strlen(src,offsetSrc);
        int cpylen = (srclen > (maxncpy)) ? (maxncpy) : srclen;
        System.arraycopy(src, offsetSrc, dst, offsetDst, cpylen);
	dst[offsetDst + cpylen]= '\0';
        return dst;
    }

//int BLI_snprintf(char *buffer, size_t count, const char *format, ...)
//{
//	int n;
//	va_list arg;
//
//	va_start(arg, format);
//	n = vsnprintf(buffer, count, format, arg);
//
//	if (n != -1 && n < count) {
//		buffer[n] = '\0';
//	} else {
//		buffer[count-1] = '\0';
//	}
//
//	va_end(arg);
//	return n;
//}
//
//char *BLI_sprintfN(const char *format, ...)
//{
//	DynStr *ds;
//	va_list arg;
//	char *n;
//
//	va_start(arg, format);
//
//	ds= BLI_dynstr_new();
//	BLI_dynstr_vappendf(ds, format, arg);
//	n= BLI_dynstr_get_cstring(ds);
//	BLI_dynstr_free(ds);
//
//	va_end(arg);
//
//	return n;
//}

    public static boolean BLI_streq(byte[] a, int offset1, byte[] b, int offset2) {
        return strcmp(a, offset1, b, offset2) == 0;
    }

//int BLI_strcaseeq(const char *a, const char *b)
//{
//	return (BLI_strcasecmp(a, b)==0);
//}
//
///* strcasestr not available in MSVC */
//char *BLI_strcasestr(const char *s, const char *find)
//{
//    register char c, sc;
//    register size_t len;
//
//    if ((c = *find++) != 0) {
//		c= tolower(c);
//		len = strlen(find);
//		do {
//			do {
//				if ((sc = *s++) == 0)
//					return (NULL);
//				sc= tolower(sc);
//			} while (sc != c);
//		} while (BLI_strncasecmp(s, find, len) != 0);
//		s--;
//    }
//    return ((char *) s);
//}
//
//
//int BLI_strcasecmp(const char *s1, const char *s2) {
//	int i;
//
//	for (i=0; ; i++) {
//		char c1 = tolower(s1[i]);
//		char c2 = tolower(s2[i]);
//
//		if (c1<c2) {
//			return -1;
//		} else if (c1>c2) {
//			return 1;
//		} else if (c1==0) {
//			break;
//		}
//	}
//
//	return 0;
//}
//
//int BLI_strncasecmp(const char *s1, const char *s2, int n) {
//	int i;
//
//	for (i=0; i<n; i++) {
//		char c1 = tolower(s1[i]);
//		char c2 = tolower(s2[i]);
//
//		if (c1<c2) {
//			return -1;
//		} else if (c1>c2) {
//			return 1;
//		} else if (c1==0) {
//			break;
//		}
//	}
//
//	return 0;
//}
//
///* natural string compare, keeping numbers in order */
//int BLI_natstrcmp(const char *s1, const char *s2)
//{
//	int d1= 0, d2= 0;
//
//	/* if both chars are numeric, to a strtol().
//	   then increase string deltas as long they are
//	   numeric, else do a tolower and char compare */
//
//	while(1) {
//		char c1 = tolower(s1[d1]);
//		char c2 = tolower(s2[d2]);
//
//		if( isdigit(c1) && isdigit(c2) ) {
//			int val1, val2;
//
//			val1= (int)strtol(s1+d1, (char **)NULL, 10);
//			val2= (int)strtol(s2+d2, (char **)NULL, 10);
//
//			if (val1<val2) {
//				return -1;
//			} else if (val1>val2) {
//				return 1;
//			}
//			d1++;
//			while( isdigit(s1[d1]) )
//				d1++;
//			d2++;
//			while( isdigit(s2[d2]) )
//				d2++;
//
//			c1 = tolower(s1[d1]);
//			c2 = tolower(s2[d2]);
//		}
//
//		if (c1<c2) {
//			return -1;
//		} else if (c1>c2) {
//			return 1;
//		} else if (c1==0) {
//			break;
//		}
//		d1++;
//		d2++;
//	}
//	return 0;
//}

    public static String BLI_timestr(double _time) {
        String str;
        /* format 00:00:00.00 (hr:min:sec) string has to be 12 long */
        int hr = ((int) _time) / (60 * 60);
        int min = (((int) _time) / 60) % 60;
        int sec = ((int) (_time)) % 60;
        int hun = ((int) (_time * 100.0)) % 100;

        if (hr != 0) {
            str = String.format("%d:%d:%d.%d", hr, min, sec, hun);
        } else {
            str = String.format("%d:%d.%d", min, sec, hun);
        }

        return str;
    }

}