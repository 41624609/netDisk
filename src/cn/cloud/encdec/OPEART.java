package cn.cloud.encdec;

/**
 * OPET V1.2
 * 采用分组方式加密，每组明文对应的ASCII字符串最小长度blkLen，
 * 如果明文段不满一组，则填充；

 * OPET V1.1;
 * 拐点采用2分法产生，增加拐点产生函数division_border(...);
 * 调整随机数产生范围从[0, 1]变为[ran_fac, 1-ran_fac];

 * OPET V1.0；
 * VL采用均匀分布；
 * GL默认有4个拐点，拐点之间部分采用累加；
 */
import java.util.Random;

public class OPEART  {
   private String zeros; /**明文的最小值，10个0的字符串.*/
   private String nines; /**明文的最大值，10个9的字符串.*/
   private int len; /**每次加密的位数,2. */
   private int depth; /**循环次数,6.*/
   private int expLen; /**一次加密ascii的长度,12.*/
   private long minAll; /**密文区间的最小值.*/
   private long maxAll; /**密文区间的最大值.*/
   private long key;  /**密匙.*/
   private int nFakeNums; /**小间隔数,22*/
   private  int divParts; /**分大间隔数,4.*/
   private float ran_fac; /**值域和间隔域划分的比例系数,初始0.5.*/
   private int blkLen; /**每组加密的真实长度,22,每组分2次加密.*/

     /**
      * No parameters constructor.
      */
    public OPEART() {
 		len = 2;
 		depth = 6;
 		expLen = len * depth;
        StringBuffer sb0 = new StringBuffer(expLen);
        StringBuffer sb1 = new StringBuffer(expLen);
        for (int i = 0; i < expLen; ++i) {
            sb0.append(0);
            sb1.append(9);
       }
        zeros = new String(sb0);
 	    nines = new String(sb1);
 		minAll = 0;
 		maxAll = minAll + (long) Math.pow(10, (expLen + 3));
 		key = 2568941234L;
 		nFakeNums = 22;
 		divParts = 4;
 		ran_fac = 0.5f;
 		blkLen = 22;
  }

    /**
     * compress a long value to a 4-byte string.
     * @param key long type value
     */
    public OPEART(long key) {
		len = 2;
		depth = 6;
		expLen = len * depth;
		StringBuffer sb0 = new StringBuffer(expLen);
	    StringBuffer sb1 = new StringBuffer(expLen);
	    for (int i = 0; i < expLen; ++i) {
	           sb0.append(0);
	           sb1.append(9);
	    }
	    zeros = new String(sb0);
	    nines = new String(sb1);
		minAll = 0;
		maxAll = minAll + (long) Math.pow(10, (expLen + 3));
		this.key = key;
		nFakeNums = 22;
		divParts = 4;
		ran_fac = 0.5f;
		blkLen = 22;
	}

    /**
	 * compress a long value to a 4-byte string.
	 * @param lvalue long type value
	 * @return 4-byte compressed string
	 */
    public String compress_expLen(long lvalue) {
		String res = "";
		for (int i = 0; i < 4; ++i) {
			res += (char) (lvalue >> ((3 - i) * 16));
	    /**16位作为一个char进行压缩，从高位到低位进行压缩*/
		}
		return res;
	}

	/**
	 * decompress 4-byte string to a long value.
	 * @param str 4-byte compressed string
	 * @return res long type value
	 */
    public long decompress_expLen(String str) {
		long res = 0;
		for (int i = 0; i < 4; ++i) {
			/**res*65536实现左移，高位的char对应的 int 在每次循环时都向左移16位*/
			res = res * 65536 + (int) str.charAt(i);
		}
		return res;
	}

	/**
	 * translate an unicode string to its ASCII format.
	 * @param str unicode string.
	 * @return ASCII string.
	 */
	public String string2ascii(String str) {
		String res = "";
		for (int i = 0; i < str.length(); ++i) {
			/**获得单个字符的ASCII码*/
			res += char2ascii(str.charAt(i));
		}
		return res;
	}

