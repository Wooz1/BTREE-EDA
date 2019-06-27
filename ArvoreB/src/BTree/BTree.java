package BTree;
import java.util.Stack;

public class BTree<K extends Comparable, V>
{
    public int balanceNodeL = 1;
    public int balanceNodeI = 2;
    private BTNode<K, V> root = null;
    private int  tam = 0;
    private BTNode<K, V> noDoMeio = null;
    private int indexNode = 0;
    private Stack<StackInfo> pilhaAux = new Stack<StackInfo>();

    public BTNode<K, V> getr() {
        return root;
    }

    public int size(){
        return tam;
    }

    private BTNode<K, V> criaNode() {
        BTNode<K, V> tmp;
        tmp = new BTNode<K, V>();
        tmp.nodeFolha = true;
        tmp.KeyNum = 0;
        return tmp;
    }

    public V get(K key) {
    	//Criando uma referencia auxiliar para o root.
        BTNode<K, V> node = root;
        //Criando nó para guardar o KeyVal de uma nó.
        BTKeyValue<K, V> chaveAtual;
        int i, numChaves;
        while (node != null) {
        	//Guardando a quantidade de chaves que aquele nó possui.
            numChaves = node.KeyNum;
            i = 0;
            //Guardando o nó atual que contem chave e valor; 	
            chaveAtual = node.Keys[i];
            //Se a chave for maior e o valor de "i" ainda andamos para a direita do vetor.(Valores maiores) 
            while ((i < numChaves) && (key.compareTo(chaveAtual.Key) > 0)) {
                ++i;
                if (i < numChaves) {
                    chaveAtual = node.Keys[i];
                }
                else {
                    --i;
                    break;
                }
            }
            //Se compararmos a chave e ela for igual, retornamos o valor dela.
            if ((i < numChaves) && (key.compareTo(chaveAtual.Key) == 0)) {
                return chaveAtual.Value;
            }
            //Se a chave que procuramos ainda for maior, vamos para o filho da direita. (Valores Maiores)
            if (key.compareTo(chaveAtual.Key) > 0) {
            	node = BTNode.getFilhoDirIndex(node, i);
            }
            //Se não, a chave é menor e vamos para o filho da esquerda. (Valores menores)
            else {
            	node = BTNode.getFilhoEsqIndex(node, i);
            }
        }
        //Se o node for nulo, então retornamos null.
        return null;
    }

    public BTree insert(K key, V value) {
    //Se o a raiz for nula então devemos criar o nosso primeiro node.
    	if (root == null) {
            root = criaNode();
        }
    	//Como agora existe um node aumentamos o tamanho.
        ++tam;
        //Verificamos se a quantidade de chaves naquele node é igual ao máximo permitido. 
        if (root.KeyNum == BTNode.MAX_KEY) {
            //Se entramos no if então quer dizer que o node está cheio e devemos fazer um split.
            BTNode<K, V> Node = criaNode();
            Node.nodeFolha = false;
            Node.filhos[0] = root;
            root = Node;
            //Chamamos a função de splitar.
            splitNode(root, 0, Node.filhos[0]);
        }
        //Agora que fizemos o slipt ou não (Quando não é necessário), vamos inserir no node.
        inserirChaveNode(root, key, value);
        return this;
    }
 
