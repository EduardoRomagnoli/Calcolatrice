package RomagnoliCalcolatrice;

public class ServerStart 
{
    public static void main(String[] args) 
    {
        Server server = new Server();
        server.ConnessioneClient();
        server.MessaggiClient();
    }
}