   /**
	 * translate character to ASCII string.
	 * @param ch character.
	 * @return ASCII string of ch.
	 */
	public String char2ascii(char ch) {
		int val = (int) ch;
		String str = "" + val;
		if (val < 256) {
			/**数字和英文时，位数不到3位的前面补0*/
			return "000".substring(str.length()) + str;
			 }
		else {
			/**中文时，前面加3，若小于5位数，则需要再在前面加0*/
			return "3" + "00000".substring(str.length()) + str;
		}
	}

	/**
	 * translate ASCII string to character.
	 * @param as ASCII string of a character.
	 * @return character.
	 */
	public char ascii2char(String as) {
		int val = Integer.valueOf(as);
		if (val > 300000) {
			 /**中文，前面有补3，需要去掉*/
			return (char) (val - 300000);
		}
		else {
			return (char) val;
		}
	}

	/**
	 * translate an ASCII string to an unicode string.
	 * @param asc ASCII string.
	 * @return unicode string.
	 */
	public String ascii2string(String asc) {
		String res = "";
		int i = 0;
		while (i < asc.length()) {
			char ch = asc.charAt(i);
			if (ch == '3') {
                /**中文时，6位一个*/
				res += ascii2char(asc.substring(i, i + 6));
				i += 6;
			}
			else if (ch < '3') {
				/**英文时，3位一个*/
				res += ascii2char(asc.substring(i, i + 3));
				i += 3;
				}
			else {
				break;
			}
		}
		return res;
	}

	/**
	 * encrypt plaintext string to ciphertext string.
	 * @param str plaintext string
	 * @return ciphertext string
	 */
	public String encrypt(String str) {
		String res = "";
		String asc = string2ascii(str);
		/**每次22个ASCII为一组*/
		for (int i = 0; i < asc.length(); i += blkLen) {
			String curBlock = "";
        if (asc.length() < i + blkLen) {  /**当asc剩余的长度小于22时*/	
				curBlock = asc.substring(i, asc.length()); /**取得剩下的所有值*/
				Random random = new Random(curBlock.hashCode()); /**字符串的哈希值填充*/
				String len = "" + curBlock.length(); /**len的值在2位数内，0-21 */
				len = "00".substring(len.length()) + len; /**使得len的值为2位,不足2位的前面补0*/
         		if (Integer.valueOf(len) == blkLen - 1) { /**加0是为了保序，00<任何一个字符的ASCII的前2位*/
         			curBlock += "0";
                }
         		else {
				curBlock += "00"; 
         		}
	     	while (curBlock.length() < blkLen) { /**长度小于22*/
					/**长度是6的倍数*/
					if (curBlock.length() % 6 == 0) {
						curBlock += random.nextInt(4); /**加上0-3*/
					}
					else {
						curBlock += random.nextInt(10); /**加上0-9*/
					}
				}
				curBlock += len; /**加上真实的长度*/
			}
			else { /**当asc剩余的长度大于等于22时，取22个ASCII，并在后面加入长度22*/
				curBlock = asc.substring(i, i + blkLen) + blkLen;
			}
			for (int j = 0; j < blkLen; j += expLen) { /**24位的curBlock每次加密12位*/
				long lRes = encrypt_expLen(curBlock.substring(j, j + expLen));
				res += compress_expLen(lRes);
			}
		}
		return res;
	}

