package BTree;

public class BTreeTest {

	public static void main(String[] args) {

		BTree<Integer, String> tree =  new BTree<Integer, String>();
		
		tree.insert(204, "Miguel");
		tree.insert(215, "Davi");
		tree.insert(671, "Arthur");
		tree.insert(5641, "Pedro");
		tree.insert(611, "Gabriel");
		tree.insert(8361, "Bernardo");
		tree.insert(561, "Lucas	");
		tree.insert(7871, "Matheus");
		tree.insert(821, "Rafael");
		tree.insert(6121, "Heitor");
		tree.insert(621, "Enzo");
		tree.insert(721, "Guilherme");
		
		tree.insert(527, "Nicolas");
		tree.insert(672, "Maria Luiza");
		tree.insert(3123, "Lara");
		tree.insert(783, "Mariana");
		tree.insert(7891, "Nicole");
		tree.insert(8361, "Rafaela");
		tree.insert(6787, "Heloísa	");
		tree.insert(231, "Daniel");
		tree.insert(1289, "Isabelly");
		tree.insert(6121, "Heitor");
		tree.insert(473, "Cauã");
		tree.insert(1747, "Lucca");
		
		tree.insert(1724, "Rebeca");
		tree.insert(2085, "Maria Luiza");
		tree.insert(540, "Bryan");
		tree.insert(320, "Joaquim");
		tree.insert(5, "Francisco");
		tree.insert(78, "Maria ");
		tree.insert(456, "Gabrielly	");
		tree.insert(789783, "Otávio");
		tree.insert(1237, "Thiago");
		tree.insert(6121, "Sarah");
		tree.insert(232, "Lavínia");
		tree.insert(426, "Melissa");
		
		System.out.println("\nTESTANDO GET\n");
		System.out.println(tree.get(789783));
		System.out.println(tree.get(231));
		System.out.println(tree.get(783));
		System.out.println(tree.get(473));
		System.out.println(tree.get(204));


		
		System.out.println(tree.delete(789783));
		System.out.println(tree.delete(204));
		
		System.out.println("\nAPÓS DELETAR\n\n");
		System.out.println(tree.get(789783));
		System.out.println(tree.get(231));
		System.out.println(tree.get(783));
		System.out.println(tree.get(473));
		System.out.println(tree.get(204));
		
	}

}
