package RomagnoliCalcolatrice;


public class clientStart 
{
    public static void main(String[] args) 
    {
        client client1 = new client();
        client1.Serverconn();
        client1.SendOpe();
    }
}