   /**
	 * encrypt 12 ASCII.
	 * @param asc ASCII String
	 * @return res one ciphertext string
	 */
	public long encrypt_expLen(String asc) {
		/**seed=密匙和整个asc作为种子来产生随机数，只在最后得到密文值时使用*/
		long seed = key + Long.valueOf(asc);
		Random random = new Random(seed);
		float r = 0f; /**值域和间隔域的分割比例*/
		long min = minAll; /**区间的最小值（值域的最小值）*/
		long max = maxAll; /**区间的最大值（间隔域的最大值）*/
		long mid = 0; /**值域的最大值（间隔域的最小值）*/
		double vPart = 0, gLen = 0; /**vpart是值域的每份的值，glen是间隔域的长度*/
		int num = 0; /**值域的份数*/
        for (int dp = 0; dp < depth; ++dp) {
				num = (int) Math.pow(10, len); /**值域分成100份*/
			long divKey = key; /**分边界与更新min，max的密匙*/
			if (dp > 0) {
				divKey = key + Long.valueOf(asc.substring(0, dp * len));/**密钥与asc有关，每次都不同*/
			}
			Random ran = new Random(divKey);
			/**ran_fac=0.5，使得r为0.5到1*/
			r = ran_fac + ran.nextFloat() * (1 -  ran_fac);
			/**更新值域的最大值，为 min+原来整个区间的 r 倍*/
			mid = min + (long) ((max - min) * r);
			vPart = ((double) (mid - min)) / num; /**得到每份值域的值*/
			gLen = max - mid; /**间隔域的长度*/
			int n = Integer.valueOf(asc.substring(dp * len, (dp + 1) * len)); /**每次取2位*/

			/**[0][0]是小于 n 的边界值，[0][1]是对应的间隔域的百分比 ，
			  *[1][0]是大于等于n的边界值，[1][1]是对应的间隔域的百分比*/
			float[][] div = division(n, num, divKey); /**划分边界*/
			if (n != (int) div[1][0]) { /**n不是边界值*/
				/**min=n个值域+它前面的间隔，max=min+自己的一份值域*/
				min = min + (long) (n * vPart + div[0][1] * gLen);
				max = min + (long) vPart;
			}
			else { /**n是边界值*/
				/**min=n个值域加上他前面的间隔，max=min+自己的一份值域+自己的间隔域*/
				min = min + (long) (n * vPart + div[0][1] * gLen);
				max = min + (long) (vPart + (div[1][1] - div[0][1]) * gLen);
			}
		}
		long res = min + 1 + (long) ((max - min - 2) * random.nextDouble()); /**得到密文值*/
		return res;
	}

    /**
	 * division four big border and make sure the percent of n.
	 * @param n the 2 number
	 * @param num part of value
	 * @param seed the seed of key
	 * @return res border and percents
	 */
	public float[][] division(int n, int num, long seed) {
		float[][] res = new float[2][2];
		Random random = new Random(seed);
		random.nextFloat();

        /**把100映射到0-25来考虑，最后再进行还原*/
		int numPerPart = num / divParts; /**num/4,每个大间隔中点的个数,值为25*/
		int nPart = n / numPerPart; /** n/25，确定 n 属于第几个大间隔，0-24属于第一个，25-49属于第2个,以此类推*/
		int nInPart = n - nPart * numPerPart; /**n在该所属大间隔域中的相对位置，如24在第一个的24位，25在第二个的0位，26在第二个的1位*/

		/**5个拐点，第一个和最后一个确定，是0和1,拐点的位置是 0,25,50,75,100,但它们对应的值是随机的*/
		float[] maxFs = new float[divParts + 1];
		maxFs[0] = 0;
		maxFs[maxFs.length - 1] = 1f;
		/**确定3个中间拐点*/
		division_border(random, maxFs, 0, maxFs.length - 1);
		/**上界-下界，确定n所在的区间占的总的百分比*/
		float maxF = maxFs[nPart + 1] - maxFs[nPart];
		int sumN = 0; /**值累加和*/
		float sumF = 0.0f; /**百分比累加和*/

		/**lower bound 下边界，小于n*/
		res[0][0] = 0;
		res[0][1] = 0;
		/**res[0][0]存储的值sumN小于 n,当 sumN大于等于n时，跳出循环，但 res[0][0]并没有保存*/
		while (sumN < nInPart && sumF < maxF) {
			res[0][0] = sumN; /**值累加和*/
			res[0][1] = sumF; /**百分比累加和*/
			sumN += 1 + random.nextInt(3); /**累加  1-3*/
			sumF += random.nextFloat() / nFakeNums; /**r/22*/
		}
		/**upper bound, 上边界，大于等于 n*/
		res[1][0] = numPerPart - 1; /**24,区间最大值（大间隔）*/
		res[1][1] = maxF; /**n所在区间占的总的百分比*/
		if (sumN < res[1][0] && sumF < maxF) { /**上边界小于大间隔*/
			res[1][0] = sumN; /**保存上边界的值*/
			res[1][1] = sumF; /**对应的百分比*/
		}

		res[0][0] += nPart * numPerPart; /**还原到100*/
		res[1][0] += nPart * numPerPart; /**还原到100*/
		res[0][1] += maxFs[nPart]; /**加上前面的百分比才是真正对应的百分比*/
		res[1][1] += maxFs[nPart]; /**加上前面的百分比才是真正对应的百分比*/
		return res;
	}

