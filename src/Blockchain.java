import java.util.*;
import java.io.*;
import java.time.LocalTime;


public class Blockchain {
	public List<Block> blockchain;
	public static String complexity = "00000";
	private static int updateNum = 30;
	private static int blockMiningTime = 120;
	private static int miningTime = updateNum * blockMiningTime;
	private static long prevUpdateTime;
	private static long updateTime;
	private static int realMiningTime;
	
	private static void findComplexity(int realMiningTime) {
		int k = Math.round(complexity.length()*miningTime/realMiningTime);
		complexity = "";
		for(int i=0; i < k; i++) {
			complexity += "0";
		}
	}
	
	public static Blockchain setBlockchain(Blockchain blockchain) {
		try{
			FileWriter writer = new FileWriter("blockchain.txt", false);
			writer.write("block 0:\n");
			writer.close();
			
			writer = new FileWriter("blockchain.txt", true);
			writer.write("   data: " + blockchain.blockchain.get(0).data + "\n");
			writer.write("   previous hash: " + blockchain.blockchain.get(0).previousHash + "\n");
			writer.write("   current hash: " + blockchain.blockchain.get(0).currentHash + "\n");
			writer.write("   nonce: " + blockchain.blockchain.get(0).nonce + "\n");
			writer.write("   complexity: " + blockchain.blockchain.get(0).complexity);
			
			for(int num = 1; num < blockchain.blockchain.size(); num++) {
				writer.write("\n\n");
				writer.write("block " + num + ":\n");
				writer.write("   data: " + blockchain.blockchain.get(num).data + "\n");
				writer.write("   previous hash: " + blockchain.blockchain.get(num).previousHash + "\n");
				writer.write("   current hash: " + blockchain.blockchain.get(num).currentHash + "\n");
				writer.write("   nonce: " + blockchain.blockchain.get(num).nonce + "\n");
				writer.write("   complexity: " + blockchain.blockchain.get(num).complexity);
			}
			writer.close();
		}
		catch(Exception e){
			
		}
		return blockchain;
	}
	
	public Blockchain() {
		this.blockchain = new ArrayList<Block>();
		
		try{
			Scanner reader = new Scanner(new FileReader("blockchain.txt"));
			
			if(!reader.hasNextLine()) {
				Block block0 = new Block("", "", complexity);
				
				this.blockchain.add(block0);
				
				updateTime = (LocalTime.now().getHour()*60 + LocalTime.now().getMinute())*60 + LocalTime.now().getSecond();
				
				block0.writeBlock(0);
			}
			else {
				reader.nextLine();
				reader.next();
				String data = reader.nextLine().substring(1);
				reader.next();
				reader.next();
				String previousHash = reader.nextLine().substring(1);
				reader.next();
				reader.next();
				String currentHash = reader.nextLine().substring(1);
				reader.next();
				String nonce = reader.nextLine().substring(1);
				reader.next();
				String complexity = reader.nextLine().substring(1);
				
				Block bufBlock = new Block(data, previousHash, currentHash, nonce, complexity);
				
				this.blockchain.add(bufBlock);
				
				while(reader.hasNextLine()) {
					reader.nextLine();
					reader.nextLine();
					reader.next();
					data = reader.nextLine().substring(1);
					reader.next();
					reader.next();
					previousHash = reader.nextLine().substring(1);
					reader.next();
					reader.next();
					currentHash = reader.nextLine().substring(1);
					reader.next();
					nonce = reader.nextLine().substring(1);
					reader.next();
					complexity = reader.nextLine().substring(1);
					
					bufBlock = new Block(data, previousHash, currentHash, nonce, complexity);
					
					this.blockchain.add(bufBlock);
				}
			}
			
			reader.close();
		}
		catch(IOException ex){ 
			System.out.println(ex.getMessage());
		}
	}
	
	public void addBlock(String a) {		
		Block block = new Block(a, this.blockchain.get(this.blockchain.size()-1).currentHash, complexity);
		this.blockchain.add(block);
		
		if ((this.blockchain.size() - 1) % updateNum == 0) {
			prevUpdateTime = updateTime;
			updateTime = (LocalTime.now().getHour()*60 + LocalTime.now().getMinute())*60 + LocalTime.now().getSecond();
			realMiningTime = (int)(updateTime - prevUpdateTime);
			findComplexity(realMiningTime);
		}
		
		System.out.println("Блок успешно создан\nего хеш: " + this.blockchain.get(this.blockchain.size()-1).currentHash);
		System.out.println();
		
		block.writeBlock(this.blockchain.size()-1);
	}
	
	public boolean isBlockchainValid() {
		if (this.blockchain.size() > 1) {
			for(int i = 1; i < this.blockchain.size(); i++) {
				Block currentBlock = this.blockchain.get(i-1);
				Block nextBlock = this.blockchain.get(i);
				//if (!Hash.sha256(nextBlock.data + nextBlock.complexity + nextBlock.nonce + currentBlock.currentHash).equals(nextBlock.currentHash))				
				if (!nextBlock.previousHash.equals(currentBlock.currentHash) ||
				!Hash.sha256(nextBlock.data + nextBlock.complexity + nextBlock.nonce + nextBlock.previousHash).equals(nextBlock.currentHash)) {
					return false;
				}
			}
		}
		return true;
	}
} 