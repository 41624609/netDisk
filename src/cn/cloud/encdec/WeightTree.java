package cn.cloud.encdec;

public class WeightTree {
	private int[] nodes;
	private int nBase;
	private int nNodes;
	
	public WeightTree(int weightNums) {
		nodes = create(weightNums);
	}
	/*
	 * creat()����Ȩ������������������ret[]��
	 * weightNumsΪ��ֵ�ַ�������
	 * heightΪ����
	 * nNodesΪ���нڵ�����
	 * nBase ���з�Ҷ�ӽڵ���
	 */
	public int treeSize(){
		return nodes.length*4;
	}
	private int[] create(int weightNums) {
		int height = (int)Math.ceil(Math.log10(weightNums) / Math.log10(2))+1;
		nNodes = (int)Math.pow(2, height-1) + weightNums - 1;
		nBase = nNodes - weightNums;
		int[] ret = new int[nNodes];
		for(int i = 0; i < nNodes; ++i) {
			if(i < nBase)
				ret[i] = 0;
			else
				ret[i] = 1;
		}
		
		for(int h = height-1; h > 0; --h) {
			int start = (int)Math.pow(2, h)-1;
			int end = (int)Math.pow(2, h+1)-1;
			if(end > nNodes)
				end = nNodes;
			for(int j = start; j < end; j += 2) {
				if(j+1 < end)
					ret[j/2] = ret[j] + ret[j+1];
				else
					ret[j/2] = ret[j];
			}
		}
		
		return ret;
	}
	
	public void print(int node) {
		if(node < 0 || node >= nodes.length)
			return;
//		int bdr = 0, step = 2;
//		for(int i = 0; i < nodes.length; ++i) {
//			System.out.print(nodes[i]+" ");
//			if(i == bdr) {
//				System.out.println();
//				bdr += step;
//				step *= 2;
//			}
//		}
//		System.out.println();	
		System.out.print(get(node) + " ");
		print(leftChild(node));
		print(rightChild(node));
	}
	
	public int root() {
		return 0;
	}
	
	private int childNum(int m) {
		int ret = 0;
		if(get(m*2+1) > 0)
			++ret;
		if(get(m*2+2) > 0)
			++ret;
		
		return ret;
	}
		
	public int get(int index) {
		if(index >= nodes.length)
			return -1;
		return nodes[index];
	}
	
	public boolean isLeaf(int m) {
		return (childNum(m) == 0);
	}
	
	public void update(int m) {
		int n = nBase + m;//�ҵ���ֵ�ַ���Ӧ��Ҷ�ӽڵ�
		while(true) {//��Ҷ�ӽڵ㿪ʼ��ѭ������Ȩֵ����Ѱ�Ҹ��ڵ�
			++nodes[n];
			if(n == root())//ֱ�����ڵ�Ȩֵ�������Ϊֹ
				break;
			n = parent(n);
		}
	}
	
	public int parent(int child) {
		return (child-1) / 2;
	}
	
	public int leftChild(int parent) {
		if(isLeaf(parent)) 
			return -1;
		
		int child = parent*2 + 1;
		while(childNum(child) == 1)
			child = leftChild(child);
		
		return child;
	}
	
	public int rightChild(int parent) {
		if(childNum(parent) < 2)
			return -1;
		
		int child = parent*2 + 2;
		if(childNum(child) == 1)
			return leftChild(child);
		else
			return child;
	}
	
	public static void main(String[] args) {
		WeightTree wt = new WeightTree(8);
		System.out.print("Ȩ����:");
		wt.print(wt.root());
		System.out.println();
		System.out.println("Ȩ������С��"+wt.treeSize()/4);
		
//		wt.update(8);
//		wt.print(wt.root());
	}
}