    private void inserirChaveNode(BTNode r, K key, V value) {
        int i;
        int chaveAtualNum = r.KeyNum;

        if (r.nodeFolha) {
            if (r.KeyNum == 0) {
                r.Keys[0] = new BTKeyValue<K, V>(key, value);
                ++(r.KeyNum);
                return;
            }

            for (i = 0; i < r.KeyNum; ++i) {
                if (key.compareTo(r.Keys[i].Key) == 0) {
                    r.Keys[i].Value = value;
                    --tam;
                    return;
                }
            }

            i = chaveAtualNum - 1;
            BTKeyValue<K, V> existingKeyVal = r.Keys[i];
            while ((i > -1) && (key.compareTo(existingKeyVal.Key) < 0)) {
                r.Keys[i + 1] = existingKeyVal;
                --i;
                if (i > -1) {
                    existingKeyVal = r.Keys[i];
                }
            }

            i = i + 1;
            r.Keys[i] = new BTKeyValue<K, V>(key, value);

            ++(r.KeyNum);
            return;
        }

        i = 0;
        int numChaves = r.KeyNum;
        BTKeyValue<K, V> chaveAtual = r.Keys[i];
        while ((i < numChaves) && (key.compareTo(chaveAtual.Key) > 0)) {
            ++i;
            if (i < numChaves) {
                chaveAtual = r.Keys[i];
            }
            else {
                --i;
                break;
            }
        }

        if ((i < numChaves) && (key.compareTo(chaveAtual.Key) == 0)) {
            chaveAtual.Value = value;
            --tam;
            return;
        }

        BTNode<K, V> Node;
        if (key.compareTo(chaveAtual.Key) > 0) {
            Node = BTNode.getFilhoDirIndex(r, i);
            i = i + 1;
        }
        else {
            if ((i - 1 >= 0) && (key.compareTo(r.Keys[i - 1].Key) > 0)) {
                Node = BTNode.getFilhoDirIndex(r, i - 1);
            }
            else {
                Node = BTNode.getFilhoEsqIndex(r, i);
            }
        }

        if (Node.KeyNum == BTNode.MAX_KEY) {
            splitNode(r, i, Node);
            inserirChaveNode(r, key, value);
            return;
        }

        inserirChaveNode(Node, key, value);
    }
    
    //Slipta um filho com o respectivo pai.
    private void splitNode(BTNode paiNode, int nodeID, BTNode Node) {
    	//Criamos um Node temporário.
        BTNode<K, V> tmp = criaNode();
        
        tmp.nodeFolha = Node.nodeFolha;

        tmp.KeyNum = BTNode.MIN_KEY;

        //Copiamos metade das chaves do Node atual para o novo.
        for (int i = 0; i < BTNode.MIN_KEY; ++i) {
            tmp.Keys[i] = Node.Keys[i + BTNode.ord];
            Node.Keys[i + BTNode.ord] = null;
        }

        //Se o node não for uma folha então ele possui filhos,assim
        //devemos copialos também para o novo node.
        if (!Node.nodeFolha) {
            for (int i = 0; i < BTNode.ord; ++i) {
                tmp.filhos[i] = Node.filhos[i + BTNode.ord];
                Node.filhos[i + BTNode.ord] = null;
            }
        }
        //Ao chegar aqui garantimos que o node possui apenas o valor míninmo de chaves,
        //isso por causa do for que só vai até o mínimo
        Node.KeyNum = BTNode.MIN_KEY;

        //Fazer uma "shift para direita" nos filhos daquela node, assim podemos inserir
        //o novo node como um novo filho.
        for (int i = paiNode.KeyNum; i > nodeID; --i) {
            paiNode.filhos[i + 1] = paiNode.filhos[i];
            paiNode.filhos[i] = null;
        }
        paiNode.filhos[nodeID + 1] = tmp;
        //Fazemos um "shift para direita" nas chaves do node pai para que aja espaço para
        //mover um node intermediario do node que foi splitado.
        for (int i = paiNode.KeyNum - 1; i >= nodeID; --i) {
            paiNode.Keys[i + 1] = paiNode.Keys[i];
            paiNode.Keys[i] = null;
        }
        
        paiNode.Keys[nodeID] = Node.Keys[BTNode.MIN_KEY];
        Node.Keys[BTNode.MIN_KEY] = null;
        ///Aumentamos o número de chaves que o nó pai tem.
        ++(paiNode.KeyNum);
    }


