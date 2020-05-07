# Harkapankki

App works and when you start application first time it will initialize data file used to store all the data

When you close the application you need to change data initialization to comment or delete the initialization before starting it next time.
This happens in MainActivity. Below is code how it is supposed to look before starting 2nd time. 

        //Initialize data file(remove the comments to make the data file in phones memory)
        /*
        String asd = "[\n" +
                "  {\n" +
                "    \"accounts\": [],\n" +
                "    \"address\": \"1\",\n" +
                "    \"cards\": [],\n" +
                "    \"name\": \"1\",\n" +
                "    \"password\": \"1\",\n" +
                "    \"username\": \"1\"\n" +
                "  }\n" +
                "]";

        ReadAndWrite.write("data.json",asd,MainActivity.this);
        */
        
To use the application first use admin user with
Username: Admin123
Password: Pass123

As an admin you can create your own user and sign in with that. 
