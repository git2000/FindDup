
public class IndFile {
	private String filename;
	private String md5;
	private long size;

	public IndFile(String f, String m, long s) {
		filename = f;
		md5 = m;
		size = s;
	}
	
	public void Print() {
		System.out.println("filename="+ filename+ "; md5=" + md5 + "; size="+size);
	}

	public String toString() {
		String a = "filename="+ filename+ "; md5=" + md5 + "; size="+size;
		return a;
	}
	public String toString(String loc) {
		String a = "filename="+ loc+"\\"+filename+ "; md5=" + md5 + "; size="+size;
		return a;
	}
	
	public String getFilename () {
		return filename;
	}
	public String getMD5() {
		return md5;
	}
	public long getSize() {
		return size;
	}
}
