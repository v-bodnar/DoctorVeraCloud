package ua.kiev.doctorvera.services;

import ua.kiev.doctorvera.entities.Users;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by volodymyr.bodnar on 1/10/2016.
 */
@Stateless
public class SMSService implements SMSServiceLocal{
    @Override
    public void sendSMS(Users user, String message) {
        //todo
    }

    @Override
    public void sendSMS(List<Users> userList, String message) {
        //todo
    }
}
