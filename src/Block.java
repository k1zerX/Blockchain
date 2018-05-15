import java.io.FileWriter;

public class Block{

	public String data;
	public String previousHash;
	public String currentHash;
	public String nonce;
	public String complexity;
	
	
	
	public Block(String data, String previousHash, String complexity){
		this.data = data;
		this.previousHash = previousHash;
		this.complexity = complexity;
		this.currentHash = findHash(data, complexity, previousHash)[0];
		this.nonce = findHash(data, complexity, previousHash)[1];
	}
	
	public Block(String data, String previousHash, String currentHash, String nonce, String complexity){
		this.data = data;
		this.previousHash = previousHash;
		this.currentHash = currentHash;
		this.nonce = nonce;
		this.complexity = complexity;
	}

	private String[] findHash(String data, String complexity, String previousHash) {
		int nonce = 0;
		String hash;
		do{
			hash = Hash.sha256(data + complexity + nonce + previousHash);
			nonce++;
		} while(!hash.matches(complexity + ".*"));
		String[] strings = {hash, Integer.toString(nonce-1)};
		return strings;
	}
	
	public void writeBlock(int num) {
		try{
			FileWriter writer = new FileWriter("blockchain.txt", true);
			if(num != 0) {
				writer.write("\n\n");
			}
			writer.write("block " + num + ":\n");
			writer.write("   data: " + this.data + "\n");
			writer.write("   previous hash: " + this.previousHash + "\n");
			writer.write("   current hash: " + this.currentHash + "\n");
			writer.write("   nonce: " + this.nonce + "\n");
			writer.write("   complexity: " + this.complexity);
			writer.close();
		}
		catch(Exception e){
			
		}
	}
}