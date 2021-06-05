package cn.cloud.encdec;


public class Codec {
	private static final char[] hexChar = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
};
	
    public static String string2hex(String str) {
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < str.length(); ++i) {
        	int ch = (int)str.charAt(i);

        	String sval = Integer.toHexString(ch);
        	if(ch < 128)
        		ret.append(("00"+sval).substring(sval.length()));
        	else if(ch < 16384)
        		ret.append("80000".substring(0,5-sval.length()) + sval);
        	else if(ch < 40960)
        		ret.append(Integer.toHexString(ch+20480));
        	else if(ch < 65536)
        		ret.append("f"+sval);
        	else 
        		return null;
        }
        
        //Trace.output("hex string"+ret.toString());
        return ret.toString();
    }
	
    public static String hex2string(String hex) {
    	StringBuilder ret = new StringBuilder();
    	
    	int i = 0;
    	while(i < hex.length()) {
    		char ch = hex.charAt(i);
    		if(ch < '8') {
//    			原来的
    			ret.append((char)(int)Integer.valueOf(hex.substring(i, i+2), 16));
    			i += 2;
//    			if(i+2<hex.length()){
//    				ret.append((char)(int)Integer.valueOf(hex.substring(i, i+2), 16));
//        			i += 2;
//    			}
    			
    		}
    		else if(ch == '8') {
//    			原来的
    			ret.append((char)(int)Integer.valueOf(hex.substring(i+1, i+5), 16));
    			i += 5;
//    			if(i+5<hex.length()){
//    			ret.append((char)(int)Integer.valueOf(hex.substring(i+1, i+5), 16));
//    			i += 5;
//    			}
    		}
    		else if(ch < 'f') {
    			ret.append((char)(int)(Integer.valueOf(hex.substring(i, i+4), 16)-20480));
    			i += 4;
    		}
    		else {
    			ret.append((char)(int)Integer.valueOf(hex.substring(i+1, i+5), 16));
    			i += 5;
    		}
    	}
    	
    	return ret.toString();
    }
	
    public static int char2int(char ch) {
		int ret = -1;

		if (ch >= 'a' && ch <= 'f')
			ret = ch - 87;
		else if (ch >= 'A' && ch <= 'F')
			ret = ch - 55;
		else if (ch >= '0' && ch <= '9')
			ret = ch - 48;

		return ret;
    }
    
    public static char int2char(int ival) {
    	char ret = (char)-1;
    	
    	if(ival >= 0 && ival <= 9)
    		ret = (char)(ival + 48);
    	else if(ival >= 10 && ival <= 16)
    		ret = (char)(ival+87);
    	
    	return ret;
    }
    
    public static String bytes2chars(byte[] bytes, int len) {
		if (len > bytes.length || (len % 2) != 0)
			return null;

		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < len; i += 2) {
			int temp = (bytes[i] * 256) & 0xFFFF;
			int ch = (bytes[i] * 256) | (bytes[i + 1] & 0xFF);
			ret.append((char) ch);
		}

		return ret.toString();
    }
    
	public static String long2hex(long lvalue) {
		StringBuilder ret = new StringBuilder();
		for(int i = 0; i < 16; ++i) {
			ret.append(hexChar[(int)(lvalue >> ((15-i)*4)) & 0x0f]);
		}
		return ret.toString();
	}
	
	public static long hex2long(String str) {
		long res = 0;
		for(int i = 0; i < 16; ++i) {
			res = res * 16 + char2int(str.charAt(i));
		}
		return res;
	}
}