    //
    //Encontra o node predecessor de um node específico. 
    private BTNode<K, V> Predecessor(BTNode<K, V> Node, int nodeID) {
    	//
        if (Node.nodeFolha) {
            return Node;
        }

        BTNode<K, V> predecessorNode;
        //Se for menor que 1 eu tenho que buscar no filho esquedo
        if (nodeID > -1) {
            predecessorNode = BTNode.getFilhoEsqIndex(Node, nodeID);
            if (predecessorNode != null) {
                noDoMeio = Node;
                indexNode = nodeID;
                Node = Predecessor(predecessorNode, -1);
            }

            return Node;
        }
        //Se for diferente de -1 eu tenho que buscar no filho direito.
        predecessorNode = BTNode.getFilhoDirIndex(Node, Node.KeyNum - 1);
        if (predecessorNode != null) {
            noDoMeio = Node;
            indexNode = Node.KeyNum;
            Node = predecessorNode(predecessorNode, -1);
        }

        return Node;
    }
    //Encontrar o node predecessor de um node específico.
    private BTNode<K, V> predecessorNode(BTNode<K, V> Node, int keyIdx) {
        BTNode<K, V> predecessorNode;
        BTNode<K, V> originalNode = Node;
        if (keyIdx > -1) {
            predecessorNode = BTNode.getFilhoEsqIndex(Node, keyIdx);
            if (predecessorNode != null) {
                Node = predecessorNode(predecessorNode, -1);
                rebalancearNode(originalNode, predecessorNode, keyIdx, balanceNodeL);
            }

            return Node;
        }

        predecessorNode = BTNode.getFilhoDirIndex(Node, Node.KeyNum - 1);
        if (predecessorNode != null) {
            Node = predecessorNode(predecessorNode, -1);
            rebalancearNode(originalNode, predecessorNode, keyIdx, balanceNodeL);
        }

        return Node;
    }
    
    // Função para fazer uma rotação a esquerda
    private void rotacaoEsq(BTNode<K, V> Node, int nodeID, BTNode<K, V> paiNode, BTNode<K, V> irmaoDir) {
        int chavePaiID = nodeID;
        //Movemos a chave pai e o filho em questão para o no que está precisando.
        Node.Keys[Node.KeyNum] = paiNode.Keys[chavePaiID];
        Node.filhos[Node.KeyNum + 1] = irmaoDir.filhos[0];
        ++(Node.KeyNum);
        
        //Movemos a chave mais a esquerda do irmão direito e do filho em questão para o
        //node pai.
        paiNode.Keys[chavePaiID] = irmaoDir.Keys[0];
        --(irmaoDir.KeyNum);
        //Fazemos um "shift left de todas as chaves e filhos do irmão direito para a esquerda.
        for (int i = 0; i < irmaoDir.KeyNum; ++i) {
            irmaoDir.Keys[i] = irmaoDir.Keys[i + 1];
            irmaoDir.filhos[i] = irmaoDir.filhos[i + 1];
        }
        irmaoDir.filhos[irmaoDir.KeyNum] = irmaoDir.filhos[irmaoDir.KeyNum + 1];
        irmaoDir.filhos[irmaoDir.KeyNum + 1] = null;
    }

    // Função para fazer uma rotação a esquerda
    private void rotacaoDir(BTNode<K, V> Node, int nodeID, BTNode<K, V> paiNode, BTNode<K, V> irmaoEsqNode) {
        int chavePaiID = nodeID;

        //Assim o nó terá um espaço para inserir na esquerda.
        Node.filhos[Node.KeyNum + 1] = Node.filhos[Node.KeyNum];
        for (int i = Node.KeyNum - 1; i >= 0; --i) {
            Node.Keys[i + 1] = Node.Keys[i];
            Node.filhos[i + 1] = Node.filhos[i];
        }
        
        //Movemos o pai e o filho em questão para no node que está precisando.
        Node.Keys[0] = paiNode.Keys[chavePaiID];
        Node.filhos[0] = irmaoEsqNode.filhos[irmaoEsqNode.KeyNum];
        ++(Node.KeyNum);

        //Movemos a chave mais a esquerda do irmão direito e do filho necessário
        //para o node pai.
        paiNode.Keys[chavePaiID] = irmaoEsqNode.Keys[irmaoEsqNode.KeyNum - 1];
        irmaoEsqNode.filhos[irmaoEsqNode.KeyNum] = null;
        --(irmaoEsqNode.KeyNum);
    }


    //Função para fazer um merge com o irmão da esquerda.
    //Retorna true se for necessário ir além
    //Retorna falso se não.

