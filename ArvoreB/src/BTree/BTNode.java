package BTree;

public class BTNode<K extends Comparable, V>
{
    public final static int ord =  7;
    public final static int MIN_KEY  =   ord/2;
    public final static int MAX_KEY  = (ord *2) - 1;

    public boolean nodeFolha;
    public int KeyNum;
    //Vetor de chaves ;
    protected BTKeyValue<K, V> Keys[];
    protected BTNode filhos[];


    public BTNode() {
        nodeFolha = true;
        KeyNum = 0;
        Keys = new BTKeyValue[MAX_KEY];
        filhos = new BTNode[MAX_KEY + 1];
    }


    protected static BTNode getFilhoIndex(BTNode Node, int keyIdx, int direcao) {
        if (Node.nodeFolha) {
            return null;
        }
        keyIdx += direcao;
        if ((keyIdx < 0) || (keyIdx > Node.KeyNum)) {
            return null;
        }
        return Node.filhos[keyIdx];
    }


    protected static BTNode getFilhoEsqIndex(BTNode Node, int keyIdx) {
        return getFilhoIndex(Node, keyIdx, 0);
    }


    protected static BTNode getFilhoDirIndex(BTNode Node, int keyIdx) {
        return getFilhoIndex(Node, keyIdx, 1);
    }


    protected static BTNode irmaoEsqIndex(BTNode NodePai, int keyIdx) {
        return getFilhoIndex(NodePai, keyIdx, -1);
    }


    protected static BTNode irmaoDirIndex(BTNode NodePai, int keyIdx) {
        return getFilhoIndex(NodePai, keyIdx, 1);
    }
}
