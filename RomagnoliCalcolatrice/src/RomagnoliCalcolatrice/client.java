package RomagnoliCalcolatrice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class client 
{
    String nome_server="localhost";
    int porta_server=1013;
    Socket socket;
    BufferedReader client_input;
    String math_input;
    DataOutputStream dati_al_server;
    BufferedReader dati_dal_server;
    String risultato;
    public Socket Serverconn()
    {
        try 
        {
            client_input = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(nome_server,porta_server);
            dati_al_server = new DataOutputStream(socket.getOutputStream());
            dati_dal_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connessione effettuata");
        }
        catch(UnknownHostException e)
        {
            System.out.println(e.getMessage());
            System.err.println("Host non valido");
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore di connessione");
            System.exit(1);
        }
        return(socket);
    }
    public void SendOpe()
    {
        while(true)
        {
            try 
            {
                System.out.println("Digitare l'operazione da eseguire");
                math_input = client_input.readLine();
                risultato = dati_dal_server.readLine();
                System.out.println("Risultato: " + risultato);
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Errore di comunicazione client server");
                System.exit(1);
            }
        }
    }
}