    private boolean mergeEsqIrmao(BTNode<K, V> Node, int nodeID, BTNode<K, V> paiNode, BTNode<K, V> irmaoEsqNode) {
        if (nodeID == paiNode.KeyNum) {
        	//Para o caso de que o index do node seja o mais a direita
            nodeID = nodeID - 1;
        }
        //Aqui precisamos determinar o index do pai node a partir do index do filho.
        if (nodeID > 0) {
            if (irmaoEsqNode.Keys[irmaoEsqNode.KeyNum - 1].Key.compareTo(paiNode.Keys[nodeID - 1].Key) < 0) {
                nodeID = nodeID - 1;
            }
        }
        //Copiamos a chave pai para o node (da esquerda).
        irmaoEsqNode.Keys[irmaoEsqNode.KeyNum] = paiNode.Keys[nodeID];
        ++(irmaoEsqNode.KeyNum);
        //Copiamos as chaves e filhos do node para o irmão esquerdo.
        for (int i = 0; i < Node.KeyNum; ++i) {
            irmaoEsqNode.Keys[irmaoEsqNode.KeyNum + i] = Node.Keys[i];
            irmaoEsqNode.filhos[irmaoEsqNode.KeyNum + i] = Node.filhos[i];
            Node.Keys[i] = null;
        }
        irmaoEsqNode.KeyNum += Node.KeyNum;
        irmaoEsqNode.filhos[irmaoEsqNode.KeyNum] = Node.filhos[Node.KeyNum];
        Node.KeyNum = 0; //O node não deve possuir mais chaves.

        //Fazemos um shift com as chaves relevants e filhos do node pai para a esquerda
        int i;
        for (i = nodeID; i < paiNode.KeyNum - 1; ++i) {
            paiNode.Keys[i] = paiNode.Keys[i + 1];
            paiNode.filhos[i + 1] = paiNode.filhos[i + 2];
        }
        paiNode.Keys[i] = null;
        paiNode.filhos[paiNode.KeyNum] = null;
        --(paiNode.KeyNum);

        //Para termos certeza de que o pai aponte para o filho correto a esquerda
        //após o merge.
        paiNode.filhos[nodeID] = irmaoEsqNode;

        //Se entrar no if não será mais necessário continuar
        if ((paiNode == root) && (paiNode.KeyNum == 0)) {
        	root = irmaoEsqNode;
            return false;
        }

        return true;
    }


    //Função para fazer um merge com o irmão da direita.
    //Retorna true se for necessário ir além
    //Retorna falso se não.
    private boolean mergeDirIrmao(BTNode<K, V> Node, int nodeID, BTNode<K, V> paiNode, BTNode<K, V> irmaoDir) {
        //Copia a chave pai para o o mais o espaço mais a direita.
        Node.Keys[Node.KeyNum] = paiNode.Keys[nodeID];
        ++(Node.KeyNum);
        //Copia as chaves e filho do irmaõ direito para o node.
        for (int i = 0; i < irmaoDir.KeyNum; ++i) {
            Node.Keys[Node.KeyNum + i] = irmaoDir.Keys[i];
            Node.filhos[Node.KeyNum + i] = irmaoDir.filhos[i];
        }
        Node.KeyNum += irmaoDir.KeyNum;
        Node.filhos[Node.KeyNum] = irmaoDir.filhos[irmaoDir.KeyNum];
        irmaoDir.KeyNum = 0;  // Abandon the sibling node
        //Move todas as chaves e filhos relevantes do node pai para a esquerda.
        int i;
        for (i = nodeID; i < paiNode.KeyNum - 1; ++i) {
            paiNode.Keys[i] = paiNode.Keys[i + 1];
            paiNode.filhos[i + 1] = paiNode.filhos[i + 2];
        }
        paiNode.Keys[i] = null;
        paiNode.filhos[paiNode.KeyNum] = null;
        --(paiNode.KeyNum);

        //Para termos certeza de que o pai aponte para o filho correto a esquerda
        //após o merge.
        paiNode.filhos[nodeID] = Node;

        if ((paiNode == root) && (paiNode.KeyNum == 0)) {
            //Se entrar no if então não precisamos ir além disso.
            root = Node;
            return false;
        }

        return true;
    }


