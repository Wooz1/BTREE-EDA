package BTree;
//Classe onde onde vai ficar as informações de chava e valor de um nó da arvore B.
public class BTKeyValue<K extends Comparable, V>
{
    protected K Key;
    protected V Value;

    public BTKeyValue(K key, V value) {
        this.Key = key;
        this.Value = value;
    }
}
