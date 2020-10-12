package RomagnoliCalcolatrice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server 
{
    ServerSocket socket_server = null;
    Socket socket_client = null;
    String math_input = null;
    String risposta_server = null;
    BufferedReader dati_dal_client;
    DataOutputStream dati_al_client;
    ArrayList<Float> numeri = new ArrayList<>();
    ArrayList<Character> opera = new ArrayList<>();
    String lastResult="0";
    public Socket ConnessioneClient()
    {
        try 
        {
            System.out.println("Server attivo");
            socket_server=new ServerSocket(1013);
            System.out.println("Server in attesa del client");
            socket_client=socket_server.accept();
            System.out.println("Client connesso");
            dati_dal_client=new BufferedReader(new InputStreamReader(socket_client.getInputStream()));
            dati_al_client=new DataOutputStream(socket_client.getOutputStream());
        }
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
            System.out.println("Errore d'istanziamento del server");
            System.exit(1);
        }
        return(socket_client);
    }
    public void MessaggiClient()
    {
        try 
        {
            while(true)
            {
                System.out.println("In attesa del messaggio client");
                math_input = dati_dal_client.readLine();
                System.out.println("Messaggio ricevuto.");
                math_input = math_input.replaceAll("ris", lastResult);
                if(vera())
                {
                    risposta_server = calc();
                    if(risposta_server.equals("NaN"))
                    {
                        risposta_server = "Errore matematico";
                    }
                    else
                    {
                        lastResult = risposta_server;
                    }
                }
                else
                {
                    risposta_server = "Errore operazione";
                }
                System.out.println("Invio al client");
                dati_al_client.writeBytes(risposta_server+'\n');
                System.out.println("Risposta inviata.");
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Errore di comunicazione");
        }
    }
   public boolean vera()
   {
        for (int i = 0; i < math_input.length(); i++) 
        {
            switch(math_input.charAt(i))
            {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    break;
                case '.':
                    if(i == 0)
                    {
                        if(math_input.length()>1)
                        {
                            switch(math_input.charAt(i+1))
                            {
                                case '+':
                                case '-':
                                	math_input = math_input.substring(i+1);
                                    i--;
                                    break;
                                case '*':
                                case '/':
                                	math_input = math_input.substring(i+2);
                                    i--;
                                    break;
                            }
                        }
                        else{
                        	math_input = math_input+'0';
                        }
                    }
                    else if(i == math_input.length()-1)
                    {
                        switch(math_input.charAt(i-1))
                        {
                                case '+':
                                case '-':
                                case '*':
                                case '/':
                                	math_input = math_input.substring(0,i-1);
                                    break;
                            }
                    }
                    else
                    {
                        switch(math_input.charAt(i-1))
                        {
                            case '+':
                            case '-':
                            case '*':
                            case '/':
                                switch(math_input.charAt(i+1))
                                {
                                    case '+':
                                    case '-':
                                    case '*':
                                    case '/':
                                        if(i+1 == math_input.length()-1)
                                        {
                                        	math_input = math_input.substring(0,i-1);
                                        }
                                        else
                                        {
                                        	math_input = math_input.substring(0, i-1)+math_input.substring(i+1);
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                    break;
                case '+':
                case '-':
                    if(i==0)
                    {
                    	math_input='0'+math_input;
                    }
                    if(i==math_input.length()-1)
                    {
                    	math_input=math_input.substring(0, i);
                    }
                    break;
                case '*':
                case '/':
                    if(i==0)
                    {
                    	math_input = math_input.substring(1);
                        i--;
                    }
                    if(i == math_input.length()-1)
                    {
                    	math_input = math_input.substring(0, i);
                    }
                    break;
                case ',':
                	math_input.replace(',','.');
                    break;
                default:
                	math_input = math_input.subSequence(0, i)+math_input.substring(i+1);
                    i--;
                    break;
            }
            if(i<math_input.length()&&i>=0)
            {
                switch(math_input.charAt(i))
                {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        switch(math_input.charAt(i-1))
                        {
                            case '+':
                            case '-':
                            case '*':
                            case '/':
                            	math_input = math_input.subSequence(0, i-1)+math_input.substring(i);
                                i--;
                                break;
                        }
                        break;
                }
            }
        }
        if(math_input.length()<1)
        {
            return (false);
        }
        return (true);
    }
    private String calc()
    {
        String appoggio = "";
        numeri = new ArrayList<>();
        opera = new ArrayList<>();
        for (int i = 0; i < math_input.length(); i++) 
        {
            Character c=math_input.charAt(i);
            if(c<='9'&&c>='0')
            {
                appoggio+=math_input.charAt(i);
            }
            else if(c.equals('.'))
            {
                if(appoggio.indexOf('.')==-1)
                {
                    appoggio += math_input.charAt(i);
                }
            }
            else
            {
                if(appoggio.equals("."))
                {
                    appoggio="0";
                }
                numeri.add(Float.valueOf(appoggio));
                appoggio="";
                opera.add(c);
            }
        }
        if(appoggio.equals("."))
        {
            appoggio="0";
        }
        numeri.add(Float.valueOf(appoggio));
        System.out.println(numeri.toString()+'\n'+opera.toString());
        Operatori_S('/');
        Operatori_S('*');
        Operatori_S('+');
        Operatori_S('-');
        return (numeri.get(0)+"");
    }
    private void Operatori_S(Character c)
    {
        while(opera.indexOf(c)!=-1)
        {
            Float f = 0f;
            switch(c)
            {
                case '/':
                    f = numeri.get(opera.indexOf(c))/numeri.get(opera.indexOf(c)+1);
                    break;
                case '*':
                    f = numeri.get(opera.indexOf(c))*numeri.get(opera.indexOf(c)+1);
                    break;
                case '-':
                    f = numeri.get(opera.indexOf(c))-numeri.get(opera.indexOf(c)+1);
                    break;
                case '+':
                    f = numeri.get(opera.indexOf(c))+numeri.get(opera.indexOf(c)+1);
                    break;
            }
            numeri.set(opera.indexOf(c), f);
            numeri.remove(opera.indexOf(c)+1);
            opera.remove(opera.indexOf(c));
        }
    }
}