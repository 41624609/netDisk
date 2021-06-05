package cn.cloud.encdec;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;


public class RCOPES {
	
	private int key;
	private long minAll;
	private long maxAll;
	private long bucSize;
	private int M_NUM = 16;
	private int size=4*2+8*3;
/*
 * bucSizeΪͰ��С
 * ������D=[minAll,maxAll]
 * Ͱ����ĿΪ  (maxAll-minAll)/bucSize
 */
	public RCOPES(String passwd) {
		key = passwd.hashCode();
		System.out.println("key��ֵΪ��"+key);
		minAll = 0;
		maxAll = (long)Math.pow(10, 18);
		bucSize = (long)Math.pow(10, 7);
	}
	
	public RCOPES(int seed) {
		key = seed;
		minAll = 0;
		maxAll = (long)Math.pow(10, 18);
		bucSize = (long)Math.pow(10, 7);
	}
	/*
	 * wRID������仮���㷨��ʵ��
	 * parent[]���������䣬seed����. part��Ҫ�������������� ��Ȩ����wt
	 */
	long[] childOf(long[] parent, long seed, int part, WeightTree wt) {
//		Trace.output("wRID seed: " + seed);
		long[] ret = new long[2];
		Random random = new Random(seed);		
		long len = parent[1] - parent[0];
		Random lRan = new Random(random.nextInt());
		Random rRan = new Random(random.nextInt());
		int node = wt.root();
		double lval = 1 * (0.1+0.8*lRan.nextDouble());
		double rval = wt.get(node) * (0.1+0.8*rRan.nextDouble());
		double scale = lval / (lval + rval);
		long lSeed = lRan.nextLong();//����l
		long rSeed = rRan.nextLong();//����r
		if(part == 0) {
			ret[0] = parent[0];
			ret[1] = parent[0] + (long)(len*scale);
		}
		else {
			ret[0] = parent[0] + 1 + (long)(len*scale);
			ret[1] = parent[1];
			int step = (int)Math.pow(2, (Math.log10(M_NUM)) / Math.log10(2) - 1);
			int base = 0;
			while(!wt.isLeaf(node)) {
				len = ret[1] - ret[0];
				lRan = new Random(lSeed);
				rRan = new Random(rSeed);
				lval = wt.leftChild(node) * (0.1+0.8*lRan.nextDouble());
				rval = wt.rightChild(node) * (0.1+0.8*rRan.nextDouble());
				scale = lval / (lval + rval);
				if(part <= base + step) {
					ret[1] = ret[0] + (long)(len*scale);
					node = wt.leftChild(node);
					step /= 2;
					lSeed = lRan.nextLong() & 0xFFFFFFFF;
					rSeed = rRan.nextLong() & 0xFFFFFFFF;
//					lSeed++;
//					rSeed++;
				}
				else {
					ret[0] = ret[0] + 1 + (long)(len*scale);
					node = wt.rightChild(node);
					base += step;
					step /= 2;
					lSeed = (lRan.nextLong() >> 32) & 0xFFFFFFFF;
					rSeed = (rRan.nextLong() >> 32) & 0xFFFFFFFF;
//					lSeed--;
//					rSeed--;
				}
			}
		}
		
		return ret;
	}
	//���ܳ���
public String encryptSize(String plainText) {
		
		WeightTree wt = new WeightTree(M_NUM);
		this.size+= wt.treeSize();
		StringBuilder sb = new StringBuilder();
		long[] range = new long[]{minAll, maxAll};
		long seed = key;
		
		//translate plainText to numeric string
		String hex = Codec.string2hex(plainText);
		
		for(int index = 0; index < hex.length(); ++index) {
			char ch = hex.charAt(index);
			//Start to process ch ...
			int nPart = Codec.char2int(ch) + 1;
			range = childOf(range, seed, nPart, wt);
			wt.update(nPart-1);
			if(isDevided(range)) {
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
				seed = key;
				continue;
			}
			else if(index == hex.length()-1) {				
				seed += nPart - 1;
				range = childOf(range, seed, 0, wt);
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
			}
			seed += nPart - 1;
		}
		
		return sb.toString();
	}
	/*
	 * OPRIT���ܹ��̵�ʵ��
	 */
	public String encrypt(String plainText) {

		WeightTree wt = new WeightTree(M_NUM);
		StringBuilder sb = new StringBuilder();
		long[] range = new long[]{minAll, maxAll};
		long seed = key;
		
		//translate plainText to numeric string
		String hex = Codec.string2hex(plainText);
		
		for(int index = 0; index < hex.length(); ++index) {
			char ch = hex.charAt(index);
			//Start to process ch ...
			int nPart = Codec.char2int(ch) + 1;
			range = childOf(range, seed, nPart, wt);
			wt.update(nPart-1);
			if(isDevided(range)) {//�Ѿ����ﲻ�ɻ��ֵ�ֵ�ڵ�
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
				seed = key;
				continue;
			}
			else if(index == hex.length()-1) {		//�Ѿ������ַ�����ĩβ		
				seed += nPart - 1;
				range = childOf(range, seed, 0, wt);
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
			}
			seed += nPart - 1;
		}
		
		return sb.toString();
	}
	
