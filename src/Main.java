import java.util.Scanner;

public class Main{
	public static Blockchain blockchain;
	
	public static void main(String[] args){
		new Thread(new Network()).start();
		
		blockchain = new Blockchain();
		Network.syncrhonize();
		
		Scanner in = new Scanner(System.in);
		String a;
		
		System.out.println();
		System.out.println("�� � ����� ����� ������ ������ \"stop\" ��� ������ �� ���������\n��� \"back\" ����� ��������� �����");
		System.out.println();
		do{
			System.out.println("�������� ��������:");
			System.out.println("��� ���� ����� �������� ���� ������� \"add\",");
			System.out.println("��� ���� ����� ��������� ���������� ��������� ������� \"check\",");
			System.out.println("��� ���� ����� ���������������� ��������� ������� \"synchronize\"");
			System.out.println("��� ���� ����� �������� �������� ������� \"god mode\"");
			
			a = in.nextLine();
			
		    switch(a){
				case "add": 
					System.out.println("������� ������ �����:");
					a = in.nextLine();
					blockchain.addBlock(a);
					break;
					
	    		case "check":
	    			System.out.println(blockchain.isBlockchainValid());
	    			break;
	    			
	    		case "synchronize":
	    			Network.syncrhonize();
	    			break;
	    			
	    		case "god mode":
	    			System.out.println("block 0:");
	    			System.out.println("   data: ");
	    			System.out.println("   previous hash: ");
	    			System.out.println("   current hash: " + blockchain.blockchain.get(0).currentHash);
	    			System.out.println("   nonce: " + blockchain.blockchain.get(0).nonce);
    				System.out.println("   complexity: " + blockchain.blockchain.get(0).complexity);
	    			
	    			for(int j = 1; j < blockchain.blockchain.size(); j++) {
	    				System.out.println("\nblock " + j + ":");
	    				System.out.println("   data: " + blockchain.blockchain.get(j).data);
	    				System.out.println("   previous hash: " + blockchain.blockchain.get(j).previousHash);
	    				System.out.println("   current hash: " + blockchain.blockchain.get(j).currentHash);
	    				System.out.println("   nonce: " + blockchain.blockchain.get(j).nonce);
	    				System.out.println("   complexity: " + blockchain.blockchain.get(j).complexity);
	    			}
	    			
	    			System.out.println();
	    			
	    			do {
	    				boolean bool = true;
	    				int i = -1;
		    			System.out.println("������� ����� ����� ������� ������ ���������������");
		    			a = in.nextLine();
		    			try {
		    				i = Integer.parseInt(a);
		    			}
		    			catch(Exception e) {
		    				bool = false;
		    			}
		    			
						if (bool) {
							do {
								System.out.println("�������� � ������� �������� ������� ������ ���������������\ndata, current hash, previous hash, nonce, complexity");
								a = in.nextLine();
								if(!a.equals("stop") && !a.equals("back")) {
					    			System.out.println("������� ����� ��������");
									String buf = in.nextLine();
									if(buf.equals("stop")) {
										a = buf;
									}
									else if(!buf.equals("back")){
										do {
											switch(a) {
												case "data":
													blockchain.blockchain.get(i).data = buf;
													a = "break";
													break;
													
												case "current hash":
													blockchain.blockchain.get(i).currentHash = buf;
													a = "break";
													break;
													
												case "previous hash":
													blockchain.blockchain.get(i).previousHash = buf;
													a = "break";
													break;
													
												case "nonce":
													blockchain.blockchain.get(i).nonce = buf;
													a = "break";
													break;
													
												case "complexity":
													blockchain.blockchain.get(i).nonce = buf;
													a = "break";
													break;
													
												case "back":
													break;
													
												case "stop":
													break;
												
												default:
									    			System.out.println("�� ����� ������������ ��������");
									    			a = "y";
													break;
											}
										} while(a.equals("y"));
									}
								}
							} while(!a.equals("stop") && !a.equals("back") && !a.equals("break"));
							if(a.equals("back")) {
								a = "";
							}
						}
	    			} while(!a.equals("stop") && !a.equals("back"));
	    			break;
	    			
	    		case "stop":
	    			break;
	    			
	    		default:
	    			System.out.println("�� ����� ������� �����������");
	    			break;
		    }
		} while(!a.equals("stop"));
		
	    in.close();
	}
}