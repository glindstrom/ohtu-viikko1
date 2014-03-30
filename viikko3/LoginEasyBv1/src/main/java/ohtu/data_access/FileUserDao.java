
package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author gabriel
 */
@Component
public class FileUserDao implements UserDao
{
    private File passwordFile;
    private List<User> users;

    @Autowired
    public FileUserDao(@Value("salasanat.txt") String fileLocation) throws IOException
    {
        passwordFile = new File(fileLocation);
        if (!passwordFile.exists()) passwordFile.createNewFile();
        users = new ArrayList();
        addAllUsersToList();
    }
    

    @Override
    public List<User> listAll()
    {
        return users;
    }

    @Override
    public User findByName(String name)
    {
       for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void add(User user)
    {
        users.add(user);
        String newLine = user.getUsername() + " " + user.getPassword() + "\n";
        try
        {
            FileWriter writer = new FileWriter(passwordFile);
            writer.append(newLine);
            writer.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addAllUsersToList() throws FileNotFoundException
    {
            Scanner scanner = new Scanner(passwordFile);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String [] tokens = line.split(" ");
                users.add(new User(tokens[0], tokens[1]));
            }
        
    }
    
}