	public String encrypt(String plainText, WeightTree wt) {
//		WeightTree wt = new WeightTree(M_NUM);
		StringBuilder sb = new StringBuilder();
		long[] range = new long[]{minAll, maxAll};
		long seed = key;
		
		//translate plainText to numeric string
		String hex = Codec.string2hex(plainText);
		
		for(int index = 0; index < hex.length(); ++index) {
			char ch = hex.charAt(index);
			//Start to process ch ...
			int nPart = Codec.char2int(ch) + 1;
			range = childOf(range, seed, nPart, wt);
			wt.update(nPart-1);
			if(isDevided(range)) {
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
				seed = key;
				continue;
			}
			else if(index == hex.length()-1) {				
				seed += nPart - 1;
				range = childOf(range, seed, 0, wt);
				sb.append(Codec.long2hex((rndInRange(range, seed))));
				range[0] = minAll;
				range[1] = maxAll;
			}
			seed += nPart - 1;
		}
		
		return sb.toString();
	}
	
	long[] belongTo(long[] parent, long seed, long value, WeightTree wt) {
//		Trace.output("seed: " + seed);
		long[] ret = new long[3];
		Random random = new Random(seed);
		long len = parent[1] - parent[0];
		int node = wt.root();
		Random lRan = new Random(random.nextInt());
		Random rRan = new Random(random.nextInt());
		double lval = 1 * (0.1+0.8*lRan.nextDouble());
		double rval = wt.get(node) * (0.1+0.8*rRan.nextDouble());
		double scale = lval / (lval + rval);
		long lSeed = lRan.nextLong();
		long rSeed = rRan.nextLong();
		if(value <= parent[0]+(long)(len*scale)) {
			ret[0] = parent[0];
			ret[1] = parent[0] + (long)(len*scale);
			ret[2] = 0;
		}
		else {
			parent[0] = parent[0] + 1 + (long)(len*scale);
			int step = (int)Math.pow(2, (Math.log10(M_NUM)) / Math.log10(2) - 1);
			int base = 0;
			while(!wt.isLeaf(node)) {
				len = parent[1] - parent[0];
				lRan = new Random(lSeed);
				rRan = new Random(rSeed);
				lval = wt.leftChild(node) * (0.1+0.8*lRan.nextDouble());
				rval = wt.rightChild(node) * (0.1+0.8*rRan.nextDouble());
				scale = lval / (lval + rval);
				if(value <= parent[0]+(long)(len*scale)) {
					parent[1] = parent[0] + (long)(len*scale);
					node = wt.leftChild(node);
					step /= 2;
					lSeed = lRan.nextLong() & 0xFFFFFFFF;
					rSeed = rRan.nextLong() & 0xFFFFFFFF;
//					lSeed++;
//					rSeed++;
				}
				else {
					parent[0] = parent[0] + 1 + (long)(len*scale);
					node = wt.rightChild(node);
					base += step;
					step /= 2;
					lSeed = (lRan.nextLong() >> 32) & 0xFFFFFFFF;
					rSeed = (rRan.nextLong() >> 32) & 0xFFFFFFFF;
//					lSeed--;
//					rSeed--;
				}
			}
			ret[0] = parent[0];
			ret[1] = parent[1];
			ret[2] = base + step + 1;
		}
		
		return ret;
	}
	
