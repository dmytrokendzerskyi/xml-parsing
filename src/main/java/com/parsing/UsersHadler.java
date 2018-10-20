package com.parsing;

import javafx.scene.input.KeyCode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class UsersHadler extends DefaultHandler {


    private Users.User user;
    private Users.User.Roles.Role role;
    private List<Users.User> userList = new ArrayList<>();

    protected boolean bRole;
    protected boolean bId;
    protected boolean bName;
    protected boolean bAge;
    protected boolean bGender;
    protected boolean bActive;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(qName.equalsIgnoreCase("user")){
            user = new Users.User();
        }else if(qName.equalsIgnoreCase("role")){
            if(user.roles == null){
                user.roles = new Users.User.Roles();
            }
            role = new Users.User.Roles.Role();
            bRole = true;
        }else if(qName.equalsIgnoreCase("id")){
            bId = true;
        } else if(qName.equalsIgnoreCase("name")){
            bName = true;
        } else if(qName.equalsIgnoreCase("age")){
            bAge = true;
        } else if(qName.equalsIgnoreCase("gender")){
            bGender = true;
        } else if(qName.equalsIgnoreCase("active")) {
            bActive = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("user")){
            userList.add(user);
        }
        if(qName.equalsIgnoreCase("role")){
            user.roles.getRole().add(role);
            bRole = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(bId){
            if(bRole) {
                role.setId(Integer.valueOf(new String(ch, start, length)));
            }else{
                user.setId(Integer.valueOf(new String(ch, start, length)));
            }
            bId = false;
        } else if(bName){
            if(bRole){
                role.setName(new String(ch,start,length));
            }else {
                user.setName(new String(ch, start, length));
            }
            bName = false;
        }else if(bAge){
            user.setAge(Integer.valueOf(new String(ch,start,length)));
            bAge = false;
        }else if(bGender){
            user.setGender(Gender.fromValue(new String(ch,start,length)));
            bGender = false;
        }else if(bActive){
            user.setActive(Boolean.getBoolean(new String(ch,start,length)));
            bActive = false;
        }
    }

    public List<Users.User> getUserList() {
        return userList;
    }

    public void setUserList(List<Users.User> userList) {
        this.userList = userList;
    }
}
