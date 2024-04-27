//import java.io.*;
//import java.net.*;
//import java.sql.SQLOutput;
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class ServerMain implements Runnable {
//
//    public static Object lock = new Object();
//    public static final String USER_LIST = "data/Users";
//    static ArrayList<User> users;
//    private Socket socket;
//    public ServerMain(Socket socket)
//    {
//        this.socket = socket;
//    }
//
//    public void run()
//    {
//        try
//        {
//            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//
//            User user = null;
//
//            while (true) {
//                String str = (String) in.readObject();
//                if (str.equals("CREATE_USER")) {
//                    User userToAdd = (User) in.readObject();
//                    /*
//                    String email = (String) in.readObject();
//                    String password = (String) in.readObject();
//                    String accountType = (String) in.readObject();
//                     */
//
//                    //User test = new User(email, password);
//                    synchronized (lock)
//                    {
//                        boolean exists = users.contains(userToAdd);
//                        if (exists)
//                        {
//                            out.writeObject("ERROR_EMAIL_TAKEN");
//                        }
//                        else {
//                            if (userToAdd != null) {
//                                User u = (User)userToAdd;
//                                System.out.println("This is a User: " + u.toString());
//                                users.add(u);
//                                IO.write(users, USER_LIST);
//                            }
//                            else{
//                                throw new Exception();
//                            }
//                        }
//                        out.writeObject("ACCOUNT_CREATION_SUCCESS");
//                    }
//                } else if(str.equals("LOGIN")) {
//                    String email = (String) in.readObject();
//                    String password = (String) in.readObject();
//
////                    User test = new User(email, password);
//
//                    synchronized (lock)
//                    {
//                        for (User u : users) {
//                            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
//                                user = u;
//                            }
//                        }
//                        if (user != null && user.getPassword().equals(password)) {
//                            out.writeObject("LOGIN_SUCCESS_CUSTOMER");
//                            break;
//                        } else {
//                            out.writeObject("LOGIN_FAILURE");
//                            user = null;
//                        }
//                    }
//                }
//                else {
//                    throw new Exception();
//                }
//            }
//            //user is now logged in, can now start taking requests to do stuff
//            while(true)
//            {
//                if (user != null) {
//                    //User database Option
//                    boolean cont = true;
//                    while (cont) { // this is the main loop for the customer, which will continue until the user decides to exit
//                        int userChoice;
//                        int userRequest = Integer.parseInt((String) in.readObject());
//                        // this is the menu for the customer, from which they can choose what they want to do
//                        switch (userRequest) { // this is the switch statement for the customer menu
//                            case 1:
//                                System.out.println("Enter your search term (leave blank to view all):");
//                                String searchTerm = (String) in.readObject();
//                                //Users searching for other users
//                                ArrayList<User> searchResults = new ArrayList<>();
//                                String userResults = "";
//
//                                userChoice = Integer.parseInt((String) in.readObject());
//
//                                for (Product p : results) {
//                                    out.writeObject("Product ID: " + productId + "\n");
//                                    out.writeObject(p);
//                                    out.writeObject("--------------------");
//                                    productId++;
//                                }
//
//                                //customer chooses product by ID
//                                int choiceProduct = Integer.parseInt((String) in.readObject());
//
//                                //creating a new object of the chosen product
//                                int temp = 1;
//                                Product productChosen = new Product(results.get(choiceProduct - 1));
//
//
//                                //customer chooses quantity
//
//                                int quantity = Integer.parseInt((String) in.readObject());
//                                productChosen.setQuantity(quantity);
//
//                                //adding the product to the cart
//                                boolean inShoppingCart = false;
//                                for (int i = 0; i < ((Customer) user).getShoppingCart().size(); i++) {
//                                    if (((Customer) user).getShoppingCart().get(i).equalsProduct(productChosen)) {
//                                        out.writeObject("Product is already in cart");
//                                        inShoppingCart = true;
//                                        break;
//                                    }
//                                }
//
//                                // if the product is not already in the cart, it will be added
//                                if (!inShoppingCart) {
//                                    ((Customer) user).getShoppingCart().add(productChosen);
//                                    out.writeObject("Product added to cart");
//                                }
//                                break;
//                            case 2: // this is the case for the customer to view their purchase history
//                                for (int i = 0; i < ((Customer) user).getPurchases().size(); i++) {
//                                    out.writeObject(((Customer) user).getPurchases().get(i) + "\n");
//                                }
//                                break;
//                            case 3: //here the customer can view the marketplace
//
//                                //first the general information about all products is displayed
//                                productId = 1;
//                                for (Seller seller : sellers) {
//                                    for (Store store : seller.getStores()) {
//                                        for (Product product : store.getProducts()) {
//                                            out.writeObject("Product ID: " + productId + "\n");
//                                            out.writeObject("Name: " + product.getName() +
//                                                    " | Store: " + product.getStore() +
//                                                    " | Price: " + String.format("%.2f", product.getPrice()) + "$");
//                                            out.writeObject("--------------------");
//                                            productId++;
//                                        }
//                                    }
//                                }
//
//                                // then the customer can choose a product by ID to view more information about it
//                                int finalProductId = productId;
//                                int choiceProductDetail = Integer.parseInt((String) in.readObject());
//                                temp = 1;
//                                for (Seller seller : sellers) {
//                                    for (int i = 0; i < seller.getStores().size(); i++) {
//                                        for (int j = 0; j < seller.getStores().get(i).getProducts().size(); j++) {
//                                            if (choiceProductDetail == temp) {
//                                                out.writeObject(seller.getStores().get(i).getProducts().get(j).toString());
//                                                break;
//                                            }
//                                            temp++;
//                                        }
//                                    }
//                                }
//                                break;
//                            case 4: // this is the case for the customer to clear their purchase history
//                                ((Customer) user).setPurchases(new ArrayList<>());
//                                out.writeObject("Your purchase history has been cleared");
//                                break;
//                            case 5:
//                                String filename = (String) in.readObject();
//                                ((Customer) user).exportHistoryCSV(filename);
//                                break;
//                            case 6: // this is the case for the customer to check out
//
//                                //first the cart is printed to the user
//                                out.writeObject("Your cart:\n");
//                                double totalPrice = 0;
//                                for (Product product : ((Customer) user).getShoppingCart()) {
//                                    totalPrice += product.getPrice() * product.getQuantity();
//                                    out.writeObject(product.toString() + "\n");
//                                }
//                                //the total price is displayed
//                                out.writeObject("Total price: " + String.format("%.2f", totalPrice) + "$\n");
//                                out.writeObject("Do you want to checkout?");
//                                int checkoutChoice = Integer.parseInt((String) in.readObject());
//                                // if the user checks out, the cart is cleared and the purchase is added to the history
//                                if (checkoutChoice == 0) {
//                                    Customer c = (Customer) user;
//                                    c.getPurchases().addAll(c.getShoppingCart());
//                                    //  ((Customer) user).setPurchases(((Customer) user).getShoppingCart());
//                                    for (int i = 0; i < ((Customer) user).getShoppingCart().size(); i++) {
//                                        for (Seller seller : sellers) {
//                                            for (Store store : seller.getStores()) {
//                                                if (store.getName().equals(c.getShoppingCart().get(i).getStore())) {
//                                                    for (Product product : store.getProducts()) {
//                                                        if (product.equalsProduct(c.getShoppingCart().get(i))) {
//                                                            product.setQuantity(product.getQuantity() -
//                                                                    c.getShoppingCart().get(i).getQuantity());
//                                                            product.incrementNumSales();
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                    ((Customer) user).setShoppingCart(new ArrayList<>());
//                                    out.writeObject("Checkout successful!");
//                                } else {
//                                    out.writeObject("Checkout cancelled");
//                                }
//                                break;
//                            case 7: // this is the case for the customer to clear their cart
//                                ((Customer) user).setShoppingCart(new ArrayList<>());
//                                out.writeObject("Your cart has been cleared");
//                                break;
//                            case 8:
//                                /**
//                                 * Statistics for Customers
//                                 */
//                                boolean sorting = true;
//                                System.out.println("Statistics\n**********************************************");
//                                ArrayList<String> storeSales = new ArrayList<>();
//                                int storeSalesTotal = 0;
//                                for (Seller seller : sellers) {
//                                    for (int j = 0; j < seller.getStores().size(); j++) {
//                                        Store store = seller.getStores().get(j);
//                                        for (int k = 0; k < store.getProducts().size(); k++) {
//                                            Product product = store.getProducts().get(k);
//                                            storeSalesTotal += product.getNumSales();
//                                        }
//                                        storeSales.add(store.getName() + "'s Total Products Sold: " + storeSalesTotal);
//                                        storeSalesTotal = 0;
//                                    }
//                                }
//
//                                ArrayList<String> purchaseStats = new ArrayList<>();
//                                int purchases = 0;
//                                for (int i = 0; i < sellers.size(); i++) {
//                                    for (int j = 0; j < sellers.get(i).getStores().size(); j++) {
//                                        Store store = sellers.get(i).getStores().get(j);
//                                        for (int k = 0; k < store.getProducts().size(); k++) {
//                                            Product product = store.getProducts().get(k);
//                                            for (int l = 0; l < ((Customer) user).getPurchases().size(); l++) {
//                                                Product purchase = ((Customer) user).getPurchases().get(l);
//                                                if (purchase.equalsProduct(product)) {
//                                                    purchases++;
//                                                }
//                                            }
//                                        }
//                                        purchaseStats.add("Purchases from " + store.getName() + ": " + purchases);
//                                        purchases = 0;
//                                    }
//                                }
//
//                                int response = Integer.parseInt((String) in.readObject());
//
//                                while (sorting) {
//                                    switch (response) {
//                                        case 1:
//                                            String ans = "";
//                                            for (String storeSale : storeSales) {
//                                                ans += storeSale + "\n";
//                                            }
//                                            out.writeObject(ans);
//                                            System.out.println(ans);
//                                            sorting = false;
//                                            break;
//
//                                        case 2:
//                                            String ans2 = "";
//                                            ArrayList<String> inter = (ArrayList<String>) storeSales.clone();
//                                            storeSales.sort(String.CASE_INSENSITIVE_ORDER);
//                                            for (String s : storeSales) {
//                                                ans2 += s;
//                                            }
//
//                                            out.writeObject(ans2);
//                                            storeSales = (ArrayList<String>) inter.clone();
//                                            sorting = false;
//                                            break;
//
//                                        default:
//                                            out.writeObject("Not A Valid Option!");
//                                    }
//                                }
//
//                                response = Integer.parseInt((String) in.readObject());
//
//                                sorting = true;
//                                while (sorting) {
//                                    switch (response) {
//                                        case 1:
//                                            for (String storeSale : purchaseStats) {
//                                                out.writeObject(storeSale);
//                                            }
//                                            out.writeObject("**********************************************");
//                                            sorting = false;
//                                            break;
//
//                                        case 2:
//                                            ArrayList<String> inter = (ArrayList<String>) purchaseStats.clone();
//                                            purchaseStats.sort(String.CASE_INSENSITIVE_ORDER);
//                                            for (String s : purchaseStats) {
//                                                out.writeObject(s);
//                                            }
//                                            out.writeObject("**********************************************");
//                                            purchaseStats = (ArrayList<String>) inter.clone();
//                                            sorting = false;
//                                            break;
//
//                                        default:
//                                            out.writeObject("Not A Valid Option!");
//                                    }
//                                }
//                                break;
//                        }
//                        IO.write(customers, CUSTOMERS_PATH);
//                        IO.write(sellers, SELLERS_PATH);
//                        userChoice = Integer.parseInt((String) in.readObject());
//                        if (userChoice == 2) {
//                            cont = false;
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//
//        users = IO.read(USER_LIST);
//
//        /*
//        System.out.println(customers);
//        System.out.println(sellers);
//        */
//
//        ServerSocket serverSocket;
//        try {
//            serverSocket = new ServerSocket(4242);
//        }
//        catch(Exception e)
//        {
//            System.out.println("There was an error creating serverSocket");
//            e.printStackTrace();
//            return;
//        }
//
//        while (true) {
//            try {
//                System.out.println("Waiting for the client to connect...");
//                Socket socket = serverSocket.accept();
//                System.out.println("Client connected!");
//
//                Thread toRun = new Thread(new ServerMain(socket));
//                toRun.start();
//            } catch (Exception e) {
//                System.out.println("There was an error");
//                e.printStackTrace();
//            }
//        }
//    }
//}