    //Função para pegar uma chave específica de um node.
    //Retorna o index da chave se for encontrada
    //Senão retorna -1
    private int getKey(BTNode<K, V> Node, K key) {
        for (int i = 0; i < Node.KeyNum; ++i) {
            if (key.compareTo(Node.Keys[i].Key) == 0) {
                return i;
            }
            else if (key.compareTo(Node.Keys[i].Key) < 0) {
                return -1;
            }
        }

        return -1;
    }

    public V delete(K key) {
        noDoMeio = null;
        BTKeyValue<K, V> keyVal = deleteKey(null, root, key, 0);
        if (keyVal == null) {
            return null;
        }
        --tam;
        return keyVal.Value;
    }

    private BTKeyValue<K, V> deleteKey(BTNode<K, V> paiNode, BTNode<K, V> Node, K key, int nodeID) {
        int i;
        int idNode;
        BTKeyValue<K, V> tmp;

        if (Node == null) {
            return null;
        }

        if (Node.nodeFolha) {
            idNode = getKey(Node, key);
            if (idNode < 0) {
                return null;
            }

            tmp = Node.Keys[idNode];

            if ((Node.KeyNum > BTNode.MIN_KEY) || (paiNode == null)) {
                for (i = idNode; i < Node.KeyNum - 1; ++i) {
                    Node.Keys[i] = Node.Keys[i + 1];
                }
                Node.Keys[i] = null;
                --(Node.KeyNum);

                if (Node.KeyNum == 0) {
                    root = null;
                }

                return tmp;
            }

            BTNode<K, V> irmaoDir;
            BTNode<K, V> leftSibling = BTNode.irmaoEsqIndex(paiNode, nodeID);
            if ((leftSibling != null) && (leftSibling.KeyNum > BTNode.MIN_KEY)) {
                moveIrmaoEsq(Node, nodeID, idNode, paiNode, leftSibling);
            }
            else {
                irmaoDir = BTNode.irmaoDirIndex(paiNode, nodeID);
                if ((irmaoDir != null) && (irmaoDir.KeyNum > BTNode.MIN_KEY)) {
                    moveIrmaoDir(Node, nodeID, idNode, paiNode, irmaoDir);
                }
                else {
                    boolean isRebalanceNeeded = false;
                    boolean bStatus;
                    if (leftSibling != null) {
                        bStatus = mergeIrmaoRemove(Node, nodeID, idNode, paiNode, leftSibling, false);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        }
                        else if (paiNode.KeyNum < BTNode.MIN_KEY) {
                            isRebalanceNeeded = true;
                        }
                    }
                    else {
                        bStatus = mergeIrmaoRemove(Node, nodeID, idNode, paiNode, irmaoDir, true);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        }
                        else if (paiNode.KeyNum < BTNode.MIN_KEY) {
                            isRebalanceNeeded = true;
                        }
                    }

                    if (isRebalanceNeeded && (root != null)) {
                        rebalanceTree(root, paiNode, paiNode.Keys[0].Key);
                    }
                }
            }

