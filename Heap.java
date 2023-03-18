import java.io.*;

public class Heap {
    public static void ordenar(){
        try{
            //BANCO DE DADOS
            RandomAccessFile ygo = new RandomAccessFile("./tmp/ygo", "rw");
            //ARQUIVOS TEMPORÁRIOS
            RandomAccessFile temp1 = new RandomAccessFile("./tmp/temp1", "rw");
            RandomAccessFile temp2 = new RandomAccessFile("./tmp/temp2", "rw");
            RandomAccessFile temp3 = new RandomAccessFile("./tmp/temp3", "rw");
            RandomAccessFile temp4 = new RandomAccessFile("./tmp/temp4", "rw");

            int cardNum = divisao(4, ygo, temp1, temp2);
            intercalacao(cardNum,4,temp1,temp2,temp3,temp4);
        }catch (IOException e) {
            System.out.println("ERRO NO SORTING");
            e.printStackTrace();
        }
    }

    public static int divisao(int block,RandomAccessFile ygo, RandomAccessFile temp1, RandomAccessFile temp2){
        int len;
        byte[] ba;
        Card[] c1 = new Card[block];
        Card[] c2 = new Card[block];
        Card c;
        int cardCount = 0;
        int cont2 = 0;

        int cardNum = 0;
        int cards1 = 0;
        int cards2 = 0;
        try{
            temp1.seek(0);
            temp2.seek(0);
            cardNum = ygo.readInt();
            
            //CONTAR OS CARDS
            while(true){
                if(cards1 + cards2 < cardNum){
                    cards1 += block;
                    cards2 = cards1;
                }else if(cards1 + cards2 > cardNum){
                    cards2--;
                }else break;
            }

            //PRINTAR QUANTIDADE
            temp1.writeInt(cards1);
            temp2.writeInt(cards2);

            while(cardCount < cardNum){

                for(int i = 0; i < block; i++){
                    if(cardCount < cardNum){
                        c1[i] = new Card();
                        len = ygo.readInt();
                        ba = new byte[len];
                        ygo.read(ba);
                        c1[i].fromByteArray(ba);
                        cardCount++;
                        if(c1[i].getTombstone()) i--;
                    }
                }
                for(int i = 0; i < block; i++){
                    if(cardCount < cardNum){
                        c2[i] = new Card();
                        len = ygo.readInt();
                        ba = new byte[len];
                        ygo.read(ba);
                        c2[i].fromByteArray(ba);
                        cardCount++;
                        cont2++;
                        if(c2[i].getTombstone()) {
                            cont2--;
                            i--;
                        }
                    }
                }

                //SORT C1
                for (int i = 1; i < block; i++) {
                        c = new Card();
                        c.clonar(c1[i]);
                        int position = i;

                        while (position > 0 && c1[position - 1].getID() > c.getID()) {
                            c1[position] = c1[position - 1];
                            position--;
                        }
                        c1[position] = c;
                }
                for (int i = 0; i < block; i++) {
                        ba = c1[i].toByteArray();
                        temp1.writeInt(ba.length);
                        temp1.write(ba);
                }

                //SORT C2
                for (int i = 1; i < cont2; i++) {
                        c = new Card();
                        c.clonar(c2[i]);
                        int position = i;

                            while (position > 0 && c2[position - 1].getID() > c.getID()) {
                                c2[position] = c2[position - 1];
                                position--;
                            }

                        c2[position] = c;
                }
                for (int i = 0; i < cont2; i++) {
                        ba = c2[i].toByteArray();
                        temp2.writeInt(ba.length);
                        temp2.write(ba);
                }
                cont2 = 0;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return cardNum;
    }

    public static void intercalacao(int cardNum, int block, RandomAccessFile temp1, RandomAccessFile temp2, RandomAccessFile temp3, RandomAccessFile temp4){
        try{
            if(block < cardNum){
                temp1.seek(0);
                temp2.seek(0);
                int cont = 0;
                int len;
                byte[] ba;
                Card[] c1 = new Card[block];
                Card[] c2 = new Card[block];
                int cont1 = 0;
                int cont2 = 0;

                int cardNum1 = 0;
                int cardNum2 = 0;
                cardNum1 = temp1.readInt();
                cardNum2 = temp2.readInt();

                int cards1 = 0;
                int cards2 = 0;
                while(true){
                    if(cards1 + cards2 < cardNum){
                        cards1 += block;
                        cards2 = cards1;
                    }else if(cards1 + cards2 > cardNum){
                        cards2--;
                    }else break;
                }

                System.out.println(cards1 + " " + cards2);
                System.out.println(cardNum1 + " " + cardNum2);
                temp3.writeInt(cards1);
                temp4.writeInt(cards2);

                boolean wFile = true;
                int index1 = 0;
                int index2 = 0;

                while(cont2 < cardNum2){
                    //CRIAR OS CARDS
                    for(int i = 0; i < block; i++){
                        if(cont1 < cardNum1){
                            c1[i] = new Card();
                            len = temp1.readInt();
                            ba = new byte[len];
                            temp1.read(ba);
                            c1[i].fromByteArray(ba);
                            cont1++;
                            if(c1[i].getTombstone()){
                                i--;
                                cont1--;
                            }
                        }
                    }
                    for(int i = 0; i < block; i++){
                        if(cont2 < cardNum2){
                            c2[i] = new Card();
                            len = temp2.readInt();
                            ba = new byte[len];
                            temp2.read(ba);
                            c2[i].fromByteArray(ba);
                            cont2++;
                            if(c2[i].getTombstone()) {
                                i--;
                                cont2--;
                            }
                        }
                    }
                    if(wFile){
                        for(int i = 0; i < (block * 2); i++){
                            if(index1 < block && index2 < block){
                                if(c1[index1].getID() > c2[index2].getID()){
                                    ba = c2[index2].toByteArray();
                                    temp3.writeInt(ba.length);
                                    temp3.write(ba);
                                    index2++;
                                }else{
                                    ba = c1[index1].toByteArray();
                                    temp3.writeInt(ba.length);
                                    temp3.write(ba);
                                    index1++;
                                }
                            }else if(index2 < block){
                                ba = c2[index2].toByteArray();
                                temp3.writeInt(ba.length);
                                temp3.write(ba);
                                index2++;
                            }else if(index1 < block){
                                ba = c1[index1].toByteArray();
                                temp3.writeInt(ba.length);
                                temp3.write(ba);
                                index1++;
                            }
                        }
                        wFile = false;
                    }else {
                        for(int i = 0; i < block * 2; i++){
                            if(index1 < block && index2 < block){
                                if(c1[index1].getID() > c2[index2].getID()){
                                    ba = c2[index2].toByteArray();
                                    temp4.writeInt(ba.length);
                                    temp4.write(ba);
                                    index1++;
                                }else{
                                    ba = c1[index1].toByteArray();
                                    temp4.writeInt(ba.length);
                                    temp4.write(ba);
                                    index2++;
                                }
                            }else if(index2 < block){
                                ba = c2[index2].toByteArray();
                                temp4.writeInt(ba.length);
                                temp4.write(ba);
                                index2++;
                            }else if(index1 < block){
                                ba = c1[index1].toByteArray();
                                temp4.writeInt(ba.length);
                                temp4.write(ba);
                                index1++;
                            }
                        }
                        wFile = true;
                    }
                    index1 = index2 = 0;
                }
                intercalacao(cardNum, block * 2, temp3, temp4, temp1, temp2);
            }

        }catch (IOException e) {
            System.out.println("ERRO");
            e.printStackTrace();
        }
    }
}