    /**
	 * creat three middle border.
	 * @param ran Random value
	 * @param bdr save three border
	 * @param left the first one of bdr
	 * @param right the last one of bdr
	 */
	public void division_border(Random ran, float[] bdr, int left, int right) {
		if (left + 1 >= right) { /**左边等于右边-1时，比如3=4-1时，不用再确定中间的拐点，结束递归*/
			return;
		}
		int mid = (left + right) / 2; /**确定中间拐点的位置*/
		/**原来区间的 0.3-0.7的值作为中间拐点的值*/
		bdr[mid] = bdr[left] + (0.3f + 0.4f * ran.nextFloat()) * (bdr[right] - bdr[left]);
		division_border(ran, bdr, left, mid); /**递归产生 left和 mid 的中间的拐点*/
		division_border(ran, bdr, mid, right); /**递归产生 mid和 right 中间的拐点*/
	}

	/**
	 * decrypt ciphertext string to plaintext string.
	 * @param cipher ciphertext string
	 * @return res plaintext string
	 */
	public String decrypt(String cipher) {
		String res = "";
		String asc = "";
		for (int i = 0; i < cipher.length(); i += 4) {
			/**压缩时将4个char表示一个密文。解压缩密文值*/
			long lval = decompress_expLen(cipher.substring(i, i + 4));
			asc += decrypt_expLen(lval); /**解密单个密文*/
		}
		for (int i = 0; i < asc.length(); i += (blkLen + 2)) { /**每次取24个ASCII*/
			int len = Integer.valueOf(asc.substring(i + blkLen, i + blkLen + 2)); /**实际长度*/
			res += asc.substring(i, i + len); /**在24个中取len长度的值*/
		}
		res = ascii2string(res); /**ASCII转化为对应的字符串*/
		return res;
	}

   /**
	 * decrypt ciphertext.
	 * @param lvalue ciphertext value
	 * @return res one plaintext ASCII string
	 */
	public String decrypt_expLen(long lvalue) {
		String res = "";
		float r = 0f;
		long min = minAll;
		long max = maxAll;
		long mid = 0;
		double vPart = 0, gLen = 0;
		int num = 0;
		for (int dp = 0; dp < depth; ++dp) { /**6次循环*/
				num = (int) Math.pow(10, len); /**值域分100份*/
			long divKey = key; /**更新间隔与min和max的密匙*/
			if (dp > 0) {
				divKey = key + Long.valueOf(res);
			}
			Random ran = new Random(divKey);
			r = ran_fac + ran.nextFloat() * (1 - ran_fac);
			mid = min + (long) ((max - min) * r);
			vPart = ((double) (mid - min)) / num;
			gLen = max - mid;
			/**res[0][0]存n,res[0][1]存小于n的值对应的间隔域百分比
			 *res[1][0]存大于等于n的值，res[1][1]存对应的间隔域的百分比 */
			float [][]ret = belongTo(lvalue, num, divKey, min, max, r); /**确定密文所对应的n的相关值*/
			int n = (int) ret[0][0]; /**获得n*/
			String temp = n + "";
			res += "00".substring(temp.length()) + temp; /**前面补0,使其为2位数*/
			if (n != (int) ret[1][0]) { /**n是边界值*/
				/**min=n个值域加上它前面的间隔，max=min+自己的一份值域，同加密*/
				min = min + (long) (n * vPart + ret[0][1] * gLen);
				max = min + (long) vPart;
			}
			else { /**n是边界值*/
				/**min=n个值域加上它前面的间隔，max=min+自己的一份值域+自己的间隔域*/
				min = min + (long) (n * vPart + ret[0][1] * gLen);
				max = min + (long) (vPart + (ret[1][1] - ret[0][1]) * gLen);
			}
		}
		return res;
	}

