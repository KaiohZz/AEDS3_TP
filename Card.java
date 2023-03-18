import java.util.*;
import javax.lang.model.util.ElementScanner6;
import java.io.*;
import java.time.*;
import java.time.format.*;

class No {
    //VARIÁVEIS
    private Card carta;
    No next;

    //CONSTRUTOR
    public No() {
        this.carta = new Card();
        this.next = null;
    }

    //COSNTRUTOR COM PARÂMETROS
    public No(Card c) {
        this.carta = c;
        this.next = null;
    }

    //GETTER
    public Card getCard()
    {return this.carta;}

    public No getNext()
    {return this.next;}

    //SETTERS
    public void setCard(Card c)
    {this.carta=c;}

    public void setNext(No n)
    {this.next = n;}
}

class Stack {
    //VARIÁVEIS
    DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public No topo;
    private int lastIdentifier;

    //CONSTRUTOR
    public Stack() {
        this.topo = null;
        this.lastIdentifier = -1;
    }

    //INCLUIR REGISTRO    
    public void inserir(List<String> s) {
        No temp = new No();

        //PARSE
        temp.getCard().setID((lastIdentifier+1));

        try {
            temp.getCard().setCardNumber(s.get(2).toCharArray());
        } catch (Exception e) {
            temp.getCard().setCardNumber("0000-0000".toCharArray());
        }
        
        try {
            temp.getCard().setRarity(s.get(3));
        } catch (Exception e) {
            temp.getCard().setRarity(null);
        }
        
        try {
            temp.getCard().setCardName(s.get(4));
        } catch (Exception e) {
            temp.getCard().setCardName("");
        }
        
        try {
            temp.getCard().setCardtype(s.get(6));
        } catch (Exception e) {
            temp.getCard().setCardtype(null);
        }

        try {
            temp.getCard().setAttribute(s.get(7));
        } catch (Exception e) {
            temp.getCard().setAttribute(null);
        }
        
        try {
            temp.getCard().setLevel(Double.parseDouble(s.get(9)));
        } catch (Exception e) {
            temp.getCard().setLevel(0.0);
        }
        
        temp.getCard().setTombstone(false);
        temp.getCard().setLoadDate(LocalDateTime.now().format(form));
        temp.getCard().setSize();

        temp.setNext(topo);
        topo = temp;
        temp = null;
        lastIdentifier++;
    }

    //NOVA INSERÇÃO PARA ATUALIZAÇÃO
    public void novaInserção(No n) 
    {novaInserção(n,topo);}

    public void novaInserção(No n, No t) {
        n.setNext(t);
        t = n;
        n = null;
    }

    //ATUALIZAR REGISTRO
    public void atualizar(int IdUpdate, Card carta) {
        No n = buscar(IdUpdate);

        if(n!=null && carta.getSize() <= n.getCard().getSize()) {
            n.setCard(carta);
        } else {
            deletar(n.getCard().getID());
            novaInserção(n);
        }
    }

    //DELETAR REGISTRO
    public void deletar(int IdDelete) {
        for(No n=topo;n!=null;n=n.getNext()) {
            if(IdDelete == n.getCard().getID()) {
                n.getCard().setTombstone(true);
                System.out.println("\n\nREGISTRO DELETADO COM SUCESSO\n\n");
            }
        }
    }

    //IMPRIMIR REGISTROS
    public void imprimir(Card c) {
        System.out.print("ID da carta: "); System.out.println(c.getID());
        System.out.print("Nome da carta: "); System.out.println(c.getCardName());
        System.out.print("Tipo da carta: "); System.out.println(c.getCardtype());
        System.out.print("Atributo da carta: "); System.out.println(c.getAttribute());
        System.out.print("Nível da carta: "); System.out.println(c.getLevel());
        System.out.print("Número da carta: "); System.out.println(c.getCardNumber());
        System.out.print("Raridade da carta: "); System.out.println(c.getRarity());
    }