	public String decrypt(String cipherText) {
		StringBuilder ret = new StringBuilder();
		long seed = key;
		WeightTree wt = new WeightTree(M_NUM);
		
		for(int index = 0; index < cipherText.length(); index += 16) {
			long lval = Codec.hex2long(cipherText.substring(index, index+16));
			long[] range = new long[]{minAll, maxAll};
			while(!isDevided(range)) {
				long[] res = belongTo(range, seed, lval, wt);
				wt.update((int)res[2]-1);
//				Trace.output("res: " + Arrays.toString(res));
				if(res[2] == 0) {
					seed = key;
					range = new long[]{minAll, maxAll};
					break;
				}
				range[0] = res[0];
				range[1] = res[1];
//				Trace.output("range: " + Arrays.toString(range));
				ret.append(Codec.int2char((int)(res[2]-1)));
				seed += res[2] - 1;
			}
			seed = key;
		}
		return Codec.hex2string(ret.toString());
	}
	
	public String decrypt(String cipherText, WeightTree wt) {
		StringBuilder ret = new StringBuilder();
		long seed = key;
//		WeightTree wt = new WeightTree(M_NUM);
		
		for(int index = 0; index < cipherText.length(); index += 16) {
			long lval = Codec.hex2long(cipherText.substring(index, index+16));
			long[] range = new long[]{minAll, maxAll};
			while(!isDevided(range)) {
				long[] res = belongTo(range, seed, lval, wt);
				wt.update((int)res[2]-1);
//				Trace.output("res: " + Arrays.toString(res));
				if(res[2] == 0) {
					seed = key;
					range = new long[]{minAll, maxAll};
					break;
				}
				range[0] = res[0];
				range[1] = res[1];
//				Trace.output("range: " + Arrays.toString(range));
				ret.append(Codec.int2char((int)(res[2]-1)));
				seed += res[2] - 1;
			}
			seed = key;
		}
		return Codec.hex2string(ret.toString());
	}
	
	long rndInRange(long[] range, long seed) {
		long ret = 0;
		Random random = new Random(seed);
		ret = range[0] + (long)((range[1]-range[0]) * random.nextDouble());		
		return ret;
	}
	/*
	 * �Ƿ��Ѿ��ﵽ���ɻ��ֵ�ֵ�ڵ�
	 */
	boolean isDevided(long[] range) {
//		if(range[1] - range[0] <= thresh)
//			return true;
		return (range[0]/bucSize == range[1]/bucSize);
	}
	
	public static void main(String[] args) {
		RCOPES dop = new RCOPES("clouddesign");
		String plain = "secure";
		String str = dop.encrypt(plain);
		System.out.println("���ܺ������Ϊ��"+str);
//		for(int i = 0; i < Integer.MAX_VALUE; ++i) {
		for(int i = 0; i < 10; ++i) {
			try {
					RCOPES dopdec = new RCOPES(i);
					String dec = dopdec.decrypt(str);
					System.out.println("���ܺ������Ϊ��"+dec);
					if(plain.equals(dec)) {
						//System.out.println("���"+i);
						break;
					}
					//System.out.println(""+i);

			} catch (Exception e) {
				
			}	
		}
	}
}