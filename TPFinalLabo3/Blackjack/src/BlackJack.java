
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Carta{
        String valor;
        String tipo;

        Carta(String valor, String tipo){
            this.valor = valor;
            this.tipo = tipo;
        }

        public String toString(){
            return valor + "-" + tipo;
        }

        public int getValor(){
            if("AJQK".contains(valor)){ // A J Q K 
                if(valor == "A"){
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(valor); // 2-10
        }

        public boolean esAs(){
            return valor == "A";
        }

        public String getImagePath(){
            return "./cards/" + toString() + ".png";
        }
    }
    
    ArrayList<Carta> mazo;
    Random random = new Random(); //mezclar mazo

    //Dealer

    Carta hiddenCarta;
    ArrayList<Carta> manoDealer;
    int dealerSuma;
    int contadorAceDealer;

    //Jugador

    ArrayList<Carta> manoJugador;
    int sumaJugador;
    int contadorAceJugador;

    //Ventana

    int anchoTablero = 500;
    int altoTablero = anchoTablero;

    int cartaAncho = 90; // El radio es 1/1.4
    int cartaAlto = 126;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try{
            //Dibuja carta oculta
            Image hiddenCartaImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
            if (!stayBoton.isEnabled()) {
                hiddenCartaImg = new ImageIcon(getClass().getResource(hiddenCarta.getImagePath())).getImage();
            }
            g.drawImage(hiddenCartaImg, 10, 20, cartaAncho, cartaAlto, null);

            //Dibuja mano dealer
            for(int i=0; i < manoDealer.size();i++){
                Carta carta = manoDealer.get(i);
                Image imgCarta = new ImageIcon(getClass().getResource(carta.getImagePath())).getImage();
                g.drawImage(imgCarta, cartaAncho + 15 + (cartaAncho + 5)* i , 20, cartaAncho, cartaAlto, null); 
            }
            //Dibuja mano jugador
            for(int i = 0; i < manoJugador.size(); i++){
                Carta carta = manoJugador.get(i);
                Image imgCarta = new ImageIcon(getClass().getResource(carta.getImagePath())).getImage();
                g.drawImage(imgCarta, 10 + (cartaAncho + 5)* i , 250, cartaAncho, cartaAlto, null); 
            }    

            if(!stayBoton.isEnabled()){
                dealerSuma = reduceDealerAce();
                sumaJugador = reducePlayerAce();
                System.out.println("STAY: ");
                System.out.println(dealerSuma);
                System.out.println(sumaJugador);

                String mensaje = "";
                if(sumaJugador > 21){
                    mensaje = "¡Perdiste!";
                }
                else if(dealerSuma > 21){
                    mensaje = "¡Ganaste!";
                }
                //Ambos, vos y el dealer <= 21  
                else if(sumaJugador == dealerSuma){
                    mensaje = "¡Empate!";
                }
                else if(sumaJugador > dealerSuma){
                    mensaje = "¡Ganaste!";
                }
                else if(sumaJugador < dealerSuma){
                    mensaje = "¡Perdiste!";
                }
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.WHITE);
                g.drawString(mensaje, 180, 200);
            }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    JPanel botonPanel = new JPanel();
    JButton hitBoton = new JButton("Hit");
    JButton stayBoton = new JButton("Stay");



    BlackJack(){
        iniciarJuego();
        
        frame.setVisible(true);
        frame.setSize(anchoTablero, altoTablero);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);
         
        hitBoton.setFocusable(false);
        botonPanel.add(hitBoton);
        stayBoton.setFocusable(false);
        botonPanel.add(stayBoton);
        frame.add(botonPanel, BorderLayout.SOUTH);

        hitBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Carta carta = mazo.remove(mazo.size()-1);
                sumaJugador += carta.getValor();
                contadorAceJugador += carta.esAs() ? 1 : 0;
                manoJugador.add(carta);
                if(reducePlayerAce() > 21){ //A + 2 + J --> 1 + 2 + J
                    hitBoton.setEnabled(false);
                }

                gamePanel.repaint();
            }
        });

        stayBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitBoton.setEnabled(false);
                stayBoton.setEnabled(false);

                while (dealerSuma < 17) {
                    Carta carta = mazo.remove(mazo.size()-1);
                    dealerSuma += carta.getValor();
                    contadorAceDealer += carta.esAs() ? 1 : 0;
                    manoDealer.add(carta);
                }
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void iniciarJuego(){
        //mazo
        hacerMazo();
        mezclarMazo();

        //dealer

        manoDealer = new ArrayList<Carta>();
        dealerSuma = 0;
        contadorAceDealer = 0;

        hiddenCarta = mazo.remove(mazo.size()-1); //quita la ultima carta del mazo
        dealerSuma += hiddenCarta.getValor();
        contadorAceDealer += hiddenCarta.esAs() ? 1 : 0;

        Carta carta = mazo.remove(mazo.size()-1);
        dealerSuma += carta.getValor();
        contadorAceDealer += carta.esAs() ? 1 : 0;
        manoDealer.add(carta);

        System.out.println("DEALER:");
        System.out.println(hiddenCarta);
        System.out.println(manoDealer);
        System.out.println(dealerSuma);
        System.out.println(contadorAceDealer);

        //jugador

        manoJugador = new ArrayList<Carta>();
        sumaJugador = 0;
        contadorAceJugador = 0;
         
        for(int i=0; i < 2; i++){
            carta = mazo.remove(mazo.size()-1);
            sumaJugador += carta.getValor();
            contadorAceJugador += carta.esAs() ? 1 : 0;
            manoJugador.add(carta);
        }

        System.out.println("PLAYER: ");
        System.out.println(manoJugador);
        System.out.println(sumaJugador);
        System.out.println(contadorAceJugador);
          

    }

    public void hacerMazo(){
        mazo = new ArrayList<Carta>();

        String[] valores = {"A", "2","3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] tipos = {"C", "D", "H", "S"};

        for(int i=0; i < tipos.length; i++){
            for(int j=0; j < valores.length; j++){
                Carta carta = new Carta(valores[j], tipos[i]);
                mazo.add(carta);
            }
        }

        System.out.println("Armado de mazo:");
        System.out.println(mazo);
    }

    public void mezclarMazo(){
        for(int i=0; i < mazo.size(); i++){
            int j = random.nextInt(mazo.size());
            Carta actualCarta = mazo.get(i);
            Carta randomCarta = mazo.get(j);
            mazo.set(i, randomCarta);
            mazo.set(j, actualCarta);
        }
        System.out.println("Despues de mezclar");
        System.out.println(mazo);
    }

    public int reducePlayerAce(){
        while (sumaJugador > 21 && contadorAceJugador > 0) {
            sumaJugador -= 10;
            contadorAceJugador -= 1;
        }
        return sumaJugador;
    }

    public int reduceDealerAce(){
        while (dealerSuma > 21 && contadorAceDealer > 0) {
            dealerSuma -= 10;
            contadorAceDealer -= 1;
        }
        return dealerSuma;
    }
    
}