    //BUSCAR REGISTRO
    public No buscar(int Id) {
        No found = new No();

        for(No n=topo;n!=null;n=n.getNext()) {
            if(Id == n.getCard().getID())
            {found = n;}
            else
            {found = null;}
        }

        return found;
    }
}

class Card {
    //VARIÁVEIS
    DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private char cardNumber[] = new char[15];
    private String rarity, cardName, cardType, attribute, loadDate; 
    private double level;
    private int ID, size;
    private boolean tombstone;

    //CONTAGEM DO TAMANHO DO REGISTRO
    private int Contagem (Card carta) {
        int tam = 0;

        if(carta.getRarity()!=null)
        {tam+=carta.getRarity().length();}

        if(carta.getCardName()!=null)
        {tam+=carta.getCardName().length();}

        if(carta.getCardtype()!=null)
        {tam+=carta.getCardtype().length();}

        if(carta.getAttribute()!=null)
        {tam+=carta.getAttribute().length();}

        tam+=8; //por conta do level ser um double

        tam+=9; //por conta da lápide ser um boolean

        if(carta.getLoadDate()!=null)
        {tam+=getLoadDate().length();}

        return tam;
    }
    
    //CONSTRUTOR
    public Card () {
        this.cardNumber = null;
        this.rarity = null;
        this.cardName = null;
        this.cardType = null;
        this.attribute = null;
        this.level = 0.0;
        this.tombstone = false;
        this.loadDate = null;
        this.size = 10;
    }  

    //CONTRUTOR COM PARÂMETROS
    public Card (char[] cNum, String r, String cNam, String cTyp, String att, double lvl) {
        this.ID = 0;
        this.cardNumber = cNum;
        this.rarity = r;
        this.cardName = cNam;
        this.cardType = cTyp;
        this.attribute = att;
        this.level = lvl;
        this.tombstone = false;
        this.loadDate = LocalDateTime.now().format(form);
        this.size = 10 + Contagem(this);
    }

    //CLONAGEM
    public void clonar(Card c){
        this.ID = c.getID();
        this.cardNumber = c.getCardNumber();
        this.rarity = c.getRarity();
        this.cardName = c.getCardName();
        this.cardType = c.getCardtype();
        this.attribute = c.getAttribute();
        this.level = c.getLevel();
        this.tombstone = false;
        this.loadDate = LocalDateTime.now().format(form);
        this.size = 10 + Contagem(c);
    }

    //SETTERS
    public void setID (int id) {this.ID = id;}
    public void setCardNumber (char[] cNum) {this.cardNumber = cNum;}
    public void setRarity (String r) {this.rarity = r;}
    public void setCardName (String cNam) {this.cardName = cNam;}
    public void setCardtype (String typ) {this.cardType = typ;}
    public void setAttribute (String att) {this.attribute = att;}
    public void setLevel (double lvl) {this.level = lvl;}
    public void setTombstone(Boolean ts) {this.tombstone = ts;}
    public void setLoadDate(String ldt) {this.loadDate = ldt;}
    public void setSize() {this.size = 10+Contagem(this);}

    //GETTERS
    public int getID () {return this.ID;}
    public char[] getCardNumber () {return this.cardNumber;}
    public String getRarity () {return this.rarity;}
    public String getCardName () {return this.cardName;}
    public String getCardtype () {return this.cardType;}
    public String getAttribute () {return this.attribute;}
    public double getLevel () {return this.level;}
    public Boolean getTombstone() {return this.tombstone;}
    public String getLoadDate() {return this.loadDate;}
    public int getSize() {return this.size;}

