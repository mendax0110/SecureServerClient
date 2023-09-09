public class Main 
{
    public static void main(String[] args) 
    {
        // Start the HTTPS server
        Thread serverThread = new Thread(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    SecureHttpsServer.main(null);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });

        serverThread.start();

        // Delay to ensure the server is up and running
        try 
        {
            Thread.sleep(2000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        // Start the HTTPS client
        try 
        {
            SimpleHttpsClient.main(null);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