            return tmp; 
        }

               
        idNode = getKey(Node, key);
        if (idNode >= 0) {

            noDoMeio = Node;
            indexNode = idNode;
            BTNode<K, V> predecessorNode =  Predecessor(Node, idNode);
            BTKeyValue<K, V> predecessorKey = predecessorNode.Keys[predecessorNode.KeyNum - 1];

            BTKeyValue<K, V> deletedKey = Node.Keys[idNode];
            Node.Keys[idNode] = predecessorKey;
            predecessorNode.Keys[predecessorNode.KeyNum - 1] = deletedKey;

            return deleteKey(noDoMeio, predecessorNode, deletedKey.Key, indexNode);
        }

        i = 0;
        BTKeyValue<K, V> chaveAtual = Node.Keys[0];
        while ((i < Node.KeyNum) && (key.compareTo(chaveAtual.Key) > 0)) {
            ++i;
            if (i < Node.KeyNum) {
                chaveAtual = Node.Keys[i];
            }
            else {
                --i;
                break;
            }
        }

        BTNode<K, V> childNode;
        if (key.compareTo(chaveAtual.Key) > 0) {
            childNode = BTNode.getFilhoDirIndex(Node, i);
            if (childNode.Keys[0].Key.compareTo(Node.Keys[Node.KeyNum - 1].Key) > 0) {
                i = i + 1;
            }
        }
        else {
            childNode = BTNode.getFilhoEsqIndex(Node, i);
        }

        return deleteKey(Node, childNode, key, i);
    }



    private void moveIrmaoDir(BTNode<K, V> Node, int nodeID, int keyIdx, BTNode<K, V> paiNode,BTNode<K, V> irmaoDir)
    {
        for (int i = keyIdx; i < Node.KeyNum - 1; ++i) {
            Node.Keys[i] = Node.Keys[i + 1];
        }

        Node.Keys[Node.KeyNum - 1] = paiNode.Keys[nodeID];
        paiNode.Keys[nodeID] = irmaoDir.Keys[0];

        for (int i = 0; i < irmaoDir.KeyNum - 1; ++i) {
            irmaoDir.Keys[i] = irmaoDir.Keys[i + 1];
        }

        --(irmaoDir.KeyNum);
    }



    private void moveIrmaoEsq(BTNode<K, V> Node, int nodeID, int keyIdx,BTNode<K, V> paiNode, BTNode<K, V> irmaoEsqNode) {
        nodeID = nodeID - 1;

        for (int i = keyIdx; i > 0; --i) {
            Node.Keys[i] = Node.Keys[i - 1];
        }

        Node.Keys[0] = paiNode.Keys[nodeID];
        paiNode.Keys[nodeID] = irmaoEsqNode.Keys[irmaoEsqNode.KeyNum - 1];
        --(irmaoEsqNode.KeyNum);
    }


    private boolean mergeIrmaoRemove(BTNode<K, V> Node,int nodeID, int keyIdx, BTNode<K, V> paiNode, BTNode<K, V> irmaoNode, boolean irmaoDir)
    {
        int i;

        if (nodeID == paiNode.KeyNum) {
            nodeID = nodeID - 1;
        }

        if (irmaoDir) {
            for (i = keyIdx; i < Node.KeyNum - 1; ++i) {
                Node.Keys[i] = Node.Keys[i + 1];
            }
            Node.Keys[i] = paiNode.Keys[nodeID];
        }
        else {
            if (nodeID > 0) {
                if (irmaoNode.Keys[irmaoNode.KeyNum - 1].Key.compareTo(paiNode.Keys[nodeID - 1].Key) < 0) {
                    nodeID = nodeID - 1;
                }
            }

            irmaoNode.Keys[irmaoNode.KeyNum] = paiNode.Keys[nodeID];
            ++(irmaoNode.KeyNum);

            for (i = keyIdx; i < Node.KeyNum - 1; ++i) {
                Node.Keys[i] = Node.Keys[i + 1];
            }
            Node.Keys[i] = null;
            --(Node.KeyNum);
        }

        if (irmaoDir) {
            for (i = 0; i < irmaoNode.KeyNum; ++i) {
                Node.Keys[Node.KeyNum + i] = irmaoNode.Keys[i];
                irmaoNode.Keys[i] = null;
            }
            Node.KeyNum += irmaoNode.KeyNum;
        }
        else {
            for (i = 0; i < Node.KeyNum; ++i) {
                irmaoNode.Keys[irmaoNode.KeyNum + i] = Node.Keys[i];
                Node.Keys[i] = null;
            }
            irmaoNode.KeyNum += Node.KeyNum;
            Node.Keys[Node.KeyNum] = null;
        }

        for (i = nodeID; i < paiNode.KeyNum - 1; ++i) {
            paiNode.Keys[i] = paiNode.Keys[i + 1];
            paiNode.filhos[i + 1] = paiNode.filhos[i + 2];
        }
        paiNode.Keys[i] = null;
        paiNode.filhos[paiNode.KeyNum] = null;
        --(paiNode.KeyNum);

        if (irmaoDir) {
            paiNode.filhos[nodeID] = Node;
        }
        else {
            paiNode.filhos[nodeID] = irmaoNode;
        }

        if ((root == paiNode) && (root.KeyNum == 0)) {
            root = paiNode.filhos[nodeID];
            root.nodeFolha = true;
            return false;  
        }

        return true;
    }


    private boolean rebalancearNode(BTNode<K, V> paiNode, BTNode<K, V> Node, int nodeID, int balanceType) {
        if (balanceType == balanceNodeL) {
            if ((Node == null) || (Node == root)) {
                return false;
            }
        }
        else if (balanceType == balanceNodeI) {
            if (paiNode == null) {
                return false;
            }
        }

        if (Node.KeyNum >= BTNode.MIN_KEY) {
            return false;
        }

        BTNode<K, V> irmaoDir;
        BTNode<K, V> irmaoEsqNode = BTNode.irmaoEsqIndex(paiNode, nodeID);
        if ((irmaoEsqNode != null) && (irmaoEsqNode.KeyNum > BTNode.MIN_KEY)) {
            rotacaoDir(Node, nodeID, paiNode, irmaoEsqNode);
        }
        else {
            irmaoDir = BTNode.irmaoDirIndex(paiNode, nodeID);
            if ((irmaoDir != null) && (irmaoDir.KeyNum > BTNode.MIN_KEY)) {
                rotacaoEsq(Node, nodeID, paiNode, irmaoDir);
            }
            else {
                boolean bStatus;
                if (irmaoEsqNode != null) {
                    bStatus = mergeEsqIrmao(Node, nodeID, paiNode, irmaoEsqNode);
                }
                else {
                    bStatus = mergeDirIrmao(Node, nodeID, paiNode, irmaoDir);
                }

                if (!bStatus) {
                    return false;
                }
            }
        }

        return true;
    }



    private void rebalanceTree(BTNode<K, V> upperNode, BTNode<K, V> lowerNode, K key) {
        pilhaAux.clear();
        pilhaAux.add(new StackInfo(null, upperNode, 0));

        BTNode<K, V> paiNode, childNode;
        BTKeyValue<K, V> chaveAtual;
        int i;
        paiNode = upperNode;
        while ((paiNode != lowerNode) && !paiNode.nodeFolha) {
            chaveAtual = paiNode.Keys[0];
            i = 0;
            while ((i < paiNode.KeyNum) && (key.compareTo(chaveAtual.Key) > 0)) {
                ++i;
                if (i < paiNode.KeyNum) {
                    chaveAtual = paiNode.Keys[i];
                }
                else {
                    --i;
                    break;
                }
            }

            if (key.compareTo(chaveAtual.Key) > 0) {
                childNode = BTNode.getFilhoDirIndex(paiNode, i);
                if (childNode.Keys[0].Key.compareTo(paiNode.Keys[paiNode.KeyNum - 1].Key) > 0) {
                    i = i + 1;
                }
            }
            else {
                childNode = BTNode.getFilhoEsqIndex(paiNode, i);
            }

            if (childNode == null) {
                break;
            }

            if (key.compareTo(chaveAtual.Key) == 0) {
                break;
            }

            pilhaAux.add(new StackInfo(paiNode, childNode, i));
            paiNode = childNode;
        }

        boolean bStatus;
        StackInfo stackInfo;
        while (!pilhaAux.isEmpty()) {
            stackInfo = pilhaAux.pop();
            if ((stackInfo != null) && !stackInfo.Node.nodeFolha) {
                bStatus = rebalancearNode(stackInfo.nodePai,stackInfo.Node,stackInfo.indexNode,balanceNodeI);
                if (!bStatus) {
                    break;
                }
            }
        }
    }

    public class StackInfo {
        public BTNode<K, V> nodePai = null;
        public BTNode<K, V> Node = null;
        public int indexNode = -1;

        public StackInfo(BTNode<K, V> parent, BTNode<K, V> node, int nodeID) {
            nodePai = parent;
            this.Node = node;
            indexNode = nodeID;
        }
    }
}