    /**
	 * division four big border and make sure the percent of n.
	 * @param lvalue ciphertext value
	 * @param num part of value
	 * @param seed the seed of key
	 * @param min min of border
	 * @param max max of border
	 * @param r value and interval percent
	 * @return res border and percents
	 */
	public float[][] belongTo(long lvalue, int num, long seed, long min, long max, float r) {
		float [][]res = new float[2][2];
		int n = 0;
		Random random = new Random(seed);
		random.nextFloat();
		long mid = min + (long) ((max - min) * r);
		double vPart = ((double) (mid - min)) / num;
		double gLen = max - mid; /**double，增加精度*/

		int nPart = 0;
		int numPerPart = num / divParts; /**100/4=25*/
		float[] maxFs = new float[divParts + 1]; /**5个拐点*/
		maxFs[0] = 0;
		maxFs[maxFs.length - 1] = 1f;
		division_border(random, maxFs, 0, maxFs.length - 1); /**中间3个拐点,同加密*/
		/**divParts=4，确定lvalue属于哪个大间隔，npart=0时是0-24*/
		for (; nPart < divParts; ++nPart) {
			/**lBdr是一个大间隔的最大值*/
			long lBdr = min + (long) ((nPart + 1) * numPerPart * vPart + maxFs[nPart + 1] * gLen);
			if (lvalue <= lBdr) { /**lvalue小于大间隔的最大值，即属于这个大间隔*/
				break;
			}
		}
		/**所属的大间隔的占的总的百分比*/
		float maxF = maxFs[nPart + 1] - maxFs[nPart];
		int sumN = 0;
		float sumF = 0;
		/**lower bound 下边界，小于n
		 *res[0][0]存小于2位数n的值,res[0][1]是对应的百分比*/
		res[0][0] = 0;
		res[0][1] = 0;
		long tempMax = min;
		while (sumN < numPerPart - 1 && sumF < maxF) {
			/**tempMax的值为sumN对应的区间的最大值，即值域和间隔域的和，
			 * 值域加上1份因为加上自己所占的这一份值域，取得n对应的上边界。*/
			tempMax = min + (long) ((sumN + nPart * numPerPart + 1) * vPart + (sumF + maxFs[nPart]) * gLen);
			if (lvalue <= tempMax) { /**小于区间的最大值时，确定上限*/
				break;
			}
			res[0][0] = sumN; /**累加值和*/
			res[0][1] = sumF; /**累加百分比和*/
			sumN += 1 + random.nextInt(3);
			sumF += random.nextFloat() / nFakeNums;
		}
		/**upper bound, 上边界，大于等于 n*/
		res[1][0] = numPerPart - 1; /**24,区间最大值（大间隔）*/
		res[1][1] = maxF; /**n所在区间占的总的百分比*/
		if (sumN < res[1][0] && sumF < maxF) { /**上边界小于大间隔*/
			res[1][0] = sumN; /**保存上边界的值*/
			res[1][1] = sumF; /**对应的百分比*/
		}

		/**确定n的值*/
		n = (int) res[0][0];
		/**小于n的值（有间隔域）对应的区间的最大值，和前面的tempMax类似*/
		tempMax = min + (long) ((n + nPart * numPerPart + 1) * vPart + (res[0][1] + maxFs[nPart]) * gLen);
		double vTemp = 0;
		while (n < res[1][0]) { /**确定n，tempMax每次增加一份值域值来确定n*/
			vTemp += vPart;
			++n;
			if (lvalue < tempMax + vTemp) { /**确定n，不能取<=,因为有可能密文刚好是min（max）*/
				break;
			}
		}
		res[0][0] = n; /**保存n*/
		res[0][0] += nPart * numPerPart; /**还原到100*/
		res[1][0] += nPart * numPerPart; /**还原到100*/
		res[0][1] += maxFs[nPart]; /**加上前面的百分比才是真正的百分比*/
		res[1][1] += maxFs[nPart]; /**加上前面的百分比才是真正的百分比*/
		return res;
	}
}