    //BYTESTREAM
    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.ID);
        dos.writeChars(this.cardNumber.toString());
        dos.writeChars(this.rarity);
        dos.writeChars(this.cardName);
        dos.writeChars(this.cardType);
        dos.writeChars(this.attribute);
        dos.writeDouble(this.level);
        dos.writeBoolean(this.tombstone);
        dos.writeUTF(this.loadDate);

        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.ID=dis.readInt();
        this.cardNumber=dis.readUTF().toCharArray();
        this.rarity=dis.readUTF();
        this.cardName=dis.readUTF();
        this.cardType=dis.readUTF();
        this.attribute=dis.readUTF();
        this.level=dis.readDouble();
        this.tombstone=dis.readBoolean();
        this.loadDate=dis.readUTF();
    }

    public static void main(String args[]) {
        List<List<String>> cardsTemp = new ArrayList<>();
        Stack cardsDef = new Stack();
        Scanner scr =  new Scanner(System.in);
        int searchId;
        int modId, delId;
        int input = -1;
        byte[] ba;
        long pos;

        System.out.println("******______YU-GI-OH!_DATABASE______******");

        while(input!=0) {
            System.out.println("INSIRA A OPÇÂO DESEJADA");
            System.out.println("[0] SAIR");
            System.out.println("[1] CARREGAR BANCO DE DADOS");
            System.out.println("[2] GERAR ARQUIVO BINÁRIO");
            System.out.println("[3] BUSCAR ID");
            System.out.println("[4] ATUALIZAR REGISTRO");
            System.out.println("[5] DELETAR REGISTRO\n");
            

            input = scr.nextInt();

            switch (input) {
                case 0:
                    
                break;
                
                case 1:
                    //LER CSV
                    try (BufferedReader br =  new BufferedReader(new FileReader("./tmp/yugioh.csv"))) {
                        
                        String line;
                        while((line = br.readLine()) != null) {
                            String[] register = line.split(",");
                            cardsTemp.add(Arrays.asList(register));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for(int i=0;i<8032;i++) {
                        if(cardsTemp.get(i)!=null) {
                            cardsDef.inserir(cardsTemp.get(i));
                        }
                    }

                    System.out.println("\n\nBANCO DE DADOS CARREGADO COM SUCESSO.\n\n");
                    
                    //LIBERAÇÃO DE MEMÓRIA
                    cardsTemp = null;
                break;

                case 2:
                    try {
                        //GERAÇÃO DO AQRUIVO
                        RandomAccessFile arq = new RandomAccessFile("./tmp/yugioh", "rw");

                        //GRAVAÇÃO
                        for(No i=cardsDef.topo;i!=null;i=i.getNext()) {
                            pos=arq.getFilePointer();
                            System.out.println("Registro iniciado na posição: "+pos);
                            ba = i.getCard().toByteArray();
                            arq.writeInt(ba.length); //Tamano do registro em bytes
                            arq.write(ba);
                            //Heap.ordenar();
                        }

                        arq.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    System.out.println("\n\nARQUIVOS GERADOS COM SUCESSO.\n\n");

                break;

                case 3:
                    System.out.println("Insira o ID da carta a ser procurada\n");
                    searchId = scr.nextInt();
                    if(cardsDef.buscar(searchId).getCard() != null)
                    {cardsDef.imprimir(cardsDef.buscar(searchId).getCard());}
                    else
                    {System.out.println("Não foi possível recuperar a carta\n");}
                break;

                case 4:
                    Card carta = new Card();

                    System.out.println("\nInsira o ID da carta que será modificada\n");
                    modId = scr.nextInt();
                    System.out.println("\nInsira o novo número da carta (máximo 10 caracteres)\n");
                    String nextln = scr.nextLine();
                    carta.setCardNumber(nextln.toCharArray());
                    System.out.println("\nInsira a nova raridade da carta\n");
                    carta.setRarity(scr.nextLine());
                    System.out.println("\nInsira o novo nome da carta\n");
                    carta.setCardName(scr.nextLine());
                    System.out.println("\nInsira o novo tipo da carta\n");
                    carta.setCardtype(scr.nextLine());
                    System.out.println("\nInsira o novo atributo da carta\n");
                    carta.setAttribute(scr.nextLine());
                    System.out.println("\nInsira o novo nível da carta (Double)\n");
                    carta.setLevel(scr.nextDouble());

                    cardsDef.atualizar(modId, carta);
                break;

                case 5:
                    System.out.println("Insira o ID que deseja deletado\n");
                    delId = scr.nextInt();
                    cardsDef.deletar(delId);
                break;
            
                default:
                    break;
            }
        }

        scr.close();
    }
}